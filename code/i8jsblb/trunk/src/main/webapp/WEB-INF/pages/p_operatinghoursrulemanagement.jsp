<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils'%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    
	<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
	<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<%@include file="/common/ajax.jsp"%>
	<meta name="title" content="Operating Hours Rules/ Agent Location Verification" />
    <script language="javascript" type="text/javascript">
    var jq=$.noConflict();
    var serverDate ="<%=PortalDateUtils.getServerDate()%>";
      function error(request)
      {
      	alert("An unknown error has occured. Please contact with the administrator for more details");
      }
     
    </script>
	<%
		String createPermission = PortalConstants.MNG_OPERATING_HOURS_RULES_CREATE;
		/*createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.ADMIN_GP_CREATE;*/

		String updatePermission =	PortalConstants.MNG_OPERATING_HOURS_RULES_UPDATE;
		/*updatePermission +=	"," + PortalConstants.MNG_OPERATING_HOURS_RULES_UPDATE;
		updatePermission +=	"," + PortalConstants.ADMIN_GP_UPDATE;*/

 	%>

	</head>
	<body bgcolor="#ffffff" onunload="javascript:closeChild();">

		<div id="rsp" class="ajaxMsg"></div>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
		
			
			<html:form name='extendedOperatingHoursRuleModelForm' commandName="extendedOperatingHoursRuleModel" method="post"
				action="p_operatinghoursrulemanagement.html" onsubmit="return validateForm(this)" >
				<table width="770px" border="0">
				<authz:authorize ifAnyGranted="<%=createPermission %>">
				<tr>
					<td colspan="4">
						<div align="left"><a href="p_operatinghoursruleform.html?actionId=1" class="linktext">Operating Hours Rule/Agent Location Verification </a><br>&nbsp;&nbsp;</div>
					</td>		
				</tr>
				</authz:authorize>	
				<tr>
					<td class="formText" width="20%" align="right">
						Product:
					</td>
					<td align="left" width="30%" >
						<html:select path="productId" cssClass="textBox" tabindex="6" >
							<html:option value="">---All---</html:option>
							<c:if test="${productList != null}">
								<html:options items="${productList}" itemLabel="name" itemValue="productId"/>	
							</c:if>
						</html:select>
					</td>
					<%--<td class="formText" width="20%" align="right">Account Type:</td>
					<td align="left" width="30%">
						<html:select path="customerAccountTypeId" id="customerAccountTypeId" cssClass="textBox" tabindex="1">
							<html:option value="">---All---</html:option>
							<html:options items="${olaCustomerAccountTypeList}" itemLabel="name" itemValue="customerAccountTypeId"/>
						</html:select>
					</td>--%>
				</tr>
			<%--	<tr>
					<td class="formText" width="20%" align="right">
						Channel:
					</td>
					<td align="left" width="30%" >
						<html:select path="deviceTypeId" cssClass="textBox" tabindex="6" >
							<html:option value="">---All---</html:option>
							<c:if test="${deviceTypeList != null}">
								<html:options items="${deviceTypeList}" itemLabel="name" itemValue="deviceTypeId"/>	
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right">
						
						Segment:
					</td>
					<td align="left">
						<html:select path="segmentId" cssClass="textBox" tabindex="6" >
							<html:option value="">---All---</html:option>
							<c:if test="${segmentList != null}">
								<html:options items="${segmentList}" itemLabel="name" itemValue="segmentId" />
							</c:if>
						</html:select>					
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						
						Agent Network:
					</td>
					<td align="left">
						<html:select path="distributorId" cssClass="textBox" tabindex="6" >
							<html:option value="">---All---</html:option>
							<c:if test="${distributorList != null}">
								<html:options items="${distributorList}" itemLabel="name" itemValue="distributorId" />
							</c:if>
						</html:select>					</td>
					<td align="right"  class="formText">Agent Level:&nbsp;&nbsp; </td>
		             <td align="left">	
						<html:select path="distributorLevelId"  cssClass="textBox" tabindex="5">
							<html:option value="">---All---</html:option>
							<html:options items="${distributorLevelList}" itemLabel="name" itemValue="distributorLevelId"/>
						</html:select>										
		             </td>
				</tr>--%>
				<tr>
					<td class="formText" align="right">
						Created On From:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						Created On To:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_operatinghoursrulemanagement.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="13" />
					</td>
					<td class="formText" align="right">

					</td>
					<td align="left">&nbsp;</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</table></html:form>
		

		<ec:table items="operatingHoursRuleModelList" var="operatingHoursRuleModel"
		action="${contextPath}/p_operatinghoursrulemanagement.html"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<ec:row>	
				
				
				<ec:column property="productIdProductModel.name" title="Product" escapeAutoFormat="True" style="text-align: center"/>
				<%--<ec:column property="deviceTypeIdDeviceTypeModel.name" title="Channel" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="segmentIdSegmentModel.name" title="Segment" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="distributorIdDistributorModel.name" title="Agent Network" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="distributorLevelIdDistributorLevelModel.name" title="Agent Level" escapeAutoFormat="True" style="text-align: center"/>
				&lt;%&ndash;<ec:column property="limitTypeIdLimitTypeModel.name" title="Limit Type" escapeAutoFormat="True" style="text-align: center"/>&ndash;%&gt;
				<ec:column property="customerAccountTypeIdOlaCustomerAccountTypeModel.name" title="Account Type" escapeAutoFormat="True" style="text-align: center"/>
				--%><%--<ec:column property="maxNoOfTransaction" title="Max Transactions" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="maxAmountOfTransaction" title="Max Amount Transactions" escapeAutoFormat="True" style="text-align: center"/>--%>
				<ec:column property="allowedStartingHours" title="Transaction Allowed From(Hours 24H)" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="allowedEndingHours" title="Transaction Allowed To(Hours 24H)" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="errorMsg" title="Error Message" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Created On"/>
				<ec:column property="createdByAppUserModel.username" title="Created By" escapeAutoFormat="true"/>
				<ec:column property="updatedOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Updated On"/>	
				<ec:column property="updatedByAppUserModel.username" title="Updated By" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="isLocVrfReq" title="Agent Location Verification Required" escapeAutoFormat="true" style="text-align: center">
					<c:if test="${operatingHoursRuleModel.isLocVrfReq == true}">Active </c:if>
					<c:if test="${operatingHoursRuleModel.isLocVrfReq != true}">Deactivate </c:if>
				</ec:column>
				<ec:column property="isActive" title="Status" escapeAutoFormat="true" style="text-align: center">			
					<c:if test="${operatingHoursRuleModel.isActive == true}">Active </c:if>
					<c:if test="${operatingHoursRuleModel.isActive != true}">Deactivate </c:if>
				</ec:column>
				<ec:column alias="" title="" style="text-align: center" filterable="false" sortable="false" viewsAllowed="html">
					<authz:authorize ifAnyGranted="<%=updatePermission %>">			
						<input type="button" class="button" value="Edit" 
							onclick="javascript:document.location='p_operatinghoursruleform.html?operatingHoursRuleId=${operatingHoursRuleModel.operatingHoursRuleId}'" />
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=updatePermission %>">
						<input type="button" class="button" value="Edit" disabled="disabled" />		
					</authz:authorize>
				</ec:column>
			</ec:row>
		</ec:table>
		
		<%--<ajax:select source="distributorId" target="distributorLevelId"
		baseUrl="${contextPath}/p_getDistributorRefData.html"
		parameters="distributorId={distributorId},actionId=${retriveAction}" errorFunction="error"/>
--%>
		<script language="javascript" type="text/javascript">

      		Calendar.setup(
      		{
		       inputField  : "startDate", // id of the input field
			   button      : "sDate",    // id of the button
		    }
      		);
			Calendar.setup(
		    {
		      inputField  : "endDate", // id of the input field
		      button      : "eDate",    // id of the button
		  	  isEndDate: true
		    }
		    );

			var childWindow;
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
		<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>