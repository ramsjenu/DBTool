/*
 * FileUpload.java
 *
 * Created on September 30, 2011, 12:37 PM
 */

package com.tcs.tools.business.frontend.dao;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.sun.java.swing.plaf.windows.WindowsTreeUI.ExpandedIcon;
import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.business.fileupload.dao.FileUploadDAO;
//import com.tcs.tools.business.frontend.dto.DSQLTargetMapDataDTO;
import com.tcs.tools.business.frontend.dto.DynamicSQLDataDTO;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;
/**
 * 
 * @author 477780
 */
public class VBFrontEndParseForDSqlDAO {

	/** Creates a new instance of FileUpload */
	public VBFrontEndParseForDSqlDAO() {
	}
	int lFileCount=0;
	FileWriter lFileWriter=null;
	private List lDBConnectionPatternStrList=new ArrayList();
	PreparedStatement lPreparedStatement=null;
	int lQueryCount=0;
	int lTargetMapQueryCount=0;
	//Connection lConnection=null;
	String lTempFilesPath=null;
	String lPathsFileName=null;
	PreparedStatement lInsertVbFuncPreparedStatement=null;
	String lPathForConvertedTempFiles="";
	String lSQLWaysLogFile="";
	String lTargetPathForFrontEndFiles="";
	String lSqlWaysRootPath="";
	PreparedStatement lInsertDsqlTargetPreparedStatement=null;

	/**
	 * @param args
	 *            the command line arguments
	 */
	/**
	 * @param args
	 */
	
	private List prepareDBConnectionPatList(){
		List lDBConnectionPatternStrList=new ArrayList();
			lDBConnectionPatternStrList.add("\\s*DBConnect.SQLExec\\s*\\(");
			lDBConnectionPatternStrList.add("\\s*DBConnect.SQLSvr.ExecNonQuery\\s*\\(");
			lDBConnectionPatternStrList.add("\\s*DBConnect.SQLSvr.ExecDataTable\\s*\\(");
			// DBConnect.SQLSvr.ExecDataTable(sql)
			return lDBConnectionPatternStrList;
	}
	private String getDBConnectionRegEx(){
		List lDBConnectionPatternStrList=prepareDBConnectionPatList();
		String lRegEx="";
			for (int i = 0; i < lDBConnectionPatternStrList.size(); i++) {
				if(i>0){
					lRegEx+="|";
				}
				lRegEx=lRegEx+lDBConnectionPatternStrList.get(i);
		}
		return lRegEx;
	}
	

