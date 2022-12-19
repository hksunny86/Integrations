<%@include file="/common/taglibs.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
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
		<meta name="title" content="Utility Bill Payment Report" />
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
				action="p_marketingbillpayment.html" onsubmit="return validateForm(this)" >
				<table width="750px" border="0"><tr>
					<td align="right" class="formText">
						Transaction ID:
					</td>
					<td align="left" colspan="3">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" maxlength="12" tabindex="1" onkeypress="return maskNumber(this,event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText" width="18%">
						MSISDN:
					</td>
					<td align="left" width="32%">
						<html:input path="saleMobileNo" id="saleMobileNo" cssClass="textBox" maxlength="11" tabindex="2" onkeypress="return maskNumber(this,event)"/>
					</td>
					<td align="right" class="formText" width="18%">
						Consumer No.:
					</td>
					<td align="left" width="32%">
						<html:input path="consumerNo" id="consumerNo" cssClass="textBox" maxlength="12" tabindex="3" onkeypress="return maskNumber(this,event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Bill Type:
					</td>
					<td align="left">
						<html:select path="billType" id="billType" cssClass="textBox" tabindex="4" onchange="populateBillCompanies(this.value);" >
							<html:option value="">---All---</html:option>
							<c:if test="${billTypeSet != null}">
								<html:options items="${billTypeSet}"/>
							</c:if>
						</html:select>
					</td>
					<td align="right" class="formText">
						Bill Company:
					</td>
					<td align="left">
						<html:select path="productCode" id="billCompany" cssClass="textBox" tabindex="5" >
							<html:option value="">---All---</html:option>
							<c:if test="${billCompanySet != null}">
								<html:options items="${billCompanySet}"/>
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
						<img id="sDate" tabindex="6" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="7" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td align="right" class="formText">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="8" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="9" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="10" />
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_marketingbillpayment.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="11" />
					</td>
					<td colspan="2">
						&nbsp;
					</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</table></html:form>
		

		<ec:table items="transactionDetailPortalList" var="transactionDetailPortalModel"
		action="${contextPath}/p_marketingbillpayment.html?actionId=${retriveAction}"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Utility Bill Payment Report.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Utility Bill Payment Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Utility Bill Payment Report" fileName="Utility Bill Payment Report.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Utility Bill Payment Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>	
			<ec:row>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy" filterable="false" title="Date"/>
				<ec:column property="createdOn" cell="date" format="hh:mm a" filterable="false" sortable="false" alias="Time" width="55px"/>
				<ec:column property="transactionCode" filterable="false" title="Transaction ID" escapeAutoFormat="true">
			  	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
					<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${transactionDetailPortalModel.transactionCode}" onclick="return openTransactionWindow('${transactionDetailPortalModel.transactionCode}')">
					  ${transactionDetailPortalModel.transactionCode}
					</a>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
					${transactionDetailPortalModel.transactionCode}
				</authz:authorize>
				</ec:column>
				<ec:column property="saleMobileNo" filterable="false" title="MSISDN" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="productName" filterable="false" title="Transaction"/>
				<ec:column property="billType" filterable="false" title="Type of Bill"/>
				<ec:column property="productCode" filterable="false" title="Bill Company">
					 ${fn:toUpperCase(transactionDetailPortalModel.productCode)}
				</ec:column>
				<ec:column property="consumerNo" filterable="false" title="Consumer No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="transactionAmount" filterable="false" title="Amount" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="processingStatusName" filterable="false" title="Transaction Status" />
				<ec:column property="billAggregator" title="Service Provider" />
			</ec:row>
		</ec:table>
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

			function populateBillCompanies( billType )
			{
				var select = document.getElementById( 'billCompany' );
				select.options.length = 1; //remove all options except default option

				if( billType == '' ) //default option is selected
				{
					var billCompanies = '${billCompanySet}'; //simply alert billCompanies to view its format
					billCompanies = billCompanies.substr( 1, billCompanies.length - 2 );
					var billCompaniesArr = billCompanies.split(',');
					for( var idx=0; idx<billCompaniesArr.length; idx++ )
					{
						select.options[select.options.length] = new Option( billCompaniesArr[idx], billCompaniesArr[idx] );
					}
				}
				else
				{
					var billTypeAndCompanyCsv = '${billTypeAndCompanyCsv}';
					var billTypeAndCompaniesArr = billTypeAndCompanyCsv.split( ',' );
					for( var idx=0; idx<billTypeAndCompaniesArr.length; idx++ )
					{
						var typeAndCompany = billTypeAndCompaniesArr[idx].split( "=" );
						if( billType == typeAndCompany[0] )
						{
							select.options[select.options.length] = new Option( typeAndCompany[1], typeAndCompany[1] );
						}
					}
				}
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
