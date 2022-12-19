<%@ include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
       
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

	<style type="text/css">
		label {
		  width: 150px;
		  text-align:right;
		  left: 0;
		  top: 5px; /* x to align nicely with the baseline of the text in the input */
		}
	</style>

	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
	<meta name="title" content="Complaint Nature"/>
	<meta name="decorator" content="decorator">
</head>
<body>

<spring:bind path="complaintSubcategoryModel.*">
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
	<table width="100%" border="0">
	<html:form name="complaintSubcategoryForm" commandName="complaintSubcategoryModel" method="post" action="p_complaintsubcategoryform.html" onsubmit="return onFormSubmit(this);">
		<c:if test="${not empty param.subcategoryId}">
			<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
				value="<%=PortalConstants.ACTION_UPDATE%>" />
		</c:if>
		<c:if test="${empty param.subcategoryId}">
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
				value="<%=PortalConstants.ACTION_CREATE%>" />
		</c:if>
		<input type="hidden" name="subcategoryId" value="${param.subcategoryId}">
			<tr>

				<c:if test="${not empty param.subcategoryId}">
					<td class="formText" align="right" width="25%" bgcolor="F3F3F3">
						<span style="color: #FF0000">*</span>Complaint Category:
					</td>
					<td bgcolor="FBFBFB" width="25%">
						<div class="textBox" style="background: #D3D3D3; font-size: 14px;">${complaintSubcategoryModel.relationComplaintCategoryIdModel.name}</div>
						<input type="hidden" name="complaintCategoryId" value="${complaintSubcategoryModel.complaintCategoryId}">
					</td>
					<td class="formText" align="right" width="25%" bgcolor="F3F3F3">
						<span style="color: #FF0000">*</span>Complaint Nature Name:
					</td>
					<td align="left" bgcolor="FBFBFB" width="25%">
						<div class="textBox" style="background: #D3D3D3; font-size: 14px; height: 35px;">${complaintSubcategoryModel.name}</div>
						<input type="hidden" name="name" value="${complaintSubcategoryModel.name}">
					</td>
				</c:if>
	 			<c:if test="${empty param.subcategoryId}">
		 			<td class="formText" align="right" width="25%" bgcolor="F3F3F3">
						<span style="color: #FF0000">*</span>Complaint Category:
					</td>
					<td bgcolor="FBFBFB" width="25%">
						<html:select path="complaintCategoryId" tabindex="1" cssClass="textBox">
							<html:option value="">---All---</html:option>
								<c:if test="${categoryModelList != null}">
									<html:options items="${categoryModelList}" itemValue="complaintCategoryId" itemLabel="name" />
		 						</c:if>
		 				</html:select>
					</td>
					<td class="formText" align="right" width="25%" bgcolor="F3F3F3">
						<span style="color: #FF0000">*</span>Complaint Nature Name:
					</td>
					<td align="left" bgcolor="FBFBFB" width="25%">
						<html:input path="name" tabindex="2" cssClass="textBox"  maxlength="100"/> 
					</td>
				</c:if>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">
					<span style="color: #FF0000">*</span>Level 0 Assignee:
				</td>
				<td bgcolor="FBFBFB">
					<html:select path="level0Assignee" tabindex="3" cssClass="textBox">
						<html:option value="">---All---</html:option>
							<c:if test="${bankUserList != null}">
								<html:options items="${bankUserList}" itemValue="value" itemLabel="label" />
	 						</c:if>
	 				</html:select> 
				</td>
				<td class="formText" align="right" width="20%" bgcolor="F3F3F3">
					<label><span style="color: #FF0000">*</span>Level 0 TAT:</label>
				</td>
				<td align="left" bgcolor="FBFBFB">
					<html:input path="level0AssigneeTat" tabindex="4" cssClass="textBox" maxlength="3" onkeypress="return maskInteger(this,event)"/>
				</td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">
					<span style="color: #FF0000">*</span>Level 1 Assignee:
				</td>
				<td bgcolor="FBFBFB">
 					<html:select path="level1Assignee" tabindex="5" cssClass="textBox">
						<html:option value="">---All---</html:option>
							<c:if test="${bankUserList != null}">
								<html:options items="${bankUserList}" itemValue="value" itemLabel="label" />
	 						</c:if>
	 				</html:select> 
				</td>
				<td class="formText" align="right" width="20%" bgcolor="F3F3F3">
					<label><span style="color: #FF0000">*</span>Level 1 TAT:</label>
				</td>
				<td align="left" bgcolor="FBFBFB">
					<html:input path="level1AssigneeTat" tabindex="6" cssClass="textBox" maxlength="3" onkeypress="return maskInteger(this,event)"/>
				</td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">
					<span style="color: #FF0000">*</span>Level 2 Assignee:
				</td>
				<td bgcolor="FBFBFB">
					<html:select path="level2Assignee" tabindex="7" cssClass="textBox">
						<html:option value="">---All---</html:option>
							<c:if test="${bankUserList != null}">
								<html:options items="${bankUserList}" itemValue="value" itemLabel="label" />
	 						</c:if>
	 				</html:select> 
				</td>
				<td class="formText" align="right" width="20%" bgcolor="F3F3F3">
					<label><span style="color: #FF0000">*</span>Level 2 TAT:</label>
				</td>
				<td align="left" bgcolor="FBFBFB">
					<html:input path="level2AssigneeTat" tabindex="8" cssClass="textBox" maxlength="3" onkeypress="return maskInteger(this,event)"/>
				</td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">
					<span style="color: #FF0000">*</span>Level 3 Assignee:
				</td>
				<td bgcolor="FBFBFB">
					<html:select path="level3Assignee" tabindex="9" cssClass="textBox">
						<html:option value="">---All---</html:option>
							<c:if test="${bankUserList != null}">
								<html:options items="${bankUserList}" itemValue="value" itemLabel="label" />
	 						</c:if>
	 				</html:select> 
				</td>
				<td class="formText" align="right" width="20%" bgcolor="F3F3F3">
					<label><span style="color: #FF0000">*</span>Level 3 TAT:</label>
				</td>
				<td align="left" bgcolor="FBFBFB">
					<html:input path="level3AssigneeTat" tabindex="10" cssClass="textBox" maxlength="3" onkeypress="return maskInteger(this,event)"/>
				</td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">
					Description:
				</td>
				<td bgcolor="FBFBFB">
					<html:textarea tabindex="11" style="font-family:Arial,Helvetica,sans-serif" path="description" rows="4" cols="21" onkeypress="return textAreaLengthCounter(this,250);"/>
				</td>
				<td class="formText" align="right" width="20%" bgcolor="F3F3F3">
					Active:
				</td>
				<td align="left" bgcolor="FBFBFB">
					<html:checkbox tabindex="12" path="isActive" />
				</td>
			</tr>
			<tr>
				<td class="formText" align="right"></td>
				<td align="left">
						<input name="_search" tabindex="13" type="submit" class="button" value="Submit"/>
						<c:if test="${empty param.subcategoryId}"> 
							<input name="reset" tabindex="14" type="reset" class="button" value="Cancel" onclick="javascript: window.location='p_complaintsubcategorysearch.html?actionId=2'"/>
						</c:if>
						<c:if test="${not empty param.subcategoryId}"> 
							<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_complaintsubcategorysearch.html?actionId=2'" tabindex="14" />
						</c:if>
				</td>
				<td class="formText" align="right"></td>
				<td align="left"></td>
			</tr>
	</html:form>
	</table>
  <script type="text/javascript">

