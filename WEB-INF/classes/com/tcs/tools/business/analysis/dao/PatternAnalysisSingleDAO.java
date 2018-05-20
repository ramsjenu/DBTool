package com.tcs.tools.business.analysis.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import sun.util.logging.resources.logging;

import com.tcs.tools.business.analysis.dto.PatternIndividualKeywordScoreDTO;
import com.tcs.tools.business.analysis.dto.PatternScoreDTO;
import com.tcs.tools.business.analysis.dto.PatternSummaryDTO;
import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.dto.StoredProceduresDetailsDTO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;


public class PatternAnalysisSingleDAO {
	
	 private Connection lConnection= null;
	 private PreparedStatement  lPreparedStatement = null;
	 
	 private ResultSet lResultSet = null;
	 private HashMap lScoreMap = null;
	 private List lWholeScorePlatternList =null; 
	 private HashMap lProcCountMap = null;
         private List lWholeKeywordScorePlatternList = new ArrayList();
         private int lIncrementBatch = 0;
         private List lConstructsList = new ArrayList();
         
         private   HashMap lExceptionPatternMap = new HashMap();
         
         public PatternAnalysisSingleDAO(){
        	 //getParsedDataPrepare();
         }
         public PatternAnalysisSingleDAO(String pPrepare){
        	 getParsedDataPrepare();
         }
         String lCurState="";
		 String lStausMsg="";
         
         
	/**
     * this method for getting the pattern value for the DB
     * @param pPramQueryString
     * @return
     */
    public PreparedStatement prepareInsertStatement(String lRunid,String lStatement,String lStatementNo,StoredProceduresDetailsDTO pStoredProceduresDetailsDTO /*String lProcedureName*/,int score,String lStatementType,String lConcatenatedValue ,String pQueryCount,PreparedStatement pPreparedStatement,String pDbMigrationType)
    {
        PatternSummaryDTO lPatternSummaryDTO = new PatternSummaryDTO();
        PatternScoreDTO lPatternScoreDTO = new PatternScoreDTO();
        //PreparedStatement pPreparedStatement = null;
        lPatternSummaryDTO.setRunId(lRunid);
        lPatternSummaryDTO.setStatement(lStatement);                        	
        lPatternSummaryDTO.setStatementNo(lStatementNo);
        lPatternSummaryDTO.setProcedureName(pStoredProceduresDetailsDTO.getProcName());        
        //lPatternSummaryDTO.setKeyWord(lResultSet.getString("STATEMENT_TYPE"));
        lPatternSummaryDTO.setFormedStatement(lConcatenatedValue);
        //lPatternSummaryDTO.setFormedStatement("");
        lPatternSummaryDTO.setScore(score+"");
        //System.out.println(":::score::::"+score+":::statement no::::"+lStatementNo);
        //for gettin the pattern id for a score
        lPatternScoreDTO = getSinglePatternScoreData(score+"");
        lPatternSummaryDTO.setPatternId(lPatternScoreDTO.getPatternId());
        lPatternSummaryDTO.setPatternDesc(lPatternScoreDTO.getPatternFormat());
        lPatternSummaryDTO.setKeyWord(lStatementType);
        lPatternSummaryDTO.setQueryCount(pQueryCount);
        lPatternSummaryDTO.setDbMigrationType(pDbMigrationType);
        lPatternSummaryDTO.setFolderPath(pStoredProceduresDetailsDTO.getFolderPath());
        
        return insertSummary(lPatternSummaryDTO,lConnection,pPreparedStatement);
    
    }
    
