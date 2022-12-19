
<!--Author: Rizwan-ur-Rehman-->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
<meta name="decorator" content="decorator2">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Application Version" />
	</head>

	<body bgcolor="#ffffff">

		<spring:bind path="appVersionModel.*">
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


		<table width="100%" border="0" cellpadding="0" cellspacing="1">

			<form method="post"
				action="<c:url value="/applicationversionform.html"/>"
				id="applicationVersionForm" onsubmit="return onFormSubmit(this)">

				<c:if test="${not empty param.appVersionId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
				</c:if>

				<spring:bind path="appVersionModel.appVersionId">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="appVersionModel.versionNo">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="appVersionModel.createdBy">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="appVersionModel.updatedBy">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="appVersionModel.createdOn">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="appVersionModel.updatedOn">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
			<tr bgcolor="FBFBFB">
				<td colspan="2" align="center">
					&nbsp;
				</td>
			</tr>
			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText">
					<span style="color:#FF0000">*</span>Application Name:&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB">
					<spring:bind path="appVersionModel.deviceTypeId">
                   <select name="${status.expression}" class="textBox">
                     
                     <c:forEach items="${deviceTypeModelList}" var="deviceTypeModelList">
                       <option value="${deviceTypeModelList.deviceTypeId}" <c:if test="${status.value == deviceTypeModelList.deviceTypeId}">selected="selected"</c:if>>
                       ${deviceTypeModelList.name}
                       </option>
                     </c:forEach>
                   </select>
                   </spring:bind>
				</td>	
			</tr>

			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText">
					<span style="color:#FF0000">*</span>Application Version:&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB">
					<spring:bind path="appVersionModel.appVersionNumber">
						<c:if test="${appVersionModel.appVersionId!=null}">
							<input type="text" name="${status.expression}"
								value="${status.value}" class="textBox" readonly />
						</c:if>
						<c:if test="${appVersionModel.appVersionId==null}">
							<input type="text" name="${status.expression}"
								value="${status.value}" class="textBox" />
						</c:if>
					</spring:bind>
				</td>
			</tr>

			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText">
					<span style="color:#FF0000">*</span>Release Date:&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB">
					<spring:bind path="appVersionModel.releaseDate">
						<c:if test="${appVersionModel.appVersionId!=null}">
							<input type="text" name="${status.expression}"
								value="${status.value}" id="startDate" class="textBox"
								readonly="readonly" />&nbsp;
                <img id="sDate" tabindex="4" name="popcal" align="top"
								style="cursor:pointer"
								src="${pageContext.request.contextPath}/images/cal.gif"
								border="0" />
							<img id="sDate" tabindex="5" title="Clear Date" name="popcal"
								onclick="javascript:$('startDate').value=''" align="middle"
								style="cursor:pointer"
								src="${pageContext.request.contextPath}/images/refresh.png"
								border="0" />
						</c:if>
						<c:if test="${appVersionModel.appVersionId==null}">
							<input type="text" name="${status.expression}"
								value="${status.value}" id="startDate" class="textBox"
								readonly="readonly" />&nbsp;
                   <img id="sDate" tabindex="4" name="popcal"
								align="top" style="cursor:pointer"
								src="${pageContext.request.contextPath}/images/cal.gif"
								border="0" />
							<img id="sDate" tabindex="5" title="Clear Date" name="popcal"
								onclick="javascript:$('startDate').value=''" align="middle"
								style="cursor:pointer"
								src="${pageContext.request.contextPath}/images/refresh.png"
								border="0" />
						</c:if>
					</spring:bind>
				</td>
			</tr>

			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText">
					From Compatible Version:&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB">
					<spring:bind path="appVersionModel.fromCompatibleVersion">
						<c:if test="${appVersionModel.appVersionId!=null}">
							<input id="fCompatibleVersion" type="text"
								name="${status.expression}" value="${status.value}"
								class="textBox" />
						</c:if>
						<c:if test="${appVersionModel.appVersionId==null}">
							<input id="fCompatibleVersion" type="text"
								name="${status.expression}" value="${status.value}"
								class="textBox" />
						</c:if>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText">
					To Compatible version:&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB">
					<spring:bind path="appVersionModel.toCompatibleVersion">
						<c:if test="${appVersionModel.appVersionId!=null}">
							<input id="tCompatibleVersion" type="text"
								name="${status.expression}" value="${status.value}"
								class="textBox" />
						</c:if>
						<c:if test="${appVersionModel.appVersionId==null}">
							<input id="tCompatibleVersion" type="text"
								name="${status.expression}" value="${status.value}"
								class="textBox" />
						</c:if>
					</spring:bind>
				</td>
			</tr>


			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText">
					Black Listed:&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
					<spring:bind path="appVersionModel.blackListed">
						<input name="${status.expression}" type="checkbox" value="true"
							<c:if test="${status.value==true}">checked="checked"</c:if>
