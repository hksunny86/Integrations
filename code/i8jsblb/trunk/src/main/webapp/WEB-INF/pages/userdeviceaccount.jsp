<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->

<%@include file="/common/taglibs.jsp"%>
<html>

	<head>
<meta name="decorator" content="decorator">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<meta name="title" content="MWallet/Device Account" />
		<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{

			isOperationSuccessful(request);
			updateLockUnlock();
		}
	    	function updateLockUnlock(divId)
	      	{
				var url = '${contextPath}/refreshlockunlock.html';
				var pars = 'actionId=${defaultAction}';

				var myAjax = new Ajax.Updater(
					{success: 'successLockUnlock'}, 
					url, 
					{
						method: 'post', 
						parameters: pars,
						onFailure: reportError,
						onSuccess: updateValue
					});
	      	}
	      	
	      	function reportError(request)
	      	{
	      		alert('Inside ReportError');
	      	}
			
	      	function updateValue(request)
	      	{
	      		$('successLockUnlock').innerHTML="";
		      	$('successLockUnlock').hide();
	      		response = request.responseText;
	      		var responseArray = response.split(",");
	      		divName = 'div_'+responseArray[0];
			    $(divName).innerHTML = responseArray[1];
			    
			    
	      	}
		
	</script>

	</head>

	<body bgcolor="#ffffff">
			   <div align="right"><a href="userdeviceaccountform.html" class="linktext"> Add MWallet/Device Account </a><br>&nbsp;&nbsp;</div>
			   <div id="successMsg" class ="infoMsg" style="display:none;"></div>
			   <div id="successLockUnlock" class = "infoMsg" style="height: 0px;width: 0px;background: white;border: 0px"></div>
			
			<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
			
			
			
			<ec:table 
				items="userDeviceAccountList" 
				var="userDeviceAccountListViewModel" 
				retrieveRowsCallback="limit" 
				filterRowsCallback="limit" 
				sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/userdeviceaccount.html"
				title="">
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
					<ec:exportXls fileName="MWallet/Device Account.xls" tooltip="Export Excel"/>
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
					<ec:exportXlsx fileName="MWallet/Device Account.xlsx" tooltip="Export Excel" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
					<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="MWallet/Device Account" fileName="MWallet/Device Account.pdf" tooltip="Export PDF"/>
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
					<ec:exportCsv fileName="MWallet/Device Account.csv" tooltip="Export CSV"></ec:exportCsv>
				</authz:authorize>
				<ec:row>
    				<ec:column property="userId" filterable="true" title="Inov8 MWallet ID"/>
    				
    				<ec:column property="firstName" filterable="true" title="First Name">
    					<c:out value="${userDeviceAccountListViewModel.firstName}"/>    					
    				</ec:column>
    				<ec:column property="lastName" filterable="true" title="Last Name">
    					<c:out value="${userDeviceAccountListViewModel.lastName}"/>    					
    				</ec:column>
    							
    				
    				<ec:column property="accountLocked" title="Locked" filterable="false" viewsDenied="xls">
      					<div id="div_<c:out value="${userDeviceAccountListViewModel.userId}"/>">
        					<c:if test="${userDeviceAccountListViewModel.accountLocked}">Locked</c:if>
        					<c:if test="${!userDeviceAccountListViewModel.accountLocked}">Un-Locked</c:if>
        				</div>	
      					
    				</ec:column>			
      				
      				<ec:column property="accountExpired" title="Expired" filterable="false" viewsDenied="xls">      					
        					<c:if test="${!userDeviceAccountListViewModel.accountExpired}">Not Expired</c:if>
        					<c:if test="${userDeviceAccountListViewModel.accountExpired}">Expired</c:if>      					
    				</ec:column>			
 
      				<ec:column property="accountExpired" title="Commissioned" filterable="false" viewsDenied="xls">      					
        					<c:if test="${!userDeviceAccountListViewModel.commissioned}">No</c:if>
        					<c:if test="${userDeviceAccountListViewModel.commissioned}">Yes</c:if>      					
    				</ec:column>	     				
      				
      				
      				<ec:column alias=" " filterable="false"  title="Edit" sortable="false" viewsDenied="xls">
						
						<a href="${pageContext.request.contextPath}/userdeviceaccountform.html?flag=true&userDeviceAccountsId=${userDeviceAccountListViewModel.userDeviceAccountsId}">Edit</a>						
					</ec:column>
					
			<ec:column  property="accountEnabled" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
   <%--  <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/userdeviceaccount.html?&_setActivate=${!userDeviceAccountListViewModel.accountEnabled}&userDeviceAccountsId=${userDeviceAccountListViewModel.userDeviceAccountsId}')">
    <c:if test="${userDeviceAccountListViewModel.accountEnabled}">Deactivate</c:if>
    <c:if test="${!userDeviceAccountListViewModel.accountEnabled}">Activate</c:if>
    </a> --%>
    
    <tags:activatedeactivate id="${userDeviceAccountListViewModel.userDeviceAccountsId}"
						model="com.inov8.microbank.common.model.UserDeviceAccountsModel"
						property="accountEnabled"
						url="activatedeactivateuserdeviceaccount.html"
						propertyValue="${userDeviceAccountListViewModel.accountEnabled}"
						callback="actdeact" error="defaultError" />
    
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











































































