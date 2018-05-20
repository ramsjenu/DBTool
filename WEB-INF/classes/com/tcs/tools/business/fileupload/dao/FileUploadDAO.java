/*
 * FileUploadDAO.java
 *
 * Created on September 30, 2011, 6:29 PM
 */

package com.tcs.tools.business.fileupload.dao;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;
/**
 *
 * @author  369750
 */
public class FileUploadDAO {
    
    /** Creates a new instance of FileUploadDAO */
    public FileUploadDAO() {
    }
    private Connection lConnection= null;
    private PreparedStatement  lPreparedStatement = null;
    private ResultSet lResultSetNew = null;
    

     /**
     * this method for getting the pattern value for the DB
     * @param pPramQueryString
     * @return
     */
    public String getPatternMatchData(String pPramQueryString,String pDbMigrationType){
         
        String retValue="";        
        
        try{
            lConnection = DBConnectionManager.getConnection();
            
            //replacing the special characters in the query string
            pPramQueryString = pPramQueryString.replaceAll("'","");
            
            // select the data from the table
            //if required we can add % in the like operator
            String lSQL= "select STATEMENT_TYPE from PATTERN_LOOKUP_TABLE where  PATTERN LIKE '"+pPramQueryString+"' AND VALID_YN ='Y' and DB_MIGRATION_TYPE = ?" ;
            lPreparedStatement = lConnection.prepareStatement(lSQL);
            lPreparedStatement.setString(1, pDbMigrationType);
            lResultSetNew = lPreparedStatement.executeQuery();
            // if rs == null, then there is no ResultSet to view
            if (lResultSetNew != null){
                // this will step through our data row-by-row
                while(lResultSetNew.next()){
                    //System.out.println("Data from column_name: " + lResultSet.getString("STATEMENT_TYPE"));
                   retValue = lResultSetNew.getString("STATEMENT_TYPE");
                }
            }
            
            //if there is no value in the result value matching then return a constant
            if(retValue == null || "".equals(retValue))  retValue ="Other Format";
            
        }catch(SQLException se ){
            se.printStackTrace();
            return null;
        }catch(Exception e ){
            e.printStackTrace();
            return null;
        }finally{
            //close the connection and the result set
            DBConnectionManager.closeConnection(lPreparedStatement,lResultSetNew);
            DBConnectionManager.closeConnection(lConnection);           
        }
        
        return retValue;
    }
     
