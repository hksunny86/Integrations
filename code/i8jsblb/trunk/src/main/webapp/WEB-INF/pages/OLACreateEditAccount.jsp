<!--Author: Mohammad Shehzad Ashraf-->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>  
<meta name="decorator" content="decorator">
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
       
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <c:choose>
		<c:when test="${not empty param.accountId}">
			<meta name="title" content="Edit OLA Account"/>
        </c:when>
        <c:otherwise>
	      <meta name="title" content="New OLA Account"/>
      	</c:otherwise>
      </c:choose>
      
      <script type="text/javascript">
         
      </script>
      
     
</head>

   <body bgcolor="#ffffff"  >
   
   		<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<div id="errorMsg" class ="errorMsg" style="display:none;"></div>
   
<%--      <spring:bind path="mfsAccountModel.*">--%>
<%--	  <c:if test="${not empty status.errorMessages}">--%>
<%--	    <div class="errorMsg">--%>
<%--	      <c:forEach var="error" items="${status.errorMessages}">--%>
<%--	        <c:out value="${error}" escapeXml="false"/>--%>
<%--	        <br/>--%>
<%--	      </c:forEach>--%>
<%--	    </div>--%>
<%--	  </c:if>--%>
<%--	</spring:bind>--%>
	
	<c:if test="${not empty messages}">
	    <div class="infoMsg" id="successMessages">
	        <c:forEach var="msg" items="${messages}">
	            <c:out value="${msg}" escapeXml="false"/><br/>
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>				
      <table width="100%" border="0" cellpadding="0" cellspacing="1">
	  
        <html:form name="OLACreateEditAccount" commandName="olaVo"  onsubmit="return onFormSubmit(this)"
         action="p_OLACreateEditAccount.html">
<%--   change needed        --%>
           <c:if test="${not empty param.accountId}">  
             <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
           </c:if>						          

           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>First Name:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
<%--                 <c:choose>--%>
<%--		            <c:when test="${not empty param.accountId}">--%>
<%--	                    <html:input  path="firstName" tabindex="1" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" readonly="true"/>--%>
<%--                    </c:when>--%>
<%--                    <c:otherwise>--%>
	                    <html:input  path="firstName" tabindex="1" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"  />
<%--      	            </c:otherwise>--%>
<%--                 </c:choose>--%>
             </td>
           </tr>
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText">Middle Name:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
<%--                 <c:choose>--%>
<%--		            <c:when test="${not empty param.accountId}">--%>
<%--	                    <html:input  path="middleName" tabindex="1" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" readonly="true"/>--%>
<%--                    </c:when>--%>
<%--                    <c:otherwise>--%>
	                    <html:input  path="middleName" tabindex="1" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"  />
<%--      	            </c:otherwise>--%>
<%--                 </c:choose>--%>
             </td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Last Name:</td>
             <td align="left">
<%--                 <c:choose>--%>
<%--		            <c:when test="${not empty param.accountId}">--%>
<%--	                    <html:input  path="lastName"  tabindex="2" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" readonly="true"  />--%>
<%--                    </c:when>--%>
<%--                    <c:otherwise>--%>
	                    <html:input  path="lastName"  tabindex="2" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"  />
<%--      	            </c:otherwise>--%>
<%--                 </c:choose>--%>
             </td>
           </tr>
			<tr bgcolor="FBFBFB">
             <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Father's Name:</td>
             <td align="left">
<%--                 <c:choose>--%>
<%--		            <c:when test="${not empty param.accountId}">--%>
<%--	                    <html:input  path="fatherName"  tabindex="2" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" readonly="true"  />--%>
<%--                    </c:when>--%>
<%--                    <c:otherwise>--%>
	                    <html:input  path="fatherName"  tabindex="2" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"  />
<%--      	            </c:otherwise>--%>
<%--                 </c:choose>--%>
             </td>
           </tr>
