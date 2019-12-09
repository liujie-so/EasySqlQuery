package com.iuie.basic.dao.hibernate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.hql.QuerySplitter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iuie.basic.dao.IBasicDao;
import com.iuie.basic.util.JsonUtils;
import com.iuie.basic.util.MapUtils;
import com.iuie.basic.util.SqlUtils;
import com.iuie.basic.util.StringUtils;
import com.iuie.basic.util.opretor.ISqlOperator;
import com.iuie.core.bo.Entity;
import com.iuie.core.dao.hibernate.SingleEntityDao;
import com.iuie.core.id.generator.ObjectId;

/**
 * 基础的DAO实现类
 * @author liu.jie
 * @since 2019年4月14日
 */
public class BasicDaoTemplate extends SingleEntityDao implements IBasicDao {
	
	protected final String SYSDATE = "sysdate";
	protected final String SYSTIMESTAMP = "systimestamp";
	protected final String UUID = "uuid";
	protected final ObjectId idGenerator = new ObjectId();

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> listForHql(String hql, int startPos, int pageSize) {
		return super.find(hql, startPos, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity<?>> T getBoById(T bo) {
		return (T)super.get(bo.getClass(), (Serializable) bo.getId());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity<?>> T getBoById(Serializable id, String entityName) {
		return (T) getSession().get(this.getEntityClassPath(entityName), id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> listForSql(String sql, Object[] params,
			int startPos, int pageSize) {
		return super.findByNativeSQL(sql, params, null, startPos, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity<?>> T saveEntity(T bo) {
		return (T)super.save(bo);
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public <T extends Entity<?>> T saveEntity(String jsonData,
			Map<String, String> dataMap) {
		return (T) this.saveEntity(JsonUtils.getGson().fromJson(jsonData,
				getEntityClass(dataMap.get(IBasicDao.ENTITY_NAME))));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> listForSql(String sql, Object[] params, Class<T> clz,
			int startPos, int pageSize) {
		return super.findByNativeSQL(sql, params, clz, startPos, pageSize);
	}

	@Override
	public int countForHql(String hql) {
		return super.findCount(hql);
	}

	@Override
	public int countForSql(String sql, Object... params) {
		return super.findCountByNativeSQL(sql, params);
	}

	@Override
	public int executeForSql(final String sql, final Object... params) {
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				if(ArrayUtils.isNotEmpty(params)) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(i, params[i]);
					}
				}
				return query.executeUpdate();
			}
		});
	}

	@Override
	public int executeForSql(String sql) {
		return executeForSql(sql, new Object[]{});
	}

	@Override
	public int countForSql(String sql) {
		return countForSql(sql, new Object[]{});
	}

	@Override
	public <T extends Entity<?>> void deleteEntity(T bo) {
		super.delete(bo);
	}
	
	@Override
	public int deleteEntity(Serializable id, String entityName) {
		return super.delete("delete " + entityName +" where id='" + id + "'");
	}

	@Override
	public List<Map<String, Object>> list(String tableName, String columns,
			Map<String, ?> params, int startPos, int pageSize,
			String... notInParamNames) {
		List<Object> paramValues = new ArrayList<Object>();
		String sql = SqlUtils.createSql(tableName, columns, params, paramValues, notInParamNames);
		return listForSql(sql.toString(), paramValues.toArray(), startPos, pageSize);
	}

	@Override
	public List<Map<String, Object>> listOfAllColumns(String tableName,
			Map<String, ?> params, int startPos, int pageSize) {
		return list(tableName, "*", params, startPos, pageSize);
	}

	@Override
	public List<Map<String, Object>> listOfAllColumns(String tableName,
			Map<String, ?> params, int startPos, int pageSize,
			String... notInParamNames) {
		return list(tableName, "*", params, startPos, pageSize, notInParamNames);
	}

	@Override
	public int count(String tableName, Map<String, ?> params,
			String... notInParamNames) {
		List<Object> paramValues = Lists.newArrayList();
		String sql = SqlUtils.createSql(tableName, SqlUtils.SQL_PART_COUNT, params, paramValues, notInParamNames);
		return super.findCountByNativeSQL(sql, paramValues.toArray());
	}

	@Override
	public int deleteForHql(String hql) {
		return super.delete(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity<?>> T getBoById(Serializable id, Class<T> clazz) {
		return (T) super.get(clazz, id);
	}

	@Override
	public boolean existsData(String tableName, Map<String, Object> params) {
		return count(tableName, params) > 0;
	}
	
	@Override
	public boolean existsData(String tableName, Object... args) {
		return existsData(tableName, MapUtils.create(args));
	}

	@Override
	public List<Map<String, Object>> listOfAllRows(String tableName,
			String columns, Map<String, ?> params) {
		return list(tableName, columns, params, 0, -1);
	}

	@Override
	public int executeForHql(String hql) {
		return super.delete(hql);
	}

	@Override
	public List<Map<String, Object>> listOfAllRows(String tableName,
			String columns, Map<String, ?> params, String... notInParamNames) {
		return list(tableName, columns, params, 0, -1, notInParamNames);
	}
	
	@Override
	public List<Map<String, Object>> listOfAllRows(String tableName,
			Map<String, ?> params) {
		return listOfAllRows(tableName, "*", params);
	}

	@Override
	public List<Map<String, Object>> listOfAllRows(String tableName,
			Map<String, ?> params, String... notInParamNames) {
		return listOfAllRows(tableName, "*", params, notInParamNames);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Class<Entity<T>> getEntityClass(String className) {
		try {
			return (Class<Entity<T>>) Class.forName(getEntityClassPath(className));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getEntityClassPath(String className) {
		return QuerySplitter.getImportedClass(className,
				(SessionFactoryImplementor) getSession().getSessionFactory());
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return null;
	}
	
	@Override
	public int insertData(String tableName, Map<String, Object> map,
			String... ignoredParamNames) {
		if(StringUtils.isBlank(tableName) || MapUtils.isEmpty(map)) {
			return 0;
		}
		
		if(!map.containsKey("create_by") && !map.containsKey("CREATE_BY")) {
			User user = getUser();
			if(user != null) {
				map.put("create_by", user.getUserName());
			} else {
				map.put("create_by", "without_login_user");
			}
		}
		if(!map.containsKey("create_time") && !map.containsKey("CREATE_TIME")) {
			map.put("create_time", "sysdate");
		}
		
		StringBuffer columns = new StringBuffer();
		StringBuffer values = new StringBuffer();
		
		List<Object> params = Lists.newArrayList();
		for(String key : map.keySet()) {
			if(ArrayUtils.contains(ignoredParamNames, key)) {
				continue;
			}
			if(StringUtils.isNotBlank(columns)) {
				columns.append(",");
				values.append(",");
			}
			columns.append(key);
			values.append("?");
			if(map.get(key) == null) {
				params.add(null);
				continue;
			}
			if ( SYSDATE.equalsIgnoreCase(MapUtils.getString(map, key)) 
							|| SYSTIMESTAMP.equalsIgnoreCase(MapUtils.getString(map, key)) ) {
				params.add(new Date(System.currentTimeMillis()));
			} else if(UUID.equalsIgnoreCase(MapUtils.getString(map, key))) {
				params.add(idGenerator.toHexString());
			} else {
				params.add(map.get(key));
			}
		}
		return executeForSql("insert into "+tableName+"("+columns+") values("+values+")", params.toArray());
	}

	@Override
	public User getUser() {
		return (User) SecurityUtils.getSubject().getPrincipal();
	}

	@Override
	public Clob createClob(String string) {
		return Hibernate.createClob(string, getSession());
	}

	@Override
	public Blob createBlob(InputStream stream) throws IOException {
		
		return Hibernate.createBlob(stream, stream.available(), getSession());
		
	}

	@Override
	public Blob createBlob(File file) throws IOException {
		return createBlob(new FileInputStream(file));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> listForHql(final String hql, final int startPos,
			final int pageSize, final Object... values) {
		return getHibernateTemplate().executeFind(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session)
					throws HibernateException {
				Query query = session.createQuery(hql);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				if (startPos > 0) {
					query.setFirstResult(startPos);
				}
				if (pageSize > 0) {
					query.setMaxResults(pageSize);
				}
				return query.list();
			}
		});
	}
	
	@Override
	public int executeForHql(final String hql, final Object... values) {
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.executeUpdate();
			}
			
		});
	}

	@Override
	public Map<String, Object> getOne(String tableName, Map<String, ?> params) {
		List<Map<String, Object>> rows = listOfAllRows(tableName, params);
		if(CollectionUtils.isEmpty(rows)) {
			return null;
		}
		return rows.get(0);
	}

	@Override
	public Map<String, Object> getOneForSql(String sql, Object... values) {
		List<Map<String, Object>> rows = listForSql(sql, values, 0, -1);
		if(CollectionUtils.isEmpty(rows)) {
			return null;
		}
		return rows.get(0);
	}

	@Override
	public <T> T getOneForHql(String hql, Object... values) {
		List<T> beans = listForHql(hql, 0, -1, values);
		if(CollectionUtils.isEmpty(beans)) {
			return null;
		}
		return beans.get(0);
	}

	@Override
	public List<Map<String, Object>> listSoe(String tableName, String columns,
			ISqlOperator[] soes, int startPos, int pageSize,
			String... notInParamNames) {
		List<Object> paramValues = new ArrayList<Object>();
		String sql = SqlUtils.createSql(tableName, columns, soes, paramValues, notInParamNames);
		return listForSql(sql.toString(), paramValues.toArray(), startPos, pageSize);
	}

	@Override
	public List<Map<String, Object>> listSoe(String tableName, String columns,
			ISqlOperator[] soes, int startPos, int pageSize) {
		return listSoe(tableName, columns, soes, startPos, pageSize, new String[]{});
	}

	@Override
	public List<Map<String, Object>> listOfAllRowsSoe(String tableName,
			String columns, ISqlOperator[] soes, String... ignoredParamNames) {
		return listSoe(tableName, columns, soes, 0, -1, ignoredParamNames);
	}
	
	@Override
	public int countSoe(String tableName, ISqlOperator[] soes,
			String[] ignoredParamNames) {
		List<Object> paramValues = new ArrayList<Object>();
		String sql = SqlUtils.createSql(tableName, SqlUtils.SQL_PART_COUNT,
				soes, paramValues, ignoredParamNames);
		return super.findCountByNativeSQL(sql, paramValues.toArray());
	}
	
	@Override
	public int countSoe(String tableName, ISqlOperator... soes) {
		return countSoe(tableName, soes, ArrayUtils.EMPTY_STRING_ARRAY);
	}
	
	@Override
	public boolean existsDataSoe(String tableName, ISqlOperator... soes) {
		return countSoe(tableName, soes, ArrayUtils.EMPTY_STRING_ARRAY) > 0;
	}

	@Override
	public List<Map<String, Object>> listOfAllRowsSoe(String tableName,
			String columns, ISqlOperator... soes) {
		return listOfAllRowsSoe(tableName, columns, soes, ArrayUtils.EMPTY_STRING_ARRAY);
	}

	@Override
	public List<Map<String, Object>> listOfAllSoe(String tableName) {
		return listOfAllRowsSoe(tableName, "*", new ISqlOperator[]{});
	}

	@Override
	public int delete(String table, ISqlOperator... ops) {
		Pair<String, List<Object>> p = SqlUtils.processWhere(ops);
		return executeForSql("delete " + table + " where 1=1 " + p.getLeft(), p.getRight().toArray());
	}
}
