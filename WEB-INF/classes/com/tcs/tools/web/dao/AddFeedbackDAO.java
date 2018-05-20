package com.tcs.tools.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.tcs.tools.business.compare.dto.ComparedSummaryDTO;
import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dto.ChartReportDTO;
import com.tcs.tools.web.dto.FeedbackDTO;
import com.tcs.tools.web.dto.SPPatternAnalysisReportDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class AddFeedbackDAO {
	private Connection lConnection= null;
	private PreparedStatement  lPreparedStatement = null;
	private ResultSet lResultSet = null;
	private static Logger logger = Logger.getLogger("ToolLogger");
	
	
		public int insertFeedbackDetails(HashMap feedbackDetailsMap) {
			
			 int lInsertCount=0;
				
		 		try {
		 			
		 			
		 			lConnection = DBConnectionManager.getConnection();
		 			lConnection.setAutoCommit(false);
		 			String sql="insert into user_feedback_table(EMP_NAME,EMP_ID,SUBJECT,MESSAGE,CREATED_DATE) values(?,?,?,?,?)";
		 			
		 				lPreparedStatement = lConnection.prepareStatement(sql);
			 		
		 				logger.info("::::::inside updateProjectSPUploadDetails:::query to be executed:::;"+sql);
		 				
		 				
			 			lPreparedStatement.setString(1, ToolsUtil.replaceNull((String)feedbackDetailsMap.get("EMP_NAME")));
			 			lPreparedStatement.setString(2, ToolsUtil.replaceNull((String)feedbackDetailsMap.get("EMP_ID")));
			 			lPreparedStatement.setString(3, ToolsUtil.replaceNull((String)feedbackDetailsMap.get("SUBJECT")));
			 			lPreparedStatement.setString(4, ToolsUtil.replaceNull((String)feedbackDetailsMap.get("MESSAGE")));
			 			lPreparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			 			
			 			lInsertCount = lPreparedStatement.executeUpdate();	
		 			
		 			System.out.println("::::::inside feedback insert:::::"+lInsertCount);
		 			lConnection.commit();
		 			lConnection.setAutoCommit(true);
		 		
		 		
		 		} catch (SQLException pSQLException) {
		 			pSQLException.printStackTrace();
		 		} catch (Exception pException) {
		 			pException.printStackTrace();
		 		} finally {
		 			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
		 			DBConnectionManager.closeConnection(lConnection);
		 		}
		 		return lInsertCount;
		}
		
		
		
		
		 
		 
		 public ArrayList viewFeedback() {
			ArrayList<FeedbackDTO> feedbackList=new ArrayList<FeedbackDTO>();
		 		try {
		 			lConnection = DBConnectionManager.getConnection();
		 			//if("viewFeedback".equalsIgnoreCase(pMode)){
		 			// select the data from the table
		 			lPreparedStatement = lConnection.prepareStatement("select EMP_NAME,EMP_ID,SUBJECT,MESSAGE,CREATED_DATE from user_feedback_table");
		 						
		 			lResultSet = lPreparedStatement.executeQuery();
		 			//}
		 			 
		 			
		 			// if rs == null, then there is no ResultSet to view
		 			if (lResultSet != null) {
		 				
		 				
		 				while (lResultSet.next()) {
		 					FeedbackDTO lFeedbackDTO = new FeedbackDTO();
		 					
		 					lFeedbackDTO.setEmpName(ToolsUtil.replaceNull(lResultSet.getString("EMP_NAME")));
		 					lFeedbackDTO.setEmpId( ToolsUtil.replaceNull(lResultSet.getString("EMP_ID")));
		 					lFeedbackDTO.setSubject( ToolsUtil.replaceNull(lResultSet.getString("SUBJECT")));
		 					lFeedbackDTO.setMessage( ToolsUtil.replaceNull(lResultSet.getString("MESSAGE")));
		 					lFeedbackDTO.setDate( ToolsUtil.replaceNull(lResultSet.getString("CREATED_DATE")));
		 					
		 					feedbackList.add(lFeedbackDTO);
		 					
		 					
		 				
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
		 				 		
		 		return feedbackList;
		 	}
	 	
		 
		
		
		
		
	 
}
