package com.richitec.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.richitec.util.ArrayUtil;
import com.richitec.util.StringUtil;

public class BaseDao {
	private static Log log = LogFactory.getLog(BaseDao.class);
	protected JdbcTemplate jdbc;
	private DataSource dataSource;

	public void setDataSource(DataSource ds) {
		this.dataSource = ds;
		jdbc = new JdbcTemplate(ds);
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	protected long insertAndReturnLastId(String sql, SqlParameter[] params,
			Object[] values, String[] keyColumnNames) {
		long id = -1;
		SqlUpdate insert = new SqlUpdate(getDataSource(), sql);
		insert.setParameters(params);
		insert.setReturnGeneratedKeys(true);
		insert.setGeneratedKeysColumnNames(keyColumnNames);
		insert.compile();

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		insert.update(values, keyHolder);
		id = keyHolder.getKey().longValue();

		return id;
	}

	protected long insert(String table, TableField[] values, String[] keys) {
		if (values == null || values.length == 0) {
			return -1;
		}
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("INSERT INTO ").append(table).append(" (");
		ArrayList<Object> objList = new ArrayList<Object>();
		List<SqlParameter> paramList = new ArrayList<SqlParameter>();
		StringBuffer valueBuffer = new StringBuffer();
		for (TableField field : values) {
			if (!StringUtil.isNullOrEmpty(field.getValue())) {
				sqlBuffer.append(" ").append(field.getName()).append(',');
				valueBuffer.append("?,");
				objList.add(field.getValue());
				paramList.add(new SqlParameter(field.getType()));
			}
		}
		sqlBuffer = StringUtil.deleteLastChar(sqlBuffer, ',');
		valueBuffer = StringUtil.deleteLastChar(valueBuffer, ',');
		
		sqlBuffer.append(") VALUES(").append(valueBuffer).append(")");
		
		SqlParameter[] params = new SqlParameter[paramList.size()];
		params = paramList.toArray(params);
		
		log.info("insert - sql: " + sqlBuffer);
		long id = insertAndReturnLastId(sqlBuffer.toString(), params,
				objList.toArray(), keys);
		return id;
	}
	
	protected int update(String table, TableField[] updateFields,String selection, TableField[] selectionArgs) {
		if (updateFields == null || updateFields.length == 0) {
			return -1;
		}
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("UPDATE ").append(table);
		sqlBuffer.append(" SET ");
		
		ArrayList<Object> objList = new ArrayList<Object>();
		ArrayList<Integer> typeList = new ArrayList<Integer>();
		for (TableField field : updateFields) {
			if (!StringUtil.isNullOrEmpty(field.getValue())) {
				log.info("param - name: " + field.getName() + " value: " + field.getValue());
				
				sqlBuffer.append(field.getName()).append("=?,");
				objList.add(field.getValue());
				typeList.add(field.getType());
			}
		}
		sqlBuffer = StringUtil.deleteLastChar(sqlBuffer, ',');
		if (!StringUtil.isNullOrEmpty(selection)) {
			sqlBuffer.append(" ").append(selection);
			for (TableField field : selectionArgs) {
				log.info("param - name: " + field.getName() + " value: " + field.getValue());
				objList.add(field.getValue());
				typeList.add(field.getType());
			}
		}
		int[] types = ArrayUtil.convertIntegerListToIntArray(typeList);
		log.info("update - sql: " + sqlBuffer);
		
		return jdbc.update(sqlBuffer.toString(), objList.toArray(), types);
	}

	public static class TableField {
		private String name;
		private String value;
		private int type;

		public TableField(String name, String value, int type) {
			this.name = name;
			this.value = value;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

	}
}
