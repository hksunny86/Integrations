<!-- Naseer Ullah -->
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
		<meta name="decorator" content="decorator">
		<meta name="title" content="Regional Retail Activity Report"/>
	    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
        
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css"/>
		<%@include file="/common/ajax.jsp"%>
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
		
			<html:form name='extendedRetailerTransactionViewForm' commandName="extendedRetailerTransactionViewModel" method="post"
				action="p_marketingregionalactivity.html" onsubmit="return validateForm(this)" >
				<table width="900px" border="0">
				<!-- <tr>
					<td align="center" colspan="4" class="formText">Please select Sender Agent Network and/or Receiver Agent Network.</td>
				</tr> -->
				<tr>
					<td align="right" class="formText" width="20%">
						Sender Agent Network:
					</td>
					<td align="left" width="30%">
						<html:select path="senderDistributorId" id="senderDistributorId" cssClass="textBox" tabindex="1">
							<html:option value="">---All---</html:option>
							<c:if test="${distributorModelList != null}">
								<html:options items="${distributorModelList}" itemValue="distributorId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
					<td align="right" class="formText" width="20%">
						Receiver Agent Network:
					</td>
					<td align="left" width="30%">
						<html:select path="receiverDistributorId" id="receiverDistributorId" cssClass="textBox" tabindex="2">
							<html:option value="">---All---</html:option>
							<c:if test="${distributorModelList != null}">
								<html:options items="${distributorModelList}" itemValue="distributorId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Sender Agent Region:
					</td>
					<td align="left">
						<html:select path="senderAgentRegionId" id="senderAgentRegionId" cssClass="textBox" tabindex="3">
							<html:option value="">---All---</html:option>
							<c:if test="${senderAgentRegionModelList != null}">
								<html:options items="${senderAgentRegionModelList}" itemValue="regionId" itemLabel="regionName"/>
							</c:if>
						</html:select>
					</td>
					<td align="right" class="formText">
						Receiver Agent Region:
					</td>
					<td align="left">
						<html:select path="receiverAgentRegionId" id="receiverAgentRegionId" cssClass="textBox" tabindex="4">
							<html:option value="">---All---</html:option>
							<c:if test="${receiverAgentRegionModelList != null}">
								<html:options items="${receiverAgentRegionModelList}" itemValue="regionId" itemLabel="regionName"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText" width="18%">
						Sender Agent Level:
					</td>
					<td align="left" width="32%">
						<html:select path="senderDistLevelId" id="senderDistLevelId" cssClass="textBox" tabindex="5">
							<html:option value="">---All---</html:option>
							<c:if test="${senderDistributorLevelModelList != null}">
								<html:options items="${senderDistributorLevelModelList}" itemValue="distributorLevelId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
					<td align="right" class="formText" width="18%">
						Receiver Agent Level:
					</td>
					<td align="left" width="32%">
						<html:select path="receiverDistLevelId" id="receiverDistLevelId" cssClass="textBox" tabindex="6">
							<html:option value="">---All---</html:option>
							<c:if test="${receiverDistributorLevelModelList != null}">
								<html:options items="${receiverDistributorLevelModelList}" itemValue="distributorLevelId" itemLabel="name"/>
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
						<img id="sDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0"/>
						<img id="sDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0"/>
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="11" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="12" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="13"/>
						<input name="reset" type="reset" onclick="javascript: window.location='p_marketingregionalactivity.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="14"/>
					</td>
					<td colspan="2">
						&nbsp;
					</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</table>
			</html:form>
		

		<ec:table items="retailerTransactionViewModelList" var="retailerTransactionViewModel" action="${contextPath}/p_marketingregionalactivity.html?actionId=${retriveAction}"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false" sortable="false" rowsDisplayed="100" showPagination="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Regional Retail Activity Report.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Regional Retail Activity Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Regional Retail Activity Report" fileName="Regional Retail Activity Report.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Regional Retail Activity Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>	
			<ec:row>
				<ec:column property="senderDistributorName" title="Sender Agent Network"/>
				<ec:column property="senderAgentRegionName" title="Sender Agent Region"/>
				<ec:column property="senderDistLevelName" title="Sender Agent Level"/>
				<ec:column property="numOfSenderAgents" title="No of Sender Agents"/>
				<ec:column property="receiverDistributorName" title="Receiver Agent Network"/>
				<ec:column property="receiverAgentRegionName" title="Receiver Agent Region"/>				
				<ec:column property="receiverDistLevelName" title="Receiver Agent Level"/>
				<ec:column property="numOfReceiverAgents" title="No of Receiver Agents"/>
				<ec:column property="productName" title="Transaction Type"/>
				<ec:column property="numOfTransaction" title="No. of Transactions" format="0" calc="total" calcTitle="Total:" style="text-align: right"/>
				<ec:column property="transactionAmount" title="Transactional Amount" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="serviceChargesExclusive" title="Exclusive Service Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="taxDeducted" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="toAskari" title="Bank Share" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
 			    
<%--  			    <ec:column property="toFranchise1" title="Sender Franchise Share" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>	--%>
 			    <ec:column property="toAgent1" title="Sender Agent Share" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
 			    <ec:column property="salesTeamCommission" title="Sale Team Share" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
 			    <ec:column property="othersCommission" title="Other Share" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			</ec:row>
		</ec:table>

		<ajax:select source="senderDistributorId" target="senderAgentRegionId" baseUrl="${contextPath}/p_regionrefdata.html"
		parameters="distributorId={senderDistributorId},actionId=${retriveAction}" errorFunction="error" />

		<ajax:select source="receiverDistributorId" target="receiverAgentRegionId" baseUrl="${contextPath}/p_regionrefdata.html"
		parameters="distributorId={receiverDistributorId},actionId=${retriveAction}" errorFunction="error" />

		<ajax:select source="senderAgentRegionId" target="senderDistLevelId" baseUrl="${contextPath}/p_agentlevelrefdata.html"
		parameters="regionId={senderAgentRegionId},actionId=${retriveAction}" errorFunction="error"/>

		<ajax:select source="receiverAgentRegionId" target="receiverDistLevelId" baseUrl="${contextPath}/p_agentlevelrefdata.html"
		parameters="regionId={receiverAgentRegionId},actionId=${retriveAction}" errorFunction="error"/>

		<script language="javascript" type="text/javascript">
			document.forms[0].senderDistributorId.focus();

      		Calendar.setup(
      		{
		       inputField  : "startDate",// id of the input field
		       button      : "sDate"     // id of the button
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
