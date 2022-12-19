 
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="defaultAction"><%=PortalConstants.ACTION_DEFAULT %></c:set>

<html>
	<head>
<meta name="decorator" content="decorator">
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
		
		
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<meta name="title" content="Customer Dispute" />
		<script language="javascript" type="text/javascript">


	    	function submitIssue()
	      	{
					var cTransCode = $F('custTransCode');
					var _cTransCode = $('custTransCode');
					var issueMfsId     = $F('issueMfsId');
					var _issueMfsId    = $('issueMfsId');
					
					
					var cmts = $F('comments');
					
				if(issueMfsId.length==0)
					{
						alert("Inov8 MWallet ID is a required field");
						$('issueMfsId').focus();
						return false;
					}
					else 
						if(!isNumberWithZeroes(_issueMfsId)){
							alert("Please enter valid Inov8 MWallet ID");
							$('issueMfsId').focus();
							return false;
								
						}
					else					
					if(cTransCode.length==0)
					{
						alert("Transaction Code is a required field");
						$('custTransCode').focus();
						return false;
 					} 
 					else
					if(cmts.length==0)
					{
						alert("Customer Claim is a required field");
						$('comments').focus();
						return false;
					}
					else
					{
						
				      	if(!validateFormChar(document.issueForm)){
				      		return false;
				      	}
																		
						if(cmts.length>250)
						{
							alert('Maximum characters should not be more than 250');
							$('comments').focus();
						}
						else if (confirm('Are you sure you want to proceed?')==true)
			        	{
						
			        		$("btnSubmit").disabled='disabled';
							var url = '${contextPath}/p_createIssueController.html';
							var pars = 'actionId=${defaultAction}&tcode='+cTransCode+'&comment='+cmts+'&issueMfsId='+issueMfsId+'&actionId=<%=PortalConstants.ACTION_DEFAULT %>';
							
							var myAjax = new Ajax.Updater(
								{success: 'successMsg'}, 
								url, 
								{
									method: 'post', 
									parameters: pars,
									onFailure: reportError,
									onSuccess: function(transport) {
										if (!transport.responseText.match("Inov8 MWallet ID does not exist."))
										{
											$('issueMfsId').value='';
								      		$('custTransCode').value='';
								      		$('comments').value='';
										}	
										else
										{
										
										}
							      		$("btnSubmit").disabled='';	
									    $('errorMsg').innerHTML = "";
									  	Element.hide('errorMsg'); 
									    Element.show('successMsg');
									    }
								});
						}
					}
	      	}
	     
	      	
	      	function reportError(request)
	      	{
			  $('successMsg').innerHTML = "";
			  Element.hide('successMsg'); 
			  $('errorMsg').innerHTML = "Error occured while processing. Please contact with administrator for more details";
			  Element.show('errorMsg');
      		  $("btnSubmit").disabled='';
	      	}

	</script>
		
	</head>
	<body bgcolor="#ffffff" onunload="javascript:closeChild();" >
	
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
	<div id="errorMsg" class ="errorMsg" style="display:none;"></div>

	
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		<table width="100%">
			<html:form name='customerDisputeForm'
				commandName="extendedCustDisputeTranVrDcListViewModel" method="post"
				action="p_customerdisputeprodmanagement.html"  onsubmit="return validateForm()" >
				<tr>
					<td width="33%" align="right" class="formText">
						Inov8 MWallet ID:
					</td>
					<td width="17%" align="left">
						<html:input onkeypress="return maskCommon(this,event)" path="mfsId" id="mfsId" cssClass="textBox" maxlength="8" tabindex="1" /> 
						${status.errorMessage}
					</td>
					<td width="16%" align="right" class="formText">
						Mobile #:
					</td>
					<td width="34%" align="left">
						<html:input onkeypress="return maskCommon(this,event)" path="currentMobileNo" id="currentMobileNo" cssClass="textBox" maxlength="11" tabindex="2" />
						${status.errorMessage}
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Issue Code:
					</td>
					<td align="left">


					<html:input onkeypress="return maskCommon(this,event)" path="issueCode" id="issueCode" cssClass="textBox" maxlength="50" tabindex="3" />
						${status.errorMessage}

 
					</td>
					<td class="formText" align="right">
						Start Date:
					</td>
					<td align="left">
						<html:input path="startDate" readonly="true" id="startDate" tabindex="-1"  cssClass="textBox" maxlength="10" />
						<img id="sDate" tabindex="4" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="5" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>	
				</tr>
				<tr>
					<td class="formText" align="right">
						Transaction Code:
					</td>
					<td align="left">
						<html:input onkeypress="return maskCommon(this,event)" path="transactionCode" id="transactionCode" cssClass="textBox" maxlength="50" tabindex="6" />
						${status.errorMessage}
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
						<html:input path="endDate" readonly="true" id="endDate" tabindex="-1"  cssClass="textBox" maxlength="10" />
						<img id="eDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="eDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />	
					</td>
				</tr>
				<tr>
                  <td class="formText" align="right">Product:</td>
                  <td align="left"> 
	                  <html:select path="productId" cssClass="textBox" tabindex="9"> 
	                  <html:option value="">---All---</html:option> 
	                  <c:if test="${custDisputeProductListViewModelList != null}"> <html:options items="${custDisputeProductListViewModelList}" itemValue="productId" itemLabel="productName"/> </c:if> </html:select> </td>
                  <td colspan="2">&nbsp; </td>
		    </tr>


				<tr>
                  <td class="formText" align="right">Bank:</td>
                  <td align="left"> 

	                  <html:select path="bankId" cssClass="textBox" tabindex="9"> 
							<html:option value="">---All---</html:option>
							<c:if test="${bankModelList!=null}">
								<html:options items="${bankModelList}"
									itemLabel="name" itemValue="bankId" />
							</c:if>
					</html:select>
                  <td colspan="2">&nbsp; </td>
		    </tr>


				<tr>
				  <td class="formText" align="right">&nbsp;</td>
				  <td align="left"><span class="formText">
				  
				   <input type ="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>" />           
			          <security:isauthorized action="<%=PortalConstants.ACTION_RETRIEVE%>">
						<jsp:attribute name="ifAccessAllowed">
						<input name="_search" type="submit" class="button"	tabindex="10"					
						value="Search" />
					    </jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed">
						<input name="_search" type="submit" class="button"	tabindex="-1"					
						value="Search" disabled="disabled"/>
						</jsp:attribute>
			        </security:isauthorized>
				   
                    <input name="reset" type="reset" class="button" value="Cancel" tabindex="11" onclick="javascript: window.location='p_customerdisputeprodmanagement.html?actionId=${retriveAction}'"/>
