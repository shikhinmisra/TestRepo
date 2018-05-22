package com.middleware.restclient.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.middleware.restclient.service.table.restimpl.ConsignmentHeaderTableRestImpl;

@Component
public class RemoteDataReceiverClient {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	@Autowired
	ConsignmentHeaderTableRestImpl consignmentHeaderTableRestImpl;

	public void getRemoteTableDataForIndexId(String tableName, String indexId) {

		final String url = "http://localhost:9595/DataProviderApp/rest/tabledata/{tableName}/{id}";

		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("tableName", tableName);
		pathParams.put("id", indexId);
		
				
		if (tableName.equals("CONSIGNMENT_HEADER")) {
			consignmentHeaderTableRestImpl.processConsignmentHeaderTableRemoteDataForIndex(url,pathParams);
		}
		
	}

	
	public void getAllRemoteTableData(String tableName) {

		final String url = "http://localhost:9595/DataProviderApp/rest/tabledata/{tableName}";

		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("tableName", tableName);
		
				
		if (tableName.equals("CONSIGNMENT_HEADER")) {
			consignmentHeaderTableRestImpl.processConsignmentHeaderTableRemoteData(url,pathParams);
		}

		
	}
	
	
}
