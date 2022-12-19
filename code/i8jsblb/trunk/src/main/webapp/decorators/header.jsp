<jsp:directive.page import="com.inov8.microbank.common.util.UserUtils"/>
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<%@include file="/common/taglibs.jsp"%>

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


<html>
<head>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/extremecomponents.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/scriptaculous.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/global.js'/>"></script>
</head>

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
		<% if(!(UserUtils.getCurrentUser().getAppUserTypeId()== 7L)){ %>
		<div class="logo_portal"></div>
		<% }        %>
		<% if(UserUtils.getCurrentUser().getAppUserTypeId()== 7L){ %>
			<div class="sco_logo_portal"></div>
		<% }        %>
        <!--<div class="banner_right_top"></div>-->

	    <div class="header_top_buttons">
	    	<a href="${contextPath}/home.html" title="Home" class="btn btn-large btn-home btn-home-icon">Home</a>
	        <a href="changepasswordform.html" title="Change Password" class="btn btn-large btn-setting btn-setting-icon">Change Password</a>
            <a href="${contextPath}/logout.jsp" title="Logout" class="btn btn-large btn-signout btn-signout-icon">Signout</a>
	        
	        <br clear="all" />
	    </div>
	    
	    <%-- <div class="creden_name_portal">
	    	<%
				AppUserModel appUser = UserUtils.getCurrentUser();
				if(null!=appUser)
				{
					if (null!=appUser.getFirstName())
					{
						out.print(appUser.getFirstName());
						if ((appUser.getFirstName().length() + appUser.getFirstName().length()) > 50)
						{
							out.print("</BR>");
						}
					}
						
					out.print("&nbsp;");

					if (null!=appUser.getLastName())
						out.print(appUser.getLastName());
				}
			 %>
             <span class="creden_date_portal">&nbsp;|&nbsp;&nbsp;&nbsp;<%=PortalDateUtils.currentFormattedDate("EEE, MMM dd, yyyy")%></span>
	    </div> --%>
	</div>

	<%--
	<div id="branding">
	  <img src="${contextPath}/images/portal-logo.png"/>
	</div>
	<div id="partnerlogo">
		<c:choose>
			<c:when test="${logo eq null}"></c:when>
			<c:otherwise>
			  <img src="${contextPath}/images/${logo}"/>
			</c:otherwise>
		</c:choose>
	</div>
	<div id="divider"><div>
	--%>
