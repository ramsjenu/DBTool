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
<script>
var prevId="";
function submitToolApplyChnages(){
	document.form1.action='<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=applyChanges&projectId="+'<%=projectId%>';
	document.form1.submit();
}

function downloadToolApplyChnages(){
	document.form1.action='<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=downloadChanges&projectId="+'<%=projectId%>';
	document.form1.submit();
}
function changeBgColor(elementId){
	//alert(elementId+"---"+prevId);
	if(prevId.length>0){
		document.getElementById(prevId).style.backgroundColor="WHITE";
	}	
	document.getElementById(elementId).style.backgroundColor="#D2DFEF";	
	prevId=elementId;
} 	

function onloadChnageTrackerContent(){
	//alert(":::ibnside onload method::::");
	//parent.setDesignMode("off");
	
	var tracker_url="<%=filePath%>/tool/postProcessorIdentifyPattern.action?submitMode=changeTracker&linkMode=frameMode&projectId=<%=projectId%>&procName=<%=procName%>";
	//alert("tracker_url-->"+tracker_url);
	parent.chnageFrameSrc("changeTracker",tracker_url);
	hideLoadingJquery();
	
}

/************read html table column data**************/
function getTableData(){
	parent.setDesignMode("off");
	var tableName ="spCodeTable";
	var colNum ="2";
	var totalData="";
	var oTable = document.getElementById(tableName);
	var rowLength = oTable.rows.length; 
	for (var i = 0; i < rowLength; i++){	
		if(i > 0){
			totalData +="_DBT-NEW-LINE_";
		}
		
		var oCells = oTable.rows.item(i).cells;
		totalData += oCells.item(colNum-1).innerHTML; 
	}
	document.getElementById("spModifiedCode").value=totalData;
	//alert('<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=saveChanges&projectId=<%=projectId%>&procName=<%=procName%>");
	//alert("final value:::"+document.getElementById('spModifiedCode').value);
	document.form1.action='<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=saveChanges&linkMode=frameMode&projectId=<%=projectId%>&procName=<%=procName%>";
	document.form1.submit();
	
	}
	
function removeElement() {
var divNum ="spModifiedCode";
	  var d = document.getElementById('form1');

	  var olddiv = document.getElementById(divNum);

	  d.removeChild(olddiv);

	}
function createElement() { 
	//Create an input type dynamically. 
	var element = document.createElement("input"); 
	//Assign different attributes to the element. 
	element.setAttribute("type", "hidden"); 
	element.setAttribute("value", ""); 
	element.setAttribute("name", "spModifiedCode");
	element.setAttribute("id", "spModifiedCode");
	var foo = document.getElementById("form1"); 
	//Append the element in page (in span). 
	foo.appendChild(element); 
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
<script type="text/javascript" src="<%=jsPath %>/custom/common.js"></script>
<script type="text/javascript" src="<%=jsPath %>/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/jquery.blockUI.js"></script>

<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />
</head>
<body onload="onloadChnageTrackerContent()"
	style="background-color: #FFFFFF; text-color: black;">
	<form name="form1" id="form1"
		style="background-color: #FFFFFF; text-color: black;" method="post"
		action="">


		<div id="yahoo123"></div>



		<table border="1">


			<%-- <tr align="right" > <td>
	
				
	<%if("applyChanges".equals(submitMode) ||"downloadChanges".equals(submitMode)){ %>
	<div  style="display:inline;text-align:right;" align="right">
		
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		
		
		<a align="right" class="moreproject" style="cursor:hand" onclick="downloadToolApplyChnages()"  >
		<!-- <u><font align="right" color="blue" >Download File</font> </u> --></a> 
	 </div>
	<%} %>
	</td></tr> --%>


			<tr>
				<td>
					<!-- <iframe id="textEditor" ></iframe> --> <!--  <div name="register" id="register" style="font-family:Consolas;overflow:scroll;height:480px;border:1px solid;border-color:#6699FF;border-width:1px;font-size:12px;text-align:left;color:black" > -->
					<div id="spCodeFrame" name="spCodeFrame" style="display: inline;">
						<table name="spCodeTable" id="spCodeTable" width="100%" border="1"
							style="font-family: Consolas; font-size: 12px; text-align: left; color: black; border-color: black">
							<% 
	 	String lCssClass="background-color:#FFFFFF;";
	 	lCssClass="tableroweven1";
	 	if(spLines != null && spLines.length >0){
		 	for (int i = 0; i < spLines.length;i++){
		 		//System.out.println("::::spLines[i]::::"+spLines[i]);
				if( i%2 ==0){
					//lCssClass="background-color:#CCEEFF;";
					lCssClass="tablerowodd1";
				}else{
					//lCssClass="background-color:#FFFFFF;";
					lCssClass="tableroweven1";
					
				}%>
							<tr onmouseover="changeBgColor('tbRow<%=(i+1)%>')"
								id="tbRow<%=(i+1)%>"<%-- class="<%=lCssClass%>" --%>  >
								<td
									style="background-color: #ECE9D8; border-right: 1px solid; /* border-color:green; */ color: BLUE"><%=(i+1)%></td>
								<td
									style="border-left: 1px solid; /* border-color:green; */ color: #000000 /* #6B664F; */">&nbsp;&nbsp;&nbsp;<%= spLines[i]%></td>
							</tr>

							<%} 
	 	}else{%>
							<tr>
								<td colspan="2" height="440px" align="center" valign="middle"><font
									size="+1" color="red"><b>Target File Not Found </b></font></td>
							</tr>
							<%} %>
						</table>
					</div> <!--  </div>  -->

				</td>
			</tr>
			<%-- <%if(spLines != null && spLines.length >0){ %>
	<tr > <td><input class="moreproject" type= "button" value="ApplyChanges" name="ApplyChanges" onclick="submitToolApplyChnages()"/>
	<% } %> --%>


			</td>
			</tr>

		</table>

		<input type="hidden" name="spModifiedCode" id="spModifiedCode"
			value="testdata" style="display: none; visiblity: hidden;">

	</form>
</body>
</html>