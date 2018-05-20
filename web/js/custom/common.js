function showLoadingJquery(rootJsPath){ 
	
		
		  $.blockUI({ css: { 
	            border: 'none', 
	            padding: '15px', 
	            backgroundColor: '#000', 
	            '-webkit-border-radius': '10px', 
	            '-moz-border-radius': '10px', 
	            opacity: .5, 
	            color: '#fff' 
	        },message: '<img src=\"'+rootJsPath+'/loading.gif\" alt=\"Loading...\"/><br>Please wait...' 
		  }); 

	        //setTimeout(hideLoadingJquery, 2000);
	        //alert("window unblocked....");
}
function showLoadingJqueryWithText(rootJsPath,msgToShow){ 
	
 alert("msgToShow::"+msgToShow);
	  $.blockUI({ css: { 
          border: 'none', 
          padding: '15px', 
          backgroundColor: '#000', 
          '-webkit-border-radius': '10px', 
          '-moz-border-radius': '10px', 
          opacity: .5, 
          color: '#fff' 
      },message: '<img src=\"'+rootJsPath+'/loading.gif\" alt=\"Loading...\"/> <br>Please wait...'+msgToShow 
	  }); 	 
	 
     
}

function hideLoadingJquery(){ 
		    $.unblockUI();
}

function isValidIPAddress(ipaddr) {
	   var re = /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/;
	   if (re.test(ipaddr)) {
	      var parts = ipaddr.split(".");
	      if (parseInt(parseFloat(parts[0])) == 0) { return false; }
	      for (var i=0; i<parts.length; i++) {
	         if (parseInt(parseFloat(parts[i])) > 255) { return false; }
	      }
	      return true;
	   } else {
	      return false;
	   }
	}

function isValidPort(ipPort) {
	   var re = /^\d{1,4}$/;
	   if (re.test(ipPort)) {
	      var parts = ipPort.split(".");
	      if (parseInt(parseFloat(parts[0])) == 0) { return false; }
	      for (var i=0; i<parts.length; i++) {
	         if (parseInt(parseFloat(parts[i])) > 255) { return false; }
	      }
	      return true;
	   } else {
	      return false;
	   }
	}

function isNumberKey(evt)
{
	//sample implementation ->onkeypress="return isNumberKey(event)" 
	
   var charCode = (evt.which) ? evt.which : event.keyCode
   if (charCode > 31 && (charCode < 48 || charCode > 57))
      return false;

   return true;
}

/**************method to remove / delete all rows in a table ***************/
function deleteAllTabeRows(tableID){
	//var tableID ="spDetialsList";
	//alert("inside delete function::121111::"+tableID);
	var Parent = document.getElementById(tableID);
	//alert("inside delete function:Parent:::"+Parent);
	while(Parent.hasChildNodes())
	{
	   Parent.removeChild(Parent.firstChild);
	}

}

/***************method to add row in a table (tableid , td-InnerHtmlValue)*************************************/
function addRowInnerHTML(tblId,tdInnerHtmlValue,trClassName){
	//alert(":::tdInnerHtmlValue:::"+tdInnerHtmlValue);
  var tblBody = document.getElementById(tblId).tBodies[0];
  var table = document.getElementById(tblId); 
  var rowCount = table.rows.length;  
  var newRow = table.insertRow(-1);
  if(trClassName !=""){
	  newRow.className = trClassName;
  }
  var newCell0 = newRow.insertCell(0);
  newCell0.innerHTML = tdInnerHtmlValue;
 }

/***************method to check string contains*************/
function strContains(pSearchText,pCompleteText,pCaseSensitve){
	/*var igonrecase="";
	if(pCaseSensitve == false){
		igonrecase='i';
	}
	 try { 
		if(pCompleteText.search(new RegExp(pSearchText,igonrecase))>0){
			//alert("inside if::"+pSearchText+"::-::"+pCompleteText);
			return true;
		}else{
			//alert("inside else if::"+pSearchText+"::-::"+pCompleteText);
		}
	 }catch(e) { 
			//alert(e);             
		}*/ 
	
	if(pCaseSensitve == false){
		if(pCompleteText.toUpperCase().indexOf(pSearchText.toUpperCase())!=-1){
			return true;
		}
	}else{
		if(pCompleteText.indexOf(pSearchText)!=-1){
			return true;
		}
	}
	
	return false;
}
/******************string replacement**************************/
function replaceAll(txt, replace, with_this) {
	return txt.replace(new RegExp(replace, 'g'),with_this);
}


