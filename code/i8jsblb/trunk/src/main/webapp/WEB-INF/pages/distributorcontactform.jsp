<!--Author: Jawwad Farooq-->

<%@ include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>




<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator2">



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="title" content="Agent Contact"/>
<script type="text/javascript">

var level 


function error(request)
{
     	alert("An unknown error has occured. Please contact with the administrator for more details");
}

function loadLevel()
{	

//var level = document.forms.distributorContactForm.distributorLevelId.value();

	var obj = new AjaxJspTag.Select("/i8Microbank/distributorcontactformrefdata.html", {
		parameters: "distributorId={distributorId},actionType=2",target: "distributorLevelId",
		postFunction: loadPartnerGroup ,
		source: "distributorId",errorFunction: error});

	obj.execute();

}

function loadPartnerGroup()
		{	
			var obj = new AjaxJspTag.Select("/i8Microbank/partnerTypeFormRefDataController.html", {
			parameters: "id={distributorId},appUserTypeId=4,actionType=2",target: "partnerGroupId",
			source: "distributorId",errorFunction: error});
			obj.execute();
		}

function clearManagingContact()
{
	document.forms.distributorContactForm.managingContactId.value='';
	document.forms.distributorContactForm.managingContactFirstName.value='';
}

function popupCallback(src,popupName,columnHashMap)
{

  if(src=="levelName")
  {
    document.forms.distributorContactForm.distributorLevelId.value = columnHashMap.get('PK');
    document.forms.distributorContactForm.levelName.value = columnHashMap.get('name');
  }
  if(src=="managingContactFirstName")
  {
    document.forms.distributorContactForm.managingContactId.value = columnHashMap.get('PK');
    document.forms.distributorContactForm.managingContactFirstName.value = columnHashMap.get('firstName')+' ' +columnHashMap.get('lastName');
  }
 if(src=="areaName")
  {
    document.forms.distributorContactForm.areaId.value = columnHashMap.get('PK');
    document.forms.distributorContactForm.areaName.value = columnHashMap.get('name');
    
    // Clear all other popups value
    document.forms.distributorContactForm.distributorId.value = '';
    document.forms.distributorContactForm.distributorName.value = '';
    
    document.forms.distributorContactForm.distributorLevelId.value = '';
    document.forms.distributorContactForm.levelName.value = '';
  }
}
function toggleLevel()
{
	
	 level = document.forms[0].distributorLevelId.value;
	
	if(document.forms[0].head.checked == true)
	{
		document.forms.distributorContactForm.distributorLevelId.value=level;
		document.forms.distributorContactForm.distributorLevelId.disabled = true;
		document.forms.distributorContactForm.distributorLevel.value=level;
		
	}
	else if (document.forms[0].head.checked == false)
	{
		
		document.forms.distributorContactForm.distributorLevelId.disabled = false;
	}
}

</script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
			<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

</head>
<body>
<%@include file="/common/ajax.jsp"%>

<spring:bind path="distributorContactModel.*">
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
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

<table width="100%"  border="0" cellpadding="0" cellspacing="1">
<form id="distributorContactForm" method="post" action="distributorcontactform.html" onsubmit="return onFormSubmit(this)">

<c:if test="${not empty param.distributorContactId}">
  <input type="hidden"  name="isUpdate" id="isUpdate" value="true"/>
  <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
  <spring:bind path="distributorContactModel.areaId">
	  <input type="hidden" name="${status.expression}" value="${status.value}" />
  </spring:bind>
  
</c:if>

<c:if test="${empty param.distributorContactId}">
					
	<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>" />
</c:if>
	<input type="hidden" name="<%=PortalConstants.KEY_USECASE_ID%>"	value="<%=PortalConstants.DISTRIBUTOR_CONTACT_FORM_USECASE_ID%>"/>
				

<spring:bind path="distributorContactModel.distributorContactId">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>



<spring:bind path="distributorContactModel.versionNo">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="distributorContactModel.appUserId">
    <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="distributorContactModel.distributorContactModelVersionNo">
    <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="distributorContactModel.appUserModelVersionNo">
    <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="distributorContactModel.managingContactId">
    <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

    <input type="hidden" name="distributor" value=""/>