<%--           <tr>--%>
<%--             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>CNIC #:</td>--%>
<%--             <td width="58%" align="left" bgcolor="FBFBFB">--%>
<%--	                    <html:input  path="cnicNo" cssClass="textBox" tabindex="3"  maxlength="11" onkeypress="return maskNumber(this,event)"  />--%>
<%--             </td>--%>
<%--           </tr>--%>

		<tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Contact # Landline:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
	                    <html:input  path="landlineNumber" cssClass="textBox" tabindex="3"  maxlength="10" onkeypress="return maskNumber(this,event)"  />

             </td>
           </tr>
           
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Contact # Mobile:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
	                    <html:input  path="mobileNumber" cssClass="textBox" tabindex="3"  maxlength="11" onkeypress="return maskNumber(this,event)"  />

             </td>
           </tr>
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Permanent Address:</td>
             <td align="left" bgcolor="FBFBFB">
                <html:textarea path="address" cssClass="textBox"  tabindex="5" onkeypress="return maskCommon(this,event)"  onkeyup="textAreaLengthCounter(this,250);"   />
             </td>
           </tr>                     
<tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Date Of Birth:</td>
             <td align="left" bgcolor="FBFBFB">
             <html:input readonly="true" path="dob" id="dob" cssClass="textBox"  tabindex="5" onkeypress="return maskCommon(this,event)"  onkeyup="textAreaLengthCounter(this,250);"   />
             <img id="popcal" tabindex="4" name="popcal" align="top"
							style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="sDate" tabindex="5" name="popcal" title="Clear Date"
							onclick="javascript:$('dob').value=''" align="middle"
					style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
             </td>
           </tr>                     
		<c:choose>
		<c:when test="${not empty param.accountId}">           
 		<tr> 
             <td align="right" bgcolor="F3F3F3" class="formText">Inov8 Credit Account Number:</td>
             <td align="left" bgcolor="FBFBFB">
             <html:input path="payingAccNo" cssClass="textBox"  tabindex="5" onkeypress="return maskNumber(this,event)"  maxlength="11"  />
             </td>
           </tr>
 		</c:when>
 		</c:choose>
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>NIC #:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
               <html:input path="cnic" cssClass="textBox"  tabindex="5" onkeypress="return maskNumber(this,event)"   
             				maxlength="13"  />
                </td>
           </tr>
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Account Status:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
                          
               <html:select path="statusId" cssClass="textBox"  tabindex="5" onkeypress="return maskNumber(this,event)"  >
               <html:option value="1" label="Active"/>
               <html:option value="2" label="Inactive"/>
               <html:option value="3" label="Blocked"/>
             	</html:select>			  
                </td>
           </tr>
           <c:choose>
           <c:when test="${not empty param.accountId}">
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Account Balance:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
               <html:input path="balance" cssClass="textBox"  tabindex="5" onkeypress="return maskNumber(this,event)"   
             				maxlength="13"  />
                </td>
           </tr>
           </c:when>
           </c:choose>
		    <tr>
             <td height="16" colspan="2" align="right" bgcolor="FBFBFB" class="formText">&nbsp;</td>
           </tr>		              

           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
           
        <tr><td>
        <tr >
             <td colspan="2" align="center"><GenericToolbar:toolbar formName="OLACreateEditAccount"/></td>
           </tr>
		</html:form>
      </table>
   <%--    <c:choose>
		<c:when test="${not empty param.appUserId}">
	--%>		

<%--
        </c:when>
        <c:otherwise>
      	</c:otherwise>
      </c:choose>
