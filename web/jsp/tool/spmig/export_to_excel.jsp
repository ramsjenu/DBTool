<%@page contentType="application/vnd.ms-excel" pageEncoding="UTF-8" language="java"%>
 <%@ page import="java.io.PrintWriter" %>   

<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page
	import="com.tcs.tools.web.dto.SearchSPDTO,com.tcs.tools.web.util.ToolsUtil"%>
<%   
      
response.reset();
response.setHeader("Content-type","application/xls");

  
       
    String fileName =  request.getParameter("fileName");   
    
    response.setHeader("Content-disposition","inline; filename=" + fileName);   
  
    PrintWriter op = response.getWriter();  
    op.println(fileName);
    String CSV = request.getParameter("tableHTML");   
    if (CSV == null)   
    {   
        CSV="NO DATA";   
    }   
    if (fileName == null)   
{   
    CSV="NO FILE NAME SPECIFIED";   
}   
  
op.write(CSV);   
%>   

<%
String filePath= request.getContextPath();
String cssPath=filePath+"/web/css";
String jsPath=filePath+"/web/js";
String imagePath=filePath+"/web/images";
%>


<html>
<head>
</head>
<body>
</body>
</html>

