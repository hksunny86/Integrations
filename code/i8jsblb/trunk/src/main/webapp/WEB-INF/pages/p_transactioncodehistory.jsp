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
	<meta name="title" content="Transaction Code History" />
    <script language="javascript" type="text/javascript">

      function error(request)
      {
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
		<table width="750px" border="0">
			<html:form name='extendedTransactionCodeHtrViewModelForm' commandName="extendedTransactionCodeHtrViewModel" method="post"
				action="p_transactioncodehistory.html" onsubmit="return validateForm(this)" >
				<tr>
					<td class="formText" width="20%" align="right">
						Transaction ID:
					</td>
					<td align="left" width="30%" >
						<html:input path="code" id="transactionCode" cssClass="textBox" maxlength="50" tabindex="1" onkeypress="return maskNumber(this,event)"/>
					</td>
					<td class="formText" width="20%" align="right">
						
					</td>
					<td align="left" width="30%" >
					</td>
				</tr>
				

				<tr>
					<td class="formText" align="right">
						Updated By:
					</td>
					<td align="left">
						<html:input path="username" id="username" cssClass="textBox" tabindex="5" maxlength="50"/>
					
					</td>
					<td class="formText" align="right">
						Action:
					</td>
					<td align="left" >
						<html:select path="usecaseId" tabindex="1" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<c:if test="${usecaseModelList != null}">
								<html:options items="${usecaseModelList}" itemValue="usecaseId" itemLabel="name" />
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						<span class="asterisk">*</span>Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						<span class="asterisk">*</span>End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_transactioncodehistory.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="13" />
					</td>
					<td class="formText" align="right">

					</td>
					<td align="left">&nbsp;</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</html:form>
		</table>

		<ec:table items="transactionCodeHtrViewModelList" var="transactionCodeHtrViewModel"
		action="${contextPath}/p_transactioncodehistory.html"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Transaction Code History.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Transaction Code History.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Transaction Code History"
					fileName="Transaction Code History.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Transaction Code History.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>

			<ec:row>		
				<ec:column property="code" title="Transaction ID" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="usecaseName" title="Action Performed" escapeAutoFormat="True" style="text-align: center"/>
			    <ec:column property="username" title="Updated by" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="createdOn" title="Updated On" cell="date" format="dd/MM/yyyy hh:mm a" />
				<ec:column property="comments" title="Comments" escapeAutoFormat="True" style="text-align: center" sortable="false"/>
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
	</body>
</html>
