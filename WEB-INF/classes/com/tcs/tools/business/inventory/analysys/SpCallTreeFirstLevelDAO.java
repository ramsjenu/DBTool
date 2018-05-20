package com.tcs.tools.business.inventory.analysys;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dto.IdentifyPatternDTO;
import com.tcs.tools.web.dto.StoredProceduresDetailsDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;




public class SpCallTreeFirstLevelDAO {
	
	private Connection lConnection = null;
	private PreparedStatement  lPreparedStatement = null;
	 private ResultSet lResultSet = null;
	 
	public void getdata(String pRunId){
		// lConnection = getConnection();
		lConnection = DBConnectionManager.getConnection();
		ResultSet lResultSet = null;
		PreparedStatement lPreparedStatement = null;
		try {
			//String pRunId ="PRID74_SOURCE"; //'PRID69_SOURCE' " +//65 //69 //gears-61 //lm-all 68
			String pCreatedBy ="TCS USER";
			int lDeleteCount = deleteSPFirstCall(pRunId);
			System.out.println(":::::lDeleteCount:::::"+lDeleteCount);
			
			lPreparedStatement = lConnection.prepareStatement(" SELECT PROCEDURE_NAME,FORMED_STATEMENT,FOLDER_PATH  FROM PATTERN_RESULTS_TABLE A " +
					" 	WHERE  A.RUN_ID=  '"+pRunId+"' " +//65 //69 
					" 	and PATTERN_ID in ('PAT_36','PAT_35','PAT_34')  " +
					"	ORDER BY A.PROCEDURE_NAME  "); 
			lResultSet =lPreparedStatement.executeQuery();
			ResultSetMetaData rsMetaData = lResultSet.getMetaData();
			int numberOfColumns = rsMetaData.getColumnCount();
			//System.out.println("::::numberOfColumns:::"+numberOfColumns);
			Pattern lPatternEqualSPNamePattern = Pattern.compile("(?i)\\s*\\bEXEC\\b\\s+@\\w+\\s*=[\\w\\s\\W\\r\\n]+");
			Pattern lPatternEqualSPNameExecutePattern = Pattern.compile("(?i)\\s*\\bEXECUTE\\b\\s+@\\w+\\s*=[\\w\\s\\W\\r\\n]+");
			Matcher lPatternEqualSPNamematcher = null;
			
			List lMainList = new ArrayList();
			HashMap lDataMap = new HashMap();  
			if(lResultSet != null){
				while(lResultSet.next()) {
					String lFormedStatment = lResultSet.getString("formed_statement");
					System.out.println(":::lFormedStatment:::"+lFormedStatment);
					String lCallingProc = "";
					String[] lTmpArr=lFormedStatment.trim().split("\\s+");
					if(/*lFormedStatment.contains(" = ")*/lPatternEqualSPNamePattern.matcher(lFormedStatment).find() || lPatternEqualSPNameExecutePattern.matcher(lFormedStatment).find() ){
						//System.out.println("Procname:::"+lTmpArr.toString());	
						/*if("".equals(lTmpArr[3].trim())){
							lCallingProc = lTmpArr[2];
						}else{
							lCallingProc = lTmpArr[3];
						}*/
						lCallingProc = lTmpArr[3];
						//System.out.println("IN SIDE");
						lPatternEqualSPNamePattern.matcher(lFormedStatment);
						//System.out.println("::::inside if:::::");
					}else{
						//System.out.println("Procname:::"+lTmpArr[1]);
						if(lTmpArr.length >=2){
						lCallingProc = lTmpArr[1];
						}
						//System.out.println("lFormedStatment:::"+lFormedStatment);
						//System.out.println("::::inside else:::::");
					}
					if( lCallingProc.equals("(") || (lCallingProc == null || lCallingProc.equals(""))){
						
					}else{
					//System.out.println(lResultSet.getString("PROCEDURE_NAME").replaceAll(".src", "")+";"+lCallingProc);
					insertSPFirstCall( pRunId,lResultSet.getString("PROCEDURE_NAME").replaceAll(".src", "").trim(),lCallingProc.trim(), pCreatedBy,lResultSet.getString("FOLDER_PATH").trim(),pRunId.substring(0, 7));
					//lDataMap.put(lResultSet.getString("PROCEDURE_NAME").replaceAll(".src", ""), lCallingProc);
					}
					 
				}
			}
			
			System.out.println("-----------------------------------------------------------");
				//printing hash map
				Iterator it = lDataMap.entrySet().iterator(); 
				while (it.hasNext()) { 
					Map.Entry pairs = (Map.Entry)it.next();
					//pairs.getKey()  pairs.getValue()
					System.out.println(pairs.getKey()+";"+pairs.getValue());
				}
				
			
			//System.out.println("::::list value::::"+lSubList.toString());
			//System.out.println("::::list value::::"+lMainList.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int deleteSPFirstCall(String pRunId){
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		int lInsertCount = 0 ;
		try{
			lPreparedStatement = lConnection.prepareStatement("delete from CALL_TREE_FIRST_LEVEL_DATA where RUN_ID =? ");
			lPreparedStatement.setString(1, pRunId);
		
			
			lInsertCount = lPreparedStatement.executeUpdate();
			
		} catch (SQLException se) {
 			se.printStackTrace();
 			return 0;
 		} catch (Exception e) {
 			e.printStackTrace();
 			return 0;
 		} finally {
 			
 		}
		return lInsertCount;
	}
	public int insertSPFirstCall(String pRunId,String pSPName,String pFirstLevel,String pCreatedBy,String pFolderPath,String pProjectId){
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		int lInsertCount = 0 ;
		try{
			lPreparedStatement = lConnection.prepareStatement("INSERT INTO CALL_TREE_FIRST_LEVEL_DATA ( RUN_ID,SP_PROCEDURE_NAME, FIRST_LEVEL, CREATED_BY, CREATED_DATE,FOLDER_PATH,PROJECT_ID) VALUES(?,?,?,?,?,?,?)");
			lPreparedStatement.setString(1, pRunId);
			lPreparedStatement.setString(2, pSPName);
			lPreparedStatement.setString(3, pFirstLevel);
			lPreparedStatement.setString(4, pCreatedBy);
			lPreparedStatement.setTimestamp(5,new Timestamp(System.currentTimeMillis()));
			lPreparedStatement.setString(6, pFolderPath);
			lPreparedStatement.setString(7, pProjectId);
			
			lInsertCount = lPreparedStatement.executeUpdate();
			
		} catch (SQLException se) {
 			se.printStackTrace();
 			return 0;
 		} catch (Exception e) {
 			e.printStackTrace();
 			return 0;
 		} finally {
 			
 		}
		return lInsertCount;
	}
	
	public   Connection getConnection(){
	    //System.out.println("::::inside Get Connection method:::::"); 
	    Connection con = null;
	    /*String userName = "root";
	    String password = "";
	    String url = "jdbc:mysql://localhost/TOOLDB";*/
	    String userName = "ToolUser";
	    String password = "ToolUser";  
	    String url = "jdbc:mysql://172.20.0.46:3306/TOOLDB";
	    //String url = "jdbc:mysql://172.20.0.46:3306/Environmentdb";
	    
	 
	    
	   
	    //
	   


	        try{            
	        	 //Class.forName ("com.mysql.jdbc.Driver").newInstance ();
	        	  //  con = DriverManager.getConnection (url, userName, password);
	        	  //  System.out.println ("Database connection established");
	        	Class.forName("com.mysql.jdbc.Driver").newInstance(); 
	        	con = DriverManager.getConnection(url,userName,password); 

	             
	                 }catch(Exception e){
	            e.printStackTrace();
	        }
	    
	    return con;

	    }
	
	public List getPatternFolderDetails(String pRunId,String pPath,String pFileName){
		 PreparedStatement lPreparedStatement =  null;
		 ResultSet lResultSet = null;
		 List lSPLocationDetails = new ArrayList();
		try {
			//lConnection = getConnection();
			lConnection = DBConnectionManager.getConnection();
			StoredProceduresDetailsDTO lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
 			lPreparedStatement = lConnection.prepareStatement(" SELECT DISTINCT FOLDER_PATH FROM CALL_TREE_FIRST_LEVEL_DATA WHERE RUN_ID= ? ");
 			lPreparedStatement.setString(1, pRunId);	
 			lResultSet = lPreparedStatement.executeQuery();
 			
 			HSSFWorkbook hwb=new HSSFWorkbook(); 
 			String lSheetName="";
 			if(lResultSet != null){
 				while(lResultSet.next()){
 					if(!"".equals(((lResultSet.getString("FOLDER_PATH")))) ){
 						//hwb = getPatternAnalysisReportCompleteList( pProjectId, pRecType, pAnalysisMode, pSeqNo , pFileName, pPath, ToolsUtil.replaceNull(lResultSet.getString("FOLDER_PATH")), hwb);
 						//lStoredProceduresDetailsDTO.setFolderPath(ToolsUtil.replaceNull(lResultSet.getString("FOLDER_PATH")));
 						//lStoredProceduresDetailsDTO.setProcName(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")));
 						//lSheetName = ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME"));
 						lSPLocationDetails.add(lStoredProceduresDetailsDTO);
 						hwb = getPatternAnalysisReportCompleteListFolders( hwb , pRunId,ToolsUtil.replaceNull(lResultSet.getString("FOLDER_PATH")));
 					}
 				}
 			}
 			
 			FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
   			hwb.write(fileOut);
   			fileOut.close();
   		//for file download
	   		 //FileUploadDownloadUtility.downloadFile(pFileName,pPath,ServletActionContext.getResponse());
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
		return lSPLocationDetails;
	}
	public HSSFWorkbook getPatternAnalysisReportCompleteListFolders(HSSFWorkbook hwb ,String pRunId,String pFolderPath) {
		 PreparedStatement lPreparedStatement =  null;
		 ResultSet lResultSet = null;
		 try {
			 lConnection = DBConnectionManager.getConnection();
	 		//	lConnection = getConnection();
	 			//String pRunId = pProjectId+"_SOURCE";
	 			// List lSPLocationDetails = getPatternFolderDetails( pRunId);
	 			 
	 			String lSheetname ="";
	 			String[] lFolderArr = pFolderPath.split("\\\\");
	 			if(lFolderArr.length >= 2){
	 				lSheetname += lFolderArr[lFolderArr.length-2];
	 			}
	 			if(lFolderArr.length >= 1){
	 				lSheetname += "_"+lFolderArr[lFolderArr.length-1];
	 			}
	 			HSSFSheet sheet = hwb.createSheet(lSheetname);
	 			HSSFRow row= sheet.createRow((short)(0));
   				row.createCell((short) 0).setCellValue("S.No");
   				row.createCell((short) 1).setCellValue("SP_NAME");
   				row.createCell((short) 2).setCellValue("FIRST_LEVEL_CALL");
	 			int inc=0;
	 			//if(lSPLocationDetails != null && lSPLocationDetails.size() > 0){
	 				//for (int i = 0; i < lSPLocationDetails.size(); i++) {
	 					lPreparedStatement = lConnection.prepareStatement(" SELECT SP_PROCEDURE_NAME , FIRST_LEVEL FROM CALL_TREE_FIRST_LEVEL_DATA WHERE RUN_ID = ? and FOLDER_PATH = ? ");
	 		 			lPreparedStatement.setString(1, pRunId);	
	 		 			lPreparedStatement.setString(2, pFolderPath);
	 		 			lResultSet = lPreparedStatement.executeQuery();
	 		 			if(lResultSet != null){
	 		 				while(lResultSet.next()){
	 		 					HSSFRow row1= sheet.createRow((short)(inc+1));
			       				row1.createCell((short) 0).setCellValue((inc+1)+"");
			       				row1.createCell((short) 1).setCellValue(ToolsUtil.replaceNull(lResultSet.getString("SP_PROCEDURE_NAME")));
			       				row1.createCell((short) 2).setCellValue(ToolsUtil.replaceNull(lResultSet.getString("FIRST_LEVEL")));
			       				
			       				inc ++;
	 		 				}
	 		 			}
					//}
	 			//}
	 			
	 			/*FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
	   			hwb.write(fileOut);
	   			fileOut.close();*/
	   			
	   		//for file download
		   		 //FileUploadDownloadUtility.downloadFile(pFileName,pPath,ServletActionContext.getResponse());
		   		 //for file download
	 			
		 } catch (SQLException se) {
	 			se.printStackTrace();
	 			//return null;
	 		} catch (Exception e) {
	 			e.printStackTrace();
	 			//return null;
	 		} finally {
	 			// close the connection and the result set
	 			//DBConnectionManager.closeConnection(lPreparedStatement,
	 					//lResultSet);
	 			//DBConnectionManager.closeConnection(lConnection);
	 		}
		 return hwb;
	 }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
SpCallTreeFirstLevelDAO one = new SpCallTreeFirstLevelDAO();
String pRunId ="PRID88_SOURCE" ;
one.getdata(pRunId);

String pPath ="C:\\arun\\documents\\project\\liberty mutual\\sp call tree\\";
String pFileName ="sample.xls";
one.getPatternFolderDetails( pRunId, pPath, pFileName);
System.out.println(":::main over:::");
	}
	
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
	
	 public String insertIdentifyPatternDetails(String pProjectId,String pMigrationMode,String pAnalysisType,String pPartialSPSelected,String pCreatedBy,String pReportType) {
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
	 			lPreparedStatement.setString(3, pMigrationMode);
	 			lPreparedStatement.setString(5, pCreatedBy);
	 			lPreparedStatement.setTimestamp(6, new Timestamp(System
	 					.currentTimeMillis()));
	 			
	 			lPreparedStatement.setString(7, pReportType);
	 			lPreparedStatement.executeUpdate();
	 			
	 			if("partial".equalsIgnoreCase(pMigrationMode)){
	 				String[] lPartialSPList  = pPartialSPSelected.split(",");
	 				for (int i = 0; i < lPartialSPList.length; i++) {
	 					
	 					if(lPartialSPList[i] == null || "".equals(lPartialSPList[i])) {
	 						continue;
	 					}
	 					
	 					lPreparedStatement = lConnection.prepareStatement(WebConstant.INSERT_IDENTIFY_PATTERN_SP_LIST);
			 			lPreparedStatement.setString(1, lSeqNo);
			 			lPreparedStatement.setString(2, lPartialSPList[i]);
			 			lPreparedStatement.setString(3, pCreatedBy);
			 			lPreparedStatement.setTimestamp(4, new Timestamp(System
			 					.currentTimeMillis()));
			 			lPreparedStatement.executeUpdate();
	 					
					}
	 			}
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
	 

}
