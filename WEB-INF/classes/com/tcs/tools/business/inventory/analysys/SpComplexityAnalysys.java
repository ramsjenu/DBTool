package com.tcs.tools.business.inventory.analysys;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dto.SPComplexityAnalysysProcDataDTO;
import com.tcs.tools.web.dto.SPComplexityScoreDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class SpComplexityAnalysys {

	private Connection lConnection = null;
	private ResultSet lResultSet = null;
	private PreparedStatement lPreparedStatement = null;
			
	
	/**
	 * @param pRunId
	 * @param pFileName
	 * @param pPath
	 * @param pAnalysysMode
	 */
	public void getPatternAnalysisReportCompleteListFolders(String pRunId,String pFileName,String pPath,String pAnalysysMode) {
		 PreparedStatement lPreparedStatement =  null;
		 ResultSet lResultSet = null;
		 try {
	 			lConnection = DBConnectionManager.getConnection();
	 			HSSFWorkbook hwb=new HSSFWorkbook();
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_DISTINCT_SP_FOLDER_PATHS);
	 			lPreparedStatement.setString(1, pRunId);	
	 			lResultSet = lPreparedStatement.executeQuery();
	 			if(lResultSet != null){
	 				while(lResultSet.next()){
	 					if(!"".equals((ToolsUtil.replaceNull(lResultSet.getString("FOLDER_PATH")))) ){
	 						String pInputFolderPath =ToolsUtil.replaceNull(lResultSet.getString("FOLDER_PATH"));
	 						
	 						hwb = getComplexityMatrixSpDetails( pRunId, pFileName, pPath, pAnalysysMode, hwb, pInputFolderPath);
	 						//hwb = getPatternAnalysisReportCompleteList( pProjectId, pRecType, pAnalysisMode, pSeqNo , pFileName, pPath, ToolsUtil.replaceNull(lResultSet.getString("FOLDER_PATH")), hwb);
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
	
	/**
   	 * method to get the complete list of the data
   	 * @return
   	 */
   	public HSSFWorkbook getComplexityMatrixSpDetails(String pRunId,String pFileName,String pPath,String pAnalysysMode,HSSFWorkbook hwb,String pInputFolderPath) {
   		List lComplexityMatrixSpDetailsList = new ArrayList(); 
   		
   		try {
   			
   			//lConnection = DBConnectionManager.getConnection();
   			SPComplexityAnalysysProcDataDTO lSPComplexityAnalysysProcDataDTO = new SPComplexityAnalysysProcDataDTO();
   			if("PARTIAL".equalsIgnoreCase(pAnalysysMode)){
   				
   			}else{
	          	lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_COMPLEXITY_MATRIX_SP_DATA);
	            lPreparedStatement.setString(1,pRunId);
	            lPreparedStatement.setString(2,pInputFolderPath);
	            lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
	   			lResultSet = lPreparedStatement.executeQuery();
   			}
   			
   			
   			String[] lFolderArr = pInputFolderPath.split("\\\\");
	 			String lSheetname ="";
	 			if(lFolderArr.length >= 2){
	 				lSheetname += lFolderArr[lFolderArr.length-2];
	 			}
	 			if(lFolderArr.length >= 1){
	 				lSheetname += "_"+lFolderArr[lFolderArr.length-1];
	 			}
	 			System.out.println("::::pInputFolderPath:::"+pInputFolderPath);
	 			
   			
   						// if rs == null, then there is no ResultSet to view
   		
   						//HSSFWorkbook hwb=new HSSFWorkbook();
                  		HSSFSheet sheet = hwb.createSheet(lSheetname);
                  		
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
                  		rowhead.createCell((short) 11).setCellValue("Dynamic Sqls");
                  		
                  		
                  		
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
	      				row.createCell((short) 11).setCellValue(ToolsUtil.replaceZero(lResultSet.getString("DYNAMIC_SQLS")));
	      				
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
	      				lSPComplexityAnalysysProcDataDTO.setDynamicSqls(ToolsUtil.replaceZero(lResultSet.getString("DYNAMIC_SQLS")));
	      				
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
	      				lParameterList.add(ToolsUtil.replaceNull(lResultSet.getString("DYNAMIC_SQLS")));
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
	      				lParameterMap.put("DYNAMIC_SQLS", ToolsUtil.replaceNull(lResultSet.getString("DYNAMIC_SQLS")));
	      				lSPComplexityAnalysysProcDataDTO.setSpScoresMap(lParameterMap);
	      				
	      				lComplexityMatrixSpDetailsList.add(lSPComplexityAnalysysProcDataDTO );
      				  
                      i++;     
   				}
   			}
   			
   		


   			
  	

   			/*FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
   			hwb.write(fileOut);
   			fileOut.close();*/
   			
   		 
   			
                       

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
   	
   	public HSSFWorkbook createExcelForComplexityMatrixSpDetailsCopy(List lComplexityMatrixSpDetailsList,HSSFWorkbook hwb){
   		
   		return hwb;   		
   	}
   	
   	/**
   	 * method to get the complete list of the data
   	 * @return
   	 */
   	public List getComplexityMatrixSpDetailsCopy(String pRunId,String pFileName,String pPath,String pAnalysysMode,String pInputFolderPath) {
   		List lComplexityMatrixSpDetailsList = new ArrayList(); 
   		PreparedStatement lPreparedStatement = null;
   		ResultSet lResultSet = null;
   		try {
   			
   			//lConnection = DBConnectionManager.getConnection();
   			SPComplexityAnalysysProcDataDTO lSPComplexityAnalysysProcDataDTO = new SPComplexityAnalysysProcDataDTO();
   			if("PARTIAL".equalsIgnoreCase(pAnalysysMode)){
   				
   			}else{
	          	lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_COMPLEXITY_MATRIX_SP_DATA);
	            lPreparedStatement.setString(1,pRunId);
	            lPreparedStatement.setString(2,pInputFolderPath);
	            lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
	   			lResultSet = lPreparedStatement.executeQuery();
	   			//System.out.println("::::::getComplexityMatrixSpDetailsCopy - lPreparedStatement::::"+lPreparedStatement);
   			}
   			
   			
   			String[] lFolderArr = pInputFolderPath.split("\\\\");
	 			String lSheetname ="";
	 			if(lFolderArr.length >= 2){
	 				lSheetname += lFolderArr[lFolderArr.length-2];
	 			}
	 			if(lFolderArr.length >= 1){
	 				lSheetname += "_"+lFolderArr[lFolderArr.length-1];
	 			}
	 			System.out.println("::::pInputFolderPath:::"+pInputFolderPath);
	 			
   			
   						// if rs == null, then there is no ResultSet to view
   		
   						
                  		
                  		
              List lParameterList = new ArrayList();    		
              HashMap lParameterMap = new HashMap();
                  		
            int i=0;              
   			if (lResultSet != null) {
   				// this will step through our data row-by-row
   				while (lResultSet.next()) {
   					lSPComplexityAnalysysProcDataDTO = new SPComplexityAnalysysProcDataDTO();
                      
	      				
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
	      				lSPComplexityAnalysysProcDataDTO.setDynamicSqls(ToolsUtil.replaceZero(lResultSet.getString("DYNAMIC_SQLS")));
	      				
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
	      				lParameterList.add(ToolsUtil.replaceNull(lResultSet.getString("DYNAMIC_SQLS")));
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
	      				lParameterMap.put("DYNAMIC_SQLS", ToolsUtil.replaceNull(lResultSet.getString("DYNAMIC_SQLS")));
	      				lSPComplexityAnalysysProcDataDTO.setSpScoresMap(lParameterMap);
	      				
	      				lComplexityMatrixSpDetailsList.add(lSPComplexityAnalysysProcDataDTO );
      				  
                      i++;     
   				}
   			}
   			
   		


   			
  	

   			/*FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
   			hwb.write(fileOut);
   			fileOut.close();*/
   			
   		 
   			
                       

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

   		return lComplexityMatrixSpDetailsList;
   	}
   	
   	
   	public void calcSpComplexityWeightageDataWhole (String pRunId,String pFileName,String pPath,String pAnalysysMode,String pDbMigrationType){
   		
   		
   			 PreparedStatement lPreparedStatement =  null;
   			 ResultSet lResultSet = null;
   			 try {
   		 			lConnection = DBConnectionManager.getConnection();
   		 			HSSFWorkbook hwb=new HSSFWorkbook();
   		 			HSSFWorkbook hwbOne=new HSSFWorkbook();
   		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_DISTINCT_SP_FOLDER_PATHS);
   		 			lPreparedStatement.setString(1, pRunId);	
   		 			lResultSet = lPreparedStatement.executeQuery();
   		 			List lExcelDataList = new ArrayList();
   		 			
   		 			
   		 			if(lResultSet != null){
   		 				while(lResultSet.next()){
   		 					if(!"".equals((ToolsUtil.replaceNull(lResultSet.getString("FOLDER_PATH")))) ){
   		 					lExcelDataList = new ArrayList();
   		 					String pInputFolderPath =ToolsUtil.replaceNull(lResultSet.getString("FOLDER_PATH"));
   		 					//for creating first sheet
   		 					
   		 					lExcelDataList = calcSpComplexityWeightageData( pRunId, pFileName, pPath, pAnalysysMode,  pDbMigrationType, pInputFolderPath);
   		 					
   		 				createComplexityScoreSheet(lExcelDataList,pDbMigrationType,pFileName,pPath,hwb,pInputFolderPath);
   		 						//hwb = getComplexityMatrixSpDetails( pRunId, pFileName, pPath, pAnalysysMode, hwb, pInputFolderPath);
   		 						//hwb = getPatternAnalysisReportCompleteList( pProjectId, pRecType, pAnalysisMode, pSeqNo , pFileName, pPath, ToolsUtil.replaceNull(lResultSet.getString("FOLDER_PATH")), hwb);
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
   	
 	/**
 	 * @param pRunId
 	 * @param pFileName
 	 * @param pPath
 	 * @param pAnalysysMode
 	 * @param pDownloadMode
 	 * @param pDbMigrationType
 	 * @return
 	 */
 	public List calcSpComplexityWeightageData(String pRunId,String pFileName,String pPath,String pAnalysysMode,String pDbMigrationType,String pInputFolderPath){
 		
 		
 		//lConnection = DBConnectionManager.getConnection();
 		
   		List lComplexityMatrixSpDetailsList    = getComplexityMatrixSpDetailsCopy( pRunId, pFileName, pPath, pAnalysysMode, pInputFolderPath);
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
												
												
												lInnerMap.put(lParameterType, lSPComplexityScoreDTO.getScore());
												
											 }
						   					}
						   				}
	   				
					}
					}		
   				
   					//lSPComplexityAnalysysProcDataDTOFinal.setLinesOfCode("1111");
   					lSPComplexityAnalysysProcDataDTOFinal.setProcName(lSPComplexityAnalysysProcDataDTO.getProcName());
	   				lSPComplexityAnalysysProcDataDTOFinal.setSpScoresMap(lInnerMap);
   					lReturnList.add(lSPComplexityAnalysysProcDataDTOFinal);
					
			}
   		}
   		
   		System.out.println(":::stage4:::::");
   		
   		return lReturnList;
   	}
 	
 	
	public HashMap getSPComplexityMasterData(String pDbMigrationType){
   		
   		HashMap lSpComplexityCompleteMasterMap = new HashMap();
   		HashMap lSpComplexityCompleteInnerMasterMap = new HashMap();
   		PreparedStatement lPreparedStatementInner = null;
   		ResultSet lResultSetInner = null;
   		
   		try{
   			//lConnection = DBConnectionManager.getConnection();
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
 			//DBConnectionManager.closeConnection(lConnection);
 		}
 		return lSpComplexityCompleteMasterMap;
   		
   	}
	public List getSPComplexityParameters(String pDbMigrationType){		   		
   		List lSpComplexityCompleteMasterMap = new ArrayList();		  
   		PreparedStatement lPreparedStatement = null;
   		ResultSet lResultSet = null;
   		try{
   			//lConnection = DBConnectionManager.getConnection();
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
 			//DBConnectionManager.closeConnection(lConnection);
 		}
 		return lSpComplexityCompleteMasterMap;
   		
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
	
	public HashMap getSPComplexityWeightage(String pDbMigrationType){		   		
		HashMap lSpComplexityCompleteMasterMap = new HashMap();		
		PreparedStatement lPreparedStatement = null;
   		ResultSet lResultSet = null;
   		try{
   			//lConnection = DBConnectionManager.getConnection();
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
 			//DBConnectionManager.closeConnection(lConnection);
 		}
 		return lSpComplexityCompleteMasterMap;
   		
   	}
	
	public HashMap getSPComplexityScoreCategory(String pDbMigrationType){		   		
		HashMap lSpComplexityCompleteMasterMap = new HashMap();		   
		PreparedStatement lPreparedStatement = null;
   		ResultSet lResultSet = null;
   		try{
   			//lConnection = DBConnectionManager.getConnection();
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
 			//DBConnectionManager.closeConnection(lConnection);
 		}
 		return lSpComplexityCompleteMasterMap;
   		
   	}
	
	
	public HSSFWorkbook createComplexityScoreSheet(List lComplexityWeightageDataList,String pDbMigrationType,String pFileName,String pPath,HSSFWorkbook hwb,String pInputFolderPath){
   		try {
			SPComplexityAnalysysProcDataDTO lSPComplexityAnalysysProcDataDTO = new SPComplexityAnalysysProcDataDTO();
			
			List lComplexityParameters = getSPComplexityParameters(pDbMigrationType);
			 HashMap lComplexityWeightageMap = getSPComplexityWeightage( pDbMigrationType);
			 
			 HashMap lComplexityScoreMap = getSPComplexityScoreCategory( pDbMigrationType);
			 
			 String[] lFolderArr = pInputFolderPath.split("\\\\");
	 			String lSheetname ="";
	 			if(lFolderArr.length >= 2){
	 				lSheetname += lFolderArr[lFolderArr.length-2];
	 			}
	 			if(lFolderArr.length >= 1){
	 				lSheetname += "_"+lFolderArr[lFolderArr.length-1];
	 			}
	 			System.out.println("::::pInputFolderPath:::"+pInputFolderPath);
			//HSSFWorkbook hwb=new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet(lSheetname);
			
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
			rowhead.createCell((short) 11).setCellValue("Dynamic Sqls");
			rowhead.createCell((short) 12).setCellValue("Weighted Score");
			rowhead.createCell((short) 13).setCellValue("Overall Complexity");
			

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
   		return hwb;
   	}
	
   	public static void main(String[] args){
   		System.out.println("::::inside main:::");
   		String pRunId ="PRID118_SOURCE" ;
   		String pFileName = "sample_sheet1.xls";
   		String pPath = "C:\\arun\\Tool Output\\dcms\\";
   		String pAnalysysMode = "BULK";
   		String pMigrationType ="SYSBASE_TO_DB2";
   		SpComplexityAnalysys lSpComplexityAnalysys = new SpComplexityAnalysys();
   		//for sheet1
   	  lSpComplexityAnalysys.getPatternAnalysisReportCompleteListFolders(pRunId,pFileName,pPath,pAnalysysMode) ;
   	  
   	  //for sheet 2
   	  pFileName = "sample_sheet2.xls";
   	  lSpComplexityAnalysys.calcSpComplexityWeightageDataWhole ( pRunId, pFileName, pPath, pAnalysysMode, pMigrationType);
   	}
   	
}
