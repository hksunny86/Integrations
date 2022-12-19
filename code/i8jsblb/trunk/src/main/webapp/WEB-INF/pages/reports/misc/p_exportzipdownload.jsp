<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>



<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<html>
	<head>
	<meta name="decorator" content="decorator">
	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	
   	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<meta name="title" content="Export Zip Download" />
          
	</head>
	<body bgcolor="#ffffff">

	
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		<div class="eXtremeTable">


		<table id="ec_table" border="0" cellspacing="0" cellpadding="0" class="tableRegion" width="100%">
			<thead>
			<tr style="padding: 0px;">
				<td colspan="2">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tbody><tr>
						<td class="statusBar">${totalFiles} result(s) found</td>
					</tr>
				</tbody></table>
				</td>
			</tr>		
		
			<tr>
				<td class="tableHeader" onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'" onmouseout="this.className='tableHeader';this.style.cursor='default'" >Files</td>
				<td class="tableHeader" onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'" onmouseout="this.className='tableHeader';this.style.cursor='default'" >Download Link</td>
			</tr>
			</thead>
			<tbody class="tableBody">
		
				<c:forEach var="filename" items="${files}" varStatus="loop">
					<tr class="even" onmouseover="this.className='highlight'" onmouseout="this.className='odd'" style="text-align:center;">
						<td >
					    	<c:out value="${filename}" />
					    </td>
					    <td>
					    	<a href="${downloadLinks[loop.index]}">Download</a>
					    </td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	</body>
</html>
