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
		<meta name="title" content="Search Alerts Recipients" />
		<meta name="decorator" content="decorator">
	<style type="text/css">
		table{clear:both;}
	</style>
	</head>

	<body bgcolor="#ffffff">
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
	<spring:bind path="alertsRecipientsModel.*">
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
		<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_ALERT_RECIPIENT_CREATE%>">
			<div align="right"><a href="p_alertsrecepientsform.html?actionId=${createAction}" class="linktext"> Add Alert Recipient </a></div>
		</authz:authorize>
		<html:form name="alertsRecipientsForm" commandName="alertsRecipientsModel"  onsubmit="return validateForm()">

			<tr>
				<td class="formText" align="right">Name:</td>
				<td align="left"><html:input path="name" tabindex="1" cssClass="textBox" /></td>
				<td class="formText" align="right" width="20%">Mobile No:</td>
				<td align="left"><html:input path="mobileNo" tabindex="2" cssClass="textBox" onkeypress="return maskNumber(this,event)" maxlength="11"/></td>
			</tr>
			<tr>
				<td class="formText" align="right">Email ID:</td>
				<td align="left"><html:input path="emailId" tabindex="3" cssClass="textBox"/></td>
				<td class="formText" align="right" width="20%">Alert Type:</td>
				<td align="left">
					<html:select path="alertConfigId" tabindex="4" cssClass="textBox">
						<html:option value="">---All---</html:option>
							<c:if test="${alertsConfigModelList != null}">
								<html:options items="${alertsConfigModelList}" itemValue="alertsConfigId" itemLabel="alertName" />
	 						</c:if>
	 				</html:select>
				</td>
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
						<input type="reset" class="button" tabindex="7" value="Cancel" name="_reset" onclick="javascript: window.location='p_alertsrecepientssearch.html?actionId=${retriveAction}'" tabindex="7" />
				</td>
				<td class="formText" align="right"></td>
				<td align="left"></td>
			</tr>
	</html:form>
	</table>
		
		<ec:table filterable="false" items="alertsRecipientsModelList"
			var="alertsRecipientsModel" retrieveRowsCallback="limit"
			filterRowsCallback="limit" sortRowsCallback="limit"
			action="${contextPath}/p_alertsrecepientssearch.html?actionId=${retriveAction}"
			title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Alert Recepients.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Alert Recepients.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Alert Recepients" fileName="Alert Recepients.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Alert Recepients.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>			
				<ec:column  property="name" title="Name"  />
				<ec:column  property="mobileNo" title="Mobile No."  style="text-align: right;"/>
				<ec:column  property="emailId" title="Email ID"/>
				<ec:column  property="alertConfigIdAlertsConfigModel.alertName" title="Alert Type" />
				<ec:column  property="description" title="Description" />
				<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_ALERT_RECIPIENT_UPDATE%>">
				<ec:column  property="isActive" title="Status" viewsAllowed="html">
					<tags:activatedeactivate 
						id="${alertsRecipientsModel.alertsRecipientsId}" 
						model="com.inov8.microbank.common.model.AlertsRecipientsModel" 
						property="isActive"
						propertyValue="${alertsRecipientsModel.isActive}"
						callback="actdeact"
						error="defaultError"
					/>
				</ec:column>
				<ec:column alias="Edit" style="text-align: center;" viewsAllowed="html" filterable="false" sortable="false" width="50px">
					<a onclick="javascript:window.location.href='${contextPath}/p_alertsrecepientsform.html?alertsRecipientsId=${alertsRecipientsModel.alertsRecipientsId}';" >Edit</a>
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