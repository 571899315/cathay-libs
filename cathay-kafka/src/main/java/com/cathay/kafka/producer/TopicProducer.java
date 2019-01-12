package com.cathay.kafka.producer;

import com.cathay.kafka.message.DefaultMessage;
import com.cathay.kafka.producer.handler.ProducerEventHandler;

/**
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2016年6月14日
 */
public interface TopicProducer {
	
	void addEventHandler(ProducerEventHandler eventHandler);
	/**
	 * 发送消息（可选择发送模式）
	 * @param topic
	 * @param message
	 * @param asynSend 是否异步发送
	 * @return
	 */
	boolean publish(final String topic, final DefaultMessage message,boolean asynSend);
	
	void close();
}
