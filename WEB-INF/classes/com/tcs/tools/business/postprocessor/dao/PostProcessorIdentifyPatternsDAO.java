package com.tcs.tools.business.postprocessor.dao;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcs.tools.business.postprocessor.PostProcessorIdentifyPatterns;
import com.tcs.tools.business.postprocessor.dto.PostProcChangePatDTO;
import com.tcs.tools.web.util.RGX;



public class PostProcessorIdentifyPatternsDAO {
	public List getMasterPatternList(){
		List lMasterPatternList = new ArrayList();
		
	/*	String RGX.SRN="[\\s\\r\\n]";
		String RGX.TEXT="[\\w\\W\\s\\r\\n]";
		String RGX.ONE_OR_MANY="+";
		String RGX.ZERO_OR_MANY="*";*/
		
		PostProcChangePatDTO pPostProcChangePatDTO=null;
		String pFindPattern="";
		String pFindDesc="";
		
		//--------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//source - IF CHAR(coalesce(V_localUpdatedDate,'1900-01-01 00:00:00.000')) = ''
		//target - IF coalesce(V_localUpdatedDate,'1900-01-01 00:00:00.000')  =  '1900-01-01 00:00:00.000'
		//lMasterPatternList.add("\\bIF\\b[\\s\\r\\n]+CHAR[\\s\\r\\n]*\\([\\s\\r\\n]*coalesce[\\s\\r\\n]*\\((.+?)\\,\\s*\\'1900\\-01\\-01[\\s\\r\\n]+00:00:00\\.000\\'\\)[\\s\\r\\n]*\\)[\\s\\r\\n]*=[\\s\\r\\n]*\\'\\'"); //17
		pFindPattern ="\\bIF\\b[\\s\\r\\n]+CHAR[\\s\\r\\n]*\\([\\s\\r\\n]*coalesce[\\s\\r\\n]*\\((.+?)\\,\\s*\\'1900\\-01\\-01[\\s\\r\\n]+00:00:00\\.000\\'\\)[\\s\\r\\n]*\\)[\\s\\r\\n]*=[\\s\\r\\n]*\\'\\'";
		pFindDesc ="If Char Coalesce with timestamp condition";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		
		//--------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		pFindPattern ="\\bSWF_Str\\b[\\s\\r\\n]*\\(";
		pFindDesc ="str() function usage";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		//lMasterPatternList.add("\\bSWF_Str\\b[\\s\\r\\n]*\\("); //31
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		//--------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add("\\bSUBSTR\\b[\\s\\r\\n]*\\(");//43
		pFindPattern ="\\bSUBSTR\\b[\\s\\r\\n]*\\(";
		pFindDesc ="substring() function usage";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		//--------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add("\\bv_transtate\\b\\s*");//5 - @@transtate
		pFindPattern ="\\bv_transtate\\b\\s*";
		pFindDesc="@@transtate global variable usage";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		//--------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add("@@servername\\b");//18-@@servername
		pFindPattern ="@@servername\\b";
		pFindDesc ="@@servername  global variable usage";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		
		//--------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add("\\bCALL\\b[\\s\\r\\n]+\\bDBMS_OUTPUT\\.PUT_LINE\\b[\\s\\r\\n]*\\(");//24-CALL DBMS_OUTPUT.PUT_LINE( for print
		pFindPattern ="\\bCALL\\b[\\s\\r\\n]+\\bDBMS_OUTPUT\\.PUT_LINE\\b[\\s\\r\\n]*\\(";
		pFindDesc ="print statement ";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		
		//--------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add("\\bisnumeric[\\s\\r\\n]*\\([\\s\\r\\n]*"); //isnumeric( - 32
		pFindPattern ="\\bisnumeric[\\s\\r\\n]*\\([\\s\\r\\n]*";
		pFindDesc ="isnumeric() function usage ";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		//--------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add("\\bSWV_Fetch_Status\\b"); //SWV_Fetch_Status - 33
		pFindPattern ="\\bSWV_Fetch_Status\\b";
		pFindDesc ="Fetch_Status";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		//--------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add("\\bDECLARE\\b"+RGX.SRN+RGX.ONE_OR_MANY+"\\bSWV_cursor"+RGX.TEXT+RGX.ZERO_OR_MANY+"\\bCURSOR\\b"+RGX.SRN+RGX.ONE_OR_MANY+"((?!\\bWITH\\b"+RGX.SRN+RGX.ONE_OR_MANY+"\\bRETURN\\b"+RGX.SRN+RGX.ONE_OR_MANY+"\\bTO\\b"+RGX.SRN+RGX.ONE_OR_MANY+"\\bCLIENT\\b).)+"); 
		// DECLARE SWV_cursor_var CURSOR WITH RETURN TO CLIENT - 34
		pFindDesc ="DECLARE CURSOR - WITH RETURN TO CLIENT - Not Found";
		pFindPattern ="\\bDECLARE\\b"+RGX.SRN+RGX.ONE_OR_MANY+"\\bSWV_cursor"+RGX.TEXT+RGX.ZERO_OR_MANY+"\\bCURSOR\\b"+RGX.SRN+RGX.ONE_OR_MANY+"((?!\\bWITH\\b"+RGX.SRN+RGX.ONE_OR_MANY+"\\bRETURN\\b"+RGX.SRN+RGX.ONE_OR_MANY+"\\bTO\\b"+RGX.SRN+RGX.ONE_OR_MANY+"\\bCLIENT\\b).)+";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		//--------------------

		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add(lComment); //40
		pFindPattern ="(?i)\\/[\\s\\r\\n]*\\*[\\s\\r\\n]*\"[\\s\\r\\n]*\\bFETCH\\b[\\s\\r\\n]+"+RGX.REP_SPACES_SRN("FIRST 1 ROWS")+"\\bONLY\\b[\\s\\r\\n]*\"[\\s\\r\\n]*" +
				RGX.REP_SPACES_SRN("has been added to UPDATE")+"\\bstatement\\b[\\s\\r\\n]*,[\\s\\r\\n]*" +
				RGX.REP_SPACES_SRN("although this may not work properly for")+"\\bDB2\\b[\\s\\r\\n]*\\.[\\s\\r\\n]*" +
				RGX.REP_SPACES_SRN("Manual intervention might be")+"\\brequired\\b[\\s\\r\\n]*\\.[\\s\\r\\n]*\\*[\\s\\r\\n]*\\/";
		pFindDesc ="SQlWAYS misleading  Comment";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		//--------------------
		//TIMESTAMPDIFF(
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add("TIMESTAMPDIFF"+RGX.SRN+RGX.ZERO_OR_MANY+"\\("); //46
		pFindPattern ="TIMESTAMPDIFF"+RGX.SRN+RGX.ZERO_OR_MANY+"\\(";
		pFindDesc ="TIMESTAMPDIFF";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		
		//--------------------
		//CREATE INDEX wihtout session. - 50
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add(RGX.B_STR("CREATE")+RGX.SRN+RGX.ONE_OR_MANY+RGX.B_STR("INDEX")+RGX.SRN+RGX.ONE_OR_MANY+"((?!\\bSESSION"+RGX.SRN+RGX.ZERO_OR_MANY+"\\.).)+");
		//pFindPattern =RGX.B_STR("CREATE")+RGX.SRN+RGX.ONE_OR_MANY+RGX.B_STR("INDEX")+RGX.SRN+RGX.ONE_OR_MANY+"((?!\\bSESSION"+RGX.SRN+RGX.ZERO_OR_MANY+"\\."+RGX.TEXT+RGX.ONE_OR_MANY+"\\bSESSION.TT\\_).)+";
		pFindPattern =RGX.B_STR("CREATE")+RGX.SRN+RGX.ONE_OR_MANY+RGX.B_STR("INDEX")+RGX.SRN+RGX.ONE_OR_MANY+"((?!\\bSESSION"+RGX.SRN+RGX.ZERO_OR_MANY+"\\.).)+"+RGX.TEXT+RGX.ONE_OR_MANY+"\\bSESSION.TT\\_";
		pFindDesc = "Index Created on Temp Table without SESSION";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		//--------------------
		//select   DataElementId INTO V_DataElementID	   FROM DcmsDataElementForms
		//\\bSELECT\\b[\\S\\W\\w\\r\\n]+\\bINTO\\b[\\S\\W\\w\\r\\n]+\\bFROM\\b\\s+
		//RGX.B_STR("SELECT")+RGX.TEXT+RGX.ONE_OR_MANY+RGX.B_STR("INTO")+RGX.TEXT+RGX.ONE_OR_MANY+RGX.B_STR("FROM")+RGX.TEXT+RGX.ONE_OR_MANY+"((?!"+RGX.REP_SPACES_SRN("FETCH FIRST 1 ROWS ONLY")+").)+"
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add(RGX.B_STR("SELECT")+RGX.TEXT+RGX.ONE_OR_MANY+RGX.B_STR("INTO")+RGX.TEXT+RGX.ONE_OR_MANY+RGX.B_STR("FROM")+RGX.SRN+RGX.ONE_OR_MANY+"((?!"+RGX.REP_SPACES_SRN("FETCH FIRST 1 ROWS ONLY")+").)+");
		//pFindPattern =RGX.B_STR("SELECT")+RGX.TEXT+RGX.ONE_OR_MANY+RGX.B_STR("INTO")+RGX.TEXT+RGX.ONE_OR_MANY+RGX.B_STR("FROM")+RGX.SRN+RGX.ONE_OR_MANY+"((?!"+RGX.REP_SPACES_SRN("FETCH FIRST 1 ROWS ONLY")+").)+";
		pFindPattern = RGX.B_STR("SELECT")+RGX.TEXT+RGX.ONE_OR_MANY+RGX.B_STR("INTO")+RGX.TEXT+RGX.ONE_OR_MANY+RGX.B_STR("FROM")+RGX.SRN+RGX.ONE_OR_MANY+"((?!"+RGX.REP_SPACES_SRN("FETCH FIRST 1 ROWS ONLY")+").)+"+RGX.SRN+RGX.ZERO_OR_MANY+";"+RGX.SRN+RGX.ZERO_OR_MANY+"$";
		pFindDesc = "Select INTO statements must return only one record";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		//--------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add("CAST"+RGX.SRN+RGX.ZERO_OR_MANY+"\\("+"\\'"+RGX.SRN+RGX.ZERO_OR_MANY+"\\'"+RGX.SRN+RGX.ZERO_OR_MANY+"AS"+RGX.SRN+RGX.ZERO_OR_MANY+"INTEGER"); //60
		//cast('' AS integer)
		pFindPattern ="CAST"+RGX.SRN+RGX.ZERO_OR_MANY+"\\("+"\\'"+RGX.SRN+RGX.ZERO_OR_MANY+"\\'"+RGX.SRN+RGX.ZERO_OR_MANY+"AS"+RGX.SRN+RGX.ZERO_OR_MANY+"INTEGER";
		pFindDesc = "Null value casted to integer";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		//--------------------
		//UPDATE STATISTICS
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add(RGX.REP_SPACES_SRN("UPDATE STATISTICS")+RGX.TEXT+RGX.ONE_OR_MANY); //69
		pFindPattern =RGX.REP_SPACES_SRN("UPDATE STATISTICS")+RGX.TEXT+RGX.ONE_OR_MANY;
		pFindDesc = "Update Statisitcs - not supported";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		//--------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add("(?i)if[\\r\\n\\s]*\\([\\r\\n\\s\\w_]*\\![\\r\\n\\s]*=");//75
		pFindPattern ="(?i)if[\\r\\n\\s]*\\([\\r\\n\\s\\w_]*\\![\\r\\n\\s]*=";
		pFindDesc = "If Condition without null check";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		//--------------------
		/*pPostProcChangePatDTO =new PostProcChangePatDTO();
		//lMasterPatternList.add("(?i)if[\\r\\n\\s]*\\([\\r\\n\\s\\w_]*\\![\\r\\n\\s]*=");//75
		pFindPattern ="(?i)CAST"+RGX.SRN+RGX.ZERO_OR_MANY+"\\("+RGX.TEXT+RGX.ONE_OR_MANY+RGX.B_STR("AS")+RGX.SRN+RGX.ONE_OR_MANY+RGX.L_STR("TIMESTAMP");
		
		pFindDesc = "Type Casting Varchar to timestamp ";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);*/
		//--------------------
		//"CAST"+RGX.SRN+RGX.ZERO_OR_MANY+"\\("+RGX.TEXT+RGX.ONE_OR_MANY+RGX.B_STR("AS")+RGX.SRN+RGX.ONE_OR_MANY+RGX.L_STR("TIMESTAMP")
		//--------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//SET v_DistributionDate=v_ControlDate+1 MONTH
		pFindPattern ="(?i)\\b[\\w\\w]+[\\s\\r\\n]*(\\+|\\-)[\\d]+[\\s\\r\\n]+(\\bMONTH\\b|\\bDAY\\b|\\bYEAR\\b)";
		pFindDesc = "Varaible need to be checked to type cast as timestamp";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		//--------------------
		
		
		
		
		
		
		return lMasterPatternList;
		
	}
	//Main Method to Start the Process
	public List checkBlocks(List lBlockStrList,String pProcName){
		List lMasterPatternList =getMasterPatternList();
		
		//seeing for recursive proc calls
		//[\\bEXEC\\b|\\bCALL\\b]+ Procename - 39
		//lMasterPatternList.add("("+RGX.B_STR("EXEC")+RGX.OR+RGX.B_STR("CALL")+")"+RGX.SRN+RGX.ONE_OR_MANY+RGX.B_STR(pProcName)); //39
		PostProcChangePatDTO pPostProcChangePatDTO=new PostProcChangePatDTO();
		String findPattern="("+RGX.B_STR("EXEC")+RGX.OR+RGX.B_STR("CALL")+")"+RGX.SRN+RGX.ONE_OR_MANY+RGX.B_STR(pProcName);
		String pFindDesc ="";
		pPostProcChangePatDTO.setFindPattern(findPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterPatternList.add(pPostProcChangePatDTO);
		
		for (int i = 0; i < lMasterPatternList.size(); i++) {
			//System.out.println("::::list val::"+lMasterPatternList.get(i));
			lBlockStrList = identifyPatterns( pProcName,lBlockStrList,(PostProcChangePatDTO)lMasterPatternList.get(i));
		}
		for (int i = 0; i < lBlockStrList.size(); i++) {
			//lBlockStrList.set(i, (String)lBlockStrList.get(i)+"<br/>");
			lBlockStrList.set(i, (String)lBlockStrList.get(i)+"\n");
		}
		return lBlockStrList;
	}

	public List identifyPatterns(String pProcName,List lBlockStrList,PostProcChangePatDTO pPostProcChangePatDTO){
		
		String pPatternString =pPostProcChangePatDTO.getFindPattern();
		Pattern lChkPattern=Pattern.compile(pPatternString,Pattern.CASE_INSENSITIVE);
		StringBuffer lBlockStrBuf=null;
		int shiftValue=0;
		int oldLength=0;
		boolean lchk=false;
		for (int i = 0; i < lBlockStrList.size(); i++) {
			lBlockStrBuf=new StringBuffer((String)lBlockStrList.get(i));
			shiftValue=0;
			Matcher lChkPatMatcher=lChkPattern.matcher(lBlockStrBuf.toString());
			lchk=false;
			while(lChkPatMatcher.find()){
				/*if(lchk==false){
					System.out.println("::::::::::::::::::::::::::::::::::::::");
					System.out.println(lBlockStrBuf);
					System.out.println("::::::::::::::::::::::::::::::::::::::");
					lchk=true;
				}*/
								
				//
				oldLength=lBlockStrBuf.length();
				//System.out.println(lBlockStrBuf.substring(lChkPatMatcher.start()+shiftValue,lChkPatMatcher.end()+shiftValue));
				lBlockStrBuf=lBlockStrBuf.replace(lChkPatMatcher.start()+shiftValue, lChkPatMatcher.end()+shiftValue, "<font_DBT_SPACE_style=\"background-color:yellow;color:RED\"_DBT_SPACE_title=\""+pPostProcChangePatDTO.getFindDesc()+"\"> "+lChkPatMatcher.group(0).trim()/*"-Captured Pattern-"*/+" </font>");
				shiftValue+=lBlockStrBuf.length()-oldLength;
				//
				//System.out.println("---------------------");
				//System.out.println(lChkPatMatcher.group(0));
				//System.out.println(lBlockStrBuf);
				//lChkPatMatcher=lChkPattern.matcher(lBlockStrBuf.toString());
			}
			
			//lBlockStrBuf=new StringBuffer(lBlockStrBuf.toString().replaceAll("\n", "<br/>"));
			lBlockStrList.set(i, removeToolChars(lBlockStrBuf.toString()));
			/*if(lChkPatMatcher.find()){
				System.out.println(lChkPatMatcher.group(0));
			}*/
		}
		/*System.out.println("--------------------------------------------------------------------------------");
		for (int i = 0; i < lBlockStrList.size(); i++) {
			System.out.println(lBlockStrList.get(i));
		}*/
		return lBlockStrList;
	}
	
	 public String removeToolChars(String pLine){
     	//System.out.println(":::pline in remove method::::"+pLine);
     	//System.out.println(":::pline in remove method::::"+ToolsUtil.replaceNull(pLine).replaceAll("0000_TFC_", " ").replaceAll("_DBT_COMM_", " "));
     	return pLine.replaceAll("0000_TFC_", " ").replaceAll("_DBT_COMM_", " ");     	
     }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Inside Main:::"+new Timestamp(System.currentTimeMillis()));
		PostProcessorIdentifyPatterns lPotProcessorIdentifyPatterns=new PostProcessorIdentifyPatterns();
		String lProjectId="";		
		String lRootFolderPath="C:\\arun\\documents\\project\\Test Run\\post_processor\\3";
		Connection lConnection=null;

		System.out.println("End Main:::"+new Timestamp(System.currentTimeMillis()));
	}
}
