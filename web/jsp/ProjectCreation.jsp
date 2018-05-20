<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String filePath= request.getContextPath();
%>
<%
List targetDBTypeList = new ArrayList();
%>

<html>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>

<title>Insert title here</title>
<script>
function submitProjectCreationForm(){
	alert("inside submit method");
	document.projectCreationForm.action= '<%=filePath%>'+"/projectCreation/createProject.action";
	document.projectCreationForm.submit();
}
</script>
</head>
<body>
	<form name="projectCreationForm" id="projectCreationForm"
		enctype="multipart/form-data" method="POST">

		<table width="100%">
			<tr>
				<td><h3>Project Creation</h3></td>
			</tr>
			<tr>
				<td>
					<table border=0 width="70%">
						<tr>
							<!--  <td width="10%">Project Name</td>
						<td>
							<select name="projectName">
								<option value="" >--Select--</option>
								<option value="PRN001" >Prudential GI </option>
								<option value="PRN002" >CNA</option>
							</select> 
						</td> -->
							<td width="25%"><s:select label="Project Name" headerKey=""
									headerValue="--Select--"
									list="#{'PRN001':'Prudential GI','PRN002':'CNA'}"
									name="projectName" /></td>
						</tr>
						<tr>
							<!--  <td>Source DB Type</td>
						<td>
							<select name="sourceDBType">
								<option value="" >--Select--</option>
								<option value="SYBASE" >SYBASE</option>								
							</select> 
						</td> -->
							<td><s:select label="Source DB Type" headerKey=""
									headerValue="--Select--" list="#{'SYBASE':'SYBASE'}"
									name="sourceDBType" /></td>
						</tr>

						<tr>
							<!-- <td>Target DB Type</td>  -->
							<td><s:select label="Target DB Type" headerKey=""
									headerValue="--Select--" list="#{'DB2':'DB2'}"
									name="targetDBType" /> <!--<select name="targetDBType">
								<option value="" >--Select--</option>
								<option value="DB2" >DB2</option>								
							</select> --></td>
						</tr>
						<tr>
							<!-- <td>SP Files location </td>
						<td><input type="file"  name="fileLocation" > </td> -->
							<td><s:file name="fileLocation" label="SP Files location" /></td>
						</tr>
						<tr>
							<td colspan="2" align="center">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="2" align="left"><input type="submit"
								name="submitProjectDetails"
								onclick="return submitProjectCreationForm();" value="Submit">
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td><font color="red" size="-1"> <s:actionerror /> <s:fielderror />
				</font></td>
			</tr>

		</table>

	</form>
</body>
</html>