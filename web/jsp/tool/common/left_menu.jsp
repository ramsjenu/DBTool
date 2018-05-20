<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>
<%
String filePath= request.getContextPath();
String cssPath=filePath+"/web/css";
String jsPath=filePath+"/web/js";
String imagePath=filePath+"/web/images";

%>

<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>:::::db TransPlant::::</title>
<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />
<link rel="icon" href="images/icon.ico" />
<!-- <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script> -->
<%-- <script type="text/javascript" src="<%=jsPath %>/jquery-1.6.2.min.js"></script> --%>
<script type="text/javascript" src="<%=jsPath %>/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=jsPath %>/menu/ddaccordion.js"> </script>
<script type="text/javascript" src="<%=jsPath %>/custom/common.js"></script>

<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script> -->
<script type="text/javascript"
	src="<%=jsPath %>/jquery.colorbox_closebutton.js"></script>
<link href="<%=cssPath%>/colorbox.css" rel="stylesheet" type="text/css" />


<!-- for light box on mosue over -->
<link rel="stylesheet" href="<%=cssPath%>/jquery.tooltip.css" />
<script src="<%=jsPath %>/lib/jquery.bgiframe.js" type="text/javascript"></script>
<script src="<%=jsPath %>/lib/jquery.dimensions.js"
	type="text/javascript"></script>
<script src="<%=jsPath %>/jquery.tooltip.js" type="text/javascript"></script>
<!-- for light box on mosue over -->

<%-- <script type="text/javascript" src="<%=jsPath%>/menu/multi-level-menu-html.js"></script>  --%>

<script type="text/javascript">

//Initialize Arrow Side Menu:
ddaccordion.init({
	headerclass: "menuheaders", //Shared CSS class name of headers group
	contentclass: "menucontents", //Shared CSS class name of contents group
	revealtype: "clickgo", //Reveal content when user clicks or onmouseover the header? Valid value: "click", or "mouseover"
	mouseoverdelay: 200, //if revealtype="mouseover", set delay in milliseconds before header expands onMouseover
	collapseprev: true, //Collapse previous content (so only one open at any time)? true/false 
	defaultexpanded: [0], //index of content(s) open by default [index1, index2, etc]. [] denotes no content.
	onemustopen: false, //Specify whether at least one header should be open always (so never all headers closed)
	animatedefault: false, //Should contents open by default be animated into view?
	persiststate: true, //persist state of opened contents within browser session?
	toggleclass: ["unselected", "selected"], //Two CSS classes to be applied to the header when it's collapsed and expanded, respectively ["class1", "class2"]
	togglehtml: ["none", "", ""], //Additional HTML added to the header when it's collapsed and expanded, respectively  ["position", "html1", "html2"] (see docs)
	animatespeed: 500, //speed of animation: integer in milliseconds (ie: 200), or keywords "fast", "normal", or "slow"
	oninit:function(expandedindices){ //custom code to run when headers have initalized
		//do nothing
	},
	onopenclose:function(header, index, state, isuseractivated){ //custom code to run whenever a header is opened or closed
		//do nothing
	}
})

</script>





</head>

