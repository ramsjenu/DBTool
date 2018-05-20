package com.tcs.tools.web.idmt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;


public class CsToolNamingConventions {
	
	 public String set_NamingConvention(String pFolderPath,String pFileName,String pOutpurFolderpath){
		 String lOutputFile ="";
		 try{
		 File file = new File(pFolderPath+pFileName);
    	 InputStream is = new FileInputStream(file);
			StringBuffer buffer = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = is.read(b)) != -1;) {
				
				buffer.append(new String(b, 0, n));
			}
			
         String content = buffer.toString();
         //System.out.println("::::before line count"+ content.split("[\\r\\n]+").length);
        // content=content.replaceAll(";", ";\r\n");
        // System.out.println("::::after line count"+ content.split(";[\\r\\n]*").length);
         
         //String[] lines = content.split("\\n\\r+");
         content=content.replaceAll(";[\\r\\n]*", "; _DBT_DELIM");
         content=content.replaceAll("(?i)\\bGO\\b[\\s\\r\\n]+", "go _DBT_DELIM");
         String[] lines = content.split("\\s*_DBT_DELIM");

        String oldName = null;
        String preName = null;
        String rep=null;
        String exte = null;
        int no = 0;

        String[] ixoldname=new String[(lines.length)];
        String[] ixnewname = new String[(lines.length )];
        String match = null;
        int l = 0;
        Pattern lAlterTablePattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b\\s+");
        
        Pattern lCreateindexPattern = Pattern.compile("(?i)\\bCREATE\\b[\\w\\r\\n\\s]+\\bINDEX\\b\\s+");
        Pattern lTableNamePattern = Pattern.compile("(?i)\\bON\\b\\s+(.+?)[\\s\\r\\n]*\\(");
        Pattern lDBObjectNameForIndexPattern = Pattern.compile("(?i)\\bCREATE\\b[\\w\\r\\n\\s]+\\bINDEX\\b[\\s\\r\\n]+(.+?)[\\s\\r\\n]+\\bON\\b");
        
        Pattern lAlterTablePrimaryKeyPattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b[\\W\\w\\r\\n\\s]+\\bPRIMARY\\b\\s+\\bKEY\\b");
        Pattern lAlterTableForeignKeyPattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b[\\W\\w\\r\\n\\s]+\\bFOREIGN\\b\\s+\\bKEY\\b");
        Pattern lAlterTableCheckConstraintPattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b[\\W\\w\\r\\n\\s]+\\bCONSTRAINT\\b[\\W\\w\\r\\n\\s]+\\bCHECK\\b");
        //Pattern lAlterTableForeignKeyPattern = Pattern.compile("(?i)\\bFOREIGN\\b\\s+\\bKEY\\b\\s+(.+?)[\\s\\r\\n]+\\(");
        Pattern lAlterTableCheckPattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b\\s+");
        Pattern lDBObjectNameForTablePattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b\\s+\\b(ADD|MODIFY|DROP)+\\b(.+?)");
        Pattern lAlterTableNamePattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b\\s+(.+?)\\b(ADD|MODIFY|DROP)+\\b");
        
        
        
        
        Pattern lObjectNameForeignKeyPattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b[\\W\\w\\r\\n\\s]+\\bFOREIGN\\b\\s+\\bKEY\\b\\s+(.+?)[\\r\\n\\s]+");
        Pattern lObjectNamePrimaryKeyPattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b[\\W\\w\\r\\n\\s]+\\bPRIMARY\\b\\s+\\bKEY\\b\\s+(.+?)[\\r\\n\\s]+");
        Pattern lObjectNameCheckConstraintPattern = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b[\\W\\w\\r\\n\\s]+\\bCONSTRAINT\\b[\\W\\w\\r\\n\\s]+\\bCHECK\\b");
        
        
        //new way
        Pattern lAlterTablePrimaryKeyPattern1 = Pattern.compile("(?i)\\bALTER\\b[\\r\\n\\s]+\\bTABLE\\b[\\W\\w\\r\\n\\s]+\\bADD\\b\\s+\\bCONSTRAINT\\b\\s+(.+?)\\s+\\bPRIMARY\\b\\s+\\bKEY\\b\\s+");
        