highlightFormElements();

function onFormSubmit(theForm) {
	if(doRequired( theForm.complaintCategoryId, 'Complaint Category' ) && doRequired( theForm.level0Assignee, 'Level 0 Assignee' )
		&& doRequired( theForm.level1Assignee, 'Level 1 Assignee' ) && doRequired( theForm.level2Assignee, 'Level 2 Assignee' )
		&& doRequired( theForm.level3Assignee, 'Level 3 Assignee' ) && doRequired( theForm.name, 'Complaint Nature Name' )
		&& doRequired( theForm.level0AssigneeTat, 'Level 0 TAT' ) && doRequired( theForm.level1AssigneeTat, 'Level 1 TAT' )
		&& doRequired( theForm.level2AssigneeTat, 'Level 2 TAT' ) && doRequired( theForm.level3AssigneeTat, 'Level 3 TAT' ) ) {
		if(isPositiveInt( theForm.level0AssigneeTat, 'Level 0 TAT' ) && isPositiveInt( theForm.level1AssigneeTat, 'Level 1 TAT' )
			&& isPositiveInt( theForm.level2AssigneeTat, 'Level 2 TAT' ) && isPositiveInt( theForm.level3AssigneeTat, 'Level 3 TAT' )) {
			 return true;
			}
	}
    return false;
}

function doRequired( field, label ) {
	if( field.value == '' || field.value.length == 0 ) {
      	alert( label + ' is required field.' );
      	return false;
    }
    return true;
}

function isPositiveInt(field, label) {
   if(!(field.value  % 1 === 0) || field.value <= 0){
   		alert( label + ' must be positive integer.' );
   		return false;
   }
   return true;
}
</script>

</body>
</html>
