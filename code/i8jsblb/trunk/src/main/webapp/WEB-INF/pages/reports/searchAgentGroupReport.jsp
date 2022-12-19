
 <!--Author: Tayyab Islam -->
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants" %>
<%@page import="com.inov8.microbank.common.util.ReportConstants" %>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<%@ page import='java.util.Calendar'%>
<%@ page import='java.util.GregorianCalendar'%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="decorator" content="decorator">
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Agent Group Detail"/>
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
		<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
       	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>  
   	 	<script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script> 
				
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
        <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css"/>
        <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		<script type="text/javascript">
			var jq=$.noConflict();
			var serverDate ="<%=PortalDateUtils.getServerDate()%>";
			jq(document).ready(function() {
				jq('.filterButtons').hide();
				jq("#searchAgentGRP").click(function(){
				
				if(document.getElementById("startDate").value == "" || document.getElementById("endDate").value == "")
				
				{
				  alert("Start Date and End Date is Requied!")
				  return false;
				}
				
				});
				});
			
			
		</script>
	<%@include file="/common/ajax.jsp"%>
		
	</head>
   	<body bgcolor="#ffffff">
		<div id="rsp" class="ajaxMsg"></div>
		<!-- <div id="successMsg" class ="infoMsg" style="display:none;"></div> -->
		<spring:bind path="agentTaggingViewModel.*">
					<c:if test="${not empty messages}">
						<div class="infoMsg" id="successMessages">
							<c:forEach var="msg" items="${messages}">
									<c:out value="${msg}" escapeXml="false" />
											<br />
							</c:forEach>
						</div>
								<c:remove var="messages" scope="session" />
				</c:if>
		</spring:bind>
			<html:form action="searchAgentGroupReport.html" name="agentGroupViewForm" method="post" commandName="agentTaggingViewModel" onsubmit="return validateForm(this);">
					<table width="750px">
										<tr>
											<td width="17%" align="right" class="formText">
											Group Title:
											</td>
											<td width="33%" align="left">
												<html:select path="groupTitle" cssClass="textBox">
															<html:option value="" tabindex="1">[All]</html:option>
																		<c:if test="${agentGroupList != null}">
															<html:options items="${agentGroupList}"  />
				 														</c:if>
												</html:select>
											</td>
												<td width="16%" align="right" class="formText">
												 CNIC:
												</td>
													<td width="34%" align="left">
														<html:input path="cnic" tabindex="2" id="lastName" cssClass="textBox" maxlength="20"/>
													</td>
									</tr>
										<tr>
											<td align="right" class="formText">
											ID:
											</td>
												<td align="left">
													<html:input onkeypress="return maskNumber(this,event)" path="parrentId" tabindex="3" id="cnic" cssClass="textBox" maxlength="13"/>
												</td>
													<td align="right" class="formText">
													Mobile No.:
													</td>
														<td align="left">
															<html:input onkeypress="return maskNumber(this,event)" path="mobileNumber" tabindex="4" id="mobileNumber" cssClass="textBox" maxlength="11"/>
														</td>
										</tr>
											<tr>
												<td class="formText" align="right">
													<span style="color:#FF0000">*</span>Start Date:
												</td>
													<td align="left">
				      								  <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
														<img id="sDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
															<img id="sDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
													</td>
														<td class="formText" align="right">
															<span style="color:#FF0000">*</span>End Date:
														</td>
															<td align="left">
					 										    <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					    											 <img id="eDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					   													  <img id="eDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
															</td>
											</tr>

												<tr>
													<td></td>
															<td align="left">
																			<%-- <c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
																					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="${retriveAction}" /> --%>
																							<input type="submit" class="button" value="Search" tabindex="5" name="_search" id="searchAgentGRP"/>
																									<input type="reset" class="button" value="Cancel" tabindex="6" name="_cancel" onclick="javascript: window.location='searchAgentGroupReport.html?actionId=${retriveAction}'">
															</td>
					
												</tr>
			</table>
			<%@ include file="../zip_report.jsp" %>
</html:form>
		 
        <ec:table items="agentTaggingGroupList" var="agentTaggingViewModel" method="Post" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit"
			action="${contextPath}/searchAgentGroupReport.html?actionId=${retriveAction}" title="" filterable="false">
			
	        <ec:row>
	       		 <c:set var="parentAgentID" value="${agentTaggingViewModel.pk}"> </c:set>
	        
               		 <ec:column property="groupTitle" title="Group Title"/>
                	 <ec:column property="parrentId" title="ID"/>
               		 <ec:column property="businessName" title="Business Name"/>
               		 <ec:column property="mobileNumber" title="Mobile Number"/>
               		 <ec:column  property="pk" alias="edit" title="Tagged Agents" viewsAllowed="html" sortable="false" style="text-align:center;">
              					<input name="_search" type="button" class="button" style="width='90px'" value="Open Tagged Agents"
							onclick="javascript:window.location.href='${pageContext.request.contextPath}/searchTaggedAgents.html?pID=${parentAgentID}&cDate=${CDATE}&eDate=${EDATE} }';" />
			  		</ec:column>  
		</ec:row>
    </ec:table>

     
	  
		<%@ include file="../post_zip_report.jsp" %>
	  
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
		    
      	</script>
      	 
   </body>
</html> 