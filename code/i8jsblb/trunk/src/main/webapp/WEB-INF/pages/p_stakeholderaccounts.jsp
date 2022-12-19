<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>

<html>
  <head>
<meta name="decorator" content="decorator">

   <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script> 
<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />	    
		<!-- <script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script> -->
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
	<script type="text/javascript">
	var jq=$.noConflict();
	var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
<meta name="title" content="Stakeholder Accounts" />

  </head>
  
  <body bgcolor="#ffffff">
  <%@include file="/common/ajax.jsp"%>
  
  <script language="javascript" type="text/javascript">
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
      </script>
      
      <div id="successMsg" class ="infoMsg" style="display:none;"></div>
      <c:if test="${not empty messages}">
        <div class="infoMsg" id="successMessages">
          <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/>
            <br/>
          </c:forEach>
        </div>
        <c:remove var="messages" scope="session"/>
      </c:if>


<ec:table filterable="false"
		items="stakehldAcctMapList"
        var = "stakehldAcctMapListModel"
        retrieveRowsCallback="limit"
        filterRowsCallback="limit"
        sortRowsCallback="limit"
		action="${pageContext.request.contextPath}/p_stakeholderaccounts.html"
		title="">
		
		
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Stakeholder Accounts.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Stakeholder Accounts.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Stakeholder Accounts"
					fileName="Stakeholder Accounts.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Stakeholder Accounts.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
		<ec:row>
			<ec:column property="blbAccNo" sortable="true" title="BLB Account No" filterable="true"/>
			<ec:column property="blbAccTitle" sortable="true" title="BLB Account Title" escapeAutoFormat="true"/>
			<ec:column property="ofAccNo" sortable="true" title="OF Settlement Account No" escapeAutoFormat="true"/>
			<ec:column property="ofAccTitle" sortable="true" title="OF Settlement Account Title" escapeAutoFormat="true"/>
			<ec:column property="t24AccNo" sortable="true" title="T24 Account No" escapeAutoFormat="true"/>
			<ec:column property="t24AccTitle" sortable="true" title="T24 Account Title" escapeAutoFormat="true"/>		
    	</ec:row>
	</ec:table>
	
	<script language="javascript" type="text/javascript">
      function confirmUpdateStatus(link)
      {
        if (confirm('Are you sure you want to update status?')==true)
        {
          window.location.href=link;
        }
      }
      </script>
	<%-- <script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script> --%>
</body>
</html>
