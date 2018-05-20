<%-- 
<%@ page contentType="charset=iso-8859-7"%>


<%
response.setContentType("application/vnd.ms-excel;charset=UTF-8");
%> --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tcs.tools.business.frontend.dto.DynamicSQLDataDTO"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@ page trimDirectiveWhitespaces="true" %> 
<%@page import="java.util.List"%>
<%@page
	import="com.tcs.tools.web.dto.SPPatternAnalysisReportDTO,com.tcs.tools.web.util.ToolsUtil"%>

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
	 
	 String migrationMode = "";
	 if(request.getAttribute("migrationMode") != null){
		 migrationMode = (String)request.getAttribute("migrationMode");
	 }
	 System.out.println(":::::migrationMode::::"+migrationMode);
	
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>
<script>
 function openPopUpReport(){
		document.form1.action = "<%=filePath%>/tool/spMigrationReports.action?submitMode=spPatternReportDownLoad&projectId=<%=projectId%>&analysisType=<%=analysisType%>&seqNo=<%=seqNo%>","report1","menubar=0,resizable=0,width=850,height=500";
		document.form1.submit();
	 }
 
 
 function openFullReport(){
		document.form1.action = "<%=filePath%>/tool/feCodeMigrationReports.action?submitMode=feMapToTargetReportDownload&projectId=<%=projectId%>&analysisType=<%=analysisType%>&seqNo=<%=seqNo%>&migrationMode=<%=migrationMode%>","report1","menubar=0,resizable=0,width=850,height=500";
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


<STYLE type=text/css>
P {
	MARGIN: 0px auto 1em;
	WIDTH: 300px
}

UL.fdtablePaginater {
	PADDING-RIGHT: 0px;
	PADDING-LEFT: 0px;
	PADDING-BOTTOM: 0px;
	MARGIN: 0px auto 2em;
	WIDTH: auto;
	PADDING-TOP: 0px;
	LIST-STYLE-TYPE: none;
	HEIGHT: 2em;
	TEXT-ALIGN: center
}

UL.fdtablePaginater LI {
	PADDING-RIGHT: 4px;
	COLOR: #666;
	LIST-STYLE-TYPE: none;
	-moz-user-select: none;
	-khtml-user-select: none
}

UL.fdtablePaginater LI A.currentPage {
	BORDER-LEFT-COLOR: #a84444 ! important;
	BORDER-BOTTOM-COLOR: #a84444 ! important;
	COLOR: #000;
	BORDER-TOP-COLOR: #a84444 ! important;
	BORDER-RIGHT-COLOR: #a84444 ! important
}

UL.fdtablePaginater LI A:active {
	BORDER-LEFT-COLOR: #222 ! important;
	BORDER-BOTTOM-COLOR: #222 ! important;
	COLOR: #222;
	BORDER-TOP-COLOR: #222 ! important;
	BORDER-RIGHT-COLOR: #222 ! important
}

UL.fdtablePaginater LI A {
	BORDER-RIGHT: #ccc 1px solid;
	PADDING-RIGHT: 0px;
	BORDER-TOP: #ccc 1px solid;
	DISPLAY: block;
	PADDING-LEFT: 0px;
	FONT-SIZE: 1em;
	PADDING-BOTTOM: 0px;
	MARGIN: 0px;
	BORDER-LEFT: #ccc 1px solid;
	WIDTH: 2em;
	COLOR: #666;
	PADDING-TOP: 0px;
	BORDER-BOTTOM: #ccc 1px solid;
	FONT-FAMILY: georgia, serif;
	TEXT-DECORATION: none;
	outline: none
}

UL.fdtablePaginater LI DIV {
	BORDER-RIGHT: #ccc 1px solid;
	PADDING-RIGHT: 0px;
	BORDER-TOP: #ccc 1px solid;
	DISPLAY: block;
	PADDING-LEFT: 0px;
	FONT-SIZE: 1em;
	PADDING-BOTTOM: 0px;
	MARGIN: 0px;
	BORDER-LEFT: #ccc 1px solid;
	WIDTH: 2em;
	COLOR: #666;
	PADDING-TOP: 0px;
	BORDER-BOTTOM: #ccc 1px solid;
	FONT-FAMILY: georgia, serif;
	TEXT-DECORATION: none;
	outline: none
}

UL.fdtablePaginater LI DIV {
	FILTER: alpha(opacity = 50);
	opacity: .5
}

UL.fdtablePaginater LI A SPAN {
	BORDER-RIGHT: #fff 1px solid;
	BORDER-TOP: #fff 1px solid;
	DISPLAY: block;
	BACKGROUND: url(../media/gradient.gif) #fff repeat-x 0px -20px;
	BORDER-LEFT: #fff 1px solid;
	LINE-HEIGHT: 2em;
	BORDER-BOTTOM: #fff 1px solid
}

UL.fdtablePaginater LI DIV SPAN {
	BORDER-RIGHT: #fff 1px solid;
	BORDER-TOP: #fff 1px solid;
	DISPLAY: block;
	BACKGROUND: url(../media/gradient.gif) #fff repeat-x 0px -20px;
	BORDER-LEFT: #fff 1px solid;
	LINE-HEIGHT: 2em;
	BORDER-BOTTOM: #fff 1px solid
}

UL.fdtablePaginater LI A {
	CURSOR: pointer
}

UL.fdtablePaginater LI A:focus {
	BORDER-LEFT-COLOR: #aaa;
	BORDER-BOTTOM-COLOR: #aaa;
	COLOR: #333;
	BORDER-TOP-COLOR: #aaa;
	TEXT-DECORATION: none;
	BORDER-RIGHT-COLOR: #aaa
}

.fdtablePaginaterWrap {
	CLEAR: both;
	TEXT-ALIGN: center;
	TEXT-DECORATION: none
}

UL.fdtablePaginater LI .next-page SPAN {
	FONT-WEIGHT: bold ! important
}

UL.fdtablePaginater LI .previous-page SPAN {
	FONT-WEIGHT: bold ! important
}

UL.fdtablePaginater LI .first-page SPAN {
	FONT-WEIGHT: bold ! important
}

UL.fdtablePaginater LI .last-page SPAN {
	FONT-WEIGHT: bold ! important
}

TD.sized1 {
	WIDTH: 16em;
	TEXT-ALIGN: left
}

TD.sized2 {
	WIDTH: 10em;
	TEXT-ALIGN: left
}

TD.sized3 {
	WIDTH: 7em;
	TEXT-ALIGN: left
}

TFOOT TD {
	FONT-WEIGHT: bold;
	TEXT-TRANSFORM: uppercase;
	LETTER-SPACING: 1px;
	TEXT-ALIGN: right
}

#visibleTotal {
	TEXT-ALIGN: center
}

