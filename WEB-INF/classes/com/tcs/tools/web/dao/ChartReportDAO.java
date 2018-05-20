package com.tcs.tools.web.dao;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dto.ChartReportDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class ChartReportDAO {
	 private Connection lConnection= null;
	 private PreparedStatement  lPreparedStatement = null;
	 private ResultSet lResultSet = null;
	 
	 
	 public List getChartDetails(String pProjectId,String pMode) {
		 pProjectId =pProjectId+"_SOURCE";
		 System.out.println("::::::pProjectId:::"+pProjectId+":::::pmode:::"+pMode);
	 		List lChartDetialsList = new ArrayList();
	 		List lTableDataList = new ArrayList();
	 		ChartReportDTO lChartReportDTO = new ChartReportDTO();
	 		String lChartValues ="";
	 		String lChartLables = "";
	 		String lChartPercValues ="";
	 		String lChartTitle ="";
	 		int lTotalCount = 0;
	 		String lCategory ="";
	 		try {
	 			lConnection = DBConnectionManager.getConnection();

	 			if("spPatternReport".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_SP_SQL_PATTERN_DATA);
		 			lPreparedStatement.setString(1, pProjectId);	 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="SQL Statement Pattern Occurrence Distribution";
		 			lCategory ="SQL Statements";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Pattern Description");
 					lChartReportDTO.setColumn3("Total Occurrence Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("spPatternReportProcCount".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_SP_SQL_PATTERN_DATA_PROC_COUNT);
		 			lPreparedStatement.setString(1, pProjectId);	 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="SQL Statement Pattern Occurrence Distribution";
		 			lCategory ="SQL Statements";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Pattern Description");
 					lChartReportDTO.setColumn3("Stored Procedure Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("patternCountInSp".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_SP_SQL_COUNT);
		 			lPreparedStatement.setString(1, pProjectId);	 			
		 			lPreparedStatement.setString(2, pProjectId);
		 			lPreparedStatement.setString(3, pProjectId);
		 			lPreparedStatement.setString(4, pProjectId);
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Stored Procedure Count";
		 			lCategory ="SQL Statements";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Total Pattern Occurrence Range");
 					lChartReportDTO.setColumn3("Stored Procedure Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("datatypePatternAnalysys".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_USAGE);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, "DTYP%");
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Datatype Usage Distribution";
		 			lCategory ="Datatypes";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Pattern Description");
 					lChartReportDTO.setColumn3("Total Occurrence Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("datatypePatternAnalysysSPWise".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_USAGE_SP_WISE);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, "DTYP%");
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Datatype Usage Distribution";
		 			lCategory ="Datatypes";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Pattern Description");
 					lChartReportDTO.setColumn3("Stored Procedure Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("datatypePatternAnalysysSPWiseComplexity".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_USAGE_SP_WISE_COMPLEXITY);
		 			
		 			lPreparedStatement.setString(1, "1 To 10");
		 			lPreparedStatement.setString(2, pProjectId);
		 			lPreparedStatement.setString(3, "DTYP%");
		 			lPreparedStatement.setInt(4, 10);
		 			
		 			lPreparedStatement.setString(5, "11 To 50");
		 			lPreparedStatement.setString(6, pProjectId);
		 			lPreparedStatement.setString(7, "DTYP%");
		 			lPreparedStatement.setInt(8, 11);
		 			lPreparedStatement.setInt(9, 50);
		 			
		 			lPreparedStatement.setString(10, "51 To 400");
		 			lPreparedStatement.setString(11, pProjectId);
		 			lPreparedStatement.setString(12, "DTYP%");
		 			lPreparedStatement.setInt(13, 51);
		 			lPreparedStatement.setInt(14, 400);
		 			
		 			lPreparedStatement.setString(15, "Greater Than 400");
		 			lPreparedStatement.setString(16, pProjectId);
		 			lPreparedStatement.setString(17, "DTYP%");
		 			lPreparedStatement.setInt(18, 400);
		 			
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Datatype Usage Distribution";
		 			lCategory ="Datatypes";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Total Pattern Occurrence Range");
 					lChartReportDTO.setColumn3("Stored Procedure Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("datatypeImpactVsNonImpact".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_IMPACT_VS_NONIMPACT);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, "DTYP%");
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Impacted Vs Non-Impacted Datatypes in the SP's";
		 			lCategory ="Datatypes";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Pattern Description");
 					lChartReportDTO.setColumn3("Impacted / Non-Impacted");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("datatypePatternAnalysysForImpacted".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_USAGE_IMPACT_YN);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, "DTYP%");
		 			lPreparedStatement.setString(3, "Y");
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Datatype Usage Distribution for Impacted Datatypes";
		 			lCategory ="Datatypes";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Pattern Description");
 					lChartReportDTO.setColumn3("Total Occurrence Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("functionPatternAnalysys".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_USAGE);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, "FUNC%");
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Built-in Function Pattern Analysis";
		 			lCategory ="Functions";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Pattern Description");
 					lChartReportDTO.setColumn3("Total Occurrence Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("functionPatternAnalysysSPWise".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_USAGE_SP_WISE);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, "FUNC%");
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Built-in Function Usages";
		 			lCategory ="Functions";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Pattern Description");
 					lChartReportDTO.setColumn3("Stored Procedure Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("functionPatternAnalysysSPWiseComplexity".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_USAGE_SP_WISE_COMPLEXITY);
		 			
		 			lPreparedStatement.setString(1, "1 To 10");
		 			lPreparedStatement.setString(2, pProjectId);
		 			lPreparedStatement.setString(3, "FUNC%");
		 			lPreparedStatement.setInt(4, 10);
		 			
		 			lPreparedStatement.setString(5, "11 To 50");
		 			lPreparedStatement.setString(6, pProjectId);
		 			lPreparedStatement.setString(7, "FUNC%");
		 			lPreparedStatement.setInt(8, 11);
		 			lPreparedStatement.setInt(9, 50);
		 			
		 			lPreparedStatement.setString(10, "51 To 100");
		 			lPreparedStatement.setString(11, pProjectId);
		 			lPreparedStatement.setString(12, "FUNC%");
		 			lPreparedStatement.setInt(13, 51);
		 			lPreparedStatement.setInt(14, 100);
		 			
		 			lPreparedStatement.setString(15, "Greater Than 100");
		 			lPreparedStatement.setString(16, pProjectId);
		 			lPreparedStatement.setString(17, "FUNC%");
		 			lPreparedStatement.setInt(18, 100);
		 			
		 			
		 			
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Functions Usage Distribution";
		 			lCategory ="Functions";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Total Pattern Occurrence Range");
 					lChartReportDTO.setColumn3("Stored Procedure Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("gVarPatternAnalysys".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_USAGE);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, "GVAR%");
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Global Variable Pattern Analysis";
		 			lCategory ="Global Variables";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Pattern Description");
 					lChartReportDTO.setColumn3("Total Occurrence Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("gVarPatternAnalysysSPWise".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_USAGE_SP_WISE);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, "GVAR%");
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Global Variable Usages";
		 			lCategory ="Global Variables";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Pattern Description");
 					lChartReportDTO.setColumn3("Stored Procedure Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("gVarPatternAnalysysSPWiseComplexity".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_USAGE_SP_WISE_COMPLEXITY);
		 			
		 			lPreparedStatement.setString(1, "1 To 10");
		 			lPreparedStatement.setString(2, pProjectId);
		 			lPreparedStatement.setString(3, "GVAR%");
		 			lPreparedStatement.setInt(4, 10);
		 			
		 			lPreparedStatement.setString(5, "11 To 20");
		 			lPreparedStatement.setString(6, pProjectId);
		 			lPreparedStatement.setString(7, "GVAR%");
		 			lPreparedStatement.setInt(8, 11);
		 			lPreparedStatement.setInt(9, 20);
		 			
		 			lPreparedStatement.setString(10, "21 To 100");
		 			lPreparedStatement.setString(11, pProjectId);
		 			lPreparedStatement.setString(12, "GVAR%");
		 			lPreparedStatement.setInt(13, 21);
		 			lPreparedStatement.setInt(14, 100);
		 			
		 			lPreparedStatement.setString(15, "Greater Than 100");
		 			lPreparedStatement.setString(16, pProjectId);
		 			lPreparedStatement.setString(17, "GVAR%");
		 			lPreparedStatement.setInt(18, 100);
		 			
		 			
		 			
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Global Variable Usage Distribution";
		 			lCategory ="Global Variables";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Total Pattern Occurrence Range");
 					lChartReportDTO.setColumn3("Stored Procedure Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("operatorPatternAnalysys".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_USAGE);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, "OPR%");
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Operator Pattern Analysis";
		 			lCategory ="Operators";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Pattern Description");
 					lChartReportDTO.setColumn3("Total Occurrence Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("operatorPatternAnalysysSPWise".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_USAGE_SP_WISE);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, "OPR%");
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Operator Usages";
		 			lCategory ="Operators";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Pattern Description");
 					lChartReportDTO.setColumn3("Stored Procedure Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("keywordPatternAnalysys".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_USAGE);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, "KWORD%");
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Keyword Pattern Analysis";
		 			lCategory ="Keywords";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Pattern Description");
 					lChartReportDTO.setColumn3("Total Occurrence Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}else if("keywordPatternAnalysysSPWise".equalsIgnoreCase(pMode)){
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_CHART_REPORT_DATATYPE_USAGE_SP_WISE);
		 			lPreparedStatement.setString(1, pProjectId);
		 			lPreparedStatement.setString(2, "KWORD%");
		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			lChartTitle ="Keyword Usages";
		 			lCategory ="Keywords";
		 			lChartReportDTO = new ChartReportDTO();
		 			lChartReportDTO.setColumn1("Pattern Category");
		 			lChartReportDTO.setColumn2("Pattern Description");
 					lChartReportDTO.setColumn3("Stored Procedure Count");
 					lTableDataList.add(lChartReportDTO);
		 			
	 			}
	 			
	 			
	 			
	 			
	 			
	 			// if rs == null, then there is no ResultSet to view
	 			if (lResultSet != null) {
	 				while (lResultSet.next()) {
	 					lChartReportDTO = new ChartReportDTO();
	 					lChartReportDTO.setColumn1(lCategory);
	 					lChartReportDTO.setColumn2(ToolsUtil.replaceNull(lResultSet.getString("MODIFIED_PATTERN_DESC")));
	 					lChartReportDTO.setColumn3(ToolsUtil.replaceNullToZero(lResultSet.getString("COUNT")));
	 					
	 					lTableDataList.add(lChartReportDTO);
	 					
	 					lTotalCount += lResultSet.getInt("COUNT");
	 					if("".equals(lChartValues)){
	 						lChartValues =ToolsUtil.replaceNullToZero(lResultSet.getString("COUNT"));
	 					}else{
	 						lChartValues =lChartValues+","+ToolsUtil.replaceNullToZero(lResultSet.getString("COUNT"));
	 					}
	 					
	 					
	 					if("".equals(lChartLables)){
	 						lChartLables =ToolsUtil.replaceNull(lResultSet.getString("MODIFIED_PATTERN_DESC"));
	 					}else{
	 						lChartLables =lChartLables+"|"+ToolsUtil.replaceNull(lResultSet.getString("MODIFIED_PATTERN_DESC"));
	 					}
	 					
	 					
	 				}
	 			}
	 			
	 			System.out.println(":::from dao side ::lChartLables::::"+lChartLables);
	 			System.out.println(":::from dao side ::lChartValues::::"+lChartValues);
	 			
	 			String[] lChartValuesArr = lChartValues.split(",");
	 			String[] lChartLablesArr = lChartLables.split("\\|");
	 			lChartPercValues ="";
	 			for(int i=0;i<lChartLablesArr.length;i++){
	 				lChartValuesArr[i]=String.valueOf( (float)Math.round( (Integer.parseInt(lChartValuesArr[i]))*1000/lTotalCount)/10);
	 				lChartLablesArr[i]=lChartLablesArr[i]+"("+lChartValuesArr[i]+"%)"; //update the total
	 			}
	 			
	 			double lDeltaValue =(double) 360/lTotalCount;

	 			lChartLables = "";
	 			lChartPercValues = "";
	 			for(int i=0;i<lChartLablesArr.length;i++){
	 				if("".equals(lChartLables)){
	 					lChartLables = lChartLablesArr[i];
	 					lChartPercValues =  lChartValuesArr[i];
	 					//lChartPercValues =  String.valueOf( new BigDecimal(Double.parseDouble(lChartValuesArr[i]) * lDeltaValue).setScale(2, RoundingMode.HALF_DOWN) );
	 				}else{
	 					//lChartPercValues = lChartPercValues+"," +String.valueOf( new BigDecimal(Double.parseDouble(lChartValuesArr[i]) * lDeltaValue).setScale(2, RoundingMode.HALF_DOWN) );;
	 					lChartPercValues = lChartPercValues+"," +lChartValuesArr[i];
	 					lChartLables = lChartLables+"|"+ lChartLablesArr[i];
	 				}
	 				
	 			}
	 			
	 			lChartDetialsList.add(lChartValues);
	 			lChartDetialsList.add(lChartLables);
	 			lChartDetialsList.add(lChartTitle);
	 			lChartDetialsList.add(lTableDataList);
	 			lChartDetialsList.add(lChartPercValues);
	 			
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
	 		return lChartDetialsList;

	 	}
	 
	 public HSSFWorkbook generateExcel(List  chartDetailsList,String pPath,String pFileName, String pSheetName,HSSFWorkbook hwb){
		
   		
   		
   		
   		
   		String lChartValues ="";
   		String lChartLables = "";
   		String lChartTitle ="";
   		List lTableDataList =new ArrayList();
   		String lChartPercValues = "";
   		
   		try {
			if(chartDetailsList != null && chartDetailsList.size() == 5){
				lChartValues = (String)chartDetailsList.get(0);
				lChartLables = (String)chartDetailsList.get(1);
				lChartTitle = (String)chartDetailsList.get(2);
				lTableDataList = (List)chartDetailsList.get(3);
				lChartPercValues  = (String)chartDetailsList.get(4);
			}
  	// HSSFWorkbook hwb=new HSSFWorkbook();
			if(!"".equals(ToolsUtil.replaceNull(pSheetName))){
				lChartTitle = pSheetName;
			}
			HSSFSheet sheet = hwb.createSheet(lChartTitle); //name as it is in the UI
			ChartReportDTO lChartReportDTO = new ChartReportDTO();
			if(lTableDataList != null && lTableDataList.size() > 0 ){
				for(int i=0;i<lTableDataList.size();i++){
					lChartReportDTO =(ChartReportDTO)lTableDataList.get(i);
					
					HSSFRow row= sheet.createRow((short)(i));
					if( i ==0 ){
						row.createCell((short) 0).setCellValue("S.No");
					}else{
						row.createCell((short) 0).setCellValue((i)+"");
					}
					row.createCell((short) 1).setCellValue(ToolsUtil.replaceZero(lChartReportDTO.getColumn1()));
					row.createCell((short) 2).setCellValue(ToolsUtil.replaceZero(lChartReportDTO.getColumn2()));		      				
					row.createCell((short) 3).setCellValue(ToolsUtil.replaceZero(lChartReportDTO.getColumn3()));
					
				}
			}
			
		/*	FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
				hwb.write(fileOut);
				fileOut.close();
				
			 
					FileUploadDownloadUtility.downloadFile(pFileName,pPath,ServletActionContext.getResponse());*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		 return hwb;
   		
	 }
	 
		 
}
