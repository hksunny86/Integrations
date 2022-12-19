<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--Author: Naseer Ullah -->

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Transaction Reversal Detail"/>
		<link rel="stylesheet" href="${contextPath}/styles/style.css" type="text/css"/>
      	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css"/>
   </head>
   <body bgcolor="#ffffff">
   <div class="eXtremeTable">
   		<table class="tableRegion" width="100%">
	   		<tr>
				<td class="titleRow" style="width: 150px; font-weight: bold">Transaction ID</td>
		   		<td> 
		   			<c:out value="${requestScope.transactionCode}"/>
				</td>
			</tr>
		</table>
		<c:if test="${not empty transactionReversalVo}">
			<table class="tableRegion" style="padding-top: 5px; padding-bottom: 5px" width="100%">
				<tr>
					<td class="tableHeader" style="width:125px;">Date</td>
   					<td class="tableHeader" style="text-align: center; width:125px;">Status</td>
   					<td class="tableHeader" >Reversed By</td>
   					<td class="tableHeader" >Comments</td>
				</tr>
				<tr class="odd" onmouseout="this.className='odd'" onmouseover="this.className='highlight'">
					<td>
						<c:out value="${transactionReversalVo.updatedOn}"></c:out>&nbsp;
					</td>
					<td>
						<%=SupplierProcessingStatusConstants.REVERSED_NAME%>&nbsp;
					</td>
					<td>
						<c:out value="${transactionReversalVo.updatedBy}"></c:out>&nbsp;
					</td>
					<td>
						<c:out value="${transactionReversalVo.comments}"></c:out>&nbsp;
					</td>
				</tr>							
   			</table>
   		</c:if>
   		<table width="100%">
	   		<tr align="center">
	   			<td align="center" style="padding-top: 10px">
				   	<input type ="button" class="popbutton" value ="Close Window" onclick = "window.close();"/>
	   			</td>
	   		</tr>
   		</table>
   		</div>
	</body>
</html>