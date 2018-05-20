<html>
<%
	String filePath= request.getContextPath();
	String cssPath=filePath+"/web/css";
	String jsPath=filePath+"/web/js";
	String imagePath=filePath+"/web/images";	
	%>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/><head>
<!--<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>-->
<!--<script type="text/javascript" src="https://github.com/malsup/blockui/raw/master/jquery.blockUI.js"></script>-->
<script type="text/javascript" src="<%=jsPath %>/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/jquery.blockUI.js"></script>
<script type="text/javascript">
	function showLoadingJquery(){ 
		alert("inside function");
		  $.blockUI({ css: { 
	            border: 'none', 
	            padding: '15px', 
	            backgroundColor: '#000', 
	            '-webkit-border-radius': '10px', 
	            '-moz-border-radius': '10px', 
	            opacity: .5, 
	            color: '#fff' 
	        },message: '<img src=\"<%=imagePath%>/loading.gif\" alt=\"Loading...\"/><br>Please wait...' 
		  }); 

	        setTimeout(hideLoadingJquery, 2000);
	}
	function hideLoadingJquery(){ 
		    $.unblockUI();
	}
	$(document).ready(function() { 
	    $('#demo2_testing').click(function() {		 
	        $.blockUI({ css: { 
	            border: 'none', 
	            padding: '15px', 
	            backgroundColor: '#000', 
	            '-webkit-border-radius': '10px', 
	            '-moz-border-radius': '10px', 
	            opacity: .5, 
	            color: '#fff' 
	        } }); 

	        setTimeout($.unblockUI, 2000); 
	    }); 
	});
	</script>

</head>
<body>
	<jsp:include page="/web/jsp/tool/common/left_menu.jsp" flush="true" />
	<input type="button" value="run" id="demo2_testing">
	<input type="button" value="run" id="test111"
		onclick="showLoadingJquery()">
</body>
</html>