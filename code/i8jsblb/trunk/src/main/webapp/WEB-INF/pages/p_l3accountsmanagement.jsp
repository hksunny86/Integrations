<!--Title: i8Microbank-->
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<html>
	<head>
		<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
		<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>
		<meta name="decorator" content="decorator">
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="L2 Accounts" />
		<script type="text/javascript">
			var jq=$.noConflict();
			var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		</script>
		<%
			String createPermission = PortalConstants.ADMIN_GP_CREATE;
			createPermission +=	"," + PortalConstants.PG_GP_CREATE;
			createPermission +=	"," + PortalConstants.MNG_L3_ACT_CREATE;
	
			String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
			updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;	
			updatePermission +=	"," + PortalConstants.MNG_L3_ACT_UPDATE;
			
			String readPermission = PortalConstants.ADMIN_GP_UPDATE;
			readPermission +=	"," + PortalConstants.PG_GP_UPDATE;	
			readPermission +=	"," + PortalConstants.MNG_L3_AMDF_READ;
			readPermission +=	"," + PortalConstants.MNG_L3_KYC_READ;
			readPermission +=	"," + PortalConstants.MNG_L3_ACT_READ;

			String agentUpdatePermission = PortalConstants.ADMIN_GP_UPDATE;
			agentUpdatePermission +=	"," + PortalConstants.PG_GP_UPDATE;	
			agentUpdatePermission +=	"," + PortalConstants.MNG_AGNTS_UPDATE;
		 %>
	</head>
	<body bgcolor="#ffffff">
	
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		<html:form name="level3AccountsViewForm" commandName="level3AccountsViewModel" method="post"
			action="p_l3accountsmanagement.html" onsubmit="return validateForm(this)">
			<table width="750px" border="0">
				<tr>
					<td align="right" class="formText">
						User ID:
					</td>
					<td align="left">
						<html:input path="userId" cssClass="textBox" maxlength="10" tabindex="1" onkeypress="return maskNumber(this,event)"/>
					</td>
					<td class="formText" align="right" width="20%">
						Initial App. Form No.:
					</td>
					<td align="left" width="30%">
						<html:input path="initialAppFormNo" cssClass="textBox" maxlength="16" tabindex="3" onkeypress="return maskAlphaNumericWithSpdashforAH(this,event)"/> 
					</td>
				</tr>
				<tr>
					<td class="formText" align="right" width="20%">
						CNIC:
					</td>
					<td align="left" width="30%">
						<html:input path="cnic" cssClass="textBox" maxlength="13" tabindex="2" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right" width="20%">
						Mobile No:
					</td>
					<td align="left" width="30%">
						<html:input path="mobileNo" cssClass="textBox" maxlength="11" tabindex="3" onkeypress="return maskNumber(this,event)"/> 
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Account Type:
					</td>
					<td align="left">
						<html:select path="accountTypeId" cssClass="textBox" tabindex="4">
							<html:option value="">---All---</html:option>
							<c:if test="${accountTypeList != null}">
								<html:options items="${accountTypeList}" itemValue="customerAccountTypeId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
					<td align="right" class="formText">
						Registration State:
					</td>
					<td align="left">
						<html:select path="registrationStateId" cssClass="textBox" tabindex="5">
							<html:option value="">---All---</html:option>
							<c:if test="${registrationStateModelList != null}">
								<html:options items="${registrationStateModelList}" itemValue="registrationStateId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="6" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="7" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td align="right" class="formText">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="8" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="9" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
						<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
						<input name="_search" type="submit" class="button" value="Search" tabindex="10"/>
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_l3accountsmanagement.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="11" />
					</td>
					<td colspan="2">
						&nbsp;
					</td>
				</tr>
			</table>
		</html:form>
		<ec:table items="level3AccountsViewModelList" var="level3AccountsViewModel"
			retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit" action="${contextPath}/p_l3accountsmanagement.html?actionId=${retriveAction}"
			title="" width="100%" filterable="false" sortable="true">
			
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="L3 Accounts.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXls fileName="L3 Accounts.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="L3 Accounts" fileName="L3 Accounts.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="L3 Accounts.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<c:set var="appUserId">
					<security:encrypt strToEncrypt="${level3AccountsViewModel.appUserId}"></security:encrypt>
				</c:set>
				<ec:column property="createdOn" title="Account Creation Date" cell="date" format="dd/MM/yyyy" style="text-align:center;"/>
				<ec:column property="userId" title="User ID"/>
				<ec:column property="initialAppFormNo" title="Initial App. Form No."/>
				<ec:column property="cnic" title="CNIC"  style="text-align:center;"/>
				<ec:column property="mobileNo" style="text-align:center;"/>
				<ec:column property="dormancyRemovedOn" title="Reactivation Date" cell="date" format="dd/MM/yyyy" style="text-align:center;"/>
				<ec:column property="accountTypeName" title="Account Type"/>
				<ec:column property="registrationStateName" title="Registration State"/>
				<ec:column property="appUserId" title=" " viewsAllowed="html" sortable="false">
					<authz:authorize ifAnyGranted="<%=updatePermission %>">
						<c:if test="${not empty level3AccountsViewModel.initialAppFormNo}">
							<input type="button" class="button" value="Edit L2 Account" onclick="javascript: window.location='p_agentmerchantdetailsform.html?initAppFormNumber=${level3AccountsViewModel.initialAppFormNo}'">
									
						</c:if>
						<c:if test="${empty level3AccountsViewModel.initialAppFormNo}">
							<input type="button" class="button" value="Edit L2 Account" disabled="true"/>
						</c:if>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=updatePermission %>">
						<authz:authorize ifAnyGranted="<%=readPermission%>">
							<c:if test="${not empty level3AccountsViewModel.initialAppFormNo}">
								<input type="button" class="button" value="View L3 Account" onclick="javascript: window.location='p_agentmerchantdetailsform.html?initAppFormNumber=${level3AccountsViewModel.initialAppFormNo}'">					
							</c:if>
							<c:if test="${empty level3AccountsViewModel.initialAppFormNo}">
								<input type="button" class="button" value="View L3 Account" disabled="true"/>					
							</c:if>
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=readPermission%>">
								<input type="button" class="button" value="View L3 Account" disabled="true"/>
						</authz:authorize>				
					</authz:authorize>
  				</ec:column>
			</ec:row>
		</ec:table>

		<script language="javascript" type="text/javascript">
		document.forms[0].userId.focus();

  		Calendar.setup(
  		{
	       inputField  : "startDate", // id of the input field
		   button      : "sDate",    // id of the button
	    }
  		);
		Calendar.setup(
	    {
	      inputField  : "endDate", // id of the input field
	      button      : "eDate",    // id of the button
		  isEndDate: true	
	    }
	    );
  	</script>
	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>
