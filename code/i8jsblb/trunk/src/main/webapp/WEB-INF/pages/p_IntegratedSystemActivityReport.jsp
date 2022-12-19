<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>
<html>
	<head>
<meta name="decorator" content="decorator">
   		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />	    
		<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
		<script type="text/javascript">
		var jq=$.noConflict();
		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
			function actdeact(request)
			{
				isOperationSuccessful(request);
			}
			function error(request)
		      {
		      	alert("An unknown error has occured. Please contact with the administrator for more details");
		      }
		   
		</script>
		<meta name="title" content="Integrated System Logs" />
	</head>
	<body bgcolor="#ffffff">
  	<%@include file="/common/ajax.jsp"%>

	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<c:if test="${not empty messages}">
	    	<div class="infoMsg" id="successMessages">
	      		<c:forEach var="msg" items="${messages}">
		        	<c:out value="${msg}" escapeXml="false"/>
		        	<br/>
	      		</c:forEach>
	    	</div>
	    	<c:remove var="messages" scope="session"/>
	  	</c:if>

		<html:form name="integratedSystemActivitySearchForm" commandName="fonePayLogModel" method="post" action="p_IntegratedSystemActivityReport.html" onsubmit="return validateForm(this)">
			<input type="hidden" name="appUserId" value="${param.appUserId}">
			<table border="0" width="750px" >
				<tr>
					<td  align="right" class="formText" width="20%">
						Transaction Id:
					</td>
					<td align="left"  width="30%">
						<html:input path="transactionId" id="transactionId" cssClass="textBox"/>
					</td>
					<td  align="right" class="formText" width="20%">
						Response Code:
					</td>
					<td align="left" width="30%" >
						<html:input path="response_code" id="response_code" cssClass="textBox"/>
					</td>
				</tr>
				<tr>
					<td  align="right" class="formText" width="20%">
						Request Type:
					</td>
					<%-- <td align="left"  width="30%">
						<html:input path="requestType" id="requestType" cssClass="textBox"/>
					</td> --%>
					 <td align="left">
                    <html:select path="requestType" tabindex="2" cssClass="textBox">
                        <html:option value="">---All---</html:option>
                        <c:if test="${requestTypeList != null}">
                            <html:options items="${requestTypeList}" itemValue="value" itemLabel="label" />
                        </c:if>
                    </html:select>
                </td>
					<td  align="right" class="formText" width="20%">
						RRN:
					</td>
					<td align="left" width="30%" >
						<html:input path="rrn" id="rrn" cssClass="textBox"/>
					</td>
				</tr>
				
				<tr>
					<td  align="right" class="formText" width="20%">
						Mobile No.:
					</td>
					<td align="left"  width="30%">
						<html:input path="mobile_no" id="mobile_no" cssClass="textBox" onkeypress="return maskNumber(this,event)" />
					</td>
					<td  align="right" class="formText" width="20%">
						CNIC:
					</td>
					<td align="left" width="30%" >
						<html:input path="cnic" id="cnic" cssClass="textBox" onkeypress="return maskNumber(this,event)" />
					</td>
				</tr>
				
				<tr>
	            	<td width="20%"  align="right"  class="formText">From Date:</td>
	             	<td width="30%" align="left" >
	             		<html:input path="fromDate" cssClass="textBox" readonly="true" tabindex="1"/>
						<img id="fDate" tabindex="2" name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="fDate" tabindex="3" name="popcal" title="Clear Date" onclick="javascript:$('fromDate').value=''" align="middle" style="cursor:pointer"
							src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
					</td>
					<td width="20%"  align="right"  class="formText">To Date:</td>
		             <td width="30%" align="left" >
	             		<html:input path="toDate" cssClass="textBox" readonly="true" tabindex="4"/>
						<img id="tDate" tabindex="5" name="popcal" align="top"  style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="tDate" tabindex="6" name="popcal" title="Clear Date" onclick="javascript:$('toDate').value=''" align="middle" style="cursor:pointer"
							src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
					</td>
		       	</tr>
			  <tr>
				<td></td>
			    <td align="left" >
			    	<input type="submit"  class="button" value="Search" tabindex="3" name="_search"/>
			    	<input type="reset" class="button" value="Cancel"  name="_cancel" onClick="javascript: window.location='p_IntegratedSystemActivityReport.html'">
			   	</td>
			  </tr>
	 	 	</table>
		</html:form>

		<ec:table filterable="false" items="fonePayLogModelList"
			var="fonePayLogModelListView" retrieveRowsCallback="limit"
			filterRowsCallback="limit" sortRowsCallback="limit"
			action="${pageContext.request.contextPath}/p_IntegratedSystemActivityReport.html"
			title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Integrated System Logs.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Integrated System Logs.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Logs" fileName="Integrated System Logs.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Integrated System Logs.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
			
			
				<ec:column filterable="false" property="transactionId" title="Transaction Id" escapeAutoFormat="true"  />
				<ec:column filterable="false" property="created_on" cell="date" format="dd/MM/yyyy hh:mm a" alias="Date" title="Date"/>				
				<ec:column filterable="false" property="requestType" title="Request Type"  />
				<ec:column filterable="false"  property="rrn" title="Retrival Refrence Number"  />
				<ec:column filterable="false" property="mobile_no" title="Mobile No" escapeAutoFormat="true"  />
				<ec:column filterable="false" property="cnic" title="CNIC" escapeAutoFormat="true"  />
				<ec:column filterable="false"  property="response_code" title="Response Code"  />
				<ec:column filterable="false" property="response_description" title="Response Description" escapeAutoFormat="true"  />
			</ec:row>
		</ec:table>
		<script language="javascript" type="text/javascript">
		
	      
	      Calendar.setup(
	      {
	        inputField  : "fromDate", // id of the input field
	        button      : "fDate"    // id of the button
	      }
	      );
	      
	   	  Calendar.setup(
	      {
	        inputField  : "toDate", // id of the input field
	        button      : "tDate",    // id of the button
	        isEndDate: true
	      }
	      );
	      
	      </script>
		<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>