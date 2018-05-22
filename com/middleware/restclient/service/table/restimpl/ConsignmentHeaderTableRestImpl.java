package com.middleware.restclient.service.table.restimpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import com.middleware.controller.model.ConsignmentHeader;


@Controller
public class ConsignmentHeaderTableRestImpl {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void processConsignmentHeaderTableRemoteDataForIndex(String url, Map<String, String> pathParams) {

		RestTemplate consignmentDataRecevier = new RestTemplate();

		ConsignmentHeader consignmentModel = consignmentDataRecevier.getForObject(url,
				ConsignmentHeader.class, pathParams);

		Object[] params = new Object[] { consignmentModel.getConsignmentId(), consignmentModel.getOrderId(),
				consignmentModel.getBuCode(), consignmentModel.getDateOfOrder(), consignmentModel.getCreatedDate() };
		int[] types = new int[] { Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.DATE, Types.DATE };

		final String insertSql = "INSERT INTO CONSIGNMENT_HEADER (" + " consignment_id, " + " order_id, " + " bu_code, "
				+ " date_of_order, " + " created_date) " + "VALUES (?, ?,?,?,?)";

		jdbcTemplate.update(insertSql, params, types);

	}

	public void processConsignmentHeaderTableRemoteData(String url, Map<String, String> pathParams) {

		jdbcTemplate.execute("TRUNCATE TABLE CONSIGNMENT_HEADER");

		RestTemplate consignmentDataRecevier = new RestTemplate();

		System.out.println("before fetching data");

		ConsignmentHeader[] consignmentModel = consignmentDataRecevier.getForObject(url,
				ConsignmentHeader[].class, pathParams);

		ArrayList<ConsignmentHeader> ConsignmentHeaderRows = new ArrayList<ConsignmentHeader>(
				Arrays.asList(consignmentModel));

		System.out.println("Size of the array list : " + ConsignmentHeaderRows.size());

		final String insertSql = "INSERT INTO CONSIGNMENT_HEADER (" + " consignment_id, " + " order_id, " + " bu_code, "
				+ " date_of_order, " + " created_date) " + "VALUES (?, ?,?,?,?)";

		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return ConsignmentHeaderRows.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {

				ConsignmentHeader ConsignmentHeader = ConsignmentHeaderRows.get(i);
				ps.setInt(1, ConsignmentHeader.getConsignmentId());
				ps.setInt(2, ConsignmentHeader.getOrderId());
				ps.setString(3, ConsignmentHeader.getBuCode());
				ps.setDate(4, ConsignmentHeader.getDateOfOrder());
				ps.setDate(5, ConsignmentHeader.getCreatedDate());
			}
		});

	}

}
