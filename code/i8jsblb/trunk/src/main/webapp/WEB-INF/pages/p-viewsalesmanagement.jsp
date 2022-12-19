
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="defaultAction"><%=PortalConstants.ACTION_DEFAULT %></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" 
			src="${contextPath}/scripts/date-validation.js"></script>	
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<%@include file="/common/ajax.jsp"%>
		<meta name="title" content="View Sales" />

		<script type="text/javascript">

	function submitStatus(selectCtrl , transactionId)
	{
	
				$('ctrl').value = selectCtrl.id;				      	
			   	if (confirm('Are you sure you want to proceed?')==true)
	        	{
	        		selectCtrl.disabled='disabled';
	        		
					var url = '${contextPath}/p-supplierProcessing.html';
					var pars = 'processingStatus='+$F(selectCtrl)+'&transactionId='+transactionId+'&actionId=3'

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
					resetProcessStatusCombo();
				}
	}



	      	function reportError(request)
	      	{
			  $('successMsg').innerHTML = "";
			  Element.hide('successMsg'); 
			  $('errorMsg').innerHTML = "Error occured while changing the status. Please contact with administrator for more details";
			  Element.show('errorMsg');
			  resetProcessStatusCombo();
      		  $($F('ctrl')).disabled='';
	      	}
			
			function resetProcessStatusCombo()
			{
				var elems = document.getElementsByName($F('ctrl'));
				var sel;

				sel = elems[elems.length-1];

				var opt = sel.options;
				if(opt == null || opt.length == null ){
					amIndex = <%=SupplierProcessingStatusConstants.AMBIGUOUS%>;
					sel.selectedIndex = amIndex-1;
				}else{
					for (i=0; i<opt.length;i++)
					{
						if(opt[i].value==3)
							sel.selectedIndex=opt[i].index;
					}
				}
			}

	      	function enableState(request)
	      	{
			  	Element.hide('errorMsg'); 
			    Element.show('successMsg');
			    $('errorMsg').innerHTML = "";
	      	}
