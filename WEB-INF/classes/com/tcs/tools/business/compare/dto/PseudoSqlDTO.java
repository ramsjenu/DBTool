package com.tcs.tools.business.compare.dto;

public class PseudoSqlDTO {
	private int index ;
	private int subIndex = 0 ;
	private String actualString ="";
	private String pseudoSql = "";
	private String matchedYN = "";
	private String prevKeyword ="";
	private String patternType ="";
	private String aliasName="";
	private String matchDesc="";
	private String levelCount="";
	private String primarySql="";
	
	/**
	 * @return the levelCount
	 */
	public String getLevelCount() {
		return levelCount;
	}
	/**
	 * @param levelCount the levelCount to set
	 */
	public void setLevelCount(String levelCount) {
		this.levelCount = levelCount;
	}
	/**
	 * @return the matchDesc
	 */
	public String getMatchDesc() {
		return matchDesc;
	}
	/**
	 * @param matchDesc the matchDesc to set
	 */
	public void setMatchDesc(String matchDesc) {
		this.matchDesc = matchDesc;
	}
	/**
	 * @return the prevKeyword
	 */
	public String getPrevKeyword() {
		return prevKeyword;
	}
	/**
	 * @return the aliasName
	 */
	public String getAliasName() {
		return aliasName;
	}
	/**
	 * @param aliasName the aliasName to set
	 */
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	/**
	 * @param prevKeyword the prevKeyword to set
	 */
	public void setPrevKeyword(String prevKeyword) {
		this.prevKeyword = prevKeyword;
	}
	/**
	 * @return the patternType
	 */
	public String getPatternType() {
		return patternType;
	}
	/**
	 * @param patternType the patternType to set
	 */
	public void setPatternType(String patternType) {
		this.patternType = patternType;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return the actualString
	 */
	public String getActualString() {
		return actualString;
	}
	/**
	 * @param actualString the actualString to set
	 */
	public void setActualString(String actualString) {
		this.actualString = actualString;
	}
	/**
	 * @return the pseudoSql
	 */
	public String getPseudoSql() {
		return pseudoSql;
	}
	/**
	 * @param pseudoSql the pseudoSql to set
	 */
	public void setPseudoSql(String pseudoSql) {
		this.pseudoSql = pseudoSql;
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
	 * @return the subIndex
	 */
	public int getSubIndex() {
		return subIndex;
	}
	/**
	 * @param subIndex the subIndex to set
	 */
	public void setSubIndex(int subIndex) {
		this.subIndex = subIndex;
	}
	/**
	 * @return the primarySql
	 */
	public String getPrimarySql() {
		return primarySql;
	}
	/**
	 * @param primarySql the primarySql to set
	 */
	public void setPrimarySql(String primarySql) {
		this.primarySql = primarySql;
	}
		
	
	

}
