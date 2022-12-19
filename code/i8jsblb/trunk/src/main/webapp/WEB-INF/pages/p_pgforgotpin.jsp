<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>

<html>

	<head>
<meta name="decorator" content="decorator">
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<meta name="title" content="Forgot MWallet PIN" />
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


		<table width="100%" >
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
					
					<input tabindex="6" type="reset" name="cancel" value="Cancel" class="button" onclick="window.location='p_pgforgotpin.html'" />
					</td>
					<td align="left">&nbsp;</td>
					<td align="left">&nbsp;
					</td>
				</tr>

			</html:form>
		</table>


		<ec:table filterable="false" items="mfsRecord"
			var="forgotpinListViewModel" retrieveRowsCallback="limit"
			filterRowsCallback="limit" sortRowsCallback="limit"
			action="${pageContext.request.contextPath}/p_pgforgotpin.html"
			title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.EXP_RPT_PDF_XLS_READ%>">
				<ec:exportXls fileName="Forgot MWallet PIN.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Forgot MWallet PIN.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerTitle="Forgot MWallet PIN"
					headerBackgroundColor="#b6c2da" fileName="Forgot MWallet PIN.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName=Forgot MWallet PIN.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<c:set var="forgotPinAppUserId">
					<security:encrypt strToEncrypt="${forgotpinListViewModel.appUserId}"/>
				</c:set>
			
				<ec:column filterable="false" title="Name" property="fullName" />

				<ec:column property="userId" filterable="false" title="Inov8 MWallet ID" />

				<ec:column property="mobileNo" filterable="false" title="Mobile #" escapeAutoFormat="true"/>
				<ec:column property="name" filterable="false" title="Type" />
				<ec:column property="fullAddress"  sortable="false"  filterable="false" title="Address" />
				<ec:column property="dob" filterable="false" title="DOB" cell="date"
					format="dd/MM/yyyy" />
				<ec:column property="nic" filterable="false" title="NIC #" escapeAutoFormat="true"/>
				<ec:column alias=" " filterable="false" sortable="false" viewsAllowed="html">

				<authz:authorize ifAnyGranted="<%=PortalConstants.FRGT_GEN_PIN_UPDATE%>">
					<c:if test="${not forgotpinListViewModel.accountLocked}">
					<input type="button"  class="button" value="Change PIN" id="chgPin${forgotPinAppUserId}"/>
					</c:if>	    
					<c:if test="${forgotpinListViewModel.accountLocked}">
					<input type="button"  class="button" value="Locked" id="chgPin${forgotPinAppUserId}" disabled="disabled"/>
					</c:if>	          
				</authz:authorize>
	            <authz:authorize ifNotGranted="<%=PortalConstants.FRGT_GEN_PIN_UPDATE%>">	            
					<input type="button"  class="button" value="Change PIN" id="chgPin${forgotPinAppUserId}" disabled="disabled" />
	            </authz:authorize>
	            	         
					<input type="hidden" value="${forgotPinAppUserId}" name="appUser${forgotPinAppUserId}" id="appUser${forgotPinAppUserId}" />
					<input type="hidden" id="usecaseId${forgotPinAppUserId}" value="<%=PortalConstants.PG_FORGOT_PIN_USECASE_ID%>"/>	

					<ajax:htmlContent baseUrl="${contextPath}/p_pgforgotpinupdate.html" 
						eventType="click" 
						source="chgPin${forgotPinAppUserId}" 
						target="successMsg" 
						parameters="appUserId={appUser${forgotPinAppUserId}},usecaseId={usecaseId${forgotPinAppUserId}}"
						errorFunction="reportError"
						preFunction="initProgress"
						postFunction="resetProgress"
						/>
					<%--
					<input type="button" class="button" value="Change PIN"
						onclick="javascript:confirmChangePin('${pageContext.request.contextPath}/p_pgforgotpinupdate.html?appUserId=${forgotpinListViewModel.appUserId}&forgotpinflag=1&<%=PortalConstants.KEY_ACTION_ID%>=3&<%=PortalConstants.KEY_USECASE_ID%>=<%=PortalConstants.PG_FORGOT_PIN_USECASE_ID%>');" />
					--%>	
				</ec:column>
			</ec:row>
		</ec:table>

		<script language="javascript" type="text/javascript">
			highlightFormElements();
			document.forms[0].firstName.focus();
			
			function validateForm(){
	        	return validateFormChar(document.forms[0]);
	        }
			

/*			function confirmChangePin(link)
			{
    			if (confirm('If customer information is verified then press OK to continue')==true)
    			{
      				window.location.href=link;
    			}
			}
*/			
		</script>

	</body>
</html>
