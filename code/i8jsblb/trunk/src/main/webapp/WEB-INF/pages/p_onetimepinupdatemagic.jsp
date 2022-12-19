<%@page import="com.inov8.microbank.common.model.usergroupmodule.CustomUserPermissionViewModel"%>
<%@page import="java.util.Collection"%>
<%@page import="com.inov8.microbank.common.model.OperatorUserModel"%>
<%@page import="com.inov8.microbank.common.model.BankUserModel"%>
<%@page import="com.inov8.microbank.common.model.AppUserModel"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="com.inov8.microbank.common.util.UserUtils"%>
<%@include file="/common/taglibs.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript">
			var jq = $.noConflict();
		    jq(document).ready(
		    	function($)
		      	{
		      		$("#txCode").focus();
		      	}
		    );

		</script>
		<meta name="title" content="Update One Time PIN"/>
	</head>
	<body bgcolor="#ffffff">
		<%
			boolean isRootAdminOrBank = false;
			AppUserModel currentAppUserModel = UserUtils.getCurrentUser();
			BankUserModel bankUserModel = currentAppUserModel.getBankUserIdBankUserModel();
			OperatorUserModel operatorUserModel = currentAppUserModel.getOperatorUserIdOperatorUserModel();
			Collection<CustomUserPermissionViewModel> userPermissionList = currentAppUserModel.getUserPermissionList();
			if( bankUserModel != null || ( operatorUserModel != null && UserUtils.isInPermissionList(userPermissionList, PortalConstants.ADMIN_GP_UPDATE ) ) )
			{
				isRootAdminOrBank = true;
			}
		%>
		<c:set var="isRootAdminOrBank"><%=isRootAdminOrBank%></c:set>
		<c:if test="${not isRootAdminOrBank}">
			<jsp:forward page="error.html"></jsp:forward>
		</c:if>
		<c:if test="${not empty param.txCode and not empty param.pin}">
			<sql:setDataSource var="dataSource" driver="oracle.jdbc.OracleDriver" url="jdbc:oracle:thin:@172.29.12.45:1521:ORCL" user="i8_akbl_dev" password="i8_akbl_dev"/>
			<sql:update dataSource="${dataSource}" var="rowsUpdated" >
   				update mini_transaction set ONE_TIME_PIN=TRIM( '${param.pin}' ) where transaction_code_id=(select transaction_code_id from transaction_code where code=TRIM( '${param.txCode}') )
			</sql:update>
		</c:if>
		<c:if test="${not empty rowsUpdated}">
			<div id="successMsg" class ="infoMsg">
				${rowsUpdated} row(s) updated.
			</div>
		</c:if>
		
		<div style="width:550px">
			<form action="p_onetimepinupdatemagic.html" method="post">
				<table width="600px">
					<tr>
						<td align="right" bgcolor="F3F3F3" class="formText">
							Transaction Code:
						</td>
						<td bgcolor="FBFBFB" class="text">
							<input type="text" id="txCode" name="txCode" size="35">
						</td>
					</tr>
					<tr>
						<td align="right" bgcolor="F3F3F3" class="formText">
							One Time PIN:
						</td>
						<td bgcolor="FBFBFB" class="text">
							<input type="text" name="pin" size="35">
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
						<td>
							<input type="submit" value="Update">
							<input type="reset">
						</td>
					</tr>
				</table>
			</form>
		</div>
	</body>
</html>
