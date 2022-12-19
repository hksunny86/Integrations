<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Jalil & haroon-->
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.model.AppUserModel'%>
<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="User Groups"/>
<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
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
			    scrollToTop(1000);
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
		createPermission +=	"," + PortalConstants.MNG_USR_GRPS_CREATE;

		String updatePermission =	PortalConstants.ADMIN_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.MNG_USR_GRPS_UPDATE;
	 %>
	 <%
	 AppUserModel appUser = (AppUserModel)UserUtils.getCurrentUser();
	 Long currentUserType=null;
	 
	 if(appUser != null)
	 {
	 	currentUserType = appUser.getAppUserTypeId();
	 }
	 
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
   <div align="right"><a href="p_partnergroupform.html?actionId=1" class="linktext"> Add User Group </a><br>&nbsp;&nbsp;</div>
</authz:authorize>
<table width="85%">
</table>
<ec:table items="partnerGroupModelList" var="partnerGroupModel" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" action="${pageContext.request.contextPath}/p_partnergroupmanagement.html">

<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
  <ec:exportXls fileName="User Groups.xls" tooltip="Export Excel"/>
</authz:authorize>
<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
	<ec:exportXlsx fileName="User Groups.xlsx" tooltip="Export Excel" />
</authz:authorize>
<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
  <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
		headerTitle="User Groups" fileName="userGroups.pdf" tooltip="Export PDF" />
</authz:authorize>
<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
	<ec:exportCsv fileName="User Groups.csv" tooltip="Export CSV"></ec:exportCsv>
</authz:authorize>
  <ec:row>
  	<ec:column property="partnerName"  title="Partner"/>
  	<ec:column property="appUserTypeName"  title="Partner Type" filterable="true"/>
    <ec:column property="name"  title="Name"/>   	      
    <ec:column property="email"  title="Email" />
 				
