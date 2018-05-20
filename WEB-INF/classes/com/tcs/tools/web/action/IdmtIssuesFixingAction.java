package com.tcs.tools.web.action;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.web.dao.InventoryAnalyticsDAO;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.idmt.CsToolCreateDrops;
import com.tcs.tools.web.idmt.CsToolDefaultColumns;
import com.tcs.tools.web.idmt.CsToolFixIdentity;
import com.tcs.tools.web.idmt.CsToolNamingConventions;
import com.tcs.tools.web.idmt.CsToolRemoveQuotes;
import com.tcs.tools.web.idmt.CsToolRemoveSchema;
import com.tcs.tools.web.idmt.CsToolSplCharTable;
import com.tcs.tools.web.idmt.CsToolSplitDDL;
import com.tcs.tools.web.util.FileTransferPlink;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class IdmtIssuesFixingAction extends ActionSupport {

	private static Logger logger = Logger.getLogger("ToolLogger");

	
	
	private String submitMode="";
	
	private File   sourceSPUpFile;
	private String sourceSPUpFileFileName;
	private String sourceSPUpFileContentType;
	
	private File   upFileTwo;
	private String upFileTwoFileName;
	private String upFileTwoContentType;
	HashMap projectNameIdMap = null;
	private String projectId ="";
	
	
	private String textBoxOne = "";
	
	public String execute() throws Exception {
		System.out.println("::::::insside IdmtIssuesFixingAction::::"+getSubmitMode());
		
		//for project names - id - start
		ProjectModifyDAO lProjectModifyDAO = new ProjectModifyDAO();
		HashMap lProjectNameIdMap = lProjectModifyDAO.getProjectDetails(); 
		setProjectNameIdMap(lProjectNameIdMap);		
		//for project names - id - end
		
		if("DEFAULT_COLUMNS".equalsIgnoreCase(getSubmitMode())){
			System.out.println("::::inside submit mode:::::");
			if(getSourceSPUpFileFileName() != null && (!"".equals(getSourceSPUpFileFileName()))){
				String fullFileName =ToolsUtil.readProperty("fileUploadPath")+""+""+"\\IDMT_UPLOADS\\"+getSubmitMode()+"\\" ;
				FileUploadDownloadUtility.createFolders(fullFileName);
				System.out.println("::::::fullFileName::::::"+fullFileName+getSourceSPUpFileFileName());
				File theFile = new File(fullFileName+getSourceSPUpFileFileName());
				FileUtils.copyFile(getSourceSPUpFile(), new File(fullFileName+getSourceSPUpFileFileName()));
				System.out.println("::::::;file upoad over:::::");
				
				//caling the tool for doing operations
				 //String pFolderPath="C:\\arun\\documents\\project\\idmt integration\\work\\default columns\\";
				 //String pFileName="input.txt";
				 System.out.println(":::inside main:::");
				 CsToolDefaultColumns lCsToolSource = new CsToolDefaultColumns();
				 String lTargetFilename = lCsToolSource.default_columns(fullFileName, getSourceSPUpFileFileName(),fullFileName+"\\PROCESSED\\");
				 System.out.println(":::::main over::::");
				 String[] lTargetFilenameArr =ToolsUtil.splitFileNameAndPath(lTargetFilename);
				 FileUploadDownloadUtility.downloadFile(lTargetFilenameArr[1],lTargetFilenameArr[0],ServletActionContext.getResponse());
				 return null;
			}
			return "DEFAULT_COLUMNS";
			//return SUCCESS;
		}
		
		if("SPL_CHAR_TABLE".equalsIgnoreCase(getSubmitMode())){
			System.out.println("::::inside submit mode:::::"+getSubmitMode());
			if(getSourceSPUpFileFileName() != null && (!"".equals(getSourceSPUpFileFileName()))){
				String fullFileName =ToolsUtil.readProperty("fileUploadPath")+""+""+"\\IDMT_UPLOADS\\"+getSubmitMode()+"\\" ;
				FileUploadDownloadUtility.createFolders(fullFileName);
				System.out.println("::::::fullFileName::::::"+fullFileName+getSourceSPUpFileFileName());
				File theFile = new File(fullFileName+getSourceSPUpFileFileName());
				FileUtils.copyFile(getSourceSPUpFile(), new File(fullFileName+getSourceSPUpFileFileName()));
				System.out.println("::::::;file upoad over:::::");
				
				//caling the tool for doing operations
				CsToolSplCharTable lCsToolSplCharTable = new CsToolSplCharTable();
				
				//input file path
				String pOutputFolder =fullFileName+"\\PROCESSED\\";
				
				 
				lCsToolSplCharTable.generate_CharFile(pOutputFolder, fullFileName+"\\"+getSourceSPUpFileFileName());
				 
				 
				
				CsToolDefaultColumns lCsToolSource = new CsToolDefaultColumns();
				 String lTargetFilename = lCsToolSource.default_columns(fullFileName, getSourceSPUpFileFileName(),fullFileName+"\\PROCESSED\\");
				 
				 String[] lTargetFilenameArr =ToolsUtil.splitFileNameAndPath(lTargetFilename);
				 FileUploadDownloadUtility.downloadFile(lTargetFilenameArr[1],lTargetFilenameArr[0],ServletActionContext.getResponse());
				 return null;
			}
			return "DEFAULT_COLUMNS";
			//return SUCCESS;
		}
		
		if("REMOVE_DOUBLE_QUOTES".equalsIgnoreCase(getSubmitMode())){
			System.out.println("::::inside submit mode:::::"+getSubmitMode());
			if(getSourceSPUpFileFileName() != null && (!"".equals(getSourceSPUpFileFileName()))){
				String fullFileName =ToolsUtil.readProperty("fileUploadPath")+""+""+"\\IDMT_UPLOADS\\"+getSubmitMode()+"\\" ;
				FileUploadDownloadUtility.createFolders(fullFileName);
				System.out.println("::::::fullFileName::::::"+fullFileName+getSourceSPUpFileFileName());
				File theFile = new File(fullFileName+getSourceSPUpFileFileName());
				FileUtils.copyFile(getSourceSPUpFile(), new File(fullFileName+getSourceSPUpFileFileName()));
				System.out.println("::::::;file upoad over:::::");
				
				//caling the tool for doing operations
				CsToolSplCharTable lCsToolSplCharTable = new CsToolSplCharTable();
				
				//input file path
				String pOutputFolder =fullFileName+"\\PROCESSED\\";
				
				CsToolRemoveQuotes lCsToolRemoveQuotes = new CsToolRemoveQuotes();
				String pOutputFile =lCsToolRemoveQuotes.remove_Quotes(fullFileName, getSourceSPUpFileFileName(),pOutputFolder);
				
				FileUploadDownloadUtility.downloadFile(pOutputFile,ServletActionContext.getResponse());
				return null;
			}
			return "DEFAULT_COLUMNS";
			//return SUCCESS;
		}
		
		if("NAMING_CONVENTIONS".equalsIgnoreCase(getSubmitMode())){
			System.out.println("::::inside submit mode:::::"+getSubmitMode());
			if(getSourceSPUpFileFileName() != null && (!"".equals(getSourceSPUpFileFileName()))){
				String fullFileName =ToolsUtil.readProperty("fileUploadPath")+""+""+"\\IDMT_UPLOADS\\"+getSubmitMode()+"\\" ;
				FileUploadDownloadUtility.createFolders(fullFileName);
				System.out.println("::::::fullFileName::::::"+fullFileName+getSourceSPUpFileFileName());
				File theFile = new File(fullFileName+getSourceSPUpFileFileName());
				FileUtils.copyFile(getSourceSPUpFile(), new File(fullFileName+getSourceSPUpFileFileName()));
				System.out.println("::::::;file upoad over:::::");
				
				//caling the tool for doing operations
				CsToolSplCharTable lCsToolSplCharTable = new CsToolSplCharTable();
				
				//input file path
				String pOutputFolder =fullFileName+"\\PROCESSED\\";
				
				CsToolNamingConventions lCsToolNamingConventions = new CsToolNamingConventions();
				String pOutputFile =lCsToolNamingConventions.set_NamingConvention(fullFileName, getSourceSPUpFileFileName(),pOutputFolder);
				FileUploadDownloadUtility.downloadFile(pOutputFile,ServletActionContext.getResponse());
				return null;
			}
			return "DEFAULT_COLUMNS";
			//return SUCCESS;
		}
		
		if("FIX_IDENTITY".equalsIgnoreCase(getSubmitMode())){
			System.out.println("::::inside submit mode:::::"+getSubmitMode());
			if(getSourceSPUpFileFileName() != null && (!"".equals(getSourceSPUpFileFileName()))){
				String fullFileName =ToolsUtil.readProperty("fileUploadPath")+""+""+"\\IDMT_UPLOADS\\"+getSubmitMode()+"\\" ;
				FileUploadDownloadUtility.createFolders(fullFileName);
				System.out.println("::::::fullFileName::::::"+fullFileName+getSourceSPUpFileFileName());
				File theFile = new File(fullFileName+getSourceSPUpFileFileName());
				FileUtils.copyFile(getSourceSPUpFile(), new File(fullFileName+getSourceSPUpFileFileName()));
				System.out.println("::::::;file upoad over:::::");
				
				//caling the tool for doing operations
				CsToolSplCharTable lCsToolSplCharTable = new CsToolSplCharTable();
				
				//input file path
				String pOutputFolder =fullFileName+"\\PROCESSED\\";
				
				 CsToolFixIdentity lCsToolFixIdentity = new CsToolFixIdentity();
				
				String pOutputFile =lCsToolFixIdentity.set_FixIdentity(fullFileName, getSourceSPUpFileFileName(),pOutputFolder);
				FileUploadDownloadUtility.downloadFile(pOutputFile,ServletActionContext.getResponse());
				return null;
			}
			return "DEFAULT_COLUMNS";
			//return SUCCESS;
		}
		
		
		if("REMOVE_SCHEMA".equalsIgnoreCase(getSubmitMode())){
			System.out.println("::::inside submit mode:::::"+getSubmitMode()+":::::text box value::::"+getTextBoxOne());
			if(getSourceSPUpFileFileName() != null && (!"".equals(getSourceSPUpFileFileName()))){
				String fullFileName =ToolsUtil.readProperty("fileUploadPath")+""+""+"\\IDMT_UPLOADS\\"+getSubmitMode()+"\\" ;
				FileUploadDownloadUtility.createFolders(fullFileName);
				System.out.println("::::::fullFileName::::::"+fullFileName+getSourceSPUpFileFileName());
				File theFile = new File(fullFileName+getSourceSPUpFileFileName());
				FileUtils.copyFile(getSourceSPUpFile(), new File(fullFileName+getSourceSPUpFileFileName()));
				System.out.println("::::::;file upoad over:::::");
				
				//caling the tool for doing operations
				CsToolSplCharTable lCsToolSplCharTable = new CsToolSplCharTable();
				
				//input file path
				String pOutputFolder =fullFileName+"\\PROCESSED\\";
				
				CsToolRemoveSchema lCsToolRemoveSchema = new CsToolRemoveSchema();
				
				String pOutputFile =lCsToolRemoveSchema.remove_Schema(fullFileName, getSourceSPUpFileFileName(),getTextBoxOne(),pOutputFolder);
				FileUploadDownloadUtility.downloadFile(pOutputFile,ServletActionContext.getResponse());
				return null;
			}
			return "DEFAULT_COLUMNS";
			//return SUCCESS;
		}
		
		if("CREATE_DROPS".equalsIgnoreCase(getSubmitMode())){
			System.out.println("::::inside submit mode:::::"+getSubmitMode()+":::::text box value::::"+getTextBoxOne());
			if(getSourceSPUpFileFileName() != null && (!"".equals(getSourceSPUpFileFileName()))){
				String fullFileName =ToolsUtil.readProperty("fileUploadPath")+""+""+"\\IDMT_UPLOADS\\"+getSubmitMode()+"\\" ;
				FileUploadDownloadUtility.createFolders(fullFileName);
				System.out.println("::::::fullFileName::::::"+fullFileName+getSourceSPUpFileFileName());
				File theFile = new File(fullFileName+getSourceSPUpFileFileName());
				FileUtils.copyFile(getSourceSPUpFile(), new File(fullFileName+getSourceSPUpFileFileName()));
				System.out.println("::::::;file upoad over:::::");
				
				//caling the tool for doing operations
				CsToolSplCharTable lCsToolSplCharTable = new CsToolSplCharTable();
				
				//input file path
				String pOutputFolder =fullFileName+"\\PROCESSED\\";
				
				 CsToolCreateDrops lCsToolCreateDrops = new CsToolCreateDrops();
				 String pOutputFile =lCsToolCreateDrops.generate_Drop(fullFileName, getSourceSPUpFileFileName(),pOutputFolder);
				FileUploadDownloadUtility.downloadFile(pOutputFile,ServletActionContext.getResponse());
				return null;
			}
			return "DEFAULT_COLUMNS";
			//return SUCCESS;
		}
		
		if("CREATE_DROPS".equalsIgnoreCase(getSubmitMode())){
			System.out.println("::::inside submit mode:::::"+getSubmitMode()+":::::text box value::::"+getTextBoxOne());
			if(getSourceSPUpFileFileName() != null && (!"".equals(getSourceSPUpFileFileName()))){
				String fullFileName =ToolsUtil.readProperty("fileUploadPath")+""+""+"\\IDMT_UPLOADS\\"+getSubmitMode()+"\\" ;
				FileUploadDownloadUtility.createFolders(fullFileName);
				System.out.println("::::::fullFileName::::::"+fullFileName+getSourceSPUpFileFileName());
				File theFile = new File(fullFileName+getSourceSPUpFileFileName());
				FileUtils.copyFile(getSourceSPUpFile(), new File(fullFileName+getSourceSPUpFileFileName()));
				System.out.println("::::::;file upoad over:::::");
				
				//caling the tool for doing operations
				CsToolSplCharTable lCsToolSplCharTable = new CsToolSplCharTable();
				
				//input file path
				String pOutputFolder =fullFileName+"\\PROCESSED\\";
				
				 CsToolCreateDrops lCsToolCreateDrops = new CsToolCreateDrops();
				 String pOutputFile =lCsToolCreateDrops.generate_Drop(fullFileName, getSourceSPUpFileFileName(),pOutputFolder);
				FileUploadDownloadUtility.downloadFile(pOutputFile,ServletActionContext.getResponse());
				return null;
			}
			return "DEFAULT_COLUMNS";
			//return SUCCESS;
		}
		
		if("SPLIT_DDLS".equalsIgnoreCase(getSubmitMode())){
			System.out.println("::::inside submit mode:::::"+getSubmitMode()+":::::text box value::::"+getTextBoxOne());
			if(getSourceSPUpFileFileName() != null && (!"".equals(getSourceSPUpFileFileName()))){
				String fullFileName =ToolsUtil.readProperty("fileUploadPath")+""+""+"\\IDMT_UPLOADS\\"+getSubmitMode()+"\\" ;
				FileUploadDownloadUtility.createFolders(fullFileName);
				FileUtils.copyFile(getSourceSPUpFile(), new File(fullFileName+getSourceSPUpFileFileName()));
				
				//getting the second upload file
				String lSecondUploadFile="";
				if(getUpFileTwoFileName() != null && (!"".equals(getUpFileTwoFileName()))){
					FileUtils.copyFile(getUpFileTwo(), new File(fullFileName+getUpFileTwoFileName()));
					lSecondUploadFile  = fullFileName+getUpFileTwoFileName();
				}
				
				
				
				
				
				//caling the tool for doing operations
				CsToolSplCharTable lCsToolSplCharTable = new CsToolSplCharTable();
				
				//input file path
				String pOutputFolder =fullFileName+"\\PROCESSED\\"+ToolsUtil.splitFileNameAndExtension(getSourceSPUpFileFileName())[0]+"\\";
				
				 
				 CsToolSplitDDL lCsToolSplitDDL = new CsToolSplitDDL();
				 String pOutputFile = lCsToolSplitDDL.split_Files( fullFileName, getSourceSPUpFileFileName(), pOutputFolder,lSecondUploadFile);
				
				
				FileUploadDownloadUtility.downloadFile(pOutputFile,ServletActionContext.getResponse());
				return null;
			}
			return "DEFAULT_COLUMNS";
			//return SUCCESS;
		}
		
		if("downloadSybaseSource".equalsIgnoreCase(getSubmitMode())){
			
			logger.info(":::::::::::inside :::::submitmode-->"+getSubmitMode()+"::::::project id--->"+getProjectId());
			InventoryAnalyticsDAO lInventoryAnalyticsDAO = new InventoryAnalyticsDAO();
			HashMap lDBConnectionDetailsMap = lInventoryAnalyticsDAO.getDBConnectionDetails( getProjectId());
			//SOURCE_UNIX_IP,SOURCE_UNIX_USER_NAME,SOURCE_UNIX_PASSWORD
			String pHostName = ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_UNIX_IP"));
			
			String pUserName =ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_UNIX_USER_NAME"));
			String pPassword =ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_UNIX_PASSWORD"));
			
			
			String pDBHostName = ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_DB_HOST_IP"));
			String pDBPortNo=ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_DB_HOST_PORT"));
			String pDBUserName =ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_DB_USER_NAME"));
			String pDBPassword =ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("SOURCE_DB_PASSWORD"));
			
			String pProjectName = ToolsUtil.replaceNull((String)lDBConnectionDetailsMap.get("PROJECT_NAME"));
			String pDDlGenOutputFolder =ToolsUtil.readProperty("ddlGenOutputFolder")+pUserName+"/DDL_GEN_OUTPUT/";
			
			String pCommand ="";
			//way 1
			//pCommand += " cd  "+ToolsUtil.readProperty("ddlGenOutputFolder") +" " +WebConstant.TOOLS_UNIX_CMD_SPLITTER;
			
			//way 2
			//pCommand += " cd  "+ToolsUtil.readProperty("ddlGenOutputFolder") +" && " ;//+WebConstant.TOOLS_UNIX_CMD_SPLITTER;
			
			//way 3
			//pCommand += " cd  "+ToolsUtil.readProperty("ddlGenOutputFolder") +" ; " ;//+WebConstant.TOOLS_UNIX_CMD_SPLITTER;
			
			pCommand =" ddlgen -U"+pDBUserName+" -P"+pDBPassword+" -S"+pDBHostName+":"+pDBPortNo+" -TU -NDCMS.dbo.% -o"+pDDlGenOutputFolder+"DDL_OUT_"+pProjectName+".out";
			 //pCommand =" ls -lrt ";
			
			 String pSourceFolderPath =ToolsUtil.readProperty("ddlGenOutputFolder")+"DDL_OUT_"+pProjectName+".out";
			 String pTargetFolderPath =ToolsUtil.readProperty("ddlgenOutputLocalCopy")+"_"+getProjectId()+"\\";
			 FileUploadDownloadUtility.createFolders(pTargetFolderPath);
			 FileTransferPlink.executeSshCommands( pHostName, pUserName, pPassword , pCommand);
			 FileTransferPlink.sshGetFiles(pHostName, pUserName, pPassword, pSourceFolderPath, pTargetFolderPath);
			
			FileUploadDownloadUtility.downloadFile(pTargetFolderPath,ServletActionContext.getResponse());
			return null;
			
		}
		
		
		System.out.println("::::::default return");
		return SUCCESS;
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
	 * @return the textBoxOne
	 */
	public String getTextBoxOne() {
		return textBoxOne;
	}

	/**
	 * @param textBoxOne the textBoxOne to set
	 */
	public void setTextBoxOne(String textBoxOne) {
		this.textBoxOne = textBoxOne;
	}

	/**
	 * @return the upFileTwo
	 */
	public File getUpFileTwo() {
		return upFileTwo;
	}

	/**
	 * @param upFileTwo the upFileTwo to set
	 */
	public void setUpFileTwo(File upFileTwo) {
		this.upFileTwo = upFileTwo;
	}

	/**
	 * @return the upFileTwoFileName
	 */
	public String getUpFileTwoFileName() {
		return upFileTwoFileName;
	}

	/**
	 * @param upFileTwoFileName the upFileTwoFileName to set
	 */
	public void setUpFileTwoFileName(String upFileTwoFileName) {
		this.upFileTwoFileName = upFileTwoFileName;
	}

	/**
	 * @return the upFileTwoContentType
	 */
	public String getUpFileTwoContentType() {
		return upFileTwoContentType;
	}

	/**
	 * @param upFileTwoContentType the upFileTwoContentType to set
	 */
	public void setUpFileTwoContentType(String upFileTwoContentType) {
		this.upFileTwoContentType = upFileTwoContentType;
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

}
