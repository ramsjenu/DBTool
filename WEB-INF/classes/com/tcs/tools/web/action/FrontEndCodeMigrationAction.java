package com.tcs.tools.web.action;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.business.frontend.FrontEndFileUpload;
import com.tcs.tools.web.dao.IdentifyPatternDAO;
import com.tcs.tools.web.dao.InvokePrimaryToolDAO;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;
import com.tcs.tools.web.util.UnZip;

public class FrontEndCodeMigrationAction extends ActionSupport{
	
	private static Logger logger = Logger.getLogger("ToolLogger");
	private String submitMode="";
	private String sourceType ="";
	private String projectId ="";
	private String migrationMode="";
	
	private File sourceSPUpFile;
	private String sourceSPUpFileFileName;
	private String sourceSPUpFileContentType;
	private String msgToJsp="";
	private String errorMsgToJsp="";
	private StringBuffer msgValue= new StringBuffer(); 
	
	
	private HashMap projectNameIdMap = null;
	String lSourceFilePath="";
	
	
	IdentifyPatternDAO lIdentifyPatternDAO = new IdentifyPatternDAO();
	ProjectModifyDAO lProjectCreationDAO = new ProjectModifyDAO();
	InvokePrimaryToolDAO lInvokePrimaryToolDAO = new InvokePrimaryToolDAO();
	UnZip lUnZip=new UnZip();
	
	public String execute() throws Exception {
		logger.info("::inside FrontEndCodeMigrationAction-submitMode:::::"+getSubmitMode());
		logger.info("::inside FrontEndCodeMigrationAction-sourceType:::::"+getSourceType());
		logger.info("::inside FrontEndCodeMigrationAction-projectId:::::"+getProjectId());
		logger.info("::inside FrontEndCodeMigrationAction-migrationMode:::::"+getMigrationMode());
		logger.info("::::::form value:::::-sourceSPUpFileFileName->"+getSourceSPUpFileFileName());
		
		//getting the project details list
		HashMap lProjectNameIdMap = lProjectCreationDAO.getProjectDetails(); 
		setProjectNameIdMap(lProjectNameIdMap);
		if(getProjectId() == null || "".equals(getProjectId())){
			if(lProjectNameIdMap.size() > 0){
				setProjectId((String)((Map.Entry)(lProjectNameIdMap.entrySet().iterator().next())).getKey());
			}
		}
		
		if("save".equalsIgnoreCase(getSubmitMode())){			
			String lRunSeq = lProjectCreationDAO.getRunSeq(getProjectId());
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
					msgValue.append("Invalid SourceFile Type");					
					
				}else{
					//copy file to server path		
					Connection lConnection= DBConnectionManager.getConnection();
					try {
						lConnection.setAutoCommit(false);
					//String fullFileName =ToolsUtil.readProperty("fileUploadPath")+""+getProjectId()+"\\VB\\Source\\" +getProjectId()/*+"_"+lRunSeq*/+".zip";
					String fullFileName =ToolsUtil.readProperty("fileUploadPath")+""+getProjectId()+"\\"+getSourceType()+"\\Source\\" +getProjectId()/*+"_"+lRunSeq*/+".zip";
					String lRootFolderPath=ToolsUtil.readProperty("fileUploadPath")+""+getProjectId()+"\\"+getSourceType()+"\\Source\\Unzipped\\"+getProjectId()+"\\";
					System.out.println("::::::fullFileName::::::"+fullFileName);
					File theFile = new File(fullFileName);
					FileUtils.copyFile(getSourceSPUpFile(), new File(fullFileName));					
					lSourceFilePath = lUnZip.unzipFile( fullFileName);
					FrontEndFileUpload lFrontEndFileUpload=new FrontEndFileUpload();
					//JOptionPane.showMessageDialog(null, "source file path  "+lSourceFilePath);
					System.out.println("::::lSourceFilePath::::::"+lSourceFilePath);
					System.out.println("::::lRootFolderPath::::::"+lRootFolderPath);
					//chnaged to other things as the source path is the unzipped path not the root path
					
					//lSourceFilePath += "\\"+ToolsUtil.splitFileNameAndExtension(getSourceSPUpFileFileName())[0]+"\\";
					System.out.println("::::lSourceFilePath-modified::::::"+lSourceFilePath);
					//Invoking Parse process
						lFrontEndFileUpload.intiateFilParsing(getProjectId(),lSourceFilePath/*lRootFolderPath*//*Present dirctory for parse; first time root will be the present directory*/
								,lRootFolderPath/*lRootFolderPath*/,/*"VB"*/getSourceType() ,new ArrayList(),lConnection);
						//Update Status to Front End - Start
						 String lCurState="Completed";
						String lStausMsg="Process Completed";
						ToolsUtil.prepareInsertStatusMsg( getProjectId(), ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
						lConnection.commit();
						lConnection.setAutoCommit(true);
						//Update Status to Front End - End
							
					msgValue.append("Source Code uploaded on "+ToolsUtil.getDateTime());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						DBConnectionManager.closeConnection(lConnection);
					}
				}
				setMsgToJsp(msgValue.toString());
				
				
			}
		}
		if("changeProject".equalsIgnoreCase(getSubmitMode())){
			migrationMode ="";
			setMigrationMode("");
			System.out.println("::::empty contents:::");
		}
		
		
		
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
	 * @return the sourceType
	 */
	public String getSourceType() {
		return sourceType;
	}

	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
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
	 * @return the migrationMode
	 */
	public String getMigrationMode() {
		return migrationMode;
	}

	/**
	 * @param migrationMode the migrationMode to set
	 */
	public void setMigrationMode(String migrationMode) {
		this.migrationMode = migrationMode;
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
	
	
}
