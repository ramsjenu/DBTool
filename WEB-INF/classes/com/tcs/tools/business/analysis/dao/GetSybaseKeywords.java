/*
 * GetSybaseKeywords.java
 *
 * Created on October 3, 2011, 4:24 PM
 * To get Sybase keywords which r not there in DB2.
 */

package com.tcs.tools.business.analysis.dao;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.web.util.DBConnectionManager;

/**
 *
 * @author  477780
 */
public class GetSybaseKeywords {
    
     ArrayList lstKeywords;
    /** Creates a new instance of GetSybaseKeywords */
     public GetSybaseKeywords() {
        lstKeywords = new ArrayList();

    }
    
    public void getAllKeywords()
    {
        PreparedStatement  lPreparedStatement;
        ResultSet lResultSet = null;
        Connection lConnection=null;
        try {
            
            lConnection = DBConnectionManager.getConnection();  
            lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_DB2_NOT_SUPPORTED_KEYWORDS);                        
            lResultSet = lPreparedStatement.executeQuery(); 
            //System.out.println(lPreparedStatement.toString());
            while (lResultSet.next()) {
                //System.out.println(lResultSet.getString("KEY_WORD"));
                lstKeywords.add(lResultSet.getString("KEY_WORD"));
            }
        } catch (SQLException se) {
            se.printStackTrace();
            //return null;
        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        } finally {
            // close the connection and the result set
            //DBConnectionManager.closeConnection(lPreparedStatement,lResultSet);
            DBConnectionManager.closeConnection(lConnection);
        }
        
        
        
    }
    public void getParseList(String pRunId) {
        
 		PreparedStatement  lPreparedStatement;
                ResultSet lResultSet = null;
 		Connection lConnection=null;
                PreparedStatement  pPreparedStatement=null;
 		try {
                    
                     
 			lConnection = DBConnectionManager.getConnection();  
                        
 			lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_PARSE_RESULTS);                        
                        lPreparedStatement.setString(1,pRunId);
                        lResultSet = lPreparedStatement.executeQuery();                         
                        //System.out.println(lPreparedStatement.toString());
                        while (lResultSet.next()) {
                                    
                                     String chkKeyword=lResultSet.getString("ACTUAL_PATTERN");
                                     if(lstKeywords.contains(chkKeyword.trim())) {
                                       
                                       pPreparedStatement = lConnection.prepareStatement(ToolConstant.INSERT_REPORT_KEYWORDS);
                                       System.out.println(chkKeyword+"::::: " +ToolConstant.INSERT_REPORT_KEYWORDS);
                                       pPreparedStatement.setInt(1,Integer.parseInt(pRunId));
                                       pPreparedStatement.setString(2,lResultSet.getString("PROCEDURE_NAME"));
                                       pPreparedStatement.setString(3,chkKeyword);
                                       //pPreparedStatement.setString(2,"asdasd");
                                       //pPreparedStatement.setString(3,"3343");
                                       pPreparedStatement.setString(4,ToolConstant.REPORT_TYPE);
                                       pPreparedStatement.setString(5,ToolConstant.CREATED_BY );
                                       pPreparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                                       // Execute Query
                                        //
                                       
                                       pPreparedStatement.executeUpdate();
                                       
                                       
                                       
                                     }                                    
                                     
 				
 			}
                        
 		} catch (SQLException se) {
 			se.printStackTrace();
 			//return null;
 		} catch (Exception e) {
 			e.printStackTrace();
 			//return null;
 		} finally {
 			// close the connection and the result set
 			//DBConnectionManager.closeConnection(lPreparedStatement,lResultSet);
 			DBConnectionManager.closeConnection(lConnection);
 		}
    }
    public void getReportKeywords()
    {
        System.out.println("::::::::::: Report Keywords::::::::::::::");
        PreparedStatement  lPreparedStatement;
        ResultSet lResultSet = null;        
        Connection lConnection=null;
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("c:/arun/Sybase_Keywords_Not_In_DB2.xlsx"));            
            lConnection = DBConnectionManager.getConnection();  
            lPreparedStatement = lConnection.prepareStatement(ToolConstant.GET_REPORT_KEYWORDS);                                    
            lResultSet = lPreparedStatement.executeQuery(); 
            StringBuffer sbGetData= new StringBuffer();
            sbGetData.append("S.No");
            sbGetData.append("\t");
            sbGetData.append("Procedure Name");
            sbGetData.append("\t");
            sbGetData.append("Keyword");
            sbGetData.append("\t");
            sbGetData.append("Report Type");
            sbGetData.append("\t");
            sbGetData.append("CREATED DATE");
            writer.write(sbGetData.toString());
            writer.newLine();
            int sno=1;
            //System.out.println(lPreparedStatement.toString());
            while (lResultSet.next()) {
                sbGetData= new StringBuffer();
                sbGetData.append(sno);
                sbGetData.append("\t");
                sbGetData.append(lResultSet.getString("PROCEDURE_NAME"));
                sbGetData.append("\t");
                sbGetData.append(lResultSet.getString("KEY_WORD"));
                sbGetData.append("\t");
                sbGetData.append(lResultSet.getString("REPORT_TYPE"));
                sbGetData.append("\t");
                sbGetData.append(lResultSet.getString("CREATED_DATE"));
                
                //System.out.println(lResultSet.getString("KEY_WORD"));                 
                 sno++;
                 writer.write(sbGetData.toString());
                 writer.newLine();
            } 
            //lPreparedStatement = lConnection.prepareStatement("DELETE * FROM REPORT_KEYWORDS_USAGE_TABLE"); 
            //lPreparedStatement.executeUpdate();
            writer.close();
        } catch (SQLException se) {
            se.printStackTrace();
            //return null;
        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        } finally {
            // close the connection and the result set
            //DBConnectionManager.closeConnection(lPreparedStatement,lResultSet);
            DBConnectionManager.closeConnection(lConnection);
        }
        
        
        
    }
   
    public static void main(String [] args)
    {
        GetSybaseKeywords objSybaseKey = new GetSybaseKeywords(); 
         //objSybaseKey.getAllKeywords();
         //objSybaseKey.getParseList("224");
        objSybaseKey.getReportKeywords();
        
       
    
    }
}
