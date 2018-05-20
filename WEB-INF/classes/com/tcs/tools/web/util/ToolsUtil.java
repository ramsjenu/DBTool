package com.tcs.tools.web.util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

import org.apache.log4j.Logger;

import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.web.dto.StoredProceduresDetailsDTO;



public class ToolsUtil {
	private static Logger logger = Logger.getLogger("ToolLogger");
	
	public static String replaceNull(String pParam) {
		return (pParam == null) ? "" : pParam.trim();
	}
	public static String replaceZero(String pParam) {
		return (pParam == null || "".equals(pParam)) ? "0" : pParam.trim();
	}
	public static String replaceOne(String pParam) {
		return (pParam == null || "".equals(pParam)) ? "1" : pParam.trim();
	}
	public static int replaceZeroInt(String pParam) {
		return (pParam == null || "".equals(pParam)) ? 0 : Integer.parseInt(pParam.trim());
	}
	public static String replaceNullToZero(String pParam) {		
		return (pParam == null || "".equals(pParam)) ? "0" : pParam.trim();
		//
	}
	
	public static List replaceNullList(List pParamList) {
		return (pParamList == null) ? new ArrayList() : pParamList;
	}
	public static String removeToolChars(String pParam,String pDelimit) {		
		return (pParam == null ) ? "" : pParam.trim().replaceAll(pDelimit, "");
	}
	
	public static String replaceMisMatchDesc(String pParam){
		if(pParam == null) return "";
		//System.out.println("::::pParam:::;"+pParam);
		pParam = pParam.replaceAll("COND","Conditional Mismatch");
		pParam = pParam.replaceAll("COM_DOP","Complex Data Operation");
		pParam = pParam.replaceAll("SRC_DT","Source of Data Mismatch");
		pParam = pParam.replaceAll("DTYPE","Data Type Mismatch");
		pParam = pParam.replaceAll("OTH","Other Mismatch");
		pParam = pParam.replaceAll("VAR","Variable Mismatch");
		pParam = pParam.replaceAll("COL","Column Level Mismatch");
		pParam = pParam.replaceAll("TAB","Table Name/Alias Mismatch");
		pParam = pParam.replaceAll("SQL","SQL Type Mismatch");
		pParam = pParam.replaceAll("DTP","Data Type Mismatch");
		pParam = pParam.replaceAll("PAT","Pattern Mismatch");
		pParam = pParam.replaceAll(",","&nbsp;,<br/>");
		
		return pParam;
	}
	
	
	public static String removeToolKeywords(String pParam){
		
		if(pParam == null ) return "";
		
		//pParam = ToolsUtil.removeToolChars(pParam,"TABLE_TOOL_COLUMN_(");
		pParam= pParam.trim().replaceAll("(?i)TABLE_TOOL_COLUMN_\\(", " ( ");
		pParam= pParam.trim().replaceAll("(?i)TOOL_OTHER_\\(", "(");
		pParam= pParam.trim().replaceAll("(?i)INSERT_TOOL_COLUMN", " ");
		pParam= pParam.trim().replaceAll("(?i)INSERT_TCSTOOL", "INSERT ");
		pParam= pParam.trim().replaceAll("(?i)GRANT_EXECUTE", " GRANT EXECUTE ");
		pParam= pParam.trim().replaceAll("(?i)END_WHILE_TCSTOOL", " END WHILE ");
		//pParam= pParam.trim().replaceAll("(?i)ELSE_IF_EXISTS(", " ELSE IF EXISTS(");
		pParam= pParam.trim().replaceAll("(?i)END_WHILE_TCSTOOL", " END WHILE ");
		pParam= pParam.trim().replaceAll("(?i)END_IF_TCSTOOL", " END IF ");
		pParam= pParam.trim().replaceAll("(?i)CALL_DBMS_OUTPUT_\\s*.", " CALL DBMS_OUTPUT.");
		pParam= pParam.trim().replaceAll("(?i)ON_ROLLBACK_RETAIN_CURSORS", " ON ROLLBACK RETAIN CURSORS ");
		pParam= pParam.trim().replaceAll("(?i)WITH_RETURN", " WITH RETURN ");
		pParam= pParam.trim().replaceAll("(?i)on_commit_preserve_rows_not_logged", " on commit preserve rows not logged ");
		pParam= pParam.trim().replaceAll("(?i)FETCH_FIRST_1_ROWS_ONLY", " FETCH FIRST 1 ROWS ONLY ");
		
		pParam= pParam.trim().replaceAll("(?i)_DBT_COMM_", " ");
		
	//	pParam= pParam.trim().replaceAll("(?i)ELSE IF", "ELSEIF");
		return pParam;
	}
	

