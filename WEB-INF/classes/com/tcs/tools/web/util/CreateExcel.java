package com.tcs.tools.web.util;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;


public class CreateExcel {
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		System.out.println("::::inside main:::");		
		CreateExcel lCreateExcel = new CreateExcel();
		List pDataList = new ArrayList();
		
		lCreateExcel.createExcel();		 
	}
	
	public static void createExcel(List pDataList,String pPath,String pFileName,HttpServletResponse pResponse){
		 HSSFWorkbook workbook = new HSSFWorkbook(); 
		 HSSFSheet sheet = workbook.createSheet("new_sheet");		 
		 HSSFCellStyle headerStyle = getHeaderStyle( workbook);
		 HSSFCellStyle cellStyle = getCellStyle( workbook);
		 int lColCount =0;
		 
		 List lSubList = new ArrayList();
		 if(pDataList != null && pDataList.size()>0){
			 lSubList = (List)pDataList.get(0);
			 HSSFRow rowHeader = sheet.createRow((short)0);
			 if(lSubList != null && lSubList.size()>0){
				for (int i = 0; i < lSubList.size(); i++) {
					lColCount = createExcelCell(  rowHeader , headerStyle,lColCount,(String)lSubList.get(i));
				} 
			 }
		 }
		 
		 if(pDataList != null && pDataList.size() > 0){
			 for (int i = 0; i < pDataList.size(); i++) {
				 lColCount =0;
				 lSubList = (List)pDataList.get(i);
				 HSSFRow rowNormal= sheet.createRow((short)i);
				 
				 if(lSubList != null && lSubList.size() > 0){
					 for (int j = 0; j < lSubList.size(); j++) {
						 lColCount = createExcelCell(  rowNormal , cellStyle,lColCount,(String)lSubList.get(j));
					 }
				 }
			}
		 }
		 
		 
			
			
		 
		  
	
		/* HSSFCell cell = row.createCell((short) 0);
	     cell.setCellValue("Class Name 1 ");
	     cell.setCellStyle(style4);
	     
	      
	     HSSFCell cel2 = row.createCell((short) 1);
	     cel2.setCellValue("Class Name 2 ");
	     cel2.setCellStyle(style4);
	      
	     HSSFCell cel3 = row.createCell((short) 2);
	     cel3.setCellValue("Class Name  3");
	     cel3.setCellStyle(style4);*/
	      
	     autoSizeExcelColumn(sheet ,lColCount);
	  
	      try {
	    	  
	    	  FileUploadDownloadUtility.createFolders(pPath);
				 FileOutputStream fileOut = new FileOutputStream(pPath+pFileName);
				 workbook.write(fileOut);
				 fileOut.close();
				 System.out.println("Your excel file has been generated!");
				 if(pResponse != null){
					 //for file download
					 FileUploadDownloadUtility.downloadFile(pFileName,pPath,pResponse);
					 //for file download
				 }
			/*FileOutputStream fOut = new FileOutputStream("c:\\arun\\abc1.xls");
			  // Write the Excel sheet
			  workbook.write(fOut);
			  fOut.flush();
			  // Done deal. Close it.
			  fOut.close();*/
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("::::::mathod over::::");
	}
	
	public void createExcel(){
		 HSSFWorkbook workbook = new HSSFWorkbook(); 
		 HSSFSheet sheet = workbook.createSheet("Java Excels");
		  
		 
		 HSSFRow row = sheet.createRow((short)0); 
		
		
		 HSSFCellStyle headerStyle = getHeaderStyle( workbook);
		 int lColCount =0;
		 lColCount = createExcelCell(  row , headerStyle,lColCount,"Col1");
		 lColCount = createExcelCell(  row , headerStyle,lColCount,"Col2");
		 lColCount = createExcelCell(  row , headerStyle,lColCount,"Col3");
		 lColCount = createExcelCell(  row , headerStyle,lColCount,"Col4");
		 lColCount = createExcelCell(  row , headerStyle,lColCount,"Col5");
		 
		 HSSFRow row1 = sheet.createRow((short)1); 
			
			
		 HSSFCellStyle cellStyle = getCellStyle( workbook);
		  lColCount =0;
		 lColCount = createExcelCell(  row1 , cellStyle,lColCount,"Col1");
		 lColCount = createExcelCell(  row1 , cellStyle,lColCount,"Col2");
		 lColCount = createExcelCell(  row1 , cellStyle,lColCount,"Col3");
		 lColCount = createExcelCell(  row1 , cellStyle,lColCount,"Col4");
		 lColCount = createExcelCell(  row1 , cellStyle,lColCount,"Col5");
		/* HSSFCell cell = row.createCell((short) 0);
	     cell.setCellValue("Class Name 1 ");
	     cell.setCellStyle(style4);
	     
	      
	     HSSFCell cel2 = row.createCell((short) 1);
	     cel2.setCellValue("Class Name 2 ");
	     cel2.setCellStyle(style4);
	      
	     HSSFCell cel3 = row.createCell((short) 2);
	     cel3.setCellValue("Class Name  3");
	     cel3.setCellStyle(style4);*/
	      
	     autoSizeExcelColumn(sheet ,lColCount);
	  
	      try {
			FileOutputStream fOut = new FileOutputStream("c:\\arun\\abc1.xls");
			  // Write the Excel sheet
			  workbook.write(fOut);
			  fOut.flush();
			  // Done deal. Close it.
			  fOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("::::::mathod over::::");
	}

	public static int createExcelCell( HSSFRow row ,HSSFCellStyle pStyle,int pCellNo,String pCellText){
		HSSFCell cell = row.createCell((short) pCellNo);
	     cell.setCellValue(pCellText);
	     cell.setCellStyle(pStyle);
	     pCellNo++;
	     return pCellNo;
	}
	
	public static HSSFCellStyle getHeaderStyle( HSSFWorkbook pWorkbook){
		
		 //create and set font properties
		  HSSFFont fontHeader = pWorkbook.createFont();
		  fontHeader.setColor(HSSFColor.BLACK.index);		 
		  fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		  // Create the style and add bg color and border
		  HSSFCellStyle headerStyle = pWorkbook.createCellStyle();
	      headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      headerStyle.setBottomBorderColor(HSSFColor.BLACK.index);
	      headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      headerStyle.setTopBorderColor(HSSFColor.BLACK.index);		      
	      headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      headerStyle.setLeftBorderColor(HSSFColor.BLACK.index);		      
	      headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      headerStyle.setRightBorderColor(HSSFColor.BLACK.index);  
	      headerStyle.setFont(fontHeader);
	      //style4.setFillBackgroundColor(bg)
	      headerStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
	      headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		      
	  return headerStyle;
	}
	public static HSSFCellStyle getCellStyle( HSSFWorkbook pWorkbook){
		
		 //create and set font properties
		  HSSFFont fontHeader = pWorkbook.createFont();
		  fontHeader.setColor(HSSFColor.BLACK.index);		 
		  
		  // Create the style and add bg color and border
		  HSSFCellStyle headerStyle = pWorkbook.createCellStyle();
	      headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      headerStyle.setBottomBorderColor(HSSFColor.BLACK.index);
	      headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      headerStyle.setTopBorderColor(HSSFColor.BLACK.index);		      
	      headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      headerStyle.setLeftBorderColor(HSSFColor.BLACK.index);		      
	      headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      headerStyle.setRightBorderColor(HSSFColor.BLACK.index);  
	      headerStyle.setFont(fontHeader);
	      
	      
		      
	  return headerStyle;
	}
	
	public static void  autoSizeExcelColumn(HSSFSheet pSheet ,int pColumnCount){
		/*pSheet.autoSizeColumn((short)0) ;
		pSheet.autoSizeColumn((short)1) ;
		pSheet.autoSizeColumn((short)2) ;*/
		//System.out.println("::auto size method modified 111111::::::");
		  
		for(int i=0; i < pColumnCount; i++){
			pSheet.autoSizeColumn((short)i ) ;
			//System.out.println(":::i val::"+i);
		}
		//pSheet.autoSizeColumn((short)0) ;
		//pSheet.autoSizeColumn((short)1) ;
	}

}
