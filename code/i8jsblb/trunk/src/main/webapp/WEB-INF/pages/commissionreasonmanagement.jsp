<!--Author: Rizwan-ur-Rehman -->

<%@include file="/common/taglibs.jsp"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>
<meta name="decorator" content="decorator">
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
      <meta name="title" content="Charges Method"/>

   </head>
   <body bgcolor="#ffffff">
<%--      <div align="right">--%>
<%--        <a href="commissionreasonform.html" class="linktext">--%>
<%--          Add Commission Reason--%>
<%--        </a><br>&nbsp;&nbsp;--%>
<%--      </div>--%>
      <c:if test="${not empty messages}">
        <div class="infoMsg" id="successMessages">
          <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/>
            <br/>
          </c:forEach>
        </div>
        <c:remove var="messages" scope="session"/>
      </c:if>
      <ec:table
      items="commissionReasonList"
      var = "commissionReasonModel"
      retrieveRowsCallback="limit"
      filterRowsCallback="limit"
      sortRowsCallback="limit"
      action="${pageContext.request.contextPath}/commissionreasonmanagement.html"
      title="">
         	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Charges Method.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Charges Method.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Charges Method" fileName="Charges Method.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Charges Method.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
         
         <ec:row>
           <ec:column property="name" title="Charges Method"/>
           <ec:column property="reasonDesc" title="Description" />
           <ec:column  property="commissionReasonId"  title="Edit" viewsDenied="xls" filterable="false" sortable="false">
             <a href ="${pageContext.request.contextPath}/commissionreasonform.html?commissionReasonId=${commissionReasonModel.commissionReasonId}">
             Edit
             </a>
           </ec:column>
         </ec:row>
      </ec:table>
   </body>
</html>