</script>
	</head>
	<body bgcolor="#ffffff" onunload="javascript:closeChild();">
		<div id="successMsg" class="infoMsg" style="display:none;"></div>
		<div id="errorMsg" class="errorMsg" style="display:none;"></div>

		<div id="smsContent" class="ajaxMsg"></div>
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		<table width="750px">

			<c:set var="AMBIGUOUS"
				value="<%=SupplierProcessingStatusConstants.AMBIGUOUS_NAME%>"
				scope="page" />

			<html:form name='salesForm' commandName="extendedSalesListViewModel"
				onsubmit="return validateForm()" method="post"
				action="p-viewsalesmanagement.html">


				<tr>
					<td width="33%" align="right" class="formText">
						Customer ID:
					</td>
					<td width="17%" align="left">
						<input type="hidden" id="ctrl" />
						<html:input onkeypress="return maskCommon(this,event)"
							path="mfsId" id="mfsId" cssClass="textBox" maxlength="11"
							tabindex="1" />
						${status.errorMessage}
					</td>
					<td width="16%" align="right" class="formText">
						Mobile #:
					</td>
					<td width="34%" align="left">
						<html:input onkeypress="return maskCommon(this,event)"
							path="currentMobileNo" cssClass="textBox" maxlength="11"
							tabindex="2" />
					</td>


				</tr>



				<tr>

					<td class="formText" align="right">
						Transaction ID:
					</td>
					<td align="left">

						<html:input onkeypress="return maskCommon(this,event)"
							path="transactionCode" id="transactionCode" cssClass="textBox"
							tabindex="3" maxlength="50" />

						${status.errorMessage}
					</td>

					<td class="formText" align="right">
						Start Date:
					</td>
					<td align="left">
						<html:input path="startDate" id="startDate" readonly="true"
							tabindex="-1" cssClass="textBox" maxlength="10" />
						<img id="sDate" tabindex="4" name="popcal" align="top"
							style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="5" title="Clear Date" name="popcal"
							onclick="javascript:$('startDate').value=''" align="middle"
							style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>


				</tr>
				<tr>

					<td align="right" class="formText">
						Product:
					</td>
					<td align="left">

						<html:select path="productId" cssClass="textBox" tabindex="6">
							<html:option value="">---All---</html:option>
							<c:if test="${custDisputeProductListViewModelList!=null}">
								<html:options items="${custDisputeProductListViewModelList}"
									itemLabel="productName" itemValue="productId" />
							</c:if>
						</html:select>

					</td>

					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
						<html:input path="endDate" id="endDate" readonly="true"
							tabindex="-1" cssClass="textBox" maxlength="10" />
						<img id="eDate" tabindex="7" name="popcal" align="top"
							style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="eDate" tabindex="8" title="Clear Date" name="popcal"
							onclick="javascript:$('endDate').value=''" align="middle"
							style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>

				</tr>
				
				<!-- 
				<tr>

					<td align="right" class="formText">
						Bank:
					</td>
					<td align="left">

						<html:select path="bankId" cssClass="textBox" tabindex="6">
							<html:option value="">---All---</html:option>
							<c:if test="${bankModelList!=null}">
								<html:options items="${bankModelList}"
									itemLabel="name" itemValue="bankId" />
							</c:if>
						</html:select>

					</td>

					<td class="formText" align="right">
					</td>
					<td align="left">  </td>

				</tr>				
				
				 -->
				<tr>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
						<span class="formText"> <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>" />
							<input name="_search" type="submit" class="button" value="Search" tabindex="9" />
							<input name="reset" type="reset" class="button" value="Cancel" tabindex="10"
								onclick="javascript: window.location='p-viewsalesmanagement.html?actionId=${retriveAction}'" />
						</span>
					</td>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
						&nbsp;
					</td>
				</tr>
			</html:form>
		</table>
		<ec:table items="salesModelList" var="transactionDetailModel"
			filterable="false"
			action="${pageContext.request.contextPath}/p-viewsalesmanagement.html?actionId=${retriveAction}"
			retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit" title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="View Sales.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="View Sales.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="View Sales" fileName="View Sales.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="View Sales.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<c:set var="transactionDetailTransactionId">
					<security:encrypt strToEncrypt="${transactionDetailModel.transactionId}"/>
				</c:set>
				<c:set var="transactionDetailTransactionCodeId">
					<security:encrypt strToEncrypt="${transactionDetailModel.transactionCodeId}"/>
				</c:set>				
			

				<ec:column property="transactionCode" filterable="false"
					title="Transaction ID" escapeAutoFormat="true">

					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
						value="<%=PortalConstants.ACTION_RETRIEVE%>" />
					<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
							<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${transactionDetailModel.transactionCode}" onclick="return openTransactionWindow('${transactionDetailModel.transactionCode}')">
								${transactionDetailModel.transactionCode}
							</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
							${transactionDetailModel.transactionCode}
	     			</authz:authorize>
				</ec:column>
				<ec:column property="mfsId" filterable="false" title="Customer ID" />
				<ec:column property="saleMobileNo" filterable="false"
					title="Subscriber Number" escapeAutoFormat="true"/>
				<ec:column property="notificationMobileNo" filterable="false"
					title="Sale Number" escapeAutoFormat="true" />
				<ec:column property="createdOn" cell="date"
					format="dd/MM/yyyy hh:mm a" filterable="false" title="Date" />
				<ec:column property="productName" filterable="false" title="Product" />

				
                <ec:column property="bankAccountNoLastFive" filterable="false" title="Account No." />
                <ec:column property="supplierCommission" filterable="false" title="Commission"  calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>                
				<ec:column property="amount" calc="total" calcTitle="Total:"
					cell="currency" format="0.00" filterable="false" title="Amount" style="text-align: right"/>


				<ec:column property="processingStatusName" title="Status"
					filterable="false" style="vertical-align: middle" sortable="false">
					<div
						id="div_processingStatus_${transactionDetailModel.transactionCode}">
						<c:choose>
							<c:when
								test="${transactionDetailModel.processingStatusName eq AMBIGUOUS}">
								<select style="width: 80px"
									id="processingStatus_${transactionDetailModel.transactionCode}"
									name="processingStatus_${transactionDetailModel.transactionCode}"
									onchange="javascript:submitStatus(this, '${transactionDetailTransactionId}' )">
									<c:forEach var="supplierProcessingStatusModel"
										varStatus="index" items="${supplierProcessingStatusModelList}">
										<option
											value="<c:out value='${supplierProcessingStatusModel.supProcessingStatusId}'/>"
											<c:if test="${supplierProcessingStatusModel.name eq transactionDetailModel.processingStatusName}">selected="selected"</c:if>>
											<c:out value="${supplierProcessingStatusModel.name}" />
										</option>
									</c:forEach>
								</select>
							</c:when>
							<c:otherwise>
								<c:out value="${transactionDetailModel.processingStatusName}" />
							</c:otherwise>
						</c:choose>
					</div>



				</ec:column>


				<%--
				<ec:column alias=" " title="Dispute" filterable="false"
					sortable="false" viewsAllowed="html">

					<c:set var="issueTypeId">
						<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.DISPUTE_NEW.toString()%>"/>
					</c:set>
					
					<c:choose>
						<c:when test="${!transactionDetailModel.issue}">
							<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
								value="<%=PortalConstants.ACTION_DEFAULT%>" />
							<security:isauthorized action="<%=PortalConstants.ACTION_DEFAULT%>">
								<jsp:attribute name="ifAccessAllowed">
									<input type="button" class="button" value="Disputed"
										name="Dispute${transactionDetailTransactionId}"
										onclick="openDisputeWindow('Dispute${transactionDetailTransactionId}', '${transactionDetailTransactionId}', '${transactionDetailTransactionCodeId}', '${transactionDetailModel.transactionCode}', '${issueTypeId}', 'pg_dispute')">
								</jsp:attribute>
								<jsp:attribute name="ifAccessNotAllowed">
									<input type="button" class="button" value="Disputed"
										name="Dispute${transactionDetailTransactionId}"
										disabled="disabled">
								</jsp:attribute>
							</security:isauthorized>
						</c:when>
						<c:otherwise>
							<input type="button" class="button" value="Disputed"
								name="DisputeDisabled${transactionDetailTransactionId}"
								disabled="disabled" />
						</c:otherwise>
					</c:choose>
				</ec:column>
				--%>
			</ec:row>
		</ec:table>


		<script type="text/javascript">

	document.forms[0].mfsId.focus();
		
     var childWindow;     
     function closeChild()
       {
          try
              {
              if(childWindow != undefined)
               {
                   childWindow.close();
                   childWindow=undefined;
               }
		      }catch(e){}
      }		

		function openDisputeWindow(btnName, transactionId, transactionCodeId, transactionCode, issueStId, winId)
		{
			childWindow = window.open('p-issueupdateform.html?actionId=${defaultAction}&<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>='+btnName+'&transactionId='+transactionId+'&transactionCodeId='+transactionCodeId+'&transactionCode='+transactionCode+'&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>='+issueStId ,winId,'width=400,height=250,menubar=no,toolbar=no,left=150,top=150,directories=no,scrollbars=no,resizable=no,status=no');		
		}

      function openTransactionWindow(transactionCode)
      {
		newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
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
</script>

	</body>
</html>
