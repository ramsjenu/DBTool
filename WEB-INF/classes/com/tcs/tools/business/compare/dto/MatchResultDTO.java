package com.tcs.tools.business.compare.dto;

public class MatchResultDTO {
private PseudoSqlDTO sourcePseudoSqlDTO;
private PseudoSqlDTO targetPseudoSqlDTO;
private String matchedYN="";
private String matchDesc="";
/**
 * @return the sourcePseudoSqlDTO
 */
public PseudoSqlDTO getSourcePseudoSqlDTO() {
	return sourcePseudoSqlDTO;
}
/**
 * @param sourcePseudoSqlDTO the sourcePseudoSqlDTO to set
 */
public void setSourcePseudoSqlDTO(PseudoSqlDTO sourcePseudoSqlDTO) {
	this.sourcePseudoSqlDTO = sourcePseudoSqlDTO;
}
/**
 * @return the targetPseudoSqlDTO
 */
public PseudoSqlDTO getTargetPseudoSqlDTO() {
	return targetPseudoSqlDTO;
}
/**
 * @param targetPseudoSqlDTO the targetPseudoSqlDTO to set
 */
public void setTargetPseudoSqlDTO(PseudoSqlDTO targetPseudoSqlDTO) {
	this.targetPseudoSqlDTO = targetPseudoSqlDTO;
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


}
