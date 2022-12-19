<!--Author: Zeeshan Naeem-->
<%@ taglib prefix="beforeSend" uri="http://ajaxtags.org/tags/ajax" %>
<%@page import="org.springframework.web.bind.ServletRequestUtils"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta name="decorator" content="decorator2">
    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>

    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.0.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-custom.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/p_WHTExemptionmapping.js"></script>
    <link rel="stylesheet" type="text/css"
          href="styles/deliciouslyblue/calendar.css" />
    <link rel="stylesheet" type="text/css" href="styles/jquery.multiselect.css" />
    <link rel="stylesheet" type="text/css" href="styles/jquery-ui-custom.min.css" />
    <link rel="stylesheet" type="text/css"
          href="${contextPath}/styles/extremecomponents.css">

    <meta http-equiv="Content-Type"
          content="text/html; charset=iso-8859-1" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="pragma" content="no-cache" />

    <meta name="title" content="WHT Exemption" />

    <style>
        #table_form{
            display:block;
            width:100%;
            height:auto;
            margin:0px;
            padding:0px;
        }
        #table_form .left{
            display:block;
            width:49%;
            float:left;
        }
        #table_form .right{
            display:block;
            width:49%;
            float:right;
        }
    </style>
    <%@include file="/common/ajax.jsp"%>


    <%
        String createPermission = PortalConstants.MNG_WHT_CREATE;
        String updatePermission = PortalConstants.MNG_WHT_UPDATE;
        String readUpdatePermission = PortalConstants.MNG_WHT_READ;
        
        boolean isPermission = (AuthenticationUtil.checkRightsIfAny(updatePermission) || AuthenticationUtil.checkRightsIfAny(createPermission));

    %>
    
    <script type="text/javascript">
    
	function sourceACInitProgress(){
	
		if(document.forms[0].userId.value == ''){
			alert('Agent Id is Mandatory to load Name and CNIC');
			return false;
		}
		
		
		$('errorMessages').innerHTML = "";
		Element.hide('errorMessages');
		return true;
	}
	
	function sourceACEndProgress(){
		if(document.forms[0].agentName.value == 'null' && document.forms[0].agentCnic.value == 'null'){
			document.getElementById('submitBtn').disabled = true;
			document.forms[0].agentName.value = '';
			document.forms[0].agentCnic.value = '';
			Element.show('errorMessages');
			$('errorMessages').innerHTML = $F('errMsg');
			scrollToTop(1000);

		}

		if(document.forms[0].errMsg.value == 'null'){
			document.getElementById('submitBtn').disabled = false;
			document.forms[0].errMsg.value = '';
		}
	}
	
    
    </script>

</head>
<body bgcolor="#ffffff">

<div id="successMsg" class="infoMsg" style="display: none;"></div>
<spring:bind path="wHTExemptionVO.*">
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
<html:form name="wHTExemptionForm" commandName="wHTExemptionVO"
           enctype="multipart/form-data" method="POST"
           onsubmit="return onFormSubmit(this)"
           action="p_WHTExemption_form.html">

<input type="hidden" id="errMsg" value=""/>
<div class="infoMsg" id="errorMessages" style="display:none;"></div>

<input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
<input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />

<input type="hidden" id="whtExemptionId" name="whtExemptionId" />


<div class="infoMsg" id="errorMessages" style="display:none;"></div>
<div id="table_form">
    <div class="center">
        <table width="100%" border="0" cellpadding="0" cellspacing="1">
        
        <!-- <input type="hidden" name="isUpdate" id="isUpdate" value="false" /> -->
        
            <tr>
                <td height="16" align="right" class="formText"
                    width="25%">
                    <span style="color: #FF0000">*</span>Agent Id:
                </td>

                <td align="left"  class="formText" width="25%">
                    <html:input id="userId" path="userId" cssClass="textBox" tabindex="3" maxlength="20" onkeypress="return maskAlphaNumeric(this,event)"/>
                    <input id="loadButton" name="loadButton" type="button" value="Load" class="button"/>
                </td>
            </tr>


            <tr>
                <td height="16" align="right"  class="formText">
                    Name:
                </td>
                <td width="58%" align="left">
                    <input name="agentName" id="agentName" type="text" class="textBox" maxlength="35" readonly="true"/>
                </td>
            </tr>


            <tr>
                <td height="16" align="right" class="formText">
                    CNIC:
                </td>
                <td width="58%" align="left">
                    <input name="agentCnic" id="agentCnic" type="text" class="textBox" maxlength="35" readonly="true" />
                </td>
            </tr>

            <tr>
                <td class="formText" align="right">
                    <span style="color:#FF0000">*</span> Exemption Start Date:
                </td>
                <td align="left">
                    <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                    <img id="sDate" tabindex="11" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                    <img id="sDate" tabindex="12" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
                </td>
                </tr>
            <tr>
                <td class="formText" align="right">
                    <span style="color:#FF0000">*</span>  Exemption End Date:
                </td>
                <td align="left">
                    <html:input path="endDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                    <img id="eDate" tabindex="13" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                    <img id="eDate" tabindex="14" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
                </td>
            </tr>

                              
             <ajax:updateField parser="new ResponseXmlParser()"
                              source="userId"
                              action="loadButton"
                              target="agentName,agentCnic,errMsg"
                              baseUrl="${contextPath}/fetchDataByAppUserId.html"
                              parameters="userId={userId}"
                              preFunction="sourceACInitProgress"
                              postFunction="sourceACEndProgress"></ajax:updateField>
                              
                              
        </table>
    </div>

    <table>
        <tr>
            <td width="100%" align=center>
			
			<c:choose>
             <c:when  test="${param.isReadOnly}">
						<input type="submit" name=" Close " value=" Close " class="button" onclick="window.close();" />
			</c:when>
			
			<c:otherwise>
                       

