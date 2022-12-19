 <%@include file="/common/taglibs.jsp"%> 
 <%@ page import='com.inov8.microbank.common.util.*'%>
 <c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<html>
	<head>
	  <meta name="decorator" content="decorator">
	  <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	  <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
      <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
      <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
      <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
	  <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
	  <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
	  <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css" />
	  <%@include file="/common/ajax.jsp"%>
	  
	  <meta name="title" content="FonePay Transaction Detail"/>
	  
      <script language="javascript" type="text/javascript">
		var jq=$.noConflict();
		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
        var username = "<%=UserUtils.getCurrentUser().getUsername()%>";
        var appUserId= "<%=UserUtils.getCurrentUser().getAppUserId()%>";
        var email= "<%=UserUtils.getCurrentUser().getEmail()%>";
      
/* 	      function openTransactionDetailWindow(transactionCode)
		  {
		      var popupWidth = 550;
			  var popupHeight = 350;
			  var popupLeft = (window.screen.width - popupWidth)/2;
			  var popupTop = (window.screen.height - popupHeight)/2;
		      var action = 'p_showtransactiondetail.html?actionId='+${retriveAction}+'&transactionCodeId='+transactionCode+'&isMfs=true';
              newWindow = window.open( action , 'TransactionDetail','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
              if(window.focus) newWindow.focus();
                    return false;
		  } */
      
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
      </script>
      
      	<%@include file="/WEB-INF/pages/export_zip.jsp"%>	
		<%-- <script type="text/javascript" src="${contextPath}/scripts/exportzip.js"></script> --%>
		<%
		String detailBtnPermission = PortalConstants.ADMIN_GP_READ;
		 %>
		
	</head>
	<body bgcolor="#ffffff">
	
		<div id="rsp" class="ajaxMsg"></div>
	
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
			<html:form name="fonePayTransactionDetailI8Form"
				commandName="extendedFonePayTransactionDetailModel" method="post"
				action="p_fonePayTransactionDetail.html?actionId=${retriveAction}" onsubmit="return validateForm(this)" >
		<table width="950px" border="0">
				<tr>
				<td class="formText" align="right">
					Transaction ID:
					</td>
					<td align="left">
					<html:input path="transactionId"   id="transactionId" tabindex="1" cssClass="textBox" maxlength="12" onkeypress="return maskNumber(this,event)" />
				</td>

				<td class="formText" align="right">
					FonePay Transaction Code:
					</td>
					<td align="left">
					<html:input path="fonePayTransactionCode"   id="fonepayTransactionCode" tabindex="2" cssClass="textBox" maxlength="50" />
				</td>
				</tr>
				
				<tr>
					<td class="formText" width="25%" align="right">
						Sender ID:
					</td>
					<td align="left" width="25%" >
						<html:input path="mfsId" id="mfsId" cssClass="textBox" maxlength="50" tabindex="3" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right" width="25%">
						Sender Mobile No.:
					</td>
					<td align="left">
						<html:input path="saleMobileNo" id="saleMobileNo" cssClass="textBox" maxlength="11" tabindex="4" onkeypress="return maskNumber(this,event)"/> 
					</td>
				</tr>
				
				<tr>
					<td class="formText" align="right">
						Recipient ID:
					</td>
					<td align="left">
						<html:input path="recipientMfsId" id="recipientMfsId" cssClass="textBox" maxlength="50" tabindex="5" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right">
						Recipient Mobile No.:
					</td>
					<td align="left" >
						<html:input path="recipientMobileNo" id="recipientMobileNo" cssClass="textBox" maxlength="11" tabindex="6" onkeypress="return maskNumber(this,event)"/> 
					</td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Transaction Date - Start:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="7" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="8" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						Transaction Date - End:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="9" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="10" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
			
			<tr>
         	   <td class="formText" align="right">
                Transaction Updated On - Start:
           	 </td>
           	 <td align="left">
                <html:input path="updatedOnStartDate" id="UpdtFromStartDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="fromStartDate" tabindex="11" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="fromStartDate" tabindex="12" title="Clear Date" name="popcal" onclick="javascript:$('UpdtFromStartDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td class="formText" align="right">
                Transaction Updated On - End:
            </td>
            <td align="left">
                <html:input path="updatedOnEndDate" id="UpdtToStartDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="toStartDate" tabindex="13" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="toStartDate" tabindex="14" title="Clear Date" name="popcal" onclick="javascript:$('UpdtToStartDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
    	    </tr>
				
				<tr>
					<td class="formText" align="right">
					</td>
					<td align="left">
						<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
							   value="<%=PortalConstants.ACTION_RETRIEVE%>">
						<input name="_search" type="submit" class="button" value="Search" tabindex="15"/> 
						<input name="reset" type="reset" 
							onclick="javascript: window.location='p_fonePayTransactionDetail.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="16" />
					</td>
					<td class="formText" align="right">
					</td>
					<td align="left">
					</td>
				</tr>
				
			</table>
		</html:form>
		
		<ec:table items="fonePayTransactionDetailList" var="transactionDetailPortalModel"
		action="${contextPath}/p_fonePayTransactionDetail.html?actionId=${retriveAction}"
		title=""
          retrieveRowsCallback="limit"
          filterRowsCallback="limit"
          sortRowsCallback="limit"
          filterable="false"		
		>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="FonePay Transaction Detail.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="FonePay Transaction Detail.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Transaction Detail"
					fileName="FonePay Transaction Detail.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="FonePay Transaction Detail.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="transactionId" filterable="false" title="Transaction ID" escapeAutoFormat="true">
				  	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${transactionDetailPortalModel.transactionId}" onClick="return openTransactionWindow('${transactionDetailPortalModel.transactionId}')">
						  ${transactionDetailPortalModel.transactionId}
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${transactionDetailPortalModel.transactionId}
					</authz:authorize>
				</ec:column>
				<ec:column property="saleMobileNo" filterable="false" title="Sender Mobile No" escapeAutoFormat="true"/>
				<ec:column property="senderCNIC" filterable="false" title="Sender CNIC" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="deviceType" filterable="false" title="Sender Channel" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="senderBVS" filterable="false" title="Sender BVS" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="paymentMode" filterable="false" title="Payment Mode" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="senderAgentAccountNo" title="Sender Account No"/>
				<ec:column property="createdOn" sortable="false" filterable="false" title="Transaction Date" cell="date" escapeAutoFormat="true" style="text-align: center" format="dd/MM/yyyy hh:mm a"/>
				<ec:column property="updatedOn" filterable="false" title="Transaction Date Last Updated On" cell="date" format="dd/MM/yyyy hh:mm a"/>
				<ec:column property="supplierName" filterable="false" title="Supplier" escapeAutoFormat="true" />
				<ec:column property="productName" filterable="false" title="Product"/>
				<ec:column property="recipientMfsId" filterable="false" title="Recipient ID" escapeAutoFormat="true"/>								
				<ec:column property="recipientAccountNo" title="Recipient Account No."/>				
				<ec:column property="recipientMobileNo" filterable="false" title="Recipient Mobile No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="recipientCNIC" filterable="false" title="Recipient CNIC" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="receiverBVS" sortable="false" filterable="false" title="Recipient BVS" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="receivableFromFonepay" filterable="false" title="Receivable From FonePay" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="payableToFonepay" filterable="false" title="Payable To FonePay" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>			
				<ec:column property="netSettlement" filterable="false" title="Net Settlement" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>			
				
				<ec:column property="serviceChargesInclusive" filterable="false" title="Inclusive Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>			
				<ec:column property="serviceChargesExclusive" filterable="false" title="Exclusive Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>				
 			    <ec:column property="totalCustomerCharges" filterable="false" title="Total Customer Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>		
 			   
				<ec:column property="fundTransferRRN" title="Fund Transfer RRN" escapeAutoFormat="true"/>
				<ec:column property="processingStatusName" title="Status" escapeAutoFormat="true"/>
				<ec:column property="fonePayTransactionCode" filterable="false"  title="FonePay Transaction Code" />
				<ec:column property="fonePayTransactionType" filterable="false"  title="FonePay Transaction Type" />
				
				<authz:authorize ifAnyGranted="<%=detailBtnPermission%>">
				<ec:column alias=" " title="Detail" sortable="false">
					<input type="button" class="button" value="Detail" onClick="return openTransactionDetailWindow('${transactionDetailPortalModel.transactionCode}')"/>
	   			</ec:column>
   			</authz:authorize>
			</ec:row>
		</ec:table>

		<%-- <ajax:select source="supplierId" target="productId"
		baseUrl="${contextPath}/p_refData.html"
		parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>
 --%>
		
		<script language="javascript" type="text/javascript">
		
			 document.forms[0].transactionId.focus(); 
			function openTransactionWindow(transactionCode)
			{
		        var popupWidth = 550;
				var popupHeight = 350;
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;
		        newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) newWindow.focus();
			    return false;
			}
			
			
			
			Calendar.setup({
				inputField : "startDate", // id of the input field
				button : "sDate", // id of the button
			});

			Calendar.setup({
				inputField : "endDate", // id of the input field
				button : "eDate", // id of the button
				isEndDate : true
			});
			
		    Calendar.setup( {inputField  : "UpdtFromStartDate",button : "fromStartDate",} );
		    Calendar.setup( {inputField  : "UpdtToStartDate",button : "toStartDate", isEndDate: true } );
			
      	</script>
     	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>