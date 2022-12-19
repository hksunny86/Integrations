<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
	    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
        
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="Search Agent Group"/>
		<script language="javascript" type="text/javascript">
			var jq=$.noConflict();
			var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		
      function error(request)
      {
      	alert("An unknown error has occured. Please contact with the administrator for more details");
      }
    </script>
		
	</head>
	<body bgcolor="#ffffff">
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
			<html:form name="agentGroupVOForm" commandName="agentTaggingViewModel" method="post" action="p_searchAgentGroup.html" onsubmit="return validateForm(this)" >
				<table width="750px" border="0">
				<tr>
					<td align="right" class="formText">
						Parent ID:
					</td>
					<td align="left" colspan="3">
						<html:input path="parrentId" id="parrentId" cssClass="textBox" maxlength="50" tabindex="3" onkeypress="return maskNumber(this,event)"/>
						${status.errorMessage}
					</td>
				</tr>
				<tr>
					<td class="formText" width="23%" align="right">Group Title :</td>
					<td align="left" width="30%" ><html:input path="groupTitle" id="groupTitle" cssClass="textBox" maxlength="50" tabindex="1"/></td>
					<td class="formText" align="right"></td>
					<td align="left" ></td>
				</tr>
				<tr>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="9"/>
						<input name="reset" type="reset" class="button" value="Cancel" tabindex="10" />
					</td>
					<td colspan="2">
						&nbsp;
					</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</table>
			</html:form>
		

		<ec:table items="agentTaggingViewModelList" var="agentTaggingViewModel" action="${contextPath}/p_searchAgentGroup.html?actionId=${retriveAction}"
					title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
		
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Agent Group Report.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Agent Group Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Agent Group Report" fileName="Agent Group Report.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Agent Group Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			

			<c:set var="disabled" scope="request" value="disabled='disabled'"/>
			<authz:authorize ifAnyGranted="<%=PortalConstants.AGENT_GRP_U%>">
				<c:set var="disabled" scope="request" value=""/>
			</authz:authorize>
			
			<ec:row>
			
				<ec:column property="groupTitle" title="Group Title" style="text-align: center"/>
				<ec:column property="parrentId" title="Parent ID" style="text-align: center"/>
				<ec:column property="mobileNumber" title="Mobile #" style="text-align: center"/>
				<ec:column property="businessName" title="Business Name" style="text-align: center"/>
										
				<ec:column alias=" " viewsAllowed="html" filterable="false" sortable="false">
								
					<input type="button" class="button" value="Edit" ${disabled}
						onclick="javascript: window.location='p_createAgentGroup.html?groupId=${agentTaggingViewModel.pk}'" />
						
				</ec:column>
			</ec:row>
			
		</ec:table>

	</body>
</html>
