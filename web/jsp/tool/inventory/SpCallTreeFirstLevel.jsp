<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.tcs.tools.web.dto.IdentifyPatternDTO"%>
<%@ page import="java.util.List,java.util.ArrayList,java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>

<title>:::::db TransPlant::::</title>
<%
	String filePath= request.getContextPath();
	String cssPath=filePath+"/web/css";
	String jsPath=filePath+"/web/js";
	String imagePath=filePath+"/web/images";	
	%>
<%

HashMap projectNameIdMap = new HashMap();
if(request.getAttribute("projectNameIdMap") != null){
	projectNameIdMap = (HashMap)request.getAttribute("projectNameIdMap");
}


IdentifyPatternDTO lIdentifyPatternDTO = new IdentifyPatternDTO();
List procNameList = new ArrayList();
if(request.getAttribute("procNameList") != null){
	procNameList = (List)request.getAttribute("procNameList");
}

 String projectId = "";
 if(request.getAttribute("projectId") != null){
	 projectId = (String)request.getAttribute("projectId");
 }
 System.out.println("::::projectId::::"+projectId);
 String migrationMode = "";
 if(request.getAttribute("migrationMode") != null){
	 migrationMode = (String)request.getAttribute("migrationMode");
 }
 
 String analysisType = "";
 if(request.getAttribute("analysisType") != null){
	 analysisType = (String)request.getAttribute("analysisType");
 }
 
 String msgToJsp ="";
 if(request.getAttribute("msgToJsp") != null){
	 msgToJsp = (String)request.getAttribute("msgToJsp");
 }
 
 String seqNo="";
 if(request.getAttribute("seqNo") != null){
	 seqNo = (String)request.getAttribute("seqNo");
 }
 
 String pRunId="";
 if(request.getAttribute("pRunId") != null){
	 pRunId = (String)request.getAttribute("pRunId");
 }
 
 
 String partialSPSelected = "";
 String[] lPartialSPArr;
 List lPartialSPList = new ArrayList();
 if(request.getAttribute("partialSPSelected") != null){
	 partialSPSelected =  (String)request.getAttribute("partialSPSelected");
	 
	 lPartialSPArr  = partialSPSelected.split(",");
		for (int i = 0; i < lPartialSPArr.length; i++) {
			if(lPartialSPArr[i] == null || "".equals(lPartialSPArr[i])) {
				continue;
			}
			lPartialSPList.add(lPartialSPArr[i]);
		}	
 }
 System.out.println("::::partialSPSelected::::"+partialSPSelected);
%>

