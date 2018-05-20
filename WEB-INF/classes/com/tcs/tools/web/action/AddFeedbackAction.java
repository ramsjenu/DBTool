package com.tcs.tools.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.tcs.tools.web.dao.AddFeedbackDAO;
import com.tcs.tools.web.dto.FeedbackDTO;
import com.tcs.tools.web.util.ToolsUtil;

public class AddFeedbackAction extends ActionSupport{
	
AddFeedbackDAO addfeedback=new AddFeedbackDAO();
private static Logger logger = Logger.getLogger("ToolLogger");

private String submitMode = "";
private String subject = "";
private String msgToJsp = "";
private String errorMsgToJsp = "";
private StringBuffer msgValue= new StringBuffer(); 
private String empName = "";
private String empId = "";
private String message = "";
private ArrayList feedbackList=null;


public String execute() throws Exception {
	
	if("reset".equalsIgnoreCase(getSubmitMode())){
	System.out.println("::::inside feedback reset mode");
   	 setEmpName("");
   	 setEmpId("");
   	 setSubject("");
   	 setMessage("");
   	 
   	 return SUCCESS;
    }
	if("save".equalsIgnoreCase(getSubmitMode())){
   	 System.out.println("::::inside feedback save mode");
   	 
   	 
   	 
   	 // String msg=getMessage();
   	 // int msgLen=msg.length();
   	 // if(msgLen<=150){
   	//	  setErrorMsgToJsp("Please enter atleast 150 characters.");
   	 // }
   	//  else{
   	  
   	    HashMap feedbackMap = new HashMap();
 			
 			feedbackMap.put("EMP_NAME", getEmpName());			
 			feedbackMap.put("EMP_ID", getEmpId());
 			feedbackMap.put("SUBJECT", getSubject());
 			feedbackMap.put("MESSAGE", getMessage());
 			
 			
 			//logger.debug("::::empid::::;"+feedbackMap.get("EMP_ID"));
 			
 			
 			int feedbackAdditionChk=addfeedback.insertFeedbackDetails(feedbackMap) ;
 			if(feedbackAdditionChk>0)
			{
			//for file type validation - end
				msgValue.append("Thank You.");
				msgValue.append("\n");
				msgValue.append("Your feedback successfully saved on ");
				msgValue.append(ToolsUtil.getDateTime());
				setMsgToJsp(msgValue.toString());
				setEmpName("");
			   	setEmpId("");
			   	setSubject("");
			   	setMessage("");
				
			}
			else
			{
				msgValue.append("Unable to save data.Sorry for the inconvenience. ");
				msgValue.append("Please check your database connection");
				setErrorMsgToJsp(msgValue.toString());	
			}
   	  }
   // }
	
	
	//System.out.println(feedbackList.size());
	 if("viewFeedback".equalsIgnoreCase(getSubmitMode())){
		 
		 feedbackList=addfeedback.viewFeedback(); 
		 return  "viewFeedback";
	 }
       return SUCCESS;
	
	
}




/******************************************getters and setters************************/

public String getSubject() {
	return subject;
}
public void setSubject(String subject) {
	this.subject = subject;
}
public String getMsgToJsp() {
	return msgToJsp;
}
public void setMsgToJsp(String msgToJsp) {
	this.msgToJsp = msgToJsp;
}
public String getErrorMsgToJsp() {
	return errorMsgToJsp;
}
public void setErrorMsgToJsp(String errorMsgToJsp) {
	this.errorMsgToJsp = errorMsgToJsp;
}
public StringBuffer getMsgValue() {
	return msgValue;
}
public void setMsgValue(StringBuffer msgValue) {
	this.msgValue = msgValue;
}
public String getEmpName() {
	return empName;
}
public void setEmpName(String empName) {
	this.empName = empName;
}
public String getEmpId() {
	return empId;
}
public void setEmpId(String empId) {
	this.empId = empId;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}




public String getSubmitMode() {
	return submitMode;
}




public void setSubmitMode(String submitMode) {
	this.submitMode = submitMode;
}




public ArrayList getFeedbackList() {
	return feedbackList;
}




public void setFeedbackList(ArrayList feedbackList) {
	this.feedbackList = feedbackList;
}



}
