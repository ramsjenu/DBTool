package com.tcs.tools.web.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;

import org.apache.log4j.Logger;

import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dto.ProjectDetailsMainDTO;
import com.tcs.tools.web.dto.StoredProceduresDetailsDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class ProjectModifyDAO {
	 private Connection lConnection= null;
	 private PreparedStatement  lPreparedStatement = null;
	 private ResultSet lResultSet = null;
		private static Logger logger = Logger.getLogger("ToolLogger");

	 
	 /**
	 	 * method to return the sequence
	 	 * 
	 	 * @return
	 	 */
	 	public String getRunSeq(String pProjectId) {
	 		String lRetSeqValue = null;
	 		try {
	 			lConnection = DBConnectionManager.getConnection();

	 			// select the data from the table

	 			String lSQL = "UPDATE TOOL_PROJECT_FOLDER_SEQUENCE  SET SEQ_NO=SEQ_NO+1 where PROJECT_ID  = ?";
	 			String lSQL1 = "SELECT SEQ_NO FROM TOOL_PROJECT_FOLDER_SEQUENCE  where PROJECT_ID  = ?";

	 			lPreparedStatement = lConnection.prepareStatement(lSQL);
	 			lPreparedStatement.setString(1, pProjectId);
	 			lPreparedStatement.executeUpdate();
	 			//lConnection.commit();

	 			lPreparedStatement = lConnection.prepareStatement(lSQL1);
	 			lPreparedStatement.setString(1, pProjectId);
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
	 			DBConnectionManager.closeConnection(lConnection);
	 		}
	 		return lRetSeqValue;

	 	}
	 	
	 	@SuppressWarnings("finally")
		public int insertRunSeq(String pProjectId) {
	 		int lRetSeqValue = 0;
	 		try {
	 			lConnection = DBConnectionManager.getConnection();

	 			// select the data from the table
	 			String lSQL1 = "SELECT count(PROJECT_ID) AS COUNT_project_id FROM TOOL_PROJECT_FOLDER_SEQUENCE  where PROJECT_ID  = ?";
	 			lPreparedStatement = lConnection.prepareStatement(lSQL1);
	 			lPreparedStatement.setString(1, pProjectId);
	 			lResultSet = lPreparedStatement.executeQuery();
	 			
	 			// if rs == null, then there is no ResultSet to view
	 			if (lResultSet != null) {
	 				while (lResultSet.next()) {
	 					lRetSeqValue = lResultSet.getInt("COUNT_project_id");
	 				}
	 			}

	 			String lSQL = "INSERT INTO TOOL_PROJECT_FOLDER_SEQUENCE(PROJECT_ID, SEQ_NO) VALUES(?,?)";
	 			
	 			if(lRetSeqValue ==0 ){
		 			lPreparedStatement = lConnection.prepareStatement(lSQL);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, "0");
		 			lRetSeqValue = lPreparedStatement.executeUpdate();
	 			}
	 			//lConnection.commit();

	 		} catch (SQLException se) {
	 			se.printStackTrace();
	 			return 0;
	 		} catch (Exception e) {
	 			e.printStackTrace();
	 			return 0;
	 		} finally {
	 			// close the connection and the result set
	 			DBConnectionManager.closeConnection(lPreparedStatement,
	 					lResultSet);
	 			DBConnectionManager.closeConnection(lConnection);
	 			return lRetSeqValue;
	 		}
	 		

	 	}
	 	
	 
	 public int insertProjectDetails(String pProjectId,String pSourceDbTpe,String pTargetDbType,String pFilePath,String pProjectState) {
		 int lInsertCount=0;
	 		try {
	 			if(pProjectState == null) pProjectState ="1";
	 			
	 			lConnection = DBConnectionManager.getConnection();
	 		//System.out.println("::::dto pat run id:::"+pPatternSummaryDTO.getRunId()+"::::i val:::::pattern id::"+pPatternSummaryDTO.getPatternId()+"::::pattern format::"+pPatternSummaryDTO.getPatternFormat());
	 			//System.out.println("dto ru id::::"+pPatternSummaryDTO.getRunId());;
	 			//System.out.println("dto key word::::"+pPatternSummaryDTO.getKeyWord());;
	 			
	 			// prepare the query
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.INSERT_PROJECT_DETAILS);
	 			lPreparedStatement.setString(1, pProjectId);
	 			//lPreparedStatement.setString(2, pProjectName);
	 			lPreparedStatement.setString(2, pSourceDbTpe);
	 			lPreparedStatement.setString(3, pTargetDbType);
	 			lPreparedStatement.setString(4, pFilePath);
	 			lPreparedStatement.setString(5, pProjectState);
	 					 			
	 			lPreparedStatement.setString(6, "TCS USER");
	 			lPreparedStatement.setTimestamp(7, new Timestamp(System
	 					.currentTimeMillis()));
	 			
	 			//System.out.println("::::getting inserted::::"+pPatternSummaryDTO.getPatternDesc()+"-"+pPatternSummaryDTO.getKeyWord());
	 			

	 			// Execute Query
	 			lInsertCount = lPreparedStatement.executeUpdate();
	 			//pPreparedStatement.addBatch(); 		
	 			
	 			
	 			 
	 			//lPreparedStatement.executeBatch();

	 		} catch (SQLException pSQLException) {
	 			pSQLException.printStackTrace();
	 		} catch (Exception pException) {
	 			pException.printStackTrace();
	 		} finally {
	 			
	 		}
	 		return lInsertCount;

	 	}
	 
	 
	 /**
	 * @param pProjectId
	 * @param pSourcePath
	 * @param pTargetPath
	 * @param pSourceDbTpe
	 * @param pTargetDbType
	 * @param pCreatedBy
	 * @return
	 */
	public int updateProjectDetails(String pProjectId,String pSourcePath,String pTargetPath ,String pSourceDbTpe,String pTargetDbType,String pCreatedBy,String pProjectName,String pCustomerName,String pApplicationName,String pSourceDbTypeVersion,String pTargetDbTypeVersion,String pDataUploadStatus,String pRunSeq     ,String pSourceDbIp,String pSourceDbPort,String pSourceDbSchemaName,String pSourceDbUserName,String pSourceDbPassword ,String pSourceUnixIp,String pSourceUnixUserName,String pSourceUnixPassword) {
		 int lInsertCount=0;
		 int lDataCount = 0;
	 		try {
	 		
	 			
	 			lConnection = DBConnectionManager.getConnection();
	 			lConnection.setAutoCommit(false);
	 			// prepare the query
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_FILE_PROJECT_DETAILS);
	 			lPreparedStatement.setString(1, pProjectId);
	 			
	 			lResultSet = lPreparedStatement.executeQuery();
	 			
	 			if(lResultSet != null){
	 				while(lResultSet.next()){
	 					lDataCount = lResultSet.getInt("COUNT");
	 				}
	 			}
	 			
	 			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
	 			if(lDataCount == 0){
	 				lPreparedStatement = lConnection.prepareStatement(WebConstant.INSERT_FILE_PROJECT_DETAILS);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, pSourcePath);
		 			lPreparedStatement.setString(3, pTargetPath);
		 			lPreparedStatement.setString(4, pCreatedBy);
		 			lPreparedStatement.setTimestamp(5, new Timestamp(System
		 					.currentTimeMillis()));
		 			lPreparedStatement.setString(6, pSourceDbTpe);
		 			lPreparedStatement.setString(7, pTargetDbType);
		 			
		 			lPreparedStatement.setString(8, pProjectName);
		 			lPreparedStatement.setString(9, pCustomerName);
		 			lPreparedStatement.setString(10, pApplicationName);
		 			lPreparedStatement.setString(11, pSourceDbTypeVersion);
		 			lPreparedStatement.setString(12, pTargetDbTypeVersion);
		 			lPreparedStatement.setString(13, pDataUploadStatus);
		 			lPreparedStatement.setString(14, pRunSeq);
		 			
		 			lPreparedStatement.setString(15, pSourceDbIp);
		 			lPreparedStatement.setString(16, pSourceDbPort);
		 			lPreparedStatement.setString(17, pSourceDbSchemaName);
		 			lPreparedStatement.setString(18, pSourceDbUserName);
		 			lPreparedStatement.setString(19, pSourceDbPassword);
		 			
		 			lPreparedStatement.setString(20, pSourceUnixIp);
		 			lPreparedStatement.setString(21, pSourceUnixUserName);
		 			lPreparedStatement.setString(22, pSourceUnixPassword);
		 			
		 			
		 			
		 			lInsertCount = lPreparedStatement.executeUpdate();		 			
	 			}else{
	 				lPreparedStatement = lConnection.prepareStatement(WebConstant.UPDATE_FILE_PROJECT_DETAILS);
		 			
		 			lPreparedStatement.setString(1, pSourcePath);
		 			lPreparedStatement.setString(2, pTargetPath);
		 			lPreparedStatement.setString(3, pCreatedBy);
		 			lPreparedStatement.setTimestamp(4, new Timestamp(System
		 					.currentTimeMillis()));	
		 			//lPreparedStatement.setString(5, pSourceDbTpe);
		 			//lPreparedStatement.setString(6, pTargetDbType);
		 			
		 			
		 			lPreparedStatement.setString(5, pCustomerName);
		 			lPreparedStatement.setString(6, pApplicationName);
		 			lPreparedStatement.setString(7, pSourceDbTypeVersion);
		 			lPreparedStatement.setString(8, pTargetDbTypeVersion);
		 			lPreparedStatement.setString(9, pDataUploadStatus);
		 			
		 			lPreparedStatement.setString(10, pRunSeq);
		 			
		 			lPreparedStatement.setString(11, pSourceDbIp);
		 			lPreparedStatement.setString(12, pSourceDbPort);
		 			lPreparedStatement.setString(13, pSourceDbSchemaName);
		 			lPreparedStatement.setString(14, pSourceDbUserName);
		 			lPreparedStatement.setString(15, pSourceDbPassword);
		 			
		 			lPreparedStatement.setString(16, pSourceUnixIp);
		 			lPreparedStatement.setString(17, pSourceUnixUserName);
		 			lPreparedStatement.setString(18, pSourceUnixPassword);
		 			
		 			lPreparedStatement.setString(19, pProjectId);
		 			
		 			lInsertCount = lPreparedStatement.executeUpdate();	
	 			}
	 			
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
	
	public int updateProjectSPUploadDetails(String pProjectId,HashMap pProjectDetailsMap) {
		 int lInsertCount=0;
		
	 		try {
	 			//String pSourcePath,String pTargetPath ,String pRunSeq 
	 			
	 			lConnection = DBConnectionManager.getConnection();
	 			lConnection.setAutoCommit(false);
	 			// prepare the query
	 			System.out.println("inside updateProjectSPUploadDetails");
	 				String lSQL ="UPDATE TOOL_PROJECT_DETAILS SET ";
	 				
	 				if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("CREATED_BY")))){
	 			    	lSQL += "  UPDATED_BY = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("CREATED_BY"))+"' ";
	 			    }
	 			    
	 			    if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("CUSTOMER_NAME")))){
	 			    	lSQL += " , CUSTOMER_NAME = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("CUSTOMER_NAME"))+"'";
	 			    }
	 				
	 			   if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("APPLICATION_NAME")))){
	 			    	lSQL += " , APPLICATION_NAME = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("APPLICATION_NAME"))+"'";
	 			    }
	 			   if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_VERSION")))){
	 			    	lSQL += " , SOURCE_DB_VERSION = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_VERSION"))+"'";
	 			    }
	 			   if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_VERSION")))){
	 			    	lSQL += " , TARGET_DB_VERSION = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_VERSION"))+"'";
	 			    }
	 			   
	 			  if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("DATA_UPLOAD_STATUS")))){
	 			    	lSQL += " , DATA_UPLOAD_STATUS = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("DATA_UPLOAD_STATUS"))+"'";
	 			    }
	 			  
	 			  
	 			  
	 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_HOST_IP")))){
	 			    	lSQL += " , SOURCE_DB_HOST_IP = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_HOST_IP"))+"'";
	 			    }
	 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_HOST_PORT")))){
 			    	lSQL += " , SOURCE_DB_HOST_PORT = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_HOST_PORT"))+"'";
 			    }
	 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_SCHEMA_NAME")))){
 			    	lSQL += " , SOURCE_DB_SCHEMA_NAME = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_SCHEMA_NAME"))+"'";
 			    }
	 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_USER_NAME")))){
 			    	lSQL += " , SOURCE_DB_USER_NAME = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_USER_NAME"))+"'";
 			    }
	 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_PASSWORD")))){
 			    	lSQL += " , SOURCE_DB_PASSWORD = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_PASSWORD"))+"'";
 			    }
	 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_UNIX_IP")))){
 			    	lSQL += " , SOURCE_UNIX_IP = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_UNIX_IP"))+"'";
 			    }
	 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_UNIX_USER_NAME")))){
 			    	lSQL += " , SOURCE_UNIX_USER_NAME = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_UNIX_USER_NAME"))+"'";
 			    }
	 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_UNIX_PASSWORD")))){
 			    	lSQL += " , SOURCE_UNIX_PASSWORD = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_UNIX_PASSWORD"))+"'";
 			    }
	 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_TYPE")))){
 			    	lSQL += " , SOURCE_DB_TYPE = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_TYPE"))+"'";
 			    }
	 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_TYPE")))){
 			    	lSQL += " , TARGET_DB_TYPE = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_TYPE"))+"'";
 			    }
	 			
	 			//for target db details
	 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_HOST_IP")))){
 			    	lSQL += " , TARGET_DB_HOST_IP = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_HOST_IP"))+"'";
 		    }
 		    if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_HOST_PORT")))){
			    	lSQL += " , TARGET_DB_HOST_PORT = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_HOST_PORT"))+"'";
			    }
 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_SCHEMA_NAME")))){
			    	lSQL += " , TARGET_DB_SCHEMA_NAME = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_SCHEMA_NAME"))+"'";
			    }
 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_USER_NAME")))){
			    	lSQL += " , TARGET_DB_USER_NAME = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_USER_NAME"))+"'";
			    }
 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_PASSWORD")))){
			    	lSQL += " , TARGET_DB_PASSWORD = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_PASSWORD"))+"'";
			    }
 		
 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_UNIX_USER_NAME")))){
			    	lSQL += " , TARGET_UNIX_USER_NAME = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_UNIX_USER_NAME"))+"'";
			    }
 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_UNIX_PASSWORD")))){
			    	lSQL += " , TARGET_UNIX_PASSWORD = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_UNIX_PASSWORD"))+"'";
			    }
 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_TYPE")))){
			    	lSQL += " , TARGET_DB_TYPE = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_TYPE"))+"'";
			    }
 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_NAME")))){
			    	lSQL += " , TARGET_DB_NAME = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_NAME"))+"'";
			    }
 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_PATH")))){
		    	lSQL += " , SOURCE_PATH = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_PATH")).replaceAll("\\\\", "\\\\\\\\")+"'";
		    }
 			if(!"".equals(ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_PATH")))){
		    	lSQL += " , TARGET_PATH = '"+ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_PATH")).replaceAll("\\\\", "\\\\\\\\")+"'";
		    }
	 			
	 			
 			
	 		
	 			
	 			   
	 				lSQL += " , UPDATED_DATE  = ? WHERE PROJECT_ID = ? "; 
	 				lPreparedStatement = lConnection.prepareStatement(lSQL);
	 				
	 				logger.info("::::::inside updateProjectSPUploadDetails:::query to be executed-pProjectId:::;"+pProjectId);
	 				logger.info("::::::inside updateProjectSPUploadDetails:::query to be executed:::;"+lSQL);
	 				//commented as chnaged these details
		 			/*lPreparedStatement.setString(1, pSourcePath);
		 			lPreparedStatement.setString(2, pTargetPath);*/
	 				//lPreparedStatement.setString(1, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("CREATED_BY")));
		 			lPreparedStatement.setTimestamp(1, new Timestamp(System
		 					.currentTimeMillis()));	
		 			lPreparedStatement.setString(2, pProjectId);
		 			
		 			lInsertCount = lPreparedStatement.executeUpdate();	
	 			
	 			
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
	
	
	public int insertProjectSPUploadDetails(String pProjectId,HashMap pProjectDetailsMap) {
		 int lInsertCount=0;
		
	 		try {
	 			//String pSourcePath,String pTargetPath ,String pRunSeq 
	 			
	 			lConnection = DBConnectionManager.getConnection();
	 			lConnection.setAutoCommit(false);
	 			// prepare the query
	 			
	 				lPreparedStatement = lConnection.prepareStatement(WebConstant.INSERT_FILE_PROJECT_DETAILS_NEW);
		 		
	 				logger.info("::::::inside updateProjectSPUploadDetails:::query to be executed:::;"+WebConstant.INSERT_FILE_PROJECT_DETAILS_NEW);
	 				
	 				//, , , ,  , ,,,,,,,, ,,,,, ,,,, , , , , ,TARGET_UNIX_USER_NAME ,TARGET_UNIX_PASSWORD,TARGET_DB_NAME
	 				
	 				lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_PATH")));
		 			lPreparedStatement.setString(3, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_PATH")));
		 			lPreparedStatement.setString(4, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("CREATED_BY")));
		 			lPreparedStatement.setTimestamp(5, new Timestamp(System
		 					.currentTimeMillis()));
		 			lPreparedStatement.setString(6, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_TYPE")));
		 			lPreparedStatement.setString(7, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_TYPE")));
		 			
		 			lPreparedStatement.setString(8, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("PROJECT_NAME")));
		 			lPreparedStatement.setString(9, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("CUSTOMER_NAME")));
		 			lPreparedStatement.setString(10, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("APPLICATION_NAME")));
		 			lPreparedStatement.setString(11, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_VERSION")));
		 			lPreparedStatement.setString(12, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_VERSION")));
		 			lPreparedStatement.setString(13, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("DATA_UPLOAD_STATUS")));
		 			lPreparedStatement.setString(14, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("UPLOAD_SEQ")));
		 			
		 			lPreparedStatement.setString(15, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_HOST_IP")));
		 			lPreparedStatement.setString(16, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_HOST_PORT")));
		 			lPreparedStatement.setString(17, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_SCHEMA_NAME")));
		 			lPreparedStatement.setString(18, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_USER_NAME")));
		 			lPreparedStatement.setString(19, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_DB_PASSWORD")));
		 			lPreparedStatement.setString(20, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_UNIX_IP")));
		 			
		 			
		 			lPreparedStatement.setString(21, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_UNIX_USER_NAME")));
		 			lPreparedStatement.setString(22, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("SOURCE_UNIX_PASSWORD")));
		 			lPreparedStatement.setString(23, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_HOST_IP")));
		 			
		 			lPreparedStatement.setString(24, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_HOST_PORT")));
		 			lPreparedStatement.setString(25, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_SCHEMA_NAME")));
		 			lPreparedStatement.setString(26, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_USER_NAME")));
		 			lPreparedStatement.setString(27, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_PASSWORD")));		 			
		 			lPreparedStatement.setString(28, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_UNIX_USER_NAME")));
		 			lPreparedStatement.setString(29, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_UNIX_PASSWORD")));
		 			lPreparedStatement.setString(30, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("TARGET_DB_NAME")));
		 			
		 			
	 				//commented as chnaged these details
		 			/*lPreparedStatement.setString(1, pSourcePath);
		 			lPreparedStatement.setString(2, pTargetPath);*/
	 				//lPreparedStatement.setString(1, ToolsUtil.replaceNull((String)pProjectDetailsMap.get("CREATED_BY")));
		 			
		 			
		 			lInsertCount = lPreparedStatement.executeUpdate();	
	 			
	 			System.out.println("::::::inside insert:::::"+lInsertCount);
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
	 
	public int updateProjectDataUploadStatus(String pProjectId,String pDataUploadStatus) {
		 int lInsertCount=0;
	 		try {
	 			lConnection = DBConnectionManager.getConnection();
	 			lConnection.setAutoCommit(false);
	 			System.out.println(":::::::inside updateProjectDataUploadStatus method:::"+pProjectId+"<--->"+pDataUploadStatus);
	 			// prepare the query
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.UPDATE_FILE_PROJECT_UPLOAD_STATUS);
	 			
	 			lPreparedStatement.setString(1, pDataUploadStatus);
	 			lPreparedStatement.setString(2, pProjectId);
	 			lInsertCount = lPreparedStatement.executeUpdate();
	 			logger.info(":::::::inside updateProjectDataUploadStatus method:::"+pProjectId+"<--->"+pDataUploadStatus);
	 			logger.info("::::inside updateProjectDataUploadStatus:::::-insert count-->"+lInsertCount);
	 			
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
	
	public int insertSPDetails(String pProjectId,String pRecType,String pFolderPath,String pCreatedBy,String pRunSeq) {
		 int lInsertCount=0;
	 		try {
	 			lConnection = DBConnectionManager.getConnection();
	 			lConnection.setAutoCommit(false);
	 			
	 			/*// prepare the query
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.DELETE_PROJECT_SP_LIST);
	 			lPreparedStatement.setString(1, pProjectId);
	 			lPreparedStatement.setString(2, pRecType);
	 			lPreparedStatement.executeUpdate();
	 			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);*/
	 			
	 			lInsertCount = lInsertCount + getIndividualSPName(pFolderPath, pProjectId,pRecType,pCreatedBy,pRunSeq);
	 			
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
	 
	 public int getIndividualSPName(String pFolderPath,String pProjectId,String pRecType,String pCreatedBy,String pRunSeq){
		 int lInsertCount = 0;
		 try{
			 File dirpath = new File(pFolderPath);
				
				File[] filesAndDirs = dirpath.listFiles();
				//System.out.println(":::::::No: if Files:::::" + filesAndDirs.length);

				for (int cnt = 0; cnt < filesAndDirs.length; cnt++) {
					File file = filesAndDirs[cnt];
					
					if ( file.isFile() ) {
						//System.out.println(":::::File name::::::->" +file.getAbsolutePath()+"->"+file.getName());
						
						lPreparedStatement = lConnection.prepareStatement(WebConstant.INSERT_PROJECT_SP_LIST);
			 			lPreparedStatement.setString(1, pProjectId);
			 			lPreparedStatement.setString(2, pRunSeq);
			 			lPreparedStatement.setString(3, file.getName());
			 			lPreparedStatement.setString(4, file.getAbsolutePath());
			 			lPreparedStatement.setString(5, pRecType);
			 			lPreparedStatement.setString(6, pCreatedBy);
			 			lPreparedStatement.setTimestamp(7, new Timestamp(System
			 					.currentTimeMillis()));
			 			lPreparedStatement.setString(8,"Y");
			 			lInsertCount = lInsertCount + lPreparedStatement.executeUpdate();
			 			
					}else{
						getIndividualSPName(file.getAbsolutePath(),pProjectId,pRecType,pCreatedBy,pRunSeq);
					}
				} 
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return lInsertCount; 
	 }
	 
	 public String validateUploadedFiles(String pSourceFilePath,String pTargetFilePath){
		 String lRetValue="";
		 String lFilesPresentInSourceButNotInTarget ="";
		 String lFilesPresentInTargetButNotInSource ="";
		 String lInvalidFileTypesInSource ="";
		 String lInvalidFileTypesInTarget ="";
		  List lSourceSPNameList = ToolsUtil.getFileNamesFromFolderUpperCaseDTO(new File(pSourceFilePath),new ArrayList());
		  List lTargetSPNameList = ToolsUtil.getFileNamesFromFolderUpperCaseDTO(new File(pTargetFilePath),new ArrayList());
		  
		 
			StoredProceduresDetailsDTO lStoredProceduresDetailsSourceDTO = new StoredProceduresDetailsDTO();
			StoredProceduresDetailsDTO lStoredProceduresDetailsTargetDTO = new StoredProceduresDetailsDTO();
			
			boolean isFilePresent = false;
			System.out.println("inside validateUploadedFiles in ProjectModifyDAO");
		  if(lSourceSPNameList != null && lSourceSPNameList.size() >0){
			  for (int i = 0; i < lSourceSPNameList.size(); i++) {
				  lStoredProceduresDetailsSourceDTO =(StoredProceduresDetailsDTO)lSourceSPNameList.get(i);
				  
				  if(ToolsUtil.validateFileType(lStoredProceduresDetailsSourceDTO.getFolderPath()+"\\"+lStoredProceduresDetailsSourceDTO.getProcName(),
						  "SQL") == false){
					  /*if(!"".equals(lInvalidFileTypesInSource)){ 
						  lInvalidFileTypesInSource += ",";
					  }*/
					  lInvalidFileTypesInSource += lStoredProceduresDetailsSourceDTO.getProcName()+" - Invalid File type<br>";
					 // continue;
				  }
				  
				  
				  if(lTargetSPNameList != null && lTargetSPNameList.size() >0){
					  for (int j = 0; j <lTargetSPNameList.size(); j++) {
						  lStoredProceduresDetailsTargetDTO =(StoredProceduresDetailsDTO)lTargetSPNameList.get(j);
						  if(lStoredProceduresDetailsSourceDTO.getProcName().equalsIgnoreCase(lStoredProceduresDetailsTargetDTO.getProcName())){
							  isFilePresent = true;
							  break;
						  }
					  }
				  }
					  if(isFilePresent == false){
						  if("".equals(lFilesPresentInSourceButNotInTarget)){
							 lFilesPresentInSourceButNotInTarget = lFilesPresentInSourceButNotInTarget +lStoredProceduresDetailsSourceDTO.getProcName();
						  }else{
							 lFilesPresentInSourceButNotInTarget = lFilesPresentInSourceButNotInTarget +","+lStoredProceduresDetailsSourceDTO.getProcName();
						  }
					  }
				  
				  
				 /*if(!lTargetSPNameList.contains(lSourceSPNameList.get(i))){
					 
					 if("".equals(lFilesPresentInSourceButNotInTarget)){
						 lFilesPresentInSourceButNotInTarget = lFilesPresentInSourceButNotInTarget +(String)lSourceSPNameList.get(i);
					 }else{
						 lFilesPresentInSourceButNotInTarget = lFilesPresentInSourceButNotInTarget +","+(String)lSourceSPNameList.get(i);
					 }
					 
				 }*/
			}
		  }
		  //validating the file types ie checking the sp files are in .sql format or not
		  
		  if(lTargetSPNameList != null && lTargetSPNameList.size() >0){
			  for (int i = 0; i < lTargetSPNameList.size(); i++) {
				  
				  isFilePresent = false;
				  lStoredProceduresDetailsTargetDTO =(StoredProceduresDetailsDTO)lTargetSPNameList.get(i);
				  
				  if(ToolsUtil.validateFileType(lStoredProceduresDetailsTargetDTO.getFolderPath()+"\\"+lStoredProceduresDetailsTargetDTO.getProcName(),
						  "SQL") == false){
					 /* if(!"".equals(lInvalidFileTypesInTarget)){ 
						  lInvalidFileTypesInTarget += ",";
					  }*/
					  lInvalidFileTypesInTarget += lStoredProceduresDetailsTargetDTO.getProcName()+" - Invalid File type<br/>";
					 // continue;
				  }
				  
				/*  JFileChooser chooser = new JFileChooser();
				  String fileTypeName = chooser.getTypeDescription(new File(lStoredProceduresDetailsTargetDTO.getFolderPath()+"\\"+lStoredProceduresDetailsTargetDTO.getProcName()));
				  logger.info("::::::target-fileTypeName::::::"+fileTypeName);*/
				  
				  if(lSourceSPNameList != null && lSourceSPNameList.size() >0){
					  for (int j = 0; j < lSourceSPNameList.size(); j++) {
						  lStoredProceduresDetailsSourceDTO =(StoredProceduresDetailsDTO)lSourceSPNameList.get(j);
						  if(lStoredProceduresDetailsSourceDTO.getProcName().equalsIgnoreCase(lStoredProceduresDetailsTargetDTO.getProcName())){
							  isFilePresent = true;
							  break;
						  }
					  }
				  }
				  
				  if(isFilePresent == false){
					  lFilesPresentInTargetButNotInSource +=lStoredProceduresDetailsTargetDTO.getProcName();
					  /*if("".equals(lFilesPresentInSourceButNotInTarget)){
						 lFilesPresentInSourceButNotInTarget = lFilesPresentInSourceButNotInTarget +lStoredProceduresDetailsTargetDTO.getProcName();
					  }else{
						 lFilesPresentInSourceButNotInTarget = lFilesPresentInSourceButNotInTarget +","+lStoredProceduresDetailsTargetDTO.getProcName();
					  }*/
				  }
				  
				 /*if(!lSourceSPNameList.contains(lTargetSPNameList.get(i))){
					 if("".equals(lFilesPresentInTargetButNotInSource)){
						 lFilesPresentInTargetButNotInSource = lFilesPresentInTargetButNotInSource + (String)lTargetSPNameList.get(i);
					 }else{
						 lFilesPresentInTargetButNotInSource = lFilesPresentInTargetButNotInSource + "," +(String)lTargetSPNameList.get(i);
					 }
				 }*/
			}
		  }
		  
		  if((! "".equals(lFilesPresentInSourceButNotInTarget)) && (! "".equals(lFilesPresentInTargetButNotInSource))){
			  lRetValue =  "Files uploaded in Source and Target mismatches<br/><br/>";
		  }
		  
		  if(! "".equals(lFilesPresentInSourceButNotInTarget)){
			  lRetValue += "Following files were present in Source but not in Target:<br/>";
			  lRetValue += lFilesPresentInSourceButNotInTarget+"<br/>";
		  }
		  
		  if(! "".equals(lFilesPresentInTargetButNotInSource)){
			  lRetValue += "Following files were present in Target but not in Source:<br/>";
			  lRetValue += lFilesPresentInTargetButNotInSource+"<br/>";
		  }
		  if(! "".equals(lInvalidFileTypesInSource)){
			  lRetValue += "<br>Invalid File Types found in uploaded Source  <br>";
			  lRetValue += lInvalidFileTypesInSource+"<br>";
		  }
		  if(! "".equals(lInvalidFileTypesInTarget)){
			  lRetValue += "Invalid File Types found in uploaded Target  <br>";
			  lRetValue += lInvalidFileTypesInTarget+"<br>";
		  }
		  
		  System.out.println("::::::lFilesPresentInSourceButNotInTarget::::"+lFilesPresentInSourceButNotInTarget);
		  System.out.println("::::::lFilesPresentInTargetButNotInSource::::"+lFilesPresentInTargetButNotInSource);
		  System.out.println("::::::lInvalidFileTypesInSource::::"+lInvalidFileTypesInSource);
		  System.out.println("::::::lInvalidFileTypesInTarget::::"+lInvalidFileTypesInTarget);
		 return lRetValue;
	 }
	 
	 
	
	 
	 public ProjectDetailsMainDTO getProjectDetails(String pProjectId){
		 ProjectDetailsMainDTO lProjectDetailsMainDTO = new ProjectDetailsMainDTO();
		 System.out.println(":::::project id::::"+pProjectId);
		 try{
			 lConnection = DBConnectionManager.getConnection();
	 			lConnection.setAutoCommit(false);
	 			// prepare the query
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_TOTAL_PROJECT_DETAILS);
	 			lPreparedStatement.setString(1, pProjectId);
	 			
	 			lResultSet = lPreparedStatement.executeQuery();
	 			
	 			if(lResultSet != null){
	 				while(lResultSet.next()){
	 					lProjectDetailsMainDTO = new ProjectDetailsMainDTO();
	 					lProjectDetailsMainDTO.setProjectId(ToolsUtil.replaceNull(lResultSet.getString("PROJECT_ID")));
	 					lProjectDetailsMainDTO.setProjectName(ToolsUtil.replaceNull(lResultSet.getString("PROJECT_NAME")));
	 					lProjectDetailsMainDTO.setSourceDBType(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_TYPE")));
	 					lProjectDetailsMainDTO.setSourceDBTypeVersion(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_VERSION")));
	 					lProjectDetailsMainDTO.setTargetDBType(ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_TYPE")));
	 					lProjectDetailsMainDTO.setTargetDBTypeVersion(ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_VERSION")));
	 					lProjectDetailsMainDTO.setApplicationName(ToolsUtil.replaceNull(lResultSet.getString("APPLICATION_NAME")));
	 					lProjectDetailsMainDTO.setCustomerName(ToolsUtil.replaceNull(lResultSet.getString("CUSTOMER_NAME")));
	 					
	 					lProjectDetailsMainDTO.setSourceDbIp(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_HOST_IP")));
	 					lProjectDetailsMainDTO.setSourceDbPort(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_HOST_PORT")));
	 					lProjectDetailsMainDTO.setSourceDbUserName(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_USER_NAME")));
	 					lProjectDetailsMainDTO.setSourceDbPassword(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_PASSWORD")));
	 					lProjectDetailsMainDTO.setSourceDbSchemaName(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_SCHEMA_NAME")));
	 					
	 					lProjectDetailsMainDTO.setSourceUnixIp(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_UNIX_IP")));
	 					lProjectDetailsMainDTO.setSourceUnixUserName(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_UNIX_USER_NAME")));
	 					lProjectDetailsMainDTO.setSourceUnixPassword(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_UNIX_PASSWORD")));
	 					
	 					
	 					lProjectDetailsMainDTO.setTargetDbIp(ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_HOST_IP")));
	 					lProjectDetailsMainDTO.setTargetDbPort(ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_HOST_PORT")));
	 					lProjectDetailsMainDTO.setTargetDbUserName(ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_USER_NAME")));
	 					lProjectDetailsMainDTO.setTargetDbPassword(ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_PASSWORD")));
	 					lProjectDetailsMainDTO.setTargetDbSchemaName(ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_SCHEMA_NAME")));
	 					

	 					lProjectDetailsMainDTO.setTargetUnixUserName(ToolsUtil.replaceNull(lResultSet.getString("TARGET_UNIX_USER_NAME")));
	 					lProjectDetailsMainDTO.setTargetUnixPassword(ToolsUtil.replaceNull(lResultSet.getString("TARGET_UNIX_PASSWORD")));
	 					lProjectDetailsMainDTO.setTargetDbName(ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_NAME")));
	 					
	 					String dbMigrationType =ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_TYPE").trim())+"_TO_"+ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_TYPE").trim());
	 					if("SYSBASE_TO_DB2".equalsIgnoreCase(dbMigrationType)){
	 						dbMigrationType = WebConstant.DB_MIGRATION_TYPE_SYBASE_TO_DB2;
	 					}
	 					else if("SYSBASE_TO_Oracle".equalsIgnoreCase(dbMigrationType)){
	 						dbMigrationType = WebConstant.DB_MIGRATION_TYPE_SYBASE_TO_Oracle;
	 					}
	 					
	 					lProjectDetailsMainDTO.setDbMigrationType(dbMigrationType);
	 					 
	 					
	 					
	 					
	 					
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
		 return lProjectDetailsMainDTO;
	 }
	 
	 
	 public HashMap getProjectDetails() {
		 HashMap lProjectNameIdMap = new HashMap();
			
		 		try {
		 			lConnection = DBConnectionManager.getConnection();
		 			// prepare the query
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_DISTINCT_PROJECT_DETAILS);
		 			lResultSet = lPreparedStatement.executeQuery();
		 			
		 			if(lResultSet != null){
		 				while(lResultSet.next()){
		 					lProjectNameIdMap.put(ToolsUtil.replaceNull(lResultSet.getString("PROJECT_ID")), ToolsUtil.replaceNull(lResultSet.getString("PROJECT_NAME")));
		 					//System.out.println(":::::"+ToolsUtil.replaceNull(lResultSet.getString("PROJECT_ID"))+"<--->"+ToolsUtil.replaceNull(lResultSet.getString("PROJECT_NAME")));
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
		 		return lProjectNameIdMap;
	 	}
	 
	
	 
	 public static void main(String[] args){
		 System.out.println("::::::::");
		 String pFolderPath ="C:\\Documents and Settings\\divya\\Desktop\\upload\\src";
		// new ProjectCreationDAO().getIndividualSPName(pFolderPath,"");
	 }
	 
}
