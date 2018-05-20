package com.tcs.tools.web.dao;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;
import com.tcs.tools.business.temp.ExtractSP_DB2;

public class Db2DatabaseAnalyticsDAO {
	private Connection lConnection= null;
	 private PreparedStatement  lPreparedStatement = null;
	 private ResultSet lResultSet = null;
	 private static Logger logger = Logger.getLogger("ToolLogger");
	 
	 
	 
	 public HashMap getDB2ConnectionDetails(String pProjectId){
		 HashMap lDBConnectionDetailsMap = new HashMap();
		 try {
	 			lConnection = DBConnectionManager.getConnection();

	 			// select the data from the table
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_PROJECT_DETAILS_TARGET_DB_DETAILS);
	 			lPreparedStatement.setString(1,pProjectId);	 			
	 			lResultSet = lPreparedStatement.executeQuery();
	 			
	 			
	 			// if rs == null, then there is no ResultSet to view
	 			if (lResultSet != null) {
	 				while (lResultSet.next()) {
	 					lDBConnectionDetailsMap.put("TARGET_DB_HOST_IP" , ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_HOST_IP")));
	 					lDBConnectionDetailsMap.put("TARGET_DB_HOST_PORT" , ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_HOST_PORT")));
	 					lDBConnectionDetailsMap.put("TARGET_DB_SCHEMA_NAME" , ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_SCHEMA_NAME")));
	 					lDBConnectionDetailsMap.put("TARGET_DB_USER_NAME" , ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_USER_NAME")));
	 					lDBConnectionDetailsMap.put("TARGET_DB_PASSWORD" , ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_PASSWORD")));

	 					lDBConnectionDetailsMap.put("TARGET_UNIX_USER_NAME" , ToolsUtil.replaceNull(lResultSet.getString("TARGET_UNIX_USER_NAME")));
	 					lDBConnectionDetailsMap.put("TARGET_UNIX_PASSWORD" , ToolsUtil.replaceNull(lResultSet.getString("TARGET_UNIX_PASSWORD")));
	 					lDBConnectionDetailsMap.put("TARGET_DB_NAME" , ToolsUtil.replaceNull(lResultSet.getString("TARGET_DB_NAME")));
	 					lDBConnectionDetailsMap.put("PROJECT_NAME" , ToolsUtil.replaceNull(lResultSet.getString("PROJECT_NAME")));	 					
	 					//lDBConnectionDetailsMap.put("PROJECT_NAME" , ToolsUtil.replaceNull(lResultSet.getString("PROJECT_NAME")));
	 					lDBConnectionDetailsMap.put("SOURCE_PATH" , ToolsUtil.replaceNull(lResultSet.getString("SOURCE_PATH")));
	 					lDBConnectionDetailsMap.put("TARGET_PATH" , ToolsUtil.replaceNull(lResultSet.getString("TARGET_PATH")));
	 					
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
		 
		 return lDBConnectionDetailsMap;
		 
	 }
	 
	 
	 
	 public Connection getDb2Connection(HashMap lDBConnectionDetailsMap){
		 logger.info("Inside getDb2Connection method:::::::");
	    
		 Connection lConnection = null;
		 try{
			 
			 logger.info("Db2 Connection Parameters:::::::");
			 
			 logger.info("::::user::::"+(String)lDBConnectionDetailsMap.get("TARGET_DB_USER_NAME"));
			 logger.info("::::pass::::"+(String)lDBConnectionDetailsMap.get("TARGET_DB_PASSWORD"));
			 
			 logger.info("::::ip::::"+(String)lDBConnectionDetailsMap.get("TARGET_DB_HOST_IP"));
			 logger.info("::::port::::"+(String)lDBConnectionDetailsMap.get("TARGET_DB_HOST_PORT"));
			 logger.info("::::schema::::"+(String)lDBConnectionDetailsMap.get("TARGET_DB_SCHEMA_NAME"));
			 
			 lConnection = DBConnectionManager.getDb2Connection(
						ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("TARGET_DB_USER_NAME")),
						ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("TARGET_DB_PASSWORD")), 
						ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("TARGET_DB_HOST_IP")), 
						ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("TARGET_DB_HOST_PORT")), 
						ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("TARGET_DB_NAME")),
						ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("TARGET_DB_SCHEMA_NAME")) );
		 }catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
			 logger.info("error getDb2Connection method:::::::"+e.getMessage());
			 logger.info("error getDb2Connection method:::::::"+e.getLocalizedMessage());
			 logger.info("error getDb2Connection method:::::::"+e.getStackTrace());
		}
		 return lConnection;
	 }
	 
	 
	 
	 
	 
