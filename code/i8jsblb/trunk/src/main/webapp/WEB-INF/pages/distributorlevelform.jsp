<!--Author: Asad Hayat-->

<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="decorator" content="decorator2">
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
        <script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Agent Level" />

		<script type="text/javascript">
		
	function error(request)
	{
	  alert("An unknown error has occured. Please contact with the administrator for more details");
	}

	function enableDisable()
	{
		if(document.forms[0].ultamateLevelName.value.length<1)
		{
			document.forms[0].managingLevelId.length = 0;
			document.forms[0].managingLevelId.disabled = 'disabled';
			return false;
		}
		else
			return true;
		
	}			
	function loadMangingLevel()
	{
		if( enableDisable() )
		{
			var url = '${contextPath}/distributorcontactformrefdata.html';
			var pars = 'actionType=6&distributorId='+$F('distributorId');
			var myAjax = new Ajax.Request(
				url, 
				{
					method: 'get', 
					parameters: pars,
					onFailure: error,
					onSuccess: handleResponseList
				});
		}	

/*
		var ect = new AjaxJspTag.Select("/i8Microbank/distributorcontactformrefdata.html", {
			parameters: "distributorId={distributorId},actionType=6",
			target: "managingLevelId",
			postFunction: enableDisable,
			parser: new ResponseXmlParser(),
			source: "distributorId", errorFunction: error}
		);
	ect.execute();
*/
	
	}

	function handleResponseList(request)
	{
		var xmlDoc = request.responseXML;
		var items = xmlDoc.getElementsByTagName("item");
		outputList(items);
	}

	function outputList(options) 
	{
	  $('managingLevelId').length = 0;
	  $('managingLevelId').disabled = false;
      for (var i=0; i<options.length; i++) 
      {
        var nameNodes = options[i].getElementsByTagName("name");
        var valueNodes = options[i].getElementsByTagName("value");
        if (nameNodes.length > 0 && valueNodes.length > 0) 
        {
          var name = nameNodes[0].firstChild.nodeValue;
          var value = valueNodes[0].firstChild.nodeValue;
          var newOption = new Option(name, value);
          $('managingLevelId').options[i] = newOption;
        }
      }	
	}

	
	function popupCallback(src,popupName,columnHashMap)
	{
	  if(src=='distributorName')
	  {
	    document.forms.distributorLevelForm.distributorId.value = columnHashMap.get('PK');
	    document.forms.distributorLevelForm.distributorName.value = columnHashMap.get('name');
	  }
	  if(src=='managingLevelName')
	  {
	    document.forms.distributorLevelForm.managingLevelId.value = columnHashMap.get('PK');
	    document.forms.distributorLevelForm.managingLevelName.value = columnHashMap.get('name');
	  }
	}
	

