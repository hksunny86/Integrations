<%--
  Created by IntelliJ IDEA.
  User: bkr
  Date: 11/9/2016
  Time: 10:21 AM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
    <title>Versisys Data</title>
</head>
<body bgcolor="#ffffff">
<c:choose>
    <c:when test="${showErrorMsg}">
        <c:out value="${errorMsg}" />
    </c:when>

    <c:otherwise>
        <div class="eXtremeTable">
            <table class="tableRegion" width="100%">
                <tr>
                    <td class="titleRow" >Field Name</td>
                    <td class="titleRow" >Nadra Data</td>
                    <td class="titleRow" >Translated Data</td>
                </tr>
                <tr>
                    <td class="titleRow"  >Name</td>
                    <td>
                        <c:out value="${verisysDataModel.name}"/>
                    </td>
                    <td>
                        <c:out value="${verisysDataModel.nameTranslated}"/>
                    </td>
                </tr>
                <tr>
                    <td class="titleRow" >CNIC</td>
                    <td>
                        <c:out value="${verisysDataModel.cnic}"/>
                    </td>
                    <td>
                        <c:out value="${verisysDataModel.cnic}"/>
                    </td>
                </tr>
                <tr>
                    <td class="titleRow" >Mother Maiden Name</td>
                    <td>
                        <c:out value="${verisysDataModel.motherMaidenName}"/>
                    </td>
                    <td>
                        <c:out value="${verisysDataModel.motherMaidenNameTranslated}"/>
                    </td>
                </tr>
                <tr>
                    <td class="titleRow" >Birth Place</td>
                    <td>
                        <c:out value="${verisysDataModel.placeOfBirth}"/>
                    </td>
                    <td>
                        <c:out value="${verisysDataModel.placeOfBirthTranslated}"/>
                    </td>
                </tr>
                <tr>
                    <td class="titleRow" >Peresent Address</td>
                    <td>
                        <c:out value="${verisysDataModel.currentAddress}"/>
                    </td>
                    <td>
                        <c:out value="${verisysDataModel.currentAddressTranslated}"/>
                    </td>
                </tr>
                <tr>
                    <td class="titleRow" >Permanent Address</td>
                    <td>
                        <c:out value="${verisysDataModel.permanentAddress}"/>
                    </td>
                    <td>
                        <c:out value="${verisysDataModel.permanentAddressTranslated}"/>
                    </td>
                </tr>

            </table>
        </c:otherwise>
    </c:choose>
</div>
<table width="100%">
    <tr align="center">
        <td align="center" style="padding-top: 8px">
            <input type ="button" class="popbutton" value ="Close Window" onclick = "window.close();"/>
        </td>
    </tr>
</table>
</body>
</html>
