<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@ page import='com.inov8.microbank.common.util.FinancialInstitutionConstants'%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<html>
	<head>
	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	  <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
      <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
      <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
      <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
      <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet"
			href="${contextPath}/styles/extremecomponents.css"
			type="text/css">
		<%@include file="/common/ajax.jsp"%>
		<meta name="title" content="Closed/Blacklisted Account Commission" />
		<meta name="decorator" content="decorator">
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
	
		<div id="rsp" class="ajaxMsg"></div>
	
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
			<html:form name='unsettledAgentCommForm'
				commandName="unsettledAgentCommModel" method="post"
				action="p_unsettledcommreport.html" onsubmit="return validateForm(this)" >
				<table width="750px" border="0">
				<tr>
					<td class="formText" width="18%" align="right">
						Transaction ID:
					</td>
					<td align="left" width="32%" >
						<html:input path="transactionCode" cssClass="textBox" maxlength="20" tabindex="1" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right" width="18%">
						Agent ID:
					</td>
					<td align="left">
						<html:input path="agentId" cssClass="textBox" maxlength="20" tabindex="2" onkeypress="return maskNumber(this,event)"/> 
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
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
					<td class="formText" align="right">
						Product:
					</td>
					<td align="left">
						<html:select path="productId" cssClass="textBox" tabindex="4" >
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>				
				</tr>
				<tr>
					<td class="formText" align="right">
						From Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="5" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="6" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						To Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="7" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="8" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="9"/> 
						<input name="reset" type="reset" 
							onclick="javascript: window.location='p_unsettledcommreport.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="10" />
					</td>
					<td class="formText" align="right"></td>
					<td align="left"></td>
				</tr>

				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
					value="<%=PortalConstants.ACTION_RETRIEVE%>">
				</table>
			</html:form>
	
		
		<ec:table items="unsettledAgentCommModelList" var="unsettledAgentCommModel"
				action="${contextPath}/p_unsettledcommreport.html?actionId=${retriveAction}"
				title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Unsettled Agent Commission.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Unsettled Agent Commission.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Transaction Details"
					fileName="Unsettled Agent Commission.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Unsettled Agent Commission.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			
			<ec:row>
				<ec:column property="transactionCode" filterable="false" title="Transaction ID" escapeAutoFormat="true" style="text-align: center" width="15%">
				  	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${unsettledAgentCommModel.transactionCode}" onClick="return openTransactionWindow('${unsettledAgentCommModel.transactionCode}')">
						  ${unsettledAgentCommModel.transactionCode}
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${unsettledAgentCommModel.transactionCode}
					</authz:authorize>
				</ec:column>
				<ec:column property="transactionDate" title="Transaction Date" cell="date" format="dd/MM/yyyy hh:mm a" style="text-align: center" width="15%"/>
				<ec:column property="agentId" title="Agent ID" escapeAutoFormat="true" style="text-align: center" width="15%"/>
				<ec:column property="productName" title="Product" escapeAutoFormat="true" style="text-align: center" width="15%"/>
				<ec:column property="commissionType" title="Commission Type" escapeAutoFormat="true" style="text-align: left"/>
				<ec:column property="commissionAmount" title="Commission Amount" calc="total" calcTitle="Total:" escapeAutoFormat="true" cell="currency" format="0.00" style="text-align: right" width="15%"/>
			</ec:row>
		</ec:table>
		
		<ajax:select source="supplierId" target="productId"
			baseUrl="${contextPath}/p_refData.html"
			parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>
		
		<script language="javascript" type="text/javascript">
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
			   button      : "sDate",    // id of the button
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
