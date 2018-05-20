package com.tcs.tools.web.action;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.business.postprocessor.PostProcessorApplyChanges;
import com.tcs.tools.business.postprocessor.PostProcessorIdentifyPatterns;
import com.tcs.tools.business.postprocessor.dao.PostProcessorApplyChangesDAO;
import com.tcs.tools.web.dao.ProjectModifyDAO;
import com.tcs.tools.web.dto.StoredProceduresDetailsDTO;
import com.tcs.tools.web.util.FileUploadDownloadUtility;
import com.tcs.tools.web.util.ToolsUtil;

public class PostProcessorAction extends ActionSupport {
	private String msgToJsp="";
	private String submitMode="";
	
	private String[] spLines=null;
	private HashMap projectNameIdMap = null;
	private List procNameList ;
	private String projectId ="";
	private List projectSpDetailsList ;
	private String procName="";
	private String linkMode ="";
	private String spModifiedCode ="";
	
	private List trackerDetailsList ;
	
	PostProcessorIdentifyPatterns lPotProcessorIdentifyPatterns= new PostProcessorIdentifyPatterns();
	PostProcessorApplyChanges lPostProcessorApplyChanges = new PostProcessorApplyChanges();
	PostProcessorApplyChangesDAO lPostProcessorApplyChangesDAO = new PostProcessorApplyChangesDAO();
	ProjectModifyDAO lProjectCreationDAO = new ProjectModifyDAO();
	private static Logger logger = Logger.getLogger("ToolLogger");

