package com.iuie.basic.util;

import java.lang.reflect.Constructor;

import com.iuie.basic.util.opretor.AfterLikeOperator;
import com.iuie.basic.util.opretor.BeforeLikeOperator;
import com.iuie.basic.util.opretor.EqualsOperator;
import com.iuie.basic.util.opretor.GreaterThanOperator;
import com.iuie.basic.util.opretor.GreaterThanOrEqualsOperator;
import com.iuie.basic.util.opretor.ISqlOperator;
import com.iuie.basic.util.opretor.InOperator;
import com.iuie.basic.util.opretor.IsNullOperator;
import com.iuie.basic.util.opretor.LessThanOperator;
import com.iuie.basic.util.opretor.LessThanOrEqualsOperator;
import com.iuie.basic.util.opretor.LikeOperator;
import com.iuie.basic.util.opretor.NotEqualsOperator;
import com.iuie.basic.util.opretor.NotInOperator;
import com.iuie.basic.util.opretor.NotNullOperator;
import com.iuie.basic.util.opretor.OrOperator;
import com.iuie.basic.util.opretor.OrderByOperator;


/**
 * SQL条件运算符枚举
 * @author liu.jie
 * @since 2019年6月20日
 */
public enum SqlOperatorEnum {
	LIKE(1, LikeOperator.class), 
	BEFORE_LIKE(2, BeforeLikeOperator.class),
	AFTER_LIKE(3, AfterLikeOperator.class),
	IN(4, InOperator.class),
	NOT_IN(5, NotInOperator.class),
	NOT_NULL(6, NotNullOperator.class),
	IS_NULL(7, IsNullOperator.class),
	EQUALS(8, EqualsOperator.class),
	NOT_EQUALS(9, NotEqualsOperator.class),
	GREATER_THAN(10, GreaterThanOperator.class),
	GREATER_THAN_EQUALS(11, GreaterThanOrEqualsOperator.class),
	LESS_THAN(12, LessThanOperator.class),
	LESS_THAN_EQUALS(13, LessThanOrEqualsOperator.class),
	ORDER_BY(14, OrderByOperator.class),
	OR(14, OrOperator.class);
	@Setter
	@Getter
	private Integer code;
	@Setter
	@Getter
	private Class<ISqlOperator> clazz;
	private SqlOperatorEnum(int code, Class clazz) {
		this.code = code;
		this.clazz = clazz;
	}
	/**
	 * 创建查询条件，也可使用{@link SqlOperatorEnum#of(String, Object)}
	 * @author liu.jie
	 * @since 2019年10月21日
	 * @param field	列名
	 * @param value 条件值
	 * @return
	 */
	public ISqlOperator create(String field, Object value) {
		ISqlOperator operator = null;
		try {
			Class[] parameterTypes = {String.class, Object.class};
			Constructor constructor = this.clazz.getConstructor(parameterTypes);
			operator = (ISqlOperator) constructor.newInstance(field, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return operator;
	}
	/**
	 * 创建查询条件
	 */
	public ISqlOperator of(String field, Object value) {
		return create(field, value);
	}

}
