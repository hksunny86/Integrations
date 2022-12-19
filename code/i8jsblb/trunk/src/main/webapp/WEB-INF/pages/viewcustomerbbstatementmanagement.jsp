<!--Author: Naseer Ullah-->
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.BBStatementReportConstants" %>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    	<script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    	<script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    	<script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="Customer BB Statement"/>
		<%@include file="/common/ajax.jsp"%>
		<script language="javascript" type="text/javascript">
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
			
	      function initProgress(){
				if (confirm('If Transaction information is verified then press OK to continue')==true){
			    	return true;
			    }else{
				  $('errorMsg').innerHTML = "";
				  $('successMsg').innerHTML = "";
	 			  $('message').value="";
				  Element.hide('successMsg'); 
				  Element.hide('errorMsg'); 
				  return false;
				}
			}
	      
	  	var isErrorOccured = false;		
		function resetProgress(){
			if(!isErrorOccured){
			    $('errorMsg').innerHTML = "";
		  		Element.hide('errorMsg'); 
			    $('successMsg').innerHTML = $F('message');
			    var btnName = $F('currentBtn');
			    if(btnName != ''){
			  		document.getElementById('revBtn_'+btnName).disabled=true;
			  		document.getElementById('revBtn_'+btnName).value="Reversed";
			  	}
			    Element.show('successMsg');
			} 
			isErrorOccured = false;
		}
	</script>
		<%
			String readPermission = PortalConstants.MNG_GEN_ACC_READ;
			readPermission +=	"," + PortalConstants.CSR_GP_READ;
			readPermission +=	"," + PortalConstants.PG_GP_READ;
			readPermission +=	"," + PortalConstants.MNG_BB_CUST_READ;
			readPermission +=	"," + PortalConstants.RET_GP_READ;
			readPermission +=	"," + PortalConstants.ADMIN_GP_READ;
	
			String updatePermission = PortalConstants.MNG_GEN_ACC_UPDATE;
			updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
			updatePermission +=	"," + PortalConstants.SEND_BB_TX_REVERSAL;
				
			String reprintPermission = PortalConstants.RET_GP_READ;
			reprintPermission += "," + PortalConstants.ADMIN_GP_READ;
			reprintPermission +=	"," + PortalConstants.PG_GP_READ;
			reprintPermission +=	"," + PortalConstants.BB_CUST_REPRINT_FORM_READ;
		 %>
	</head>
	<body bgcolor="#ffffff">
		<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<div id="errorMsg" class ="errorMsg" style="display:none;"></div>
		<html:form name="customerBbStatementForm" action="viewcustomerbbstatementmanagement.html" commandName="customerBbStatementViewModel" onsubmit="return validateForm(this);">
			<table width="750px">				
				<tr>
					<td align="right" class="formText">
						Transaction ID:
					</td>
					<td align="left" colspan="3">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" maxlength="12" tabindex="1" onkeypress="return maskNumber(this,event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText" width="18%">Account Type:</td>
					<td align="left" width="32%">
						<html:select path="paymentModeId" id="acType" name="acType" cssClass="textBox">
							<html:option value="">[All]</html:option>
							<c:if test="${accountTypeList != null}">
								<html:options items="${accountTypeList}" itemLabel="name" itemValue="customerAccountTypeId" />
							</c:if>
						</html:select>
						<%--<select id="acType" name="acType" cssClass="textBox" tabindex="6" >
							<option value="">---ALL---</option>
							<c:forEach items="${accountTypeList}" var="accountType">
								<option value="${accountType}">${accountType}</option>
							</c:forEach>
						</select>--%>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText" width="18%">
						Start Date:
					</td>
					<td align="left" width="32%">
				        <html:input path="dateRangeHolderModel.fromDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="2" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="3" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td align="right" class="formText" width="18%">
						End Date:
					</td>
					<td align="left" width="32%">
					     <html:input path="dateRangeHolderModel.toDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="4" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="5" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="left" class="formText">
						<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="${retriveAction}"/>
						<input type="hidden" name="appUserId" value="${param.appUserId}"/>
						<input type="hidden" name="referrer" value="${param.referrer}"/>
						<input type="submit" class="button" value="Search" name="_search" tabindex="5"/>
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='viewcustomerbbstatementmanagement.html?appUserId=${param.appUserId}&actionId=${retriveAction}&referrer=${param.referrer}'" tabindex="6"/>
					</td>
				</tr>
			</table>
		</html:form>
		
		<c:if test="${not empty customerBbStatementViewModelList}">
			<table width="750px">
				
					<tr>
						<td align="left" class="formText" width="40%">
							
						</td>
						<td align="right" class="formText">
							Issued on :&nbsp; ${requestScope.reportHeaderMap.issueDate}
						</td>
					</tr>
					<tr>
						<td align="left" class="formText" width="40%">
							<b>BRANCH NAME</b>
						</td>
						<td align="right" class="formText">
							<b>Statement Period</b>
						</td>
					</tr>
					<tr>
						<td align="left" class="formText" width="40%">
							${requestScope.reportHeaderMap.branchName1}
						</td>
						<td align="right" class="formText">
							From:&nbsp;${requestScope.reportHeaderMap.statementDateRange} 
						</td>
					</tr>
					<tr>
						<td align="left" class="formText" width="40%">
							${requestScope.reportHeaderMap.branchName2}
						</td>
					</tr>
			</table>	
			<table width="750px">
					<tr>
						<td align="left" class="formText" width="20%">
							<b>Name:</b>
						</td>
						<td align="left" class="formText">
							${requestScope.reportHeaderMap.customerName}
						</td>
						<td align="left" class="formText" width="20%">
							BB Mobile Account#
						</td>
						<td align="right" class="formText">
							${requestScope.reportHeaderMap.zongMsisdn}
						</td>
					</tr>
					<tr>
						<td align="left" class="formText" width="20%">
							Address:
						</td>
						<td align="left" class="formText">
							${requestScope.reportHeaderMap.address1}
						</td>
						<td align="left" class="formText" width="20%">
							Customer ID#
						</td>
						<td align="right" class="formText">
							${requestScope.reportHeaderMap.customerId}
						</td>
					</tr>
					<tr>
						<td align="left" class="formText" width="20%">						
													
						</td>
						<td align="left" class="formText">
							${requestScope.reportHeaderMap.address2}
						</td>
						<td align="left" class="formText" width="20%">
							Currency
						</td>
						<td align="right" class="formText">
							${requestScope.reportHeaderMap.currency}
						</td>
					</tr>
					<tr>
						<td align="left" class="formText" width="20%">
							
						</td>
						<td align="left" class="formText">
							${requestScope.reportHeaderMap.address3}
						</td>
						<td align="left" class="formText" width="20%">
							Account Type
						</td>
						<td align="right" class="formText">
							${requestScope.reportHeaderMap.accountLevel}
						</td>
					</tr>
					
				
			</table>
		</c:if>
		

		<c:if test="${not empty openingBalance}">
			<div align="right" bgcolor="F3F3F3" class="formText">
				Opening Balance : <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openingBalance}"></fmt:formatNumber>
			</div>
		</c:if>

		<ec:table filterable="false" items="customerBbStatementViewModelList" var="bbStatementViewModel" action="${contextPath}/viewcustomerbbstatementmanagement.html"
			retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" showPagination="true" sortable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls view="com.inov8.microbank.common.util.ViewCustomerBbStatementXlsView" fileName="Customer BB Statement.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Customer BB Statement.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<c:if test="${isHRA}" >
					<ec:exportPdf view="com.inov8.microbank.common.util.CustomerBBStatementPDFView" headerBackgroundColor="#b6c2da"
								  headerTitle="Customer HRA BB Statement" fileName="Customer BB Statement.pdf" tooltip="Export PDF" />
				</c:if>
				<c:if test="${not isHRA}" >
					<ec:exportPdf view="com.inov8.microbank.common.util.CustomerBBStatementPDFView" headerBackgroundColor="#b6c2da"
								  headerTitle="Customer BLB BB Statement" fileName="Customer BB Statement.pdf" tooltip="Export PDF" />
				</c:if>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Customer BB Statement.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="transactionTime" title="Transaction Date" cell="date" format="dd/MM/yyyy hh:mm a"/>
				<ec:column property="transactionCode" title="Transaction ID" escapeAutoFormat="true"/>
				<ec:column property="transactionSummaryText" title="Description" viewsAllowed="html"/>
				<ec:column property="transactionSummaryTextEscape" title="Description" viewsDenied="html"/>
				<ec:column property="debitAmount" title="Debit" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="creditAmount" title="Credit" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="balanceAfterTransaction" title="Balance" cell="currency" format="0.00" style="text-align: right"/>
			</ec:row>
		</ec:table>

		<c:if test="${not empty closingBalance}">
			<div align="right" bgcolor="F3F3F3" class="formText">
				Closing Balance : <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalance}"></fmt:formatNumber>
			</div>
		</c:if>
		<input type="button" class="button" value="Back" tabindex="48" onclick="javascript: window.location.href='p_mnomfsaccountdetails.html?appUserId=${param.appUserId}&actionId=2&referrer=${param.referrer}'" />
		<script type="text/javascript">		
			document.forms[0].startDate.focus();
			function validateForm(form)
			{
				var isValid=true;
				var currentDate = "<%=PortalDateUtils.getServerDate()%>";
				var _transactionCode=form.transactionCode.value;
	        	var _fDate = form.startDate.value;
	  			var _tDate = form.endDate.value;
				var accountType = form.acType.value;
				if(accountType == "")
				{
					alert("Please Select Valid Account Type.");
					isValid=false;
					return isValid;
				}
		  		var startlbl = "Start Date";
		  		var endlbl   = "End Date";
		  		if(_transactionCode=="" && _fDate=="" && _tDate=="" && accountType == ""){
		  			alert("Please Provide Date Range or Transaction ID");
		  			isValid=false; 
		  		}
		  		else if (_transactionCode!="" && (_fDate!="" || _tDate!="")) {
		  			isValid = validateDateRange(_fDate,_tDate,startlbl,endlbl,currentDate);
		  		}
		  		else if (_transactionCode=="" && (_fDate!="" || _tDate!="")) {
		  			isValid = validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,currentDate);
		  		}

	        	return isValid;
	        }

      		Calendar.setup(
		    {
		        inputField  : "startDate", // id of the input field
		        ifFormat    : "%d/%m/%Y",      // the date format
		        button      : "sDate",    // id of the button
		      	showsTime   :   false
		    }
		    );
		      
		    Calendar.setup(
		    {
		        inputField  : "endDate", // id of the input field
		        ifFormat    : "%d/%m/%Y",      // the date format
		        button      : "eDate",    // id of the button
		        showsTime   :   false,
		      }
		    );
		</script>
	</body>
</html>
