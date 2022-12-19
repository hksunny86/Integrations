<!--Author: Mohammad Shehzad Ashraf-->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<%@ page import="com.inov8.microbank.common.util.PortalConstants"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>
<meta name="decorator" content="decorator2">
	<style>
		.fake-link {
		    color: blue;
		    text-decoration: underline;
		    cursor: pointer;
			float: right;
			padding: 0px 1%;
		}
	</style>
	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
   	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
   	<script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>
      <script language="javascript" type="text/javascript">
      var jq = $.noConflict();
      jq(document).ready(
      	function($)
      	{
      		$( "#permissionList" ).accordion({heightStyle: "content", collapsible: true});
      	}
      );
      function adjust(obj)
		{		              
		    var id = obj.id;
			var index = id.indexOf('.');
			var refId = id.substring(0, index+1);
			var actId = id.substring(index+1);
			//alert("id: "+id+", refId: "+refId +", actId: "+actId);
			
			var createAllowed = "createAllowed";
			var readAllowed = "readAllowed";
			var updateAllowed = "updateAllowed";
			var deleteAllowed = "deleteAllowed";
		
			var createAllowedPermissionId = refId+createAllowed;
			var readAllowedPermissionId = refId+readAllowed;
			var updateAllowedPermissionId = refId+updateAllowed;
			var deleteAllowedPermissionId = refId+deleteAllowed;
			
			if(obj.checked)
			{
				if(actId == createAllowed)
				{	
					if(!document.getElementById(readAllowedPermissionId).disabled)
					{
						document.getElementById(readAllowedPermissionId).checked=true;
					}	
					if(!document.getElementById(updateAllowedPermissionId).disabled)
					{
						document.getElementById(updateAllowedPermissionId).checked=true;
					}	
					if(!document.getElementById(deleteAllowedPermissionId).disabled)
					{
						document.getElementById(deleteAllowedPermissionId).checked=true;
					}	
				}
				if(actId == updateAllowed)
				{
					if(!document.getElementById(readAllowedPermissionId).disabled)
					{
						document.getElementById(readAllowedPermissionId).checked=true;
					}			
				}
				if(actId == deleteAllowed)
				{
					if(!document.getElementById(readAllowedPermissionId).disabled)
					{
						document.getElementById(readAllowedPermissionId).checked=true;
					}
					if(!document.getElementById(updateAllowedPermissionId).disabled)
					{	
						document.getElementById(updateAllowedPermissionId).checked=true;
					}	
				}
			}else
			{
				if(actId == readAllowed)
				{
					if(!document.getElementById(createAllowedPermissionId).disabled)
					{
						document.getElementById(createAllowedPermissionId).checked=false;
					}	
					if(!document.getElementById(updateAllowedPermissionId).disabled)
					{
						document.getElementById(updateAllowedPermissionId).checked=false;
					}	
					if(!document.getElementById(deleteAllowedPermissionId).disabled)
					{
						document.getElementById(deleteAllowedPermissionId).checked=false;
					}	
				}
				if(actId == updateAllowed)
				{
					if(!document.getElementById(createAllowedPermissionId).disabled)
					{
						document.getElementById(createAllowedPermissionId).checked=false;
					}	
					if(!document.getElementById(deleteAllowedPermissionId).disabled)
					{
						document.getElementById(deleteAllowedPermissionId).checked=false;
					}			
				}
				if(actId == deleteAllowed)
				{
					if(!document.getElementById(createAllowedPermissionId).disabled)
					{
						document.getElementById(createAllowedPermissionId).checked=false;
					}	
				}
		
			}
			
		}
		
		function validateDescription(_description)
		{			
	        var descLength = _description.value.length;
	        var browser=navigator.appName;	        
	        if(browser=="Netscape") //-- check for Firefox.
	        {
		        var descArr = _description.value.toArray();	        
		        var count = 0;	        	        
		        
		        var i = 0;
		        while (i < descArr.length)
				{
					if(descArr[i] == '\n')
					{
						count = count + 1;
					}
									
					i=i+1;
				}
				descLength = descLength + count; 
				
			}
			        
	        if(descLength > 250)
	        {
	        	alert('Description can not be more than 250 characters.');
	        	_description.focus();
	        	return false;
	        }
	        return true;
		}
      

	    function error(request)
		{
		     	alert("An unknown error has occured. Please contact with the administrator for more details");
		}
		
		function loadPermissionList()
		{	
			var pId = $F('partnerId');
			if(pId == "")
			{
				pId = -1;
			}		
			
			var url = '${contextPath}/partnerTypeFormRefDataController.html';
			var pars = 'actionType=3&partnerId='+pId;
			var myAjax = new Ajax.Request(
				url, 
				{
					method: 'get', 
					parameters: pars,
					onFailure: error,
					onSuccess: handleResponseList
				});					
		}
	
		function handleResponseList(request)
		{
			var txt = request.responseText;
			jq("#permissionList").replaceWith( txt );
			jq( "#permissionList" ).accordion({heightStyle: "content", collapsible: true});
			jq("#permissionList > div").contents().filter(function(){return this.nodeType == 3;}).remove();
			jq( "#permissionList > div" ).each(function(){
				var name=jq(this).prev().text();
				jq(this).prepend("<span class=\"fake-link\" onclick=\"selectNone('"+name+"');\">Select None</span><span class=\"fake-link\" onclick=\"selectAll('"+name+"');\">Select All</span>");
			});
		}
     
     </script>  
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
				<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/table.css" type="text/css"/>
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <c:choose>
		<c:when test="${not empty param.partnerGroupId or not empty partnerGroupId}">
	      <meta name="title" content="User Group">
        </c:when>
        <c:otherwise>
	      <meta name="title" content="User Group"/>
      	</c:otherwise>
      </c:choose>
      
     	<%
		String createPermission = PortalConstants.MNG_USR_GRPS_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.ADMIN_GP_CREATE;
		
		String updatePermission = PortalConstants.MNG_USR_GRPS_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.ADMIN_GP_UPDATE;
		%>
