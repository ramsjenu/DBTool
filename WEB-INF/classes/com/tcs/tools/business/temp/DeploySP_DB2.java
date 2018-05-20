package com.tcs.tools.business.temp;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tcs.tools.web.dto.StoredProceduresDetailsDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class DeploySP_DB2 {
	static String pSchema ="GXPRD01";
	static String pDatabase="GEARSD1";	
	
	//static String pUserId="X174005";
	//static String pPassword ="zxcv1234";
	
	static String pUserId="X174650";
	static String pPassword ="rahim789";
	static String pIP ="48.140.158.105";
	static String pPort ="50012";
	
	
	

	
	
	
	
	public static void deploySP(String pSPLocation){
		List  lSPList = ToolsUtil.getFileNamesFromFolderDTO(new File(pSPLocation), new ArrayList());
		Connection con = DBConnectionManager.getDb2Connection (pUserId, pPassword,pIP, pPort,pDatabase ,pSchema) ;
		
		Statement lStatement=null;
		Statement lStatement1=null;
        try{
        	 lStatement = con.createStatement();
        	 String qualifier =pSchema;
        	 lStatement.execute( "set CURRENT SCHEMA = "  + qualifier);
        	 lStatement.execute( "SET CURRENT PATH = SYSTEM PATH, "  + qualifier);         
        } catch (Exception e) {
        	e.printStackTrace();
       	}finally  {
       		DBConnectionManager.closeConnection(lStatement, null);
        }
       
       
		try {
			
			
			String lSourceFilePath ="";
			String lSPContent="";
			for (int i = 0; i < lSPList.size(); i++) {
				StoredProceduresDetailsDTO lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
				lStoredProceduresDetailsDTO = (StoredProceduresDetailsDTO)lSPList.get(i);
				lSourceFilePath = lStoredProceduresDetailsDTO.getFolderPath()+"\\"+lStoredProceduresDetailsDTO.getProcName();
				lSPContent = ToolsUtil.readFile(new File(lSourceFilePath));
				
				if(lSPContent.contains("#")){
					lSPContent = lSPContent.substring(0, lSPContent.lastIndexOf("#")) ;
				}
				
				System.out.println(lStoredProceduresDetailsDTO.getProcName()+"->"+"Being Created");
				try{
				//System.out.println(lSPContent);
					//System.out.println(":::lSPContent:::"+lSPContent);
					lStatement1 = con.createStatement();
					lStatement1.executeUpdate(lSPContent);
					
					System.out.println(lStoredProceduresDetailsDTO.getProcName()+"->"+"Created Successfully");
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					System.out.println(lStoredProceduresDetailsDTO.getProcName()+"->"+"Failed Creating SP");
				}
				



			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(con);
			DBConnectionManager.closeConnection(lStatement1, null);
		}
		
	}
	
	public static void deployPermissions(String pSPLocation){
		List  lSPList = ToolsUtil.getFileNamesFromFolderDTO(new File(pSPLocation), new ArrayList());
		Connection con = DBConnectionManager.getDb2Connection (pUserId, pPassword,pIP, pPort,pDatabase ,pSchema) ;
		
		Statement lStatement=null;
		Statement lStatement1=null;
        try{
        	 lStatement = con.createStatement();
        	 String qualifier =pSchema;
        	 lStatement.execute( "set CURRENT SCHEMA = "  + qualifier);
        	 lStatement.execute( "SET CURRENT PATH = SYSTEM PATH, "  + qualifier);         
        } catch (Exception e) {
        	e.printStackTrace();
       	}finally  {
       		DBConnectionManager.closeConnection(lStatement, null);
        }
       
       
		try {
			
			
			String lSourceFilePath ="";
			String lSPContent="";
			for (int i = 0; i < lSPList.size(); i++) {
				StoredProceduresDetailsDTO lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
				lStoredProceduresDetailsDTO = (StoredProceduresDetailsDTO)lSPList.get(i);
				lSourceFilePath = lStoredProceduresDetailsDTO.getFolderPath()+"\\"+lStoredProceduresDetailsDTO.getProcName();
				lSPContent = ToolsUtil.readFile(new File(lSourceFilePath));
				
				if(lSPContent.contains("#")){
					lSPContent = lSPContent.substring(0, lSPContent.lastIndexOf("#")) ;
				}
				
				String[] lPermissionContentArr = lSPContent.split("\\;");
				System.out.println(lStoredProceduresDetailsDTO.getProcName()+"->"+"Being Created");
				
				for (int j = 0; j < lPermissionContentArr.length; j++) {
					
					if(lPermissionContentArr[j] == null || lPermissionContentArr[j].trim().equals("")){
						continue;
					}
				
					try{
					//System.out.println(lSPContent);
						//System.out.println(":::lSPContent:::"+lSPContent);
						lStatement1 = con.createStatement();
						lPermissionContentArr[j] = lPermissionContentArr[j].replaceAll("\\;", "");
						System.out.println("::::lPermissionContentArr[j]:::"+lPermissionContentArr[j]);
						lStatement1.executeUpdate(lPermissionContentArr[j]);
						
						System.out.println(lStoredProceduresDetailsDTO.getProcName()+"->"+"Created Successfully");
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						System.out.println(lStoredProceduresDetailsDTO.getProcName()+"->"+"Failed Creating SP");
					}
				
				}


			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(con);
			DBConnectionManager.closeConnection(lStatement1, null);
		}
		
	}
	public static void dropSP(){
		Statement lStatement=null;
		Statement lStatement1=null;
		Connection con = DBConnectionManager.getDb2Connection (pUserId, pPassword,pIP, pPort,pDatabase ,pSchema) ;
		
        try{
        	 lStatement = con.createStatement();
        	 String qualifier =pSchema;
        	 lStatement.execute( "set CURRENT SCHEMA = "  + qualifier);
        	 lStatement.execute( "SET CURRENT PATH = SYSTEM PATH, "  + qualifier); 
        	 
        	 lStatement1 = con.createStatement();
        	 lStatement1.executeUpdate("drop procedure TEST_JAVA"); //TEST_JAVA ,TEST_JAVA_new
        	 
        } catch (Exception e) {
        	e.printStackTrace();
       	}finally  {
       		DBConnectionManager.closeConnection(lStatement, null);
       		DBConnectionManager.closeConnection(lStatement1, null);
       		DBConnectionManager.closeConnection(con);
        }
	}
	
	public static void main(String args[]){
		String pSPLocation="C:\\arun\\tool_ip\\18june_sp\\";
		deploySP(pSPLocation);
		//deployPermissions(pSPLocation);
		//dropSP();
		//getSPs();
		System.out.println("::sp deployed successsfully");
		
		/*String ltest ="2012/12/31";
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
		
		System.out.println(":::::::inside main:::"+df.format(new java.util.Date(ltest)));*/
	}
}
