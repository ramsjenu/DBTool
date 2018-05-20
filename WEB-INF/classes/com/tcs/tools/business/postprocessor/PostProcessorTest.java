package com.tcs.tools.business.postprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.business.postprocessor.dao.PostProcessorApplyChangesDAO;
import com.tcs.tools.business.postprocessor.dto.PostProcChangePatDTO;
import com.tcs.tools.web.util.ToolsUtil;
import com.tcs.tools.web.util.RGX;

public class PostProcessorTest {
	
	PostProcessorApplyChangesDAO lPostProcessorApplyChangesDAO=new PostProcessorApplyChangesDAO();
	HashMap lVariableDataTypeMap=new HashMap(); 
	public static  void test(){
		
		
		String lQueryStr="SWL_Label: WHILE 1 = 1 DO " +
				"\n IF NOT EXISTS(SELECT 1 FROM ISGroupRefs ES1, SESSION.tt_ISQuestionsTemp ES2 " +
				"\n WHERE ES1.GroupId = ES2.Refid AND coalesce(ES2.ProcessedInd,'') != 'Y') then " +
				"\n  leave SWL_Label;";
		
		System.out.println("Before:::->"+lQueryStr);
		//System.out.println(RGX.REP_SPACES_SRN(lQueryStr));
		//(?i)WHERE[\\s\\r\\n]*\\([\\s\\r\\n]*[\\w\\W]+"+RGX.REP_SPACES_SRN("is NOT NULL AND")+"[\\w\\W]+[\\s\\r\\n]*\\=[\\s\\r\\n]*[\\w\\W]+[\\s\\r\\n]*\\)
		String lRegex="(?i)\\bSWL" ;				
		
		
		Pattern lChkPat=Pattern.compile(lRegex);
		
		
		Matcher matcher=lChkPat.matcher(lQueryStr);
		System.out.println("lChkPat:::->"+lChkPat);
		
		while(matcher.find()){
			System.out.println("---Found----->"+matcher.group(0));
		
			/*Matcher matcher1=lChkPat1.matcher(matcher.group());			
			if(matcher1.find()){
				System.out.println("---Found----->"+matcher1.group(0));
			}*/
			
		}
		
		
		/*String pFindPattern ="(?i)\\bCAST\\b[\\s\\r\\n]*\\([\\s\\r\\n]*\\bCHAR\\b\\((.+?)\\)[\\s\\r\\n]*\\bAS\\b[\\s\\r\\n]+\\bVARCHAR\\b[\\s\\r\\n]*\\([\\d]+[\\s\\r\\n]*\\)[\\s\\r\\n]*\\)";
		String pReplacementPattern = pFindPattern;
		String pReplacementText="FN_DECIMALTOCHAR( _DBT_VAR_RPL_ )";
		int pGroupCount = -5;
		String pdataTypeToChk="DECIMAL";
		boolean pReplaceComplete=true;
		boolean pchkTextType=true;
		
		PostProcChangePatDTO lPostProcChangePatDTO =new PostProcChangePatDTO();
		lPostProcChangePatDTO.setFindPattern(pFindPattern);
		lPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		lPostProcChangePatDTO.setReplacementText(pReplacementText);
		lPostProcChangePatDTO.setGroupCount(pGroupCount);
		lPostProcChangePatDTO.setDataTypeToChk(pdataTypeToChk);		
		lPostProcChangePatDTO.setReplaceComplete(pReplaceComplete);
		lPostProcChangePatDTO.setChkTextType(pchkTextType);
		lQueryStr=new PostProcessorTest().checkAndReplace(lQueryStr,lPostProcChangePatDTO);
		
		System.out.println("After::->"+lQueryStr);*/
		
	}
	
