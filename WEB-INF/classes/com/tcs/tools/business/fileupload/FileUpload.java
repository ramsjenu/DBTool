/*
 * FileUpload.java
 *
 * Created on September 30, 2011, 12:37 PM
 */

package com.tcs.tools.business.fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.business.fileupload.dao.FileUploadDAO;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;
/**
 * 
 * @author 477780
 */
public class FileUpload {
	boolean lDoubleQuoteFound=false;
	boolean lSingleQuoteFound=false;
	/** Creates a new instance of FileUpload */
	public FileUpload() {
	}

	private int lWordOrderNo = 1;
	private HashMap lPatternDataHm = null;
	
	private String pCreatedBy ="FileUplaodUser";
	int lQueryCount=0;
	String lCurState="";
	String lStausMsg="";
	
	//private PatternAnalysisSingleDAO pads = new PatternAnalysisSingleDAO("prepareCall");
	

	/**
	 * @param args
	 *            the command line arguments
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		System.out.println("test main::::::::::"
				+ new Timestamp(System.currentTimeMillis()));
		// Connection con=DBConnectionManager.getConnection();
		FileUpload lFileUpload = new FileUpload();
		String pFolderPath ="C:\\arun\\documents\\project\\DCMS\\checking procs input\\338 procs\\sample\\TEST";	
		lFileUpload.getFiles(pFolderPath,"SYSBASE_TO_DB2","prid_test_01");
		System.out.println("test main::::done::::::"
				+ new Timestamp(System.currentTimeMillis()));
		FileUploadDAO lFileUploadDAO = new FileUploadDAO();
		//lFileUpload.lPatternDataHm = lFileUploadDAO.getPatternMatchDataList("SYBASE_TO_SQL"); 
		//System.out.println(":::method called::::"+ lFileUpload.getHmPatternData("SELECT"));
		
		

	}

	public String getFiles(String pFolderPath,String pDbMigrationType,String pProjectRunId) {
		
		String lRunningSeqNo = null;
		try {
			// getting the master sequence
			FileUploadDAO lFileUploadDAO = new FileUploadDAO();
				//running sequence commented
			 //lRunningSeqNo = lFileUploadDAO.getRunSeq();
			lRunningSeqNo = pProjectRunId;
			lPatternDataHm = lFileUploadDAO.getPatternMatchDataList(pDbMigrationType); 
                        Connection lConnection = DBConnectionManager.getConnection();
			System.out.println("pFolderPath--->"+pFolderPath);
                        System.out.println("lRunningSeqNo--->"+lRunningSeqNo);
			pFolderPath = pFolderPath.replaceAll("\\\\", "/");
			//for hard coding the folder path
			//pFolderPath ="D:/STUDY/WORKSPACE_TOOL/sample_io/small/good";			
			 File dirpath = new File(pFolderPath);
			 getIndividualFile( dirpath, pFolderPath, lRunningSeqNo,lConnection);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lRunningSeqNo;
	}

	public void getIndividualFile(File dirpath,String pFolderPath,String lRunningSeqNo,Connection lConnection){
		 try{
			System.out.println("pree " +dirpath);
			
				File[] filesAndDirs = dirpath.listFiles();
				System.out.println("files pree " +dirpath.listFiles());
				System.out
						.println(":::::::No: of Files:::::" + filesAndDirs.length);
				/*
				 * String fileName="C:/Documents and Settings/477780/My
				 * Documents/Tool Data/inputs/ABS_DetailByEE.sql";
				 */

