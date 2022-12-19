<!--Author: Naseer Ullah-->

<%@include file="/common/taglibs.jsp"%>
<%@ page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>

<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
        <%@include file="/common/ajax.jsp"%>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="Account Opening Report"/>
		<script language="javascript" type="text/javascript">
		var jq=$.noConflict();
		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		  function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
        </script>
	</head>
	<body bgcolor="#ffffff">
		<spring:bind path="extendedUserInfoListViewModel.*">

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

	    <table border="0" cellpadding="0" cellspacing="0" width="100%">
	        <tr>
	            <td align="right"></td>
	        </tr>
	    </table>

		<html:form name="userInfoListViewForm" commandName="extendedUserInfoListViewModel" onsubmit="return validateForm(this)" action="p_marketingaccountopening.html">
			<table width="750px">
				<tr>
					<td class="formText" align="right" width="18%">
						MSISDN:
					</td>
					<td align="left" width="32%">
						<html:input path="mobileNo" id="mobileNo" cssClass="textBox" maxlength="11" onkeypress="return maskNumber(this,event)" tabindex="1"/>
					</td>
					<td align="right" class="formText" width="18%">
						Account Type:
					</td>
					<td align="left" width="32%">
						<html:select path="customerAccountTypeId" cssClass="textBox" tabindex="2">
							<html:option value="">---All---</html:option>
							<html:options items="${customerAccountTypeList}" itemLabel="name" itemValue="customerAccountTypeId"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Account Status:
					</td>
					<td align="left">
						<html:select path="accountEnabled" cssClass="textBox" tabindex="3">
							<html:option value="">---All---</html:option>
							<html:option value="ACTIVATED">ACTIVATED</html:option>
							<html:option value="DE-ACTIVATED">DE-ACTIVATED</html:option>
						</html:select>
					</td>
 					<td align="right" class="formText">
						Agent Network:
					</td>
					<td align="left">
                        <html:select id="agentNetworkId" tabindex="3" path="agentNetworkId" cssClass="textBox" >
                            <html:option value="">[Select]</html:option>
                            <c:if test="${distributorModelList != null}">
                                <html:options items="${distributorModelList}" itemLabel="name" itemValue="distributorId" />
                            </c:if>
                        </html:select>
					</td>
				</tr>
                <tr>
                    <td class="formText" align="right">
                        Region:
                    </td>
                    <td align="left">
                        <html:select id="regionId"  path="regionId" cssClass="textBox" tabindex="8">
                            <html:option value="">[Select]</html:option>
                            <c:if test="${regionList != null}">
                                <html:options items="${regionList}" itemLabel="regionName" itemValue="regionId" />
                            </c:if>
                        </html:select>
                    </td>
                    <td class="formText" align="right">
                        Area Level:
                    </td>
                    <td align="left">
                        <html:select id="areaLevelId" path="areaLevelId"
                                     cssClass="textBox" tabindex="11">
                            <html:option value="">[Select]</html:option>
                            <c:if test="${areaLevelList != null}">
                                <html:options items="${areaLevelList}" itemLabel="areaLevelName" itemValue="areaLevelId" />
                            </c:if>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td class="formText" align="right">
                        Area:
                    </td>
                    <td align="left">
                        <html:select id="areaId" path="areaId"
                                     cssClass="textBox" tabindex="10">
                            <html:option value="">[Select]</html:option>
                            <c:if test="${areaList != null}">
                                <html:options items="${areaList}" itemLabel="name" itemValue="areaId" />
                            </c:if>
                        </html:select>
                    </td>
                    <td class="formText" align="right">
                        City:
                    </td>
                    <td align="left">
                        <html:select id="cityId" path="businessCityId" cssClass="textBox" tabindex="10">
                            <html:option value="">[Select]</html:option>
                            <c:if test="${cityModelList != null}">
                                <html:options items="${cityModelList}" itemLabel="name" itemValue="cityId" />
                            </c:if>
                        </html:select>
                    </td>
                </tr>

				<tr>
					<td class="formText" align="right">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="6" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">&nbsp;</td>
					<td align="left" class="formText">
						<input type="submit" class="button" value="Search" name="_search" tabindex="9" /> 
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_marketingaccountopening.html'" tabindex="10"/>
					</td>
					<td align="left" class="formText">&nbsp;</td>
					<td align="left" class="formText">&nbsp;</td>
				</tr>
				<tr>
				   <td colspan="4">&nbsp;</td>
				</tr>
			</table>
		</html:form>
        <ajax:select source="agentNetworkId" target="regionId" baseUrl="${contextPath}/p_regionrefdata.html"
                     parameters="distributorId={agentNetworkId},actionId=1" errorFunction="error"/>
        <ajax:select source="regionId" target="areaLevelId" baseUrl="${contextPath}/p_arealevelrefdata.html"
                     parameters="regionId={regionId},actionId=1" errorFunction="error"/>
        <ajax:select source="areaLevelId" target="areaId" baseUrl="${contextPath}/p_areanamerefdata.html"
                     parameters="areaLevelId={areaLevelId},actionId=1" errorFunction="error"/>
		<ec:table filterable="false" items="userInfoListViewModelList" var="userInfoListViewModel" retrieveRowsCallback="limit" filterRowsCallback="limit"
		sortRowsCallback="limit" action="${contextPath}/p_marketingaccountopening.html" title="">

			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Account Opening Report.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Account Opening Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="List Customer Accounts" fileName="Account Opening Report.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Account Opening Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>	
			<ec:row>
				<ec:column property="accountOpeningDate" alias="Date" cell="date" format="dd/MM/yyyy" title="Date"/>
				<ec:column property="accountOpeningDate" alias="Time" cell="date" format="hh:mm a" sortable="false" title="Time" width="55px"/>
				<ec:column property="accountOpenedBy" title="Agent ID"/>
				<ec:column property="agentBusinessName" title="Agent Business Name"/>
				<ec:column property="mobileNo" title="MSISDN" escapeAutoFormat="true"/>

                <ec:column property="region" title="Region"/>
                <ec:column property="areaLevel" title="Area Level"/>
                <ec:column property="area" title="Area"/>
                <ec:column property="businessCity" title="City"/>

                <ec:column property="customerAccountType" title="Account Type"/>
				<ec:column property="accountEnabled" title="Status (Active/Deactive)" escapeAutoFormat="true"/>
				<ec:column property="registrationState"/>
			</ec:row>
		</ec:table>

		<script language="javascript" type="text/javascript">
	        document.forms[0].mobileNo.focus();
		
      		Calendar.setup(
      		{
		       inputField  : "startDate", // id of the input field
		       button      : "sDate"    // id of the button
		    }
      		);
			Calendar.setup(
		    {
		      inputField  : "endDate", // id of the input field
		      button      : "eDate",    // id of the button
		      isEndDate: true
		    }
		    );
			</script>
		<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>
