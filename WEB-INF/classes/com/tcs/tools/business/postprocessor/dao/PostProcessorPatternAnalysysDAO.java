package com.tcs.tools.business.postprocessor.dao;

import java.util.ArrayList;
import java.util.List;

import com.tcs.tools.business.postprocessor.dto.PostProcessorPattenDTO;

public class PostProcessorPatternAnalysysDAO {
	
	public List getPatternDetails(){
		List lPatternList = new ArrayList();
		PostProcessorPattenDTO lPostProcessorPattenDTO = new PostProcessorPattenDTO();
		lPostProcessorPattenDTO.setCategory("Conditional");
		lPostProcessorPattenDTO.setDataPattern("C(V1)");
		lPostProcessorPattenDTO.setMainConstruct("SWF_Str");
		lPostProcessorPattenDTO.setSubConstruct("(");
		lPostProcessorPattenDTO.setPrimaryPattern("SELECT");
		lPostProcessorPattenDTO.setDirectReplace("Y");
		lPostProcessorPattenDTO.setReplaceConstruct("to_char");
		lPatternList.add(lPostProcessorPattenDTO);
		
		return lPatternList;
	}

	
	public void validateBlock(String pBlockText){
		System.out.println("::::::pBlockText::::::"+pBlockText);
		List lPatternList = getPatternDetails();
		PostProcessorPattenDTO lPostProcessorPattenDTO = new PostProcessorPattenDTO();
				
				
		if(lPatternList != null && lPatternList.size() >0 ) {
			for (int i = 0; i < lPatternList.size(); i++) {
			  lPostProcessorPattenDTO =  (PostProcessorPattenDTO)lPatternList.get(i);
			 if(pBlockText.trim().toUpperCase().contains(lPostProcessorPattenDTO.getPrimaryPattern())){
				 if(lPostProcessorPattenDTO.getDirectReplace().equals("Y")){
					 //System.out.println(":::inside primary pattern::::");
					 //\\bSWF_Str\\b[\\s\\r\\n]*\\(
				 }
			 }
		 
		 
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("::::inside main::::");
		PostProcessorPatternAnalysysDAO lPostProcessorPatternAnalysysDAO = new PostProcessorPatternAnalysysDAO();
		String pBlockText="        select 'Total ' || SWF_Str(v_totalHeaderCount) || ' records on feed file'";
		lPostProcessorPatternAnalysysDAO.validateBlock(pBlockText);
	}

}
