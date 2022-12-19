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
	<meta name="title" content="Tax Regime Default FED Rates" />
    <script language="javascript" type="text/javascript">

      function error(request)
      {
      	alert("An unknown error has occured. Please contact with the administrator for more details");
      }
     
    </script>
    <%
    	String createPermission = PortalConstants.ADMIN_GP_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.MNG_TAX_REGIME_C;
		
		String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;	
		updatePermission +=	"," + PortalConstants.MNG_TAX_REGIME_U;
		
		
		String readPermission = PortalConstants.ADMIN_GP_READ;
		readPermission +=	"," + PortalConstants.PG_GP_READ;	
		readPermission +=	"," + PortalConstants.MNG_TAX_REGIME_R;
		
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
		
		<authz:authorize ifAnyGranted="<%=createPermission %>">
   			<div align="right"><a href="p_taxRegimeform.html?actionId=1" class="linktext"> Add Tax Regime Default Rates </a><br>&nbsp;&nbsp;</div>
		</authz:authorize>
		
		<ec:table items="taxRegimeModelList" var="taxRegime"
		action="${contextPath}/p_taxregimedefaultfedrate.html"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Tax Regime Default FED Rates.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Tax Regime Default FED Rates.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Tax Regime Default FED Rates"
					fileName="Tax Regime Default FED Rates.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Tax Regime Default FED Rates.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>		
				<ec:column property="name" title="Territory/Province" style="text-align: center" escapeAutoFormat="True" />
				<ec:column property="fed" title="FED (%)" style="text-align: center" escapeAutoFormat="True"  sortable="true"/>
				<c:if test="${taxRegime.isActive}">
					<ec:column property="isActive" title="Status" style="text-align: center" escapeAutoFormat="True" sortable="false" value="Active" />
				</c:if>
				<c:if test="${!taxRegime.isActive}">
					<ec:column property="isActive" title="Status" style="text-align: center" escapeAutoFormat="True" sortable="false" value="In-active" />
				</c:if>
			<authz:authorize ifAnyGranted="<%=updatePermission %>">
				<ec:column alias="" title="" style="text-align: center" filterable="false" sortable="false" viewsAllowed="html">
					<input type="button" class="button" value="Edit" 
					onclick="javascript:document.location='p_taxRegimeform.html?taxRegimeId=${taxRegime.taxRegimeId}'" />
				</ec:column>
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
