package com.tcs.tools.web.constant;

public class WebConstant {
	public static final String INSERT_PROJECT_DETAILS = " INSERT INTO tool_project_details_table(PROJECT_ID,  SOURCE_DB_TYPE, TARGET_DB_TYPE, FILE_PATH, PROJECT_STATE, CREATED_BY, CREATED_DATE )"
			+ " VALUES(?,?,?,?,?,?,?)";
	// public static final String UPDATE_FILE_PROJECT_DETAILS =
	// " UPDATE TOOL_PROJECT_DETAILS SET SOURCE_PATH = ? ,TARGET_PATH= ? ,UPDATED_BY = ? , UPDATED_DATE  = ?,SOURCE_DB_TYPE = ? ,TARGET_DB_TYPE =?,CUSTOMER_NAME =?,APPLICATION_NAME =?,SOURCE_DB_VERSION =?,TARGET_DB_VERSION =? ,DATA_UPLOAD_STATUS =? WHERE PROJECT_ID = ?  ";
	public static final String UPDATE_FILE_PROJECT_DETAILS = " UPDATE TOOL_PROJECT_DETAILS SET SOURCE_PATH = ? ,TARGET_PATH= ? ,UPDATED_BY = ? , UPDATED_DATE  = ?,CUSTOMER_NAME =?,APPLICATION_NAME =?,SOURCE_DB_VERSION =?,TARGET_DB_VERSION =? ,DATA_UPLOAD_STATUS =?,UPLOAD_SEQ = ? , SOURCE_DB_HOST_IP = ? ,SOURCE_DB_HOST_PORT = ? ,SOURCE_DB_SCHEMA_NAME = ? ,SOURCE_DB_USER_NAME = ? ,SOURCE_DB_PASSWORD = ? ,SOURCE_UNIX_IP = ? ,SOURCE_UNIX_USER_NAME = ? ,SOURCE_UNIX_PASSWORD = ?  WHERE PROJECT_ID = ?  ";
	// public static final String UPDATE_FILE_PROJECT_DETAILS =
	// " UPDATE TOOL_PROJECT_DETAILS SET UPDATED_BY = ? , UPDATED_DATE  = ?,CUSTOMER_NAME =?,APPLICATION_NAME =?,SOURCE_DB_VERSION =?,TARGET_DB_VERSION =? ,DATA_UPLOAD_STATUS =?, SOURCE_DB_HOST_IP = ? ,SOURCE_DB_HOST_PORT = ? ,SOURCE_DB_SCHEMA_NAME = ? ,SOURCE_DB_USER_NAME = ? ,SOURCE_DB_PASSWORD = ? ,SOURCE_UNIX_IP = ? ,SOURCE_UNIX_USER_NAME = ? ,SOURCE_UNIX_PASSWORD = ?  WHERE PROJECT_ID = ?  ";

	public static final String SELECT_FILE_PROJECT_DETAILS = " SELECT COUNT(*) AS COUNT  FROM TOOL_PROJECT_DETAILS WHERE PROJECT_ID= ? ";
	public static final String SELECT_TOTAL_PROJECT_DETAILS = " SELECT PROJECT_ID, SOURCE_DB_TYPE, TARGET_DB_TYPE, SOURCE_PATH, TARGET_PATH, PROJECT_NAME, CUSTOMER_NAME, APPLICATION_NAME, SOURCE_DB_VERSION, TARGET_DB_VERSION ,SOURCE_DB_HOST_IP, SOURCE_DB_HOST_PORT, SOURCE_DB_SCHEMA_NAME, SOURCE_DB_USER_NAME, SOURCE_DB_PASSWORD, SOURCE_UNIX_IP, SOURCE_UNIX_USER_NAME, SOURCE_UNIX_PASSWORD, TARGET_DB_HOST_IP, TARGET_DB_HOST_PORT, TARGET_DB_SCHEMA_NAME, TARGET_DB_USER_NAME, TARGET_DB_PASSWORD, TARGET_UNIX_USER_NAME, TARGET_UNIX_PASSWORD, TARGET_DB_NAME  FROM TOOL_PROJECT_DETAILS WHERE PROJECT_ID= ? ";

	public static final String INSERT_FILE_PROJECT_DETAILS = " INSERT INTO TOOL_PROJECT_DETAILS(PROJECT_ID, SOURCE_PATH, TARGET_PATH, CREATED_BY, CREATED_DATE ,SOURCE_DB_TYPE ,TARGET_DB_TYPE,PROJECT_NAME,CUSTOMER_NAME,APPLICATION_NAME,SOURCE_DB_VERSION,TARGET_DB_VERSION,DATA_UPLOAD_STATUS,UPLOAD_SEQ ,SOURCE_DB_HOST_IP,SOURCE_DB_HOST_PORT,SOURCE_DB_SCHEMA_NAME,SOURCE_DB_USER_NAME,SOURCE_DB_PASSWORD ,SOURCE_UNIX_IP,SOURCE_UNIX_USER_NAME,SOURCE_UNIX_PASSWORD) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,? ,?,?,?,?,? ,?,?,?) ";
	public static final String INSERT_FILE_PROJECT_DETAILS_NEW = " INSERT INTO TOOL_PROJECT_DETAILS(PROJECT_ID, SOURCE_PATH, TARGET_PATH, CREATED_BY, CREATED_DATE ,SOURCE_DB_TYPE ,TARGET_DB_TYPE,PROJECT_NAME,CUSTOMER_NAME,APPLICATION_NAME,SOURCE_DB_VERSION,TARGET_DB_VERSION,DATA_UPLOAD_STATUS,UPLOAD_SEQ ,SOURCE_DB_HOST_IP,SOURCE_DB_HOST_PORT,SOURCE_DB_SCHEMA_NAME,SOURCE_DB_USER_NAME,SOURCE_DB_PASSWORD ,SOURCE_UNIX_IP,SOURCE_UNIX_USER_NAME,SOURCE_UNIX_PASSWORD,TARGET_DB_HOST_IP ,TARGET_DB_HOST_PORT ,TARGET_DB_SCHEMA_NAME ,TARGET_DB_USER_NAME ,TARGET_DB_PASSWORD ,TARGET_UNIX_USER_NAME ,TARGET_UNIX_PASSWORD,TARGET_DB_NAME ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,?,?,?,?,?,?,?) ";

	public static final String UPDATE_FILE_PROJECT_UPLOAD_STATUS = "UPDATE TOOL_PROJECT_DETAILS SET DATA_UPLOAD_STATUS =? WHERE PROJECT_ID = ? ";

	public static final String GET_PROJECT_SP_LIST = " SELECT PROCEDURE_NAME FROM TOOL_PROJECT_STORED_PROC_DETAILS WHERE PROJECT_ID = ? AND REC_TYPE = ? ";

	public static final String INSERT_PROJECT_SP_LIST = "INSERT INTO TOOL_PROJECT_STORED_PROC_DETAILS (PROJECT_ID, RUN_SEQ,PROCEDURE_NAME,PROCEDURE_FOLDER_PATH, REC_TYPE, CREATED_BY, CREATED_DATE,USE_YN)VALUES(?,?,?,?,?,?,?,?)";

	public static final String DELETE_PROJECT_SP_LIST = "DELETE FROM TOOL_PROJECT_STORED_PROC_DETAILS WHERE PROJECT_ID= ? AND REC_TYPE = ?";

