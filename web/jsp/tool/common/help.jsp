<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head><!--  Tells ie 8 to display standard doctypes in ie 7 standard mode-->

<title>:::::db TransPlant::::</title>

<%
	String filePath= request.getContextPath();
	String cssPath=filePath+"/web/css";
	String jsPath=filePath+"/web/js";
	String imagePath=filePath+"/web/images";	
	%>
<script>
function ShowHide(divObj1,divObj2,divObj3) {

state = document.getElementById(divObj2).style.display;

if(state != null && state == "none")
{
document.getElementById(divObj1).className = "helphead";
document.getElementById(divObj2).style.display = "block";
}
else
{
document.getElementById(divObj1).className = "helphead";
document.getElementById(divObj2).style.display = "none";
}
}
</script>
</head>
<body>
<table width="100%" border=1 cellspacing="0" cellpadding="0"
		id="contentblank">
		<tr>
			<td colspan="3"><jsp:include
					page="/web/jsp/tool/common/header.jsp" flush="true" /></td>
		</tr>
		<tr>
			<td width="20%" valign="top"><jsp:include
					page="/web/jsp/tool/common/left_menu.jsp" flush="true" /></td>
			<td width="70%" valign="top">
				<!--  page content starts -->
			<table width="100%" border=0 cellspacing="2" cellpadding="2"
					valign="top">
					<TR>
			<td>
				<div id="">
				<a name="top"></a>
					<div class="midheadinghelp">
						<h2>Help Index</h2>
					</div>
					<br/><br/><br/>
					<div class="">
				<ul >
						<li class="hh"><div id="helphead1" class="helphead">
							<a href="#projects" title="Projects" class="helphead1">Projects</a>
						</div></li>
						<div id="helpindex1">
						
						<ul class="">
							<li ><a href="#projects1" class="helpindex1" id="show1" title="Click to view details">Create
									Migration Project</a></li>
							<%-- <li><a href="<%=filePath%>/tool/modifyProject.action?submitMode=reset">Manage Migration Project</a></li> --%>
							<li><a
								href="#projects2" class="helpindex1" id="show2" title="Click to view details">Manage
									Project Details</a></li>
							<li><a
								href="#projects3" class="helpindex1" id="show3" title="Click to view details">Manage
									Stored Procedure Repository</a></li>
						</ul>
						</div>
						<br/><br/>
						<li class="hh"><div id="helphead1">
							<a href="#inventory" title="Inventory Analytics"  class="helphead1">Inventory Analytics</a>
						</div></li>
						<div id="helpindex1">
						<ul class="">
						<li><a href="#inventory1" class="helpindex1" title="Click to view details">SP Call Tree</a></li>
							<li><a
								href="#inventory2"
								title="Click to view details" class="helpindex1">Source Database Analytics</a></li>
							<li><a
								href="#inventory3"
								title="Click to view details" class="helpindex1">Target Database Analytics</a></li>
							<li><a href="#inventory4" class="helpindex1">Stored
									Procedure Analytics</a></li>
							
							<!-- <li><a href="#">SP Analytics</a></li> -->
							<li><a href="#inventory5" class="helpindex1" title="Click to view details">Frontend Analytics</a></li>
							<!-- <li><a href="#">Script Analytics</a></li>
			<li><a href="#">Others</a></li> -->
						</ul>
</div>
<br/><br/>
					<li class="hh">	<div id="helphead1">
							<a href="#stored" title="Stored Procedure Migration"  class="helphead1">Stored
								Procedure Migration</a>
						</div></li>
						<div id="helpindex1">
						<ul class="">

							<!--<li><a href="identify_patterns.html">Validate SP Migration</a></li> -->
							<li><a href="#stored1" class="helpindex1" title="Click to view details">Identify
									Patterns</a></li>
							<%-- <li><a href="<%=filePath %>/tool/invokePrimaryTool.action?submitMode=init">Invoke Primary Tool</a></li> --%>
							<li><a
								href="#stored2" class="helpindex1" title="Click to view details">Download
									Target</a></li>
							<li><a href="#stored3" class="helpindex1" title="Click to view details">Analyze
									Gaps</a></li>
							<li><a
								href="#stored4" class="helpindex1" title="Click to view details">View Manual Modification</a></li>
							<li><a
								href="#stored5" class="helpindex1" title="Click to view details">Invoke
									Post Processor</a></li>
									<li><a
								href="#stored6" class="helpindex1" title="Click to view details">Search Stored Procedure</a></li>
							<li><a href="#stored7" class="helpindex1" title="Click to view details">Compile And Release</a></li>
							
						</ul>
