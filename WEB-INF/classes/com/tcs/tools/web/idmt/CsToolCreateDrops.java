package com.tcs.tools.web.idmt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.List;

import com.tcs.tools.web.util.ExcelSheetReader;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class CsToolCreateDrops {

	 public String generate_Drop (String pFolderPath ,String pFileName,String pOutpurFolderpath )
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
             //content=content.replaceAll(";[\\r\\n]*", "; _DBT_DELIM");
             
             
            
             String str = null;
             String[] lines = content.split("\\s+");
        
             for(int i=0;i<lines.length;i++)                    
             {
                 if (!lines[i].trim().equals("\\"))
                 {
                	 String[] splittedLines = lines[i].split("\\.");
                     
                    
                     if (lines[i].contains("ix."))
                         str = "DROP INDEX ";
                     else
                        str = "ALTER TABLE " + splittedLines[1] + " DROP CONSTRAINT ";

                     //lines[i] = lines[i].Remove(0, lines[i].lastIndexOf(".")+1);
                     
                     lines[i] = str + splittedLines[2]+";";
                 }
                 else
                     break;

                
             }
             
             
             String[] lRetArr =  ToolsUtil.splitFileNameAndExtension(pFileName);
             lOutputFile = pOutpurFolderpath+lRetArr[0]+"_drop"+"."+lRetArr[1];
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(":::::");
		 String pFolderPath="C:\\arun\\documents\\project\\idmt integration\\work\\create drops\\";
		 String pFileName="input.txt";
		 
		 CsToolCreateDrops lCsToolCreateDrops = new CsToolCreateDrops();
		 lCsToolCreateDrops.generate_Drop(pFolderPath,pFileName,pFolderPath);
		 
	}

}
