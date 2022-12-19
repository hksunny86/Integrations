
<!--Author: Rizwan-ur-Rehman-->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>
<meta name="decorator" content="decorator2">
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <meta name="title" content="Commission Stakeholder"/>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
   </head>

   <body bgcolor="#ffffff">

<spring:bind path="commissionStakeholderModel.*">
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

        <form method="post" action="<c:url value="/commissionstakeholderform.html"/>" id="commissionStakeholderForm"
        onsubmit="return onFormSubmit(this)">



           <c:if test="${not empty param.commissionStakeholderId}">
             <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
           </c:if>

           <spring:bind path="commissionStakeholderModel.commissionStakeholderId">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="commissionStakeholderModel.versionNo">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="commissionStakeholderModel.createdBy">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="commissionStakeholderModel.updatedBy">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="commissionStakeholderModel.createdOn">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <spring:bind path="commissionStakeholderModel.updatedOn">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>

           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
           <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">
             	<span style="color:#FF0000">*</span>Commission Stakeholder:&nbsp;
             </td>
             <td width="51%" align="left" bgcolor="FBFBFB">

	          <spring:bind path="commissionStakeholderModel.name">
                 <c:if test="${commissionStakeholderModel.commissionStakeholderId!=null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" readonly="readonly"  maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"/>
                 </c:if>
                 <c:if test="${commissionStakeholderModel.commissionStakeholderId==null}">
                   <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"/>
                 </c:if>
               </spring:bind>
             </td>
           </tr>

			<tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">
             	<span style="color:#FF0000">*</span>Contact Name:&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">

	          <spring:bind path="commissionStakeholderModel.contactName">
                 <c:if test="${commissionStakeholderModel.commissionStakeholderId!=null}">
                   <input type="text" name="${status.expression}" value="${status.value}" maxlength="50"  class="textBox" />
                 </c:if>
                 <c:if test="${commissionStakeholderModel.commissionStakeholderId==null}">
                   <input type="text" name="${status.expression}" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" value="${status.value}" class="textBox"/>
                 </c:if>
               </spring:bind>
             </td>
           </tr>
           
               <tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Stakeholder Type:&nbsp;</td>
      <td align="left">
        <spring:bind path="commissionStakeholderModel.stakeholderTypeId">
            <select name="${status.expression}" class="textBox" onchange="disablecontrollsforstakeholderType(this)" <c:if test="${not empty param.commissionStakeholderId}">disabled="disabled"</c:if>>              
              <c:forEach items="${stakeholderTypeModelList}" var="stakeholderTypeModelList">
                <option value="${stakeholderTypeModelList.stakeholderTypeId}"
                  <c:if test="${status.value == stakeholderTypeModelList.stakeholderTypeId}">selected="selected"</c:if>/>
                  ${stakeholderTypeModelList.name}
                </option>
              </c:forEach>
            </select>
        </spring:bind>
        <c:if test="${not empty param.commissionStakeholderId}">
                   <spring:bind path="commissionStakeholderModel.stakeholderTypeId">
		             <input type="hidden" name="${status.expression}" value="${status.value}"/>
        		   </spring:bind>  
        </c:if>
      </td>
    </tr>
           
<%--           <tr bgcolor="FBFBFB">--%>
<%--             <td align="right" bgcolor="F3F3F3" class="formText">Distributor:&nbsp;</td>--%>
<%--             <td align="left">--%>
<%--               <spring:bind path="commissionStakeholderModel.distributorId">--%>
<%--                 <c:if test="${empty param.commissionStakeholderId}">--%>
<%--                   <select id="distributorName" name="${status.expression}" class="textBox">                                          --%>
<%--                     <c:forEach items="${distributorModelList}" var="distributorModelList">--%>
<%--                       <option value="${distributorModelList.distributorId}" --%>
<%--                       <c:if test="${status.value == distributorModelList.distributorId}">selected="selected"</c:if>>--%>
<%--                       ${distributorModelList.name}--%>
<%--                       </option>--%>
<%--                     </c:forEach>--%>
<%--                   </select>--%>
<%--                 </c:if>--%>
<%--                 <c:if test="${not empty param.commissionStakeholderId}">  --%>
<%--                   <spring:bind path="commissionStakeholderModel.distributorId">--%>
<%--		             <input type="hidden" name="${status.expression}" value="${status.value}"/>--%>
<%--        		   </spring:bind>             --%>
<%--                   <select id="distributorName" name="${status.expression}" class="textBox">                     --%>
<%--                     <option value=""/>--%>
<%--                     <c:forEach items="${distributorModelList}" var="distributorModelList">--%>
<%--                       <option value="${distributorModelList.distributorId}" --%>
<%--                       <c:if test="${status.value == distributorModelList.distributorId}">selected="selected"</c:if>>--%>
<%--                       ${distributorModelList.name}--%>
<%--                       </option>--%>
<%--                     </c:forEach>--%>
<%--                   </select>--%>
<%--                 </c:if>--%>
<%--               </spring:bind>--%>
<%--             </td>--%>
<%--           </tr>--%>

           <tr bgcolor="FBFBFB">
             <td align="right" bgcolor="F3F3F3" class="formText">Operator:&nbsp;</td>
             <td align="left">
               <spring:bind path="commissionStakeholderModel.operatorId">
                 <c:if test="${empty param.commissionStakeholderId}">
                   <select id="operatorName" name="${status.expression}" class="textBox">                     
                     <c:forEach items="${operatorModelList}" var="operatorModelList">
                       <option value="${operatorModelList.operatorId}" <c:if test="${status.value == operatorModelList.operatorId}">selected="selected"</c:if>>
                       ${operatorModelList.name}
                       </option>
                     </c:forEach>
                   </select>
                 </c:if>
                 <c:if test="${not empty param.commissionStakeholderId}">
                   <spring:bind path="commissionStakeholderModel.operatorId">
		             <input type="hidden" name="${status.expression}" value="${status.value}"/>
        		   </spring:bind>  
                   <select id="operatorName" name="${status.expression}" class="textBox">                     
                     <option value=""/>
                     <c:forEach items="${operatorModelList}" var="operatorModelList">
                       <option value="${operatorModelList.operatorId}" <c:if test="${status.value == operatorModelList.operatorId}">selected="selected"</c:if>>
                       ${operatorModelList.name}
                       </option>
                     </c:forEach>
                   </select>
                 </c:if>
               </spring:bind>
             </td>
           </tr>
