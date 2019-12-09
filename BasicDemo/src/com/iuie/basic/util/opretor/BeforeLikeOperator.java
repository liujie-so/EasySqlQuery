package com.iuie.basic.util.opretor;

/**
 * 前模糊匹配
 * @author liu.jie
 * @since 2019年6月25日
 */
public class BeforeLikeOperator extends AbstractOperator {

	public BeforeLikeOperator(String field, Object value) {
		super(field, value);
	}

	@Override
	protected String addWhere() {
		return new StringBuffer(" and ").append(this.field).append(" like ?").toString();
	}
	@Override
	protected String addOrsWhere() {
		return new StringBuffer(" ").append(this.field).append(" like ?").toString();
	}

	@Override
	protected boolean addParam() {
		getParams().add(this.value + "%");
		return true;
	}
}
