<%@ page import="java.util.UUID" %>
<%@ include file="/common/taglibs.jsp"%>

<%--
Turab:Security:CSRF
--%>
<c:set var="_csrToken" value='<%=UUID.randomUUID().toString()%>'/>
<%
    String tokenStr = (String)pageContext.getAttribute("_csrToken");
    request.setAttribute("_csrToken", tokenStr);
    // Creating the cookies object to avoid login csrf
    Cookie csrfcookie = new Cookie("_csrfcookie", tokenStr);
    csrfcookie.setMaxAge(300);

    // Adding cookies to the response header.
    response.addCookie(csrfcookie);
    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/pages/awlogin.jsp");
    rd.forward(request, response);
%>

<%--
<%@ include file="/common/taglibs.jsp"%>

&lt;%&ndash; Include the awlogin form &ndash;%&gt;
<jsp:include page="/WEB-INF/pages/awlogin.jsp"/>
--%>
