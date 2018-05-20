package com.tcs.tools.business.postprocessor.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.business.postprocessor.PostProcessFileUpload;
import com.tcs.tools.business.postprocessor.TextComparison;
import com.tcs.tools.business.postprocessor.dto.PostProcChangePatDTO;
import com.tcs.tools.business.postprocessor.dto.PostProcChangeTrackerDTO;
import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.dto.ProjectDetailsMainDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.RGX;
import com.tcs.tools.web.util.ToolsUtil;
/*
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.ESqlStatementType;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.TCallSpec;
import gudusoft.gsqlparser.nodes.TExpression;
import gudusoft.gsqlparser.nodes.TObjectName;
import gudusoft.gsqlparser.pp.print.TextPrinter;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
import gudusoft.gsqlparser.stmt.TUnknownSqlStatement;
import gudusoft.gsqlparser.stmt.db2.TCustomDb2Stmt;
import gudusoft.gsqlparser.stmt.db2.TDb2CallStmt;
import gudusoft.gsqlparser.stmt.db2.TDb2ConditionDeclaration;
import gudusoft.gsqlparser.stmt.db2.TDb2CreateProcedure;
import gudusoft.gsqlparser.stmt.db2.TDb2IfStmt;
import gudusoft.gsqlparser.stmt.db2.TDb2SetVariableStmt;
import gudusoft.gsqlparser.stmt.db2.TDb2SqlVariableDeclaration;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlCreateProcedure;

*/
public class PostProcessorApplyChangesDAO {

	private static Logger logger = Logger.getLogger("ToolLogger");

