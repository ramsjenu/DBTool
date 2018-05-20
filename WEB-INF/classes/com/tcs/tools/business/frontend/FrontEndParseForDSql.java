/*
 * FileUpload.java
 *
 * Created on September 30, 2011, 12:37 PM
 */

package com.tcs.tools.business.frontend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.business.frontend.dao.JavaFrontEndParseForDSqlDAO;

import com.tcs.tools.business.frontend.dao.VBFrontEndParseForDSqlDAO;
import com.tcs.tools.business.frontend.dto.DynamicSQLDataDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.SQLWaysConnectionUtil;
import com.tcs.tools.web.util.ToolsUtil;
/**
 * 
 * @author 477780
 */
public class FrontEndParseForDSql {

	/** Creates a new instance of FileUpload */
	public FrontEndParseForDSql() {
	}

	private int lWordOrderNo = 1;
	private HashMap lPatternDataHm = null;
	//FileWriter lFileWriter=null;
	private List lDBConnectionPatternStrList=new ArrayList();
	//private List lDynamicSqlDataList=new ArrayList();
	int lFileCount=0;
	int lSingleQuoteTextCount=1;
	String lReplceSingleQuoteText="_DBT_VAR_";
	HashMap lReplaceSingleQuotesMap=new HashMap();
	String lRootPath="";
	String lPathForConvertedTempFiles="";
	String lPathsFileName="";
	String lSQLWaysLogFile="";
	String lTargetPathForFrontEndFiles="";
	String lSqlWaysRootPath="";
	String lTempFilesPath=null;
	Connection lConnection=null;
	VBFrontEndParseForDSqlDAO lVBFrontEndParseForDSqlDAO =null;
	JavaFrontEndParseForDSqlDAO lJavaFrontEndParseForDSqlDAO =null;
	List lExcelMainList=new ArrayList();
	String lStausMsg="";
	String lCurState="";
	
	int lFileCountInCurDir=0;

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
		FrontEndParseForDSql lFrontEndFileUpload = new FrontEndParseForDSql();
		
