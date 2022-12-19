<!--Author: Jalil-ur-Rehman -->

<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>
<meta name="decorator" content="decorator">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
      <meta name="title" content="Stakeholder Bank"/>
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
        <a href="stakeholderbankform.html" class="linktext">
          Add Stakeholder Bank
        </a><br>&nbsp;&nbsp;
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
      items="stakeholderBankInfoModelList"
      var = "stakeholderBankInfoModel"
      retrieveRowsCallback="limit"
      filterRowsCallback="limit"
      sortRowsCallback="limit"
      action="${pageContext.request.contextPath}/stakeholderbankinfomanagement.html"
      title="">
         	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Stakeholder Bank.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Stakeholder Bank.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Stakeholder Bank" fileName="Stakeholder Bank.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Stakeholder Bank.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
         <ec:row>
           <ec:column property="name" title="Stakeholder Bank"/>
           <ec:column property="commissionStakeholderName" title="Stakeholder"/>
           <ec:column property="stakeholderTypeName" title="Stakeholder Type"/>
           <ec:column property="bankName" title="Bank"/>
           <ec:column property="accountNo" title="Account No"/>
           <ec:column  property="stakeholderBankInfoId"  title="Edit" viewsDenied="xls" filterable="false" sortable="false">
             <a href ="${pageContext.request.contextPath}/stakeholderbankform.html?stakeholderBankInfoId=${stakeholderBankInfoModel.stakeholderBankInfoId}">
             Edit
             </a>
           </ec:column>
           
           <ec:column  property="active" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
                         <%-- <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/stakeholderbankinfomanagement.html?&_setActivate=${!stakeholderBankInfoModel.active}&stakeholderBankInfoId=${stakeholderBankInfoModel.stakeholderBankInfoId}')"><c:if test="${stakeholderBankInfoModel.active}">Deactivate</c:if><c:if test="${!stakeholderBankInfoModel.active}">Activate</c:if></a> --%>
                         
                         <tags:activatedeactivate 
			id="${stakeholderBankInfoModel.stakeholderBankInfoId}" 
			model="com.inov8.microbank.common.model.StakeholderBankInfoModel" 
			property="active"
			propertyValue="${stakeholderBankInfoModel.active}"
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



