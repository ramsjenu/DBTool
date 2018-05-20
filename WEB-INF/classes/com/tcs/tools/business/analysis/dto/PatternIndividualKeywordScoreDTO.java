/*
 * PatternIndividualKeywordScore.java
 *
 * Created on October 7, 2011, 5:47 PM
 */

package com.tcs.tools.business.analysis.dto;

/**
 *
 * @author  477780
 */
public class PatternIndividualKeywordScoreDTO {
    
    /** Creates a new instance of PatternIndividualKeywordScore */
    public PatternIndividualKeywordScoreDTO() {
    }
    
    private String keyWord="";
    private String score="";
    private String patternId="";
    private String patternDesc="";
    
    
    /**
	 * @return the patternScore
	 */
	public String getKeyWord() {
		return keyWord;
	}
	/**
	 * @param patternScore the patternScore to set
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
    
    
}
