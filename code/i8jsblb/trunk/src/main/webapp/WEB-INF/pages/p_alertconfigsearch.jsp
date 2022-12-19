<!--Title: i8Microbank-->
<!--Author: Rizwan Munir-->
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
	<script type="text/javascript">
		function actdeact(request) {
			isOperationSuccessful(request);
		}
	</script>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="Search Error Alerts Configurations" />
		<meta name="decorator" content="decorator">
	<style type="text/css">
		table{clear:both;}
	</style>
	</head>

	<body bgcolor="#ffffff">
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
	<spring:bind path="alertsConfigModel.*">
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

		<table width="940px" border="0">
		<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_ALERT_CONFIG_CREATE%>">
			<div align="right"><a href="p_alertconfigform.html?actionId=${createAction}" class="linktext"> Add Alert Configurations </a></div>
		</authz:authorize>
		<html:form name="alertsConfigForm" commandName="alertsConfigModel"  onsubmit="return validateForm()">
			<tr>
				<td class="formText" align="right">Alert Type:</td>
				<td align="left"><html:input path="alertName" tabindex="1" cssClass="textBox" /></td>
				<td class="formText" align="right" width="20%">Failure Rate(%age):</td>
				<td align="left"><html:input path="rate" tabindex="2" cssClass="textBox" onkeypress="return maskNumber(this,event)"/></td>
			</tr>
			<tr>
				<td class="formText" align="right">Frequency(Min.):</td>
				<td align="left"><html:input tabindex="3" path="alertInterval" cssClass="textBox" onkeypress="return maskNumber(this,event)"/></td>
				<td class="formText" align="right">Minimum Requests:</td>
				<td align="left"><html:input tabindex="4" path="alertRequests" cssClass="textBox" onkeypress="return maskNumber(this,event)"/></td>
			</tr>
			<tr>
				<td class="formText" align="right" width="20%">
					Status:
				</td>
				<td align="left">
	 				<html:select cssClass="textBox" tabindex="5" path="isActive">
					   <html:option value="" label="--- All ---"/>
					   <html:options items="${statusList}" />
					</html:select>
				</td>
			</tr>
			<tr>
				<td class="formText" align="right"></td>
				<td align="left">
						<input type="submit" class="button" tabindex="6" value="Search" name="_search" tabindex="6" /> 
						<input type="reset" class="button" tabindex="7" value="Cancel" name="_reset" onclick="javascript: window.location='p_alertconfigsearch.html?actionId=${retriveAction}'" />
				</td>
				<td class="formText" align="right"></td>
				<td align="left"></td>
			</tr>
	</html:form>
	</table>
		
		<ec:table filterable="false" items="alertsConfigModelList"
			var="alertsConfigModel" retrieveRowsCallback="limit"
			filterRowsCallback="limit" sortRowsCallback="limit"
			action="${contextPath}/p_alertconfigsearch.html?actionId=${retriveAction}"
			title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Alert Configuration.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Alert Configuration.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Alert Configuration" fileName="Alert Configuration.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Alert Configuration.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>			
				<ec:column  property="alertName" title="Alert Type"  />
				<ec:column  property="rate" title="Failure Rate(%age)"  style="text-align: right"/>
				<ec:column  property="alertInterval" title="Frequency(Min.)" style="text-align: right"/>
				<ec:column  property="alertRequests" title="Minimum Requests" style="text-align: right"/>
				<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_ALERT_CONFIG_UPDATE%>">
				<ec:column  property="isActive" title="Status" viewsAllowed="html">
					<tags:activatedeactivate 
						id="${alertsConfigModel.alertsConfigId}" 
						model="com.inov8.microbank.common.model.AlertsConfigModel" 
						property="isActive"
						propertyValue="${alertsConfigModel.isActive}"
						callback="actdeact"
						error="defaultError"
					/>
				</ec:column>
				<ec:column alias="Edit" style="text-align: center" viewsAllowed="html" filterable="false" sortable="false" width="50px">
					<a onclick="javascript:window.location.href='${contextPath}/p_alertconfigform.html?alertsConfigId=${alertsConfigModel.alertsConfigId}';" >Edit</a>
				</ec:column>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=PortalConstants.MNG_ALERT_CONFIG_UPDATE%>">
					<ec:column  property="isActive" title="Status" viewsAllowed="html">
						<c:if test="${alertsConfigModel.isActive==true}">Active</c:if>
						<c:if test="${alertsConfigModel.isActive==false}">Inactive</c:if>
					</ec:column>
				</authz:authorize>
			</ec:row>
		</ec:table>
		<script language="javascript" type="text/javascript">
		    highlightFormElements();
	        function validateForm(){
	        	return validateFormChar(document.forms[0]);
	        }	
			</script>
	</body>
</html>