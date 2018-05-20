package com.tcs.tools.web.action;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.web.dao.StatusDisplayAjaxDAO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class StatusDisplayAjaxAction extends ActionSupport {
	private static Logger logger = Logger.getLogger("ToolLogger");
	
	private String submitMode="";
	private String runId="";
	private String refreshMode="";
	private String completeStatusText="";
	
	StatusDisplayAjaxDAO lStatusDisplayAjaxDAO = new StatusDisplayAjaxDAO();
	public String execute() throws Exception {
		
		logger.info(":::inside StatusDisplayAjaxAction :: submit mode::::"+getSubmitMode());	
		
		if("ajax".equalsIgnoreCase(getSubmitMode())){
			logger.info(":::inside  StatusDisplayAjaxAction ::ajax mode::::Run Id"+getRunId());			
			
			String lDetailedStatusMsg = lStatusDisplayAjaxDAO.getProgressStatusDetials(getRunId());
			HttpServletResponse response =ServletActionContext.getResponse();			
			//for text sending the status to the front end			
			//Write the HTML to response			
		    response.setContentType("text/html");
		    String html ="test data from server new 1234567....";
		    PrintWriter out = response.getWriter();
		    out.println(lDetailedStatusMsg);
		    out.flush();
		    
			return null;
		}
		
		if( ("init".equalsIgnoreCase(getSubmitMode()) ) || ("initNoDel".equalsIgnoreCase(getSubmitMode()))){
			if("initNoDel".equalsIgnoreCase(getSubmitMode())){
				return "init";
			}
			int lDeleteCount =0;
			logger.info(":::inside StatusDisplayAjaxAction :: init mode::::Run Id"+getRunId()+"::::::getSubmitMode:::->"+getSubmitMode());
			
			//Removing Old Status of the Current running project.
			String lSQL = " DELETE FROM CURRENT_PROCESS_STATUS_TABLE WHERE RUN_ID ='"+getRunId().trim()+"'"; 			
 			System.out.println("::::lSQL::::"+lSQL);
 			Connection lConnection=DBConnectionManager.getConnection();
 			PreparedStatement lPreparedStatement = lConnection.prepareStatement(lSQL);
 			//lDeleteCount = lPreparedStatement.executeUpdate();
 			 if(lConnection.getAutoCommit()==false){
 				 lConnection.commit();
 			 }
 			System.out.println("::StatusDisplayAjaxAction ->::Status lDeleteCount::::"+lDeleteCount);

 			//Starting Process Status
 			String lCurState="Started";
			String lStausMsg="Clearing buffer... ";
			ToolsUtil.prepareInsertStatusMsg( getRunId(), ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),lConnection);
			
			return "init";
		}
		
		if("reload".equalsIgnoreCase(getSubmitMode())){
			logger.info(":::inside StatusDisplayAjaxAction :: Reload mode::::Process Completed :: Reload::"+getCompleteStatusText());
			setRefreshMode("Yes");
			setCompleteStatusText(getCompleteStatusText());
		}
		if("chk".equalsIgnoreCase(getSubmitMode())){
			logger.info(":::inside StatusDisplayAjaxAction :: Chk mode::::"+getRunId());
		}
		if("finalStatusDisplay".equalsIgnoreCase(getSubmitMode())){
			
			logger.info(":::inside StatusDisplayAjaxAction ::finalStatusDisplay::::"+getRunId());
			String lFinalStatusMsg = lStatusDisplayAjaxDAO.getProgressStatusDetials(getRunId());
			
			setCompleteStatusText(lFinalStatusMsg);
			logger.info(":::inside StatusDisplayAjaxAction ::finalStatusDisplay::::"+getCompleteStatusText());
			lStatusDisplayAjaxDAO.removeCurStatusData(getRunId());
			return "finalStatusDisplay";
		}
		return SUCCESS;
	}

	public String getSubmitMode() {
		return submitMode;
	}

	public void setSubmitMode(String submitMode) {
		this.submitMode = submitMode;
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public String getRefreshMode() {
		return refreshMode;
	}

	public void setRefreshMode(String refreshMode) {
		this.refreshMode = refreshMode;
	}

	public String getCompleteStatusText() {
		return completeStatusText;
	}

	public void setCompleteStatusText(String completeStatusText) {
		this.completeStatusText = completeStatusText;
	}
	
	

}
