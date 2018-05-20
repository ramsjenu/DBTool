package com.tcs.tools.web.action;

import java.util.Map; 
import javax.servlet.ServletContext; 
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse; 
import org.apache.struts2.interceptor.ServletRequestAware; 
import org.apache.struts2.interceptor.ServletResponseAware; 
import org.apache.struts2.interceptor.SessionAware; 
import org.apache.struts2.util.ServletContextAware; 
import com.opensymphony.xwork2.ActionSupport; 


public class BaseAction extends ActionSupport implements ServletContextAware, 
SessionAware, ServletRequestAware, ServletResponseAware { 
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected ServletContext context = null; 
protected Map<String, Object> session = null; 
protected HttpServletRequest request = null; 
protected HttpServletResponse response = null; 
public void setServletContext(ServletContext context) { 
this.context = context; 
} 


public void setServletRequest(HttpServletRequest request) { 
this.request = request; 
} 

public void setServletResponse(HttpServletResponse response) { 
this.response = response; 
}


public void setSession(Map arg0) {
	// TODO Auto-generated method stub
	
} 
}

