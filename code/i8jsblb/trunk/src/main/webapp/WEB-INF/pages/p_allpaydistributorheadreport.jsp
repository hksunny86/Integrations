
<jsp:directive.page import="com.inov8.microbank.common.util.PortalDateUtils"/><!--Author: Jawwad Farooq -->
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

<meta name="title" content="AllPay Agent Head Report" />


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
		      var action = 'allpaytransactiondetailmanagement.html?transactionCodeId='+transactionCode;
              newWindow = window.open( action , 'TransactionDetail', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
              if(window.focus) newWindow.focus();
                    return false;
		  }
	      
  </script>
  <spring:bind path="distHeadReportListViewModel.*">
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


<form name="distHeadReportListViewModel" method="post" action="p_allpaydistributorheadreport.html?actionId=2" onsubmit="return onFormSubmit(this);">
<table width="100%" >

  
  

<tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Date:&nbsp;</td>
    <td><spring:bind path="distHeadReportListViewModel.createdOn">
     	<input type="text" name="${status.expression}" class="textBox" id="${status.expression}"
								maxlength="50" value="${status.value}" readonly="readonly" />
						</spring:bind>
						<img id="tDate" tabindex="4"  name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="tDate" tabindex="5" name="popcal" title="Clear Date" onclick="javascript:$('createdOn').value=''"  align="middle" style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />  
      	
     
    </td>
  </tr>
<tr>
<td></td>
    <td align="left" ><input type="submit"  class="button" value="Search" tabindex="3" name="_search" onClick="/*javascript:resetExportParameters('transactiondetailinfomanagement');*/"/>
    </td>
    <td align="left" >&nbsp;</td>
    <td align="left" >&nbsp;
   </td>
   

  </tr>

			
	</table>
	
	 
</form>


<ec:table filterable="false"
		items="distHeadReportListViewModelList"
                var = "distHeadReportModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit"
		action="${pageContext.request.contextPath}/p_allpaydistributorheadreport.html"
		title="">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="AllPay Agent Head Report.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="AllPay Agent Head Report.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="AllPay Agent Head Report" fileName="AllPay Agent Head Report.pdf" tooltip="Export PDF"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="AllPay Agent Head Report.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
			
				
		<ec:row>
		
			<ec:column property="createdOn" filterable="false" title="Transaction Date" cell="date" format="dd/MM/yyyy"/>			
			<ec:column property="startDayBalance" filterable="false" title="Balance at the start of the day" format="###,###,###" sortable="false"/>
			<ec:column property="distAmountDisbursed" filterable="false" title="Balance disbursed in a day"/>
			<ec:column property="balance" filterable="false" title="Available balance"  sortable="false"/>
				
						

			<ec:column property="userId" filterable="false" title="Retailer Head ID" escapeAutoFormat="true"/>
			<ec:column property="retailerUsername" filterable="false" title="Retailer Head CSC" escapeAutoFormat="true"/>
			
			<ec:column property="totalAmount" filterable="false" title="Amount transferred to Retailer Head" escapeAutoFormat="true"/>
			
			<ec:column property="retailerHeadTransactions" filterable="false" title="Retailer Head total transactions for the day" format="###"/>
			<ec:column property="retAmountDisbursed" filterable="false" title="Retailer Head total amount disbursed for the day"/>
			<ec:column property="startDayBalanceRet" filterable="false" title="Retailer Head Balance available at the start of the day" sortable="false"/>
			<ec:column property="endDayBalance" filterable="false" title="Retailer Head Balance available at the end of the day"  sortable="false"/>
			
			
      </ec:row>
   </ec:table>



<script type="text/javascript">


Calendar.setup(      
      {  
        inputField  : "createdOn", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "tDate"    // id of the button
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


var toDate = theForm.createdOn.value.substring(6,10)   + theForm.createdOn.value.substring(3,5) + theForm.createdOn.value.substring(0,2);
var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';

		 if(trim(theForm.createdOn.value) == '' ) 
		 {
					
					 toDate=currServerDate.substring(6,10)   + currServerDate.substring(3,5) + currServerDate.substring(0,2);
					
		 }

	
		
	
   if( trim(theForm.createdOn.value) != '' && isDateGreater(theForm.createdOn.value,currServerDate)){
			alert('Future date is not allowed.');
			document.getElementById('createdOn').focus();
			return false;
		}

   return true;
}
</script>


</body>
</html>
