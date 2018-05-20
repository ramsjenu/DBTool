package com.tcs.tools.business.compare.dto;

public class CursorDetailsDTO {
private String procedureName="";
private String cursorName="";
private String statementNo;
private String orderNo;
private String formedStatement="";
private String patternId="";

public String getPatternId() {
	return patternId;
}
public void setPatternId(String patternId) {
	this.patternId = patternId;
}
public String getFormedStatement() {
	return formedStatement;
}
public void setFormedStatement(String formedStatement) {
	this.formedStatement = formedStatement;
}
/**
 * @return the procedureName
 */
public String getProcedureName() {
	return procedureName;
}
/**
 * @param procedureName the procedureName to set
 */
public void setProcedureName(String procedureName) {
	this.procedureName = procedureName;
}
/**
 * @return the cursorName
 */
public String getCursorName() {
	return cursorName;
}
/**
 * @param cursorName the cursorName to set
 */
public void setCursorName(String cursorName) {
	this.cursorName = cursorName;
}
/**
 * @return the statementNo
 */
public String getStatementNo() {
	return statementNo;
}
/**
 * @param statementNo the statementNo to set
 */
public void setStatementNo(String statementNo) {
	this.statementNo = statementNo;
}
/**
 * @return the orderNo
 */
public String getOrderNo() {
	return orderNo;
}
/**
 * @param orderNo the orderNo to set
 */
public void setOrderNo(String orderNo) {
	this.orderNo = orderNo;
}

}