	public static String removeToolKeywordsNoTrim(String pParam){
		
		if(pParam == null ) return "";
		
		pParam= pParam.replaceAll("(?i)TABLE_TOOL_COLUMN_\\(", " ( ");
		pParam= pParam.replaceAll("(?i)TOOL_OTHER_\\(", "(");
		pParam= pParam.replaceAll("(?i)INSERT_TOOL_COLUMN", " ");
		pParam= pParam.replaceAll("(?i)INSERT_TCSTOOL", "INSERT ");
		pParam= pParam.replaceAll("(?i)GRANT_EXECUTE", " GRANT EXECUTE ");
		pParam= pParam.replaceAll("(?i)END_WHILE_TCSTOOL", " END WHILE ");
		//pParam= pParam.replaceAll("(?i)ELSE_IF_EXISTS(", " ELSE IF EXISTS(");
		
		pParam= pParam.replaceAll("(?i)END_WHILE_TCSTOOL", " END WHILE ");
		pParam= pParam.replaceAll("(?i)END_IF_TCSTOOL", " END IF ");
		pParam= pParam.replaceAll("(?i)CALL_DBMS_OUTPUT_\\s*.", " CALL DBMS_OUTPUT.");
		pParam= pParam.replaceAll("(?i)ON_ROLLBACK_RETAIN_CURSORS", " ON ROLLBACK RETAIN CURSORS ");
		pParam= pParam.replaceAll("(?i)WITH_RETURN", " WITH RETURN ");
		pParam= pParam.replaceAll("(?i)on_commit_preserve_rows_not_logged", " on commit preserve rows not logged ");
		pParam= pParam.replaceAll("(?i)FETCH_FIRST_1_ROWS_ONLY", " FETCH FIRST 1 ROWS ONLY ");
		
		pParam= pParam.replaceAll("(?i)_DBT_COMM_", " ");
		
		
		return pParam;
	}
public static String replaceWithSpace(String pParam){
		
		if(pParam == null ) return "";
		
		pParam= pParam.replaceAll("0000_TFC_", " ");
		
		return pParam;
	}


