<!--Author: Naseer Ullah -->
<%@ page import='com.inov8.microbank.common.util.*'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants" %>
<%@page import="com.inov8.microbank.common.util.ReportConstants" %>
<%@ page import='java.util.Calendar'%>
<%@ page import='java.util.GregorianCalendar'%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="decorator" content="decorator">
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Branchless Banking Accounts"  id="<%=ReportIDTitleEnum.BranchlessBankingAccountsReport.getId()%>" />
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
		<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
        <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css"/>
        <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		<script type="text/javascript">
			var jq=$.noConflict();
			var serverDate ="<%=PortalDateUtils.getServerDate()%>";
			var username = "<%=UserUtils.getCurrentUser().getUsername()%>";
        	var appUserId= "<%=UserUtils.getCurrentUser().getAppUserId()%>";
        	var email= "<%=UserUtils.getCurrentUser().getEmail()%>";
			
			function actdeact(request)
			{
				isOperationSuccessful(request);
			}

			var maxDob = new Date();

			function populateMaxDob()
			{
				maxDob.setHours( 0 );
				maxDob.setMinutes( 0 );
				maxDob.setSeconds( 0 );
				maxDob.setMilliseconds( 0 );
				<%
					Calendar curDate = GregorianCalendar.getInstance();
				%>
				var maxDobYear = <%=curDate.get(Calendar.YEAR) - 18%>;
				var month = <%=curDate.get( Calendar.MONTH )%>;
				var date = <%=curDate.get( Calendar.DATE )%>; 
				maxDob.setFullYear( maxDobYear );
				maxDob.setMonth( month );
				maxDob.setDate( date );
			}
			window.onload = populateMaxDob();
		</script>
		<%@include file="/WEB-INF/pages/export_zip.jsp"%>	
		<%-- <script type="text/javascript" src="${contextPath}/scripts/exportzip.js"></script> --%>
	<%@include file="/common/ajax.jsp"%>
		
	</head>
   	<body bgcolor="#ffffff">
		<div id="rsp" class="ajaxMsg"></div>
		<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
			<html:form action="p_bbcustomeraccounts.html" name="bbCustomerAccountsViewForm" method="post" commandName="bbCustomerAccountsViewModel" onsubmit="return validateForm(this);">
				<table width="950px">
				<tr>
					<td width="25%" align="right" class="formText">
						First Name:
					</td>
					<td width="25%" align="left">
						<html:input path="firstName" tabindex="1" id="firstName" cssClass="textBox" maxlength="20"/>
					</td>
					<td class="formText" align="right" width="25%">
						Last Name:
					</td>
					<td align="left">
						<html:input path="lastName" tabindex="2" id="lastName" cssClass="textBox" maxlength="20"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						CNIC:
					</td>
					<td align="left">
						<html:input onkeypress="return maskNumber(this,event)" path="cnic" tabindex="3" id="cnic" cssClass="textBox" maxlength="13"/>
					</td>
					<td align="right" class="formText">
						Mobile No.:
					</td>
					<td align="left">
						<html:input onkeypress="return maskNumber(this,event)" path="mobileNumber" tabindex="4" id="mobileNumber" cssClass="textBox" maxlength="11"/>
					</td>
				</tr>
                    <tr>
                        <td class="formText" align="right">
                            Segment:
                        </td>
                        <td align="left">
                            <html:select path="segmentId" cssClass="textBox" tabindex="10" >
                                <html:option value="">---All---</html:option>
                                <c:if test="${segmentModelList != null}">
                                    <html:options items="${segmentModelList}" itemValue="segmentId" itemLabel="name"/>
                                </c:if>
                            </html:select>
                        </td>
                        <td>&nbsp;</td>
                    </tr>
				<tr>
					<td align="right" class="formText">
						DOB:
					</td>
					<td align="left">
				        <html:input path="dob" id="dob" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="dateOfBirth" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0"/>
						<img id="dateOfBirth" tabindex="6" title="Clear Date" name="popcal" onclick="javascript:$('dob').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0"/>
					</td>
					<td align="right" class="formText">
						Account No.:
					</td>
					<td align="left">
					     <html:input onkeypress="return maskNumber(this,event)" path="accountNumber" tabindex="7" id="accountNumber" cssClass="textBox" maxlength="11"/>
					</td>
				</tr>
					<tr>
						<td class="formText" align="right">
							Account Created On - Start:
						</td>
						<td align="left">
							<html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="11" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="12" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
						</td>
						<td class="formText" align="right">
							Account Created On - End:
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
							<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
								   value="<%=PortalConstants.ACTION_RETRIEVE%>">
							<input name="_search" type="submit" class="button" value="Search" tabindex="13"/>
							<input name="reset" type="reset"
								   onclick="javascript: window.location='p_bbcustomeraccounts.html?actionId=${retriveAction}'"
								   class="button" value="Cancel" tabindex="14" />
						</td>
						<td class="formText" align="right">
						</td>
						<td align="left">
						</td>
					</tr>
			    <tr>
					<td colspan="2">
						<a href="#" id="btnZippedXLS" name="btnZippedXLS"><img src="images/table/xls.gif" style="display:none"/></a>
						<a href="#" id="btnZippedXLSX" name="btnZippedXLSX"><img src="images/table/xlsx.gif" style="display:none"/></a>
						<a href="#" id="btnZippedCSV" name="btnZippedCSV"><img src="images/table/csv.gif" style="display:none"/></a>
					</td>
			    </tr>
			</table>
			<%@ include file="../zip_report.jsp" %>
