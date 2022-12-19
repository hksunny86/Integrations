<!--Author: Naseer Ullah-->

<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="url" value="p_dayendsettlementreport.html"></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="Day End Settlement Report"/>
	</head>
	<body bgcolor="#ffffff">
		<%-- <html:form name="dateRangeVoForm" commandName="dateRangeVo" onsubmit="return validateForm(this)" action="p_dayendsettlementreport.html" method="post">
			<table width="750px">
				<tr>
					<td class="formText" align="right">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="1" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="2" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="3" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="4" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">&nbsp;</td>
					<td align="left" class="formText">
						<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
						<input type="submit" class="button" value="Search" name="_search" tabindex="5" /> 
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_dayendsettlementreport.html'" tabindex="6"/>
					</td>
				</tr>
			</table>
		</html:form> --%>
		<ec:table filterable="false" sortable="false" items="dayEndSettlementVoList" var="dayEndSettlementVo" retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit" action="${contextPath}/p_dayendsettlementreport.html" title="">
			<ec:row style="text-align:center;">
				<ec:column property="fileType" alias="File Name" title="File Name"/>
				<ec:column property="createdOn" alias="Date" cell="date" format="dd/MM/yyyy" title="Date"/>
				<ec:column property="createdOn" alias="Time" cell="date" format="hh:mm a" sortable="false" title="Time"/>
				<ec:column property="fileName" title="Download" sortable="false">
					<a href="${contextPath}/dayEndSettlementDownload.html?fileName=${dayEndSettlementVo.fileName}">Download</a>
				</ec:column>
			</ec:row>
		</ec:table>

		<script type="text/javascript">		
			function validateForm(form)
			{
				var isValid=true;
				var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        	var _fDate = form.startDate.value;
	  			var _tDate = form.endDate.value;
		  		var startlbl = "Start Date";
		  		var endlbl   = "End Date";
		  		isValid = validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,currentDate);

	        	return isValid;
	        }

      		Calendar.setup(
		    {
		        inputField  : "startDate", // id of the input field
		        ifFormat    : "%d/%m/%Y",      // the date format
		        button      : "sDate",    // id of the button
		      	showsTime   :   false
		    }
		    );
		      
		    Calendar.setup(
		    {
		        inputField  : "endDate", // id of the input field
		        ifFormat    : "%d/%m/%Y",      // the date format
		        button      : "eDate",    // id of the button
		        showsTime   :   false,
		      }
		    );
		</script>
	</body>
</html>
