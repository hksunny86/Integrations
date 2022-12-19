
<jsp:directive.page import="com.inov8.microbank.common.util.PortalDateUtils"/><!--Author: Jalil-Ur-Rehman -->
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ajax.jsp"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>

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
		<script type="text/javascript" 
			src="${contextPath}/scripts/date-validation.js"></script>
<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />	    

<meta name="title" content="Lesco Bill Collection" />

<script type="text/javascript">
	function go(id){
	
			window.location = '${pageContext.request.contextPath}/lescofiledownload.html?id='+id;
	}
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
  </head>
  
  <body bgcolor="#ffffff">
  <spring:bind path="extendedLescoCollectionModel.*">
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


<form name="lescoCollectionForm" method="post" action="lescocollectionmanagement.html" onsubmit="return onFormSubmit(this);">
<table width="100%" >
  
  
   <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">From Date:&nbsp;</td>
    <td><spring:bind path="extendedLescoCollectionModel.startDate">
     	<input type="text" name="${status.expression}" class="textBox" id="${status.expression}"
								maxlength="50" value="${status.value}" readonly="readonly" />
						</spring:bind>
						<img id="fDate" tabindex="4"  name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="fDate" tabindex="5" name="popcal" title="Clear Date" onclick="javascript:$('startDate').value=''"  align="middle" style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />  
      	
     
    </td>
  </tr>


<tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">To Date:&nbsp;</td>
    <td><spring:bind path="extendedLescoCollectionModel.endDate">
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
</td>
    <td align="left" >&nbsp;</td>
    <td align="left" >&nbsp;
   </td>
   

  </tr>

			
	</table>
	
	
</form>


<ec:table filterable="false"
		items="lescoCollectionList"
                var = "lescoCollectionList"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit"
		action="${pageContext.request.contextPath}/lescocollectionmanagement.html"
		title="">
		
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Lesco Bill Collection.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Lesco Bill Collection.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Lesco Bill Collection" fileName="Lesco Bill Collection.pdf" tooltip="Export PDF"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Lesco Bill Collection.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
			
				
		<ec:row>

			<ec:column viewsAllowed ="xls,html" property="fileName"   alias= " "  title="File Name " sortable="false" filterable="false">
						<a href="javascript:go(${lescoCollectionList.lescoCollectionId});">${lescoCollectionList.fileName}</a>						
 			</ec:column>
			<ec:column property="createdOn" filterable="false" title="Transaction Date" cell="date" format="dd/MM/yyyy"/>	
			
			
			
					
    </ec:row>
	</ec:table>



<script type="text/javascript">
			function onFormSubmit(theForm){
				var currentDate = "<%=PortalDateUtils.getServerDate()%>";
				var _fDate = theForm.startDate.value;
				var _tDate = theForm.endDate.value;
				var startlbl = "From Date";
				var endlbl   = "To Date";
				var isValid = validateDateRange(_fDate,_tDate,startlbl,endlbl,currentDate);
				return isValid;
			}
				
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
      
</script>


</body>
</html>