	public String execute() throws Exception {
		
		System.out.println(":::::inside PostProcessorAction :::::"+getSubmitMode());
		String pSourceRunId =getProjectId()+"_SOURCE";
		String pTargetRunId =getProjectId()+"_TARGET";
		logger.info(":::inside PostProcessorAction - submitMode->"+getSubmitMode()+"---projectId--->"+getProjectId()+"---proc name--->"+getProcName());
		if("changeTracker".equalsIgnoreCase(getSubmitMode())){
			logger.info(":::inside - changeTracker submit mode:::::::");
			setTrackerDetailsList(lPostProcessorApplyChangesDAO.getChangeTrackerDetails(getProjectId(),getProcName()));
			return "changeTracker";
		}
		if("view".equalsIgnoreCase(getSubmitMode())){
			HashMap lProjectNameIdMap = lProjectCreationDAO.getProjectDetails(); 
			setProjectNameIdMap(lProjectNameIdMap);
			return SUCCESS;
		}
		if("openMainTool".equalsIgnoreCase(getSubmitMode())){
			/*HashMap lProjectNameIdMap = lProjectCreationDAO.getProjectDetails(); 
			setProjectNameIdMap(lProjectNameIdMap);
			
			List  lSpNameList = ToolsUtil.getFileNamesFromTableDTO( pSourceRunId);
			setProjectSpDetailsList(lSpNameList);*/
			System.out.println(":::::inside openMainTool  submit mode:::::"+getSubmitMode());
			return "openMainTool";
		}
		if("projectExlporer".equalsIgnoreCase(getSubmitMode())){
			HashMap lProjectNameIdMap = lProjectCreationDAO.getProjectDetails(); 
			setProjectNameIdMap(lProjectNameIdMap);
			System.out.println(":::::inside openMainTool  submit mode:::::"+getSubmitMode());
			List  lSpNameList = ToolsUtil.getFileNamesFromTableDTO( pSourceRunId);
			setProjectSpDetailsList(lSpNameList);
			return "projectExlporer";
		}		
		if("changeProject".equalsIgnoreCase(getSubmitMode())){
			HashMap lProjectNameIdMap = lProjectCreationDAO.getProjectDetails(); 
			setProjectNameIdMap(lProjectNameIdMap);
			System.out.println(":::::inside changeProject  submit mode:::::");
			//get the sp details for the selected project
			
			List  lSpNameList = ToolsUtil.getFileNamesFromTableDTO(pSourceRunId);
			setProjectSpDetailsList(lSpNameList);
			
			//get the sp details
			return SUCCESS;
		}
		
		
		/*if("enterPatterns".equalsIgnoreCase(getSubmitMode())){
			System.out.println(":::::inside enter pattern submit mode:::::");
			//invoking post processor identify actions
			//pTargetRunId =getProjectId()+"_TARGET";
			
			StoredProceduresDetailsDTO lTargetSPDetailsDTO =  ToolsUtil.getSPDiskLocation( pTargetRunId,getProcName());
			String lTargetSpFileLocation=lTargetSPDetailsDTO.getFolderPath()+"\\"+lTargetSPDetailsDTO.getProcName();
			logger.info("lTargetSpFileLocation:::::::::::"+lTargetSpFileLocation);
			System.out.println("lTargetSpFileLocation:::::::::::"+lTargetSpFileLocation);
			
			try{
			String[] lines  =  lPotProcessorIdentifyPatterns.identifyPatterns(lTargetSpFileLocation);
			logger.info("::::lines size::::"+lines.length);
			setSpLines(ToolsUtil.splitBlocksToArray(lines));
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
			return "enterPatterns";
		}*/
		
		if("applyChanges".equalsIgnoreCase(getSubmitMode())
				||  "downloadChanges".equalsIgnoreCase(getSubmitMode())
				||  "saveChanges".equalsIgnoreCase(getSubmitMode())
				||  "enterPatterns".equalsIgnoreCase(getSubmitMode())){
			
			
			
			//pTargetRunId =getProjectId()+"_TARGET";
			logger.info("::::::apply changes :::: submit mode started::::");
			logger.info(":::::::runid::::"+pTargetRunId);
			logger.info("::::::proc name::::"+getProcName());
			logger.info("::::::getLinkMode::::"+getLinkMode());
			
			
			StoredProceduresDetailsDTO lSourceSPDetailsDTO =  ToolsUtil.getSPDiskLocation( pSourceRunId,getProcName());
			StoredProceduresDetailsDTO lTargetSPDetailsDTO =  ToolsUtil.getSPDiskLocation( pTargetRunId,getProcName());
			
			String lSourceSpFileLocation=lSourceSPDetailsDTO.getFolderPath()+"\\"+lSourceSPDetailsDTO.getProcName();
			String lTargetSpFileLocation=lTargetSPDetailsDTO.getFolderPath()+"\\"+lTargetSPDetailsDTO.getProcName();
			
			logger.info("lSpFileLocation:::::::::::"+lTargetSpFileLocation);
			System.out.println("lSpFileLocation:::::::::::"+lTargetSpFileLocation);
			
			//-------------------------------------apply chnages----------------------------------------------------------
			String lWriteMode ="WRITE_TO_JSP";
			if("downloadChanges".equalsIgnoreCase(getSubmitMode())){
				lWriteMode ="WRITE_TO_FILE";
			}
			String pIdentifyOrReplaceMode="REPLACE_PATTERN";
			if("enterPatterns".equalsIgnoreCase(getSubmitMode())){
				pIdentifyOrReplaceMode ="IDENTIFY_PATTERN";
			}
			String[] lines=null;
			String lOutputFolder =ToolsUtil.readProperty("fileUploadPath")+getProjectId()+ToolsUtil.readProperty("targetFileModifiedPath");
			
			if( "saveChanges".equalsIgnoreCase(getSubmitMode())){
				logger.info(":::file from front end:::::"+ToolsUtil.removeHtmlTags(getSpModifiedCode()));
				 lines = ToolsUtil.removeHtmlTags(getSpModifiedCode()).split("_DBT-NEW-LINE_");
				System.out.println("::::::downloadChanges::::;"+getProjectId()+"----"+getProcName());
				ToolsUtil.writeToFile(lines, lOutputFolder+getProcName(), lOutputFolder);
				ToolsUtil.writeToFile(lines, lTargetSPDetailsDTO.getFolderPath()+"\\"+lTargetSPDetailsDTO.getProcName(), lTargetSPDetailsDTO.getFolderPath()+"\\");
				
				//	return "enterPatternsFrameMode";
			}
			
						
			 lines = lPostProcessorApplyChanges.applyChanges(getProjectId(),lSourceSpFileLocation,lTargetSpFileLocation,lWriteMode,pIdentifyOrReplaceMode);		
			logger.info(":::::sp output folder::::"+lOutputFolder);
			
			
			
			if( "downloadChanges".equalsIgnoreCase(getSubmitMode())){
				System.out.println("::::::downloadChanges::::;"+getProjectId()+"----"+getProcName());
				ToolsUtil.writeToFile(lines, lOutputFolder+lTargetSPDetailsDTO.getProcName(), lOutputFolder);
				String lOutputFile =ToolsUtil.readProperty("fileUploadPath")+getProjectId()+ToolsUtil.readProperty("targetFileModifiedPath")+getProcName();
				FileUploadDownloadUtility.downloadFile(lOutputFile, ServletActionContext.getResponse());
				return null;
			}
			
			//------------------------------------apply changes------------------------------------------------------
			System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
			//invoking post processor identify actions
			
			//lPotProcessorIdentifyPatterns.intiateFilParsing( lProjectId, lRootFolderPath, lRootFolderPath, lConnection);
			
			//temp commented identify patterns - start
			//lines = null;
			//lines  =  lPotProcessorIdentifyPatterns.identifyinPatterns(lOutputFolder+lTargetSPDetailsDTO.getProcName());
			//temp commented identify patterns - end
			
			
			/* for (int i = 0; i < lines.length; i++) {
				 System.out.println(lines[i]);
					lines[i]=lines[i].replaceAll("\n", "<br/>");
				}*/
			setSpLines(ToolsUtil.splitBlocksToArray(lines));
			
			if("frameMode".equalsIgnoreCase(getLinkMode())){
				System.out.println(":::::::::::::::::::::::::::::::::::::enterPatternsFrameMode:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
				return "enterPatternsFrameMode";
			}
			
			
			return "enterPatterns";
		}
		
		
		
		
		return SUCCESS;
	}

	public String getMsgToJsp() {
		return msgToJsp;
	}

	public void setMsgToJsp(String msgToJsp) {
		this.msgToJsp = msgToJsp;
	}

	public String getSubmitMode() {
		return submitMode;
	}

	public void setSubmitMode(String submitMode) {
		this.submitMode = submitMode;
	}

	public String[] getSpLines() {
		return spLines;
	}

	public void setSpLines(String[] spLines) {
		this.spLines = spLines;
	}

	public HashMap getProjectNameIdMap() {
		return projectNameIdMap;
	}

	public void setProjectNameIdMap(HashMap projectNameIdMap) {
		this.projectNameIdMap = projectNameIdMap;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public List getProjectSpDetailsList() {
		return projectSpDetailsList;
	}

	public void setProjectSpDetailsList(List projectSpDetailsList) {
		this.projectSpDetailsList = projectSpDetailsList;
	}

	public String getProcName() {
		return procName;
	}

	public void setProcName(String procName) {
		this.procName = procName;
	}

	/**
	 * @return the linkMode
	 */
	public String getLinkMode() {
		return linkMode;
	}

	/**
	 * @param linkMode the linkMode to set
	 */
	public void setLinkMode(String linkMode) {
		this.linkMode = linkMode;
	}

	/**
	 * @return the trackerDetailsList
	 */
	public List getTrackerDetailsList() {
		return trackerDetailsList;
	}

	/**
	 * @param trackerDetailsList the trackerDetailsList to set
	 */
	public void setTrackerDetailsList(List trackerDetailsList) {
		this.trackerDetailsList = trackerDetailsList;
	}

	/**
	 * @return the spModifiedCode
	 */
	public String getSpModifiedCode() {
		return spModifiedCode;
	}

	/**
	 * @param spModifiedCode the spModifiedCode to set
	 */
	public void setSpModifiedCode(String spModifiedCode) {
		this.spModifiedCode = spModifiedCode;
	}
	
}
