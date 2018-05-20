package com.tcs.tools.web.dao;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.tcs.tools.business.frontend.dto.DynamicSQLDataDTO;
import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dto.SPComplexityAnalysysProcDataDTO;
import com.tcs.tools.web.dto.SPComplexityScoreDTO;
import com.tcs.tools.web.dto.SPPatternAnalysisReportDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class FECodeMigrationReportsDAO {
	 private Connection lConnection= null;
	 private PreparedStatement  lPreparedStatement = null;
	 private ResultSet lResultSet = null;
	 
	 
	 public List getPatternAnalysisReportList(String pProjectId,String pRecType,String pAnalysisMode,String pSeqNo) {
	 		List lPatternAnalysisReportList = new ArrayList();
	 		List lLineNumsList=null;
			List lLineStartKeywordsList=null;
			DynamicSQLDataDTO lDynamicSQLDataDTO=null;
	 		try {
	 			lConnection = DBConnectionManager.getConnection();

	 			// select the data from the table
	 			if("partial".equalsIgnoreCase(pAnalysisMode)){
	 				lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_FRONT_END_DSQL_DETAILS);
		 			lPreparedStatement.setString(1, pProjectId);	 
		 			//lPreparedStatement.setString(2, pSeqNo);	 
		 			
	 			}else{
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_FRONT_END_DSQL_DETAILS);
	 			lPreparedStatement.setString(1, pProjectId);
	 			}
	 			lResultSet = lPreparedStatement.executeQuery();
	 			ObjectInputStream objectIn=null;
				byte[] buf=null;
	 			if(lResultSet!=null){
	 				while(lResultSet.next()){
	 					//SELECT  PROJECT_ID, RUN_SEQ, DSQL_QUERY, SOURCE_FILE_PATH, SOURCE_FILE_NAME, TARGET_FILE_NAME, DSQL_LINE_NUMS_LIST,
	 					//DSQL_START_KEYWORDS_LIST, CONVERTION_STATUS, DSQL_INVOKED_LINE_NUM, FRONT_END_VAR_NAME,ORIGINAL_DSQL_QUERY 					
						
						//Extracting the Linenums List -start
						buf = lResultSet.getBytes("DSQL_LINE_NUMS_LIST");
						objectIn = null;
						if (buf != null) {
							objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
						}
						lLineNumsList =(List) objectIn.readObject();					
						//System.out.println(lLineNumsList);
						//Extracting StartKwywordsList - start
						buf = lResultSet.getBytes("DSQL_START_KEYWORDS_LIST");
						objectIn = null;
						if (buf != null) {
							objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
						}
						lLineStartKeywordsList =(List) objectIn.readObject();
						//System.out.println(lLineStartKeywordsList);
						//Forming DTO
						lDynamicSQLDataDTO=new DynamicSQLDataDTO();
						lDynamicSQLDataDTO.setOrginalDSQLQuery(lResultSet.getString("ORIGINAL_DSQL_QUERY"));
						lDynamicSQLDataDTO.setSourceFileName(lResultSet.getString("SOURCE_FILE_NAME"));
						lDynamicSQLDataDTO.setSourceFilePath(lResultSet.getString("SOURCE_FILE_PATH"));
						lDynamicSQLDataDTO.setsQLLineNumsLst(lLineNumsList);
						lDynamicSQLDataDTO.setTargetFileName(lResultSet.getString("TARGET_FILE_NAME"));					
						lDynamicSQLDataDTO.setsQLLineStartKeywordsLst(lLineStartKeywordsList);				
						lDynamicSQLDataDTO.setFrontEndVarName(lResultSet.getString("FRONT_END_VAR_NAME"));					
						lDynamicSQLDataDTO.setInvokedLineNum(lResultSet.getString("DSQL_INVOKED_LINE_NUM"));
						lDynamicSQLDataDTO.setConvertionStatus(lResultSet.getString("CONVERTION_STATUS"));
						lPatternAnalysisReportList.add(lDynamicSQLDataDTO);
						//System.out.println(lResultSet.getString("SOURCE_FILE_PATH")+lResultSet.getString("TARGET_FILE_NAME"));
											
	 					
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
	 
	 public List getDSQLTargetMapData(String pProjectId,String pRecType,String pAnalysisMode,String pSeqNo) {
	 		List lPatternAnalysisReportList = new ArrayList();
	 		
			DynamicSQLDataDTO lDynamicSQLDataDTO=null;
	 		try {
	 			lConnection = DBConnectionManager.getConnection();

	 			// select the data from the table
	 			if("partial".equalsIgnoreCase(pAnalysisMode)){
	 				lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_FRONT_END_DSQL_TARGET_MAP_DETAILS);
		 			lPreparedStatement.setString(1, pProjectId);	 
		 			//lPreparedStatement.setString(2, pSeqNo);	 
		 			
	 			}else{
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_FRONT_END_DSQL_TARGET_MAP_DETAILS);
	 			lPreparedStatement.setString(1, pProjectId);
	 			}
	 			lResultSet = lPreparedStatement.executeQuery();
	 			
				
	 			if(lResultSet!=null){
	 				while(lResultSet.next()){
	 					//ORIGINAL_DSQL_QUERY, SOURCE_FILE_PATH, SOURCE_FILE_NAME, CONVERTED_DSQL_QUERY, CONVERTION_STATUS 					
						
						//Extracting the Linenums List -start
						
						//System.out.println(lLineStartKeywordsList);
						//Forming DTO
						lDynamicSQLDataDTO=new DynamicSQLDataDTO();
						lDynamicSQLDataDTO.setOrginalDSQLQuery(lResultSet.getString("ORIGINAL_DSQL_QUERY"));
						lDynamicSQLDataDTO.setSourceFileName(lResultSet.getString("SOURCE_FILE_NAME"));
						lDynamicSQLDataDTO.setSourceFilePath(lResultSet.getString("SOURCE_FILE_PATH"));
						lDynamicSQLDataDTO.setConvertedQuery(lResultSet.getString("CONVERTED_DSQL_QUERY"));
						lDynamicSQLDataDTO.setConvertionStatus(lResultSet.getString("CONVERTION_STATUS"));
						lPatternAnalysisReportList.add(lDynamicSQLDataDTO);
						
						//System.out.println(lResultSet.getString("SOURCE_FILE_PATH")+lResultSet.getString("TARGET_FILE_NAME"));
											
	 					
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
	 
	
		   	
			
		 
	 
	 	 
}
