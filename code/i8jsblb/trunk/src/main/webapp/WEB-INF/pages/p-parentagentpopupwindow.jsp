<!--Author: Mohammad Shehzad Ashraf -->
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>

<html>
  <head>
    
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Search Parent" />
<%@include file="/common/ajax.jsp"%>
	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
	<script type="text/javascript">
	function callback(parentName,parentRetailerId,ultimateAgentName,ultimateAgentId,retailerId)
	{
		if(window.opener && !opener.closed && null != opener.popupCallback)
	  	{
			opener.popupCallback(parentName,parentRetailerId,ultimateAgentName,ultimateAgentId,retailerId); 
	 	}
	  	window.close();
	}
	</script>

  </head>
  
  <body bgcolor="#ffffff">
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
	<div id="errorMsg" class ="errorMsg" style="display:none;"></div>  
   <c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
    
    
    	<ec:table 
		items="parentAgentsList" 
                var = "parentAgentModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/p-parentagentpopupwindow.html?actionId=${retriveAction}"
				title="">
				
		
		
		<ec:row	onmouseover="this.style.cursor='pointer'" onclick = "callback('${parentAgentModel.parentAgentName}','${parentAgentModel.parentRetailerContactId}',
		'${parentAgentModel.ultimateAgentName}','${parentAgentModel.ultimateRetailerContactId}','${parentAgentModel.retailerId}')">
		    <ec:column property="parentAgentName" title="Agent Name" filterable="true"/>
			<ec:column property="parentAgentId" title="Agent ID" filterable="true"/>
			<ec:column property="mobileNo" title="Mobile No" filterable="true"/>	
			<ec:column property="cnic" title="NIC" filterable="true"/>
			<ec:column property="ultimateAgentName" title="Ultimate Parent" filterable="true"/>
       </ec:row>
	</ec:table>
  </body>
</html>
