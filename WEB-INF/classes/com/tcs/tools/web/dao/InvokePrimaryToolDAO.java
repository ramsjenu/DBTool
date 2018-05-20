package com.tcs.tools.web.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.tcs.tools.business.analysis.main.SequentialRunning;
import com.tcs.tools.business.fileupload.dao.FileUploadDAO;
import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dto.ProjectDetailsMainDTO;
import com.tcs.tools.web.dto.StoredProceduresDetailsDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.SQLWaysConnectionUtil;
import com.tcs.tools.web.util.ToolsUtil;
import com.tcs.tools.web.util.ZipUtilRecursive;

public class InvokePrimaryToolDAO {
	 private Connection lConnection= null;
	 private PreparedStatement  lPreparedStatement = null;
	 private ResultSet lResultSet = null;
	 
	 private static Logger logger = Logger.getLogger("ToolLogger");

	 
	 public String getSPSourceLocationDetail(String pRunId , String pSeqNo,String pMigrationMode,String pSpInputCopiedPath){
		 String lConcatenatedFileNames ="";
		 try{
		 lConnection = DBConnectionManager.getConnection();
		 if("partial".equalsIgnoreCase(pMigrationMode)){
			 lPreparedStatement =lConnection.prepareStatement(WebConstant.GET_DISTINCT_PATTERN_FOLDER_PATHS_PARTIAL);
			 lPreparedStatement.setString(1,pRunId);
			 lPreparedStatement.setString(2,pSeqNo);
			 lResultSet = lPreparedStatement.executeQuery();
		 }else{
			 lPreparedStatement =lConnection.prepareStatement(WebConstant.GET_DISTINCT_PATTERN_FOLDER_PATHS);
			 lPreparedStatement.setString(1,pRunId);			 
			 lResultSet = lPreparedStatement.executeQuery();
		 }
		 
		String lDirSplitter = ToolsUtil.readProperty("dirSplitter");
		
		 if(lResultSet != null){
			 while (lResultSet.next()) {
				 //C:\Praveen\Temp_Files\tmpFile_0.sql
				 
				 //copy all the files together into one folder
				 
				 FileUtils.copyFile(new File(ToolsUtil.replaceNull(lResultSet.getString("FOLDER_PATH")).trim()+lDirSplitter.trim()+ ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")).trim()), new File(pSpInputCopiedPath.trim()+ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME")).trim()));
				 
				 
				 if("".equals(lConcatenatedFileNames)){
					 lConcatenatedFileNames += ToolsUtil.replaceNull(lResultSet.getString("FOLDER_PATH"))+lDirSplitter+ ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME"));
				 }else{
					 lConcatenatedFileNames +=","+ ToolsUtil.replaceNull(lResultSet.getString("FOLDER_PATH"))+lDirSplitter+ ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME"));
				 }
				 
				 
			}
		 }
		 }catch (SQLException e) {
			e.printStackTrace();
			logger.info(":::::error in getSPSourceLocationDetail ::::"+e.getMessage());
		}catch (Exception e) {			
			e.printStackTrace();
			logger.info(":::::error in getSPSourceLocationDetail ::::"+e.getMessage());
		}finally{
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			DBConnectionManager.closeConnection(lConnection);
		}
		 return lConcatenatedFileNames;
	 }
	 
	 public void createSqlWaysInputFile(String pSeqNo,String pProjectId,String pMigrationMode){
		 String pRunId =pProjectId+"_SOURCE";
		 
		 //all sp that are getting copied will be copied to this path
		 String lSpInputCopiedPath=ToolsUtil.readProperty("sqlWaysSPConversionInput")+"\\"+pProjectId+"\\"+pSeqNo+"\\";
		 
		 String lRetValue =  getSPSourceLocationDetail(pRunId,pSeqNo,pMigrationMode,lSpInputCopiedPath);
		logger.debug(":::file name list:::"+lRetValue);
		 String pOutputDir =ToolsUtil.readProperty("sqlWaysSPConversionFileList");
		 String fileName="SP_LIST_FILE_"+pProjectId+"_"+pSeqNo+".sql";
		 String lFileCreated= pOutputDir+fileName;
		 ToolsUtil.writeToFile(lRetValue, lFileCreated, pOutputDir);
		 //sqlWaysSPConversionInput
		 String lConvertedSPLocation =ToolsUtil.readProperty("sqlWaysSPConversionOutput")+"\\"+pProjectId+"\\"+pSeqNo+"\\";
		 SQLWaysConnectionUtil.runSqlWays(lFileCreated, lConvertedSPLocation,"batch");
		 
		// ProjectDetailsMainDTO lProjectDetailsMainDTO = lProjectCreationDAO.getProjectDetails(pProjectId);
		 ToolsUtil.removeSqlWaysLogs(pProjectId,null ,lConvertedSPLocation);
		 
		 invokeSequentialFunctions( pProjectId,lSpInputCopiedPath,lConvertedSPLocation,pMigrationMode);
	 }
	 
	 public void invokeSequentialFunctions(String pProjectId,String pSourceFilePath,String pTargetFilePath,String pMigrationMode){
		 Connection lConnection= null;
		 try {
			FileUploadDAO lFileUploadDAO = new FileUploadDAO();
			 SequentialRunning lSequentialRunning = new SequentialRunning();
				lFileUploadDAO.deleteExistingProjectData(pSourceFilePath,pProjectId);
				
				//calling functuion for the showing status
				lConnection=DBConnectionManager.getConnection();
				lConnection.setAutoCommit(false);
				if(pMigrationMode.equalsIgnoreCase("SYSBASE_TO_DB2")){
				//folder run seq sent but not yet added to methods
				lSequentialRunning.callSequentialFunctions(pSourceFilePath,pTargetFilePath,WebConstant.DB_MIGRATION_TYPE_SYBASE_TO_DB2,pProjectId,"TCS USER","BOTH",null,lConnection);
				}
				if(pMigrationMode.equalsIgnoreCase("SYSBASE_TO_Oracle")){
					//folder run seq sent but not yet added to methods
				lSequentialRunning.callSequentialFunctions(pSourceFilePath,pTargetFilePath,WebConstant.DB_MIGRATION_TYPE_SYBASE_TO_Oracle,pProjectId,"TCS USER","BOTH",null,lConnection);
				}
				//call things for process
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 public String getConcatFileNames(String pSourceFilePath ){
		 StoredProceduresDetailsDTO lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
			List lSourceFileList = ToolsUtil.getFileNamesFromFolderDTO(new File(pSourceFilePath), new ArrayList());
			String lConcatFileNames="";
			
			if(lSourceFileList != null && lSourceFileList.size() >0){
				for (int i = 0; i < lSourceFileList.size(); i++) {
					lStoredProceduresDetailsDTO = (StoredProceduresDetailsDTO)lSourceFileList.get(i);
					if(i>0) lConcatFileNames+=" , ";
					lConcatFileNames += lStoredProceduresDetailsDTO.getFolderPath()+"\\"+lStoredProceduresDetailsDTO.getProcName(); 
				}
			}
			
			return lConcatFileNames;
			
	 }
	 
	 
	 public String createMigratedTargetZip(String pSeqNo,String pProjectId,String pMigrationMode){
		 String lRetPath="";
		 try {
			String pRunId =pProjectId+"_TARGET";
			 
			 //all sp that are getting copied will be copied to this path
			 String lSpInputCopiedPath=ToolsUtil.readProperty("sqlWaysSPConversionInputDownload")+"\\"+pProjectId+"\\"+pSeqNo+"\\";
			 
			 String lRetValue =  getSPSourceLocationDetail(pRunId,pSeqNo,pMigrationMode,lSpInputCopiedPath);
			 String lZippedOutputFile = lSpInputCopiedPath.substring(0, lSpInputCopiedPath.length()-2)+".zip";
			 ZipUtilRecursive.createZipFile(lSpInputCopiedPath, lZippedOutputFile);
			 lRetPath =lZippedOutputFile; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 return lRetPath;
	
	 }
	 public static void main(String args[]){
		 InvokePrimaryToolDAO lInvokePrimaryToolDAO = new InvokePrimaryToolDAO();
		 String pRunId ="PRID71_SOURCE";
		 String lReturnStr = lInvokePrimaryToolDAO.getSPSourceLocationDetail(pRunId,"","","");
		 System.out.println("::::lReturnStr:::::"+lReturnStr);
	 }
	 
}
