<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DecimalFormat" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
			<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/toolbar.js"></script>
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Cash-Bank Reconciliation Upload" />
		
		<%@include file="/common/ajax.jsp"%>
		
	</head>

	<body bgcolor="#ffffff">

		<div id="successMsg" class="infoMsg" style="display: none;"></div>
		<div id="errorMsg" class="errorMsg" style="display: none;"></div>

		<spring:bind path="agentCashVOModel.*">
			<c:if test="${not empty status.errorMessages}">
				<div class="errorMsg">
					<c:forEach var="error" items="${status.errorMessages}">
						<c:out value="${error}" escapeXml="false" />
						<br />
					</c:forEach>
				</div>
			</c:if>
		</spring:bind>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>




			<html:form name="agentCashUploadForm" commandName="agentCashVOModel" enctype="multipart/form-data" method="POST"
				action="p_agentcashupload.html">
				
					<input type="hidden" name="isPreview" value="${isPreview}" />
					<input type="hidden" name="csvAbsPath" value="${csvAbsPath}" />
					

					<c:if test="${empty isPreview}">
						<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
						<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
							value="<%=PortalConstants.ACTION_UPDATE%>" />
					</c:if>
						
					<c:if test="${not empty isPreview}">
						<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
							value="<%=PortalConstants.ACTION_CREATE%>" />
					</c:if>


					<table width="750px" border="0" cellpadding="0" cellspacing="1">
						<tr>
						<c:if test="${empty isPreview}">
							<c:remove var="validatedList" scope="session" />
							<td align="right" bgcolor="F3F3F3" class="formText" width="25%">
								<span style="color: #FF0000">*</span>Upload File:
							</td>
							<td align="left" bgcolor="FBFBFB" class="text" width="29%">

								<spring:bind path="agentCashVOModel.csvFile">
									<input type="file" name="csvFile" tabindex="1" 
										style="height: 20px; width: 200px" />
								</spring:bind>
								&nbsp;&nbsp;
							</td>
							<td align="left" bgcolor="FBFBFB" class="text" width="46%">
								<input type="button" name="_save" value="  Preview  " tabindex="2"
								onclick="javascript:onFormSubmit(document.forms.agentCashUploadForm);"
								class="button" />
							</td>
						</c:if>
							
							<c:if test="${not empty isPreview}">
								<td align="center" bgcolor="FBFBFB" class="text" width="100%">
									<input id="_save" type="button" name="_save" value="  Save  " tabindex="2"
									onclick="javascript:disableSubmit();"
									class="button" />
									&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="button" class="button" value="Cancel" tabindex="3" onClick="javascript: window.location='p_agentcashupload.html'" />
								</td>
							</c:if>
						</tr>
						
						
						
					</table>
			</html:form>

		
		
		
<c:if test="${empty cashBankMappingModelList}">
	<div align="left" bgcolor="F3F3F3" class="formText">
	No results found.
	</div>	
	<div class="eXtremeTable">
	<table id="ec_tableBBStatement_empty"  class="tableRegion"  width="1000px" border="0">
		<thead>
		<tr class="tableHeader" style="padding: 0px;" >
		<td class="tableHeader" style="text-align:left;" align="left">Date</td>
		<td class="tableHeader" style="text-align:left;" align="left">CSC ID</td>
		<td class="tableHeader" style="text-align:left;" align="left">Agent A/c #</td>
		<td class="tableHeader" style="text-align:left;" align="left">Mobile No</td>
		<td class="tableHeader" style="text-align:left;" align="left">Bank Deposit</td>
		<td class="tableHeader" style="text-align:left;" align="left">Bank Withdrawal</td>
		<td class="tableHeader" style="text-align:left;" align="left">CSC Cash IN</td>
		<td class="tableHeader" style="text-align:left;" align="left">CSC Cash OUT</td>
		</tr>
		</thead>
	</table>
	</div>
</c:if>


<c:if test="${not empty cashBankMappingModelList}">

