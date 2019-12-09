package com.iuie.basic.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Map工具类
 * @author liu.jie
 *
 */
public class MapUtils extends org.apache.commons.collections.MapUtils {
	/**
	 * 大于号
	 */
	public final static String GREATER_THAN = ">";
	
	/**
	 * 根据上下级关系获取map对象
	 * 2017年10月30日
	 * liu.jie
	 * @param rootContainer
	 * @param relation				"zf>yy>bb"：祖父>爷爷>爸爸
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMapObject(Map<String, Object> rootContainer, String relation) {
		if(isEmpty(rootContainer) || StringUtils.isBlank(relation)) {
			return null;
		}
		
		Map<String, Object> parentContainer = null;
		String[] strs = relation.split(GREATER_THAN);
		for (int i=0; i < strs.length; i++) {
			if(i == 0) {
				parentContainer = rootContainer;
			}
			if(parentContainer.get(strs[i]) == null) {
				parentContainer.put(strs[i], Maps.newHashMap());
			}
			parentContainer = (Map<String, Object>)parentContainer.get(strs[i]);
		}
		return parentContainer;
	}
	/**
	 * 概据KEY值获取MAP对象，前提是所有KEY是唯一的
	 * 2017年11月22日
	 * liu.jie
	 * @param rootContainer
	 * @param key
	 * @return
	 */
	public static Map<String, Object> getMapObjectByKey(Map<String, Object> rootContainer, String key) {
		if(isEmpty(rootContainer) || StringUtils.isBlank(key)) {
			return null;
		}
		if(!existsMapObject(rootContainer, key)) {
			return null;
		}
		
		return getMapObject(rootContainer, getMapRelation(rootContainer, key));
	}
	
