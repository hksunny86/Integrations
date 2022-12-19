<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author:Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<head>
<meta name="decorator" content="decorator2">
    
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

     <meta name="title" content="Service Operator"/>
     <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
   </head>

<body>

    <spring:bind path="mnoModel.*">
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
	<form id="mnoForm" method="post" action="mnoform.html" onSubmit="return onFormSubmit(this)">
         <c:if test="${not empty param.mnoId}">
          <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
        </c:if>

    	<spring:bind path="mnoModel.mnoId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>
        <spring:bind path="mnoModel.versionNo">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="mnoModel.createdBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="mnoModel.updatedBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

                <spring:bind path="mnoModel.createdOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
         

           <spring:bind path="mnoModel.updatedOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
		   <tr>
            <td colspan="2" align="center" bgcolor="FBFBFB">&nbsp;</td>
    </tr>
		 
		  <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Service Operator:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="mnoModel.name">
            <c:if test="${not empty param.mnoId}">
            
            <input name="${status.expression}" type="text" size="40" class="textBox" readonly="readonly" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
            </c:if>
            <c:if test="${empty param.mnoId}">
            <input name="${status.expression}" type="text" size="40" class="textBox"  maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
            </c:if>
            </spring:bind></td>
          </tr>

   <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Permission Group:&nbsp;</td>
    <td>

							<select name="permissionGroupId" class="textBox" <c:if test="${not empty param.mnoId}">disabled="disabled"</c:if> 	  >
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
          <tr >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Supplier:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">
                <spring:bind path="mnoModel.supplierId">
                    <select name="${status.expression}" class="textBox" <c:if test="${not empty param.mnoId && mnoModel.supplierId != null}">disabled="disabled"</c:if>>
                    <option value=""/>
                     <c:forEach items="${SupplierModelList}" var="supplierModel">
                       <option value="${supplierModel.supplierId}" <c:if test="${status.value == supplierModel.supplierId}"> selected="selected"</c:if>>
                           ${supplierModel.name}
                       </option>
                     </c:forEach>
                   </select>
                </spring:bind>
              </td>
          </tr>

           <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Contact Name:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">
            <spring:bind path="mnoModel.contactName">
            <input name="${status.expression}" type="text" size="40" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
            </spring:bind></td>
          </tr>
           <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Phone No:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="mnoModel.phoneNo"><input name="${status.expression}" type="text" size="40" class="textBox" maxlength="11" value="${status.value}" onkeypress="return maskNumber(this,event)"/></spring:bind></td>
          </tr>
           <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Fax No:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="mnoModel.fax"><input name="${status.expression}" type="text" size="40" class="textBox" maxlength="11" value="${status.value}" onkeypress="return maskNumber(this,event)"/></spring:bind></td>
          </tr>
          <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Email:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="mnoModel.email"><input name="${status.expression}" type="text" size="40" class="textBox" maxlength="50" value="${status.value}"/></spring:bind></td>
          </tr>
           <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Address1:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="mnoModel.address1"><input name="${status.expression}" type="text" size="40" class="textBox" onkeyup="textAreaLengthCounter(this,250);" maxlength="250" value="${status.value}"/></spring:bind></td>
          </tr>
           <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Address2:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="mnoModel.address2"><input name="${status.expression}" type="text" onkeyup="textAreaLengthCounter(this,250);" size="40" class="textBox" maxlength="250" value="${status.value}"/></spring:bind></td>
          </tr>
           <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">City:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="mnoModel.city"><input name="${status.expression}" onkeypress="return maskAlphaWithSp(this,event)" type="text" size="40" class="textBox" maxlength="50" value="${status.value}"/></spring:bind></td>
          </tr>
           <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">State:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="mnoModel.state"><input name="${status.expression}" onkeypress="return maskAlphaWithSp(this,event)" type="text" size="40" class="textBox" maxlength="50" value="${status.value}"/></spring:bind></td>
          </tr>
          <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Zip Code:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="mnoModel.zip"><input name="${status.expression}" type="text" size="40" class="textBox" maxlength="10" value="${status.value}" onkeypress="return maskAlphaNumeric(this,event)"/></spring:bind></td>
          </tr>
           <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Country:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="mnoModel.country"><input name="${status.expression}" type="text" size="40" class="textBox" maxlength="50" value="${status.value}"/></spring:bind></td>
          </tr>
          <tr >
            <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
            <td align="left" bgcolor="FBFBFB"><spring:bind path="mnoModel.description"><textarea name="${status.expression}" cols="40" rows="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox">${status.value}</textarea></spring:bind></td>
          </tr>
          <tr >
            <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
            <td align="left" bgcolor="FBFBFB"><spring:bind path="mnoModel.comments"><textarea name="${status.expression}" cols="40" rows="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox">${status.value}</textarea></spring:bind></td>
          </tr> 
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Image Path:&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
               <spring:bind path="mnoModel.imagePath">
                 <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50"/>
               </spring:bind>
             </td>
           </tr>
           
<tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="mnoModel.active">
	    <input name="${status.expression}" type="checkbox" value="true" 	
	    <c:if test="${status.value==true}">checked="checked"</c:if>
	    <c:if test="${empty param.mnoId  && empty param._save}">checked="checked"</c:if>
	    <c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
	    />			    
     </spring:bind>
    </td>
  </tr>
          
          <tr>
            <td colspan="2" align="center" bgcolor="FBFBFB"><GenericToolbar:toolbar formName="mnoForm" /></td>
            </tr>
	 
        </table>
        </form>
<script type="text/javascript">



function popupCallback(src,popupName,columnHashMap)
		{
   			
			if(src=="supplier")
  			{
    			document.forms.mnoForm.supplierId.value = columnHashMap.get('PK');
    			document.forms.mnoForm.supplierName.value = columnHashMap.get('name');
  				
 			 }
		
		
		}

function onFormSubmit(theForm) {
  /*  if(!validateFormChar(theForm)){
      	return false;
    }*/
    return validateMnoModel(theForm);
}

</script>


 <v:javascript formName="mnoModel"
    staticJavascript="false" xhtml="true" cdata="false"/>
    <script type="text/javascript"
  charset=" " src="<c:url value="/scripts/validator.jsp"/>"></script>
</body>
</html>
