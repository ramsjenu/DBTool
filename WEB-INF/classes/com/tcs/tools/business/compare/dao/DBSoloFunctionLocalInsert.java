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
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class DBSoloFunctionLocalInsert {
	
	private static Logger logger = Logger.getLogger("ToolLogger");
	
	
	public String comapreTables(String pTableName ,HashMap lDbConectionMap ,String pProjectid){
		
		
		
		
		System.out.println(":::pTableName111133332222111:::"+pTableName);
		logger.info("::::::::::table name:::::"+pTableName);
		
		//HashMap lDbConectionMap = new HashMap();
		String lValidationText = "";
		
		
				
				
		
		Connection lSybaseConnection = null;
		Connection lDb2Connection = null;
		Connection lConnection=DBConnectionManager.getConnection();
		
		try{
			
			//for status window- start
			//Update Status to Front End - Start
			String lCurState="Started";
			String lStausMsg="Intializing table Compare process for table--->"+pTableName;
			ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),lConnection);
			//Update Status to Front End - End
			//for status window- end
			
			
			logger.info(":::::::::::::hashmap details-SOURCE_DB_USER_NAME:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_USER_NAME")));
			logger.info(":::::::::::::hashmap details-SOURCE_DB_PASSWORD:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_PASSWORD")));
			logger.info(":::::::::::::hashmap details-SOURCE_DB_HOST_IP:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_HOST_IP")));
			logger.info(":::::::::::::hashmap details-SOURCE_DB_HOST_PORT:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_HOST_PORT")));
			logger.info(":::::::::::::hashmap details-SOURCE_DB_SCHEMA_NAME:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_SCHEMA_NAME")));
			logger.info(":::::::::::::hashmap details-SOURCE_DB_TYPE:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_TYPE")));
			logger.info(":::::::::::::hashmap details-TARGET_DB_TYPE:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_TYPE")));
			logger.info(":::::::::::::hashmap details-TARGET_DB_USER_NAME:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_USER_NAME")));
			logger.info(":::::::::::::hashmap details-TARGET_DB_PASSWORD:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_PASSWORD")));
			logger.info(":::::::::::::hashmap details-TARGET_DB_HOST_IP:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_HOST_IP")));
			logger.info(":::::::::::::hashmap details-TARGET_DB_HOST_PORT:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_HOST_PORT")));
			logger.info(":::::::::::::hashmap details-TARGET_DB_NAME:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_NAME")));
			logger.info(":::::::::::::hashmap details-TARGET_DB_SCHEMA_NAME:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_SCHEMA_NAME")));
			logger.info(":::::::::::::hashmap details-SOURCE_DB_USER_NAME:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_USER_NAME")));
			logger.info(":::::::::::::hashmap details-SOURCE_DB_USER_NAME:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_USER_NAME")));
			logger.info(":::::::::::::hashmap details-SOURCE_DB_USER_NAME:::::::"+ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_USER_NAME")));
			
			
			//geting the source connection
			if((ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_TYPE"))).equalsIgnoreCase("SYBASE") ){
				//Update Status to Front End - Start
				lCurState="Connecting to DB Server  ";
				lStausMsg="Conneting to source db server--->"+ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_HOST_IP"));
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);							
				//Update Status to Front End - End
				
				lSybaseConnection = DBConnectionManager.getSybaseConnection(
						ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_USER_NAME"))/*user*/,
						ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_PASSWORD"))/*passwd*/,
						ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_HOST_IP")) /*host*/, 
						ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_HOST_PORT"))/*port*/, 
						ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_SCHEMA_NAME"))/*database*/);
				System.out.println(":::::::;source connection established");
				
				//Update Status to Front End - Start
				lCurState="Connecting to DB Server  ";
				lStausMsg="Conneting to source db server successfull...";
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				
			}else if((ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_TYPE"))).equalsIgnoreCase("MYSQL") ){
				//getTestConnection
				//getSybaseConnection
				
				//Update Status to Front End - Start
				lCurState="Connecting to DB Server  ";
				lStausMsg="Conneting to source db server--->"+ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_HOST_IP"));
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				
				lSybaseConnection = DBConnectionManager.getMySqlConnection(
						ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_USER_NAME"))/*user*/,
						ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_PASSWORD"))/*passwd*/,
						ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_HOST_IP")) /*host*/, 
						ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_HOST_PORT"))/*port*/, 
						ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_SCHEMA_NAME"))/*database*/);
				
				
				
				//Update Status to Front End - Start
				lCurState="Connecting to DB Server  ";
				lStausMsg="Conneting to source db server successfull";
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				
				
				System.out.println(":::::::;source connection established");
			}
			
			
			//geting the target connection
			if((ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_TYPE"))).equalsIgnoreCase("DB2") ){
				//Update Status to Front End - Start
				lCurState="Connecting to DB Server  ";
				lStausMsg="Conneting to target db server--->"+ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_HOST_IP"));
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				
				lDb2Connection = DBConnectionManager.getDb2Connection(
						ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_USER_NAME"))/*user*/,
						ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_PASSWORD"))/*passwd*/,
						ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_HOST_IP")) /*host*/, 
						ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_HOST_PORT"))/*port*/,
						ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_NAME"))/*database*/, 
						ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_SCHEMA_NAME"))/*schema*/);
				//Update Status to Front End - Start
				lCurState="Connecting to DB Server  ";
				lStausMsg="Conneting to source db server successfull...";		
				//Heading , Info , Error
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//lConnection.commit();
				
				//Update Status to Front End - End
				
				
			}else if((ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_TYPE"))).equalsIgnoreCase("MYSQL") ){
				
				//Update Status to Front End - Start
				lCurState="Connecting to DB Server  ";
				lStausMsg="Conneting to source db server--->"+ToolsUtil.replaceNull((String)lDbConectionMap.get("SOURCE_DB_HOST_IP"));		
				//Heading , Info , Error
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//lConnection.commit();				
				//Update Status to Front End - End
				
				
				lDb2Connection = DBConnectionManager.getMySqlConnection(
						ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_USER_NAME"))/*user*/,
						ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_PASSWORD"))/*passwd*/,
						ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_HOST_IP")) /*host*/, 
						ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_HOST_PORT"))/*port*/,						
						ToolsUtil.replaceNull((String)lDbConectionMap.get("TARGET_DB_SCHEMA_NAME"))/*schema*/);
				
				
				//Update Status to Front End - Start
				lCurState="Connecting to DB Server  ";
				lStausMsg="Conneting to source db server successfull";
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				System.out.println(":::::::;target connection established");
			}
			
			
			//check for the data count
			int lSourceCount = 0;
			int lTargetCount = 0;
			if(lSybaseConnection != null){
				//Update Status to Front End - Start
				lCurState="Getting Table Data";
				lStausMsg="Getting Source Table Data....";
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				
				lSourceCount = getTableDataCount( pTableName, (String)lDbConectionMap.get("SOURCE_DB_TYPE"),"OTHER"/*ASCII*/,lSybaseConnection);
				
				//Update Status to Front End - Start
				lCurState="Getting Table Data Completed";
				lStausMsg="Getting Source Table Data Completed.";
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				
				
				System.out.println("---------------------------sybase count created------------------------------"+lSourceCount);
			}
			if(lDb2Connection != null){
				
				//Update Status to Front End - Start
				lCurState="Getting Table Data";
				lStausMsg="Getting Target Table Data....";
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				
				
				lTargetCount = getTableDataCount( pTableName, (String)lDbConectionMap.get("TARGET_DB_TYPE"),"OTHER"/*ASCII*/,lDb2Connection);
				
				//Update Status to Front End - Start
				lCurState="Getting Table Data Completed";
				lStausMsg="Getting Target Table Data Completed.";			
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				
				System.out.println("---------------------------db2 count created------------------------------"+lTargetCount);
			}
			
			//Update Status to Front End - Start
			lCurState="Record Count Validation";
			lStausMsg="Comparing source and target Record Count...";			
			ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
			//Update Status to Front End - End
			
			String lCountValidate = validateDataCount( lSourceCount, lTargetCount);
			
			//Update Status to Front End - Start
			lCurState="Record Count Validation Complete";
			lStausMsg="Record Count Validation Completed Successfully...";
			ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
			//Update Status to Front End - End
			
			if(!"".equals(lCountValidate)){
				lStausMsg=lCountValidate;
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, "Complete","Data Mismatch", "Heading",new Timestamp(System.currentTimeMillis()),lConnection);
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Error",new Timestamp(System.currentTimeMillis()),lConnection);
			}
			
			
			
			
			System.out.println(":::count validation over::::::");
			
			if(!"".equals(lCountValidate)){
				return lCountValidate;
			}
			
			//check for the data count
			List lSourceList= new ArrayList();
			List lTargetList= new ArrayList();
			ResultSet lSourceResultSet = null;
			ResultSet lTargetResultSet = null;
			
			//getting the source data
			if(lSybaseConnection != null){
				//Update Status to Front End - Start
				lCurState="Data Import";
				lStausMsg="Getting Data from the source table..";			
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				
				
				//lSourceList = getTableDataList( pTableName, (String)lDbConectionMap.get("SOURCE_DB_TYPE"),"OTHER"/*ASCII*/,lSybaseConnection);
				lSourceResultSet = getTableDataResultSet( pTableName, (String)lDbConectionMap.get("SOURCE_DB_TYPE"),"OTHER"/*ASCII*/,lSybaseConnection);
				
				//Update Status to Front End - Start
				lCurState="Data Import";
				lStausMsg="Getting Data from the source table complete..";			
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				
				System.out.println("---------------------------sybase list created------------------------------"+new Timestamp(System.currentTimeMillis()));
			}
			
			//getting the Target data
			if(lDb2Connection != null){
				
				//Update Status to Front End - Start
				lCurState="Data Import";
				lStausMsg="Getting Data from the target table..";			
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				
				//lTargetList = getTableDataList( pTableName, (String)lDbConectionMap.get("TARGET_DB_TYPE"),"OTHER"/*ASCII*/,lDb2Connection);
				lTargetResultSet = getTableDataResultSet( pTableName, (String)lDbConectionMap.get("TARGET_DB_TYPE"),"OTHER"/*ASCII*/,lDb2Connection);
				
				
				//Update Status to Front End - Start
				lCurState="Data Import";
				lStausMsg="Getting Data from the source table complete..";			
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				
				System.out.println("---------------------------db2 list created------------------------------"+new Timestamp(System.currentTimeMillis()));
			}
			
			
			//System.out.println(":::: lSourceList-size:::"+lSourceList.size());
			//System.out.println(":::: lTargetList-size:::"+lTargetList.size());
			
			//check for the both record count - if this doesnt match return error 
			//lValidationText = validateDataList( lSourceList, lTargetList);
			if( lSourceResultSet != null && lTargetResultSet != null){
				//Update Status to Front End - Start
				lCurState="Data Validation";
				lStausMsg="Extracting data from tables....";			
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				
				insertResultSetData(lSourceResultSet, lTargetResultSet,pProjectid,pTableName);
				
				//lValidationText = validateDataResultSet( lSourceResultSet, lTargetResultSet);
				logger.info(":::::lValidationText:::::"+lValidationText);
				
				/*//Update Status to Front End - Start
				lCurState="Data Validation Complete";
				lStausMsg="Comparing source and target Data Completed Successfully...";
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				
				if(!"".equals(lValidationText)){
					lStausMsg=lValidationText;
					ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, "Complete","Data Mismatch", "Heading",new Timestamp(System.currentTimeMillis()),lConnection);
					ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Error",new Timestamp(System.currentTimeMillis()),lConnection);
				}
				//Update Status to Front End - End
*/				
				
			}else{
				logger.info(":::::::::::::no result set founr for source and target:::::::::::::");
				//Update Status to Front End - Start
				lCurState="Complete";
				lStausMsg="No Data found in Source or Target....";			
				ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
			}
			
			
			
			/*lSourceResultSet = null ;
			 lTargetResultSet = null;
			////////////////taking the inserted data and using
			lSourceList =  getStoredData( pProjectid, pTableName,"source");
			lTargetList =  getStoredData( pProjectid, pTableName,"target");
			
			lValidationText = validateDataList(lSourceList, lTargetList);
			
			System.out.println(":::lValidationText:::::::::::::"+lValidationText);*/
			
			
			System.out.println("---------------------------method over------------------------------"+new Timestamp(System.currentTimeMillis()));
			
		}/*catch (SQLException e) {
			// TODO: handle exception
			logger.error("::::error in ::"+e.getMessage());
		}*/catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("::::error in ::"+e.getMessage());
			//Update Status to Front End - Start
			ToolsUtil.prepareInsertStatusMsg( pProjectid, ToolConstant.CREATED_BY, "Error",e.getMessage(), "Error",new Timestamp(System.currentTimeMillis()),lConnection);
			
			//Update Status to Front End - End
			
		}finally{
			DBConnectionManager.closeConnection(lSybaseConnection);
			DBConnectionManager.closeConnection(lDb2Connection);
		}
		System.out.println("---------------------------method over going to return------------------------------"+new Timestamp(System.currentTimeMillis()));
		return lValidationText;
		
	}
	
	public String validateDataCount(int pSourceCount,int pTargetCount){
		String lValidationText ="";
		
		if((pSourceCount == 0)&&(pTargetCount == 0) ){
			return lValidationText;
		}
		
		//check for the both record count - if this doesnt match return error 
		if(pSourceCount != pTargetCount){
			lValidationText += " Record count in Source & Target doesnt match";
			lValidationText += "  <br>  Recors Count in Source-"+pSourceCount;
			lValidationText += "  <br>  Recors Count in Target-"+pTargetCount;
			
		}
		
		return lValidationText;
		
	}
	
	public String validateDataList(List pSourceList,List pTargetList){
		String lValidationText ="";
		
		
		//check for the both record count - if this doesnt match return error 
		if(pSourceList.size() != pTargetList.size() ){
			lValidationText += " Record count in Source & Target doesnt match";
			lValidationText += "  <br>  Recors Count in Source-"+pSourceList.size();
			lValidationText += "  <br>  Recors Count in Target-"+pTargetList.size();
			return lValidationText;
		}
		
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
						System.out.println("i::->"+i+"Source-->"+(String)pSourceList.get(i));
						System.out.println("i::->"+i+"Target-->"+(String)pTargetList.get(i));
						lValidationText += " <br> Record No:"+(i+1)+"is not matching";
						//return lValidationText;
					}
				}
			}
		}
		
		
		return lValidationText;
	}
	
	public String validateDataResultSet(ResultSet pSourceResultSet,	ResultSet pTargetResultSet){
		String lValidationText ="";
		
		try {
			
			System.out.println(":::::validateDataResultSet-stage1::::;"+new Timestamp(System.currentTimeMillis()));
		ResultSetMetaData rsMetaDataSource = pSourceResultSet.getMetaData();
		int lSourceColCount = rsMetaDataSource.getColumnCount();
		
		ResultSetMetaData rsMetaDataTarget = pTargetResultSet.getMetaData();
		int lTargetColCount = rsMetaDataTarget.getColumnCount();
		
		System.out.println(":::::validateDataResultSet-stage2::::;"+new Timestamp(System.currentTimeMillis()));
		
		//check if columns count is zero then dont proceed
		if( (lSourceColCount==0) || (lTargetColCount ==0) ){
			lValidationText ="Columns are not present to compare,pls check the table name....";
			return lValidationText;
		}
		System.out.println(":::::validateDataResultSet-stage3::::;"+new Timestamp(System.currentTimeMillis()));
		//check if columns count is zero then dont proceed
		if(lSourceColCount != lTargetColCount ){
			lValidationText += " Column count in Source & Target doesnt match";
			lValidationText += "  <br>  Column Count in Source-"+lSourceColCount;
			lValidationText += "  <br>  Column Count in Target-"+lTargetColCount;
			return lValidationText;
		}
		
		System.out.println(":::::validateDataResultSet-stage4::::;"+new Timestamp(System.currentTimeMillis()));		
		String lSourceRowString ="";
		String lTargetRowString ="";
		String lDelimChar =ToolConstant.TOOL_DELIMT;  //, ||
		System.out.println(":::::validateDataResultSet-stage5::::;"+new Timestamp(System.currentTimeMillis()));
		
		/*pSourceResultSet.setFetchSize(50);
		pTargetResultSet.setFetchSize(50);
		pSourceResultSet.setFetchDirection(ResultSet.FETCH_FORWARD);
		pTargetResultSet.setFetchDirection(ResultSet.FETCH_FORWARD);
	*/
		//List<String> sourceList = new ArrayList<String>((List<String>) pSourceResultSet);
		//List<String> targetList = new ArrayList<String>((List<String>) pTargetResultSet); 
		
		//System.out.println("::::converted source list size--->"+sourceList.size());
		//System.out.println("::::converted target list size--->"+targetList.size());
			int inc=1;
			if(pSourceResultSet != null && pTargetResultSet != null){
				while(pSourceResultSet.next() && pTargetResultSet.next()){
					 lSourceRowString ="";
					 lTargetRowString ="";
						for (int i = 0; i < lSourceColCount; i++) {
							if(i > 0){
								lSourceRowString += lDelimChar;
								lTargetRowString += lDelimChar;
							}
							//System.out.println(":::"+rsMetaData.getColumnType(i+1));
							//lConcatColumnName += rsMetaData.getColumnLabel(i+1);
							lSourceRowString += ToolsUtil.replaceNull(pSourceResultSet.getString(i+1));
							lTargetRowString += ToolsUtil.replaceNull(pTargetResultSet.getString(i+1));
						}
						
						if(! (lSourceRowString).equals(lTargetRowString) ){
							System.out.println("i::->"+inc+"Source-->"+lSourceRowString);
							System.out.println("i::->"+inc+"Target-->"+lTargetRowString);
							lValidationText += " <br> Record No:"+(inc)+"is not matching";
							//return lValidationText;
						}
						
						inc++;
				}
			}else{
				System.out.println("::::::::::::::no data to compare:::::::::::::");
			}
			System.out.println(":::::validateDataResultSet-stage6::::;"+new Timestamp(System.currentTimeMillis()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(null, pSourceResultSet);
			DBConnectionManager.closeConnection(null, pTargetResultSet);
		}
		
		return lValidationText;
	}
	
	public int getTableDataCount(String pTableName,String pDbType,String pMode,Connection pConnection){
		int lDataCount =0;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		String lSql ="SELECT count(*) COUNT_VAL FROM "+pTableName +"  ";
		
		System.out.println(":::::;main query in getTableDataCount:;;:"+lSql);
		try{
			lPreparedStatement = pConnection.prepareStatement(lSql);
			lResultSet = lPreparedStatement.executeQuery();
			if(lResultSet != null){
				while (lResultSet.next()) {
					lDataCount = lResultSet.getInt("COUNT_VAL");
					
				}
			}
		}catch (SQLException e) {
			// TODO: handle exception
			logger.error("::::error in ::"+e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("::::error in ::"+e.getMessage());
			e.printStackTrace();
		}
		return lDataCount;
	}
	
	public List getTableDataList(String pTableName,String pDbType,String pMode,Connection pConnection){

		List lDataList = new ArrayList();
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		try{
			String lSql ="SELECT * FROM "+pTableName +" WHERE 1=0 ";
			
			System.out.println(":::::;main query:;;:"+lSql);
			lPreparedStatement = pConnection.prepareStatement(lSql);
			lResultSet = lPreparedStatement.executeQuery();
			
			ResultSetMetaData rsMetaData = lResultSet.getMetaData();
			int numberOfColumns = rsMetaData.getColumnCount();
			String lConCatChar =",";  //, ||
			String lDelimChar ="_DBT_DELIM";  //, ||
			/*if("sybase".equalsIgnoreCase(pDbType)){
				lConCatChar ="+";
			}else if("db2".equalsIgnoreCase(pDbType)){
				lConCatChar ="||";
			}*/
			
			System.out.println("::::::::columns count:::::::"+numberOfColumns+"<--->"+new Timestamp(System.currentTimeMillis()));
			
			String lConcatColumnName="";
			
			
			for (int i = 0; i < numberOfColumns; i++) {
				if(i > 0){
					lConcatColumnName += lConCatChar;
				}
				//System.out.println(":::"+rsMetaData.getColumnType(i+1));
				lConcatColumnName += rsMetaData.getColumnLabel(i+1);
			}
			
			//logger.info("::::concatenated clumn nams::::"+lConcatColumnName);
			//System.out.println("::::concatenated clumn nams::::"+lConcatColumnName);
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			//String lSql2="SELECT CONCAT("+lConcatColumnName+") AS 'CONCAT_COLUMNS' FROM "+pTableName +" ORDER BY CONCAT_COLUMNS ASC";
			
			
			//String lSql2="SELECT "+lConcatColumnName+"  FROM "+pTableName +" ORDER BY "+lConcatColumnName +"";
			String lSql2="SELECT "+lConcatColumnName+"  FROM "+pTableName +" ";
			
			
			lPreparedStatement = pConnection.prepareStatement(lSql2);
			lResultSet = lPreparedStatement.executeQuery();
			System.out.println("::::::::SELECT QUERY TO BE EXECUTED:::::::::"+lSql2+new Timestamp(System.currentTimeMillis()));
			
			   List lSubList = new ArrayList();
			   String lTableRowData ="";
			   int inc =0;
				if(lResultSet!=null){
					while(lResultSet.next()){ 
						lTableRowData ="";
						
						
							
							for (int i = 0; i < numberOfColumns; i++) {
								if(i > 0){
									lTableRowData += lDelimChar;
								}
								//System.out.println(":::"+rsMetaData.getColumnType(i+1));
								//lConcatColumnName += rsMetaData.getColumnLabel(i+1);
								lTableRowData += ToolsUtil.replaceNull(lResultSet.getString(i+1));
							}
							lDataList.add(lTableRowData);
							//System.out.println("lTableRowData::->"+lTableRowData+inc++);
							
							
						
						//System.out.println(":::list data123:::"+lDataList.toString());
						//System.out.println(":::table record::::"+ToolsUtil.replaceNull(lResultSet.getString(1)));
						//System.out.println(":::hasvalue:::"+ToolsUtil.replaceNull(lResultSet.getString(1)).hashCode());
						
					}
				}
			
		}catch (SQLException e) {
			// TODO: handle exception
			logger.error("::::error in ::"+e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("::::error in ::"+e.getMessage());
			e.printStackTrace();
		}
		
		return lDataList ;
	
	}
	
	public ResultSet getTableDataResultSet(String pTableName,String pDbType,String pMode,Connection pConnection){

		List lDataList = new ArrayList();
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		try{
			String lSql ="SELECT * FROM "+pTableName +" WHERE 1=0 ";
			
			System.out.println(":::::;main query:;;:"+lSql);
			lPreparedStatement = pConnection.prepareStatement(lSql);
			lResultSet = lPreparedStatement.executeQuery();
			
			ResultSetMetaData rsMetaData = lResultSet.getMetaData();
			int numberOfColumns = rsMetaData.getColumnCount();
			String lConCatChar =",";  //, ||
			String lDelimChar ="_DBT_DELIM";  //, ||
			/*if("sybase".equalsIgnoreCase(pDbType)){
				lConCatChar ="+";
			}else if("db2".equalsIgnoreCase(pDbType)){
				lConCatChar ="||";
			}*/
			
			System.out.println("::::::::columns count:::::::"+numberOfColumns+"<--->"+new Timestamp(System.currentTimeMillis()));
			
			String lConcatColumnName="";
			
			
			for (int i = 0; i < numberOfColumns; i++) {
				if(i > 0){
					lConcatColumnName += lConCatChar;
				}
				//System.out.println(":::"+rsMetaData.getColumnType(i+1));
				lConcatColumnName += rsMetaData.getColumnLabel(i+1);
				System.out.println("::::::::::::meta data info::::::::::::::"+rsMetaData.getColumnType(i+1)+"<--->"+rsMetaData.getColumnTypeName(i+1));
				
			}
			
			//logger.info("::::concatenated clumn nams::::"+lConcatColumnName);
			//System.out.println("::::concatenated clumn nams::::"+lConcatColumnName);
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			//String lSql2="SELECT CONCAT("+lConcatColumnName+") AS 'CONCAT_COLUMNS' FROM "+pTableName +" ORDER BY CONCAT_COLUMNS ASC";
			
			
			//String lSql2="SELECT "+lConcatColumnName+"  FROM "+pTableName +" ORDER BY "+lConcatColumnName +"";
			String lSql2="SELECT "+lConcatColumnName+"  FROM "+pTableName +" ";
			
			
			lPreparedStatement = pConnection.prepareStatement(lSql2);
			lResultSet = lPreparedStatement.executeQuery();
			System.out.println("::::::::SELECT QUERY TO BE EXECUTED:::::::::"+lSql2+new Timestamp(System.currentTimeMillis()));
			
			   
			   
				
			
		}catch (SQLException e) {
			// TODO: handle exception
			logger.error("::::error in ::"+e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("::::error in ::"+e.getMessage());
			e.printStackTrace();
		}
		
		return lResultSet ;
	
	}

	public int getMaxAsciiValue(String pParamInput){
		int lMaxAscii=0;		
		try{ 
		        char[] cArray = pParamInput.toCharArray();
		        //int[] iArray = new int[cArray.length];
		        List lAsciiValList = new ArrayList();
		        for (int i = 0; i < cArray.length; i++) {
		        	//iArray[i] = (int)cArray[i];
		        	lAsciiValList.add((int)cArray[i]);
		        	
				}
		        
		        /*int max=0;
		        for (int i = 0; i < iArray.length; i++){
		        	while(iArray[i]>max){ 
		        		max=iArray[i];
		        	}
		        }
		        System.out.println("maximum number is= " + max);*/
		        //System.out.println("::::max value from collections:::::"+Collections.max(lAsciiValList));
		        lMaxAscii = (Integer)Collections.max(lAsciiValList);
		        cArray = null;
		        lAsciiValList = null;
		}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
		}
		
		
		return lMaxAscii;
	}
	
	public void insertResultSetData(ResultSet pSourceResultSet ,ResultSet pTargetResultSet,String pProjectId,String pTableName){
		PreparedStatement lPreparedStatementSource = null;
		PreparedStatement lPreparedStatementTarget = null;
		Connection lConnection = null;
		try {
			
			lConnection = DBConnectionManager.getConnection();
			String lSql ="";
			String lSourceRowString ="";
			String lTargetRowString ="";
			ResultSetMetaData rsMetaData = pSourceResultSet.getMetaData();
			ResultSetMetaData rsMetaDataTarget = pTargetResultSet.getMetaData();
			
			int numberOfColumns = rsMetaData.getColumnCount();
			String lConCatChar =",";  //, ||
			String lDelimChar ="_DBT_DELIM";  //, ||
			int inc =0;
			
			
			//clear existincg tables - start
			lSql = "TRUNCATE TABLE TOOL_DATA_MIGRATION_TEMP_SOURCE";
			lConnection.createStatement().executeUpdate(lSql);
			logger.info("::::::::::;crleared source table::::::");			
			lSql = "TRUNCATE TABLE TOOL_DATA_MIGRATION_TEMP_TARGET";
			lConnection.createStatement().executeUpdate(lSql);
			logger.info("::::::::::;cleared target table::::::");			
			//clear existincg tables - end
			
			//prepare insert scripts
			
			lPreparedStatementSource = lConnection.prepareStatement("INSERT INTO TOOL_DATA_MIGRATION_TEMP_SOURCE (PROJECT_ID, TABLE_NAME, DATA_VAL) VALUES(?,?,?)");
			lPreparedStatementTarget = lConnection.prepareStatement("INSERT INTO TOOL_DATA_MIGRATION_TEMP_TARGET (PROJECT_ID, TABLE_NAME, DATA_VAL) VALUES(?,?,?)");
			
			lConnection.setAutoCommit(false);
			if(pSourceResultSet != null && pTargetResultSet != null){
				while(pSourceResultSet.next() && pTargetResultSet.next()){
					 lSourceRowString ="";
					 lTargetRowString ="";
						for (int i = 0; i < numberOfColumns; i++) {
							if(i > 0){
								lSourceRowString += lDelimChar;
								lTargetRowString += lDelimChar;
							}
							//System.out.println(":::"+rsMetaData.getColumnType(i+1));
							//lConcatColumnName += rsMetaData.getColumnLabel(i+1);
							if(("datetime".equalsIgnoreCase(rsMetaData.getColumnTypeName(i+1)))|| ("smalldatetime".equalsIgnoreCase(rsMetaData.getColumnTypeName(i+1)))){
								lSourceRowString += pSourceResultSet.getTimestamp(i+1);
							}else{
								lSourceRowString += ToolsUtil.replaceNull(pSourceResultSet.getString(i+1));
							}
							
							if("TIMESTAMP".equalsIgnoreCase(rsMetaDataTarget.getColumnTypeName(i+1))){
								lTargetRowString += pTargetResultSet.getTimestamp(i+1);
							}else{
								lTargetRowString += ToolsUtil.replaceNull(pTargetResultSet.getString(i+1));
							}
							
							
							
							
						}
						
						lPreparedStatementSource.setString(1, pProjectId);
						lPreparedStatementSource.setString(2, pTableName);
						lPreparedStatementSource.setString(3, lSourceRowString);
						lPreparedStatementSource.addBatch();
						
						lPreparedStatementTarget.setString(1, pProjectId);
						lPreparedStatementTarget.setString(2, pTableName);
						lPreparedStatementTarget.setString(3, lTargetRowString);
						lPreparedStatementTarget.addBatch();
						
						inc++;
						
						if(inc == 1000){
							lPreparedStatementSource.executeBatch();
							lPreparedStatementTarget.executeBatch();
							inc =0;
						}
						
				}
			}else{
				System.out.println("::::::::::::::no data to compare:::::::::::::");
			}
			
			lPreparedStatementSource.executeBatch();
			lPreparedStatementTarget.executeBatch();
			lConnection.commit();
			lConnection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(lPreparedStatementSource, null);
			DBConnectionManager.closeConnection(lPreparedStatementTarget, null);
			DBConnectionManager.closeConnection(lConnection);
		}
	}
	
	public List getStoredData(String pProjectId,String pTableName,String pMode){
		PreparedStatement lPreparedStatement = null;		
		Connection lConnection = null;
		List lDataList = new ArrayList();
		ResultSet lResultSet = null;
		try{
			lConnection = DBConnectionManager.getConnection();
			String lSql="";
			if("source".equalsIgnoreCase(pMode)){
				lSql = "select DATA_VAL from  TOOL_DATA_MIGRATION_TEMP_SOURCE  order by DATA_VAL";
			}else if("target".equalsIgnoreCase(pMode)){
				lSql = "select DATA_VAL from TOOL_DATA_MIGRATION_TEMP_TARGET  order by DATA_VAL";
			} 
			
			lPreparedStatement =lConnection.prepareStatement(lSql);
			lResultSet = lPreparedStatement.executeQuery();
			if(lResultSet != null){
				while(lResultSet.next()){
					lDataList.add(lResultSet.getString("DATA_VAL"));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lDataList;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("::::::inside main:::::::"+new Timestamp(System.currentTimeMillis()));
		DBSoloFunctionLocalInsert lDBSoloFunction = new DBSoloFunctionLocalInsert();
		String pTableName ="GXT_CONVERSIONS"; 
		// GXT_Accrual_Reference
		//GXT_ACCRUAL_VOUCHER_DETAIL
		//GXT_CALCULATION_CONTROL
		//GXT_CASE_LIST_CASES
		//GXT_CASE_LIST
		//GXT_CONVERSIONS - 23393 - COUNT SAME IN BOTH
		
		HashMap lDbConectionMap = new HashMap();
		//for tcs domain system
		/*//for source
		lDbConectionMap.put("SOURCE_DB_HOST_IP" , "172.20.0.82");
		lDbConectionMap.put("SOURCE_DB_HOST_PORT" , "3306");
		lDbConectionMap.put("SOURCE_DB_SCHEMA_NAME" , "ToolDb");
		lDbConectionMap.put("SOURCE_DB_USER_NAME" , "ToolUser");
		lDbConectionMap.put("SOURCE_DB_PASSWORD" , "ToolUser");
		lDbConectionMap.put("SOURCE_DB_TYPE" , "SYBASE");
		
		//for target
		lDbConectionMap.put("TARGET_DB_HOST_IP" , "172.20.0.82");
		lDbConectionMap.put("TARGET_DB_HOST_PORT" , "3306");
		lDbConectionMap.put("TARGET_DB_SCHEMA_NAME" , "ToolDb");
		lDbConectionMap.put("TARGET_DB_USER_NAME" , "ToolUser");
		lDbConectionMap.put("TARGET_DB_PASSWORD" , "ToolUser");
		lDbConectionMap.put("TARGET_DB_TYPE" , "DB2");*/
		
		//for prudential n/w system
		//for source
		lDbConectionMap.put("SOURCE_DB_HOST_IP" , "48.113.192.157");
		lDbConectionMap.put("SOURCE_DB_HOST_PORT" , "7000");
		lDbConectionMap.put("SOURCE_DB_SCHEMA_NAME" , "gxprd01");
		lDbConectionMap.put("SOURCE_DB_USER_NAME" , "X174650");
		lDbConectionMap.put("SOURCE_DB_PASSWORD" , "X174650");
		lDbConectionMap.put("SOURCE_DB_TYPE" , "SYBASE");
		
		//for target
		lDbConectionMap.put("TARGET_DB_HOST_IP" , "48.140.158.105");
		lDbConectionMap.put("TARGET_DB_HOST_PORT" , "50012");
		lDbConectionMap.put("TARGET_DB_SCHEMA_NAME" , "gxprd01"); //gearsd1
		lDbConectionMap.put("TARGET_DB_USER_NAME" , "X174650");
		lDbConectionMap.put("TARGET_DB_PASSWORD" , "rahim123");
		lDbConectionMap.put("TARGET_DB_TYPE" , "DB2");
		lDbConectionMap.put("TARGET_DB_TYPE" , "DB2");
		lDbConectionMap.put("TARGET_DB_NAME" , "gearsd1"); //gearsd1
		
		lDbConectionMap  = lDBSoloFunction.getLocalDbConnectionDetails();
		String lValidationText =		lDBSoloFunction.comapreTables(pTableName,lDbConectionMap,"");
		System.out.println(":::lValidationText::::"+lValidationText);
		/*String lValidationText ="Ea";
		String lpValidationText ="FB";
		System.out.println(":::lValidationText::::"+lValidationText.hashCode());
		System.out.println(":::lpValidationText::::"+lpValidationText.hashCode());*/
		System.out.println("::::::End main:::::::"+new Timestamp(System.currentTimeMillis()));
		
		
	}
	
	public HashMap getLocalDbConnectionDetails(){
		HashMap lDbConectionMap = new HashMap();
		//for tcs domain system
		/*//for source
		lDbConectionMap.put("SOURCE_DB_HOST_IP" , "172.20.0.82");
		lDbConectionMap.put("SOURCE_DB_HOST_PORT" , "3306");
		lDbConectionMap.put("SOURCE_DB_SCHEMA_NAME" , "ToolDb");
		lDbConectionMap.put("SOURCE_DB_USER_NAME" , "ToolUser");
		lDbConectionMap.put("SOURCE_DB_PASSWORD" , "ToolUser");
		lDbConectionMap.put("SOURCE_DB_TYPE" , "MYSQL");
		
		//for target
		lDbConectionMap.put("TARGET_DB_HOST_IP" , "172.20.0.82");
		lDbConectionMap.put("TARGET_DB_HOST_PORT" , "3306");
		lDbConectionMap.put("TARGET_DB_SCHEMA_NAME" , "ToolDb");
		lDbConectionMap.put("TARGET_DB_USER_NAME" , "ToolUser");
		lDbConectionMap.put("TARGET_DB_PASSWORD" , "ToolUser");
		lDbConectionMap.put("TARGET_DB_TYPE" , "MYSQL");*/
		
		//for prudential n/w system
				//for source
				lDbConectionMap.put("SOURCE_DB_HOST_IP" , "48.113.192.157");
				lDbConectionMap.put("SOURCE_DB_HOST_PORT" , "7000");
				lDbConectionMap.put("SOURCE_DB_SCHEMA_NAME" , "gxprd01");
				lDbConectionMap.put("SOURCE_DB_USER_NAME" , "X174650");
				lDbConectionMap.put("SOURCE_DB_PASSWORD" , "X174650");
				lDbConectionMap.put("SOURCE_DB_TYPE" , "SYBASE");
				
				//for target
				lDbConectionMap.put("TARGET_DB_HOST_IP" , "48.140.158.105");
				lDbConectionMap.put("TARGET_DB_HOST_PORT" , "50012");
				lDbConectionMap.put("TARGET_DB_SCHEMA_NAME" , "gxprd01"); //gearsd1
				lDbConectionMap.put("TARGET_DB_USER_NAME" , "X174650");
				lDbConectionMap.put("TARGET_DB_PASSWORD" , "rahim123");
				lDbConectionMap.put("TARGET_DB_TYPE" , "DB2");
				lDbConectionMap.put("TARGET_DB_TYPE" , "DB2");
				lDbConectionMap.put("TARGET_DB_NAME" , "gearsd1"); //gearsd1
				
		return lDbConectionMap;
	}
	
	

}
