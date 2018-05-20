package com.tcs.tools.business.analysis.dto;

public class PatternSummaryDTO {
	private String runId = "";
	private String procedureName = "";
	private String statement = "";
	private String statementNo = "";
	private String keyWord = "";
	private String score = "";
	private String patternId = "";
	private String patternDesc = "";
	private String formedStatement ="";
	private String queryCount="";
	private String dbMigrationType ="";
	private String folderPath ="";
	
	/**
	 * @return the folderPath
	 */
	public String getFolderPath() {
		return folderPath;
	}

	/**
	 * @param folderPath the folderPath to set
	 */
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
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
	 * @return the runId
	 */
	public String getRunId() {
		return runId;
	}

	/**
	 * @param runId
	 *            the runId to set
	 */
	public void setRunId(String runId) {
		this.runId = runId;
	}

	/**
	 * @return the procedureName
	 */
	public String getProcedureName() {
		return procedureName;
	}

	/**
	 * @param procedureName
	 *            the procedureName to set
	 */
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	/**
	 * @return the statement
	 */
	public String getStatement() {
		return statement;
	}

	/**
	 * @param statement
	 *            the statement to set
	 */
	public void setStatement(String statement) {
		this.statement = statement;
	}

	/**
	 * @return the statementNo
	 */
	public String getStatementNo() {
		return statementNo;
	}

	/**
	 * @param statementNo
	 *            the statementNo to set
	 */
	public void setStatementNo(String statementNo) {
		this.statementNo = statementNo;
	}

	/**
	 * @return the keyWord
	 */
	public String getKeyWord() {
		return keyWord;
	}

	/**
	 * @param keyWord
	 *            the keyWord to set
	 */
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	/**
	 * @return the score
	 */
	public String getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(String score) {
		this.score = score;
	}

	/**
	 * @return the patternId
	 */
	public String getPatternId() {
		return patternId;
	}

	/**
	 * @param patternId
	 *            the patternId to set
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
	 * @param patternDesc
	 *            the patternDesc to set
	 */
	public void setPatternDesc(String patternDesc) {
		this.patternDesc = patternDesc;
	}

	/**
	 * @return the dbMigrationType
	 */
	public String getDbMigrationType() {
		return dbMigrationType;
	}

	/**
	 * @param dbMigrationType the dbMigrationType to set
	 */
	public void setDbMigrationType(String dbMigrationType) {
		this.dbMigrationType = dbMigrationType;
	}
	
	
}
