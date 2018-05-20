package com.tcs.tools.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

public class ZipUtilRecursive {

	public static void main(String[] args ){
		System.out.println("::::inside main::::");
		 //createZipFile(String folderName,String outZipFilename) 
		String folderName ="C:\\arun\\Tool Output\\dcms";
		String outZipFilename ="C:\\arun\\Tool Output\\dcms_test\\1.zip";
		try {
			createZipFile(folderName,outZipFilename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	/*public static void createZipFile( HttpServletResponse response) throws IOException
	{
	   try 
	   { 
	      String outZipFilename =  "/myFile.zip"; 
	      FileOutputStream fout = new FileOutputStream(outZipFilename);
	      ZipOutputStream zout = new ZipOutputStream(fout); 
	      zipFile(folderName, zout, "/");  // call the function below

	      // complete the ZIP file
	      //
	      zout.close();

	      // read out
	      //
	      FileInputStream in = new FileInputStream(outZipFilename); 

	      int len; 
	      OutputStream os = response.getOutputStream();
	      byte[] buf = new byte[1024];

	      while ((len = in.read(buf)) > 0) 
	      { 
	            os.write(buf, 0, len); 
	      }

	      in.close();
	      os.flush();
	      os.close();
	            
	      File f = new File("/myFile.zip");
	      if (f.exists()) f.delete();
	   }
	   catch (IOException e) 
	   { 
	       e.printStackTrace();
	       throw e;
	   } 
	}*/
	
	public static void createZipFile(String folderName,String outZipFilename) throws IOException
	{
	   try 
	   { 
	      //String outZipFilename =  "/myFile.zip"; 
	      FileOutputStream fout = new FileOutputStream(outZipFilename);
	      ZipOutputStream zout = new ZipOutputStream(fout); 
	      zipFile(folderName, zout, "");  // call the function below
	      //zipFile(folderName, zout, "zipped/");  // call the function below

	      // complete the ZIP file
	      //
	      zout.close();

	      // read out
	      //
	      FileInputStream in = new FileInputStream(outZipFilename); 

	    /*  int len; 
	      OutputStream os = response.getOutputStream();
	      
	      byte[] buf = new byte[1024];

	      while ((len = in.read(buf)) > 0) 
	      { 
	            os.write(buf, 0, len); 
	      }

	      in.close();
	      os.flush();
	      os.close();
	            
	      File f = new File("/myFile.zip");
	      if (f.exists()) f.delete();*/
	   }
	   catch (IOException e) 
	   { 
	       e.printStackTrace();
	       throw e;
	   } 
	}

	
	private static void zipFile(String path, ZipOutputStream out, String relPath) throws IOException 
	{
	   File file = new File(path);
	        
	   if (!file.exists())
	   {
	        System.out.println(file.getName() + " does NOT exist!");
	        return;
	   }
	        
	   byte[] buf = new byte[1024];
	   String[] files = file.list();
	        
	   if (files == null) // it is a file, not a directory
	   {   
	      FileInputStream in = new FileInputStream(file.getAbsolutePath()); 

	      // add ZIP entry to output stream. 
	      //
	      try
	      {
	         // note the "relPath " used here
	         //
	         out.putNextEntry(new ZipEntry(relPath + file.getName()));

	         // transfer bytes from the file to the ZIP file 
	         //
	         int len; 

	         while ((len = in.read(buf)) > 0) 
	         { 
	             out.write(buf, 0, len); 
	         }

	         // complete the entry 
	         //
	         out.closeEntry(); 
	         in.close();
	      }
	      catch (ZipException zipE)
	      {
	         String msg = zipE.getMessage();

	         if (msg.startsWith("duplicate entry"))
	         {
	             System.out.println(file.getName() + " already exist!");
	         }
	         else
	         {
	             throw zipE;
	         }
	      }
	      finally
	      {
	         // complete the entry 
	         //
	         if (out != null) out.closeEntry(); 
	         if (in != null) in.close();
	      }
	   }
	   else if (files.length > 0) // non-empty folder
	   {
	      for (int i = 0; i < files.length; ++i)
	      {
	         // recursive call, note the relPath is dynamically built.
	         //
	         zipFile(path + "/" + files[i], out, relPath + file.getName() + "/");
	      }
	   }
	}

	
}
