<!--Author: Mohammad Shehzad Ashraf -->
<jsp:directive.page import="com.inov8.microbank.common.util.ConcernsConstants"/>
<jsp:directive.page import="com.inov8.microbank.common.util.*"/>
<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
   <head>
<meta name="decorator" content="decorator">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
      <meta name="title" content="List Concerns"/>
    <script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>  
	<%
           	String allowedReadPermission = PortalConstants.MNO_GP_READ;
           	allowedReadPermission += ",";
           	allowedReadPermission += PortalConstants.PAS_GP_READ;
           	allowedReadPermission += ",";             	             	             	             	
           	allowedReadPermission += PortalConstants.PRS_GP_READ;             	
           	allowedReadPermission += ",";             	             	             	             	
           	allowedReadPermission += PortalConstants.CSR_GP_READ;             	
          	allowedReadPermission += ",";             	             	             	             	
           	allowedReadPermission += PortalConstants.RET_GP_READ;             	

           	String allowedUpdatePermission = PortalConstants.MNG_PRT_CNCRN_UPDATE;
           	allowedUpdatePermission += ",";
           	allowedUpdatePermission += PortalConstants.MNO_GP_UPDATE;
           	allowedUpdatePermission += ",";
           	allowedUpdatePermission += PortalConstants.PAS_GP_UPDATE;
           	allowedUpdatePermission += ",";             	             	             	             	
           	allowedUpdatePermission += PortalConstants.PRS_GP_UPDATE; 
           	allowedUpdatePermission += ",";             	             	             	             	
           	allowedUpdatePermission += PortalConstants.CSR_GP_UPDATE; 
           	allowedUpdatePermission += ",";             	             	             	             	
           	allowedUpdatePermission += PortalConstants.RET_GP_UPDATE; 
            %>
   </head>
   <body bgcolor="#ffffff">

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
	<c:set var="retrieveAction" value="<%=PortalConstants.ACTION_RETRIEVE%>" />
	<c:set var="createAction" value="<%=PortalConstants.ACTION_CREATE%>" />
      
      <table width="750px">
          <html:form name='transactionDetailForm'
				commandName="concernListViewModel" method="post"
				action="p_listconcerns.html?actionId=${retrieveAction}"
				onsubmit="/*return validateForm()*/">
          <tr>
          
          <input type="hidden" name="actionId" value="${retrieveAction}">
              <td align="right" class="formText" width="33%">Concern Code:</td>
              <td width="17%"><html:input path="concernCode"
							onkeypress="return maskCommon(this,event)" id="concernCode"
							cssClass="textBox" tabindex="1" maxlength="10" /></td>
			  <td align="right" class="formText" width="16%">Raised By:</td>
              <td width="34%">
                  <html:select path="initiatorPartnerName" tabindex="2" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<c:if test="${concernPartnerModelList != null}">
								<html:options items="${concernPartnerModelList}"
									itemValue="name" itemLabel="name" />
							</c:if>
						</html:select>
              </td>
          </tr>
          <tr>
              <td align="right" class="formText">Category:</td>
              <td>
                  <html:select path="concernCategoryName" tabindex="3" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<c:if test="${concernCategoryModelList != null}">
								<html:options items="${concernCategoryModelList}"
									itemValue="name" itemLabel="name" />
							</c:if>
						</html:select>
              </td>
              <td align="right" class="formText">Priority:</td>
              <td>
                  <html:select path="concernPriorityName" tabindex="4" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<c:if test="${concernPriorityModelList != null}">
								<html:options items="${concernPriorityModelList}"
									itemValue="name" itemLabel="name" />
							</c:if>
						</html:select>
              </td>
          </tr>
          <tr>
              <td align="right" class="formText">Status:</td>
              <td>
                  <html:select path="concernStatusName" tabindex="5" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<c:if test="${concernStatusModelList != null}">
								<html:options items="${concernStatusModelList}"
									itemValue="name" itemLabel="name" />
							</c:if>
						</html:select>
              </td>
              <td align="right" class="formText"></td>
              <td>
                  
              </td>
          </tr>
          <tr>
             <td>
             </td>
             <td>
				<input name="_search" type="submit" class="button" tabindex="6" value="Search" />
				<input name="reset" type="reset" class="button" tabindex="7" value="Cancel"
							onclick="javascript: window.location='p_listconcerns.html?actionId=${retrieveAction}'" />
             </td>
          </tr>
          </html:form>
      </table>
      
      <ec:table items="concernsList" var = "concernListViewModel" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit"
     	 action="${contextPath}/p_listconcerns.html?actionId=${retrieveAction}" filterable="false">

		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="List Concerns.xls" tooltip="Export Excel"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="List Concerns.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="List Concerns" fileName="List Concerns.pdf" tooltip="Export PDF" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="List Concerns.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>	

         <ec:row>
           <ec:column property="concernCode" title="Concern Code" filterable="false">
              
              <input type="hidden" name="actionId" value="<%=PortalConstants.ACTION_RETRIEVE%>" >

			  <authz:authorize ifAnyGranted="<%=allowedUpdatePermission %>">  
			       <a href="${pageContext.request.contextPath}/p_concerndetailform.html?actionId=2" onclick="return openConcernWindow('${concernListViewModel.concernCode }','${concernListViewModel.concernId}')">
                       <c:out value="${concernListViewModel.concernCode}"></c:out>
              </authz:authorize>
              <authz:authorize ifNotGranted="<%=allowedUpdatePermission %>">          
		          <c:out value="${concernListViewModel.concernCode}"></c:out>
              </authz:authorize>    
              
           </ec:column>
           <ec:column property="title" title="Title" filterable="false"/>
           <ec:column property="initiatorPartnerName" title="Raised By" filterable="false"/>
           <ec:column property="concernCategoryName" title="Category" filterable="false"/>
           <ec:column property="concernStatusName" title="Status" filterable="false"/>
           <ec:column property="concernPriorityName" title="Priority" filterable="false"/>
         </ec:row>
      </ec:table>

     <script language="javascript" type="text/javascript">
         function openConcernWindow(concernCode, concernId)
		 {
		    var popupWidth = 650;
		 	var popupHeight = 400;
		 	var popupLeft = (window.screen.width - popupWidth)/2;
		 	var popupTop = (window.screen.height - popupHeight)/2;
		    newWindow = window.open('p_concerndetailform.html?actionId=2&concernCode='+concernCode+'&concernId='+concernId+'&<%=ConcernsConstants.KEY_CONCERN_PAGE%>=<%=ConcernsConstants.PAGE_LIST_CONCERN%>','ConcernDetail','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+'   ,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		 	if(window.focus) newWindow.focus();
		 	return false;
		 }
	  </script> 

   </body>
</html>



