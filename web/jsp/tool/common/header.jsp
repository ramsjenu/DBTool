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



</head>

<body>
	<table width="100%" cellspacing="0" cellpadding="0">
		<tr>
			<td>
				<div id="headerbg">
					<div id="headerblank">
						<div id="header">
							<div id="menu">
								<ul>
									<li><a href="<%=filePath%>/tool/index.action" class="menu" style="cursor: default;">Home</a></li>
									<!--  <li><a href="#" class="menu">Overview</a></li> -->
									<li><a href="#" class="menu" style="cursor: default;">Contact Us</a></li>
									
									<li><a href="<%=filePath%>/tool/addingFeedback.action?submitMode=viewFeedback" class="menu" style="cursor: default;">Feedback</a></li>
								<li><a href="<%=filePath%>/tool/help.action" class="menu" style="cursor: default">Help</a></li>
								</ul>
							</div>
							<!-- <div id="login">
					<div id="logintxtblank">
					  <div id="loginheading">
						<h4>User Login</h4>
					  </div>
					  <div id="username">User Name:</div>
					  <div id="input">
						<label>
						  <input name="textfield" type="text" class="input" id="textfield" value="enter your user name" />
						</label>
					  </div>
					  <div id="password">Password:</div>
					  <div id="input02">
						<label>
						  <input name="textfield2" type="password" class="input" id="textfield2" value="password" />
						</label>
					  </div>
					  <div id="loginbutton"><a href="#" class="login">login</a></div>
					  <div id="member">Not yet a Member? </div>
					  <div id="register"><a href="#" class="register">Register Now</a></div>
					</div>
				  </div> -->
						</div>
					</div>
				</div>
			</td>
		</tr>
	</table>

</body>
</html>
