package com.iuie.basic.util;

import com.iuie.basic.dao.IBasicDao;
import com.iuie.core.ApplicationContext;

/**
 * 获取Bean工具类
 * @author liu.jie
 * @since 2019年4月23日
 */
public class SpringBeanUtils {
	/**
	 * ynfk_res库连接dao
	 */
	public final static String BASIC_DAO = "basicDao";
	/**
	 * ynfk_ywk库连接dao
	 */
	public final static String BASIC_DATA_DAO = "basicDataDao";
	/**
	 * ynfk_ztk库连接dao
	 */
	public final static String BASIC_ZTK_DAO = "basicZtkDao";
	/**
	 * 获取Bean对象
	 * @author liu.jie
	 * @since 2019年4月23日
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		try {
			return (T) ApplicationContext.getBean(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 新建basicDao对象
	 * @author liu.jie
	 * @since 2019年4月23日
	 * @return
	 */
	public static IBasicDao createBasicDao() {
		return getBean(BASIC_DAO);
	}
	/**
	 * 新建basicDataDao对象
	 * 需要将basicDataDao配置成非单例模式
	 * @author liu.jie
	 * @since 2019年4月23日
	 * @return
	 */
	public static IBasicDao createBasicDataDao() {
		return getBean(BASIC_DATA_DAO);
	}
	/**
	 * 新建basicZtkDao对象
	 * 需要将basicZtkDao配置成非单例模式
	 * @author chen.yingwei
	 * @since 2019年8月20日
	 * @return
	 */
	public static IBasicDao createBasicZtkDao() {
		return getBean(BASIC_ZTK_DAO);
	}
}
