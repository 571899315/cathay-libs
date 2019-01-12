/**
 * 
 */
package com.cathay.rest.excetion;

import com.cathay.rest.response.WrapperResponseEntity;

/**
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2016年12月23日
 */
public interface ExcetionWrapper {

	WrapperResponseEntity toResponse(Exception e);
}
