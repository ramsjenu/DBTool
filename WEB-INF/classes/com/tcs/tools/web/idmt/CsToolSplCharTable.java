package com.tcs.tools.web.idmt;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.tcs.tools.web.util.ExcelSheetReader;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class CsToolSplCharTable {
	
	  public void generate_CharFile(String pPath,/*String pFileName,*/String pExcelFileFullPath){
		  try{
			  List lOutputList = new ArrayList();
			  List lOutputInnerList = new ArrayList();
			  List lDataList = ExcelSheetReader.readExcelFile(pExcelFileFullPath);
			  List lInnerList = new ArrayList();
			  
			  				
			  lOutputInnerList = new ArrayList();
			  lOutputInnerList.add("Schema");
			  lOutputInnerList.add("Table Name");
			  lOutputInnerList.add("Column Name");
			  lOutputInnerList.add("Datatype");
			  lOutputInnerList.add("Result");
			  lOutputList.add(lOutputInnerList);
			  if(lDataList != null && lDataList.size() > 0){
				  for (int i = 0; i < lDataList.size(); i++) {
					  if( i == 0) continue;
					  lInnerList = (List)lDataList.get(i);
					  lOutputInnerList = new ArrayList();
					  lOutputInnerList.add("dbo");
					  lOutputInnerList.add(lInnerList.get(0));
					  lOutputInnerList.add(lInnerList.get(1));
					  lOutputInnerList.add(lInnerList.get(2));
					  lOutputInnerList.add("dbo."+lInnerList.get(0)+"."+lInnerList.get(1)+"=n"+lInnerList.get(2));
					  lOutputList.add(lOutputInnerList);
					  //System.out.println(":::lOutputInnerList:::"+lOutputInnerList.size()+lOutputInnerList.toString());
				}
			  }
			  
			  //FileUploadDownloadUtility.createFolders(pPath);
			  String[] lFilenamePathArr = ToolsUtil.splitFileNameAndPath( pExcelFileFullPath);
	             String[] lRetArr =  ToolsUtil.splitFileNameAndExtension(lFilenamePathArr[1]);
	             //FileOutputStream fileOut = new FileOutputStream(pPath+lRetArr[0]+"_DEFAULTS.xls");
	             String lTargetFilename=lRetArr[0]+"_SPECIALCHARSEXCEL.xls";
				
					
			  System.out.println("::::file will be created in this::::"+pPath+lFilenamePathArr[1]);
			  FileUploadDownloadUtility.downloadListAsExcelFile(lOutputList,pPath,lTargetFilename,ServletActionContext.getResponse());
		  }catch (Exception e) {
			e.printStackTrace();
		}
	  }

	public static void main(String args[]){
		System.out.println("::::");
		CsToolSplCharTable lCsToolSplCharTable = new CsToolSplCharTable();
		 String pFolderPath="C:\\arun\\documents\\project\\idmt integration\\work\\spl char table\\";
		 String pFileName="output.xls";
		 String pOutputFolder = pFolderPath+"\\SPLIT_FILES\\";
		 String pExcelFileFullPath ="C:\\arun\\documents\\project\\idmt integration\\work\\spl char table\\untitled.xls";
		 
		lCsToolSplCharTable.generate_CharFile(pFolderPath,/*pFileName,*/ pExcelFileFullPath);
	}
}
