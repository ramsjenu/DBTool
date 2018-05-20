package com.tcs.tools.business.postprocessor;

import java.util.ArrayList;
import java.util.List;

class ComparisonDTO{
	private String actualString="";
	private boolean matchYN=false;
	
	public String getActualString() {
		return actualString;
	}
	public void setActualString(String actualString) {
		this.actualString = actualString;
	}
	public boolean isMatchYN() {
		return matchYN;
	}
	public void setMatchYN(boolean matchYN) {
		this.matchYN = matchYN;
	}
	
}

public class TextComparison {

	public List getStringKeywordsList(String pParam){
		List lStringKeywordsList=new ArrayList();
		String[] lStrArr=pParam.split("\\s+");
		ComparisonDTO lComparisonDTO=null;
		for (int i = 0; i < lStrArr.length; i++) {
			lComparisonDTO=new ComparisonDTO();
			lComparisonDTO.setActualString(lStrArr[i]);
			lComparisonDTO.setMatchYN(false);
			lStringKeywordsList.add(lComparisonDTO);
		}
		
		return lStringKeywordsList;
	}
	
	public static String[] compareTwoStrings(String pSourceString,String pTargetString){
		TextComparison lTextComparison =new TextComparison();
		if("".equals(pSourceString.trim()) || "".equals(pTargetString.trim())){
			return new String[]{pSourceString,pTargetString};
		}
		List lSourceStringKeywordsList= lTextComparison.getStringKeywordsList(pSourceString);
		List lTargetStringKeywordsList= lTextComparison.getStringKeywordsList(pTargetString);
		
		lTextComparison.doCompare(lSourceStringKeywordsList,lTargetStringKeywordsList);
		lTextComparison.doCompare(lTargetStringKeywordsList,lSourceStringKeywordsList);
		
		//System.out.println("<<<<<<<Source List>>>>>>>");
		pSourceString= lTextComparison.getString(lSourceStringKeywordsList);
		//System.out.println("<<<<<<<Target List>>>>>>>");
		pTargetString =lTextComparison.getString(lTargetStringKeywordsList);
		System.out.println("pSourceString::->"+pSourceString);
		System.out.println("pTargetString::->"+pTargetString);
		
		return new String[]{pSourceString,pTargetString};
	}
	public void doCompare(List lSourceStringKeywordsList,List  lTargetStringKeywordsList){		
		ComparisonDTO lSourceComparisonDTO=null;
		ComparisonDTO lTargetComparisonDTO=null;
		/*System.out.println("lSourceStringKeywordsList ::->"+lSourceStringKeywordsList.size());
		System.out.println("lTargetStringKeywordsList ::->"+lTargetStringKeywordsList.size());*/
		for (int i = 0; i < lSourceStringKeywordsList.size(); i++) {
			lSourceComparisonDTO = (ComparisonDTO)lSourceStringKeywordsList.get(i);
			System.out.println(lSourceComparisonDTO.getActualString());
			if(lSourceComparisonDTO.isMatchYN()==false){				
				for (int j = 0; j < lTargetStringKeywordsList.size(); j++) {
					lTargetComparisonDTO = (ComparisonDTO)lTargetStringKeywordsList.get(j);
					if(lSourceComparisonDTO.getActualString().trim().equalsIgnoreCase(lTargetComparisonDTO.getActualString().trim())
							&&  lTargetComparisonDTO.isMatchYN()==false){
						//System.out.println("Matched::- Source:::->"+lSourceComparisonDTO.getActualString()+" :::Target->"+lTargetComparisonDTO.getActualString() );
						lTargetComparisonDTO.setMatchYN(true);
						lSourceComparisonDTO.setMatchYN(true);
						break;
						
					}else if(lTargetComparisonDTO.isMatchYN()==false){
						//System.out.println("Not Matched::- Source ::->"+lSourceComparisonDTO.getActualString()+" :::Target->"+lTargetComparisonDTO.getActualString() );
					}
					lTargetStringKeywordsList.set(j, lTargetComparisonDTO);
				}
				lSourceStringKeywordsList.set(i, lSourceComparisonDTO);
			}
			
		}
		return ;
	}
	public void printList(List lParamList){
		ComparisonDTO lComparisonDTO = new ComparisonDTO();
		for (int i = 0; i < lParamList.size(); i++) {			
			lComparisonDTO = (ComparisonDTO)lParamList.get(i);
			if(lComparisonDTO.isMatchYN()==true){
				continue;
			}
			System.out.println(lComparisonDTO.getActualString()+" -:::- "+lComparisonDTO.isMatchYN());
		}
	}
	public String getString(List lParamList){
		String lRetString="";
		String lKeyString="";
		
		ComparisonDTO lComparisonDTO = new ComparisonDTO();
		for (int i = 0; i < lParamList.size(); i++) {			
			lComparisonDTO = (ComparisonDTO)lParamList.get(i);
			lKeyString=(String)lComparisonDTO.getActualString();
			if(lComparisonDTO.isMatchYN()==false){
				lKeyString="<font style=\"background-color:yellow;color:RED\">"+lKeyString+"</font>";
			}
			lRetString=lRetString+" "+lKeyString;
			
		}
		return lRetString;
	}
	public static void main(String[] args){
	System.out.println("Main Started");
	TextComparison lTextComparison =new TextComparison();
	String pSourceString="Begin  DECLARE SWV_cursor_var CURSOR WITH RETURN FOR select RuleId, VersionNumber, RuleName, RuleDescription, " +
			" CalenderBasis, CalenderStartDate, CalenderEndDate, TimePeriod, TimePeriodBasis,  ApprovedTimeTaken, ApprovedHoursTaken," +
			" TotalTimeRequested, TotalHoursRequested, SharedTime, OtherLeavesTime, CAST(null AS VARCHAR(255)) Comments, " +
			" FutureApprovedTime, FutureApprovedHours, PendingTime, PendingHours, TimeAvailable, ExhaustionDate, DeniedTime, DeniedHours," +
			" SWV_DateAbsent AsOfDate from WorkABSSummary where SessionId = v_SessionId and 1 = 2; ";
	
	String pTargetString="DECLARE SWV_cursor_var CURSOR WITH RETURN TO CLIENT FOR select RuleId, VersionNumber, RuleName, RuleDescription, " +
			" CalenderBasis, CalenderStartDate, CalenderEndDate, TimePeriod, TimePeriodBasis, ApprovedTimeTaken, ApprovedHoursTaken," +
			" FutureApprovedTime, FutureApprovedHours, PendingTime, PendingHours, TimeAvailable, ExhaustionDate, DeniedTime, DeniedHours," +
			" TotalTimeRequested, TotalHoursRequested, SharedTime, OtherLeavesTime, CAST(null AS VARCHAR(255)) Comments, " +
			" SWV_DateAbsent AsOfDate from WorkABSSummary where SessionId = v_SessionId and 1 = 2;  ";
	lTextComparison.compareTwoStrings(pSourceString, pTargetString);	
	System.out.println("Main Ends");
	}
}