        public static String replaceTabToSace(String pParam) {
            Pattern p = Pattern.compile("\n");
             Matcher m = p.matcher(pParam); // get a matcher object
        return  m.replaceAll("\\s");

	}
        
        
        public static String readProperty(String pKey){
        	Properties prop = new Properties();
        	String pValue="";
        	try {
        		InputStream inStream = ToolsUtil.class.getClassLoader().getResourceAsStream("application.properties");
        		
        	//FileInputStream fis = new FileInputStream("C:\\arun\\code\\workspace_10102011\\WebProject\\WebContent\\config.files\\appication.properties");
        		if(inStream==null){
        			//C:\\Praveen\\Workspaces\\Web Workspace\\WebProject\\WebContent\\WEB-INF\\classes\\application.properties
        			String filename =System.getProperty("user.dir").replaceAll("\\\\", "/")+"/WebContent/WEB-INF/classes/application.properties";
        			inStream = new FileInputStream(filename);
        		}
        	prop.load(inStream);
        	// You can do something here like getting the value of a key. Example
        	pValue = prop.getProperty(pKey);
        	//System.out.println("::::pValue:::::"+pValue);
        	//pValue = pValue.replaceAll("\\\\", "/");

        	inStream.close();
        	}catch (Exception e) {
				e.printStackTrace();
			}
        	return pValue;

        }
        public static List getFileNamesFromFolder(File dirpath,List pSPListNameList){
   		 
    		File[] filesAndDirs = dirpath.listFiles();
    		if(filesAndDirs!= null && filesAndDirs.length > 0 ){
    		for (int cnt = 0; cnt < filesAndDirs.length; cnt++) {
    			File file = filesAndDirs[cnt];
    			
    			if ( file.isFile() ) {
    				//System.out.println(":::::File name::::::" + file.getName());	
    				pSPListNameList.add(file.getName().trim());
    				}else{
    					getFileNamesFromFolder(file,pSPListNameList);
    				}
    		}
    		}
    		
    		return pSPListNameList;

    	}
        public static List getFileNamesFromFolderNoExtn(File dirpath,List pSPListNameList){
      		 
    		File[] filesAndDirs = dirpath.listFiles();
    		if(filesAndDirs!= null && filesAndDirs.length > 0 ){
    		for (int cnt = 0; cnt < filesAndDirs.length; cnt++) {
    			File file = filesAndDirs[cnt];
    			
    			if ( file.isFile() ) {
    				//System.out.println(":::::File name::::::" + file.getName());	
    				pSPListNameList.add(splitFileNameAndExtension(file.getName().trim())[0]);
    				}else{
    					getFileNamesFromFolderNoExtn(file,pSPListNameList);
    				}
    		}
    		}
    		
    		return pSPListNameList;

    	}
        public static List getFileNamesFromFolderUpperNoExtn(File dirpath,List pSPListNameList){
     		 
    		File[] filesAndDirs = dirpath.listFiles();
    		if(filesAndDirs!= null && filesAndDirs.length > 0 ){
    		for (int cnt = 0; cnt < filesAndDirs.length; cnt++) {
    			File file = filesAndDirs[cnt];
    			
    			if ( file.isFile() ) {
    				//System.out.println(":::::File name::::::" + file.getName());	
    				pSPListNameList.add(splitFileNameAndExtension(file.getName().trim())[0].toUpperCase());
    				}else{
    					getFileNamesFromFolderUpperNoExtn(file,pSPListNameList);
    				}
    		}
    		}
    		
    		return pSPListNameList;

    	}
        public static List getFileNamesFromFolderDTO(File dirpath,List pSPListNameList){
        	StoredProceduresDetailsDTO lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
    		File[] filesAndDirs = dirpath.listFiles();
    		if(filesAndDirs!= null && filesAndDirs.length > 0 ){
    		for (int cnt = 0; cnt < filesAndDirs.length; cnt++) {
    			File file = filesAndDirs[cnt];
    			lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
    			
				if ( file.isFile() ) {
    				//System.out.println(":::::File name::::::" + file.getName());	
    				lStoredProceduresDetailsDTO.setProcName(file.getName().trim());
    				lStoredProceduresDetailsDTO.setFolderPath(file.getParent());
    				pSPListNameList.add(lStoredProceduresDetailsDTO);
				}else{
					getFileNamesFromFolderDTO(file,pSPListNameList);
				}
    		}
    		}
    		
    		return pSPListNameList;

    	}
        
