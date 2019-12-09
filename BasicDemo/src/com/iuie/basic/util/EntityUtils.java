package com.iuie.basic.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

import com.google.common.collect.Maps;
import com.iuie.core.ApplicationContext;
/**
 * Hibernate实体工具
 * @author liu.jie
 * @since 2019年3月12日
 */
public class EntityUtils {
	private static Configuration cfg;
	/**
	 * 冒号
	 */
	private static final String COLON = ":";
	
	private static Configuration getHibernateCfg() {
		LocalSessionFactoryBean factory = (LocalSessionFactoryBean) ApplicationContext
				.getBean("&new_sessionFactory");
		if(cfg == null) {
			cfg = factory.getConfiguration();
		}
		return cfg;
	}
	
	private static PersistentClass getPersistentClass(Class<?> clazz) {
		synchronized (EntityUtils.class) {
			PersistentClass pc = getHibernateCfg().getClassMapping(clazz.getName());
			if(pc == null) {
				cfg = getHibernateCfg().addClass(clazz);
				pc = getHibernateCfg().getClassMapping(clazz.getName());
			}
			return pc;
		}
	}
	
	public static String getTableName(Class<?> clazz) {
		return getPersistentClass(clazz).getTable().getName();
	}
	
	public static String getColumnName(Class<?> clazz, String propertyName) {
		PersistentClass pc = getPersistentClass(clazz);
		Property property = pc.getProperty(propertyName);
		Iterator<?> it = property.getColumnIterator();
		if(it.hasNext()) {
			Column column = (Column) it.next();
			return column.getName();
		}
		return null;
	}
	
	/**
	 * 合并对象
	 * @author liu.jie
	 * @since 2019年4月3日
	 * @param ent1
	 * @param ent2
	 * @param fields		属性关系：xm:xm,sfzhm:sfzh，属性相同的可简写：mz,whcd
	 */
	public static <T1, T2> void mergeEntity(T1 ent1, T2 ent2, String... relations) {
		if(ent1 == null || ent2 == null || ArrayUtils.isEmpty(relations)) {
			return;
		}
		try {
			for(String rela : relations) {
				String[] fields = rela.split(COLON);
				String field1 = "";
				String field2 = "";
				if(fields.length == 2) {
					field1 = fields[0];
					field2 = fields[1];
				} else {
					field1 = field2 = fields[0];
				}
				if(StringUtils.isBlank(BeanUtils.getProperty(ent1, field1))) {
					BeanUtils.setProperty(ent1, field1, BeanUtils.getProperty(ent2, field2));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 覆写对象
	 * @author liu.jie
	 * @since 2019年4月10日
	 * @param ent1
	 * @param ent2
	 * @param relations		属性关系：xm:xm,sfzhm:sfzh，属性相同的可简写：mz,whcd
	 */
	public static <T1, T2> void extendEntity(T1 ent1, T2 ent2, String... relations) {
		if(ent1 == null || ent2 == null || ArrayUtils.isEmpty(relations)) {
			return;
		}
		try {
			for(String rela : relations) {
				String[] fields = rela.split(COLON);
				String field1 = "";
				String field2 = "";
				if(fields.length == 2) {
					field1 = fields[0];
					field2 = fields[1];
				} else {
					field1 = field2 = fields[0];
				}
				BeanUtils.setProperty(ent1, field1, BeanUtils.getProperty(ent2, field2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 替换实体中的属性
	 * @author liu.jie
	 * @since 2019年5月7日
	 * @param datas
	 * @param name
	 * @param parent
	 */
	public static void replaceObjs(List<?> datas, String name, Object obj) {
		if(CollectionUtils.isEmpty(datas)) {
			return;
		}
		for(Object data : datas) {
			try {
				BeanUtils.setProperty(data, name, obj);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 复制bean属性，构造map对象
	 * @author liu.jie
	 * @since 2019年5月24日
	 * @param bean
	 * @param map
	 * @param propNames
	 */
	public static <T> void copyProperties(T bean, Map<String, String> map,
			String... propNames) {
		if(bean == null || ArrayUtils.isEmpty(propNames)) {
			return;
		}
		if(map == null) {
			map = Maps.newHashMap();
		}
		try {
			for (String name : propNames) {
				map.put(name, BeanUtils.getProperty(bean, name));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
