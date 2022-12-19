
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*,com.inov8.microbank.common.model.portal.concernmodule.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!-- Author: Mohammad Shehzad Ashraf -->

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>  
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
       
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css">
      <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

     <meta name="title" content=""/>
	<%

           	String allowedUpdatePermission = PortalConstants.MNG_MY_CNCRN_UPDATE;
           	allowedUpdatePermission += ",";             	             	             	             	
           	allowedUpdatePermission += PortalConstants.MNG_PRT_CNCRN_UPDATE; 	
           	allowedUpdatePermission += ",";
           	allowedUpdatePermission += PortalConstants.MNO_GP_UPDATE;
           	allowedUpdatePermission += ",";
           	allowedUpdatePermission += PortalConstants.PAS_GP_UPDATE;
           	allowedUpdatePermission += ",";             	             	             	             	
           	allowedUpdatePermission += PortalConstants.PRS_GP_UPDATE; 
           	allowedUpdatePermission += ",";             	             	             	             	
           	allowedUpdatePermission += PortalConstants.RET_GP_UPDATE; 
           	allowedUpdatePermission += ",";             	             	             	             	
           	allowedUpdatePermission += PortalConstants.CSR_GP_UPDATE; 
            %>
    
</head>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>
<c:set var="updateAction"><%=PortalConstants.ACTION_UPDATE %></c:set>
<c:set var="concernPage" >${param.concernPage}</c:set>
<c:set var="myConcern" ><%=ConcernsConstants.PAGE_MY_CONCERN%></c:set>
<c:set var="listConcern"  ><%=ConcernsConstants.PAGE_LIST_CONCERN%></c:set>



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

        <c:choose>
           <c:when test="${concernPage eq listConcern}">
		             <script>window.opener.location = 'p_listconcerns.html?actionId=2&_formSubmit=true'</script>
           </c:when>
           <c:otherwise>
                	<script>window.opener.location = 'p_myconcerns.html?actionId=2&_formSubmit=true'</script>
           </c:otherwise>
         </c:choose>
			<script>self.focus()</script>
			
	        <c:forEach var="msg" items="${messages}">
	            <c:out value="${msg}" escapeXml="false"/><br/>
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
		  
        <html:form name="concernDetailForm" commandName="concernListViewModel"
         action="p_concerndetailform.html?actionId=${updateAction}">
          
            <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
			<input type="hidden" name="localAction" id="localAction" />
			<input type="hidden" name="newComments"  />
            <input type="hidden" name="concernPage" value="${param.concernPage}"  /> 
             
             
			<html:hidden path="concernId" />
			<html:hidden path="initiatorPartnerId" />
			<html:hidden path="concernCategoryId" />
			<html:hidden path="concernStatusId" />
			<html:hidden path="concernPriorityId" />
			<html:hidden path="parentConcernId" />
			<html:hidden path="title" />
			<html:hidden path="comments" />
			
			 <c:set var="statusClose"><%=ConcernsConstants.STATUS_CLOSED%></c:set>
			 <c:set var="statusReplied"><%=ConcernsConstants.STATUS_REPLIED%></c:set>
			 <c:set var="statusVoid"><%=ConcernsConstants.STATUS_VOID%></c:set>	
			 <c:set var="statusOpen"><%=ConcernsConstants.STATUS_OPEN%></c:set>	
			 <c:set var="statusInProcess"><%=ConcernsConstants.STATUS_INPROCESS%></c:set>
			 <c:set var="statusRaisedAgain"><%=ConcernsConstants.STATUS_INPROCESS%></c:set>
			
 
 
 
 
 <div class="eXtremeTable">
  		<table class="tableRegion" width="100%">
	   		<tr>
				<td class="titleRow" style="width: 160px; font-weight: bold">Concern Code</td>
		   		<td colspan="3"> 
		   			${concernListViewModel.concernCode}
				</td>
			</tr>
			
	   		<tr>
				<td class="titleRow" style="width: 160px; font-weight: bold">Category</td>
		   		<td colspan="3"> 
		   			${concernListViewModel.concernCategoryName}
				</td>
			</tr>

	   		<tr>
				<td class="titleRow" style="width: 160px; font-weight: bold">Priority</td>
		   		<td colspan="3"> 
		   			${concernListViewModel.concernPriorityName}
				</td>
			</tr>

	   		<tr>
				<td class="titleRow" style="width: 160px; font-weight: bold">Comments</td>
		   		<td colspan="3"> 
		   			${concernListViewModel.comments}
				</td>
			</tr>
			
			<tr>
				<td class="titleRow" style="width: 160px; font-weight: bold">Title</td>
		   		<td colspan="3"> 
		   			${concernListViewModel.title}
				</td>
			</tr>			

	   		<tr>
 				<c:choose>
					<c:when test="${concernPage eq listConcern}"> 
			 
			            <td class="titleRow" style="width: 160px; font-weight: bold">Raised By</td>
			             <td colspan="3">
			             	${concernListViewModel.initiatorPartnerName}
					</c:when>
					<c:otherwise>
			            <td class="titleRow" style="width: 160px; font-weight: bold">Raised To</td>
			             <td colspan="3">
			             	${concernListViewModel.recipientPartnerName}
					</c:otherwise>
 				   </c:choose>	
				</td>
			</tr>

			<tr>
				<td width="160" class="tableHeader" style="width:125px;">Date</td>
				<td width="50" class="tableHeader" style="text-align: center; width:125px;">Status</td>
				<td width="88" class="tableHeader" align="center" >Comments By</td>
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
				   						ConcernHistoryListViewModel concernHist = (ConcernHistoryListViewModel)pageContext.getAttribute("concernHistoryModel");
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
										${concernHistoryModel.historyComments}
				   					</td>

				   				</tr>
			   				</c:forEach>




			
		</table>
 </div>
     
   </html:form>
      
