package com.tcs.tools.web.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SPComplexityAnalysysProcDataDTO {

	private String procName ="";
	private String linesOfCode="";
	private String distinctPatterns ="";
	private String statementPatterns = "";
	private String globalVariable ="";
	private String dataTypes="";
	private String functions ="";
	private String printStatement ="";
	private String nestedStoredProcs ="";
	private String tempTables ="";
	private String dynamicSqls="";
	
	private String category ="";
	private String score ="";
	private List spScoresList = new ArrayList();
	
	/**
	 * @return the spScoresMap
	 */
	public HashMap getSpScoresMap() {
		return spScoresMap;
	}
	/**
	 * @param spScoresMap the spScoresMap to set
	 */
	public void setSpScoresMap(HashMap spScoresMap) {
		this.spScoresMap = spScoresMap;
	}
	private HashMap spScoresMap = new HashMap();
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @return the spScoresList
	 */
	public List getSpScoresList() {
		return spScoresList;
	}
	/**
	 * @param spScoresList the spScoresList to set
	 */
	public void setSpScoresList(List spScoresList) {
		this.spScoresList = spScoresList;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
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
	 * @return the tempTables
	 */
	public String getTempTables() {
		return tempTables;
	}
	/**
	 * @param tempTables the tempTables to set
	 */
	public void setTempTables(String tempTables) {
		this.tempTables = tempTables;
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
	 * @return the linesOfCode
	 */
	public String getLinesOfCode() {
		return linesOfCode;
	}
	/**
	 * @param linesOfCode the linesOfCode to set
	 */
	public void setLinesOfCode(String linesOfCode) {
		this.linesOfCode = linesOfCode;
	}
	/**
	 * @return the distinctPatterns
	 */
	public String getDistinctPatterns() {
		return distinctPatterns;
	}
	/**
	 * @param distinctPatterns the distinctPatterns to set
	 */
	public void setDistinctPatterns(String distinctPatterns) {
		this.distinctPatterns = distinctPatterns;
	}
	/**
	 * @return the statementPatterns
	 */
	public String getStatementPatterns() {
		return statementPatterns;
	}
	/**
	 * @param statementPatterns the statementPatterns to set
	 */
	public void setStatementPatterns(String statementPatterns) {
		this.statementPatterns = statementPatterns;
	}
	/**
	 * @return the globalVariable
	 */
	public String getGlobalVariable() {
		return globalVariable;
	}
	/**
	 * @param globalVariable the globalVariable to set
	 */
	public void setGlobalVariable(String globalVariable) {
		this.globalVariable = globalVariable;
	}
	/**
	 * @return the dataTypes
	 */
	public String getDataTypes() {
		return dataTypes;
	}
	/**
	 * @param dataTypes the dataTypes to set
	 */
	public void setDataTypes(String dataTypes) {
		this.dataTypes = dataTypes;
	}
	/**
	 * @return the functions
	 */
	public String getFunctions() {
		return functions;
	}
	/**
	 * @param functions the functions to set
	 */
	public void setFunctions(String functions) {
		this.functions = functions;
	}
	/**
	 * @return the printStatement
	 */
	public String getPrintStatement() {
		return printStatement;
	}
	/**
	 * @param printStatement the printStatement to set
	 */
	public void setPrintStatement(String printStatement) {
		this.printStatement = printStatement;
	}
	/**
	 * @return the nestedStoredProcs
	 */
	public String getNestedStoredProcs() {
		return nestedStoredProcs;
	}
	/**
	 * @param nestedStoredProcs the nestedStoredProcs to set
	 */
	public void setNestedStoredProcs(String nestedStoredProcs) {
		this.nestedStoredProcs = nestedStoredProcs;
	}
	/**
	 * @return the dynamicSqls
	 */
	public String getDynamicSqls() {
		return dynamicSqls;
	}
	/**
	 * @param dynamicSqls the dynamicSqls to set
	 */
	public void setDynamicSqls(String dynamicSqls) {
		this.dynamicSqls = dynamicSqls;
	}
	
	
	
	
}
