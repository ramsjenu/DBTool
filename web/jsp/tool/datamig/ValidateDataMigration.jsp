<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.tcs.tools.web.dto.IdentifyPatternDTO"%>
<%@ page import="java.util.List,java.util.ArrayList,java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@taglib uri="/struts-tags" prefix="s"%>
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
	
	 
	 String msgToJsp ="";
	 if(request.getAttribute("msgToJsp") != null){
		 msgToJsp = (String)request.getAttribute("msgToJsp");
	 }
	 System.out.println("::::msgToJsp::::"+msgToJsp);
	
		String submitMode = "";
		if(request.getAttribute("submitMode") != null){
			submitMode = (String)request.getAttribute("submitMode");
		}
		
		HashMap projectNameIdMap = new HashMap();
		if(request.getAttribute("projectNameIdMap") != null){
			projectNameIdMap = (HashMap)request.getAttribute("projectNameIdMap");
		}	
		 String projectId = "";
		 if(request.getAttribute("projectId") != null){
			 projectId = (String)request.getAttribute("projectId");
		 }
		 System.out.println("::::projectId::::"+projectId);
		 
		 String tableName ="";
		 if(request.getAttribute("tableName") != null){
			 tableName = (String)request.getAttribute("tableName");
		 }
		 //tableName ="indv_statement_score_table";
	%>
<script>
	function changeProjectName(){		
		//document.form1.action='<%=filePath%>'+"/tool/validateDataMigration.action?submitMode=changeProject";
		//document.form1.submit();
	}
	
	function submitValidateDataMigration(){
		if( document.form1.projectId.value == ""){
			alert("Please select a project for data migration validation");
			document.form1.projectId.focus();
			return false;
		}
		if( document.form1.tableName.value == ""){
			alert("Please enter a Table Name to validate data migration");
			document.form1.tableName.focus();
			return false;
		}
		document.form1.buttonValidateData.href="<%=filePath%>/tool/statusDisplayAjax.action?submitMode=chk&runId="+document.form1.projectId.value;
		openStatusWindow();
		
		document.form1.action='<%=filePath%>'+"/tool/validateDataMigration.action?submitMode=validateMigration";
		document.form1.submit();
		
		//openStatusWindow();
	}
	
function openStatusWindow()	{		
		
		$(".buttonClass").colorbox({iframe:true, innerWidth:700, innerHeight:500});
		
		<%-- window.showModalDialog('<%=filePath%>'+"/tool/statusDisplayAjax.action?submitMode=init&runId="+runId,"name","dialogWidth:500px,dialogHeight:300px,scrollbars=1,resizable:0,edge:sunken,center:yes"); --%>
		//document.form1.modifyProjectBtn.disabled=true;
		//document.getElementById("modifyProjectBtn").style.display="none";
		//document.getElementById("resetButton").style.display="none"
	}
	
	function onLoadValidateDataMigration(){
		if('<%=msgToJsp%>' != ""){
			
			//window.open ("<%=filePath%>/web/jsp/tool/spmig/test.jsp?url=<%=filePath%>/tool/statusDisplayAjax.action?submitMode=finalStatusDisplay&runId=<%=projectId%>","SP_Migration_Final_Status_Report","menubar=0,resizable=0,width=850,height=600");
			window.open ("<%=filePath%>/tool/statusDisplayAjax.action?submitMode=finalStatusDisplay&runId=<%=projectId%>","SP_Migration_Final_Status_Report","menubar=0,resizable=0,width=850,height=600");
		}
	}
	</script>

</head>
<body onload="onLoadValidateDataMigration()">
	<form action="" name="form1" method="post" method="post">


		<input type="hidden" name="retMsgToJsp" id="retMsgToJsp"
			value="<%=msgToJsp %>">
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
					<table width="100%" border=0 cellspacing="2" cellpadding="2"
						valign="top">

						<tr class="pageheadernoWidth">
							<td colspan="2">&nbsp;&nbsp;&nbsp;Validate Data Migration</td>
						</tr>

						<tr>
							<td>
								<table width="100%">
									<tr>
										<td class="label1">Project Name</td>
										<td><select class="input" name="projectId" id="projectId"
											onChange="return changeProjectName();">
												<option value="">--Select--</option>
												<%-- <option <%if("PR001".equalsIgnoreCase(projectId)){out.println("selected");} %> value="PR001">Prudential</option>
					      					<option <%if("PR002".equalsIgnoreCase(projectId)){out.println("selected");} %> value="PR002">CNA</option>      --%>
												<%
					      					Iterator it = projectNameIdMap.entrySet().iterator(); 
					      					while (it.hasNext()) { 
					      					Map.Entry pairs = (Map.Entry)it.next(); 
					      					%>
												<option value="<%=pairs.getKey()%>"
													<% if(projectId.equalsIgnoreCase((String)pairs.getKey())){out.println("selected");} %>><%=pairs.getValue() %></option>
												<%
					      					//System.out.println(pairs.getKey() + " = " + pairs.getValue()); 
					      					it.remove(); // avoids a ConcurrentModificationException 
					      					}
					      					%> %>
										</select></td>
									</tr>

									<!--  <tr>
					      			<td colspan="2">&nbsp;</td>
					      		</tr> -->
									<tr>
										<td class="label1">Table Name</td>
										<td><input type="text" class="inputtext" name="tableName"
											value="<%=tableName %>" id="tableName" /></td>
									</tr>
									<tr>
										<td colspan="2">&nbsp;</td>
									</tr>


									<tr align="">
										<td>&nbsp;</td>
										<td>&nbsp;<input type="button" id="buttonValidateData"
											name="buttonValidateData" class="buttonClass"
											value="Validate"
											onclick="return submitValidateDataMigration();">
										</td>
									</tr>
									<tr>
										<td colspan="2"><font class="labelText1"><b>Note:</b>&nbsp;&nbsp;&nbsp;
												The Table name entered above is used to check for data
												migration based on Source and Target Details Entered for the
												selected project</font></td>
									</tr>

								</table>
							</td>
						</tr>






					</table> <!--  page content ends -->
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
	</form>
</body>
</html>