<%@page import="com.inov8.microbank.webapp.action.allpayweb.LoginController"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils" %>
<%@page import="com.inov8.microbank.common.util.CommandFieldConstants" %>
<%@ page import="com.inov8.microbank.common.util.UserUtils" %>
<%! 
	LoginController loginController = new LoginController(); 
%>

<%
	Integer INVALID_REQUEST_ERROR = loginController.isValidRequest(request);

	if(INVALID_REQUEST_ERROR == 3) {
%>
	<jsp:forward page="/agentweblogin.aw"></jsp:forward>
	<%-- <jsp:forward page="/awlogin.aw"></jsp:forward> --%>
<%		
	}
%>

<%--
Turab:Security:Pragma No-cache
Pragma and Cache-Control Headers are set in responce in order to prevent Browser Clients from caching the response that is send back to them from server.
Following scriptlet should be added on every page of the application, so in Microbank we need to add this scriptlet to header.jsp and login.jsp pages.
--%>
<%
    HttpServletResponse httpServletResponse = (HttpServletResponse) pageContext.getResponse();
    httpServletResponse.setHeader("Cache-Control", "no-cache");
    httpServletResponse.setHeader("Pragma", "No-cache");
    long currentTime = System.currentTimeMillis();
    httpServletResponse.setDateHeader("Expires", currentTime + 3000L);

%>

<%--
Turab:Security:paramter tampring handled
--%>
<%
    if (request.getAttribute("parameterCompromized")!=null && request.getAttribute("parameterCompromized").equals("true")) {
%>
<script> window.location = "403_.html"; </script>
<%
    }
%>

<div id="header_portal">
    <%
        Long appUserTypeId = UserUtils.getCurrentUser().getAppUserTypeId();
        Long mnoId = UserUtils.getCurrentUser().getMnoId();
    %>
    <% if(appUserTypeId.equals(3L) && mnoId != null && mnoId.equals(50028L)){ %>
    <div class="sco_logo_portal"></div>
    <% }        %>
    <% if(appUserTypeId.equals(3L) && mnoId == null || (mnoId != null && mnoId.equals(50027L))){ %>
    <div class="logo_portal"></div>
    <% }        %>
    <%--<div class="logo_portal"></div>--%>
    <div class="banner_right_top"></div>

    <div class="header_top_buttons">
    
        <!-- <a href="#" onclick="window.close()" title="Close Window" class="close"></a> -->
        
        <a href="${contextPath}/logout.jsp" title="Logout" class="signout"></a>
        <a href="awChangepasswordform.html" title="Change Password" class="change-password"></a>
        <a href="${contextPath}/home.html" title="Home" class="home"></a>
        
        <br clear="all" />
    </div>
    
    <div class="creden_name_portal">
		${sessionScope.APPU.firstName}&nbsp;${sessionScope.APPU.lastName}
         <span class="creden_date_portal">&nbsp;|&nbsp;&nbsp;&nbsp;<%=PortalDateUtils.currentFormattedDate("EEE, MMM dd, yyyy")%></span>
    </div>
</div>