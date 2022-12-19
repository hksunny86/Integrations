<%--
  Created by IntelliJ IDEA.
  User: Malik
  Date: 7/1/2016
  Time: 10:08 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.inov8.microbank.common.util.PortalConstants" %>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>
    <meta name="decorator" content="decorator2">
    <meta name="title" content="WHT Configurations"/>

    <script language="javascript" type="text/javascript">
        var jq=$.noConflict();
        jq('#whtConfigVoList0.filerRate').focus();

        function validateForm() {

            var isValid = true;

            jq('#whtConfigTable table').each( function(outerIndex) {
             var commissionFilerPercentage = parseFloat(jq(this).find("input[id$='filerRate']").val()) || 0;
             var commissionNonFilerPercentage = parseFloat(jq(this).find("input[id$='nonFilerRate']").val()) || 0;

             
             
             if ( commissionFilerPercentage > 100 || commissionFilerPercentage <0) {
                alert("Invalid Filer Percentage Amount. Amount must be between 0 and 100");
                jq(this).find("input[id$='filerRate']").css('border-color', 'red');
                 return isValid = false;
             }
             else if (commissionNonFilerPercentage > 100 ||commissionNonFilerPercentage <0) {
                 alert("Invalid Non Filer Percentage Amount. Amount must be between 0 and 100");
                 jq(this).find("input[id$='nonFilerRate']").css('border-color', 'red');
                 return isValid = false;
             }
             
             });
            
            jq("[id$='thresholdLimit']").each(function(){
            	
              var thresholdLimitPerDay = parseInt(jq(this).val()) || 0;
              
               if (thresholdLimitPerDay == "" ||thresholdLimitPerDay <=0) {
                   alert("Invalid Threshold Limit per Day. Threshold Limit per Day must be greater than 0");
                   jq(this).css('border-color', 'red');
                   return isValid = false;
               }
            });
            
            return isValid;
        }
    </script>
    <style type="text/css">
        select { width: 50px !important; height: 24px !important; }
    </style>

    <%
        String updatePermission= PortalConstants.PG_GP_UPDATE;
        updatePermission +=	"," + PortalConstants.ADMIN_GP_READ;
        updatePermission +=	"," + PortalConstants.WHT_CNFG_UPDATE;
    %>
</head>

