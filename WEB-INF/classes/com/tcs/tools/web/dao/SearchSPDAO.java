package com.tcs.tools.web.dao;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dto.IdentifyPatternDTO;
import com.tcs.tools.web.dto.SearchSPDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;





public class SearchSPDAO {
	
	 private Connection lConnection= null;
	 private PreparedStatement  lPreparedStatement = null;
	 private ResultSet lResultSet = null;
	 private PreparedStatement  lPreparedStatement2 = null;
	 private ResultSet lResultSet2 = null;
	
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
	 
	 public ArrayList<SearchSPDTO> searchParseResultsTable(String search,String projectId,String seq){
	//	 System.out.println("searchPatternResultsTable");
		// System.out.println("search "+search);
		// System.out.println("projectId "+projectId);
		 String searchedWords[]=search.split(",");
		 ArrayList<SearchSPDTO> lSearchResultReport=new ArrayList<SearchSPDTO>();
		
		 
		 
		 try{ 
			 if(!search.equalsIgnoreCase(" ") && search!=null && search!="" && !search.equalsIgnoreCase("")){
			 
				 for(int i=0;i<searchedWords.length;i++){
					
			 lConnection = DBConnectionManager.getConnection();
			 
		 String lSQL = "select distinct CONCAT(CONCAT(SUBSTRING_INDEX(run_id,'_',-1),' ','SP'),'->',procedure_name) as proc,statement_no,trim(both ' ' from replace(statement,'  ',' ')) as STMT from pattern_results_table  where SUBSTRING_INDEX(run_id, '_', 1)= ? and trim(both ' ' from replace(statement,'  ',' ')) like ? or trim(both ' ' from replace(statement,'  ',' ')) like ? or trim(both ' ' from replace(statement,'  ',' ')) like ?  ";
		
		 lPreparedStatement = lConnection.prepareStatement(lSQL);
		 lPreparedStatement.setString(1,projectId);
		 lPreparedStatement.setString(2,searchedWords[i] + " %");
		 lPreparedStatement.setString(3,"% " + searchedWords[i] + " %");
		 lPreparedStatement.setString(4,"% " +searchedWords[i]);
		 lResultSet=lPreparedStatement.executeQuery();
		// System.out.println("prep "+lPreparedStatement.toString());
		 if (lResultSet != null) {
		 while(lResultSet.next()){
			 SearchSPDTO lSearchSPDTO=new SearchSPDTO();
		
			
				 lSearchSPDTO.setProcedureName(ToolsUtil.replaceWithSpace(ToolsUtil.removeToolKeywords(ToolsUtil.replaceNull(lResultSet.getString("PROC")))));
				 lSearchSPDTO.setStatementNo(ToolsUtil.replaceWithSpace(ToolsUtil.removeToolKeywords(ToolsUtil.replaceNull(lResultSet.getString("STATEMENT_NO")))));
				 lSearchSPDTO.setStatement(ToolsUtil.replaceWithSpace(ToolsUtil.removeToolKeywords(ToolsUtil.replaceNull(lResultSet.getString("STMT")) .toUpperCase().replaceAll(searchedWords[i].toUpperCase(),"<font color=\"red\"> "+searchedWords[i].toUpperCase()+" </font>"))) );
				 lSearchSPDTO.setSearchedWord(searchedWords[i]);
				 
				 lSearchResultReport.add(lSearchSPDTO);
				
				
		 }
		 }
		 
		 
		
		 }
		 
		 }
			 
		 
		 }
		 catch (SQLException pSQLException) {
	 			pSQLException.printStackTrace();
	 		} catch (Exception pException) {
	 			pException.printStackTrace();
	 			return null;
	 		} finally {
	 			DBConnectionManager.closeConnection(lPreparedStatement,
	 					lResultSet);
	 			DBConnectionManager.closeConnection(lConnection);
	 		}
		
		 return lSearchResultReport;
	 }
	 
	 public ArrayList<SearchSPDTO> searchStringParseResultsTable(String search,String projectId,String seq){
			//	 System.out.println("searchPatternResultsTable");
				// System.out.println("search "+search);
				// System.out.println("projectId "+projectId);
				
				 ArrayList<SearchSPDTO> lSearchResultReport=new ArrayList<SearchSPDTO>();
				
				 
				 
				 try{ 
					if(!search.equalsIgnoreCase(" ") && search!=null && search!="" && !search.equalsIgnoreCase("")){
					 lConnection = DBConnectionManager.getConnection();
					 
				 String lSQL = " select distinct CONCAT(CONCAT(SUBSTRING_INDEX(run_id,'_',-1),' ','SP'),'->',procedure_name) as proc,statement_no,replace(statement,'  ',' ') as stmt from pattern_results_table  where SUBSTRING_INDEX(run_id, '_', 1)= ? and replace(statement,'  ',' ') like ? ";
				
				
				 lPreparedStatement = lConnection.prepareStatement(lSQL);
				 lPreparedStatement.setString(1,projectId);
				 lPreparedStatement.setString(2,"%" + search+ "%");
				 
				 lResultSet=lPreparedStatement.executeQuery();
				// System.out.println("prep "+lPreparedStatement.toString());
				 if (lResultSet != null) {
				 while(lResultSet.next()){
					 SearchSPDTO lSearchSPDTO=new SearchSPDTO();
					 
					
						 lSearchSPDTO.setProcedureName(ToolsUtil.replaceWithSpace(ToolsUtil.removeToolKeywords(ToolsUtil.replaceNull(lResultSet.getString("PROC")))));
						 lSearchSPDTO.setStatementNo(ToolsUtil.replaceWithSpace(ToolsUtil.removeToolKeywords(ToolsUtil.replaceNull(lResultSet.getString("STATEMENT_NO")))));
						 lSearchSPDTO.setStatement(ToolsUtil.replaceWithSpace(ToolsUtil.removeToolKeywords(ToolsUtil.replaceNull(lResultSet.getString("STMT")) .toUpperCase().replaceAll(search.toUpperCase(),"<font color=\"red\"> "+search.toUpperCase()+" </font>"))) );
						 lSearchSPDTO.setSearchedWord(search);
						 
						 lSearchResultReport.add(lSearchSPDTO);
						
						
				 }
				 }
				 
				 
				 }
				 	
				 
				 }
				
				 catch (SQLException pSQLException) {
			 			pSQLException.printStackTrace();
			 		} catch (Exception pException) {
			 			pException.printStackTrace();
			 			return null;
			 		} finally {
			 			DBConnectionManager.closeConnection(lPreparedStatement,
			 					lResultSet);
			 			DBConnectionManager.closeConnection(lConnection);
			 		}
				
				 return lSearchResultReport;
			 }
			 
	 
	 public String insertIdentifyPatternDetails(String pProjectId,String pAnalysisType,String pCreatedBy,String pReportType) {
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
	 			lPreparedStatement.setString(3, "bulk");
	 			lPreparedStatement.setString(5, pCreatedBy);
	 			lPreparedStatement.setTimestamp(6, new Timestamp(System
	 					.currentTimeMillis()));
	 			
	 			lPreparedStatement.setString(7, pReportType);
	 			lPreparedStatement.executeUpdate();
	 			
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
