package com.tcs.tools.web.action;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.web.dao.Db2DatabaseAnalyticsDAO;
import com.tcs.tools.web.dao.InventoryAnalyticsDAO;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

/**
 * @author 455953
 *
 */

public class Db2DatabaseAnalyticsAction extends ActionSupport {
	private StringBuffer msgValue= new StringBuffer();
	private String submitMode="";
	private String msgToJsp="";
	private String type ="";
	private HashMap inventoryMap = null;
	private String projectId ="";
	private HashMap projectNameIdMap = new HashMap();
	private String dbName="";
	private String schemaName="";
	
	
	

	Db2DatabaseAnalyticsDAO lDb2DatabaseAnalyticsDAO = new Db2DatabaseAnalyticsDAO();
	ProjectModifyDAO lProjectModifyDAO = new ProjectModifyDAO();

	public String execute() throws Exception {
		
		System.out.println("::::inside Db2DatabaseAnalyticsAction ::::::"+getSubmitMode()+"---->"+getType());
		if("view".equalsIgnoreCase(getSubmitMode())
				||"changeProject".equalsIgnoreCase(getSubmitMode())){
			inventoryMap =  lDb2DatabaseAnalyticsDAO.getWholeQueryTextDb2() ;
			setInventoryMap(inventoryMap);
			HashMap lProjectNameIdMap = lProjectModifyDAO.getProjectDetails();
			setProjectNameIdMap(lProjectNameIdMap);
			
			if("changeProject".equalsIgnoreCase(getSubmitMode())){
				HashMap lDBConnectionDetailsMap = lDb2DatabaseAnalyticsDAO.getDB2ConnectionDetails( getProjectId());
				dbName = (String)lDBConnectionDetailsMap.get("TARGET_DB_NAME") ;
				schemaName = (String)lDBConnectionDetailsMap.get("TARGET_DB_SCHEMA_NAME") ;
				setDbName(dbName);
				setSchemaName(schemaName);
				System.out.println(getSchemaName()+"schema");
			}
		}
		
		
		
		
		if("download".equalsIgnoreCase(getSubmitMode())){
			
			HashMap lDBConnectionDetailsMap = lDb2DatabaseAnalyticsDAO.getDB2ConnectionDetails( getProjectId());
			dbName = (String)lDBConnectionDetailsMap.get("TARGET_DB_NAME") ;
			schemaName = (String)lDBConnectionDetailsMap.get("TARGET_DB_SCHEMA_NAME") ;
			setDbName(dbName);
			setSchemaName(schemaName);
			
			System.out.println(getSchemaName()+"schema");
			
			System.out.println("::::::requested type::::"+getType());
		
			String path=ToolsUtil.readProperty("tempDownloadPath");
			
			FileUploadDownloadUtility.createFolders(path);
		
			String filename=getType()+".txt";
			
			String fileContent = "no file found...";
			
			fileContent =  lDb2DatabaseAnalyticsDAO.getQueryText(getType(),getSchemaName()); 
			System.out.println("::::::inside download "+getSchemaName());
			System.out.println("::::::inside "+fileContent);
			if((fileContent != null) && (!"".equals(fileContent)) ){
				//for creating a temp file
				System.out.println("::::::inside file content ");
				FileUploadDownloadUtility.createFile(path,filename,fileContent);
				//for downloading a created file
	            FileUploadDownloadUtility.downloadFile(filename,path,ServletActionContext.getResponse());
			}
            return null;
			
		}
		
		if("downloadReport".equalsIgnoreCase(getSubmitMode())){
			HashMap lDBConnectionDetailsMap = lDb2DatabaseAnalyticsDAO.getDB2ConnectionDetails( getProjectId());
			dbName = (String)lDBConnectionDetailsMap.get("TARGET_DB_NAME") ;
			schemaName = (String)lDBConnectionDetailsMap.get("TARGET_DB_SCHEMA_NAME") ;
			setDbName(dbName);
			setSchemaName(schemaName);
			
			System.out.println("::::::requested type::::"+getType());
			HashMap lProjectNameIdMap = lProjectModifyDAO.getProjectDetails();
			String lProjectName = (String)lProjectNameIdMap.get(getProjectId());
			
			String path=ToolsUtil.readProperty("tempDownloadPath");
			FileUploadDownloadUtility.createFolders(path);
			String filename=lProjectName+"_"+getType()+".xls";
			
		
			
			System.out.println("::::::file name and path:::"+path+filename);
			//HashMap lDBConnectionDetailsMap = lDb2DatabaseAnalyticsDAO.getDB2ConnectionDetails( getProjectId());
			Connection lDb2Connection =  lDb2DatabaseAnalyticsDAO.getDb2Connection( lDBConnectionDetailsMap);
			List lInvertoryDetailsList = lDb2DatabaseAnalyticsDAO.populateDBInventoryReport(getType(),getProjectId(),lDb2Connection,getSchemaName());
			FileUploadDownloadUtility.downloadListAsExcelFile(lInvertoryDetailsList,path,filename,ServletActionContext.getResponse());
			
			//lInventoryAnalyticsDAO.getConsolidatedInventoryReport(getProjectId(),path,filename,ServletActionContext.getResponse());
			
            return null;
			
		}
		
		if("downloadConsolidatedReport".equalsIgnoreCase(getSubmitMode())){
			HashMap lDBConnectionDetailsMap = lDb2DatabaseAnalyticsDAO.getDB2ConnectionDetails( getProjectId());
			dbName = (String)lDBConnectionDetailsMap.get("TARGET_DB_NAME") ;
			schemaName = (String)lDBConnectionDetailsMap.get("TARGET_DB_SCHEMA_NAME") ;
			setDbName(dbName);
			setSchemaName(schemaName);
			
			System.out.println("::::::requested type::::"+getType());
			HashMap lProjectNameIdMap = lProjectModifyDAO.getProjectDetails();
			String lProjectName = (String)lProjectNameIdMap.get(getProjectId());
			
			String path=ToolsUtil.readProperty("tempDownloadPath");
			FileUploadDownloadUtility.createFolders(path);
			String filename=lProjectName+"_"+"CONSOLIDATED_REPORT"+".xls";
			
			System.out.println("::::::file name and path:::"+path+filename);
			
			lDb2DatabaseAnalyticsDAO.getConsolidatedInventoryReport(getProjectId(),path,filename,ServletActionContext.getResponse(),getSchemaName());
			
            return null;
			
		}
		return SUCCESS;
	}
	
	
	
	/**
	 * @return
	 */
	
	public StringBuffer getMsgValue() {
		return msgValue;
	}
	public void setMsgValue(StringBuffer msgValue) {
		this.msgValue = msgValue;
	}
	public String getSubmitMode() {
		return submitMode;
	}
	public void setSubmitMode(String submitMode) {
		this.submitMode = submitMode;
	}
	public String getMsgToJsp() {
		return msgToJsp;
	}
	public void setMsgToJsp(String msgToJsp) {
		this.msgToJsp = msgToJsp;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public HashMap getInventoryMap() {
		return inventoryMap;
	}
	public void setInventoryMap(HashMap inventoryMap) {
		this.inventoryMap = inventoryMap;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public HashMap getProjectNameIdMap() {
		return projectNameIdMap;
	}
	public void setProjectNameIdMap(HashMap projectNameIdMap) {
		this.projectNameIdMap = projectNameIdMap;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
}