<body bgcolor="#ffffff" id="body-content">
<div class="infoMsg" id="errorMessages" style="display:none;"></div>
<c:if test="${not empty errors}">
    <div class="errorMsg" id="errorMessages">
        <c:forEach var="error" items="${errors}">
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="errors" scope="session"/>
</c:if>
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
<html:form id="whtConfigForm" name="whtConfigForm" commandName="whtConfigWrapper" action="p_WHTConfig.html" onsubmit="return validateForm()" >
    <table border="0" cellpadding="1" width="100%" id="whtConfigTable">

        <tr>
            <td>
                <fieldset>
                    <legend>&nbsp; WHT on Commission &nbsp;</legend>
                    <table id="onCommissionTable" class="tableRegion" width="100%" cellspacing="2" cellpadding="0">
                        <tr bgcolor="FBFBFB" >
                            <td colspan="2" align="center">&nbsp;</td>
                        </tr>
                        <tr style="height: 40px">
                            <td bgcolor="F3F3F3" class="formText" width="50%" align="right">
                                <span style="color:#FF0000">*</span>   Filer Rate(%):
                            </td>
                            <td align="left" bgcolor="FBFBFB" width="50%" >
                                <authz:authorize ifAnyGranted="<%=updatePermission%>">
                                    <html:input path="whtConfigVoList[0].filerRate" tabindex="1" onkeypress="return checkNumeric(this,event)" class="textBox" maxlength="5"/>
                                    <html:hidden path="whtConfigVoList[0].whtConfigId"/>
                                </authz:authorize>
                                <authz:authorize ifNotGranted="<%=updatePermission%>">
                                    ${whtConfigWrapper.whtConfigVoList[0].filerRate}
                                </authz:authorize>
                            </td>
                        </tr>
                        <tr style="height: 40px">
                            <td bgcolor="F3F3F3" class="formText" width="50%" align="right">
                                <span style="color:#FF0000">*</span>  Non Filer Rate(%):
                            </td>
                            <td align="left" bgcolor="FBFBFB" width="50%" >
                                <authz:authorize ifAnyGranted="<%=updatePermission%>">
                                    <html:input path="whtConfigVoList[0].nonFilerRate" tabindex="2" onkeypress="return checkNumeric(this,event)" class="textBox" maxlength="5"/>
                                </authz:authorize>
                                <authz:authorize ifNotGranted="<%=updatePermission%>">
                                    ${whtConfigWrapper.whtConfigVoList[0].nonFilerRate}
                                </authz:authorize>
                            </td>
                        </tr>
                    </table>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td>
                <fieldset>
                    <legend> WHT on Cash Withdrawal(Section 231-A)</legend>
                    <table id="onCahWithdrawalTable" class="tableRegion" width="100%" cellspacing="2" cellpadding="0">
                        <tr bgcolor="FBFBFB" >
                            <td colspan="2" align="center">&nbsp;</td>
                        </tr>
                        <tr style="height: 40px">
                            <td bgcolor="F3F3F3" class="formText" width="50%" align="right">
                                <span style="color:#FF0000">*</span>  Filer Rate(%):
                            </td>
                            <td align="left" bgcolor="FBFBFB" width="50%" >
                                <authz:authorize ifAnyGranted="<%=updatePermission%>">
                                    <html:input path="whtConfigVoList[1].filerRate" tabindex="3" onkeypress="return checkNumeric(this,event)"  class="textBox" maxlength="5"/>
                                    <html:hidden path="whtConfigVoList[1].whtConfigId"/>
                                </authz:authorize>
                                <authz:authorize ifNotGranted="<%=updatePermission%>">
                                    ${whtConfigWrapper.whtConfigVoList[1].filerRate}
                                </authz:authorize>
                            </td>
                        </tr>
                        <tr style="height: 40px">
                            <td bgcolor="F3F3F3" class="formText" width="50%" align="right">
                                <span style="color:#FF0000">*</span>  Non Filer Rate(%):
                            </td>
                            <td align="left" bgcolor="FBFBFB" width="50%" >
                                <authz:authorize ifAnyGranted="<%=updatePermission%>">
                                    <html:input path="whtConfigVoList[1].nonFilerRate" tabindex="4" onkeypress="return checkNumeric(this,event)" class="textBox" maxlength="5"/>
                                </authz:authorize>
                                <authz:authorize ifNotGranted="<%=updatePermission%>">
                                    ${whtConfigWrapper.whtConfigVoList[1].nonFilerRate}
                                </authz:authorize>
                            </td>
                        </tr>
                        <tr style="height: 40px">
                            <td bgcolor="F3F3F3" class="formText" width="50%" align="right">
                                <span style="color:#FF0000">*</span>   Threshold Limit per Day:
                            </td>
                            <td align="left" bgcolor="FBFBFB" width="50%" >
                                <authz:authorize ifAnyGranted="<%=updatePermission%>">
                                    <html:input path="whtConfigVoList[1].thresholdLimit" tabindex="5" onkeypress="return maskInteger(this,event)" class="textBox" maxlength="7"/>
                                </authz:authorize>
                                <authz:authorize ifNotGranted="<%=updatePermission%>">
                                    ${whtConfigWrapper.whtConfigVoList[1].thresholdLimit}
                                </authz:authorize>
                            </td>
                        </tr>
                    </table>
                </fieldset>
            </td>
        </tr>

        <tr>
            <td>
                <fieldset>
                    <legend> WHT on other Transactions(Section 236-P)</legend>
                    <table id="onTransactionTable" class="tableRegion" width="100%" cellspacing="2" cellpadding="0">
                        <tr bgcolor="FBFBFB" >
                            <td colspan="2" align="center">&nbsp;</td>
                        </tr>
                        <tr style="height: 40px">
                            <td bgcolor="F3F3F3" class="formText" width="50%" align="right">
                                <span style="color:#FF0000">*</span>  Filer Rate(%):
                            </td>
                            <td align="left" bgcolor="FBFBFB" width="50%" >
                                <authz:authorize ifAnyGranted="<%=updatePermission%>">
                                <html:input path="whtConfigVoList[2].filerRate" tabindex="6" onkeypress="return checkNumeric(this,event)" class="textBox" maxlength="5"/>
                                <html:hidden path="whtConfigVoList[2].whtConfigId"/>
                                </authz:authorize>
                                <authz:authorize ifNotGranted="<%=updatePermission%>">
                                    ${whtConfigWrapper.whtConfigVoList[2].filerRate}
                                </authz:authorize>
                            </td>
                        </tr>
                        <tr style="height: 40px">
                            <td bgcolor="F3F3F3" class="formText" width="50%" align="right">
                                <span style="color:#FF0000">*</span>   Non Filer Rate(%):
                            </td>
                            <td align="left" bgcolor="FBFBFB" width="50%" >
                                <authz:authorize ifAnyGranted="<%=updatePermission%>">
                                    <html:input path="whtConfigVoList[2].nonFilerRate" tabindex="7" onkeypress="return checkNumeric(this,event)" class="textBox" maxlength="5"/>
                                </authz:authorize>
                                <authz:authorize ifNotGranted="<%=updatePermission%>">
                                    ${whtConfigWrapper.whtConfigVoList[2].nonFilerRate}
                                </authz:authorize>
                            </td>
                        </tr>
                        <tr style="height: 40px">
                            <td bgcolor="F3F3F3" class="formText" width="50%" align="right">
                                <span style="color:#FF0000">*</span>  Threshold Limit per Day:
                            </td>
                            <td align="left" bgcolor="FBFBFB" width="50%" >
                                <authz:authorize ifAnyGranted="<%=updatePermission%>">
                                    <html:input path="whtConfigVoList[2].thresholdLimit" tabindex="8" onkeypress="return maskInteger(this,event)" class="textBox" maxlength="7"/>
                                </authz:authorize>
                                <authz:authorize ifNotGranted="<%=updatePermission%>">
                                    ${whtConfigWrapper.whtConfigVoList[2].thresholdLimit}
                                </authz:authorize>
                            </td>
                        </tr>
                    </table>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td align="center" colspan="2">
            <c:choose>
             <c:when  test="${param.isReadOnly}">
						<input type="submit" name=" Close " value=" Close " class="button" onclick="window.close();" />
			</c:when>
			
			<c:otherwise>
                <authz:authorize ifAnyGranted="<%=updatePermission%>">
                <input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
                <input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />
                <input type="hidden" name="isUpdate" value="true" />
                <input type="submit" value="Submit" class="button" id="btnSave"/>
                <input type="button" name="reset" type="reset" onclick="javascript: window.location='p_WHTConfig.html?<%=PortalConstants.KEY_ACTION_ID%>=<%=PortalConstants.ACTION_UPDATE%>'" class="button" value="Cancel" />
               </authz:authorize>
              </c:otherwise>
              </c:choose>
            </td>
        </tr>
    </table>
</html:form>
</body>
</html>
