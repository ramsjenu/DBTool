<%-- 
<%@ page contentType="charset=iso-8859-7"%>


<%
response.setContentType("application/vnd.ms-excel;charset=UTF-8");
%> --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page
	import="com.tcs.tools.business.compare.dto.ComparedSummaryDTO,com.tcs.tools.web.util.ToolsUtil"%>

<%
String filePath= request.getContextPath();
String cssPath=filePath+"/web/css";
String jsPath=filePath+"/web/js";
String imagePath=filePath+"/web/images";
%>
<%
	List patternAnalysisReportList = new ArrayList();
	if(request.getAttribute("patternAnalysisReportList") != null){
		patternAnalysisReportList = (List)request.getAttribute("patternAnalysisReportList");
	}
	
	HashMap patternAnalysisReportTopDataMap  = new HashMap();
	if(request.getAttribute("patternAnalysisReportTopDataMap") != null){
		patternAnalysisReportTopDataMap = (HashMap)request.getAttribute("patternAnalysisReportTopDataMap");
	}

	
	String projectId = "";
	 if(request.getAttribute("projectId") != null){
		 projectId = (String)request.getAttribute("projectId");
	 }
	 System.out.println(":::::projectId::::"+projectId);
	 String analysisType = "";
	 if(request.getAttribute("analysisType") != null){
		 analysisType = (String)request.getAttribute("analysisType");
	 }
	 System.out.println(":::::analysisType::::"+analysisType);
	 String seqNo="";
	 if(request.getAttribute("seqNo") != null){
		 seqNo = (String)request.getAttribute("seqNo");
	 }
	 System.out.println(":::::seqNo::::"+seqNo);
	
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>
<script>
 function openPopUpReport(){
		document.form1.action = "<%=filePath%>/tool/spMigrationReports.action?submitMode=spPatternReportDownLoad&projectId=<%=projectId%>&analysisType=<%=analysisType%>&seqNo=<%=seqNo%>","repot1","menubar=0,resizable=0,width=850,height=500";
		document.form1.submit();
	 }
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>:::::db TransPlant::::</title>
<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />
<link rel="icon" href="images/icon.ico" />
<!-- <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script> -->
<script type="text/javascript" src="<%=jsPath%>/paginate.js"> </script>
<link href="<%=cssPath%>/style_pagination_1.css" rel="stylesheet"
	type="text/css" />



</head>

<body>

	<form action="" method="post" name="form1" id="form1">

		<table width="100%" bgcolor="#f4e7bd" border=0>


			<tr width="100%">
				<td width="100%" class="pageheadernoWidth">&nbsp;
					&nbsp;&nbsp;&nbsp; Complete MisMatch Summary Report</td>
			</tr>

			<tr>
				<td>&nbsp;</td>
			</tr>

			<tr width="100%">
				<td width="100%">
					<!-- header table-->
					<table width="100%" align="center" class="subtable" border=0>
						<tr>
							<td width="20%" class="labelText2" align="right">Customer
								Name</td>
							<td class="labelBorder">&nbsp; <%=ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"CUSTOMER_NAME")) %></td>
							<td width="5%">&nbsp;</td>
							<td width="20%" class="labelText2" align="right">Generated
								By</td>
							<td class="labelBorder">&nbsp; <%=ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"CREATED_BY")) %></td>
						</tr>

						<tr>
							<td class="labelText2" align="right">Project Name</td>
							<td class="labelBorder">&nbsp;<%=ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"PROJECT_NAME")) %></td>
							<td>&nbsp;</td>
							<td class="labelText2" align="right">Generated On</td>
							<td class="labelBorder">&nbsp;<%=ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"CREATED_DATE")) %></td>
						</tr>

						<tr>
							<td class="labelText2" align="right">Application Name</td>
							<td class="labelBorder">&nbsp;<%=ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"APPLICATION_NAME")) %></td>
							<td>&nbsp;</td>
							<td class="labelText2" align="right">Source DBMS</td>
							<td class="labelBorder">&nbsp;<%=ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"SOURCE_DB_TYPE")) %></td>
						</tr>

						<tr>
							<td class="labelText2" align="right">Migration Type</td>
							<td class="labelBorder">&nbsp;<%=ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"ANALYSIS_MODE")) %></td>
							<td>&nbsp;</td>
							<td class="labelText2" align="right">Target DBMS</td>
							<td class="labelBorder">&nbsp;<%=ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"TARGET_DB_TYPE")) %></td>
						</tr>


					</table>
				</td>
			</tr>

			<tr>
				<td>&nbsp;</td>
			</tr>


			<tr>
				<td>
					<table width="100%" class="subtable" cellspacing="2"
						cellpadding="2" border="0">

						<tr class="tableheader">
							<th>S.No</th>
							<th>Procedure Name</th>
							<th>Matched Count</th>
							<th>Mismatched Count</th>

						</tr>

						<%
					String lCssClass ="";
					
					ComparedSummaryDTO lComparedSummaryDTO = new ComparedSummaryDTO();
					if(patternAnalysisReportList != null && patternAnalysisReportList.size() >0){
						for(int i=0;i<patternAnalysisReportList.size();i++){
							lComparedSummaryDTO =(ComparedSummaryDTO)patternAnalysisReportList.get(i);
							
							if( i%2 ==0){
								lCssClass="tablerowodd";
							}else{
								lCssClass="tableroweven";
							}
							
							
							
					%>
						<tr class="<%=lCssClass %>">
							<td><%=(i+1)%></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getProcedureName() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getyMatchCount() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getnMatchCount() %></td>

						</tr>
						<%}} %>
					</table>
				</td>
			</tr>




		</table>

	</form>
</body>
</html>
