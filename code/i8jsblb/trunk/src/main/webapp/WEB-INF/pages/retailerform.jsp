<!--Title: i8Microbank-->

<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@ include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator2">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

<script language="javascript" type="text/javascript">
function popupCallback(src,popupName,columnHashMap)
{
  if (src=="retailerTypeName")
  {
    document.forms.RetailerForm.retailerTypeId.value = columnHashMap.get('PK');
    document.forms.RetailerForm.retailerTypeName.value = columnHashMap.get('name');
  }
//  if (src=="distributorName")
//  {
//    document.forms.RetailerForm.distributorId.value = columnHashMap.get('PK');
//    document.forms.RetailerForm.distributorName.value = columnHashMap.get('name');
//  }
  if (src=="areaName")
  {
    document.forms.RetailerForm.areaId.value = columnHashMap.get('PK');
    document.forms.RetailerForm.areaName.value = columnHashMap.get('name');
  }


}
</script>

<script language="javascript" type="text/javascript">
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
      </script>
<meta name="title" content="Franchise/Branch"/>


</head>
<body>

<%@include file="/common/ajax.jsp"%>
<spring:bind path="retailerModel.*">
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

<form id="RetailerForm" method="post" action="retailerform.html" onSubmit="return onFormSubmit(this)">
<table width="100%"  border="0" cellpadding="0" cellspacing="1">
 <c:if test="${not empty param.retailerId}">
          <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
          <spring:bind path="retailerModel.name">
             <input name="${status.expression}" type="hidden" value="${status.value}"  />
          </spring:bind>
          <spring:bind path="retailerModel.areaId">
             <input name="${status.expression}" type="hidden" value="${status.value}"  />
          </spring:bind>
          <spring:bind path="retailerModel.distributorId">
             <input name="${status.expression}" type="hidden" value="${status.value}"  />
          </spring:bind>
 </c:if>
 <c:if test="${empty param.retailerId}">
          <spring:bind path="retailerModel.distributorId">
             <input name="${status.expression}" type="hidden" value="${singleDistributorModel.distributorId}"  />
          </spring:bind>
 </c:if>
        <spring:bind path="retailerModel.retailerId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>
        <spring:bind path="retailerModel.versionNo">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="retailerModel.createdBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="retailerModel.updatedBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

           <spring:bind path="retailerModel.createdOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

           <spring:bind path="retailerModel.updatedOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
  <tr>
    <td colspan="2" align="center" bgcolor="FBFBFB">&nbsp;</td>
  </tr>
  <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText"><span style="color:#FF0000">*</span>Franchise/Branch:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="retailerModel.name">
    <input name="${status.expression}" type="text" size="40" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaNumericWithSp(this,event)" <c:if test="${not empty param.retailerId}">disabled="disabled"</c:if> />
    </spring:bind>
    </td>
  </tr>
	<tr>
    <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Agent Type:&nbsp;</td>
    <td width="50%" align="left" bgcolor="FBFBFB">
      <spring:bind path="retailerModel.retailerTypeId">
        <select name="${status.expression}" class="textBox" id="${status.expression}">
          <c:forEach items="${retailerTypeModelList}" var="retailerTypeModelList">
            <option value="${retailerTypeModelList.retailerTypeId}"
              <c:if test="${retailerTypeModelList.retailerTypeId == retailerModel.retailerTypeId}">selected="selected"</c:if>>
              ${retailerTypeModelList.name}
            </option>
          </c:forEach>

        </select>
      </spring:bind>
    </td>
  </tr>
  
     <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Permission Group:&nbsp;</td>
    <td>

		<select name="permissionGroupId" class="textBox" <c:if test="${not empty param.retailerId}">disabled="disabled"</c:if> 	  >
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
    <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Area:&nbsp;</td>
    <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="retailerModel.areaId">
        <select name="${status.expression}" class="textBox"
							id="${status.expression}" <c:if test="${not empty param.retailerId}">disabled="disabled"</c:if> >
							
								<c:forEach items="${areaModelList}" var="areaModelList">
									<option value="${areaModelList.areaId}"
										<c:if test="${status.value == areaModelList.areaId}">selected="selected"</c:if>>
										${areaModelList.name}
									</option>
								</c:forEach>
						</select>
                </spring:bind>
                

  </tr>

