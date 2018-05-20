package com.tcs.tools.web.dto;

public class SearchSPDTO {

	private String procedureName;
	private String statement;
	private String statementNo;
	private String searchedWord;
	public String getProcedureName() {
		return procedureName;
	}
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	public String getStatement() {
		return statement;
	}
	public void setStatement(String statement) {
		this.statement = statement;
	}
	public String getStatementNo() {
		return statementNo;
	}
	public void setStatementNo(String statementNo) {
		this.statementNo = statementNo;
	}
	public String getSearchedWord() {
		return searchedWord;
	}
	public void setSearchedWord(String searchedWord) {
		this.searchedWord = searchedWord;
	}
	
}
