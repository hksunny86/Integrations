<!--Author:Asad Hayat-->
<%@ include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="decorator" content="decorator2">
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Agent" />
		<script type="text/javascript">
function popupCallback(src,popupName,columnHashMap)
{

  if (src=="areaName")
  {
    document.forms.distributorForm.areaId.value = columnHashMap.get('PK');
    document.forms.distributorForm.areaName.value = columnHashMap.get('name');
  }


}
</script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
	</head>
	<body>
		<spring:bind path="distributorModel.*">
			<c:if test="${not empty status.errorMessages}">
				<div class="errorMsg">
					<c:forEach var="error" items="${status.errorMessages}">
						<c:out value="${error}" escapeXml="false" />
						<br />
					</c:forEach>
				</div>
			</c:if>
		</spring:bind>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		<table width="100%" border="0" cellpadding="0" cellspacing="1">
			<form id="distributorForm" method="post"
				action="distributorform.html" onsubmit="return onFormSubmit(this)" />

				<c:if test="${not empty param.distributorId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
					<spring:bind path="distributorModel.name">
						<input type="hidden" name="${status.expression}"
							value="${status.value}" />
					</spring:bind>
				</c:if>

				<spring:bind path="distributorModel.distributorId">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>


				<spring:bind path="distributorModel.createdBy">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="distributorModel.updatedBy">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="distributorModel.createdOn">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="distributorModel.updatedOn">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="distributorModel.versionNo">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
			<tr bgcolor="FBFBFB">
				<td colspan="2" align="center">
					&nbsp;
				</td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Agent:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="distributorModel.name">
						<input type="text" name="${status.expression}"
							value="${status.value}"
							onkeypress="return maskAlphaWithSp(this,event)" class="textBox"
							maxlength="50"
							<c:if test="${not empty param.distributorId}">disabled="disabled"</c:if> />
					</spring:bind>
				</td>
			</tr>

			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Permission Group:&nbsp;
				</td>
				<td>

					<select name="permissionGroupId" class="textBox"
						<c:if test="${not empty param.distributorId}">disabled="disabled"</c:if>>
						<c:forEach items="${permissionGroupModelList}"
							var="permissionGroupModelList">
							<option value="${permissionGroupModelList.permissionGroupId}"
								<c:if test="${permissionGroupIdInReq == permissionGroupModelList.permissionGroupId}">selected="selected"</c:if>>
								${permissionGroupModelList.name}
							</option>
						</c:forEach>
					</select>
					${status.errorMessage}
				</td>
			</tr>

			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Area:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="distributorModel.areaId">
						<select name="${status.expression}" class="textBox">
							<c:forEach items="${arearModelList}" var="arearModelList">
								<option value="${arearModelList.areaId}"
									<c:if test="${status.value == arearModelList.areaId}">selected="selected"</c:if> />
									${arearModelList.name}
								</option>
							</c:forEach>
						</select>
					</spring:bind>
				</td>
			</tr>



			<tr bgcolor="FBFBFB">
				<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Contact Name:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="distributorModel.contactName">
						<input type="text" name="${status.expression}"
							value="${status.value}"
							onkeypress="return maskAlphaWithSp(this,event)" class="textBox"
							maxlength="50" />
					</spring:bind>
				</td>
			<tr bgcolor="FBFBFB">
				<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
					Phone No:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="distributorModel.phoneNo">
						<input type="text" name="${status.expression}"
							value="${status.value}"
							onkeypress="return maskNumber(this,event)" class="textBox"
							maxlength="12" />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
					Fax No:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="distributorModel.fax">
						<input type="text" name="${status.expression}"
							value="${status.value}"
							onkeypress="return maskNumber(this,event)" class="textBox"
							maxlength="12" />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
					Email:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="distributorModel.email">
						<input type="text" name="${status.expression}"
							value="${status.value}" class="textBox" maxlength="50" />
					</spring:bind>
				</td>
			</tr>

			<tr bgcolor="FBFBFB">
				<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Address1:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="distributorModel.address1">
						<input type="text" name="${status.expression}"
							value="${status.value}"
							onkeyup="textAreaLengthCounter(this,250);" class="textBox"
							maxlength="250" />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
					Address2:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="distributorModel.address2">
						<input type="text" name="${status.expression}"
							onkeyup="textAreaLengthCounter(this,250);"
							value="${status.value}" class="textBox" maxlength="250" />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
					City:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="distributorModel.city">
						<input type="text" name="${status.expression}"
							value="${status.value}"
							onkeypress="return maskAlphaWithSp(this,event)" class="textBox"
							maxlength="50" />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
					State:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="distributorModel.state">
						<input type="text" name="${status.expression}"
							value="${status.value}"
							onkeypress="return maskAlphaWithSp(this,event)" class="textBox"
							maxlength="50" />
					</spring:bind>
				</td>
			</tr>

			<tr bgcolor="FBFBFB">
				<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
					Zip Code:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="distributorModel.zip">
						<input type="text" name="${status.expression}"
							value="${status.value}"
							onkeypress="return maskAlphaNumeric(this,event)" class="textBox"
							maxlength="10" />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
					Country:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="distributorModel.country">
						<input type="text" name="${status.expression}"
							value="${status.value}"
							onkeypress="return maskAlphaWithSp(this,event)" class="textBox"
							maxlength="50" />
					</spring:bind>
				</td>
			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText">
					Description:&nbsp;
				</td>
				<td align="left">

					<spring:bind path="distributorModel.description">
						<textarea name="${status.expression}" class="textBox"
							onkeyup="textAreaLengthCounter(this,250);"
							onkeypress="return maskCommon(this,event)" cols="50" rows="5">${status.value}</textarea>
					</spring:bind>

				</td>
			</tr>

			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText">
					Comments:&nbsp;
				</td>
				<td align="left">

					<spring:bind path="distributorModel.comments">
						<textarea name="${status.expression}" class="textBox"
							onkeyup="textAreaLengthCounter(this,250);"
							onkeypress="return maskCommon(this,event)" cols="50" rows="5">${status.value}</textarea>
					</spring:bind>

				</td>
			</tr>

