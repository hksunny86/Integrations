<%@page import="java.util.Date"%>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>

<html>
  <head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Complaints List" />

  </head>
  
  <body bgcolor="#ffffff">
   <c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

    	<ec:table 
		items="complaintModelList" 
                var = "complaintModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/p_billpaycomplaints.html?actionId=${retriveAction}"
				title="" filterable="false">
		<ec:row>
		
		    <ec:column property="createdOn" title="Initiated On" filterable="false" cell="date" format="dd/MM/yyyy hh:mm a" width="14%"/>
			<ec:column property="complaintCode" title="Complaint ID" filterable="false" width="9%" escapeAutoFormat="true">
				<a href="${pageContext.request.contextPath}/p_complaintDetailForm.html?complaintId=${complaintModel.complaintId}&actionId=${retriveAction}">
					<c:out value="${complaintModel.complaintCode}"></c:out>
				</a>
		    </ec:column>
		    <ec:column  property="complaintCategory" title="Category"  filterable="false" width="12%"/>
			<ec:column  property="complaintSubcategory" title="Nature" filterable="false" />
			<ec:column  property="transactionId" title="Transaction ID" filterable="false" escapeAutoFormat="true"/>
			<ec:column  property="initiatorId" title="Customer / Agent ID" filterable="false" width="9%" escapeAutoFormat="true"/>
			<ec:column  property="initiatorType" title="Type" filterable="false" width="9%"/>
			<ec:column  property="initiatorCNIC" title="CNIC" filterable="false" width="10%" escapeAutoFormat="true"/>
			<ec:column  property="mobileNo" title="Mobile No" filterable="false" width="9%" escapeAutoFormat="true"/>
			<ec:column  property="status" title="Status" filterable="false" width="7%"/>
		    <ec:column property="expectedTat" title="Expected TAT" filterable="false" cell="date" format="dd/MM/yyyy hh:mm a" width="14%"/>
			<ec:column  property="escalationStatus" title="Escalation Level" filterable="false" width="7%"/>
			<ec:column  property="currentAssigneeName" title="Current Assignee" filterable="false" width="12%"/>
			<ec:column  property="level0AssigneeName" title="Default Assignee" filterable="false" width="12%"/>
			<ec:column  property="level0TATEndTime" title="Default TAT Ends On" filterable="false" cell="date" format="dd/MM/yyyy hh:mm a" width="12%"/>
			<ec:column  property="level1AssigneeName" title="Esc. Level 1 Assignee" filterable="false" width="12%"/>
			<ec:column  property="level1TATEndTime" title="Level 1 TAT Ends On" filterable="false" cell="date" format="dd/MM/yyyy hh:mm a" width="12%"/>
			<ec:column  property="level2AssigneeName" title="Esc. Level 2 Assignee" filterable="false" width="12%"/>
			<ec:column  property="level2TATEndTime" title="Level 2 TAT Ends On" filterable="false" cell="date" format="dd/MM/yyyy hh:mm a" width="12%"/>
			<ec:column  property="level3AssigneeName" title="Esc. Level 3 Assignee" filterable="false" width="12%"/>
			<ec:column  property="level3TATEndTime" title="Level 3 TAT Ends On" filterable="false" cell="date" format="dd/MM/yyyy hh:mm a" width="12%"/>
        </ec:row>
	</ec:table>
    
	<input type="button" class="button" value="Back" tabindex="48" onclick="javascript: window.location.href='home.html'" />
   
  </body>
</html>
