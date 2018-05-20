package com.tcs.tools.business.analysis.main;

import java.sql.Connection;
import java.sql.Timestamp;

import com.tcs.tools.business.compare.FileCompare;
import com.tcs.tools.business.compare.dao.ComparisonSummeryDAO;
import com.tcs.tools.business.compare.dao.DeepDiveComparisonDAO;
import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class SequentialRunning {
	//String pDbMigrationType ="SYSBASE_TO_DB2";
	String lCurState="";
	String lStausMsg="";
	
	//String pDbMigrationType ="SYSBASE_TO_DB2";
	public void callSequentialFunctions(String pSybasePath,String pTargetPath,String pDbMigrationType,String pProjectId,String pCreatedBy,String pAnalysysMode,String pProjectRunSeq,Connection pConnection){
		UserInterFaceOpening lUserInterFaceOpening = new UserInterFaceOpening();
		FileCompare lFileCompare = new FileCompare();
		/*String pSybasePath="C:\\arun\\documents\\project\\Tool Input\\small\\1\\before";
		String pDB2Path = "C:\\arun\\documents\\project\\Tool Input\\small\\1\\after";*/
		//String pSybasePath="C:\\arun\\documents\\project\\Test Run\\80 procs\\before";
		//String pDB2Path="C:\\arun\\documents\\project\\Test Run\\80 procs\\after";
		System.out.println("::::pDbMigrationType::::"+pDbMigrationType+"::::pAnalysysMode:::"+pAnalysysMode);
		if(pDbMigrationType.equalsIgnoreCase("SYSBASE_TO_DB2") || pDbMigrationType.equalsIgnoreCase("SYBASE_TO_SQL")||pDbMigrationType.equalsIgnoreCase("SYSBASE_TO_Oracle")){
			  
			//Update Status to Front End - Start
			 lCurState="Selected Mode";
			 lStausMsg=":::::::::::::: Selected Mode -----> "+pAnalysysMode+" :::::::::::::: ";			
			ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),pConnection);
			//Update Status to Front End - End
			
			if("SOURCE".equalsIgnoreCase(pAnalysysMode)){
				
				
				//Update Status to Front End - Start
				lCurState="Uploading";
				lStausMsg="Uploading Source Data... ";				
				ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),pConnection);
				//Update Status to Front End - End
				
				 String lRunIdSybase = lUserInterFaceOpening.invokeFileUpload(pSybasePath,pProjectId+"_SOURCE" ,pDbMigrationType);
				 
				//Update Status to Front End - Start
				 lCurState="Pattern Analysis";
				 lStausMsg="Intiating Source Pattern Analysis... ";
				 ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),pConnection);
				 //Update Status to Front End - End
					
				 System.out.println(":::lRunId-Sybase:::"+lRunIdSybase);
				 lUserInterFaceOpening.invokeParsedDataInsert(lRunIdSybase,pSybasePath, pDbMigrationType);
				// lUserInterFaceOpening.invokeSpCallFirstLevel(lRunIdSybase,pSybasePath, pDbMigrationType);
			}else if("TARGET".equalsIgnoreCase(pAnalysysMode)){
				
				//Update Status to Front End - Start
				lCurState="Uploading";
				lStausMsg="Uploading Target Data... ";				
				ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),pConnection);
				//Update Status to Front End - End
				
				String lRunIdTarget = lUserInterFaceOpening.invokeFileUpload(pTargetPath,pProjectId+"_TARGET", pDbMigrationType);
				
				//Update Status to Front End - Start
				 lCurState="Pattern Analysis";
				 lStausMsg="Intiating Target Pattern Analysis... ";
				 ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),pConnection);
				 //Update Status to Front End - Ends
				
				System.out.println(":::lRunId-Sybase:::"+lRunIdTarget);
				lUserInterFaceOpening.invokeParsedDataInsert(lRunIdTarget,pTargetPath, pDbMigrationType);
			//	lUserInterFaceOpening.invokeSpCallFirstLevel(lRunIdTarget,pSybasePath, pDbMigrationType);
			}else if("BOTH".equalsIgnoreCase(pAnalysysMode)){
				

				  //Update Status to Front End - Start
				  lCurState="Uploading";
				  lStausMsg="Uploading Source Data... ";				
				  ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),pConnection);
				  //Update Status to Front End - End				
				  String lRunIdSybase = lUserInterFaceOpening.invokeFileUpload(pSybasePath,pProjectId+"_SOURCE" ,pDbMigrationType);
				  System.out.println(":::lRunId-Sybase:::"+lRunIdSybase);
				  
				  //Update Status to Front End - Start
				  lCurState="Pattern Analysis";
				  lStausMsg="Intiating Source Pattern Analysis... ";
				  ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),pConnection);
				  //Update Status to Front End - End				  
				  lUserInterFaceOpening.invokeParsedDataInsert(lRunIdSybase,pSybasePath, pDbMigrationType);
				//  lUserInterFaceOpening.invokeSpCallFirstLevel(lRunIdSybase,pSybasePath, pDbMigrationType);
				  //***********************************
				  //Update Status to Front End - Start
				  lCurState="Uploading";
				  lStausMsg="Uploading Target Data... ";				
				  ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),pConnection);
				  //Update Status to Front End - End
					
				  String lRunIdTarget = lUserInterFaceOpening.invokeFileUpload(pTargetPath,pProjectId+"_TARGET", pDbMigrationType);			  
				  
				  System.out.println(":::lRunId-Sybase:::"+lRunIdTarget);
				  
				  //Update Status to Front End - Start
				  lCurState="Pattern Analysis";
				  lStausMsg="Intiating Target Pattern Analysis... ";
				  ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),pConnection);
				  //Update Status to Front End - Ends
					 
				  lUserInterFaceOpening.invokeParsedDataInsert(lRunIdTarget,pTargetPath, pDbMigrationType);
				//  lUserInterFaceOpening.invokeSpCallFirstLevel(lRunIdTarget,pSybasePath, pDbMigrationType);
				  
				  //***********************************
				  //Update Status to Front End - Start
				  lCurState="Source to Target Mapping";
				  lStausMsg="Mapping Source Data to Target... ";
				  ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),pConnection);
				  //Update Status to Front End - Ends
				  String lCompareSeq="";
				  if(pDbMigrationType.equalsIgnoreCase("SYSBASE_TO_DB2")){
				  lCompareSeq = lFileCompare.compareTwoProcs(lRunIdSybase, lRunIdTarget, pDbMigrationType,pProjectId,pProjectRunSeq);
				  System.out.println("::::::::lCompareSeq::::::"+lCompareSeq);
				  }
				  if(pDbMigrationType.equalsIgnoreCase("SYSBASE_TO_Oracle")){
					 lCompareSeq = lFileCompare.compareTwoProcsSybaseToOracle(lRunIdSybase, lRunIdTarget, pDbMigrationType,pProjectId,pProjectRunSeq);
					 // lCompareSeq = lFileCompare.compareTwoProcs(lRunIdSybase, lRunIdTarget, pDbMigrationType,pProjectId,pProjectRunSeq);
					  System.out.println("::::::::lCompareSeq::::::"+lCompareSeq);
				  }
				  //**************************************
				 
				  //Update Status to Front End - Start
				  lCurState="Analysing Gaps";
				  lStausMsg="Comparing Source with Target & Analysing Gaps...";
				  ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),pConnection);
				  //Update Status to Front End - Ends
				  
				  DeepDiveComparisonDAO lDeepDiveComparisonDAO = new DeepDiveComparisonDAO();
				  lDeepDiveComparisonDAO.statementCompare(lCompareSeq, pDbMigrationType);
				  
				  System.out.println("::::::::lDeepDiveComparisonDAO over111111::::::"+lCompareSeq);
				  
				  //**************************************
				  //Update Status to Front End - Start
				  lCurState="Populating Mismatch Summery";
				  lStausMsg="Populating Mismatch Summery...";
				  ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),pConnection);
				  //Update Status to Front End - Ends
				  
				  ComparisonSummeryDAO lKeywordCompareDAO=new ComparisonSummeryDAO();
				  lKeywordCompareDAO.prepareSummery(lCompareSeq,pCreatedBy);
				  System.out.println("::::::::ComparisonSummeryDAO over::::::"+lCompareSeq);
			  }
				  
			  ProjectModifyDAO lProjectModifyDAO = new ProjectModifyDAO();
			  lProjectModifyDAO.updateProjectDataUploadStatus(pProjectId,"Data Upload Complete");
			
		}else if(pDbMigrationType.equalsIgnoreCase("TEST_SYBASE_TO_SQL")){
			
			  String lRunIdSybase = lUserInterFaceOpening.invokeFileUpload(pSybasePath,pProjectId+"_SOURCE" ,pDbMigrationType);
			  System.out.println(":::lRunId-Sybase:::"+lRunIdSybase);
			  lUserInterFaceOpening.invokeParsedDataInsert(lRunIdSybase,pSybasePath, pDbMigrationType);
			 // lUserInterFaceOpening.invokeSpCallFirstLevel(lRunIdSybase,pSybasePath, pDbMigrationType);
			  /*String lRunIdDB2 = lUserInterFaceOpening.invokeFileUpload(pDB2Path,pProjectId+"_TARGET", pDbMigrationType);
			  System.out.println(":::lRunId-Sybase:::"+lRunIdDB2);
			  lUserInterFaceOpening.invokeParsedDataInsert(lRunIdDB2,pDB2Path, pDbMigrationType);
			  
			  String lCompareSeq = lFileCompare.compareTwoProcs(lRunIdSybase, lRunIdDB2, pDbMigrationType,pProjectId);
			  System.out.println("::::::::lCompareSeq::::::"+lCompareSeq);
			  
			  DeepDiveComparisonDAO lDeepDiveComparisonDAO = new DeepDiveComparisonDAO();
			  lDeepDiveComparisonDAO.statementCompare(lCompareSeq, pDbMigrationType);
			  
				ComparisonSummeryDAO lKeywordCompareDAO=new ComparisonSummeryDAO();
				lKeywordCompareDAO.prepareSummery(lCompareSeq,pCreatedBy);*/
			  
			  ProjectModifyDAO lProjectModifyDAO = new ProjectModifyDAO();
			  lProjectModifyDAO.updateProjectDataUploadStatus(pProjectId,"Data Upload Complete");
			
		}
		
		  
		  
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SequentialRunning lSequentialRunning = new SequentialRunning();
		String pSybasePath="C:\\arun\\documents\\project\\Test Run\\13 procs\\before";
		String pDB2Path="C:\\arun\\documents\\project\\Test Run\\13 procs\\after";
		String pDbMigrationType="SYSBASE_TO_DB2";
		String pProjectId="ARPR004";
		lSequentialRunning.callSequentialFunctions(pSybasePath, pDB2Path,pDbMigrationType,pProjectId,"PruUser","BOTH",null,DBConnectionManager.getConnection());

	}



}
