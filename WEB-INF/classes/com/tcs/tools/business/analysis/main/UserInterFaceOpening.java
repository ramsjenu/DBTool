package com.tcs.tools.business.analysis.main;

import java.sql.Timestamp;

import com.tcs.tools.business.analysis.PatternAnalysis;
import com.tcs.tools.business.analysis.dao.PatternAnalysisDAO;
import com.tcs.tools.business.analysis.dao.PatternAnalysisSingleDAO;
import com.tcs.tools.business.fileupload.FileUpload;
import com.tcs.tools.business.inventory.analysys.SpCallTreeFirstLevelDAO;

public class UserInterFaceOpening  {

	//String pDbMigrationType ="SYSBASE_TO_DB2";
	//String pDbMigrationType="SYBASE_TO_SQL";
	
	public String invokeFileUpload(String pFileDiretory,String pProjectRunId,String pDbMigrationType){
		System.out.println("File Parsing Staring::::::::::"+ new Timestamp(System.currentTimeMillis()));
		 System.out.println("directory "+pFileDiretory);
		FileUpload lFileUpload = new FileUpload();
		String lRunId = lFileUpload.getFiles(pFileDiretory,pDbMigrationType,pProjectRunId);
		
		System.out.println("File Parsing Completed::::::"+ new Timestamp(System.currentTimeMillis()));
		return lRunId;
	}
	 
	public String invokeParsedDataInsert(String pRunId,String pFolderPath,String pDbMigrationType){
		System.out.println("Parsed Data Insert Staring::::::::::"+ new Timestamp(System.currentTimeMillis()));
		 System.out.println("::::pRunId::::"+pRunId);
		PatternAnalysis lPatternAnalysis = new PatternAnalysis();
		lPatternAnalysis.getPatternData(pRunId,pDbMigrationType,pFolderPath);
		PatternAnalysisSingleDAO pads = new PatternAnalysisSingleDAO();
		//pads.updatePatternException(pRunId);
		SpCallTreeFirstLevelDAO lSpCallTreeFirstLevelDAO=new SpCallTreeFirstLevelDAO();
		lSpCallTreeFirstLevelDAO.getdata(pRunId);
		System.out.println("Parsed Data Insert Completed::::::"+ new Timestamp(System.currentTimeMillis()));
		//PatternAnalysisDAO pad=new PatternAnalysisDAO();
		 //pad.getSummaryList(pRunId);//229-237
		 //System.out.println("Report Generated");
		return "";
	}
	
	/*
	public String invokeSpCallFirstLevel(String pRunId,String pFolderPath,String pDbMigrationType){
		//System.out.println("Parsed Data Insert Staring::::::::::"+ new Timestamp(System.currentTimeMillis()));
		// System.out.println("::::pRunId::::"+pRunId);
		//PatternAnalysis lPatternAnalysis = new PatternAnalysis();
		//lPatternAnalysis.getPatternData(pRunId,pDbMigrationType,pFolderPath);
		//PatternAnalysisSingleDAO pads = new PatternAnalysisSingleDAO();
		//pads.updatePatternException(pRunId);
		SpCallTreeFirstLevelDAO lSpCallTreeFirstLevelDAO=new SpCallTreeFirstLevelDAO();
		lSpCallTreeFirstLevelDAO.getdata(pRunId);
		//System.out.println("Parsed Data Insert Completed::::::"+ new Timestamp(System.currentTimeMillis()));
		//PatternAnalysisDAO pad=new PatternAnalysisDAO();
		 //pad.getSummaryList(pRunId);//229-237
		 //System.out.println("Report Generated");
		return "";
	}
	*/

}
