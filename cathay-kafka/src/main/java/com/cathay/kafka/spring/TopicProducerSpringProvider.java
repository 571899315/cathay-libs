package com.cathay.kafka.spring;

import java.io.Serializable;
import java.util.Properties;
import java.util.Set;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.cathay.common.util.NodeNameHolder;
import com.cathay.common.util.ResourceUtils;
import com.cathay.kafka.KafkaConst;
import com.cathay.kafka.message.DefaultMessage;
import com.cathay.kafka.partiton.DefaultPartitioner;
import com.cathay.kafka.producer.DefaultTopicProducer;
import com.cathay.kafka.producer.TopicProducer;
import com.cathay.kafka.producer.handler.SendCounterHandler;
import com.cathay.kafka.producer.handler.SendErrorDelayRetryHandler;
import com.cathay.kafka.serializer.KyroMessageSerializer;
import com.cathay.kafka.serializer.ZKStringSerializer;

/**
 * 消息发布者集成spring封装对象
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2016年5月25日
 */
public class TopicProducerSpringProvider implements InitializingBean, DisposableBean {
	
	private static final Logger log = LoggerFactory.getLogger(TopicProducerSpringProvider.class);

    private TopicProducer producer;

    /**
     * kafka配置
     */
    private Properties configs;
    
    //默认是否异步发送
    private boolean defaultAsynSend = true;
    
    private String producerGroup;
    
    private boolean monitorEnabled = false;
    
    //延迟重试次数
    private int delayRetries = 3;
    
    //环境路由
    private String routeEnv;
    
    private ZkClient zkClient;
    
    private boolean consumerAckEnabled = true;
    
    @Override
    public void afterPropertiesSet() throws Exception {

        Validate.notEmpty(this.configs, "configs is required");
        
        routeEnv = StringUtils.trimToNull(ResourceUtils.getProperty(KafkaConst.PROP_ENV_ROUTE));
        
        if(routeEnv != null)log.info("current route Env value is:",routeEnv);
      //移除错误的或者未定义变量的配置
        Set<String> propertyNames = configs.stringPropertyNames();
        for (String propertyName : propertyNames) {
			String value = configs.getProperty(propertyName);
			if(StringUtils.isBlank(value) || value.trim().startsWith("$")){
				configs.remove(propertyName);
				log.warn("remove prop[{}],value is:{}",propertyName,value);
			}
		}
        
        if(!configs.containsKey(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG)){
        	configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // key serializer
        }
        
        if(!configs.containsKey(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG)){
        	configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KyroMessageSerializer.class.getName());
        }
        
        if(!configs.containsKey(ProducerConfig.PARTITIONER_CLASS_CONFIG)){
        	configs.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DefaultPartitioner.class.getName()); 
        }
        
        //默认重试一次
        if(!configs.containsKey(ProducerConfig.RETRIES_CONFIG)){
        	configs.put(ProducerConfig.RETRIES_CONFIG, "1"); 
        }
        
        if(!configs.containsKey(ProducerConfig.COMPRESSION_TYPE_CONFIG)){
        	configs.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy"); 
        }
        
        if(!configs.containsKey("client.id")){
			configs.put("client.id", (producerGroup == null ? "" : "_"+producerGroup) + NodeNameHolder.getNodeId());
		}

        KafkaProducer<String, Object> kafkaProducer = new KafkaProducer<String, Object>(configs);

        String monitorZkServers = ResourceUtils.getProperty("kafka.zkServers");
        if(StringUtils.isNotBlank(monitorZkServers)){
        	zkClient = new ZkClient(monitorZkServers, 10000, 5000, new ZKStringSerializer());
        }
        this.producer = new DefaultTopicProducer(kafkaProducer,zkClient,consumerAckEnabled);

        //hanlder
        if(monitorEnabled){    
			Validate.notBlank(producerGroup,"enable producer monitor property[producerGroup] is required");
			Validate.notNull(zkClient, "enable producer monitor property[kafka.zkServers] is required");
			
			this.producer.addEventHandler(new SendCounterHandler(producerGroup,zkClient));
		}
        if(delayRetries > 0){
        	this.producer.addEventHandler(new SendErrorDelayRetryHandler(producerGroup,kafkaProducer, delayRetries));
        }
    }

    @Override
    public void destroy() throws Exception {
        this.producer.close();
    }

    /**
     * kafka配置
     *
     * @param configs kafka配置
     */
    public void setConfigs(Properties configs) {
        this.configs = configs;
    }

	public void setDefaultAsynSend(boolean defaultAsynSend) {
		this.defaultAsynSend = defaultAsynSend;
	}

	public void setProducerGroup(String producerGroup) {
		this.producerGroup = producerGroup;
	}
	

	public void setMonitorEnabled(boolean monitorEnabled) {
		this.monitorEnabled = monitorEnabled;
	}

	public void setDelayRetries(int delayRetries) {
		this.delayRetries = delayRetries;
	}
	
	public void setConsumerAckEnabled(boolean consumerAckEnabled) {
		this.consumerAckEnabled = consumerAckEnabled;
	}

	/**
	 * 发送kafka消息（发送模式由配置defaultAsynSend参数确定，如果没有配置则为异步发送）
	 * @param topicName
	 * @param message
	 * @return
	 */
	public boolean publish(final String topicName, final DefaultMessage message) {
		return publish(topicName, message, defaultAsynSend);
	}
	
	/**
	 * 发送kafka消息（可选择是否异步发送）
	 * @param topicName
	 * @param message
	 * @param asynSend 是否异步发送
	 * @return
	 */
	public boolean publish(String topicName, DefaultMessage message,boolean asynSend){
		if(routeEnv != null)topicName = routeEnv + "." + topicName;
		return producer.publish(topicName, message,asynSend);
	}
	
	/**
	 * 发送kafka消息（消息体不经过包装，以兼容旧的未配套使用的consumer端消费）
	 * @param topicName
	 * @param message
	 * @return
	 */
	public boolean publishNoWrapperMessage(final String topicName, final Serializable message) {
		return publishNoWrapperMessage(topicName, message, defaultAsynSend);
	}
	
	/**
	 * 发送kafka消息（消息体不经过包装，以兼容旧的为配套使用的consumer）
	 * @param topicName
	 * @param message
	 * @param asynSend 是否异步发送
	 * @return
	 */
	public boolean publishNoWrapperMessage(String topicName, Serializable message, boolean asynSend) {
		return publishNoWrapperMessage(topicName, null, message, asynSend);
	}
	
	/**
	 * 
	 * @param topicName
	 * @param msgId
	 * @param message
	 * @param asynSend
	 * @return
	 */
	public boolean publishNoWrapperMessage(String topicName,String msgId, Serializable message, boolean asynSend) {
		DefaultMessage defaultMessage = new DefaultMessage(msgId,message).sendBodyOnly(true);
		if(routeEnv != null)topicName = routeEnv + "." + topicName;
		return producer.publish(topicName, defaultMessage,asynSend);
	}

}
