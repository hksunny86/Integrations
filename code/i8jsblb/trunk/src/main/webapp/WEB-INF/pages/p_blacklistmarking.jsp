<%--
  Created by IntelliJ IDEA.
  User: Malik
  Date: 8/10/2016
  Time: 6:56 PM
  To change this template use File | Settings | File Templates.
--%>
<!--Title: i8Microbank-->
<!--Author: Shawaiz Malik-->
<%@page import="com.inov8.microbank.common.util.PortalConstants" %>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%-- <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> --%>
<%@include file="/common/taglibs.jsp" %>

<html>
<head>
    <c:set var="retrieveAction"><%=PortalConstants.ACTION_RETRIEVE %>
    </c:set>
    
    <script type="text/javascript" src="${contextPath}/scripts/prototype.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/account/p_blacklistmarking.js"></script>
    <script type="text/javascript">
        var ACTION_UPDATE = <%=PortalConstants.ACTION_UPDATE%>;
    </script>
    <link type="text/css" rel="stylesheet" href="styles/ajaxtags.css"/>
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <%@include file="/common/ajax.jsp"%>
    <meta name="decorator" content="decorator">
    <meta name="title" content="Blacklist Marking"/>
    
    <%
        String blacklistMarkingPermission =  PortalConstants.PG_GP_CREATE;
        blacklistMarkingPermission +=","+ PortalConstants.ADMIN_GP_READ;
        blacklistMarkingPermission += "," +PortalConstants.MNG_BLKLST_MRKNG_UPDATE;

    %>
    
    
     <script language="javascript" type="text/javascript">
       // var jq=$.noConflict();
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";
       if (performance.navigation.type == performance.navigation.TYPE_RELOAD) {
           defineTabID();
           console.info("Page is reload")
       } else {
           console.info( "page is not reloaded");
       }



       function error(request) {
            alert("An unknown error has occured. Please contact with the administrator for more details");
        }
    </script>
</head>
<body bgcolor="#ffffff">
<spring:bind path="blacklistMarkingViewModel.*">
 <div id="rsp" class="ajaxMsg"></div>
    <c:if test="${not empty status.errorMessages}">
        <div class="errorMsg">
            <c:forEach var="error" items="${status.errorMessages}">
                <c:out value="${error}" escapeXml="false"/>
                <br/>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${not empty messages}">
        <div class="infoMsg" id="successMessages">
            <c:forEach var="msg" items="${messages}">
                <c:out value="${msg}" escapeXml="false"/>
                <br/>
            </c:forEach>
        </div>
        <c:remove var="messages" scope="session"/>
    </c:if>
</spring:bind>

<html:form name="blacklistMarkingViewForm" id="blacklistMarkingViewForm"
	commandName="blacklistMarkingViewModel" 
	action="${contextPath}/p_blacklistmarking.html?actionId=${retrieveAction} "
	onsubmit="return validateForm(this)" >



    <table width="850px" onmouseenter="defineTabID()">
    
        <%--<tr>
            <td align="right" class="formText">Mobile #:</td>
            <td align="left">
                <html:input path="mobileNo" onkeypress="return maskInteger(this,event)" cssClass="textBox" maxlength="11" tabindex="1"/>
            </td>

        </tr>--%>
        <tr>
            <td align="right" class="formText">CNIC #:</td>
            <td align="left">
                <html:input path="cnic" onkeypress="return maskInteger(this,event)" cssClass="textBox" maxlength="13" tabindex="2"/>
            </td>
            <td align="right" class="formText">
                Registration Status:
            </td>
            <td align="left">
                <html:select path="regStateId" cssClass="textBox" tabindex="3">
                    <html:option value="">[All]</html:option>
                    <c:if test="${registrationStateList != null}">
                        <html:options items="${registrationStateList}" itemLabel="name" itemValue="registrationStateId"/>
                    </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText">User ID:</td>
            <td align="left">
                <html:input path="userId" onkeypress="return maskInteger(this,event)" cssClass="textBox" maxlength="11" tabindex="4"/>
            </td>
            <td align="right" class="formText">User Type</td>
            <td align="left">
                <html:select path="appUserTypeId" cssClass="textBox"  tabindex="5">
                    <html:option value="">[All]</html:option>
                    <c:if test="${appUserTypeModelList != null}">
                        <html:options items="${appUserTypeModelList}" itemLabel="name" itemValue="appUserTypeId"/>
                    </c:if>
                </html:select>
            </td>
        </tr>

        <tr>
            <td align="right" class="formText">&nbsp;
            </td>
            <td align="left" colspan="2" class="formText">
                <input type="submit" class="button" id="searchButton" onclick="defineTabID()" value="Search" tabindex="6"/>
                <input type="reset" class="button" value="Cancel" name="_reset" tabindex="7" onclick="javascript: window.location='p_blacklistmarking.html?actionId=<%=PortalConstants.ACTION_RETRIEVE%>'"/>
                <input type="hidden" id="tabvalue" value="AQ" onclick="defineTabID()"/>
            </td>
            <td align="left" class="formText">
            </td>
        </tr>
    </table>
    
    
    



