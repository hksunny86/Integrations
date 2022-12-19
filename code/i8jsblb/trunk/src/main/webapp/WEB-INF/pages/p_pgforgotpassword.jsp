<!--Title: i8Microbank-->
<jsp:directive.page import="com.inov8.microbank.common.util.PortalConstants"/>
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="Forgot Web Password" />
		<%@include file="/common/ajax.jsp"%>	
	<script type="text/javascript">
		function initProgress()
		{
			if (confirm('If customer information is verified then press OK to continue')==true)
		    {
		    	return true;
		    }
		    else
			{
			  $('errorMsg').innerHTML = "";
			  $('successMsg').innerHTML = "";
			  Element.hide('successMsg');
			  Element.hide('errorMsg'); 
			  return false;
			}
		}
		
	var isErrorOccured = false;
		function resetProgress()
		{
		    if(!isErrorOccured)
		    {
			    // clear error box
			    $('errorMsg').innerHTML = "";
		  		Element.hide('errorMsg');
			    // display success message
			    Element.show('successMsg');
			} 
				isErrorOccured = false;
		}

  
		function reportError(request) 
		{
		  var msg = "The PIN could not be generated";
		  $('successMsg').innerHTML = "";
		  Element.hide('successMsg');           
		  $('errorMsg').innerHTML = msg;
		  Element.show('errorMsg');
		  isErrorOccured = true;
		}
	</script>
		
	</head>

	<body bgcolor="#ffffff">
		<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<div id="errorMsg" class ="errorMsg" style="display:none;"></div>

		<spring:bind path="forgotpinListViewModel.*"> 
  			
  				<c:if test="${not empty status.errorMessages}">
    				<div class="errorMsg">
      					<c:forEach var="error" items="${status.errorMessages}">
        					<c:out value="${error}" escapeXml="false" /><br/>
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
		
				<table width="100%">
		<html:form name="mnoforgotPin" commandName="forgotpinListViewModel" onsubmit="return validateForm()" >
				<tr>
					<td width="33%" align="right" class="formText">First Name:</td>
					<td width="17%" align="left"><html:input tabindex="1" path="firstName" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="50"/></td>
					<td width="16%" align="right"><span class="formText">Last Name:</span></td>
					<td width="34%" align="left">
						<html:input tabindex="2" path="lastName" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="50"/>
					</td>

				</tr>
				<tr>
				  <td class="formText" align="right"> Inov8 MWallet ID: </td>
					<td align="left"><html:input tabindex="3" path="userId" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="8"/></td>
					<td align="left">&nbsp;</td>
					<td align="left">
						
					</td>
				</tr>
				<tr>
				  <td class="formText" align="right"> NIC #: </td>
					<td align="left"><html:input tabindex="4" path="nic" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="13"/></td>
					<td align="left">&nbsp;</td>
					<td align="left">
						
					</td>
				</tr>


				<tr>
					<td class="formText" align="right"></td>
					<td align="left">
					<authz:authorize ifAnyGranted="<%=PortalConstants.FRGT_GEN_PIN_READ%>">
						<input tabindex="5" type="submit" name="sumbit" value="Search"	class="button" /> 
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.FRGT_GEN_PIN_READ%>">
						<input type="submit" name="sumbit" value="Search" class="button" tabindex="-1" disabled="disabled" /> 
					</authz:authorize>
					
					<input tabindex="6" type="reset" name="cancel" value="Cancel" class="button" onclick="window.location='p_pgforgotpassword.html'" />
					</td>
					<td align="left">&nbsp;</td>
					<td align="left">&nbsp;
					</td>
				</tr>

			</html:form>
						</table>	
		
			<ec:table filterable="false"
				items="mfsRecord" 
				var="forgotpinListViewModel" 
				retrieveRowsCallback="limit" 
				filterRowsCallback="limit" 
				sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/p_gmnoforgotpassword.html?actionId=${retriveAction}"
				title="">
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
					<ec:exportXls fileName="Forgot Web Password.xls" tooltip="Export Excel" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
					<ec:exportXlsx fileName="Forgot Web Password.xlsx" tooltip="Export Excel" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
					<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
						headerTitle="Forgot Web Password" fileName="Forgot Web Password.pdf" tooltip="Export PDF" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
    				<ec:exportCsv fileName="Forgot Web Password.csv" tooltip="Export CSV"></ec:exportCsv>
    			</authz:authorize>
  				<ec:row>
					<c:set var="forgotpinListAppUserId">
						<security:encrypt strToEncrypt="${forgotpinListViewModel.appUserId}"/>
					</c:set>
 
    				<ec:column property="fullName" filterable="false" title="Name"/>
    				<ec:column property="userId" filterable="false" title="Inov8 MWallet ID"/>
    				<ec:column property="mobileNo" filterable="false" title="Mobile #" escapeAutoFormat="true"/>
     				<ec:column property="name" filterable="false" title="Type"/>
					<ec:column property="fullAddress" filterable="false" title="Address"/>

									<ec:column property="dob" filterable="false" title="DOB" cell="date"
					format="dd/MM/yyyy" />
					<ec:column property="nic" filterable="false" title="NIC #" escapeAutoFormat="true"/>
    		 		<ec:column alias="" filterable="false" sortable="false" viewsAllowed="html" style="text-align: center;">
					
					
					<security:isauthorized action="<%=PortalConstants.ACTION_UPDATE%>">
						<jsp:attribute name="ifAccessAllowed">
						<c:if test="${empty forgotpinListViewModel.password}">
						
						<c:if test="${forgotpinListViewModel.accountLocked}">
					<input type="button"  class="button" value="         Locked        " id="chgPin${forgotPinAppUserId}" disabled="disabled"/>
					</c:if>	          
					
					<c:if test="${not forgotpinListViewModel.accountLocked}">
						<input type="button"  class="button" value="Generate Password" id="chgPin${forgotpinListAppUserId}"/>
						</c:if>
						</c:if>
						<c:if test="${not empty forgotpinListViewModel.password}">
						
												<c:if test="${forgotpinListViewModel.accountLocked}">
					<input type="button"  class="button" value="         Locked        " id="chgPin${forgotPinAppUserId}" disabled="disabled"/>
					</c:if>	          
					
												<c:if test="${not forgotpinListViewModel.accountLocked}">
					
						<input type="button"  class="button" value="  Change Password  " id="chgPin${forgotpinListAppUserId}"/>
						</c:if>
						</c:if>
					   	</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed"> 
						 <input type="button"  class="button" value="Generate Password" id="chgPin${forgotpinListAppUserId}" disabled="disabled"/>    
						</jsp:attribute>
			        </security:isauthorized>  
					
					<input type="hidden" value="${forgotpinListAppUserId}" name="appUser${forgotpinListAppUserId}" id="appUser${forgotpinListAppUserId}" />
						<input type="hidden" id="usecaseId${forgotpinListAppUserId}" value="<%=PortalConstants.PG_FORGOT_PASSWORD_USECASE_ID%>"/>	
						<input type="hidden" id="actionId${forgotpinListAppUserId}" value="<%=PortalConstants.ACTION_UPDATE%>"/>
						<ajax:htmlContent baseUrl="${contextPath}/p_pgforgotpasswordupdate.html" 
							eventType="click" 
							source="chgPin${forgotpinListAppUserId}" 
							target="successMsg" 
							parameters="appUserId={appUser${forgotpinListAppUserId}},usecaseId={usecaseId${forgotpinListAppUserId}},actionId={actionId${forgotpinListAppUserId}}"
							errorFunction="reportError"
							preFunction="initProgress"
							postFunction="resetProgress"
							/>
    		 		<%-- 
						<input type="button" class="button" value="Change PIN" onclick="javascript:confirmChangePin('${pageContext.request.contextPath}/p_mnoforgotpinform.html?appUserId=${forgotpinListViewModel.appUserId}&actionId=3&usecaseId=1011');"/>
					 --%>						
    				</ec:column>
      			</ec:row>		
      		</ec:table>	
				
			<script language="javascript" type="text/javascript">
			    highlightFormElements();
			    document.forms[0].userId.focus();
/*				function confirmChangePin(link)
				{
    				if (confirm('If customer information is verified then press OK to continue')==true)
    				{
      					window.location.href=link;
    				}
				}
*/				

			function validateForm(){
	        	return validateFormChar(document.forms[0]);
	        }
				

			</script>
	
	
	</body>
</html>
