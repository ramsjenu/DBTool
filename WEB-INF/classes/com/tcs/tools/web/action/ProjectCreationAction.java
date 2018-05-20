package com.tcs.tools.web.action;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.web.dao.ProjectCreationDAO;
import com.tcs.tools.web.util.ToolsUtil;
import com.tcs.tools.web.util.UnZip;

public class ProjectCreationAction extends ActionSupport /*implements ServletRequestAware,ServletResponseAware */ {
	// private static final long serialVersionUID = 1L;   
	// private HttpServletRequest request;
	//  private HttpServletResponse response;
	  ProjectCreationDAO lProjectCreationDAO = new ProjectCreationDAO();
	  UnZip lUnZip = new UnZip(); 
	   /* private Map<String, Object> session;  
	    private Map<String, Object> request;*/
	  private File fileLocation ;
	  private String fileLocationFileName; //The uploaded file name

	
	  private String errorContents="testnew";
	  
	  public void setErrorContents(final String edit) {
		  errorContents = edit;
		  }

	  
	  
	public void setFileLocationFileName(String fileLocationFileName) {
		this.fileLocationFileName = fileLocationFileName;
	}
	/**
	 * @return the fileLocation
	 */
	public File getFileLocation() {
		return fileLocation;
	}
	/**
	 * @param fileLocation the fileLocation to set
	 */
	public void setFileLocation(File fileLocation) {
		this.fileLocation = fileLocation;
	}
	
	/**
	 * @return the fileLocationFileName
	 */
	public String getFileLocationFileName() {
		return fileLocationFileName;
	}
	/**
	 * @param fileLocationFileName the fileLocationFileName to set
	 */
	public String execute() throws Exception {
	        //setMessage(getText(MESSAGE));
		 //request.get
		// request=(HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);  
		  
		System.out.println(":::::::INSIDE EXECUTE METHOD::::");
		//session = ActionContext.getContext().getSession();
		System.out.println(":::::inside the execute method:::::::-project name"+getProjectName());
		lProjectCreationDAO.insertProjectDetails(getProjectName(), getSourceDBType(), getTargetDBType(), "", null);
		System.out.println(":::::files started copying to server::::;"+fileLocationFileName);
		//System.out.println(":::: from prop file:::::"+ToolsUtil.readProperty("fileUploadPath"));
		String fullFileName =ToolsUtil.readProperty("fileUploadPath")+fileLocationFileName;
		File theFile = new File(fullFileName);

		FileUtils.copyFile(fileLocation, theFile);
		lUnZip.unzipFile( fullFileName);
		System.out.println(":::::files ended copying to server::::;"+fileLocationFileName);
		 
	        return SUCCESS;
	    }
	
	public void validate() {		
		/*if (getTargetDBType().length() == 0) {			       
			addFieldError("targetDBType", "User Name is required");
		} */
	}

		
			/*public void setServletRequest(HttpServletRequest request){
		  this.request = request;
		  }

		  public HttpServletRequest getServletRequest(){
		  return request;
		  }

		  public void setServletResponse(HttpServletResponse response){
		  this.response = response;
		  }

		  public HttpServletResponse getServletResponse(){
		  return response;
		  }*/

	    /**
	     * Provide default valuie for Message property.
	     */
	    private String projectName ="";
	    private String sourceDBType ="";
	    private String targetDBType ="";
	   
		/**
		 * @return the projectName
		 */
		public String getProjectName() {
			return projectName;
		}
		/**
		 * @param projectName the projectName to set
		 */
		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}
		/**
		 * @return the sourceDBType
		 */
		public String getSourceDBType() {
			return sourceDBType;
		}
		/**
		 * @param sourceDBType the sourceDBType to set
		 */
		public void setSourceDBType(String sourceDBType) {
			this.sourceDBType = sourceDBType;
		}
		/**
		 * @return the targetDBType
		 */
		public String getTargetDBType() {
			return targetDBType;
		}
		/**
		 * @param targetDBType the targetDBType to set
		 */
		public void setTargetDBType(String targetDBType1) {
			this.targetDBType = targetDBType;
		}
		
	    

	    
	    
	    

}