<div align="left" bgcolor="F3F3F3" class="formText">
	<% 
	List tempList = (List)request.getAttribute("cashBankMappingModelList"); 
	if(tempList != null && tempList.size() > 0){
	%>
	<%= tempList.size()%> result(s) found.
	<%
	}
	else{
	%>
		No results found.
	<%
	}
	%>
	</div>
	

<div class="eXtremeTable">
<table id="ec_tableBBStatement"  class="tableRegion"  width="1000px" border="0">
<thead>
 
<tr class="tableHeader" style="padding: 0px;" >
		<td class="tableHeader" style="text-align:left;" align="left">Date</td>
		<td class="tableHeader" style="text-align:left;" align="left">CSC ID</td>
		<td class="tableHeader" style="text-align:left;" align="left">Agent A/c #</td>
		<td class="tableHeader" style="text-align:left;" align="left">Mobile No</td>
		<td class="tableHeader" style="text-align:left;" align="left">Bank Deposit</td>
		<td class="tableHeader" style="text-align:left;" align="left">Bank Withdrawal</td>
		<td class="tableHeader" style="text-align:left;" align="left">CSC Cash IN</td>
		<td class="tableHeader" style="text-align:left;" align="left">CSC Cash OUT</td>
</tr>
	
</thead>

	<c:forEach items="${cashBankMappingModelList}" var="cashBankMappingModel">
	
	<c:choose>
		<c:when test="${cashBankMappingModel.validRecord}">
			<c:set var="highlighted"> </c:set>
		</c:when>
		<c:otherwise>
			<c:set var="highlighted"> color:red; font-weight:bold; </c:set>
		</c:otherwise>
	</c:choose>
	<tr>
	
		<td class ="even" style="${highlighted}font-family:verdana;font-size: 10px;" >
			<fmt:formatDate pattern="dd/MM/yyyy" value="${cashBankMappingModel.transactionDate}"/> 
		</td>
		<td class ="even" style="${highlighted}font-family:verdana;font-size: 10px;">
			<c:out value="${cashBankMappingModel.cscId}" /> 
		</td>
		<td class ="even" style="${highlighted}font-family:verdana;font-size: 10px;" >
			<c:out value="${cashBankMappingModel.agentAccountNo}" escapeXml="false"/> 
		</td>
		<td class ="even" style="${highlighted}font-family:verdana;font-size: 10px;" >
			<c:out value="${cashBankMappingModel.agentMobileNo}" />
		</td>
		<td class ="even" style="${highlighted}font-family:verdana;font-size: 10px;" >
			<fmt:formatNumber value="${cashBankMappingModel.bankCreditAmount}"  pattern="###.##"/> 
		</td>
		<td class ="even" style="${highlighted}font-family:verdana;font-size: 10px;" >
			<fmt:formatNumber value="${cashBankMappingModel.bankDebitAmount}"  pattern="###.##"/> 
		</td>
		<td class ="even" style="${highlighted}font-family:verdana;font-size: 10px;" >
			<fmt:formatNumber value="${cashBankMappingModel.tillCreditAmount}"  pattern="###.##"/> 
		</td>
		<td class ="even" style="${highlighted}font-family:verdana;font-size: 10px;">
			<fmt:formatNumber value="${cashBankMappingModel.tillDebitAmount}"  pattern="###.##"/> 
		</td>
	</tr>
	</c:forEach>
	
</table>
</div>
</c:if>
		
		
		
		<script language="javascript" type="text/javascript">
      function onFormSubmit(theForm)
      {
      	if(document.forms.agentCashUploadForm.csvFile.value == ''){
      		alert("Upload File is required.");
      		return false;
      	}
      	if(!document.forms.agentCashUploadForm.csvFile.value.endsWith('.csv')){
      		alert("Only files with extension (.csv) are allowed.");
      		return false;
	    }
	    onSave(document.forms.agentCashUploadForm,null);
      }
      
      function disableSubmit()
      {
      	document.forms.agentCashUploadForm._save.disabled=true;
      	
	    onSave(document.forms.agentCashUploadForm,null);
      }
     
      </script>
			
	</body>

</html>
