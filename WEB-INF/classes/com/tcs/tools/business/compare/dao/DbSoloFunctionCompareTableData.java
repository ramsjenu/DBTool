package com.tcs.tools.business.compare.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class DbSoloFunctionCompareTableData {
	
	Connection lConnection = null;
	
	public void localDataCompare(String pProjectId,String pTableName,String pCreatedBy){
		System.out.println(":::::methd started::::::"+"<--->"+new Timestamp(System.currentTimeMillis()));
		List lSourceList = new ArrayList();
		List lTargetList = new ArrayList();
		String lValidationText = "";
		int lrecCount = getRecordCount( pProjectId, pTableName);
		
		int  pStartIndex =1;
		int  pEndIndex =10000;
		int lPageSize =10000;
		String lCurState ="";
		String lStausMsg = "";
		String lCurrentValidationContent="";
		
		try{
			lConnection = DBConnectionManager.getConnection();
		
		
		System.out.println(":::::Record Count in Table:"+lrecCount+"<--->"+new Timestamp(System.currentTimeMillis()));		
		System.out.println("Splitting Total rows into segments of"+lPageSize+"<--->"+new Timestamp(System.currentTimeMillis()));
		
		//Update Status to Front End - Start
		lStausMsg = "Data Validation";
		lCurState = "Data Validation";
		ToolsUtil.prepareInsertStatusMsg( pProjectId, pCreatedBy, lCurState,lStausMsg, "Heading",null,lConnection);
		lStausMsg = ":::::Record Count in Table:"+lrecCount;
		lCurState = "Data Validation";
		ToolsUtil.prepareInsertStatusMsg( pProjectId, pCreatedBy, lCurState,lStausMsg, "Info",null,lConnection);
		lStausMsg = "Splitting Total rows into segments of"+lPageSize;
		lCurState = "Data Validation";
		ToolsUtil.prepareInsertStatusMsg( pProjectId, pCreatedBy, lCurState,lStausMsg, "Info",null,lConnection);
		//Update Status to Front End - End
		
		//System.out.println(":::::Record Count in Table:"+lrecCount);		
		//System.out.println("Splitting Total rows into segments of"+lPageSize);
		
		for (int i = 0; i <= lrecCount/lPageSize; i++) {
			
			lSourceList = null;
			lTargetList = null;
			pStartIndex = pStartIndex*i*lPageSize+1;
			//pEndIndex = pStartIndex+lPageSize-1;
			pEndIndex = lPageSize;			
			System.out.println(":::::pStartIndex::::::::"+pStartIndex+"::::pEndIndex::::"+pEndIndex+"<--->"+new Timestamp(System.currentTimeMillis()));
			
			//Update Status to Front End - Start
			lStausMsg = "Segment No:"+(i+1)+"-Recors Start index->"+pStartIndex+"-Records End Index->"+(pStartIndex+pEndIndex-1);
			lCurState = "Data Validation";
			ToolsUtil.prepareInsertStatusMsg( pProjectId, pCreatedBy, lCurState,lStausMsg, "Info",null,lConnection);
			lStausMsg = "Going to get Source List for segment:"+(i+1);
			lCurState = "Data Validation";
			ToolsUtil.prepareInsertStatusMsg( pProjectId, pCreatedBy, lCurState,lStausMsg, "Info",null,lConnection);
			//Update Status to Front End - End
			
			//System.out.println("Segment No:"+(i+1)+"-Recors Start index->"+pStartIndex+"-Records End Index->"+pEndIndex);
			//System.out.println("Going to get Source List for segment:"+(i+1));
			
			lSourceList = getStoredData( pProjectId, pTableName,"source",pStartIndex,pEndIndex,lConnection);
			
			//Update Status to Front End - Start
			lStausMsg = "Source List populated for segment:"+(i+1);
			lCurState = "Data Validation";
			ToolsUtil.prepareInsertStatusMsg( pProjectId, pCreatedBy, lCurState,lStausMsg, "Info",null,lConnection);
			lStausMsg = "Going to get Target List for segment:"+(i+1);
			lCurState = "Data Validation";
			ToolsUtil.prepareInsertStatusMsg( pProjectId, pCreatedBy, lCurState,lStausMsg, "Info",null,lConnection);
			//Update Status to Front End - End
			
			//System.out.println("Source List populated for segment:"+(i+1));
			//System.out.println("Going to get Target List for segment:"+(i+1));
			
			System.out.println(":::::got source list::::::i val->"+i+"<--->"+new Timestamp(System.currentTimeMillis()));
			lTargetList = getStoredData( pProjectId, pTableName,"target",pStartIndex,pEndIndex,lConnection);
			//System.out.println("Target List populated for segment:"+(i+1));
			//System.out.println("Validating data for segment:"+(i+1));
			//Update Status to Front End - Start
			lStausMsg = "Target List populated for segment:"+(i+1);
			lCurState = "Data Validation";
			ToolsUtil.prepareInsertStatusMsg( pProjectId, pCreatedBy, lCurState,lStausMsg, "Info",null,lConnection);
			lStausMsg ="Validating data for segment:"+(i+1);
			lCurState = "Data Validation";
			ToolsUtil.prepareInsertStatusMsg( pProjectId, pCreatedBy, lCurState,lStausMsg, "Info",null,lConnection);
			//Update Status to Front End - End
			
			
			System.out.println(":::::got target list::::::i val->"+i+"<--->"+new Timestamp(System.currentTimeMillis()));
			lCurrentValidationContent += validateDataList( lSourceList, lTargetList);
			//System.out.println("Validating data for segment:"+(i+1)+"--->"+"Complete...");
			//Update Status to Front End - Start
			lStausMsg = "Validating data for segment:"+(i+1)+"--->"+"Complete...";
			lCurState = "Data Validation";
			ToolsUtil.prepareInsertStatusMsg( pProjectId, pCreatedBy, lCurState,lStausMsg, "Info",null,lConnection);
			if(!"".equals(lCurrentValidationContent)){
				lStausMsg = lCurrentValidationContent;
				lCurState = "Data Validation Error";
				ToolsUtil.prepareInsertStatusMsg( pProjectId, pCreatedBy, lCurState,lStausMsg, "Error",null,lConnection);
			}
			//Update Status to Front End - End
			
			System.out.println("::::::::lValidationText:::"+lCurrentValidationContent+"<--->"+new Timestamp(System.currentTimeMillis()));
			System.out.println("::::::source list size:::"+lSourceList.size()+":::::ltarget list size:::"+lTargetList.size());
			lValidationText += lCurrentValidationContent;
			pStartIndex = 1;
			lSourceList = null;
			lTargetList = null;
			lCurrentValidationContent ="";
			
			System.gc();
		}
		
		insertDataComaprisonReport( pProjectId, pTableName, lValidationText, pCreatedBy);
		
		//Update Status to Front End - Start
		if(!"".equals(lValidationText)){
			lStausMsg = lValidationText;
			lCurState = "Data Validation Error";
			ToolsUtil.prepareInsertStatusMsg( pProjectId, pCreatedBy, lCurState,lStausMsg, "Error",null,lConnection);
		}
		
		lStausMsg = "Data Validation process Competeled SucessFuly....";
		lCurState = "Data Validation";
		ToolsUtil.prepareInsertStatusMsg( pProjectId, pCreatedBy, lCurState,lStausMsg, "Info",null,lConnection);	
		//Update Status to Front End - End
		
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(lConnection);
		}
		System.out.println("::::::::lValidationText-consolidated:::"+lValidationText+"<--->"+new Timestamp(System.currentTimeMillis()));
		System.out.println(":::::methd ended::::::"+"<--->"+new Timestamp(System.currentTimeMillis()));
	}
	
	
	public List getStoredData(String pProjectId,String pTableName,String pMode,int pStartIndex,int pEndIndex,Connection lConnection){
		PreparedStatement lPreparedStatement = null;	
		
		List lDataList = new ArrayList();
		ResultSet lResultSet = null;
		try{
			//lConnection = DBConnectionManager.getConnection();
			
			
			String lSql="";
			if("source".equalsIgnoreCase(pMode)){
				/*lSql = "select DATA_VAL, ROW_NUM " +
						" from (select DATA_VAL,@rownum := @rownum+1 ROW_NUM from TOOL_DATA_MIGRATION_TEMP_SOURCE order by DATA_VAL) a ,(SELECT @rownum:=0) b " +
						" where ROW_NUM >="+pStartIndex+" and ROW_NUM <="+pEndIndex;*/ 
				
				lSql =" select DATA_VAL from TOOL_DATA_MIGRATION_TEMP_SOURCE  order by DATA_VAL limit "+(pStartIndex-1)+","+pEndIndex;
				}else if("target".equalsIgnoreCase(pMode)){
				/*lSql = "select DATA_VAL, ROW_NUM " +
						" from (select DATA_VAL,@rownum := @rownum+1 ROW_NUM from TOOL_DATA_MIGRATION_TEMP_TARGET order by DATA_VAL) a ,(SELECT @rownum:=0) b " +
						" where ROW_NUM >="+pStartIndex+" and ROW_NUM <="+pEndIndex;*/
					lSql =" select DATA_VAL from TOOL_DATA_MIGRATION_TEMP_TARGET  order by DATA_VAL limit "+(pStartIndex-1)+","+pEndIndex;
			} 
			
			lPreparedStatement =lConnection.prepareStatement(lSql);
			System.out.println("Query to fetch data Executed.....");
			System.out.println(":::::query preparing for getting list::::::->"+"<--->"+new Timestamp(System.currentTimeMillis()));
			System.out.println(":::::query to get list::::"+lSql);
			lResultSet = lPreparedStatement.executeQuery();
			System.out.println(":::::query preparing for getting list executed::::::->"+"<--->"+new Timestamp(System.currentTimeMillis()));
			if(lResultSet != null){
				while(lResultSet.next()){
					lDataList.add(lResultSet.getString("DATA_VAL"));
				}
			}
			System.out.println("Data List populated...");
			
			System.out.println(":::::data added to list::::::->"+"<--->"+new Timestamp(System.currentTimeMillis()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
		}
		return lDataList;
	}
	
	public String validateDataList(List pSourceList,List pTargetList){
		String lValidationText ="";
		
		
//		//check for the both record count - if this doesnt match return error 
//		if(pSourceList.size() != pTargetList.size() ){
//			lValidationText += " Record count in Source & Target doesnt match";
//			lValidationText += "  <br>  Recors Count in Source-"+pSourceList.size();
//			lValidationText += "  <br>  Recors Count in Target-"+pTargetList.size();
//			return lValidationText;
//		}
		
		//check for the list and compare i to i
		if(pSourceList != null && pSourceList.size() >0 ){
			for (int i = 0; i < pSourceList.size(); i++) {
				//System.out.println(":::::i value::::"+i);
				//checking if the target list with ith element is existing
				if(pTargetList != null && pTargetList.size() >= i ){
					//System.out.println("Source-->"+(String)pSourceList.get(i));
					//System.out.println("Target-->"+(String)pTargetList.get(i));
					//checking if i to i list values are same
					if(! ((String)pSourceList.get(i)).equals((String)pTargetList.get(i)) ){
						System.out.println("********************************i::->"+i+"Source-->"+(String)pSourceList.get(i));
						System.out.println("********************************i::->"+i+"Target-->"+(String)pTargetList.get(i));
						lValidationText += " <br> Record No:"+(i+1)+"is not matching";
						lValidationText += " <br> Source Row Data:->"+(String)pSourceList.get(i);
						lValidationText += " <br> Target Row Data:->"+(String)pTargetList.get(i);
						//return lValidationText;
					}
				}
			}
		}
		
		pSourceList = null;
		pTargetList = null;
		return lValidationText.replaceAll("_DBT_DELIM", ",");
	}
	
	

	public int getRecordCount(String pProjectId,String pTableName){
		PreparedStatement lPreparedStatement = null;		
		Connection lConnection = null;
		List lDataList = new ArrayList();
		ResultSet lResultSet = null;
		try{
			lConnection = DBConnectionManager.getConnection();
			String lSql="";
		
				lSql = " select count(*) COUNT_VAL from TOOL_DATA_MIGRATION_TEMP_TARGET "; 
			
			lPreparedStatement =lConnection.prepareStatement(lSql);
			lResultSet = lPreparedStatement.executeQuery();
			if(lResultSet != null){
				while(lResultSet.next()){
					return lResultSet.getInt("COUNT_VAL");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int insertDataComaprisonReport(String pProjectId,String pTableName,String pErrorText,String pCreatedBy){
		PreparedStatement lPreparedStatement = null;		
		
		int lInsertCount = 0;
		
		try{
			lConnection = DBConnectionManager.getConnection();
			lPreparedStatement =lConnection.prepareStatement(WebConstant.INSERT_DATA_MIGRATION_ERROR_REPORT);
			lPreparedStatement.setString(1, pProjectId);
			lPreparedStatement.setString(2, pTableName);
			lPreparedStatement.setString(3, pErrorText);
			lPreparedStatement.setString(4, pCreatedBy);
			lPreparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			lInsertCount = lPreparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return lInsertCount;
	}
	public static void main(String[] args){
		System.out.println(":::::inside main::::");
		DbSoloFunctionCompareTableData lDbSoloFunctionCompareTableData =  new DbSoloFunctionCompareTableData();
		lDbSoloFunctionCompareTableData.localDataCompare("","","TCS_USER");
	}
}
