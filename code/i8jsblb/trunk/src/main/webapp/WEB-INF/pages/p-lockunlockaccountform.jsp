<%@include file="/common/taglibs.jsp"%>

<%@ page import='com.inov8.microbank.common.util.*'%>

<c:choose>
	<c:when test="${!empty status}">
		<c:set var="_status" value="${status}"/>
	</c:when>
	<c:otherwise>
		<c:set var="_status" value="${param.status}"/>
	</c:otherwise>
</c:choose>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="cache-control" content="no-store" />
	<meta http-equiv="cache-control" content="private" />
	<meta http-equiv="cache-control" content="max-age=0, must-revalidate" />
	<meta http-equiv="expires" content="now-1" />
	<meta http-equiv="pragma" content="no-cache" />

	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
	<meta name="title" content="Account Closing"/>

	<script type="text/javascript">

		$acType = $('#acType');

		$acType.change (
				function() {
					$.ajax({
						type: "GET",
						url: "CustomerAccountActionController",
						data: {type: $acType.attr("selectedIndex") },
						success: function(data){
							$("#state").html(data)
						}
					});
				}
		);

		function validateCloseForm()
		{
			var input = document.getElementById("acType").value;
			if(input == '')
				document.getElementById("saveButton").disabled = true;
			else
				document.getElementById("saveButton").disabled = false;
		}
		function backButtonOverrideBody(e)
		{
			if (navigator.appName != "Microsoft Internet Explorer"){
				window.forward();
			}
			else{
				history.forward();
			}

		}



		function setFocus()
		{
			try
			{
				if($('comments')!=null)
				{
					$('comments').focus();
				}
			}catch(e){}
		}
		function validate()
		{
			try
			{
				if($F('comments')=="")
				{
					alert('Comments is a required field');
					$('comments').focus();
					return false;
				}
				else
				{
					if($F('comments').length>250)
					{
						alert('Maximum characters should not be more than 250');
						$('comments').focus();
						return false;
					}
					else
					{
						//if there is illegal special character
						if(!validateFormChar(document.forms[0]))
							return false;

						$('_save').disabled = true;
						return true;
					}
				}
			}catch(e){}
		}

		function refreshParent() {
			window.opener.location.reload();
			window.close();
		}
	</script>
	<style>
		.header {
			background-color: #6699CC;/*308dbb original*/
			color: white;
			font-family:verdana, arial, helvetica, sans-serif;/*verdana, arial, helvetica, sans-serif*/
			font-size: 11px;
			font-weight: bold;
			text-align: center;
			padding-right: 3px;
			padding-left: 3px;
			padding-top: 4px;
			padding-bottom: 4px;
			margin: 0px;
			border-right-style: solid;
			border-right-width: 1px;
			border-color: white;
		}
	</style>
</head>
<body bgcolor="#ffffff" onload="javascript:setFocus();backButtonOverrideBody(event);">
<h3 class="header" id="logHeader">
	<c:if test="${param.isLockUnlock }">Block/Unblock Account</c:if>
	<c:if test="${param.isLockUnlock eq 'false' }">Activate/Deactivate Account</c:if>
</h3>

