<jsp:directive.page import="com.inov8.microbank.common.model.AppUserModel"/>
<jsp:directive.page import="com.inov8.microbank.common.util.UserUtils"/><!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Rashid Mahmood-->
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

