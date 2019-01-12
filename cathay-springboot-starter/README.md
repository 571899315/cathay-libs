#### 添加依赖
```
<dependency>
	<groupId>com.cathay</groupId>
	<artifactId>cathay-springboot-starter</artifactId>
	<version>1.1.3</version>
</dependency>
```

#### 注解定义
 - @EnablecathayCache ：集成cache模块
 - @EnablecathayMybatis ： 集成mybatis模块
 - @EnablecathaySchedule ：集成schedule模块
 - @EnablecathayKafkaConsumer ： 集成kafka consumer
 - @EnablecathayKafkaProducer ： 集成kafka producer
 
#### 配置
注：=后面的为默认值，（）包含的为可选值。含义请参考具体模块。
```
--cache
cathay.cache.mode=standalone (standalone:单机模式，sentinel：哨兵模式(主从),cluster：集群模式，shard：分片模式)
cathay.cache.servers=
cathay.cache.password=
cathay.cache.database=0
cathay.cache.maxPoolSize=
cathay.cache.maxPoolIdle=
cathay.cache.minPoolIdle=
cathay.cache.maxPoolWaitMillis=
cathay.cache.level1.distributedMode=true
cathay.cache.level1.bcastServer=
cathay.cache.level1.password=
cathay.cache.level1.bcastScope=
cathay.cache.level1.cacheProvider=guavacache  (ehcache , guavacache)
cathay.cache.level1.maxCacheSize=5000
cathay.cache.level1.timeToLiveSeconds=300
cathay.cache.level1.cacheNames=

--mybatis
cathay.mybatis.dbType=MySQL
cathay.mybatis.crudDriver=mapper3
cathay.mybatis.cacheEnabled=false
cathay.mybatis.rwRouteEnabled=false
cathay.mybatis.dbShardEnabled=false
cathay.mybatis.paginationEnabled=true

--schedule
cathay.task.registryType=zookeeper
cathay.task.registryServers=
cathay.task.groupName=
cathay.task.scanPackages=
cathay.task.threadPoolSize=

--kafka
kafka.bootstrap.servers=
kafka.zkServers=
cathay.kafka.producer.defaultAsynSend=
cathay.kafka.producer.producerGroup=
cathay.kafka.producer.monitorZkServers=
cathay.kafka.producer.delayRetries=0
#其他kafka原生配置(kafka.producer.xxx)
kafka.producer.acks=1
kafka.producer.retries=1

cathay.kafka.consumer.independent
cathay.kafka.consumer.useNewAPI=false
cathay.kafka.consumer.processThreads=100
cathay.kafka.consumer.scanPackages=
#其他kafka原生配置(kafka.consumer.xxx)
kafka.consumer.group.id=
kafka.consumer.enable.auto.commit=

```

#### 使用

