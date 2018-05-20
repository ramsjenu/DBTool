package com.tcs.tools.web.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetInventoryFileTypeDetails {


	public static HashMap getInventory(String pFolderPath){
		
		List lAllFilesList=new ArrayList();
		List lFileTypesLst=new ArrayList();
		lFileTypesLst.add("xml");
		lFileTypesLst.add("config");
		lFileTypesLst.add("properties");
		lFileTypesLst.add("jsp");
		lFileTypesLst.add("java");
		
		lAllFilesList= ToolsUtil.getFileNamesFromFolder(new File(pFolderPath),lAllFilesList );
		HashMap lInventoryMap=new HashMap();
		String lFileName="";
		String lFileType="";
		Set set=null;
		Iterator iterator =null;
		int count=0;		
		for (int i = 0; i < lAllFilesList.size(); i++) {
			lFileName=(String)lAllFilesList.get(i);
			//System.out.println(lFileName);
			if(lFileName.contains(".")){
				lFileType=lFileName.substring(lFileName.lastIndexOf(".")+1,lFileName.length());
			}
			else{
				lFileType="NO_EXTENSION";
			}
			
			/*if(lFileTypesLst.contains(lFileType.toLowerCase().trim())){*/
				if(lInventoryMap.containsKey(lFileType.trim().toLowerCase())){
					count=Integer.parseInt(((String)lInventoryMap.get(lFileType.trim().toLowerCase())).trim());
					count=count+1;
					lInventoryMap.put(lFileType.trim().toLowerCase(),count+"");
				}else{
					lInventoryMap.put(lFileType.trim().toLowerCase(),"1");
				}
			/*}*/
				
			
		}
		System.out.println("Size::->"+lInventoryMap.size());
		/*set = lInventoryMap.entrySet();
		iterator = set.iterator(); 
		while(iterator.hasNext()) { 
			Map.Entry me = (Map.Entry)iterator.next();				
			System.out.println(me.getKey()+"::::Count::->"+me.getValue());
		}*/
		return lInventoryMap;
		
	}
	public static  void readFolders(String pIntputPath,String pReportOutputPath,String pReportFileName){
		File[] lFilesAndDirs = new File(pIntputPath).listFiles();
		List lHeaderList=new ArrayList();
		lHeaderList.add("Application Name");
		List lMainList=new ArrayList();
		List lRowList=new ArrayList();
		HashMap lFolderDataMap=null;
		Set set=null;
		Iterator iterator =null;
		for (int i = 0; i < lFilesAndDirs.length; i++) {
			if(lFilesAndDirs[i].isDirectory()){
				lRowList=new ArrayList();
				lRowList.add(lFilesAndDirs[i].getName());
				System.out.println(lFilesAndDirs[i].getPath());
				lFolderDataMap=getInventory(lFilesAndDirs[i].getPath());
				set = lFolderDataMap.entrySet();
				iterator = set.iterator(); 
				while(iterator.hasNext()) { 
					Map.Entry me = (Map.Entry)iterator.next();
					
						if(!lHeaderList.contains(((String)me.getKey()).toLowerCase())){
							lHeaderList.add(((String)me.getKey()).toLowerCase());
						}
						if(lHeaderList.indexOf(((String)me.getKey()).toLowerCase())<lRowList.size()){
							lRowList.set(lHeaderList.indexOf(((String)me.getKey()).toLowerCase()),(String)me.getValue());
						}else{
							for (int j = lRowList.size(); j < lHeaderList.indexOf(((String)me.getKey()).toLowerCase()); j++) {
								lRowList.add(j,"0");
							}
							lRowList.add(lHeaderList.indexOf(((String)me.getKey()).toLowerCase()),(String)me.getValue());
						}
					
				}
			}
			lMainList.add(lRowList);
		}
		System.out.println(lHeaderList);
		for (int i = 0; i < lMainList.size(); i++) {
			lRowList=(List)lMainList.get(i);
			if(lRowList.size()<lHeaderList.size()){
				for (int j = lRowList.size(); j < lHeaderList.size(); j++) {
					lRowList.add("0");
				}
			}
			lMainList.set(i, lRowList);
		}
		
		lMainList.add(0,lHeaderList);
		for (int i = 0; i < lMainList.size(); i++) {
			lRowList=(List)lMainList.get(i);			
			System.out.println(lRowList);
			/*for (int j = 0; j < lRowList.size(); j++) {
				System.out.println(lRowList.get(j));
			}*/
		}
		
		//for ordering
		/*List lOrderHeaderList=new ArrayList();		
		for (int i = 0; i < lHeaderList.size(); i++) {
			lOrderHeaderList.add("");
		}
		Collections.copy(lOrderHeaderList, lHeaderList);
		Collections.sort(lOrderHeaderList);
		HashMap lSorIndexPairMap=new HashMap();
		for (int i = 0; i < lHeaderList.size(); i++) {
			System.out.println(":::::lHeaderList-"+lHeaderList.get(i)+":::::lOrderHeaderList->"+lOrderHeaderList.get(i));
			for (int j = 0; j < lOrderHeaderList.size(); j++) {
				if( ((String)lOrderHeaderList.get(j)).equalsIgnoreCase((String)lHeaderList.get(i))){
					lSorIndexPairMap.put(j, i);
				}
			}
		}
		int curIndex=0;
		int afterIndex=0;
		String lTempStr="";
		List lNewRowList= new ArrayList();
		List lNewMainList=new ArrayList();		
		
		for (int i = 0; i < lMainList.size(); i++) {
			lNewRowList=(List)lMainList.get(i);			
			for (int j = 0; j < lNewRowList.size(); j++) {				
				afterIndex=(Integer)lSorIndexPairMap.get(j);
				//swapping
				lTempStr=(String)lNewRowList.get(j);
				lNewRowList.set(j,(String)lNewRowList.get(afterIndex));
				lNewRowList.set(afterIndex,lTempStr);
			}
			lNewMainList.add(i, lNewRowList);
		}*/
		
		
		
		
		FileUploadDownloadUtility.createFolders(pReportOutputPath);
		
		//FileUploadDownloadUtility.downloadListAsExcelFile(lMainList, pReportOutputPath, pReportFileName, null);
		CreateExcel.createExcel(lMainList, pReportOutputPath, pReportFileName, null);
		
	}
	
	public static void main(String [] args){
		//readFolders("C:\\Documents and Settings\\477780\\Desktop\\sample\\");
		//
		//String pSourcePath="C:\\arun\\documents\\project\\UVC PXB\\Source Code\\Combined\\PSBCSR_Workspace\\Workspace";
		String pSourcePath="C:\\arun\\documents\\project\\UVC PXB\\Source Code\\Combined\\UVC_workspace\\workspace\\";
		
		String pReportOutputPath="C:\\arun\\Tool Output\\UVC_PSB\\";
		String pReportFileName="InventoryDetails.xls";
		readFolders(pSourcePath,pReportOutputPath,pReportFileName);
	}
}
