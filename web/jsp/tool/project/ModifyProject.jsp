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
		 /* if(request.getAttribute("sourceDbIp") != null){
			 sourceDbIp = (String)request.getAttribute("sourceDbIp");
		 } */
		 if("".equals(sourceDbIp)){
			 sourceDbIp =  projectDetailsMainDTO.getSourceDbIp();
		 }
		 
		 String sourceDbPort ="";
		 /* if(request.getAttribute("sourceDbPort") != null){
			 sourceDbPort = (String)request.getAttribute("sourceDbPort");
		 } */
		 if("".equals(sourceDbPort)){
			 sourceDbPort =  projectDetailsMainDTO.getSourceDbPort();
		 }
		 
		 String sourceDbSchemaName ="";
		 /* if(request.getAttribute("sourceDbSchemaName") != null){
			 sourceDbSchemaName = (String)request.getAttribute("sourceDbSchemaName");
		 } */
		 if("".equals(sourceDbSchemaName)){
			 sourceDbSchemaName =  projectDetailsMainDTO.getSourceDbSchemaName();
		 }
		 
		 String sourceDbUserName ="";
		 /* if(request.getAttribute("sourceDbUserName") != null){
			 sourceDbUserName = (String)request.getAttribute("sourceDbUserName");
		 } */
		 if("".equals(sourceDbUserName)){
			 sourceDbUserName =  projectDetailsMainDTO.getSourceDbUserName();
		 }
		 
		 String sourceDbPassword ="";
		 /* if(request.getAttribute("sourceDbPassword") != null){
			 sourceDbPassword = (String)request.getAttribute("sourceDbPassword");
		 } */
		 if("".equals(sourceDbPassword)){
			 sourceDbPassword =  projectDetailsMainDTO.getSourceDbPassword();
		 }
		 
		 
		 //for souce unix details
		 String sourceUnixIp ="";
		 /* if(request.getAttribute("sourceUnixIp") != null){
			 sourceUnixIp = (String)request.getAttribute("sourceUnixIp");
		 } */
		 if("".equals(sourceUnixIp)){
			 sourceUnixIp =  projectDetailsMainDTO.getSourceUnixIp();
		 }
		 
		 String sourceUnixUserName ="";
		 /* if(request.getAttribute("sourceUnixUserName") != null){
			 sourceUnixUserName = (String)request.getAttribute("sourceUnixUserName");
		 } */
		 if("".equals(sourceUnixUserName)){
			 sourceUnixUserName =  projectDetailsMainDTO.getSourceUnixUserName();
		 }
		 
		 String sourceUnixPassword ="";
		 /* if(request.getAttribute("sourceUnixPassword") != null){
			 sourceUnixPassword = (String)request.getAttribute("sourceUnixPassword");
		 } */
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
			document.form1.action="<%=filePath%>/tool/modifyProject.action";
			document.form1.submit();
		} 
		
	}
	
	function resetForm(){
		var confRet = confirm("You are about to clear all the field values,Click yes to continue...  ");
		if(confRet == false){
			return false;
		}
		
		document.form1.action="<%=filePath%>/tool/modifyProject.action?submitMode=reset";
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
		//document.form1.modifyProjectBtn.disabled=false;
		if('<%=triggerSqlWays%>'=="yes"){
			document.getElementById("targetUpfileLable").style.display="none";
			document.getElementById("targetSPUpFile").style.display="none";
		}else{
			document.getElementById("targetUpfileLable").style.display="inline";
			document.getElementById("targetSPUpFile").style.display="block";
		}
		
		if((document.form1.retMsgToJsp.value != "" ) && ('<%=sourceSPUpFileFileName%>' != "")){
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
		if(document.form1.customerName.value == ""){
			alert("Please enter a customer name");
			document.form1.customerName.focus();
			return false;
		}
		if(document.form1.applicationName.value == ""){
			alert("Please enter a application name");
			document.form1.applicationName.focus();
			return false;
		}
		if(document.form1.sourceDBType.value == ""){
			alert("Please enter a source DB Type");
			document.form1.sourceDBType.focus();
			return false;
		}
		if(document.form1.targetDBType.value == ""){
			alert("Please enter a target DB Type");
			document.form1.targetDBType.focus();
			return false;
		}
		
		if(document.form1.sourceDBTypeVersion.value == ""){
			alert("Please enter a source DataBase version");
			document.form1.sourceDBTypeVersion.focus();
			return false;
		}
		if(document.form1.targetDBTypeVersion.value == ""){
			alert("Please enter a target DataBase version");
			document.form1.targetDBTypeVersion.focus();
			return false;
		}
		if(checkFileExtension_CSV(document.form1.sourceSPUpFile) == false){
			return false;
		}
		if(document.form1.targetSPUpFile.disabled == false){
			if(checkFileExtension_CSV(document.form1.targetSPUpFile) == false){
				return false;
			}
			if((document.form1.sourceSPUpFile.value == "") &&(document.form1.targetSPUpFile.value != "") ){
				alert("Pls upload source Stored Procedures in zip format");
				document.form1.sourceSPUpFile.select();
				return false;
			}
		}
		
		
		//for source db ip validation		
		if(document.form1.sourceDbIp.value != ""){			
			var ipaddrVal = document.form1.sourceDbIp.value;
			//alert("::::ret val::::"+isValidIPAddress(ipaddrVal));
			if(isValidIPAddress(ipaddrVal) == false){
				alert("Please enter a valid source DataBase IP");
				document.form1.sourceDbIp.focus();
				return false;
			}
			
			//source db port validation
			if(document.form1.sourceDbPort.value == ""){			
				alert("Please enter a valid source DataBase Port No");	
				document.form1.sourceDbPort.focus();
				return false;
			}	
			//for source db port validation		
			if(document.form1.sourceDbPort.value != ""){			
				var ipaddrVal = document.form1.sourceDbPort.value;
				//alert("::::ret val::::"+isValidIPAddress(ipaddrVal));
				if(ipaddrVal.length != 4){
					alert("Please enter a valid source DataBase Port");
					document.form1.sourceDbPort.focus();
					return false;
				}			
			}
			
			//source db user name validation
			if(document.form1.sourceDbSchemaName.value == ""){			
				alert("Please enter a valid source DataBase Name");	
				document.form1.sourceDbSchemaName.focus();
				return false;
			}
			//source db user name validation
			if(document.form1.sourceDbUserName.value == ""){			
				alert("Please enter a valid source DataBase user name");	
				document.form1.sourceDbUserName.focus();
				return false;
			}
			//source db password validation
			if(document.form1.sourceDbPassword.value == ""){			
				alert("Please enter a valid source DataBase password");	
				document.form1.sourceDbPassword.focus();
				return false;
			}
			
			//source unix user id validation
			if(document.form1.sourceUnixUserName.value == ""){			
				alert("Please enter a valid unix server user id...");	
				document.form1.sourceUnixUserName.focus();
				return false;
			}
			//source unix password validation
			if(document.form1.sourceUnixPassword.value == ""){			
				alert("Please enter a valid sunix server pasword...");	
				document.form1.sourceUnixPassword.focus();
				return false;
			}
			
			
      		
			
		}
		
		//for source db port validation		
		if(document.form1.sourceDbPort.value != ""){			
			var ipaddrVal = document.form1.sourceDbPort.value;			
			//alert("::::ret val::::"+isValidIPAddress(ipaddrVal));
			if(ipaddrVal.length != 4){
				alert("Please enter a valid source DataBase Port");
				document.form1.sourceDbPort.focus();
				return false;
			}		
		}
		
		var confRet = confirm("You are about to upload files,Click yes to continue...  ");
		if(confRet == false){
			return false;
		}else{
			//document.form1.buttonModifyProject.disabled=true;
			//document.myform.mybutton.disabled = true;

			
		}
		//document.getElementById("buttonModifyProject").disabled=true;
		document.form1.sourceDBType.disabled=false;
		document.form1.targetDBType.disabled=false;
		//document.form1.modifyProjectBtn.disabled=true;
		
		
		<%-- showLoadingJquery('<%=imagePath%>'); --%>
		//showLoading();
		
		if(document.form1.sourceSPUpFile.value == ""){
			showLoadingJquery('<%=imagePath%>');
		}else{
			openStatusWindow();
		}
		
		
		
		document.form1.action='<%=filePath%>'+"/tool/modifyProject.action?submitMode=save";
		
		document.form1.submit();
		
	}
	
	function openStatusWindow()	{		
		
		$(".buttonClass").colorbox({iframe:true, innerWidth:700, innerHeight:500});
		
		<%-- window.showModalDialog('<%=filePath%>'+"/tool/statusDisplayAjax.action?submitMode=init&runId="+runId,"name","dialogWidth:500px,dialogHeight:300px,scrollbars=1,resizable:0,edge:sunken,center:yes"); --%>
		//document.form1.modifyProjectBtn.disabled=true;
		document.getElementById("modifyProjectBtn").style.display="none";
		document.getElementById("resetButton").style.display="none"
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
						value="<%=msgToJsp %>">
					<table width="100%" border=0 cellspacing="2" cellpadding="2"
						valign="top">

						<tr class="pageheadernoWidth">
							<td colspan="2">&nbsp;&nbsp;&nbsp;Manage Migration Project:</td>
						</tr>

						<tr>
							<td colspan="2"><div id="saveResult" class="msg">
									<font color="red">&nbsp;<b><%=errorMsgToJsp %></b></font><font
										color="green">&nbsp;<s:property value="msgToJsp" /></font>
								</div></td>
						</tr>


						<tr>
							<td class="label1" width="25%" nowrap>Project Name</td>
							<td width="75%"><select class="input" name="projectName"
								id="projectName" onchange="return onChnageProjectId();">
									<option value="">--Select--</option>
									<%
		      					
		      					
		      					
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
							<td class="label1" width="25%" nowrap>Customer Name</td>
							<td width="75%"><input type="text" class="inputtext"
								name="customerName" id="customerName" value="<%=customerName%>" />
							</td>
						</tr>

						<tr>
							<td class="label1" width="25%" nowrap>Application Name</td>
							<td width="75%"><input type="text" class="inputtext"
								name="applicationName" id="applicationName"
								value="<%=applicationName%>" /></td>
						</tr>
						<tr>
							<td class="label1">Source DB Type</td>
							<td><select class="input" name="sourceDBType"
								id="sourceDBType" disabled>
									<option value="">--Select--</option>
									<option value="SYBASE"
										<%if("SYBASE".equalsIgnoreCase(sourceDBType)){out.println("selected");} %>>SYBASE</option>
							</select></td>
						</tr>
						<tr>
							<td class="label1" width="25%" nowrap>Source DBMS Version</td>
							<td width="75%"><select class="input"
								name="sourceDBTypeVersion" id="sourceDBTypeVersion">
									<option value="">--Select--</option>
									<option value="15"
										<%if("15".equalsIgnoreCase(sourceDBTypeVersion)){out.println("selected");} %>>15</option>
									<%-- 	<option value="14" <%if("14".equalsIgnoreCase(sourceDBTypeVersion)){out.println("selected");} %>>14</option>
		      					<option value="13" <%if("13".equalsIgnoreCase(sourceDBTypeVersion)){out.println("selected");} %>>13</option>  --%>
									<option value="12"
										<%if("12".equalsIgnoreCase(sourceDBTypeVersion)){out.println("selected");} %>>12</option>
									<%-- 	<option value="11" <%if("11".equalsIgnoreCase(sourceDBTypeVersion)){out.println("selected");} %>>11</option> 	
		      					<option value="10" <%if("10".equalsIgnoreCase(sourceDBTypeVersion)){out.println("selected");} %>>10</option>	 --%>
							</select></td>
						</tr>

						<tr>
							<td class="label1">Target DB Type</td>
							<td><select class="input" name="targetDBType"
								id="targetDBType" disabled>
									<option value="">--Select--</option>
									<option value="DB2"
										<%if("DB2".equalsIgnoreCase(targetDBType)){out.println("selected");} %>>DB2</option>
									<option value="SQL_SERVER"
										<%if("SQL_SERVER".equalsIgnoreCase(targetDBType)){out.println("selected");} %>>SQL
										SERVER</option>
							</select></td>
						</tr>
						<tr>
							<td class="label1" width="25%" nowrap>Target DBMS Version</td>
							<td width="75%"><select class="input"
								name="targetDBTypeVersion" id="targetDBTypeVersion">
									<option value="">--Select--</option>
									<option value="9.7"
										<%if("9.7".equalsIgnoreCase(targetDBTypeVersion)){out.println("selected");} %>>9.7</option>
									<%-- <option  value="5" <%if("5".equalsIgnoreCase(targetDBTypeVersion)){out.println("selected");} %>>5</option>      					      					
		      					<option  value="4" <%if("4".equalsIgnoreCase(targetDBTypeVersion)){out.println("selected");} %>>4</option>
		      					<option  value="3" <%if("3".equalsIgnoreCase(targetDBTypeVersion)){out.println("selected");} %>>3</option> --%>
							</select></td>
						</tr>
						<tr>
							<td class="label1">Source SP's<br>(Please upload in.zip
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
									Target SP's<br>(Please upload in.zip format)
								</div></td>
							<td><input class="input" type="file" name="targetSPUpFile"
								id="targetSPUpFile"></td>
						</tr>

						<tr>
							<td colspan="2"><hr /></td>
						</tr>
						<tr>
							<td class="label1" width="25%" nowrap>Source Database IP</td>
							<td width="75%"><input type="text" class="inputtext"
								name="sourceDbIp" id="sourceDbIp" maxLength="15"
								value="<%=sourceDbIp%>" /> &nbsp;&nbsp;&nbsp;<font
								class="labelText1">Ex: 123.123.123.123</font></td>
						</tr>
						<tr>
							<td class="label1" width="25%" nowrap>Source Database Port
								No:</td>
							<td width="75%"><input type="text" class="inputtext"
								name="sourceDbPort" id="sourceDbPort" maxLength="4"
								onkeypress="return isNumberKey(event)" value="<%=sourceDbPort%>">
								&nbsp;&nbsp;&nbsp;<font class="labelText1">Ex: 1234</font></td>
						</tr>

						<tr>
							<td class="label1" width="25%" nowrap>Source Database Name</td>
							<td width="75%"><input type="text" class="inputtext"
								name="sourceDbSchemaName" id="sourceDbSchemaName"
								value="<%=sourceDbSchemaName%>" /></td>
						</tr>
						<tr>
							<td class="label1" width="25%" nowrap>Source Database User
								Name</td>
							<td width="75%"><input type="text" class="inputtext"
								name="sourceDbUserName" id="sourceDbUserName"
								value="<%=sourceDbUserName%>" /></td>
						</tr>
						<tr>
							<td class="label1" width="25%" nowrap>Source Database
								Password</td>
							<td width="75%"><input type="password" class="inputtext"
								name="sourceDbPassword" id="sourceDbPassword"
								value="<%=sourceDbPassword%>" /></td>
						</tr>

						<!-- <tr>
		      			<td colspan="2"><hr/> </td>
		      		</tr> -->


						<%-- <tr>
		      			<td class="label1" width="25%" nowrap>Source Unix IP </td>
		      			<td width="75%" >
		      				<input type="text"  class="inputtext"  name="sourceUnixIp" id="sourceUnixIp" maxLength="15" value="<%=sourceUnixIp%>"/>	
		      				&nbsp;&nbsp;&nbsp;<font class="labelText1">Ex: 123.123.123.123</font>	      			
		      			</td> 
		      		</tr> --%>


						<tr>
							<td class="label1" width="25%" nowrap>Source Unix User Name</td>
							<td width="75%"><input type="text" class="inputtext"
								name="sourceUnixUserName" id="sourceUnixUserName"
								value="<%=sourceUnixUserName%>" /></td>
						</tr>
						<tr>
							<td class="label1" width="25%" nowrap>Source Unix Password</td>
							<td width="75%"><input type="password" class="inputtext"
								name="sourceUnixPassword" id="sourceUnixPassword"
								value="<%=sourceUnixPassword%>" /></td>
						</tr>

						<!-- <tr><td colspan="2" align="center">&nbsp;  </td> </tr> -->
						<tr>
							<td colspan="1" align="center">&nbsp;</td>
							<td colspan="1" align="left">
								<!-- <input type="button" value="run" id="demo2" > -->
								<input type="button" id="resetButton" value="Reset"
								onclick="return resetForm()">&nbsp; <input type="button"
								id="modifyProjectBtn" name="modifyProjectBtn"
								class="buttonClass"
								href="<%=filePath%>/tool/statusDisplayAjax.action?submitMode=init&runId=<%=projectName%>"
								value="Update" onclick="return submitModifyProject();">
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">&nbsp;
								<div id="lodingDiv">&nbsp;</div>
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