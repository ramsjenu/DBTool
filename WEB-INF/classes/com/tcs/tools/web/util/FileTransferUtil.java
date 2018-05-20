package com.tcs.tools.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.tcs.tools.web.constant.WebConstant;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class FileTransferUtil {
	
	private static Logger logger = Logger.getLogger("ToolLogger");
	
	/**
	 * modified method to get/put the files into server - use the same logic as of the main
	 * @param pHostName
	 * @param pUserName
	 * @param pPassword
	 * @param pSourceFolderPath
	 * @param pTargetFolderPath
	 * @param pMode
	 */
	public static void sshGetFiles(String pHostName,String pUserName,String pPassword,String pSourceFolderPath,String pTargetFolderPath,String pMode){
		String hostname = pHostName;
		String username = pUserName;
		String password = pPassword;
		
		try {
			logger.info(":::::::inside sshGetFiles method::::");
			Connection conn = new Connection(hostname);
			ConnectionInfo lConnectionInfo = conn.connect();
			
			logger.info(":::::::inside sshGetFiles method-conn::::"+conn);
			logger.info(":::::::inside sshGetFiles method-lConnectionInfo::::"+lConnectionInfo);
			
			logger.info(":::::::inside sshGetFiles method::::-hostname->"+hostname);
			logger.info(":::::::inside sshGetFiles method::::-username->"+username);
			logger.info(":::::::inside sshGetFiles method::::-password->"+password);
			logger.info(":::::::inside sshGetFiles method::::-pSourceFolderPath->"+pSourceFolderPath);
			logger.info(":::::::inside sshGetFiles method::::-pTargetFolderPath->"+pTargetFolderPath);
			logger.info(":::::::inside sshGetFiles method::::-pMode->"+pMode);

			boolean isAuthenticated = conn.authenticateWithPassword(username,
					password);
			logger.info(":::::::inside sshGetFiles method-isAuthenticated::::"+isAuthenticated);
			if (isAuthenticated == false)
				throw new IOException("Authentication failed.");

			SCPClient objSCP = new SCPClient(conn);

			//String remoteTargetDirectory = "/prustaff/x174005/";
			//String localFile = "c:\\software_list.txt";
			String mode = "0777";

			if("get".equalsIgnoreCase(pMode)){
				logger.info(":::::::inside sshGetFiles method-file going to be taken::::");
				//get a file from server
				objSCP.get(pSourceFolderPath, pTargetFolderPath);
				logger.info(":::::::inside sshGetFiles method-file going taken succcessfully::::");
			}else if("put".equalsIgnoreCase(pMode)){
				// push the file to server
				//objSCP.put(localFile, remoteTargetDirectory, mode);
				logger.info(":::::::inside sshGetFiles method-file going to be moved::::");
				objSCP.put(pSourceFolderPath, pTargetFolderPath, mode);
				logger.info(":::::::inside sshGetFiles method-file going moved succcessfully::::");
			}
			
			logger.info(":::::::inside sshGetFiles method-over::::");
			

			// Following code is to get the list of files available on Unix box
			/*Session sess = conn.openSession();
			sess.execCommand("ls -lrt ");

			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader br = new BufferedReader(
					new InputStreamReader(stdout));

			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				System.out.println(line);
			}

			System.out.println("ExitCode: " + sess.getExitStatus());
			sess.close();*/

			// This is a must to free up resources
			conn.close();

		} catch (IOException e) {
			e.printStackTrace(System.err);
			logger.info(":::::exception in this methd:::::"+e.getMessage());
		}
		
	}
	
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
			
			
			Connection conn = new Connection(hostname);
			ConnectionInfo lConnectionInfo =conn.connect();
			 
			 logger.info(":::::::inside sshGetFiles method-executeSshCommandss::::"+lConnectionInfo);
			
			logger.info(":::::::inside executeSshCommands method-conn::::"+conn);
			boolean isAuthenticated = conn.authenticateWithPassword(username,
					password);
			logger.info(":::::::inside executeSshCommands method-isAuthenticated::::"+isAuthenticated);
			if (isAuthenticated == false)
				throw new IOException("Authentication failed.");

			SCPClient objSCP = new SCPClient(conn);
			
			

			//splitting the commands - start
			
			String[] lCommandArr = pCommand.split(WebConstant.TOOLS_UNIX_CMD_SPLITTER);
			//System.out.println("::::ltestvarL:::"+ltestvar);
			

			logger.info(":::::::inside executeSshCommands method-command going to be executed::::");
			// Following code is to get the list of files available on Unix box
			Session sess = conn.openSession();
			
			//executing an array of commands
			for (int i = 0; i < lCommandArr.length; i++) {
				logger.info(":::::::inside executeSshCommands method-command to be executed ::::->"+lCommandArr[i]+"<-");
				//System.out.println("::::array val:::"+lCommandArr[i]);
				sess.execCommand(lCommandArr[i].trim());
			}
			
			logger.info(":::::::inside executeSshCommands method-command executed successfully::::");
			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader br = new BufferedReader(
					new InputStreamReader(stdout));

			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				System.out.println(line);
			}

			System.out.println("ExitCode: " + sess.getExitStatus());
			sess.close();

			// This is a must to free up resources
			conn.close();

		} catch (IOException e) {
			e.printStackTrace(System.err);
			logger.info(":::::exception in this methd:::::"+e.getMessage());
		}
	}

	public static void main(String[] args) {
		System.out.println(":::::inside main::::");
		/**************************************sample code sent by dinesh-start***************************************************/
		/*String hostname = "";
		String username = "";
		String password = "";

		try {
			Connection conn = new Connection(hostname);
			conn.connect();

			boolean isAuthenticated = conn.authenticateWithPassword(username,
					password);

			if (isAuthenticated == false)
				throw new IOException("Authentication failed.");

			SCPClient objSCP = new SCPClient(conn);

			String remoteTargetDirectory = "/prustaff/x174005/";
			String localFile = "c:\\software_list.txt";
			String mode = "0777";

			// push the file to server
			objSCP.put(localFile, remoteTargetDirectory, mode);

			// Following code is to get the list of files available on Unix box
			Session sess = conn.openSession();
			sess.execCommand("ls -lrt ");

			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader br = new BufferedReader(
					new InputStreamReader(stdout));

			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				System.out.println(line);
			}

			System.out.println("ExitCode: " + sess.getExitStatus());
			sess.close();

			// This is a must to free up resources
			conn.close();

		} catch (IOException e) {
			e.printStackTrace(System.err);
		}*/
		/**************************************sample code sent by dinesh-end***************************************************/
		String pHostName ="";
		String pUserName ="";
		String pPassword ="";
		String pSourceFolderPath ="c:\\test_folder\\";
		String pTargetFolderPath ="/prustaff/x174005/software_list.txt";
		String pMode ="get";
		//for getting files from the server
		//sshGetFiles( pHostName, pUserName, pPassword, pSourceFolderPath, pTargetFolderPath, pMode);
		
		//for putting files into the server
		 pSourceFolderPath ="c:\\test_folder\\software_list.txt";
		 pTargetFolderPath ="/prustaff/x174005/";
		 pMode ="put";
		 //sshGetFiles( pHostName, pUserName, pPassword, pSourceFolderPath, pTargetFolderPath, pMode);
		 System.out.println("::::syso::::");
		 
		//for executing a command
		 String pCommand =" cd /abc "+WebConstant.TOOLS_UNIX_CMD_SPLITTER+" ls -lrt ";
		 executeSshCommands( pHostName, pUserName, pPassword , pCommand);
	}

}
