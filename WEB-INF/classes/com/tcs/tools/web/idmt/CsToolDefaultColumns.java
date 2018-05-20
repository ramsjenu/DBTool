package com.tcs.tools.web.idmt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class CsToolDefaultColumns {

	
	 public String default_columns(String pFolderPath,String pFileName,String pOutputPath)
     {
		 System.out.println(":::inside method::::");
		 String lTargetFilename="";
         try
         {
        	 //StreamReader sr = new StreamReader(new FileStream(ddlPath, FileMode.Open));
        	 File file = new File(pFolderPath+pFileName);
        	 InputStream is = new FileInputStream(file);
 			StringBuffer buffer = new StringBuffer();
 			byte[] b = new byte[4096];
 			for (int n; (n = is.read(b)) != -1;) {
 				
 				buffer.append(new String(b, 0, n));
 			}
 			
             String content = buffer.toString();
             String[] lines = content.split("\n");
             
             System.out.println(":::array length:::::"+lines.length);

            /* Excel.Application xlApp ;
             Excel.Workbook xlWorkBook ;
             Excel.Worksheet xlWorkSheet ;
             object misValue = System.Reflection.Missing.Value;

             xlApp = new Excel.ApplicationClass();
             xlWorkBook = xlApp.Workbooks.Add(misValue);

             xlWorkSheet = (Excel.Worksheet)xlWorkBook.Worksheets.get_Item(1);
             xlWorkSheet.Cells[1, 1] = "Schema";
             xlWorkSheet.Cells[1, 2] = "Table Name";
             xlWorkSheet.Cells[1, 3] = "Column";
             xlWorkSheet.Cells[1, 4] = "Column Name";
             xlWorkSheet.Cells[1, 5] = "DataType";
             xlWorkSheet.Cells[1, 6] = "Default";
             xlWorkSheet.Cells[1, 7] = "Function";
             xlWorkSheet.Cells[1, 8] = "NullType";
             xlWorkSheet.Cells[1, 9] = "Result";*/
             
             
             HSSFWorkbook hwb=new HSSFWorkbook();
				HSSFSheet sheet = hwb.createSheet("new sheet");
				
				HSSFRow rowhead= sheet.createRow((short)0);
				rowhead.createCell((short) 0).setCellValue("Schema");
				//rowhead.createCell((short) 1).setCellValue("Stored Procedure Name");
				rowhead.createCell((short) 1).setCellValue("Table Name");
				rowhead.createCell((short) 2).setCellValue("Column");
				rowhead.createCell((short) 3).setCellValue("Column Name");
				rowhead.createCell((short) 4).setCellValue("DataType");
				rowhead.createCell((short) 5).setCellValue("Default");
				rowhead.createCell((short) 6).setCellValue("Function");
				rowhead.createCell((short) 7).setCellValue("NullType");
				rowhead.createCell((short) 8).setCellValue("Result");
				


             int row=1;
             int col=0;
             
             Pattern lCreateTablePattern = Pattern.compile("(?i)\\bCREATE\\b[\\s\\w\\W\\r\\n]+\\bTABLE\\b[\\s\\w\\W\\r\\n]+");
            		 
             int lExcelRowCount =1;
             for (int i = 0; i < lines.length; i++)
             {	 //if (lCreateTablePattern.matcher(lines[i]).find())	//c#
            	
            	
            	
                 if (lines[i].contains("CREATE TABLE ") || lines[i].contains("create table "))
                 {
                	
                     String tabName=null;
                     if (lines[i].contains("create table "))
                     {
                         tabName = lines[i].trim().replaceAll("create table ", "");
                         //tabName = tabName.trim().Remove(tabName.Length-1, 1); //c#
                         //here in this line ( will come after the table name
                         tabName = tabName.trim().substring(0, tabName.length()-1);
                     }
                     else{
                         tabName = lines[i].trim().replaceAll("CREATE TABLE ", "");
                     }
                     
                     //System.out.println(":::::inside create table::::"+tabName);
                     int j = i;
                     while (!lines[j].trim().equalsIgnoreCase("go"))
                     {
                         if (lines[j].contains("DEFAULT "))
                         { HSSFRow excel_row= sheet.createRow((short)(row));
                             String datatype = null;
                             String temp = " ";
                             col = 0;
                             //xlWorkSheet.Cells[row, col] = "dbo";
                             excel_row.createCell((short) col).setCellValue("dbo");
                             col++;

                             //xlWorkSheet.Cells[row, col] = tabName;
                             excel_row.createCell((short) col).setCellValue(tabName);                             
                             col++;

                             //xlWorkSheet.Cells[row, col] = lines[j].trim();
                             excel_row.createCell((short) col).setCellValue(lines[j].trim());  
                             col++;

                             String[] cols = lines[j].trim().split("\\s");
                             //System.out.println(":::::cols:::::"+cols.toString());
                             for (int k = 0; k < cols.length; k++) {
								//System.out.println("::::col-"+k+":::"+cols[k]);
							}
                             int k = 0;

                             //xlWorkSheet.Cells[row, col] = cols[k];
                             excel_row.createCell((short) col).setCellValue(cols[k]);  
                             col++;
                             k++;
                             if (cols[k].equals(""))
                                 k++;



                             //if (lines[j].Contains("suser_name()"))
                             //{
                             //    xlWorkSheet.Cells[row, col] = "varchar(128)";
                             //    datatype = "varchar(128)";
                             //}
                             //else 
                             if (lines[j].trim().contains(" user"))
                             {
                                 //xlWorkSheet.Cells[row, col] = "varchar(8)";
                            	 excel_row.createCell((short) col).setCellValue("varchar(8)");  
                                 datatype = "varchar(8)";
                             }
                             else if (lines[j].contains(" datetime "))
                             {
                                 //xlWorkSheet.Cells[row, col] = "timestamp";
                            	 excel_row.createCell((short) col).setCellValue("timestamp");  
                                 datatype = "timestamp";
                             }
                             else if (lines[j].contains(" smalldatetime "))
                             {
                                 //xlWorkSheet.Cells[row, col] = "timestamp";
                            	 excel_row.createCell((short) col).setCellValue("timestamp");  
                                 datatype = "timestamp";
                             }
                             else if (lines[j].contains(" bit "))
                             {
                                 //xlWorkSheet.Cells[row, col] = "smallint";
                            	 excel_row.createCell((short) col).setCellValue("smallint");  
                                 datatype = "smallint";
                             }
                             else if (lines[j].contains(" money "))
                             {
                                 //xlWorkSheet.Cells[row, col] = "decimal(19,4)";
                                 excel_row.createCell((short) col).setCellValue("decimal(19,4)");  
                                 datatype = "decimal(19,4)";
                             }
                             else if (lines[j].contains(" tinyint "))
                             {
                                 //xlWorkSheet.Cells[row, col] = "smallint";
                            	 excel_row.createCell((short) col).setCellValue("smallint");  
                                 datatype = "smallint";
                             }

                             else
                             {

                                 datatype = lines[j].replaceAll(cols[0], "").trim();
                                 datatype = datatype.trim().substring(0, datatype.indexOf(" "));
                                 //xlWorkSheet.Cells[row, col] = datatype;
                                 excel_row.createCell((short) col).setCellValue(datatype);  
                             }

                             col++;
                             k++;
                             if (cols[k].trim().equals(""))
                                 k++;

                             //xlWorkSheet.Cells[row, col] = "DEFAULT";
                             excel_row.createCell((short) col).setCellValue("DEFAULT");  
                             
                             col++;

                             k++;
                             if (cols[k].trim().equals(""))
                                 k++;


                             temp = " " + lines[j].substring(lines[j].indexOf("DEFAULT ") + 7).trim();
                             if (temp.contains("NOT NULL")){
                                 //temp = temp.Remove(temp.indexOf("NOT NULL"));                            	
                            	 //temp = temp.substring(temp.indexOf("NOT NULL"),temp.length()-1);
                            	 temp = temp.substring(0,temp.indexOf("NOT NULL"))+"NOT NULL";
                             }else if (temp.contains("NULL")){
                                 //temp = temp.Remove(temp.indexOf("NULL"));
                            	 //temp = temp.substring(temp.indexOf("NULL"),temp.length()-1);
                            	 temp = temp.substring(0,temp.indexOf("NULL"))+"NULL";
                             }
                             System.out.println("::::temp val::::"+temp);
                             //if (String.Equals(temp.trim(), "suser_name()", StringComparison.OrdinalIgnoreCase))
                             if ("suser_name()".equalsIgnoreCase(temp.trim()))	 
                             {
                                 if (lines[j].contains(" datetime "))
                                 {
                                     //xlWorkSheet.Cells[row, 5] = "timestamp";
                                	 excel_row.createCell((short) 5).setCellValue("timestamp");  
                                     datatype = "timestamp";

                                     //xlWorkSheet.Cells[row, col] = "current timestamp";
                                     excel_row.createCell((short) col).setCellValue("current timestamp");  
                                     temp = "current timestamp";
                                 }
                                 else
                                 {	 
                                     //xlWorkSheet.Cells[row, 5] = "varchar(8)";
                                	 excel_row.createCell((short) 5).setCellValue("varchar(8)");
                                     datatype = "varchar(8)";

                                     //xlWorkSheet.Cells[row, col] = "user";
                                     excel_row.createCell((short) col).setCellValue("user");
                                     temp = "user";
                                 }

                             }

                             //else if (string.Equals(temp.trim(), "getdate()", StringComparison.OrdinalIgnoreCase))
                             else if ("getdate()".equalsIgnoreCase(temp.trim()))	 
                             {
                                 //xlWorkSheet.Cells[row, col] = "current timestamp";
                            	 excel_row.createCell((short) col).setCellValue("current timestamp");
                                 temp = "current timestamp";
                             }
                             else if (temp.trim().substring(0, 1).equals("\""))
                             {
                                 temp = " " + temp.replaceAll("\"", "\'");
                                 //xlWorkSheet.Cells[row, col] = temp;
                                 excel_row.createCell((short) col).setCellValue(temp);
                             }
                             else if (temp.trim().equals("\'19000101\'"))
                             {
                                 temp = temp.replace("19000101", "1900-01-01");
                                 //xlWorkSheet.Cells[row, col] = temp;
                                 excel_row.createCell((short) col).setCellValue(temp);
                             }

                             else{
                                 //xlWorkSheet.Cells[row, col] = temp.trim();
                            	 excel_row.createCell((short) col).setCellValue(temp.trim());
                             }




                             col++;
                             k++;
                             if (cols[k].trim().equals(""))
                                 k++;

                             if (lines[j].contains("NOT")){
                                 //xlWorkSheet.Cells[row, col] = "NOT NULL";
                            	 excel_row.createCell((short) col).setCellValue("NOT NULL");
                             }else if (lines[j].contains("NULL")){
                                 //xlWorkSheet.Cells[row, col] = " ";
                            	 excel_row.createCell((short) col).setCellValue(" ");
                             }

                             col++;
                             k++;
                             if (cols[k].trim().equals(""))
                                 k++;

                             //xlWorkSheet.Cells[row, col] = "dbo." + tabName.trim() + "." + cols[0].trim() + "=" + datatype.trim() + " " + "DEFAULT" + " " + temp.trim();
                             excel_row.createCell((short) col).setCellValue("dbo." + tabName.trim() + "." + cols[0].trim() + "=" + datatype.trim() + " " + "DEFAULT" + " " + temp.trim());

                             row++;
                         }
                         else
                         {
                             //if (lines[j].contains(" timestamp "))
                             //{
                             //    xlWorkSheet.Cells[row, 1] = "dbo";

                             //    xlWorkSheet.Cells[row, 2] = tabName;

                             //    xlWorkSheet.Cells[row, 3] = lines[j].Trim();

                             //    string colName = lines[j].Trim().Substring(0, lines[j].IndexOf(" ")).Trim();
                             //    string dataType = "timestamp";
                             //    xlWorkSheet.Cells[row, 4] = colName;
                             //    xlWorkSheet.Cells[row, 5] = dataType;
                             //    xlWorkSheet.Cells[row, 9] = "dbo." + tabName.Trim() + "." + colName.Trim() + "=" + dataType.Trim();
                             //    row++;
                             //}
                             //else 
                             if (lines[j].contains("nvarchar"))
                             {	
                            	 HSSFRow excel_row= sheet.createRow((short)(row));
                                 //xlWorkSheet.Cells[row, 1] = "dbo";
                            	 excel_row.createCell((short) 0).setCellValue("dbo");

                                 //xlWorkSheet.Cells[row, 2] = tabName;
                                 excel_row.createCell((short) 1).setCellValue(tabName);

                                 //xlWorkSheet.Cells[row, 3] = lines[j].trim();
                                 excel_row.createCell((short) 2).setCellValue(lines[j].trim());
                                 
                                 String colName = lines[j].trim().substring(0, lines[j].indexOf(" ")).trim();
                                 String dataType = lines[j].trim().substring(lines[j].indexOf(" ")).trim();
                                 //dataType = dataType.Remove(dataType.indexOf(" "));
                                 dataType = dataType.substring(dataType.indexOf(" "),dataType.length()-1);
                                 
                                 //xlWorkSheet.Cells[row, 4] = colName;
                                 excel_row.createCell((short) 3).setCellValue(colName);
                                 
                                 //xlWorkSheet.Cells[row, 5] = dataType;
                                 excel_row.createCell((short) 4).setCellValue(dataType);
                                 
                                 //xlWorkSheet.Cells[row, 9] = "dbo." + tabName.trim() + "." + colName.trim() + "=" + dataType.trim();
                                 System.out.println(":::value:::::"+"dbo." + tabName.trim() + "." + colName.trim() + "=" + dataType.trim());
                                 excel_row.createCell((short) 8).setCellValue("dbo." + tabName.trim() + "." + colName.trim() + "=" + dataType.trim());
                                 row++;
                             }

                         }
                         col = 1;                         
                         j++;
                     }
                 }
             }
            
             //xlWorkBook.SaveAs("DefaultColumns.xls", Excel.XlFileFormat.xlWorkbookNormal, misValue, misValue, misValue, misValue, Excel.XlSaveAsAccessMode.xlExclusive, misValue, misValue, misValue, misValue, misValue);
             //xlWorkBook.Close(true, misValue, misValue);
             //xlApp.Quit();
             
            
             
             FileUploadDownloadUtility.createFolders(pOutputPath);
             String[] lRetArr =  ToolsUtil.splitFileNameAndExtension(pFileName);
             FileOutputStream fileOut = new FileOutputStream(pOutputPath+lRetArr[0]+"_DEFAULTS.xls");
             System.out.println(pOutputPath+lRetArr[0]+"_DEFAULTS.xls");
             lTargetFilename=pOutputPath+lRetArr[0]+"_DEFAULTS.xls";
				hwb.write(fileOut);
				fileOut.close();
             //MessageBox.Show("Excel File Created");
				
				

         }
         catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e)
         {	e.printStackTrace();
             //MessageBox.Show(e.Message);
         }
         finally
         {
            // sr.Close();
         }
         return lTargetFilename;
     }
	 
	 public static void main(String args[]){
		 String pFolderPath="C:\\arun\\documents\\project\\idmt integration\\work\\default columns\\errors\\";
		 String pFileName="gxprd01_DDL.src";
		 String pOutputPath ="C:\\arun\\documents\\project\\idmt integration\\work\\default columns\\processed\\";
		 System.out.println(":::inside main11111:::");
		 CsToolDefaultColumns lCsToolSource = new CsToolDefaultColumns();
		 lCsToolSource.default_columns(pFolderPath, pFileName,pOutputPath);
		 System.out.println(":::::main over::::");
		 
		 
	 }
}