<c:choose>
	<c:when test="${not empty _status}">
		<c:choose>
			<c:when test="${_status eq 'success'}">

				<div class="">
					<table class="tableRegion">
						<tr class="odd">
							<td class="formText">
									${message }<p>Click the button below to close the window</p>

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
		<div class="eXtremeTable" style="text-align: center; padding-top: 10px;">
			<input type ="button" class="button" value = "Close Window" onclick="javascript:refreshParent();"/>
		</div>
	</c:when>
	<c:otherwise>
		<div class="eXtremeTable">
			<table class="tableRegion" width="100%">
				<tr class="titleRow">
					<td>
						<form id="lockunlockaccountform" method="post" action="p-lockunlockaccountform.html" onsubmit="return validateForm(this)"  >
							<table width="100%"  border="0" cellpadding="0" cellspacing="1">

								<input type="hidden" name="isUpdate" id="isUpdate" value="true"/>

								<tr bgcolor="FBFBFB">
									<td colspan="2" align="center">&nbsp;</td>
								</tr>

								<tr>
									<td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>
										<c:if test="${param.isAgent }">Agent ID</c:if>
										<c:if test="${param.isAgent eq 'false' and param.isHandler eq null }">Customer ID</c:if>
										<c:if test="${param.isHandler }">Handler ID</c:if>:

										&nbsp;</td>
									<td align="left" bgcolor="FBFBFB">

										<input name="mfsId" type="text" size="40" readonly="readonly" class="textBox" tabindex="0" maxlength="50"  value="${param.mfsId}"  />
								</tr>
								<c:if test="${not param.isAgent and not param.isHandler}">
									<tr>
										<td align="right" bgcolor="F3F3F3" class="formText">
											<span style="color:#FF0000">*</span>Action: &nbsp;
										</td>
										<td>
											<select id="usecaseId" name="usecaseId" tabindex="1" onchange="showCustomer()">
												<option value="" selected>--SELECT--</option>
												<c:if test="${param.isLockUnlock}">
													<option value="<%=PortalConstants.BLOCK_CUSTOMER_USECASE_ID%>">BLOCK</option>
													<option value="<%=PortalConstants.UNBLOCK_CUSTOMER_USECASE_ID%>">UN-BLOCK</option>
												</c:if>
												<c:if test="${not param.isLockUnlock}">
													<option value="<%= PortalConstants.REACTIVATE_CUSTOMER_USECASE_ID %>">ACTIVE</option>
													<option value="<%= PortalConstants.DEACTIVATE_CUSTOMER_USECASE_ID %>">DE-ACTIVE</option>
												</c:if>
											</select>
										</td>
									</tr>
									<tr>
										<td align="right" bgcolor="F3F3F3" class="formText">
											<span style="color:#FF0000">*</span>Account Type: &nbsp;
										</td>
										<td>
											<select id="acType" name="acType" cssClass="textBox" tabindex="5" disabled="true" onchange="enableSaveButton()">
												<option value="" selected>---All---</option>
											</select>
										</td>
									</tr>
								</c:if>
								<tr>
									<td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
									<td align="left" bgcolor="FBFBFB">

										<textarea name="comments" cols="50" rows="5" tabindex="1"  style="width:163px; height: 102px" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox" >${status.value}</textarea>
									</td>
								</tr>
								<tr bgcolor="FBFBFB">
									<td height="19"  align="right">
										<c:if test="${param.isAgent or param.isHandler}">
											<input name="_save" type="submit" id="saveButton" class="button" value="Save" tabindex="2" />
										</c:if>
										<c:if test="${not param.isAgent and not param.isHandler}">
											<input name="_save" type="submit" id="saveButton" class="button" value="Save" tabindex="2" disabled/>
										</c:if>
									</td>
									<td height="19" align="left" >
										<input name="cancel" type="button" class="button" value=" Cancel " tabindex="3" onclick="javascript:window.close();"/>
									</td>
								</tr>
								<input type="hidden" name="appUserId" value="${param.appUserId}">
								<input type="hidden" name="mfsId" value="${param.mfsId}">
								<c:if test="${param.isAgent or param.isHandler}">
									<input type="hidden" name="usecaseId" value="${param.usecaseId}">
								</c:if>
								<input type="hidden" name="actionId" value="${param.actionId}">
								<input type="hidden" name="isLockUnlock" value="${param.isLockUnlock}">
								<input type="hidden" name="isAgent" value="${param.isAgent}">
								<input type="hidden" name="isHandler" value="${param.isHandler}">
							</table>
						</form>
					</td>
				</tr>
			</table>
		</div>
	</c:otherwise>
