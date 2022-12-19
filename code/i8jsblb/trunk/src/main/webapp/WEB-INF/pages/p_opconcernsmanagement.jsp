<!--Author: Basit Mehr -->
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>

<html>
  <head>
<meta name="decorator" content="decorator">
    
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Concerns List" />
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
<script type="text/javascript">
	var jq=$.noConflict();
	var serverDate ="<%=PortalDateUtils.getServerDate()%>";
</script>
	<%
		String readPermission = PortalConstants.CSR_GP_READ;
		readPermission +=	"," + PortalConstants.PG_GP_READ;
		readPermission +=	"," + PortalConstants.CONCERNS_LIST_READ;
	%>
  </head>
  
  <body bgcolor="#ffffff">
   <c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

       
          <html:form name='transactionDetailForm'
				commandName="concernsParentListViewModel" method="post"
				action="p_opconcernsmanagement.html?actionId=${retriveAction}"
				onsubmit="return validateForm()">
           <table width="750px"><tr>
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
              <td align="right" class="formText">Raised To:</td>
              <td>
                   <html:select path="recipientPartnerName" tabindex="2" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<c:if test="${concernPartnerModelList != null}">
								<html:options items="${concernPartnerModelList}"
									itemValue="name" itemLabel="name" />
							</c:if>
						</html:select>
              </td>
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
          </tr>
          <tr>
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
          </tr>
          <tr>
             <td>
             </td>
             <td>
                <input name="_search" type="submit" class="button" tabindex="6"
									value="Search" />
				<input name="reset" type="reset" class="button" tabindex="7"
							value="Cancel"
							onclick="javascript: window.location='p_opconcernsmanagement.html?actionId=${retriveAction}'" />
             </td>
          </tr>
         </table>
         </html:form>
      
  
    
    	<ec:table 
		items="concernModelList" 
                var = "concernModel1"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/p_opconcernsmanagement.html?actionId=${retriveAction}"
				title="" filterable="false">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Concerns List.xls" tooltip="Export Excel"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Concerns List.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Concerns List" fileName="concernsList.pdf" tooltip="Export PDF"  />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Concerns List.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
		<ec:row>
		
			<ec:column property="concernCode" filterable="false" >

				  <input type="hidden" name="actionId" value="<%=PortalConstants.ACTION_RETRIEVE%>">


					 <authz:authorize ifAnyGranted="<%=readPermission%>">
				        <a href="${pageContext.request.contextPath}/p_opconcernsdetail.html?actionId=${retriveAction}" onclick="return openConcernWindow('${concernModel1.concernCode }','${concernModel1.concernId}','${concernModel1.recipientPartnerId}','${concernModel1.initiatorPartnerId}')"  >
				            <c:out value="${concernModel1.concernCode}"></c:out>
				        </a>
					 </authz:authorize>	 	 
					 <authz:authorize ifNotGranted="<%=readPermission%>">
				         <c:out value="${concernModel1.concernCode}"></c:out>
					 </authz:authorize>	 	 

		    </ec:column>
		    <ec:column  property="title"  filterable="false" width="40%" style="word-break: break-all;" />
			<ec:column  property="initiatorPartnerName" title="Raised By" filterable="false" />
			<ec:column  property="recipientPartnerName" title="Raised To" filterable="false" />
			<ec:column  property="concernCategoryName" title="Category" filterable="false" />
			<ec:column  property="concernStatusName" title="Status" filterable="false" />
			<ec:column  property="secondaryActiveStatusName" title="Secondary Status" filterable="false" />
			<ec:column  property="concernPriorityName"  title="Priority" filterable="false" />
			
        </ec:row>
	</ec:table>
    
    
    
	
    <script language="javascript" type="text/javascript">
            
         function openConcernWindow(concernCode, concernId, paramRecipientPartnerId, paramInitiatorPartnerId)
			{
		        var popupWidth = 650;
		 		var popupHeight = 400;
		 		var popupLeft = (window.screen.width - popupWidth)/2;
		 		var popupTop = (window.screen.height - popupHeight)/2;
		        newWindow=window.open('p_opconcernsdetail.html?actionId=${retriveAction}&concernCode='+concernCode+'&concernId='+concernId+'&paramRecipientPartnerId='+paramRecipientPartnerId+'&paramInitiatorPartnerId='+paramInitiatorPartnerId,'ConcernDetail', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) newWindow.focus();
			    if(newWindow.opener == null){
			    	newWindow.close();
			    }
			    return false;
			}
            
            
	</script>
	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>  
  </body>
</html>
