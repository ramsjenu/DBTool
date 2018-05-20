package com.tcs.tools.web.dto;

import java.io.Serializable;

public class IdentifyPatternDTO implements  Serializable{
	private static final long serialVersionUID = -8767337896773261247L; 
	private String procName ="";
	private String analysisType ="";

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
	 * @return AnalysisType
	 */
	public String getAnalysisType() {
		return analysisType;
	}

	/**
	 * @param analysisType
	 */
	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}
}
