<!--Author: Asad Hayat-->

<%@include file="/common/taglibs.jsp"%>

<spring:bind path="smartMoneyAccountVeriflyModel.*">
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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">


   <head>
<meta name="decorator" content="decorator2">
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <meta name="title" content="Smart Money Account"/>
   </head>
   <body bgcolor="#ffffff">


      <table width="100%" border="0" cellpadding="0" cellspacing="1">

        <form method="post" action="<c:url value="/smartmoneyaccountform.html"/>" id="smartMoneyAccountForm"
        onsubmit="return onFormSubmit(this)">
        <input type="hidden" name="isDefaultAccount" id="isDefaultAccount" value="0"/>
          <c:if test="${not empty param.smartMoneyAccountId}">
             <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
           </c:if>

           <spring:bind path="smartMoneyAccountVeriflyModel.smartMoneyAccountId">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="smartMoneyAccountVeriflyModel.versionNo">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="smartMoneyAccountVeriflyModel.createdBy">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>
            <spring:bind path="smartMoneyAccountVeriflyModel.comments">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>


           <spring:bind path="smartMoneyAccountVeriflyModel.updatedBy">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="smartMoneyAccountVeriflyModel.createdOn">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="smartMoneyAccountVeriflyModel.updatedOn">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>

           <tr bgcolor="FBFBFB">
             <td align="right" bgcolor="F3F3F3" class="formText">Customer Name:&nbsp;</td>
             <td align="left">
               <spring:bind path="smartMoneyAccountVeriflyModel.customerId">
                 <c:if test="${empty param.smartMoneyAccountId}">
                   <select name="${status.expression}" class="textBox">
                     <option value=""></option>
                     <c:forEach items="${customerModelList}" var="customerModelList">
                       <option value="${customerModelList.customerId}" <c:if test="${status.value == customerModelList.customerId}">selected="selected"</c:if>>
                       ${customerModelList.firstName}&nbsp;${customerModelList.lastName}
                       </option>
                     </c:forEach>
                   </select>
                 </c:if>

                  <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">First Name:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="smartMoneyAccountVeriflyModel.firstName">
                 <c:if test="${smartMoneyAccountVeriflyModel.smartMoneyAccountId!=null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" readonly/>
                 </c:if>
                 <c:if test="${smartMoneyAccountVeriflyModel.smartMoneyAccountId==null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50" />
                 </c:if>
               </spring:bind>
             </td>
           </tr>

            <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Last Name:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="smartMoneyAccountVeriflyModel.lastName">
                 <c:if test="${smartMoneyAccountVeriflyModel.smartMoneyAccountId!=null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" readonly/>
                 </c:if>
                 <c:if test="${smartMoneyAccountVeriflyModel.smartMoneyAccountId==null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50" />
                 </c:if>
               </spring:bind>
             </td>
           </tr>

            <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Customer MobileNo:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="smartMoneyAccountVeriflyModel.customerMobileNo">
                 <c:if test="${smartMoneyAccountVeriflyModel.smartMoneyAccountId!=null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" readonly/>
                 </c:if>
                 <c:if test="${smartMoneyAccountVeriflyModel.smartMoneyAccountId==null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="11" />
                 </c:if>
               </spring:bind>
             </td>
           </tr>



                 <c:if test="${not empty param.smartMoneyAccountId}">
                   <c:forEach items="${customerModelList}" var="customerModelList">
                     <c:if test="${status.value == customerModelList.customerId}">
                       <select name="${status.expression}" class="textBox">
                         <option value="${customerModelList.customerId}" <c:if test="${status.value == customerModelList.customerId}">selected="selected"</c:if>>
                         ${customerModelList.firstName}$&nbsp;{customerModelList.lastName}
                        </option>
                       </select>
                     </c:if>
                   </c:forEach>
                 </c:if>
               </spring:bind>
             </td>
           </tr>

           <tr bgcolor="FBFBFB">
             <td align="right" bgcolor="F3F3F3" class="formText">Bank Name:&nbsp;</td>
             <td align="left">
               <spring:bind path="smartMoneyAccountVeriflyModel.bankId">
                 <c:if test="${empty param.smartMoneyAccountId}">
                   <select name="${status.expression}" class="textBox">
                     <option value=""></option>
                     <c:forEach items="${bankModelList}" var="bankModelList">
                       <option value="${bankModelList.bankId}" <c:if test="${status.value == bankModelList.bankId}">selected="selected"</c:if>>
                          ${bankModelList.name}
                       </option>
                     </c:forEach>
                   </select>
                 </c:if>

                 <c:if test="${not empty param.smartMoneyAccountId}">
                   <c:forEach items="${bankModelList}" var="bankModelList">
                     <c:if test="${status.value == bankModelList.bankId}">
                       <select name="${status.expression}" class="textBox">
                         <option value="${bankModelList.bankId}" <c:if test="${status.value == bankModelList.bankId}">selected="selected"</c:if>>
                         ${bankModelList.name}
                        </option>
                       </select>
                     </c:if>
                   </c:forEach>
                 </c:if>
               </spring:bind>
             </td>
           </tr>

          <tr>
    <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Card Type:&nbsp;</td>
    <td width="50%" align="left" bgcolor="FBFBFB">
      <spring:bind path="smartMoneyAccountVeriflyModel.cardTypeId">
        <select name="${status.expression}" class="textBox">
          <c:forEach items="${cardTypeModelList}" var="cardTypeModelList">
            <option value="${cardTypeModelList.cardTypeId}"
              <c:if test="${cardTypeModelList.cardTypeId == smartMoneyAccountVeriflyModel.cardTypeId}">selected="selected"</c:if>>
              ${cardTypeModelList.name}
            </option>
          </c:forEach>

        </select>
      </spring:bind>
    </td>
  </tr>

          <tr>
    <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Payment Mode:&nbsp;</td>
    <td width="50%" align="left" bgcolor="FBFBFB">
      <spring:bind path="smartMoneyAccountVeriflyModel.paymentModeId">
        <select name="${status.expression}" class="textBox">
          <c:forEach items="${paymentModeModelList}" var="paymentModeModelList">
            <option value="${paymentModeModelList.paymentModeId}"
              <c:if test="${paymentModeModelList.paymentModeId == smartMoneyAccountVeriflyModel.paymentModeId}">selected="selected"</c:if>>
              ${paymentModeModelList.name}
            </option>
          </c:forEach>

        </select>
      </spring:bind>
    </td>
  </tr>

   <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Account Name:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="smartMoneyAccountVeriflyModel.name">
                 <c:if test="${smartMoneyAccountVeriflyModel.smartMoneyAccountId!=null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" readonly/>
                 </c:if>
                 <c:if test="${smartMoneyAccountVeriflyModel.smartMoneyAccountId==null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50" />
                 </c:if>
               </spring:bind>
             </td>
           </tr>
            <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Account Nick:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="smartMoneyAccountVeriflyModel.accountNick">
                 <c:if test="${smartMoneyAccountVeriflyModel.smartMoneyAccountId!=null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" readonly/>
                 </c:if>
                 <c:if test="${smartMoneyAccountVeriflyModel.smartMoneyAccountId==null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50" />
                 </c:if>
               </spring:bind>
             </td>
           </tr>
            <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Card No:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="smartMoneyAccountVeriflyModel.cardNo">
                 <c:if test="${smartMoneyAccountVeriflyModel.smartMoneyAccountId!=null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox"  readonly/>
                 </c:if>
                 <c:if test="${smartMoneyAccountVeriflyModel.smartMoneyAccountId==null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="16" />
                 </c:if>
               </spring:bind>
             </td>
           </tr>
            <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Card Expiry Date:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="smartMoneyAccountVeriflyModel.cardExpiryDate">
                 <c:if test="${smartMoneyAccountVeriflyModel.smartMoneyAccountId!=null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox"  readonly/>
                 </c:if>
                 <c:if test="${smartMoneyAccountVeriflyModel.smartMoneyAccountId==null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="10" />
                 </c:if>
               </spring:bind>
             </td>
           </tr>





           

           
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="smartMoneyAccountVeriflyModel.description">
                 <textarea name="${status.expression}" cols="50" rows="5" class="textBox" type="_moz">${status.value}</textarea>
               </spring:bind>
             </td>
           </tr>



       <tr>
      <td align="right" bgcolor="#F3F3F3" class="formText">Change Ping Required:&nbsp;</td>
      <td align="left" bgcolor="#FBFBFB">
        <spring:bind path="smartMoneyAccountVeriflyModel.changePinRequired">

          <input type="hidden" name="_${status.expression}"/>
          <input type="checkbox" name="${status.expression}" value="true" <c:if test="${status.value}">checked="checked" </c:if>/>

        </spring:bind>
        </td>
      </tr>

           <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Status:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
               <spring:bind path="smartMoneyAccountVeriflyModel.active">
                 <input type="hidden" name="_${status.expression}"/>
                 <c:if test="${not empty param.smartMoneyAccountId}">
                   <input name="${status.expression}" type="radio" value="true" <c:if test="${status.value}">checked="checked"</c:if>/>
                   Active&nbsp;&nbsp;&nbsp;&nbsp;
                   <input name="${status.expression}" type="radio" value="false" <c:if test="${!status.value}">checked="checked"</c:if>/>InActive
                 </c:if>
                 <c:if test="${empty param.smartMoneyAccountId}">
                   <input name="${status.expression}" type="radio" value="true" <c:if test="${!status.value}">checked="checked"</c:if>/>
                   Active&nbsp;&nbsp;&nbsp;&nbsp;
                   <input name="${status.expression}" type="radio" value="false" <c:if test="${status.value}">checked="checked"</c:if>/>InActive
                 </c:if>
               </spring:bind>
             </td>
           </tr>


           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">      <GenericToolbar:toolbar formName="smartMoneyAccountForm"/></td>
           </tr>
        </form>
      </table>
      <script language="javascript" type="text/javascript">
      function submitForm()
      {
        if (confirm('Are you sure you want to save this record?')==true)
        {
          document.smartMoneyAccountForm.submit();
        }
        else
        {
          return false;
        }
      }
      Form.focusFirstElement($('smartMoneyAccountForm'));
      highlightFormElements();

      function onFormSubmit(theForm)
      {
        if (confirm('Do You Want To Make This As Default Account?')==true)
        {
          document.forms.smartMoneyAccountForm.isDefaultAccount.value="1";
        }

        return validateSmartMoneyAccountVeriflyModel(theForm);
      }
      </script>

      <v:javascript formName="smartMoneyAccountVeriflyModel" staticJavascript="false"/>
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
   </body>
</html>
