package com.tcs.tools.web.action;

import java.io.File;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.business.analysis.main.SequentialRunning;
import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.business.fileupload.dao.FileUploadDAO;
import com.tcs.tools.web.dao.InvokePrimaryToolDAO;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.dto.ProjectDetailsMainDTO;
import com.tcs.tools.web.dto.StoredProceduresDetailsDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.SQLWaysConnectionUtil;
import com.tcs.tools.web.util.ToolsUtil;
import com.tcs.tools.web.util.UnZip;

public class ProjectModifySPUploadAction extends ActionSupport /*implements ServletRequestAware,ServletResponseAware */ {

	private static Logger logger = Logger.getLogger("ToolLogger");
	
	//url
	//http://localhost:8080/WebProject/tool/index.action
	private String retValue=""; 
	private StringBuffer msgValue= new StringBuffer(); 
	UnZip lUnZip = new UnZip(); 
	ProjectModifyDAO lProjectCreationDAO = new ProjectModifyDAO();
	InvokePrimaryToolDAO lInvokePrimaryToolDAO = new InvokePrimaryToolDAO();
	String pCreatedBy ="Pru_user1";
	
	private String projectName ="";
	private String sourceDBType ="";
	private String targetDBType ="";
	private File sourceSPUpFile;
	private File targetSPUpFile;
	private String sourceSPUpFileFileName;
	private String targetSPUpFileFileName;
	private String sourceSPUpFileContentType;
	private String targetSPUpFileContentType;
	private String msgToJsp="";
	private String errorMsgToJsp="";
	private String uploadErrorMsgToJsp="";
	private String customerName = "";
	private String applicationName = "";
	private String sourceDBTypeVersion = "";
	private String targetDBTypeVersion = "";
	private String submitMode= "";
	private ProjectDetailsMainDTO projectDetailsMainDTO = null;
	private HashMap projectNameIdMap = null;
	private String paramProjectName = "";
	
	//for calling sql ways
	private String triggerSqlWays="";
	
	
	
	//FOR SOURCE DB CONNECTION ESTABLISHING
		private String sourceDbIp ="";
		private String sourceDbPort ="";
		private String sourceDbSchemaName ="";
		private String sourceDbUserName ="";
		private String sourceDbPassword ="";
		
		//for unix connection
		private String sourceUnixIp  ="";
		private String sourceUnixUserName  ="";
		private String sourceUnixPassword  ="";
	