<%--			<tr>--%>
<%--				<td align="right" bgcolor="#F3F3F3" class="formText">--%>
<%--					Inov8:&nbsp;--%>
<%--				</td>--%>
<%--				<td align="left" bgcolor="#FBFBFB">--%>
<%--					<spring:bind path="distributorModel.inov8">--%>
<%----%>
<%--						<input type="hidden" name="_${status.expression}" />--%>
<%--						<input type="checkbox" name="${status.expression}" value="true"--%>
<%--							<c:if test="${status.value}">checked="checked" </c:if> />--%>
<%----%>
<%--					</spring:bind>--%>
<%--				</td>--%>
<%--			</tr>--%>
			<tr>
				<td align="right" bgcolor="#F3F3F3" class="formText">
					Is National:&nbsp;
				</td>
				<td align="left" bgcolor="#FBFBFB">
					<spring:bind path="distributorModel.national">
						<c:if test="${not empty param.distributorId}">
							<c:if test="${distributorModel.national}">
								<input type="checkbox" name="${status.expression}" value="true"
									disabled="disabled"
									<c:if test="${status.value}">checked="checked" </c:if> />
								<input type="hidden" name="${status.expression}"
									value="${status.value}" />
							</c:if>
							<c:if test="${not distributorModel.national}">
								<input type="checkbox" name="${status.expression}" value="true" onclick="nationalDistCheck();"
									<c:if test="${status.value}">checked="checked" </c:if> />								
							</c:if>

						</c:if>
						<c:if test="${empty param.distributorId}">
							<input type="checkbox" name="${status.expression}" value="true"
								<c:if test="${status.value}">checked="checked" </c:if> />
						</c:if>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText">
					Active:&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
					<spring:bind path="distributorModel.active">
						<input name="${status.expression}" type="checkbox" value="true"
							<c:if test="${status.value==true}">checked="checked"</c:if>
							<c:if test="${empty param.distributorId && empty param._save }">checked="checked"</c:if>
							<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if> />						
					</spring:bind>
				</td>
			</tr>


			<tr bgcolor="FBFBFB">
				<td height="19" colspan="2" align="center"></td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td colspan="2" align="center">
					<GenericToolbar:toolbar formName="distributorForm" />
				</td>
			</tr>

			</form>
		</table>

		<script type="text/javascript">
if(document.forms[0].isUpdate == null)
    document.forms[0].name.focus();
else
    document.forms[0].areaId.focus();
highlightFormElements();



function onFormSubmit(theForm) {
/*if(!validateFormChar(theForm)){
      		return false;
}*/
return validateDistributorModel(theForm);
}
function nationalDistCheck(){

}
</script>

		<v:javascript formName="distributorModel" staticJavascript="false" />
		<script type="text/javascript"
			src="<c:url
value="/scripts/validator.jsp"/>"></script>

	</body>


</html>
