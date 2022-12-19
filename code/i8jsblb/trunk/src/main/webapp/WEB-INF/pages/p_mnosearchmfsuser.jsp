<!--Author: Jalil-Ur-Rehman -->
<jsp:directive.page import="com.inov8.microbank.common.util.PortalConstants" />
<%@include file="/common/taglibs.jsp"%>

<html>
	<head>
<meta name="decorator" content="decorator">

		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<meta name="title" content="Search MWallet User" />
		<script type="text/javascript"
			src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
		<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
	</head>

	<body bgcolor="#ffffff">

		<div id="successMsg" class="infoMsg" style="display:none;"></div>
		<spring:bind path="mnoSearchMfsUsrListViewModel.*">
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
			<html:form name="changemobileForm" method="post"
				commandName="mnoSearchMfsUsrListViewModel"
				onsubmit="return validateForm()">

				<tr>


					<td width="33%" align="right" class="formText">
						Inov8 MWallet ID:
					</td>
					<td width="17%" align="left">
						<html:input onkeypress="return maskCommon(this,event)"
							path="userId" tabindex="1" id="userId" cssClass="textBox"
							maxlength="8" />
					</td>
					<td width="16%" align="left">
						&nbsp;
					</td>
					<td width="34%" align="left"></td>

				</tr>
				<tr>

					<td class="formText" align="right">
						Mobile #:
					</td>
					<td align="left">
						<html:input onkeypress="return maskCommon(this,event)"
							path="mobileNo" tabindex="2" id="mobileNo" cssClass="textBox"
							maxlength="11" />
					</td>
					<td align="left">
						&nbsp;
					</td>
					<td align="left">
					</td>

				</tr>
				<tr>
					<td></td>
					<td align="left">
						<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
						<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
							value="${retriveAction}" />
						<security:isauthorized action="<%=PortalConstants.ACTION_RETRIEVE%>">
							<jsp:attribute name="ifAccessAllowed">
								<input type="submit" class="button" value="Search" tabindex="3" name="_search" />
							</jsp:attribute>
							<jsp:attribute name="ifAccessNotAllowed">
								<input type="submit" class="button" value="Search" tabindex="-1"
									name="_search"
									disabled="disabled" />
							</jsp:attribute>
						</security:isauthorized>

						<input type="reset" class="button" value="Cancel" tabindex="4"
							name="_cancel"
							onClick="javascript: window.location='p_mnosearchmfsuser.html?actionId=${retriveAction}'">
					</td>
					<td align="left">
						&nbsp;
					</td>
					<td align="left">
						&nbsp;
					</td>


				</tr>



			</html:form>
		</table>
		<ec:table filterable="false" items="mnoSearchUserList"
			var="mnoSearchUserListViewModel" retrieveRowsCallback="limit"
			filterRowsCallback="limit" sortRowsCallback="limit"
			action="${pageContext.request.contextPath}/p_mnosearchmfsuser.html"
			title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="MWallet User.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="MWallet User.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf headerBackgroundColor="#b6c2da"
					headerTitle="MWallet User" view="com.inov8.microbank.common.util.CustomPdfView" 
					fileName="MWallet User.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="MWallet User.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>

			<ec:row>
				<ec:column property="userId" filterable="false" title="Inov8 MWallet ID" />
				<ec:column property="mobileNo" filterable="false" title="Mobile #" escapeAutoFormat="true"/>
				<ec:column property="lockStatus" title="Action"
					style="text-align: center">
					
						<security:isauthorized action="<%=PortalConstants.ACTION_UPDATE%>">
							<jsp:attribute name="ifAccessAllowed">
								<tags:activatedeactivate
									id="${mnoSearchUserListViewModel.userDeviceAccountsId}"
									model="com.inov8.microbank.common.model.UserDeviceAccountsModel"
									property="accountLocked"
									propertyValue="${mnoSearchUserListViewModel.accountLocked}"
									callback="actdeact" error="defaultError" activeMessage="locked"
									deactiveMessage="Unlocked" activeLabel="   Lock  "
									deactiveLabel=" Unlock " isButton="true" />
							</jsp:attribute>
							<jsp:attribute name="ifAccessNotAllowed">
								<c:choose>
									<c:when test="${mnoSearchUserListViewModel.accountLocked}">
										<c:set var="label" value="  UnLock  "/>
									</c:when>
									<c:otherwise>
										<c:set var="label" value="    Lock    "/>
									</c:otherwise>
								</c:choose>
								<input type="submit" class="button" value="${label}" tabindex="-1"
									name="_search"
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
























































