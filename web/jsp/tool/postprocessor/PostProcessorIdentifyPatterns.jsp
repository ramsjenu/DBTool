<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.tcs.tools.web.dto.IdentifyPatternDTO"%>
<%@ page import="com.tcs.tools.web.dto.StoredProceduresDetailsDTO"%>
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
	 
	
		String submitMode = "";
		if(request.getAttribute("submitMode") != null){
			submitMode = (String)request.getAttribute("submitMode");
		}
		
		 String projectId ="";
		  if(request.getAttribute("projectId") != null){
			  projectId = (String)request.getAttribute("projectId");
		 } 
		
		  HashMap projectNameIdMap = new HashMap();
			if(request.getAttribute("projectNameIdMap") != null){
				projectNameIdMap = (HashMap)request.getAttribute("projectNameIdMap");
			}
			
		List projectSpDetailsList = new ArrayList();	
		if(request.getAttribute("projectSpDetailsList") != null){
			projectSpDetailsList = (List)request.getAttribute("projectSpDetailsList");
		}
	%>
<script>

	function openStatusWindow(pProcName,iVal)	{		
		//alert("pProcName--->"+'<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=enterPatterns&projectId="+document.form1.projectId.value+"&procName="+pProcName);
		//window.open('<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=enterPatterns&projectId="+document.form1.projectId.value+"&procName="+pProcName);
		$(".buttonClass").colorbox({iframe:true, innerWidth:750, innerHeight:600});
		
		<%-- window.showModalDialog('<%=filePath%>'+"/tool/statusDisplayAjax.action?submitMode=init&runId="+runId,"name","dialogWidth:500px,dialogHeight:300px,scrollbars=1,resizable:0,edge:sunken,center:yes"); --%>
		
		//document.form1.openPopUp.href='<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=enterPatterns&projectId="+document.form1.projectId.value;
		//document.getElementById("openPopUp"+iVal).href='<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=enterPatterns&projectId="+document.form1.projectId.value+"&procName="+pProcName;
	}
	
	
	
	function changeProjectName(){
		//alert(document.form1.projectId.value);
		if(document.form1.projectId.value != ""){
			document.form1.action="<%=filePath%>/tool/postProcessorIdentifyPattern.action?submitMode=changeProject";
			document.form1.submit();
		} else{
		alert("Please select a project...");
		return false;
		}
		
	}
	
