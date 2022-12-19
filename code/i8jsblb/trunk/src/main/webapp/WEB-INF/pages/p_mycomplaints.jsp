<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>

<html>
  <head>
<meta name="decorator" content="decorator">
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="My Complaints" />
<script language="javascript" type="text/javascript">
var jq=$.noConflict();
var serverDate ="<%=PortalDateUtils.getServerDate()%>";
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
        </script>

	<%
		String readPermission = PortalConstants.CSR_GP_READ;
		readPermission +=	"," + PortalConstants.PG_GP_READ;
	%>
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

        
          <html:form name='complaintListForm'
				commandName="complaintReportModel" method="post"
				action="p_mycomplaints.html?actionId=${retriveAction}"
				onsubmit="return validateForm(this);">
          <table width="800px">
          <tr>
              <td align="right" class="formText" width="23%">Complaint ID:</td>
              <td width="30%"><html:input path="complaintCode" onkeypress="return maskInteger(this,event)" id="complaintCode"
 							cssClass="textBox" tabindex="1" maxlength="10" /></td>
			  <td align="right" class="formText" width="18%">Complaint Category:</td>
              <td>
                  <html:select path="complaintCategoryId" tabindex="2" cssClass="textBox">
					<html:option value="">---All---</html:option>
						<c:if test="${categoryModelList != null}">
							<html:options items="${categoryModelList}" itemValue="complaintCategoryId" itemLabel="name" />
 						</c:if>
 					</html:select>
              </td>
          </tr>
          <tr>
              <td align="right" class="formText">Customer/Agent/Handler ID:</td>
              <td>
				<html:input path="initiatorId" id="initiatorId" cssClass="textBox" maxlength="12" tabindex="3"  onkeypress="return maskInteger(this,event)"/>
			  </td>
              <td align="right" class="formText">Complaint Status:</td>
              <td>
                   <html:select path="status" tabindex="4" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<html:option value="Assigned">Assigned</html:option>
							<html:option value="Resolved">Resolved</html:option>
							<html:option value="Overdue">Overdue</html:option>
							<html:option value="Declined">Declined</html:option>
 						</html:select>
              </td>
          </tr>
          <tr>
              <td align="right" class="formText">CNIC:</td>
              <td>
				<html:input path="initiatorCNIC" id="initiatorCNIC" cssClass="textBox" maxlength="13" tabindex="5" onkeypress="return maskInteger(this,event)"/>
              </td>
              <td align="right" class="formText">Escalation Level:</td>
              <td>
                   <html:select path="escalationStatus" tabindex="6" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<html:option value="Default">Default</html:option>
							<html:option value="Level 1">Level 1</html:option>
							<html:option value="Level 2">Level 2</html:option>
							<html:option value="Level 3">Level 3</html:option>
							<html:option value="Overdue">Overdue</html:option>
 						</html:select>
              </td>
          </tr>
          <tr>
              <td align="right" class="formText">Mobile No:</td>
              <td>
				<html:input path="mobileNo" id="mobileNo" cssClass="textBox" maxlength="11" tabindex="7" onkeypress="return maskInteger(this,event)"/>
              </td>
              <td align="right" class="formText">Transaction ID</td>
              <td>
              	<html:input path="transactionId" cssClass="textBox" tabindex="7" maxlength="12" onkeypress="return maskInteger(this,event)"/>
			  </td>
          </tr>
          <tr>
              <td align="right" class="formText">Logged From:</td>
              <td>
				<spring:bind path="complaintReportModel.loggedFrom">
					<input type="text" name="${status.expression}" class="textBox" id="${status.expression}" maxlength="50" value="${status.value}" readonly="readonly" tabindex="8" />
				</spring:bind>
				<img id="fDate" tabindex="37" name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" tabindex="9" />
				<img id="fDate" tabindex="37" name="popcal" title="Clear Date" onclick="javascript:$('loggedFrom').value=''" align="middle" style="cursor:pointer" tabindex="10" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
              </td>
              <td align="right" class="formText">To:</td>
              <td>
				<spring:bind path="complaintReportModel.loggedTo">
					<input type="text" name="${status.expression}" class="textBox" id="${status.expression}" maxlength="50" value="${status.value}" readonly="readonly" tabindex="11" />
				</spring:bind>
				<img id="tDate" tabindex="37" name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" tabindex="12" />
				<img id="tDate" tabindex="37" name="popcal" title="Clear Date" onclick="javascript:$('loggedTo').value=''" align="middle" style="cursor:pointer" tabindex="13" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
              </td>
          </tr>
          <tr>
             <td>
             </td>
             <td>
                <input name="_search" type="submit" class="button" tabindex="14"
									value="Search" />
				<input name="reset" type="reset" class="button" tabindex="15"
							value="Cancel"
							onclick="javascript: window.location='p_mycomplaints.html?actionId=${retriveAction}'" />
             </td>
          </tr>
           </table>
        </html:form>
     
  
    
    	<ec:table 
		items="complaintModelList" 
                var = "complaintModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/p_mycomplaints.html?actionId=${retriveAction}"
				title="" filterable="false" width="1000px">
		<authz:authorize ifAnyGranted="<%=PortalConstants.EXPORT_XLS_READ%>">
			<ec:exportXls fileName="My Complaints.xls" tooltip="Export Excel"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="My Complaints.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="My Complaints"
					fileName="MyComplaints.pdf" tooltip="Export PDF"  />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="MyComplaints.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
		<ec:row>
		
		    <ec:column property="createdOn" title="Initiated On" filterable="false" cell="date" format="dd/MM/yyyy hh:mm a" width="14%"/>
			<ec:column property="complaintCode" title="Complaint ID" filterable="false" width="9%">
				<a href="${pageContext.request.contextPath}/p_complaintDetailForm.html?complaintId=${complaintModel.complaintId}&actionId=${retriveAction}">
					<c:out value="${complaintModel.complaintCode}"></c:out>
				</a>
		    </ec:column>
		    <ec:column  property="complaintCategory" title="Category"  filterable="false" width="12%"/>
			<ec:column  property="complaintSubcategory" title="Nature" filterable="false" />
			<ec:column  property="transactionId" title="Transaction ID" filterable="false" />
			<ec:column  property="initiatorId" title="Customer / Agent / Handler ID" filterable="false" width="9%"/>
			<ec:column  property="initiatorType" title="Type" filterable="false" width="9%"/>
			<ec:column  property="initiatorCNIC" title="CNIC" filterable="false" width="10%"/>
			<ec:column  property="mobileNo" title="Mobile No" filterable="false" width="9%"/>
			<ec:column  property="currentAssigneeName" title="Current Assignee" filterable="false" width="12%"/>
			<ec:column  property="status" title="Complaint Status" filterable="false" width="7%"/>
			<ec:column  property="escalationStatus" title="Escalation Level" filterable="false" width="7%"/>
			<ec:column property="expectedTat" title="Expected TAT" filterable="false" cell="date" format="dd/MM/yyyy hh:mm a" width="14%"/>
        </ec:row>
	</ec:table>
    
    
    
	
    <script language="javascript" type="text/javascript">

         function openConcernWindow(concernCode, concernId, paramRecipientPartnerId, paramInitiatorPartnerId)
			{
		        var popupWidth = 650;
		 		var popupHeight = 400;
		 		var popupLeft = (window.screen.width - popupWidth)/2;
		 		var popupTop = (window.screen.height - popupHeight)/2;
		        newWindow=window.open('p_opconcernsdetail.html?actionId=${retriveAction}&concernCode='+concernCode+'&concernId='+concernId+'&paramRecipientPartnerId='+paramRecipientPartnerId+'&paramInitiatorPartnerId='+paramInitiatorPartnerId,'ConcernDetail','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) newWindow.focus();
			    if(newWindow.opener == null){
			    	newWindow.close();
			    }
			    return false;
			}
   	  Calendar.setup( {inputField  : "loggedFrom",button : "fDate"} );
	  Calendar.setup( {inputField  : "loggedTo",button : "tDate", isEndDate: true } );
            
            
	</script>
	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
  </body>
</html>
