
<!--Author: Rizwan-ur-Rehman-->
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>
<meta name="decorator" content="decorator2">
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <meta name="title" content="Bank"/>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
   </head>

   <body bgcolor="#ffffff">
      

       <spring:bind path="bankModel.*">
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

        <form method="post" action="<c:url value="/bankform.html"/>" id="bankForm"
        onsubmit="return onFormSubmit(this)">



           <c:if test="${not empty param.bankId}">
             <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
             <spring:bind path="bankModel.name">
                <input type="hidden" name="${status.expression}" value="${status.value}"/>
             </spring:bind>
             <spring:bind path="bankModel.veriflyId">
                <input type="hidden" name="${status.expression}" value="${status.value}"/>
             </spring:bind>
             <spring:bind path="bankModel.financialIntegrationId">
                <input type="hidden" name="${status.expression}" value="${status.value}"/>
             </spring:bind>
           </c:if>

           <spring:bind path="bankModel.bankId">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="bankModel.versionNo">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="bankModel.createdBy">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="bankModel.updatedBy">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="bankModel.createdOn">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="bankModel.updatedOn">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
           <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Bank:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="bankModel.name">
                 <c:if test="${not empty param.bankId}">
                   <input type="text" name="${status.expression}" value="${status.value}" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" class="textBox" disabled="disabled"/>
                 </c:if>
                 <c:if test="${empty param.bankId}">
                   <input type="text" name="${status.expression}" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50" class="textBox"/>
                 </c:if>
               </spring:bind>
             </td>
           </tr>

           <tr bgcolor="FBFBFB">
             <td align="right" bgcolor="F3F3F3" class="formText">Verifly:&nbsp;</td>
             <td align="left">
               <spring:bind path="bankModel.veriflyId">
                 <c:if test="${empty param.bankId}">
                   <select name="${status.expression}" class="textBox">
                     <option value=""></option>
                     <c:forEach items="${veriflyModelList}" var="veriflyModelList">
                       <option value="${veriflyModelList.veriflyId}" <c:if test="${status.value == veriflyModelList.veriflyId}">selected="selected"</c:if>>
                       ${veriflyModelList.name}
                       </option>
                     </c:forEach>
                   </select>
                 </c:if>

                 <c:if test="${not empty param.bankId}">
                   
                   <select name="${status.expression}" class="textBox" disabled="disabled">
                     <option value=""></option>
                     <c:forEach items="${veriflyModelList}" var="veriflyModelList">
                       <option value="${veriflyModelList.veriflyId}" <c:if test="${status.value == veriflyModelList.veriflyId}">selected="selected"</c:if>>
                       ${veriflyModelList.name}
                       </option>
                     </c:forEach>
                 </select>
                                     
                 </c:if>
               </spring:bind>
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Financial Institution:&nbsp;</td>
             <td align="left">
               <spring:bind path="bankModel.financialIntegrationId">
                   <c:if test="${empty param.bankId}">
                      <select name="${status.expression}" class="textBox">
                        <c:forEach items="${financialIntegrationModelList}" var="financialIntegrationModelList">
                          <option value="${financialIntegrationModelList.financialIntegrationId}" <c:if test="${status.value == financialIntegrationModelList.financialIntegrationId}">selected="selected"</c:if>>
                          ${financialIntegrationModelList.name}
                          </option>
                        </c:forEach>
                      </select>
                   </c:if>
                   <c:if test="${not empty param.bankId}">
                      
                      <select name="${status.expression}" class="textBox" disabled="disabled">
                        <c:forEach items="${financialIntegrationModelList}" var="financialIntegrationModelList">
                          <option value="${financialIntegrationModelList.financialIntegrationId}" <c:if test="${status.value == financialIntegrationModelList.financialIntegrationId}">selected="selected"</c:if>>
                          ${financialIntegrationModelList.name}
                          </option>
                        </c:forEach>
                      </select>
                   </c:if>
               </spring:bind>
             </td>
           </tr>
           
   <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Permission Group:&nbsp;</td>
    <td>

							<select name="permissionGroupId" class="textBox" <c:if test="${not empty param.bankId}">disabled="disabled"</c:if> 	  >
								<c:forEach items="${permissionGroupModelList}"
									var="permissionGroupModelList">
									<option value="${permissionGroupModelList.permissionGroupId}" 
										<c:if test="${permissionGroupIdInReq == permissionGroupModelList.permissionGroupId}">selected="selected"</c:if>
										>
										${permissionGroupModelList.name}
									</option>
								</c:forEach>
							</select>
        ${status.errorMessage}
   </td>
  </tr>                    
 
           <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Contact Name:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="bankModel.contactName">
                 <input type="text" name="${status.expression}" value="${status.value}" maxlength="50"  onkeypress="return maskAlphaWithSp(this,event)" class="textBox"/>
               </spring:bind>
             </td>
           </tr>
           <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Phone No:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="bankModel.phoneNo">
                 <input type="text" name="${status.expression}" value="${status.value}" class="textBox"onkeypress="return maskNumber(this,event)" maxlength="11"/>
               </spring:bind>
             </td>
           </tr>

           <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Fax No:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="bankModel.fax">
                 <input type="text" name="${status.expression}" value="${status.value}" class="textBox" onkeypress="return maskNumber(this,event)" maxlength="11" />
               </spring:bind>
             </td>
           </tr>

           <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Email:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="bankModel.email">
                 <input type="text" name="${status.expression}" value="${status.value}" class="textBox"  maxlength="50" />
               </spring:bind>
             </td>
           </tr>
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Address1:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="bankModel.address1">
                   <input name="${status.expression}" type="text" size="40" class="textBox" onkeyup="textAreaLengthCounter(this,250);" maxlength="250" value="${status.value}"/>
               </spring:bind>
             </td>
           </tr>

           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Address2:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="bankModel.address2">
                 <input name="${status.expression}" type="text" size="40" class="textBox" onkeyup="textAreaLengthCounter(this,250);" maxlength="250" value="${status.value}"/>
               </spring:bind>
             </td>
           </tr>

           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">City:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="bankModel.city">
                 <input type="text" name="${status.expression}" value="${status.value}" class="textBox" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50"/>
               </spring:bind>
             </td>
           </tr>
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">State:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="bankModel.state">
                 <input type="text" name="${status.expression}" value="${status.value}" class="textBox" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50"/>
               </spring:bind>
             </td>
           </tr>
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Zip Code:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="bankModel.zip">
                 <input type="text" name="${status.expression}" value="${status.value}" class="textBox" onkeypress="return maskAlphaNumeric(this,event)" maxlength="10"/>
               </spring:bind>
             </td>
           </tr>
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Country:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="bankModel.country">
                 <input type="text" name="${status.expression}" value="${status.value}" class="textBox" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50"/>
               </spring:bind>
             </td>
           </tr>
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="bankModel.description">
               <textarea name="${status.expression}" cols="50"  onkeypress="return maskCommon(this,event)"
								onkeyup="textAreaLengthCounter(this,250);" rows="5"
								class="textBox" type="_moz">${status.value}</textarea>
               </spring:bind>
             </td>
           </tr>
               <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="bankModel.comments">
                 <textarea name="${status.expression}" cols="50"  onkeypress="return maskCommon(this,event)"
								onkeyup="textAreaLengthCounter(this,250);" rows="5"
								class="textBox" type="_moz">${status.value}</textarea>
               </spring:bind>
             </td>
           </tr>
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Image Path:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="bankModel.imagePath">
                 <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50"/>
               </spring:bind>
             </td>
           </tr>
      <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="bankModel.active">
	    <input name="${status.expression}" type="checkbox" value="true" 	
	    <c:if test="${status.value==true}">checked="checked"</c:if>
	    <c:if test="${empty param.bankId}">checked="checked"</c:if>
	    />			    
     </spring:bind>
    </td>
  </tr>
           

           <tr >
             <td colspan="2" align="center"><GenericToolbar:toolbar formName="bankForm"/></td>
           </tr>
        </form>
      </table>
      <script language="javascript" type="text/javascript">
      
      Form.focusFirstElement($('bankForm'));
      highlightFormElements();

      function onFormSubmit(theForm)
      {
        /*if(!validateFormChar(theForm)){
      		return false;
        }*/
        return validateBankModel(theForm);
      }
      </script>

      <v:javascript formName="bankModel" staticJavascript="false"/>
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
   </body>
</html>
