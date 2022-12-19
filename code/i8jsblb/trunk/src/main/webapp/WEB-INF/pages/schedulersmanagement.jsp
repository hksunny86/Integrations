<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="retrieveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="updateAction"><%=PortalConstants.ACTION_UPDATE %></c:set>
<html>
<head>
	<meta name="decorator" content="decorator">
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">

	<meta name="title" content="Batch Jobs"/>
	<script type="text/javascript" src="${contextPath}/scripts/activatedeactivate.js"></script>
	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
</head>
<body bgcolor="#ffffff">
	<%@include file="/common/ajax.jsp"%>
<c:choose>
	<c:when test="${not empty _status}">
		<c:choose>
			<c:when test="${_status eq 'success'}">

				<div class="">
					<table class="tableRegion">
						<tr class="odd">
							<td class="formText">
									${message }
							</td>
						</tr>
					</table>
				</div>
			</c:when>
			<c:otherwise>
				<div class="eXtremeTable">
					<table class="tableRegion">
						<tr class="odd">
							<td class="formText"><c:out value="${message}" /></td>
						</tr>
					</table>
				</div>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise></c:otherwise>
</c:choose>
    <ec:table filterable="false"
            items="jobs" var="model"
            action="${pageContext.request.contextPath}/schedulersmanagement.html?actionId=${retriveAction}"
            title="">
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
            <ec:exportCsv fileName="Scheduled Jobs.csv" tooltip="Export CSV"/>
        </authz:authorize>
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
            <ec:exportXls fileName="Scheduled Jobs.xls" tooltip="Export Excel"/>
        </authz:authorize>
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
            <ec:exportXlsx fileName="Scheduled Jobs.xlsx" tooltip="Export Excel"/>
        </authz:authorize>
        <ec:row>
            <ec:column property="jobName" title="Job Name" filterable="false" style="width:350px;"/>
            <ec:column property="jobType" title="Type" filterable="false"/>
            <ec:column property="action" title="Action" filterable="false" style="width:80px;">

				<input type="button" class="button" value="Run Now"  onclick="this.disabled=true; return openTransactionDetailWindow('${model.jobName}')"/>

				<%--<input type="button" class="button" value="Run Now" onclick="javascript:window.location.href='${conFtextPath}/debitCardSchedulersController.html?actionId=3'"/>--%>
				<%--<input type="button" class="button" value="Run Now" onclick="javascript:window.location.href='${contextPath}/debitCardFeeSchedulersController.html?actionId=3'"/>--%>
<%----%>
			</ec:column>
        </ec:row>


    </ec:table>
	<script type="text/javascript">
        var isErrorOccured = false;
        function resetProgress()
        {
            if(!isErrorOccured)
            {
                // clear error box
                $('errorMsg').innerHTML = "";
                Element.hide('errorMsg');
                // display success message

                Element.show('successMsg');
            }
            isErrorOccured = false;
        }

        function openTransactionDetailWindow(jobName)
        {
            var popupWidth = 550;
            var popupHeight = 350;
            var popupLeft = (window.screen.width - popupWidth)/2;
            var popupTop = (window.screen.height - popupHeight)/2;
            if (jobName=="Debit Card Import Export Scheduler"){
            var action = "debitCardSchedulersController.html";
			}
			else if (jobName=="Ac Holder Disbursement Scheduler"){
				var action = "acHolderDisbursementSchedulersController.html";
			}
			else {
                var action = "debitCardFeeSchedulersController.html";
            }
            newWindow = window.open( action, "_self" );

            // if(window.focus) newWindow.focus();
            // return false;
        }

	</script>
	<%--<ajax:htmlContent baseUrl="${contextPath}/debitCardSchedulersController.html"--%>
					  <%--eventType="click"--%>
					  <%--source=""--%>
					  <%--target="successMsg"--%>
					  <%--parameters="actionId=3"--%>
					  <%--errorFunction="globalAjaxErrorFunction"--%>
					  <%--preFunction="initProgress"--%>
					  <%--postFunction="resetProgress"--%>
	<%--/>--%>

	<%--<ajax:htmlContent baseUrl="${contextPath}/debitCardFeeSchedulersController.html"--%>
					  <%--eventType="click"--%>
					  <%--source=""--%>
					  <%--target="successMsg"--%>
					  <%--parameters="actionId=3"--%>
					  <%--errorFunction="globalAjaxErrorFunction"--%>
					  <%--preFunction="initProgress"--%>
					  <%--postFunction="resetProgress"--%>
	<%--/>--%>
</body>
</html>
