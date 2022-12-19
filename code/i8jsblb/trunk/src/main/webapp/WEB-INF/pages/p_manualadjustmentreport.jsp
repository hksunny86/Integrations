<%@include file="/common/taglibs.jsp"%>
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
		<link rel="stylesheet"
			href="${contextPath}/styles/extremecomponents.css"
			type="text/css">
		<%@include file="/common/ajax.jsp"%>
		<meta name="title" content="Manual Adjustment Report" />
		<meta name="decorator" content="decorator">
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
	
		<div id="rsp" class="ajaxMsg"></div>
	
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
			<html:form name='manualAdjustmentForm'
				commandName="manualAdjustmentModel" method="post"
				action="p_manualadjustmentreport.html" onsubmit="return validateForm(this)" >
				<table width="800px" border="0">
					<tr>
						<td class="formText" width="18%" align="right">
							Transaction ID:
						</td>
						<td align="left" width="32%" >
							<html:input path="transactionCodeId" cssClass="textBox" maxlength="50" tabindex="1" onkeypress="return maskNumber(this,event)"/>
						</td>
					</tr>
					<tr>

						<td class="formText" align="right" width="18%">
							Adjustment Type:
						</td>
						<td align="left">
							<html:select path="adjustmentType" tabindex="2" cssClass="textBox">
								<html:option value="">---All---</html:option>
									<c:if test="${adjustmentTypeList != null}">
										<html:options items="${adjustmentTypeList}" itemValue="value" itemLabel="label" />
									</c:if>
							</html:select>
						</td>
						<td class="formText" align="right" width="18%">
							Adjustment Category:
						</td>
						<td align="left">
							<html:select path="adjustmentCategory" tabindex="3" cssClass="textBox">
								<html:option value="">---All---</html:option>
								<c:if test="${adjustmentCategoryList != null}">
									<html:options items="${adjustmentCategoryList}" itemValue="value" itemLabel="label" />
								</c:if>
							</html:select>
						</td>
					</tr>
				<tr>
					<td class="formText" align="right">
						From Account No:
					</td>
					<td align="left">
						<html:input path="fromACNo" cssClass="textBox" maxlength="50" tabindex="4" onkeypress="return maskNumber(this,event)"/>
					</td>
					<td class="formText" align="right">
						To Account No:
					</td>
					<td align="left" >
						<html:input path="toACNo" cssClass="textBox" maxlength="11" tabindex="5" onkeypress="return maskNumber(this,event)"/>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						From Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="6" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="7" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						To Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="8" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="9" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="9"/> 
						<input name="reset" type="reset" 
							onclick="javascript: window.location='p_manualadjustmentreport.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="10" />
					</td>
					<td class="formText" align="right"></td>
					<td align="left"></td>
				</tr>

				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
					value="<%=PortalConstants.ACTION_RETRIEVE%>">
				</table>
			</html:form>
	
		
		<ec:table items="manualAdjustmentModelList" var="manualAdjustmentModel"
		action="${contextPath}/p_manualadjustmentreport.html?actionId=${retriveAction}"
		title=""
          retrieveRowsCallback="limit"
          filterRowsCallback="limit"
          sortRowsCallback="limit"
          filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Manual Correction/Adjustment.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Manual Correction/Adjustment.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Transaction Details"
					fileName="Manual Correction/Adjustment.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Manual Correction/Adjustment.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			
			<ec:row>
				<ec:column property="transactionCodeId" title="Transaction ID" escapeAutoFormat="true"/>
				<ec:column property="adjustmentType" title="Adjustment Type" style="text-align: center">
					<c:if test="${manualAdjustmentModel.adjustmentType=='1'}">BB To BB</c:if>
					<c:if test="${manualAdjustmentModel.adjustmentType=='2'}">BB To Core</c:if>
					<c:if test="${manualAdjustmentModel.adjustmentType=='3'}">Core To BB</c:if>
				</ec:column>
				<ec:column property="fromACNo" title="From Account No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="fromACNick" title="From Account Title" style="text-align: center"/>
				<ec:column property="toACNo" title="To Account No."  escapeAutoFormat="true"  style="text-align: center"/>
				<ec:column property="toACNick" title="To Account Title" style="text-align: center"/>
				<ec:column property="amount" title="Amount (Rs.)" escapeAutoFormat="true" cell="currency" format="0.00" />
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Created On"/>
				<ec:column property="authorizerId" title="Authorizer/Checker ID" style="text-align: center"/>
				<ec:column property="adjustmentCategory" title="Adjustment Category" style="text-align: center">
					<c:if test="${manualAdjustmentModel.adjustmentCategory=='1'}">Single</c:if>
					<c:if test="${manualAdjustmentModel.adjustmentCategory=='2'}">Bulk</c:if>
				</ec:column>
				<ec:column property="comments" filterable="false" title="Comments"/>
			</ec:row>
		</ec:table>
		
		<script language="javascript" type="text/javascript">
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
