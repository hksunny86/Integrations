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
        <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>

        <%@include file="/common/ajax.jsp"%>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" type="text/css" href="${contextPath}/styles/extremecomponents.css"/>
		<script language="javascript" type="text/javascript">
		var jq=$.noConflict();
		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
        </script>
		<meta name="title" content="Account Summary Report"/>
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

		<html:form name="bbAccountsByAgentsViewForm" commandName="bbAccountsByAgentsViewModel" onsubmit="return validateForm(this)" action="p_bbaccountsbyagents.html">
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
                        Agent ID:
					</td>
					<td align="left">
                        <html:input path="agentId" id="agentId" cssClass="textBox" maxlength="12" tabindex="4" onkeypress="return maskNumber(this,event)"/>
					</td>
					<td class="formText" align="right">
                        Account Status:
					</td>
					<td align="left">
                        <html:select path="accountStatus" cssClass="textBox" tabindex="6">
                            <html:option value="">---All---</html:option>
                            <html:option value="ACTIVATED">ACTIVATED</html:option>
                            <html:option value="DEACTIVATED">DEACTIVATED</html:option>
                        </html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Account Opening Start Date:
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
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_bbaccountsbyagents.html'" tabindex="12"/>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
			</table>
		</html:form>

		<ec:table filterable="false" items="bbAccountsByAgentsViewModelList" retrieveRowsCallback="limit" filterRowsCallback="limit"
		sortRowsCallback="limit" action="${contextPath}/p_bbaccountsbyagents.html">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Account Summary Report.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Account Summary Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Account Summary Report" fileName="Account Summary Report.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Account Summary Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="distributorName" title="Agent Network"/>
				<ec:column property="regionName" title="Region"/>
                <ec:column property="areaName" title="Area"/>
                <ec:column property="cityName" title="City"/>
                <ec:column property="businessName" title="Agent Business Name"/>
                <ec:column property="customerName" title="Customer Name"/>
                <ec:column property="accountTypeName" title="Account Type"/>
				<ec:column property="agentId" title="Agent ID" escapeAutoFormat="true"/>
				<ec:column property="customerUserId" title="User Account ID" escapeAutoFormat="true"/>
				<ec:column property="createdOn" title="Account Opening Date" cell="date" format="dd/MM/yyyy hh:mm a" style="text-align:center;"/>
				<ec:column property="lastTransactionDate" cell="date" format="dd/MM/yyyy"/>
				<ec:column property="accountStatus" title="Account Status"/>
			</ec:row>
		</ec:table>

		<ajax:select source="distributorId" target="regionId" baseUrl="${contextPath}/p_regionrefdata.html"
		parameters="distributorId={distributorId},actionId=${retriveAction}" errorFunction="error"/>

		<ajax:select source="regionId" target="retailerId" baseUrl="${contextPath}/p_retailerrefdata.html"
		parameters="regionId={regionId},actionId=${retriveAction}" errorFunction="error"/>

		<script language="javascript" type="text/javascript">
	        document.forms[0].distributorId.focus();
			
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
