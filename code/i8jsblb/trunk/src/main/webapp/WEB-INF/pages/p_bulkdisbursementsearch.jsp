<%@page import="com.inov8.microbank.common.util.ProductConstantsInterface"%>
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.EncryptionUtil"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set> 
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<%@include file="/common/ajax.jsp"%>
		<meta name="title" content="Scheduled Bulk Disbursements/Payments" />
		<meta name="decorator" content="decorator">
		<script type="text/javascript" src="<c:url value='/scripts/deleterecord.js'/>"></script>
		<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		
      	<script language="javascript" type="text/javascript">
		var jq=$.noConflict();
		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		
	   	function deleteThis(request) {
			isDelOperationSuccessful(request);
		}
		
		
		function togglePaymentTypeElement(element) {
			if(element.value == '<%=ProductConstantsInterface.BULK_PAYMENT%>'){
				document.getElementById("type").disabled=false;
				document.getElementById("type").style.background="#FFFFFF";

				document.getElementById("payCashViaCnic").disabled=false;
				document.getElementById("payCashViaCnic").style.background="#FFFFFF";
			}else{
				document.getElementById("type").selectedIndex = 0;
				document.getElementById("type").disabled=true;
				document.getElementById("type").style.background="#d6d6d6";

				document.getElementById("payCashViaCnic").selectedIndex = 0;
				document.getElementById("payCashViaCnic").disabled=true;
				document.getElementById("payCashViaCnic").style.background="#d6d6d6";
			}
			
		}
				
	    function error(request) {
	      alert("An unknown error has occured. Please contact with the administrator for more details");
	    }
        </script>
	</head>
	<body bgcolor="#ffffff">
		<div id="successMsg" class="infoMsg" style="display:none;"></div>
		<spring:bind path="bulkDisbursementsViewModel.*">
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
		
			<html:form name='bulkDisbursementsForm' commandName="bulkDisbursementsViewModel" method="post" onsubmit="return onFormSubmit(this);">
				<table width="800px" border="0"><tr>

					<td align="right" class="formText">Transaction ID:</td>
					<td align="left"><html:input path="transactionCode" tabindex="3" cssClass="textBox" maxlength="13" onkeypress="return maskNumber(this,event)" /></td>
					<td class="formText" align="right">Batch Number:</td>

					<td align="left"><html:input path="batchNumber" id="batchNumber" cssClass="textBox" maxlength="50" tabindex="3" onkeypress="return maskInteger(this,event)" onblur="return maskNumber(this,event)" /></td>
					</td>

				</tr>
				
				<tr>
					<td class="formText" align="right">Service:</td>
					<td align="left" >
						<html:select id="serviceId" path="serviceId" cssClass="textBox" tabindex="1">
							<html:option value="">--- Select ---</html:option>
							<c:if test="${serviceList != null}">
								<html:options items="${serviceList}" itemValue="id" itemLabel="label"/>
							</c:if>
						</html:select>
					</td>
					<td align="right" class="formText">Product:</td>
					<td align="left"><html:select id="productId" path="productId" cssClass="textBox" tabindex="1" onchange="togglePaymentTypeElement(this);">
						<html:option value="">---Select---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>

				</tr>
				
				<tr>
					<td align="right" class="formText">A/C Creation Status:</td>
					<td align="left">
						<html:select id="accountCreationStatus" path="accountCreationStatus" cssClass="textBox" tabindex="5">
							<html:option value="">---Select---</html:option>
							<c:if test="${accountCreationStatusList != null}">
								<html:options items="${accountCreationStatusList}" itemValue="value" itemLabel="label"/>
							</c:if>
						</html:select>
					</td>
					<td align="right" class="formText" width="18%">Emp/Reg No:</td>
					<td align="left" width="32%"><html:input path="employeeNo" cssClass="textBox" tabindex="6" /></td>
				</tr>
				<tr>
					<td align="right" class="formText">Name:</td>
					<td align="left"><html:input path="name" cssClass="textBox" tabindex="7" /></td>
					<td align="right" class="formText" width="18%">Cheque No:</td>
					<td align="left" width="32%"><html:input path="chequeNo" tabindex="8" cssClass="textBox" /></td>
				</tr>
				<tr>
					<td align="right" class="formText" width="18%">CNIC:</td>
					<td align="left" width="32%"><html:input path="nic" id="nic" tabindex="9" cssClass="textBox" maxlength="13" onkeypress="return maskNumber(this,event)" /></td>
					<td align="right" class="formText" width="18%">Mobile No:</td>
					<td align="left" width="32%"><html:input path="mobileNo" tabindex="10" cssClass="textBox" maxlength="11" onkeypress="return maskNumber(this,event)" /></td>
				</tr>
				<tr>
					<td class="formText" align="right">Uploaded By:</td>
					<td align="left">
						<html:select path="createdBy" tabindex="11" cssClass="textBox">
							<html:option value="">---Select---</html:option>
								<c:if test="${bankUsersList != null}">
									<html:options items="${bankUsersList}" itemValue="value" itemLabel="label" />
	 							</c:if>
	 					</html:select>
					</td>
					<td class="formText" align="right" width="20%">Updated By:</td>
					<td align="left">
						<html:select path="updatedBy" tabindex="12" cssClass="textBox">
							<html:option value="">---Select---</html:option>
								<c:if test="${bankUsersList != null}">
									<html:options items="${bankUsersList}" itemValue="value" itemLabel="label" />
	 							</c:if>
	 					</html:select>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">Payment From Date:</td>
					<td align="left">
					     <html:input path="paymentFromDate" id="paymentFromDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="pFDate" tabindex="13" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="pFDate" tabindex="14" title="Clear Date" name="popcal" onclick="javascript:$('paymentFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td align="right" class="formText">Payment To Date:</td>
					<td align="left">
				        <html:input path="paymentToDate" id="paymentToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="pTDate" tabindex="15" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="pTDate" tabindex="16" title="Clear Date" name="popcal" onclick="javascript:$('paymentToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
 				<tr>
					<td align="right" class="formText">File Upload From Date:</td>
					<td align="left">
					     <html:input path="uploadFromDate" id="uploadFromDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="uFDate" tabindex="17" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="uFDate" tabindex="18" title="Clear Date" name="popcal" onclick="javascript:$('uploadFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td align="right" class="formText">File Upload To Date:</td>
					<td align="left">
				        <html:input path="uploadToDate" id="uploadToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="uTDate" tabindex="19" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="uTDate" tabindex="20" title="Clear Date" name="popcal" onclick="javascript:$('uploadToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr> 
				<tr>
					<td align="right" class="formText">Posted On From Date:</td>
					<td align="left">
					     <html:input path="postedOnFromDate" id="postedOnFromDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="poFDate" tabindex="21" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="poFDate" tabindex="22" title="Clear Date" name="popcal" onclick="javascript:$('postedOnFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td align="right" class="formText">Posted On To Date:</td>
					<td align="left">
				        <html:input path="postedOnToDate" id="postedOnToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="poTDate" tabindex="23" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="poTDate" tabindex="24" title="Clear Date" name="popcal" onclick="javascript:$('postedOnToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr> 
				<tr>
					<td align="right" class="formText">Settled On From Date:</td>
					<td align="left">
					     <html:input path="settledOnFromDate" id="settledOnFromDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="soFDate" tabindex="25" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="soFDate" tabindex="26" title="Clear Date" name="popcal" onclick="javascript:$('settledOnFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td align="right" class="formText">Settled On To Date:</td>
					<td align="left">
				        <html:input path="settledOnToDate" id="settledOnToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="soTDate" tabindex="27" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="soTDate" tabindex="28" title="Clear Date" name="popcal" onclick="javascript:$('settledOnToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr> 
				<tr>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="29" />
						<input name="reset" type="reset" onclick="javascript: window.location='p_bulkdisbursementsearch.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="27" />
					</td>
					<td colspan="2">
						&nbsp;
					</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</table>
				<ajax:select source="serviceId" target="productId"
							 baseUrl="${contextPath}/p_productRefData.html" errorFunction="error"
							 parameters="serviceId={serviceId}"/>

			</html:form>
		

		<ec:table filterable="false" items="bulkDisbursementsViewModelList"
			var="bulkDisbursementsModel" retrieveRowsCallback="limit"
			filterRowsCallback="limit" sortRowsCallback="limit"
			action="${contextPath}/p_bulkdisbursementsearch.html?actionId=${retriveAction}"
			title="">
			<c:set var="encryptedId"><security:encrypt strToEncrypt="${bulkDisbursementsModel.bulkDisbursementsId}"/></c:set>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Bulk Disbursements.xls" tooltip="Export Excel" />
			</authz:authorize>
 			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Bulk Disbursements.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Bulk Disbursements" fileName="Bulk Disbursements.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Bulk Disbursements.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>			
				<ec:column  property="employeeNo" title="Emp/Reg No"  />
				<ec:column  property="name" title="Name"  />
				<ec:column  property="customerId" title="Customer Id"  />
				<ec:column  property="nic" title="CNIC" escapeAutoFormat="true"/>
				<ec:column  property="mobileNo" title="Mobile No" escapeAutoFormat="true"/>
				<ec:column  property="productName" title="Product"/>
<%--				<ec:column  property="paymentType" title="Type"/>--%>
				<ec:column  property="accountCreationStatus" title="A/C Creation Status"/>
				<ec:column  property="chequeNo" title="Cheque No." style="text-align: right"/>
				<ec:column  property="amount" title="Amount" style="text-align: right"/>
				<ec:column  property="paymentDate" title="Payment Date" cell="date" format="dd/MM/yyyy"/>
				<ec:column  property="batchNumber" title="Batch No." style="text-align: right" escapeAutoFormat="true"/>
				<ec:column  property="transactionCode" title="Transaction ID" style="text-align: right" escapeAutoFormat="true"/>
				<ec:column  property="createdOn" title="Uploaded On" cell="date" format="dd/MM/yyyy hh:mm a"/>
				<ec:column  property="createdByName" title="Uploaded By"/>
				<ec:column  property="updatedOn" title="Updated On" cell="date" format="dd/MM/yyyy hh:mm a"/>
				<ec:column  property="updatedByName" title="Updated By"/>
				<ec:column  property="posted" title="Posted" />
				<ec:column  property="postedOn" title="Posted On" cell="date" format="dd/MM/yyyy hh:mm a"/>
				<ec:column  property="settled" title="Settled" />
				<ec:column  property="settledOn" title="Settled On" cell="date" format="dd/MM/yyyy hh:mm a"/>
				<ec:column  property="description" title="Description"/>
				<%--<authz:authorize ifAnyGranted="<%=PortalConstants.UPDATE_BULK_DISBURSEMENTS%>">
				<ec:column alias="" viewsAllowed="html" filterable="false" sortable="false" width="50px" style="text-align: center">
					<c:if test="${bulkDisbursementsModel.productId==2510816}">
						<c:choose>
	  						<c:when test="${bulkDisbursementsModel.deleted==true or (bulkDisbursementsModel.posted=='Yes' and bulkDisbursementsModel.settled=='Yes')}">
	  							<input type="button" class="button" disabled="disabled" value="Edit"/>
	  						</c:when>
	  						<c:otherwise>
	  							<input type="button" class="button" value="Edit" id="edit_href${encryptedId}" onclick="javascript:window.location.href='${contextPath}/p_bulkdisbursementform.html?bulkDisbursementsId=${encryptedId}';"/>
	  						</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${bulkDisbursementsModel.productId==2510733}">
						<c:choose>
	  						<c:when test="${bulkDisbursementsModel.deleted==true or (bulkDisbursementsModel.posted=='Yes' or bulkDisbursementsModel.settled=='Yes')}">
	  							<input type="button" class="button" disabled="disabled" value="Edit"/>
	  						</c:when>
	  						<c:otherwise>
	  							<input type="button" class="button" value="Edit" id="edit_href${encryptedId}" onclick="javascript:window.location.href='${contextPath}/p_bulkdisbursementform.html?bulkDisbursementsId=${encryptedId}';"/>
	  						</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${bulkDisbursementsModel.productId!=2510816 && bulkDisbursementsModel.productId!=2510733 }  ">

					</c:if>
				</ec:column>
				</authz:authorize>--%>
				<authz:authorize ifAnyGranted="<%=PortalConstants.DELETE_BULK_DISBURSEMENTS%>">
				<ec:column sortable="false" property="" alias="" viewsAllowed="html">
					<c:choose>
  						<c:when test="${bulkDisbursementsModel.deleted==true or bulkDisbursementsModel.posted=='Yes' or bulkDisbursementsModel.settled=='Yes'}">
							<input type="button" class="button" disabled="disabled" value="Delete"/>
						</c:when>
  						<c:otherwise>
    						<tags:delete
								id="${bulkDisbursementsModel.bulkDisbursementsId}" 
								model="com.inov8.microbank.common.model.BulkDisbursementsModel" 
								property="deleted"
								propertyValue="${bulkDisbursementsModel.deleted}"
								callback="deleteThis"
								error="defaultError"
								useCaseId="<%=PortalConstants.DELETE_BULK_DISBURSEMENT_USECASE_ID %>"
								isButton="true"
							/>
  						</c:otherwise>
					</c:choose>
				</ec:column>
				</authz:authorize>
			</ec:row>
		</ec:table>
<%--		<input type="button" class="button" value="Edit" id="edit_href${encryptedId}" onclick="javascript:window.location.href='${contextPath}/p_bulkdisbursementform.html?bulkDisbursementsId=${encryptedId}';"/>--%>
		<script language="javascript" type="text/javascript">
			/*jq(document).ready(function(){
				jq(document).keydown(function(event) {
					if (event.ctrlKey==true && (event.which == '118' || event.which == '86')) {
						//alert('thou. shalt. not. PASTE!');
						event.preventDefault();
					}
				});
			});
*/



  		Calendar.setup( {inputField  : "paymentFromDate", ifFormat : "%d/%m/%Y", button : "pFDate", showsTime : false} );
	  	Calendar.setup( {inputField  : "paymentToDate", ifFormat : "%d/%m/%Y", button : "pTDate", isEndDate: true, showsTime : false } );
	  	
	  	Calendar.setup( {inputField  : "uploadFromDate",button : "uFDate"} );
	  	Calendar.setup( {inputField  : "uploadToDate",button : "uTDate", isEndDate: true } );
	  		  	
	  	Calendar.setup( {inputField  : "postedOnFromDate",button : "poFDate"} );
	  	Calendar.setup( {inputField  : "postedOnToDate",button : "poTDate", isEndDate: true } );
	  		  	
	  	Calendar.setup( {inputField  : "settledOnFromDate",button : "soFDate"} );
	  	Calendar.setup( {inputField  : "settledOnToDate",button : "soTDate", isEndDate: true } );
	  	
	  	
	  	function onFormSubmit(form){


			if(!IsNumericData(jq('#batchNumber').val(),'Batch#')){
				return false;
			}
			if(!IsNumericData(jq('#nic').val(),'CNIC#')){
				return false;
			}





			var currentDate = "<%=PortalDateUtils.getServerDate()%>";
		    
		    var isValid	=	validateForm(form);
		    
		    if(!isValid)
		    return isValid;
		    
		    var _payFDate = form.paymentFromDate.value;
			var _payTDate = form.paymentToDate.value;
			startlbl = "Payment From Date";
			endlbl   = "Payment To Date";
		    var isValidPaymentDate = isValidDateRange(_payFDate,_payTDate,startlbl,endlbl,currentDate);
		    
		    var _uFDate = form.uploadFromDate.value;
			var _uTDate = form.uploadToDate.value;
			startlbl = "Upload From Date";
			endlbl   = "Upload To Date";
		    var isValidUploadDate = isValidDateRange(_uFDate,_uTDate,startlbl,endlbl,currentDate);
		    
		    var _poFDate = form.postedOnFromDate.value;
			var _poTDate = form.postedOnToDate.value;
			startlbl = "Posted On From Date";
			endlbl   = "Posted On To Date";
		    var isValidUPostedOnDate = isValidDateRange(_poFDate,_poTDate,startlbl,endlbl,currentDate);
		    
		    var _soFDate = form.settledOnFromDate.value;
			var _soTDate = form.settledOnToDate.value;
			startlbl = "Settled On From Date";
			endlbl   = "Settled On To Date";
		    var isValidUSettledOnDate = isValidDateRange(_soFDate,_soTDate,startlbl,endlbl,currentDate);
		    
		    isValid = isValidPaymentDate && isValidUploadDate && isValidUPostedOnDate && isValidUSettledOnDate;
		    
		    return isValid;
  		}
	  	



		if(document.getElementById("productId").value == '<%=ProductConstantsInterface.BULK_PAYMENT%>'){
				document.getElementById("type").disabled=false;
				document.getElementById("type").style.background="#FFFFFF";

				document.getElementById("payCashViaCnic").disabled=false;
				document.getElementById("payCashViaCnic").style.background="#FFFFFF";
		}else{
				document.getElementById("type").selectedIndex = 0;
				document.getElementById("type").disabled=true;
				document.getElementById("type").style.background="#d6d6d6";

				document.getElementById("payCashViaCnic").selectedIndex = 0;
				document.getElementById("payCashViaCnic").disabled=true;
				document.getElementById("payCashViaCnic").style.background="#d6d6d6";
			}

		
      	</script>
      	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>
