<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.tcs.tools.web.dto.IdentifyPatternDTO"%>
<%@ page import="java.util.List,java.util.ArrayList,java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
	
	 function openPopUpReport(){
		window.open ("<%=filePath%>/tool/feCodeMigrationReports.action?submitMode=feDsqlPatternReport&projectId=<%=projectId%>&analysisType=<%=analysisType%>&seqNo=<%=seqNo%>&migrationMode=<%=migrationMode%>","repot1","menubar=0,resizable=1,scrollbars=1,width=850,height=600");
	 }

	function onLoadIdentifyPattern(){
		if(parseInt('<%=lPartialSPList.size()%>') > 0 ){
			changeAnalysisMode("Partial");
		}
		if(document.form1.retMsgToJsp.value != ""){
			document.getElementById("report1_link").style.visibility = "visible";
			document.getElementById("report1_link").style.display = "inline";
		}
		hideLoadingJquery();
		
	}
	function changeProjectName(){
		document.form1.submitMode.value = "changeProject";		
		document.form1.action='<%=filePath%>'+"/tool/frontEndIdentifyPatterns.action";
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
			return ;
		}else{
			document.getElementById("spListToSelect").style.visibility = "hidden";
			document.getElementById("spListToSelect").style.display = "none";
		}
	} 
	
	function submitIdentifyPattern(){
		
		if(document.form1.analysisType.value == "JAVA"){
			//alert("Please Select other Source Type...");
			//document.form1.analysisType.focus();
			//return false;
		}
		if(document.form1.projectId.value == ""){
			alert("Please enter a project name.");
			document.form1.projectId.focus();
			return false;
		} 
		
		if(document.form1.analysisType.value == ""){
			alert("Please Select Code Type to identify patterns.");
			document.form1.analysisType.focus();
			return false;
		}
		
		if(document.form1.migrationMode.value == ""){
			alert("Please enter a mode of migration.");
			document.form1.migrationMode.focus();
			return false;
		}
		var pMode = document.form1.migrationMode.value ;
		var partialSPSelectedCount = 0;
		var partialSPSelected = "";
		if(pMode == "Partial"){
			for(var x=0;x<parseInt('<%=procNameList.size()%>');x++){
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
		
		showLoadingJquery('<%=imagePath%>');
		document.form1.submitMode.value = "save";
		document.form1.action='<%=filePath%>'+"/tool/frontEndIdentifyPatterns.action";
		document.form1.submit();
		
	}
	</script>

</head>
<body onload="return onLoadIdentifyPattern();">



	<form action="" name="form1" method="post">
		<input type="hidden" name="submitMode" id="submitMode" value="">
		<input type="hidden" name="partialSPSelected" id="partialSPSelected"
			value=""> <input type="hidden" name="retMsgToJsp"
			id="retMsgToJsp" value="<%=msgToJsp %>">
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
							<td colspan="2">&nbsp;&nbsp;&nbsp;Identify Patterns</td>
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
									<%-- <option <%if("PR001".equalsIgnoreCase(projectId)){out.println("selected");} %> value="PR001">Prudential</option>
		      					<option <%if("PR002".equalsIgnoreCase(projectId)){out.println("selected");} %> value="PR002">CNA</option>      --%>
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
		      					%> %>
							</select></td>
						</tr>

						<tr height="25%">
							<td class="label1" nowrap width="25%">Pattern To identify in</td>
							<td nowrap width="75%"><select class="input1"
								name="analysisType">
									<option>--Select--</option>
									<option
										<%if("VB".equalsIgnoreCase(analysisType)){out.println("selected");} %>
										value="VB">VB</option>
									<option
										<%if("JAVA".equalsIgnoreCase(analysisType)){out.println("selected");} %>
										value="JAVA">Java</option>
							</select>
						</tr>

						<tr>
							<td class="label1">Migration Mode</td>
							<td><select class="input1" id="migrationMode"
								name="migrationMode"
								onchange="return changeAnalysisMode(this.value)">
									<option>--Select--</option>
									<option
										<% if("Bulk".equalsIgnoreCase(migrationMode)){out.println("selected");} %>
										value="Bulk">Bulk</option>
									<%-- <option <% if("Partial".equalsIgnoreCase(migrationMode)){out.println("selected");} %> value="Partial">Partial</option> --%>
							</select></td>
						</tr>




						<tr id="spListToSelect" style="visibility: hidden; display: none;">

							<td align="left" class="label1">Please select from the list
								of SP's</td>

							<td>
								<div style="overflow: scroll; height: 150px; width: 400px;">
									<table width="100%" cellspacing="2" cellpadding="2">
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
								class="moreproject" value="Submit" id="pageSubmit"
								onclick="return submitIdentifyPattern();">
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
										onclick="openPopUpReport();"><u><font color="blue">Dynamic SQL Patterns Report</font></u> </a>
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