				for (int cnt = 0; cnt < filesAndDirs.length; cnt++) {
					lWordOrderNo = 1;
					File file = filesAndDirs[cnt];
					
					
					
					System.out.println(":::::PRIYANKA File name::::::" + file.getName());
					// String[] lines= getLines(file);
					// System.out.println(lines.length);
					if ( file.isFile() ) {
						//Update Status to Front End - Start
						 lCurState="File Upload";
						 lStausMsg="File Name::->"+file.getName();			
						 ToolsUtil.prepareInsertStatusMsg( ToolsUtil.getProjectId(lRunningSeqNo), ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
						//Update Status to Front End - End
						
					parseFile(getLines(file)/* reading each file contenet */, file
							.getName(),file.getParent(), lRunningSeqNo,lConnection);
					
					//pads.getParsedData(lRunningSeqNo,file.getName());
					}else{
						//Update Status to Front End - Start
						 lCurState="File Upload";
						 lStausMsg="Processing Folder Name::->"+file.getName();			
						 ToolsUtil.prepareInsertStatusMsg( ToolsUtil.getProjectId(lRunningSeqNo), ToolConstant.CREATED_BY, lCurState,lStausMsg, "Info",new Timestamp(System.currentTimeMillis()),lConnection);
						//Update Status to Front End - End
						
						 getIndividualFile(file , pFolderPath, lRunningSeqNo,lConnection);
					}
				} 
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	 
	public String[] getLines(File file) {
		String[] lines = null;
		try {
			
			InputStream is = new FileInputStream(file);
			StringBuffer buffer = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = is.read(b)) != -1;) {
				
				buffer.append(new String(b, 0, n));
			}
			
			

			 
			
			
			String str = buffer.toString();
			
			//insert( to be replaced with insert_(
			str = str.replaceAll("(?i)\\bINSERT\\b[\\s\\r\\n]*\\(", "INSERT_TCSTOOL (");
			str = str.replaceAll("(?i)\\bend\\b[\\s\\r\\n]+\\bif\\b[\\s\\r\\n]*;", " END_IF_TCSTOOL ; ");
			str = str.replaceAll("(?i)\\bend\\b[\\s\\r\\n]+\\bif\\b[\\s\\r\\n]*#", " END_IF_TCSTOOL # ");
			str = str.replaceAll("(?i)\\bEND\\b[\\s\\r\\n]+\\bWHILE\\b[\\s\\r\\n]*;", " END_WHILE_TCSTOOL ; ");
			str = str.replaceAll("(?i)\\bGRANT\\b[\\s\\r\\n]+\\bEXECUTE\\b[\\s\\r\\n]+", " GRANT_EXECUTE ");
			//str = str.replaceAll("(?i)\\bELSE\\b[\\s\\r\\n]+\\bIF\\b[\\s\\r\\n]+", " ELSE_IF ");
			str = str.replaceAll("(?i)\\bELSEIF\\b[\\s\\r\\n]+", " ELSE IF ");
			
			str = str.replaceAll("(?i)\\bSELEC\\s*\\bT", " SELECT ");
			
			
			// replacing all the brackets with some spaces - start
			str = str.replaceAll("\\(", " \\( ");
			str = str.replaceAll("\\)", " \\) ");
			str = str.replaceAll("/\\*", " /\\* ");
			str = str.replaceAll("\\*/", " \\*/ ");
			//str = str.replaceAll("\"", " \" ");
			str = str.replaceAll(";", " ; ");
			
			
			
			
			
			//revert spaced = for operators
			str = str.replaceAll("=", " = ");
			str = str.replaceAll("\\+", " + ");
			str = str.replaceAll("<[\\s\\r\\n]+=", " <= ");
			str = str.replaceAll(">[\\s\\r\\n]+=", " >= ");
			str = str.replaceAll("\\![\\s\\r\\n]+=", " != ");
			str = str.replaceAll("\\+[\\s\\r\\n]+=", " += ");
			str = str.replaceAll("\\+[\\s\\r\\n]+\\+", " ++ ");
			str = str.replaceAll("-[\\s\\r\\n]+=", " -= ");
			str = str.replaceAll("=[\\s\\r\\n]+=", " == ");
			str = str.replaceAll("\\*[\\s\\r\\n]+=", " *= ");
			//str = str.replaceAll("\\-\\w", " - ");
			
			
			
			//str = str.replaceAll("/*", " /* ");			
			//str = str.replaceAll("*/", " \\*/ ");
			
                       //str = str.replaceAll("/\\*(?:.|[\\n\\r])*?\\*/","");
			
                        
						//for replacing multi line comment
                        //str= StringUtils.replace(StringUtils.trim(str), "/\\*(?:.|[\\n\\r])*?\\*/", "");
			
                        

                        //for adding space to comma
                        str = str.replaceAll(",", " , ");
                        //for replcing text with other text
                        str = str.replaceAll("(?i)\\bFETCH\\b[\\s\\r\\n]+\\bFIRST\\b[\\s\\r\\n]+[\\d]+[\\s\\r\\n]+\\bROWS\\b[\\s\\r\\n]+\\bONLY", " FETCH_FIRST_1_ROWS_ONLY ");
                        //commit  preserve  rows  not  logged
                        str= str.replaceAll("(?i)\\bon\\b[\\s\\r\\n]+\\bcommit\\b[\\s\\r\\n]+\\bpreserve\\b[\\s\\r\\n]+\\brows\\b[\\s\\r\\n]+\\bnot\\b[\\s\\r\\n]+\\blogged\\b", " on_commit_preserve_rows_not_logged ");
                        str = str.replaceAll("(?i)\\bWITH\\b[\\s\\r\\n]+\\bRETURN\\b", " WITH_RETURN ");
                        //ON ROLLBACK RETAIN CURSORS
                        str = str.replaceAll("(?i)\\bON\\b[\\s\\r\\n]+\\bROLLBACK\\b[\\s\\r\\n]+\\bRETAIN\\b[\\s\\r\\n]+\\bCURSORS\\b", " ON_ROLLBACK_RETAIN_CURSORS ");
                         
                        //CALL DBMS_OUTPUT.
                        str = str.replaceAll("(?i)\\bCALL\\b[\\s\\r\\n]+\\bDBMS_OUTPUT\\.", " CALL_DBMS_OUTPUT_.");
                           
                        //System.out.println("after replace::::::::::"+ new Timestamp(System.currentTimeMillis()));

			// replacing all the brackets with some spaces - end

			// System.out.println(":::full file content:::::"+str);
			Pattern p = Pattern.compile("\n");                        
			lines = p.split(str.trim());
			
			/*System.out.println("*************************************************************");
			System.out.println("LineS lENGTH::"+lines.length);
			System.out.println("*************************************************************");*/
            

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}