--%>
      <script language="javascript" type="text/javascript">



      
      <c:choose>
		<c:when test="${not empty param.accountId}">
        </c:when>
        <c:otherwise>
	      function onCancel(a, b)
          {
             document.forms[0].reset();
          }
      	</c:otherwise>
      </c:choose>
         
      function submitForm(theForm)
      {
			
		var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';

      	_form = theForm;
      	_firstName 	= _form.firstName;
      	_lastName 	= _form.lastName;
      	_mobileNo 	= _form.mobileNo;
      	_city 		= _form.city;
      	_state 		= _form.state;
      	_country 	= _form.country;
      	_connectionType = _form.connectionType;
      	_dob = _form.dob;
      	_nic = _form.nic;
      	
      	_firstName.value = trim(_firstName.value);
      	_lastName.value = trim(_lastName.value);
      	_mobileNo.value = trim(_mobileNo.value);
      	_city.value = trim(_city.value);
      	_state.value = trim(_state.value);
      	_country.value = trim(_country.value);
		_nic.value = trim(_nic.value);
 		
 		if(_firstName.value == ''){
 			alert('Please enter First Name.');
 			_firstName.focus();
 			return false;
 		}	
 		
 		if(!isAlphaWithSpace(_firstName)){
 			alert('Please enter correct First Name.');
 			_firstName.focus();
 			return false;
 		}	

 		if(_lastName.value == ''){
 			alert('Please enter Last Name.');
 			_lastName.focus();
 			return false;
 		}		

 		if(!isAlphaWithSpace(_lastName)){
 			alert('Please enter correct Last Name.');
 			_lastName.focus();
 			return false;
 		}	

		if(_mobileNo.value == ''){
 			alert('Please enter Mobile #.');
 			_mobileNo.focus();
 			return false;
 		}		

 		if(!isNumberWithZeroes(_mobileNo) || _mobileNo.value.length < 11 ){
 			alert('Please enter correct Mobile #.');
 			_mobileNo.focus();
 			return false;
 		}

		if(_connectionType.value == ''){
 			alert('Please select Connection Type.');
 			_connectionType.focus(); 			
 			return false;
 		}	

		//city
 		if( _city.value != '' && !isAlphaWithSpace(_city)){
 			alert('Please enter valid city.');
 			_city.focus();
 			return false;
 		}

		//state
 		if( _state.value != '' && !isAlpha(_state)){
 			alert('Please enter valid state.');
 			_state.focus();
 			return false;
 		}

		//country
 		if( _country.value != '' && !isAlphaWithSpace(_country)){
 			alert('Please enter valid country.');
 			_country.focus();
 			return false;
 		}

<%--		//dob	--%>
<%--		if( trim(_dob.value) != '' &&  !isDate1(_dob)){--%>
<%--	//		alert('Please enter valid date.');--%>
<%--			_dob.focus();--%>
<%--			return;--%>
<%--		}--%>
<%--        --%>
<%--		if( trim(_dob.value) != '' && isDateGreater(_dob.value,currServerDate)){--%>
<%--			alert('Future date of birth is not allowed.');--%>
<%--			_dob.focus();--%>
<%--			return;--%>
<%--		}--%>
		
		//nic
 		if( _nic.value == ''){
 			alert('Please enter valid NIC #.');
 			_nic.focus();
 			return false;
 		}
 				
 		if( _nic.value != '' && !isNumberWithZeroes(_nic)){
 			alert('Please enter valid NIC #.');
 			_nic.focus();
 			return false;
 		}		
      	if(_nic.value.length < 13){
 	 		alert('NIC can not be less than 13 characters.');
			_nic.focus();
			return;	 	
		 }
		 if(_nic.value.length > 13){
 	 		alert('NIC can not be greater than 13 characters.');
			_nic.focus();
			return;	 	
		 }
      	if(!validateFormChar(theForm)){
      		return false;
      	}
		
		      	
      	//submitting form
        if (confirm('Are you sure you want to proceed?')==true)
        {
          return true;
        }
        else
        {
          return false;
        }
  }
      
      function onFormSubmit(theForm)
      {
		 
      	if(submitForm(theForm)){
      		return true;
	    }else{
		    return false;
	    }
	    
        // return validatemfsAccountModel(theForm); 
      }
      
<%--      Calendar.setup(--%>
<%--      {--%>
<%--        inputField  : "dob", // id of the input field--%>
<%--        ifFormat    : "%d/%m/%Y",      // the date format--%>
<%--        button      : "calButton"    // id of the button--%>
<%--      }--%>
<%--      );--%>
       Calendar.setup(
      {
        inputField  : "dob", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "popcal"    // id of the button
      }
      );

     
      </script>

      <v:javascript formName="mfsAccountModel" staticJavascript="false"/>
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"> </script>
   </body>
</html>





