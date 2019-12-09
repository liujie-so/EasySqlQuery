package com.iuie.basic.util.opretor;

import org.apache.commons.lang3.math.NumberUtils;

import com.iuie.basic.util.DateUtils;
/**
 * 小于等于操作
 * @author liu.jie
 * @since 2019年6月25日
 */
public class LessThanOrEqualsOperator extends AbstractOperator {

	public LessThanOrEqualsOperator(String field, Object value) {
		super(field, value);
	}

	@Override
	protected String addWhere() {
		return new StringBuffer(" and ").append(this.field).append(" <= ?").toString();
	}
	@Override
	protected String addOrsWhere() {
		return new StringBuffer(" ").append(this.field).append(" <= ?").toString();
	}

	@Override
	protected boolean addParam() {
		Object value = this.value;
		if(value instanceof Number) {
			
		} else {
			if(NumberUtils.isNumber(value.toString())) {
				value = NumberUtils.toDouble(value.toString());
			} else {
				String oldValue = value.toString();
				value = DateUtils.parseFullDate(value);
				if (oldValue.length() <= 10) {
					value = org.apache.commons.lang3.time.DateUtils.addDays((java.util.Date) value, 1);
				}
				if(value == null) {
					return false;
				}
			}
		}
		getParams().add(value);
		return true;
	}
}