	public String execute() throws Exception {
	System.out.println(":::modify project action inside projectmodifyspuploadaction::::");	
	System.out.println("::::::form value:::::-projectId->"+getProjectName());
	System.out.println("::::::form value:::::-sourceDBType->"+getSourceDBType());
	System.out.println("::::::form value:::::-targetDBType->"+getTargetDBType());
	System.out.println("::::::form value:::::-sourceSPUpFileFileName->"+getSourceSPUpFileFileName());
	System.out.println("::::::form value:::::-targetSPUpFileFileName->"+getTargetSPUpFileFileName());
	System.out.println("::::::form value:::::-sourceSPUpFileContentType->"+getSourceSPUpFileContentType());
	System.out.println("::::::form value:::::-targetSPUpFileContentType->"+getTargetSPUpFileContentType());
	System.out.println("::::::form value:::::-getParamProjectId->"+getParamProjectName());
	String lSourceFilePath="";
	String lTargetFilePath="";	
	if(getParamProjectName() != null && (!"".equals(paramProjectName))){
		setProjectName(getParamProjectName());
	}
	
	//populating data for the project drop down - start
	HashMap lProjectNameIdMap = lProjectCreationDAO.getProjectDetails(); 
	setProjectNameIdMap(lProjectNameIdMap);
	//populating data for the project drop down - end
	
	if("save".equalsIgnoreCase(getSubmitMode())){
		
		System.out.println(":::inside savemode:::"+getTriggerSqlWays());
		
		//temp condition for sybase to sql server - start
		/*if("SQL_SERVER".equalsIgnoreCase(getTargetDBType())){
			
			setTargetSPUpFile(getSourceSPUpFile());
			setTargetSPUpFileFileName(getSourceSPUpFileFileName());
			setTargetSPUpFileContentType(getSourceSPUpFileContentType());
		}*/
			
		//temp condition for sybase to sql server - end
		
		
		String lRunSeq = lProjectCreationDAO.getRunSeq(getProjectName());
			//for file type validation - start
			if(getSourceSPUpFileFileName() != null && (!"".equals(getSourceSPUpFileFileName()))){
				//validate for file extension
				if (FileUploadDownloadUtility.validateFile(
						getSourceSPUpFileFileName(), ".zip")
						.equalsIgnoreCase("invalid_format")
						&& FileUploadDownloadUtility.validateFile(
								getSourceSPUpFileFileName(), ".ZIP")
								.equalsIgnoreCase("invalid_format")) {
					System.out.println(":::invalid src file type:::::");
					msgValue.append("Invalid Source file Type");
					
					retValue = "error";
				}else{
					//copy file to server path		
					String fullFileName =ToolsUtil.readProperty("fileUploadPath")+""+getProjectName()+"\\SP\\Source\\" +getProjectName()+"_"+lRunSeq+".zip";
					System.out.println("::::::fullFileName Source SP::::::"+fullFileName);
					File theFile = new File(fullFileName);
					FileUtils.copyFile(getSourceSPUpFile(), new File(fullFileName));
					lSourceFilePath = lUnZip.unzipFile( fullFileName);
				}
				
				
				
			}
			
			//for target
			if(getTargetSPUpFileFileName() != null && (!"".equals(getTargetSPUpFileFileName()))){
				if (FileUploadDownloadUtility.validateFile(
						getTargetSPUpFileFileName(), ".zip")
						.equalsIgnoreCase("invalid_format")
						&& FileUploadDownloadUtility.validateFile(
								getTargetSPUpFileFileName(), ".ZIP")
								.equalsIgnoreCase("invalid_format")) {
					System.out.println(":::invalid target file type:::::");
					
					msgValue.append("Invalid Target file Type");
					retValue = "error";
				}else{
					//copy file to server path		
					String fullFileName = ToolsUtil.readProperty("fileUploadPath")+""+getProjectName()+"\\SP\\Target\\" +getProjectName()+"_"+lRunSeq+".zip";
					System.out.println("::::::fullFileName Target SP::::::"+fullFileName);
					File theFile = new File(fullFileName);
					FileUtils.copyFile(getTargetSPUpFile(), new File(fullFileName));
					lTargetFilePath = lUnZip.unzipFile( fullFileName);
				}
			}
			
			if(getTargetSPUpFileFileName() != null && (!"".equals(getTargetSPUpFileFileName()))){
				String lValidateFiles = lProjectCreationDAO.validateUploadedFiles( lSourceFilePath, lTargetFilePath);
				System.out.println("::::lValidateFiles:::::"+lValidateFiles);
				if(!"".equals(lValidateFiles)){
					setErrorMsgToJsp(lValidateFiles.toString());
					setUploadErrorMsgToJsp("Error in upload process");
					return "error";
				}
			}
			HashMap pProjectDetailsMap = new HashMap();
			pProjectDetailsMap.put("SOURCE_PATH", lSourceFilePath);
			pProjectDetailsMap.put("TARGET_PATH", lTargetFilePath);
			pProjectDetailsMap.put("CREATED_BY", pCreatedBy);
			logger.info(":::::lSourceFilePath::::"+lSourceFilePath);
			logger.info(":::::lTargetFilePath::::"+lTargetFilePath);
			lProjectCreationDAO.updateProjectSPUploadDetails(getProjectName(),pProjectDetailsMap) ;
			//lProjectCreationDAO.updateProjectDetails(getProjectName(),lSourceFilePath,lTargetFilePath ,getSourceDBType(),getTargetDBType(),pCreatedBy,null,getCustomerName(),getApplicationName(),getSourceDBTypeVersion(),getTargetDBTypeVersion(),"Data Uploading",lRunSeq , getSourceDbIp(),getSourceDbPort(),getSourceDbSchemaName(),getSourceDbUserName(),getSourceDbPassword(),/*getSourceUnixIp() //commented as dp ip and unix ip are same*/getSourceDbIp(),getSourceUnixUserName(),getSourceUnixPassword() );
			
			if(getSourceSPUpFileFileName() != null && (!"".equals(getSourceSPUpFileFileName()))){
				lProjectCreationDAO.insertSPDetails(getProjectName(),"SOURCE",lSourceFilePath,pCreatedBy, lRunSeq);
			}
			
			if(getTargetSPUpFileFileName() != null && (!"".equals(getTargetSPUpFileFileName()))){
				lProjectCreationDAO.insertSPDetails(getProjectName(),"TARGET",lTargetFilePath,pCreatedBy, lRunSeq);
			}
			
			FileUploadDAO lFileUploadDAO = new FileUploadDAO();
			lFileUploadDAO.deleteExistingProjectData(lSourceFilePath,getProjectName());
			
			//call things for process
			
			//get the project specific details
			ProjectDetailsMainDTO lProjectDetailsMainDTO = lProjectCreationDAO.getProjectDetails(getProjectName());
			setTargetDBType(lProjectDetailsMainDTO.getTargetDBType());
			
			SequentialRunning lSequentialRunning = new SequentialRunning();
			String lSourceDbType = "SYSBASE";
			
			String lDbMigrationType= "";
			

			 if("DB2".equalsIgnoreCase(getTargetDBType())) {
				 lDbMigrationType= "SYSBASE_TO_DB2";
				// JOptionPane.showMessageDialog(null,"1 "+lDbMigrationType);
			}
			 if("ORACLE".equalsIgnoreCase(getTargetDBType())) {
				 lDbMigrationType= "SYSBASE_TO_Oracle";
				//JOptionPane.showMessageDialog(null,"2 "+lDbMigrationType);
			}
			if("SQL_SERVER".equalsIgnoreCase(getTargetDBType())) {
					lDbMigrationType="SYBASE_TO_SQL";
				//	JOptionPane.showMessageDialog(null,"3 "+lDbMigrationType);
				}
			
			String pAnalysysMode ="";
			
			if(getTargetSPUpFileFileName() != null && (!"".equals(getTargetSPUpFileFileName()))){
				pAnalysysMode +="_TARGET";
			}
			if(getSourceSPUpFileFileName() != null && (!"".equals(getSourceSPUpFileFileName()))){
				pAnalysysMode +="_SOURCE";
			}
			
			if( (pAnalysysMode.contains("SOURCE")) && (pAnalysysMode.contains("TARGET") ) ){
				pAnalysysMode ="BOTH";
			}else if( pAnalysysMode.contains("SOURCE") ){
				pAnalysysMode ="SOURCE";
			}else if( pAnalysysMode.contains("TARGET") ){
				pAnalysysMode ="TARGET";
			}
			
			/*if(getTargetSPUpFileFileName() != null && (!"".equals(getTargetSPUpFileFileName()))){
				pAnalysysMode ="BOTH";
			}*/
			
			//Update Status to Front End - Start
			String lCurState="Started";
			String lStausMsg="Intializing the upload process...";
			Connection lConnection=DBConnectionManager.getConnection();
			lConnection.setAutoCommit(false);
			ToolsUtil.prepareInsertStatusMsg( getProjectName(), ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
			//Update Status to Front End - End
			
			//if triggerSqlWays== yes then call primary tool for conversion		
			if(getSourceSPUpFileFileName() != null && (!"".equals(getSourceSPUpFileFileName()))){
				if("yes".equalsIgnoreCase(getTriggerSqlWays())){
					//Update Status to Front End - Start
					 lCurState="Primary Tool-SP Conversion";
					 lStausMsg="Invoking SQLWAYS...";
					ToolsUtil.prepareInsertStatusMsg( getProjectName(), ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
					 lCurState="Primary Tool-SP Conversion";
					 lStausMsg="Stored Procs getting converted...";
					ToolsUtil.prepareInsertStatusMsg( getProjectName(), ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
					//Update Status to Front End - End
					logger.info("::::inside yes mode of calling sql ways::::");
					String lConcatFileNames = lInvokePrimaryToolDAO.getConcatFileNames(lSourceFilePath) ;
					logger.info("::::inside yes mode of calling sql ways-lConcatFileNames111::::"+lConcatFileNames);	
					String pOutputDir =ToolsUtil.readProperty("sqlWaysSPConversionFileList");
					String fileName="SP_LIST_FILE_"+getProjectName()+"_"+lRunSeq+".sql";
					String lFileCreated= pOutputDir+fileName;
					ToolsUtil.writeToFile(lConcatFileNames, lFileCreated, pOutputDir);
					
					String lSqlWaysOutputFilePath = ToolsUtil.readProperty("fileUploadPath")+""+getProjectName()+"\\SP\\Target\\Unzipped\\" +getProjectName()+"_"+lRunSeq+"\\";
					logger.info(":::::sqlways output path:::::"+lSqlWaysOutputFilePath );
					SQLWaysConnectionUtil.runSqlWays(lFileCreated, lSqlWaysOutputFilePath , "batch" );
					pAnalysysMode ="BOTH";
					lTargetFilePath = lSqlWaysOutputFilePath;
					
					ToolsUtil.removeSqlWaysLogs(getProjectName(),lSourceFilePath ,lTargetFilePath);
					
					//Update Status to Front End - Start
					 lCurState="Primary Tool SP Conversion";
					 lStausMsg="SQLWAYS SP Migration Completed Successfully...";
					ToolsUtil.prepareInsertStatusMsg( getProjectName(), ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
					//Update Status to Front End - End
				}
			}
				
			logger.info(":::lSourceFilePath::::"+lSourceFilePath);
			logger.info(":::lTargetFilePath::::"+lTargetFilePath);
			
			
			
			
			
			
			//folder run seq sent but not yet added to methods
			lSequentialRunning.callSequentialFunctions(lSourceFilePath,lTargetFilePath,lDbMigrationType,getProjectName(),pCreatedBy,pAnalysysMode,lRunSeq,lConnection);			
			//call things for process
			
			
			
			  
			  
			//Update Status to Front End - Start
			lCurState="Completed";
			lStausMsg="Process Completed";			
			ToolsUtil.prepareInsertStatusMsg( getProjectName(), ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
			lConnection.commit();
			lConnection.setAutoCommit(true);
			lConnection.close();
			//Update Status to Front End - End
			
			msgValue.append("Stored Procedures uploaded on ");
			msgValue.append(ToolsUtil.getDateTime());
			setMsgToJsp(msgValue.toString());	
			//for file type validation - end
				
			}
	

	 if("reset".equalsIgnoreCase(getSubmitMode())){
   	 setCustomerName("");
   	 setApplicationName("");
   	 setSourceDBType("");
   	 setTargetDBType("");
   	 setProjectName("");
   	 setTargetDBTypeVersion("");
   	 setSourceDBTypeVersion("");
   	// return SUCCESS;
    }
		
		 lProjectNameIdMap = lProjectCreationDAO.getProjectDetails(); 
		setProjectNameIdMap(lProjectNameIdMap);
		/*if("".equals(getProjectName())){
			if(lProjectNameIdMap.size() > 0){
				setProjectName((String)((Map.Entry)(lProjectNameIdMap.entrySet().iterator().next())).getKey());
				
			}
		}*/
		//if(!"".equals(getProjectName())){
			ProjectDetailsMainDTO lProjectDetailsMainDTO = lProjectCreationDAO.getProjectDetails(getProjectName());
			setProjectDetailsMainDTO(lProjectDetailsMainDTO);
		//}
		
		
		if(!"".equals(retValue)){
			return retValue;
		}

        // do the work
        return SUCCESS;
   }

	/***************form getter setter elemements***************/
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the sourceDBType
	 */
	public String getSourceDBType() {
		return sourceDBType;
	}

	/**
	 * @param sourceDBType the sourceDBType to set
	 */
	public void setSourceDBType(String sourceDBType) {
		this.sourceDBType = sourceDBType;
	}

	/**
	 * @return the targetDBType
	 */
	public String getTargetDBType() {
		return targetDBType;
	}

	/**
	 * @param targetDBType the targetDBType to set
	 */
	public void setTargetDBType(String targetDBType) {
		this.targetDBType = targetDBType;
	}

	/**
	 * @return the sourceSPUpFile
	 */
	public File getSourceSPUpFile() {
		return sourceSPUpFile;
	}

	/**
	 * @param sourceSPUpFile the sourceSPUpFile to set
	 */
	public void setSourceSPUpFile(File sourceSPUpFile) {
		this.sourceSPUpFile = sourceSPUpFile;
	}

	/**
	 * @return the targetSPUpFile
	 */
	public File getTargetSPUpFile() {
		return targetSPUpFile;
	}

	/**
	 * @param targetSPUpFile the targetSPUpFile to set
	 */
	public void setTargetSPUpFile(File targetSPUpFile) {
		this.targetSPUpFile = targetSPUpFile;
	}

	/**
	 * @return the sourceSPUpFileFileName
	 */
	public String getSourceSPUpFileFileName() {
		return sourceSPUpFileFileName;
	}

	/**
	 * @param sourceSPUpFileFileName the sourceSPUpFileFileName to set
	 */
	public void setSourceSPUpFileFileName(String sourceSPUpFileFileName) {
		this.sourceSPUpFileFileName = sourceSPUpFileFileName;
	}

	/**
	 * @return the targetSPUpFileFileName
	 */
	public String getTargetSPUpFileFileName() {
		return targetSPUpFileFileName;
	}

	/**
	 * @param targetSPUpFileFileName the targetSPUpFileFileName to set
	 */
	public void setTargetSPUpFileFileName(String targetSPUpFileFileName) {
		this.targetSPUpFileFileName = targetSPUpFileFileName;
	}

	/**
	 * @return the sourceSPUpFileContentType
	 */
	public String getSourceSPUpFileContentType() {
		return sourceSPUpFileContentType;
	}

	/**
	 * @param sourceSPUpFileContentType the sourceSPUpFileContentType to set
	 */
	public void setSourceSPUpFileContentType(String sourceSPUpFileContentType) {
		this.sourceSPUpFileContentType = sourceSPUpFileContentType;
	}

	/**
	 * @return the targetSPUpFileContentType
	 */
	public String getTargetSPUpFileContentType() {
		return targetSPUpFileContentType;
	}

	/**
	 * @param targetSPUpFileContentType the targetSPUpFileContentType to set
	 */
	public void setTargetSPUpFileContentType(String targetSPUpFileContentType) {
		this.targetSPUpFileContentType = targetSPUpFileContentType;
	}

	/**
	 * @return the msgToJsp
	 */
	public String getMsgToJsp() {
		return msgToJsp;
	}

	/**
	 * @param msgToJsp the msgToJsp to set
	 */
	public void setMsgToJsp(String msgToJsp) {
		this.msgToJsp = msgToJsp;
	}
	
	/**
	 * @return the errorMsgToJsp
	 */
	public String getErrorMsgToJsp() {
		return errorMsgToJsp;
	}

	/**
	 * @param errorMsgToJsp the errorMsgToJsp to set
	 */
	public void setErrorMsgToJsp(String errorMsgToJsp) {
		this.errorMsgToJsp = errorMsgToJsp;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @return the sourceDBTypeVersion
	 */
	public String getSourceDBTypeVersion() {
		return sourceDBTypeVersion;
	}

	/**
	 * @param sourceDBTypeVersion the sourceDBTypeVersion to set
	 */
	public void setSourceDBTypeVersion(String sourceDBTypeVersion) {
		this.sourceDBTypeVersion = sourceDBTypeVersion;
	}

	/**
	 * @return the targetDBTypeVersion
	 */
	public String getTargetDBTypeVersion() {
		return targetDBTypeVersion;
	}

	/**
	 * @param targetDBTypeVersion the targetDBTypeVersion to set
	 */
	public void setTargetDBTypeVersion(String targetDBTypeVersion) {
		this.targetDBTypeVersion = targetDBTypeVersion;
	}

	/**
	 * @return the submitMode
	 */
	public String getSubmitMode() {
		return submitMode;
	}

	/**
	 * @param submitMode the submitMode to set
	 */
	public void setSubmitMode(String submitMode) {
		this.submitMode = submitMode;
	}

	/**
	 * @return the projectDetailsMainDTO
	 */
	public ProjectDetailsMainDTO getProjectDetailsMainDTO() {
		return projectDetailsMainDTO;
	}

	/**
	 * @param projectDetailsMainDTO the projectDetailsMainDTO to set
	 */
	public void setProjectDetailsMainDTO(ProjectDetailsMainDTO projectDetailsMainDTO) {
		this.projectDetailsMainDTO = projectDetailsMainDTO;
	}

	/**
	 * @return the projectNameIdMap
	 */
	public HashMap getProjectNameIdMap() {
		return projectNameIdMap;
	}

	/**
	 * @param projectNameIdMap the projectNameIdMap to set
	 */
	public void setProjectNameIdMap(HashMap projectNameIdMap) {
		this.projectNameIdMap = projectNameIdMap;
	}

	/**
	 * @return the paramProjectName
	 */
	public String getParamProjectName() {
		return paramProjectName;
	}

	/**
	 * @param paramProjectName the paramProjectName to set
	 */
	public void setParamProjectName(String paramProjectName) {
		this.paramProjectName = paramProjectName;
	}

	/**
	 * @return the sourceDbIp
	 */
	public String getSourceDbIp() {
		return sourceDbIp;
	}

	/**
	 * @param sourceDbIp the sourceDbIp to set
	 */
	public void setSourceDbIp(String sourceDbIp) {
		this.sourceDbIp = sourceDbIp;
	}

	/**
	 * @return the sourceDbPort
	 */
	public String getSourceDbPort() {
		return sourceDbPort;
	}

	/**
	 * @param sourceDbPort the sourceDbPort to set
	 */
	public void setSourceDbPort(String sourceDbPort) {
		this.sourceDbPort = sourceDbPort;
	}

	/**
	 * @return the sourceDbSchemaName
	 */
	public String getSourceDbSchemaName() {
		return sourceDbSchemaName;
	}

	/**
	 * @param sourceDbSchemaName the sourceDbSchemaName to set
	 */
	public void setSourceDbSchemaName(String sourceDbSchemaName) {
		this.sourceDbSchemaName = sourceDbSchemaName;
	}

	/**
	 * @return the sourceDbUserName
	 */
	public String getSourceDbUserName() {
		return sourceDbUserName;
	}

	/**
	 * @param sourceDbUserName the sourceDbUserName to set
	 */
	public void setSourceDbUserName(String sourceDbUserName) {
		this.sourceDbUserName = sourceDbUserName;
	}

	/**
	 * @return the sourceDbPassword
	 */
	public String getSourceDbPassword() {
		return sourceDbPassword;
	}

	/**
	 * @param sourceDbPassword the sourceDbPassword to set
	 */
	public void setSourceDbPassword(String sourceDbPassword) {
		this.sourceDbPassword = sourceDbPassword;
	}

	/**
	 * @return the triggerSqlWays
	 */
	public String getTriggerSqlWays() {
		return triggerSqlWays;
	}

	/**
	 * @param triggerSqlWays the triggerSqlWays to set
	 */
	public void setTriggerSqlWays(String triggerSqlWays) {
		this.triggerSqlWays = triggerSqlWays;
	}

	/**
	 * @return the sourceUnixIp
	 */
	public String getSourceUnixIp() {
		return sourceUnixIp;
	}

	/**
	 * @param sourceUnixIp the sourceUnixIp to set
	 */
	public void setSourceUnixIp(String sourceUnixIp) {
		this.sourceUnixIp = sourceUnixIp;
	}

	/**
	 * @return the sourceUnixUserName
	 */
	public String getSourceUnixUserName() {
		return sourceUnixUserName;
	}

	/**
	 * @param sourceUnixUserName the sourceUnixUserName to set
	 */
	public void setSourceUnixUserName(String sourceUnixUserName) {
		this.sourceUnixUserName = sourceUnixUserName;
	}

	/**
	 * @return the sourceUnixPassword
	 */
	public String getSourceUnixPassword() {
		return sourceUnixPassword;
	}

	/**
	 * @param sourceUnixPassword the sourceUnixPassword to set
	 */
	public void setSourceUnixPassword(String sourceUnixPassword) {
		this.sourceUnixPassword = sourceUnixPassword;
	}

	public String getUploadErrorMsgToJsp() {
		return uploadErrorMsgToJsp;
	}

	public void setUploadErrorMsgToJsp(String uploadErrorMsgToJsp) {
		this.uploadErrorMsgToJsp = uploadErrorMsgToJsp;
	}

}
