/**
 * 
 */
package com.cathay.mybatis.crud;

import java.util.List;

import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cathay.mybatis.crud.builder.BatchInsertBuilder;
import com.cathay.mybatis.crud.builder.DeleteBuilder;
import com.cathay.mybatis.crud.builder.GetByPrimaryKeyBuilder;
import com.cathay.mybatis.crud.builder.InsertBuilder;
import com.cathay.mybatis.crud.builder.SelectAllBuilder;
import com.cathay.mybatis.crud.builder.UpdateBuilder;
import com.cathay.mybatis.crud.name.DefaultCrudMethodDefine;
import com.cathay.mybatis.parser.EntityInfo;
import com.cathay.mybatis.parser.MybatisMapperParser;

/**
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2016年2月2日
 * @Copyright (c) 2015, jwww
 */
public class GeneralSqlGenerator {

	private static final Logger log = LoggerFactory.getLogger(GeneralSqlGenerator.class);
	public static DefaultCrudMethodDefine methodDefines = new DefaultCrudMethodDefine();
	
	private LanguageDriver languageDriver;
	private Configuration configuration;
	private String group;
	
	public GeneralSqlGenerator(String group,Configuration configuration) {
		this.group = group;
		this.configuration = configuration;
		this.languageDriver = configuration.getDefaultScriptingLanguageInstance();
	}
	
	public void generate() {
		if(languageDriver == null)languageDriver = configuration.getDefaultScriptingLanguageInstance();
		List<EntityInfo> entityInfos = MybatisMapperParser.getEntityInfos(group);
		for (EntityInfo entity : entityInfos) {
			//TODO 排除生成
			GetByPrimaryKeyBuilder.build(configuration, languageDriver,entity);
			InsertBuilder.build(configuration,languageDriver, entity);
			UpdateBuilder.build(configuration,languageDriver, entity);
			DeleteBuilder.build(configuration, languageDriver,entity);
			BatchInsertBuilder.build(configuration, languageDriver, entity);
			SelectAllBuilder.build(configuration, languageDriver, entity);
			log.info(" >> generate autoCrud for:[{}] finish",entity.getEntityClass().getName());
		}
	}
}