	public static final String GET_PROC_NAMES_FOR_IDENTIFY = " SELECT DISTINCT PROCEDURE_NAME FROM TOOL_PROJECT_STORED_PROC_DETAILS WHERE PROJECT_ID= ? AND REC_TYPE=? AND USE_YN='Y' ";

	public static final String INSERT_IDENTIFY_PATTERN_MAIN = "INSERT INTO TOOL_IDENTIFY_PATTERN_DETAIL(SEQ_NO, PROJECT_ID, ANALYSIS_MODE, REC_TYPE, CREATED_BY, CREATED_DATE,REPORT_TYPE) VALUES(?,?,?,?,?,?,?)";

	public static final String INSERT_IDENTIFY_PATTERN_SP_LIST = " INSERT INTO TOOL_IDENTIFY_PATTERN_SP_LIST(SEQ_NO, PROCEDURE_NAME, CREATED_BY, CREATED_DATE) VALUES(?,?,?,?) ";

	public static final String GET_SP_PATTERN_DESC_ANALYSIS_REPORT = " SELECT RUN_ID, PROCEDURE_NAME, STATEMENT, STATEMENT_NO, "
			+ " 	KEY_WORD, SCORE, PATTERN_ID, PATTERN_DESC, "
			+ " 	FORMED_STATEMENT, CREATED_BY, CREATED_DATE, QUERY_COUNT "
			+ " 	FROM PATTERN_RESULTS_TABLE WHERE RUN_ID= ? ; ";
	
	public static final String GET_SP_MANUAL_MODIFICATION_LOG_REPORT = " SELECT RUN_ID, PROCEDURE_NAME, STATEMENT, STATEMENT_NO, "
			
			+ " 	 CREATED_BY, CREATED_DATE "
			+ " 	FROM manually_added_code_blocks_table WHERE RUN_ID= ? ; ";
	
	
	
	public static final String GET_SP_PATTERN_DESC_ANALYSIS_REPORT_PARTIAL = " SELECT RUN_ID, a.PROCEDURE_NAME, STATEMENT, STATEMENT_NO, "
			+ " KEY_WORD, SCORE, PATTERN_ID, PATTERN_DESC, "
			+ " FORMED_STATEMENT,QUERY_COUNT  "
			+ " FROM PATTERN_RESULTS_TABLE a,TOOL_IDENTIFY_PATTERN_SP_LIST b WHERE RUN_ID= ?  "
			+ " and a.procedure_name =b.procedure_name and b.seq_no= ?  ";
	
	
	public static final String GET_SP_MANUAL_MODIFICATION_LOG_REPORT_PARTIAL = " SELECT RUN_ID, a.PROCEDURE_NAME, STATEMENT, STATEMENT_NO "
			
			+ " FROM manually_added_code_blocks_table a,TOOL_IDENTIFY_PATTERN_SP_LIST b WHERE RUN_ID= ?  "
			+ " and a.procedure_name =b.procedure_name and b.seq_no= ?  ";
	
	
	

	public static final String GET_SP_PATTERN_DESC_ANALYSIS_REPORT_FOLDER = " SELECT RUN_ID, PROCEDURE_NAME, STATEMENT, STATEMENT_NO, "
			+ " 	KEY_WORD, SCORE, PATTERN_ID, PATTERN_DESC, "
			+ " 	FORMED_STATEMENT, CREATED_BY, CREATED_DATE, QUERY_COUNT "
			+ " 	FROM PATTERN_RESULTS_TABLE WHERE RUN_ID= ? and FOLDER_PATH =? ; ";
	public static final String GET_SP_PATTERN_DESC_ANALYSIS_REPORT_PARTIAL_FOLDER = " SELECT RUN_ID, a.PROCEDURE_NAME, STATEMENT, STATEMENT_NO, "
			+ " KEY_WORD, SCORE, PATTERN_ID, PATTERN_DESC, "
			+ " FORMED_STATEMENT,QUERY_COUNT  "
			+ " FROM PATTERN_RESULTS_TABLE a,TOOL_IDENTIFY_PATTERN_SP_LIST b WHERE RUN_ID= ?  "
			+ " and a.procedure_name =b.procedure_name and b.seq_no= ? and a.FOLDER_PATH =? ";

	public static final String GET_SP_PATTERN_DESC_ANALYSIS_REPORT_TOP_DATA = " select SEQ_NO, a.PROJECT_ID,c.PROJECT_NAME,c.CUSTOMER_NAME, c.APPLICATION_NAME, c.SOURCE_DB_VERSION, c.TARGET_DB_VERSION ,ANALYSIS_MODE, REC_TYPE, "
			+ " a.CREATED_BY, a.CREATED_DATE,c.SOURCE_DB_TYPE,C.TARGET_DB_TYPE "
			+ " from TOOL_IDENTIFY_PATTERN_DETAIL a,TOOL_PROJECT_DETAILS c"
			+ " where seq_no= ? " +
			// " and b.project_id = a.project_id " +
			" and a.project_id = c.project_id ";

	public static final String SELECT_DISTINCT_FILE_PROJECT_DETAILS = " SELECT count(*) AS COUNT  FROM TOOL_PROJECT_DETAILS WHERE PROJECT_NAME = ? ";
	public static final String SELECT_DISTINCT_PROJECT_DETAILS = " SELECT distinct PROJECT_NAME,PROJECT_ID  FROM TOOL_PROJECT_DETAILS order by PROJECT_NAME";

	public static final String SELECT_INVENTORY_ANALYSYS_QUERY = "select QUERY_TITLE,QUERY_TEXT from TOOL_INVERNTORY_ANALYSYS_QUERIES where QUERY_TYPE= ?  ";
	public static final String SELECT_PROJECT_DETAILS_SOURCE_DB_DETAILS = " SELECT SOURCE_DB_HOST_IP,SOURCE_DB_HOST_PORT,SOURCE_DB_SCHEMA_NAME,SOURCE_DB_USER_NAME,SOURCE_DB_PASSWORD,SOURCE_UNIX_IP,SOURCE_UNIX_USER_NAME,SOURCE_UNIX_PASSWORD,PROJECT_NAME,SOURCE_PATH, TARGET_PATH   FROM TOOL_PROJECT_DETAILS WHERE PROJECT_ID = ? ";
	
	public static final String SELECT_PROJECT_DETAILS_TARGET_DB_DETAILS = " SELECT TARGET_DB_HOST_IP,TARGET_DB_HOST_PORT,TARGET_DB_SCHEMA_NAME,TARGET_DB_USER_NAME,TARGET_DB_PASSWORD,TARGET_UNIX_USER_NAME,TARGET_UNIX_PASSWORD,TARGET_DB_NAME,PROJECT_NAME,SOURCE_PATH, TARGET_PATH   FROM TOOL_PROJECT_DETAILS WHERE PROJECT_ID = ? ";
	
	
	
