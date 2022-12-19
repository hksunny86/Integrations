<!--Author: Asad Hayat -->

<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>
<meta name="decorator" content="decorator">

      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
	<meta name="title" content="Smart Money Account "/>

   </head>
   <body bgcolor="#ffffff">
      <%--<div align="right">
        <a href="smartmoneyaccountform.html" class="linktext">
          Add Smart Money Account
        </a>&nbsp;&nbsp;
      </div>
      --%><c:if test="${not empty messages}">
        <div class="infoMsg" id="successMessages">
          <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/>
            <br/>
          </c:forEach>
        </div>
        <c:remove var="messages" scope="session"/>
      </c:if>
      <ec:table
      items="smartMoneyAccountList"
      var = "smartMoneyAccountModel"
      retrieveRowsCallback="limit"
      filterRowsCallback="limit"
      sortRowsCallback="limit"
      action="${pageContext.request.contextPath}/smartmoneyaccountmanagement.html"
      title="">
         	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Smart Money Account.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Smart Money Account.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Smart Money Account" fileName="Smart Money Account.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Smart Money Account.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
         <ec:row>
           <ec:column property="smartMoneyAccountName" title="Smart Money Account"/>
           <ec:column property="mfsId" title="Inov8 MWallet ID"/>
           <ec:column property="bankName" title="Bank"/>
           <ec:column property="mobileNo" title="Mobile No"/>
    	   <ec:column property="isActive" title="Status" filterable="true"/>


		   
		   <%-- 
		   <ec:column  property="smartMoneyAccountId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
             <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/smartmoneyaccountmanagement.html?&_setActivate=${!smartMoneyAccountList.active}&smartMoneyAccountId=${smartMoneyAccountList.smartMoneyAccountId}');"><c:if test="${smartMoneyAccountList.active}">Deactivate</c:if><c:if test="${!smartMoneyAccountList.active}">Activate</c:if></a>
           </ec:column>
		   
		     <ec:column  property="smartMoneyAccountId"  title="Edit" viewsDenied="xls" filterable="false" sortable="false">
             <a href ="${pageContext.request.contextPath}/smartmoneyaccountform.html?smartMoneyAccountId=${smartMoneyAccountList.smartMoneyAccountId}">
             Edit
             </a>
           </ec:column>
 --%>
           
		   
         </ec:row>
      </ec:table>

      <script language="javascript" type="text/javascript">
      function confirmUpdateStatus(link)
      {
        if (confirm('Are you sure you want to update status?')==true)
        {
          window.location.href=link;
        }
      }
      </script>

   </body>
</html>






