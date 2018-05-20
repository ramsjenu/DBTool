<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="com.tcs.tools.web.dto.FeedbackDTO,com.tcs.tools.web.util.ToolsUtil"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>

<title>:::::db TransPlant::::</title>
<%
	String filePath= request.getContextPath();
	String cssPath=filePath+"/web/css";
	String jsPath=filePath+"/web/js";
	String imagePath=filePath+"/web/images";	
%>
<%
	
ArrayList<FeedbackDTO> feedbackList = new ArrayList<FeedbackDTO>();
if(request.getAttribute("feedbackList") != null){
	feedbackList = (ArrayList)request.getAttribute("feedbackList");
}

	String errorMsgToJsp ="";
	if(request.getAttribute("errorMsgToJsp") != null){
		errorMsgToJsp = (String)request.getAttribute("errorMsgToJsp");
	}
	String msgToJsp ="";
	if(request.getAttribute("msgToJsp") != null){
		msgToJsp = (String)request.getAttribute("msgToJsp");
	}
	
	 String empName = "";
	 if(request.getAttribute("empName") != null){
		 empName = (String)request.getAttribute("empName");
	 }
	String empId = "";
	if(request.getAttribute("empId") != null){
		empId = (String)request.getAttribute("empId");
	}
	String subject = "";
	if(request.getAttribute("subject") != null){
		subject = (String)request.getAttribute("subject");
	}
	String message = "";
	if(request.getAttribute("message") != null){
		message = (String)request.getAttribute("message");
	}
	
	%>
<script type="text/javascript" src="<%=jsPath %>/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/jquery.blockUI.js"></script>

<script type="text/javascript">


	
</script>


</head>
<body>
<table width="100%" border=1 cellspacing="0" cellpadding="0"
		id="contentblank">
		<tr>
			<td colspan="3"><jsp:include
					page="/web/jsp/tool/common/header.jsp" flush="true" /></td>
		</tr>
		<tr>
			<td width="20%" valign="top"><jsp:include
					page="/web/jsp/tool/common/left_menu.jsp" flush="true" /></td>
			<td width="50%" valign="top">
<!--  page content starts -->
          <form name="form1" method="post" enctype="multipart/form-data" action="">
            <table width="100%" border=0 cellspacing="2" cellpadding="2" valign="top">
            <tr class="pageheadernoWidth">
			<td colspan="2">&nbsp;&nbsp;&nbsp;Feedbacks:</td>
			</tr>
            </table>
            <br/>
            <div>&nbsp;<a href="<%=filePath%>/tool/addingFeedback.action" class="feedback">Add Feedback</a></div>
            <br/>
            <table width="100%" class="subtable" cellspacing="0"
						cellpadding="1" border="1" valign="top">


<tr class="tableheader">

<th>Date</th>
<th>Employee Name</th>
<th>Employee Id</th>
<th>Subject</th>
<th>Message</th>
</tr>

					
<%
	
	
	
	//Iterator it = feedbackList.entrySet().iterator(); 
	//while (it.hasNext()) { 
	//Map.Entry pairs = (Map.Entry)it.next(); 
	for(int i=0;i<feedbackList.size();i++){
	%>
	<tr>
	<td>
	<%=feedbackList.get(i).getDate()%>
	</td>
	<td>
	<%=feedbackList.get(i).getEmpName()%>
	</td>
	<td>
	<%=feedbackList.get(i).getEmpId()%>
	</td>
	<td>
	<%=feedbackList.get(i).getSubject()%>
	</td>
	<td>
	<%=feedbackList.get(i).getMessage()%>
	</td>
	</tr>
	<%
	//System.out.println(pairs.getKey() + " = " + pairs.getValue()); 
	//it.remove(); // avoids a ConcurrentModificationException 
	}
			
							
	%>
	

						
  </table>
  
          </form>
        <!--  page content ends -->
			</td>
			<!-- right pane contents  -->
			<td width="25%" valign="top"><jsp:include
					page="/web/jsp/tool/common/right_menu.jsp" flush="true" /></td>
		</tr>
		<tr>
			<td colspan="5"><jsp:include
					page="/web/jsp/tool/common/footer.jsp" flush="true" /></td>
		</tr>
	</table>

</body>
</html>