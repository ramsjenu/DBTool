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
	
	 String customerName = "";
	 if(request.getAttribute("customerName") != null){
		 customerName = (String)request.getAttribute("customerName");
	 }
	String applicationName = "";
	if(request.getAttribute("applicationName") != null){
		applicationName = (String)request.getAttribute("applicationName");
	}
	String sourceDBType = "";
	if(request.getAttribute("sourceDBType") != null){
		sourceDBType = (String)request.getAttribute("sourceDBType");
	}
	String sourceDBTypeVersion = "";
	if(request.getAttribute("sourceDBTypeVersion") != null){
		sourceDBTypeVersion = (String)request.getAttribute("sourceDBTypeVersion");
	}
	String targetDBType = "";
	if(request.getAttribute("targetDBType") != null){
		targetDBType = (String)request.getAttribute("targetDBType");
	}
	String targetDBTypeVersion = "";
	if(request.getAttribute("targetDBTypeVersion") != null){
		targetDBTypeVersion = (String)request.getAttribute("targetDBTypeVersion");
	}
	String projectName = "";
	if(request.getAttribute("projectName") != null){
		projectName = (String)request.getAttribute("projectName");
	}
	
	String projectId = "";
	if(request.getAttribute("projectId") != null){
		projectId = (String)request.getAttribute("projectId");
	}
	System.out.println(":::projectId in create project jsp::::"+projectId);
	
	
	//for source db data
	 String sourceDbIp ="";
	 if(request.getAttribute("sourceDbIp") != null){
		 sourceDbIp = (String)request.getAttribute("sourceDbIp");
	 }
	 
	 String sourceDbPort ="";
	 if(request.getAttribute("sourceDbPort") != null){
		 sourceDbPort = (String)request.getAttribute("sourceDbPort");
	 }
	 
	 String sourceDbSchemaName ="";
	 if(request.getAttribute("sourceDbSchemaName") != null){
		 sourceDbSchemaName = (String)request.getAttribute("sourceDbSchemaName");
	 }
	 String sourceDbUserName ="";
	 if(request.getAttribute("sourceDbUserName") != null){
		 sourceDbUserName = (String)request.getAttribute("sourceDbUserName");
	 }
	 
	 String sourceDbPassword ="";
	 if(request.getAttribute("sourceDbPassword") != null){
		 sourceDbPassword = (String)request.getAttribute("sourceDbPassword");
	 }
	 
	 
	//for target db data
		 String targetDbIp ="";
		 if(request.getAttribute("targetDbIp") != null){
			 targetDbIp = (String)request.getAttribute("targetDbIp");
		 }
		 
		 String targetDbPort ="";
		 if(request.getAttribute("targetDbPort") != null){
			 targetDbPort = (String)request.getAttribute("targetDbPort");
		 }
		 
		 String targetDbSchemaName ="";
		 if(request.getAttribute("targetDbSchemaName") != null){
			 targetDbSchemaName = (String)request.getAttribute("targetDbSchemaName");
		 }
		 
		 String targetDbName ="";
		 if(request.getAttribute("targetDbName") != null){
			 targetDbName = (String)request.getAttribute("targetDbName");
		 }
		 
		 
		 String targetDbUserName ="";
		 if(request.getAttribute("targetDbUserName") != null){
			 targetDbUserName = (String)request.getAttribute("targetDbUserName");
		 }
		 
		 String targetDbPassword ="";
		 if(request.getAttribute("targetDbPassword") != null){
			 targetDbPassword = (String)request.getAttribute("targetDbPassword");
		 }
	 
	
	%>
<script type="text/javascript" src="<%=jsPath %>/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/jquery.blockUI.js"></script>



<script type="text/javascript">
var targetDBType = new Array("DB2", "SQL_SERVER","ORACLE");
var targetDBTypeVersion = new Array();
targetDBTypeVersion["DB2"] = new Array("9.7");
targetDBTypeVersion["SQL_SERVER"] = new Array("2005","2008","2010");
targetDBTypeVersion["ORACLE"] = new Array("10g","11g");

var sourceDBType = new Array("SYBASE");
var sourceDBTypeVersion = new Array();
sourceDBTypeVersion["SYBASE"] = new Array("12","15");

/*
function resetForm2(theForm)
{   // reset source db type 
	theForm.sourceDBType.options[0] = new Option("--Select--", ""); 
	for (var i=0; i<sourceDBType.length; i++) { 
		  theForm.sourceDBType.options[i+1] = new Option(sourceDBType[i], sourceDBType[i])
	  }
	theForm.sourceDBType.options[0].selected = true;
	 // reset source db version 
	theForm.sourceDBTypeVersion.options[0] = new Option("--Select--", "");
	 theForm.sourceDBTypeVersion.options[0].selected = true;
}*/

