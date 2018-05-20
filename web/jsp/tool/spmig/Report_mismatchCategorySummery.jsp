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
	List lMismatchSummeyRow=new ArrayList();
	List lMismatchSummeyCatgeoryRow=null;
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
	 String migrationMode="";
	 
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>

<script>
 function openPopUpReport(){
		document.form1.action = "<%=filePath%>/tool/spMigrationReports.action?submitMode=spPatternReportDownLoad&projectId=<%=projectId%>&analysisType=<%=analysisType%>&seqNo=<%=seqNo%>","repot1","menubar=0,resizable=0,width=850,height=500";
		document.form1.submit();
	 }
 function openPopUpDetailedMismatchProcWiseReport(procName,misMatchCategory){
		
		
	 window.open ("<%=filePath%>/tool/spMigrationReports.action?submitMode=detailedMismatchProcWiseReport&projectId=<%=projectId%>&migrationMode=<%=migrationMode%>&seqNo=<%=seqNo%>&procName="+procName+"&misMatchCategory="+misMatchCategory,"detailedMismatchProcWiseReport","scrollbars=1,menubar=0,resizable=1,width=850,height=500");  
		
		}
 function gotoExcel(elemId, frmFldId)   
 {   
     var obj = document.getElementById(elemId);   
     var oFld = document.getElementById(frmFldId);   
     oFld.value = obj.innerHTML;   

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

	<form action="exportToExcel.action" method="post" name="form1" id="form1">
<div id="tableData">
		<table width="100%" bgcolor="#ffffff" border=0>


			<tr width="100%">
				<td width="100%" class="pageheadernoWidth">&nbsp;
					&nbsp;&nbsp;&nbsp; Category Wise Mismatch Summary Report</td>
			</tr>

			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<!-- <table width="100%" class="subtable">
					<tr class="tableheader">
						<td width="1%">S.No</td>
						<td>Procedure Name</td>
						<td>Source Statement No</td>
						<td width="100px">Source Statement</td>
						<td width="100px">Target Statement</td>
						<td>Target Statement No</td>
						<td>Matched Yes/No</td>					
						
					</tr>
				</table> -->
					<table width="100%" class="subtable" cellspacing="0"
						cellpadding="1" border="1">
						<% if(patternAnalysisReportList != null && patternAnalysisReportList.size() >0){ 
					lMismatchSummeyRow=(List)patternAnalysisReportList.get(0); 
				%>
						<tr class="tableheader">

							<%for(int j=0;j<lMismatchSummeyRow.size();j++){
							%>
							<th><%=((String)lMismatchSummeyRow.get(j)).replaceAll("_", " ") %>
							</th>
							<% 
						}
						%>
						</tr>

						<%
					
				}
				
					
					if(patternAnalysisReportList != null && patternAnalysisReportList.size() >0){
						for(int i=1;i<patternAnalysisReportList.size();i++){
							if(i==1){
								lMismatchSummeyCatgeoryRow=(List)patternAnalysisReportList.get(i);
								continue;
							}
							lMismatchSummeyRow=(List)patternAnalysisReportList.get(i);		
					%>
						<tr>

							<% for(int j=0;j<lMismatchSummeyRow.size();j++){
							String lClass="cellWhite";
							 if(j>0 ) {
								 if(Integer.parseInt((String)lMismatchSummeyRow.get(j))==0 ){
									 lClass="cellGreen"; 
								 }else if(Integer.parseInt((String)lMismatchSummeyRow.get(j))<=5 ){
									 lClass="cellBlue"; 
								 }else {
									 lClass="cellRed"; 
								 }
								 
							 }
							%>
							<td class="<%=lClass%>">
								<% if(j>0  && Integer.parseInt((String)lMismatchSummeyRow.get(j))>0 ) {
								String lProcName=(String)lMismatchSummeyRow.get(0);
								String lMisMatchCategory=(String)lMismatchSummeyCatgeoryRow.get(j);
							%><a style="cursor: hand"
								onclick="openPopUpDetailedMismatchProcWiseReport('<%=lProcName%>','<%=lMisMatchCategory%>');">
									<% } %> <%=(String)lMismatchSummeyRow.get(j)%> <% if(j>0) {%>
							</a> <%} %>
							</td>
							<% 
						}
						%>

						</tr>
						<%}} %>
					</table>
				</td>
			</tr>

		</table>
		<br />
		<br />
		<table class="subtable">
			<tr>
				<td class="label1">0 Mismatch</td>
				<td class="cellGreenColorOnly"></td>
			</tr>
			<tr>
				<td class="label1">1-5 Mismatch</td>
				<td class="cellBlueColorOnly"></td>
			</tr>
			<tr>
				<td class="label1">More than 5 Mismatch</td>
				<td class="cellRedColorOnly"></td>
			</tr>
		</table>
		</div>
	
		
		
<input type="hidden" id="tableHTML" name="tableHTML" value="" /> 
      <input type="hidden" id="fileName" name="fileName" value="MISMATCH_CATEGORY_COUNT_<%=ToolsUtil.getDateTime()%>.xls" /> 
   <center> <input type="submit" class="moreproject"  onclick="gotoExcel('tableData', 'tableHTML');" value="Export To Excel" /></center>
	</form>
</body>
</html>
