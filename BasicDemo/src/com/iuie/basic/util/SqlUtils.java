package com.iuie.basic.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.iuie.basic.util.opretor.AfterLikeOperator;
import com.iuie.basic.util.opretor.BeforeLikeOperator;
import com.iuie.basic.util.opretor.DateEqualsOperator;
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
 * SQL语句拼装类
 * @author liu.jie
 * @since 2019年4月14日
 */
public class SqlUtils {
	
	/** 排序标记*/
	public static final String ORDER = "_order";
	/** 升序*/
	public static final String ASC = "asc";
	/** 降序*/
	public static final String DESC = "desc";
	
	public static final String SQL_PART_COUNT = "count(1)";
	
	public static final String ORG = "_ors_group";
	
	public static final List<Pair<String, Class<?>>> OPAIRS = Arrays.asList(
			Pair.of("_start_like", BeforeLikeOperator.class),
			Pair.of("_end_like", AfterLikeOperator.class),
			Pair.of("_like", LikeOperator.class),
			Pair.of("_gt_date", GreaterThanOperator.class),
			Pair.of("_gte_date", GreaterThanOrEqualsOperator.class),
			Pair.of("_lt_date", LessThanOperator.class),
			Pair.of("_lte_date", LessThanOrEqualsOperator.class),
			Pair.of("_date", DateEqualsOperator.class),
			Pair.of("_gt_num", GreaterThanOperator.class),
			Pair.of("_gte_num", GreaterThanOrEqualsOperator.class),
			Pair.of("_lt_num", LessThanOperator.class),
			Pair.of("_lte_num", LessThanOrEqualsOperator.class),
			Pair.of("_not_in", NotInOperator.class),
			Pair.of("_in", InOperator.class),
			Pair.of("_ne", NotEqualsOperator.class),
			Pair.of("_nu", IsNullOperator.class),
			Pair.of("_nn", NotNullOperator.class),
			Pair.of("_or", OrOperator.class));
	/**
	 * 拼接查询条件
	 * @param params
	 * @param newParams
	 * @return
	 * 创建人：LJIE
	 * 创建时间：2018年6月25日
	 */
	public static String processWhere(Map<String, ?> params,
			List<Object> newParams, String... notInParamNames) {
		
		if(MapUtils.isEmpty(params)) {
			return StringUtils.EMPTY;
		}
		if(newParams == null) {
			newParams = Lists.newArrayList();
		}
		List<ISqlOperator> ops = Lists.newArrayList();
		for(String key : params.keySet()) {
			// 排除不需要拼接的条件集合，排除排序字段
			if (ArrayUtils.contains(notInParamNames, key)
					|| StringUtils.endsWithIgnoreCase(key, ORDER) || StringUtils.contains(key, ORG)) {
				continue;
			}
			Object value = getArrayValue(params.get(key));
			if(StringUtils.isBlank(value)) {
				continue;
			}
			
			Optional<Pair<String, Class<?>>> op = OPAIRS.stream()
					.filter(p -> key.toLowerCase().endsWith(p.getLeft())).findFirst();
			
			if(op.isPresent()) {
				String field = StringUtils.removeEnd(key.toLowerCase(), op
						.get().getLeft());
				Optional<SqlOperatorEnum> soe = Stream
						.of(SqlOperatorEnum.values())
						.filter(p -> ClassUtils.isAssignable(p.getClazz(), op
								.get().getRight())).findFirst();
				if(soe.isPresent()) {
					ops.add(soe.get().create(field, value));
				}
			} else {
				ops.add(SqlOperatorEnum.EQUALS.create(key, value));
			}
		}
		
		return processWhere(ops.toArray(new ISqlOperator[] {}), newParams,
				notInParamNames);
	}

