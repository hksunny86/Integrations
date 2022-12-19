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
			<meta name="title" content="Edit Branchless Banking Account"/>
        </c:when>
        <c:otherwise>
	      <meta name="title" content="New Branchless Banking Account"/>
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
	  
        <html:form id="OLACreateEditAccount" name="OLACreateEditAccount" commandName="olaVo"  onsubmit="return onFormSubmit(this)"
         action="p_OLACreateEditAccount.html">
<%--   change needed        --%>
           <c:if test="${not empty param.accountId}">  
             <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
             <html:hidden  path="accountId" />
           </c:if>						          

           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>First Name:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
<%--                 <c:choose>--%>
<%--		            <c:when test="${not empty param.accountId}">--%>
<%--	                    <html:input  path="firstName" tabindex="1" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" readonly="true"/>--%>
<%--                    </c:when>--%>
<%--                    <c:otherwise>--%>
	                    <html:input id="firstName" path="firstName" tabindex="1" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"  />
<%--      	            </c:otherwise>--%>
<%--                 </c:choose>--%>
             </td>
           </tr>
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Middle Name:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
<%--                 <c:choose>--%>
<%--		            <c:when test="${not empty param.accountId}">--%>
<%--	                    <html:input  path="middleName" tabindex="1" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" readonly="true"/>--%>
<%--                    </c:when>--%>
<%--                    <c:otherwise>--%>
	                    <html:input id = "middleName" path="middleName" tabindex="2" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"  />
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
	                    <html:input id = "lastName" path="lastName"  tabindex="3" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"  />
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
	                    <html:input  id = "fatherName" path="fatherName"  tabindex="4" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"  />
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
	                    <html:input id = "landlineNumber" path="landlineNumber" cssClass="textBox" tabindex="5"  maxlength="10" onkeypress="return maskNumber(this,event)"  />

             </td>
           </tr>
           
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Contact # Mobile:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
	                    <html:input id = "mobileNumber" path="mobileNumber" cssClass="textBox" tabindex="6"  maxlength="11" onkeypress="return maskNumber(this,event)"  />

             </td>
           </tr>
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Permanent Address:</td>
             <td align="left" bgcolor="FBFBFB">
                <html:textarea id = "address" path="address" cssClass="textBox"  tabindex="7" onkeypress="return maskCommon(this,event)"  onkeyup="textAreaLengthCounter(this,250);"   />
             </td>
           </tr>                     
<tr>
             <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Date Of Birth:</td>
             <td align="left" bgcolor="FBFBFB">
             <html:input readonly="true" path="dob" id="dob" cssClass="textBox"  tabindex="8" onkeypress="return maskCommon(this,event)"  onkeyup="textAreaLengthCounter(this,250);"   />
             <img id="popcal" tabindex="9" name="popcal" align="top"
							style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="sDate" tabindex="10" name="popcal" title="Clear Date"
							onclick="javascript:$('dob').value=''" align="middle"
					style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
             </td>
           </tr>                     
		<c:choose>
		<c:when test="${not empty param.accountId}">           
 		<tr> 
             <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Inov8 Credit Account Number:</td>
             <td align="left" bgcolor="FBFBFB">
             <html:input id ="payingAccNo" path="payingAccNo" cssClass="textBox"  tabindex="11" onkeypress="return maskNumber(this,event)"  maxlength="11"  />
             </td>
           </tr>
 		</c:when>
 		</c:choose>
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>NIC #:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
               <html:input id = "cnic" path="cnic" cssClass="textBox"  tabindex="12" onkeypress="return maskNumber(this,event)"   
             				maxlength="13"  />
                </td>
           </tr>
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Account Status:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
                          
               <html:select path="statusId" cssClass="textBox"  tabindex="13" onkeypress="return maskNumber(this,event)"  >
               <html:option value="1" label="Active"/>
               <html:option value="2" label="Inactive"/> 
               <html:option value="3" label="Blocked"/>               
             	</html:select>			  
                </td>
           </tr>
           <c:choose>
           <c:when test="${not empty param.accountId}">
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText">Account Balance:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
               <html:input id = "balance" path="balance" cssClass="textBox"  tabindex="14" onkeypress="return maskNumber(this,event)"   
             				maxlength="13"  />
                </td>
           </tr>
           </c:when>
           </c:choose>
		    <tr>
             <td height="16" colspan="2" align="right" bgcolor="FBFBFB" class="formText">&nbsp;</td>
           </tr>		              

           <tr bgcolor="FBFBFB">
           
          <c:choose>
           <c:when test="${not empty param.accountId}">
			<c:choose>
           <c:when test="${isSaved}">
			<td colspan="2" align="center"><input type = "submit" value="Update" class ="button" disabled="disabled"/>           
			<input type = "reset" value="Cancel" class ="button" disabled="disabled"/></td>
			</c:when>
			<c:otherwise>			
             <td colspan="2" align="center"><input type = "submit" onclick="return onFormSubmit(this)" value="Update" class ="button" />
             <input type = "reset" value="Cancel" class ="button"/></td>
             </c:otherwise>
             </c:choose>
			
			</c:when>
			<c:otherwise>			
           <c:choose>
           <c:when test="${isSaved}">
			<td colspan="2" align="center"><input type = "submit" value="Save" class ="button" disabled="disabled"/>           
			<input type = "reset" value="Cancel" class ="button" disabled="disabled"/></td>
			</c:when>
			<c:otherwise>			
             <td colspan="2" align="center"><input type = "submit" onclick="return onFormSubmit(this)" value="Save" class ="button" />
             <input type = "reset" value="Cancel" class ="button"/></td>
             </c:otherwise>
             </c:choose>
             
             </c:otherwise>
             </c:choose>
           
           
           
           </tr>
           
        

