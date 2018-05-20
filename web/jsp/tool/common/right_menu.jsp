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



</head>

<body>
	<table width="100%" cellspacing="0" cellpadding="2">
		<TR>
			<td>
				<div id="contentright">
				
				<div id="leftnavheading">
						<h4>Tag Cloud</h4>
					</div>
					<div class="tagcloud">
							<a href="<%=filePath%>/tool/modifyProjectSPUpload.action?submitMode=reset"
								style="font-size: 15px;  color: #000080;">Upload SP</a>
							<a href="<%=filePath%>/tool/SPSearch.action"
								style="font-size: 11px;  color: #822D7C;">Search SP</a>
							<a href="<%=filePath%>/tool/creatingProject.action"
								style="font-size: 14px;  color: #039FAF;">Create Project</a>
							<a href="<%=filePath%>/tool/modifyProjectDetails.action?submitMode=reset"
								style="font-size: 13px;  color: #008080;">Modify Project</a>
							<a href="<%=filePath%>/tool/SpCallTreeFirstLevel.action"
								style="font-size: 15px;  color: #000080;">SP Call Tree</a>
							<a href="<%=filePath %>/tool/viewManualModification.action"
								style="font-size: 13px;  color: #822D7C;">View Manual Modification</a>
							<a href="<%=filePath %>/tool/analyseGaps.action"
								style="font-size: 21px;  color: #000080;">Analyze Gaps</a>
							<a href="<%=filePath %>/tool/identifyPattern.action"
								style="font-size: 13px;  color: #039FAF;">Identify Patterns</a>
							<a href="<%=filePath %>/tool/postProcessorIdentifyPattern.action?submitMode=view"
								style="font-size: 18px;  color: #008080;">Postprocessor</a>
							<a href="<%=filePath %>/tool/chartReport.action"
								style="font-size: 12px;  color: #822D7C;">SP Analytics</a>
							<a href="<%=filePath %>/tool/inventoryAnalytics.action?submitMode=view""
								style="font-size: 14px;  color: #000080;">Source DB Analytics</a>


						</div>	
						
					<div id="leftnavheading">
						<h4>Tracking Dashboard</h4>
					</div>
					<div id="galleryblank">
					
						<div id="rightpic">
							<center><span class="midboldtxt">Stored Procedure Dashboard</span></center><a
								href="#" class="rightpic" alt="asdasd"></a>
						</div>
						<div id="rightpic02">
							 <center><span class="midboldtxt">Visual Basic Dashboard</span></center><a
								href="#" class="rightpic02"></a>
						</div>
						<div id="rightpic03">
							 <center><span class="midboldtxt">Java
								Dashboard</span></center><a href="#" class="rightpic03"></a>
						</div>
						<!--<div class="viewbutton"><a href="#" class="view"> view more</a></div> 
						         
						          -->
					</div>

						<div id="leftnavheading">
							<h4>Resources</h4>
						</div>
						<div id="leftnav">
							<ul>
								<li><a href="#" class="leftnav">Customer Testimonials </a></li>
								<li><a href="#" class="leftnav">Database Trends and
										Directions</a></li>
								<li><a href="#" class="leftnav">Useful Links </a></li>
								<li><a href="#" class="leftnav">Blog Spot </a></li>
							</ul>
						</div>
						
						</div>
			</td>
		</TR>
	</table>

</body>
</html>