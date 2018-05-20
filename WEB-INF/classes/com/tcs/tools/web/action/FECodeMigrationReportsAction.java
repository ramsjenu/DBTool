package com.tcs.tools.web.action;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.business.frontend.dto.DynamicSQLDataDTO;
import com.tcs.tools.web.dao.FECodeMigrationReportsDAO;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class FECodeMigrationReportsAction extends ActionSupport /*implements ServletRequestAware,ServletResponseAware */ {

	private String submitMode= "";
	private String projectId = "";
	private String analysisType = "";
	private String migrationMode = "";
	private List patternAnalysisReportList = new ArrayList() ;
	private String seqNo = "";
	private HashMap patternAnalysisReportTopDataMap = new HashMap();
	
	private InputStream fileInputStream;
	
	//for Detailed MismatchProcWise Report
	private String procName = "";
	private String misMatchCategory = "";
	


	/**
	 * @return the fileInputStream
	 */
	public InputStream getFileInputStream() {
		return fileInputStream;
	}


	/**
	 * @param fileInputStream the fileInputStream to set
	 */
	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}


	public String execute() throws Exception {
	System.out.println(":::FECODEMigrationReportsAction project action::::"+getSubmitMode());
	FECodeMigrationReportsDAO lFECodeMigrationReportsDAO = new FECodeMigrationReportsDAO();
	
	
	
	
	
	/***************************************for report 1 - fe dsql pattern report - start*************************************************/
	if("feDsqlPatternReport".equalsIgnoreCase(getSubmitMode())){
		
		patternAnalysisReportTopDataMap = lFECodeMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());		
		setPatternAnalysisReportList(lFECodeMigrationReportsDAO.getPatternAnalysisReportList(getProjectId(),getAnalysisType(),getMigrationMode(),getSeqNo()));		
		return "feDsqlPatternReport";
	}
	/***************************************for report 1 -  fe dsql pattern  report - end*************************************************/
	
	
	/***************************************for report 1 - fe dsql pattern report - start*************************************************/
	if("feDsqlPatternReportDownload".equalsIgnoreCase(getSubmitMode())){
		
		patternAnalysisReportTopDataMap = lFECodeMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());		
		patternAnalysisReportList  = lFECodeMigrationReportsDAO.getPatternAnalysisReportList(getProjectId(),getAnalysisType(),getMigrationMode(),getSeqNo());
		List lMainList = new ArrayList();
		List lSubList = new ArrayList();
		
		
		DynamicSQLDataDTO lDynamicSQLDataDTO = new DynamicSQLDataDTO();
		List lLineNumsList=null;
		String[] lDSqlQueryArr=null;
		int lSno=0;
		String lSourceFilePath="";
		
		lSubList.add("S.No");
		lSubList.add("Application Path");
		lSubList.add("File Name");
		lSubList.add("Line No");
		lSubList.add("Embedded SQL");
		lSubList.add("SQL Invoked Line No");
		lMainList.add(lSubList);
	
		
		  
		
		if(patternAnalysisReportList != null && patternAnalysisReportList.size() > 0 ){
			for(int i=0;i<patternAnalysisReportList.size();i++){
				lDynamicSQLDataDTO =(DynamicSQLDataDTO)patternAnalysisReportList.get(i);
				lLineNumsList=lDynamicSQLDataDTO.getsQLLineNumsLst();
				lDSqlQueryArr=lDynamicSQLDataDTO.getOrginalDSQLQuery().trim().split("\r\n");
				
				lSourceFilePath="";
				lSourceFilePath=lDynamicSQLDataDTO.getSourceFilePath().trim().split("\\\\Unzipped\\\\")[1];
				lSourceFilePath=lSourceFilePath.substring(lSourceFilePath.indexOf("\\")+1,lSourceFilePath.length());
				lSourceFilePath=ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"PROJECT_NAME")) +"\\"+lSourceFilePath+"\\";
			
				
				for(int j=0;j<lLineNumsList.size();j++){
					lSno=lSno+1;
					lSubList = new ArrayList();
					lSubList.add(lSno+"");
					lSubList.add(lSourceFilePath);
					lSubList.add(lDynamicSQLDataDTO.getSourceFileName().trim());
					lSubList.add((String)lLineNumsList.get(j));
					lSubList.add(lDSqlQueryArr[j]);
					lSubList.add(lDynamicSQLDataDTO.getInvokedLineNum());
					lMainList.add(lSubList);
						
					
					
					}
				}
			} 
		String pOutputPath=ToolsUtil.readProperty("excelReportPath")+"\\";
		String pFileName ="FRONT_END_SQL_IDENTIFY_PATTERN_"+patternAnalysisReportTopDataMap.get("PROJECT_NAME")+".xls";
		FileUploadDownloadUtility.downloadListAsExcelFile(lMainList, pOutputPath, pFileName, ServletActionContext.getResponse());
	
		return "feDsqlPatternReport";
	}
	/***************************************for report 1 -  fe dsql pattern  report - end*************************************************/
	
	
	/***************************************for report 1 - fe dsql pattern report - start*************************************************/
	if("feMapToTargetReport".equalsIgnoreCase(getSubmitMode())){
		
		patternAnalysisReportTopDataMap = lFECodeMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());		
		setPatternAnalysisReportList(lFECodeMigrationReportsDAO.getDSQLTargetMapData(getProjectId(),getAnalysisType(),getMigrationMode(),getSeqNo()));		
		return "feMapToTargetReport";
	}
	/***************************************for report 1 -  fe dsql pattern  report - end*************************************************/
	
	/***************************************for report 1 - fe dsql pattern report - start*************************************************/
	if("feMapToTargetReportDownload".equalsIgnoreCase(getSubmitMode())){
		
		patternAnalysisReportTopDataMap = lFECodeMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());		
		patternAnalysisReportList = lFECodeMigrationReportsDAO.getDSQLTargetMapData(getProjectId(),getAnalysisType(),getMigrationMode(),getSeqNo());
		DynamicSQLDataDTO lDynamicSQLDataDTO = new DynamicSQLDataDTO();
		int lSno =0;
		String lSourceFilePath ="";
		
		List lMainList = new ArrayList();
		List lSubList = new ArrayList();
		
		lSubList.add("S.No");
		lSubList.add("Application Path");
		lSubList.add("File Name");
		lSubList.add("Source Query");
		lSubList.add("Converted Query");
		lSubList.add("Convertion Status ");
		lMainList.add(lSubList);
	
		
		    
		
		if(patternAnalysisReportList != null && patternAnalysisReportList.size() > 0 ){
			for(int i=0;i<patternAnalysisReportList.size();i++){
				lDynamicSQLDataDTO =(DynamicSQLDataDTO)patternAnalysisReportList.get(i);	
					lSno=lSno+1;
					
					lSourceFilePath=lDynamicSQLDataDTO.getSourceFilePath().trim().split("\\\\Unzipped\\\\")[1];
					lSourceFilePath=lSourceFilePath.substring(lSourceFilePath.indexOf("\\")+1,lSourceFilePath.length());
					lSourceFilePath=ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"PROJECT_NAME")) +"\\"+lSourceFilePath+"\\";
					lSubList = new ArrayList();
					lSubList.add(lSno+"");
					lSubList.add(lSourceFilePath.trim());
					lSubList.add(lDynamicSQLDataDTO.getSourceFileName().trim());
					lSubList.add(lDynamicSQLDataDTO.getOrginalDSQLQuery());
					lSubList.add(lDynamicSQLDataDTO.getConvertedQuery());
					lSubList.add(lDynamicSQLDataDTO.getConvertionStatus() );
					
					lMainList.add(lSubList);
					
					
					
					
				}
			} 
		
		String pOutputPath=ToolsUtil.readProperty("excelReportPath")+"\\";
		String pFileName ="FRONT_END_SQL_MAP_TO_TARGET_"+patternAnalysisReportTopDataMap.get("PROJECT_NAME")+".xls";
		FileUploadDownloadUtility.downloadListAsExcelFile(lMainList, pOutputPath, pFileName, ServletActionContext.getResponse());
		return "feMapToTargetReport";
	}
	/***************************************for report 1 -  fe dsql pattern  report - end*************************************************/
	
	System.out.println(":::::after if conditions::::::");
	        // do the work
        return SUCCESS;
   }

	
	/***************form getter setter elemements***************/
	/**
	 * @return the patternAnalysisReportTopDataMap
	 */
	public HashMap getPatternAnalysisReportTopDataMap() {
		return patternAnalysisReportTopDataMap;
	}


	/**
	 * @param patternAnalysisReportTopDataMap the patternAnalysisReportTopDataMap to set
	 */
	public void setPatternAnalysisReportTopDataMap(
			HashMap patternAnalysisReportTopDataMap) {
		this.patternAnalysisReportTopDataMap = patternAnalysisReportTopDataMap;
	}
	/**
	 * @return the patternAnalysisReportList
	 */
	public List getPatternAnalysisReportList() {
		return patternAnalysisReportList;
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
	 * @param patternAnalysisReportList the patternAnalysisReportList to set
	 */
	public void setPatternAnalysisReportList(List patternAnalysisReportList) {
		this.patternAnalysisReportList = patternAnalysisReportList;
	}


	/**
	 * @return the submitMode
	 */
	public String getSubmitMode() {
		return submitMode;
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
	 * @param submitMode the submitMode to set
	 */
	public void setSubmitMode(String submitMode) {
		this.submitMode = submitMode;
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


	public String getProcName() {
		return procName;
	}


	public void setProcName(String procName) {
		this.procName = procName;
	}


	public String getMisMatchCategory() {
		return misMatchCategory;
	}


	public void setMisMatchCategory(String misMatchCategory) {
		this.misMatchCategory = misMatchCategory;
	}

	
	
	

}
