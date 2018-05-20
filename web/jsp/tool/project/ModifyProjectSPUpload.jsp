<%@page import="com.tcs.tools.web.util.ToolsUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="com.tcs.tools.web.dto.ProjectDetailsMainDTO"%>
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
	HashMap projectNameIdMap = new HashMap();
	if(request.getAttribute("projectNameIdMap") != null){
		projectNameIdMap = (HashMap)request.getAttribute("projectNameIdMap");
	}
	System.out.println("::::projectNameIdMap:::"+projectNameIdMap.size());
	ProjectDetailsMainDTO projectDetailsMainDTO = new ProjectDetailsMainDTO();
	if(request.getAttribute("projectDetailsMainDTO") != null){
		projectDetailsMainDTO = (ProjectDetailsMainDTO)request.getAttribute("projectDetailsMainDTO");
	}
	
	String errorMsgToJsp ="";
	if(request.getAttribute("errorMsgToJsp") != null){
		errorMsgToJsp = (String)request.getAttribute("errorMsgToJsp");
	}
	System.out.println("::::errorMsgToJsp::::"+errorMsgToJsp);
	
	String msgToJsp ="";
	if(request.getAttribute("msgToJsp") != null){
		msgToJsp = (String)request.getAttribute("msgToJsp");
	}
	String uploadErrorMsgToJsp ="";
	if(request.getAttribute("uploadErrorMsgToJsp") != null){
		uploadErrorMsgToJsp = (String)request.getAttribute("uploadErrorMsgToJsp");
	}
	
	 String customerName = "";
	 /* if(request.getAttribute("customerName") != null){
		 customerName = (String)request.getAttribute("customerName");
	 } */
	 if("".equals(customerName)){
		customerName =  projectDetailsMainDTO.getCustomerName();
	 }
	String applicationName = "";
	/* if(request.getAttribute("applicationName") != null){
		applicationName = (String)request.getAttribute("applicationName");
	} */
	if("".equals(applicationName)){
		applicationName =  projectDetailsMainDTO.getApplicationName();
	 }
	String sourceDBType = "";
	/* if(request.getAttribute("sourceDBType") != null){
		sourceDBType = (String)request.getAttribute("sourceDBType");
	} */
	if("".equals(sourceDBType)){
		sourceDBType =  projectDetailsMainDTO.getSourceDBType();
	 }
	
	
	String sourceDBTypeVersion = "";
	/* if(request.getAttribute("sourceDBTypeVersion") != null){
		sourceDBTypeVersion = (String)request.getAttribute("sourceDBTypeVersion");
	} */
	if("".equals(sourceDBTypeVersion)){
		sourceDBTypeVersion =  projectDetailsMainDTO.getSourceDBTypeVersion();
	 }
	
	String targetDBType = "";
	/* if(request.getAttribute("targetDBType") != null){
		targetDBType = (String)request.getAttribute("targetDBType");
	} */
	if("".equals(targetDBType)){
		targetDBType =  projectDetailsMainDTO.getTargetDBType();
	 }
	
	String targetDBTypeVersion = "";
	/* if(request.getAttribute("targetDBTypeVersion") != null){
		targetDBTypeVersion = (String)request.getAttribute("targetDBTypeVersion");
	} */
	if("".equals(targetDBTypeVersion)){
		targetDBTypeVersion =  projectDetailsMainDTO.getTargetDBTypeVersion();
	 }
	
	String projectName = "";
	if(request.getAttribute("projectName") != null){
		projectName = (String)request.getAttribute("projectName");
	}
	
	System.out.println("::::projectName::::"+projectName);
	
	String triggerSqlWays ="yes";
	if(request.getAttribute("triggerSqlWays") != null){
		triggerSqlWays = (String)request.getAttribute("triggerSqlWays");
	}
	
	if("".equals(triggerSqlWays)){
		triggerSqlWays ="no";
	}
	 
	//for source db data
		 String sourceDbIp ="";
		 if(request.getAttribute("sourceDbIp") != null){
			 sourceDbIp = (String)request.getAttribute("sourceDbIp");
		 }
		 if("".equals(sourceDbIp)){
			 sourceDbIp =  projectDetailsMainDTO.getSourceDbIp();
		 }
		 
		 String sourceDbPort ="";
		 if(request.getAttribute("sourceDbPort") != null){
			 sourceDbPort = (String)request.getAttribute("sourceDbPort");
		 }
		 if("".equals(sourceDbPort)){
			 sourceDbPort =  projectDetailsMainDTO.getSourceDbPort();
		 }
		 
		 String sourceDbSchemaName ="";
		 if(request.getAttribute("sourceDbSchemaName") != null){
			 sourceDbSchemaName = (String)request.getAttribute("sourceDbSchemaName");
		 }
		 if("".equals(sourceDbSchemaName)){
			 sourceDbSchemaName =  projectDetailsMainDTO.getSourceDbSchemaName();
		 }
		 
		 String sourceDbUserName ="";
		 if(request.getAttribute("sourceDbUserName") != null){
			 sourceDbUserName = (String)request.getAttribute("sourceDbUserName");
		 }
		 if("".equals(sourceDbUserName)){
			 sourceDbUserName =  projectDetailsMainDTO.getSourceDbUserName();
		 }
		 
		 String sourceDbPassword ="";
		 if(request.getAttribute("sourceDbPassword") != null){
			 sourceDbPassword = (String)request.getAttribute("sourceDbPassword");
		 }
		 if("".equals(sourceDbPassword)){
			 sourceDbPassword =  projectDetailsMainDTO.getSourceDbPassword();
		 }
		 
		 
		 //for souce unix details
		 String sourceUnixIp ="";
		 if(request.getAttribute("sourceUnixIp") != null){
			 sourceUnixIp = (String)request.getAttribute("sourceUnixIp");
		 }
		 if("".equals(sourceUnixIp)){
			 sourceUnixIp =  projectDetailsMainDTO.getSourceUnixIp();
		 }
		 
		 String sourceUnixUserName ="";
		 if(request.getAttribute("sourceUnixUserName") != null){
			 sourceUnixUserName = (String)request.getAttribute("sourceUnixUserName");
		 }
		 if("".equals(sourceUnixUserName)){
			 sourceUnixUserName =  projectDetailsMainDTO.getSourceUnixUserName();
		 }
		 
		 String sourceUnixPassword ="";
		 if(request.getAttribute("sourceUnixPassword") != null){
			 sourceUnixPassword = (String)request.getAttribute("sourceUnixPassword");
		 }
		 if("".equals(sourceUnixPassword)){
			 sourceUnixPassword =  projectDetailsMainDTO.getSourceUnixPassword();
		 }
		 
		 
		 String sourceSPUpFileFileName ="";
		  if(request.getAttribute("sourceSPUpFileFileName") != null){
			  sourceSPUpFileFileName = (String)request.getAttribute("sourceSPUpFileFileName");
		 } 
		 
		 System.out.println(":::::sourceSPUpFileFileName::::"+sourceSPUpFileFileName);
	%>




