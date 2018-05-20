package com.tcs.tools.business.analysis.dao;

import java.io.BufferedWriter;
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

import com.tcs.tools.business.analysis.dto.PatternIndividualKeywordScoreDTO;
import com.tcs.tools.business.analysis.dto.PatternScoreDTO;
import com.tcs.tools.business.analysis.dto.PatternSummaryDTO;
import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;


public class PatternAnalysisDAO {
	
	 private Connection lConnection= null;
	 private PreparedStatement  lPreparedStatement = null;
	 private ResultSet lResultSet = null;
	 private HashMap lScoreMap = null;
	 private List lWholeScorePlatternList = new ArrayList(); 
	 private HashMap lProcCountMap = null;
         private List lWholeKeywordScorePlatternList = new ArrayList();
         private int lIncrementBatch = 0;
	 
	/**
     * this method for getting the pattern value for the DB
     * @param pPramQueryString
     * @return
     */
    public PreparedStatement prepareInsertStatement(String lRunid,String lStatement,String lStatementNo,String lProcedureName,int score,String lStatementType,String lConcatenatedValue ,String pQueryCount,PreparedStatement pPreparedStatement)
    {
        PatternSummaryDTO lPatternSummaryDTO = new PatternSummaryDTO();
        PatternScoreDTO lPatternScoreDTO = new PatternScoreDTO();
        //PreparedStatement pPreparedStatement = null;
        lPatternSummaryDTO.setRunId(lRunid);
        lPatternSummaryDTO.setStatement(lStatement);                        	
        lPatternSummaryDTO.setStatementNo(lStatementNo);
        lPatternSummaryDTO.setProcedureName(lProcedureName);        
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
        
        return insertSummary(lPatternSummaryDTO,lConnection,pPreparedStatement);
    
    }
    public List getParsedData(String pRunId,String pDbMigrationType){
         
        String retValue="";  
        boolean lCanCaoncatenate= false;
        List lConcatenatedList = new ArrayList();
        List lScoreList = new ArrayList();
        List lSummaryList = new ArrayList();
        lConnection = DBConnectionManager.getConnection();
        lScoreMap = getScoreList(lConnection,pDbMigrationType);
        lProcCountMap = getProcRowCount(pRunId,lConnection);
        lWholeScorePlatternList = getWholePatternScoreData(lConnection,pDbMigrationType);
        lWholeKeywordScorePlatternList = getWholeKeyWordScoreData(lConnection,pDbMigrationType);
        
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
        	
        	List lMultiStringIgnoreAndInsertList = new ArrayList();
        	lMultiStringIgnoreAndInsertList.add((String)"CONVERT");
        	
        	HashMap lIgnoreConstructsHm= new HashMap();
        	lIgnoreConstructsHm.put((String)"CASE", (String)"END");
        	
        	 StringBuffer lConcatenatedValue= new StringBuffer();
            //select the data from the table
            //if required we can add % in the like operator
            //lConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //lPreparedStatement = lConnection.prepareStatement(ToolConstant.PATTERN_ANALYSYS_GET_PARSED_DATA,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            System.out.println(":::::before executing select:::::");
            lPreparedStatement = lConnection.prepareStatement(ToolConstant.PATTERN_ANALYSYS_GET_PARSED_DATA);
            
            lPreparedStatement.setString(1, pRunId);
            //System.out.println("::::pRunId::::"+pRunId+"$$$$");
            lResultSet = lPreparedStatement.executeQuery();
            System.out.println(":::::after executing select:::::");
            //lResultSet.last();
           // int lRowCount = lResultSet.getRow();            
          // System.out.println(":::::res size::::"+lRowCount);
          // lResultSet.first();
            
            lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_ALL_CONSTRUCTS);
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
            
            
            
