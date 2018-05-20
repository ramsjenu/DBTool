package com.tcs.tools.web.dto;

public class ProjectDetailsMainDTO {
	private String customerName = "";
	private String applicationName = "";
	private String sourceDBTypeVersion = "";
	private String targetDBTypeVersion = "";
	private String projectName ="";
	private String sourceDBType ="";
	private String targetDBType ="";
	private String projectId ="";
	
	private String sourceDbIp ="";
	private String sourceDbPort ="";
	private String sourceDbSchemaName ="";
	private String sourceDbUserName ="";
	private String sourceDbPassword ="";
	
	//for unix connection
	private String sourceUnixIp  ="";
	private String sourceUnixUserName  ="";
	private String sourceUnixPassword  ="";
	
	
	//for target db details
	private String targetDbIp ="";
	private String targetDbPort ="";
	private String targetDbSchemaName ="";
	private String targetDbUserName ="";
	private String targetDbPassword ="";
	private String targetUnixIp  ="";
	private String targetUnixUserName  ="";
	private String targetUnixPassword  ="";
	private String targetDbName ="";
	
	private String dbMigrationType="";
	
	
	
	public String getDbMigrationType() {
		return dbMigrationType;
	}
	public void setDbMigrationType(String dbMigrationType) {
		this.dbMigrationType = dbMigrationType;
	}
	public String getTargetDbName() {
		return targetDbName;
	}
	public void setTargetDbName(String targetDbName) {
		this.targetDbName = targetDbName;
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
	public String getSourceDbIp() {
		return sourceDbIp;
	}
	public void setSourceDbIp(String sourceDbIp) {
		this.sourceDbIp = sourceDbIp;
	}
	public String getSourceDbPort() {
		return sourceDbPort;
	}
	public void setSourceDbPort(String sourceDbPort) {
		this.sourceDbPort = sourceDbPort;
	}
	public String getSourceDbSchemaName() {
		return sourceDbSchemaName;
	}
	public void setSourceDbSchemaName(String sourceDbSchemaName) {
		this.sourceDbSchemaName = sourceDbSchemaName;
	}
	public String getSourceDbUserName() {
		return sourceDbUserName;
	}
	public void setSourceDbUserName(String sourceDbUserName) {
		this.sourceDbUserName = sourceDbUserName;
	}
	public String getSourceDbPassword() {
		return sourceDbPassword;
	}
	public void setSourceDbPassword(String sourceDbPassword) {
		this.sourceDbPassword = sourceDbPassword;
	}
	public String getSourceUnixIp() {
		return sourceUnixIp;
	}
	public void setSourceUnixIp(String sourceUnixIp) {
		this.sourceUnixIp = sourceUnixIp;
	}
	public String getSourceUnixUserName() {
		return sourceUnixUserName;
	}
	public void setSourceUnixUserName(String sourceUnixUserName) {
		this.sourceUnixUserName = sourceUnixUserName;
	}
	public String getSourceUnixPassword() {
		return sourceUnixPassword;
	}
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
	public String getTargetUnixIp() {
		return targetUnixIp;
	}
	public void setTargetUnixIp(String targetUnixIp) {
		this.targetUnixIp = targetUnixIp;
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