<form action="" name="dummyform" method="get">
         <table width="100%" border="0" cellpadding="0" cellspacing="1">
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>New Comments:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
			   
			   
			   <c:choose>
					<c:when test="${ ( statusClose eq concernListViewModel.concernStatusId or 
					statusVoid eq concernListViewModel.concernStatusId or ( 
						(statusOpen eq concernListViewModel.concernStatusId or 
						statusRaisedAgain eq concernListViewModel.concernStatusId) and
						concernPage eq myConcern
					) ) or   ( statusReplied eq concernListViewModel.concernStatusId and concernPage eq listConcern  )   }">
						 <textarea name="newComments" class="textBox" style="width: 163px; height: 102px"  tabindex="2" rows="5" disabled="disabled"  
               onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,1000);" ></textarea>
					</c:when>
					<c:otherwise>
						<textarea name="newComments" class="textBox" style="width: 163px; height: 102px"  tabindex="2" rows="5"  
               onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,1000);" ></textarea>
					</c:otherwise>
				</c:choose>
			</td>
           </tr>

           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Action:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
             
				<c:choose>
					<c:when test="${ ( statusClose eq concernListViewModel.concernStatusId or 
					statusVoid eq concernListViewModel.concernStatusId or ( 
						(statusOpen eq concernListViewModel.concernStatusId or 
						statusRaisedAgain eq concernListViewModel.concernStatusId) and
						concernPage eq myConcern
					) ) or   ( statusReplied eq concernListViewModel.concernStatusId and concernPage eq listConcern  )   }">
						 <select name="localAction" class="textBox" tabindex="4" disabled="disabled" >
					</c:when>
					<c:otherwise>
						<select name="localAction" class="textBox" tabindex="4" >
					</c:otherwise>
				</c:choose>		


				<c:if test="${(statusOpen eq concernListViewModel.concernStatusId or
								statusRaisedAgain eq concernListViewModel.concernStatusId) and
								concernPage eq listConcern}"> 
        	             <option value="<%=ConcernsConstants.ACTION_REPLY%>"><%=ConcernsConstants.ACTION_REPLY%></option> 
				</c:if>
					
				<c:if test="${statusReplied eq concernListViewModel.concernStatusId and 
							concernPage eq myConcern }">	
	                     <option value="<%=ConcernsConstants.ACTION_RAISE_AGAIN%>"><%=ConcernsConstants.ACTION_RAISE_AGAIN%></option> 
		                 <option value="<%=ConcernsConstants.ACTION_RESOLVE%>"><%=ConcernsConstants.ACTION_RESOLVE%></option>
				</c:if>

				</select>
               
			</td>
           </tr>
         
		    <tr>
             <td height="16" colspan="2" align="right" bgcolor="FBFBFB" class="formText">&nbsp;</td>
           </tr>
		   
           <tr>
             <td height="16" align="right" bgcolor="FBFBFB" class="formText"></td>
             <td width="58%" align="left" bgcolor="FBFBFB">
             <c:set var="createAction"><%=PortalConstants.ACTION_CREATE%></c:set>	
             <c:set var="updateAction"><%=PortalConstants.ACTION_UPDATE%></c:set>	
             
  				
				<c:choose>
				<c:when test="${ ( statusClose eq concernListViewModel.concernStatusId or 
					statusVoid eq concernListViewModel.concernStatusId or ( 
						(statusOpen eq concernListViewModel.concernStatusId or 
						statusRaisedAgain eq concernListViewModel.concernStatusId) and
						concernPage eq myConcern
					) ) or ( statusReplied eq concernListViewModel.concernStatusId and concernPage eq listConcern  ) }">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" style="width='101px'" class="button" value=" Close Window " tabindex="4"  onclick="refreshParentWindow()" />	
					</c:when>
					<c:otherwise>	  		
	  			          <input type ="hidden" name="<%=PortalConstants.KEY_ACTION_ID %>" value="<%=PortalConstants.ACTION_UPDATE%>" />           
	  			          
	          			  <authz:authorize ifAnyGranted="<%=allowedUpdatePermission %>">
					  		    <input type="button" style="width='70px'" name="submitButton" id="submitButton" class="button" value="Submit" tabindex="4"  onclick="return onFormSubmit(document.concernDetailForm);" />
	          			  </authz:authorize>
	          			  <authz:authorize ifNotGranted="<%=allowedUpdatePermission %>">
								<input type="button" style="width='70px'" name="submitButton"  id="submitButton" class="button" value="Submit" tabindex="-1"  disabled="disabled" />
	          			  </authz:authorize>
	          			  
					</c:otherwise>
					</c:choose>		          
		
			 </td>
             </tr>

           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
     
        
        </table>
	</form>	
      
       <script language="javascript" type="text/javascript">


      //document.concernDetailForm.firstName.focus();
      
      
         
      function submitForm(theForm)
      {
      	var _form = document.concernDetailForm; 
      	var _dform = document.dummyform
    
      
		//city
 		if(_dform.newComments.value == '' ){
 			alert('New Comments are required.');
 			_dform.newComments.focus();
 			return false;
 		}
//        if(document.getElementById('localAction').value == '' ){
// 			alert('Action is required.');
// 			document.getElementById('localAction').focus();
// 			return false;
// 		}

      	if(!validateFormChar(_form)){
      		return false;
      	}		      

      	//submitting form
        if (confirm('Are you sure you want to proceed?')==true)
        {
           // here title is used to carry the value for localAction / Action
//          _form.title.value = _form.localAction.value;
			_form.title.value = _dform.localAction.value;
          _dform.submitButton.disabled = "disabled";

		
      	_form.newComments.value = _dform.newComments.value
      	_form.comments.value = _dform.newComments.value
 //     	_form.localAction.value = _dform.localAction.value			

          _form.submit();
          return true;
        }
        else
        {
          return false;
        }
  }
      
      function onFormSubmit(theForm)
      {
      	if(submitForm(theForm)){
      		return true;
	    }else{
		    return false;
	    }
      }
      function refreshParentWindow(){

		 window.close();         
      }
      </script>

      <v:javascript formName="mfsAccountModel" staticJavascript="false"/>
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"> </script>
   </body>
</html>