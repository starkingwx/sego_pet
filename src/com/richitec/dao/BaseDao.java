package com.richitec.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class BaseDao {
	protected JdbcTemplate jdbc;
	private DataSource dataSource;
	public void setDataSource(DataSource ds) {
		this.dataSource = ds;
		jdbc = new JdbcTemplate(ds);
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	
	protected long insertAndReturnLastId(String sql, SqlParameter[] params, Object[] values, String[] keyColumnNames) {
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
}
