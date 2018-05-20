package com.tcs.tools.web.dto;

public class StoredProceduresDetailsDTO {
	private String procName = "";
	private String folderPath ="";
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
	 * @return the folderPath
	 */
	public String getFolderPath() {
		return folderPath;
	}
	/**
	 * @param folderPath the folderPath to set
	 */
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	
	
	
}