	/**
	 * @param lines
	 * @param fileName
	 * @param pRunningSeqNo
	 */
	/**
	 * @param lines
	 * @param fileName
	 * @param pRunningSeqNo
	 */
	public List removeMultiLineComments(String[] lines){
		List lRetLines=new ArrayList();
		String[] wordsOfLine = null;
        String lTmpLine="";
        boolean lisComment=false;
        for (int i = 0; i < lines.length; i++) {
        	wordsOfLine = lines[i].trim().split("\\s+");
        	lTmpLine="";
        	for (int j = 0; j < wordsOfLine.length; j++) {
            	
            	if("".equals(wordsOfLine[j].trim())){
            		continue; 
    			 	}
    				if("/*".equals(wordsOfLine[j].trim()))
    				{
    					 lisComment=true;
    				}
    				 else if("*/".equals(wordsOfLine[j].trim()))
    				 {
    					 lisComment=false;
    					 continue;
    				 }						 
    				 if(lisComment==true)
    				 {
    					 continue;
    				 }
    				lTmpLine=lTmpLine+" "+wordsOfLine[j];
            }
        	lRetLines.add(lTmpLine);       	
			
		}
        System.out.println(lines.length+" - List Size: "+lRetLines.size());
        
		return lRetLines;
	}
	public void parseFile(String[] lines, String fileName,String pFilePath, String pRunningSeqNo,Connection lConnection) {
		try {
			FileUploadDAO lFileUploadDAO = new FileUploadDAO();
			 //StringBuffer buffer = new StringBuffer();

			// for writing into text file
			// BufferedWriter writer = new BufferedWriter(new FileWriter("c:/arun/filename.txt"));
			//StringBuffer sbInsert = new StringBuffer();
		//	String lSQL = "INSERT INTO PARSE_RESULTS_TABLE(RUN_ID,PROCEDURE_NAME,STATEMENT,STATEMENT_NO,ACTUAL_PATTERN,STATEMENT_TYPE,CREATED_BY,CREATED_DATE,ORDER_NO) VALUES (?,?,?,?,?,?,?,?,?)";
 			String lSQL=ToolConstant.PARSE_RESULT_TABLE_INSERT;
			lDoubleQuoteFound=false;
			 lSingleQuoteFound=false;
			 
 			PreparedStatement lPreparedStatement = null;
 			lConnection.setAutoCommit(false);
 			boolean lisComment=false;
 			lPreparedStatement = lConnection.prepareStatement(lSQL);
 			List lLinesOfFile=removeMultiLineComments(lines); 			
 			if(lLinesOfFile==null || lLinesOfFile.size()==0 ){
 				return;
 			}
 			String lLine="";
 			
 			int counter=0;
 			ArrayList<Integer> counter2=new ArrayList<Integer>();
 			ArrayList<Integer> counter3=new ArrayList<Integer>();
 			ArrayList<String> toRemove = new ArrayList<String>();
 			
 			for (int ind = 0; ind < lLinesOfFile.size(); ind++) {
 				lLine=(String)lLinesOfFile.get(ind);
 				Pattern pc1=Pattern.compile("(?i)\\bCREATE\\b[\\s\\r\\n]+\\b(PROCEDURE|PROC)");
 				Matcher mc1=pc1.matcher(lLine);
 				Pattern pc2=Pattern.compile("(?i)\\bCREATE\\b[\\s\\r\\n]+\\bOR\\b[\\s\\r\\n]+\\bREPLACE\\b[\\s\\r\\n]+\\b(PROCEDURE|PROC)");
 				Matcher mc2=pc2.matcher(lLine);
 			//	if(lLine.contains("CREATE PROCEDURE")||lLine.contains("CREATE PROC")){
 						if(mc1.find()|mc2.find()){
					counter=ind;
 				}
 			
 			}
 			/*for (int ind = 0; ind < lLinesOfFile.size(); ind++) {
 				lLine=(String)lLinesOfFile.get(ind);
 				Pattern pc=Pattern.compile("(?i)\\bCREATE\\b[\\s\\r\\n]+\\bOR+\\bREPLACE+\\b(PROCEDURE|PROC)");
 				Matcher mc=pc.matcher(lLine);
 			//	if(lLine.contains("CREATE PROCEDURE")||lLine.contains("CREATE PROC")){
 						if(mc.find()){
					counter=ind;
 				}
 			
 			}*/
 			//System.out.println("LINESOFFILE:::::: "+lLinesOfFile);
 	/*
 			for (int ind = counter; ind < lLinesOfFile.size(); ind++) {
 				lLine=(String)lLinesOfFile.get(ind);
 				if(lLine.contains("-- START MANUAL ADDITION")){
 						counter2.add(ind);
					//System.out.println("counter2+" +counter2);
				}
 				if(lLine.contains("-- END MANUAL ADDITION")){
 						counter3.add(ind);
					//System.out.println("counter3+" +counter3);
				}
 			}
 			for(int i=0;i<counter2.size();i++)
 			{
 				for(int cn=counter2.get(i);cn<=counter3.get(i);cn++)
 					{
 						lLine=(String)lLinesOfFile.get(cn);
 						toRemove.add(lLine);
 					}
 			}
 		
 			//System.out.println("LINESOFFILE:::::: "+toRemove);
 			for(String c:toRemove )
 				lLinesOfFile.remove(c);
 		//	System.out.println("LINESOFFILE:::::: "+lLinesOfFile);
 			
 			*/
 			
 			
 			//List lAfterManualCodeRemoval=removeManuallyAddedBlocks(lines,fileName,pFilePath,pRunningSeqNo,lConnection);
 			removeManuallyAddedBlocks(lLinesOfFile,fileName,pFilePath,pRunningSeqNo,lConnection);
	
 			
 			for (int ind = counter; ind < lLinesOfFile.size(); ind++) {
			lLine=(String)lLinesOfFile.get(ind);
		
			lLine = checkLine(lLine.trim());
			//System.out.println("LINE::::"+lLine);
 				String[] words = lLine.split("\\s+");
 				
 				for (int i = 0; i < words.length; i++) 
 				{
 					if(words[i].contains("-"))
 					{
 						words[i]=words[i].replaceAll("-", "-\n");
 					
 						
 					}
 					
 				}
				 //System.out.println("WORDSLENGTH"+words.length);
				// System.out.println(":::LINE IN EACH FILE"+lLine);
				
				for (int i = 0; i < words.length; i++) {
					if(!"".equals(words[i].trim())){
						lPreparedStatement.setString(1, pRunningSeqNo);
						lPreparedStatement.setString(2, fileName.trim());
						lPreparedStatement.setString(3, ToolsUtil.removeToolKeywordsNoTrim(lLine.trim()));
						lPreparedStatement.setString(4, String.valueOf(ind + 1));
						lPreparedStatement.setString(5, ToolsUtil.removeToolKeywordsNoTrim(words[i].trim()));
						lPreparedStatement.setString(6, getHmPatternData(words[i].trim()));
						lPreparedStatement.setString(7, ToolConstant.CREATED_BY);
						lPreparedStatement.setTimestamp(8, new Timestamp(System
								.currentTimeMillis()));
						lPreparedStatement.setInt(9, lWordOrderNo++);
						// Execute Query
						lPreparedStatement.addBatch();
						//System.out.println("QUERY::::"+lSQL);
						lQueryCount++;
						if(lQueryCount==1000){
							lPreparedStatement.executeBatch();
							lQueryCount=0;
						}
					}
					                                 
					
				}
 				
 				
			}
 		
			/*for (int cnt = 0; cnt < lines.length; cnt++) {
				
                                //call a method to check if the lines are ok and then process further or go to next line
                           
				if(lines[cnt] == null ||  "".equals(lines[cnt])){
                    continue;
                }
                            
                            
                            
                            
                            
                            
                            lines[cnt] = checkLine(lines[cnt].trim());
                            

				
				Pattern p = Pattern.compile("\\s");
				String[] words = p.split(lines[cnt].trim());
				// System.out.println(words.length);
				 System.out.println(":::LINE IN EACH FILE"+lines[cnt]);
				
				for (int i = 0; i < words.length; i++) {
					
					
					 if("".equals(words[i].trim())){
						continue; 
					 }
					// if("/*".equals(words[i].trim()))
					 {
						 lisComment=true;
					 }
					// else if(""put comment symbol here .equals(words[i].trim()))
					 {
						 lisComment=false;
						 continue;
					 }						 
					 if(lisComment==true)
					 {
						 continue;
					 }
					 words[i] = checkDouleQuotes(words[i]);
					 words[i] = checkSingleQuotes(words[i]);
					 
                         			
                         			lPreparedStatement.setString(1, pRunningSeqNo);
                         			lPreparedStatement.setString(2, fileName.trim());
                         			lPreparedStatement.setString(3, lines[cnt].trim());
                         			lPreparedStatement.setString(4, String.valueOf(cnt + 1));
                         			lPreparedStatement.setString(5, words[i].trim());
                         			lPreparedStatement.setString(6, getHmPatternData(words[i].trim()));
                         			lPreparedStatement.setString(7, ToolConstant.CREATED_BY);
                         			lPreparedStatement.setTimestamp(8, new Timestamp(System
                         					.currentTimeMillis()));
                         			lPreparedStatement.setInt(9, lWordOrderNo++);

                         			// Execute Query
                         			lPreparedStatement.addBatch();
                                    
					
				}
				
			}*/
			//System.out.println(":::sbInsert:::"+sbInsert);
			//System.out.println("::::batch start:::;");
			lPreparedStatement.executeBatch();
			
			lFileUploadDAO.insertSPLineCountDetails(lConnection,pRunningSeqNo,fileName.trim(),pFilePath,lines.length+"",pCreatedBy);
			lConnection.commit();
			lConnection.setAutoCommit(true);
			//System.out.println("::::batch end:::;");
			// for writing into text file
			 //writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void removeManuallyAddedBlocks(List list,String fileName,String pFilePath, String pRunningSeqNo,Connection lConnection){
		
		try{
		String line="";
		String c="";
		String lSQL=ToolConstant.MANUALLY_ADDED_BLOCKS_INSERT;
		//String lSQL = "INSERT INTO MANUALLY_ADDED_CODE_BLOCKS_TABLE(RUN_ID,PROJECT_ID,PROCEDURE_NAME,STATEMENT,STATEMENT_NO,STATEMENT_TYPE,ORDER_NO,CREATED_BY,CREATED_DATE) VALUES (?,?,?,?,?,?,?,?,?)";
		PreparedStatement lPreparedStatement = null;
		lConnection.setAutoCommit(false);
		
		lPreparedStatement = lConnection.prepareStatement(lSQL);
		
		
			ArrayList<Integer> startcommentcounter=new ArrayList<Integer>();
			ArrayList<Integer> endcommentcounter=new ArrayList<Integer>();
			HashMap<Integer,String> toRemove = new HashMap<Integer,String>();
			for (int ind = 0; ind < list.size(); ind++) {
				line=(String)list.get(ind);
				//if(line.contains("-- START MANUAL ADDITION")){
				Pattern p1=Pattern.compile("(?i)\\--\\s*\\bSTART\\b[\\s\\r\\n]+\\bMANUAL\\b[\\s\\r\\n]+\\bADDITION");
				Matcher m1=p1.matcher(line);
			if(m1.find()){
					startcommentcounter.add(ind);
				//System.out.println("startcommentcounter+" +counter2);
			}
			Pattern p2=Pattern.compile("(?i)\\--\\s*\\bEND\\b[\\s\\r\\n]+\\bMANUAL\\b[\\s\\r\\n]+\\bADDITION");
			Matcher m2=p2.matcher(line);
				//if(line.contains("-- END MANUAL ADDITION")){
			if(m2.find()){
					endcommentcounter.add(ind);
				//System.out.println("endcommentcounter+" +counter3);
			}
			}
			for(int i=0;i<startcommentcounter.size();i++)
			{
				for(int cn=startcommentcounter.get(i);cn<=endcommentcounter.get(i);cn++)
					{
						line=(String)list.get(cn);
						//toRemove.add(line);
						toRemove.put(cn, line);
						//System.out.println("LINESOFFILE:::::: "+toRemove);
					}
			}
		
			//System.out.println("LINESOFFILE:::::: "+toRemove);
			//for (int ind = 0; ind < toRemove.size(); ind++) {
			
			for (int ind : toRemove.keySet()) {  
				
				c=(String)toRemove.get(ind);
				list.set(ind, " ");
				//list.remove(c);//removing manually added blocks from the list
				//c = checkLine(c.trim());
			
				//inserting manually added blocks into separate table
 				
				lPreparedStatement.setString(1, pRunningSeqNo);
				lPreparedStatement.setString(2, pRunningSeqNo.substring(0, 7));
				lPreparedStatement.setString(3, fileName.trim());
				lPreparedStatement.setString(4, c.trim());
				lPreparedStatement.setString(5, String.valueOf(ind));
				lPreparedStatement.setString(6, getHmPatternData(c.trim()));
				lPreparedStatement.setInt(7, lWordOrderNo++);
				lPreparedStatement.setString(8, ToolConstant.CREATED_BY);
				lPreparedStatement.setTimestamp(9, new Timestamp(System
						.currentTimeMillis()));
				lPreparedStatement.addBatch();
				lQueryCount++;
				
				lPreparedStatement.executeBatch();
					
				  
				//	//JOptionPane.showMessageDialog(null,"batch query performed");
					//lQueryCount=0;
				//}
				}	
				
			
			//System.out.println("LINESOFFILE:::::: "+lLinesOfFile);
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
        /*method for removing the commented lines from the code*/
        public String checkLine(String pLine){
            pLine = pLine.trim(); 
            //System.out.println("Step1----->"+pLine);
                if (pLine.startsWith("--")) {
                // Do nothing
                return "";        
                } else if (pLine.length() < 1
                        || pLine.startsWith("//")) {
                    // Do nothing
                return "";
                } else if (pLine.length() < 1
                        || pLine.startsWith("--")) {
                	
                    // Do nothing
                	
                return "";
                }/* else if (pLine.length() < 1
                        ||(pLine.toUpperCase()).startsWith("PRINT")) {
                    // Do nothing
                return "";
                }*/
            //System.out.println("Step2----->"+pLine);
            String arr[] = pLine.split("--");
            //System.out.println("Step3---->"+pLine);
            /*System.out.println(":: array length:::"+arr.length);
            for (int i = 0; i < arr.length; i++) {
            System.out.println("::::array value:::for i val::::"+i+"--->"+arr[i]);
            }*/
            if(arr != null && arr.length >0 && arr[0].length() >0 ) 
            	pLine = arr[0];
            //System.out.println("Step4---->"+pLine);
            //pLine = pLine.replaceAll("/\\*(?:.|[\\n\\r])*?\\*/","");
            //pLine = pLine.replaceAll("^/\\*\*(.|\s)+?\\*/","");
            //System.out.println("Step5---->"+pLine);
            //replace for , with a space
            //pLine = pLine.replaceAll(",", " , ");
            //System.out.println("Step6---->"+pLine);
            
            //pLine= StringUtils.replace(StringUtils.trim(pLine), "/\\*(?:.|[\\n\\r])*?\\*/", "");
            
            //this empties all the char between double quotes
            
                          //pLine = pLine.replaceAll("(?<=\")[^\"]*(?=\")","");
                          //this line removes empty double quote
                           //pLine = pLine.replaceAll("\"\"","");
            pLine = checkDoubleQuotes(pLine);
            pLine = checkSingleQuotes(pLine);
            
            //this line removes empty double quote
            //pLine = pLine.replaceAll("\"([^\"]*)\"" ,"");
            //for removing contents inside the single quotes
            //pLine = pLine.replaceAll("\'([^\']*)\'" ,"");



                           
                           //for removing contents inside the single quotes
                          // pLine = pLine.replaceAll("(?<=')[^']*(?=')","");
                          // pLine = pLine.replaceAll("''","");
                           
                           
           //System.out.println(":::::pLine:::::::"+pLine);                
            return pLine;
       }

        public String checkDoubleQuotes(String pLine) {
    		
   		 pLine = pLine.replaceAll("\"", " \" ");
   		pLine = pLine.replaceAll("\'", " \' ");
   		 Pattern p = Pattern.compile("\\s");
   			String[] words = p.split(pLine.trim());		 
   		 String totval ="";
   		 String lDoubleQuoteString ="";
   		 //boolean lDoubleQuoteFound=false;
   		 for (int i = 0; i < words.length; i++) {
   			 
   			 	
   			 
   			 if("".equals(words[i].replaceAll("[\\n\\r]*", "").trim())){
					continue; 
				 }  
   			 
   			 
   			 if("\"".equals(words[i].trim())){
   				 if(lDoubleQuoteFound == false){
   					 lDoubleQuoteFound = true;
   					//lDoubleQuoteString = words[i].trim()+"TFC_0000";
   					lDoubleQuoteString = words[i].trim()+"0000_TFC_";
  					 totval = totval+lDoubleQuoteString;
  					 continue;
   					 
   				 }else{
   					 lDoubleQuoteFound = false;
   					 //lDoubleQuoteString = "TFC_0000"+words[i].trim();
   					lDoubleQuoteString = words[i].trim();
   					 totval = totval+lDoubleQuoteString;
   					 continue;
   				 }
   			 }
   			 
   			 if(lDoubleQuoteFound == true ){
   				 
   				if("'".equals(words[i].trim())){
   	   				words[i]="_DBT_single_quote_";
   	   			 }
   				
   				 lDoubleQuoteString = words[i].trim()+"0000_TFC_";
   			 }
   			 else
   			 {
   				 lDoubleQuoteString = " "+words[i].trim()+" ";
   			 }
   			 
   			 totval = totval+lDoubleQuoteString;
   			 
   		 }
   		 //System.out.println("::::totval:::"+totval);
   		return totval;
   	}
        
        public String checkSingleQuotes(String pLine) {
        	//System.out.println(":::::pline:::"+pLine);
    		//System.out.println("lDoubleQuoteFound::->"+lDoubleQuoteFound);
        	 if(lDoubleQuoteFound==true){
        		 return pLine;
        	 }
      		 pLine = pLine.replaceAll("\'", " \' ");
      		 Pattern p = Pattern.compile("\\s");
      			String[] words = p.split(pLine.trim());		 
      		 String totval ="";
      		 String lSingleQuoteString ="";
      		 /*boolean lSingleQuoteFound=false;*/
      		 for (int i = 0; i < words.length; i++) {
      			 
      			 if("".equals(words[i].replaceAll("[\\n\\r]*", "").trim())){
   					continue; 
   				 }   				 
      			 
      			 if("\'".equals(words[i].trim())){
      				 if(lSingleQuoteFound == false){
      					 lSingleQuoteFound = true;
      					//lDoubleQuoteString = words[i].trim()+"_TFC_";
      					lSingleQuoteString = words[i].trim()+"0000_TFC_";
     					 totval = totval+lSingleQuoteString;
     					 continue;
      					 //continue;
      				 }else{
      					 lSingleQuoteFound = false;
      					 //lDoubleQuoteString = "TFC_"+words[i].trim();
      					lSingleQuoteString = words[i].trim();
      					 totval = totval+lSingleQuoteString;
      					 continue;
      				 }
      			 }
      			 
      			 if(lSingleQuoteFound == true ){
      				 //lDoubleQuoteString = words[i].trim()+"_";
      				
      				 lSingleQuoteString = words[i].trim()+"0000_TFC_";
      			 }
      			 else
      			 {
      				 lSingleQuoteString = " "+words[i].trim()+" ";
      			 }
      			 
      			 totval = totval+lSingleQuoteString;
      			 
      		 }
      		 totval=totval.replaceAll("_DBT_single_quote_", "'");
      		 //System.out.println("::::totval:::"+totval);
      		return totval;
      	}
	public String getHmPatternData(String pPramQueryString){
		 pPramQueryString = pPramQueryString.replaceAll("'","");
		 pPramQueryString =pPramQueryString.toUpperCase().trim();
		 
		 //if there is no value in the result value matching then return a constant
         if(pPramQueryString == null || "".equals(pPramQueryString))  return "BLANK";
         
         try {
        	 pPramQueryString = (String)lPatternDataHm.get(pPramQueryString);
        	 if(pPramQueryString == null || "".equals(pPramQueryString))  return "OTHER FORMAT";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "OTHER FORMAT";
		}
         
         return pPramQueryString;
	}
	

	
}