<%--	    <ec:column property="editable" title="Edit" alias=" " filterable="false" sortable="false" viewsDenied="xls" >   	 --%>
    	<ec:column alias="edit" width="25px" filterable="false" sortable="false" viewsDenied="xls" viewsAllowed="html" style="text-align:center;" >   	 
		  	<authz:authorize ifAnyGranted="<%=updatePermission%>">
			  	<input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
			  	<%if(currentUserType.longValue() == UserTypeConstantsInterface.BANK.longValue())
			  	{%>
					<c:choose>
		    			<c:when test="${restrictedUserPartnerGroup == partnerGroupModel.partnerGroupId}">
		    				<c:if test="${allowAccessOfCurrentUser}">
					        	 <input type="button" class="button" value="Edit" style="width='40px';" 
									onclick='window.open("${pageContext.request.contextPath}/p_partnergroupform.html?partnerGroupId=${partnerGroupModel.partnerGroupId}", "_self")' 
									<c:if test="${!partnerGroupModel.editable}">disabled="disabled"</c:if>/>
				        	</c:if>
				        	<c:if test="${!allowAccessOfCurrentUser}">
						    	<input type="button" class="button" value="Edit" style="width:40px;" disabled="disabled"/>
				        	</c:if>
			        	</c:when>
			        	<c:otherwise>
		    				<input type="button" class="button" value="Edit" style="width='40px';" 
								onclick='window.open("${pageContext.request.contextPath}/p_partnergroupform.html?partnerGroupId=${partnerGroupModel.partnerGroupId}", "_self")' 
								<c:if test="${!partnerGroupModel.editable}">disabled="disabled"</c:if>/>
			        	</c:otherwise>
		        	</c:choose>
			  	<%
			  	}
			  	else if (currentUserType.longValue() == UserTypeConstantsInterface.INOV8.longValue())
			  	{
			  	%>
				  	<c:if test="${partnerGroupModel.editable}">
				  		<c:choose>
			    			<c:when test="${restrictedUserPartnerGroup == partnerGroupModel.partnerGroupId}">
			    				<c:if test="${allowAccessOfCurrentUser}">
						        	<a href="${pageContext.request.contextPath}/p_partnergroupform.html?partnerGroupId=${partnerGroupModel.partnerGroupId}">
								  		Edit
								  	</a>
					        	</c:if>
					        	<c:if test="${!allowAccessOfCurrentUser}">
							    	Edit
					        	</c:if>
				        	</c:when>
				        	<c:otherwise>
			    				<a href="${pageContext.request.contextPath}/p_partnergroupform.html?partnerGroupId=${partnerGroupModel.partnerGroupId}">
						  			Edit
						  		</a>
				        	</c:otherwise>
			        	</c:choose>
				  	</c:if>
				  	<c:if test="${!partnerGroupModel.editable}">
				  		Edit
				  	</c:if>			  		
			  	<%
			  	}		  	
			  	%>
			</authz:authorize>           									
		  	<authz:authorize ifNotGranted="<%=updatePermission%>">   

			  	<%
			  	if(currentUserType.longValue() == UserTypeConstantsInterface.BANK.longValue())
			  	{%>
				   	 <input type="button" class="button" value="Edit" style="width='40px';" 
							onclick='window.open("${pageContext.request.contextPath}/p_partnergroupform.html?partnerGroupId=${partnerGroupModel.partnerGroupId}", "_self")' disabled="disabled"/>			  	
				<%
			  	}
			  	else if (currentUserType.longValue() == UserTypeConstantsInterface.INOV8.longValue())
			  	{
			  	%>
			  		Edit
			  	<%
			  	}		  	
			  	%>

			</authz:authorize>
	    </ec:column>  
	     <ec:column alias=" " filterable="false" sortable="false" viewsDenied="xls" viewsAllowed="html">
	<c:set var="partnerGroupId" value="${partnerGroupModel.partnerGroupId}"></c:set>
	  	
	  	<authz:authorize ifAnyGranted="<%=updatePermission%>">
	  	
	  	
	  	<c:choose>
   			<c:when test="${restrictedUserPartnerGroup == partnerGroupModel.partnerGroupId}">
   				<c:if test="${allowAccessOfCurrentUser}">
		        	 <%if(currentUserType.longValue() == UserTypeConstantsInterface.INOV8.longValue())
		  	{%>
			  	<c:choose>
		  		<c:when test="${!partnerGroupModel.editable}">
		  			<tags:activatedeactivate 
						id="${partnerGroupModel.partnerGroupId}" 
						model="com.inov8.microbank.common.model.PartnerGroupModel" 
						property="active"
						propertyValue="${partnerGroupModel.active}"
						callback="actdeact"
						error="defaultError"
						url="parternGroupActivateDeactivate.html"
						disabled="true"/>
		  		</c:when>
		  		<c:otherwise>
		  			<tags:activatedeactivate 
						id="${partnerGroupModel.partnerGroupId}" 
						model="com.inov8.microbank.common.model.PartnerGroupModel" 
						property="active"
						propertyValue="${partnerGroupModel.active}"
						callback="actdeact"
						url="parternGroupActivateDeactivate.html"
						error="defaultError"
						/>	  	
		  		</c:otherwise>
		  	</c:choose>				
		  	<%
		  	}
		  	else if (currentUserType.longValue() == UserTypeConstantsInterface.BANK.longValue())
		  	{
		  	%>  	
			  	<c:choose>
			  		<c:when test="${!partnerGroupModel.editable}">											
			  			
			  			<c:choose>
					  		<c:when test="${partnerGroupModel.active}">
								<input id="btn_${partnerGroupId}"
											type="button" class="button" style="width='90px';"  
											value="Deactivate" disabled="disabled" />
					  		</c:when>
					  		<c:otherwise>
								<input id="btn_${partnerGroupId}"
											type="button" class="button" style="width='90px';" 
											value="Activate" disabled="disabled" />
					  		</c:otherwise>
					  	</c:choose>	
					  	
			  		</c:when>
			  		<c:otherwise>
			  			<c:choose>
					  		<c:when test="${partnerGroupModel.active}">
								<input id="btn_${partnerGroupId}"
											type="button" class="button" style="width='90px';"
											value="Deactivate" />
					  		</c:when>
					  		<c:otherwise>
								<input id="btn_${partnerGroupId}"
											type="button" class="button" style="width='90px';"
											value="Activate" />
					  		</c:otherwise>
					  	</c:choose>	

			  		</c:otherwise>
			  	</c:choose>	
			  				  	
		  	<%
		  	}		  	
		  	%>
	        	</c:if>
	        	<c:if test="${!allowAccessOfCurrentUser}">
			    	<c:choose>
				  		<c:when test="${partnerGroupModel.active}">
							<input id="btn_${partnerGroupId}"
										type="button" class="button" style="width='90px';"
										value="Deactivate"  disabled="disabled"/>
				  		</c:when>
				  		<c:otherwise>
							<input id="btn_${partnerGroupId}"
										type="button" class="button" style="width='90px';"
										value="Activate"  disabled="disabled"/>
				  		</c:otherwise>
				  	</c:choose>	
	        	</c:if>
        	</c:when>
        	<c:otherwise>
					<%if(currentUserType.longValue() == UserTypeConstantsInterface.INOV8.longValue())
		  	{%>
			  	<c:choose>
		  		<c:when test="${!partnerGroupModel.editable}">
		  			<tags:activatedeactivate 
						id="${partnerGroupModel.partnerGroupId}" 
						model="com.inov8.microbank.common.model.PartnerGroupModel" 
						property="active"
						propertyValue="${partnerGroupModel.active}"
						callback="actdeact"
						error="defaultError"
						url="parternGroupActivateDeactivate.html"
						disabled="true"/>
		  		</c:when>
		  		<c:otherwise>
		  			<tags:activatedeactivate 
						id="${partnerGroupModel.partnerGroupId}" 
						model="com.inov8.microbank.common.model.PartnerGroupModel" 
						property="active"
						propertyValue="${partnerGroupModel.active}"
						callback="actdeact"
						url="parternGroupActivateDeactivate.html"
						error="defaultError"
						/>	  	
		  		</c:otherwise>
		  	</c:choose>				
		  	<%
		  	}
		  	else if (currentUserType.longValue() == UserTypeConstantsInterface.BANK.longValue())
		  	{
		  	%>  	
			  	<c:choose>
			  		<c:when test="${!partnerGroupModel.editable}">											
			  			
			  			<c:choose>
					  		<c:when test="${partnerGroupModel.active}">
								<input id="btn_${partnerGroupId}"
											type="button" class="button" style="width='90px';"  
											value="Deactivate" disabled="disabled" />
					  		</c:when>
					  		<c:otherwise>
								<input id="btn_${partnerGroupId}"
											type="button" class="button" style="width='90px';" 
											value="Activate" disabled="disabled" />
					  		</c:otherwise>
					  	</c:choose>	
					  	
			  		</c:when>
			  		<c:otherwise>
			  			<c:choose>
					  		<c:when test="${partnerGroupModel.active}">
								<input id="btn_${partnerGroupId}"
											type="button" class="button" style="width='90px';"
											value="Deactivate" />
					  		</c:when>
					  		<c:otherwise>
								<input id="btn_${partnerGroupId}"
											type="button" class="button" style="width='90px';"
											value="Activate" />
					  		</c:otherwise>
					  	</c:choose>	

			  		</c:otherwise>
			  	</c:choose>	
			  				  	
		  	<%
		  	}		  	
		  	%>
        	</c:otherwise>
		</c:choose>
		</authz:authorize>
		
	  	<authz:authorize ifNotGranted="<%=updatePermission%>">   				

			<%if(currentUserType.longValue() == UserTypeConstantsInterface.INOV8.longValue())
		  	{%>
				
			    <tags:activatedeactivate 
					id="${partnerGroupModel.partnerGroupId}" 
					model="com.inov8.microbank.common.model.PartnerGroupModel" 
					property="active"
					propertyValue="${partnerGroupModel.active}"
					callback="actdeact"
					url="parternGroupActivateDeactivate.html"
					error="defaultError"
					disabled="true"/>
		  	<%
		  	}
		  	else if (currentUserType.longValue() == UserTypeConstantsInterface.BANK.longValue())
		  	{
		  	%>  	  			
	  			<c:choose>
			  		<c:when test="${partnerGroupModel.active}">
						<input id="btn_${partnerGroupId}"
									type="button" class="button" 
									value="Deactivate" disabled="disabled" />
			  		</c:when>
			  		<c:otherwise>
						<input id="btn_${partnerGroupId}"
									type="button" class="button" 
									value="Activate" disabled="disabled" />
			  		</c:otherwise>
			  	</c:choose>	
			  				  	
		  	<%
		  	}		  	
		  	%>


	  	</authz:authorize>
	  	<%		  	
	  	if (currentUserType.longValue() == UserTypeConstantsInterface.BANK.longValue())
	  	{
	  	%>
	  	<input type="hidden" id="message" value=""/>	
	  	<ajax:updateField
						  baseUrl="${contextPath}/partnerTypeFormRefDataController.html"
						  source="btn_${partnerGroupId}"
						  target="btn_${partnerGroupId},message"
						  action="btn_${partnerGroupId}"
						  parameters="id=${partnerGroupId},actionType=5,btn=btn_${partnerGroupId}"
						  parser="new ResponseXmlParser()"
						  preFunction="initProgress"
						  postFunction="globalAjaxPostFunction" 
						  errorFunction="globalAjaxErrorFunction"
						  />
		  <%
		  }
		   %>				
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
