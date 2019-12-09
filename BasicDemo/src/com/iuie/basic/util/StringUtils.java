package com.iuie.basic.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * String工具类
 * @author liu.jie
 * @since 2019年4月14日
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	/** 逗号*/
	public final static String COMMA = ",";
	/** 点号*/
	public final static String DIT = ".";
	/** 斜杠*/
	public final static String SLASH = "/";
	/** 反斜杠*/
	public final static String BACK_SLASH = "\\";
	/** 换行*/
	public final static String LINE_BREAK = "\\n";
	
	/**
	 * 按关键字优先级，处理字符截取
	 * 2017年10月21日
	 * liu.jie
	 * @param str
	 * @param sign	关键字以逗号分隔：区,县,市,州
	 * @return
	 */
	public static String subString(Object str, String sign) {
		if(str == null || isBlank(str)) {
			return "";
		}
		
		if(sign.contains(COMMA)) {
			String[] keys = sign.split(COMMA);
			for (int i = 0; i < keys.length; i++) {
				String string = subString(str, keys[i]);
				if(isBlank(string)) {
					continue;
				}
				return string;
			}
		}
		
		if(str.toString().indexOf(sign) < 0) {
			return EMPTY;
		}
		
		return str.toString().substring(0, str.toString().indexOf(sign)) + sign;
	}
	
	/**
	 * 判断关键字是否包含在字符串中
	 * 2017年11月5日
	 * liu.jie
	 * @param data
	 * @param sign
	 * @return
	 */
	public static boolean existsKeyInString(Object data, String sign) {
		if(data == null || isBlank(data.toString())) {
			return false;
		}
		
		if(sign.contains(COMMA)) {
			String[] s = sign.split(COMMA);
			for (int i = 0; i < s.length; i++) {
				if(!existsKeyInString(data, s[i])) {
					continue;
				}
				return true;
			}
		}
		
		if(data.toString().equals(sign)) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isBlank(Object value) {
		if(value == null) {return true;}
		return isBlank(value.toString());
	}
	/**
	 * 判断map中多个key对应的值是否为空，如果其中一个为空就返回true
	 * @author liu.jie
	 * @since 2019年8月2日
	 * @param value
	 * @param keys
	 * @return
	 */
	public static <K, V> boolean isBlank(Map<K, V> value, K... keys) {
		if (MapUtils.isEmpty(value) || ArrayUtils.isEmpty(keys)) {
			throw new RuntimeException();
		}
		for (K key : keys) {
			if(isBlank(value.get(key))) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isNotBlank(Object value) {
		return !isBlank(value);
	}
	
	public static <K, V> boolean isNotBlank(Map<K, V> map, K... keys) {
		return !isBlank(map, keys);
	}
	
	public static String nvl(Object value, String defaultValue) {
		if(isBlank(value)) {
			return defaultValue;
		}
		return defaultString(value.toString(), defaultValue);
	}
	
	public static String nvl(Object value) {
		return nvl(value, EMPTY);
	}
	
	/**
	 * 字符清洗转换成数组
	 * @author liu.jie
	 * @since 2019年7月17日
	 * @param value
	 * @param separatorChars &nbsp;&nbsp;&nbsp;&nbsp;分割符
	 * @return
	 */
	public static String[] stringToArray(Object value, String separatorChars) {
		List<String> list = stringToList(value, separatorChars);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		return stringToList(value, separatorChars).toArray(ArrayUtils.EMPTY_STRING_ARRAY);
	}
	/**
	 * 请查看&nbsp;&nbsp;&nbsp;&nbsp;{@link StringUtils#stringToArray(Object, String)}
	 */
	public static String[] stringToArray(Object value) {
		return stringToArray(value, COMMA);
	}
	/**
	 * 字符清洗转换成集合
	 * @author liu.jie
	 * @since 2019年7月17日
	 * @param value
	 * @param separatorChars &nbsp;&nbsp;&nbsp;&nbsp;分割符
	 * @return
	 */
	public static List<String> stringToList(Object value, String separatorChars) {
		if (isBlank(value)) {
			return null;
		}
		return Arrays.stream(split(value.toString(), separatorChars))
				.map(f -> trim(f)).filter(f -> isNotBlank(f))
				.collect(Collectors.toList());
	}
	/**
	 * 请查看&nbsp;&nbsp;&nbsp;&nbsp;{@link StringUtils#stringToList(Object, String)}
	 */
	public static List<String> stringToList(Object value) {
		return stringToList(value, COMMA);
	}
	/**
	 * 字符清洗转换成Set
	 * @author liu.jie
	 * @since 2019年7月30日
	 * @param value
	 * @param separatorChars &nbsp;&nbsp;&nbsp;&nbsp;分割符
	 * @return
	 */
	public static Set<String> stringToSet(Object value, String separatorChars) {
		if (isBlank(value)) {
			return null;
		}
		return Arrays.stream(split(value.toString(), separatorChars))
				.map(f -> trim(f)).filter(f -> isNotBlank(f))
				.collect(Collectors.toSet());
	}
	/**
	 * 请查看&nbsp;&nbsp;&nbsp;&nbsp;{@link StringUtils#stringToSet(Object, String)}
	 */
	public static Set<String> stringToSet(Object value) {
		return stringToSet(value, COMMA);
	}
	
	public static void main(String[] args) {}
	

}
