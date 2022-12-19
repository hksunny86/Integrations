<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@ page import='com.inov8.microbank.common.util.FinancialInstitutionConstants'%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>

	<head>
	    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
	    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
	    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
	    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
	    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
	    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
	    <link type="text/css" rel="stylesheet" href="styles/ajaxtags.css"/>
    	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	    <%@include file="/common/ajax.jsp"%>
	    <meta name="title" content="Mark/Unmark History" />
	    <meta name="decorator" content="decorator">
	    <script language="javascript" type="text/javascript">
	        var jq=$.noConflict();
	        var serverDate ="<%=PortalDateUtils.getServerDate()%>";
	
	
	        function error(request) {
	            alert("An unknown error has occured. Please contact with the administrator for more details");
	        }
	    </script>
	
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
	<!-- <div class="eXtremeTable"> -->
	
	 <html:form name='blacklistMarkUnmarkHistoryForm'
               commandName="blacklistMarkUnmarkHistoryModel" method="post"
               action="p_blacklistMarkUnmarkHistory.html" onsubmit="return validateForm(this)" >
     
  
         <ec:table items="bulkManualAdjustmentModelList" var="blacklistMarkUnmarkHistoryModel"
              action="${contextPath}/p_blacklistMarkUnmarkHistory.html?cnicNo=${model.cnic}?actionId=${retriveAction}"
              title=""
              
              filterable="false">
           
	         <ec:row>
	         
		         <ec:column property="cnicNo" title="CNIC No" escapeAutoFormat="true" style="text-align: center"/>
		         <ec:column property="updatedBy" title="Updated By" escapeAutoFormat="true" style="text-align: center"/>
		         <ec:column property="updatedOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Updated On"/>
		         <ec:column property="action" title="Action" escapeAutoFormat="true" style="text-align: center"/>
		         <ec:column property="comments" title="Comments" escapeAutoFormat="true" style="text-align: center"/>
	         
	         </ec:row>
       
              
         </ec:table>          
               
         <table>
         	<tr>
         		<td>
         		<input type="button" class="button"  value="Back" tabindex="7"  onclick="javascript:document.location='p_blacklistmarking.html?actionId=<%=PortalConstants.ACTION_RETRIEVE%>';" />
         			
         		</td>
         	</tr>
         </table>      
               
               
               
     </html:form>
	
	
	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>