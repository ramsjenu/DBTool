package com.tcs.tools.web.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.util.DBConnectionManager;

public class ProjectCreationDAO {
	 private Connection lConnection= null;
	 private PreparedStatement  lPreparedStatement = null;
	 private ResultSet lResultSet = null;
	 
	 
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
	 			
	 			System.out.println(":::::lInsertCount::::"+lInsertCount);
	 			 
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
	public int updateProjectDetails(String pProjectId,String pSourcePath,String pTargetPath ,String pSourceDbTpe,String pTargetDbType,String pCreatedBy) {
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
		 			
		 			lInsertCount = lPreparedStatement.executeUpdate();		 			
	 			}else{
	 				lPreparedStatement = lConnection.prepareStatement(WebConstant.UPDATE_FILE_PROJECT_DETAILS);
		 			
		 			lPreparedStatement.setString(1, pSourcePath);
		 			lPreparedStatement.setString(2, pTargetPath);
		 			lPreparedStatement.setString(3, pCreatedBy);
		 			lPreparedStatement.setTimestamp(4, new Timestamp(System
		 					.currentTimeMillis()));	
		 			lPreparedStatement.setString(5, pSourceDbTpe);
		 			lPreparedStatement.setString(6, pTargetDbType);
		 			lPreparedStatement.setString(7, pProjectId);
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
	 
	
	
	public int insertSPDetails(String pProjectId,String pRecType,String pFolderPath,String pCreatedBy) {
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
	 			
	 			lInsertCount = lInsertCount + getIndividualSPName(pFolderPath, pProjectId,pRecType,pCreatedBy);
	 			
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
	 
	 public int getIndividualSPName(String pFolderPath,String pProjectId,String pRecType,String pCreatedBy){
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
			 			lPreparedStatement.setString(2, file.getName());
			 			lPreparedStatement.setString(3, file.getAbsolutePath());
			 			lPreparedStatement.setString(4, pRecType);
			 			lPreparedStatement.setString(5, pCreatedBy);
			 			lPreparedStatement.setTimestamp(6, new Timestamp(System
			 					.currentTimeMillis()));
			 			lPreparedStatement.setString(7,"Y");
			 			lInsertCount = lInsertCount + lPreparedStatement.executeUpdate();
			 			
					}else{
						getIndividualSPName(file.getAbsolutePath(),pProjectId,pRecType,pCreatedBy);
					}
				} 
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return lInsertCount; 
	 }
	 
	 public static void main(String[] args){
		 System.out.println("::::::::");
		 String pFolderPath ="C:\\Documents and Settings\\divya\\Desktop\\upload\\src";
		// new ProjectCreationDAO().getIndividualSPName(pFolderPath,"");
	 }
	 
}
