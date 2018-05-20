package com.tcs.tools.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	
	public static String zipFolder(String pFolderPath,String pTargetFileName){

		  try
		 {
		 File inFolder=new File(pFolderPath);
		   File outFolder=new File(pTargetFileName);
		 ZipOutputStream out = new ZipOutputStream(new 
		BufferedOutputStream(new FileOutputStream(outFolder)));
		 BufferedInputStream in = null;
		 byte[] data  = new byte[1000];
		 String files[] = inFolder.list();
		 for (int i=0; i<files.length; i++)
		  {
		  in = new BufferedInputStream(new FileInputStream
		(inFolder.getPath() + "/" + files[i]), 1000);  
		out.putNextEntry(new ZipEntry(files[i])); 
		  int count;
		  while((count = in.read(data,0,1000)) != -1)
		  {
		 out.write(data, 0, count);
		  }
		  out.closeEntry();
		  }
		  out.flush();
		  out.close();
		  }
		  catch(Exception e)
		 {
		  e.printStackTrace();
		  } 
		 return pTargetFileName;
	}
	public static void main(String a[])
	  {
		String pFolderPath ="C:\\arun\\documents\\project\\idmt integration\\work\\remove quotes";
		String pTargetFileName = "C:\\arun\\documents\\project\\idmt integration\\work\\remove quotes.zip";
		zipFolder( pFolderPath, pTargetFileName);
	
	 }

}
