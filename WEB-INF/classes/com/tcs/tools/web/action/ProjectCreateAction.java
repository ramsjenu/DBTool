package com.tcs.tools.web.action;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.web.dao.ProjectCreateDAO;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.util.ToolsUtil;
import com.tcs.tools.web.util.UnZip;

public class ProjectCreateAction extends ActionSupport /*implements ServletRequestAware,ServletResponseAware */ {
	// private static final long serialVersionUID = 1L;   
	// private HttpServletRequest request;
	//  private HttpServletResponse response;
	  ProjectModifyDAO lProjectModifyDAO = new ProjectModifyDAO();
	  ProjectCreateDAO lProjectCreateDAO = new ProjectCreateDAO();
		private static Logger logger = Logger.getLogger("ToolLogger");

	private String submitMode = "";
	private String msgToJsp = "";
	private String errorMsgToJsp = "";
	private StringBuffer msgValue= new StringBuffer(); 
	private String customerName = "";
	private String applicationName = "";
	private String sourceDBType = "";
	private String sourceDBTypeVersion = "";
	private String targetDBType = "";
	private String targetDBTypeVersion = "";
	private String projectName = "";
	String pCreatedBy ="Pru_user1";
	private String projectId ="";
	
	
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
	
	
	private String targetDbIp  ="";
	private String targetDbPort  ="";
	private String targetDbSchemaName  ="";
	private String targetDbName  ="";
	private String targetDbUserName  ="";
	private String targetDbPassword  ="";
	private String targetUnixUserName  ="";
	private String targetUnixPassword  ="";
	
	
	
	
	
	@SuppressWarnings("deprecation")
	public String execute() throws Exception {
	     System.out.println(":::::inside project creation actionL:::::"+getSubmitMode());
	     String lProjectId = "";
	     
	     if("reset".equalsIgnoreCase(getSubmitMode())){
	    	 setCustomerName("");
	    	 setApplicationName("");
	    	 setSourceDBType("");
	    	 setTargetDBType("");
	    	 setProjectName("");
	    	 setTargetDBTypeVersion("");
	    	 setSourceDBTypeVersion("");
	    	 return SUCCESS;
	     }
	     
	     if("save".equalsIgnoreCase(getSubmitMode())){
	    	 System.out.println("::::inside save mode");
	    	 
	    	 
	    	 //check if the project name exists
	    	  boolean lIsProjectExist = lProjectCreateDAO.checkProjectExists( getProjectName() );
	    	  System.out.println(":::::lIsProjectExist::::"+lIsProjectExist+"::::proj name::::"+getProjectName());
	    	  if(lIsProjectExist == true){
	    		  setErrorMsgToJsp("Project already Exists");
	    		  return SUCCESS;
	    	  }
	    	  String pName=getProjectName();
	    	  int pNameLen=pName.length();
	    	  if(pNameLen<=3){
	    		  setErrorMsgToJsp("Project Name must contain atleast 4 characters");
	    	  }
	    	  else{
	    	  lProjectId = lProjectCreateDAO.createProjectId();
		    	 System.out.println("::::created project id::::"+lProjectId);
		    	 setProjectId(lProjectId);
		    	 lProjectModifyDAO.insertRunSeq(lProjectId);	
	    		  //lProjectModifyDAO.updateProjectDetails(lProjectId,"","" ,getSourceDBType(),getTargetDBType(),pCreatedBy,getProjectName(),getCustomerName(),getApplicationName(),getSourceDBTypeVersion(),getTargetDBTypeVersion(),"Data Uploading","0", getSourceDbIp(),getSourceDbPort(),getSourceDbSchemaName(),getSourceDbUserName(),getSourceDbPassword(),getSourceUnixIp(),getSourceUnixUserName(),getSourceUnixPassword());
	    		  
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
	  			pProjectDetailsMap.put("TARGET_UNIX_IP", getTargetDbIp());
	  			pProjectDetailsMap.put("TARGET_UNIX_PASSWORD", getTargetUnixPassword());
	  			pProjectDetailsMap.put("TARGET_UNIX_USER_NAME", getTargetUnixUserName());
	  			pProjectDetailsMap.put("TARGET_DB_NAME", getTargetDbName());
	  			pProjectDetailsMap.put("PROJECT_NAME", getProjectName());
	  			
	  			logger.debug("::::TARGET_DB_VERSION::::;"+pProjectDetailsMap.get("TARGET_DB_VERSION"));
	  			
	  			
	  			int pCreationChk=lProjectModifyDAO.insertProjectSPUploadDetails(lProjectId,pProjectDetailsMap) ;
	  			if(pCreationChk>0)
				{
				//for file type validation - end
					msgValue.append("Project created on ");
					msgValue.append(ToolsUtil.getDateTime());
					setMsgToJsp(msgValue.toString());	
					
				}
				else
				{
					msgValue.append("Unable to create project. ");
					msgValue.append("Please check database connection");
					setErrorMsgToJsp(msgValue.toString());	
				}
	    	  }
	     }
		 
	        return SUCCESS;
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

	public void validate() {		
		/*if (getTargetDBType().length() == 0) {			       
			addFieldError("targetDBType", "User Name is required");
		} */
	}

	/**************getter setters for form******************/
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
	 * @return the projectId
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
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

	public String getTargetDbName() {
		return targetDbName;
	}

	public void setTargetDbName(String targetDbName) {
		this.targetDbName = targetDbName;
	}
	
		
	    

	    
	    
	    

}
