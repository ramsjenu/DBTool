<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head><!--  Tells ie 8 to display standard doctypes in ie 7 standard mode-->

<title>:::::db TransPlant::::</title>

<%
	String filePath= request.getContextPath();
	String cssPath=filePath+"/web/css";
	String jsPath=filePath+"/web/js";
	String imagePath=filePath+"/web/images";	
	%>

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
					<tr>
						<td>
							<div id="contentmid">
								<div class="midheading">
									<h2>Our Main Purpose</h2>
									
								</div>
								<div class="midtxt">
									<!-- <span class="midboldtxt">To ensure smooth and seamless movement from one relational database to another....</span><br /> -->
									To ensure smooth migration of application components from one
									relational database to another....
								</div>
								<div id="comments">
									<div id="addcomments">
										<a href="<%=filePath%>/tool/addingFeedback.action"  class="addcomments">Add Comment</a>
									</div>
									<div id="morecomments">
										<a href="<%=filePath%>/tool/addingFeedback.action?submitMode=viewFeedback"  class="morecomments">Read More...</a>
									</div>
								</div>
								<!--  <div class="midheading">
					          <h2>Latest Projects<span class="projectheading">Wednesday, May 07, 2008</span></h2>
					        </div>
					        <div id="projectbg">
					          <div id="projectthumnail"></div>
					          <div id="projecttxtblank">
					            <div id="projecttxt"><span class="projectboldtxt">Nulla venenati sed varius an teproin</span> libero aecenas dapibus am gravida ante quis arcu liquam eleifend. Donec at elit. Integer lectus dolor utrum a volutpat .<br />
					            </div>
					            <div id="moreproject"><a href="#" class="moreproject">read more</a></div>
					          </div>
					        </div> -->
								<div class="midheading">
									<h2>About DBTransPlant</h2>
								</div>
								<div id="purposetxt">Its main aim is to provide end-to-end
									migrations between any-to-any relational databases. However,
									Current focus is to help in migration from Sybase to other
									relational databases.</div>


								<div id="galleryblank">
									<div class="rightheading">
										<h4>Migration Approach</h4>
									</div>
									<div id="purposetxt">The approach for migration involves
										the following steps:</div>
								</div>
								<div id="purposenav">
									<ul>
										<li><a href="#" class="purposenav">Upload the source
												code</a></li>
										<li><a href="#" class="purposenav">Build a list of
												patterns for each application & store in a pattern
												repository</a></li>
									</ul>
									<ul>
										<li><a href="#" class="purposenav">Invoke the primary
												tool to migrate the source application </a></li>
										<li><a href="#" class="purposenav">Manually change
												the unsupported patterns by the primary tool after the
												migration</a></li>
									</ul>
									<ul>
										<li><a href="#" class="purposenav"> Compare the
												source patterns with the target patterns using the pattern
												repository</a></li>
										<li><a href="#" class="purposenav">Address the
												discrepancy (if any), re-validate the migration and get
												sign-off</a></li>
									</ul>
								</div>
								<%-- <div class="midtxt"><span class="midboldtxt">Morbi porta odio id erat. Curabitur ut massa uspendisse ipsum. In vitae dolor eget lorem</span> Suspendisse massa lacus, ullamcorper ac, pulvinar ut, aliquet et, elit. </div> --%>
							</div>



						</td>
						
					</tr>
				</table> <!--  page content ends -->
			</td>
			<!-- right pane contents  -->
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