

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
<meta name="decorator" content="decorator">
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<meta name="title" content="AllPay Commission Rate" />
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" 
			src="${contextPath}/scripts/date-validation.js"></script>	
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />

<script language="javascript" type="text/javascript">
		Form.focusFirstElement($('allpaycommissionratesform'));
      function error(request)
{
     	alert("An unknown error has occured. Please contact with the administrator for more details");
}
</script>
	</head>

	<body bgcolor="#ffffff">
		<%@include file="/common/ajax.jsp"%>

		<spring:bind path="allpayCommissionRateModel.*">
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

			<form method="post" name = "allpaycommissionratesform"
				action="<c:url value="/AllpayCommissionRatesForm.html"/>"
				id="allpayCommissionForm">



				<c:if test="${not empty param.allpayCommissionRateId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />

				</c:if>

				<spring:bind path="allpayCommissionRateModel.allpayCommissionRateId">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
				<%----%>
				<spring:bind path="allpayCommissionRateModel.versionNo">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
				<c:if test="${not empty param.allpayCommissionRateId}">
				<%----%>
				           <spring:bind path="allpayCommissionRateModel.productId">
				             <input type="hidden" name="${status.expression}" value="${status.value}"/>
				           </spring:bind>
				<%----%>
				           <spring:bind path="allpayCommissionRateModel.distributorId">
				             <input type="hidden" name="${status.expression}" value="${status.value}"/>
				           </spring:bind>
				<%----%>
				           <spring:bind path="allpayCommissionRateModel.retailerId">
				             <input type="hidden" name="${status.expression}" value="${status.value}"/>
				           </spring:bind>
				<%----%>
				           <spring:bind path="allpayCommissionRateModel.allpayCommissionReasonId">
				             <input type="hidden" name="${status.expression}" value="${status.value}"/>
				           </spring:bind>
				<%----%>
				</c:if>
				<%--           <tr bgcolor="FBFBFB">--%>
				<%--             <td colspan="2" align="center">&nbsp;</td>--%>
				<%--           </tr>--%>
				<spring:bind path="allpayCommissionRateModel.nationalDistributorId">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText"><span style="color: rgb(255, 0, 0);">*</span>
					Product:&nbsp;
				</td>
				<td align="left">
					<%--  change           --%>
					<spring:bind path="allpayCommissionRateModel.productId">
						<c:if test="${empty param.allpayCommissionRateId}">
							<select name="${status.expression}" class="textBox">
								<%--                     <option value=""></option>--%>
								<c:forEach items="${productModelList}" var="productModelList">
									<option value="${productModelList.productId}"
										<c:if test="${status.value == productModelList.productId}">selected="selected"</c:if>>
										${productModelList.name}
									</option>
								</c:forEach>
							</select>
						</c:if>

						<c:if test="${not empty param.allpayCommissionRateId}">

							<select name="${status.expression}" class="textBox"
								disabled="disabled">
								<%--                     <option value=""></option>--%>
								<c:forEach items="${productModelList}" var="productModelList">
									<option value="${productModelList.productId}"
										<c:if test="${status.value == productModelList.productId}">selected="selected"</c:if>>
										${productModelList.name}
									</option>
								</c:forEach>
							</select>

						</c:if>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText">
					<span style="color:#FF0000">*</span>National Agent :&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB">
					<spring:bind
						path="allpayCommissionRateModel.nationalDistributorName">
						<input type="text" name="${status.expression} "
							value="${status.value}" class="textBox" readonly="readonly" />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000"></span>Distributor:&nbsp;
				</td>
				<td align="left">
					<%--    Change         --%>
					<spring:bind path="allpayCommissionRateModel.distributorId">
						<c:if test="${empty param.allpayCommissionRateId}">
							<select name="${status.expression}" class="textBox">
								<option value=""></option>
								<c:forEach items="${distributorModelList}"
									var="distributorModelList">
									<option value="${distributorModelList.distributorId}"
										<c:if test="${status.value == Agent ModelList.distributorId}">selected="selected"</c:if>>
										${distributorModelList.name}
									</option>
								</c:forEach>
							</select>
						</c:if>
						<c:if test="${not empty param.allpayCommissionRateId}">

							<select name="${status.expression}" class="textBox"
								disabled="disabled">
								<option value=""></option>
								<c:forEach items="${distributorModelList}"
									var="distributorModelList">
									<option value="${distributorModelList.distributorId}"
										<c:if test="${status.value == Agent ModelList.distributorId}">selected="selected"</c:if>>
										${distributorModelList.name}
									</option>
								</c:forEach>
							</select>
						</c:if>
					</spring:bind>
				</td>
			</tr>
			<%-- --------------------------------------------------------------------------------------------------------------------------          --%>
			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000"></span>Retailer:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="allpayCommissionRateModel.retailerId">
						<c:if test="${empty param.allpayCommissionRateId}">
							<select name="${status.expression}" class="textBox">
							<option value=""></option>
								<c:forEach items="${retailerModelList}" var="retailerModelList">
									<option value="${retailerModelList.retailerId}"
										<c:if test="${status.value == retailerModelList.retailerId}">selected="selected"</c:if>>
										${retailerModelList.name}
									</option>
								</c:forEach>
							</select>
						</c:if>
						<c:if test="${not empty param.allpayCommissionRateId}">

							<select name="${status.expression}" class="textBox"
								disabled="disabled">
								<option value=""></option>
								<c:forEach items="${retailerModelList}" var="retailerModelList">
									<option value="${retailerModelList.retailerId}"
										<c:if test="${status.value == retailerModelList.retailerId}">selected="selected"</c:if>>
										${retailerModelList.name}
									</option>
								</c:forEach>
							</select>
						</c:if>
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Commission Reason:
					<br />
				</td>
				<td align="left">
					<spring:bind
						path="allpayCommissionRateModel.allpayCommissionReasonId">
						<c:if test="${empty param.allpayCommissionRateId}">
							<select name="${status.expression}" class="textBox">
								<c:forEach items="${allpayCommissionReasonModelList}"
									var="allpayCommissionReasonModelList">
									<option
										value="${allpayCommissionReasonModelList.allpayCommissionReasonId}"
										<c:if test="${status.value == allpayCommissionReasonModelList.allpayCommissionReasonId}">selected="selected"</c:if>>
										${allpayCommissionReasonModelList.name}
									</option>
								</c:forEach>
							</select>
						</c:if>
						<c:if test="${not empty param.allpayCommissionRateId}">
							<select name="${status.expression}" class="textBox"
								disabled="disabled">
								<c:forEach items="${allpayCommissionReasonModelList}"
									var="allpayCommissionReasonModelList">
									<option
										value="${allpayCommissionReasonModelList.allpayCommissionReasonId}"
										<c:if test="${status.value == allpayCommissionReasonModelList.allpayCommissionReasonId}">selected="selected"</c:if>>
										${allpayCommissionReasonModelList.name}
									</option>
								</c:forEach>
							</select>
						</c:if>

					</spring:bind>
				</td>
			</tr>
			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText">
					<span style="color:#FF0000">*</span>From Date:&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB">
					<spring:bind path="allpayCommissionRateModel.fromDate">
						<input type="text" name="${status.expression}"
							value="${status.value}" maxlength="11"
							onkeypress="return maskAlphaWithSp(this,event)" class="textBox" readonly="readonly"/>
					</spring:bind>
					<img id="calFromDate" tabindex="4" name="popcal" align="top"
						style="cursor:pointer"
						src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
					<img id="calFromDateImg" tabindex="5" name="popcal"
						title="Clear Date" onclick="javascript:$('fromDate').value=''"
						align="middle" style="cursor:pointer"
						src="${pageContext.request.contextPath}/images/refresh.png"
						border="0" />
				</td>
			</tr>
			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText">
					To Date:&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB">
					<spring:bind path="allpayCommissionRateModel.toDate">
						<input type="text" name="${status.expression}"
							value="${status.value}" class="textBox"
							onkeypress="return maskNumber(this,event)" maxlength="11" readonly="readonly"/>
					</spring:bind>
					<img id="calToDate" tabindex="4" name="popcal" align="top"
						style="cursor:pointer"
						src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
					<img id="calToDateImg" tabindex="5" name="popcal"
						title="Clear Date" onclick="javascript:$('toDate').value=''"
						align="middle" style="cursor:pointer"
						src="${pageContext.request.contextPath}/images/refresh.png"
						border="0" />
				</td>
			</tr>

			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText"><span style="color: rgb(255, 0, 0);">*</span>
					Rate For National Agent :&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB">
					<spring:bind
						path="allpayCommissionRateModel.nationalDistributorRate">
						<input type="text" name="${status.expression}"
							value="${status.value}" class="textBox"
							onkeypress="return maskNumber(this,event)" maxlength="6" />%
               </spring:bind>
				</td>
			</tr>

			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText"><span style="color: rgb(255, 0, 0);">*</span>
					Rate For Agent :&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB">
					<spring:bind path="allpayCommissionRateModel.distributorRate">
						<input type="text" name="${status.expression}"
						onkeypress="return maskNumber(this,event)" maxlength="6"
							value="${status.value}" class="textBox"  />%
               </spring:bind>
				</td>
			</tr>
			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000"></span><span style="color: rgb(255, 0, 0);">*</span>Rate For Retailer:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB">
					<spring:bind path="allpayCommissionRateModel.retailerRate">
						<input name="${status.expression}" type="text" class="textBox"
							onkeypress="return maskNumber(this,event)" maxlength="6"
							value="${status.value}" />%
               </spring:bind>
				</td>
			</tr>



			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText">
					Description:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB">
					<spring:bind path="allpayCommissionRateModel.description">
						<textarea name="${status.expression}" cols="50"
							onkeypress="return maskCommon(this,event)"
							onkeyup="textAreaLengthCounter(this,250);" rows="5"
							class="textBox" type="_moz">${status.value}</textarea>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText">
					Comments:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB">
					<spring:bind path="allpayCommissionRateModel.comments">
						<textarea name="${status.expression}" cols="50"
							onkeypress="return maskCommon(this,event)"
							onkeyup="textAreaLengthCounter(this,250);" rows="5"
							class="textBox" type="_moz">${status.value}</textarea>
					</spring:bind>
				</td>
			</tr>

			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText">
					Active:&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
					<spring:bind path="allpayCommissionRateModel.active">
						<input name="${status.expression}" type="checkbox" value="true"
							<c:if test="${status.value==true}">checked="checked"</c:if>
							<c:if test="${empty param.allpayCommissionRateId}">checked="checked"</c:if> />
					</spring:bind>
				</td>
			</tr>


			<tr>
				<%--             <td colspan="2" align="center"><GenericToolbar:toolbar formName="allpayCommissionForm"/></td>--%>
				<td colspan="2" align="center">
					<input type="submit" value="Save" class="button"
						onclick="return validateForm()"/>
					
					<input type="reset" value="Cancel" class="button"/>
					
				</td>
			</tr>
			</form>
			<%--        <c:forEach items="${allpayCommissionReasonModelList}" var="allpayCommissionReasonModelList">--%>
			<%--        					<c:out value="${allpayCommissionReasonModelList.allpayCommissionReasonId}"></c:out>--%>
			<%--        					<c:out value="${allpayCommissionReasonModelList.name}"></c:out>--%>
			<%--                        </c:forEach>--%>

		</table>
		<ajax:select source="distributorId" target="retailerId"
			baseUrl="${contextPath}/commissionRateFormRefDataController.html"
			parameters="distributorId={distributorId},actionType=3"
			errorFunction="error" />
		<script language="javascript" type="text/javascript">
   
      //Form.focusFirstElement($('allpayCommissionForm'));
      highlightFormElements();
 