<%--             <td colspan="2" align="center"><GenericToolbar:toolbar formName="OLACreateEditAccount"/></td>--%>

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
      
      <c:if test="${not empty param.accountId}">
      
		document.getElementById("balance").readOnly = true;
		document.getElementById("payingAccNo").readOnly = true;
		
		</c:if>
      
      <c:if test="${isSaved}">


      	_form = document.forms[0];
      	
	 _form.firstName.value="";
	 _form.lastName.value="";
     _form.mobileNumber.value="";
     _form.middleName.value="";
     _form.fatherName.value="";
    _form.landlineNumber.value="";
     _form.address.value="";
     _form.dob.value="";
     _form.cnic.value="";
     

      </c:if>
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

      	_form = document.forms[0];
      	_firstName 	= _form.firstName;
      	_lastName 	= _form.lastName;
      	_mobileNo 	= _form.mobileNumber;
      	_middleName 	= _form.middleName;
      	_fatherName 	= _form.fatherName;
      	_landlineNumber 	= _form.landlineNumber;
      	_address 	= _form.address;
      	_dob = _form.dob;
      	_nic = _form.cnic;
      	
      	_firstName.value = trim(_firstName.value);
      	_lastName.value = trim(_lastName.value);
      	_mobileNo.value = trim(_mobileNo.value);
      _middleName.value 	= trim(_middleName.value);
      	_fatherName.value 	= trim(_fatherName.value);
      	_landlineNumber.value 	= trim(_landlineNumber.value);
      	_address.value 	= trim(_address.value);
      _dob.value = trim(_dob.value);
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
		if(_middleName.value == ''){
 			alert('Please enter Middle Name.');
 			_middleName.focus();
 			return false;
 		}	
 		
 		if(!isAlphaWithSpace(_middleName)){
 			alert('Please enter correct Middle Name.');
 			_middleName.focus();
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

		if(_fatherName.value == ''){
 			alert('Please enter Father Name.');
 			_fatherName.focus();
 			return false;
 		}	
 		
 		if(!isAlphaWithSpace(_fatherName)){
 			alert('Please enter correct Father Name.');
 			_fatherName.focus();
 			return false;
 		}

		if(_landlineNumber.value == ''){
 			alert('Please enter Land line  #.');
 			_landlineNumber.focus();
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
 		
 		if(_address.value == ''){
 			alert('Please enter Address');
 			_address.focus();
 			return false;
 		}	
 		
 		if(_dob.value == ''){
 			alert('Please enter the date of birth.');
 			_dob.focus();
 			return false;
 		}
 		if( trim(_dob.value) != '' && isDateGreater(_dob.value,currServerDate)){
			alert('Future date of birth is not allowed.');
			_dob.focus();
			return false;
		}
 		
 		if( _nic.value == ''){
 			alert('Please enter NIC #.');
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





