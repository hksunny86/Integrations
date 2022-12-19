<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<%-- <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%> --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
   <head>
		<meta name="decorator" content="decorator">
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Bulk Reversal" />
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/prototype.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/toolbar.js"></script>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css" />
	</head>

	<body bgcolor="#ffffff">
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
	
                    <fieldset>
                    	<legend>Summary</legend>
                                	<table width="95%" border="0" cellpadding="0" cellspacing="1" style="margin:0px auto; border:1px solid #999;">
                                        <tr align="center">
                                            <td width="33%" height="23"><b></b></td>
                                            <td width="34%" bgcolor="#8ec6e5"><b># of Transcations</b></td>
                                            <td width="34%" bgcolor="#8ec6e5"><b>Amount</b></td>
                                        </tr>
                                        <tr align="center">
                                            <td width="33%" height="23" bgcolor="#8ec6e5"><b>Invalid Records</b></td>
                                            <td width="34%" bgcolor="#f8f8f8">${totalInValidTraxns}</td>
                                            <td width="33%" bgcolor="#f8f8f8"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${totalInValidRecordTraxnAmount}"/></td>
                                        </tr>
                                        <tr align="center">
                                            <td width="33%" height="23" bgcolor="#8ec6e5"><b>Valid Records</b></td>
                                            <td width="34%" bgcolor="#f8f8f8">${totalValidTraxns}</td>
                                            <td width="33%" bgcolor="#f8f8f8"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${totalValidRecordTraxnAmount}"/></td>
                                        </tr>
                                        <tr align="center">
                                          <td width="34%" bgcolor="#8ec6e5"><b>Total Records</b></td>
                                          <td width="34%" bgcolor="#f8f8f8">${totalInValidTraxns + totalValidTraxns} </td>
                                          <td width="33%" bgcolor="#f8f8f8"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${totalInValidRecordTraxnAmount + totalValidRecordTraxnAmount}"/></td>
                                        </tr>
                                    </table>
                    </fieldset>
                    
                    
          <c:if test="${not empty bulkAutoReversalModelList}">
			<html:form name="bulkReversalForm" commandName="bulkAutoReversalModel" method="post" action="p_bulkreversal.html">
				<input type="hidden" name="usecaseId" value = "<%=PortalConstants.BULK_AUTO_REVERSAL_USECASE_ID %>"/>
			
				<table width="750px" border="0">
				   <c:if test="${totalInValidTraxns > 0}">
				      <tr>
						<td class="formText" style="color:red;">
									File contains one or more invalid records. Kindly fix and upload again. 
						</td>
					 </tr>
					 <tr>
					   <td class="formText">
						 <a href="${contextPath}/p_downloadinvalidbulkreversalrecordsfile.html">Export Invalid Records</a>
					   </td>
					</tr>	
	             </c:if>
				 <c:if test="${totalValidTraxns > 0}">
					<tr>
					 <td class="formText">
					   <input id="_save" type="button" name="_save" value="  Save Valid Records " tabindex="1" onclick="javascript:disableSubmit();"class="button" />
					   &nbsp;&nbsp;&nbsp;&nbsp;
					   <input type="button" class="button" value="Cancel" tabindex="2" onClick="javascript: window.location='p_bulkreversal.html'" />
					</td>
				  </tr>
			  </c:if>
		</table>
	</html:form>
</c:if>
		
		<ec:table items="bulkAutoReversalModelList" var="bulkAutoReversalModel" action="${contextPath}/p_bulkreversalpreview.html"
			title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false" sortable="false">
			<ec:row interceptor="bulkAutoReversalErrorRowInterceptor">
				<ec:column property="srNo"/>
<%--				<ec:column alias="Adjustment Type">--%>
<%--				<c:if test="${bulkAutoReversalModel.adjustmentType == 1}">--%>
<%--					BB to BB--%>
<%--				</c:if>--%>
<%--				<c:if test="${bulkAutoReversalModel.adjustmentType == 2}">--%>
<%--					BB to Core--%>
<%--				</c:if>--%>
<%--				<c:if test="${bulkAutoReversalModel.adjustmentType == 3}">--%>
<%--					Core to BB--%>
<%--				</c:if>--%>
<%--				<c:if test="${bulkAutoReversalModel.transactionId > 3 or bulkAutoReversalModel.adjustmentType < 1 }">--%>
<%--					${bulkAutoReversalModel.trxnId}--%>
<%--				</c:if>--%>
<%--				</ec:column>--%>
				<ec:column property="trxnId" title="Transaction ID"/>
<%--				<ec:column property="fromAccount"/>--%>
<%--				<ec:column property="toAccount"/>--%>
<%--				<ec:column property="balance"/>--%>
<%--				<ec:column property="description"/>--%>
				<ec:column property="valildationErrorMessage"/>
			</ec:row>
		</ec:table>




	<input type="button" class="button" value="Back" tabindex="3" onclick="javascript: window.location.href='p_bulkreversal.html?actionId=1'" />
		
		
		<script language="javascript" type="text/javascript">
			function disableSubmit()
		    {
		    	document.forms.bulkReversalForm._save.disabled=true;
		    	
			    onSave(document.forms.bulkReversalForm,null);
		    }
		</script>
  </body>
</html>
