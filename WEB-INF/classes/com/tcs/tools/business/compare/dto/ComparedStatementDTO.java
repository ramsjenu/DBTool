package com.tcs.tools.business.compare.dto;

public class ComparedStatementDTO {
	private String compareSeq = "";
	private String orderNo = "";
	private String procName = "";
	private String sourcePatternId = "";
	private String targetPatternId = "";
	private String sourceFormedStatement = "";
	private String targetFormedStatement = "";
	private String sourceStatementNo="";
	private String targetStatementNo="";
	private String matchedYN="";
	

	/**
	 * @return the matchedYN
	 */
	public String getMatchedYN() {
		return matchedYN;
	}

	/**
	 * @param matchedYN the matchedYN to set
	 */
	public void setMatchedYN(String matchedYN) {
		this.matchedYN = matchedYN;
	}

	/**
	 * @return the sourceStatementNo
	 */
	public String getSourceStatementNo() {
		return sourceStatementNo;
	}

	/**
	 * @param sourceStatementNo the sourceStatementNo to set
	 */
	public void setSourceStatementNo(String sourceStatementNo) {
		this.sourceStatementNo = sourceStatementNo;
	}

	/**
	 * @return the targetStatementNo
	 */
	public String getTargetStatementNo() {
		return targetStatementNo;
	}

	/**
	 * @param targetStatementNo the targetStatementNo to set
	 */
	public void setTargetStatementNo(String targetStatementNo) {
		this.targetStatementNo = targetStatementNo;
	}

	/**
	 * @return the compareSeq
	 */
	public String getCompareSeq() {
		return compareSeq;
	}

	/**
	 * @param compareSeq
	 *            the compareSeq to set
	 */
	public void setCompareSeq(String compareSeq) {
		this.compareSeq = compareSeq;
	}

	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo
	 *            the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the procName
	 */
	public String getProcName() {
		return procName;
	}

	/**
	 * @param procName
	 *            the procName to set
	 */
	public void setProcName(String procName) {
		this.procName = procName;
	}

	/**
	 * @return the sourcePatternId
	 */
	public String getSourcePatternId() {
		return sourcePatternId;
	}

	/**
	 * @param sourcePatternId
	 *            the sourcePatternId to set
	 */
	public void setSourcePatternId(String sourcePatternId) {
		this.sourcePatternId = sourcePatternId;
	}

	/**
	 * @return the targetPatternId
	 */
	public String getTargetPatternId() {
		return targetPatternId;
	}

	/**
	 * @param targetPatternId
	 *            the targetPatternId to set
	 */
	public void setTargetPatternId(String targetPatternId) {
		this.targetPatternId = targetPatternId;
	}

	/**
	 * @return the sourceFormedStatement
	 */
	public String getSourceFormedStatement() {
		return sourceFormedStatement;
	}

	/**
	 * @param sourceFormedStatement
	 *            the sourceFormedStatement to set
	 */
	public void setSourceFormedStatement(String sourceFormedStatement) {
		this.sourceFormedStatement = sourceFormedStatement;
	}

	/**
	 * @return the targetFormedStatement
	 */
	public String getTargetFormedStatement() {
		return targetFormedStatement;
	}

	/**
	 * @param targetFormedStatement
	 *            the targetFormedStatement to set
	 */
	public void setTargetFormedStatement(String targetFormedStatement) {
		this.targetFormedStatement = targetFormedStatement;
	}

}
