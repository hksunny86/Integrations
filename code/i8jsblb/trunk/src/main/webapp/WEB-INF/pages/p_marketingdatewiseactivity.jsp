<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set> 
<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<%@include file="/common/ajax.jsp"%>
		<meta name="title" content="Date Wise Activity Report" />
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
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
			<html:form name='retailerTransactionViewForm' commandName="extendedRetailerTransactionViewModel" method="post"
				action="p_marketingdatewiseactivity.html" onsubmit="return validateForm(this)" >
				<table width="750px" border="0"><tr>
					<td align="right" class="formText" width="18%">
						Transaction ID:
					</td>
					<td align="left" width="32%" colspan="3">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" maxlength="12" tabindex="1" onkeypress="return maskNumber(this,event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Supplier:
					</td>
					<td align="left">
						<html:select path="supplierId" cssClass="textBox" tabindex="3" >
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
						<html:select path="productId" cssClass="textBox" tabindex="4" >
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
							</c:if>
						</html:select>
						${status.errorMessage}
					</td>
				</tr>
				<!-- added by atif hussain -->
				<tr>
					<td align="right" class="formText">
						Sender Regional Hierarchy:
					</td>
					<td align="left">
						<html:select path="senderRegHierId" cssClass="textBox" tabindex="3" >
							<html:option value="">---All---</html:option>
							<c:if test="${senderRegHierModelList != null}">
								<html:options items="${senderRegHierModelList}" itemValue="regionalHierarchyId" itemLabel="regionalHierarchyName"/>
							</c:if>
						</html:select>
					</td>
					<td align="right" class="formText" width="18%">
						Sender Region:
					</td>
					<td align="left" width="32%">
						<html:select path="senderAgentRegionId" cssClass="textBox" tabindex="4" >
						</html:select>
						${status.errorMessage}
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Receiver Regional Hierarchy:
					</td>
					<td align="left">
						<html:select path="receiverRegHierId" cssClass="textBox" tabindex="3" >
							<html:option value="">---All---</html:option>
							<c:if test="${receiverRegHierModelList != null}">
								<html:options items="${receiverRegHierModelList}" itemValue="regionalHierarchyId" itemLabel="regionalHierarchyName"/>
							</c:if>
						</html:select>
					</td>
					<td align="right" class="formText" width="18%">
						Receiver Region:
					</td>
					<td align="left" width="32%">
						<html:select path="receiverAgentRegionId" cssClass="textBox" tabindex="4" >
							
						</html:select>
						${status.errorMessage}
					</td>
				</tr>
				<!-- end of addition -->
				<tr>
					<td align="right" class="formText">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="6" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td align="right" class="formText">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="9" />
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_marketingdatewiseactivity.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="10" />
					</td>
					<td colspan="2">
						&nbsp;
					</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</table></html:form>
		

		<ec:table items="retailerTransactionViewModelList" var="retailerTransactionViewModel"
		action="${contextPath}/p_marketingdatewiseactivity.html?actionId=${retriveAction}"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Date Wise Activity Report.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Date Wise Activity Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>	
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Date Wise Activity Report" fileName="Date Wise Activity Report.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Date Wise Activity Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>	
			<ec:row>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy" filterable="false" title="Date"/>
				<ec:column property="createdOn" cell="date" format="hh:mm a" filterable="false" sortable="false" alias="Time" width="55px"/>
				<ec:column property="transactionCode" filterable="false" title="Transaction ID" escapeAutoFormat="true">
				  	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${retailerTransactionViewModel.transactionCode}" onclick="return openTransactionWindow('${retailerTransactionViewModel.transactionCode}')">
						  ${retailerTransactionViewModel.transactionCode}
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${retailerTransactionViewModel.transactionCode}
					</authz:authorize>
				</ec:column>
				<ec:column property="productName" filterable="false" title="Transaction"/>
				<ec:column property="senderAgentRegionName" title="Sender Agent Region" />
				<ec:column property="senderAgentAreaName" title="Sender Agent Area" />
				<ec:column property="receiverAgentRegionName" title="Receiver Agent Region" />
				<ec:column property="receiverAgentAreaName" title="Receiver Agent Area" />
				<ec:column property="transactionAmount" title="Transaction Amount" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="serviceChargesExclusive" title="Service Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="toAskari" title="Bank Revenue" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="processingStatusName" filterable="false" title="Transaction Status" />
			</ec:row>
		</ec:table>

		<ajax:select source="supplierId" target="productId"
		baseUrl="${contextPath}/p_refData.html"
		parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>


		<ajax:select source="senderRegHierId" target="senderAgentRegionId"
		baseUrl="${contextPath}/p_regionrefdata.html"
		parameters="regionalHierarchicalId={senderRegHierId}" errorFunction="error"/>

		<ajax:select source="receiverRegHierId" target="receiverAgentRegionId"
		baseUrl="${contextPath}/p_regionrefdata.html"
		parameters="regionalHierarchicalId={receiverRegHierId}" errorFunction="error"/>


		<script language="javascript" type="text/javascript">

			document.forms[0].transactionCode.focus();

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
