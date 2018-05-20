package com.tcs.tools.business.compare.dto;

public class ComparedKeywordDTO {
	private String compareSeq = "";
	private String compareKeywordSeq = "";
	private String orderNo = "";
	private String procName = "";	
	private String prevKeyword="";
	private String matchedYN="";
	
	
	//Source
	
	private String sourceStatementNo="";
	private String sourcePatternId = "";
	private String sourceKeyword="";
	private String sourceIndex="";
	private String sourcePatternType="";
	private String sourceAlias="";
	private String sourceLevelCount="";
	private String sourceFormedStatement = "";
	private String sourcePrimaryStatement = "";
	//Target
	private String targetStatementNo="";
	private String targetPatternId = "";
	private String targetKeyword="";
	private String targetIndex="";
	private String targetPatternType="";
	private String targetAlias="";
	private String targetLevelCount="";	
	private String targetFormedStatement = "";
	private String targetPrimaryStatement = "";
	
	private String category="";

	private String sourceSubIndex="";
	private String targetSubIndex="";
	

	public String getPrevKeyword() {
		return prevKeyword;
	}

	public void setPrevKeyword(String prevKeyword) {
		this.prevKeyword = prevKeyword;
	}

	public String getSourceIndex() {
		return sourceIndex;
	}

	public void setSourceIndex(String sourceIndex) {
		this.sourceIndex = sourceIndex;
	}

	public String getSourcePatternType() {
		return sourcePatternType;
	}

	public void setSourcePatternType(String sourcePatternType) {
		this.sourcePatternType = sourcePatternType;
	}

	public String getSourceAlias() {
		return sourceAlias;
	}

	public void setSourceAlias(String sourceAlias) {
		this.sourceAlias = sourceAlias;
	}

	public String getSourceLevelCount() {
		return sourceLevelCount;
	}

	public void setSourceLevelCount(String sourceLevelCount) {
		this.sourceLevelCount = sourceLevelCount;
	}

	public String getTargetIndex() {
		return targetIndex;
	}

	public void setTargetIndex(String targetIndex) {
		this.targetIndex = targetIndex;
	}

	public String getTargetPatternType() {
		return targetPatternType;
	}

	public void setTargetPatternType(String targetPatternType) {
		this.targetPatternType = targetPatternType;
	}

	public String getTargetAlias() {
		return targetAlias;
	}

	public void setTargetAlias(String targetAlias) {
		this.targetAlias = targetAlias;
	}

	public String getTargetLevelCount() {
		return targetLevelCount;
	}

	public void setTargetLevelCount(String targetLevelCount) {
		this.targetLevelCount = targetLevelCount;
	}

	public String getSourceKeyword() {
		return sourceKeyword;
	}

	public void setSourceKeyword(String sourceKeyword) {
		this.sourceKeyword = sourceKeyword;
	}

	public String getTargetKeyword() {
		return targetKeyword;
	}

	public void setTargetKeyword(String targetKeyword) {
		this.targetKeyword = targetKeyword;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCompareKeywordSeq() {
		return compareKeywordSeq;
	}

	public void setCompareKeywordSeq(String compareKeywordSeq) {
		this.compareKeywordSeq = compareKeywordSeq;
	}



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

	/**
	 * @return the sourceSubIndex
	 */
	public String getSourceSubIndex() {
		return sourceSubIndex;
	}

	/**
	 * @param sourceSubIndex the sourceSubIndex to set
	 */
	public void setSourceSubIndex(String sourceSubIndex) {
		this.sourceSubIndex = sourceSubIndex;
	}

	/**
	 * @return the targetSubIndex
	 */
	public String getTargetSubIndex() {
		return targetSubIndex;
	}

	/**
	 * @param targetSubIndex the targetSubIndex to set
	 */
	public void setTargetSubIndex(String targetSubIndex) {
		this.targetSubIndex = targetSubIndex;
	}

	public String getSourcePrimaryStatement() {
		return sourcePrimaryStatement;
	}

	public void setSourcePrimaryStatement(String sourcePrimaryStatement) {
		this.sourcePrimaryStatement = sourcePrimaryStatement;
	}

	public String getTargetPrimaryStatement() {
		return targetPrimaryStatement;
	}

	public void setTargetPrimaryStatement(String targetPrimaryStatement) {
		this.targetPrimaryStatement = targetPrimaryStatement;
	}
	
}
