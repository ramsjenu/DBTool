package com.tcs.tools.business.compare.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.tcs.tools.business.compare.dto.ComparedStatementDTO;
import com.tcs.tools.business.compare.dto.MatchResultDTO;
import com.tcs.tools.business.compare.dto.PseudoSqlDTO;
import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.web.dao.InventoryAnalyticsDAO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class DeepDiveComparisonDAO {
	
	private static Logger logger = Logger.getLogger("ToolLogger");

	Connection lConnection=null;
	PreparedStatement lPreparedStatement=null;
	PreparedStatement lInsertPreparedStatement=null;	
	HashMap lKeywordCategoryMap=null;
	List lDelimtersList=null;
	HashMap lKeywordPairingMap=null;
	HashMap lVarUsageMap = null;
	HashMap lSourceToTargetKeywordMap=null;
	
	
	int lQueryCount=0;
	String lCurState="";
	 String lStausMsg="";
	/*public HashMap prepareKeywordPairMap(){
		lKeywordPairingMap=new HashMap();
		lKeywordPairingMap.put((String)"CALL",(String) "EXEC");
		lKeywordPairingMap.put((String)"SET",(String) "EXEC");
		lKeywordPairingMap.put((String)"AND",(String) "WHERE");
		lKeywordPairingMap.put((String)"OR",(String) "WHERE");
		lKeywordPairingMap.put((String)"TOOL_OTHER_(",(String) "EXEC");
		
		//TOOL_OTHER_(
		return lKeywordPairingMap;
	}
	*/
	
	
	
	public HashMap getVarUsageMap(String lRunId,String pProcedureName) {
		PreparedStatement lPreparedStatement;
		Connection lConnection;
		HashMap lVarUsageMap = new HashMap();
		ResultSet lResultSetNew = null;
		try {
			lConnection = DBConnectionManager.getConnection();

			// replacing the special characters in the query string

			// select the data from the table
			// if required we can add % in the like operator
			String lSQL = "SELECT * FROM VARIABLE_USAGE_DETAILS_TABLE  WHERE RUN_ID=? AND PROCEDURE_NAME=? ";
			lPreparedStatement = lConnection.prepareStatement(lSQL);
			lPreparedStatement.setString(1, lRunId);
			lPreparedStatement.setString(2, pProcedureName);
			int i = 0;
			lResultSetNew = lPreparedStatement.executeQuery();
			// if rs == null, then there is no ResultSet to view
			
			
			if (lResultSetNew != null) {
				// this will step through our data row-by-row
				while (lResultSetNew.next()) {
					// System.out.println("Data from column_name: " +
					// lResultSet.getString("STATEMENT_TYPE"));
					// retValue = lResultSetNew.getString("STATEMENT_TYPE");						
						// System.out.println(lPattern+
						// "::: Category"+lResultSetNew.getString("STATEMENT_TYPE"));
						lVarUsageMap.put(lResultSetNew.getString("SOURCE_VARIABLE").trim().toUpperCase(),
								lResultSetNew.getString("TARGET_VARIABLE").trim().toUpperCase());
						//System.out.println(lResultSetNew.getString("SOURCE_VARIABLE").trim().toUpperCase()+"  ::->  "+lResultSetNew.getString("TARGET_VARIABLE"));
						
							
						//	System.out.println("VARIABLE USAGE MAP "+lVarUsageMap);
						

					
				}
			}

		} catch (SQLException se) {
			se.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// close the connection and the result set

		}

		return lVarUsageMap;
	}
	
	public HashMap getSourceToTargetKeywordMap(String pDbMigrationType) {
		PreparedStatement lPreparedStatement;
		Connection lConnection;
		HashMap lSourceToTargetKeywordMap = new HashMap();
		ResultSet lResultSetNew = null;
		try {
			lConnection = DBConnectionManager.getConnection();

			// replacing the special characters in the query string

			// select the data from the table
			// if required we can add % in the like operator
			String lSQL = "SELECT STATEMENT_TYPE, SOURCE_KEYWORD, TARGET_KEYWORD FROM SOURCE_TARGET_KEYWORD_MATCH_TABLE WHERE DB_MIGRATION_TYPE=?";
			lPreparedStatement = lConnection.prepareStatement(lSQL);
			lPreparedStatement.setString(1, pDbMigrationType);			
			int i = 0;
			lResultSetNew = lPreparedStatement.executeQuery();
			// if rs == null, then there is no ResultSet to view
			
			
			if (lResultSetNew != null) {
				// this will step through our data row-by-row
				while (lResultSetNew.next()) {
					// System.out.println("Data from column_name: " +
					// lResultSet.getString("STATEMENT_TYPE"));
					// retValue = lResultSetNew.getString("STATEMENT_TYPE");						
						// System.out.println(lPattern+
						// "::: Ctegory"+lResultSetNew.getString("STATEMENT_TYPE"));
						lSourceToTargetKeywordMap.put(lResultSetNew.getString("SOURCE_KEYWORD").trim().toUpperCase(),
								lResultSetNew.getString("TARGET_KEYWORD").trim().toUpperCase());
					//	System.out.println(lResultSetNew.getString("SOURCE_KEYWORD").trim().toUpperCase()+"  ::->  "+lResultSetNew.getString("TARGET_KEYWORD"));
					
				}
			}

		} catch (SQLException se) {
			se.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// close the connection and the result set

		}

		return lSourceToTargetKeywordMap;
	}
	
	public HashMap prepareKeywordPairMap(){
		lKeywordPairingMap=new HashMap();
		List lKeywordMappingList = new ArrayList();
		
		//STRING PRESENT IN Hash map will the the source string
		//List contents will have the probable  match in the target
				
		lKeywordMappingList.add("SELECT");
		lKeywordMappingList.add("SET");
		lKeywordMappingList.add("INTO");
		lKeywordMappingList.add("DECLARE");
		lKeywordPairingMap.put((String)"SELECT",(List)lKeywordMappingList);
		
		lKeywordMappingList = new ArrayList();
		lKeywordMappingList.add( "CALL");
		lKeywordMappingList.add( "SET");
		lKeywordMappingList.add( "TOOL_OTHER_(");
		lKeywordMappingList.add( "EXECUTE");
		lKeywordPairingMap.put((String) "EXEC",(List)lKeywordMappingList);
		
		lKeywordMappingList = new ArrayList();
		lKeywordMappingList.add( "AND");
		lKeywordMappingList.add( "OR");
		lKeywordPairingMap.put((String) "WHERE",(List)lKeywordMappingList);
		
		lKeywordMappingList = new ArrayList();
		lKeywordMappingList.add( "PROCEDURE");
		
		lKeywordPairingMap.put((String) "PROC",(List)lKeywordMappingList);
		
		
		lKeywordMappingList = new ArrayList();
		lKeywordMappingList.add( "CALL");
		lKeywordMappingList.add( "SET");
		lKeywordMappingList.add( "TOOL_OTHER_(");
		lKeywordMappingList.add( "EXEC");
		lKeywordPairingMap.put((String) "EXECUTE",(List)lKeywordMappingList);
		
		lKeywordMappingList = new ArrayList();
		lKeywordMappingList.add( "TABLE");		
		lKeywordPairingMap.put((String) "TABLE_TOOL_COLUMN_(",(List)lKeywordMappingList);
		//TABLE_TOOL_COLUMN_( 
		
		/*lKeywordMappingList = new ArrayList();
		lKeywordMappingList.add("SELECT");		
		lKeywordPairingMap.put((String)"INTO",(List)lKeywordMappingList);
		*/
		
		
		//TOOL_OTHER_(
		return lKeywordPairingMap;
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
	public List getKeywordsForOtherNamesList(){
		List lKeywordsForOtherNamesLst = new ArrayList();
		lKeywordsForOtherNamesLst.add((String) "PROCEDURE");
		lKeywordsForOtherNamesLst.add((String) "PROC");
		lKeywordsForOtherNamesLst.add((String) "INDEX");
		lKeywordsForOtherNamesLst.add((String) "EXEC");
		lKeywordsForOtherNamesLst.add((String) "EXECUTE");
		lKeywordsForOtherNamesLst.add((String) "CALL");
		lKeywordsForOtherNamesLst.add((String) "CREATE");		
		lKeywordsForOtherNamesLst.add((String) "COMMIT");
		lKeywordsForOtherNamesLst.add((String) "SAVE");
		lKeywordsForOtherNamesLst.add((String) "ROLLBACK");
		lKeywordsForOtherNamesLst.add((String) "OPEN");
		lKeywordsForOtherNamesLst.add((String) "CLOSE");
		lKeywordsForOtherNamesLst.add((String) "FETCH");
		lKeywordsForOtherNamesLst.add((String) "TO");
		lKeywordsForOtherNamesLst.add((String) "GO");
		lKeywordsForOtherNamesLst.add((String) "GRANT");
		lKeywordsForOtherNamesLst.add((String) "DECLARE");
		lKeywordsForOtherNamesLst.add((String) "GLOBAL");
		lKeywordsForOtherNamesLst.add((String) "TEMPORARY");
		lKeywordsForOtherNamesLst.add((String) "CURSOR");
		lKeywordsForOtherNamesLst.add((String) "WITH");
		lKeywordsForOtherNamesLst.add((String) "RETURN");
		lKeywordsForOtherNamesLst.add((String) "WITH_RETURN");
		lKeywordsForOtherNamesLst.add((String) "FOR");
		lKeywordsForOtherNamesLst.add((String) "TOOL_OTHER_(");
		lKeywordsForOtherNamesLst.add((String) "IF");
		
		return lKeywordsForOtherNamesLst;
		
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

	

	/*
	 * SELECT UPDATE INSERT DELETE FROM SET INTO VALUES WHERE EXISTS NOT EXISTS
	 * , ColNames TableNames Functions(Sybase n DB2) Other(Text,Operator,Other
	 * Keywords,Symbols,Text in quotes,)
	 */

	public HashMap getKeywordCategoryList(String pDbMigrationType) {
		PreparedStatement lPreparedStatement;
		Connection lConnection;
		HashMap lhmPatternData = new HashMap();
		ResultSet lResultSetNew = null;
		try {
			lConnection = DBConnectionManager.getConnection();

			// replacing the special characters in the query string

			// select the data from the table
			// if required we can add % in the like operator
			String lSQL = "select DISTINCT  PATTERN, STATEMENT_TYPE from PATTERN_LOOKUP_TABLE where  VALID_YN ='Y' and DB_MIGRATION_TYPE = ? ";
			lPreparedStatement = lConnection.prepareStatement(lSQL);
			lPreparedStatement.setString(1, pDbMigrationType);
			int i = 0;
			lResultSetNew = lPreparedStatement.executeQuery();
			// if rs == null, then there is no ResultSet to view
			String lPattern = "";
			String lStatementType = "";
			if (lResultSetNew != null) {
				// this will step through our data row-by-row
				while (lResultSetNew.next()) {
					// System.out.println("Data from column_name: " +
					// lResultSet.getString("STATEMENT_TYPE"));
					// retValue = lResultSetNew.getString("STATEMENT_TYPE");
					lPattern = lResultSetNew.getString("PATTERN");
						//System.out.println("PATTERN:::::::"+lPattern);
					if (lPattern != null) {
						lPattern = lPattern.toUpperCase();
						// System.out.println(lPattern+
						// "::: Category"+lResultSetNew.getString("STATEMENT_TYPE"));
						lhmPatternData.put(lPattern,
								lResultSetNew.getString("STATEMENT_TYPE"));
					}
				}
			}

		} catch (SQLException se) {
			se.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// close the connection and the result set

		}

		return lhmPatternData;
	}

	
	public List getBlockList(String pFormedStatement){
		
		String lPatternFormed = "";
		
		//common part for replace set of strings - start
		pFormedStatement=pFormedStatement.replaceAll(",", " , ");
		pFormedStatement=pFormedStatement.replaceAll("\\(", " \\( ");
		pFormedStatement=pFormedStatement.replaceAll("\\)", " \\) ");
		Pattern lInsertBracketColumnPattern = Pattern.compile("\\bINSERT\\b[\\s\\W\\w\\r\\n]+\\([\\s\\W\\w\\r\\n]+");
		Pattern lCallPattern = Pattern.compile("\\bCALL\\b[\\s\\W\\w\\r\\n]+\\([\\s\\W\\w\\r\\n]+");
		Pattern lCreateTablePattern = Pattern.compile("\\bCREATE\\b\\s+\\bTABLE\\b[\\s\\W\\w\\r\\n]+\\([\\s\\W\\w\\r\\n]+");
		Pattern lDeclareGlobalTablePattern = Pattern.compile("\\bDECLARE\\b\\s+\\bGLOBAL\\b\\s+\\bTEMPORARY\\b\\s+\\bTABLE\\b[\\s\\W\\w\\r\\n]+\\([\\s\\W\\w\\r\\n]+");
		if(lInsertBracketColumnPattern.matcher(pFormedStatement.toUpperCase()).find()){
			pFormedStatement = pFormedStatement.replaceFirst("\\(", "INSERT_TOOL_COLUMN (");
		}
		if(lCallPattern.matcher(pFormedStatement.toUpperCase()).find()){
			pFormedStatement = pFormedStatement.replaceFirst("\\(", "TOOL_OTHER_( ");
		}
		if(lCreateTablePattern.matcher(pFormedStatement.toUpperCase()).find()){
			pFormedStatement = pFormedStatement.replaceFirst("\\(", "TABLE_TOOL_COLUMN_( ");
		}
		if(lDeclareGlobalTablePattern.matcher(pFormedStatement.toUpperCase()).find()){
			pFormedStatement = pFormedStatement.replaceFirst("\\(", "TABLE_TOOL_COLUMN_( ");
		}
		//common part for replace set of strings - end
		
		
		String[] lWordsArr = pFormedStatement.split("\\s+");
	
		String lPrevDelim = "";
		String lTmpStr = "";
		int lChkOpenBrace = 0;
		boolean lIsVisitedFunction = false;
		List lBlocks = new ArrayList();
		String lCurBolck = "";
		int lSubQueryCount = 0;
	
		lCurBolck = lSubQueryCount+1+"_DBT_";
		for (int i = 0; i < lWordsArr.length; i++) {
			
			

			if ((i > 0) && (lWordsArr[i].trim().equalsIgnoreCase("SELECT"))
					&& (lWordsArr[i - 1].trim().equals("("))) {
				lSubQueryCount++;
			}
			

			if ((lDelimtersList.contains((String) lWordsArr[i].trim()
					.toUpperCase()) /*&& (lSubQueryCount == 0)*/ )
					|| (lWordsArr[i].trim().equals(",") && (lChkOpenBrace == 0) )) {

				// System.out.println("Delimiter:::->"+lWordsArr[i]+"::::::lPatternFormed::::"+lPrevDelim+"_"+lPatternFormed);
				if(!"".equals(lCurBolck.trim()) && i>0){
					lBlocks.add((String)lCurBolck.trim());
					//System.out.println("In::-:>"+lCurBolck.trim());
					lCurBolck = lSubQueryCount+1+"_DBT_";
				}
				
				lBlocks.add((String) (lSubQueryCount+1+"_DBT_")+lWordsArr[i].trim());
				// lPatternFormed ="";
				lPrevDelim = lWordsArr[i].trim().toUpperCase();
				//System.out.println("lWordsArr[i]::->if--->"+lWordsArr[i]+"lChkOpenBrace::->"+lChkOpenBrace);
			} else {
				/*if (lSubQueryCount == 0) {
					lCurBolck += lWordsArr[i] + " ";
				} else if (lWordsArr[i].trim().equalsIgnoreCase("SELECT")) {
					lCurBolck += lWordsArr[i] + " ";
				}*/
				lCurBolck += lWordsArr[i] + " ";
				//System.out.println("lWordsArr[i]::->else-->"+lWordsArr[i]+"lChkOpenBrace::->"+lChkOpenBrace);
				/*
				 * if(lSubQueryCount>0 ){
				 * 
				 * if(prepareDelimtersList().contains((String)lWordsArr[i].trim()
				 * .toUpperCase())){ lCurBolck+=lWordsArr[i]+"_";
				 * //lPatternFormed += lWordsArr[i].trim().toUpperCase(); }
				 * 
				 * 
				 * }else{ lCurBolck+=lWordsArr[i]+" "; //lPatternFormed +=
				 * lWordsArr[i].trim().toUpperCase(); }
				 */

			}

			if (lWordsArr[i].trim().equals("(")/* && (lIsVisitedFunction==true) */) {
				lChkOpenBrace++;
				// System.out.println("1  lChkOpenBrace::->"+lChkOpenBrace+" lSubQueryCount::->"+lSubQueryCount
				// );
			}
			if (lWordsArr[i].trim().equals(")") /* && (lIsVisitedFunction==true) */) {
				lChkOpenBrace--;
				if(lChkOpenBrace<0){
					lChkOpenBrace=0;
				}
				if (lChkOpenBrace == 0 && lIsVisitedFunction == true) {
					lIsVisitedFunction = false;
				}
				// System.out.println("2  lChkOpenBrace::->"+lChkOpenBrace+" lSubQueryCount::->"+lSubQueryCount
				// );
				if (lChkOpenBrace == lSubQueryCount - 1 && lSubQueryCount > 0) {
					lSubQueryCount--;
					if (lSubQueryCount == 0) {
						lCurBolck = lCurBolck + "  )";
					}
				}
			}

		}
		
		lBlocks.add((String) lCurBolck);
		//System.out.println("Out::-:>"+lCurBolck.trim());
		/*System.out.println(":::: Bolcks Print::::::::::");
		for (int j = 0; j < lBlocks.size(); j++) {
			logger.info("::::::lCurBolck::::::"+lBlocks.get(j));
		  //System.out.println(lBlocks.get(j));
		  }*/
		 
		return lBlocks;
	}
	public List getPatternString(String pFormedStatement) {
		

		String lSetColumnArr[];
		String lTmpSelectStr = "";
		String lStartKeyWord = "";
		String lEndKeyWord = "";
		String lActualString = "";
		String lPatternString = "";
		List lCollectedBlocks = new ArrayList();
		PseudoSqlDTO lPseudoSqlDTO = new PseudoSqlDTO();
		String[] lColDataArr = new String[2]; 
		String[] lTabData={"",""};
		String lLevelCount="";
		
		/*List lKeywordsForTablesNamesLst = getKeywordsForTablesNamesList();
		List lKeywordsForColumnNamesLst = getKeywordsForColumnNamesList();*/
		List lKeywordsForTablesNamesLst = new ArrayList();
		List lKeywordsForColumnNamesLst = new ArrayList();
		List lKeywordsForOtherNamesLst = getKeywordsForOtherNamesList();
		
		String lTmpStr= "";
		
		
		 List lBlocks = getBlockList(pFormedStatement);
		// System.out.println("BLOCKS:::::::"+lBlocks);
		 if(lBlocks.size()==0 || lBlocks==null){
			 return lCollectedBlocks;
		 }
		 //String lPrimarySql=(String)lBlocks.get(0);
		 String lPrimarySql="";
		 if(lBlocks.size()>2){			 
			 lPrimarySql=(String)lBlocks.get(2); 
			 lPrimarySql=lPrimarySql.substring(lPrimarySql.indexOf("_DBT_")+5,lPrimarySql.length());
		 }
		
		//String lPrimarySql=(String)lBlocks.get(2);// 1st index will be the primary sql becuase delimeter will be there at 0th position.
		//for creating blocks
		for (int i = 0; i < lBlocks.size(); i++) {
			// System.out.println(" Index "+i+"  ::->"+lBlocks.get(i).toString());
			/*
			 * lActualString=""; lStartKeyWord=""; lEndKeyWord="";
			 */
			lActualString=((String) lBlocks.get(i)).trim();
			lLevelCount=lActualString.substring(0,lActualString.indexOf("_DBT_")).trim();
			lActualString = lActualString.substring(lActualString.indexOf("_DBT_")+5,lActualString.length());
			//System.out.println("ACTUAL STRING::::::"+lActualString);
			if("".equals(lActualString.trim())){
				continue;
			}
			//if ((lDelimtersList.contains(((String) lBlocks.get(i))
			if ((lDelimtersList.contains((lActualString).toUpperCase()))) {
				// lPatternString+=";"+(String) lBlocks.get(i);
			//	System.out.println("CONDITION:::::::"+(lDelimtersList.contains((lActualString).toUpperCase()))+"ACTUAL KEYWORD:::::"+lActualString);
				lStartKeyWord = lActualString;
				lPseudoSqlDTO = new PseudoSqlDTO();
				lPseudoSqlDTO.setIndex(i);
				lPseudoSqlDTO.setPseudoSql(lStartKeyWord);
				lPseudoSqlDTO.setActualString(lStartKeyWord);				
				lPseudoSqlDTO.setPatternType("SQL KEYWORD");
				lPseudoSqlDTO.setPrevKeyword(lStartKeyWord);
				lPseudoSqlDTO.setLevelCount(lLevelCount);
				lPseudoSqlDTO.setPrimarySql(lPrimarySql);
				lCollectedBlocks.add(lPseudoSqlDTO);
				// System.out.println("lStartKeyWord;;->"+lStartKeyWord);
			} else {
				
							

				//if (!(",".equalsIgnoreCase((String) lBlocks.get(i)))) {
				if (!("".equalsIgnoreCase(lActualString))) {
					//System.out.println("lLevelCount::->"+lLevelCount+"  lStartKeyWord;;->"+lStartKeyWord+"  lActualString;;->"+lActualString);
					
					
					if (lKeywordsForTablesNamesLst
							.contains((String) lStartKeyWord.toUpperCase())) {
						// System.out.println(lActualString+" --> TABLE NAME");
						
						if(lActualString.contains(",")){
							String [] lSetTabArr = lActualString.split(",");
							for (int j = 0; j < lSetTabArr.length; j++) {
								lTabData=getTablePart(lSetTabArr[j]);
								lPatternString += lStartKeyWord + "_T_" + lActualString+ ";";								
								lPseudoSqlDTO = new PseudoSqlDTO();
								lPseudoSqlDTO.setIndex(i);
								lPseudoSqlDTO.setSubIndex(j);
								lPseudoSqlDTO.setPseudoSql(lPatternString);
								lPseudoSqlDTO.setActualString(lTabData[0]);
								lPseudoSqlDTO.setAliasName(lTabData[1]);
								lPseudoSqlDTO.setPatternType("TABLE");
								lPseudoSqlDTO.setPrevKeyword(lStartKeyWord);
								lPseudoSqlDTO.setLevelCount(lLevelCount);
								lPseudoSqlDTO.setPrimarySql(lPrimarySql);
								lCollectedBlocks.add(lPseudoSqlDTO);
								
								lPatternString = "";
								
								
								//for table alias								
								/*lPseudoSqlDTO = new PseudoSqlDTO();
								lPseudoSqlDTO.setIndex(i);
								lPseudoSqlDTO.setPseudoSql(lPatternString);
								lPseudoSqlDTO.setActualString(lTabData[1]);
								//lPseudoSqlDTO.setAliasName(lTabData[1]);
								lPseudoSqlDTO.setPatternType("TABLE_ALIAS");
								lPseudoSqlDTO.setPrevKeyword(lStartKeyWord);
								lCollectedBlocks.add(lPseudoSqlDTO);
								lPatternString = "";*/
								
							}
						}
					/*	else if (lStartKeyWord.trim().equalsIgnoreCase("INTO")) {
							Pattern p = Pattern.compile("(?i)\\bINSERT\\b[\\s\\r\\n]+\\bINTO\\b[\\w|\\W]{1,}");
							System.out.println("PATTERN::::::::: "+p.matcher(lActualString.toUpperCase()).find());
							if (p.matcher(lActualString.toUpperCase()).find()) {
								lPatternString=lStartKeyWord + "_T_" + lActualString+ ";";
								lPseudoSqlDTO = new PseudoSqlDTO();
								lPseudoSqlDTO.setIndex(i);
								lPseudoSqlDTO.setPseudoSql(lPatternString);
								lPseudoSqlDTO.setActualString(lTabData[0]);
								lPseudoSqlDTO.setAliasName(lTabData[1]);
								lPseudoSqlDTO.setPatternType("TABLE");
								lPseudoSqlDTO.setPrevKeyword(lStartKeyWord);
								lPseudoSqlDTO.setLevelCount(lLevelCount);
								lPseudoSqlDTO.setPrimarySql(lPrimarySql);
								lCollectedBlocks.add(lPseudoSqlDTO);
								lPatternString = "";
							}
						}*/
						else{
							lTabData=getTablePart(lActualString);
							lPatternString += lStartKeyWord + "_T_" + lActualString+ ";";							
							lPseudoSqlDTO = new PseudoSqlDTO();
							lPseudoSqlDTO.setIndex(i);
							lPseudoSqlDTO.setPseudoSql(lPatternString);
							lPseudoSqlDTO.setActualString(lTabData[0]);
							lPseudoSqlDTO.setAliasName(lTabData[1]);
							lPseudoSqlDTO.setPatternType("TABLE");
							lPseudoSqlDTO.setPrevKeyword(lStartKeyWord);
							lPseudoSqlDTO.setLevelCount(lLevelCount);
							lPseudoSqlDTO.setPrimarySql(lPrimarySql);
							lCollectedBlocks.add(lPseudoSqlDTO);
							lPatternString = "";
							//for table alias
							/*lPseudoSqlDTO = new PseudoSqlDTO();
							lPseudoSqlDTO.setIndex(i);
							lPseudoSqlDTO.setPseudoSql(lPatternString);
							lPseudoSqlDTO.setActualString(lTabData[1]);
							//lPseudoSqlDTO.setAliasName(lTabData[1]);
							lPseudoSqlDTO.setPatternType("TABLE_ALIAS");
							lPseudoSqlDTO.setPrevKeyword(lStartKeyWord);
							lCollectedBlocks.add(lPseudoSqlDTO);
							lPatternString = "";*/
							
						}
							
						
					} else if (lKeywordsForColumnNamesLst
							.contains(((String) lStartKeyWord).toUpperCase())) {
						//System.out.println(lActualString+" --> COLUMNS NAME");
						/*if (lActualString.trim().contains(" ")
								|| lActualString.trim().contains("=")) {
							
						}*/
							//System.out.println("-----------------"+lActualString+"  "+lStartKeyWord);
							if (lStartKeyWord.trim().equalsIgnoreCase("SET")) {
								Pattern p = Pattern
										.compile("\\([\\s\\w\\W\\n\\r]*\\)\\s*=\\s*\\(\\s*SELECT[\\s\\w\\W\\n\\r]*");
								
								if (p.matcher(lActualString.toUpperCase())
										.find()) {
									lTmpStr = lActualString.substring(
											lActualString.indexOf("(") + 1,
											lActualString.indexOf("="));
									lTmpStr = lTmpStr.replaceAll("\\)", "")
											.trim();
									
									lTmpSelectStr = lActualString.substring(
											lActualString.toUpperCase()
													.indexOf("SELECT"),
											lActualString.length());
									lTmpSelectStr = lTmpSelectStr.replaceAll(
											"\\)", "").trim();
									lSetColumnArr = lTmpStr.split(",");
									// lTmpSelectStr=getPseudoQuery(lTmpSelectStr);

									for (int j = 0; j < lSetColumnArr.length; j++) {

										lPatternString += lStartKeyWord + "_C_"
												+ lSetColumnArr[j].trim()
												+ "_( " + lTmpSelectStr + " );";
										//lCollectedBlocks.add(lPatternString);
										//lPatternString = "";
										lPseudoSqlDTO = new PseudoSqlDTO();
										lPseudoSqlDTO.setIndex(i);
										lPseudoSqlDTO.setSubIndex(j);
										lPseudoSqlDTO.setPseudoSql(lPatternString);
										lPseudoSqlDTO.setActualString(lSetColumnArr[j]);
										lPseudoSqlDTO.setPatternType("COLUMN");
										lPseudoSqlDTO.setPrevKeyword(lStartKeyWord);
										lPseudoSqlDTO.setAliasName(lTmpSelectStr.replaceAll("\\s+", "_"));
										lPseudoSqlDTO.setLevelCount(lLevelCount);
										lPseudoSqlDTO.setPrimarySql(lPrimarySql);
										lCollectedBlocks.add(lPseudoSqlDTO);
										lPatternString = "";
										

									}
									
								}else{
									Pattern lSetMultiColPattern = Pattern
											.compile("\\([\\s\\w\\W\\n\\r]*,[\\s\\w\\W\\n\\r]*\\)\\s*=");
									if(lSetMultiColPattern.matcher(lActualString).find()){
										//System.out.println("lActualString>>>>>"+lActualString);
										lSetColumnArr = lActualString.replaceAll("\\(|\\)", " ").split(",");
										// lTmpSelectStr=getPseudoQuery(lTmpSelectStr);

										for (int j = 0; j < lSetColumnArr.length; j++) {
											//System.out.println("Set Cols"+j+"::->"+lSetColumnArr[j]);

											lPatternString += lStartKeyWord + "_C_"
													+ lSetColumnArr[j].trim();
											//lCollectedBlocks.add(lPatternString);
											//lPatternString = "";
											lColDataArr=getColData(lSetColumnArr[j].trim());
											//System.out.println("Column::->"+lColDataArr[0]+" ::ColData::->>"+lColDataArr[1]);
											lPseudoSqlDTO = new PseudoSqlDTO();
											lPseudoSqlDTO.setIndex(i);
											lPseudoSqlDTO.setSubIndex(j);
											lPseudoSqlDTO.setPseudoSql(lPatternString);
											lPseudoSqlDTO.setActualString(lColDataArr[0]);
											lPseudoSqlDTO.setPatternType("COLUMN");
											lPseudoSqlDTO.setPrevKeyword(lStartKeyWord);
											lPseudoSqlDTO.setAliasName(lColDataArr[1]);
											lPseudoSqlDTO.setLevelCount(lLevelCount);
											lPseudoSqlDTO.setPrimarySql(lPrimarySql);
											lCollectedBlocks.add(lPseudoSqlDTO);
											lPatternString = "";
											

										}
									}else{
										lColDataArr=getColData(lActualString);
										
										lPatternString += lStartKeyWord + "_C_"
												+ lActualString.trim() + ";";
										//lCollectedBlocks.add(lPatternString);
										//lPatternString = "";
										lPseudoSqlDTO = new PseudoSqlDTO();
										lPseudoSqlDTO.setIndex(i);
										lPseudoSqlDTO.setPseudoSql(lPatternString);
										lPseudoSqlDTO.setActualString(lColDataArr[0]);
										lPseudoSqlDTO.setPatternType("COLUMN");
										lPseudoSqlDTO.setAliasName(lColDataArr[1]);
										lPseudoSqlDTO.setPrevKeyword(lStartKeyWord);
										lPseudoSqlDTO.setLevelCount(lLevelCount);
										lPseudoSqlDTO.setPrimarySql(lPrimarySql);
										lCollectedBlocks.add(lPseudoSqlDTO);
										lPatternString = "";
										
									}
									
								}

							} else {
								lColDataArr=getColData(lActualString);
								//System.out.println(lActualString+":::->"+lColDataArr[0]+" aND :: " +lColDataArr[1]);
								
								//lCollectedBlocks.add(lPatternString);
								//lPatternString = "";
								if("".equals(lColDataArr[1])){
									String lColData=lColDataArr[0];
									lColData=lColData.replaceAll("\\s*\\(\\s*","(");
									lColData=lColData.replaceAll("\\s*\\)",")");
									String[] lSubColArr=lColDataArr[0].split("\\s+");
									for (int k = 0; k < lSubColArr.length; k++) {
										lPatternString += lStartKeyWord + "_C_"
												+ lSubColArr[k]+ ";";
										lPseudoSqlDTO = new PseudoSqlDTO();
										lPseudoSqlDTO.setIndex(i);
										lPseudoSqlDTO.setSubIndex(k);
										lPseudoSqlDTO.setPseudoSql(lPatternString);
										lPseudoSqlDTO.setActualString(lSubColArr[k]);
										lPseudoSqlDTO.setPatternType("COLUMN");
										lPseudoSqlDTO.setAliasName("");
										lPseudoSqlDTO.setPrevKeyword(lStartKeyWord);
										lPseudoSqlDTO.setLevelCount(lLevelCount);
										lPseudoSqlDTO.setPrimarySql(lPrimarySql);
										lCollectedBlocks.add(lPseudoSqlDTO);
										lPatternString = "";
									}
								}else{
									lPatternString += lStartKeyWord + "_C_"
											+ lActualString.trim() + ";";
									lPseudoSqlDTO = new PseudoSqlDTO();
									lPseudoSqlDTO.setIndex(i);
									lPseudoSqlDTO.setPseudoSql(lPatternString);
									lPseudoSqlDTO.setActualString(lColDataArr[0]);
									lPseudoSqlDTO.setPatternType("COLUMN");
									lPseudoSqlDTO.setAliasName(lColDataArr[1]);
									lPseudoSqlDTO.setPrevKeyword(lStartKeyWord);
									lPseudoSqlDTO.setLevelCount(lLevelCount);
									lPseudoSqlDTO.setPrimarySql(lPrimarySql);
									lCollectedBlocks.add(lPseudoSqlDTO);
									lPatternString = "";
								}
								
								//Alias
								/*lPseudoSqlDTO = new PseudoSqlDTO();
								lPseudoSqlDTO.setIndex("0");
								lPseudoSqlDTO.setPseudoSql(lPatternString);
								lPseudoSqlDTO.setActualString(lColDataArr[1]);
								lPseudoSqlDTO.setPatternType("COLUMN_VALUE");
								lPseudoSqlDTO.setPrevKeyword(lStartKeyWord);
								lCollectedBlocks.add(lPseudoSqlDTO);
								lPatternString = "";*/

							}
							
							
							

							// SET ( Status , StatusReason ) = ( SELECT distinct
							// 'ZV002' FROM WHERE)
						

					}else /*if (lKeywordsForOtherNamesLst
							.contains(((String) lStartKeyWord).toUpperCase()))*/{
						String[] lOtherDataArr=lActualString.split("\\s+");
						for (int j = 0; j < lOtherDataArr.length; j++) {
							String lTempString=lOtherDataArr[j];
							lPatternString += lStartKeyWord + "_O_" + lTempString+ ";";						
							lPseudoSqlDTO = new PseudoSqlDTO();
							lPseudoSqlDTO.setIndex(i);
							lPseudoSqlDTO.setSubIndex(j);
							lPseudoSqlDTO.setPseudoSql(lPatternString);						
							lPseudoSqlDTO.setActualString(lTempString);
							lPseudoSqlDTO.setAliasName("");
							lPseudoSqlDTO.setPatternType("OTHER");
							lPseudoSqlDTO.setPrevKeyword(lStartKeyWord);
							lPseudoSqlDTO.setLevelCount(lLevelCount);	
							lPseudoSqlDTO.setPrimarySql(lPrimarySql);
							lCollectedBlocks.add(lPseudoSqlDTO);						
							lPatternString = "";
						}
						/*String[] lOtherDataArr=getOtherData(lActualString);
						lPatternString += lStartKeyWord + "_O_" + lActualString+ ";";						
						lPseudoSqlDTO = new PseudoSqlDTO();
						lPseudoSqlDTO.setIndex(i);
						lPseudoSqlDTO.setPseudoSql(lPatternString);						
						lPseudoSqlDTO.setActualString(lOtherDataArr[0]);
						lPseudoSqlDTO.setAliasName(lOtherDataArr[1]);
						lPseudoSqlDTO.setPatternType("OTHER");
						lPseudoSqlDTO.setPrevKeyword(lStartKeyWord);
						lPseudoSqlDTO.setLevelCount(lLevelCount);								
						lCollectedBlocks.add(lPseudoSqlDTO);						
						lPatternString = "";*/
					}

				}

			}

		}
		
		


		return lCollectedBlocks;

	}
	
	
	public String[] getTablePart(String pParam){
		String[] lTabArr = {"",""};
		if(pParam.split("\\s+").length == 2){
			lTabArr = pParam.split("\\s+");
		}
		else{
			lTabArr[0]=pParam;
			lTabArr[1]="";
		}
		
		String[] lCrossJoinSplitArr = lTabArr[0].split("\\.\\.");
		//System.out.println(":::::lTabArr[0]:::"+lTabArr[0]);
		//System.out.println(":::::lCrossJoinSplitArr[0]:::"+lCrossJoinSplitArr.length);
		if(lCrossJoinSplitArr != null && lCrossJoinSplitArr.length ==2){
			lTabArr[0]=lCrossJoinSplitArr[1];
			//System.out.println(":::::lCrossJoinSplitArr:::"+lCrossJoinSplitArr[1]);
		}
		return lTabArr;
	}
	
	public String[] getOtherData(String pParam) {
		pParam=pParam.trim();
		String[] lRetArr =  new String[2];
		try{
			if(pParam.contains(" ")){
				lRetArr=pParam.split("\\s+");
				if(lRetArr.length==2)
					return lRetArr;
			}
			
		}catch(Exception e){
			lRetArr[0]=pParam;
			lRetArr[1]="";
			return lRetArr;
			
		}
		
		lRetArr[0]=pParam;
		lRetArr[1]="";
		return lRetArr;

	}
	
	
	
	public String[] getColData(String pParam) {
		pParam=pParam.trim();
		String[] lColArr = null;
		String lColName = "";
		String lValue = "";
		
		String[] lRetArr =  new String[2]; 

		try {
			Pattern p=Pattern.compile("\\(\\s+SELECT\\s+");
			//"ClaimStatus"=(  SELECT  display  FROM  CodeApplication2  WHERE  Code  =  c.ClaimStatus  )  
			if(p.matcher(pParam.toUpperCase()).find()){
				if (pParam.contains("=")){
					lColArr=pParam.split("=");
					lValue = lColArr[1];
					lColName = lColArr[0];
				}else{
					lColArr=pParam.split("\\s+\\)");
					lValue = lColArr[0]+" ) ";
					lColName = lColArr[1];
				}
				lRetArr[0]=lColName.replaceAll("(\"|')", "");
				lRetArr[1]=lValue;
				//System.out.println("lColName-->"+lColName);
				//return lColName.trim() + "_" + lValue.trim();
				return lRetArr;
			}
			
			if (pParam.contains("=") || pParam.contains("<") || pParam.contains(">") || pParam.toUpperCase().contains(" LIKE ")|| pParam.toUpperCase().contains(" AS ")) {
				if(pParam.toUpperCase().contains(" AS ")){
					boolean lChkAsKeyword=true;
					String []lTempArr=pParam.split("\\s+");
					int lOpenBraceCount=0;
					lColName="";
					lValue="";
					for (int i = 0; i < lTempArr.length; i++) {
						if(lTempArr[i].trim().equals(")")){
							lOpenBraceCount++;
						}
						if(lTempArr[i].trim().equals("(")){
							lOpenBraceCount--;
						}
						if(lTempArr[i].trim().equalsIgnoreCase("AS") && lOpenBraceCount==0){
							lChkAsKeyword=false;
							continue;
						}
						if(lChkAsKeyword){
							lColName+=lTempArr[i]+" ";
						}else{
							lValue+=lTempArr[i]+" ";
						}
					}
					/*System.out.println("In Method++ColName::->>>"+lColName+"::Value::->>"+lValue);*/
					if(lChkAsKeyword){
						lColArr = pParam.toUpperCase().split("(\\*=|<=|>=|<|>|!>\\s*|!<\\s*|=\\s*|!=\\s*|%|\\s+LIKE\\s+)");		
					}else{
						lRetArr[0]=lColName.replaceAll("(\"|')", "");
						lRetArr[1]=lValue.replaceAll("(\"|')", "");
						
						//return lColName.trim() + "_" + lValue.trim();
						return lRetArr;						
					}
					
				}
				else{
					lColArr = pParam.toUpperCase().split("(\\*=|<=|>=|<|>|!>\\s*|!<\\s*|=\\s*|!=\\s*|%|\\s+LIKE\\s+)");					
				}
				
				
				//lColArr = pParam.split("=");
			} else {
				lColArr = pParam.split("\\s+");
			}
			if (lColArr != null && lColArr.length == 2) {
				if (lColArr[0].trim().startsWith("\"")
						|| lColArr[0].trim().startsWith("'")) {
					lValue = lColArr[0];
					lColName = lColArr[1];
				} else if (lColArr[1].trim().startsWith("\"")
						|| lColArr[1].trim().startsWith("'")) {
					lValue = lColArr[1];
					lColName = lColArr[0];
				} else if (lColArr[0].toUpperCase().trim().startsWith("@")
						|| lColArr[0].toUpperCase().trim().startsWith("V_")
						|| lColArr[0].toUpperCase().trim().startsWith("SWV_")) {
				
					lValue = lColArr[0];
					lColName = lColArr[1];
				} else if (lColArr[1].toUpperCase().trim().startsWith("@")
						|| lColArr[1].toUpperCase().trim().startsWith("V_")
						|| lColArr[1].toUpperCase().trim().startsWith("SWV_")) {
					
					lValue = lColArr[1];
					lColName = lColArr[0];
				}else {
					lValue = lColArr[1];
					lColName = lColArr[0];
				}
				lRetArr[0]=lColName;
				lRetArr[1]=lValue.replaceAll("(\"|')", "");
				//return lColName.trim() + "_" + lValue.trim();
				
				return lRetArr;

			} else{
				lRetArr[0]=pParam.replaceAll("(\"|')", "");
				lRetArr[1]="";				
				//return pParam;
				return lRetArr;
			}

		} catch (Exception e) {
			lRetArr[0]=pParam.replaceAll("(\"|')", "");
			lRetArr[1]="";
			
			//return pParam;
			return lRetArr;
		}

	}

	public String getPseudoQuery(String pParam) {
		String[] lSplitArray = pParam.split("\\s+");
		String lRetValue = "";
		for (int i = 0; i < lSplitArray.length; i++) {
			if (prepareDelimtersList().contains(
					(String) lSplitArray[i].trim().toUpperCase())) {

				if ("".equals(lRetValue)) {
					lRetValue = lSplitArray[i].trim().toUpperCase();
				} else {
					lRetValue = lRetValue + "_"
							+ lSplitArray[i].trim().toUpperCase();
				}

			}
		}

		return lRetValue;
	}

	public List prepareDelimtersList() {
		List lDelimtersList = new ArrayList();
		lDelimtersList.add((String) "SELECT");
		lDelimtersList.add((String) "UPDATE");
		lDelimtersList.add((String) "INSERT");
		lDelimtersList.add((String) "DELETE");
		lDelimtersList.add((String) "FROM");
		lDelimtersList.add((String) "SET");
		lDelimtersList.add((String) "INTO");
		lDelimtersList.add((String) "VALUES");
		lDelimtersList.add((String) "WHERE");
		lDelimtersList.add((String) "AND");
		lDelimtersList.add((String) "OR");
		lDelimtersList.add((String) "LEFT");
		lDelimtersList.add((String) "RIGHT");
		lDelimtersList.add((String) "OUTER");
		lDelimtersList.add((String) "INNER");
		lDelimtersList.add((String) "JOIN");
		lDelimtersList.add((String) "ON");
		lDelimtersList.add((String) "ORDER");
		lDelimtersList.add((String) "GROUP");
		lDelimtersList.add((String) "HAVING");
		lDelimtersList.add((String) "BY");
		lDelimtersList.add((String) "INSERT_TOOL_COLUMN");
		lDelimtersList.add((String) "DISTINCT");
		
		//After the Old Logic
		lDelimtersList.add((String) "DROP");
		lDelimtersList.add((String) "PROCEDURE");
		lDelimtersList.add((String) "PROC");
		lDelimtersList.add((String) "EXEC");
		lDelimtersList.add((String) "EXECUTE");
		lDelimtersList.add((String) "CALL");
		lDelimtersList.add((String) "CREATE");
		lDelimtersList.add((String) "COMMIT");
		lDelimtersList.add((String) "SAVE");
		lDelimtersList.add((String) "ROLLBACK");
		lDelimtersList.add((String) "OPEN");
		lDelimtersList.add((String) "CLOSE");
		lDelimtersList.add((String) "FETCH");
		lDelimtersList.add((String) "TO");
		//lDelimtersList.add((String) "GO");
		lDelimtersList.add((String) "GRANT");
		lDelimtersList.add((String) "DECLARE");
		lDelimtersList.add((String) "GLOBAL");
		lDelimtersList.add((String) "TEMPORARY");
		lDelimtersList.add((String) "CURSOR");
		lDelimtersList.add((String) "WITH");
		lDelimtersList.add((String) "RETURN");
		lDelimtersList.add((String) "WITH_RETURN");
		lDelimtersList.add((String) "FOR");
		lDelimtersList.add((String) ToolConstant.TOOL_DELIMT);
		lDelimtersList.add((String) "TOOL_OTHER_(");
		lDelimtersList.add((String) "TABLE_TOOL_COLUMN_(");
		//lDelimtersList.add((String) "IF");
		//lDelimtersList.add((String) "ELSE");
		lDelimtersList.add((String) "ELSE_IF");
		lDelimtersList.add((String) "GRANT_EXECUTE");
		lDelimtersList.add((String) "TABLE");
		//TABLE_TOOL_COLUMN_(

		
		return lDelimtersList;
	}

	
	public List[] compareStatements(List lSourceList,List lTargetList,List lMatchResultList, int flag){
		List[] lListArr = new List[3];
		// System.out.println(sample);
		PseudoSqlDTO lSourcePseudoSqlDTO = new PseudoSqlDTO();
		PseudoSqlDTO lTargetPseudoSqlDTO = new PseudoSqlDTO();
		//List lMatchResultList=new ArrayList();
		MatchResultDTO lMatchResultDTO=new MatchResultDTO();
		String lTargetVar="";
		// System.out.println(":::::source list-->"+((PseudoSqlDTO)lSourceList.get(i)).getActualString()+"########"+((PseudoSqlDTO)lTargetList.get(i)).getActualString());
		for (int i = 0; i < lSourceList.size(); i++) {
			lSourcePseudoSqlDTO = (PseudoSqlDTO) lSourceList.get(i);
			if (lSourcePseudoSqlDTO.getMatchedYN() != null
					&& !"".equals(lSourcePseudoSqlDTO.getMatchedYN())) {
				break;
			}
			/*
			 * System.out.println("getPseudoSql::->"+lSourcePseudoSqlDTO.
			 * getPseudoSql());
			 * System.out.println("getPatternType::->"+lSourcePseudoSqlDTO
			 * .getPatternType());
			 * System.out.println("getActualString::->"+lSourcePseudoSqlDTO
			 * .getActualString());
			 * System.out.println("getAliasName::->"+lSourcePseudoSqlDTO
			 * .getAliasName());
			 */
			for (int j = 0; j < lTargetList.size(); j++) {
				lTargetPseudoSqlDTO = (PseudoSqlDTO) lTargetList.get(j);
				/*if (lTargetPseudoSqlDTO.getMatchedYN() != null
						&& !"".equals(lTargetPseudoSqlDTO.getMatchedYN())) {
					break;
				}*/
				/////////////**********Start Checking for Variable Usage Consistency ***********////////////
				
				lTargetVar=ToolsUtil.replaceNull((String)lVarUsageMap.get((String)lSourcePseudoSqlDTO.getActualString().trim().toUpperCase()));
		//	System.out.println("ACTUAL STRING SOURCE "+(String)lSourcePseudoSqlDTO.getActualString());
		//	System.out.println("ACTUAL STRING TARGET "+(String)lTargetPseudoSqlDTO.getActualString());
		//	System.out.println("PREVIOUS KEYWORD SOURCE "+(String)lSourcePseudoSqlDTO.getPrevKeyword());
		//	System.out.println("PREV KEYWORD TARGET "+(String)lTargetPseudoSqlDTO.getPrevKeyword());
		//	System.out.println("ALIAS STRING SOURCE "+(String)lSourcePseudoSqlDTO.getAliasName());
			
		//	System.out.println("ALIAS STRING TARGET "+(String)lTargetPseudoSqlDTO.getAliasName());
			
				/////////////**********End Checking for Variable Usage Consistency ***********////////////
				//System.out.println(lSourcePseudoSqlDTO.getActualString().trim().replaceFirst("#", "SESSION.tt_")+"$$$$$$$$$$"+lTargetPseudoSqlDTO.getActualString().trim()+"$$$$$"+lSourcePseudoSqlDTO.getActualString().trim().replaceFirst("#", "SESSION.tt_").equalsIgnoreCase(lTargetPseudoSqlDTO.getActualString().trim()));
				if (  ( (lSourcePseudoSqlDTO.getPrevKeyword().equalsIgnoreCase(lTargetPseudoSqlDTO.getPrevKeyword().trim())						
						&& lSourcePseudoSqlDTO.getPatternType().trim().equalsIgnoreCase(lTargetPseudoSqlDTO.getPatternType().trim()))
						//||  lSourcePseudoSqlDTO.getPrevKeyword().trim().equalsIgnoreCase((String)lKeywordPairingMap.get((String)lTargetPseudoSqlDTO.getPrevKeyword())) )						
						||  (ToolsUtil.replaceNullList((List)lKeywordPairingMap.get((String)lSourcePseudoSqlDTO.getPrevKeyword().trim().toUpperCase()))).contains((String)lTargetPseudoSqlDTO.getPrevKeyword().trim().toUpperCase()) )
						/*&& lSourcePseudoSqlDTO.getLevelCount().trim().equalsIgnoreCase(lTargetPseudoSqlDTO.getLevelCount().trim())*/
						&& (/*lSourcePseudoSqlDTO.getActualString().trim().replaceFirst("#", "SESSION.tt_").replaceFirst("@","v_").equalsIgnoreCase(lTargetPseudoSqlDTO.getActualString().trim())*/
								lSourcePseudoSqlDTO.getActualString().trim().replaceFirst("(?i)dbo.", "").replaceAll("(\\\"|')", "").equalsIgnoreCase(lTargetPseudoSqlDTO.getActualString().replaceAll("(\\\"|')", "").trim()) //Replacing quotes with empty 
								
								||lSourcePseudoSqlDTO.getActualString().trim().replaceFirst("#", "SESSION.tt_").equalsIgnoreCase(lTargetPseudoSqlDTO.getActualString().trim()) //for Temp Tables
								|| ((ToolsUtil.replaceNull((String)lSourceToTargetKeywordMap.get((String)lSourcePseudoSqlDTO.getActualString().trim().toUpperCase()))).equalsIgnoreCase(lTargetPseudoSqlDTO.getActualString().trim()))
								||(lTargetVar.trim().equalsIgnoreCase(lTargetPseudoSqlDTO.getActualString().trim())) // Condition for variable usage Consistency
								
								||  (ToolsUtil.replaceNullList((List)lKeywordPairingMap.get((String)lSourcePseudoSqlDTO.getActualString().trim().toUpperCase()))).contains((String)lTargetPseudoSqlDTO.getActualString().trim().toUpperCase()) )	
						&& (lTargetPseudoSqlDTO.getMatchedYN() == null || "".equals(lTargetPseudoSqlDTO.getMatchedYN())))
					{
					//lTargetVar=ToolsUtil.replaceNull((String)lVarUsageMap.get((String)lSourcePseudoSqlDTO.getAliasName().trim().toUpperCase()));
					//System.out.println("PARAMETER:::: "+(String)lSourcePseudoSqlDTO.getAliasName());
					//System.out.println("TARGET VAR LIST:::: "+lTargetVar);
					if (/*lSourcePseudoSqlDTO.getAliasName().trim().toUpperCase().replaceFirst("@","V_").equalsIgnoreCase(lTargetPseudoSqlDTO.getAliasName().trim())*/
							lSourcePseudoSqlDTO.getAliasName().trim().equalsIgnoreCase(lTargetPseudoSqlDTO.getAliasName().trim())
							|| (lTargetVar.trim().equalsIgnoreCase(lTargetPseudoSqlDTO.getAliasName().trim()))
							) {
						lSourcePseudoSqlDTO.setMatchedYN("Y");
						lTargetPseudoSqlDTO.setMatchedYN("Y");
						 lMatchResultDTO=new MatchResultDTO();
						 if(flag==0){
							 lMatchResultDTO.setSourcePseudoSqlDTO(lSourcePseudoSqlDTO);
							 lMatchResultDTO.setTargetPseudoSqlDTO(lTargetPseudoSqlDTO);
						 }else{
							 lMatchResultDTO.setSourcePseudoSqlDTO(lTargetPseudoSqlDTO);
							 lMatchResultDTO.setTargetPseudoSqlDTO(lSourcePseudoSqlDTO);
							 
						 }
						
						lMatchResultDTO.setMatchedYN("Y");
						lMatchResultDTO.setMatchDesc("Completely Matched");
						lMatchResultList.add(lMatchResultDTO);
						
					} else {
						lSourcePseudoSqlDTO.setMatchedYN("P");
						lTargetPseudoSqlDTO.setMatchedYN("P");
						 lMatchResultDTO=new MatchResultDTO();						 
						 if(flag==0){
							 lMatchResultDTO.setSourcePseudoSqlDTO(lSourcePseudoSqlDTO);
							 lMatchResultDTO.setTargetPseudoSqlDTO(lTargetPseudoSqlDTO);
							 
						 }else{
							 lMatchResultDTO.setSourcePseudoSqlDTO(lTargetPseudoSqlDTO);
							 lMatchResultDTO.setTargetPseudoSqlDTO(lSourcePseudoSqlDTO);
							 
						 }
						lMatchResultDTO.setMatchedYN("P");
						lMatchResultDTO.setMatchDesc("Partially Matched");
						lMatchResultList.add(lMatchResultDTO);
					}
					lSourceList.set(i, lSourcePseudoSqlDTO);
					lTargetList.set(j, lTargetPseudoSqlDTO);
					break;
				}/*else{					
					if (lSourcePseudoSqlDTO.getAliasName().trim().replaceFirst("@","v_").equalsIgnoreCase(lTargetPseudoSqlDTO.getAliasName().trim())) {
						lSourcePseudoSqlDTO.setMatchedYN("P");
						lTargetPseudoSqlDTO.setMatchedYN("P");
						 lMatchResultDTO=new MatchResultDTO();
						  if(flag==0){
							 lMatchResultDTO.setSourcePseudoSqlDTO(lSourcePseudoSqlDTO);
							 lMatchResultDTO.setTargetPseudoSqlDTO(lTargetPseudoSqlDTO);
							 
						 }else{
							 lMatchResultDTO.setSourcePseudoSqlDTO(lTargetPseudoSqlDTO);
							 lMatchResultDTO.setTargetPseudoSqlDTO(lSourcePseudoSqlDTO);
							 
						 }
						
						  lMatchResultDTO.setMatchedYN("P");
						lMatchResultDTO.setMatchDesc("Partially Matched");
						lMatchResultList.add(lMatchResultDTO);
						
					}					
				}*/

			}
		}
		

		
		
		/*for (int j = 0; j < lMatchResultList.size(); j++) {
			 lMatchResultDTO=(MatchResultDTO)lMatchResultList.get(j);
			System.out.println(":::::source->"+lMatchResultDTO.getSourcePseudoSqlDTO().getActualString()+"<<<<>>>>>"+lMatchResultDTO.getTargetPseudoSqlDTO().getActualString()+"<<<>>>>"+lMatchResultDTO.getMatchDesc());
		}*/
		lListArr[0] = lMatchResultList;
		lListArr[1] = lSourceList;
		lListArr[2] = lTargetList;
		return lListArr;
	}
	
	public String getComparedCollectiveList(String pSourceStatement, String pTargetStatement,String pCompareSeq,String pCompareKeywordSeq,String pProcedureName,String pOrderNum,String patternMatch){
		
		 List lMatchResultList=new ArrayList();
		 
		 List lSourceList = getPatternString(pSourceStatement);		 
		 List lTargetList = getPatternString(pTargetStatement);		 
		 lKeywordPairingMap=prepareKeywordPairMap();
		 
		List[] lFirstArrayList = compareStatements( lSourceList, lTargetList, lMatchResultList, 0);
		/*List lChkMatchResultList=lFirstArrayList[0];
		System.out.println("First Run");
		for (int j = 0; j < lChkMatchResultList.size(); j++) {
			MatchResultDTO lMatchResultDTO=(MatchResultDTO)lChkMatchResultList.get(j);
			System.out.println("index:-->"+lMatchResultDTO.getSourcePseudoSqlDTO().getIndex() +":::::source->"+lMatchResultDTO.getSourcePseudoSqlDTO().getActualString()+" Value:"+lMatchResultDTO.getSourcePseudoSqlDTO().getAliasName()+"<<<<>>>>>"+lMatchResultDTO.getTargetPseudoSqlDTO().getActualString()+" Value:"+lMatchResultDTO.getTargetPseudoSqlDTO().getAliasName()+"<<<>>>>"+lMatchResultDTO.getMatchDesc());
		}*/
		
		List[] lSecondArrayList = compareStatements( lFirstArrayList[2], lFirstArrayList[1], lFirstArrayList[0], 1);
		
		lSourceList = new ArrayList();
		lTargetList = new ArrayList();
		lMatchResultList = new ArrayList();
		lSourceList =lSecondArrayList[2];
		lTargetList =lSecondArrayList[1];
		lMatchResultList=lSecondArrayList[0];
		/*System.out.println("::::::::::::::::::Before :::::::::::::");
		 for (int i = 0; i < lTargetList.size(); i++) {
			 PseudoSqlDTO lPseudoSqlDTO = (PseudoSqlDTO) lTargetList.get(i);
				
				
				  System.out.println("getPseudoSql::->"+lPseudoSqlDTO.
				  getPseudoSql());
				  System.out.println("getPatternType::->"+lPseudoSqlDTO
				  .getPatternType());
				  System.out.println("getActualString::->"+lPseudoSqlDTO
				  .getActualString());
				  System.out.println("getAliasName::->"+lPseudoSqlDTO
				  .getAliasName());
				  System.out.println("getiNDEX::->"+lPseudoSqlDTO
						  .getIndex());
				  System.out.println("getMtached::->"+lPseudoSqlDTO
						  .getMatchedYN());
				 
		 }*/
		/*for (int j = 0; j < lMatchResultList.size(); j++) {
			MatchResultDTO lMatchResultDTO=(MatchResultDTO)lMatchResultList.get(j);
			System.out.println("index:-->"+lMatchResultDTO.getSourcePseudoSqlDTO().getIndex() +":::::source->"+lMatchResultDTO.getSourcePseudoSqlDTO().getActualString()+" Value:"+lMatchResultDTO.getSourcePseudoSqlDTO().getAliasName()+"<<<<>>>>>"+lMatchResultDTO.getTargetPseudoSqlDTO().getActualString()+" Value:"+lMatchResultDTO.getTargetPseudoSqlDTO().getAliasName()+"<<<>>>>"+lMatchResultDTO.getMatchDesc());
		}
		*/
		 List lSortedMatchResult=sortMatchResult( lSourceList, lTargetList, lMatchResultList);
		 MatchResultDTO lMatchResultDTO=null;
		 try{
				//System.out.println("Sorted List");
			 
				for (int j = 0; j < lSortedMatchResult.size(); j++) {
					 lMatchResultDTO=(MatchResultDTO)lSortedMatchResult.get(j);
					// System.out.println("MATCH RESULT:::::"+lMatchResultDTO.getMatchedYN());
					if(patternMatch.equalsIgnoreCase("N")){
						 lMatchResultDTO.setMatchedYN("M");
						 lMatchResultDTO.setMatchDesc("Pattern Mismatch");
					 }
					 prepareInsert(lMatchResultDTO,pCompareSeq,pCompareKeywordSeq,pProcedureName,pOrderNum);
					 
					 //System.out.println("Type:-->"+lMatchResultDTO.getSourcePseudoSqlDTO().getPatternType()+"Source Level:-->"+lMatchResultDTO.getSourcePseudoSqlDTO().getLevelCount()+"Source Keyword:-->"+lMatchResultDTO.getSourcePseudoSqlDTO().getPrevKeyword() +"Target Keyword:-->"+lMatchResultDTO.getTargetPseudoSqlDTO().getPrevKeyword());
					//System.out.println("Type:-->"+lMatchResultDTO.getTargetPseudoSqlDTO().getPatternType()+"Target Level:-->"+lMatchResultDTO.getTargetPseudoSqlDTO().getLevelCount()+"index:-->"+lMatchResultDTO.getSourcePseudoSqlDTO().getIndex() +":::::source->"+lMatchResultDTO.getSourcePseudoSqlDTO().getActualString()+" Value:"+lMatchResultDTO.getSourcePseudoSqlDTO().getAliasName()+"<<<<>>>>>"+lMatchResultDTO.getTargetPseudoSqlDTO().getActualString()+" Value:"+lMatchResultDTO.getTargetPseudoSqlDTO().getAliasName()+"<<<>>>>"+lMatchResultDTO.getMatchDesc());
					 
				}				
				
			}catch(Exception e){
				e.printStackTrace();
			}
		
		return "";
	}
	
		
		

	
	
	public PreparedStatement prepareInsert(MatchResultDTO pMatchResultDTO,String pCompareSeq,String pCompareKeywordSeq,String pProcedureName,String pOrderNum){
		try{
			
			lInsertPreparedStatement.setString(1,ToolsUtil.replaceZero(pCompareSeq));
			lInsertPreparedStatement.setString(2,ToolsUtil.replaceZero(pCompareKeywordSeq));
			lInsertPreparedStatement.setString(3,ToolsUtil.replaceZero(pProcedureName));
			lInsertPreparedStatement.setString(4,ToolsUtil.replaceZero(pOrderNum));
			lInsertPreparedStatement.setString(5, ToolsUtil.replaceNull(pMatchResultDTO.getSourcePseudoSqlDTO().getPrevKeyword()));
			lInsertPreparedStatement.setString(6, ToolsUtil.replaceZero(pMatchResultDTO.getSourcePseudoSqlDTO().getIndex()+""));
			lInsertPreparedStatement.setString(7, pMatchResultDTO.getSourcePseudoSqlDTO().getPatternType());
			//lInsertPreparedStatement.setString(8, replaceTempKeywords(pMatchResultDTO.getSourcePseudoSqlDTO().getActualString()));
			lInsertPreparedStatement.setString(8, pMatchResultDTO.getSourcePseudoSqlDTO().getActualString());
			lInsertPreparedStatement.setString(9, pMatchResultDTO.getSourcePseudoSqlDTO().getAliasName());			
			lInsertPreparedStatement.setInt(10, pMatchResultDTO.getTargetPseudoSqlDTO().getIndex());
			lInsertPreparedStatement.setString(11, pMatchResultDTO.getTargetPseudoSqlDTO().getPatternType());
			//lInsertPreparedStatement.setString(12, replaceTempKeywords(pMatchResultDTO.getTargetPseudoSqlDTO().getActualString()));
			lInsertPreparedStatement.setString(12, pMatchResultDTO.getTargetPseudoSqlDTO().getActualString());
			lInsertPreparedStatement.setString(13, pMatchResultDTO.getTargetPseudoSqlDTO().getAliasName());			
			lInsertPreparedStatement.setString(14, pMatchResultDTO.getMatchedYN());
			lInsertPreparedStatement.setString(15, pMatchResultDTO.getMatchDesc());	
			lInsertPreparedStatement.setString(16, pMatchResultDTO.getSourcePseudoSqlDTO().getLevelCount());
			lInsertPreparedStatement.setString(17, pMatchResultDTO.getTargetPseudoSqlDTO().getLevelCount());
			lInsertPreparedStatement.setString(18, ToolsUtil.replaceZero(pMatchResultDTO.getSourcePseudoSqlDTO().getSubIndex()+""));
			lInsertPreparedStatement.setInt(19, pMatchResultDTO.getTargetPseudoSqlDTO().getSubIndex());
			lInsertPreparedStatement.setString(20, pMatchResultDTO.getSourcePseudoSqlDTO().getPrimarySql());
			lInsertPreparedStatement.setString(21, pMatchResultDTO.getTargetPseudoSqlDTO().getPrimarySql());
			lQueryCount++;
			lInsertPreparedStatement.addBatch();
			
			if(lQueryCount%1000==0)
			 { 
				 lInsertPreparedStatement.executeBatch();
				 lConnection.commit();
				 lQueryCount=0;
			 
			 }
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return lInsertPreparedStatement;
	}
	
	
	
	public List sortMatchResult(List lSourceList,List lTargetList,List lMatchResultList){
		MatchResultDTO lMatchResultDTO=new MatchResultDTO();
		MatchResultDTO lSortedMatchResultDTO=new MatchResultDTO();
		PseudoSqlDTO lSourcePseudoSqlDTO=new PseudoSqlDTO();
		PseudoSqlDTO lTargetPseudoSqlDTO=new PseudoSqlDTO();
		
		PseudoSqlDTO lSourceTempPseudoSqlDTO = null;
		List lSortedMatchResult=new ArrayList();
		
		
		
		for (int i = 0; i < lMatchResultList.size(); i++) {
			lMatchResultDTO =(MatchResultDTO) lMatchResultList.get(i);
			lSourcePseudoSqlDTO=(PseudoSqlDTO) lMatchResultDTO.getSourcePseudoSqlDTO();
			lTargetPseudoSqlDTO=(PseudoSqlDTO) lMatchResultDTO.getTargetPseudoSqlDTO();
			//Checking for source non matched things
			for (int j = 0; j < lSourceList.size(); j++) {
				PseudoSqlDTO  lSourceListPseudoSqlDTO=(PseudoSqlDTO)lSourceList.get(j);
				if(lSourcePseudoSqlDTO.getIndex()>lSourceListPseudoSqlDTO.getIndex()){
					//System.out.println("Source Matched YN::->"+lSourceListPseudoSqlDTO.getMatchedYN());
					if(lSourceListPseudoSqlDTO.getMatchedYN() == null || "".equals(lSourceListPseudoSqlDTO.getMatchedYN())){						
						lSortedMatchResultDTO=new MatchResultDTO();
						lSortedMatchResultDTO.setSourcePseudoSqlDTO(lSourceListPseudoSqlDTO);
						lSortedMatchResultDTO.setTargetPseudoSqlDTO(new PseudoSqlDTO());
						lSortedMatchResultDTO.setMatchedYN("N");
						lSortedMatchResultDTO.setMatchDesc("Not Matched");
						//Adding to sortedList
						lSortedMatchResult.add(lSortedMatchResultDTO);
						
						lSourceListPseudoSqlDTO.setMatchedYN("N");
						//lSourceList.remove(j);
						lSourceList.set(j, lSourceListPseudoSqlDTO);
					}
				}
			}
			//System.out.println(" Target Index in MatchList::->"+lTargetPseudoSqlDTO.getIndex());
			//Checking for Target non matched things
			for (int j = 0; j < lTargetList.size(); j++) {
				PseudoSqlDTO  lTargetListPseudoSqlDTO=(PseudoSqlDTO)lTargetList.get(j);				
				if(lTargetPseudoSqlDTO.getIndex()>lTargetListPseudoSqlDTO.getIndex()){
					
					if(lTargetListPseudoSqlDTO.getMatchedYN() == null || "".equals(lTargetListPseudoSqlDTO.getMatchedYN())){
						lSortedMatchResultDTO=new MatchResultDTO();
						//New
						lSourceTempPseudoSqlDTO=new PseudoSqlDTO();	
						if(i>0){
						lSourceTempPseudoSqlDTO.setIndex(((MatchResultDTO)lMatchResultList.get(i-1)).getSourcePseudoSqlDTO().getIndex());
						lSourceTempPseudoSqlDTO.setSubIndex(((MatchResultDTO)lMatchResultList.get(i-1)).getSourcePseudoSqlDTO().getSubIndex()+1);
						lSourceTempPseudoSqlDTO.setPrevKeyword(lTargetListPseudoSqlDTO.getPrevKeyword());
						lSourceTempPseudoSqlDTO.setPrimarySql(((MatchResultDTO)lMatchResultList.get(i-1)).getSourcePseudoSqlDTO().getPrimarySql());
						}
						lSortedMatchResultDTO.setSourcePseudoSqlDTO(lSourceTempPseudoSqlDTO);
						//New END
						//lSortedMatchResultDTO.setSourcePseudoSqlDTO(new PseudoSqlDTO());
						lSortedMatchResultDTO.setTargetPseudoSqlDTO(lTargetListPseudoSqlDTO);
						lSortedMatchResultDTO.setMatchedYN("N");
						lSortedMatchResultDTO.setMatchDesc("Not Matched");
						//Adding to sortedList
						lSortedMatchResult.add(lSortedMatchResultDTO);
						
						lTargetListPseudoSqlDTO.setMatchedYN("N");
						//lTargetList.remove(j);

						lTargetList.set(j, lTargetListPseudoSqlDTO);
					}
				}
			}
			//Adding to sortedList
			//lSortedMatchResultDTO=(MatchResultDTO) lMatchResultList.get(i);
			//System.out.println(":::::source->"+lMatchResultDTO.getSourcePseudoSqlDTO().getActualString()+"<<<<>>>>>"+lMatchResultDTO.getTargetPseudoSqlDTO().getActualString()+"<<<>>>>"+lMatchResultDTO.getMatchDesc());
			lSortedMatchResult.add(lMatchResultDTO);
			
		}
		
		for (int j = 0; j < lSourceList.size(); j++) {
			PseudoSqlDTO  lSourceListPseudoSqlDTO=(PseudoSqlDTO)lSourceList.get(j);
			//if(lSourcePseudoSqlDTO.getIndex()>lSourceListPseudoSqlDTO.getIndex()){
				//System.out.println("Source Matched YN::->"+lSourceListPseudoSqlDTO.getMatchedYN());
				if(lSourceListPseudoSqlDTO.getMatchedYN() == null || "".equals(lSourceListPseudoSqlDTO.getMatchedYN())){						
					lSortedMatchResultDTO=new MatchResultDTO();
					lSortedMatchResultDTO.setSourcePseudoSqlDTO(lSourceListPseudoSqlDTO);
					lSortedMatchResultDTO.setTargetPseudoSqlDTO(new PseudoSqlDTO());
					lSortedMatchResultDTO.setMatchedYN("N");
					lSortedMatchResultDTO.setMatchDesc("Not Matched");
					//Adding to sortedList
					lSortedMatchResult.add(lSortedMatchResultDTO);
					
					lSourceListPseudoSqlDTO.setMatchedYN("N");
					//lSourceList.remove(j);
					lSourceList.set(j, lSourceListPseudoSqlDTO);
				}
			//}
		}
		//System.out.println(" Target Index in MatchList::->"+lTargetPseudoSqlDTO.getIndex());
		//Checking for Target non matched things
		for (int j = 0; j < lTargetList.size(); j++) {
			PseudoSqlDTO  lTargetListPseudoSqlDTO=(PseudoSqlDTO)lTargetList.get(j);				
			//if(lTargetPseudoSqlDTO.getIndex()>lTargetListPseudoSqlDTO.getIndex()){
				
				if(lTargetListPseudoSqlDTO.getMatchedYN() == null || "".equals(lTargetListPseudoSqlDTO.getMatchedYN())){
					lSortedMatchResultDTO=new MatchResultDTO();
					//New
					lSourceTempPseudoSqlDTO=new PseudoSqlDTO();	
					if(lMatchResultList.size()>0){										
					lSourceTempPseudoSqlDTO.setIndex(((MatchResultDTO)lMatchResultList.get(lMatchResultList.size()-1)).getSourcePseudoSqlDTO().getIndex());
					lSourceTempPseudoSqlDTO.setSubIndex(((MatchResultDTO)lMatchResultList.get(lMatchResultList.size()-1)).getSourcePseudoSqlDTO().getSubIndex()+1);
					lSourceTempPseudoSqlDTO.setPrevKeyword(lTargetListPseudoSqlDTO.getPrevKeyword());
					lSourceTempPseudoSqlDTO.setPrimarySql(((MatchResultDTO)lMatchResultList.get(lMatchResultList.size()-1)).getSourcePseudoSqlDTO().getPrimarySql());
					}
					lSortedMatchResultDTO.setSourcePseudoSqlDTO(lSourceTempPseudoSqlDTO);
					//New END
					//lSortedMatchResultDTO.setSourcePseudoSqlDTO(new PseudoSqlDTO());
					lSortedMatchResultDTO.setTargetPseudoSqlDTO(lTargetListPseudoSqlDTO);
					lSortedMatchResultDTO.setMatchedYN("N");
					lSortedMatchResultDTO.setMatchDesc("Not Matched");
					//Adding to sortedList
					lSortedMatchResult.add(lSortedMatchResultDTO);
					
					lTargetListPseudoSqlDTO.setMatchedYN("N");
					//lTargetList.remove(j);

					lTargetList.set(j, lTargetListPseudoSqlDTO);
				}
			//}
		}
		
		
		return lSortedMatchResult;
		
	}
	
	public List getComparedPatternData(String pCompareSeq,
			Connection lConnection,String pProcedureName) {
		List lComparedPatternDataList = new ArrayList();
		ComparedStatementDTO lComparedStatementDTO = null;
		ResultSet lResultSet=null;
		try {
			lPreparedStatement = lConnection
					.prepareStatement(ToolConstant.GET_COMPARED_SOURCE_TARGET_PATTERNS);
			lPreparedStatement.setString(1, pCompareSeq);
			lPreparedStatement.setString(2, pProcedureName);
			
			lResultSet = lPreparedStatement.executeQuery();
			if (lResultSet != null) {
				while (lResultSet.next()) {
					lComparedStatementDTO = new ComparedStatementDTO();
					lComparedStatementDTO
							.setProcName(ToolsUtil.replaceNull(lResultSet
									.getString("PROCEDURE_NAME")));
					lComparedStatementDTO.setOrderNo(ToolsUtil
							.replaceNull(lResultSet.getString("ORDER_NO")));
					lComparedStatementDTO.setSourceStatementNo(ToolsUtil
							.replaceNull(lResultSet
									.getString("SOURCE_STATEMENT_NO")));
					lComparedStatementDTO.setSourcePatternId(ToolsUtil
							.replaceNull(lResultSet
									.getString("SOURCE_PATTERN_ID")));
					lComparedStatementDTO.setSourceFormedStatement(ToolConstant.TOOL_DELIMT+" "+ToolsUtil
							.replaceNull(lResultSet
									.getString("SOURCE_FORMED_STATEMENT")));
					lComparedStatementDTO.setTargetStatementNo(ToolsUtil
							.replaceNull(lResultSet
									.getString("TARGET_STATEMENT_NO")));
					lComparedStatementDTO.setTargetPatternId(ToolsUtil
							.replaceNull(lResultSet
									.getString("TARGET_PATTERN_ID")));
					lComparedStatementDTO.setTargetFormedStatement(ToolsUtil
							.replaceNull(ToolConstant.TOOL_DELIMT+" "+lResultSet
									.getString("TARGET_FORMED_STATEMENT")));
					lComparedStatementDTO.setMatchedYN(ToolsUtil
							.replaceNull(lResultSet.getString("MATCHED_YN")));
					lComparedPatternDataList.add(lComparedStatementDTO);
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
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
		}
		return lComparedPatternDataList;

	}
	public String getCompareKeywordSeq(Connection lConnection) {
		String lRetSeqValue = null;
		ResultSet lResultSet=null;
		try {
			// lConnection = DBConnectionManager.getConnection();

			// select the data from the table

			String lSQL = "UPDATE compare_keyword_seq_table SET COMPARE_KEYWORD_SEQ=COMPARE_KEYWORD_SEQ+1";
			//String lSQL1 = "SELECT COMPARE_KEYWORD_SEQ FROM compare_keyword_seq_table";

			lPreparedStatement = lConnection.prepareStatement(lSQL);
			lPreparedStatement.executeUpdate();
			// lConnection.commit();

			lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_COMPARE_KEYWORD_SEQ);
			lResultSet = lPreparedStatement.executeQuery();
			// if rs == null, then there is no ResultSet to view
			if (lResultSet != null) {
				while (lResultSet.next()) {
					lRetSeqValue = lResultSet.getString("COMPARE_KEYWORD_SEQ");
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// close the connection and the result set
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			// DBConnectionManager.closeConnection(lConnection);
		}
		return lRetSeqValue;

	}

	public void statementCompare(String pCompareSeq, String pDbMigrationType) {

		String lCompareKeywordSeq = "";
		List lComparedPatternDataList= null;
		ResultSet lResultSet=null;
		try {
			lConnection=DBConnectionManager.getConnection();	
			lConnection.setAutoCommit(false);
			lInsertPreparedStatement=lConnection.prepareStatement(ToolConstant.INSERT_COMPARE_FORMED_KEYWORD_STMT);
			
			//get the project details hash map
			InventoryAnalyticsDAO lInventoryAnalyticsDAO = new InventoryAnalyticsDAO();			
			HashMap lProjectDetailsMap =  lInventoryAnalyticsDAO.getDBConnectionDetails(pCompareSeq);
			List lSPNameList = ToolsUtil.getFileNamesFromFolderUpperCase(new File(ToolsUtil.replaceNull((String)lProjectDetailsMap.get("SOURCE_PATH"))),new ArrayList());
			
			
			lKeywordCategoryMap = getKeywordCategoryList(pDbMigrationType);
			//System.out.println("pattern lookup:::::::::::" +lKeywordCategoryMap);
			lDelimtersList = prepareDelimtersList();
			lCompareKeywordSeq = getCompareKeywordSeq(lConnection);
			lSourceToTargetKeywordMap=getSourceToTargetKeywordMap(pDbMigrationType);			
			lPreparedStatement = lConnection
					.prepareStatement(ToolConstant.GET_PROC_NAMES_IN_COMAPRED_FORMED_TABLE);
			
			lPreparedStatement.setString(1, pCompareSeq);
			
			lResultSet = lPreparedStatement.executeQuery();
			if (lResultSet != null) {
				while (lResultSet.next()) {
					
					//added for runnning for new procs only...
					if(!lSPNameList.contains(lResultSet.getString("PROCEDURE_NAME").trim().toUpperCase())){
						continue;
					}
					//added for runnning for new procs only...
					
					//lprocdeureList.add((String)lResultSet.getString("PROCEDURE_NAME").trim());
					System.out.println("Proc name::->"+lResultSet.getString("PROCEDURE_NAME").trim());
					
					//Update Status to Front End - Start
					lCurState="Analysing Gaps";
					lStausMsg="File Name::->"+lResultSet.getString("PROCEDURE_NAME");
					ToolsUtil.prepareInsertStatusMsg(pCompareSeq, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
					//Update Status to Front End - End
					
					lVarUsageMap=getVarUsageMap(pCompareSeq, lResultSet.getString("PROCEDURE_NAME").trim());
					lComparedPatternDataList = getComparedPatternData(pCompareSeq,	lConnection,lResultSet.getString("PROCEDURE_NAME").trim());					
					ComparedStatementDTO lComparedStatementDTO = null;					
					for (int i = 0; i < lComparedPatternDataList.size(); i++) {
						lComparedStatementDTO = (ComparedStatementDTO) lComparedPatternDataList	.get(i);
						if (lComparedStatementDTO != null) {
							//System.out.println("STATEMENT::::"+lComparedStatementDTO.getMatchedYN());
							//if(!lComparedStatementDTO.getMatchedYN().equalsIgnoreCase("N")){
								getComparedCollectiveList(lComparedStatementDTO.getSourceFormedStatement(), lComparedStatementDTO.getTargetFormedStatement(), pCompareSeq, lCompareKeywordSeq, lComparedStatementDTO.getProcName(), lComparedStatementDTO.getOrderNo(),lComparedStatementDTO.getMatchedYN());
							//}
							//getComparedCollectiveList(lComparedStatementDTO.getSourceFormedStatement(), lComparedStatementDTO.getTargetFormedStatement(), pCompareSeq, lCompareKeywordSeq, lComparedStatementDTO.getProcName(), lComparedStatementDTO.getOrderNo());
							// pPreparedStatement.executeUpdate();
						
								

						}
						
					}
				}
			}
				
			
			
			lInsertPreparedStatement.executeBatch();
			lConnection.commit();
			/* pPreparedStatement.executeBatch(); */
			lConnection.setAutoCommit(true);

		}/*
		 * catch (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// close the connection and the result set
			DBConnectionManager.closeConnection(lConnection);
		}

	}
	
	
	public void testMethod(){


		String lCompareKeywordSeq = "";
		
		try {
			String pCompareSeq = "";
			String pDbMigrationType = "";
			String Source = "_DBT-DELIM_ IF  OBJECT_ID  (  'dbo.TAMAutoEvaluate2'  )  IS  NOT  NULL";
			String Target = "_DBT-DELIM_ IF  SWS.SWF_object_id  (  'SQLWAYS_EVAL#luate2'  )  IS  NOT  NULL";
			
			
			//lConnection=DBConnectionManager.getConnection();	
			//lConnection.setAutoCommit(false);
			//lInsertPreparedStatement=lConnection.prepareStatement(ToolConstant.INSERT_COMPARE_FORMED_KEYWORD_STMT);
			
			
			//List lComparedPatternDataList = getComparedPatternData(pCompareSeq,	lConnection);			
			lKeywordCategoryMap = getKeywordCategoryList(pDbMigrationType);
			lDelimtersList = prepareDelimtersList();

			getComparedCollectiveList(Source, Target, pCompareSeq, "", "", "","");

			//lCompareKeywordSeq = getCompareKeywordSeq(lConnection);
			
			//lInsertPreparedStatement.executeBatch();
			//lConnection.commit();
			/* pPreparedStatement.executeBatch(); */
			//lConnection.setAutoCommit(true);

		}/*
		 * catch (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// close the connection and the result set
			DBConnectionManager.closeConnection(lConnection);
		}

		
	}
	public static void main(String[] args) {
		DeepDiveComparisonDAO lTest2 = new DeepDiveComparisonDAO();
		String pDbMigrationType = "SYSBASE_TO_DB2";
		String pCompareSeq="PRID92";
		/*HashMap lKeywordCategoryMap = lTest2
				.getKeywordCategoryList(pDbMigrationType);*/
		//lTest2.lConnection=DBConnectionManager.getConnection();
		//List lDelimtersList = lTest2.prepareDelimtersList(); // String sample =	
		
		// String Source = " Insert  into  #dvuseraccesslevel  (  name  ,  value  )  Select  \"DataVision\"  ,  'N'";
		 //String Target = " Insert  into  SESSION.tt_dvuseraccesslevel  (  name  ,  value  )  Select  'DataVision'  ,  'N'  from  SYSIBM.SYSDUMMY1;  SET  v_PrumarcRptInd  =  'N'";
		System.out.println("Method Starts");
		lTest2.statementCompare(pCompareSeq, pDbMigrationType);
		//lTest2.testMethod();
		
		System.out.println("Method Ends");
		
		
		// "UPDATE #STD_5Day_Decision_Rpt  SET  LastDecisionSoapDate  =  s.Date  ,  SoapCreatedBy  =  cms.Name   FROM tbname s WHERE col1='' ";
		// PSUPDATE;#STD_5Day_Decision_Rpt;SET;LastDecisionSoapDate_
		// s.Date;SoapCreatedBy_cms.Name;FROM;tbname_s;WHERE; col1_'';
		// String
		// String
		// sample="UPDATE  #STD_5Day_Decision_Rpt   SET    LastDecisionSoapDate  =  s.Date  ,  SoapCreatedBy  =  ( Select b.col2 , ( SELECT 2 FROM yyyy ) as col1FROM  xxxx  b ) 'praveen'  ,  SoapCreatedByTeam  =  cms.Team  ,  SoapCreatedbyUserID  =  s.UserId  FROM  #STD_5Day_Decision_Rpt  SDSRL  ,  Soap  S  ,  #CMSUser5Day  cms  WHERE  SDSRL.claimid  =  S.ClaimId  AND  s.UserId  =  cms.UserId  AND  (  (  cms.DefaultClaimGroup  =  '37001'  AND  (  db_name  (  )  LIKE  'East%'  OR  db_name  (  )  LIKE  'DCMS%'  )  )  OR  (  cms.DefaultClaimGroup  =  '37004'  AND  (  db_name  (  )  LIKE  'Pedu%'  OR  db_name  (  )  LIKE  'PCMS%'  )  )  )  AND  s.SoapSeqNum  =  (  SELECT  MAX  (  s1.SoapSeqNum  )  FROM  Soap  s1  WHERE  s1.claimid  =  s.claimid  AND  s1.categORy  =  34003  AND  convert  (  char  (  8  )  ,  s1.date  ,  112  )  <=  convert  (  char  (  8  )  ,  SDSRL.Decisiondate  ,  112  )  )";
		// String
		// sample="UPDATE  #STD_5Day_Decision_Rpt   SET    LastDecisionSoapDate  =  s.Date  ,  SoapCreatedBy  =  ( Select b.col2 ( SELECT * FROM  ( ( ) ) ( ) ( ) GJGJ ) FROM  xxxx  b ) 'praveen'  ,  SoapCreatedByTeam  =  cms.Team  ,  SoapCreatedbyUserID  =  s.UserId  FROM  #STD_5Day_Decision_Rpt  SDSRL  ,  Soap  S  ,  #CMSUser5Day  cms  WHERE  SDSRL.claimid  =  S.ClaimId  AND  s.UserId  =  cms.UserId  AND  (  (  cms.DefaultClaimGroup  =  '37001'  AND  (  db_name  (  )  LIKE  'East%'  OR  db_name  (  )  LIKE  'DCMS%'  )  )  OR  (  cms.DefaultClaimGroup  =  '37004'  AND  (  db_name  (  )  LIKE  'Pedu%'  OR  db_name  (  )  LIKE  'PCMS%'  )  )  )  AND  s.SoapSeqNum  =  (  SELECT  MAX  (  s1.SoapSeqNum  )  FROM  Soap  s1  WHERE  s1.claimid  =  s.claimid  AND  s1.categORy  =  34003  AND  convert  (  char  (  8  )  ,  s1.date  ,  112  )  <=  convert  (  char  (  8  )  ,  SDSRL.Decisiondate  ,  112  )  ) AND A=B";
		
		
		/* String Source= "SELECT  \"EmployeeSSN\"  =  c.ClaimantSocialSecurityNumber  ,  \"EmployeeId\"  =  ce.ClientEmployeeId  ,  \"EmployeeName\"  =  c.ClaimantFirstName  +  \"\"  +  c.ClaimantLastName  ,  c.ClaimId  ,  \"ClaimStatus\"  =  (  SELECT  display  FROM  CodeApplication  WHERE  Code  =  c.ClaimStatus  )  ,  \"DateOfClaimFirstNotice\"  =  convert  (  VARCHAR  (  10  )  ,  c.DateOfClaimFirstNotice  ,  101  )  ,  e.CoverageSeqNum  ,  \"AbsenceReason\"  =  (  SELECT  display  FROM  CodeApplication  WHERE  Code  =  e.AbsenceReason  )  ,  \"LeaveStatus\"  =  (  SELECT  display  FROM  CodeApplication  WHERE  Code  =  e.Status  )  ,  \"LeaveType\"  =  r.RuleName  ,  \"DenialReason\"  =  cd1.Display  ,  \"DenialReasonDescription\"  =  cd2.Display  ,  dod.WorkDateAbsent  ,  \"TimeInDays\"  =  dod.TimeInDays  ,  \"TimeUnits\"  =  dod.TimeUnits  INTO  #DenialDaysRpt  FROM  ReportControl  rc  ,  TAMEvent  e  ,  Claim  c  ,  ClientEmployee  ce  ,  TAMDaysOffDetails  dod  ,  TAMRule  r  ,  CodeApplication  cd1  ,  CodeApplication  cd2  WHERE  rc.ReportCode  =  @ReportCode  AND  rc.ReportId  =  @ReportId  AND  e.PlanControlNumber  =  rc.ControlNumber  AND  e.PlanCoverageType  =  isnull  (  rc.CoverageType  ,  e.PlanCoverageType  )  AND  e.PlanBranch  =  isnull  (  rc.ClaimBranchCode  ,  e.PlanBranch  )  AND  e.Status  !=  'ZE004'  AND  c.ClaimId  =  e.ClaimId  AND  c.DisabilityDate  >=  @StartDate  AND  c.DisabilityDate  <  @EndDate  AND  c.ClaimId  *=  ce.ClaimId  AND  dod.ClaimId  =  e.ClaimId  AND  dod.CoverageSeqNum  =  e.CoverageSeqNum  AND  dod.Status  =  'ZV003'  AND  dod.RuleId  =  r.RuleId  AND  dod.RuleVersionNumber  =  r.VersionNumber  AND  cd1.Code  =  dod.StatusReason  AND  cd2.Code  =  dod.StatusReasonDescriptor  AND  exists  (  select  1  from  Coverage  cv  where  cv.ClaimId  =  e.ClaimId  and  cv.PlanCoverageType  =  'UCD'  and  cv.Status  <>  '02006'  )";
		 String Target= "INSERT  INTO  SESSION.tt_DenialDaysRpt  SELECT  c.ClaimantSocialSecurityNumber  EmployeeSSN  ,  ce.ClientEmployeeId  EmployeeId  ,  c.ClaimantFirstName  ||  ''  ||  c.ClaimantLastName  EmployeeName  ,  c.ClaimId  ,  (  SELECT  display  FROM  CodeApplication2  WHERE  Code  =  c.ClaimStatus  )  ClaimStatus  ,  CAST  (  CHAR  (  c.DateOfClaimFirstNotice  )  AS  VARCHAR  (  10  )  )  DateOfClaimFirstNotice  ,  e.CoverageSeqNum  ,  (  SELECT  display  FROM  CodeApplication2  WHERE  Code  =  e.AbsenceReason  )  AbsenceReason  ,  (  SELECT  display  FROM  CodeApplication2  WHERE  Code  =  e.Status  )  LeaveStatus  ,  r.RuleName  LeaveType  ,  cd1.Display  DenialReason  ,  cd2.Display  DenialReasonDescription  ,  dod.WorkDateAbsent  ,  dod.TimeInDays  TimeInDays  ,  dod.TimeUnits  TimeUnits  FROM  Claim  c  LEFT  OUTER  JOIN  ClientEmployee  ce  ON  c.ClaimId  =  ce.ClaimId  ,  ReportControl  rc  ,  TAMEvent  e  ,  TAMDaysOffDetails  dod  ,  TAMRule2  r  ,  CodeApplication2  cd1  ,  CodeApplication2  cd2  WHERE  rc.ReportCode  =  v_ReportCode  AND  rc.ReportId  =  v_ReportId  AND  e.PlanControlNumber  =  rc.ControlNumber  AND  e.PlanCoverageType  =  coalesce  (  rc.CoverageType  ,  e.PlanCoverageType  )  AND  e.PlanBranch  =  coalesce  (  rc.ClaimBranchCode  ,  e.PlanBranch  )  AND  e.Status  !=  'ZE004'  AND  c.ClaimId  =  e.ClaimId  AND  c.DisabilityDate  >=  v_StartDate  AND  c.DisabilityDate  <  v_EndDate  AND  dod.ClaimId  =  e.ClaimId  AND  dod.CoverageSeqNum  =  e.CoverageSeqNum  AND  dod.Status  =  'ZV003'  AND  dod.RuleId  =  r.RuleId  AND  dod.RuleVersionNumber  =  r.VersionNumber  AND  cd1.Code  =  dod.StatusReason  AND  cd2.Code  =  dod.StatusReasonDescriptor  AND  exists  (  select  1  from  Coverage  cv  where  cv.ClaimId  =  e.ClaimId  and  cv.PlanCoverageType  =  'UCD'  and  cv.Status  <>  '02006'  )  ;";
		 
		 //Source = "select isnull ( asd , asd ) , @ClaimantSocialSecurityNumber  =  ClaimantSocialSecurityNumber  from  DCMSbcp..TakeoverFeedSummaryBcp  where  ControlNumber  =  @VarControlNumber  AND  ClientClaimNumber  =  @ClientClaimNumber";
		 //Target = " select  ClaimantSocialSecurityNumber  INTO  v_ClaimantSocialSecurityNumber  from  TakeoverFeedSummaryBcp  where  ControlNumber  =  v_VarControlNumber  AND  ClientClaimNumber  =  v_ClientClaimNumber; ";
		 
		 Source = " INSERT   INTO  Timeline  (  ClaimId  ,  TimeLineSeqNum  ,  EventDate  ,  EventText  ,  EventCode  ,  UserId  ,  CriticalInd  )  VALUES  (  @ClaimId  ,  @TimelineSeqNum  ,  getdate  (  )  ,  ,  )";
		 Target = " INSERT  INTO  TimeLine2  (  ClaimId  ,  TimeLineSeqNum  ,  EventDate  ,  EventText  ,  EventCode  ,  UserId  ,  CriticalInd  )  VALUES  (  v_ClaimId  ,  v_TimelineSeqNum  ,  CURRENT  TIMESTAMP  ,  ,  )  ;  SET  v_Counter  =  0;  SET  v_Counter  =  CHAR  (  v_Counter  )  ||  1; ";
		 
		 Source = " insert  #UniqueClaim  select @id = ClaimId  ,  CoverageSeqNum  ,  Status  ,  StatusReason  ,  DataUpdateDateTime  ,  PaymentThruDateAU  ,  null  ,  '_TFC_P_TFC_'  WhichTable  from  #PaymentThruDateLog  PL  order  by  DataUpdateDateTime ";
		 Target = " insert INTO  SESSION.tt_UniqueClaim  select  v_id = ClaimId  ,  CoverageSeqNum  ,  Status  ,  StatusReason  ,  DataUpdateDateTime  ,  PaymentThruDateAU  ,  CAST  (  null  as  INTEGER  )  ,  '_TFC_P_TFC_'  WhichTable  from  SESSION.tt_PaymentThruDateLog  PL  order  by  DataUpdateDateTime; ";*/
		 
		/* Source=" Order  by DataUpdateDateTim ";
		 Target=" Order by DataUpdateDateTim;";*/
		 // sample=lTest.handleSelectIntoVariable(sample);
		 /*List lSourceList = lTest2.getPatternString(Source,lKeywordCategoryMap,lDelimtersList);
		 List lTargetList =lTest2.getPatternString(Target,lKeywordCategoryMap,lDelimtersList);
		 List lMatchResultList=new ArrayList();
		 lTest2.getComparedCollectiveList(lSourceList,lTargetList);*/
		/*for (int i = 0; i < lSourceList.size(); i++) {
			 PseudoSqlDTO lPseudoSqlDTO = (PseudoSqlDTO) lSourceList.get(i);
				
				
				  System.out.println("getPseudoSql::->"+lPseudoSqlDTO.
				  getPseudoSql());
				  System.out.println("getPatternType::->"+lPseudoSqlDTO
				  .getPatternType());
				  System.out.println("getActualString::->"+lPseudoSqlDTO
				  .getActualString());
				  System.out.println("getAliasName::->"+lPseudoSqlDTO
				  .getAliasName());
				  System.out.println("getiNDEX::->"+lPseudoSqlDTO
						  .getIndex());
				 
		 }*/
		/* List lMatchResultList=new ArrayList();
		 lTest2.getComparedCollectiveList(lSourceList,lTargetList,lMatchResultList);*/
		 
		 
		 
		 /*for (int j = 0; j < lMatchResultList.size(); j++) {
			 MatchResultDTO  lMatchResultDTO=(MatchResultDTO)lMatchResultList.get(j);
			System.out.println(":::::source->"+lMatchResultDTO.getSourcePseudoSqlDTO().getActualString()+"<<<<>>>>>"+lMatchResultDTO.getTargetPseudoSqlDTO().getActualString()+"<<<>>>>"+lMatchResultDTO.getMatchDesc());
		}
*/
		 
		/*String sample =  "ISNULL ( MAX ( TIMELINESEQNUM ) , 0 ) + 1 @TIMELINESEQNUM ";
		String[] lColArr = lTest2.getColData(sample);
		//String[] lColArr = lTest2.getTablePart(sample);
		System.out.println("ColName::->"+lColArr[0]+"  Value: "+lColArr[1]);*/
		 
		/*String sampleTab="ISNULL ( MAX ( TIMELINESEQNUM ) , 0 ) + 1 @TIMELINESEQNUM ";
		 String[] temp = lTest2.getTablePart(sampleTab);
		 for (int i = 0; i < temp.length; i++) {
			 System.out.println(":::::array value::::"+temp[i]);
		 }*/	 

	}
}
