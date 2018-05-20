<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List,java.util.ArrayList,java.util.HashMap"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
		 String procName ="";
		  if(request.getAttribute("procName") != null){
			  procName = (String)request.getAttribute("procName");
		 }
		  
	%>
<script type="text/javascript" src="<%=jsPath %>/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=jsPath %>/custom/common.js"></script>
<style>
body {
	font: 12px Tahoma, Verdana, sans-serif;
	padding: 0 10px;
}

a:link,a:visited {
	text-decoration: none;
	color: #416CE5;
	border-bottom: 1px solid #416CE5;
}

h2 {
	font-size: 13px;
	margin: 15px 0 0 0;
}
</style>
<script>
		function chnageFrameSrc(frameId,srcLocation){
			//alert("inside parent function");
			setDesignMode("off");			
			document.getElementById(frameId).src=srcLocation;
			try{
				document.getElementById(frameId).contentWindow.showLoadingJquery('<%=imagePath%>');
			}catch (e) {
				// TODO: handle exception
			}
		}
		function submitToolApplyChnages(){
			var v_procName = document.form1.procName.value;
			
			if(v_procName == ""){
				alert("Please Select a Stored Procedure...");
				return false;
			}
			var v_designMode=getDesignMode();
			if(v_designMode.toUpperCase()=="ON"){
				alert("Please save edited content...");
				return false;
			}
			//alert("proc name::::::jjj---"+v_procName);
			var url="<%=filePath%>/tool/postProcessorIdentifyPattern.action?submitMode=applyChanges&linkMode=frameMode&projectId=<%=projectId%>&procName="+v_procName;
			chnageFrameSrc("codeEditor",url);
			//document.getElementById('codeEditor').contentWindow.test();
			
			//document.form1.action='<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?linkMode=frameMode&submitMode=applyChanges&projectId="+'<%=projectId%>'+"&procName="+v_procName;
			//document.form1.submit();
		}
		function setElementValue(elementName,elementValue){
			document.getElementById(elementName).value=elementValue;
		}
		
		function editContent(){
			var v_procName = document.form1.procName.value;
			
			if(v_procName == ""){
				alert("Please Select a Stored Procedure...");
				return false;
			}
			
			var v_designMode=getDesignMode();
			if(v_designMode.toUpperCase()=="ON"){
				alert("Please save edited content...");
				return false;
			}
			
			document.getElementById("codeEditor").contentWindow.removeElement();
			//alert("step1");
			var dataContent = getFrameContents();
			//dataContent = "<font color='#000000'>"+dataContent+"</font>";
			dataContent = replaceAll(dataContent, "#000000", "#000000");//"#000000" ,#6b664f
			dataContent = replaceAll(dataContent, "border=1", "border=0");//for table border
			//alert("frameContent::::"+dataContent);
			setDesignMode("on");
			//codeEditor.document.designMode="on";
			codeEditor.document.open();
			codeEditor.document.write('<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head><style type="text/css">body{ font-family:arial; font-size:13px; }</style> </head>'+dataContent);
			codeEditor.document.close();
			}
		
		function setDesignMode(pMode){
			codeEditor.document.designMode=pMode;
		}
		function getDesignMode(){
			return codeEditor.document.designMode;
		}
		function getFrameContents(){
			//alert("step2");
			   var iFrame =  document.getElementById('codeEditor');
			   
			  // alert(window.frames["codeEditor"].document.documentElement.outerHTML);
			   
			  // alert("step3");
			   var iFrameBody; 
			   if ( iFrame.contentDocument )  
			   { // FF 
			     iFrameBody = iFrame.contentDocument.getElementsByTagName('body')[0]; 
			   } 
			   else if ( iFrame.contentWindow )  
			   { // IE 
			     iFrameBody = iFrame.contentWindow.document.getElementsByTagName('body')[0]; 
			   } 
			    //alert(iFrameBody.innerHTML); 
			    //return iFrameBody.innerHTML;
			   // document.write(window.frames["codeEditor"].document.documentElement.outerHTML);
			    return window.frames["codeEditor"].document.documentElement.outerHTML;
			    
			 } 
		function downloadFile(){
			var v_procName = document.form1.procName.value;
			if(v_procName == ""){
				alert("Please Select a Stored Procedure...");
				return false;
			}
			var v_designMode=getDesignMode();
			if(v_designMode.toUpperCase()=="ON"){
				alert("Please save edited content...");
				return false;
			}
			var url_download='<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=downloadChanges&projectId=<%=projectId%>";
			//alert("::::url_download::::;"+url_download);
			document.form1.action=url_download;
			document.form1.submit();
		}
		
		function saveEditedContents(){
			var v_procName = document.form1.procName.value;
			if(v_procName == ""){
				alert("Please Select a Stored Procedure...");
				return false;
			}
			var v_designMode=getDesignMode();
			if(v_designMode.toUpperCase()=="OFF"){	
				alert("Cant save when editor mode is off, To edit please click edit..");
				return false;
			}
			var frameName="codeEditor";
			if (document.getElementById(frameName).contentWindow.document.documentElement.contentEditable === false || document.getElementById(frameName).contentWindow.document.designMode === "off") { 
				alert("cannot save contents in Non-Editable Mode");
				return false;
			}
			
			//document.getElementById("codeEditor").contentWindow.getTableData() ;
			getChildFrameTableData();
		}
		
		/************read html table column data**************/
		function getChildFrameTableData(){
			
			//alert(document.getElementById("codeEditor").contentWindow.document.getElementById("spCodeTable").body.innerHTML);
			
			var tableName ="spCodeTable";
			var frameName="codeEditor";
			var colNum ="2";
			var totalData="";
			var v_procName = document.form1.procName.value;
			//alert(":::step1");
			//var oTable = document.getElementById(tableName);
			var oTable = document.getElementById(frameName).contentWindow.document.getElementById(tableName);
			//alert(":::step2");
			var rowLength = oTable.rows.length;
			//alert(":::step3");
			for (var i = 0; i < rowLength; i++){	
				if(i > 0){
					totalData +="_DBT-NEW-LINE_";
				}
				
				var oCells = oTable.rows.item(i).cells;
				totalData += oCells.item(colNum-1).innerHTML; 
			}
			//alert(":::step4::::1111:::sss:::::");
			//setDesignMode("off");
			//document.getElementById("spModifiedCode").value=totalData;
			var temp_text= window.frames["codeEditor"].document.documentElement.outerHTML;
			//alert("before"+codeEditor);
			codeEditor.document.designMode="off";
			//alert("after"+codeEditor);
			
			codeEditor.document.open();
			codeEditor.document.write('<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head><style type="text/css">body{ font-family:arial; font-size:13px; }</style> </head>'+temp_text);
			codeEditor.document.close();
			
			document.getElementById(frameName).contentWindow.createElement();
			codeEditor.document.getElementById("spModifiedCode").value=totalData;
			//document.getElementById(frameName).contentWindow.document.getElementById("spModifiedCode").value=totalData;
			//alert('<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=saveChanges&projectId=<%=projectId%>&procName=<%=procName%>");
			//alert("final value:::"+totalData);
			
			//document.form1.action='<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=saveChanges&linkMode=frameMode&projectId=<%=projectId%>&procName=<%=procName%>";
			//document.form1.submit();
			//setDesignMode("off");
			document.getElementById(frameName).contentWindow.showLoadingJquery('<%=imagePath%>');
			//alert("Hiii");
			document.getElementById(frameName).contentWindow.document.form1.action='<%=filePath%>'+"/tool/postProcessorIdentifyPattern.action?submitMode=saveChanges&linkMode=frameMode&projectId=<%=projectId%>&procName="+v_procName;
			document.getElementById(frameName).contentWindow.document.form1.submit();
			
			
			}
		</script>
