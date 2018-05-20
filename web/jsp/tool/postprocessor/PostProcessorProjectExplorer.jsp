<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List,java.util.ArrayList,java.util.HashMap"%>
<%@ page import="com.tcs.tools.web.dto.StoredProceduresDetailsDTO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>

<title>:::::db TransPlant- Post Processor Left Page::::</title>

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
	 
	
		String submitMode = "";
		if(request.getAttribute("submitMode") != null){
			submitMode = (String)request.getAttribute("submitMode");
		}
		
		 String projectId ="";
		  if(request.getAttribute("projectId") != null){
			  projectId = (String)request.getAttribute("projectId");
		 } 
		
		  HashMap projectNameIdMap = new HashMap();
			if(request.getAttribute("projectNameIdMap") != null){
				projectNameIdMap = (HashMap)request.getAttribute("projectNameIdMap");
			}
			
		List projectSpDetailsList = new ArrayList();	
		if(request.getAttribute("projectSpDetailsList") != null){
			projectSpDetailsList = (List)request.getAttribute("projectSpDetailsList");
		}
	%>
<script type="text/javascript" src="<%=jsPath %>/custom/common.js"></script>
<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />
<style>
body {
	font: 12px Tahoma, Verdana, sans-serif;
	padding: 0 5px;
}

a:link,a:visited {
	text-decoration: none;
	color: #416CE5;
	border-bottom: 1px solid #416CE5;
}

h2 {
	font-size: 13px;
	margin: 5px 0 0 0;
}
</style>
<script>
		var prevProcId="";
		function loadSpCode(pProcName,count){
		//alert(pProcName+"--1-"+count);
			var url="<%=filePath%>/tool/postProcessorIdentifyPattern.action?submitMode=enterPatterns&linkMode=frameMode&projectId=<%=projectId%>&procName="+pProcName;
			//alert("::::url to be changed:::"+url);
			//document.form1.action="<%=filePath%>/tool/postProcessorIdentifyPattern.action?submitMode=enterPatterns&projectId=<%=projectId%>&procName="+pProcName;
			//document.form1.action="/WebProject/tool/postProcessorIdentifyPattern.action?submitMode=enterPatterns";
			//document.form1.target="codeEditor";		
			if(prevProcId.length>0){
				document.getElementById(prevProcId).style.color="BLACK";				
				document.getElementById(prevProcId).style.fontWeight="normal";				
			}	
			document.getElementById("procName"+count).style.color="GREEN";
			document.getElementById("procName"+count).style.fontWeight="bold"; 
			
			prevProcId="procName"+count;
			parent.chnageFrameSrc("codeEditor",url);
			
			//var tracker_url="<%=filePath%>/tool/postProcessorIdentifyPattern.action?submitMode=changeTracker&linkMode=frameMode&projectId=<%=projectId%>&procName="+pProcName;
			//parent.chnageFrameSrc("changeTracker",tracker_url);
			
			parent.setElementValue("procName",pProcName);

			//document.form1.submit();
		}
		
		</script>
</head>
<body style="background-color: #D2DFEF; text-color: black;">

	<form name="form1" action="post">
		<!-- <h2>Project Explorer</h2> -->
		<table width="100%" cellspacing=0 cellpadding=0>

			<tr>
				<td colspan="2"><h2>
						Project Selected:
						<%=projectNameIdMap.get(projectId.trim()).toString() %></h2></td>
			</tr>

			<tr>
				<td>
					<table width="100%">
						<!-- <tr class="pageheadernoWidth1" style="text-align:left"> 
					<td> S.No</td>
					<td> Procedure Name</td> 
					</tr> -->
						<% 
						String lCssClass="tableroweven1";
						StoredProceduresDetailsDTO lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
						for(int i=0;i<projectSpDetailsList.size();i++){
							lStoredProceduresDetailsDTO =(StoredProceduresDetailsDTO)projectSpDetailsList.get(i);
							if( i%2 ==0){
								//lCssClass="background-color:#CCEEFF;";
								//lCssClass="tablerowodd1";
							}else{
								//lCssClass="background-color:#FFFFFF;";
								//lCssClass="tableroweven1";
							}
						%>
						<tr>
							<%-- <td width="1%"><%=i+1 %></td> --%>
							<td><span style="color: black; cursor: hand;"
								id='procName<%=i%>'
								onclick="loadSpCode('<%=lStoredProceduresDetailsDTO.getProcName()%>','<%=i%>');"><%=lStoredProceduresDetailsDTO.getProcName()%></span>
							</td>
						</tr>

						<%
						}%>

					</table>
				</td>
			</tr>
			<tr>
				<td style="padding: 2px"></td>
			</tr>
		</table>

	</form>

</body>
</html>