	 public List populateDBInventoryReport(String pQueryType,String pProjectId,Connection pDb2Connection,String pSchema){
		 List lMainList = new ArrayList();
		 List lExcutedSPList=new ArrayList();
		 try {
			// String spText=ExtractSP_DB2.getSpText(pDb2Connection, pSPName, pQueryType);
			String lQueryToExecute = getQueryText(pQueryType,pSchema);
			lQueryToExecute = lQueryToExecute.replaceAll("\\n\\r", "");
			 
			
			 
			System.out.println(":::::;query to be executed:::::"+lQueryToExecute);
			logger.info("Inside populateDBInventoryReport method:::::::");
			List lSubList = new ArrayList();
			
			lPreparedStatement = pDb2Connection.prepareStatement(lQueryToExecute);
			logger.info("Inside populateDBInventoryReport method-prepare stmt:::::::"+pDb2Connection);
			logger.info("Inside populateDBInventoryReport method-prepare stmt-main query:::::::"+lQueryToExecute);
			lResultSet = lPreparedStatement.executeQuery();
			ResultSetMetaData rsMetaData = lResultSet.getMetaData();
			int numberOfColumns = rsMetaData.getColumnCount();
			List lHeaderList = new ArrayList();
			List lSubHeaderList = new ArrayList();
			
			if(numberOfColumns > 0){
				for (int i = 0; i < numberOfColumns; i++) {
					lHeaderList.add(rsMetaData.getColumnLabel(i+1));
				}
			}
			
			String lSubQuery ="";
			
			boolean chkHeader=false;
			boolean lExceuteQueryCalled=false;
			if(lResultSet != null){
				while(lResultSet.next()) {
					String pSpText =ExtractSP_DB2.getSpText( pDb2Connection,lResultSet.getString("SP_NAME"),pQueryType);
					lSubList = new ArrayList();
					lExceuteQueryCalled=false;
					if(numberOfColumns > 0){
						for (int i = 0; i < numberOfColumns; i++) {
							
							if( (ToolsUtil.replaceNull(lResultSet.getString(i+1)).toUpperCase().trim().contains("SP_SPACEUSED") ) 
									||( ToolsUtil.replaceNull(lResultSet.getString(i+1)).toUpperCase().trim().contains("SP_PKEYS") )
									||( ToolsUtil.replaceNull(lResultSet.getString(i+1)).toUpperCase().trim().contains("SP_HELPINDEX") )){
								
								logger.info("Inside populateDBInventoryReport method-prepare stmt-sub query:::::::"+ToolsUtil.replaceNull(lResultSet.getString(i+1)));
								lExcutedSPList=new ArrayList();
								lExcutedSPList = ToolsUtil.executeQuery(ToolsUtil.replaceNull(lResultSet.getString(i+1)), lSubList,pDb2Connection);
								for (int j = i+1 ; j < numberOfColumns; j++) {
									//ToolsUtil.replaceNull(lResultSet.getString(i+1))
									for (int k = 0; k < lExcutedSPList.size(); k++) {
										((List)lExcutedSPList.get(k)).add(ToolsUtil.replaceNull(lResultSet.getString(j+1)));
									}
									
								}
							lExceuteQueryCalled=true;
							if(chkHeader==false){
								lSubHeaderList=ToolsUtil.getResultSetHeader(pDb2Connection.createStatement().executeQuery(lResultSet.getString(i+1)));
								chkHeader=true;
								logger.info("Inside DAO::Column Label at :::"+i+"->"+(lHeaderList.get(i)).toString());
								lHeaderList.remove(i);
								for (int j = 0; j < lSubHeaderList.size(); j++) {
									
									lHeaderList.add(i+j,(String)lSubHeaderList.get(j) );
								}
								
							}
							break;
							}else{
								lSubList.add(ToolsUtil.replaceNull(lResultSet.getString(i+1)));
								
							}
							//lExcutedSPList = new ArrayList();
							
						}
					}
					if(lExceuteQueryCalled==true){						

						if(lExcutedSPList != null && lExcutedSPList.size() >0){
							for (int j = 0; j < lExcutedSPList.size(); j++) {
								logger.info("Inside DAO::true:::"+j+"->"+((List)lExcutedSPList.get(j)).toString());
								lMainList.add((List)lExcutedSPList.get(j));
							}
						}
						
					}else{
						logger.info("Inside DAO::false:::->"+lSubList.toString());
						lMainList.add(lSubList);
					}
										
				}				
			}
			lMainList.add(0, lHeaderList); 
			 
			 
			 }
		 catch (Exception e) {
			e.printStackTrace();
			logger.info("error populateDBInventoryReport method:::::::"+e.getStackTrace());
			logger.info("error populateDBInventoryReport method:::::::"+e.getMessage());
		}finally{
			DBConnectionManager.closeConnection(lConnection);
		}
		 
		 
		 return lMainList;
	 }
	 
