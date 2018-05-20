package com.tcs.tools.web.idmt;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcs.tools.web.util.ExcelSheetReader;
import com.tcs.tools.web.util.ToolsUtil;
import com.tcs.tools.web.util.ZipUtil;

public class CsToolSplitDDL {
	public String getTablenamesSpaceList(List pTableNamesList,String pTableName){
		List lInnerList = new ArrayList();
		if(pTableNamesList != null && pTableNamesList.size() >0){
			for (int i = 0; i < pTableNamesList.size(); i++) {
				lInnerList = (List)pTableNamesList.get(i);
				if(((String)lInnerList.get(1)).equals(pTableName)){
					return (String)lInnerList.get(2);
				}
				
			}
		}
		return "NAME_SPACE_NOTFOUND";
	}
	
	 public String  split_Files(String pFolderPath,String pFileName,String pOutputFolder,String pExcelFileFullPath){
		 String lOutputFile ="";
		 try{
			
			 List lTableNamesList = ExcelSheetReader.readExcelFile(pExcelFileFullPath);
		 File file = new File(pFolderPath+pFileName);
    	 InputStream is = new FileInputStream(file);
			StringBuffer buffer = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = is.read(b)) != -1;) {
				
				buffer.append(new String(b, 0, n));
			}
			
         String content = buffer.toString();
         //System.out.println("::::before line count"+ content.split("[\\r\\n]+").length);
        // content=content.replaceAll(";", ";\r\n");
        // System.out.println("::::after line count"+ content.split(";[\\r\\n]*").length);
         
         //String[] lines = content.split("\\n\\r+");
         content=content.replaceAll(";[\\r\\n]*", "; _DBT_DELIM");
         content=content.replaceAll("(?i)\\bGO\\b[\\s\\r\\n]+", "go _DBT_DELIM");
         String[] lines = content.split("\\s*_DBT_DELIM");
         String lFileName ="";
         
         
         
         Matcher lMatcher = null;
         
         Pattern lCreateIndexPattern = Pattern.compile("(?i)\\bCREATE\\b[\\w\\r\\n\\s]+\\bINDEX\\b\\s+(.+?)\\bON\\b\\s+(.+?)\\s+");
         Pattern lAlterTablePattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b\\s+(.+?)\\s+[\\W\\w\\r\\n\\s]+");
         Pattern lCreateTablePattern = Pattern.compile("(?i)\\bCREATE\\b[\\r\\n\\s]+\\bTABLE\\b\\s+(.+?)\\s+[\\W\\w\\r\\n\\s]+");
         
         Pattern lAlterTableCheckConstraintPattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b\\s+(.+?)\\s+[\\W\\w\\r\\n\\s]*\\s+(.+?)\\s+\\bCHECK\\b");
         Pattern lAlterTableForeignKeyPattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b\\s+(.+?)\\s+[\\W\\w\\r\\n\\s]+[\\W\\w\\r\\n\\s]*\\s+\\bFOREIGN\\b\\s+\\bKEY\\b\\s+(.+?)[\\s\\(]+");
         Pattern lAlterTableForeignKeyConstraintPattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b\\s+(.+?)\\s+[\\W\\w\\r\\n\\s]*\\s+(.+?)\\s+\\bFOREIGN\\b\\s+\\bKEY\\b");
         Pattern lAlterTablePrimaryConstraintPattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b\\s+(.+?)\\s+[\\W\\w\\r\\n\\s]*\\s+(.+?)\\s+\\bPRIMARY\\b\\s+\\bKEY\\b");
         
         String lObjectType ="";
         String lFileExtn ="";
         
         String lObjectName="";
         String lTableName="";
         
         boolean lCheckCondition =true;
         //Matcher lAlterTableMatcher = null;
         String lTableNameSpace ="";
         for (int i = 0; i < lines.length; i++){
        	 
        	 lCheckCondition = true;
        	 lTableNameSpace = "";
        	 lMatcher = lCreateIndexPattern.matcher(lines[i].trim());
        	 if(lMatcher.find()){
        		 //System.out.println(":::Create index name::;"+lMatcher.group(1)+":::table name::::"+lMatcher.group(2));
        		 lObjectType ="Ix";
        		 lFileExtn = "ix";
        		 lObjectName =lMatcher.group(1);
        		 lTableName = lMatcher.group(2);
        	 }
        	 
        	 lMatcher = lCreateTablePattern.matcher(lines[i].trim());
        	 if(lMatcher.find()){
        		 //System.out.println(":::create Table name::;"+lMatcher.group(1));
        		 lObjectType ="tbl";
        		 lFileExtn = "tbl";
        		 lObjectName =lMatcher.group(1);
        		 lTableName = lMatcher.group(1);
        		 
        		 //get the names space 
        		 lTableNameSpace = getTablenamesSpaceList(lTableNamesList,lObjectName);
        		 //remove ; from the string , we will add at the last stage
        		 if(lines[i].contains(";")){
        			 lines[i]  = lines[i].substring(0, lines[i].lastIndexOf(";"));
        		 }
        		 //add a static text for create able scripts
        		 //lines[i] += " IN "+lTableNameSpace+" \n " + " INDEX IN " + lTableNameSpace+" \n " + " COMPRESS YES" +";";
        		 lines[i] += " IN "+lTableNameSpace+"  " + " INDEX IN " + lTableNameSpace+"  " + " COMPRESS YES" +";";
        	 }
        	 
        	 
        	 //check
        	 lMatcher = lAlterTableCheckConstraintPattern.matcher(lines[i].trim());
        	 if(lMatcher.find()){
        		 //System.out.println(":::alter check constraint Table name::;"+lMatcher.group(1)+":::constaint name:::"+lMatcher.group(2));
        		 lCheckCondition = false;
        		 lObjectType ="ck";
        		 lFileExtn = "cc";
        		 
        		 lObjectName =lMatcher.group(2);
        		 lTableName = lMatcher.group(1);
        	 }
        	 
        	 //forign key
        	 lMatcher = lAlterTableForeignKeyConstraintPattern.matcher(lines[i].trim());
        	 if(lMatcher.find()){
        		 //System.out.println(":::alter forign constraint Table name::;"+lMatcher.group(1)+":::constaint name:::"+lMatcher.group(2));
        		 lCheckCondition = false;
        		 lObjectType ="fk";
        		 lFileExtn = "fk";
        		 lObjectName =lMatcher.group(2);
        		 lTableName = lMatcher.group(1);
        	 }
        	 
        	 //primary key
        	 lMatcher = lAlterTablePrimaryConstraintPattern.matcher(lines[i].trim());
        	 if(lMatcher.find()){
        		 //System.out.println(":::alter check constraint Table name::;"+lMatcher.group(1)+":::constaint name:::"+lMatcher.group(2));
        		 lCheckCondition = false;
        		 lObjectType ="pk";
        		 lFileExtn = "pk";
        		 lObjectName =lMatcher.group(2);
        		 lTableName = lMatcher.group(1);
        	 }
        	 
        	 //forign key
        	 lMatcher = lAlterTableForeignKeyPattern.matcher(lines[i].trim());
        	 if(lMatcher.find() && lCheckCondition==true){
        		 //System.out.println(":::alter foreign key Table name::;"+lMatcher.group(1)+":::constaint name:::"+lMatcher.group(2));
        		 lCheckCondition = false;
        		 lObjectType ="fk";
        		 lFileExtn = "fk";
        		 lObjectName =lMatcher.group(2);
        		 lTableName = lMatcher.group(1);
        	 }
        	 
        	 //alter table
        	 lMatcher = lAlterTablePattern.matcher(lines[i].trim() );
        	 if(lMatcher.find() && lCheckCondition==true){
        		 //System.out.println(":::alter  Table name::;"+lMatcher.group(1));
        		 lCheckCondition = true;
        		 lObjectType ="tbl";
        		 lFileExtn = "tbl";
        		 lObjectName =lMatcher.group(1);
        		 lTableName = lMatcher.group(1);
        	 }
        	 
        	 
        	 lObjectType = lObjectType.trim();
    		 lFileExtn = lFileExtn.trim();
    		 lObjectName =lObjectName.trim();
    		 lTableName = lTableName.trim();
        	 //System.out.println(lObjectType+"."+lTableName+"."+lObjectName+"."+lFileExtn);
    		 System.out.println(":::lines val:::"+lines[i]);
    		 if(!"".equals(lTableName)){
    			 ToolsUtil.writeToFile(lines[i], pOutputFolder+lObjectType+"."+lTableName+"."+lObjectName+"."+lFileExtn,pOutputFolder);
    		 }
         }
         
         lOutputFile = ZipUtil.zipFolder(pOutputFolder, pOutputFolder.substring(0, pOutputFolder.length()-1)+".zip"); 
     }
     catch (Exception e)
     {
         e.printStackTrace();
     }
     finally
     {
         
     }
 	return lOutputFile;
      }

	
	
	public static void main(String args[]){
		System.out.println(":::::inside main:::");
		CsToolSplitDDL lCsToolSplitDDL = new CsToolSplitDDL();
		
		 String pFolderPath="C:\\arun\\documents\\project\\idmt integration\\work\\split ddls\\errors\\";
		 String pFileName="gxprd01_DDL.src";
		 String pOutputFolder = pFolderPath+"\\SPLIT_FILES\\";
		 String pExcelFileFullPath ="C:\\arun\\documents\\project\\idmt integration\\work\\split ddls\\Untitled 2.xls";
		 
		 lCsToolSplitDDL.split_Files( pFolderPath, pFileName, pOutputFolder,pExcelFileFullPath);
		 
	}
}
