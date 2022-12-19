<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*,com.inov8.microbank.common.model.portal.concernmodule.*"%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>
<c:set var="updateAction"><%=PortalConstants.ACTION_UPDATE %></c:set>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<!--Author: Mohammad Shehzad Ashraf-->

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>  
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
       
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css">
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

     <meta name="title" content=""/>

    <%
		String updatePermission = PortalConstants.CSR_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.CONCERNS_LIST_UPDATE;
	%>
</head>

   <body bgcolor="#ffffff" >
   
      <spring:bind path="concernListViewModel.*">
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
	    	<script>window.opener.location = 'p_opconcernsmanagement.html?actionId=2&_formSubmit=true'</script>
	        <c:forEach var="msg" items="${messages}">
	            <c:out value="${msg}" escapeXml="false"/><br/>
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
	  
        <html:form name="opConcernDetailForm" commandName="concernListViewModel"   action="p_opconcernsdetail.html?actionId=${updateAction}">
          
            <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
			
			<input type="hidden" name="paramRecipientPartnerId" value="${param.paramRecipientPartnerId}"  />
			<input type="hidden" name="paramInitiatorPartnerId" value="${param.paramInitiatorPartnerId}" />			
			
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"  />

			<html:hidden path="initiatorPartnerId" />
			<html:hidden path="concernCategoryId" />
			<html:hidden path="concernStatusId" />
			<html:hidden path="concernPriorityId" />
			<html:hidden path="parentConcernId" />
			<html:hidden path="concernId" />
			<html:hidden path="concernCode" />
			
			
			 <c:set var="statusClose"><%=ConcernsConstants.STATUS_CLOSED%></c:set>	
			 <c:set var="statusReplied"><%=ConcernsConstants.STATUS_REPLIED%></c:set>
			 
			 
			 <c:set var="attribCurrentUserPartnerId"><%=request.getAttribute("attribCurrentUserPartnerId")%></c:set>
			 <c:set var="paramRecipientPartnerId">${param.paramRecipientPartnerId}</c:set>
			 <c:set var="paramInitiatorPartnerId">${param.paramInitiatorPartnerId}</c:set>	
			
			 <input type="hidden" name="paramInitiatorPartnerId" value="${param.paramInitiatorPartnerId}">



<div class="eXtremeTable">
  		<table class="tableRegion" width="100%">
	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">Concern Code</td>
		   		<td colspan="4"> 
		   			${concernListViewModel.concernCode}
				</td>
			</tr>
			
	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">Category</td>
		   		<td> 
		   			${concernListViewModel.concernCategoryName}
				</td colspan="4">
			</tr>

	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">Priority</td>
		   		<td colspan="4"> 
		   			${concernListViewModel.concernPriorityName}
				</td>
			</tr>

	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">Raised By</td>
		   		<td colspan="4"> 
		   			${concernListViewModel.initiatorPartnerName}
				</td>
			</tr>

	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">Comments</td>
		   		<td colspan="4"> 
		   			${concernListViewModel.comments}
				</td>
			</tr>			

	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">Title</td>
		   		<td colspan="4"> 
		   			${concernListViewModel.title}
				</td>
			</tr>	
			
	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">Raised To</td>
		   		<td colspan="4"> 
					${concernListViewModel.recipientPartnerName}		   			
				</td>
			</tr>			
			
			
							<tr>
	   							<td width="185" class="tableHeader" style="width:125px;">Date</td>
			   					<td width="60" class="tableHeader" style="text-align: center; width:125px;">Status</td>
			   					<td width="88" class="tableHeader" style="text-align: center; width:125px;">Comments By</td>
			   					<td width="88" class="tableHeader" >Recipient</td>
			   					<td width="250" class="tableHeader" >Comments</td>
							</tr>		


							<c:forEach items="${concernHistoryList}" var="concernHistoryModel"  varStatus="x">

								<c:set var="rowCssClass" value="even"/>
				   				<c:if test="${x.count%2!=0}">
			   						<c:set var="rowCssClass" value="odd" scope="page"/>
		   						</c:if>
					   			<tr class="${rowCssClass}" onmouseover="this.className='highlight'"  onmouseout="this.className='${rowCssClass}'">
		   							<td style="width: 125px;">	
										
				   					<%
				   						ConcernOpHistoryListViewModel concernHist = (ConcernOpHistoryListViewModel)pageContext.getAttribute("concernHistoryModel");
				   						out.println(PortalDateUtils.formatDate(concernHist.getHistoryUpdatedOn(), "dd/MM/yyyy hh:mm a"));
				   					 %>										
										
										
				   					</td>
				   					<td style="text-align: center; width:125px;">
										${concernHistoryModel.recipientConcernStatusName}
				   					</td>
				   					<td style="text-align: center; width:125px;">
										${concernHistoryModel.commentBy}
				   					</td>
				   					<td style="text-align: center; width:125px;">
										${concernHistoryModel.commentByRecipient}
				   					</td>
				   					<td style="text-align: center; width:125px;">
										${concernHistoryModel.historyComments}
				   					</td>

				   				</tr>
			   			</c:forEach>
			
		</table>
