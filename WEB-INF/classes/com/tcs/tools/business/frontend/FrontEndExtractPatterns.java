package com.tcs.tools.business.frontend;

/*
 * FileUpload.java
 *
 * Created on September 30, 2011, 12:37 PM
 */



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcs.tools.business.fileupload.dao.FileUploadDAO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;
/**
 * 
 * @author 477780
 */
public class FrontEndExtractPatterns {

	/** Creates a new instance of FileUpload */
	public FrontEndExtractPatterns() {
	}

	private int lWordOrderNo = 1;
	private HashMap lPatternDataHm = null;
	FileWriter lFileWriter=null;
	FileWriter lConnectStrFileWriter=null;
	private List lDBConnectionPatternStrList=new ArrayList();
	private List lDynamicSqlDataList=new ArrayList();
	int lFileCount=0;
	int lSingleQuoteTextCount=1;
	String lReplceSingleQuoteText="DBT_VAR_";
	HashMap lReplaceSingleQuotesMap=new HashMap();
	
	//private PatternAnalysisSingleDAO pads = new PatternAnalysisSingleDAO("prepareCall");
	

	/**
	 * @param args
	 *            the command line arguments
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		System.out.println("test main::::::::::"
				+ new Timestamp(System.currentTimeMillis()));
		
		// Connection con=DBConnectionManager.getConnection();
		FrontEndExtractPatterns lFileUpload = new FrontEndExtractPatterns();
		//String pFolderPath ="C:\\Praveen\\Source of Prudential DCMS\\Front End\\Front End\\Admin";   //"C:\\Praveen\\Tool Data\\inputs\\single";
		String pFolderPath ="C:\\arun\\documents\\project\\UVC PXB\\Source Code\\Combined\\unzipped\\PSBCSR_Workspace";//\\Java_application";
		lFileUpload.getFiles(pFolderPath,"SYSBASE_TO_DB2","");	
		System.out.println("------------------------------------------------------------------------------------------------");
		System.out.println("test main:done::::::"
				+ new Timestamp(System.currentTimeMillis()));
		
		

	}

	public String getFiles(String pFolderPath,String pDbMigrationType,String pProjectRunId) {
		String lRunningSeqNo = null;
		try {
			String lFileName="JavaPatterns"+System.currentTimeMillis()+".txt";
			String lPath=ToolsUtil.readProperty("pathForConvertedTempFiles");			
			lFileWriter=new FileWriter(lPath+lFileName,false);
			lConnectStrFileWriter=new FileWriter(lPath+"ConStr"+lFileName,false);
			lFileWriter.append("SNo");
			lFileWriter.append(";");			
			lFileWriter.append("File Location");
			lFileWriter.append(";");
			lFileWriter.append("File Name");
			lFileWriter.append(";");
			lFileWriter.append("Line Num");
			lFileWriter.append(";");
			lFileWriter.append("SQL Statement");					
			lFileWriter.append("\n");
			
			lConnectStrFileWriter.append("SNo");
			lConnectStrFileWriter.append(";");			
			lConnectStrFileWriter.append("File Location");
			lConnectStrFileWriter.append(";");
			lConnectStrFileWriter.append("File Name");
			lConnectStrFileWriter.append(";");
			lConnectStrFileWriter.append("Line Num");
			lConnectStrFileWriter.append(";");
			lConnectStrFileWriter.append("SQL Statement");					
			lConnectStrFileWriter.append("\n");
			
			//lFileWriter.append("//Dynamic SQLS");
			// getting the master sequence
			FileUploadDAO lFileUploadDAO = new FileUploadDAO();
				//running sequence commented
			 //lRunningSeqNo = lFileUploadDAO.getRunSeq();
			lRunningSeqNo = pProjectRunId;
			lPatternDataHm = lFileUploadDAO.getPatternMatchDataList(pDbMigrationType); 
                       // Connection lConnection = DBConnectionManager.getConnection();
			//System.out.println("pFolderPath--->"+pFolderPath);
                       // System.out.println("lRunningSeqNo--->"+lRunningSeqNo);
			pFolderPath = pFolderPath.replaceAll("\\\\", "/");
			//for hard coding the folder path
			//pFolderPath ="D:/STUDY/WORKSPACE_TOOL/sample_io/small/good";			
			 File dirpath = new File(pFolderPath);
			 getIndividualFile( dirpath, pFolderPath, lRunningSeqNo);
			 lFileWriter.close();
			 lConnectStrFileWriter.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lRunningSeqNo;
	}

	public void getIndividualFile(File dirpath,String pFolderPath,String lRunningSeqNo){
		 try{
			
				
				File[] filesAndDirs = dirpath.listFiles();
				//System.out.println(":::::::No: if Files:::::" + filesAndDirs.length);
				/*
				 * String fileName="C:/Documents and Settings/477780/My
				 * Documents/Tool Data/inputs/ABS_DetailByEE.sql";
				 */

