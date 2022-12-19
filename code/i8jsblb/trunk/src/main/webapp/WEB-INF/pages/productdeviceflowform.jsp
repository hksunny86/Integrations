<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: -->
<%@include file="/common/taglibs.jsp"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="decorator" content="decorator2">
		<meta name="title" content="Product Device Flow" />
		
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
	</head>
	<body>
        <%@include file="/common/ajax.jsp"%>
        <script language="javascript" type="text/javascript">
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
        </script>
		<spring:bind path="productDeviceFlowModel.*">
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


		<form id="productDeviceFlowForm" method="post" action="productdeviceflowform.html"
			onsubmit="return onFormSubmit(this);">
			<table width="100%" border="0" cellpadding="0" cellspacing="1">
				<%--
				<input type="hidden" name="isUpdate" id="isUpdate" value="false"/>
				--%>
				<c:if test="${not empty param.productdeviceflowid}">
                     <input type="hidden"  name="isUpdate" id="isUpdate" value="true"/>
                     <spring:bind path="productDeviceFlowModel.productDeviceFlowId" >
                         <input type="hidden" name="${status.expression}" value="${status.value}"/>
                     </spring:bind>
                     
                     <spring:bind path="productDeviceFlowModel.deviceTypeId" >
                         <input type="hidden" name="${status.expression}" value="${status.value}"/>
                     </spring:bind>
                     <spring:bind path="productDeviceFlowModel.productId" >
                         <input type="hidden" name="${status.expression}" value="${status.value}"/>
                     </spring:bind>
                     
                </c:if>
                
                <tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Supplier:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">
						<select name="supplierId" class="textBox" id="supplierId" <c:if test="${not empty param.productdeviceflowid}">disabled='disabled' </c:if>>
							<c:forEach items="${supplierModelList}" var="supplierModelList">
								<option value="${supplierModelList.supplierId}"
								       <c:if test="${supplierModelList.supplierId == productDeviceFlowModel.productIdProductModel.supplierIdSupplierModel.supplierId}">selected="selected"</c:if>>
										${supplierModelList.name}
								</option>
							</c:forEach>
						</select>
					</td>
				</tr>
                
				<tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Product:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">
						<spring:bind path="productDeviceFlowModel.productId">
								<select name="${status.expression}" class="textBox" id="${status.expression}" <c:if test="${not empty param.productdeviceflowid}">disabled='disabled' </c:if>>
							        <c:if test="${not empty productModelList}">
								       <c:forEach items="${productModelList}" var="productModelList">
									      <option value="${productModelList.productId}"
										     <c:if test="${status.value == productModelList.productId}">selected="selected"</c:if>>
										        ${productModelList.name}
									      </option>
								       </c:forEach>
								    </c:if>	
							    </select>
						</spring:bind>
					</td>
				</tr>
<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Device Type:&nbsp;
					</td>
					<td>
						<spring:bind path="productDeviceFlowModel.deviceTypeId">							
							<select name="${status.expression}" class="textBox" id="${status.expression}" <c:if test="${not empty param.productdeviceflowid}">disabled='disabled' </c:if>>
						        <c:if test="${not empty deviceTypeModelList}">
							       <c:forEach items="${deviceTypeModelList}" var="deviceTypeModel">
								      <option value="${deviceTypeModel.deviceTypeId}"
									     <c:if test="${status.value == deviceTypeModel.deviceTypeId}">selected="selected"</c:if>>
									        ${deviceTypeModel.name}
								      </option>
							       </c:forEach>
							    </c:if>	
							</select>
						</spring:bind>
					</td>
				</tr>
                
<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>MWallet Service Flow:&nbsp;
					</td>
					<td>
						<spring:bind path="productDeviceFlowModel.deviceFlowId">							
							<select name="${status.expression}" class="textBox" id="${status.expression}">
						        <c:if test="${not empty deviceFlowModelList}">
							       <c:forEach items="${deviceFlowModelList}" var="deviceFlowModel">
								      <option value="${deviceFlowModel.deviceFlowId}"
									     <c:if test="${status.value == deviceFlowModel.deviceFlowId}">selected="selected"</c:if>>
									        ${deviceFlowModel.name}
								      </option>
							       </c:forEach>
							    </c:if>	
							</select>
						</spring:bind>
					</td>
				</tr>
				
				
				
				
	            <tr bgcolor="FBFBFB">
					<td height="19" colspan="2" align="center"></td>
				</tr>
				
				<tr bgcolor="FBFBFB">
					<td colspan="2" align="center">
					
		                <GenericToolbar:toolbar formName="productDeviceFlowForm" />
					
					</td>
				</tr>
	 
			</table>
					<ajax:select source="supplierId" target="productId"
			baseUrl="${contextPath}/shipmentFormRefDataController.html"
			parameters="supplierId={supplierId},rType=1" errorFunction="error" />
		</form>


<script type="text/javascript">
if (document.forms[0].isUpdate!=null)
{
document.forms[0].deviceFlowId.focus();
}
else
{
  document.forms[0].supplierId.focus();
 } 
  highlightFormElements();
  
      
  function onFormSubmit(theForm) {
    return validateProductDeviceFlowModel(theForm);
  }
</script>
 
		<v:javascript formName="productDeviceFlowModel"
			staticJavascript="false" />
		<script type="text/javascript"
			src="<c:url value="/scripts/validator.jsp"/>"></script>


	</body>
</html>
