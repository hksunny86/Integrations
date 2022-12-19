<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.inov8.microbank.common.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
<meta name="decorator" content="decorator2">
		<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Customer Segments"/>
	</head>
	<body>
	
	<spring:bind path="segmentModel.*">
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
	
	<form id="segmentForm" method="post" action="p_customersegmentform.html" onsubmit="return validateForm(this)"  >
		<table width="100%"  border="0" cellpadding="0" cellspacing="1">
			
			 <c:if test="${not empty param.segmentId}">
			<input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
			</c:if>
			
			<spring:bind path="segmentModel.segmentId">
	          <input type="hidden" name="${status.expression}" value="${status.value}"/>
	        </spring:bind>
	
	         <spring:bind path="segmentModel.versionNo">
	          <input type="hidden" name="${status.expression}" value="${status.value}"/>
	        </spring:bind>
	
	       
	
	           <spring:bind path="segmentModel.createdOn">
	          <input type="hidden" name="${status.expression}" value="${status.value}"/>
	          </spring:bind>
	
	           <spring:bind path="segmentModel.updatedOn">
	          <input type="hidden" name="${status.expression}" value="${status.value}"/>
	          </spring:bind>
	          	
			<tr bgcolor="FBFBFB">
	            <td colspan="2" align="center">&nbsp;</td>
	        </tr>
	        
	        <tr>
	            <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Customer Segment:&nbsp;</td>
	            <td align="left" bgcolor="FBFBFB">
	            <spring:bind path="segmentModel.name">
	                <input name="${status.expression}" type="text" size="40" class="textBox" tabindex="1" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" value="${status.value}"  />
	            </spring:bind></td>
	          </tr>
	        
	        <tr>  
	            <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
	            <td align="left" bgcolor="FBFBFB">
	            <spring:bind path="segmentModel.description">
	            <textarea name="${status.expression}" cols="50" rows="5" tabindex="2"  style="width:163px; height: 102px" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox" >${status.value}</textarea>
	            </spring:bind></td>
	         </tr>
	         <tr>
	    	<td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
	    	<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
	    	<spring:bind path="segmentModel.isActive">
			    <input name="${status.expression}" type="checkbox" value="true" 	tabindex="3"
			    <c:if test="${status.value==true}">checked="checked"</c:if>
			    <c:if test="${empty param.segmentId && empty param._save }">checked="checked"</c:if>
			    <c:if test="${status.value==false && not empty param._save }">unchecked="checked"</c:if>
		    	/>		    
	    	</spring:bind>
	    	</td>
	  		</tr> 
	
	        <tr bgcolor="FBFBFB">
	    	<td height="19" colspan="2" align="center">
	    	<input name="_save" type="submit" class="button" value="Save" tabindex="4"/>
			<input name="reset" type="reset" onclick="javascript: window.location='p_customersegmentmanagement.html?actionId=2'" class="button" value="Cancel" tabindex="5" />
	    	</td>
	          
	          
	 	</table>
	</form>
	
	<script language="javascript" type="text/javascript">
		function validateForm(form)
		{	        	
	     	var name = form.name.value;
	     	var isValid = true;
	
	     	if(name=="" )
	     	{
	     		alert("Provide Customer Segment Name")
	     		isValid = false;
	     	}
	
	     	return isValid;
		}
	</script>
	
	</body>
</html>