</html:form>
<ec:table filterable="false" items="blacklistMarkingViewModelList" var="model" retrieveRowsCallback="limit"
          filterRowsCallback="limit" sortRowsCallback="limit"
          action="${contextPath}/p_blacklistmarking.html?actionId=${retrieveAction} "
          title="" autoIncludeParameters="false">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Blacklist Marking Report.xls" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Blacklist Marking Report.xlsx" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="Blacklist Marking Report" fileName="Blacklist Marking Report.pdf" tooltip="Export PDF"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Blacklist Marking Report.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>
     
    <input type="hidden" name="changedCnicList" id="changedCnicList"> 
    
    <ec:row>
		
        <ec:column property="appUserId"  title="Blacklisted"  viewsAllowed="html" sortable="false" headerClass="tableHeader blacklistedHeader" style="text-align:center;" width="10%" >
            <input type="hidden" id="blacklistMarkingVoList${index}.cnicNo" name="blacklistMarkingVoList[${index}].cnicNo" value="${model.cnic}"/>
            <c:choose>
                <c:when test="${model.blacklisted == 1}">
                    <input id="blacklistMarkingVoList${index}.blacklisted" type="checkbox" 
                    	checked="checked" name="blacklistMarkingVoList[${index}].blacklisted" 
                    	class="blacklisted" onclick="getCheckBoxValue(this, '${model.cnic}');" />
                </c:when>
                <c:otherwise>
                    <input id="blacklistMarkingVoList${index}.blacklisted" type="checkbox" 
                    name="blacklistMarkingVoList[${index}].blacklisted" class="blacklisted"  
                    onclick="getCheckBoxValue(this, '${model.cnic}');" />
                </c:otherwise>
            </c:choose>
        </ec:column>
        <%--<ec:column property="mobileNo" title="Mobile #" escapeAutoFormat="true" style="text-align:center;" width="18%" />--%>
        <ec:column property="cnic" title="CNIC #" escapeAutoFormat="true" style="text-align:center;" width="20%" />
        <ec:column property="appUserType" title="User Type" width="20%"/>
        <ec:column property="userId" title="User ID" escapeAutoFormat="true" style="text-align:center;" width="20%"/>
        <ec:column property="regState" title="Registration Status" width="20%"/>
        <%-- <ec:column property="" title="History" width="20%"/> --%>
        <ec:column alias=" " title="History" viewsAllowed="html" width="20%" filterable="false" sortable="false">
			<input type="button" class="button" style="width='90px'" value="History" onclick="javascript:window.location.href='${pageContext.request.contextPath}/p_blacklistMarkUnmarkHistory.html?cnicNo=${model.cnic}&actionId=2';" />
						<%-- <c:choose>						
							<c:when test="${userInfoListViewModel.customerAccountTypeId==1 || userInfoListViewModel.customerAccountTypeId==2 }">
								<input type="button" class="button" style="width='90px'" value="History" onclick="javascript:window.location.href='${pageContext.request.contextPath}/p_mnomfsaccountdetails.html?appUserId=${userInfoListViewAppUserId}&actionId=2';" />
							</c:when>
							<c:otherwise>
								<input type="button" class="button" style="width='90px'" value="History" onclick="javascript:window.location.href='${pageContext.request.contextPath}/p_level2accountdetails.html?appUserId=${userInfoListViewAppUserId}&actionId=2';" />
							</c:otherwise>
						</c:choose> --%>
		 </ec:column>
            
        
    </ec:row>
   
</ec:table>
<table width="100%">
    <tr>
        <td align="center" class="formText">
            <c:if test="${not empty blacklistMarkingViewModelList}">
                <authz:authorize ifNotGranted="<%=blacklistMarkingPermission%>">
                    <input type="button" name="saveButton" id="saveButton" value="Save" disabled="true" tabindex="8" />
                </authz:authorize>
                <authz:authorize ifAnyGranted="<%=blacklistMarkingPermission%>">
                    <input type="button" name="saveButton" id="saveButton" value="Save" tabindex="9" onclick="setActionId(this, changedCnicList)"/>
                </authz:authorize>
            </c:if>
        </td>
    </tr>
</table>

<script language="javascript" type="text/javascript">


		
	  function getCheckBoxValue(val, cnic){	  
		 
		var oldVal, newVal;
		newVal = val.checked;
		if (newVal == true) {
			oldVal = false;
		} else {
			oldVal = true;
		}
		//alert("Old Value : "+ oldVal + " & New Value : "+newVal+ " &  CNIC : "+cnic);	  				
		var element = document.getElementById("changedCnicList");
		element.value = element.value.concat(cnic + ",");
		changedCnicList = element.value;
		//alert(changedCnicList);	  
	}
</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
<script>
    function defineTabID() {
        var iPageTabID = sessionStorage.getItem("tabID");
        document.getElementById("tabvalue").setAttribute('value', iPageTabID);
        if (iPageTabID == null) {
            window.location.href='login.jsp';
        }
        else {
            document.getElementById("tabvalue").setAttribute('value', iPageTabID);
        }
    }
</script>

</body>
</html>

