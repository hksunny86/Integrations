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
        <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
        
        <%@include file="/common/ajax.jsp"%>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" type="text/css" href="${contextPath}/styles/extremecomponents.css"/>
		<script language="javascript" type="text/javascript">
		var jq=$.noConflict();
		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
        </script>
		<meta name="title" content="Bulk Franchise Report"/>
	</head>
	<body bgcolor="#ffffff">
		<html:form name="bulkFranchiseForm" commandName="bulkFranchiseModel" onsubmit="return validateForm(this)" action="p_bulkfranchisereport.html">
			<table width="750px">
				<tr>
					<td align="right" class="formText">Agent Network</td>
					<td align="left" colspan="3">
						<html:select id="distributorId" path="distributorId" cssClass="textBox" tabindex="1">
							<html:option value="">---All---</html:option>
							<html:options items="${distributorModelList}" itemLabel="name" itemValue="distributorId"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right" width="18%">
						Region:
					</td>
					<td align="left" width="30%">
						<html:select id="regionId" path="regionModelId" cssClass="textBox" tabindex="2">
							<html:option value="">---All---</html:option>
							<html:options items="${regionModelList}" itemLabel="regionName" itemValue="regionId"/>
						</html:select>
					</td>
					<td align="right" class="formText" width="22%">
						Franchise/Branch Name:
					</td>
					<td align="left" width="30%">
						<html:input path="name" id="name" cssClass="textBox" maxlength="50" tabindex="3" onkeypress="return maskAlphaWithSp(this,event)" onkeydown="return disablePasteOption(event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Contact Name:
					</td>
					<td align="left">
						<html:input path="contactName" id="contactName" cssClass="textBox" maxlength="50" tabindex="4" onkeypress="return maskAlphaWithSp(this,event)" onkeydown="return disablePasteOption(event)"/>
					</td>
					<td align="right" class="formText">
						Mobile No:
					</td>
					<td align="left">
						<html:input path="phoneNo" id="phoneNo" cssClass="textBox" maxlength="11" tabindex="5" onkeypress="return maskInteger(this,event)" onkeydown="return disablePasteOption(event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Email Address:
					</td>
					<td align="left">
						<html:input path="email" id="email" cssClass="textBox" maxlength="50" tabindex="6"/>
					</td>
					<td align="right" class="formText">
						Status:
					</td>
					<td align="left">
						<html:select id="result" path="result" cssClass="textBox" tabindex="7">
							<html:option value="">---All---</html:option>
							<html:options items="${statuses}"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="8" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="9" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="10" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="11" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="left" class="formText">
						<input type="submit" class="button" value="Search" name="_search" tabindex="12"/>
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_bulkfranchisereport.html'" tabindex="13"/>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
			</table>
		</html:form>

		<ec:table filterable="false" items="bulkFranchiseModelList" var="bulkFranchiseModel" retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit" action="${contextPath}/p_bulkfranchisereport.html">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Bulk Franchise Report.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Bulk Franchise Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Bulk Franchise Report" fileName="Bulk Franchise Report.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Bulk Franchise Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Uploaded On" style="text-align:center;"/>
				<ec:column property="distributorName" title="Agent Network"/>
				<ec:column property="regionName" title="Region"/>
				<ec:column property="name" title="Franchise/Branch Name"/>
				<ec:column property="contactName"/>
				<ec:column property="phoneNo" title="Mobile No" escapeAutoFormat="true" style="text-align:center;"/>
				<ec:column property="email" title="Email Address"/>
				<ec:column property="active" viewsAllowed="html">
					${bulkFranchiseModel.activeAsString}
				</ec:column>
				<ec:column property="activeAsString" title="Active" viewsDenied="html"/>
				<ec:column property="result" title="Status"/>
				<ec:column property="createdByName" title="Uploaded By"/>
			</ec:row>
		</ec:table>

		<ajax:select source="distributorId" target="regionId" baseUrl="${contextPath}/p_regionrefdata.html"
		parameters="distributorId={distributorId},actionId=${retriveAction}" errorFunction="error"/>

		<script language="javascript" type="text/javascript">
	        document.forms[0].distributorId.focus();

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