	public static final String SELECT_INVENTORY_ANALYSYS_QUERY_LIST = "select QUERY_TITLE,QUERY_TEXT,QUERY_TYPE from TOOL_INVERNTORY_ANALYSYS_QUERIES ORDER BY QUERY_TITLE";
	public static final String SELECT_CHART_REPORT_SP_SQL_PATTERN_DATA = "SELECT CASE WHEN PATTERN_DESC LIKE 'Complex%' THEN 'Complex Query' ELSE PATTERN_DESC END AS MODIFIED_PATTERN_DESC, "
			+ " COUNT(*) AS COUNT  "
			+ " FROM pattern_results_table p "
			+ " where run_id= ? "
			+ " and (pattern_id like 'PAT%' OR pattern_id like 'P_DML%') "
			+ " and pattern_id not like 'PAT_E%' "
			+ " GROUP BY CASE WHEN PATTERN_DESC LIKE 'Complex%' THEN 'Complex Query' ELSE PATTERN_DESC END "
			+ " ORDER BY MODIFIED_PATTERN_DESC ";

	public static final String SELECT_CHART_REPORT_SP_SQL_PATTERN_DATA_PROC_COUNT = "SELECT CASE WHEN PATTERN_DESC LIKE 'Complex%' THEN 'Complex Query' ELSE PATTERN_DESC END AS MODIFIED_PATTERN_DESC, "
			+ " COUNT(DISTINCT PROCEDURE_NAME) AS COUNT  "
			+ " FROM pattern_results_table p "
			+ " where run_id= ? "
			+ " and pattern_id like 'PAT%' "
			+ " and pattern_id not like 'PAT_E%' "
			+ " GROUP BY CASE WHEN PATTERN_DESC LIKE 'Complex%' THEN 'Complex Query' ELSE PATTERN_DESC END "
			+ " ORDER BY MODIFIED_PATTERN_DESC ";

	public static final String SELECT_CHART_REPORT_SP_SQL_COUNT = " select count(COUNT) as COUNT,'0 TO 50' as MODIFIED_PATTERN_DESC ,'1' AS ORDER_VALUE from ( "
			+ " SELECT COUNT( pattern_id )AS COUNT , "
			+ " PROCEDURE_NAME AS MODIFIED_PATTERN_DESC "
			+ " FROM pattern_results_table p "
			+ " where run_id= ? and (pattern_id like 'PAT%' OR pattern_id like 'P_DML%') "
			+ " and pattern_id not like 'PAT_E%'"
			+ " GROUP BY PROCEDURE_NAME"
			+ " ORDER BY MODIFIED_PATTERN_DESC) a"
			+ " where a.COUNT <=50"
			+ " union"
			+ " select count(COUNT) as COUNT,'51 TO 100','2' AS ORDER_VALUE from ("
			+ " SELECT COUNT( pattern_id )AS COUNT ,"
			+ " PROCEDURE_NAME AS MODIFIED_PATTERN_DESC"
			+ " FROM pattern_results_table p"
			+ " where run_id= ? and  (pattern_id like 'PAT%' OR pattern_id like 'P_DML%') "
			+ " and pattern_id not like 'PAT_E%'"
			+ " GROUP BY PROCEDURE_NAME"
			+ " ORDER BY MODIFIED_PATTERN_DESC) a"
			+ " where a.COUNT >=51 and a.COUNT<=100"
			+ " union"
			+ " select count(COUNT) as COUNT,'101 TO 300','3' AS ORDER_VALUE from ("
			+ " SELECT COUNT( pattern_id )AS COUNT ,"
			+ " PROCEDURE_NAME AS MODIFIED_PATTERN_DESC"
			+ " FROM pattern_results_table p"
			+ " where run_id= ? and (pattern_id like 'PAT%' OR pattern_id like 'P_DML%') "
			+ " and pattern_id not like 'PAT_E%'"
			+ " GROUP BY PROCEDURE_NAME"
			+ " ORDER BY MODIFIED_PATTERN_DESC) a"
			+ " where a.COUNT >=101 and a.COUNT<=300"
			+ " union"
			+ " select count(COUNT) as COUNT,'Greater Than 300','4' AS ORDER_VALUE from ("
			+ " SELECT COUNT( pattern_id )AS COUNT ,"
			+ " PROCEDURE_NAME AS MODIFIED_PATTERN_DESC"
			+ " FROM pattern_results_table p"
			+ " where run_id= ? and (pattern_id like 'PAT%' OR pattern_id like 'P_DML%')"
			+ " and pattern_id not like 'PAT_E%'"
			+ " GROUP BY PROCEDURE_NAME"
			+ " ORDER BY MODIFIED_PATTERN_DESC) a"
			+ " where a.COUNT >=301"
			+ " order by ORDER_VALUE";

	public static final String SELECT_CHART_REPORT_DATATYPE_USAGE = "SELECT a.PATTERN_DESC, "
			+ " /*b.keyword*/ a.PATTERN_DESC  AS MODIFIED_PATTERN_DESC, "
			+ " COUNT(a.PROCEDURE_NAME) AS COUNT "
			+ " FROM pattern_results_table a,pattern_indvidual_keyword_lookup_table b "
			+ "  where run_id= ? and a.pattern_id like ? "
			+
			// "  and impacted_yn='Y' " +
			" and a.pattern_id =b.pattern_id "
			+ " GROUP BY a.PATTERN_DESC,b.keyword "
			+ " ORDER BY MODIFIED_PATTERN_DESC ";

	public static final String SELECT_CHART_REPORT_DATATYPE_USAGE_SP_WISE = "SELECT a.PATTERN_DESC, "
			+ " /*b.keyword*/ a.PATTERN_DESC  AS MODIFIED_PATTERN_DESC, "
			+ " COUNT(DISTINCT a.PROCEDURE_NAME) AS COUNT "
			+ " FROM pattern_results_table a,pattern_indvidual_keyword_lookup_table b "
			+ "  where run_id= ? and a.pattern_id like ? "
			+
			// "  and impacted_yn='Y' " +
			" and a.pattern_id =b.pattern_id "
			+ " GROUP BY a.PATTERN_DESC,b.keyword "
			+ " ORDER BY MODIFIED_PATTERN_DESC ";

	public static final String SELECT_CHART_REPORT_DATATYPE_USAGE_SP_WISE_COMPLEXITY = " select ? MODIFIED_PATTERN_DESC,count(*) as COUNT,'1'AS ORDER_VAL from ( "
			+ " SELECT a.procedure_name  AS MODIFIED_PATTERN_DESC, "
			+ " COUNT(*) AS COUNT "
			+ " FROM pattern_results_table a,pattern_indvidual_keyword_lookup_table b "
			+ " where run_id=? and a.pattern_id like  ?  "
			+ " and a.pattern_id =b.pattern_id "
			+ " GROUP BY a.procedure_name ) a "
			+ " where COUNT <= ? "
			+ " union "
			+ " select  ? MODIFIED_PATTERN_DESC,count(*) as COUNT,'2'AS ORDER_VAL from ( "
			+ " SELECT a.procedure_name  AS MODIFIED_PATTERN_DESC, "
			+ " COUNT(*) AS COUNT "
			+ " FROM pattern_results_table a,pattern_indvidual_keyword_lookup_table b "
			+ " where run_id=? and a.pattern_id like  ?  "
			+ " and a.pattern_id =b.pattern_id "
			+ " GROUP BY a.procedure_name ) a "
			+ " where COUNT >=? and COUNT <= ? "
			+ " union "
			+ " select  ? MODIFIED_PATTERN_DESC,count(*) as COUNT,'3'AS ORDER_VAL from ( "
			+ " SELECT a.procedure_name  AS MODIFIED_PATTERN_DESC, "
			+ " COUNT(*) AS COUNT "
			+ " FROM pattern_results_table a,pattern_indvidual_keyword_lookup_table b "
			+ " where run_id=? and a.pattern_id like ? "
			+ " and a.pattern_id =b.pattern_id "
			+ " GROUP BY a.procedure_name ) a "
			+ " where COUNT >= ? and COUNT <= ? "
			+ " union "
			+ " select  ?  MODIFIED_PATTERN_DESC,count(*) as COUNT,'4'AS ORDER_VAL from ( "
			+ " SELECT a.procedure_name  AS MODIFIED_PATTERN_DESC, "
			+ " COUNT(*) AS COUNT "
			+ " FROM pattern_results_table a,pattern_indvidual_keyword_lookup_table b "
			+ " where run_id=? and a.pattern_id like ?  "
			+ " and a.pattern_id =b.pattern_id "
			+ " GROUP BY a.procedure_name ) a "
			+ " where COUNT > ? "
			+ "order by ORDER_VAL ";

