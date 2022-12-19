<!--Author: Basit Mehr -->
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>

<html>
  <head>
<meta name="decorator" content="decorator">
    
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Concern Rules" />

	<%
		String updateConcernRulesPermission = PortalConstants.CONCERN_RULES_UPDATE;
		updateConcernRulesPermission += "," + PortalConstants.PG_GP_UPDATE;
	%>
  </head>
 
	  <c:if test="${not empty status.errorMessages}">
	    <div class="errorMsg">
	      <c:forEach var="error" items="${status.errorMessages}">
	        <c:out value="${error}" escapeXml="false"/>
	        <br/>
	      </c:forEach>
	    </div>
	  </c:if>
	
	<c:if test="${not empty messages}">
	    <div class="infoMsg" id="successMessages">
	        <c:forEach var="msg" items="${messages}">
	            <c:out value="${msg}" escapeXml="false"/><br/>
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
  
  <form name="rulesform"   action="">
  
    <table cellpadding="0" cellspacing="1" border="0" width="750px">

          <tr align="left">
             <td height="16" bgcolor="F3F3F3" align="right" class="formText">Choose Partner:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
				<select  name="partnerId"  Class="textBox" tabindex="1">
					<c:forEach items="${concernPartnerList}" var="partnerModel">
						<option value="${partnerModel.concernPartnerId}"><c:out value="${partnerModel.name}"/></option>
					</c:forEach>
	            </select>
              </td>
           </tr>

          <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText">
			</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
				<input type="hidden" name="actionId" value="<%=PortalConstants.ACTION_UPDATE%>">
					<authz:authorize ifAnyGranted="<%=updateConcernRulesPermission%>">
						<input type="button" name="submitButton" class="button" value="Define Rules" onclick="submitForm()" />		
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=updateConcernRulesPermission%>">
						<input type="button" name="submitButton" class="button" value="View Rules" onclick="submitForm()" />		
					</authz:authorize>
              </td>
           </tr>
			
		<input type="hidden" name="actionId" value="2">	
			
    </table>
    </form>
   
   
   
   <script>
   		
   		function submitForm(){
   			_form = document.rulesform;
   			_form.action = "p_opconcernrulesform.html?actionId=2&partnerId="+_form.partnerId.value;
   			_form.submitButton.disabled = true;
   			_form.submit();
   		}
   		
   </script>
   
   
  </body>
</html>
	
