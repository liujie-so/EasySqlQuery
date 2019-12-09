package com.iuie.basic.util.opretor;

import java.util.List;
import java.util.stream.Collectors;

import com.iuie.basic.util.StringUtils;
/**
 * 或者操作
 * @author liu.jie
 * @since 2019年8月18日
 */
public class OrOperator extends AbstractOperator {

	public OrOperator(String field, Object value) {
		super(field, value);
	}

	@Override
	protected String addWhere() {
		List<String> fields = StringUtils.stringToList(field, "_");
		StringBuilder wherePart = new StringBuilder(" and (1=0 or ");
		wherePart.append(
				fields.stream().map(f -> f + "=?")
						.collect(Collectors.joining(" or "))).append(")");
		fields.stream().forEach(f -> getParams().add(value));
		return wherePart.toString();
	}
	
	@Override
	protected String addOrsWhere() {
		List<String> fields = StringUtils.stringToList(field, "_");
		StringBuilder wherePart = new StringBuilder(" (1=0 or ");
		wherePart.append(
				fields.stream().map(f -> f + "=?")
						.collect(Collectors.joining(" or "))).append(")");
		fields.stream().forEach(f -> getParams().add(value));
		return wherePart.toString();
	}
	
	@Override
	protected boolean addParam() {
		return true;
	}

}