	public static final String SELECT_CHART_REPORT_DATATYPE_IMPACT_VS_NONIMPACT = " SELECT count(*) as COUNT, "
			+ "      case when impacted_yn='Y' then 'Yes' when impacted_yn='N' then 'No' end as  MODIFIED_PATTERN_DESC "
			+ "	   FROM pattern_results_table a,pattern_indvidual_keyword_lookup_table b "
			+ "	   where run_id= ?  and a.pattern_id like  ?  "
			+ "      and a.pattern_id =b.pattern_id"
			+ "      group by impacted_yn " + "	   order by COUNT";
	public static final String SELECT_CHART_REPORT_DATATYPE_USAGE_IMPACT_YN = "SELECT a.PATTERN_DESC, "
			+ " /*b.keyword*/ a.PATTERN_DESC  AS MODIFIED_PATTERN_DESC, "
			+ " COUNT(a.PROCEDURE_NAME) AS COUNT "
			+ " FROM pattern_results_table a,pattern_indvidual_keyword_lookup_table b "
			+ "  where run_id= ? and a.pattern_id like ? "
			+ "  and impacted_yn= ?  "
			+ " and a.pattern_id =b.pattern_id "
			+ " GROUP BY a.PATTERN_DESC,b.keyword "
			+ " ORDER BY MODIFIED_PATTERN_DESC ";

	/*
	 * public static final String GET_SP_BIG_SUMMARY_REPORT_PARTIAL =
	 * "SELECT COMPARE_SEQ, A.PROCEDURE_NAME, SOURCE_STATEMENT_NO, SOURCE_MODIFIED_FORMED_STATEMENT, "
	 * +
	 * " SOURCE_FORMED_STATEMENT, SOURCE_PATTERN_ID, TARGET_STATEMENT_NO, TARGET_MODIFIED_FORMED_STATEMENT, "
	 * +
	 * " TARGET_FORMED_STATEMENT, TARGET_PATTERN_ID, MISMATCH_CATEGORY, MISMATCH_CATEGORY_DESC, MATCHED_YN "
	 * +
	 * " FROM COMPARE_FORMED_STATEMENTS_SUMMARY_TABLE A,TOOL_IDENTIFY_PATTERN_SP_LIST B "
	 * + " WHERE COMPARE_SEQ= ?   " + " AND A.PROCEDURE_NAME =B.PROCEDURE_NAME "
	 * + " AND B.SEQ_NO = ?  ";
	 */

	/*
	 * public static final String GET_SP_BIG_SUMMARY_REPORT =
	 * " SELECT A.COMPARE_SEQ, A.PROCEDURE_NAME, A.SOURCE_STATEMENT_NO, A.SOURCE_MODIFIED_FORMED_STATEMENT, "
	 * +
	 * " 		 E.SOURCE_FORMED_STATEMENT, A.SOURCE_PATTERN_ID, A.TARGET_STATEMENT_NO, A.TARGET_MODIFIED_FORMED_STATEMENT, E.TARGET_FORMED_STATEMENT, "
	 * +
	 * "		 A.TARGET_PATTERN_ID, A.MISMATCH_CATEGORY, A.MISMATCH_CATEGORY_DESC, A.MATCHED_YN "
	 * +
	 * "		 ,C.PATTERN_FORMAT SOURCE_PATTERN_DESC,D.PATTERN_FORMAT TARGET_PATTERN_DESC,A.PERFORMANCE_IMPACT "
	 * +
	 * " 		 FROM COMPARE_FORMED_STATEMENTS_SUMMARY_TABLE A left join PATTERN_SCORE_TABLE C on A.SOURCE_PATTERN_ID =C.PATTERN_ID  left join PATTERN_SCORE_TABLE D on A.TARGET_PATTERN_ID =D.PATTERN_ID , "
	 * + "    COMPARE_FORMED_STATEMENTS_TABLE E " +
	 * " 		 WHERE A.COMPARE_SEQ= ? " + " and E.ORDER_NO =A.COMPARE_ORDERNO " +
	 * "  AND A.PROCEDURE_NAME =E.PROCEDURE_NAME " +
	 * " AND A.COMPARE_SEQ= E.COMPARE_SEQ " +
	 * "		 ORDER BY A.PROCEDURE_NAME,A.SOURCE_STATEMENT_NO ;";
	 */
	public static final String GET_SP_BIG_SUMMARY_REPORT = " SELECT COMPARE_SEQ,PROCEDURE_NAME, SOURCE_STATEMENT_NO, SOURCE_MODIFIED_FORMED_STATEMENT, "
			+ " SOURCE_FORMED_STATEMENT, SOURCE_PATTERN_ID, TARGET_STATEMENT_NO, TARGET_MODIFIED_FORMED_STATEMENT,"
			+ " TARGET_FORMED_STATEMENT, TARGET_PATTERN_ID, MISMATCH_CATEGORY, MISMATCH_CATEGORY_DESC, "
			+ " MATCHED_YN, CREATED_BY, CREATED_DATE, COMPARE_ORDERNO, PERFORMANCE_IMPACT ,PERFORMANCE_IMPACT_DESC"
			+ " FROM COMPARE_FORMED_STATEMENTS_SUMMARY_TABLE "
			+ " WHERE COMPARE_SEQ= ? ORDER BY PROCEDURE_NAME,SOURCE_STATEMENT_NO ;";
	
	
	public static final String GET_SP_MANUAL_MODLOG_SUMMARY_REPORT = " SELECT PROJECT_ID,PROCEDURE_NAME, STATEMENT_NO, STATEMENT "
			
			+ " FROM manually_added_code_blocks_table "
			+ " WHERE PROJECT_ID= ? ORDER BY PROCEDURE_NAME,STATEMENT_NO ;";
	
