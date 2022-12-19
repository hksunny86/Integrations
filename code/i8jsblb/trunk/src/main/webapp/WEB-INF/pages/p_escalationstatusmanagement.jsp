<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>

<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="defaultAction"><%=PortalConstants.ACTION_DEFAULT %></c:set>

<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
		<%@include file="/common/ajax.jsp"%>

		<meta name="title" content="Escalation Status" />
		<script language="javascript" type="text/javascript">
	    	function submitIssue(selectCtrl, issueId, tId)
	      	{
	      		$('ctrl').value = selectCtrl.id;
	      	
			   	if (confirm('Are you sure you want to proceed?')==true)
	        	{
	        		selectCtrl.disabled='disabled';
	        		$('btn_'+selectCtrl.id).disabled='disabled';
	        		$('ctrlBtn').value = 'btn_'+selectCtrl.id;
	        		
					var url = '${contextPath}/p_i8IssueController.html';
					var pars = 'actionId=${defaultAction}&issueId='+issueId+'&status='+$F(selectCtrl)+'&tId='+tId;

					var myAjax = new Ajax.Updater(
						{success: 'successMsg'}, 
						url, 
						{
							method: 'post', 
							parameters: pars,
							onFailure: reportError,
							onSuccess: enableState
						});
				}
				else
				{
					resetIssueStatusCombo();
				}
	      	}
	      	
	      	function reportError(request)
	      	{
			  $('successMsg').innerHTML = "";
			  Element.hide('successMsg'); 
			  $('errorMsg').innerHTML = "Error occured while changing the status. Please contact with administrator for more details";
			  Element.show('errorMsg');
			  resetIssueStatusCombo();
      		  $($F('ctrl')).disabled='';
      		  $($F('ctrlBtn')).disabled='';
			  
	      	}
			
			function resetIssueStatusCombo()
			{
				var opt = $($F('ctrl')).options;
				for (i=0; i<opt.length;i++)
				{
					if(opt[i].value==6 || opt[i].value==8)
						$($F('ctrl')).selectedIndex=opt[i].index;
				}
			}

	      	function enableState(request)
	      	{
			    $('errorMsg').innerHTML = "";
			  	Element.hide('errorMsg'); 
			    Element.show('successMsg');
	      	}

	</script>

	</head>
	<body bgcolor="#ffffff">	
	
	<c:set var="BILL_SALE_TX" scope="page">
		<security:encrypt strToEncrypt="<%=TransactionTypeConstantsInterface.BILL_SALE_TX.toString()%>"/>
	</c:set>
	<c:set var="CUST_DISC_PRODUCT_SALE_TX" scope="page">
		<security:encrypt strToEncrypt="<%=TransactionTypeConstantsInterface.CUST_DISC_PRODUCT_SALE_TX.toString()%>"/>
	</c:set>
	<c:set var="CUST_VAR_PRODUCT_SALE_TX" scope="page">
		<security:encrypt strToEncrypt="<%=TransactionTypeConstantsInterface.CUST_VAR_PRODUCT_SALE_TX.toString()%>"/>
	</c:set>
	
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
	<div id="errorMsg" class ="errorMsg" style="display:none;"></div>
	
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
			<html:form name='escalationStatusForm'
				commandName="extendedEscalateInov8ListViewModel" method="post"
				action="p_escalationstatusmanagement.html" onsubmit="return validateForm()" >
				<tr>
					<td width="33%" align="right" class="formText">
						Customer ID:<input type="hidden" id="ctrl"/><input type="hidden" id="ctrlBtn"/>
						
					</td>
					<td width="17%" align="left">
						<html:input path="mfsId" cssClass="textBox" maxlength="8"
							tabindex="1" />
					</td>
					<td width="16%" align="right">
						<span class="formText">Start Date: </span>
					</td>
					<td width="34%" align="left">
						<html:input path="startDate" tabindex="-1" id="startDate"
							readonly="true" cssClass="textBox" maxlength="10" />
						<img id="sDate" tabindex="2" name="popcal" align="top"
							style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="3" title="Clear Date" name="popcal"
							onclick="javascript:$('startDate').value=''" align="middle"
							style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>


				<tr>
					<td class="formText" align="right">
						Issue Code:
					</td>
					<td align="left">
						<html:input path="issueCode" id="issueCode" cssClass="textBox"
							maxlength="50" tabindex="4" />
						${status.errorMessage}
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
						<html:input path="endDate" tabindex="-1" id="endDate"
							readonly="true" cssClass="textBox" maxlength="10" />
						<img id="eDate" tabindex="5" name="popcal" align="top"
							style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="eDate" tabindex="6" title="Clear Date" name="popcal"
							onclick="javascript:$('endDate').value=''" align="middle"
							style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Transaction ID:
					</td>
					<td align="left">
						<html:input path="transactionCode" cssClass="textBox"
							maxlength="50" tabindex="7" />
						${status.errorMessage}
					</td>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left"></td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Service:
					</td>
					<td align="left">
						<html:select path="serviceId" onchange="changeToAll(1)"
							cssClass="textBox" tabindex="8">
							<html:option value="">---All---</html:option>
							<c:if test="${serviceModelList != null}">
								<html:options items="${serviceModelList}" itemValue="productId"
									itemLabel="productName" />
							</c:if>
						</html:select>
					</td>

					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
				</tr>


				<tr>
					<td class="formText" align="right">
						Product:
					</td>
					<td align="left">
						<html:select path="productId" onchange="changeToAll(2)"
							cssClass="textBox" tabindex="9">
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId"
									itemLabel="productName" />
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
				</tr>
				<tr>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
						<span class="formText">
						 <input type ="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>" />
				  <security:isauthorized action="<%=PortalConstants.ACTION_RETRIEVE%>">
						<jsp:attribute name="ifAccessAllowed">
							 <input name="_search" type="submit"	class="button" value="Search" tabindex="10" /> 
		          		</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed">  					          
        					  <input name="_search" type="submit"	class="button" value="Search" tabindex="-1" disabled="disabled" /> 
						</jsp:attribute>
		         </security:isauthorized> 
								
				<input
								name="reset" type="reset"
								onclick="javascript: window.location='p_escalationstatusmanagement.html?actionId=${retriveAction}'"
								class="button" value="Cancel" tabindex="11" /> </span>
					</td>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
					</td>
				</tr>
			</html:form>
		</table>

		<ec:table items="escalationStatusModelList"
			var="escalationStatusModel" filterable="false"
			action="${pageContext.request.contextPath}/p_escalationstatusmanagement.html?actionId=${retriveAction}"
			title="" retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit">
				
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Escalation Status.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Escalation Status.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Escalation Status" fileName="Escalation Status.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Escalation Status.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>	
				
				
			<ec:row>
				<c:set var="escalationStatusTransactionId">
					<security:encrypt strToEncrypt="${escalationStatusModel.transactionId}"/>
				</c:set>
				<c:set var="escalationStatusIssueId">
					<security:encrypt strToEncrypt="${escalationStatusModel.issueId}"/>
				</c:set>
				<c:set var="escalationStatusTransactionTypeId">
					<security:encrypt strToEncrypt="${escalationStatusModel.transactionTypeId}"/>
				</c:set>
				
				
				<ec:column property="issueCode" filterable="false" title="Issue Code">
				  <security:isauthorized action="<%=PortalConstants.ACTION_RETRIEVE%>">
						<jsp:attribute name="ifAccessAllowed">
							<a href="p_issuehistorymanagement.html"
								onclick="return openIssueWindow('${escalationStatusModel.issueCode}')">
								<c:out value="${escalationStatusModel.issueCode}"></c:out> 
							</a>
		          		</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed">  					          
								<c:out value="${escalationStatusModel.issueCode}"></c:out> 
						</jsp:attribute>
		         </security:isauthorized> 
				</ec:column>
				<ec:column property="transactionCode" filterable="false" title="Transaction ID" escapeAutoFormat="true">

					<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html"
							onClick="return openTransactionWindow('${escalationStatusModel.transactionCode}')">
							${escalationStatusModel.transactionCode} 
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${escalationStatusModel.transactionCode} 
					</authz:authorize>
				</ec:column>
				<ec:column property="mfsId" filterable="false" title="Customer ID" escapeAutoFormat="true"/>
				<ec:column property="saleMobileNo" filterable="false" title="Customer Reference" escapeAutoFormat="true" />
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Date" />
				<ec:column property="totalAmount" filterable="false" title="Amount" calc="total" calcTitle="Total:"  cell="currency"  format="0.00"/>
				<ec:column property="processingStatusName" filterable="false"  title="Status" />
				<ec:column property="issueStatusName" filterable="false" style="vertical-align: middle"
					title="Bank Status">
					
				  <security:isauthorized action="<%=PortalConstants.ACTION_UPDATE%>">
						<jsp:attribute name="ifAccessAllowed">
						<c:if test="${escalationStatusModel.issueTypeId eq 1}">
							<c:choose>
								<c:when test="${escalationStatusTransactionId eq null || escalationStatusTransactionId eq ''}">
									<select id="issueStatus_${escalationStatusIssueId}" onchange="javascript:submitIssue(this, ${escalationStatusIssueId}, -999)">
								</c:when>
								<c:otherwise>
									<select id="issueStatus_${escalationStatusIssueId}" onchange="javascript:submitIssue(this, ${escalationStatusIssueId}, ${escalationStatusTransactionId})">
								</c:otherwise>
							</c:choose>							
								<c:forEach var="issueTypeStatusModel" varStatus="index"
									items="${issueTypeStatusModelList}">
									<option
										value="<c:out value='${issueTypeStatusModel.issueTypeStatusId}'/>"
										<c:if test="${escalationStatusModel.issueTypeStatusId eq issueTypeStatusModel.issueTypeStatusId}">selected="selected"</c:if>>
										<c:out value="${issueTypeStatusModel.issueStatusName}" />
									</option>
								</c:forEach>
							</select>
						</c:if>
						<c:if test="${escalationStatusModel.issueTypeId eq 2}">
							<c:choose>
								<c:when test="${escalationStatusTransactionId eq null || escalationStatusTransactionId eq ''}">
									<select id="issueStatus_${escalationStatusIssueId}" onchange="javascript:submitIssue(this, ${escalationStatusIssueId}, -999)">
								</c:when>
								<c:otherwise>
									<select id="issueStatus_${escalationStatusIssueId}" onchange="javascript:submitIssue(this, ${escalationStatusIssueId}, ${escalationStatusTransactionId})">
								</c:otherwise>
							</c:choose>							
							
								<c:forEach var="disputeIssueTypeStatusModel"  varStatus="index"
									items="${disputeIssueTypeStatusModelList}">
									<option
										value="<c:out value='${disputeIssueTypeStatusModel.issueTypeStatusId}'/>"
										<c:if test="${escalationStatusModel.issueTypeStatusId eq disputeIssueTypeStatusModel.issueTypeStatusId}">selected="selected"</c:if>>
										<c:out value="${disputeIssueTypeStatusModel.issueStatusName}" />
									</option>
								</c:forEach>
							</select>
						</c:if>
			       		</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed">  					          
							<select id="issueStatus_${escalationStatusIssueId}" onchange="javascript:submitIssue(this, ${escalationStatusIssueId}, ${escalationStatusTransactionId})" disabled="disabled" >				
								<c:forEach var="issueTypeStatusModel" varStatus="index"
									items="${issueTypeStatusModelList}">
									<option
										value="<c:out value='${issueTypeStatusModel.issueTypeStatusId}'/>"
										<c:if test="${escalationStatusModel.issueTypeStatusId eq issueTypeStatusModel.issueTypeStatusId}">selected="selected"</c:if>>
										<c:out value="${issueTypeStatusModel.issueStatusName}" />
									</option>
								</c:forEach>
							</select>
						</jsp:attribute>
			      </security:isauthorized> 
				</ec:column>				
				
				<ec:column alias=" " title=" " filterable="false" sortable="false"
					viewsAllowed="html">
					<c:choose>
						<c:when test="${escalationStatusModel.mfsId == null || escalationStatusModel.mfsId eq '' || !(  
						escalationStatusTransactionTypeId eq BILL_SALE_TX ||
						escalationStatusTransactionTypeId eq CUST_DISC_PRODUCT_SALE_TX ||  
						escalationStatusTransactionTypeId eq CUST_VAR_PRODUCT_SALE_TX )
						 }">
							<input id="btn_issueStatus_${escalationStatusIssueId}" type="button" class="button"  value="Escalate" disabled="disabled"/>
						 </c:when>
						 <c:otherwise>
							  <security:isauthorized action="<%=PortalConstants.ACTION_DEFAULT%>">
									<jsp:attribute name="ifAccessAllowed">
										<input id="btn_issueStatus_${escalationStatusIssueId}" type="button" class="button"  value="Escalate"
											onclick="javascript:escalate('${pageContext.request.contextPath}/p_i8escalateform.html?actionId=${defaultAction}&issueId=${escalationStatusIssueId}&transactionCode=${escalationStatusModel.transactionCode}');"
										 />
						       		</jsp:attribute>
									<jsp:attribute name="ifAccessNotAllowed">  					          
									 	<input id="btn_issueStatus_${escalationStatusIssueId}" type="button" class="button"  value="Escalate" disabled="disabled"/>
									</jsp:attribute>
						      </security:isauthorized> 
						 </c:otherwise>
					 </c:choose>
				</ec:column>
			</ec:row>
		</ec:table>

		<script type="text/javascript">


		function escalate(link)
		{
		      window.location.href=link;
		}

		document.forms[0].mfsId.focus();

      function openTransactionWindow(transactionCode){
		newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary', 'width=400,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		if(window.focus) newWindow.focus();
		return false;
      }
      
  	  function openIssueWindow(issueCode)
	  {
          newWindow=window.open('p_issuehistorymanagement.html?actionId=${retriveAction}&issueCode='+issueCode,'IssueHistory', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
          if(window.focus) newWindow.focus();
              return false;
  	  }

	        function validateForm(){
	        	return validateFormChar(document.forms[0]);
	        }

      Calendar.setup(
      {
        inputField  : "startDate", // id of the input field
        button      : "sDate"    // id of the button
      }
      );

      Calendar.setup(
      {
        inputField  : "endDate", // id of the input field
        button      : "eDate",    // id of the button
         isEndDate: true
      }
      );
      

	function changeToAll(option){
		_form = document.forms[0];
		if(option == 1){
			_form.productId[0].selected = true;
		}else{
		_form.serviceId[0].selected = true;
		}
	}      
      
</script>

	</body>
</html>

