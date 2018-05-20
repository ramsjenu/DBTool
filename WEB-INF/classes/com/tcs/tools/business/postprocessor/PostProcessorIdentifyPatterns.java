package com.tcs.tools.business.postprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.business.postprocessor.dao.PostProcessorIdentifyPatternsDAO;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;



public class PostProcessorIdentifyPatterns {

	//private PostProcessFileUploadDAO lPostProcessFileUploadDAO = new PostProcessFileUploadDAO();
	 PostProcessorIdentifyPatternsDAO lPotProcessorIdentifyPatternsDAO =new PostProcessorIdentifyPatternsDAO();

		private static Logger logger = Logger.getLogger("ToolLogger");

	PostProcessFileUpload lPostProcessFileUpload = new PostProcessFileUpload();
	
	public String[] identifyPatterns(String pInputFilePath) throws FileNotFoundException,Exception{
		
		List lBlockStrList = lPostProcessFileUpload.getBlocksFromFile(pInputFilePath);
		logger.info(":::lBlockStrList size:::"+lBlockStrList.size());
		lBlockStrList = lPotProcessorIdentifyPatternsDAO.checkBlocks(lBlockStrList,ToolsUtil.splitFileNameAndExtension(new File(pInputFilePath).getName())[0]/*pass the proc name*/);
		logger.info(":::lBlockStrList size after:::"+lBlockStrList.size());
		String[] lNewLines=(String[])lBlockStrList.toArray(new String[lBlockStrList.size()]);
		logger.info(":::lNewLines size after:::"+lBlockStrList.size());
		/*String pOutputDirectory = "C:\\arun\\Tool Output\\post processor\\3\\";
		String lTargetFileName=new File(pInputFilePath).getName();
		lTargetFileName ="abc.html";
		ToolsUtil.writeToFile(lNewLines, pOutputDirectory+"\\"+lTargetFileName,pOutputDirectory);*/
		
		return lNewLines;
	}
	
	   
	public static void main(String [] args){
		System.out.println("Inside Main:::"+new Timestamp(System.currentTimeMillis()));
		PostProcessorIdentifyPatterns lPotProcessorIdentifyPatterns=new PostProcessorIdentifyPatterns();
		String pInputFilePath="C:\\arun\\documents\\project\\Test Run\\post_processor\\3\\New Text Document.txt";
		//lPotProcessorIdentifyPatterns.identifyPatterns(pInputFilePath);
		System.out.println("End Main:::"+new Timestamp(System.currentTimeMillis()));
	}

}
