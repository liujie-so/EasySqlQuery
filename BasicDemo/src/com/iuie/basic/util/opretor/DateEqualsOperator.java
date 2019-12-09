package com.iuie.basic.util.opretor;

import com.iuie.basic.util.DateUtils;


/**
 * 时间等于操作
 * @author liu.jie
 * @since 2019年6月25日
 */
public class DateEqualsOperator extends EqualsOperator {

	public DateEqualsOperator(String field, Object value) {
		super(field, value);
	}
	
	@Override
	protected boolean addParam() {
		if(value instanceof String) {
			value = DateUtils.parseFullDate(value);
		}
		return super.addParam();
	}

}
