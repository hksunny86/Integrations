<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="java.util.Date"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>
<html>
	<head>
<meta name="decorator" content="decorator">
   		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/date-validation.js"></script>
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />	    
		<meta name="title" content="Posted Transactions" />
		<%@include file="/common/ajax.jsp"%>	
	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
  	<script language="javascript" type="text/javascript">
  		var jq=$.noConflict();
      function error(request)
      {
      	alert("An unknown error has occured. Please contact with the administrator for more details");
      }
		
      function initProgress(){
			if (confirm('If Transaction information is verified then press OK to continue')==true){
		    	return true;
		    }else{
			  $('errorMsg').innerHTML = "";
			  $('successMsg').innerHTML = "";
 			  $('message').value="";
			  Element.hide('successMsg'); 
			  Element.hide('errorMsg'); 
			  return false;
			}
		}
      
  	var isErrorOccured = false;		
	function resetProgress(){
		if(!isErrorOccured){
		    $('errorMsg').innerHTML = "";
	  		Element.hide('errorMsg'); 
		    $('successMsg').innerHTML = $F('message');
		    var btnName = $F('currentBtn');
		    if(btnName != ''){
		  		document.getElementById('revBtn_'+btnName).disabled=true;
		  		document.getElementById('recBtn_'+btnName).disabled=true;
		  		document.getElementById('resBtn_'+btnName).disabled=true;
		  	}
		    Element.show('successMsg');
		} 
		isErrorOccured = false;
	}

    </script>				
	<%
	String sendReversalPermission = PortalConstants.ADMIN_GP_UPDATE;
	sendReversalPermission += "," + PortalConstants.PG_GP_UPDATE;
	sendReversalPermission += "," + PortalConstants.RPT_POSTED_TX_SEND_REV_UPDATE;

	String noRectificationPermission = PortalConstants.ADMIN_GP_UPDATE;
	noRectificationPermission += "," + PortalConstants.PG_GP_UPDATE;
	noRectificationPermission += "," + PortalConstants.RPT_POSTED_TX_NO_RTF_UPDATE;

	String externallyResolvedPermission = PortalConstants.ADMIN_GP_UPDATE;
	externallyResolvedPermission += "," + PortalConstants.PG_GP_UPDATE;
	externallyResolvedPermission += "," + PortalConstants.RPT_POSTED_TX_EXT_RES_UPDATE;
	%>
	</head>
	<body bgcolor="#ffffff">
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
	<div id="errorMsg" class ="errorMsg" style="display:none;"></div>
		<c:if test="${not empty messages}">
	    	<div class="infoMsg" id="successMessages">
	      		<c:forEach var="msg" items="${messages}">
		        	<c:out value="${msg}" escapeXml="false"/>
		        	<br/>
	      		</c:forEach>
	    	</div>
	    	<c:remove var="messages" scope="session"/>
	  	</c:if>

		<html:form name="phoenixTransactionSearchForm" commandName="phoenixTransactionSearchModel" method="post" action="p_postedtransactionmanagement.html" onsubmit="return onFormSubmit(this)">
			<input type="hidden" name="appUserId" value="${param.appUserId}">
			<table border="0" width="750px">
				<tr>
					<td width="20%" height="16" align="right"  class="formText">Transaction Type:&nbsp;
		            </td>
					<td width="30%" align="left">
						<html:select id= "transactiontype" path="transactionCode" tabindex="1" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<html:option value="073">Balance Inquiry</html:option>
							<html:option value="036">Bill Payment</html:option>
							<html:option value="076">Fund Transfer</html:option>
						</html:select>
					</td>	
				</tr>
				<tr>
	            	<td width="20%" height="16" align="right"  class="formText"><span style="color:#FF0000">*</span>From Date:&nbsp;</td>
	             	<td width="30%" align="left" >
	             		<html:input path="fromDate" cssClass="textBox" readonly="true" tabindex="1"/>
						<img id="fDate" tabindex="2" name="popcal" align="top" 
							style="cursor:pointer"
							src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="fDate" tabindex="3" name="popcal"
							title="Clear Date" onclick="javascript:$('fromDate').value=''"
							align="middle" style="cursor:pointer"
							src="${pageContext.request.contextPath}/images/refresh.png"
							border="0" />
					</td>
					<td width="20%" height="16" align="right"  class="formText"><span style="color:#FF0000">*</span>To Date:&nbsp;</td>
		             <td width="30%" align="left" >
	             		<html:input path="toDate" cssClass="textBox" readonly="true" tabindex="4"/>
						<img id="tDate" tabindex="5" name="popcal" align="top" 
							style="cursor:pointer"
							src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="tDate" tabindex="6" name="popcal"
							title="Clear Date" onclick="javascript:$('toDate').value=''"
							align="middle" style="cursor:pointer"
							src="${pageContext.request.contextPath}/images/refresh.png"
							border="0" />
					</td>
		       	</tr>
			  <tr>
				<td></td>
			    <td align="left" >
			    	<input type="submit"  class="button" value="Search" tabindex="3" name="_search"/>
			    	<input type="reset" class="button" value="Cancel"  name="_cancel" onClick="javascript: window.location='p_postedtransactionmanagement.html'">
			   	</td>
			  </tr>
	 	 	</table>
		</html:form>

		<ec:table filterable="false" sortable="false" items="phoenixTransactionList"
			var="phoenixTransactionModel" retrieveRowsCallback="limit"
			filterRowsCallback="limit" sortRowsCallback="limit"
			action="${pageContext.request.contextPath}/p_postedtransactionmanagement.html"
			title="" width="850px">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Posted Transactions.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Posted Transactions.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Phoenix_Transactions" fileName="Posted Transactions.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Posted Transactions.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="transactionDate" title="Date" cell="date" format="dd/MM/yyyy hh:mm:ss a" width="16%" style="text-align: center" />
				<ec:column property="displayTransactionType" title="Type" width="10%" style="text-align: center" />
				<ec:column property="displayRRN" title="RRN" width="10%" escapeAutoFormat="true" style="text-align: center">
					<c:if test="${phoenixTransactionModel.transactionType == '076' && (empty phoenixTransactionModel.responseCode || phoenixTransactionModel.statusId == 5001 || phoenixTransactionModel.statusId == 5002 || phoenixTransactionModel.statusId == 5003 || phoenixTransactionModel.statusId == 5004 || phoenixTransactionModel.statusId == 2001 || phoenixTransactionModel.statusId == 2002)}">
						<a href="${pageContext.request.contextPath}/p_postedtransactiondetail.html?actionId=2" onclick="return openDetailWindow('${phoenixTransactionModel.transactionLogId}')"  >
						<c:out value="${phoenixTransactionModel.displayRRN}"/>
						</a>
					</c:if>
				</ec:column>
				<ec:column property="fromAccount" title="From Account #" width="12%" escapeAutoFormat="true"/>
				<ec:column property="toAccount" title="To Account #" width="11%" escapeAutoFormat="true"/>
				<ec:column property="amount" title="Amount" width="7%" style="text-align: right"/>
				<ec:column property="responseCode" title="Response Code" width="10%" style="text-align: center" />
				<ec:column property="displayStatus" title="Rectification Status" style="text-align: center" width="13%"/>
				<ec:column property="responseCode" alias="sendReversal" title="Send Reversal" style="text-align: center" viewsAllowed="html">&nbsp;
				<c:choose>	
					<c:when test="${phoenixTransactionModel.transactionType == '076' && (phoenixTransactionModel.statusId == 5003 || phoenixTransactionModel.statusId == 5004)}">
						<authz:authorize ifAnyGranted="<%=sendReversalPermission%>">
							<input id="revBtn_${phoenixTransactionModel.transactionLogId}" name="revBtn_${phoenixTransactionModel.transactionLogId}" type="button" class="button" value="Send Reversal"/>
							<input type="hidden" id="message" value=""/>
							<input type="hidden" id="currentBtn" value=""/>
							<ajax:updateField baseUrl="${contextPath}/p_phoenixReversalAjax.html"
								source="revBtn_${phoenixTransactionModel.transactionLogId}" 
								action="revBtn_${phoenixTransactionModel.transactionLogId}"
								target="message,currentBtn"
								parameters="trxLogId=${phoenixTransactionModel.transactionLogId},rrn=${phoenixTransactionModel.displayRRN},resType=FT"
								parser="new ResponseXmlParser()"
								errorFunction="globalAjaxErrorFunction"
								preFunction="initProgress"
								postFunction="resetProgress"/>
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=sendReversalPermission%>">
	 			    			<input id="revBtn_${phoenixTransactionModel.transactionLogId}" name="revBtn_${phoenixTransactionModel.transactionLogId}" type="button" class="button" value="Send Reversal" disabled="disabled"/>
	 			    	</authz:authorize>
					</c:when>
					<c:otherwise>
	 			    			<input id="revBtn_${phoenixTransactionModel.transactionLogId}" name="revBtn_${phoenixTransactionModel.transactionLogId}" type="button" class="button" value="Send Reversal" disabled="disabled"/>
    				</c:otherwise>
				 </c:choose>	
				</ec:column>
				<ec:column property="responseCode" alias="noRectification" title="No Rectification" style="text-align: center" viewsAllowed="html">&nbsp;
					<c:choose>
					<c:when test="${phoenixTransactionModel.transactionType == '076' && ((empty phoenixTransactionModel.responseCode && (phoenixTransactionModel.statusId != 2001 && phoenixTransactionModel.statusId != 2002)) || (phoenixTransactionModel.statusId == 5003 || phoenixTransactionModel.statusId == 5004))}">
						<authz:authorize ifAnyGranted="<%=noRectificationPermission%>">
							<input id="recBtn_${phoenixTransactionModel.transactionLogId}" name="recBtn_${phoenixTransactionModel.transactionLogId}" type="button" class="button" value="No Rectification"/>
							<ajax:updateField baseUrl="${contextPath}/p_phoenixReversalAjax.html"
								source="recBtn_${phoenixTransactionModel.transactionLogId}" 
								action="recBtn_${phoenixTransactionModel.transactionLogId}"
								target="message,currentBtn"
								parameters="trxLogId=${phoenixTransactionModel.transactionLogId},rrn=${phoenixTransactionModel.displayRRN},resType=RNR"
								parser="new ResponseXmlParser()"
								errorFunction="globalAjaxErrorFunction"
								preFunction="initProgress"
								postFunction="resetProgress"/>
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=noRectificationPermission%>">
							<input id="recBtn_${phoenixTransactionModel.transactionLogId}" name="recBtn_${phoenixTransactionModel.transactionLogId}" type="button" class="button" value="No Rectification" disabled="disabled"/>								
	 			    	</authz:authorize>
					</c:when>
					<c:otherwise>
							<input id="recBtn_${phoenixTransactionModel.transactionLogId}" name="recBtn_${phoenixTransactionModel.transactionLogId}" type="button" class="button" value="No Rectification" disabled="disabled"/>								
					</c:otherwise>
					</c:choose>
				</ec:column>
				<ec:column property="responseCode" alias="externallyResolved" title="Externally Resolved" style="text-align: center" viewsAllowed="html">&nbsp;
					<c:choose>
					<c:when test="${phoenixTransactionModel.transactionType == '076' && ((empty phoenixTransactionModel.responseCode && (phoenixTransactionModel.statusId != 2001 && phoenixTransactionModel.statusId != 2002)) || (phoenixTransactionModel.statusId == 5003 || phoenixTransactionModel.statusId == 5004))}">
						<authz:authorize ifAnyGranted="<%=externallyResolvedPermission%>">
							<input id="resBtn_${phoenixTransactionModel.transactionLogId}" name="resBtn_${phoenixTransactionModel.transactionLogId}" type="button" class="button" value="Externally Resolved"/>
							<ajax:updateField baseUrl="${contextPath}/p_phoenixReversalAjax.html"
								source="resBtn_${phoenixTransactionModel.transactionLogId}" 
								action="resBtn_${phoenixTransactionModel.transactionLogId}"
								target="message,currentBtn"
								parameters="trxLogId=${phoenixTransactionModel.transactionLogId},rrn=${phoenixTransactionModel.displayRRN},resType=ER"
								parser="new ResponseXmlParser()"
								errorFunction="globalAjaxErrorFunction"
								preFunction="initProgress"
								postFunction="resetProgress"/>
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=externallyResolvedPermission%>">
							<input id="resBtn_${phoenixTransactionModel.transactionLogId}" name="resBtn_${phoenixTransactionModel.transactionLogId}" type="button" class="button" value="Externally Resolved" disabled="disabled"/>
	 			    	</authz:authorize>
					</c:when>
					<c:otherwise>					
							<input id="resBtn_${phoenixTransactionModel.transactionLogId}" name="resBtn_${phoenixTransactionModel.transactionLogId}" type="button" class="button" value="Externally Resolved" disabled="disabled"/>	 			    	
					</c:otherwise>
					</c:choose>
				</ec:column>
			</ec:row>
		</ec:table>
		<script language="javascript" type="text/javascript">
		  document.forms[0].transactiontype.focus();
		  function onFormSubmit(form){
	  		var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        var _fDate = form.fromDate.value;
		  	var _tDate = form.toDate.value;
		  	var startlbl = "From Date";
		  	var endlbl   = "To Date";
	        var isValid = validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,currentDate);
	        return isValid;
	  	  }

	      function confirmUpdateStatus(link)
	      {
	        if (confirm('Are you sure you want to update status?')==true)
	        {
	          window.location.href=link;
	        }
	      }

		  function openDetailWindow(transactionLogId){
			newWindow = window.open('p_postedtransactiondetail.html?actionId=2&trxLogId='+transactionLogId,'TransactionDetail', 'width=650,height=400,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			if(window.focus) newWindow.focus();
			if(newWindow.opener == null){
				newWindow.close();
			}
			return false;
		  }

	      Calendar.setup(
	      {
	        inputField  : "fromDate", // id of the input field
	        button      : "fDate"    // id of the button
	      }
	      );
	      
	   	  Calendar.setup(
	      {
	        inputField  : "toDate", // id of the input field
	        button      : "tDate",    // id of the button
	        isEndDate: true
	      }
	      );    
	      </script>
	
	</body>
</html>