	public static final String GET_SP_CALL_TREE_FIRST_LEVEL_SUMMARY_REPORT =" select distinct a20.*, IFNULL(cc.first_level,'NA') l21"+
			" from("+
			"select a19.*, IFNULL(cc.first_level,'NA') l20"+
			" from("+
			"select a18.*, IFNULL(cc.first_level,'NA') l19"+
			" from("+
			"select a17.*, IFNULL(cc.first_level,'NA') l18"+
			" from("+
			"select a16.*, IFNULL(cc.first_level,'NA') l17"+
			" from("+
			"select a15.*, IFNULL(cc.first_level,'NA') l16"+
			" from("+
			"select a14.*,IFNULL(cc.first_level,'NA') l15"+
			" from("+
			"select a13.*, IFNULL(cc.first_level,'NA') l14"+
			" from("+
			"select a12.*, IFNULL(cc.first_level,'NA') l13"+
			" from("+
			"select a11.*, IFNULL(cc.first_level,'NA') l12"+
			" from("+
			"select a10.*, IFNULL(cc.first_level,'NA') l11"+
			" from("+
			"select a9.*, IFNULL(cc.first_level,'NA') l10"+
			" from("+
			"select a8.*, IFNULL(cc.first_level,'NA') l9"+
			" from("+
			"select a7.*, IFNULL(cc.first_level,'NA') l8"+
			" from("+
			"select a6.*, IFNULL(cc.first_level,'NA') l7"+
			" from("+
			"select a5.*, IFNULL(cc.first_level,'NA') l6"+
			" from("+
			"select a4.*, IFNULL(cc.first_level,'NA') l5"+
			" from("+
			"select a3.*, IFNULL(cc.first_level,'NA') l4"+
			" from("+
			"select a2.*, IFNULL(cc.first_level,'NA') l3"+
			" from("+
			"select a1.*, IFNULL(cc.first_level,'NA') l2"+
			" from"+
			"(select aa.l0, IFNULL(cc.first_level,'NA') l1"+
			"  from"+
			"(select distinct SUBSTRING_INDEX(SP_PROCEDURE_NAME, '.', 1) as l0"+
			"  from call_tree_first_level_data "+
			" where SUBSTRING_INDEX(SP_PROCEDURE_NAME, '.', 1) not in (select distinct first_level  from  call_tree_first_level_data) and project_id=? "+
			" )aa,call_tree_first_level_data cc where aa.l0=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1))a1 left outer join call_tree_first_level_data cc on  a1.l1=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1))a2 left outer join call_tree_first_level_data cc on  a2.l2=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a3 left outer join call_tree_first_level_data cc on  a3.l3=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a4 left outer join call_tree_first_level_data cc on  a4.l4=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a5 left outer join call_tree_first_level_data cc on  a5.l5=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a6 left outer join call_tree_first_level_data cc on  a6.l6=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a7 left outer join call_tree_first_level_data cc on  a7.l7=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a8 left outer join call_tree_first_level_data cc on  a8.l8=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a9 left outer join call_tree_first_level_data cc on  a9.l9=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a10 left outer join call_tree_first_level_data cc on  a10.l10=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a11 left outer join call_tree_first_level_data cc on  a11.l11=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a12 left outer join call_tree_first_level_data cc on  a12.l12=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a13 left outer join call_tree_first_level_data cc on  a13.l13=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a14 left outer join call_tree_first_level_data cc on  a14.l14=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a15 left outer join call_tree_first_level_data cc on  a15.l15=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a16 left outer join call_tree_first_level_data cc on  a16.l16=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a17 left outer join call_tree_first_level_data cc on  a17.l17=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a18 left outer join call_tree_first_level_data cc on  a18.l18=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a19 left outer join call_tree_first_level_data cc on  a19.l19=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
			")a20 left outer join call_tree_first_level_data cc on  a20.l20=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"; ;
	
	
	/*
	 * public static final String GET_SP_BIG_SUMMARY_REPORT_PARTIAL =
	 * " SELECT A.COMPARE_SEQ, A.PROCEDURE_NAME, A.SOURCE_STATEMENT_NO, A.SOURCE_MODIFIED_FORMED_STATEMENT,"
	 * +
	 * " E.SOURCE_FORMED_STATEMENT, A.SOURCE_PATTERN_ID, A.TARGET_STATEMENT_NO, A.TARGET_MODIFIED_FORMED_STATEMENT, E.TARGET_FORMED_STATEMENT,"
	 * +
	 * " A.TARGET_PATTERN_ID, A.MISMATCH_CATEGORY, A.MISMATCH_CATEGORY_DESC, A.MATCHED_YN"
	 * +
	 * " ,C.PATTERN_FORMAT SOURCE_PATTERN_DESC,D.PATTERN_FORMAT TARGET_PATTERN_DESC,A.PERFORMANCE_IMPACT"
	 * +
	 * "  FROM COMPARE_FORMED_STATEMENTS_SUMMARY_TABLE A left join PATTERN_SCORE_TABLE C on A.SOURCE_PATTERN_ID =C.PATTERN_ID  left join PATTERN_SCORE_TABLE D on A.TARGET_PATTERN_ID =D.PATTERN_ID ,"
	 * + " COMPARE_FORMED_STATEMENTS_TABLE E,TOOL_IDENTIFY_PATTERN_SP_LIST F" +
	 * " WHERE A.COMPARE_SEQ= ?" + " and E.ORDER_NO =A.COMPARE_ORDERNO" +
	 * " AND A.PROCEDURE_NAME =E.PROCEDURE_NAME" +
	 * " AND A.COMPARE_SEQ= E.COMPARE_SEQ" +
	 * " AND A.PROCEDURE_NAME =F.PROCEDURE_NAME " + " AND F.SEQ_NO= ? " +
	 * " ORDER BY A.PROCEDURE_NAME,A.SOURCE_STATEMENT_NO ";
	 */
	
	
	public static final String GET_SP_MANUAL_MODLOG_REPORT_PARTIAL = " SELECT A.PROJECT_ID,A.PROCEDURE_NAME, A.STATEMENT_NO, A.STATEMENT "
			+ " FROM manually_added_code_blocks_table A,TOOL_IDENTIFY_PATTERN_SP_LIST F "
			+ " WHERE A.PROJECT_ID= ? "
			+ " AND F.SEQ_NO= ? "
			+ " AND A.PROCEDURE_NAME =F.PROCEDURE_NAME "
			+ " ORDER BY A.PROCEDURE_NAME,A.STATEMENT_NO ; ";
	
	
	public static final String GET_SP_CALL_TREE_FIRST_LEVEL_REPORT_PARTIAL = " select distinct a20.*, IFNULL(cc.first_level,'NA') l21"+
								" from("+
								"select a19.*, IFNULL(cc.first_level,'NA') l20"+
								" from("+
								"select a18.*, IFNULL(cc.first_level,'NA') l19"+
								" from("+
								"select a17.*, IFNULL(cc.first_level,'NA') l18"+
								" from("+
								"select a16.*, IFNULL(cc.first_level,'NA') l17"+
								" from("+
								"select a15.*, IFNULL(cc.first_level,'NA') l16"+
								" from("+
								"select a14.*,IFNULL(cc.first_level,'NA') l15"+
								" from("+
								"select a13.*, IFNULL(cc.first_level,'NA') l14"+
								" from("+
								"select a12.*, IFNULL(cc.first_level,'NA') l13"+
								" from("+
								"select a11.*, IFNULL(cc.first_level,'NA') l12"+
								" from("+
								"select a10.*, IFNULL(cc.first_level,'NA') l11"+
								" from("+
								"select a9.*, IFNULL(cc.first_level,'NA') l10"+
								" from("+
								"select a8.*, IFNULL(cc.first_level,'NA') l9"+
								" from("+
								"select a7.*, IFNULL(cc.first_level,'NA') l8"+
								" from("+
								"select a6.*, IFNULL(cc.first_level,'NA') l7"+
								" from("+
								"select a5.*, IFNULL(cc.first_level,'NA') l6"+
								" from("+
								"select a4.*, IFNULL(cc.first_level,'NA') l5"+
								" from("+
								"select a3.*, IFNULL(cc.first_level,'NA') l4"+
								" from("+
								"select a2.*, IFNULL(cc.first_level,'NA') l3"+
								" from("+
								"select a1.*, IFNULL(cc.first_level,'NA') l2"+
								" from"+
								"(select aa.l0, IFNULL(cc.first_level,'NA') l1"+
								"  from"+
								"(select distinct SUBSTRING_INDEX(SP_PROCEDURE_NAME, '.', 1) as l0"+
								"  from call_tree_first_level_data,TOOL_IDENTIFY_PATTERN_SP_LIST f "+
								" where SUBSTRING_INDEX(SP_PROCEDURE_NAME, '.', 1) not in (select distinct first_level  from  call_tree_first_level_data) and project_id=?  and SUBSTRING_INDEX(call_tree_first_level_data.SP_PROCEDURE_NAME, '.', 1) =SUBSTRING_INDEX(f.PROCEDURE_NAME, '.', 1) and f.seq_no=?"+
								" )aa,call_tree_first_level_data cc where aa.l0=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1))a1 left outer join call_tree_first_level_data cc on  a1.l1=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1))a2 left outer join call_tree_first_level_data cc on  a2.l2=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a3 left outer join call_tree_first_level_data cc on  a3.l3=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a4 left outer join call_tree_first_level_data cc on  a4.l4=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a5 left outer join call_tree_first_level_data cc on  a5.l5=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a6 left outer join call_tree_first_level_data cc on  a6.l6=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a7 left outer join call_tree_first_level_data cc on  a7.l7=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a8 left outer join call_tree_first_level_data cc on  a8.l8=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a9 left outer join call_tree_first_level_data cc on  a9.l9=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a10 left outer join call_tree_first_level_data cc on  a10.l10=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a11 left outer join call_tree_first_level_data cc on  a11.l11=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a12 left outer join call_tree_first_level_data cc on  a12.l12=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a13 left outer join call_tree_first_level_data cc on  a13.l13=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a14 left outer join call_tree_first_level_data cc on  a14.l14=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a15 left outer join call_tree_first_level_data cc on  a15.l15=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a16 left outer join call_tree_first_level_data cc on  a16.l16=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a17 left outer join call_tree_first_level_data cc on  a17.l17=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a18 left outer join call_tree_first_level_data cc on  a18.l18=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a19 left outer join call_tree_first_level_data cc on  a19.l19=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)"+
								")a20 left outer join call_tree_first_level_data cc on  a20.l20=SUBSTRING_INDEX(cc.SP_PROCEDURE_NAME, '.', 1)";
	
	

