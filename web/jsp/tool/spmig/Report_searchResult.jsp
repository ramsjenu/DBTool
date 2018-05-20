<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page
	import="com.tcs.tools.web.dto.SearchSPDTO,com.tcs.tools.web.util.ToolsUtil"%>

<%
String filePath= request.getContextPath();
String cssPath=filePath+"/web/css";
String jsPath=filePath+"/web/js";
String imagePath=filePath+"/web/images";
%>


<%


    ArrayList<SearchSPDTO> searchedList = new ArrayList();
	if(request.getAttribute("searchedList") != null){
		searchedList = (ArrayList)request.getAttribute("searchedList");
	}
	
	HashMap spSearchReportTopDataMap = new HashMap();
	if(request.getAttribute("spSearchReportTopDataMap") != null){
		spSearchReportTopDataMap = (HashMap)request.getAttribute("spSearchReportTopDataMap");
	}
	
	
	
	
	String projectId = "";
	 if(request.getAttribute("projectId") != null){
		 projectId = (String)request.getAttribute("projectId");
	 }
	 String searchedString = "";
	
	 if(request.getAttribute("searchedString") != null){
		 searchedString = (String)request.getAttribute("searchedString");
		
	 }
	 String searchedString2 = "";
	 if(request.getAttribute("searchedString2") != null){
		 searchedString2 = (String)request.getAttribute("searchedString2");
		
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
	if(document.getElementById("searchedString")!=null && document.getElementById("searchedString")!=" ")	{
	 document.form1.action = "<%=filePath%>/tool/spMigrationReports.action?submitMode=spSearchReportDownload&projectId=<%=projectId%>&analysisType=<%=analysisType%>&seqNo=<%=seqNo%>&searchedString=<%=searchedString%>","report1","menubar=0,resizable=0,width=850,height=500";
	}
	if(document.getElementById("searchedString2")!=null && document.getElementById("searchedString2")!=" ")	{
		document.form1.action = "<%=filePath%>/tool/spMigrationReports.action?submitMode=spSearchReportDownload&projectId=<%=projectId%>&seqNo=<%=seqNo%>&searchedString2=<%=searchedString2%>","report2","menubar=0,resizable=0,width=850,height=500";
		
	}document.form1.submit();
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
<input type="hidden" id=searchedString value=<%=searchedString %>>
<input type="hidden" id=searchedString2 value=<%=searchedString2 %>>
		 <div id="tableData">   
		
		<table width="100%" bgcolor="#ffffff" border=0>

			<tr>
				<td>
			<tr width="100%">
				<td width="100%" class="pageheadernoWidth">&nbsp;
					&nbsp;&nbsp;&nbsp; Search Result</td>
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
							<th>Searched Word</th>
							<th>Procedure Name</th>
							<th>Statement No</th>
							<th>Statement</th>


						</tr>

						<%
	
	if(searchedList.size()==0){
		out.println("<font color=red>0 result(s) returned</font>");
	}
	else{
		out.println("<font color=red>"+ searchedList.size() +" result(s) returned </font>");
	}
	
	//Iterator it = feedbackList.entrySet().iterator(); 
	//while (it.hasNext()) { 
	//Map.Entry pairs = (Map.Entry)it.next(); 
	
	for(int i=0;i<searchedList.size();i++){
	
	%>
	<tr>
	<td>
	<%=i+1%>
	</td>
	<td>
	<%=searchedList.get(i).getSearchedWord()%>
	</td>
	<td>
	<%=searchedList.get(i).getProcedureName()%>	</td>
	<td>
	<%=searchedList.get(i).getStatementNo()%>
	</td>
	<td>
	
	<%=searchedList.get(i).getStatement()%>
	</td>
	
	</tr>
	<%
	//System.out.println(pairs.getKey() + " = " + pairs.getValue()); 
	//it.remove(); // avoids a ConcurrentModificationException 
	}
		
							
	%>
					</table>
				</td>
			</tr>

<tr>



							
						</tr>


		</table>
		</div>
		<br/>
		
		
<input type="hidden" id="tableHTML" name="tableHTML" value="" /> 
      <input type="hidden" id="fileName" name="fileName" value="SEARCH_RESULT_<%=ToolsUtil.getDateTime()%>.xls" /> 
   <center> <input type="submit" class="moreproject"  onclick="gotoExcel('tableData', 'tableHTML');" value="Export To Excel" /></center>

	</form>
</body>
</html>
