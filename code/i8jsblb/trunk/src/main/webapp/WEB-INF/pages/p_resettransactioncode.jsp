<%@ include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
	<meta name="decorator" content="decorator">
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
		<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Manual Reset Transaction Code"/>
	</head>
	<body>
	
	<c:if test="${not empty messages}">
	    <div class="infoMsg" id="successMessages">
	        <c:forEach var="msg" items="${messages}">
	            <c:out value="${msg}" escapeXml="false"/><br />
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
	
	<form id="resetTransactionCodeForm" method="post" action="p_resettransactioncode.html" onsubmit="return validateForm(this)"  >
		<table width="100%"  border="0" cellpadding="0" cellspacing="1">
			
			
			<input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
			<input type="hidden" name="usecaseId" id="usecaseId" value="<%=PortalConstants.ONE_TIME_PIN_RESET_USECASE_ID%>"/>
			
			
			
	          	
			<tr bgcolor="FBFBFB">
	            <td colspan="2" align="center">&nbsp;</td>
	        </tr>
	        
	        <tr>
	            <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Transaction ID:&nbsp;</td>
	            <td align="left" bgcolor="FBFBFB">
	          
	                <input name="transactionId" type="text" size="40" readonly="readonly" class="textBox" tabindex="1" maxlength="50"  value="${param.transactionId }"  />       
	          </tr>
	        <tr>
	            <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Enter New Transaction Code:&nbsp;</td>
	            <td align="left" bgcolor="FBFBFB">
	           
	                <input name="transactionCode" type="text" size="40" class="textBox" tabindex="1" maxlength="4" onkeypress="return maskNumber(this,event)" />
	          
	          </tr>
	          <tr>
	            <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Re-Enter New Transaction Code:&nbsp;</td>
	            <td align="left" bgcolor="FBFBFB">
	           
	                <input name="transactionCode2" type="text" size="40" class="textBox" tabindex="1" maxlength="4" onkeypress="return maskNumber(this,event)" />
	           </td>
	          </tr>
	        <tr>  
	            <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
	            <td align="left" bgcolor="FBFBFB">
	            
	            <textarea name="comments" cols="50" rows="5" tabindex="2"  style="width:163px; height: 102px" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox" >${status.value}</textarea>
	           </td>
	         </tr>
	         
	
	        <tr bgcolor="FBFBFB">
	    	<td height="19" colspan="2" align="center">
	    	<input name="_save" type="submit" class="button" value="Save" tabindex="4"/>
			<input name="reset" type="reset" onclick="javascript: window.location.href='p_cashwithdrawaltransdetails.html?customerCnic=${param.customerCnic}&appUserId=${param.appUserId}&registered=${param.registered}&referrer=${param.referrer}'" class="button" value="Cancel" tabindex="5" />
	    	</td>
	        
	        <input type="hidden" name="customerCnic" value="${param.customerCnic}">
			<input type="hidden" name="appUserId" value="${param.appUserId}">
			<input type="hidden" name="registered" value="${param.registered}">
			<input type="hidden" name="home" value="${param.home}">
			<input type="hidden" name="referrer" value="${param.referrer}"/> 
	    
	 	</table>
	</form>
	
	<script language="javascript" type="text/javascript">
		function validateForm(form)
		{	        	
	     	var transactioncode1 = form.transactionCode.value;
	     	var transactioncode2 = form.transactionCode2.value;
	     	var isValid = true;
	
	     	if(transactioncode1=="")
	     	{
	     		alert("Please Provide Transaction Code")
	     		isValid = false;
	     	}else if(transactioncode2==""){
	     	
	     		alert("Please Re-Enter Transaction Code")
	     		isValid = false;
	     	}else if(transactioncode1!=transactioncode2){
	     		alert("Transaction Code Do Not Match")
	     		isValid = false;
	     	}
	
	     	return isValid;
		}
	</script>
	
	</body>
</html>
