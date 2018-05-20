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
	 
	 HashMap inventoryMap = new HashMap();
		if(request.getAttribute("inventoryMap") != null){
			inventoryMap = (HashMap)request.getAttribute("inventoryMap");
		}
		
		HashMap projectNameIdMap = new HashMap();
		if(request.getAttribute("projectNameIdMap") != null){
			projectNameIdMap = (HashMap)request.getAttribute("projectNameIdMap");
		}
		String projectId = "";
		if(request.getAttribute("projectId") != null){
			projectId = (String)request.getAttribute("projectId");
		}
		
		String dbName = "";
		if(request.getAttribute("dbName") != null){
			dbName = (String)request.getAttribute("dbName");
		}
		
	%>
<script>
	function downloadFile(paramValue,paramSubmitMode){
		if( paramSubmitMode == "downloadReport" || paramSubmitMode =="downloadConsolidatedReport"){
			if( document.form1.projectId.value == ""){
				alert("Please enter a project name...");
				document.form1.projectId.focus();
				return false;
			}
		}
		document.form1.action='<%=filePath%>'+"/tool/inventoryAnalytics.action?submitMode="+paramSubmitMode+"&type="+paramValue ;
		document.form1.submit();
		
	}
	
	function changeProject(){
		if( document.form1.projectId.value == ""){
			alert("Please enter a project name...");
			document.form1.projectId.focus();
			return false;
		}
		document.form1.action='<%=filePath%>'+"/tool/inventoryAnalytics.action?submitMode=changeProject";
		document.form1.submit();
		
	}
	</script>

</head>
<body>



	<form action="" name="form1" method="post">


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
					<table width="100%" border="0" cellspacing="2" cellpadding="2"
						valign="top">

						<tr class="pageheadernoWidth">
							<td colspan="2">&nbsp;&nbsp;&nbsp;Database Analytics</td>
						</tr>



						<tr>
							<td class="label1" width="25%" nowrap>Project Name</td>
							<td width="75%"><select class="input" name="projectId"
								id="projectId" onchange="return changeProject();">
									<option value="">--Select--</option>
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
		      					%>


							</select></td>
						</tr>

						<%if(!"".equals(dbName)){ %>
						<tr align="left">
							<td colspan="2" class="labelText2"><font size="-1">Database
									being used:</font>&nbsp;<font color="blue"><%=dbName %></font></td>
						</tr>
						<%} %>

						<tr>
							<td colspan="2" align="center">&nbsp;
								<table width="100%" class="subtable" cellpadding="0"
									cellspacing="2">
									<tr class="tableheader">
										<td width="5%" align="left">&nbsp;S.No</td>
										<td width="44%" align="left">&nbsp;Query Name</td>
										<td align="left">&nbsp;Query</td>
										<td align="left">&nbsp;Report</td>

									</tr>
								</table>
								<div align="center"
									style="overflow: auto; height: 270px; width: 100%;"
									class="subtable">
									<table width="97%">
										<%
										//System.out.println("::::inventoryMap:::"+inventoryMap.size());
										int i=0;
											String lCssClass="tablerowodd";
											 it = inventoryMap.entrySet().iterator(); 
					      					while (it.hasNext()) { 
					      					Map.Entry pairs = (Map.Entry)it.next(); 
					      					if( i%2 ==0){
												lCssClass="tablerowodd";
											}else{
												lCssClass="tableroweven";
											}
					      					%>
										<tr class="<%=lCssClass%>">
											<td width="5%" align="left"><%=i+1 %></td>
											<td width="45%" align="left">&nbsp;<%=pairs.getKey()%></td>
											<td>&nbsp;<a class="moreproject3" style="cursor: hand"
												onclick="downloadFile('<%=pairs.getValue()%>','download');"><u><font
														color="blue">Download Query</font></u> </a>
											</td>
											<td>&nbsp;<a class="moreproject3" style="cursor: hand"
												onclick="downloadFile('<%=pairs.getValue()%>','downloadReport');"><u><font
														color="blue">Download Report</font></u> </a>
											</td>
										</tr>

										<%
					      					//System.out.println(pairs.getKey() + " = " + pairs.getValue()); 
					      					it.remove(); // avoids a ConcurrentModificationException
					      					i++;
					      					}
					      					%>
										</tr>

									</table>
								</div>
							</td>
						</tr>


						<tr>
							<td colspan="2" align="center">&nbsp;</td>
						</tr>

						<tr>
							<td colspan="2" align="center">&nbsp; &nbsp;<a
								class="moreproject2" style="cursor: hand"
								onclick="downloadFile('','downloadConsolidatedReport');"><u><font
										color="blue">Download Consolidated Report</font></u>
							</a>
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