function resetFormDD(theForm)
{  /* reset target db type */
 theForm.targetDBType.options[0] = new Option("--Select--", ""); 
 
 for (var i=0; i<targetDBType.length; i++) { 
  theForm.targetDBType.options[i+1] = new Option(targetDBType[i], targetDBType[i])
  }
 
 theForm.targetDBType.options[0].selected = true;
 /* reset target db version */
 theForm.targetDBTypeVersion.options[0] = new Option("--Select--", "");
 theForm.targetDBTypeVersion.options[0].selected = true;
 /* reset source db type */
	theForm.sourceDBType.options[0] = new Option("--Select--", ""); 
	for (var i=0; i<sourceDBType.length; i++) { 
		  theForm.sourceDBType.options[i+1] = new Option(sourceDBType[i], sourceDBType[i])
	  }
	theForm.sourceDBType.options[0].selected = true;
	 /* reset source db version */
	theForm.sourceDBTypeVersion.options[0] = new Option("--Select--", "");
	 theForm.sourceDBTypeVersion.options[0].selected = true;

 }
 function updateTargetDbVersions(theForm)
{  
	 var dbType = theForm.targetDBType.options[theForm.targetDBType.options.selectedIndex].value; 
	 var newVer = targetDBTypeVersion[dbType];
	 theForm.targetDBTypeVersion.options.length = 0;
	 theForm.targetDBTypeVersion.options[0] = new Option("--Select--", "");
     theForm.targetDBTypeVersion.disabled = false;
 
 for (var i=0; i<newVer.length; i++)
 { 
  theForm.targetDBTypeVersion.options[i+1] = new Option(newVer[i], newVer[i]);
  }
 theForm.targetDBTypeVersion.options[0].selected = true

}
 
 function updateSourceDbVersions(theForm){

 var dbTypeS = theForm.sourceDBType.options[theForm.sourceDBType.options.selectedIndex].value; 
 var newVerS = sourceDBTypeVersion[dbTypeS];
 theForm.sourceDBTypeVersion.options.length = 0;
 theForm.sourceDBTypeVersion.options[0] = new Option("--Select--", "");
 theForm.sourceDBTypeVersion.disabled = false;

for (var i=0; i<newVerS.length; i++)
{ 
 theForm.sourceDBTypeVersion.options[i+1] = new Option(newVerS[i], newVerS[i]);
 }
theForm.sourceDBTypeVersion.options[0].selected = true
 }
</script>

<script type="text/javascript">
	resetFormDD(document.form1);
	//resetForm2(document.form1);
</script>


