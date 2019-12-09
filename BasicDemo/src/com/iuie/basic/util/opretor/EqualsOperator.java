package com.iuie.basic.util.opretor;

/**
 * 等于操作
 * @author liu.jie
 * @since 2019年6月25日
 */
public class EqualsOperator extends AbstractOperator {

	public EqualsOperator(String field, Object value) {
		super(field, value);
	}

	@Override
	protected String addWhere() {
		return new StringBuffer(" and ").append(this.field).append("=?").toString();
	}
	
	@Override
	protected String addOrsWhere() {
		return new StringBuffer(" ").append(this.field).append("=?").toString();
	}

}
