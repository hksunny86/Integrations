<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*,com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>  
<meta name="decorator" content="decorator">

   <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
      
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
       
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

     <meta name="title" content="Complaint Detail"/>

	<script language="javascript" type="text/javascript">
		function showHideAssignee(){
			var combo = document.getElementById('complaintModel.status');
			var selectedCategory = combo.options[combo.selectedIndex].value;
			if(selectedCategory == 'Escalate'){
				document.getElementById('assigneeDiv').style.display = 'inline';
			}else{
				document.getElementById('assigneeDiv').style.display = 'none';
			}
			return true;
		}
	</script>
	<%
		String updateComplaintPermission = PortalConstants.PG_GP_UPDATE +","+ PortalConstants.UPDATE_COMPLAINT_UPDATE; 
	%>
</head>


   <body bgcolor="#ffffff">
		<%@include file="/common/ajax.jsp"%>
   
      <spring:bind path="complaintDetailVO.*">
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


	<table width="1000px" border="0" cellpadding="2" cellspacing="1">
	  <tr bgcolor="FBFBFB">
	    <td width="20%" align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">Complaint ID :</td>
	    <td width="35%" align="left" valign="top">${complaintDetailVO.complaintModel.complaintCode}</td>
	    <td width="50%" rowspan="9" valign="top">
		<table width="100%" border="0" cellpadding="2" cellspacing="1">
		  
		      <c:forEach items="${complaintDetailVO.complaintParamValueMap}" var="complaintParamValueMap">
				  <tr bgcolor="FBFBFB">
				    <td width="50%" align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">${complaintParamValueMap.key} :</td>
				    <td align="left" valign="top">${complaintParamValueMap.value}</td>	
				  </tr>
			</c:forEach>
		  
		</table>  
		</td>
	  </tr>
	  <tr bgcolor="FBFBFB">
	    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">Created On :</td>
	    <td align="left" valign="top"><fmt:formatDate pattern="dd/MM/yyyy hh:mm a" value="${complaintDetailVO.complaintModel.createdOn}"/></td>
	  </tr>
	  
	  <c:if test="${not empty complaintDetailVO.complaintModel.expectedTat}">
	  <tr bgcolor="FBFBFB">
	    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">Expected TAT :</td>
	    <td align="left" valign="top"><fmt:formatDate pattern="dd/MM/yyyy hh:mm a" value="${complaintDetailVO.complaintModel.expectedTat}"/></td>
	  </tr>
	  </c:if>
	  
	  <tr bgcolor="FBFBFB">
	    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">Logged By :</td>
	    <td align="left" valign="top">${complaintDetailVO.loggedByName}</td>
	  </tr>
	  
	  <tr bgcolor="FBFBFB">
	    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">Name :</td>
	    <td align="left" valign="top">${complaintDetailVO.complaintModel.initiatorName}</td>
	  </tr>

       <c:if test="${complaintDetailVO.isCustomer || complaintDetailVO.isAgent}">
           <tr bgcolor="FBFBFB">
	         <td align="right" height="16" bgcolor="F3F3F3" class="formText">
	             <c:if test="${complaintDetailVO.isCustomer}">Customer ID:</c:if>
	             <c:if test="${complaintDetailVO.isAgent}">Agent ID:</c:if>
             </td>
             <td align="left">${complaintDetailVO.complaintModel.initiatorId}</td>
           </tr>
	   </c:if>
	  <tr bgcolor="FBFBFB">
	    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">MSISDN :</td>
	    <td align="left" valign="top">${complaintDetailVO.complaintModel.mobileNo}</td>
	  </tr>
	  <tr bgcolor="FBFBFB">
	    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">CNIC :</td>
	    <td align="left" valign="top">${complaintDetailVO.complaintModel.initiatorCNIC}</td>
	  </tr>
	  
	  <c:if test="${not empty complaintDetailVO.complaintModel.initiatorCity}">
	  <tr bgcolor="FBFBFB">
	    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">City/Location :</td>
	    <td align="left" valign="top">${complaintDetailVO.complaintModel.initiatorCity}</td>
	  </tr>
	  </c:if>
	  
	  <tr bgcolor="FBFBFB">
	    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">Complaint Category :</td>
	    <td align="left" valign="top">${complaintDetailVO.complaintCategoryModel.name}</td>
	  </tr>
	  <c:if test="${not empty complaintDetailVO.complaintSubcategoryModel.name}">
	  <tr bgcolor="FBFBFB">
	    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">Complaint Nature :</td>
	    <td align="left" valign="top">${complaintDetailVO.complaintSubcategoryModel.name}</td>
	  </tr>
	  </c:if>
	  
	  <c:if test="${not empty complaintDetailVO.complaintModel.complaintDescription}">
	  <tr bgcolor="FBFBFB">
	    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">Description :</td>
	    <td align="left" valign="top">${complaintDetailVO.complaintModel.complaintDescription}</td>
	  </tr>
	  </c:if>
	  
	  <tr bgcolor="FBFBFB">
	    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">Complaint Status :</td>
	    <td align="left" valign="top">${complaintDetailVO.complaintModel.status}</td>
	  </tr> 
	  
	  <c:if test="${not empty complaintDetailVO.complaintSubcategoryModel.name}">
	  <tr bgcolor="FBFBFB">
	    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">Escalation Status :</td>
	    <td align="left" valign="top">${complaintDetailVO.complaintModel.escalationStatus}</td>
	  </tr>
	  </c:if>
	  
	  <c:if test="${not empty complaintDetailVO.complaintModel.remarks}">
	  <tr bgcolor="FBFBFB">
	    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top">Remarks :</td>
	    <td align="left" valign="top">${complaintDetailVO.complaintModel.remarks}
	    </td>
	  </tr>  
	  </c:if>
	
	<c:if test="${complaintDetailVO.isSameAssigneeUser}"> 
		<authz:authorize ifAnyGranted="<%=updateComplaintPermission%>">
			<html:form name="complaintDetailForm" commandName="complaintDetailVO" action="p_complaintDetailForm.html?actionId=3" onsubmit="return onFormSubmit(this)">	  
				<tr bgcolor="FBFBFB">
			    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top"><span style="color:#FF0000">*</span>Update Status :</td>
			    <td align="left" valign="top">
		        	<html:select path="complaintModel.status" cssClass="textBox" onchange="javascript:showHideAssignee()" >
						<html:option value="Resolved">Resolved</html:option>
						<html:option value="Declined">Declined</html:option>
						<c:if test="${not empty complaintDetailVO.escalationList}">
						<html:option value="Escalate">Escalate To</html:option>
						</c:if>
		 			</html:select>	    
		 			<div id="assigneeDiv" style="display:none">
		 				<html:select path="escalateTo" cssClass="textBox">
							<c:if test="${complaintDetailVO.escalationList != null}">
								<html:options items="${complaintDetailVO.escalationList}" itemValue="value" itemLabel="label" />
							</c:if>
						</html:select>
					</div>
			    </td>
			  </tr>	
			  <tr bgcolor="FBFBFB">
			    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top"><span style="color:#FF0000">*</span>Update Remarks :</td>
			    <td align="left" valign="top">
			    	<html:textarea path="complaintModel.remarks" rows="3" cssClass="textBox" cssStyle="overflow: auto; width: 163px; height: 102px" onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);" cols="50"/>
			    	<html:hidden path="complaintModel.complaintId" id="complaintId"/>
			    	<input type="hidden" name="actionId" id="actionId" value="2"/>
			    </td>
			  </tr>  
			  <tr bgcolor="FBFBFB">
			    <td align="right" height="16" bgcolor="F3F3F3" class="formText">&nbsp;</td>
			    <td align="left">
		             <input id="btnSubmit" type="submit" class="button" value=" Update Status" /> 
		             <input type="button" class="button" value="Back" onclick="javascript:window.history.back()"/>
			    </td>
				</tr>  
			</html:form>
		</authz:authorize>
		<authz:authorize ifNotGranted="<%=updateComplaintPermission%>">
			<html:form name="complaintDetailForm" commandName="complaintDetailVO"  onsubmit="return onFormSubmit(this)">	  
				<tr bgcolor="FBFBFB">
			    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top"><span style="color:#FF0000">*</span>Update Status :</td>
			    <td align="left" valign="top">
		        	<html:select path="complaintModel.status" cssClass="textBox" disabled="true" >
						<html:option value="Resolved">Resolved</html:option>
						<html:option value="Declined">Declined</html:option>				
						<c:if test="${not empty complaintDetailVO.escalationList}">
						<html:option value="Escalate">Escalate To</html:option>
						</c:if>
		 			</html:select>	    
		 			<div id="assigneeDiv" style="display:none">
		 				<html:select path="escalateTo" cssClass="textBox">
							<c:if test="${complaintDetailVO.escalationList != null}">
								<html:options items="${complaintDetailVO.escalationList}" itemValue="value" itemLabel="label" />
							</c:if>
						</html:select>
					</div>
			    </td>
			  </tr>	
			  <tr bgcolor="FBFBFB">
			    <td align="right" height="16" bgcolor="F3F3F3" class="formText" valign="top"><span style="color:#FF0000">*</span>Update Remarks :</td>
			    <td align="left" valign="top">
			    	<html:textarea path="complaintModel.remarks" rows="3" cssClass="textBox" cssStyle="overflow: auto; width: 163px; height: 102px" onkeypress="return maskCommon(this,event)"  disabled="true"  cols="50"/>
			    	
			    </td>
			  </tr>  
			  <tr bgcolor="FBFBFB">
			    <td align="right" height="16" bgcolor="F3F3F3" class="formText">&nbsp;</td>
			    <td align="left">
		             <input id="btnSubmit" type="submit" class="button" value=" Update Status" disabled="disabled" /> 
		             <input type="button" class="button" value="Back" onclick="javascript:window.history.back()"/>
			    </td>
				</tr>  
			</html:form>
		</authz:authorize>
	</c:if>
	</table>

			<c:if test="${not empty complaintDetailVO.complaintHistoryList}">
			<fieldset>
				<legend>
					<font color="#3333FF">&nbsp; Complaint History &nbsp;</font></legend>
					<table width="100%" border="0">
							
						<tr bgcolor="F3F3F3">
						    <td align="center"><b>Sr #</b></td>
						    <td><b>Assignee</b></td>
						    <td><b>Status</b></td>
							<td><b>Assigned Date</b></td>
							<td><b>Escalation Date</b></td>
							<td><b>Closed On</b></td>
							<td><b>Remarks</b></td>
						</tr>					
					    <c:forEach items="${complaintDetailVO.complaintHistoryList}" var="complaintHistoryModel" varStatus="i">
							<tr bgcolor="FBFBFB">
							    <td align="center">${i.count}</td>
								<td>${complaintHistoryModel.assignedName}</td>
								<td>${complaintHistoryModel.status}</td>
								<td><fmt:formatDate pattern="dd/MM/yyyy hh:mm a" value="${complaintHistoryModel.assignedOn}"/></td>
								<td><fmt:formatDate pattern="dd/MM/yyyy hh:mm a" value="${complaintHistoryModel.tatEndTime}"/></td>
								<td><c:if test="${complaintHistoryModel.status == 'Resolved' || complaintHistoryModel.status == 'Declined'}"><fmt:formatDate pattern="dd/MM/yyyy hh:mm a" value="${complaintDetailVO.complaintModel.closedOn}"/></c:if></td>
								<td>${complaintHistoryModel.remarks}</td>
							</tr>			
						</c:forEach>		
					</table>
		  </fieldset>   
		  </c:if>
    
      <script type="text/javascript" src='<c:url value="/scripts/validator.jsp"/>'> </script>
      <script language="javascript" type="text/javascript">
      function onFormSubmit(theForm){
      	if(submitForm(theForm)){
	    	document.getElementById('btnSubmit').disabled = true;
      		return true;
	    }else{
		    return false;
	    }
      }
	  function submitForm(theForm){
		if(document.getElementById('complaintModel.status').value == ''){
			alert('Update Status is required.');
			return false;
		}
    	if(document.getElementById('complaintModel.remarks').value == ''){
		    alert('Update Remarks is required.');
		    return false;
		}
    	return true;
      }

	  </script>
      
      
   </body>
</html>
