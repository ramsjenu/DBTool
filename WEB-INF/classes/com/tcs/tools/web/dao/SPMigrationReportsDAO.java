package com.tcs.tools.web.dao;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.tcs.tools.business.compare.dto.ComparedSummaryDTO;
import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dto.SPComplexityAnalysysProcDataDTO;
import com.tcs.tools.web.dto.SPComplexityScoreDTO;
import com.tcs.tools.web.dto.SPManualModificationLogReportDTO;
import com.tcs.tools.web.dto.SPPatternAnalysisReportDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class SPMigrationReportsDAO {
	 private Connection lConnection= null;
	 private PreparedStatement  lPreparedStatement = null;
	 private ResultSet lResultSet = null;
	 
	 
	 public List getPatternAnalysisReportList(String pProjectId,String pRecType,String pAnalysisMode,String pSeqNo) {
	 		List lPatternAnalysisReportList = new ArrayList();
	 		try {
	 			lConnection = DBConnectionManager.getConnection();

	 			// select the data from the table
	 			if("partial".equalsIgnoreCase(pAnalysisMode)){
	 				lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_PATTERN_DESC_ANALYSIS_REPORT_PARTIAL);
		 			lPreparedStatement.setString(1, pProjectId+"_"+pRecType);	 
		 			lPreparedStatement.setString(2, pSeqNo);	 
		 			lResultSet = lPreparedStatement.executeQuery();
	 			}else{
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_PATTERN_DESC_ANALYSIS_REPORT);
	 			lPreparedStatement.setString(1, pProjectId+"_"+pRecType);	 			
	 			lResultSet = lPreparedStatement.executeQuery();
	 			}
	 			
	 			SPPatternAnalysisReportDTO lSPPatternAnalysisReportDTO = new SPPatternAnalysisReportDTO();
	 			// if rs == null, then there is no ResultSet to view
	 			if (lResultSet != null) {
	 				while (lResultSet.next()) {
	 					lSPPatternAnalysisReportDTO = new SPPatternAnalysisReportDTO();
	 					lSPPatternAnalysisReportDTO.setProcName(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")));
	 					lSPPatternAnalysisReportDTO.setStatementNo(ToolsUtil.replaceNull(lResultSet.getString("STATEMENT_NO")));
	 					lSPPatternAnalysisReportDTO.setStatement(ToolsUtil.replaceNull(lResultSet.getString("STATEMENT")));
	 					lSPPatternAnalysisReportDTO.setPatternId(ToolsUtil.replaceNull(lResultSet.getString("PATTERN_ID")));
	 					lSPPatternAnalysisReportDTO.setPatternDesc(ToolsUtil.replaceNull(lResultSet.getString("PATTERN_DESC")));
	 					lSPPatternAnalysisReportDTO.setQueryCount(ToolsUtil.replaceNull(lResultSet.getString("QUERY_COUNT")));
	 					lSPPatternAnalysisReportDTO.setCategory(ToolsUtil.replaceNull(lResultSet.getString("QUERY_COUNT")));
	 					//System.out.println("::::inside loop");
	 					lPatternAnalysisReportList.add(lSPPatternAnalysisReportDTO);
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
	 		return lPatternAnalysisReportList;

	 	}
	 
	 
	 
	 public HashMap getPatternAnalysisReportTopDetails(String pSeqNo) {
		 HashMap lPatternAnalysisReportTopDataMap = new HashMap();
	 		try {
	 			lConnection = DBConnectionManager.getConnection();

	 			// select the data from the table
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_PATTERN_DESC_ANALYSIS_REPORT_TOP_DATA);
	 			lPreparedStatement.setString(1,pSeqNo);	 			
	 			lResultSet = lPreparedStatement.executeQuery();
	 			
	 			SPPatternAnalysisReportDTO lSPPatternAnalysisReportDTO = new SPPatternAnalysisReportDTO();
	 			// if rs == null, then there is no ResultSet to view
	 			if (lResultSet != null) {
	 				while (lResultSet.next()) {
	 					lPatternAnalysisReportTopDataMap.put("PROJECT_NAME", ToolsUtil.replaceNull(lResultSet.getString("PROJECT_NAME")));
	 					lPatternAnalysisReportTopDataMap.put("ANALYSIS_MODE", ToolsUtil.replaceNull(lResultSet.getString("ANALYSIS_MODE")));
	 					lPatternAnalysisReportTopDataMap.put("REC_TYPE", ToolsUtil.replaceNull(lResultSet.getString("REC_TYPE")));
	 					lPatternAnalysisReportTopDataMap.put("CREATED_BY", ToolsUtil.replaceNull(lResultSet.getString("CREATED_BY")));
	 					lPatternAnalysisReportTopDataMap.put("CREATED_DATE", ToolsUtil.replaceNull(lResultSet.getString("CREATED_DATE")));
	 					lPatternAnalysisReportTopDataMap.put("SOURCE_DB_TYPE", ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_TYPE")));
	 					lPatternAnalysisReportTopDataMap.put("TARGET_DB_TYPE", ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_TYPE")));
	 					lPatternAnalysisReportTopDataMap.put("CUSTOMER_NAME", ToolsUtil.replaceNull(lResultSet.getString("CUSTOMER_NAME")));
	 					lPatternAnalysisReportTopDataMap.put("APPLICATION_NAME", ToolsUtil.replaceNull(lResultSet.getString("APPLICATION_NAME")));
	 					lPatternAnalysisReportTopDataMap.put("SOURCE_DB_VERSION", ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_VERSION")));
	 					lPatternAnalysisReportTopDataMap.put("TARGET_DB_VERSION", ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_VERSION")));
	 					
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
	 		return lPatternAnalysisReportTopDataMap;

	 	}
	 
	 //for report 2 summary report 
	 public List getSumaryBigReportList(String pProjectId,String pAnalysisMode,String pSeqNo) {
	 		List lSummaryList = new ArrayList();
	 		try {
	 			lConnection = DBConnectionManager.getConnection();
System.out.println("---------------------------------------------------------");
	 			// select the data from the table
	 			if("partial".equalsIgnoreCase(pAnalysisMode)){
	 				lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_BIG_SUMMARY_REPORT_PARTIAL);
		 			lPreparedStatement.setString(1, pProjectId);	 
		 			lPreparedStatement.setString(2, pSeqNo);	 
		 			lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
		 			lResultSet = lPreparedStatement.executeQuery();
	 			}else{
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_BIG_SUMMARY_REPORT);
	 			lPreparedStatement.setString(1, pProjectId);	
	 			lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
	 			lResultSet = lPreparedStatement.executeQuery();
	 			}
//SELECT PROCEDURE_NAME, SOURCE_STATEMENT_NO, SOURCE_MODIFIED_FORMED_STATEMENT, SOURCE_FORMED_STATEMENT, 
	 			//SOURCE_PATTERN_ID, TARGET_STATEMENT_NO, TARGET_MODIFIED_FORMED_STATEMENT, TARGET_FORMED_STATEMENT, 
	 			//TARGET_PATTERN_ID, MISMATCH_CATEGORY, MISMATCH_CATEGORY_DESC, MATCHED_YN, CREATED_BY, CREATED_DATE, 
	 			//COMPARE_ORDERNO, PERFORMANCE_IMPACT FROM compare_formed_statements_summary_table where COMPARE_SEQ= 'PRID71' ;
	 			ComparedSummaryDTO lComparedSummaryDTO = new ComparedSummaryDTO();
	 			// if rs == null, then there is no ResultSet to view
	 			if (lResultSet != null) {
	 				while (lResultSet.next()) {
	 					 lComparedSummaryDTO = new ComparedSummaryDTO();	 					
	 					 lComparedSummaryDTO.setCompareSeq(ToolsUtil.replaceNull(lResultSet.getString("COMPARE_SEQ")));						 
						 lComparedSummaryDTO.setProcedureName(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")));
						 lComparedSummaryDTO.setSourceFormedStmt(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_FORMED_STATEMENT")));
						 lComparedSummaryDTO.setSourceModifiedFormedStmt(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_MODIFIED_FORMED_STATEMENT")));
						 /*lComparedSummaryDTO.setSourcePatId(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_PATTERN_ID")));*/
						 lComparedSummaryDTO.setSourceStmtNo(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_STATEMENT_NO")));
						 lComparedSummaryDTO.setTargetFormedStmt(ToolsUtil.replaceNull(lResultSet.getString("TARGET_FORMED_STATEMENT")));
						 lComparedSummaryDTO.setTargetModifiedFormedStmt(ToolsUtil.replaceNull(lResultSet.getString("TARGET_MODIFIED_FORMED_STATEMENT")));
						 /*lComparedSummaryDTO.setTargetPatId(ToolsUtil.replaceNull(lResultSet.getString("TARGET_PATTERN_ID")));*/
						 lComparedSummaryDTO.setTargetStmtNo(ToolsUtil.replaceNull(lResultSet.getString("TARGET_STATEMENT_NO")));
						 lComparedSummaryDTO.setMisMatchCategory(ToolsUtil.replaceNull(lResultSet.getString("MISMATCH_CATEGORY")));
						 lComparedSummaryDTO.setMisMatchCategoryDesc(ToolsUtil.replaceNull(lResultSet.getString("MISMATCH_CATEGORY_DESC")));
						 lComparedSummaryDTO.setMatchedYN(ToolsUtil.replaceNull(lResultSet.getString("MATCHED_YN")));
						 /*lComparedSummaryDTO.setSourcePatDesc(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_PATTERN_DESC")));
						 lComparedSummaryDTO.setTargetPatDesc(ToolsUtil.replaceNull(lResultSet.getString("TARGET_PATTERN_DESC")));*/
						 lComparedSummaryDTO.setPerformanceImpact(ToolsUtil.replaceNull(lResultSet.getString("PERFORMANCE_IMPACT")));
						 lComparedSummaryDTO.setPerformanceImpactDesc(ToolsUtil.replaceNull(lResultSet.getString("PERFORMANCE_IMPACT_DESC")));
	 					 lSummaryList.add(lComparedSummaryDTO);
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
	 		return lSummaryList;

	 	}
	 
	//for report 2 summary report 
		 public List getSPSumaryReportList(String pProjectId,String pAnalysisMode,String pSeqNo) {
		 		List lSummaryList = new ArrayList();
		 		try {
		 			lConnection = DBConnectionManager.getConnection();

		 			// select the data from the table
		 			if("partial".equalsIgnoreCase(pAnalysisMode)){
		 				lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_WISE_SUMMARY_REPORT_PARTIAL);
			 			lPreparedStatement.setString(1, pProjectId);	 
			 			lPreparedStatement.setString(2, pSeqNo);	 
			 			lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
			 			lResultSet = lPreparedStatement.executeQuery();
		 			}else{
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_WISE_SUMMARY_REPORT);
		 			lPreparedStatement.setString(1, pProjectId);	
		 			lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
		 			lResultSet = lPreparedStatement.executeQuery();
		 			}
		 			
		 			ComparedSummaryDTO lComparedSummaryDTO = new ComparedSummaryDTO();
		 			// if rs == null, then there is no ResultSet to view
		 			if (lResultSet != null) {
		 				while (lResultSet.next()) {
		 					 
		 					 lComparedSummaryDTO = new ComparedSummaryDTO();	 					
		 					lComparedSummaryDTO.setProcedureName(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")));
		 					 lComparedSummaryDTO.setyMatchCount(ToolsUtil.replaceNull(lResultSet.getString("MATCHED_Y")));
		 					 lComparedSummaryDTO.setnMatchCount(ToolsUtil.replaceNull(lResultSet.getString("MATCHED_N")));							
		 					 lSummaryList.add(lComparedSummaryDTO);
		 					 
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
		 		return lSummaryList;

		 	}
		 
		 public void getPatternAnalysisReportCompleteListFolders(String pProjectId,String pRecType,String pAnalysisMode,String pSeqNo ,String pFileName,String pPath) {
			 PreparedStatement lPreparedStatement =  null;
			 ResultSet lResultSet = null;
			 try {
		 			lConnection = DBConnectionManager.getConnection();
		 			HSSFWorkbook hwb=new HSSFWorkbook();
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_DISTINCT_PATTERN_FOLDER_PATHS);
		 			lPreparedStatement.setString(1, pProjectId+"_"+pRecType);	
		 			lResultSet = lPreparedStatement.executeQuery();
		 			if(lResultSet != null){
		 				while(lResultSet.next()){
		 					if(!"".equals((ToolsUtil.replaceNull(lResultSet.getString("FOLDER_PATH")))) ){
		 						hwb = getPatternAnalysisReportCompleteList( pProjectId, pRecType, pAnalysisMode, pSeqNo , pFileName, pPath, ToolsUtil.replaceNull(lResultSet.getString("FOLDER_PATH")), hwb);
		 					}
		 				}
		 			}
		 			FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
		   			hwb.write(fileOut);
		   			fileOut.close();
		   			
		   		//for file download
			   		 FileUploadDownloadUtility.downloadFile(pFileName,pPath,ServletActionContext.getResponse());
			   		 //for file download
		 			
			 } catch (SQLException se) {
		 			se.printStackTrace();
		 			//return null;
		 		} catch (Exception e) {
		 			e.printStackTrace();
		 			//return null;
		 		} finally {
		 			// close the connection and the result set
		 			DBConnectionManager.closeConnection(lPreparedStatement,
		 					lResultSet);
		 			DBConnectionManager.closeConnection(lConnection);
		 		}
		 }
		 
		 public HSSFWorkbook getPatternAnalysisReportCompleteList(String pProjectId,String pRecType,String pAnalysisMode,String pSeqNo ,String pFileName,String pPath,String pInputFolderPath,HSSFWorkbook hwb) {
		 	
		 		try {
		 			//lConnection = DBConnectionManager.getConnection();

		 			// select the data from the table
		 			if("partial".equalsIgnoreCase(pAnalysisMode)){
		 				lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_PATTERN_DESC_ANALYSIS_REPORT_PARTIAL_FOLDER);
			 			lPreparedStatement.setString(1, pProjectId+"_"+pRecType);	 
			 			lPreparedStatement.setString(2, pSeqNo);	
			 			lPreparedStatement.setString(3, pInputFolderPath);
			 			lResultSet = lPreparedStatement.executeQuery();
		 			}else{
			 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_PATTERN_DESC_ANALYSIS_REPORT_FOLDER);
			 			lPreparedStatement.setString(1, pProjectId+"_"+pRecType);	
			 			lPreparedStatement.setString(2, pInputFolderPath);
			 			lResultSet = lPreparedStatement.executeQuery();
		 			}
		 			
		 			SPPatternAnalysisReportDTO lSPPatternAnalysisReportDTO = new SPPatternAnalysisReportDTO();
		 			String[] lFolderArr = pInputFolderPath.split("\\\\");
		 			String lSheetname ="";
		 			if(lFolderArr.length >= 2){
		 				lSheetname += lFolderArr[lFolderArr.length-2];
		 			}
		 			if(lFolderArr.length >= 1){
		 				lSheetname += "_"+lFolderArr[lFolderArr.length-1];
		 			}
		 			//System.out.println(":::::lSheetname:::"+lSheetname);
		 			//HSSFWorkbook hwb=new HSSFWorkbook();
              		HSSFSheet sheet = hwb.createSheet(lSheetname);
              		//String colValue="Hi";
              		//System.out.println(":::::folder path:::"+pInputFolderPath+":::::replaced::::"+pInputFolderPath.replaceAll("\\\\", "").replaceAll("C:", ""));
              		
              		
              		HSSFRow rowhead= sheet.createRow((short)0);
              		rowhead.createCell((short) 0).setCellValue("S.No");
              		rowhead.createCell((short) 1).setCellValue("Stored Procedure Name");
              		rowhead.createCell((short) 2).setCellValue("Statement");
              		rowhead.createCell((short) 3).setCellValue("Line No");
              		rowhead.createCell((short) 4).setCellValue("Score");
              		rowhead.createCell((short) 5).setCellValue("Key Word");
              		rowhead.createCell((short) 6).setCellValue("pattern Id");
              		rowhead.createCell((short) 7).setCellValue("pattern Description");
              		rowhead.createCell((short) 8).setCellValue("Sub Query Count");
              		rowhead.createCell((short) 9).setCellValue("Formed Statement");
              		
              		int i =0 ;
		 			// if rs == null, then there is no ResultSet to view
		 			if (lResultSet != null) {
		 				while (lResultSet.next()) {
		 					
		 					//System.out.println(":::::inside resultset::::");
		 					
		 					HSSFRow row= sheet.createRow((short)(i+1));
		       				row.createCell((short) 0).setCellValue((i+1)+"");
		       				row.createCell((short) 1).setCellValue(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")));
		       				row.createCell((short) 2).setCellValue(ToolsUtil.replaceNull(lResultSet.getString("STATEMENT")));
		       				row.createCell((short) 3).setCellValue(ToolsUtil.replaceNull(lResultSet.getString("STATEMENT_NO")));
		       				row.createCell((short) 4).setCellValue(ToolsUtil.replaceNull(lResultSet.getString("SCORE")));
		       				row.createCell((short) 5).setCellValue(ToolsUtil.replaceNull(lResultSet.getString("KEY_WORD")));
		       				row.createCell((short) 6).setCellValue(ToolsUtil.replaceNull(lResultSet.getString("PATTERN_ID")));
		       				row.createCell((short) 7).setCellValue(ToolsUtil.replaceNull(lResultSet.getString("PATTERN_DESC"))/*getExceptionedPatternWithId(lPatternId,lPatternDesc,lFormedStatement, lQueryCount)*/);
		       				row.createCell((short) 8).setCellValue(ToolsUtil.replaceNull(lResultSet.getString("QUERY_COUNT")));
		       				row.createCell((short) 9).setCellValue(ToolsUtil.replaceNull(lResultSet.getString("FORMED_STATEMENT")));
		       				
		       				i++;
		 				}
		 			}
		 			
		 			
		 			/*FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
		   			hwb.write(fileOut);
		   			fileOut.close();*/
		   			
		   			
		   		/*//for file download
		   		 FileUploadDownloadUtility.downloadFile(pFileName,pPath,ServletActionContext.getResponse());
		   		 //for file download
*/		   			
		 		} catch (SQLException se) {
		 			se.printStackTrace();
		 			//return null;
		 		} catch (Exception e) {
		 			e.printStackTrace();
		 			//return null;
		 		} finally {
		 			// close the connection and the result set
		 			DBConnectionManager.closeConnection(lPreparedStatement,
		 					lResultSet);
		 			//DBConnectionManager.closeConnection(lConnection);
		 		}
		 		return hwb;
		 	}
		 
		 
		    /**
		   	 *  method to get the complete list of the data
		   	 * @return
		   	 */
		   	public void getPatternResultsSummaryExcel(String pRunId,String pFileName,String pPath) {
		   		
		   		
		   		try {
		   			
		   			lConnection = DBConnectionManager.getConnection();
		          	lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_PATTERN_DATA);
		            lPreparedStatement.setString(1,pRunId);
		            //lPreparedStatement = lConnection.prepareStatement("SELECT RUN_ID,PROCEDURE_NAME,STATEMENT,STATEMENT_NO,KEY_WORD,SCORE,PATTERN_ID,PATTERN_DESC,FORMED_STATEMENT,CREATED_BY,CREATED_DATE,QUERY_COUNT FROM PATTERN_RESULTS_TABLE WHERE RUN_ID in(27,31,32,33,34,35) ORDER BY PROCEDURE_NAME,STATEMENT_NO");          	
		            //lPreparedStatement.setString(1,pRunId);
		            lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
		   			int i = 0;
		   			lResultSet = lPreparedStatement.executeQuery();
		   			// if rs == null, then there is no ResultSet to view
		   			String lStatementCont = "";
		   			String lStatementType = "";
		   			String lPatternId="";
		   			String lFormedStatement="";
		   			String lExceptionPatternDesc="";
		   			String lPatternDesc="";
		   			String lQueryCount="";
		                          StringBuffer sbGetData= new StringBuffer();
		                          String lSeperator=";";
		                          
		                          HSSFWorkbook hwb=new HSSFWorkbook();
		                  		HSSFSheet sheet = hwb.createSheet("new sheet");
		                  		String colValue="Hi";
		                  		
		                  		HSSFRow rowhead= sheet.createRow((short)0);
		                  		rowhead.createCell((short) 0).setCellValue("S.No");
		                  		rowhead.createCell((short) 1).setCellValue("Stored Procedure Name");
		                  		rowhead.createCell((short) 2).setCellValue("Statement");
		                  		rowhead.createCell((short) 3).setCellValue("Line No");
		                  		rowhead.createCell((short) 4).setCellValue("Score");
		                  		rowhead.createCell((short) 5).setCellValue("Key Word");
		                  		rowhead.createCell((short) 6).setCellValue("pattern Id");
		                  		rowhead.createCell((short) 7).setCellValue("pattern Description");
		                  		rowhead.createCell((short) 8).setCellValue("Sub Query Count");
		                  		rowhead.createCell((short) 9).setCellValue("Formed Statement");
		                  		
		                  		
		                  		
		                          
		   			if (lResultSet != null) {
		   				// this will step through our data row-by-row
		   				while (lResultSet.next()) {
		   					
		   					lPatternId=lResultSet.getString("PATTERN_ID");
		   					lFormedStatement=lResultSet.getString("FORMED_STATEMENT");
		   					lExceptionPatternDesc="Pattern With ";
		   					lPatternDesc=lResultSet.getString("PATTERN_DESC");
		   					lQueryCount=lResultSet.getString("QUERY_COUNT");
		   					
		                                      
		                      HSSFRow row= sheet.createRow((short)(i+1));
		      				row.createCell((short) 0).setCellValue((i+1)+"");
		      				row.createCell((short) 1).setCellValue(lResultSet.getString("PROCEDURE_NAME"));
		      				row.createCell((short) 2).setCellValue(lResultSet.getString("STATEMENT"));
		      				row.createCell((short) 3).setCellValue(lResultSet.getString("STATEMENT_NO"));
		      				row.createCell((short) 4).setCellValue(lResultSet.getString("SCORE"));
		      				row.createCell((short) 5).setCellValue(lResultSet.getString("KEY_WORD"));
		      				row.createCell((short) 6).setCellValue(lResultSet.getString("PATTERN_ID"));
		      				row.createCell((short) 7).setCellValue(lResultSet.getString("PATTERN_DESC") /*getExceptionedPatternWithId(lPatternId,lPatternDesc,lFormedStatement, lQueryCount)*/);
		      				row.createCell((short) 8).setCellValue(lResultSet.getString("QUERY_COUNT"));
		      				row.createCell((short) 9).setCellValue(lResultSet.getString("FORMED_STATEMENT"));
		      				
		                      i++;     
		   				}
		   			}
		   			
		   		


		   			
		  	

		   			FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
		   			hwb.write(fileOut);
		   			fileOut.close();
		   			
		   		 //for file download
		   		 FileUploadDownloadUtility.downloadFile(pFileName,pPath,ServletActionContext.getResponse());
		   		 //for file download
		   			
		                       

		   		} catch (SQLException se) {
		   			se.printStackTrace();
		   			//return null;
		   		} catch (Exception e) {
		   			e.printStackTrace();
		   			//return null;
		   		} finally {
		   			// close the connection and the result set
		   			DBConnectionManager.closeConnection(lPreparedStatement,
		   					lResultSet);
		   			DBConnectionManager.closeConnection(lConnection);
		   		}

		   		//return lhmPatternData;
		   	}
		   	
		   	/**
		   	 * method to get the complete list of the data
		   	 * @return
		   	 */
		   	public List getComplexityMatrixSpDetails(String pRunId,String pFileName,String pPath,String pAnalysysMode,String pDisplayMode) {
		   		List lComplexityMatrixSpDetailsList = new ArrayList(); 
		   		
		   		try {
		   			
		   			lConnection = DBConnectionManager.getConnection();
		   			SPComplexityAnalysysProcDataDTO lSPComplexityAnalysysProcDataDTO = new SPComplexityAnalysysProcDataDTO();
		   			if("PARTIAL".equalsIgnoreCase(pAnalysysMode)){
		   				
		   			}else{
			          	lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_COMPLEXITY_MATRIX_SP_DATA);
			            lPreparedStatement.setString(1,pRunId);
			            lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
			   			lResultSet = lPreparedStatement.executeQuery();
		   			}
		   			
		   			
		   						// if rs == null, then there is no ResultSet to view
		   		
		   						HSSFWorkbook hwb=new HSSFWorkbook();
		                  		HSSFSheet sheet = hwb.createSheet("new sheet");
		                  		
		                  		HSSFRow rowhead= sheet.createRow((short)0);
		                  		rowhead.createCell((short) 0).setCellValue("S.No");
		                  		rowhead.createCell((short) 1).setCellValue("Stored Procedure Name");
		                  		rowhead.createCell((short) 2).setCellValue("Lines Of Code");
		                  		rowhead.createCell((short) 3).setCellValue("Distinct Patterns");
		                  		rowhead.createCell((short) 4).setCellValue("Statement Patterns");
		                  		rowhead.createCell((short) 5).setCellValue("Global Variables");
		                  		rowhead.createCell((short) 6).setCellValue("Datatypes");
		                  		rowhead.createCell((short) 7).setCellValue("Functions");
		                  		rowhead.createCell((short) 8).setCellValue("PRINT Statements");
		                  		rowhead.createCell((short) 9).setCellValue("Temp Table Usage");
		                  		rowhead.createCell((short) 10).setCellValue("Nested Stored Procedures");
		                  		
		              List lParameterList = new ArrayList();    		
		              HashMap lParameterMap = new HashMap();
		                  		
		            int i=0;              
		   			if (lResultSet != null) {
		   				// this will step through our data row-by-row
		   				while (lResultSet.next()) {
		   					lSPComplexityAnalysysProcDataDTO = new SPComplexityAnalysysProcDataDTO();
		                      HSSFRow row= sheet.createRow((short)(i+1));
			      				row.createCell((short) 0).setCellValue((i+1)+"");
			      				row.createCell((short) 1).setCellValue(ToolsUtil.replaceZero(lResultSet.getString("PROCEDURE_NAME")));
			      				row.createCell((short) 2).setCellValue(ToolsUtil.replaceZero(lResultSet.getString("LINE_COUNT")));		      				
			      				row.createCell((short) 3).setCellValue(ToolsUtil.replaceZero(lResultSet.getString("DISTINCT_PATTERN_ID")));
			      				row.createCell((short) 4).setCellValue(ToolsUtil.replaceZero(lResultSet.getString("STATEMENT_PATTERNS")));
			      				row.createCell((short) 5).setCellValue(ToolsUtil.replaceZero(lResultSet.getString("GLOBAL_VARIABLE")));
			      				row.createCell((short) 6).setCellValue(ToolsUtil.replaceZero(lResultSet.getString("DATATYPE") ));
			      				row.createCell((short) 7).setCellValue(ToolsUtil.replaceZero(lResultSet.getString("FUNCTIONS")));
			      				row.createCell((short) 8).setCellValue(ToolsUtil.replaceZero(lResultSet.getString("PRINT_STATEMENT")));
			      				row.createCell((short) 9).setCellValue(ToolsUtil.replaceZero(lResultSet.getString("TEMP_TABLE")));
			      				row.createCell((short) 10).setCellValue(ToolsUtil.replaceZero(lResultSet.getString("NESTED_STORED_PROCS")));
			      				
			      				lSPComplexityAnalysysProcDataDTO.setProcName(ToolsUtil.replaceZero(lResultSet.getString("PROCEDURE_NAME")));
			      				lSPComplexityAnalysysProcDataDTO.setLinesOfCode(ToolsUtil.replaceZero(lResultSet.getString("LINE_COUNT")));
			      				lSPComplexityAnalysysProcDataDTO.setDistinctPatterns(ToolsUtil.replaceZero(lResultSet.getString("DISTINCT_PATTERN_ID")));
			      				lSPComplexityAnalysysProcDataDTO.setStatementPatterns(ToolsUtil.replaceZero(lResultSet.getString("STATEMENT_PATTERNS")));
			      				lSPComplexityAnalysysProcDataDTO.setGlobalVariable(ToolsUtil.replaceZero(lResultSet.getString("GLOBAL_VARIABLE")));
			      				lSPComplexityAnalysysProcDataDTO.setDataTypes(ToolsUtil.replaceZero(lResultSet.getString("DATATYPE")));
			      				lSPComplexityAnalysysProcDataDTO.setFunctions(ToolsUtil.replaceZero(lResultSet.getString("FUNCTIONS")));
			      				lSPComplexityAnalysysProcDataDTO.setPrintStatement(ToolsUtil.replaceZero(lResultSet.getString("PRINT_STATEMENT")));
			      				lSPComplexityAnalysysProcDataDTO.setTempTables(ToolsUtil.replaceZero(lResultSet.getString("TEMP_TABLE")));
			      				lSPComplexityAnalysysProcDataDTO.setNestedStoredProcs(ToolsUtil.replaceZero(lResultSet.getString("NESTED_STORED_PROCS")));
			      				
			      				lParameterList = new ArrayList(); 
			      				
			      				lParameterList.add(ToolsUtil.replaceNull(lResultSet.getString("LINE_COUNT")));
			      				lParameterList.add(ToolsUtil.replaceNull(lResultSet.getString("DISTINCT_PATTERN_ID")));
			      				lParameterList.add(ToolsUtil.replaceNull(lResultSet.getString("STATEMENT_PATTERNS")));
			      				lParameterList.add(ToolsUtil.replaceNull(lResultSet.getString("GLOBAL_VARIABLE")));
			      				lParameterList.add(ToolsUtil.replaceNull(lResultSet.getString("DATATYPE")));
			      				lParameterList.add(ToolsUtil.replaceNull(lResultSet.getString("FUNCTIONS")));
			      				lParameterList.add(ToolsUtil.replaceNull(lResultSet.getString("PRINT_STATEMENT")));
			      				lParameterList.add(ToolsUtil.replaceNull(lResultSet.getString("TEMP_TABLE")));
			      				lParameterList.add(ToolsUtil.replaceNull(lResultSet.getString("NESTED_STORED_PROCS")));
			      				lSPComplexityAnalysysProcDataDTO.setSpScoresList(lParameterList);
			      				
			      				
			      				lParameterMap = new HashMap();
			      				lParameterMap.put("LINE_COUNT", ToolsUtil.replaceNull(lResultSet.getString("LINE_COUNT")));
			      				lParameterMap.put("DISTINCT_PATTERN_ID", ToolsUtil.replaceNull(lResultSet.getString("DISTINCT_PATTERN_ID")));
			      				lParameterMap.put("STATEMENT_PATTERNS", ToolsUtil.replaceNull(lResultSet.getString("STATEMENT_PATTERNS")));
			      				lParameterMap.put("GLOBAL_VARIABLE", ToolsUtil.replaceNull(lResultSet.getString("GLOBAL_VARIABLE")));
			      				lParameterMap.put("DATATYPE", ToolsUtil.replaceNull(lResultSet.getString("DATATYPE")));
			      				lParameterMap.put("FUNCTIONS", ToolsUtil.replaceNull(lResultSet.getString("FUNCTIONS")));
			      				lParameterMap.put("PRINT_STATEMENT", ToolsUtil.replaceNull(lResultSet.getString("PRINT_STATEMENT")));
			      				lParameterMap.put("TEMP_TABLE", ToolsUtil.replaceNull(lResultSet.getString("TEMP_TABLE")));
			      				lParameterMap.put("NESTED_STORED_PROCS", ToolsUtil.replaceNull(lResultSet.getString("NESTED_STORED_PROCS")));
			      				lSPComplexityAnalysysProcDataDTO.setSpScoresMap(lParameterMap);
			      				
			      				lComplexityMatrixSpDetailsList.add(lSPComplexityAnalysysProcDataDTO );
		      				  
		                      i++;     
		   				}
		   			}
		   			
		   		


		   			
		  	

		   			FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
		   			hwb.write(fileOut);
		   			fileOut.close();
		   			
		   		 //for file download
		   			if("DOWNLOAD".equalsIgnoreCase(pDisplayMode)){
		   				FileUploadDownloadUtility.downloadFile(pFileName,pPath,ServletActionContext.getResponse());
		   			}
		   		 //for file download
		   			
		                       

		   		} catch (SQLException se) {
		   			se.printStackTrace();
		   			//return null;
		   		} catch (Exception e) {
		   			e.printStackTrace();
		   			//return null;
		   		} finally {
		   			// close the connection and the result set
		   			DBConnectionManager.closeConnection(lPreparedStatement,
		   					lResultSet);
		   			DBConnectionManager.closeConnection(lConnection);
		   		}

		   		return lComplexityMatrixSpDetailsList;
		   	}
		   	public List getMismatchCategorySummeryReport(String pProjectId,String pAnalysisMode,String pSeqNo,String pDbMigrationType) {
		   		List lMismatchSummeryList=null;
	 			List lMismatchSummeyRow=null;
	 			List lMismatchSummeyCatgeoryRow=null;
		 		try {
		 			System.out.println("::::::inside getMismatchCategorySummeryReport :::");
		 			lConnection = DBConnectionManager.getConnection();		 			
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_MISMATCH_CATEGORY_LIST);
		 			lPreparedStatement.setString(1,pDbMigrationType);		 				 
		 			//lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
		 			lResultSet = lPreparedStatement.executeQuery();
		 			
		 			String lSql="";
		 			int lRecordCount=0;
		 			int count=1;
		 			HashMap lMisMatchCategoryMap=new HashMap();
		 			if (lResultSet != null) {
		 				while (lResultSet.next()) {
		 					lMisMatchCategoryMap.put(lResultSet.getString("MISMATCH_CATEGORY").trim(),ToolsUtil.replaceNull(lResultSet.getString("MISMATCH_CATEGORY_DESC").trim().replaceAll("\\s+", "_")));					
		 				}
		 				
		 			}
		 			if(lMisMatchCategoryMap.size()>1){
		 				lSql=lSql+ "SELECT A.PROCEDURE_NAME, ";
		 				lRecordCount=lMisMatchCategoryMap.size();
		 				Iterator iterator=lMisMatchCategoryMap.entrySet().iterator();
			 			while(iterator.hasNext()) {
			 				Map.Entry lRowEntry=(Map.Entry)iterator.next();
			 				lSql=lSql+" SUM(CASE WHEN  MISMATCH_CATEGORY LIKE " +
		 							"'%"+(String)lRowEntry.getKey() +	"%'" +		 									
		 							" THEN 1 ELSE 0 END) "+
		 							" AS "+ (String)lRowEntry.getValue();
		 					
		 					//System.out.println(count+"::->"+lRecordCount);
		 					if(count<lRecordCount){
			 					lSql=lSql+" , ";
			 				}
		 					count++;
						}
			 			
			 			if("partial".equalsIgnoreCase(pAnalysisMode)){
			 				lSql=lSql+" FROM compare_formed_statements_summary_table A ,TOOL_IDENTIFY_PATTERN_SP_LIST F" +
			 						" WHERE A.COMPARE_SEQ=? " +
			 						" AND F.SEQ_NO= ? " +
			 						" AND A.PROCEDURE_NAME =F.PROCEDURE_NAME " +
			 						" GROUP BY A.PROCEDURE_NAME " +
			 						" ORDER BY A.PROCEDURE_NAME";
			 			}else{
			 				lSql=lSql+" FROM compare_formed_statements_summary_table A" +
		 						" WHERE A.COMPARE_SEQ=? " +
		 						" GROUP BY A.PROCEDURE_NAME " +
		 						" ORDER BY A.PROCEDURE_NAME";
			 			}
		 			}
		 			
		 			
		 			
		 			System.out.println("SQL::->"+lSql);

		 			//get the summery data from the table
		 			if("partial".equalsIgnoreCase(pAnalysisMode)){
		 				lPreparedStatement = lConnection.prepareStatement(lSql);
			 			lPreparedStatement.setString(1, pProjectId);	 
			 			lPreparedStatement.setString(2, pSeqNo);	 
			 			lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
			 			lResultSet = lPreparedStatement.executeQuery();
		 			}else{
		 			lPreparedStatement = lConnection.prepareStatement(lSql);
		 			lPreparedStatement.setString(1, pProjectId);	
		 			lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
		 			lResultSet = lPreparedStatement.executeQuery();
		 			}
		 			
		 			
		 			// if rs == null, then there is no ResultSet to view
		 			if (lResultSet != null) {
		 				lMismatchSummeryList=new ArrayList();
		 				//Header Part
		 				lMismatchSummeyRow=new ArrayList();
		 				lMismatchSummeyCatgeoryRow=new ArrayList();
		 				lMismatchSummeyRow.add("PROCEDURE_NAME");
		 				lMismatchSummeyCatgeoryRow.add("PROCEDURE_NAME");
		 				Iterator iteratorHeader=lMisMatchCategoryMap.entrySet().iterator();
			 			while(iteratorHeader.hasNext()) {
			 				Map.Entry lMapRowEntry=(Map.Entry)iteratorHeader.next();
			 				lMismatchSummeyRow.add((String)lMapRowEntry.getValue());
			 				lMismatchSummeyCatgeoryRow.add((String)lMapRowEntry.getKey());
			 			}
			 			lMismatchSummeryList.add(lMismatchSummeyRow);
			 			//Category Types
			 			lMismatchSummeryList.add(lMismatchSummeyCatgeoryRow);
			 			
		 				while (lResultSet.next()) {
		 					lMismatchSummeyRow=new ArrayList();
		 					Iterator iterator=lMisMatchCategoryMap.entrySet().iterator();
		 					lMismatchSummeyRow.add(lResultSet.getString("PROCEDURE_NAME"));
				 			while(iterator.hasNext()) {
				 				Map.Entry lMapRowEntry=(Map.Entry)iterator.next();
				 				lMismatchSummeyRow.add(lResultSet.getString((String)lMapRowEntry.getValue()));
				 			}
				 			lMismatchSummeryList.add(lMismatchSummeyRow);
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
		 		return lMismatchSummeryList;

		 	}

		   	
			public List getDetailedMismatchProcWiseReport(String pProjectId,String pAnalysisMode,String pSeqNo,String pProcName,String pMismatchCategory) {
		   		List lSummaryList=null;	 			
		 		try {
		 			lConnection = DBConnectionManager.getConnection();	
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_DETAILED_MISMATCH_PROC_WISE);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, pProcName.trim());
		 			lPreparedStatement.setString(3, "%"+pMismatchCategory.trim()+"%");
		 			lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
		 			lResultSet = lPreparedStatement.executeQuery();
		 			
		 			
		 			
		 			ComparedSummaryDTO lComparedSummaryDTO = new ComparedSummaryDTO();
		 			// if rs == null, then there is no ResultSet to view
		 			if (lResultSet != null) {
		 				lSummaryList=new ArrayList();
		 				while (lResultSet.next()) {
		 					 lComparedSummaryDTO = new ComparedSummaryDTO();	 					
		 					 lComparedSummaryDTO.setCompareSeq(ToolsUtil.replaceNull(lResultSet.getString("COMPARE_SEQ")));						 
							 lComparedSummaryDTO.setProcedureName(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")));
							 
							 lComparedSummaryDTO.setSourceModifiedFormedStmt(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_MODIFIED_FORMED_STATEMENT")));
							 lComparedSummaryDTO.setSourcePatId(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_PATTERN_ID")));
							 lComparedSummaryDTO.setSourceStmtNo(ToolsUtil.replaceNull(lResultSet.getString("SOURCE_STATEMENT_NO")));
							 
							 lComparedSummaryDTO.setTargetModifiedFormedStmt(ToolsUtil.replaceNull(lResultSet.getString("TARGET_MODIFIED_FORMED_STATEMENT")));
							 lComparedSummaryDTO.setTargetPatId(ToolsUtil.replaceNull(lResultSet.getString("TARGET_PATTERN_ID")));
							 lComparedSummaryDTO.setTargetStmtNo(ToolsUtil.replaceNull(lResultSet.getString("TARGET_STATEMENT_NO")));
							 lComparedSummaryDTO.setMisMatchCategory(ToolsUtil.replaceNull(lResultSet.getString("MISMATCH_CATEGORY")));
							 lComparedSummaryDTO.setMisMatchCategoryDesc(ToolsUtil.replaceNull(lResultSet.getString("MISMATCH_CATEGORY_DESC")));
							 lComparedSummaryDTO.setMatchedYN(ToolsUtil.replaceNull(lResultSet.getString("MATCHED_YN")));
							 
		 					 lSummaryList.add(lComparedSummaryDTO);
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
		 		return lSummaryList;

		 	}

		   	
		   	public HashMap getSPComplexityMasterData(String pDbMigrationType){
		   		
		   		HashMap lSpComplexityCompleteMasterMap = new HashMap();
		   		HashMap lSpComplexityCompleteInnerMasterMap = new HashMap();
		   		PreparedStatement lPreparedStatementInner = null;
		   		ResultSet lResultSetInner = null;
		   		
		   		try{
		   			lConnection = DBConnectionManager.getConnection();
		   			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_COMPLEXITY_MATRIX_MASTER_PARAMETRS);
		   			lPreparedStatement.setString(1, pDbMigrationType);
		   			lResultSet = lPreparedStatement.executeQuery();
		   			List lScoreList = new ArrayList();
		   			if(lResultSet != null){
		   				while(lResultSet.next()){
		   					lPreparedStatementInner = lConnection.prepareStatement(WebConstant.SELECT_COMPLEXITY_MATRIX_MASTER_DETAILS);
		   					lPreparedStatementInner.setString(1, ToolsUtil.replaceNull(lResultSet.getString("PARAMETER")));
		   					lPreparedStatementInner.setString(2, pDbMigrationType);
		   					lResultSetInner = lPreparedStatementInner.executeQuery();
		   					lScoreList = new ArrayList();
		   					if(lResultSetInner != null){
		   						while(lResultSetInner.next()){
		   							
		   							SPComplexityScoreDTO lSPComplexityScoreDTO = new SPComplexityScoreDTO();
		   							lSPComplexityScoreDTO.setOpertator1(ToolsUtil.replaceNull(lResultSetInner.getString("OPERATOR_1")));
		   							lSPComplexityScoreDTO.setOpertator2(ToolsUtil.replaceNull(lResultSetInner.getString("OPERATOR_2")));
		   							lSPComplexityScoreDTO.setScore( ToolsUtil.replaceNull(lResultSetInner.getString("SCORE")));
		   							lSPComplexityScoreDTO.setCategory( ToolsUtil.replaceNull(lResultSetInner.getString("CATEGORY")));
		   							lScoreList.add(lSPComplexityScoreDTO);
		   							
		   							/*lSpComplexityCompleteInnerMasterMap = new HashMap();
		   							lSpComplexityCompleteInnerMasterMap.put("OPERATOR_1", ToolsUtil.replaceNull(lResultSetInner.getString("OPERATOR_1")));
		   							lSpComplexityCompleteInnerMasterMap.put("OPERATOR_2", ToolsUtil.replaceNull(lResultSetInner.getString("OPERATOR_2")));
		   							lSpComplexityCompleteInnerMasterMap.put("CATEGORY", ToolsUtil.replaceNull(lResultSetInner.getString("CATEGORY")));
		   							lSpComplexityCompleteInnerMasterMap.put("SCORE", ToolsUtil.replaceNull(lResultSetInner.getString("SCORE")));
		   							*/
		   							
		   							
		   						}
		   					}
		   					lSpComplexityCompleteMasterMap.put(ToolsUtil.replaceNull(lResultSet.getString("PARAMETER")), lScoreList);
		   					DBConnectionManager.closeConnection(lPreparedStatementInner, lResultSetInner);
		   					
		   					
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
		 		return lSpComplexityCompleteMasterMap;
		   		
		   	}
		   	
	public List getSPComplexityParameters(String pDbMigrationType){		   		
		   		List lSpComplexityCompleteMasterMap = new ArrayList();		   		
		   		try{
		   			lConnection = DBConnectionManager.getConnection();
		   			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_COMPLEXITY_MATRIX_MASTER_PARAMETRS);
		   			lPreparedStatement.setString(1, pDbMigrationType);
		   			lResultSet = lPreparedStatement.executeQuery();
		   			if(lResultSet != null){
		   				while(lResultSet.next()){
		   					lSpComplexityCompleteMasterMap.add(ToolsUtil.replaceNull(lResultSet.getString("PARAMETER")));
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
		 		return lSpComplexityCompleteMasterMap;
		   		
		   	}
	
	public HashMap getSPComplexityWeightage(String pDbMigrationType){		   		
		HashMap lSpComplexityCompleteMasterMap = new HashMap();		   		
   		try{
   			lConnection = DBConnectionManager.getConnection();
   			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_COMPLEXITY_MATRIX_MASTER_WEIGHTAGE);
   			lPreparedStatement.setString(1, pDbMigrationType);
   			lResultSet = lPreparedStatement.executeQuery();
   			if(lResultSet != null){
   				while(lResultSet.next()){
   					lSpComplexityCompleteMasterMap.put(ToolsUtil.replaceNull(lResultSet.getString("PARAMETER")) ,ToolsUtil.replaceZero(lResultSet.getString("WEIGHTAGE")));
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
 		return lSpComplexityCompleteMasterMap;
   		
   	}
	
	public HashMap getSPComplexityScoreCategory(String pDbMigrationType){		   		
		HashMap lSpComplexityCompleteMasterMap = new HashMap();		   		
   		try{
   			lConnection = DBConnectionManager.getConnection();
   			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_COMPLEXITY_MATRIX_MASTER_SCORE);
   			lPreparedStatement.setString(1, pDbMigrationType);
   			lResultSet = lPreparedStatement.executeQuery();
   			if(lResultSet != null){
   				while(lResultSet.next()){
   					lSpComplexityCompleteMasterMap.put(ToolsUtil.replaceNull(lResultSet.getString("SCORE")) ,ToolsUtil.replaceZero(lResultSet.getString("CATEGORY")));
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
 		return lSpComplexityCompleteMasterMap;
   		
   	}
		   	
		   	public List calcSpComplexityWeightageData(String pRunId,String pFileName,String pPath,String pAnalysysMode,String pDownloadMode,String pDbMigrationType){
		   		List lComplexityMatrixSpDetailsList    = getComplexityMatrixSpDetails(pRunId,pFileName,pPath,pAnalysysMode,"");
		   		System.out.println(":::stage1:::::");
		   		HashMap lSpComplexityCompleteMasterMap = getSPComplexityMasterData(pDbMigrationType);
		   		System.out.println(":::stage2:::::");
		   		HashMap lInnermap = new HashMap();
		   		
		   		SPComplexityAnalysysProcDataDTO lSPComplexityAnalysysProcDataDTO = new SPComplexityAnalysysProcDataDTO();
		   		SPComplexityAnalysysProcDataDTO lSPComplexityAnalysysProcDataDTOFinal = new SPComplexityAnalysysProcDataDTO();
		   		List lComplexityParameters = getSPComplexityParameters(pDbMigrationType);
		   		boolean firstCond= false;
					boolean secondCond = false;
		   		System.out.println(":::stage3:::::");
		   		
		   		List lReturnList = new ArrayList();
		   		HashMap lReturnMap = new HashMap();
		   		List lTempList = new ArrayList();
		   		List lParameterList = new ArrayList();
		   		SPComplexityScoreDTO lSPComplexityScoreDTO = new SPComplexityScoreDTO();
		   		String lCheckingValue= "";
		   		String lParameterType ="";
		   		HashMap lInnerMap = new HashMap();
		   		if(lComplexityParameters != null && lComplexityParameters.size() > 0){
						for (int k = 0; k < lComplexityParameters.size(); k++) {
							lParameterType = (String)lComplexityParameters.get(k);
							System.out.println(":::::lParameterType:::"+lParameterType);
						}
		   		}
		   		if(lComplexityMatrixSpDetailsList != null && lComplexityMatrixSpDetailsList.size() > 0){
		   			for (int i = 0; i < lComplexityMatrixSpDetailsList.size(); i++) {
		   				lSPComplexityAnalysysProcDataDTO = (SPComplexityAnalysysProcDataDTO)lComplexityMatrixSpDetailsList.get(i);
		   				
		   				lParameterList = new ArrayList();
		   				lInnerMap = new HashMap();
		   				lSPComplexityAnalysysProcDataDTOFinal = new SPComplexityAnalysysProcDataDTO();
		   				
		   				
		   					/*if(lComplexityParameters != null && lComplexityParameters.size() > 0){
		   						for (int k = 0; k < lComplexityParameters.size(); k++) {
		   							lParameterType = (String)lComplexityParameters.get(k);									
								
		   							
		   							
				   				//lParameterType ="LINE_COUNT";
				   				lTempList = (List)lSpComplexityCompleteMasterMap.get(lParameterType);
				   				
				   				if(lTempList != null && lTempList.size() >0){
				   					for (int j = 0; j < lTempList.size(); j++) {
										lSPComplexityScoreDTO = (SPComplexityScoreDTO)lTempList.get(j);
										
										if(checkComplexity( lSPComplexityAnalysysProcDataDTO.getLinesOfCode() ,
												(String)lSPComplexityAnalysysProcDataDTO.getSpScoresMap().get(lParameterType),
									   			lSPComplexityScoreDTO.getOpertator1(),lSPComplexityScoreDTO.getOpertator2(),lSPComplexityScoreDTO.getScore())
											){
											
											lSPComplexityAnalysysProcDataDTOFinal.setLinesOfCode(lSPComplexityScoreDTO.getScore());
											//System.out.println(":::hash map value::::"+(String)lSPComplexityAnalysysProcDataDTO.getSpScoresMap().get(lParameterType)+":::lSPComplexityScoreDTO.getScore()::"+lSPComplexityScoreDTO.getScore());	
											lParameterList.add(lSPComplexityScoreDTO.getScore());
										 }
									}//j
				   				}//j
				   				
				   				//lReturnMap.put(lParameterType, lParameterList);
		   						}//k
		   					}//k	
*/		   					
		   				//System.out.println(":::lTempList:::"+lTempList.size());
		   				
		   				if(lComplexityParameters != null && lComplexityParameters.size() > 0){
							for (int k = 0; k < lComplexityParameters.size(); k++) {
								lParameterType = (String)lComplexityParameters.get(k);
								   				
								   				//lParameterType ="LINE_COUNT";
								   				lTempList = (List)lSpComplexityCompleteMasterMap.get(lParameterType);
								   				if(lTempList != null && lTempList.size() >0){
								   					for (int j = 0; j < lTempList.size(); j++) {
														lSPComplexityScoreDTO = (SPComplexityScoreDTO)lTempList.get(j);
									   				if(checkComplexity( /*lSPComplexityAnalysysProcDataDTO.getLinesOfCode() ,		*/
									   						(String)lSPComplexityAnalysysProcDataDTO.getSpScoresMap().get(lParameterType),
												   			lSPComplexityScoreDTO.getOpertator1(),lSPComplexityScoreDTO.getOpertator2(),lSPComplexityScoreDTO.getScore())
														){
														//System.out.println("::::line no:::"+ lSPComplexityAnalysysProcDataDTO.getLinesOfCode() +"::::score::::"+lSPComplexityScoreDTO.getScore());
														/*if("LINE_COUNT".equals(lParameterType)){
															lSPComplexityAnalysysProcDataDTOFinal.setLinesOfCode(lSPComplexityScoreDTO.getScore());
														}
														if("DATATYPE".equals(lParameterType)){
															lSPComplexityAnalysysProcDataDTOFinal.setDataTypes(lSPComplexityScoreDTO.getScore());
														}
														if("DISTINCT_PATTERN_ID".equals(lParameterType)){
															lSPComplexityAnalysysProcDataDTOFinal.setDistinctPatterns(lSPComplexityScoreDTO.getScore());
														}
														if("FUNCTIONS".equals(lParameterType)){
															lSPComplexityAnalysysProcDataDTOFinal.setFunctions(lSPComplexityScoreDTO.getScore());
														}
														if("GLOBAL_VARIABLE".equals(lParameterType)){
															lSPComplexityAnalysysProcDataDTOFinal.setGlobalVariable(lSPComplexityScoreDTO.getScore());
														}
														if("NESTED_STORED_PROCS".equals(lParameterType)){
															lSPComplexityAnalysysProcDataDTOFinal.setNestedStoredProcs(lSPComplexityScoreDTO.getScore());
														}
														if("PRINT_STATEMENT".equals(lParameterType)){
															lSPComplexityAnalysysProcDataDTOFinal.setPrintStatement(lSPComplexityScoreDTO.getScore());
														}
														if("STATEMENT_PATTERNS".equals(lParameterType)){
															lSPComplexityAnalysysProcDataDTOFinal.setStatementPatterns(lSPComplexityScoreDTO.getScore());
														}
														if("TEMP_TABLE".equals(lParameterType)){
															lSPComplexityAnalysysProcDataDTOFinal.setTempTables(lSPComplexityScoreDTO.getScore());
														}*/
														
														lInnerMap.put(lParameterType, lSPComplexityScoreDTO.getScore());
														//System.out.println("::::proc name:::"+lSPComplexityAnalysysProcDataDTO.getProcName()+":::::lParameterType::::"+lParameterType+"::::value::::"+(String)lSPComplexityAnalysysProcDataDTO.getSpScoresMap().get(lParameterType)+"::::score::::"+lSPComplexityScoreDTO.getScore());
														//System.out.println(":::hash map value::::"+(String)lSPComplexityAnalysysProcDataDTO.getSpScoresMap().get(lParameterType)+":::lSPComplexityScoreDTO.getScore()::"+lSPComplexityScoreDTO.getScore());	
														//lParameterList.add(lSPComplexityScoreDTO.getScore());
													 }
								   					}
								   				}
			   				
							}}		
		   				
		   					//lSPComplexityAnalysysProcDataDTOFinal.setLinesOfCode("1111");
		   					lSPComplexityAnalysysProcDataDTOFinal.setProcName(lSPComplexityAnalysysProcDataDTO.getProcName());
			   				lSPComplexityAnalysysProcDataDTOFinal.setSpScoresMap(lInnerMap);
		   					lReturnList.add(lSPComplexityAnalysysProcDataDTOFinal);
							
					}
		   		}
		   		
		   		System.out.println(":::stage4:::::");
		   		
		   		return lReturnList;
		   	}
		   	public boolean checkComplexity(String pCheckingValue,
		   			String pOperator1,String pOperator2,String pScore){
		   		//SPComplexityAnalysysProcDataDTO lSPComplexityAnalysysProcDataDTO = new SPComplexityAnalysysProcDataDTO();
		   		String lRetValue ="";
		   	//for line count
		   		//HashMap lInnermap = (HashMap)lSpComplexityCompleteMasterMap.get(pReportType) ;
   				boolean firstCond = false;
   				boolean secondCond = false;
   					////////////////////////////////////////////////////////////////
   				//System.out.println("::::::pCheckingValue->"+pCheckingValue+"::::pOperator1->"+pOperator1+":::pOperator2:::->"+pOperator2+"::::pScore:::->"+pScore);
   				
   					//first condition
					if(( pOperator1 != null) && (!"".equals(pOperator1))){
						if( ToolsUtil.replaceZeroInt(pCheckingValue)  >  Integer.parseInt(pOperator1)  ){
							firstCond = true;
						}
					}else{
						firstCond = true;
					}
					
					//second condition
					if(( pOperator2 != null) && (!"".equals(pOperator2))){
						//System.out.println(":::pOperator2::::"+pOperator2);
						if( ToolsUtil.replaceZeroInt(pCheckingValue)  <=  Integer.parseInt(pOperator2)  ){
							secondCond = true;
						}
					}else{
						secondCond = true;
					}
					
					//validation both
					if(firstCond == true && secondCond == true){
						//lRetValue =pScore;
						//System.out.println("::::::pCheckingValue->"+pCheckingValue+"::::pOperator1->"+pOperator1+":::pOperator2:::->"+pOperator2+"::::pScore:::->"+pScore);
						return true;
					}
					
		   		
		   		return false;
		   	}
		   	
		   	public void createComplexityScoreSheet(List lComplexityWeightageDataList,String pDbMigrationType,String pFileName,String pPath){
		   		try {
					SPComplexityAnalysysProcDataDTO lSPComplexityAnalysysProcDataDTO = new SPComplexityAnalysysProcDataDTO();
					
					List lComplexityParameters = getSPComplexityParameters(pDbMigrationType);
					 HashMap lComplexityWeightageMap = getSPComplexityWeightage( pDbMigrationType);
					 
					 HashMap lComplexityScoreMap = getSPComplexityScoreCategory( pDbMigrationType);
					 
					
					HSSFWorkbook hwb=new HSSFWorkbook();
					HSSFSheet sheet = hwb.createSheet("new sheet");
					
					HSSFRow rowhead= sheet.createRow((short)0);
					rowhead.createCell((short) 0).setCellValue("S.No");
					rowhead.createCell((short) 1).setCellValue("Stored Procedure Name");
					rowhead.createCell((short) 2).setCellValue("Lines Of Code");
					rowhead.createCell((short) 3).setCellValue("Distinct Patterns");
					rowhead.createCell((short) 4).setCellValue("Statement Patterns");
					rowhead.createCell((short) 5).setCellValue("Global Variables");
					rowhead.createCell((short) 6).setCellValue("Datatypes");
					rowhead.createCell((short) 7).setCellValue("Functions");
					rowhead.createCell((short) 8).setCellValue("PRINT Statements");
					rowhead.createCell((short) 9).setCellValue("Temp Table Usage");
					rowhead.createCell((short) 10).setCellValue("Nested Stored Procedures");
					rowhead.createCell((short) 11).setCellValue("Weighted Score");
					rowhead.createCell((short) 12).setCellValue("Overall Complexity");
					

					if(lComplexityWeightageDataList != null && lComplexityWeightageDataList.size() > 0){
					for (int i = 0; i < lComplexityWeightageDataList.size(); i++) {
						lSPComplexityAnalysysProcDataDTO = (SPComplexityAnalysysProcDataDTO) lComplexityWeightageDataList.get(i);
						
					 HSSFRow row= sheet.createRow((short)(i+1));
					row.createCell((short) 0).setCellValue((i+1)+"");
					row.createCell((short) 1).setCellValue(ToolsUtil.replaceNull(lSPComplexityAnalysysProcDataDTO.getProcName()));
					//BigDecimal lWeightage = new BigDecimal("0");
					double lWeightage =0.0d;
						if(lComplexityParameters != null && lComplexityParameters.size() > 0){
							for (int j = 0; j < lComplexityParameters.size(); j++) {
								row.createCell((short) j+2).setCellValue(ToolsUtil.replaceOne((String)lSPComplexityAnalysysProcDataDTO.getSpScoresMap().get( (String)lComplexityParameters.get(j)) ));
								//weightage * the current cell value
								//lWeightage = lWeightage.add( new BigDecimal(	ToolsUtil.replaceZero((String)lComplexityWeightageMap.get( (String)lComplexityParameters.get(j))) ).multiply( new BigDecimal(ToolsUtil.replaceZero((String)lSPComplexityAnalysysProcDataDTO.getSpScoresMap().get( (String)lComplexityParameters.get(j)) ) )) );
								lWeightage +=  (Double.parseDouble(ToolsUtil.replaceOne((String)lSPComplexityAnalysysProcDataDTO.getSpScoresMap().get( (String)lComplexityParameters.get(j)) ))) * 	Double.parseDouble(ToolsUtil.replaceZero((String)lComplexityWeightageMap.get( (String)lComplexityParameters.get(j))) );
							}							
						}
						
						
						row.createCell((short) lComplexityParameters.size()+2).setCellValue(String.valueOf(new BigDecimal(lWeightage).setScale(0, RoundingMode.HALF_UP)));
						row.createCell((short) lComplexityParameters.size()+3).setCellValue((String)lComplexityScoreMap.get(String.valueOf(new BigDecimal(lWeightage).setScale(0, RoundingMode.HALF_UP))));
					}
					}
					
					
					
					FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
					hwb.write(fileOut);
					fileOut.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   	}
		   	
		 	public void createComplexitySummarySheet(List lComplexityWeightageDataList,String pDbMigrationType,String pFileName,String pPath){
		   		try {
					SPComplexityAnalysysProcDataDTO lSPComplexityAnalysysProcDataDTO = new SPComplexityAnalysysProcDataDTO();
					
					List lComplexityParameters = getSPComplexityParameters(pDbMigrationType);
					 HashMap lComplexityWeightageMap = getSPComplexityWeightage( pDbMigrationType);
					 
					 HashMap lComplexityScoreMap = getSPComplexityScoreCategory( pDbMigrationType);
					 
					
					 
					HSSFWorkbook hwb=new HSSFWorkbook();
					HSSFSheet sheet = hwb.createSheet("new sheet");
					
					HSSFRow rowhead= sheet.createRow((short)0);
					rowhead.createCell((short) 0).setCellValue("S.No");
					rowhead.createCell((short) 0).setCellValue("Category");
					rowhead.createCell((short) 1).setCellValue("Count");
					rowhead.createCell((short) 2).setCellValue("Complexity %");

					if(lComplexityWeightageDataList != null && lComplexityWeightageDataList.size() > 0){
					for (int i = 0; i < lComplexityWeightageDataList.size(); i++) {
						lSPComplexityAnalysysProcDataDTO = (SPComplexityAnalysysProcDataDTO) lComplexityWeightageDataList.get(i);
						
					/* HSSFRow row= sheet.createRow((short)(i+1));
					row.createCell((short) 0).setCellValue((i+1)+"");
					row.createCell((short) 1).setCellValue(ToolsUtil.replaceNull(lSPComplexityAnalysysProcDataDTO.getProcName()));*/
					//BigDecimal lWeightage = new BigDecimal("0");
					double lWeightage =0.0d;
						if(lComplexityParameters != null && lComplexityParameters.size() > 0){
							for (int j = 0; j < lComplexityParameters.size(); j++) {
								//row.createCell((short) j+2).setCellValue(ToolsUtil.replaceOne((String)lSPComplexityAnalysysProcDataDTO.getSpScoresMap().get( (String)lComplexityParameters.get(j)) ));
								//weightage * the current cell value
								//lWeightage = lWeightage.add( new BigDecimal(	ToolsUtil.replaceZero((String)lComplexityWeightageMap.get( (String)lComplexityParameters.get(j))) ).multiply( new BigDecimal(ToolsUtil.replaceZero((String)lSPComplexityAnalysysProcDataDTO.getSpScoresMap().get( (String)lComplexityParameters.get(j)) ) )) );
								lWeightage +=  (Double.parseDouble(ToolsUtil.replaceOne((String)lSPComplexityAnalysysProcDataDTO.getSpScoresMap().get( (String)lComplexityParameters.get(j)) ))) * 	Double.parseDouble(ToolsUtil.replaceZero((String)lComplexityWeightageMap.get( (String)lComplexityParameters.get(j))) );
							}							
						}
						
						
						//row.createCell((short) lComplexityParameters.size()+2).setCellValue(String.valueOf(new BigDecimal(lWeightage).setScale(0, RoundingMode.HALF_UP)));
						//row.createCell((short) lComplexityParameters.size()+3).setCellValue((String)lComplexityScoreMap.get(String.valueOf(new BigDecimal(lWeightage).setScale(0, RoundingMode.HALF_UP))));
					}
					}
					
					
					
					FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
					hwb.write(fileOut);
					fileOut.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   	}
		 	
		 	
		 	public List getManualModificationReportList(String pProjectId,String pRecType,String pAnalysisMode,String pSeqNo) {
		 		List lManualModificationReportList = new ArrayList();
		 		try {
		 			lConnection = DBConnectionManager.getConnection();

		 			// select the data from the table
		 			if("partial".equalsIgnoreCase(pAnalysisMode)){
		 				lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_MANUAL_MODIFICATION_LOG_REPORT_PARTIAL);
			 			lPreparedStatement.setString(1, pProjectId+"_"+pRecType);	 
			 			lPreparedStatement.setString(2, pSeqNo);	 
			 			lResultSet = lPreparedStatement.executeQuery();
		 			}else{
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_MANUAL_MODIFICATION_LOG_REPORT);
		 			lPreparedStatement.setString(1, pProjectId+"_"+pRecType);	 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			}
		 			
		 			SPManualModificationLogReportDTO lSPManualModificationLogReportDTO = new SPManualModificationLogReportDTO();
		 			// if rs == null, then there is no ResultSet to view
		 			if (lResultSet != null) {
		 				while (lResultSet.next()) {
		 					lSPManualModificationLogReportDTO = new SPManualModificationLogReportDTO();
		 					lSPManualModificationLogReportDTO.setProcName(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")));
		 					lSPManualModificationLogReportDTO.setStatementNo(ToolsUtil.replaceNull(lResultSet.getString("STATEMENT_NO")));
		 					lSPManualModificationLogReportDTO.setStatement(ToolsUtil.replaceNull(lResultSet.getString("STATEMENT")));
		 					
		 					//System.out.println("::::inside loop");
		 					lManualModificationReportList.add(lSPManualModificationLogReportDTO);
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
		 		return lManualModificationReportList;

		 	}
		 	
		 	
		 	 public HashMap getManualModificationReportTopDetails(String pSeqNo) {
				 HashMap lManualModificationReportTopDataMap = new HashMap();
			 		try {
			 			lConnection = DBConnectionManager.getConnection();

			 			// select the data from the table
			 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_PATTERN_DESC_ANALYSIS_REPORT_TOP_DATA);
			 			lPreparedStatement.setString(1,pSeqNo);	 			
			 			lResultSet = lPreparedStatement.executeQuery();
			 			
			 			SPPatternAnalysisReportDTO lSPPatternAnalysisReportDTO = new SPPatternAnalysisReportDTO();
			 			// if rs == null, then there is no ResultSet to view
			 			if (lResultSet != null) {
			 				while (lResultSet.next()) {
			 					lManualModificationReportTopDataMap.put("PROJECT_NAME", ToolsUtil.replaceNull(lResultSet.getString("PROJECT_NAME")));
			 					lManualModificationReportTopDataMap.put("ANALYSIS_MODE", ToolsUtil.replaceNull(lResultSet.getString("ANALYSIS_MODE")));
			 					lManualModificationReportTopDataMap.put("REC_TYPE", ToolsUtil.replaceNull(lResultSet.getString("REC_TYPE")));
			 					lManualModificationReportTopDataMap.put("CREATED_BY", ToolsUtil.replaceNull(lResultSet.getString("CREATED_BY")));
			 					lManualModificationReportTopDataMap.put("CREATED_DATE", ToolsUtil.replaceNull(lResultSet.getString("CREATED_DATE")));
			 					lManualModificationReportTopDataMap.put("SOURCE_DB_TYPE", ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_TYPE")));
			 					lManualModificationReportTopDataMap.put("TARGET_DB_TYPE", ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_TYPE")));
			 					lManualModificationReportTopDataMap.put("CUSTOMER_NAME", ToolsUtil.replaceNull(lResultSet.getString("CUSTOMER_NAME")));
			 					lManualModificationReportTopDataMap.put("APPLICATION_NAME", ToolsUtil.replaceNull(lResultSet.getString("APPLICATION_NAME")));
			 					lManualModificationReportTopDataMap.put("SOURCE_DB_VERSION", ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_VERSION")));
			 					lManualModificationReportTopDataMap.put("TARGET_DB_VERSION", ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_VERSION")));
			 					
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
			 		return lManualModificationReportTopDataMap;

			 	}
		 	 
		 	 
		 	 
		 	 
		 	 public HashMap getSPCallTreeFirstLevelReportTopDetails(String pSeqNo) {
				 HashMap lSpCallTreeFirstLevelReportTopDataMap = new HashMap();
			 		try {
			 			lConnection = DBConnectionManager.getConnection();

			 			// select the data from the table
			 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_PATTERN_DESC_ANALYSIS_REPORT_TOP_DATA);
			 			lPreparedStatement.setString(1,pSeqNo);	 			
			 			lResultSet = lPreparedStatement.executeQuery();
			 			
			 			SPPatternAnalysisReportDTO lSPPatternAnalysisReportDTO = new SPPatternAnalysisReportDTO();
			 			// if rs == null, then there is no ResultSet to view
			 			if (lResultSet != null) {
			 				while (lResultSet.next()) {
			 					lSpCallTreeFirstLevelReportTopDataMap.put("PROJECT_NAME", ToolsUtil.replaceNull(lResultSet.getString("PROJECT_NAME")));
			 					lSpCallTreeFirstLevelReportTopDataMap.put("ANALYSIS_MODE", ToolsUtil.replaceNull(lResultSet.getString("ANALYSIS_MODE")));
			 					lSpCallTreeFirstLevelReportTopDataMap.put("REC_TYPE", ToolsUtil.replaceNull(lResultSet.getString("REC_TYPE")));
			 					lSpCallTreeFirstLevelReportTopDataMap.put("CREATED_BY", ToolsUtil.replaceNull(lResultSet.getString("CREATED_BY")));
			 					lSpCallTreeFirstLevelReportTopDataMap.put("CREATED_DATE", ToolsUtil.replaceNull(lResultSet.getString("CREATED_DATE")));
			 					lSpCallTreeFirstLevelReportTopDataMap.put("SOURCE_DB_TYPE", ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_TYPE")));
			 					lSpCallTreeFirstLevelReportTopDataMap.put("TARGET_DB_TYPE", ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_TYPE")));
			 					lSpCallTreeFirstLevelReportTopDataMap.put("CUSTOMER_NAME", ToolsUtil.replaceNull(lResultSet.getString("CUSTOMER_NAME")));
			 					lSpCallTreeFirstLevelReportTopDataMap.put("APPLICATION_NAME", ToolsUtil.replaceNull(lResultSet.getString("APPLICATION_NAME")));
			 					lSpCallTreeFirstLevelReportTopDataMap.put("SOURCE_DB_VERSION", ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_VERSION")));
			 					lSpCallTreeFirstLevelReportTopDataMap.put("TARGET_DB_VERSION", ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_VERSION")));
			 					
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
			 		return lSpCallTreeFirstLevelReportTopDataMap;

			 	}
		 	
		 	 
		 	 
		 	
		 	
		 	 
		 	 public List getManualModLogReportList(String pProjectId,String pAnalysisMode,String pSeqNo) {
			 		List lSummaryList = new ArrayList();
			 		try {
			 			lConnection = DBConnectionManager.getConnection();
		System.out.println("---------------------------------------------------------");
			 			// select the data from the table
			 			if("partial".equalsIgnoreCase(pAnalysisMode)){
			 				lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_MANUAL_MODLOG_REPORT_PARTIAL);
				 			lPreparedStatement.setString(1, pProjectId);	 
				 			lPreparedStatement.setString(2, pSeqNo);	 
				 			lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
				 			lResultSet = lPreparedStatement.executeQuery();
			 			}else{
			 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_MANUAL_MODLOG_SUMMARY_REPORT);
			 			lPreparedStatement.setString(1, pProjectId);	
			 			lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
			 			lResultSet = lPreparedStatement.executeQuery();
			 			}
			 			//SELECT PROCEDURE_NAME, SOURCE_STATEMENT_NO, SOURCE_MODIFIED_FORMED_STATEMENT, SOURCE_FORMED_STATEMENT, 
			 			//SOURCE_PATTERN_ID, TARGET_STATEMENT_NO, TARGET_MODIFIED_FORMED_STATEMENT, TARGET_FORMED_STATEMENT, 
			 			//TARGET_PATTERN_ID, MISMATCH_CATEGORY, MISMATCH_CATEGORY_DESC, MATCHED_YN, CREATED_BY, CREATED_DATE, 
			 			//COMPARE_ORDERNO, PERFORMANCE_IMPACT FROM compare_formed_statements_summary_table where COMPARE_SEQ= 'PRID71' ;
			 			ComparedSummaryDTO lComparedSummaryDTO = new ComparedSummaryDTO();
			 			// if rs == null, then there is no ResultSet to view
			 			if (lResultSet != null) {
			 				while (lResultSet.next()) {
			 					 lComparedSummaryDTO = new ComparedSummaryDTO();	 					
			 					 lComparedSummaryDTO.setCompareSeq(ToolsUtil.replaceNull(lResultSet.getString("PROJECT_ID")));						 
								 lComparedSummaryDTO.setProcedureName(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")));
								 lComparedSummaryDTO.setStmtno(ToolsUtil.replaceNull(lResultSet.getString("STATEMENT_NO")));
								 lComparedSummaryDTO.setStmt(ToolsUtil.replaceNull(lResultSet.getString("STATEMENT")));
								 
			 					 lSummaryList.add(lComparedSummaryDTO);
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
			 		return lSummaryList;

			 	}
		 	 
		 	 
		 	 
		 	
		 	 
		 	 public List getSPCallTreeFirstLevelReportList(String pProjectId,String pAnalysisMode,String pSeqNo) {
			 		List lSummaryList = new ArrayList();
			 		//CallableStatement cs=null;
			 		
			 		ResultSet rs=null;
			 		try {
			 			lConnection = DBConnectionManager.getConnection();
			 			System.out.println("---------------------------------------------------------");
			 			// select the data from the table
			 			if("partial".equalsIgnoreCase(pAnalysisMode)){
			 				lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_CALL_TREE_FIRST_LEVEL_REPORT_PARTIAL);
				 			lPreparedStatement.setString(1, pProjectId);
				 			lPreparedStatement.setString(2, pSeqNo);	 
				 			lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
				 			lResultSet = lPreparedStatement.executeQuery();
			 				
			 				/*System.out.println( "project "+ pProjectId);
				 			System.out.println( "seq " +pSeqNo);
				 			String query = "{CALL ListReached1(?)}";
				 			cs = lConnection.prepareCall(query,
				 			    ResultSet.TYPE_SCROLL_INSENSITIVE,
				 			    ResultSet.CONCUR_READ_ONLY);
				 			cs.setString(1, pProjectId);
				 		//	cs.setInt(2, Integer.parseInt(pSeqNo));
				 			rs = cs.executeQuery();*/
				 			
			 			}
			 			else if("bulk".equalsIgnoreCase(pAnalysisMode)){
			 				
			 				lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_CALL_TREE_FIRST_LEVEL_SUMMARY_REPORT);
				 			lPreparedStatement.setString(1, pProjectId);
				 			//lPreparedStatement.setString(2, pSeqNo);	 
				 			lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
				 			lResultSet = lPreparedStatement.executeQuery();
			 			
			 			}
			 			
			 			ComparedSummaryDTO lComparedSummaryDTO = new ComparedSummaryDTO();
			 			// if rs == null, then there is no ResultSet to view
			 			//if("bulk".equalsIgnoreCase(pAnalysisMode)){
			 		/*	if (rs != null) {
			 				while (rs.next()) {
			 					 lComparedSummaryDTO = new ComparedSummaryDTO();	 					
			 					
			 					lComparedSummaryDTO.setProcedureName(ToolsUtil.replaceNull(rs.getString("nodeId")));
								lComparedSummaryDTO.setFirstLevel(ToolsUtil.replaceNull(rs.getString("status")));
								System.out.println("procedure name "+lComparedSummaryDTO.getProcedureName());
			 					 lSummaryList.add(lComparedSummaryDTO);
			 				}
			 			}*/
			 		//	}
			 			//else if("partial".equalsIgnoreCase(pAnalysisMode)){
			 				if (lResultSet != null) {
				 				while (lResultSet.next()) {
				 					 lComparedSummaryDTO = new ComparedSummaryDTO();	 					
				 					
				 					
				 					lComparedSummaryDTO.setLevel0(ToolsUtil.replaceNull(lResultSet.getString("l0")));
				 					lComparedSummaryDTO.setLevel1(ToolsUtil.replaceNull(lResultSet.getString("l1")));
				 					lComparedSummaryDTO.setLevel2(ToolsUtil.replaceNull(lResultSet.getString("l2")));
				 					lComparedSummaryDTO.setLevel3(ToolsUtil.replaceNull(lResultSet.getString("l3")));
				 					lComparedSummaryDTO.setLevel4(ToolsUtil.replaceNull(lResultSet.getString("l4")));
				 					lComparedSummaryDTO.setLevel5(ToolsUtil.replaceNull(lResultSet.getString("l5")));
				 					lComparedSummaryDTO.setLevel6(ToolsUtil.replaceNull(lResultSet.getString("l6")));
				 					lComparedSummaryDTO.setLevel7(ToolsUtil.replaceNull(lResultSet.getString("l7")));
				 					lComparedSummaryDTO.setLevel8(ToolsUtil.replaceNull(lResultSet.getString("l8")));
				 					lComparedSummaryDTO.setLevel9(ToolsUtil.replaceNull(lResultSet.getString("l9")));
				 					lComparedSummaryDTO.setLevel10(ToolsUtil.replaceNull(lResultSet.getString("l10")));
				 					lComparedSummaryDTO.setLevel11(ToolsUtil.replaceNull(lResultSet.getString("l11")));
				 					lComparedSummaryDTO.setLevel12(ToolsUtil.replaceNull(lResultSet.getString("l12")));
				 					lComparedSummaryDTO.setLevel13(ToolsUtil.replaceNull(lResultSet.getString("l13")));
				 					lComparedSummaryDTO.setLevel14(ToolsUtil.replaceNull(lResultSet.getString("l14")));
				 					lComparedSummaryDTO.setLevel15(ToolsUtil.replaceNull(lResultSet.getString("l15")));
				 					lComparedSummaryDTO.setLevel16(ToolsUtil.replaceNull(lResultSet.getString("l16")));
				 					lComparedSummaryDTO.setLevel17(ToolsUtil.replaceNull(lResultSet.getString("l17")));
				 					lComparedSummaryDTO.setLevel18(ToolsUtil.replaceNull(lResultSet.getString("l18")));
				 					lComparedSummaryDTO.setLevel19(ToolsUtil.replaceNull(lResultSet.getString("l19")));
				 					lComparedSummaryDTO.setLevel20(ToolsUtil.replaceNull(lResultSet.getString("l20")));
				 					lComparedSummaryDTO.setLevel21(ToolsUtil.replaceNull(lResultSet.getString("l21")));
								
				 					 lSummaryList.add(lComparedSummaryDTO);
				 				}
				 			}
			 			//}
			 			
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
			 		return lSummaryList;

			 	}
		 	 
		 	 public HashMap getSPSearchTopDetails(String pSeqNo) {
				 HashMap lManualModificationReportTopDataMap = new HashMap();
			 		try {
			 			lConnection = DBConnectionManager.getConnection();

			 			// select the data from the table
			 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_SP_PATTERN_DESC_ANALYSIS_REPORT_TOP_DATA);
			 			lPreparedStatement.setString(1,pSeqNo);	 			
			 			lResultSet = lPreparedStatement.executeQuery();
			 			
			 			SPPatternAnalysisReportDTO lSPPatternAnalysisReportDTO = new SPPatternAnalysisReportDTO();
			 			// if rs == null, then there is no ResultSet to view
			 			if (lResultSet != null) {
			 				while (lResultSet.next()) {
			 					lManualModificationReportTopDataMap.put("PROJECT_NAME", ToolsUtil.replaceNull(lResultSet.getString("PROJECT_NAME")));
			 					lManualModificationReportTopDataMap.put("ANALYSIS_MODE", ToolsUtil.replaceNull(lResultSet.getString("ANALYSIS_MODE")));
			 					lManualModificationReportTopDataMap.put("REC_TYPE", ToolsUtil.replaceNull(lResultSet.getString("REC_TYPE")));
			 					lManualModificationReportTopDataMap.put("CREATED_BY", ToolsUtil.replaceNull(lResultSet.getString("CREATED_BY")));
			 					lManualModificationReportTopDataMap.put("CREATED_DATE", ToolsUtil.replaceNull(lResultSet.getString("CREATED_DATE")));
			 					lManualModificationReportTopDataMap.put("SOURCE_DB_TYPE", ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_TYPE")));
			 					lManualModificationReportTopDataMap.put("TARGET_DB_TYPE", ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_TYPE")));
			 					lManualModificationReportTopDataMap.put("CUSTOMER_NAME", ToolsUtil.replaceNull(lResultSet.getString("CUSTOMER_NAME")));
			 					lManualModificationReportTopDataMap.put("APPLICATION_NAME", ToolsUtil.replaceNull(lResultSet.getString("APPLICATION_NAME")));
			 					lManualModificationReportTopDataMap.put("SOURCE_DB_VERSION", ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_VERSION")));
			 					lManualModificationReportTopDataMap.put("TARGET_DB_VERSION", ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_VERSION")));
			 					
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
			 		return lManualModificationReportTopDataMap;

			 	}
		 	 
		 	 
		 	 
		 	 
		 	
		   	public static void main(String [] args){
		   		SPMigrationReportsDAO lSPMigrationReportsDAO=new SPMigrationReportsDAO();
		   		//lSPMigrationReportsDAO.getMismatchCategorySummeryReport("PRID49", "", "", "SYSBASE_TO_DB2");
		   		//lSPMigrationReportsDAO.getComplexityMatrixSpDetails("PRID51_SOURCE","sample.xls","C:\\arun\\Tool Output\\gears\\","BULK","");
		   		List lTestList  = lSPMigrationReportsDAO.calcSpComplexityWeightageData("PRID89_SOURCE","sample.xls","C:\\arun\\Tool Output\\gears\\","BULK","","SYSBASE_TO_DB2");
		   		SPComplexityAnalysysProcDataDTO lSPComplexityAnalysysProcDataDTOFinal = new SPComplexityAnalysysProcDataDTO();
		   		System.out.println(":::list size:::::"+lTestList.size());
		   		for (int i = 0; i < lTestList.size(); i++) {
		   			lSPComplexityAnalysysProcDataDTOFinal = (SPComplexityAnalysysProcDataDTO) lTestList.get(i);
		   			System.out.println("::::::proc name:::"+lSPComplexityAnalysysProcDataDTOFinal.getProcName()+":::::lines::::"+lSPComplexityAnalysysProcDataDTOFinal.getSpScoresMap().toString());
				}
		   		
		   		lSPMigrationReportsDAO.createComplexityScoreSheet(lTestList,"SYSBASE_TO_DB2","sample_test.xls","C:\\arun\\Tool Output\\gears\\");
		   		System.out.println("::::over::::");
		   		
		   	}
		 
	 
	 	 
}
