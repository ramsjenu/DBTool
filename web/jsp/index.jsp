<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>
<%
String filePath= request.getContextPath();
%>


<title>Insert title here</title>
<link type="text/css"
	href="<%=filePath%>/web/css/smoothness/jquery-ui-1.8.16.custom.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="<%=filePath%>/web/js/jquery-1.6.2.min.js"></script>
<script type="text/javascript"
	src="<%=filePath%>/web/js/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript">
			$(function(){
				// Tabs
				$('#tabs').tabs();
				$("#tabs").tabs("select", "#tabs-2"); 
				

			});
		</script>
<style type="text/css">
/*demo page css*/
body {
	font: 62.5% "Trebuchet MS", sans-serif;
	margin: 50px;
}

.demoHeaders {
	margin-top: 2em;
}

#dialog_link {
	padding: .4em 1em .4em 20px;
	text-decoration: none;
	position: relative;
}

#dialog_link span.ui-icon {
	margin: 0 5px 0 0;
	position: absolute;
	left: .2em;
	top: 50%;
	margin-top: -8px;
}

ul#icons {
	margin: 0;
	padding: 0;
}

ul#icons li {
	margin: 2px;
	position: relative;
	padding: 4px 0;
	cursor: pointer;
	float: left;
	list-style: none;
}

ul#icons span.ui-icon {
	float: left;
	margin: 0 4px;
}

#tabs-1.ui-tabs-selected { 'background-image ' :'none',
	'background-color' : '#ff6666', 'font-weight' : 'bolder'
	
}
</style>
</head>
<body valign="top">



	<form valign="top" method="post">
		<table width="100%" cellspacing=2 cellpadding=2 valign="top">
			<tr>
				<td><jsp:include page="/web/jsp/tool/common/header.jsp" /></td>
			</tr>
			<tr>
				<td>
					<center>
						<h1>TCS Pattern Analysis Tool</h1>
					</center>
				</td>
			</tr>
			<tr>
				<td>
					<div id="tabs">
						<ul>
							<li><a href="#tabs-1" class="ui-tabs-nav">Home</a></li>
							<li><a href="#tabs-2">Projects</a></li>
							<li><a href="#tabs-3">About</a></li>
							<li><a href="#tabs-4">Contacts</a></li>
						</ul>
						<div id="tabs-1" class="tab_content">Lorem ipsum dolor sit
							amet, consectetur adipisicing elit, sed do eiusmod tempor
							incididunt ut labore et dolore magna aliqua. Ut enim ad minim
							veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip
							ex ea commodo consequat.</div>
						<div id="tabs-2"><jsp:include
								page="/web/jsp/ProjectCreation.jsp" />
						</div>
						<div id="tabs-3">Under Construction</div>
						<div id="tabs-4">Under Construction</div>
					</div>

				</td>

			</tr>

			<tr>
				<td><jsp:include page=/web/jsp/tool/common/footer.jsp " /></td>
			</tr>

		</table>

	</form>

</body>
</html>