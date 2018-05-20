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

import javax.swing.JOptionPane;

import com.tcs.tools.business.compare.dto.ComparedStatementDTO;
import com.tcs.tools.business.compare.dto.CursorDetailsDTO;
import com.tcs.tools.business.compare.dto.PatternDataDTO;
import com.tcs.tools.business.compare.dto.PatternMatchCountDTO;
import com.tcs.tools.business.compare.dto.PatternMatchDTO;
import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dao.InventoryAnalyticsDAO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

;

public class FileCompareDAO {

	private Connection lConnection = null;
	private PreparedStatement lPreparedStatement = null;
	private ResultSet lResultSet = null;

	private List lSourceCursorDetailsList = new ArrayList();
	private int lInsertBatchCount = 0;
	private int lOrderCount = 0;
	private List lSetVarList=null;
	private PreparedStatement lInsertVarUsagePreparedStatement=null;
	private HashMap lPatternMatchCountMapOtherDB=null;
	private HashMap lSrcTargtPatternMatchHMOtherDB=null;
	 String lCurState="";
	 String lStausMsg="";

	public List getPatternMatchCountList(String pDbMigrationType,
			Connection lConnection) {
		List lPatternMatchCountList = new ArrayList();
		
		try {
			lPreparedStatement = lConnection
					.prepareStatement(ToolConstant.GET_PATTERN_MATCH_COUNT_DETAILS);
			lPreparedStatement.setString(1, pDbMigrationType);
			lResultSet = lPreparedStatement.executeQuery();
			PatternMatchCountDTO lPatternMatchCountDTO = null;
			if (lResultSet != null) {
				while (lResultSet.next()) {
					lPatternMatchCountDTO = new PatternMatchCountDTO();
					lPatternMatchCountDTO.setPatternId(ToolsUtil
							.replaceNull(lResultSet.getString("PATTERN_ID")));
					lPatternMatchCountDTO.setPatternDesc(ToolsUtil
							.replaceNull(lResultSet.getString("PATTERN_DESC")));
					lPatternMatchCountDTO.setMatchCount(ToolsUtil
							.replaceNull(lResultSet.getString("COUNT_VAL")));
					lPatternMatchCountList.add(lPatternMatchCountDTO);
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
		return lPatternMatchCountList;

	}

	/**
	 * @param pRunId
	 * @param lConnection
	 * @return
	 */
	public List getCursorDetailsList(String pRunId,List pSPNameList ,Connection lConnection) {
		List lCursorDetailsList = new ArrayList();
		try {
			System.out.println(":::::inside - getCursorDetailsList method:::::");
			lPreparedStatement = lConnection
					.prepareStatement(ToolConstant.GET_CURSOR_DETAILS);
			lPreparedStatement.setString(1, pRunId);
			System.out.println("lPreparedStatement::->"+lPreparedStatement);
			lResultSet = lPreparedStatement.executeQuery();
			CursorDetailsDTO lCursorDetailsDTO = null;
			if (lResultSet != null) {
				while (lResultSet.next()) {
					lCursorDetailsDTO = new CursorDetailsDTO();
					// ORDER_NO, PROCEDURE_NAME, STATEMENT_NO, CURSOR_NAME
					if(!pSPNameList.contains(ToolsUtil.replaceNull(lResultSet.getString("PROCEDURE_NAME").trim().toUpperCase()))){
						System.out.println(":: SP Not Found:::"+lResultSet.getString("PROCEDURE_NAME"));
						continue;
					}
					lCursorDetailsDTO
							.setProcedureName(ToolsUtil.replaceNull(lResultSet
									.getString("PROCEDURE_NAME")));
					lCursorDetailsDTO.setCursorName(ToolsUtil
							.replaceNull(lResultSet.getString("CURSOR_NAME")));
					lCursorDetailsDTO.setStatementNo(ToolsUtil
							.replaceNull(lResultSet.getString("STATEMENT_NO")));
					lCursorDetailsDTO.setOrderNo(ToolsUtil
							.replaceNull(lResultSet.getString("ORDER_NO")));
					lCursorDetailsList.add(lCursorDetailsDTO);
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
		return lCursorDetailsList;

	}

	public HashMap getPatternMatchCountHashMap(String pDbMigrationType,
			Connection lConnection) {
		HashMap lPatternMatchCountMap = new HashMap();
		// List lPatternMatchCountList = new ArrayList();
		try {
			lPreparedStatement = lConnection
					.prepareStatement(ToolConstant.GET_PATTERN_MATCH_COUNT_DETAILS);
			lPreparedStatement.setString(1, pDbMigrationType);
			lResultSet = lPreparedStatement.executeQuery();

			if (lResultSet != null) {
				while (lResultSet.next()) {
					lPatternMatchCountMap.put((String) ToolsUtil.replaceNull(lResultSet.getString("PATTERN_ID")
									.trim()), (String) ToolsUtil
							.replaceNull(lResultSet.getString("COUNT_VAL")));
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
		return lPatternMatchCountMap;

	}

	public List getPatternDataList(String pDbMigrationType,
			Connection lConnection, String pRunId) {
		List lPatternDataList = new ArrayList();
		try {
			lPreparedStatement = lConnection
					.prepareStatement(ToolConstant.GET_PATTERN_DATA_MATCH);
			lPreparedStatement.setString(1, pRunId);
			lResultSet = lPreparedStatement.executeQuery();
			PatternDataDTO lPatternDataDTO = null;
			if (lResultSet != null) {
				while (lResultSet.next()) {
					lPatternDataDTO = new PatternDataDTO();
					lPatternDataDTO.setPatternId(ToolsUtil
							.replaceNull(lResultSet.getString("PATTERN_ID")));
					lPatternDataDTO.setPatternDesc(ToolsUtil
							.replaceNull(lResultSet.getString("PATTERN_DESC")));
					lPatternDataDTO
							.setProcedureName(ToolsUtil.replaceNull(lResultSet
									.getString("PROCEDURE_NAME")));
					lPatternDataDTO.setStatementNo(ToolsUtil
							.replaceNull(lResultSet.getString("STATEMENT_NO")));
					lPatternDataDTO.setFormedStatement(ToolsUtil
							.replaceNull(lResultSet
									.getString("FORMED_STATEMENT")));

					lPatternDataList.add(lPatternDataDTO);
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
		return lPatternDataList;

	}

	public List getSourceTargetPatternMatchList(Connection lConnection,
			String pDbMigrationType) {
		List lSourceTargetPatternMatchList = new ArrayList();
		try {
			lPreparedStatement = lConnection
					.prepareStatement(ToolConstant.GET_SOURCE_TARGET_PATTERN_MATCH_DETAILS);
			lPreparedStatement.setString(1, pDbMigrationType);
			lResultSet = lPreparedStatement.executeQuery();
			PatternMatchDTO lPatternMatchDTO = null;
			if (lResultSet != null) {
				while (lResultSet.next()) {
					lPatternMatchDTO = new PatternMatchDTO();
					// SEQ_NO, SOURCE_PATTERN_ID, TARGET_PATTERN_ID
					lPatternMatchDTO.setSeqNo(lResultSet.getInt("SEQ_NO"));
					lPatternMatchDTO.setSourcePatternId(lResultSet
							.getString("SOURCE_PATTERN_ID"));
					lPatternMatchDTO.setTargetPatternId(lResultSet
							.getString("TARGET_PATTERN_ID"));
					// System.out.println("Source "+lPatternMatchDTO.getSourcePatternId()+" Target "+lPatternMatchDTO.getTargetPatternId());
					lSourceTargetPatternMatchList.add(lPatternMatchDTO);
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
		return lSourceTargetPatternMatchList;

	}

	public HashMap getSourceTargetPatternMatchHashMap(String pDbMigrationType,
			Connection lConnection) {
		HashMap lSourceTargetPatternMatchHM = new HashMap();
		List lPatternMatchList = new ArrayList();
		try {

			List lSourceTargetPatternMatchList = getSourceTargetPatternMatchList(
					lConnection, pDbMigrationType);
			String lPrevSourcePatternId = "";
			if (lSourceTargetPatternMatchList != null) {
				for (int i = 0; i <= lSourceTargetPatternMatchList.size(); i++) {
					if (i == lSourceTargetPatternMatchList.size()) {
						lSourceTargetPatternMatchHM.put(
								(String) lPrevSourcePatternId.trim(),
								(List) lPatternMatchList);
						break;
					}

					PatternMatchDTO lPatternMatchDTO = (PatternMatchDTO) lSourceTargetPatternMatchList
							.get(i);
					if (i == 0
							|| lPrevSourcePatternId
									.equalsIgnoreCase(lPatternMatchDTO
											.getSourcePatternId().trim())) {
						lPatternMatchList.add((String) lPatternMatchDTO
								.getTargetPatternId().trim());

					} else {
						lSourceTargetPatternMatchHM.put(
								(String) lPrevSourcePatternId.trim(),
								(List) lPatternMatchList);
						lPatternMatchList = new ArrayList();
						lPatternMatchList.add((String) lPatternMatchDTO
								.getTargetPatternId().trim());
					}
					lPrevSourcePatternId = lPatternMatchDTO
							.getSourcePatternId().trim();
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// close the connection and the result set
			/*
			 * DBConnectionManager.closeConnection(lPreparedStatement,
			 * lResultSet);
			 */
		}
		return lSourceTargetPatternMatchHM;

	}

	/**
	 * @param pProcedureName
	 * @param pFormedStatement
	 * @return
	 */

	public String getCursorName(String pFormedStatement) {
		try {
			pFormedStatement = pFormedStatement.trim().toUpperCase();
			Pattern lCursorPattern = Pattern.compile("\\bCURSOR\\b[\\S\\W\\w\\r\\n]+\\bFOR\\b");
			
			
			String lCursorName = "";
			// Getting Cursor name from fomedStaement.
			if (lCursorPattern.matcher(pFormedStatement).find()) {
				lCursorName = pFormedStatement.substring(
						pFormedStatement.indexOf("DECLARE ") + 8,
						pFormedStatement.indexOf(" CURSOR")).trim();
				if (lCursorName == null && (("".equals(lCursorName)))) {
					return "";
				} else {
					return lCursorName.trim();
				}

				/*
				 * System.out.println("lFormedStatement :--> "+lFormedStatement);
				 * System.out.println("Cursor Name:-->  "+lCursorName);
				 */
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "";

	}

	public void compareDeclareCursorPattern(List pPatternDataListSource,
			List pPatternDataListTarget, HashMap pPatternMatchCountMap,
			String pCompareSeq) {
		List lDecalreCursorListSource = new ArrayList();
		List lDecalreCursorListTarget = new ArrayList();
		List lMatchList = new ArrayList();
		CursorDetailsDTO lCursorDetailsDTO = null;
		ComparedStatementDTO lComparedStatementDTO = null;
		PreparedStatement pPreparedStatement = null;

		PatternDataDTO lPatternDataDTOSource = new PatternDataDTO();
		PatternDataDTO lPatternDataDTOTarget = new PatternDataDTO();
		// Preparing Source Declare Cursor Pattern List
		if (pPatternDataListSource == null
				|| pPatternDataListSource.size() == 0) {
			return;
		}
		//int lOrderCount = 0;
		for (int i = 0; i < pPatternDataListSource.size(); i++) {
			lPatternDataDTOSource = (PatternDataDTO) pPatternDataListSource
					.get(i);
			if ((lPatternDataDTOSource.getPatternId().toString().trim()
					.equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_CURSOR))||(lPatternDataDTOSource.getPatternId().toString().trim()
					.equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_CURSOR_SYBASEORACLE))) {
				System.out.println("-----------------To check Cursor - Source-----------------------");
				System.out.println(lPatternDataDTOSource.getFormedStatement());
				lCursorDetailsDTO = new CursorDetailsDTO();
				lCursorDetailsDTO.setProcedureName(lPatternDataDTOSource
						.getProcedureName().trim());
				lCursorDetailsDTO.setCursorName(getCursorName(
						lPatternDataDTOSource.getFormedStatement()).trim()
						.toUpperCase());
				lCursorDetailsDTO.setStatementNo(lPatternDataDTOSource
						.getStatementNo().trim());
				lCursorDetailsDTO.setFormedStatement(lPatternDataDTOSource
						.getFormedStatement().trim());
				lCursorDetailsDTO.setPatternId(lPatternDataDTOSource
						.getPatternId().trim());
				lDecalreCursorListSource.add(lCursorDetailsDTO);
			}
		}
		// Preparing Target Declare Cursor Pattern List
		for (int i = 0; i < pPatternDataListTarget.size(); i++) {
			lPatternDataDTOTarget = (PatternDataDTO) pPatternDataListTarget
					.get(i);
			if ((lPatternDataDTOTarget.getPatternId().toString().trim()
					.equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_CURSOR))||(lPatternDataDTOSource.getPatternId().toString().trim()
					.equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_CURSOR_SYBASEORACLE))) {
				
				if (checkInputCursorName(
						((lPatternDataDTOTarget).getProcedureName()),
						(lPatternDataDTOTarget).getFormedStatement())) {
					System.out.println("-----------------To check Cursor -Target 1111-----------------------"+(lPatternDataDTOTarget).getProcedureName());
					System.out.println(lPatternDataDTOTarget.getFormedStatement());
					lCursorDetailsDTO = new CursorDetailsDTO();
					lCursorDetailsDTO.setProcedureName(lPatternDataDTOTarget
							.getProcedureName().trim());
					lCursorDetailsDTO.setCursorName(getCursorName(
							lPatternDataDTOTarget.getFormedStatement()).trim()
							.toUpperCase());
					lCursorDetailsDTO.setStatementNo(lPatternDataDTOTarget
							.getStatementNo().trim());
					lCursorDetailsDTO.setFormedStatement(lPatternDataDTOTarget
							.getFormedStatement().trim());
					lCursorDetailsDTO.setPatternId(lPatternDataDTOTarget
							.getPatternId().trim());
					lDecalreCursorListTarget.add(lCursorDetailsDTO);
					System.out.println("TARGET CURSOR LIST "+lDecalreCursorListTarget);
				}

			}
		}

		//
		if (lDecalreCursorListSource != null && lDecalreCursorListSource.size()  > 0  ) {
			try {
				pPreparedStatement = lConnection
						.prepareStatement(ToolConstant.INSERT_COMPARE_FORMED_STMT);
				CursorDetailsDTO lCursorDetailsDTOSource = null;
				CursorDetailsDTO lCursorDetailsDTOTarget = null;
				/*
				 * for (int i = 0; i < lDecalreCursorListSource.size(); i++) {
				 * lCursorDetailsDTOSource
				 * =(CursorDetailsDTO)lDecalreCursorListSource.get(i);
				 * System.out
				 * .println(" Source "+i+" -->"+lCursorDetailsDTOSource
				 * .getCursorName()); } for (int i = 0; i <
				 * lDecalreCursorListTarget.size(); i++) {
				 * lCursorDetailsDTOTarget
				 * =(CursorDetailsDTO)lDecalreCursorListTarget.get(i);
				 * System.out
				 * .println(" Source "+i+" -->"+lCursorDetailsDTOTarget
				 * .getCursorName()); }
				 */
				/*
				 * System.out.println(
				 * "------------------------------------------------");
				 * System.out.println("lDecalreCursorListSource size "+
				 * lDecalreCursorListSource.size());
				 * System.out.println("lDecalreCursorListTarget size "
				 * +lDecalreCursorListTarget.size());
				 */
				boolean lChkMatchFound = false;
				for (int i = 0; i < lDecalreCursorListSource.size(); i++) {
					lChkMatchFound = false;
					lCursorDetailsDTOSource = (CursorDetailsDTO) lDecalreCursorListSource
							.get(i);
					
					//lOrderCount++;
					
					
					for (int j = 0; j < lDecalreCursorListTarget.size(); j++) {
						lCursorDetailsDTOTarget = (CursorDetailsDTO) lDecalreCursorListTarget
								.get(j);
						if (lCursorDetailsDTOSource
								.getCursorName()
								.trim()
								.equalsIgnoreCase(
										lCursorDetailsDTOTarget.getCursorName()
												.trim())) {
							/*
							 * System.out.println(" Source::->"+
							 * lCursorDetailsDTOSource.getFormedStatement());
							 * System
							 * .out.println(" Target::->"+lCursorDetailsDTOTarget
							 * .getFormedStatement());
							 */

							// ////////

							lComparedStatementDTO = new ComparedStatementDTO();
							lOrderCount++;
							lComparedStatementDTO.setCompareSeq(pCompareSeq);
							lComparedStatementDTO.setOrderNo(lOrderCount+"");
							lComparedStatementDTO
									.setProcName(lCursorDetailsDTOSource
											.getProcedureName());
							lComparedStatementDTO
									.setSourceStatementNo(lCursorDetailsDTOSource
											.getStatementNo());
							lComparedStatementDTO
									.setSourcePatternId(lCursorDetailsDTOSource
											.getPatternId());
							lComparedStatementDTO
									.setSourceFormedStatement(lCursorDetailsDTOSource
											.getFormedStatement());
							lComparedStatementDTO
									.setTargetStatementNo(lCursorDetailsDTOTarget
											.getStatementNo());
							lComparedStatementDTO
									.setTargetPatternId(lCursorDetailsDTOTarget
											.getPatternId());
							lComparedStatementDTO
									.setTargetFormedStatement(lCursorDetailsDTOTarget
											.getFormedStatement());
							lComparedStatementDTO.setMatchedYN("Y");
							pPreparedStatement = insertCompareStatement(
									pPreparedStatement, lConnection,
									lComparedStatementDTO);

							// /////////
							lDecalreCursorListSource.remove(i);
							i--;
							lDecalreCursorListTarget.remove(j);
							j--;
							lChkMatchFound = true;
							break;
						}

					}
					if (lChkMatchFound == false) {
						lCursorDetailsDTOTarget = new CursorDetailsDTO();
						// System.out.println(" Not Matched Source::->"+lCursorDetailsDTOSource.getFormedStatement());
						lComparedStatementDTO = new ComparedStatementDTO();
						lOrderCount++;
						lComparedStatementDTO.setCompareSeq(pCompareSeq);
						lComparedStatementDTO.setOrderNo(lOrderCount+"");
						lComparedStatementDTO
								.setProcName(lCursorDetailsDTOSource
										.getProcedureName());
						lComparedStatementDTO
								.setSourceStatementNo(lCursorDetailsDTOSource
										.getStatementNo());
						lComparedStatementDTO
								.setSourcePatternId(lCursorDetailsDTOSource
										.getPatternId());
						lComparedStatementDTO
								.setSourceFormedStatement(lCursorDetailsDTOSource
										.getFormedStatement());
						lComparedStatementDTO
								.setTargetStatementNo(lCursorDetailsDTOTarget
										.getStatementNo());
						lComparedStatementDTO
								.setTargetPatternId(lCursorDetailsDTOTarget
										.getPatternId());
						lComparedStatementDTO
								.setTargetFormedStatement(lCursorDetailsDTOTarget
										.getFormedStatement());
						lComparedStatementDTO.setMatchedYN("N");
						pPreparedStatement = insertCompareStatement(
								pPreparedStatement, lConnection,
								lComparedStatementDTO);
						lDecalreCursorListSource.remove(i);
						i--;
					}

				}
				// System.out.println("------------------------------------------------");
				pPreparedStatement.executeBatch();

			} catch (Exception pException) {
				pException.printStackTrace();
			} finally {
				DBConnectionManager.closeConnection(pPreparedStatement, null);
			}

		}

	}

	public HashMap getPatternDataMap(String pDbMigrationType,
			Connection lConnection, String pRunId,List pSPNameList) {
		List lPatternDataList = new ArrayList();
		HashMap lPatternDataCollectiveMap = new HashMap();
		String lPrevProcName = "";
		String lCurProcName = "";
		int i = 0;
		try {
			lPreparedStatement = lConnection
					.prepareStatement(ToolConstant.GET_PATTERN_DATA_MATCH);
			lPreparedStatement.setString(1, pRunId);
			//System.out.println("Query::->"+lPreparedStatement.toString());
			lResultSet = lPreparedStatement.executeQuery();
			PatternDataDTO lPatternDataDTO = null;
			if (lResultSet != null) {
				while (lResultSet.next()) {
					lCurProcName = lResultSet.getString("PROCEDURE_NAME");
					//System.out.println(lResultSet.getString("PROCEDURE_NAME"));
					//added for runnning for new procs only...
					if(!pSPNameList.contains(lResultSet.getString("PROCEDURE_NAME").trim().toUpperCase())){
						continue;
					}
					//added for runnning for new procs only...

					if (i == 0) {
						lPrevProcName = ToolsUtil.replaceNull(lCurProcName);
					}
					if ((!(lPrevProcName.equalsIgnoreCase(ToolsUtil
							.replaceNull(lCurProcName))))) {
						// lPrevProcName ="";
						// System.out.println("procname "+lPrevProcName);
						lPatternDataCollectiveMap.put(lPrevProcName.trim()
								.toUpperCase(), lPatternDataList);
						lPatternDataList = new ArrayList();
						i = 0;
					}
					lPatternDataDTO = new PatternDataDTO();
					lPatternDataDTO.setPatternId(ToolsUtil
							.replaceNull(lResultSet.getString("PATTERN_ID")));
					lPatternDataDTO.setPatternDesc(ToolsUtil
							.replaceNull(lResultSet.getString("PATTERN_DESC")));
					lPatternDataDTO.setProcedureName(ToolsUtil
							.replaceNull(lCurProcName));
					lPatternDataDTO.setStatementNo(ToolsUtil
							.replaceNull(lResultSet.getString("STATEMENT_NO")));
					lPatternDataDTO.setFormedStatement(ToolsUtil
							.replaceNull(lResultSet
									.getString("FORMED_STATEMENT")));

					lPatternDataList.add(lPatternDataDTO);
					i++;
					lPrevProcName = ToolsUtil.replaceNull(lCurProcName);
				}
				//System.out.println("Data List::"+lPatternDataList.size());
				lPatternDataCollectiveMap.put(lPrevProcName.trim()
						.toUpperCase(), lPatternDataList);

				/*
				 * Iterator iterator =
				 * lPatternDataCollectiveMap.keySet().iterator(); String
				 * lkey=""; List lvalue=new ArrayList(); PatternDataDTO
				 * lTempPatternDataDTO=new PatternDataDTO(); while
				 * (iterator.hasNext()) { lkey=iterator.next().toString();
				 * System.out.println("Key"+lkey);
				 * lvalue=(List)lPatternDataCollectiveMap.get((String)lkey); for
				 * (int j = 0; j < lvalue.size(); j++) {
				 * lTempPatternDataDTO=(PatternDataDTO) lvalue.get(j);
				 * System.out
				 * .println("  j-->"+j+" : "+lTempPatternDataDTO.getFormedStatement
				 * ()); } }
				 */
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
		//System.out.println("Map Size::"+lPatternDataCollectiveMap.size()+"Data::"+lPatternDataCollectiveMap);
		return lPatternDataCollectiveMap;
	}
	
	public String prepareListsToCompareSybaseToOracle(String pRunIdSybase, String pRunIdOracle,
			String pDbMigrationType,String pProjectId) {
		String lCompareSeq = "";
		try {
			lConnection = DBConnectionManager.getConnection();
			lConnection.setAutoCommit(false);
			HashMap lPatternMatchCountMap=null;
			HashMap lSrcTargtPatternMatchHM=null;
			
			if(pDbMigrationType.trim().equalsIgnoreCase(WebConstant.DB_MIGRATION_TYPE_SYBASE_TO_Oracle)){
				 lPatternMatchCountMap = getPatternMatchCountHashMap(
						pDbMigrationType, lConnection);
				 lSrcTargtPatternMatchHM = getSourceTargetPatternMatchHashMap(
						pDbMigrationType, lConnection);
				 //JOptionPane.showMessageDialog(null,"INSIDE IF oracle sybase");
			}
			//get the project details hash map
			InventoryAnalyticsDAO lInventoryAnalyticsDAO = new InventoryAnalyticsDAO();			
			HashMap lProjectDetailsMap =  lInventoryAnalyticsDAO.getDBConnectionDetails(pProjectId);
			List lSPNameList = ToolsUtil.getFileNamesFromFolderUpperCase(new File(ToolsUtil.replaceNull((String)lProjectDetailsMap.get("SOURCE_PATH"))),new ArrayList());
			
			
			// Getting pattern data hashmap
			HashMap lSybasePatternDataMap = getPatternDataMap(pDbMigrationType,
					lConnection, pRunIdSybase,lSPNameList); //need to add condition
			 //JOptionPane.showMessageDialog(null,"lSybasePatternDataMap "+lSybasePatternDataMap.toString());
			// System.out.println("Sybase::->"+lSybasePatternDataMap.toString());
			 
			HashMap lOraclePatternDataMap = getPatternDataMap(pDbMigrationType,
					lConnection, pRunIdOracle,lSPNameList); //need to add condition
			 //JOptionPane.showMessageDialog(null,"lOraclePatternDataMap "+lOraclePatternDataMap.toString());
			//System.out.println("DB2::->"+lDb2PatternDataMap.toString());
			
			
			lInsertVarUsagePreparedStatement=lConnection.prepareStatement(ToolConstant.INSERT_VARIABLES_INTO_VARIABLE_USAGE_TABLE);
			 //JOptionPane.showMessageDialog(null,"lInsertVarUsagePreparedStatement "+lInsertVarUsagePreparedStatement);
			// Creating Source and Target Lists to populate pattern for a single
			// procedure at a time.
			List lPatternDataListSybase = new ArrayList();
			List lPatternDataListOracle = new ArrayList();
			// getting the compare seq no
			 //lCompareSeq = getCompareSeq(lConnection);
			lCompareSeq = pProjectId;

			// insert into main table
			insertCompareMain(lConnection, lCompareSeq, pDbMigrationType,
					pRunIdSybase, pRunIdOracle);

			// get the cursor details
			lSourceCursorDetailsList = getCursorDetailsList(pRunIdSybase,lSPNameList,
					lConnection); //need to add condition
			// System.out.println(":::::lSourceCursorDetailsList size:::::"+lSourceCursorDetailsList.size());

			System.out.println("lSybasePatternDataMap Count"
					+ lSybasePatternDataMap.size());

			// Comparing One by one procedure
			Iterator iterator = lSybasePatternDataMap.keySet().iterator();
			int lProcCount = 1;
			while (iterator.hasNext()) {
				String lProcedureName = iterator.next().toString().trim()
						.toUpperCase();
				if (lOraclePatternDataMap.containsKey(lProcedureName)) {
					lPatternDataListSybase = (List) lSybasePatternDataMap.get((String) lProcedureName);
					lPatternDataListOracle = (List) lOraclePatternDataMap.get((String) lProcedureName);
					System.out.println(":::->Proc Name: " + lProcedureName+ " Proc Count:->" + lProcCount);
					
 				   //Update Status to Front End - Start
 				   lCurState="Source to Target Mapping";
 				   lStausMsg="File Name::->"+lProcedureName;
 				   ToolsUtil.prepareInsertStatusMsg(pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
 				   //Update Status to Front End - End
					
					// Invoking Method to Compare
					lOrderCount = 0;
					if(pDbMigrationType.trim().equalsIgnoreCase(WebConstant.DB_MIGRATION_TYPE_SYBASE_TO_Oracle)){
						compareTwoProcsSybaseToOracle(lPatternDataListSybase,lPatternDataListOracle, lPatternMatchCountMap,lSrcTargtPatternMatchHM, lCompareSeq);
						compareDeclareCursorPattern(lPatternDataListSybase,	lPatternDataListOracle, lPatternMatchCountMap,	lCompareSeq);// Comparing Cursor Patterns
						compareDeclareVariables(lPatternDataListSybase,lPatternDataListOracle, lCompareSeq); // Comparing Declare Variable Patterns
					}
					

				} else {
					//Update Status to Front End - Start
					lCurState="Error";
					lStausMsg=lProcedureName.toLowerCase()+" Error::->Procedure not found in Target..<br/> Please ensure the source and target file names should be same...";
					ToolsUtil.prepareInsertStatusMsg(pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Error",new Timestamp(System.currentTimeMillis()),lConnection);
					//Update Status to Front End - End
					System.out.println("Procedure not found in Target Hashmap");
				}
				lProcCount++;
			}
			lConnection.commit();
			lConnection.setAutoCommit(true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// close the connection and the result set
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			DBConnectionManager.closeConnection(lConnection);
		}

		return lCompareSeq;
	}

	public String prepareListsToCompare(String pRunIdSybase, String pRunIdDb2,
			String pDbMigrationType,String pProjectId) {
		String lCompareSeq = "";
		try {
			lConnection = DBConnectionManager.getConnection();
			lConnection.setAutoCommit(false);
			HashMap lPatternMatchCountMap=null;
			HashMap lSrcTargtPatternMatchHM=null;
			
			if(pDbMigrationType.trim().equalsIgnoreCase(WebConstant.DB_MIGRATION_TYPE_SYBASE_TO_DB2)){
				//JOptionPane.showMessageDialog(null, pDbMigrationType);
				lPatternMatchCountMap = getPatternMatchCountHashMap(
						pDbMigrationType, lConnection);
				 lSrcTargtPatternMatchHM = getSourceTargetPatternMatchHashMap(
						pDbMigrationType, lConnection);
			}else{
				setMatchMapForOtherDBTypes(pDbMigrationType);
				 lPatternMatchCountMap = lPatternMatchCountMapOtherDB;
				 lSrcTargtPatternMatchHM = lSrcTargtPatternMatchHMOtherDB;
			}

			//get the project details hash map
			InventoryAnalyticsDAO lInventoryAnalyticsDAO = new InventoryAnalyticsDAO();			
			HashMap lProjectDetailsMap =  lInventoryAnalyticsDAO.getDBConnectionDetails(pProjectId);
			List lSPNameList = ToolsUtil.getFileNamesFromFolderUpperCase(new File(ToolsUtil.replaceNull((String)lProjectDetailsMap.get("SOURCE_PATH"))),new ArrayList());
			
			
			// Getting pattern data hashmap
			HashMap lSybasePatternDataMap = getPatternDataMap(pDbMigrationType,
					lConnection, pRunIdSybase,lSPNameList); //need to add condition
			
			// System.out.println("Sybase::->"+lSybasePatternDataMap.toString());
			 
			HashMap lDb2PatternDataMap = getPatternDataMap(pDbMigrationType,
					lConnection, pRunIdDb2,lSPNameList); //need to add condition
			
			//System.out.println("DB2::->"+lDb2PatternDataMap.toString());
			
			
			lInsertVarUsagePreparedStatement=lConnection.prepareStatement(ToolConstant.INSERT_VARIABLES_INTO_VARIABLE_USAGE_TABLE);

			// Creating Source and Target Lists to populate pattern for a single
			// procedure at a time.
			List lPatternDataListSybase = new ArrayList();
			List lPatternDataListDb2 = new ArrayList();
			// getting the compare seq no
			 //lCompareSeq = getCompareSeq(lConnection);
			lCompareSeq = pProjectId;

			// insert into main table
			insertCompareMain(lConnection, lCompareSeq, pDbMigrationType,
					pRunIdSybase, pRunIdDb2);

			// get the cursor details
			lSourceCursorDetailsList = getCursorDetailsList(pRunIdSybase,lSPNameList,
					lConnection); //need to add condition
			// System.out.println(":::::lSourceCursorDetailsList size:::::"+lSourceCursorDetailsList.size());

			System.out.println("lSybasePatternDataMap Count"
					+ lSybasePatternDataMap.size());

			// Comparing One by one procedure
			Iterator iterator = lSybasePatternDataMap.keySet().iterator();
			int lProcCount = 1;
			while (iterator.hasNext()) {
				String lProcedureName = iterator.next().toString().trim()
						.toUpperCase();
				if (lDb2PatternDataMap.containsKey(lProcedureName)) {
					lPatternDataListSybase = (List) lSybasePatternDataMap.get((String) lProcedureName);
					lPatternDataListDb2 = (List) lDb2PatternDataMap.get((String) lProcedureName);
					System.out.println(":::->Proc Name: " + lProcedureName+ " Proc Count:->" + lProcCount);
					
 				   //Update Status to Front End - Start
 				   lCurState="Source to Target Mapping";
 				   lStausMsg="File Name::->"+lProcedureName;
 				   ToolsUtil.prepareInsertStatusMsg(pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
 				   //Update Status to Front End - End
					
					// Invoking Method to Compare
					lOrderCount = 0;
					if(pDbMigrationType.trim().equalsIgnoreCase(WebConstant.DB_MIGRATION_TYPE_SYBASE_TO_DB2)){
						
						compareTwoProcs(lPatternDataListSybase,lPatternDataListDb2, lPatternMatchCountMap,lSrcTargtPatternMatchHM, lCompareSeq);
						compareDeclareCursorPattern(lPatternDataListSybase,	lPatternDataListDb2, lPatternMatchCountMap,	lCompareSeq);// Comparing Cursor Patterns
						compareDeclareVariables(lPatternDataListSybase,lPatternDataListDb2, lCompareSeq); // Comparing Declare Variable Patterns
					}
					
					else{
						compareTwoProcsForOtherDBType(lPatternDataListSybase,lPatternDataListDb2, lPatternMatchCountMap,lSrcTargtPatternMatchHM, lCompareSeq);
					}
					

				} else {
					//Update Status to Front End - Start
					lCurState="Error";
					lStausMsg=lProcedureName.toLowerCase()+" Error::->Procedure not found in Target..<br/> Please ensure the source and target file names should be same...";
					ToolsUtil.prepareInsertStatusMsg(pProjectId, ToolConstant.CREATED_BY, lCurState,lStausMsg, "Error",new Timestamp(System.currentTimeMillis()),lConnection);
					//Update Status to Front End - End
					System.out.println("Procedure not found in Target Hashmap");
				}
				lProcCount++;
			}
			lConnection.commit();
			lConnection.setAutoCommit(true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// close the connection and the result set
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			DBConnectionManager.closeConnection(lConnection);
		}

		return lCompareSeq;
	}
	public void setMatchMapForOtherDBTypes(String pDbMigrationType){
		 lPatternMatchCountMapOtherDB=new HashMap();
		 lSrcTargtPatternMatchHMOtherDB=new HashMap();
		try {
			lPreparedStatement=lConnection.prepareStatement(ToolConstant.GET_PATTERNS_FOR_DBTYPE);
			lPreparedStatement.setString(1, pDbMigrationType);
			lResultSet=lPreparedStatement.executeQuery();
			if(lResultSet!=null){
				while(lResultSet.next()){
					lPatternMatchCountMapOtherDB.put(ToolsUtil
							.replaceNull(lResultSet.getString("PATTERN_ID")), "1");
					lSrcTargtPatternMatchHMOtherDB.put(ToolsUtil.replaceNull(lResultSet.getString("PATTERN_ID")), 
							ToolsUtil.replaceNull(lResultSet.getString("PATTERN_ID")));
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void compareTwoProcsSybaseToOracle(List pPatternDataListSybase,
			List pPatternDataListOracle, HashMap lPatternMatchCountMap,
			HashMap lSrcTargtPatternMatchHM, String pCompareSeq) {
		List lPatternDataListSybase = pPatternDataListSybase;
		List lPatternDataListOracle = pPatternDataListOracle;
		PatternDataDTO lPatternDataDTOSybase = new PatternDataDTO();
		PatternDataDTO lPatternDataDTOOracle = new PatternDataDTO();
		PatternDataDTO lTmpPatternDataDTO = new PatternDataDTO();
		List lSetVariableList=new ArrayList();
		PatternDataDTO lTempPatternDataDTOSybase = null;
		try {
			
			int lCount = 0;
			int lOracleListCount = 0;
			int lJCount = 0;
			lSetVarList=new ArrayList();
			List lTrgtPatternMatchLLst = new ArrayList();
			ComparedStatementDTO lComparedStatementDTO = null;
			PreparedStatement pPreparedStatement = null;
			pPreparedStatement = lConnection
					.prepareStatement(ToolConstant.INSERT_COMPARE_FORMED_STMT);
			int lSetVarCount=0;
			if (lPatternDataListSybase != null) 
			{
				for (int i = 0; i < lPatternDataListSybase.size(); i++) 
				{

					lPatternDataDTOSybase = (PatternDataDTO) lPatternDataListSybase
							.get(i);
					
					

					 System.out.println("---------------"+lPatternDataDTOSybase.getFormedStatement()+" Pat ID:"+lPatternDataDTOSybase.getPatternId()+"------");
					
					// ignore the record if pattern is declare cursor
					if (ToolConstant.PATTERN_ID_DECLARE_CURSOR_SYBASEORACLE.equalsIgnoreCase(lPatternDataDTOSybase.getPatternId().trim())
							|| ToolConstant.PATTERN_ID_DECLARE_VARIABLE_SYBASEORACLE.equalsIgnoreCase(lPatternDataDTOSybase.getPatternId().trim())||ToolConstant.PATTERN_ID_OPEN_CURSOR_SYBASEORACLE.equalsIgnoreCase(lPatternDataDTOSybase.getPatternId().trim())) {
						continue;
						
					}

					if (lPatternMatchCountMap.containsKey(lPatternDataDTOSybase
							.getPatternId().trim())) {
						if ((String) lPatternMatchCountMap
								.get(lPatternDataDTOSybase.getPatternId()
										.trim()) != null) {
							lCount = Integer
									.parseInt((String) lPatternMatchCountMap
											.get(lPatternDataDTOSybase
													.getPatternId().trim()));
							
							//Calculating the count value Dynamically for Select Other Pattern
							if(lPatternDataDTOSybase.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_SELECT_OTHER_SYBASEORACLE)){
																
								lSetVariableList=getSplitStatements(lPatternDataDTOSybase.getFormedStatement(),"SELECT ");								
								lCount=getVariableCount(lPatternDataDTOSybase.getFormedStatement().trim(),"SELECT ");
								lSetVarCount=0;
							}
							

							if (lCount <= 0) {
								pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, null, pCompareSeq, "N");
						//for insert - start		
						
						
							} else if (lCount > 0) {

								lOracleListCount = lJCount;
								for (int j = lJCount; j < lOracleListCount
										+ lCount; j++) {

									// System.out.println("i::-> "+i+"  :::: j-> "+j+" lJCount::-> "+lJCount);

									lPatternDataDTOOracle = (PatternDataDTO) lPatternDataListOracle.get(j);
									if(lPatternDataDTOOracle.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_SET_VARIABLE_SYBASEORACLE)){
										Pattern lSetPattern=Pattern.compile("(?i)(\\bSET\\b\\s+SWV_|\\bSET\\b\\s+l_)");
										if(lSetPattern.matcher(lPatternDataDTOOracle.getFormedStatement().toUpperCase().trim()).find()){
											//New Set Found in Target which is no where related to Source so skipping pass.
											if(chkForExtraSet(lPatternDataDTOOracle.getFormedStatement().trim())){
												lOracleListCount=lOracleListCount+1;
												lJCount++;
												lTempPatternDataDTOSybase = new PatternDataDTO();
												lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO) lPatternDataListSybase.get(i)).getStatementNo());
												lTempPatternDataDTOSybase.setProcedureName(((PatternDataDTO) lPatternDataListSybase.get(i)).getProcedureName());
												pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lTempPatternDataDTOSybase, lPatternDataDTOOracle, pCompareSeq, "N");
												continue;
												//Insert
											}else{
												if(lPatternDataDTOSybase.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_SELECT_OTHER_SYBASEORACLE)){
													lPatternDataDTOSybase.setFormedStatement((String)lSetVariableList.get(lSetVarCount++));
													
												}
												
												//lSetVarCount++;
											}
										}else{
											if(lPatternDataDTOSybase.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_SELECT_OTHER_SYBASEORACLE)){
												lPatternDataDTOSybase.setFormedStatement((String)lSetVariableList.get(lSetVarCount++));
											}
											//lPatternDataDTOSybase.setFormedStatement((String)lSetVariableList.get(lSetVarCount++));
											//lSetVarCount++;
										}
									}
									
									
									if(lPatternDataDTOOracle.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_OPEN_CURSOR_SYBASEORACLE)){
										Pattern lOpenPattern=Pattern.compile("\\bOPEN\\b\\s+(SWV_|C_|\\W)");
									//	Pattern lOpenPattern2=Pattern.compile("\\bOPEN\\b\\s+(SWV_|C_|cv_)+\\bFOR");
										if((lOpenPattern.matcher(lPatternDataDTOOracle.getFormedStatement().toUpperCase().trim()).find())){
											//New Open Found in Target which no where related to Source so skipping pass.											
											lOracleListCount=lOracleListCount+1;
											lJCount++;
											lTempPatternDataDTOSybase = new PatternDataDTO();
											lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO) lPatternDataListSybase.get(i)).getStatementNo());
											lTempPatternDataDTOSybase.setProcedureName(((PatternDataDTO) lPatternDataListSybase.get(i)).getProcedureName());
											pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lTempPatternDataDTOSybase, lPatternDataDTOOracle, pCompareSeq, "N");
											continue;
											//Insert
											
										}
									}
									if(lPatternDataDTOOracle.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_VARIABLE_SYBASEORACLE) ){
										lOracleListCount=lOracleListCount+1;
										lJCount++;
										continue;
									}
									/*if(lPatternDataDTOOracle.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_CONTINUE_HANDLER)){
										lOracleListCount=lOracleListCount+1;
										lJCount++;
										lTempPatternDataDTOSybase = new PatternDataDTO();
										lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO) lPatternDataListSybase.get(i)).getStatementNo());
										lTempPatternDataDTOSybase.setProcedureName(((PatternDataDTO) lPatternDataListSybase.get(i)).getProcedureName());
										pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lTempPatternDataDTOSybase, lPatternDataDTOOracle, pCompareSeq, "N");
										continue;
										//Insert
									}*/
									

									// System.out.println("1DB2:-> "+((PatternDataDTO)lPatternDataListDb2.get(j)).getFormedStatement());
									// ignore the record if pattern is declare
									// cursor
									if (ToolConstant.PATTERN_ID_DECLARE_CURSOR_SYBASEORACLE
											.equalsIgnoreCase(lPatternDataDTOOracle
													.getPatternId())) {
									
										if (checkInputCursorName(
												((lPatternDataDTOOracle)
														.getProcedureName()),
												(lPatternDataDTOOracle)
														.getFormedStatement())) {
											// lJCount++;
											lOracleListCount++;
											// continue;
										} else {
											if (lSrcTargtPatternMatchHM
													.containsKey(lPatternDataDTOSybase
															.getPatternId()
															.trim())) {
												// System.out.println("Trgt pattern_id:"+lPatternDataDTODb2.getPatternId()+"Statement no"+lPatternDataDTODb2.getStatementNo());
												// System.out.println("1DB2:-> "+lPatternDataDTODb2.getFormedStatement());
												lTrgtPatternMatchLLst = (List) lSrcTargtPatternMatchHM
														.get(lPatternDataDTOSybase
																.getPatternId()
																.trim());
												

												if (lTrgtPatternMatchLLst
														.contains(lPatternDataDTOOracle
																.getPatternId()
																.trim())) {
													// System.out.println("Trgt pattern_id:"+lPatternDataDTODb2.getPatternId()+"Statement no"+lPatternDataDTODb2.getStatementNo());
													// System.out.println("1DB2:-> "+lPatternDataDTODb2.getFormedStatement());

													
													
													pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lPatternDataDTOOracle, pCompareSeq, "Y");

													// /
												} else {
													// System.out.println("1Trgt pattern_id:"+lPatternDataDTODb2.getPatternId()+"Statement no"+lPatternDataDTODb2.getStatementNo());
													// System.out.println("1DB2:-> "+lPatternDataDTODb2.getFormedStatement());
													// /
												
													pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lPatternDataDTOOracle, pCompareSeq, "N");
													pPreparedStatement
															.executeBatch();
													// /
													// System.out.println("11Pattern Mismatch Error");
													return;
												}

											} else {
												// System.out.println("2Inavlid Pattern Captured Pattern Id:: "+lPatternDataDTOSybase.getPatternId()+
												// " at Statement No: "+lPatternDataDTOSybase.getStatementNo());
												return;
											}

										}

									} else {
										if (lSrcTargtPatternMatchHM
												.containsKey(lPatternDataDTOSybase
														.getPatternId().trim())) {
											 System.out.println("Trgt pattern_id:"+lPatternDataDTOOracle.getPatternId()+"Statement no"+lPatternDataDTOOracle.getStatementNo());
											 System.out.println("2DB2:-> "+lPatternDataDTOOracle.getFormedStatement());
											lTrgtPatternMatchLLst = (List) lSrcTargtPatternMatchHM
													.get(lPatternDataDTOSybase
															.getPatternId()
															.trim());
											
											if (lTrgtPatternMatchLLst
													.contains(lPatternDataDTOOracle
															.getPatternId()
															.trim())) {
												 System.out.println("123Trgt pattern_id:"+lPatternDataDTOOracle.getPatternId()+"Statement no"+lPatternDataDTOOracle.getStatementNo());
												 System.out.println("1232DB2:-> "+lPatternDataDTOOracle.getFormedStatement());
												// /
												//Getting the Input Parameters from create proc.
												
												System.out.println("::inside if condition for sp param cal- pat id::::"+lPatternDataDTOSybase.getPatternId().trim());
												System.out
														.println("PATTERN ID "+lPatternDataDTOSybase.getPatternId());		
												if(lPatternDataDTOSybase.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_CREATE_DB_OBJECT_SYBASEORACLE)){
													
													System.out
															.println("::inside if condition for sp param cal::::");
													getSPInputVariables(lPatternDataDTOSybase.getFormedStatement(), lPatternDataDTOOracle.getFormedStatement(),pCompareSeq,lPatternDataDTOSybase.getProcedureName());
												}
												
												pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lPatternDataDTOOracle, pCompareSeq, "Y");

												// /

											} else {
												// System.out.println("2 Trgt pattern_id:"+lPatternDataDTODb2.getPatternId()+"Statement no"+lPatternDataDTODb2.getStatementNo());
												// System.out.println("2DB2:-> "+lPatternDataDTODb2.getFormedStatement());
												// /
												//if(lPatternDataDTODb2.getFormedStatement().contains("--TCS MODLOG"))
												//{
												//	pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "PATTERN");
												//}
												pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lPatternDataDTOOracle, pCompareSeq, "N");
												pPreparedStatement.executeBatch();

												// /
												// System.out.println("2Pattern Mismatch Error");
												return;
											}

										} else {
											System.out
													.println("Invalid Pattern Captured Pattern Id:: "
															+ lPatternDataDTOSybase
																	.getPatternId()
															+ " at Statement No: "
															+ lPatternDataDTOSybase
																	.getStatementNo());
											pPreparedStatement.executeBatch();
											return;
										}
										// System.out.println("Proc Name: "+(lPatternDataDTODb2).getProcedureName());
										// System.out.println("1Sybase :-> "+((PatternDataDTO)lPatternDataListSybase.get(i)).getFormedStatement());
										// System.out.println("1DB2:-> "+lPatternDataDTODb2.getFormedStatement());
									}

									lJCount++;

								}

							} else {
								System.out
										.println("Count is -1"
												+ ((PatternDataDTO) lPatternDataListSybase
														.get(i)).getPatternId()+ ((PatternDataDTO) lPatternDataListSybase
																.get(i)).getFormedStatement());
							}

							// System.out.print(((PatternDataDTO)lPatternDataListSybase.get(i)).getFormedStatement()+"  ::::  ");
							// System.out.println("---------------");

						}
					}

					// System.out.println("---------------");
				}
				//Inserting the rest of target patterns after comparison
				for (int j = lJCount; j <lPatternDataListOracle.size() ; j++) {
					lPatternDataDTOOracle = (PatternDataDTO) lPatternDataListOracle.get(j);
					/*lComparedStatementDTO = new ComparedStatementDTO();
					lOrderCount++;
					lComparedStatementDTO
							.setCompareSeq(pCompareSeq);
					lComparedStatementDTO
							.setOrderNo(lOrderCount+"");
					lComparedStatementDTO
							.setProcName(lPatternDataDTODb2
									.getProcedureName());
					lComparedStatementDTO
							.setSourceStatementNo("0123");
					lComparedStatementDTO
							.setSourcePatternId("");
					lComparedStatementDTO
							.setSourceFormedStatement("");
					lComparedStatementDTO
							.setTargetStatementNo(lPatternDataDTODb2
									.getStatementNo());
					lComparedStatementDTO
							.setTargetPatternId(lPatternDataDTODb2
									.getPatternId());
					lComparedStatementDTO
							.setTargetFormedStatement(lPatternDataDTODb2
									.getFormedStatement());
					lComparedStatementDTO
							.setMatchedYN("N");
					
					pPreparedStatement = insertCompareStatement(pPreparedStatement,	lConnection,lComparedStatementDTO);*/
				lTempPatternDataDTOSybase = new PatternDataDTO();
					lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO) lPatternDataListSybase.get(lPatternDataListSybase.size()-1)).getStatementNo());
					lTempPatternDataDTOSybase.setProcedureName(((PatternDataDTO) lPatternDataListSybase.get(lPatternDataListSybase.size()-1)).getProcedureName());
					pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lTempPatternDataDTOSybase, lPatternDataDTOOracle, pCompareSeq, "N");

				}
				
				
				// Getting Declare Cursor Match List.
				// System.out.println("::::Declare Cursor List Method Called::::");
				// lDeclareCursorMatchList=compareDeclareCursorPattern(lPatternDataListSybase
				// ,lPatternDataListDb2,lPatternMatchCountMap);
			}

			// ///////////////
			pPreparedStatement.executeBatch();

		} catch (Exception pException) {
			pException.printStackTrace();
		} finally {
			DBConnectionManager.closeConnection(lPreparedStatement, null);
		}

	}
	

	public void compareTwoProcs(List pPatternDataListSybase,
			List pPatternDataListDb2, HashMap lPatternMatchCountMap,
			HashMap lSrcTargtPatternMatchHM, String pCompareSeq) {

		List lPatternDataListSybase = pPatternDataListSybase;
		List lPatternDataListDb2 = pPatternDataListDb2;
		PatternDataDTO lPatternDataDTOSybase = new PatternDataDTO();
		PatternDataDTO lPatternDataDTODb2 = new PatternDataDTO();
		// System.out.println("lPatternDataListSybase size"+lPatternDataListSybase.size());
		// System.out.println("lPatternDataListDb2 size"+lPatternDataListDb2.size());
		PatternDataDTO lTmpPatternDataDTO = new PatternDataDTO();
		List lSetVariableList=new ArrayList();
		PatternDataDTO lTempPatternDataDTOSybase = null;
		try {
			
			int lCount = 0;
			int lDB2ListCount = 0;
			int lJCount = 0;
			lSetVarList=new ArrayList();
			List lTrgtPatternMatchLLst = new ArrayList();
			ComparedStatementDTO lComparedStatementDTO = null;
			PreparedStatement pPreparedStatement = null;
			pPreparedStatement = lConnection.prepareStatement(ToolConstant.INSERT_COMPARE_FORMED_STMT);
			int lSetVarCount=0;
			if (lPatternDataListSybase != null) 
			{
				for (int i = 0; i < lPatternDataListSybase.size(); i++) 
				{

					lPatternDataDTOSybase = (PatternDataDTO) lPatternDataListSybase.get(i);
					
					

					 System.out.println("---------------"+lPatternDataDTOSybase.getFormedStatement()+" Pat ID:"+lPatternDataDTOSybase.getPatternId()+"------");
					// System.out.println(lPatternDataDTOSybase.getFormedStatement()+"  ::::  ");

					// ignore the record if pattern is declare cursor
					if (ToolConstant.PATTERN_ID_DECLARE_CURSOR.equalsIgnoreCase(lPatternDataDTOSybase.getPatternId().trim())
							|| ToolConstant.PATTERN_ID_DECLARE_VARIABLE.equalsIgnoreCase(lPatternDataDTOSybase.getPatternId().trim())) {
						continue;
						
					}

					if (lPatternMatchCountMap.containsKey(lPatternDataDTOSybase
							.getPatternId().trim())) {
						if ((String) lPatternMatchCountMap
								.get(lPatternDataDTOSybase.getPatternId()
										.trim()) != null) {
							lCount = Integer
									.parseInt((String) lPatternMatchCountMap
											.get(lPatternDataDTOSybase
													.getPatternId().trim()));
							
							//Calculating the count value Dynamically for Select Other Pattern
							if(lPatternDataDTOSybase.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_SELECT_OTHER)){
																
								lSetVariableList=getSplitStatements(lPatternDataDTOSybase.getFormedStatement(),"SELECT ");								
								lCount=getVariableCount(lPatternDataDTOSybase.getFormedStatement().trim(),"SELECT ");
								lSetVarCount=0;
							}
							

							if ((lCount <= 0) /*&&(lPatternDataDTOSybase.getPatternId()
									.trim().equals("PAT_C_85"))*/) {
								// System.out.println(((PatternDataDTO)lPatternDataListSybase.get(i)).getFormedStatement()+"  ::::  ");
								// System.out.println("No DB2 Pattern For This \n");
								//for insert 30.11.2011- start
								/*lOrderCount++;
								lComparedStatementDTO
								.setCompareSeq(pCompareSeq);
						lComparedStatementDTO
								.setOrderNo(lOrderCount+"");
						lComparedStatementDTO
								.setProcName(lPatternDataDTOSybase
										.getProcedureName());
						lComparedStatementDTO
								.setSourceStatementNo(lPatternDataDTOSybase
										.getStatementNo());
						lComparedStatementDTO
								.setSourcePatternId(lPatternDataDTOSybase
										.getPatternId());
						lComparedStatementDTO
								.setSourceFormedStatement(lPatternDataDTOSybase
										.getFormedStatement());
						lComparedStatementDTO
								.setTargetStatementNo("0");
						lComparedStatementDTO
								.setTargetPatternId("");
						lComparedStatementDTO
								.setTargetFormedStatement("");
						lComparedStatementDTO
								.setMatchedYN("N");
						pPreparedStatement = insertCompareStatement(
								pPreparedStatement,
								lConnection,
								lComparedStatementDTO);*/
						
								pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, null, pCompareSeq, "N");
						//for insert - start		
						
						
							} else if (lCount > 0) {

								lDB2ListCount = lJCount;
								for (int j = lJCount; j < lDB2ListCount
										+ lCount; j++) {

									// System.out.println("i::-> "+i+"  :::: j-> "+j+" lJCount::-> "+lJCount);

									lPatternDataDTODb2 = (PatternDataDTO) lPatternDataListDb2.get(j);
									if(lPatternDataDTODb2.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_SET_VARIABLE)){
										Pattern lSetPattern=Pattern.compile("(?i)(\\bSET\\b\\s+SWV_|\\bSET\\b\\s+l_)");
										if(lSetPattern.matcher(lPatternDataDTODb2.getFormedStatement().toUpperCase().trim()).find()){
											//New Set Found in Target which is no where related to Source so skipping pass.
											if(chkForExtraSet(lPatternDataDTODb2.getFormedStatement().trim())){
												lDB2ListCount=lDB2ListCount+1;
												lJCount++;
												lTempPatternDataDTOSybase = new PatternDataDTO();
												lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO) lPatternDataListSybase.get(i)).getStatementNo());
												lTempPatternDataDTOSybase.setProcedureName(((PatternDataDTO) lPatternDataListSybase.get(i)).getProcedureName());
												pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lTempPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "N");
												continue;
												//Insert
											}else{
												if(lPatternDataDTOSybase.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_SELECT_OTHER)){
													lPatternDataDTOSybase.setFormedStatement((String)lSetVariableList.get(lSetVarCount++));
													
												}
												
												//lSetVarCount++;
											}
										}else{
											if(lPatternDataDTOSybase.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_SELECT_OTHER)){
												lPatternDataDTOSybase.setFormedStatement((String)lSetVariableList.get(lSetVarCount++));
											}
											//lPatternDataDTOSybase.setFormedStatement((String)lSetVariableList.get(lSetVarCount++));
											//lSetVarCount++;
										}
									}
									
									
									if(lPatternDataDTODb2.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_OPEN_CURSOR)){
										Pattern lOpenPattern=Pattern.compile("\\bOPEN\\b\\s+(SWV_|C_|\\W)");
										if(lOpenPattern.matcher(lPatternDataDTODb2.getFormedStatement().toUpperCase().trim()).find()){
											//New Open Found in Target which no where related to Source so skipping pass.											
											lDB2ListCount=lDB2ListCount+1;
											lJCount++;
											lTempPatternDataDTOSybase = new PatternDataDTO();
											lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO) lPatternDataListSybase.get(i)).getStatementNo());
											lTempPatternDataDTOSybase.setProcedureName(((PatternDataDTO) lPatternDataListSybase.get(i)).getProcedureName());
											pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lTempPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "N");
											continue;
											//Insert
											
										}
									}
									if(lPatternDataDTODb2.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_VARIABLE) ){
										lDB2ListCount=lDB2ListCount+1;
										lJCount++;
										continue;
									}
									if(lPatternDataDTODb2.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_CONTINUE_HANDLER)){
										lDB2ListCount=lDB2ListCount+1;
										lJCount++;
										lTempPatternDataDTOSybase = new PatternDataDTO();
										lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO) lPatternDataListSybase.get(i)).getStatementNo());
										lTempPatternDataDTOSybase.setProcedureName(((PatternDataDTO) lPatternDataListSybase.get(i)).getProcedureName());
										pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lTempPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "N");
										continue;
										//Insert
									}
									

									// System.out.println("1DB2:-> "+((PatternDataDTO)lPatternDataListDb2.get(j)).getFormedStatement());
									// ignore the record if pattern is declare
									// cursor
									if (ToolConstant.PATTERN_ID_DECLARE_CURSOR
											.equalsIgnoreCase(lPatternDataDTODb2
													.getPatternId())) {
									
										if (checkInputCursorName(
												((lPatternDataDTODb2)
														.getProcedureName()),
												(lPatternDataDTODb2)
														.getFormedStatement())) {
											// lJCount++;
											lDB2ListCount++;
											// continue;
										} else {
											if (lSrcTargtPatternMatchHM
													.containsKey(lPatternDataDTOSybase
															.getPatternId()
															.trim())) {
												// System.out.println("Trgt pattern_id:"+lPatternDataDTODb2.getPatternId()+"Statement no"+lPatternDataDTODb2.getStatementNo());
												// System.out.println("1DB2:-> "+lPatternDataDTODb2.getFormedStatement());
												lTrgtPatternMatchLLst = (List) lSrcTargtPatternMatchHM
														.get(lPatternDataDTOSybase
																.getPatternId()
																.trim());
												

												if (lTrgtPatternMatchLLst
														.contains(lPatternDataDTODb2
																.getPatternId()
																.trim())) {
													// System.out.println("Trgt pattern_id:"+lPatternDataDTODb2.getPatternId()+"Statement no"+lPatternDataDTODb2.getStatementNo());
													// System.out.println("1DB2:-> "+lPatternDataDTODb2.getFormedStatement());

													
													
													pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "Y");

													// /
												} else {
													// System.out.println("1Trgt pattern_id:"+lPatternDataDTODb2.getPatternId()+"Statement no"+lPatternDataDTODb2.getStatementNo());
													// System.out.println("1DB2:-> "+lPatternDataDTODb2.getFormedStatement());
													// /
												
													pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "N");
													pPreparedStatement
															.executeBatch();
													// /
													// System.out.println("11Pattern Mismatch Error");
													return;
												}

											} else {
												// System.out.println("2Inavlid Pattern Captured Pattern Id:: "+lPatternDataDTOSybase.getPatternId()+
												// " at Statement No: "+lPatternDataDTOSybase.getStatementNo());
												return;
											}

										}

									} else {
										if (lSrcTargtPatternMatchHM
												.containsKey(lPatternDataDTOSybase
														.getPatternId().trim())) {
											 System.out.println("Trgt pattern_id:"+lPatternDataDTODb2.getPatternId()+"Statement no"+lPatternDataDTODb2.getStatementNo());
											 System.out.println("2DB2:-> "+lPatternDataDTODb2.getFormedStatement());
											lTrgtPatternMatchLLst = (List) lSrcTargtPatternMatchHM
													.get(lPatternDataDTOSybase
															.getPatternId()
															.trim());
											
											if (lTrgtPatternMatchLLst
													.contains(lPatternDataDTODb2
															.getPatternId()
															.trim())) {
												 System.out.println("123Trgt pattern_id:"+lPatternDataDTODb2.getPatternId()+"Statement no"+lPatternDataDTODb2.getStatementNo());
												 System.out.println("1232DB2:-> "+lPatternDataDTODb2.getFormedStatement());
												// /
												//Getting the Input Parameters from create proc.
												
												System.out.println("::inside if condition for sp param cal- pat id::::"+lPatternDataDTOSybase.getPatternId().trim());
														
												if(lPatternDataDTOSybase.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_CREATE_DB_OBJECT)){
													
													System.out
															.println("::inside if condition for sp param cal::::");
													getSPInputVariables(lPatternDataDTOSybase.getFormedStatement(), lPatternDataDTODb2.getFormedStatement(),pCompareSeq,lPatternDataDTOSybase.getProcedureName());
												}
												
												pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "Y");

												// /

											} else {
												// System.out.println("2 Trgt pattern_id:"+lPatternDataDTODb2.getPatternId()+"Statement no"+lPatternDataDTODb2.getStatementNo());
												// System.out.println("2DB2:-> "+lPatternDataDTODb2.getFormedStatement());
												// /
												//if(lPatternDataDTODb2.getFormedStatement().contains("--TCS MODLOG"))
												//{
												//	pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "PATTERN");
												//}
												pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "N");
												pPreparedStatement.executeBatch();

												// /
												// System.out.println("2Pattern Mismatch Error");
												return;
											}

										} else {
											System.out
													.println("2Inavlid Pattern Captured Pattern Id:: "
															+ lPatternDataDTOSybase
																	.getPatternId()
															+ " at Statement No: "
															+ lPatternDataDTOSybase
																	.getStatementNo());
											pPreparedStatement.executeBatch();
											return;
										}
										// System.out.println("Proc Name: "+(lPatternDataDTODb2).getProcedureName());
										// System.out.println("1Sybase :-> "+((PatternDataDTO)lPatternDataListSybase.get(i)).getFormedStatement());
										// System.out.println("1DB2:-> "+lPatternDataDTODb2.getFormedStatement());
									}

									lJCount++;

								}

							} else {
								System.out
										.println("Count is -1"
												+ ((PatternDataDTO) lPatternDataListSybase
														.get(i)).getPatternId()+ ((PatternDataDTO) lPatternDataListSybase
																.get(i)).getFormedStatement());
							}

							// System.out.print(((PatternDataDTO)lPatternDataListSybase.get(i)).getFormedStatement()+"  ::::  ");
							// System.out.println("---------------");

						}
					}

					// System.out.println("---------------");
				}
				//Inserting the rest of target patterns after comparison
				for (int j = lJCount; j <lPatternDataListDb2.size() ; j++) {
					lPatternDataDTODb2 = (PatternDataDTO) lPatternDataListDb2.get(j);
					/*lComparedStatementDTO = new ComparedStatementDTO();
					lOrderCount++;
					lComparedStatementDTO
							.setCompareSeq(pCompareSeq);
					lComparedStatementDTO
							.setOrderNo(lOrderCount+"");
					lComparedStatementDTO
							.setProcName(lPatternDataDTODb2
									.getProcedureName());
					lComparedStatementDTO
							.setSourceStatementNo("0123");
					lComparedStatementDTO
							.setSourcePatternId("");
					lComparedStatementDTO
							.setSourceFormedStatement("");
					lComparedStatementDTO
							.setTargetStatementNo(lPatternDataDTODb2
									.getStatementNo());
					lComparedStatementDTO
							.setTargetPatternId(lPatternDataDTODb2
									.getPatternId());
					lComparedStatementDTO
							.setTargetFormedStatement(lPatternDataDTODb2
									.getFormedStatement());
					lComparedStatementDTO
							.setMatchedYN("N");
					
					pPreparedStatement = insertCompareStatement(pPreparedStatement,	lConnection,lComparedStatementDTO);*/
					lTempPatternDataDTOSybase = new PatternDataDTO();
					lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO) lPatternDataListSybase.get(lPatternDataListSybase.size()-1)).getStatementNo());
					lTempPatternDataDTOSybase.setProcedureName(((PatternDataDTO) lPatternDataListSybase.get(lPatternDataListSybase.size()-1)).getProcedureName());
					pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lTempPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "N");

				}
				
				
				// Getting Declare Cursor Match List.
				// System.out.println("::::Declare Cursor List Method Called::::");
				// lDeclareCursorMatchList=compareDeclareCursorPattern(lPatternDataListSybase
				// ,lPatternDataListDb2,lPatternMatchCountMap);
			}

			// ///////////////
			pPreparedStatement.executeBatch();

		} catch (Exception pException) {
			pException.printStackTrace();
		} finally {
			DBConnectionManager.closeConnection(lPreparedStatement, null);
		}

	}
	public void compareDeclareVariablesSybaseToOracle(List pPatternDataListSybase,List pPatternDataListTarget, String pCompareSeq){


		List lPatternDataListSybase = pPatternDataListSybase;
		List lPatternDataListTarget = pPatternDataListTarget;
		List lDeclareVariableSybaseList=new ArrayList();
		List lDeclareVariableSybaseListtemp=new ArrayList();
		List lDeclareVariableTargetList=new ArrayList();
		PatternDataDTO lPatternDataDTOSybase = new PatternDataDTO();
		PatternDataDTO lPatternDataDTOTarget = new PatternDataDTO();
		List lDeclareVarList=null;
		int lCount=0;
		System.out.println();
		
		///Preparing Source Declare Variable Pattern List
		for (int i = 0; i < pPatternDataListSybase.size(); i++) {
			lPatternDataDTOSybase = (PatternDataDTO) pPatternDataListSybase	.get(i);
			if(lPatternDataDTOTarget.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_VARIABLE_SYBASEORACLE)){
				
				///***********New Code for Multiple Variable Declarations***********//
				lDeclareVarList=getSplitStatements(lPatternDataDTOSybase.getFormedStatement(),"DECLARE ");
				//lCount =getVariableCount(lPatternDataDTOSybase.getFormedStatement(), "DECLARE ");		
				//System.out.println("::::lDeclareVarList:::"+lDeclareVarList.toString());
				
				for (int j = 0; j < lDeclareVarList.size(); j++) {
					PatternDataDTO lTmpPatternDataDTOSybase=new PatternDataDTO();
					lTmpPatternDataDTOSybase.setPatternId(lPatternDataDTOSybase.getPatternId());
					lTmpPatternDataDTOSybase.setPatternDesc(lPatternDataDTOSybase.getPatternDesc());
					lTmpPatternDataDTOSybase.setStatementNo(lPatternDataDTOSybase.getStatementNo());
					lTmpPatternDataDTOSybase.setProcedureName(lPatternDataDTOSybase.getProcedureName());
					//lTmpPatternDataDTOSybase.setFormedStatement(lPatternDataDTOSybase.getFormedStatement());
					
					lTmpPatternDataDTOSybase.setFormedStatement((String)lDeclareVarList.get(j));
					
					lDeclareVariableSybaseList.add(lTmpPatternDataDTOSybase);
					//lDeclareVariableSybaseListtemp.add(lTmpPatternDataDTOSybase);
					//System.out.println("-1::lDeclareVariableSybaseList value::::::"+lTmpPatternDataDTOSybase.getFormedStatement());
				}
				
				
				///***********New Code for Multiple Variable Declarations***********//
				
				//lDeclareVariableSybaseList.add(lPatternDataDTOSybase);
			}
			
			
		}
		
		
		//System.out.println("-2::lDeclareVariableSybaseList value::::::"+lDeclareVariableSybaseListtemp.toString());
		/*for (int j = 0; j < lDeclareVariableSybaseList.size(); j++) {
			System.out.println("-2::lDeclareVariableSybaseList value::::::"+((PatternDataDTO)lDeclareVariableSybaseList.get(j)).getFormedStatement());
		}
		System.out.println("***********************************");*/
		// Preparing Target Declare Variable Pattern List
		for (int i = 0; i < pPatternDataListTarget.size(); i++) {
			lPatternDataDTOTarget = (PatternDataDTO) pPatternDataListTarget.get(i);
			if(lPatternDataDTOTarget.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_VARIABLE_SYBASEORACLE)){
				lDeclareVariableTargetList.add(lPatternDataDTOTarget);
			}
			
		}		
		/*System.out.println("Source Size::->>"+lDeclareVariableSybaseList.size());
		System.out.println("Target Size::->>"+lDeclareVariableDB2List.size());
		int lChkCount=0;
		for (int i = 0; i < lDeclareVariableSybaseList.size(); i++) {
			lChkCount+= getVariableCount(((PatternDataDTO)lDeclareVariableSybaseList.get(i)).getFormedStatement(), "DECLARE ");	
		}
		System.out.println("lChkCount::->"+lChkCount);
*/
		PatternDataDTO lTempPatternDataDTOSybase=null;
		PatternDataDTO lTempPatternDataDTOTarget=null;
		
		try {
			// /////////////////
			
			int lTargetListCount = 0;
			int lJCount = 0;			
			List lTrgtPatternMatchLLst = new ArrayList();
			ComparedStatementDTO lComparedStatementDTO = null;
			PreparedStatement pPreparedStatement = null;
			pPreparedStatement = lConnection
					.prepareStatement(ToolConstant.INSERT_COMPARE_FORMED_STMT);
			int lDeclareCount=0;
			
			if (lDeclareVariableSybaseList != null) {
				for (int i = 0; i < lDeclareVariableSybaseList.size(); i++) {

					lPatternDataDTOSybase = (PatternDataDTO) lDeclareVariableSybaseList.get(i);
					if(lPatternDataDTOSybase==null){
						continue;
					}
					//System.out.println("Source::->"+lPatternDataDTOSybase.getFormedStatement());
					//System.out.println("-2::lDeclareVariableSybaseList value::::::"+((PatternDataDTO)lDeclareVariableSybaseList.get(i+1)).getFormedStatement());
					

					// System.out.println("---------------"+lPatternDataDTOSybase.getStatementNo()+" Pat ID:"+lPatternDataDTOSybase.getPatternId()+"------");
					// System.out.println(lPatternDataDTOSybase.getFormedStatement()+"  ::::  ");

					
					/*lDeclareVarList=getSplitStatements(lPatternDataDTOSybase.getFormedStatement(),"DECLARE ");
					lCount =getVariableCount(lPatternDataDTOSybase.getFormedStatement(), "DECLARE ");							
					lDeclareCount=0;
					////////////////////	
					lDB2ListCount = lJCount;
					lPatternDataDTOSybase.setFormedStatement((String)lDeclareVarList.get(lDeclareCount++));
					*/
					for (int j =0 /*lJCount*/; j < lDeclareVariableTargetList.size() /*lDB2ListCount+ lCount*/; j++) {
						lPatternDataDTOTarget = (PatternDataDTO) lDeclareVariableTargetList.get(j);	
						if(lPatternDataDTOTarget==null){
							continue;
						}
						//System.out.println("i:->"+i+"::No:"+lPatternDataDTOSybase.getStatementNo()+" Pat ::->"+lPatternDataDTOSybase.getFormedStatement()+"------");
						//System.out.println("i:->"+j+"::No:"+lPatternDataDTODb2.getStatementNo()+" Pat ::->"+lPatternDataDTODb2.getFormedStatement()+"------");
											
						/*if(lPatternDataDTODb2.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_VARIABLE)){
							Pattern lOpenPattern=Pattern.compile("\\bDECLARE\\b\\s+V_");
							if(!lOpenPattern.matcher(lPatternDataDTODb2.getFormedStatement().toUpperCase().trim()).find()){
								//New Open Found in Target which no where related to Source so skipping pass.											
								lDB2ListCount=lDB2ListCount+1;
								lJCount++;
								lTempPatternDataDTOSybase = new PatternDataDTO();
								lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO) lDeclareVariableSybaseList.get(i)).getStatementNo());
								lTempPatternDataDTOSybase.setProcedureName(((PatternDataDTO) lDeclareVariableSybaseList.get(i)).getProcedureName());
								pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lTempPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "N");
								lDeclareVariableDB2List.remove(j);
								j=j-1;
								continue;
								
							}
						}*/
						
						//
						/*System.out.println("Source::->"+lPatternDataDTOSybase.getFormedStatement());
						System.out.println("Target::->"+lPatternDataDTODb2.getFormedStatement());
						System.out.println("----------------------------------");
						if(lPatternDataDTOSybase.getFormedStatement().contains("@DisabilityABSConcurrentInd")){
							System.out.println(lPatternDataDTODb2.getFormedStatement());
						}*/
						//
						
						if(chkDeclareStatement(lPatternDataDTOSybase.getFormedStatement(),lPatternDataDTOTarget.getFormedStatement(),pCompareSeq,lPatternDataDTOSybase.getProcedureName())){
							pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lPatternDataDTOTarget, pCompareSeq, "Y");
							/*lDeclareVariableDB2List.remove(j);
							if(j>0){ j=j-1; }*/
							lDeclareVariableTargetList.set(j, null);
							
							/*lDeclareVariableSybaseList.remove(i);
							if(i>0){ i=i-1; }*/
							lDeclareVariableSybaseList.set(i, null);
							break;
						}
						/*if(j==lDeclareVariableDB2List.size()-1){
							lTempPatternDataDTODb2 = new PatternDataDTO();							
							pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lTempPatternDataDTODb2, pCompareSeq, "N");
							lDeclareVariableSybaseList.remove(i);
							if(i>0){
								i=i-1;
							}
							
						}*/

						//lJCount++;

					}
					//////////////////////	
				}
				
			}
			System.out.println("--******-----------------------------");
			for (int i = 0; i < lDeclareVariableSybaseList.size(); i++) {				
				if(lDeclareVariableSybaseList.get(i)!=null){
					lTempPatternDataDTOTarget = new PatternDataDTO();
					lPatternDataDTOSybase=(PatternDataDTO)lDeclareVariableSybaseList.get(i);
					System.out.println("Sybase::=>"+lPatternDataDTOSybase.getFormedStatement());
					pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lTempPatternDataDTOTarget, pCompareSeq, "N");
				}
				
			}
			for (int i = 0; i < lDeclareVariableTargetList.size(); i++) {
				if(lDeclareVariableTargetList.get(i)!=null){
				lTempPatternDataDTOSybase = new PatternDataDTO();
				
				lPatternDataDTOTarget=(PatternDataDTO)lDeclareVariableTargetList.get(i);
				lTempPatternDataDTOSybase.setProcedureName(lPatternDataDTOTarget.getProcedureName());
				System.out.println("DB2::=>"+lPatternDataDTOTarget.getFormedStatement());
				lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO)pPatternDataListSybase.get(pPatternDataListSybase.size()-1)).getStatementNo());
				/*lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO) lDeclareVariableSybaseList.get(i)).getStatementNo());
				lTempPatternDataDTOSybase.setProcedureName(((PatternDataDTO) lDeclareVariableSybaseList.get(i)).getProcedureName());*/
				pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lTempPatternDataDTOSybase, lPatternDataDTOTarget, pCompareSeq, "N");
				}
			}

			// ///////////////
			pPreparedStatement.executeBatch();
			 //JOptionPane.showMessageDialog(null,"lInsertVarUsagePreparedStatement execute orcl"+lInsertVarUsagePreparedStatement);
			lInsertVarUsagePreparedStatement.executeBatch();
			
			System.out.println("lInsertVarUsagePreparedStatement execute orcl ");

		} catch (Exception pException) {
			pException.printStackTrace();
		} finally {
			DBConnectionManager.closeConnection(lPreparedStatement, null);
		}

	
	}
	public void compareDeclareVariables(List pPatternDataListSybase,List pPatternDataListTarget, String pCompareSeq){


		List lPatternDataListSybase = pPatternDataListSybase;
		List lPatternDataListTarget = pPatternDataListTarget;
		List lDeclareVariableSybaseList=new ArrayList();
		List lDeclareVariableSybaseListtemp=new ArrayList();
		List lDeclareVariableTargetList=new ArrayList();
		PatternDataDTO lPatternDataDTOSybase = new PatternDataDTO();
		PatternDataDTO lPatternDataDTOTarget = new PatternDataDTO();
		List lDeclareVarList=null;
		int lCount=0;
		System.out.println();
		
		///Preparing Source Declare Variable Pattern List
		for (int i = 0; i < pPatternDataListSybase.size(); i++) {
			lPatternDataDTOSybase = (PatternDataDTO) pPatternDataListSybase	.get(i);
			if(lPatternDataDTOSybase.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_VARIABLE)){
				
				///***********New Code for Multiple Variable Declarations***********//
				lDeclareVarList=getSplitStatements(lPatternDataDTOSybase.getFormedStatement(),"DECLARE ");
				//lCount =getVariableCount(lPatternDataDTOSybase.getFormedStatement(), "DECLARE ");		
				//System.out.println("::::lDeclareVarList:::"+lDeclareVarList.toString());
				
				for (int j = 0; j < lDeclareVarList.size(); j++) {
					PatternDataDTO lTmpPatternDataDTOSybase=new PatternDataDTO();
					lTmpPatternDataDTOSybase.setPatternId(lPatternDataDTOSybase.getPatternId());
					lTmpPatternDataDTOSybase.setPatternDesc(lPatternDataDTOSybase.getPatternDesc());
					lTmpPatternDataDTOSybase.setStatementNo(lPatternDataDTOSybase.getStatementNo());
					lTmpPatternDataDTOSybase.setProcedureName(lPatternDataDTOSybase.getProcedureName());
					//lTmpPatternDataDTOSybase.setFormedStatement(lPatternDataDTOSybase.getFormedStatement());
					
					lTmpPatternDataDTOSybase.setFormedStatement((String)lDeclareVarList.get(j));
					
					lDeclareVariableSybaseList.add(lTmpPatternDataDTOSybase);
					//lDeclareVariableSybaseListtemp.add(lTmpPatternDataDTOSybase);
					//System.out.println("-1::lDeclareVariableSybaseList value::::::"+lTmpPatternDataDTOSybase.getFormedStatement());
				}
				
				
				///***********New Code for Multiple Variable Declarations***********//
				
				//lDeclareVariableSybaseList.add(lPatternDataDTOSybase);
			}
			
			
		}
		
		
		//System.out.println("-2::lDeclareVariableSybaseList value::::::"+lDeclareVariableSybaseListtemp.toString());
		/*for (int j = 0; j < lDeclareVariableSybaseList.size(); j++) {
			System.out.println("-2::lDeclareVariableSybaseList value::::::"+((PatternDataDTO)lDeclareVariableSybaseList.get(j)).getFormedStatement());
		}
		System.out.println("***********************************");*/
		// Preparing Target Declare Variable Pattern List
		for (int i = 0; i < pPatternDataListTarget.size(); i++) {
			lPatternDataDTOTarget = (PatternDataDTO) pPatternDataListTarget.get(i);
			if(lPatternDataDTOTarget.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_VARIABLE)){
				lDeclareVariableTargetList.add(lPatternDataDTOTarget);
			}
			
		}		
		/*System.out.println("Source Size::->>"+lDeclareVariableSybaseList.size());
		System.out.println("Target Size::->>"+lDeclareVariableDB2List.size());
		int lChkCount=0;
		for (int i = 0; i < lDeclareVariableSybaseList.size(); i++) {
			lChkCount+= getVariableCount(((PatternDataDTO)lDeclareVariableSybaseList.get(i)).getFormedStatement(), "DECLARE ");	
		}
		System.out.println("lChkCount::->"+lChkCount);
*/
		PatternDataDTO lTempPatternDataDTOSybase=null;
		PatternDataDTO lTempPatternDataDTOTarget=null;
		
		try {
			// /////////////////
			
			int lTargetListCount = 0;
			int lJCount = 0;			
			List lTrgtPatternMatchLLst = new ArrayList();
			ComparedStatementDTO lComparedStatementDTO = null;
			PreparedStatement pPreparedStatement = null;
			pPreparedStatement = lConnection
					.prepareStatement(ToolConstant.INSERT_COMPARE_FORMED_STMT);
			int lDeclareCount=0;
			
			if (lDeclareVariableSybaseList != null) {
				for (int i = 0; i < lDeclareVariableSybaseList.size(); i++) {

					lPatternDataDTOSybase = (PatternDataDTO) lDeclareVariableSybaseList.get(i);
					if(lPatternDataDTOSybase==null){
						continue;
					}
					//System.out.println("Source::->"+lPatternDataDTOSybase.getFormedStatement());
					//System.out.println("-2::lDeclareVariableSybaseList value::::::"+((PatternDataDTO)lDeclareVariableSybaseList.get(i+1)).getFormedStatement());
					

					// System.out.println("---------------"+lPatternDataDTOSybase.getStatementNo()+" Pat ID:"+lPatternDataDTOSybase.getPatternId()+"------");
					// System.out.println(lPatternDataDTOSybase.getFormedStatement()+"  ::::  ");

					
					/*lDeclareVarList=getSplitStatements(lPatternDataDTOSybase.getFormedStatement(),"DECLARE ");
					lCount =getVariableCount(lPatternDataDTOSybase.getFormedStatement(), "DECLARE ");							
					lDeclareCount=0;
					////////////////////	
					lDB2ListCount = lJCount;
					lPatternDataDTOSybase.setFormedStatement((String)lDeclareVarList.get(lDeclareCount++));
					*/
					for (int j =0 /*lJCount*/; j < lDeclareVariableTargetList.size() /*lDB2ListCount+ lCount*/; j++) {
						lPatternDataDTOTarget = (PatternDataDTO) lDeclareVariableTargetList.get(j);	
						if(lPatternDataDTOTarget==null){
							continue;
						}
						//System.out.println("i:->"+i+"::No:"+lPatternDataDTOSybase.getStatementNo()+" Pat ::->"+lPatternDataDTOSybase.getFormedStatement()+"------");
						//System.out.println("i:->"+j+"::No:"+lPatternDataDTODb2.getStatementNo()+" Pat ::->"+lPatternDataDTODb2.getFormedStatement()+"------");
											
						/*if(lPatternDataDTODb2.getPatternId().trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_VARIABLE)){
							Pattern lOpenPattern=Pattern.compile("\\bDECLARE\\b\\s+V_");
							if(!lOpenPattern.matcher(lPatternDataDTODb2.getFormedStatement().toUpperCase().trim()).find()){
								//New Open Found in Target which no where related to Source so skipping pass.											
								lDB2ListCount=lDB2ListCount+1;
								lJCount++;
								lTempPatternDataDTOSybase = new PatternDataDTO();
								lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO) lDeclareVariableSybaseList.get(i)).getStatementNo());
								lTempPatternDataDTOSybase.setProcedureName(((PatternDataDTO) lDeclareVariableSybaseList.get(i)).getProcedureName());
								pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lTempPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "N");
								lDeclareVariableDB2List.remove(j);
								j=j-1;
								continue;
								
							}
						}*/
						
						//
						/*System.out.println("Source::->"+lPatternDataDTOSybase.getFormedStatement());
						System.out.println("Target::->"+lPatternDataDTODb2.getFormedStatement());
						System.out.println("----------------------------------");
						if(lPatternDataDTOSybase.getFormedStatement().contains("@DisabilityABSConcurrentInd")){
							System.out.println(lPatternDataDTODb2.getFormedStatement());
						}*/
						//
						
						if(chkDeclareStatement(lPatternDataDTOSybase.getFormedStatement(),lPatternDataDTOTarget.getFormedStatement(),pCompareSeq,lPatternDataDTOSybase.getProcedureName())){
							pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lPatternDataDTOTarget, pCompareSeq, "Y");
							/*lDeclareVariableDB2List.remove(j);
							if(j>0){ j=j-1; }*/
							lDeclareVariableTargetList.set(j, null);
							
							/*lDeclareVariableSybaseList.remove(i);
							if(i>0){ i=i-1; }*/
							lDeclareVariableSybaseList.set(i, null);
							break;
						}
						/*if(j==lDeclareVariableDB2List.size()-1){
							lTempPatternDataDTODb2 = new PatternDataDTO();							
							pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lTempPatternDataDTODb2, pCompareSeq, "N");
							lDeclareVariableSybaseList.remove(i);
							if(i>0){
								i=i-1;
							}
							
						}*/

						//lJCount++;

					}
					//////////////////////	
				}
				
			}
			System.out.println("--******-----------------------------");
			for (int i = 0; i < lDeclareVariableSybaseList.size(); i++) {				
				if(lDeclareVariableSybaseList.get(i)!=null){
					lTempPatternDataDTOTarget = new PatternDataDTO();
					lPatternDataDTOSybase=(PatternDataDTO)lDeclareVariableSybaseList.get(i);
					System.out.println("Sybase::=>"+lPatternDataDTOSybase.getFormedStatement());
					pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lTempPatternDataDTOTarget, pCompareSeq, "N");
				}
				
			}
			for (int i = 0; i < lDeclareVariableTargetList.size(); i++) {
				if(lDeclareVariableTargetList.get(i)!=null){
				lTempPatternDataDTOSybase = new PatternDataDTO();
				
				lPatternDataDTOTarget=(PatternDataDTO)lDeclareVariableTargetList.get(i);
				lTempPatternDataDTOSybase.setProcedureName(lPatternDataDTOTarget.getProcedureName());
				System.out.println("DB2::=>"+lPatternDataDTOTarget.getFormedStatement());
				lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO)pPatternDataListSybase.get(pPatternDataListSybase.size()-1)).getStatementNo());
				/*lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO) lDeclareVariableSybaseList.get(i)).getStatementNo());
				lTempPatternDataDTOSybase.setProcedureName(((PatternDataDTO) lDeclareVariableSybaseList.get(i)).getProcedureName());*/
				pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lTempPatternDataDTOSybase, lPatternDataDTOTarget, pCompareSeq, "N");
				}
			}

			// ///////////////
			pPreparedStatement.executeBatch();
			 //JOptionPane.showMessageDialog(null,"lInsertVarUsagePreparedStatement execute "+lInsertVarUsagePreparedStatement);
			lInsertVarUsagePreparedStatement.executeBatch();
			
			System.out.println("lInsertVarUsagePreparedStatement execute ");

		} catch (Exception pException) {
			pException.printStackTrace();
		} finally {
			DBConnectionManager.closeConnection(lPreparedStatement, null);
		}

	
	}
	public void getSPInputVariables(String pSourceFormedStatement,String pTargetFomredStatement,String pRunId,String pProcName){

		System.out.println("getSPInputVariables:::inside this function");
		try {
			Pattern lCreateProcPattern=Pattern.compile("(?i)(\\bCREATE\\b|\\bCREATE\\b\\s+\\bOR\\b\\s+\\bREPLACE\\b)\\s+(\\bPROCEDURE\\b|\\bPROC\\b|\\bFUNCTION\\b)+\\b[\\s\\w\\W\\r\\n]+ ");	
			//Pattern lCreateProcPattern2=Pattern.compile("(?i)\\bCREATE\\b[\\s\\r\\n]+\\bPROC");
			if(lCreateProcPattern.matcher(pSourceFormedStatement.toUpperCase().trim()).find()
					&& lCreateProcPattern.matcher(pTargetFomredStatement.toUpperCase().trim()).find()){
				String[] lSourceArray=pSourceFormedStatement.split("\\s+");
				for(int i=0;i<lSourceArray.length;i++){
				System.out.println("source array: "+lSourceArray[i]);
				}
				String[] lTargetArray=pTargetFomredStatement.split("\\s+");
				for(int i=0;i<lTargetArray.length;i++){
				System.out.println("target array: "+lTargetArray[i]);
				}
				List lSourceVarList=new ArrayList();
				List lTargetVarList=new ArrayList();
				String[] lSourceVarArr=null;
				String[] lTargetVarArr=null;
				for (int i = 0; i < lSourceArray.length; i++) {
					if(lSourceArray[i].toUpperCase().startsWith("@")){
						lSourceVarArr=new String[2];
						lSourceVarArr[0]=lSourceArray[i].trim();
						lSourceVarArr[1]=lSourceArray[i+1].trim();
						lSourceVarList.add(lSourceVarArr);	
						for(int k=0;k<lSourceVarArr.length;k++)
							System.out.println("source var arr " +lSourceVarArr[k]);
						
					}		
				}
				
				for (int i = 0; i < lTargetArray.length; i++) {
					if(lTargetArray[i].toUpperCase().startsWith("V_")){
						lTargetVarArr=new String[2];
						lTargetVarArr[0]=lTargetArray[i].trim();
						if(lTargetArray[i+1].toUpperCase().trim().equalsIgnoreCase("IN")){
						lTargetVarArr[1]=lTargetArray[i+2].trim();
						}
						else
							lTargetVarArr[1]=lTargetArray[i+1].trim();
						lTargetVarList.add(lTargetVarArr);	
						for(int k=0;k<lTargetVarArr.length;k++)
						System.out.println("target var arr " +lTargetVarArr[k]);
					}		
				}
				
				/*if(lSourceVarList.size()>lTargetVarList.size()){
					//return;
					for(int i=lTargetVarList.size();i<lSourceVarList.size();i++){
					lTargetVarList.add(i, " ");
					}
				}
				if(lTargetVarList.size()>lSourceVarList.size()){
					//return;
					for(int i=lSourceVarList.size();i<lTargetVarList.size();i++){
					lSourceVarList.add(i, " ");
					}
				}*/
				
				
				for (int i = 0; i < lSourceVarList.size(); i++) {
					//System.out.println("source var list "+(String[])lSourceVarList.get(i));
					//System.out.println("target var list "+(String[])lTargetVarList.get(i));
					//JOptionPane.showMessageDialog(null,"INSIDE SP INPUT VAR");
					
					pPrepareInsertForVarUsage(((String[])lSourceVarList.get(i)),((String[])lTargetVarList.get(i)),pRunId,pProcName);
					//System.out.println((((String[])lSourceVarList.get(i))[0])+"::"+((String[])lSourceVarList.get(i))[1]+" - "+(((String[])lTargetVarList.get(i))[0])+"::"+((String[])lTargetVarList.get(i))[1]);
				}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
			return;
		}
	
	}

	public boolean chkDeclareStatement(String lSourceFormedStatement,String lTargetFormedStatement,String pRunId,String pProcName){
	
		lSourceFormedStatement=lSourceFormedStatement.replaceAll("\\s+\\(\\s+","(");
		lSourceFormedStatement=lSourceFormedStatement.replaceAll("\\s+\\)",")");
		
		lTargetFormedStatement=lTargetFormedStatement.replaceAll("\\s+\\(\\s+","(");
		lTargetFormedStatement=lTargetFormedStatement.replaceAll("\\s+\\)",")");
			
		String[] lSourceVarArray=lSourceFormedStatement.trim().split("\\s+");
		String[] lTargetVarArray=lTargetFormedStatement.trim().split("\\s+");
	
		if(lSourceVarArray[1].toUpperCase().trim().replaceFirst("@","V_").equalsIgnoreCase(lTargetVarArray[1].toUpperCase().trim()) 
				|| lSourceVarArray[1].trim().toUpperCase().replaceFirst("@","SWV_").equalsIgnoreCase(lTargetVarArray[1].toUpperCase().trim()) ){
			
			//System.out.println("TARGET VAR ARRAY 3"+lTargetVarArray[3]);
		if(lTargetVarArray[2].toUpperCase().trim().equalsIgnoreCase("IN")){
				pPrepareInsertForVarUsage(new String[]{lSourceVarArray[1],lSourceVarArray[2]},new String[]{lTargetVarArray[1],lTargetVarArray[3]},pRunId,pProcName);
			}
		else 
			{
			pPrepareInsertForVarUsage(new String[]{lSourceVarArray[1],lSourceVarArray[2]},new String[]{lTargetVarArray[1],lTargetVarArray[2]},pRunId,pProcName);
			}
			return true;
		}
		return false;
	}
	
	public void pPrepareInsertForVarUsage(String[] lSourceVarArray,String[] lTargetVarArray,String pRunId,String pProcName){
		try {
			
			lInsertVarUsagePreparedStatement.setString(1, pRunId);
			lInsertVarUsagePreparedStatement.setString(2, pProcName);
			lInsertVarUsagePreparedStatement.setString(3, lSourceVarArray[1]);
			lInsertVarUsagePreparedStatement.setString(4, lSourceVarArray[0]);
			lInsertVarUsagePreparedStatement.setString(5, lTargetVarArray[0]);
			lInsertVarUsagePreparedStatement.setString(6, lTargetVarArray[1]);
			lInsertVarUsagePreparedStatement.setString(7, ToolConstant.CREATED_BY);
			lInsertVarUsagePreparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
			System.out.println("lInsertVarUsagePreparedStatement"+lSourceVarArray[1]);
			System.out.println("lInsertVarUsagePreparedStatement"+lTargetVarArray[1]);
			lInsertVarUsagePreparedStatement.addBatch();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public PreparedStatement prepareInsertCompareStatement(
			PreparedStatement pPreparedStatement, Connection pConnection,
			PatternDataDTO pPatternDataDTOSybase,PatternDataDTO pPatternDataDTODb2,String pCompareSeq,String pMatchedYN){
		
		if(pPatternDataDTOSybase == null){
			pPatternDataDTOSybase = new PatternDataDTO(); 
			pPatternDataDTOSybase.setProcedureName(pPatternDataDTODb2.getProcedureName());
		}
		if(pPatternDataDTODb2 == null){
			pPatternDataDTODb2 = new PatternDataDTO();	
			//pPatternDataDTODb2.setProcedureName(pPatternDataDTOSybase.getProcedureName());
		}
		
		if( (pPatternDataDTOSybase == null) && (pPatternDataDTODb2 == null)){
			return pPreparedStatement;
		}
		
		ComparedStatementDTO lComparedStatementDTO  = new ComparedStatementDTO();
		lOrderCount++;
		lComparedStatementDTO.setCompareSeq(pCompareSeq);
		lComparedStatementDTO.setOrderNo(lOrderCount+"");
		lComparedStatementDTO.setProcName(pPatternDataDTOSybase.getProcedureName());
		lComparedStatementDTO.setSourceStatementNo(ToolsUtil.replaceZero(pPatternDataDTOSybase.getStatementNo()));
		lComparedStatementDTO.setSourcePatternId(pPatternDataDTOSybase.getPatternId());
		lComparedStatementDTO.setSourceFormedStatement(pPatternDataDTOSybase.getFormedStatement());
		lComparedStatementDTO.setTargetStatementNo(ToolsUtil.replaceZero(pPatternDataDTODb2.getStatementNo()));
		lComparedStatementDTO.setTargetPatternId(pPatternDataDTODb2.getPatternId());
		lComparedStatementDTO.setTargetFormedStatement(pPatternDataDTODb2.getFormedStatement());
		lComparedStatementDTO.setMatchedYN(pMatchedYN);
		
		pPreparedStatement = insertCompareStatement(
				 pPreparedStatement,  pConnection,
				 lComparedStatementDTO);
		
		return pPreparedStatement;
		
	}
	
	public PreparedStatement insertCompareStatement(
			PreparedStatement pPreparedStatement, Connection pConnection,
			ComparedStatementDTO pComparedStatementDTO) {
		try {

			// prepare the query

			pPreparedStatement.setString(1,
					pComparedStatementDTO.getCompareSeq());
			pPreparedStatement.setString(2, pComparedStatementDTO.getOrderNo());
			pPreparedStatement
					.setString(3, pComparedStatementDTO.getProcName());
			pPreparedStatement.setString(4,
					pComparedStatementDTO.getSourceStatementNo());
			pPreparedStatement.setString(5,
					pComparedStatementDTO.getSourcePatternId());
			pPreparedStatement.setString(6,
					/*ToolConstant.TOOL_DELIMT+" "+*/pComparedStatementDTO.getSourceFormedStatement());
			pPreparedStatement.setString(7,
					pComparedStatementDTO.getTargetStatementNo());
			pPreparedStatement.setString(8,
					pComparedStatementDTO.getTargetPatternId());
			pPreparedStatement.setString(9,
					/*ToolConstant.TOOL_DELIMT+" "+*/pComparedStatementDTO.getTargetFormedStatement());
			pPreparedStatement.setString(10,
					pComparedStatementDTO.getMatchedYN());// for matched yn

			pPreparedStatement.setString(11, ToolConstant.CREATED_BY);
			pPreparedStatement.setTimestamp(12,
					new Timestamp(System.currentTimeMillis()));

			// Execute Query
			pPreparedStatement.addBatch();
			lInsertBatchCount++;
			/*
			 * if(lInsertBatchCount >= 1000){ lInsertBatchCount = 0;
			 * pPreparedStatement.executeBatch(); }
			 */

			// lPreparedStatement.executeBatch();

		} catch (SQLException pSQLException) {
			pSQLException.printStackTrace();
		} catch (Exception pException) {
			pException.printStackTrace();
		} finally {
			// DBConnectionManager.closeConnection(lPreparedStatement, null);
		}
		// return lInsertCount;

		return pPreparedStatement;
	}

	/**
	 * @param pPatternId
	 * @param pProcedureName
	 * @param pFormedStatement
	 * @return
	 */
	public boolean checkInputCursorName(String pProcedureName,
			String pFormedStatement) {
		try {
			System.out.println("Inside checkInputCursorName:::::::::::"+pFormedStatement);
			pFormedStatement = pFormedStatement.trim().toUpperCase();
			pProcedureName = pProcedureName.trim();
			System.out.println("ProceDure Name::"+pProcedureName);
			Pattern lCursorPattern = Pattern.compile("(?i)\\bCURSOR\\b[\\S\\W\\w\\r\\n]+\\bFOR\\b");
		
			String lCursorName = "";
			// Getting Cursor name from formedStaement.
			if (lCursorPattern.matcher(pFormedStatement.toUpperCase()).find()) {
				lCursorName = pFormedStatement.substring(
						pFormedStatement.indexOf("DECLARE ") +8,
						pFormedStatement.indexOf(" CURSOR")).trim();
				if (lCursorName == null && (("".equals(lCursorName)))) {
					return false;
				}

				/*
				 * System.out.println("lFormedStatement :--> "+lFormedStatement);
				 * System.out.println("Cursor Name:-->  "+lCursorName);
				 */
			}
			System.out.println("lCursorName:::->"+lCursorName);
			lCursorName=lCursorName.trim();
			/*
			 * System.out.println(":::::: proc name:::::"+pProcedureName);
			 * System.out.println(":::::::cursor name::::"+lCursorName);
			 */
			CursorDetailsDTO lCursorDetailsDTO = new CursorDetailsDTO();
			System.out.println("lSourceCursorDetailsList:::::-Size:::::"+lSourceCursorDetailsList.size());
			if (lSourceCursorDetailsList != null
					&& lSourceCursorDetailsList.size() > 0) {
				for (int i = 0; i < lSourceCursorDetailsList.size(); i++) {
					lCursorDetailsDTO = (CursorDetailsDTO) lSourceCursorDetailsList
							.get(i);
					if ((pProcedureName.trim().equalsIgnoreCase(lCursorDetailsDTO
							.getProcedureName().trim()))
							&& (lCursorName.equalsIgnoreCase(lCursorDetailsDTO
									.getCursorName().trim()))) {
						System.out.println("::::::fr loop data:::"+lCursorDetailsDTO.getProcedureName()+"<--->"+lCursorDetailsDTO.getCursorName());
						return true;
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * method to return the sequence
	 * 
	 * @return
	 */
	public String getCompareSeq(Connection lConnection) {
		String lRetSeqValue = null;
		try {
			// lConnection = DBConnectionManager.getConnection();

			// select the data from the table

			String lSQL = "UPDATE COMPARE_SEQUENCE_TABLE SET COMPARE_SEQ=COMPARE_SEQ+1";
			String lSQL1 = "SELECT COMPARE_SEQ FROM COMPARE_SEQUENCE_TABLE";

			lPreparedStatement = lConnection.prepareStatement(lSQL);
			lPreparedStatement.executeUpdate();
			// lConnection.commit();

			lPreparedStatement = lConnection.prepareStatement(lSQL1);
			lResultSet = lPreparedStatement.executeQuery();
			// if rs == null, then there is no ResultSet to view
			if (lResultSet != null) {
				while (lResultSet.next()) {
					lRetSeqValue = lResultSet.getString("COMPARE_SEQ");
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

	public int insertCompareMain(Connection pConnection, String pCompareSeq,
			String pDbMigrationType, String pSourceRunId, String pTargetRunId) {
		int lInsertCount = 0;
		try {
			int lDataCount = 0;
			
			lPreparedStatement = pConnection
					.prepareStatement(ToolConstant.SELECT_COMAPRE_MAIN_TABLE_DATA);
			lPreparedStatement.setString(1, pCompareSeq);
			lPreparedStatement.setString(2, pDbMigrationType);
			lResultSet = lPreparedStatement.executeQuery();
			
			while(lResultSet.next()){
				lDataCount = lResultSet.getInt(1);
			}
			DBConnectionManager.closeConnection(lPreparedStatement, lResultSet);
			if(lDataCount == 0){
				// prepare the query
				lPreparedStatement = pConnection
						.prepareStatement(ToolConstant.INSERT_COMAPRE_MAIN_TABLE_DATA);
				lPreparedStatement.setString(1, pCompareSeq);
				lPreparedStatement.setString(2, pDbMigrationType);
				lPreparedStatement.setString(3, pSourceRunId);
				lPreparedStatement.setString(4, pTargetRunId);
				lPreparedStatement.setString(5, ToolConstant.CREATED_BY);
				lPreparedStatement.setTimestamp(6,
						new Timestamp(System.currentTimeMillis()));

				// Execute Query
				lInsertCount = lPreparedStatement.executeUpdate();
			}			

			

			// lPreparedStatement.executeBatch();

		} catch (SQLException pSQLException) {
			pSQLException.printStackTrace();
		} catch (Exception pException) {
			pException.printStackTrace();
		} finally {
			DBConnectionManager.closeConnection(lPreparedStatement, null);
		}
		return lInsertCount;

	}
	
	
	public List getSplitStatements(String pSourceFormedStatement,String pStatementType){
		
		 //pSourceFormedStatement="SELECT @var1=sdsd , @var2=asdad, var3=ASDAdas, var4=adad";
		 //System.out.println("Source::->"+pSourceFormedStatement);
		 List lDeclareVarList=new ArrayList();
		 //String pStatementType="DECLARE ";
		 String lVarString=pSourceFormedStatement.substring(pSourceFormedStatement.toUpperCase().indexOf(pStatementType)+pStatementType.length(),pSourceFormedStatement.length()).trim();
		 int lVarCount=0;
		 int lChkOpenBrace=0;
		 lVarString=lVarString.replaceAll(",", " , ");
		 lVarString=lVarString.replaceAll("\\)", " \\) ");
		 lVarString=lVarString.replaceAll("\\-", " \\- ");
		 lVarString=lVarString.replaceAll("\\(", " \\( ");
		 lVarString=lVarString.replaceAll("\"", " \" ");
		 lVarString=lVarString.replaceAll("'", " ' ");
		 String lActualVar="";
		 //System.out.println("Var String::->"+lVarString);
		 boolean chkDoubleQuotes=false;
		 boolean chkSingleQuotes=false;
		 String[] lVarArray=lVarString.split("\\s+");
		 for (int i = 0; i < lVarArray.length; i++) {
			if((",".equals(lVarArray[i].trim())) && (lChkOpenBrace==0) && (chkDoubleQuotes==false) && (chkSingleQuotes==false)){
				lVarCount++;
				//lActualVar=pStatementType+" "+lActualVar+ToolConstant.TOOL_DELIMT;
				lActualVar=pStatementType+" "+lActualVar;
				lDeclareVarList.add((String)lActualVar.trim());
				//System.out.println("lActualVar::->>>> DECLARE "+lActualVar);
				lActualVar="";
				continue;
			}
			if("\"".equals(lVarArray[i].trim())){
				if(chkDoubleQuotes==false){
					chkDoubleQuotes=true;
				}else{
					chkDoubleQuotes=false;
				}
			}
			if("'".equals(lVarArray[i].trim())){
				if(chkSingleQuotes==false){
					chkSingleQuotes=true;
				}else{
					chkSingleQuotes=false;
				}
			}
			if("(".equals(lVarArray[i].trim())){
				lChkOpenBrace++;
			}
			if(")".equals(lVarArray[i].trim())){
				lChkOpenBrace--;
			}
			lActualVar=lActualVar+" "+lVarArray[i];
		}
		 //lActualVar=pStatementType+" "+lActualVar+ToolConstant.TOOL_DELIMT;
		 lActualVar=pStatementType+" "+lActualVar;
		 lDeclareVarList.add((String)lActualVar.trim());
		 //System.out.println("lActualVar::->>>> DECLARE "+lActualVar);
		 //System.out.println("$$$$$$$$$$$$$$$lDeclareVarList$$$$$$$$$$$$$$$$"+lDeclareVarList.toString());
		 return lDeclareVarList;
		 
	}
	
	public  int getVariableCount(String pSourceFormedStatement,String pStatementType){
		
		 //pSourceFormedStatement="SELECT @var1=sdsd , @var2=asdad, var3=ASDAdas, var4=adad";
		 //System.out.println("Source::->"+pSourceFormedStatement);
		 String lVarString=pSourceFormedStatement.substring(pSourceFormedStatement.toUpperCase().indexOf(pStatementType.toUpperCase())+pStatementType.length(),pSourceFormedStatement.length()).trim();
		 int lVarCount=0;
		 int lChkOpenBrace=0;
		 lVarString=lVarString.replaceAll(",", " , ");
		 lVarString=lVarString.replaceAll("\\)", " \\) ");
		 lVarString=lVarString.replaceAll("\\(", " \\( ");
		 lVarString=lVarString.replaceAll("\"", " \" ");
		 lVarString=lVarString.replaceAll("'", " ' ");
		 lVarString=lVarString.replaceAll("\\-", " \\- ");
		 //System.out.println("Var String::->"+lVarString);
		 boolean chkDoubleQuotes=false;
		 boolean chkSingleQuotes=false;
		 String[] lVarArray=lVarString.split("\\s+");
		 for (int i = 0; i < lVarArray.length; i++) {
			if((",".equals(lVarArray[i].trim())) && (lChkOpenBrace==0) && (chkDoubleQuotes==false) && (chkSingleQuotes==false)){
				lVarCount++;
			}
			if("\"".equals(lVarArray[i].trim())){
				if(chkDoubleQuotes==false){
					chkDoubleQuotes=true;
				}else{
					chkDoubleQuotes=false;
				}
			}
			if("'".equals(lVarArray[i].trim())){
				if(chkSingleQuotes==false){
					chkSingleQuotes=true;
				}else{
					chkSingleQuotes=false;
				}
			}
			if("(".equals(lVarArray[i].trim())){
				lChkOpenBrace++;
			}
			if(")".equals(lVarArray[i].trim())){
				lChkOpenBrace--;
			}
		}
		 return lVarCount+1;
		 }
	public boolean chkForExtraSet(String pTargetFormedStatement){
		System.out.println(":::: in method -chkForExtraSet- pTargetFormedStatement:::-"+pTargetFormedStatement);
		 pTargetFormedStatement=pTargetFormedStatement.trim().toUpperCase();
		 String[] lVarArray=null;
		 String lVarString=pTargetFormedStatement.trim().substring(pTargetFormedStatement.indexOf("SET ")+3,pTargetFormedStatement.length()).trim();
		 System.out.println(":::: in method -chkForExtraSet- lVarString:::-"+lVarString);
		 System.out.println("Index of ::->"+pTargetFormedStatement.indexOf("SET ")+3);
		 String lFirstVar="";
		 String lSecVar="";
		if(lVarString.contains("=")){
			lVarArray=lVarString.split("=");
			
			//Checking for l_error set
			if(lVarArray[0].toUpperCase().trim().startsWith("l_")){
				return true;				
			}			
			
			if(lVarArray[0].toUpperCase().trim().startsWith("SWV_")){
				lFirstVar=lVarArray[0].substring(lVarArray[0].toUpperCase().indexOf("SWV_")+4,lVarArray[0].length());
				
			}
			if(lVarArray[1].toUpperCase().trim().startsWith("V_")){
				
				lSecVar=lVarArray[1].substring(lVarArray[1].toUpperCase().indexOf("V_")+2,lVarArray[1].length());
				
			}
			System.out.println("<<<<<<<<<<<<chkForExtraSet-method>>>>>>>>>>>>>>>>>>>>"+lFirstVar);
			System.out.println("<<<<<<<<<<<<chkForExtraSet-method-lSecVar>>>>>>>>>>>>>>>>>>>>"+lSecVar);
			System.out.println("lVarArray[0]::"+lVarArray[0]+"  ::-::lVarArray[1]::->"+lVarArray[1]);
			//System.out.println("lFirstVar::->"+lFirstVar+"<<<>>>lSecVar::::->"+lSecVar);
			if(lFirstVar.trim().equalsIgnoreCase(lSecVar.trim())){
				System.out.println("Came Inside");
				lSetVarList.add((String)lFirstVar.trim().toUpperCase());
				return true;//This Set is an Extra Statement.
			}
			if(lVarArray[0].toUpperCase().trim().startsWith("SWV_") && lSetVarList.contains((String)lFirstVar.trim().toUpperCase())){			
				return false;//Not an Extra Statement
			}else{
				return true;
			}
			
		}
		
		return false;//Not an Extra Statement
	 }
	
	//Comparison for other DB MIGRATION TYPES
	public void compareTwoProcsForOtherDBType(List pPatternDataListSybase,
			List pPatternDataListDb2, HashMap lPatternMatchCountMap,
			HashMap lSrcTargtPatternMatchHM, String pCompareSeq) {

		List lPatternDataListSybase = pPatternDataListSybase;
		List lPatternDataListDb2 = pPatternDataListDb2;
		PatternDataDTO lPatternDataDTOSybase = new PatternDataDTO();
		PatternDataDTO lPatternDataDTODb2 = new PatternDataDTO();
		// System.out.println("lPatternDataListSybase size"+lPatternDataListSybase.size());
		// System.out.println("lPatternDataListDb2 size"+lPatternDataListDb2.size());
		PatternDataDTO lTmpPatternDataDTO = new PatternDataDTO();
		List lSetVariableList=new ArrayList();
		PatternDataDTO lTempPatternDataDTOSybase = null;
		try {
			
			int lCount = 0;
			int lDB2ListCount = 0;
			int lJCount = 0;
			lSetVarList=new ArrayList();
			String lTrgtPatternMatchPatStr ="";
			ComparedStatementDTO lComparedStatementDTO = null;
			PreparedStatement pPreparedStatement = null;
			pPreparedStatement = lConnection
					.prepareStatement(ToolConstant.INSERT_COMPARE_FORMED_STMT);
			int lSetVarCount=0;
			if (lPatternDataListSybase != null) {
				for (int i = 0; i < lPatternDataListSybase.size(); i++) {

					lPatternDataDTOSybase = (PatternDataDTO) lPatternDataListSybase	.get(i);
					if (lPatternMatchCountMap.containsKey(lPatternDataDTOSybase.getPatternId().trim())) {
						if ((String) lPatternMatchCountMap.get(lPatternDataDTOSybase.getPatternId().trim()) != null) {
							lCount = Integer.parseInt((String) lPatternMatchCountMap.get(lPatternDataDTOSybase.getPatternId().trim()));							

							if (lCount <=0){
								System.out.println("Count less than 0");
									
						
							} else if (lCount > 0) {

								lDB2ListCount = lJCount;
								for (int j = lJCount; j < lDB2ListCount + lCount; j++) {

									// System.out.println("i::-> "+i+"  :::: j-> "+j+" lJCount::-> "+lJCount);

									lPatternDataDTODb2 = (PatternDataDTO) lPatternDataListDb2.get(j);									

									if (lSrcTargtPatternMatchHM.containsKey(lPatternDataDTOSybase.getPatternId().trim())) {
										// System.out.println("Trgt pattern_id:"+lPatternDataDTODb2.getPatternId()+"Statement no"+lPatternDataDTODb2.getStatementNo());
										// System.out.println("2DB2:-> "+lPatternDataDTODb2.getFormedStatement());
										lTrgtPatternMatchPatStr = (String) lSrcTargtPatternMatchHM.get(lPatternDataDTOSybase.getPatternId().trim());
										
										if (lTrgtPatternMatchPatStr.equalsIgnoreCase(lPatternDataDTODb2.getPatternId().trim())) {
											
											pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "Y");										

										} else {
											
											pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "N");
											pPreparedStatement.executeBatch();
											return;
										}

									} else {
										System.out.println("2Inavlid Pattern Captured Pattern Id:: "
														+ lPatternDataDTOSybase
																.getPatternId()
														+ " at Statement No: "
														+ lPatternDataDTOSybase
																.getStatementNo());
										pPreparedStatement.executeBatch();
										return;
									}
									lJCount++;

								}

							} else {
								System.out.println("Count is -1" + ((PatternDataDTO) lPatternDataListSybase
														.get(i)).getPatternId()+ ((PatternDataDTO) lPatternDataListSybase
																.get(i)).getFormedStatement());
							}
						}
					}

					
				}
				//Inserting the rest of target patterns after comparison
				for (int j = lJCount; j <lPatternDataListDb2.size() ; j++) {
					lPatternDataDTODb2 = (PatternDataDTO) lPatternDataListDb2.get(j);					
					lTempPatternDataDTOSybase = new PatternDataDTO();
					lTempPatternDataDTOSybase.setStatementNo(((PatternDataDTO) lPatternDataListSybase.get(lPatternDataListSybase.size()-1)).getStatementNo());
					lTempPatternDataDTOSybase.setProcedureName(((PatternDataDTO) lPatternDataListSybase.get(lPatternDataListSybase.size()-1)).getProcedureName());
					pPreparedStatement = prepareInsertCompareStatement(pPreparedStatement,lConnection,lTempPatternDataDTOSybase, lPatternDataDTODb2, pCompareSeq, "N");

				}
			}

			
			pPreparedStatement.executeBatch();

		} catch (Exception pException) {
			pException.printStackTrace();
		} finally {
			DBConnectionManager.closeConnection(lPreparedStatement, null);
		}

	}

	/*
	 * public getHashmapPatterCount(HashMap phm ,String pPatternId){
	 * phm.get(key) }
	 */

	public static void main(String[] args) {
		System.out.println(":::::inside main:::::: ");
		FileCompareDAO lFileCompareDAO=new FileCompareDAO();
		lFileCompareDAO. prepareListsToCompare("PRID91_SOURCE", "PRID91_TARGET","SYSBASE_TO_DB2","PRID91");
	}
}