				for (int cnt = 0; cnt < filesAndDirs.length; cnt++) {
					lWordOrderNo = 1;
					File file = filesAndDirs[cnt];					
					// String[] lines= getLines(file);
					// System.out.println(lines.length);
					if ( file.isFile()) {
						if( /*(file.getName().trim().endsWith(".vb") && (!file.getName().trim().endsWith("designer.vb")))*/
								 (file.getName().trim().endsWith(".java")/*file.getName().trim().endsWith(".properties")||file.getName().trim().endsWith(".xml")*/ ) ) {
							System.out.println(":::::File name::::::" + file.getName());//+" Parent path::"+file.getParent());
							parseFile(getLines(file)/* reading each file contenet */, file
									.getName(),file.getParent(),lRunningSeqNo);
							
							
							
						}else{
							//System.out.println("File Name::"+file.getName());
						}
					
					//pads.getParsedData(lRunningSeqNo,file.getName());
					}else{
						getIndividualFile(file , pFolderPath, lRunningSeqNo);
					}
				} 
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	
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
			str=str.replaceAll("(\\s*&\\s*\r\n|\r\n\\&\\s*)", " & ");
			str = str.replaceAll("/\\s*\\*", " /\\* ");
			str = str.replaceAll("\\*\\s*/", " \\*/ ");
			str = str.replaceAll("\\*\\s*/", " \\*/ ");
			str = str.replaceAll("(?i)\\s*\\bplease\\b\\s*", "_please_");
			//please call the help desk
			//System.out.println(str);
			Pattern p = Pattern.compile("\r\n");                        
			lines = p.split(str.trim());
			lNewLines=handleMultiLineComments(lines);
			for (int i = 0; i < lNewLines.length; i++) {
				if(lNewLines[i].contains("//")){
					lNewLines[i]=lNewLines[i].substring(0,lNewLines[i].indexOf("//"));
				}
				//System.out.println(lNewLines[i]);
			}
			



		} catch (Exception e) {
			e.printStackTrace();
		}
		return lNewLines;
	}
	public String[] handleMultiLineComments(String [] lines){
		boolean lisComment=false;
		String lTmpLine="";
		String[] lRetLine=lines;
		for (int cnt = 0; cnt < lines.length; cnt++) {
			lTmpLine="";
			String[] words = lines[cnt].trim().split("\\s+");
			
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

	/**
	 * @param lines
	 * @param fileName
	 * @param pRunningSeqNo
	 */
	/**
	 * @param lines
	 * @param fileName
	 * @param pRunningSeqNo
	 */
	public void parseFile(String[] lines, String fileName,String pFilePath, String pRunningSeqNo) {
		try {
			//System.out.println("In Parse File"+lines.length);
			FileUploadDAO lFileUploadDAO = new FileUploadDAO();
			
			 //StringBuffer buffer = new StringBuffer();

			// for writing into text file
			// BufferedWriter writer = new BufferedWriter(new FileWriter("c:/arun/filename.txt"));
			//StringBuffer sbInsert = new StringBuffer();
			/*if(fileName.trim().endsWith(".properties")){
				for (int cnt = 0; cnt < lines.length; cnt++) {
					//Pattern lConnectionStrMatchPattern=Pattern.compile("(com.sybase.jdbc2.jdbc.SybDriver|jdbc:sybase:Tds:)");
					Pattern lConnectionStrMatchPattern=Pattern.compile("(?i)(com.sybase.jdbc2.jdbc.SybDriver|jdbc:sybase:Tds:|org.hibernate.dialect.SybaseDialect|binary)");
					//org.hibernate.dialect.SybaseDialect
					if(fileName.trim().equalsIgnoreCase("batch.properties")){
						System.out.println(lConnectionStrMatchPattern.matcher(lines[cnt]).find()+ "Line Num ::"+cnt+":::->"+lines[cnt]);
						
					}

					if(lConnectionStrMatchPattern.matcher(lines[cnt]).find()){
						
						lConnectStrFileWriter.append(0+"");
						lConnectStrFileWriter.append(";");					
						lConnectStrFileWriter.append(pFilePath);
						lConnectStrFileWriter.append(";");
						lConnectStrFileWriter.append(fileName);
						lConnectStrFileWriter.append(";");
						lConnectStrFileWriter.append(cnt+"");
						lConnectStrFileWriter.append(";");
						lConnectStrFileWriter.append(lines[cnt].trim());					
						lConnectStrFileWriter.append("\n");
					}
					
				}
				return;
				
			}*/
		
 			
 			/*Pattern lInlineSQLMatchPattern=Pattern.compile("(?!i)(\\b(Exec |Select |Insert Into |Delete |Update |Execute )\\w*)|([\\r\\n ]*CommandType.StoredProcedure[^\\r\\n]*)");*/
 			//Pattern lSingleLineCommentPattern=Pattern.compile("(^['].*$)|(^[//].*$)");
 			//Pattern lMsgBoxPattern=Pattern.compile("([\\r\\n ]*MsgBox[^\\r\\n]*)|([\\r\\n ]*System.out.print[^\\r\n\\s]*)");
 			//Java
 			//Pattern lInlineSQLMatchPattern=Pattern.compile("(?i)([\\W\\s]+\\bCALL\\b\\s+|\\bEXEC\\b\\s+|\\bSelect\\b|\\bInsert\\b\\s+\\bInto\\b|\\bDelete\\b|\\bUpdate\\b\\s*(?\\W+)|)");
 			Pattern lInlineSQLMatchPattern=Pattern.compile("(?i)([^\\w\\.]+\\bCALL\\b\\s+[^\\W]+|\\bEXEC\\b\\s+[^\\W]+|\\bSelect\\b\\s+[^\\W]+|\\bInsert\\b\\s*\\bInto\\b\\s+|\\bDelete\\b\\s+[^\\W]+|\\bUpdate\\b\\s+[^\\W]+)");
 			//(?i)([^\\w\\.]+\\bCALL\\b\\s+[^\\W]+|\\bEXEC\\b\\s+[^\\W]+|\\bSelect\\b\\s+[^\\W]+|\\bInsert\\b\\s*\\bInto\\b\\s+|\\bDelete\\b\\s+[^\\W]+|\\bUpdate\\b\\s+[^\\W]+)
 			Pattern lSingleLineCommentPattern=Pattern.compile("(^[//].*)");
 			Pattern lPrintStrPattern=Pattern.compile("(?i)(\\bSYSTEM.OUT.[\\r\n\\s\\w\\W]*|\\bthrow\\b\\s+[\\r\n\\s\\w\\W]*Exception[\\r\n\\s\\w\\W]*|[\\r\n\\s\\w\\W]+\\.debug\\s*\\(|[\\r\n\\s\\w\\W]+\\.writeToLog\\s*\\()");
 			//throw ExceptionHelper.newException(
 			
 			int lSno=0;
			for (int cnt = 0; cnt < lines.length; cnt++) {
				
				if(lines[cnt] == null ||  "".equals(lines[cnt])){
					continue;					
				}
				//System.out.println(cnt+":::::->"+lines[cnt]);
				if(lInlineSQLMatchPattern.matcher(lines[cnt].trim().toUpperCase()).find()
						&& (!lPrintStrPattern.matcher(lines[cnt].trim().toUpperCase()).find())
						&& (!lSingleLineCommentPattern.matcher(lines[cnt].trim().toUpperCase()).find()) ){
					lSno++;
					lFileWriter.append(lSno+"");
					lFileWriter.append(";");					
					lFileWriter.append(pFilePath);
					lFileWriter.append(";");
					lFileWriter.append(fileName);
					lFileWriter.append(";");
					lFileWriter.append(cnt+"");
					lFileWriter.append(";");
					lFileWriter.append(lines[cnt].trim());					
					lFileWriter.append("\n");
					//System.out.println(cnt+":::::->"+lines[cnt]);
				}
				
				
			}
			lSno=0;
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public String getHmPatternData(String pPramQueryString){
		 pPramQueryString = pPramQueryString.replaceAll("'","");
		 pPramQueryString =pPramQueryString.toUpperCase();
		 
		 //if there is no value in the result value matching then return a constant
         if(pPramQueryString == null || "".equals(pPramQueryString))  return "BLANK";
         
         try {
        	 pPramQueryString = (String)lPatternDataHm.get(pPramQueryString);
        	 if(pPramQueryString == null || "".equals(pPramQueryString))  return "OTHER FORMAT";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "OTHER FORMAT";
		}
         
         return pPramQueryString;
	}
	
	

}