</span></td>
				  <td colspan="2">&nbsp;</td>
		    </tr>

			</html:form>
		</table>
		<c:set var="RIFC" scope="page">
			<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.DISPUTE_RIFC.toString()%>"/>
		</c:set>
		<c:set var="INVALID" scope="page">
			<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.DISPUTE_INVALID.toString()%>"/>
		</c:set>
		<c:set var="OPEN" scope="page">
			<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.DISPUTE_OPEN.toString()%>"/>
		</c:set>
		<c:set var="NEW" scope="page">
			<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.DISPUTE_NEW.toString()%>"/>
		</c:set>
		<c:set var="INOV8_RIFC" scope="page">
			<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.INOV8_DISPUTE_RIFC.toString()%>"/>
		</c:set>
		<c:set var="INOV8_INVALID" scope="page">
			<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.INOV8_DISPUTE_INVALID.toString()%>"/>
		</c:set>
		
		<ec:table filterable="false" items="customerDisputeProductList" var="custDisputeTransModel"
		action="${pageContext.request.contextPath}/p_customerdisputeprodmanagement.html?actionId=${retriveAction}"
		title=""
        retrieveRowsCallback="limit"
        filterRowsCallback="limit"
        sortRowsCallback="limit"		
		>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Customer Dispute.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Customer Dispute.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf  view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Customer Dispute" fileName="Customer Dispute.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Customer Dispute.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<c:set var="custDisputeTransTransactionId">
					<security:encrypt strToEncrypt="${custDisputeTransModel.transactionId}"/>
				</c:set>
				<c:set var="custDisputeTransTransactionCodeId">
					<security:encrypt strToEncrypt="${custDisputeTransModel.transactionCodeId}"/>
				</c:set>				
				<c:set var="custDisputeTransIssueId">
					<security:encrypt strToEncrypt="${custDisputeTransModel.issueId}"/>
				</c:set>
				<c:set var="custDisputeTransIssueTypeStatusId">
					<security:encrypt strToEncrypt="${custDisputeTransModel.issueTypeStatusId}"/>
				</c:set>				
				
				<ec:column property="issueCode" filterable="false" title="Issue Code" style="width:">
				<input type ="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>" />
				<security:isauthorized action="<%=PortalConstants.ACTION_RETRIEVE%>">
						<jsp:attribute name="ifAccessAllowed">
						<a href="p_issuehistorymanagement.html" onclick="return openIssueWindow('${custDisputeTransModel.issueCode}')">
					    <c:out value="${custDisputeTransModel.issueCode}"></c:out>
						</a>
						</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed"> 
					    <c:out value="${custDisputeTransModel.issueCode}"></c:out>
						</jsp:attribute>
			        </security:isauthorized>  
				</ec:column>

				<ec:column property="transactionCode" filterable="false" title="Transaction Code" escapeAutoFormat="true">

					<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${custDisputeTransModel.transactionCode}" onclick="return openTransactionWindow('${custDisputeTransModel.transactionCode}')">
					  		<c:out value="${custDisputeTransModel.transactionCode}"></c:out>
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<c:out value="${custDisputeTransModel.transactionCode}"></c:out>	
					</authz:authorize>
 
				</ec:column>

				<ec:column property="mfsId" filterable="false" title="Inov8 MWallet ID" style="width: "/>
				<ec:column property="saleMobileNo" filterable="false" title="Subscriber Number" escapeAutoFormat="true" />
				<ec:column property="notificationMobileNo" filterable="false" title="Sale Number" escapeAutoFormat="true"/>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Date"/>
				<ec:column property="productName" filterable="false" title="Product" />
				<ec:column property="bankName" filterable="false" title="Bank" />
				<ec:column property="bankAccountNoLastFive" filterable="false" title="Account No." escapeAutoFormat="true"/>
				<ec:column property="amount" calc="total" calcTitle="Total:" cell="currency" format="0.00" filterable="false" title="Amount" />
				<ec:column property="processingStatusName" filterable="false" title="Status"/>
				<ec:column  property="rifc" title="RIFC"  filterable="false" style="text-align: center"   sortable="false"  >
			        <c:if test="${custDisputeTransIssueTypeStatusId == NEW}">
   			        	<div id="DIV_RIFC_${custDisputeTransIssueId}">
   			        	
   			        	<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
								value="<%=PortalConstants.ACTION_DEFAULT%>" />
							<security:isauthorized
								action="<%=PortalConstants.ACTION_DEFAULT%>">
								<jsp:attribute name="ifAccessAllowed">
									<input type="button" class="button" value="Resolve" 
					        name="CustomerDisputeRIFC${custDisputeTransModel.transactionId}"
					        onclick="openRifcWindow('CustomerDisputeRIFC${custDisputeTransTransactionId}', '${custDisputeTransTransactionId}','${custDisputeTransTransactionCodeId}', '${custDisputeTransModel.transactionCode}', '${custDisputeTransIssueId}', '${RIFC}', 'pg_dispute')" />
					        	</jsp:attribute>
								<jsp:attribute name="ifAccessNotAllowed">
									<input type="button" class="button" value="Resolve" 
					        name="CustomerDisputeRIFC${custDisputeTransTransactionId}" 
					        disabled="disabled"/>
					    		</jsp:attribute>
							</security:isauthorized>
   			        	 </div>    
			        </c:if>
			        <c:if test="${custDisputeTransIssueTypeStatusId == RIFC || custDisputeTransIssueTypeStatusId == INOV8_RIFC}">YES</c:if>&nbsp;
		       </ec:column>
       
       		   <ec:column  property="invalid" title="In-Valid"  filterable="false" style="text-align: center"   sortable="false" >
			       <c:if test="${custDisputeTransIssueTypeStatusId == NEW}">
				       <div id="DIV_INVALID_${custDisputeTransIssueId}">

							<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
								value="<%=PortalConstants.ACTION_DEFAULT%>" />
							<security:isauthorized
								action="<%=PortalConstants.ACTION_DEFAULT%>">
								<jsp:attribute name="ifAccessAllowed">
									<input type="button" class="button" value="Resolve"
										name="CustomerDisputeINVALID${custDisputeTransTransactionId}"
										onclick="openInvalidWindow('CustomerDisputeINVALID${custDisputeTransTransactionId}', '${custDisputeTransTransactionId}','${custDisputeTransTransactionCodeId}', '${custDisputeTransModel.transactionCode}', '${custDisputeTransIssueId}', '${INVALID}', 'pg_dispute')" />
								</jsp:attribute>
								<jsp:attribute name="ifAccessNotAllowed">
									<input type="button" class="button" value="Resolve"
										name="CustomerDisputeINVALID${custDisputeTransTransactionId}"
										disabled="disabled" />

								</jsp:attribute>
							</security:isauthorized>

							</div>    
			       </c:if>
			       <c:if test="${custDisputeTransIssueTypeStatusId == INVALID || custDisputeTransIssueTypeStatusId == INOV8_INVALID}">YES</c:if>&nbsp;
		       </ec:column>
       
       		   <ec:column  property="i8" title="Escalate To Inov8"  filterable="false" style="text-align: center"  sortable="false" >
			       <c:if test="${custDisputeTransIssueTypeStatusId == NEW}">
				       <div id="DIV_ETOI8_${custDisputeTransIssueId}">
				       <input type ="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_DEFAULT%>" />
				       <security:isauthorized action="<%=PortalConstants.ACTION_DEFAULT%>">
						<jsp:attribute name="ifAccessAllowed">
						<input type="button" class="button" value="Escalate" 
					       name="CustomerDisputeETOI8${custDisputeTransTransactionId}" 
					       onclick="openEsclateWindow('CustomerDisputeETOI8${custDisputeTransTransactionId}', '${custDisputeTransTransactionId}','${custDisputeTransTransactionCodeId}', '${custDisputeTransModel.transactionCode}', '${custDisputeTransIssueId}', '${OPEN}', 'pg_dispute')" />
					   	</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed">
						<input type="button" class="button" value="Escalate" 
					       name="CustomerDisputeETOI8${custDisputeTransTransactionId}" 
					       disabled="disabled">
						</jsp:attribute>
			        </security:isauthorized>  
				       
				       	</div>    
			       </c:if>
			       <c:if test="${custDisputeTransIssueTypeStatusId == OPEN}">YES</c:if>&nbsp;
		       </ec:column>
    		</ec:row>
		</ec:table>
		
		<form  name="issueForm" action="" >
		
		<table width="85%" align="left">
			<tr>
				<td class="formText" align="right" width="25%">
					<span style="color:#FF0000">*</span>Inov8 MWallet ID:
				</td>
				<td align="left" width="75%">
					<input type="text" tabindex="12" onkeypress="return maskNumber(this,event)" id="issueMfsId" class="textBox" maxlength="8" /> 
				</td>
			</tr>
			<tr>
				<td class="formText" align="right" width="25%">
					<span style="color:#FF0000">*</span>Transaction Code:
				</td>
				<td align="left" width="75%">
					<input type="text" tabindex="13" onkeypress="return maskCommon(this,event)" id="custTransCode" class="textBox" maxlength="50" /> 
				</td>
			</tr>
			<tr>
				<td class="formText" align="right" width="25%">
					<span style="color:#FF0000">*</span>Customer Claim:	
				</td>
				<td align="left" width="75%">
					<textarea id="comments" tabindex="14" onkeypress="return maskCommon(this,event)" class="textBox" cols="50" rows="5" style="overflow: auto"></textarea>
				</td>
			</tr>
			<tr>
				<td align="left">&nbsp;
					
			  </td>
				<td align="left">
				 <input type ="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_DEFAULT%>" />
				<security:isauthorized action="<%=PortalConstants.ACTION_DEFAULT%>">
						<jsp:attribute name="ifAccessAllowed">
						<input id="btnSubmit" type="button" class="button"
					value="Escalate to Inov8" onclick="submitIssue()" tabindex="15" />
					   	</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed"> 
						<input id="btnSubmit" type="button" class="button"
					value="Escalate to Inov8" tabindex="-1" disabled="disabled"/>
						</jsp:attribute>
			        </security:isauthorized>  
				</td>
			</tr>
		</table>
		
		</form>
		
	<script language="javascript" type="text/javascript">
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
	
		function openTransactionWindow(transactionCode)
		{
	        newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		    if(window.focus) newWindow.focus();
		    return false;
		}
		
		function openRifcWindow(btnName, transactionId, transactionCodeId, transactionCode, issueStId, rifc, winId)
		{
			childWindow = window.open('p-issueupdateform.html?actionId=${defaultAction}&<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>='+btnName+'&transactionId='+transactionId+'&transactionCodeId='+transactionCodeId+'&transactionCode='+transactionCode+'&issueId='+issueStId+'&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>='+rifc,winId,'width=400,height=250,menubar=no,toolbar=no,left=150,top=150,directories=no,scrollbars=no,resizable=no,status=no');		
		}
		
		
		function openInvalidWindow(btnName, transactionId, transactionCodeId, transactionCode, issueStId, invalid, winId)
		{
			childWindow = window.open('p-issueupdateform.html?actionId=${defaultAction}&<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>='+btnName+'&transactionId='+transactionId+'&transactionCodeId='+transactionCodeId+'&transactionCode='+transactionCode+'&issueId='+issueStId+'&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>='+invalid,winId,'width=400,height=250,menubar=no,toolbar=no,left=150,top=150,directories=no,scrollbars=no,resizable=no,status=no');		
		}


        function openEsclateWindow(btnName, transactionId, transactionCodeId, transactionCode, issueStId, esclate, winId)
        {
        	childWindow = window.open('p-issueupdateform.html?actionId=${defaultAction}&<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>='+btnName+'&transactionId='+transactionId+'&transactionCodeId='+transactionCodeId+'&transactionCode='+transactionCode+'&issueId='+issueStId+'&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>='+esclate,winId,'width=400,height=250,menubar=no,toolbar=no,left=150,top=150,directories=no,scrollbars=no,resizable=no,status=no');		
        }

 

// for issue popup

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
      	</script>
	</body>
</html>