	public void parseFilesForDynamicSqls(String pProjectId,String pRunSeq,String[] lines, String pfileName,String pFilePath, String pRunningSeqNo,Connection pConnection){

		try {
			lFileCount=1;
			//pConnection=DBConnectionManager.getConnection();			
			
			lPreparedStatement=pConnection.prepareStatement(ToolConstant.INSERT_FRONT_END_DSQL_DETAILS);
			lInsertVbFuncPreparedStatement=pConnection.prepareStatement(ToolConstant.INSERT_DSQL_VB_FUNC_REPLACE_DETAILS);
			
			FileUploadDAO lFileUploadDAO = new FileUploadDAO(); 			
 			//String lPathsFileName="paths.txt";
			pRunSeq= new ProjectModifyDAO().getRunSeq(pProjectId);
			lPathsFileName=ToolsUtil.readProperty("fileForTempFilePaths");
			//String lTempFilesPath="C:\\Praveen\\Temp_Files";
			//lTempFilesPath=ToolsUtil.readProperty("dSqlsTempFilesPath");
			lTempFilesPath=ToolsUtil.readProperty("dSqlsTempFilesPath")+pProjectId+"_"+pRunSeq+"\\";
			FileUploadDownloadUtility.createFolders(lTempFilesPath);
 			lFileWriter=new FileWriter(lPathsFileName,true);
 			
 			String lDSQLVarName="";
 			int pPrevDBConnectIndex=0;
 			lDBConnectionPatternStrList=prepareDBConnectionPatList();
 			Pattern lDBConnectSQLMatchPattern=null; 			
 			lDBConnectSQLMatchPattern=Pattern.compile(getDBConnectionRegEx()); 			
			for (int cnt = 0; cnt < lines.length; cnt++) {	
				if(lines[cnt] == null ||  "".equals(lines[cnt])){
					continue;					
				}
				lDSQLVarName="";
				if(lDBConnectSQLMatchPattern.matcher(lines[cnt].trim()).find()){
					//System.out.println("-------------------------------");
					//System.out.println("lFileCount::->"+lFileCount+":::Line Num::"+cnt+":::::->"+lines[cnt]);
					lDSQLVarName=getDynamicSQLVarName(lines[cnt]);		
					
					getDynamicSQL(pProjectId,pRunSeq,lines ,pPrevDBConnectIndex,cnt,lDSQLVarName, pfileName,pFilePath,pConnection);					
					pPrevDBConnectIndex=cnt;					
				}				
				
			}
			lFileWriter.close();
			lPreparedStatement.executeBatch();
			lInsertVbFuncPreparedStatement.executeBatch();
			pConnection.commit();
			
			
			
		} catch (Exception e) {
			try{
			lFileWriter.close();			
			}catch (IOException ex) {			
				ex.printStackTrace();
			}
			e.printStackTrace();
		}finally{			
			//DBConnectionManager.closeConnection(pConnection);
		}
		
	}
	public String getDynamicSQLVarName(String pParam){
		System.out.println(pParam);
		String lVarName="";
		String lConnectionStatement ="";
		try {
			pParam = pParam.replaceAll("\\s*\\(\\s*", " ( ");
			pParam = pParam.replaceAll("\\s*\\)\\s*", " ) ");
			
			String lTmpStr= pParam.substring(pParam.indexOf("DBConnect."),pParam.length());
			//System.out.println("TmpStr1::->"+lTmpStr);
			lTmpStr= lTmpStr.substring(lTmpStr.indexOf("(")+2,lTmpStr.indexOf(")"));			
			String[] lConnecParamsArr=lTmpStr.split("\\s*,\\s*");
			//System.out.println("TmpStr2::->"+lTmpStr);
			lVarName=lConnecParamsArr[0];
			//System.out.println("lVarName::->" + lVarName);
			/*lConnectionStatement = pParam.substring(
					pParam.indexOf("DBConnect.SQLExec"),
					pParam.indexOf(")", pParam.indexOf("DBConnect.SQLExec")));
			lVarName = lConnectionStatement.substring(
					lConnectionStatement.indexOf("(") + 2,
					lConnectionStatement.indexOf(","));*/
			//System.out.println("lVarName::->" + lVarName);
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return lVarName;
	}
	public void getDynamicSQL(String pProjectId,String pRunSeq,String[] lines ,int pStartIndex,int pEndIndex,String lDSQLVarName,String pSourceFileName,String pSourceFilePath,Connection pConnection){
		lDSQLVarName=lDSQLVarName.replaceAll("\\$", "");
		//System.out.println("lDSQLVarName::->"+lDSQLVarName);
		Pattern lSingleLineCommentPattern=Pattern.compile("(^['].*$)");
		Pattern lDSQLVarMatchPattern=Pattern.compile("\\b"+lDSQLVarName+"[\\$]*\\s*=");
		String lDSQLQuery="";
		String lOrgDSQLQuery="";
		//Patterns to idetify the Query Asiignment to a Variable in vb
		Pattern lDirectAssignmentToVarPattern=Pattern.compile("(\\b"+lDSQLVarName+"\\s*=)");
		Pattern lDirectAssignmentWithDollorPattern=Pattern.compile("(\\b"+lDSQLVarName+"\\s*$\\s*=)");
		Pattern lAssignAndConcatToVarPattern=Pattern.compile("(\\b"+lDSQLVarName+"\\s*=\\s*"+lDSQLVarName+"\\s*&\\s*)");		
		Pattern lAssignAndConcatToVarWithDollorPattern=Pattern.compile("(\\b"+lDSQLVarName+"\\s*$\\s*=\\s*"+lDSQLVarName+"\\s*&\\s*)");
		Pattern lAssignAndConcatToVarUsingPlusPattern=Pattern.compile("(\\b"+lDSQLVarName+"\\s*=\\s*"+lDSQLVarName+"\\s*\\+\\s*)");
		Pattern lAssignAndConcatToVarUsingPlusWithDollorPattern=Pattern.compile("(\\b"+lDSQLVarName+"\\s*$\\s*=\\s*"+lDSQLVarName+"\\s*\\+\\s*)");
		String lVarAssignPatStr="";
		List lLineNumsLst=new ArrayList();//to Preserve Line#s.
		List lLineStartKeywordsLst=new ArrayList();//to Store Assigned VB Varaibles 
		try {
			for (int i = pStartIndex; i <=pEndIndex; i++) {
				if(lDSQLVarMatchPattern.matcher(lines[i].trim()).find() && (!lSingleLineCommentPattern.matcher(lines[i].trim()).find())){
					/*if(!"".equals(lDSQLQuery.trim())){
						lDSQLQuery+="\r\n";
					}	
								
					lDSQLQuery=lDSQLQuery+lines[i];*/
					if(!"".equals(lines[i].replaceAll("("+lDSQLVarName+"|\"|=|\\+)", "").trim())){
						if(!"".equals(lDSQLQuery.trim())){
							lDSQLQuery+="\r\n";
						}
						if(lDirectAssignmentToVarPattern.matcher(lines[i]).find()){
							if(lAssignAndConcatToVarPattern.matcher(lines[i]).find()){							
								lVarAssignPatStr=lDSQLVarName+" = "+lDSQLVarName+" & ";
							}else if(lAssignAndConcatToVarUsingPlusPattern.matcher(lines[i]).find()){							
								lVarAssignPatStr=lDSQLVarName+" = "+lDSQLVarName+" + ";
							}else{
								lVarAssignPatStr=lDSQLVarName+" = ";
							}
							
						}else if(lDirectAssignmentWithDollorPattern.matcher(lines[i]).find()){
							if(lAssignAndConcatToVarWithDollorPattern.matcher(lines[i]).find()){
								lVarAssignPatStr=lDSQLVarName+"$ = "+lDSQLVarName+" & ";
							}else if(lAssignAndConcatToVarUsingPlusWithDollorPattern.matcher(lines[i]).find()){							
								lVarAssignPatStr=lDSQLVarName+"$ = "+lDSQLVarName+" + ";
							}else{
								lVarAssignPatStr=lDSQLVarName+"$ = ";
							}							
						}						
						lDSQLQuery=lDSQLQuery+lines[i];						
						lLineNumsLst.add((i+1)+"");
						lLineStartKeywordsLst.add(lVarAssignPatStr);
					}
					
					//System.out.println("Query ::->"+lines[i]);
				}
				
			}
			
			if(!"".equals(lDSQLQuery.trim())){
				lOrgDSQLQuery=lDSQLQuery.trim();
				String lTmpFileName=pSourceFileName.replaceAll(".vb", "")+"_tmpFile_"+lFileCount+".sql";
				//System.out.println(lFileCount+"::->"+lDSQLQuery);
				
				//Replacing vb Functions like .Item("itemname") , trim(),replace("","")
				//lDSQLQuery=lDSQLQuery.replaceAll("\\s+\"\\s+", "\"");
				lDSQLQuery=lDSQLQuery.replaceAll("\\s+'\\s+", "'");
				lDSQLQuery=replaceVbFunctions(lDSQLQuery,lTmpFileName,pProjectId,pRunSeq,pSourceFilePath,pSourceFileName);
				
				//Modifying query into SqlWays understandable from
				lDSQLQuery=modifyQueryString(lDSQLQuery,lDSQLVarName);				
				/*lDSQLQuery=lDSQLQuery.replaceAll("(\\b"+lDSQLVarName+"\\s*=\\s*"+lDSQLVarName+"\\s*)|(\\b"+lDSQLVarName+"\\s*=)|("+lDSQLVarName+")", " ");
				lDSQLQuery=lDSQLQuery.replaceAll("(\\\"|&|$)", " ");//.replaceAll("\\s+", " ");
*/				//Creating Temporary File which store One DSQL.
				//System.out.println(lFileCount+"::->"+lDSQLQuery);
				
				
				//System.out.println("::::lDSQLQuery:::"+lDSQLQuery);
				lFileCount++;
				//String lTempFilesPath="C:\\Praveen\\Temp_Files";
				//FileWriter lTmpFileWriter=new FileWriter(lPath+lTmpFileName,true);
				//lTmpFileWriter.append(lDSQLQuery);
				//System.out.println("lPath+lTmpFileName::->"+lPath+lTmpFileName);
				//lTmpFileWriter.close();	
				
				//lDSQLQuery=replaceVbFunctions(lDSQLQuery,lTmpFileName,pProjectId,pRunSeq,pSourceFilePath,pSourceFileName);
				writeToFile(lDSQLQuery,lTempFilesPath+lTmpFileName);
				//System.out.println(lTmpFileWriter.toString());
				lFileWriter.append(lTempFilesPath+lTmpFileName);
				lFileWriter.append(",");
				//Adding Dynamic Sql to List
				
				DynamicSQLDataDTO lDynamicSQLDataDTO=new DynamicSQLDataDTO();
				lDynamicSQLDataDTO.setDynamicSqlStr(lDSQLQuery);
				lDynamicSQLDataDTO.setSourceFileName(pSourceFileName);
				lDynamicSQLDataDTO.setSourceFilePath(pSourceFilePath);
				lDynamicSQLDataDTO.setsQLLineNumsLst(lLineNumsLst);
				lDynamicSQLDataDTO.setTargetFileName(lTmpFileName);					
				lDynamicSQLDataDTO.setsQLLineStartKeywordsLst(lLineStartKeywordsLst);				
				lDynamicSQLDataDTO.setFrontEndVarName(lDSQLVarName);					
				lDynamicSQLDataDTO.setInvokedLineNum((pEndIndex+1)+"");
				lDynamicSQLDataDTO.setOrginalDSQLQuery(lOrgDSQLQuery);
				//lDynamicSqlDataList.add(lDynamicSQLDataDTO);
				insertFontEndDsqlData(pProjectId,pRunSeq,lDynamicSQLDataDTO,pConnection);
				
				//System.out.println("lDSQLQuery"+lDSQLQuery);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String modifyQueryString(String pQueryStr, String lDSQLVarName){
		
		StringBuffer lQueryStrBuf=new StringBuffer(pQueryStr);
		String[] lLines=lQueryStrBuf.toString().split("\r\n");
		String lQueryStr="";
		lQueryStrBuf=new StringBuffer();		
		for (int i = 0; i < lLines.length; i++) {
			if(!"".equals(lLines[i].trim())){
				String lTmpStr=lLines[i].trim()+"_DBT_DEL_";
				//lTmpStr=lTmpStr.replaceAll("\\s*\"\\s*_DBT_DEL_", "_DBT_DELD_\""); //Persisting Double Quote
				lTmpStr=lTmpStr.replaceAll("\\s*\"\\s*_DBT_DEL_", "_DBT_DELD_");
				//lTmpStr=lTmpStr.replaceAll("\\s*'\\s*_DBT_DEL_", "_DBT_DEL_'");
				lQueryStrBuf.append(lTmpStr.trim());
				lQueryStrBuf.append("\r\n");
			}
			
		}
		//System.out.println(lQueryStrBuf);
		
		//lQueryStrBuf.append(pQueryStr);
		
		//Seperate Single Quote in Double Quotes  like "exec procaname @var1=" & "'" & vb_var &"'"
		lQueryStr=lQueryStrBuf.toString();	
		lQueryStr=lQueryStr.replaceAll("\"\\s*&\\s*\"'\"\\s*", "'\" ");
		lQueryStrBuf=new StringBuffer(lQueryStr);
		lQueryStr="";
		//handling vb variables which are concatenated between single quotes - Start
		Pattern pattern = Pattern.compile("'\\s*\"\\s*&(.+?)&\\s*\"\\s*'");		
		Matcher matcher = pattern.matcher(lQueryStrBuf);		
		while(matcher.find()){
			//System.out.println(matcher.start()+":::"+matcher.end());		
			//System.out.println(lQueryStrBuf.substring(matcher.start(),matcher.end()));
			lQueryStrBuf=lQueryStrBuf.replace(matcher.start(), matcher.end(), "'_DBT_VAR_S_START"+matcher.group(1).trim()+"_DBT_VAR_S_END'");			
			matcher = pattern.matcher(lQueryStrBuf);	
		}		
				
		//handling vb variables which are concatenated between single quotes - End
		//System.out.println("Step 1:->"+lQueryStrBuf);

		
		//handling Varaiable Concatenated like a string probably using some vb function - Start
		 pattern = Pattern.compile("\"\\s*&(.+?)&\\s*\"");		
		matcher = pattern.matcher(lQueryStrBuf);		
		while(matcher.find()){
			//System.out.println(matcher.start()+":::"+matcher.end());		
			//System.out.println(lQueryStrBuf.substring(matcher.start(),matcher.end()));
			lQueryStrBuf=lQueryStrBuf.replace(matcher.start(), matcher.end(), "_DBT_VAR_CON_START_"+matcher.group(1).trim()+"_DBT_VAR_CON_END_");			
			matcher = pattern.matcher(lQueryStrBuf);	
		}	
		
		//System.out.println("Step 2:->"+lQueryStrBuf);
		lQueryStr=lQueryStrBuf.toString();			
		//handling Varaiable Concatenated like a string probably using some vb function - End
		//Removing VB Var name in Query
		lQueryStr=lQueryStr.replaceAll("(\\b"+lDSQLVarName+"\\s*[\\$]*\\s*=\\s*"+lDSQLVarName+"\\s*[&\\+]+\\s*)|(\\b"+lDSQLVarName+"\\s*[\\$]*\\s*=)", " _DBT_VAR_CONCAT_ ");
		//lQueryStr=lQueryStr.replaceAll("(\\\")", " ");//.replaceAll("\\s+", " ");
		//Normal Delimeter
		lQueryStr=lQueryStr.replaceAll("\\s*,\\s*_DBT_DEL_", "_DBT_DELC_,");
		lQueryStr=lQueryStr.replaceAll("\\s*'\\s*(_DBT_DEL_)", "_DBT_DELS_'");
		lQueryStr=lQueryStr.replaceAll("\\s*,\\s*(_DBT_DELS_)", "_DBT_DELSC_,");
		lQueryStr=lQueryStr.replaceAll("\\s*'\\s*(_DBT_DELC_)", "_DBT_DELCS_'");
		lQueryStr=lQueryStr.replaceAll("\\s*'\\s*(_DBT_DELCS_)", "_DBT_DELCSS_'");
		lQueryStr=lQueryStr.replaceAll("\\s*'\\s*(_DBT_DELSC_)", "_DBT_DELSCS_'");
		//Double Quotes Delimeter
		lQueryStr=lQueryStr.replaceAll("\\s*,\\s*_DBT_DELD_", "_DBT_DELDC_,");
		lQueryStr=lQueryStr.replaceAll("\\s*'\\s*(_DBT_DELD_)", "_DBT_DELDS_'");
		lQueryStr=lQueryStr.replaceAll("\\s*,\\s*(_DBT_DELDS_)", "_DBT_DELDSC_,");
		lQueryStr=lQueryStr.replaceAll("\\s*'\\s*(_DBT_DELDC_)", "_DBT_DELDCS_'");
		lQueryStr=lQueryStr.replaceAll("\\s*'\\s*(_DBT_DELDCS_)", "_DBT_DELDCSS_'");
		lQueryStr=lQueryStr.replaceAll("\\s*'\\s*(_DBT_DELDSC_)", "_DBT_DELDSCS_'");
		
		lQueryStr=lQueryStr.replaceAll("\"\\s*&\\s*\"\\s*'\\s*", "'_DBT_STR_CONCAT_S_");
		lQueryStr=lQueryStr.replaceAll("\"\\s*&\\s*", "_DBT_AND_VAR_1_");
		lQueryStr=lQueryStr.replaceAll("\\s*&\\s*\"", "_DBT_AND_VAR_2_");
		lQueryStr=lQueryStr.replaceAll("\\s*&\\s*", "_DBT_AND_SYM_");
		lQueryStr=lQueryStr.replaceAll("\\s*\\)\\s*_DBT_DEL_", "_DBT_DEL_ )");
		//System.out.println(lQueryStr);
		//lQueryStr=lQueryStr.replaceAll("(\\\")|_DBT_VAR_CONCAT_", " "); // for removing double quotes
		lQueryStr=lQueryStr.replaceAll("_DBT_VAR_CONCAT_\\s*\"", ""); // To remove starting double quote in every line.
		lQueryStr=lQueryStr.replaceAll("_DBT_VAR_CONCAT_\\s*", "");
	
		//To handle other vb text which is in double quotes - start				
		lQueryStrBuf=new StringBuffer(lQueryStr);
		pattern = Pattern.compile("\"(.+?)\"");
		matcher = pattern.matcher(lQueryStrBuf);		
		while(matcher.find()){
			//System.out.println(matcher.start()+":::"+matcher.end());		
			//System.out.println(lQueryStrBuf.substring(matcher.start(),matcher.end()));
			lQueryStrBuf=lQueryStrBuf.replace(matcher.start(), matcher.end(), "_DBT_D_QUOTE_TEXT_START_"+matcher.group(1).trim()+"_DBT_D_QUOTE_TEXT_END_");			
			matcher = pattern.matcher(lQueryStrBuf);	
		}
		lQueryStr=lQueryStrBuf.toString();
		//To handle other vb text which is in double quotes - End
		//System.out.println(lQueryStr);
		//lQueryStr=replaceSingleQuoteTextToVar(lQueryStr);
		return lQueryStr;
		
		
	}
	public void writeToFile(String pContent,String pFullFilePath){
		//System.out.println(":::file going to be created::::");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(pFullFilePath));
			//System.out.println(":::file content::::"+pContent);	
			
			writer.write(pContent);
			writer.close();
            //writer.newLine();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void  insertFontEndDsqlData(String pProjectId,String pRunSeq,DynamicSQLDataDTO pDynamicSQLDataDTO,Connection pConnection){
		try {
			
			//  PROJECT_ID, RUN_SEQ, DSQL_QUERY, SOURCE_FILE_PATH, SOURCE_FILE_NAME, TARGET_FILE_NAME, DSQL_LINE_NUMS_LIST, 
			//DSQL_START_KEYWORDS_LIST, CONVERTION_STATUS, DSQL_INVOKED_LINE_NUM, FRONT_END_VAR_NAME		
			lPreparedStatement.setString(1, pProjectId);
			lPreparedStatement.setString(2, ToolsUtil.replaceZero(pRunSeq));
			lPreparedStatement.setString(3, ToolsUtil.replaceNull(pDynamicSQLDataDTO.getDynamicSqlStr()));
			lPreparedStatement.setString(4, ToolsUtil.replaceNull(pDynamicSQLDataDTO.getSourceFilePath()));
			lPreparedStatement.setString(5, ToolsUtil.replaceNull(pDynamicSQLDataDTO.getSourceFileName()));
			lPreparedStatement.setString(6, ToolsUtil.replaceNull(pDynamicSQLDataDTO.getTargetFileName()));			
			lPreparedStatement.setObject(7,(Object) ToolsUtil.replaceNullList(pDynamicSQLDataDTO.getsQLLineNumsLst()));
			lPreparedStatement.setObject(8,(Object) ToolsUtil.replaceNullList(pDynamicSQLDataDTO.getsQLLineStartKeywordsLst()));
			lPreparedStatement.setObject(9, ToolsUtil.replaceNull(pDynamicSQLDataDTO.getConvertionStatus()));
			lPreparedStatement.setObject(10, ToolsUtil.replaceNull(pDynamicSQLDataDTO.getInvokedLineNum()));
			lPreparedStatement.setObject(11, ToolsUtil.replaceNull(pDynamicSQLDataDTO.getFrontEndVarName()));
			lPreparedStatement.setObject(12, ToolsUtil.replaceNull(pDynamicSQLDataDTO.getOrginalDSQLQuery()));
			lPreparedStatement.addBatch();
			lQueryCount++;
			if(lQueryCount==50){
				lPreparedStatement.executeBatch();
				lInsertVbFuncPreparedStatement.executeBatch();
				pConnection.commit();
				lQueryCount=0;
			}
			
			//lConnection.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public List  getDynamicSqlData(String pProjectId,String pRunSeq,String pSourceFilePath,Connection pConnection){
		System.out.println("inside getDynamicSqlData method for vb ");
		List lDynamicSqlDataList=new ArrayList();
		DynamicSQLDataDTO lDynamicSQLDataDTO=null;
		List lLineNumsList=null;
		List lLineStartKeywordsList=null;
		ResultSet lResultSet= null;
		try {
			//pConnection=DBConnectionManager.getConnection();
			
			//System.out.println("pProjectId::"+pProjectId+":::"+pSourceFilePath);
			lPreparedStatement=pConnection.prepareStatement(ToolConstant.GET_FRONT_END_DSQL_DETAILS);
			lPreparedStatement.setString(1,pProjectId);
			lPreparedStatement.setString(2,"%"+pSourceFilePath.replaceAll("\\\\", "\\\\\\\\")+"%");
			lResultSet= lPreparedStatement.executeQuery();			
			ObjectInputStream objectIn=null;
			byte[] buf=null;
			if(lResultSet!=null){
				while(lResultSet.next()){
					//PROJECT_ID, RUN_SEQ, DSQL_QUERY,SOURCE_FILE_PATH, SOURCE_FILE_NAME, TARGET_FILE_NAME, 
					//DSQL_LINE_NUMS_LIST, DSQL_START_KEYWORDS_LIST, CONVERTION_STATUS, DSQL_INVOKED_LINE_NUM, FRONT_END_VAR_NAME					
					
					//Extracting the Linenums List -start
					buf = lResultSet.getBytes("DSQL_LINE_NUMS_LIST");
					objectIn = null;
					if (buf != null) {
						objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
					}
					lLineNumsList =(List) objectIn.readObject();					
					//System.out.println(lLineNumsList);
					//Extracting StartKwywordsList - start
					buf = lResultSet.getBytes("DSQL_START_KEYWORDS_LIST");
					objectIn = null;
					if (buf != null) {
						objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
					}
					lLineStartKeywordsList =(List) objectIn.readObject();
					//System.out.println(lLineStartKeywordsList);
					//Forming DTO
					lDynamicSQLDataDTO=new DynamicSQLDataDTO();
					lDynamicSQLDataDTO.setDynamicSqlStr(lResultSet.getString("DSQL_QUERY"));
					lDynamicSQLDataDTO.setSourceFileName(lResultSet.getString("SOURCE_FILE_NAME"));
					lDynamicSQLDataDTO.setSourceFilePath(lResultSet.getString("SOURCE_FILE_PATH"));
					lDynamicSQLDataDTO.setsQLLineNumsLst(lLineNumsList);
					lDynamicSQLDataDTO.setTargetFileName(lResultSet.getString("TARGET_FILE_NAME"));					
					lDynamicSQLDataDTO.setsQLLineStartKeywordsLst(lLineStartKeywordsList);				
					lDynamicSQLDataDTO.setFrontEndVarName(lResultSet.getString("FRONT_END_VAR_NAME"));					
					lDynamicSQLDataDTO.setInvokedLineNum(lResultSet.getString("DSQL_INVOKED_LINE_NUM"));
					lDynamicSQLDataDTO.setConvertionStatus(lResultSet.getString("CONVERTION_STATUS"));
					lDynamicSQLDataDTO.setOrginalDSQLQuery(lResultSet.getString("ORIGINAL_DSQL_QUERY"));
					lDynamicSqlDataList.add(lDynamicSQLDataDTO);
					//System.out.println(lResultSet.getString("SOURCE_FILE_PATH")+lResultSet.getString("TARGET_FILE_NAME"));
										
				}				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(lPreparedStatement,lResultSet);
		}
		return lDynamicSqlDataList;
	}
	
	public void clearDynamicSqlData(String pProjectId,String pRunSeq,String pSourceFilePath,Connection pConnection){
		try {
			System.out.println("pProjectId::"+pProjectId);			
						
			lPreparedStatement=pConnection.prepareStatement("DELETE FROM front_end_dsql_details_table WHERE PROJECT_ID=? AND SOURCE_FILE_PATH like ?");
			lPreparedStatement.setString(1,pProjectId);
			lPreparedStatement.setString(2,"%"+pSourceFilePath.replaceAll("\\\\", "\\\\\\\\")+"%");
			System.out.println("Records Deleted ::: "+lPreparedStatement.executeUpdate());
			lPreparedStatement=pConnection.prepareStatement("DELETE FROM dsql_vb_func_replace_table WHERE PROJECT_ID=? AND SOURCE_FILE_PATH like ?");
			lPreparedStatement.setString(1,pProjectId);
			lPreparedStatement.setString(2,"%"+pSourceFilePath.replaceAll("\\\\", "\\\\\\\\")+"%");
			System.out.println("Records Deleted ::: "+lPreparedStatement.executeUpdate());
			
			lPreparedStatement=pConnection.prepareStatement("DELETE FROM front_end_dsql_target_map_table WHERE PROJECT_ID=? AND SOURCE_FILE_PATH like ?");
			lPreparedStatement.setString(1,pProjectId);
			lPreparedStatement.setString(2,"%"+pSourceFilePath.replaceAll("\\\\", "\\\\\\\\")+"%");
			System.out.println("Records Deleted ::: "+lPreparedStatement.executeUpdate());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//DBConnectionManager.closeConnection(pConnection);
		}
		
	}
	
	public String replaceVbFunctions(String pQueryStr,String pTmpFileName,String pProjectId,String pRunSeq,String pSourceFilePath,String pSourceFileName){
		//replacing open & close braces in DatabaseObjects.ReturnStoredProcName()
		//beacuse it should get captured as it is, other wise this ll be assumed as vb function.
		pQueryStr= pQueryStr.trim();
		if(pQueryStr.trim().lastIndexOf(")")==pQueryStr.length()-1){
			pQueryStr=pQueryStr.replaceAll("DatabaseObjects.ReturnStoredProcName\\s*\\(", "DatabaseObjects.ReturnStoredProcName_Open_Brace_");		
			pQueryStr=pQueryStr.substring(0,pQueryStr.lastIndexOf(")"))+" _Close_Brace_";
		}
		//System.out.println(pQueryStr);
		String[] lQuryLinesArr=pQueryStr.split("\\r\\n");
		String lResQueryStr="";
		String pQueryLineStr="";
		int lFunctionCount=1;
		String lReplaceStr="";
		int lChkOpenBrace=0;
		boolean lchkPrevious=false;
		
		try {
			for (int i = 0; i < lQuryLinesArr.length; i++) {
				pQueryLineStr=lQuryLinesArr[i];
				pQueryLineStr=pQueryLineStr.replaceAll("\\s*\\.\\s*", ".");
				pQueryLineStr=pQueryLineStr.replaceAll("\\(", " ( ");
				pQueryLineStr=pQueryLineStr.replaceAll("\\)", " ) ");
				pQueryLineStr=pQueryLineStr.replaceAll("=", " = ");
				pQueryLineStr=pQueryLineStr.replaceAll(",", " , ");
				
				//To remove space at , in vb funcitons. - Start
				String[] lTempArr=pQueryLineStr.split("\\s+");
				pQueryLineStr="";
				for (int j = 0; j < lTempArr.length; j++) {
					if(lTempArr[j].trim().equalsIgnoreCase("(")){
						pQueryLineStr=pQueryLineStr.trim()+lTempArr[j].trim();
						lChkOpenBrace++;
						continue;
					}
					if(lTempArr[j].trim().equalsIgnoreCase(")")){
						pQueryLineStr=pQueryLineStr.trim()+lTempArr[j].trim()+" ";
						lChkOpenBrace--;
						continue;
					}
					if( ( lTempArr[j].trim().equalsIgnoreCase(",")|| lTempArr[j].trim().equalsIgnoreCase("\"") 
							|| lTempArr[j].trim().equalsIgnoreCase("'") || lTempArr[j].trim().equalsIgnoreCase("=") ) 
							&& (lChkOpenBrace>0)){						
						
						pQueryLineStr=pQueryLineStr.trim()+lTempArr[j].trim();
						lchkPrevious=true;
						continue;
					}
					if(lchkPrevious==true){
						pQueryLineStr=pQueryLineStr+lTempArr[j];
						lchkPrevious=false;
					}else{
						pQueryLineStr=pQueryLineStr+lTempArr[j]+" ";
					}
					
					
				}
				
				//To remove space at , in vb funcitons. - End
				
				pQueryLineStr=pQueryLineStr.replaceAll("\\(\\s+", "(");
				pQueryLineStr=pQueryLineStr.replaceAll("\\s+\\)", ")");
				pQueryLineStr=pQueryLineStr.replaceAll("\\s*\\.\\s*", ".");				
				//System.out.println(pQueryLineStr);
				
				String lOueryArr[]=pQueryLineStr.split("\\s+");
				
				for (int j = 0; j < lOueryArr.length; j++) {
					//Pattern to chk any parameter starting with digit 
					//because any text(varname or plain text) starts with digit should be in single quotes other wise sqlways wont accept it
					Pattern lStartsWithDigitPattern=Pattern.compile("^\\d+");
					if( (lOueryArr[j].contains("(")&lOueryArr[j].contains(")")) || (lStartsWithDigitPattern.matcher(lOueryArr[j].trim()).find())){
						if(lStartsWithDigitPattern.matcher(lOueryArr[j].trim()).find()){
							lReplaceStr="_DBT_START_DIGIT_";
						}else{
							lReplaceStr="_DBT_VB_FUNC_";
						}
						lReplaceStr=lReplaceStr+pTmpFileName.replaceAll("\\.sql", "").trim()+"_"+lFunctionCount+"_";
						lFunctionCount=lFunctionCount+1;
						lResQueryStr=lResQueryStr+" "+lReplaceStr;
						//Keeping Original Data in Table - Insert - Start
						//PROJECT_ID, RUN_SEQ, SOURCE_FILE_PATH, SOURCE_FILE_NAME, temp_FILE_NAME, DSQL_VAR, REPLACED_CONST
						lInsertVbFuncPreparedStatement.setString(1,pProjectId);
						lInsertVbFuncPreparedStatement.setString(2,pRunSeq);
						lInsertVbFuncPreparedStatement.setString(3,pSourceFilePath);
						lInsertVbFuncPreparedStatement.setString(4,pSourceFileName);
						lInsertVbFuncPreparedStatement.setString(5,pTmpFileName);
						lInsertVbFuncPreparedStatement.setString(6,lOueryArr[j]);
						lInsertVbFuncPreparedStatement.setString(7,lReplaceStr);
						lInsertVbFuncPreparedStatement.addBatch();
						// Insert - End
					}else{
						lResQueryStr=lResQueryStr+" "+lOueryArr[j];
					}
					
				}
				lResQueryStr=lResQueryStr+"\r\n";
			}
			//System.out.println(lResQueryStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Placing actual braces in query string.
		lResQueryStr=lResQueryStr.replaceAll("_Open_Brace_", "(");
		lResQueryStr=lResQueryStr.replaceAll("_Close_Brace_", ")");
		return lResQueryStr;
		//System.out.println(lResQueryStr);
	}
	public HashMap getReplacedVbFuncData(String pProjectId,String pRunSeq,String pSourceFilePath,Connection pConnection){
		System.out.println("inside getReplacedVbFuncData for vb");
		PreparedStatement lGetVbFuncDataPreparedStatement=null;
		ResultSet lResultSet=null;
		HashMap lVBFuncConstMap=new HashMap();
		try {
			//PROJECT_ID, RUN_SEQ, SOURCE_FILE_PATH, SOURCE_FILE_NAME, temp_FILE_NAME, ORGINAL_QUERY_STR, REPLACED_CONST
			lGetVbFuncDataPreparedStatement=pConnection.prepareStatement(ToolConstant.GET_DSQL_VB_FUNC_REPLACE_DETAILS);
			lGetVbFuncDataPreparedStatement.setString(1, pProjectId);
			lGetVbFuncDataPreparedStatement.setString(2, "%"+pSourceFilePath.replaceAll("\\\\", "\\\\\\\\")+"%");
			lResultSet=lGetVbFuncDataPreparedStatement.executeQuery();
			if(lResultSet!=null){
				while(lResultSet.next()){
					lVBFuncConstMap.put(lResultSet.getString("REPLACED_CONST"),lResultSet.getString("ORGINAL_QUERY_STR"));
					//System.out.println(lResultSet.getString("ORGINAL_QUERY_STR")+lResultSet.getString("REPLACED_CONST"));
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lVBFuncConstMap;
	}
	
	public void setAllRequiredPaths(){
		lPathsFileName=ToolsUtil.readProperty("fileForTempFilePaths");
		lPathForConvertedTempFiles=ToolsUtil.readProperty("pathForConvertedTempFiles");
		lSQLWaysLogFile=ToolsUtil.readProperty("sqlWaysLogFile");
		lTargetPathForFrontEndFiles=ToolsUtil.readProperty("targetPathForFrontEndFiles");
		lSqlWaysRootPath=ToolsUtil.readProperty("sqlWaysRootPath");
		lTempFilesPath=ToolsUtil.readProperty("dSqlsTempFilesPath");
	}
	
	public void readConvertedTmpFiles(String pProjectId,String pRunSeq,String pSourceFilePath,String pRootPath,Connection pConnection ){
		setAllRequiredPaths();
		List lDynamicSqlDataList=getDynamicSqlData(pProjectId,pRunSeq,pSourceFilePath,pConnection);
		HashMap lVBFuncConstMap=getReplacedVbFuncData(pProjectId,pRunSeq,pSourceFilePath,pConnection);
		
		if(lDynamicSqlDataList==null || lDynamicSqlDataList.size()==0){
			return;
		}
		List lConvertedFilesList=readSqlWyasLog();		
		
		String lTmpFileName="";
		DynamicSQLDataDTO lDynamicSQLDataDTO=null;		
		List pSourceLinesLst=null;
		String lPrevSourceFileName="";
		List lSourceLineNumsList=null;
		List lSourceLineStartKeywordsList=null;		
		String[] lConvertedDsqlLines=null;
		String pConvertedQuery="";
		System.out.println("Tmp File Count::"+lConvertedFilesList.size());	
		System.out.println("DSQL List Count::"+lDynamicSqlDataList.size());	
		Pattern lCallPattern=Pattern.compile("(?i)^\\s*\\bCALL\\b\\s+");
		int lLineNumDiff=0;
		try {
			lInsertDsqlTargetPreparedStatement=pConnection.prepareStatement(ToolConstant.INSERT_FRONT_END_DSQL_TARGET_MAP_DATA);
			for (int i = 0; i < lDynamicSqlDataList.size()+1; i++) {
				if(i==lDynamicSqlDataList.size()){
					lDynamicSQLDataDTO=(DynamicSQLDataDTO)lDynamicSqlDataList.get(i-1);
				}else{
					lDynamicSQLDataDTO=(DynamicSQLDataDTO)lDynamicSqlDataList.get(i);
				}
				
				//System.out.println(":::lDynamicSQLDataDTO Filename::"+lDynamicSQLDataDTO.getTargetFileName());
				
				//Reading Source File - Start
				if((!(lPrevSourceFileName.equalsIgnoreCase(lDynamicSQLDataDTO.getSourceFileName()))) || (i==lDynamicSqlDataList.size()) ){
					if(i>0){
						
						//String lRootPath="C:\\Praveen\\bck\\1\\praveen\\Source";
						//"C:\\Praveen\\bck\\1\\praveen\\Target\\files\\1.vb";
						//String pSourceFilePath ="C:\\Praveen\\bck\\1\\praveen\\Source\\files\\1.vb";
						//System.out.println("----------------------------------");
						String pSourceFullFilePath=pSourceFilePath+"\\"+lPrevSourceFileName;
						//System.out.println("Source File Path::->"+pSourceFullFilePath);
						//System.out.println("Source Root Path::->"+pRootPath);
						String lSourceRootParent=ToolsUtil.splitFileNameAndPath(pRootPath)[0];
						//System.out.println("lSourceRootParent:::->"+lSourceRootParent);
						
						String lTargetRootPath=lSourceRootParent+"Target";
						//System.out.println("lTargetRootPath:::->"+lTargetRootPath);
						
						String lSourceFileParent=ToolsUtil.splitFileNameAndPath(pSourceFullFilePath)[0];
						//System.out.println("lSourceFileParent:::->"+lSourceFileParent);
						
									
						/*lRootPath=lRootPath.replaceAll("\\\\", "\\\\\\\\");
						System.out.println(lRootPath);*/
						//System.out.println(lSourceFileParent.contains(lRootPath));
						
						String lTargetFileParentDir=StringUtils.replace(lSourceFileParent,pRootPath.replaceAll("\\\\", "\\\\\\\\"),lTargetRootPath);			
						//System.out.println("TargetFileParent::->"+lTargetFileParentDir);
						
						
						File lFile=new File(lTargetFileParentDir);
						boolean chk=lFile.mkdirs();
						//System.out.println(lTargetFileParentDir+"  ::Created--> "+chk);
						//System.out.println("prevsourcefilename  "+lPrevSourceFileName);
						//FileWriter lTargetFileWriter=new FileWriter(lTargetPathForFrontEndFiles+lPrevSourceFileName,false);
						lFile=new File(lTargetRootPath+lPrevSourceFileName);
						if(lFile.exists()){
							lFile.delete();						
						}
						
						FileWriter lTargetFileWriter=new FileWriter(lTargetFileParentDir+lPrevSourceFileName,false);						
						for (int j = 0; j < pSourceLinesLst.size(); j++) {
							lTargetFileWriter.append((String)pSourceLinesLst.get(j));
							if(j<pSourceLinesLst.size()-1){
								lTargetFileWriter.append("\r\n");
							}
							
						}
						lTargetFileWriter.close();
						System.out.println("File Generated:::->"+lPrevSourceFileName);
					}
					if(i==lDynamicSqlDataList.size()){
						int[] lResArr=lInsertDsqlTargetPreparedStatement.executeBatch();						
						pConnection.commit();
						return;
					}
					pSourceLinesLst=readSourceFileData(lDynamicSQLDataDTO.getSourceFilePath(),lDynamicSQLDataDTO.getSourceFileName());
					System.out.println("Source File :: "+lDynamicSQLDataDTO.getSourceFileName()+" Size::"+pSourceLinesLst.size());
					lLineNumDiff=0;					
				}
				
				//Reading Source File - End		
				for (int j = 0; j < lConvertedFilesList.size(); j++) {		
					lTmpFileName=((String)lConvertedFilesList.get(j)).trim();				
					if(lDynamicSQLDataDTO.getTargetFileName().trim().equalsIgnoreCase(lTmpFileName)){
						lDynamicSQLDataDTO.setConvertionStatus("OK");
						//System.out.println("MatchedlTmpFileName::->"+lTmpFileName);
						//Reading SQL Wyas Converted Output - Start
						//File lTmpConvertedFile=new File("C:\\Praveen\\Outputs\\"+lTmpFileName);
						File lTmpConvertedFile=new File(lPathForConvertedTempFiles+lTmpFileName);
						InputStream is = new FileInputStream(lTmpConvertedFile);
						StringBuffer buffer = new StringBuffer();
						byte[] b = new byte[4096];
						for (int n; (n = is.read(b)) != -1;) {
							
							buffer.append(new String(b, 0, n));
						}
						String lConvertedDSQLQueryStr = buffer.toString();
						//System.out.println("CONVERTEDQUERYSTRING::::::::::  "+lConvertedDSQLQueryStr);
						lConvertedDSQLQueryStr=lConvertedDSQLQueryStr.replaceAll("\"|(\\r\\n)|;", "");
						//System.out.println("CONVERTEDQUERYSTRING222222::::::::::  "+lConvertedDSQLQueryStr);
						
						//Reading SQL Ways Converted Output - End
						/******************************************/
						
						//Removing the added Constant Variables like DBT_S_VAR OR DB_VAR_CON-Start
						lConvertedDSQLQueryStr=removeTempVarConstants(lConvertedDSQLQueryStr);
						//Removing the added Constant Variables like DBT_S_VAR OR DB_VAR_CON-End
						
						/******************************************/						
						//Removing Delimiter Constants -Start
						/*System.out.println("--------------------------l");
						System.out.println("lConvertedDSQLQueryStr"+lConvertedDSQLQueryStr);*/
						lConvertedDSQLQueryStr=removeDelimeters(lConvertedDSQLQueryStr);
						//System.out.println("lConvertedDSQLQueryStr::->"+lConvertedDSQLQueryStr);					
						//Removing Delimiter Constants -End
						
						/******************************************/
						//Replces the Constants with the original vb functions -start
						lConvertedDSQLQueryStr=ReplaceConstsByOriginalVBFunctions(lVBFuncConstMap,lConvertedDSQLQueryStr);
						//Replces the Constants with the original vb functions -End
						/******************************************/
						
						//Splitting into Lines Using Delimeter Logic -Start
						//System.out.println("-------------"+lTmpFileName+"-----------------");
						
						//System.out.println(lConvertedDSQLQueryStr);
						//lConvertedDSQLQueryStr=lConvertedDSQLQueryStr.replaceAll("call[\\s\\r\\n\\w\\W]+_DBT_DEL_\\s*\\(","( _DBT_DEL_");
						pConvertedQuery=lConvertedDSQLQueryStr.replaceAll("_DBT_DEL_", "\r\n");
						lConvertedDSQLQueryStr=lConvertedDSQLQueryStr.replaceAll("(?i)\"_DBT_DEL_\\s*\\(","(\" _DBT_DEL_");
						//pConvertedQuery=lConvertedDSQLQueryStr.replaceAll("_DBT_DEL_", "\r\n");
						//lConvertedDSQLQueryStr=lConvertedDSQLQueryStr.trim().replaceAll("_DBT_DEL_[\\s\\r\\n]*\\)$"," _DBT_DEL_");
						//System.out.println("lConvertedDSQLQueryStr::->"+lConvertedDSQLQueryStr);
						if(lCallPattern.matcher(lConvertedDSQLQueryStr).find()){
							//Shifted to here from above because only for call statements v hv to remove and brace.
							lConvertedDSQLQueryStr=lConvertedDSQLQueryStr.trim().replaceAll("_DBT_DEL_[\\s\\r\\n]*\\)$"," _DBT_DEL_");
							//Call {0}.dcms_FMLA_GetYesNo (2
							lConvertedDSQLQueryStr=lConvertedDSQLQueryStr.replaceAll("(?i)\\bCall\\b\\s*", "CALL {0}.");
						}
						
						lConvertedDsqlLines=lConvertedDSQLQueryStr.trim().split("\\s*_DBT_DEL_[\\s\\r\\n]*");
						/*for (int k = 0; k < lConvertedDsqlLines.length; k++) {
							System.out.println(lConvertedDsqlLines[k]);
						}						*/
						//Splitting into Lines Using Delimiter Logic -End
						
						//System.out.println(_fileDateStr);
						
					}//if
					
				}//j end
				
				//Placing Converted Sqls in Source File-Start
				if(lDynamicSQLDataDTO.getConvertionStatus().trim().equalsIgnoreCase("OK")){								
					lSourceLineNumsList=lDynamicSQLDataDTO.getsQLLineNumsLst();
					lSourceLineStartKeywordsList=lDynamicSQLDataDTO.getsQLLineStartKeywordsLst();
					/*System.out.println("File Name ::"+lDynamicSQLDataDTO.getSourceFileName()+" ::Temp File:::->"+lDynamicSQLDataDTO.getTargetFileName());
					System.out.println("lSourceLineStartKeywordsList Size:::"+lSourceLineStartKeywordsList.size());
					System.out.println("lSourceLineNumsList Size:::"+lSourceLineNumsList.size());				
					System.out.println(lSourceLineNumsList);
					System.out.println("lConvertedDsqlLines Size:::"+lConvertedDsqlLines.length);*/
					/*for (int j = 0; j < lSourceLineStartKeywordsList.size(); j++) {
						System.out.println(lSourceLineStartKeywordsList.get(j));
					}*/
					//System.out.println("SOURCELINENUMS:::::::::: "+lSourceLineNumsList.size());
					int s1=0;
					int s2=0;
					int s3=0;
					int s4=0;
					int s5=0;
					int s6=0;
					//System.out.println("lConvertedDsqlLines.length  "+lConvertedDsqlLines.length);
					//System.out.println("lSourceLineStartKeywordsList.size()  "+lSourceLineStartKeywordsList.size());
					//System.out.println("lSourceLineNumsList.size()  "+lSourceLineNumsList.size());
					/*if(lConvertedDsqlLines.length>lSourceLineStartKeywordsList.size()){
						System.out.println("inside if 1");
						s1=lConvertedDsqlLines.length-lSourceLineStartKeywordsList.size();
						for(int i1=lSourceLineStartKeywordsList.size();i1<lSourceLineStartKeywordsList.size()+s1;i1++){
							lSourceLineStartKeywordsList.add(i1, " ");
						}
					}
					else if(lConvertedDsqlLines.length>lSourceLineNumsList.size()){
						System.out.println("inside if 2");
						s2=lConvertedDsqlLines.length-lSourceLineNumsList.size();
						for(int i1=lSourceLineNumsList.size();i1<lSourceLineNumsList.size()+s2;i1++){
							lSourceLineNumsList.add(i1, " ");
						}
					}*/
					if(lSourceLineStartKeywordsList.size()>lConvertedDsqlLines.length){	
					//	System.out.println("inside if 3");
						s3=lSourceLineStartKeywordsList.size()-lConvertedDsqlLines.length;
						//System.out.println("S3 "+s3);
						lConvertedDsqlLines=ToolsUtil.expand(lConvertedDsqlLines,lConvertedDsqlLines.length+s3);
						/*for(int i1=lConvertedDsqlLines.length;i1<lConvertedDsqlLines.length+s3-1;i1++){
							System.out.println("inside for");
							System.out.println("i1"+i1);
							lConvertedDsqlLines[i1]=" ";
							System.out.println("lConvertedDsqlLines[i1] "+lConvertedDsqlLines[i1]);
						}*/
						//System.out.println("lConvertedDsqlLines.length new1 "+lConvertedDsqlLines.length);
					}
					/*else if(lSourceLineStartKeywordsList.size()>lSourceLineNumsList.size())
					{	System.out.println("inside if 4");
						s4=lSourceLineStartKeywordsList.size()-lSourceLineNumsList.size();
						for(int i1=lSourceLineNumsList.size();i1<lSourceLineNumsList.size()+s4;i1++){
							lSourceLineNumsList.add(i1, " ");
						}
					}*/
					else if(lSourceLineNumsList.size()>lConvertedDsqlLines.length)
					{	//System.out.println("inside if 5");
						s5=lSourceLineNumsList.size()-lConvertedDsqlLines.length;
						lConvertedDsqlLines=ToolsUtil.expand(lConvertedDsqlLines,lConvertedDsqlLines.length+s5);
						/*for(int i1=lConvertedDsqlLines.length;i1<lConvertedDsqlLines.length+s5;i1++){
							lConvertedDsqlLines[i1]=" ";
						}
						System.out.println("lConvertedDsqlLines.length  new2"+lConvertedDsqlLines.length);*/
					}
					/*else if(lSourceLineNumsList.size()>lSourceLineStartKeywordsList.size())
					{	System.out.println("inside if 6");
						s6=lSourceLineNumsList.size()-lSourceLineStartKeywordsList.size();
						for(int i1=lSourceLineStartKeywordsList.size();i1<lSourceLineStartKeywordsList.size()+s6;i1++){
							lSourceLineStartKeywordsList.add(i1, " ");
						}
						System.out.println();
					}*/
					
					
				//	for (int k = 0; k < lConvertedDsqlLines.length; k++) {
					for (int j = 0; j < lSourceLineStartKeywordsList.size(); j++) {
						
						int lLineNum= Integer.parseInt((String)lSourceLineNumsList.get(j));
						lLineNum=lLineNum+lLineNumDiff;
						//System.out.println("LINENUM:::: "+lLineNum);
						String lSourceLineStr=(String)pSourceLinesLst.get(lLineNum-1);
						//System.out.println(lSourceLineStr+"   sourcelinestr");
						//System.out.println(lSourceLineStr.indexOf(lDynamicSQLDataDTO.getFrontEndVarName().trim()));
						String lTabSpace="";
						String lNewLine="";
						if((lSourceLineStr.indexOf(lDynamicSQLDataDTO.getFrontEndVarName().trim())!=-1)/*&& (lConvertedDsqlLines[j].trim()!="")*/){
						 lTabSpace=lSourceLineStr.substring(0,lSourceLineStr.indexOf(lDynamicSQLDataDTO.getFrontEndVarName().trim()));
						 //System.out.println("lTabSpace  "+lTabSpace);
						// System.out.println("(String)lSourceLineStartKeywordsList.get(j)   "+(String)lSourceLineStartKeywordsList.get(j));
						// System.out.println("lConvertedDsqlLines[j].trim()  "+lConvertedDsqlLines[j].trim());
						 lNewLine=lTabSpace+((String)lSourceLineStartKeywordsList.get(j)).trim()+"\""+lConvertedDsqlLines[j].trim();
						// System.out.println("NEWLINE::::::::: "+lNewLine);
						}
						//System.out.println(lTabSpace.length());	}					
						//String lNewLine=lTabSpace+((String)lSourceLineStartKeywordsList.get(j)).trim()+"\""+lConvertedDsqlLines[j].trim();						
						pSourceLinesLst.set(lLineNum-1,lNewLine);						
						pSourceLinesLst.add(lLineNum-1,lTabSpace+"'"+lSourceLineStr.trim());
					//	System.out.println("SOURCELINELIST:::::: "+pSourceLinesLst);
						lLineNumDiff=lLineNumDiff+1;
					//	System.out.println("VALUE OF J::::::::"+j);
					//	if( j==lSourceLineNumsList.size()-1 && lCallPattern.matcher(lConvertedDsqlLines[0].trim().toUpperCase()).find()){	
					//	System.out.println("lConvertedDsqlLines size::::;"+lConvertedDsqlLines.length );
						if( j==lSourceLineNumsList.size()-1 && lCallPattern.matcher(lConvertedDsqlLines[j].trim().toUpperCase()).find()){							
							lLineNum=Integer.parseInt((String)lDynamicSQLDataDTO.getInvokedLineNum());
							lLineNum=lLineNum+lLineNumDiff;
							lNewLine=lDynamicSQLDataDTO.getFrontEndVarName()+" = "+lDynamicSQLDataDTO.getFrontEndVarName()+" & \")\"";
							//System.out.println("NEWLINE222222222222222::::::::: "+lNewLine);
							pSourceLinesLst.add(lLineNum-1,lTabSpace+lNewLine);
							lLineNumDiff=lLineNumDiff+1;
						}
						
				//	}
				}
					insertDSqlTargetMapData(pProjectId,pRunSeq,lDynamicSQLDataDTO,pConvertedQuery,"Ok",pConnection);
					
				}else{
					//System.out.println(lDynamicSQLDataDTO.getTargetFileName()+"--- Failed:: Line no:"+lDynamicSQLDataDTO.getInvokedLineNum());
					lSourceLineNumsList=lDynamicSQLDataDTO.getsQLLineNumsLst();
					lSourceLineStartKeywordsList=lDynamicSQLDataDTO.getsQLLineStartKeywordsLst();
					for (int j = 0; j < lSourceLineNumsList.size(); j++) {
						int lLineNum= Integer.parseInt((String)lSourceLineNumsList.get(j));						
						lLineNum=lLineNum+lLineNumDiff;						
						pSourceLinesLst.set(lLineNum-1,((String)pSourceLinesLst.get(lLineNum-1))+"  'Not Converted");
						//pSourceLinesLst[lLineNum-1]=pSourceLinesLst[lLineNum-1]+"  'Not Converted";
					}
					pConvertedQuery="";
					insertDSqlTargetMapData(pProjectId,pRunSeq,lDynamicSQLDataDTO,pConvertedQuery,"Failed",pConnection);
				}//end if
				
				//Placing Converted Sqls in Source File-End
				
			//	System.out.println("FINAL LIST IN METHOD::::: "+pSourceLinesLst);
				lPrevSourceFileName=lDynamicSQLDataDTO.getSourceFileName();
			}//i end
			
			//Generating  Output File -start
			//FileWriter lOutFileWriter=new FileWriter("C:\\Praveen\\Tool Data\\inputs\\Outputs\\"+lDynamicSQLDataDTO.getSourceFileName(),false);
			/*FileWriter lTargetFileWriter=new FileWriter(lTargetPathForFrontEndFiles+lDynamicSQLDataDTO.getSourceFileName(),false);
			for (int j = 0; j < pSourceLinesLst.size(); j++) {
				lTargetFileWriter.append((String)pSourceLinesLst.get(j));
				if(j<pSourceLinesLst.size()-1){
					lTargetFileWriter.append("\r\n");
				}
				
			}
			lTargetFileWriter.close();
			*/
			//System.out.println("FINAL LIST IN METHOD111::::: "+pSourceLinesLst);
			//System.out.println("Output Files Generation Completed");
			//Generating  Output File -End
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public List readSqlWyasLog(){
		System.out.println("inside readSqlWyasLog for vb");
		File lFile=null;
		List lFileNamesLst=new ArrayList();
		try {			
			//lFile=new File("C:\\Praveen\\Outputs\\sqlways.log");
			lFile=new File(lPathForConvertedTempFiles+lSQLWaysLogFile);
			InputStream is = new FileInputStream(lFile);
			StringBuffer buffer = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = is.read(b)) != -1;) {
				
				buffer.append(new String(b, 0, n));
			}			
			String _logDatStr = buffer.toString();			
			//System.out.println(str);
			//Reading Files List from Log File
			
			String lTmpStr=_logDatStr.substring(_logDatStr.indexOf("Reading scripts ("),_logDatStr.indexOf(")", _logDatStr.indexOf("Reading scripts (")));
			lTmpStr=lTmpStr.replaceAll("Reading scripts \\(|template ", "");
			
			
			//System.out.println("lTMPSTR   "+lTmpStr);
			String[] lFileNamesArr=lTmpStr.split("\\s*,\\s*");			
			for (int i = 0; i < lFileNamesArr.length; i++) {
				//lFileNamesArr[i]=lFileNamesArr[i].substring(lFileNamesArr[i].indexOf("_tmpFile_")+1,lFileNamesArr[i].length()).replaceAll("_sql", ".sql");
				String lTmpFileName=lFileNamesArr[i];
				lTmpFileName=lTmpFileName.replaceAll("C_DBTransplant_Temp_Files_", "");
				lTmpFileName=lTmpFileName.substring(lTmpFileName.indexOf("_")+1);
				lTmpFileName=lTmpFileName.substring(lTmpFileName.indexOf("_")+1);
				
				String lTxtToReplace="\\";
				//lTempFilesPath=lTempFilesPath.substring(-1, lTempFilesPath.indexOf("_"));
				String lTmpStrToDelete=lTempFilesPath.replaceAll("\\\\", "_");
				lTmpStrToDelete=lTmpStrToDelete.replaceAll(":", "");
				lTmpFileName=lTmpFileName.replaceAll(lTmpStrToDelete, "").trim();
				lFileNamesArr[i]=lTmpFileName.replaceAll("_sql", ".sql");;				
				//System.out.println(lFileNamesArr[i]);
			}
			//Converting file (tmpFile_100.sql)...Ok (1 lines, 0 sec)
			//Checking Convertion Status in Log Data
			Pattern lConvertFilePat=null;
			String[] logDataArr=_logDatStr.split("\\r\\n");
			String lFileStatusStr="";
			for (int i = 0; i < logDataArr.length; i++) {
				
				for (int lFileCount = 0; lFileCount < lFileNamesArr.length; lFileCount++) {
					lConvertFilePat=Pattern.compile("Converting\\s+file\\s*\\(\\s*"+lFileNamesArr[lFileCount]+"\\s*\\)\\.\\.\\.\\bOk");					
				//	System.out.println("lConvertFilePat.matcher(logDataArr[i]).find()  "+lConvertFilePat.matcher(logDataArr[i]).find()+ "  "+logDataArr[i]);
					if(lConvertFilePat.matcher(logDataArr[i]).find()){
						//System.out.println(logDataArr[i]);
						lFileStatusStr=logDataArr[i].substring(logDataArr[i].indexOf("...")+3,logDataArr[i].length()).trim();
						
						if(lFileStatusStr.length()>2){
							lFileStatusStr=lFileStatusStr.substring(0,2);
						//	System.out.println(lFileStatusStr+"  length");
						}
						//System.out.println(logDataArr[i]);
						if("".equals(lFileStatusStr.trim())){
							lFileStatusStr="Failed";
						}
						if(lFileStatusStr.trim().equalsIgnoreCase("OK")){
						//	System.out.println("inside ok");
							lFileNamesLst.add(lFileNamesArr[lFileCount].trim());
							//System.out.println("File Name::"+lFileNamesArr[lFileCount]+"::Status::->"+lFileStatusStr);
						}
						//System.out.println("File Name::"+lFileNamesArr[lFileCount]+"::Status::->"+lFileStatusStr);
						lFileStatusStr="";						
						break;
					}
				}	
			}
			
			System.out.println(lFileNamesLst);		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return lFileNamesLst;
		}
		return lFileNamesLst;
	}
	
	public String ReplaceConstsByOriginalVBFunctions(HashMap pVBFuncConstMap,String pQueryStr){
		
		try {			
			Iterator it = pVBFuncConstMap.entrySet().iterator();
			String lTmpVar="";
			while (it.hasNext()) {			
				Map.Entry lMapEntry = (Map.Entry)it.next();			
				if(lMapEntry.getKey()!=null && !"".equals((String)lMapEntry.getKey())){
					lTmpVar=(String)lMapEntry.getKey();					
					//System.out.println(lTmpVar+"::::"+(String)lMapEntry.getValue());
					if( !"".equals(lTmpVar.trim()) && pQueryStr.contains(lTmpVar.trim())){
						//pQueryStr=pQueryStr.replaceAll(lTmpVar.trim(),(String)lMapEntry.getValue());
						pQueryStr=StringUtils.replace(pQueryStr,lTmpVar.trim(),(String)lMapEntry.getValue());
					}					
				}				
			}			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pQueryStr;
	}
	public String removeDelimeters(String pQueryStr){
		
		// For Noramal Delimeters
		pQueryStr=pQueryStr.replaceAll("_DBT_DELSCS_\\s*'","'_DBT_DELSC_");
		pQueryStr=pQueryStr.replaceAll("_DBT_DELCSS_\\s*'","'_DBT_DELCS_" );
		pQueryStr=pQueryStr.replaceAll("_DBT_DELCS_\\s*'","'_DBT_DELC_" );
		pQueryStr=pQueryStr.replaceAll("_DBT_DELSC_\\s*,",",_DBT_DELS_");
		pQueryStr=pQueryStr.replaceAll("_DBT_DELS_\\s*'","'_DBT_DEL_");
		
		pQueryStr=pQueryStr.replaceAll("_DBT_DELC_\\s*,",",_DBT_DEL_");
		
		
		pQueryStr=pQueryStr.replaceAll("_DBT_DELDSCS_\\s*'","'_DBT_DELDSC_");
		pQueryStr=pQueryStr.replaceAll("_DBT_DELDCSS_\\s*'","'_DBT_DELDCS_" );
		pQueryStr=pQueryStr.replaceAll("_DBT_DELDCS_\\s*'","'_DBT_DELDC_" );
		pQueryStr=pQueryStr.replaceAll("_DBT_DELDSC_\\s*,",",_DBT_DELDS_");
		pQueryStr=pQueryStr.replaceAll("_DBT_DELDS_\\s*'","'_DBT_DELD_");
		pQueryStr=pQueryStr.replaceAll("_DBT_DELDS_\\s*\\(\\s*'","('_DBT_DELD_");
		pQueryStr=pQueryStr.replaceAll("_DBT_DELDC_\\s*,",",_DBT_DELD_");
		pQueryStr=pQueryStr.replaceAll("_DBT_DELD_","\"_DBT_DEL_");
		
		
		return pQueryStr;
	}
	public String removeTempVarConstants(String pQueryStr){
		
		//System.out.println("Before Replace ::->"+pQueryStr);
		
		pQueryStr=pQueryStr.replaceAll("_DBT_D_QUOTE_TEXT_START_|_DBT_D_QUOTE_TEXT_END_", "\"");
		//_DBT_AND_SYM_
		//pQueryStr=pQueryStr.replaceAll( "_DBT_DEL_\\s*\\)"," ) _DBT_DEL_");
		//Removed because after call one more extra close brace is coming if already source code has that so no need to replace it.
		//chk next time if any problem comes with close brace.
		pQueryStr=pQueryStr.replaceAll("_DBT_AND_SYM_", " & ");
		pQueryStr=pQueryStr.replaceAll("_DBT_AND_VAR_1_", "\" & ");
		pQueryStr=pQueryStr.replaceAll("_DBT_AND_VAR_2_", "  & \"");
		pQueryStr=pQueryStr.replaceAll("'_DBT_STR_CONCAT_S_","\" & \" '" );
		
		
		pQueryStr=pQueryStr.replaceAll("_DBT_VAR_CON_START_"," \"& ");
		pQueryStr=pQueryStr.replaceAll("_DBT_VAR_CON_END_"," &\" ");
		pQueryStr=pQueryStr.replaceAll("_DBT_VAR_S_START"," \"& ");
		pQueryStr=pQueryStr.replaceAll("_DBT_VAR_S_END"," &\" ");
		//_DBT_S_VAR_
		//System.out.println("After Replace ::->"+pQueryStr);
		return pQueryStr;
		
	}
	
	public List readSourceFileData(String pPath,String pFileName){	
		String[] lines = null;		
		List lOutputLinesLst=new ArrayList();
		try {
			//InputStream is = new FileInputStream(pPath+"\\"+pFileName);
			InputStream is = new FileInputStream(pPath);
			StringBuffer buffer = new StringBuffer();			
			byte[] b = new byte[4096];
			for (int n; (n = is.read(b)) != -1;) {
				
				buffer.append(new String(b, 0, n));
			}			
			String str = buffer.toString();			
			Pattern p = Pattern.compile("\r\n");                        
			lines = p.split(str);
			for (int i = 0; i < lines.length; i++) {
				lOutputLinesLst.add(lines[i]);
			}			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lOutputLinesLst;
	}
	
	private void insertDSqlTargetMapData(String pProjectId,String pRunSeq,DynamicSQLDataDTO pDynamicSQLDataDTO,String pConvertedQuery,String pConvertionStatus,Connection pConnection){
		//PROJECT_ID, RUN_SEQ, ORIGINAL_DSQL_QUERY, SOURCE_FILE_PATH, SOURCE_FILE_NAME,
		//CONVERTED_DSQL_QUERY, CONVERTION_STATUS
		try {
			//System.out.println("::: In Target Map Insert::::");
			lInsertDsqlTargetPreparedStatement.setString(1, pProjectId);
			lInsertDsqlTargetPreparedStatement.setString(2, pRunSeq);
			lInsertDsqlTargetPreparedStatement.setString(3, pDynamicSQLDataDTO.getOrginalDSQLQuery());
			lInsertDsqlTargetPreparedStatement.setString(4, pDynamicSQLDataDTO.getSourceFilePath());
			lInsertDsqlTargetPreparedStatement.setString(5, pDynamicSQLDataDTO.getSourceFileName());
			lInsertDsqlTargetPreparedStatement.setString(6, pConvertedQuery);
			lInsertDsqlTargetPreparedStatement.setString(7, pConvertionStatus);
			lInsertDsqlTargetPreparedStatement.addBatch();
			if(lTargetMapQueryCount==500){
				lInsertDsqlTargetPreparedStatement.executeBatch();
				pConnection.commit();
				lTargetMapQueryCount=0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		// TODO code application logic here
		System.out.println("test main::::::::::"
				+ new Timestamp(System.currentTimeMillis()));
		
		// Connection con=DBConnectionManager.getConnection();
		VBFrontEndParseForDSqlDAO lFrontEndParseForDSqlDAO = new VBFrontEndParseForDSqlDAO();
		//lFrontEndParseForDSqlDAO.getDynamicSqlData("PRID_005","0");
		String pSourceFilePath="C:\\Praveen\\Tool Data\\inputs\\Source\\single";
		lFrontEndParseForDSqlDAO.getDynamicSqlData("PRID_005","0",pSourceFilePath,DBConnectionManager.getConnection());
		System.out.println("test main:done::::::"
				+ new Timestamp(System.currentTimeMillis()));
		
		

	}

	
	 
}
