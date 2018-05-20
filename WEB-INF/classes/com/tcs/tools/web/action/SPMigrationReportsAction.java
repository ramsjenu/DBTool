package com.tcs.tools.web.action;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.dao.SPMigrationReportsDAO;
import com.tcs.tools.web.dao.SearchSPDAO;
import com.tcs.tools.web.dto.SPCallTreeFirstLevelDTO;
import com.tcs.tools.web.dto.SPManualModificationLogReportDTO;
import com.tcs.tools.web.dto.SPPatternAnalysisReportDTO;
import com.tcs.tools.web.dto.SearchSPDTO;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class SPMigrationReportsAction extends ActionSupport /*implements ServletRequestAware,ServletResponseAware */ {
	private String searchedString="";
	private String searchedString2="";
	private String submitMode= "";
	private String projectId = "";
	private String analysisType = "";
	private String migrationMode = "";
	private List patternAnalysisReportList = new ArrayList() ;
	private String seqNo = "";
	private int seqNoI = 0;
	private HashMap patternAnalysisReportTopDataMap = new HashMap();
	
	private HashMap manualModificationReportTopDataMap = new HashMap();
	private List manualModificationReportList = new ArrayList() ;
	
	private HashMap spCallTreeFirstLevelReportTopDataMap = new HashMap();
	private List spCallTreeFirstLevelReportList = new ArrayList() ;
	private ArrayList<SearchSPDTO> searchedList = new ArrayList<SearchSPDTO>() ;
	private HashMap spSearchReportTopDataMap = new HashMap();
	ProjectModifyDAO lProjectModifyDAO = new ProjectModifyDAO();
	
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
	System.out.println(":::SPMigrationReportsAction project action::::"+getSubmitMode());
	SPMigrationReportsDAO lSPMigrationReportsDAO = new SPMigrationReportsDAO();
	SearchSPDAO lSearchSPDAO=new SearchSPDAO();
	if("SearchReport".equalsIgnoreCase(getSubmitMode())){
		System.out.println("inside search report");
		System.out.println("inside search report "+getSearchedString());
		System.out.println("inside search report "+getProjectId());
		System.out.println("inside search report "+getSeqNo());
		//patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
		setSearchedList(lSearchSPDAO.searchParseResultsTable(getSearchedString(), getProjectId(),getSeqNo())); 
		System.out.println(getSearchedList());
		//setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		return "SearchReport";
	 }
	if("SearchReportString".equalsIgnoreCase(getSubmitMode())){
		System.out.println("inside search report");
		System.out.println("inside search report "+getSearchedString2());
		System.out.println("inside search report "+getProjectId());
		System.out.println("inside search report "+getSeqNo());
		//patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
		setSearchedList(lSearchSPDAO.searchStringParseResultsTable(getSearchedString2(), getProjectId(),getSeqNo())); 
		System.out.println(getSearchedList());
		//setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		return "SearchReportString";
	 }
	
	
	
	
	if("manualModificationLogReport".equalsIgnoreCase(getSubmitMode())){
		
		//patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
		setManualModificationReportList ( lSPMigrationReportsDAO.getManualModLogReportList(getProjectId(),getMigrationMode(),getSeqNo()));
		//setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		return "manualModificationLogReport";
	 }
	
	
	if("spCallTreeFirstLevelReport".equalsIgnoreCase(getSubmitMode())){
		
		//patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
		setSpCallTreeFirstLevelReportList ( lSPMigrationReportsDAO.getSPCallTreeFirstLevelReportList(getProjectId(),getMigrationMode(),getSeqNo()));
		//setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		return "spCallTreeFirstLevelReport";
	 }
	

	
	
	
	/***************************************for report 1 - sp pattern report - start*************************************************/	
	if("spPatternReport".equalsIgnoreCase(getSubmitMode())){
		
		patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
		setPatternAnalysisReportList ( lSPMigrationReportsDAO.getPatternAnalysisReportList(getProjectId(), getAnalysisType(),getMigrationMode(),getSeqNo() ));
		setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		return "spPatternReport";
	}
	
	if("spPatternResultReportDownLoad".equalsIgnoreCase(getSubmitMode())){
		
		//getting list from db
		patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
		
		 //for file creation
		String filename="SP_PATTERN_DTL_RPT_"+
							ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"PROJECT_NAME"))+"_"+
							new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date())+
							".xls" ;
		String path=ToolsUtil.readProperty("excelReportPath");
		FileUploadDownloadUtility.createFolders(path);
		
		lSPMigrationReportsDAO.getPatternAnalysisReportCompleteListFolders(getProjectId(), getAnalysisType(),ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"ANALYSIS_MODE")),getSeqNo() ,filename,path);
		
		return null;
		
	}
	if("spPatternReportDownLoad".equalsIgnoreCase(getSubmitMode())){
		System.out.println("::::::insdice------------------------>>>>>>"+(String)patternAnalysisReportTopDataMap.get((String)"PROJECT_NAME")+"<<<<<<<");
		
		
		//getting list from db
				patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
				
				
				
				
		
		 //for file creation
		String filename="SP_PATTERN_SUM_RPT_"+
							ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"PROJECT_NAME"))+"_"+
							new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date())+
							".xls" ;
		String path=ToolsUtil.readProperty("excelReportPath");
		FileUploadDownloadUtility.createFolders(path);
		
		HSSFWorkbook hwb=new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		String colValue="Hi";
		/*for (int i = 1; i < 11; i++) {
			HSSFRow rowhead= sheet.createRow((short)i);
			rowhead.createCell((short) 0).setCellValue(i+"");
			rowhead.createCell((short) 1).setCellValue(colValue);
			rowhead.createCell((short) 2).setCellValue(colValue);
			rowhead.createCell((short) 3).setCellValue(colValue);
			rowhead.createCell((short) 4).setCellValue(colValue);
			rowhead.createCell((short) 5).setCellValue(colValue);
			rowhead.createCell((short) 6).setCellValue(colValue);
		}*/

		

	    
	    
		HSSFRow rowhead= sheet.createRow((short)0);
		rowhead.createCell((short) 0).setCellValue("S.No") ;
		rowhead.createCell((short) 1).setCellValue("Stored Procedure Name");
		rowhead.createCell((short) 2).setCellValue("Line No");
		rowhead.createCell((short) 3).setCellValue("Category");
		rowhead.createCell((short) 4).setCellValue("Pattern Id");
		rowhead.createCell((short) 5).setCellValue("Pattern Desc");
		rowhead.createCell((short) 6).setCellValue("No. Sub Statement");
		
		
		/*HSSFRow row1= sheet.createRow((short)(lrowCount+1));
		row1.createCell((short) 0).setCellValue("1");
		row1.createCell((short) 1).setCellValue("Rose");
		row1.createCell((short) 2).setCellValue("India");
		row1.createCell((short) 3).setCellValue("roseindia");
		row1.createCell((short) 4).setCellValue("hello@roseindia.net");
		row1.createCell((short) 5).setCellValue("India");
		lrowCount++;
		//HSSFRow row1= sheet.createRow((short)2);
		row1= sheet.createRow((short)(short)(lrowCount+1));
		row1.createCell((short) 0).setCellValue("12");
		row1.createCell((short) 1).setCellValue("Rose");
		row1.createCell((short) 2).setCellValue("India");
		row1.createCell((short) 3).setCellValue("roseindia");
		row1.createCell((short) 4).setCellValue("hello@roseindia.net");
		row1.createCell((short) 5).setCellValue("India");
*/
		
		//setPatternAnalysisReportList ( lSPMigrationReportsDAO.getPatternAnalysisReportList(getProjectId(), getAnalysisType(),ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"ANALYSIS_MODE")),getSeqNo() ));
		//setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		List patternAnalysisReportList = lSPMigrationReportsDAO.getPatternAnalysisReportList(getProjectId(), getAnalysisType(),ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"ANALYSIS_MODE")),getSeqNo() );
		
		SPPatternAnalysisReportDTO lSPPatternAnalysisReportDTO = new SPPatternAnalysisReportDTO();
		
		if(patternAnalysisReportList != null && patternAnalysisReportList.size() > 0 ){
			for(int i=0;i<patternAnalysisReportList.size();i++){
				lSPPatternAnalysisReportDTO =(SPPatternAnalysisReportDTO)patternAnalysisReportList.get(i);
				
				HSSFRow row= sheet.createRow((short)(i+1));
				row.createCell((short) 0).setCellValue((i+1)+"");
				row.createCell((short) 1).setCellValue(lSPPatternAnalysisReportDTO.getProcName());
				row.createCell((short) 2).setCellValue(lSPPatternAnalysisReportDTO.getStatementNo());
				row.createCell((short) 3).setCellValue(lSPPatternAnalysisReportDTO.getCategory());
				row.createCell((short) 4).setCellValue(lSPPatternAnalysisReportDTO.getPatternId());
				row.createCell((short) 5).setCellValue(lSPPatternAnalysisReportDTO.getPatternDesc());
				row.createCell((short) 6).setCellValue(lSPPatternAnalysisReportDTO.getQueryCount());
			}
		}
		//getting list from db
		
		

		

		FileOutputStream fileOut = new FileOutputStream(path+filename);
		hwb.write(fileOut);
		fileOut.close();
		System.out.println("Your excel file has been generated!");

		 //for file creation
		 //for file download
		 FileUploadDownloadUtility.downloadFile(filename,path,ServletActionContext.getResponse());
		 //for file download
		 
		 
		 
		 
		 //res.sendRedirect("/index.jsp");

		 
		 return null;
	}
	/***************************************for report 1 - sp pattern report - end*************************************************/
	
	
	
	
	
	if("spManualModificationReportDownload".equalsIgnoreCase(getSubmitMode())){
	//	System.out.println("::::::insdice------------------------>>>>>>"+(String)patternAnalysisReportTopDataMap.get((String)"PROJECT_NAME")+"<<<<<<<");
		
		
		//getting list from db
		manualModificationReportTopDataMap = lSPMigrationReportsDAO.getManualModificationReportTopDetails(getSeqNo());
				
		
		
		 //for file creation
		String filename="SP_MANUAL_MODLOG_RPT_"+
							ToolsUtil.replaceNull((String)manualModificationReportTopDataMap.get((String)"PROJECT_NAME"))+"_"+
							new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date())+
							".xls" ;
		String path=ToolsUtil.readProperty("excelReportPath");
		FileUploadDownloadUtility.createFolders(path);
		
		HSSFWorkbook hwb=new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		String colValue="Hi";
		/*for (int i = 1; i < 11; i++) {
			HSSFRow rowhead= sheet.createRow((short)i);
			rowhead.createCell((short) 0).setCellValue(i+"");
			rowhead.createCell((short) 1).setCellValue(colValue);
			rowhead.createCell((short) 2).setCellValue(colValue);
			rowhead.createCell((short) 3).setCellValue(colValue);
			rowhead.createCell((short) 4).setCellValue(colValue);
			rowhead.createCell((short) 5).setCellValue(colValue);
			rowhead.createCell((short) 6).setCellValue(colValue);
		}*/

		

	    
	    
		HSSFRow rowhead= sheet.createRow((short)0);
		rowhead.createCell((short) 0).setCellValue("S.No") ;
		rowhead.createCell((short) 1).setCellValue("Stored Procedure Name");
		rowhead.createCell((short) 2).setCellValue("Statement No");
		rowhead.createCell((short) 3).setCellValue("Statement");
		
		
		
		/*HSSFRow row1= sheet.createRow((short)(lrowCount+1));
		row1.createCell((short) 0).setCellValue("1");
		row1.createCell((short) 1).setCellValue("Rose");
		row1.createCell((short) 2).setCellValue("India");
		row1.createCell((short) 3).setCellValue("roseindia");
		row1.createCell((short) 4).setCellValue("hello@roseindia.net");
		row1.createCell((short) 5).setCellValue("India");
		lrowCount++;
		//HSSFRow row1= sheet.createRow((short)2);
		row1= sheet.createRow((short)(short)(lrowCount+1));
		row1.createCell((short) 0).setCellValue("12");
		row1.createCell((short) 1).setCellValue("Rose");
		row1.createCell((short) 2).setCellValue("India");
		row1.createCell((short) 3).setCellValue("roseindia");
		row1.createCell((short) 4).setCellValue("hello@roseindia.net");
		row1.createCell((short) 5).setCellValue("India");
*/
		
		//setPatternAnalysisReportList ( lSPMigrationReportsDAO.getPatternAnalysisReportList(getProjectId(), getAnalysisType(),ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"ANALYSIS_MODE")),getSeqNo() ));
		//setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		List manualModificationReportList = lSPMigrationReportsDAO.getManualModificationReportList(getProjectId(), getAnalysisType(),ToolsUtil.replaceNull((String)manualModificationReportTopDataMap.get((String)"ANALYSIS_MODE")),getSeqNo() );
		
		
		
		
		SPManualModificationLogReportDTO lSPManualModificationLogReportDTO = new SPManualModificationLogReportDTO();
		
		if(manualModificationReportList != null && manualModificationReportList.size() > 0 ){
			for(int i=0;i<manualModificationReportList.size();i++){
				lSPManualModificationLogReportDTO =(SPManualModificationLogReportDTO)manualModificationReportList.get(i);
				
				HSSFRow row= sheet.createRow((short)(i+1));
				row.createCell((short) 0).setCellValue((i+1)+"");
				row.createCell((short) 1).setCellValue(lSPManualModificationLogReportDTO.getProcName());
				row.createCell((short) 2).setCellValue(lSPManualModificationLogReportDTO.getStatementNo());
				row.createCell((short) 3).setCellValue(lSPManualModificationLogReportDTO.getStatement());
				
			}
		}
		//getting list from db
		
		

		

		FileOutputStream fileOut = new FileOutputStream(path+filename);
		hwb.write(fileOut);
		fileOut.close();
		System.out.println("Your excel file has been generated!");

		 //for file creation
		 //for file download
		 FileUploadDownloadUtility.downloadFile(filename,path,ServletActionContext.getResponse());
		 //for file download
		 
		 
		 
		 
		 //res.sendRedirect("/index.jsp");

		 
		 return null;
	}
	
	
	if("spSearchReportDownload".equalsIgnoreCase(getSubmitMode())){
			//System.out.println("::::::insdice------------------------>>>>>>"+(String)patternAnalysisReportTopDataMap.get((String)"PROJECT_NAME")+"<<<<<<<");
			
			
			//getting list from db
			spSearchReportTopDataMap = lSPMigrationReportsDAO.getSPSearchTopDetails(getSeqNo());
					
			
			
			 //for file creation
			String filename="SP_STRING_SEARCH_RPT_"+
								ToolsUtil.replaceNull((String)spSearchReportTopDataMap.get((String)"PROJECT_NAME"))+"_"+
								new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date())+
								".xls" ;
			String path=ToolsUtil.readProperty("excelReportPath");
			FileUploadDownloadUtility.createFolders(path);
			
			HSSFWorkbook hwb=new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet("search result");
			String colValue="Hi";
			
		    
			HSSFRow rowhead= sheet.createRow((short)0);
			rowhead.createCell((short) 0).setCellValue("S.No") ;
			rowhead.createCell((short) 1).setCellValue("Searched Keyword");
			rowhead.createCell((short) 2).setCellValue("Stored Procedure Name");
			rowhead.createCell((short) 3).setCellValue("Statement No");
			rowhead.createCell((short) 4).setCellValue("Statement");
			
			
			System.out.println("priyanka");
			List searchedList = lSearchSPDAO.searchStringParseResultsTable(getSearchedString2(), getProjectId(),getSeqNo());
			System.out.println("priyanka2 "+searchedList);
			
			
			
			SearchSPDTO lSearchSPDTO = new SearchSPDTO();
			
			if(searchedList != null && searchedList.size() > 0 ){
				for(int i=0;i<searchedList.size();i++){
					lSearchSPDTO =(SearchSPDTO)searchedList.get(i);
					
					HSSFRow row= sheet.createRow((short)(i+1));
					row.createCell((short) 0).setCellValue((i+1));
					row.createCell((short) 1).setCellValue(lSearchSPDTO.getSearchedWord());
					row.createCell((short) 2).setCellValue(lSearchSPDTO.getProcedureName());
					row.createCell((short) 3).setCellValue(lSearchSPDTO.getStatementNo());
					row.createCell((short) 4).setCellValue(lSearchSPDTO.getStatement());
					
				}
			}
			//getting list from db
			
			

			

			FileOutputStream fileOut = new FileOutputStream(path+filename);
			hwb.write(fileOut);
			fileOut.close();
			System.out.println("Your excel file has been generated!");

			 //for file creation
			 //for file download
			 FileUploadDownloadUtility.downloadFile(filename,path,ServletActionContext.getResponse());
			 //for file download
			 
			 
			 
			 
			 //res.sendRedirect("/index.jsp");

			 
			 return null;
		}
		
	
	
	
	
	if("spSearchReportDownload2".equalsIgnoreCase(getSubmitMode())){
		//System.out.println("::::::insdice------------------------>>>>>>"+(String)patternAnalysisReportTopDataMap.get((String)"PROJECT_NAME")+"<<<<<<<");
		
		
		//getting list from db
		spSearchReportTopDataMap = lSPMigrationReportsDAO.getSPSearchTopDetails(getSeqNo());
				
		
		
		 //for file creation
		String filename="SP_KEYWORD_SEARCH_RPT_"+
							ToolsUtil.replaceNull((String)spSearchReportTopDataMap.get((String)"PROJECT_NAME"))+"_"+
							new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date())+
							".xls" ;
		String path=ToolsUtil.readProperty("excelReportPath");
		FileUploadDownloadUtility.createFolders(path);
		
		HSSFWorkbook hwb=new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("search result");
		String colValue="Hi";
		
	    
		HSSFRow rowhead= sheet.createRow((short)0);
		rowhead.createCell((short) 0).setCellValue("S.No") ;
		rowhead.createCell((short) 1).setCellValue("Searched Keyword");
		rowhead.createCell((short) 2).setCellValue("Stored Procedure Name");
		rowhead.createCell((short) 3).setCellValue("Statement No");
		rowhead.createCell((short) 4).setCellValue("Statement");
		
		
		System.out.println("priyanka3");
		List searchedList = lSearchSPDAO.searchParseResultsTable(getSearchedString(), getProjectId(),getSeqNo());
		System.out.println("priyanka4 "+searchedList);
		
		
		
		SearchSPDTO lSearchSPDTO = new SearchSPDTO();
		
		if(searchedList != null && searchedList.size() > 0 ){
			for(int i=0;i<searchedList.size();i++){
				lSearchSPDTO =(SearchSPDTO)searchedList.get(i);
				
				HSSFRow row= sheet.createRow((short)(i+1));
				row.createCell((short) 0).setCellValue((i+1));
				row.createCell((short) 1).setCellValue(lSearchSPDTO.getSearchedWord());
				row.createCell((short) 2).setCellValue(lSearchSPDTO.getProcedureName());
				row.createCell((short) 3).setCellValue(lSearchSPDTO.getStatementNo());
				row.createCell((short) 4).setCellValue(lSearchSPDTO.getStatement());
				
			}
		}
		//getting list from db
		
		

		

		FileOutputStream fileOut = new FileOutputStream(path+filename);
		hwb.write(fileOut);
		fileOut.close();
		System.out.println("Your excel file has been generated!");

		 //for file creation
		 //for file download
		 FileUploadDownloadUtility.downloadFile(filename,path,ServletActionContext.getResponse());
		 //for file download
		 
		 
		 
		 
		 //res.sendRedirect("/index.jsp");

		 
		 return null;
	}
	


	
	
	
	
	if("spCallTreeFirstLevelReportDownload".equalsIgnoreCase(getSubmitMode())){
		
			
			
			//getting list from db
		spCallTreeFirstLevelReportTopDataMap = lSPMigrationReportsDAO.getSPCallTreeFirstLevelReportTopDetails(getSeqNo());
					
			
			
			 //for file creation
			String filename="SP_CALL_TREE_FIRST_LEVEL_RPT_"+
								ToolsUtil.replaceNull((String)spCallTreeFirstLevelReportTopDataMap.get((String)"PROJECT_NAME"))+"_"+
								new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date())+
								".xls" ;
			String path=ToolsUtil.readProperty("excelReportPath");
			FileUploadDownloadUtility.createFolders(path);
			
			HSSFWorkbook hwb=new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet("new sheet");
			String colValue="Level";
			
		    
			HSSFRow rowhead= sheet.createRow((short)0);
			for (int i = 1; i < 11; i++) 
			{
			rowhead= sheet.createRow((short)i);
			rowhead.createCell((short) 0).setCellValue(i+"");
			rowhead.createCell((short) 1).setCellValue(colValue+i);
			rowhead.createCell((short) 2).setCellValue(colValue+i);
			rowhead.createCell((short) 3).setCellValue(colValue+i);
			rowhead.createCell((short) 4).setCellValue(colValue+i);
			rowhead.createCell((short) 5).setCellValue(colValue+i);
			rowhead.createCell((short) 6).setCellValue(colValue+i);
		    }
			
			
			
			
			List spCallTreeFirstLevelReportList = lSPMigrationReportsDAO.getManualModificationReportList(getProjectId(), getAnalysisType(),ToolsUtil.replaceNull((String)manualModificationReportTopDataMap.get((String)"ANALYSIS_MODE")),getSeqNo() );
			
			
			
			
			SPCallTreeFirstLevelDTO lSPCallTreeFirstLevelDTO  = new SPCallTreeFirstLevelDTO();
			
			if(spCallTreeFirstLevelReportList != null && spCallTreeFirstLevelReportList.size() > 0 ){
				for(int i=0;i<spCallTreeFirstLevelReportList.size();i++){
					lSPCallTreeFirstLevelDTO  =(SPCallTreeFirstLevelDTO)spCallTreeFirstLevelReportList.get(i);
					
					
					HSSFRow row= sheet.createRow((short)(i+1));
					row.createCell((short) 0).setCellValue((i+1)+"");
					row.createCell((short) 1).setCellValue(lSPCallTreeFirstLevelDTO .getProcName());
					row.createCell((short) 2).setCellValue(lSPCallTreeFirstLevelDTO .getFirstLevel());
					
					
				}
			}
			//getting list from db
			
			

			

			FileOutputStream fileOut = new FileOutputStream(path+filename);
			hwb.write(fileOut);
			fileOut.close();
			System.out.println("Your excel file has been generated!");

			 //for file creation
			 //for file download
			 FileUploadDownloadUtility.downloadFile(filename,path,ServletActionContext.getResponse());
			 //for file download
			 
			 
			 
			 
			 //res.sendRedirect("/index.jsp");

			 
			 return null;
		}
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/***************************************for report 2 - sp pattern report - start*************************************************/
	if("detailedSummaryReport".equalsIgnoreCase(getSubmitMode())){
		
		//patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
		setPatternAnalysisReportList(lSPMigrationReportsDAO.getSumaryBigReportList(getProjectId(),getMigrationMode(),getSeqNo()));
		//setPatternAnalysisReportList ( lSPMigrationReportsDAO.getPatternAnalysisReportList(getProjectId(), getAnalysisType(),ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"ANALYSIS_MODE")),getSeqNo() ));
		//setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		return "detailedSummaryReport";
	}
	/***************************************for report 2 - sp pattern report - end*************************************************/
	
	/***************************************for report 3 - sp pattern report - start*************************************************/
	if("compareSummaryReport".equalsIgnoreCase(getSubmitMode())){
		
		//patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
		setPatternAnalysisReportList(lSPMigrationReportsDAO.getSumaryBigReportList(getProjectId(),getMigrationMode(),getSeqNo()));
		//setPatternAnalysisReportList ( lSPMigrationReportsDAO.getPatternAnalysisReportList(getProjectId(), getAnalysisType(),ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"ANALYSIS_MODE")),getSeqNo() ));
		//setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		return "compareSummaryReport";
	}
	/***************************************for report 3 - sp pattern report - end*************************************************/
	
	/***************************************for report 3 - sp pattern report - start*************************************************/
	if("compareSummaryReportSpWiseCount".equalsIgnoreCase(getSubmitMode())){
		
		patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
		setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		//patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
		setPatternAnalysisReportList(lSPMigrationReportsDAO.getSPSumaryReportList(getProjectId(),getMigrationMode(),getSeqNo()));
		//setPatternAnalysisReportList ( lSPMigrationReportsDAO.getPatternAnalysisReportList(getProjectId(), getAnalysisType(),ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"ANALYSIS_MODE")),getSeqNo() ));
		//setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		return "compareSummaryReportSpWiseCount";
	}
	/***************************************for report 4 - sp pattern report - end*************************************************/
	
	/***************************************for report 3 - sp pattern report - start*************************************************/
	if("mismatchCategorySummeryReport".equalsIgnoreCase(getSubmitMode())){
		
		patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
		//setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		//patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
		setPatternAnalysisReportList(lSPMigrationReportsDAO.getMismatchCategorySummeryReport(getProjectId(),getMigrationMode(),getSeqNo(),"SYSBASE_TO_DB2"));
		//setPatternAnalysisReportList ( lSPMigrationReportsDAO.getPatternAnalysisReportList(getProjectId(), getAnalysisType(),ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"ANALYSIS_MODE")),getSeqNo() ));
		//setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		return "mismatchCategorySummeryReport";
	}
	/***************************************for report 4 - sp pattern report - end*************************************************/
	
	/***************************************for report # - DetailedMismatchProcWiseReport - start*************************************************/
	if("detailedMismatchProcWiseReport".equalsIgnoreCase(getSubmitMode())){
		
		patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
		//setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		//patternAnalysisReportTopDataMap = lSPMigrationReportsDAO.getPatternAnalysisReportTopDetails(getSeqNo());
		setPatternAnalysisReportList(lSPMigrationReportsDAO.getDetailedMismatchProcWiseReport(getProjectId(),getMigrationMode(),getSeqNo(),getProcName(),getMisMatchCategory()));
		//setPatternAnalysisReportList ( lSPMigrationReportsDAO.getPatternAnalysisReportList(getProjectId(), getAnalysisType(),ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"ANALYSIS_MODE")),getSeqNo() ));
		//setPatternAnalysisReportTopDataMap(patternAnalysisReportTopDataMap);
		return "detailedMismatchProcWiseReport";
	}
	/***************************************for report 4 - sp pattern report - end*************************************************/
