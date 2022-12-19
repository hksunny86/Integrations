<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="decorator" content="decorator">
   		<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script> 
		<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>
		
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<link rel="stylesheet" type="text/css" href="${contextPath}/styles/deliciouslyblue/calendar.css" />
		<%@include file="/common/ajax.jsp"%>
		<script language="javascript" type="text/javascript">
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
	  	</script>	    
		<meta name="title" content="Posted Transactions NADRA" />
	</head>
	<body bgcolor="#ffffff">
		<html:form name="phoenixPostedTransactionNadraSearchForm" commandName="postedTransactionNadraViewModel" method="post" action="p_postedtransactionnadra.html" onsubmit="return validateForm(this)">
			<table border="0" width="750px">
				<tr>
					<td align="right" class="formText">
						Transaction ID:
					</td>
					<td align="left" colspan="3">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" maxlength="12" tabindex="1" onkeypress="return maskInteger(this,event)" />
					</td>
				</tr>
				<tr>
					<td align="right" class="formText" width="18%">
						Transaction Type:
					</td>
					<td align="left" width="32%">
						<html:select path="intgTransactionTypeId" id="intgTransactionTypeId" cssClass="textBox" tabindex="2">
							<html:option value="">---All---</html:option>
							<c:if test="${intgTransactionTypeModelList != null}">
								<html:options items="${intgTransactionTypeModelList}" itemValue="intgTransactionTypeId" itemLabel="transactionType"/>
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right" width="18%">
						Product:
					</td>
					<td align="left" width="32%">
						<html:select path="productId" cssClass="textBox" tabindex="3">
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						RRN:
					</td>
					<td align="left">
						<html:input path="refCode" id="refCode" cssClass="textBox" maxlength="16" tabindex="4" onkeypress="return maskInteger(this,event)"/>
					</td>
					<td align="right" class="formText">
						Consumer No.:
					</td>
					<td align="left">
						<html:input path="consumerNo" id="consumerNo" cssClass="textBox" maxlength="16" tabindex="5" onkeypress="return maskInteger(this,event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						<span class="asterisk">*</span>Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="6" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="7" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0"/>
					</td>
					<td align="right" class="formText">
						<span class="asterisk">*</span>End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="8" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="9" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0"/>
					</td>
				</tr>
			  <tr>
				<td></td>
			    <td align="left" >
			    	<input type="submit" class="button" value="Search" tabindex="10" name="_search"/>
			    	<input type="reset" class="button" value="Cancel" name="_cancel" tabindex="11" onClick="javascript: window.location='p_postedtransactionnadra.html'">
			   	</td>
			  </tr>
	 	 	</table>
		</html:form>

		<ec:table filterable="false" items="postedTransactionNadraViewModelList" var="postedTransactionNadraViewModel" retrieveRowsCallback="limit" filterRowsCallback="limit" 
		sortRowsCallback="limit" action="${contextPath}/p_postedtransactionnadra.html" title="" width="850px">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Posted Transactions NADRA.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Posted Transactions NADRA.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Posted Transactions NADRA" fileName="Posted Transactions NADRA.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Posted Transactions NADRA.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="createdOn" alias="Date" cell="date" format="dd/MM/yyyy"/>
				<ec:column property="createdOn" alias="Time" cell="date" format="hh:mm a" sortable="false" width="55px"/>
				<ec:column property="transactionCode" title="Transaction ID" style="text-align: center" escapeAutoFormat="true"/>
				<ec:column property="transactionType" title="Transaction Type" style="text-align: center"/>
				<ec:column property="productName" title="Product" style="text-align: center"/>
				<ec:column property="refCode" title="RRN" style="text-align: center" escapeAutoFormat="true"/>
				<ec:column property="consumerNo" title="Consumer No." style="text-align: center" escapeAutoFormat="true"/>
				<ec:column property="amount" title="Amount" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="responseCode" title="Response Code" style="text-align: center"/>
			</ec:row>
		</ec:table>
		<script language="javascript" type="text/javascript">
	      document.forms[0].transactionCode.focus();

	      function validateForm(form){
	        	var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        	var _fDate = form.startDate.value;
		  		var _tDate = form.endDate.value;
		  		var startlbl = "Start Date";
		  		var endlbl   = "End Date";
	        	var isValid = validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,currentDate);
	        	if( isValid )
	        	{
	        		isValid = validateFormChar(form);
	        	}
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
