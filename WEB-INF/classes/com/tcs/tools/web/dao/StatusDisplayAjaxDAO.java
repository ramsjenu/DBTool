package com.tcs.tools.web.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class StatusDisplayAjaxDAO {
	
	 private Connection lConnection= null;
	 private PreparedStatement  lPreparedStatement = null;
	 private ResultSet lResultSet = null;
	 
	 private static Logger logger = Logger.getLogger("ToolLogger");
	 
	 public String getProgressStatusDetials(String pRunId) {
		 
		 String lStatusMsg ="";
		 try{
		 lConnection = DBConnectionManager.getConnection();
		
		 
		 lPreparedStatement =lConnection.prepareStatement(WebConstant.GET_UPLOAD_STATUS_DETAILS);
		 lPreparedStatement.setString(1,pRunId);			 
		 lResultSet = lPreparedStatement.executeQuery();
		 String lMsg="";
		 String lMsgType="";
		 String curState="";
		//RUN_ID, USER_ID, CURRENT_STAGE, STATUS_MSG, MSG_TYPE, CREATED_DATE
		 
		 if(lResultSet != null){
			 while (lResultSet.next()) {
				 lMsgType=lResultSet.getString("MSG_TYPE").trim();
				 lMsg=lResultSet.getString("STATUS_MSG");
				 //Heading Text
				 if(lMsgType.equalsIgnoreCase("Heading")){
					 lMsg="<font class=\"heading\">"+lMsg+"</font>";
					 
				 }else if(lMsgType.equalsIgnoreCase("Error")){ //Error Msg
					 lMsg="<font class=\"error\">"+lMsg+"</font>";
				 }else{// Normal Message to user
					 lMsg="<font class=\"msgText\">"+lMsg+"</font>"; 
					 
				 }
				 lStatusMsg +=lMsg.trim()+ "<br/>";
				 curState=lResultSet.getString("CURRENT_STAGE").trim();
			 }
			 lStatusMsg +=" _DBT_DELIM_ "+curState;
			 //lStatusMsg=lStatusMsg.replaceAll("\\n", "");
			 //_DBT_DELIM_
		 }
		 }catch (SQLException e) {
			e.printStackTrace();
			logger.info(":::::error in getSPSourceLocationDetail ::::"+e.getMessage());
		}catch (Exception e) {			
			e.printStackTrace();
			logger.info(":::::error in getSPSourceLocationDetail ::::"+e.getMessage());
		}finally{
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			DBConnectionManager.closeConnection(lConnection);
		}
		 return lStatusMsg;
	 }
	 
	 public void removeCurStatusData(String pRunId) {
		 try{
		 lConnection = DBConnectionManager.getConnection();
		 lPreparedStatement =lConnection.prepareStatement(WebConstant.REMOVE_CURRENT_STATUS_DETAILS);
		 lPreparedStatement.setString(1,pRunId);			 
		 lPreparedStatement.executeUpdate();
		 if(lConnection.getAutoCommit()==false){
			 lConnection.commit();
		 }
		 
		 }catch (SQLException e) {
			e.printStackTrace();
			logger.info(":::::error in removeCurStatusData ::::"+e.getMessage());
		}catch (Exception e) {			
			e.printStackTrace();
			logger.info(":::::error in removeCurStatusData ::::"+e.getMessage());
		}finally{
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			DBConnectionManager.closeConnection(lConnection);
		}
		 
	 }
	 
	 
	 
	 
	 
}