</script>

	</head>
	<body onload="javascript: enableDisable();">
		<%@include file="/common/ajax.jsp"%>

		<spring:bind path="distributorLevelModel.*">
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

	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
	<div id="errorMsg" class ="errorMsg" style="display:none;"></div>

		<table width="100%" border="0" cellpadding="0" cellspacing="1">
				
			<form id="distributorLevelForm" method="post" action="distributorlevelform.html"
				onSubmit="return onFormSubmit(this)">

				<c:if test="${not empty param.distributorLevelId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
					<spring:bind path="distributorLevelModel.distributorId">
						<input type="hidden" name="${status.expression}"
							value="${status.value}" />
					</spring:bind>
				</c:if>
				<c:if test="${empty param.distributorLevelId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="false" />
				</c:if>
				<spring:bind path="distributorLevelModel.distributorLevelId">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="distributorLevelModel.createdBy">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="distributorLevelModel.updatedBy">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="distributorLevelModel.createdOn">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="distributorLevelModel.updatedOn">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="distributorLevelModel.versionNo">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
			<tr bgcolor="FBFBFB">
				<td colspan="2" align="center">
					&nbsp;
				</td>
			</tr>
			<tr bgcolor="#FBFBFB">
				<td width="50%" align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Level:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="distributorLevelModel.name">

						<input type="text" name="${status.expression}" class="textBox"
							maxlength="50" value="${status.value}"
							<c:if test="${not empty param.distributorLevelId}">readonly="readOnly"</c:if> />

					</spring:bind>
				</td>
			</tr>

			<tr>
				<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Agent:&nbsp;
				</td>
				<td width="50%" align="left" bgcolor="FBFBFB">
					<spring:bind path="distributorLevelModel.distributorId">
						<select name="${status.expression}" id="${status.expression}"
							class="textBox"
							<c:if test="${not empty param.distributorLevelId}">disabled='disabled'</c:if>>
							<c:forEach items="${distributorModelList}" var="distributorModel">
								<option value="${distributorModel.distributorId}"
									<c:if test="${status.value == distributorModel.distributorId}"> selected="selected"</c:if>>
									${distributorModel.name}
								</option>
							</c:forEach>
						</select>
					</spring:bind>
				</td>
			</tr>

			<tr>
				<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
					Managing Level:&nbsp;
				</td>
				<td width="50%" align="left" bgcolor="FBFBFB">
					<spring:bind path="distributorLevelModel.managingLevelId">
						<select name="${status.expression}" class="textBox" 
							id="${status.expression}"<c:if test="${empty ultamateLevelId}">disabled='disabled'</c:if>>
							<option name="" value=""></option>
							<c:forEach items="${managingDistributorLevelModelList}"
								var="managingDistributorLevelModel">
								<option
									value="${managingDistributorLevelModel.distributorLevelId}"
									<c:if test="${status.value == managingDistributorLevelModel.distributorLevelId}"> selected="selected"</c:if>>
									${managingDistributorLevelModel.name}
								</option>
							</c:forEach>
						</select>
					</spring:bind>
				</td>
			</tr>

			<tr>
				<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
					Ultimate Managing Level:&nbsp;
				</td>
				<td width="50%" align="left" bgcolor="FBFBFB">
					<spring:bind path="distributorLevelModel.ultimateManagingLevelId">
						<input type="hidden" id="${status.expression}"
							name="${status.expression}" value="${ultamateLevelId}" />
						<%--
                <c:if test="${empty ultamateLevelId}">
	                <input type="text" name="${status.expression}" value="${ultamateLevelId}" class="textBox" readonly="readonly"/>
                </c:if>
                 
                --%>
					</spring:bind>
					
					
						<c:if test="${empty ultamateLevelId}">
						<input type="text" id="ultamateLevelName" name="ultamateLevelName"
							class="textBox" readonly="readonly"  value=""/>
						</c:if>
						
						<c:if test="${not empty ultamateLevelId}">
						<input type="text" id="ultamateLevelName" name="ultamateLevelName"
							class="textBox" readonly="readonly"  value="<c:out value='${ultamateLevelName}' default="Generate this if p is null" />"/>
					</c:if>

					

					<%--<select name="${status.expression}" class="textBox">
                     <option name="" value=""></option>
                     <c:forEach items="${managingUltimateDistributorLevelModelList}" var="managingUltimateDistributorLevelModelList">
                        <c:if test="${ empty managingUltimateDistributorLevelModelList.ultimateManagingLevelId}">
                       <option value="${managingUltimateDistributorLevelModelList.ultimateManagingLevelId}" <c:if test="${status.value == managingUltimateDistributorLevelModelList.ultimateManagingLevelId}"> selected="selected"</c:if>>
                           ${managingUltimateDistributorLevelModelList.ultimateLevelName}
                       </option>
                       </c:if>
                     </c:forEach>
                   </select>
                --%>
				</td>
			</tr>



			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText">
					Description:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB">
					<spring:bind path="distributorLevelModel.description">
						<textarea name="${status.expression}" cols="50" rows="5"
							class="textBox" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)">${status.value}</textarea>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText">
					Comments:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB">
					<spring:bind path="distributorLevelModel.comments">
						<textarea name="${status.expression}" cols="50" rows="5" onkeypress="return maskCommon(this,event)"
							class="textBox" onkeyup="textAreaLengthCounter(this,250);">${status.value}</textarea>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="middle">
					<GenericToolbar:toolbar formName="distributorLevelForm" />
				</td>
			</tr>

<ajax:updateField source="distributorId" eventType="change"
				action="distributorId"
				target="ultimateManagingLevelId,ultamateLevelName"
				postFunction="loadMangingLevel"
				parser="new ResponseXmlParser()"
				baseUrl="${contextPath}/distributorcontactformrefdata.html"
				parameters="distributorId={distributorId},actionType=4" />
			<!--	
			<ajax:select source="distributorId" target="managingLevelId"
			baseUrl="${contextPath}/distributorcontactformrefdata.html"
			preFunction="checkManagingLevelId"
			
			parameters="distributorId={distributorId},actionType=6" 
			errorFunction="error"/>
			-->
			
		<%--
	<ajax:select source="distributorId" target="ultamateLevelName"
			baseUrl="${contextPath}/distributorcontactformrefdata.html"
			parameters="distributorId={distributorId},actionType=4" errorFunction="error"/>
			--%>
			</form>
		</table>


		<script type="text/javascript">
		
			document.forms[0].name.focus();
			highlightFormElements();
			
			
			function onFormSubmit(theForm) 
			{
			
				if (validateDistributorLevelModel(theForm))
				{
					if(validateTextAreaCommonChar(theForm.description))
					{
						alert("Description is incorrect");
				 		return false;
					}
					if (validateTextAreaCommonChar(theForm.comments))
					{
						alert("Comments is incorrect");
						return false;
					}	
				return true;
				}
				return false;
			
			}
</script>

		<v:javascript formName="distributorLevelModel"
			staticJavascript="false" />
		<script type="text/javascript"
			src="<c:url
value="/scripts/validator.jsp"/>"></script>

	</body>
</html>

