<!--Author: Mohammad Shehzad Ashraf -->
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>

<html>
  <head>
<meta name="decorator" content="decorator">
    
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="User Management" />
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

	<%
		String createPermission = PortalConstants.ADMIN_GP_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.MNG_USRS_CREATE;
		
		String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.MNG_USRS_UPDATE;

		String userGroupUpdatePermission = PortalConstants.ADMIN_GP_UPDATE;
		userGroupUpdatePermission += "," + PortalConstants.PG_GP_UPDATE;
		userGroupUpdatePermission += "," + PortalConstants.MNG_USR_GRPS_UPDATE;
	%>
  </head>
  
  <body bgcolor="#ffffff">
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
	<div id="errorMsg" class ="errorMsg" style="display:none;"></div>  
   <c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
    <table border="0" width="100%" cellpadding="0" cellspacing="0">
        <tr>
            <td align="right">
				<authz:authorize ifAnyGranted="<%=createPermission%>">
					<a href="javascript: window.location='p_i8userformtest.html?actionId=${createAction}'" class="linktext">Add User</a>
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
				action="${pageContext.request.contextPath}/p_i8usermanagement.html?actionId=${retriveAction}"
				title="">
				
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Users.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Users.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Users"
				fileName="Users.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Users.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>		
		
		<ec:row>
		<c:set var="UserManagementListAppUserId">
			<security:encrypt strToEncrypt="${UserManagementListViewModel.appUserId}"/>
		</c:set>
		
			<ec:column property="username" title="User ID" filterable="true" escapeAutoFormat="true">
				<authz:authorize ifAnyGranted="<%=updatePermission%>">
				<c:if test="${UserManagementListViewModel.partnerGroupId != 1031}" >
						<c:choose>
			    			<c:when test="${restrictedUserPartnerGroup == UserManagementListViewModel.partnerGroupId}">
			    				<c:if test="${allowAccessOfCurrentUser}">
						        	 <a href="${pageContext.request.contextPath}/p_i8userformtest.html?appUserId=${UserManagementListAppUserId}&actionId=${retriveAction}">
							            <c:out value="${UserManagementListViewModel.username}"></c:out>
							        </a>
					        	</c:if>
					        	<c:if test="${!allowAccessOfCurrentUser}">
							    	<c:out value="${UserManagementListViewModel.username}"></c:out>
					        	</c:if>
				        	</c:when>
				        	<c:otherwise>
			    				<a href="${pageContext.request.contextPath}/p_i8userformtest.html?appUserId=${UserManagementListAppUserId}&actionId=${retriveAction}">
						            <c:out value="${UserManagementListViewModel.username}"></c:out>
						        </a>
				        	</c:otherwise>
			        	</c:choose>
			        </c:if>
		        </authz:authorize>
		        <authz:authorize ifNotGranted="<%=updatePermission%>">
		            <c:out value="${UserManagementListViewModel.username}"></c:out>
		        </authz:authorize>
		    </ec:column>
		    <ec:column property="employeeId" title="Employee ID" filterable="true"/>
			<ec:column property="firstName" title="First Name" filterable="true"/>
			<ec:column property="lastName" title="Last Name" filterable="true"/>
		    <ec:column property="partnerGroupName" title="User Group" filterable="true">
		    	<authz:authorize ifAnyGranted="<%=userGroupUpdatePermission%>">
		    	<c:if test="${UserManagementListViewModel.partnerGroupId != 1031}" >
			        <c:choose>
		    			<c:when test="${restrictedUserPartnerGroup == UserManagementListViewModel.partnerGroupId}">
		    				<c:if test="${allowAccessOfCurrentUser}">
					        	 <a href="${contextPath}/p_partnergroupform.html?&partnerGroupId=${UserManagementListViewModel.partnerGroupId}">
						            ${UserManagementListViewModel.partnerGroupName}
						        </a>
				        	</c:if>
				        	<c:if test="${!allowAccessOfCurrentUser}">
						    	${UserManagementListViewModel.partnerGroupName}
				        	</c:if>
			        	</c:when>
			        	<c:otherwise>
		    				<a href="${contextPath}/p_partnergroupform.html?&partnerGroupId=${UserManagementListViewModel.partnerGroupId}">
					            ${UserManagementListViewModel.partnerGroupName}
					        </a>
			        	</c:otherwise>
		        	</c:choose>
			        </c:if>
				</authz:authorize>
		    	<authz:authorize ifNotGranted="<%=userGroupUpdatePermission%>">
		    		${UserManagementListViewModel.partnerGroupName}
		    	</authz:authorize>
		    </ec:column>
		    <ec:column property="accountStatus" title="Status" escapeAutoFormat="True" style="text-align: center"/>
			<ec:column alias="" title="" style="text-align: center" filterable="false" sortable="false" viewsAllowed="html">
			<authz:authorize ifAnyGranted="<%=updatePermission%>">
					<c:choose>
						<c:when test="${UserManagementListViewModel.appUserId == currentAppUserId}">
							<input type="button" value="Change Password" tabindex="47" onclick="javascript:document.location='changepasswordform.html';"  style="font-family:verdana, arial, helvetica, sans-serif;font-size: 10px" />&nbsp;
						</c:when>
						<c:otherwise>
							<c:choose>
				    			<c:when test="${restrictedUserPartnerGroup == UserManagementListViewModel.partnerGroupId}">
				    				<c:if test="${allowAccessOfCurrentUser}">
							        	 <c:if test="${UserManagementListViewModel.accountEnabled and UserManagementListViewModel.partnerGroupId != 1031}">
											<input type="button" id="rstPass${UserManagementListAppUserId}" name="rstPass${UserManagementListAppUserId}" value=" Reset Password  " class="button" onclick="openResetPasswordWindow('${UserManagementListAppUserId}','${UserManagementListViewModel.username}')"/>
										</c:if>
										<c:if test="${!UserManagementListViewModel.accountEnabled}">
											<input type="button" id="rstPass${UserManagementListAppUserId}" name="rstPass${UserManagementListAppUserId}" value=" Reset Password  " disabled="disabled" class="button"/>
										</c:if>
						        	</c:if>
						        	<c:if test="${!allowAccessOfCurrentUser}">
								    	<input type="button" id="rstPass${UserManagementListAppUserId}" name="rstPass${UserManagementListAppUserId}" value=" Reset Password  " disabled="disabled" class="button"/>
						        	</c:if>
					        	</c:when>
					        	<c:otherwise>
					        		<c:if test="${UserManagementListViewModel.partnerGroupId != 1031}">
					    				<c:if test="${UserManagementListViewModel.accountEnabled}">
											<input type="button" id="rstPass${UserManagementListAppUserId}" name="rstPass${UserManagementListAppUserId}" value=" Reset Password  " class="button" onclick="openResetPasswordWindow('${UserManagementListAppUserId}','${UserManagementListViewModel.username}')">
										</c:if>
										<c:if test="${!UserManagementListViewModel.accountEnabled}">
											<input type="button" id="rstPass${UserManagementListAppUserId}" name="rstPass${UserManagementListAppUserId}" value=" Reset Password  " disabled="disabled" class="button"/>
										</c:if>
									</c:if>
					        		<c:if test="${UserManagementListViewModel.partnerGroupId == 1031}">
					    				<c:if test="${UserManagementListViewModel.accountEnabled}">
											<input type="button" id="rstPass${UserManagementListAppUserId}" name="rstPass${UserManagementListAppUserId}" value=" Reset Password  " disabled="disabled" class="button" onclick="openResetPasswordWindow('${UserManagementListAppUserId}','${UserManagementListViewModel.username}')">
										</c:if>
										<c:if test="${!UserManagementListViewModel.accountEnabled}">
											<input type="button" id="rstPass${UserManagementListAppUserId}" name="rstPass${UserManagementListAppUserId}" value=" Reset Password  " disabled="disabled" class="button"/>
										</c:if>
					        		</c:if>	
					        	</c:otherwise>
				        	</c:choose>
						</c:otherwise>
					</c:choose>
			</authz:authorize>
			<authz:authorize ifNotGranted="<%=updatePermission%>">
					<input type="button" id="rstPass${UserManagementListAppUserId}" name="rstPass${UserManagementListAppUserId}" value=" Reset Password  " class="button" disabled="disabled">
	 	    </authz:authorize>
	 	    </ec:column>
        </ec:row>
	</ec:table>
    
    
    
	
    <script language="javascript" type="text/javascript">
    function openResetPasswordWindow(appUserId,username)
	{
		var popupWidth = 550;
		var popupHeight = 350;
		var usecaseId = <%=PortalConstants.RESET_USER_PASSWORD_PORTAL_USECASE_ID%>;	       
		var popupLeft = (window.screen.width - popupWidth)/2;
		var popupTop = (window.screen.height - popupHeight)/2;		
		var url = 'p-resetportalpasswordbysmsemailform.html?appUserId='+appUserId+'&username='+username+'&usecaseId='+usecaseId+'&isAgent=false';
        newWindow=window.open(url,'Action Detail Window', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
	    if(window.focus) newWindow.focus();
	    return false;		
	}
	</script>
   
  </body>
</html>
