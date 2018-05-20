package com.tcs.tools.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.web.dao.IdentifyPatternDAO;
import com.tcs.tools.web.dao.InvokePrimaryToolDAO;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.dto.IdentifyPatternDTO;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class InvokePrimaryToolAction  extends ActionSupport {


	//url
	//http://localhost:8080/WebProject/tool/index.action
	private String retValue=""; 
	private StringBuffer msgValue= new StringBuffer(); 
	
	private String msgToJsp="";
	
	private String projectId = "";
	private String migrationMode = "";
	private String analysisType = "";
	private List<IdentifyPatternDTO>  procNameList ;
	private String submitMode="";
	private String partialSPSelected = "";
	private String pCreatedBy ="TCS USER";
	private String seqNo=""; 
	private HashMap projectNameIdMap = null;
	
	IdentifyPatternDAO lIdentifyPatternDAO = new IdentifyPatternDAO();
	ProjectModifyDAO lProjectCreationDAO = new ProjectModifyDAO();
	InvokePrimaryToolDAO lInvokePrimaryToolDAO = new InvokePrimaryToolDAO();
	
	 private static Logger logger = Logger.getLogger("ToolLogger");
	
	public String execute() throws Exception {
		
	System.out.println(":::identify patterns action1111::::"+getSubmitMode());
	System.out.println("::::form values->projectId"+getProjectId());
	System.out.println("::::form values->migrationMode"+getMigrationMode());
	System.out.println("::::form values->analysisType"+getAnalysisType());
	System.out.println("::::form values->submitMode"+getSubmitMode());
	
	String lSeqNo="0";
	/*if(getProjectId() == null || "".equals(getProjectId())){
		setProjectId("PR001");
	}*/
	if(getAnalysisType() == null || "".equals(getAnalysisType())){
		setAnalysisType("SOURCE");
	}
	
	
	HashMap lProjectNameIdMap = lProjectCreationDAO.getProjectDetails(); 
	setProjectNameIdMap(lProjectNameIdMap);
	if(getProjectId() == null || "".equals(getProjectId())){
		if(lProjectNameIdMap.size() > 0){
			setProjectId((String)((Map.Entry)(lProjectNameIdMap.entrySet().iterator().next())).getKey());
		}
	}
	
	
	procNameList  = lIdentifyPatternDAO.getProcNameList(getProjectId(),getAnalysisType());
	//System.out.println(":::::procNameList size:::"+procNameList.size());
	 
	
		
		
		
		if("save".equalsIgnoreCase(getSubmitMode())){
			//lSeqNo = lIdentifyPatternDAO.getRunSeq();
			lSeqNo = lIdentifyPatternDAO.insertIdentifyPatternDetails(getProjectId(),getMigrationMode(),getAnalysisType(),getPartialSPSelected(),pCreatedBy,"INVOKE_PRIMARY_TOOL");
			 lInvokePrimaryToolDAO.createSqlWaysInputFile( lSeqNo , getProjectId() ,getMigrationMode());
			setSeqNo(lSeqNo);
			msgValue.append("SP Downloaded on "+ToolsUtil.getDateTime());
		}
		if("changeProject".equalsIgnoreCase(getSubmitMode())){
			migrationMode ="";
			setMigrationMode("");
			System.out.println("::::empty contents:::");
		}
		
		if("downloadSource".equalsIgnoreCase(getSubmitMode())){
			logger.info(":::::inside submitmode::::"+getSubmitMode());
			logger.info(":::::inside getSeqNo::::"+getSeqNo());
			logger.info(":::::inside getMigrationMode::::"+getMigrationMode());
			logger.info(":::::inside getProjectId::::"+getProjectId());
			String lRetPath = lInvokePrimaryToolDAO.createMigratedTargetZip( getSeqNo(),getProjectId(),getMigrationMode());
			//for file download
			logger.info(":::::inside lRetPath::::"+lRetPath);
			 FileUploadDownloadUtility.downloadFile(lRetPath,ServletActionContext.getResponse());
			 return null;
			 //for file download
		}
		
		
		setMsgToJsp(msgValue.toString());
		System.out.println("::::jsp to msg ::::"+getMsgToJsp());
		if(!"".equals(retValue)){
			return retValue;
		}
        // do the work
        return SUCCESS;
   }

	

	/***************form getter setter elemements***************/
	/**
	 * @return the msgToJsp
	 */
	public String getMsgToJsp() {
		return msgToJsp;
	}

	/**
	 * @return the seqNo
	 */
	public String getSeqNo() {
		return seqNo;
	}



	/**
	 * @param seqNo the seqNo to set
	 */
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}



	/**
	 * @param msgToJsp the msgToJsp to set
	 */
	public void setMsgToJsp(String msgToJsp) {
		this.msgToJsp = msgToJsp;
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
	 * @return the analysisType
	 */
	public String getAnalysisType() {
		return analysisType;
	}



	/**
	 * @param analysisType the analysisType to set
	 */
	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}



	/**
	 * @return the procNameList
	 */
	public List getProcNameList() {
		return procNameList;
	}



	/**
	 * @param procNameList the procNameList to set
	 */
	public void setProcNameList(List procNameList) {
		this.procNameList = procNameList;
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
	 * @return the partialSPSelected
	 */
	public String getPartialSPSelected() {
		return partialSPSelected;
	}



	/**
	 * @param partialSPSelected the partialSPSelected to set
	 */
	public void setPartialSPSelected(String partialSPSelected) {
		this.partialSPSelected = partialSPSelected;
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


}
