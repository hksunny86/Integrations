
<jsp:directive.page import="com.inov8.microbank.common.util.PortalDateUtils"/><!--Author: Rizwan ur Rehman -->

<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta name="decorator" content="decorator2">

<v:javascript formName="commissionShSharesModel" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>

<script type="text/javascript">
		
	function writeError(message) {
		var msg = document.getElementById('msg');
		msg.innerHTML = message;
		msg.style.color = 'red';
		return false;
	}
	
	function validateNumber(fieldVal){
	
		if(fieldVal==""){
			return false; 
		}
		
		if(isNaN(fieldVal) ) {
			return false; 
		}
		
		return true;
	}
	
		var jq = $.noConflict();
	    jq(document).ready(
	    	function($)
	      	{
	      		$( "#bank" ).focus();
				
				$( "#_save" ).click(
					function()
					{
						
						var isValidForm = true;
						var bankShare = $( "#bank" ).val();
						var agent1Share = $( "#agent1" ).val();
						var agent2Share = $( "#agent2" ).val();
						var franchise1Share = $( "#franchise1" ).val();
						var franchise2Share = $( "#franchise2" ).val();
						
						if(!validateNumber(bankShare)){
							return writeError("Bank Share is required !");
						}
						
						var sumTotal = 0.0;
						
						if( bankShare != '' )
						{
							var share = parseFloat(bankShare);
							sumTotal = sumTotal + share;
							sumTotal = Math.round(sumTotal * 100) / 100;
						}
						
						if( agent1Share != '' )
						{
							var share = parseFloat(agent1Share);
							sumTotal = sumTotal + share;
							sumTotal = Math.round(sumTotal * 100) / 100;
						}
						
						if( agent2Share != '' )
						{
							var share = parseFloat(agent2Share);
							sumTotal = sumTotal + share;
							sumTotal = Math.round(sumTotal * 100) / 100;
						}
						
						if( franchise1Share != '' )
						{
							var share = parseFloat(franchise1Share);
							if(share > 100){
								alert( " Franchise 1 share must not exceed 100.");
								isValidForm = false;
							}
						}
						
						if( franchise2Share != '' )
						{
							var share = parseFloat(franchise2Share);
							if(share > 100){
								alert( " Franchise 2 share must not exceed 100.");
								isValidForm = false;
							}
						}
						
						alert(sumTotal);
						if( sumTotal != 100 )
						{
							alert( " The sum of all stakeholder shares must be 100.");
							isValidForm = false;
						}

						if( isValidForm )
						{
							$( "#commissionShSharesForm" ).submit();
						}
					}
				);		      		
	      	}
	    );


		function error(request)
		{
		     	alert("An unknown error has occured. Please contact with the administrator for more details");
		}

</Script>

<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="title" content="Commission Stakeholder Shares"/>

<input type="hidden" name="insertPageFlag" value="${insertFlag}"/>

</head>
<body>
<%@include file="/common/ajax.jsp"%>
<spring:bind path="commissionShSharesModel.*">
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


<table width="100%"  border="0" cellpadding="0" cellspacing="1">

  <form method="post" action="<c:url value="/commissionshsharesform.html"/>" id="commissionShSharesForm"
  onsubmit="return true;">

    <c:if test="${not empty param.productId}">
      <input type="hidden"  name="isUpdate" id="isUpdate" value="true"/>
      <spring:bind path="commissionShSharesModel.productId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
      </spring:bind>
    </c:if>
    <tr>
	<td align="center" colspan="2" id="msg">&nbsp;

		<c:if test="${not empty errors}">
			<font color="#FF0000"> <c:out value="${errors}" /></font>
		</c:if>				

	</td>