<%--           <tr bgcolor="FBFBFB">--%>
<%--             <td align="right" bgcolor="F3F3F3" class="formText">Retailer:&nbsp;</td>--%>
<%--             <td align="left">--%>
<%--               <spring:bind path="commissionStakeholderModel.retailerId">--%>
<%--                 <c:if test="${empty param.commissionStakeholderId}">--%>
<%--                   <select id="retailerName" name="${status.expression}" class="textBox">                     --%>
<%--                     <c:forEach items="${retailerModelList}" var="retailerModelList">--%>
<%--                       <option value="${retailerModelList.retailerId}" <c:if test="${status.value == retailerModelList.retailerId}">selected="selected"</c:if>>--%>
<%--                       ${retailerModelList.name}--%>
<%--                       </option>--%>
<%--                     </c:forEach>--%>
<%--                   </select>--%>
<%--                 </c:if>--%>
<%--                 <c:if test="${not empty param.commissionStakeholderId}">--%>
<%--                   <spring:bind path="commissionStakeholderModel.retailerId">--%>
<%--		             <input type="hidden" name="${status.expression}" value="${status.value}"/>--%>
<%--        		   </spring:bind>  --%>
<%--                   <select id="retailerName" name="${status.expression}" class="textBox">                     --%>
<%--                     <option value=""/>--%>
<%--                     <c:forEach items="${retailerModelList}" var="retailerModelList">--%>
<%--                       <option value="${retailerModelList.retailerId}" <c:if test="${status.value == retailerModelList.retailerId}">selected="selected"</c:if>>--%>
<%--                       ${retailerModelList.name}--%>
<%--                       </option>--%>
<%--                     </c:forEach>--%>
<%--                   </select>--%>
<%--                 </c:if>--%>
<%--               </spring:bind>--%>
<%--             </td>--%>
<%--           </tr>--%>
           <tr bgcolor="FBFBFB">
             <td align="right" bgcolor="F3F3F3" class="formText">Bank:&nbsp;</td>
             <td align="left">
               <spring:bind path="commissionStakeholderModel.bankId">
                 <c:if test="${empty param.commissionStakeholderId}">
                   <select id="bankName" name="${status.expression}" class="textBox">                     
                     <c:forEach items="${bankModelList}" var="bankModelList">
                       <option value="${bankModelList.bankId}" <c:if test="${status.value == bankModelList.bankId}">selected="selected"</c:if>>
                       ${bankModelList.name}
                       </option>
                     </c:forEach>
                   </select>
                 </c:if>
                 <c:if test="${not empty param.commissionStakeholderId}">
                   <spring:bind path="commissionStakeholderModel.bankId">
		             <input type="hidden" name="${status.expression}" value="${status.value}"/>
        		   </spring:bind>  
                   <select id="bankName" name="${status.expression}" class="textBox">                     
                     <option value=""/>
                     <c:forEach items="${bankModelList}" var="bankModelList">
                       <option value="${bankModelList.bankId}" <c:if test="${status.value == bankModelList.bankId}">selected="selected"</c:if>>
                       ${bankModelList.name}
                       </option>
                     </c:forEach>
                   </select>
                 </c:if>
               </spring:bind>
             </td>
           </tr>
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">
             	Description:&nbsp;
             </td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="commissionStakeholderModel.description">
                 <textarea name="${status.expression}" cols="50" rows="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox">${status.value}</textarea>
               </spring:bind>
             </td>
           </tr>
           <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">
             	Comments:&nbsp;
             </td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <spring:bind path="commissionStakeholderModel.comments">
				<textarea name="${status.expression}" cols="50" rows="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox">${status.value}</textarea>
               </spring:bind>
             </td>
           </tr>

           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">    
             <input type= "button" name = "_save" value="  Save  " onclick="javascript:onSave(document.forms.commissionStakeholderForm,null);" class="button"/>
             <input type= "button" name = "_cancel" value=" Cancel " onclick="javascript:window.location='commissionstakeholdermanagement.html';" class="button"/>
