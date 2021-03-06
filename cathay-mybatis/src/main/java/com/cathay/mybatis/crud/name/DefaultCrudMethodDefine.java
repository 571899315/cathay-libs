/**
 * 
 */
package com.cathay.mybatis.crud.name;

import com.cathay.mybatis.crud.CrudMethodDefine;

/**
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2016年9月16日
 */
public class DefaultCrudMethodDefine implements CrudMethodDefine {

	@Override
	public String selectName() {
		return "selectByPrimaryKey";
	}

	@Override
	public String insertName() {
		return "insert,insertSelective";
	}

	@Override
	public String updateName() {
		return "updateByPrimaryKey,updateByPrimaryKeySelective";
	}

	@Override
	public String deleteName() {
		return "deleteByPrimaryKey";
	}

	@Override
	public String selectAllName() {
		return "selectAll";
	}

}
