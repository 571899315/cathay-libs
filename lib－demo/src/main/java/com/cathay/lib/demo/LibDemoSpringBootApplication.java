package com.cathay.lib.demo;


import com.cathay.jvm.cache.spring.EnableMultiCaching;
import com.cathay.springboot.starter.cache.EnableCathayCache;
import com.cathay.springboot.starter.mybatis.EnableCathayMybatis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@ComponentScan(basePackages = {"com.cathay","com.cathay.jvm.cache.spring"})
@EnableMultiCaching
public class LibDemoSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibDemoSpringBootApplication.class, args);
    }


}
