package com.tcs.tools.web.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.business.frontend.FrontEndFileUpload;
import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dao.IdentifyPatternDAO;
import com.tcs.tools.web.dao.InvokePrimaryToolDAO;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.dto.IdentifyPatternDTO;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;
import com.tcs.tools.web.util.UnZip;

public class FrontEndIdentifyPatternsAction extends ActionSupport{
	
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
	//Newly Added
	private String seqNo="";
	private String analysisType = "";
	private String partialSPSelected = "";
	private String pCreatedBy ="TCS USER";
	private List<IdentifyPatternDTO>  procNameList ;
	
	
	private HashMap projectNameIdMap = null;
	String lSourceFilePath="";
	
	
	IdentifyPatternDAO lIdentifyPatternDAO = new IdentifyPatternDAO();
	ProjectModifyDAO lProjectCreationDAO = new ProjectModifyDAO();
	InvokePrimaryToolDAO lInvokePrimaryToolDAO = new InvokePrimaryToolDAO();
	UnZip lUnZip=new UnZip();
	
	public String execute() throws Exception {
		logger.info("::inside FrontEndCodeIdentifyPatternsAction-submitMode:::::"+getSubmitMode());
		logger.info("::inside FrontEndCodeIdentifyPatternsAction-sourceType:::::"+getSourceType());
		logger.info("::inside FrontEndCodeIdentifyPatternsAction-projectId:::::"+getProjectId());
		logger.info("::inside FrontEndCodeIdentifyPatternsAction-migrationMode:::::"+getMigrationMode());
		logger.info("::::::form value:::::-sourceSPUpFileFileName->"+getSourceSPUpFileFileName());
		
		//getting the project details list
		
		String lSeqNo="0";
		
		/*if(getAnalysisType() == null || "".equals(getAnalysisType())){
			setAnalysisType("VB");
		}*/
		
		
		HashMap lProjectNameIdMap = lProjectCreationDAO.getProjectDetails(); 
		setProjectNameIdMap(lProjectNameIdMap);
		if(getProjectId() == null || "".equals(getProjectId())){
			if(lProjectNameIdMap.size() > 0){
				setProjectId((String)((Map.Entry)(lProjectNameIdMap.entrySet().iterator().next())).getKey());
			}
		}
		
		
		procNameList  = lIdentifyPatternDAO.getProcNameList(getProjectId(),getAnalysisType());
		
		if("save".equalsIgnoreCase(getSubmitMode())){			
			lSeqNo = lIdentifyPatternDAO.insertIdentifyPatternDetails(getProjectId(),getMigrationMode(),getAnalysisType(),getPartialSPSelected(),pCreatedBy,"FRONT_END_IDENTIFY_PATTERN_REPORT");
			setSeqNo(lSeqNo);
			msgValue.append(getMigrationMode()+" mode selected");
			
			
		}
		if("changeProject".equalsIgnoreCase(getSubmitMode())){
			migrationMode ="";
			setMigrationMode("");
			System.out.println("::::empty contents:::");
		}
		
		setMsgToJsp(msgValue.toString());
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

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getAnalysisType() {
		return analysisType;
	}

	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}

	public String getPartialSPSelected() {
		return partialSPSelected;
	}

	public void setPartialSPSelected(String partialSPSelected) {
		this.partialSPSelected = partialSPSelected;
	}

	public String getpCreatedBy() {
		return pCreatedBy;
	}

	public void setpCreatedBy(String pCreatedBy) {
		this.pCreatedBy = pCreatedBy;
	}
	
	
}
