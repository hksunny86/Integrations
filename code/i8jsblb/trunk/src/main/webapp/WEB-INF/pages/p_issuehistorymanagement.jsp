
<jsp:directive.page import="com.inov8.microbank.common.model.portal.issuemodule.IssueHistoryListViewModel"/>
<!--Author: Rizwan-ur-Rehman -->

<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Issue History Management"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
   </head>
   <body bgcolor="#ffffff">

   	<c:set var="C_OPEN" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_OPEN.toString()%>"/>
	</c:set>
   	<c:set var="C_NEW" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_NEW.toString()%>"/>
	</c:set>
   	<c:set var="D_OPEN" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.DISPUTE_OPEN.toString()%>"/>
	</c:set>
   	<c:set var="D_NEW" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.DISPUTE_NEW.toString()%>"/>
	</c:set>
	
<div class="eXtremeTable">
   		<table class="tableRegion" width="100%">
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold">Transaction ID</td>
		   		<td> 
		   			<c:choose>
		   				<c:when test="${not empty issueHistoryListViewModel.transactionCode}">
				   			<c:out value="${issueHistoryListViewModel.transactionCode}"/>
		   				</c:when>
		   				<c:otherwise>
				   			<c:out value="${issueHistoryListViewModel.custTransCode}"/>
		   				</c:otherwise>
		   			</c:choose>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold">Issue Code</td>
		   		<td>
		   			<c:out value="${issueHistoryListViewModel.issueCode}"/>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 117px; font-weight: bold">Issue Date</td>
		   		<td> 
   					<%
   						IssueHistoryListViewModel issue = (IssueHistoryListViewModel)request.getAttribute("issueHistoryListViewModel");
   						out.println(PortalDateUtils.formatDate(issue.getIssueDate(), "dd/MM/yyyy hh:mm a"));
   					 %>
		   		
		   		<%--
		   			<c:out value="${issueHistoryListViewModel.issueDate}"/>
		   		--%>
				</td>
			</tr>	
			<tr>
   				<td class="tableHeader" style="width: 125px;">Date</td>
   				<td class="tableHeader" style="width: 125px;">Status</td>
				<td class="tableHeader">Comments</td>
			</tr>
	   		<c:forEach items="${issueHistoryList}" var="issueHistoryModel" varStatus="x">
				<c:set var="issueHistoryToIssueTypeStatusId">
					<security:encrypt strToEncrypt="${issueHistoryModel.toIssueTypeStatusId}"/>
				</c:set>

				<c:set var="rowNum" value="even"/>
	   				<c:if test="${x.count%2!=0}">
	   					<c:set var="rowNum" value="odd" scope="page"/>
	   				</c:if>
			   			<tr class="${rowNum}" onmouseover="this.className='highlight'"  onmouseout="this.className='${rowNum}'">
			   				<td>
			   					<%
			   						IssueHistoryListViewModel issueHistory = (IssueHistoryListViewModel)pageContext.getAttribute("issueHistoryModel");
			   						out.println(PortalDateUtils.formatDate(issueHistory.getIssueHistoryDate(), "dd/MM/yyyy hh:mm a"));
			   					 %>
<%--
			   				<fmt:parseDate pattern="yyyy-MM-dd HH:mm:ss.S" var="parsedDateTime">${issueHistoryModel.issueHistoryDate}</fmt:parseDate>
							<c:out value="${issueHistoryModel.issueHistoryDate}"/>
--%>			   					
			   				</td>
			   				<td style="text-align: center">	
	   							<c:choose>
			   						<c:when test="${
			   						issueHistoryToIssueTypeStatusId eq C_NEW 
			   						|| issueHistoryToIssueTypeStatusId eq D_NEW 
			   						|| issueHistoryToIssueTypeStatusId eq C_OPEN 
			   						|| issueHistoryToIssueTypeStatusId eq D_OPEN}">
			   								<c:if test="${issueHistoryToIssueTypeStatusId eq C_NEW}">Charged Back</c:if>
			   								<c:if test="${issueHistoryToIssueTypeStatusId eq D_NEW}">Disputed</c:if>
			   								<c:if test="${issueHistoryToIssueTypeStatusId eq D_OPEN || issueHistoryToIssueTypeStatusId eq C_OPEN}">Escalated</c:if>
					   				</c:when>		
			   							<c:otherwise>
					   						<c:out value="${issueHistoryModel.statusName}"/>
			   							</c:otherwise>
	  							</c:choose>
			   				</td>
			   				<td>	
			   					<c:out value="${issueHistoryModel.comments}"/>
			   				</td>
			   			</tr>	
	   		</c:forEach>
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