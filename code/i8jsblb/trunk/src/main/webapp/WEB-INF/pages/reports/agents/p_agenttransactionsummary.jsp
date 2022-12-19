<!--Author: Naseer Ullah-->
<%@ page import='com.inov8.microbank.common.util.*'%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
		<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>
        
        <%@include file="/common/ajax.jsp"%>
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" type="text/css" href="${contextPath}/styles/extremecomponents.css"/>
		<meta name="title" content="Agent/Franchise Commission Report"   id="<%=ReportIDTitleEnum.AgentFranchiseCommissionReport.getId()%>"    />
		<meta name="decorator" content="decorator">
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

		<html:form name="agentTransactionSummaryViewForm" commandName="agentTransactionSummaryViewModel" onsubmit="return validateForm(this)" action="p_agenttransactionsummary.html">
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
					<td class="formText" align="right">
						Agent ID:
					</td>
					<td align="left">
						<html:input path="agentId" id="agentId" cssClass="textBox" maxlength="12" tabindex="4" onkeypress="return maskInteger(this,event)"/>
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
						<span class="asterisk">*</span>Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						<span class="asterisk">*</span>End Date:
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
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_agenttransactionsummary.html'" tabindex="12"/>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
			</table>
		</html:form>

		<ec:table filterable="false" items="agentTransactionSummaryViewModelList" var="agentTransactionSummaryViewModel"
		 retrieveRowsCallback="limit" filterRowsCallback="limit"
		sortRowsCallback="limit" action="${contextPath}/p_agenttransactionsummary.html">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Agent Transaction Summary Report.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Agent Transaction Summary Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Agent Transaction Summary Report" fileName="Agent Transaction Summary Report.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Agent Transaction Summary Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="agentId" title="Agent ID" escapeAutoFormat="true"/>

                <ec:column property="agentBusinessName" title="Agent Business Name"/>
                <ec:column property="areaName" title="Area"/>
                <ec:column property="areaLevelName" title="Area Level"/>
                <ec:column property="cityName" title="City"/>
                <ec:column property="regionName" title="Region"/>

                <ec:column property="distributorName" title="Agent Network"/>
				<ec:column property="productName" title="Transaction Type"/>
				<ec:column property="productCount" title="Transaction Count" format="0" calc="total" calcTitle="Total:" style="text-align: right"/>
				<ec:column property="bankBalance" title="Transaction Amount" sortable="false" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right" width="100px"/>
				<ec:column property="agentCommissionSum" title="Agent Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
<%-- 				<ec:column property="franchiseCommissionSum" title="Franchise Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>		--%>
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
		
	</body>
</html>