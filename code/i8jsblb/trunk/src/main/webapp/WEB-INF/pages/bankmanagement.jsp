<!--Author: Rizwan-ur-Rehman -->

<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
   <head>
<meta name="decorator" content="decorator">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
      <meta name="title" content="Bank"/>
      <script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>

   </head>
   <body bgcolor="#ffffff">
   <div id="successMsg" class ="infoMsg" style="display:none;"></div>
      <c:if test="${not empty messages}">
        <div class="infoMsg" id="successMessages">
          <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/>
            <br/>
          </c:forEach>
        </div>
        <c:remove var="messages" scope="session"/>
      </c:if>
   <div align="right"><a href="bankform.html" class="linktext"> Add Bank </a><br/></div>
      <ec:table
      items="bankList"
      var = "bankModel"
      retrieveRowsCallback="limit"
      filterRowsCallback="limit"
      sortRowsCallback="limit"
      action="${pageContext.request.contextPath}/bankmanagement.html"
      title="">
         	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Bank.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Bank.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Bank" fileName="Bank.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Bank.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
         <ec:row>
           <ec:column property="bankName" title="Bank"/>
           <ec:column property="contactName" title="Contact Name"/>
           <ec:column property="veriflyName" title="Verifly"/>
           <ec:column property="phoneNo" title="Phone No"/>
           <ec:column property="email" title="Email"/>

           <ec:column  property="bankId" alias="edit" title="Edit" viewsDenied="xls" filterable="false" sortable="false" style="text-align:center;">
             <a href ="${pageContext.request.contextPath}/bankform.html?bankId=${bankModel.bankId}">
             Edit
             </a>
           </ec:column>
           <ec:column  property="bankId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
             <%-- <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/bankmanagement.html?&_setActivate=${!bankModel.active}&bankId=${bankModel.bankId}');"><c:if test="${bankModel.active}">Deactivate</c:if><c:if test="${!bankModel.active}">Activate</c:if></a> --%>
             <tags:activatedeactivate 
			id="${bankModel.bankId}" 
			model="com.inov8.microbank.common.model.BankModel" 
			property="active"
			propertyValue="${bankModel.active}"
			callback="actdeact"
			error="defaultError"
			/>
             
           </ec:column>
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



