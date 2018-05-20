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
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class InventoryAnalyticsDAO {
	
	 private Connection lConnection= null;
	 private PreparedStatement  lPreparedStatement = null;
	 private ResultSet lResultSet = null;
	 private static Logger logger = Logger.getLogger("ToolLogger");
	 
	 public String getQueryText(String pQueryType) {
		String lReturnString ="";
	 		try {
	 			lConnection = DBConnectionManager.getConnection();

	 			// select the data from the table
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_INVENTORY_ANALYSYS_QUERY);
	 			lPreparedStatement.setString(1,pQueryType);	 			
	 			lResultSet = lPreparedStatement.executeQuery();
	 			
	 			
	 			// if rs == null, then there is no ResultSet to view
	 			if (lResultSet != null) {
	 				while (lResultSet.next()) {
	 					lReturnString = ToolsUtil.replaceNull(lResultSet.getString("QUERY_TEXT"));
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
	 		return lReturnString;

	 	}
	 
	 public Connection getSybaseConnection(HashMap lDBConnectionDetailsMap){
		 logger.info("Inside getSybaseConnection method:::::::");
	    
		 Connection lConnection = null;
		 try{
			 
			 logger.info("Sybase Connection Parameters:::::::");
			 
			 logger.info("::::user::::"+(String)lDBConnectionDetailsMap.get("SOURCE_DB_USER_NAME"));
			 logger.info("::::pass::::"+(String)lDBConnectionDetailsMap.get("SOURCE_DB_PASSWORD"));
			 
			 logger.info("::::ip::::"+(String)lDBConnectionDetailsMap.get("SOURCE_DB_HOST_IP"));
			 logger.info("::::port::::"+(String)lDBConnectionDetailsMap.get("SOURCE_DB_HOST_PORT"));
			 logger.info("::::schema::::"+(String)lDBConnectionDetailsMap.get("SOURCE_DB_SCHEMA_NAME"));
			 
			 lConnection = DBConnectionManager.getSybaseConnection(
						ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_DB_USER_NAME")),
						ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_DB_PASSWORD")), 
						ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_DB_HOST_IP")), 
						ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_DB_HOST_PORT")), 
						ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_DB_SCHEMA_NAME")) );
		 }catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
			 logger.info("error getSybaseConnection method:::::::"+e.getMessage());
			 logger.info("error getSybaseConnection method:::::::"+e.getLocalizedMessage());
			 logger.info("error getSybaseConnection method:::::::"+e.getStackTrace());
		}
		 return lConnection;
	 }
	 
	 public List populateDBInventoryReport(String pQueryType,String pProjectId,Connection pSybaseConnection){
		 List lMainList = new ArrayList();
		 List lExcutedSPList=new ArrayList();
		 try {
			String lQueryToExecute = getQueryText( pQueryType);
			lQueryToExecute = lQueryToExecute.replaceAll("\\n\\r", "");
			System.out.println(":::::;query to be executed:::::"+lQueryToExecute);
			logger.info("Inside populateDBInventoryReport method:::::::");
			List lSubList = new ArrayList();
			 //HashMap lDBConnectionDetailsMap = getDBConnectionDetails( pProjectId);
			/* lConnection = DBConnectionManager.getSybaseConnection(
					 									ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_DB_HOST_IP")),
					 									ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_DB_PASSWORD")), 
					 									ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_DB_HOST_IP")), 
					 									ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_DB_HOST_PORT")), 
					 									ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_DB_SCHEMA_NAME")) );*/
			lPreparedStatement = pSybaseConnection.prepareStatement(lQueryToExecute);
			logger.info("Inside populateDBInventoryReport method-prepare stmt:::::::"+pSybaseConnection);
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
					lSubList = new ArrayList();
					lExceuteQueryCalled=false;
					if(numberOfColumns > 0){
						for (int i = 0; i < numberOfColumns; i++) {
							
							if( (ToolsUtil.replaceNull(lResultSet.getString(i+1)).toUpperCase().trim().contains("SP_SPACEUSED") ) 
									||( ToolsUtil.replaceNull(lResultSet.getString(i+1)).toUpperCase().trim().contains("SP_PKEYS") )
									||( ToolsUtil.replaceNull(lResultSet.getString(i+1)).toUpperCase().trim().contains("SP_HELPINDEX") )){
								
								logger.info("Inside populateDBInventoryReport method-prepare stmt-sub query:::::::"+ToolsUtil.replaceNull(lResultSet.getString(i+1)));
								lExcutedSPList=new ArrayList();
								lExcutedSPList = ToolsUtil.executeQuery(ToolsUtil.replaceNull(lResultSet.getString(i+1)), lSubList,pSybaseConnection);
								for (int j = i+1 ; j < numberOfColumns; j++) {
									//ToolsUtil.replaceNull(lResultSet.getString(i+1))
									for (int k = 0; k < lExcutedSPList.size(); k++) {
										((List)lExcutedSPList.get(k)).add(ToolsUtil.replaceNull(lResultSet.getString(j+1)));
									}
									
								}
							lExceuteQueryCalled=true;
							if(chkHeader==false){
								lSubHeaderList=ToolsUtil.getResultSetHeader(pSybaseConnection.createStatement().executeQuery(lResultSet.getString(i+1)));
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
			 
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("error populateDBInventoryReport method:::::::"+e.getStackTrace());
			logger.info("error populateDBInventoryReport method:::::::"+e.getMessage());
		}finally{
			DBConnectionManager.closeConnection(lConnection);
		}
		 
		 
		 return lMainList;
	 }
	 
	 
	 
	 public HashMap getDBConnectionDetails(String pProjectId){
		 HashMap lDBConnectionDetailsMap = new HashMap();
		 try {
	 			lConnection = DBConnectionManager.getConnection();

	 			// select the data from the table
	 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_PROJECT_DETAILS_SOURCE_DB_DETAILS);
	 			lPreparedStatement.setString(1,pProjectId);	 			
	 			lResultSet = lPreparedStatement.executeQuery();
	 			
	 			
	 			// if rs == null, then there is no ResultSet to view
	 			if (lResultSet != null) {
	 				while (lResultSet.next()) {
	 					lDBConnectionDetailsMap.put("SOURCE_DB_HOST_IP" , ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_HOST_IP")));
	 					lDBConnectionDetailsMap.put("SOURCE_DB_HOST_PORT" , ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_HOST_PORT")));
	 					lDBConnectionDetailsMap.put("SOURCE_DB_SCHEMA_NAME" , ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_SCHEMA_NAME")));
	 					lDBConnectionDetailsMap.put("SOURCE_DB_USER_NAME" , ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_USER_NAME")));
	 					lDBConnectionDetailsMap.put("SOURCE_DB_PASSWORD" , ToolsUtil.replaceNull(lResultSet.getString("SOURCE_DB_PASSWORD")));
	 					lDBConnectionDetailsMap.put("SOURCE_UNIX_IP" , ToolsUtil.replaceNull(lResultSet.getString("SOURCE_UNIX_IP")));
	 					lDBConnectionDetailsMap.put("SOURCE_UNIX_USER_NAME" , ToolsUtil.replaceNull(lResultSet.getString("SOURCE_UNIX_USER_NAME")));
	 					lDBConnectionDetailsMap.put("SOURCE_UNIX_PASSWORD" , ToolsUtil.replaceNull(lResultSet.getString("SOURCE_UNIX_PASSWORD")));
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
	 public HashMap getWholeQueryText() {
		 HashMap lQueryListMap = new HashMap();
		 System.out.println(":::::inside getWholeQueryText::::");
		 		try {
		 			lConnection = DBConnectionManager.getConnection();

		 			// select the data from the table
		 			lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_INVENTORY_ANALYSYS_QUERY_LIST);		 			
		 			lResultSet = lPreparedStatement.executeQuery();
		 			
		 			System.out.println(":::::inside getWholeQueryText::::");
		 			// if rs == null, then there is no ResultSet to view
		 			if (lResultSet != null) {
		 				while (lResultSet.next()) {
		 					lQueryListMap.put(ToolsUtil.replaceNull(lResultSet.getString("QUERY_TITLE")), ToolsUtil.replaceNull(lResultSet.getString("QUERY_TYPE")));
		 					//System.out.println(":::::inside result set..........");
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
		 		
		 		System.out.println("::::lQueryListMap::::"+lQueryListMap.size());
		 		return lQueryListMap;

		 	}
	 
	 public String getConsolidatedInventoryReport(String pProjectId,String pPath,String pFileName,HttpServletResponse pResponse){
		 Connection lSybaseConnection = null;
		 try {
			HashMap lQueryListMap = getWholeQueryText();
			 HashMap lDBConnectionDetailsMap = getDBConnectionDetails( pProjectId);
			  lSybaseConnection =  getSybaseConnection( lDBConnectionDetailsMap);
			  
			  logger.info("Inside getConsolidatedInventoryReport method:::::::");
			 
			 HSSFWorkbook hwb = new HSSFWorkbook();
				
				Iterator it = lQueryListMap.entrySet().iterator(); 
				while (it.hasNext()) { 
				Map.Entry pairs = (Map.Entry)it.next();
				
				List lMainList = populateDBInventoryReport( (String)pairs.getValue(), pProjectId, lSybaseConnection);
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
			DBConnectionManager.closeConnection(lSybaseConnection);
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
			 
			/* FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
			 hwb.write(fileOut);
			 fileOut.close();
			 System.out.println("Your excel file has been generated!");*/
			 //for file download
			 //FileUploadDownloadUtility.downloadFile(pFileName,pPath,pResponse);
			 //for file download
				
		 } catch (Exception e) {			
				e.printStackTrace();
		 }
		 return hwb;
		 
	 }	
	 
	
	 public static void main (String args[]){ 
	 //DOMConfigurator.configure("log4j.xml");
		 
	 logger.info("Test Log");
	 System.out.println(":::over:::");
	 }
}
