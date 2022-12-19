<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

  <link rel='stylesheet' href="${contextPath}/styles/style.css" type='text/css'>
  <link rel="stylesheet" type="text/css" media="all" href="${contextPath}/styles/decorator-style/theme.css"/>
  <link rel="stylesheet" type="text/css" media="print" href="${contextPath}/styles/decorator-style/print.css"/>
  <link rel='stylesheet' href="${contextPath}/styles/style_js.css" type='text/css'/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>

  <script type="text/javascript">

    function processBatch(event, id) {
      window.location = 'processBatch.html?id=' + id;
    }

    function cancelBatch(event, id) {
      window.location = 'cancelBatch.html?id=' + id;
    }
  </script>

    <%
        String createPermission = PortalConstants.BULK_SUM_VIEW_POPUP_CREATE;
        String updatePermission = PortalConstants.BULK_SUM_VIEW_POPUP_UPDATE;
        String readUpdatePermission = PortalConstants.BULK_SUM_VIEW_POPUP_READ;

    %>

</head>

<body>

<html:form name="bulkFileInfoSummaryViewForm" commandName="disbursementFileInfoViewModel" method="POST" action="p_bulkfileinfosummaryviewpopup.html">

  <div align="left" style="padding-left: 20px;">

    <table width="100%" border="0" >

      <tr>
        <td width="100%" align="center">
          <c:if test="${not empty messages}">
            <div class="infoMsg" id="successMessages">
              <c:forEach var="msg" items="${messages}">
                <c:out value="${msg}" escapeXml="false"/><br />
              </c:forEach>
            </div>
            <c:remove var="messages" scope="session"/>
          </c:if>
        </td>
      </tr>

      <tr>
        <td>
          <table border="0" cellpadding="5px;" style="font-weight: bold;">
            <tr>
              <td colspan="4" bgcolor="FBFBFB" style="vertical-align: middle">
                <h3><span style="font-weight: bold">${disbursementFileInfoViewModel.serviceName} - ${disbursementFileInfoViewModel.productName}
                  (${disbursementFileInfoViewModel.batchNumber})</span></h3>
              </td>
            </tr>
            <tr>
              <td align="right" class="formText" width="25%">File Name:</td>
              <td align="left" bgcolor="FBFBFB" class="text" width="25%">${disbursementFileInfoViewModel.filename}</td>
              <td align="right" class="formText" width="25%">File Path:</td>
              <td align="left" bgcolor="FBFBFB" width="25%">${disbursementFileInfoViewModel.filePath}</td>
            </tr>

            <tr>
              <td align="right" class="formText">Uploaded On:</td>
              <td align="left" bgcolor="FBFBFB"><fmt:formatDate pattern="dd/MM/yyyy hh:mm:ss" value="${disbursementFileInfoViewModel.createdOn}"/></td>
              <td align="right" class="formText">File Status:</td>
              <td align="left" bgcolor="FBFBFB">${disbursementFileInfoViewModel.statusStr}</td>
            </tr>
            <tr>
              <td colspan="4"><span style="height: 30px;"/> </td>
            </tr>
            <tr>
              <td align="right" class="formText">Total Records:</td>
              <td align="left" bgcolor="FBFBFB"><fmt:formatNumber pattern="#,###.##" value="${disbursementFileInfoViewModel.totalRecords}"/></td>
            </tr>
            <tr>
              <td align="right" class="formText">Valid Records:</td>
              <td align="left" bgcolor="FBFBFB"><fmt:formatNumber pattern="#,###.##" value="${disbursementFileInfoViewModel.validRecords}"/></td>
              <td align="right" class="formText">Invalid Records:</td>
              <td align="left" bgcolor="FBFBFB"><fmt:formatNumber pattern="#,###.##" value="${disbursementFileInfoViewModel.invalidRecords}"/></td>
            </tr>
            <tr>
              <td align="right" class="formText">Disbursement To:</td>
              <td align="left" bgcolor="FBFBFB">${disbursementFileInfoViewModel.appUserTypeName}</td>
              <td align="right" class="formText">Disbursement Amount:</td>
              <td align="left" bgcolor="FBFBFB"> <fmt:formatNumber pattern="#,###.##" value="${disbursementFileInfoViewModel.totalAmount}"/></td>
            </tr>
            <tr>
              <td align="right" class="formText">Total Charges:</td>
              <td align="left" bgcolor="FBFBFB"><fmt:formatNumber pattern="#,###.##" value="${disbursementFileInfoViewModel.totalCharges}"/></td>
              <td align="right"class="formText">Total FED:</td>
              <td align="left" bgcolor="FBFBFB"><fmt:formatNumber pattern="#,###.##" value="${disbursementFileInfoViewModel.totalFed}"/></td>
            </tr>
            <tr>
              <td align="right" class="formText">Total Amount:</td>
              <td align="left" bgcolor="FBFBFB">
                <fmt:formatNumber pattern="#,###.##" value="${disbursementFileInfoViewModel.totalAmount + disbursementFileInfoViewModel.totalCharges}"/> </td>
            </tr>
            <tr>
              <td colspan="4"><span style="height: 30px;"/> </td>
            </tr>

            <c:if test="${disbursementFileInfoViewModel.status != 10}">
              <tr style="padding-top: 20px;">
                <td colspan="4" bgcolor="FBFBFB">
                  <h3><span style="font-weight: bold">File Settlement Status</span> </h3>
                </td>
              </tr>
              <c:if test="${disbursementFileInfoViewModel.serviceId == 13}">
              <tr>
                <td align="right" class="formText">Total A/c Created :</td>
                <td align="left" bgcolor="FBFBFB" class="formText"><fmt:formatNumber pattern="#,###.##" value="${totalCreated}"/></td>
              </tr>
              </c:if>
              <tr>
                <td align="right" class="formText">Total Settled :</td>
                <td align="left" bgcolor="FBFBFB" class="formText"><fmt:formatNumber pattern="#,###.##" value="${totalSettled}"/></td>
              </tr>
              <tr>
                <td align="right" class="formText">First Settled On:</td>
                <td align="left" bgcolor="FBFBFB" class="formText"><fmt:formatDate pattern="dd/MM/yyyy hh:mm:ss" value="${firstSettledOn}"/></td>
                <td align="right" class="formText">Last Settled On :</td>
                <td align="left" bgcolor="FBFBFB" class="formText"><fmt:formatDate pattern="dd/MM/yyyy hh:mm:ss" value="${lastSettledOn}"/></td>
              </tr>
            </c:if>
            <tr>
              <td colspan="4" align="center">
                <input type="hidden" name="id" value="${disbursementFileInfoViewModel.disbursementFileInfoId}"/>
