package com.middleware.controller.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.middleware.controller.model.ConsignmentHeader;


@Component
public class LoadConsignmentHeadersTable {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<ConsignmentHeader> getConsignmentHeaderTable() {

		String sql = "SELECT * FROM CONSIGNMENT_HEADER";
		return jdbcTemplate.query(sql, new ConsignmentHeaderMapper());

	}

	private static final class ConsignmentHeaderMapper implements RowMapper<ConsignmentHeader> {

		@Override
		public ConsignmentHeader mapRow(ResultSet resultSet, int arg1) throws SQLException {
			// TODO Auto-generated method stub
			ConsignmentHeader consignmentHeader = new ConsignmentHeader();
			consignmentHeader.setConsignmentId(resultSet.getInt("CONSIGNMENT_ID"));
			consignmentHeader.setOrderId(resultSet.getInt("ORDER_ID"));
			consignmentHeader.setBuCode(resultSet.getString("BU_CODE"));
			consignmentHeader.setDateOfOrder(resultSet.getDate("DATE_OF_ORDER"));
			consignmentHeader.setCreatedDate(resultSet.getDate("CREATED_DATE"));
			return consignmentHeader;
		}
	}

}
