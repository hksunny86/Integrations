
<!--Author: Fahad Tariq-->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>
<meta name="decorator" content="decorator2">
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      
            <c:if test="${empty stakeholderBankInfoModel.stakeholderBankInfoId or stakeholderBankInfoModel.stakeholderBankInfoId == null}">
               <meta name="title" content="Create Stakeholder G/L"/>
             </c:if>
             <c:if test="${not empty stakeholderBankInfoModel.stakeholderBankInfoId or stakeholderBankInfoModel.stakeholderBankInfoId != null}">
            	      <meta name="title" content="Edit Stakeholder G/L"/>
			 </c:if>

      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
      <script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/toolbar.js"></script>
   </head>

   <body bgcolor="#ffffff">

<spring:bind path="stakeholderBankInfoModel.*">
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


      <table width="100%" border="0" cellpadding="0" cellspacing="1">
		<html:form name="commissionStakeholderAccountsForm" commandName="stakeholderBankInfoModel" method="post" action="commissionstakeholderaccountsform.html" onsubmit="return onFormSubmit(this);">
	     
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
				value="<%=PortalConstants.ACTION_UPDATE%>" />
			<c:if test="${not empty param.stakeholderBankInfoId}">
				<input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
			</c:if>
			<input type="hidden" id="stakeholderBankInfoId" name="stakeholderBankInfoId" value="${stakeholderBankInfoModel.stakeholderBankInfoId}">
			
       <!--  <form method="post" action="<c:url value="/commissionstakeholderaccountsform.html"/>" id="commissionStakeholderAccountsForm"
        onsubmit="return onFormSubmit(this)"> -->





           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Account Type:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB">
					<spring:bind path="stakeholderBankInfoModel.cmshaccttypeIdCommissionShAcctsTypeModel.cmshacctstypeId">
						<select name="${status.expression}" class="textBox" tabindex="1" onchange="setFilerField(this)" id="cmshacctstypeId">
							<option value="">[Select]</option>
							<c:forEach items="${commissionShAcctsTypeModelList}" var="commissionShAcctsTypeModelList">
								<option value="${commissionShAcctsTypeModelList.cmshacctstypeId}"
										<c:if test="${status.value == commissionShAcctsTypeModelList.cmshacctstypeId}">selected="selected"</c:if>>
										${commissionShAcctsTypeModelList.name}
								</option>
							</c:forEach>
						</select>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>G/L Type:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB">
					<spring:bind path="stakeholderBankInfoModel.glTypeModel">
						<c:if test="${empty stakeholderBankInfoModel.stakeholderBankInfoId or stakeholderBankInfoModel.stakeholderBankInfoId == null}">

							<select name="${status.expression}" class="textBox" tabindex="2" id="glTypeModel">
								<option value="">[Select]</option>
								<c:forEach items="${glTypeModelList}" var="glType">
									<option value="${glType.id}"
											<c:if test="${status.value == glType.id}">selected="selected"</c:if>>
											${glType.name}
									</option>
								</c:forEach>
							</select>
						</c:if>

					</spring:bind>
					<spring:bind path="stakeholderBankInfoModel.glTypeModel">

						<c:if test="${not empty stakeholderBankInfoModel.stakeholderBankInfoId or stakeholderBankInfoModel.stakeholderBankInfoId != null}">
							<select name="${status.expression}" class="textBox" tabindex="2" id="glTypeId" disabled="disabled">
								<option value="">[Select]</option>
								<c:forEach items="${glTypeModelList}" var="glType">
									<option value="${glType.id}"
											<c:if test="${status.value == glType.id}">selected="selected"</c:if>>
											${glType.name}
									</option>
								</c:forEach>
							</select>


							<html:hidden path="glTypeModel" />
						</c:if>

					</spring:bind>

				</td>
			</tr>
			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Parent G/L:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB">
					<spring:bind path="stakeholderBankInfoModel.parentGlTypeModel">

										<c:if test="${empty stakeholderBankInfoModel.stakeholderBankInfoId or stakeholderBankInfoModel.stakeholderBankInfoId == null}">
					<select name="${status.expression}" class="textBox" tabindex="3" id="parentGlModel">
						<option value="">[Select]</option>
						<c:forEach items="${parentGLModelList}" var="parentGL">
							<option value="${parentGL.id}"
									<c:if test="${status.value == parentGL.id}">selected="selected"</c:if>>
									${parentGL.name}
							</option>
						</c:forEach>
					</select>
					</c:if>
					</spring:bind>



					<spring:bind path="stakeholderBankInfoModel.parentGlTypeModel">
						<c:if test="${not empty stakeholderBankInfoModel.stakeholderBankInfoId or stakeholderBankInfoModel.stakeholderBankInfoId != null}">
							<html:hidden path="parentGlTypeModel" />
							<select name="${status.expression}" class="textBox" tabindex="3" id="parentGlModel" disabled="disabled">
								<option value="">[Select]</option>
								<c:forEach items="${parentGLModelList}" var="parentGL">
									<option value="${parentGL.id}"
											<c:if test="${status.value == parentGL.id}">selected="selected"</c:if>>
											${parentGL.name}
									</option>
								</c:forEach>
							</select>
						</c:if>

					</spring:bind>

				</td>
			</tr>
          
           <tr>
           <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Commission Stakeholder:&nbsp;
				</td>
				<td  width="51%" align="left" bgcolor="FBFBFB">
			
			<c:choose>
				      <c:when test="${stakeholderBankInfoModel.stakeholderBankInfoId != NULL}">
				      	 
				      	 <spring:bind path="stakeholderBankInfoModel.commissionStakeholderIdCommissionStakeholderModel.name">
							 <input type="text" id="stakeHolderName" name="${status.expression}" value="${status.value}" readonly="readonly"
									class="textBox" maxlength="50" onkeypress="return maskCommon(this,event)" tabindex="4" />
						</spring:bind>
				      </c:when>
				      <c:otherwise>
				      		  <spring:bind path="stakeholderBankInfoModel.commissionStakeholderIdCommissionStakeholderModel.name">
				                   <input type="text" id="stakeHolderName" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50" onkeypress="return maskCommon(this,event)" tabindex="4" />
				              </spring:bind>
				      </c:otherwise>
					</c:choose>	
				<!-- 	
	          <spring:bind path="stakeholderBankInfoModel.commissionStakeholderIdCommissionStakeholderModel.name">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50" disabled="true" onkeypress="return maskCommon(this,event)" tabindex="3" />
              </spring:bind>
					
					<spring:bind path="stakeholderBankInfoModel.commissionStakeholderId">
						<select name="${status.expression}" class="textBox" tabindex="1"
							id="${status.expression}" >
							<c:if test="${empty commissionStakeholderModelList}">
								<option value="" />
							</c:if>
							<c:if test="${not empty commissionStakeholderModelList}">
							
							<c:forEach items="${commissionStakeholderModelList}" var="commissionStakeholderModelList">
								<option value="${commissionStakeholderModelList.commissionStakeholderId}"
									<c:if test="${status.value == commissionStakeholderModelList.commissionStakeholderId}">selected="selected"</c:if>>
									${commissionStakeholderModelList.name}
								</option>
							</c:forEach>
							
							</c:if>
						</select>
					</spring:bind>
					 -->
				</td>
           </tr>
           
       
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">
             	<span style="color:#FF0000">*</span>Account No:&nbsp;
             </td>
             <td align="left" bgcolor="FBFBFB">

	          <spring:bind path="stakeholderBankInfoModel.accountNo">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50" onkeypress="return maskInteger(this,event)" tabindex="5" />
              </spring:bind>
             </td>
           </tr>
           
                      <tr id="filerMarkingRow">
				<td height="50" align="right" bgcolor="F3F3F3" class="formText">
					Filer Marking:
				<td align="left" bgcolor="FBFBFB" class="formText" id="filerTd">
					<html:radiobuttons tabindex="7" items="${filerList}" itemLabel="label" itemValue="value" path="filer"/>
				</td>
			</tr>
           
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">
             	<span style="color:#FF0000">*</span>Account Title:&nbsp;
             </td>
             <td align="left" bgcolor="FBFBFB">

	          <spring:bind path="stakeholderBankInfoModel.name">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50" onkeypress="return maskCommon(this,event)" tabindex="6" />
              </spring:bind>
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">    
            <c:if test="${empty stakeholderBankInfoModel.stakeholderBankInfoId or stakeholderBankInfoModel.stakeholderBankInfoId == null}">
             	<input name="_save"  type="submit" class="button" tabindex="7" value="Save"/>
             </c:if>
             <c:if test="${not empty stakeholderBankInfoModel.stakeholderBankInfoId or stakeholderBankInfoModel.stakeholderBankInfoId != null}">
            	 <input name="_save"  type="submit" class="button" tabindex="7" value="Update"/>
			 </c:if>

				 <c:choose>
					 <c:when test="${ not empty stakeholderBankInfoModel.stakeholderBankInfoId or stakeholderBankInfoModel.stakeholderBankInfoId != NULL}">

						 <input name="reset" tabindex="8" type="reset" onclick="test()" class="button" value="Cancel" />
					 </c:when>
					 <c:otherwise>
						 <input name="reset" tabindex="8" type="reset" class="button" value="Cancel" />
					 </c:otherwise>
				 </c:choose>


			 

               </td>
           </tr>
           
        <!-- </form> -->
        </html:form>
      </table>
      <script language="javascript" type="text/javascript">

		  function test(){

			  var ask = window.confirm("Do You Want to leave page?");
			  if(ask)
			  location.href = "${contextPath}/commissionstakeholderaccountsmanagement.html";

		  }

      Form.focusFirstElement($('commissionStakeholderAccountsForm'));
      highlightFormElements();

	  function onFormSubmit(theForm)
	  {
		  if( document.commissionStakeholderAccountsForm.stakeholderBankInfoId.value == ''){

			  if (document.forms.commissionStakeholderAccountsForm.cmshacctstypeId.value==''){
				  alert("Account Type is a required field");
				  document.forms.commissionStakeholderAccountsForm.cmshacctstypeId.focus();
				  return false;
			  }

			  if (document.forms.commissionStakeholderAccountsForm.glTypeModel.value==''){
				  alert("G/L Type is a required field");
				  document.forms.commissionStakeholderAccountsForm.glTypeModel.focus();
				  return false;
			  }

			  if (document.forms.commissionStakeholderAccountsForm.parentGlModel.value==''){
				  alert("Parent G/L is a required field");
				  document.forms.commissionStakeholderAccountsForm.parentGlModel.focus();
				  return false;
			  }
			  if (document.forms.commissionStakeholderAccountsForm.stakeHolderName.value==''){
				  alert("Name is a required field");
				  document.forms.commissionStakeholderAccountsForm.stakeHolderName.focus();
				  return false;
			  }
			  if (document.forms.commissionStakeholderAccountsForm.name.value==''){
				  alert("Account Head is a required field");
				  document.forms.commissionStakeholderAccountsForm.name.focus();
				  return false;
			  }
			  if (document.forms.commissionStakeholderAccountsForm.accountNo.value==''){
				  alert("Account Number is a required field");
				  document.forms.commissionStakeholderAccountsForm.accountNo.focus();
				  return false;
			  }
			  if (document.forms.commissionStakeholderAccountsForm.accountNo.value.length < 5){
				  alert("Account Number cannot be less than 5 digits");
				  document.forms.commissionStakeholderAccountsForm.accountNo.focus();
				  return false;
			  }
		  }else{

			  if (document.forms.commissionStakeholderAccountsForm.cmshacctstypeId.value==''){
				  alert("Account Type is a required field");
				  document.forms.commissionStakeholderAccountsForm.cmshacctstypeId.focus();
				  return false;
			  }


			  if (document.forms.commissionStakeholderAccountsForm.name.value==''){
				  alert("Account Head is a required field");
				  document.forms.commissionStakeholderAccountsForm.name.focus();
				  return false;
			  }

			  if (document.forms.commissionStakeholderAccountsForm.accountNo.value==''){
				  alert("Account Number is a required field");
				  document.forms.commissionStakeholderAccountsForm.accountNo.focus();
				  return false;
			  }
			  if (document.forms.commissionStakeholderAccountsForm.accountNo.value.length < 5){
				  alert("Account Number cannot be less than 5 digits");
				  document.forms.commissionStakeholderAccountsForm.accountNo.focus();
				  return false;
			  }
		  }

		  if (confirm("Are you sure you want to save this record?")==true){
			  return true;
		  }else{
			  return false;
		  }

	  }


/*	  function test(){
		  location.href = "${contextPath}commissionstakeholderaccountsmanagement.html";

	  }*/
function disablecontrollsforstakeholderTypeOnCancel(theForm)
      {
      //alert('IN the on cancle');
	  theForm.reset();
	}
	
	
	
	
	
      </script>

      <v:javascript formName="commissionStakeholderModel" staticJavascript="false"/>
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
   </body>
</html>