		//String lpFolderPath ="C:\\Praveen\\Source of Prudential DCMS\\Front End\\Front End\\Admin";
		String lpFolderPath ="C:\\Praveen\\Tool Data\\inputs\\Source";
		lFrontEndFileUpload.lRootPath=lpFolderPath;
		//lFrontEndFileUpload.lInputPath ="C:\\Praveen\\Tool Data\\inputs\\single";; 
		/*String pFolderPath = "C:\\Praveen\\Source of Prudential DCMS\\java";*/		
		String pRunSeq="0";
		String pProjectId="PRID_005";
		String pSourceCodeType="Java";
		//lFrontEndFileUpload.lFrontEndParseForDSqlDAO = new FrontEndParseForDSqlDAO();
		lFrontEndFileUpload.startFileParse(pProjectId,pRunSeq,lpFolderPath,lFrontEndFileUpload.lRootPath, pSourceCodeType, new ArrayList(),DBConnectionManager.getConnection());
		/*try {
			lFrontEndFileUpload.lConnection=DBConnectionManager.getConnection();
			lFrontEndFileUpload.lConnection.setAutoCommit(false);
			System.out.println("Removing Old Data...");
			lFrontEndFileUpload.lFrontEndParseForDSqlDAO .clearDynamicSqlData(pProjectId,pRunSeq,lpFolderPath,lFrontEndFileUpload.lConnection);
			
			System.out.println("Uploading Files n Creating Temporary Files...");
			System.out.println("Before Parse:::"+lpFolderPath);
			lFrontEndFileUpload.getFiles(lpFolderPath,"SYSBASE_TO_DB2",pProjectId,pRunSeq);
			
			 System.out.println("Running SQL Ways....");
			 lFrontEndFileUpload.runSqlWays();
			
			System.out.println("Reading Log...");
			lFrontEndFileUpload.readConvertedTmpFiles(pProjectId,pRunSeq,lpFolderPath );
			lFrontEndFileUpload.lConnection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		System.out.println("test main:done::::::"
				+ new Timestamp(System.currentTimeMillis()));
		
		

	}
	public void setAllRequiredPaths(){
		lPathsFileName=ToolsUtil.readProperty("fileForTempFilePaths");
		lPathForConvertedTempFiles=ToolsUtil.readProperty("pathForConvertedTempFiles");
		lSQLWaysLogFile=ToolsUtil.readProperty("sqlWaysLogFile");
		lTargetPathForFrontEndFiles=ToolsUtil.readProperty("targetPathForFrontEndFiles");
		lSqlWaysRootPath=ToolsUtil.readProperty("sqlWaysRootPath");
		lTempFilesPath=ToolsUtil.readProperty("dSqlsTempFilesPath");
		
		//Creating Folders if not exists.
		FileUploadDownloadUtility.createFolders(lTempFilesPath);
		FileUploadDownloadUtility.createFolders(lPathForConvertedTempFiles);
		FileUploadDownloadUtility.createFolders(lTargetPathForFrontEndFiles);
	}
	public List startFileParse(String pProjectId,String pRunSeq,String pFolderPath,String pRootDirPath,String pSourceCodeType,List pExcelMainList,Connection pConnection){
		
		
		try {
			lExcelMainList=pExcelMainList;			
			lVBFrontEndParseForDSqlDAO = new VBFrontEndParseForDSqlDAO();
			lJavaFrontEndParseForDSqlDAO = new JavaFrontEndParseForDSqlDAO();
			//Connection
			lConnection=pConnection;
			//lConnection.setAutoCommit(false);
			
			lRootPath=pRootDirPath;
			System.out.println("********* lRootPath :::::::::->"+lRootPath);
			System.out.println("Removing Old Data...");
			lVBFrontEndParseForDSqlDAO .clearDynamicSqlData(pProjectId,pRunSeq,pFolderPath,lConnection);		
			lJavaFrontEndParseForDSqlDAO .clearDynamicSqlData(pProjectId,pRunSeq,pFolderPath,lConnection);			
			System.out.println("Uploading Files n Creating Temporary Files...");
			//Update Status to Front End - Start
			String lSourceFilePath="";
			System.out.println("::::::::pFolderPath::::::::"+pFolderPath);
			lSourceFilePath=pFolderPath.trim().toUpperCase().split("\\\\UNZIPPED\\\\")[1]; //  Taking current folder path			
			lSourceFilePath=lSourceFilePath.substring(lSourceFilePath.indexOf("\\")+1,lSourceFilePath.length());			 
			lCurState="Reading Files";
			lStausMsg="Processing Folder :"+lSourceFilePath;	
			ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),lConnection);
			//Update Status to Front End - End
			getFiles(lRootPath,"SYSBASE_TO_DB2",pProjectId,pRunSeq,pSourceCodeType);	
			
			// Run SQL Ways if and only if the curdirectory has Files
			if(lFileCountInCurDir>0){
				System.out.println("Running SQL Ways....");
				//Update Status to Front End - Start
				lCurState="Converting";
				lStausMsg="SQL Ways Triggered";
				ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				runSqlWays();
				
				System.out.println("Reading Log...");
				
				//Update Status to Front End - Start
				lCurState="Read SQL Ways Outputs";
				lStausMsg="Parsing SQL Ways Log Started";
				ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
				//Update Status to Front End - End
				if(pSourceCodeType.equalsIgnoreCase("VB")){
					System.out.println("VB CODE");
					lVBFrontEndParseForDSqlDAO.readConvertedTmpFiles(pProjectId,pRunSeq,pFolderPath ,pRootDirPath,lConnection);
				//System.out.println("FINAL LIST:::::::::"+l);
				}
				else if(pSourceCodeType.equalsIgnoreCase("JAVA")){
					System.out.println("JAVA CODE");
					lJavaFrontEndParseForDSqlDAO.readConvertedTmpFiles(pProjectId,pRunSeq,pFolderPath ,pRootDirPath,lConnection);
				}
			}
			
			if(lConnection.getAutoCommit()==false){
				lConnection.commit();
			}
			//lConnection.setAutoCommit(true);
			pExcelMainList=lExcelMainList;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//DBConnectionManager.closeConnection(lConnection);
		}
		
		return pExcelMainList;
	}

	public String getFiles(String pFolderPath,String pDbMigrationType,String pProjectId,String pRunSeq,String pSourceCodeType) {
		System.out.println("::::Inside Method getFilesL::::pSourceCodeType-"+pSourceCodeType);
		String lRunningSeqNo = null;
		try {
			
			//String lPath="C:\\Praveen\\Temp_Files";
			setAllRequiredPaths();
			File file=new File(lPathsFileName);
			if(file.exists()){
				
				System.out.println("Path File Deleted:: "+file.delete());
			}
			
			//Deleting Exisited Pathes			
			//Runtime.getRuntime().exec("cmd /c del "+lPath +"*.sql");
			//lFileWriter=new FileWriter(lPath+"\\"+lFileName,false);
			
			//lFileWriter.append("//Dynamic SQLS");
			// getting the master sequence
			//lVBFrontEndParseForDSqlDAO = new VBFrontEndParseForDSqlDAO();
			
				//running sequence commented
			 //lRunningSeqNo = lFileUploadDAO.getRunSeq();
			lRunningSeqNo = pProjectId;
			//lPatternDataHm = lFileUploadDAO.getPatternMatchDataList(pDbMigrationType); 
                        //Connection lConnection = DBConnectionManager.getConnection();
			  
			//System.out.println("pFolderPath--->"+pFolderPath);
                       // System.out.println("lRunningSeqNo--->"+lRunningSeqNo);
			
			//for hard coding the folder path
			//pFolderPath ="D:/STUDY/WORKSPACE_TOOL/sample_io/small/good";	
			pFolderPath = pFolderPath.replaceAll("\\\\", "/");
			 File dirpath = new File(pFolderPath);
			 if(pSourceCodeType.equalsIgnoreCase("VB")){
				 getVBIndividualFile(pProjectId,pRunSeq, dirpath, pFolderPath, lRunningSeqNo); 
			 }else if(pSourceCodeType.equalsIgnoreCase("JAVA")){
				 getJavaIndividualFile(pProjectId,pRunSeq, dirpath, pFolderPath, lRunningSeqNo);
			 }
			 //lFileWriter.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lRunningSeqNo;
	}

	public void getVBIndividualFile(String pProjectId,String pRunSeq,File dirpath,String pFolderPath,String lRunningSeqNo){
		 try{
			
				lWordOrderNo = 1;
				//	spc_count++;
					//  for (int i = 0; i < spc_count; i++){
					
						if  ( dirpath.isFile()) {
						System.out.println("INSIDE IF ");
						 if( (dirpath.getName().trim().toUpperCase().endsWith(".VB") ) && (!dirpath.getName().trim().toUpperCase().endsWith("DESIGNER.VB")))  {
							System.out.println("INSIDE IF 1");
							lFileCountInCurDir++;
							System.out.println(":::::File name::::::" + dirpath.getName());
							
							//Update Status to Front End - Start
							lCurState ="Extract Files";
							lStausMsg="File Name::->"+dirpath.getName();
							ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
							//Update Status to Front End - End
							
							/*parseFile(getLines(file) reading each file contenet , file
									.getName(), lRunningSeqNo,lConnection);*/
							System.out.println("INSIDE IF 5");
							lVBFrontEndParseForDSqlDAO.parseFilesForDynamicSqls(pProjectId,pRunSeq,getLines(dirpath,"VB")/* reading each file contenet*/ , dirpath
									.getName(),/*file.getPath()*/dirpath.getPath(), lRunningSeqNo,lConnection);

