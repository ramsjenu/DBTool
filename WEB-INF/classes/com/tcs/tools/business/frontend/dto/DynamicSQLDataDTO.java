package com.tcs.tools.business.frontend.dto;

import java.util.ArrayList;
import java.util.List;

public class DynamicSQLDataDTO {
	private String dynamicSqlStr="";
	private String sourceFilePath="";
	private String sourceFileName="";
	private String targetFileName="";	
	private List sQLLineNumsLst=new ArrayList();
	private List sQLLineStartKeywordsLst=new ArrayList(); // Mostly to Store Assigned VB Varaibles
	
	private String convertionStatus="";
	private String invokedLineNum="";
	private String frontEndVarName="";
	private String orginalDSQLQuery="";
	private String convertedQuery="";
	public String getDynamicSqlStr() {
		return dynamicSqlStr;
	}
	public void setDynamicSqlStr(String dynamicSqlStr) {
		this.dynamicSqlStr = dynamicSqlStr;
	}
	public String getSourceFileName() {
		return sourceFileName;
	}
	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}
	public String getTargetFileName() {
		return targetFileName;
	}
	public void setTargetFileName(String targetFileName) {
		this.targetFileName = targetFileName;
	}
	public List getsQLLineNumsLst() {
		return sQLLineNumsLst;
	}
	
	public String getInvokedLineNum() {
		return invokedLineNum;
	}
	public void setInvokedLineNum(String invokedLineNum) {
		this.invokedLineNum = invokedLineNum;
	}
	public void setsQLLineNumsLst(List sQLLineNums) {
		this.sQLLineNumsLst = sQLLineNums;
	}
	public String getSourceFilePath() {
		return sourceFilePath;
	}
	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}
	public List getsQLLineStartKeywordsLst() {
		return sQLLineStartKeywordsLst;
	}
	public void setsQLLineStartKeywordsLst(List sQLLineStartKeywordsLst) {
		this.sQLLineStartKeywordsLst = sQLLineStartKeywordsLst;
	}
	
	public String getConvertionStatus() {
		return convertionStatus;
	}
	public void setConvertionStatus(String convertionStatus) {
		this.convertionStatus = convertionStatus;
	}
	public String getFrontEndVarName() {
		return frontEndVarName;
	}
	public void setFrontEndVarName(String frontEndVarName) {
		this.frontEndVarName = frontEndVarName;
	}
	public String getOrginalDSQLQuery() {
		return orginalDSQLQuery;
	}
	public void setOrginalDSQLQuery(String orginalDSQLQuery) {
		this.orginalDSQLQuery = orginalDSQLQuery;
	}
	public String getConvertedQuery() {
		return convertedQuery;
	}
	public void setConvertedQuery(String convertedQuery) {
		this.convertedQuery = convertedQuery;
	}
	
	
}
