package com.iuie.basic.util.opretor;

import java.util.List;

import com.iuie.basic.util.StringUtils;
/**
 * not in操作
 * @author liu.jie
 * @since 2019年6月25日
 */
public class NotInOperator extends AbstractOperator {
	
	public static int ARRAY_SIZE = 2;

	public NotInOperator(String field, Object value) {
		super(field, value);
	}

	@Override
	protected String addWhere() {
		String[] array = {};
		if(this.value instanceof List) {
			array = (String[]) ((List) this.value).toArray();
		} else if(this.value instanceof String[]) {
			array = (String[]) this.value;
		} else {
			array = this.value.toString().split(StringUtils.COMMA);
		}
		StringBuffer part = new StringBuffer();
		if(array.length < ARRAY_SIZE) {
			part.append(" and ").append(this.field).append("<>?");
		} else {
			part.append(" and ").append(this.field).append(" not in (?");
			params.add(StringUtils.trim(array[0]));
			for (int i = 1; i < array.length; i++) {
				part.append(",?");
				params.add(StringUtils.trim(array[i]));
			}
			part.append(")");
		}
		return part.toString();
	}
	@Override
	protected String addOrsWhere() {
		String[] array = {};
		if(this.value instanceof List) {
			array = (String[]) ((List) this.value).toArray();
		} else if(this.value instanceof String[]) {
			array = (String[]) this.value;
		} else {
			array = this.value.toString().split(StringUtils.COMMA);
		}
		StringBuffer part = new StringBuffer();
		if(array.length < ARRAY_SIZE) {
			part.append(" ").append(this.field).append("<>?");
		} else {
			part.append(" ").append(this.field).append(" not in (?");
			params.add(StringUtils.trim(array[0]));
			for (int i = 1; i < array.length; i++) {
				part.append(",?");
				params.add(StringUtils.trim(array[i]));
			}
			part.append(")");
		}
		return part.toString();
	}

	@Override
	protected boolean addParam() {
		return true;
	}
}