<%--                  <c:if test="${(disbursementFileInfoViewModel.status == 4 || disbursementFileInfoViewModel.status == 6)--%>
<%--                              && disbursementFileInfoViewModel.totalRecords != disbursementFileInfoViewModel.invalidRecords}">--%>

                    <authz:authorize ifAnyGranted="<%=createPermission%>">
                      <c:if test="${disbursementFileInfoViewModel.status == 4}">
                        <input id="bt1" type="button" name="button" value="Confirm"
                               onclick="processBatch(this, '${disbursementFileInfoViewModel.disbursementFileInfoId}');"/>
                      </c:if>

                      <c:if test="${disbursementFileInfoViewModel.status == 6 && disbursementFileInfoViewModel.validRecords != totalCreated}">
                        <input id="btn2" type="button" name="button" value="Retry Walk-in Creation"
                               onclick="processBatch(this, '${disbursementFileInfoViewModel.disbursementFileInfoId}');"/>
                      </c:if>

                      <c:if test="${(disbursementFileInfoViewModel.status == 4 || disbursementFileInfoViewModel.status == 6)
                                       && disbursementFileInfoViewModel.totalRecords != disbursementFileInfoViewModel.invalidRecords}">
                        <c:if test="${disbursementFileInfoViewModel.cancel}">
                        <input id="btn3" type="button" name="button" value="Cancel Batch"
                               onclick="cancelBatch(this, '${disbursementFileInfoViewModel.disbursementFileInfoId}');"/>
                        </c:if>
                      </c:if>
                    </authz:authorize>
<%--                  </c:if>--%>

                  <input id="button" name="button" type="button" value="Close" onclick="javascript:window.close();" />
              </td>
            </tr>
          </table>
        </td>
      </tr>

    </table>
  </div>

</html:form>

</body>
</html>