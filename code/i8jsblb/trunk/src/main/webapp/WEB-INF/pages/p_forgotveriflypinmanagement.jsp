<!--Author: Basit Mehr -->
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<html>
	<head>
<meta name="decorator" content="decorator">
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<meta name="title" content="Forgot Account PIN" />
		<%@include file="/common/ajax.jsp"%>
	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>	
	<script type="text/javascript">
		var jq=$.noConflict();
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
	</script>
		
	</head>

	<body bgcolor="#ffffff">
		<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<div id="errorMsg" class ="errorMsg" style="display:none;"></div>
		
		<spring:bind path="forgotVeriflyPinViewModel.*">
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

		<table width="100%">
			<html:form name="forgotVeriflyPinForm"
				action="p_forgotveriflypinmanagement.html" method="post"
				commandName="forgotVeriflyPinViewModel" onsubmit="return validateForm()" >
				
				<input type="hidden" id="fromAppUserId" name="fromAppUserId" value="${fromAppUserId}" />				
				
				<tr>
					<td width="33%" align="right" class="formText">
						First Name:
					</td>
					<td width="17%" align="left">
						<c:choose>
							<c:when test="${not empty param.fromAppUserId}">
								<html:input tabindex="1" readonly="true" path="firstName" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="50" />
							</c:when>					
							<c:otherwise>
								<html:input tabindex="1" path="firstName" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="50" />
							</c:otherwise>
						</c:choose>					
					</td>
					<td width="16%" align="right"><span class="formText">Last Name:</span></td>
					<td width="34%" align="left">
						<c:choose>
							<c:when test="${not empty param.fromAppUserId}">
								<html:input path="lastName" readonly="true"  onkeypress="return maskCommon(this,event)" tabindex="2"  cssClass="textBox" maxlength="50" />
							</c:when>					
							<c:otherwise>
								<html:input path="lastName" onkeypress="return maskCommon(this,event)" tabindex="2"  cssClass="textBox" maxlength="50" />
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
				  <td class="formText" align="right"> Inov8 MWallet ID: </td>
					<td align="left">
					<c:choose>
						<c:when test="${not empty param.fromAppUserId}">
							<html:input tabindex="3" readonly="true" path="mfsId" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="8" />
						</c:when>					
						<c:otherwise>
							<html:input tabindex="3" path="mfsId" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="8" />
						</c:otherwise>
					</c:choose>
					</td>
					<td align="left">&nbsp;</td>
					<td align="left">
						
					</td>
				</tr>
				<tr>
				  <td class="formText" align="right"> NIC #: </td>
					<td align="left">
					
					
					<c:choose>
						<c:when test="${not empty param.fromAppUserId}">
							<html:input readonly="true" tabindex="4" path="nic" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="13" />
						</c:when>					
						<c:otherwise>
							<html:input tabindex="4" path="nic" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="13" />
						</c:otherwise>
					</c:choose>
					
					
					</td>
					<td align="left">&nbsp;</td>
					<td align="left">
						
					</td>
				</tr>
				<tr>
				  <td class="formText" align="right"> Account Nick: </td>
					<td align="left"><html:input tabindex="5" path="accountNick" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="50"/><c:if test="${not empty param.fromAppUserId}" >&nbsp;<img id="dobD" tabindex="6"  name="popcal" title="Clear Account Nick" align="middle" onclick="javascript:$('accountNick').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" /></c:if></td>
					<td align="left">&nbsp;</td>
					<td align="left">
					</td>
				</tr>

				<tr>
					<td class="formText" align="right"></td>
					<td align="left">
					
					<authz:authorize ifAnyGranted="<%=PortalConstants.FRGT_BNK_PIN_READ%>">
						<input tabindex="7" name="_search" type="submit" class="button" value="Search" />
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.FRGT_BNK_PIN_READ%>">
						<input name="_search" type="submit" class="button" value="Search" tabindex="-1" disabled="disabled" />					
					</authz:authorize>
					
					<c:choose>
						<c:when test="${not empty param.fromAppUserId}">
		                    <input tabindex="8" name="reset" type="reset" class="button" value="Cancel"
									onclick="window.location='p_mnonewmfsaccountform.html?appUserId=${fromAppUserId}'" />
						</c:when>					
						<c:otherwise>
		                    <input tabindex="8" name="reset" type="reset" class="button" value="Cancel"
									onclick="window.location='p_forgotveriflypinmanagement.html'" />
						</c:otherwise>
					</c:choose>
					</td>
					<td align="left">&nbsp;</td>
					<td align="left">&nbsp;
					</td>
				</tr>

				<input type="hidden" name="<%=PortalConstants.KEY_USECASE_ID%>"
					value="<%=PortalConstants.FORGOT_VERIFLY_PIN_USECASE_ID%>">
			</html:form>
		</table>

		<ec:table filterable="false" width="100%" 
			items="forgotVeriflyPinViewModelList" var="forgotVeriflyPinViewR"
			retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit" 
			action="${pageContext.request.contextPath}/p_forgotveriflypinmanagement.html"
			title="">

			<authz:authorize ifAnyGranted="<%=PortalConstants.EXP_RPT_PDF_XLS_READ%>">
				<ec:exportXls fileName="Forgot Account PIN.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Collection Payment Transactions.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Forgot Account PIN" fileName="Forgot Account PIN.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Forgot Account PIN.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<c:set var="forgotVPinSmartMoneyAccountId">
					<security:encrypt strToEncrypt="${forgotVeriflyPinViewR.smartMoneyAccountId}"/>
				</c:set>
				<c:set var="forgotVPinAppUserId">
					<security:encrypt strToEncrypt="${forgotVeriflyPinViewR.appUserId}"/>
				</c:set>

				<ec:column property="fullName" title="Name" filterable="false"	sortable="true" />
				<ec:column property="mfsId" title="Inov8 MWallet ID" sortable="true" filterable="false" width="10%" />
				<ec:column property="mobileNo" title="Mobile #" filterable="false" width="10%" escapeAutoFormat="true"/>
				<ec:column property="mobileTypeName" title="Type" filterable="false" width="10%" />
				<ec:column property="fullAddress" sortable="false" title="Address" filterable="false" width="10%" />
				<ec:column property="dob" title="DOB" cell="date" parse="yyyy-MM-dd" format="dd/MM/yyyy" filterable="false" />
				<ec:column property="accountNick" title="Account Nick" filterable="false" width="10%" />
				<ec:column property="paymentModeName" title="Payment Mode" filterable="false" width="10%" />
				<ec:column property="nic" title="NIC #" filterable="false" width="10%" escapeAutoFormat="true"/>
				<ec:column alias=" " filterable="false" sortable="false" viewsAllowed="html" width="10%">
	            
	            <authz:authorize ifAnyGranted="<%=PortalConstants.FRGT_BNK_PIN_UPDATE%>">
					<c:choose>
						   <c:when test="${veriflyRequired}">
		    			        <input type="button"  class="button" value="Change PIN" id="btnSmAcId${forgotVPinSmartMoneyAccountId}"/>   						       
						   </c:when>
						   <c:otherwise>
						       <input type="button"  class="button" value="Change PIN" id="btnSmAcId${forgotVPinSmartMoneyAccountId}" disabled="disabled"/>   
						   </c:otherwise>
					</c:choose>
	            </authz:authorize>
	            <authz:authorize ifNotGranted="<%=PortalConstants.FRGT_BNK_PIN_UPDATE%>">	            
		            <input type="button"  class="button" value="Change PIN" id="btnSmAcId${forgotVPinSmartMoneyAccountId}" disabled="disabled" />
	            </authz:authorize>
	            
		         
					<input type="hidden" value="${forgotVPinAppUserId}" name="appUser${forgotVPinAppUserId}" id="appUser${forgotVPinAppUserId}" />
					<input type="hidden" value="${forgotVPinSmartMoneyAccountId}" name="smAcId${forgotVPinSmartMoneyAccountId}" id="smAcId${forgotVPinSmartMoneyAccountId}" />
					<input type="hidden" id="usecaseId${forgotVPinAppUserId}" value="<%=PortalConstants.FORGOT_VERIFLY_PIN_USECASE_ID%>"/>		
					<ajax:htmlContent baseUrl="${contextPath}/p_forgotveriflypinChange.html" 
						eventType="click" 
						source="btnSmAcId${forgotVPinSmartMoneyAccountId}" 
						target="successMsg" 
						parameters="appUserId={appUser${forgotVPinAppUserId}},smAcId={smAcId${forgotVPinSmartMoneyAccountId}},usecaseId={usecaseId${forgotVPinAppUserId}}"
						errorFunction="globalAjaxErrorFunction"
						preFunction="initProgress"
						postFunction="resetProgress"
						/>
					<%--
					<input name="ChangePinButton" type="button" class="button"
						value="Change PIN"
						onclick="javascript:confirmChangePin('${pageContext.request.contextPath}/p_forgotveriflypinChange.html?&appUserId=${forgotVeriflyPinViewR.appUserId}&smartMoneyAccountId=${forgotVeriflyPinViewR.smartMoneyAccountId}&<%=PortalConstants.KEY_USECASE_ID%>=<%=PortalConstants.FORGOT_VERIFLY_PIN_USECASE_ID%>&<%=PortalConstants.KEY_ACTION_ID%>=3');" />
					--%>	
				</ec:column>
			</ec:row>
		</ec:table>

		<script language="javascript" type="text/javascript">
		        highlightFormElements();
		        document.forms[0].firstName.focus();
				function confirmChangePin(link)
				{
    				if (confirm('If customer information is verified then press OK to continue')==true)
    				{
      					window.location.href=link;
    				}
				}
				
	        function validateForm(){
	        	return validateFormChar(document.forms[0]);
	        }

				
		</script>


	</body>
</html>