	/**
	 * 获取对象值
	 * @author liu.jie
	 * @since 2019年7月8日
	 * @param object
	 * @return
	 */
	public static Object getArrayValue(Object object) {
		Object value = StringUtils.EMPTY;
		if(object == null) {
			return value;
		}
		if(object instanceof String[]) {
			String[] strs = (String[])object;
			if(ArrayUtils.isNotEmpty(strs)) {
				value = strs[0];
			}
		} else {
			value = object;
		}
		return value;
	}
	/**
	 * 创建sql
	 * @author liu.jie
	 * @since 2019年7月8日
	 * @param tableName
	 * @param columns
	 * @param params
	 * @param paramValues
	 * @param notInParamNames
	 * @return
	 */
	public static String createSql(String tableName, String columns, 
			Map<String, ?> params, List<Object> paramValues, String... notInParamNames) {
		StringBuffer sql = new StringBuffer("select "+columns+" from " + tableName + " where 1=1 ");
		
		sql.append(processWhere(params, paramValues, notInParamNames));
		List<Object> newParamValues = new ArrayList<Object>();
		sql.append(processOrsWhere(params, newParamValues, notInParamNames));
		paramValues.addAll(newParamValues);
		if(!SQL_PART_COUNT.equals(columns)) {
			sql.append(processOrderby(params));
		}
		
		return sql.toString();
	}
	/**
	 * 拼接orderby
	 * @author liu.jie
	 * @since 2019年7月8日
	 * @param soes
	 * @return
	 */
	public static String processOrderby(Map<String, ?> params) {
		String orderPart = "";
		
		if(params == null || params.isEmpty()) {
			return orderPart;
		}
		
		String suffix = ORDER;
		
		for (String key : params.keySet()) {
			
			if(!key.toLowerCase().endsWith(ORDER)) {
				continue;
			}
			
			Object value = SqlUtils.getArrayValue(params.get(key));
			if(StringUtils.isBlank(value)) {
				continue;
			}
			
			if(StringUtils.isBlank(orderPart)) {
				orderPart += " order by ";
			} else {
				orderPart += ", ";
			}
			orderPart += StringUtils.removeEnd(key.toLowerCase(), suffix) + " " + value;
		}
		
		return orderPart;
	}
	/**
	 * 拼接update语句的set
	 * @author liu.jie
	 * @since 2019年7月8日
	 * @param params
	 * @param ignoredNames
	 * @return
	 */
	public static String processSet(Map<String, ?> params, String... ignoredNames) {
		StringBuffer setPart = new StringBuffer();
		if(MapUtils.isEmpty(params)) {
			return StringUtils.EMPTY;
		}
		
		for (String key : params.keySet()) {
			if(ArrayUtils.contains(ignoredNames, key)) {
				continue;
			}
			
			Object value = params.get(key);
			
			if(setPart.length() > 0) {
				setPart.append(",");
			}
			setPart.append(key).append("='").append(value).append("'");
		}
		
		return setPart.toString();
	}
	/**
	 * 拼接where
	 * @author liu.jie
	 * @since 2019年7月8日
	 * @param soes
	 * @param newParams
	 * @param notInParamNames
	 * @return
	 */
	public static String processWhere(ISqlOperator[] soes, 
			List<Object> newParams, String... notInParamNames) {
		if(ArrayUtils.isEmpty(soes)) {
			return StringUtils.EMPTY;
		}
		StringBuffer wherePart = new StringBuffer();
		if(newParams == null) {
			newParams = Lists.newArrayList();
		}
		for(ISqlOperator soe : soes) {
			if(soe instanceof OrderByOperator) {
				continue;
			}
			wherePart.append(soe.process(newParams, notInParamNames));
		}
		return wherePart.toString();
	}
	
	/**
	 * 拼接Orswhere，例：NO_ors_group_like，NO_ors_group_in，NO_ors_group，
	 * 				  MC_ors_group2_in，MC_ors_group2_like，MC_ors_group3_like，
	 *                MC_ors_group4条件的拼接语句：
	 * and ( MC=?)  and ( mc like ? or  mc in (?))  and ( no in (?) or  no like ?)  and ( mc like ?) 
	 * @author lmp
	 * @since 2019年8月26日
	 * @param soes
	 * @param newParams
	 * @param notInParamNames
	 * @return
	 */
	public static String processOrsWhere(ISqlOperator[] soes, 
			List<Object> newParams, String... notInParamNames) {
		if(ArrayUtils.isEmpty(soes)) {
			return StringUtils.EMPTY;
		}
		StringBuffer wherePart = new StringBuffer();
		if(newParams == null) {
			newParams = Lists.newArrayList();
		}

		//ors的分组
		Map<String,String> orsGroup = new LinkedHashMap<String, String>();
		//sql值重新排序用
		Map<String,Object> orsSort = new LinkedHashMap<String, Object>();
		int sortnum = 0;
		//处理查询所有的ors子条件,并根据分组拼接该组的ors子条件 
		for(ISqlOperator soe : soes) {
			if(soe instanceof OrderByOperator) {
				continue;
			}
			String part = "sort"+(sortnum)+"_"+ soe.processOrs(newParams, notInParamNames);
			orsSort.put("sort"+(sortnum)+"_", newParams.get(sortnum));
			String field = soe.getField().substring(soe.getField().indexOf("_ors_group"));
			if(orsGroup.containsKey(field)){
				orsGroup.put(field,orsGroup.get(field) +" or "+ part );
			}else{
				orsGroup.put(field, part);
			}
			sortnum++;
		}
		//拼接所有的ors查询语句,并却去掉分组的标识字符
		for(String key : orsGroup.keySet()){
			wherePart.append(" and (").append(orsGroup.get(key).replace(key, "")).append(") ");
		}
		//重新排序ors的param值
		List<List<Object>> sortParam = new ArrayList<List<Object>>();
		for(String ol : orsSort.keySet()){
			List<Object> list = new ArrayList<Object>();
			list.add(wherePart.toString().indexOf(ol));
			list.add(ol);
			list.add(orsSort.get(ol));
			sortParam.add(list);
		}
		Collections.sort(sortParam,new Comparator<List<Object>>(){
			@Override
			public int compare(List<Object> o1,List<Object> o2){	
				return Integer.parseInt(o1.get(0).toString()) - Integer.parseInt(o2.get(0).toString());
			}
		});
		newParams.clear();
		for(List<Object> parmlist : sortParam){
			newParams.add(parmlist.get(2));
		}

		String whereStr = wherePart.toString();
		//去除掉排序标识字符
		for(String ol : orsSort.keySet()){
			whereStr = whereStr.replace(ol,"");
		}
		return whereStr;
	}
	