	public static void main(String [] args){
		System.out.println("--Main Starts---");
		test();
		//System.out.println(new PostProcessorTest().getDataTypeofText("012.00,aaksja"));
		/*String pBlockStr= "CALL Dcms_IS_IntakeQuestionBranch(V_InstanceId,V_CalledFrom,SWV_BranchToRefid,V_MsgLevel,V_ParentGroup,V_GroupId,V_RefIdVisible,V_Debug,'');";
		System.out.println(pBlockStr);
		handleResursiveCallStatement(pBlockStr,"Dcms_IS_IntakeQuestionBranch" );*/
		String lQueryStr="Create or replace Procedure v_TAMGetABSSummary2(v_CalledFrom VARCHAR(15) DEFAULT null," +
				"v_SessionId                    VARCHAR(50) DEFAULT null," +
				"v_ControlNumber                INTEGER," +
				"v_ClaimantSocialSecurityNumber VARCHAR(9) DEFAULT null," +
				"v_ClientEmployeeId             VARCHAR(10) DEFAULT null," +
				"v_ClaimId                      INTEGER DEFAULT null," +
				"v_CoverageSeqNum               INTEGER DEFAULT null," +
				"v_Gender                       CHAR(1) DEFAULT null," +
				"v_WorkLocationState            CHAR(2) DEFAULT null," +
				"v_WorkLocationCode             VARCHAR(50) DEFAULT null," +
				"v_ERType                       CHAR(5) DEFAULT null," +
				"v_EEType                       CHAR(5) DEFAULT null," +
				"v_DateAbsent                   TIMESTAMP DEFAULT null )";
		
		//getSPInputVariables(lQueryStr);
		System.out.println("--Main Ends---");
		
	}
	
