<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<c:set var="retrieveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="updateAction"><%=PortalConstants.ACTION_UPDATE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<meta name="title" content="Account Types"/>
	<script type="text/javascript" src="${contextPath}/scripts/activatedeactivate.js"></script>
	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
	
	<script type="text/javascript">
		var jq=$.noConflict();
		function error(request)
		{
     		alert("An unknown error has occured. Please contact with the administrator for more details");
		}

		function actdeact(request)
		{
			isOperationSuccessful(request);
		}

		function initProgress()
		{
			if (confirm('Are you sure you want to update status?')==true)
		    {
		    	<c:if test="${not empty messages}">
		    		Element.hide('successMessages'); 
		    	</c:if>
		    	return true;
		    }
		    else
			{
			  $('errorMsg').innerHTML = "";
			  $('successMsg').innerHTML = "";
			  $('message').value="";
			  Element.hide('successMsg'); 
			  Element.hide('errorMsg'); 
			  return false;
			}
		}
		
		var isErrorOccured = false;		
		function resetProgress()
		{
		    if(!isErrorOccured)
		    {
			    // clear error box
			    $('errorMsg').innerHTML = "";
			    
		  		Element.hide('errorMsg'); 
		  		Element.hide('successMsg'); 
			    $('successMsg').innerHTML = $F('message');
			    // display success message
			    Element.show('successMsg');
			} 
				isErrorOccured = false;
		}

  
		function reportError(request, obj) 
		{
		  var msg = "Your request cannot be processed at the moment. Please try again later.";
		  $('successMsg').innerHTML = "";
		  Element.hide('successMsg'); 
		  $('errorMsg').innerHTML = msg;
		  Element.show('errorMsg');
		  isErrorOccured = true;
		}
	</script>
	<%
		String createPermission = PortalConstants.ADMIN_GP_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.MNG_CUST_AC_TP_LMT_CREATE;
		
		String readPermission = PortalConstants.ADMIN_GP_READ;
		readPermission +=	"," + PortalConstants.PG_GP_READ;
		readPermission +=	"," + PortalConstants.MNG_CUST_AC_TP_LMT_READ;
		
		String updatePermission =	PortalConstants.ADMIN_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.MNG_CUST_AC_TP_LMT_UPDATE;
	 %>
	</head>
	<body bgcolor="#ffffff">
	  	<%@include file="/common/ajax.jsp"%>
	  	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
			<div id="errorMsg" class ="errorMsg" style="display:none;"></div>
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
			    <c:forEach var="msg" items="${messages}">
			      <c:out value="${msg}" escapeXml="false"/>
			      <br/>
			    </c:forEach>
			</div>
	  		<c:remove var="messages" scope="session"/>
		</c:if>
		<authz:authorize ifAnyGranted="<%=createPermission %>">
		   <div align="right"><a href="p_customeraccounttypeform.html?actionId=${createAction}" class="linktext"> Add Account Type </a></div>
		</authz:authorize>
		<ec:table items="olaCustomerAccountTypeModelList" var="accountTypeModel" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit"
			action="${contextPath}/p_customeraccounttypemanagement.html?actionId=${retrieveAction}" filterable="true">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			  <ec:exportXls fileName="Account Types.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Account Types.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			  <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Account Types" fileName="Account Types.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Account Types.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
		  	<ec:row>
	  			<ec:column property="accountTypeCategory" sortable="false" filterOptions="droplist" filterable="true" title="Account Type Category"/>
			  	<ec:column property="name" title="Account Type"/>
			  	<ec:column property="parentAccountTypeName" title="Parent Account Type"/>
			  	<authz:authorize ifAnyGranted="<%=updatePermission%>">
				  	<ec:column property="accountTypeId" title="Edit" sortable="false" style="text-align: center;" filterable="false"  viewsAllowed="html">
		               <a href="${contextPath}/p_customeraccounttypeform.html?actionId=${updateAction}&customerAccountTypeId=${accountTypeModel.accountTypeId}&isCustomerAccountType=${accountTypeModel.isCustomerAccountType}">Edit</a>
		             </ec:column>
					<ec:column property="isActive" sortable="false" filterable="false"  viewsAllowed="html">
			           	<tags:activatedeactivate id="${accountTypeModel.accountTypeId}" model="com.inov8.integration.common.model.OlaCustomerAccountTypeModel" property="active"
							propertyValue="${accountTypeModel.isActive}" callback="actdeact" error="defaultError" />
		            </ec:column>
	            </authz:authorize>
	            <authz:authorize ifNotGranted="<%=updatePermission%>">
	            	<ec:column property="accountTypeId" title="View" sortable="false"  viewsAllowed="html">
		               <a href="${contextPath}/p_customeraccounttypeform.html?actionId=${updateAction}&customerAccountTypeId=${olaCustomerAccountTypeModel.customerAccountTypeId}">View</a>
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
	</body>
</html>
