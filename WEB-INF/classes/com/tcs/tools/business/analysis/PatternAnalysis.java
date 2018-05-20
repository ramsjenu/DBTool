package com.tcs.tools.business.analysis;

import com.tcs.tools.business.analysis.dao.PatternAnalysisDAO;
import com.tcs.tools.business.analysis.dao.PatternAnalysisSingleDAO;

public class PatternAnalysis {

	PatternAnalysisDAO lPatternAnalysisDAO = new PatternAnalysisDAO();
	PatternAnalysisSingleDAO lPatternAnalysisSingleDAO = new PatternAnalysisSingleDAO();
	
	
	public void getPatternData(String pRunId,String pDbMigrationType,String pFolderPath){
		//for full set
		//List lConcatenatedList = lPatternAnalysisDAO.getParsedData(pRunId);
		//for file by file
		lPatternAnalysisSingleDAO.getParsedDataWhole(pRunId,pDbMigrationType,pFolderPath); 
		/*if(lConcatenatedList != null){
			for (int i = 0; i < lConcatenatedList.size(); i++) {
				System.out.println("::::from list::::"+(String)lConcatenatedList.get(i));
			}
		}*/
	}
}
