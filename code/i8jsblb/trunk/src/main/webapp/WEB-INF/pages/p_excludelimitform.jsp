<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.inov8.microbank.common.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
<meta name="decorator" content="decorator2">
		<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Exclude Limits"/>
	</head>
	<body>
	
	<spring:bind path="limitRuleModel.*">
	  <c:if test="${not empty status.errorMessages}">
	    <div class="errorMsg">
	      <c:forEach var="error" items="${status.errorMessages}">
	        <c:out value="${error}" escapeXml="false" />
	        <br/>
	      </c:forEach>
	    </div>
	  </c:if>
	</spring:bind>
	<c:if test="${not empty messages}">
	    <div class="infoMsg" id="successMessages">
	        <c:forEach var="msg" items="${messages}">
	            <c:out value="${msg}" escapeXml="false"/><br />
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
	
	<html:form  name = "limitRuleModelForm" id = "limitRuleModelForm" commandName="limitRuleModel" action="p_excludelimitform.html" method="post" onsubmit="return validateForm(this)">
       <table width="100%" border="0" cellpadding="0" cellspacing="1">
       		
	        <html:hidden path="limitRuleId"/>
	        <html:hidden path="createdOn"/>
	        <html:hidden path="updatedOn"/>
	        <html:hidden path="updatedBy"/>
	        <html:hidden path="createdBy"/>
	        <html:hidden path="versionNo"/>
	        
	        <tr bgcolor="FBFBFB">
	             <td colspan="2" align="center">&nbsp;</td>
            </tr>            
           <tr>
             <td  bgcolor="F3F3F3" class="formText" width="50%" align="right">Product:&nbsp;&nbsp; </td>
             <td align="left" bgcolor="FBFBFB" width="50%"> 	
				<html:select path="productId" id="productId" cssClass="textBox">
					<html:option value="">---All---</html:option>
					<html:options items="${productList}" itemLabel="name" itemValue="productId"/>									
				</html:select>										
             </td>
           </tr>
            <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Segment:&nbsp;&nbsp; </td>
             <td align="left" bgcolor="FBFBFB">	
				<html:select path="segmentId" id="segmentId" cssClass="textBox">
					<html:option value="">---All---</html:option>
					<html:options items="${segmentList}" itemLabel="name" itemValue="segmentId" />									
				</html:select>										
             </td>
           </tr>
            <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Account Type:&nbsp;&nbsp; </td>
             <td align="left" bgcolor="FBFBFB">	
				<html:select path="accountTypeId" id="accountTypeId" cssClass="textBox">
					<html:option value="">---All---</html:option>
					<html:options items="${accountTypeList}" itemLabel="name" itemValue="customerAccountTypeId" />									
				</html:select>										
             </td>
           </tr>       
           <tr>
           	<td align="right" bgcolor="F3F3F3" class="formText">Active :</td>
            <td align="left"  bgcolor="FBFBFB">                
	             <html:checkbox  path="isActive" tabindex="5"/>	         
            </td>
           </tr>
           
           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
           <tr>    
	           <td align="center" colspan="2">	
	           		<input name="_save" id="_save" type="submit" class="button"  value=" Save " /> &nbsp;
	           		<input name="reset" type="reset" onclick="javascript: window.location='p_limitrulemanagement.html?actionId=2'"
							class="button" value="Cancel" tabindex="14" />
	           </td>
           </tr>
            <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
    </table>
</html:form>
		
	<script language="javascript" type="text/javascript">
		function validateForm(form)
		{	        	
	     	var _productId = form.productId.value;
	     	var _segmentId = form.segmentId.value;
	     	var _accountTypeId = form.accountTypeId.value;
	     	
	
	     	if(_productId=="" && _segmentId=="" && _accountTypeId=="")
	     	{
	     		alert("Atleast one field should be selected form Product , Segment or Account type")
	     		return false;
	     	}
	
	     	
	     	return true;
		}
	</script>
	
	</body>
</html>