<!--   <tr>
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Distributor:&nbsp;</td>
    <td align="left" bgcolor="fbfbfb">
      <spring:bind path="retailerModel.distributorId">
			    <select name="${status.expression}" class="textBox"
							id="${status.expression}" <c:if test="${not empty param.retailerId}">disabled="disabled"</c:if>  >
							<c:if test="${empty distributorModelList}">
							        	<option value="" />
							        </c:if>				
							        
							     <c:if test="${not empty distributorModelList}">   
								<c:forEach items="${distributorModelList}" var="distributorModelList">
									
									<option value="${distributorModelList.distributorId}"
										<c:if test="${status.value == distributorModelList.distributorId}">selected="selected"</c:if>>
										${distributorModelList.name}
									</option>
								</c:forEach>
								 </c:if>
						</select>


      </spring:bind>
        </td>
  </tr>
 -->  
  <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText"><span style="color:#FF0000">*</span>Contact Name:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="retailerModel.contactName"><input name="${status.expression}" type="text" size="40" class="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" value="${status.value}"></spring:bind></td>
  </tr>
  <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText">Phone No:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="retailerModel.phoneNo"><input name="${status.expression}" type="text" size="40" class="textBox" onkeypress="return maskNumber(this,event)" maxlength="11" value="${status.value}"></spring:bind></td>
  </tr>
   <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText">Fax No:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="retailerModel.fax"><input name="${status.expression}" type="text" size="40" onkeypress="return maskNumber(this,event)" class="textBox" maxlength="11" value="${status.value}"></spring:bind></td>
  </tr>
   <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText">Email:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="retailerModel.email"><input name="${status.expression}" type="text" size="40" class="textBox" maxlength="50" value="${status.value}"></spring:bind></td>
  </tr>
  <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText"><span style="color:#FF0000">*</span>Address1:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="retailerModel.address1"><input name="${status.expression}" type="text" onkeyup="textAreaLengthCounter(this,250);" size="40" class="textBox" maxlength="250" value="${status.value}"></spring:bind></td>
  </tr>
  <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText">Address2:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="retailerModel.address2"><input name="${status.expression}" type="text" onkeyup="textAreaLengthCounter(this,250);" size="40" class="textBox" maxlength="250" value="${status.value}"></spring:bind></td>
  </tr>
   <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText">City:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="retailerModel.city"><input name="${status.expression}" type="text" size="40" class="textBox" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50" value="${status.value}"></spring:bind></td>
  </tr>
   <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText">Province:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="retailerModel.state"><input name="${status.expression}" type="text" size="40" onkeypress="return maskAlphaWithSp(this,event)" class="textBox" maxlength="50" value="${status.value}"></spring:bind></td>
  </tr>
    <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText">Zip Code:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="retailerModel.zip"><input name="${status.expression}" onkeypress="return maskAlphaNumeric(this,event)" type="text" size="40" class="textBox" maxlength="10" value="${status.value}"></spring:bind></td>
  </tr>
   <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText">Country:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="retailerModel.country"><input name="${status.expression}" type="text" onkeypress="return maskAlphaWithSp(this,event)" size="40" class="textBox" maxlength="50" value="${status.value}"></spring:bind></td>
  </tr>
   
	<tr>
    <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Product Catalog:&nbsp;</td>
    <td width="50%" align="left" bgcolor="FBFBFB">
      <spring:bind path="retailerModel.productCatalogueId">
        <select name="${status.expression}" class="textBox">
          <c:forEach items="${catalogModelList}" var="catalogModelList">
            <option value="${catalogModelList.productCatalogId}"
              <c:if test="${catalogModelList.productCatalogId == retailerModel.productCatalogueId}">selected="selected"</c:if>>
              ${catalogModelList.name}
            </option>
          </c:forEach>

        </select>
      </spring:bind>
    </td>
  </tr>

   <tr>
            <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
            <td align="left" bgcolor="FBFBFB"><spring:bind path="retailerModel.description"><textarea name="${status.expression}" cols="50" rows="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox">${status.value}</textarea></spring:bind></td>
          </tr>
<tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="retailerModel.active">
	    <input name="${status.expression}" type="checkbox" value="true" 	
	    <c:if test="${status.value==true}">checked="checked"</c:if>
	    <c:if test="${empty param.retailerId && empty param._save}">checked="checked"</c:if>
	    <c:if test="${status.value==false && not empty param._save }">unchecked="checked"</c:if>
	    />			    
     </spring:bind>
    </td>
  </tr>
  <tr>
    <td colspan="2" align="center" bgcolor="FBFBFB">
    				<input type= "button" name = "_save" value="  Save  " onclick="javascript:onSave(document.forms.RetailerForm,null);" class="button"/>		  
 <c:if test="${empty param.retailerId}">
<input type= "button" name = "_cancel" value=" Cancel " onclick="javascript:revertDropdowns(document.forms.RetailerForm);" class="button"/>
</c:if>
 <c:if test="${not empty param.retailerId}">
<input type= "reset" name = "_cancel" value=" Cancel "  class="button" />
</c:if>
    
  </tr>
</table>
</form>

<!-- <ajax:select source="areaId" target="distributorId" baseUrl="${contextPath}/retailerformrefdata.html" parameters="areaId={areaId},rType=1" errorFunction="error" /> -->


<script type="text/javascript">


function revertDropdowns(theForm)
{
	
	
	var obj = new AjaxJspTag.Select(
	"/i8Microbank/retailerformrefdata.html", {
	parameters: "areaId={areaId},rType=1",
	target: "distributorId",
	source: "areaId",
	errorFunction: error
	});
	theForm.reset();
	obj.execute();
	
	

}

function onFormSubmit(theForm) {
    //alert('in the on form submit');
    /*if(!validateFormChar(theForm)){
      	return false;
    }*/
    return validateRetailerModel(theForm);
}

</script>

<v:javascript formName="retailerModel" staticJavascript="false"/>
<script type="text/javascript" src="<c:url
value="/scripts/validator.jsp"/>"></script>
</body>
</html>
