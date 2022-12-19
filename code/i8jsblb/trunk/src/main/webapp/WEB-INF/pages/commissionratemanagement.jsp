<!--Author: Asad Hayat -->

<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>
<meta name="decorator" content="decorator">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
      <meta name="title" content="Transaction Charges"/>
      <script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
	<%
		String addTxChargePermission = PortalConstants.ADMIN_GP_CREATE;
		addTxChargePermission +=","+PortalConstants.PG_GP_CREATE;
		addTxChargePermission+=","+PortalConstants.MNG_TX_CHARGES_CREATE;
		
		String updateTxChargePermission = PortalConstants.ADMIN_GP_UPDATE;
		updateTxChargePermission +=","+PortalConstants.PG_GP_UPDATE;
		updateTxChargePermission+=","+PortalConstants.MNG_TX_CHARGES_UPDATE;
	%>
   </head>
   <body bgcolor="#ffffff">
   	  <div id="successMsg" class ="infoMsg" style="display:none;"></div>
      <c:if test="${not empty messages}">
        <div class="infoMsg" id="successMessages">
          <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/>
            <br/>
          </c:forEach>
        </div>
        <c:remove var="messages" scope="session"/>
      </c:if>
      <authz:authorize ifAnyGranted="<%=addTxChargePermission%>">
		<div align="right">
  			<a href="commissionrateform.html" class="linktext">
    			Add Transaction Charges
  			</a><br>&nbsp;&nbsp;
		</div>
      </authz:authorize>
      <ec:table
      items="commissionRateList"
      var = "commissionRateModel"
      retrieveRowsCallback="limit"
      filterRowsCallback="limit"
      sortRowsCallback="limit"
      action="${pageContext.request.contextPath}/commissionratemanagement.html"
      title="">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Transaction Charges.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Transaction Charges.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Transaction Charges" fileName="Transaction Charges.pdf" tooltip="Export PDF" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Transaction Charges.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>			
         <ec:row>
         	<ec:column property="segmentName" title="Segment"/>
           <ec:column property="productName" title="Product"/>
           <ec:column property="rangeStarts" title="Slab Range Starts" cell="number" format="#.##"/>
           <ec:column property="rangeEnds" title="Slab Range Ends"  cell="number" format="#.##" />
           <ec:column property="rate" title="Rate" cell="currency" format="#.##" />
           <ec:column property="commissionTypeName" title="Charges Type"/>
           <ec:column property="commissionReasonName" title="Charges Method"/>    
           <ec:column property="commissionRateId" title="Edit" filterable="false" escapeAutoFormat="true" sortable="false" viewsAllowed="html" style="text-align:center;">
				<authz:authorize ifAnyGranted="<%=updateTxChargePermission %>">			
					<a href ="${contextPath}/commissionrateform.html?commissionRateId=${commissionRateModel.commissionRateId}">Edit</a>		
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=updateTxChargePermission %>">
					<input type="button" class="button" style="width='90px'" value="Edit" disabled="disabled" />		
				</authz:authorize>
  				</ec:column>
  				<ec:column  property="commissionRateId" title="Deactivate" filterable="false" sortable="false" viewsAllowed="html" style="text-align:center;">
  				<authz:authorize ifAnyGranted="<%=updateTxChargePermission %>">
    			<tags:activatedeactivate 
					id="${commissionRateModel.commissionRateId}" 
					model="com.inov8.microbank.common.model.CommissionRateModel" 
					property="active"
					propertyValue="${commissionRateModel.active}"
					callback="actdeact"
					error="defaultError"
				/>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=updateTxChargePermission %>">
				<c:choose>
				<c:when test="${commissionRateModel.active}"><input id="btn_deAct"type="button" class="button" style="width='90px';"value="Deactivate" disabled="disabled" /></c:when>
				<c:otherwise><input id="btn_act"type="button" class="button" style="width='90px';"value="Activate" disabled="disabled" /></c:otherwise>
				</c:choose>
				</authz:authorize>
  				</ec:column>
         </ec:row>
      </ec:table>
      
      <script language="javascript" type="text/javascript">
      function confirmUpdateStatus(link)
      {
        if (confirm('Are you sure you want to update status?')==true)
        {
          window.location.href=link;
        }
      }
      </script>
      
   </body>
</html>