<input type="hidden" name="distributorLevel" value=""/>




  <tr bgcolor="FBFBFB">
            <td colspan="2" align="center">&nbsp;</td>
    </tr>
         
    	          <tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Area:&nbsp;</td>
      <td align="left">
        <spring:bind path="distributorContactModel.areaId">
            <select name="${status.expression}" class="textBox" id="${status.expression}" class="textBox" <c:if test="${not empty param.distributorContactId}">disabled='disabled'</c:if> >              
              <c:forEach items="${areaModelList}" var="areaModelList">
                <option value="${areaModelList.areaId}"
                  <c:if test="${status.value == areaModelList.areaId}">selected="selected"</c:if> />
                  ${areaModelList.name}
                </option>
              </c:forEach>
            </select>

        </spring:bind>
      </td>
    </tr>
    
    			<tr bgcolor="#FBFBFB">
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Agent:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="distributorContactModel.distributorId">
					<c:if test="${not empty param.distributorContactId}">
                    <%-- 
					<input type="text" name="${status.expression}" readonly="readonly" class="textBox" value="${status.value}"/>
					--%>
					<select name="${status.expression}" class="textBox" id="${status.expression}" >
							        <c:forEach items="${distributorModelList}" var="distributorModel">
									<option value="${distributorModel.distributorId}"
										<c:if test="${status.value == distributorModel.distributorId}">selected="selected"</c:if>>
										${distributorModel.name}
									</option>
								</c:forEach>
						        </select>
					
					</c:if>
					<c:if test="${empty param.distributorContactId}">
					
						        <select name="${status.expression}" class="textBox" id="${status.expression}"  >
							        <c:forEach items="${distributorModelList}" var="distributorModel">
									<option value="${distributorModel.distributorId}"
										<c:if test="${status.value == distributorModel.distributorId}">selected="selected"</c:if>>
										${distributorModel.name}
									</option>
								</c:forEach>
						        </select>
			       </c:if>
					</spring:bind>
			</tr>
    			<tr bgcolor="#FBFBFB">
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Level:&nbsp;
				</td>
				<td align="left">
				
					<spring:bind path="distributorContactModel.distributorLevelId">
						        <select          
						         name="${status.expression}" class="textBox" id="${status.expression}">
							        <c:if test="${empty distributorLevelModelList}">
							        	<option value="" />
							        </c:if>								        
							       <c:if test="${not empty distributorLevelModelList}">		        	
							        <c:forEach items="${distributorLevelModelList}" var="distributorLevelModelList">
									<option value="${distributorLevelModelList.distributorLevelId}"
										<c:if test="${status.value == distributorLevelModelList.distributorLevelId}">selected="selected"</c:if>>
										${distributorLevelModelList.name}
									</option>
								</c:forEach>
								</c:if>
						        </select>
					</spring:bind>
			</tr>

           <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Managing Contact:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">
                <c:if test="${not empty distributorContactModel.managingContactName}">

                <input type="text" name="managingContactFirstName" readonly="readonly" class="textBox" value="${distributorContactModel.managingContactName}"/>
                <input name="contactLookupButton" type="button" value="-o" class="button" style="height: 17px;width: 17px;text-align: center" onClick="javascript:callLookup('managingContactFirstName','distributorContactPopup',400,200,'distributorContactPopup.distributorId='+ document.forms.distributorContactForm.distributorId.value+'&distributorContactPopup.accountEnabled=1')"/>
                </c:if>
                <c:if test="${empty distributorContactModel.managingContactName}">

                <input type="text" name="managingContactFirstName" readonly="readonly" class="textBox" value="${param.managingContactFirstName}"/>
                <input name="contactLookupButton" type="button" value="-o" class="button" style="height: 17px;width: 17px;text-align: center" onClick="javascript:callLookup('managingContactFirstName','distributorContactPopup',400,200,'distributorContactPopup.distributorId='+ document.forms.distributorContactForm.distributorId.value+'&distributorContactPopup.accountEnabled=1')"/>
                </c:if>
                <img title="Clear Managing Contact" onclick="clearManagingContact()"  style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" /> 
              </td>
          </tr>
          
             <tr bgcolor="#FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>First Name:&nbsp;</td>
            <td width="50%" align="left">
		    <spring:bind path="distributorContactModel.firstName">
			 <input type="text" name="${status.expression}" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)" class="textBox" maxlength="50"/></spring:bind>

            </td>
          </tr>
          
            <tr bgcolor="#FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Last Name:&nbsp;</td>
            <td width="50%" align="left">
		    <spring:bind path="distributorContactModel.lastName">
			        <input type="text" name="${status.expression}" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)" class="textBox" maxlength="50"/></spring:bind>

            </td>
          </tr>
          

  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>User Name:&nbsp;</td>
    <td><spring:bind path="distributorContactModel.username">
          
      <c:choose>
					<c:when test="${not empty errorCode}">
			        <input type="text" name="${status.expression}" class="textBox"
							maxlength="50" value="${status.value}"/>
			        </c:when>
			        <c:otherwise>
					<input type="text" name="${status.expression}" class="textBox"
							maxlength="50" value="${status.value}"<c:if test="${not empty param.distributorContactId}">readonly="readOnly"</c:if>/>
			      	</c:otherwise>
			      </c:choose>
      </spring:bind>
    </td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Password:&nbsp;</td>
    <td><spring:bind path="distributorContactModel.password">
      <input type="password" name="${status.expression}" class="textBox" maxlength="4000" value=""/>
      </spring:bind>
    </td>
  </tr>
  <tr>
  	<td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Confirm Password:&nbsp;</td>
    <td><input type="password" name="confirmPassword" class="textBox" maxlength="4000" value="${status.value}"/></td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Password Hint:&nbsp;</td>
    <td><spring:bind path="distributorContactModel.passwordHint">
      <input type="text" name="${status.expression}" class="textBox" maxlength="11" value="${status.value}"/>
      </spring:bind>
    </td>
  </tr>

<tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Partner Group:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">
						<spring:bind path="distributorContactModel.partnerGroupId">
							<select name="${status.expression}" class="textBox" id="${status.expression}">
								<c:forEach items="${partnerGroupModelList}"
									var="partnerGroupModelList">
									<option value="${partnerGroupModelList.partnerGroupId}"
										<c:if test="${status.value == partnerGroupModelList.partnerGroupId}">selected="selected"</c:if>>
										${partnerGroupModelList.name}
									</option>
								</c:forEach>
							</select>
					        ${status.errorMessage}
					      </spring:bind>
					</td>
				</tr>


  	<tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Connection Type:&nbsp;</td>
      <td align="left">
        <spring:bind path="distributorContactModel.mobileTypeId">

            <select name="${status.expression}" class="textBox">              
              <c:forEach items="${mobileTypeModelList}" var="mobileTypeModelList">
                <option value="${mobileTypeModelList.mobileTypeId}"
                  <c:if test="${status.value == mobileTypeModelList.mobileTypeId}">selected="selected"</c:if>/>
                  ${mobileTypeModelList.name}
                </option>
              </c:forEach>
            </select>

        </spring:bind>
      </td>
    </tr>

           
            <tr bgcolor="#FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Mobile No:&nbsp;</td>
            <td width="50%" align="left">
		    <spring:bind path="distributorContactModel.mobileNo">
			    <input type="text" name="${status.expression}"  value="${status.value}" onkeypress="return maskNumber(this,event)" class="textBox" maxlength="11"/></spring:bind>
		 </td>
          </tr>
          
            <tr bgcolor="FBFBFB">
            <td align="right" bgcolor="F3F3F3" class="formText" >E-Mail:&nbsp;</td>
            <td align="left">
		    <spring:bind path="distributorContactModel.email">
			    <input type="text" name="${status.expression}" class="textBox" value="${status.value}"  maxlength="50"/>

		    </spring:bind>
                </td>
    </tr>
    
    <tr bgcolor="#FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText" >Fax No:&nbsp;</td>
            <td align="left">

	    <spring:bind path="distributorContactModel.fax">
			    <input type="text" name="${status.expression}" onkeypress="return maskNumber(this,event)" class="textBox" value="${status.value}"  maxlength="11"/>

	    </spring:bind>
	    </td>
	    </tr>
	    
	      <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Date of Birth:&nbsp;</td>
    <td>
				    <spring:bind path="distributorContactModel.dob">
							<input type="text" name="${status.expression}" class="textBox" id="${status.expression}"
								maxlength="50" value="${status.value}" readonly="readonly" />
						</spring:bind>
						<img id="sDate" tabindex="4"  name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="sDate" tabindex="5" name="popcal" title="Clear Date" onclick="javascript:$('dob').value=''"  align="middle" style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" /> 
						
    
    </td>
  </tr>
  
    <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">NIC:&nbsp;</td>
    <td><spring:bind path="distributorContactModel.nic">
							<input type="text" name="${status.expression}" onkeypress="return maskNumber(this,event)" class="textBox"
								maxlength="13" value="${status.value}" />
						</spring:bind>
    </td>
  </tr>
  
  				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Mother Maiden Name:&nbsp;
					</td>
					<td>
						<spring:bind path="distributorContactModel.motherMaidenName">
							<input type="text" name="${status.expression}" onkeypress="return maskAlphaWithSp(this,event)" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlpha(this,event)" />
						</spring:bind>
					</td>
				</tr>
  
    
                     <tr bgcolor="#FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Address 1:&nbsp;</td>
            <td width="50%" align="left">
		    <spring:bind path="distributorContactModel.address1">
			        <input type="text" name="${status.expression}" value="${status.value}" class="textBox" onkeypress="return maskCommon(this,event)" maxlength="250"/>
		    </spring:bind>

            </td>
          </tr>
           <tr bgcolor="#FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Address2:&nbsp;</td>
            <td width="50%" align="left">
		    <spring:bind path="distributorContactModel.address2">
			        <input type="text" name="${status.expression}" value="${status.value}" class="textBox" onkeypress="return maskCommon(this,event)" maxlength="250"/>
		    </spring:bind>

            </td>
          </tr>

           <tr bgcolor="#FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">City:&nbsp;</td>
            <td width="50%" align="left">
		    <spring:bind path="distributorContactModel.city">
			        <input type="text" name="${status.expression}" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)" class="textBox" maxlength="50"/>
			           </spring:bind>

            </td>
          </tr>
           <tr bgcolor="#FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">State:&nbsp;</td>
            <td width="50%" align="left">
		    <spring:bind path="distributorContactModel.state">
			        <input type="text" name="${status.expression}" onkeypress="return maskAlphaWithSp(this,event)" value="${status.value}" class="textBox" maxlength="50"/>
			        </spring:bind>

            </td>
          </tr>
          
           <tr bgcolor="#FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Zip Code:&nbsp;</td>
            <td width="50%" align="left">
		    <spring:bind path="distributorContactModel.zip">
			        <input type="text" name="${status.expression}" value="${status.value}" onkeypress="return maskAlphaNumeric(this,event)"class="textBox" maxlength="10"/>
		    </spring:bind>

            </td>
          </tr>
          
           <tr bgcolor="#FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Country:&nbsp;</td>
            <td width="50%" align="left">
		    <spring:bind path="distributorContactModel.country">
			        <input type="text" name="${status.expression}" onkeypress="return maskAlphaWithSp(this,event)" value="${status.value}" class="textBox" maxlength="50"/>
		    </spring:bind>

            </td>
          </tr>

          
          		 
                  
		  
            <tr bgcolor="#FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText">Balance:&nbsp;</td>
            <td align="left">

	    <spring:bind path="distributorContactModel.balance">
	  <c:if test="${not empty param.distributorContactId}">
					<c:choose>
						<c:when test="${empty balance}">
								<input type="text" name="${status.expression}" class="textBox" value="0.0"  readonly="readonly" onkeypress="return maskNumber(this,event)" maxlength="14"/>	  		
						</c:when>
						<c:otherwise>
								<input type="text" name="${status.expression}" class="textBox" readonly="readonly" value="<c:out value="${balance}"></c:out>"  onkeypress="return maskNumber(this,event)" maxlength="14"/>  										
						</c:otherwise>
					</c:choose>
		
	  </c:if>
	  
	  <c:if test="${empty param.distributorContactId}">
		<input type="text" name="${status.expression}" class="textBox" value="0.0"  readonly="readonly" onkeypress="return maskNumber(this,event)" maxlength="14"/>	  
	  </c:if>
	   

			    

	    </spring:bind>


	    </td>
		  </tr>
		  

  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
    <td align="left" bgcolor="FBFBFB"><spring:bind path="distributorContactModel.description"><textarea name="${status.expression}" cols="50" rows="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox" type="_moz">${status.value}</textarea></spring:bind></td>
  </tr>
		  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
    <td align="left" bgcolor="FBFBFB"><spring:bind path="distributorContactModel.comments"><textarea name="${status.expression}" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" cols="50" rows="5" class="textBox" type="_moz">${status.value}</textarea></spring:bind></td>
  </tr>		
    
<tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Head:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="distributorContactModel.head">
	    <input name="${status.expression}" type="checkbox" value="true" 	
	    <c:if test="${status.value==true}">checked="checked"</c:if>	
	     />			    
     </spring:bind>
    </td>
  </tr>    




<tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Verified:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="distributorContactModel.verified">
	    <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>
				<c:if test="${empty param.distributorContactId && empty param._save }">checked="checked"</c:if> 
				<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>

	    />			    
     </spring:bind>
    </td>
  </tr>
          

      
      <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText">Account Enabled:&nbsp;</td>
            <td align="left" class="formText">
		    <spring:bind path="distributorContactModel.accountEnabled">
			    <input type="hidden" name="_${status.expression}"/>
			    <input name="${status.expression}" type="checkbox" value="true"
			    <c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${empty param.distributorContactId && empty param._save }">checked="checked"</c:if> 
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
 />
		    </spring:bind>
			</td>
      </tr>
      
      <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText">Account Expired:&nbsp;</td>
            <td align="left" class="formText">
		    <spring:bind path="distributorContactModel.accountExpired">
			    <input name="${status.expression}" type="checkbox" value="true" 	
			    								<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${empty param.customerId}">unchecked="checked"</c:if>
/>			    
		    </spring:bind>
			</td>
      </tr>
          
      <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText">Account Locked:&nbsp;</td>
            <td align="left" class="formText">
		    <spring:bind path="distributorContactModel.accountLocked">
			    <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
			    />			    
		    </spring:bind>
			</td>
      </tr>    
      
      <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText">Credentials Expired:&nbsp;</td>
            <td align="left" class="formText">
		    <spring:bind path="distributorContactModel.credentialsExpired">
			    <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
			    />			    
		    </spring:bind>
			</td>
      </tr>

	  <tr bgcolor="FBFBFB">
		  <td colspan="2" align="center">&nbsp;</td>
		  <tr bgcolor="FBFBFB">
			  <td colspan="2" align="center">

