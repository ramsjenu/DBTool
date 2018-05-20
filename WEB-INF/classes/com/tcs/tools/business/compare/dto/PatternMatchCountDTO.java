package com.tcs.tools.business.compare.dto;

public class PatternMatchCountDTO {

	private String patternId="";
	private String patternDesc="";
	private String matchCount="";
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
	 * @return the matchCount
	 */
	public String getMatchCount() {
		return matchCount;
	}
	/**
	 * @param matchCount the matchCount to set
	 */
	public void setMatchCount(String matchCount) {
		this.matchCount = matchCount;
	}
}
