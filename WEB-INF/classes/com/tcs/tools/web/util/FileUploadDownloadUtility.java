
package com.tcs.tools.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;





/**
 * This class is used for file uploading
 * 
 * @author polaris
 * 
 */
public class FileUploadDownloadUtility {
	
	/**
	 * @param pFileName
	 * @param pFileType
	 * @return
	 * @throws Exception
	 */
	public static String validateFile(String pFileName, String pFileType){

		try {
			
			
		
			if (ToolsUtil.replaceNull(pFileType).trim().length() > 0) {
				if (pFileName.lastIndexOf(pFileType) < 0) {
					return "invalid_format";
				} else {
					return "valid_formate";
				}
			} else {
				return "invalid_format";
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			return "invalid_format";
		}
	}
	
	public static void downloadFile(String pFileName,String pPath,HttpServletResponse response){
		 //for file download
		 File file = new File(pPath+pFileName);
		 if(response != null){
			 response.setHeader("Content-Type", ServletActionContext.getServletContext().getMimeType(file.getName())); 
			 response.setHeader("Content-Length", file.length()+""); 
			 response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
		 }
		 BufferedInputStream input = null;     
		 BufferedOutputStream output = null; 
		 
		 try { 
			 input = new BufferedInputStream(new FileInputStream(file)); 
			 output = new BufferedOutputStream(response.getOutputStream()); 
			 String sample="";

			 byte[] buffer = new byte[8192]; 
			
			 for (int length = 0; (length = input.read(buffer)) > 0;) { 
			 output.write(buffer, 0, length); 
			 } 
			 }catch (Exception e) {
				e.printStackTrace();
			} finally { 
			 if (output != null) try { output.close(); } catch (IOException ignore) {} 
			 if (input != null) try { input.close(); } catch (IOException ignore) {} 
			 } 

	}
	
	public static void downloadFile(String pFullFileName,HttpServletResponse response){
		
		
		 //for file download
		 File file = new File(pFullFileName);
		 if(response != null){
			 response.setHeader("Content-Type", ServletActionContext.getServletContext().getMimeType(file.getName())); 
			 response.setHeader("Content-Length", file.length()+""); 
			 response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
		 }
		 BufferedInputStream input = null;     
		 BufferedOutputStream output = null; 
		 
		 try { 
			 input = new BufferedInputStream(new FileInputStream(file)); 
			 output = new BufferedOutputStream(response.getOutputStream()); 
			String sample="";

			 byte[] buffer = new byte[8192]; 
			
			 for (int length = 0; (length = input.read(buffer)) > 0;) { 
			 output.write(buffer, 0, length); 
			 } 
			 }catch (Exception e) {
				e.printStackTrace();
			} finally { 
			 if (output != null) 
				 try 
			 		{ 
					 output.close(); 
					} 
			 	 catch (IOException ignore) {} 
			 if (input != null) 
				 try 
			 		{ 
					 input.close();
			 		} 
			 	 catch (IOException ignore) {} 
			} 

	}
	
	public static void createFolders(String pFolderPath){		
		try {
			File temp = new File(pFolderPath);
			temp.mkdir();
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	public static void createFile(String pFolderPath,String pFileName,String pFileContent){		
		try {
			createFolders(pFolderPath);
			BufferedWriter writer = new BufferedWriter(new FileWriter(pFolderPath + pFileName));
			/*String[] tempArr = pFileContent.split("\n");
			for (int i = 0; i < tempArr.length; i++) {
				writer.write(tempArr[i].toString());
	            writer.newLine();
			}*/
			writer.write(pFileContent);
            writer.newLine();
			
            writer.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	public static void downloadListAsExcelFile(List pMainList,String pPath,String pFileName,HttpServletResponse pResponse){
		 try{		
			 List lSubList = new ArrayList();
			 String lCellContent = "";
			 HSSFWorkbook hwb=new HSSFWorkbook();
				HSSFSheet sheet = hwb.createSheet(pFileName.substring(0,pFileName.indexOf(".")));
			 if(pMainList != null && pMainList.size() > 0){
				 for (int i = 0; i < pMainList.size(); i++) {
					 lSubList = (List)pMainList.get(i);
					 HSSFRow rowhead= sheet.createRow((short)i);
					 if(lSubList != null && lSubList.size() > 0){
						 for (int j = 0; j < lSubList.size(); j++) {
							 lCellContent = (String)lSubList.get(j);
							
							 rowhead.createCell((short) j).setCellValue(lCellContent);
						 }
					 }
					 
					
				}
			 }
			 FileUploadDownloadUtility.createFolders(pPath);
			 FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
			 hwb.write(fileOut);
			 fileOut.close();
			 System.out.println("Your excel file has been generated!");
			 if(pResponse != null){
				 //for file download
				 FileUploadDownloadUtility.downloadFile(pFileName,pPath,pResponse);
				 //for file download
			 }
				
		 } catch (Exception e) {			
				e.printStackTrace();
		 }
		 
	 }	
	
	public static void main(String[] args){
		System.out.println(":::: inside main::::"+FileUploadDownloadUtility.validateFile("30.08.2011.zip", ".zip"));
		
	}
}