<input type= "button" name = "_save" value="  Save  " onclick="javascript:onSave(document.forms.distributorContactForm,null);" class="button"/>		  
 <c:if test="${empty param.distributorContactId}">
<input type= "button" name = "_cancel" value=" Cancel " onclick="javascript:revertDropdowns(document.forms.distributorContactForm);" class="button"/>
</c:if>
 <c:if test="${not empty param.distributorContactId}">
<input type= "button" name = "_cancel" value=" Cancel "  class="button" onclick="javascript:revertDropdownsLevel(document.forms.distributorContactForm);"/>
</c:if>
			  </td>
                  </tr>
</table>
</form>

		<ajax:select source="areaId" target="distributorId" 
			baseUrl="${contextPath}/distributorcontactformrefdata.html"
			parameters="areaId={areaId},actionType=1" errorFunction="error"  postFunction="loadLevel"/>
			 
		<ajax:select source="distributorId" target="distributorLevelId"
			baseUrl="${contextPath}/distributorcontactformrefdata.html"
			parameters="distributorId={distributorId},actionType=2" errorFunction="error"/>
			
			<ajax:select source="distributorId" target="partnerGroupId"
			baseUrl="${contextPath}/partnerTypeFormRefDataController.html"
			parameters="id={distributorId},appUserTypeId=4,actionType=2"  errorFunction="error" />
			
			 
			
				<ajax:updateField 
			  		source="distributorId"
					eventType="click"
					action="head"
					
					target="head,distributorLevelId"	
					parser="new ResponseXmlParser()"
					postFunction="toggleLevel"
					baseUrl="${contextPath}/distributorcontactformrefdata.html"
					parameters="distributorId={distributorId},actionType=5,isHead={head}"/>
			
			
			
			
			
			

<script type="text/javascript">

  
 
      function isDateGreaterOrEqualForCard(from, to) {

	var fromDate;
	var toDate;

	var result = false;

//  format dd/mm/yyyy
	fromDate = from.substring(6,10) + from.substring(3,5) + from.substring(0,2);
	toDate = to.substring(6,10)   + to.substring(3,5) + to.substring(0,2);
	if( fromDate != toDate && fromDate > toDate ) {
		result = true;
	}

	return result;
}

