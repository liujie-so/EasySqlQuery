package com.iuie.basic.util.opretor;

import org.apache.commons.lang3.math.NumberUtils;

import com.iuie.basic.util.DateUtils;
/**
 * 大于等于操作
 * @author liu.jie
 * @since 2019年6月25日
 */
public class GreaterThanOrEqualsOperator extends AbstractOperator {

	public GreaterThanOrEqualsOperator(String field, Object value) {
		super(field, value);
	}

	@Override
	protected String addWhere() {
		return new StringBuffer(" and ").append(this.field).append(" >= ?").toString();
	}
	
	@Override
	protected String addOrsWhere() {
		return new StringBuffer(" and ").append(this.field).append(" >= ?").toString();
	}

	@Override
	protected boolean addParam() {
		Object value = this.value;
		if(value instanceof Number) {
			
		} else {
			if(NumberUtils.isNumber(value.toString())) {
				value = NumberUtils.toDouble(value.toString());
			} else {
				value = DateUtils.parseFullDate(value);
				if(value == null) {
					return false;
				}
			}
		}
		getParams().add(value);
		return true;
	}
}
