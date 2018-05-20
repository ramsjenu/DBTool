<%@ page import="java.util.List,java.util.ArrayList,java.util.HashMap"%>
<%@ page
	import="com.tcs.tools.business.postprocessor.dto.PostProcChangeTrackerDTO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String filePath= request.getContextPath();
	String cssPath=filePath+"/web/css";
	String jsPath=filePath+"/web/js";
	String imagePath=filePath+"/web/images";	
	%>

<%
		List trackerDetailsList = new ArrayList();	
		if(request.getAttribute("trackerDetailsList") != null){
			trackerDetailsList = (List)request.getAttribute("trackerDetailsList");
		}
	%>
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>

<title>:::::db TransPlant- Post Processor Left Page::::</title>
<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />

</head>
<body style="background-color: #ffffff">
	<form name="form1" bgcolor="#ffffff" method="post" action="">
		<table width="100%" bgcolor="#ffffff" border=0>
			<tr>
				<table width="100%" class="subtable1">
					<tr class="tableheader2">
						<td width="3%">S.No</td>
						<td width="50%">SP Checklist Identified Block - Before</td>
						<td width="50%">SP Checklist Identified Block - After</td>
					</tr>
				</table>
			</tr>
			<tr>
				<td>
					<div style="overflow: scroll; height: 180px;">
						<table width="100%" class="" cellspacing="0" cellpadding="1"
							border="1">
							<tr>
								<td></td>
							</tr>
							<% 
						String lCssClass="tableroweven1";
						PostProcChangeTrackerDTO lPostProcChangeTrackerDTO = new PostProcChangeTrackerDTO();
						for(int i=0;i<trackerDetailsList.size();i++){
							lPostProcChangeTrackerDTO =(PostProcChangeTrackerDTO)trackerDetailsList.get(i);
							if( i%2 ==0){
								//lCssClass="background-color:#CCEEFF;";
								//lCssClass="tablerowodd1";
							}else{
								//lCssClass="background-color:#FFFFFF;";
								//lCssClass="tableroweven1";
							}
						%>
							<tr class="labelText3">
								<%-- <td width="1%"><%=i+1 %></td> --%>
								<td width="3%"><%=(i+1) %></td>
								<td width="50%">&nbsp;<%=lPostProcChangeTrackerDTO.getBeforeBlock()%>
								</td>
								<td width="50%">&nbsp;<%=lPostProcChangeTrackerDTO.getAfterBlock()%>
								</td>

							</tr>

							<%
						}%>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>