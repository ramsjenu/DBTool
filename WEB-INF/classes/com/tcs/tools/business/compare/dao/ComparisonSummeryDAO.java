package com.tcs.tools.business.compare.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tcs.tools.business.compare.dto.ComparedKeywordDTO;
import com.tcs.tools.business.compare.dto.ComparedSummaryDTO;
import com.tcs.tools.business.compare.dto.PseudoSqlDTO;
import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.web.dao.InventoryAnalyticsDAO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class ComparisonSummeryDAO {
	Connection lConnection=null;
	ResultSet lResultSet=null;
	PreparedStatement lPreparedStatement=null;
	PreparedStatement lPreparedStatementInsert=null;
	private int lQueryCount =0;
	HashMap lMisMatchDescMap=null;
	String lCurState="";
	String lStausMsg="";
	HashMap lKeywordPairingMap = null;
	 
	public List getDataTypesList() {
			
			List lDataTypesList = new ArrayList();
			//Sybase DataTypesList
			lDataTypesList.add((String) "CHAR");
			lDataTypesList.add((String) "VARCHAR");
			lDataTypesList.add((String) "TEXT");
			lDataTypesList.add((String) "BIT");
			lDataTypesList.add((String) "TINYINT");		
			lDataTypesList.add((String) "INT");
			lDataTypesList.add((String) "SMALLINT");
			lDataTypesList.add((String) "NUMERIC");	
			lDataTypesList.add((String) "DECIMAL");
			lDataTypesList.add((String) "FLOAT");
			lDataTypesList.add((String) "REAL");
			lDataTypesList.add((String) "SMALLMONEY");
			lDataTypesList.add((String) "DOLLAR");
			lDataTypesList.add((String) "MONEY");
			lDataTypesList.add((String) "SMALLDATETIME");
			lDataTypesList.add((String) "DATETIME");
			lDataTypesList.add((String) "TIMESTAMP");
			lDataTypesList.add((String) "DOUBLE PRECISION");
			lDataTypesList.add((String) "IMAGE");
			
			//DB2 DataTypesList
			lDataTypesList.add((String) "BINARY");
			lDataTypesList.add((String) "REAL");
			lDataTypesList.add((String) "DOUBLE");
			lDataTypesList.add((String) "DATE");
			lDataTypesList.add((String) "TIME");
			lDataTypesList.add((String) "INTEGER");
			lDataTypesList.add((String) "DECIMAL");
			lDataTypesList.add((String) "TIMESTAMP");
			lDataTypesList.add((String) "VARCHAR");
			lDataTypesList.add((String) "NVARCHAR");
			lDataTypesList.add((String) "CHAR");
			lDataTypesList.add((String) "NCHAR");
			lDataTypesList.add((String) "VARBINARY");
			lDataTypesList.add((String) "GRAPHIC");
			lDataTypesList.add((String) "VARGRAPHIC");
			lDataTypesList.add((String) "SMALLINT");
			lDataTypesList.add((String) "BLOB");
			lDataTypesList.add((String) "CLOB");
			
			return lDataTypesList;
			
		}
	
	public List getOracleDataTypesList() {
		
		List lDataTypesList = new ArrayList();
		//Sybase DataTypesList
		lDataTypesList.add((String) "CHAR");
		lDataTypesList.add((String) "VARCHAR");
		lDataTypesList.add((String) "TEXT");
		lDataTypesList.add((String) "BIT");
		lDataTypesList.add((String) "TINYINT");		
		lDataTypesList.add((String) "INT");
		lDataTypesList.add((String) "SMALLINT");
		lDataTypesList.add((String) "NUMERIC");	
		lDataTypesList.add((String) "DECIMAL");
		lDataTypesList.add((String) "FLOAT");
		lDataTypesList.add((String) "REAL");
		lDataTypesList.add((String) "SMALLMONEY");
		lDataTypesList.add((String) "DOLLAR");
		lDataTypesList.add((String) "MONEY");
		lDataTypesList.add((String) "SMALLDATETIME");
		lDataTypesList.add((String) "DATETIME");
		lDataTypesList.add((String) "TIMESTAMP");
		lDataTypesList.add((String) "DOUBLE PRECISION");
		lDataTypesList.add((String) "IMAGE");
		
		//Oracle DataTypesList
		lDataTypesList.add((String) "NUMBER");
		lDataTypesList.add((String) "VARCHAR2");
		lDataTypesList.add((String) "NCLOB");
		lDataTypesList.add((String) "DATE");
		lDataTypesList.add((String) "RAW");
		lDataTypesList.add((String) "BINARY_DOUBLE");
		lDataTypesList.add((String) "BINARY_FLOAT");
		lDataTypesList.add((String) "TIMESTAMP");
		lDataTypesList.add((String) "XMLTYPE");
		lDataTypesList.add((String) "NVARCHAR2");
		lDataTypesList.add((String) "CHAR");
		lDataTypesList.add((String) "NCHAR");
		lDataTypesList.add((String) "BLOB");
		lDataTypesList.add((String) "CLOB");
		
		return lDataTypesList;
		
	}
	
	
	public List getKeywordsForTablesNamesList() {
		List lKeywordsForTablesNamesLst = new ArrayList();
		lKeywordsForTablesNamesLst.add((String) "UPDATE");
		lKeywordsForTablesNamesLst.add((String) "INSERT");
		lKeywordsForTablesNamesLst.add((String) "INTO");
		lKeywordsForTablesNamesLst.add((String) "FROM");
		lKeywordsForTablesNamesLst.add((String) "JOIN");
		lKeywordsForTablesNamesLst.add((String) "ON");
		
		//After Changing Old Code
		lKeywordsForTablesNamesLst.add((String) "TABLE");
		//for Procedures
		
			
		return lKeywordsForTablesNamesLst;
	}	
	
	public List getKeywordsForColumnNamesList() {
		List lKeywordsForColumnNamesLst = new ArrayList();
		lKeywordsForColumnNamesLst.add((String) "SELECT");
		lKeywordsForColumnNamesLst.add((String) "SET");
		lKeywordsForColumnNamesLst.add((String) "WHERE");
		lKeywordsForColumnNamesLst.add((String) "AND");
		lKeywordsForColumnNamesLst.add((String) "OR");		
		lKeywordsForColumnNamesLst.add((String) "ON");
		lKeywordsForColumnNamesLst.add((String) "INSERT_TOOL_COLUMN");	
		lKeywordsForColumnNamesLst.add((String) "ORDER");
		lKeywordsForColumnNamesLst.add((String) "GROUP");
		lKeywordsForColumnNamesLst.add((String) "HAVING");
		lKeywordsForColumnNamesLst.add((String) "BY");
		lKeywordsForColumnNamesLst.add((String) "DISTINCT");
		lKeywordsForColumnNamesLst.add((String) "TABLE_TOOL_COLUMN_(");
		//TABLE_TOOL_COLUMN_(
		return lKeywordsForColumnNamesLst;
	}
	
	public HashMap getMisMatchDescMap() {	
		HashMap lMisMatchDescMap = new HashMap();
		lMisMatchDescMap.put("IF", "COND");//Conditional Mismatch
		lMisMatchDescMap.put("WHILE", "COND");//Conditional Mismatch
		lMisMatchDescMap.put("WHERE", "COM_DOP"); //Complex Data Operation
		lMisMatchDescMap.put("SRC_DT", "SRC_DT"); // Source of Data Mismatch
		
		System.out.println("mismatch desc value "+lMisMatchDescMap.values());
		//lMisMatchDescMap.put("DECLARE", "DTYPE"); //Data Type Mismatch
		
		//tool utils method replaceMisMatchDesc(String pParam) to add the description
		//ToolsUtil.replaceMisMatchDesc(String pParam);
		
		//lMisMatchDescMap.put("WHERE", "COM_DOP");
		return lMisMatchDescMap;
	}
	
	
	
	public List getKeywordComparedList(String pCompareSeq,String pProcName,String pSQL){
		List lKeywordComparedList=new ArrayList();	
		PreparedStatement lPreparedStatementGetDetais=null;
		ResultSet lResultSetOne= null;
		try { 
			int lRecCount=0;
			lPreparedStatementGetDetais = lConnection
					.prepareStatement(pSQL);
			lPreparedStatementGetDetais.setString(1, pCompareSeq);
			lPreparedStatementGetDetais.setString(2, pProcName);
			lPreparedStatementGetDetais.setFetchSize(Integer.MIN_VALUE);
			lResultSetOne = lPreparedStatementGetDetais.executeQuery();
			
			//CachedRowSet records = new CachedRowSetImpl();
			
			
			//records.populate(lResultSetOne);

			
			//ComparedKeywordDTO lComparedKeywordDTO = null;			
			
			if (lResultSetOne != null) {
				
				while (lResultSetOne.next()) {
					//System.out.println(":::::lResultSetOne.getString(SOURCE_FORMED_STATEMENT)::::"+lResultSetOne.getString("SOURCE_FORMED_STATEMENT"));
					ComparedKeywordDTO lComparedKeywordDTO=new ComparedKeywordDTO();
					lComparedKeywordDTO.setCompareSeq(pCompareSeq);
					lComparedKeywordDTO.setProcName(lResultSetOne.getString("PROCEDURE_NAME"));
					lComparedKeywordDTO.setOrderNo(lResultSetOne.getString("COMPARE_ORDERNO"));
					lComparedKeywordDTO.setPrevKeyword(lResultSetOne.getString("PREVIOUS_KEYWORD"));
					lComparedKeywordDTO.setMatchedYN(lResultSetOne.getString("MATCH_YN"));
					//Source
					lComparedKeywordDTO.setSourceStatementNo(lResultSetOne.getString("SOURCE_STATEMENT_NO"));
					lComparedKeywordDTO.setSourcePatternId(lResultSetOne.getString("SOURCE_PATTERN_ID"));					
					lComparedKeywordDTO.setSourceIndex(lResultSetOne.getString("SOURCE_INDEX"));
					lComparedKeywordDTO.setSourcePatternType(lResultSetOne.getString("SOURCE_PATTERN_TYPE"));
					lComparedKeywordDTO.setSourceKeyword(lResultSetOne.getString("SOURCE_KEYWORD"));
					lComparedKeywordDTO.setSourceAlias(lResultSetOne.getString("SOURCE_ALIAS"));
					lComparedKeywordDTO.setSourceLevelCount(lResultSetOne.getString("SOURCE_LEVELCOUNT"));
					lComparedKeywordDTO.setSourcePrimaryStatement(lResultSetOne.getString("SOURCE_PRIMARY_STATEMENT"));
					
					//lComparedKeywordDTO.setSourceFormedStatement(lResultSetOne.getString("SOURCE_LEVELCOUNT"));
					/*lComparedKeywordDTO.setSourceFormedStatement(lResultSetOne.getString("SOURCE_FORMED_STATEMENT"));*/
					
					
					//Target
					lComparedKeywordDTO.setTargetStatementNo(lResultSetOne.getString("TARGET_STATEMENT_NO"));
					lComparedKeywordDTO.setTargetPatternId(lResultSetOne.getString("TARGET_PATTERN_ID"));					
					lComparedKeywordDTO.setTargetIndex(lResultSetOne.getString("TARGET_INDEX"));
					lComparedKeywordDTO.setTargetPatternType(lResultSetOne.getString("TARGET_PATTERN_TYPE"));
					lComparedKeywordDTO.setTargetKeyword(lResultSetOne.getString("TARGET_KEYWORD"));
					lComparedKeywordDTO.setTargetAlias(lResultSetOne.getString("TARGET_ALIAS"));
					lComparedKeywordDTO.setTargetLevelCount(lResultSetOne.getString("TARGET_LEVELCOUNT"));
					lComparedKeywordDTO.setTargetPrimaryStatement(lResultSetOne.getString("TARGET_PRIMARY_STATEMENT"));
					
					lComparedKeywordDTO.setSourceSubIndex(lResultSetOne.getString("SOURCE_SUB_INDEX"));
					lComparedKeywordDTO.setTargetSubIndex(lResultSetOne.getString("TARGET_SUB_INDEX"));
					
					
					
					
					//lComparedKeywordDTO.setTargetFormedStatement(lResultSetOne.getString("TARGET_FORMED_STATEMENT"));
					
					
					//System.out.println("Matched::::"+lResultSet.getString("a.Match_yn")+"<<>>Source::->>"+lResultSet.getString("a.Source_keyword")+"  :::Target::->>>"+lResultSet.getString("a.Target_keyword"));
					
					/*System.out.println("lRecCount:::"+lRecCount);
					lRecCount++;*/
					lKeywordComparedList.add(lComparedKeywordDTO);
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// close the connection and the result set
			DBConnectionManager.closeConnection(lPreparedStatementGetDetais, lResultSetOne);
			
		}
		return lKeywordComparedList;
	}
	public void prepareSummery(String pCompareSeq,String pCreatedBy){
		System.out.println(":::::inside comparison summary dao::::::");
		try{
			lConnection=DBConnectionManager.getConnection();	
			List lprocdeureList=new ArrayList();
			List lSourceKeywordComparedList=null;
			List lTargetKeywordComparedList=null;
			List lSourceComparedSummeyList=null;
			List lTargetComparedSummeyList=null;
			ComparedSummaryDTO lSourceComparedSummaryDTO=null;
			ComparedSummaryDTO lTargetComparedSummaryDTO=null;
			
			DeepDiveComparisonDAO lDeepDiveComparisonDAO = new DeepDiveComparisonDAO();
			lKeywordPairingMap=lDeepDiveComparisonDAO.prepareKeywordPairMap();
			lConnection.setAutoCommit(false);
			lMisMatchDescMap=getMisMatchDescMap();
			lPreparedStatementInsert = lConnection.prepareStatement(ToolConstant.INSERT_COMPARE_STATEMENT_AFTER_MODIFICATION);
			
			//get the project details hash map
			InventoryAnalyticsDAO lInventoryAnalyticsDAO = new InventoryAnalyticsDAO();			
			HashMap lProjectDetailsMap =  lInventoryAnalyticsDAO.getDBConnectionDetails(pCompareSeq);
			List lSPNameList = ToolsUtil.getFileNamesFromFolderUpperCase(new File(ToolsUtil.replaceNull((String)lProjectDetailsMap.get("SOURCE_PATH"))),new ArrayList());
			
			
			lPreparedStatement = lConnection
					.prepareStatement(ToolConstant.GET_PROC_NAMES_IN_COMAPRED_KEYWORD);
			lPreparedStatement.setString(1, pCompareSeq);
			lResultSet = lPreparedStatement.executeQuery();
			
			if (lResultSet != null) {
				while (lResultSet.next()) {
					
					//added for runnning for new procs only...
					if(!lSPNameList.contains(lResultSet.getString("PROCEDURE_NAME").trim().toUpperCase())){
						continue;
					}
					//added for runnning for new procs only...
					
					lprocdeureList.add((String)lResultSet.getString("PROCEDURE_NAME").trim());
				}
			}
			
			if(lprocdeureList!=null){
				for (int i = 0; i < lprocdeureList.size(); i++) {		
					System.out.println("Proc Name:->"+lprocdeureList.get(i));
					
					//Update Status to Front End - Start
					lCurState="Populating Mismatch Summery";
					lStausMsg="File Name::->"+lprocdeureList.get(i);
					ToolsUtil.prepareInsertStatusMsg(pCompareSeq, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
					//Update Status to Front End - End
					
					
					lSourceKeywordComparedList=null;
					lTargetKeywordComparedList = null;
					lSourceComparedSummeyList= null;
					lTargetComparedSummeyList =null;
					
					lSourceKeywordComparedList = getKeywordComparedList(pCompareSeq,((String)lprocdeureList.get(i)).trim(),ToolConstant.GET_COMAPRED_KEYWORD_DETAILS_SOURCE);
					
					lSourceComparedSummeyList = getSourceSummery(lSourceKeywordComparedList,pCompareSeq,pCreatedBy);
					
					lSourceKeywordComparedList=null;
					System.out.println("Source Summery inside Created");
					lTargetKeywordComparedList = getKeywordComparedList(pCompareSeq,((String)lprocdeureList.get(i)).trim(),ToolConstant.GET_COMAPRED_KEYWORD_DETAILS_TARGET);					
					
					lTargetComparedSummeyList = getTargetSummery(lTargetKeywordComparedList,pCompareSeq,pCreatedBy);
					
					lTargetKeywordComparedList=null;
					System.out.println("Target Summery inside Created");
					for (int j = 0; j < lSourceComparedSummeyList.size(); j++) {
						lSourceComparedSummaryDTO=(ComparedSummaryDTO)lSourceComparedSummeyList.get(j);
						for (int k = 0; k < lTargetComparedSummeyList.size(); k++) {
							lTargetComparedSummaryDTO=(ComparedSummaryDTO)lTargetComparedSummeyList.get(k);
							if( lSourceComparedSummaryDTO.getSourceStmtNo().equalsIgnoreCase(lTargetComparedSummaryDTO.getSourceStmtNo()) && lSourceComparedSummaryDTO.getOrderNo().equalsIgnoreCase(lTargetComparedSummaryDTO.getOrderNo())){
								lSourceComparedSummaryDTO.setTargetModifiedFormedStmt(lTargetComparedSummaryDTO.getTargetModifiedFormedStmt());
								lSourceComparedSummaryDTO.setTargetPatId(lTargetComparedSummaryDTO.getTargetPatId());
								lSourceComparedSummaryDTO.setTargetPatDesc(lTargetComparedSummaryDTO.getTargetPatDesc());
								lSourceComparedSummaryDTO.setTargetStmtNo(lTargetComparedSummaryDTO.getTargetStmtNo());
								insertIdentifyPatternDetails(lSourceComparedSummaryDTO);
								break;
							}
							
						}
						
						
						
					}
				}
			}
			
			lPreparedStatementInsert.executeBatch();
			lConnection.commit();
 			lConnection.setAutoCommit(true);
 			System.out.println(":::::inside comparison summary dao over::::::");
		}catch(Exception  E){
			E.printStackTrace();
			
		}finally{
			
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			DBConnectionManager.closeConnection(lPreparedStatementInsert, null);
			DBConnectionManager.closeConnection(lConnection);
		}
		
	}
	
	public List getSourceSummery(List lKeywordComparedList,String pCompareSeq,String pCreatedBy){
		List lComparedSummaryDTOLst=new ArrayList();
		
		try {
			List lKeywordsForTablesNamesLst = getKeywordsForTablesNamesList();
			List lKeywordsForColumnNamesLst = getKeywordsForColumnNamesList();
			List lDataTypesList=getDataTypesList();
			
			ComparedKeywordDTO lComparedKeywordDTO=null;
			ComparedKeywordDTO lPrevComparedKeywordDTO=null;
			
			
			String lSourceMisMatchString="";			
			String lPrevSourceStatementNo="";			
			String lColor="";
			String lMisMatchType="";
			String lMatchedYn ="";	
			String lIndMisMatch="";
			String lSourcePrimarySql="";
			String lTargetPrimarySql="";
			
			String lPerformImpact="No";
			String lPerformImpactDesc="";
			int lSourceQueryCount=0;
			int lTargetQueryCount=0;
			
			ComparedSummaryDTO lComparedSummaryDTO = null;			
			//System.out.println(":::::::lKeywordComparedList size::::"+lKeywordComparedList.size());
			if(lKeywordComparedList!=null && lKeywordComparedList.size() >0 ){				
				for (int j = 0; j <= lKeywordComparedList.size(); j++) {
					if(j < lKeywordComparedList.size()){
						lComparedKeywordDTO=(ComparedKeywordDTO)lKeywordComparedList.get(j);
					}
					/*if( (!"".equals(lPrevSourceStatementNo) && !lComparedKeywordDTO.getSourceStatementNo().trim().equalsIgnoreCase(lPrevSourceStatementNo.trim()))
							|| (lSourceMisMatchString.trim().contains(ToolConstant.TOOL_DELIMT)))*/
					//Checking for the starting of the next statement to know the end of the previous statement using defined delimeter.
					if( ((lPrevComparedKeywordDTO!=null && j>0 ) && lComparedKeywordDTO.getSourceKeyword().equalsIgnoreCase(ToolConstant.TOOL_DELIMT))
							|| ( j == lKeywordComparedList.size() )) {
						//System.out.println("lSourceMisMatchString::->"+lSourceMisMatchString);
						//forming the DTO
						 lComparedSummaryDTO = new ComparedSummaryDTO();
						 lComparedSummaryDTO.setCompareSeq(pCompareSeq);
						 lComparedSummaryDTO.setCreatedBy(pCreatedBy);
						 lComparedSummaryDTO.setProcedureName(lPrevComparedKeywordDTO.getProcName());
						 //lComparedSummaryDTO.setSourceFormedStmt(ToolsUtil.removeToolChars(lPrevComparedKeywordDTO.getSourceFormedStatement(),ToolConstant.TOOL_DELIMT));
						 lComparedSummaryDTO.setSourceModifiedFormedStmt(ToolsUtil.removeToolChars(lSourceMisMatchString,ToolConstant.TOOL_DELIMT));
						 //lComparedSummaryDTO.setSourceModifiedFormedStmt(lSourceMisMatchString.replaceAll(ToolConstant.TOOL_DELIMT, ""));
						 //lComparedSummaryDTO.setSourceFormedStmt(lPrevComparedKeywordDTO.getSourceFormedStatement());
						 //lComparedSummaryDTO.setSourceModifiedFormedStmt(lSourceMisMatchString);
						 lComparedSummaryDTO.setSourcePatId(lPrevComparedKeywordDTO.getSourcePatternId());
						 lComparedSummaryDTO.setSourceStmtNo(lPrevComparedKeywordDTO.getSourceStatementNo());
						 //lComparedSummaryDTO.setTargetFormedStmt(ToolsUtil.removeToolChars(lPrevComparedKeywordDTO.getTargetFormedStatement(),ToolConstant.TOOL_DELIMT));
						// lComparedSummaryDTO.setTargetModifiedFormedStmt(ToolsUtil.removeToolChars(lTargetMisMatchString,ToolConstant.TOOL_DELIMT));
						 //lComparedSummaryDTO.setTargetFormedStmt(lPrevComparedKeywordDTO.getTargetFormedStatement());
						 //lComparedSummaryDTO.setTargetModifiedFormedStmt(lTargetMisMatchString);
						 
						 //lComparedSummaryDTO.setTargetPatId(lPrevComparedKeywordDTO.getTargetPatternId());
						 //lComparedSummaryDTO.setTargetStmtNo(lPrevComparedKeywordDTO.getTargetStatementNo());
						 lComparedSummaryDTO.setMisMatchCategory(lMisMatchType);
						 lComparedSummaryDTO.setMisMatchCategoryDesc("");
						 
						 if("".equals(lMisMatchType)){
							if("".equals(lPrevComparedKeywordDTO.getSourcePatternId().trim()) || "".equals(lPrevComparedKeywordDTO.getTargetPatternId().trim())){
								lMatchedYn ="N";
							}else{
								lMatchedYn ="Y";
							}
						 }else{
							 lMatchedYn ="N";
						 }
						 
						 lComparedSummaryDTO.setMatchedYN(lMatchedYn);
						 lComparedSummaryDTO.setOrderNo(lPrevComparedKeywordDTO.getOrderNo());
						 
						  //inserting the modified formed statements
						 //lComparedSummaryDTOLst.add(lComparedSummaryDTO);
						//insertIdentifyPatternDetails(lComparedSummaryDTO);
						
						lSourceMisMatchString="";						
						lMisMatchType="";
						if(!"Y".equalsIgnoreCase(lMatchedYn)){
							 //System.out.println("St No::-"+lPrevComparedKeywordDTO.getSourceStatementNo()+"<<<>>>"+lSourcePrimarySql+"<<<<<>>>>>"+lTargetPrimarySql);
							//Checking subquery Count
							if( (lSourceQueryCount!=lTargetQueryCount) ){
								lPerformImpact="Yes";
								lPerformImpactDesc="Sub-Queries Added/Deleted";
								if("".equals(lSourcePrimarySql.trim())){
									 lPerformImpactDesc="Additional Statement in Target";										 
								 }else if("".equals(lTargetPrimarySql.trim())){
									 lPerformImpactDesc="Additional Statement in Source";
								 }
								//System.out.println("St No::-"+lPrevComparedKeywordDTO.getSourceStatementNo()+"<<<>>>"+lSourcePrimarySql.trim().toUpperCase()+"---"+lTargetPrimarySql.trim().toUpperCase());
							 }
							//Checking Primary Statement
							/*if(!(ToolsUtil.replaceNullList((List)lKeywordPairingMap.get(lPrevComparedKeywordDTO.getSourcePrimaryStatement().trim().toUpperCase()))).contains(lPrevComparedKeywordDTO.getTargetPrimaryStatement().trim().toUpperCase())
									&& ((!lPrevComparedKeywordDTO.getSourcePrimaryStatement().trim().equalsIgnoreCase(lPrevComparedKeywordDTO.getTargetPrimaryStatement().trim())))){
									 lPerformImpact="Yes---";
									 System.out.println("St No::-"+lPrevComparedKeywordDTO.getSourceStatementNo()+"<<<>>>"+lPrevComparedKeywordDTO.getSourcePrimaryStatement().trim().toUpperCase()+"---"+lPrevComparedKeywordDTO.getTargetPrimaryStatement().trim().toUpperCase());
									 System.out.println("St No::-"+lPrevComparedKeywordDTO.getSourceStatementNo()+"<<<>>>"+lPrevComparedKeywordDTO.getSourceKeyword()+"---"+lPrevComparedKeywordDTO.getTargetKeyword());
									 System.out.println("::::map list value for pairing :::::"+(List)lKeywordPairingMap.get(lPrevComparedKeywordDTO.getSourcePrimaryStatement().trim().toUpperCase()));
									 
							}*/
							
							if(!(ToolsUtil.replaceNullList((List)lKeywordPairingMap.get(lSourcePrimarySql.trim().toUpperCase()))).contains(lTargetPrimarySql.trim().toUpperCase())
									&& ((!lSourcePrimarySql.trim().equalsIgnoreCase(lTargetPrimarySql.trim())))){
									 lPerformImpact="Yes";
									 lPerformImpactDesc="Primary SQL Statement Type Mismatch";
									 if("".equals(lSourcePrimarySql.trim())){
										 lPerformImpactDesc="Additional Statement in Target";										 
									 }else if("".equals(lTargetPrimarySql.trim())){
										 lPerformImpactDesc="Additional Statement in Source";
									 }
									 
									 //System.out.println("St No::-"+lPrevComparedKeywordDTO.getSourceStatementNo()+"<<<>>>"+lSourcePrimarySql.trim().toUpperCase()+"---"+lTargetPrimarySql.trim().toUpperCase());
									 
									// System.out.println("::::map list value for pairing :::::"+(List)lKeywordPairingMap.get(lSourcePrimarySql.trim().toUpperCase()));
									 
							}
						}
							
							//
							
						
						
						 
						 lComparedSummaryDTO.setPerformanceImpact(lPerformImpact);	
						 lComparedSummaryDTO.setPerformanceImpactDesc(lPerformImpactDesc);
						 
						 //inserting the modified formed statements
						 lComparedSummaryDTOLst.add(lComparedSummaryDTO);
						//insertIdentifyPatternDetails(lComparedSummaryDTO);
						 lPerformImpact="No";
						 lSourceQueryCount=0;
						 lTargetQueryCount=0;
						 lSourcePrimarySql="";
						 lTargetPrimarySql="";
						 lPerformImpactDesc ="";
								 
					}
					if(j == lKeywordComparedList.size()){
						break;
					}
					
					
					if("".equals(lSourcePrimarySql.trim())){
						lSourcePrimarySql=lComparedKeywordDTO.getSourcePrimaryStatement();
					}
					if("".equals(lTargetPrimarySql.trim())){
						lTargetPrimarySql=lComparedKeywordDTO.getTargetPrimaryStatement();
					}
					
					//Checking Query COunt
					//Source
					int queryCount=Integer.parseInt(ToolsUtil.replaceZero(lComparedKeywordDTO.getSourceLevelCount().trim()));
					if(queryCount>lSourceQueryCount){
						lSourceQueryCount=queryCount;
					}
					queryCount=0;
					//target
					queryCount=Integer.parseInt(ToolsUtil.replaceZero(lComparedKeywordDTO.getTargetLevelCount().trim()));
					if(queryCount>lTargetQueryCount){
						lTargetQueryCount=queryCount;
					}
					queryCount=0;
					lColor="BLACK";
					if(lComparedKeywordDTO.getMatchedYN().trim().equalsIgnoreCase("N")){
						lColor="RED";
					}
					if(lComparedKeywordDTO.getMatchedYN().trim().equalsIgnoreCase("M")){
						lColor="BLUE";
					}
					if(lComparedKeywordDTO.getMatchedYN().trim().equalsIgnoreCase("P")){
						lColor="#CC6600";
					}
					
					//System.out.println("Chk "+lOuterComparedKeywordDTO.getSourceStatementNo().trim());
					lIndMisMatch="";
					//Finding the type of MisMatch -  06-12-2011 Start
					if(lComparedKeywordDTO.getMatchedYN().trim().equalsIgnoreCase("M")){
						lIndMisMatch ="PAT";
					}
					if(!lComparedKeywordDTO.getMatchedYN().trim().equalsIgnoreCase("Y")&&!lComparedKeywordDTO.getMatchedYN().trim().equalsIgnoreCase("M")){
						
						if(lMisMatchDescMap.containsKey((String)lComparedKeywordDTO.getPrevKeyword().toUpperCase()))
						{
							if("".equals(lComparedKeywordDTO.getSourceKeyword())){
								lIndMisMatch=(String)lMisMatchDescMap.get((String)lComparedKeywordDTO.getPrevKeyword().toUpperCase());
							}else{
								//lIndMisMatch=(String)lMisMatchDescMap.get((String)"SRC_DT");;
								lIndMisMatch="";
							}						
							
						}
						if((lComparedKeywordDTO.getSourceKeyword().trim().toUpperCase().startsWith("@") 
								||lComparedKeywordDTO.getSourceKeyword().trim().toUpperCase().startsWith("@@")
								|| lComparedKeywordDTO.getTargetKeyword().trim().toUpperCase().startsWith("V_") 
								|| lComparedKeywordDTO.getTargetKeyword().trim().toUpperCase().startsWith("SWV_")) 
								&& lComparedKeywordDTO.getSourceKeyword().trim().toUpperCase()!="" 
								&& lComparedKeywordDTO.getTargetKeyword().trim().toUpperCase()!="")
						{
							/*if(!"".equals(lIndMisMatch)){
								lIndMisMatch=lIndMisMatch+" - ";
							}*/
					
						lIndMisMatch ="VAR";	
							
						}
						//if{
							
							//if(!"".equals(lComparedKeywordDTO.getSourcePatternType()) ){								
								/*if( (!lMisMatchType.toUpperCase().contains(lComparedKeywordDTO.getSourcePatternType().substring(0,3).trim().toUpperCase()))){
									if(!"".equals(lMisMatchType)){
										lMisMatchType=lMisMatchType+",";
									}
									lMisMatchType=lMisMatchType+" "+lComparedKeywordDTO.getSourcePatternType().substring(0,3).trim();
									
								}*/
								/*if(!"".equals(lIndMisMatch)){
									lIndMisMatch=lIndMisMatch+" - ";
								}*/
						if(!"".equals(lComparedKeywordDTO.getSourcePatternType())||!"".equals(lComparedKeywordDTO.getTargetPatternType()) ){
								if(lKeywordsForTablesNamesLst.contains((String)lComparedKeywordDTO.getSourceKeyword().trim().toUpperCase())
										|| lKeywordsForColumnNamesLst.contains((String)lComparedKeywordDTO.getSourceKeyword().trim().toUpperCase()) /* For Source*/
										|| lKeywordsForTablesNamesLst.contains((String)lComparedKeywordDTO.getTargetKeyword().trim().toUpperCase())
										|| lKeywordsForColumnNamesLst.contains((String)lComparedKeywordDTO.getTargetKeyword().trim().toUpperCase())
									 /* For Target*/ ){
									lIndMisMatch="SQL";
								}else if(lKeywordsForTablesNamesLst.contains((String)lComparedKeywordDTO.getPrevKeyword().trim().toUpperCase())&&(lComparedKeywordDTO.getSourceKeyword().trim().toUpperCase()!=""&&lComparedKeywordDTO.getTargetKeyword().trim().toUpperCase()!="")){
									lIndMisMatch="TAB";
								}else if(lKeywordsForColumnNamesLst.contains((String)lComparedKeywordDTO.getPrevKeyword().trim().toUpperCase())){
									lIndMisMatch="COL";
								}else if(lDataTypesList.contains((String)lComparedKeywordDTO.getSourceKeyword().trim().toUpperCase())
										|| lDataTypesList.contains((String)lComparedKeywordDTO.getTargetKeyword().trim().toUpperCase()) ){
									lIndMisMatch="DTP";
								}
							/*	else if(lComparedKeywordDTO.getSourceKeyword()!=lComparedKeywordDTO.getTargetKeyword()){
									lIndMisMatch="KWD";
								}*/
								else{
									lIndMisMatch="OTH";
								}
								//lIndMisMatch+=lComparedKeywordDTO.getSourcePatternType().substring(0,3).trim();
							} 
							
						}
				//	}
					if(!lMisMatchType.toUpperCase().contains(lIndMisMatch)){
						if(!"".equals(lMisMatchType)){
							lMisMatchType=lMisMatchType+", ";
						}
						lMisMatchType=lMisMatchType+" "+lIndMisMatch;
					}
					
					//Finding the type of MisMatch -  06-12-2011 End
					
					lSourceMisMatchString+=" "+"<font color=\""+lColor+"\"> "+lComparedKeywordDTO.getSourceKeyword()+" "+lComparedKeywordDTO.getSourceAlias()+" </font>";
					//lTargetMisMatchString+=" "+"<font color=\""+lColor+"\"> "+lComparedKeywordDTO.getTargetKeyword()+" "+lComparedKeywordDTO.getTargetAlias()+" </font>";
					//style=\"background-color
					lPrevSourceStatementNo=lComparedKeywordDTO.getSourceStatementNo().trim();
					lPrevComparedKeywordDTO=lComparedKeywordDTO;
				}
			}
			//System.out.println("</table>");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lComparedSummaryDTOLst;
	}
	
	public List getTargetSummery(List lKeywordComparedList,String pCompareSeq,String pCreatedBy){
		List lComparedSummaryDTOLst=new ArrayList();
		try {
			ComparedKeywordDTO lComparedKeywordDTO=null;
			ComparedKeywordDTO lPrevComparedKeywordDTO=null;
			
			String lTargetMisMatchString="";
			String lPrevSourceStatementNo="";
			
			String lColor="";
			String lMisMatchType="";
			String lMatchedYn ="";
			
			ComparedSummaryDTO lComparedSummaryDTO = null;
			//System.out.println(":::::::lKeywordComparedList size::::"+lKeywordComparedList.size());
			if(lKeywordComparedList!=null && lKeywordComparedList.size() >0 ){
				
				for (int j = 0; j <= lKeywordComparedList.size(); j++) {
					
					 
					
					//lComparedKeywordDTO=new ComparedKeywordDTO();
					if(j < lKeywordComparedList.size()){
						lComparedKeywordDTO=(ComparedKeywordDTO)lKeywordComparedList.get(j);
					}
					/*if( (!"".equals(lPrevSourceStatementNo) && !lComparedKeywordDTO.getSourceStatementNo().trim().equalsIgnoreCase(lPrevSourceStatementNo.trim()))
							|| (lSourceMisMatchString.trim().contains(ToolConstant.TOOL_DELIMT)))*/
					//Checking for the starting of the next statement to know the end of the previous statement using defined delimeter.
					if( ((lPrevComparedKeywordDTO!=null && j>0 ) && lComparedKeywordDTO.getTargetKeyword().toUpperCase().equalsIgnoreCase(ToolConstant.TOOL_DELIMT))
							|| ( j == lKeywordComparedList.size() )) {
						
						 //System.out.println("lTargetMisMatchString::->"+lTargetMisMatchString);
						//forming the DTO
						 lComparedSummaryDTO = new ComparedSummaryDTO();
						 lComparedSummaryDTO.setCompareSeq(pCompareSeq);
						 lComparedSummaryDTO.setCreatedBy(pCreatedBy);
						 lComparedSummaryDTO.setProcedureName(lPrevComparedKeywordDTO.getProcName());
						 //lComparedSummaryDTO.setSourceFormedStmt(ToolsUtil.removeToolChars(lPrevComparedKeywordDTO.getSourceFormedStatement(),ToolConstant.TOOL_DELIMT));
						 //lComparedSummaryDTO.setSourceModifiedFormedStmt(ToolsUtil.removeToolChars(lSourceMisMatchString,ToolConstant.TOOL_DELIMT));
						 //lComparedSummaryDTO.setSourceModifiedFormedStmt(lSourceMisMatchString.replaceAll(ToolConstant.TOOL_DELIMT, ""));
						 //lComparedSummaryDTO.setSourceFormedStmt(lPrevComparedKeywordDTO.getSourceFormedStatement());
						 //lComparedSummaryDTO.setSourceModifiedFormedStmt(lSourceMisMatchString);
						 //lComparedSummaryDTO.setSourcePatId(lPrevComparedKeywordDTO.getSourcePatternId());
						 lComparedSummaryDTO.setSourceStmtNo(lPrevComparedKeywordDTO.getSourceStatementNo());
						 //lComparedSummaryDTO.setTargetFormedStmt(ToolsUtil.removeToolChars(lPrevComparedKeywordDTO.getTargetFormedStatement(),ToolConstant.TOOL_DELIMT));
						 lComparedSummaryDTO.setTargetModifiedFormedStmt(ToolsUtil.removeToolChars(lTargetMisMatchString,ToolConstant.TOOL_DELIMT));
						 //lComparedSummaryDTO.setTargetFormedStmt(lPrevComparedKeywordDTO.getTargetFormedStatement());
						 //lComparedSummaryDTO.setTargetModifiedFormedStmt(lTargetMisMatchString);
						 
						 lComparedSummaryDTO.setTargetPatId(lPrevComparedKeywordDTO.getTargetPatternId());
						 lComparedSummaryDTO.setTargetStmtNo(lPrevComparedKeywordDTO.getTargetStatementNo());
						 //lComparedSummaryDTO.setMisMatchCategory(lMisMatchType);
						 //lComparedSummaryDTO.setMisMatchCategoryDesc("");
						 
						/* if("".equals(lMisMatchType)){
							if("".equals(lPrevComparedKeywordDTO.getSourcePatternId().trim()) || "".equals(lPrevComparedKeywordDTO.getTargetPatternId().trim())){
								lMatchedYn ="N";
							}else{
								lMatchedYn ="Y";
							}
						 }else{
							 lMatchedYn ="N";
						 }*/
						 
						 //lComparedSummaryDTO.setMatchedYN(lMatchedYn);
						 lComparedSummaryDTO.setOrderNo(lPrevComparedKeywordDTO.getOrderNo());
						
						 //inserting the modified formed statements
						 lComparedSummaryDTOLst.add(lComparedSummaryDTO);
						//insertIdentifyPatternDetails(lComparedSummaryDTO);
						
						
						lTargetMisMatchString="";
						lMisMatchType="";
					}
					if(j == lKeywordComparedList.size()){
						break;
					}
					lColor="BLACK";
					if(lComparedKeywordDTO.getMatchedYN().trim().equalsIgnoreCase("N")){
						lColor="RED";
					}
					if(lComparedKeywordDTO.getMatchedYN().trim().equalsIgnoreCase("P")){
						lColor="#CC6600";
					}
					if(lComparedKeywordDTO.getMatchedYN().trim().equalsIgnoreCase("M")){
						lColor="BLUE";
					}
					//System.out.println("Chk "+lOuterComparedKeywordDTO.getSourceStatementNo().trim());
					
					
					/*if(lComparedKeywordDTO.getSourceKeyword().trim().toUpperCase().startsWith("@") 
							|| lComparedKeywordDTO.getTargetKeyword().trim().toUpperCase().startsWith("V_") 
							|| lComparedKeywordDTO.getTargetKeyword().trim().toUpperCase().startsWith("SWV_")){
						if(!lMisMatchType.toUpperCase().contains("VAR")){
							if(!"".equals(lMisMatchType)){
								lMisMatchType=lMisMatchType+",";
							}
							lMisMatchType=lMisMatchType+" VAR";
						}
						
						
					}*/
				/*	else if(lComparedKeywordDTO.getSourceKeyword().trim().toUpperCase().startsWith("@") ||lComparedKeywordDTO.getSourceKeyword().trim().toUpperCase().startsWith("@@")
							|| lComparedKeywordDTO.getTargetKeyword().trim().toUpperCase().startsWith("V_") 
							|| lComparedKeywordDTO.getTargetKeyword().trim().toUpperCase().startsWith("SWV_")){
						/*if(!"".equals(lIndMisMatch)){
							lIndMisMatch=lIndMisMatch+" - ";
						}*/
					
					/*
					else{
						if(!"".equals(lComparedKeywordDTO.getSourcePatternType()) ){
							if( (!lMisMatchType.toUpperCase().contains(lComparedKeywordDTO.getSourcePatternType().substring(0,3).trim().toUpperCase()))
									&& (!lComparedKeywordDTO.getMatchedYN().trim().equalsIgnoreCase("Y"))){
								if(!"".equals(lMisMatchType)){
									lMisMatchType=lMisMatchType+",";
								}
								lMisMatchType=lMisMatchType+" "+lComparedKeywordDTO.getSourcePatternType().substring(0,3).trim();
							}						
						} 
						
					}*/
					//System.out.println("Source:::->"+lComparedKeywordDTO.getSourceFormedStatement());
					
					//lSourceMisMatchString+=" "+"<font color=\""+lColor+"\"> "+lComparedKeywordDTO.getSourceKeyword()+" "+lComparedKeywordDTO.getSourceAlias()+" </font>";
					lTargetMisMatchString+=" "+"<font color=\""+lColor+"\"> "+lComparedKeywordDTO.getTargetKeyword()+" "+lComparedKeywordDTO.getTargetAlias()+" </font>";					
					
					//style=\"background-color
					lPrevSourceStatementNo=lComparedKeywordDTO.getSourceStatementNo().trim();
					lPrevComparedKeywordDTO=lComparedKeywordDTO;
				}
				
				
				
				
				
			}
			//System.out.println("</table>");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lComparedSummaryDTOLst;
	}
	/*public String getSummery(List lKeywordComparedList,String pCompareSeq,String pCreatedBy){
		
		try {
			ComparedKeywordDTO lComparedKeywordDTO=null;
			ComparedKeywordDTO lPrevComparedKeywordDTO=null;
			String lSourceMisMatchString="";
			String lTargetMisMatchString="";
			String lPrevSourceStatementNo="";
			
			String lColor="";
			String lMisMatchType="";
			String lMatchedYn ="";
			
			List lSourceMisMatchList= new ArrayList();
			List lTargetMisMatchList=new ArrayList();
			PseudoSqlDTO lSourcePseudoSqlDTO = new PseudoSqlDTO();
			PseudoSqlDTO lTargetPseudoSqlDTO = new PseudoSqlDTO();
			
			//System.out.println("<table width=\"100%\" border=\"2\" style=\"border-color=black\"> ");
			//System.out.println("<tr><th width=\"10%\" >Statement No</th><th width=\"40%\">Source</th><th width=\"40%\">Target</th><th width=\"10%\">MisMatchType</th></tr>");
			ComparedSummaryDTO lComparedSummaryDTO = null;
			boolean lChkForDelimeter=false;
			System.out.println(":::::::lKeywordComparedList size::::"+lKeywordComparedList.size());
			if(lKeywordComparedList!=null && lKeywordComparedList.size() >0 ){
				
				for (int j = 0; j <= lKeywordComparedList.size(); j++) {
					
					 
					
					//lComparedKeywordDTO=new ComparedKeywordDTO();
					if(j < lKeywordComparedList.size()){
						lComparedKeywordDTO=(ComparedKeywordDTO)lKeywordComparedList.get(j);
					}
					if( (!"".equals(lPrevSourceStatementNo) && !lComparedKeywordDTO.getSourceStatementNo().trim().equalsIgnoreCase(lPrevSourceStatementNo.trim()))
							|| (lSourceMisMatchString.trim().contains(ToolConstant.TOOL_DELIMT)))
					//Checking for the starting of the next statement to know the end of the previous statement using defined delimeter.
					if( ((lPrevComparedKeywordDTO!=null && j>0 ) && lComparedKeywordDTO.getSourceKeyword().equalsIgnoreCase(ToolConstant.TOOL_DELIMT))
							|| ( j == lKeywordComparedList.size() )) {
						//System.out.println("<tr>");
						//System.out.println("<td><b>"+lPrevComparedKeywordDTO.getSourceStatementNo()+"</b></td>");
						
						//System.out.println("<td>"+lSourceMisMatchString+"</td>");
						
						//System.out.println("<td>"+lTargetMisMatchString+"</td>");
						//System.out.println("<td>"+lMisMatchType+"</td>");
						//System.out.println("</tr>");
						
						///FOR ADDING INTO LIST - start
						
							//lSourceMisMatchString=reorderModifiedFormedStatements(lSourceMisMatchList);
							//lTargetMisMatchString=reorderModifiedFormedStatements(lTargetMisMatchList);
							lSourceMisMatchList= new ArrayList();
							lTargetMisMatchList=new ArrayList();
							
						
						////FOR ADDING INTO LIST - END
						
						//forming the DTO
						 lComparedSummaryDTO = new ComparedSummaryDTO();
						 lComparedSummaryDTO.setCompareSeq(pCompareSeq);
						 lComparedSummaryDTO.setCreatedBy(pCreatedBy);
						 lComparedSummaryDTO.setProcedureName(lPrevComparedKeywordDTO.getProcName());
						 //lComparedSummaryDTO.setSourceFormedStmt(ToolsUtil.removeToolChars(lPrevComparedKeywordDTO.getSourceFormedStatement(),ToolConstant.TOOL_DELIMT));
						 lComparedSummaryDTO.setSourceModifiedFormedStmt(ToolsUtil.removeToolChars(lSourceMisMatchString,ToolConstant.TOOL_DELIMT));
						 //lComparedSummaryDTO.setSourceModifiedFormedStmt(lSourceMisMatchString.replaceAll(ToolConstant.TOOL_DELIMT, ""));
						 //lComparedSummaryDTO.setSourceFormedStmt(lPrevComparedKeywordDTO.getSourceFormedStatement());
						 //lComparedSummaryDTO.setSourceModifiedFormedStmt(lSourceMisMatchString);
						 lComparedSummaryDTO.setSourcePatId(lPrevComparedKeywordDTO.getSourcePatternId());
						 lComparedSummaryDTO.setSourceStmtNo(lPrevComparedKeywordDTO.getSourceStatementNo());
						 //lComparedSummaryDTO.setTargetFormedStmt(ToolsUtil.removeToolChars(lPrevComparedKeywordDTO.getTargetFormedStatement(),ToolConstant.TOOL_DELIMT));
						 lComparedSummaryDTO.setTargetModifiedFormedStmt(ToolsUtil.removeToolChars(lTargetMisMatchString,ToolConstant.TOOL_DELIMT));
						 //lComparedSummaryDTO.setTargetFormedStmt(lPrevComparedKeywordDTO.getTargetFormedStatement());
						 //lComparedSummaryDTO.setTargetModifiedFormedStmt(lTargetMisMatchString);
						 
						 lComparedSummaryDTO.setTargetPatId(lPrevComparedKeywordDTO.getTargetPatternId());
						 lComparedSummaryDTO.setTargetStmtNo(lPrevComparedKeywordDTO.getTargetStatementNo());
						 lComparedSummaryDTO.setMisMatchCategory(lMisMatchType);
						 lComparedSummaryDTO.setMisMatchCategoryDesc("");
						 
						 if("".equals(lMisMatchType)){
							if("".equals(lPrevComparedKeywordDTO.getSourcePatternId().trim()) || "".equals(lPrevComparedKeywordDTO.getTargetPatternId().trim())){
								lMatchedYn ="N";
							}else{
								lMatchedYn ="Y";
							}
						 }else{
							 lMatchedYn ="N";
						 }
						 
						 lComparedSummaryDTO.setMatchedYN(lMatchedYn);
						 lComparedSummaryDTO.setOrderNo(lPrevComparedKeywordDTO.getOrderNo());
						 
						 //inserting the modified formed statements
						insertIdentifyPatternDetails(lComparedSummaryDTO);
						
						lSourceMisMatchString="";
						lTargetMisMatchString="";
						lMisMatchType="";
					}
					if(j == lKeywordComparedList.size()){
						break;
					}
					lColor="BLACK";
					if(lComparedKeywordDTO.getMatchedYN().trim().equalsIgnoreCase("N")){
						lColor="RED";
					}
					if(lComparedKeywordDTO.getMatchedYN().trim().equalsIgnoreCase("P")){
						lColor="#CC6600";
					}
					//System.out.println("Chk "+lOuterComparedKeywordDTO.getSourceStatementNo().trim());
					
					
					if(lComparedKeywordDTO.getSourceKeyword().trim().toUpperCase().startsWith("@") 
							|| lComparedKeywordDTO.getTargetKeyword().trim().toUpperCase().startsWith("V_") 
							|| lComparedKeywordDTO.getTargetKeyword().trim().toUpperCase().startsWith("SWV_")){
						if(!lMisMatchType.toUpperCase().contains("VAR")){
							if(!"".equals(lMisMatchType)){
								lMisMatchType=lMisMatchType+",";
							}
							lMisMatchType=lMisMatchType+" VAR";
						}
						
						
					}else{
						if(!"".equals(lComparedKeywordDTO.getSourcePatternType()) ){
							if( (!lMisMatchType.toUpperCase().contains(lComparedKeywordDTO.getSourcePatternType().substring(0,3).trim().toUpperCase()))
									&& (!lComparedKeywordDTO.getMatchedYN().trim().equalsIgnoreCase("Y"))){
								if(!"".equals(lMisMatchType)){
									lMisMatchType=lMisMatchType+",";
								}
								lMisMatchType=lMisMatchType+" "+lComparedKeywordDTO.getSourcePatternType().substring(0,3).trim();
							}						
						} 
						
					}
					//System.out.println("Source:::->"+lComparedKeywordDTO.getSourceFormedStatement());
					lSourceMisMatchString+=" "+"<font bgcolor= \""+lColor+"\">"+lComparedKeywordDTO.getSourceKeyword()+" "+lComparedKeywordDTO.getSourceAlias()+"</font>";
					lTargetMisMatchString+=" "+"<font bgcolor= \""+lColor+"\">"+lComparedKeywordDTO.getTargetKeyword()+" "+lComparedKeywordDTO.getTargetAlias()+"</font>";
					lSourceMisMatchString+=" "+"<font color=\""+lColor+"\"> "+lComparedKeywordDTO.getSourceKeyword()+" "+lComparedKeywordDTO.getSourceAlias()+" </font>";
					lTargetMisMatchString+=" "+"<font color=\""+lColor+"\"> "+lComparedKeywordDTO.getTargetKeyword()+" "+lComparedKeywordDTO.getTargetAlias()+" </font>";
					
					///FOR ADDING INTO LIST - start
					
					
						 lSourcePseudoSqlDTO = new PseudoSqlDTO();
						lSourcePseudoSqlDTO.setActualString(" "+"<font color=\""+lColor+"\"> "+lComparedKeywordDTO.getSourceKeyword()+" "+lComparedKeywordDTO.getSourceAlias()+" </font>");
						lSourcePseudoSqlDTO.setIndex(Integer.parseInt(lComparedKeywordDTO.getSourceIndex()));
						lSourcePseudoSqlDTO.setSubIndex(Integer.parseInt(lComparedKeywordDTO.getSourceSubIndex()));
						lSourceMisMatchList.add(lSourcePseudoSqlDTO);
						
						 lTargetPseudoSqlDTO = new PseudoSqlDTO();
						lTargetPseudoSqlDTO.setActualString(" "+"<font color=\""+lColor+"\"> "+lComparedKeywordDTO.getTargetKeyword()+" "+lComparedKeywordDTO.getTargetAlias()+" </font>");
						lTargetPseudoSqlDTO.setIndex(Integer.parseInt(lComparedKeywordDTO.getTargetIndex()));
						lTargetPseudoSqlDTO.setSubIndex(Integer.parseInt(lComparedKeywordDTO.getTargetSubIndex()));
						lTargetMisMatchList.add(lTargetPseudoSqlDTO);
						
						
					
					////FOR ADDING INTO LIST - END
					
					
					//style=\"background-color
					lPrevSourceStatementNo=lComparedKeywordDTO.getSourceStatementNo().trim();
					lPrevComparedKeywordDTO=lComparedKeywordDTO;
				}
				
				
				
				
				
			}
			//System.out.println("</table>");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}*/
	
	public int insertIdentifyPatternDetails(ComparedSummaryDTO pComparedSummaryDTO) {
		 int lInsertCount = 0;
	 		try {
	 			
	 			
	 			
	 			//String pCompareSeq,String pProcedureName,String pSourceStmtNo,String pSourceModifiedFormedStmt,String pSourceFormedStmt,String pSourcePatId,String pTargetStmtNo,String pTargetModifiedFormedStmt,String pTargetFormedStmt,String pTargetPatId,String pCreatedBy
	 			
	 			
	 			
	 			// prepare the query
	 			//lPreparedStatementInsert = lConnection.prepareStatement(ToolConstant.INSERT_COMPARE_STATEMENT_AFTER_MODIFICATION);
	 			lPreparedStatementInsert.setString(1, pComparedSummaryDTO.getCompareSeq());
	 			lPreparedStatementInsert.setString(2, pComparedSummaryDTO.getProcedureName());		 			
	 			lPreparedStatementInsert.setString(3, pComparedSummaryDTO.getSourceStmtNo());
	 			lPreparedStatementInsert.setString(4, ToolsUtil.removeToolKeywords(pComparedSummaryDTO.getSourceModifiedFormedStmt()));
	 			lPreparedStatementInsert.setString(5, pComparedSummaryDTO.getSourceFormedStmt());	 			
	 			lPreparedStatementInsert.setString(6, pComparedSummaryDTO.getSourcePatId());
	 			
	 			lPreparedStatementInsert.setString(7, pComparedSummaryDTO.getTargetStmtNo());
	 			lPreparedStatementInsert.setString(8, ToolsUtil.removeToolKeywords(pComparedSummaryDTO.getTargetModifiedFormedStmt()));
	 			lPreparedStatementInsert.setString(9, pComparedSummaryDTO.getTargetFormedStmt());	 			
	 			lPreparedStatementInsert.setString(10, pComparedSummaryDTO.getTargetPatId());
	 			lPreparedStatementInsert.setString(11, pComparedSummaryDTO.getCreatedBy());
	 			
	 			lPreparedStatementInsert.setTimestamp(12, new Timestamp(System
	 					.currentTimeMillis()));
	 			lPreparedStatementInsert.setString(13, pComparedSummaryDTO.getMisMatchCategory());
	 			lPreparedStatementInsert.setString(14, pComparedSummaryDTO.getMisMatchCategoryDesc());
	 			lPreparedStatementInsert.setString(15, pComparedSummaryDTO.getMatchedYN());
	 			lPreparedStatementInsert.setString(16, pComparedSummaryDTO.getOrderNo());
	 			lPreparedStatementInsert.setString(17, pComparedSummaryDTO.getPerformanceImpact());
	 			lPreparedStatementInsert.setString(18, pComparedSummaryDTO.getPerformanceImpactDesc());
	 			
	 			//System.out.println("::::::source formed stmt::::::"+pComparedSummaryDTO.getTargetModifiedFormedStmt());
	 			//lInsertCount = lPreparedStatementInsert.executeUpdate();
	 			lPreparedStatementInsert.addBatch();
	 			lQueryCount++;
	 			 if(lQueryCount%100==0){ 
	 				lPreparedStatementInsert.executeBatch();
					lConnection.commit();
					lQueryCount=0;
					
				 }
	 			
	 		} catch (SQLException pSQLException) {
	 			pSQLException.printStackTrace();
	 		} catch (Exception pException) {
	 			pException.printStackTrace();
	 		} finally {
	 			/*DBConnectionManager.closeConnection(lPreparedStatement,
	 					lResultSet);
	 			DBConnectionManager.closeConnection(lConnection);*/
	 		}
	 		return lInsertCount;

	 	}
	
	public String reorderModifiedFormedStatements(List lReorderList){
		PseudoSqlDTO lOuterPseudoSqlDTO = new PseudoSqlDTO();
		PseudoSqlDTO lInnerPseudoSqlDTO = new PseudoSqlDTO();
		List lResultList=new ArrayList();
		List lTempListPerIndex=null;
		String lRetString="";
		if(lReorderList != null && lReorderList.size() >0){
			for (int i = 0; i < lReorderList.size(); i++) {
				lTempListPerIndex=new ArrayList();
				lOuterPseudoSqlDTO = (PseudoSqlDTO)lReorderList.get(i);				
				for (int j = 0; j < lReorderList.size(); j++) {
					lInnerPseudoSqlDTO=(PseudoSqlDTO)lReorderList.get(j);
					if(lOuterPseudoSqlDTO.getIndex()==lInnerPseudoSqlDTO.getIndex()){						
						lTempListPerIndex.add(lInnerPseudoSqlDTO);
					}					
				}
				lTempListPerIndex=sortList(lTempListPerIndex);
				for (int j = 0; j < lTempListPerIndex.size(); j++) {
					lResultList.add((PseudoSqlDTO)lTempListPerIndex.get(j));
				}
				
				//lResultList.add(lTempListPerIndex);				
			}
			for (int i = 0; i < lResultList.size(); i++) {
				lRetString+=" "+((PseudoSqlDTO)lResultList.get(i)).getActualString();
			}
			//System.out.println("lRetString::->"+lRetString);
			
		}
		return lRetString;
	}
	public List sortList(List pInputList){
		List lResultList=pInputList;
		PseudoSqlDTO lPseudoSqlDTO=new PseudoSqlDTO();
		for (int i = 0; i < pInputList.size(); i++) {
			lPseudoSqlDTO = (PseudoSqlDTO)pInputList.get(i);		
			lResultList.set(lPseudoSqlDTO.getSubIndex(), lPseudoSqlDTO);
		}
		return lResultList;
	}
	public static void main(String[] args){
		
		try {
			System.out.println("::::::::::In Main Method::::::::::");
			String pCompareSeq="PRID92"; //52
			ComparisonSummeryDAO lKeywordCompareDAO=new ComparisonSummeryDAO();
			lKeywordCompareDAO.prepareSummery(pCompareSeq,"PruUser");
			System.out.println("::::::::::Method Ends::::::::::");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

