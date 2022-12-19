<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.FinancialInstitutionConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="veriflyFinancialInstitution"><%=FinancialInstitutionConstants.VERIFLY_FINANCIAL_INSTITUTION %></c:set>

<html>
	<head>
<meta name="decorator" content="decorator">
	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/date-validation.js"></script>
      
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<%@include file="/common/ajax.jsp"%>
		<meta name="title" content="Agent Transaction Details" />
      <script language="javascript" type="text/javascript">
      
	      var popupWidth = 550;
		  var popupHeight = 350;
		  var popupLeft = (window.screen.width - popupWidth)/2;
		  var popupTop = (window.screen.height - popupHeight)/2;
	      function openTransactionDetailWindow(transactionCode)
		  {  
		      var action = 'p_showtransactiondetail.html?actionId='+${retriveAction}+'&transactionCodeId='+transactionCode+'&isMfs=false';
              newWindow = window.open( action , 'TransactionDetail','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
              if(window.focus) newWindow.focus();
                    return false;
		  }
      
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
	      
      </script>
		<%
		String readPermission = PortalConstants.CSR_GP_READ;
		readPermission += "," + PortalConstants.ADMIN_GP_READ;
		readPermission += "," + PortalConstants.PG_GP_READ;
		 %>
		
	</head>
	<body bgcolor="#ffffff">
	
		<div id="rsp" class="ajaxMsg"></div>
	
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		<table width="750px">
			<html:form name='transactionDetailI8Form'
				commandName="extendedTransactionDetailPortalListModel" method="post"
				action="p_allpaytransactiondetaili8management.html" onsubmit="return validateForm(this)">
				<tr>
					<td class="formText" width="17%" align="right">
						Transaction ID:
					</td>
					<td align="left"  width="33%">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" tabindex="1" maxlength="50" /> 
					</td>
				</tr>
				<tr>
					<td class="formText" width="17%" align="right">
						Agent ID:
					</td>
					<td align="left" width="33%" >
						<html:input path="allpayId" id="allpayId" cssClass="textBox" maxlength="12" tabindex="2" /> 
					</td>
					<td class="formText" align="right" width="17%">
					Authorization Code:
					</td>
					<td align="left" width="33">
					<html:input path="authorizationCode" tabindex="3"  id="authorizationCode" cssClass="textBox" maxlength="50" /> 
					</td>
				</tr>
				
				<tr>
					<td class="formText" align="right">
						Supplier:
					</td>
					<td align="left">
						<html:select path="supplierId" cssClass="textBox" tabindex="4" >
							<html:option value="">---All---</html:option>
							<c:if test="${supplierModelList != null}">
								<html:options items="${supplierModelList}" itemValue="supplierId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right">
						Product:
					</td>
					<td align="left">
						<html:select path="productId" cssClass="textBox" tabindex="5" >
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>	
				</tr>
				<tr>
					<td class="formText" align="right">
						Bank:
					</td>
					<td align="left">
						<html:select path="bankId" cssClass="textBox" tabindex="6" >
							<html:option value="">---All---</html:option>
							<c:if test="${bankModelList != null}">
								<html:options items="${bankModelList}" itemValue="bankId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right">
						Payment Mode:
					</td>
					<td align="left">
						<html:select path="paymentModeId" cssClass="textBox" tabindex="7" >
							<html:option value="">---All---</html:option>
							<c:if test="${paymentModeList != null}">
								<html:options items="${paymentModeList}" itemValue="paymentModeId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						<span class="asterisk">*</span>Start Date:
					</td>
					<td align="left" >
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="8" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="9" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						<span class="asterisk">*</span>End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="10" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="11" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>



				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
				  <security:isauthorized action="<%=PortalConstants.ACTION_RETRIEVE%>">
						<jsp:attribute name="ifAccessAllowed">
								<input name="_search" type="submit" class="button" value="Search" tabindex="12"  /> 
		          		</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed">  
					          <input name="_search" type="submit" class="button" value="Search" tabindex="-1" disabled="disabled" /> 
						</jsp:attribute>
		         </security:isauthorized> 
					<input name="reset" type="reset" 
						onclick="javascript: window.location='p_allpaytransactiondetaili8management.html?actionId=${retriveAction}'"
						class="button" value="Cancel" tabindex="13" />
					</td>
					<td class="formText" align="right">

					</td>
					<td align="left">
					</td>
				</tr>

				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
					value="<%=PortalConstants.ACTION_RETRIEVE%>">



			</html:form>
		</table>
		
		<ec:table items="transactionDetailPortalList" var="transactionDetailPortalModel"
		action="${pageContext.request.contextPath}/p_allpaytransactiondetaili8management.html?actionId=${retriveAction}"
		title=""
          retrieveRowsCallback="limit"
          filterRowsCallback="limit"
          sortRowsCallback="limit"
          filterable="false"		
		>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Agent Transaction Details.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Agent Transaction Details.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Transaction Details"
					fileName="Agent Transaction Details.pdf" tooltip="Export PDF" />
			</authz:authorize>	
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Agent Transaction Details.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			headerTitle="Transaction Details"
			fileName="transactiondetaili8management.pdf" tooltip="Export PDF" />
			
			<ec:row>
				<ec:column property="transactionCode" filterable="false" title="Transaction ID" escapeAutoFormat="true">
				  <security:isauthorized action="<%=PortalConstants.ACTION_RETRIEVE%>">
					<jsp:attribute name="ifAccessAllowed">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${transactionDetailPortalModel.transactionCode}" onclick="return openTransactionWindow('${transactionDetailPortalModel.transactionCode}')">
						  ${transactionDetailPortalModel.transactionCode}
						</a>
	          		</jsp:attribute>
					<jsp:attribute name="ifAccessNotAllowed">  
				          ${transactionDetailPortalModel.transactionCode}
					</jsp:attribute>
		         </security:isauthorized> 
				</ec:column>
				<ec:column property="allpayId" filterable="false" title="Agent ID" escapeAutoFormat="true"/>				
				
				<ec:column property="paymentMode" filterable="false" title="Payment Mode" />
				
				<ec:column property="bankName" filterable="false" title="Bank" />
				
				<ec:column property="bankAccountNoLastFive" filterable="false" title="Account No." escapeAutoFormat="true" />
				
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Date"/>
				
				<ec:column property="supplierName" filterable="false" title="Supplier" />
				
				<ec:column property="productName" filterable="false" title="Product"/>
				
				<ec:column property="authorizationCode" filterable="false" title="Authorization Code" />								
				
				<!-- New Columns Added By Noor-->

				<ec:column property="transactionAmount" filterable="false" title="Amount" calc="total" calcTitle="Total:"  cell="currency"  format="0.00"/>
				
				<ec:column property="supplierCommission" filterable="false" title="Commission From Supplier" calc="total" calcTitle="Total:"  cell="currency"  format="0.00"/>			
				
				<ec:column property="serviceCharges" filterable="false" title="Service Charges From Customers" calc="total" calcTitle="Total:"  cell="currency"  format="0.00"/>				
								
 			    <ec:column property="totalAmount" filterable="false" title="Total Customer Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00"/>								
 			    
				<ec:column property="toSupplier" filterable="false" title="To Supplier" calc="total" calcTitle="Total:"  cell="currency"  format="0.00"/>								
				
				<ec:column property="toDistributor" filterable="false" title="To Agent " calc="total" calcTitle="Total:"  cell="currency"  format="0.00"/>								
 			    
 			    <ec:column property="toRetailer" filterable="false" title="To Retailer" cell="currency" format="0.00" calc="total" calcTitle="Total:"/>
 			    <ec:column property="toNationalDistributor" filterable="false" title="To National Agent " cell="currency" format="0.00" calc="total" calcTitle="Total:"/>
 			    <ec:column property="mcpl" filterable="false" title="I8 Commission" cell="currency" format="0.00" calc="total" calcTitle="Total:"/>
 			    
				<!-- End -->
				
				<ec:column property="processingStatusName" filterable="false"  title="Status" />
				
				<ec:column property="veriflyStatus" alias=" " title="Verifly Status" style="text-align: center" filterable="false" sortable="false" >
				</ec:column>				
				
				
<%--				<ec:column alias=" " title="Verifly Status" style="text-align: center" filterable="false" sortable="false" >--%>
<%--				    <c:choose>--%>
<%--				       <c:when test="${transactionDetailPortalModel.veriflyStatus == veriflyFinancialInstitution}">--%>
<%--				          OK--%>
<%--				       </c:when>--%>
<%--				       <c:otherwise>--%>
<%--				       </c:otherwise>--%>
<%--				    </c:choose>--%>
<%--				</ec:column>--%>
				<ec:column alias=" "  title="Detail " sortable="false" filterable="false">
					<authz:authorize ifAnyGranted="<%=readPermission%>">
						<input type="button" class="button" value="Detail" onclick="return openTransactionDetailWindow('${transactionDetailPortalModel.transactionCode}')"/>
					</authz:authorize>
   				</ec:column>
			</ec:row>
		</ec:table>

		<ajax:select source="supplierId" target="productId"
		baseUrl="${contextPath}/p_refData.html"
		parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>

		
		<script language="javascript" type="text/javascript">
		
			document.forms[0].allpayId.focus();
			function openTransactionWindow(transactionCode)
			{	
		        newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) newWindow.focus();
			    return false;
			}
		
	        function validateForm(form){
	        	var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        	var _fDate = form.startDate.value;
		  		var _tDate = form.endDate.value;
		  		var startlbl = "Start Date";
		  		var endlbl   = "End Date";
	        	var isValid = validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,currentDate);
	        	return isValid;
	        }
			
      		Calendar.setup(
      		{
		       inputField  : "startDate", // id of the input field
			   button      : "sDate",    // id of the button
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
