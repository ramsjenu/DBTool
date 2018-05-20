package com.tcs.tools.web.util;

public class RGX {
	public static final String SRN="[\\s\\r\\n]";
	public static final String TEXT="[\\w\\W\\s\\r\\n]";
	public static final String ONE_OR_MANY="+";
	public static final String ZERO_OR_MANY="*";
	public static final String B_STR="\\b";
	public static final String OR="|";
	
	public static String B_STR(String pParam){
		if(pParam != null){
			pParam = B_STR+pParam+B_STR;
		}
		return pParam;
	}
	
	public static String L_STR(String pParam){
		if(pParam != null){
			pParam = B_STR+pParam;
		}
		return pParam;
	}
	
	public static String R_STR(String pParam){
		if(pParam != null){
			pParam = pParam+B_STR;
		}
		return pParam;
	}
	
	public static String REP_SPACES_SRN(String pParam){
		String[] pParamArr = pParam.split("\\s+");
		pParam = "";
		for (int i = 0; i < pParamArr.length; i++) {
			pParam +=B_STR(pParamArr[i])+SRN+ONE_OR_MANY ;
			
		}
		
		return pParam;
	}
	public static void main(String[] args){
		System.out.println("::::inside main:::"+REP_SPACES_SRN("/* asdasd asdasd"));
	}
}
