<!--Author: Jalil-Ur-Rehman -->
<jsp:directive.page import="com.inov8.microbank.common.util.PortalConstants"/>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<html>
  <head>
<meta name="decorator" content="decorator">

   <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script> 
    
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Change User Information" />


  </head>
  
  <body bgcolor="#ffffff">
  <spring:bind path="changemobileListViewModel.*">
  <c:if test="${not empty status.errorMessages}">
    <div class="errorMsg">
      <c:forEach var="error" items="${status.errorMessages}">
        <c:out value="${error}" escapeXml="false" />
        <br/>
      </c:forEach>
    </div>
  </c:if>
</spring:bind>
   <c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

<table width="100%" >
<html:form name="changemobileForm" method="post" commandName="changemobileListViewModel" onsubmit="return validateForm()"  >

  <tr>


   <td width="33%" align="right" class="formText">Customer ID: </td>
    <td width="17%" align="left"><html:input onkeypress="return maskCommon(this,event)" path="userId" tabindex="1" id="userId"  cssClass="textBox" maxlength="8"/></td>
    <td width="16%" align="left">&nbsp;</td>
    <td width="34%" align="left"></td>

  </tr>
<tr>

    <td class="formText" align="right">Mobile #: </td>
    <td align="left"><html:input onkeypress="return maskCommon(this,event)" path="mobileNo" tabindex="2" id="mobileNo"  cssClass="textBox" maxlength="11"/></td>
    <td align="left">&nbsp;</td>
    <td align="left">
    </td>

  </tr>
<tr>
<td></td>
    <td align="left" >
    
     <input type ="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>" />           
			          <security:isauthorized action="<%=PortalConstants.ACTION_RETRIEVE%>">
						<jsp:attribute name="ifAccessAllowed">
					         <input type="submit"  class="button" value="Search" tabindex="3" name="_search" onClick="/*javascript:resetExportParameters('p_changemobilemanagement');*/"/>
				       	</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed">  
					         <input type="submit"  class="button" value="Search" tabindex="-1" name="_search" onClick="/*javascript:resetExportParameters('p_changemobilemanagement');*/" disabled="disabled"/>						
						</jsp:attribute>
			        </security:isauthorized>  	
    <input type="reset" class="button" value="Cancel" tabindex="4" name="_cancel" onClick="javascript: window.location='p_changemobilenomanagement.html?actionId=${retriveAction}'"></td>
    <td align="left" >&nbsp;</td>
    <td align="left" >&nbsp;
   </td>
   

  </tr>



</html:form>
</table>
	<ec:table filterable="false"
		items="changeMobileList"
                var = "changemobileListViewModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit"
		action="${pageContext.request.contextPath}/p_changemobilenomanagement.html?actionId=${retriveAction}"
		title="">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Change User Information.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Change User Information.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf headerBackgroundColor="#b6c2da"
				headerTitle="Change User Information" view="com.inov8.microbank.common.util.CustomPdfView" fileName="Change User Information.pdf" tooltip="Export PDF" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Change User Information.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
				
		<ec:row>
			<c:set var="changemobileListAppUserId">
				<security:encrypt strToEncrypt="${changemobileListViewModel.appUserId}"/>
			</c:set>
		
			<ec:column property="fullName" filterable="false" title="Name"/>
			<ec:column property="userId" filterable="false" title="Customer ID" escapeAutoFormat="true"/>
			<ec:column property="mobileNo" filterable="false" title="Mobile #" escapeAutoFormat="true"/>
			<ec:column property="name" filterable="false" title="Type"/>
			<ec:column property="fullAddress" filterable="false" title="Address" />
			<ec:column property="nic" filterable="false" title="NIC #" escapeAutoFormat="true"/>
            <ec:column alias=" " 
					viewsAllowed="html" filterable="false" sortable="false">

 			<input type ="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
					<security:isauthorized action="<%=PortalConstants.ACTION_UPDATE%>">
						<jsp:attribute name="ifAccessAllowed">
							<input type="button" class="button" style="width='104px'"
								value="Change Number"
								onclick='javascript:changeInfo("${pageContext.request.contextPath}/p_changemobileform.html?appUserId=${changemobileListAppUserId}&actionId=${retriveAction}")' />
						</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed">
							<input type="button" class="button" style="width='104px'"
								value="Change Number"
								disabled="disabled" />
						</jsp:attribute>
					</security:isauthorized>
				</ec:column>
			
    </ec:row>
	</ec:table>
    <script language="javascript" type="text/javascript">
            highlightFormElements();
            document.forms[0].userId.focus();
				function changeInfo(link)
				{
				    if (confirm('If customer information is verified then press OK to continue.')==true)
				    {
				      window.location.href=link+'&searchMobileNo='+$F('mobileNo')+'&searchMfsId='+$F('userId');
				    }
				}
				
	        function validateForm(){
	        	return validateFormChar(document.forms[0]);
	        }
				
				
				
	</script>
   
  </body>
</html>
























