<script>




	var lMatchedSPCount=0;
	
	 function openPopUpReport(){
		 window.open ("<%=filePath%>/tool/spMigrationReports.action?submitMode=spCallTreeFirstLevelReport&projectId=<%=projectId%>&migrationMode=<%=migrationMode%>&seqNo=<%=seqNo%>&analysisType=<%=analysisType%>","detailedSummaryReport","scrollbars=1,menubar=0,resizable=1,width=850,height=500");
	 }

	function onLoadIdentifyMode(){
		if(parseInt('<%=lPartialSPList.size()%>') > 0 ){
			changeAnalysisMode("Partial");
		}
		if(document.form1.retMsgToJsp.value != ""){
			document.getElementById("report1_link").style.visibility = "visible";
			document.getElementById("report1_link").style.display = "inline";
		}
		
	}
	function changeProjectName(){
		document.form1.submitMode.value = "changeProject";
		document.form1.action='<%=filePath%>'+"/tool/SpCallTreeFirstLevel.action";
		document.form1.submit();
	} 
	function changeAnalysisMode(pMode){
		
		if(pMode.length == 0){		
			return false;
		}
		if(pMode == "Partial"){
			document.getElementById("spListToSelect").style.visibility = "visible";
			document.getElementById("spListToSelect").style.display = "inline";
			document.getElementById("spListToSelect").focus();
			document.getElementById("pageSubmit").disabled = false;
			document.getElementById("pageSubmit").style.background = "#8c2c02";
			document.getElementById("pageSubmit").style.color = "#f4e7bd";
			document.getElementById("pageSubmit").style.cursor = "pointer";
			return ;
		}else{
			document.getElementById("spListToSelect").style.visibility = "hidden";
			document.getElementById("spListToSelect").style.display = "none";
			document.getElementById("pageSubmit").disabled = false;
			document.getElementById("pageSubmit").style.background = "#8c2c02";
			document.getElementById("pageSubmit").style.color = "#f4e7bd";
			document.getElementById("pageSubmit").style.cursor = "pointer";
		}
	} 
	
	function submitIdentifyPattern(){
		if(document.form1.projectId.value == ""){
			alert("Please select project name.");
			document.form1.projectId.focus();
			return false;
		} 
		
		/*if(document.form1.analysisType.value == ""){
			alert("Please enter a pattern to analyse gaps.");
			document.form1.analysisType.focus();
			return false;
		}*/
		
		if(document.form1.migrationMode.value == ""){
			alert("Please select migration mode.");
			document.form1.migrationMode.focus();
			return false;
		}
		var pMode = document.form1.migrationMode.value ;
		var partialSPSelectedCount = 0;
		var partialSPSelected = "";
		if(pMode == "Partial"){
			
			if(lMatchedSPCount==0){
				lMatchedSPCount='<%=procNameList.size()%>';
				alert(procNameList.size());
			}
			for(var x=0;x<lMatchedSPCount;x++){
				if(document.getElementById("partialSP"+x).checked ==  true){
					partialSPSelectedCount++
					if(partialSPSelected == ""){
						partialSPSelected =document.getElementById("partialSPName"+x).value;
					}else{
						partialSPSelected += ","+document.getElementById("partialSPName"+x).value;
					}
					
				}
			}
		}
		
		//alert("partialSPSelected--->"+partialSPSelected);
		
		if( (parseInt(partialSPSelectedCount) == 0 ) && (pMode == "Partial")){
			alert("Please select any one Stored Procedure Name for partial mode...");
			return false;
		}else{
			document.form1.partialSPSelected.value = partialSPSelected;
		}
		
		document.form1.submitMode.value = "save";
		document.form1.action='<%=filePath%>'+"/tool/SpCallTreeFirstLevel.action";
		document.form1.submit();
		
	}
	
	
	function filterSPs(){
		var tableName="spDetialsList";
		deleteAllTabeRows("spDetialsList");
		
		var tdInnerHtmlValue="";
		var funcRet =false;
		lMatchedSPCount=0;
		for(var x=0;x < '<%=procNameList.size()%>';x++){
			//alert(document.getElementById("hiddenPartialSPName"+x).value);
			tdInnerHtmlValue ="";
			
			funcRet = strContains(document.getElementById("partialFilterText").value,document.getElementById("hiddenPartialSPName"+x).value , document.getElementById("matchCase").checked);
			//alert(document.getElementById("hiddenPartialSPName"+x).value+"---"+funcRet);
			if(funcRet == true){
				
				//tdInnerHtmlValue += "<input type='checkbox' name='partialSP"+x+"' id='partialSP"+x+"'/> ";
				tdInnerHtmlValue += "<input type=\"checkbox\" name=\"partialSP"+lMatchedSPCount+"\" id=\"partialSP"+lMatchedSPCount+"\"/> ";
				tdInnerHtmlValue += "&nbsp;"+document.getElementById("hiddenPartialSPName"+x).value;
				tdInnerHtmlValue += "<input type=\"hidden\" name=\"partialSPName"+lMatchedSPCount+"\" id=\"partialSPName"+lMatchedSPCount+"\"  value=\""+document.getElementById("hiddenPartialSPName"+x).value+"\"/> ";
				//alert(tdInnerHtmlValue);
				lMatchedSPCount=lMatchedSPCount+1;
				addRowInnerHTML(tableName ,tdInnerHtmlValue,"tablerowodd");
			}
			
		}
	
	
	 }
	
	</script>




