package com.iuie.basic.util;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 集合工具类
 * @author liu.jie
 * @since 2019年7月29日
 */
public class CollectionUtils extends
		org.apache.commons.collections.CollectionUtils {

	/**
	 * 集合拼接字符串
	 * @author liu.jie
	 * @since 2019年7月29日
	 * @param coll
	 * @param separatorChars 连接符
	 * @return
	 */
	public static <E> String joining(Collection<E> coll) {
		return StringUtils.join(coll, StringUtils.COMMA);
	}
	/**
	 * 去重的拼接
	 * @author liu.jie
	 * @since 2019年7月30日
	 * @param coll
	 * @return
	 */
	public static <E> String joiningDupRemove(Collection<E> coll) {
		return joiningDupRemove(coll, ArrayUtils.EMPTY_STRING_ARRAY);
	}
	
	public static <E> String joiningDupRemove(Collection<E> coll,
			String... ignoredValues) {
		Predicate<String> predicate = p -> ArrayUtils.isEmpty(ignoredValues)
				|| !ArrayUtils.contains(ignoredValues, p);
		
		return StringUtils.stringToSet(joining(coll)).parallelStream()
				.filter(predicate)
				.collect(Collectors.joining(StringUtils.COMMA));
	}
}
