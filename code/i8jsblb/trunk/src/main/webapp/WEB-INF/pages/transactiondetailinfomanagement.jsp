
<jsp:directive.page import="com.inov8.microbank.common.util.PortalDateUtils"/><!--Author: Jalil-Ur-Rehman -->
<%@include file="/common/taglibs.jsp"%>

<html>
  <head>
<meta name="decorator" content="decorator">

   <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script> 
<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />	    

<meta name="title" content="Customer Transactions" />


  </head>
  
  <body bgcolor="#ffffff">
  <%@include file="/common/ajax.jsp"%>
  
  <script language="javascript" type="text/javascript">
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
	      
	      function openTransactionDetailWindow(transactionCode)
		  {
		      var action = 'transactiondetailmanagement.html?transactionCodeId='+transactionCode;
              newWindow = window.open( action , 'TransactionDetail', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
              if(window.focus) newWindow.focus();
                    return false;
		  }
	      
  </script>
  <spring:bind path="extendedTransactionDetInfoListViewModel.*">
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


<form name="transactionDetailInfoForm" method="post" action="transactiondetailinfomanagement.html" onsubmit="return onFormSubmit(this);">
<table width="100%" >
<tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Transaction ID:&nbsp;</td>
    <td><spring:bind path="extendedTransactionDetInfoListViewModel.transactionCode">
      <input type="text" name="${status.expression}" class="textBox" maxlength="10" value="${status.value}" onkeypress="return maskAlphaNumeric(this,event)"/>
      </spring:bind>
    </td>
  </tr>

<tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Customer ID:&nbsp;</td>
    <td><spring:bind path="extendedTransactionDetInfoListViewModel.mfsId">
      <input type="text" name="${status.expression}" class="textBox" maxlength="8" value="${status.value}" onkeypress="return maskNumber(this,event)"/>
      </spring:bind>
    </td>
  </tr>
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Mobile No:&nbsp;</td>
    <td><spring:bind path="extendedTransactionDetInfoListViewModel.saleMobileNo">
      <input type="text" name="${status.expression}" class="textBox" maxlength="13" value="${status.value}" onkeypress="return maskNumber(this,event)"/>
      </spring:bind>
    </td>
  </tr>

<tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Transaction Type:&nbsp;</td>
    <td><spring:bind path="extendedTransactionDetInfoListViewModel.transactionTypeId">
      
      <select name="${status.expression}" class="textBox">
		<option value="">---All---</option>
              <c:forEach items="${transactionTypeModelList}" var="transactionTypeModelList">
                <option value="${transactionTypeModelList.transactionTypeId}"
                  <c:if test="${status.value == transactionTypeModelList.transactionTypeId}">selected="selected"</c:if>/>
                  ${transactionTypeModelList.name}
                </option>
              </c:forEach>
            </select>
      </spring:bind>
    </td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Service Type:&nbsp;</td>
    <td><spring:bind path="extendedTransactionDetInfoListViewModel.servicetypeid">
		<select name="${status.expression}" class="textBox">
		<option value="">---All---</option>
              <c:forEach items="${serviceTypeModelList}" var="serviceTypeModelList">
                <option value="${serviceTypeModelList.serviceTypeId}"
                  <c:if test="${status.value == serviceTypeModelList.serviceTypeId}">selected="selected"</c:if>/>
                  ${serviceTypeModelList.name}
                </option>
              </c:forEach>
            </select>
      </spring:bind>
    </td>
  </tr>



<tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Supplier:&nbsp;</td>
    <td><spring:bind path="extendedTransactionDetInfoListViewModel.supplierid">
<select name="${status.expression}" class="textBox" id="${status.expression}">
								 <c:if test="${empty supplierModelList}">
								<option value="">---All---</option>
								 </c:if>
								
								  <c:if test="${not empty supplierModelList}">
								 <option value="">---All---</option>
								<c:forEach items="${supplierModelList}" var="supplierModelList">
									<option value="${supplierModelList.supplierId}"
										<c:if test="${status.value == supplierModelList.supplierId}">selected="selected"</c:if>>
										${supplierModelList.name}
									</option>
								</c:forEach>
								</c:if>
							</select>
        ${status.errorMessage}
      </spring:bind>
    </td>
  </tr>
  
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Product:&nbsp;</td>
    <td><spring:bind path="extendedTransactionDetInfoListViewModel.productId">
      
      <select name="${status.expression}" class="textBox" id="${status.expression}">
							
							 
							 <c:if test="${empty productModelList}">
							 <option value="">---All---</option>
							 </c:if>
							   <c:if test="${not empty productModelList}">
								 <option value="">---All---</option>
								<c:forEach items="${productModelList}" var="productModelList">
									<option value="${productModelList.productId}"
										<c:if test="${status.value == productModelList.productId}">selected="selected"</c:if>>
										${productModelList.name}
									</option>
								</c:forEach>
								</c:if>	
							</select>
        ${status.errorMessage}
      </spring:bind>
    </td>
  </tr>
  
  

  
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Bank:&nbsp;</td>
    <td><spring:bind path="extendedTransactionDetInfoListViewModel.bankid">
      
      	<select name="${status.expression}" class="textBox">
      	<option value="">---All---</option>
								<c:forEach items="${bankModelList}" var="bankModelList">
									<option value="${bankModelList.bankId}"
										<c:if test="${status.value == bankModelList.bankId}">selected="selected"</c:if>>
										${bankModelList.name}
									</option>
								</c:forEach>
							</select>
        ${status.errorMessage}
      
      </spring:bind>
    </td>
  </tr>
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Is Commissioned User:&nbsp;</td>
    <td><spring:bind path="extendedTransactionDetInfoListViewModel.commissioned">      
      	<select name="${status.expression}" class="textBox">  
      	<c:choose>      	        	          	
	 	    <c:when test="${status.value == ''}"> 	     
	 	       <option value="" selected="selected">---All---</option>		
	 	       <option value="0">No</option>
	 	       <option value="1">Yes</option>
	 	    </c:when>
	 	    <c:otherwise>
	  	      <option value="">---All---</option>
	      	  <option value="0" <c:if test="${status.value == false}">selected="selected"</c:if>>No</option>
			  <option value="1" <c:if test="${status.value == true}">selected="selected"</c:if>>Yes</option>
	  	    </c:otherwise>
  	    </c:choose>  	    
		</select>
      </spring:bind>
    </td>
  </tr>  
  
  
  
   <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">From Date:&nbsp;</td>
    <td><spring:bind path="extendedTransactionDetInfoListViewModel.startDate">
     	<input type="text" name="${status.expression}" class="textBox" id="${status.expression}"
								maxlength="50" value="${status.value}" readonly="readonly" />
						</spring:bind>
						<img id="fDate" tabindex="4"  name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="fDate" tabindex="5" name="popcal" title="Clear Date" onclick="javascript:$('startDate').value=''"  align="middle" style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />  
      	
     
    </td>
  </tr>


<tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">To Date:&nbsp;</td>
    <td><spring:bind path="extendedTransactionDetInfoListViewModel.endDate">
     	<input type="text" name="${status.expression}" class="textBox" id="${status.expression}"
								maxlength="50" value="${status.value}" readonly="readonly" />
						</spring:bind>
						<img id="tDate" tabindex="4"  name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="tDate" tabindex="5" name="popcal" title="Clear Date" onclick="javascript:$('endDate').value=''"  align="middle" style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />  
      	
     
    </td>
  </tr>
<tr>
<td></td>
    <td align="left" ><input type="submit"  class="button" value="Search" tabindex="3" name="_search" onClick="/*javascript:resetExportParameters('transactiondetailinfomanagement');*/"/>
    <input type="reset" class="button" value="Cancel"  name="_cancel" onClick="javascript: window.location='transactiondetailinfomanagement.html'"></td>
    <td align="left" >&nbsp;</td>
    <td align="left" >&nbsp;
   </td>
   

  </tr>

			
	</table>
	
	  <ajax:select source="supplierId" target="productId"
			baseUrl="${contextPath}/shipmentFormRefDataController.html"
			parameters="supplierId={supplierId},rType=1" errorFunction="error" />
</form>


<ec:table filterable="false"
		items="transactionDetInfoListViewModelList"
                var = "transactionDetInfoListViewModelList"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit"
		action="${pageContext.request.contextPath}/transactiondetailinfomanagement.html"
		title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Customer Transactions.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Customer Transactions.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Customer Transactions" fileName="Customer Transactions.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Customer Transactions.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
		<ec:row>
		
			<ec:column property="createdOn" filterable="false" title="Transaction Date" cell="date" format="dd/MM/yyyy"/>	
			<ec:column property="transactionCode" filterable="false" title="Transaction ID"/>			
			<ec:column property="mfsId" filterable="false" title="Customer ID"/>
			<ec:column property="saleMobileNo" filterable="false" title="Mobile No"/>
			<ec:column property="transactiontype" filterable="false" title="Transaction Type"/>
			<ec:column property="servicetypename" filterable="false" title="Service Type"/>
			<ec:column property="suppliername" filterable="false" title="Supplier"/>
			<ec:column property="productname" filterable="false" title="Product"/>
			<ec:column property="bankname" filterable="false" title="Bank"/>
			<ec:column property="bankAccountNoLastFive" filterable="false" title="Account No."/>
			<ec:column property="discountAmount" filterable="false" title="Discount"/>			
			<ec:column alias=" "  title="Detail " sortable="false" filterable="false">
			  <a href="${pageContext.request.contextPath}/transactiondetailmanagement.html?transactionCodeId=${transactionDetInfoListViewModelList.transactionCode}" onclick="return openTransactionDetailWindow('${transactionDetInfoListViewModelList.transactionCode}')">Detail</a>						    				
 			</ec:column>					
      </ec:row>
   </ec:table>



<script type="text/javascript">

			Calendar.setup(
      		{
		       inputField  : "startDate", // id of the input field
			   button      : "fDate",    // id of the button
		    }
      		);
			Calendar.setup(
		    {
		      inputField  : "endDate", // id of the input field
		      button      : "tDate",    // id of the button
		  	  isEndDate: true
		    }
		    );
      
      
      function isDateGreaterOrEqualForCard(from, to) {

	var fromDate;
	var toDate;

	var result = false;

//  format dd/mm/yyyy
	fromDate = from.substring(6,10) + from.substring(3,5) + from.substring(0,2);
	toDate = to.substring(6,10)   + to.substring(3,5) + to.substring(0,2);
	if( fromDate != toDate && fromDate > toDate ) {
		result = true;
	}

	return result;
}
      
function onFormSubmit(theForm) {

var fromDate = theForm.startDate.value.substring(6,10) + theForm.startDate.value.substring(3,5) + theForm.startDate.value.substring(0,2);
var toDate = theForm.endDate.value.substring(6,10)   + theForm.endDate.value.substring(3,5) + theForm.endDate.value.substring(0,2);
var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';

		 if(trim(theForm.endDate.value) == '' ) 
		 {
					
					 toDate=currServerDate.substring(6,10)   + currServerDate.substring(3,5) + currServerDate.substring(0,2);
					
		 }

	
	   if(   trim(theForm.startDate.value) != '' && fromDate > toDate )
	{
			alert('To Date can never be lesser than from Date');
			document.getElementById('tDate').focus();
			return false;
	}		
	
   if( trim(theForm.endDate.value) != '' && isDateGreater(theForm.endDate.value,currServerDate)){
			alert('Future To date is not allowed.');
			document.getElementById('tDate').focus();
			return false;
		}

   return true;
}
</script>


</body>
</html>
