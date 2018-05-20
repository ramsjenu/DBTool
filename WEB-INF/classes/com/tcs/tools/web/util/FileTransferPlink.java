package com.tcs.tools.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.tcs.tools.web.constant.WebConstant;

public class FileTransferPlink {
	private static Logger logger = Logger.getLogger("ToolLogger");
	
	/**
	 * modified method to get/put the files into server - use the same logic as of the main
	 * @param pHostName
	 * @param pUserName
	 * @param pPassword
	 * @param pCommand
	 */
	public static void executeSshCommands(String pHostName,String pUserName,String pPassword ,String pCommand){
		String hostname = pHostName;
		String username = pUserName;
		String password = pPassword;

		try {
			logger.info(":::::::inside executeSshCommands method::::");
			
			logger.info(":::::::inside executeSshCommands method::::-hostname->"+hostname);
			logger.info(":::::::inside executeSshCommands method::::-username->"+username);
			logger.info(":::::::inside executeSshCommands method::::-password->"+password);
			logger.info(":::::::inside executeSshCommands method::::-pCommand->"+pCommand);
			
			System.out.println(":::::::inside executeSshCommands method::::");
			
			System.out.println(":::::::inside executeSshCommands method::::-hostname->"+hostname);
			System.out.println(":::::::inside executeSshCommands method::::-username->"+username);
			System.out.println(":::::::inside executeSshCommands method::::-password->"+password);
			System.out.println(":::::::inside executeSshCommands method::::-pCommand->"+pCommand);
			
			
			
			String pSqlWaysHome = ToolsUtil.readProperty("sqlWaysRootPath");
			String dosCommand = ""; //start
			
			dosCommand += "cmd /c ";
			dosCommand += ToolsUtil.readProperty("puttyRootDir").trim();  
			dosCommand += "plink -l "+pUserName.trim()+" -pw "+pPassword.trim()+" -P 22 -ssh  "+pHostName.trim()+" \" "+pCommand+"\" ".trim();

		    		  //"C:\\Praveen\\Tools\\SQL_Ways\\sqlways.exe /D=FIXED /TARGET=IBM DB2 /DIR=C:\\Praveen\\Outputs\\ /INI=C:\\Documents and Settings\\477780\\My Documents\\Ispirer\\SQLWays 5.0\\sqlways.ini /F=C:\\Praveen\\Tool Data\\inputs\\sample.sql /SOURCE=Sybase Adaptive Server Enterprise";
		      Process process=null;
		      String location ="";
		         try {
		        	 logger.info("::::::command for sql ways getting invked:::::"+dosCommand + " " + location);
					process = Runtime.getRuntime().exec(dosCommand + " " + location);				

					//System.out.println(process);
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
			         process.destroy();
			         logger.info("::::::from command prompt output:::::"+sb.toString());
			         System.out.println("::::::from command prompt output:::::"+sb.toString());


		         } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.info("::::;error in runSqlWays method::::"+e.getMessage());
					logger.error(e.getMessage());
					logger.error(e.getStackTrace());
					process.destroy();
				}
			

		} catch (Exception e) {
			e.printStackTrace(System.err);
			logger.info(":::::exception in this methd:::::"+e.getMessage());
		}
	}
	
	
	public static void sshGetFiles(String pHostName,String pUserName,String pPassword ,String pRemoteFolderPath,String pLocalFolderPath){
		String hostname = pHostName;
		String username = pUserName;
		String password = pPassword;

		try {
			logger.info(":::::::inside executeSshCommands method::::");
			
			logger.info(":::::::inside sshGetFiles method::::-hostname->"+hostname);
			logger.info(":::::::inside sshGetFiles method::::-username->"+username);
			logger.info(":::::::inside sshGetFiles method::::-password->"+password);
			logger.info(":::::::inside sshGetFiles method::::-pSourceFolderPath->"+pRemoteFolderPath);
			logger.info(":::::::inside sshGetFiles method::::-pTargetFolderPath->"+pLocalFolderPath);
			
			System.out.println(":::::::inside executeSshCommands method::::");
			
			System.out.println(":::::::inside sshGetFiles method::::-hostname->"+hostname);
			System.out.println(":::::::inside sshGetFiles method::::-username->"+username);
			System.out.println(":::::::inside sshGetFiles method::::-password->"+password);
			System.out.println(":::::::inside sshGetFiles method::::-pSourceFolderPath->"+pRemoteFolderPath);
			System.out.println(":::::::inside sshGetFiles method::::-pTargetFolderPath->"+pLocalFolderPath);
			
			
			
			String pSqlWaysHome = ToolsUtil.readProperty("sqlWaysRootPath");
			String dosCommand = ""; //start
			
			dosCommand += "cmd /c ";
			dosCommand += ToolsUtil.readProperty("puttyRootDir").trim();  
			//dosCommand += "plink -l "+pUserName.trim()+" -pw "+pPassword.trim()+" -P 22 -ssh  "+pHostName.trim()+" \" "+pCommand+"\" ".trim();
			dosCommand += "pscp -l "+pUserName.trim()+" -pw "+pPassword.trim()+" "+pHostName.trim()+":"+pRemoteFolderPath+" "+pLocalFolderPath;
		    		  //"C:\\Praveen\\Tools\\SQL_Ways\\sqlways.exe /D=FIXED /TARGET=IBM DB2 /DIR=C:\\Praveen\\Outputs\\ /INI=C:\\Documents and Settings\\477780\\My Documents\\Ispirer\\SQLWays 5.0\\sqlways.ini /F=C:\\Praveen\\Tool Data\\inputs\\sample.sql /SOURCE=Sybase Adaptive Server Enterprise";
		      Process process=null;
		      String location ="";
		         try {
		        	 logger.info("::::::command for sql ways getting invked:::::"+dosCommand + " " + location);
					process = Runtime.getRuntime().exec(dosCommand + " " + location);				

					//System.out.println(process);
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
			         process.destroy();
			         logger.info("::::::from command prompt output:::::"+sb.toString());
			         System.out.println("::::::from command prompt output:::::"+sb.toString());


		         } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.info("::::;error in runSqlWays method::::"+e.getMessage());
					logger.error(e.getMessage());
					logger.error(e.getStackTrace());
					process.destroy();
				}
			

		} catch (Exception e) {
			e.printStackTrace(System.err);
			logger.info(":::::exception in this methd:::::"+e.getMessage());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(":::::inside main:::");
		System.out.println(":::::inside main:::");
		FileTransferPlink lFileTransferPlink = new FileTransferPlink();
		String pHostName ="48.113.192.157";
		String pUserName ="X174650";
		String pPassword  ="rahim123";
		String pCommand  ="ls -lrt";
		//lFileTransferPlink.executeSshCommands( pHostName, pUserName, pPassword , pCommand);
		
		String pRemoteFolderPath ="/prustaff/x174650/results.sql";
		String pLocalFolderPath ="C:\\DBTransplant\\arun\\tool_output\\example-hosts.txt";
		
		lFileTransferPlink.sshGetFiles( pHostName, pUserName, pPassword , pRemoteFolderPath, pLocalFolderPath);

	}

}
