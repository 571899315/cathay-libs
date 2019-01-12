package com.cathay.mybatis.core;

import org.apache.ibatis.plugin.Invocation;

import com.cathay.mybatis.plugin.CathayMybatisInterceptor;

/**
 * mybatis插件拦截处理器接口
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2015年12月7日
 * @Copyright (c) 2015, jwww
 */
public interface InterceptorHandler {
	
	void start(CathayMybatisInterceptor context);
	
	void close();

	Object onInterceptor(Invocation invocation) throws Throwable;
	
	void onFinished(Invocation invocation,Object result);
	
	int interceptorOrder();
	
	
}
