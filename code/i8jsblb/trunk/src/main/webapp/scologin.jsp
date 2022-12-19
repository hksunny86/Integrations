<%@ page import="java.util.UUID" %>
<%@ include file="/common/taglibs.jsp"%>

<%--
Turab:Security:CSRF
--%>
<c:set var="_csrToken" value='<%=UUID.randomUUID().toString()%>'/>
<%
    String tokenStr = (String)pageContext.getAttribute("_csrToken");
    // Creating the cookies object to avoid login csrf
    request.setAttribute("_csrToken", tokenStr);
    Cookie csrfcookie = new Cookie("_csrfcookie", tokenStr);
    csrfcookie.setMaxAge(300);

    // Adding cookies to the response header.
    response.addCookie(csrfcookie);
    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/pages/scologin.jsp");
    rd.forward(request, response);
%>

