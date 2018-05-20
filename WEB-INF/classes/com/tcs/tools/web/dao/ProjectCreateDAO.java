package com.tcs.tools.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class ProjectCreateDAO {
	 private Connection lConnection= null;
	 private PreparedStatement  lPreparedStatement = null;
	 private ResultSet lResultSet = null;
	 
	 
	 /**
	 	 * method to return the sequence
	 	 * 
	 	 * @return
	 	 */
	 	public String createProjectId() {
	 		String lRetSeqValue = null;
	 		try {
	 			lConnection = DBConnectionManager.getConnection();

	 			// select the data from the table

	 			String lSQL = "UPDATE tool_project_sequence_table  SET PROJECT_ID_SEQ=PROJECT_ID_SEQ+1 ";
	 			String lSQL1 = "SELECT PROJECT_ID_SEQ FROM tool_project_sequence_table  ";

	 			lPreparedStatement = lConnection.prepareStatement(lSQL);
	 			//lPreparedStatement.setString(1, pProjectId);
	 			lPreparedStatement.executeUpdate();
	 			//lConnection.commit();

	 			lPreparedStatement = lConnection.prepareStatement(lSQL1);
	 			//lPreparedStatement.setString(1, pProjectId);
	 			lResultSet = lPreparedStatement.executeQuery();
	 			
	 			// if rs == null, then there is no ResultSet to view
	 			if (lResultSet != null) {
	 				while (lResultSet.next()) {
	 					lRetSeqValue = "PRID"+lResultSet.getString("PROJECT_ID_SEQ");
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
	 		return lRetSeqValue;

	 	}
	 	
	 	public boolean checkProjectExists(String pProjectName) {
	 		
			boolean lDataCount = false;
		 		try {
		 		
		 			
		 			lConnection = DBConnectionManager.getConnection();
		 			//lConnection.setAutoCommit(false);
		 			// prepare the query
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_DISTINCT_FILE_PROJECT_DETAILS);
		 			lPreparedStatement.setString(1, pProjectName);
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			
		 			if(lResultSet != null){
		 				while(lResultSet.next()){
		 					if(lResultSet.getInt("COUNT") > 0){
		 						lDataCount = true;
		 					}
		 				}
		 			}
		 		}catch (SQLException pSQLException) {
		 			pSQLException.printStackTrace();
		 		} catch (Exception pException) {
		 			pException.printStackTrace();
		 		} finally {
		 			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
		 			DBConnectionManager.closeConnection(lConnection);
		 		}
		 		return lDataCount;
	 	}
	 
}
