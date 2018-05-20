package com.tcs.tools.business.postprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcs.tools.business.constant.ToolConstant;

import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.util.ToolsUtil;

public class PostProcessFileUpload {
	
	boolean lDoubleQuoteFound=false;
	boolean lSingleQuoteFound=false;

	
	
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
					//invokePostPocessor( lFilesAndDirs[i],pRootFolderPath);
					 getBlocksFromFile(lFilesAndDirs[i].getPath());
				}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ;
	}
	
public List getBlocksFromFile(String pFilePath) throws FileNotFoundException,Exception{
	File pFile = new File (pFilePath);
		
	
		String lines[]=null;
		try{
		lines = getLines(pFile);
		}catch (FileNotFoundException e) {
			throw new FileNotFoundException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		
		 
		 lDoubleQuoteFound=false;
		 lSingleQuoteFound=false;
		 StringBuffer sb= new StringBuffer();
		 System.out.println(":::lines in file::::"+lines.length);
		 List lBlockContent = new ArrayList();
		 List lBlockStrList = new ArrayList();
		 String lBlockStr ="";
	
		 boolean lIsStatementComplete = false;
		 
		 for (int i = 0; i < lines.length; i++) {
				
				
			
				
				//arun - post processor -16.01.2012 - start
				//split the data with ;
				//lIsStatementComplete = false;
				if(lines[i].trim().endsWith(";")){
					lIsStatementComplete = true;
					lBlockStr += lines[i]+ " ";
					lBlockStrList.add(lBlockStr);
					lBlockStr ="";
					
				}else{
					lIsStatementComplete = false;

					lBlockStr += lines[i]+"\n";//+ToolConstant.TOOL_DELIMT+ " ";
				}
				
				if(lIsStatementComplete == true){
					//System.out.println("----------------------------------------------------------------------------------");
				}
				
				
		 }
		
		
		
		return lBlockStrList;
	}

public String[] getLines(File file) throws FileNotFoundException,Exception {
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
		//str=str.replaceAll("(\\s*&\\s*\r\n|\r\n\\&\\s*)", " & ");
		str = str.replaceAll("/\\s*\\*", " /\\* ");
		str = str.replaceAll("\\*\\s*/", " \\*/ ");
		
		//System.out.println(str);
		Pattern p = Pattern.compile("\r\n");                        
		lines = p.split(str.trim());
		
		for (int i = 0; i < lines.length; i++) {
			//System.out.println("Befoer::"+(i+1)+" ::-> "+lines[i]);
			lines[i] = lines[i].replaceAll("/\\s*/", " // ");
			//System.out.println("Midle::"+(i+1)+" ::-> "+lines[i]);
			lines[i]=lines[i].replaceAll("/\\s*/[\\s\\W\\w\\d]*", "");				
			//System.out.println("After::"+(i+1)+" ::-> "+lines[i]);
			//lines[i]=lines[i].replaceAll("(?i)\\bSET\\b[\\s\\r\\n]+\\bROWCOUNT\\b0", "FETCH FIRST 0 ROWS ONLY");
			//lines[i]=lines[i].replaceAll("(?i)\\bSET\\b[\\s\\r\\n]+\\bROWCOUNT\\b1", "FETCH FIRST 1 ROWS ONLY");
			lines[i] = handleSingleLineComment(lines[i]);
		
		}
		
		
		//handleMultiLineComments will replace all spaces in side multi line comments with tool specified constat(Ex:_DBT_COMM_).
		lNewLines=handleMultiLineComments(lines);
	
	} catch (FileNotFoundException e) {
		e.printStackTrace();
		throw new FileNotFoundException(e.getMessage());
	} catch (Exception e) {
		e.printStackTrace();
		throw new Exception(e.getMessage());
	}
	return lNewLines;
}

	/**
	 * @param pLine
	 * @return
	 */
	public String  handleSingleLineComment(String pLine){
		
		if(pLine == null || "".equals(pLine)){
			 return pLine;
		}
		
		if(!pLine.contains("--")) {
			return pLine;
		}
		
		 if (pLine.startsWith("--")) {
			 pLine= pLine.replaceAll("\\s", "_DBT_COMM_"); 
			 return pLine;
		 }
		 String lConcatStr ="";
		 String arr[] = pLine.split("--");
		 for (int i = 0; i < arr.length; i++) {
			 if(i > 0 ){ 
				 lConcatStr += "--";
			 }	 
			 lConcatStr +=  arr[i].replaceAll("\\s", "_DBT_COMM_"); 
		 }
		
		
		
		return lConcatStr;
	}

//handleMultiLineComments will replace all spaces in side multi line comments with tool specified constat(Ex:_DBT_COMM_)
public  String[] handleMultiLineComments(String [] lines){
	boolean lisComment=false;
	String lTmpLine="";
	String[] lRetLine=lines;
	for (int cnt = 0; cnt < lines.length; cnt++) {
		lTmpLine="";
		String[] words = lines[cnt].split("\\s");
		
		for (int i = 0; i < words.length; i++) {			
			
			 if("/*".equals(words[i].trim()))
			 {
				 //System.out.println("Comment Set");
				 lisComment=true;
			 }
			 else if("*/".equals(words[i].trim()))
			 {
				 lisComment=false;
				 lTmpLine+=words[i];
				 continue;
			 }						 
			 if(lisComment==true)
			 {
				 if("\n".equals(words[i].trim())){
					 lTmpLine+=" "+words[i]+" ";
				 }else if(!"".equals(words[i].trim())){
					 lTmpLine+=words[i]+"_DBT_COMM_";
				 }					 
				 continue;
			 }
			 
			 lTmpLine+=words[i]+" ";				 
			 
		}
		
		lRetLine[cnt]=lTmpLine;
	}
	return lRetLine;
}

public List getBlocksFromLines(List lLineStrList){
	List lBlockStrList=new ArrayList();
	String lBlockStr="";
	String lTempStr="";
	boolean lIsStatementComplete= false;
	for (int j = 0; j < lLineStrList.size(); j++) {
		lTempStr=(String)lLineStrList.get(j);
		if(lTempStr.trim().endsWith(";")){
			lIsStatementComplete = true;
			lBlockStr += lTempStr+ " ";
			lBlockStrList.add(lBlockStr);
			lBlockStr ="";
			
		}else{
			lIsStatementComplete = false;
			lBlockStr += lTempStr+"\n";//+ToolConstant.TOOL_DELIMT+ " ";
		}
		
	}
	return lBlockStrList;
}

public List getLinesFromBlocks(List lBlockStrList){
	 List lLineStrList=new ArrayList();
	 for (int i = 0; i < lBlockStrList.size(); i++) {
		String[] lSubArray  = ((String)lBlockStrList.get(i)).split("\\n");
			
			for (int j = 0; j < lSubArray.length; j++) {
				lLineStrList.add((String)lSubArray[j]);
			}
		 
	 }
	
	return lLineStrList;
}
public static void main(String[] a){
	System.out.println(":::::;inside main::::");
}
	
}