</div>
<br/><br/>
						<li class="hh"><div id="helphead1">
							<a href="#frontend" title="Front-end Migration"  class="helphead1">Frontend Migration</a>
						</div></li>
						<div id="helpindex1">
						<ul class="">
							<li><a
								href="#frontend1" class="helpindex1" title="Click to view details">Upload
									Source code</a></li>
							<li><a
								href="#frontend2" class="helpindex1" title="Click to view details">Identify
									Patterns</a></li>
							<li><a
								href="#frontend3" class="helpindex1" title="Click to view details">Map
									to Target</a></li>
							
							<li><a
								href="#frontend4" class="helpindex1" title="Click to view details">Compile
									And Release</a></li>

						</ul>

						</div><br/><br/>
						<li class="hh"><div id="helphead1">
							<a href="#report" title="Reports"  class="helphead1">Reports</a>
						</div></li>
						<div id="helpindex1">
						<ul class="">
							<li><a href="#report1" class="helpindex1" title="Click to view details">SP Patterns Report</a></li>
							<li><a href="#report2" class="helpindex1" title="Click to view details">Frontend Patterns Report</a></li>
							<li><a href="#report3" class="helpindex1" title="Click to view details">Primary Tool Exception Report</a></li>
							<li><a href="#report4" class="helpindex1" title="Click to view details">Postprocessor Change Report</a></li>
							<li><a href="#report5" class="helpindex1" title="Click to view details">Compiler Report</a></li>
						</ul>

						</div>
