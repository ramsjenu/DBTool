package com.tcs.tools.business.postprocessor.dto;

public class PostProcChangePatDTO {
	String findPattern="";
	String replacementPattern="";
	String replacementText="";
	String findDesc ="";
	int groupCount=0;
	String dataTypeToChk="";
	boolean replaceComplete=false;
	boolean chkTextType=false;
	
	public String getFindPattern() {
		return findPattern;
	}
	public void setFindPattern(String findPattern) {
		this.findPattern = findPattern;
	}
	public String getReplacementPattern() {
		return replacementPattern;
	}
	public void setReplacementPattern(String replacementPattern) {
		this.replacementPattern = replacementPattern;
	}
	public String getReplacementText() {
		return replacementText;
	}
	public void setReplacementText(String replacementText) {
		this.replacementText = replacementText;
	}
	public int getGroupCount() {
		return groupCount;
	}
	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}
	public String getFindDesc() {
		return findDesc;
	}
	public void setFindDesc(String findDesc) {
		this.findDesc = findDesc;
	}
	public String getDataTypeToChk() {
		return dataTypeToChk;
	}
	public void setDataTypeToChk(String dataTypeToChk) {
		this.dataTypeToChk = dataTypeToChk;
	}
	public boolean isReplaceComplete() {
		return replaceComplete;
	}
	public void setReplaceComplete(boolean replaceComplete) {
		this.replaceComplete = replaceComplete;
	}
	public boolean isChkTextType() {
		return chkTextType;
	}
	public void setChkTextType(boolean chkTextType) {
		this.chkTextType = chkTextType;
	}
	
	
	

}
