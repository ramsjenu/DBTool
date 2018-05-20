<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page
	import="com.tcs.tools.business.compare.dto.ComparedSummaryDTO,com.tcs.tools.web.util.ToolsUtil"%>

<%
response.reset();
response.setHeader("Content-type","application/xls");
response.setHeader("Content-disposition","inline; filename=sp_call_tree_report.xls");
%>
<%
String filePath= request.getContextPath();
String cssPath=filePath+"/web/css";
String jsPath=filePath+"/web/js";
String imagePath=filePath+"/web/images";
%>
<%
	List spCallTreeFirstLevelReportList = new ArrayList();
	if(request.getAttribute("spCallTreeFirstLevelReportList") != null){
		spCallTreeFirstLevelReportList = (List)request.getAttribute("spCallTreeFirstLevelReportList");
	}
	
	HashMap spCallTreeFirstLevelReportTopDataMap  = new HashMap();
	if(request.getAttribute("spCallTreeFirstLevelReportTopDataMap") != null){
		spCallTreeFirstLevelReportTopDataMap = (HashMap)request.getAttribute("spCallTreeFirstLevelReportTopDataMap");
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
		document.form1.action = "<%=filePath%>/tool/spMigrationReports.action?submitMode=spCallTreeFirstLevelReportDownload&projectId=<%=projectId%>&analysisType=<%=analysisType%>&seqNo=<%=seqNo%>","report1","menubar=0,resizable=0,width=850,height=500";
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

		<table width="100%" bgcolor="#ffffff" border=0>

			<tr>
				<td>
			<tr width="100%">
				<td width="100%" class="pageheadernoWidth">&nbsp;
					&nbsp;&nbsp;&nbsp; SP Call Tree Report</td>
			</tr>

			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>

					<table width="100%" class="subtable" cellspacing="0"
						cellpadding="1" border="1">

						<tr class="tableheader">
							
							<th>SL No.</th>
							
							<th>Level 0</th>
							<th>Level 1</th>
							<th>Level 2</th>
							<th>Level 3</th>
							<th>Level 4</th>
							<th>Level 5</th>
							<th>Level 6</th>
							<th>Level 7</th>
							<th>Level 8</th>
							<th>Level 9</th>
							<th>Level 10</th>
							<th>Level 11</th>
							<th>Level 12</th>
							<th>Level 13</th>
							<th>Level 14</th>
							<th>Level 15</th>
							<th>Level 16</th>
							<th>Level 17</th>
							<th>Level 18</th>
							<th>Level 19</th>
							<th>Level 20</th>
							<th>Level 21</th>
							


						</tr>

						<%
					ComparedSummaryDTO lComparedSummaryDTO = new ComparedSummaryDTO();
					//System.out.println("LIST:::"+manualModificationReportList);
					if(spCallTreeFirstLevelReportList != null && spCallTreeFirstLevelReportList.size() >0){
						for(int i=0;i<spCallTreeFirstLevelReportList.size();i++){
							lComparedSummaryDTO =(ComparedSummaryDTO)spCallTreeFirstLevelReportList.get(i);
							//System.out.println("LIST:::"+(ComparedSummaryDTO)manualModificationReportList.get(i));
					%>
						<tr>
							<td>&nbsp;<%=i+1 %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel0() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel1() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel2() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel3() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel4() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel5() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel6() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel7() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel8() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel9() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel10() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel11() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel12() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel13() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel14() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel15() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel16() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel17() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel18() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel19() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel20() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getLevel21() %></td>
							
							


						</tr>
						<%}} %>
					</table>
				</td>
			</tr>




		</table>

	</form>
</body>
</html>
