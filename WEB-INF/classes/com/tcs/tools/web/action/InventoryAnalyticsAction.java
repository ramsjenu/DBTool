package com.tcs.tools.web.action;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.web.dao.InventoryAnalyticsDAO;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class InventoryAnalyticsAction extends ActionSupport  {
	
	private StringBuffer msgValue= new StringBuffer();
	private String submitMode="";
	private String msgToJsp="";
	private String type ="";
	private HashMap inventoryMap = null;
	private String projectId ="";
	private HashMap projectNameIdMap = new HashMap();
	private String dbName="";




	InventoryAnalyticsDAO lInventoryAnalyticsDAO = new InventoryAnalyticsDAO();
	ProjectModifyDAO lProjectModifyDAO = new ProjectModifyDAO();

	public String execute() throws Exception {
		System.out.println("::::inside InventoryAnalyticsAction ::::::"+getSubmitMode()+"---->"+getType());
		if("view".equalsIgnoreCase(getSubmitMode())
				||"changeProject".equalsIgnoreCase(getSubmitMode())){
			inventoryMap =  lInventoryAnalyticsDAO.getWholeQueryText() ;
			setInventoryMap(inventoryMap);
			HashMap lProjectNameIdMap = lProjectModifyDAO.getProjectDetails();
			setProjectNameIdMap(lProjectNameIdMap);
			
			if("changeProject".equalsIgnoreCase(getSubmitMode())){
				HashMap lDBConnectionDetailsMap = lInventoryAnalyticsDAO.getDBConnectionDetails( getProjectId());
				dbName = (String)lDBConnectionDetailsMap.get("SOURCE_DB_SCHEMA_NAME") ;
				setDbName(dbName);
			}
		}
		
		
		
		
		if("download".equalsIgnoreCase(getSubmitMode())){
			System.out.println("::::::requested type::::"+getType());
			String path=ToolsUtil.readProperty("tempDownloadPath");
			FileUploadDownloadUtility.createFolders(path);
			String filename=getType()+".txt";
			String fileContent = "no file found...";
			fileContent =  lInventoryAnalyticsDAO.getQueryText(getType()); 
			if((fileContent != null) && (!"".equals(fileContent)) ){
				//for creating a temp file
				FileUploadDownloadUtility.createFile(path,filename,fileContent);
				//for downloading a created file
	            FileUploadDownloadUtility.downloadFile(filename,path,ServletActionContext.getResponse());
			}
            return null;
			
		}
		
		if("downloadReport".equalsIgnoreCase(getSubmitMode())){
			System.out.println("::::::requested type::::"+getType());
			HashMap lProjectNameIdMap = lProjectModifyDAO.getProjectDetails();
			String lProjectName = (String)lProjectNameIdMap.get(getProjectId());
			
			String path=ToolsUtil.readProperty("tempDownloadPath");
			FileUploadDownloadUtility.createFolders(path);
			String filename=lProjectName+"_"+getType()+".xls";
			
		
			
			System.out.println("::::::file name and path:::"+path+filename);
			HashMap lDBConnectionDetailsMap = lInventoryAnalyticsDAO.getDBConnectionDetails( getProjectId());
			 Connection lSybaseConnection =  lInventoryAnalyticsDAO.getSybaseConnection( lDBConnectionDetailsMap);
			List lInvertoryDetailsList = lInventoryAnalyticsDAO.populateDBInventoryReport(getType(),getProjectId(),lSybaseConnection);
			FileUploadDownloadUtility.downloadListAsExcelFile(lInvertoryDetailsList,path,filename,ServletActionContext.getResponse());
			
			//lInventoryAnalyticsDAO.getConsolidatedInventoryReport(getProjectId(),path,filename,ServletActionContext.getResponse());
			
            return null;
			
		}
		
		if("downloadConsolidatedReport".equalsIgnoreCase(getSubmitMode())){
			System.out.println("::::::requested type::::"+getType());
			HashMap lProjectNameIdMap = lProjectModifyDAO.getProjectDetails();
			String lProjectName = (String)lProjectNameIdMap.get(getProjectId());
			
			String path=ToolsUtil.readProperty("tempDownloadPath");
			FileUploadDownloadUtility.createFolders(path);
			String filename=lProjectName+"_"+"CONSOLIDATED_REPORT"+".xls";
			
			System.out.println("::::::file name and path:::"+path+filename);
			
			lInventoryAnalyticsDAO.getConsolidatedInventoryReport(getProjectId(),path,filename,ServletActionContext.getResponse());
			
            return null;
			
		}
		return SUCCESS;
	}

	
	
	
	/******form getter and setters******/
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}




	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}




	/**
	 * @return the inventoryMap
	 */
	public HashMap getInventoryMap() {
		return inventoryMap;
	}




	/**
	 * @param inventoryMap the inventoryMap to set
	 */
	public void setInventoryMap(HashMap inventoryMap) {
		this.inventoryMap = inventoryMap;
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




	public String getDbName() {
		return dbName;
	}




	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	
}
