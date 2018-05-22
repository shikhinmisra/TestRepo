package com.middleware.controller.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.middleware.controller.model.SchemaTables;

@Component
public class LoadHomePage {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<SchemaTables> getAllSchemaTables() {

		String sql = "SELECT TABLE_NAME FROM USER_TABLES WHERE TABLE_NAME NOT IN ('APP_USERS')";
		return jdbcTemplate.query(sql, new SchemaTableMapper());

	}

	private static final class SchemaTableMapper implements RowMapper<SchemaTables> {

		@Override
		public SchemaTables mapRow(ResultSet resultSet, int arg1) throws SQLException {
			// TODO Auto-generated method stub
			SchemaTables schemaTables = new SchemaTables();
			schemaTables.setTableName(resultSet.getString("TABLE_NAME"));
			return schemaTables;
		}

	}

}
