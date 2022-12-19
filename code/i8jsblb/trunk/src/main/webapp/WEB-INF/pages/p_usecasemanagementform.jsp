<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.inov8.microbank.common.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<meta name="decorator" content="decorator2">
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
		<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		<link rel="stylesheet" href="${contextPath}/styles/picklist/chosen.min.css" type="text/css">
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
	   	<script type="text/javascript" src="${contextPath}/scripts/picklist/chosen.jquery.min.js"></script>
	   	<script type="text/javascript" src="${contextPath}/scripts/picklist/chosen.proto.min.js"></script>
	   
	   	
	   	
<script language="javascript" type="text/javascript">
var jq = $.noConflict();
      jq(document).ready(
      	function($)
      	{
      		 $( "select[id^='levelcheckers']" ).each(function(){$(this).chosen({placeholder_text_multiple:"Click to Select Options"});});
      		 adjustLevels();
      	}
      );
 
     
</script> 	   	
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Usecase Management"/>
	</head>
	<body>
	
	<c:if test="${not empty messages}">
	    <div class="infoMsg" id="successMessages">
	        <c:forEach var="msg" items="${messages}">
	            <c:out value="${msg}" escapeXml="false"/><br />
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
	
	<table width="80%" border="0">
		<html:form name='usecaseManagementForm' commandName="usecaseModel" method="post" action="p_usecasemanagementform.html" onsubmit="return validateForm(this)" >
			 <tr>
	            <td align="right"  bgcolor="F3F3F3" class="formText" width="40%" ><span style="color:#FF0000">*</span>Usecase Name:&nbsp;</td>
	            <td align="left" bgcolor="FBFBFB" width="40%">
	           
						<html:input id="name"  path="name" tabindex="1" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaNumericWithSp(this,event)" readonly="true"/>	          	
				</td>
				<td align="right"  bgcolor="FBFBFB" width="20%"></td>
	          </tr>
	           <tr>
	            <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Escalation Levels:&nbsp;</td>
	            <td align="left" bgcolor="FBFBFB">
	           
				 <html:select path="escalationLevels" cssClass="textBox"  id="escalationLevels" tabindex="2" onchange="adjustLevels();">
	                  <html:option value="0">---SELECT---</html:option>
	                  <c:forEach begin="1" end="${maxLevel}" varStatus="loop">				
						    <html:option value="${loop.count}">${loop.count}</html:option>
					  </c:forEach>
				 </html:select>	          	
				</td>
				<td align="right"  bgcolor="FBFBFB" width="20%"></td>
			  </tr>	
			<tr>
	            <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Description:&nbsp;</td>
	            <td align="left" bgcolor="FBFBFB">
	           
						 <html:textarea  path="description"  tabindex="3" cssClass="textBox" cssStyle="width:163px; height: 102px" onkeypress="return maskAlphaNumericWithSp(this,event)" onkeyup="textAreaLengthCounter(this,250);" />
				</td>
				<td align="right"  bgcolor="FBFBFB" width="20%"></td>
	          </tr>
				
	      	<tr>
	            <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Comments:&nbsp;</td>
	            <td align="left" bgcolor="FBFBFB">
	           
						 <html:textarea  path="comments"  tabindex="4" cssClass="textBox" cssStyle="width:163px; height: 102px" onkeypress="return maskAlphaNumericWithSp(this,event)" onkeyup="textAreaLengthCounter(this,250);" />
				</td>
				<td align="right"  bgcolor="FBFBFB" width="20%"></td>
	          </tr>
	          
	          <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Active Authorization:</td>
             <td align="left"  bgcolor="FBFBFB">
	                    
	                 	<html:checkbox  path="isAuthorizationEnable" id="isAuthorizationEnable" tabindex="5" />	         
             </td>
             <td align="right"  bgcolor="FBFBFB" width="20%"></td>
           </tr> 
          
           <c:forEach  items="${usecaseModel.usecaseIdLevelModelList}" var="usecaseLevelModel" varStatus="iterationStatus">
          
           <tr id="level${iterationStatus.index}">
            	<td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Level ${iterationStatus.count} Checkers:</td>
            	<td align="Left"  bgcolor="FBFBFB">
           		<spring:bind path="usecaseModel.levelcheckers[${iterationStatus.index}]">			
           		<select id="levelcheckers${iterationStatus.index}" name="<c:out value="${status.expression}"/>" multiple="multiple">	      		
           			<c:forEach items="${usecaseLevelModel.usecaseLevelIdLevelCheckerModelList}" var="scl"  varStatus="status">  	
           		  		<option value="${scl.relationCheckerIdAppUserModel.appUserId}" selected="selected">${scl.relationCheckerIdAppUserModel.username}</option>
					</c:forEach>
					<c:forEach items="${checkersList}" var="cl"  varStatus="status">  
           		  		<option value="${cl.relationCheckerIdAppUserModel.appUserId}">${cl.relationCheckerIdAppUserModel.username}</option>
					</c:forEach>
					
				</select>	
				</spring:bind>
          	</td>
					
          	<td align="left" bgcolor="FBFBFB" width="20%" class="formText"><span style="color:#FF0000">*</span>Intimate Only:
				<html:checkbox   path="usecaseIdLevelModelList[${iterationStatus.index}].intimateOnly"   tabindex="5" />	 
            <td>
	
				<spring:bind path="usecaseModel.usecaseIdLevelModelList[${iterationStatus.index}].levelNo">								
					<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${iterationStatus.count}"/>" />
				</spring:bind>
				<spring:bind path="usecaseModel.usecaseIdLevelModelList[${iterationStatus.index}].usecaseId">								
					<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${usecaseModel.usecaseId}"/>" />
				</spring:bind>
		
			</td>      	
           </tr>
 
           </c:forEach> 
        
           <tr>
           <td>
           		<spring:bind path="usecaseModel.usecaseId">
		          <input type="hidden" name="${status.expression}" value="${status.value}"/>
		        </spring:bind>

		         <spring:bind path="usecaseModel.versionNo">
		          <input type="hidden" name="${status.expression}" value="${status.value}"/>
		        </spring:bind>
		
		         <spring:bind path="usecaseModel.createdBy">
		          <input type="hidden" name="${status.expression}" value="${status.value}"/>
		        </spring:bind>

	           <spring:bind path="usecaseModel.createdOn">
	          	<input type="hidden" name="${status.expression}" value="${status.value}"/>
	           </spring:bind>
    
           </td>
           
           </tr>
           
				<tr>
					<td class="formText" align="right" >

					</td>
					<td align="left">
						<input name="_save" type="submit" class="button" value="Save" tabindex="13"/> 
						<input name="reset" type="reset" 
							onclick="javascript: window.location='p_usecasemanagement.html'"
							class="button" value="Cancel" tabindex="14" />
					</td>
					<td class="formText" align="right">

					</td>
					<td align="left">
					</td>
				</tr>

				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
					value="<%=PortalConstants.ACTION_UPDATE%>">
				<input type="hidden" name="isManageUsecase" value="true">
				<input type="hidden" name="isParentUsecase" value="${isParentUsecase}">		
				<input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
           		<input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />

			</html:form>
		</table>
	
	<script language="javascript" type="text/javascript">
		function adjustLevels()
		{	        		
	      var escalationLevel = document.getElementById('escalationLevels').value;
	       
		  for(var i = 0 ; i <escalationLevel ; i++){    		
				document.getElementById("level"+i).style.display="table-row";
	     	}	
	      for(var i = escalationLevel ; i <${maxLevel} ; i++){ 		
				document.getElementById("level"+i).style.display="none";
	     	}
	     	
	      if(escalationLevel==0){
	      	document.getElementById("isAuthorizationEnable").checked=false;
	      	document.getElementById("isAuthorizationEnable").disabled=true;
	      	
	      }
	      else{
	      	document.getElementById("isAuthorizationEnable").disabled=false;
	      }	     	
		}
		
		
		function validateForm(form)
		{	        	
	     	var name= form.name.value;
	     	var escalationLevels= form.escalationLevels.value;
	     	var description = form.description.value;
	     	var comments = form.comments.value;
	     	var isAuthorizationEnable= form.isAuthorizationEnable.checked;
	     	
	     	var checkersLevel = [];
	     	
	     	var isValid = true;
	
	     	if(name==""){
	     		alert("Name cannot be Empty. Kindly refresh page")
	     		isValid = false;
	     	}else if(description==""){
	     		alert("Kindly provide Description.")
	     		isValid = false;
	     	}else if(comments==""){
	     		alert("Kindly provide Comments.")
	     		isValid = false;
	     	}else if((isAuthorizationEnable) && (escalationLevels<1)){
	     		alert("Escalation Levels must be greater than 0 to enable Authorization.")
	     		isValid = false;
	     	}
	     	
	     	/*code to check same checker at multiple levels*/
	     	else if(escalationLevels>0){
	     		for(var i = 0 ; i<escalationLevels;i++ ){
	     			var list=document.getElementById("levelcheckers"+i);
	     			if(list.value!=""){
	     			
		     			for(var j = 0; j < list.options.length; j++)
						   {
						      if(list.options[j].selected == true)
						      {
						      	
						      	if(i>0){
						      		for(var k=i-1; k>=0; k--){
						      			
										var match = checkersLevel[k].search(list.options[j].text);
										if(match!=-1){
											alert("Checker "+list.options[j].text+ " can exists either for Level "+(k+1)+" or Level "+(i+1)+".");
											isValid = false;
											return isValid;
										}				      		
						      		}
						      	}
						      	
						      	checkersLevel[i] = checkersLevel[i] +","+ list.options[j].text;
						      }
						   }
						
	     			}else{
	     				alert("Atleast 1 Checker must exist for Level "+(i+1)+".");
	     				isValid = false; 
	     				break;    			
	     			}
	     		}
	     	}
	     	
	     	return isValid;     	
		}
		
		
	</script>
	
	</body>
</html>
