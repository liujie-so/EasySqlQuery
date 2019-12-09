package com.iuie.basic.util.opretor;

import java.util.List;

/**
 * Sql操作类接口
 * @author liu.jie
 * @since 2019年6月25日
 */
public interface ISqlOperator {

	/**
	 * 处理条件逻辑
	 * @author liu.jie
	 * @since 2019年6月25日
	 * @param params
	 * @param ignoredParams
	 * @return
	 */
	String process(List params, String[] ignoredParams);
	/**
	 * 
	 * 方法描述：处理ors条件逻辑
	 * @author li.maopeng
	 * @since 2019年8月27日
	 * @param params
	 * @param ignoredParams
	 * @return
	 */
	String processOrs(List params, String[] ignoredParams);

	/**
	 * 
	 * 方法描述：获取查询参数名
	 * @author li.maopeng
	 * @since 2019年8月27日
	 * @return
	 */
	String getField();
	
	/**
	 * 
	 * 方法描述：获取查询参数值
	 * @author li.maopeng
	 * @since 2019年8月27日
	 * @return
	 */
	Object getValue();
}
