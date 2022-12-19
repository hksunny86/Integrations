<!--Author: Mohammad Shehzad Ashraf-->

<%@page import="com.inov8.microbank.common.model.portal.concernmodule.ConcernPartnersRuleViewModel"%>
<%@page import="java.util.List"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>  
<meta name="decorator" content="decorator2">
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
       
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <meta name="title" content="Concern Rules for ${partnerName}"/>
      <link rel="stylesheet" href="/i8Microbank/styles/extremecomponents.css" type="text/css">
      <link rel="stylesheet" href="/i8Microbank/styles/style.css" type="text/css">
		<%
			String updatePermission = PortalConstants.PG_GP_UPDATE;
			updatePermission += "," + PortalConstants.CONCERN_RULES_UPDATE;
		%>
</head>

   <body bgcolor="#ffffff"  >
   
      <spring:bind path="concernPartnersRuleViewModel.*">
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
     
	  
       <html:form name="concernPartnersRuleViewForm" commandName="concernPartnersRuleViewModel"  onsubmit="return onFormSubmit(this)"
         action="p_opconcernrulesform.html?actionId=2&ruleForm=true">

             <c:set var="createAction"><%=PortalConstants.ACTION_CREATE%></c:set>	
             <c:set var="updateAction"><%=PortalConstants.ACTION_UPDATE%></c:set>	
      		
      		 <input type="hidden" name="actionId" value="${updateAction}" />
      		 <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
      		 <input type="hidden" name="apartnerId"  value="${partnerList1[0].partnerId}"/>

      <table width="100%" border="0" cellpadding="0" cellspacing="1">
           <tr>
             <td height="5%" align="right" >&nbsp;</td>
             <td width="58%" align="left" >
             
   <div class="eXtremeTable" >             
<table id="ec_table"  border="0"  cellspacing="0"  cellpadding="0"  class="tableRegion"  width="80%" >
	<thead>
	<tr>
		<td class="tableHeader" >Partner</td>
		<td class="tableHeader" >Forward Concern</td>
	</tr>
	</thead>
	<tbody class="tableBody" >


			
			<%int i=0;%>
	        <c:forEach var="partnerModel" items="${partnerList1}">
	        	<%i++;%>    
				<tr class= <%=(i%2)==0?"even":"odd" %> onmouseover="this.className='highlight'"  onmouseout="this.className='odd'" >
					<td width="70%" > <c:out value="${partnerModel.associatedPartnerName}" /> </td>
					<td width="30%" align="center" >
					<input type="checkbox" name="isPartnerAssociated" value="${partnerModel.concernPartnerAsociationId}" <c:if test="${partnerModel.associated}">checked</c:if>
						<c:if test="${partnerModel.associatedPartnerId eq currentPartnerId }">disabled="disabled"</c:if> 
					/>

					 </td>
					
				</tr>
	            
	            
	            
	        </c:forEach>



	</tbody>
</table>
</div>                
            </td>
            
            <td height="37%" align="right" >&nbsp;</td>
                
           </tr>
           <tr >
             <td colspan="2" align="right">
             <table width="100%" border="0">
             	<tr>
             		<td width="10%"	>&nbsp;
             		</td>
             		<td width="76%" align="right">
				<authz:authorize ifAnyGranted="<%=updatePermission%>">
          			<input type="button" name="submitButton" value="  Save  " class="button" onclick="submitForm()">
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=updatePermission%>">
           			<input type="button" name="submitButton" value="  Save  " disabled="disabled"	class="button" >
				</authz:authorize> 
				<input name="reset" type="reset" class="button" tabindex="-1" value="Cancel" onclick="javascript: window.location='p_opconcernrules.html?actionId=2'" /> 
             		</td>
             		<td width="14%"	>&nbsp;
             		</td>
             		
             	</tr>
             </table>	
             
             
				
			
				</td>
           </tr>			
 
      	</table>
		</html:form>      
 
  	<script>
	
		function submitForm(){ 
		       _form = document.concernPartnersRuleViewForm;
			   _form.submitButton.disabled = true;
			   _form.submit();
		}
	</script>
      <v:javascript formName="concernPartnersRuleViewModel" staticJavascript="false"/>
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"> </script>
      
   </body>
</html>