        Matcher lTabNameMatcher=null;
        Matcher lDBObjectNameMatcher=null;
        
        String lTableName ="";
        String lReplacementString ="";
        String lIndexNameAppender ="_ix";
        String lForeignKeyAppender ="_fk";
        String lPrimaryKeyAppender ="_pk";
        
        HashMap  lTableNameMap = new HashMap();
        HashMap  lAlterPrimaryKeyMap = new HashMap();
        HashMap  lForeignKeyMap = new HashMap();
        HashMap  lAlterCheckMap = new HashMap();
        for (int i = 0; i < lines.length; i++)
        {
        	lTableName ="";
        	lReplacementString = "";
        	//System.out.println("::::lines[i]::::"+lines[i]);
        	//for alter table 
        	if((lAlterTablePattern.matcher(lines[i]).find()) ){
        		 //System.out.println("::::inside alter table::::");
        		 lTabNameMatcher=lAlterTableNamePattern.matcher(lines[i].trim());
        		
        		 if(lTabNameMatcher.find()){
 	       			lTableName = lTabNameMatcher.group(1).trim();
 	       		//System.out.println("::::lines[i]::::"+lines[i]+":::lTableName::::"+lTableName);
 	       			//System.out.println(lTabNameMatcher.group(1));
 	       		 }
        		 
        		 
        		/* if(lTableNameMap.containsKey(lTableName)){
 	       			lTableNameMap.put(lTableName,String.valueOf(Integer.parseInt((String)lTableNameMap.get(lTableName))+1 ));
 	       		 }else{
 	       			lTableNameMap.put(lTableName, "1");
 	       		 }*/
        		 
        		 //for primary key
        		 
        		//alter table
        		 lDBObjectNameMatcher = lAlterTablePrimaryKeyPattern1.matcher(lines[i].trim() );
            	 if(lDBObjectNameMatcher.find() ){
            		 if(lAlterPrimaryKeyMap.containsKey(lTableName)){
          				lAlterPrimaryKeyMap.put(lTableName,String.valueOf(Integer.parseInt((String)lForeignKeyMap.get(lTableName))+1 ));
          				 //System.out.println(":::lAlterPrimaryKeyMap::else::"+lTableName);
       	       		}else{
       	       		lAlterPrimaryKeyMap.put(lTableName, "1");
       	       			//System.out.println(":::lAlterPrimaryKeyMap::::"+lTableName);
       	       			
       	       		}
            		 
            		 lReplacementString = lTableName+""+lPrimaryKeyAppender+ lAlterPrimaryKeyMap.get(lTableName)+" ";
	 	       			lines[i] = lines[i].replaceAll(lDBObjectNameMatcher.group(1), lReplacementString);
            		 
            	 }
        		
        		 
        		 //for forein key
        		 if(lAlterTableForeignKeyPattern.matcher(lines[i]).find() ){
        			 
        			if(lForeignKeyMap.containsKey(lTableName)){
        				 lForeignKeyMap.put(lTableName,String.valueOf(Integer.parseInt((String)lForeignKeyMap.get(lTableName))+1 ));
        				 //System.out.println(":::lForeignKeyMap::else::"+lTableName);
     	       		}else{
     	       			lForeignKeyMap.put(lTableName, "1");
     	       			//System.out.println(":::lForeignKeyMap::::"+lTableName);
     	       			
     	       		}
        			 
        			 
        			 //System.out.println("::::foreign key found");
        			 lDBObjectNameMatcher=lObjectNameForeignKeyPattern.matcher(lines[i].trim());
        			 if(lDBObjectNameMatcher.find()){
	        			//System.out.println("::::$$$$$$$$$$$$$$::::"+lDBObjectNameMatcher.group(1));
	        			 
	        			lReplacementString = lTableName+""+lForeignKeyAppender+ lForeignKeyMap.get(lTableName)+" ";
	 	       			lines[i] = lines[i].replaceAll(lDBObjectNameMatcher.group(1), lReplacementString);
        			 }
        		 }
        		 
        		 
        		 
        		//for check constraint
        		 if(lAlterTableCheckConstraintPattern.matcher(lines[i]).find() ){
        			 
        			if(lAlterCheckMap.containsKey(lTableName)){
        				lAlterCheckMap.put(lTableName,String.valueOf(Integer.parseInt((String)lAlterCheckMap.get(lTableName))+1 ));
        				// System.out.println(":::lForeignKeyMap::else::"+lTableName);
     	       		}else{
     	       			lAlterCheckMap.put(lTableName, "1");
     	       			//System.out.println(":::lForeignKeyMap::::"+lTableName);
     	       			
     	       		}
        			 
        			 
        			 //System.out.println("::::check constrinat found ::::");
        			 lDBObjectNameMatcher=lObjectNameForeignKeyPattern.matcher(lines[i].trim());
        			 if(lDBObjectNameMatcher.find()){
	        			//System.out.println("::::$$$$$$$$$$$$$$::::"+lDBObjectNameMatcher.group(1));
	        			 
	        			lReplacementString = lTableName+""+lForeignKeyAppender+ lAlterCheckMap.get(lTableName)+" ";
	 	       			lines[i] = lines[i].replaceAll(lDBObjectNameMatcher.group(1), lReplacementString);
        			 }
        		 }
        		
        	}else if((lCreateindexPattern.matcher(lines[i]).find()) ){ //for create index
        		
        		lTabNameMatcher=lTableNamePattern.matcher(lines[i].trim());
	       		if(lTabNameMatcher.find()){
	       			lTableName = lTabNameMatcher.group(1).trim();
	       			//System.out.println(lTabNameMatcher.group(1));
	       		}
	       		
	       		
	       		
	       		if(lTableNameMap.containsKey(lTableName)){
	       			lTableNameMap.put(lTableName,String.valueOf(Integer.parseInt((String)lTableNameMap.get(lTableName))+1 ));
	       		}else{
	       			lTableNameMap.put(lTableName, "1");
	       		}
	       		
	       		
	       		System.out.println("::::create index pattern found-table name-> ::::"+lTableName+"::::line val---"+lines[i].trim());
	       		lDBObjectNameMatcher=lDBObjectNameForIndexPattern.matcher(lines[i].trim());
	       		if(lDBObjectNameMatcher.find()){
	       			
	       			//System.out.println(lDBObjectNameMatcher.group(1));
	       			lReplacementString = lTableName+""+lIndexNameAppender+ lTableNameMap.get(lTableName)+" ";
	       			lines[i] = lines[i].replaceAll(lDBObjectNameMatcher.group(1), lReplacementString);
	       		}
	       		
	       		
	       		
	       		
       		
        	}
        	
        	/*
            
            if (lines[i].contains("ALTER TABLE "))
            {
                match = null;

                if (lines[i + 1].trim().contains(" PRIMARY KEY"))
                {
                    rep = " PRIMARY KEY";
                    exte = "_pk";
                }
                else if (lines[i + 1].trim().contains(" FOREIGN KEY"))
                {
                    rep = " FOREIGN KEY";
                    exte = "_fk1";
                }
                else if (lines[i + 1].trim().contains(" CHECK "))
                {
                    rep = " CHECK ";
                    exte = "_ck1";
                }

                oldName = lines[i + 1].replaceAll("ADD CONSTRAINT ", "");
                //oldName = oldName.Remove(oldName.indexOf(rep)).Trim();
                oldName = oldName.substring(0,oldName.indexOf(rep)).trim();

                ixoldname[l] = oldName;

                //oldName = oldName.trim().Remove(0, oldName.trim().indexOf("_") + 1);
                oldName = oldName.trim().substring(oldName.trim().indexOf("_") + 1,oldName.length()-1);

                if (ixnewname[0] == null)
                    ixnewname[l] = oldName + exte;
                else
                {
                    for (int k = 0; k < l; k++)
                    {
                        preName = ixoldname[k];
                        //preName = preName.Trim().Remove(0, preName.Trim().IndexOf("_") + 1);
                        preName = preName.trim().substring(preName.trim().indexOf("_") + 1,preName.length()-1);

                        if (oldName.equals(preName))
                        {
                            match = ixnewname[k];
                            //no = Integer.ParseInt((match.substring(match.lastIndexOf("_"))).replaceAll(exte.Remove(3), ""));
                            no = Integer.parseInt((match.substring(match.lastIndexOf("_"))).replaceAll(exte.substring(0,exte.length()-3), ""));
                            no++;
                            //ixnewname[l] = oldName + exte.Remove(exte.Length - 1, 1) + no.ToString();
                            ixnewname[l] = oldName + exte.substring(0,exte.length() -2 ) + no;
                        }
                    }
                    if (match == null)
                        ixnewname[l] = oldName + exte;
                }
                lines[i+1] = lines[i+1].replaceAll(ixoldname[l], ixnewname[l]);
                l++;                        
            }
            else if (lines[i].contains("CREATE ") && lines[i].contains(" INDEX "))
            {
                exte = "_ix1";
                match = null;
                

                
                //oldName = lines[i].Remove(0, lines[i].trim().indexOf(".") + 1);
                oldName = lines[i].substring(lines[i].trim().indexOf(".") + 2);
                
                //oldName = oldName.Remove(oldName.IndexOf(" ON")).Trim();
                oldName = oldName.substring(0,oldName.indexOf(" ON")).trim();

                ixoldname[l]=oldName;

                //oldName = oldName.Trim().Remove(0, oldName.Trim().IndexOf("_") + 1);
                oldName = oldName.trim().substring(oldName.trim().indexOf("_") + 1);

                if (ixnewname[0] == null)
                    ixnewname[l] = oldName + exte;
                else
                {
                    for(int k=0;k<l;k++)
                    {
                        preName=ixoldname[k];
                        //preName = preName.Trim().Remove(0, preName.Trim().IndexOf("_") + 1);
                        preName = preName.trim().substring(preName.trim().indexOf("_") + 1);

                        if(oldName.equalsIgnoreCase(preName))
                        {
                            match=ixnewname[k];
                            System.out.println("exte:::::"+exte+"<<<>>match::->"+match);
                            //no = Int32.Parse((match.Substring(match.LastIndexOf("_"))).Replace(exte.Remove(3), ""));
                            System.out.println("-----------------");
                            System.out.println("sub::"+match.substring(match.lastIndexOf("_")));
                            System.out.println("repla::"+exte.substring(0,3));
                            System.out.println("Com::->"+(match.substring(match.lastIndexOf("_"))).replaceAll(exte.substring(0,3), ""));
                            System.out.println("-----------------");
                            no = Integer.parseInt((match.substring(match.lastIndexOf("_"))).replaceAll(exte.substring(0,3), ""));
                            no++;
                            //ixnewname[l] = oldName + exte.Remove(exte.length() - 1, 1) + no;                                    
                            ixnewname[l] = oldName + exte.substring(0,exte.length() - 1) + no;
                        }   
                    }
                    if(match==null)
                        ixnewname[l]=oldName + exte;
                }
                System.out.println("::::before:::::"+lines[i]);
                lines[i] = lines[i].replaceAll(ixoldname[l], ixnewname[l]);
                System.out.println("::::after:::::"+lines[i]);
                l++;

            }

        */}

       
        String[] lRetArr =  ToolsUtil.splitFileNameAndExtension(pFileName);
        
       
        lOutputFile = pOutpurFolderpath+lRetArr[0]+"_setted"+"."+lRetArr[1];
        FileUploadDownloadUtility.createFolders(pOutpurFolderpath);
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(lOutputFile));
        System.out.println(pFolderPath+"filename_quotes_removed11.txt");
        for (int i = 0; i < lines.length;i++)
        {
        	 writer.write(lines[i].trim());
             writer.newLine();
        }
       
        writer.close();
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    finally
    {
        
    }
	return lOutputFile;
     }
	

	 public static void main(String args[]){
		 String pFolderPath="C:\\arun\\documents\\project\\idmt integration\\work\\naming converntions\\errors\\";
		 String pFileName="gxprd01_DDL.src";
		 System.out.println(":::inside main:::");
		 CsToolNamingConventions lCsToolNamingConventions = new CsToolNamingConventions();
		 lCsToolNamingConventions.set_NamingConvention(pFolderPath, pFileName,pFolderPath);
		 System.out.println(":::::main over::::");
		 
		 
	 }

}
