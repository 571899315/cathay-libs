/**
 * 
 */
package com.cathay.springboot.starter.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cathay.kafka.consumer.hanlder.OffsetLogHanlder;
import com.cathay.kafka.consumer.hanlder.RetryErrorMessageHandler;
import com.cathay.kafka.spring.TopicConsumerSpringProvider;

/**
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2017年3月28日
 */
@Configuration
@EnableConfigurationProperties(KafkaConsumerProperties.class)
@ConditionalOnClass(TopicConsumerSpringProvider.class)
@ConditionalOnProperty(name="kafka.bootstrap.servers")
public class DelegateKafkaConsumerConfiguration {

	@Autowired
	private KafkaConsumerProperties properties;
	@Autowired(required=false)
    private OffsetLogHanlder offsetLogHanlder;
	@Autowired(required=false)
    private RetryErrorMessageHandler retryErrorMessageHandler;

	@Bean
	public TopicConsumerSpringProvider comsumerProvider() {

		TopicConsumerSpringProvider bean = new TopicConsumerSpringProvider();
		bean.setConfigs(properties.getConfigs());
		bean.setIndependent(properties.isIndependent());
		bean.setProcessThreads(properties.getProcessThreads());
		bean.setUseNewAPI(properties.isUseNewAPI());
		bean.setScanPackages(properties.getScanPackages());
		bean.setOffsetLogHanlder(offsetLogHanlder);
		bean.setRetryErrorMessageHandler(retryErrorMessageHandler);

		return bean;
	}

	
	
}