     /**
 	 * method to get the complete list of the data
 	 * @return
 	 */
 	public HashMap getPatternMatchDataList(String pDbMigrationType) {
 		
 		HashMap lhmPatternData = new HashMap();
 		try {
 			lConnection = DBConnectionManager.getConnection();

 			// replacing the special characters in the query string

 			// select the data from the table
 			// if required we can add % in the like operator
 			String lSQL = "select DISTINCT  PATTERN, STATEMENT_TYPE from PATTERN_LOOKUP_TABLE where  VALID_YN ='Y' and DB_MIGRATION_TYPE = ? ";
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lPreparedStatement.setString(1, pDbMigrationType);
 			int i = 0;
 			lResultSetNew = lPreparedStatement.executeQuery();
 			// if rs == null, then there is no ResultSet to view
 			String lPattern = "";
 			String lStatementType = "";
 			if (lResultSetNew != null) {
 				// this will step through our data row-by-row
 				while (lResultSetNew.next()) {
 					// System.out.println("Data from column_name: " +
 					// lResultSet.getString("STATEMENT_TYPE"));
 					// retValue = lResultSetNew.getString("STATEMENT_TYPE");
 					lPattern = lResultSetNew.getString("PATTERN");
 					
 					if (lPattern != null) {
 						lPattern = lPattern.toUpperCase().trim();
 						lhmPatternData.put(lPattern, lResultSetNew
 								.getString("STATEMENT_TYPE").trim());
 					}
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
 					lResultSetNew);
 			DBConnectionManager.closeConnection(lConnection);
 		}

 		return lhmPatternData;
 	}
     
     /**
 	 * method to insert into parse result table
 	 * @param pProcName
 	 * @param pStatement
 	 * @param pStatementNo
 	 * @param pActualPattern
 	 * @param pStatementType
 	 * @return
 	 */
 	
 
 	/*public Connection 	getConnection(){
 	// get the connection
			try {
				return DBConnectionManager.getConnection();
			} catch (Exception pException) {
	 			pException.printStackTrace();
	 			return null;
	 		}	
 	}
 	public PreparedStatement PrepStmtinsertParseResult(Connection lConnection){
 		PreparedStatement lPreparedStatement = null;
 		try {
 			String lSQL = "INSERT INTO PARSE_RESULTS_TABLE(RUN_ID,PROCEDURE_NAME,STATEMENT,STATEMENT_NO,ACTUAL_PATTERN,STATEMENT_TYPE,CREATED_BY,CREATED_DATE,ORDER_NO) VALUES (?,?,?,?,?,?,?,?,?)";
 			;

 			// prepare the query
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 		
			
		} catch (Exception pException) {
 			pException.printStackTrace();
 		}
		return lPreparedStatement;
 	}
 	public PreparedStatement insertParseResultBatch(String Seq,String pProcName, String pStatement,
 			String pStatementNo, String pActualPattern, String pStatementType,int pWordOrderNo,PreparedStatement lPreparedStatement) {
 		
 		try {
 			
 			 //replacing the special characters in the query string
 			pStatement = pStatement.replaceAll("'","''");
 			pActualPattern = pActualPattern.replaceAll("'","''");
            
 			lPreparedStatement.setString(1, Seq);
 			lPreparedStatement.setString(2, pProcName);
 			lPreparedStatement.setString(3, pStatement);
 			lPreparedStatement.setString(4, pStatementNo);
 			lPreparedStatement.setString(5, pActualPattern);
 			lPreparedStatement.setString(6, pStatementType);
 			lPreparedStatement.setString(7, FileUploadConstant.CREATED_BY);
 			lPreparedStatement.setTimestamp(8, new Timestamp(System
 					.currentTimeMillis()));
 			lPreparedStatement.setInt(9, pWordOrderNo);

 			// Execute Query
 			//lPreparedStatement.executeUpdate();
 			lPreparedStatement.addBatch(); 			
 			//lPreparedStatement.executeBatch();

 		} catch (SQLException pSQLException) {
 			pSQLException.printStackTrace();
 		} catch (Exception pException) {
 			pException.printStackTrace();
 		} finally {
 			// close the PreparedStatement and ResultSet
 			//DBConnectionManager.closeConnection(lPreparedStatement, null);
 			// close the connection
 			//DBConnectionManager.closeConnection(lConnection);
 		}
 		return lPreparedStatement;

 	}*/
 	
 	public boolean insertParseResult(String Seq,String pProcName, String pStatement,
 			String pStatementNo, String pActualPattern, String pStatementType,int pWordOrderNo,Connection lConnection) {
 		//Connection lConnection = null;
 		String retValue = "";
 		PreparedStatement lPreparedStatement = null;
 		
 		try {
 			
 			 //replacing the special characters in the query string
 			//pStatement = pStatement.replaceAll("'","''");
 			//pActualPattern = pActualPattern.replaceAll("'","''");
            
 			// get the connection
 			//lConnection = DBConnectionManager.getConnection();
 			
 			String lSQL = "INSERT INTO PARSE_RESULTS_TABLE(RUN_ID,PROCEDURE_NAME,STATEMENT,STATEMENT_NO,ACTUAL_PATTERN,STATEMENT_TYPE,CREATED_BY,CREATED_DATE,ORDER_NO) VALUES (?,?,?,?,?,?,?,?,?)";
 			;

 			// prepare the query
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lPreparedStatement.setString(1, Seq);
 			lPreparedStatement.setString(2, pProcName);
 			lPreparedStatement.setString(3, pStatement);
 			lPreparedStatement.setString(4, pStatementNo);
 			lPreparedStatement.setString(5, pActualPattern);
 			lPreparedStatement.setString(6, pStatementType);
 			lPreparedStatement.setString(7, ToolConstant.CREATED_BY);
 			lPreparedStatement.setTimestamp(8, new Timestamp(System
 					.currentTimeMillis()));
 			lPreparedStatement.setInt(9, pWordOrderNo);

 			// Execute Query
 			lPreparedStatement.executeUpdate();
 			//lPreparedStatement.addBatch(); 			
 			//lPreparedStatement.executeBatch();
 			//lConnection.commit();
 		} catch (SQLException pSQLException) {
 			pSQLException.printStackTrace();
 		} catch (Exception pException) {
 			pException.printStackTrace();
 		} finally {
 			// close the PreparedStatement and ResultSet
 			DBConnectionManager.closeConnection(lPreparedStatement, null);
 			// close the connection
 			//DBConnectionManager.closeConnection(lConnection);
 		}
 		return false;

 	}

 
 	/**
 	 * method to return the sequence
 	 * 
 	 * @return
 	 */
 	public String getRunSeq() {
 		String lRetSeqValue = null;
 		try {
 			lConnection = DBConnectionManager.getConnection();

 			// select the data from the table

 			String lSQL = "UPDATE SEQUENCE_TABLE SET SEQ_NO=SEQ_NO+1";
 			String lSQL1 = "SELECT SEQ_NO FROM SEQUENCE_TABLE";

 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lPreparedStatement.executeUpdate();
 			//lConnection.commit();

 			lPreparedStatement = lConnection.prepareStatement(lSQL1);
 			lResultSetNew = lPreparedStatement.executeQuery();
 			// if rs == null, then there is no ResultSet to view
 			if (lResultSetNew != null) {
 				while (lResultSetNew.next()) {
 					lRetSeqValue = lResultSetNew.getString("SEQ_NO");
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
 					lResultSetNew);
 			DBConnectionManager.closeConnection(lConnection);
 		}
 		return lRetSeqValue;

 	}
        /**
 	 * method to return the sequence
 	 * 
 	 * @return
 	 */
 	public String getRunSeqSingle(Connection lConnection) {
 		String lRetSeqValue = null;
 		try {
 			//lConnection = DBConnectionManager.getConnection();

 			// select the data from the table

 			String lSQL = "UPDATE SEQUENCE_TABLE SET SEQ_NO=SEQ_NO+1";
 			String lSQL1 = "SELECT SEQ_NO FROM SEQUENCE_TABLE";

 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lPreparedStatement.executeUpdate();
 			lConnection.commit();

 			lPreparedStatement = lConnection.prepareStatement(lSQL1);
 			lResultSetNew = lPreparedStatement.executeQuery();
 			// if rs == null, then there is no ResultSet to view
 			if (lResultSetNew != null) {
 				while (lResultSetNew.next()) {
 					lRetSeqValue = lResultSetNew.getString("SEQ_NO");
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
 					lResultSetNew);
 			//DBConnectionManager.closeConnection(lConnection);
 		}
 		return lRetSeqValue;

 	}
 	
 	
 	
 	/**
 	 * method to return the sequence
 	 * 
 	 * @return
 	 */
 	public String deleteExistingProjectData(String pFolderPath,String pProjectId) {
 		String lRetSeqValue = null;
 		try {
 			lConnection = DBConnectionManager.getConnection();
 			String lProcNameConcatenated = "";
 			String lRunIdParam ="";
 			List lSPNameList = ToolsUtil.getFileNamesFromFolder(new File(pFolderPath),new ArrayList());
 	  		  if(lSPNameList != null && lSPNameList.size() >0){
 	  			  for (int i = 0; i < lSPNameList.size(); i++) {
 	  				if(i>0){
 	  					lProcNameConcatenated = lProcNameConcatenated +",";
 	  				}
 	  				lProcNameConcatenated = lProcNameConcatenated +"'"+((String)lSPNameList.get(i)).trim()+"'";
 				  }
 	  		  }
 	  		
 	  		  if("".equals(lProcNameConcatenated)){
 	  			return lRetSeqValue;
 	  		  }
 	  		  int lDeleteCount=0;
 			// select the data from the table
 			lConnection.setAutoCommit(false);
 			lRunIdParam ="'"+pProjectId.trim()+"_SOURCE','"+pProjectId.trim()+"_TARGET'";
 			String lSQL = "DELETE FROM parse_results_table where run_id in ("+lRunIdParam+") and procedure_name in( "+lProcNameConcatenated+");";
 			System.out.println("::::lSQL::::"+lSQL);
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lDeleteCount = lPreparedStatement.executeUpdate();
 			System.out.println("::::lDeleteCount1::::"+lDeleteCount);
 			
 			
 			lSQL = "DELETE FROM pattern_results_table  where run_id in ("+lRunIdParam+") and procedure_name in( "+lProcNameConcatenated+");";
 			System.out.println("::::lSQL::::"+lSQL);
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lDeleteCount = lPreparedStatement.executeUpdate();
 			System.out.println("::::lDeleteCount1::::"+lDeleteCount);
 			
 			
 			lSQL = "DELETE FROM cursor_usage_details_table  where run_id in ("+lRunIdParam+") and procedure_name in( "+lProcNameConcatenated+");";
 			System.out.println("::::lSQL::::"+lSQL);
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lDeleteCount = lPreparedStatement.executeUpdate();
 			System.out.println("::::lDeleteCount2::::"+lDeleteCount);
 			
 			
 			lSQL = "DELETE FROM compare_formed_statements_table where compare_seq='"+pProjectId+"' and procedure_name in( "+lProcNameConcatenated+");";
 			System.out.println("::::lSQL::::"+lSQL);
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lDeleteCount = lPreparedStatement.executeUpdate();
 			System.out.println("::::lDeleteCount3::::"+lDeleteCount);
 			
 			lSQL = "DELETE FROM compare_formed_statements_keywords_table where compare_seq='"+pProjectId+"' and procedure_name in( "+lProcNameConcatenated+");";
 			System.out.println("::::lSQL::::"+lSQL);
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lDeleteCount = lPreparedStatement.executeUpdate();
 			System.out.println("::::lDeleteCount4::::"+lDeleteCount);
 			
 			lSQL = "DELETE FROM compare_formed_statements_summary_table where compare_seq='"+pProjectId+"' and procedure_name in( "+lProcNameConcatenated+");";
 			System.out.println("::::lSQL::::"+lSQL);
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lDeleteCount = lPreparedStatement.executeUpdate();
 			System.out.println("::::lDeleteCount5::::"+lDeleteCount);
 			
 			
 			lSQL = "DELETE FROM variable_usage_details_table where RUN_ID='"+pProjectId+"' and procedure_name in( "+lProcNameConcatenated+");";
 			System.out.println("::::lSQL::::"+lSQL);
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lDeleteCount = lPreparedStatement.executeUpdate();
 			System.out.println("::::lDeleteCount6::::"+lDeleteCount);
 			
 			lSQL = "DELETE FROM SP_ANALYSYS_LINE_COUNT  where run_id in ("+lRunIdParam+") and procedure_name in( "+lProcNameConcatenated+");"; 			
 			System.out.println("::::lSQL::::"+lSQL);
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lDeleteCount = lPreparedStatement.executeUpdate();
 			System.out.println("::::lDeleteCount6::::"+lDeleteCount);
 			
 			lSQL = " DELETE FROM TOOL_PROJECT_SP_FILE_LOCATION_DETAILS  where run_id in ("+lRunIdParam+") and PROCEDURE_NAME in( "+lProcNameConcatenated+");"; 			
 			System.out.println("::::lSQL::::"+lSQL);
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lDeleteCount = lPreparedStatement.executeUpdate();
 			System.out.println("::::lDeleteCount7::::"+lDeleteCount);
 			
 			
 			lSQL = " DELETE FROM CURRENT_PROCESS_STATUS_TABLE WHERE RUN_ID ='"+pProjectId.trim()+"'"; 			
 			System.out.println("::::lSQL::::"+lSQL);
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lDeleteCount = lPreparedStatement.executeUpdate();
 			System.out.println("::::Status lDeleteCount8::::"+lDeleteCount);
 			
 			
 			lSQL = "DELETE FROM manually_added_code_blocks_table  where run_id in ("+lRunIdParam+") and procedure_name in( "+lProcNameConcatenated+");";
 			System.out.println("::::lSQL::::"+lSQL);
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lDeleteCount = lPreparedStatement.executeUpdate();
 			System.out.println("::::lDeleteCount9::::"+lDeleteCount);
 			
 			lSQL = "delete from CALL_TREE_FIRST_LEVEL_DATA where RUN_ID in ("+lRunIdParam+") and sp_procedure_name in( "+lProcNameConcatenated+");";
 			System.out.println("::::lSQL::::"+lSQL);
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			lDeleteCount = lPreparedStatement.executeUpdate();
 			System.out.println("::::lDeleteCount9::::"+lDeleteCount);
 			
 			
 			
 			//DBConnectionManager.closeConnection(lPreparedStatement,null);
 			
 			
 			
 			
 			lConnection.commit();
 			lConnection.setAutoCommit(true);
 			
 		} catch (SQLException se) {
 			se.printStackTrace();
 			return null;
 		} catch (Exception e) {
 			e.printStackTrace();
 			return null;
 		} finally {
 			// close the connection and the result set
 			DBConnectionManager.closeConnection(lPreparedStatement,
 					lResultSetNew);
 			DBConnectionManager.closeConnection(lConnection);
 		}
 		return lRetSeqValue;

 	}
 	
 	public int insertSPLineCountDetails(Connection lConnection,String pRunId,String pSpName,String pFilePath,String pLineCount,String pCreatedBy){
 		try{
 			
 			lPreparedStatement = lConnection.prepareStatement(ToolConstant.INSERT_SP_LINE_COUNT);
 			lPreparedStatement.setString(1, pRunId);
 			lPreparedStatement.setString(2, pSpName);
 			lPreparedStatement.setString(3, pLineCount);
 			lPreparedStatement.setString(4, pCreatedBy);
 			lPreparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
 			lPreparedStatement.setString(6, pFilePath);
 			
 			lPreparedStatement.executeUpdate();
 			
 			
 			
 		} catch (SQLException se) {
 			se.printStackTrace();
 			return 0;
 		} catch (Exception e) {
 			e.printStackTrace();
 			return 0;
 		} finally {
 			// close the connection and the result set
 			DBConnectionManager.closeConnection(lPreparedStatement,
 					null);
 			
 		}
 		
 		
 		return 0;
 	}
}
