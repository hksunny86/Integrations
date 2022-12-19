<!--Author: Mohammad Shehzad Ashraf -->
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<%@include file="/common/taglibs.jsp"%>

<html>
  <head>
<meta name="decorator" content="decorator">
    
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Change Account Nick" />

	
  </head>
  
  <body bgcolor="#ffffff">
   <c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
   	<ec:table 
		items="accountsNicksList" filterable="false"
                var="accountNickModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/p_pgaccountnickmanagement.html?appUserId=${param.appUserId}"
				title="">
		<ec:row>
		<c:set var="accountNickModelSmartMoneyAccountId">
			<security:encrypt strToEncrypt="${accountNickModel.smartMoneyAccountId}"/>
		</c:set>
		<c:set var="accountNickModelappUserId">
			<security:encrypt strToEncrypt="${accountNickModel.appUserId}"/>
		</c:set>		
		
			<ec:column property="accountNick" title="Account Nick" filterable="false">
				
				<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_ACC_NICK_UPDATE%>">				
					<c:choose>
				       <c:when test="${veriflyRequired}">
					           <a href="p_pgupdateaccountnick.html?smartMoneyAccountId=${accountNickModelSmartMoneyAccountId}&appUserId=${accountNickModelappUserId}">
				                  ${accountNickModel.accountNick}
				               </a>
				       </c:when>
				       <c:otherwise>
				           ${accountNickModel.accountNick}
				       </c:otherwise>
				    </c:choose>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=PortalConstants.MNG_ACC_NICK_UPDATE%>">
					${accountNickModel.accountNick}				
				</authz:authorize>
				
		    </ec:column>
			<ec:column property="paymentModeName" title="Payment Mode" filterable="false"/>
        </ec:row>
	</ec:table>
	<c:if test="${not empty param.appUserId }">
	   <div align="center">
          <input type="button" value=" Back " id="backButton" name="backButton" class="button" align="middle" onClick="javascript:backFunction();">
	   </div>
	</c:if>
    
    <script language="javascript" type="text/javascript">
        function openAccountsWindow(smartMoneyAccountId){
			newWindow = window.open('p_pgupdateaccountnick.html?smartMoneyAccountId='+smartMoneyAccountId+')','ChangeAccountNick','width=400,height=200,menubar=no,toolbar=no,left=150,top=150,directories=no,scrollbars=no,resizable=no,status=no');
			if(window.focus) newWindow.focus();
				return false;
        }
        function backFunction(){
            window.location = '${pageContext.request.contextPath}/p_mnonewmfsaccountform.html?appUserId=${param.appUserId}';
        }
	</script>
  </body>
</html>
