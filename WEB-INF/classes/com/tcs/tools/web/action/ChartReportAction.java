package com.tcs.tools.web.action;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.web.dao.ChartReportDAO;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class ChartReportAction extends ActionSupport /*implements ServletRequestAware,ServletResponseAware */ {

	//url
	//http://localhost:8080/WebProject/tool/index.actionr
	private String msgToJsp="";
	private String submitMode="";
	private String projectId="";
	private HashMap projectNameIdMap = new HashMap();
	private String projectName ="";
	private String paramProjectId = "";
	private List chartDetailsList = null;
	private String mode= "";
	
	public String execute() throws Exception {
		
		System.out.println(":::ChartReportAction patterns action::::"+getSubmitMode()+":::mode::::"+getMode());
		ProjectModifyDAO lProjectModifyDAO = new ProjectModifyDAO();
		ChartReportDAO lChartReportDAO = new  ChartReportDAO();
		//to get the project List - start
		HashMap lProjectNameIdMap = lProjectModifyDAO.getProjectDetails();
		setProjectNameIdMap(lProjectNameIdMap);
		//to get the project List - start
		
	
	if("openChart".equalsIgnoreCase(getSubmitMode())){
		System.out.println("::::inside sp pattern report:::::");
		setChartDetailsList(lChartReportDAO.getChartDetails(getParamProjectId(),getMode()));
		return "openChart";
		
	}
	
	if("downChart".equalsIgnoreCase(getSubmitMode())){
		System.out.println("::::inside sp pattern report:::::");
		chartDetailsList = lChartReportDAO.getChartDetails(getParamProjectId(),getMode());
		//for file creation
				String filename="SP_PROCEDURE_ANALYTICS_RPT_"+
						getParamProjectId()+"_"+getMode()+"_"+
									ToolsUtil.getDateTime()+
									".xls" ;
				String path=ToolsUtil.readProperty("excelReportPath");
				FileUploadDownloadUtility.createFolders(path);
				HSSFWorkbook hwb=new HSSFWorkbook();
				lChartReportDAO.generateExcel(  chartDetailsList, path, filename,null,hwb);
				FileOutputStream fileOut = new FileOutputStream(path+filename);
				hwb.write(fileOut);
				fileOut.close();
				
			 
					FileUploadDownloadUtility.downloadFile(filename,path,ServletActionContext.getResponse());
		return null;
		
	}
	
	if("downChartComplete".equalsIgnoreCase(getSubmitMode())){
		
			List lDispNameList= new ArrayList();
			lDispNameList.add("spPatternReport");
			lDispNameList.add("spPatternReportProcCount");
			lDispNameList.add("patternCountInSp");
			lDispNameList.add("datatypePatternAnalysys");
			lDispNameList.add("datatypePatternAnalysysSPWise");
			lDispNameList.add("datatypePatternAnalysysSPWiseComplexity");
			lDispNameList.add("datatypePatternAnalysysForImpacted");
			lDispNameList.add("datatypeImpactVsNonImpact");
			lDispNameList.add("functionPatternAnalysys");
			lDispNameList.add("functionPatternAnalysysSPWise");
			lDispNameList.add("functionPatternAnalysysSPWiseComplexity");
			lDispNameList.add("gVarPatternAnalysys");
			lDispNameList.add("gVarPatternAnalysysSPWise");
			lDispNameList.add("gVarPatternAnalysysSPWiseComplexity");
			lDispNameList.add("operatorPatternAnalysys");
			lDispNameList.add("operatorPatternAnalysysSPWise");
			lDispNameList.add("keywordPatternAnalysys");
			lDispNameList.add("keywordPatternAnalysysSPWise");
			
			List lDispLabelList= new ArrayList();
			lDispLabelList.add("SQL Statement Pattern Analysis");
			lDispLabelList.add("SQL Statement Pattern Analysis SP wise");
			lDispLabelList.add("SQL Statement Pattern Analysis SP wise Complexity");
			lDispLabelList.add("Datatype Pattern Analysis");
			lDispLabelList.add("Datatype Pattern Analysis SP Wise");
			lDispLabelList.add("Datatype Pattern Analysis SP Wise Complexity");
			lDispLabelList.add("Datatype Pattern Analysis For Impacted Datatypes");
			lDispLabelList.add("Datatype Pattern Analysis Impacted vs Non-Impacted");
			lDispLabelList.add("Built-in Function Pattern Analysis");
			lDispLabelList.add("Built-in Function Pattern Analysis SP Wise");
			lDispLabelList.add("Built-in Function Pattern Analysis SP Wise Complexity");
			lDispLabelList.add("Global Variable Pattern Analysis");
			lDispLabelList.add("Global Variable Pattern Analysis SP Wise");
			lDispLabelList.add("Global Variable Pattern Analysis SP Wise Complexity");
			lDispLabelList.add("Operator Pattern Analysis");
			lDispLabelList.add("Operator Pattern Analysis SP Wise");
			lDispLabelList.add("Keyword Pattern Analysis");
			lDispLabelList.add("Keyword Pattern Analysis SP Wise");
	
	
			
			String filename="SP_PROCEDURE_ANALYTICS_COMPLETE_RPT_"+
					getParamProjectId()+"_"+
								new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date())+
								".xls" ;
			String path=ToolsUtil.readProperty("excelReportPath");
			FileUploadDownloadUtility.createFolders(path);
			HSSFWorkbook hwb=new HSSFWorkbook();	
										
										System.out.println("::::project id:::"+getParamProjectId());
										
		for (int i = 0; i < lDispNameList.size(); i++) {
			
		
		chartDetailsList = lChartReportDAO.getChartDetails(getParamProjectId(),(String)lDispNameList.get(i));
		//for file creation
				
		hwb = lChartReportDAO.generateExcel(  chartDetailsList, path, filename,(String)lDispLabelList.get(i),hwb);
				
				
		}
		FileOutputStream fileOut = new FileOutputStream(path+filename);
		hwb.write(fileOut);
		fileOut.close();
					FileUploadDownloadUtility.downloadFile(filename,path,ServletActionContext.getResponse());
		return null;
		
	}
	
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
	 * @param msgToJsp the msgToJsp to set
	 */
	public void setMsgToJsp(String msgToJsp) {
		this.msgToJsp = msgToJsp;
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
	 * @return the paramProjectId
	 */
	public String getParamProjectId() {
		return paramProjectId;
	}


	/**
	 * @param paramProjectId the paramProjectId to set
	 */
	public void setParamProjectId(String paramProjectId) {
		this.paramProjectId = paramProjectId;
	}


	/**
	 * @return the chartDetailsList
	 */
	public List getChartDetailsList() {
		return chartDetailsList;
	}


	/**
	 * @param chartDetailsList the chartDetailsList to set
	 */
	public void setChartDetailsList(List chartDetailsList) {
		this.chartDetailsList = chartDetailsList;
	}


	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}


	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	

	
	
}