	/**
	 * 根据上下级关系判断map对象是事存在
	 * 2017年10月30日
	 * liu.jie
	 * @param rootContainer
	 * @param relation				"zzf>zf>bb"：曾祖父>祖父（爷爷）>爸爸
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean existsMapObjectByRelation(Map<String, Object> rootContainer, String relation) {
		if(isEmpty(rootContainer) || StringUtils.isBlank(relation)) {
			return false;
		}
		
		Map<String, Object> parentContainer = null;
		String[] strs = relation.split(GREATER_THAN);
		for (int i=0; i < strs.length; i++) {
			if(i == 0) {
				parentContainer = rootContainer;
			}
			if(parentContainer.get(strs[i]) == null) {
				return false;
			}
			parentContainer = (Map<String, Object>)parentContainer.get(strs[i]);
		}
		return true;
	}
	
	/**
	 * 根据KEY获取其在MAP对象中的层级关系，前提是所有KEY是唯一的
	 * 2017年11月21日
	 * liu.jie
	 * @param rootContainer
	 * @param key
	 * @param relation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K> String getMapRelation(Map<K, Object> rootContainer, String key, String relation) {
		if (isEmpty(rootContainer) || StringUtils.isBlank(key)
				|| !existsMapObject(rootContainer, key)) {
			return relation;
		}
		
		for (K skey : rootContainer.keySet()) {
			if(key.equals(skey)) {
				if(StringUtils.isBlank(relation)) {
					relation += skey;
				} else {
					relation += GREATER_THAN + skey;
				}
				break;
			}
			if(!(rootContainer.get(skey) instanceof Map)) {
				continue;
			}
			if(!existsMapObject((Map<K, Object>)rootContainer.get(skey), key)) {
				continue;
			}
			
			if(StringUtils.isBlank(relation)) {
				relation += skey;
			} else {
				relation += GREATER_THAN + skey;
			}
			if(key.equals(skey)) {
				break;
			} else {
				relation = getMapRelation((Map<K, Object>)rootContainer.get(skey), key, relation);
			}
		}
		return relation.toString();
	}
	/**
	 * 根据KEY获取其在MAP对象中的层级关系，前提是所有KEY是唯一的
	 * 2017年11月21日
	 * liu.jie
	 * @param rootContainer
	 * @param key
	 * @return
	 */
	public static <K> String getMapRelation(Map<K, Object> rootContainer, String key) {
		return getMapRelation(rootContainer, key , StringUtils.EMPTY);
	}
	/**
	 * 根据KEY判断map对象是否存在，前提是所有KEY是唯一的
	 * 2017年11月21日
	 * liu.jie
	 * @param rootContainer
	 * @param key
	 * @param isExists			默认给false
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <K> boolean existsMapObject(Map<K, Object> rootContainer,
			String key, boolean isExists) {
		if(isEmpty(rootContainer) || StringUtils.isBlank(key)) {
			return isExists;
		}
		
		for (K skey : rootContainer.keySet()) {
			if(isExists) {
				break;
			}
			
			if(key.equals(skey)) {
				isExists = true;
				break;
			} else {
				if(rootContainer.get(skey) instanceof Map) {
					isExists = existsMapObject((Map<K, Object>)rootContainer.get(skey), key, isExists);
				}
			}
		}
		return isExists;
	}
	/**
	 *  根据KEY判断map对象是否存在，前提是所有KEY是唯一的
	 * 2017年11月21日
	 * liu.jie
	 * @param rootContainer
	 * @param key
	 * @return
	 */
	public static <K> boolean existsMapObject(Map<K, Object> rootContainer, String key) {
		return existsMapObject(rootContainer, key, false);
	}
	/**
	 * 将MAP对象转换成JSONTREE字符串
	 * 2017年11月22日
	 * liu.jie
	 * @param rootContainer
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static String convertMapToJsonTree(Map<String, Object> rootContainer, List<Map<String, Object>> list) {
		if(isEmpty(rootContainer)) {
			return StringUtils.EMPTY;
		}
		
		if(list == null) {
			list = Lists.newArrayList();
		}
		for (String key : rootContainer.keySet()) {
			
			if(getMapObjectByKey(rootContainer, key).get("children") == null) {
				list.add(getMapObjectByKey(rootContainer, key));
			} else {
				Map<String, Object> m = Maps.newHashMap();
				m.putAll(MapUtils.getMapObjectByKey(rootContainer, key));
				m.put("children", Lists.newArrayList());
				list.add(m);
				convertMapToJsonTree((Map<String, Object>)getMapObjectByKey(rootContainer, key).get("children"), (List<Map<String, Object>>)m.get("children"));
			}
		}
		return JSON.toJSONString(list);
	}
	/**
	 * 将MAP对象转换成JSONTREE字符串
	 * 2017年11月22日
	 * liu.jie
	 * @param rootContainer
	 * @return
	 */
	public static String convertMapToJsonTree(Map<String, Object> rootContainer) {
		return convertMapToJsonTree(rootContainer, null);
	}
	/**
	 * 将集合数据以上下级关系组装成MAP树
	 * 2017年12月17日
	 * liu.jie
	 * @param datas
	 * @return
	 */
	public static Map<String, Object> makeMapTree(List<Map<String, Object>> datas) {
		Map<String, Object> map = Maps.newHashMap();
		if(CollectionUtils.isEmpty(datas)) {
			return map;
		}
		
		String relation = StringUtils.EMPTY;
		for (Map<String, Object> data : datas) {
			if(data.get("PID") == null) {
				map.put(data.get("ID")+"", data);
			} else {
				relation = MapUtils.getMapRelation(map, data.get("PID")+"");
				MapUtils.getMapObject(map, relation+">children").put(data.get("ID")+"", data);
			}
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static void makeMapTree2(List<Map<String, Object>> datas, Map<String, Object> result) {
		if(CollectionUtils.isEmpty(datas)) {
			return;
		}
		
		result.put("children", Lists.newArrayList());
		for (Map<String, Object> data : datas) {
			if(data.get("PID") == null) {
				data.put("PID", "000000");
			}
			if(data.get("PID").equals(result.get("ID"))) {
				((List<Map<String, Object>>)result.get("children")).add(data);
				makeMapTree2(datas, data);
			}
		}
	}
	/**
	 * 组织tree结构json数据串
	 * 2017年8月29日
	 * liu.jie
	 * @param datas
	 * @return
	 */
	public static String convertListToJsonTree(List<Map<String, Object>> datas) {
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("ID", "000000");
		makeMapTree2(datas, resultMap);
		return JSON.toJSONString(resultMap.get("children"));
	}
	/**
	 * 过滤MAP中不属于KEY所在路径的“其它”
	 * 2017年12月17日
	 * liu.jie
	 * @param map
	 * @param key
	 * @return
	 */
	public static Map<String, Object> filterMapTreeOfKey(List<Map<String, Object>> datas, String key) {
		Map<String, Object> map = makeMapTree(datas);
		String relation = getMapRelation(map, key);
		String[] ids = relation.split(GREATER_THAN);
		
		Map<String, Object> pMap = null;
		for (int i = 0; i < ids.length; i++) {
			if(!"children".equals(ids[i]) && !key.equals(ids[i])) {
				getMapObject(map, getMapRelation(map, ids[i])).put("isExport", false);
			}
			if(i == 0 || "children".equals(ids[i])) {
				continue;
			}
			pMap = getMapObject(map, getMapRelation(map, ids[i-2])+">children");
			decMap(pMap, ids[i]);
		}
		return map;
	}
	/**
	 * MAP自减
	 * 2017年12月17日
	 * liu.jie
	 * @param map
	 * @param key
	 */
	public static void decMap(Map<String, Object> map, String key) {
		for (String id : map.keySet()) {
			if(!id.equals(key)) {
				map.remove(id);
				decMap(map, key);
				break;
			}
		}
	}
	
	/**
	 * 创建Map，传入一个数据，偶数下标为KEY、奇数下标为VALUE
	 * @param obj
	 * @return
	 * @author liu.jie
	 * @since 2018年10月23日
	 * @deprecated 请使用 {@link MapUtils#create(Object...)}
	 */
	@Deprecated	
	public static Map<String, Object> createParamMap(Object... obj) {
		Map<String, Object> param = Maps.newHashMap();
		if(ArrayUtils.isEmpty(obj)) {
			return param;
		}
		
		for (int i = 0; i < obj.length; i++) {
			if(i%2 == 0) {
				param.put(StringUtils.nvl(obj[i]), obj[i + 1]);
			}
		}
		return param;
	}
	
	public static void main(String[] args) {
		Map<String, Object> rootContainer = Maps.newHashMap();
//		getMapObject(rootContainer, "zzf");
//		getMapObject(rootContainer, "zzf>zf");
//		getMapObject(rootContainer, "zzf>zf1");
//		getMapObject(rootContainer, "zzf>zf1>bb1");
//		getMapObject(rootContainer, "zzf>zf>bb");
//		getMapObject(rootContainer, "zzf>zf>zbb");
//		getMapObject(rootContainer, "zzf>zf>zbb>ez");
		//System.out.println(getMapRelation(rootContainer, "bb1"));
		//System.out.println(getMapRelation(rootContainer, "bb1"));
		//System.out.println(existsMapObject(rootContainer, "bb1"));
		//System.out.println(getMapObjectByKey(rootContainer, "zf"));
		//System.out.println(filterMapTreeOfKey(rootContainer, "zf"));
	}
	
	/**
	 * 创建Map，传入一个数据，偶数下标为KEY、奇数下标为VALUE
	 * @param obj
	 * @return
	 * @author liu.jie
	 * @since 2018年10月23日
	 */
	public static Map<String, Object> create(Object... obj) {
		return creator(obj);
	}
	
	/**
	 * 新建Map对象，同一种类型的KEY/VALUE值
	 * @author liu.jie
	 * @since 2019年4月17日
	 * @param array
	 * @return
	 */
	@SafeVarargs
	public static <V> Map<String, V> creator(V... array) {
		Map<String, V> map = Maps.newHashMap();
		if(ArrayUtils.isEmpty(array)) {
			return map;
		}
		for (int i = 0; i < array.length; i++) {
			if(i%2 == 0) {
				map.put(StringUtils.nvl(array[i]), array[i + 1]);
			}
		}
		return map;
	}
	
	/**
	 * 根据上下级关系获取对象
	 * @author liu.jie
	 * @since 2019年4月16日
	 * @param rootContainer
	 * @param relation		"zf>yy>bb"：祖父>爷爷>爸爸
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, K> T get(Map<K, Object> rootContainer, String relation) {
		if(rootContainer == null || StringUtils.isBlank(relation)) {
			return null;
		}
		
		Map<K, Object> parentContainer = rootContainer;
		String[] strs = relation.split(GREATER_THAN);
		for (int i = 0; i < strs.length; i++) {
			if(parentContainer.get(strs[i]) == null) {
				parentContainer.put((K) strs[i], Maps.newHashMap());
			}
			if(i < strs.length - 1) {
				parentContainer = (Map<K, Object>)parentContainer.get(strs[i]);
			} else {
				return (T) parentContainer.get(strs[i]);
			}
		}
		return null;
	}
	/**
	 * 根据上下级关系获取MAP对象
	 * @author liu.jie
	 * @since 2019年4月24日
	 * @param rootContainer
	 * @param relation
	 * @return
	 */
	public static <K> Map<K, Object> getMap(Map<K, Object> rootContainer, String relation) {
		return get(rootContainer, relation);
	}
	/**
	 * 概据KEY值获取对象，前提是所有KEY是唯一的<br>
	 * 建议在关系不明的情况下使用，明确关系层次的情况请使用 {@link #get(Map, String)}
	 * @author liu.jie
	 * @since 2019年4月16日
	 * @param rootContainer
	 * @param key
	 * @return
	 */
	public static <T, K> T findByKey(Map<K, Object> rootContainer, String key) {
		if (isEmpty(rootContainer) || StringUtils.isBlank(key)
				|| !existsMapObject(rootContainer, key)) {
			return null;
		}
		return get(rootContainer, getMapRelation(rootContainer, key));
	}
	/**
	 * 按序创建数组
	 * @author liu.jie
	 * @since 2019年7月31日
	 * @param map
	 * @param keys
	 * @return
	 */
	public static Object[] createArray(Map<String, ?> map, String... keys) {
		if(isEmpty(map) || ArrayUtils.isEmpty(keys)) {
			return null;
		}
		return Arrays.stream(keys).map(f -> map.get(f))
				.collect(Collectors.toList()).toArray();
	}
	
	public static <V> String getStringIgnoreCase(Map<String, V> map, String key) {
		if(StringUtils.isBlank(key) || isEmpty(map)) {
			return null;
		}
		Optional<Entry<String, V>> opt = map.entrySet().parallelStream()
				.filter(f -> f.getKey().equalsIgnoreCase(key)).findFirst();
		if(opt.isPresent()) {
			return (String) opt.get().getValue();
		}
		return null;
	}
	/**
	 * key转小写
	 * @author liu.jie
	 * @since 2019年8月4日
	 * @param map
	 * @return
	 */
	public static <V> Map<String, V> keyToLowerCase(Map<String, V> map) {
		return map.entrySet().parallelStream()
			.collect(Collectors.toMap(f -> f.getKey().toLowerCase(), Map.Entry::getValue));
	}
	
}
