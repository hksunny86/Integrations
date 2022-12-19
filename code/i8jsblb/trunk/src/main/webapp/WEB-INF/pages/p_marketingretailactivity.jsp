<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE%></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
	    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
        
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<%@include file="/common/ajax.jsp"%>
		<meta name="title" content="Agent Network Activity Report" />
      	<script language="javascript" type="text/javascript">
      		var jq=$.noConflict();
      		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
      		function openTransactionDetailWindow(transactionCode)
		  {
		      var action = 'p_showtransactiondetail.html?actionId='+${retriveAction}+'&transactionCodeId='+transactionCode+'&isMfs=true';
              newWindow = window.open( action , 'TransactionDetail', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
              if(window.focus) newWindow.focus();
                    return false;
		  }

	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
        </script>
	</head>
	<body bgcolor="#ffffff">
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
			<html:form name='retailerTransactionViewForm' commandName="extendedRetailerTransactionViewModel" method="post"
				action="p_marketingretailactivity.html" onsubmit="return validateForm(this);" >
				<table width="760px" border="0">
				<tr>
					<td align="right" class="formText">
						Transaction ID:
					</td>
					<td align="left">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" maxlength="12" tabindex="1" onkeypress="return maskNumber(this,event)"/>
					</td>
					<td class="formText" align="right">
						Transaction Status:
					</td>
					<td align="left">
						<html:select path="supProcessingStatusId" cssClass="textBox" tabindex="2">
							<html:option value="">---All---</html:option>
							<c:if test="${supplierProcessingStatusList != null}">
								<html:options items="${supplierProcessingStatusList}" itemValue="supProcessingStatusId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText" width="18%">
						Sender Agent Network:
					</td>
					<td align="left" width="32%">
						<html:select path="senderDistributorId" cssClass="textBox" tabindex="3">
							<html:option value="">---All---</html:option>
							<c:if test="${distributorModelList != null}">
								<html:options items="${distributorModelList}" itemValue="distributorId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
					<td align="right" class="formText" width="18%">
						Receiver Agent Network:
					</td>
					<td align="left">
						<html:select path="receiverDistributorId" cssClass="textBox" tabindex="4">
							<html:option value="">---All---</html:option>
							<c:if test="${distributorModelList2 != null}">
								<html:options items="${distributorModelList2}" itemValue="distributorId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText" width="18%">
						Sender Retailer ID:
					</td>
					<td align="left" width="32%">
						<html:input path="senderAgentId" id="senderAgentId" cssClass="textBox" maxlength="7" tabindex="5" onkeypress="return maskNumber(this,event)"/>
					</td>
					<td align="right" class="formText" width="18%">
						Receiver Retailer ID:
					</td>
					<td align="left">
						<html:input path="receiverAgentId" id="receiverAgentId" cssClass="textBox" maxlength="7" tabindex="6" onkeypress="return maskNumber(this,event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Supplier:
					</td>
					<td align="left">
						<html:select path="supplierId" cssClass="textBox" tabindex="7">
							<html:option value="">---All---</html:option>
							<c:if test="${supplierModelList != null}">
								<html:options items="${supplierModelList}" itemValue="supplierId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
					<td align="right" class="formText" width="18%">
						Product:
					</td>
					<td align="left" width="32%">
						<html:select path="productId" cssClass="textBox" tabindex="8">
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="${contextPath}/images/cal.gif" border="0"/>
						<img id="sDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="${contextPath}/images/refresh.png" border="0" />
					</td>
					<td align="right" class="formText">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="11" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0"/>
					     <img id="eDate" tabindex="12" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="13"/>
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_marketingfranchiseactivity.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="14"/>
					</td>
					<td colspan="2">
						&nbsp;
					</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</table>
			</html:form>
		

		<ec:table items="retailerTransactionViewModelList" var="retailerTransactionViewModel" action="${contextPath}/p_marketingretailactivity.html?actionId=${retriveAction}"
			title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Retail Activity Report.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Retail Activity Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Retail Activity Report" fileName="Retail Activity Report.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Retail Activity Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>	
			<ec:row>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy" title="Date"/>
				<ec:column property="createdOn" cell="date" format="hh:mm a" sortable="false" alias="Time" width="55px"/>
				<ec:column property="transactionCode" title="Transaction ID" escapeAutoFormat="true">
					<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${retailerTransactionViewModel.transactionCode}" onclick="return openTransactionWindow('${retailerTransactionViewModel.transactionCode}')">
						  ${retailerTransactionViewModel.transactionCode}
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${retailerTransactionViewModel.transactionCode}
					</authz:authorize>
				</ec:column>
				<ec:column property="senderDistributorName" title="Sender Agent Network"/>
				<ec:column property="senderRetailerName" title="Sender Franchise"/>	
				<ec:column property="senderAgentId" title="Sender Retailer ID" style="text-align: center" escapeAutoFormat="true"/>
				<ec:column property="senderAgentAreaName" title="Sender Retailer Location"/>
				<ec:column property="senderAgentBusinessName" title="Sender Retailer Details"/>
				<ec:column property="senderDistLevelName" title="Sender Retailer Level"/>
				<ec:column property="receiverDistributorName" title="Receiver Agent Network"/>
				<ec:column property="receiverRetailerName" title="Receiver Franchise"/> 
				<ec:column property="receiverAgentId" title="Receiver Retailer ID" style="text-align: center" escapeAutoFormat="true"/>
				<ec:column property="receiverAgentAreaName" title="Receiver Retailer Location"/>
				<ec:column property="receiverAgentBusinessName" title="Receiver Retailer Details"/>
				<ec:column property="receiverDistLevelName" title="Receiver Retailer Level"/>
				<ec:column property="productName" title="Transaction"/>
				<ec:column property="transactionAmount" title="Transaction Amount" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="serviceChargesExclusive" title="Service Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="taxDeducted" title="Tax Deducted" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
<%-- 				<ec:column property="toFranchise1" title="Sender Franchise Share" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>	--%>
				<ec:column property="toAgent1" title="Sender Retailer Share" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="salesTeamCommission" title="Sales team Share" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="othersCommission" title="Other Share" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="processingStatusName" title="Transaction Status"/>
			</ec:row>
		</ec:table>

		<ajax:select source="supplierId" target="productId"
		baseUrl="${contextPath}/p_refData.html"
		parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>

		<script language="javascript" type="text/javascript">
			document.forms[0].transactionCode.focus();
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
