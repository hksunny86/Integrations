
<!--Author: Rizwan-ur-Rehman-->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>
<meta name="decorator" content="decorator2">
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <meta name="title" content="Area"/>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
   </head>

   <body bgcolor="#ffffff">


       <spring:bind path="areaModel.*">
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

        <form method="post" action="<c:url value="/areaform.html"/>" id="areaForm"
        onsubmit="return onFormSubmit(this)">



           <c:if test="${not empty param.areaId}">
             <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
           </c:if>

           <spring:bind path="areaModel.areaId">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="areaModel.versionNo">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="areaModel.createdBy">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="areaModel.updatedBy">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="areaModel.createdOn">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="areaModel.updatedOn">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
           <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Area Name:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="areaModel.name">
                 <c:if test="${areaModel.areaId!=null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" readonly/>
                 </c:if>
                 <c:if test="${areaModel.areaId==null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox"/>
                 </c:if>
               </spring:bind>
             </td>
           </tr>
                      <tr bgcolor="FBFBFB">
             <td align="right" bgcolor="F3F3F3" class="formText">Ultimate Parent Area:&nbsp;</td>
             <td align="left">
               <spring:bind path="areaModel.ultimateParentAreaId">
                   <select name="${status.expression}" class="textBox">
                     <option value=""></option> 
                     <c:forEach items="${areaModelList}" var="areaModelList">
                       <option value="${areaModelList.areaId}" 
                       			<c:if test="${status.value == areaModelList.areaId}">selected="selected"</c:if>>
                       ${areaModelList.name}
                       </option>
                     </c:forEach>
                   </select>
               </spring:bind>
             </td>
           </tr>
           

           <tr bgcolor="FBFBFB">
             <td align="right" bgcolor="F3F3F3" class="formText">Parent Area:&nbsp;</td>
             <td align="left">
               <spring:bind path="areaModel.parentAreaId">
                   <select name="${status.expression}" class="textBox">
                     <option value=""></option> 
                     <c:forEach items="${areaModelList}" var="areaModelList">
                       <option value="${areaModelList.areaId}" <c:if test="${status.value == areaModelList.areaId}">selected="selected"</c:if>>
                       ${areaModelList.name}
                       </option>
                     </c:forEach>
                   </select>
               </spring:bind>
             </td>
           </tr>
           
           
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="areaModel.description">
                 <textarea name="${status.expression}" cols="50" rows="5" onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);" class="textBox">${status.value}</textarea>
               </spring:bind>
             </td>
           </tr>
               <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="areaModel.comments">
                 <textarea name="${status.expression}" cols="50" rows="5" class="textBox" onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);">${status.value}</textarea>
               </spring:bind>
             </td>
           </tr>
           
           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">      <GenericToolbar:toolbar formName="areaForm"/></td>
           </tr>
        </form>
      </table>
      <script language="javascript" type="text/javascript">
      function submitForm()
      {
        if (confirm('Are you sure you want to save this record?')==true)
        {
          document.areaForm.submit();
        }
        else
        {
          return false;
        }
      }

      Form.focusFirstElement($('areaForm'));
      highlightFormElements();

      function onFormSubmit(theForm)
      {
        /*if(!validateFormChar(theForm)){
      		return false;
        }*/
        return validateAreaModel(theForm);
      }
      </script>

      <v:javascript formName="areaModel" staticJavascript="false"/>
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
   </body>
</html>
