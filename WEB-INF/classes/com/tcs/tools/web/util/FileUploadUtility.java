
package com.tcs.tools.web.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.StringTokenizer;





/**
 * This class is used for file uploading
 * 
 * @author polaris
 * 
 */
public class FileUploadUtility {
	
	/**
	 * @param pFileName
	 * @param pFileType
	 * @return
	 * @throws Exception
	 */
	public static String validateFile(String pFileName, String pFileType){

		try {
			
			
		
			if (ToolsUtil.replaceNull(pFileType).trim().length() > 0) {
				if (pFileName.lastIndexOf(pFileType) < 0) {
					return "invalid_format";
				} else {
					return "valid_formate";
				}
			} else {
				return "invalid_format";
			}
		} 
		
		catch (Exception exception) {
			exception.printStackTrace();
			return "invalid_format";
		}
	}
	public static void main(String[] args){
		System.out.println(":::: inside main::::"+FileUploadUtility.validateFile("30.08.2011.zip", ".zip"));
		
	}
}