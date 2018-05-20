import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcs.tools.business.constant.ToolConstant;
import com.tcs.tools.web.dto.StoredProceduresDetailsDTO;
import com.tcs.tools.web.util.RGX;
import com.tcs.tools.web.util.ToolsUtil;


public class Test {

	public static void changeTargetFileTypes(String pSourcePath,String pTargetPath){
		 	
		StoredProceduresDetailsDTO lSourceSPDetailsDTO=null;
		StoredProceduresDetailsDTO lTargetSPDetailsDTO=null;
		
		String lSourceFileName="";
		String lSourceFileType="";
		
		String lTargetFileName="";
		String lTargetFileType="";
		
		List lSourceFileNamesLst =ToolsUtil.getFileNamesFromFolderDTO(new File(pSourcePath),new ArrayList());
		List lTargetFileNamesLst =ToolsUtil.getFileNamesFromFolderDTO(new File(pTargetPath),new ArrayList());
		
		for (int i = 0; i < lSourceFileNamesLst.size(); i++) {
			lSourceSPDetailsDTO = (StoredProceduresDetailsDTO)lSourceFileNamesLst.get(i);
			lSourceFileName=lSourceSPDetailsDTO.getProcName().substring(0,lSourceSPDetailsDTO.getProcName().lastIndexOf("."));
			lSourceFileType=lSourceSPDetailsDTO.getProcName().substring(lSourceSPDetailsDTO.getProcName().lastIndexOf("."),lSourceSPDetailsDTO.getProcName().length());
			for (int j = 0; j < lTargetFileNamesLst.size(); j++) {
				lTargetSPDetailsDTO=(StoredProceduresDetailsDTO)lTargetFileNamesLst.get(j);
				lTargetFileName=lTargetSPDetailsDTO.getProcName().substring(0,lTargetSPDetailsDTO.getProcName().lastIndexOf("."));
				lTargetFileType=lTargetSPDetailsDTO.getProcName().substring(lTargetSPDetailsDTO.getProcName().lastIndexOf("."),lTargetSPDetailsDTO.getProcName().length());
				if(lSourceFileName.trim().equalsIgnoreCase(lTargetFileName.trim())){
					File lTargetFile=new File(lTargetSPDetailsDTO.getFolderPath()+"\\"+lTargetFileName.trim()+lTargetFileType.trim());
					boolean lChkRename=lTargetFile.renameTo(new File(lTargetSPDetailsDTO.getFolderPath()+"\\"+lTargetFileName.trim()+lSourceFileType.trim()));					
					/*System.out.println(lSourceFileName+":::->"+lTargetFileName);
					System.out.println("Before::->"+lTargetSPDetailsDTO.getFolderPath()+"\\"+lTargetFileName.trim()+lTargetFileType.trim());
					System.out.println("After::->"+lTargetSPDetailsDTO.getFolderPath()+"\\"+lTargetFileName.trim()+lSourceFileType.trim());
					System.out.println("Chk::->"+lChkRename);*/
				}
			}
		}
		return ;
	}
	public static String checkAndReplace(String pBlockToCheck,String pFindPattern,String pReplacementPattern,String pReplacementText,int pGroupCount){
		String lTempReplacement=pReplacementText;
		pFindPattern ="(?i)"+pFindPattern;
		 Pattern lStrFunctionPattern = Pattern.compile(pFindPattern);
		 Matcher lMatcher=null;
		 lMatcher=lStrFunctionPattern.matcher(pBlockToCheck);
		 StringBuffer sb= new StringBuffer();
		 System.out.println(pFindPattern);
		 while(lMatcher.find()){
			 
			 System.out.println(":::::::before:::"+pBlockToCheck);
			 //"(?i)if[\\r\\n\\s]*\\((.+?)\\![\\r\\n\\s]*="
 			if(pGroupCount > 0 ){
	    			for (int i = 1; i <= pGroupCount; i++) {
	    				pReplacementText = pReplacementText.replaceAll(ToolConstant.TOOL_DELIMT_GROUP_SPLIT+i,lMatcher.group(1).trim());
	    				pBlockToCheck=pBlockToCheck.substring(0,lMatcher.start())+pReplacementText+pBlockToCheck.substring(lMatcher.end(),pBlockToCheck.length());
					}
 			}else if(pGroupCount == 0 ){
 				
 				pBlockToCheck=pBlockToCheck.substring(0,lMatcher.start())+pReplacementText+pBlockToCheck.substring(lMatcher.end(),pBlockToCheck.length());
 			}else if(pGroupCount == -1 ){
 				System.out.println("Group 0:::"+lMatcher.group(0).trim());
 				pReplacementText =lMatcher.group(0).trim()+ pReplacementText;
				pBlockToCheck=pBlockToCheck.substring(0,lMatcher.start())+pReplacementText+pBlockToCheck.substring(lMatcher.end(),pBlockToCheck.length()); 				
 			}else if(pGroupCount == -2 ){
 				System.out.println("Group 0:::"+lMatcher.group(0).trim());
 				if(pBlockToCheck.trim().endsWith(";")){
 					pBlockToCheck=pBlockToCheck.substring(0,pBlockToCheck.lastIndexOf(";"));
 					pBlockToCheck=pBlockToCheck+pReplacementText+" ; ";
 				}
 				
 			}else if(pGroupCount == -3 ){
 				//add Replacement Text after the end of the replacement regex and replace after group 0 position.
 				Matcher lReplaceMatcher=Pattern.compile(pReplacementPattern).matcher(pBlockToCheck);
 				if(lReplaceMatcher.find()){
 					pBlockToCheck=pBlockToCheck.substring(0,lReplaceMatcher.end())+pReplacementText+pBlockToCheck.substring(lReplaceMatcher.end(),pBlockToCheck.length());
 					
 				}
 			}else if(pGroupCount == -4 ){
 				//Take some text from regex and add Replacement Text to that n replace it at the group 1  position.
 				Matcher lReplaceMatcher=Pattern.compile(pReplacementPattern).matcher(pBlockToCheck);
 				String lTextFromRgx="";
 				if(lReplaceMatcher.find()){
 					lTextFromRgx=lReplaceMatcher.group(1);
 					pReplacementText=lTextFromRgx+pReplacementText;
 					pBlockToCheck=pBlockToCheck.substring(0,lReplaceMatcher.start(1))+pReplacementText+pBlockToCheck.substring(lReplaceMatcher.start(1),pBlockToCheck.length()); 					
 				}
 			}
 			
 			
 			System.out.println(":::::::after:::"+pBlockToCheck);
	    		
 			pReplacementText=lTempReplacement;
	    	//lMatcher=lStrFunctionPattern.matcher(pBlockToCheck);
		 }
		
    	
      return pBlockToCheck;
	}
	public static void main(String [] Files){
		/*String lSourcePath="C:\\Praveen\\Tool Data\\inputs\\Source 1\\";
		String lTargetPath="C:\\Praveen\\Tool Data\\inputs\\Target 1\\";*/
		/*String pFindPattern =RGX.B_STR("CREATE")+RGX.SRN+RGX.ONE_OR_MANY+RGX.B_STR("INDEX")+RGX.SRN+RGX.ONE_OR_MANY+"((?!\\bSESSION"+RGX.SRN+RGX.ZERO_OR_MANY+"\\.).)+"+RGX.TEXT+RGX.ONE_OR_MANY+"\\bSESSION.TT\\_";
		Pattern lChkPat=Pattern.compile("(?i)"+pFindPattern);
		System.out.println("Started:::");
		String input="CREATE INDEX ClosedIntervention_IX1  ON session.tt_ClosedIntervention";
		Matcher lm = lChkPat.matcher(input);
		if(lm.find()){
			System.out.println("Found"+lm.group(0));
		}
		input = checkAndReplace(input,pFindPattern,"(?i)\\bCREATE\\b[\\s\\r\\n]+\\bINDEX\\b[\\s\\r\\n]+",  "Session.", -3);
		System.out.println(input);*/
		String pFindPattern ="(?i)if[\\r\\n\\s]*\\([\\r\\n\\s\\w_]*\\![\\r\\n\\s]*=";
		Pattern lChkPat=Pattern.compile("(?i)"+pFindPattern);
		System.out.println("Started:::");
		String input="IF(V_VAR1 !=Y)";
		Matcher lm = lChkPat.matcher(input);
		if(lm.find()){
			System.out.println("Found"+lm.group(0));
		}
		input = checkAndReplace(input,pFindPattern,"(?i)if[\\r\\n\\s]*\\((.+?)\\![\\r\\n\\s]*=",  " IS NULL  OR ", -4);
		System.out.println(input);
		
		//"(?i)if[\\r\\n\\s]*\\([\\r\\n\\s\\w_]*\\![\\r\\n\\s]*="
//CREATE INDEX session.ClosedIntervention_IX1  ON session.tt_ClosedIntervention
		
		
	}
}