<%--
<input type= "button" name = "_cancel" value=" Cancel " onclick="javascript:disablecontrollsforstakeholderTypeOnCancel(document.forms.commissionStakeholderForm);" class="button"/>
--%>
               </td>
           </tr>
        </form>
      </table>
      <script language="javascript" type="text/javascript">
      function submitForm()
      {
        if (confirm('Are you sure you want to save this record?')==true)
        {
          document.commissionStakeholderForm.submit();
        }
        else
        {
          return false;
        }
      }

      Form.focusFirstElement($('commissionStakeholderForm'));
      highlightFormElements();

      function onFormSubmit(theForm)
      {
        /*if(!validateFormChar(theForm)){
      		return false;
        }*/
        return validateCommissionStakeholderModel(theForm);
      }
   
      if( document.getElementById("isUpdate") == null )      
      {
          var stakeholderTypeIdSelectedIndex = document.getElementById("commissionStakeholderForm").stakeholderTypeId.selectedIndex;
		  var stakeholderTypeIdValue = document.getElementById("commissionStakeholderForm").stakeholderTypeId[stakeholderTypeIdSelectedIndex].text;
      
	      if(stakeholderTypeIdValue.toUpperCase() == "OPERATOR")
	      {
		      <%--	   	 document.getElementById("distributorName").disabled=true;--%>
			 document.getElementById("operatorName").disabled=false;
	<%--		 document.getElementById("retailerName").disabled=true;--%>
			 document.getElementById("bankName").disabled=true;
	      }
	      else if (stakeholderTypeIdValue.toUpperCase() == "BANK")
	      {
		      <%--	   	 document.getElementById("distributorName").disabled=true;--%>
			 document.getElementById("operatorName").disabled=true;
	<%--		 document.getElementById("retailerName").disabled=true;--%>
			 document.getElementById("bankName").disabled=false;
	      }
	      else
	      {
	      	//?
	      }
	  }	  
	  else
      {
<%--	   	  document.getElementById("distributorName").disabled=true;--%>
		 document.getElementById("operatorName").disabled=true;
<%--		 document.getElementById("retailerName").disabled=true;--%>
		 document.getElementById("bankName").disabled=true;
	  }
	  
      
      function disablecontrollsforstakeholderType(theForm)
      {
	  	if (document.getElementById("stakeholderTypeId").value=="1")
		{
<%--			document.getElementById("distributorName").disabled=true;--%>
			document.getElementById("operatorName").disabled=true;
<%--			document.getElementById("retailerName").disabled=true;--%>
			document.getElementById("bankName").disabled=false;
		}
		else if (document.getElementById("stakeholderTypeId").value=="3")
		{
<%--			document.getElementById("distributorName").disabled=true;--%>
			document.getElementById("operatorName").disabled=true;
<%--			document.getElementById("retailerName").disabled=false;--%>
			document.getElementById("bankName").disabled=true;
		}
		else if (document.getElementById("stakeholderTypeId").value=="4")
		{
<%--			document.getElementById("distributorName").disabled=false;--%>
			document.getElementById("operatorName").disabled=true;
<%--			document.getElementById("retailerName").disabled=true;--%>
			document.getElementById("bankName").disabled=true;
		}
		else if (document.getElementById("stakeholderTypeId").value=="5")
		{
<%--			document.getElementById("distributorName").disabled=true;--%>
			document.getElementById("operatorName").disabled=false;
<%--			document.getElementById("retailerName").disabled=true;--%>
			document.getElementById("bankName").disabled=true;
		}
		else 
		{
<%--			document.getElementById("distributorName").disabled=true;--%>
			document.getElementById("operatorName").disabled=true;
<%--			document.getElementById("retailerName").disabled=true;--%>
			document.getElementById("bankName").disabled=true;
		}
	}
	
function disablecontrollsforstakeholderTypeOnCancel(theForm)
      {
      //alert('IN the on cancle');
	  theForm.reset();
	  

<%--			document.getElementById("distributorName").disabled=true;--%>
			document.getElementById("operatorName").disabled=true;
<%--			document.getElementById("retailerName").disabled=true;--%>
			if(document.getElementById("isUpdate") != null)
			    document.getElementById("bankName").disabled=true;
            else
	            document.getElementById("bankName").disabled=false;
	}
	
	
	
	
	
      </script>

      <v:javascript formName="commissionStakeholderModel" staticJavascript="false"/>
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
   </body>
</html>




