
<jsp:directive.page import="com.inov8.microbank.common.model.ActionLogModel"/>
<!--Author: M Shehzad Ashraf -->

<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Action Log Detail"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
   </head>
   <body bgcolor="#ffffff">
	
<div class="eXtremeTable">
   		<table class="tableRegion" width="115%">
			<tr class="tableHeader" style="padding: 0px;" >
		     <td class="tableHeader" style="text-align:left;" colspan="2" align="left">Audit Log</td>
	        </tr>
			<tr>
				<td width="30%" class="titleRow" style=" font-weight: bold">Audit&nbsp;Log&nbsp;ID</td>
		   		<td width="70%" class="odd"> 
		   			<c:choose>
		   				<c:when test="${not empty auditLogModel.auditLogId}">
				   			<c:out value="${auditLogModel.auditLogId}"/>
		   				</c:when>
		   				<c:otherwise>
				   			<c:out value="${auditLogModel.auditLogId}"/>
		   				</c:otherwise>
		   			</c:choose>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Transaction&nbsp;Code&nbsp;ID</td>
		   		<td width="70%" class="even">
		   			<c:out value="${auditLogModel.transactionCodeId}"/>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Transaction&nbsp;Code</td>
		   		<td width="70%" class="odd">
		   			<c:out value="${auditLogModel.transactionCodeIdTransactionCodeModel.code}"/>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Input Param</td>
		   		<td width="70%" class="even">
		   			<c:out value="${auditLogModel.inputParam}"/>
			  </td>
			</tr>
		
		    <tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Output Param</td>
		   		<td width="70%" class="odd">
		   			<c:out value="${auditLogModel.outputParam}"/>
			  </td>
			</tr>
		
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Custom Field 1</td>
		   		<td width="70%" class="even">
		   			<c:out value="${auditLogModel.customField1}"/>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Custom Field 2</td>
		   		<td width="70%" class="odd">
		   			<c:out value="${auditLogModel.customField2}"/>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Custom Field 3</td>
		   		<td width="70%" class="even">
		   			<c:out value="${auditLogModel.customField3}"/>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Custom Field 4</td>
		   		<td width="70%" class="odd">
		   			<c:out value="${auditLogModel.customField4}"/>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Custom Field 5</td>
		   		<td width="70%" class="even">
		   			<c:out value="${auditLogModel.customField5}"/>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Custom Field 6</td>
		   		<td width="70%" class="odd">
		   			<c:out value="${auditLogModel.customField6}"/>
			  </td>
			</tr>	
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Custom Field 7</td>
		   		<td width="70%" class="even">
		   			<c:out value="${auditLogModel.customField7}"/>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Custom Field 8</td>
		   		<td width="70%" class="odd">
		   			<c:out value="${auditLogModel.customField8}"/>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Custom Field 9</td>
		   		<td width="70%" class="even">
		   			<c:out value="${auditLogModel.customField9}"/>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Custom Field 10</td>
		   		<td width="70%" class="odd">
		   			<c:out value="${auditLogModel.customField10}"/>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Start Time</td>
		   		<td width="70%" class="even">
		   			<c:out value="${auditLogModel.transactionStartTime}"/>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">End Time</td>
		   		<td width="70%" class="odd">
		   			<c:out value="${auditLogModel.transactionEndTime}"/>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Action Log ID</td>
		   		<td width="70%" class="even">
		   		    <c:if test="${not empty auditLogModel.actionLogId}">
		   			    <c:out value="${auditLogModel.actionLogId}"/>
		   			</c:if>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">User Name</td>
		   		<td width="70%" class="odd">
		   		    <c:if test="${not empty auditLogModel.actionLogIdActionLogModel.userName}">
		   			    <c:out value="${auditLogModel.actionLogIdActionLogModel.userName}"/>
		   			</c:if>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="font-weight: bold">Integration&nbsp;Module&nbsp;ID</td>
		   		<td width="70%" class="even">
		   		    <c:if test="${not empty auditLogModel.integrationModuleId}">
		   			    <c:out value="${auditLogModel.integrationModuleId}"/>
		   			</c:if>
			    </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="width: 117px; font-weight: bold">Integration&nbsp;Module</td>
		   		<td width="70%" class="odd">
		   		    <c:if test="${not empty auditLogModel.integrationModuleIdIntegrationModuleModel.name}">
		   			    <c:out value="${auditLogModel.integrationModuleIdIntegrationModuleModel.name}"/>
		   			</c:if>
			  </td>
			</tr>
			<tr>
				<td width="30%" class="titleRow" style="font-weight: bold">Integration&nbsp;Partner&nbsp;Identifier</td>
		   		<td width="70%" class="even">
		   		    <c:if test="${not empty auditLogModel.integrationPartnerIdentifier}">
		   			    <c:out value="${auditLogModel.integrationPartnerIdentifier}"/>
		   			</c:if>
			  </td>
			</tr>
  </table>

   	   		<table width="100%">
	   		<tr align="center">
	   			<td align="center" style="padding-top: 10px">
				   		<input type ="button" class="popbutton" value = "Close Window" onclick = "window.close();"/>
	   			</td>
	   		</tr>
   		</table>

   </div>
	</body>
</html>