	public static void getSPInputVariables(List pBlockStrList){
	String pBlockStr="";
	Pattern lCreateProcPattern=Pattern.compile("(?i)(\\bCREATE\\b|\\bCREATE\\b\\s+\\bOR\\b\\s+\\bREPLACE\\b)\\s+(\\bPROCEDURE\\b|\\bPROC\\b)+[\\s\\r\\n]+[\\S]+[\\s\\r\\n]*\\(");
		try {
			for (int i = 0; i < pBlockStrList.size(); i++) {
				pBlockStr=(String)pBlockStrList.get(i);
				Matcher lChkPatMatcher=lCreateProcPattern.matcher(pBlockStr.trim());
				if(lChkPatMatcher.find()){
					pBlockStr=pBlockStr.substring(lChkPatMatcher.end(),pBlockStr.length());
					pBlockStr=pBlockStr.replaceAll("\\,", " , ");
					pBlockStr=pBlockStr.replaceAll("\\(", " ( ");
					String[] lSourceArray=pBlockStr.split("\\s+");
					for (int j = 0; j < lSourceArray.length; j++) {
						if(lSourceArray[j].startsWith("v_")){
							//lVariableDataTypeMap.put(lSourceArray[j].trim().toUpperCase(),lSourceArray[j+1].trim());
							//System.out.println("--"+lSourceArray[j].trim()+"---"+lSourceArray[j+1].trim());															
						}		
					}
					
				}
				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
			return;
		}
		
	}
	
	
	public static String handleResursiveCallStatement(String pBlockStr,String pProcName ){
		Pattern lChkPattern=Pattern.compile("(?i)\\bCALL\\b[\\s\\r\\n]+\\b"+pProcName.trim()+"\\b[\\s\\r\\n]*\\((.+?)\\)[\\s\\r\\n]*;");
		Matcher lChkPatMatcher=lChkPattern.matcher(pBlockStr);
		String callParamStr="";
		String param="";
		int lopenBraceCount=0;
		List lParamList=new ArrayList();
		if(lChkPatMatcher.find()){
			callParamStr=lChkPatMatcher.group(1);			
			callParamStr=callParamStr.replaceAll("\\,"," , ");			
			String[] lStmtArr=callParamStr.split("\\s");
			
			for (int i = 0; i < lStmtArr.length; i++) {
				if(lStmtArr[i].trim().equals("(")){
					lopenBraceCount++;					
				}
				if(lStmtArr[i].trim().equals(")")){
					lopenBraceCount--;
				}
				if(lopenBraceCount==0 && lStmtArr[i].trim().equals(",") ){
					lParamList.add(param);
					param="";
					continue;
				}
				param=param+lStmtArr[i];
			}
			if(!param.equals("")){
				lParamList.add(param);
			}
		}
		String pVar="V_Sql";
		String lResultStr=" SET "+pVar+" = 'Call Dcms_IS_IntakeQuestionBranch(' ;\n";
		for (int i = 0; i < lParamList.size(); i++) {
			if(i==lParamList.size()-1){
				lResultStr= lResultStr+chkCallParameter("V_Sql",(String)lParamList.get(i),true);
			}else{
				lResultStr= lResultStr+chkCallParameter("V_Sql",(String)lParamList.get(i),false);
			}
			
		}
		lResultStr=lResultStr+" EXECUTE IMMEDIATE "+pVar+" ;\n";				
		System.out.println("lParamList::->"+lResultStr);
				
	return "";			
	}
	public static String  chkCallParameter(String pVar,String pParam,boolean isLastParam){
		//"+pVar+"
		String lVarChkStr="";
		if(isLastParam==true){
			lVarChkStr=" IF "+pParam+" IS NULL THEN \n" +
					" SET "+pVar+" = "+pVar+" || '"+pParam+"=>' || rtrim(coalesce("+pParam+",'null')) || ')';\n" +
					" ELSE \n" +
					" SET "+pVar+" = "+pVar+" || '"+pParam+"=>' ||  '''' || rtrim("+pParam+") || '''' || ')';\n" +
					" END IF;\n";
		}else{
			lVarChkStr=" IF "+pParam+" IS NULL THEN \n" +
					" SET "+pVar+" = "+pVar+" || '"+pParam+"=>' || rtrim(coalesce("+pParam+",'null')) || ',';\n" +
					" ELSE \n" +
					" SET "+pVar+" = "+pVar+" || '"+pParam+"=>' ||  '''' || rtrim("+pParam+") || '''' || ',';\n" +
					" END IF;\n";
		}
			
		
		return lVarChkStr;
	}
	
	
	public String checkAndReplace(String pBlockToCheck,PostProcChangePatDTO pPostProcChangePatDTO){
		
	lVariableDataTypeMap.put("V_CONTROLDATE","DECIMAL");
	String pFindPattern=pPostProcChangePatDTO.getFindPattern();
	String pReplacementPattern=pPostProcChangePatDTO.getReplacementPattern();
	String pReplacementText=pPostProcChangePatDTO.getReplacementText();
	int pGroupCount=pPostProcChangePatDTO.getGroupCount();
	String pdataTypeToChk=pPostProcChangePatDTO.getDataTypeToChk();
	boolean pReplaceComplete=pPostProcChangePatDTO.isReplaceComplete();
	
	boolean lNotDatatype=false;
	boolean lisStatemetnsNeedtoChange=false;
	//Checking for not Datatype.	
	if(pdataTypeToChk.trim().startsWith("!")){
		pdataTypeToChk=pdataTypeToChk.replaceFirst("!","").trim();
		lNotDatatype=true;
	}
	
	String lTempReplacement=pReplacementText;
	pFindPattern ="(?i)"+pFindPattern;
	 Pattern lStrFunctionPattern = Pattern.compile(pFindPattern);
	 Matcher lMatcher=null;
	 lMatcher=lStrFunctionPattern.matcher(pBlockToCheck);
	 StringBuffer sb= new StringBuffer();
	 //System.out.println(pFindPattern);
	 while(lMatcher.find()){
		 
		 //System.out.println(":::::::before:::"+pBlockToCheck);
		 //"(?i)if[\\r\\n\\s]*\\((.+?)\\![\\r\\n\\s]*="
			if(pGroupCount > 0 ){
    			for (int i = 1; i <= pGroupCount; i++) {
    				pReplacementText = pReplacementText.replaceAll(ToolConstant.TOOL_DELIMT_GROUP_SPLIT+i,lMatcher.group(1).trim());
    				pBlockToCheck=pBlockToCheck.substring(0,lMatcher.start())+pReplacementText+pBlockToCheck.substring(lMatcher.end(),pBlockToCheck.length());
				}
			}else if(pGroupCount == 0 ){
				
				pBlockToCheck=pBlockToCheck.substring(0,lMatcher.start())+pReplacementText+pBlockToCheck.substring(lMatcher.end(),pBlockToCheck.length());
			}else if(pGroupCount == -1 ){
				//System.out.println("Group 0:::"+lMatcher.group(0).trim());
				pReplacementText =lMatcher.group(0).trim()+ pReplacementText;
			pBlockToCheck=pBlockToCheck.substring(0,lMatcher.start())+pReplacementText+pBlockToCheck.substring(lMatcher.end(),pBlockToCheck.length()); 				
			}else if(pGroupCount == -2 ){
				//Add Text at the End of the Block;
				//System.out.println("Group 0:::"+lMatcher.group(0).trim());
				if(pBlockToCheck.trim().endsWith(";")){
					pBlockToCheck=pBlockToCheck.substring(0,pBlockToCheck.lastIndexOf(";"));
					pBlockToCheck=pBlockToCheck+pReplacementText+" ; ";
				}
				
			}else if(pGroupCount == -3 ){
				//add Replacement Text after the end of the replacement regex and replace after group 0 position.
				Matcher lReplaceMatcher=Pattern.compile(pReplacementPattern).matcher(pBlockToCheck);
				if(lReplaceMatcher.find()){
					pBlockToCheck=pBlockToCheck.substring(0,lReplaceMatcher.end())+pReplacementText+pBlockToCheck.substring(lReplaceMatcher.end(),pBlockToCheck.length());
					
				}
			}else if(pGroupCount == -4 ){
				String lFindStr=lMatcher.group(0);
				//Take some text from regex and add Replacement Text to that n replace it at the group 1  position.
				//Matcher lReplaceMatcher=Pattern.compile(pReplacementPattern).matcher(pBlockToCheck);
				Matcher lReplaceMatcher=Pattern.compile(pReplacementPattern).matcher(lFindStr);
			String lTextFromRgx="";
				if(lReplaceMatcher.find()){
					lTextFromRgx=lReplaceMatcher.group(1);
					//pReplacementText=lTextFromRgx+pReplacementText;
					if(pdataTypeToChk!=null && !"".equals(pdataTypeToChk)){
						if(lVariableDataTypeMap.containsKey(lTextFromRgx.trim().toUpperCase()) ){
							if(lNotDatatype==true){
								if( ! pdataTypeToChk.trim().equalsIgnoreCase(((String)lVariableDataTypeMap.get(lTextFromRgx.trim().toUpperCase())).trim())){
									lisStatemetnsNeedtoChange=true;
	 							}
							}else if(  pdataTypeToChk.trim().equalsIgnoreCase(((String)lVariableDataTypeMap.get(lTextFromRgx.trim().toUpperCase())).trim())){
								lisStatemetnsNeedtoChange=true; 								
							}
							if(lisStatemetnsNeedtoChange==true){
								pReplacementText=pReplacementText.replaceAll("\\s*_DBT_VAR_RPL_\\s*",lTextFromRgx); 								
								lFindStr=lFindStr.substring(0,lReplaceMatcher.start(1))+pReplacementText+lFindStr.substring(lReplaceMatcher.start(1),lFindStr.length());
								pBlockToCheck=pBlockToCheck.substring(0,lMatcher.start(0))+lFindStr+pBlockToCheck.substring(lMatcher.end(0),pBlockToCheck.length());
			 					//pBlockToCheck=pBlockToCheck.substring(0,lReplaceMatcher.start(1))+pReplacementText+pBlockToCheck.substring(lReplaceMatcher.start(1),pBlockToCheck.length());
							}
						}else{
							System.out.println("Variable Not Found in Declare Var HashMap::pdataTypeToChk->"+pdataTypeToChk+":::-:::"+lTextFromRgx.trim());
						}
						
					}else{
						pReplacementText=pReplacementText.replaceAll("\\s*_DBT_VAR_RPL_\\s*",lTextFromRgx);
						lFindStr=lFindStr.substring(0,lReplaceMatcher.start(1))+pReplacementText+lFindStr.substring(lReplaceMatcher.start(1),lFindStr.length());
						pBlockToCheck=pBlockToCheck.substring(0,lMatcher.start(0))+lFindStr+pBlockToCheck.substring(lMatcher.end(0),pBlockToCheck.length());
	 					//pBlockToCheck=pBlockToCheck.substring(0,lReplaceMatcher.start(1))+pReplacementText+pBlockToCheck.substring(lReplaceMatcher.start(1),pBlockToCheck.length());
					}
					 					
				}
			}else if(pGroupCount == -5 ){
				String lFindStr=lMatcher.group(0);
				int lReplaceGroupCount=0;
				//System.out.println("pGroupCount::"+pGroupCount);
				//Take some text from regex and Replace the Replacement Text with that at the group 1  position.
				//Matcher lReplaceMatcher=Pattern.compile(pReplacementPattern).matcher(pBlockToCheck);
				Matcher lReplaceMatcher=Pattern.compile(pReplacementPattern).matcher(lFindStr);
				//System.out.println("pReplacementPattern:::->"+pReplacementPattern);
				String lTextFromRgx="";
				if(lReplaceMatcher.find()){
					if(lReplaceMatcher.groupCount()>0){
						lReplaceGroupCount=lReplaceMatcher.groupCount();
					}
					
					lTextFromRgx=lReplaceMatcher.group(lReplaceGroupCount);
					//System.out.println("lTextFromRgx:;->"+lTextFromRgx);
					//pReplacementText=lTextFromRgx+pReplacementText;
					if(pdataTypeToChk!=null && !"".equals(pdataTypeToChk)){
						if(lVariableDataTypeMap.containsKey(lTextFromRgx.trim().toUpperCase()) || pdataTypeToChk.trim().equalsIgnoreCase(getDataTypeofText(lTextFromRgx.trim())) ){
							System.out.println("lNotDatatype::"+lNotDatatype);
							if(lNotDatatype==true){
								System.out.println("::Datatype from Map::"+((String)lVariableDataTypeMap.get(lTextFromRgx.trim().toUpperCase())).trim());
								System.out.println("::pdataTypeToChk to check:::"+pdataTypeToChk);
								if( ! pdataTypeToChk.trim().equalsIgnoreCase(((String)lVariableDataTypeMap.get(lTextFromRgx.trim().toUpperCase())).trim())){
									lisStatemetnsNeedtoChange=true;
	 							}
							}else if(  pdataTypeToChk.trim().equalsIgnoreCase(ToolsUtil.replaceNull(((String)lVariableDataTypeMap.get(lTextFromRgx.trim().toUpperCase()))).trim())
									|| pdataTypeToChk.trim().equalsIgnoreCase(getDataTypeofText(lTextFromRgx.trim()))){
								lisStatemetnsNeedtoChange=true; 								
							}
							System.out.println("lisStatemetnsNeedtoChange::->"+lisStatemetnsNeedtoChange );
							if(lisStatemetnsNeedtoChange==true){
								
									if(pReplaceComplete){
										lReplaceGroupCount=0; // here making group count so v can replace complete text
									}
									pReplacementText=pReplacementText.replaceAll("\\s*_DBT_VAR_RPL_\\s*",lTextFromRgx);
									lFindStr=lFindStr.substring(0,lReplaceMatcher.start(lReplaceGroupCount))+pReplacementText+lFindStr.substring(lReplaceMatcher.end(lReplaceGroupCount),lFindStr.length());
									//System.out.println("lFindStr::::->"+lFindStr);
				 					//pBlockToCheck=pBlockToCheck.substring(0,lReplaceMatcher.start(1))+pReplacementText+pBlockToCheck.substring(lReplaceMatcher.end(1)-1,pBlockToCheck.length());
									pBlockToCheck=pBlockToCheck.substring(0,lMatcher.start(0))+lFindStr+pBlockToCheck.substring(lMatcher.end(0),pBlockToCheck.length());								
								
							}
						}else{
							System.out.println("Variable Not Found in Declare Var HashMap::pdataTypeToChk->"+pdataTypeToChk+":::-:::"+lTextFromRgx.trim());
						}
						
					}else{
						if(pReplaceComplete){
							lReplaceGroupCount=0; // here making group count so v can replace complete text
						}
						pReplacementText=pReplacementText.replaceAll("\\s*_DBT_VAR_RPL_\\s*",lTextFromRgx);
						lFindStr=lFindStr.substring(0,lReplaceMatcher.start(lReplaceGroupCount))+pReplacementText+lFindStr.substring(lReplaceMatcher.end(lReplaceGroupCount),lFindStr.length());
		 				//pBlockToCheck=pBlockToCheck.substring(0,lReplaceMatcher.start(1))+pReplacementText+pBlockToCheck.substring(lReplaceMatcher.end(1)-1,pBlockToCheck.length());
						pBlockToCheck=pBlockToCheck.substring(0,lMatcher.start(0))+lFindStr+pBlockToCheck.substring(lMatcher.end(0),pBlockToCheck.length());
						
						
					}
					 					
				}
			}
			//System.out.println(":::::::after:::"+pBlockToCheck);
		pReplacementText=lTempReplacement;
    	//lMatcher=lStrFunctionPattern.matcher(pBlockToCheck);
	 }
	
	
  return pBlockToCheck;

}
	public String getDataTypeofText(String pParam){
		String DataType ="";
		Pattern lDecimalPattern=Pattern.compile("(^[\\-\\+]*[0-9]+\\d*\\.\\d*$)");
		//(^\\d*\\.?\\d*[1-9]+\\d*$)|(^[1-9]+\\d*\\.\\d*$)
		if(lDecimalPattern.matcher(pParam).find()){
			return "DECIMAL";
		}
		return "";
	}
	public static void addCoalesceForPipe(String lQueryStr){
		Pattern lPipePattern = Pattern.compile("(?i)\\s+(.+?)\\s*\\|\\|\\s*(.+?)\\s+");
		Matcher lPipeMatcher=lPipePattern.matcher(lQueryStr);
		while(lPipeMatcher.find()){
			System.out.println(lPipeMatcher.group());
			System.out.println(lPipeMatcher.group(1));
			System.out.println(lPipeMatcher.group(2));
		}
		
	}
}
