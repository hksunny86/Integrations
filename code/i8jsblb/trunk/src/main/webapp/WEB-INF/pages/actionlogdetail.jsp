
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
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
   </head>
   <body bgcolor="#ffffff">
	
<div class="eXtremeTable">
   		<table class="tableRegion" width="100%">
			<tr class="tableHeader" style="padding: 0px;" >
		     <td class="tableHeader" style="text-align:left;" colspan="2" align="left">Action Log</td>
	        </tr>
			
			<tr>
				
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">User&nbsp;Name</td>
		   		<td class="odd"> 
		   			<c:choose>
		   				<c:when test="${not empty actionLogModel.userName}">
				   			<c:out value="${actionLogModel.userName}"/>
		   				</c:when>
		   				<c:otherwise>
				   			<c:out value="${actionLogModel.userName}"/>
		   				</c:otherwise>
		   			</c:choose>
				</td>
			</tr>

			<tr>

				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Client&nbsp;IP&nbsp;Address</td>
				<td class="odd">
					${actionLogModel.clientIpAddress}"
				</td>
			</tr>

			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Action&nbsp;Log&nbsp;ID</td>
		   		<td class="even">
		   			<c:out value="${actionLogModel.actionLogId}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Device&nbsp;User&nbsp;ID</td>
		   		<td class="odd">
		   			<c:out value="${actionLogModel.deviceUserId}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Input XML</td>
		   		<td class="even">
		   			<c:out value="${actionLogModel.inputXml}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Output XML</td>
		   		<td class="odd">
		   			<c:out value="${actionLogModel.outputXml}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Custom&nbsp;Field&nbsp;1</td>
		   		<td class="even">
		   			<c:out value="${actionLogModel.customField1}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Custom Field 2</td>
		   		<td class="odd">
		   			<c:out value="${actionLogModel.customField2}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Custom Field 3</td>
		   		<td class="even">
		   			<c:out value="${actionLogModel.customField3}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Custom Field 4</td>
		   		<td class="odd">
		   			<c:out value="${actionLogModel.customField4}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Custom Field 5</td>
		   		<td class="even">
		   			<c:out value="${actionLogModel.customField5}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Custom Field 6</td>
		   		<td class="odd">
		   			<c:out value="${actionLogModel.customField6}"/>
				</td>
			</tr>	
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Custom Field 7</td>
		   		<td class="even">
		   			<c:out value="${actionLogModel.customField7}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Custom Field 8</td>
		   		<td class="odd">
		   			<c:out value="${actionLogModel.customField8}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Custom Field 9</td>
		   		<td class="even">
		   			<c:out value="${actionLogModel.customField9}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Custom Field 10</td>
		   		<td class="odd">
		   			<c:out value="${actionLogModel.customField10}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Start Time</td>
		   		<td class="even">
		   			<c:out value="${actionLogModel.startTime}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">End Time</td>
		   		<td class="odd">
		   			<c:out value="${actionLogModel.endTime}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold; white-space:nowrap">Action Name</td>
		   		<td class="even">
		   		    <c:if test="${not empty actionLogModel.actionId}">
		   			    <c:out value="${actionLogModel.actionIdActionModel.name}"/>
		   			</c:if>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold">Use&nbsp;Case&nbsp;Name</td>
		   		<td class="odd">
		   		    <c:if test="${not empty actionLogModel.usecaseId}">
		   			    <c:out value="${actionLogModel.usecaseIdUsecaseModel.name}"/>
		   			</c:if>
				</td>  
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold">Action&nbsp;Status&nbsp;Name</td>
		   		<td class="even">
		   		    <c:if test="${not empty actionLogModel.actionStatusId}">
		   			    <c:out value="${actionLogModel.actionStatusIdActionStatusModel.name}"/>
		   			</c:if>
				</td>  
			</tr>
		</table>
   		
   		<table width="100%">
	   		<tr align="center">
	   			<td align="center" style="padding-top: 10px">
				   		<input type ="button" class="button" class="button" value = "Close Window" onclick = "window.close();"/>
	   			</td>
	   		</tr>
   		</table>

   	</div>
	</body>
</html>