	public static final String GET_SP_BIG_SUMMARY_REPORT_PARTIAL = " SELECT A.COMPARE_SEQ,A.PROCEDURE_NAME, SOURCE_STATEMENT_NO, SOURCE_MODIFIED_FORMED_STATEMENT, SOURCE_FORMED_STATEMENT, "
			+ " SOURCE_PATTERN_ID, TARGET_STATEMENT_NO, TARGET_MODIFIED_FORMED_STATEMENT, TARGET_FORMED_STATEMENT, TARGET_PATTERN_ID, "
			+ " MISMATCH_CATEGORY, MISMATCH_CATEGORY_DESC, MATCHED_YN,  COMPARE_ORDERNO, PERFORMANCE_IMPACT,PERFORMANCE_IMPACT_DESC "
			+ " FROM COMPARE_FORMED_STATEMENTS_SUMMARY_TABLE A,TOOL_IDENTIFY_PATTERN_SP_LIST F "
			+ " WHERE COMPARE_SEQ= ? "
			+ " AND F.SEQ_NO= ? "
			+ " AND A.PROCEDURE_NAME =F.PROCEDURE_NAME "
			+ " ORDER BY A.PROCEDURE_NAME,A.SOURCE_STATEMENT_NO ; ";
	// created by
	public static final String GET_SP_WISE_SUMMARY_REPORT = " SELECT PROCEDURE_NAME ,SUM(CASE WHEN MATCHED_YN='Y' THEN 1 ELSE 0 END)  AS MATCHED_Y,SUM(CASE WHEN MATCHED_YN='Y' THEN 0 ELSE 1 END ) AS MATCHED_N "
			+ " FROM COMPARE_FORMED_STATEMENTS_SUMMARY_TABLE "
			+ " WHERE COMPARE_SEQ= ? "
			+ " GROUP BY PROCEDURE_NAME "
			+ " ORDER BY PROCEDURE_NAME  ";

	public static final String GET_SP_WISE_SUMMARY_REPORT_PARTIAL = " SELECT A.PROCEDURE_NAME ,SUM(CASE WHEN MATCHED_YN='Y' THEN 1 ELSE 0 END)  AS MATCHED_Y,SUM(CASE WHEN MATCHED_YN='Y' THEN 0 ELSE 1 END ) AS MATCHED_N "
			+ "		 FROM COMPARE_FORMED_STATEMENTS_SUMMARY_TABLE A,TOOL_IDENTIFY_PATTERN_SP_LIST B "
			+ "		 WHERE COMPARE_SEQ= ? "
			+ " AND B.SEQ_NO = ?"
			+ "      AND  A.PROCEDURE_NAME = B.PROCEDURE_NAME "
			+ " 		 GROUP BY A.PROCEDURE_NAME "
			+ "		 ORDER BY A.PROCEDURE_NAME ";