</html:form>
		 
        <ec:table items="bbCustomerAccountsViewModelList" var="_bbCustomerAccountsViewModel" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit"
			action="${contextPath}/p_bbcustomeraccounts.html" title="" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="BB Customer Accounts.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="BB Customer Accounts.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="BB Customer Accounts" fileName="BB Customer Accounts.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="BB Customer Accounts.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
	        <ec:row>
	        	<ec:column property="taxRegimeName" title="Tax Regime"/>
                <ec:column property="regionName" title="Region"/>
                <ec:column property="areaName" title="Area"/>
                <ec:column property="cityName" title="City"/>
                <ec:column property="customerAccountType" title="Account Type"/>
				<ec:column property="lastActivityDate" title="Last Activity Date" cell="date" format="dd/MM/yyyy hh:mm a"/>
				<ec:column property="accountOpenDate" title="Account Opening Date" cell="date" format="dd/MM/yyyy hh:mm a"/>
                <ec:column property="firstName" title="First Name"/>
				<ec:column property="lastName" title="Last Name"/>
				<ec:column property="cnic" title="CNIC" sortable="false" escapeAutoFormat="true" style="text-align:center;" />
				<ec:column property="mobileNumber" title="Mobile No." escapeAutoFormat="true" style="text-align:center;" />
	            <ec:column property="dob" title="DOB" width="10%" sortable="false" style="text-align:center;"/>
	            <ec:column property="accountNumber" title="Account No." sortable="false" escapeAutoFormat="true" style="text-align:center;" />
	            <ec:column property="balance" title="Balance" sortable="false" style="text-align:right;"/>
				<ec:column property="segment" filterable="false"  title="Segment" />
				<ec:column property="isActive" title="Active" escapeAutoFormat="True" style="text-align: center">
					<c:if test ="${_bbCustomerAccountsViewModel.isActive ==false}">No</c:if>
					<c:if test ="${_bbCustomerAccountsViewModel.isActive ==true}">Yes</c:if>
				</ec:column>
	        </ec:row>
        </ec:table>

      <script language="javascript" type="text/javascript">
      	document.forms[0].firstName.focus();

     	function changeInfo(link)
		{
			window.location.href=link;
		}
        function confirmUpdateStatus(link)
      	{
        	if (confirm('Are you sure you want to update status?')==true)
	        {
	          window.location.href=link;
	        }
        }

		Calendar.setup(
     	{
	       inputField  : "dob", 	   // id of the input field
	       ifFormat    : "%d/%m/%Y",   // the date format
	       button      : "dateOfBirth", // id of the button
	       range       : [1940,maxDob.getFullYear()]
	    });

		Calendar.setup({
			inputField : "startDate", // id of the input field
			button : "sDate", // id of the button
		});

		Calendar.setup({
			inputField : "endDate", // id of the input field
			button : "eDate", // id of the button
			isEndDate : true
		});
		     
	  </script>
	  
	<%-- 	<%@ include file="../post_zip_report.jsp" %> --%>
	  
	  <script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
				
   </body>
</html>