<!--Author: omar-->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
	<style type="text/css">
		.wrap {
		   width:80%;
		   margin:0 auto;
		}
		.left_col {
		   float:left;
		   width:48%;
		   margin:5px;
		}
		.right_col {
		   float:right;
		   width:48%;
		   margin:5px;
		}
		.feild_container{
			float:left;
		   	margin:5px;
		   	clear: both;
		}
	</style>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/prototype.js"></script>

		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Complaint Subcategory Details" />
		<%@include file="/common/ajax.jsp"%>
	 <script type="text/javascript">

	       function closeChild()
	       {
	          try
	              {
	              if(childWindow != undefined)
	               {
	                   childWindow.close();
	                   childWindow=undefined;
	               }
			      }catch(e){}
	      }
	</script>
	</head>

	<body bgcolor="#ffffff" onunload="javascript:closeChild();">
		<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<div id="errorMsg" class ="errorMsg" style="display:none;"></div>
		<c:if test="${not empty sessionScope.ajaxMessageToDisplay}" >
			<div id="ajaxMsgDiv" class ="infoMsg">
				<c:out value="${sessionScope.ajaxMessageToDisplay}" /><c:remove var="ajaxMessageToDisplay" scope="session" />
			</div>
		</c:if>
		
		<spring:bind path="complaintSubcategoryViewModel.*">
			<c:if test="${not empty status.errorMessages}">
				<div class="errorMsg">
					<c:forEach var="error" items="${status.errorMessages}">
						<c:out value="${error}" escapeXml="false" />
						<br />
					</c:forEach>
				</div>
			</c:if>
		</spring:bind>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
<table width="940px">
		<html:form name="complaintSubcategoryForm" commandName="complaintSubcategoryViewModel" method="POST" action="p_complaintsubcategoryform.html">
		<input type="hidden" name="complaintSubcategoryId" value="${param.subcategoryId}">
			<tr>
				<td class="formText" align="right">
					Complaint Category:
				</td>
				<td align="left">
					${complaintSubcategoryViewModel.complaintCategoryName}
				</td>
				<td class="formText" align="right" width="20%">
					Complaint Nature Name:
				</td>
				<td align="left">
					${complaintSubcategoryViewModel.complaintSubcategoryName}
				</td>
			</tr>
			<tr>
				<td class="formText" align="right">
					Level 0 Assignee:
				</td>
				<td align="left">
					${complaintSubcategoryViewModel.level0AssigneeName}
				</td>
				<td class="formText" align="right" width="20%">
					Level 0 TAT:
				</td>
				<td align="left">
					${complaintSubcategoryViewModel.level0AssigneeTat}
				</td>
			</tr>
			<tr>
				<td class="formText" align="right">
					Level 1 Assignee:
				</td>
				<td align="left">
					${complaintSubcategoryViewModel.level1AssigneeName}
				</td>
				<td class="formText" align="right" width="20%">
					Level 1 TAT:
				</td>
				<td align="left">
					${complaintSubcategoryViewModel.level1AssigneeTat}
				</td>
			</tr>
			<tr>
				<td class="formText" align="right">
					Level 2 Assignee:
				</td>
				<td align="left">
					${complaintSubcategoryViewModel.level2AssigneeName}
				</td>
				<td class="formText" align="right" width="20%">
					Level 2 TAT:
				</td>
				<td align="left">
					${complaintSubcategoryViewModel.level2AssigneeTat}
				</td>
			</tr>
			<tr>
				<td class="formText" align="right">
					Level 3 Assignee:
				</td>
				<td align="left">
					${complaintSubcategoryViewModel.level3AssigneeName}
				</td>
				<td class="formText" align="right" width="20%">
					Level 3 TAT:
				</td>
				<td align="left">
					${complaintSubcategoryViewModel.level3AssigneeTat}
				</td>
			</tr>
			<tr>
				<td class="formText" align="right">
					Description:
				</td>
				<td align="left">
					${complaintSubcategoryViewModel.description}
				</td>
				<td class="formText" align="right" width="20%">
					Active:
				</td>
				<td align="left">
					${complaintSubcategoryViewModel.isActive}
				</td>
			</tr>
	</html:form>
	</table>
			
			

	</body>

</html>