							System.out.println("INSIDE IF 6");
							//spc_count--;
						}
						
					}
						else if (dirpath.isDirectory()){
							File[] filesAndDirs = dirpath.listFiles();
							// for (int cnt = 0; cnt < filesAndDirs.length; cnt++) {
							for (File file : filesAndDirs) {
							//	 File file = filesAndDirs[cnt];
							System.out.println("inside else");
							
							//System.out.println();
							getVBIndividualFile(pProjectId,pRunSeq, file, pFolderPath, lRunningSeqNo);
							System.out.println("called method in else");
						}
						}
				
			
					//  }	
					
					  
		
				
				
		 
	 }catch(Exception e){
		 e.printStackTrace();
	 }
		 
	 }
	//For Java
		public void getJavaIndividualFile(String pProjectId,String pRunSeq,File dirpath,String pFolderPath,String lRunningSeqNo){
			 try{
				
						lWordOrderNo = 1;
						
					
							
							if  ( dirpath.isFile()) {
							//System.out.println("INSIDE IF ");
							 if( (dirpath.getName().trim().toUpperCase().endsWith(".JAVA") ) ) {
							//	System.out.println("INSIDE IF 1");
								lFileCountInCurDir++;
								//System.out.println(":::::File name::::::" + dirpath.getName());
								
								//Update Status to Front End - Start
								lCurState ="Extract Files";
								lStausMsg="File Name::->"+dirpath.getName();
								ToolsUtil.prepareInsertStatusMsg( pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
								//Update Status to Front End - End
								
								/*parseFile(getLines(file) reading each file contenet , file
										.getName(), lRunningSeqNo,lConnection);*/
							//	System.out.println("INSIDE IF 5");
								lExcelMainList=lJavaFrontEndParseForDSqlDAO.parseFilesForDynamicSqls(pProjectId,pRunSeq,getLines(dirpath,"JAVA")/* reading each file contenet*/ , dirpath.getName(),dirpath.getPath(), lRunningSeqNo,lConnection,lExcelMainList);
								
								//System.out.println("INSIDE IF 6");
								//spc_count--;
							}
							
						}
							else if (dirpath.isDirectory()){
								String[] filesAndDirs = dirpath.list();
								// for (int cnt = 0; cnt < filesAndDirs.length; cnt++) {
								for (int i=0; i<filesAndDirs.length; i++) {
								//	 File file = filesAndDirs[cnt];
								//System.out.println("inside else");
								
								//System.out.println();
								getJavaIndividualFile(pProjectId,pRunSeq, new File(dirpath,filesAndDirs[i]), pFolderPath, lRunningSeqNo);
							//	System.out.println("called method in else");
							}
							}
					
				
					
				 
			 }catch(Exception e){
				 e.printStackTrace();
			 }
		 }
		
		
	public String[] getLines(File file,String pSourceCodeType) {
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
			}
			lNewLines=handleMultiLineComments(lines);
			if(pSourceCodeType.equalsIgnoreCase("Java")){
				//Combining splitted lines in code
				for (int i = 0; i < lNewLines.length; i++) {
					if((!"".equals(lNewLines[i].trim())) && (!lNewLines[i].trim().endsWith(";")) && (!lNewLines[i].trim().endsWith("{"))
							&& (!lNewLines[i].trim().endsWith("}")) ){
						//System.out.println("Step:::1"+lStrArr[i]);
						for (int j = i+1; j < lNewLines.length; j++) {
							lNewLines[i]=lNewLines[i]+" "+lNewLines[j];							
								//System.out.println("Step:::2"+lStrArr[i]);
								if( lNewLines[j].trim().endsWith(";") || (lNewLines[i].trim().endsWith("{"))
										||(lNewLines[i].trim().endsWith("}"))){
									lNewLines[j]="";
									break;
								}
								lNewLines[j]="";
						}
					}	
				}

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

	
	public void runSqlWays(){
		
		SQLWaysConnectionUtil.runSqlWays(lPathsFileName.trim(), lPathForConvertedTempFiles.trim(), "batch");
		//System.out.println(pPathsFileName);
		/*String dosCommand = "cmd /c ";
	      String location =
	    		  //" C:\\Praveen\\Tools\\SQL_Ways\\sqlways.exe " +
	    		  lSqlWaysRootPath+"sqlways.exe " +  " /D=FIXED /TARGET=IBM DB2 " +
	    		  " /DIR= "+lPathForConvertedTempFiles +//C:\\Praveen\\Outputs\\ " +
	      		  " /INI=C:\\Documents and Settings\\477780\\My Documents\\Ispirer\\SQLWays 5.0\\sqlways.ini " +
	    		  " /FF= "+ lPathsFileName.trim() + C:\\Praveen\\Temp_Files\\tmpFile.sql " + 
	    		  " /SOURCE=Sybase Adaptive Server Enterprise" ;// +
	    		  //" /LDEL = ;" ; 
	    		  
	    		  //"C:\\Praveen\\Tools\\SQL_Ways\\sqlways.exe /D=FIXED /TARGET=IBM DB2 /DIR=C:\\Praveen\\Outputs\\ /INI=C:\\Documents and Settings\\477780\\My Documents\\Ispirer\\SQLWays 5.0\\sqlways.ini /F=C:\\Praveen\\Tool Data\\inputs\\sample.sql /SOURCE=Sybase Adaptive Server Enterprise";
	      Process process=null;
	         try {
				process = Runtime.getRuntime().exec(dosCommand + " " + location);				
				
				//System.out.println(process);
				 InputStream in = process.getInputStream();
		         int ch;
		         while((ch = in.read()) != -1) {
		            System.out.print((char)ch);
		         }
				//process.notify();
				//System.out.println("::::notify:::"+);
		        // process.destroy();
				
		        
		         
	         } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//process.destroy();
			}*/
	         
	      //System.out.println("::::SqlWays End:::::");
		
	}
	/*public List readSqlWyasLog(){
		File lFile=null;
		List lFileNamesLst=new ArrayList();
		try {			
			//lFile=new File("C:\\Praveen\\Outputs\\sqlways.log");
			lFile=new File(lPathForConvertedTempFiles+lSQLWaysLogFile);
			InputStream is = new FileInputStream(lFile);
			StringBuffer buffer = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = is.read(b)) != -1;) {
				
				buffer.append(new String(b, 0, n));
			}			
			String _logDatStr = buffer.toString();			
			//System.out.println(str);
			//Reading Files List from Log File
			
			String lTmpStr=_logDatStr.substring(_logDatStr.indexOf("Reading scripts ("),_logDatStr.indexOf(")", _logDatStr.indexOf("Reading scripts (")));
			lTmpStr=lTmpStr.replaceAll("Reading scripts \\(|template ", "");
			String[] lFileNamesArr=lTmpStr.split("\\s*,\\s*");			
			for (int i = 0; i < lFileNamesArr.length; i++) {
				//lFileNamesArr[i]=lFileNamesArr[i].substring(lFileNamesArr[i].indexOf("_tmpFile_")+1,lFileNamesArr[i].length()).replaceAll("_sql", ".sql");
				String lTmpFileName=lFileNamesArr[i];
				String lTxtToReplace="\\";
				String lTmpStrToDelete=lTempFilesPath.replaceAll("\\\\", "_");
				lTmpStrToDelete=lTmpStrToDelete.replaceAll(":", "");
				lTmpFileName=lTmpFileName.replaceAll(lTmpStrToDelete, "").trim();
				lFileNamesArr[i]=lTmpFileName.replaceAll("_sql", ".sql");;				
				//System.out.println(lFileNamesArr[i]);
			}
			//Converting file (tmpFile_100.sql)...Ok (1 lines, 0 sec)
			//Checking Convertion Status in Log Data
			Pattern lConvertFilePat=null;
			String[] logDataArr=_logDatStr.split("\\r\\n");
			String lFileStatusStr="";
			for (int i = 0; i < logDataArr.length; i++) {
				for (int lFileCount = 0; lFileCount < lFileNamesArr.length; lFileCount++) {
					lConvertFilePat=Pattern.compile("Converting\\s+file\\s*\\(\\s*"+lFileNamesArr[lFileCount]+"\\s*\\)\\.\\.\\.");					
					if(lConvertFilePat.matcher(logDataArr[i]).find()){
						//System.out.println(logDataArr[i]);
						lFileStatusStr=logDataArr[i].substring(logDataArr[i].indexOf("...")+3,logDataArr[i].length()).trim();
						
						if(lFileStatusStr.length()>2){
							lFileStatusStr=lFileStatusStr.substring(0,2);
							//System.out.println(lFileStatusStr);
						}
						//System.out.println(logDataArr[i]);
						if("".equals(lFileStatusStr.trim())){
							lFileStatusStr="Failed";
						}
						if(lFileStatusStr.trim().equalsIgnoreCase("OK")){							
							lFileNamesLst.add(lFileNamesArr[lFileCount].trim());
							//System.out.println("File Name::"+lFileNamesArr[lFileCount]+"::Status::->"+lFileStatusStr);
						}
						//System.out.println("File Name::"+lFileNamesArr[lFileCount]+"::Status::->"+lFileStatusStr);
						lFileStatusStr="";						
						break;
					}
				}	
			}
			
					
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return lFileNamesLst;
		}
		return lFileNamesLst;
	}*/
	
	/*public void readConvertedTmpFiles(String pProjectId,String pRunSeq,String pSourceFilePath ){
		List lDynamicSqlDataList=lFrontEndParseForDSqlDAO.getDynamicSqlData(pProjectId,pRunSeq,pSourceFilePath,lConnection);
		HashMap lVBFuncConstMap=lFrontEndParseForDSqlDAO.getReplacedVbFuncData(pProjectId,pRunSeq,pSourceFilePath,lConnection);
		if(lDynamicSqlDataList==null || lDynamicSqlDataList.size()==0){
			return;
		}
		List lConvertedFilesList=readSqlWyasLog();		
		
		String lTmpFileName="";
		DynamicSQLDataDTO lDynamicSQLDataDTO=null;
		List pSourceLinesLst=null;
		String lPrevSourceFileName="";
		List lSourceLineNumsList=null;
		List lSourceLineStartKeywordsList=null;		
		String[] lConvertedDsqlLines=null;
		System.out.println("Tmp File Count::"+lConvertedFilesList.size());	
		System.out.println("DSQL List Count::"+lDynamicSqlDataList.size());	
		Pattern lCallPattern=Pattern.compile("^\\s*CALL\\s+");
		int lLineNumDiff=0;
		try {
			for (int i = 0; i < lDynamicSqlDataList.size()+1; i++) {
				if(i==lDynamicSqlDataList.size()){
					lDynamicSQLDataDTO=(DynamicSQLDataDTO)lDynamicSqlDataList.get(i-1);
				}else{
					lDynamicSQLDataDTO=(DynamicSQLDataDTO)lDynamicSqlDataList.get(i);
				}
				
				//System.out.println(":::lDynamicSQLDataDTO Filename::"+lDynamicSQLDataDTO.getTargetFileName());
				
				//Reading Source File - Start
				if((!(lPrevSourceFileName.equalsIgnoreCase(lDynamicSQLDataDTO.getSourceFileName()))) || (i==lDynamicSqlDataList.size()) ){
					if(i>0){
						
						//String lRootPath="C:\\Praveen\\bck\\1\\praveen\\Source";
						//"C:\\Praveen\\bck\\1\\praveen\\Target\\files\\1.vb";
						//String pSourceFilePath ="C:\\Praveen\\bck\\1\\praveen\\Source\\files\\1.vb";
						System.out.println("----------------------------------");
						String pSourceFullFilePath=pSourceFilePath+"\\"+lPrevSourceFileName;
						System.out.println("Source File Path::->"+pSourceFullFilePath);
						System.out.println("Source Root Path::->"+lRootPath);
						String lSourceRootParent=ToolsUtil.splitFileNameAndPath(lRootPath)[0];
						System.out.println("lSourceRootParent:::->"+lSourceRootParent);
						
						String lTargetRootPath=lSourceRootParent+"Target";
						System.out.println("lTargetRootPath:::->"+lTargetRootPath);
						
						String lSourceFileParent=ToolsUtil.splitFileNameAndPath(pSourceFullFilePath)[0];
						System.out.println("lSourceFileParent:::->"+lSourceFileParent);
						
									
						lRootPath=lRootPath.replaceAll("\\\\", "\\\\\\\\");
						System.out.println(lRootPath);
						//System.out.println(lSourceFileParent.contains(lRootPath));
						
						String lTargetFileParentDir=StringUtils.replace(lSourceFileParent,lRootPath.replaceAll("\\\\", "\\\\\\\\"),lTargetRootPath);			
						System.out.println("TargetFileParent::->"+lTargetFileParentDir);
						
						
						File lFile=new File(lTargetFileParentDir);
						boolean chk=lFile.mkdirs();
						System.out.println(lTargetFileParentDir+"  ::Created--> "+chk);
						//FileWriter lTargetFileWriter=new FileWriter(lTargetPathForFrontEndFiles+lPrevSourceFileName,false);
						lFile=new File(lTargetRootPath+lPrevSourceFileName);
						if(lFile.exists()){
							lFile.delete();						
						}
						
						FileWriter lTargetFileWriter=new FileWriter(lTargetFileParentDir+lPrevSourceFileName,false);						
						for (int j = 0; j < pSourceLinesLst.size(); j++) {
							lTargetFileWriter.append((String)pSourceLinesLst.get(j));
							if(j<pSourceLinesLst.size()-1){
								lTargetFileWriter.append("\r\n");
							}
							
						}
						lTargetFileWriter.close();
						System.out.println("File Generated:::->"+lPrevSourceFileName);
					}
					if(i==lDynamicSqlDataList.size()){
						return ;
					}
					pSourceLinesLst=readSourceFileData(lDynamicSQLDataDTO.getSourceFilePath(),lDynamicSQLDataDTO.getSourceFileName());
					System.out.println("Source File :: "+lDynamicSQLDataDTO.getSourceFileName()+" Size::"+pSourceLinesLst.size());
					lLineNumDiff=0;					
				}
				
				//Reading Source File - End		
				for (int j = 0; j < lConvertedFilesList.size(); j++) {		
					lTmpFileName=((String)lConvertedFilesList.get(j)).trim();				
					if(lDynamicSQLDataDTO.getTargetFileName().trim().equalsIgnoreCase(lTmpFileName)){
						lDynamicSQLDataDTO.setConvertionStatus("OK");
						//System.out.println("MatchedlTmpFileName::->"+lTmpFileName);
						//Reading SQL Wyas Converted Output - Start
						//File lTmpConvertedFile=new File("C:\\Praveen\\Outputs\\"+lTmpFileName);
						File lTmpConvertedFile=new File(lPathForConvertedTempFiles+lTmpFileName);
						InputStream is = new FileInputStream(lTmpConvertedFile);
						StringBuffer buffer = new StringBuffer();
						byte[] b = new byte[4096];
						for (int n; (n = is.read(b)) != -1;) {
							
							buffer.append(new String(b, 0, n));
						}
						String lConvertedDSQLQueryStr = buffer.toString();
						lConvertedDSQLQueryStr=lConvertedDSQLQueryStr.replaceAll("\"|(\\r\\n)|;", "");
						
						//Reading SQL Wyas Converted Output - End
						*//******************************************//*
						
						//Removing the added Constant Varaibles like DBT_S_VAR OR DB_VAR_CON-Start
						lConvertedDSQLQueryStr=removeTempVarConstants(lConvertedDSQLQueryStr);
						//Removing the added Constant Varaibles like DBT_S_VAR OR DB_VAR_CON-End
						
						*//******************************************//*						
						//Removing Delimeter Constants -Start
						System.out.println("--------------------------l");
						System.out.println("lConvertedDSQLQueryStr"+lConvertedDSQLQueryStr);
						lConvertedDSQLQueryStr=removeDelimeters(lConvertedDSQLQueryStr);
						//System.out.println("lConvertedDSQLQueryStr::->"+lConvertedDSQLQueryStr);					
						//Removing Delimeter Constants -End
						
						*//******************************************//*
						//Replces the Constants with the original vb functions -start
						lConvertedDSQLQueryStr=ReplaceConstsByOriginalVBFunctions(lVBFuncConstMap,lConvertedDSQLQueryStr);
						//Replces the Constants with the original vb functions -End
						*//******************************************//*
						
						//Splitting into Lines Using Delimeter Logic -Start
						//System.out.println("-------------"+lTmpFileName+"-----------------");
						
						//System.out.println(lConvertedDSQLQueryStr);
						//lConvertedDSQLQueryStr=lConvertedDSQLQueryStr.replaceAll("call[\\s\\r\\n\\w\\W]+_DBT_DEL_\\s*\\(","( _DBT_DEL_");
						lConvertedDSQLQueryStr=lConvertedDSQLQueryStr.replaceAll("(?i)\"_DBT_DEL_\\s*\\(","(\" _DBT_DEL_");;
						lConvertedDSQLQueryStr=lConvertedDSQLQueryStr.trim().replaceAll("_DBT_DEL_[\\s\\r\\n]*\\)$"," _DBT_DEL_");
						//System.out.println("lConvertedDSQLQueryStr::->"+lConvertedDSQLQueryStr);
						
						
						lConvertedDsqlLines=lConvertedDSQLQueryStr.trim().split("\\s*_DBT_DEL_[\\s\\r\\n]*");
						for (int k = 0; k < lConvertedDsqlLines.length; k++) {
							System.out.println(lConvertedDsqlLines[k]);
						}						
						//Splitting into Lines Using Delimeter Logic -End
						
						//System.out.println(_fileDateStr);
						
					}//if
					
				}//j end
				
				//Placing Converted Sqls in Source File-Start
				if(lDynamicSQLDataDTO.getConvertionStatus().trim().equalsIgnoreCase("OK")){								
					lSourceLineNumsList=lDynamicSQLDataDTO.getsQLLineNumsLst();
					lSourceLineStartKeywordsList=lDynamicSQLDataDTO.getsQLLineStartKeywordsLst();
					System.out.println("File Name ::"+lDynamicSQLDataDTO.getSourceFileName()+" ::Temp File:::->"+lDynamicSQLDataDTO.getTargetFileName());
					System.out.println("lSourceLineStartKeywordsList Size:::"+lSourceLineStartKeywordsList.size());
					System.out.println("lSourceLineNumsList Size:::"+lSourceLineNumsList.size());				
					System.out.println(lSourceLineNumsList);
					System.out.println("lConvertedDsqlLines Size:::"+lConvertedDsqlLines.length);
					for (int j = 0; j < lSourceLineStartKeywordsList.size(); j++) {
						System.out.println(lSourceLineStartKeywordsList.get(j));
					}
					for (int j = 0; j < lSourceLineNumsList.size(); j++) {
						int lLineNum= Integer.parseInt((String)lSourceLineNumsList.get(j));
						lLineNum=lLineNum+lLineNumDiff;
						
						String lSourceLineStr=(String)pSourceLinesLst.get(lLineNum-1);
						//System.out.println(lSourceLineStr);
						
						String lTabSpace=lSourceLineStr.substring(0,lSourceLineStr.indexOf(lDynamicSQLDataDTO.getFrontEndVarName().trim()));
						//System.out.println(lTabSpace.length());						
						String lNewLine=lTabSpace+((String)lSourceLineStartKeywordsList.get(j)).trim()+"\""+lConvertedDsqlLines[j].trim();						
						pSourceLinesLst.set(lLineNum-1,lNewLine);						
						pSourceLinesLst.add(lLineNum-1,lTabSpace+"'"+lSourceLineStr.trim());
						lLineNumDiff=lLineNumDiff+1;
						
						if(j==lSourceLineNumsList.size()-1 && lCallPattern.matcher(lConvertedDsqlLines[0].trim().toUpperCase()).find()){							
							lLineNum=Integer.parseInt((String)lDynamicSQLDataDTO.getInvokedLineNum());
							lLineNum=lLineNum+lLineNumDiff;
							lNewLine=lDynamicSQLDataDTO.getFrontEndVarName()+" = "+lDynamicSQLDataDTO.getFrontEndVarName()+" & \")\"";
							pSourceLinesLst.add(lLineNum-1,lTabSpace+lNewLine);
							lLineNumDiff=lLineNumDiff+1;
						}
						
					}
				}else{
					//System.out.println(lDynamicSQLDataDTO.getTargetFileName()+"--- Failed:: Line no:"+lDynamicSQLDataDTO.getInvokedLineNum());
					lSourceLineNumsList=lDynamicSQLDataDTO.getsQLLineNumsLst();
					lSourceLineStartKeywordsList=lDynamicSQLDataDTO.getsQLLineStartKeywordsLst();
					for (int j = 0; j < lSourceLineNumsList.size(); j++) {
						int lLineNum= Integer.parseInt((String)lSourceLineNumsList.get(j));						
						lLineNum=lLineNum+lLineNumDiff;						
						pSourceLinesLst.set(lLineNum-1,((String)pSourceLinesLst.get(lLineNum-1))+"  'Not Converted");
						//pSourceLinesLst[lLineNum-1]=pSourceLinesLst[lLineNum-1]+"  'Not Converted";
					}
				}//end if
				
				//Placing Converted Sqls in Source File-End
				
				
				lPrevSourceFileName=lDynamicSQLDataDTO.getSourceFileName();
			}//i end
			
			//Generating  Output File -start
			//FileWriter lOutFileWriter=new FileWriter("C:\\Praveen\\Tool Data\\inputs\\Outputs\\"+lDynamicSQLDataDTO.getSourceFileName(),false);
			FileWriter lTargetFileWriter=new FileWriter(lTargetPathForFrontEndFiles+lDynamicSQLDataDTO.getSourceFileName(),false);
			for (int j = 0; j < pSourceLinesLst.size(); j++) {
				lTargetFileWriter.append((String)pSourceLinesLst.get(j));
				if(j<pSourceLinesLst.size()-1){
					lTargetFileWriter.append("\r\n");
				}
				
			}
			lTargetFileWriter.close();
			
			System.out.println("Output File Generated");
			//Generating  Output File -End
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	/*public String removeTempVarConstants(String pQueryStr){
		
		//System.out.println("Before Replace ::->"+pQueryStr);
		
		pQueryStr=pQueryStr.replaceAll("_DBT_D_QUOTE_TEXT_START_|_DBT_D_QUOTE_TEXT_END_", "\"");
		//_DBT_AND_SYM_
		pQueryStr=pQueryStr.replaceAll( "_DBT_DEL_\\s*\\)"," ) _DBT_DEL_");
		pQueryStr=pQueryStr.replaceAll("_DBT_AND_SYM_", " & ");
		pQueryStr=pQueryStr.replaceAll("_DBT_AND_VAR_1_", "\" & ");
		pQueryStr=pQueryStr.replaceAll("_DBT_AND_VAR_2_", "  & \"");
		pQueryStr=pQueryStr.replaceAll("'_DBT_STR_CONCAT_S_","\" & \" '" );
		
		
		pQueryStr=pQueryStr.replaceAll("_DBT_VAR_CON_START_"," \"& ");
		pQueryStr=pQueryStr.replaceAll("_DBT_VAR_CON_END_"," &\" ");
		pQueryStr=pQueryStr.replaceAll("_DBT_VAR_S_START"," \"& ");
		pQueryStr=pQueryStr.replaceAll("_DBT_VAR_S_END"," &\" ");
		//_DBT_S_VAR_
		//System.out.println("After Replace ::->"+pQueryStr);
		return pQueryStr;
		
	}*/
	
	
	//not Using
	public String replaceSingleQuoteTextToVar(String pQueryStr){
		
		try {
			StringBuffer lQueryStrBuf=new StringBuffer(pQueryStr);
			Pattern pattern = Pattern.compile("'(.+?)'");		
			Matcher matcher = pattern.matcher(lQueryStrBuf);		
			while(matcher.find()){
				//System.out.println(matcher.start()+":::"+matcher.end());		
				//System.out.println(lQueryStrBuf.substring(matcher.start(),matcher.end()));
				
				String lTextToReplace=lReplceSingleQuoteText+lSingleQuoteTextCount+"_";
				lSingleQuoteTextCount=lSingleQuoteTextCount+1;
				lReplaceSingleQuotesMap.put(lTextToReplace.trim(),lQueryStrBuf.substring(matcher.start(),matcher.end()).trim()); 
				lQueryStrBuf=lQueryStrBuf.replace(matcher.start(), matcher.end(), lTextToReplace);			
				matcher = pattern.matcher(lQueryStrBuf);	
			}			
			pQueryStr=lQueryStrBuf.toString();
			//System.out.println("pQueryStr"+pQueryStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pQueryStr;
		
	}
	
	/*public String ReplaceConstsByOriginalVBFunctions(HashMap pVBFuncConstMap,String pQueryStr){
		
		try {			
			Iterator it = pVBFuncConstMap.entrySet().iterator();
			String lTmpVar="";
			while (it.hasNext()) {			
				Map.Entry lMapEntry = (Map.Entry)it.next();			
				if(lMapEntry.getKey()!=null && !"".equals((String)lMapEntry.getKey())){
					lTmpVar=(String)lMapEntry.getKey();					
					//System.out.println(lTmpVar+"::::"+(String)lMapEntry.getValue());
					if( !"".equals(lTmpVar.trim()) && pQueryStr.contains(lTmpVar.trim())){
						//pQueryStr=pQueryStr.replaceAll(lTmpVar.trim(),(String)lMapEntry.getValue());
						pQueryStr=StringUtils.replace(pQueryStr,lTmpVar.trim(),(String)lMapEntry.getValue());
					}					
				}				
			}			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pQueryStr;
	}*/
	/*public String removeDelimeters(String pQueryStr){
		
		// For Noramal Delimeters
		pQueryStr=pQueryStr.replaceAll("_DBT_DELSCS_\\s*'","'_DBT_DELSC_");
		pQueryStr=pQueryStr.replaceAll("_DBT_DELCSS_\\s*'","'_DBT_DELCS_" );
		pQueryStr=pQueryStr.replaceAll("_DBT_DELCS_\\s*'","'_DBT_DELC_" );
		pQueryStr=pQueryStr.replaceAll("_DBT_DELSC_\\s*,",",_DBT_DELS_");
		pQueryStr=pQueryStr.replaceAll("_DBT_DELS_\\s*'","'_DBT_DEL_");
		
		pQueryStr=pQueryStr.replaceAll("_DBT_DELC_\\s*,",",_DBT_DEL_");
		
		
		pQueryStr=pQueryStr.replaceAll("_DBT_DELDSCS_\\s*'","'_DBT_DELDSC_");
		pQueryStr=pQueryStr.replaceAll("_DBT_DELDCSS_\\s*'","'_DBT_DELDCS_" );
		pQueryStr=pQueryStr.replaceAll("_DBT_DELDCS_\\s*'","'_DBT_DELDC_" );
		pQueryStr=pQueryStr.replaceAll("_DBT_DELDSC_\\s*,",",_DBT_DELDS_");
		pQueryStr=pQueryStr.replaceAll("_DBT_DELDS_\\s*'","'_DBT_DELD_");
		pQueryStr=pQueryStr.replaceAll("_DBT_DELDS_\\s*\\(\\s*'","('_DBT_DELD_");
		pQueryStr=pQueryStr.replaceAll("_DBT_DELDC_\\s*,",",_DBT_DELD_");
		pQueryStr=pQueryStr.replaceAll("_DBT_DELD_","\"_DBT_DEL_");
		
		
		return pQueryStr;
	}*/
	/*public List readSourceFileData(String pPath,String pFileName){	
		String[] lines = null;		
		List lOutputLinesLst=new ArrayList();
		try {
			InputStream is = new FileInputStream(pPath+"\\"+pFileName);
			StringBuffer buffer = new StringBuffer();			
			byte[] b = new byte[4096];
			for (int n; (n = is.read(b)) != -1;) {
				
				buffer.append(new String(b, 0, n));
			}			
			String str = buffer.toString();			
			Pattern p = Pattern.compile("\r\n");                        
			lines = p.split(str);
			for (int i = 0; i < lines.length; i++) {
				lOutputLinesLst.add(lines[i]);
			}			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lOutputLinesLst;
	}*/

}