        public static List getFileNamesFromFolderUpperCaseDTO(File dirpath,List pSPListNameList){
        	StoredProceduresDetailsDTO lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
    		File[] filesAndDirs = dirpath.listFiles();
    		if(filesAndDirs!= null && filesAndDirs.length > 0 ){
    		for (int cnt = 0; cnt < filesAndDirs.length; cnt++) {
    			File file = filesAndDirs[cnt];
    			lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
    			
				if ( file.isFile() ) {
    				//System.out.println(":::::File name::::::" + file.getName());	
    				lStoredProceduresDetailsDTO.setProcName(file.getName().trim().toUpperCase());
    				lStoredProceduresDetailsDTO.setFolderPath(file.getParent());
    				pSPListNameList.add(lStoredProceduresDetailsDTO);
				}else{
					getFileNamesFromFolderDTO(file,pSPListNameList);
				}
    		}
    		}
    		
    		return pSPListNameList;

    	}
        public static List getFileNamesFromFolderUpperCase(File dirpath,List pSPListNameList){
      		 logger.info("inside getFileNamesFromFolderUpperCase-dirpath::::"+dirpath.getPath());
    		File[] filesAndDirs = dirpath.listFiles();
    		for (int cnt = 0; cnt < filesAndDirs.length; cnt++) {
    			File file = filesAndDirs[cnt];
    			
    			if ( file.isFile() ) {
    				//System.out.println(":::::File name::::::" + file.getName());	
    				pSPListNameList.add(file.getName().trim().toUpperCase());
    				}else{
    					getFileNamesFromFolderUpperCase(file,pSPListNameList);
    				}
    		}
    		return pSPListNameList;

    	}
        
        public static List getFileNamesFromTableDTO(String pRunId){
        List pSPListNameList = new ArrayList();	
        Connection lConnection = null;
        PreparedStatement lPreparedStatement = null;
        ResultSet lResultSet = null;
        	try{
        	StoredProceduresDetailsDTO lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
        	lConnection = DBConnectionManager.getConnection();
        	lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_SP_LOCATION_DETAILS);
        	lPreparedStatement.setString(1, pRunId);
        	lResultSet = lPreparedStatement.executeQuery();
        	if(lResultSet != null){
        		while (lResultSet.next()) {
        			lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
        			lStoredProceduresDetailsDTO.setProcName(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")));
    				lStoredProceduresDetailsDTO.setFolderPath(ToolsUtil.replaceNull(lResultSet.getString("SP_DISK_LOCATION")));
    				pSPListNameList.add(lStoredProceduresDetailsDTO);
				}
        	}
        	}catch (SQLException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
				DBConnectionManager.closeConnection(lConnection);
			}
    		return pSPListNameList;

    	}
        
