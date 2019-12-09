package com.iuie.basic.util.opretor;

/**
 * 为空操作
 * @author liu.jie
 * @since 2019年6月25日
 */
public class IsNullOperator extends AbstractOperator {

	public IsNullOperator(String field, Object value) {
		super(field, value);
	}

	@Override
	protected String addWhere() {
		return new StringBuffer(" and ").append(this.field).append(" is null").toString();
	}
	
	@Override
	protected String addOrsWhere() {
		return new StringBuffer(" ").append(this.field).append(" is null").toString();
	}
	
	@Override
	protected boolean addParam() {
		return true;
	}

}