	// for idmt details
	public static final String INSERT_IDMT_PROP_FILE_DETAILS = " INSERT INTO IDMT_PROP_FILE_DETAILS_TABLE (FILE_NAME, FILE_PATH, LINE_NO, FILE_TEXT, CREATED_BY, CREATED_DATE) "
			+ " VALUES(?,?,?,?,?,?) ";
	public static final String SELECT_IDMT_PROP_FILE_DETAILS = " SELECT FILE_NAME, FILE_PATH, LINE_NO, FILE_TEXT, CREATED_BY, CREATED_DATE FROM IDMT_PROP_FILE_DETAILS_TABLE WHERE FILE_NAME = ? ORDER BY LINE_NO";
	public static final String DELETE_IDMT_PROP_FILE_DETAILS = " DELETE FROM IDMT_PROP_FILE_DETAILS_TABLE WHERE FILE_NAME = ?";
	/*
	 * public static final String SELECT_COMPLEXITY_MATRIX_SP_DATA =
	 * " SELECT B.RUN_ID,B.PROCEDURE_NAME,B.LINE_COUNT,DISTINCT_PATTERN_ID,A.STATEMENT_PATTERNS,A.GLOBAL_VARIABLE,A.FUNCTIONS,A.DATATYPE,A.PRINT_STATEMENT,A.TEMP_TABLE,A.NESTED_STORED_PROCS FROM ( "
	 * +
	 * " SELECT RUN_ID,PROCEDURE_NAME ,COUNT(DISTINCT PATTERN_ID) DISTINCT_PATTERN_ID, SUM(CASE WHEN PATTERN_ID LIKE 'PAT%' THEN 1 ELSE 0 END)  AS 'STATEMENT_PATTERNS', "
	 * +
	 * " SUM(CASE WHEN PATTERN_ID LIKE 'GVAR%' THEN 1 ELSE 0 END)  AS 'GLOBAL_VARIABLE', "
	 * +
	 * " SUM(CASE WHEN PATTERN_ID LIKE 'FUNC%' THEN 1 ELSE 0 END)  AS 'FUNCTIONS', "
	 * +
	 * " SUM(CASE WHEN PATTERN_ID LIKE 'DTYP%' THEN 1 ELSE 0 END ) AS 'DATATYPE', "
	 * +
	 * " SUM(CASE WHEN PATTERN_ID LIKE 'KWORD_18%' THEN 1 ELSE 0 END)  AS 'PRINT_STATEMENT', "
	 * +
	 * " SUM(CASE WHEN PATTERN_ID LIKE 'PAT_40%' THEN 1 ELSE 0 END)  AS 'TEMP_TABLE', "
	 * + " SUM(CASE WHEN PATTERN_ID LIKE 'PAT_36%' THEN 1 " +
	 * "       WHEN PATTERN_ID LIKE 'PAT_35%' THEN 1 " +
	 * "       WHEN PATTERN_ID LIKE 'PAT_34%' THEN 1 " +
	 * "       ELSE 0 END ) AS 'NESTED_STORED_PROCS' " +
	 * " FROM PATTERN_RESULTS_TABLE WHERE RUN_ID= ? " +
	 * "GROUP BY PROCEDURE_NAME ) A RIGHT JOIN SP_ANALYSYS_LINE_COUNT B ON A.PROCEDURE_NAME=B.PROCEDURE_NAME AND A.RUN_ID=B.RUN_ID ORDER BY B.PROCEDURE_NAME "
	 * ;
	 */
	public static final String SELECT_COMPLEXITY_MATRIX_SP_DATA = " SELECT A.RUN_ID,A.PROCEDURE_NAME,A.LINE_COUNT,DISTINCT_PATTERN_ID,B.STATEMENT_PATTERNS,B.GLOBAL_VARIABLE,B.FUNCTIONS,B.DATATYPE,B.PRINT_STATEMENT,B.TEMP_TABLE,B.NESTED_STORED_PROCS,B.DYNAMIC_SQLS "
			+ " FROM SP_ANALYSYS_LINE_COUNT A LEFT  JOIN (SELECT RUN_ID,PROCEDURE_NAME ,COUNT(DISTINCT PATTERN_ID) DISTINCT_PATTERN_ID, SUM(CASE WHEN PATTERN_ID LIKE 'PAT%' THEN 1  WHEN PATTERN_ID LIKE 'P_DML%' THEN 1 ELSE 0 END)  AS 'STATEMENT_PATTERNS',  "
			+ "		 SUM(CASE WHEN PATTERN_ID LIKE 'GVAR%' THEN 1 ELSE 0 END)  AS 'GLOBAL_VARIABLE', "
			+ " 		 SUM(CASE WHEN PATTERN_ID LIKE 'FUNC%' THEN 1 ELSE 0 END)  AS 'FUNCTIONS', "
			+ " 		 SUM(CASE WHEN PATTERN_ID LIKE 'DTYP%' THEN 1 ELSE 0 END ) AS 'DATATYPE', "
			+ " 		 SUM(CASE WHEN PATTERN_ID LIKE 'KWORD_18%' THEN 1 ELSE 0 END)  AS 'PRINT_STATEMENT', "
			+ " 		 SUM(CASE WHEN PATTERN_ID LIKE 'PAT_40%' THEN 1 ELSE 0 END)  AS 'TEMP_TABLE', "
			+ " 		 SUM(CASE WHEN PATTERN_ID LIKE 'PAT_36%' THEN 1 "
			+ " 		       WHEN PATTERN_ID LIKE 'PAT_35%' THEN 1 "
			+ " 		       WHEN PATTERN_ID LIKE 'PAT_34%' THEN 1 "
			+ " 		       ELSE 0 END ) AS 'NESTED_STORED_PROCS' ,"
			+ " 		 SUM(CASE WHEN PATTERN_ID LIKE 'PAT_36%' THEN 1 "
			+ " 		       WHEN PATTERN_ID LIKE 'PAT_35%' THEN 1 "
			+ " 		       WHEN PATTERN_ID LIKE 'PAT_34%' THEN 1 "
			+ " 		       ELSE 0 END ) AS 'DYNAMIC_SQLS' ,"
			+ "       FOLDER_PATH "
			+ " 		 FROM PATTERN_RESULTS_TABLE "
			+ " 		GROUP BY PROCEDURE_NAME,FOLDER_PATH) B "
			+ "     ON A.PROCEDURE_NAME =B.PROCEDURE_NAME "
			+ "     AND A.RUN_ID=B.RUN_ID "
			+ "    WHERE  A.RUN_ID=  ? AND B.FOLDER_PATH = ? "
			+ " AND B.FOLDER_PATH = A.FOLDER_PATH "
			+ "     ORDER BY A.PROCEDURE_NAME ";

	/*
	 * public static final String SELECT_COMPLEXITY_MATRIX_MASTER_PARAMETRS =
	 * "SELECT DISTINCT PARAMETER " +
	 * " FROM SP_ANALYSYS_COMPLEXITY_SCORE_MASTER " + " WHERE VALID_YN='Y' " +
	 * " AND DB_MIGRATION_TYPE = ? " + " ORDER BY PARAMETER ";
	 */
	public static final String SELECT_COMPLEXITY_MATRIX_MASTER_PARAMETRS = " SELECT DISTINCT PARAMETER FROM SP_ANALYSYS_COMPLEXITY_WEIGHTAGE_MASTER WHERE DB_MIGRATION_TYPE = ? ORDER BY ORDERBY_VAL  ";

	public static final String SELECT_COMPLEXITY_MATRIX_MASTER_WEIGHTAGE = " SELECT PARAMETER ,WEIGHTAGE FROM SP_ANALYSYS_COMPLEXITY_WEIGHTAGE_MASTER WHERE DB_MIGRATION_TYPE = ?  ";
	public static final String SELECT_COMPLEXITY_MATRIX_MASTER_SCORE = " SELECT SCORE ,CATEGORY FROM SP_ANALYSYS_COMPLEXITY_SCORE_CATEGORY WHERE DB_MIGRATION_TYPE = ?  ";
	public static final String SELECT_COMPLEXITY_MATRIX_MASTER_DETAILS = " SELECT PARAMETER, OPERATOR_1_VALUE OPERATOR_1, OPERATOR_2_VALUE OPERATOR_2, CATEGORY, SCORE "
			+ " FROM SP_ANALYSYS_COMPLEXITY_SCORE_MASTER "
			+ " WHERE VALID_YN='Y' "
			+ " AND PARAMETER = ? "
			+ " AND DB_MIGRATION_TYPE = ?  " + " ORDER BY PARAMETER , SCORE";
	public static final String CREATED_BY = "TCS USER";
	public static final String DB_MIGRATION_TYPE_SYBASE_TO_DB2 = "SYSBASE_TO_DB2";
	public static final String DB_MIGRATION_TYPE_SYBASE_TO_Oracle = "SYSBASE_TO_Oracle";
	public static final String GET_MISMATCH_CATEGORY_LIST = "SELECT SEQ_NO,MISMATCH_CATEGORY, MISMATCH_CATEGORY_DESC, CREATED_BY, CREATED_DATE FROM MISMATCH_CATEGORY_DESC_TABLE WHERE DB_MIGRATION_TYPE=? AND VALID_YN='Y' ORDER BY SEQ_NO";
	public static final String GET_DETAILED_MISMATCH_PROC_WISE = "SELECT A.COMPARE_SEQ, A.PROCEDURE_NAME, A.SOURCE_STATEMENT_NO, A.SOURCE_MODIFIED_FORMED_STATEMENT,"
			+ " A.SOURCE_PATTERN_ID, A.TARGET_STATEMENT_NO, A.TARGET_MODIFIED_FORMED_STATEMENT,"
			+ " A.TARGET_PATTERN_ID, A.MISMATCH_CATEGORY, A.MISMATCH_CATEGORY_DESC, A.MATCHED_YN FROM compare_formed_statements_summary_table A WHERE A.COMPARE_SEQ=? AND A.PROCEDURE_NAME=? AND A.MISMATCH_CATEGORY LIKE ? ";

