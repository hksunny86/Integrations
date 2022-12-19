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
      <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
      <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet"
			href="${contextPath}/styles/extremecomponents.css"
			type="text/css">
		<%@include file="/common/ajax.jsp"%>
		<meta name="title" content="OF Settlement Transaction Summary" />
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
		
			<html:form name='ofSettlementTransactionDetailForm'
				commandName="settlementTransactionDetailViewModel" method="post"
				action="p_ofsettlementtransactiondetail.html" onSubmit="return validateForm(this);">
				<table width="750px" border="0">			
				<tr>
					<td class="formText" align="right">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="11" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="12" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="13" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="14" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" /> 
						<input name="reset" type="reset" 
							onclick="javascript: window.location='${contextPath}/p_ofsettlementtransactiondetail.html'"
							class="button" value="Cancel" tabindex="14" />
					</td>
					<td class="formText" align="right">

					</td>
					<td align="left">
					</td>
				</tr>
			</table>
			</html:form>
		

		<ec:table items="settlementTransactionDetailModelList" var="settlementTransactionDetailModel"
		action="${contextPath}/p_ofsettlementtransactiondetail.html"
		title=""
          retrieveRowsCallback="limit"
          filterRowsCallback="limit"
          sortRowsCallback="limit"
          filterable="false"		
		>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="OF Settlement Transaction Summary.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="OF Settlement Transaction Summary.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="OF Settlement Transaction"
					fileName="OF Settlement Transaction Summary.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="OF Settlement Transaction.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="settlementDate" style="text-align: center" cell="date" format="dd/MM/yyyy hh:mm a" title="Transaction Date"/>
				<ec:column property="oracleNumber" title="Oracle Number" style="text-align: center" />
				<ec:column property="accountTitle" title="Account Title" style="text-align: center" />
				<ec:column property="accountTypeName" title="Account Type" filterable="false" sortable="false"  style="text-align: right"/>
				<ec:column property="prodName" title="Product" filterable="false" sortable="false"  style="text-align: right"/>
				<ec:column property="debitMovement" title="Debit" style="text-align: right" cell="currency" format="0.00"/>
				<ec:column property="creditMovemement" title="Credit" style="text-align: right" cell="currency" format="0.00"/>
			</ec:row>
		</ec:table>

		<script language="javascript" type="text/javascript">
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
