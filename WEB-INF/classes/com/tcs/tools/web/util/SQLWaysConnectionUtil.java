package com.tcs.tools.web.util;

import java.io.InputStream;

import org.apache.log4j.Logger;

public class SQLWaysConnectionUtil {
	private static Logger logger = Logger.getLogger("ToolLogger");
	
	public static void runSqlWays(/*String pPath,*/String pFileName,String pOutputDirectory,String pInvokationMode){
		
		logger.info(":::::SQLWaysConnectionUtil-params-pFileName:::"+pFileName) ;
		logger.info(":::::SQLWaysConnectionUtil-params-pOutputDirectory:::"+pOutputDirectory) ;
		logger.info(":::::SQLWaysConnectionUtil-params-pInvokationMode:::"+pInvokationMode) ;
		FileUploadDownloadUtility.createFolders(pOutputDirectory);
		//System.out.println("::::SqlWays Started::::File Name::-> "+pFileName);
		String pSqlWaysHome = ToolsUtil.readProperty("sqlWaysRootPath");
		String dosCommand = "cmd /c "; //start
	      String location =
	    		    //" C:\\Praveen\\Tools\\SQL_Ways\\sqlways.exe /D=FIXED /TARGET=IBM DB2 " +
	    		  pSqlWaysHome +"sqlways.exe /D=FIXED /TARGET=IBM DB2 " +
		      		//" /DIR=C:\\Praveen\\Outputs\\ " +
		      		" /DIR=" +pOutputDirectory+" ";
		      		//" /INI=C:\\Documents and Settings\\477780\\My Documents\\Ispirer\\SQLWays 5.0\\sqlways.ini " +
		      		if("batch".equalsIgnoreCase(pInvokationMode)){
		      			location +=" /FF= "/*+ pPath.trim()+"\\"*/+pFileName.trim()  /*C:\\Praveen\\Temp_Files\\tmpFile.sql " + */ ;
		      		}else{
		      			location +=" /F= "/*+ pPath.trim()+"\\"*/+pFileName.trim() ; /*C:\\Praveen\\Temp_Files\\tmpFile.sql " + */
		      		}
	      	
		      		location +=" /SOURCE=Sybase Adaptive Server Enterprise" +
		      		" /LDEL = ;" ; 

	    		  //"C:\\Praveen\\Tools\\SQL_Ways\\sqlways.exe /D=FIXED /TARGET=IBM DB2 /DIR=C:\\Praveen\\Outputs\\ /INI=C:\\Documents and Settings\\477780\\My Documents\\Ispirer\\SQLWays 5.0\\sqlways.ini /F=C:\\Praveen\\Tool Data\\inputs\\sample.sql /SOURCE=Sybase Adaptive Server Enterprise";
	      Process process=null;
	         try {
	        	 logger.info("::::::command for sql ways getting invked:::::"+dosCommand + " " + location);
				//process = Runtime.getRuntime().exec(dosCommand + " " + location);				
	        	 process = Runtime.getRuntime().exec( location);
				System.out.println(process);
				 InputStream in = process.getInputStream();
		         int ch;
		         StringBuffer sb = new StringBuffer();
		         while((ch = in.read()) != -1) {
		           // System.out.print((char)ch);
		            //logger.info("::::::from command prompt output:::::"+(char)ch);
		        	 sb.append((char)ch);
		         }
				//process.notify();
				//System.out.println("::::notify:::"+);
		        // process.destroy();
		         logger.info("::::::from command prompt output:::::"+sb.toString());


	         } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info("::::;error in runSqlWays method::::"+e.getMessage());
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
				process.destroy();
			}

	      //System.out.println("::::SqlWays End:::::");

	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("::::inside main::::");
		
		String pPath ="C:\\pree\\";
		String pFileName="AdminWorker_java_tmpFile_1.sql";
		String pOutputDirectory="C:\\ajs\\";
		
		SQLWaysConnectionUtil lSQLWaysConnectionUtil = new SQLWaysConnectionUtil();
		lSQLWaysConnectionUtil.runSqlWays(pFileName, pOutputDirectory,"batch");

	}

}