<br/><br/>
						<li class="hh"><div id="helphead1">
							<a href="#migr" title="Schema/Data Migration"  class="helphead1">Schema/Data
								Migration</a>
						</div></li>
						<div id="helpindex1">
						<ul class="">
							<li><a href="#migr1" class="helpindex1" title="Click to view details">Process
									Input Files</a></li>
							<li><a href="#migr2" class="helpindex1" title="Click to view details">Validate
									Data Migration</a></li>
							<li><a href="#migr3" class="helpindex1" title="Click to view details">Generate Data Migration Scripts</a></li>
							<li><a href="#migr4" class="helpindex1" title="Click to view details">Generate Security Migration Scripts</a></li>
							<li><a href="#migr5" class="helpindex1" title="Click to view details">Invoke Schema Migration Scripts</a></li>
							<li><a href="#migr6" class="helpindex1" title="Click to view details">Invoke Data Migration Scripts</a></li>
							<li><a href="#migr7" class="helpindex1" title="Click to view details">Invoke Security Migration Scripts</a></li>
						</ul>
						</div>
						</ul>
					</div>

					
					</div>
					<br/><br/><br/><hr/>
					<div>
					
					<div class="helpindex2">
					<h2>
						<a name="projects" >Projects</a>
					</h2>
					<p >Project is created/edited under this option.</p>
					</div>
					
					
					<div class="helpindex3">
					<h4><a name="projects1" >Create Migration Project</a>
					
					</h4>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex3">
					<h4>
					<a name="projects2" >Manage Project Details</a>
					</h4>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex3">
					<h4>
				<a name="projects3" >Manage Stored Procedure Repository</a>
					</h4>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex2">
					<h2><a name="inventory" >Inventory Analytics</a>
					</h2>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex3">
					<h4><a name="inventory1" >SP Call Tree</a></h4>
					<p>Add Content Here</p>
					
					</div>
					<div class="helpindex3">
					<h4><a name="inventory2" >Source Database Analytics</a></h4>
					<p>Add Content Here</p>
					
					</div>
					<div class="helpindex3">
					<h4><a name="inventory3">Target Database Analytics</a></h4>
					<p>Add Content Here</p>
					
					</div>
					<div class="helpindex3">
					<h4><a name="inventory4" >Stored Procedure Analytics</a></h4>
					<p>Add Content Here</p>
					</div>
					<div class="helpindex3">
					<h4><a name="inventory5" >Frontend Analytics</a></h4>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex2">
					<h2><a name="stored">
				Stored Procedure Migration</a>
					</h2>
					<p>Add Content Here</p>
					</div>
					
					
					<div class="helpindex3">
					<h4><a name="stored1">Identify Patterns</a></h4>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex3">
					<h4><a name="stored2">Download Target</a></h4>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex3">
					<h4><a name="stored3">Analyze Gaps</a></h4>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex3">
					<h4><a name="stored4">View Manual Modification</a></h4>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex3">
					<h4><a name="stored5">Invoke Post Processor</a></h4>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex3">
					<h4><a name="stored6">Search Stored Procedure</a></h4>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex3">
					<h4><a name="stored7">Compile And Release</a></h4>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex2">
					<h2><a name="frontend">
					Frontend Migration</a>
					</h2>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex3">
					<h4><a name="frontend1">Upload Source code</a></h4>
					<p>Add Content Here</p>
					</div>
					<div class="helpindex3">
					<h4><a name="frontend2">Identify Patterns</a></h4>
					<p>Add Content Here</p>
					</div>
					<div class="helpindex3">
					<h4><a name="frontend3">Map to Target</a></h4>
					<p>Add Content Here</p>
					</div>
					<div class="helpindex3">
					<h4><a name="frontend4">Compile And Release</a></h4>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex2">
					<h2><a name="report">
					Reports</a>
					</h2>
					<p>Add Content Here</p>
					</div>
					
					<div class="helpindex3">
					<h4><a name="report1">SP Patterns Report</a></h4>
					<p></p>
					</div>
					<div class="helpindex3">
					<h4><a name="report2">Frontend Patterns Report</a></h4>
					<p></p>
					</div>
					<div class="helpindex3">
					<h4><a name="report3">Primary Tool Exception Report</a></h4>
					<p></p>
					</div>
					<div class="helpindex3">
					<h4><a name="report4">Postprocessor Change Report</a></h4>
					<p></p>
					</div>
					<div class="helpindex3">
					<h4><a name="report5">Compiler Report</a></h4>
					<p></p>
					</div>
					
					
					
					<div class="helpindex2">
					<h2><a name="migr">
				Schema/Data Migration</a>
					</h2>
					<p>Add Content Here</p>
					</div>
					<div class="helpindex3">
					<h4><a name="migr1">Process Input Files</a></h4>
					<p></p>
					</div>
					<div class="helpindex3">
					<h4><a name="migr2">Validate Data Migration</a></h4>
					<p></p>
					</div>
					<div class="helpindex3">
					<h4><a name="migr3">Generate Data Migration Scripts</a></h4>
					<p></p>
					</div>
					<div class="helpindex3">
					<h4><a name="migr4">Generate Security Migration Scripts</a></h4>
					<p></p>
					</div>
					<div class="helpindex3">
					<h4><a name="migr5">Invoke Schema Migration Scripts</a></h4>
					<p></p>
					</div>
					<div class="helpindex3">
					<h4><a name="migr6">Invoke Data Migration Scripts</a></h4>
					<p></p>
					</div>
					<div class="helpindex3">
					<h4><a name="migr7">Invoke Security Migration Scripts</a></h4>
					<p></p>
					</div>
					
					<div align="right" ><a href="#top">[&uarr; Top of Page]</a></div>
					</div>
			</td>
			
		</TR>
				</table> <!--  page content ends -->
			</td>
			<td width="20%" valign="top"><jsp:include
								page="/web/jsp/tool/common/right_menu.jsp" flush="true" /></td>
		</tr>
		<tr valign="top">
			<td colspan="5"><jsp:include
					page="/web/jsp/tool/common/footer.jsp" flush="true" /></td>
		</tr>
	</table>

</body>
</html>