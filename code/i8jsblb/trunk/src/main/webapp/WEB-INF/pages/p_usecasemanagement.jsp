<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils'%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<html>
	<head>
	<meta name="decorator" content="decorator">
	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>
    
	<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
	<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<%@include file="/common/ajax.jsp"%>
	<meta name="title" content="Usecase Management" />
    <script language="javascript" type="text/javascript">

      function error(request)
      {
      	alert("An unknown error has occured. Please contact with the administrator for more details");
      }
     
    </script>
    <%
		
		String updatePermission = PortalConstants.MNG_USECASE_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;	
		updatePermission +=	"," + PortalConstants.ADMIN_GP_UPDATE;
	 %>
    
    
	</head>
	<body bgcolor="#ffffff">

		<div id="rsp" class="ajaxMsg"></div>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		<ec:table items="usecaseModelList" var="usecase"
		action="${contextPath}/p_usecasemanagement.html"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Usecase Management.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Usecase Management.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Usecase Management"
					fileName="Usecase Management.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Usecase Management.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>		
				<ec:column property="name" title="Name" escapeAutoFormat="True" />
				<ec:column property="description" title="Description" escapeAutoFormat="True"  sortable="false"/>
				<ec:column property="escalationLevels" title="Escalation Levels" escapeAutoFormat="True"  sortable="false"/>
				<ec:column property="authorizationStatus" title="Authorization" escapeAutoFormat="True" style="text-align: center">
				</ec:column>			
				<authz:authorize ifAnyGranted="<%=updatePermission %>">
					<c:choose>
						<c:when test="${!isSuperAdmin && usecase.usecaseId == 1099}">
							<ec:column alias="" title="" style="text-align: center" filterable="false" sortable="false" viewsAllowed="html">
								<input type="button" class="button" value="Edit" disabled="disabled" />
							</ec:column>
						</c:when>
						<c:otherwise>
							<ec:column alias="" title="" style="text-align: center" filterable="false" sortable="false" viewsAllowed="html">
								<input type="button" class="button" value="Edit" 
								onclick="javascript:document.location='p_usecasemanagementform.html?usecaseId=${usecase.usecaseId}'" />
							</ec:column>
						</c:otherwise>
					</c:choose>
					
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=updatePermission %>">	
					<ec:column alias="" title="" style="text-align: center" filterable="false" sortable="false" viewsAllowed="html">
							<input type="button" class="button" value="Edit" disabled="disabled" />
					</ec:column>
				</authz:authorize>	
			</ec:row>
		</ec:table>


		<script language="javascript" type="text/javascript">

			function validateForm(form){
				var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        	var _fDate = form.startDate.value;
		  		var _tDate = form.endDate.value;
		  		var startlbl = "Start Date";
		  		var endlbl   = "End Date";
	        	var isValid = validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,currentDate);

	        	return isValid;
	        }
			
      	</script>
	</body>
</html>
