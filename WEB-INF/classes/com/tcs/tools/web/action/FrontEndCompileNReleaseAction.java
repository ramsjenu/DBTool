package com.tcs.tools.web.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dao.IdentifyPatternDAO;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.dto.IdentifyPatternDTO;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;
import com.tcs.tools.web.util.ZipUtil;
import com.tcs.tools.web.util.ZipUtilRecursive;

public class FrontEndCompileNReleaseAction extends ActionSupport /*implements ServletRequestAware,ServletResponseAware */ {

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
	
	public String execute() throws Exception {
		
	System.out.println(":::FrontEndCompileNRelease action::::"+getSubmitMode());
	System.out.println("::::form values->projectId"+getProjectId());
	System.out.println("::::form values->migrationMode"+getMigrationMode());
	System.out.println("::::form values->analysisType"+getAnalysisType());
	System.out.println("::::form values->submitMode"+getSubmitMode());
	
	String lSeqNo="0";
	ZipUtilRecursive lZipUtilRecursive=new ZipUtilRecursive();
	
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
			lSeqNo = lIdentifyPatternDAO.insertIdentifyPatternDetails(getProjectId(),getMigrationMode(),getAnalysisType(),getPartialSPSelected(),pCreatedBy,"FRONT_END_IDENTIFY_PATTERN_REPORT");
			setSeqNo(lSeqNo);
			msgValue.append("Records saved SuccessFully.... ");
		}
		if("changeProject".equalsIgnoreCase(getSubmitMode())){
			migrationMode ="";
			setMigrationMode("");
			System.out.println("::::empty contents:::");
		}
		if("feGenerateOutput".equalsIgnoreCase(getSubmitMode())){
		//	String lSourcePathToZip=ToolsUtil.readProperty("fileUploadPath")+getProjectId()+"\\"+getAnalysisType()+"\\"+ToolsUtil.readProperty("frontEndTargetFolderName");
			
			String lSourcePathToZip=ToolsUtil.readProperty("fileUploadPath")+getProjectId()+"\\"+getAnalysisType()+"\\"+"Source\\Unzipped\\";
			String lTargetPathToSaveZip=ToolsUtil.readProperty("fileUploadPath")+getProjectId()+"\\"+getAnalysisType()+"\\"+"Target_"+getProjectId()+".zip";
			try {
				File lFile=new File(lSourcePathToZip.trim());			
				System.out.println(lFile.canWrite());
				lZipUtilRecursive.createZipFile(lSourcePathToZip, lTargetPathToSaveZip);
				System.out.println("lSourcePathToZip::->"+lSourcePathToZip);
				System.out.println("lTargetPathToSaveZip::->"+lTargetPathToSaveZip);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FileUploadDownloadUtility.downloadFile(lTargetPathToSaveZip, ServletActionContext.getResponse());
			msgValue.append("Output Files Generated on "+ToolsUtil.getDateTime());
			return null;
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
