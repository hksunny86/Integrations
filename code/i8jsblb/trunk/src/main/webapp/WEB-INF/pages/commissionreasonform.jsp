<!--Author: Rizwan-ur-Rehman-->

<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>
<meta name="decorator" content="decorator2">
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <meta name="title" content="Charges Method"/>

   </head>
   <body bgcolor="#ffffff">

<spring:bind path="commissionReasonModel.*">
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

		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>


      <table width="100%" border="0" cellpadding="0" cellspacing="1">

        <form method="post" action="<c:url value="/commissionreasonform.html"/>" id="commissionReasonForm"
        onsubmit="return onFormSubmit(this)">


           <c:if test="${not empty param.commissionReasonId}">
             <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
           </c:if>

           <spring:bind path="commissionReasonModel.commissionReasonId">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="commissionReasonModel.versionNo">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="commissionReasonModel.createdBy">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="commissionReasonModel.updatedBy">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="commissionReasonModel.createdOn">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="commissionReasonModel.updatedOn">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
           <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Charges Method:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="commissionReasonModel.name">
                 <c:if test="${commissionReasonModel.commissionReasonId!=null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox"  readonly onkeypress="return maskAlphaWithSp(this,event)"/>
                 </c:if>
                 <c:if test="${commissionReasonModel.commissionReasonId==null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"/>
                 </c:if>
               </spring:bind>
             </td>
           </tr>

           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="commissionReasonModel.reasonDesc">
                 <textarea name="${status.expression}" cols="50" rows="5" class="textBox" onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);">${status.value}</textarea>
               </spring:bind>
             </td>
           </tr>

           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="commissionReasonModel.comments">
                 <textarea name="${status.expression}" cols="50" rows="5" class="textBox" onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);">${status.value}</textarea>
               </spring:bind>
             </td>
           </tr>
           
		 <tr>
		    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
		    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
		    <spring:bind path="commissionReasonModel.active">
			    <input name="${status.expression}" type="checkbox" value="true" 	
				<c:if test="${status.value==true}">checked="checked"</c:if>
				<c:if test="${empty param.commissionReasonId && empty param._save }">checked="checked"</c:if> 
				<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
			    />			    
		     </spring:bind>
		    </td>
		  </tr>

           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">
             	<input type="button" class="button" onclick="javascript:onSave(document.forms.commissionReasonForm,null);" value="  Save  " name="_save">
             	<input type="button" class="button" onclick="javascript:window.location='commissionreasonmanagement.html';" value=" Cancel " name="_cancel">
             </td>
           </tr>
           
        </form>
      </table>
      <script language="javascript" type="text/javascript">
      
      function submitForm()
      {
        if (confirm('Are you sure you want to save this record?')==true)
        {
          document.commissionReasonForm.submit();
        }
        else
        {
          return false;
        }
      }

      Form.focusFirstElement($('commissionReasonForm'));
      highlightFormElements();

      function onFormSubmit(theForm)
      {
        /*if(!validateFormChar(theForm)){
      		return false;
        }*/
        return validateCommissionReasonModel(theForm);
      }
      </script>

      <v:javascript formName="commissionReasonModel" staticJavascript="false"/>
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
   </body>
</html>
