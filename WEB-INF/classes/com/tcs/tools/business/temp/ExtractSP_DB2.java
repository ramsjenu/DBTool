package com.tcs.tools.business.temp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class ExtractSP_DB2 {
	/*static String pSchema ="GXPRD01";
	static String pDatabase="GEARSD1";	
	static String pUserId="x174005";
	static String pPassword ="Aug@2012";
	static String pIP ="48.140.158.105";	
	static String pPort ="50012";*/
	
	//qa server
	/*static String pSchema ="GXPRD01";
	static String pDatabase="GEARSQ1";	
	static String pUserId="X174005";
	static String pPassword ="Aug@2012";
	static String pIP ="48.140.158.105";	
	static String pPort ="50022";*/
	
	//uat server
	/*static String pSchema ="GXPRD01";
	static String pDatabase="GEARSU1";	
	static String pUserId="X174005";
	static String pPassword ="Aug@2012";
	static String pIP ="48.126.157.82";
	static String pPort ="50012";*/

	//for lcms
	static String pSchema = "LCMS";
	static String pDatabase = "LCMSD3";

	static String pUserId = "X139130";
	static String pPassword = "sept2012";
	static String pIP = "48.160.129.159";

	//static String pIP ="48.126.157.82";
	static String pPort = "50012";

	static String pOutputDirectory = "C:\\arun\\tool_op\\";
	
	
	//pOutputDirectory = pOutputDirectory+"\\"+pDatabase+"_"+new java.text.SimpleDateFormat("dd_MM_yyyy").format(new java.util.Date())) ;

	/*public static Connection connectSybase(){
		try{
			Connection lConnection = DBConnectionManager.getDb2Connection ("X174005", "asdf1234","48.140.158.105", "50012",pDatabase ,pSchema) ;
			
			return lConnection;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}*/
	public static String getSpText(Connection con,String pSPName,String pObjectType){
		String pSpText="";
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		try{
			String sql= "";
			
			if("SP".equalsIgnoreCase(pObjectType)){
				sql ="SELECT text as SP_TEXT FROM syscat.procedures WHERE PROCSCHEMA='"+pSchema.toUpperCase()+"' AND PROCNAME='"+pSPName+"'";
			}else if("TRIGGER".equalsIgnoreCase(pObjectType)){
				sql ="SELECT text as SP_TEXT FROM syscat.triggers WHERE TRIGSCHEMA='"+pSchema.toUpperCase()+"' AND TRIGNAME='"+pSPName+"'";
			}else if("VIEW".equalsIgnoreCase(pObjectType)){
				sql ="SELECT text as SP_TEXT FROM syscat.views WHERE VIEWSCHEMA='"+pSchema.toUpperCase()+"' AND VIEWNAME='"+pSPName+"'";
			}else if("FUNCTION".equalsIgnoreCase(pObjectType)){
				sql ="SELECT body as SP_TEXT FROM syscat.functions WHERE FUNCSCHEMA='"+pSchema.toUpperCase()+"' AND FUNCNAME='"+pSPName+"'";
			}
			
			System.out.println(":::sql::"+sql);
			ps1 = con.prepareStatement(sql);
			//ps1.setString(1,pSPName);
			rs1 = ps1.executeQuery();
			while(rs1.next()){
				pSpText +=rs1.getString("SP_TEXT")+"\n";
				//ToolsUtil.writeToFile(rs.getString("SP_TEXT").trim(), pOutputDirectory+rs.getString("SP_NAME")+".sql", pOutputDirectory);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			//DBConnectionManager.closeConnection(con);
		}
		//System.out.println(pSpText);
		return pSpText;
	}
	public static void getSPs(){
		/*Connection con = DBConnectionManager.getSybaseConnection ("X120401", 
				"X120401", 
				"48.160.4.77", 
				"4200", 
				"LCMS"); */
		
		Connection con = DBConnectionManager.getDb2Connection (pUserId, pPassword,pIP, pPort,pDatabase ,pSchema) ;
		
		System.out.println("::con:::"+con);
		//pOutputDirectory += "\\"+pDatabase+"_";
		//pOutputDirectory = pOutputDirectory+"\\"+pDatabase+"_"+new java.text.SimpleDateFormat("dd_MM_yyyy_hhmm").format(new java.util.Date())+"\\" ;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="SELECT PROCNAME AS SP_NAME FROM syscat.procedures WHERE PROCSCHEMA='"+pSchema.toUpperCase()+"' ";
		
		
		try{
			System.out.println(":::sql::::"+sql);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				 //getSpText( con,rs.getString("SP_TEXT"));
				String pSpText =getSpText( con,rs.getString("SP_NAME"),"SP");
				System.out.println(rs.getString("SP_NAME"));
				ToolsUtil.writeToFile(pSpText, pOutputDirectory+"\\SP\\"+rs.getString("SP_NAME")+".sql", pOutputDirectory+"\\SP\\");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(con);
		}
	}
	
	public static void getTriggers(){
		/*Connection con = DBConnectionManager.getSybaseConnection ("X120401", 
				"X120401", 
				"48.160.4.77", 
				"4200", 
				"LCMS"); */
		
		Connection con = DBConnectionManager.getDb2Connection (pUserId, pPassword,pIP, pPort,pDatabase ,pSchema) ;
		
		System.out.println("::con:::"+con);
		//pOutputDirectory += "\\"+pDatabase+"_";
		//pOutputDirectory = pOutputDirectory+"\\"+pDatabase+"_"+new java.text.SimpleDateFormat("dd_MM_yyyy_hhmm").format(new java.util.Date())+"\\" ;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql=" SELECT TRIGNAME AS SP_NAME  FROM syscat.triggers WHERE TRIGSCHEMA='"+pSchema.toUpperCase()+"' ";
		
		
		try{
			System.out.println(":::sql::::"+sql);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				 //getSpText( con,rs.getString("SP_TEXT"));
				String pSpText =getSpText( con,rs.getString("SP_NAME"),"TRIGGER");
				System.out.println(rs.getString("SP_NAME"));
				ToolsUtil.writeToFile(pSpText, pOutputDirectory+"\\TRIGGER\\"+rs.getString("SP_NAME")+".sql", pOutputDirectory+"\\TRIGGER\\");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(con);
		}
	}
	
	public static void getViews(){
		/*Connection con = DBConnectionManager.getSybaseConnection ("X120401", 
				"X120401", 
				"48.160.4.77", 
				"4200", 
				"LCMS"); */
		
		Connection con = DBConnectionManager.getDb2Connection (pUserId, pPassword,pIP, pPort,pDatabase ,pSchema) ;
		
		System.out.println("::con:::"+con);
		//pOutputDirectory += "\\"+pDatabase+"_";
		
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql=" SELECT VIEWNAME AS SP_NAME  FROM syscat.views WHERE VIEWSCHEMA='"+pSchema.toUpperCase()+"' ";
		
		
		try{
			//System.out.println(":::sql::::"+sql);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				 //getSpText( con,rs.getString("SP_TEXT"));
				String pSpText =getSpText( con,rs.getString("SP_NAME"),"VIEW");
				System.out.println(rs.getString("SP_NAME"));
				ToolsUtil.writeToFile(pSpText, pOutputDirectory+"\\VIEW\\"+rs.getString("SP_NAME")+".sql", pOutputDirectory+"\\VIEW\\");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(con);
		}
	}
	
	public static void getFunction(){
		/*Connection con = DBConnectionManager.getSybaseConnection ("X120401", 
				"X120401", 
				"48.160.4.77", 
				"4200", 
				"LCMS"); */
		
		Connection con = DBConnectionManager.getDb2Connection (pUserId, pPassword,pIP, pPort,pDatabase ,pSchema) ;
		
		System.out.println("::con:::"+con);
		//pOutputDirectory += "\\"+pDatabase+"_";
		
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql=" SELECT FUNCNAME AS SP_NAME  FROM syscat.functions WHERE FUNCSCHEMA='"+pSchema.toUpperCase()+"' ";
		
		
		try{
			//System.out.println(":::sql::::"+sql);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				 //getSpText( con,rs.getString("SP_TEXT"));
				String pSpText =getSpText( con,rs.getString("SP_NAME"),"FUNCTION");
				System.out.println(rs.getString("SP_NAME"));
				ToolsUtil.writeToFile(pSpText, pOutputDirectory+"\\FUNCTION\\"+rs.getString("SP_NAME")+".sql", pOutputDirectory+"\\FUNCTION\\");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(con);
		}
	}
	public static void main(String args[]){
		
		pOutputDirectory = pOutputDirectory+"\\"+pDatabase+"_"+new java.text.SimpleDateFormat("dd_MM_yyyy_hhmm").format(new java.util.Date())+"\\" ;
		getSPs( );
		getTriggers();
		getViews();
		getFunction();
		System.out.println("::sp extraction over");
		
		/*String ltest ="2012/12/31";
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
		
		System.out.println(":::::::inside main:::"+df.format(new java.util.Date(ltest)));*/
	}
}