<%--							<c:if test="${empty param.appVersionId}">checked="checked"</c:if> --%>
							/>
					</spring:bind>
				</td>
			</tr>

			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText">
					Active:&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
					<spring:bind path="appVersionModel.active">
						<input name="${status.expression}" type="checkbox" value="true"
							<c:if test="${status.value==true}">checked="checked"</c:if>
							<c:if test="${empty param.appVersionId}">checked="checked"</c:if> />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td colspan="2" align="center">
					<GenericToolbar:toolbar formName="applicationVersionForm" />
				</td>
			</tr>
			</form>
		</table>
		<script language="javascript" type="text/javascript">
      
    function isValidApplicaitonVersionNo(ipaddr) {
   var re = /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/;
   if (re.test(ipaddr)) {
     
      return true;
   } else {
      return false;
   }
}
      function submitForm()
      {
		if (confirm('Are you sure you want to save this record?')==true)
        {
         document.applicationVersionForm.submit();
        }
        else
        {
          return false;
        }
      }

      Form.focusFirstElement($('applicationVersionForm'));
      highlightFormElements();

function ZeroCheck(value){
	if (value != ''){
		value = value * 1 ;
		//alert (value);
		//return false;//debugging
		if (value == 0){			
			return true;
		}else{
			return false;
		}
	}

}

      function onFormSubmit(theForm)
      {
        
        /*if ( ! isValidApplicaitonVersionNo(theForm.fCompatibleVersion.value))
        {
        alert('Invalid from compatiable version');
        theForm.fCompatibleVersion.focus();
        return false;
        }
        
        if (! isValidApplicaitonVersionNo(theForm.tCompatibleVersion.value)) 
        {
        alert('Invalid to compatiable version');
        theForm.tCompatibleVersion.focus();
        return false;
        
        }
        */
        
        
		
		
        if  (validateAppVersionModel(theForm))
        
        {
        	if (ZeroCheck (document.getElementById("tCompatibleVersion").value)){
        		alert('To Compatible Version is Incorrect');
			return false;
        	}
        	if (ZeroCheck (document.getElementById("fCompatibleVersion").value)){
        		alert('From Compatible Version is Incorrect');
			return false;
        	}
        	if (ZeroCheck (document.getElementById("appVersionNumber").value)){
        		alert('Application Version number is Incorrect');
			return false;
        	}
        	
			        	
        	if ( (document.getElementById("tCompatibleVersion").value * 1) > ( document.getElementById("fCompatibleVersion").value * 1) )
			{
			alert('To Compatible Version should be less than From Compatible Version');
			return false;
			}
			if ( ( (document.getElementById("tCompatibleVersion").value * 1) > (document.getElementById("appVersionNumber").value * 1)  )  ||  ( document.getElementById("fCompatibleVersion").value * 1) > (document.getElementById("appVersionNumber").value * 1)  )
			{
			alert('To Compatible Version or From Compatible Version can not be greater than Application Version');
			return false;
			}
			
			return true;
        }   	
      }
      
      Calendar.setup(
      		{
		       inputField  : "startDate", // id of the input field
		       ifFormat    : "%d/%m/%Y", // the date format
		       button      : "sDate"    // id of the button
		    }
      		);
      
      
      </script>

		<v:javascript formName="appVersionModel" staticJavascript="false" />
		<script type="text/javascript"
			src="<c:url value="/scripts/validator.jsp"/>"></script>
	</body>
</html>
