<!--Author: Abu Turab-->

<%@page import="org.springframework.web.bind.ServletRequestUtils"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
<meta name="decorator" content="decorator2">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-custom.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />
		<link rel="stylesheet" type="text/css" href="styles/jquery.multiselect.css" />
		<link rel="stylesheet" type="text/css" href="styles/jquery-ui-custom.min.css" />	

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="pragma" content="no-cache" />
		
				<meta name="title" content="KYC Form for Individuals" />

		<style>
                #table_form{
                                display:block;
                                width:100%;
                                height:auto;
                                margin:0px;
                                padding:0px;
                }
                #table_form .left{
                                display:block;
                                width:49%;
                                float:left;
                }
                #table_form .right{
                                display:block;
                                width:49%;
                                float:right;
                }              
		</style>
		
		<%@include file="/common/ajax.jsp"%>
		<!-- turab added below for jquery -->		

	<script>
	var jq = $.noConflict();
	jq(document).ready(
		function($) {
			jq("#modeOfTx").multiselect({
			     noneSelectedText: "[Select]",
			     minWidth : "210px"
			     });
			
			jq("#comments").keyup(function(){
			    if($(this).val().length > 250){
			        $(this).val($(this).val().substr(0, 249));
			}
			});

	});

	function onFormSubmit(theForm) {
			
			var theForm = document.forms.level2KycForm;
			
		if(doRequired( theForm.initialAppFormNo, 'Initial Application Form Number' ) 
            ){
				
			}else {
				return false;
			}
	}
	
	function isNumberKey(evt){
	    var charCode = (evt.which) ? evt.which : event.keyCode
	    if (charCode > 31 && (charCode < 48 || charCode > 57))
	        return false;
	    return true;
	}
	
    function doRequired( field, label )
    {
    	if( field.value == '' || field.value.length == 0 )
    	{
    		alert( label + ' is required field.' );
    		field.focus();
    		return false;
    	}
    	return true;
    }
	</script>		
		
		
	<%
		String createPermission = PortalConstants.MNG_L2_KYC_C;
		String updatePermission = PortalConstants.MNG_L2_KYC_U;
		String readUpdatePermission = PortalConstants.MNG_L2_KYC_R;
		
	%>

	</head>
	<body bgcolor="#ffffff">

		<div id="successMsg" class="infoMsg" style="display: none;"></div>
		<spring:bind path="level2KycModel.*">
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
			<html:form name="level2KycForm" commandName="level2KycModel"
				enctype="multipart/form-data" method="POST"
				onsubmit="return onFormSubmit(this)"
				action="p_l2_kyc_form.html">

				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
						value="${level2KycModel.actionId }" />
				<c:if test="${level2KycModel.actionId == 3}" >
				<input type="hidden" name="isUpdate"
						value="true" />
				</c:if>

				<html:hidden path="<%=PortalConstants.KEY_USECASE_ID %>" />
				<div class="infoMsg" id="errorMessages" style="display:none;"></div>
				<div id="table_form">
                	<div class="center">
                	<table>
                		<tr>
                			<input type="hidden" name="timeStamp" value="<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>"/>
                			<html:hidden path="kycId"/>
                 			<html:hidden path="versionNo"/>
                 			<html:hidden path="createdOn"/>
                 			<html:hidden path="createdBy"/>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Application Form Number:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<%-- <html:input id="initialAppFormNo" path="initialAppFormNo" cssClass="textBox" tabindex="1" maxlength="5" 
								onkeypress="return maskAlphaNumeric(this,event)"/> --%>
								
								<c:choose>
									<c:when test="${not empty level2KycModel.kycId}">
										<html:hidden path="initialAppFormNo"/>
										<div class="textBox" style="background: #D3D3D3; ">${level2KycModel.initialAppFormNo}</div>
									</c:when>
									<c:otherwise>
										<html:input id="initialAppFormNo" path="initialAppFormNo" cssClass="textBox" tabindex="1" maxlength="16" 
											onkeypress="return maskAlphaNumericWithSpdashforAH(this,event)"/>
									</c:otherwise>
								</c:choose>
								
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Main Source of Funds to be Credited into Account:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:select path="fundSourceId" cssClass="textBox" id="fundSourceId"
									tabindex="2">
									<html:option value="">[Select]</html:option>
										<c:if test="${fundSourceList != null}">
											<html:options items="${fundSourceList}" itemLabel="name" itemValue="fundSourceId" />
				 						</c:if>
								</html:select>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Expected Number of Transactions per Month &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Debit(#):
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="noOfTxDebit" path="noOfTxDebit" cssClass="textBox" tabindex="3" maxlength="10" 
								onkeypress="return isNumberKey(event)"/>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								 Credit(#):
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="noOfTxCredit" path="noOfTxCredit" cssClass="textBox" tabindex="4" maxlength="10"
								onkeypress="return isNumberKey(event)" />
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Expected Value of Transactions per Month &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Debit(Rs.):
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="valOfTxDebit" path="valOfTxDebit" cssClass="textBox" tabindex="5" maxlength="10" 
								onkeypress="return isNumberKey(event)"/>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								 Credit(Rs.):
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="valOfTxCredit" path="valOfTxCredit" cssClass="textBox" tabindex="6" maxlength="10" 
								onkeypress="return isNumberKey(event)"/>
							</td>
						</tr>
	 					<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Expected Mode of Transactions:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:select path="expectedModeOfTransaction" cssClass="textBox" id="modeOfTx"
									tabindex="7" multiple="multiple">
										<c:if test="${transactionModeList != null}">
											<html:options items="${transactionModeList}" itemLabel="name" itemValue="transactionModeId" />
				 						</c:if>
								</html:select>
							</td>
						</tr>

						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Account Risk Level:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:select path="riskLevel" cssClass="textBox"
									tabindex="8">
									<html:option value="">[Select]</html:option>
									<html:option value="1">Low</html:option>
									<html:option value="2">Medium</html:option>
									<html:option value="3">High</html:option>
									</html:select>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Comments by RM/AM:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" id="comments" path="comments" tabindex="9" cssClass="textBox" />
							</td>
						</tr>
					</table>
					</div>

					<table>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td width="100%" align=center bgcolor="FBFBFB">
									<c:choose>
										<c:when test="${not empty initialAppFormNo}">
											<authz:authorize ifAnyGranted="<%=updatePermission%>">
												<input type="submit" class="button" value="Update"
													tabindex="53" />&nbsp;
											</authz:authorize>
											<input type="button" class="button" value="Cancel" tabindex="54" onclick="javascript: window.location='home.html'" />
										</c:when>
										<c:otherwise>
											<authz:authorize ifAnyGranted="<%=createPermission%>">
												<input type="submit" class="button" value="Save" tabindex="53" onclick="javascript:onSave(document.forms.level2AccountForm,null);" />
												<input type="button" class="button" value="Cancel" tabindex="54" onclick="javascript: window.location='home.html'"/>
											</authz:authorize>
											<authz:authorize ifNotGranted="<%=createPermission%>">
												<input type="button" class="button"
													value="Save" tabindex="-1"
													disabled="disabled" />
												<input type="button" class="button" value="Cancel" onclick="javascript: window.location='home.html'"
													tabindex="55" />
											</authz:authorize>
										</c:otherwise>
									</c:choose>
								</td>
						</tr>
					</table>
			</html:form>
	</body>

</html>




