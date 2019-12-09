package com.iuie.basic.util.opretor;

import com.iuie.basic.util.StringUtils;
/**
 * 排序操作
 * @author liu.jie
 * @since 2019年6月25日
 */
public class OrderByOperator extends AbstractOperator {

	public OrderByOperator(String field, Object value) {
		super(field, value);
	}

	@Override
	protected String addWhere() {
		return StringUtils.EMPTY;
	}
	@Override
	protected String addOrsWhere() {
		return StringUtils.EMPTY;
	}
	@Override
	protected boolean addParam() {
		return true;
	}
}