</c:choose>
<script>
	function showCustomer() {
		var xhttp;
		var tag;
		var input = document.getElementById("usecaseId").value;
		if(input == '')
		{
			tag = document.getElementById("acType");
			clearSelectTag(tag);
			return;
		}

		tag = document.getElementById("acType");
		clearSelectTag(tag);
		xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				ajaxFunctionToCheckStatus(this,input);
			}
		};
		xhttp.open("GET", "p_customerAccountActions.html?usecaseId="+input+"&appUserId="+${param.appUserId}, true);
		xhttp.send();
	}
	function ajaxFunctionToCheckStatus(xml,input) {
		var text = xml.responseText;
		var parser = new DOMParser();
		var xmlDoc = parser.parseFromString(text,"text/xml");
		var len = xmlDoc.getElementsByTagName("value").length;
		var txt,tag,option;
		tag = document.getElementById("acType");
		var errorMesage;

		removeOptions(tag);

		txt = xmlDoc.getElementsByTagName("value")[0].childNodes[0].nodeValue;
		if(txt == "NO")
			errorMesage = "Both Accounts are already in ";
		else
			errorMesage = "BLB is already in ";
		var inputVal;
        if(input == <%= PortalConstants.REACTIVATE_CUSTOMER_USECASE_ID%>)
            inputVal = "ACTIVE";
        else if(input == <%= PortalConstants.DEACTIVATE_CUSTOMER_USECASE_ID%>)
            inputVal = "DE-ACTIVE";
        else if(input == <%= PortalConstants.BLOCK_CUSTOMER_USECASE_ID%>)
            inputVal = "BLOCK";
        else if(input == <%= PortalConstants.UNBLOCK_CUSTOMER_USECASE_ID%>)
            inputVal = "UN-BLOCK";

		if(txt == "NO" || txt == "NO-HRA")
		{
			alert(errorMesage + inputVal + " State");
			clearSelectTag(tag);
			return;
		}
		tag.disabled = false;
		for(var i=0;i<len;i++)
		{
			txt = xmlDoc.getElementsByTagName("value")[i].childNodes[0].nodeValue;
			option = document.createElement("option");
			option.text = txt;
			tag.add(option);
		}
	}
	function clearSelectTag(tag)
	{
		document.getElementById("saveButton").disabled = true;
		tag.disabled = true;
		removeOptions(tag);
	}
	function removeOptions(obj) {
		while (obj.options.length) {
			obj.remove(0);
		}
		var option = document.createElement("option");
		option.text = "---SELECT---";
		option.value = "";
		obj.add(option);
	}
	function enableSaveButton()
	{
		var xhttp;
		var tag;
		var input = document.getElementById("acType").value;
		var action = document.getElementById("usecaseId").value;
		if(input == 'BLB')
			document.getElementById("saveButton").disabled = false;
		if(input == '')
		{
			tag = document.getElementById("acType");
			clearSelectTag(tag);
			return;
		}

		tag = document.getElementById("acType");
		/*clearSelectTag(tag);*/
		xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				ajaxFunctionToCheckAccountState(this,input)
			}
		};
		xhttp.open("GET", "p_customerAccountActions.html?account="+input+"&usecaseId="+action+"&appUserId="+${param.appUserId}, true);
		xhttp.send();
		/*var input = document.getElementById("acType").value;
		 if(input == '')
		 {
		 document.getElementById("saveButton").disabled = true;
		 return;
		 }
		 document.getElementById("saveButton").disabled = false;*/
	}
	function ajaxFunctionToCheckAccountState(xml,input)
	{
		var action = document.getElementById("usecaseId").value;
		var acType = document.getElementById("usecaseId").value;
		var text = xml.responseText;
		var parser = new DOMParser();
		var xmlDoc = parser.parseFromString(text,"text/xml");
		var len = xmlDoc.getElementsByTagName("value").length;
		var txt,tag,option;
		tag = document.getElementById("acType");

		/*removeOptions(tag);*/

		txt = xmlDoc.getElementsByTagName("value")[0].childNodes[0].nodeValue;
		/*alert("Text: "+txt);*/
		if(txt == 'YES')
		{
			document.getElementById("saveButton").disabled = true;
			var statusId = xmlDoc.getElementsByTagName("value")[1].childNodes[0].nodeValue;
			/*alert("Status Id: "+statusId);*/
			var errorMessage = displayAccountStatus(statusId);
			alert(errorMessage);
		}
		else
			document.getElementById("saveButton").disabled = false;
	}
	function displayAccountStatus(val)
	{
		var errorMessage;
		if(val == "3")
			errorMessage = "Your Account is in Blocked State.";
		if(val == "1")
			errorMessage = "Your Account is not Active.";

		var tag = document.getElementById("acType");
		clearSelectTag(tag);
		return errorMessage;
	}
</script>
</body>
</html>  	