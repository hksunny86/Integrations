<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Mohammad Shehzad Ashraf-->
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<html>

	<head>
<meta name="decorator" content="decorator">
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<meta name="title" content="De-Activate/Re-Activate MWallet Account" />
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
			  $('message').value="";
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
			    $('successMsg').innerHTML = $F('message');
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
	
		<!--
        <div align="right"><a href="productform.html" class="linktext">Add Product</a>&nbsp;&nbsp;</div>
	   -->
		<spring:bind path="userInfoListViewModel.*">

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
			<html:form name="userInfoListViewForm"
							commandName="userInfoListViewModel" onsubmit="return validateForm()" >

				<tr>
					<td width="33%" align="right" class="formText">
					First Name:</td>
					<td width="17%" align="left"><html:input path="firstName" onkeypress="return maskCommon(this,event)" cssClass="textBox" tabindex="1" maxlength="50"/></td>
					<td width="16%" align="right"><span class="formText">Last Name: </span></td>
					<td width="34%" align="left"><html:input path="lastName" onkeypress="return maskCommon(this,event)" cssClass="textBox" tabindex="2" maxlength="50"/>
						
					</td>
				</tr>

				<tr>
					<td align="right" class="formText">
					Inov8 MWallet ID:</td>
					<td align="left"><html:input path="userId" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="8" tabindex="3" /></td>
					<td align="left">&nbsp;</td>
					<td align="left">
						
					</td>
				</tr>

				<tr>
					<td align="right" class="formText">NIC #:
					</td>
					<td align="left"><html:input path="nic" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="13" tabindex="4" /></td>
					<td align="left">&nbsp;</td>
					<td align="left">
						
					</td>
				</tr>

				<tr>
					<td align="right" class="formText">&nbsp;
						
					</td>

					<td align="left" class="formText">
					
					<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_GEN_ACC_READ%>">
						<input type="submit" class="button" value="Search" name="_search"
							onClick="/*javascript:resetExportParameters('userInfoListViewForm');*/" tabindex="5" />
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.MNG_GEN_ACC_READ%>">
						<input type="submit" class="button" value="Search" name="_search"
								tabindex="-1" disabled="disabled" />
					</authz:authorize>
					
                      <input type="reset" class="button" value="Cancel" name="_reset"
							onclick="javascript: window.location='p_pgdeactivatemfsaccount.html'" tabindex="6" ></td>
					<td align="left" class="formText">&nbsp;</td>
					<td align="left" class="formText">&nbsp;
					</td>

				</tr>
		</table>						
		
	</html:form>

		<ec:table filterable="false" items="userInfoListViewModelList"
			var="userInfoListViewModel" retrieveRowsCallback="limit"
			filterRowsCallback="limit" sortRowsCallback="limit"
			action="${pageContext.request.contextPath}/p_pgdeactivatemfsaccount.html"
			title="">

			<authz:authorize ifAnyGranted="<%=PortalConstants.EXP_RPT_PDF_XLS_READ%>">
				<ec:exportXls fileName="De-Activate/Re-Activate MWallet Account.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="De-Activate/Re-Activate MWallet Account.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="De-Activate/Re-Activate MWallet Account" fileName="De-Activate/Re-Activate MWallet Account.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="De-Activate/Re-Activate MWallet Account.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>

			<ec:row>
			<c:set var="userInfoUserDeviceAccountsId">
				<security:encrypt strToEncrypt="${userInfoListViewModel.userDeviceAccountsId}"/>
			</c:set>
			
			<ec:column property="fullName" title="Name" filterable="false" />
			<ec:column property="userId" title="Inov8 MWallet ID" filterable="false"  />
			<ec:column filterable="false" property="mobileNo" title="Mobile #" escapeAutoFormat="true"/>
			<ec:column filterable="false" property="name" title="Type" />
			<ec:column property="fullAddress" sortable="false" filterable="false" title="Address" />
				<ec:column property="dob" filterable="false" cell="date" format="dd/MM/yyyy" alias="DOB" />
				<ec:column property="nic" filterable="false" title="NIC #" escapeAutoFormat="true"/>
				<ec:column alias=" "  viewsAllowed="html" filterable="false" sortable="false"  style="vertical-align: middle">
					 <c:choose>
					 	<c:when test="${userInfoListViewModel.accountEnabled}">    
					 	
						 	 <authz:authorize ifAnyGranted="<%=PortalConstants.MNG_GEN_ACC_UPDATE%>">
						 	 <c:if test="${not userInfoListViewModel.isAccountLocked}">	
								<input id="btn_${userInfoUserDeviceAccountsId}" type="button" class="button" value="De-Activate"/>
							</c:if>	
							<c:if test="${userInfoListViewModel.isAccountLocked}">	
								<input id="btn_${userInfoUserDeviceAccountsId}" type="button" class="button" value="Locked" disabled="disabled"/>
							</c:if>
				            </authz:authorize>
				            <authz:authorize ifNotGranted="<%=PortalConstants.MNG_GEN_ACC_UPDATE%>">	            
								<input id="btn_${userInfoUserDeviceAccountsId}" type="button" class="button" value="De-Activate" disabled="disabled" />
				            </authz:authorize>
				            
					 	</c:when>
					 	<c:otherwise>

						 	 <authz:authorize ifAnyGranted="<%=PortalConstants.MNG_GEN_ACC_UPDATE%>">
						 	 <c:if test="${not userInfoListViewModel.isAccountLocked}">
								<input id="btn_${userInfoUserDeviceAccountsId}" type="button" class="button" value="Re-Activate" />
								</c:if>
								<c:if test="${userInfoListViewModel.isAccountLocked}">
								<input id="btn_${userInfoUserDeviceAccountsId}" type="button" class="button" value="Locked"  disabled="disabled"/>
								</c:if>
				            </authz:authorize>
				            <authz:authorize ifNotGranted="<%=PortalConstants.MNG_GEN_ACC_UPDATE%>">	            
								<input id="btn_${userInfoUserDeviceAccountsId}" type="button" class="button" value="Re-Activate" disabled="disabled" />
				            </authz:authorize>
					 	
					 	</c:otherwise>
					</c:choose>	
						<input type="hidden" id="usecaseId" value="<%=PortalConstants.DEACTIVATE_CUSTOMER_USECASE_ID%>"/>	
						<input type="hidden" id="message" value=""/>	
						<input id="usrDevAccId_${userInfoUserDeviceAccountsId}" type="hidden"
							value="${userInfoUserDeviceAccountsId}"/>

						<ajax:updateField
						  baseUrl="${contextPath}/p_pgdeactivatedeactivate.html"
						  source="btn_${userInfoUserDeviceAccountsId}"
						  target="btn_${userInfoUserDeviceAccountsId},message"
						  action="btn_${userInfoUserDeviceAccountsId}"
						  parameters="usrDevAccId={usrDevAccId_${userInfoUserDeviceAccountsId}},usecaseId={usecaseId},btn=btn_${userInfoUserDeviceAccountsId}"
						  parser="new ResponseXmlParser()"
						  preFunction="initProgress"
						  postFunction="resetProgress" 
						  errorFunction="globalAjaxErrorFunction"
						  />						
				</ec:column>

			</ec:row>
		</ec:table>
		<script language="javascript" type="text/javascript">
	        highlightFormElements();
	        document.forms[0].firstName.focus();
		        
	        function validateForm(){
	        	return validateFormChar(document.forms[0]);
	        }
		        
			</script>
	</body>
</html>