<c:if test="${not empty param.distributorContactId}">
var selectedIndexDistributorId = document.forms[0].distributorId.selectedIndex;
var selectedIndexDistributorLevelId = document.forms[0].distributorLevelId.selectedIndex;
function revertDropdownsLevel(theForm)
{

theForm.reset();
	var areaId = <c:out value="${distributorContactModel.areaId}"/>;
	var distributorId = <c:out value="${distributorContactModel.distributorId}"/>;
	var distributorLevelId = <c:out value="${distributorContactModel.distributorLevelId}"/>;
	//var selectedIndexDistributorId = document.forms[0].distributorId.selectedIndex;
	//var selectedIndexDistributorLevelId = document.forms[0].distributorLevelId.selectedIndex;
	//alert('area id is: '+areaId+', distributorId: '+distributorId+', distributorLevelId: '+distributorLevelId);
	
	/* var obj = new AjaxJspTag.Select(
        "/i8Microbank/distributorcontactformrefdata.html", {
        parameters: "areaId="+areaId+",actionType=1",
        postFunction: loadLevel,
        target: "distributorId",
        source: "areaId",
        errorFunction: error
        });
	obj.execute();*/
	
	obj1 = new AjaxJspTag.Select(
	"/i8Microbank/distributorcontactformrefdata.html", {
	parameters: "distributorId="+distributorId+",actionType=2",

	target: "distributorLevelId",
	source: "distributorId",
	errorFunction: error
	});
	obj1.execute();
	
	//alert("theForm.distributorId.selected = "+theForm.distributorId.selected);
    //theForm.distributorId.selected = distributorId;
    //alert('after executing');
    //alert('selectedIndexDistributorId: '+selectedIndexDistributorId+'selectedIndexDistributorLevelId: '+selectedIndexDistributorLevelId);
    
    var length = document.forms[0].distributorId.options.length;
    var e_distributorId = document.forms[0].distributorId;
    for(var i=0 ; i<length ; i++){
        if(e_distributorId[i].value == distributorId){
            e_distributorId[i].selected = true;
            break;
        }
    }
    length = document.forms[0].distributorLevelId.options.length;
    var e_distributorLevelId = document.forms[0].distributorLevelId;
    for(var i=0 ; i<length ; i++){
        if(e_distributorLevelId[i].value == distributorLevelId){
            e_distributorLevelId[i].selected = true;
            break;
        }
    }
    
    //document.forms[0].distributorId[selectedIndexDistributorId].selected = true;
    //document.forms[0].distributorLevelId[selectedIndexDistributorLevelId].selected = true;
}
</c:if>

function revertDropdowns(theForm)
{

		
	theForm.reset();
	var obj = new AjaxJspTag.Select(
"/i8Microbank/distributorcontactformrefdata.html", {
parameters: "areaId={areaId},actionType=1",
postFunction: loadLevel,
target: "distributorId",
source: "areaId",
errorFunction: error
});
	obj.execute();
	
	 obj1 = new AjaxJspTag.Select(
	"/i8Microbank/distributorcontactformrefdata.html", {
	parameters: "distributorId={distributorId},actionType=2",
	target: "distributorLevelId",
	source: "distributorId",
	errorFunction: error
	});
	obj1.execute();
	

}


function onFormSubmit(theForm) 
{	
   /*if(!validateFormChar(theForm)){
      return false;
   }*/
	 var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
   if( trim(theForm.dob.value) != '' && isDateGreater(theForm.dob.value,currServerDate)){
			alert('Future date of birth is not allowed.');
			document.getElementById('sDate').focus();
			return false;
		}
	
	
	if( document.forms.distributorContactForm.isUpdate != null && document.forms.distributorContactForm.isUpdate.value == 'true' )
  	{
	
		if( document.forms.distributorContactForm.password.value == document.forms.distributorContactForm.confirmPassword.value )
		{
			if( document.forms.distributorContactForm.managingContactId.value != '' && 
  		 document.forms.distributorContactForm.head.checked )
  		 {
  		 	alert("Contact under a Manager cannot be a Head ");
  		 	return false;
  		 }
  		 else
			return validateDistributorContactModel(document.forms.distributorContactForm);
	 	}	 
		alert("Password and Confirm Password do not match");
		document.forms[0].password.focus();
	    return false;
	}
	
  	else if( document.forms.distributorContactForm.password.value == document.forms.distributorContactForm.confirmPassword.value )
	{
		if( document.forms.distributorContactForm.password.value == '' )
	    {
		   alert("Password and Confirm Password is required");
		   document.forms.distributorContactForm.password.focus();
		   return false;
	    }
		if( document.forms.distributorContactForm.managingContactId.value != '' && 
  		   document.forms.distributorContactForm.head.checked )
  		{
  		 	alert("Contact under a Manager cannot be a Head ");
  		}
  		else{
		    return validateDistributorContactModel(theForm);
		}
 	}	 
 	else
 	{
		alert("Password and Confirm Password do not match");
		document.forms.distributorContactForm.password.focus();
	}
  	
    return false;	
}

Calendar.setup(
      {
        inputField  : "dob", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "sDate"    // id of the button
      }
      );

</script>

<v:javascript formName="distributorContactModel" staticJavascript="false"/>
<script type="text/javascript" src="<c:url
value="/scripts/validator.jsp"/>"></script>


</body>
</html>