<script type="text/javascript">
	function resetForm(){
		var confRet = confirm("You are about to clear all the field values,Click yes to continue...  ");
		if(confRet == false){
			return false;
		}
		document.form1.action="<%=filePath%>/tool/creatingProject.action?submitMode=reset";
		document.form1.submit();
	}
	function manageRepository(){
		//document.form1.action="<%=filePath%>/tool/modifyProject.action?paramProjectName="+'<%=projectId%>';
		document.form1.action="<%=filePath%>/tool/modifyProjectSPUpload.action?paramProjectName="+'<%=projectId%>';
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
	
	function onLoadCreateProject(){
		document.form1.buttonCreateProject.disabled=false;
	
		if('<%=msgToJsp%>' != ""){
			document.form1.moveToNext.style.visiblity ="visible";
			document.form1.moveToNext.style.display ="inline";
			//document.form1.modifyProjectBtn.disabled=true;
		}
		hideLoadingJquery();
		//hideLoading();
	}
	function submitModifyProject(){
		
		
		//document.getElementById("buttonModifyProject").disabled=true;
		//document.form1.modifyProjectBtn.disabled=true;
		//showLoading();
		showLoadingJquery('<%=imagePath%>');
		document.form1.action='<%=filePath%>'+"/tool/modifyProject.action";
		
		document.form1.submit();
	}
	

	function submitCreateProject(){
		
		if(document.form1.projectName.value == ""){
			alert("Please enter a project name");
			document.form1.projectName.focus();
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
				alert("Please enter a valid unix server pasword...");	
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
				if(ipaddrVal.length != 4){
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
			if(ipaddrVal.length != 4){
				alert("Please enter a valid target DataBase Port");
				document.form1.targetDbPort.focus();
				return false;
			}			
		}
		//for target db details vailation - end 
		
		
		
		var confRet = confirm("You are about to create a new project,Click yes to continue...  ");
		if(confRet == false){
			return false;
		}else{
			//document.form1.buttonModifyProject.disabled=true;
			//document.myform.mybutton.disabled = true;

			
		}
		
		document.form1.buttonCreateProject.disabled=true;
		
		showLoadingJquery('<%=imagePath%>');
		document.form1.action='<%=filePath%>'+"/tool/creatingProject.action?submitMode=save";		
		document.form1.submit();
	}
	
	
	</script>








</head>
<body onload="return onLoadCreateProject();">

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
							<td colspan="2">&nbsp;&nbsp;&nbsp;Create Migration Project:</td>
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
							<td width="75%"><input type="text" class="inputtext"
								name="projectName" value="<%=projectName %>" id="projectName" />
							</td>
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
							<td class="label1" width="25%" nowrap>Source DBMS</td>
							<td width="75%"><select class="input" name="sourceDBType"
								id="sourceDBType" onchange="updateSourceDbVersions(this.form)">
									<option value="">--Select--</option>
									<option value="SYBASE"
										<%if("SYBASE".equalsIgnoreCase(sourceDBType)){out.println("selected");} %>>SYBASE</option>

							</select></td>
						</tr>

						<tr>
							<td class="label1" width="25%" nowrap>Source DBMS Version</td>
							<td width="75%"><select class="input"
								name="sourceDBTypeVersion" id="sourceDBTypeVersion"
								disabled="disabled">
									<option value="">--Select--</option>
									
							</select></td>
						</tr>

						<tr>
							<td class="label1" width="25%" nowrap>Target DBMS</td>
							<td width="75%"><select class="input" name="targetDBType"
								id="targetDBType" onchange="updateTargetDbVersions(this.form)">
									<option value="">--Select--</option>
									<option value="DB2"
										<%if("DB2".equalsIgnoreCase(targetDBType)){out.println("selected");} %>>DB2</option>
									<option value="SQL_SERVER"
										<%if("SQL_SERVER".equalsIgnoreCase(targetDBType)){out.println("selected");} %>>SQL
										SERVER</option>
									<option value="ORACLE"
										<%if("ORACLE".equalsIgnoreCase(targetDBType)){out.println("selected");} %>>ORACLE</option>
							</select></td>
						</tr>

						<tr>
							<td class="label1" width="25%" nowrap>Target DBMS Version</td>
							<td width="75%">
								<div id="targetDBTypeVersion">
									<select class="input" name="targetDBTypeVersion"
										id="targetDBTypeVersion" disabled="disabled">
										<option value="-1">--Select--</option>
										
									</select>
								</div>
							</td>
						</tr>

						<!-- <tr>
		      			<td class="label1" width="25%" nowrap>&nbsp; </td>
		      			<td width="75%" >&nbsp;</td> 
		      		</tr>
		      		 -->

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



						<%-- 	<tr>
		      			<td colspan="2"><hr/> </td>
		      		</tr>
		      		
		      		
		      		<tr>
		      			<td class="label1" width="25%" nowrap>Source Unix IP </td>
		      			<td width="75%" >
		      				<input type="text"  class="inputtext"  name="sourceUnixIp" id="sourceUnixIp" maxLength="15" value="<%=sourceDbIp%>"/>	
		      				&nbsp;&nbsp;&nbsp;<font class="labelText1">Ex: 123.123.123.123</font>	      			
		      			</td> 
		      		</tr> --%>


						<tr>
							<td class="label1" width="25%" nowrap>Source Unix User Name</td>
							<td width="75%"><input type="text" class="inputtext"
								name="sourceUnixUserName" id="sourceUnixUserName"
								value="<%=sourceDbUserName%>" /></td>
						</tr>
						<tr>
							<td class="label1" width="25%" nowrap>Source Unix Password</td>
							<td width="75%"><input type="password" class="inputtext"
								name="sourceUnixPassword" id="sourceUnixPassword"
								value="<%=sourceDbPassword%>" /></td>
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
								name="targetDbPort" id="targetDbPort" maxLength="4"
								onkeypress="return isNumberKey(event)" value="<%=targetDbPort%>">
								&nbsp;&nbsp;&nbsp;<font class="labelText1">Ex: 1234</font></td>
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
							<td colspan="1" align="left"><input type="button"
								class="moreproject" value="Reset" onclick="return resetForm()">&nbsp;<input
								type="submit" id="buttonCreateProject"
								name="buttonCreateProject" class="moreproject"
								value="Create Project" onclick="return submitCreateProject();">
								&nbsp;<input style="display: none" type="button"
								name="moveToNext" id="moveToNext"
								value="Upload SP"
								onclick="return manageRepository();" class="moreproject" /></td>
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