	public static <L, R> Pair<L, R> processWhere(ISqlOperator[] soes,
			String... ignoredNames) {
		List<Object> list = Lists.newArrayList();
		String str = processWhere(soes, list, ignoredNames);
		return Pair.of((L)str, (R)list);
	}
	
	public static <L, R, V> Pair<L, R> processWhere(Map<String, V> params,
			String... ignoredNames) {
		List<Object> list = Lists.newArrayList();
		String str = processWhere(params, list, ignoredNames);
		return Pair.of((L)str, (R)list);
	}
	
	/**
	 * 拼接ors查询条件
	 * @param params
	 * @param newParams
	 * @return
	 * 创建人：lmp
	 * 创建时间：2018年8月26日
	 */
	public static String processOrsWhere(Map<String, ?> params,
			List<Object> newParams, String... notInParamNames) {
		
		if(MapUtils.isEmpty(params)) {
			return StringUtils.EMPTY;
		}
		if(newParams == null) {
			newParams = Lists.newArrayList();
		}
		List<ISqlOperator> ops = Lists.newArrayList();
		//筛选出所有哦ORG的条件语句
		Map<String, Object> params2 =new HashMap<String, Object>(16);
		for(String key : params.keySet()) {
			if (ArrayUtils.contains(notInParamNames, key)
					|| StringUtils.endsWithIgnoreCase(key, ORDER)) {
				continue;
			}
			Object value = getArrayValue(params.get(key));
			if(StringUtils.isBlank(value)) {
				continue;
			}
			if (StringUtils.indexOfIgnoreCase(key, ORG)>-1) {
				params2.put(key, params.get(key));
			}
		}
		//查询出ORG结尾对应的查询语句类型
		for(String key : params2.keySet()) {
			Object value = getArrayValue(params2.get(key));
			Optional<Pair<String, Class<?>>> op = OPAIRS.stream()
					.filter(p -> key.toLowerCase().endsWith(p.getLeft())).findFirst();
			
			if(op.isPresent()) {
				String field = StringUtils.removeEnd(key.toLowerCase(), op
						.get().getLeft());
				Optional<SqlOperatorEnum> soe = Stream
						.of(SqlOperatorEnum.values())
						.filter(p -> ClassUtils.isAssignable(p.getClazz(), op
								.get().getRight())).findFirst();
				if(soe.isPresent()) {
					ops.add(soe.get().create(field, value));
				}
			} else {
				ops.add(SqlOperatorEnum.EQUALS.create(key, value));
			}
		}
		
	return processOrsWhere(ops.toArray(new ISqlOperator[] {}), newParams,notInParamNames);
	}
	
	/**
	 * 创建SQL
	 * @author liu.jie
	 * @since 2019年7月8日
	 * @param tableName
	 * @param columns
	 * @param soes
	 * @param paramValues
	 * @param notInParamNames
	 * @return
	 */
	public static String createSql(String tableName, String columns, 
			ISqlOperator[] soes, List<Object> paramValues, String... notInParamNames) {
		StringBuffer sql = new StringBuffer("select "+columns+" from " + tableName + " where 1=1 ");
		
		sql.append(processWhere(soes, paramValues, notInParamNames));
		if(!SQL_PART_COUNT.equals(columns)) {
			sql.append(processOrderby(soes));
		}
		
		return sql.toString();
	}
	/**
	 * 拼接orderby
	 * @author liu.jie
	 * @since 2019年7月8日
	 * @param soes
	 * @return
	 */
	public static String processOrderby(ISqlOperator[] soes) {
		if(ArrayUtils.isEmpty(soes)) {
			return StringUtils.EMPTY;
		}
		StringBuffer orderPart = new StringBuffer();
		for(ISqlOperator soe : soes) {
			if (soe instanceof OrderByOperator
					&& StringUtils.isNotBlank(soe.getValue())
					&& StringUtils.isNotBlank(soe.getField())) {
				if (StringUtils.isBlank(orderPart.toString())) {
					orderPart.append(" order by ");
				} else {
					orderPart.append(StringUtils.COMMA);
				}
				orderPart.append(soe.getField()).append(" ")
						.append(soe.getValue());
			}
		}
		
		return orderPart.toString();
	}
	
}
