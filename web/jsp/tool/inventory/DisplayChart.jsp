<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List,java.util.ArrayList,java.util.HashMap"%>
<%@page
	import="com.tcs.tools.web.dto.ChartReportDTO,com.tcs.tools.web.util.ToolsUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
String filePath= request.getContextPath();
String cssPath=filePath+"/web/css";
String jsPath=filePath+"/web/js";
String imagePath=filePath+"/web/images";

%>
<%
	List chartDetailsList = new ArrayList();
	if(request.getAttribute("chartDetailsList") != null){
		chartDetailsList = (List)request.getAttribute("chartDetailsList");
	}
	String lChartValues ="";
	String lChartLables = "";
	String lChartTitle ="";
	List lTableDataList =new ArrayList();
	String lChartPercValues = "";
	
	if(chartDetailsList != null && chartDetailsList.size() == 5){
		lChartValues = (String)chartDetailsList.get(0);
		lChartLables = (String)chartDetailsList.get(1);
		lChartTitle = (String)chartDetailsList.get(2);
		lTableDataList = (List)chartDetailsList.get(3);
		lChartPercValues  = (String)chartDetailsList.get(4);
	}
	System.out.println(":::::lChartValues:::::"+lChartValues);
	System.out.println(":::::lChartLables:::::"+lChartLables);
	System.out.println(":::::lChartTitle:::::"+lChartTitle);
	System.out.println(":::::lTableDataList:::::"+lTableDataList.size());
	System.out.println(":::::lChartPercValues:::::"+lChartPercValues);
%>

<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>
<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />

<title>:::::db TransPlant::::</title>
</head>
<body>
	<table width="100%">
		<tr>
			<td>
				<table width="100%">
					<tr class="pageheadernoWidth" width="100%">
						<td><%=lChartTitle %></td>
					</tr>
					<tr>
						<td>
							<table width="100%" class="subtable">
								<%	ChartReportDTO lChartReportDTO = new ChartReportDTO();
										if(lTableDataList != null && lTableDataList.size() > 0){
										lChartReportDTO =(ChartReportDTO)lTableDataList.get(0); %>
								<tr class="tableheader">
									<td width="5%">S.No</td>
									<td width="15%"><%=lChartReportDTO.getColumn1() %></td>
									<td width="25%"><%=lChartReportDTO.getColumn2() %></td>
									<td width="5%"><%=lChartReportDTO.getColumn3() %></td>
								</tr>
								<% } %>

							</table>

						</td>
					</tr>
					<tr>
						<td>
							<table width="100%" class="subtable">

								<%	String lCssClass="tablerowodd";
								 lChartReportDTO = new ChartReportDTO();
									if(lTableDataList != null && lTableDataList.size() > 0 ){
										for(int i=1;i<lTableDataList.size();i++){
											lChartReportDTO =(ChartReportDTO)lTableDataList.get(i);
										if( i%2 ==0){
											lCssClass="tableroweven";
										}else{
											lCssClass="tablerowodd";
											
										}
										
										
								%>
								<tr class="<%=lCssClass %>">
									<td width="5%"><%=i %></td>
									<td width="15%"><%=lChartReportDTO.getColumn1() %></td>
									<td width="25%"><%=lChartReportDTO.getColumn2() %></td>
									<td width="5%"><%=lChartReportDTO.getColumn3() %></td>
								</tr>

								<% }}%>
							</table>

						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>
							<table>
								<tr>
									<td><img
										src="http://chart.apis.google.com/chart?cht=p&chs=765x300&chd=t:<%=lChartPercValues/* lChartValues */ %>&chl=<%=lChartLables %>&chtt=<%=lChartTitle %>&chco=">
									</td>
								</tr>
							</table>

						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>


</body>
</html>