package com.middleware.controller.dao;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserCredentialsDaoImpl {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public String getUserPassword(String userName) {
		try {
		String sql = "SELECT PASSWORD FROM APP_USERS WHERE USER_NAME=?";
		String resultSet = jdbcTemplate.queryForObject(sql, new Object[] {userName}, String.class);
		if ((resultSet != "") || (resultSet != null)){
		return resultSet;
		} 
		else {
			return "invaliduser";
		}
		}
		catch (DataAccessException e) {
			return "invaliduser";
		}		
	}
	
	public void registerNewUser(String userName, String password) {
		
		@SuppressWarnings("deprecation")
		int maxId = jdbcTemplate.queryForInt("SELECT MAX(USER_ID) FROM APP_USERS");		
		Object[] params = new Object[] {maxId+1, userName, password};
		int[] types = new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR};

		
		
		final String insertSql = "INSERT INTO APP_USERS (" +" user_id, " +" user_name, " +" password) " +
				"VALUES (?, ?,?)";
		int row = jdbcTemplate.update(insertSql, params, types);
	}
	
	
}
