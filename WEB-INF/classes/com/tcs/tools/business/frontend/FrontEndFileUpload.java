package com.tcs.tools.business.frontend;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts2.ServletActionContext;

import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class FrontEndFileUpload {
	
	public List intiateFilParsing(String pProjectId,String pFolderPath,String pRootFolderPath,String pSourceCodeType,List pExcelMainList,Connection pConnection){
		String pRunSeq="0";
		pFolderPath=pFolderPath.trim();
		pRootFolderPath=pRootFolderPath.trim();
		FrontEndParseForDSql lFrontEndParseForDSql=new  FrontEndParseForDSql();
		File ldirpath=new File(pFolderPath);
		File[] lFilesAndDirs = ldirpath.listFiles();
		//System.out.println(pFolderPath+"::No of Files::"+lFilesAndDirs.length);
		for (File file:lFilesAndDirs) {
			
		
			//System.out.println("directory chk"+lFilesAndDirs[i].getName()+"::->"+lFilesAndDirs[i].isDirectory());
			//System.out.println("file chk"+lFilesAndDirs[i].getName()+"::->"+lFilesAndDirs[i].isFile());
			if(file.isDirectory()){
				System.out.println("name of file  "+file);
				//intiateFilParsing(pProjectId,lFilesAndDirs[i].getPath(),pRootFolderPath);				
				//System.out.println("Cur Path:::"+lFilesAndDirs[i].getPath());
				pExcelMainList=lFrontEndParseForDSql.startFileParse(pProjectId,pRunSeq,file.getPath(),pRootFolderPath,pSourceCodeType,pExcelMainList,pConnection);
				//intiateFilParsing(pProjectId,file.getPath(),pRootFolderPath,pSourceCodeType,pExcelMainList,pConnection);				
			}
			
		}
		return pExcelMainList;
		
		
	}
	
	public static void main(String[] args){
		System.out.println("test main::::::::::"
				+ new Timestamp(System.currentTimeMillis()));
		FrontEndFileUpload lFrontEndFileUpload=new FrontEndFileUpload();
		String pProjectId="PORJ_UVC_REPORT_2";
		//String pRootFolderPath="C:\\Praveen\\Source of Prudential DCMS\\Java";
		//String pRootFolderPath="C:\\Praveen\\Tool Data\\Liberty_Mutual\\Source Code";//\\Java_application";
		//String pRootFolderPath="C:\\arun\\documents\\project\\UVC PXB\\Source Code\\Combined\\unzipped\\PSBCSR_Workspace\\";
		String pRootFolderPath="C:\\arun\\documents\\project\\UVC PXB\\Source Code\\Combined\\unzipped\\UVC_workspace\\workspace";
		//String pRootFolderPath="C:\\arun\\documents\\project\\Test Run\\front end\\java\\unzipped\\Single\\";
		
		String pSourceCodeType="JAVA";
		FrontEndParseForDSql lFrontEndParseForDSql=new  FrontEndParseForDSql();
		List lExcelMainList=new ArrayList();
		Connection lConnection = DBConnectionManager.getConnection(); 
		try {
			lConnection.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		lFrontEndFileUpload.intiateFilParsing(pProjectId,pRootFolderPath, pRootFolderPath,pSourceCodeType,lExcelMainList,lConnection);
		try {
			if(lConnection.getAutoCommit()==false){
				lConnection.commit();
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//lFrontEndParseForDSql.startFileParse(pProjectId,"0",pRootFolderPath,pRootFolderPath);
		//System.out.println("Parent Over:::");
		//For Report - Start
		
		List lExcelRowList=new ArrayList();			
		lExcelRowList.add("Folder Path");
		lExcelRowList.add("File Name");
		lExcelRowList.add("Line No");
		lExcelRowList.add("Dynamic Sql");
		lExcelMainList.add(lExcelRowList);
		
		//For Report - End
		
		
		
		//For Report - Start
				for (int i = 0; i < lExcelMainList.size(); i++) {
					 lExcelRowList =(List)lExcelMainList.get(i);
					System.out.println("");
					for (int j = 0; j < lExcelRowList.size(); j++) {
						System.out.print(lExcelRowList.get(j)+" --- ");
					}
				}
				String path="C:\\arun\\Tool Output\\UVC_PSB\\JAVA_REPORTS\\";
				FileUploadDownloadUtility.createFolders(path);
				System.out.println(":::::;lExcelMainList size"+lExcelMainList.size());
				FileUploadDownloadUtility.downloadListAsExcelFile(lExcelMainList, path, "Java_Pattern_Summery_one.xls", null);
				
				//For Report - End
	
		System.out.println("test main:done::::::"
				+ new Timestamp(System.currentTimeMillis()));
	}

}
