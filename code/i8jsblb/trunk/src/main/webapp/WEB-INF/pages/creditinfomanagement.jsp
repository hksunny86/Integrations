
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

<meta name="title" content="Credit Information" />


  </head>
  
  <body bgcolor="#ffffff">
  <%@include file="/common/ajax.jsp"%>
  
  <script language="javascript" type="text/javascript">
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
      </script>




<form name="creditInfoForm" method="post" action="creditinfomanagement.html">
<table width="100%" >

<tr bgcolor="FBFBFB">





    <td align="right" bgcolor="F3F3F3" class="formText">Distributor:&nbsp;</td>
    <td>

      <spring:bind path="extendedCreditInfoListViewModel.distributorId">
      <select name="distributorId" class="textBox" onchange="javascript:document.creditInfoForm.retailerId.options[0].selected = 'selected'; ">
		<option value="">---All---</option>
              <c:forEach items="${distributorModelList}" var="distributorModelList">
                <option value="${distributorModelList.distributorId}"
                  <c:if test="${status.value == distributorModelList.distributorId}">selected="selected"</c:if>/>
                  ${distributorModelList.name}
                </option>
              </c:forEach>
            </select>
            </spring:bind>

            
      
    </td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Retailer:&nbsp;</td>
    <td>
    <spring:bind path="extendedCreditInfoListViewModel.retailerId">
		<select name="retailerId" class="textBox" onchange="javascript:document.creditInfoForm.distributorId.options[0].selected = 'selected';">
		<option value="">---All---</option>
              <c:forEach items="${retailerModelList}" var="retailerModelList">
                <option value="${retailerModelList.retailerId}"
                  <c:if test="${status.value == retailerModelList.retailerId}">selected="selected"</c:if>/>
                  ${retailerModelList.name}
                </option>
              </c:forEach>
            </select>
            </spring:bind>
      
    </td>
  </tr>
  
  <tr>
<td></td>
    <td align="left" ><input type="submit"  class="button" value="Search" tabindex="3" name="_search" onClick="/*javascript:resetExportParameters('creditInfomanagement');*/"/>
    <input type="reset" class="button" value="Cancel"  name="_cancel" onClick="javascript: window.location='creditinfomanagement.html'"></td>
    <td align="left" >&nbsp;</td>
    <td align="left" >&nbsp;
   </td>
   </tr>
	</table>
</form>

<ec:table filterable="false"
		items="extendedCreditInfoListViewModelList"
        var = "creditInfoListViewModelList"
        retrieveRowsCallback="limit"
        filterRowsCallback="limit"
        sortRowsCallback="limit"
		action="${pageContext.request.contextPath}/creditinfomanagement.html"
		title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Credit Information.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Credit Information.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Credit Information" fileName="Credit Information.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Credit Information.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
		<ec:row>
			<ec:column property="name" filterable="false" title="Name"/>
			<ec:column property="organizationName" filterable="false" title="Retailer/Distributor"/>
			<ec:column property="mobileNo" filterable="false" title="Mobile Number" escapeAutoFormat="true"/>
			<ec:column property="balance" filterable="false" title="Credit" cell="currency"  format="0.00" style="text-align: right;"/>
			
    </ec:row>
	</ec:table>
</body>
</html>
