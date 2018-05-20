package com.tcs.tools.business.postprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.List;

import com.tcs.tools.business.postprocessor.dao.PostProcessorApplyChangesDAO;
import com.tcs.tools.business.postprocessor.dao.PostProcessorIdentifyPatternsDAO;
import com.tcs.tools.web.util.ToolsUtil;

public class PostProcessorApplyChanges {

	//private PostProcessFileUploadDAO lPostProcessFileUploadDAO = new PostProcessFileUploadDAO();
	 PostProcessorApplyChangesDAO lPostProcessorApplyChangesDAO =new PostProcessorApplyChangesDAO();

	
	PostProcessFileUpload lPostProcessFileUpload = new PostProcessFileUpload();
	
	public String[] applyChanges(String pProjectId,String lSourceFilePath,String pTargetFilePath,String pWriteMode,String pIdentifyOrReplaceMode) throws FileNotFoundException,Exception{
		
		List lTargetBlockStrList = lPostProcessFileUpload.getBlocksFromFile(pTargetFilePath);
		//System.out.println("lSourceFilePath:::->"+lSourceFilePath);
		String [] lLines=lPostProcessFileUpload.getLines(new File(lSourceFilePath));
		/*for (int i = 0; i < lLines.length; i++) {
		System.out.println("<<<i>>:::->"+i+"::->:::"+lLines[i]);
		}*/
		List lSourceBlockStrList =ToolsUtil.arrayToList(lLines);
		
		lTargetBlockStrList = lPostProcessorApplyChangesDAO.checkBlocks(pProjectId,lSourceBlockStrList,lTargetBlockStrList,new File(pTargetFilePath).getName()/*pass the proc name*/,pWriteMode,pIdentifyOrReplaceMode);
		
		String[] lNewLines=(String[])lTargetBlockStrList.toArray(new String[lTargetBlockStrList.size()]);
		
		/*String pOutputDirectory = "C:\\arun\\Tool Output\\post processor\\3\\";
		String lTargetFileName=new File(pInputFilePath).getName();
		lTargetFileName ="abc.html";
		ToolsUtil.writeToFile(lNewLines, pOutputDirectory+"\\"+lTargetFileName,pOutputDirectory);*/
		
		return lNewLines;
	}
	
	   
	public static void main(String [] args){
		System.out.println("Inside Main:::"+new Timestamp(System.currentTimeMillis()));
		PostProcessorApplyChanges lPostProcessorApplyChanges=new PostProcessorApplyChanges();
		String pInputFilePath="C:\\arun\\documents\\project\\Test Run\\post_processor\\3\\New Text Document.txt";
		//lPostProcessorApplyChanges.applyChanges(pInputFilePath);
		System.out.println("End Main:::"+new Timestamp(System.currentTimeMillis()));
	}

}
