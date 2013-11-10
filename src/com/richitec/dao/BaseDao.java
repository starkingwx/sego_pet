package com.richitec.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class BaseDao {
	protected JdbcTemplate jdbc;

	public void setDataSource(DataSource ds) {
		jdbc = new JdbcTemplate(ds);
	}
}