<script type="text/javascript">
	
	function onChnageProjectId(){
		//alert(document.form1.projectName.value);
		if(document.form1.projectName.value != ""){
			document.form1.action="<%=filePath%>/tool/modifyProjectSPUpload.action";
			document.form1.submit();
		} 
		
	}
	
	function resetForm(){
		var confRet = confirm("You are about to clear all the field values,Click yes to continue...  ");
		if(confRet == false){
			return false;
		}
		
		document.form1.action="<%=filePath%>/tool/modifyProjectSPUpload.action?submitMode=reset";
		document.form1.submit();
	}
	function showLoading(){
		document.getElementById("lodingDiv").innerHTML="<img src=\"<%=imagePath%>/loading.gif\" alt=\"Loading...\"/>";
		//document.getElementById("lodingDiv").innerHTML="<font class=\"label1\" color=\"black\">Please wait while the form is being submitted...</font>";
		//alert("div inner html changed");
	}
	function hideLoading(){
		document.getElementById("lodingDiv").innerHTML="";	
		//alert("div inner html empty");
	}
	
	function onLoadModifyProject(){
		document.form1.modifyProjectBtn.disabled=false;
		if('<%=triggerSqlWays%>'=="yes"){
			document.getElementById("targetUpfileLable").style.display="none";
			document.getElementById("targetSPUpFile").style.display="none";
		}else{
			document.getElementById("targetUpfileLable").style.display="inline";
			document.getElementById("targetSPUpFile").style.display="block";
		}
		
		var msg= document.form1.retMsgToJsp.value;
		if(msg =="Error in upload process"){
			msg ="";
		}
		
		if(( msg != "" ) && (('<%=sourceSPUpFileFileName%>')!= "")){
			
			window.open ("<%=filePath%>/tool/statusDisplayAjax.action?submitMode=finalStatusDisplay&runId=<%=projectName%>","SP_Migration_Final_Status_Report","menubar=0,resizable=0,width=850,height=600");			
		<%-- window.showModalDialog('<%=filePath%>'+"/tool/statusDisplayAjax.action?submitMode=finalStatusDisplay&runId=<%=projectName%>","::SP Migration Final Status Report::","dialogWidth:800px,dialogHeight:600px,scrollbars=0,resizable:0,edge:sunken,center:yes"); --%>
		}
		
		<%-- if('<%=targetDBType%>' == "SQL_SERVER" ){
			document.form1.targetSPUpFile.disabled=true;
		} --%>
		hideLoadingJquery();
		//hideLoading();
	}
	function submitModifyProject(){
		
		
		if(document.form1.projectName.value == ""){
			alert("Please enter a project name");
			document.form1.projectName.focus();
			return false;
		} 
		
		if(checkFileExtension_CSV(document.form1.sourceSPUpFile) == false){
			return false;
		}
		if(document.form1.targetSPUpFile.disabled == false){
			if(checkFileExtension_CSV(document.form1.targetSPUpFile) == false){
				return false;
			}
			if(document.form1.sourceSPUpFile.value == ""){
				alert("Please upload source Stored Procedures in *.zip format");
				document.form1.sourceSPUpFile.select();
				return false;
			}
		}
		
		
		var confRet = confirm("You are about to upload files,Click yes to continue....  ");
		if(confRet == false){
			return false;
		}else{
			//document.form1.buttonModifyProject.disabled=true;
			//document.myform.mybutton.disabled = true;

			
		}
		//alert(":::stag1111::::");
		//document.getElementById("buttonModifyProject").disabled=true;
		//document.form1.sourceDBType.disabled=false;
		//document.form1.targetDBType.disabled=false;
		//document.form1.modifyProjectBtn.disabled=true;
		//showLoadingJquery('<%=imagePath%>');
		
		//showLoading();
		//alert("url:::::"+"/tool/modifyProjectSPUpload.action?submitMode=save");
		
		openStatusWindow();
		document.form1.action='<%=filePath%>'+"/tool/modifyProjectSPUpload.action?submitMode=save";
		document.form1.submit();
	}
	
	function openStatusWindow()	{
		$(".buttonClass").colorbox({iframe:true, innerWidth:700, innerHeight:500});
		<%-- window.showModalDialog('<%=filePath%>'+"/tool/statusDisplayAjax.action?submitMode=init&runId="+runId,"name","dialogWidth:500px,dialogHeight:300px,scrollbars=1,resizable:0,edge:sunken,center:yes"); --%>
		//document.form1.modifyProjectBtn.disabled=true;
		document.getElementById("modifyProjectBtn").style.display="none";
		document.getElementById("resetButton").style.display="none";
	}
	function checkFileExtension_CSV(upfile){
		var file = upfile.value; 
		if(file.length == 0){
			return true;
		}
		if(file.lastIndexOf(".zip") >= 0 || file.lastIndexOf(".ZIP") >= 0)
		{
			
			return true;
		}
		else{
			alert("Please upload *.zip file only");	
			upfile.select();					
			return false;
		}
	}
	
	function changeTriigerSqlWays(pParam){
	if(pParam =="yes"){
			document.getElementById("targetUpfileLable").style.display="none";
			document.getElementById("targetSPUpFile").style.display="none";
		}else{
			document.getElementById("targetUpfileLable").style.display="inline";
			document.getElementById("targetSPUpFile").style.display="block";
		}
	}
	
	</script>


