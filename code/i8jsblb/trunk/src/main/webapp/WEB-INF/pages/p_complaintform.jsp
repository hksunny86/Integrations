<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*,com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>  
<meta name="decorator" content="decorator2">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>

   <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />
   <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
      
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
       
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

     <meta name="title" content="Add Complaint"/>

	<script language="javascript" type="text/javascript">
		function error(request){
			alert("Network failure error has been occurred .Please try again later or consult your system administrator");
			document.getElementById("btnSubmit").disabled = false;
			document.getElementById("complaintCategoryId").value='';
		}
		
	  
	   function enableSubmit(){
	  	document.getElementById("btnSubmit").disabled = false;
	  
	  	} 
		
		function showHideParams(){
			var combo = document.getElementById('complaintCategoryId');
			var selectedCategory = combo.options[combo.selectedIndex].value;
			document.getElementById('otherContactNoStar').style.display = 'none';

			if(selectedCategory == <%=ComplaintsModuleConstants.CATEGORY_UTILITY%>){
				showCategoryParams('fieldsUtility');
			}else if(selectedCategory == <%=ComplaintsModuleConstants.CATEGORY_FUND_TRANSFER%>){
				showCategoryParams('fieldsFT');
			}else if(selectedCategory == <%=ComplaintsModuleConstants.CATEGORY_AGENT_COMPLAINT%>){
				showCategoryParams('fieldsAgent');
			}else if(selectedCategory == <%=ComplaintsModuleConstants.CATEGORY_BB_ACCOUNT%>){
				document.getElementById("otherContactNoStar").style.display = 'inline';
				showCategoryParams('fieldsBBAccount');
			}else if(selectedCategory == <%=ComplaintsModuleConstants.CATEGORY_TOP_UP%>){
				showCategoryParams('fieldsTopUp');
			}else if(selectedCategory == <%=ComplaintsModuleConstants.CATEGORY_CHARGEBACK%>){
				showCategoryParams('fieldsChargeBack');
			}else{
				showCategoryParams('none');
			}
			
			if (!document.getElementById("complaintCategoryId").value=='')
				document.getElementById("btnSubmit").disabled = true;
			
			return true;
		}
		
		function showCategoryParams(selectedCategoryDivId){
			document.getElementById('fieldsUtility').style.display = 'none';
			document.getElementById('fieldsFT').style.display = 'none';
			document.getElementById('fieldsAgent').style.display = 'none';
			document.getElementById('fieldsBBAccount').style.display = 'none';
			document.getElementById('fieldsTopUp').style.display = 'none';
			document.getElementById('fieldsChargeBack').style.display = 'none';
			if(selectedCategoryDivId != 'none'){
				document.getElementById(selectedCategoryDivId).style.display = 'inline';
			}
		}
		var currentDate = new Date();

	  function setCurrentDate()
	  {
	   		currentDate.setHours( 0 );
			currentDate.setMinutes( 0 );
			currentDate.setSeconds( 0 );
			<%
				Calendar curDate = GregorianCalendar.getInstance();
			%>
			var year = <%=curDate.get( Calendar.YEAR )%>;
			var month = <%=curDate.get( Calendar.MONTH )%>;
			var date = <%=curDate.get( Calendar.DATE )%>;
			currentDate.setFullYear( year );
			currentDate.setMonth( month );
			currentDate.setDate( date );
	  }
	   window.onload = setCurrentDate();
		
	</script>
    
