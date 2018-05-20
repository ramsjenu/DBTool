package com.tcs.tools.web.idmt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class CsToolFixIdentity {

	
	 public String  set_FixIdentity(String pFolderPath,String pFileName,String pOutpurFolderpath)
    {
		 String lOutputFile ="";
	try
   {
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
        String[] lines = content.split("\\s*_DBT_DELIM");
        Pattern lGeneratePattern = Pattern.compile("(?i)\\bGENERATED\\b\\s+\\bBY\\b\\s+\\bDEFAULT\\b\\s+\\bAS\\b\\s+\\bIDENTITY\\b\\s+\\(\\s*\\bSTART\\b\\s+\\bWITH\\b\\s+\\b1\\b\\s*\\,\\s*\\bINCREMENT\\b\\s+\\bBY\\b\\s+\\b1\\b\\s*\\,\\s*\\bCACHE\\b\\s+\\b20\\b\\s*\\)");
        String lGenrateReplacement ="GENERATED ALWAYS AS IDENTITY NOT NULL ";
        
        
        Pattern lGetCreateTablePattern = Pattern.compile("(?i)\\bCREATE\\b\\s+\\bTABLE\\b(.+?)\\s*\\(");
        Matcher lTableNameMatcher=null;
        
        Pattern lGetApproxTableSizePattern = Pattern.compile("(?i)--\\s*\\bApproximate\\b\\s*\\bTable\\b\\s*\\bSize\\b(.+?)[\\r\\n\\s]+");
        Matcher lGetApproxTableSizeMatcher=null;
        
       
        //System.out.println(":::lCommentLine::::"+lCommentLine);
        String lTableName ="";
        String lApproxSizeData ="";
        for (int i = 0; i < lines.length; i++) {
        	/*if(lGeneratePattern.matcher(lines[i]).find()){
        		System.out.println(":::match found:::");
        	}*/
        	
        	lTableName ="";
        	lines[i] = lines[i].replaceAll("(?i)\\bGENERATED\\b\\s+\\bBY\\b\\s+\\bDEFAULT\\b\\s+\\bAS\\b\\s+\\bIDENTITY\\b\\s+\\(\\s*\\bSTART\\b\\s+\\bWITH\\b\\s+\\b1\\b\\s*\\,\\s*\\bINCREMENT\\b\\s+\\bBY\\b\\s+\\b1\\b\\s*\\,\\s*\\bCACHE\\b\\s+\\b20\\b\\s*\\)"
        									, lGenrateReplacement);
        	
        		lTableNameMatcher = lGetCreateTablePattern.matcher(lines[i]);
        		if(lTableNameMatcher.find()){
        			//System.out.println("::::in:::"+lTableNameMatcher.group(1));
        			lTableName = lTableNameMatcher.group(1);
        			//System.out.println("::::inside"+((Matcher)lGetCreateTablePattern.matcher(lines[i])).group(1));
        		}
        		
        		lGetApproxTableSizeMatcher = lGetApproxTableSizePattern.matcher(lines[i]);
        		if(lGetApproxTableSizeMatcher.find()){
        			lApproxSizeData = lGetApproxTableSizeMatcher.group(1);
        		}
        		//System.out.println(":::::lApproxSizeData:::"+lApproxSizeData);
        		//lGetApproxTableSizeMatcher.replaceAll(getCommentLine(lTableName, lApproxSizeData));
        		lines[i] = lines[i].replaceAll( "(?i)--\\s*\\bAPPROXIMATE\\b\\s*\\bTABLE\\b\\s*\\bSIZE\\b\\s*\\d+" , getCommentLine(lTableName, lApproxSizeData));
        }
        
        //finding and replacing contents in the file
        //GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1, CACHE 20)
        //GENERATED ALWAYS AS IDENTITY NOT NULL 
       
        String[] lRetArr =  ToolsUtil.splitFileNameAndExtension(pFileName);
        
        
        lOutputFile = pOutpurFolderpath+lRetArr[0]+"_modified"+"."+lRetArr[1];
        FileUploadDownloadUtility.createFolders(pOutpurFolderpath);
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(lOutputFile));
        System.out.println(lOutputFile);
        //writer.write(lCommentLine);
        //writer.newLine();
        
        for (int i = 0; i < lines.length;i++)
        {	String[] lSubLines =lines[i].trim().split("\\n+");
        		for (int j = 0; j < lSubLines.length; j++) {
        			writer.write(lSubLines[j].trim());
        			writer.newLine();
				}
        	 
             
        }
       
        writer.close();

   }catch (Exception e) {
	e.printStackTrace();
}
	       		
	       		
	       		
	   return lOutputFile;    		
      		
       	}
       	
       	
public String getCommentLine(String pTableName,String pApproxSize){
	pTableName = pTableName.replaceAll("(?i)dbo.", "");
	 String lCommentLine="--------------------------------------------------------------\n";
     lCommentLine += "---********************Change History ***********************-\n";
     lCommentLine +="---*****************SYBASE15 to UDB 9.7 Migration *************-\n";
     lCommentLine +="----Date:Oct 2011 --------------------------------------------\n";
     lCommentLine +="----TAG:TCS --------------------------------------------------\n";
     lCommentLine +="----Desc:Schema Migration ------------------------------------\n";
     lCommentLine +="----Table Name :"+pTableName+"\n";
     lCommentLine +="-- Approximate Table Size "+pApproxSize+ "\n";
     //original content-- Approximate Table Size 4

     
     lCommentLine +="--------------------------------------------------------------\n";
     return lCommentLine;
}
	 public static void main(String args[]){
		 String pFolderPath="C:\\arun\\documents\\project\\idmt integration\\work\\fix identity\\";
		 String pFileName="input.txt";
		 System.out.println(":::inside main:::");
		 CsToolFixIdentity lCsToolFixIdentity = new CsToolFixIdentity();
		 lCsToolFixIdentity.set_FixIdentity(pFolderPath, pFileName,pFolderPath);
		 System.out.println(":::::main over::::");
		 
		 
	 }


}