function submitFrom(){
		
		if(document.form1.projectId.value == ""){
			alert("Please enter a project name.");
			document.form1.projectId.focus();
			return false;
		}
		var confRet = confirm("Are sure you want to submit,Click yes to continue...  ");
		if(confRet == false){
			return false;
		}
		
		
		<%-- $(".pageSubmit").colorbox({iframe:true, innerWidth:1300, innerHeight:600});
		document.getElementById("pageSubmit").href='<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=openMainTool&projectId="+document.form1.projectId.value; --%>
		f_open_window_max( '<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=openMainTool&projectId="+document.form1.projectId.value, "replacement_tool_new" );
		
		//window.open ('<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=openMainTool&projectId"+document.form1.projectId.value,"replacement_tool_new","menubar=0,resizable=1,width=1050,height=800");
		//document.form1.action='<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=openMainTool";
		//document.form1.submit();
		
		
	}
	
function enableButton(){
	document.getElementById("pageSubmit").disabled = false;
	document.getElementById("pageSubmit").style.background = "#8c2c02";
	document.getElementById("pageSubmit").style.color = "#f4e7bd";
	document.getElementById("pageSubmit").style.cursor = "pointer";
}
	
function f_open_window_max( aURL , aWinName ){
   var sOptions;

   sOptions = 'status=yes,menubar=yes,scrollbars=yes,resizable=yes,toolbar=yes';
   sOptions = sOptions + ',width=' + (screen.availWidth - 10).toString();
   sOptions = sOptions + ',height=' + (screen.availHeight - 122).toString();
   sOptions = sOptions + ',screenX=0,screenY=0,left=0,top=0,toolbar=no,menubar=no';
   
   wOpen = window.open( '', aWinName, sOptions );
   wOpen.location = aURL;
   wOpen.focus();
   wOpen.moveTo( 0, 0 );
   wOpen.resizeTo( screen.availWidth, screen.availHeight );
   return wOpen;
}

	
	</script>

</head>
<body onload="">



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
							<td colspan="2">&nbsp;&nbsp;&nbsp;Post Processor Tool</td>
						</tr>

						<tr>
							<td colspan="2">
								<table border="0" width="100%">
									<tr>
										<td class="label1">Project Name</td>
										<td><select class="input" name="projectId" id="projectId"<%-- onChange="return changeProjectName();" --%> onchange="return enableButton()" >
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
										<td colspan="2">&nbsp;</td>
									</tr>
									<tr>
										<td colspan="2">&nbsp;</td>
									</tr>
									<tr>
										<td></td>
										<td><input type="button" class="moreproject"
											value="Invoke Post Processor" id="pageSubmit"
											onclick="return submitFrom();" disabled="disabled" style="background-color:gray ;color: #000101;cursor: text; " > &nbsp;&nbsp;&nbsp;<input
											id="pageNext" type="button" class="moreproject"
											value="Proceed with Invoking Primary Tool"
											onclick="return identifyPatternNext();" style="display: none"
											disabled></td>
									</tr>

									<tr>
										<td colspan="2">&nbsp;</td>
									</tr>
									<%-- <%if(projectSpDetailsList != null && projectSpDetailsList.size() > 0){ %>
					      		<tr><td colspan="2" align="center">&nbsp;
									<table width="100%" class="subtable" cellpadding="0" cellspacing="2">
										<tr class="tableheader">
											<td width="5%" align="left">&nbsp;S.No</td>
											<td width="44%" align="left">&nbsp;Proc Name</td>
											<td align="left">&nbsp;</td>
											<!-- <td align="left">&nbsp;Download Modified File</td> -->
										</tr>
									</table>
									<div align="left" style="overflow: auto;height:270px;width:100%;" class="subtable">
											<table width="97%">
														<%
														//System.out.println("::::inventoryMap:::"+inventoryMap.size());
															StoredProceduresDetailsDTO lStoredProceduresDetailsDTO = new StoredProceduresDetailsDTO();
															String lCssClass="tablerowodd";
															String href = "";
															if(projectSpDetailsList != null && projectSpDetailsList.size() > 0){
																for(int i=0;i<projectSpDetailsList.size();i++){	
																	lStoredProceduresDetailsDTO =(StoredProceduresDetailsDTO)projectSpDetailsList.get(i);
																	if( i%2 ==0){
																		lCssClass="tablerowodd";
																	}else{
																		lCssClass="tableroweven";
																	}
																	href = filePath+"/tool/postProcessorIdentifyPattern.action?submitMode=enterPatterns&projectId="+projectId+"&procName="+lStoredProceduresDetailsDTO.getProcName();
											      					%>
											      					<tr class="<%=lCssClass%>">
											      					<td width="5%" align="left"><%=i+1 %></td>
											      					<td width="45%" align="left">&nbsp;<%=lStoredProceduresDetailsDTO.getProcName()%></td>
																	<td>&nbsp;<span class="buttonClass" name="openPopUp<%=i%>" id="openPopUp<%=i%>" href="<%=href %>"style="cursor:hand" onclick="openStatusWindow('<%=lStoredProceduresDetailsDTO.getProcName() %>')"><u><font color="blue">Invoke Post Processor <!-- View/Download Target Code --></font></u> </span> </td>
																	<!-- <td>&nbsp;</td> -->
																	</tr>
													
											      					
											      					<%
											      					//System.out.println(pairs.getKey() + " = " + pairs.getValue()); 
											      					//it.remove(); // avoids a ConcurrentModificationException
											      					//i++;
										      						}
																}
										      					%>
										
												</tr>
												
											</table>  
											</div>
										</td> 
									</tr>
									
									<%} %> --%>


								</table>
							</td>
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