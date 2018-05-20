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
		String projectId = "";
		if(request.getAttribute("projectId") != null){
			projectId = (String)request.getAttribute("projectId");
		}
		
		String submitMode = "";
		if(request.getAttribute("submitMode") != null){
			submitMode = (String)request.getAttribute("submitMode");
		}
		
		
		
	%>
<script>
	function loadChartReport(){
		if(document.form1.projectName.value != ""){
			document.getElementById("data_table_contents").style.visibility = "visible";
			document.getElementById("data_table_contents").style.display = "inline";
			document.getElementById("table_header").style.visibility = "visible";
			document.getElementById("table_header").style.display = "inline";
			
			return ;
		}
	}
	 function openPopUpReport(paramValue,pProjectId){
		window.open ("<%=filePath%>/tool/chartReport.action?submitMode=openChart&mode="+paramValue+"&paramProjectId="+pProjectId,"report_pie","menubar=0,resizable=1,scrollbars=1,width=800,height=600");
	 }
	 function downloadPopUpReport(paramValue,pProjectId){
			window.open ("<%=filePath%>/tool/chartReport.action?submitMode=downChart&mode="+paramValue+"&paramProjectId="+pProjectId,"report_pie","menubar=0,resizable=1,scrollbars=1,width=800,height=600");
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
	
	function checkFileExtension_xls(upfile){
		
		var file = upfile.value; 
		//alert("inside extn check method..."+file);
		if(file.length == 0){
			//alert(":::stage1::::");
			return true;
		}
		if(file.lastIndexOf(".xls") >= 0 || file.lastIndexOf(".XLS") >= 0)
		{
			//alert(":::stage2::::");
			return true;
		}
		else{
			//alert(":::stage3::::");
			alert("Please upload *.xls file only");	
			upfile.select();					
			return false;
		}
	}
	
function checkFileExtension_xlsx(upfile){
		
		var file = upfile.value; 
		//alert("inside extn check method..."+file);
		if(file.length == 0){
			//alert(":::stage1::::");
			return true;
		}
		if(file.lastIndexOf(".xlsx") >= 0 || file.lastIndexOf(".XLSX") >= 0)
		{
			//alert(":::stage2::::");
			return true;
		}
		else{
			//alert(":::stage3::::");
			//alert("Please upload *.xls file only");	
			//upfile.select();					
			return false;
		}
	}

	
	 function downloadCompletePopUpReport(paramValue,pProjectId){
			window.open ("<%=filePath%>/tool/chartReport.action?submitMode=downChartComplete&mode="+paramValue+"&paramProjectId="+pProjectId,"report_pie","menubar=0,resizable=1,scrollbars=1,width=800,height=600");
		 }
	function submitIdmtDefaultCoulmns(){
		//alert(document.form1.idmtSelect.SelectedValue);
		
		var radioSelectedValue = "";

		for( i = 0; i < document.form1.idmtSelect.length; i++ )
		{
		if( document.form1.idmtSelect[i].checked == true )
			radioSelectedValue = document.form1.idmtSelect[i].value;
		}
		//alert( "radioSelectedValue = " + radioSelectedValue );
		if(radioSelectedValue == ""){
			alert("Please select a option from the radio butons to run....");
			return false;
		}
		
		if(radioSelectedValue == "SPL_CHAR_TABLE"){
			
			if(checkFileExtension_xlsx(document.form1.sourceSPUpFile) == true){
				alert("Pls upload a .xls file ....");
				document.form1.sourceSPUpFile.select();
				return false;
			}
			
			if(checkFileExtension_xls(document.form1.sourceSPUpFile) == false){
				return false;
			}
		}
		
		if(radioSelectedValue == "SPLIT_DDLS"){
			
			if(checkFileExtension_xlsx(document.form1.upFileTwo) == true){
				alert("Pls upload a .xls file ....");
				document.form1.upFileTwo.select();
				return false;
			}
			
			if(checkFileExtension_xls(document.form1.upFileTwo) == false){
				return false;
			}
		}
		
		
		
		
		if(document.form1.sourceSPUpFile.value != ""){
			document.form1.action="<%=filePath %>/tool/idmtIssuesFixing.action?submitMode="+radioSelectedValue;
			document.form1.submit();
		}else{
			alert("Please upload a file...");
		} 
	}
	
	function loadIdmtToolOptions(){
		//document.getElementById("dummyDiv").style.display="inline";
		if('<%=submitMode%>' =="REMOVE_SCHEMA"){
			document.getElementById("thirdDiv").style.display="inline";
			//document.getElementById("dummyDiv").style.display="none";
		}else if('<%=submitMode%>' =="SPLIT_DDLS"){
			document.getElementById("secondDiv").style.display="inline";
			document.getElementById("thirdDiv").style.display="none";
			//document.getElementById("dummyDiv").style.display="none";
		}
	}
	
	function clickOptionItem(param){
		emptyElements();
		//alert(":::option value:::"+param);
		if(param =="REMOVE_SCHEMA"){
			document.getElementById("thirdDiv").style.display="inline";
			document.getElementById("secondDiv").style.display="none";
			//document.getElementById("dummyDiv").style.display="none";
		}else if(param =="SPLIT_DDLS"){
			document.getElementById("secondDiv").style.display="inline";
			document.getElementById("thirdDiv").style.display="none";
			//document.getElementById("dummyDiv").style.display="none";
		}else{
			document.getElementById("secondDiv").style.display="none";
			document.getElementById("thirdDiv").style.display="none";
		}
		
		
		
	}
	
	function emptyElements(){
		//alert("::::emty elements:122::"+document.getElementById("sourceSPUpFile").value);
		//document.getElementById('sourceSPUpFile').innerHTML ="";
		//document.getElementById("sourceSPUpFile").reset();
		document.getElementById("file1Div").innerHTML ="<input class=\"input\" type=\"file\" name=\"sourceSPUpFile\" id=\"sourceSPUpFile\">";
		document.getElementById("file2Div").innerHTML ="<input class=\"input\" type=\"file\" name=\"upFileTwo\" id=\"upFileTwo\">";

		

		//alert("::::emty elements after:::"+document.getElementById("sourceSPUpFile").value);
//		document.getElementById("upFileTwo").form.reset();
		document.getElementById("textBoxOne").value="";
	}
	
	function downloadSybaseSource(){
		
		if(document.form1.projectId.value == ""){
			alert("Please select a project...");
			document.form1.projectId.focus();
			return false;
		}
		
		document.form1.action="<%=filePath %>/tool/idmtIssuesFixing.action?submitMode=downloadSybaseSource";
		document.form1.submit();
	}
	</script>

</head>
<body onload="return loadIdmtToolOptions()">



	<form action="" name="form1" method="post" method="post"
		enctype="multipart/form-data">


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
							<td colspan="2">&nbsp;&nbsp;&nbsp;Fix IDMT Issues</td>
						</tr>

						<tr>
							<td colspan="2">
								<table width="100%">
									<tr>
										<td colspan="2" align="center">&nbsp;</td>
									</tr>
									<tr>
										<td width="25%">
											<table class="subtable" width="100%">
												<tr class="label1">
													<td><input type="radio" name="idmtSelect"
														value="DEFAULT_COLUMNS"
														onclick="return clickOptionItem(this.value);"
														<%if("DEFAULT_COLUMNS".equalsIgnoreCase(submitMode)){out.println("checked");} %>>&nbsp;Default
														Columns</td>
												</tr>
												<tr class="label1">
													<td><input type="radio" name="idmtSelect"
														value="SPL_CHAR_TABLE"
														onclick="return clickOptionItem(this.value);"
														<%if("SPL_CHAR_TABLE".equalsIgnoreCase(submitMode)){out.println("checked");} %>>&nbsp;SPL
														Char Table</td>
												</tr>
												<tr class="label1">
													<td><input type="radio" name="idmtSelect"
														value="REMOVE_DOUBLE_QUOTES"
														onclick="return clickOptionItem(this.value);"
														<%if("REMOVE_DOUBLE_QUOTES".equalsIgnoreCase(submitMode)){out.println("checked");} %>>&nbsp;Remove
														Double Quotes</td>
												</tr>
												<tr class="label1">
													<td><input type="radio" name="idmtSelect"
														value="NAMING_CONVENTIONS"
														onclick="return clickOptionItem(this.value);"
														<%if("NAMING_CONVENTIONS".equalsIgnoreCase(submitMode)){out.println("checked");} %>>&nbsp;Naming
														Conventions</td>
												</tr>
												<tr class="label1">
													<td><input type="radio" name="idmtSelect"
														value="FIX_IDENTITY"
														onclick="return clickOptionItem(this.value);"
														<%if("FIX_IDENTITY".equalsIgnoreCase(submitMode)){out.println("checked");} %>>&nbsp;Fix
														Identity</td>
												</tr>
												<tr class="label1">
													<td><input type="radio" name="idmtSelect"
														value="REMOVE_SCHEMA"
														onclick="return clickOptionItem(this.value);"
														<%if("REMOVE_SCHEMA".equalsIgnoreCase(submitMode)){out.println("checked");} %>>&nbsp;Remove
														Schema</td>
												</tr>
												<tr class="label1">
													<td><input type="radio" name="idmtSelect"
														value="SPLIT_DDLS"
														onclick="return clickOptionItem(this.value);"
														<%if("SPLIT_DDLS".equalsIgnoreCase(submitMode)){out.println("checked");} %>>&nbsp;Split
														DDL's</td>
												</tr>
												<tr class="label1">
													<td><input type="radio" name="idmtSelect"
														value="CREATE_DROPS"
														onclick="return clickOptionItem(this.value);"
														<%if("CREATE_DROPS".equalsIgnoreCase(submitMode)){out.println("checked");} %>>&nbsp;Create
														Drops</td>
												</tr>

											</table>
										</td>
										<td>
											<table class="subtable" width="100%" border="0"
												height="200px" valign="top">

												<tr height="20">
													<td valign="top">
														<table width="100%" valign="top">
															<tr>
																<td width="25%" class="label1">Source File<br></td>
																<td width="75%" align="left">
																	<div id="file1Div">
																		<input class="input" type="file" name="sourceSPUpFile"
																			id="sourceSPUpFile">
																	</div>
																</td>
															</tr>
														</table>
													</td>
												</tr>

												<tr>
													<td>
														<div id="secondDiv" style="display: none">
															<table width="100%" valign="top">
																<tr>
																	<td width="25%" class="label1">Excel File<br></td>
																	<td width="75%" align="left">
																		<div id="file2Div">
																			<input class="input" type="file" name="upFileTwo"
																				id="upFileTwo">
																		</div>
																	</td>
																</tr>
															</table>
														</div>
													</td>
												</tr>

												<tr>
													<td>
														<div id="thirdDiv" style="display: none">
															<table width="100%" valign="top">
																<tr>
																	<td width="25%" class="label1">Text Replacement<br></td>
																	<td width="75%" align="left"><input class="input"
																		type="text" name="textBoxOne" id="textBoxOne">
																	</td>
																</tr>
															</table>
														</div>
													</td>
												</tr>



												<tr>
													<td colspan="2" align="center">&nbsp; <input
														type="button" class="moreproject" value="Submit"
														onclick="return submitIdmtDefaultCoulmns();"></td>
												</tr>
											</table>
										</td>
									</tr>


									<!-- <tr>
					      			<td width="15%" class="label1">Source File<br></td>
					      			<td width="85%" align="left"> 
					      				<input class="input" type="file" name="sourceSPUpFile" id="sourceSPUpFile">
					      			</td> 
					      		</tr>
					      		
					      		<tr><td colspan="2" align="center">&nbsp;  </td> </tr>
					      		
					      		<tr>
					      			<td width="15%" align="right" ></td> 
					      			<td>&nbsp; <input type="button" class="moreproject" value="Submit" onclick="return submitIdmtDefaultCoulmns();"></td>
					      		</tr> -->
								</table>

							</td>
						</tr>


						<tr>
							<td colspan="2" align="center">&nbsp;</td>
						</tr>
						<tr class="pageheadernoWidth">
							<td colspan="2">&nbsp;&nbsp;&nbsp;Generate Sybase Source</td>
						</tr>
						<tr>
							<td colspan="2" align="center">&nbsp;</td>
						</tr>
						<tr>
							<td class="label1">Project Name</td>
							<td><select class="input" name="projectId" id="projectId">
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
						<tr>
							<td colspan="2" align="center">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="1" align="center">&nbsp;</td>
							<td align="left"><a class="moreproject2"
								style="cursor: hand" onclick="downloadSybaseSource();"><u><font
										color="blue">Click Here to Download Sybase DDL's</font></u> </a></td>
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