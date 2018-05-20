package com.tcs.tools.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dto.IdentifyPatternDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class IdentifyPatternDAO {
	 private Connection lConnection= null;
	 private PreparedStatement  lPreparedStatement = null;
	 private ResultSet lResultSet = null;
	 
	 
	 public List getProcNameList(String pProjectId,String pRecType) {
	 		List<IdentifyPatternDTO> lProcNameList = new ArrayList<IdentifyPatternDTO>();
	 		try {
	 			lConnection = DBConnectionManager.getConnection();

	 			// select the data from the table
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_PROC_NAMES_FOR_IDENTIFY);
	 			lPreparedStatement.setString(1, pProjectId);
	 			lPreparedStatement.setString(2, pRecType);
	 			lResultSet = lPreparedStatement.executeQuery();
	 			
	 			IdentifyPatternDTO lIdentifyPatternDTO = new IdentifyPatternDTO();
	 			// if rs == null, then there is no ResultSet to view
	 			if (lResultSet != null) {
	 				while (lResultSet.next()) {
	 					lIdentifyPatternDTO = new IdentifyPatternDTO();
	 					lIdentifyPatternDTO.setProcName(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")));
	 					//System.out.println("::::inside loop");
	 					lProcNameList.add(lIdentifyPatternDTO);
	 				}
	 			}
	 		} catch (SQLException se) {
	 			se.printStackTrace();
	 			return null;
	 		} catch (Exception e) {
	 			e.printStackTrace();
	 			return null;
	 		} finally {
	 			// close the connection and the result set
	 			DBConnectionManager.closeConnection(lPreparedStatement,
	 					lResultSet);
	 			DBConnectionManager.closeConnection(lConnection);
	 		}
	 		return lProcNameList;

	 	}
	 
	 
	 /**
	 	 * method to return the sequence
	 	 * 
	 	 * @return
	 	 */
	 	public String getRunSeq() {
	 		String lRetSeqValue = null;
	 		try {
	 			//lConnection = DBConnectionManager.getConnection();

	 			// select the data from the table

	 			String lSQL = " UPDATE TOOL_IDENTIFY_PATTERN_SEQUENCE SET SEQ_NO=SEQ_NO+1";
	 			String lSQL1 = " SELECT SEQ_NO FROM TOOL_IDENTIFY_PATTERN_SEQUENCE";

	 			lPreparedStatement = lConnection.prepareStatement(lSQL);
	 			lPreparedStatement.executeUpdate();
	 			//lConnection.commit();

	 			lPreparedStatement = lConnection.prepareStatement(lSQL1);
	 			lResultSet = lPreparedStatement.executeQuery();
	 			// if rs == null, then there is no ResultSet to view
	 			if (lResultSet != null) {
	 				while (lResultSet.next()) {
	 					lRetSeqValue = lResultSet.getString("SEQ_NO");
	 				}
	 			}
	 		} catch (SQLException se) {
	 			se.printStackTrace();
	 			return null;
	 		} catch (Exception e) {
	 			e.printStackTrace();
	 			return null;
	 		} finally {
	 			// close the connection and the result set
	 			DBConnectionManager.closeConnection(lPreparedStatement,
	 					lResultSet);
	 			//DBConnectionManager.closeConnection(lConnection);
	 		}
	 		return lRetSeqValue;

	 	}
	 	
	 	 public String insertIdentifyPatternDetails(String pProjectId,String pMigrationMode,String pAnalysisType,String pPartialSPSelected,String pCreatedBy,String pReportType) {
			 String lSeqNo="";
		 		try {
		 			
		 			lConnection = DBConnectionManager.getConnection();
		 			lConnection.setAutoCommit(false);
		 			 lSeqNo = getRunSeq(); 
		 			
		 			// prepare the query
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.INSERT_IDENTIFY_PATTERN_MAIN);
		 			lPreparedStatement.setString(1, lSeqNo);
		 			lPreparedStatement.setString(2, pProjectId);		 			
		 			lPreparedStatement.setString(4, pAnalysisType);
		 			lPreparedStatement.setString(3, pMigrationMode);
		 			lPreparedStatement.setString(5, pCreatedBy);
		 			lPreparedStatement.setTimestamp(6, new Timestamp(System
		 					.currentTimeMillis()));
		 			lPreparedStatement.setString(7, pReportType);
		 			
		 			lPreparedStatement.executeUpdate();
		 			
		 			if("partial".equalsIgnoreCase(pMigrationMode)){
		 				String[] lPartialSPList  = pPartialSPSelected.split(",");
		 				for (int i = 0; i < lPartialSPList.length; i++) {
		 					
		 					if(lPartialSPList[i] == null || "".equals(lPartialSPList[i])) {
		 						continue;
		 					}
		 					
		 					lPreparedStatement = lConnection.prepareStatement(WebConstant.INSERT_IDENTIFY_PATTERN_SP_LIST);
				 			lPreparedStatement.setString(1, lSeqNo);
				 			lPreparedStatement.setString(2, lPartialSPList[i]);
				 			lPreparedStatement.setString(3, pCreatedBy);
				 			lPreparedStatement.setTimestamp(4, new Timestamp(System
				 					.currentTimeMillis()));
				 			lPreparedStatement.executeUpdate();
		 					
						}
		 			}
		 			lConnection.commit();
		 			lConnection.setAutoCommit(true);
		 		} catch (SQLException pSQLException) {
		 			pSQLException.printStackTrace();
		 		} catch (Exception pException) {
		 			pException.printStackTrace();
		 		} finally {
		 			DBConnectionManager.closeConnection(lPreparedStatement,
		 					lResultSet);
		 			DBConnectionManager.closeConnection(lConnection);
		 		}
		 		return lSeqNo;

		 	}
	
	 
}
