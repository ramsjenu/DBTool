<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="com.tcs.tools.web.util.ToolsUtil"%>


<html>
<%

    String filePath= request.getContextPath();
	String cssPath=filePath+"/web/css";
	String jsPath=filePath+"/web/js";
	String imagePath=filePath+"/web/images";
	
	%>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/jquery.colorbox.js"></script>


<link href="<%=cssPath%>/colorbox.css" rel="stylesheet" type="text/css" />
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
	String submitMode ="";
	if(request.getAttribute("submitMode") != null){
		submitMode = (String)request.getAttribute("submitMode");
	}
	System.out.println("::::submitMode::::"+submitMode);
	String runId ="";
	if(request.getAttribute("runId") != null){
		runId = (String)request.getAttribute("runId");
	}
	System.out.println("::::Runid::::"+runId);
	String refreshMode ="";
	if(request.getAttribute("refreshMode") != null){
		refreshMode = (String)request.getAttribute("refreshMode");
	}
	System.out.println("::::refreshMode::::"+refreshMode);
	
	String completeStatusText ="";
	if(request.getAttribute("completeStatusText") != null){
		completeStatusText = (String)request.getAttribute("completeStatusText");
	}
	System.out.println("::::refreshMode::::"+completeStatusText);
%>
<script type="text/javascript">
	var req;
	var stopProcess;
	<% if( submitMode.equalsIgnoreCase("init")){
		%>
		initProcess();
		<%
	}%>
	function initProcess(){
		stopProcess=setInterval("register()",1000);
		//register();
	}
	function register()
	{
	
	
	if (window.XMLHttpRequest) 
	{
	req = new XMLHttpRequest( );
	
	}
	else if (window.ActiveXObject) 
	{
	req = new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	var url = '<%=filePath%>'+"/tool/statusDisplayAjax.action?submitMode=ajax&runId=<%=runId%>";
	req.onreadystatechange = processStateChange;
	req.open("POST", url, true);
	req.send(null);
	}
	
	function processStateChange()
	{
	//alert("method called::::");
	if (req.readyState==4) 
	{
	if (req.status == 200) 
	{
	var responseText = req.responseText;	
	if(responseText.search(/\bProcess\s+\bCompleted/i)>0){
		//alert("Process Completed");
		clearInterval(stopProcess);
		<%-- document.form1.completeStatusText.value=responseText;
		document.form1.action='<%=filePath%>'+"/tool/statusDisplayAjax.action?submitMode=reload";
		document.form1.submit(); --%>
		
	}
	//alert(time);
	var returnedText=req.responseText;
	var msgText=returnedText.split("_DBT_DELIM_")[0];
	var curState="<b>Current State:</b> "+returnedText.split("_DBT_DELIM_")[1];
	//alert("curState--->"+curState);
	
	//document.getElementById("register").innerHTML = req.responseText;
	document.getElementById("register").innerHTML = msgText;
	document.getElementById("curState").innerHTML = curState;
	document.getElementById("register").scrollTop=2*document.getElementById("register").scrollHeight;
	<%-- showLoadingJqueryWithText('<%=imagePath%>',req.responseText); --%>
	//alert(document.body.scrollHeight);
	//window.scrollBy(0,document.body.scrollHeight);
	
	}
	}
	}
	function openStatusWindow()	{
		
		
		window.showModalDialog('<%=filePath%>'+"/tool/statusDisplayAjax.action?","name","dialogWidth:500px,dialogHeight:300px,scrollbars=0,resizable:0,edge:sunken,center:yes");
		
	}
	function popUpColorBox( pElementClassName){
		alert("ada");
		/* alert("ada");
		document.form1.submit(); */
		
		  $("."+pElementClassName).colorbox({iframe:true, innerWidth:600, innerHeight:344});  
	}
	function setStatusText(){
		<%if(!"".equals(completeStatusText.trim())){
			%>
			document.getElementById("register").innerHTML = '<%=completeStatusText.split("\\s*_DBT_DELIM_\\s*")[0]%>'+"";
			<%
		}%>
	}
	function closeWindow(){
		//window.opener='self';
		window.close();
	}
	
</script>
<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />
</head>
<body onload="setStatusText()">
	<form name="form1" method="post" action="">

		<center style="font-family: verdana; font-size: 14px">


			<table>
				<tr class="pageheadernoWidth" style="text-align: left">
					<td>:::Current Process Status:::</td>
				</tr>
				<tr>
					<td class="label1" style="text-align: right">Started At:::: <%= (ToolsUtil.getDateTime())%></td>
				</tr>
				<% if("".equals(completeStatusText.trim())) {
		%>
				<tr>
					<td><label name="curState" style="display: inline"
						id="curState" class="label1">::::Process Started::::</label></td>
				</tr>
				<%} %>
				<tr>
					<td>

						<div name="register" id="register"
							style="overflow: scroll; height: 400px; width: 600px; border: solid; border-width: 1px; font-size: 12px; text-align: left">
							<br />:::Starting.....
						</div>
					</td>
				</tr>
				
				<% if(!"".equalsIgnoreCase(completeStatusText.trim())) {
		%>
				<tr>
					<td><input type="button" value="Close Window"
						onClick="window.close()"> <!-- <input type="button" value="Close" name="Close" onlick="closeWindow()"/>	 --></td>
				</tr>
				<%
	} %>
			</table>
			<input type="hidden" name="completeStatusText" />
		</center>

	</form>
</body>
</html>