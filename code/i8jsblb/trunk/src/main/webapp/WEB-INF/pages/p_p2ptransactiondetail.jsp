<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.webapp.action.portal.transactiondetaili8module.ReportTypeEnum"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="reportTitle"><%=ReportTypeEnum.valueOf( request.getParameter( "reportType" ).toUpperCase() ).getTitle()%></c:set>
<html>
	<head>
	    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>

        
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="${reportTitle}" />
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
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
			<html:form name='transactionDetailI8Form' commandName="extendedTransactionDetailPortalListModel" method="post"
				action="p_p2ptransactiondetail.html" onsubmit="return validateForm(this)" >
				<input type='hidden' name='reportType' value='${param.reportType}' />
				<table width="750px" border="0"><tr>
					<td class="formText" width="18%" align="right">
						Sender CNIC:
					</td>
					<td align="left" width="32%" >
						<html:input path="senderCnic" cssClass="textBox" maxlength="13" tabindex="1" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right" width="18%">
						Sender Mobile No:
					</td>
					<td align="left">
						<html:input path="saleMobileNo" id="saleMobileNo" cssClass="textBox" maxlength="11" tabindex="2" onkeypress="return maskNumber(this,event)"/> 
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Recipient CNIC:
					</td>
					<td align="left">
						<html:input path="recipientCnic" cssClass="textBox" maxlength="50" tabindex="3" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right">
						Recipient Mobile No:
					</td>
					<td align="left" >
						<html:input path="recipientMobileNo" id="recipientMobileNo" cssClass="textBox" maxlength="11" tabindex="4" onkeypress="return maskNumber(this,event)"/> 
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Transaction ID:
					</td>
					<td align="left">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" tabindex="5" maxlength="50" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right">
						Processing Status:
					</td>
					<td align="left">
						<html:select path="processingStatusId" cssClass="textBox" tabindex="7" >
							<html:option value="">---All---</html:option>
							<html:option value="1">Complete</html:option>
							<html:option value="4">In-process</html:option>
							<html:option value="6">Unclaimed</html:option>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Sender Channel:
					</td>
					<td align="left">
						<html:select path="senderDeviceTypeId" cssClass="textBox" tabindex="7" >
							<html:option value="">---All---</html:option>
							<c:if test="${deviceTypeModelList != null}">
								<html:options items="${deviceTypeModelList}" itemValue="deviceTypeId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right">
						Recipient Channel:
					</td>
					<td align="left">
						<html:select path="recipientDeviceTypeId" cssClass="textBox" tabindex="8" >
							<html:option value="">---All---</html:option>
							<c:if test="${deviceTypeModelList != null}">
								<html:options items="${deviceTypeModelList}" itemValue="deviceTypeId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>			
				</tr>
				<tr>
					<td class="formText" align="right">
						Sender Agent ID:
					</td>
					<td align="left">
						<html:input path="agent1Id" cssClass="textBox"/>
					</td>
					<td class="formText" align="right">
						Recipient Agent ID:
					</td>
					<td align="left">
						<html:input path="agent2Id" cssClass="textBox"/>
					</td>				
				</tr>
				<tr>
					<td class="formText" align="right">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="11" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="12" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="13" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="14" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="15"/> 
						<input name="reset" type="reset" 
							onclick="javascript: window.location='p_p2ptransactiondetail.html?actionId=${retriveAction}&reportType=p_to_p'"
							class="button" value="Cancel" tabindex="16" />
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
		

		<ec:table items="transactionDetailPortalList" var="transactionDetailPortalModel"
		action="${contextPath}/p_p2ptransactiondetail.html?actionId=${retriveAction}&reportType=p_to_p"
		title=""
          retrieveRowsCallback="limit"
          filterRowsCallback="limit"
          sortRowsCallback="limit"
          filterable="false"		
		>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="P2P Transaction Details.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="P2P Transaction Details.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="P2P Transaction Details"
					fileName="P2P Transaction Details.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="P2P Transaction Details.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			
			<ec:row>
				<ec:column property="transactionCode" title="Transaction ID" escapeAutoFormat="true">
				  	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${transactionDetailPortalModel.transactionCode}" onclick="return openTransactionWindow('${transactionDetailPortalModel.transactionCode}')">
						  ${transactionDetailPortalModel.transactionCode}
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${transactionDetailPortalModel.transactionCode}
					</authz:authorize>
				</ec:column>
				<ec:column property="senderCnic" title="Sender CNIC" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="saleMobileNo" title="Sender Mobile No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="agent1Id" title="Sender Agent ID" escapeAutoFormat="true"/>
<%-- 				<ec:column property="sendingRegion" title="Sender Region" escapeAutoFormat="true"/> --%>
				<ec:column property="deviceType" title="Sender Channel"/>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Initiation Date"/>
				<ec:column property="updatedOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Completion Date"/>
				<ec:column property="transactionAmount" title="Amount" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="exclusiveCharges" title="Exclusive Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>				
 			    <ec:column property="totalAmount" title="Total Customer Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>		
				<ec:column property="recipientMobileNo" title="Recipient Mobile No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="recipientCnic" title="Recipient CNIC" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="agent2Id" title="Receiver Agent ID" escapeAutoFormat="true"/>
<%-- 				<ec:column property="receivingRegion" title="Receiver Region" escapeAutoFormat="true"/>	--%>
				<ec:column property="recipientDeviceType" title="Recipient Channel"/>
<%-- 				<ec:column property="agent2AccountNo" title="Agent 2 Account"/> --%>
				<ec:column property="processingStatusName" title="Processing Status"/>
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
		    });
      		
			Calendar.setup(
		    {
		      inputField  : "endDate", // id of the input field
		      button      : "eDate",    // id of the button
			  isEndDate: true	
		    });
      	</script>
      	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>