	HashMap lVariableDataTypeMap = null;
	List lSourceBlockStrList = null;
	String lProjectId="";
	String lCompleteProcName="";
	Connection lConnection=null;
	String lWriteMode="";
	String lIdentifyOrReplaceMode="";
	//forming ChangePatternsList
	public List getMasterApplyPatternList(String pProcName){
		List lMasterApplyPatternList = new ArrayList();
		String pReplacementText="";
		String pReplacementPattern="";
		String pFindPattern="";
		int pGroupCount =0;
		String pFindDesc="";
		String pdataTypeToChk="";
		
		PostProcChangePatDTO pPostProcChangePatDTO=null;
		//--------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		pFindPattern ="\\bIF\\b[\\s\\r\\n]+CHAR[\\s\\r\\n]*\\([\\s\\r\\n]*coalesce[\\s\\r\\n]*\\((.+?)\\,\\s*\\'1900\\-01\\-01[\\s\\r\\n]+00:00:00\\.000\\'\\)[\\s\\r\\n]*\\)[\\s\\r\\n]*=[\\s\\r\\n]*\\'\\'" ;
		pReplacementPattern = pFindPattern;
		pReplacementText ="IF coalesce("+ToolConstant.TOOL_DELIMT_GROUP_SPLIT+1+",'1900-01-01 00:00:00.000')  =  '1900-01-01 00:00:00.000'";
		pGroupCount =1;
		pFindDesc ="If Char Coalesce with timestamp condition";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		
		//------------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		pFindPattern ="\\bSWF_Str\\b[\\s\\r\\n]*\\(";
		pReplacementPattern = pFindPattern;
		pFindDesc ="str() Function Usage";
		//pBlockToCheck = "select 'Total ' || SWF_Str(v_totalHeaderCount) || ' records on feed file'";
		pReplacementText=" /* dbTransplant Change Comment- str() to to_char() */ to_char ( ";
		pGroupCount = 0;
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		
		//------------------------
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		pFindPattern ="\\bSUBSTR\\b[\\s\\r\\n]*\\(";
		pFindDesc ="substring() function usage";
		pReplacementPattern = pFindPattern;
		//pBlockToCheck = ",SUBSTR('abcde',1,7,1)";
		pReplacementText=" /* dbTransplant Change Comment- _SUBSTR() to SWF_SUBSTR() */ SWF_SUBSTR ( ";
		pGroupCount = 0;
		
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------		
		//CURSOR WITH RETURN TO CLIENT
		//pFindPattern ="(?i)\\bDECLARE\\b[\\s\\r\\n\\w\\W\\S]+\\bCURSOR\\b[\\s\\r\\n]+\\bWITH\\b[\\s\\r\\n]+\\bRETURN\\b[\\s\\r\\n]+((?!\\bTO\\b[\\s\\r\\n]+\\bCLIENT\\b).)+";
		//pFindPattern ="\\bDECLARE\\b"+RGX.SRN+RGX.ONE_OR_MANY+"\\bSWV_cursor"+RGX.TEXT+RGX.ZERO_OR_MANY+"\\bCURSOR\\b"+RGX.SRN+RGX.ONE_OR_MANY+"((?!\\bWITH\\b"+RGX.SRN+RGX.ONE_OR_MANY+"\\bRETURN\\b"+RGX.SRN+RGX.ONE_OR_MANY+"\\bTO\\b"+RGX.SRN+RGX.ONE_OR_MANY+"\\bCLIENT\\b).)+";
		//pReplacementPattern ="(?i)\\bDECLARE\\b"+RGX.SRN+RGX.ONE_OR_MANY+"\\bSWV_cursor"+RGX.TEXT+RGX.ZERO_OR_MANY+"\\bCURSOR\\b[\\s\\r\\n]+\\bWITH\\b[\\s\\r\\n]+\\bRETURN\\b";
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		pFindPattern ="(?i)\\bDECLARE\\b[\\s\\r\\n\\w\\W\\S]+\\bCURSOR\\b[\\s\\r\\n]+\\bWITH\\b[\\s\\r\\n]+\\bRETURN\\b[\\s\\r\\n]+((?!\\bTO\\b[\\s\\r\\n]+\\bCLIENT\\b).)+";
		pReplacementPattern ="(?i)\\bDECLARE\\b[\\s\\r\\n\\w\\W\\S]+\\bCURSOR\\b[\\s\\r\\n]+\\bWITH\\b[\\s\\r\\n]+\\bRETURN\\b";
		pFindDesc ="Declare Cursor with return to client";
		pReplacementText = " TO CLIENT ";
		pGroupCount = -3;
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------
		/*pPostProcChangePatDTO =new PostProcChangePatDTO();
		pFindPattern ="(?i)\\bDECLARE\\b[\\s\\r\\n\\w\\W\\S]+\\bCURSOR\\b[\\s\\r\\n]+((?!\\bWITH\\b[\\s\\r\\n]+\\bHOLD\\b).)+";
		pReplacementPattern ="(?i)\\bDECLARE\\b[\\s\\r\\n\\w\\W\\S]+\\bCURSOR\\b";
		pReplacementText = " WITH HOLD";
		pGroupCount = -3;
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);*/
		//------------------------		
		
		//Count -2 :Add Text at the End of the Block;
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		pFindPattern ="(?i)\\bSELECT\\b[\\s\\r\\n\\w\\W]+\\bINTO\\b[\\s\\r\\n\\w\\W]+\\bFROM\\b[\\s\\r\\n\\w\\W]+;[\\s\\r\\n]*$";
		
		pReplacementPattern = pFindPattern;
		pFindDesc ="Select Into Variable FETCH FIRST 1 ROWS ONLY ";
		//pBlockToCheck = ",SUBSTR('abcde',1,7,1)";
		pReplacementText=" FETCH FIRST 1 ROWS ONLY  /* dbTransplant Change Comment- Added FETCH FIRST 1 ROWS ONLY */ "; 
		pGroupCount = -2;
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------
		//TIMESTAMPDIFF
		/*pPostProcChangePatDTO =new PostProcChangePatDTO();
		pFindPattern ="\\s*\\bTIMESTAMPDIFF";
		pReplacementPattern = pFindPattern;		
		pReplacementText=" FN_MILLISECONDS  ";
		pGroupCount = 0;
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);*/
		//------------------------		
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		pFindPattern="CAST[\\s\\r\\n]*\\(\\'[\\s\\r\\n]*\\'[\\s\\r\\n]*AS[\\s\\r\\n]+INTEGER[\\s\\r\\n]*\\)";
		pReplacementPattern = pFindPattern;
		pFindDesc ="CAST empty string as Integer";
		pReplacementText ="CAST('0' AS INTEGER ) /* dbTransplant Change Comment- casting '' to '0' */ ";
		pGroupCount =0;
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------
		//IF(V_VAR1 !=Y)
		//IF(V_VAR1 IS NULL  OR V_VAR1 !=Y)
		pFindPattern ="(?i)if[\\r\\n\\s]*\\([\\r\\n\\s\\w_]*\\![\\r\\n\\s]*=";
		pReplacementPattern ="(?i)if[\\r\\n\\s]*\\((.+?)\\![\\r\\n\\s]*=";
		pReplacementText = "_DBT_VAR_RPL_ IS NULL  OR ";
		pFindDesc ="If Condition with NULL Check";
		pGroupCount = -4;
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------
		//CREATE INDEX ClosedIntervention_IX1  ON session.tt_ClosedIntervention
		//CREATE INDEX session.ClosedIntervention_IX1  ON session.tt_ClosedIntervention
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		pFindPattern =RGX.B_STR("CREATE")+RGX.SRN+RGX.ONE_OR_MANY+RGX.B_STR("INDEX")+RGX.SRN+RGX.ONE_OR_MANY+"((?!\\bSESSION"+RGX.SRN+RGX.ZERO_OR_MANY+"\\.).)+"+RGX.TEXT+RGX.ONE_OR_MANY+"\\bSESSION.TT\\_";
		pReplacementPattern = "(?i)\\bCREATE\\b[\\s\\r\\n]+\\bINDEX\\b[\\s\\r\\n]+";
		pFindDesc ="Create Index On Temporary Table Using Sessions";
		pReplacementText="Session.";		
		pGroupCount = -3;
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
 
		//------------------------
		// SET v_DistributionDate = v_ControlDate+1 MONTH
		// SET v_DistributionDate = CAST(v_ControlDate AS TIMESTAMP) +1 MONTH; - after change if v_ControlDate is not timestamp.
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		pFindPattern ="(?i)\\b[\\w\\w]+[\\s\\r\\n]*(\\+|\\-)[\\d]+[\\s\\r\\n]+(\\bMONTH\\b|\\bDAY\\b|\\bYEAR\\b)";
		pReplacementPattern = "^(.+?)(\\+|\\-)";
		pFindDesc ="Casting to Timestmap";
		pReplacementText="CAST( _DBT_VAR_RPL_ AS TIMESTAMP)";
		pGroupCount = -5;
		pdataTypeToChk="!TIMESTAMP";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		pPostProcChangePatDTO.setDataTypeToChk(pdataTypeToChk);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------
		/*\"FETCH FIRST 1 ROWS ONLY\" has been added to UPDATE statement, 
		 although this may not work properly for DB2. 
		 Manual intervention might be required.*/
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		//_DBT_COMM_
		/*
		 * FETCH_DBT_COMM_FIRST_DBT_COMM_1_DBT_COMM_ROWS_DBT_COMM_ONLY"_DBT_COMM_has_DBT_COMM_been_DBT_COMM_added_DBT_COMM_to_DBT_COMM_UPDATE_DBT_COMM_statement,_DBT_COMM_
		 although_DBT_COMM_this_DBT_COMM_may_DBT_COMM_not_DBT_COMM_work_DBT_COMM_properly_DBT_COMM_for_DBT_COMM_DB2._DBT_COMM_
		 Manual_DBT_COMM_intervention_DBT_COMM_might_DBT_COMM_be_DBT_COMM_required._DBT_COMM_ 

		 */
		pFindPattern ="(?i)\\/\\*_DBT_COMM_\"FETCH_DBT_COMM_FIRST_DBT_COMM_1_DBT_COMM_ROWS_DBT_COMM_ONLY\"_DBT_COMM_has_DBT_COMM_been_DBT_COMM_added_DBT_COMM_to_DBT_COMM_UPDATE_DBT_COMM_statement\\,_DBT_COMM_" +
				"[\\s\\r\\n]*although_DBT_COMM_this_DBT_COMM_may_DBT_COMM_not_DBT_COMM_work_DBT_COMM_properly_DBT_COMM_for_DBT_COMM_DB2\\._DBT_COMM_" +
				"[\\s\\r\\n]*Manual_DBT_COMM_intervention_DBT_COMM_might_DBT_COMM_be_DBT_COMM_required\\._DBT_COMM_\\*\\/" ;
				
		/*pFindPattern ="(?i)\\/[\\s\\r\\n]*\\*[\\s\\r\\n]*\"[\\s\\r\\n]*\\bFETCH\\b[\\s\\r\\n]+"+RGX.REP_SPACES_SRN("FIRST 1 ROWS")+"\\bONLY\\b[\\s\\r\\n]*\"[\\s\\r\\n]*" +
				RGX.REP_SPACES_SRN("has been added to UPDATE")+"\\bstatement\\b[\\s\\r\\n]*,[\\s\\r\\n]*" +
				RGX.REP_SPACES_SRN("although this may not work properly for")+"\\bDB2\\b[\\s\\r\\n]*\\.[\\s\\r\\n]*" +
				RGX.REP_SPACES_SRN("Manual intervention might be")+"\\brequired\\b[\\s\\r\\n]*\\.[\\s\\r\\n]*\\*[\\s\\r\\n]*\\/";*/
		
		pReplacementPattern = pFindPattern;
		pFindDesc ="Unwanted Comment";
		//pReplacementText=" /* UPDATE COMMENT FETCH FIRST 1 ROWS ONLY removed by DBTransplant */ ";
		pReplacementText="";
		pGroupCount = -5;
		pdataTypeToChk="";
		boolean pReplaceComplete=true;
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		pPostProcChangePatDTO.setDataTypeToChk(pdataTypeToChk);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------
		//(?i)\\bUPDATE\\b[\\s\\r\\n]+\\bSTATISTICS\\b[\\s\\r\\n]+(.+?)[\\s\\r\\n]*;
		
		
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		pFindPattern ="(?i)\\bUPDATE\\b[\\s\\r\\n]+\\bSTATISTICS\\b[\\s\\r\\n]+(.+?)[\\s\\r\\n]*;";
		pReplacementPattern =pFindPattern;	
		pFindDesc ="Update Statistics Usage ";
		pReplacementText ="CALL SYSPROC.ADMIN_CMD('RUNSTATS on Table "+ToolConstant.TOOL_DELIMT_GROUP_SPLIT+1+" with distribution and indexes all allow read access')" +
				"   /* dbTransplant Change Comment- to handle UPDATE STATISTICS */ ;";		
		pGroupCount = 1;
		pdataTypeToChk="";		
		pReplaceComplete=false;
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		pPostProcChangePatDTO.setDataTypeToChk(pdataTypeToChk);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------		
		pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="("+RGX.B_STR("EXEC")+RGX.OR+RGX.B_STR("CALL")+")"+RGX.SRN+RGX.ONE_OR_MANY+RGX.B_STR(pProcName);
		pFindDesc ="Recursive Stored Procedure Call";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------		
		pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)\\bBETWEEN\\b";
		pFindDesc ="BETWEEN Clause";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------		
		pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)\\bLIKE\\b";
		pFindDesc ="LIKE Clause";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------		
		pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)\\@\\@spid\\b";
		pFindDesc ="SPID Variable usage";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------
		
		
		pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)\\bSUM\\s*\\("+"((?!coalesce).)*";
		pFindDesc ="SUM Clause";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------
		pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)INOUT";
		pFindDesc =" 'INOUT' Parameter Need to Handle Carefully";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
			
		//------------------------
		pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)SELECT[\\s\\r\\n\\W\\w]+\\b(SUM|AVG|COUNT|MIN|MAX)\\b[\\s\\r\\n\\W\\w]+\\bGROUP\\b[\\s\\r\\n]+\\bBY\\b[\\s\\r\\n\\W\\w]+";
		pFindDesc =" Aggregate Functions Used in Select with Group By";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);		
		
		//------------------------
	/*	pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)SELECT\\b[\\s\\r\\n\\W\\w]+\\bORDER\\b[\\s\\r\\n]+\\bBY\\b[\\s\\r\\n\\W\\w]+";
		pFindDesc =" Select with Order By";
		//pReplacementPattern = pFindPattern;
		pReplacementText=removeTableAliasFromOrderBy(pFindPattern);
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);	*/
		
		
		//------------------------
		pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)\\bCURRENT\\b[\\s\\r\\n]+\\bSCHEMA\\b";
		pFindDesc =" CURRENT SCHEMA USED";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		
		//------------------------
		pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)\\bCALL\\b[\\s\\r\\n]+\\bTestSP\\b\\([\\s\\r\\n]*\\)";
		pFindDesc ="CALL TestSP() Need to be handled";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------
		//SWV_
		/*pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)\\bSWV_";
		pFindDesc ="Prefix Used need to be Changed";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);*/
		//------------------------
		//SWV_cursor_
		pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)\\bSWV_cursor";
		pFindDesc ="Cursor Prefix need to be changed as C_cursor";
		pReplacementPattern = pFindPattern;
		pReplacementText="C_cursor";
		pGroupCount=0;
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------
		//SWL_
		pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)\\bSWL_";
		pFindDesc ="Cursors which are used in loops Prefix need to be changed as L_";		
		pGroupCount=0;
		pReplacementPattern = pFindPattern;
		pReplacementText="L_";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		
		//------------------------
		//Select query having ORDER BY and DISTINCT Clauses need to be handled using OLAP functions.
		pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)SELECT[\\s\\r\\n]+\\bDISTINCT\\b[\\s\\r\\n\\W\\w]+\\bORDER\\b[\\s\\r\\n]+\\bBY\\b[\\s\\r\\n\\W\\w]+";
		pFindDesc ="Select query having ORDER BY and DISTINCT Clauses need to be handled using OLAP functions.";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		//------------------------
		//Select query having ORDER BY and DISTINCT Clauses need to be handled using OLAP functions.
		pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)\\bNOT\\b[\\s\\r\\n]+\\bLIKE\\b[\\s\\r\\n]+\\'[\\s\\r\\n\\W\\w]+\\'";
		pFindDesc ="Make Sure that Not Like with RegEx has been converted properly";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);	
		//------------------------
		//INSERT INTO SESSION.tt_Temptablename  FETCH FIRST 1 ROWS ONLY;
		pPostProcChangePatDTO=new PostProcChangePatDTO();
		pFindPattern="(?i)\\bINSERT\\b[\\s\\r\\n]+\\bINTO\\b[\\s\\r\\n]+\\bSESSION\\.tt_[\\s\\r\\n\\W\\w]+\\bFETCH\\b[\\s\\r\\n]+\\bFIRST\\b[\\s\\r\\n]+\\b1\\b[\\s\\r\\n]+\\bROWS\\b[\\s\\r\\n]+\\bONLY\\b";
		pFindDesc ="INSERT INTO TEMP TABLE may not need --FETCH FIRST 1 ROWS ONLY-- ";
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setFindDesc(pFindDesc);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);	 
		//------------------------
	
		//add DYNAMIC RESULT SETS 100
		pPostProcChangePatDTO =new PostProcChangePatDTO();
		pFindPattern ="(?i)\\bLANGUAGE\\b[\\s\\r\\n]+\\bSQL\\b";
		//pFindPattern="LANGUAGE SQL";
		pReplacementPattern = pFindPattern;
		//pFindDesc ="str() Function Usage";
		//pBlockToCheck = "select 'Total ' || SWF_Str(v_totalHeaderCount) || ' records on feed file'";
		pReplacementText="LANGUAGE SQL"+"\n"+"DYNAMIC RESULT SETS 100";
		pGroupCount = 0;
		pPostProcChangePatDTO.setFindPattern(pFindPattern);
		pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
		pPostProcChangePatDTO.setReplacementText(pReplacementText);
		pPostProcChangePatDTO.setGroupCount(pGroupCount);
		lMasterApplyPatternList.add(pPostProcChangePatDTO);
		
		
		//---------------------------
		
				
		/*
		pPostProcChangePatDTO =new PostProcChangePatDTO();
				pFindPattern ="(?i)\\bSET\\b[\\s\\r\\n]+\\bROWCOUNT\\b[\\s\\r\\n]+\\b0";
				pReplacementPattern = pFindPattern;
				//pFindDesc ="str() Function Usage";
				//pBlockToCheck = "select 'Total ' || SWF_Str(v_totalHeaderCount) || ' records on feed file'";
				pReplacementText="FETCH FIRST 0 ROWS ONLY";
				pGroupCount = 0;
				pPostProcChangePatDTO.setFindPattern(pFindPattern);
				pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
				pPostProcChangePatDTO.setReplacementText(pReplacementText);
				pPostProcChangePatDTO.setGroupCount(pGroupCount);
				lMasterApplyPatternList.add(pPostProcChangePatDTO);
				
				
				*/
			/*	
			
				//coalesce
				pPostProcChangePatDTO =new PostProcChangePatDTO();
				pFindPattern ="(?i)\\bSELECT\\b[\\s\\r\\n]+\\bSUM\\s*\\(\\s*\\b\\w\\s*\\)\\s*\\bFROM";
				pReplacementPattern = "(?i)\\bSELECT\\b[\\s\\r\\n]+\\bSUM\\s*\\(\\s*\\b[\\s\\r\\n]+\\bCOALESCE\\s*\\(\\s*\\b\\w\\s*\\,\\s*\\b0\\s*\\)\\s*\\)\\s*\\bFROM";
				//pFindDesc ="str() Function Usage";
				//pBlockToCheck = "select 'Total ' || SWF_Str(v_totalHeaderCount) || ' records on feed file'";
				pReplacementText="";
				pGroupCount = 0;
				pPostProcChangePatDTO.setFindPattern(pFindPattern);
				pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
				pPostProcChangePatDTO.setReplacementText(pReplacementText);
				pPostProcChangePatDTO.setGroupCount(pGroupCount);
				lMasterApplyPatternList.add(pPostProcChangePatDTO);
		*/
				
				//variable SWV_ to V_
				pPostProcChangePatDTO =new PostProcChangePatDTO();
				pFindPattern ="(?i)\\bDECLARE\\b[\\s\\r\\n]+\\bSWV_(?!CURSOR)";
				pReplacementPattern = pFindPattern;
				pReplacementText="DECLARE V_";
				pGroupCount = 0;
				pPostProcChangePatDTO.setFindPattern(pFindPattern);
				pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
				pPostProcChangePatDTO.setReplacementText(pReplacementText);
				pPostProcChangePatDTO.setGroupCount(pGroupCount);
				lMasterApplyPatternList.add(pPostProcChangePatDTO);
				
			
				//change function name from SWV_ to FN_
				pPostProcChangePatDTO =new PostProcChangePatDTO();
				pFindPattern ="(?i)\\bCREATE\\b[\\s\\r\\n]+\\bFUNCTION\\b[\\s\\r\\n]+\\bSWV\\s*\\_";
				pReplacementPattern = pFindPattern;
				pReplacementText="CREATE FUNCTION FN_";
				pGroupCount = 0;
				pPostProcChangePatDTO.setFindPattern(pFindPattern);
				pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
				pPostProcChangePatDTO.setReplacementText(pReplacementText);
				pPostProcChangePatDTO.setGroupCount(pGroupCount);
				lMasterApplyPatternList.add(pPostProcChangePatDTO);
				//(?i)\\bCAST\\s*\\(\\s*\\b[\\s\\r\\n]+\\bCURRENT\\b[\\s\\r\\n]+\\bTIMESTAMP\\b[\\s\\r\\n]+\\bAS\\b[\\s\\r\\n]+\\bVARCHAR\\s*\\(\\s*\\[0-9]{1,}\\s*\\)\\s*\\)\\s*
				
				
				
				
				//removing multiple COALESCE
				pPostProcChangePatDTO =new PostProcChangePatDTO();
				pFindPattern ="(?i)\\bSUM\\s*\\(\\b(COALESCE\\s*\\(){1,}";
				pReplacementPattern = pFindPattern;
				pReplacementText="SUM(COALESCE(";
				pGroupCount = 0;
				pPostProcChangePatDTO.setFindPattern(pFindPattern);
				pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
				pPostProcChangePatDTO.setReplacementText(pReplacementText);
				pPostProcChangePatDTO.setGroupCount(pGroupCount);
				lMasterApplyPatternList.add(pPostProcChangePatDTO);
				
				
				 /*
				
				pPostProcChangePatDTO =new PostProcChangePatDTO();
				pFindPattern ="(?i)\\bCAST\\b[\\s\\r\\n]*\\([\\s\\r\\n]*\\bCURRENT\\b[\\s\\r\\n]+\\bTIMESTAMP\\b\\((.+?)\\)[\\s\\r\\n]*\\bAS\\b[\\s\\r\\n]+\\bVARCHAR\\b[\\s\\r\\n]*\\([\\d]+[\\s\\r\\n]*\\)[\\s\\r\\n]*\\)";
				pReplacementPattern = pFindPattern;
				pReplacementText="TO_CHAR(CURRENT TIMESTAMP ,'mm/dd/yy')";
				pGroupCount = 0;
				pPostProcChangePatDTO.setFindPattern(pFindPattern);
				pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
				pPostProcChangePatDTO.setReplacementText(pReplacementText);
				pPostProcChangePatDTO.setGroupCount(pGroupCount);
				lMasterApplyPatternList.add(pPostProcChangePatDTO);
				*/
				
				
				//deleting inspirer licensing information
				pPostProcChangePatDTO =new PostProcChangePatDTO();
				pFindPattern ="(?i)\\--\\s*\\b(GENERATED|STORED|THIS|TIMESTAMP)[^\\n]*";
				//pFindPattern="--DECLARE V_ActiveInd SMALLINT";
				pReplacementPattern = pFindPattern;
				pReplacementText=" ";
				pGroupCount = 0;
				pPostProcChangePatDTO.setFindPattern(pFindPattern);
				pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
				pPostProcChangePatDTO.setReplacementText(pReplacementText);
				pPostProcChangePatDTO.setGroupCount(pGroupCount);
				lMasterApplyPatternList.add(pPostProcChangePatDTO);
				
				//replace decfloat with float
				pPostProcChangePatDTO =new PostProcChangePatDTO();
				pFindPattern ="(?i)\\bFLOAT";
				//pFindPattern="(?i)\\bset\\s*";
				pReplacementPattern = pFindPattern;
				pReplacementText="DECFLOAT";
				pGroupCount = 0;
				pPostProcChangePatDTO.setFindPattern(pFindPattern);
				pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
				pPostProcChangePatDTO.setReplacementText(pReplacementText);
				pPostProcChangePatDTO.setGroupCount(pGroupCount);
				lMasterApplyPatternList.add(pPostProcChangePatDTO);
				
				
				
				//remove unwanted code for creating empty temporary table
				pPostProcChangePatDTO =new PostProcChangePatDTO();
				pFindPattern ="(?i)\\bINSERT\\s*\\bINTO\\s*\\bSESSION.tt_[\\s\\r\\n\\W\\w]+\\bselect[\\s\\r\\n\\W\\w]+\\bwhere\\s*\\b1\\s*\\=\\s*\\b2\\s*\\;";
				
				pReplacementPattern = pFindPattern;
				pReplacementText=" ";
				pGroupCount = 0;
				pPostProcChangePatDTO.setFindPattern(pFindPattern);
				pPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
				pPostProcChangePatDTO.setReplacementText(pReplacementText);
				pPostProcChangePatDTO.setGroupCount(pGroupCount);
				lMasterApplyPatternList.add(pPostProcChangePatDTO);
				
				
				
				
				
		return lMasterApplyPatternList;
	}
	//Main Method to Start the Process
	public List checkBlocks(String pProjectId,List pSourceBlockStrList,List lBlockStrList,String pProcName,String pWriteMode,String pIdentifyOrReplaceMode){
		System.out.println(":::: inside method :: - checkBlocks - ::::;");
		//Get Required Data -Start
		lCompleteProcName=pProcName;
		pProcName=ToolsUtil.splitFileNameAndExtension(pProcName)[0];
		List lMasterApplyPatternList =getMasterApplyPatternList(pProcName);		
		//add schema patterns to master pattern list to change schemas
		
		lMasterApplyPatternList= replaceSourceSchemas(pProjectId,lMasterApplyPatternList);
		lMasterApplyPatternList=identifySybaseSysObjects(pProjectId,lMasterApplyPatternList);
		lVariableDataTypeMap=new HashMap();
		getAllDeclareVarOfSp( lBlockStrList, pProcName);
		getSPInputVariables(lBlockStrList);
		lSourceBlockStrList=pSourceBlockStrList;
		//System.out.println("lSourceBlockStrList Size::"+lSourceBlockStrList.size()+"lSourceBlockStrList ::->"+lSourceBlockStrList);
		lProjectId=pProjectId;
		lConnection=DBConnectionManager.getConnection();
		deleteForChangeTracker();
		try {
			lConnection.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lWriteMode=pWriteMode;
		lIdentifyOrReplaceMode= pIdentifyOrReplaceMode;
		//Get Required Data -End
		
		//Check in source and change in target - Start 
		String lSourceChkPattern="";
		String pFindPattern="";
		String pReplacementText="";		 
		String pFindDesc="";
		PostProcChangePatDTO lPostProcChangePatDTO=new PostProcChangePatDTO();
		//----------------------------------------------------
		//arun commented as this is not completely done
		//Check in Source for if - @transtate.
		// if v_transtate = 2 - before
		//IF (substr(l_error,1,2)='23') - after
		/*lSourceChkPattern="(?i)\\bif\\b[\\s\\r\\n]+@@transtate[\\s\\r\\n]*=[\\s\\r\\n]*2";
		pFindPattern="(?i)\\bif\\b[\\s\\r\\n]*(\\()*[\\s\\r\\n]*v_transtate[\\d]*[\\s\\r\\n]*=[\\s\\r\\n]*[\\d]+";
		pReplacementText=" IF (substr(l_error,1,2)='23') ";		 
		pFindDesc="";
		lPostProcChangePatDTO=new PostProcChangePatDTO();
		lPostProcChangePatDTO.setFindPattern(pFindPattern);
		lPostProcChangePatDTO.setReplacementPattern(pFindPattern);
		lPostProcChangePatDTO.setReplacementText(pReplacementText);
		lPostProcChangePatDTO.setGroupCount(0);
		lPostProcChangePatDTO.setDataTypeToChk("");
		lPostProcChangePatDTO.setFindDesc(pFindDesc);		
		lBlockStrList=chkInSourceNReplace(lBlockStrList,lSourceChkPattern,lPostProcChangePatDTO);*/
		//-------------------------------------------------------
		//Check in Source for create table tempdb..tablebname
		// if v_transtate = 2 - before
		//IF (substr(l_error,1,2)='23') - after

		lSourceChkPattern="(?i)\\bCREATE\\b[\\s\\r\\n]+\\bTABLE\\b[\\s\\r\\n]+\\bTEMPDB\\b\\.";
		pFindPattern="(?i)\\bCREATE\\b[\\s\\r\\n]+\\bTABLE\\b[\\s\\r\\n]+";
		pReplacementText=" DECLARE GLOBAL TEMPORARY TABLE ";		 
		lPostProcChangePatDTO=new PostProcChangePatDTO();
		lPostProcChangePatDTO.setFindPattern(pFindPattern);
		lPostProcChangePatDTO.setReplacementPattern(pFindPattern);
		lPostProcChangePatDTO.setReplacementText(pReplacementText);
		lPostProcChangePatDTO.setGroupCount(0);
		lPostProcChangePatDTO.setDataTypeToChk("");
		lPostProcChangePatDTO.setFindDesc(pFindDesc);
		lBlockStrList=chkInSourceNReplace(lBlockStrList,lSourceChkPattern,lPostProcChangePatDTO);
		//-------------------------------------------------------
		
		//Check in source and change in target - End
		
		//Identify specific statements like cast , replace, char() 
		//where v cant put regex on whole then pic that string alone n apply the same process.
		lBlockStrList=identifyStatementNChange(lBlockStrList,pProcName);
		
		//Check  Cursors opened multiple times should have declare cursor before opening if it is closed. 
		lBlockStrList=chkCursorDeclarations(pProjectId,lCompleteProcName,lBlockStrList);
		//rename identical cursor names
		//lBlockStrList=replaceDuplicateCursorNames(pProjectId,lCompleteProcName,lBlockStrList);
		
		//------------------------------------------- Adding Variables - Start---------------
		String pOldVarName="";
		String pNewVarName="";
		List pToAddStatemenstList=null;
		//For @@ServerName Var
		pOldVarName="@@servername";
		pNewVarName="DBTV_servername";		
		pNewVarName=chkVarNameNAssignNewName(pNewVarName);
		if(!"".equals(pNewVarName.trim())){
			pToAddStatemenstList=new ArrayList(); // Add to List in Reverse Order.
			pToAddStatemenstList.add("SELECT INST_NAME into "+pNewVarName+" FROM SYSIBMADM.ENV_INST_INFO;  /* dbTransplant Change Comment- to handle @@servername */");		
			pToAddStatemenstList.add("DECLARE "+pNewVarName+" VARCHAR(50)   /* dbTransplant Change Comment- to handle @@servername */ ;");
			lVariableDataTypeMap.put(pNewVarName.trim().toUpperCase(), "VARCHAR");
			lBlockStrList=addVarAfterDeclareNChangeVar(lBlockStrList,pProcName,pOldVarName,pNewVarName,pToAddStatemenstList);
		}
		
		//For @@SqlStatus var Name
		pOldVarName="SWV_Fetch_Status";
		pNewVarName="v_sqlstatus";
		pNewVarName=chkVarNameNAssignNewName(pNewVarName);
		if(!"".equals(pNewVarName.trim())){
			pToAddStatemenstList=new ArrayList(); // Add to List in Reverse Order.
			pToAddStatemenstList.add("set "+pNewVarName+" = sqlcode;  /* dbTransplant Change Comment- to handle @@sqlstatus */ ");		
			pToAddStatemenstList.add("DECLARE "+pNewVarName+" VARCHAR(50)  /* dbTransplant Change Comment- to handle @@sqlstatus */ ;");
			lVariableDataTypeMap.put(pNewVarName.trim().toUpperCase(), "VARCHAR");
			lBlockStrList=addVarAfterDeclareNChangeVar(lBlockStrList,pProcName,pOldVarName,pNewVarName,pToAddStatemenstList);
		}
		
	
		
		//for v_transatate variable adde dbut not completed so commenting
		/*//For SQLSTATE var Name
		pOldVarName="";
		pNewVarName="SQLSTATE";		
		if(!lVariableDataTypeMap.containsKey(pNewVarName.trim().toUpperCase())){
			pToAddStatemenstList=new ArrayList(); // Add to List in Reverse Order.							
			pToAddStatemenstList.add("DECLARE "+pNewVarName+" CHAR(5) DEFAULT '00000' ; ");
			lVariableDataTypeMap.put(pNewVarName.trim().toUpperCase(), "CHAR");
			lBlockStrList=addVarAfterDeclareNChangeVar(lBlockStrList,pProcName,pOldVarName,pNewVarName,pToAddStatemenstList);
		}
		//For l_error var Name
		pOldVarName="";
		pNewVarName="l_error";		
		if(!lVariableDataTypeMap.containsKey(pNewVarName.trim().toUpperCase())){
			pToAddStatemenstList=new ArrayList(); // Add to List in Reverse Order.							
			pToAddStatemenstList.add("DECLARE "+pNewVarName+" CHAR(5) DEFAULT '00000' ;  ");
			lVariableDataTypeMap.put(pNewVarName.trim().toUpperCase(), "CHAR");
			lBlockStrList=addVarAfterDeclareNChangeVar(lBlockStrList,pProcName,pOldVarName,pNewVarName,pToAddStatemenstList);
		}	*/	 
		///DECLARE l_error CHAR(5) DEFAULT '00000';
		
		//------------------------------------------- Adding Variables - End---------------
		//--UpdateStatistics Table
		//lBlockStrList=changeUpdateStatistics(lBlockStrList,pProcName);
		
		
		//Handle Recursive call to change it as dynamic sqls
		lBlockStrList=handleResursiveCallStatement(lBlockStrList,pProcName);
		
		
		//Cursor with hold
		//lBlockStrList=checkCursorWithHold(lBlockStrList,pProcName);
		
		lBlockStrList=likeOperatorChk(lBlockStrList,pProcName);
		lBlockStrList=modifySqlInPostProcessor(lBlockStrList,pProcName);
	//	lBlockStrList=replPattern(lBlockStrList,pProcName);
		for (int i = 0; i < lMasterApplyPatternList.size(); i++) {
			// Changing one by one PatternDTO
			lBlockStrList = applyChanges( pProcName,lBlockStrList,(PostProcChangePatDTO)lMasterApplyPatternList.get(i));
			
		}
		try {
			//lConnection.commit();
			lConnection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnectionManager.closeConnection(lConnection);
		for (int i = 0; i < lBlockStrList.size(); i++) {
			lBlockStrList.set(i,removeToolChars((String)lBlockStrList.get(i)));
		}
		
		return lBlockStrList;
	}
	//Applying one patternDto to complete block list n return list
	public List applyChanges(String pProcName,List lBlockStrList,PostProcChangePatDTO pPostProcChangePatDTO){
		String lChangedBolckStr="";
		for (int i = 0; i < lBlockStrList.size(); i++) {
			
			lChangedBolckStr=checkAndReplace((String)lBlockStrList.get(i),pPostProcChangePatDTO);			
			lBlockStrList.set(i, lChangedBolckStr);
			
		}
		return lBlockStrList;
	}
	//Actual Replace method 
	public String checkAndReplace(String pBlockToCheck,PostProcChangePatDTO pPostProcChangePatDTO){
		
	String pIdentifyBlockToCheck = pBlockToCheck;
	String pIntialBlockToCheck = pBlockToCheck;
	String pReplacementTextCopy = "";
	
	String pFindPattern=pPostProcChangePatDTO.getFindPattern();
	String pReplacementPattern=pPostProcChangePatDTO.getReplacementPattern();
	String pReplacementText=pPostProcChangePatDTO.getReplacementText();
	int pGroupCount=pPostProcChangePatDTO.getGroupCount();
	String pdataTypeToChk=pPostProcChangePatDTO.getDataTypeToChk();
	boolean pReplaceComplete=pPostProcChangePatDTO.isReplaceComplete();
	String lOldBlock="";
	int shiftValue=0;
	int start=0;
	int end=0;
	// Variables For Change Tracker
	String pOldText="";
	String pNewText="";
	
	/*if( !"IDENTIFY_PATTERN".equals(lIdentifyOrReplaceMode) && 
			(pReplacementPattern==null || "".equals(pReplacementPattern) || "".equals(pReplacementText) || pReplacementText==null)){
		return pBlockToCheck;
	}*/
	
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
	// Pattern prep=Pattern.compile(pReplacementPattern);
	// Matcher m1=null;
	// m1=prep.matcher("FETCH FIRST ");
	 Matcher lMatcher=null;
	 lMatcher=lStrFunctionPattern.matcher(pBlockToCheck);
	 StringBuffer sb= new StringBuffer();
	 boolean isInsertRequired = false;
	 
	 //System.out.println(pFindPattern);
	 while(lMatcher.find()){
		 
		 //System.out.println(":::::::before:::"+pBlockToCheck);
		 //"(?i)if[\\r\\n\\s]*\\((.+?)\\![\\r\\n\\s]*="
		 if(!"".equals(lOldBlock)){
			 shiftValue+=pBlockToCheck.length()-lOldBlock.length();
		 }
		 lOldBlock=pBlockToCheck;
		 
		 pOldText = pBlockToCheck.substring(lMatcher.start()+shiftValue,lMatcher.end()+shiftValue) ;  //For Tracker
			if(pGroupCount > 0 ){
    			for (int i = 1; i <= pGroupCount; i++) {
    				
    				start=lMatcher.start()+shiftValue;
    				end=lMatcher.end()+shiftValue;
    				
    				
    				if("IDENTIFY_PATTERN".equalsIgnoreCase(lIdentifyOrReplaceMode)){
    					pReplacementText = addColor(pBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());
    					pBlockToCheck=pBlockToCheck.substring(0,start)+pReplacementText+pBlockToCheck.substring(end,pBlockToCheck.length());
    				}else{
    					
    					if(!(pReplacementPattern==null || "".equals(pReplacementPattern) || "".equals(pReplacementText) || pReplacementText==null )){
	    					pReplacementText = pReplacementText.replaceAll(ToolConstant.TOOL_DELIMT_GROUP_SPLIT+i,lMatcher.group(1).trim());    				
	    					//pOldText = pBlockToCheck.substring(start,end) ;  //For Tracker  //For Tracker
	        				pNewText=pReplacementText; //For Tracker        				
	        				pReplacementText = addColor(pReplacementText,pPostProcChangePatDTO.getFindDesc()); //for adding font color green
	        				pBlockToCheck=pBlockToCheck.substring(0,start)+pReplacementText+pBlockToCheck.substring(end,pBlockToCheck.length());
    					}
    					else{
    						//for identification        				
            				pReplacementTextCopy = addColor(pIdentifyBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());
            				pIdentifyBlockToCheck=pIdentifyBlockToCheck.substring(0,start)+pReplacementTextCopy+pIdentifyBlockToCheck.substring(end,pIdentifyBlockToCheck.length());
    					}
        				
    				}
    				isInsertRequired = true;
    				
    				
    				
				}
			}else if(pGroupCount == 0 ){
				start=lMatcher.start()+shiftValue;
				end=lMatcher.end()+shiftValue;
				if("IDENTIFY_PATTERN".equalsIgnoreCase(lIdentifyOrReplaceMode)){
					pReplacementText = addColor(pBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());
					pBlockToCheck=pBlockToCheck.substring(0,start)+pReplacementText+pBlockToCheck.substring(end,pBlockToCheck.length());
				}else{
					
					if(!(pReplacementPattern==null || "".equals(pReplacementPattern) || "".equals(pReplacementText) || pReplacementText==null )){
						//pOldText = pBlockToCheck.substring(start,end) ;  //For Tracker  //For Tracker
	    				pNewText=pReplacementText; //For Tracker
						pReplacementText = addColor(pReplacementText,pPostProcChangePatDTO.getFindDesc());//for adding font color green
						pBlockToCheck=pBlockToCheck.substring(0,start)+pReplacementText+pBlockToCheck.substring(end,pBlockToCheck.length());
					}else{
						pReplacementTextCopy = addColor(pIdentifyBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());
						pIdentifyBlockToCheck=pIdentifyBlockToCheck.substring(0,start)+pReplacementTextCopy+pIdentifyBlockToCheck.substring(end,pIdentifyBlockToCheck.length());
					}
					
				}
				isInsertRequired = true;				
				
			}else if(pGroupCount == -1 ){
				start=lMatcher.start()+shiftValue;
				end=lMatcher.end()+shiftValue;
				//System.out.println("Group 0:::"+lMatcher.group(0).trim());
				if("IDENTIFY_PATTERN".equalsIgnoreCase(lIdentifyOrReplaceMode)){
					pReplacementText = addColor(pBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());
					pBlockToCheck=pBlockToCheck.substring(0,start)+pReplacementText+pBlockToCheck.substring(end,pBlockToCheck.length());
				}else{
					//if(m1.find()){
					if(!(pReplacementPattern==null || "".equals(pReplacementPattern) || "".equals(pReplacementText) || pReplacementText==null )){
						pReplacementText =lMatcher.group(0).trim()+ pReplacementText;
						//pOldText = pBlockToCheck.substring(start,end) ;  //For Tracker  //For Tracker
	    				pNewText=pReplacementText; //For Tracker
						pReplacementText = addColor(pReplacementText,pPostProcChangePatDTO.getFindDesc());//for adding font color green
						pBlockToCheck=pBlockToCheck.substring(0,start)+pReplacementText+pBlockToCheck.substring(end,pBlockToCheck.length());
					//}
					}else{
						pReplacementTextCopy = addColor(pIdentifyBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());
						pIdentifyBlockToCheck=pIdentifyBlockToCheck.substring(0,start)+pReplacementTextCopy+pIdentifyBlockToCheck.substring(end,pIdentifyBlockToCheck.length());
					}
				}	
				isInsertRequired = true;
			     				
			}else if(pGroupCount == -2 ){
				//Add Text at the End of the Block;
				//System.out.println("Group 0:::"+lMatcher.group(0).trim());
				start=lMatcher.start()+shiftValue;
				end=lMatcher.end()+shiftValue;
				if(pBlockToCheck.trim().endsWith(";")){
					if("IDENTIFY_PATTERN".equalsIgnoreCase(lIdentifyOrReplaceMode)){
						pReplacementText = addColor(pBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());																		
						pBlockToCheck=pBlockToCheck.substring(0,start)+pReplacementText+pBlockToCheck.substring(end,pBlockToCheck.length());
					}else{
						if(!(pReplacementPattern==null || "".equals(pReplacementPattern) || "".equals(pReplacementText) || pReplacementText==null )){
							//pOldText = pBlockToCheck.substring(start,end) ;  //For Tracker  //For Tracker
	        				pNewText=pReplacementText; //For Tracker
							pReplacementText = addColor(pReplacementText,pPostProcChangePatDTO.getFindDesc());//for adding font color green					
							pBlockToCheck=pBlockToCheck.substring(0,pBlockToCheck.lastIndexOf(";"));
							pBlockToCheck=pBlockToCheck+" \n"+pReplacementText+" ; ";
						}else{
							pReplacementTextCopy = addColor(pIdentifyBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());
							pIdentifyBlockToCheck=pIdentifyBlockToCheck.substring(0,start)+pReplacementTextCopy+pIdentifyBlockToCheck.substring(end,pIdentifyBlockToCheck.length());
						}
					}
					isInsertRequired = true;
				}
				
			}else if(pGroupCount == -3 ){
				//add Replacement Text after the end of the replacement regex and replace after group 0 position.
				start=lMatcher.start()+shiftValue;
				end=lMatcher.end()+shiftValue;
				Matcher lReplaceMatcher=Pattern.compile(pReplacementPattern).matcher(pBlockToCheck);
				if(lReplaceMatcher.find()){
					if("IDENTIFY_PATTERN".equalsIgnoreCase(lIdentifyOrReplaceMode)){
						pReplacementText = addColor(pBlockToCheck.substring(lReplaceMatcher.start(),lReplaceMatcher.end()),pPostProcChangePatDTO.getFindDesc());//for adding font color green					
						pBlockToCheck=pBlockToCheck.substring(0,lReplaceMatcher.start())+pReplacementText+pBlockToCheck.substring(lReplaceMatcher.end(),pBlockToCheck.length());
					}else{
						if(!(pReplacementPattern==null || "".equals(pReplacementPattern) || "".equals(pReplacementText) || pReplacementText==null )){
							//pOldText = pBlockToCheck.substring(start,end) ;  //For Tracker  //For Tracker
	        				pNewText=pReplacementText; //For Tracker
							pReplacementText = addColor(pReplacementText,pPostProcChangePatDTO.getFindDesc());//for adding font color green					
							pBlockToCheck=pBlockToCheck.substring(0,lReplaceMatcher.end())+pReplacementText+pBlockToCheck.substring(lReplaceMatcher.end(),pBlockToCheck.length());
						}else{
							pReplacementTextCopy = addColor(pIdentifyBlockToCheck.substring(lReplaceMatcher.start(),lReplaceMatcher.end()),pPostProcChangePatDTO.getFindDesc());
							pIdentifyBlockToCheck=pIdentifyBlockToCheck.substring(0,lReplaceMatcher.start())+pReplacementTextCopy+pIdentifyBlockToCheck.substring(lReplaceMatcher.end(),pIdentifyBlockToCheck.length());
						}
					}
					isInsertRequired = true;					
				}
			}else if(pGroupCount == -4 ){
				
				start=lMatcher.start()+shiftValue;
				end=lMatcher.end()+shiftValue;				
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
								
								if("IDENTIFY_PATTERN".equalsIgnoreCase(lIdentifyOrReplaceMode)){
									pReplacementText = addColor(pBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());//for adding font color green
								}else{
									if(!(pReplacementPattern==null || "".equals(pReplacementPattern) || "".equals(pReplacementText) || pReplacementText==null )){
										pReplacementText=pReplacementText.replaceAll("\\s*_DBT_VAR_RPL_\\s*",lTextFromRgx); 								
										lFindStr=lFindStr.substring(0,lReplaceMatcher.start(1))+pReplacementText+lFindStr.substring(lReplaceMatcher.start(1),lFindStr.length());
										//for adding font color green									
										pReplacementText=lFindStr;
										//pOldText = pBlockToCheck.substring(start,end) ;  //For Tracker  //For Tracker
				        				pNewText=pReplacementText+pBlockToCheck.substring(lMatcher.start(),lMatcher.end()); //For Tracker
										pReplacementText = addColor(pReplacementText,pPostProcChangePatDTO.getFindDesc());
									}else{
										pReplacementTextCopy = addColor(pIdentifyBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());
										pIdentifyBlockToCheck=pIdentifyBlockToCheck.substring(0,start)+pReplacementTextCopy+pIdentifyBlockToCheck.substring(end,pIdentifyBlockToCheck.length());
									}
								}
								
								isInsertRequired = true;
								
								pBlockToCheck=pBlockToCheck.substring(0,start)+pReplacementText+pBlockToCheck.substring(end,pBlockToCheck.length());
			 					//pBlockToCheck=pBlockToCheck.substring(0,lReplaceMatcher.start(1))+pReplacementText+pBlockToCheck.substring(lReplaceMatcher.start(1),pBlockToCheck.length());
							}
						}else{
							System.out.println("Variable Not Found in Declare Var HashMap::pdataTypeToChk->"+pdataTypeToChk+":::-:::"+lTextFromRgx.trim());
						}
						
					}else{
						if("IDENTIFY_PATTERN".equalsIgnoreCase(lIdentifyOrReplaceMode)){
							pReplacementText = addColor(pBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());//for adding font color green
						}else{
							if(!(pReplacementPattern==null || "".equals(pReplacementPattern) || "".equals(pReplacementText) || pReplacementText==null )){
								pReplacementText=pReplacementText.replaceAll("\\s*_DBT_VAR_RPL_\\s*",lTextFromRgx);
								lFindStr=lFindStr.substring(0,lReplaceMatcher.start(1))+pReplacementText+lFindStr.substring(lReplaceMatcher.start(1),lFindStr.length());
								//for adding font color green							
								pReplacementText=lFindStr;
								//pOldText = pBlockToCheck.substring(start,end) ;  //For Tracker  //For Tracker
		        				pNewText=pReplacementText+pBlockToCheck.substring(lMatcher.start(),lMatcher.end()); //For Tracker
								pReplacementText = addColor(pReplacementText,pPostProcChangePatDTO.getFindDesc());
							}else{
								pReplacementTextCopy = addColor(pIdentifyBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());
								pIdentifyBlockToCheck=pIdentifyBlockToCheck.substring(0,start)+pReplacementTextCopy+pIdentifyBlockToCheck.substring(end,pIdentifyBlockToCheck.length());
							}
						}
						isInsertRequired = true;
						pBlockToCheck=pBlockToCheck.substring(0,start)+pReplacementText+pBlockToCheck.substring(end,pBlockToCheck.length());
	 					//pBlockToCheck=pBlockToCheck.substring(0,lReplaceMatcher.start(1))+pReplacementText+pBlockToCheck.substring(lReplaceMatcher.start(1),pBlockToCheck.length());
					}
					 					
				}
			}else if(pGroupCount == -5 ){
				start=lMatcher.start()+shiftValue;
				end=lMatcher.end()+shiftValue;	
				
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
									if("IDENTIFY_PATTERN".equalsIgnoreCase(lIdentifyOrReplaceMode)){
										pReplacementText = addColor(pBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());//for adding font color green
									}else{
										if(!(pReplacementPattern==null || "".equals(pReplacementPattern) || "".equals(pReplacementText) || pReplacementText==null )){
											pReplacementText=pReplacementText.replaceAll("\\s*_DBT_VAR_RPL_\\s*",lTextFromRgx);
											lFindStr=lFindStr.substring(0,lReplaceMatcher.start(lReplaceGroupCount))+pReplacementText+lFindStr.substring(lReplaceMatcher.end(lReplaceGroupCount),lFindStr.length());
											//System.out.println("lFindStr::::->"+lFindStr);
											pReplacementText=lFindStr;
											//pOldText = pBlockToCheck.substring(start,end) ;  //For Tracker  //For Tracker
					        				pNewText=pReplacementText; //For Tracker
											pReplacementText = addColor(pReplacementText,pPostProcChangePatDTO.getFindDesc());
										}else{
											pReplacementTextCopy = addColor(pIdentifyBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());
											pIdentifyBlockToCheck=pIdentifyBlockToCheck.substring(0,start)+pReplacementTextCopy+pIdentifyBlockToCheck.substring(end,pIdentifyBlockToCheck.length());
										}
									}	
									isInsertRequired = true;
				 					//pBlockToCheck=pBlockToCheck.substring(0,lReplaceMatcher.start(1))+pReplacementText+pBlockToCheck.substring(lReplaceMatcher.end(1)-1,pBlockToCheck.length());									
									//for adding font color green 
									pBlockToCheck=pBlockToCheck.substring(0,start)+pReplacementText+pBlockToCheck.substring(end,pBlockToCheck.length());								
								
							}
						}else{
							System.out.println("Variable Not Found in Declare Var HashMap::pdataTypeToChk->"+pdataTypeToChk+":::-:::"+lTextFromRgx.trim());
						}
						
					}else{
						if(pReplaceComplete){
							lReplaceGroupCount=0; // here making group count so v can replace complete text
						}
						if("IDENTIFY_PATTERN".equalsIgnoreCase(lIdentifyOrReplaceMode)){
							pReplacementText = addColor(pBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());//for adding font color green
						}else{
							if(!(pReplacementPattern==null || "".equals(pReplacementPattern) || "".equals(pReplacementText) || pReplacementText==null )){
								pReplacementText=pReplacementText.replaceAll("\\s*_DBT_VAR_RPL_\\s*",lTextFromRgx);
								lFindStr=lFindStr.substring(0,lReplaceMatcher.start(lReplaceGroupCount))+pReplacementText+lFindStr.substring(lReplaceMatcher.end(lReplaceGroupCount),lFindStr.length());
				 				//pBlockToCheck=pBlockToCheck.substring(0,lReplaceMatcher.start(1))+pReplacementText+pBlockToCheck.substring(lReplaceMatcher.end(1)-1,pBlockToCheck.length());
								pReplacementText=lFindStr;
								//for adding font color green 
								//pOldText = pBlockToCheck.substring(start,end) ;  //For Tracker  //For Tracker
		        				pNewText=pReplacementText; //For Tracker
								pReplacementText = addColor(pReplacementText,pPostProcChangePatDTO.getFindDesc());							
							}else{
								pReplacementTextCopy = addColor(pIdentifyBlockToCheck.substring(start,end),pPostProcChangePatDTO.getFindDesc());
								pIdentifyBlockToCheck=pIdentifyBlockToCheck.substring(0,start)+pReplacementTextCopy+pIdentifyBlockToCheck.substring(end,pIdentifyBlockToCheck.length());
							}
						}
						isInsertRequired = true;
						pBlockToCheck=pBlockToCheck.substring(0,start)+pReplacementText+pBlockToCheck.substring(end,pBlockToCheck.length());
						
						
					}
					 					
				}
			}
			//System.out.println(":::::::after:::"+pBlockToCheck);
		
    	//lMatcher=lStrFunctionPattern.matcher(pBlockToCheck);
		
		if("IDENTIFY_PATTERN".equalsIgnoreCase(lIdentifyOrReplaceMode)){
			if(isInsertRequired == true){
				prepareInsertForChangeTracker(pOldText,"","000",pBlockToCheck,"");
			}
		}else if(!"".equals(pNewText)){
				prepareInsertForChangeTracker(pOldText,pNewText,"000",ToolsUtil.removeHtmlTags(pIntialBlockToCheck),ToolsUtil.removeHtmlTags(pBlockToCheck)); // Insert Changes occured For Tracker.
							
		}else{
			if(pReplacementPattern==null || "".equals(pReplacementPattern) || "".equals(pReplacementText) || pReplacementText==null ){			
				prepareInsertForChangeTracker(pOldText,pNewText,"000",pIdentifyBlockToCheck,""); // Insert the things which r identified but not changed For Tracker.			
			}
		}
		pReplacementText=lTempReplacement;
	 }
	
	
  return pBlockToCheck;

}
	
	public void prepareInsertForChangeTracker(String pOldText,String pNewText,String pBlockNum,String pBeforeBlock,String pAfterBlock){
		PreparedStatement lPreparedStatement=null;
		ResultSet lResultSet =null;
		
		try{
			pBeforeBlock=pBeforeBlock.replaceAll("_DBT_NEW_LINE_", "\n");
			pAfterBlock=pAfterBlock.replaceAll("_DBT_NEW_LINE_", "\n");
			
			
			pBeforeBlock = pBeforeBlock.replaceAll("_DBT_SPACE_", " ");
			pAfterBlock = pAfterBlock.replaceAll("_DBT_SPACE_", " ");
			
			pBeforeBlock = pBeforeBlock.replaceAll("\\n", "<BR/>");
			pAfterBlock = pAfterBlock.replaceAll("\\n", "<BR/>");
			
			pBeforeBlock = removeToolChars(pBeforeBlock);
			pAfterBlock = removeToolChars(pAfterBlock);
			
			String[] lBlockArr = TextComparison.compareTwoStrings(pBeforeBlock,pAfterBlock);
			pBeforeBlock = lBlockArr[0];
			pAfterBlock = lBlockArr[1];
			
			//, PROCEDURE_NAME, LINE_N0, BLOCK_N0, OLD_TEXT, NEW_TEXT, CODE_TYPE, CREATED_BY, CREATED_DATE
			lPreparedStatement=lConnection.prepareStatement(WebConstant.INSERT_POST_PROCESSOR_CHANGES_TRACKER_DETAILS);
			lPreparedStatement.setString(1,lProjectId);
			lPreparedStatement.setString(2,lCompleteProcName);
			lPreparedStatement.setString(3,pBlockNum);
			lPreparedStatement.setString(4,pBlockNum);
			lPreparedStatement.setString(5,pOldText);
			lPreparedStatement.setString(6,pNewText);
			lPreparedStatement.setString(7,"SP");
			lPreparedStatement.setString(8,ToolConstant.CREATED_BY);
			lPreparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
			lPreparedStatement.setString(10,pBeforeBlock);
			lPreparedStatement.setString(11,pAfterBlock);
			lPreparedStatement.executeUpdate();
			if(lConnection.getAutoCommit()==false){
				lConnection.commit();
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			//DBConnectionManager.closeConnection(lPreparedStatement);
		}
		return ;
	}
	
	public void deleteForChangeTracker(){
		PreparedStatement lPreparedStatement=null;
		ResultSet lResultSet =null;
		
		try{
			//, PROCEDURE_NAME, LINE_N0, BLOCK_N0, OLD_TEXT, NEW_TEXT, CODE_TYPE, CREATED_BY, CREATED_DATE
			lPreparedStatement=lConnection.prepareStatement(WebConstant.DELETE_POST_PROCESSOR_CHANGES_TRACKER_DETAILS);
			lPreparedStatement.setString(1,lProjectId);
			lPreparedStatement.setString(2,lCompleteProcName);
			int lInsertCount = lPreparedStatement.executeUpdate();
			if(lConnection.getAutoCommit()==false){
				lConnection.commit();
			}
			logger.info(":::::deleteForChangeTracker::::"+lInsertCount);
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			//DBConnectionManager.closeConnection(lPreparedStatement);
		}
		return ;
	}
	
	
	public List likeOperatorChk(List lBlockStrList,String pProcName){
		Pattern likePattern1=Pattern.compile("(?i)\\bWHERE\\b[\\s\\r\\n\\w\\W\\S]+\\bLIKE\\s*\\'\\%\\[\\^0-9\\]\\%\\'");
		Pattern likePattern2=Pattern.compile("(?i)\\bWHERE\\b[\\s\\r\\n\\w\\W\\S]+\\bLIKE\\s*\\'\\%\\[0-9\\]\\%\\'");
		Pattern likePattern3=Pattern.compile("(?i)\\bWHERE\\b[\\s\\r\\n\\w\\W\\S]+\\bNOT\\b[\\s\\r\\n]+\\bLIKE\\s*\\'\\[0-9\\]\\[0-9\\]\\[0-9\\]\\[0-9\\]\\[0-9\\]\\[0-9\\]\\[0-9\\]\\[0-9\\]\\[0-9\\]\\'");
		Pattern likePattern4=Pattern.compile("(?i)\\bWHERE\\b[\\s\\r\\n\\w\\W\\S]+\\bNOT\\b[\\s\\r\\n]+\\bLIKE\\s*\\'\\%\\[a-z\\]\\%\\[a-z\\]\\%\\'");
		Matcher likeOpMatch1=null;
		Matcher likeOpMatch2=null;
		Matcher likeOpMatch3=null;
		Matcher likeOpMatch4=null;
		String pBlockStr="";
		for(int i=0;i<lBlockStrList.size(); i++){
			pBlockStr=(String)lBlockStrList.get(i);
			likeOpMatch1=likePattern1.matcher(pBlockStr);
			likeOpMatch2=likePattern2.matcher(pBlockStr);
			likeOpMatch3=likePattern3.matcher(pBlockStr);
			likeOpMatch4=likePattern4.matcher(pBlockStr);
			if(likeOpMatch1.find()||likeOpMatch3.find()){
				pBlockStr=pBlockStr.replaceAll("(?i)\\bWHERE\\b", " WHERE FN_ISNUMBER(");
				pBlockStr=pBlockStr.replaceAll("(?i)\\bLIKE\\s*\\'\\%\\[\\^0-9\\]\\%\\'", ") = 1");
				pBlockStr=pBlockStr.replaceAll("(?i)\\bLIKE\\s*\\'\\[0-9\\]\\[0-9\\]\\[0-9\\]\\[0-9\\]\\[0-9\\]\\[0-9\\]\\[0-9\\]\\[0-9\\]\\[0-9\\]\\'", ") = 1");
			}
			if(likeOpMatch2.find()){
				pBlockStr=pBlockStr.replaceAll("(?i)\\bWHERE\\b", " WHERE FN_ISNUMBER(");
				pBlockStr=pBlockStr.replaceAll("(?i)\\bLIKE\\s*\\'\\%\\[0-9]\\%\\'", ") = 0");
			}
			if(likeOpMatch4.find()){
				
				String sub=pBlockStr.substring(pBlockStr.toUpperCase().indexOf("WHERE")+6,pBlockStr.toUpperCase().indexOf("NOT"));
				
				
				pBlockStr=pBlockStr.replaceAll("(?i)\\bwhere\\s*\\b"+sub+"\\s*\\bNOT\\b[\\s\\r\\n]+\\bLIKE\\s*\\'\\%\\[a-z\\]\\%\\[a-z\\]\\%\\'\\s*\\;", "where length("+sub.trim()+") - length(replace(translate(lower("+sub.trim()+"),'','abcdefghijklmnopqrstuvwxyz'),' ','')) < 2 ;");
				 
			
				//System.out.println(pBlockStr);
				//System.out.println(sub);
			}
			lBlockStrList.set(i, pBlockStr);
		}
		return lBlockStrList;
	}

	
	
	/*
	public List replPattern(List lBlockStrList,String pProcName){
		Pattern likePatternChk=Pattern.compile("(?i)\\bWHERE\\b[\\s\\r\\n\\w\\W\\S]+\\bLIKE\\s*\\'\\%\\[\\^0-9\\]\\%\\'");
		Pattern likeKeyword=Pattern.compile("(?i)\\bLIKE\\b");
		Pattern andKeyword=Pattern.compile("(?i)\\bAND\\b");
		Pattern orKeyword=Pattern.compile("(?i)\\bOR\\b");
		Pattern whereKeyword=Pattern.compile("(?i)\\bWHERE\\b");
		String pBlockStr="";
		int indx1=0;
		int indxR=0;
		int indxL=0;
		String pStr="";
		String pStr2="";
		Matcher mKeyword=null;
		Matcher mKeywordor=null;
		Matcher mKeywordand=null;
		Matcher mKeywordwhere=null;
		Matcher m=null;
		for (int i = 0; i < lBlockStrList.size(); i++) {
			pBlockStr=(String)lBlockStrList.get(i);
			mKeyword=likeKeyword.matcher(pBlockStr);
			if(mKeyword.find()){
				indx1=i;
				
				for (int j = indx1; j <lBlockStrList.size(); j++)
				{
					pStr+=(String)lBlockStrList.get(j);
					mKeywordand=andKeyword.matcher(pStr);
					mKeywordor=orKeyword.matcher(pStr);
					if(mKeywordand.find()||mKeywordor.find())
					{	
						indxR=j;
						break;
					}
					
				}
				for (int j = lBlockStrList.size()-1; j >indx1; j--)
				{
					pStr2+=(String)lBlockStrList.get(j);
					mKeywordand=andKeyword.matcher(pStr2);
					mKeywordor=orKeyword.matcher(pStr2);
					mKeywordwhere=whereKeyword.matcher(pStr2);
					if(mKeywordand.find()||mKeywordor.find()||mKeywordwhere.find())
					{	
						indxL=j;
						break;
					}
					
				}
			}
			
			m=likePatternChk.matcher(pBlockStr);
			if(m.find()){
				for(int c=0;c<indxL;c++)
				{
					if(pStr.contains("WHERE\\b[\\s\\r\\n\\w\\W\\S]"))
					{
						pStr="WHERE FN_ISNUMBER(";
					}
				}	
			
			for(int c=indxR;c<lBlockStrList.size();c++)
			{
				if(pStr2.contains("\\bLIKE\\s*\\'\\%\\[\\^0-9\\]\\%\\'"))
				{
					pStr2=")=1";
				}
			}
			pBlockStr=pStr+pBlockStr+pStr2;
			lBlockStrList.set(i, pBlockStr);
			}
		}
		return lBlockStrList;
		
	}
	
	*/
	
	
	public List checkCursorWithHold(List lBlockStrList,String pProcName){
		 Pattern lDeclareCursorPattern = Pattern.compile("(?i)\\bDECLARE(?i)CURSOR\\b");
		 Pattern lDeclareCursorWithHoldPattern = Pattern.compile("(?i)\\bDECLARE\\b[\\s\\r\\n\\w\\W\\S]+\\bCURSOR\\b[\\s\\r\\n]+\\bWITH\\b[\\s\\r\\n]+\\bHOLD\\b");
		 Matcher lMatcher=null;
		 String pBlockStr="";
		for (int i = 0; i < lBlockStrList.size(); i++) {
			pBlockStr=(String)lBlockStrList.get(i);
			lMatcher=lDeclareCursorWithHoldPattern.matcher(pBlockStr);
			if(lMatcher.find()){
				//System.out.println(":::::;;;with hold aready present:::::");
			}else{
			 lMatcher=lDeclareCursorPattern.matcher(pBlockStr);
	   		if(lMatcher.find()){
	   			pBlockStr = pBlockStr.replaceAll("(?i)\\bCURSOR\\b", " CURSOR WITH HOLD /* dbTransplant Change Comment- WITH HOLD added */");
	   			//System.out.println(":::::::::::::::::::declare pattern found::::::::::"+pBlockStr);
	   		}
			}
			lBlockStrList.set(i, pBlockStr);
		}
			
		
       return lBlockStrList;
	}
	public List changeUpdateStatistics(List lBlockStrList,String pProcName){
		Pattern lUpdateStatsPattern=Pattern.compile("(?i)\\bUPDATE\\b[\\s\\r\\n]+\\bSTATISTICS\\b[\\s\\r\\n]+");
		Pattern lUpdateStatsPattern1=Pattern.compile("(?i)\\bUPDATE\\b[\\s\\r\\n]+\\bSTATISTICS\\b[\\s\\r\\n]+(.+?)[\\s\\r\\n]*;");
		String lBlockStr="";
		String lTabName="";
		String lStatementToAdd="";
		//CALL SYSPROC.ADMIN_CMD('RUNSTATS on Table
		//SESSION.<tablename> with distribution and indexes all allow read access'); -69
		for (int i = 0; i < lBlockStrList.size(); i++) {
			lBlockStr=(String)lBlockStrList.get(i);
			if(lUpdateStatsPattern.matcher(lBlockStr).find()){
				lTabName=lBlockStr.substring(lBlockStr.indexOf(" STATISTICS ")+10,lBlockStr.length()).trim();				
				lStatementToAdd="CALL SYSPROC.ADMIN_CMD('RUNSTATS on Table "+lTabName
				+" with distribution and indexes all allow read access');  /* dbTransplant Change Comment- to handle UPDATE STATISTICS */";
				lBlockStrList.set(i,lStatementToAdd);
			}
			
		}
		return lBlockStrList;
	}
	public List addVarAfterDeclareNChangeVar(List lBlockStrList,String pProcName,String pOldVarName,String pNewVarName,List pToAddStatemenstList){
		if("IDENTIFY_PATTERN".equalsIgnoreCase(lIdentifyOrReplaceMode)){
			return lBlockStrList;
		}
		int lDeclareEndIdx=identifyDeclareBlocks( lBlockStrList, pProcName);
		String lStatementToAdd="";
		System.out.println("lDeclareEndIdx:::+"+lDeclareEndIdx);
		//Manually modify the code as    -18                
		//SELECT INST_NAME into V_severname FROM SYSIBMADM.ENV_INST_INFO;
		try {
			for (int i = 0; i < pToAddStatemenstList.size(); i++) {				
				lStatementToAdd=(String)pToAddStatemenstList.get(i);
				lStatementToAdd = addColor(lStatementToAdd,"--DBT Variable Declaration");				
				lBlockStrList.add(lDeclareEndIdx,lStatementToAdd);
				
			}
			
			String lBlockStr="";
			if(pOldVarName!=null && !"".equals(pOldVarName.trim())){
				for (int i = lDeclareEndIdx; i < lBlockStrList.size(); i++) {
					lBlockStr=(String)lBlockStrList.get(i);
					lBlockStr=lBlockStr.replaceAll("(?i)"+pOldVarName+"\\b",addColor(pNewVarName,pOldVarName+" Replaced by "+pNewVarName));
					lBlockStrList.set(i,lBlockStr);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*int lDeclareEndIdx=identifyDeclareBlocks( lBlockStrList, pProcName);
		String lStatementToAdd="";
		//Manually modify the code as    -18                
		//SELECT INST_NAME into V_severname FROM SYSIBMADM.ENV_INST_INFO;
		try {
			lStatementToAdd="SELECT INST_NAME into DBTV_severname FROM SYSIBMADM.ENV_INST_INFO;";
			lBlockStrList.add(lDeclareEndIdx,lStatementToAdd);
			lStatementToAdd="DECLARE DBTV_severname VARCHAR(50);";
			lBlockStrList.add(lDeclareEndIdx,lStatementToAdd);
			String lBlockStr="";
			for (int i = 0; i < lBlockStrList.size(); i++) {
				lBlockStr=(String)lBlockStrList.get(i);
				lBlockStr=lBlockStr.replaceAll("(?i)@@ServerName\\b","DBTV_severname");
				lBlockStrList.set(i,lBlockStr);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return lBlockStrList;
	}
	
	public int identifyDeclareBlocks(List lBlockStrList,String pProcName){
		
		Pattern lCreateProcPattern=Pattern.compile("(?i)\\bCREATE\\b[\\s\\r\\n]+(\\bOR\\b[\\s\\r\\n]+\\bREPLACE\\b[\\s\\r\\n]+)*(\\bPROC\\b|\\bPROCEDURE\\b)");
		Pattern lProcBeginPattern=Pattern.compile("(?i)\\bBEGIN\\b");
		Pattern lDeclarePattern=Pattern.compile("(?i)\\bDECLARE\\b[\\s\\r\\n]+");
		boolean lCreateProcVisited=false;
		boolean lProcBeginVisited=false;
		boolean lDeclareVisited=false;
		String lChkStr="";
		int lDeclareEndIdx=0;
		
		try {
			for (int i = 0; i < lBlockStrList.size(); i++) {
				lChkStr=(String)lBlockStrList.get(i);
				if(lCreateProcPattern.matcher(lChkStr).find()){
					lCreateProcVisited=true;
				}
				if(lCreateProcVisited==true && lProcBeginPattern.matcher(lChkStr).find()){
					lProcBeginVisited=true;
				}
				
				if(lProcBeginVisited==true && lDeclarePattern.matcher(lChkStr.trim()).find()){
					lDeclareVisited=true;
				}
				if(lDeclareVisited==true && (!lDeclarePattern.matcher(lChkStr.trim()).find())){
					System.out.println("Declare End Found at index::"+i);
					//System.out.println("lChkStr::->"+lChkStr);
					//System.out.println("i-1::->"+lBlockStrList.get(i-1));
					lDeclareEndIdx=i;
					
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lDeclareEndIdx;
		
	}
	public HashMap getAllDeclareVarOfSp(List lBlockStrList,String pProcName){
		//Pattern lDeclarePattern = Pattern.compile("(?i)^DECLARE\\b\\s*((?!\\bCURSOR\\b).)*$");
		Pattern lDeclarePattern = Pattern.compile("(?i)\\bDECLARE\\b\\s+");
		Pattern lNotDeclareVarPattern = Pattern.compile("(?i)(\\bDECLARE\\b\\s*\\bCONTINUE\\b\\s*\\bHANDLER\\b|\\bDECLARE\\b\\s*\\bGLOBAL\\b\\s*\\bTEMPORARY\\b\\s*\\bTABLE\\b|\\bDECLARE\\b[\\s\\W\\w\\r\\n]+\\bCURSOR\\b)");
		Matcher matcher=null;
		String lBlockStr="";		
		for (int i = 0; i < lBlockStrList.size(); i++) {
			
			lBlockStr=lBlockStr.replaceAll("\\(", " ( ");
			lBlockStr=lBlockStr.replaceAll("\\)", " ) ");
			lBlockStr =(String)lBlockStrList.get(i); 
			//to capture declare statements and create a hash map with -> KEY: variable name , VALUE: variable type
			matcher=lDeclarePattern.matcher(lBlockStr);
			if( matcher.find() && (!lNotDeclareVarPattern.matcher(lBlockStr).find())){
				//lBlockStr=lBlockStr.trim();				
			//	System.out.println("--Inside---"+lBlockStr);				
				//String[] lVarArr=lBlockStr.substring(lBlockStr.toUpperCase().trim().indexOf("DECLARE ")+8,lBlockStr.length()).trim().split("\\s+");
				String[] lVarArr=lBlockStr.substring(matcher.end(),lBlockStr.length()).trim().split("\\s+");
				//System.out.println(lBlockStr.substring(matcher.end(),lBlockStr.length()).trim().split("\\s+"));
				//System.out.println("lVarArr:::"+lVarArr.toString());
				//System.out.println("----------");				
				//System.out.println("Arrays:::->"+lVarArr[0]+":::::"+lVarArr[1]);
				//System.out.println("--------");
				lVariableDataTypeMap.put(lVarArr[0].trim().toUpperCase(), lVarArr[1]);
			}
		}
		//System.out.println("-----------lVariableDataTypeMap-----------------"+lDeclareVariableDataTypeMap.size());
		Set entries= lVariableDataTypeMap.entrySet();
		Iterator it=entries.iterator();
		while(it.hasNext()){
			Entry entry =(Entry)it.next();
			System.out.println(entry.getKey()+" --- <<<>>> --- "+entry.getValue());
		}
		
		
		return lVariableDataTypeMap;
	}
	public  List chkInSourceNReplace(List lBlockStrList,String pPatternToChk,PostProcChangePatDTO pPostProcChangePatDTO){
		System.out.println("::: Method Called:::::chkInSourceNReplace");
		System.out.println("pPatternToChk::->"+pPatternToChk);
		Pattern pChkPattern=Pattern.compile(pPatternToChk);
		Matcher pChkPatMatcher=null;
		String lSourceStr="";
		int lTargetLineNum=0;
		for (int i = 0; i < lSourceBlockStrList.size(); i++) {
			lSourceStr=(String)lSourceBlockStrList.get(i);
			//System.out.println(lSourceStr);
			if(pChkPattern.matcher(lSourceStr).find()){
				lTargetLineNum=	getTargetLineNumFromCompareFromedStmts((i+1)+"");
				System.out.println("i::->"+i+":::lTargetLineNum::->"+lTargetLineNum);
				
				lBlockStrList=checkNReplaceForLines(lBlockStrList,lTargetLineNum,pPostProcChangePatDTO);
			}
			/*lTargetLineNum=	getTargetLineNumFromCompareFromedStmts(pProcName,(i+1)+"");
			System.out.println("i::->"+i+":::lTargetLineNum::->"+lTargetLineNum);*/
			
			
		}
		return lBlockStrList;
	}
	public int getTargetLineNumFromCompareFromedStmts(String pSourceLineNum){
		String lTargetLineNum="0";
		Connection lConnection=null;
		PreparedStatement lPreparedStatement=null;
		ResultSet lResultSet=null;
		try {
			lConnection=DBConnectionManager.getConnection();
			lPreparedStatement=lConnection.prepareStatement(ToolConstant.GET_TARGET_LINE_NUM);
			lPreparedStatement.setString(1, lProjectId);
			lPreparedStatement.setString(2, lCompleteProcName);
			lPreparedStatement.setString(3, pSourceLineNum);
			System.out.println("lPreparedStatement::->"+lPreparedStatement);
			lResultSet=lPreparedStatement.executeQuery();
			if(lResultSet!=null){
				while(lResultSet.next()){
					lTargetLineNum=lResultSet.getString("TARGET_STATEMENT_NO");
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(lConnection);
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
		}
		return Integer.parseInt(lTargetLineNum);
	}
	public List checkNReplaceForLines(List lBlockStrList,int pTargetLineNum,PostProcChangePatDTO lPostProcChangePatDTO){		
		PostProcessFileUpload lPostProcessFileUpload=new PostProcessFileUpload();
		List lLineStrList=lPostProcessFileUpload.getLinesFromBlocks(lBlockStrList);
		/*System.out.println("::::::::::<<<Lines List>>>>::::::::::::");
		for (int i = 0; i < lLineStrList.size(); i++) {
			System.out.println(":::i::->"+i+":::::"+lLineStrList.get(i));
		}*/
		String lBlockString="";
		String lLineString="";
		
		for (int i = pTargetLineNum; i < lLineStrList.size(); i++) {			
			lLineString=(String)lLineStrList.get(i);
			//System.out.println(i+":::<<lLineString>>->"+lLineString);
			if(!"".equals(lBlockString.trim())){
				lBlockString=lBlockString+"\n";
			}
			lBlockString=lBlockString+lLineString;
			lLineStrList.set(i,"");
			if(lLineString.trim().endsWith(";")){
				break;
			}
			
			
		}
		//System.out.println("lBlockString target Str::->"+lBlockString);
		//\\bif\\b[\\s\\r\\n]*(\\()*[\\s\\r\\n]*transtate[\\d]*[\\s\\r\\n]*=[\\s\\r\\n]*[\\d]+
		/*String pFindPattern="(?i)\\bif\\b[\\s\\r\\n]*(\\()*[\\s\\r\\n]*v_transtate[\\d]*[\\s\\r\\n]*=[\\s\\r\\n]*[\\d]+";
		String pReplacementText=" IF (substr(l_error,1,2)='23') ";
		// if v_transtate = 2
		//IF (substr(l_error,1,2)='23') 
		PostProcChangePatDTO lPostProcChangePatDTO=new PostProcChangePatDTO();
		lPostProcChangePatDTO.setFindPattern(pFindPattern);
		lPostProcChangePatDTO.setReplacementPattern(pFindPattern);
		lPostProcChangePatDTO.setReplacementText(pReplacementText);
		lPostProcChangePatDTO.setGroupCount(0);
		lPostProcChangePatDTO.setDataTypeToChk("");*/
		//System.out.println(":: Before::lBlockString->"+lBlockString);
		lBlockString=checkAndReplace(lBlockString, lPostProcChangePatDTO);
		//System.out.println(":: After::lBlockString->"+lBlockString);
		lLineStrList.set(pTargetLineNum, lBlockString);		
		lBlockStrList=lPostProcessFileUpload.getBlocksFromLines(lLineStrList);
		
		return lBlockStrList;
	}
	public  List identifyStatementNChange(List lBlockStrList,String pProcName){
		String lBlockStr="";
		String pFindDesc="";
		for (int i = 0; i < lBlockStrList.size(); i++) {
			lBlockStr=(String)lBlockStrList.get(i);
			//-----------------------------
			PostProcChangePatDTO lPostProcChangePatDTO=new PostProcChangePatDTO();			
			String pFindPattern ="CAST"+RGX.SRN+RGX.ZERO_OR_MANY+"\\("+RGX.TEXT+RGX.ONE_OR_MANY+RGX.B_STR("AS")+RGX.SRN+RGX.ONE_OR_MANY+RGX.L_STR("TIMESTAMP");
			String pReplacementPattern = "(?i)\\bCAST\\b[\\s\\r\\n]*\\([\\s\\r\\n]*(.+?)\\bAS\\b[\\s\\r\\n]+\\bTIMESTAMP";
			String pReplacementText="to_date(_DBT_VAR_RPL_,'YYYYMMDD')";
			int pGroupCount = -5;
			String pdataTypeToChk="VARCHAR";
			lPostProcChangePatDTO.setFindPattern(pFindPattern);
			lPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
			lPostProcChangePatDTO.setReplacementText(pReplacementText);
			lPostProcChangePatDTO.setGroupCount(pGroupCount);
			lPostProcChangePatDTO.setDataTypeToChk(pdataTypeToChk);
			lPostProcChangePatDTO.setFindDesc(pFindDesc);
			//Identify Statement Here..
			lBlockStr=identifyStatement(lBlockStr,"CAST",lPostProcChangePatDTO);
			//-----------------------------
			lPostProcChangePatDTO=new PostProcChangePatDTO();			
			pFindPattern ="(?i)REPLACE[\\s\\r\\n]*\\([\\s\\r\\n\\w\\W]+,[\\s\\r\\n\\w\\W]+,[\\s\\r\\n\\w\\W]+\\)";
			pReplacementPattern = "(?i)NULLIF[\\s\\r\\n]*\\([\\s\\r\\n]*0[\\s\\r\\n]*,[\\s\\r\\n]*0[\\s\\r\\n]*\\)";
			pReplacementText="''";
			pGroupCount =-5;
			pdataTypeToChk="";
			lPostProcChangePatDTO.setFindPattern(pFindPattern);
			lPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
			lPostProcChangePatDTO.setReplacementText(pReplacementText);
			lPostProcChangePatDTO.setGroupCount(pGroupCount);
			lPostProcChangePatDTO.setDataTypeToChk(pdataTypeToChk);	
			lPostProcChangePatDTO.setFindDesc(pFindDesc);
			lBlockStr=identifyStatement(lBlockStr,"REPLACE",lPostProcChangePatDTO);
			//-----------------------------
			pFindPattern ="(?i)\\bCAST\\b[\\s\\r\\n]*\\([\\s\\r\\n]*\\bCHAR\\b\\((.+?)\\)[\\s\\r\\n]*\\bAS\\b[\\s\\r\\n]+\\bVARCHAR\\b[\\s\\r\\n]*\\([\\d]+[\\s\\r\\n]*\\)[\\s\\r\\n]*\\)";
			pReplacementPattern = pFindPattern;
			pReplacementText="FN_DECIMALTOCHAR( _DBT_VAR_RPL_ )";
			pGroupCount = -5;
			pdataTypeToChk="DECIMAL";
			boolean pReplaceComplete=true;
			boolean pchkTextType=true;			
			lPostProcChangePatDTO =new PostProcChangePatDTO();
			lPostProcChangePatDTO.setFindPattern(pFindPattern);
			lPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
			lPostProcChangePatDTO.setReplacementText(pReplacementText);
			lPostProcChangePatDTO.setGroupCount(pGroupCount);
			lPostProcChangePatDTO.setDataTypeToChk(pdataTypeToChk);		
			lPostProcChangePatDTO.setReplaceComplete(pReplaceComplete);
			lPostProcChangePatDTO.setChkTextType(pchkTextType);
			lPostProcChangePatDTO.setFindDesc(pFindDesc);
			lBlockStr=identifyStatement(lBlockStr,"CAST",lPostProcChangePatDTO);
			
			
			
			//-----------------------------
			pFindPattern ="(?i)\\bCAST\\b[\\s\\r\\n]*\\([\\s\\r\\n]*\\bCURRENT\\b[\\s\\r\\n]+\\bTIMESTAMP\\b[\\s\\r\\n]+\\bAS\\b[\\s\\r\\n]+\\bVARCHAR\\b[\\s\\r\\n]*\\([\\d]+[\\s\\r\\n]*\\)[\\s\\r\\n]*\\)";
			pReplacementPattern = pFindPattern;
			pReplacementText="TO_CHAR(CURRENT TIMESTAMP ,'mm/dd/yy')";
			pGroupCount = -5;
			pdataTypeToChk="VARCHAR";
			pReplaceComplete=true;
			pchkTextType=true;			
			lPostProcChangePatDTO =new PostProcChangePatDTO();
			lPostProcChangePatDTO.setFindPattern(pFindPattern);
			lPostProcChangePatDTO.setReplacementPattern(pReplacementPattern);
			lPostProcChangePatDTO.setReplacementText(pReplacementText);
			lPostProcChangePatDTO.setGroupCount(pGroupCount);
			lPostProcChangePatDTO.setDataTypeToChk(pdataTypeToChk);		
			lPostProcChangePatDTO.setReplaceComplete(pReplaceComplete);
			lPostProcChangePatDTO.setChkTextType(pchkTextType);
			lPostProcChangePatDTO.setFindDesc(pFindDesc);
			lBlockStr=identifyStatement(lBlockStr,"CAST",lPostProcChangePatDTO);
			
			
			lBlockStrList.set(i, lBlockStr);
		}
		return lBlockStrList;
	}
	public String identifyStatement(String pBlockStr,String pchkStr,PostProcChangePatDTO pPostProcChangePatDTO){
		//System.out.println("Before:::"+pBlockStr);
		pBlockStr=pBlockStr.replaceAll("\\(", " sp_( ");
		pBlockStr=pBlockStr.replaceAll("\\)", " sp_) ");
		pBlockStr=pBlockStr.replaceAll("\\n", " _DBT_NEW_LINE_ ");
		Pattern lChkPattern = Pattern.compile("(?i)\\b"+pchkStr.trim()+"\\b[\\s\\r\\n]*sp_\\(");
		Matcher lChkPatMatcher=lChkPattern.matcher(pBlockStr);
		String [] lStmtArr=null;
		int lopenBraceCount=0;
		boolean lopenBraceFound=false;
		String lSubStmtStr="";
		String lOtherStr="";
		
		if(lChkPatMatcher.find()){
			
			lStmtArr=pBlockStr.substring(pBlockStr.toUpperCase().lastIndexOf(pchkStr.trim()+" "),pBlockStr.length()).split("\\s");			
			for (int i = 0; i < lStmtArr.length; i++) {
				if(lStmtArr[i].trim().equals("sp_(")){
					lopenBraceCount++;
					lopenBraceFound=true;
				}
				if(lStmtArr[i].trim().equals("sp_)")){
					lopenBraceCount--;
				}
				lSubStmtStr=lSubStmtStr+" "+lStmtArr[i];
				if(lopenBraceFound==true && lopenBraceCount==0){
					lSubStmtStr=lSubStmtStr+" ";
					for (int j = i+1; j < lStmtArr.length; j++) {
						//System.out.println("::lStmtArr[j]:::::--"+lStmtArr[j]+"--");
						lOtherStr=lOtherStr+" "+lStmtArr[j];
					}
					break;
				}
				
			}
			lSubStmtStr=lSubStmtStr.replaceAll("\\ssp_\\(\\s", "(");
			lSubStmtStr=lSubStmtStr.replaceAll("\\ssp_\\)\\s", ")");
			
			lOtherStr=lOtherStr.replaceAll("\\ssp_\\(\\s", "(");
			lOtherStr=lOtherStr.replaceAll("\\ssp_\\)\\s", ")");
			
			//System.out.println("lCastStmtStr ::->"+lCastStmtStr);
			
			//Call check and Replace Methods Here to modify cast statements.
			//CAST(V_inString AS TIMESTAMP);
					
			String pFindPattern =pPostProcChangePatDTO.getFindPattern();
			String pReplacementPattern = pPostProcChangePatDTO.getReplacementPattern();
			String pReplacementText=pPostProcChangePatDTO.getReplacementText();
			int pGroupCount = pPostProcChangePatDTO.getGroupCount();
			String pdataTypeToChk=pPostProcChangePatDTO.getDataTypeToChk();			
			
			lSubStmtStr=checkAndReplace(lSubStmtStr,pPostProcChangePatDTO);			
			//------------------------
			///////////////////////////////////
			
			lSubStmtStr=lSubStmtStr.replaceFirst("(?i)\\b"+pchkStr.trim()+"\\b", "DBT_MOD_T"+pchkStr.trim()+"_");
			pBlockStr=pBlockStr.substring(0,pBlockStr.toUpperCase().lastIndexOf(pchkStr.trim()+" ")-1)+lSubStmtStr+lOtherStr;		
			//System.out.println("lCastStmtStr ::->"+lCastStmtStr);
		}
		
		
		pBlockStr=pBlockStr.replaceAll("\\ssp_\\(\\s", "(");
		pBlockStr=pBlockStr.replaceAll("\\ssp_\\)\\s", ")");
		
		//DBT_MOD_TCAST_(
		//System.out.println("pBlockStr ::->"+pBlockStr);
		
		lChkPattern = Pattern.compile("(?i)\\b"+pchkStr.trim()+"\\b[\\s\\r\\n]*\\(");
		if(lChkPattern.matcher(pBlockStr).find()){
			//System.out.println(":::: One More Found::::::::::");
			pBlockStr=identifyStatement(pBlockStr,pchkStr,pPostProcChangePatDTO);
		}
		
		pBlockStr=pBlockStr.replaceAll("DBT_MOD_T"+pchkStr.trim()+"_", pchkStr);
		pBlockStr=pBlockStr.replaceAll("_DBT_NEW_LINE_", "\n");
		
		return pBlockStr;
	}
	/*public List patternChange(List lBlockStrList,String keyword){
		String pBlockStr="";
		for (int i=0;i<lBlockStrList.size();i++){
			pBlockStr=(String)lBlockStrList.get(i);
		}
		return lBlockStrList;
	}*/
	public List handleResursiveCallStatement(List lBlockStrList,String pProcName){
		
		if("IDENTIFY_PATTERN".equalsIgnoreCase(lIdentifyOrReplaceMode)){
			return lBlockStrList;
		}
		String pVar="DBTV_Sql";
		List pToAddStatemenstList=null;
		String pBlockStr="";
		for (int i = 0; i < lBlockStrList.size(); i++) {
			pBlockStr=(String)lBlockStrList.get(i);
			if(!lVariableDataTypeMap.containsKey(pVar.trim().toUpperCase())){
				lVariableDataTypeMap.put(pVar.trim().toUpperCase(), "VARCHAR");
				pToAddStatemenstList=new ArrayList(); // Add to List in Reverse Order.					
				pToAddStatemenstList.add("DECLARE "+pVar+" VARCHAR(1000)  /* dbTransplant Change Comment- to handle Recursive calls */ ; ");			
				addVarAfterDeclareNChangeVar(lBlockStrList,pProcName,"",pVar,pToAddStatemenstList);
			}
			
			pBlockStr=changeResursiveCallStatement(pBlockStr,pProcName,pVar );			
			lBlockStrList.set(i, pBlockStr);
		}
		
		
		return lBlockStrList;
		
	}
	public String changeResursiveCallStatement(String pBlockStr,String pProcName,String pVar ){
		Pattern lChkPattern=Pattern.compile("(?i)\\bCALL\\b[\\s\\r\\n]+\\b"+pProcName.trim()+"\\b[\\s\\r\\n]*\\((.+?)\\)[\\s\\r\\n]*;");
		Matcher lChkPatMatcher=lChkPattern.matcher(pBlockStr);		
		String callParamStr="";
		String param="";
		int lopenBraceCount=0;
		List lParamList=new ArrayList();		
		String lResultStr="";
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
			
			lResultStr=lResultStr+"/* dbTransplant Change Comment- to handle Recursive call -Start */ \n";
			lResultStr= lResultStr+" SET "+pVar +"='' ; \n" +
					" SET "+pVar+" = 'Call Dcms_IS_IntakeQuestionBranch(' ;\n";
			for (int i = 0; i < lParamList.size(); i++) {
				if(i==lParamList.size()-1){
					lResultStr= lResultStr+chkCallParameter(pVar,(String)lParamList.get(i),true);
				}else{
					lResultStr= lResultStr+chkCallParameter(pVar,(String)lParamList.get(i),false);
				}
				
			}		
			lResultStr=lResultStr+" EXECUTE IMMEDIATE "+pVar+" ;\n";
			lResultStr=lResultStr+"/* dbTransplant Change Comment- to handle Recursive call -End */ \n";
			System.out.println("lParamList::->"+lResultStr);
			return  addColor(lResultStr,"Changed Call Statements as Dyanamic sql");	
			
		}
		return pBlockStr ;
				
	}
	public String  chkCallParameter(String pVar,String pParam,boolean isLastParam){
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
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//getMasterApplyPatternList("");
		System.out.println("::::::inside main:::");
		PostProcessorApplyChangesDAO p=new PostProcessorApplyChangesDAO();
		//p.replaceSetRowcountOn("SET ROWCOUNT 500 ");
	    //p.replaceNullifWithEmptyString("coalesce(replace(A.ZipCode,'-',NULLIF(0,0)))");
		//p.replaceVarcharSizeInTimestamp("set HoursWorkedAsOfDate = CAST(CHAR(V_HeaderDate) AS VARCHAR(30))      ");
		//p.changeSwfStrToChar("swf_str(var1)");
		//p.castAsTimestamp("SET V_outDate = CAST(V_inString AS TIMESTAMP)");
		//p.likePattern("select '$' || CAST(TO_CHAR(FN_TO_MONEY(12345678.90),'mm/dd/yy') as VARCHAR(20))");
		//p.likePattern("SET V_SelectSql2 = '''' || V_Value || ''''; ");
		//p.setDayOfWeek("SET DATEFIRST 1;"," begin","DECLARE SWV_cursor_var CURSOR WITH RETURN FOR select 'Day Count from the Week begining -> ' || CAST((DAYOFWEEK('2012-02-20')) AS VARCHAR(30)) FROM SYSIBM.SYSDUMMY1;");
		//p.insertBracesInSelectInsideFromClause("SELECT V_ClaimID, V_TimeLineSeqNum, V_NewInterventionSeqNum, a.ReasonCode FROM SELECT V_SetCSReasonCode ReasonCode  from SYSIBM.SYSDUMMY1 UNION ALL SELECT V_SetERSReasonCode  from SYSIBM.SYSDUMMY1 UNION ALL SELECT V_SetAPSReasonCode  from SYSIBM.SYSDUMMY1 a  WHERE a.ReasonCode IS NOT NULL; ");
		//p.replaceLengthFunction("select clientid from bankprofile  where length(ClientID) - length(replace(translate(lower(ClientID),'','abcdefghijklmnopqrstuvwxyz'),' ','')) <2");
		//p.removeSQL("SELECT LISTAGG(NULLIF(RTRIM(C.Display),'')||',','') WITHIN GROUP(ORDER BY rowid)  INTO V_Reasons  FROM PendingReasons A,PendingReasons A INNER JOIN CodeApplication C ON A.ReasonCode = C.Code WHERE (A.ClaimId = V_ClaimID AND A.TimeLineSeqNum  = CAST(DECODE(V_TimeLineSeqNum,'','0',V_TimeLineSeqNum) AS SMALLINT)) ");
	}
	public String chkVarNameNAssignNewName(String lVarName){
		/*Set lVarMapSet=lVariableDataTypeMap.entrySet();
		Iterator iterator =lVarMapSet.iterator();
		while(iterator.hasNext()){
			Entry entry=(Entry)iterator.next();
			
		}*/
		if(lVariableDataTypeMap.containsKey(lVarName.trim().toUpperCase())){
			for (int i = 1; i < 20; i++) {
				lVarName=lVarName+i;
				if(!lVariableDataTypeMap.containsKey(lVarName.trim().toUpperCase())){
					return lVarName;
				}
			}
			
		}else{
			return lVarName;
		}
			
		return "";
	}
	
	
	
	
	
	public List replaceSourceSchemas(String pProjectId,List lMasterApplyPatternList){
		
		List lSchemaPairList=getSchemas(pProjectId);
		String[] lSchemaArr=null;
		PostProcChangePatDTO pPostProcChangePatDTO =null;
		
		for (int i = 0; i < lSchemaPairList.size(); i++) {
			lSchemaArr=((String)lSchemaPairList.get(i)).split("_DBT_SPLIT_");
			//call Replace Method
			pPostProcChangePatDTO=new PostProcChangePatDTO();
			pPostProcChangePatDTO.setFindPattern("(?i)\\b"+lSchemaArr[0].trim()+"\\b");
			pPostProcChangePatDTO.setReplacementPattern("(?i)\\b"+lSchemaArr[0].trim()+"\\b");
			pPostProcChangePatDTO.setReplacementText(lSchemaArr[1]);
			pPostProcChangePatDTO.setGroupCount(0);
			pPostProcChangePatDTO.setFindDesc("Schema Replaced");
			lMasterApplyPatternList.add(pPostProcChangePatDTO);
			
		}			
		return lMasterApplyPatternList;
	}
	public List getSchemas(String pProjectId){
		//GET_SOURCE_TARGET_SCHEMAS
		
		PreparedStatement lPreparedStatement=null;
		ResultSet lResultSet = null;
		List lSchemaPairList=new ArrayList();		
		try{
			lConnection=DBConnectionManager.getConnection();
			lPreparedStatement=lConnection.prepareStatement(WebConstant.GET_SOURCE_TARGET_SCHEMAS);
			lPreparedStatement.setString(1, pProjectId);
			lResultSet=lPreparedStatement.executeQuery();
			//SOURCE_SCHEMA, TARGET_SCHEMA  
			if(lResultSet!=null){
				while(lResultSet.next()){
					lSchemaPairList.add(lResultSet.getString("SOURCE_SCHEMA")+"_DBT_SPLIT_"+lResultSet.getString("TARGET_SCHEMA"));					
				}
			}
			
		}catch(SQLException e){
			return lSchemaPairList;
		}finally{
			
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			
		}
		
		return lSchemaPairList;
	}
	public List identifySybaseSysObjects(String pProjectId,List lMasterApplyPatternList){
		
		List lSybaseObjList=getSchemas(pProjectId);
		String[] lSchemaArr=null;
		PostProcChangePatDTO pPostProcChangePatDTO =null;
		
		for (int i = 0; i < lSybaseObjList.size(); i++) {
			
			//call Replace Method
			pPostProcChangePatDTO=new PostProcChangePatDTO();
			pPostProcChangePatDTO.setFindPattern("(?i)[\\b|\\--]"+lSybaseObjList.get(i).toString().trim()+"\\b");
			pPostProcChangePatDTO.setReplacementPattern("");
			pPostProcChangePatDTO.setReplacementText("");
			pPostProcChangePatDTO.setGroupCount(0);
			pPostProcChangePatDTO.setFindDesc("Sybase System Object Encountered");
			lMasterApplyPatternList.add(pPostProcChangePatDTO);
			
		}			
		return lMasterApplyPatternList;
	}
	public List getybaseSysObjects(String pProjectId){
		
		ProjectModifyDAO lProjectModifyDAO = new ProjectModifyDAO();
		ProjectDetailsMainDTO lProjectDetailsMainDTO = lProjectModifyDAO.getProjectDetails(pProjectId);
		
		
		//GET_SOURCE_TARGET_SCHEMAS
		String pDBMigrationTye=lProjectDetailsMainDTO.getDbMigrationType();
		PreparedStatement lPreparedStatement=null;
		ResultSet lResultSet = null;
		List lSybaseObjList=new ArrayList();		
		try{
			lConnection=DBConnectionManager.getConnection();
			lPreparedStatement=lConnection.prepareStatement(WebConstant.GET_SYBASE_SYS_OBJECTS);
			lPreparedStatement.setString(1, pDBMigrationTye);
			lResultSet=lPreparedStatement.executeQuery();
			//SOURCE_DB_OBJ, TARGET_DB_OBJ
			if(lResultSet!=null){
				while(lResultSet.next()){
					lSybaseObjList.add(lResultSet.getString("SOURCE_DB_OBJ"));					
				}
			}
			
		}catch(SQLException e){
			return lSybaseObjList;
		}finally{
			
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			
		}
		
		return lSybaseObjList;
	}
	public List chkCursorDeclarations(String pProjectId,String pCompleteProcName,List pBlockStrList){
		System.out.println("::::: inside method chkCusrorDecalrations  ::::");
		String lCursorName="";
		String lBlockString="";
		String lDeclareCursorStr="";
		String lNewCursorName="";
		List lCursorNamesList=getAllCursorNames(pProjectId, pCompleteProcName);
		
		//List lCursorNamesList=replaceDuplicateCursorNames(pProjectId, pCompleteProcName);
		System.out.println("Cursor Names:::->"+lCursorNamesList);
		
		System.out.println("Cursor Names:::->"+lCursorNamesList);
		Pattern lDeclareCursorPattern=null;
		Pattern lOpenCursorPattern=null;
		Pattern lCloseCursorPattern=null;
		Matcher lDeclareCursorPatMatcher=null;
		
		boolean isDeclareFound=false;		
		
		for (int i = 0; i < lCursorNamesList.size(); i++) {
			lCursorName=(String)lCursorNamesList.get(i);
			lDeclareCursorPattern=Pattern.compile("(?i)\\bDECLARE\\b[\\s\\\r\\n]+\\b"+lCursorName.trim()+"\\b[\\s\\\r\\n]+\\bCURSOR\\b[\\s\\\r\\n]+\\bFOR\\b[\\s\\r\\n\\W\\w]+;");
			lOpenCursorPattern=Pattern.compile("(?i)\\bOPEN\\b[\\s\\r\\n]+\\b"+lCursorName.trim()+"\\b");
			lCloseCursorPattern=Pattern.compile("(?i)\\bCLOSE\\b[\\s\\r\\n]+\\b"+lCursorName.trim()+"\\b");
			isDeclareFound=false;
			lDeclareCursorStr="";
			int lCursorCount=0;
			
			for (int j = 0; j < pBlockStrList.size(); j++) {
				lBlockString=(String)pBlockStrList.get(j);
				lDeclareCursorPatMatcher=lDeclareCursorPattern.matcher(lBlockString);
				lCursorCount=0;
				if(lDeclareCursorPatMatcher.find()){
					isDeclareFound=true;
					lDeclareCursorStr=lDeclareCursorPatMatcher.group(0);
				}
				if(lCloseCursorPattern.matcher(lBlockString).find()){
					isDeclareFound=false;
				}
				if(isDeclareFound == false && lOpenCursorPattern.matcher(lBlockString).find()){
					if(!"".equals(lDeclareCursorStr.trim())){
						lCursorCount=lCursorCount+1;
						lNewCursorName=lCursorName+lCursorCount;
						//System.out.println("OldCursor Name:::->"+lCursorName+"::New Cursor Name:::->"+lNewCursorName);						
						lDeclareCursorStr=lDeclareCursorStr.replaceAll("(?i)\\b"+lCursorName.trim()+"\\b", lNewCursorName);						
						lBlockString="/* Declare Cursor Added by DBTransplant */\n"+lDeclareCursorStr+"\n"+lBlockString;						
						pBlockStrList.set(j, lBlockString);
						for (int k = j; k < pBlockStrList.size(); k++) {
							pBlockStrList.set(k, ((String)pBlockStrList.get(k)).replaceAll("(?i)\\b"+lCursorName.trim()+"\\b", lNewCursorName));
						}	
					}
				}
			}
		}
		
		System.out.println("::::: method End chkCusrorDecalrations  ::::");
		return pBlockStrList;
	}
	public List getAllCursorNames(String pProjectId,String pCompleteProcName){
		//RUN_ID, ORDER_NO, PROCEDURE_NAME, STATEMENT_NO, CURSOR_NAME 
		
		PreparedStatement lPreparedStatement=null;
		ResultSet lResultSet = null;
		List lCursorNamesList=new ArrayList();		
		try{
			lConnection=DBConnectionManager.getConnection();
			lPreparedStatement=lConnection.prepareStatement(ToolConstant.GET_CURSOR_DETAILS_FOR_PROC);
			lPreparedStatement.setString(1, pProjectId.trim()+"_TARGET");
			lPreparedStatement.setString(2, pCompleteProcName);
			lResultSet=lPreparedStatement.executeQuery();
			//SOURCE_SCHEMA, TARGET_SCHEMA  
			if(lResultSet!=null){
				while(lResultSet.next()){
					lCursorNamesList.add(lResultSet.getString("CURSOR_NAME"));					
				}
			}
			
		}catch(SQLException e){
			return lCursorNamesList;
		}finally{			
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);			
		}
		return lCursorNamesList;
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
	public String addColor(String pParam,String pTitle){
		
		if("WRITE_TO_FILE".equalsIgnoreCase(lWriteMode)){
			return pParam;
		}
		
		String[] lParamArr = pParam.split("\\n");
		String lBlockStr="";
		for (int i = 0; i < lParamArr.length; i++) {
			
			if(!"".equals(lBlockStr) ){
				lBlockStr +="\n";
			}
			
			if("IDENTIFY_PATTERN".equalsIgnoreCase(lIdentifyOrReplaceMode)){
				lBlockStr += "<font_DBT_SPACE_style=\"background-color:yellow;color:RED\"_DBT_SPACE_title=\""+pTitle+"\">"+lParamArr[i]+"</font>";
			}else{
				lBlockStr +=  "<font_DBT_SPACE_style=\"background-color:#00DD00;color:black\"_DBT_SPACE_title=\""+pTitle+"\">"+lParamArr[i]+"</font>";
			}
		}
		
		
		
		return lBlockStr;
	}
	
	public void getSPInputVariables(List pBlockStrList){
		String pBlockStr="";
		//Pattern lCreateProcPattern=Pattern.compile("(?i)(\\bCREATE\\b|\\bCREATE\\b\\s+\\bOR\\b\\s+\\bREPLACE\\b)\\s+(\\bPROCEDURE\\b|\\bPROC\\b)+[\\s\\r\\n]+[\\S]+[\\s\\r\\n]*\\(");
			try {
				for (int i = 0; i < pBlockStrList.size(); i++) {
					pBlockStr=(String)pBlockStrList.get(i);
					lVariableDataTypeMap=getSPInputVar( pBlockStr,lVariableDataTypeMap);
					/*Matcher lChkPatMatcher=lCreateProcPattern.matcher(pBlockStr.trim());
					if(lChkPatMatcher.find()){
						pBlockStr=pBlockStr.substring(lChkPatMatcher.end(),pBlockStr.length());
						pBlockStr=pBlockStr.replaceAll("\\,", " , ");
						pBlockStr=pBlockStr.replaceAll("\\(", " ( ");
						String[] lSourceArray=pBlockStr.split("\\s+");
						for (int j = 0; j < lSourceArray.length; j++) {
							if(lSourceArray[j].startsWith("v_")){
								if(lVariableDataTypeMap != null ){
									lVariableDataTypeMap.put(lSourceArray[j].trim().toUpperCase(),lSourceArray[j+1].trim());
								}
								//System.out.println("--"+lSourceArray[j].trim()+"---"+lSourceArray[j+1].trim());															
							}		
						}
						
					}*/
					
				}
				//printing the hashmap
				/*Set entries= lVariableDataTypeMap.entrySet();
				Iterator it=entries.iterator();
				while(it.hasNext()){
					Entry entry =(Entry)it.next();
					//System.out.println(entry.getKey()+" --- <<<>>> --- "+entry.getValue());
					logger.info(":::::variabe map details:::::"+entry.getKey()+" --- <<<>>> --- "+entry.getValue());
				}*/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();			
				return;
			}
			
		}
	
	public HashMap getSPInputVar(String pBlockStr,HashMap pInputVariableDataTypeMap){
		

		Pattern lCreateProcPattern=Pattern.compile("(?i)(\\bCREATE\\b|\\bCREATE\\b\\s+\\bOR\\b\\s+\\bREPLACE\\b)\\s+(\\bPROCEDURE\\b|\\bPROC\\b)+[\\s\\r\\n]+[\\S]+[\\s\\r\\n]*\\(");
		
		Matcher lChkPatMatcher=lCreateProcPattern.matcher(pBlockStr.trim());
		if(lChkPatMatcher.find()){
			pBlockStr=pBlockStr.substring(lChkPatMatcher.end(),pBlockStr.length());
			pBlockStr=pBlockStr.replaceAll("\\,", " , ");
			pBlockStr=pBlockStr.replaceAll("\\(", " ( ");
			String[] lSourceArray=pBlockStr.split("\\s+");
			for (int j = 0; j < lSourceArray.length; j++) {
				if(lSourceArray[j].startsWith("v_")){
					if(pInputVariableDataTypeMap != null ){
						pInputVariableDataTypeMap.put(lSourceArray[j].trim().toUpperCase(),lSourceArray[j+1].trim());
					}
					//System.out.println("--"+lSourceArray[j].trim()+"---"+lSourceArray[j+1].trim());															
				}		
			}
			
		}
		return pInputVariableDataTypeMap;
		
	
	}
	
	public List getChangeTrackerDetails(String pProjectId,String pProcName){
		List lTrackerDetailsList = new ArrayList();
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		try{
			lConnection = DBConnectionManager.getConnection();
			lPreparedStatement = lConnection.prepareStatement(WebConstant.GET_POST_PROCESSOR_CHANGES_TRACKER_DETAILS);
			lPreparedStatement.setString(1, pProjectId);
			lPreparedStatement.setString(2, pProcName);
			lResultSet = lPreparedStatement.executeQuery();
			PostProcChangeTrackerDTO lPostProcChangeTrackerDTO = new PostProcChangeTrackerDTO();
			if(lResultSet != null){
				while (lResultSet.next()) {
					 lPostProcChangeTrackerDTO = new PostProcChangeTrackerDTO();
					
					 lPostProcChangeTrackerDTO.setBeforeBlock(ToolsUtil.replaceNull(lResultSet.getString("BEFORE_BLOCK")));
					 lPostProcChangeTrackerDTO.setAfterBlock(ToolsUtil.replaceNull(lResultSet.getString("AFTER_BLOCK")));
					 lTrackerDetailsList.add(lPostProcChangeTrackerDTO);
					
				}
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.info(":::::error in getChangeTrackerDetails-->"+e.getMessage());
		}finally{
			DBConnectionManager.closeConnection(lConnection);
		}
		
		return lTrackerDetailsList;
	}
	
	public String removeToolChars(String pLine){
     	//System.out.println(":::pline in remove method::::"+pLine);
     	//System.out.println(":::pline in remove method::::"+ToolsUtil.replaceNull(pLine).replaceAll("0000_TFC_", " ").replaceAll("_DBT_COMM_", " "));
     	return pLine.replaceAll("0000_TFC_", " ").replaceAll("_DBT_COMM_", " ");     	
     }
	
public List modifySqlInPostProcessor(List lBlockStrList,String pProcName){
	//System.out.println("BLOCKSTRINGLIST "+lBlockStrList);
	String sqlstr="";
	
		for (int i = 0; i < lBlockStrList.size(); i++) {
			sqlstr = (String) lBlockStrList.get(i);
			//System.out.println("SQLSTR " + sqlstr);

			// pattern matching for select statements with order by clause
			Pattern pSelectOrderBy = Pattern
					.compile("(?i)\\bSELECT\\b[\\s\\r\\n\\W\\w]+\\bORDER\\b[\\s\\r\\n]+\\bBY\\b[\\s\\r\\n\\W\\w]+");
			Matcher mSelectOrderBy = pSelectOrderBy.matcher(sqlstr);
			
			if (mSelectOrderBy.find()) {
				sqlstr = modifyOrderBy(sqlstr);
				// lBlockStrList.set(i, sqlstr);
				lBlockStrList.set(i, sqlstr);
			}
			
			
			// pattern matching for call dbms output with coalesce
			Pattern pCallWithCoalesce = Pattern
					.compile("(?i)\\bCALL\\b[\\s\\r\\n\\W\\w]+\\bCOALESCE\\(");
			Matcher mCallWithCoalesce = pCallWithCoalesce.matcher(sqlstr);
			
			if (mCallWithCoalesce.find()) {
				sqlstr = modifyCoalesceInCall(sqlstr);
				// lBlockStrList.set(i, sqlstr);
				lBlockStrList.set(i, sqlstr);
			}
			
			
			// pattern matching for set variable with coalesce
			Pattern pSetWithCoalesce = Pattern
								.compile("(?i)\\bSET\\b[\\s\\r\\n\\W\\w]+\\bCOALESCE\\(");
			Matcher mSetWithCoalesce = pSetWithCoalesce.matcher(sqlstr);
			
			if (mSetWithCoalesce.find()) {
				sqlstr = insertReplaceInSetVariable(sqlstr);
				// lBlockStrList.set(i, sqlstr);
				lBlockStrList.set(i, sqlstr);
			}
		//	SET V_SelectSql2 = '''' || V_Value || ''''; 
			
			// pattern matching for set variable 
			Pattern pSet = Pattern.compile("(?i)\\bset\\s*\\w+\\s*\\=\\s*\\b[\\s\\r\\n\\w\\W]+\\||");
			Matcher mSet = pSet.matcher(sqlstr);
			System.out.println(mSet.find()+" mset.find value"+"  "+sqlstr);
			if (mSet.find()) {
				sqlstr = insertReplaceInSetVariable(sqlstr);
				// lBlockStrList.set(i, sqlstr);
				lBlockStrList.set(i, sqlstr);
			}
			
			

			// pattern matching for if not condition
			Pattern pIfNot = Pattern.compile("(?i)\\bIF\\s*\\bNOT\\s*\\(\\b[\\s\\r\\n\\W\\w]+\\)");
			Matcher mIfNot = pIfNot.matcher(sqlstr);
			//System.out.println(mSet.find());
			if (mIfNot.find()) {
				sqlstr = modifyIfNotCondition(sqlstr);
				// lBlockStrList.set(i, sqlstr);
				lBlockStrList.set(i, sqlstr);
			}
			
			
			// pattern matching set rowcount
			Pattern pSetRowcount = Pattern.compile("(?i)\\bSET\\b[\\s\\r\\n]+\\bROWCOUNT\\s*\\d{1,}");
			Matcher mSetRowcount = pSetRowcount.matcher(sqlstr);
			//System.out.println(mSet.find());
			if (mSetRowcount.find()) {
				sqlstr = replaceSetRowcountOn(sqlstr);
				// lBlockStrList.set(i, sqlstr);
				lBlockStrList.set(i, sqlstr);
			}
			
			// pattern matching replace with nullif
			Pattern pReplaceWithNullif = Pattern.compile("(?i)\\b[\\s\\r\\n\\W\\w]*\\breplace\\b[\\s\\r\\n\\W\\w]");
			Matcher mReplaceWithNullif  = pReplaceWithNullif .matcher(sqlstr);
			//System.out.println(mSet.find());
			if (mReplaceWithNullif .find()) {
				sqlstr = replaceNullifWithEmptyString(sqlstr);
				// lBlockStrList.set(i, sqlstr);
				lBlockStrList.set(i, sqlstr);
			}
			
			
			
			// pattern matching timestamp variable varchar size
			Pattern pTimestampInSet = Pattern.compile("(?i)\\b[\\s\\r\\n\\W\\w]*\\bcast\\s*\\(\\s*\\bchar\\s*\\(\\b[\\s\\r\\n\\W\\w]+\\)\\s*\\bas\\s*\\bvarchar\\(\\s*\\b30\\)");
			Matcher mTimestampInSet  = pTimestampInSet .matcher(sqlstr);
			//System.out.println(mSet.find());
			if (mTimestampInSet .find()) {
				sqlstr = replaceVarcharSizeInTimestamp(sqlstr);
				// lBlockStrList.set(i, sqlstr);
				lBlockStrList.set(i, sqlstr);
			}
			
			// pattern matching timestamp variable varchar size
			Pattern pTimestampCast = Pattern.compile("(?i)\\b[\\s\\r\\n\\W\\w]*\\bcast\\s*\\(\\b[\\s\\r\\n\\W\\w]+\\s*\\bas\\s*\\btimestamp\\)");
			Matcher mTimestampCast  = pTimestampCast .matcher(sqlstr);
			//System.out.println(mSet.find());
			if (mTimestampCast .find()) {
				sqlstr = castAsTimestamp(sqlstr);
				// lBlockStrList.set(i, sqlstr);
				lBlockStrList.set(i, sqlstr);
			}
			
			
			// pattern matching swf_str(
			Pattern pSwfStr = Pattern.compile("\\b[\\s\\r\\n\\W\\w]*\\bswf\\s*\\_str\\s*\\(");
			Matcher mSwfStr  = pSwfStr .matcher(sqlstr);
			//System.out.println(mSet.find());
			if (mSwfStr .find()) {
				sqlstr = changeSwfStrToChar(sqlstr);
				// lBlockStrList.set(i, sqlstr);
				lBlockStrList.set(i, sqlstr);
			}
			
			
			// pattern matching for select in from clause
			Pattern pSelectInFrom = Pattern.compile("(?i)\\b[\\s\\r\\n\\W\\w]*\\bfrom\\s*\\bselect\\b[\\s\\r\\n\\W\\w]+\\bwhere\\b[\\s\\r\\n\\W\\w]+");
			Matcher mSelectInFrom  = pSelectInFrom .matcher(sqlstr);
			//System.out.println(mSet.find());
			if (mSelectInFrom .find()) {
				sqlstr = insertBracesInSelectInsideFromClause(sqlstr);
				// lBlockStrList.set(i, sqlstr);
				lBlockStrList.set(i, sqlstr);
			}
			
			
			
			// pattern matching for length function
			Pattern pLenFunc = Pattern.compile("(?i)\\b[\\s\\r\\n\\W\\w]*\\b(?!where)\\s*\\blength\\s*\\(\\b(?!replace)");
			Matcher mLenFunc  = pLenFunc .matcher(sqlstr);
			//System.out.println(mSet.find());
			if (mLenFunc .find()) {
				sqlstr = replaceLengthFunction(sqlstr);
				// lBlockStrList.set(i, sqlstr);
				lBlockStrList.set(i, sqlstr);
			}
			
						
			
		
			// pattern matching get diagnostics and delete statement
			//Pattern pDeleteStatementAndGetDiagnostics = Pattern.compile("(?i)\\bset\\s*\\b[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bSWV_Error\\s*\\;\\n\\bGET\\s*\\bDIAGNOSTICS\\s*\\b[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bROW_COUNT\\s*\\;");
			Pattern pDeleteStatement = Pattern.compile("(?i)\\bset\\s*\\b[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bSWV_Error\\s*\\;");
			Matcher mDeleteStatement  = pDeleteStatement .matcher(sqlstr);
			
			if (mDeleteStatement .find()) {
				
				String s2=(String) lBlockStrList.get(i+1);
				//System.out.println("from calling method2"+s2);
				Pattern pGetDiagnostics = Pattern.compile("(?i)\\bGET\\s*\\bDIAGNOSTICS\\s*\\b[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bROW_COUNT\\s*\\;");
				Matcher mGetDiagnostics  = pGetDiagnostics .matcher(s2);
				//System.out.println("from calling method3"+mGetDiagnostics.find());
				if( Pattern.compile("(?i)\\bGET\\s*\\bDIAGNOSTICS\\s*\\b[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bROW_COUNT\\s*\\;").matcher(s2).find())
				{
					//System.out.println("from calling method4"+ Pattern.compile("(?i)\\bGET\\s*\\bDIAGNOSTICS\\s*\\b[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bROW_COUNT\\s*\\;").matcher(s2).find());
				sqlstr = getDiagnosticsStatementSwap(sqlstr,s2);
				
				lBlockStrList.set(i, sqlstr);
				lBlockStrList.set(i+1, " ");
				}
			}
		
		
			
			
			// pattern matching for set datefirst
		
				Pattern pBegin = Pattern.compile("(?i)\\bbegin+\\b[\\s\\r\\n\\W\\w]+\\bSET\\s*\\bDATEFIRST");
				Matcher mBegin  = pBegin .matcher(ToolsUtil.removeToolKeywords(sqlstr));
				Pattern pSetDatefirstSeven = Pattern.compile("(?i)\\b\\bset\\s*\\bdatefirst\\s*\\b7;");
				Matcher mSetDatefirstSeven  = pSetDatefirstSeven .matcher(ToolsUtil.removeToolKeywords(sqlstr));
				
			//	System.out.println(Pattern.compile("(?i)\\b\\bset\\s*\\bdatefirst\\s*\\b7;").matcher(ToolsUtil.removeToolKeywords(sqlstr)).find()+" mbegin pattern");
				if (mBegin .find()) {
					sqlstr = setDayOfWeek(sqlstr);
					
					lBlockStrList.set(i, sqlstr);
				}
				
				if (Pattern.compile("(?i)\\b\\bset\\s*\\bdatefirst\\s*\\b7;").matcher(ToolsUtil.removeToolKeywords(sqlstr)) .find()) {
					sqlstr = setDayOfWeek(sqlstr);
					
					lBlockStrList.set(i, sqlstr);
				}
				
			

				// pattern matching for update with case when exists
			
				Pattern pUpdateSessionWithCaseWhenExists=Pattern.compile("(?i)\\bUPDATE\\s*\\bSESSION.tt_[\\s\\r\\n\\W\\w]+\\bset[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bcase\\s*\\bwhen\\s*\\bexists");
				Matcher mUpdateSessionWithCaseWhenExists=pUpdateSessionWithCaseWhenExists.matcher(sqlstr);
					//System.out.println("from calling method "+mUpdateSessionWithCaseWhenExists.find()+"  sqlstring"+sqlstr);
					
					if (Pattern.compile("(?i)\\bUPDATE\\s*\\bSESSION.tt_[\\s\\r\\n\\W\\w]+\\bset[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bcase\\s*\\bwhen\\s*\\bexists").matcher(sqlstr) .find()) {
						//System.out.println("inside if");
						sqlstr = updateWithCaseWhenExists(sqlstr);
						
						lBlockStrList.set(i, sqlstr);
					}
					
				
					
				// pattern matching for select top 1 in sybase
					
				Pattern pSelectTopOne=Pattern.compile("(?i)\\bIF\\s*\\b[\\s\\r\\n\\W\\w]+\\bis\\s*\\bnull\\b[\\s\\r\\n\\W\\w]+\\bBEGIN+\\b[\\s\\r\\n\\W\\w]");
				Matcher mSelectTopOne=pSelectTopOne.matcher(sqlstr);
					//System.out.println("from calling method "+mSelectTopOne.find()+"  sqlstring"+sqlstr);
						
					if (Pattern.compile("(?i)\\bIF\\s*\\b[\\s\\r\\n\\W\\w]+\\bis\\s*\\bnull\\b[\\s\\r\\n\\W\\w]+\\bBEGIN+\\b[\\s\\r\\n\\W\\w]").matcher(sqlstr) .find()) {
					//	System.out.println("inside if");
						sqlstr = modifySelectTopOne(sqlstr);
							
						lBlockStrList.set(i, sqlstr);
						lBlockStrList.set(i+1, "");
						lBlockStrList.set(i+2, "");
					}	
					
					// pattern matching select statement containing '$'
					
					Pattern pMoneyValue=Pattern.compile("(?i)\\bselect\\s*\\'$'\\s*\\||\\s*\\bcast\\s*\\(\\s*\\bcast\\s*\\(\\s*\\d*\\.\\d*\\s*\\bas\\s*\\bdecimal\\s*\\(\\s*\\d{1,}\\s*\\,\\s*\\d{1,}\\s*\\)\\s*\\)\\s*\\bas\\s*\\b[\\s\\r\\n\\w\\W]+\\s*\\)\\s*\\)");
					Matcher mMoneyValue=pMoneyValue.matcher(sqlstr);
						//System.out.println("from calling method "+mMoneyValue.find()+"  sqlstring"+sqlstr);
						
						if (Pattern.compile("(?i)\\bselect\\s*\\'$'\\s*\\||\\s*\\bcast\\s*\\(\\s*\\bcast\\s*\\(\\s*\\d*\\.\\d*\\s*\\bas\\s*\\bdecimal\\s*\\(\\s*\\d{1,}\\s*\\,\\s*\\d{1,}\\s*\\)\\s*\\)\\s*\\bas\\s*\\b[\\s\\r\\n\\w\\W]+\\s*\\)\\s*\\)").matcher(sqlstr) .find()) {
						//	System.out.println("inside if");
							sqlstr = castingMoneyValue(sqlstr);
							
							lBlockStrList.set(i, sqlstr);
						}
						
					//	\\b[\\s\\r\\n\\w\\W]+\\s*\\bINNER\\s*\\bJOIN\\b[\\s\\r\\n\\w\\W]+\\s*\\bON		
						
						// pattern matching select in listagg function
						
					Pattern pSelectInListAgg=Pattern.compile("(?i)\\bselect\\s*\\blistagg\\s*\\(\\s*\\bnullif\\s*\\(\\s*\\brtrim\\s*\\(\\s*\\b[\\s\\r\\n\\w\\W]+\\s*\\bwithin\\s*\\bgroup\\s*\\(\\s*\\border\\s*\\bby\\s*\\b[\\s\\r\\n\\w\\W]+\\s*\\)\\s*\\b[\\s\\r\\n\\w\\W]+");
					Matcher mSelectInListAgg=pSelectInListAgg.matcher(sqlstr);
						//	System.out.println("from calling method "+mSelectInListAgg.find()+"  sqlstring"+sqlstr);
							
						if (Pattern.compile("(?i)\\bselect\\s*\\blistagg\\s*\\(\\s*\\bnullif\\s*\\(\\s*\\brtrim\\s*\\(\\s*\\b[\\s\\r\\n\\w\\W]+\\s*\\bwithin\\s*\\bgroup\\s*\\(\\s*\\border\\s*\\bby\\s*\\b[\\s\\r\\n\\w\\W]+\\s*\\)\\s*\\b[\\s\\r\\n\\w\\W]+").matcher(sqlstr) .find()) {
							//System.out.println("inside if");
								sqlstr = selectInListagg(sqlstr);
								
								lBlockStrList.set(i, sqlstr);
							}		
		
						
						
						
						
						//pattern matching for length function with empty string
						Pattern pLengthForEmptyString=Pattern.compile("(?i)\\bvalues\\s*\\bcoalesce\\s*\\(\\s*\\bcast\\s*\\(\\s*\\brepeat\\s*\\(\\s*\\'\\b0\\'\\s*\\,\\s*\\d\\s*\\-\\s*\\blength\\s*\\(\\s*\\bcast\\s*\\(\\b[\\s\\r\\n\\w\\W]+");
						Matcher mLengthForEmptyString=pLengthForEmptyString.matcher(sqlstr);
							//	System.out.println("from calling method "+mLengthForEmptyString.find()+"  sqlstring"+sqlstr);
								
							if (Pattern.compile("(?i)\\bvalues\\s*\\bcoalesce\\s*\\(\\s*\\bcast\\s*\\(\\s*\\brepeat\\s*\\(\\s*\\'\\b0\\'\\s*\\,\\s*\\d\\s*\\-\\s*\\blength\\s*\\(\\s*\\bcast\\s*\\(\\b[\\s\\r\\n\\w\\W]+").matcher(sqlstr) .find()) {
							//	System.out.println("inside if");
									sqlstr = modifyLengthFunctionForEmptyString(sqlstr);
									
									lBlockStrList.set(i, sqlstr);
								}
						
						
						
		}
		return lBlockStrList;
	}



      
   public String modifyOrderBy(String sqlstr){
/*
		TGSqlParser sqlparser1 = new TGSqlParser(EDbVendor.dbvdb2);
	   
	    sqlparser1.sqltext = sqlstr;
	    int ret = sqlparser1.parse();
	      if (ret != 0) {
	          System.out.println(sqlparser1.getErrormessage());
	         // return null;
	      }

	     

	      TSelectSqlStatement sql1 = (TSelectSqlStatement)sqlparser1.sqlstatements.get(0);
	     
	      
	      //get column name
	      StringBuffer flds = new StringBuffer();
	      for(int k=0;k<sql1.getResultColumnList().size();k++){
	          if (k>0) { flds.append(","); }
	          flds.append(sql1.getResultColumnList().getResultColumn(k).toString());
	         }

	      //get table name
	      StringBuffer tbls = new StringBuffer();
	      for(int j=0;j<sql1.tables.size();j++){
	          if (j>0) { tbls.append(","); }
	          tbls.append(sql1.tables.getTable(j).toString());
	      }

	      
	      String ord="";
	      if(sql1.getOrderbyClause()!=null){
	    	  ord=sql1.getOrderbyClause().toString();
	    	  System.out.println(ord);
	      }
	      */
	   String substr="";
	    if(sqlstr!=null){ 
	    
	     Pattern p2=Pattern.compile("(?i)\\bORDER\\s*\\bBY\\b(\\s\\r\\n\\W\\w)*");
	     Matcher m2=p2.matcher(sqlstr);
	      if( m2.find()){
	   	  // sqlstr=sqlstr.replaceAll("(?i)\\bORDER\\s*\\bBY\\b[\\s\\r\\n\\W\\w]*", " ");
	   	  substr=sqlstr.substring(sqlstr.indexOf("ORDER"));
	    	//  System.out.println("substring "+sqlstr);
	      }

	    Pattern p1=Pattern.compile("\\w\\.");
	    Matcher m1=p1.matcher(substr);
	    if(m1.find())
	    {
	    	substr=substr.replaceAll("\\w\\.", " ");
	    	sqlstr=sqlstr.replaceAll("(?i)\\bORDER\\s*\\bBY\\b[\\s\\r\\n\\W\\w]*", " ");
		//System.out.println(ord);
	    	sqlstr=sqlstr+substr;
	    	 return sqlstr;
	    }
	     
	   
	 //  System.out.println(sqlstr);
	   
	  
	    }
	    return sqlstr;
   }
    
   
   public String modifyCoalesceInCall(String sqlstr){
/*
		TGSqlParser sqlparser1 = new TGSqlParser(EDbVendor.dbvdb2);
	  
	    sqlparser1.sqltext = sqlstr;
	    int ret = sqlparser1.parse();
	      if (ret != 0) {
	          System.out.println(sqlparser1.getErrormessage());
	        // return null;
	      }

	     

	     TCustomSqlStatement st=(TCustomSqlStatement)sqlparser1.sqlstatements.get(0);
	    // System.out.println(st.sqlstatementtype);
	     System.out.println((TCustomSqlStatement)sqlparser1.sqlstatements.get(0));
	     TDb2CallStmt callstmt=(TDb2CallStmt)st;
	    
	    // System.out.println(callstmt);
	     String callstmttostring="";
	     if(callstmt!=null){
	    	 callstmttostring=callstmt.toString();*/
	   if(sqlstr!=null){
	    	 Pattern p1=Pattern.compile("(?i)\\bCALL\\s*\\b[\\s\\r\\n\\w\\W]+\\bCOALESCE\\s*\\(");
			   // Matcher m1=p1.matcher(callstmttostring);
	    	 Matcher m1=p1.matcher(sqlstr);
			    if(m1.find())
			    {
			    	sqlstr=sqlstr.replaceAll("(?i)\\bCOALESCE\\s*\\(", "COALESCE(CAST(");
			    	sqlstr=sqlstr.replaceAll("\\,", " as varchar(40),");
			    	sqlstr=sqlstr.replaceAll("\\;", ");");
				//System.out.println(callstmttostring);
				
				return sqlstr;
			    }
	     }
	     
	     
	  return "";
	     
  }
    
   public String insertReplaceInSetVariable(String sqlstr){

	/*	TGSqlParser sqlparser1 = new TGSqlParser(EDbVendor.dbvdb2);
	  
	    sqlparser1.sqltext = sqlstr;
	    int ret = sqlparser1.parse();
	      if (ret != 0) {
	          System.out.println(sqlparser1.getErrormessage());
	        // return null;
	      }

	     

	     TCustomSqlStatement st=(TCustomSqlStatement)sqlparser1.sqlstatements.get(0);
	    // System.out.println(st.sqlstatementtype);
	     System.out.println((TCustomSqlStatement)sqlparser1.sqlstatements.get(0));
	     TDb2SetVariableStmt setstmt=(TDb2SetVariableStmt)st;
	    
	    // System.out.println(callstmt);
	     String setstmttostring="";
	    
	     if(setstmt!=null){
	    	 setstmttostring=setstmt.toString();
	    	*/
	   if(sqlstr!=null){
	    	Pattern p1=Pattern.compile("(?i)\\SET\\s*\\b[\\s\\r\\n\\w\\W]+\\bCOALESCE\\s*\\(");
			Matcher m1=p1.matcher(sqlstr);
			
			 Pattern p2=Pattern.compile("(?i)\\bset\\s*\\b[\\s\\r\\n\\w\\W]\\s*\\=\\b[\\s\\r\\n\\w\\W]+\\||\\s*\\||\\b[\\s\\r\\n\\w\\W]\\;");
			 Matcher m2=p2.matcher(sqlstr);
			// System.out.println("m1.find= "+m1.find()+" sqlstr"+sqlstr);
			// System.out.println("m2.find= "+m2.find()+" sqlstr"+sqlstr);
			  if(m1.find())
			    {
				  sqlstr=sqlstr.replaceAll("(?i)\\bCOALESCE\\s*\\(", "COALESCE(REPLACE(");
				
				  sqlstr=sqlstr.replaceAll("\\,\\'\\'\\)", ",'''',''''''),'')");
				//System.out.println(setstmt.getResultColumnList()+" RESULT");
				
				return sqlstr;
			    }
			  /*  if(Pattern.compile("(?i)\\bset+\\b[\\s\\r\\n\\w\\W]+\\=\\s+\\.+\\||\\b[\\s\\r\\n\\w\\W]+\\||").matcher(sqlstr).find())
			    {
			    	System.out.println("inside loop");
			    	
			    	//SET V_SelectSql2 = '''' || replace(V_Value,'''','''''') || '''';
			    	//sqlstr=sqlstr.replaceAll("\\bV_", "'||REPLACE(");
			    	if(sqlstr.contains("||")){
				String sub=sqlstr.substring(sqlstr.indexOf("||"));
				String sub2=sub.substring(0, sub.indexOf("||"));
				System.out.println(sub2);
			    sqlstr=sqlstr.replaceAll("\\||\\s*\\||", "|| replace("+sub2.trim()+" ,'''','''''') || ");
			
			    	  return sqlstr;
			    	}
			    }*/
			    
			 // else
			 // {
				//  sqlstr=sqlstr.replaceAll("\\bV_", "'||REPLACE("); 
				//  return sqlstr;
			 // }
			    
	     }
	    
	     
	     
	  return sqlstr;
	     
 }
   
   
 
   public String modifyIfNotCondition(String sqlstr){

	/*	TGSqlParser sqlparser1 = new TGSqlParser(EDbVendor.dbvdb2);
	  
	    sqlparser1.sqltext = sqlstr;
	    int ret = sqlparser1.parse();
	      if (ret != 0) {
	          System.out.println(sqlparser1.getErrormessage());
	        // return null;
	      }

	     

	     TCustomSqlStatement st=(TCustomSqlStatement)sqlparser1.sqlstatements.get(0);
	    // System.out.println(st.sqlstatementtype);
	     //System.out.println((TCustomSqlStatement)sqlparser1.sqlstatements.get(0));
	     
	   
	     String ifnotcondntostring="";
	    
	    if(st!=null){
	    	 ifnotcondntostring=st.toString();
	    	*/
	   if(sqlstr!=null){
	    	Pattern p1=Pattern.compile("(?i)\\bIF\\s*\\bNOT\\s*\\(\\b[\\s\\r\\n\\W\\w]+\\)");
			Matcher m1=p1.matcher(sqlstr);
			//System.out.println(m1.find());
			
			 if(m1.find())
			    {
				// System.out.println(ifnotcondntostring.indexOf("("));
				 int start=sqlstr.indexOf("(");
				 int end=sqlstr.indexOf("=");
				 String sub=sqlstr.substring(start+1,end);
				// System.out.println(sub);
				 sqlstr=sqlstr.replaceAll("(?i)\\)\\s*\\bthen", "AND "+sub.trim()+" IS NOT NULL ) then");
				
				 sqlstr=sqlstr.replaceAll("\\,\\'\\'\\)", ",'''',''''''),'')");
			//	System.out.println(ifnotcondntostring);
				
				return sqlstr;
			   }
			   
	     }
	    
	  return "";
	     
}
   
  
   
   public String replaceSetRowcountOn(String sqlstr){

	    if(sqlstr!=null){
	    	
	    	
	    	Pattern p1=Pattern.compile("(?i)\\bSET\\b[\\s\\r\\n]+\\bROWCOUNT\\s*\\d{1,}");
			Matcher m1=p1.matcher(sqlstr);
		//	System.out.println("sqlstr "+sqlstr);
			
		 if(m1.find())
		 {
				//System.out.println(sqlstr.indexOf("ROWCOUNT"));
				 int start=sqlstr.toUpperCase().indexOf("ROWCOUNT");
				
				 String sub=sqlstr.substring(start+9);
				 
				// System.out.println(sub);
				 int end=sub.indexOf(" ");
				// System.out.println(end);
				 String sub2=sub.substring(0, end);
				// System.out.println("sub2   "+sub2);
				 sqlstr=sqlstr.replaceAll("(?i)\\bSET\\b[\\s\\r\\n]+\\bROWCOUNT\\s*\\d{1,}", "FETCH FIRST "+sub2+" ROWS ONLY");
				
				//System.out.println(sqlstr);
				
				 return sqlstr;
		   }
			   
	     }
	    
	  return "";
	     
}
   
   
   public String replaceNullifWithEmptyString(String sqlstr){

	    if(sqlstr!=null){
	    	
	    	
	    	Pattern p1=Pattern.compile("(?i)\\b[\\s\\r\\n\\W\\w]*\\breplace\\b[\\s\\r\\n\\W\\w]");
			Matcher m1=p1.matcher(sqlstr);
			//System.out.println("sqlstr "+sqlstr);
			//System.out.println(m1.find());
		 if(m1.find())
		 {
				
				 sqlstr=sqlstr.replaceAll("(?i)\\bNULLIF\\s*\\(\\d\\,\\d\\)", "''");
				
				// System.out.println("REPLACED STRING "+sqlstr);
				
				 return sqlstr;
		   }
			   
	     }
	    
	  return "";
	     
}
   
   public List replaceDuplicateCursorNames(String pProjectId,String  pCompleteProcName,List pBlockStrList){
	   
	   List lCursorNamesList=getAllCursorNames(pProjectId, pCompleteProcName);
	   String modifiedName="";
	   for (int i = 0; i < lCursorNamesList.size(); i++) 
	   {
		   for (int j = i; j < lCursorNamesList.size(); j++)
		   {
			   if(lCursorNamesList.get(i).toString().equalsIgnoreCase(lCursorNamesList.get(j).toString()))
			   {
				   
				   for (int k = 0; k < pBlockStrList.size();k++) {
					 //  System.out.println(lCursorNamesList.get(i).toString());
					  // System.out.println(pBlockStrList.get(k).toString().toUpperCase().contains(lCursorNamesList.get(i).toString().toUpperCase()));
					  // System.out.println(pBlockStrList.get(k).toString().toUpperCase());
					   if( pBlockStrList.get(k).toString().toUpperCase().contains(lCursorNamesList.get(i).toString().toUpperCase())){
						//System.out.println("inside if");
					   pBlockStrList.set(k,(pBlockStrList.get(k).toString().toUpperCase()).replace(lCursorNamesList.get(i).toString().toUpperCase(),lCursorNamesList.get(i).toString().concat("C"+k)));
					   System.out.println(pBlockStrList.get(k));
					   }
					   }
			   }
		   
		   }
	   }
	  //  System.out.println("cursor names list in method "+lCursorNamesList);
	  // System.out.println("block list in method "+pBlockStrList);
	   return pBlockStrList;
		   
   }
   
    
   
   public String replaceVarcharSizeInTimestamp(String sqlstr){

	    if(sqlstr!=null){
	    	
	    	
	    	Pattern p1=Pattern.compile("(?i)\\b[\\s\\r\\n\\W\\w]*\\bcast\\s*\\(\\s*\\bchar\\s*\\(\\b[\\s\\r\\n\\W\\w]+\\)\\s*\\bas\\s*\\bvarchar\\(\\s*\\b30\\)");
			Matcher m1=p1.matcher(sqlstr);
			//System.out.println("sqlstr "+sqlstr);
			//System.out.println(m1.find());
		 if(m1.find())
		 {
				
				 sqlstr=sqlstr.replaceAll("(?i)\\)\\s*\\bas\\s*\\bvarchar\\(\\s*\\b30\\)", ",'YYYYMMDD') AS VARCHAR(8))");
				
				// System.out.println("REPLACED STRING "+sqlstr);
				
				 return sqlstr;
		  }
			   
	     }
	    
	  return "";
	     
}
   
   
   
   public String castAsTimestamp(String sqlstr){

	    if(sqlstr!=null){
	    	
	    	
	    	Pattern p1=Pattern.compile("(?i)\\b[\\s\\r\\n\\W\\w]*\\bcast\\s*\\(\\b[\\s\\r\\n\\W\\w]+\\s*\\bas\\s*\\btimestamp\\)");
			Matcher m1=p1.matcher(sqlstr);
			//System.out.println("sqlstr "+sqlstr);
			System.out.println(m1.find());
		 if(m1.find())
		 {
				
				 sqlstr=sqlstr.replaceAll("(?i)\\bcast\\s*\\(", "CAST(to_date(");
				 sqlstr=sqlstr.replaceAll("(?i)\\bas\\s*\\btimestamp\\)", ",'YYYYMMDD') AS TIMESTAMP)");
				
				 //System.out.println("REPLACED STRING "+sqlstr);
				
				 return sqlstr;
		  }
			   
	     }
	    
	  return "";
	     
}
   
   public String changeSwfStrToChar(String sqlstr){

	    if(sqlstr!=null){
	    	
	    	
	    	Pattern p1=Pattern.compile("(?i)\\b[\\s\\r\\n\\W\\w]*\\bswf\\s*\\_str\\s*\\(");
			Matcher m1=p1.matcher(sqlstr);
			//System.out.println("sqlstr "+sqlstr);
		//	System.out.println(m1.find());
		 if(m1.find())
		 {
				
				
				 sqlstr=sqlstr.replaceAll("(?i)\\bswf\\s*\\_str\\s*\\(", "to_char(");
				
				// System.out.println("REPLACED STRING "+sqlstr);
				
				 return sqlstr;
		  }
			   
	     }
	    
	  return "";
	     
}
   
   
   public String insertBracesInSelectInsideFromClause(String sqlstr){

	    if(sqlstr!=null){
	    	
	    	
	    	Pattern p1=Pattern.compile("(?i)\\b[\\s\\r\\n\\W\\w]*\\bfrom\\s*\\bselect\\b[\\s\\r\\n\\W\\w]+\\bwhere\\b[\\s\\r\\n\\W\\w]+");
			Matcher m1=p1.matcher(sqlstr);
			//System.out.println("sqlstr "+sqlstr);
			//System.out.println(m1.find());
			String subS="";
		 if(m1.find())
		 {
			
			 
				//System.out.println(sqlstr.toLowerCase().contains("where"));
				
				//if(sqlstr.toLowerCase().contains("where"))
				//{
				//	System.out.println("inside if");
				String subSqlstr=sqlstr.toLowerCase().substring(sqlstr.toLowerCase().indexOf("where"));
				subS=subSqlstr.substring(subSqlstr.indexOf(" "), subSqlstr.indexOf("."));
				
				sqlstr=sqlstr.replaceAll("(?i)\\bfrom\\s*\\bselect", "from ( select");
				// System.out.println(subS);
				// System.out.println(Pattern.compile("(?i)\\n*\\b"+subS.trim()+"\\s*\\bwhere").matcher(sqlstr).find());
				sqlstr=sqlstr.replaceAll("(?i)\\n*\\b"+subS.trim()+"\\s*\\bwhere", " ) "+subS.trim()+" where");
				
				//System.out.println(sqlstr);
				return sqlstr;
				}
				
				 
		//  }
			   
	     }
	    
	  return "";
	     
    }
   
   
   public String replaceLengthFunction(String sqlstr){

	    if(sqlstr!=null){
	    	
	    	
	    	Pattern p1=Pattern.compile("(?i)\\b[\\s\\r\\n\\W\\w]*\\b(?!where)\\s*\\blength\\s*\\(\\b(?!replace)");
			Matcher m1=p1.matcher(sqlstr);
			//System.out.println("sqlstr "+sqlstr);
			//System.out.println(m1.find());
			String subS="";
		 if(m1.find())
		 {
				
				
				
				sqlstr=sqlstr.replaceAll("(?i)\\b(?!where)\\s*\\blength\\s*\\(\\b(?!replace|cast)", " fn_length(");
				
				sqlstr=sqlstr.replaceAll("(?i)\\bwhere\\s*\\bfn_length\\s*\\(", "where length(");
				
				
				//System.out.println(sqlstr);
				return sqlstr;
		 }
				
				 
		
			   
	     }
	    
	  return "";
	     
   }
   
   
   
   public String getDiagnosticsStatementSwap(String sqlstr,String sqlstr2){

	    if(sqlstr!=null){
	    	
	    	
	    	Pattern p1=Pattern.compile("(?i)\\bset\\s*\\b[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bSWV_Error\\s*\\;" );
	    			
			Matcher m1=p1.matcher(sqlstr);
			
			Pattern p2=Pattern.compile("(?i)\\bGET\\s*\\bDIAGNOSTICS\\s*\\b[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bROW_COUNT\\s*\\;" );
			
			Matcher m2=p2.matcher(sqlstr2);
			
			//System.out.println("sqlstr "+sqlstr);
		//	System.out.println("inside method"+m1.find());
			
		 if(m1.find()&& m2.find())
		 {
				
				String sub1=sqlstr.substring(sqlstr.toUpperCase().indexOf("SET")+3, sqlstr.toUpperCase().indexOf("="));
				String sub2=sqlstr2.substring(sqlstr2.toUpperCase().indexOf("DIAGNOSTICS")+11,sqlstr2.toUpperCase().indexOf("="));
				
				sqlstr=sqlstr.replaceAll("(?i)\\bset\\s*\\b[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bSWV_Error\\s*\\;","GET DIAGNOSTICS "+sub2.trim()+" = ROW_COUNT ;");
				sqlstr2=sqlstr2.replaceAll("(?i)\\bGET\\s*\\bDIAGNOSTICS\\s*\\b[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bROW_COUNT\\s*\\;", " SET "+sub1.trim()+" = SWV_Error ;");
				//System.out.println(sub1);
				//System.out.println(sub2);
				//System.out.println(sub3);
			//	System.out.println("string"+sqlstr+"\n"+sqlstr2);
				return sqlstr+"\n"+sqlstr2;
		 }
				
				 
		
			   
	     }
	    
	  return "";
	     
  }
  
   
  
   
   public String castingMoneyValue(String sqlstr){

	    if(sqlstr!=null){
	    	//select '$' || CAST(TO_CHAR(FN_TO_MONEY(12345678.90),'mm/dd/yy') as VARCHAR(20))
	    //	select '$' || CAST(CAST(12345678.90 AS DECIMAL(19,4)) AS VARCHAR(20))  from SYSIBM.SYSDUMMY1
	    	Pattern p1=Pattern.compile("(?i)\\bselect\\s*\\'$'\\s*\\||\\s*\\bcast\\s*\\(\\s*\\bcast\\s*\\(\\s*\\d*\\.\\d*\\s*\\bas\\s*\\bdecimal\\s*\\(\\s*\\d{1,}\\s*\\,\\s*\\d{1,}\\s*\\)\\s*\\)\\s*\\bas\\s*\\b[\\s\\r\\n\\w\\W]+\\s*\\)\\s*\\)");
			Matcher m1=p1.matcher(sqlstr);
			//System.out.println("sqlstr "+sqlstr);
			//System.out.println(m1.find());
			if(Pattern.compile("(?i)\\bselect\\s*\\'$'\\s*\\||\\s*\\bcast\\s*\\(\\s*\\bcast\\s*\\(\\s*\\d*\\.\\d*\\s*\\bas\\s*\\bdecimal\\s*\\(\\s*\\d{1,}\\s*\\,\\s*\\d{1,}\\s*\\)\\s*\\)\\s*\\bas\\s*\\b[\\s\\r\\n\\w\\W]+\\s*\\)\\s*\\)").matcher(sqlstr).find())
			{
				//select '$' || COALESCE(CAST(TRIM(VARCHAR_FORMAT(CAST(12345678.90 AS DECIMAL(19,4)),'9,999,999,999,999,999.99')) AS NVARCHAR(20)), '')
			
				//String sub=sqlstr.substring(sqlstr.toLowerCase().indexOf("fn_to_money"), sqlstr.indexOf(")"));
				//System.out.println(sub);
				sqlstr=sqlstr.replaceAll("(?i)\\bcast\\s*\\(\\s*\\bcast\\s*\\(", "COALESCE(CAST(TRIM(VARCHAR_FORMAT(CAST("); 
				sqlstr=sqlstr.replaceAll("(?i)\\bas\\s*\\bdecimal\\s*\\(\\s*\\d{1,}\\s*\\,\\s*\\d{1,}\\s*\\)\\s*\\)\\s*\\bas\\s*\\b[\\s\\r\\n\\w\\W]+\\s*\\)\\s*\\)", " AS DECIMAL(19,4)),'9,999,999,999,999,999.99')) AS NVARCHAR(20)), '')");
				//System.out.println("sqlstr "+sqlstr);
			   
	     }
	    
			return sqlstr;
	    }
	  return sqlstr;
  }
   
      
   
   public String setDayOfWeek(String sqlstr){

	    if(sqlstr!=null){
	    	//[\\s\\r\\n\\W\\w]*\\bset\\b[\\s\\r\\n\\W\\w]*\\bdatefirst\\b[\\s\\r\\n\\W\\w]*\\b1
	    	sqlstr=ToolsUtil.removeToolKeywords(sqlstr);
	    	Pattern p1=Pattern.compile("(?i)\\b\\bset\\s*\\bdatefirst\\s*\\b1;");
			Matcher m1=p1.matcher(sqlstr);
			
			Pattern p2=Pattern.compile("(?i)\\b\\bset\\s*\\bdatefirst\\s*\\b7;");
			Matcher m2=p2.matcher(sqlstr);
			
			//System.out.println("sqlstr "+sqlstr);
			//System.out.println(m2.find()+" condition");
			
			
			//Pattern p3=Pattern.compile("(?i)\\b[\\s\\r\\n\\W\\w]*\\bdeclare\\b[\\s\\r\\n\\W\\w]*\\bcast\\s*\\(\\s*\\(\\s*\\bDAYOFWEEK\\b[\\s\\r\\n\\W\\w]*\\;");
			//Matcher m3=p3.matcher(sqlstr);
			
		if(Pattern.compile("(?i)\\bset\\s*\\bdatefirst\\s*\\b1;").matcher(sqlstr).find()){
			sqlstr=sqlstr.replaceAll("(?i)\\bset\\s*\\bdatefirst\\s*\\b1;", "/*set datefirst 1 removed*/");
			sqlstr=sqlstr.replaceAll("(?i)\\--\\s*\\bsets\\s*\\bthe\\s*\\bbeginning\\s*\\bof\\s*\\bthe\\s*\\bweek\\s*\\bas\\s*\\bmonday", " ");
			sqlstr=sqlstr.replaceAll("(?i)\\bDAYOFWEEK", "DAYOFWEEK_ISO");
			//System.out.println("sqlstr modified "+sqlstr);
			
			return sqlstr;
		}
		
		if(Pattern.compile("(?i)\\bset\\s*\\bdatefirst\\s*\\b7;").matcher(sqlstr).find()){
			sqlstr=sqlstr.replaceAll("(?i)\\bset\\s*\\bdatefirst\\s*\\b7;", "/*set datefirst 7 removed*/");
			sqlstr=sqlstr.replaceAll("(?i)\\--\\s*\\bsets\\s*\\bthe\\s*\\bbeginning\\s*\\bof\\s*\\bthe\\s*\\bweek\\s*\\bas\\s*\\bsunday", " ");
			
		//	System.out.println("sqlstr modified "+sqlstr);
			
			return sqlstr;
		}
		
				 
		
			   
	     }
	    
	  return sqlstr;
	     
 }
   
   
   public String updateWithCaseWhenExists(String sqlstr){
//System.out.println(sqlstr+" in method");
	    if(sqlstr!=null){
	    	
	    	Pattern p1=Pattern.compile("(?i)\\bUPDATE\\s*\\bSESSION.tt_[\\s\\r\\n\\W\\w]+\\bset[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bcase\\s*\\bwhen\\s*\\bexists");
			Matcher m1=p1.matcher(sqlstr);
			//System.out.println("sqlstr "+sqlstr);
			//System.out.println(Pattern.compile("(?i)\\bUPDATE\\s*\\bSESSION.tt_[\\s\\r\\n\\W\\w]+\\bset[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bcase\\s*\\bwhen\\s*\\bexists").matcher(sqlstr).find()+" update session");
			
		 if(Pattern.compile("(?i)\\bUPDATE\\s*\\bSESSION.tt_[\\s\\r\\n\\W\\w]+\\bset[\\s\\r\\n\\W\\w]+\\s*\\=\\s*\\bcase\\s*\\bwhen\\s*\\bexists").matcher(sqlstr).find())
		 {
				
				
				
				sqlstr=sqlstr.replaceAll("(?i)\\bwhen\\s*\\bexists", "when 1 = ");
				
				sqlstr=sqlstr.replaceAll("(?i)\\)\\s*\\bthen", " FETCH FIRST 1 ROWS ONLY) then");
				
				
				//System.out.println(sqlstr);
				return sqlstr;
		 }
				
				 
		
			   
	     }
	    
	  return "";
	     
  } 
   
   
   
   public String modifySelectTopOne(String sqlstr){
//	System.out.println(sqlstr+" in method");
	 	    if(sqlstr!=null){

	 	    	Pattern p1=Pattern.compile("(?i)\\bDECLARE\\s*\\b[\\s\\r\\n\\W\\w]+\\bCURSOR\\s*\\bWITH\\s*\\bRETURN\\s*\\bTO\\s*\\bCLIENT\\s*\\bFOR");
	 			Matcher m1=p1.matcher(sqlstr);
	 			//System.out.println("sqlstr "+sqlstr);
	 			//System.out.println(Pattern.compile("(?i)\\bDECLARE\\s*\\b[\\s\\r\\n\\W\\w]+\\bCURSOR\\s*\\bWITH\\s*\\bRETURN\\s*\\bTO\\s*\\bCLIENT\\s*\\bFOR").matcher(sqlstr).find()+" SELECT TOP 1");
	 			
	 		 if(Pattern.compile("(?i)\\bDECLARE\\s*\\b[\\s\\r\\n\\W\\w]+\\bCURSOR\\s*\\bWITH\\s*\\bRETURN\\s*\\bTO\\s*\\bCLIENT\\s*\\bFOR").matcher(sqlstr).find())
	 		 {
	 				
	 				String sub=sqlstr.substring(sqlstr.toUpperCase().indexOf("IF")+2, sqlstr.toUpperCase().indexOf("IS"));
	 				//System.out.println(sub+" substring");
	 				sqlstr=sqlstr.replaceAll("(?i)\\bFROM\\b", "INTO "+sub.trim()+" FROM");
	 				sqlstr=sqlstr.replaceAll("(?i)\\bDECLARE\\s*\\b[\\s\\r\\n\\W\\w]+\\bCURSOR\\s*\\bWITH\\s*\\bRETURN\\s*\\bTO\\s*\\bCLIENT\\s*\\bFOR", "");
	 				
	 				sqlstr=sqlstr.replaceAll("(?i)\\bBEGIN\\b", "");
	 				sqlstr=sqlstr.replaceAll("(?i)\\;", " FETCH FIRST 1 ROWS ONLY ;");
	 				
	 			//	System.out.println(sqlstr);
	 				return sqlstr;
	 		 }
	 			
	 			   
	 	     }
	 	    
	 	  return sqlstr;
	 	     
	   } 
   
   /*
   
   public String likePattern(String sqlstr){
//		System.out.println(sqlstr+" in method");
		 	    if(sqlstr!=null){

		 	    	Pattern p1=Pattern.compile("(?i)\\bset+\\b[\\s\\r\\n\\w\\W]+\\=\\s+\\.*\\||\\b[\\s\\r\\n\\w\\W]\\||");
		 			Matcher m1=p1.matcher(sqlstr);
		 			System.out.println("sqlstr "+sqlstr);
		 			System.out.println(m1.find());
		 		/*	
		 		if(Pattern.compile("(?i)\\bDECLARE\\s*\\b[\\s\\r\\n\\W\\w]+\\bCURSOR\\s*\\bWITH\\s*\\bRETURN\\s*\\bTO\\s*\\bCLIENT\\s*\\bFOR").matcher(sqlstr).find())
		 		 {
		 				
		 				String sub=sqlstr.substring(sqlstr.toUpperCase().indexOf("IF")+2, sqlstr.toUpperCase().indexOf("IS"));
		 				//System.out.println(sub+" substring");
		 				sqlstr=sqlstr.replaceAll("(?i)\\bFROM\\b", "INTO "+sub.trim()+" FROM");
		 				sqlstr=sqlstr.replaceAll("(?i)\\bDECLARE\\s*\\b[\\s\\r\\n\\W\\w]+\\bCURSOR\\s*\\bWITH\\s*\\bRETURN\\s*\\bTO\\s*\\bCLIENT\\s*\\bFOR", "");
		 				
		 				sqlstr=sqlstr.replaceAll("(?i)\\bBEGIN\\b", "");
		 				sqlstr=sqlstr.replaceAll("(?i)\\;", " FETCH FIRST 1 ROWS ONLY ;");
		 				
		 			//	System.out.println(sqlstr);
		 				return sqlstr;
		 		 }
		 			
		 			   
		 	     }*/
		/* 	    }
		 	  return sqlstr;
		 	     
		 	   
		 	    } 
		    
   
   */
   
	 
   
  // SELECT LISTAGG(NULLIF(RTRIM(C.Display),'') || ',','') WITHIN GROUP(ORDER BY rowid) 
   //INTO V_Reasons 
   //FROM PendingReasons A,PendingReasons A INNER JOIN CodeApplication C ON A.ReasonCode = C.Code WHERE (A.ClaimId = V_ClaimID AND A.TimeLineSeqNum  = CAST(DECODE(V_TimeLineSeqNum,'','0',V_TimeLineSeqNum) AS SMALLINT)); 
   
   
   public String selectInListagg(String sqlstr){
//		System.out.println(sqlstr+" in method");
		 	    if(sqlstr!=null){

		 	    	Pattern p1=Pattern.compile("(?i)\\bselect\\s*\\blistagg\\s*\\(\\s*\\bnullif\\s*\\(\\s*\\brtrim\\s*\\(\\s*\\b[\\s\\r\\n\\w\\W]+\\s*\\bwithin\\s*\\bgroup\\s*\\(\\s*\\border\\s*\\bby\\s*\\b[\\s\\r\\n\\w\\W]+\\s*\\)\\s*\\b[\\s\\r\\n\\w\\W]+");
		 			Matcher m1=p1.matcher(sqlstr);
		 			//System.out.println("sqlstr "+sqlstr);
		 		//	System.out.println(m1.find());
		 			
		 	  if(Pattern.compile("(?i)\\bselect\\s*\\blistagg\\s*\\(\\s*\\bnullif\\s*\\(\\s*\\brtrim\\s*\\(\\s*\\b[\\s\\r\\n\\w\\W]+\\s*\\bwithin\\s*\\bgroup\\s*\\(\\s*\\border\\s*\\bby\\s*\\b[\\s\\r\\n\\w\\W]+\\s*\\)\\s*\\b[\\s\\r\\n\\w\\W]+\\binner\\s*\\bjoin").matcher(sqlstr).find())
		 		 {		//System.out.println(sqlstr);
		 				//System.out.println(sqlstr.toUpperCase().indexOf("JOIN"));
		 				//System.out.println(sqlstr.toUpperCase().indexOf("ON"));
		 		 		String subS1=sqlstr.substring(sqlstr.toUpperCase().indexOf("ORDER"), sqlstr.toUpperCase().indexOf("INTO"));
		 				//System.out.println(subS1);
		 				String subS2=subS1.substring(subS1.toUpperCase().indexOf("BY")+2, subS1.toUpperCase().indexOf(")"));
		 				//System.out.println(subS2);
		 		 		String sub=sqlstr.substring(sqlstr.toUpperCase().indexOf("JOIN")+4, sqlstr.toUpperCase().indexOf("="));
		 				//System.out.println(sub+" substring");
		 				String sub2=sub.substring(0, sub.toUpperCase().indexOf("ON")+5);
		 				//System.out.println("sub2 "+sub2);
		 				String sub3=sub2.trim().substring( sub2.trim().toUpperCase().indexOf(" "));
		 				//System.out.println("sub3 "+sub3);
		 				sqlstr=sqlstr.replaceAll("(?i)\\border\\s*\\bby\\s*\\w+\\s*\\)", "ORDER BY "+sub3.trim()+"."+subS2.trim()+" )");
		 			//	sqlstr=sqlstr.replaceAll("(?i)\\bDECLARE\\s*\\b[\\s\\r\\n\\W\\w]+\\bCURSOR\\s*\\bWITH\\s*\\bRETURN\\s*\\bTO\\s*\\bCLIENT\\s*\\bFOR", "");
		 				
		 			//	sqlstr=sqlstr.replaceAll("(?i)\\bBEGIN\\b", "");
		 				//sqlstr=sqlstr.replaceAll("(?i)\\;", " FETCH FIRST 1 ROWS ONLY ;");
		 				
		 				//System.out.println(sqlstr);
		 				return sqlstr;
		 		 }
		 			
		 			   
		 	     
		 	    }
		 	    return sqlstr;
		 	     
		 	   
		 	    } 
   
   
   public String modifyLengthFunctionForEmptyString(String sqlstr){
//		System.out.println(sqlstr+" in method");
	   
	   
	 //  VALUES Coalesce(cast(REPEAT('0',5 -LENGTH(CAST(V_Record AS nVARCHAR(5)))) as nVARCHAR(5)),'') ||
	 //  COALESCE(CAST(V_Record AS nVARCHAR(30)),'')
	   
	   
	   
		 	    if(sqlstr!=null){

		 	    	Pattern p1=Pattern.compile("(?i)\\bvalues\\s*\\bcoalesce\\s*\\(\\s*\\bcast\\s*\\(\\s*\\brepeat\\s*\\(\\s*\\'\\b0\\'\\s*\\,\\s*\\d\\s*\\-\\s*\\blength\\s*\\(\\s*\\bcast\\s*\\(\\b[\\s\\r\\n\\w\\W]+");
		 			Matcher m1=p1.matcher(sqlstr);
		 			//System.out.println("sqlstr "+sqlstr);
		 			//System.out.println(m1.find());
		 			
	 	  if(Pattern.compile("(?i)\\bvalues\\s*\\bcoalesce\\s*\\(\\s*\\bcast\\s*\\(\\s*\\brepeat\\s*\\(\\s*\\'\\b0\\'\\s*\\,\\s*\\d\\s*\\-\\s*\\blength\\s*\\(\\s*\\bcast\\s*\\(\\b[\\s\\r\\n\\w\\W]+").matcher(sqlstr).find())
		 		 {		
	 		  		//	System.out.println(sqlstr);
		 				//System.out.println(sqlstr.toUpperCase().indexOf("JOIN"));
		 				//System.out.println(sqlstr.toUpperCase().indexOf("ON"));
		 		 		String subS=sqlstr.substring(sqlstr.toUpperCase().indexOf("REPEAT"));
		 			//	System.out.println("subs"+subS);
		 				String subS1=subS.substring(subS.toUpperCase().indexOf("CAST")+4);
		 			//	System.out.println("subs1"+subS1);
		 				String subS2=subS1.trim().substring(1, subS1.toUpperCase().indexOf(" "));
		 			//	System.out.println("subs2"+subS2);
		 				//String subS3=subS1.trim().substring(subS2.trim().toUpperCase().indexOf("(")+2);
		 				//System.out.println(subS3);
		 		 		String sub=sqlstr.substring(0, sqlstr.toUpperCase().indexOf("-")+1);
		 			//	System.out.println(sub+" substring1");
		 				String sub2=sqlstr.substring( sqlstr.toUpperCase().indexOf("LENGTH"));
		 			//	System.out.println("sub2 "+sub2);
		 				sub2=sub2.replaceAll("(?i)\\)\\s*\\bas", " END ) as");
		 				
		 				sqlstr=sub+" CASE "+subS2.trim()+" WHEN '' THEN 1 ELSE "+sub2;
		 				
		 			//	System.out.println(sqlstr);
		 				return sqlstr;
		 		 }
		 			
		 			   
		 	     
		 	    }
		 	    return sqlstr;
		 	     
		 	   
		 	    } 
  
  
   
}
	

