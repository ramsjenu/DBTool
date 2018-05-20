package com.tcs.tools.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.dao.SearchSPDAO;
import com.tcs.tools.web.dao.ViewManualModificationDAO;
import com.tcs.tools.web.dto.IdentifyPatternDTO;
import com.tcs.tools.web.util.ToolsUtil;

public class SearchSPAction extends ActionSupport{

	private String submitMode = "";
	
	private String msgToJsp = "";
	private String errorMsgToJsp = "";
	private StringBuffer msgValue= new StringBuffer(); 
	private List<IdentifyPatternDTO>  procNameList ;
	private String pCreatedBy ="TCS USER";
	private String seqNo=""; 
	private HashMap projectNameIdMap = null;
	private String retValue=""; 
	private String projectId = "";
	private String analysisType = "";
	
	private String searchedString = "";
	private String searchedString2 = "";
	
	ProjectModifyDAO lProjectModifyDAO = new ProjectModifyDAO();
	SearchSPDAO lSearchSPDAO=new SearchSPDAO();
	
	public String execute() throws Exception {
	
		
		
		String lSeqNo="0";
		/*if(getProjectId() == null || "".equals(getProjectId())){
			setProjectId("PR001");
		}*/
		if(getAnalysisType() == null || "".equals(getAnalysisType())){
			setAnalysisType("SOURCE");
		}
		
		
		HashMap lProjectNameIdMap = lProjectModifyDAO.getProjectDetails(); 
		setProjectNameIdMap(lProjectNameIdMap);
		if(getProjectId() == null || "".equals(getProjectId())){
			if(lProjectNameIdMap.size() > 0){
				setProjectId((String)((Map.Entry)(lProjectNameIdMap.entrySet().iterator().next())).getKey());
			}
		}
		
		
		procNameList  = lSearchSPDAO.getProcNameList(getProjectId(),getAnalysisType());
		//System.out.println(":::::procNameList size:::"+procNameList.size());
		 
		
			
			
		
		
		if("reset".equalsIgnoreCase(getSubmitMode())){
			System.out.println("::::inside searchSP reset mode");
		   	 setSearchedString(" ");
		   	 setSearchedString2(" ");
		   	 return SUCCESS;
		    }
		
		if("search".equalsIgnoreCase(getSubmitMode())){
		   	 System.out.println("::::inside searchSP save mode");
		   	 lSeqNo = lSearchSPDAO.insertIdentifyPatternDetails(getProjectId(),getAnalysisType(),pCreatedBy,"SP_SEARCH");
		   	 setSeqNo(lSeqNo);
		   	 
		   		 setMsgToJsp("search successful");
		   	  }
		   
		       return SUCCESS;
			
			
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

	public String getErrorMsgToJsp() {
		return errorMsgToJsp;
	}

	public void setErrorMsgToJsp(String errorMsgToJsp) {
		this.errorMsgToJsp = errorMsgToJsp;
	}

	public StringBuffer getMsgValue() {
		return msgValue;
	}

	public void setMsgValue(StringBuffer msgValue) {
		this.msgValue = msgValue;
	}

	public String getSearchedString() {
		return searchedString;
	}

	public void setSearchedString(String searchedString) {
		this.searchedString = searchedString;
	}




	public List<IdentifyPatternDTO> getProcNameList() {
		return procNameList;
	}




	public void setProcNameList(List<IdentifyPatternDTO> procNameList) {
		this.procNameList = procNameList;
	}




	public String getpCreatedBy() {
		return pCreatedBy;
	}




	public void setpCreatedBy(String pCreatedBy) {
		this.pCreatedBy = pCreatedBy;
	}




	public String getSeqNo() {
		return seqNo;
	}




	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}




	public HashMap getProjectNameIdMap() {
		return projectNameIdMap;
	}




	public void setProjectNameIdMap(HashMap projectNameIdMap) {
		this.projectNameIdMap = projectNameIdMap;
	}




	public String getRetValue() {
		return retValue;
	}




	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}




	public String getProjectId() {
		return projectId;
	}




	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}




	public String getAnalysisType() {
		return analysisType;
	}




	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}




	public String getSearchedString2() {
		return searchedString2;
	}




	public void setSearchedString2(String searchedString2) {
		this.searchedString2 = searchedString2;
	}
}
