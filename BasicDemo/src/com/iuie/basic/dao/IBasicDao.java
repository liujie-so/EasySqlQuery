package com.iuie.basic.dao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.Clob;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.struts2.ServletActionContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.iuie.basic.util.MapUtils;
import com.iuie.basic.util.StringUtils;
import com.iuie.basic.util.opretor.EqualsOperator;
import com.iuie.basic.util.opretor.ISqlOperator;
import com.iuie.basic.util.opretor.IsNullOperator;
import com.iuie.core.action.bean.JsonBean;
import com.iuie.core.bo.Entity;
import com.iuie.core.dao.ISingleEntityDao;
import com.iuie.hcrw.constants.HcrwConstants;
import com.iuie.sys.bo.User;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 基础的DAO实现接口
 * @author liu.jie
 *
 */
public interface IBasicDao extends ISingleEntityDao {
	public final static String UTF8 = "UTF-8";
	
	/**
	 * 实体参数名
	 */
	public final static String ENTITY_NAME = "entity";
	/**
	 * 实体主键名
	 */
	public final static String ENTITY_PK_NAME = "id";
	/**
	 * 实体外键名
	 */
	public final static String ENTITY_FK_NAME = "pid";
	/**
	 * 实体多个主键名
	 */
	public final static String ENTITY_PKS_NAME = "ids";
	/**
	 * 执行HQL语句查询
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param hql
	 * @param startPos
	 * @param pageSize
	 * @return
	 */
	public <T> List<T> listForHql(String hql, int startPos, int pageSize);
	/**
	 * 获取实体对象
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param bo
	 * @return
	 */
	public <T extends Entity<?>> T getBoById(T bo);
	/**
	 * 获取实体对象
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param id
	 * @param entityName
	 * @return
	 */
	public <T extends Entity<?>> T getBoById(Serializable id, String entityName);
	/**
	 * 获取实体对象
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param id
	 * @param clazz
	 * @return
	 */
	public <T extends Entity<?>> T getBoById(Serializable id, Class<T> clazz);
	/**
	 * 执行SQL查询
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param sql
	 * @param params
	 * @param startPos
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> listForSql(String sql, Object[] params, int startPos, int pageSize);
	/**
	 * 执行SQL查询
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param sql
	 * @param params
	 * @param clz
	 * @param startPos
	 * @param pageSize
	 * @return
	 */
	public <T> List<T> listForSql(String sql, Object[] params, final Class<T> clz, int startPos, int pageSize);
	/**
	 * 保存实体对象
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param bo
	 * @return
	 */
	public <T extends Entity<?>> T saveEntity(T bo);
	/**
	 * 保存实体对象
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param jsonData
	 * @param dataMap
	 * @return
	 */
	public <T extends Entity<?>> T saveEntity(String jsonData, Map<String, String> dataMap);
	/**
	 * 执行HQL的计数COUNT
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param hql
	 * @return
	 */
	public int countForHql(String hql);
	/**
	 * 执行SQL的计数COUNT
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param sql
	 * @param params
	 * @return
	 */
	public int countForSql(String sql, Object... params);
	/**
	 * 执行SQL语句
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param sql
	 * @param params
	 * @return
	 */
	public int executeForSql(String sql, Object... params);
	/**
	 * 执行SQL语句
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param sql
	 * @return
	 */
	public int executeForSql(String sql);
	/**
	 * 执行SQL的COUNT计数
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param sql
	 * @return
	 */
	public int countForSql(String sql);
	/**
	 * 删除实体对象
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param bo
	 */
	public <T extends Entity<?>> void deleteEntity(T bo);
	/**
	 * 删除实体对象
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param id
	 * @param entityName
	 * @return
	 */
	public int deleteEntity(Serializable id, String entityName);
	
	/**
	 * 执行SQL查询
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param tableName
	 * @param columns
	 * @param params
	 * @param startPos
	 * @param pageSize
	 * @param notInParamNames
	 * @return
	 */
	public List<Map<String, Object>> list(String tableName, String columns, 
			Map<String, ?> params, int startPos, int pageSize, String... notInParamNames);
	/**
	 * 执行所有列的SQL查询
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param tableName
	 * @param params
	 * @param startPos
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> listOfAllColumns(String tableName, 
			Map<String, ?> params, int startPos, int pageSize);
	/**
	 * 执行所有列的SQL查询
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param tableName
	 * @param params
	 * @param startPos
	 * @param pageSize
	 * @param notInParamNames
	 * @return
	 */
	public List<Map<String, Object>> listOfAllColumns(String tableName,
			Map<String, ?> params, int startPos, int pageSize, String... notInParamNames);
	
