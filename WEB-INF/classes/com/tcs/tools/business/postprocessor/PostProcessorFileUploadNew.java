package com.tcs.tools.business.postprocessor;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcs.tools.web.util.ToolsUtil;

public class PostProcessorFileUploadNew {
	
	boolean lDoubleQuoteFound=false;
	boolean lSingleQuoteFound=false;
	
	
	
public void invokePostPocessor(File pFile,String pRootFolderPath){
		
		
		String lines[]=getLines(pFile);
		
		 System.out.println(":::lines in file::::"+lines.length);
	
		
		//creating the updated file
		//String pOutputDirectory = pRootFolderPath+"\\output\\";
		//ToolsUtil.writeToFile(lines, pOutputDirectory+"\\"+pFile.getName(),pOutputDirectory);
		 String pOutputDirectory = "C:\\arun\\Tool Output\\post processor\\test\\";
			ToolsUtil.writeToFile(lines, pOutputDirectory+"\\"+pFile.getName(),pOutputDirectory);
	}

	
	public void intiateFilParsing(String pProjectId,String pFolderPath,String pRootFolderPath,Connection pConnection){
		String pRunSeq="0";
		pFolderPath=pFolderPath.trim();
		pRootFolderPath=pRootFolderPath.trim();		
		try {
			File ldirpath=new File(pFolderPath);
			File[] lFilesAndDirs = ldirpath.listFiles();
			System.out.println(pFolderPath+"::No of Files::"+lFilesAndDirs.length);
			for (int i = 0; i < lFilesAndDirs.length; i++) {
				
				if(lFilesAndDirs[i].isDirectory()){
					//intiateFilParsing(pProjectId,lFilesAndDirs[i].getPath(),pRootFolderPath);				
					//System.out.println("Cur Path:::"+lFilesAndDirs[i].getPath());					
					intiateFilParsing(pProjectId,lFilesAndDirs[i].getPath(),pRootFolderPath,pConnection);				
				}else{
					System.out.println("File Name:->"+lFilesAndDirs[i].getName());
					invokePostPocessor( lFilesAndDirs[i],pRootFolderPath);
				}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ;
	}
	
	
	/**
	 * this methods return the all the lines in the file
	 * this method strips off all the comments in the file (single line , multiline comments)
	 * @param file
	 * @return
	 */
	public String[] getLines(File file) {
		String[] lines = null;
		String [] lNewLines=null;
		try {
			InputStream is = new FileInputStream(file);
			StringBuffer buffer = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = is.read(b)) != -1;) {
				
				buffer.append(new String(b, 0, n));
			}
			
			String str = buffer.toString();
		
			
			System.out.println(str);
			Pattern p = Pattern.compile("\\n");                        
			lines = p.split(str);
			
			for (int i = 0; i < lines.length; i++) {
				
			}
			String dataLine ="";
			BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\arun\\abc.txt"));
			for (int i = 0; i < lines.length;i++){
				dataLine += lines[i]+"\n"  ;
				//System.out.println("#"+lines[i]+"#");
	        	
	        }
			
			writer.write(dataLine);
			writer.close();
			
			/* try {
			      File outFile = new File("C:\\arun\\abc.txt");
			     
			      DataOutputStream dos = new DataOutputStream(new FileOutputStream(outFile));
			    
			     System.out.println(":::::::dataLine::::::::::::::::"+dataLine);
			     dos.writeBytes(dataLine.toString().getBytes("UTF-8"));
			   
			      //dos.writeBytes(dataLine);
			      dos.close();
			    } catch (FileNotFoundException ex) {
			     
			    } catch (IOException ex) {
			     
			    }*/



		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {		

		System.out.println("Inside Main:::"+new Timestamp(System.currentTimeMillis()));
		PostProcessorFileUploadNew lPostProcessorFileUploadNew=new PostProcessorFileUploadNew();
		String lProjectId="";		
		String lRootFolderPath="C:\\arun\\documents\\project\\Test Run\\post_processor\\simple_like";
		Connection lConnection=null;
		lPostProcessorFileUploadNew.intiateFilParsing( lProjectId, lRootFolderPath, lRootFolderPath, lConnection);
		System.out.println("End Main:::"+new Timestamp(System.currentTimeMillis()));
	
	}

}
