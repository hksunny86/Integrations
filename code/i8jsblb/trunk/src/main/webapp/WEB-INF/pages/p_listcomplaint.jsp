<%@page import="java.util.Date"%>
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
<meta name="title" content="Complaints List" />
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
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

       
          <html:form name='complaintReportForm'
				commandName="complaintReportModel" method="post"
				action="p_listcomplaint.html?actionId=${retriveAction}"
				onsubmit="return validateForm(this)">
				 <table width="800px">
          <tr>
          	  <td align="right" class="formText">Transaction ID</td>
              <td>
              	<html:input path="transactionId" cssClass="textBox" tabindex="15" maxlength="12" onkeypress="return maskInteger(this,event)"/>
			  </td>	
          </tr>		
          <tr>
              <td align="right" class="formText" width="23%">Complaint ID:</td>
              <td width="30%"><html:input path="complaintCode"  onkeypress="return maskInteger(this,event)" id="complaintCode"
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
              <td align="right" class="formText">Current Assignee:</td>
              <td>
				<html:select path="currentAssigneeId" tabindex="4" cssClass="textBox">
					<html:option value="">---All---</html:option>
					<c:if test="${currentAssigneeList != null}">
						<html:options items="${currentAssigneeList}" itemValue="value" itemLabel="label" />
					</c:if>
				</html:select>
              </td>
          </tr>
          <tr>
              <td align="right" class="formText">CNIC:</td>
              <td>
				<html:input path="initiatorCNIC" id="initiatorCNIC" cssClass="textBox" maxlength="13" tabindex="5" onkeypress="return maskInteger(this,event)"/>
              </td>
              <td align="right" class="formText">Default Assignee:</td>
              <td>
				<html:select path="level0AssigneeId" tabindex="6" cssClass="textBox">
					<html:option value="">---All---</html:option>
					<c:if test="${assigneel0List != null}">
						<html:options items="${assigneel0List}" itemValue="value" itemLabel="label" />
					</c:if>
				</html:select>
              </td>
          </tr>
          <tr>
              <td align="right" class="formText">Mobile No:</td>
              <td>
				<html:input path="mobileNo" id="mobileNo" cssClass="textBox" maxlength="11" tabindex="7" onkeypress="return maskInteger(this,event)"/>
              </td>
              <td align="right" class="formText">Escalation L1 Assignee:</td>
              <td>
				<html:select path="level1AssigneeId" tabindex="8" cssClass="textBox">
					<html:option value="">---All---</html:option>
					<c:if test="${assigneel1List != null}">
						<html:options items="${assigneel1List}" itemValue="value" itemLabel="label" />
					</c:if>
				</html:select>
              </td>
          </tr>
          <tr>
              <td align="right" class="formText">Complaint Status:</td>
              <td>
                   <html:select path="status" tabindex="9" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<html:option value="Assigned">Assigned</html:option>
							<html:option value="Resolved">Resolved</html:option>
							<html:option value="Overdue">Overdue</html:option>
							<html:option value="Declined">Declined</html:option>
 						</html:select>
              </td>
              <td align="right" class="formText">Escalation L2 Assignee:</td>
              <td>
				<html:select path="level2AssigneeId" tabindex="10" cssClass="textBox">
					<html:option value="">---All---</html:option>
					<c:if test="${assigneel2List != null}">
						<html:options items="${assigneel2List}" itemValue="value" itemLabel="label" />
					</c:if>
				</html:select>
              </td>
          </tr>
          <tr>
              <td align="right" class="formText">Escalation Level:</td>
              <td>
                   <html:select path="escalationStatus" tabindex="11" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<html:option value="Default">Default</html:option>
							<html:option value="Level 1">Level 1</html:option>
							<html:option value="Level 2">Level 2</html:option>
							<html:option value="Level 3">Level 3</html:option>
							<html:option value="Overdue">Overdue</html:option>
 						</html:select>
              </td>
              <td align="right" class="formText">Escalation L3 Assignee:</td>
              <td>
				<html:select path="level3AssigneeId" tabindex="12" cssClass="textBox">
					<html:option value="">---All---</html:option>
					<c:if test="${assigneel3List != null}">
						<html:options items="${assigneel3List}" itemValue="value" itemLabel="label" />
					</c:if>
				</html:select>
              </td>
          </tr>
          <tr>
              <td align="right" class="formText">Logged From:</td>
              <td>
				<spring:bind path="complaintReportModel.loggedFrom">
					<input type="text" name="${status.expression}" class="textBox" id="${status.expression}" maxlength="50" value="${status.value}" readonly="readonly" tabindex="13" />
				</spring:bind>
				<img id="fDate" tabindex="37" name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" tabindex="14" />
				<img id="fDate" tabindex="37" name="popcal" title="Clear Date" onclick="javascript:$('loggedFrom').value=''" align="middle" style="cursor:pointer" tabindex="15" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
              </td>
              <td align="right" class="formText">To:</td>
              <td>
				<spring:bind path="complaintReportModel.loggedTo">
					<input type="text" name="${status.expression}" class="textBox" id="${status.expression}" maxlength="50" value="${status.value}" readonly="readonly" tabindex="16" />
				</spring:bind>
				<img id="tDate" tabindex="37" name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" tabindex="17" />
				<img id="tDate" tabindex="37" name="popcal" title="Clear Date" onclick="javascript:$('loggedTo').value=''" align="middle" style="cursor:pointer" tabindex="18" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
              </td>            
          </tr>
          <tr>       
              <td align="right" class="formText">&nbsp;</td>
              <td>&nbsp;</td>
          </tr>
          <tr>
             <td>
             </td>
             <td>
                <input name="_search" type="submit" class="button" tabindex="19"
									value="Search" />
				<input name="reset" type="reset" class="button" tabindex="20"
							value="Cancel"
							onclick="javascript: window.location='p_listcomplaint.html?actionId=${retriveAction}'" />
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
				action="${pageContext.request.contextPath}/p_listcomplaint.html?actionId=${retriveAction}"
				title="" filterable="false">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Complaints List.xls" tooltip="Export Excel"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Complaints List.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
		<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Complaint List"
				fileName="Complaints List.pdf" tooltip="Export PDF"  />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Complaints List.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>	
		<ec:row>
		
		    <ec:column property="createdOn" title="Initiated On" filterable="false" cell="date" format="dd/MM/yyyy hh:mm a" width="14%"/>
			<ec:column property="complaintCode" title="Complaint ID" filterable="false" width="9%" escapeAutoFormat="true">
				<a href="${pageContext.request.contextPath}/p_complaintDetailForm.html?complaintId=${complaintModel.complaintId}&actionId=${retriveAction}">
					<c:out value="${complaintModel.complaintCode}"></c:out>
				</a>
		    </ec:column>
		    <ec:column  property="complaintCategory" title="Category"  filterable="false" width="12%"/>
			<ec:column  property="complaintSubcategory" title="Nature" filterable="false" />
			<ec:column  property="transactionId" title="Transaction ID" filterable="false" escapeAutoFormat="true" />
			<ec:column  property="initiatorId" title="Customer / Agent / Handler ID" filterable="false" width="9%" escapeAutoFormat="true"/>
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
    
    
    
	
    <script language="javascript" type="text/javascript">
        
   	  Calendar.setup( {inputField  : "loggedFrom", button : "fDate"} );
	  Calendar.setup( {inputField  : "loggedTo",button : "tDate", isEndDate: true} );
            
            
	</script>
	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
  </body>
</html>