</head>
<body>
<body onload="return onLoadIdentifyMode();">


	<form action="" name="form1" method="post">
		<input type="hidden" name="submitMode" id="submitMode" value="">
		<input type="hidden" name="partialSPSelected" id="partialSPSelected"
			value=""> <input type="hidden" name="retMsgToJsp"
			id="retMsgToJsp" value="<%=msgToJsp %>">

		<%if(procNameList != null && procNameList.size() >0) {
	for(int i=0;i<procNameList.size();i++){
	lIdentifyPatternDTO = (IdentifyPatternDTO)procNameList.get(i); 
%>
		<input type="hidden" name="hiddenPartialSPName<%=i%>"
			id="hiddenPartialSPName<%=i%>"
			value="<%=lIdentifyPatternDTO.getProcName()%>" />
		<%} } %>

		<table width="100%" border=1 cellspacing="0" cellpadding="0"
			id="contentblank">
			<tr>
				 <td colspan="3"><jsp:include
						page="/web/jsp/tool/common/header.jsp" flush="true" /></td>
			</tr>
			<tr>
				<td width="20%" valign="top"><jsp:include
						page="/web/jsp/tool/common/left_menu.jsp" flush="true" /></td>
				<td width="60%" valign="top">
					<!--  page content starts -->

					<table width="100%" border=0 cellspacing="2" cellpadding="2"
						valign="top">

						<tr class="pageheadernoWidth">
							<td colspan="2">&nbsp;&nbsp;&nbsp;SP Call Tree</td>
						</tr>

						<tr>
							<td colspan="2"><div id="saveResult" class="msg">
									<font color="green">&nbsp;<s:property value="msgToJsp" /></font>
								</div></td>
						</tr>
						<tr>
							<td class="label1">Project Name</td>
							<td><select class="input" name="projectId" id="projectId"
								onChange="return changeProjectName();">
									<option value="">--Select--</option>
									<%
		      					
		      					Iterator it = projectNameIdMap.entrySet().iterator(); 
		      					while (it.hasNext()) { 
		      					Map.Entry pairs = (Map.Entry)it.next(); 
		      					
		      					%>
									<option value="<%=pairs.getKey()%>"
										<% if(projectId.equalsIgnoreCase((String)pairs.getKey())){out.println("selected");} %>><%=pairs.getValue() %></option>
									<%
		      					//System.out.println(pairs.getKey() + " = " + pairs.getValue()); 
		      					
									
									it.remove(); // avoids a ConcurrentModificationException 
		      					}
		      					%> 
							</select></td>
						</tr>

						<tr>
							<td class="label1" style="visibility: hidden; display: none;">Migration Mode</td>
							<td><input type="hidden" name="migrationMode" value="bulk"></td>
						</tr>



						<tr id="spListToSelect" style="visibility: hidden; display: none;">

							<td align="left" class="label1">Please select from the list
								of SP's</td>

							<td>

								<table width="100%" cellspacing="2" cellpadding="2">
									<tr class="label1">
										<td class="label1" width="15%">Search Filter :&nbsp;</td>
										<td><input class="input" size="5" type="text"
											onkeyup="return filterSPs();" name="partialFilterText"
											id="partialFilterText" value="">&nbsp; &nbsp;&nbsp;<input
											type="checkbox" name="matchCase" id="matchCase"
											onclick="return filterSPs();" />MatchCase <!-- <input type="button"  class="moreproject" value="Search" onclick="return filterSPs();"> --></td>
									</tr>
								</table>

								<div style="overflow: scroll; height: 150px; width: 400px;">
									<table width="100%" cellspacing="2" cellpadding="2"
										id="spDetialsList" name="spDetialsList">
										<%if(procNameList != null && procNameList.size() >0) {
									for(int i=0;i<procNameList.size();i++){
										lIdentifyPatternDTO = (IdentifyPatternDTO)procNameList.get(i); 
									%>
										<tr class="input">
											<td width="1%"><input type="checkbox"
												name="partialSP<%=i%>"
												<%if(lPartialSPList.contains((String)lIdentifyPatternDTO.getProcName())){ out.println("checked");} %>>&nbsp;
											</td>
											<td><%=lIdentifyPatternDTO.getProcName() %> <input
												type="hidden" name="partialSPName<%=i%>"
												id="partialSPName<%=i%>"
												value="<%=lIdentifyPatternDTO.getProcName()%>" /></td>
										</tr>
										<%} } %>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="1" align="center">&nbsp;</td>
							<td colspan="1" align="left"><input type="button"
								class="moreproject" value="View Report" id="pageSubmit"
								onclick="return submitIdentifyPattern();" >
								&nbsp;&nbsp;&nbsp;<input id="pageNext" type="button"
								class="moreproject" value="Proceed with Invoking Primary Tool"
								onclick="return identifyPatternNext();" style="display: none"
								disabled></td>
						</tr>
						<tr>
							<td colspan="2" align="center">&nbsp;</td>
						</tr>

						<tr>
							<td colspan="2" align="center"><div id="report1_link"
									style="visiblity: hidden; display: none;">
									&nbsp; <a class="moreproject2" style="cursor: hand"
										onclick="openPopUpReport();"><u><font color="blue">SP Call Tree Report</font></u> </a> </br> </br>


								</div></td>
								
								
						</tr>
					</table> <!--  page content ends -->
				</td>
				<!-- right pane contents  -->
				<td width="20%" valign="top"><jsp:include
						page="/web/jsp/tool/common/right_menu.jsp" flush="true" /></td>
			</tr>
			<tr>
				<td colspan="5"><jsp:include
						page="/web/jsp/tool/common/footer.jsp" flush="true" /></td>
			</tr>
		</table>
	</form>
</body>
</html>

</body>
</html>