* HTML UL.fdtablePaginater LI DIV SPAN {
	BACKGROUND: #eee
}

* HTML UL.fdtablePaginater LI DIV SPAN {
	BACKGROUND: #eee
}

TR.invisibleRow {
	DISPLAY: none;
	VISIBILITY: hidden
}

P.paginationText {
	FONT-STYLE: oblique
}
</STYLE>

<STYLE type=text/css>
UL.fdtablePaginater {
	DISPLAY: inline-block
}

UL.fdtablePaginater {
	DISPLAY: inline
}

UL.fdtablePaginater LI {
	FLOAT: left
}

UL.fdtablePaginater {
	TEXT-ALIGN: center
}

TABLE {
	BORDER-BOTTOM: #c1dad7 1px solid
}
</STYLE>

</head>

<body>

	<form action="" method="post" name="form1" id="form1">





		<table width="100%" bgcolor="#f4e7bd" border=0>


			<tr width="100%">
				<td width="100%" class="pageheadernoWidth">&nbsp;
					&nbsp;&nbsp;&nbsp;Front End DSQL Source & Target Mapping Report</td>
			</tr>

			<tr>
				<td>&nbsp;</td>
			</tr>




			<tr width="100%">
				<td width="100%">
					<!-- header table-->
					<table width="90%" align="center" class="subtable" border=0>
						<tr>
							<td width="15%" class="labelText2" align="right">Customer
								Name</td>
							<td class="labelBorder">&nbsp; <%=ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"CUSTOMER_NAME")) %></td>
							<td width="5%">&nbsp;</td>
							<td width="15%" class="labelText2" align="right">Generated
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
			<!-- <tr>
				<td align="right">&nbsp;<div id="report1_link" >&nbsp; <a class="moreproject" style="cursor:hand" onclick="openPopUpReport();"><u><font color="blue">Download Pattern Analysis Summary Report in Excel Format </font></u> </a></td>
			</tr>
			<tr>
				<td align="right">&nbsp;<div id="report1_link" >&nbsp; <a class="moreproject" style="cursor:hand" onclick="openFullReport();"><u><font color="blue">Download Pattern Analysis Detail Report in Excel Format </font></u> </a></td>
			</tr> -->
			<tr>
				<td align="right">&nbsp;
					<div id="report1_link">
						&nbsp; <a class="moreproject2" style="cursor: hand"
							onclick="openFullReport();"><u><font color="blue">Export to Excel</font></u> </a>
				</td>
			</tr>

			<!-- <tr>
				<td>
					<table width="90%" align="center" class="subtable">
						<tr class="tableheader" align="left">
							<td width="4%" >S.No</td>
							<td width="26%">Stored Procedure Name</td>
							<td width="4%">Line No</td>
							<td width="10%">Category</td>
							<td width="14%">Pattern Id</td>
							<td width="24%">Pattern Desc</td>
							<td width="9%">No. Sub </td>
							<td width="9%">Line </td>
						</tr>
						
					</table>
				</td>
			</tr> -->

			<tr>
				<td align="center">

					<table width="90%" align="center"
						style="border: solid 2px #8C2C02; border-style: double;"
						class="sortable-onload-1 no-arrow rowstyle-alt colstyle-alt paginate-10 max-pages-7 theTable-fdtablePaginaterWrapBottom paginationcallback-callbackTest-calculateTotalRating paginationcallback-callbackTest-displayTextInfo sortcompletecallback-callbackTest-calculateTotalRating"
						id=theTable cellSpacing=2 cellPadding=2>
						<!-- <TFOOT>
  <TR>
    <TD colSpan=6>Average (fake) Rating</TD>
    <TD id=visibleTotal>…</TD></TR></TFOOT> -->
						<THEAD>
							<TR class="tableheader" align="left">
								<TH class=sortable-date-dmy>S.No</TH>
								<TH class=sortable-text>Application Path</TH>
								<TH class=sortable-text>File Name</TH>
								<TH class=sortable-currency>Source Query</TH>
								<TH class=sortable-currency>Converted Query</TH>
								<TH class=sortable-currency>Convertion Status</TH>
							</TR>
						</THEAD>
						<TBODY>


							<%	String lCssClass="tablerowodd";
							DynamicSQLDataDTO lDynamicSQLDataDTO = new DynamicSQLDataDTO();
							String lSourceFilePath="";
							int lSno=0;
							if(patternAnalysisReportList != null && patternAnalysisReportList.size() > 0 ){
								for(int i=0;i<patternAnalysisReportList.size();i++){
									lDynamicSQLDataDTO =(DynamicSQLDataDTO)patternAnalysisReportList.get(i);	
										lSno=lSno+1;
										if( lSno%2 ==0){
											lCssClass="tablerowodd";
										}else{
											lCssClass="tableroweven";
										}
										lSourceFilePath=lDynamicSQLDataDTO.getSourceFilePath().trim().split("\\\\Unzipped\\\\")[1];
										lSourceFilePath=lSourceFilePath.substring(lSourceFilePath.indexOf("\\")+1,lSourceFilePath.length());
										lSourceFilePath=ToolsUtil.replaceNull((String)patternAnalysisReportTopDataMap.get((String)"PROJECT_NAME")) +"\\"+lSourceFilePath+"\\";
										%>
							<tr class="<%=lCssClass%>" align="left">
								<td width="4%"><%=(lSno) %></td>
								<td><%=lSourceFilePath.trim() %></td>
								<td><%=lDynamicSQLDataDTO.getSourceFileName().trim() %></td>
								<td><%=lDynamicSQLDataDTO.getOrginalDSQLQuery() %></td>
								<td><%=lDynamicSQLDataDTO.getConvertedQuery()%></td>
								<td><%=lDynamicSQLDataDTO.getConvertionStatus() %></td>
							</tr>

							<%
										
										
									}
								} %>

						</TBODY>
					</table>

				</td>
			</tr>


		</table>
		</td>
		</tr>

		</table>

	</form>
</body>
</html>
