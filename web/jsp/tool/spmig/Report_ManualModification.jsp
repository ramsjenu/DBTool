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
	List manualModificationReportList = new ArrayList();
	if(request.getAttribute("manualModificationReportList") != null){
		manualModificationReportList = (List)request.getAttribute("manualModificationReportList");
	}
	
	HashMap manualModificationReportTopDataMap  = new HashMap();
	if(request.getAttribute("manualModificationReportTopDataMap") != null){
		manualModificationReportTopDataMap = (HashMap)request.getAttribute("manualModificationReportTopDataMap");
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
		document.form1.action = "<%=filePath%>/tool/spMigrationReports.action?submitMode=spManualModificationReportDownload&projectId=<%=projectId%>&analysisType=<%=analysisType%>&seqNo=<%=seqNo%>","repot1","menubar=0,resizable=0,width=850,height=500";
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
					&nbsp;&nbsp;&nbsp; Manual Modification Log</td>
			</tr>

			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>

					<table width="100%" class="subtable" cellspacing="0"
						cellpadding="1" border="1">

						<tr class="tableheader">
							<th>S.No</th>
							<th>Procedure Name</th>
							<th>Statement No</th>
							<th>Statement</th>


						</tr>

						<%
					ComparedSummaryDTO lComparedSummaryDTO = new ComparedSummaryDTO();
					//System.out.println("LIST:::"+manualModificationReportList);
					if(manualModificationReportList != null && manualModificationReportList.size() >0){
						for(int i=0;i<manualModificationReportList.size();i++){
							lComparedSummaryDTO =(ComparedSummaryDTO)manualModificationReportList.get(i);
							//System.out.println("LIST:::"+(ComparedSummaryDTO)manualModificationReportList.get(i));
					%>
						<tr>
							<td><%=(i+1)%></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getProcedureName() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getStmtno() %></td>
							<td>&nbsp;<%=lComparedSummaryDTO.getStmt() %></td>


						</tr>
						<%}} %>
					</table>
				</td>
			</tr>




		</table>

	</form>
</body>
</html>
