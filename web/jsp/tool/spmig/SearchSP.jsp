<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.tcs.tools.web.dto.IdentifyPatternDTO"%>
<%@ page import="java.util.List,java.util.ArrayList,java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@taglib uri="/struts-tags" prefix="s"%>
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


IdentifyPatternDTO lIdentifyPatternDTO = new IdentifyPatternDTO();
List procNameList = new ArrayList();
if(request.getAttribute("procNameList") != null){
	procNameList = (List)request.getAttribute("procNameList");
}

 String projectId = "";
 if(request.getAttribute("projectId") != null){
	 projectId = (String)request.getAttribute("projectId");
 }
 System.out.println("::::projectId::::"+projectId);
 
 
 String analysisType = "";
 if(request.getAttribute("analysisType") != null){
	 analysisType = (String)request.getAttribute("analysisType");
 }
 
 String msgToJsp ="";
 if(request.getAttribute("msgToJsp") != null){
	 msgToJsp = (String)request.getAttribute("msgToJsp");
 }
 
 String seqNo="";
 if(request.getAttribute("seqNo") != null){
	 seqNo = (String)request.getAttribute("seqNo");
 }
 String searchedString = "";
	if(request.getAttribute("searchedString") != null){
		searchedString = (String)request.getAttribute("searchedString");
	}
	String searchedString2 = "";
	if(request.getAttribute("searchedString2") != null){
		searchedString2 = (String)request.getAttribute("searchedString2");
	}
%>
<script>
var lMatchedSPCount=0;

function openPopUpReport(){
	if(document.getElementById("searchedString")!=null && document.getElementById("searchedString")!=" "){
	window.open ("<%=filePath%>/tool/spMigrationReports.action?submitMode=SearchReport&projectId=<%=projectId%>&seqNo=<%=seqNo%>&searchedString=<%=searchedString%>","detailedSummaryReport","scrollbars=1,menubar=0,resizable=1,width=850,height=500");
	}
	if(document.getElementById("searchedString2")!=null && document.getElementById("searchedString2")!=" "){
	window.open ("<%=filePath%>/tool/spMigrationReports.action?submitMode=SearchReportString&projectId=<%=projectId%>&seqNo=<%=seqNo%>&searchedString2=<%=searchedString2%>","detailedSummaryReport2","scrollbars=1,menubar=0,resizable=1,width=850,height=500");
	}
	}





function submitIdentifyPattern(){
	if(document.form1.projectId.value == ""){
		alert("Please select project name.");
		document.form1.projectId.focus();
		return false;
	} 
	
	/*if(document.form1.analysisType.value == ""){
		alert("Please enter a pattern to analyse gaps.");
		document.form1.analysisType.focus();
		return false;
	}*/
	
	
	document.form1.submitMode.value = "search";
	document.form1.action='<%=filePath%>'+"/tool/SPSearch.action?";
	document.form1.submit();
	
}


function resetForm(){
	var confRet = confirm("You are about to clear all the field values,Click yes to continue...  ");
	if(confRet == false){
		return false;
	}
	document.form1.submitMode.value = "reset";
	document.form1.action='<%=filePath%>'+"/tool/SPSearch.action?";
	document.form1.submit();
}

function changeProjectName(){
	document.form1.submitMode.value = "changeProject";
	document.form1.action='<%=filePath%>'+"/tool/SPSearch.action?";
	document.form1.submit();
} 
function onLoadIdentifyMode(){
	
	if(document.form1.retMsgToJsp.value != ""){
		document.getElementById("report1_link").style.visibility = "visible";
		document.getElementById("report1_link").style.display = "inline";
	}
	
}

</script>

</head>
<body onload="return onLoadIdentifyMode(); ">


	<form action="" name="form1" method="post">
		<input type="hidden" name="submitMode" id="submitMode" value="">
		<input type="hidden" name="retMsgToJsp"
			id="retMsgToJsp" value="<%=msgToJsp %>">
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
							<td colspan="2">&nbsp;&nbsp;&nbsp;Search Stored Procedure</td>
						</tr>

						<tr>
							<td colspan="2"><div id="saveResult" class="msg">
									<font color="green">&nbsp;<s:property value="msgToJsp" /></font>
								</div></td>
						</tr>
						<tr>
							<td class="label1" width="25%" nowrap>Project Name</td>
							<td width="75%"><select class="input" name="projectId" id="projectId"
								onChange="return changeProjectName();">
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
		      					%> %>
							</select></td>
						</tr>

						<tr>
							<td class="label1" width="25%" nowrap>Search Keyword</td>
							<td width="75%"><input type="text" class="inputtext" name="searchedString" id="searchedString" value="<%=searchedString%>"></td>
							
							
						</tr>
						<tr>
						<td colspan="2" >
                 			 <font class="ShortDescription">[Enter multiple keywords using <b>comma ','</b> as separator]</font>
               				</td>
						</tr>

						<tr>
							<td class="label1" width="25%" nowrap>Search String</td>
							<td width="75%"><input type="text" class="inputtext" name="searchedString2" id="searchedString2" value=<%=searchedString2%>></td>
							
							
						</tr>


						<tr>
							
							<td colspan="1" align="left">
							 <input type="reset" class="moreproject" id="resetfields" name="resetfields" value="Reset" onclick="return resetForm()"/>&nbsp;
							<input type="button"
								class="moreproject" value="Search" id="pageSubmit"
								onclick="return submitIdentifyPattern();" >
								</td>
						</tr>
						<tr>
							<td colspan="2" align="center">&nbsp;</td>
						</tr>

						<tr>
							<td colspan="2" align="center"><div id="report1_link"
									style="visiblity: hidden; display: none;">
									&nbsp; <a class="moreproject2" style="cursor: hand"
										onclick="openPopUpReport();"><u><font color="blue">Search Result</font></u> </a> </br> </br>


								</div></td>
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

</body>

</html>