function validateForm(){
//alert ("in the function");
//return true;
	var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	var _fDate = document.forms.allpaycommissionratesform.fromDate.value;
	var _tDate = document.forms.allpaycommissionratesform.toDate.value;
	var startlbl = "From Date";
	var endlbl   = "To Date";

	if (document.forms.allpaycommissionratesform.productId.value==''){
  			alert("Product is a required field");
  			document.forms.allpaycommissionratesform.productId.focus();
  					   
		   return false;
	}
	if (document.forms.allpaycommissionratesform.allpayCommissionReasonId.value==''){
  			alert("Allpay Commission Reason is a required field");		   
  			document.forms.allpaycommissionratesform.allpayCommissionReasonId.focus();
		   return false;
	}
	if (document.forms.allpaycommissionratesform.fromDate.value==''){
  			alert("From Date is a required field");		   
  			document.forms.allpaycommissionratesform.fromDate.focus();
		   return false;
	}
	if (! validateDateRange(_fDate,_tDate,startlbl,endlbl,currentDate)){
		return false;
	}
	
	if (document.forms.allpaycommissionratesform.nationalDistributorRate.value==''){
  			alert("National Agent Rate is a required field");
  			document.forms.allpaycommissionratesform.nationalDistributorRate.focus();  					   
		   return false;
	}
	if (document.forms.allpaycommissionratesform.nationalDistributorRate.value > 100){
  			alert("National Agent Rate percentage can not be more than 100%");
  			document.forms.allpaycommissionratesform.nationalDistributorRate.focus();  					   
		   return false;
	}
	if (document.forms.allpaycommissionratesform.distributorRate.value==''){
  			alert("Agent Rate is a required field");		   
  			document.forms.allpaycommissionratesform.distributorRate.focus();
		   return false;
	}
	if (document.forms.allpaycommissionratesform.distributorRate.value > 100){
		alert("Agent Rate percentage can not be more than 100%");		   
  			document.forms.allpaycommissionratesform.distributorRate.focus();
		   return false;
	}
	if (document.forms.allpaycommissionratesform.retailerRate.value==''){
  			alert("Retailer Rate is a required field");		   
  			document.forms.allpaycommissionratesform.retailerRate.focus();
		   return false;
	}
	if (document.forms.allpaycommissionratesform.retailerRate.value > 100){
  			alert("Retailer Rate percentage can not be more than 100%");		   
  			document.forms.allpaycommissionratesform.retailerRate.focus();
		   return false;
	}
	var nationalDist = eval(document.forms.allpaycommissionratesform.nationalDistributorRate.value);
	var dist = eval(document.forms.allpaycommissionratesform.distributorRate.value);
	var ret = eval(document.forms.allpaycommissionratesform.retailerRate.value);
	//alert(nationalDist + dist + ret);
	if ((nationalDist + dist + ret) > 100 ){
	
		alert("Total Rate percentage can not be more than 100%");
		return false;		
	}
	if ((nationalDist + dist + ret) <= 0 ){
		alert("Total Rate percentage can not be zero");
		return false;		
	}
	
}
      		Calendar.setup(
      		{
		       inputField  : "fromDate", // id of the input field
			   button      : "calFromDate",    // id of the button
		    }
      		);
			Calendar.setup(
		    {
		      inputField  : "toDate", // id of the input field
		      button      : "calToDate",    // id of the button
		  	  isEndDate: true
		    }
		    );
      

      </script>


		<script type="text/javascript"
			src="<c:url value="/scripts/validator.jsp"/>"></script>
	</body>
</html>