if("exportToExcel".equalsIgnoreCase(getSubmitMode())){
		return "exportToExcel";
	}

	System.out.println(":::::after if conditions::::::");
	        // do the work
        return SUCCESS;
   }

	
	/***************form getter setter elements***************/
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
	 * @return the manualModificationReportList
	 */
	public List manualModificationReportList() {
		return manualModificationReportList;
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
	 * @param manualModificationReportList the manualModificationReportList to set
	 */
	public void setManualModificationReportList(List manualModificationReportList) {
		this.manualModificationReportList = manualModificationReportList;
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


	public HashMap getManualModificationReportTopDataMap() {
		return manualModificationReportTopDataMap;
	}


	public void setManualModificationReportTopDataMap(
			HashMap manualModificationReportTopDataMap) {
		this.manualModificationReportTopDataMap = manualModificationReportTopDataMap;
	}


	public HashMap getSpCallTreeFirstLevelReportTopDataMap() {
		return spCallTreeFirstLevelReportTopDataMap;
	}


	public List getSpCallTreeFirstLevelReportList() {
		return spCallTreeFirstLevelReportList;
	}


	public void setSpCallTreeFirstLevelReportList(
			List spCallTreeFirstLevelReportList) {
		this.spCallTreeFirstLevelReportList = spCallTreeFirstLevelReportList;
	}


	public void setSpCallTreeFirstLevelReportTopDataMap(
			HashMap spCallTreeFirstLevelReportTopDataMap) {
		this.spCallTreeFirstLevelReportTopDataMap = spCallTreeFirstLevelReportTopDataMap;
	}


	public List getManualModificationReportList() {
		return manualModificationReportList;
	}


	public int getSeqNoI() {
		return seqNoI;
	}


	public void setSeqNoI(int seqNoI) {
		this.seqNoI = seqNoI;
	}


	public String getSearchedString() {
		return searchedString;
	}


	public void setSearchedString(String searchedString) {
		this.searchedString = searchedString;
	}


	public ArrayList<SearchSPDTO> getSearchedList() {
		return searchedList;
	}


	public void setSearchedList(ArrayList<SearchSPDTO> searchedList) {
		this.searchedList = searchedList;
	}


	public HashMap getSpSearchReportTopDataMap() {
		return spSearchReportTopDataMap;
	}


	public void setSpSearchReportTopDataMap(HashMap spSearchReportTopDataMap) {
		this.spSearchReportTopDataMap = spSearchReportTopDataMap;
	}


	public String getSearchedString2() {
		return searchedString2;
	}


	public void setSearchedString2(String searchedString2) {
		this.searchedString2 = searchedString2;
	}


	
	
	
	

}