        public static List getFileNamesFromTable(String pRunId,String pMode){
            List pSPListNameList = new ArrayList();	
            Connection lConnection = null;
            PreparedStatement lPreparedStatement = null;
            ResultSet lResultSet = null;
            	try{
            	//logger.info(":::::pRunId::::::"+pRunId);
            	lConnection = DBConnectionManager.getConnection();
            	
            	lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_SP_LOCATION_DETAILS);
            	lPreparedStatement.setString(1, pRunId);
            	//logger.info(":::::getFileNamesFromTable::::"+lPreparedStatement);
            	lResultSet = lPreparedStatement.executeQuery();
            	
            	//logger.info(":::::getFileNamesFromTable-lResultSet::::"+lResultSet);
            	if(lResultSet != null){
            		//logger.info("::::::::inside not null result set");
            		while (lResultSet.next()) {
            		//	logger.info("::::::::inside while result set");
            		//	logger.info(":::::::::inside reseult set::::"+splitFileNameAndExtension(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")).toUpperCase())[0]);
            			if("upper".equalsIgnoreCase(pMode)){
            				pSPListNameList.add(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")).toUpperCase());
            			}if("upperNoExtn".equalsIgnoreCase(pMode)){
            				pSPListNameList.add(splitFileNameAndExtension(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")).toUpperCase())[0]);
            		//		logger.info(":::::::::proc name adding::::"+splitFileNameAndExtension(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")).toUpperCase())[0]);
            			}else{
            				pSPListNameList.add(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")));
            			}
    				}
            	}
            	}catch (SQLException e) {
    				e.printStackTrace();
    			}catch (Exception e) {
    				e.printStackTrace();
    			}finally{
    				DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
    				DBConnectionManager.closeConnection(lConnection);
    			}
        		return pSPListNameList;

        	}
        
        public static StoredProceduresDetailsDTO getSPDiskLocation(String pRunId,String pProcName){            	
            Connection lConnection = null;
            PreparedStatement lPreparedStatement = null;
            ResultSet lResultSet = null;
            StoredProceduresDetailsDTO lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
            	try{
            	
            	lConnection = DBConnectionManager.getConnection();
            	lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_SP_SINGLE_LOCATION_DETAILS);
            	lPreparedStatement.setString(1, pRunId);
            	lPreparedStatement.setString(2, pProcName);
            	lResultSet = lPreparedStatement.executeQuery();
            	
            	if(lResultSet != null){
            		while (lResultSet.next()) {
            			lStoredProceduresDetailsDTO.setProcName(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")));
        				lStoredProceduresDetailsDTO.setFolderPath(ToolsUtil.replaceNull(lResultSet.getString("SP_DISK_LOCATION")));
            			return lStoredProceduresDetailsDTO;
        			}
            	}
            	}catch (SQLException e) {
    				e.printStackTrace();
    			}catch (Exception e) {
    				e.printStackTrace();
    			}finally{
    				DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
    				DBConnectionManager.closeConnection(lConnection);
    			}
        		return lStoredProceduresDetailsDTO;

        	}
        
        public static void writeToFile(String pContent,String pFullFilePath,String pOutputDirectory){
    		//System.out.println(":::file going to be created-pFullFilePath::::"+pFullFilePath);
    		//System.out.println(":::file going to be created-pOutputDirectory::::"+pOutputDirectory);
    		try {
    			if( ! "".equals(pOutputDirectory)){
    			new File(pOutputDirectory).mkdirs();
    			}
    			
    			BufferedWriter writer = new BufferedWriter(new FileWriter(pFullFilePath));
    			//System.out.println(":::file content::::"+pContent);	

    			writer.write(pContent);
    			writer.close();
                //writer.newLine();
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
        
        public static void writeToFile(String[] pContent,String pFullFilePath,String pOutputDirectory){
    		System.out.println(":::file going to be created-pFullFilePath::::"+pFullFilePath);
    		System.out.println(":::file going to be created-pOutputDirectory::::"+pOutputDirectory);
    		try {
    			//if(!new File(pOutputDirectory).isDirectory()){
    			new File(pOutputDirectory).mkdirs();
    			//}
    			
    			BufferedWriter writer = new BufferedWriter(new FileWriter(pFullFilePath));
    			//System.out.println(":::file content::::"+pContent);	
    			//String dataLine="";
    			String[] lSubArray= null;
    			for (int i = 0; i < pContent.length;i++)
    	        {
    				lSubArray  = ((String)pContent[i]).split("\\n");
    				
    				for (int j = 0; j < lSubArray.length; j++) {
    					 writer.write(removeToolKeywordsNoTrim((String)lSubArray[j]));
        	             writer.newLine();
					}
    	        	/* writer.write((String)pContent[i]);
    	             writer.newLine();*/
    	             //dataLine += pContent[i]+"\n";
    	        }
    			//writer.write(dataLine.toString().getBytes("UTF-8"));
    			//writer.write(pContent);
    			writer.close();
                //writer.newLine();
    			
    			/*FileOutputStream fos = null; 
    			try { 
    			fos = new FileOutputStream(pFullFilePath); 
    			fos.write(pFullFilePath.getBytes("UTF-8")); 
    			} catch (IOException e) {
    			fos.close();
    			throw e; 
    			} */

			      
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
        
        public static String[] splitBlocksToArray(String[] pContent){
        	
        	List lDataList = new ArrayList();
        	String[] lSubArray = null;
			for (int i = 0; i < pContent.length;i++){
				
				lSubArray  = ((String)pContent[i]).split("\\n");
				for (int j = 0; j < lSubArray.length; j++) {
					lDataList.add((String)lSubArray[j].replaceAll("\\s", "&nbsp;").replaceAll("_DBT_SPACE_", " "));
				}
	        }
			
			return (String[])(lDataList.toArray(new String[lDataList.size()]));
        }
        
        
        public static String[] splitFileNameAndExtension(String pFilename){
        	String[] lRetArr = new String[] {"" ,"" };
        	try{
        		//splitting file name
        		String lFileSplitter ="\\\\";
        		String arr[] =pFilename.split(lFileSplitter);
        		String fiename=arr[arr.length-1];
        		String[] filenameArr = fiename.split("\\."); 
        		//System.out.println("arr:::"+arr[arr.length-1]+"::::"+filenameArr[filenameArr.length-1]);
        		
        		//file name
        		//lRetArr[0] = fiename;
        		lRetArr[0] = filenameArr[filenameArr.length-2];
        		
        		//extension
        		lRetArr[1] = filenameArr[filenameArr.length-1];
        		 
        	}catch (Exception e) {
				e.printStackTrace();
				logger.info(":::::::inside exception::::::"+e.getMessage());
			}
        	return lRetArr;
        }
        public static String[] splitFileNameAndPath(String pFilename){
        	String[] lRetArr = new String[] {"" ,"" };
	        try {
				String[] name = pFilename.split("\\\\");
				lRetArr[1] = name[name.length-1];
				String lFilePath = "";
				for (int i = 0; i < name.length-1; i++) {
					if( i >0 ) lFilePath +="\\\\";
					
					lFilePath += name[i];
				}
				lRetArr[0] = lFilePath+"\\";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        return lRetArr;
        }
        
        public static List executeQuery(String pQuery,List pSubList,Connection pConnection){
    		List lTempRowList=new ArrayList();
    		List pMainList =new ArrayList();
    		try {
    			if(!"".equals(pQuery)/* && pConnection!=null*/){
    				ResultSet lResultSet=pConnection.createStatement().executeQuery(pQuery);
    				ResultSetMetaData rsMetaData = lResultSet.getMetaData();
    				int numberOfColumns = rsMetaData.getColumnCount();
    				if(lResultSet!=null){
    					while(lResultSet.next()){ 
    						lTempRowList = new ArrayList();
    						for (int i = 0; i < pSubList.size(); i++) {
								String lColDataStr=(String)pSubList.get(i);								
								lTempRowList.add(lColDataStr);
							}
    						
    						for (int i = 0; i < numberOfColumns; i++) {
    							lTempRowList.add(ToolsUtil.replaceNull(lResultSet.getString(i+1)));
    						}
    						logger.info("Inside Execute Query:::::->"+lTempRowList.toString());
    						pMainList.add(lTempRowList);
    					}
    				}
    			}

    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		return pMainList;
    	}
/*        public static List executeQuery(String pQuery,List pRowList,Connection pConnection,List pMainList){
    		List lTempRowList=new ArrayList();
    		try {
    			if(!"".equals(pQuery) && pConnection!=null){
    				ResultSet lResultSet=pConnection.createStatement().executeQuery(pQuery);
    				ResultSetMetaData rsMetaData = lResultSet.getMetaData();
    				int numberOfColumns = rsMetaData.getColumnCount();
    				if(lResultSet!=null){
    					while(lResultSet.next()){    						
    						for (int i = 0; i < numberOfColumns; i++) {
    							pRowList.add(ToolsUtil.replaceNull(lResultSet.getString(i+1)));
    						}

    					}
    				}
    			}

    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		return pRowList;
    	}*/
        
        public static List getResultSetHeader(ResultSet lResultSet){
    		List lHeaderList=new ArrayList();
    		try {
    			
    				ResultSetMetaData rsMetaData = lResultSet.getMetaData();
    				int numberOfColumns = rsMetaData.getColumnCount();
    				if(lResultSet!=null){
    					//lResultSet.first();
    					for (int i = 0; i < numberOfColumns; i++) {
    						lHeaderList.add(rsMetaData.getColumnLabel(i+1));
    					}	
    			}

    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		return lHeaderList;
    	}
        public static  void prepareInsertStatusMsg(String pRunid,String UserId,String CurStage,String pMsg,String pMsgType,Timestamp pCreatedDate,Connection pConnection){
        	
    		//RUN_ID, USER_ID, CURRENT_STAGE, STATUS_MSG, MSG_TYPE, CREATED_DATE
    		try {
    			if(pCreatedDate == null){
    				pCreatedDate = new Timestamp(System.currentTimeMillis());
    			}
    			System.out.println("pMsg:::->"+pMsg);
    			PreparedStatement lPreparedStatement=pConnection.prepareStatement(ToolConstant.INSERT_PROCESS_STATUS_DATA);
    			lPreparedStatement.setString(1, pRunid);
    			lPreparedStatement.setString(2, UserId);
    			lPreparedStatement.setString(3, CurStage);
    			lPreparedStatement.setString(4, pMsg);
    			lPreparedStatement.setString(5, pMsgType);
    			lPreparedStatement.setTimestamp(6,pCreatedDate);
    			lPreparedStatement.executeUpdate();
    			if(pConnection.getAutoCommit()==false){
    				pConnection.commit();
    			}
    			
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
        public static String getProjectId(String pRunid){
        	return pRunid.replaceAll("(?i)(_SOURCE|_TARGET)", "").trim();
        }
        

        //handleMultiLineComments will replace all spaces in side multi line comments with tool specified constat(Ex:_DBT_COMM_)
        public static String[] handleMultiLineComments(String [] lines){
    		boolean lisComment=false;
    		String lTmpLine="";
    		String[] lRetLine=lines;
    		for (int cnt = 0; cnt < lines.length; cnt++) {
    			lTmpLine="";
    			String[] words = lines[cnt].trim().split("\\s+");
    			
    			for (int i = 0; i < words.length; i++) {			
    				
    				 if("/*".equals(words[i].trim()))
    				 {
    					 //System.out.println("Comment Set");
    					 lisComment=true;
    				 }
    				 else if("*/".equals(words[i].trim()))
    				 {
    					 lisComment=false;
    					 lTmpLine+=words[i];
    					 continue;
    				 }						 
    				 if(lisComment==true)
    				 {
    					 if("\n".equals(words[i].trim())){
    						 lTmpLine+=" "+words[i]+" ";
    					 }else if(!"".equals(words[i].trim())){
    						 lTmpLine+=words[i]+"_DBT_COMM_";
    					 }					 
    					 continue;
    				 }
    				 
    				 lTmpLine+=words[i]+" ";				 
    				 
    			}
    			
    			lRetLine[cnt]=lTmpLine;
    		}
    		return lRetLine;
    	}
        
        public static void removeSqlWaysLogs(String pProjectId,String pSourceFilePath ,String pTargetFilePath){
        	logger.info(":::removeSqlWaysLogs method called::::"+pProjectId);
   		 StoredProceduresDetailsDTO lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
   		StoredProceduresDetailsDTO lStoredProceduresDetailsTargetDTO = new StoredProceduresDetailsDTO();
   			//List lSourceFileList = ToolsUtil.getFileNamesFromFolderUpperNoExtn(new File(pSourceFilePath), new ArrayList());
   		List lSourceFileList = new ArrayList();
   			if(pSourceFilePath == null || "".equals(pSourceFilePath)){
   			 lSourceFileList = ToolsUtil.getFileNamesFromTable(pProjectId+"_SOURCE","upperNoExtn");
   			}else{
   			 lSourceFileList = ToolsUtil.getFileNamesFromFolderUpperNoExtn(new File(pSourceFilePath), new ArrayList());
   			}
   			
   			logger.info("::::::lSourceFileList:::::"+lSourceFileList.toString());
   			List lTargetFileList = ToolsUtil.getFileNamesFromFolderDTO(new File(pTargetFilePath), new ArrayList());
   			
   			//logger.info(":::::source removeSqlWaysLogs::::"+lSourceFileList.toString());
   			String lCompleteFileName="";
   			String lProcName="";
   			if(lTargetFileList != null && lTargetFileList.size() >0){
   				for (int i = 0; i < lTargetFileList.size(); i++) {
   					lStoredProceduresDetailsDTO = (StoredProceduresDetailsDTO)lTargetFileList.get(i);
   					lCompleteFileName = lStoredProceduresDetailsDTO.getFolderPath()+"\\"+lStoredProceduresDetailsDTO.getProcName();   					lProcName = splitFileNameAndExtension(lStoredProceduresDetailsDTO.getProcName())[0];
   					//logger.info(":::::target proc name::::"+lCompleteFileName);
   					lProcName = splitFileNameAndExtension(lStoredProceduresDetailsDTO.getProcName())[0] ;
   					logger.info(":::::lProcName:::::"+lProcName);
   					if(! lSourceFileList.contains(lProcName.toUpperCase())){
   						logger.info(":::::::::::removeSqlWaysLogs method->file getting deleted:::::"+lCompleteFileName);
   						new File(lCompleteFileName).delete();
   						
   					}else{
   						logger.info(":::::::::::removeSqlWaysLogs method->no deletion:::::"+lCompleteFileName);
   					}
   					
   				}
   			}
   			
   			
   			
   	 }
       public static  List arrayToList(String[] pArray){
    	   List lList=new ArrayList();
    	   //System.out.println(":::pArray Length::"+pArray.length);
    	   for (int i = 0; i < pArray.length; i++) {
    		   lList.add((String)pArray[i]);
    	   }
    	   //System.out.println(":::lList Length::"+lList.size());
    	   return lList;
       }
       
       public static  boolean validateFileType(String pFullFilePath,String pAllowedExtn){
    	   String lErrorText ="";
    	   JFileChooser chooser = new JFileChooser();
			  String fileTypeName = chooser.getTypeDescription(new File(pFullFilePath));
			  logger.info("::::::validateFileType-pFullFilePath::::::"+pFullFilePath);
			  String[] lAllowedExtnArr = pAllowedExtn.split("\\,"); 
			  for (int i = 0; i < lAllowedExtnArr.length; i++) {
				  if(fileTypeName.toUpperCase().contains(lAllowedExtnArr[0])){
						 return true;
					  }
			  }
			  
			  return false;
       }
       
      public static String removeHtmlTags(String html){
    	   html = html.replaceAll("(?i)\\<br\\>|\\<br\\/\\>","\n");
    	   html = html.replaceAll("\\<.*?\\>","") ; 
    	   html = html.replaceAll("&nbsp;"," ");    
    	   html = html.replaceAll("&amp;" , "");   
    	   return html; 
    	   } 
      
      public static String getDateTime() {   
          SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");   
          Calendar cal=Calendar.getInstance();
          System.out.println(cal);
          return dateFormat.format(cal.getTime());   
      }  
      
      public static String[] expand(String[] array, int size) {
  		String[] temp = new String[size];
  		System.arraycopy(array, 0, temp, 0, array.length);
  		for(int j = array.length; j < size; j++)
  		temp[j] = "";
  		return temp;
  		}
      
      
      public static String readFile(File file) {
     		//String[] lines = null;
     		//String [] lNewLines=null;
     		String lFileContent ="";
     		try {
     			InputStream is = new FileInputStream(file);
     			StringBuffer buffer = new StringBuffer();
     			byte[] b = new byte[4096];
     			for (int n; (n = is.read(b)) != -1;) {
     				
     				buffer.append(new String(b, 0, n));
     			}
     			
     			lFileContent = buffer.toString();
     			
     		} catch (FileNotFoundException e) {
     			e.printStackTrace();
     			
     		} catch (Exception e) {
     			e.printStackTrace();
     			
     		}
     		return lFileContent;
     	}
         
        
        public static void main(String[] args) {
        	System.out.println("::::::");
        	System.out.println(":::: from prop file:::::"+ToolsUtil.readProperty("fileUploadPath"));
        }
       
}
