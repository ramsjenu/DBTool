package com.tcs.tools.web.action;

import java.io.File;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class ProjectModifyDetailsAction extends ActionSupport /*implements ServletRequestAware,ServletResponseAware */ {

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
		private String sourceDbName ="";
		private String sourceDbUserName ="";
		private String sourceDbPassword ="";
		
		//for unix connection
		private String sourceUnixIp  ="";
		private String sourceUnixUserName  ="";
		private String sourceUnixPassword  ="";
		
		private String targetDbIp  ="";
		private String targetDbPort  ="";
		private String targetDbSchemaName  ="";
		private String targetDbName  ="";
		private String targetDbUserName  ="";
		private String targetDbPassword  ="";
		private String targetUnixUserName  ="";
		private String targetUnixPassword  ="";
		
	

	public String execute() throws Exception {
	System.out.println(":::modify project action11111111111::::");	
	System.out.println("::::::form value:::::-projectName->"+getProjectName());
	System.out.println("::::::form value:::::-sourceDBType->"+getSourceDBType());
	System.out.println("::::::form value:::::-targetDBType->"+getTargetDBType());
	System.out.println("::::::form value:::::-sourceSPUpFileFileName->"+getSourceSPUpFileFileName());
	System.out.println("::::::form value:::::-targetSPUpFileFileName->"+getTargetSPUpFileFileName());
	System.out.println("::::::form value:::::-sourceSPUpFileContentType->"+getSourceSPUpFileContentType());
	System.out.println("::::::form value:::::-targetSPUpFileContentType->"+getTargetSPUpFileContentType());
	System.out.println("::::::form value:::::-getParamProjectName->"+getParamProjectName());
	
	
	if(getParamProjectName() != null && (!"".equals(paramProjectName))){
		setProjectName(getParamProjectName());
	}
	
	
	if("saveProjDetails".equalsIgnoreCase(getSubmitMode())){
		
		//System.out.println(":::inside savemode:::"+getTriggerSqlWays());
		
		//temp condition for sybase to sql server - start
		/*if("SQL_SERVER".equalsIgnoreCase(getTargetDBType())){
			
			setTargetSPUpFile(getSourceSPUpFile());
			setTargetSPUpFileFileName(getSourceSPUpFileFileName());
			setTargetSPUpFileContentType(getSourceSPUpFileContentType());
		}*/
			
		//temp condition for sybase to sql server - end
		
		
		//String lRunSeq = lProjectCreationDAO.getRunSeq(getProjectName());
			//for file type validation - start
			
			
			
			
		
			
			//lProjectCreationDAO.updateProjectDetails(getProjectName(),/*lSourceFilePath*/ null,/*lTargetFilePath*/ null ,getSourceDBType(),getTargetDBType(),pCreatedBy,null,getCustomerName(),getApplicationName(),getSourceDBTypeVersion(),getTargetDBTypeVersion(),"Data Uploading",/*lRunSeq*/null , getSourceDbIp(),getSourceDbPort(),getSourceDbSchemaName(),getSourceDbUserName(),getSourceDbPassword(),/*getSourceUnixIp() //commented as dp ip and unix ip are same*/getSourceDbIp(),getSourceUnixUserName(),getSourceUnixPassword() );
			
			HashMap pProjectDetailsMap = new HashMap();
			pProjectDetailsMap.put("CREATED_BY", pCreatedBy);
			pProjectDetailsMap.put("CUSTOMER_NAME", getCustomerName());			
			pProjectDetailsMap.put("APPLICATION_NAME", getApplicationName());
			pProjectDetailsMap.put("SOURCE_DB_VERSION", getSourceDBTypeVersion());
			pProjectDetailsMap.put("TARGET_DB_VERSION", getTargetDBTypeVersion());
			pProjectDetailsMap.put("DATA_UPLOAD_STATUS", "Data Upload Complete");
			pProjectDetailsMap.put("SOURCE_DB_HOST_IP", getSourceDbIp());
			pProjectDetailsMap.put("SOURCE_DB_HOST_PORT", getSourceDbPort());
			pProjectDetailsMap.put("SOURCE_DB_SCHEMA_NAME", getSourceDbSchemaName());
			pProjectDetailsMap.put("SOURCE_DB_USER_NAME", getSourceDbUserName());
			pProjectDetailsMap.put("SOURCE_DB_PASSWORD", getSourceDbPassword());
			pProjectDetailsMap.put("SOURCE_UNIX_IP", getSourceUnixIp());
			pProjectDetailsMap.put("SOURCE_UNIX_USER_NAME", getSourceUnixUserName());
			pProjectDetailsMap.put("SOURCE_UNIX_PASSWORD", getSourceUnixPassword());
			pProjectDetailsMap.put("SOURCE_DB_TYPE", getSourceDBType());
			pProjectDetailsMap.put("TARGET_DB_TYPE", getTargetDBType());
			
			pProjectDetailsMap.put("TARGET_DB_HOST_IP", getTargetDbIp());
  			pProjectDetailsMap.put("TARGET_DB_HOST_PORT",getTargetDbPort() );
  			pProjectDetailsMap.put("TARGET_DB_SCHEMA_NAME", getTargetDbSchemaName());
  			pProjectDetailsMap.put("TARGET_DB_USER_NAME", getTargetDbUserName());
  			pProjectDetailsMap.put("TARGET_DB_PASSWORD", getTargetDbPassword());  			
  			pProjectDetailsMap.put("TARGET_UNIX_PASSWORD", getTargetUnixPassword());
  			pProjectDetailsMap.put("TARGET_UNIX_USER_NAME", getTargetUnixUserName());
  			pProjectDetailsMap.put("TARGET_DB_NAME", getTargetDbName());
			
			logger.debug("::::TARGET_DB_VERSION::::;"+pProjectDetailsMap.get("TARGET_DB_VERSION"));
			
			
		int updtchk=lProjectCreationDAO.updateProjectSPUploadDetails(getProjectName(),pProjectDetailsMap) ;
			
			
			if(updtchk>0)
			{
			//for file type validation - end
				msgValue.append("Project updated on ");
				msgValue.append(ToolsUtil.getDateTime());
				setMsgToJsp(msgValue.toString());		
				
			}
			else
			{
				msgValue.append("Unable to update project.");
				msgValue.append("Please check database connection");
				setErrorMsgToJsp(msgValue.toString());	
			}
				
				
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
		
		HashMap lProjectNameIdMap = lProjectCreationDAO.getProjectDetails(); 
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

	public String getSourceDbName() {
		return sourceDbName;
	}

	public void setSourceDbName(String sourceDbName) {
		this.sourceDbName = sourceDbName;
	}

	public String getTargetDbIp() {
		return targetDbIp;
	}

	public void setTargetDbIp(String targetDbIp) {
		this.targetDbIp = targetDbIp;
	}

	public String getTargetDbPort() {
		return targetDbPort;
	}

	public void setTargetDbPort(String targetDbPort) {
		this.targetDbPort = targetDbPort;
	}

	public String getTargetDbSchemaName() {
		return targetDbSchemaName;
	}

	public void setTargetDbSchemaName(String targetDbSchemaName) {
		this.targetDbSchemaName = targetDbSchemaName;
	}

	public String getTargetDbName() {
		return targetDbName;
	}

	public void setTargetDbName(String targetDbName) {
		this.targetDbName = targetDbName;
	}

	public String getTargetDbUserName() {
		return targetDbUserName;
	}

	public void setTargetDbUserName(String targetDbUserName) {
		this.targetDbUserName = targetDbUserName;
	}

	public String getTargetDbPassword() {
		return targetDbPassword;
	}

	public void setTargetDbPassword(String targetDbPassword) {
		this.targetDbPassword = targetDbPassword;
	}

	public String getTargetUnixUserName() {
		return targetUnixUserName;
	}

	public void setTargetUnixUserName(String targetUnixUserName) {
		this.targetUnixUserName = targetUnixUserName;
	}

	public String getTargetUnixPassword() {
		return targetUnixPassword;
	}

	public void setTargetUnixPassword(String targetUnixPassword) {
		this.targetUnixPassword = targetUnixPassword;
	}

}
