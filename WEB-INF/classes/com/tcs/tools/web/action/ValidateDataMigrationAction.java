package com.tcs.tools.web.action;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.business.compare.dao.DBSoloFunction;
import com.tcs.tools.business.compare.dao.DBSoloFunctionLocalInsert;
import com.tcs.tools.business.compare.dao.DbSoloFunctionCompareTableData;
import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.dao.StatusDisplayAjaxDAO;
import com.tcs.tools.web.dto.ProjectDetailsMainDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class ValidateDataMigrationAction extends ActionSupport  {
	private String projectId = "";
	private HashMap projectNameIdMap = null;
	private String submitMode="";
	private String msgToJsp="";
	private String errorMsgToJsp="";
	
	private String tableName ="";
	
	private ProjectModifyDAO lProjectModifyDAO = new ProjectModifyDAO();
	private DBSoloFunction lDBSoloFunction = new DBSoloFunction();
	private DBSoloFunctionLocalInsert lDBSoloFunctionLocalInsert = new DBSoloFunctionLocalInsert();
	private DbSoloFunctionCompareTableData lDbSoloFunctionCompareTableData = new DbSoloFunctionCompareTableData();
	private StatusDisplayAjaxDAO lStatusDisplayAjaxDAO= new StatusDisplayAjaxDAO();
	private static Logger logger = Logger.getLogger("ToolLogger");
	
	public String execute() throws Exception {
		logger.info("::::inside ValidateDataMigrationAction:::::"+getSubmitMode());
		
		//get data for project drop down
		HashMap lProjectNameIdMap = lProjectModifyDAO.getProjectDetails(); 
		setProjectNameIdMap(lProjectNameIdMap);
		
		if("validateMigration".equalsIgnoreCase(getSubmitMode())){
			logger.info("::::inside ValidateDataMigrationAction->tableName:::::"+getTableName()+"<--->"+getProjectId());
			
			//clearing the data of the status
			lStatusDisplayAjaxDAO.removeCurStatusData(getProjectId());
			
			//for status window- start
			//Update Status to Front End - Start
			String lCurState="Started";
			String lStausMsg="Intializing process..."+new Timestamp(System.currentTimeMillis());
			Connection lConnection=DBConnectionManager.getConnection();			
			ToolsUtil.prepareInsertStatusMsg( getProjectId(), ToolConstant.CREATED_BY, lCurState,lStausMsg, "Heading",new Timestamp(System.currentTimeMillis()),lConnection);
			lStausMsg="Intiating Data validation for TABLE--->"+getTableName();
			ToolsUtil.prepareInsertStatusMsg( getProjectId(), ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
			//Update Status to Front End - End
			//for status window- end
			
			
			//Update Status to Front End - Start
			lCurState="Collecting Connection details";
			lStausMsg="Collecting Connection details for the selected project";		
			//Heading , Info , Error
			ToolsUtil.prepareInsertStatusMsg( getProjectId(), ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
			//lConnection.commit();
						
			//DBConnectionManager.closeConnection(lConnection);
			//Update Status to Front End - End
			
			ProjectDetailsMainDTO lProjectDetailsMainDTO =  lProjectModifyDAO.getProjectDetails(getProjectId());
			HashMap lDbConectionMap = new HashMap();
			lDbConectionMap.put("SOURCE_DB_HOST_IP" , lProjectDetailsMainDTO.getSourceDbIp());
			lDbConectionMap.put("SOURCE_DB_HOST_PORT" , lProjectDetailsMainDTO.getSourceDbPort());
			lDbConectionMap.put("SOURCE_DB_SCHEMA_NAME" , lProjectDetailsMainDTO.getSourceDbSchemaName());
			lDbConectionMap.put("SOURCE_DB_USER_NAME" , lProjectDetailsMainDTO.getSourceDbUserName());
			lDbConectionMap.put("SOURCE_DB_PASSWORD" , lProjectDetailsMainDTO.getSourceDbPassword());
			lDbConectionMap.put("SOURCE_DB_TYPE" , lProjectDetailsMainDTO.getSourceDBType());
			
			//for target
			lDbConectionMap.put("TARGET_DB_HOST_IP" , lProjectDetailsMainDTO.getTargetDbIp());
			lDbConectionMap.put("TARGET_DB_HOST_PORT" , lProjectDetailsMainDTO.getTargetDbPort());
			lDbConectionMap.put("TARGET_DB_SCHEMA_NAME" , lProjectDetailsMainDTO.getTargetDbSchemaName()); //gearsd1
			lDbConectionMap.put("TARGET_DB_USER_NAME" , lProjectDetailsMainDTO.getTargetDbUserName());
			lDbConectionMap.put("TARGET_DB_PASSWORD" , lProjectDetailsMainDTO.getTargetDbPassword());
			lDbConectionMap.put("TARGET_DB_TYPE" , lProjectDetailsMainDTO.getTargetDBType());			
			lDbConectionMap.put("TARGET_DB_NAME" , lProjectDetailsMainDTO.getTargetDbName()); //gearsd1
			
			//for temp testting in mysql
			//lDbConectionMap = new HashMap();
			//lDbConectionMap = lDBSoloFunction.getLocalDbConnectionDetails();
			//lDbConectionMap = lDBSoloFunctionLocalInsert.getLocalDbConnectionDetails();
			
			
			//lDBSoloFunctionLocalInsert.comapreTables(getTableName(),lDbConectionMap,getProjectId());
			
			lDbSoloFunctionCompareTableData.localDataCompare(getProjectId(), getTableName(), "TCS USER");
			
			//Update Status to Front End - Start
			lCurState="Completed";
			lStausMsg="Process Completed...."+new Timestamp(System.currentTimeMillis());		
			//Heading , Info , Error
			ToolsUtil.prepareInsertStatusMsg( getProjectId(), ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
			//lConnection.commit();
			//lConnection.setAutoCommit(true);			
			DBConnectionManager.closeConnection(lConnection);
			
			//Update Status to Front End - End
			
			setMsgToJsp("Records saved on "+ToolsUtil.getDateTime());
			
		}
		
		
		return SUCCESS;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public HashMap getProjectNameIdMap() {
		return projectNameIdMap;
	}

	public void setProjectNameIdMap(HashMap projectNameIdMap) {
		this.projectNameIdMap = projectNameIdMap;
	}

	public String getSubmitMode() {
		return submitMode;
	}

	public void setSubmitMode(String submitMode) {
		this.submitMode = submitMode;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getMsgToJsp() {
		return msgToJsp;
	}

	public void setMsgToJsp(String msgToJsp) {
		this.msgToJsp = msgToJsp;
	}

	public String getErrorMsgToJsp() {
		return errorMsgToJsp;
	}

	public void setErrorMsgToJsp(String errorMsgToJsp) {
		this.errorMsgToJsp = errorMsgToJsp;
	}

}
