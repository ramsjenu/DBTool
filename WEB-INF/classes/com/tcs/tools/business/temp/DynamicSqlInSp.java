package com.tcs.tools.business.temp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class DynamicSqlInSp {
	
	public void getDynamicSqlFromPatternResults(String pPath,String pFileName,String pRunId){
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		
		try{
			lConnection = DBConnectionManager.getConnection();
			String sql= " SELECT PROCEDURE_NAME,STATEMENT_NO,FORMED_STATEMENT" +
					"  FROM pattern_results_table " +
					" where run_id = ?  " +
					" and pattern_id in ('PAT_34','PAT_35','PAT_36') " +
					" and formed_statement like '%(%' ";
			lPreparedStatement = lConnection.prepareStatement(sql);
			lPreparedStatement.setString(1, pRunId);
			lResultSet = lPreparedStatement.executeQuery();
			List lDataList= new ArrayList();
			List lSubList = new ArrayList();
			lSubList.add("PROCEDURE_NAME");
			lSubList.add("LINE_NO");
			lSubList.add("STATEMENT");
			lDataList.add(lSubList);
			while (lResultSet.next()) {
				lSubList = new ArrayList();
				lSubList.add(lResultSet.getString("PROCEDURE_NAME"));
				lSubList.add(lResultSet.getString("STATEMENT_NO"));
				lSubList.add(lResultSet.getString("FORMED_STATEMENT"));
				lDataList.add(lSubList);
			}
			
			FileUploadDownloadUtility.downloadListAsExcelFile( lDataList, pPath, pFileName,null);
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			DBConnectionManager.closeConnection(lConnection);
		}
	}
	
	
	public HashMap getDynamicSqlFromPatternResultsCount(String pRunId){
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		HashMap lDataMap=  new HashMap();
		try{
			lConnection = DBConnectionManager.getConnection();
			String sql= " SELECT procedure_name,count(*) COUNT_VAL" +
					" FROM pattern_results_table " +
					" where run_id = ?  " +
					" and pattern_id in ('PAT_34','PAT_35','PAT_36') " +
					" and formed_statement like '%(%' " +
					" group by procedure_name " +
					" order by procedure_name;";
			lPreparedStatement = lConnection.prepareStatement(sql);
			lPreparedStatement.setString(1, pRunId);
			lResultSet = lPreparedStatement.executeQuery();
		
			while (lResultSet.next()) {
				lDataMap.put(lResultSet.getString("procedure_name"), lResultSet.getString("COUNT_VAL"));
			}
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			DBConnectionManager.closeConnection(lConnection);
		}
		return lDataMap;
	}
	
	
	
	
	public void getProcWiseDsqlCount(String pPath,String pFileName,String pRunId){
		HashMap lDataMap = getDynamicSqlFromPatternResultsCount( pRunId);
		
		
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		
		try{
			lConnection = DBConnectionManager.getConnection();
			String sql= " SELECT procedure_name FROM tool_project_sp_file_location_details  where run_id= ?   ";
			lPreparedStatement = lConnection.prepareStatement(sql);
			lPreparedStatement.setString(1, pRunId);
			lResultSet = lPreparedStatement.executeQuery();
		
			List lMainList= new ArrayList();
			List lSubList = new ArrayList();
			lSubList.add("S.No");
			lSubList.add("PROCEDURE_NAME");
			lSubList.add("DSQL_COUNT");
			lMainList.add(lSubList);
			int inc = 1;
			while (lResultSet.next()) {
				lSubList = new ArrayList();
				lSubList.add(inc+"");
				lSubList.add(lResultSet.getString("procedure_name"));
				lSubList.add(ToolsUtil.replaceNullToZero((String)lDataMap.get(lResultSet.getString("procedure_name"))));
				lMainList.add(lSubList);
				inc++;
			}
			
			
			FileUploadDownloadUtility.downloadListAsExcelFile( lMainList, pPath, pFileName,null);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			DBConnectionManager.closeConnection(lConnection);
		}
		
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("::::::inside main:::::");
		DynamicSqlInSp lDynamicSqlInSp = new DynamicSqlInSp();
		String pPath ="C:\\arun\\Tool Output\\dcms\\DSQL_REPORT\\";
		String pFileName ="DSQL_REPORT_DETAILS.xls";
		String pRunId="PRID106_SOURCE";
		lDynamicSqlInSp.getDynamicSqlFromPatternResults(pPath, pFileName,pRunId);
		
		 pFileName ="DSQL_REPORT_COUNT.xls";
		 lDynamicSqlInSp.getProcWiseDsqlCount(pPath, pFileName, pRunId);
		

	}

}
