<!--Author: Naseer Ullah-->
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
        
        <%@include file="/common/ajax.jsp"%>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" type="text/css" href="${contextPath}/styles/extremecomponents.css"/>
		<meta name="title" content="Handler Transaction Detail Report"/>
		<script language="javascript" type="text/javascript">
			var jq=$.noConflict();
			var serverDate ="<%=PortalDateUtils.getServerDate()%>";			
			function error(request)
	      	{
	      		alert("An unknown error has occured. Please contact with the administrator for more details");
	      	}
		</script>	      			 
	</head>
	<body bgcolor="#ffffff">
		<c:if test="${not empty status.errorMessages}">
			<div class="errorMsg">
				<c:forEach var="error" items="${status.errorMessages}">
					<c:out value="${error}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
		</c:if>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>

		<html:form name="handlerTransactionDetailViewForm" commandName="handlerTransactionDetailViewModel" onsubmit="return validateForm(this)" action="p_handlertransactiondetail.html">
			<table width="800px">
				<tr>
					<td align="right" class="formText" width="22%">Agent Network</td>
					<td width="30%">
						<html:select id="distributorId" path="distributorId" cssClass="textBox" tabindex="1">
							<html:option value="">---All---</html:option>
							<html:options items="${distributorModelList}" itemLabel="name" itemValue="distributorId"/>
						</html:select>
					</td>
					<td class="formText" align="right" width="18%">
						Region:
					</td>
					<td align="left" width="30%">
						<html:select id="regionId" path="regionId" cssClass="textBox" tabindex="2">
							<html:option value="">---All---</html:option>
							<html:options items="${regionModelList}" itemLabel="regionName" itemValue="regionId"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Franchise:
					</td>
					<td align="left">
						<html:select id="retailerId" path="retailerId" cssClass="textBox" tabindex="3">
							<html:option value="">---All---</html:option>
							<html:options items="${retailerModelList}" itemLabel="name" itemValue="retailerId"/>
						</html:select>
					</td>
					<td class="formText" align="right">
						Transaction ID:
					</td>
					<td align="left">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" maxlength="12" tabindex="4" onkeypress="return maskInteger(this,event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Agent ID:
					</td>
					<td align="left">
						<html:input path="agentId" id="agentId" cssClass="textBox" tabindex="4" onkeypress="return maskInteger(this,event)"/>
					</td>
					<td class="formText" align="right">
						Agent Mobile No.:
					</td>
					<td align="left">
						<html:input path="agentMsisdn" id="agentMsisdn" cssClass="textBox" maxlength="11" tabindex="4" onkeypress="return maskNumber(this,event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Handler ID:
					</td>
					<td align="left">
						<html:input path="handlerId" id="handlerId" cssClass="textBox"  tabindex="4" onkeypress="return maskInteger(this,event)"/>
					</td>
					<td class="formText" align="right">
						Handler Mobile No.:
					</td>
					<td align="left">
						<html:input path="handlerMobileNo" id="handlerMobileNo" cssClass="textBox" maxlength="11" tabindex="4" onkeypress="return maskNumber(this,event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Supplier:
					</td>
					<td align="left">
						<html:select path="supplierId" cssClass="textBox" tabindex="3">
							<html:option value="">---All---</html:option>
							<c:if test="${supplierModelList != null}">
								<html:options items="${supplierModelList}" itemValue="supplierId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
					<td align="right" class="formText">
						Product:
					</td>
					<td align="left">
						<html:select path="productId" cssClass="textBox" tabindex="4">
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="left" class="formText">
						<input type="submit" class="button" value="Search" name="_search" tabindex="11"/>
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_handlertransactiondetail.html'" tabindex="12"/>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
			</table>
		</html:form>

		<ec:table filterable="false" items="handlerTransactionDetailViewModelList" var="handlerTransactionDetailViewModel"
		retrieveRowsCallback="limit" filterRowsCallback="limit"
		sortRowsCallback="limit" action="${contextPath}/p_handlertransactiondetail.html">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Handler Transaction Detail Report.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Handler Transaction Detail Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Handler Transaction Detail Report" fileName="Handler Transaction Detail Report.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Handler Transaction Detail Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="transactionCode" title="Transaction ID" escapeAutoFormat="true">
				  	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${handlerTransactionDetailViewModel.transactionCode}" onclick="return openTransactionWindow('${handlerTransactionDetailViewModel.transactionCode}')">
							${handlerTransactionDetailViewModel.transactionCode}
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${handlerTransactionDetailViewModel.transactionCode}
					</authz:authorize>

				</ec:column>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Date/Time" width="120px" style="text-align:center;"/>
				<ec:column property="distributorName" title="Agent Network"/>
				<ec:column property="regionName" title="Region"/>
				<ec:column property="retailerName" title="Franchise"/>
				<ec:column property="agentId" title="Primary Agent ID" escapeAutoFormat="true"/>
				<ec:column property="agentMsisdn" title="Primary Agent Mobile No" escapeAutoFormat="true"/>
				<ec:column property="handlerId" title="Handler's ID" escapeAutoFormat="true"/>
				<ec:column property="handlerMobileNo" title="Handler's Mobile No" escapeAutoFormat="true"/>
				<ec:column property="supplierName" title="Supplier"/>
				<ec:column property="productName" title="Product"/>
				<ec:column property="tranasactionStatus" title="Transaction Status" escapeAutoFormat="true"/>
				<ec:column property="amount" filterable="false" title="Amount" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>		
				<ec:column property="inclusiveCharges" title="Inclusive Charges" calc="total" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="exclusiveCharges" title="Exclusive Charges" calc="total" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="franchiseCommission" title="Franchise Commission" calc="total"  cell="currency" format="0.00" style="text-align: right"/>		
				<ec:column property="agentCommission" title="Agent's Commission" calc="total" cell="currency" format="0.00" style="text-align: right"/>
			</ec:row>
		</ec:table>

		<ajax:select source="distributorId" target="regionId" baseUrl="${contextPath}/p_regionrefdata.html"
		parameters="distributorId={distributorId},actionId=${retriveAction}" errorFunction="error"/>

		<ajax:select source="regionId" target="retailerId" baseUrl="${contextPath}/p_retailerrefdata.html"
		parameters="regionId={regionId},actionId=${retriveAction}" errorFunction="error"/>

		<ajax:select source="supplierId" target="productId" baseUrl="${contextPath}/p_refData.html"
		parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>

		<script language="javascript" type="text/javascript">
	        document.forms[0].distributorId.focus();
	        
	        function openTransactionWindow(transactionCode)
			{
		        var popupWidth = 550;
				var popupHeight = 350;
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;
		        newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) newWindow.focus();
			    return false;
			}
	        
		    function validateForm(form){
		    	var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        	var _fDate = form.startDate.value;
		  		var _tDate = form.endDate.value;
		  		var startlbl = "Start Date";
		  		var endlbl   = "End Date";
	        	var isValid =   validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,currentDate);
	        	return isValid;
	        }
			
      		Calendar.setup(
      		{
		       inputField  : "startDate", // id of the input field
			   button      : "sDate"    // id of the button
		    }
      		);
			Calendar.setup(
		    {
		      inputField  : "endDate", // id of the input field
		      button      : "eDate",    // id of the button
		  	  isEndDate: true
		    }
		    );
		</script>
		<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>
