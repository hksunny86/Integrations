<!--Author: Mohammad Shehzad Ashraf -->
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<%@include file="/common/taglibs.jsp"%>

<html>
  <head>
<meta name="decorator" content="decorator">
    
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Users" />
<%@include file="/common/ajax.jsp"%>
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
	<script type="text/javascript">
		var jq=$.noConflict();
		function initProgress()
		{
			  $('errorMsg').innerHTML = "";
			  $('successMsg').innerHTML = "";
			  Element.hide('successMsg');
			  Element.hide('errorMsg'); 
			  if($('successMessages') != null){
				  $('successMessages').innerHTML = "";
				  Element.hide('successMessages');
			  }
			if (confirm('If customer information is verified then press OK to continue')==true)
		    {
		    	return true;
		    }
		    else
			{
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
			    // display success message
			    Element.show('successMsg');
			} 
				isErrorOccured = false;
		}

	</script>


  </head>
  
  <body bgcolor="#ffffff">

		<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<div id="errorMsg" class ="errorMsg" style="display:none;"></div>  

			<c:if test="${not empty status.errorMessages}">
				<div class="errorMsg">
					<c:forEach var="error" items="${status.errorMessages}">
						<c:out value="${error}" escapeXml="false" />
						<br />
					</c:forEach>
				</div>
			</c:if>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
  
    <table border="0" width="100%" cellpadding="0" cellspacing="0">
        <tr>
            <td align="right">
  	             <authz:authorize ifAnyGranted="<%=PortalConstants.ADM_USR_MGMT_CREATE%>">          
				      <a href="javascript: window.location='p_adminuserform.html'">Add User</a>
  	             </authz:authorize>
  	             <authz:authorize ifNotGranted="<%=PortalConstants.ADM_USR_MGMT_CREATE%>">          
						&nbsp;
  	             </authz:authorize>
  	                
            </td>
        </tr>
    </table>
    
    
    	<ec:table 
		items="userManagementListViewModelList" 
                var = "UserManagementListViewModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/p_adminusermanagement.html"
				title="">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Users.xls" tooltip="Export Excel"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Users.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Users" fileName="Users.pdf" tooltip="Export PDF" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Users.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
		<ec:row>
		
		<c:set var="UserManagementListAppUserId" >
			<security:encrypt strToEncrypt="${UserManagementListViewModel.appUserId}"/>
		</c:set>
		
			<ec:column property="username" title="User ID" filterable="true" escapeAutoFormat="true">
				<authz:authorize ifAnyGranted="<%=PortalConstants.ADM_USR_MGMT_UPDATE%>">
			        <a href="${pageContext.request.contextPath}/p_adminuserform.html?appUserId=${UserManagementListAppUserId}">
			            <c:out value="${UserManagementListViewModel.username}"></c:out>
					</a>			            
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=PortalConstants.ADM_USR_MGMT_UPDATE%>">
			            <c:out value="${UserManagementListViewModel.username}"></c:out>
				</authz:authorize>		
						        
		    </ec:column>
			<ec:column property="firstName" title="First Name" filterable="true"/>
			<ec:column property="lastName" title="Last Name" filterable="true"/>
			<ec:column property="partnerGroupName" title="Partner Group Name" />
			
			<ec:column alias="" title="Email" style="text-align: center"
					filterable="false" sortable="false" viewsAllowed="html" >
				

				
				<authz:authorize ifAnyGranted="<%=PortalConstants.RST_USR_PWD_UPDATE%>">
						<input type="button" id="rstPass${UserManagementListAppUserId}" name="rstPass${UserManagementListAppUserId}" value="Reset Password" style="font-family:verdana, arial, helvetica, sans-serif;font-size: 10px">	
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=PortalConstants.RST_USR_PWD_UPDATE%>">
						<input type="button" id="rstPass${UserManagementListAppUserId}" name="rstPass${UserManagementListAppUserId}" value="Reset Password" style="font-family:verdana, arial, helvetica, sans-serif;font-size: 10px" disabled="disabled">	
				</authz:authorize>											
					
					<input type="hidden" value="${UserManagementListAppUserId}" name="appUser${UserManagementListAppUserId}" id="appUser${UserManagementListAppUserId}" />			
					<ajax:htmlContent baseUrl="${contextPath}/p_adminuserformajax.html" 
						eventType="click" 
						source="rstPass${UserManagementListAppUserId}" 
						target="successMsg" 
						parameters="appUserId={appUser${UserManagementListAppUserId}}"
						errorFunction="globalAjaxErrorFunction"
						preFunction="initProgress"
						postFunction="resetProgress"
						/>
			
			</ec:column>		
			
        </ec:row>
	</ec:table>
    
    
    
	
    <script language="javascript" type="text/javascript">
            
	</script>
   
  </body>
</html>