	// public static final String GET_DISTINCT_PATTERN_FOLDER_PATHS =
	// " SELECT DISTINCT FOLDER_PATH FROM PATTERN_RESULTS_TABLE WHERE RUN_ID = ?  ";
	public static final String GET_DISTINCT_PATTERN_FOLDER_PATHS = " SELECT DISTINCT SP_DISK_LOCATION FOLDER_PATH,PROCEDURE_NAME FROM TOOL_PROJECT_SP_FILE_LOCATION_DETAILS WHERE RUN_ID = ? ";
	public static final String GET_DISTINCT_SP_FOLDER_PATHS = " SELECT DISTINCT SP_DISK_LOCATION FOLDER_PATH FROM TOOL_PROJECT_SP_FILE_LOCATION_DETAILS WHERE RUN_ID = ? ";
	public static final String GET_DISTINCT_PATTERN_FOLDER_PATHS_PARTIAL = " SELECT DISTINCT SP_DISK_LOCATION FOLDER_PATH,B.PROCEDURE_NAME "
			+ " FROM TOOL_PROJECT_SP_FILE_LOCATION_DETAILS A, TOOL_IDENTIFY_PATTERN_SP_LIST B "
			+ " WHERE RUN_ID = ? "
			+ " AND B.SEQ_NO= ? "
			+ " AND A.PROCEDURE_NAME=B.PROCEDURE_NAME ";

	public static final String TOOLS_UNIX_CMD_SPLITTER = "_DBT_CMD_SPLIT_";
	public static final String GET_FRONT_END_PATTERN_RESUTLS_BIG = "SELECT DSQL_QUERY, SOURCE_FILE_PATH, SOURCE_FILE_NAME, TARGET_FILE_NAME, DSQL_LINE_NUMS_LIST, DSQL_START_KEYWORDS_LIST, CONVERTION_STATUS, DSQL_INVOKED_LINE_NUM, FRONT_END_VAR_NAME, ORIGINAL_DSQL_QUERY"
			+ "FROM  front_end_dsql_details_table WHERE PROJECT_ID=?;";

	public static final String GET_FRONT_END_DSQL_DETAILS = " SELECT  PROJECT_ID, RUN_SEQ, DSQL_QUERY, SOURCE_FILE_PATH, SOURCE_FILE_NAME, TARGET_FILE_NAME, DSQL_LINE_NUMS_LIST, DSQL_START_KEYWORDS_LIST, CONVERTION_STATUS, DSQL_INVOKED_LINE_NUM, FRONT_END_VAR_NAME,ORIGINAL_DSQL_QUERY "
			+ " FROM FRONT_END_DSQL_DETAILS_TABLE WHERE PROJECT_ID=? /*AND RUN_SEQ=? AND SOURCE_FILE_PATH=? */";
	public static final String GET_FRONT_END_DSQL_TARGET_MAP_DETAILS = "SELECT ORIGINAL_DSQL_QUERY, SOURCE_FILE_PATH, SOURCE_FILE_NAME, CONVERTED_DSQL_QUERY, CONVERTION_STATUS "
			+ " FROM front_end_dsql_target_map_table WHERE PROJECT_ID=? /*AND RUN_SEQ=? AND SOURCE_FILE_PATH=? */";

	public static final String GET_UPLOAD_STATUS_DETAILS = "SELECT RUN_ID, USER_ID, CURRENT_STAGE, STATUS_MSG, MSG_TYPE, CREATED_DATE FROM CURRENT_PROCESS_STATUS_TABLE WHERE RUN_ID =? order by created_date";

	public static final String REMOVE_CURRENT_STATUS_DETAILS = "DELETE FROM CURRENT_PROCESS_STATUS_TABLE WHERE RUN_ID =?";

	public static final String INSERT_DATA_MIGRATION_ERROR_REPORT = " INSERT INTO TOOL_DATA_MIGRATION_ERROR_REPORT (PROJECT_ID, TABLE_NAME, ERROR_TEXT, CREATED_BY, CREATED_DATE ) VALUES(?,?,?,?,?)";
	public static final String GET_SOURCE_TARGET_SCHEMAS = "select SOURCE_SCHEMA, TARGET_SCHEMA from TOOL_PROJECT_SCHEMA_DETAILS WHERE PROJECT_ID=?";
	public static final String GET_SYBASE_SYS_OBJECTS = "select SOURCE_DB_OBJ, TARGET_DB_OBJ from SYSTEM_DB_OBJECTS_INFO WHERE DB_MIGRATION_TYPE=?";
	public static final String INSERT_POST_PROCESSOR_CHANGES_TRACKER_DETAILS = "INSERT INTO POST_PROCESSOR_CHANGES_TRACKER_TABLE( PROJECT_ID, PROCEDURE_NAME, LINE_N0, BLOCK_N0, OLD_TEXT, NEW_TEXT, CODE_TYPE, CREATED_BY, CREATED_DATE,BEFORE_BLOCK,AFTER_BLOCK) "
			+ "	 VALUES(?,?,?,?,?,?,?,?,?,?,?);";
	/*
	 * public static final String GET_POST_PROCESSOR_CHANGES_TRACKER_DETAILS=
	 * "SELECT PROJECT_ID, PROCEDURE_NAME, LINE_N0, BLOCK_N0, OLD_TEXT, NEW_TEXT, CODE_TYPE, BEFORE_BLOCK,AFTER_BLOCK"
	 * +
	 * " FROM POST_PROCESSOR_CHANGES_TRACKER_TABLE WHERE PROJECT_ID=? AND PROCEDURE_NAME= ?  ;"
	 * ;
	 */
	public static final String GET_POST_PROCESSOR_CHANGES_TRACKER_DETAILS = "SELECT PROJECT_ID, PROCEDURE_NAME, LINE_N0, BLOCK_N0, OLD_TEXT, NEW_TEXT, CODE_TYPE, BEFORE_BLOCK,AFTER_BLOCK"
			+ " FROM POST_PROCESSOR_CHANGES_TRACKER_TABLE WHERE PROJECT_ID=? AND PROCEDURE_NAME= ?   ORDER BY ROW_ID;";
	public static final String DELETE_POST_PROCESSOR_CHANGES_TRACKER_DETAILS = "DELETE FROM POST_PROCESSOR_CHANGES_TRACKER_TABLE WHERE PROJECT_ID = ? AND PROCEDURE_NAME = ?";

}
