<%@ page import="java.util.UUID" %>
<%@ page import="com.inov8.microbank.captcha.CaptchaGenerator" %>
<%@ page import="nl.captcha.Captcha" %>
<%@ page import="com.inov8.microbank.captcha.CaptchaUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="_csrToken" value='<%=UUID.randomUUID().toString()%>'/>
<%
    CaptchaGenerator captchaGenerator = new CaptchaGenerator();
    Captcha captcha = captchaGenerator.createCaptcha(320, 40);

    request.setAttribute("captcha", captcha.getAnswer());
    request.setAttribute("captchaEnc", CaptchaUtils.encodeBase64(captcha));
    String tokenStr = (String)pageContext.getAttribute("_csrToken");
    /*tokenStr += request.getRemoteAddr();*/
    request.setAttribute("_csrToken", tokenStr);
    // Creating the cookies object to avoid login csrf
    Cookie csrfcookie = new Cookie("_csrfcookie", tokenStr);
    csrfcookie.setMaxAge(300);

    // Adding cookies to the response header.
    response.addCookie(csrfcookie);
    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/pages/login.jsp");
    rd.forward(request, response);
%>


<%-- Include the login form --%>
<%--<jsp:include page="/WEB-INF/pages/login.jsp"/>--%>

