<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%>
<%@ page import="java.sql.Timestamp"%>


<html>
<%

    String filePath= request.getContextPath();
	String cssPath=filePath+"/web/css";
	String jsPath=filePath+"/web/js";
	String imagePath=filePath+"/web/images";
	
	%>

<%
	String[] spLines=null;
	if(request.getAttribute("spLines") != null){
		spLines = (String[])request.getAttribute("spLines");
	}
	
	String projectId ="";
	  if(request.getAttribute("projectId") != null){
		  projectId = (String)request.getAttribute("projectId");
	 } 
	  
	  String procName ="";
	  if(request.getAttribute("procName") != null){
		  procName = (String)request.getAttribute("procName");
	 } 
	 
	  String submitMode ="";
	  if(request.getAttribute("submitMode") != null){
		  submitMode = (String)request.getAttribute("submitMode");
	 } 
	%>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=jsPath %>/jquery.colorbox_closebutton.js"></script>
<link href="<%=cssPath%>/colorbox.css" rel="stylesheet" type="text/css" />

<!-- for light box on mosue over -->
<link rel="stylesheet" href="<%=cssPath%>/jquery.tooltip.css" />
<script src="<%=jsPath %>/lib/jquery.bgiframe.js" type="text/javascript"></script>
<script src="<%=jsPath %>/lib/jquery.dimensions.js"
	type="text/javascript"></script>
<script src="<%=jsPath %>/jquery.tooltip.js" type="text/javascript"></script>
<!-- for light box on mosue over -->
<style>
body {
	font: 12px/1.2 Verdana, sans-serif;
	padding: 0 10px;
}

a:link,a:visited {
	text-decoration: none;
	color: #416CE5;
	border-bottom: 1px solid #416CE5;
}

h2 {
	font-size: 13px;
	margin: 15px 0 0 0;
}
</style>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>

<title>::Migration Status::</title>
<%
%>
<!-- <script type="text/javascript">
$(document).ready(function(){ 
	
	//document.getElementById('textEditor').contentWindow.document.designMode="on"; 
	//document.getElementById('textEditor').contentWindow.document.close(); 
	
	

	$("#bold").click(function(){ 
	if($(this).hasClass("selected")) 
	{ 
	$(this).removeClass("selected"); 
	}else 
	{ 
	$(this).addClass("selected"); 
	} 
	boldIt(); 
	}); 
	$("#italic").click(function(){ 

	if($(this).hasClass("selected")) 
	{ 
	$(this).removeClass("selected"); 
	}else 
	{ 
	$(this).addClass("selected"); 
	} 
	ItalicIt(); 
	}); 

	}); 
	//You can go on adding different functionalities 
	function boldIt(){ 
	var edit = document.getElementById("textEditor").contentWindow; 
	edit.focus(); 
	edit.document.execCommand("bold", false, ""); 
	edit.focus(); 
	} 

	function ItalicIt(){ 
	var edit = document.getElementById("textEditor").contentWindow; 
	edit.focus(); 
	edit.document.execCommand("italic", false, ""); 
	edit.focus(); 
	} 
	
</script> -->
<script>
function submitToolApplyChnages(){
	document.form1.action='<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=applyChanges&projectId="+'<%=projectId%>';
	document.form1.submit();
}

function downloadToolApplyChnages(){
	document.form1.action='<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=downloadChanges&projectId="+'<%=projectId%>';
	document.form1.submit();
}
</script>

<!-- <script type="text/javascript">
$(function() {
$('#yahoo123 font').tooltip({
	track: true,
	delay: 0,
	showURL: false,
	showBody: " - ",
	

	fade: 250
});
});
  </script> -->

<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />
</head>
<body style="background-color: #FFFFFF; text-color: black;">
	<form name="form1"
		style="background-color: #FFFFFF; text-color: black;" method="post"
		action="">
		<input type="hidden" name="procName" id="procName"
			value="<%=procName %>">

		<div id="yahoo123">
			<center style="font-family: verdana; font-size: 14px">


				<table border="0">
					<tr class="pageheadernoWidth1" style="text-align: left">
						<td>DB Transplant-Post Processor:::</td>
					</tr>

					<tr align="left">
						<td><font class="labelText2">Procedure Name:</font>&nbsp;&nbsp;&nbsp;<font
							class="labelText2"><%=procName %></font> <%if("applyChanges".equals(submitMode) ||"downloadChanges".equals(submitMode)){ %>
							<div style="display: inline; text-align: right;" align="right">

								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;


								<a align="right" class="moreproject" style="cursor: hand"
									onclick="downloadToolApplyChnages()"> <u><font
										align="right" color="blue">Download File</font> </u></a>
							</div> <%} %></td>
					</tr>


					<tr>
						<td>
							<!-- <iframe id="textEditor" ></iframe> -->
							<div name="register" id="register"
								style="font-family: Consolas; overflow: scroll; height: 480px; width: 700px; border: 1px solid; border-color: #6699FF; border-width: 1px; font-size: 12px; text-align: left; color: black">
								<table width="100%" border="0">
									<% 
	 	String lCssClass="background-color:#FFFFFF;";
	 	lCssClass="tableroweven1";
	 	if(spLines != null && spLines.length >0){
		 	for (int i = 0; i < spLines.length;i++){
				if( i%2 ==0){
					//lCssClass="background-color:#CCEEFF;";
					lCssClass="tablerowodd1";
				}else{
					//lCssClass="background-color:#FFFFFF;";
					lCssClass="tableroweven1";
				}%>
									<tr <%-- style="<%=lCssClass%>" --%> class="<%=lCssClass%>">
										<td
											style="border-right: 1px solid; border-color: green; color: BLUE"><%=(i+1)%></td>
										<td style="border-left: 1px solid; border-color: green">&nbsp;&nbsp;&nbsp;<%= spLines[i]%></td>
									</tr>

									<%} 
	 	}else{%>
									<tr>
										<td colspan="2" height="440px" align="center" valign="middle"><font
											size="+1" color="red"><b>Target File Not Found </b></font></td>
									</tr>
									<%} %>
								</table>
							</div>

						</td>
					</tr>
					<%if(spLines != null && spLines.length >0){ %>
					<tr>
						<td><input class="moreproject" type="button"
							value="ApplyChanges" name="ApplyChanges"
							onclick="submitToolApplyChnages()" /> <% } %></td>
					</tr>

				</table>
			</center>


		</div>
	</form>
</body>
</html>