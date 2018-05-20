package com.tcs.tools.business.postprocessor.dto;

public class PostProcessorPattenDTO {

	private String primaryPattern="";
	private String category="";
	private String dataPattern="";
	private String mainConstruct="";
	private String subConstruct="";
	private String directReplace="";
	private String replaceConstruct="";
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDataPattern() {
		return dataPattern;
	}
	public void setDataPattern(String dataPattern) {
		this.dataPattern = dataPattern;
	}
	public String getMainConstruct() {
		return mainConstruct;
	}
	public void setMainConstruct(String mainConstruct) {
		this.mainConstruct = mainConstruct;
	}
	public String getSubConstruct() {
		return subConstruct;
	}
	public void setSubConstruct(String subConstruct) {
		this.subConstruct = subConstruct;
	}
	public String getPrimaryPattern() {
		return primaryPattern;
	}
	public void setPrimaryPattern(String primaryPattern) {
		this.primaryPattern = primaryPattern;
	}
	public String getDirectReplace() {
		return directReplace;
	}
	public void setDirectReplace(String directReplace) {
		this.directReplace = directReplace;
	}
	public String getReplaceConstruct() {
		return replaceConstruct;
	}
	public void setReplaceConstruct(String replaceConstruct) {
		this.replaceConstruct = replaceConstruct;
	}
	
	
	
}