	 public String getConsolidatedInventoryReport(String pProjectId,String pPath,String pFileName,HttpServletResponse pResponse,String pSchema){
		System.out.println("INSIDE M");
		 Connection lDb2Connection = null;
		 try {
			HashMap lQueryListMap = getWholeQueryTextDb2();
			 HashMap lDBConnectionDetailsMap = getDB2ConnectionDetails( pProjectId);
			 lDb2Connection =  getDb2Connection( lDBConnectionDetailsMap);
			  
			  logger.info("Inside getConsolidatedInventoryReport method:::::::");
			 
			 HSSFWorkbook hwb = new HSSFWorkbook();
				
				Iterator it = lQueryListMap.entrySet().iterator(); 
				while (it.hasNext()) { 
				Map.Entry pairs = (Map.Entry)it.next();
				
				List lMainList = populateDBInventoryReport( (String)pairs.getValue(), pProjectId, lDb2Connection,pSchema);
				hwb = downloadBigListAsExcelFile( lMainList, hwb,(String)pairs.getKey());
				//pairs.getKey()
				//pairs.getValue()
				it.remove(); // avoids a ConcurrentModificationException
				 
				}
				
				
				 FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
				 hwb.write(fileOut);
				 fileOut.close();
				 System.out.println("Your excel file has been generated!");
				 //for file download
				 FileUploadDownloadUtility.downloadFile(pFileName,pPath,pResponse);
				 //for file download
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("Inside getConsolidatedInventoryReport method:::::::"+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("Inside getConsolidatedInventoryReport method:::::::"+e.getMessage());
		}finally{
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			DBConnectionManager.closeConnection(lDb2Connection);
		}
			
			
		 return "";
		 
	 }
	 
	 
	 public  HSSFWorkbook downloadBigListAsExcelFile(List pMainList,HSSFWorkbook hwb,String pSheetName){
		 try{		
			 List lSubList = new ArrayList();
			 String lCellContent = "";
			// HSSFWorkbook hwb=new HSSFWorkbook();
				HSSFSheet sheet = hwb.createSheet(pSheetName);
			 if(pMainList != null && pMainList.size() > 0){
				 for (int i = 0; i < pMainList.size(); i++) {
					 lSubList = (List)pMainList.get(i);
					 HSSFRow rowhead= sheet.createRow((short)i);
					 if(lSubList != null && lSubList.size() > 0){
						 for (int j = 0; j < lSubList.size(); j++) {
							 lCellContent = (String)lSubList.get(j);
							 
							 rowhead.createCell((short) j).setCellValue(lCellContent);
						 }
					 }
					 
					
				}
			 }
			 
			
				
		 } catch (Exception e) {			
				e.printStackTrace();
		 }
		 return hwb;
		 
	 }	
	 
	 
	 public HashMap getWholeQueryTextDb2() {
		 HashMap lQueryListMap = new HashMap();
		 System.out.println(":::::inside getWholeQueryText::::");
		 		try {
		 			
		 					lQueryListMap.put(ToolsUtil.replaceNull("Stored Procedures"), ToolsUtil.replaceNull("SP_NAME_PARAMS"));
		 					lQueryListMap.put(ToolsUtil.replaceNull("Table Triggers"), ToolsUtil.replaceNull("TRIGGER_NAME"));
		 					lQueryListMap.put(ToolsUtil.replaceNull("Views"), ToolsUtil.replaceNull("VIEW_NAME"));
		 					lQueryListMap.put(ToolsUtil.replaceNull("Functions"), ToolsUtil.replaceNull("FUNCTION_NAME"));
		 					//System.out.println(":::::inside result set..........");
		 				
		 			
		 		} catch (Exception e) {
		 			e.printStackTrace();
		 			return null;
		 		} finally {
		 			// close the connection and the result set
		 			DBConnectionManager.closeConnection(lPreparedStatement,
		 					lResultSet);
		 			DBConnectionManager.closeConnection(lConnection);
		 		}
		 		
		 		System.out.println("::::lQueryListMap::::"+lQueryListMap.size());
		 		return lQueryListMap;

		 	}
	 
	 
	 public String getQueryText(String pQueryType,String pSchema) {
			String lReturnString ="";
		 		try {
		 			System.out.println("query type "+pQueryType);
		 			System.out.println("schema "+pSchema);
		 			if(pQueryType.equalsIgnoreCase("SP_NAME_PARAMS"))
		 			{
		 				lReturnString="SELECT PROCNAME AS SP_NAME FROM syscat.procedures WHERE PROCSCHEMA='"+pSchema.toUpperCase()+"' ";
		 			}
		 			if(pQueryType.equalsIgnoreCase("TRIGGER_NAME"))
		 			{
		 				lReturnString=" SELECT TRIGNAME AS SP_NAME  FROM syscat.triggers WHERE TRIGSCHEMA='"+pSchema.toUpperCase()+"' ";
		 			}
		 			if(pQueryType.equalsIgnoreCase("VIEW_NAME"))
		 			{
		 				lReturnString=" SELECT VIEWNAME AS SP_NAME  FROM syscat.views WHERE VIEWSCHEMA='"+pSchema.toUpperCase()+"' ";
		 			}
		 			if(pQueryType.equalsIgnoreCase("FUNCTION_NAME"))
		 			{
		 				lReturnString=" SELECT FUNCNAME AS SP_NAME  FROM syscat.functions WHERE FUNCSCHEMA='"+pSchema.toUpperCase()+"' ";
		 			}
		 		} catch (Exception e) {
		 			e.printStackTrace();
		 			return null;
		 		} finally {
		 			// close the connection and the result set
		 			DBConnectionManager.closeConnection(lPreparedStatement,
		 					lResultSet);
		 			DBConnectionManager.closeConnection(lConnection);
		 		}
		 		return lReturnString;

		 	}
		 
	 
}