    //method not used
    public void getParsedDataPrepare(){
    	PreparedStatement lPreparedStatementBigSelect = null;
    	ResultSet lResultSetBigRs = null; 
    	try{
    		lConnection = DBConnectionManager.getConnection();
            lScoreMap = getScoreList(lConnection,"");            
            lWholeScorePlatternList = getWholePatternScoreData(lConnection,"");
            lWholeKeywordScorePlatternList = getWholeKeyWordScoreData(lConnection,"");
         
            
    		lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_ALL_CONSTRUCTS);
            ResultSet rs=lPreparedStatement.executeQuery();
            while(rs.next())
            {
            	//System.out.println("::::Keyword:::"+rs.getString(2));            	
            	lConstructsList.add(rs.getString(2).trim().toUpperCase());
            	
            }
    		  lConnection = DBConnectionManager.getConnection();
    		  
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		 DBConnectionManager.closeConnection(lConnection);
    	}
    }
    
    //migration type is passed as parameter
    public void getParsedDataWhole(String pRunId,String pDbMigrationType,String pFolderPath){
    	System.out.println(":::::getParsedDataWhole:::::->pRunId"+pRunId+"---"+pDbMigrationType+"---"+pFolderPath);
    	PreparedStatement lPreparedStatementBigSelect = null;
    	ResultSet lResultSetBigRs = null; 
    	try{
    		lConnection = DBConnectionManager.getConnection();
            lScoreMap = getScoreList(lConnection,pDbMigrationType);
            lProcCountMap = getProcRowCount(pRunId,lConnection);
            lWholeScorePlatternList = getWholePatternScoreData(lConnection,pDbMigrationType);
            lWholeKeywordScorePlatternList = getWholeKeyWordScoreData(lConnection,pDbMigrationType);
            lExceptionPatternMap =  getExceptionPatternData( lConnection, pDbMigrationType);
            
    		lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_ALL_CONSTRUCTS);
    		lPreparedStatement.setString(1, pDbMigrationType);
            ResultSet rs=lPreparedStatement.executeQuery();
            while(rs.next())
            {
            	//System.out.println("::::Keyword:::"+rs.getString(2));            	
            	lConstructsList.add(rs.getString(2).trim().toUpperCase());
            	
            }
            
    		  lConnection = DBConnectionManager.getConnection();
    		  /*String lSelectQuery="SELECT distinct procedure_name FROM parse_results_table p where run_id=?";
    		  lPreparedStatementBigSelect = lConnection.prepareStatement(lSelectQuery);
    		  lPreparedStatementBigSelect.setString(1, pRunId);
    		  lResultSetBigRs = lPreparedStatementBigSelect.executeQuery();
    		  if(lResultSetBigRs != null){
    			  while(lResultSetBigRs.next()){
    				  getParsedDataSybaseToDb2(pRunId,lResultSetBigRs.getString(1),pDbMigrationType);
    			  }
    		  }*/
    		  
    		  List lSPNameList = ToolsUtil.getFileNamesFromFolderDTO(new File(pFolderPath),new ArrayList());
    		  System.out.println(":::list contents getParsedDataWhole method::::"+lSPNameList.toString());
    		  StoredProceduresDetailsDTO lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
    		  if(lSPNameList != null && lSPNameList.size() >0){
    			  for (int i = 0; i < lSPNameList.size(); i++) {
    				  //getParsedDataSybaseToDb2(pRunId,(String)lSPNameList.get(i),pDbMigrationType);
    				  lStoredProceduresDetailsDTO = (StoredProceduresDetailsDTO)lSPNameList.get(i);
    				  
    				  // int spLoc=
    						   insertSPSourceLocation( pRunId , lStoredProceduresDetailsDTO.getProcName() , lStoredProceduresDetailsDTO.getFolderPath() , ToolConstant.CREATED_BY);
    				/*   if(spLoc>0){
    					   File myDir = new File(lStoredProceduresDetailsDTO.getFolderPath());
    					   File[] myFiles = myDir.listFiles();
    					   for (int c = 0; c < myFiles.length; c++)
    			            {      
    						   System.out.println("processing file getParsedDataWhole method: " + myDir.getAbsolutePath()+ "\\" + myFiles[i]);
    			            }
    				   }*/
    				   //Update Status to Front End - Start
    				   lCurState="Pattern Analysis";
    				   lStausMsg="File Name::->"+lStoredProceduresDetailsDTO.getProcName();
    				   ToolsUtil.prepareInsertStatusMsg( ToolsUtil.getProjectId(pRunId), ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
    				   //Update Status to Front End - End
    				   
    				  if(pDbMigrationType.equalsIgnoreCase("SYBASE_TO_SQL")){
    					  //getParsedDataSybaseToSQLServer(pRunId,lResultSetBigRs.getString(1),pDbMigrationType);
    					  getParsedDataSybaseToSQLServer(pRunId,lStoredProceduresDetailsDTO,pDbMigrationType);
    				  }
    				  
    				  if(pDbMigrationType.equalsIgnoreCase("SYSBASE_TO_Oracle")){
    					  getParsedDataSybasetoOracle(pRunId,lStoredProceduresDetailsDTO,pDbMigrationType);
    					  //getParsedDataSybaseToDb2(pRunId,lStoredProceduresDetailsDTO,pDbMigrationType);
    				  }
    				  if(pDbMigrationType.equalsIgnoreCase("SYSBASE_TO_DB2") ){
    					 // getParsedDataSybaseToDb2(pRunId,lResultSetBigRs.getString(1),pDbMigrationType);
    					  getParsedDataSybaseToDb2(pRunId,lStoredProceduresDetailsDTO,pDbMigrationType);
    				  }

				}
    		  }
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		 DBConnectionManager.closeConnection(lConnection);
    	}
    }
    public List getParsedDataSybaseToDb2(String pRunId,StoredProceduresDetailsDTO pStoredProceduresDetailsDTO /*String pProcName*/,String pDbMigrationType){
        
    	System.out.println("::::::pProcName:::"+pStoredProceduresDetailsDTO.getProcName());
       // String retValue="";  
       // boolean lCanCaoncatenate= false;
       // List lConcatenatedList = new ArrayList();
        List lScoreList = new ArrayList();
       // List lSummaryList = new ArrayList();
        
        
        PatternSummaryDTO lPatternSummaryDTO = new PatternSummaryDTO();
       // PatternSummaryDTO lPatternSummaryDTOTemp = new PatternSummaryDTO();
       // PatternScoreDTO lPatternScoreDTO = new PatternScoreDTO();
        PatternIndividualKeywordScoreDTO lPatternIndividualKeywordScoreDTO = null;
        
        PreparedStatement pPreparedStatement = null;
        String lStatementNo ="";
        String lStatement = "";
        String lProcedureName="";
        String lPrevPattern="";
        List lConstructsList = new ArrayList();       
        int lNumofStatements=1;       
        List lIgnoreConstructsList =new ArrayList();
        
        try{
            
        	lConnection.setAutoCommit(false);
        	
        	pPreparedStatement = lConnection.prepareStatement(ToolConstant.PATTERN_ANALYSYS_INSERT_SUMMARY);
        	
        	List lMultiStringList = new ArrayList();
        	lMultiStringList.add((String)"STR");
        	lMultiStringList.add((String)"OUTER");
        	lMultiStringList.add((String)"=");
        	lMultiStringList.add((String)"!=");
        	lMultiStringList.add((String)"!>");
        	lMultiStringList.add((String)"!<");
        	lMultiStringList.add((String)"IS");
        	lMultiStringList.add((String)"IS NOT");
        	lMultiStringList.add((String)"NOT");
        	lMultiStringList.add((String)"CONVERT");
        	lMultiStringList.add((String)"(");
        	lMultiStringList.add((String)"CONVERT (");
        	lMultiStringList.add((String)"= CONVERT (");
        	
        	
        	List lMultiStringIgnoreAndInsertList = new ArrayList();
        	lMultiStringIgnoreAndInsertList.add((String)"CONVERT");
        	
        	HashMap lIgnoreConstructsHm= new HashMap();
        	lIgnoreConstructsHm.put((String)"CASE", (String)"END");
        	
        	
        	
           
            StringBuffer lConcatenatedValue= new StringBuffer();
            //select the data from the table
            //if required we can add % in the like operator
            //lConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //lPreparedStatement = lConnection.prepareStatement(ToolConstant.PATTERN_ANALYSYS_GET_PARSED_DATA,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //System.out.println(":::::before executing select:::::");
            String lSql = " SELECT "
    			+ " RUN_ID,PROCEDURE_NAME,STATEMENT,STATEMENT_NO,ACTUAL_PATTERN,STATEMENT_TYPE,ORDER_NO "
    			+ " FROM PARSE_RESULTS_TABLE " + " WHERE RUN_ID= ?  and PROCEDURE_NAME =? "
    			+ " ORDER BY  PROCEDURE_NAME,ORDER_NO ";
            lPreparedStatement = lConnection.prepareStatement(lSql);
            
            lPreparedStatement.setString(1, pRunId);
            lPreparedStatement.setString(2, pStoredProceduresDetailsDTO.getProcName());
            //System.out.println("::::pRunId::::"+pRunId+"$$$$");
            lResultSet = lPreparedStatement.executeQuery();
            //System.out.println(":::::after executing select:::::");
            //lResultSet.last();
            // int lRowCount = lResultSet.getRow();            
            // System.out.println(":::::res size::::"+lRowCount);
            // lResultSet.first();
            
            lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_ALL_CONSTRUCTS);
            lPreparedStatement.setString(1, pDbMigrationType);
            ResultSet rs=lPreparedStatement.executeQuery();
            while(rs.next())
            {
            	//System.out.println("::::PATTERN:::"+rs.getString("ACTUAL_PATTERN"));            	
            	lConstructsList.add(rs.getString(2).trim().toUpperCase());
            	
            }

           
           // int i=0;
            int score=0;
            String lActualPattern ="";
            String lProcNameFirst ="";
            int procCount =0;
            String lStatementNoLatest ="";
            String lStatementLatest ="";
            String lStatementType="";
            String lOrdNum="";
            String lPrevProcName="";
            String lChkStatementType="";
            int lChkPrevStat=0;
            String lRunid="";
            String lPrevStatementType="";
            boolean lIsUpdateVisited=false;            
            
            //System.out.println("::::while loop starting");
            //boolean lIsPrevStst=false;
            int count=0;
            lPatternSummaryDTO = new PatternSummaryDTO();
            String lNeedToCheckNextToken="";
            boolean lKeywordDataInserted = false;
            if (lResultSet != null){               
                while(lResultSet.next()){
                       
                	lRunid=lResultSet.getString("RUN_ID");
                	lProcNameFirst = lResultSet.getString("PROCEDURE_NAME");
                        //System.out.println("lProcNameFirst------>"+lProcNameFirst);
                	procCount = Integer.parseInt((String)lProcCountMap.get(lProcNameFirst)); 
                	//System.out.println("::::inside result set:::i val->"+i+"count Value->"+procCount);
                        lActualPattern =lResultSet.getString("ACTUAL_PATTERN");
                      System.out.println("actual pattern:::::::::::::::::::: "+lActualPattern);
                        lStatementNoLatest  = lResultSet.getString("STATEMENT_NO");
                        lStatementLatest = lResultSet.getString("STATEMENT");
                        lStatementType  = lResultSet.getString("STATEMENT_TYPE");
                        lOrdNum  = lResultSet.getString("ORDER_NO");  
                        
                        
                        
                        
                        //for spaced keyword    - start
                        lKeywordDataInserted = false;
                        if(lMultiStringList.contains((String)lActualPattern.toUpperCase())){
                        	if("".equals(lNeedToCheckNextToken)){
                        		lNeedToCheckNextToken = lActualPattern.toUpperCase();
                        	}else{
                        		lNeedToCheckNextToken = lNeedToCheckNextToken+" "+lActualPattern.toUpperCase();
                        	}
                        	//System.out.println(":::;;;first addition:::"+lNeedToCheckNextToken);
                        }else{
                        	//System.out.println(":::inside else::::"+lActualPattern);
                        	if(lNeedToCheckNextToken != null && (!"".equalsIgnoreCase(lNeedToCheckNextToken))){
                        		lNeedToCheckNextToken = lNeedToCheckNextToken+" "+lActualPattern.toUpperCase();
                        		//System.out.println(":::;;;lNeedToCheckNextToken:::"+lNeedToCheckNextToken+"$$$$$$$$$");
                        		lPatternIndividualKeywordScoreDTO = getSingleKeywordScoreData(lNeedToCheckNextToken);
                        		if(lPatternIndividualKeywordScoreDTO != null){
                        			//System.out.println(":::::found 1 variable::::"+lNeedToCheckNextToken);
                        			lPatternSummaryDTO = new PatternSummaryDTO();
                                    lPatternSummaryDTO.setRunId(pRunId);
                                    lPatternSummaryDTO.setPatternId(lPatternIndividualKeywordScoreDTO.getPatternId());
                                    lPatternSummaryDTO.setPatternDesc(lPatternIndividualKeywordScoreDTO.getPatternDesc());
                                    lPatternSummaryDTO.setScore(lPatternIndividualKeywordScoreDTO.getScore());
                                    lPatternSummaryDTO.setKeyWord(lPatternIndividualKeywordScoreDTO.getKeyWord());
                                    lPatternSummaryDTO.setStatement(lStatementLatest); 
                                    lPatternSummaryDTO.setFormedStatement(lPatternIndividualKeywordScoreDTO.getKeyWord());
                                    lPatternSummaryDTO.setStatementNo(lStatementNoLatest);
                                    lPatternSummaryDTO.setProcedureName(lProcNameFirst);  
                                    lPatternSummaryDTO.setQueryCount("0");
                                    lPatternSummaryDTO.setDbMigrationType(pDbMigrationType);
                                    lPatternSummaryDTO.setFolderPath(pStoredProceduresDetailsDTO.getFolderPath());
                                    pPreparedStatement = insertSummary(lPatternSummaryDTO,lConnection , pPreparedStatement);
                                    lKeywordDataInserted = true;
                                    
                        		}
                        	}
                        	if(lActualPattern != null && (!"".equals(lActualPattern))){
                        		lNeedToCheckNextToken ="";
                        	}
                        }
                        //System.out.println(":::::::::lNeedToCheckNextToken:::::"+lNeedToCheckNextToken);
                        //for spaced keyword     - end
                        
                        
                        if(count==0)
                        {
                            lPrevProcName=lProcNameFirst;
                        }
                        //System.out.println("::::lStatementNoLatest:::"+lStatementNoLatest+"::::lActualPattern::::"+lActualPattern+"::::lNeedToCheckNextToken::"+lNeedToCheckNextToken+"::::lKeywordDataInserted::::"+lKeywordDataInserted);
                        if(("".equals(lNeedToCheckNextToken) && (lKeywordDataInserted == false)) ||(lMultiStringIgnoreAndInsertList.contains((String)lActualPattern.toUpperCase()))){
                       //if("".equals(lNeedToCheckNextToken) && (lKeywordDataInserted == false)){
                	lPatternIndividualKeywordScoreDTO = getSingleKeywordScoreData(lActualPattern);
                	
                        if((lPatternIndividualKeywordScoreDTO != null)  ){
                            //System.out.println(":::::dto key word value not null::::::"+lPatternIndividualKeywordScoreDTO.getKeyWord()+","+lPatternIndividualKeywordScoreDTO.getPatternDesc()+","+lPatternIndividualKeywordScoreDTO.getPatternId()+","+lPatternIndividualKeywordScoreDTO.getScore() );
                            lPatternSummaryDTO = new PatternSummaryDTO();
                            lPatternSummaryDTO.setRunId(pRunId);
                            lPatternSummaryDTO.setPatternId(lPatternIndividualKeywordScoreDTO.getPatternId());
                            lPatternSummaryDTO.setPatternDesc(lPatternIndividualKeywordScoreDTO.getPatternDesc());
                            lPatternSummaryDTO.setScore(lPatternIndividualKeywordScoreDTO.getScore());
                            lPatternSummaryDTO.setKeyWord(lPatternIndividualKeywordScoreDTO.getKeyWord());
                            lPatternSummaryDTO.setStatement(lStatementLatest);                        	
                            lPatternSummaryDTO.setStatementNo(lStatementNoLatest);
                            lPatternSummaryDTO.setProcedureName(lProcNameFirst);  
                            lPatternSummaryDTO.setFormedStatement(lPatternIndividualKeywordScoreDTO.getKeyWord());
                            lPatternSummaryDTO.setQueryCount("0");
                            lPatternSummaryDTO.setDbMigrationType(pDbMigrationType);
                            lPatternSummaryDTO.setFolderPath(pStoredProceduresDetailsDTO.getFolderPath());
                            pPreparedStatement = insertSummary(lPatternSummaryDTO,lConnection , pPreparedStatement);
                            //score=0;

                           
                        }
                       }
                        //else
                        //{
                        	//System.out.println("1: "+lActualPattern+"  Score"+score);
                            
                            if(!lPrevProcName.equalsIgnoreCase(lProcNameFirst.trim()))
                            {
                                //System.out.println("File Changed");
                                lPrevProcName=lProcNameFirst;
                                if(score>0)
                                {
                                    //System.out.println("2::::dto pat lStatementNo:::"+lStatementNo+"::pattern id::"+lPatternScoreDTO.getPatternId()+"::::lStatement::"+lStatement+"::::Score:::"+score+"::lChkStatementType"+lChkStatementType);
                                    pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
                                    
                                    
                                }
                                score=0;
                                lConcatenatedValue=new StringBuffer();
                                lStatementNo = "" ;
                                lStatement = "" ;
                                lProcedureName = "";    
                                lChkStatementType="";
                                lIgnoreConstructsList = new ArrayList();
                            }
                            
                           
                            
                            //Checking for SET To Determine whether its a new statement or its a part of update
                            
                            if(lActualPattern.trim().equalsIgnoreCase("UPDATE")){
                            	lIsUpdateVisited=true;
                            }
                            
                            if( lActualPattern.trim().equalsIgnoreCase("SET")){
                            	if(lIsUpdateVisited==false){
                            		Pattern lDeclareContHandlerPattern = Pattern.compile("\\bDECLARE\\b\\s+\\bCONTINUE\\b\\s+\\bHANDLER\\b");
                        			Matcher lPatternMatch= lDeclareContHandlerPattern.matcher(lConcatenatedValue.toString().toUpperCase()); 
                        			if(!lPatternMatch.find()){
                        				//System.out.println("lActualPattern::-> "+lActualPattern+" ::Statement No:::->"+lStatementNoLatest);
                        				lStatementType=ToolConstant.STMT_TYPE_STATEMENT;
                        			} 
                        		}else{
                            		lIsUpdateVisited=false;
                            	}
                            	//System.out.println("Chcking for Set::-> "+lConcatenatedValue.toString());
                            	
                            }
                        	
                            if(ToolConstant.STMT_TYPE_STATEMENT.equalsIgnoreCase(lStatementType.trim()))
                            {
                            	
                            	
                            	//System.out.println("Select::::SCore;;;;;;;---->"+score);
                                if(score>0)
                                {                                	
                                	//Checking for multiple statements
                                	if(lActualPattern.trim().equalsIgnoreCase("select"))
                                	{
                                		//System.out.println(":LActualPattern:->"+lActualPattern+":lPrevPattern:-> "+lPrevPattern);
                                		Pattern p1 = Pattern.compile("\\bUNION\\b[\\s]+\\bALL\\b");
                            			Matcher m1= p1.matcher(lConcatenatedValue.toString().toUpperCase()); 
                            			
                                		if(lPrevPattern.trim().equalsIgnoreCase("(") || lPrevPattern.trim().equalsIgnoreCase(",") || (m1.find() && lPrevPattern.trim().equalsIgnoreCase("all")) )
                                		{     
                                			Pattern p = Pattern.compile("(\\s+if\\b[\\s\\w\\W]*\\bexists\\s*\\()");
                                			Matcher m= p.matcher(lConcatenatedValue.toString().toLowerCase()); 
                                			Pattern p3 = Pattern.compile("(\\s+elseif\\b[\\s\\w\\W]*\\bexists\\s*\\()");
                                			Matcher m3= p3.matcher(lConcatenatedValue.toString().toLowerCase()); 
                                			
                                			if(m.find()|m3.find())
                                			{
                                				//System.out.println("::lNumofStatements= "+lNumofStatements+"  -p1::::lConcatenatedValue;;;;;;;---->"+lConcatenatedValue);
                                				pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
                                				lConcatenatedValue=new StringBuffer();
                                				score=0;
                                				lIgnoreConstructsList = new ArrayList();
                                				lNumofStatements=1;
                                			}
                                			else
                                			{
                                				lNumofStatements++;
                                			}
                                			
                                			
                                			//System.out.println("0:LActualPattern:->"+lActualPattern+":lPrevPattern:-> "+lPrevPattern);
                                		}
                                		else
                                		{
                                			//System.out.println("::Select Else::--->"+lConcatenatedValue);
                                			if((lPrevStatementType.equalsIgnoreCase("insert") && (score==(getHmScoreData("insert")+getHmScoreData("into"))))||
                                					(lPrevStatementType.equalsIgnoreCase("insert") && (score==(getHmScoreData("insert")))) ||
                                				( lPrevStatementType.equalsIgnoreCase("declare") && (score==(getHmScoreData("declare")+getHmScoreData("cursor")))))
                                			{
                                				lNumofStatements++;
                                				
                                			}
                                			else /*if(!lPrevStatementType.trim().equalsIgnoreCase("declare"))*/
                                			{
                                				//System.out.println("::lNumofStatements= "+lNumofStatements+"  -1::::lConcatenatedValue;;;;;;;---->"+lConcatenatedValue);
                                    			//System.out.println("1::lRunid:"+lRunid+"::lStatementNo:"+lStatementNo+"::lStatement:"+lStatement+"::lProcedureName::"+lProcedureName+" :Score::"+score+"::lChkStatementType"+lChkStatementType);                                				
                                				pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
                                                score=0; 
                                                lIgnoreConstructsList = new ArrayList();
                                                lNumofStatements=1;
                                                lConcatenatedValue= new StringBuffer();                                                
                                			}
                                			/*else
                                			{
                                				score=0;
                                				lIgnoreConstructsList = new ArrayList();
                                				lNumofStatements=1;
                                				lConcatenatedValue= new StringBuffer();
                                			}*/
                                			                              			
                                		}
                                		//System.out.println("::::: lActualPattern ::->"+lActualPattern);
                                		
                                	}
                                	else /*if(!lPrevStatementType.trim().equalsIgnoreCase("declare"))*/
                                	{
                                		//System.out.println("::lNumofStatements= "+lNumofStatements+" -2::::lConcatenatedValue;;;;;;;---->"+lConcatenatedValue);
                                		//System.out.println("2::lRunid:"+lRunid+"::lStatementNo:"+lStatementNo+"::lStatement:"+lStatement+"::lProcedureName::"+lProcedureName+" :Score::"+score+"::lChkStatementType"+lChkStatementType);                         		
                                		pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
                                		 score=0;
                                		 lIgnoreConstructsList = new ArrayList();
                                		 lNumofStatements=1;
                                		lConcatenatedValue= new StringBuffer();
                                       
                                	}
                                	/*else
                        			{
                        				score=0;
                        				lIgnoreConstructsList = new ArrayList();
                        				lNumofStatements=1;
                        				lConcatenatedValue= new StringBuffer();
                        			} */                             	
                                	
                                	/////                                                                       
                                    
                                    //lChkPrevStat+=2;                                
                                }
                                lStatementNo = lStatementNoLatest ;
                                lStatement = lStatementLatest ;
                                lProcedureName = lProcNameFirst;    
                                lChkStatementType=lStatementType;
                                lPrevStatementType=lActualPattern;
                                
                                score = score + getHmScoreData(lActualPattern);
                                lConcatenatedValue.append(" "+lActualPattern+" ");
                                lPrevPattern=lActualPattern;
                                //System.out.println("1: "+lActualPattern+"  Score"+score);
                                //lIsPrevStst=true;                            
                            }
                            else if(score>0)
                            {
                            	
                            	//System.out.println("lActualPattern ::->"+lActualPattern+"  ::lIgnoreConstructsList size:::->"+lIgnoreConstructsList.size());
                            	///////
                            	if(lIgnoreConstructsList.size()>0){
                            		if(lConstructsList.contains(lActualPattern.trim().toUpperCase())){
                            			//System.out.println("HampValue::->"+ToolsUtil.replaceNull((String)lIgnoreConstructsHm.get( ((String)lIgnoreConstructsList.get(lIgnoreConstructsList.size()-1)).toUpperCase())));
                            			if((ToolsUtil.replaceNull((String)lIgnoreConstructsHm.get( ((String)lIgnoreConstructsList.get(lIgnoreConstructsList.size()-1)).toUpperCase()  ))).equalsIgnoreCase(lActualPattern.trim())){
                            				lIgnoreConstructsList.remove(lIgnoreConstructsList.size()-1);
                            				
                            				//lIgnoreConstructsHm.get(lPrevConstructsList.get(lPrevConstructsList.size()-1));                            				
                            			}
                            			else
                            			{	
                            				if(lIgnoreConstructsHm.containsKey(lActualPattern.trim())){
                            				lIgnoreConstructsList.add(lActualPattern.trim());
                            				
                            				}
                            				
                            			}
                                	}
                            			score = score + getHmScoreData(lActualPattern);
                                		lConcatenatedValue.append(" "+lActualPattern+" ");
                            		
                            		
                            		
                            		
                            	}else
                            	{
                            		if(lConstructsList.contains(lActualPattern.trim().toUpperCase())){
                            			
                            			//System.out.print(lIgnoreConstructsHm.containsKey(lActualPattern.trim().toUpperCase())+"\n");
                            			
                            			if(lIgnoreConstructsHm.containsKey(lActualPattern.trim().toUpperCase())){
                            				lIgnoreConstructsList.add(lActualPattern.trim());
                            				score = score + getHmScoreData(lActualPattern);
                                    		lConcatenatedValue.append(" "+lActualPattern+" ");
                            			}else{
                            				/*if(!((lPrevStatementType.trim().equalsIgnoreCase("declare"))&& (score==(getHmScoreData("declare"))))){
                            					pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,lProcedureName,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement);
                            				}*/
                            				pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
                                   		 	score=0;
                                   		 	lIgnoreConstructsList = new ArrayList();
                                   		 	lNumofStatements=1;
                                   		 	lConcatenatedValue= new StringBuffer();                            			
                            			}                            				
                            			
                                	}
                            		else{
                            			//if(getHmScoreData(lActualPattern)>0)
                                		score = score + getHmScoreData(lActualPattern);
                                		lConcatenatedValue.append(" "+lActualPattern+" ");
                            			
                            		}
                            		
                            	}
                            	
                            	/////
                            	
                            	/*if(!lConstructsList.contains(lActualPattern.trim().toUpperCase()))
                            	{
                            		//if(getHmScoreData(lActualPattern)>0)
                            		score = score + getHmScoreData(lActualPattern);
                            		lConcatenatedValue.append(" "+lActualPattern+" ");
                                    
                            	}*/
                            	lPrevPattern=lActualPattern;
                            	
                            }
                            
                        //}
                        
                       
                count++;
               
                }
                if((score>0 ))//&& (!lPrevStatementType.trim().equalsIgnoreCase("declare")))
                {
                	
                	//System.out.println("::lNumofStatements= "+lNumofStatements+" -::::3ConcatenatedValue;;;;;;;---->"+lConcatenatedValue);                	
        			pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
        			lConcatenatedValue=new StringBuffer();
        			score=0;
        			lIgnoreConstructsList = new ArrayList();
        			lNumofStatements=1;
    				//pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,lProcedureName,score,lChkStatementType,lstr,pPreparedStatement);
                    
                }
            }
            
            //System.out.println(":::insert batach start:::");
            pPreparedStatement.executeBatch();
            lConnection.commit();
            lConnection.setAutoCommit(true);
        }catch(SQLException se ){
            se.printStackTrace();
            return null;
        }catch(Exception e ){
            e.printStackTrace();
            return null;
        }finally{
            //close the connection and the result set
            DBConnectionManager.closeConnection(lPreparedStatement,lResultSet);
            DBConnectionManager.closeConnection(pPreparedStatement,null);
           // DBConnectionManager.closeConnection(lConnection);           
        }
        
        return lScoreList;
    }
    
    /**
 	 * method to get the complete list of the data
 	 * @return
 	 */
 	public HashMap getScoreList(Connection lConnection,String pDbMigrationType) {
 		
 		HashMap lhmPatternData = new HashMap();
 		try {
 			//lConnection = DBConnectionManager.getConnection();

 			// replacing the special characters in the query string

 			// select the data from the table
 			// if required we can add % in the like operator
 			
 			lPreparedStatement = lConnection.prepareStatement(ToolConstant.PATTERN_ANALYSYS_GET_SCORE);
 			lPreparedStatement.setString(1, pDbMigrationType);
 			int i = 0;
 			lResultSet = lPreparedStatement.executeQuery();
 			// if rs == null, then there is no ResultSet to view
 			String lStatementCont = "";
 			String lStatementType = "";
 			if (lResultSet != null) {
 				// this will step through our data row-by-row
 				while (lResultSet.next()) {
 					
 					// System.out.println("Data from column_name: " +
 					// lResultSet.getString("STATEMENT_TYPE"));
 					// retValue = lResultSetNew.getString("STATEMENT_TYPE");
 					lStatementCont = lResultSet.getString("STATEMENT_CONT");
 					

 					if (lStatementCont != null) {
 						lStatementCont = lStatementCont.toUpperCase();
 						System.out.println("indv score and statement"+lResultSet.getString("STATEMENT_CONT")+"  "+lResultSet.getString("STATEMENT_SCORE"));
 						lhmPatternData.put(lStatementCont, lResultSet
 								.getString("STATEMENT_SCORE"));
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
 			DBConnectionManager.closeConnection(lPreparedStatement,
 					lResultSet);
 			//DBConnectionManager.closeConnection(lConnection);
 		}

 		return lhmPatternData;
 	}
 	
 	
 	
 	public int getHmScoreData(String pPramQueryString){
 		int retVal = 0;
 		if (lScoreMap == null ) return 0;
 		
		 pPramQueryString = pPramQueryString.replaceAll("'","");
		 pPramQueryString =pPramQueryString.toUpperCase();
		 
		 //if there is no value in the result value matching then return a constant
        if(pPramQueryString == null || "".equals(pPramQueryString))  return 0;
        
        try {
       	 pPramQueryString = (String)lScoreMap.get(pPramQueryString);
       	 if(pPramQueryString == null || "".equals(pPramQueryString)){
       		 return 0;
       	 }else{
       		retVal = Integer.parseInt(pPramQueryString);
       	 }
       	 
       	 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return 0;
		}
        
        return retVal;
	}
 	
 	
 	
 	 /**
     * this method for getting the pattern value for the DB
     * @param pPramQueryString
     * @return
     */
    public List getWholePatternScoreData(Connection lConnection,String pDbMigrationType){
         
    	PatternScoreDTO lPatternScoreDTO = new PatternScoreDTO();
    	List lPatternScoreList = new ArrayList();
        
        try{
            //lConnection = DBConnectionManager.getConnection();
            
            // select the data from the table
            lPreparedStatement = lConnection.prepareStatement(ToolConstant.PATTERN_ANALYSYS_GET_PATTERN_SCORE);
            lPreparedStatement.setString(1,pDbMigrationType);
            lResultSet = lPreparedStatement.executeQuery();
            // if rs == null, then there is no ResultSet to view
            
            if (lResultSet != null){
                // this will step through our data row-by-row
                while(lResultSet.next()){
                	lPatternScoreDTO = new PatternScoreDTO();
                	lPatternScoreDTO.setPatternScore(ToolsUtil.replaceNullToZero(lResultSet.getString("PATTERN_SCORE")));
                	lPatternScoreDTO.setPatternId(ToolsUtil.replaceNull(lResultSet.getString("PATTERN_ID")));
                	lPatternScoreDTO.setPatternFormat(ToolsUtil.replaceNull(lResultSet.getString("PATTERN_FORMAT")));
                	lPatternScoreList.add(lPatternScoreDTO);
                }
            }
            
        }catch(SQLException se ){
            se.printStackTrace();
            return null;
        }catch(Exception e ){
            e.printStackTrace();
            return null;
        }finally{
            //close the connection and the result set
            DBConnectionManager.closeConnection(lPreparedStatement,lResultSet);
            //DBConnectionManager.closeConnection(lConnection);           
        }
        
        return lPatternScoreList;
    }
    
    /**
     * this method for getting the pattern value for the DB
     * @param pPramQueryString
     * @return
     */
    public HashMap getExceptionPatternData(Connection lConnection,String pDbMigrationType){
         
    	
    	HashMap lExceptionPatternMap = new HashMap();
        
        try{
            //lConnection = DBConnectionManager.getConnection();
            
            // select the data from the table
            lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_EXCEPTION_PATTERNS);
            lPreparedStatement.setString(1,pDbMigrationType);
            lResultSet = lPreparedStatement.executeQuery();
            // if rs == null, then there is no ResultSet to view
            
            if (lResultSet != null){
                // this will step through our data row-by-row
                while(lResultSet.next()){                	
                	lExceptionPatternMap.put((String) ToolsUtil.replaceNullToZero(lResultSet.getString("PATTERN_FORMAT")).toUpperCase().trim() , (String) ToolsUtil.replaceNullToZero(lResultSet.getString("PATTERN_ID")).toUpperCase().trim());
                }
            }
            
        }catch(SQLException se ){
            se.printStackTrace();
            return null;
        }catch(Exception e ){
            e.printStackTrace();
            return null;
        }finally{
            //close the connection and the result set
            DBConnectionManager.closeConnection(lPreparedStatement,lResultSet);
            //DBConnectionManager.closeConnection(lConnection);           
        }
        
        return lExceptionPatternMap;
    }
    
    public PatternScoreDTO getSinglePatternScoreData(String pParam){
    	PatternScoreDTO lPatternScoreDTO = new PatternScoreDTO();
    	try{
    		if(lWholeScorePlatternList != null ){
    			for (int i = 0; i < lWholeScorePlatternList.size(); i++) {
					lPatternScoreDTO = (PatternScoreDTO)lWholeScorePlatternList.get(i);
					if(lPatternScoreDTO.getPatternScore().equalsIgnoreCase(pParam)){
						return lPatternScoreDTO;
					}
				}
    		}
    		
    		
    		//if there is no matching data return a default
    		lPatternScoreDTO = new PatternScoreDTO();
    		lPatternScoreDTO.setPatternScore(pParam);
    		lPatternScoreDTO.setPatternId("PAT_E");
    		lPatternScoreDTO.setPatternFormat("PATTERN_EXCEPTION");
    		return lPatternScoreDTO;
    		
    	}catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public PreparedStatement insertSummary(PatternSummaryDTO pPatternSummaryDTO,Connection pConnection ,PreparedStatement pPreparedStatement) {
 		try {
 			/*if(pPatternSummaryDTO.getPatternDesc() == null || ("".equals(pPatternSummaryDTO.getPatternDesc()))){
 				return pPreparedStatement;
 			}*/
 			
 		//System.out.println("::::dto pat run id:::"+pPatternSummaryDTO.getRunId()+"::::i val:::::pattern id::"+pPatternSummaryDTO.getPatternId()+"::::pattern format::"+pPatternSummaryDTO.getPatternFormat());
 			//System.out.println("dto ru id::::"+pPatternSummaryDTO.getRunId());;
 			//System.out.println("dto key word::::"+pPatternSummaryDTO.getKeyWord());;
 			//String[] lPatternArr = getExceptionedPattern(pConnection,pPatternSummaryDTO.getRunId(),pPatternSummaryDTO.getPatternDesc(),pPatternSummaryDTO.getFormedStatement(),pPatternSummaryDTO.getQueryCount(),pPatternSummaryDTO.getPatternId(),pPatternSummaryDTO.getProcedureName(),pPatternSummaryDTO.getStatementNo());
 			String[] lPatternArr = new String[2];
 			lPatternArr[0] = pPatternSummaryDTO.getPatternId();
 			lPatternArr[1] = pPatternSummaryDTO.getPatternDesc();
 			
 			if((WebConstant.DB_MIGRATION_TYPE_SYBASE_TO_DB2).equalsIgnoreCase(pPatternSummaryDTO.getDbMigrationType())){
 				lPatternArr = getExceptionedPattern(pConnection,pPatternSummaryDTO.getRunId(),pPatternSummaryDTO.getPatternDesc(),pPatternSummaryDTO.getFormedStatement(),pPatternSummaryDTO.getQueryCount(),pPatternSummaryDTO.getPatternId(),pPatternSummaryDTO.getProcedureName(),pPatternSummaryDTO.getStatementNo());
 			}
 			if((WebConstant.DB_MIGRATION_TYPE_SYBASE_TO_Oracle).equalsIgnoreCase(pPatternSummaryDTO.getDbMigrationType())){
 				lPatternArr = getExceptionedPattern(pConnection,pPatternSummaryDTO.getRunId(),pPatternSummaryDTO.getPatternDesc(),pPatternSummaryDTO.getFormedStatement(),pPatternSummaryDTO.getQueryCount(),pPatternSummaryDTO.getPatternId(),pPatternSummaryDTO.getProcedureName(),pPatternSummaryDTO.getStatementNo());
 			}
 			
 			
 			// prepare the query
 			//pPreparedStatement = pConnection.prepareStatement(ToolConstant.PATTERN_ANALYSYS_INSERT_SUMMARY);
 			pPreparedStatement.setString(1, pPatternSummaryDTO.getRunId());
 			pPreparedStatement.setString(2, pPatternSummaryDTO.getProcedureName());
 			pPreparedStatement.setString(3, pPatternSummaryDTO.getStatement().replaceAll("0000_TFC_", " "));
 			pPreparedStatement.setString(4, pPatternSummaryDTO.getStatementNo());
 			pPreparedStatement.setString(5, pPatternSummaryDTO.getKeyWord());
 			pPreparedStatement.setString(6, pPatternSummaryDTO.getScore() );
 			pPreparedStatement.setString(7,lPatternArr[0] );  /*pPatternSummaryDTO.getPatternId()*/
 			//pPreparedStatement.setString(8, pPatternSummaryDTO.getPatternDesc() );
 			pPreparedStatement.setString(8, lPatternArr[1] ); /*getExceptionedPattern(pPatternSummaryDTO.getPatternDesc(),pPatternSummaryDTO.getFormedStatement(),pPatternSummaryDTO.getQueryCount(),pPatternSummaryDTO.getPatternId())*/
 			
 			//System.out.println(" Prepared st:  ---> "+pPatternSummaryDTO.getFormedStatement());
 			
 			
 			pPreparedStatement.setString(9, pPatternSummaryDTO.getFormedStatement().replaceAll("0000_TFC_", " ")); 			 			
 			pPreparedStatement.setString(10, ToolConstant.CREATED_BY);
 			pPreparedStatement.setTimestamp(11, new Timestamp(System
 					.currentTimeMillis()));
 			pPreparedStatement.setString(12, pPatternSummaryDTO.getQueryCount());
 			//System.out.println("::::getting inserted::::"+pPatternSummaryDTO.getPatternDesc()+"-"+pPatternSummaryDTO.getKeyWord());
 			pPreparedStatement.setString(13, pPatternSummaryDTO.getFolderPath());

 			// Execute Query
 			//pPreparedStatement.executeUpdate();
 			pPreparedStatement.addBatch(); 		
 			lIncrementBatch++;
 			
 			 if(lIncrementBatch ==1000){
             	pPreparedStatement.executeBatch();
             	 lConnection.commit();
             	 lIncrementBatch =0;
             }
 			 
 			//lPreparedStatement.executeBatch();

 		} catch (SQLException pSQLException) {
 			pSQLException.printStackTrace();
 		} catch (Exception pException) {
 			pException.printStackTrace();
 		} finally {
 			
 		}
 		return pPreparedStatement;

 	}
    
    
    public String[] getExceptionedPattern(Connection pConnection,String pRunID,String pPatternDesc,String lFormedStatement,String pQueryCount,String pPatternId,String pProcName,String pStatementNo){
    	String pPatternReturn="";
    	
    	try{
    		Pattern lSelectPattern = Pattern.compile("\\bSELECT\\b\\s+");
    		Pattern lUpdatePattern = Pattern.compile("\\bUPDATE\\b\\s+");
    		Pattern lInsertPattern = Pattern.compile("\\bINSERT\\b\\s+");
    		Pattern lDeletePattern = Pattern.compile("\\bDELETE\\b\\s+");
    		Pattern lDropPattern = Pattern.compile("\\bDROP\\b[\\s\\w\\W\\r\\n]+");
    		Pattern lCursorPattern = Pattern.compile("\\bCURSOR\\b[\\S\\W\\w\\r\\n]+\\bFOR\\b\\s+");
    		//Pattern lCursorPattern = Pattern.compile("\\bCURSOR\\b[\\s\\r\\n]+\\bFOR\\b\\s+");
    		Pattern lFromWherePattern = Pattern.compile("\\bFROM\\b[\\S\\W\\w\\r\\n]+,[\\S\\W\\w\\r\\n]+\\bWHERE\\b\\s+");
    		Pattern lFromWhereSelectPattern = Pattern.compile("\\bFROM\\b[\\S\\W\\w\\r\\n]+,[\\S\\W\\w\\r\\n]+\\bWHERE\\b[\\S\\W\\w\\r\\n]+\\bSELECT\\b\\s+");
    		Pattern lSelectFromIntoPattern = Pattern.compile("\\bSELECT\\b[\\S\\W\\w\\r\\n]+\\bINTO\\b[\\S\\W\\w\\r\\n]+\\bFROM\\b\\s+");
    		Pattern lSelectFromIntoOuterPattern = Pattern.compile("\\bSELECT\\b[\\S\\W\\w\\r\\n]+\\bINTO\\b[\\S\\W\\w\\r\\n]+\\bFROM\\b[\\S\\W\\w\\r\\n]+\\*=");
    		Pattern lSelectGetDate = Pattern.compile("\\bSELECT\\b[\\s\\W\\w\\r\\n]+\\bGETDATE\\s*\\(");
    		
    		Pattern lSelectOtherPatternWithEqualTo = Pattern.compile("\\bSELECT\\b[\\s\\W\\w\\r\\n]+=\\s*");
    		Pattern lSelectWithEqualToAndFrom = Pattern.compile("\\bSELECT\\b[\\S\\W\\w\\r\\n]+@[\\S\\W\\w\\r\\n]+=[\\S\\W\\w\\r\\n]+\\bFROM\\b\\s+");
    		Pattern lSelectWhereExists = Pattern.compile("\\bWHERE\\b\\s+\\bEXISTS\\b\\s*");
    		
    		Pattern lDeclareGlobalTempTable = Pattern.compile("\\bDECLARE\\b[\\s\\r\\n]+\\bGLOBAL\\b[\\s\\r\\n]+\\bTEMPORARY\\b[\\s\r\\n]+\\bTABLE\\b\\s+");
    		Pattern lDeclareVariable = Pattern.compile("\\bDECLARE\\b[\\s\\w\\W\\r\\n]+");
    		Pattern lSetVariablePattern = Pattern.compile("\\bSET\\b[\\s\\w\\W\\r\\n]+");
    		Pattern lDeclareContinueHandler = Pattern.compile("\\bDECLARE\\b\\s+\\bCONTINUE\\b\\s+\\bHANDLER\\b[\\s\\w\\W\\r\\n]+");
    		
    		Pattern lCreateDBObjectsPattern = Pattern.compile("(?i)\\bCREATE\\b[\\s\\w\\W\\r\\n]+");
    		Pattern lSetNoCountPattern = Pattern.compile("(?i)\\bSET\\b[\\s\\r\\n]+\\bNOCOUNT\\b[\\s\\r\\n]+");
    		
    		
    		//DECLARE CONTINUE  HANDLER
    		
    		
    		String[] lRetStringArr = new String[]{pPatternId,pPatternDesc}; 
			PreparedStatement pPreparedStatement=null;
    		
    		/*Pattern lComplexPattern_InsertWithSelect = Pattern.compile("\\bINSERT\\b\\s+[\\W\\w\\s]*\\bSELECT\\b");
    		Pattern lComplexPattern_UpdateWithSelect = Pattern.compile("\\bUPDATE\\b\\s+[\\W\\w\\s]*\\bSELECT\\b");
    		Pattern lComplexPattern_DeleteWithSelect = Pattern.compile("\\bDELETE\\b\\s+[\\W\\w\\s]*\\bSELECT\\b");
    		Pattern lComplexPattern_DeleteWithSelect = Pattern.compile("\\bDELETE\\b\\s+[\\W\\w\\s]*\\bSELECT\\b");*/
    		lFormedStatement = lFormedStatement.trim().toUpperCase();
    		  //this line removes empty double quote
    		lFormedStatement = lFormedStatement.replaceAll("\"([^\"]*)\"" ,"");
            //for removing contents inside the single quotes
    		lFormedStatement = lFormedStatement.replaceAll("\'([^\']*)\'" ,"");
    		
    		pPatternDesc = pPatternDesc.trim();
    	
    		//System.out.println(":::for complex pattern::;"+lComplexPattern1.matcher(lFormedStatement).find());
    		
    		//if(lFormedStatement.matches("\\bUPDATE\\b")||lFormedStatement.matches("\\bDELETE\\b"))
    		
    		if(Integer.parseInt(pQueryCount)>=2) {
    			pPatternDesc ="PATTERN_EXCEPTION";
    			pPatternId ="PAT_E";
    		}
    		
    		if(lDeclareGlobalTempTable.matcher(lFormedStatement).find()){
    			String pTempPatternDesc="DECLARE GLOBAL TEMPORARY TABLE";
				lRetStringArr[0] = (String)lExceptionPatternMap.get(((String)pTempPatternDesc).trim().toUpperCase());
				lRetStringArr[1] = pTempPatternDesc;
				return lRetStringArr;
    		}
    		
    		// Checking for Cursor Usage
    		String lCursorName="";
    		if(lCursorPattern.matcher(lFormedStatement).find())
    		{    			
    			lCursorName=lFormedStatement.substring(lFormedStatement.indexOf("DECLARE ")+8, lFormedStatement.indexOf(" CURSOR")).trim();
    			//int c=0;
    			if(lCursorName!=null && (!("".equals(lCursorName)))){
    				pPreparedStatement = pConnection.prepareStatement(ToolConstant.INSERT_CURSOR_USAGE);
         			pPreparedStatement.setString(1,pRunID );
         			pPreparedStatement.setString(2, null);
         			pPreparedStatement.setString(3, pProcName);
         			pPreparedStatement.setString(4, pStatementNo);
         			pPreparedStatement.setString(5, lCursorName);
         			pPreparedStatement.setString(6,"TCS USER");
         			pPreparedStatement.setTimestamp(7,new Timestamp(System
         					.currentTimeMillis()));
         			 pPreparedStatement.executeUpdate();    				
    			}
    		/*	if(c>0){
    				if(pPatternId.trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_DECLARE_CURSOR.trim())){
    					
    					if(!lCursorPattern.matcher(lFormedStatement).find()){    				
    						String pTempPatternDesc="declare cursor";
    						lRetStringArr[0] = (String)lExceptionPatternMap.get(((String)pTempPatternDesc).trim().toUpperCase());
    						lRetStringArr[1] = pTempPatternDesc;
    						return lRetStringArr;
    	    			}
    				}
    			}*/
    			
    			/*System.out.println("lFormedStatement :--> "+lFormedStatement);
    			System.out.println("Cursor Name:-->  "+lCursorName);*/
    		}
    		//Checking for Select With Getdate Pattern
    		if(pPatternId.trim().equalsIgnoreCase(ToolConstant.PATTERN_ID_SELECT_OTHER.trim())){
    			if(!lSelectOtherPatternWithEqualTo.matcher(lFormedStatement).find()){    				
					String pTempPatternDesc="Select Other Pattern Without =";
					lRetStringArr[0] = (String)lExceptionPatternMap.get(((String)pTempPatternDesc).trim().toUpperCase());
					lRetStringArr[1] = pTempPatternDesc;
					return lRetStringArr;
    			}
    			/*if(lSelectGetDate.matcher(lFormedStatement).find()){    				    				
    				String pTempPatternDesc="Select With Getdate Func";
    				lRetStringArr[0] = (String)lExceptionPatternMap.get(((String)pTempPatternDesc).trim().toUpperCase());
	    			lRetStringArr[1] = pTempPatternDesc;
    	       		return lRetStringArr;
    			}*/
    		}
       		
    		//return lRetStringArr;
    		
   		
    		if(pPatternDesc.trim().equalsIgnoreCase("PATTERN_EXCEPTION")){
    			
	    			//System.out.println(":::::stmt:::"+lFormedStatement);
    				if(lSelectFromIntoOuterPattern.matcher(lFormedStatement).find()){
   				 		pPatternReturn+="SELECT with into and from with outer join";
    			 	}else if(lSelectFromIntoPattern.matcher(lFormedStatement).find()){
	    				 pPatternReturn+="SELECT with into and from";	    				 
	    			}else if( (lSelectWithEqualToAndFrom.matcher((lFormedStatement.toUpperCase().split(" FROM ").length > 1 ) ? lFormedStatement.toUpperCase().split(" FROM ")[0]+" FROM " : lFormedStatement.toUpperCase().split(" FROM ")[0]).find()) 
   	       				 	&&(!lSelectFromIntoPattern.matcher(lFormedStatement.toUpperCase()).find())
   	       				 	&&(!lUpdatePattern.matcher(lFormedStatement).find()) 
   	       				 	&&(!lInsertPattern.matcher(lFormedStatement).find())
   	       				 	&&(!lDeletePattern.matcher(lFormedStatement).find())
	    					&&(!lCursorPattern.matcher(lFormedStatement).find())){
	    				if(lSelectWhereExists.matcher(lFormedStatement).find()){
	    					pPatternReturn = "Select Into Variable Where Exists";
	    				}else{
	    					pPatternReturn = "Select Into Variable ";
	    				}
	    				   	       				    			
   	       			}else if(lSelectPattern.matcher(lFormedStatement).find()){
	    				 pPatternReturn+="SELECT ";
	    		   	}
    				
				
    			 if(lUpdatePattern.matcher(lFormedStatement).find()){
					 if("".equals(pPatternReturn)){
						 pPatternReturn+="UPDATE ";
					 }else{
						 pPatternReturn+="With UPDATE ";
					 }
					 
				 }
    			 if(lInsertPattern.matcher(lFormedStatement).find()){
					if("".equals(pPatternReturn)){
						 pPatternReturn+="INSERT ";
					 }else{
						 pPatternReturn+="With INSERT ";
					 }
					//pPatternReturn+="INSERT  ";
				 }
    			 if(lDeletePattern.matcher(lFormedStatement).find()){
					//pPatternReturn+="DELETE  ";
					if("".equals(pPatternReturn)){
						 pPatternReturn+="DELETE ";
					 }else{
						 pPatternReturn+="With DELETE ";
					 }
				 }
    			/* if(lCursorPattern.matcher(lFormedStatement).find()){
					//pPatternReturn+="CURSOR  ";
					if("".equals(pPatternReturn)){
						 pPatternReturn+="CURSOR ";
					 }else{
						 pPatternReturn+="With CURSOR ";
					 }
				 }*/
    			 
    			
    			
    			 
				if(Integer.parseInt(pQueryCount)>=2) pPatternReturn="Complex Query "+pPatternReturn;
				
				
				pPatternReturn=pPatternReturn.trim();
	    		lRetStringArr[0] = (String)lExceptionPatternMap.get(((String)pPatternReturn).toUpperCase());
	    		if( lRetStringArr[0] == null|| "".equals(lRetStringArr[0])){ 
	    			lRetStringArr[0] ="PAT_E";
	    		}
	    		lRetStringArr[1] = pPatternReturn;
	    		
				
			 	}else{
				 
				 lRetStringArr[1] = pPatternReturn;
				// return lRetStringArr;
			 }
    		
    		
    		if(!pPatternDesc.trim().equalsIgnoreCase("PATTERN_EXCEPTION")){ 
       		 if((lUpdatePattern.matcher(lFormedStatement).find()) || (lDeletePattern.matcher(lFormedStatement).find())){   		    		
   	    			//System.out.println(":::inside step1"+lFormedStatement);
       			 if(lSelectPattern.matcher(lFormedStatement).find()){
       				if(lFromWhereSelectPattern.matcher(lFormedStatement).find()){	
   	    				pPatternDesc=pPatternDesc+" With Join";
   	    				pPatternId= (String)lExceptionPatternMap.get(((String)pPatternDesc).trim().toUpperCase());
   	    				lRetStringArr[0] = pPatternId;
   	    				lRetStringArr[1] = pPatternDesc;
   	    				pPatternReturn=pPatternDesc;
   	    				//return lRetStringArr;
   	    			
   	    			}
       				 
       			 }else{
       				if(lFromWherePattern.matcher(lFormedStatement).find()){	
   	    				pPatternDesc=pPatternDesc+" With Join";
   	    				pPatternId = (String)lExceptionPatternMap.get(((String)pPatternDesc).trim().toUpperCase());
   	    				lRetStringArr[0] = pPatternId;
   	    				lRetStringArr[1] = pPatternDesc;
   	    				pPatternReturn=pPatternDesc;
   	    				//return lRetStringArr;
   	    			
   	    			}
       				 
       			 }
   	    			
   	    		}
       		 
       		 
       		 if( (lSelectWithEqualToAndFrom.matcher((lFormedStatement.toUpperCase().split(" FROM ").length > 1 ) ? lFormedStatement.toUpperCase().split(" FROM ")[0]+" FROM " : lFormedStatement.toUpperCase().split(" FROM ")[0]).find()) 
       				 &&(!lSelectFromIntoPattern.matcher(lFormedStatement.toUpperCase()).find()) ){
       			pPatternDesc = "Select Into Variable "; 
       			pPatternId = (String)lExceptionPatternMap.get(((String)pPatternDesc).trim().toUpperCase());    			
       		 }
       		 
       		 
       		 lRetStringArr[0] = pPatternId;
       		 lRetStringArr[1] = pPatternDesc;
       		}else{
       			System.out.println("::::::inside else::::");
    			if((lUpdatePattern.matcher(lFormedStatement).find()) || (lDeletePattern.matcher(lFormedStatement).find())){
    				System.out.println("::::::inside else -stage1::::");
   	    			//System.out.println(":::inside step1"+lFormedStatement);
       			 if(lSelectPattern.matcher(lFormedStatement).find()){
       				System.out.println("::::::inside else -stage1-1::::");
       				if(lFromWhereSelectPattern.matcher(lFormedStatement).find()){	
       					System.out.println("::::::inside else -stage1-2::::");
   	    				pPatternReturn=pPatternReturn+" from With Join";
   	    				lRetStringArr[0] = (String)lExceptionPatternMap.get(((String)pPatternReturn).trim().toUpperCase());
   	    				//lRetStringArr[0] = "PAT_E";
   	    				lRetStringArr[1] = pPatternReturn;
   	    				
   	    				//return lRetStringArr;
   	    			
   	    			}
       				 
       			 }else{
       				//System.out.println("::::::inside else -stage2::::");
       				if(lFromWherePattern.matcher(lFormedStatement).find()){	
       					System.out.println("::::::inside else -stage2-1::::");
   	    				pPatternReturn=pPatternReturn+" from With Join";
   	    				lRetStringArr[0] = (String)lExceptionPatternMap.get(((String)pPatternReturn).trim().toUpperCase());
   	    				//lRetStringArr[0] = "PAT_E";
   	    				lRetStringArr[1] = pPatternReturn;
   	    				
   	    				//return lRetStringArr;
   	    			
   	    			}else{
   	    				System.out.println("Update Pattern not found!!!!!!!!!!!!!!!!!!!!!!!!!!!");
   	    				ToolsUtil.prepareInsertStatusMsg( ToolsUtil.getProjectId(pRunID), ToolConstant.CREATED_BY, "Pattern Error","Update Pattern Id Not found in line no->"+pStatementNo, "Info",new Timestamp(System.currentTimeMillis()),DBConnectionManager.getConnection());
   	    			}
       				 
       			 }  	    			
   	    		
   	    			/*//System.out.println(":::inside step1"+lFormedStatement);
   	    			if(lFromWherePattern.matcher(lFormedStatement).find()){	
   	    				pPatternReturn=pPatternReturn+" from With Join";
   	    				lRetStringArr[0] = (String)lExceptionPatternMap.get(((String)pPatternReturn).trim().toUpperCase());
   	    				//lRetStringArr[0] = "PAT_E";
   	    				lRetStringArr[1] = pPatternReturn;
   	    				
   	    				//return lRetStringArr;
   	    			
   	    			}*/
   	    		}
    			
    		}
       		
    	
    		if(pPatternDesc.trim().equalsIgnoreCase("PATTERN_EXCEPTION")
    				&& !(lDeclareGlobalTempTable.matcher(lFormedStatement).find())
    				/*&& !(lCursorPattern.matcher(lFormedStatement).find())*/
    				&& (lDeclareVariable.matcher(lFormedStatement).find())
    				&& "".equals(pPatternReturn)){
    			if(lDeclareContinueHandler.matcher(lFormedStatement).find()){
    				pPatternReturn="Declare Continue Handler";
        			lRetStringArr[0] = (String)lExceptionPatternMap.get(((String)pPatternReturn).trim().toUpperCase());
       				//lRetStringArr[0] = "PAT_E";
       				lRetStringArr[1] = pPatternReturn;
    				
    			}else{
    				pPatternReturn="Declare Variable";
        			lRetStringArr[0] = (String)lExceptionPatternMap.get(((String)pPatternReturn).trim().toUpperCase());
       				//lRetStringArr[0] = "PAT_E";
       				lRetStringArr[1] = pPatternReturn;
       			}
    			
    			//System.out.println("lFormedStatement::->"+lFormedStatement+" ::Desc-->"+pPatternReturn);
    		}
    		
    		if(pPatternDesc.trim().equalsIgnoreCase("PATTERN_EXCEPTION")
    				&& !(lUpdatePattern.matcher(lFormedStatement).find())    				
    				&& (lSetVariablePattern.matcher(lFormedStatement).find())
    				&& (lFormedStatement.toUpperCase().trim().startsWith("SET"))){
    			if(lSetNoCountPattern.matcher(lFormedStatement).find()){
    				pPatternReturn="SET NoCount";
    			}else{
    				pPatternReturn="SET Variable";
    			}
    			lRetStringArr[0] = (String)lExceptionPatternMap.get(((String)pPatternReturn).trim().toUpperCase());
   				//lRetStringArr[0] = "PAT_E";
   				lRetStringArr[1] = pPatternReturn;
    			//System.out.println("lFormedStatement::->"+lFormedStatement+" ::Desc-->"+pPatternReturn);
    		}
    		if(pPatternDesc.trim().equalsIgnoreCase("PATTERN_EXCEPTION")    				    				
    				&& (lDropPattern.matcher(lFormedStatement).find())
    				&& "".equalsIgnoreCase(pPatternReturn.trim())){
    			pPatternReturn="Drop DB Object";
    			lRetStringArr[0] = (String)lExceptionPatternMap.get(((String)pPatternReturn).trim().toUpperCase());
   				//lRetStringArr[0] = "PAT_E";
   				lRetStringArr[1] = pPatternReturn;
    			//System.out.println("lFormedStatement::->"+lFormedStatement+" ::Desc-->"+pPatternReturn);
    		}
    		if(pPatternDesc.trim().equalsIgnoreCase("PATTERN_EXCEPTION")    				    				
    				&& (lCreateDBObjectsPattern.matcher(lFormedStatement).find())
    				&& "".equalsIgnoreCase(pPatternReturn.trim())){
    			pPatternReturn="Create DB Object";
    			lRetStringArr[0] = (String)lExceptionPatternMap.get(((String)pPatternReturn).trim().toUpperCase());
   				//lRetStringArr[0] = "PAT_E";
   				lRetStringArr[1] = pPatternReturn;
    			//System.out.println("lFormedStatement::->"+lFormedStatement+" ::Desc-->"+pPatternReturn);
    		}
    		
    		
    		
    		return lRetStringArr;
			 
    		//return pPatternReturn;
    	}catch(Exception e){
    		//return null;
    		return new String[]{"",""};
    	}
    }
    
    
    //method not used
    public String getExceptionedPatternWithId(String pPatternId,String pParam,String lFormedStatement,String pQueryCount){
    	String pPatternReturn="";
    	try{
    		if(pPatternId.trim().equalsIgnoreCase("PAT_E"))
			 {
				 if(lFormedStatement.toUpperCase().contains("SELECT")){
					 pPatternReturn+="SELECT  ";
				 }
				 if(lFormedStatement.toUpperCase().contains("UPDATE")){
					 if("".equals(pPatternReturn)){
						 pPatternReturn+="UPDATE  ";
					 }else{
						 pPatternReturn+="With UPDATE  ";
					 }
					 
				 }
				if(lFormedStatement.toUpperCase().contains("INSERT")){
					if("".equals(pPatternReturn)){
						 pPatternReturn+="INSERT  ";
					 }else{
						 pPatternReturn+="With INSERT  ";
					 }
					//pPatternReturn+="INSERT  ";
				 }
				if(lFormedStatement.toUpperCase().contains("DELETE")){
					//pPatternReturn+="DELETE  ";
					if("".equals(pPatternReturn)){
						 pPatternReturn+="DELETE  ";
					 }else{
						 pPatternReturn+="With DELETE  ";
					 }
				 }
				if(lFormedStatement.toUpperCase().contains("CURSOR")){
					//pPatternReturn+="CURSOR  ";
					if("".equals(pPatternReturn)){
						 pPatternReturn+="CURSOR  ";
					 }else{
						 pPatternReturn+="With CURSOR  ";
					 }
				 }
				if(Integer.parseInt(pQueryCount)>=2)
					pPatternReturn="Complex Query "+pPatternReturn;
			 }else{
				 return pParam;
			 }
    		return pPatternReturn;
    	}catch(Exception e){
    		return "";
    	}
    }
    
    /**
	 * method to get the complete list of the data
	 * @return
	 */
	public HashMap getProcRowCount(String pRunId,Connection lConnection) {
		HashMap lhmCountData = new HashMap();
		try {
			//lConnection = DBConnectionManager.getConnection();

			// replacing the special characters in the query string

			// select the data from the table
			// if required we can add % in the like operator
			lPreparedStatement = lConnection.prepareStatement(ToolConstant.PATTERN_ANALYSYS_PROC_WISE_COUNT);
			lPreparedStatement.setString(1, pRunId);
			int i = 0;
			lResultSet = lPreparedStatement.executeQuery();
		
			if (lResultSet != null) {
				// this will step through our data row-by-row
				while (lResultSet.next()) {
					//System.out.println("::::"+lResultSet.getString("PROCEDURE_NAME")+":::::"+lResultSet.getString("count_val"));
					lhmCountData.put(lResultSet.getString("PROCEDURE_NAME"),lResultSet.getString("count_val"));
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
			DBConnectionManager.closeConnection(lPreparedStatement,
					lResultSet);
			//DBConnectionManager.closeConnection(lConnection);
		}

		return lhmCountData;
	}
        
        
         /**
 	 * method to get the complete list of the data
 	 * @return
 	 */
	
	//method not used
 	public void getSummaryList(String pRunId) {
 		
 		
 		try {
 			
 			lConnection = DBConnectionManager.getConnection();
                        
                        BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\arun\\tool output\\SumaryResults_NEW_DB_"+pRunId+".txt"));

 			// replacing the special characters in the query string

 			// select the data from the table
 			// if required we can add % in the like operator
 			
                        lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_PATTERN_DATA);
                        lPreparedStatement.setString(1,pRunId);
                        //lPreparedStatement = lConnection.prepareStatement(" SELECT RUN_ID,PROCEDURE_NAME,STATEMENT,STATEMENT_NO,KEY_WORD,SCORE,PATTERN_ID,PATTERN_DESC,FORMED_STATEMENT,CREATED_BY,CREATED_DATE,QUERY_COUNT FROM pattern_results_table where run_id=189 and pattern_id in ('KWORD_18','DTYP_01','FUNC_02','OPR_03') ");          	
                        //lPreparedStatement.setString(1,pRunId);
                        lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
 			int i = 1;
 			lResultSet = lPreparedStatement.executeQuery();
 			// if rs == null, then there is no ResultSet to view
 			String lStatementCont = "";
 			String lStatementType = "";
 			String lPatternId="";
 			String lFormedStatement="";
 			String lExceptionPatternDesc="";
 			String lPatternDesc="";
 			String lQueryCount="";
                        StringBuffer sbGetData= new StringBuffer();
                        String lSeperator="#$";
                         sbGetData.append(0);
                                     sbGetData.append(lSeperator);
                                    sbGetData.append("Procedure Name");
                                    sbGetData.append(lSeperator);
                                    sbGetData.append("Statement");                                    
                                    sbGetData.append(lSeperator);
                                    sbGetData.append("Statement No");
                                    sbGetData.append(lSeperator);
                                    sbGetData.append("Key Word");
                                    sbGetData.append(lSeperator);
                                    sbGetData.append("Score");  
                                    sbGetData.append(lSeperator);
                                    sbGetData.append("pattern Id");  
                                    sbGetData.append(lSeperator);
                                    sbGetData.append("pattern Description");
                                    sbGetData.append(lSeperator);
                                    sbGetData.append("Sub Query Count");  
                                    
                                    sbGetData.append(lSeperator);
                                    sbGetData.append("Formed Statement");  
                                    
                        
                          writer.write(sbGetData.toString());
                           writer.newLine();
                        
 			if (lResultSet != null) {
 				// this will step through our data row-by-row
 				while (lResultSet.next()) {
 					
 					 lPatternId=lResultSet.getString("PATTERN_ID");
 					lFormedStatement=lResultSet.getString("FORMED_STATEMENT");
 					 lExceptionPatternDesc="Pattern With ";
 					 lPatternDesc=lResultSet.getString("PATTERN_DESC");
 					lQueryCount=lResultSet.getString("QUERY_COUNT");
 					
                                    sbGetData= new StringBuffer();
                                     sbGetData.append(i);
                                     sbGetData.append(lSeperator);
                                    sbGetData.append(lResultSet.getString("PROCEDURE_NAME"));
                                    sbGetData.append(lSeperator);                                  
                                sbGetData.append(lResultSet.getString("STATEMENT"));
                                    sbGetData.append(lSeperator);
                                    sbGetData.append(lResultSet.getString("STATEMENT_NO"));
                                    sbGetData.append(lSeperator);
                                    sbGetData.append(lResultSet.getString("KEY_WORD"));
                                    sbGetData.append(lSeperator);
                                    sbGetData.append(lResultSet.getString("SCORE"));
                                    sbGetData.append(lSeperator);
                                    sbGetData.append(lPatternId);
                                    sbGetData.append(lSeperator);
                                    //sbGetData.append(lPatternDesc);
                                    sbGetData.append(getExceptionedPatternWithId(lPatternId,lPatternDesc,lFormedStatement, lQueryCount));
                                    
                                    sbGetData.append(lSeperator);
                                    sbGetData.append(lQueryCount);
                                    sbGetData.append(lSeperator);
                                    sbGetData.append(lFormedStatement);
                                    
                                  
                                     writer.write(sbGetData.toString());
					 writer.newLine();
                                         i++;
 					// System.out.println("Data from column_name: " +
 					// lResultSet.getString("STATEMENT_TYPE"));
 					// retValue = lResultSetNew.getString("STATEMENT_TYPE");
 					
 				}
 			}
                        writer.close();

 		} catch (SQLException se) {
 			se.printStackTrace();
 			//return null;
 		} catch (Exception e) {
 			e.printStackTrace();
 			//return null;
 		} finally {
 			// close the connection and the result set
 			DBConnectionManager.closeConnection(lPreparedStatement,
 					lResultSet);
 			DBConnectionManager.closeConnection(lConnection);
 		}

 		//return lhmPatternData;
 	}
 	
 	//method not used
        public void getParseList(String pRunId) {
 		
 		
 		try {
 			
 			lConnection = DBConnectionManager.getConnection();
                        
                        BufferedWriter writer = new BufferedWriter(new FileWriter("c:/arun/ParseResults_NEW_DB_"+pRunId+".txt"));

 			// replacing the special characters in the query string

 			// select the data from the table
 			// if required we can add % in the like operator
 			
 			lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_PARSE_RESULTS);
                        lPreparedStatement.setString(1,pRunId);
 			int i = 1;
 			lResultSet = lPreparedStatement.executeQuery();
 			// if rs == null, then there is no ResultSet to view
                        
                        String lStatementCont = "";
 			String lStatementType = "";
                        StringBuffer sbGetData= new StringBuffer();
                       
 			if (lResultSet != null) {
 				// this will step through our data row-by-row
 				while (lResultSet.next()) {
                                    sbGetData= new StringBuffer();
                                     sbGetData.append(i);
                                     sbGetData.append("\t");
                                    sbGetData.append(ToolsUtil.replaceTabToSace(lResultSet.getString("PROCEDURE_NAME")));
                                    sbGetData.append("\t");
                                    sbGetData.append(ToolsUtil.replaceTabToSace(lResultSet.getString("STATEMENT")));                                    
                                    sbGetData.append("\t");
                                    sbGetData.append(ToolsUtil.replaceTabToSace(lResultSet.getString("STATEMENT_NO")));
                                    sbGetData.append("\t");
                                    sbGetData.append(ToolsUtil.replaceTabToSace(lResultSet.getString("ACTUAL_PATTERN")));
                                    sbGetData.append("\t");
                                    sbGetData.append(ToolsUtil.replaceTabToSace(lResultSet.getString("STATEMENT_TYPE")));  
                                    sbGetData.append("\t");
                                    sbGetData.append(ToolsUtil.replaceTabToSace(lResultSet.getString("ORDER_NO")));  
                                  
                                     writer.write(sbGetData.toString());
					 writer.newLine();
                                         i++;
 					// System.out.println("Data from column_name: " +
 					// lResultSet.getString("STATEMENT_TYPE"));
 					// retValue = lResultSetNew.getString("STATEMENT_TYPE");
 					
 				}
 			}
                        writer.close();

 		} catch (SQLException se) {
 			se.printStackTrace();
 			//return null;
 		} catch (Exception e) {
 			e.printStackTrace();
 			//return null;
 		} finally {
 			// close the connection and the result set
 			DBConnectionManager.closeConnection(lPreparedStatement,
 					lResultSet);
 			DBConnectionManager.closeConnection(lConnection);
 		}

 		//return lhmPatternData;
 	}
       
        
        
         /**
     * this method for getting the pattern value for the DB
     * @param pPramQueryString
     * @return
     */
    public List getWholeKeyWordScoreData(Connection lConnection,String pDbMigrationType){
         
    	PatternIndividualKeywordScoreDTO lPatternIndividualKeywordScoreDTO = new PatternIndividualKeywordScoreDTO();
    	List lPatternScoreList = new ArrayList();
        
        try{
            //lConnection = DBConnectionManager.getConnection();
            
            // select the data from the table
            lPreparedStatement = lConnection.prepareStatement(ToolConstant.PATTERN_ANALYSYS_GET_KEYWORD_INDV_SCORE);
            lPreparedStatement.setString(1, pDbMigrationType);
            lResultSet = lPreparedStatement.executeQuery();
            // if rs == null, then there is no ResultSet to view
            
            if (lResultSet != null){
                // this will step through our data row-by-row
                while(lResultSet.next()){
                	lPatternIndividualKeywordScoreDTO = new PatternIndividualKeywordScoreDTO();
                        lPatternIndividualKeywordScoreDTO.setKeyWord(ToolsUtil.replaceNull(lResultSet.getString("KEYWORD")));
                	lPatternIndividualKeywordScoreDTO.setScore(ToolsUtil.replaceNullToZero(lResultSet.getString("SCORE")));
                	lPatternIndividualKeywordScoreDTO.setPatternId(ToolsUtil.replaceNull(lResultSet.getString("PATTERN_ID")));
                	lPatternIndividualKeywordScoreDTO.setPatternDesc(ToolsUtil.replaceNull(lResultSet.getString("PATTERN_DESC")));
                	lPatternScoreList.add(lPatternIndividualKeywordScoreDTO);
                }
            }
            
        }catch(SQLException se ){
            se.printStackTrace();
            return null;
        }catch(Exception e ){
            e.printStackTrace();
            return null;
        }finally{
            //close the connection and the result set
            DBConnectionManager.closeConnection(lPreparedStatement,lResultSet);
            //DBConnectionManager.closeConnection(lConnection);           
        }
        
        return lPatternScoreList;
    }
    
    
    public PatternIndividualKeywordScoreDTO getSingleKeywordScoreData(String pParam){
    	PatternIndividualKeywordScoreDTO lPatternIndividualKeywordScoreDTO = new PatternIndividualKeywordScoreDTO();
    	try{
    		if(lWholeKeywordScorePlatternList != null ){
    			for (int i = 0; i < lWholeKeywordScorePlatternList.size(); i++) {
					lPatternIndividualKeywordScoreDTO = (PatternIndividualKeywordScoreDTO)lWholeKeywordScorePlatternList.get(i);
					if(lPatternIndividualKeywordScoreDTO.getKeyWord().equalsIgnoreCase(pParam)){
						//System.out.println(":::::lActualPattern::::"+pParam+":::::pattern::::"+lPatternIndividualKeywordScoreDTO.getPatternDesc());
						return lPatternIndividualKeywordScoreDTO;
					}
				}
    		}
    		
    		return null;
    		//if there is no matching data return a default
    		//lPatternIndividualKeywordScoreDTO = new PatternIndividualKeywordScoreDTO();
    		//lPatternIndividualKeywordScoreDTO.setScore(pParam);
    		//lPatternIndividualKeywordScoreDTO.setPatternId("PAT_E");
    		//lPatternIndividualKeywordScoreDTO.setPatternDesc("PATTERN_EXCEPTION");
    		//return lPatternIndividualKeywordScoreDTO;
    		
    	}catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    //method not used
public void updatePatternException(String pRunId) {
		
		ResultSet lResultSet= null;
		PreparedStatement lPreparedStatement= null;
		PreparedStatement lPreparedStatementUpdate= null;
		try {
			lConnection = DBConnectionManager.getConnection();
                     
			lConnection.setAutoCommit(false) ;      

			// replacing the special characters in the query string

			// select the data from the table
			// if required we can add % in the like operator
			
			//lPreparedStatement = lConnection.prepareStatement("SELECT RUN_ID,PROCEDURE_NAME,STATEMENT,STATEMENT_NO,KEY_WORD,SCORE,PATTERN_ID,PATTERN_DESC,FORMED_STATEMENT,CREATED_BY,CREATED_DATE FROM PATTERN_RESULTS_TABLE WHERE RUN_ID in('229','230','231','232','233','234','235','236','237' )");
                     //lPreparedStatement.setString(1,pRunId);
                     lPreparedStatement = lConnection.prepareStatement("SELECT distinct pattern_desc FROM pattern_results_table p where run_id= ? and pattern_id ='PAT_E' order by pattern_desc ");
                     lPreparedStatement.setString(1,pRunId);
			int i = 1;
			lResultSet = lPreparedStatement.executeQuery();
			int incVal=31;
			if (lResultSet != null) {				
				while (lResultSet.next()) {
					lPreparedStatementUpdate =lConnection.prepareStatement("update pattern_results_table set pattern_id = ? where run_id= ? and pattern_id ='PAT_E' and pattern_desc= ?");
					lPreparedStatementUpdate.setString(1, "PAT_"+incVal);
					lPreparedStatementUpdate.setString(2, pRunId);
					lPreparedStatementUpdate.setString(3, lResultSet.getString("pattern_desc"));
					lPreparedStatementUpdate.executeUpdate();
					incVal++;
				}
			}
			lConnection.commit();
			lConnection.setAutoCommit(true) ;    

		} catch (SQLException se) {
			se.printStackTrace();
			//return null;
		} catch (Exception e) {
			e.printStackTrace();
			//return null;
		} finally {
			// close the connection and the result set
			DBConnectionManager.closeConnection(lPreparedStatement,
					lResultSet);
			DBConnectionManager.closeConnection(lConnection);
		}

		//return lhmPatternData;
	}

/**
	 * method to get the complete list of the data
	 * @return
	 */

//method not used
	public void getCompareList(String pRunId) {
		
		
		try {
			
			lConnection = DBConnectionManager.getConnection();
                    
                    BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\arun\\tool output\\CompareList_"+pRunId+".txt"));

			// replacing the special characters in the query string

			// select the data from the table
			// if required we can add % in the like operator
			
                    lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_COMAPRE_DETAILS);
                    lPreparedStatement.setString(1,pRunId);
                    //lPreparedStatement = lConnection.prepareStatement(" SELECT RUN_ID,PROCEDURE_NAME,STATEMENT,STATEMENT_NO,KEY_WORD,SCORE,PATTERN_ID,PATTERN_DESC,FORMED_STATEMENT,CREATED_BY,CREATED_DATE,QUERY_COUNT FROM pattern_results_table where run_id=189 and pattern_id in ('KWORD_18','DTYP_01','FUNC_02','OPR_03') ");          	
                    //lPreparedStatement.setString(1,pRunId);
                    lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
			int i = 1;
			lResultSet = lPreparedStatement.executeQuery();
			// if rs == null, then there is no ResultSet to view
			String lStatementCont = "";
			String lStatementType = "";
			String lPatternId="";
			String lFormedStatement="";
			String lExceptionPatternDesc="";
			String lPatternDesc="";
			String lQueryCount="";
                    StringBuffer sbGetData= new StringBuffer();
                    String lSeperator="#$";
                     sbGetData.append("SL.NO");
                                 sbGetData.append(lSeperator);
                                sbGetData.append("Procedure Name");
                                sbGetData.append(lSeperator);
                                sbGetData.append("Source Statement No");                                    
                                sbGetData.append(lSeperator);
                                sbGetData.append("Source Pattern Id");
                                sbGetData.append(lSeperator);
                                sbGetData.append("Source Formed Statement");
                                sbGetData.append(lSeperator);
                                sbGetData.append("Target Statement No");                                    
                                sbGetData.append(lSeperator);
                                sbGetData.append("Target Pattern Id");
                                sbGetData.append(lSeperator);
                                sbGetData.append("Target Formed Statement");
                                sbGetData.append(lSeperator);
                                sbGetData.append("Matched YN");
                                
                    
                      writer.write(sbGetData.toString());
                       writer.newLine();
                    
			if (lResultSet != null) {
				// this will step through our data row-by-row
				while (lResultSet.next()) {
					
					
					
                                sbGetData= new StringBuffer();
                                 sbGetData.append(i);
                                 sbGetData.append(lSeperator);
                                sbGetData.append(lResultSet.getString("PROCEDURE_NAME"));
                                sbGetData.append(lSeperator);                                  
                            sbGetData.append(lResultSet.getString("SOURCE_STATEMENT_NO"));
                                sbGetData.append(lSeperator);
                                sbGetData.append(lResultSet.getString("SOURCE_PATTERN_ID"));
                                sbGetData.append(lSeperator);
                                sbGetData.append(lResultSet.getString("SOURCE_FORMED_STATEMENT"));
                                sbGetData.append(lSeperator);
                                sbGetData.append(lResultSet.getString("TARGET_STATEMENT_NO"));
                                sbGetData.append(lSeperator);
                                sbGetData.append(lResultSet.getString("TARGET_PATTERN_ID"));
                                sbGetData.append(lSeperator);
                                //sbGetData.append(lPatternDesc);
                                sbGetData.append(lResultSet.getString("TARGET_FORMED_STATEMENT"));
                                sbGetData.append(lSeperator);
                                //sbGetData.append(lPatternDesc);
                                sbGetData.append(lResultSet.getString("MATCHED_YN"));
                                
                              
                                 writer.write(sbGetData.toString());
				 writer.newLine();
                                     i++;
					// System.out.println("Data from column_name: " +
					// lResultSet.getString("STATEMENT_TYPE"));
					// retValue = lResultSetNew.getString("STATEMENT_TYPE");
					
				}
			}
                    writer.close();

		} catch (SQLException se) {
			se.printStackTrace();
			//return null;
		} catch (Exception e) {
			e.printStackTrace();
			//return null;
		} finally {
			// close the connection and the result set
			DBConnectionManager.closeConnection(lPreparedStatement,
					lResultSet);
			DBConnectionManager.closeConnection(lConnection);
		}

		//return lhmPatternData;
	}
	
	/********************************************sybase to oracle**************************************************/
	public List getParsedDataSybasetoOracle(String pRunId,StoredProceduresDetailsDTO pStoredProceduresDetailsDTO,String pDbMigrationType){
		System.out.println("::::::pProcName sybase oracle:::"+pStoredProceduresDetailsDTO.getProcName());
	     
	        List lScoreList = new ArrayList();
	      
	        PatternSummaryDTO lPatternSummaryDTO = new PatternSummaryDTO();
	      
	        PatternIndividualKeywordScoreDTO lPatternIndividualKeywordScoreDTO = null;
	        
	        PreparedStatement pPreparedStatement = null;
	        String lStatementNo ="";
	        String lStatement = "";
	        String lProcedureName="";
	        String lPrevPattern="";
	        List lConstructsList = new ArrayList();       
	        int lNumofStatements=1;       
	        List lIgnoreConstructsList =new ArrayList();
	        
	        try{
	            
	        	lConnection.setAutoCommit(false);
	        	
	        	pPreparedStatement = lConnection.prepareStatement(ToolConstant.PATTERN_ANALYSYS_INSERT_SUMMARY);
	        	
	        	List lMultiStringList = new ArrayList();
	        	lMultiStringList.add((String)"STR");
	        	lMultiStringList.add((String)"OUTER");
	        	lMultiStringList.add((String)"=");
	        	lMultiStringList.add((String)"!=");
	        	lMultiStringList.add((String)"!>");
	        	lMultiStringList.add((String)"!<");
	        	lMultiStringList.add((String)"IS");
	        	lMultiStringList.add((String)"IS NOT");
	        	lMultiStringList.add((String)"NOT");
	        	lMultiStringList.add((String)"CONVERT");
	        	lMultiStringList.add((String)"(");
	        	lMultiStringList.add((String)"CONVERT (");
	        	lMultiStringList.add((String)"= CONVERT (");
	        	
	        	
	        	List lMultiStringIgnoreAndInsertList = new ArrayList();
	        	lMultiStringIgnoreAndInsertList.add((String)"CONVERT");
	        	
	        	HashMap lIgnoreConstructsHm= new HashMap();
	        	lIgnoreConstructsHm.put((String)"CASE", (String)"END");
	        	
	        	
	        	
	           
	            StringBuffer lConcatenatedValue= new StringBuffer();
	            //select the data from the table
	            //if required we can add % in the like operator
	            //lConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	            //lPreparedStatement = lConnection.prepareStatement(ToolConstant.PATTERN_ANALYSYS_GET_PARSED_DATA,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	            //System.out.println(":::::before executing select:::::");
	            String lSql = " SELECT "
	    			+ " RUN_ID,PROCEDURE_NAME,STATEMENT,STATEMENT_NO,ACTUAL_PATTERN,STATEMENT_TYPE,ORDER_NO "
	    			+ " FROM PARSE_RESULTS_TABLE " + " WHERE RUN_ID= ?  and PROCEDURE_NAME =? "
	    			+ " ORDER BY  PROCEDURE_NAME,ORDER_NO ";
	            lPreparedStatement = lConnection.prepareStatement(lSql);
	            
	            lPreparedStatement.setString(1, pRunId);
	            lPreparedStatement.setString(2, pStoredProceduresDetailsDTO.getProcName());
	            //System.out.println("::::pRunId::::"+pRunId+"$$$$");
	            lResultSet = lPreparedStatement.executeQuery();
	            //System.out.println(":::::after executing select:::::");
	            //lResultSet.last();
	            // int lRowCount = lResultSet.getRow();            
	            // System.out.println(":::::res size::::"+lRowCount);
	            // lResultSet.first();
	            
	            lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_ALL_CONSTRUCTS);
	            lPreparedStatement.setString(1, pDbMigrationType);
	            ResultSet rs=lPreparedStatement.executeQuery();
	            while(rs.next())
	            {
	            	//System.out.println("::::PATTERN:::"+rs.getString("ACTUAL_PATTERN"));            	
	            	lConstructsList.add(rs.getString(2).trim().toUpperCase());
	            	
	            }

	           
	           // int i=0;
	            int score=0;
	            String lActualPattern ="";
	            String lProcNameFirst ="";
	            int procCount =0;
	            String lStatementNoLatest ="";
	            String lStatementLatest ="";
	            String lStatementType="";
	            String lOrdNum="";
	            String lPrevProcName="";
	            String lChkStatementType="";
	            int lChkPrevStat=0;
	            String lRunid="";
	            String lPrevStatementType="";
	            boolean lIsUpdateVisited=false;            
	            
	            //System.out.println("::::while loop starting");
	            //boolean lIsPrevStst=false;
	            int count=0;
	            lPatternSummaryDTO = new PatternSummaryDTO();
	            String lNeedToCheckNextToken="";
	            boolean lKeywordDataInserted = false;
	            if (lResultSet != null){               
	                while(lResultSet.next()){
	                       
	                	lRunid=lResultSet.getString("RUN_ID");
	                	lProcNameFirst = lResultSet.getString("PROCEDURE_NAME");
	                        //System.out.println("lProcNameFirst------>"+lProcNameFirst);
	                	procCount = Integer.parseInt((String)lProcCountMap.get(lProcNameFirst)); 
	                	//System.out.println("::::inside result set:::i val->"+i+"count Value->"+procCount);
	                        lActualPattern =lResultSet.getString("ACTUAL_PATTERN");
	                     //  System.out.println("actual pattern:::::::::::::::::::: "+lActualPattern);
	                        lStatementNoLatest  = lResultSet.getString("STATEMENT_NO");
	                        lStatementLatest = lResultSet.getString("STATEMENT");
	                        lStatementType  = lResultSet.getString("STATEMENT_TYPE");
	                        lOrdNum  = lResultSet.getString("ORDER_NO");  
	                        
	                        
	                        
	                        
	                        //for spaced keyword    - start
	                        lKeywordDataInserted = false;
	                        if(lMultiStringList.contains((String)lActualPattern.toUpperCase())){
	                        	if("".equals(lNeedToCheckNextToken)){
	                        		lNeedToCheckNextToken = lActualPattern.toUpperCase();
	                        	}else{
	                        		lNeedToCheckNextToken = lNeedToCheckNextToken+" "+lActualPattern.toUpperCase();
	                        	}
	                        	//System.out.println(":::;;;first addition:::"+lNeedToCheckNextToken);
	                        }else{
	                        	//System.out.println(":::inside else::::"+lActualPattern);
	                        	if(lNeedToCheckNextToken != null && (!"".equalsIgnoreCase(lNeedToCheckNextToken))){
	                        		lNeedToCheckNextToken = lNeedToCheckNextToken+" "+lActualPattern.toUpperCase();
	                        		//System.out.println(":::;;;lNeedToCheckNextToken:::"+lNeedToCheckNextToken+"$$$$$$$$$");
	                        		lPatternIndividualKeywordScoreDTO = getSingleKeywordScoreData(lNeedToCheckNextToken);
	                        		if(lPatternIndividualKeywordScoreDTO != null){
	                        			//System.out.println(":::::found 1 variable::::"+lNeedToCheckNextToken);
	                        			lPatternSummaryDTO = new PatternSummaryDTO();
	                                    lPatternSummaryDTO.setRunId(pRunId);
	                                    lPatternSummaryDTO.setPatternId(lPatternIndividualKeywordScoreDTO.getPatternId());
	                                    lPatternSummaryDTO.setPatternDesc(lPatternIndividualKeywordScoreDTO.getPatternDesc());
	                                    lPatternSummaryDTO.setScore(lPatternIndividualKeywordScoreDTO.getScore());
	                                    lPatternSummaryDTO.setKeyWord(lPatternIndividualKeywordScoreDTO.getKeyWord());
	                                    lPatternSummaryDTO.setStatement(lStatementLatest); 
	                                    lPatternSummaryDTO.setFormedStatement(lPatternIndividualKeywordScoreDTO.getKeyWord());
	                                    lPatternSummaryDTO.setStatementNo(lStatementNoLatest);
	                                    lPatternSummaryDTO.setProcedureName(lProcNameFirst);  
	                                    lPatternSummaryDTO.setQueryCount("0");
	                                    lPatternSummaryDTO.setDbMigrationType(pDbMigrationType);
	                                    lPatternSummaryDTO.setFolderPath(pStoredProceduresDetailsDTO.getFolderPath());
	                                    pPreparedStatement = insertSummary(lPatternSummaryDTO,lConnection , pPreparedStatement);
	                                    lKeywordDataInserted = true;
	                                    
	                        		}
	                        	}
	                        	if(lActualPattern != null && (!"".equals(lActualPattern))){
	                        		lNeedToCheckNextToken ="";
	                        	}
	                        }
	                        //System.out.println(":::::::::lNeedToCheckNextToken:::::"+lNeedToCheckNextToken);
	                        //for spaced keyword     - end
	                        
	                        
	                        if(count==0)
	                        {
	                            lPrevProcName=lProcNameFirst;
	                        }
	                        //System.out.println("::::lStatementNoLatest:::"+lStatementNoLatest+"::::lActualPattern::::"+lActualPattern+"::::lNeedToCheckNextToken::"+lNeedToCheckNextToken+"::::lKeywordDataInserted::::"+lKeywordDataInserted);
	                        if(("".equals(lNeedToCheckNextToken) && (lKeywordDataInserted == false)) ||(lMultiStringIgnoreAndInsertList.contains((String)lActualPattern.toUpperCase()))){
	                       //if("".equals(lNeedToCheckNextToken) && (lKeywordDataInserted == false)){
	                	lPatternIndividualKeywordScoreDTO = getSingleKeywordScoreData(lActualPattern);
	                	
	                        if((lPatternIndividualKeywordScoreDTO != null)  ){
	                            //System.out.println(":::::dto key word value not null::::::"+lPatternIndividualKeywordScoreDTO.getKeyWord()+","+lPatternIndividualKeywordScoreDTO.getPatternDesc()+","+lPatternIndividualKeywordScoreDTO.getPatternId()+","+lPatternIndividualKeywordScoreDTO.getScore() );
	                            lPatternSummaryDTO = new PatternSummaryDTO();
	                            lPatternSummaryDTO.setRunId(pRunId);
	                            lPatternSummaryDTO.setPatternId(lPatternIndividualKeywordScoreDTO.getPatternId());
	                            lPatternSummaryDTO.setPatternDesc(lPatternIndividualKeywordScoreDTO.getPatternDesc());
	                            lPatternSummaryDTO.setScore(lPatternIndividualKeywordScoreDTO.getScore());
	                            lPatternSummaryDTO.setKeyWord(lPatternIndividualKeywordScoreDTO.getKeyWord());
	                            lPatternSummaryDTO.setStatement(lStatementLatest);                        	
	                            lPatternSummaryDTO.setStatementNo(lStatementNoLatest);
	                            lPatternSummaryDTO.setProcedureName(lProcNameFirst);  
	                            lPatternSummaryDTO.setFormedStatement(lPatternIndividualKeywordScoreDTO.getKeyWord());
	                            lPatternSummaryDTO.setQueryCount("0");
	                            lPatternSummaryDTO.setDbMigrationType(pDbMigrationType);
	                            lPatternSummaryDTO.setFolderPath(pStoredProceduresDetailsDTO.getFolderPath());
	                            pPreparedStatement = insertSummary(lPatternSummaryDTO,lConnection , pPreparedStatement);
	                            //score=0;

	                           
	                        }
	                       }
	                        //else
	                        //{
	                        	//System.out.println("1: "+lActualPattern+"  Score"+score);
	                            
	                            if(!lPrevProcName.equalsIgnoreCase(lProcNameFirst.trim()))
	                            {
	                                //System.out.println("File Changed");
	                                lPrevProcName=lProcNameFirst;
	                                if(score>0)
	                                {
	                                    //System.out.println("2::::dto pat lStatementNo:::"+lStatementNo+"::pattern id::"+lPatternScoreDTO.getPatternId()+"::::lStatement::"+lStatement+"::::Score:::"+score+"::lChkStatementType"+lChkStatementType);
	                                    pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
	                                    
	                                    
	                                }
	                                score=0;
	                                lConcatenatedValue=new StringBuffer();
	                                lStatementNo = "" ;
	                                lStatement = "" ;
	                                lProcedureName = "";    
	                                lChkStatementType="";
	                                lIgnoreConstructsList = new ArrayList();
	                            }
	                            
	                           
	                            
	                            //Checking for SET To Determine whether its a new statement or its a part of update
	                            
	                          //  if(lActualPattern.trim().equalsIgnoreCase("UPDATE")){
	                          // 	lIsUpdateVisited=true;
	                         //   }
	                            
	                         //   if( lActualPattern.trim().equalsIgnoreCase("SET")){
	                           // 	if(lIsUpdateVisited==false){
	                            //		Pattern lDeclareContHandlerPattern = Pattern.compile("\\bDECLARE\\b\\s+\\bCONTINUE\\b\\s+\\bHANDLER\\b");
	                        		//	Matcher lPatternMatch= lDeclareContHandlerPattern.matcher(lConcatenatedValue.toString().toUpperCase()); 
	                        		//	if(!lPatternMatch.find()){
	                        				//System.out.println("lActualPattern::-> "+lActualPattern+" ::Statement No:::->"+lStatementNoLatest);
	                        		//		lStatementType=ToolConstant.STMT_TYPE_STATEMENT;
	                        		//	} 
	                        		//}else{
	                            		//lIsUpdateVisited=false;
	                            //	}
	                            	//System.out.println("Chcking for Set::-> "+lConcatenatedValue.toString());
	                            	
	                          //  }
	                        	
	                            if(ToolConstant.STMT_TYPE_STATEMENT.equalsIgnoreCase(lStatementType.trim()))
	                            {
	                            	
	                            	
	                            	//System.out.println("Select::::SCore;;;;;;;---->"+score);
	                                if(score>0)
	                                {                                	
	                                	//Checking for multiple statements
	                                	if(lActualPattern.trim().equalsIgnoreCase("select"))
	                                	{
	                                		//System.out.println(":LActualPattern:->"+lActualPattern+":lPrevPattern:-> "+lPrevPattern);
	                                		Pattern p1 = Pattern.compile("\\bUNION\\b[\\s]+\\bALL\\b");
	                            			Matcher m1= p1.matcher(lConcatenatedValue.toString().toUpperCase()); 
	                            			
	                                		if(lPrevPattern.trim().equalsIgnoreCase("(") || lPrevPattern.trim().equalsIgnoreCase(",") || (m1.find() && lPrevPattern.trim().equalsIgnoreCase("all")) )
	                                		{     
	                                			Pattern p = Pattern.compile("(\\s+if\\b[\\s\\w\\W]*\\bexists\\s*\\()");
	                                			Matcher m= p.matcher(lConcatenatedValue.toString().toLowerCase()); 
	                                			Pattern p3 = Pattern.compile("(\\s+elseif\\b[\\s\\w\\W]*\\bexists\\s*\\()");
	                                			Matcher m3= p3.matcher(lConcatenatedValue.toString().toLowerCase()); 
	                                			Pattern p4 = Pattern.compile("(\\s+where\\b[\\s\\w\\W]*\\bexists\\s*\\()");
	                                			Matcher m4= p4.matcher(lConcatenatedValue.toString().toLowerCase()); 
	                                			if(m.find()|m3.find()|m4.find())
	                                			{
	                                				
	                                				pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
	                                				lConcatenatedValue=new StringBuffer();
	                                				score=0;
	                                				lIgnoreConstructsList = new ArrayList();
	                                				lNumofStatements=1;
	                                			}
	                                			else
	                                			{
	                                				lNumofStatements++;
	                                			}
	                                			
	                                			
	                                			//System.out.println("0:LActualPattern:->"+lActualPattern+":lPrevPattern:-> "+lPrevPattern);
	                                		}
	                                		else
	                                		{
	                                			//System.out.println("::Select Else::--->"+lConcatenatedValue);
	                                			if((lPrevStatementType.equalsIgnoreCase("insert") && (score==(getHmScoreData("insert")+getHmScoreData("into"))))||
	                                					(lPrevStatementType.equalsIgnoreCase("insert") && (score==(getHmScoreData("insert")))) ||
	                                				( lPrevStatementType.equalsIgnoreCase("declare") && (score==(getHmScoreData("declare")+getHmScoreData("cursor")))))
	                                			{
	                                				lNumofStatements++;
	                                				
	                                			}
	                                			else /*if(!lPrevStatementType.trim().equalsIgnoreCase("declare"))*/
	                                			{
	                                				//System.out.println("::lNumofStatements= "+lNumofStatements+"  -1::::lConcatenatedValue;;;;;;;---->"+lConcatenatedValue);
	                                    			//System.out.println("1::lRunid:"+lRunid+"::lStatementNo:"+lStatementNo+"::lStatement:"+lStatement+"::lProcedureName::"+lProcedureName+" :Score::"+score+"::lChkStatementType"+lChkStatementType);                                				
	                                				pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
	                                                score=0; 
	                                                lIgnoreConstructsList = new ArrayList();
	                                                lNumofStatements=1;
	                                                lConcatenatedValue= new StringBuffer();                                                
	                                			}
	                                			/*else
	                                			{
	                                				score=0;
	                                				lIgnoreConstructsList = new ArrayList();
	                                				lNumofStatements=1;
	                                				lConcatenatedValue= new StringBuffer();
	                                			}*/
	                                			                              			
	                                		}
	                                		//System.out.println("::::: lActualPattern ::->"+lActualPattern);
	                                		
	                                	}
	                                	else /*if(!lPrevStatementType.trim().equalsIgnoreCase("declare"))*/
	                                	{
	                                		//System.out.println("::lNumofStatements= "+lNumofStatements+" -2::::lConcatenatedValue;;;;;;;---->"+lConcatenatedValue);
	                                		//System.out.println("2::lRunid:"+lRunid+"::lStatementNo:"+lStatementNo+"::lStatement:"+lStatement+"::lProcedureName::"+lProcedureName+" :Score::"+score+"::lChkStatementType"+lChkStatementType);                         		
	                                		pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
	                                		 score=0;
	                                		 lIgnoreConstructsList = new ArrayList();
	                                		 lNumofStatements=1;
	                                		lConcatenatedValue= new StringBuffer();
	                                       
	                                	}
	                                	/*else
	                        			{
	                        				score=0;
	                        				lIgnoreConstructsList = new ArrayList();
	                        				lNumofStatements=1;
	                        				lConcatenatedValue= new StringBuffer();
	                        			} */                             	
	                                	
	                                	/////                                                                       
	                                    
	                                    //lChkPrevStat+=2;                                
	                                }
	                                lStatementNo = lStatementNoLatest ;
	                                lStatement = lStatementLatest ;
	                                lProcedureName = lProcNameFirst;    
	                                lChkStatementType=lStatementType;
	                                lPrevStatementType=lActualPattern;
	                                
	                                score = score + getHmScoreData(lActualPattern);
	                                lConcatenatedValue.append(" "+lActualPattern+" ");
	                                lPrevPattern=lActualPattern;
	                                //System.out.println("1: "+lActualPattern+"  Score"+score);
	                                //lIsPrevStst=true;                            
	                            }
	                            else if(score>0)
	                            {
	                            	
	                            	//System.out.println("lActualPattern ::->"+lActualPattern+"  ::lIgnoreConstructsList size:::->"+lIgnoreConstructsList.size());
	                            	///////
	                            	if(lIgnoreConstructsList.size()>0){
	                            		if(lConstructsList.contains(lActualPattern.trim().toUpperCase())){
	                            			//System.out.println("HampValue::->"+ToolsUtil.replaceNull((String)lIgnoreConstructsHm.get( ((String)lIgnoreConstructsList.get(lIgnoreConstructsList.size()-1)).toUpperCase())));
	                            			if((ToolsUtil.replaceNull((String)lIgnoreConstructsHm.get( ((String)lIgnoreConstructsList.get(lIgnoreConstructsList.size()-1)).toUpperCase()  ))).equalsIgnoreCase(lActualPattern.trim())){
	                            				lIgnoreConstructsList.remove(lIgnoreConstructsList.size()-1);
	                            				
	                            				//lIgnoreConstructsHm.get(lPrevConstructsList.get(lPrevConstructsList.size()-1));                            				
	                            			}
	                            			else
	                            			{	
	                            				if(lIgnoreConstructsHm.containsKey(lActualPattern.trim())){
	                            				lIgnoreConstructsList.add(lActualPattern.trim());
	                            				
	                            				}
	                            				
	                            			}
	                                	}
	                            			score = score + getHmScoreData(lActualPattern);
	                                		lConcatenatedValue.append(" "+lActualPattern+" ");
	                            		
	                            		
	                            		
	                            		
	                            	}else
	                            	{
	                            		if(lConstructsList.contains(lActualPattern.trim().toUpperCase())){
	                            			
	                            			//System.out.print(lIgnoreConstructsHm.containsKey(lActualPattern.trim().toUpperCase())+"\n");
	                            			
	                            			if(lIgnoreConstructsHm.containsKey(lActualPattern.trim().toUpperCase())){
	                            				lIgnoreConstructsList.add(lActualPattern.trim());
	                            				score = score + getHmScoreData(lActualPattern);
	                                    		lConcatenatedValue.append(" "+lActualPattern+" ");
	                            			}else{
	                            				/*if(!((lPrevStatementType.trim().equalsIgnoreCase("declare"))&& (score==(getHmScoreData("declare"))))){
	                            					pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,lProcedureName,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement);
	                            				}*/
	                            				pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
	                                   		 	score=0;
	                                   		 	lIgnoreConstructsList = new ArrayList();
	                                   		 	lNumofStatements=1;
	                                   		 	lConcatenatedValue= new StringBuffer();                            			
	                            			}                            				
	                            			
	                                	}
	                            		else{
	                            			//if(getHmScoreData(lActualPattern)>0)
	                                		score = score + getHmScoreData(lActualPattern);
	                                		lConcatenatedValue.append(" "+lActualPattern+" ");
	                            			
	                            		}
	                            		
	                            	}
	                            	
	                            	/////
	                            	
	                            	/*if(!lConstructsList.contains(lActualPattern.trim().toUpperCase()))
	                            	{
	                            		//if(getHmScoreData(lActualPattern)>0)
	                            		score = score + getHmScoreData(lActualPattern);
	                            		lConcatenatedValue.append(" "+lActualPattern+" ");
	                                    
	                            	}*/
	                            	lPrevPattern=lActualPattern;
	                            	
	                            }
	                            
	                        //}
	                        
	                       
	                count++;
	               
	                }
	                if((score>0 ))//&& (!lPrevStatementType.trim().equalsIgnoreCase("declare")))
	                {
	                	
	                	//System.out.println("::lNumofStatements= "+lNumofStatements+" -::::3ConcatenatedValue;;;;;;;---->"+lConcatenatedValue);                	
	        			pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
	        			lConcatenatedValue=new StringBuffer();
	        			score=0;
	        			lIgnoreConstructsList = new ArrayList();
	        			lNumofStatements=1;
	    				//pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,lProcedureName,score,lChkStatementType,lstr,pPreparedStatement);
	                    
	                }
	            }
	            
	            //System.out.println(":::insert batach start:::");
	            pPreparedStatement.executeBatch();
	            lConnection.commit();
	            lConnection.setAutoCommit(true);
	        }catch(SQLException se ){
	            se.printStackTrace();
	            return null;
	        }catch(Exception e ){
	            e.printStackTrace();
	            return null;
	        }finally{
	            //close the connection and the result set
	            DBConnectionManager.closeConnection(lPreparedStatement,lResultSet);
	            DBConnectionManager.closeConnection(pPreparedStatement,null);
	           // DBConnectionManager.closeConnection(lConnection);           
	        }
	        
	        return lScoreList;
	}
	
	
	
	/**********************************************for sybase to sql*******************************************************************************/
    public List getParsedDataSybaseToSQLServer(String pRunId,StoredProceduresDetailsDTO pStoredProceduresDetailsDTO,String pDbMigrationType){
    	//System.out.println("::::::in Method:::->getParsedDataSybaseToSQLServer");
        String retValue="";  
        boolean lCanCaoncatenate= false;
        List lConcatenatedList = new ArrayList();
        List lScoreList = new ArrayList();
        List lSummaryList = new ArrayList();
        
        
        PatternSummaryDTO lPatternSummaryDTO = new PatternSummaryDTO();
        PatternSummaryDTO lPatternSummaryDTOTemp = new PatternSummaryDTO();
        PatternScoreDTO lPatternScoreDTO = new PatternScoreDTO();
        PatternIndividualKeywordScoreDTO lPatternIndividualKeywordScoreDTO = null;
        
        PreparedStatement pPreparedStatement = null;
        String lStatementNo ="";
        String lStatement = "";
        String lProcedureName="";
        String lPrevPattern="";
        List lConstructsList = new ArrayList();       
        int lNumofStatements=1;       
        List lIgnoreConstructsList =new ArrayList();
        try{
            
        	lConnection.setAutoCommit(false);
        	pPreparedStatement = lConnection.prepareStatement(ToolConstant.PATTERN_ANALYSYS_INSERT_SUMMARY);
        	
        	List lMultiStringList = new ArrayList();
        	lMultiStringList.add((String)"STR");
        	lMultiStringList.add((String)"OUTER");
        	lMultiStringList.add((String)"=");
        	lMultiStringList.add((String)"!=");
        	lMultiStringList.add((String)"!>");
        	lMultiStringList.add((String)"!<");
        	lMultiStringList.add((String)"IS");
        	lMultiStringList.add((String)"IS NOT");
        	lMultiStringList.add((String)"NOT");
        	
        	HashMap lIgnoreConstructsHm= new HashMap();
        	lIgnoreConstructsHm.put((String)"CASE", (String)"END");
        	
        	
        	
           
            StringBuffer lConcatenatedValue= new StringBuffer();
            //select the data from the table
            //if required we can add % in the like operator
            //lConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //lPreparedStatement = lConnection.prepareStatement(ToolConstant.PATTERN_ANALYSYS_GET_PARSED_DATA,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //System.out.println(":::::before executing select:::::");
           String lSql = "SELECT "
    			+ " RUN_ID,PROCEDURE_NAME,STATEMENT,STATEMENT_NO,ACTUAL_PATTERN,STATEMENT_TYPE,ORDER_NO "
    			+ " FROM PARSE_RESULTS_TABLE " + " WHERE RUN_ID= ?  and PROCEDURE_NAME =? "
    			+ " ORDER BY  PROCEDURE_NAME,ORDER_NO ";
            lPreparedStatement = lConnection.prepareStatement(lSql);
            
            lPreparedStatement.setString(1, pRunId );
            lPreparedStatement.setString(2, pStoredProceduresDetailsDTO.getProcName() );
            //System.out.println("::::pRunId::::"+pRunId+"$$$$");
            lResultSet = lPreparedStatement.executeQuery();
            //System.out.println(":::::after executing select:::::");
            //lResultSet.last();
           // int lRowCount = lResultSet.getRow();            
          // System.out.println(":::::res size::::"+lRowCount);
          // lResultSet.first();
            
            lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_ALL_CONSTRUCTS);
            lPreparedStatement.setString(1, pDbMigrationType);
            ResultSet rs=lPreparedStatement.executeQuery();
            while(rs.next())
            {
            	//System.out.println("::::Keyword:::"+rs.getString(2));            	
            	lConstructsList.add(rs.getString(2).trim().toUpperCase());
            	
            }

           
            int i=0;
            int score=0;
            String lActualPattern ="";
            String lProcNameFirst ="";
            int procCount =0;
            String lStatementNoLatest ="";
            String lStatementLatest ="";
            String lStatementType="";
            String lOrdNum="";
            String lPrevProcName="";
            String lChkStatementType="";
            int lChkPrevStat=0;
            String lRunid="";
            String lPrevStatementType="";
            
            
            
            //System.out.println("::::while loop starting");
            //boolean lIsPrevStst=false;
            int count=0;
            lPatternSummaryDTO = new PatternSummaryDTO();
            String lNeedToCheckNextToken="";
            boolean lKeywordDataInserted = false;
            if (lResultSet != null){               
                while(lResultSet.next()){
                       
                	lRunid=lResultSet.getString("RUN_ID");
                	lProcNameFirst = lResultSet.getString("PROCEDURE_NAME");
                        //System.out.println("lProcNameFirst------>"+lProcNameFirst);
                	procCount = Integer.parseInt((String)lProcCountMap.get(lProcNameFirst)); //returns number of rows (order no) procedurewise
                	//System.out.println("::::inside result set:::i val->"+i+"count Value->"+procCount);
                        lActualPattern =lResultSet.getString("ACTUAL_PATTERN");
                	lStatementNoLatest  = lResultSet.getString("STATEMENT_NO");
                        lStatementLatest = lResultSet.getString("STATEMENT");
                        lStatementType  = lResultSet.getString("STATEMENT_TYPE");
                        lOrdNum  = lResultSet.getString("ORDER_NO");  
                        
                        
                        
                        
                        //for spaced keyword    - start
                        lKeywordDataInserted = false;
                        if(lMultiStringList.contains((String)lActualPattern.toUpperCase())){
                        	if("".equals(lNeedToCheckNextToken)){
                        		lNeedToCheckNextToken = lActualPattern.toUpperCase();
                        	}else{
                        		lNeedToCheckNextToken = lNeedToCheckNextToken+" "+lActualPattern.toUpperCase();
                        	}
                        	//System.out.println(":::;;;first addition:::"+lNeedToCheckNextToken);
                        }else{
                        	//System.out.println(":::inside else::::"+lActualPattern);
                        	if(lNeedToCheckNextToken != null && (!"".equalsIgnoreCase(lNeedToCheckNextToken))){
                        		lNeedToCheckNextToken = lNeedToCheckNextToken+" "+lActualPattern.toUpperCase();
                        		//System.out.println(":::;;;lNeedToCheckNextToken:::"+lNeedToCheckNextToken+"$$$$$$$$$");
                        		lPatternIndividualKeywordScoreDTO = getSingleKeywordScoreData(lNeedToCheckNextToken);
                        		if(lPatternIndividualKeywordScoreDTO != null){
                        			//System.out.println(":::::found 1 variable::::"+lNeedToCheckNextToken);
                        			lPatternSummaryDTO = new PatternSummaryDTO();
                                    lPatternSummaryDTO.setRunId(pRunId);
                                    lPatternSummaryDTO.setPatternId(lPatternIndividualKeywordScoreDTO.getPatternId());
                                    lPatternSummaryDTO.setPatternDesc(lPatternIndividualKeywordScoreDTO.getPatternDesc());
                                    lPatternSummaryDTO.setScore(lPatternIndividualKeywordScoreDTO.getScore());
                                    lPatternSummaryDTO.setKeyWord(lPatternIndividualKeywordScoreDTO.getKeyWord());
                                    lPatternSummaryDTO.setStatement(lStatementLatest); 
                                    lPatternSummaryDTO.setFormedStatement(lPatternIndividualKeywordScoreDTO.getKeyWord());
                                    lPatternSummaryDTO.setStatementNo(lStatementNoLatest);
                                    lPatternSummaryDTO.setProcedureName(lProcNameFirst);  
                                    lPatternSummaryDTO.setQueryCount("0");
                                    lPatternSummaryDTO.setDbMigrationType(pDbMigrationType);
                                    lPatternSummaryDTO.setFolderPath(pStoredProceduresDetailsDTO.getFolderPath());
                                    pPreparedStatement = insertSummary(lPatternSummaryDTO,lConnection , pPreparedStatement);
                                    lKeywordDataInserted = true;
                                    
                        		}
                        	}
                        	if(lActualPattern != null && (!"".equals(lActualPattern))){
                        		lNeedToCheckNextToken ="";
                        	}
                        }
                        
                        //for spaced keyword     - end
                        
                        
                        if(count==0)
                        {
                            lPrevProcName=lProcNameFirst;
                            lStatementNo = lStatementNoLatest ;
                            lStatement = lStatementLatest ;
                        }
                        //System.out.println("::::lStatementNoLatest:::"+lStatementNoLatest+"::::lActualPattern::::"+lActualPattern+"::::lNeedToCheckNextToken::"+lNeedToCheckNextToken+"::::lKeywordDataInserted::::"+lKeywordDataInserted);
                        
                       if("".equals(lNeedToCheckNextToken) && (lKeywordDataInserted == false)){
                	lPatternIndividualKeywordScoreDTO = getSingleKeywordScoreData(lActualPattern);
                	
                        if((lPatternIndividualKeywordScoreDTO != null)  ){
                            //System.out.println(":::::dto key word value not null::::::"+lPatternIndividualKeywordScoreDTO.getKeyWord()+","+lPatternIndividualKeywordScoreDTO.getPatternDesc()+","+lPatternIndividualKeywordScoreDTO.getPatternId()+","+lPatternIndividualKeywordScoreDTO.getScore() );
                            lPatternSummaryDTO = new PatternSummaryDTO();
                            lPatternSummaryDTO.setRunId(pRunId);
                            lPatternSummaryDTO.setPatternId(lPatternIndividualKeywordScoreDTO.getPatternId());
                            lPatternSummaryDTO.setPatternDesc(lPatternIndividualKeywordScoreDTO.getPatternDesc());
                            lPatternSummaryDTO.setScore(lPatternIndividualKeywordScoreDTO.getScore());
                            lPatternSummaryDTO.setKeyWord(lPatternIndividualKeywordScoreDTO.getKeyWord());
                            lPatternSummaryDTO.setStatement(lStatementLatest);                        	
                            lPatternSummaryDTO.setStatementNo(lStatementNoLatest);
                            lPatternSummaryDTO.setProcedureName(lProcNameFirst);  
                            lPatternSummaryDTO.setFormedStatement(lPatternIndividualKeywordScoreDTO.getKeyWord());
                            lPatternSummaryDTO.setQueryCount("0");
                            lPatternSummaryDTO.setDbMigrationType(pDbMigrationType);
                            lPatternSummaryDTO.setFolderPath(pStoredProceduresDetailsDTO.getFolderPath());
                            pPreparedStatement = insertSummary(lPatternSummaryDTO,lConnection , pPreparedStatement);
                            //score=0;

                           
                        }
                       }
                        //else
                        //{
                        	//System.out.println("1: "+lActualPattern+"  Score"+score);
                       lPatternScoreDTO = getSinglePatternScoreData(score+"");
                       
                            if(!lPrevProcName.equalsIgnoreCase(lProcNameFirst.trim()))
                            {
                                //System.out.println("File Changed");
                                lPrevProcName=lProcNameFirst;
                                if(score>0)
                                {
                                    pPreparedStatement=chkPatternForSQLServer(lRunid, lStatement, lStatementNo, pStoredProceduresDetailsDTO, score, lStatementType, lConcatenatedValue.toString(), lNumofStatements, pPreparedStatement, lPatternScoreDTO, lChkStatementType,pDbMigrationType);                                    
                                    score=0;
                                    lConcatenatedValue=new StringBuffer();
                                    //lStatementNo = "" ;
                                    //lStatement = "" ;
                                    lProcedureName = "";    
                                    lChkStatementType="";
                                    lNumofStatements=1;
                                    lStatementNo = lStatementNoLatest ;
                                    lStatement = lStatementLatest ;
                                }
                            }
                            //System.out.println("lActualPattern:::->"+lActualPattern+"::lStatementType::->"+lStatementType+"::sCORE->"+getHmScoreData(lActualPattern));
                            if(ToolConstant.STMT_TYPE_STATEMENT.equalsIgnoreCase(lStatementType.trim()))
                            {    
                                if(score>0)
                                {                                	
                                	//Chceking for multiple statemets
                                	if(lActualPattern.trim().equalsIgnoreCase("select"))
                                	{
                                		//System.out.println(":LActualPattern:->"+lActualPattern+":lPrevPattern:-> "+lPrevPattern);
                                		if(lPrevPattern.trim().equalsIgnoreCase("("))
                                		{     
                                			Pattern p = Pattern.compile("\\s+if\\b[\\s\\w]*\\bexists\\s+");
                                			Matcher m= p.matcher(lConcatenatedValue.toString());                                			
                                			if(m.find())
                                			{
                                				
                                				pPreparedStatement=chkPatternForSQLServer(lRunid, lStatement, lStatementNo, pStoredProceduresDetailsDTO, score, lStatementType, lConcatenatedValue.toString(), lNumofStatements, pPreparedStatement, lPatternScoreDTO, lChkStatementType,pDbMigrationType);
                                				lStatementNo = lStatementNoLatest ;
                                				lStatement = lStatementLatest ;
                                				lConcatenatedValue=new StringBuffer();
                                				score=0;
                                				lNumofStatements=1;
                                			}
                                			else
                                			{
                                				lNumofStatements++;
                                			}
                                			
                                			//System.out.println("0:LActualPattern:->"+lActualPattern+":lPrevPattern:-> "+lPrevPattern);
                                		}
                                		else
                                		{	
                                			pPreparedStatement=chkPatternForSQLServer(lRunid, lStatement, lStatementNo, pStoredProceduresDetailsDTO, score, lStatementType, lConcatenatedValue.toString(), lNumofStatements, pPreparedStatement, lPatternScoreDTO, lChkStatementType,pDbMigrationType);
                                			lStatementNo = lStatementNoLatest ;
                                			lStatement = lStatementLatest ;
                                                score=0; 
                                                lNumofStatements=1;
                                                lConcatenatedValue= new StringBuffer();                                                
                                			
                                			                              			
                                		}
                                		//System.out.println("::::: lActualPattern ::->"+lActualPattern);
                                		
                                	}
                                	else
                                	{
                                		
                                		pPreparedStatement=chkPatternForSQLServer(lRunid, lStatement, lStatementNo, pStoredProceduresDetailsDTO, score, lStatementType, lConcatenatedValue.toString(), lNumofStatements, pPreparedStatement, lPatternScoreDTO, lChkStatementType,pDbMigrationType);
                                		lStatementNo = lStatementNoLatest ;
                                		lStatement = lStatementLatest ;
                                		 score=0;
                                		 lNumofStatements=1;
                                		lConcatenatedValue= new StringBuffer();
                                       
                                	}                          	
                                	
                                	/////                                                                       
                                    
                                    //lChkPrevStat+=2;                                
                                
                    				}
                                
                                //lStatement = lStatementLatest ;
                                lProcedureName = lProcNameFirst;    
                                lChkStatementType=lStatementType;
                                lPrevStatementType=lActualPattern;
                                
                                score = score + getHmScoreData(lActualPattern);
                                lConcatenatedValue.append(" "+lActualPattern+" ");
                                lPrevPattern=lActualPattern;
                                //System.out.println("1: "+lActualPattern+"  Score"+score);
                                //lIsPrevStst=true;                            
                            }
                            else if(score>0)
                            {
                            	/*score = score + getHmScoreData(lActualPattern);
                        		lConcatenatedValue.append(" "+lActualPattern+" ");*/
                            	
                            	//System.out.println("2: "+lActualPattern+"  Score"+score);
                            	///////
                            	if(lIgnoreConstructsList.size()>0){
                            		if(lConstructsList.contains(lActualPattern.trim().toUpperCase())){
                            			if((ToolsUtil.replaceNull((String)lIgnoreConstructsHm.get(lIgnoreConstructsList.get(lIgnoreConstructsList.size()-1)))).equalsIgnoreCase(lActualPattern.trim())){
                            				lIgnoreConstructsList.remove(lIgnoreConstructsList.size()-1);score = score + getHmScoreData(lActualPattern);
                                    		lConcatenatedValue.append(" "+lActualPattern+" ");
                            				
                            				//lIgnoreConstructsHm.get(lPrevConstructsList.get(lPrevConstructsList.size()-1));                            				
                            			}
                            			else
                            			{	
                            				if(lIgnoreConstructsHm.containsKey(lActualPattern.trim())){
                            				lIgnoreConstructsList.add(lActualPattern.trim());
                            				score = score + getHmScoreData(lActualPattern);
                                    		lConcatenatedValue.append(" "+lActualPattern+" ");
                            				}
                            				
                            			}
                                	}
                            		else
                            		{
                            			score = score + getHmScoreData(lActualPattern);
                                		lConcatenatedValue.append(" "+lActualPattern+" ");
                            		}
                            			/*score = score + getHmScoreData(lActualPattern);
                                		lConcatenatedValue.append(" "+lActualPattern+" ");*/
                            		
                            		
                            		
                            		
                            	}else
                            	{                         		
                            		
                            		if(lConstructsList.contains(lActualPattern.trim().toUpperCase())){
                            			
                            			//System.out.print(lIgnoreConstructsHm.containsKey(lActualPattern.trim().toUpperCase())+"\n");
                            			
                            			if(lIgnoreConstructsHm.containsKey(lActualPattern.trim().toUpperCase())){
                            				lIgnoreConstructsList.add(lActualPattern.trim());
                            				score = score + getHmScoreData(lActualPattern);
                                    		lConcatenatedValue.append(" "+lActualPattern+" ");
                            			}
                            			
                                	}
                            		else{
                            			//if(getHmScoreData(lActualPattern)>0)
                                		score = score + getHmScoreData(lActualPattern);
                                		lConcatenatedValue.append(" "+lActualPattern+" ");
                            			
                            		}
                            		
                            	}
                            	
                            	/*if(!lConstructsList.contains(lActualPattern.trim().toUpperCase()))
                            	{
                            		//if(getHmScoreData(lActualPattern)>0)
                            		score = score + getHmScoreData(lActualPattern);
                            		lConcatenatedValue.append(" "+lActualPattern+" ");
                                    
                            	}*/
                            	lPrevPattern=lActualPattern;
                            	
                            	
                            	
                            }
                            
                        //}
                        
                //System.out.println("lActualPattern::->"+lActualPattern); 
                count++;
               
                }
                if(score>0 )
                {                	
                	pPreparedStatement=chkPatternForSQLServer(lRunid, lStatement, lStatementNo, pStoredProceduresDetailsDTO, score, lStatementType, lConcatenatedValue.toString(), lNumofStatements, pPreparedStatement, lPatternScoreDTO, lChkStatementType,pDbMigrationType);
        			lConcatenatedValue=new StringBuffer();
        			score=0;
        			lNumofStatements=1;
    				//pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,lProcedureName,score,lChkStatementType,lstr,pPreparedStatement);
                    
                }
            }
            
            //System.out.println(":::insert batach start:::");
            pPreparedStatement.executeBatch();
            lConnection.commit();
            lConnection.setAutoCommit(true);
        }catch(SQLException se ){
            se.printStackTrace();
            return null;
        }catch(Exception e ){
            e.printStackTrace();
            return null;
        }finally{
            //close the connection and the result set
            DBConnectionManager.closeConnection(lPreparedStatement,lResultSet);
            DBConnectionManager.closeConnection(pPreparedStatement,null);
           // DBConnectionManager.closeConnection(lConnection);           
        }
        
        return lScoreList;
    
    }
    public PreparedStatement chkPatternForSQLServer(String lRunid,String lStatement,String lStatementNo,StoredProceduresDetailsDTO pStoredProceduresDetailsDTO/*String lProcedureName*/,int score,String lStatementType,String lConcatenatedValue ,int pNumofStatements,PreparedStatement pPreparedStatement,PatternScoreDTO lPatternScoreDTO,String pChkStatementType,String pDbMigrationType){
    	
    	String lFormedStatement=lConcatenatedValue.toUpperCase().trim(); 
    	int lNumofStatements=pNumofStatements;
    	String lChkStatementType=pChkStatementType;    	
    	if(lFormedStatement.toUpperCase().contains("UPDATE ") || lFormedStatement.toUpperCase().contains("DELETE ")  )
    	{
    		if(lNumofStatements==1)
    		{
    			
    			if(lFormedStatement.contains(" FROM ")){
        			String[] lFormedStatementTockens=lFormedStatement.substring(lFormedStatement.toUpperCase().indexOf(" FROM "),lFormedStatement.length()-1).split("\\s+");
            		List lChkAliasList=new ArrayList();
            		//System.out.println(":::Tokens"+lFormedStatementTockens.length);
            		for (int j = 3; j < lFormedStatementTockens.length; j+= 3) {
						if((!"".equals(lFormedStatementTockens[j-2])) && (lFormedStatementTockens[j].equalsIgnoreCase(",")||lFormedStatementTockens[j].equalsIgnoreCase(" WHERE ")) ){
							lChkAliasList.add(lFormedStatementTockens[j-2]);
							/*System.out
									.println(lFormedStatementTockens[j-2]);*/
							
						}
					}
            		String[] lUpdateStatementTableNames;
            		if(lFormedStatement.toUpperCase().contains("UPDATE "))
            		{
            			lUpdateStatementTableNames=lFormedStatement.substring(lFormedStatement.toUpperCase().indexOf("UPDATE ")+7,lFormedStatement.length()-1).split("\\s+");
            		}
            		else
            		{
            			lUpdateStatementTableNames=lFormedStatement.substring(lFormedStatement.toUpperCase().indexOf("DELETE ")+7,lFormedStatement.length()-1).split("\\s+");
            		}
            		
            		if(lChkAliasList.contains(lUpdateStatementTableNames[0])){
            			//System.out.println("3:::::lConcatenatedValue;;;;;;;---->"+lConcatenatedValue);
            			score=score+1; //Adding 1 to identify this is a normal statement but Aliased.
            			pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
            			return pPreparedStatement;
            		}                                        			
        		}
    			if(lFormedStatement.toUpperCase().contains("UPDATE ")&& !(lPatternScoreDTO.getPatternId().equalsIgnoreCase("PAT_E")))
    			{
    				pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
    				return pPreparedStatement;
    			}
    			
    		}
    		else
    		{
    			if(lFormedStatement.toUpperCase().contains("UPDATE "))                			
        		{
        			if(lFormedStatement.toUpperCase().contains(" SUM")||lFormedStatement.toUpperCase().contains(" MIN")||lFormedStatement.toUpperCase().contains(" MAX")|| lFormedStatement.toUpperCase().contains("AVG"))
        			{
        				score=10000;
        				pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
        				return pPreparedStatement;
        			}
        			else
        			{
        				score=getHmScoreData("Update")+getHmScoreData("SET")+1;
        				pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
        				return pPreparedStatement;
        			}
        		}
    			
    		}   
    	}                            		
    	else /*if(lNumofStatements >=2* &&*/if( lPatternScoreDTO.getPatternId().equalsIgnoreCase("PAT_E"))
    	{
    		//System.out.println("4:::::lConcatenatedValue;;;;;;;---->"+lConcatenatedValue);
    		
    		if(lFormedStatement.toUpperCase().contains("SELECT "))
    		{
    			if(lFormedStatement.contains(" DISTINCT ")&&lFormedStatement.contains(" ORDER "))
    			{
    				score=getHmScoreData("Select")+getHmScoreData("Distinct")+getHmScoreData("Order");
        			pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
        			return pPreparedStatement;    				
    			}
    			else if(lFormedStatement.toUpperCase().contains(" ORDER "))
    			{
    				score=getHmScoreData("Select")+getHmScoreData("Distinct")+getHmScoreData("Order");
        			pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
        			return pPreparedStatement;    				
    			}
    			else if(lFormedStatement.toUpperCase().contains(" DISTINCT ") && lFormedStatement.contains(" Having "))
    			{
    				score=getHmScoreData("Select")+getHmScoreData("Distinct")+getHmScoreData("Having");
        			pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
        			return pPreparedStatement;    				
    			}
    			else if(lFormedStatement.toUpperCase().contains(" GROUP ") )
        		{
        			score=getHmScoreData("Select")+getHmScoreData("Group");
        			pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
        			return pPreparedStatement;
        			
        		}
    			else if(lFormedStatement.toUpperCase().contains(" HAVING ") )
        		{
        			score=getHmScoreData("Select")+getHmScoreData("Having");
        			pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
        			return pPreparedStatement;        			
        		}
    		}
    			
    		
    	}
    	else if(!lPatternScoreDTO.getPatternId().equalsIgnoreCase("PAT_E")){
    		pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,pStoredProceduresDetailsDTO,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement,pDbMigrationType);
    		return pPreparedStatement;
    	}
    	
    	return pPreparedStatement;
    }
    	

    /**
   	 * method to get the complete list of the data
   	 * @return
   	 */
    
    //method not used
   	public void getSummaryExcel(String pRunId,String pFileName) {
   		
   		
   		try {
   			
   			lConnection = DBConnectionManager.getConnection();
                          String filename="C:\\arun\\Tool Output\\Liberty\\"+pFileName+".xls";
                          //BufferedWriter writer = new BufferedWriter(new FileWriter());

   			// replacing the special characters in the query string

   			// select the data from the table
   			// if required we can add % in the like operator
   			
                          lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_PATTERN_DATA);
                          lPreparedStatement.setString(1,pRunId);
                          //lPreparedStatement = lConnection.prepareStatement("SELECT RUN_ID,PROCEDURE_NAME,STATEMENT,STATEMENT_NO,KEY_WORD,SCORE,PATTERN_ID,PATTERN_DESC,FORMED_STATEMENT,CREATED_BY,CREATED_DATE,QUERY_COUNT FROM PATTERN_RESULTS_TABLE WHERE RUN_ID in(27,31,32,33,34,35) ORDER BY PROCEDURE_NAME,STATEMENT_NO");          	
                          //lPreparedStatement.setString(1,pRunId);
                          lPreparedStatement.setFetchSize(Integer.MIN_VALUE);
   			int i = 0;
   			lResultSet = lPreparedStatement.executeQuery();
   			// if rs == null, then there is no ResultSet to view
   			String lStatementCont = "";
   			String lStatementType = "";
   			String lPatternId="";
   			String lFormedStatement="";
   			String lExceptionPatternDesc="";
   			String lPatternDesc="";
   			String lQueryCount="";
                          StringBuffer sbGetData= new StringBuffer();
                          String lSeperator=";";
                          
                          HSSFWorkbook hwb=new HSSFWorkbook();
                  		HSSFSheet sheet = hwb.createSheet("new sheet");
                  		String colValue="Hi";
                  		
                  		HSSFRow rowhead= sheet.createRow((short)0);
                  		rowhead.createCell((short) 0).setCellValue("S.No");
                  		rowhead.createCell((short) 1).setCellValue("Stored Procedure Name");
                  		rowhead.createCell((short) 2).setCellValue("Statement");
                  		rowhead.createCell((short) 3).setCellValue("Statement No");
                  		rowhead.createCell((short) 4).setCellValue("Score");
                  		rowhead.createCell((short) 5).setCellValue("Key Word");
                  		rowhead.createCell((short) 6).setCellValue("pattern Id");
                  		rowhead.createCell((short) 7).setCellValue("pattern Description");
                  		rowhead.createCell((short) 8).setCellValue("Sub Query Count");
                  		rowhead.createCell((short) 9).setCellValue("Formed Statement");
                  		
                  		
                  		
                          
   			if (lResultSet != null) {
   				// this will step through our data row-by-row
   				while (lResultSet.next()) {
   					
   					lPatternId=lResultSet.getString("PATTERN_ID");
   					lFormedStatement=lResultSet.getString("FORMED_STATEMENT");
   					lExceptionPatternDesc="Pattern With ";
   					lPatternDesc=lResultSet.getString("PATTERN_DESC");
   					lQueryCount=lResultSet.getString("QUERY_COUNT");
   					
                                      
                      HSSFRow row= sheet.createRow((short)(i+1));
      				row.createCell((short) 0).setCellValue((i+1)+"");
      				row.createCell((short) 1).setCellValue(lResultSet.getString("PROCEDURE_NAME"));
      				row.createCell((short) 2).setCellValue(lResultSet.getString("STATEMENT"));
      				row.createCell((short) 3).setCellValue(lResultSet.getString("STATEMENT_NO"));
      				row.createCell((short) 4).setCellValue(lResultSet.getString("SCORE"));
      				row.createCell((short) 5).setCellValue(lResultSet.getString("KEY_WORD"));
      				row.createCell((short) 6).setCellValue(lPatternId);
      				row.createCell((short) 7).setCellValue(lPatternDesc/*getExceptionedPatternWithId(lPatternId,lPatternDesc,lFormedStatement, lQueryCount)*/);
      				row.createCell((short) 8).setCellValue(lQueryCount);
      				row.createCell((short) 9).setCellValue(lFormedStatement);
      				
                      i++;     
   				}
   			}
   			
   		


   			
  	

   			FileOutputStream fileOut = new FileOutputStream(filename);
   			hwb.write(fileOut);
   			fileOut.close();
   			
                       

   		} catch (SQLException se) {
   			se.printStackTrace();
   			//return null;
   		} catch (Exception e) {
   			e.printStackTrace();
   			//return null;
   		} finally {
   			// close the connection and the result set
   			DBConnectionManager.closeConnection(lPreparedStatement,
   					lResultSet);
   			DBConnectionManager.closeConnection(lConnection);
   		}

   		//return lhmPatternData;
   	}
       
   	//method not used
      public void getProjectData(){
          
      	PatternIndividualKeywordScoreDTO lPatternIndividualKeywordScoreDTO = new PatternIndividualKeywordScoreDTO();
      	List lPatternScoreList = new ArrayList();
      	ResultSet lResultSet = null;
      	PreparedStatement lPreparedStatement = null;
          try{
              lConnection = DBConnectionManager.getConnection();
              
              // select the data from the table
              lPreparedStatement = lConnection.prepareStatement("SELECT project_id,REPLACE(project_name, 'CNA_', '') project_name  FROM tool_project_details t where target_db_type='SQL_SERVER' and project_name !='test proj';");
              //lPreparedStatement.setString(1, pDbMigrationType);
              lResultSet = lPreparedStatement.executeQuery();
              // if rs == null, then there is no ResultSet to view
              
              if (lResultSet != null){
                  // this will step through our data row-by-row
                  while(lResultSet.next()){
                  	System.out.println(":::proj id:::"+lResultSet.getString("project_id")+":::::proj name:::"+lResultSet.getString("project_name"));
                  	getSummaryExcel(lResultSet.getString("project_id")+"_SOURCE",lResultSet.getString("project_name"));
                  	//getSummaryList(lResultSet.getString("project_id")+"_SOURCE",lResultSet.getString("project_name"));
                  }
              }
              
          }catch(SQLException se ){
              se.printStackTrace();
             // return null;
          }catch(Exception e ){
              e.printStackTrace();
             // return null;
          }finally{
              //close the connection and the result set
              DBConnectionManager.closeConnection(lPreparedStatement,lResultSet);
              DBConnectionManager.closeConnection(lConnection);           
          }
          
         // return lPatternScoreList;
      }
	
      public int insertSPSourceLocation(String pRunId,String pProcName,String pFileLocation,String pCreatedBy){
    	  PreparedStatement lPreparedStatement = null;
    	  int lReturnCount = 0 ;
    	  try{
    		  lPreparedStatement =lConnection.prepareStatement(ToolConstant.INSERT_SP_LOCATION_DETAILS);
    		  lPreparedStatement.setString(1, pRunId);
    		  lPreparedStatement.setString(2, pFileLocation);
    		  lPreparedStatement.setString(3, pCreatedBy);
    		  lPreparedStatement.setTimestamp(4, new Timestamp(System
     					.currentTimeMillis()));
    		  lPreparedStatement.setString(5, pProcName);
    		  System.out.println(":::::::insertSPSourceLocation-lPreparedStatement"+lPreparedStatement);
    		  lReturnCount = lPreparedStatement.executeUpdate();
    		  System.out.println(":::::::insertSPSourceLocation-lReturnCount"+lReturnCount);
    		  
    	  }catch (SQLException e) {
			// TODO: handle exception
    		  e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			DBConnectionManager.closeConnection(lPreparedStatement, null);
		}
    	  return lReturnCount;
      }
        public static void main(String [] args)
        {
        	
        	PatternAnalysisSingleDAO pads=new PatternAnalysisSingleDAO();
        	//pads.getParsedDataWhole("449","SYSBASE_TO_DB2");
        	//pads.updatePatternException("449");
            //new 318-320
        	//new 27,31,32,33,34,35
        	//189 for full details-18.10.2011
        	
            //pads.getParsedDataWhole("27");
            //newdb -203,204,205,206
            //pad.getParseList("367");
            //10.10.2011 -302,308,309,310,311,312
            //10.10.2011 -second db-302,303
            //pad.getParsedData("173");//173,174,175/*problem*/ ,176,177,178/*problem*/,179,180,181,182
          //173,174,176,177,179,180,181,182
        	
        	//284- db2 converted prcs
        	//285 - sybase stored procs
        	
        	//pads.getSummaryList("633");//229-237//173,174,176,177,179,180,181,182 //633,632
        	//pads.getCompareList("181");
        	//pads.getProjectData();
        	//pads.getParsedDataWhole("PRID174_SOURCE", "SYSBASE_TO_DB2", "C:\\DBTransplant\\app_upload\\PRID174\\SP\\Source\\Unzipped\\PRID174_1\\");
        	String paramTest=" UPDATE  #ISQuestions  SET  SectionDisplay  =  isd.SectionDisplay  FROM  ISSectionDetails  isd  WHERE  isd.ChapterNumber  =  @ChapterNumber  AND  isd.PageNumber  =  @PageNumber  AND  #ISQuestions.SectionNumber  *=  isd.SectionNumber  AND  isd.MsgTextType  =  @MsgTextType ";
    		
        	String[] testArr = pads.getExceptionedPattern(DBConnectionManager.getConnection(), "PAT_E", "PATTERN_EXCEPTION", paramTest, "1", "PAT_E", "", "");
        	for (int i = 0; i < testArr.length; i++) {
				System.out.println(":::array data:::"+testArr[i]);
			}
        	
            System.out.println("Report Generated");
            
        
        }
        
}




