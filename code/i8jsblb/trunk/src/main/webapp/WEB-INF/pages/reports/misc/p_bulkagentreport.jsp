<!--Author: Naseer Ullah-->
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>
        
        <%@include file="/common/ajax.jsp"%>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" type="text/css" href="${contextPath}/styles/extremecomponents.css"/>
		<script language="javascript" type="text/javascript">
		  function resetComboBox(comboBoxId)
		  {
		  	var comboBox = document.getElementById(comboBoxId);
			comboBox.options.length = 1;
		  }
		  function handlePreDistributorOnChange()
		  {
		  	resetComboBox('retailerId');
		  	return true;
		  }

	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
        </script>
		<meta name="title" content="Bulk Agent Report"/>
	</head>
	<body bgcolor="#ffffff">
		<html:form name="bulkAgentForm" commandName="bulkAgentUploadReportModel" onsubmit="return validateForm(this)" action="p_bulkagentreport.html">
			<table width="800px">
				<tr>
					<td align="right" class="formText" width="22%">Agent Network</td>
					<td width="30%">
						<html:select id="distributorId" path="agentNetworkId" cssClass="textBox" tabindex="1">
							<html:option value="">---All---</html:option>
							<html:options items="${distributorModelList}" itemLabel="name" itemValue="distributorId"/>
						</html:select>
					</td>
					<td class="formText" align="right" width="18%">
						Region:
					</td>
					<td align="left" width="30%">
						<html:select id="regionId" path="regionId" cssClass="textBox" tabindex="2">
							<html:option value="">---All---</html:option>
							<html:options items="${regionModelList}" itemLabel="regionName" itemValue="regionId"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Franchise:
					</td>
					<td align="left">
						<html:select id="retailerId" path="franchiseBranchId" cssClass="textBox" tabindex="3">
							<html:option value="">---All---</html:option>
							<html:options items="${retailerModelList}" itemLabel="name" itemValue="retailerId"/>
						</html:select>
					</td>
					<td align="right" class="formText">
						Agent Name:
					</td>
					<td align="left">
						<html:input path="agentName" id="agentName" cssClass="textBox" maxlength="50" tabindex="4" onkeypress="return maskAlphaWithSp(this,event)" onkeydown="return disablePasteOption(event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Mobile No:
					</td>
					<td align="left">
						<html:input path="zongMsisdn" id="zongMsisdn" cssClass="textBox" maxlength="11" tabindex="5" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)"/>
					</td>
					<td align="right" class="formText">
						Status:
					</td>
					<td align="left" colspan="3">
						<html:select id="recordStatus" path="recordStatus" cssClass="textBox" tabindex="6">
							<html:option value="">---All---</html:option>
							<html:options items="${statuses}"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						<span class="asterisk">*</span>Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
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
					<td>&nbsp;</td>
					<td align="left" class="formText">
						<input type="submit" class="button" value="Search" name="_search" tabindex="11"/>
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_bulkagentreport.html'" tabindex="12"/>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
			</table>
		</html:form>

		<ec:table filterable="false" items="bulkAgentUploadReportModelList" var="bulkAgentUploadReportModel" retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit" action="${contextPath}/p_bulkagentreport.html">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Bulk Agent Report.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Bulk Agent Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Bulk Agent Report" fileName="Bulk Agent Report.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Bulk Agent Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Uploaded On" style="text-align:center;"/>
				<ec:column property="agentNetwork"/>
				<ec:column property="region"/>
				<ec:column property="franchiseBranch" title="Franchise/Branch Name"/>
				<ec:column property="agentName"/>
				<ec:column property="userName"/>
				<ec:column property="firstName"/>
				<ec:column property="lastName"/>
				<ec:column property="zongMsisdn" title="Mobile No" escapeAutoFormat="true" style="text-align:center;"/>
				<ec:column property="areaName"/>
				<ec:column property="active" viewsAllowed="html">
					${bulkAgentUploadReportModel.activeAsString}
				</ec:column>
				<ec:column property="activeAsString" title="Active" viewsDenied="html"/>
				<ec:column property="recordStatus" title="Status"/>
				<ec:column property="createdByName" title="Uploaded By"/>
			</ec:row>
		</ec:table>

		<ajax:select source="distributorId" target="regionId" baseUrl="${contextPath}/p_regionrefdata.html"
		parameters="distributorId={distributorId},actionId=${retriveAction}" errorFunction="error" preFunction="handlePreDistributorOnChange"/>

		<ajax:select source="regionId" target="retailerId" baseUrl="${contextPath}/p_retailerrefdata.html"
		parameters="regionId={regionId},actionId=${retriveAction}" errorFunction="error"/>

		<script language="javascript" type="text/javascript">
	        document.forms[0].distributorId.focus();

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
