package com.tcs.tools.web.idmt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class CsToolRemoveSchema {

	
	 public String remove_Schema(String pFolderPath,String pFileName,String pSchemaName,String pOutpurFolderpath)
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
			
			pSchemaName ="."+pSchemaName; 
			
       String content = buffer.toString();
       //System.out.println("::::before line count"+ content.split("[\\r\\n]+").length);
       // content=content.replaceAll(";", ";\r\n");
       // System.out.println("::::after line count"+ content.split(";[\\r\\n]*").length);
       
       //String[] lines = content.split("\\n\\r+");
       content=content.replaceAll(";[\\r\\n]*", "; _DBT_DELIM");
       //content.replaceAll("."+pSchemaName, "");
       String[] lines = content.split("\\s*_DBT_DELIM");
       for (int i = 0; i < lines.length; i++) {
    	   lines[i] = lines[i].replaceAll("(?i)\\b"+pSchemaName+".\\b", " ");
       }
            
       
       String[] lRetArr =  ToolsUtil.splitFileNameAndExtension(pFileName);
       lOutputFile = pOutpurFolderpath+lRetArr[0]+"_removed"+"."+lRetArr[1];
       FileUploadDownloadUtility.createFolders(pOutpurFolderpath);
       
       BufferedWriter writer = new BufferedWriter(new FileWriter(lOutputFile));
       System.out.println(lOutputFile);
       //writer.write(lCommentLine);
       //writer.newLine();
       for (int i = 0; i < lines.length;i++)
       {
       	 writer.write(lines[i].trim());
            writer.newLine();
       }
      
       writer.close();

  }catch (Exception e) {
	e.printStackTrace();
}
	       		
	       		
	       		
	     return lOutputFile;  		
     		
      	}
      	
      	

	 public static void main(String args[]){
		 String pFolderPath="C:\\arun\\documents\\project\\idmt integration\\work\\remove schema\\";
		 String pFileName="input.txt";
		 System.out.println(":::inside main:::");
		 CsToolRemoveSchema lCsToolRemoveSchema = new CsToolRemoveSchema();
		 lCsToolRemoveSchema.remove_Schema(pFolderPath, pFileName,".dbo",pFolderPath);
		 System.out.println(":::::main over::::");
		 
		 
	 }


}
