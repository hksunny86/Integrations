<!--Author: Jalil-ur-Rehman -->

<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>
<meta name="decorator" content="decorator">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
      <meta name="title" content="Operator Bank Info"/>
      <script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>

   </head>
   <body bgcolor="#ffffff">
      <div align="right">
        <a href="operatorbankinfoform.html" class="linktext">
          Add Operator Bank Info </a><br>&nbsp;&nbsp;
      </div>
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
      <ec:table
      items="operatorBankInfoModelList"
      var = "operatorBankInfoModel"
      retrieveRowsCallback="limit"
      filterRowsCallback="limit"
      sortRowsCallback="limit"
      action="${pageContext.request.contextPath}/operatorbankinfomanagement.html"
      title="">
         	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Operator Bank Info.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Operator Bank Info.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Operator Bank Info" fileName="Operator Bank Info.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Operator Bank Info.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
         <ec:row>
           <ec:column property="name" title="Operator Bank"/>
           <ec:column property="operatorName" title="Operator"/>
           <ec:column property="bankName" title="Bank"/>
           <ec:column property="paymentModeName" title="Payment Mode"/>
            <ec:column property="receivingAccountNo" title="Receiving Account No"/>
            <ec:column property="payingAccountNo" title="Paying Account No"/>
           <ec:column property="merchantCategory" title="Merchant Category"/>


            <ec:column  property="operatorBankInfoId"  title="Edit" viewsDenied="xls" filterable="false" sortable="false">
             <a href ="${pageContext.request.contextPath}/operatorbankinfoform.html?operatorBankInfoId=${operatorBankInfoModel.operatorBankInfoId}">
             Edit
             </a>
           </ec:column>
           
           
                  
         </ec:row>
      </ec:table>

 
   </body>
</html>



