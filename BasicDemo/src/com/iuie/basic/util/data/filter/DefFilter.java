package com.iuie.basic.util.data.filter;

import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.beanutils.PropertyUtils;

import com.iuie.core.bo.Entity;

import lombok.Getter;
import lombok.Setter;

public class DefFilter implements IDFilter {

	@Setter@Getter
	protected String field;
	@Setter@Getter
	protected Object value;
	
	public void setFilter(String field, Object value) {
		this.field = field;
		this.value = value;
	}
	
	@Override
	public <T> Predicate<T> of() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public <T> Object getObjectVal(T t) {
		Object v = null;
		if(t instanceof Map) {
			v = ((Map) t).get(field);
		} else if(t instanceof Entity) {
			try {
				v = PropertyUtils.getProperty(t, field);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return v;
	}

}