	/**
	 * 执行SQL的COUNT计数
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param tableName
	 * @param params
	 * @param notInParamNames
	 * @return
	 */
	public int count(String tableName, Map<String, ?> params, String... notInParamNames);
	/**
	 * 执行HQL语句，删除实体对象
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param hql
	 * @return
	 */
	public int deleteForHql(String hql);
	/**
	 * 判断数据是否存在
	 * @author liu.jie
	 * @since  2018年11月29日
	 * @param tableName
	 * @param params
	 * @return
	 */
	public boolean existsData(String tableName, Map<String, Object> params);
	/**
	 * 判断数据是否存在
	 * @author liu.jie
	 * @since  2018年11月29日
	 * @param tableName
	 * @param params
	 * @return
	 */
	public boolean existsData(String tableName, Object... args);
	/**
	 * 查询所有记录
	 * @author liu.jie
	 * @since 2019年1月3日
	 * @param tableName
	 * @param columns
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> listOfAllRows(String tableName,
			String columns, Map<String, ?> params);
	/**
	 * 查询所有记录
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param tableName
	 * @param columns
	 * @param params
	 * @param notInParamNames
	 * @return
	 */
	public List<Map<String, Object>> listOfAllRows(String tableName,
			String columns, Map<String, ?> params, String... notInParamNames);
	/**
	 * 查询所有记录
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param tableName
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> listOfAllRows(String tableName,
			Map<String, ?> params);
	/**
	 * 查询所有记录
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param tableName
	 * @param params
	 * @param notInParamNames
	 * @return
	 */
	public List<Map<String, Object>> listOfAllRows(String tableName,
			Map<String, ?> params, String... notInParamNames);
	/**
	 * 执行HQL语句
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param hql
	 * @return
	 */
	public int executeForHql(String hql);
	/**
	 * 执行HQL语句
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param hql
	 * @return
	 */
	public int executeForHql(String hql, Object... values);
	/**
	 * 根据类名获取类型
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param className
	 * @return
	 */
	public <T> Class<Entity<T>> getEntityClass(String className);
	/**
	 * 根据类名获取类路径
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param className
	 * @return
	 */
	public String getEntityClassPath(String className);
	/**
	 * 获取JdbcTemplate对象
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @return
	 */
	public JdbcTemplate getJdbcTemplate();

	/**
	 * 数据插入
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @param tableName
	 * @param map
	 * @param ignoredParamNames
	 * @return
	 */
	public int insertData(String tableName, Map<String, Object> map,
			String... ignoredParamNames);
	
	/**
	 * 获取User对象
	 * @author liu.jie
	 * @since 2019年4月14日
	 * @return
	 */
	public User getUser();
	/**
	 * 创建Blob对象
	 * @author liu.jie
	 * @since 2019年4月15日
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	public Blob createBlob(InputStream stream) throws IOException;
	/**
	 * 创建Blob对象
	 * @author liu.jie
	 * @since 2019年4月15日
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Blob createBlob(File file) throws IOException;
	/**
	 * 创建Clob对象
	 * @author liu.jie
	 * @since 2019年4月15日
	 * @param string
	 * @return
	 */
	public Clob createClob(String string);
	/**
	 * HQL查询
	 * @author liu.jie
	 * @since 2019年5月7日
	 * @param hql
	 * @param startPos
	 * @param pageSize
	 * @param values
	 * @return
	 */
	public <T> List<T> listForHql(String hql, int startPos, int pageSize, Object... values);
	/**
	 * 查询唯一记录
	 * @author liu.jie
	 * @since 2019年5月10日
	 * @param tableName
	 * @param params
	 * @return
	 */
	public Map<String, Object> getOne(String tableName, Map<String, ?> params);
	/**
	 * 查询唯一记录
	 * @author liu.jie
	 * @since 2019年5月10日
	 * @param sql
	 * @param values
	 * @return
	 */
	public Map<String, Object> getOneForSql(String sql, Object... values);
	/**
	 * 通过HQL查询唯一记录
	 * @author liu.jie
	 * @since 2019年5月17日
	 * @return
	 */
	public <T> T getOneForHql(String hql, Object... values);
	
