/**
 * 
 */
package com.cathay.springboot.starter.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cathay.cache.local.EhCacheLevel1CacheProvider;
import com.cathay.cache.local.GuavaLevel1CacheProvider;
import com.cathay.cache.local.Level1CacheSupport;
import com.cathay.cache.redis.JedisProviderFactoryBean;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2017年3月28日
 */
@Configuration
@EnableConfigurationProperties({CacheProperties.class,Level1CacheProperties.class})
@ConditionalOnClass(JedisProviderFactoryBean.class)
@ConditionalOnProperty(name="cathay.cache.mode")
public class DelegateCacheConfiguration {

	@Autowired
	private CacheProperties cacheProperties;
	
	@Autowired
	private Level1CacheProperties level1CacheProperties;

	
	@Bean
	public JedisProviderFactoryBean jedisProviderFactory(@Qualifier("jedisPoolConfig") JedisPoolConfig config) {

		JedisProviderFactoryBean bean = new JedisProviderFactoryBean();
		bean.setDatabase(cacheProperties.getDatabase());
		bean.setJedisPoolConfig(config);
		bean.setMode(cacheProperties.getMode());
		bean.setServers(cacheProperties.getServers());
		bean.setPassword(cacheProperties.getPassword());
		bean.setMasterName(cacheProperties.getMasterName());
		bean.setGroup(cacheProperties.getGroupName());
		return bean;
	}

	@Bean(name = "jedisPoolConfig")
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(cacheProperties.getMaxPoolSize());
		config.setMaxIdle(cacheProperties.getMaxPoolIdle());
		config.setMinIdle(cacheProperties.getMinPoolIdle());
		config.setMaxWaitMillis(cacheProperties.getMaxPoolWaitMillis());
		return config;
	}
	
	@Bean
	public Level1CacheSupport level1CacheSupport(){
		Level1CacheSupport support = new Level1CacheSupport();
		if(level1CacheProperties != null){			
			support.setBcastScope(level1CacheProperties.getBcastScope());
			support.setBcastServer(level1CacheProperties.getBcastServer());
			support.setCacheNames(level1CacheProperties.getCacheNames());
			support.setDistributedMode(level1CacheProperties.isDistributedMode());
			support.setPassword(level1CacheProperties.getPassword());
			if("guavacache".equals(level1CacheProperties.getCacheProvider())){
				GuavaLevel1CacheProvider cacheProvider = new GuavaLevel1CacheProvider();
				cacheProvider.setMaxSize(level1CacheProperties.getMaxCacheSize());
				cacheProvider.setTimeToLiveSeconds(level1CacheProperties.getTimeToLiveSeconds());
				support.setCacheProvider(cacheProvider);
			}else{
				EhCacheLevel1CacheProvider cacheProvider = new EhCacheLevel1CacheProvider();
				support.setCacheProvider(cacheProvider);
			}
		}
		return support;
	}
}