</head>

   <body bgcolor="#ffffff"  >
   <%@include file="/common/ajax.jsp"%>
   
   		<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<div id="errorMsg" class ="errorMsg" style="display:none;"></div>
   
      <spring:bind path="partnerGroupListViewModel.*">
	  <c:if test="${not empty status.errorMessages}">
	    <div class="errorMsg">
	      <c:forEach var="error" items="${status.errorMessages}">
	        <c:out value="${error}" escapeXml="false"/>
	        <br/>
	      </c:forEach>
	    </div>
	  </c:if>
	</spring:bind>
	
	<c:if test="${not empty messages}">
	    <div class="infoMsg" id="successMessages">
	        <c:forEach var="msg" items="${messages}">
	            <c:out value="${msg}" escapeXml="false"/><br/>
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
	
	<html:form name="partnergroupform" commandName="partnerGroupListViewModel"  onsubmit="return onFormSubmit(this)"
         action="p_partnergroupform.html">
           <c:if test="${not empty param.partnerGroupId or not empty partnerGroupId}">
             <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>          
           </c:if>
           <c:if test="${not empty param.partnerGroupId }"> <input type="hidden" name="partnerGroupId" value="${param.partnerGroupId}" ></c:if>
           <c:if test="${not empty partnerGroupId}"><input type="hidden" name="partnerGroupId" value="${partnerGroupId}" ></c:if>
       
      <table width="100%" border="0" cellpadding="0" cellspacing="1">	          
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Group Name:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
                
				<html:input  path="name" tabindex="1" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaNumericWithSp(this,event)" />
				<c:if test="${not empty param.partnerGroupId or not empty partnerGroupId}">
		    		<input type="hidden" id="p_name" name="p_name" value="${partnerGroupListViewModel.name}" />
		    		<input type="hidden" id="p_appUserTypeId" name="p_appUserTypeId" value="${partnerGroupListViewModel.appUserTypeId}" />
		    		<input type="hidden" id="p_partnerId" name="p_partnerId" value="${partnerGroupListViewModel.partnerId}" />
		    		<input type="hidden" id="usecaseId" name="usecaseId" value = "<%=PortalConstants.USER_GROUP_UPDATE_USECASE_ID %>"/>
		    	</c:if>	
		    	<c:if test="${empty param.partnerGroupId and empty partnerGroupId}">
		    		<input type="hidden" id="usecaseId" name="usecaseId" value = "<%=PortalConstants.USER_GROUP_CREATE_USECASE_ID %>"/>
		    	</c:if>	
		    	
		    	<input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
           		<input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />	    	
      	                          
             </td>
           </tr>
	           <tr>
	             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Partner Type:</td>
	             <td align="left" bgcolor="FBFBFB">
				 <html:select path="appUserTypeId" cssClass="textBox" id="appUserTypeId" tabindex="2" disabled="${not empty param.partnerGroupId}">
	                 <html:options items="${appUserTypeModelList}" itemLabel="name" itemValue="appUserTypeId"/>
				 </html:select>
				 </td>
	           </tr>
	           
	           <tr>
	             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Partner:</td>
	             <td align="left" bgcolor="FBFBFB">
				 <html:select path="partnerId" cssClass="textBox" id="partnerId" tabindex="3" onchange="loadPermissionList();" disabled="${not empty param.partnerGroupId}">
	                 <html:options items="${partnerModelList}" itemLabel="name" itemValue="partnerId"/>
				 </html:select>
				 </td>
	           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000"></span>Email:</td>
             <td align="left">
                 
	                    <html:input  path="email"  tabindex="4" cssClass="textBox" maxlength="50" />
      	         
             </td>
           </tr>           

             <tr bgcolor="FBFBFB">
             <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000"></span>Description:</td>
             <td align="left">
                 
	                    <html:textarea  path="description"  tabindex="5" cssClass="textBox"  />
      	         
             </td>
           </tr>                                       


			<tr bgcolor="FBFBFB">
             <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000"></span>Active:</td>
             <td align="left">
                 
	                    <html:checkbox  path="active"  tabindex="6"    />
      	         
             </td>
           </tr>                
      </table>
			<c:set var="prevSectionId" value="" scope="page"/>
			<div class="eXtremeTable" id="permissionList">
			<c:forEach items="${partnerGroupListViewModel.userPermissionList}" var="up" varStatus="x">
				<c:set var="rowCssClass" value="odd"/>
				<c:if test="${x.count%2!=0}">
				    <c:set var="rowCssClass" value="even" scope="page"/>
				</c:if>
				<c:if test="${prevSectionId != up.userPermissionSectionId}">
					  <c:if test="${not empty prevSectionId}">
					  	</tbody>
						</table>
						</div>
					  </c:if>
					  <h4>
					  	<c:choose>
					  		<c:when test="${empty up.userPermissionSectionName}">
					  			Misc.
					  		</c:when>
					  		<c:otherwise>
					  			${up.userPermissionSectionName}
					  		</c:otherwise>
					  	</c:choose>
					  </h4>
					  <div>
						<span class="fake-link" onclick="selectNone('${up.userPermissionSectionName}');">Select None</span>
						<span class="fake-link" onclick="selectAll('${up.userPermissionSectionName}');">Select All</span>
				      <table id="permission_table" border="0" cellspacing="0" cellpadding="0" class="tableRegion" width="100%" >
						<thead>
							<tr>				
								<td id = "permissionName" class="tableHeader">Permission Name</td>
								<td id="create" class="tableHeader" >Create</td>
								<td id="read" class="tableHeader">Read</td>
								<td id="update" class="tableHeader">Update</td>
								<td id="delete" class="tableHeader">Delete</td>
							</tr>							
						</thead>			
						<tbody class="tableBody" >
				</c:if>
			    <tr class="${rowCssClass}"  onmouseover="this.className='highlight'"  onmouseout="this.className='${rowCssClass}'" >
					<td>
						<spring:bind path="partnerGroupListViewModel.userPermissionList[${x.index}].permissionId">									
							<input type="hidden"
								name="<c:out value="${status.expression}"/>"
								id="<c:out value="${status.expression}"/>"
								value="<c:out value="${status.value}"/>" />
						</spring:bind>
						
						<spring:bind path="partnerGroupListViewModel.userPermissionList[${x.index}].userPermissionSectionId">         
				         <input type="hidden"
				          name="<c:out value="${status.expression}"/>"
				          id="<c:out value="${status.expression}"/>"
				          value="<c:out value="${status.value}"/>" />
				        </spring:bind>
				     
				        <spring:bind path="partnerGroupListViewModel.userPermissionList[${x.index}].userPermissionSectionName">         
				         <input type="hidden"
				          name="<c:out value="${status.expression}"/>"
				          id="<c:out value="${status.expression}"/>"
				          value="<c:out value="${status.value}"/>" />
				        </spring:bind>
				        
				        <spring:bind path="partnerGroupListViewModel.userPermissionList[${x.index}].sequenceNo">         
				         <input type="hidden"
				          name="<c:out value="${status.expression}"/>"
				          id="<c:out value="${status.expression}"/>"
				          value="<c:out value="${status.value}"/>" />
				        </spring:bind>
						
						<spring:bind path="partnerGroupListViewModel.userPermissionList[${x.index}].permissionName">
					    	<c:out value="${status.value}"/>
					        <input type="hidden" name="<c:out value="${status.expression}"/>"
					        	value="<c:out value="${status.value}"/>" />
						</spring:bind>
						
					</td>
					<%--Setting disabled="disabled" for hidden fields so that such fields do not become part of http request--%>
					<td align="center" class="table-autosort">
								<spring:bind path="partnerGroupListViewModel.userPermissionList[${x.index}].createAllowed">
									<input type="hidden" 
										name="_<c:out value="${status.expression}"/>" disabled="disabled"/>
									<input type="checkbox"
										class="perm_chkbox_${up.userPermissionSectionName}"
										name="<c:out value="${status.expression}"/>"
										id="<c:out value="${status.expression}"/>"
										value="<c:out value="true"/>" 
										<c:if test="${status.value}">checked</c:if> <c:if test="${!up.createAvailable}">disabled</c:if>
										onclick="adjust(this);" />
								</spring:bind>
								<spring:bind path="partnerGroupListViewModel.userPermissionList[${x.index}].createAvailable">								
									<input type="hidden" name="<c:out value="${status.expression}"/>"
										value="<c:out value="${status.value}"/>"/>
								</spring:bind>
					</td>
					<td align="center" class="table-autosort">
								<spring:bind
									path="partnerGroupListViewModel.userPermissionList[${x.index}].readAllowed">
									<input type="hidden"
										name="_<c:out value="${status.expression}"/>" disabled="disabled"/>
									<input type="checkbox"
										name="<c:out value="${status.expression}"/>"
										class="perm_chkbox_${up.userPermissionSectionName}"
										id="<c:out value="${status.expression}"/>"
										value="<c:out value="true"/>"
										<c:if test="${status.value}">checked</c:if> <c:if test="${!up.readAvailable}">disabled</c:if>
										onclick="adjust(this);" />
								</spring:bind>
								<spring:bind path="partnerGroupListViewModel.userPermissionList[${x.index}].readAvailable">								
									<input type="hidden" name="<c:out value="${status.expression}"/>"
										value="<c:out value="${status.value}"/>"/>
								</spring:bind>
					</td>
					<td align="center" class="table-autosort">
								<spring:bind
									path="partnerGroupListViewModel.userPermissionList[${x.index}].updateAllowed">
									<input type="hidden"
										name="_<c:out value="${status.expression}"/>" disabled="disabled"/>
									<input type="checkbox"
										name="<c:out value="${status.expression}"/>"
										class="perm_chkbox_${up.userPermissionSectionName}"
										id="<c:out value="${status.expression}"/>"
										value="<c:out value="true"/>"
										<c:if test="${status.value}">checked</c:if> <c:if test="${!up.updateAvailable}">disabled</c:if>
										onclick="adjust(this);" />
								</spring:bind>
								<spring:bind path="partnerGroupListViewModel.userPermissionList[${x.index}].updateAvailable">								
									<input type="hidden" name="<c:out value="${status.expression}"/>"
										value="<c:out value="${status.value}"/>"/>
								</spring:bind>
					</td>
					<td align="center" class="table-autosort">
								<spring:bind
									path="partnerGroupListViewModel.userPermissionList[${x.index}].deleteAllowed">
									<input type="hidden"
										name="_<c:out value="${status.expression}"/>" disabled="disabled"/>
									<input type="checkbox"
										name="<c:out value="${status.expression}"/>"
										class="perm_chkbox_${up.userPermissionSectionName}"
										id="<c:out value="${status.expression}"/>"
										value="<c:out value="true"/>"
										<c:if test="${status.value}">checked</c:if>  <c:if test="${!up.deleteAvailable}">disabled</c:if>
										onclick="adjust(this);"/>
								</spring:bind>
								<spring:bind path="partnerGroupListViewModel.userPermissionList[${x.index}].deleteAvailable">								
									<input type="hidden" name="<c:out value="${status.expression}"/>"
										value="<c:out value="${status.value}"/>"/>
								</spring:bind>
					</td>
			    </tr>
			    <c:set var="prevSectionId" value="${up.userPermissionSectionId}" scope="page"/>
			</c:forEach>
			<c:if test="${not empty partnerGroupListViewModel.userPermissionList}">
				</tbody></table></div>
			</c:if>
		</div>
		<table border="0" cellspacing="0" cellpadding="0" class="table-autosort"  width="100%">
			<tr>
				<td align="center">				
				<c:choose>
					<c:when test="${not empty param.partnerGroupId or not empty partnerGroupId and empty param.isReadOnly}">
						<authz:authorize ifAnyGranted="<%=updatePermission%>">      
							<input type="button" name="save" value="Update" class="button" onclick="onFormSubmit(document.forms.partnergroupform);"/>
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=updatePermission%>">      
							<input type="button" name="save" value="Update" class="button" onclick="onFormSubmit(document.forms.partnergroupform);" disabled="disabled"/>
						</authz:authorize>
						<input type="button" name="cancel" value="Cancel" class="button" onclick="javascript:window.location='p_partnergroupmanagement.html'" />
					</c:when>
					<c:when test="${param.isReadOnly}">
						<input type="button" name=" Close " value=" Close " class="button" onclick="window.close();" />
					</c:when>
					<c:otherwise>
						<authz:authorize ifAnyGranted="<%=createPermission%>">      
							<input type="button" name="save" value="Save" class="button" onclick="onFormSubmit(document.forms.partnergroupform);"/>
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=createPermission%>">      
							<input type="button" name="save" value="Save" class="button" onclick="onFormSubmit(document.forms.partnergroupform);" disabled="disabled"/>
						</authz:authorize>
						<input type="button" name="cancel" value="Cancel" class="button" onclick="javascript:window.location='p_partnergroupmanagement.html'" />
					</c:otherwise>
				</c:choose>	
				</td>
			</tr>		
		</table>
	
      </html:form>  
      <ajax:select 
			source="appUserTypeId" target="partnerId"
			baseUrl="${contextPath}/partnerTypeFormRefDataController.html"
			parameters="appUserTypeId={appUserTypeId},actionType=1" 
			postFunction="loadPermissionList"
			errorFunction="error"/>
     
     <script language="javascript" type="text/javascript">

	  function error(request)
		{
		     	alert("An unknown error has occured. Please contact with the administrator for more details");
		}
		
      		
		
		function validateCheckBoxes()
		{
			var state=false;
    		
    		jq("input[type='checkbox']:checked").each(
		    	function() {
		    		
		    		if(this.name!='active')
    				{
    					state=true;
    				}
    			}
			);

    		return state;
		}			
       function onFormSubmit(theForm)
      {
          if(!isFormDataChanged())
          {
              return;
          }

	      	_form = theForm;
	      	
	      	_name = _form.name;
	      	_email = _form.email;
	      	_description = _form.description ;
	      	
	      	
	      	_name.value = trim(_name.value);
	      	_description.value = trim(_description.value);
	      	_email.value = trim(_email.value);
			
	 		if(_name.value == ''){
	 			alert('Please enter Group Name.');
	 			_name.focus();
	 			return;
	 		}	

	 		if(!_name.readOnly && !isAlphanumericWithSpace(_name)){
	 			alert('Please enter correct Group Name.');
	 			_name.focus();
	 			return;
	 		}	
		 		
	        if(_email.value != ''){   // check the format of the email address here TODO
	            if(!isEmail(_email)){
	                alert('Please enter correct Email.');
	                return;
	            }
	        }
	        
	        //-- description validation.
	        if(!validateDescription(_description))
	        {
	        	return false;
	        }
	
			if(!validateFormChar(theForm)){
	      		return false;
	      	}
			
			if(!validateCheckBoxes()){
				alert("Select one permission at least.");
				return false;
			}
			
	      	//submitting form
	        if (confirm('Are you sure you want to proceed?')==true)
	        {
	          _form.submit();
	        }
	        else
	        {
	          return false;
	        }
      }      
     
     
     <c:choose>
     <c:when test="${not empty param.partnerGroupId or not empty partnerGroupId}">
          function disableFields()
          {
              document.forms[0].name.readOnly  = true; 
              if(document.forms[0].appUserTypeId != undefined)
              {
	              document.forms[0].appUserTypeId.readOnly  = true;
	              document.forms[0].partnerId.readOnly  = true;
              }
          }
          disableFields();
          document.forms[0].email.focus();
        </c:when>
        <c:otherwise>
        	document.forms[0].name.focus();
        </c:otherwise>
        </c:choose>
        
        function selectAll(section) {
        	var chkboxes = document.getElementsByClassName('perm_chkbox_'+section);
		      for( var i=0; i < chkboxes.length; i++ ) { 
		      
		      	 if(chkboxes[i].checked == false && chkboxes[i].disabled == false){
		              chkboxes[i].checked = "checked";
		         }
		      }
 		}
 		
 		function selectNone(section) {
        	var chkboxes = document.getElementsByClassName('perm_chkbox_'+section);
		      for( var i=0; i < chkboxes.length; i++ ) { 
				if(chkboxes[i].checked == true && chkboxes[i].disabled == false) {
 		          	  chkboxes[i].checked = "";
 		         }
		      }
 		}
        
      </script>
      <v:javascript formName="mfsAccountModel" staticJavascript="false"/>
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"> </script>
   </body>
</html>





