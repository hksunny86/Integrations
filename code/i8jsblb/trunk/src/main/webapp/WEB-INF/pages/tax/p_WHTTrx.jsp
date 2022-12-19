<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils'%>
<%@ page import='com.inov8.microbank.common.util.FinancialInstitutionConstants'%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="veriflyFinancialInstitution"><%=FinancialInstitutionConstants.VERIFLY_FINANCIAL_INSTITUTION %></c:set>

<html>
	<head>
<meta name="decorator" content="decorator">
	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    
	<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
	<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<%@include file="/common/ajax.jsp"%>
	<meta name="title" content="WHT Transaction Details" />
	</head>
	<body bgcolor="#ffffff" onunload="javascript:closeChild();">

		<div id="rsp" class="ajaxMsg"></div>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
			<html:form name='WHTTrxViewForm' commandName="extendedWHTTrxViewModel" method="post"
				action="${contextPath}/p_WHTTrx.html" onsubmit="return onFormSubmit(this);" >
				<table width="770px" border="0">
				<tr>
					<td class="formText" width="20%" align="right">
						Mobile #::
					</td>
					<td align="left" width="30%" >
						<html:input path="mobNo" id="mobNo" cssClass="textBox" maxlength="11" tabindex="1" onkeypress="return maskInteger(this,event)"/>
					</td>
					<td align="right" class="formText">Transaction Status</td>
            	<td align="left">
                <html:select path="status" cssClass="textBox" onchange="onAppUserTypeId(this);" tabindex="2">
                        <html:option value="">[All]</html:option>
                        <html:option value="0">Pending</html:option>
                        <html:option value="1">Success</html:option>
                </html:select>
            </td>
				</tr>
				        <tr>
            <td align="right" class="formText">User Type</td>
            <td align="left">
                <html:select path="appUserTypeId" cssClass="textBox" onchange="onAppUserTypeId(this);" tabindex="3">
                    <html:option value="">[All]</html:option>
                    <c:if test="${appUserTypeModelList != null}">
                        <html:options items="${appUserTypeModelList}" itemLabel="name" itemValue="appUserTypeId"/>
                    </c:if>
                </html:select>
            </td>
            <td align="right" class="formText">
                Registration Status:
            </td>
            <td align="left">
                <html:select path="regStateId" cssClass="textBox" tabindex="4">
                    <html:option value="">[All]</html:option>
                    <c:if test="${registrationStateModelList != null}">
                        <html:options items="${registrationStateModelList}" itemLabel="name" itemValue="registrationStateId"/>
                    </c:if>
                </html:select>
            </td>
        </tr>

				  <tr>
            <td class="formText" align="right">
                Start Date:
            </td>
            <td align="left">
                <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="sDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0"/>
                <img id="sDate" tabindex="6" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0"/>
            </td>
            <td class="formText" align="right">
                End Date:
            </td>
            <td align="left">
                <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="eDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0"/>
                <img id="eDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0"/>
            </td>
        </tr>
        
        
        
				 <tr>
            <td align="right" class="formText">&nbsp;
            </td>
            <td align="left" colspan="2" class="formText">
                <input type="submit" class="button" id="searchButton" value="Search" tabindex="9"/>
                <input type="reset" class="button" value="Cancel" name="_reset" tabindex="10" onclick="javascript: window.location='p_WHTTrx.html'"/>
            </td>
            </td>
        </tr>
				</table>
			</html:form>
		

		<ec:table items="WHTTrxViewModelList" var="WHTTrxViewModel"
		action="${contextPath}/p_WHTTrx.html "
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="View Transactions.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="View Transactions.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="View Transactions"
					fileName="View Transactions.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="View Transactions.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>

 <ec:row>
        <ec:column property="mobNo" title="Mobile #" escapeAutoFormat="true" style="text-align: center"/>
        <ec:column property="name" title="Name"/>
        <ec:column property="appUserType" title="User Type"/>
        <ec:column property="regState" title="Registration Status"/>
        <ec:column property="whtConfig" title="Transaction Type"/>
        <ec:column style="text-align: right"  property="amount" title="Amount"  cell="currency"  format="###,###,##0.00" />
        <ec:column format="dd-MM-yyyy" cell="date" property="createdOn" title="Settlement Date" style="text-align: center"/>
        <ec:column format="dd-MM-yyyy" cell="date" property="transactionDate" title="Transaction Date" style="text-align: center"/>
        <ec:column property="desc" title="Description"/>
        <ec:column property="statusName" title="Transaction Status"/>
    </ec:row>
		</ec:table>

		

		<script language="javascript" type="text/javascript">

		function onFormSubmit(form)
	    {
	        var isValid=true;
	        var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        var _startDate = form.startDate.value;
	        var _endDate = form.endDate.value;
	        serverDate= getJsDate( currentDate );
	        startlbl = "Start Date";
	        endlbl   = "End Date";

	        if(_startDate!=undefined && _startDate!="" )
	        {
	            var fDate = getJsDate( _startDate );
	            if(fDate > serverDate){
	                alert(startlbl+" can't be in future.");
	                isValid = false;
	            }
	        }

	        if(_endDate!=undefined && _endDate!="")
	        {
	            var tDate = getJsDate( _endDate );
	            if(tDate > serverDate){
	                alert(endlbl+" can't be in future.");
	                isValid = false;
	            }
	        }

	        if(_startDate!=undefined && _startDate!="" && _endDate!=undefined  && _endDate!="" )
	        {
	            var fDate = getJsDate( _startDate );
	            var tDate = getJsDate( _endDate );
	            if(!(fDate<=tDate)) {
	                alert(startlbl+" should be less than or equal to "+endlbl);
	                isValid = false;
	            }
	        }

	        return isValid;
	    }

	    Calendar.setup(
	            {
	                inputField: "startDate", // id of the input field
	                button: "sDate"    // id of the button
	            }
	    );
	    Calendar.setup(
	            {
	                inputField: "endDate", // id of the input field
	                button: "eDate",    // id of the button
	                isEndDate: true
	            }
	    );
	    </script>
      	
	</body>
</html>
