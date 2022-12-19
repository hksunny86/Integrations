<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@include  file= "/common/taglibs.jsp" %>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>

<html xmlns="http://www.w3.org/1999/xhtml">
   <head>
<meta name="decorator" content="decorator">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
      <meta name="title" content="Linked Accounts"/>
      <script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
      <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>

   </head>
   <body bgcolor="#ffffff">
   
   <div id="successMsg" class ="infoMsg" style="display:none;"></div>
      <c:if test="${not empty messages}">
        <div class="infoMsg" id="successMessages">
          <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/>
            <br/>
          </c:forEach>
        </div>
        <c:remove var="messages" scope="session"/>
      </c:if>
      <table width="104%">
			<html:form name='transactionDetailI8Form'
				commandName="linkedAccountsListViewModelExtended" method="post"
				action="p_linkedaccountsreport.html"  >
				<tr>
					<td class="formText" width="33%" align="right">
						Inov8 MWallet ID:
					</td>
					<td align="left" width="17%" >
						<html:input path="mfsId" id="mfsId" cssClass="textBox" maxlength="8" tabindex="1" /> 
						${status.errorMessage}
					</td>
					<td class="formText" width="16%" align="right"> 
						From Date: 
					</td>
					<td align="left" width="34%" >
				        <html:input path="fromDate" id="fromDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="2" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="3" title="Clear Date" name="popcal"  onclick="javascript:$('fromDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right"> 
						Customer Name: 
					</td>
					<td align="left">
						<html:input path="fullName" id="fullName" cssClass="textBox" tabindex="4" maxlength="50" /> 
						${status.errorMessage}
					</td>
					<td class="formText" align="right"> 
						To Date: 
					</td>
					<td align="left">
					     <html:input path="toDate" id="toDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="5" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="6" title="Clear Date" name="popcal"  onclick="javascript:$('toDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr><br />	


				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
<%--				  <security:isauthorized action="<%=PortalConstants.ACTION_RETRIEVE%>">--%>
<%--						<jsp:attribute name="ifAccessAllowed">--%>
								<input name="_search" type="submit" class="button" value="Search" tabindex="12"  /> 
<%--		          		</jsp:attribute>--%>
<%--						<jsp:attribute name="ifAccessNotAllowed">  --%>
<%--					          <input name="_search" type="submit" class="button" value="Search" tabindex="-1" disabled="disabled" /> --%>
<%--						</jsp:attribute>--%>
<%--		         </security:isauthorized> --%>
					<input name="reset" type="reset" 
						onclick="javascript: window.location='p_linkedaccountsreport.html'"
						class="button" value="Cancel" tabindex="13" />
					</td>
					<td class="formText" align="right">

					</td>
					<td align="left">
					</td>
				</tr>
			</html:form>
		</table>
      <ec:table
      items="linkedAccountsList"
      var = "linkedAccountsList"
      retrieveRowsCallback="limit"
      filterRowsCallback="limit"
      sortRowsCallback="limit"
      filterable="false" 
      action="${pageContext.request.contextPath}/p_linkedaccountsreport.html"
      title="">
         <ec:exportXls fileName="listLinkedMfsAccounts.xls" tooltip="Export Excel"/>
         <ec:row>
           <ec:column property="fullName" title="Name" filterable="false"/>
           <ec:column property="mfsId" title="Inov8 MWallet ID" escapeAutoFormat="true" filterable="false"/>
           <ec:column property="mobileNo" title="Mobile Number" escapeAutoFormat="true" filterable="false"/>
           <ec:column property="accountNick" title="Account Nick" filterable="false"/>
           <ec:column property="paymentModeName" title="Payment Mode" filterable="false"/>
           <ec:column property="nic" title="NIC" escapeAutoFormat="true" filterable="false"/>
           <ec:column property="createdOn" title="Account Linking Date" cell="date" format="dd/MM/yyyy" filterable="false"/>
           <ec:column property="smaActive" title="Account Status" filterable="false">
           <c:if test="${linkedAccountsList.smaActive }">
           <c:out value="Active"></c:out>
           </c:if>
           <c:if test="${not linkedAccountsList.smaActive }">
           <c:out value="Inactive"></c:out>
           </c:if>
           </ec:column>
         </ec:row>
      </ec:table>

      <script language="javascript" type="text/javascript">
     // function confirmUpdateStatus(link)
      //{
       // if (confirm('Are you sure you want to update status?')==true)
        //{
         // window.location.href=link;
       // }
     // }
      Calendar.setup(
      		{
		       inputField  : "fromDate", // id of the input field
		       button      : "sDate"    // id of the button
		    }
      		);
			Calendar.setup(
		    {
		      inputField  : "toDate", // id of the input field
		      button      : "eDate",    // id of the button
		      isEndDate: true
		    }
		    );
      </script>

   </body>
</html>



