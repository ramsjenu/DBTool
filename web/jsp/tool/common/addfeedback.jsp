<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

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
	function resetForm(){
		var confRet = confirm("You are about to clear all the field values,Click yes to continue...  ");
		if(confRet == false){
			return false;
		}
		document.form1.action="<%=filePath%>/tool/addingFeedback.action?submitMode=reset";
		document.form1.submit();
	}
	
	function isNumber(strString)
	   //  check for valid numeric strings	
	   {
	   var strValidChars = "0123456789";
	   var strChar;
	   var blnResult = true;

	   if (strString.length == 0) return false;

	   //  test strString consists of valid characters listed above
	   for (i = 0; i < strString.length && blnResult == true; i++)
	     {
	      strChar = strString.charAt(i);
	      if (strValidChars.indexOf(strChar) == -1)
	         {
	         blnResult = false;
	         }
	     }
	   
	   return blnResult;
	   }
	
	function validateUsername(fld) {
	    var error = "";
	    var illegalChars = /\W\d/; // allow letters, numbers, and underscores
	 
	    if (fld.value == "") {
	        fld.style.background = 'Yellow'; 
	        error = "Please enter name.\n";
	    } else if ((fld.value.length < 5) || (fld.value.length > 15)) {
	        fld.style.background = 'Yellow'; 
	        error = "Please enter valid name.\n";
	    } else if (illegalChars.test(fld.value)) {
	        fld.style.background = 'Yellow'; 
	        error = "Please enter valid name.\n";
	    } else {
	        fld.style.background = '#ead28b';
	    } 
	    return error;
	}
	
	


	function submitFeedback(){
		 var illegalChars = /\W|\d/; // allow letters and underscores
		
		 if(document.form1.empName.value == ""){
			alert("Please enter name");
			document.form1.empName.focus();
			return false;
		} 
		if ((document.form1.empName.value.length < 4) || (document.form1.empName.value.length > 50)) 
		{
			alert("Please valid enter name");
			document.form1.empName.focus();
			return false; 
		     
	    } 
		
		 if (illegalChars.test(document.form1.empName.value)==true) {
		      
			alert("Please enter valid name");
			document.form1.empName.focus();
			return false;
		}
		
		if(document.form1.empId.value == ""){
			alert("Please enter employee Id");
			document.form1.empId.focus();
			return false;
		}
		
		if(isNumber(document.form1.empId.value)==false)
		{
			alert("Please enter valid employee id");
			document.form1.empId.focus();
			return false;
			
		}
		if ((document.form1.empId.value.length < 4) || (document.form1.empId.value.length > 15)) 
		{
			alert("Please enter valid employee id");
			document.form1.empId.focus();
			return false; 
		     
	    } 
		
		if(document.form1.message.value == ""){
			alert("Please enter your valuable feedback");
			document.form1.message.focus();
			return false;
		}
		
		
		var confRet = confirm("Click yes to continue...  ");
		if(confRet == false){
			return false;
		}
		
		document.form1.submitfeedback.disabled=true;
		
		showLoadingJquery('<%=imagePath%>');
		document.form1.action='<%=filePath%>'+"/tool/addingFeedback.action?submitMode=save";		
		
		document.form1.submit();
		
		
	}
	
	
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
          <form name="form1" method="post" enctype="multipart/form-data" action="" >
            <table width="100%" border=0 cellspacing="2" cellpadding="2" valign="top">
              
              <tr class="pageheadernoWidth">
							<td colspan="2">&nbsp;&nbsp;&nbsp;Post your Message:</td>
						</tr>
              <tr>
							<td colspan="2"><div id="saveResult" class="msg">
									<font color="red">&nbsp;<s:property
											value="errorMsgToJsp" /></font><font color="green">&nbsp;<s:property
											value="msgToJsp" /></font>
								</div></td>
			  </tr>
              <tr>
                <td class="label1" width="25%" nowrap>Name<font class="Star">*</font>
                </td>
                
                 <td width="75%"><input type="text" class="inputtext" id="empName" name="empName" value="<%=empName%>" autocomplete="off"/>
                </td>
              </tr>
              <tr>
                <td class="label1" width="25%" nowrap>Employee Id<font class="Star">*</font>
                </td>
                
               
                  <td width="75%"><input type="text" class="inputtext" name="empId" id="empId" value="<%=empId%>" autocomplete="off"/>
                </td>
                
              </tr>
              <tr>
                <td class="label1" width="25%" nowrap>Subject 
                </td>
                
                
                 <td width="75%"><input type="text" class="inputtext" id="subject" name="subject" maxlength="45" value="<%=subject%>"  autocomplete="off"/>
                </td>
              </tr>
              <tr>
                <td class="label1" width="25%" nowrap>Message<font class="Star">*</font> 
                </td>
                
                <td width="75%">
                  <textarea cols="20" rows="5" class="inputtextarea" id="message" name="message"></textarea>
                </td>
              </tr>
              <tr>
                <td colspan="2" >
                  <font class="ShortDescription">[ <font class="Star">*</font> fields are mandatory]</font>
                </td>
              </tr>
              <tr>
                
               <td colspan="1" align="center">&nbsp;</td>
							<td colspan="1" align="left">
                 
                  <input type="reset" class="moreproject" id="resetfields" name="resetfields" value="Reset" onclick="return resetForm()"/>&nbsp;
                   <input type="button" class="moreproject" value="Submit" id="submitfeedback" name="submitfeedback" onclick="return submitFeedback()"/>
                   
                </td>
               
              </tr>
              <tr>
							<td colspan="2" align="center">&nbsp;
								<div id="lodingDiv">&nbsp;</div>
							</td>
						</tr>
            </table>
            <DIV align="right"><a href="<%=filePath%>/tool/addingFeedback.action?submitMode=viewFeedback" class="feedback" >View Feedbacks</a>&nbsp;&nbsp;&nbsp;</DIV>
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