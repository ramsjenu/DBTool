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
	
	
	 
	//for source db data
		 String sourceDbIp ="";
		/*  if(request.getAttribute("sourceDbIp") != null){
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
		/*  if(request.getAttribute("sourceDbSchemaName") != null){
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
		/*  if(request.getAttribute("sourceUnixIp") != null){
			 sourceUnixIp = (String)request.getAttribute("sourceUnixIp");
		 } */
		 if("".equals(sourceUnixIp)){
			 sourceUnixIp =  projectDetailsMainDTO.getSourceUnixIp();
		 }
		 
		 String sourceUnixUserName ="";
		/*  if(request.getAttribute("sourceUnixUserName") != null){
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
		 
		 
		 
		 
		 
		//for target db data
			String targetDbIp = projectDetailsMainDTO.getTargetDbIp();
			String targetDbPort = projectDetailsMainDTO.getTargetDbPort();
			String	  targetDbSchemaName =  projectDetailsMainDTO.getTargetDbSchemaName();
			String	  targetDbName =  projectDetailsMainDTO.getTargetDbName();
			String	 targetDbUserName =  projectDetailsMainDTO.getTargetDbUserName();
			String	 targetDbPassword =  projectDetailsMainDTO.getTargetDbPassword();
			String	 targetUnixIp =  projectDetailsMainDTO.getTargetUnixIp();
			String	 targetUnixUserName =  projectDetailsMainDTO.getTargetUnixUserName();
			String	 targetUnixPassword =  projectDetailsMainDTO.getTargetUnixPassword();
	%>




<script type="text/javascript">
	
	function onChnageProjectId(){
		//alert(document.form1.projectName.value);
		if(document.form1.projectName.value != ""){
			document.form1.action="<%=filePath%>/tool/modifyProjectDetails.action";
			document.form1.submit();
		} 
		
	}
	
	function resetForm(){
		var confRet = confirm("You are about to clear all the field values,Click yes to continue...  ");
		if(confRet == false){
			return false;
		}
		
		document.form1.action="<%=filePath%>/tool/modifyProjectDetails.action?submitMode=reset";
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
		
		
		//for targert db details validation - start
		//for target db ip validation		
		if(document.form1.targetDbIp.value != ""){			
			var ipaddrVal = document.form1.targetDbIp.value;
			//alert("::::ret val::::"+isValidIPAddress(ipaddrVal));
			if(isValidIPAddress(ipaddrVal) == false){
				alert("Please enter a valid target DataBase IP");
				document.form1.targetDbIp.focus();
				return false;
			}
			
			//target db port validation
			if(document.form1.targetDbPort.value == ""){			
				alert("Please enter a valid target DataBase Port No");	
				document.form1.targetDbPort.focus();
				return false;
			}	
			//for target db port validation		
			if(document.form1.targetDbPort.value != ""){			
				var ipaddrVal = document.form1.targetDbPort.value;
				//alert("::::ret val::::"+isValidIPAddress(ipaddrVal));
				if(ipaddrVal.length != 5){
					alert("Please enter a valid target DataBase Port");
					document.form1.targetDbPort.focus();
					return false;
				}			
			}
			
			//target db user name validation
			if(document.form1.targetDbSchemaName.value == ""){			
				alert("Please enter a valid target DataBase Schema Name");	
				document.form1.targetDbSchemaName.focus();
				return false;
			}
			//target db user name validation
			if(document.form1.targetDbName.value == ""){			
				alert("Please enter a valid target DataBase Name");	
				document.form1.targetDbName.focus();
				return false;
			}
			//target db user name validation
			if(document.form1.targetDbUserName.value == ""){			
				alert("Please enter a valid target DataBase user name");	
				document.form1.targetDbUserName.focus();
				return false;
			}
			//target db password validation
			if(document.form1.targetDbPassword.value == ""){			
				alert("Please enter a valid target DataBase password");	
				document.form1.targetDbPassword.focus();
				return false;
			}
			
			
			//target unix user id validation
			if(document.form1.targetUnixUserName.value == ""){			
				alert("Please enter a valid unix server user id...");	
				document.form1.targetUnixUserName.focus();
				return false;
			}
			//target unix password validation
			if(document.form1.targetUnixPassword.value == ""){			
				alert("Please enter a valid sunix server pasword...");	
				document.form1.targetUnixPassword.focus();
				return false;
			}
			
		}
		
		//for target db port validation		
		if(document.form1.targetDbPort.value != ""){			
			var ipaddrVal = document.form1.targetDbPort.value;			
			//alert("::::ret val::::"+isValidIPAddress(ipaddrVal));
			if(ipaddrVal.length != 5){
				alert("Please enter a valid target DataBase Port");
				document.form1.targetDbPort.focus();
				return false;
			}			
		}
		//for target db details vailation - end 
		
		
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
		document.form1.modifyProjectBtn.disabled=true;
		
		
		showLoadingJquery('<%=imagePath%>');
		//showLoading();
		
		
		
		
		
		document.form1.action='<%=filePath%>'+"/tool/modifyProjectDetails.action?submitMode=saveProjDetails";
		
		document.form1.submit();
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
					<table width="100%" border=0 cellspacing="2" cellpadding="2"
						valign="top">

						<tr class="pageheadernoWidth">
							<td colspan="2">&nbsp;&nbsp;&nbsp;Manage Migration Project
								Details:</td>
						</tr>

						<tr>
							<td colspan="2"><div id="saveResult" class="msg">
									<font color="red">&nbsp;<s:property
											value="errorMsgToJsp" /></font><font color="green">&nbsp;<s:property
											value="msgToJsp" /></font>
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

									<option value="<%=sourceDBType%>"
										<%if("SYBASE".equalsIgnoreCase(sourceDBType)){out.println("selected");} %>><%=sourceDBType%></option>

							</select></td>
						</tr>
						<tr>
							<td class="label1" width="25%" nowrap>Source DBMS Version</td>
							<td width="75%"><select class="input"
								name="sourceDBTypeVersion" id="sourceDBTypeVersion">
									<option value="">--Select--</option>

									<%if("SYBASE".equalsIgnoreCase(sourceDBType)){%>
									<option value="12"
										<%if("12".equalsIgnoreCase(sourceDBTypeVersion)){out.println("selected");} %>>12</option>
									<option value="15"
										<%if("15".equalsIgnoreCase(sourceDBTypeVersion)){out.println("selected");} %>>15</option>
									<%} %>

							</select></td>
						</tr>

						<tr>
							<td class="label1">Target DB Type</td>
							<td><select class="input" name="targetDBType"
								id="targetDBType" disabled>
									<option value="">--Select--</option>
									<option value="<%=targetDBType%>"
										<%if("DB2".equalsIgnoreCase(targetDBType)){out.println("selected");} %>
										<%if("SQL_SERVER".equalsIgnoreCase(targetDBType)){out.println("selected");} %>
										<%if("ORACLE".equalsIgnoreCase(targetDBType)){out.println("selected");} %>>
										<%if("SQL_SERVER".equalsIgnoreCase(targetDBType)){%>SQL SERVER<%}else {%><%=targetDBType%>
										<%}%>
									</option>

							</select></td>
						</tr>
						<tr>
							<td class="label1" width="25%" nowrap>Target DBMS Version</td>
							<td width="75%"><select class="input"
								name="targetDBTypeVersion" id="targetDBTypeVersion">
									<option value="">--Select--</option>

									<%if("DB2".equalsIgnoreCase(targetDBType)){%>
									<option value="9.7"
										<%if("9.7".equalsIgnoreCase(targetDBTypeVersion)){out.println("selected");} %>>9.7</option>
									<%} %>
									<%if("SQL_SERVER".equalsIgnoreCase(targetDBType)){%>
									<option value="2005"
										<%if("2005".equalsIgnoreCase(targetDBTypeVersion)){out.println("selected");} %>>2005</option>
									<option value="2008"
										<%if("2008".equalsIgnoreCase(targetDBTypeVersion)){out.println("selected");} %>>2008</option>
									<option value="2010"
										<%if("2010".equalsIgnoreCase(targetDBTypeVersion)){out.println("selected");} %>>2010</option>
									<%} %>
									<%if("ORACLE".equalsIgnoreCase(targetDBType)){%>
									<option value="10g"
										<%if("10g".equalsIgnoreCase(targetDBTypeVersion)){out.println("selected");} %>>10g</option>
									<option value="11g"
										<%if("11g".equalsIgnoreCase(targetDBTypeVersion)){out.println("selected");} %>>11g</option>
									<%} %>
							</select></td>
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


						<!-- for target db connection detials -->

						<tr>
							<td colspan="2"><hr /></td>
						</tr>

						<tr>
							<td class="label1" width="25%" nowrap>Target Database IP</td>
							<td width="75%"><input type="text" class="inputtext"
								name="targetDbIp" id="targetDbIp" maxLength="15"
								value="<%=targetDbIp%>" /> &nbsp;&nbsp;&nbsp;<font
								class="labelText1">Ex: 123.123.123.123</font></td>
						</tr>

						<tr>
							<td class="label1" width="25%" nowrap>Target Database Port
								No:</td>
							<td width="75%"><input type="text" class="inputtext"
								name="targetDbPort" id="targetDbPort" maxLength="5"
								onkeypress="return isNumberKey(event)" value="<%=targetDbPort%>">
								&nbsp;&nbsp;&nbsp;<font class="labelText1">Ex: 12345</font></td>
						</tr>

						<tr>
							<td class="label1" width="25%" nowrap>Target Database Name</td>
							<td width="75%"><input type="text" class="inputtext"
								name="targetDbName" id="targetDbName" value="<%=targetDbName%>" />
							</td>
						</tr>
						<tr>
							<td class="label1" width="25%" nowrap>Target Database Schema
								Name</td>
							<td width="75%"><input type="text" class="inputtext"
								name="targetDbSchemaName" id="targetDbSchemaName"
								value="<%=targetDbSchemaName%>" /></td>
						</tr>


						<tr>
							<td class="label1" width="25%" nowrap>Target Database User
								Name</td>
							<td width="75%"><input type="text" class="inputtext"
								name="targetDbUserName" id="targetDbUserName"
								value="<%=targetDbUserName%>" /></td>
						</tr>
						<tr>
							<td class="label1" width="25%" nowrap>Target Database
								Password</td>
							<td width="75%"><input type="password" class="inputtext"
								name="targetDbPassword" id="targetDbPassword"
								value="<%=targetDbPassword%>" /></td>
						</tr>

						<tr>
							<td class="label1" width="25%" nowrap>Target Unix User Name</td>
							<td width="75%"><input type="text" class="inputtext"
								name="targetUnixUserName" id="targetUnixUserName"
								value="<%=targetDbUserName%>" /></td>
						</tr>
						<tr>
							<td class="label1" width="25%" nowrap>Target Unix Password</td>
							<td width="75%"><input type="password" class="inputtext"
								name="targetUnixPassword" id="targetUnixPassword"
								value="<%=targetDbPassword%>" /></td>
						</tr>


						<!-- <tr><td colspan="2" align="center">&nbsp;  </td> </tr> -->
						<tr>
							<td colspan="1" align="center">&nbsp;</td>
							<td colspan="1" align="left">
								<!-- <input type="button" value="run" id="demo2" > -->
								<input type="button" class="moreproject" value="Reset"
								onclick="return resetForm()">&nbsp;<input type="submit"
								id="buttonModifyProject" name="modifyProjectBtn"
								class="moreproject" value="Update Project"
								onclick="return submitModifyProject();">
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