</head>

   <body bgcolor="#ffffff" onload="javascript:setCurrentDate();">
		<%@include file="/common/ajax.jsp"%>
   
      <spring:bind path="complaintModelVO.*">
	  <c:if test="${not empty status.errorMessages}">
	    <div class="errorMsg">
	      <c:forEach var="error" items="${status.errorMessages}">
	        <c:out value="${error}" escapeXml="false"/>
	        <br/>
	      </c:forEach>
	    </div>
	  </c:if>
	</spring:bind>
	
	<c:if test="${not empty messages}">
	    <div class="infoMsg" id="successMessages">
	        <c:forEach var="msg" items="${messages}">
	            <c:out value="${msg}" escapeXml="false"/><br/>
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
	
	
     
	  
        <html:form name="complaintForm" commandName="complaintModelVO"  onsubmit="return onFormSubmit(this)"
         action="p_complaintform.html?actionId=1">

            <c:if test="${not complaintModelVO.isWalkin}">
			<input type="hidden" name="initiatorMobileNo" value="${complaintModelVO.initiatorMobileNo}"/>
			<input type="hidden" name="initiatorFirstName" value="${complaintModelVO.initiatorFirstName}"/>
			<input type="hidden" name="initiatorCity" value="${complaintModelVO.initiatorCity}"/>
			<input type="hidden" name="initiatorId" value="${complaintModelVO.initiatorId}"/>
			</c:if>
			
			<input type="hidden" name="appUserId" value="${complaintModelVO.appUserId}"/>
			<input type="hidden" name="initiatorNIC" value="${complaintModelVO.initiatorNIC}"/>
			<input type="hidden" name="isCustomer" value="${complaintModelVO.isCustomer}"/>
			<input type="hidden" name="isAgent" value="${complaintModelVO.isAgent}"/>
			<input type="hidden" name="isHandler" value="${complaintModelVO.isHandler}"/>
			<input type="hidden" name="isWalkin" value="${complaintModelVO.isWalkin}"/>

    
         <table width="843px" border="0" cellpadding="0" cellspacing="1">
           <tr bgcolor="FBFBFB">
             <td width="42%" align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Complaint Category:</td>
             <td align="left">
				<html:select path="complaintCategoryId" cssClass="textBox">
					<html:option value="" label="---SELECT---"/>
				    <html:options items="${complaintCategoryModelList}" itemValue="complaintCategoryId" itemLabel="name"/>
				</html:select>			       
             </td>
           </tr>

           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Complaint Nature:</td>
             <td align="left">
				<spring:bind path="complaintModelVO.complaintSubcategoryId">
					<select name="${status.expression}" class="textBox" tabindex="2" id="${status.expression}" >
						<c:if test="${empty complaintSubcategoryList}">
							<option value="" label="---SELECT---"/>
						</c:if>
						<c:if test="${not empty complaintSubcategoryList}">
							<c:forEach items="${complaintSubcategoryList}" var="subCategoryModel">
								<option value="${subCategoryModel.complaintSubcategoryId}"
									<c:if test="${status.value == subCategoryModel.complaintSubcategoryId}">selected="selected"</c:if>>
									${subCategoryModel.name}
								</option>
							</c:forEach>
						</c:if>
					</select>
				</spring:bind>

             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><c:if test="${complaintModelVO.isWalkin}"><span style="color:#FF0000">*</span></c:if>Name:</td>
             <td align="left">
             	<c:if test="${complaintModelVO.isWalkin}">
             		<html:input path="initiatorFirstName" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"/>
             	</c:if>
             	${complaintModelVO.initiatorFirstName}
             </td>
           </tr>
	       
	       <c:if test="${complaintModelVO.isCustomer || complaintModelVO.isAgent || complaintModelVO.isHandler}">
	           <tr bgcolor="FBFBFB">
		         <td align="right" height="16" bgcolor="F3F3F3" class="formText">
		             <c:if test="${complaintModelVO.isCustomer}">Customer ID:</c:if>
		             <c:if test="${complaintModelVO.isAgent}">Agent ID:</c:if>
		             <c:if test="${complaintModelVO.isHandler}">Handler ID:</c:if>
	             </td>
	             <td align="left">${complaintModelVO.initiatorId}</td>
	           </tr>
		   </c:if>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><c:if test="${complaintModelVO.isWalkin}"><span style="color:#FF0000">*</span></c:if>MSISDN No:</td>
             <td align="left">
             	<c:if test="${complaintModelVO.isWalkin}">
             		<html:input path="initiatorMobileNo" cssClass="textBox" maxlength="11" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             	</c:if>
             	<c:if test="${not complaintModelVO.isWalkin}">
             		${complaintModelVO.initiatorMobileNo}
             	</c:if>
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><c:if test="${complaintModelVO.isWalkin}"><span style="color:#FF0000">*</span></c:if>CNIC:</td>
             <td align="left">
             	${complaintModelVO.initiatorNIC}
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><c:if test="${complaintModelVO.isWalkin}"><span style="color:#FF0000">*</span></c:if>City/Location:</td>
             <td align="left">
             	<c:if test="${complaintModelVO.isWalkin}">
             		<html:input path="initiatorCity" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"/>
             	</c:if>
             	${complaintModelVO.initiatorCity}
             </td>
           </tr>

           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><div id="otherContactNoStar" style="display:none;"><span style="color:#FF0000">*</span></div>Other Contact No:</td>
             <td align="left">
             	<html:input path="otherContactNo" cssClass="textBox" tabindex="3" maxlength="13" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Description:</td>
             <td align="left">
             	<html:textarea path="complaintDescription" rows="4" cssClass="textBox"  onkeyup="textAreaLengthCounter(this,1000);" tabindex="4"/>
             </td>
           </tr>


		   <tr>
           <td colspan="2">
		   
		   <div id="fieldsUtility" style="display:none;">
		   <table width="100%" border="0" cellpadding="0" cellspacing="1">
			<tr><td colspan="2" align="left" bgcolor="F3F3F3">
				<h3>Complaint Details:</h3>
			</td></tr>

		   <tr bgcolor="FBFBFB">
             <td width="50%" align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Type of Utility Service Provider:</td>
             <td align="left">
				<html:select path="serviceProviderType" cssClass="textBox" tabindex="4" onchange="populateBillCompanies(this.value);" >
					<html:option value="">--- Select ---</html:option>
					<c:if test="${billTypeSet != null}">
						<html:options items="${billTypeSet}"/>
					</c:if>
				</html:select>
             </td>
           </tr>

           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Service Provider Company Name:</td>
             <td align="left">
				<html:select path="serviceProviderName" id="serviceProviderName" cssClass="textBox">
					<html:option value="">--- Select ---</html:option>
				</html:select>
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Consumer No:</td>
             <td align="left">
             	<html:input path="consumerNumber" cssClass="textBox" tabindex="3" maxlength="16" onkeypress="return maskInteger(this,event)"/>
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText">Transaction ID:</td>
             <td align="left">
             	<html:input path="utilityTrxId" cssClass="textBox" tabindex="3" maxlength="12" onkeypress="return maskInteger(this,event)"/>
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Payment Date:</td>
             <td align="left">
				<spring:bind path="complaintModelVO.paymentDate">
					<input type="text" name="${status.expression}" class="textBox" id="${status.expression}" maxlength="50" value="${status.value}" readonly="readonly" tabindex="36" />
				</spring:bind>
				<img id="sDate" tabindex="37" name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" tabindex="3" />
				<img id="sDate" tabindex="37" name="popcal" title="Clear Date" onclick="javascript:$('paymentDate').value=''" align="middle" style="cursor:pointer" tabindex="37" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Payment Amount:</td>
             <td align="left">
             	<html:input path="paymentAmount" cssClass="textBox" tabindex="3" maxlength="9" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>

           </table>
		   </div>


		   <div id="fieldsFT" style="display:none;">
		   <table width="100%" border="0" cellpadding="0" cellspacing="1">
			<tr><td colspan="2" align="left" bgcolor="F3F3F3">
				<h3>Complaint Details:</h3>
			</td></tr>
		   <tr bgcolor="FBFBFB">
             <td width="50%" align="right" height="16" bgcolor="F3F3F3" class="formText">Transaction ID:</td>
             <td align="left">
             	<html:input path="transactionId" cssClass="textBox" tabindex="3" maxlength="12" onkeypress="return maskInteger(this,event)"/>
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Transaction Date:</td>
             <td align="left">
				<spring:bind path="complaintModelVO.transactionDate">
					<input type="text" name="${status.expression}" class="textBox" id="${status.expression}" maxlength="50" value="${status.value}" readonly="readonly" tabindex="36" />
				</spring:bind>
				<img id="trxDate" tabindex="37" name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" tabindex="3" />
				<img id="trxDate" tabindex="37" name="popcal" title="Clear Date" onclick="javascript:$('transactionDate').value=''" align="middle" style="cursor:pointer" tabindex="37" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td width="50%" align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Amount Transferred:</td>
             <td align="left">
             	<html:input path="amountTransferred" cssClass="textBox" tabindex="3" maxlength="9" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText">Sender MSISDN:</td>
             <td align="left">
             	<html:input path="senderMSISDN" cssClass="textBox" tabindex="3" maxlength="11" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText">Sender CNIC:</td>
             <td align="left">
             	<html:input path="senderCNIC" cssClass="textBox" tabindex="3" maxlength="13" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Sender Account No:</td>
             <td align="left">
             	<html:input path="senderAccountNo" cssClass="textBox" tabindex="3" maxlength="14" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText">Recipient MSISDN:</td>
             <td align="left">
             	<html:input path="recipientMSISDN" cssClass="textBox" tabindex="3" maxlength="11" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText">Recipient CNIC:</td>
             <td align="left">
             	<html:input path="recipientCNIC" cssClass="textBox" tabindex="3" maxlength="13" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Recipient Account No:</td>
             <td align="left">
             	<html:input path="recipientAccountNo" cssClass="textBox" tabindex="3" maxlength="14" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
           </table>
           </div>
		   
		   
		   <div id="fieldsAgent" style="display:none;">
		   <table width="100%" border="0" cellpadding="0" cellspacing="1">
			<tr><td colspan="2" align="left" bgcolor="F3F3F3">
				<h3>Complaint Details:</h3>
			</td></tr>
           <tr bgcolor="FBFBFB">
             <td width="50%" align="right" height="16" bgcolor="F3F3F3" class="formText">Agent Location:</td>
             <td align="left">
             	<html:input path="agentLocation" cssClass="textBox" tabindex="3" maxlength="100"/>
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Agent ID:</td>
             <td align="left">
             	<html:input path="agentId" cssClass="textBox" tabindex="3" maxlength="7" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Shop Name:</td>
             <td align="left">
             	<html:input path="shopName" cssClass="textBox" tabindex="3" maxlength="50"/>
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText">Shop Address:</td>
             <td align="left">
             	<html:input path="shopAddress" cssClass="textBox" tabindex="3" maxlength="100"/>
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText">Transaction ID:</td>
             <td align="left">
             	<html:input path="agentTransactionId" cssClass="textBox" tabindex="3" maxlength="12" onkeypress="return maskInteger(this,event)"/>
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Transaction Amount:</td>
             <td align="left">
             	<html:input path="agentPaymentAmount" cssClass="textBox" tabindex="3" maxlength="9" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Sender Agent MSISDN:</td>
             <td align="left">
             	<html:input path="senderAgentMSISDN" cssClass="textBox" tabindex="3" maxlength="11" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Receiver Agent MSISDN:</td>
             <td align="left">
             	<html:input path="receiverAgentMSISDN" cssClass="textBox" tabindex="3" maxlength="11" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>           
           <tr bgcolor="FBFBFB">
             <td width="50%" align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Transaction Date:</td>
             <td align="left">
				<spring:bind path="complaintModelVO.agentTransactionDate">
					<input type="text" name="${status.expression}" class="textBox" id="${status.expression}" maxlength="50" value="${status.value}" readonly="readonly" tabindex="36" />
				</spring:bind>
				<img id="agTransDate" tabindex="37" name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" tabindex="3" />
				<img id="agTransDate" tabindex="37" name="popcal" title="Clear Date" onclick="javascript:$('agentTransactionDate').value=''" align="middle" style="cursor:pointer" tabindex="37" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
             </td>
           </tr>
           
           </table>
           </div>

		   <div id="fieldsTopUp" style="display:none;">
		   <table width="100%" border="0" cellpadding="0" cellspacing="1">
			<tr><td colspan="2" align="left" bgcolor="F3F3F3">
				<h3>Complaint Details:</h3>
			</td></tr>
		   <tr bgcolor="FBFBFB">
             <td width="50%" align="right" height="16" bgcolor="F3F3F3" class="formText">Transaction ID:</td>
             <td align="left">
             	<html:input path="topUpTrxId" cssClass="textBox" tabindex="3" maxlength="12" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td width="50%" align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Mobile No:</td>
             <td align="left">
             	<html:input path="topUpMobileNo" cssClass="textBox" tabindex="3" maxlength="11" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Top Up Date:</td>
             <td align="left">
				<spring:bind path="complaintModelVO.topUpDate">
					<input type="text" name="${status.expression}" class="textBox" id="${status.expression}" maxlength="50" value="${status.value}" readonly="readonly" tabindex="36" />
				</spring:bind>
				<img id="topDate" tabindex="37" name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" tabindex="3" />
				<img id="topDate" tabindex="37" name="popcal" title="Clear Date" onclick="javascript:$('topUpDate').value=''" align="middle" style="cursor:pointer" tabindex="37" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Top Up Amount:</td>
             <td align="left">
             	<html:input path="topUpAmount" cssClass="textBox" tabindex="3" maxlength="9" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
           </table>
           </div>

		   <div id="fieldsChargeBack" style="display:none;">
		   <table width="100%" border="0" cellpadding="0" cellspacing="1">
			<tr><td colspan="2" align="left" bgcolor="F3F3F3">
				<h3>Complaint Details:</h3>
			</td></tr>
		   <tr bgcolor="FBFBFB">
             <td width="50%" align="right" height="16" bgcolor="F3F3F3" class="formText">Transaction ID:</td>
             <td align="left">
             	<html:input path="cbTrxId" cssClass="textBox" tabindex="3" maxlength="12" onkeypress="return maskInteger(this,event)"/>
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Transaction Date:</td>
             <td align="left">
				<spring:bind path="complaintModelVO.cbTrxDate">
					<input type="text" name="${status.expression}" class="textBox" id="${status.expression}" maxlength="50" value="${status.value}" readonly="readonly" tabindex="36" />
				</spring:bind>
				<img id="cbDate" tabindex="37" name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" tabindex="3" />
				<img id="cbDate" tabindex="37" name="popcal" title="Clear Date" onclick="javascript:$('cbTrxDate').value=''" align="middle" style="cursor:pointer" tabindex="37" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Amount Transferred:</td>
             <td align="left">
             	<html:input path="cbAmount" cssClass="textBox" tabindex="3" maxlength="9" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Sender MSISDN:</td>
             <td align="left">
             	<html:input path="cbSenderMSISDN" cssClass="textBox" tabindex="3" maxlength="11" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Sender CNIC:</td>
             <td align="left">
             	<html:input path="cbSenderCNIC" cssClass="textBox" tabindex="3" maxlength="13" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Recipient MSISDN:</td>
             <td align="left">
             	<html:input path="cbRecipientMSISDN" cssClass="textBox" tabindex="3" maxlength="11" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Recipient CNIC:</td>
             <td align="left">
             	<html:input path="cbRecipientCNIC" cssClass="textBox" tabindex="3" maxlength="13" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
           </table>
           </div>

		   <div id="fieldsBBAccount" style="display:none;">
		   <table width="100%" border="0" cellpadding="0" cellspacing="1">
			<tr><td colspan="2" align="left" bgcolor="F3F3F3">
				<h3>Complaint Details:</h3>
			</td></tr>
			<tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Customer MSISDN:</td>
             <td align="left">
             	<html:input path="bbCustomerMSISDN" cssClass="textBox" tabindex="3" maxlength="11" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText">Transaction ID:</td>
             <td align="left">
             	<html:input path="bbTransactionId" cssClass="textBox" tabindex="3" maxlength="12" onkeypress="return maskInteger(this,event)" />
             </td>
           </tr>
		   <tr bgcolor="FBFBFB">
             <td width="50%" align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Transaction Date:</td>
             <td align="left">
				<spring:bind path="complaintModelVO.bbAccountDate">
					<input type="text" name="${status.expression}" class="textBox" id="${status.expression}" maxlength="50" value="${status.value}" readonly="readonly" tabindex="36" />
				</spring:bind>
				<img id="bbDate" tabindex="37" name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" tabindex="3" />
				<img id="bbDate" tabindex="37" name="popcal" title="Clear Date" onclick="javascript:$('bbAccountDate').value=''" align="middle" style="cursor:pointer" tabindex="37" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Amount Transferred:</td>
             <td align="left">
             	<html:input path="bbAmountTransferred" cssClass="textBox" tabindex="3" maxlength="9" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)" />
             </td>
           </tr>
           </table>
           </div>


		   </td></tr>

           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
             <input id="btnSubmit" type="submit" class="button" value=" Add Complaint " tabindex="6"/> 
             <input type="button" class="button" value="Cancel" tabindex="7" onclick="javascript: cancelAction();"/>
                  </td>
           </tr>
		</table>

		</html:form>

			<c:if test="${not empty complaintModelVO.oldComplaints}">
			<fieldset>
				<legend><font color="#3333FF">&nbsp; Existing Complaints &nbsp;</font></legend>
					<div class="eXtremeTable">
					<table class="tableRegion" width="100%" cellspacing="0">

						<tr>
						    <td class="tableHeader"><b>Initiated On</b></td>
						    <td class="tableHeader"><b>Complaint ID</b></td>
						    <td class="tableHeader"><b>Initiator</b></td>
						    <td class="tableHeader"><b>CNIC</b></td>
						    <td class="tableHeader"><b>Category</b></td>
							<td class="tableHeader"><b>Nature</b></td>
							<td class="tableHeader"><b>Transaction ID</b></td>
							<td class="tableHeader"><b>Status</b></td>
							<td class="tableHeader"><b>Escalation Level</b></td>
							<td class="tableHeader"><b>Current Assignee</b></td>
							<td class="tableHeader"><b>Expected TAT</b></td>
						</tr>					
					    <c:forEach items="${complaintModelVO.oldComplaints}" var="oldComplaint" varStatus="i">
							<c:set var="rowCssClass" value="even"/>
			   				<c:if test="${i.count%2!=0}">
		   						<c:set var="rowCssClass" value="odd" scope="page"/>
	   						</c:if>
							<tr class="${rowCssClass}">
								<td><fmt:formatDate pattern="dd/MM/yyyy hh:mm a" value="${oldComplaint.createdOn}"/></td>
								<td>
									<a href="${pageContext.request.contextPath}/p_complaintDetailForm.html?complaintId=${oldComplaint.complaintId}&actionId=2">
										<c:out value="${oldComplaint.complaintCode}"></c:out>
									</a>
								</td>
								<td>${oldComplaint.initiatorName}</td>
								<td>${oldComplaint.initiatorCNIC}</td>
								<td>${oldComplaint.complaintCategory}</td>
								<td>${oldComplaint.complaintSubcategory}</td>
								<td>${oldComplaint.transactionId}</td>
								<td>${oldComplaint.status}</td>
								<td>${oldComplaint.escalationStatus}</td>
								<td>${oldComplaint.currentAssigneeName}</td>
								<td><fmt:formatDate pattern="dd/MM/yyyy hh:mm a" value="${oldComplaint.expectedTat}"/></td>
							</tr>			
						</c:forEach>		
					</table>
					</div>
		  </fieldset>   
		  </c:if>


 
 		<ajax:select source="complaintCategoryId" target="complaintSubcategoryId"
			baseUrl="${contextPath}/p_complaintformajax.html"
			parameters="complaintCategoryId={complaintCategoryId},rType=1" 
			preFunction="showHideParams"
			postFunction="enableSubmit"
			errorFunction="error" />
 
 
      
      
      <script language="javascript" type="text/javascript">
	
	function isFutureDate(dateFieldId) {  	
		var isFuture = false;
  		var _fDate = document.getElementById(dateFieldId).value;
  		var fDate = getJsDate( _fDate );				
		
		if(_fDate != '' && fDate > currentDate ) {
			isFuture = true;
		}
		return isFuture;
	}    

	function getJsDate( date )
	{
		var jsDate = new Date();
		jsDate.setFullYear( date.split('/')[2] );
		jsDate.setMonth( (date.split('/')[1])-1 );
		jsDate.setDate( date.split('/')[0] );
		jsDate.setHours( 0 );
		jsDate.setMinutes( 0 );
		jsDate.setSeconds( 0 );
		jsDate.setMilliseconds( 0 );
		
		return jsDate;
	}

	
	function submitForm(theForm){

    	if(theForm.isWalkin.value != 'true' && theForm.appUserId.value == ''){
		    alert('Initiator Information is required.');
		    return false;
		}
		
      	if(theForm.complaintCategoryId.value == ''){
		    alert('Complaint Category is required.');
		    theForm.complaintCategoryId.focus();
		    return false;
		}
		if(theForm.complaintSubcategoryId.value == ''){
		    alert('Complaint Nature is required.');
		    theForm.complaintSubcategoryId.focus();
		    return false;
		}

    	if(theForm.isWalkin.value == 'true'){
    		if(theForm.initiatorFirstName.value == ''){
    		    alert('Name is required.');
    		    theForm.initiatorFirstName.focus();
    		    return false;
    		}
    		if(theForm.initiatorMobileNo.value == ''){
    		    alert('MSISDN No is required.');
    		    theForm.initiatorMobileNo.focus();
    		    return false;
    		}else{
				if(theForm.initiatorMobileNo.value.length > 0 && theForm.initiatorMobileNo.value.length != 11){
					alert ("MSISDN No length must be 11 characters.");
					return false;
				}
        		
    	      	var zongNoVal = theForm.initiatorMobileNo.value;
    			if(zongNoVal != null ){
    				if( trim(zongNoVal) != ''){
    					var noStartsWith = zongNoVal.substring(0,2);
    					if(noStartsWith != '03'){
    						alert('MSISDN should always starts with 03');
    						theForm.initiatorMobileNo.focus();
    						return false;
    					}
    				}
    			}
            }
    		
    		if(theForm.initiatorNIC.value == ''){
    		    alert('CNIC is required.');
    		    theForm.initiatorNIC.focus();
    		    return false;
    		}else{
				if(theForm.initiatorNIC.value.length > 0 && theForm.initiatorNIC.value.length != 13){
					alert ("CNIC length must be 13 characters.");
					return false;
				}
			}

    		if(theForm.initiatorCity.value == ''){
    		    alert('City/Location is required.');
    		    theForm.initiatorCity.focus();
    		    return false;
    		}    
    		
    	}

		if(theForm.complaintCategoryId.value == <%=ComplaintsModuleConstants.CATEGORY_BB_ACCOUNT%>){
			if(!( doRequired(theForm.otherContactNo, 'Other Contact No' ) )){
				return false;
			}
		}


		if(theForm.complaintDescription.value == ''){
		    alert('Description is required.');
		    theForm.complaintDescription.focus();
		    return false;
		}

		if(theForm.complaintCategoryId.value == <%=ComplaintsModuleConstants.CATEGORY_UTILITY%>){
			if(!( doRequired(theForm.serviceProviderType, 'Type of Utility Service Provider' )
				&& doRequired(theForm.serviceProviderName, 'Service Provider Company Name' )
				&& doRequired(theForm.consumerNumber, 'Consumer No' )
				&& doRequired(theForm.paymentDate, 'Payment Date' )
				&& doRequired(theForm.paymentAmount, 'Payment Amount' ))){

				return false;
			}
					
			if(isFutureDate('paymentDate')){
				alert("Payment Date should be less than or equal to current date.");
			    return false;
		    }
		}else if(theForm.complaintCategoryId.value == <%=ComplaintsModuleConstants.CATEGORY_FUND_TRANSFER%>){
			if(!( doRequired(theForm.transactionDate, 'Transaction Date' )
					&& doRequired(theForm.amountTransferred, 'Amount Transferred')
					&& doRequired(theForm.senderAccountNo, 'Sender Account No')
					&& doRequired(theForm.recipientAccountNo, 'Recipient Account No')
					)){

				return false;
			}
						
			if(isFutureDate('transactionDate')){
				alert("Transaction Date should be less than or equal to current date.");
			    return false;
		    }
		}else if(theForm.complaintCategoryId.value == <%=ComplaintsModuleConstants.CATEGORY_AGENT_COMPLAINT%>){
			if(!( doRequired(theForm.agentId, 'Agent ID' )
					&& doRequired(theForm.shopName, 'Shop Name')
					&& doRequired(theForm.agentPaymentAmount, 'Transaction Amount')
					&& doRequired(theForm.senderAgentMSISDN, 'Sender Agent MSISDN')
					&& doRequired(theForm.receiverAgentMSISDN, 'Receiver Agent MSISDN')
					&& doRequired(theForm.agentTransactionDate, 'Transaction Date'))){

				return false;
			}
		}else if(theForm.complaintCategoryId.value == <%=ComplaintsModuleConstants.CATEGORY_TOP_UP%>){
			if(!( doRequired(theForm.topUpMobileNo, 'Mobile No' )
					&& doRequired(theForm.topUpDate, 'Top Up Date')
					&& doRequired(theForm.topUpAmount, 'Top Up Amount'))){

				return false;
			}
			if(isFutureDate('topUpDate')){
				alert("Top Up Date should be less than or equal to current date.");
			    return false;
		    }
		}else if(theForm.complaintCategoryId.value == <%=ComplaintsModuleConstants.CATEGORY_CHARGEBACK%>){
			if(!( doRequired(theForm.cbTrxDate, 'Transaction Date' )
					&& doRequired(theForm.cbAmount, 'Amount Transferred')
					&& doRequired(theForm.cbSenderMSISDN, 'Sender MSISDN')
					&& doRequired(theForm.cbSenderCNIC, 'Sender CNIC')
					&& doRequired(theForm.cbRecipientMSISDN, 'Recipient MSISDN')
					&& doRequired(theForm.cbRecipientCNIC, 'Recipient CNIC') )){

				return false;
			}
			if(isFutureDate('cbTrxDate')){
				alert("Transaction Date should be less than or equal to current date.");
			    return false;
		    }
		}else if(theForm.complaintCategoryId.value == <%=ComplaintsModuleConstants.CATEGORY_BB_ACCOUNT%>){
			if(!( doRequired(theForm.otherContactNo, 'Other Contact No' )
					&& doRequired(theForm.bbAccountDate, 'Transaction Date')
					&& doRequired(theForm.bbCustomerMSISDN, 'Customer MSISDN')
					&& doRequired(theForm.bbAmountTransferred, 'Amount Transferred') )){

				return false;
			}
			if(isFutureDate('bbAccountDate')){
				alert("Date should be less than or equal to current date.");
			    return false;
		    }
		}
		
		
      	if(!validateFormChar(theForm)){
      		return false;
      	}		      
		      	
      	//submitting form
        if (confirm('Are you sure you want to proceed?')==true)
        {
          return true;
        }
        else
        {
          return false;
        }
  }

    function doRequired( field, label )
    {
    	if( field.value == '' || field.value.length == 0 )
    	{
    		alert( label + ' is required.' );
    		return false;
    	}
    	return true;
    }
      
      function onFormSubmit(theForm)
      {

      	if(submitForm(theForm)){
	    	document.getElementById('btnSubmit').disabled = true;
      		return true;
	    }else{
		    return false;
	    }
	    
        // return validatemfsAccountModel(theForm); 
      }

		function populateBillCompanies( billType )
		{
			var select = document.getElementById( 'serviceProviderName' );
			select.options.length = 1;

			if( billType != '' )
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
      
      function cancelAction(){
         window.location = "${pageContext.request.contextPath}/home.html";    
      }

	  Calendar.setup( {inputField  : "paymentDate", ifFormat : "%d/%m/%Y", button : "sDate"} );
	  Calendar.setup( {inputField  : "transactionDate", ifFormat : "%d/%m/%Y", button : "trxDate"} );
	  Calendar.setup( {inputField  : "topUpDate", ifFormat : "%d/%m/%Y", button : "topDate"} );
	  Calendar.setup( {inputField  : "cbTrxDate", ifFormat : "%d/%m/%Y", button : "cbDate"} );
	  Calendar.setup( {inputField  : "bbAccountDate", ifFormat : "%d/%m/%Y", button : "bbDate"} );
	  Calendar.setup( {inputField  : "agentTransactionDate", ifFormat : "%d/%m/%Y", button : "agTransDate"} );
	  showHideParams();
      </script>
    
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"> </script>
   </body>
</html>
