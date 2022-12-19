<!--Author: Mohammad Shehzad Ashraf -->
<jsp:directive.page import="com.inov8.microbank.common.util.*"/>
<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
   <head>
<meta name="decorator" content="decorator">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
      <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
      <meta name="title" content="My Concerns"/>
      <script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
			
		<script type="text/javascript">
			var jq=$.noConflict();
			var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		</script>
	</script>
			
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

           	String allowedUpdatePermission = PortalConstants.MNG_MY_CNCRN_UPDATE;
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

           	String allowedCreatePermission = PortalConstants.MNG_MY_CNCRN_CREATE;
           	allowedCreatePermission += ",";
           	allowedCreatePermission += PortalConstants.MNO_GP_CREATE;
           	allowedCreatePermission += ",";
           	allowedCreatePermission += PortalConstants.PAS_GP_CREATE;
           	allowedCreatePermission += ",";             	             	             	             	
           	allowedCreatePermission += PortalConstants.PRS_GP_CREATE; 
           	allowedCreatePermission += ",";             	             	             	             	
           	allowedCreatePermission += PortalConstants.CSR_GP_CREATE; 
           	allowedCreatePermission += ",";             	             	             	             	
           	allowedCreatePermission += PortalConstants.RET_GP_CREATE;	
            %>
   </head>
   <body bgcolor="#ffffff">

	<c:set var="retrieveAction" value="<%=PortalConstants.ACTION_RETRIEVE%>" />
	<c:set var="createAction" value="<%=PortalConstants.ACTION_CREATE%>" />

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
      
      
      
          <html:form name='transactionDetailForm'
				commandName="concernListViewModel" method="post"
				action="p_myconcerns.html?actionId=${retrieveAction}"
				onsubmit="return validateForm()"  >
          <table width="750px"><tr>
              <td align="right" class="formText" width="33%">Concern Code:</td>
              <td width="17%"><html:input path="concernCode"
							onkeypress="return maskCommon(this,event)" id="concernCode"
							cssClass="textBox" tabindex="1" maxlength="10" /></td>
			  <td align="right" class="formText" width="16%">Raised To:</td>
              <td width="34%">
                  <html:select path="recipientPartnerName" tabindex="2" cssClass="textBox">
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
							onclick="javascript: window.location='p_myconcerns.html?actionId=${retrieveAction}'" />
             </td>
             <td colspan="2"></td>
          </tr>
          <tr>
             <td class="formText" align="right" colspan="4">
             	<authz:authorize ifAnyGranted="<%=allowedCreatePermission %>">          
						<a href="p_createconcernform.html?actionId=1" class="linktext" onclick="" align="right">Add Concern </a> <br/>
	             </authz:authorize>
	             <authz:authorize ifNotGranted="<%=allowedCreatePermission %>">          
					         &nbsp;
	             </authz:authorize>
             </td>
          </tr>
          </table> </html:form>
     
      <ec:table
      items="concernsList"
      var = "concernListViewModel"
      retrieveRowsCallback="limit"
      filterRowsCallback="limit"
      sortRowsCallback="limit"
      action="${pageContext.request.contextPath}/p_myconcerns.html?actionId=${retrieveAction}"
      title=""   filterable="false" >

      <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
         <ec:exportXls fileName="My Concerns.xls" tooltip="Export Excel"/>
      </authz:authorize>
      <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
	  		<ec:exportXlsx fileName="My Concerns.xlsx" tooltip="Export Excel" />
		</authz:authorize>
      <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
      	<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="My Concerns" fileName="My Concerns.pdf" tooltip="Export PDF" />
      </authz:authorize>
      <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
		<ec:exportCsv fileName="My Concerns.csv" tooltip="Export CSV"></ec:exportCsv>
	  </authz:authorize>

         <ec:row>
           <ec:column property="concernCode" title="Concern Code" filterable="false" width="10%">
           
              <input type="hidden" name="actionId" value="<%=PortalConstants.ACTION_RETRIEVE%>">              
              
              <authz:authorize ifAnyGranted="<%=allowedUpdatePermission %>">  
			       <a href="${pageContext.request.contextPath}/p_concerndetailform.html?actionId=2" onclick="return openConcernWindow('${concernListViewModel.concernCode }','${concernListViewModel.concernId}')">
                       <c:out value="${concernListViewModel.concernCode}"></c:out>
              </authz:authorize>
              <authz:authorize ifNotGranted="<%=allowedUpdatePermission %>">          
		         <c:out value="${concernListViewModel.concernCode}"></c:out>
              </authz:authorize>              
              
           </ec:column>
           <ec:column property="title" title="Title" filterable="false" width="50%" style="word-break: break-all;"/>
           <ec:column property="recipientPartnerName" title="Raised To" filterable="false" width="10%"/>
           <ec:column property="concernCategoryName" title="Category" filterable="false" width="10%"/>
           <ec:column property="concernStatusName" title="Status" filterable="false" width="10%"/>
           <ec:column property="concernPriorityName" title="Priority" filterable="false" width="10%"/>

           
         </ec:row>
      </ec:table>

      <script language="javascript" type="text/javascript">
            document.getElementById('concernCode').focus();
             
         function openConcernWindow(concernCode, concernId)
		 {
		    var popupWidth = 650;
		 	var popupHeight = 400;
		 	var popupLeft = (window.screen.width - popupWidth)/2;
		 	var popupTop = (window.screen.height - popupHeight)/2;
		    newWindow = window.open('p_concerndetailform.html?actionId=2&concernCode='+concernCode+'&concernId='+concernId+'&<%=ConcernsConstants.KEY_CONCERN_PAGE%>=<%=ConcernsConstants.PAGE_MY_CONCERN%>'  ,'ConcernDetail','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		 	if(window.focus) newWindow.focus();
		 	return false;
		 }
            
           
	  </script>
	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"
   </body>
</html>