</head>
<body onload="return onLoadModifyProject();">

	<table width="100%" border=1 cellspacing="0" cellpadding="0"
		id="contentblank">
		<tr>
			<td colspan="3"><jsp:include
					page="/web/jsp/tool/common/header.jsp" flush="true" /></td>
		</tr>
		<tr>
			<td width="20%" valign="top"><jsp:include
					page="/web/jsp/tool/common/left_menu.jsp" flush="true" /></td>
			<td width="60%" valign="top">
				<!--  page content starts -->
				<form name="form1" action="" method="post"
					enctype="multipart/form-data">
					<input type="hidden" name="retMsgToJsp" id="retMsgToJsp"
						value="<%=uploadErrorMsgToJsp %>">
					<table width="100%" border=0 cellspacing="2" cellpadding="2"
						valign="top">

						<tr class="pageheadernoWidth">
							<td colspan="2">&nbsp;&nbsp;&nbsp;Manage Stored Procedure
								Repository:</td>
						</tr>

						<tr>
							<td colspan="2"><div id="saveResult" class="msg">
									<font color="red">&nbsp;<s:property
											value="uploadErrorMsgToJsp" /></font><font color="green">&nbsp;<s:property
											value="msgToJsp" /></font>
								</div></td>
						</tr>


						<tr>
							<td class="label1" width="25%" nowrap>Project Name</td>
							<td width="75%"><select class="input" name="projectName"
								id="projectName" onchange="return onChnageProjectId();">
									<option value="">--Select--</option>
									<%
		      					
		      					
		      					//projectNameIdMap = ToolsUtil.getSortedMap(projectNameIdMap);
		      					Iterator it = projectNameIdMap.entrySet().iterator(); 
		      					while (it.hasNext()) { 
		      					Map.Entry pairs = (Map.Entry)it.next(); 
		      					
		      					
		      					%>
									<option value="<%=pairs.getKey()%>"
										<% if(projectName.equalsIgnoreCase((String)pairs.getKey())){out.println("selected");} %>><%=pairs.getValue() %></option>
									<%
		      					//System.out.println(pairs.getKey() + " = " + pairs.getValue()); 
		      					it.remove(); // avoids a ConcurrentModificationException 
		      					}
		      					%> %>

							</select></td>
						</tr>

						<tr>
							<td class="label1">Source SP<br>(Please upload in *.zip
								format)
							</td>
							<td><input class="input" type="file" name="sourceSPUpFile"
								id="sourceSPUpFile"></td>
						</tr>

						<tr>
							<td class="label1">Trigger Primary Tool <br>For
								Migration(Yes/No)
							</td>
							<td class="label1"><input type="radio" name="triggerSqlWays"
								onclick="return changeTriigerSqlWays(this.value)" value="none"
								<% if("none".equalsIgnoreCase(triggerSqlWays)){out.println("checked");} %>>&nbsp;
								None <input type="radio" name="triggerSqlWays"
								onclick="return changeTriigerSqlWays(this.value)" value="yes"
								<% if("yes".equalsIgnoreCase(triggerSqlWays)){out.println("checked");} %>>&nbsp;
								Yes <input type="radio" name="triggerSqlWays"
								onclick="return changeTriigerSqlWays(this.value)" value="no"
								<% if("no".equalsIgnoreCase(triggerSqlWays)){out.println("checked");} %>>
								&nbsp; No</td>
						</tr>


						<tr>
							<td class="label1"><div id="targetUpfileLable">
									Target SP<br>(Please upload in *.zip format)
								</div></td>
							<td><input class="input" type="file" name="targetSPUpFile"
								id="targetSPUpFile"></td>
						</tr>



						<!-- <tr><td colspan="2" align="center">&nbsp;  </td> </tr> -->
						<tr>
							<td colspan="1" align="center">&nbsp;</td>
							<td colspan="1" align="left">
								<!-- <input type="button" value="run" id="demo2" > -->
								<input type="submit" name="resetButton" id="resetButton"
								class="moreproject" value="Reset" onclick="return resetForm()">&nbsp;
								<input type="button" id="buttonModifyProject"
								name="modifyProjectBtn" value="Upload" class="buttonClass"
								href="<%=filePath%>/tool/statusDisplayAjax.action?submitMode=init&runId=<%=projectName%>"
								 onclick="return submitModifyProject();">
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">&nbsp;
								<div id="lodingDiv">&nbsp;</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="left">&nbsp;
								<div id="saveResult" class="msg">
									<font color="red">&nbsp;<b><%=errorMsgToJsp %></b></font>
								</div>
							</td>
						</tr>

						<tr>
							<td colspan="2" align="left" class="label1">&nbsp;&nbsp;&nbsp;<b>Screen
									Description:</b>

								<ul>
									<li>This Screen is enabled to upload Source/Target Stored
										Procedure Files in <font color="brown"><b>.zip</b></font>
										Format
									</li>
									<li>Source/Target Stored Procedures can only be in <font
										color="brown"><b>.SQL (or) .sql </b></font> format
									</li>
									<li>Tigger Primary Tool Options:
										<table>
											<tr>
												<td><font color="brown"><b>None</b></font></td>
												<td>-</td>
												<td>To Analyze Source Patterns Only</td>
											</tr>
											<tr>
												<td><font color="brown"><b>Yes</b></font></td>
												<td>-</td>
												<td>To Trigger SQLWAYS to convert the source SP to
													target SP</td>
											</tr>
											<tr>
												<td><font color="brown"><b>No</b></font></td>
												<td>-</td>
												<td>Upload Source and Target Stored Procedures(.zip
													format)</td>
											</tr>
										</table>

									</li>
								</ul>
							</td>
						</tr>

					</table>
				</form> <!--  page content ends -->
			</td>
			<!-- right pane contents  -->
			<td width="20%" valign="top"><jsp:include
					page="/web/jsp/tool/common/right_menu.jsp" flush="true" /></td>
		</tr>
		<tr>
			<td colspan="5"><jsp:include
					page="/web/jsp/tool/common/footer.jsp" flush="true" /></td>
		</tr>
	</table>

</body>
</html>