<body>
	<table width="100%" cellspacing="0" cellpadding="0">
		<TR>
			<td>
				<div id="contentleft">

					<div
						style="text-decoration: bold; color: WHITE; font: normal 13px Verdana, Arial, Helvetica, sans-serif; height: 20px">
						<b>Quick Links</b>
					</div>

					<div class="arrowsidemenu">

						<div class="menuheaders">
							<a href="#" title="Projects">Projects</a>
						</div>
						<ul class="menucontents">
							<li ><a href="<%=filePath%>/tool/creatingProject.action">Create
									Migration Project</a></li>
							<%-- <li><a href="<%=filePath%>/tool/modifyProject.action?submitMode=reset">Manage Migration Project</a></li> --%>
							<li><a
								href="<%=filePath%>/tool/modifyProjectDetails.action?submitMode=reset">Manage
									Project Details</a></li>
							<li><a
								href="<%=filePath%>/tool/modifyProjectSPUpload.action?submitMode=reset">Manage
									Stored Procedure Repository</a></li>
						</ul>

						<div class="menuheaders">
							<a href="#" title="Inventory Analytics">Inventory Analytics</a>
						</div>
						<ul class="menucontents">
						<li><a href="<%=filePath %>/tool/SpCallTreeFirstLevel.action">SP Call Tree</a></li>
							<li><a
								href="<%=filePath %>/tool/inventoryAnalytics.action?submitMode=view"
								title="Click Here">Source Database Analytics</a></li>
							<li><a
								href="<%=filePath %>/tool/Db2DatabaseAnalytics.action?submitMode=view"
								title="Click Here">Target Database Analytics</a></li>
							<li><a href="<%=filePath %>/tool/chartReport.action">Stored
									Procedure Analytics</a></li>
							
							<!-- <li><a href="#">SP Analytics</a></li> -->
							<li><a href="#">Frontend Analytics</a></li>
							<!-- <li><a href="#">Script Analytics</a></li>
			<li><a href="#">Others</a></li> -->
						</ul>

						<div class="menuheaders">
							<a href="#" title="Stored Procedure Migration">Stored
								Procedure Migration</a>
						</div>
						<ul class="menucontents">

							<!--<li><a href="identify_patterns.html">Validate SP Migration</a></li> -->
							<li><a href="<%=filePath %>/tool/identifyPattern.action">Identify
									Patterns</a></li>
							<%-- <li><a href="<%=filePath %>/tool/invokePrimaryTool.action?submitMode=init">Invoke Primary Tool</a></li> --%>
							<li><a
								href="<%=filePath %>/tool/invokePrimaryTool.action?submitMode=init">Download
									Target</a></li>
							<li><a href="<%=filePath %>/tool/analyseGaps.action">Analyze
									Gaps</a></li>
							<li><a
								href="<%=filePath %>/tool/viewManualModification.action" >View Manual Modification</a></li>
							<li><a
								href="<%=filePath %>/tool/postProcessorIdentifyPattern.action?submitMode=view">Invoke
									Post Processor</a></li>
									<li><a
								href="<%=filePath %>/tool/SPSearch.action">Search Stored Procedure</a></li>
							<li><a href="#">Compile And Release</a></li>
							<!-- level 2 sub menu start-->
							<!-- <li>			
			<div class="createawebsitediv">
			  <ul id="createawebsite1" >
			<li><a href="#">SP Migration Reports</a>
			   <ul>
			   <li><a href="#">Stored Procedure Pattern Report	History</a></li>
			   <li><a href="#">Source to Target Mapping Report	History</a></li>
			   <li><a href="#">Tool Execution Summary Report History</a></li>
			   <li><a href="#">Success Summary Report	History</a></li>
			   <li><a href="#">Gap Analysis Report	History</a></li>
			  </ul>
			 </div>			
			 </li> -->
							<!-- level 2 sub menu end> -->
						</ul>

						<div class="menuheaders">
							<a href="#" title="Front-end Migration">Frontend Migration</a>
						</div>
						<ul class="menucontents">
							<li><a
								href="<%=filePath %>/tool/frontEndMigration.action?submitMode=init">Upload
									Source code</a></li>
							<li><a
								href="<%=filePath %>/tool/frontEndIdentifyPatterns.action?">Identify
									Patterns</a></li>
							<li><a
								href="<%=filePath %>/tool/frontEndMapToTarget.action?">Map
									to Target</a></li>
							<!-- <li><a href="#">Invoke Primary Tool</a></li>
			<li><a href="#">Analyze Gaps</a></li>
			<li><a href="#">Invoke Post Processor</a></li> -->
							<li><a
								href="<%=filePath %>/tool/frontEndComplieNRelease.action?">Compile
									And Release</a></li>

						</ul>

						<!-- 	<div class="menuheaders"><a href="#" title="Menu3">Java Front-end Migration</a></div>		
			<ul class="menucontents">
			<li><a href="#">Identify Patterns</a></li>
			<li><a href="#">Map to Target</a></li>
			<li><a href="#">Invoke Primary Tool</a></li>
			<li><a href="#">Analyze Gaps</a></li>
			<li><a href="#">Invoke Post Processor</a></li>
			<li><a href="#">Compile And Release</a></li>
			
			</ul> -->

						<div class="menuheaders">
							<a href="#" title="Reports">Reports</a>
						</div>
						<ul class="menucontents">
							<li><a href="#">SP Patterns Report</a></li>
							<li><a href="#">Frontend Patterns Report</a></li>
							<li><a href="#">Primary Tool Exception Report</a></li>
							<li><a href="#">Postprocessor Change Report</a></li>
							<li><a href="#">Compiler Report</a></li>
						</ul>

						<!-- <div class="menuheaders"><a href="#" title="Menu3">Schema/Data Migration</a></div>
			<ul class="menucontents">
			<li><a href="#">Generate Schema Mig Scripts</a></li>
			<li><a href="#">Generate Data Mig Scripts</a></li>
			<li><a href="#">Generate Security Mig Scripts</a></li>
			<li><a href="#">Invoke Schema Mig Scripts</a></li>
			<li><a href="#">Invoke Data Mig Scripts</a></li>
			<li><a href="#">Invoke Security Mig Scripts</a></li>
			</ul>
		</div> -->

						<div class="menuheaders">
							<a href="#" title="Schema/Data Migration">Schema/Data
								Migration</a>
						</div>
						<ul class="menucontents">
							<li><a
								href="<%=filePath %>/tool/idmtIssuesFixing.action?submitMode=init">Process
									Input Files</a></li>
							<li><a
								href="<%=filePath %>/tool/validateDataMigration.action?submitMode=init">Validate
									Data Migration</a></li>
							<li><a href="#">Generate Data Migration Scripts</a></li>
							<li><a href="#">Generate Security Migration Scripts</a></li>
							<li><a href="#">Invoke Schema Migration Scripts</a></li>
							<li><a href="#">Invoke Data Migration Scripts</a></li>
							<li><a href="#">Invoke Security Migration Scripts</a></li>
						</ul>
					</div>

					<div
						style="text-decoration: bold; color: WHITE; font: normal 13px Verdana, Arial, Helvetica, sans-serife; height: 20px">
						<b>Tracking Dashboard</b>
					</div>

					<div class="arrowsidemenu">


						<div class="menuheaders">
							<a href="#" title="JavaScript">Stored procedure Dashboard</a>
						</div>
						<div class="menuheaders">
							<a href="#" title="JavaScript">Visual Basic Dashboard</a>
						</div>
						<div class="menuheaders">
							<a href="#" title="JavaScript">Java Dashboard</a>
						</div>



					</div>




					</div>
			</td>
		</TR>
	</table>

</body>
</html>
