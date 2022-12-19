<!--Author: Rizwan-ur-Rehman -->

<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
   <head>
<meta name="decorator" content="decorator">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
      <meta name="title" content="Branchless Banking Accounts"/>
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
      <ec:table
      items="olaAccountList"
      var = "olaAccountList"
      retrieveRowsCallback="limit"
      filterRowsCallback="limit"
      sortRowsCallback="limit"
      action="${pageContext.request.contextPath}/p_OLAAccountManagement.html"
      title="">
     	 <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
         	<ec:exportXls fileName="Branchless Banking Accounts.xls" tooltip="Export Excel"/>
         </authz:authorize>
         <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Branchless Banking Accounts.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
         <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Branchless Banking Accounts" fileName="Branchless Banking Accounts.pdf" tooltip="Export PDF" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
		 <ec:exportCsv fileName="Branchless Banking Accounts.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
         <ec:row>
           <ec:column property="firstName" title="First Name"/>
<%--           <ec:column property="middleName" title="Middle Name"/>--%>
           <ec:column property="lastName" title="Last Name"/>
<%--           <ec:column property="fatherName" title="Father's Name"/>--%>
           <ec:column property="cnic" title="CNIC" escapeAutoFormat="true"/>
<%--           <ec:column property="address" title="Address"/>--%>
<%--           <ec:column property="landlineNumber" title="Contact # Landline"/>--%>
           <ec:column property="mobileNumber" title="Mobile #" escapeAutoFormat="true"/>
           <ec:column property="dob" title="DOB" width="50%" cell="date" format="dd/MM/yyyy" />
           <ec:column property="payingAccNo" title="Inov8 credit account number "/>
           <ec:column property="balance" title="Balance" cell="currency" format="0.00"/>
           
        <%--	   <ec:column property="statusId" title="Account Status"/> --%>
    <%--    
        <ec:column alias="Status "  property="statusName" filterable="false" sortable="false"  style="vertical-align: middle">

				 	<c:if test="${olaAccountList.statusId eq 2}">
					 	<c:out value="Deactivated"></c:out>
				 	</c:if>
				 	
				 	<c:if test="${olaAccountList.statusId eq 3}">
					 	<c:out value="Blocked"></c:out>
				 	</c:if>
				 	
				 	<c:if test="${olaAccountList.statusId eq 1}">
					 	<c:out value="Active"></c:out>
				 	</c:if>
            </ec:column>
            --%>
<%--        <ec:column property="statusName" title="Status" filterable="false" sortable="false"/>
	       <ec:column property="balance" title	="Balance"/>

				<ec:column alias=" " 
						viewsAllowed="html" filterable="false" sortable="false">						
							<input type="button" class="button" style="width='90px'"
								value="View"
								onclick="javascript:changeInfo('${pageContext.request.contextPath}/p_OLACreateEditAccount.html?accountId=${olaAccountList.accountId}');" />
					</ec:column>

           <ec:column  property="accountId"  title="Edit" viewsDenied="xls" filterable="false" sortable="false">
             <a href ="${pageContext.request.contextPath}/p_OLACreateEditAccount.html?accountId=${olaAccountList.accountId}">
             Edit
             </a>
           </ec:column>
           --%>
         </ec:row>
      </ec:table>

      <script language="javascript" type="text/javascript">
      
      				function changeInfo(link)
				{
				    
				    
				      window.location.href=link;
				    
				}
      
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



