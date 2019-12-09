package com.iuie.basic.util.opretor;

import java.util.List;

import javax.jdo.annotations.Order;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.ArrayUtils;

import com.iuie.basic.util.StringUtils;
/**
 * 操作的父类
 * @author liu.jie
 * @since 2019年6月25日
 */
public abstract class AbstractOperator implements ISqlOperator {

	@Setter
	@Getter
	protected List params;
	@Setter
    @Getter
	protected String field;
	@Setter
    @Getter
	protected Object value;
	
	public AbstractOperator(String field, Object value) {
		this.field = field;
		this.value = value;
	}
	
	@Override
	public String process(List params, String[] ignoredParams) {
		this.params = params;
		// 对于以map传参数的方式，转换时就已经过滤了需排除的列
		/*if (StringUtils.isBlank(this.getField())
				|| ArrayUtils.contains(ignoredParams, this.getField())) {
			return StringUtils.EMPTY;
		}*/
		boolean isAdd = addParam();
		if(isAdd) {
			return addWhere();
		}
		return StringUtils.EMPTY;
	}
	@Override
	public String processOrs(List params, String[] ignoredParams) {
		this.params = params;
		// 对于以map传参数的方式，转换时就已经过滤了需排除的列
		/*if (StringUtils.isBlank(this.getField())
				|| ArrayUtils.contains(ignoredParams, this.getField())) {
			return StringUtils.EMPTY;
		}*/
		boolean isAdd = addParam();
		if(isAdd) {
			return addOrsWhere();
		}
		return StringUtils.EMPTY;
	}
	/**
	 * 拼接where条件
	 * @author liu.jie
	 * @since 2019年6月25日
	 * @return
	 */
	protected abstract String addWhere();
	/**
	 * 
	 * 方法描述：拼接where的ors条件
	 * @author li.maopeng
	 * @since 2019年8月26日
	 * @return
	 */
	protected abstract String addOrsWhere();

	/**
	 * 添加“?”占位符的参数
	 * @author liu.jie
	 * @since 2019年6月20日
	 */
	protected boolean addParam() {
		params.add(this.getValue());
		return true;
	}

}
