<%@ taglib prefix="html" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: Inov8
  Date: 11/29/2018
  Time: 3:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/xml;charset=UTF-8" language="java" %>
<%--<%@include file="/common/taglibs.jsp"%>--%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"--%>
<%--"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">--%>
<html>
    <head>

        <meta http-equiv="cache-control" content="no-cache" />
        <meta http-equiv="cache-control" content="no-store" />
        <meta http-equiv="cache-control" content="private" />
        <meta http-equiv="cache-control" content="max-age=0, must-revalidate" />
        <meta http-equiv="expires" content="now-1" />
        <meta http-equiv="pragma" content="no-cache" />

        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
        <meta name="title" content="Action Log XML"/>
    </head>
    <body>
        <table width="950px">
            <tr>
                <td align="left">${inputXML}</td>
                <td align="left">${outputXML}</td>
            </tr>
        </table>
    </body>
</html>