</div>

</br>

      

      

         <table width="100%" border="0" cellpadding="0" cellspacing="1">
         
           <tr height="2" >
             <td height="2" align="right" > </td>
             <td height="2" width="58%" align="left" ></td>
           </tr>         

           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Title:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
              	<html:input  path="title"  tabindex="1" cssClass="textBox"  />
			</td>
           </tr>
         
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>New Comments:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
               <textarea name="newComments" class="textBox"  tabindex="2" rows="5" style="width:163px; height: 102px" 
               onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,1000);" ></textarea>
			</td>
           </tr>

           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Action:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
				
				<c:choose>
					<c:when test="${(statusClose eq concernListViewModel.concernStatusId or statusReplied eq concernListViewModel.concernStatusId) or (attribCurrentUserPartnerId ne paramRecipientPartnerId) }">
						 <select name="localAction" class="textBox" tabindex="-1" disabled="disabled" >
					</c:when>
					<c:otherwise>
						<select name="localAction" class="textBox" onchange="handleRaisedTo()" tabindex="4" >
					</c:otherwise>
				</c:choose>		
	                 
	                 <c:if test="${!((attribCurrentUserPartnerId eq paramRecipientPartnerId) and (statusReplied eq concernListViewModel.concernStatusId)) }">
	                 	<option value="<%=ConcernsConstants.ACTION_SEND_TO_CREATOR%>"><%=ConcernsConstants.ACTION_SEND_TO_CREATOR%></option> 
	                 	<c:set value="true" var="showClosed" />
	                 </c:if>
	                 
	                 <c:if test="${not empty concernPartnerList}">
	                	 <option value="<%=ConcernsConstants.ACTION_SEND_TO_RESOLVER%>"><%=ConcernsConstants.ACTION_SEND_TO_RESOLVER%></option>
						 <c:set value="true" var="showClosed" />
					</c:if>	
					
					<c:if test="${statusReplied eq activePartnerStatusId}">
						<option value="<%=ConcernsConstants.ACTION_RAISE_AGAIN%>"><%=ConcernsConstants.ACTION_RAISE_AGAIN%> for ${activePartnerName}</option>					
						<c:set value="true" var="showClosed" />
					</c:if>
				
				</select>
			</td>
           </tr>

           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Raised To:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
					<c:choose>
						<c:when test="${attribCurrentUserPartnerId ne concernListViewModel.initiatorPartnerId and
						   attribCurrentUserPartnerId ne concernListViewModel.recipientPartnerId }" >
	 						 <html:select path="recipientPartnerId"  cssClass="textBox" tabindex="-1"  disabled="true" >
	 							 <html:option value="${concernListViewModel.recipientPartnerId}">${concernListViewModel.recipientPartnerName}</html:option>
  	        	     		 </html:select>
						</c:when>
						<c:otherwise>
								<c:choose>
								<c:when test="${(statusClose eq concernListViewModel.concernStatusId or statusReplied eq concernListViewModel.concernStatusId) or (attribCurrentUserPartnerId ne paramRecipientPartnerId) }">
			 						 <html:select path="recipientPartnerId"  cssClass="textBox" tabindex="-1"  disabled="true" >
			 							 <html:options items="${concernPartnerList}" itemLabel="partnerName" itemValue="concernPartnerId"/>	
			        	     		</html:select>
								</c:when>
								<c:otherwise>
			 						 <html:select path="recipientPartnerId"  cssClass="textBox" tabindex="3"  >
			 						 	<html:options items="${concernPartnerList}" itemLabel="partnerName" itemValue="concernPartnerId"/>	
			        	     		</html:select>
								</c:otherwise>
								</c:choose>
						
						</c:otherwise>
					</c:choose>
			</td>
           </tr>
         
		    <tr>
             <td height="16" colspan="2" align="right" bgcolor="FBFBFB" class="formText">&nbsp;</td>
           </tr>
		   
           <tr>
             <td height="16" align="right" bgcolor="FBFBFB" class="formText">&nbsp;</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
  				
				<c:choose>
					<c:when test="${(statusClose eq concernListViewModel.concernStatusId or statusReplied eq concernListViewModel.concernStatusId) or (attribCurrentUserPartnerId ne paramRecipientPartnerId) or (empty showClosed) }">
  						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  						<input type="button" style="width='102px'" class="button" value=" Close Window " tabindex="5"  onclick="closeWindow()" />
  						<script>
  							document.opConcernDetailForm.newComments.disabled="true";
  							document.opConcernDetailForm.title.disabled="true";
  						</script>	
					</c:when>
					<c:otherwise>	  		
	  			          <input type ="hidden" name="<%=PortalConstants.KEY_ACTION_ID %>" value="<%=PortalConstants.ACTION_CREATE%>" />
	  			          <authz:authorize ifAnyGranted="<%=updatePermission%>">
	  			          	<input type="button" style="width='70px'" class="button" name="submitButton" value="Submit" tabindex="6"  onclick="submitForm()" />
	  			          </authz:authorize>
	  			          <authz:authorize ifNotGranted="<%=updatePermission%>">
	  			          	<input type="button" style="width='70px'" class="button" name="submitButton" value="Submit" tabindex="-1"  disabled="disabled" />
	  			          </authz:authorize>
					</c:otherwise>
					</c:choose>		          
		
			 </td>
             </tr>

           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
     
        
        </table>
		
      </html:form>		
      
   <script language="javascript" type="text/javascript">
      
      
      if(!document.opConcernDetailForm.newComments.disabled){
      		 document.opConcernDetailForm.newComments.focus(); 
      		 document.opConcernDetailForm.recipientPartnerId.disabled = "disabled";   
      		 document.opConcernDetailForm.title.disabled = "disabled";
      } 
      
      function handleRaisedTo(){
      	if(document.opConcernDetailForm.localAction.value == '<%=ConcernsConstants.ACTION_SEND_TO_RESOLVER%>'){
      		document.opConcernDetailForm.recipientPartnerId.disabled = false;
      		document.opConcernDetailForm.title.disabled = false;
      	}else{
      		document.opConcernDetailForm.recipientPartnerId.disabled = "disabled";
      		document.opConcernDetailForm.title.disabled = "disabled";
      	}
      }
      
      function submitForm()
      {
      	var _form = document.opConcernDetailForm; 

		if(trim(_form.title.value) == ''){
			alert('Title is the required field.');
			_form.title.focus();
			return;
		}
		
		if(trim(_form.newComments.value) == ''){
			alert('New Comments is the required field.');
			_form.newComments.focus();
			return;
		}
		
		if(_form.localAction.value == '<%=ConcernsConstants.ACTION_SEND_TO_RESOLVER%>' && 
			_form.recipientPartnerId.value == '' ){
			alert('There is no resolver to send.');
			_dform.localAction.focus();
			return;
		}
      	
      	if(_form.localAction.value == '<%=ConcernsConstants.ACTION_SEND_TO_CREATOR%>'){
      		_form.actionId.value = <%=PortalConstants.ACTION_UPDATE %>;
      	}
      	else if(_form.localAction.value == '<%=ConcernsConstants.ACTION_SEND_TO_RESOLVER%>'){
      		_form.actionId.value = <%=PortalConstants.ACTION_CREATE %>;
      	}
      	
      	_form.submitButton.disabled = "disabled";     	
      	_form.submit();
      	
  }
      
      function onFormSubmit(theForm)
      {
      
		return false;
      	if(submitForm(theForm)){
      		return true;
	    }else{
		    return false;
	    }
	    
      }
     
     
     
     function closeWindow(){
        	window.close();
     }
     
      </script>
   
   </body>
</html>





