
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<jsp:directive.page import="com.inov8.microbank.common.util.PortalDateUtils"/><!--Author: Fahad Tariq -->
<%@include file="/common/taglibs.jsp"%>

<html>
  <head>
<meta name="decorator" content="decorator">

   <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script> 
<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />	    
		<!-- <script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script> -->
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
	<script type="text/javascript">
	var jq=$.noConflict();
	var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
<meta name="title" content="Manage Stakeholders" />

		<%
			String createPermission = PortalConstants.ADMIN_GP_READ +","+ PortalConstants.MNG_COMM_SH_ACC_CREATE;
			String updatePermission = PortalConstants.ADMIN_GP_READ +","+ PortalConstants.MNG_COMM_SH_ACC_UPDATE;
		%>
  </head>
  
  <body bgcolor="#ffffff">
  <%@include file="/common/ajax.jsp"%>
  
  <script language="javascript" type="text/javascript">
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
      </script>
      
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
	<!-- 
	<authz:authorize ifAnyGranted="<%=createPermission%>">
		<div align="right">
			<a href="commissionstakeholderaccountsform.html" class="linktext">
				Add Stakeholder Accounts
			</a><br>&nbsp;&nbsp;
		</div>
	</authz:authorize>
 -->
<form name="commissionstakeholderaccountsForm" method="post" action="commissionstakeholderaccountsmanagement.html" onsubmit="return validateForm(this);">
<table width="100%" >

<tr bgcolor="FBFBFB">





    <td align="right" bgcolor="F3F3F3" class="formText">StakeHolder:&nbsp;</td>
    <td>

      <spring:bind path="extendedCommShAcctsListViewModel.commStakeHolderId">
      <select name="commStakeHolderId" class="textBox">
      		  <option value="">---All---</option>
              <c:forEach items="${commissionStakeholderModelList}" var="commissionStakeholderModelList">
                <option value="${commissionStakeholderModelList.commissionStakeholderId}"
                  <c:if test="${status.value == commissionStakeholderModelList.commissionStakeholderId}">selected="selected"</c:if>/>
                  ${commissionStakeholderModelList.name}
                </option>
              </c:forEach>
            </select>
            </spring:bind>

            
      
    </td>
  </tr>
  
  <tr>
<td></td>
    <td align="left" ><input type="submit"  class="button" value="Search" tabindex="3" name="_search"/>
    <input type="reset" class="button" value="Cancel"  name="_cancel" onClick="javascript: window.location='commissionstakeholderaccountsmanagement.html'"></td>
    <td align="left" >&nbsp;</td>
    <td align="left" >&nbsp;
   </td>
   </tr>
	</table>
</form>

<ec:table filterable="false"
		items="extendedCommShAcctsListViewModelList"
        var = "stakeholderBankInfoModel"
        retrieveRowsCallback="limit"
        filterRowsCallback="limit"
        sortRowsCallback="limit"
		action="${pageContext.request.contextPath}/commissionstakeholderaccountsmanagement.html"
		title="">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Stakeholder Accounts.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Stakeholder Accounts.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Stakeholder Accounts"
					fileName="Stakeholder Accounts.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Stakeholder Accounts.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
		<ec:row>
			<ec:column property="accountNo" filterable="false" title="Account No" escapeAutoFormat="true"/>
			<ec:column property="accountTitle" filterable="false" title="Account Title" />
			<ec:column property="accountType" filterable="false" title="Account Type Category" sortable="false"/>
			
			<ec:column property="accountTypeName" title="Account Type" filterable="false" sortable="false"  style="text-align: right"/>
			<ec:column property="productName" title="Product" filterable="false" sortable="false"  style="text-align: right"/>
			
			<ec:column property="shName" filterable="false" title="Stakeholder Name"/>
			<authz:authorize ifAnyGranted="<%=updatePermission%>">
			<ec:column alias="edit" title="Edit" viewsDenied="xls" filterable="false" sortable="false" style="text-align:center;">
		       <c:if test="${stakeholderBankInfoModel.bankId == 50110}">
			       <a href ="${pageContext.request.contextPath}/commissionstakeholderaccountsform.html?stakeholderBankInfoId=${stakeholderBankInfoModel.stakeholderBankInfoId}">
			             Edit
			        </a>
		       </c:if>
			</ec:column>
			</authz:authorize>
			
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
	<%-- <script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script> --%>
</body>
</html>
