package com.tcs.tools.web.idmt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;

import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class CsToolRemoveQuotes {
	 public String remove_Quotes(String pFolderPath,String pFileName,String pOutpurFolderpath)
     {
		 String lOutputFile="";
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
             String[] lines = content.split("\\n\\r+");
             
             /*for (int i = 0; i < lines.length; i++){
            	 
            	 if (lines[i].contains(" CHECK ")){
                     lines[i] = lines[i].replaceAll("\"", "'");
                 }else{
                	 lines[i] = lines[i].replaceAll("\"", "");
                 }
            	 
            	 lines[i] = lines[i].replaceAll("\"", "");
            	 
             }*/

            for (int i = 0, k = 0; i < lines.length; i++, k = 0){

                 while (k < lines[i].length()){
                     if (0 <= lines[i].indexOf("\"", k)) //, StringComparison.InvariantCultureIgnoreCase
                     {
                         k = lines[i].indexOf("\"", k); //, StringComparison.InvariantCultureIgnoreCase
                         if (lines[i].toUpperCase().contains(" CHECK ")){
                             lines[i] = lines[i].replaceAll("\"", "\'");
                         }else{
                             //lines[i] = lines[i].Remove(k, 1);
                        	 /*System.out.println(":::line::::"+lines[i].trim()+"::::::k val::::"+k);
                        	 if(k > 0){
                        	 lines[i] = lines[i].substring(k ,lines[i].length()-1);
                        	 }*/
                        	 lines[i] = lines[i].replaceAll("\"", "");
                         }
                         k++;
                     }
                     else{
                         k = lines[i].length();
                     }
                 }
             }                
             //String ddlPath = pFolderPath;
             //String finalText = ddlPath.substring(ddlPath.LastIndexOf("\\") + 1);
             //finalText = finalText.Insert(finalText.lastIndexOf("."), "_Removed");

             //sw = new StreamWriter(final);
           
            
            String[] lRetArr =  ToolsUtil.splitFileNameAndExtension(pFileName);
         
            
            lOutputFile = pOutpurFolderpath+lRetArr[0]+"_removed"+"."+lRetArr[1];
            FileUploadDownloadUtility.createFolders(pOutpurFolderpath);
             BufferedWriter writer = new BufferedWriter(new FileWriter(lOutputFile));
             System.out.println(":::file will be written to ::::"+lOutputFile);
             for (int i = 0; i < lines.length;i++)
             {
            	 /*System.out.println("::::lines::::"+lines[i].trim());
            	 writer.write(lines[i].trim());
                 writer.newLine();*/
                 if (!lines[i].contains("--"))
                 {	
                	 writer.write(lines[i].trim());
                     writer.newLine();
                     //sw.Write(lines[i].trim());
                     //sw.WriteLine();
                 }
                 else if (lines[i].contains("-- Approximate Table Size"))
                 {
                     //sw.Write(lines[i].trim());
                    // sw.WriteLine();
                	 writer.write(lines[i].trim());
                     writer.newLine();
                 }
             }
             //MessageBox.Show("Double Quotes Removed");
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
	 
	 public static void main(String args[]){
		 String pFolderPath="C:\\arun\\documents\\project\\idmt integration\\work\\remove quotes\\";
		 String pFileName="input.txt";
		 System.out.println(":::inside main:::");
		 CsToolRemoveQuotes lCsToolRemoveQuotes = new CsToolRemoveQuotes();
		 lCsToolRemoveQuotes.remove_Quotes(pFolderPath, pFileName,pFolderPath);
		 System.out.println(":::::main over::::");
		 
		 
	 }
}
