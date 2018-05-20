package com.tcs.tools.web.util;

/*

 Extract Zip File With Subdirectories Using Command Line Argument Example.

 This Java example shows how to extract a zip file and create required 

 sub-directories using Java ZipInputStream class.

 */

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.OutputStream;

import java.util.Enumeration;

import java.util.zip.ZipEntry;

import java.util.zip.ZipFile;

import java.util.zip.ZipInputStream;

public class UnZip {

	public static void main(String args[]){
	
		String strZipFile = "C:\\arun\\app_upload\\PR001\\Source\\src.zip";
		String temp = strZipFile.substring(0, strZipFile.lastIndexOf("\\"))
				+ "\\Unzipped\\"
				+ strZipFile.substring(strZipFile.lastIndexOf("\\") + 1,
						strZipFile.length());
		System.out.println(":::temp::::" + temp);

		
		  if (strZipFile == null || strZipFile.equals("")){System.out.println("Invalid source file"); System.exit(0); }
		  UnZip lUnZip = new UnZip(); 
		  lUnZip.unzipFile(strZipFile);
		System.out.println("Zip file extracted!");
	}

	public String unzipFile(String strZipFile) {
		String zipPath = "";
		try{
			File fSourceZip = new File(strZipFile);

			zipPath = strZipFile.substring(0, strZipFile.length() - 4);
			String tempZipPath = zipPath
					.substring(0, zipPath.lastIndexOf("\\"))
					+ "\\Unzipped\\"
					+ zipPath.substring(zipPath.lastIndexOf("\\") + 1, zipPath
							.length());
			zipPath = tempZipPath;
			File temp = new File(zipPath);

			temp.mkdir();

			System.out.println(zipPath + " created");

			 // STEP 2 : Extract entries while creating required

			ZipFile zipFile = new ZipFile(fSourceZip);
			Enumeration e = zipFile.entries();
			while (e.hasMoreElements()){
				ZipEntry entry = (ZipEntry) e.nextElement();
				File destinationFilePath = new File(zipPath, entry.getName());
				// create directories if required.
				destinationFilePath.getParentFile().mkdirs();
				// if the entry is directory, leave it. Otherwise extract it.
				if (entry.isDirectory()){
					continue;
				}else{
					System.out.println("Extracting " + destinationFilePath);
					 // Get the InputStream for current entry of the zip file using InputStream getInputStream(Entry entry) method.
					BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
					int b;
					byte buffer[] = new byte[1024];
					
					 // read the current entry from the zip file, extract it and write the extracted file.

					FileOutputStream fos = new FileOutputStream(destinationFilePath);
					BufferedOutputStream bos = new BufferedOutputStream(fos,1024);
					while ((b = bis.read(buffer, 0, 1024)) != -1) {
						bos.write(buffer, 0, b);
					}

					// flush the output stream and close it.
					bos.flush();
					bos.close();
					// close the input stream.
					bis.close();
				}
			}
		}catch (IOException ioe){
			ioe.printStackTrace();
			System.out.println("IOError :" + ioe);
		}
		return zipPath;
	}
}
