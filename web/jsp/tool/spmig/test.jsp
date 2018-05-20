<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>

<title>Insert title here</title>
<%
String url ="";
if(request.getParameter("url") != null){
	url = request.getParameter("url");
}
String runId ="";
if(request.getParameter("runId") != null){
	runId = request.getParameter("runId");
}
System.out.println("::::url::::::::::::"+url);
%>
<script>
function submitPage(){
	if('<%=url%>' !="" ){
		document.form1.action='<%=url%>'+"&runId="+'<%=runId%>';
		document.form1.submit();
	}
}
</script>
</head>
<body onload="submitPage()">
	<Form name="form1" method="post"></Form>
</body>
</html>