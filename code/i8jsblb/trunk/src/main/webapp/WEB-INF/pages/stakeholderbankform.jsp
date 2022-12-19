<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<meta name="title" content="Stakeholder Bank"/>

      
<script type="text/javascript">


</script>

<body>
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
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>


	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="1">
	<form id="stakeholderbankform" action="<c:url value="/stakeholderbankform.html"/>" method="post" onsubmit="return validateStakeholderBankInfoModel(this)">
        <c:if test="${not empty param.stakeholderBankInfoId}">
          <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
                  <spring:bind path="stakeholderBankInfoModel.stakeholderBankInfoId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>
        
                <spring:bind path="stakeholderBankInfoModel.commissionStakeholderId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>
        
                <spring:bind path="stakeholderBankInfoModel.bankId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>
        </c:if>


        <spring:bind path="stakeholderBankInfoModel.versionNo">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="stakeholderBankInfoModel.createdBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="stakeholderBankInfoModel.updatedBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

           <spring:bind path="stakeholderBankInfoModel.createdOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
           <spring:bind path="stakeholderBankInfoModel.updatedOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
	<tr bgcolor="FBFBFB">
          <td colspan="2">&nbsp;</td>
	</tr>
  <tr>
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Stakeholder Bank:&nbsp;</td>
            <td width="51%" align="left" bgcolor="FBFBFB">
            <spring:bind path="stakeholderBankInfoModel.name">
                <input type="text" name="${status.expression}"  value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)" <c:if test="${not empty param.stakeholderBankInfoId}">readonly='readonly'</c:if> class="textBox" maxlength="50"/>
            </spring:bind></td>
          </tr>

  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Commission Stakeholder:&nbsp;</td>
	<td bgcolor="FBFBFB" >
		<spring:bind path="stakeholderBankInfoModel.commissionStakeholderId">
               
               <select name="${status.expression}" class="textBox" <c:if test="${not empty param.stakeholderBankInfoId}">disabled='disabled'</c:if>>
								
								<c:forEach items="${commissionStakeHolderModelList}"
									var="commissionStakeHolderModelList">
									<option value="${commissionStakeHolderModelList.commissionStakeholderId}"
										<c:if test="${status.value == commissionStakeHolderModelList.commissionStakeholderId}">selected="selected"</c:if>>
										${commissionStakeHolderModelList.name}
									</option>
								</c:forEach>
							</select>
        ${status.errorMessage}
                </spring:bind>
               
	</td>
  </tr>
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Bank:&nbsp;</td>
	<td bgcolor="FBFBFB" >
		<spring:bind path="stakeholderBankInfoModel.bankId">
		
		
		<select name="${status.expression}" class="textBox" <c:if test="${not empty param.stakeholderBankInfoId}">disabled='disabled'</c:if>>
								
								<c:forEach items="${bankModelList}"
									var="bankModelList">
									<option value="${bankModelList.bankId}"
										<c:if test="${status.value == bankModelList.bankId}">selected="selected"</c:if>>
										${bankModelList.name}
									</option>
								</c:forEach>
							</select>
        ${status.errorMessage}
                
                </spring:bind>
                
                
	</td>
  </tr>
  
  <%--<tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Payment Mode:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="stakeholderBankInfoModel.paymentModeId">
            
                
                <select name="${status.expression}" class="textBox">
								
								<c:forEach items="${PaymentModeModelList}"
									var="PaymentModeModelList">
									<option value="${PaymentModeModelList.paymentModeId}"
										<c:if test="${status.value == PaymentModeModelList.paymentModeId}">selected="selected"</c:if>>
										${PaymentModeModelList.name}
									</option>
								</c:forEach>
							</select>
        ${status.errorMessage}
                </spring:bind>
                
                
                
                
                
                          </td>
          </tr>
          --%><tr>
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Account No:&nbsp;</td>
            <td width="51%" align="left" bgcolor="FBFBFB">
            <spring:bind path="stakeholderBankInfoModel.accountNo">
                <input type="text"  name="${status.expression}" value="${status.value}" onkeypress="return maskNumber(this,event)" class="textBox" maxlength="50" />
            </spring:bind>
            </td>
          </tr>
  
  <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="stakeholderBankInfoModel.active">
	    <input name="${status.expression}" type="checkbox" value="true" 	
	    	    <c:if test="${status.value==true}">checked="checked"</c:if>
				<c:if test="${empty param.stakeholderBankInfoId && empty param._save }">checked="checked"</c:if> 
				<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
	    />			    
     </spring:bind>
    </td>
  </tr>  
  

  
<tr>
  <td colspan="2" bgcolor="FBFBFB" align="middle"><GenericToolbar:toolbar formName="stakeholderbankform" /></td></tr>
</form>
</table>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<script language="javascript" type="text/javascript">
document.forms[0].name.focus();
highlightFormElements();
function popupCallback(src,popupName,columnHashMap)
{

  if(src=='commissionStakeholderName')
  {
    document.forms.stakeholderbankform.commissionStakeholderId.value = columnHashMap.get('PK');
    document.forms.stakeholderbankform.commissionStakeholderName.value = columnHashMap.get('name');
  }
  if(src=='bankName')
  {
    document.forms.stakeholderbankform.bankId.value = columnHashMap.get('PK');
    document.forms.stakeholderbankform.bankName.value = columnHashMap.get('name');
  }
   if(src=='paymentTypeName')
  {

    document.forms.stakeholderbankform.paymentModeId.value = columnHashMap.get('PK');
    document.forms.stakeholderbankform.paymentModeName.value = columnHashMap.get('name');

  }
}
function onFormSubmit(theForm)
{
 return validateStakeholderBankInfoModel(theForm);
}
</script>
<v:javascript formName="stakeholderBankInfoModel"
staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript"
src="<c:url value="/scripts/validator.jsp"/>"></script>
</body>
</html>