	public List<Map<String, Object>> listSoe(String tableName, String columns, 
			ISqlOperator[] soes, int startPos, int pageSize, String... notInParamNames);
	
	public List<Map<String, Object>> listSoe(String tableName, String columns, 
			ISqlOperator[] soes, int startPos, int pageSize);
	
	public List<Map<String, Object>> listOfAllRowsSoe(String tableName,
			String columns, ISqlOperator[] soes, String... ignoredParamNames);
	
	public List<Map<String, Object>> listOfAllRowsSoe(String tableName,
			String columns, ISqlOperator... soes);
	
	public int countSoe(String tableName, ISqlOperator[] soes,
			String[] ignoredParamNames);
	
	public int countSoe(String tableName, ISqlOperator... soes);
	
	public boolean existsDataSoe(String tableName, ISqlOperator... soes);
	/**
	 * 查询全部
	 * @author liu.jie
	 * @since 2019年7月12日
	 * @param tableName
	 * @return
	 */
	public List<Map<String, Object>> listOfAllSoe(String tableName);
	/**
	 * SQL删除
	 * @author liu.jie
	 * @since 2019年8月16日
	 * @param table
	 * @param ops
	 * @return
	 */
	int delete(String table, ISqlOperator... ops);
	/**
	 * 生成校验条件表达式
	 * @author liu.jie
	 * @since 2019年8月23日
	 * @param values
	 * @return
	 */
	default <T> ISqlOperator[] createUniquePas(T... values) {
		Objects.requireNonNull(values);
		Map<String, Object> m = MapUtils.create(values);
		List<ISqlOperator> ls1 = m.entrySet().parallelStream()
				.filter(f -> StringUtils.isBlank(f.getValue()))
				.map(f -> new IsNullOperator((String) f.getKey(), 0))
				.collect(Collectors.toList());
		List<ISqlOperator> ls2 = m
				.entrySet()
				.parallelStream()
				.filter(f -> StringUtils.isNotBlank(f.getValue()))
				.map(f -> new EqualsOperator((String) f.getKey(), f.getValue()))
				.collect(Collectors.toList());
		ls1.addAll(ls2);
		return ls1.toArray(new ISqlOperator[]{});
	}
	/**
	 * 报告导出
	 * @author liu.jie
	 * @since 2019年8月15日
	 * @param fname 文件名
	 * @param path 模板路径
	 * @param template 模板名
	 * @param dataModel 数据
	 * @param contentType 导出文件类型
	 * @throws Exception
	 */
	default void exportReport(String fname, String path, String template,
			Object dataModel, String contentType) throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		PrintWriter writer = response.getWriter();
		response.setCharacterEncoding(UTF8);
		response.setContentType(contentType);
		
		String suffix = ".doc";
		switch (contentType) {
		case HcrwConstants.EXP_EXCEL:
			suffix = ".xls";
			break;
		default:
			break;
		}
		// 设置浏览器以下载的方式处理该文件默认名为resume.doc  
		response.addHeader(
				"Content-disposition",
				"attachment;filename="
						+ URLEncoder.encode(fname.replaceAll(":", "_")
								.replaceAll(" ", "_"), UTF8) + suffix);
		Configuration configuration = new Configuration(
				Configuration.VERSION_2_3_21);
		configuration.setDefaultEncoding(UTF8);
		configuration.setClassForTemplateLoading(this.getClass(), path);
		Template t = configuration.getTemplate(template);
		t.process(dataModel, writer);
	}
	
	default void exportReportWord(String fname, String path, String template,
			Object dataModel) throws Exception {
		exportReport(fname, path, template, dataModel, HcrwConstants.EXP_WORD);
	}
	/**
	 * 查询条件带上行政区划信息，锁定用户可查看数据
	 * @author liu.jie
	 * @since 2019年3月12日
	 * @param params
	 * @param field
	 * @return
	 */
	public boolean userPower(Map<String, String> params, String field);
	public String processUserLevel();
	public String getUserXzqh();
	
	/**
	 * 数据导出
	 * @author liu.jie
	 * @since 2019年3月14日
	 * @param dataMap
	 * @return
	 */
	public void exportData(Map<String, String> dataMap);
	/**
	 * 删除数据
	 * @author liu.jie
	 * @since 2019年11月19日
	 * @param dataMap
	 * @return
	 */
	public JsonBean deleteData(Map<String, String> dataMap);
}