</tr>
    <tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Product:&nbsp;</td>
      <td align="left">
        <spring:bind path="commissionShSharesModel.productId">
            <select name="${status.expression}" class="textBox" <c:if test="${not empty param.productId}">disabled='disabled'</c:if>>              
              <c:forEach items="${productModelList}" var="productModelList">
                <option value="${productModelList.productId}"
                  <c:if test="${param.productId == productModelList.productId}">selected="selected"</c:if>/>
                  ${productModelList.name}
                </option>
              </c:forEach>
            </select>
        </spring:bind>
      </td>
    </tr>


    <tr bgcolor="FBFBFB">
      <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Bank:&nbsp;</td>
      <td width="50%" align="left">
      	<spring:bind path="commissionShSharesModel.bank">

		  <c:if test="${not empty param.productId}">
			<input type="text" id="${status.expression}" name="${status.expression}" class="textBox" value="${status.value}"  onkeypress="return maskNumber(this,event)"  maxlength="6"/>  
		  </c:if>
		  
		  <c:if test="${empty param.productId}">
			<input type="text" id="${status.expression}" name="${status.expression}" class="textBox" value="${status.value}"  onkeypress="return maskNumber(this,event)"  maxlength="6"/>	  
		  </c:if>
        

        </spring:bind>
      </td>
    </tr>
    <tr>
      <td align="right" bgcolor="#F3F3F3" class="formText">Agent 1:&nbsp;</td>
      <td align="left" bgcolor="#FBFBFB">
        <spring:bind path="commissionShSharesModel.agent1">
          <input type="text" id="${status.expression}" name="${status.expression}" class="textBox" value="${status.value}"  onkeypress="return maskNumber(this,event)"  maxlength="6"/>  
	  	</spring:bind>
      </td>
    </tr>

    <tr>
      <td align="right" bgcolor="#F3F3F3" class="formText">Agent 2:&nbsp;</td>
      <td align="left" bgcolor="#FBFBFB">
        <spring:bind path="commissionShSharesModel.agent2">
          <input type="text" id="${status.expression}" name="${status.expression}" class="textBox" value="${status.value}"  onkeypress="return maskNumber(this,event)"  maxlength="6"/>  
	  	</spring:bind>
      </td>
    </tr>
    
     <tr>
      <td align="right" bgcolor="#F3F3F3" class="formText">Franchise 1:&nbsp;</td>
      <td align="left" bgcolor="#FBFBFB">
        <spring:bind path="commissionShSharesModel.franchise1">
          <input type="text" id="${status.expression}" name="${status.expression}" class="textBox" value="${status.value}"  onkeypress="return maskNumber(this,event)"  maxlength="6"/>  
	  	</spring:bind>
      </td>
    </tr>
    
     <tr>
      <td align="right" bgcolor="#F3F3F3" class="formText">Franchise 2:&nbsp;</td>
      <td align="left" bgcolor="#FBFBFB">
        <spring:bind path="commissionShSharesModel.franchise2">
          <input type="text" id="${status.expression}" name="${status.expression}" class="textBox" value="${status.value}"  onkeypress="return maskNumber(this,event)"  maxlength="6"/>  
	  	</spring:bind>
      </td>
    </tr>


	<tr>
		<td width="49%" height="16" align="right" bgcolor="F3F3F3"
			class="formText">Apply FED:&nbsp;</td>
		<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
			<spring:bind path="commissionShSharesModel.isFedApplicable">
				<input name="${status.expression}" type="checkbox" value="true"
					<c:if test="${status.value==true}">checked="checked"</c:if>
					<c:if test="${empty param.productId}">checked="checked"</c:if> />
			</spring:bind></td>
	</tr>
	<tr>
		<td width="49%" height="16" align="right" bgcolor="F3F3F3"
			class="formText">Apply WHT:&nbsp;</td>
		<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
			<spring:bind path="commissionShSharesModel.isWhtApplicable">
				<input name="${status.expression}" type="checkbox" value="true"
					<c:if test="${status.value==true}">checked="checked"</c:if>
					<c:if test="${empty param.productId}">checked="checked"</c:if> />
			</spring:bind></td>
	</tr>
	<tr>
      <td colspan="2" align="middle">
      
      <tr>
    <td colspan="2" align="center" bgcolor="FBFBFB">
    	<input type= "button" name = "_save" id="_save" value="  Save  "  class="button"/>		  
 <c:if test="${empty param.productId}">
 	<input type= "button" name = "_cancel" value=" Cancel " onclick="javascript:window.location='p_commissionshsharesmanagement.html';" class="button"/>
</c:if>
 <c:if test="${not empty param.productId}">
	<input type= "button" name = "_cancel" value=" Cancel " onclick="javascript:window.location='p_commissionshsharesmanagement.html';" class="button"/>
</c:if>
    
  </tr>
  
  </form>
</table>


<v:javascript formName="commissionShSharesModel" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

</body>
</html>