<c:if test="<%=isPermission %>">
                          <c:choose>
                              <c:when test="${saveAllow}">
                                  <input type="submit" id="submitBtn" class="button" value="Submit" tabindex="53"/>
                              </c:when>
                              <c:otherwise>
                                  <input type="submit"  id="submitBtn" class="button" value="Submit" tabindex="53" disabled="disabled"/>
                              </c:otherwise>
                          </c:choose>
                          </c:if>

                                <!-- <input type="submit" class="button" value="Submit" tabindex="53" disabled="disabled"/> -->
                         

                            <input type="button" class="button" value="Cancel" tabindex="54" onclick="javascript: window.location='p_WHTExemption_form.html'"/>
			</c:otherwise>
			</c:choose>
            </td>
        </tr>

        <tr>
            <td>
                <fieldset class="cold" width="1700">
                    <legend>WHT Exemption History</legend>
                    <div class="eXtremeTable">
                        <table class="tableRegion" id="p_loadWhtExemptionmapping" width="700"
                               <%--style="width: 95% !important;"--%> allign="center">
                            <thead>
                            <tr>
                                <td  width="100" class="tableHeader" width="10%" allign="right">Sr.No.</td>
                                <td  width="100" class="tableHeader" width="10%" allign="right">Agent Id</td>
                                <td  width="100"  class="tableHeader" width="10%" allign="right">Name</td>
                                <td  width="100" class="tableHeader" width="10%" allign="right">Mobile</td>
                                <td  width="100" class="tableHeader" width="10%" allign="right">CNIC</td>
                                <td  width="100" class="tableHeader" width="10%" allign="right">Exp. Start Date</td>
                                <td  width="100" class="tableHeader" width="10%" allign="right">Exp. End Date</td>
                                <td  width="100" class="tableHeader" width="10%" allign="right">Action</td>

                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                </fieldset>
            </td>
        </tr>

    </table>
</html:form>

    <div style="visibility: hidden">
        <table id="grid">
            <tr id="templateRow">
                <td align="right"></td>
                <td></td>
                <td align="center"><input type="hidden" id="appUserId" name="appUserId" /></td>
                <td align="center"><input type="hidden" id="name" name="name" /></td>
                <td align="center"><input type="hidden" id="mobile" name="mobile" /></td>
                <td align="center"><input type="hidden" id="cnic" name="cnic" /></td>
                <td align="center"><input type="hidden" id="exemptionStateDate" name="exemptionStateDate"></td>
                <!-- <td align="center"> </td> -->
                <!-- <td align="center"><input type="hidden" id="exemptionEndDate" name="exemptionEndDate" /></td> -->
                <td align="center"><input type="button" value="Edit" id="editButton" name="editButton"  onclick="editWhtRule(this)"/></td>
            </tr>
        </table>
    </div>


    <script language="javascript" type="text/javascript">

        function onFormSubmit(theForm) {

            if(doRequired( theForm.userId, 'Agent Id' )
                    && doRequired( theForm.startDate, 'Exemption Start Date' )
                    && doRequired( theForm.endDate, 'Exemption End Date' )
                  	&& validateDateRange(theForm))
            {
            	 return true;
            }else 
            {
                return false;
            }
        }


        function doRequired( field, label )
        {
            if( field.value.trim() == '' || field.value.length == 0 )
            {
                alert( label + ' is required field.' );
                field.focus();
                return false;
            }
            return true;
        }

        function validateDateRange(form){
            var currentDate = "<%=PortalDateUtils.getServerDate()%>";
            var _fDate = form.startDate.value;
            var _tDate = form.endDate.value;
            var startlbl = "Exemption Start Date";
            var endlbl   = "Exemption End Date";

            var isValid = true;
            serverDate= getJsDate( currentDate );
            
            
            if(_fDate!=undefined && _fDate!="")
    		{
    			var fDate = getJsDate( _fDate );				
    			if(fDate < serverDate){
    				alert(startlbl+" should be current or future date");
    				isValid = false;		
    			}	
    		}
    		
    		if(_tDate!=undefined && _tDate!="" && isValid)
    		{
    			var tDate = getJsDate( _tDate );
    			
    			if(tDate < serverDate){
    				alert(endlbl+" should be current or future date.");
    				isValid = false;		
    			}
    		}

            if(_fDate!=undefined && _fDate!="" && _tDate!=undefined  && _tDate!="" && isValid)
            {
                var fDate = getJsDate( _fDate );
                var tDate = getJsDate( _tDate );

                if(!(fDate<=tDate)) {
                    alert(startlbl+" should be less than or equal to "+endlbl);
                    isValid = false;
                }
            }

            return isValid;
        }

        Calendar.setup({
            inputField : "startDate", // id of the input field
            button : "sDate", // id of the button
            showsTime : false,
            ifFormat : "%d/%m/%Y" 
        });

        Calendar.setup({
            inputField : "endDate", // id of the input field
            button : "eDate", // id of the button
            isEndDate : true,
            showsTime : false,
            ifFormat : "%d/%m/%Y" 

        });

    </script>
    <script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>

</body>

</html>




