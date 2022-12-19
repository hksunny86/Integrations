<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
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
		<meta name="title" content="Sales Team Commission Report"   id="<%=ReportIDTitleEnum.SalesTeamCommissionReport.getId()%>" />
      	<script language="javascript" type="text/javascript">
      	var jq=$.noConflict();
      	var serverDate ="<%=PortalDateUtils.getServerDate()%>";
      	var username = "<%=UserUtils.getCurrentUser().getUsername()%>";
    	var appUserId= "<%=UserUtils.getCurrentUser().getAppUserId()%>";
    	var email= "<%=UserUtils.getCurrentUser().getEmail()%>";
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
		
			<html:form name='salesTeamComissionForm'
				commandName="extendedSalesTeamComissionViewModel" method="post"
				action="p_salesteamcomission.html" onsubmit="return validateForm(this)" >
				
				<table width="750px" border="0">
				<tr>
					<td class="formText" align="right">
						Transaction ID:
					</td>
					<td align="left">
						<html:input path="transactionId" id="transactionId" cssClass="textBox" tabindex="1" maxlength="50" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right">
						Agent ID:
					</td>
					<td align="left">
						<html:input path="agentId" id="agentId" cssClass="textBox" tabindex="2" maxlength="50" onkeypress="return maskNumber(this,event)"/> 
					</td>
				</tr>
				
						<tr>
					<td class="formText" align="right">
						BDE Name:
					</td>
					<td align="left">
						<html:input path="bdeUsername" id="bdeUsername" cssClass="textBox" tabindex="3" maxlength="50" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right">
						TL Name:
					</td>
					<td align="left">
						<html:input path="tlUsername"   id="tlUsername" tabindex="4" cssClass="textBox" maxlength="50" />
					</td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Supplier:
					</td>
					<td align="left">
						<html:select path="supplierId" cssClass="textBox" tabindex="5" >
							<html:option value="">---All---</html:option>
							<c:if test="${supplierModelList != null}">
								<html:options items="${supplierModelList}" itemValue="supplierId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right">
						Product:
					</td>
					<td align="left">
						<html:select path="productId" cssClass="textBox" tabindex="6" >
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
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="7" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="8" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="9" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="10" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="11"/> 
						<input name="reset" type="reset" 
							onclick="javascript: window.location='p_salesteamcomission.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="12" />
					</td>
					<td class="formText" align="right">

					</td>
					<td align="left">
					</td>
				</tr>

				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
					value="<%=PortalConstants.ACTION_RETRIEVE%>">
				</table>
			</html:form>

		
		<ec:table items="salesTeamComissionList" var="salesTeamComissionViewModel"
		action="${contextPath}/p_salesteamcomission.html?actionId=${retriveAction}"
		title=""
          retrieveRowsCallback="limit"
          filterRowsCallback="limit"
          sortRowsCallback="limit"
          filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Sales Team Commission.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Sales Team Commission.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Sales Team Commission"
					fileName="Sales Team Commission.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Sales Team Commission.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				
				<ec:column property="transactionId" filterable="false" title="Transaction ID" escapeAutoFormat="true">
				  	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${salesTeamComissionViewModel.transactionId}" onclick="return openTransactionWindow('${salesTeamComissionViewModel.transactionId}')">
						  ${salesTeamComissionViewModel.transactionId}
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${salesTeamComissionViewModel.transactionId}
					</authz:authorize>
				</ec:column>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="true" title="Transaction Date"/>
				<ec:column property="agentId" title="Agent ID" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="supplierName" title="Supplier" />
				<ec:column property="productName" title="Product"/>
				<ec:column property="totalAmount" title="Amount" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
			    <ec:column property="charges" title="Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right" />		
			    <ec:column property="salesTeamCommission" title="Sales Team Commission" cell="currency"  format="0.00" style="text-align: right" calc="total" calcTitle="Total:" />		
			    <ec:column property="bdeCommission" interceptor="" title="BDE Commission" cell="currency"  format="0.00" style="text-align: right" sortable="false" calc="total" calcTitle="Total:"/>
			    <ec:column property="bdeUsername" title="BDE Name" />		
			    <ec:column property="bdeRole" title="BDE Role" />		
			    <ec:column property="tlCommission" title="TL Commission" cell="currency"  format="0.00" style="text-align: right" sortable="false" calc="total" calcTitle="Total:"/>		
			    <ec:column property="tlUsername" title="TL Name"/>		
			    <ec:column property="tlRole" title="TL Role" />		

			</ec:row>
		</ec:table>

		<ajax:select source="supplierId" target="productId"
		baseUrl="${contextPath}/p_refData.html"
		parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>

		
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
