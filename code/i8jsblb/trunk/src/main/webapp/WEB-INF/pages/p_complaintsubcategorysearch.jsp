<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Rizwan Munir-->
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="actionId"><%=PortalConstants.KEY_ACTION_ID %></c:set>
<c:set var="updateAction"><%=PortalConstants.ACTION_UPDATE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>
<html>

<head>
	<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
	<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
	<script type="text/javascript">
		var jq=$.noConflict();
		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		function actdeact(request) {
			isOperationSuccessful(request);
		}
	</script>
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<meta name="title" content="Search Complaint Nature" />
	<meta name="decorator" content="decorator">
	<style type="text/css">
	table {
		clear: both;
	}
	</style>
</head>

<body bgcolor="#ffffff">
	<div id="successMsg" class="infoMsg" style="display:none;"></div>
	<spring:bind path="complaintSubcategoryViewModel.*">
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
		
		<html:form name="complaintSubcategoryForm" commandName="complaintSubcategoryViewModel"  onsubmit="return validateForm(this)">
			<table width="940px" border="0">
				<authz:authorize ifAnyGranted="<%=PortalConstants.ADD_COMPLAINT_NATURE%>">
					<div align="right"><a href="p_complaintsubcategoryform.html?actionId=${createAction}" class="linktext"> Add Complaint Nature </a></div>
				</authz:authorize>
				<tr>
				<td class="formText" align="right">
					Complaint Category:
				</td>
				<td align="left">
					<html:select path="complaintCategoryId" tabindex="1" cssClass="textBox">
						<html:option value="">---All---</html:option>
							<c:if test="${categoryModelList != null}">
								<html:options items="${categoryModelList}" itemValue="complaintCategoryId" itemLabel="name" />
	 						</c:if>
	 				</html:select>
				</td>
				<td class="formText" align="right" width="20%">
					Complaint Nature Name:
				</td>
				<td align="left">
					<html:input path="complaintSubcategoryName" tabindex="2" cssClass="textBox"/>
				</td>
			</tr>
			<tr>
				<td class="formText" align="right">
					Level 0 Assignee:
				</td>
				<td align="left">
		        	<html:select path="level0AssigneeId" tabindex="3" cssClass="textBox">
						<html:option value="">---All---</html:option>
							<c:if test="${assigneel0List != null}">
								<html:options items="${assigneel0List}" itemValue="value" itemLabel="label" />
	 						</c:if>
	 				</html:select>
				</td>
				<td class="formText" align="right" width="20%">
					Level 1 Assignee:
				</td>
				<td align="left">
					<html:select path="level1AssigneeId" tabindex="4" cssClass="textBox">
						<html:option value="">---All---</html:option>
							<c:if test="${assigneel1List != null}">
								<html:options items="${assigneel1List}" itemValue="value" itemLabel="label" />
	 						</c:if>
	 				</html:select>
				</td>
			</tr>
			<tr>
				<td class="formText" align="right">
					Level 2 Assignee:
				</td>
				<td align="left">
		        	<html:select path="level2AssigneeId" tabindex="5" cssClass="textBox">
						<html:option value="">---All---</html:option>
							<c:if test="${assigneel2List != null}">
								<html:options items="${assigneel2List}" itemValue="value" itemLabel="label" />
	 						</c:if>
	 				</html:select>
				</td>
				<td class="formText" align="right" width="20%">
					Level 3 Assignee:
				</td>
				<td align="left">
					<html:select path="level3AssigneeId" tabindex="6" cssClass="textBox">
						<html:option value="">---All---</html:option>
							<c:if test="${assigneel3List != null}">
								<html:options items="${assigneel3List}" itemValue="value" itemLabel="label" />
	 						</c:if>
	 				</html:select>
				</td>
			</tr>
			<tr>
				<td class="formText" align="right" width="20%">
					Status:
				</td>
				<td align="left">
	 				<html:select  cssClass="textBox" path="isActive" tabindex="7">
					   <html:option value="" label="--- All ---"/>
					   <html:options items="${statusList}" />
					</html:select>
				</td>
			</tr>
			<tr>
				<td class="formText" align="right"></td>
				<td align="left">
						<input type="submit" class="button" value="Search" name="_search" tabindex="8" /> 
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_complaintsubcategorysearch.html?actionId=2'" tabindex="9" />
				</td>
				<td class="formText" align="right"></td>
				<td align="left"></td>
			</tr>
		</table>
	</html:form>
		
		<ec:table filterable="false" items="complaintSubcategoryModelList"
			var="complaintSubcategoryViewModel" retrieveRowsCallback="limit"
			filterRowsCallback="limit" sortRowsCallback="limit"
			action="${contextPath}/p_complaintsubcategorysearch.html?actionId=${retriveAction}"
			title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Complaint Nature.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Complaint Nature.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Complaint Natures" fileName="Complaint Nature.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Complaint Nature.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>			
				<ec:column  property="complaintSubcategoryName" title="Name"  />
				<ec:column  property="complaintCategoryName" title="Category Name"  />
				<ec:column  property="totalTat" title="Total TAT (Hour)" style="text-align: right"/>
				<ec:column  property="level0AssigneeName" title="Level 0 Assignee" />
				<ec:column  property="level0AssigneeTat" title="Level 0 Assignee TAT (Hour)" style="text-align: right"/>
				<ec:column  property="level1AssigneeName" title="Level 1 Assignee" />
				<ec:column  property="level1AssigneeTat" title="Level 1 Assignee TAT (Hour)" style="text-align: right"/>
				<ec:column  property="level2AssigneeName" title="Level 2 Assignee" />
				<ec:column  property="level2AssigneeTat" title="Level 2 Assignee TAT (Hour)" style="text-align: right"/>
				<ec:column  property="level3AssigneeName" title="Level 3 Assignee" />
				<ec:column  property="level3AssigneeTat" title="Level 3 Assignee TAT (Hour)" style="text-align: right"/>
				<authz:authorize ifAnyGranted="<%=PortalConstants.UPDATE_COMPLAINT_NATURE%>">
				<ec:column  property="isActive" title="Status" viewsAllowed="html">
					<tags:activatedeactivate 
						id="${complaintSubcategoryViewModel.complaintSubcategoryId}" 
						model="com.inov8.microbank.common.model.ComplaintSubcategoryModel" 
						property="isActive"
						propertyValue="${complaintSubcategoryViewModel.isActive}"
						callback="actdeact"
						error="defaultError"
					/>
				</ec:column>
				<ec:column alias="Edit" viewsAllowed="html" filterable="false" sortable="false" width="50px" style="text-align: center">
					<a onclick="javascript:window.location.href='${contextPath}/p_complaintsubcategoryform.html?subcategoryId=${complaintSubcategoryViewModel.complaintSubcategoryId}';" >Edit</a>
				</ec:column>
				</authz:authorize>

			</ec:row>
		</ec:table>
		<script language="javascript" type="text/javascript">
		    highlightFormElements();
		</script>
		<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>
