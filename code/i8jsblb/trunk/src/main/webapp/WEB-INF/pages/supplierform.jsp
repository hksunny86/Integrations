<!--Title: i8Microbank-->
<!--Author: Asad Hayat-->
<%@ include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator2">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<v:javascript formName="supplierModel"
staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript"
src="<c:url value="/scripts/validator.jsp"/>"></script>
<meta name="title" content="Supplier"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
</head>
<body>

        
<spring:bind path="supplierModel.*">
  <c:if test="${not empty status.errorMessages}">
    <div class="errorMsg">
      <c:forEach var="error" items="${status.errorMessages}">
        <c:out value="${error}" escapeXml="false" />
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
	<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<form id="SupplierForm" action="supplierform.html" method="post" onSubmit="return onFormSubmit(this)">
        <c:if test="${not empty param.supplierId}">
          <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
          <spring:bind path="supplierModel.name">
              <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
        </c:if>
        <spring:bind path="supplierModel.supplierId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>
        <spring:bind path="supplierModel.versionNo">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>
        <spring:bind path="supplierModel.comments">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="supplierModel.createdBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>
        <spring:bind path="supplierModel.createdOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>
        <spring:bind path="supplierModel.updatedBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
        <spring:bind path="supplierModel.updatedOn">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>
          <tr bgcolor="FBFBFB">
            <td colspan="2" align="center">&nbsp;</td>
          </tr>
          <tr>
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Supplier:&nbsp;            </td>
            <td width="51%" align="left" bgcolor="FBFBFB">
            <spring:bind path="supplierModel.name">
            <input name="${status.expression}" type="text" size="40" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)" <c:if test="${not empty param.supplierId}">disabled='disabled'</c:if> />
</spring:bind></td>
          </tr>

   <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Permission Group:&nbsp;</td>
    <td>

							<select name="permissionGroupId" class="textBox" <c:if test="${not empty param.supplierId}">disabled="disabled"</c:if> 	  >
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
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Contact Name:&nbsp;              </td>
            <td width="51%" align="left" bgcolor="FBFBFB"><spring:bind path="supplierModel.contactName">
            <input name="${status.expression}" type="text" size="40" class="textBox" maxlength="50" value="${status.value}" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)">
</spring:bind></td>
          </tr>
         <tr >
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Phone No:&nbsp;              </td>
            <td width="51%" align="left" bgcolor="FBFBFB"><spring:bind path="supplierModel.phoneNo">
            <input type="text" name="${status.expression}" class="textBox" maxlength="11" value="${status.value}"onkeypress="return maskNumber(this,event)"/>
</spring:bind></td>
          </tr><tr >
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Fax:&nbsp;               </td>
            <td width="51%" align="left" bgcolor="FBFBFB"><spring:bind path="supplierModel.fax">
            <input type="text" name="${status.expression}" class="textBox" maxlength="11" value="${status.value}"onkeypress="return maskNumber(this,event)"/>
</spring:bind></td>
          </tr>
		  <tr >
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Email:&nbsp;              </td>
            <td width="51%" align="left" bgcolor="FBFBFB"><spring:bind path="supplierModel.email"><input name="${status.expression}" type="text" size="30" class="textBox" maxlength="50" value="${status.value}">

</spring:bind></td>
          </tr>
          <tr>
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Address1:&nbsp;             </td>
            <td width="51%" align="left" bgcolor="FBFBFB"><spring:bind path="supplierModel.address1">
            <input name="${status.expression}" type="text" size="40" class="textBox" maxlength="250" value="${status.value}" onkeyup="textAreaLengthCounter(this,250);"/>
</spring:bind></td>
          </tr>
          <tr>
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Address2:&nbsp;              </td>
            <td width="51%" align="left" bgcolor="FBFBFB"><spring:bind path="supplierModel.address2"><input name="${status.expression}" type="text" size="40" class="textBox"  maxlength="250" value="${status.value}" onkeyup="textAreaLengthCounter(this,250);"/>
</spring:bind></td>
          </tr>
          <tr>
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">City:&nbsp;              </td>
            <td width="51%" align="left" bgcolor="FBFBFB"><spring:bind path="supplierModel.city">
            <input name="${status.expression}" type="text" size="40" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
</spring:bind></td>
          </tr>
          <tr>
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">State:&nbsp;              </td>
            <td width="51%" align="left" bgcolor="FBFBFB"><spring:bind path="supplierModel.state">
            <input name="${status.expression}" type="text" size="40" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
</spring:bind></td>
          </tr>
          <tr>
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Zip Code:&nbsp;              </td>
            <td width="51%" align="left" bgcolor="FBFBFB"><spring:bind path="supplierModel.zip">
            <input name="${status.expression}" type="text" size="40" class="textBox" maxlength="10" value="${status.value}" onkeypress="return maskAlphaNumeric(this,event)"/>
</spring:bind></td>
          </tr>

          <tr>
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Country.:&nbsp;              </td>
            <td width="51%" align="left" bgcolor="FBFBFB"><spring:bind path="supplierModel.country">
            <input name="${status.expression}" type="text" size="40" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
</spring:bind></td>
          </tr>
	  
          <tr>
            <td align="right" bgcolor="#F3F3F3" class="formText">Description:&nbsp;</td>
            <td align="left" bgcolor="#FBFBFB"><spring:bind path="supplierModel.description"><textarea name="${status.expression}" cols="50" rows="5" class="textBox" onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);">${status.value}</textarea></spring:bind></td>
          </tr>
      
      <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Image Path:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="supplierModel.imagePath">
                 <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50"/>
               </spring:bind>
             </td>
           </tr>    
          <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText">Vendor:&nbsp;</td>
            <td align="left" class="formText">
		    <spring:bind path="supplierModel.vendor">
			    <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>/>			    
		    </spring:bind>
			</td>
      </tr>
      
      <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="supplierModel.active">
	    <input name="${status.expression}" type="checkbox" value="true" 	
	        <c:if test="${status.value==true}">checked="checked"</c:if>
	    <c:if test="${empty param.supplierId && empty param._save }">checked="checked"</c:if> 
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
	    />			    
     </spring:bind>
    </td>
  </tr>
      
          <tr bgcolor="FBFBFB">
				<td colspan="2" align="center">
							<GenericToolbar:toolbar formName="SupplierForm"/>
				</td>
			</tr>
          
          

</table>
</form>
<script type="text/javascript">



function onFormSubmit(theForm) {
/*if(!validateFormChar(theForm)){
      		return false;*/

return validateSupplierModel(theForm);
}

</script>

<v:javascript formName="supplierModel" staticJavascript="false"/>
<script type="text/javascript" src="<c:url
value="/scripts/validator.jsp"/>"></script>

</body>
</html>
