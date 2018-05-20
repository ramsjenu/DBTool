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
	
	 
	 String msgToJsp ="";
	 if(request.getAttribute("msgToJsp") != null){
		 msgToJsp = (String)request.getAttribute("msgToJsp");
	 }
	 
	 HashMap inventoryMap = new HashMap();
		if(request.getAttribute("inventoryMap") != null){
			inventoryMap = (HashMap)request.getAttribute("inventoryMap");
		}
		
		HashMap projectNameIdMap = new HashMap();
		if(request.getAttribute("projectNameIdMap") != null){
			projectNameIdMap = (HashMap)request.getAttribute("projectNameIdMap");
		}
		

		String projectName = "";
		if(request.getAttribute("projectName") != null){
			projectName = (String)request.getAttribute("projectName");
		}
		
		
	%>
<script>
	function loadChartReport(){
		if(document.form1.projectName.value != ""){
			document.getElementById("data_table_contents").style.visibility = "visible";
			document.getElementById("data_table_contents").style.display = "inline";
			document.getElementById("table_header").style.visibility = "visible";
			document.getElementById("table_header").style.display = "inline";
			
			document.getElementById("consolidatedDownload").style.visibility = "visible";
			document.getElementById("consolidatedDownload").style.display = "inline";
			
			
			
			return ;
		}
	}
	 function openPopUpReport(paramValue,pProjectId){
		window.open ("<%=filePath%>/tool/chartReport.action?submitMode=openChart&mode="+paramValue+"&paramProjectId="+pProjectId,"report_pie","menubar=0,resizable=1,scrollbars=1,width=800,height=600");
	 }
	 function downloadPopUpReport(paramValue,pProjectId){
			window.open ("<%=filePath%>/tool/chartReport.action?submitMode=downChart&mode="+paramValue+"&paramProjectId="+pProjectId,"report_pie1","menubar=0,resizable=1,scrollbars=1,width=800,height=600");
		 }
	function downloadFile(paramValue){
		document.form1.action='<%=filePath%>'+"/tool/inventoryAnalytics.action?submitMode=download&type="+paramValue;
		document.form1.submit();
		
	}
	function onChnageProjectId(){
		//alert(document.form1.projectName.value);
		if(document.form1.projectName.value != ""){
			document.form1.action="<%=filePath%>/tool/chartReport.action";
			document.form1.submit();
		} 
		
	}
	
	 function downloadCompletePopUpReport(paramValue,pProjectId){
			window.open ("<%=filePath%>/tool/chartReport.action?submitMode=downChartComplete&mode="+paramValue+"&paramProjectId="+pProjectId,"report_pie2","menubar=0,resizable=1,scrollbars=1,width=800,height=600");
		 }
	
	</script>

