package com.middleware.controller.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.middleware.controller.model.ConsignmentHeader;

@Component
public class LoadTableSelectedByUser {

	@Autowired
	LoadConsignmentHeadersTable loadConsignmentHeadersTable;
	
	public List<Object> getSelectedTableDetails (String tableName) {		
		
		List<Object> selectedTableDetails = new ArrayList<>();
		
		
		if (tableName.equals("CONSIGNMENT_HEADER")) {
			List<ConsignmentHeader> tableDetails = loadConsignmentHeadersTable.getConsignmentHeaderTable();
			
			for (int i =0; i<tableDetails.size(); ++i) {
				 selectedTableDetails.add(tableDetails.get(i)); 
			}
			
		}
				
		return selectedTableDetails;
	}
	
}
