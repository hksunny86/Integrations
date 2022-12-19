<!--Author: Naseer Ullah-->
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="updateAction"><%=PortalConstants.ACTION_UPDATE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
        <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<%@include file="/common/ajax.jsp"%>
		<script language="javascript" type="text/javascript">
			function error(request)
		    {
		     	alert("An unknown error has occured. Please contact with the administrator for more details");
		    }
		</script>
		<meta name="title" content="Stakeholder Shares"/>
		
		<%
			String createPermission = PortalConstants.ADMIN_GP_CREATE;
			createPermission +=	"," + PortalConstants.PG_GP_CREATE;
			createPermission +=	"," + PortalConstants.MNG_COMM_SH_SHARE_CREATE;
		
			String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
			updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
			updatePermission +=	"," + PortalConstants.MNG_COMM_SH_SHARE_UPDATE;
		 %>
		
	</head>
	<body bgcolor="#ffffff">
	
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
			<div id="errorMsg" class ="errorMsg" style="display:none;"></div>
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
			    <c:forEach var="msg" items="${messages}">
			      <c:out value="${msg}" escapeXml="false"/>
			      <br/>
			    </c:forEach>
			</div>
	  		<c:remove var="messages" scope="session"/>
		</c:if>
		
		<authz:authorize ifAnyGranted="<%=createPermission %>">
		   <div align="right"><a href="commissionshsharesform.html?actionId=${createAction}" class="linktext"> Add Commission Stakeholder shares </a></div>
		</authz:authorize>
		<br/>
		<html:form action="p_commissionshsharesmanagement.html" commandName="commissionShSharesViewModel" method="post">
		    <table border="0" width="750px">
		        <tr>
		            <td align="right" class="formText">Supplier:</td>
		            <td>
		            	<html:select id="supplierId" path="supplierId" cssClass="textBox" tabindex="1">
							<html:option value="">---All---</html:option>
							<c:if test="${supplierModelList != null}">
								<html:options items="${supplierModelList}" itemValue="supplierId" itemLabel="name"/>
							</c:if>
						</html:select>
		            </td>
		            <td align="right" class="formText">Product:</td>
		            <td>
		            	<html:select id="productId" path="productId" cssClass="textBox" tabindex="2">
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
							</c:if>
						</html:select>
		            </td>
		        </tr>
		        <tr>
					<td>&nbsp;</td>
					<td align="left" class="formText">
						<input type="submit" class="button" value="Search" name="_search" tabindex="3"/>
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_commissionshsharesmanagement.html'" tabindex="4"/>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
		    </table>
		    <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>"/> 
		</html:form>
		<ec:table filterable="false" items="commShSharesViewModelList" var="commissionShSharesViewModel" action="${contextPath}/p_commissionshsharesmanagement.html?actionId=${retriveAction}"
			retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Stakeholder Shares.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Stakeholder Shares.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="List Commission Stakeholder Shares"
					fileName="Stakeholder Shares.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Stakeholder Shares.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="productName" title="Product" style="text-align: left" width="15%"/>
				<ec:column property="bank" title="Bank Share" style="text-align: center"/>
				<ec:column property="fed" alias="fed" title="FED" style="text-align: center"/>
				<ec:column property="agent1" title="Agent 1 Share" style="text-align: center"/>
				<ec:column property="agent2" title="Agent 2 Share" style="text-align: center"/>
				<ec:column property="wht" title="W. Holding Tax" style="text-align: center"/>
				<ec:column property="franchise1" title="Franchise 1 Share" style="text-align: center"/>
				<ec:column property="franchise2" title="Franchise 2 Share" style="text-align: center"/>
				<authz:authorize ifAnyGranted="<%=updatePermission%>">
					<ec:column  property="productId" alias="edit" title="Actions" viewsDenied="xls" filterable="false" sortable="false" style="text-align:center;">
		          		<input type="button" class="button" style="width='100px'" value="Edit" onclick="javascript:window.location.href='${contextPath}/commissionshsharesform.html?productId=${commissionShSharesViewModel.productId}';" />
		        	</ec:column>
		        </authz:authorize>
		        
			</ec:row>
		</ec:table>

		<ajax:select source="supplierId" target="productId" baseUrl="${contextPath}/p_refData.html"
			parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>

		<script type="text/javascript">
			document.forms[0].supplierId.focus();
		</script>
	</body>
</html>
