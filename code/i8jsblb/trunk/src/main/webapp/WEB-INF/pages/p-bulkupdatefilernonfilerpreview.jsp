<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   		<meta name="decorator" content="decorator">
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Bulk Update Filer / Non Filer Preview" />
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
                                            <td width="33%" height="23" bgcolor="#8ec6e5"><b>Invalid Records</b></td>
                                            <!--  <td width="34%" bgcolor="#f8f8f8">${totalInValidRecords}</td> -->
                                            <td width="33%" bgcolor="#f8f8f8"><fmt:formatNumber type="number"  value="${totalInValidRecords}"/></td>
                                        </tr>
                                        <tr align="center">
                                            <td width="33%" height="23" bgcolor="#8ec6e5"><b>Valid Records</b></td>
                                           <!--   <td width="34%" bgcolor="#f8f8f8">${totalValidRecords}</td> -->
                                            <td width="33%" bgcolor="#f8f8f8"><fmt:formatNumber type="number"  value="${totalValidRecords}"/></td>
                                        </tr>
                                        <tr align="center">
                                          <td width="34%" bgcolor="#8ec6e5"><b>Total Records</b></td>
                                         <!--   <td width="34%" bgcolor="#f8f8f8">${totalInValidRecords + totalValidRecords} </td> -->
                                          <td width="33%" bgcolor="#f8f8f8"><fmt:formatNumber type="number"  value="${totalInValidRecords + totalValidRecords}"/></td>
                                        </tr>
                                    </table>
                    </fieldset>
                    
                    
          <c:if test="${not empty bulkFilerNonFilerVOList}">
			<html:form name="bulkFilerNonFilerForm" commandName="bulkFilerNonFilerVO" method="post" action="p_bulkupdatefilernonfiler.html">
				<table width="750px" border="0">
				   <c:if test="${totalInValidRecords > 0}">
				      <tr>
						<td class="formText" style="color:red;">
									File contains one or more invalid records. Kindly fix and upload again. 
						</td>
					 </tr>
					 <tr>
					   <td class="formText">
						 <a href="${contextPath}/p_downloadInvalidBulkUpdateFilerNonFilerRecordsFile.html">Export Invalid Records</a>
					   </td>
					</tr>	
	             </c:if>
				 <c:if test="${totalValidRecords > 0}">
					<tr>
					 <td class="formText">
					   <input id="_update" type="button" name="_update" value="  Update Valid Records " tabindex="1" onclick="javascript:disableSubmit();"class="button" />
					   &nbsp;&nbsp;&nbsp;&nbsp;
					   <input type="button" class="button" value="Cancel" tabindex="2" onClick="javascript: window.location='p_bulkupdatefilernonfiler.html'" />
					</td>
				  </tr>
			  </c:if>
		</table>
	</html:form>
</c:if>
		
		<ec:table items="bulkFilerNonFilerVOList" var="bulkFilerNonFilerVO" action="${contextPath}/p-bulkupdatefilernonfilerpreview.html"
			title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false" sortable="false">
			<ec:row interceptor="updateFilerErrorRowInterceptor">
				<ec:column property="srNo" title="Sr #"/>
				<ec:column property="filer"/>
				<ec:column property="cnic" title="CNIC"/>
				<ec:column property="description" title="Error Reason"/>
			</ec:row>
		</ec:table>

	<input type="button" class="button" value="Back" tabindex="3" onclick="javascript: window.location.href='p_bulkupdatefilernonfiler.html?actionId=1'" />
		
		
		<script language="javascript" type="text/javascript">
			function disableSubmit()
		    {
		    	document.forms.bulkFilerNonFilerForm._update.disabled=true;
		    	
			    onSave(document.forms.bulkFilerNonFilerForm,null);
		    }
		</script>
    
  </body>
</html>
