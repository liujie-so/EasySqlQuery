package com.iuie.basic.util.opretor;

/**
 * 全模糊匹配
 * @author liu.jie
 * @since 2019年6月25日
 */
public class LikeOperator extends AbstractOperator {
	
	public LikeOperator(String field, Object value) {
		super(field, value);
	}
	
	@Override
	public String addWhere() {
		return new StringBuffer(" and ").append(this.field).append(" like ?").toString();
	}
	@Override
	public String addOrsWhere() {
		return new StringBuffer(" ").append(this.field).append(" like ?").toString();
	}
	
	@Override
	protected boolean addParam() {
		getParams().add("%" + this.value + "%");
		return true;
	}

}
