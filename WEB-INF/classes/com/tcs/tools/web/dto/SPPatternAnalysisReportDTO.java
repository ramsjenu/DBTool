package com.tcs.tools.web.dto;

public class SPPatternAnalysisReportDTO {

	/*SELECT RUN_ID, PROCEDURE_NAME, STATEMENT, STATEMENT_NO, 
	KEY_WORD, SCORE, PATTERN_ID, PATTERN_DESC, 
	FORMED_STATEMENT, CREATED_BY, CREATED_DATE, QUERY_COUNT
	FROM PATTERN_RESULTS_TABLE WHERE RUN_ID='PR001'; */
	
	private String procName ="";
	private String statementNo ="";
	private String statement ="";
	private String category ="";
	private String patternId="";
	private String patternDesc="";
	private String queryCount ="";
	private String score ="";
	private String formedStatement ="";
	
	
	/**
	 * @return the score
	 */
	public String getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(String score) {
		this.score = score;
	}
	/**
	 * @return the formedStatement
	 */
	public String getFormedStatement() {
		return formedStatement;
	}
	/**
	 * @param formedStatement the formedStatement to set
	 */
	public void setFormedStatement(String formedStatement) {
		this.formedStatement = formedStatement;
	}
	/**
	 * @return the procName
	 */
	public String getProcName() {
		return procName;
	}
	/**
	 * @param procName the procName to set
	 */
	public void setProcName(String procName) {
		this.procName = procName;
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
	 * @return the statement
	 */
	public String getStatement() {
		return statement;
	}
	/**
	 * @param statement the statement to set
	 */
	public void setStatement(String statement) {
		this.statement = statement;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the patternId
	 */
	public String getPatternId() {
		return patternId;
	}
	/**
	 * @param patternId the patternId to set
	 */
	public void setPatternId(String patternId) {
		this.patternId = patternId;
	}
	/**
	 * @return the patternDesc
	 */
	public String getPatternDesc() {
		return patternDesc;
	}
	/**
	 * @param patternDesc the patternDesc to set
	 */
	public void setPatternDesc(String patternDesc) {
		this.patternDesc = patternDesc;
	}
	/**
	 * @return the queryCount
	 */
	public String getQueryCount() {
		return queryCount;
	}
	/**
	 * @param queryCount the queryCount to set
	 */
	public void setQueryCount(String queryCount) {
		this.queryCount = queryCount;
	}
	
	
	
	
}