            System.out.println("::::while loop starting");
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
                        }
                        //System.out.println("::::lStatementNoLatest:::"+lStatementNoLatest+"::::lActualPattern::::"+lActualPattern+"::::lNeedToCheckNextToken::"+lNeedToCheckNextToken+"::::lKeywordDataInserted::::"+lKeywordDataInserted);
                       
                       if(("".equals(lNeedToCheckNextToken) && (lKeywordDataInserted == false)) ||(lMultiStringIgnoreAndInsertList.contains((String)lActualPattern.toUpperCase()))){
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
                                    pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,lProcedureName,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement);
                                    score=0;
                                    lConcatenatedValue=new StringBuffer();
                                    lStatementNo = "" ;
                                    lStatement = "" ;
                                    lProcedureName = "";    
                                    lChkStatementType="";
                                }
                            }
                            if(ToolConstant.STMT_TYPE_STATEMENT.equalsIgnoreCase(lStatementType.trim()))
                            {
                            	
                            	
                            	//System.out.println("Select::::SCore;;;;;;;---->"+score);
                                if(score>0)
                                {
                                	
                                	//Chceking for multiple statemets
                                	if(lActualPattern.trim().equalsIgnoreCase("select"))
                                	{
                                		//System.out.println(":LActualPattern:->"+lActualPattern+":lPrevPattern:-> "+lPrevPattern);
                                		if(lPrevPattern.trim().equalsIgnoreCase("("))
                                		{     
                                			Pattern p = Pattern.compile("\\s+if\\b[\\s\\w]*\bexists\\s+");
                                			Matcher m= p.matcher(lConcatenatedValue.toString());                                			
                                			if(m.find())
                                			{
                                				//System.out.println("::lNumofStatements= "+lNumofStatements+"  -p1::::lConcatenatedValue;;;;;;;---->"+lConcatenatedValue);
                                				pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,lProcedureName,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement);
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
                                			//System.out.println("::Select Else::--->"+lConcatenatedValue);
                                			if((lPrevStatementType.equalsIgnoreCase("insert") && (score==(getHmScoreData("insert")+getHmScoreData("into"))))||
                                				( lPrevStatementType.equalsIgnoreCase("declare") && (score==(getHmScoreData("declare")+getHmScoreData("cursor")))))
                                			{
                                				lNumofStatements++;
                                				
                                			}
                                			else if(!lPrevStatementType.trim().equalsIgnoreCase("declare"))
                                			{
                                				//System.out.println("::lNumofStatements= "+lNumofStatements+"  -1::::lConcatenatedValue;;;;;;;---->"+lConcatenatedValue);
                                    			//System.out.println("1::lRunid:"+lRunid+"::lStatementNo:"+lStatementNo+"::lStatement:"+lStatement+"::lProcedureName::"+lProcedureName+" :Score::"+score+"::lChkStatementType"+lChkStatementType);                                				
                                				pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,lProcedureName,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement);
                                                score=0; 
                                                lNumofStatements=1;
                                                lConcatenatedValue= new StringBuffer();                                                
                                			}
                                			else
                                			{
                                				score=0;
                                				lNumofStatements=1;
                                				lConcatenatedValue= new StringBuffer();
                                			}
                                			                              			
                                		}
                                		//System.out.println("::::: lActualPattern ::->"+lActualPattern);
                                		
                                	}
                                	else if(!lPrevStatementType.trim().equalsIgnoreCase("declare"))
                                	{
                                		//System.out.println("::lNumofStatements= "+lNumofStatements+" -2::::lConcatenatedValue;;;;;;;---->"+lConcatenatedValue);
                                		//System.out.println("2::lRunid:"+lRunid+"::lStatementNo:"+lStatementNo+"::lStatement:"+lStatement+"::lProcedureName::"+lProcedureName+" :Score::"+score+"::lChkStatementType"+lChkStatementType);                         		
                                		pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,lProcedureName,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement);
                                		 score=0;
                                		 lNumofStatements=1;
                                		lConcatenatedValue= new StringBuffer();
                                       
                                	}
                                	else
                        			{
                        				score=0;
                        				lNumofStatements=1;
                        				lConcatenatedValue= new StringBuffer();
                        			}                               	
                                	
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
                            	//System.out.println("2: "+lActualPattern+"  Score"+score);
                            	///////
                            	if(lIgnoreConstructsList.size()>0){
                            		if(lConstructsList.contains(lActualPattern.trim().toUpperCase())){
                            			if((ToolsUtil.replaceNull((String)lIgnoreConstructsHm.get(lIgnoreConstructsList.get(lIgnoreConstructsList.size()-1)))).equalsIgnoreCase(lActualPattern.trim())){
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
                if((score>0 )&& (!lPrevStatementType.trim().equalsIgnoreCase("declare")))
                {
                	
                	//System.out.println("::lNumofStatements= "+lNumofStatements+" -::::3ConcatenatedValue;;;;;;;---->"+lConcatenatedValue);                	
        			pPreparedStatement = prepareInsertStatement(lRunid,lStatement,lStatementNo,lProcedureName,score,lChkStatementType,lConcatenatedValue.toString(),lNumofStatements+"",pPreparedStatement);
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
            DBConnectionManager.closeConnection(lConnection);           
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
 						lhmPatternData.put(lStatementCont, lResultSet
 								.getString("STATEMENT_SCORE"));
 						System.out.println("STATEMENT SCORE:::"+lhmPatternData);
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
            lPreparedStatement.setString(1, pDbMigrationType);
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
 			
 		//System.out.println("::::dto pat run id:::"+pPatternSummaryDTO.getRunId()+"::::i val:::::pattern id::"+pPatternSummaryDTO.getPatternId()+"::::pattern format::"+pPatternSummaryDTO.getPatternFormat());
 			//System.out.println("dto ru id::::"+pPatternSummaryDTO.getRunId());;
 			//System.out.println("dto key word::::"+pPatternSummaryDTO.getKeyWord());;
 			
 			// prepare the query
 			//pPreparedStatement = pConnection.prepareStatement(ToolConstant.PATTERN_ANALYSYS_INSERT_SUMMARY);
 			pPreparedStatement.setString(1, pPatternSummaryDTO.getRunId());
 			pPreparedStatement.setString(2, pPatternSummaryDTO.getProcedureName());
 			pPreparedStatement.setString(3, pPatternSummaryDTO.getStatement());
 			pPreparedStatement.setString(4, pPatternSummaryDTO.getStatementNo());
 			pPreparedStatement.setString(5, pPatternSummaryDTO.getKeyWord());
 			pPreparedStatement.setString(6, pPatternSummaryDTO.getScore() );
 			pPreparedStatement.setString(7, pPatternSummaryDTO.getPatternId() );
 			//pPreparedStatement.setString(8, pPatternSummaryDTO.getPatternDesc() );
 			pPreparedStatement.setString(8, getExceptionedPattern(pPatternSummaryDTO.getPatternDesc(),pPatternSummaryDTO.getFormedStatement(),pPatternSummaryDTO.getQueryCount()) );
 			
 			//System.out.println(" Prepared st:  ---> "+pPatternSummaryDTO.getFormedStatement());
 			pPreparedStatement.setString(9, pPatternSummaryDTO.getFormedStatement() ); 			 			
 			pPreparedStatement.setString(10, ToolConstant.CREATED_BY);
 			pPreparedStatement.setTimestamp(11, new Timestamp(System
 					.currentTimeMillis()));
 			pPreparedStatement.setString(12, pPatternSummaryDTO.getQueryCount());
 			//System.out.println("::::getting inserted::::"+pPatternSummaryDTO.getPatternDesc()+"-"+pPatternSummaryDTO.getKeyWord());
 			

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
    public String getExceptionedPattern(String pParam,String lFormedStatement,String pQueryCount){
    	String pPatternReturn="";
    	int score=0;
    	try{
    		if(pParam.trim().equalsIgnoreCase("PATTERN_EXCEPTION"))
			 {
				 if(lFormedStatement.toUpperCase().contains("SELECT")){
					 pPatternReturn+="SELECT  ";
				 }
				 if(lFormedStatement.toUpperCase().contains("UPDATE")){
					 pPatternReturn+="UPDATE  ";
				 }
				if(lFormedStatement.toUpperCase().contains("INSERT")){	
					
					pPatternReturn+="INSERT  ";
				 }
				if(lFormedStatement.toUpperCase().contains("DELETE")){
					pPatternReturn+="DELETE  ";
				 }
				if(lFormedStatement.toUpperCase().contains("CURSOR")){
					pPatternReturn+="CURSOR ";
				 }
				if(Integer.parseInt(pQueryCount)>=3)
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
 	public void getSummaryList(String pRunId) {
 		
 		
 		try {
 			
 			lConnection = DBConnectionManager.getConnection();
                        
                        BufferedWriter writer = new BufferedWriter(new FileWriter("c:/arun/SumaryResults_NEW_DB_"+pRunId+".txt"));

 			// replacing the special characters in the query string

 			// select the data from the table
 			// if required we can add % in the like operator
 			
 			lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_PATTERN_DATA);
                        lPreparedStatement.setString(1,pRunId);
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
                        String lSeperator=";";
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
 					 if(lPatternId.trim().equalsIgnoreCase("PAT_E"))
 					 {
 						 if(lFormedStatement.toUpperCase().contains("SELECT")){
 							lExceptionPatternDesc+="SELECT  ";
 						 }
 						 if(lFormedStatement.toUpperCase().contains("UPDATE")){
							lExceptionPatternDesc+="UPDATE  ";
						 }
 						if(lFormedStatement.toUpperCase().contains("INSERT")){
							lExceptionPatternDesc+="INSERT  ";
						 }
 						if(lFormedStatement.toUpperCase().contains("DELETE")){
							lExceptionPatternDesc+="DELETE  ";
						 }
 						if(lFormedStatement.toUpperCase().contains("CURSOR")){
							lExceptionPatternDesc+="CURSOR ";
						 }
 						if(Integer.parseInt(lQueryCount)>=3)
 						lPatternDesc="Complex Query "+lExceptionPatternDesc;
 					 }
 					
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
                                    sbGetData.append(lPatternDesc);                                   
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
    
        public static void main(String [] args)
        {
            PatternAnalysisDAO pad=new PatternAnalysisDAO();
            //new 318-320
           //pad.getSummaryList("173,174,176,177,179,180,181,182");//229-237
            pad.getParsedData("27","SYSBASE_TO_DB2");
            //newdb -203,204,205,206
            //pad.getParseList("367");
            //10.10.2011 -302,308,309,310,311,312
            //10.10.2011 -second db-302,303
            //pad.getParsedData("173");//173,174,175/*problem*/ ,176,177,178/*problem*/,179,180,181,182
          //173,174,176,177,179,180,181,182
            System.out.println("Report Generated");
            
        
        }
    
}
