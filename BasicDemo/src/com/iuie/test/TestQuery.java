package com.iuie.test;

import static com.iuie.basic.util.SqlOperatorEnum.EQUALS;
import static com.iuie.basic.util.SqlOperatorEnum.GREATER_THAN_EQUALS;
import static com.iuie.basic.util.MapUtils.create;

import java.util.List;
import java.util.Map;

import com.iuie.basic.dao.IBasicDao;
import com.iuie.basic.util.SpringBeanUtils;
import com.iuie.basic.util.opretor.ISqlOperator;

public class TestQuery {

	public static void main(String[] args) {
		IBasicDao dao = SpringBeanUtils.getBean("basicDao");
		String table = "test";
		Map<String, Object> params = create("age_gte", 18, "id", "1");
		List<Map<String, Object>> list = null;
		// select * from test where id='1' and age >= 18;
		// SQL查询的两种方式
		list = dao.list(table, "*", params, 0, -1); // 方式一
		list = dao.listSoe(table, "*", new ISqlOperator[]{GREATER_THAN_EQUALS.of("age", 18), EQUALS.of("id", "1")}, 0, -1); // 方式二
		// select id, xm, age from test where id='1' and age >= 18;
		list = dao.list(table, "id, xm, age", params, 0, -1);
		list = dao.listSoe(table, "id, xm, age", new ISqlOperator[]{GREATER_THAN_EQUALS.of("age", 18), EQUALS.of("id", "1")}, 0, -1);
		params = create("age_gte", 18, "id", "1", "type", "1");
		// select id, xm, age from test where id='1' and age >= 18;
		// 排除参数拼接
		list = dao.list(table, "id, xm, age", params, 0, -1, "type");
		
	}

}