</head>
<body style="background-color: #ECE9D8;">
	<!-- <center><h1>Post Processor Tool</h1></center> -->
	<form name="form1" action="" method="post">
		<input type='hidden' name='procName' id='procName'>


		<table name="mainTable" border="0">
			<tr>
				<td>
					<!-- Project Explorer - Start-->

					<table cellspacing=0 cellpadding=0>
						<tr>
							<td style="border: 1px solid; border-color: BLACK"><IFRAME
									frameborder="0" style="font: 8px Tahoma, Verdana, sans-serif;"
									width="300px" height="435px"
									SRC="<%=filePath%>/tool/postProcessorIdentifyPattern.action?submitMode=projectExlporer&projectId=<%=projectId%>">

								</IFRAME></td>
						</tr>



					</table> <!-- Project Explorer - End-->
				</td>
				<td>
					<!-- Code Editor - Start-->

					<table cellspacing=0 cellpadding=0
						style="background-color: #D2DFEF /* E7E7F7 */; border: 1px solid; border-color: BLACK; border-bottom: 0px">

						<tr>
							<td style="height: 4px;"></td>
						</tr>
						<tr align="right">

							<td style="height: 20px;">
								<!-- <input  type="button" value="Fix CheckList Items" name="ApplyChanges" align="left" onclick="submitToolApplyChnages()"/> -->
								<%-- <image  src="<%=imagePath%>/save.png" height="20px" width="20px" style="border:1px solid "/> --%>
								<!-- <input type="button" value="save" onclick="saveEditedContents()"> -->
								<!-- <input type="button" value="Edit" name="Edit" onclick="editContent()"/> -->
								<image alt="Save File" src="<%=imagePath%>/save1.jpg"
									height="25px" width="25px"
									style="border:1px solid;cursor:hand;"
									onclick="saveEditedContents()" /> <image alt="Edit Files"
									src="<%=imagePath%>/edit.jpg" height="25px" width="25px"
									style="border:1px solid;cursor:hand;" onclick="editContent()" />
								<image alt="Fix SP CheckList Issues"
									src="<%=imagePath%>/fix.jpg" height="25px" width="25px"
									style="border:1px solid;cursor:hand;"
									onclick="submitToolApplyChnages()" /> <image
									alt="Download File" src="<%=imagePath%>/download.jpg"
									height="25px" width="25px"
									style="border:1px solid;cursor:hand;" onclick="downloadFile()" />
								<!-- <input type="button" value="Save" name="Save" onclick="downloadFile()"/> -->
								<!-- <input type="button" value="Close" name="Close"/> -->
								&nbsp; <!-- <input type="text" /> 
					<input type="button" value="Search" name="Search"/> --> <!-- <input type="button" value="Find & Replace" name="FindNReplace"/> -->
								<%-- <input type="button"   style="background-image: url(<%=imagePath%>/save.png);background-repeat:no-repeat;height:30px;width:30px;" /> --%>

								<%-- <image  src="<%=imagePath%>/search1.jpg" height="20px" width="20px"  style="border:1px solid "/> --%>

							</td>
						</tr>
						<tr>
							<td valign="top"
								style="border: 1px solid; border-color: BLACK; border-top: 0px">
								<IFRAME frameborder="0" width="980px" name="codeEditor"
									id="codeEditor" height="400px"
									SRC="<%=filePath%>/web/jsp/tool/common/blank.jsp"> </IFRAME>
							</td>
						</tr>



					</table> <!-- Code Editor - End-->
				</td>
			</tr>
			<tr>
				<td valign="top" colspan="2"
					style="border: 1px solid; border-color: BLACK;"><IFRAME
						frameborder="0" width="1280px" name="changeTracker"
						id="changeTracker" height="210px" SRC="">
						<!--  height="210px" -->

					</IFRAME></td>
			</tr>
		</table>


	</form>

</body>
</html>