</head>
<body onload="loadChartReport()">



	<form action="" name="form1" method="post">


		<input type="hidden" name="retMsgToJsp" id="retMsgToJsp"
			value="<%=msgToJsp %>">
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
							<td colspan="2">&nbsp;&nbsp;&nbsp;Stored Procedure Analytics</td>
						</tr>

						<tr>
							<td class="label1" width="25%" nowrap>Project Name</td>
							<td width="75%"><select class="input" name="projectName"
								id="projectName" onchange="return onChnageProjectId();">
									<option value="">--Select--</option>
									<%
		      					
		      					
		      					
		      					Iterator it = projectNameIdMap.entrySet().iterator(); 
		      					while (it.hasNext()) { 
		      					Map.Entry pairs = (Map.Entry)it.next(); 
		      					
		      					
		      					%>
									<option value="<%=pairs.getKey()%>"
										<% if(projectName.equalsIgnoreCase((String)pairs.getKey())){out.println("selected");} %>><%=pairs.getValue() %></option>
									<%
		      					//System.out.println(pairs.getKey() + " = " + pairs.getValue()); 
		      					it.remove(); // avoids a ConcurrentModificationException 
		      					}
		      					%> %>

							</select></td>
						</tr>


						<tr height="20">
							<td colspan="2" align="center">&nbsp;
								<div id="table_header" style="display: none;">
									<table width="97%" class="subtable" height="20" align="left"
										cellpadding="0" cellspacing="2">
										<tr class="tableheader">
											<td width="5%" align="left">&nbsp;S.No</td>
											<td width="49%" align="left">&nbsp;Query Name</td>
											<td align="left">&nbsp;Report</td>

										</tr>
									</table>
								</div>
							</td>
						</tr>

						<tr height="20">
							<td colspan="2">
								<div id="data_table_contents" style="display: none;" align="center">
									<div
										style="overflow-y: scroll; overflow: -moz-scrollbars-vertical; height: 250px;">
										<table width="97%" border=0 class="subtable">

											<tr class="tablerowodd" align="left">
												<td width="5%" align="left">&nbsp;1</td>
												<td width="49%">SQL Statement Pattern Analysis</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="spPatternReport"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="spPatternReport"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tableroweven" align="left">
												<td>&nbsp;2</td>
												<td>SQL Statement Pattern Analysis SP wise</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="spPatternReportProcCount"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="spPatternReportProcCount"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>


											<tr class="tablerowodd" align="left">
												<td>&nbsp;3</td>
												<td>SQL Statement Pattern Analysis SP wise Complexity</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="patternCountInSp"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="patternCountInSp"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tableroweven" align="left">
												<td>&nbsp;4</td>
												<td>Datatype Pattern Analysis</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="datatypePatternAnalysys"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="datatypePatternAnalysys"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tablerowodd" align="left">
												<td>&nbsp;5</td>
												<td>Datatype Pattern Analysis SP Wise</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="datatypePatternAnalysysSPWise"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="datatypePatternAnalysysSPWise"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tableroweven" align="left">
												<td>&nbsp;6</td>
												<td>Datatype Pattern Analysis SP Wise Complexity</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="datatypePatternAnalysysSPWiseComplexity"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="datatypePatternAnalysysSPWiseComplexity"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tablerowodd" align="left">
												<td>&nbsp;7</td>
												<td>Datatype Pattern Analysis For Impacted Datatypes</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="datatypePatternAnalysysForImpacted"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="datatypePatternAnalysysForImpacted"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tableroweven" align="left">
												<td>&nbsp;8</td>
												<td>Datatype Pattern Analysis Impacted vs Non-Impacted</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="datatypeImpactVsNonImpact"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="datatypeImpactVsNonImpact"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tablerowodd" align="left">
												<td>&nbsp;9</td>
												<td>Built-in Function Pattern Analysis</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="functionPatternAnalysys"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="functionPatternAnalysys"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tableroweven" align="left">
												<td>&nbsp;10</td>
												<td>Built-in Function Pattern Analysis SP Wise</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="functionPatternAnalysysSPWise"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="functionPatternAnalysysSPWise"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tablerowodd" align="left">
												<td>&nbsp;11</td>
												<td>Built-in Function Pattern Analysis SP Wise
													Complexity</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="functionPatternAnalysysSPWiseComplexity"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="functionPatternAnalysysSPWiseComplexity"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tableroweven" align="left">
												<td>&nbsp;12</td>
												<td>Global Variable Pattern Analysis</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="gVarPatternAnalysys"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="gVarPatternAnalysys"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tablerowodd" align="left">
												<td>&nbsp;13</td>
												<td>Global Variable Pattern Analysis SP Wise</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="gVarPatternAnalysysSPWise"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="gVarPatternAnalysysSPWise"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tableroweven" align="left">
												<td>&nbsp;14</td>
												<td>Global Variable Pattern Analysis SP Wise Complexity</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="gVarPatternAnalysysSPWiseComplexity"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="gVarPatternAnalysysSPWiseComplexity"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tablerowodd" align="left">
												<td>&nbsp;15</td>
												<td>Operator Pattern Analysis</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="operatorPatternAnalysys"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="operatorPatternAnalysys"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tableroweven" align="left">
												<td>&nbsp;16</td>
												<td>Operator Pattern Analysis SP Wise</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="operatorPatternAnalysysSPWise"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="operatorPatternAnalysysSPWise"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tablerowodd" align="left">
												<td>&nbsp;17</td>
												<td>Keyword Pattern Analysis</td>
												<td><a class="moreproject3" class="moreproject2"
													style="cursor: hand"
													onclick="openPopUpReport('<%="keywordPatternAnalysys"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" class="moreproject2"
													style="cursor: hand"
													onclick="downloadPopUpReport('<%="keywordPatternAnalysys"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>

											<tr class="tableroweven" align="left">
												<td>&nbsp;18</td>
												<td>Keyword Pattern Analysis SP Wise</td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="openPopUpReport('<%="keywordPatternAnalysysSPWise"%>','<%=projectName%>');"><u><font
															color="blue">View Report</font></u> </a></td>
												<td><a class="moreproject3" style="cursor: hand"
													onclick="downloadPopUpReport('<%="keywordPatternAnalysysSPWise"%>','<%=projectName%>');"><u><font
															color="blue">Download Report</font></u> </a></td>
											</tr>



										</table>

									</div>
								</div>
							</td>
						</tr>

						<tr>
							<td colspan="2">&nbsp;</td>
						</tr>
						<tr>
							<td align="center" colspan="2"><div
									id="consolidatedDownload" style="display: none;">
									<a class="moreproject2" style="cursor: hand"
										onclick="downloadCompletePopUpReport('<%=""%>','<%=projectName%>');"><u><font
											color="blue">Download Consolidated Report</font></u> </a>
								</div></td>
						</tr>



						<tr>
							<td colspan="2" align="center">&nbsp;</td>
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