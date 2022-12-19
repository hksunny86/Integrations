<!--Author: Asad hayathttp://www.leviton.com/sections/prodinfo/newprod/npleadin.htm-->
<%@ page import="com.inov8.microbank.common.util.PortalConstants,com.inov8.microbank.common.util.*"%>
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="decorator" content="decorator2">
	
<%--	<meta http-equiv="cache-control" content="no-cache" />--%>
<%--	<meta http-equiv="cache-control" content="no-store" />--%>
<%--	<meta http-equiv="cache-control" content="private" />--%>
<%--	<meta http-equiv="cache-control" content="max-age=0, must-revalidate" />--%>
<%--	<meta http-equiv="expires" content="now-1" />--%>
<%--	<meta http-equiv="expires" content="0" />--%>
<%--	<meta http-equiv="pragma" content="no-cache" />--%>
	
	    		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>		
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>	
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
        
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Link New Account" />
		<%@include file="/common/ajax.jsp"%>
		<script type="text/javascript">
		  
		  
		  function postFetchAccountInfo(){
		     //alert('In postFetchAccountInfo()..  After ajax');
		     disablecontrollsforpaymentMode(null);
		     accNoNicChanged();
		     return true;
		  }
		  function preFetchAccountInfo(){
		     //alert('In preFetchAccountInfo()..  Before ajax');
		     return true;
		  }
		  function accNoNicChanged(){
		      if(${bank}){
		          //alert("things have changed");
		          document.forms[0].btnSave.disabled = true;
		          document.forms[0].btnVerify.disabled = false;
		          if(document.getElementById('errorMessages')!=null)
         			{
						Element.hide('errorMessages');
         			}
		          
		      }
		  }
		  var isErrorOccured = false;
		  function preTitleFetch(){
               Element.hide('errorMessages');
   
               
		     //alert('In function preTitleFetch()');
		     var _nic = document.forms[0].nic;
		     var _accountNum = document.forms[0].accountNo;
		     var _mfsId = document.forms[0].mfsId;
		     var _cardNo = document.forms[0].cardNo;
			var _calButton = document.forms[0].calButton;
		     var _cardExpirayDate=document.forms[0].expiryDate;
		     var _numberHidden = document.forms[0].numberHidden;
		    
		     if(_mfsId.value==''){
		         alert('Please enter Agent ID');
		         _mfsId.focus();
		         		       
		        //$('errorMsg').innerHTML = "";
			   // $('successMsg').innerHTML = "";
			    //Element.hide('successMessages'); 
			    //Element.hide('errorMsg');
		        //Element.hide('errorMessages');
		         return false;
		     }
		     else if(_nic.value==''){
		         alert('Please enter NIC.');
		         _nic.focus();
		        
		        //$('errorMsg').innerHTML = "";
			    //$('successMsg').innerHTML = "";
			    //Element.hide('successMessages'); 
			    //Element.hide('errorMsg');
		        //Element.hide('errorMessages');
		         return false;
		     }
		     
		     else if(document.getElementById('paymentMode').value =='3'&& !isNumeric(_accountNum.value)&&  _accountNum.value=='' && _accountNum.disabled==false ){
		         alert('Please enter Account #');
		         _accountNum.focus();
		         		       
		        //$('errorMsg').innerHTML = "";
			    //$('successMsg').innerHTML = "";
			    //Element.hide('successMessages'); 
			    //Element.hide('errorMsg');
		        //Element.hide('errorMessages');
		         return false;
		     }
		     else if(_accountNum.disabled == true && _cardNo.disabled == false && _cardNo.value==''){
		        // here you have to do
		        alert('Please enter Card #');
		        _cardNo.focus();
		        return false;
		     }
		     
		     else if(_accountNum.disabled == true && document.getElementById('paymentMode').value =='1'&& _cardExpirayDate.disabled == false && _calButton.disabled == false && _cardExpirayDate.value==''){
		        // here you have to do
		        alert('Please enter expiry date');
		        _calButton.focus();
		        return false;
		     }
		     
		     else if (document.getElementById('paymentMode').value =='3'&&_accountNum.disabled == false && !isNumeric(_accountNum.value))
		    {
		      alert('Please enter valid Account No.');
		      _accountNum.focus();
		      return false;
		    }
		     

		     else{ 
		         if(_accountNum.disabled == false && _accountNum.value!=''){
		             _numberHidden.value = _accountNum.value;
		         }
		         else{
		             _numberHidden.value = _cardNo.value;
		         }
		         
		    
		    
		if(_nic.value.length < 13)
		{
 	 		alert('NIC can not be less than 13 characters.');
			_nic.focus();
			return;	 	
	 	}
	 	if(_nic.value.length > 13)
	 	{
 	 		alert('NIC can not be greater than 13 characters.');
			_nic.focus();
			return;	 	
	 	}
		    
		  
          Element.show('errorMessages');
		 
		  
         $('errorMessages').innerHTML = 'Working ...';
         isErrorOccured= false;
         if(document.getElementById('successMessages')!=null)
         {
        	
        	Element.hide('successMessages');
         }
        
         
		         return true;
		         
		     }
		    
		  }
		  
		  function postTitleFetch(){
		    Element.hide('errorMessages');
				var t = $F('message');
				if (t.substring(0,4)=='Your' || t.substring(0,8)=='Customer'|| t.substring(0,2)=='In'||t.substring(12,15)=='not'||t.substring(11,19)=='disabled')
				{
					isErrorOccured= true;
					Element.show('errorMessages');
					
					$('errorMessages').innerHTML = $F('message');
				}
		      if(!isErrorOccured){
		         if($('successMessages')!=null)
		             Element.hide('successMessages');
		         Element.show('errorMessages');
		         
		         
		         $('errorMessages').innerHTML = $F('message');
		         document.forms[0].btnSave.disabled = false;
                 document.forms[0].btnCancel.disabled = false;
		         document.forms[0].btnVerify.disabled = true;
		      }
		      		      
		  }	
			
		  function reportTitleFetchError(request, obj){
			isErrorOccured = true;
		      //Element.hide('successMessages');
              Element.show('errorMessages');
		      $('errorMessages').innerHTML = 'Customer profile does not exists.';
		      isErrorOccured = true;
		  }	
			
		  function disablecontrollsforpaymentMode(theForm){
		  
			var _cardtypePreSelection = document.getElementById("cardtypePreSelection").value;
						
			if (document.getElementById("paymentMode").value=="3" && ${veriflyRequired}  )
			
				{ // Bank Account case , verifly bank 
					
					document.getElementById("cardType").disabled=true;
					document.getElementById("cardNo").disabled=true;
					document.getElementById("expiryDate").disabled=true;
					document.getElementById("calButton").disabled=true;
					if(!document.getElementById("paymentMode").disabled)
					{
					document.getElementById("accountNo").disabled=false;
					}
					
					document.getElementById("cardType").value='';
					document.getElementById("cardNo").value='';
					document.getElementById("expiryDate").value='';	
					
				}

			else if (document.getElementById("paymentMode").value=="3" && !${veriflyRequired}  )
				{ // Bank Account case , non-verifly bank

					document.getElementById("cardType").disabled=true;
					document.getElementById("cardNo").disabled=true;
					document.getElementById("expiryDate").disabled=true;
					document.getElementById("calButton").disabled=true;
					document.getElementById("accountNo").value = '';
					document.getElementById("accountNo").disabled=true;
					document.getElementById("name").disabled=true;
					
					document.getElementById("cardType").value='';
					document.getElementById("cardNo").value='';
					document.getElementById("expiryDate").value='';	
					
					if(document.getElementById("savedAccountNick").value != ''){
						document.getElementById("name").value = document.getElementById("savedAccountNick").value;
						
											}								
						
				}

				
			else if (document.getElementById("paymentMode").value=="1" && ${veriflyRequired} )
				{ // Credit Card case , verifly bank
				    document.getElementById("accountNo").value = '';
					document.getElementById("accountNo").disabled=true;
					document.getElementById("cardType").disabled=false;
					document.getElementById("cardNo").disabled=false;
					//document.getElementById("calButton").disabled=false;
					document.getElementById("expiryDate").disabled=false;
					document.getElementById("calButton").disabled=false;
					document.getElementById("name").disabled=false;
				}


			else if (document.getElementById("paymentMode").value=="1" && !${veriflyRequired} )
				{ // Credit Card case , non-verifly bank.
				    document.getElementById("accountNo").value = '';
					document.getElementById("accountNo").disabled=true;
					document.getElementById("cardType").disabled=false;
					document.getElementById("cardNo").disabled=false;
					document.getElementById("expiryDate").disabled=false;
					document.getElementById("calButton").disabled=false;
					document.getElementById("name").disabled=false;
					
					if(document.getElementById("savedAccountNick").value == '' && document.getElementById("name").value != '' ){
						document.getElementById("savedAccountNick").value = document.getElementById("name").value;						
					}
					
					if(_cardtypePreSelection == "3"){
						document.getElementById("name").value = '';
					}
				}
			else if (document.getElementById("paymentMode").value=="2" && ${veriflyRequired})
				{ // Debit Card case , verifly bank.
				    document.getElementById("accountNo").value = '';
					document.getElementById("accountNo").disabled=true;
					document.getElementById("expiryDate").disabled=false;
					document.getElementById("calButton").disabled=false;
					document.getElementById("cardType").disabled=false;
					document.getElementById("cardNo").disabled=false;
					document.getElementById("name").disabled=false;
				}
				
			else if (document.getElementById("paymentMode").value=="2" && !${veriflyRequired} )
				{ // Debit Card case , non-verifly bank.
				    document.getElementById("accountNo").value = '';
					document.getElementById("accountNo").disabled=true;
					document.getElementById("expiryDate").disabled=false;
					document.getElementById("calButton").disabled=false;
					document.getElementById("cardType").disabled=false;
					document.getElementById("cardNo").disabled=false;
					document.getElementById("name").disabled=false;
					
					if(document.getElementById("savedAccountNick").value == '' && document.getElementById("name").value != '' ){
						document.getElementById("savedAccountNick").value = document.getElementById("name").value;						
					}
					
					if(_cardtypePreSelection == "3"){
						document.getElementById("name").value = '';
					}
				}
			else if (document.getElementById("paymentMode").value=="4" && ${veriflyRequired})
				{ // Credit Registry case , verifly bank.
				    document.getElementById("accountNo").value = '';
					document.getElementById("accountNo").disabled=true;
					document.getElementById("expiryDate").disabled=true;
					document.getElementById("calButton").disabled=true;
					document.getElementById("cardType").disabled=true;
					document.getElementById("cardNo").disabled=true;
					document.getElementById("name").disabled=false;
					
					document.getElementById("accountNo").value='';
					document.getElementById("expiryDate").value='';
					document.getElementById("calButton").value='';
					document.getElementById("cardType").value='';
					document.getElementById("cardNo").value='';
				}
				
			else if (document.getElementById("paymentMode").value=="4" && !${veriflyRequired} )
				{ // Credit Registry case , non-verifly bank.
				    document.getElementById("accountNo").value = '';
					document.getElementById("accountNo").disabled=true;
					document.getElementById("expiryDate").disabled=true;
					document.getElementById("calButton").disabled=true;
					document.getElementById("cardType").disabled=true;
					document.getElementById("cardNo").disabled=true;
					document.getElementById("name").disabled=false;
					
					if(document.getElementById("savedAccountNick").value == '' && document.getElementById("name").value != '' ){
						document.getElementById("savedAccountNick").value = document.getElementById("name").value;						
					}
					
					if(_cardtypePreSelection == "3"){
						document.getElementById("name").value = '';
					}
					
					document.getElementById("accountNo").value='';
					document.getElementById("expiryDate").value='';
					document.getElementById("calButton").value='';
					document.getElementById("cardType").value='';
					document.getElementById("cardNo").value='';
				}				
			else if (document.getElementById("paymentMode").value=="5" && ${veriflyRequired})
				{ // Cash case , verifly bank.
				    document.getElementById("accountNo").value = '';
					document.getElementById("accountNo").disabled=true;
					document.getElementById("expiryDate").disabled=true;
					document.getElementById("calButton").disabled=true;
					document.getElementById("cardType").disabled=true;
					document.getElementById("cardNo").disabled=true;
					document.getElementById("name").disabled=false;

					document.getElementById("accountNo").value='';
					document.getElementById("expiryDate").value='';
					document.getElementById("calButton").value='';
					document.getElementById("cardType").value='';
					document.getElementById("cardNo").value='';
				}
			else if (document.getElementById("paymentMode").value=="5" && !${veriflyRequired} )
				{ // Cash case , non-verifly bank.
					document.getElementById("accountNo").disabled=true;
					document.getElementById("expiryDate").disabled=true;
					document.getElementById("calButton").disabled=true;
					document.getElementById("cardType").disabled=true;
					document.getElementById("cardNo").disabled=true;
					document.getElementById("name").disabled=false;
					
					if(document.getElementById("savedAccountNick").value == '' && document.getElementById("name").value != '' ){
						document.getElementById("savedAccountNick").value = document.getElementById("name").value;						
					}
					
					if(_cardtypePreSelection == "3"){
						document.getElementById("name").value = '';
					}
					
					document.getElementById("accountNo").value='';
					document.getElementById("expiryDate").value='';
					document.getElementById("calButton").value='';
					document.getElementById("cardType").value='';
					document.getElementById("cardNo").value='';
				}				

			 	else if (document.getElementById("paymentMode").value=="")
			 	{
			 	document.getElementById("accountNo").value = '';
			 	document.getElementById("accountNo").disabled=true;
				document.getElementById("expiryDate").disabled=true;
				document.getElementById("calButton").disabled=true;
				document.getElementById("cardType").disabled=true;
				document.getElementById("cardNo").disabled=true;
			 	}
			 	else 
			 	{
			 	/*
			 	document.getElementById("accountNo").value = '';
			 	document.getElementById("accountNo").disabled=true;
				document.getElementById("expiryDate").disabled=true;
				document.getElementById("calButton").disabled=true;
				document.getElementById("cardType").disabled=true;
				document.getElementById("cardNo").disabled=true;
				*/
			 	}				
				
				document.getElementById("cardtypePreSelection").value = document.getElementById("paymentMode").value;
			}
			
			function check(theForm){
			
				//disablecontrollsforpaymentMode(theForm);

						if (document.getElementById("mfsId").value=="")
						{
						    alert ("Please enter Agent ID.");
							document.getElementById("mfsId").focus();
							return false;
						}
						else if (document.getElementById("nic").value=="")
						{
						    alert ("Please enter NIC.");
							document.getElementById("nic").focus();
							return false;
						}
						else if (document.getElementById("name").value==""  && ${veriflyRequired}  )
					    {
							alert ("Please enter Account Nick.");
							document.getElementById("name").focus();
							return false;
						}
						else if (document.getElementById("name").value==""  && !${veriflyRequired} &&
						 document.getElementById("paymentMode").value != "3"
						 )
					    {
							alert ("Please enter Account Nick.");
							document.getElementById("name").focus();
							return false;
						}
						
						else if (document.getElementById("paymentMode").value!="")
						{
							if (document.getElementById("paymentMode").value=="3")
							{
									
								if ( !document.getElementById("accountNo").disabled && document.getElementById("accountNo").value=="")
								{
									
									alert ("Please enter Account #.");
									document.getElementById("accountNo").focus();
								    return false;
								}
							}
						 	else if (document.getElementById("paymentMode").value=="2")
							{
							
								if ( !document.getElementById("cardType").disabled && document.getElementById("cardType").value=="")
								{
									alert ("Please select Card Type.");
									document.getElementById("cardType").focus();
									return false;
								}
								if (!document.getElementById("cardNo").disabled && document.getElementById("cardNo").value=="")
								{
									alert ("Please enter Card #.");
									document.getElementById("cardNo").focus();
									return false;
								}
								if (!document.getElementById("expiryDate").disabled && document.getElementById("expiryDate").value=="")
								{
									alert ("Please enter Expiry Date.");
									document.getElementById("expiryDate").focus();
									return false;
								}	
							}
							else if (document.getElementById("paymentMode").value=="1")
							{
								
								if ( !document.getElementById("cardType").disabled && document.getElementById("cardType").value=="")
								{
									alert ("Please select Card Type.");
									document.getElementById("cardType").focus();
									return false;
								}
								if (!document.getElementById("cardNo").disabled && document.getElementById("cardNo").value=="")
								{
									alert ("Please enter Card #.");
									document.getElementById("cardNo").focus();
									return false;
								}
								if (!document.getElementById("expiryDate").disabled && document.getElementById("expiryDate").value=="")
								{
									alert ("Please enter Expiry Date.");
									document.getElementById("expiryDate").focus();
									return false;
								}				
							}
						}
						else
						{
							alert ("Please select Account Type.");
							document.getElementById("paymentMode").focus();
							return false;
						}
			return true;
			}
			
			function popupCallback(src,popupName,columnHashMap)
            {
                document.forms.linkpaymentmodeForm.nic.readOnly = true;
                if(src=="mfsId")
                {
                   document.forms.linkpaymentmodeForm.mfsId.value = columnHashMap.get('userId');                   
                   document.forms.linkpaymentmodeForm.firstName.value = columnHashMap.get('firstName');
                   document.forms.linkpaymentmodeForm.lastName.value = columnHashMap.get('lastName');
                   document.forms.linkpaymentmodeForm.mobileNo.value = columnHashMap.get('mobileNo');
                   document.forms.linkpaymentmodeForm.appUserId.value = columnHashMap.get('appUserId');
               
                   if( columnHashMap.get('nic') == 'null' ){
                      document.forms.linkpaymentmodeForm.nic.value = '';
                      document.forms.linkpaymentmodeForm.nic.readOnly = false;
                   }
                   else{
                      document.forms.linkpaymentmodeForm.nic.value = columnHashMap.get('nic');
                   }
                   accNoNicChanged();
                }
            }
            function clearMfsId(){
                document.forms.linkpaymentmodeForm.nic.readOnly = true;
                document.forms.linkpaymentmodeForm.mfsId.value = '';
                document.forms.linkpaymentmodeForm.firstName.value = '';
                document.forms.linkpaymentmodeForm.lastName.value = '';
                document.forms.linkpaymentmodeForm.mobileNo.value = '';
                document.forms.linkpaymentmodeForm.nic.value = '';
                document.forms.linkpaymentmodeForm.appUserId.value = '';
            }
		</script>
		
	</head>
	<body onload="disablecontrollsforpaymentMode(this);" >
		<%-- <spring:bind path="linkPaymentModeModel.*">
			<c:if test="${not empty status.errorMessages}">
				<div class="errorMsg">
					<c:forEach var="error" items="${status.errorMessages}">
						<c:out value="${error}" escapeXml="false" />
						<br />
					</c:forEach>
				</div>
			</c:if>
		</spring:bind>
		--%>
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages" >
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>

        <div class="infoMsg" id="errorMessages" style="display:none;"></div>
		<table width="100%" border="0" cellpadding="0" cellspacing="1">

			<html:form id="linkpaymentmodeForm" method="post"
				action="p_linkpaymentmodeform.html"
				commandName="linkPaymentModeModel"
				onsubmit="return onFormSubmit(this)">

                <input type="hidden" id="message" name="message" value=""/>
                <input type="hidden" id="errorMesg" name="errorMesg" value=""/>
				<input type="hidden" name="savedAccountNick" id="savedAccountNick" value="" />
				<input type="hidden" name="cardtypePreSelection" id="cardtypePreSelection" value="" />
				<input type="hidden" name="isUpdate" id="isUpdate" value="false" />
				<input type="hidden" name="isDefaultAccount" id="isDefaultAccount"
					value="0" />
				<input type="hidden" name="<%=PortalConstants.KEY_USECASE_ID%>"
					value="<%=PortalConstants.LINK_PAYMENT_MODE_USECASE_ID%>" />
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
					value="1" />
				<%-- 
				<input type="hidden" name="eappUserId" value="${appUserId}">
                --%>
                <html:hidden path="appUserId"/>
				<tr bgcolor="FBFBFB">
					<td colspan="2" align="center">
						&nbsp;
					</td>
				</tr>
				
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Agent ID:
					</td>
					<td align="left">
					    <c:choose>
						   <c:when test="${empty param.appUserId}" >
						   	  <authz:authorize ifAnyGranted="<%=PortalConstants.LNK_PAY_MOD_CREATE%>">
								  <html:input path="mfsId" id="mfsId"  onkeypress="return maskNumber(this,event)" cssClass="textBox" maxlength="8" tabindex="2" readonly="true" />
						          <input name="mfsIdLookupButton" type="button" value="-o" class="button" onClick="javascript: callLookup('mfsId','userInfoListViewMfsPopup',400,200)" />
						          <img id="mfsIdClear" name="popMfsId" align="middle" title="Clear Agent ID" onclick="javascript: clearMfsId();"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
							  </authz:authorize>						  
					  		  <authz:authorize ifNotGranted="<%=PortalConstants.LNK_PAY_MOD_CREATE%>">
						          <html:input path="mfsId" id="mfsId"  onkeypress="return maskNumber(this,event)" cssClass="textBox" maxlength="8" tabindex="2" readonly="true"/>
						          <input name="mfsIdLookupButton" type="button" value="-o" class="button" onClick="javascript:callLookup('mfsId','userInfoListViewMfsPopup',400,200)" disabled="disabled"/>
						          <img id="mfsIdClear" name="popMfsId" align="middle" title="Clear Inov8 MWallet ID" onclick="javascript: //clearMfsId();"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0"/>
							  </authz:authorize>											      
					       </c:when>
					       <c:otherwise>
					          <html:input path="mfsId" id="mfsId"  onkeypress="return maskNumber(this,event)" cssClass="textBox" maxlength="8" tabindex="2" readonly="true"/>
					          <input name="mfsIdLookupButton" type="button" value="-o" class="button" onClick="javascript:callLookup('mfsId','userInfoListViewMfsPopup',400,200)" disabled="disabled"/>
					          <img id="mfsIdClear" name="popMfsId" align="middle" title="Clear Agent ID" onclick="javascript: //clearMfsId();"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0"/>
					       </c:otherwise>
					    </c:choose>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						First Name:
					</td>
					<td align="left">
						<input value="${txtFirstName}" id="firstName"  onkeypress="return maskNumber(this,event)" class="textBox" readonly="readonly"/>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Last Name:
					</td>
					<td align="left">
						<input value="${txtLastName}" id="lastName"  onkeypress="return maskNumber(this,event)" class="textBox" readonly="readonly"/>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Mobile #:
					</td>
					<td align="left">
						<input value="${txtMobileNo}" id="mobileNo"  onkeypress="return maskNumber(this,event)" class="textBox" readonly="readonly"/>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>NIC:
					</td>
					<td align="left">
					

						   
					    <authz:authorize ifAnyGranted="<%=PortalConstants.LNK_PAY_MOD_CREATE%>">
					    
					 <c:choose>
						   <c:when test="${empty param.appUserId}" >
						   
							    <html:input path="nic" id="nic"  onkeypress="return maskNumber(this,event)" cssClass="textBox" maxlength="13" tabindex="3" disabled="false" onchange="javascript: accNoNicChanged();"/>
						   </c:when>
					       <c:otherwise>
					       
					       <input value="${txtNic}" id="nic"  onkeypress="return maskNumber(this,event)" class="textBox" maxlength="13" readonly="readonly"/>
						   
					       </c:otherwise>
					  </c:choose>     
					  
					     </authz:authorize>		
					     <authz:authorize ifNotGranted="<%=PortalConstants.LNK_PAY_MOD_CREATE%>">
					     
						<html:input path="nic" id="nic"  onkeypress="return maskNumber(this,event)" cssClass="textBox" maxlength="13" tabindex="3" disabled="true" />					     
					     </authz:authorize>
					     
					</td>
				</tr>
				
				<tr>
					<td height="16" colspan="2" align="right" bgcolor="FBFBFB"
						class="formText">
						&nbsp;
					</td>
				</tr>
				
				<tr>
					<td height="16" colspan="2" align="right" bgcolor="FBFBFB"
						class="formText">
						&nbsp;
					</td>
				</tr>
				
				<tr>
					<td height="16" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Account Nick: 
					</td>
					<td width="58%" align="left" bgcolor="FBFBFB">
						    <authz:authorize ifAnyGranted="<%=PortalConstants.LNK_PAY_MOD_CREATE%>">
				       <html:input path="name"  onkeypress="return maskCommon(this,event)"  id="name" cssClass="textBox" tabindex="4" maxlength="35"/>
				       							</authz:authorize>
				 <authz:authorize ifNotGranted="<%=PortalConstants.LNK_PAY_MOD_CREATE%>">
				 <html:input path="name" disabled="true"  onkeypress="return maskCommon(this,event)"  id="name" cssClass="textBox" tabindex="4" maxlength="35"/>
				 </authz:authorize>
					</td>
				</tr>

				
				
				
				    <c:if test="${bank}">
				        
				        
				        <tr>
					       <td height="16" align="right" bgcolor="F3F3F3" class="formText">
						      Account Type: 
					       </td>
					       <td width="58%" align="left" bgcolor="FBFBFB">
                               <html:select path="accountType" cssClass="textBox" id="accountType" tabindex="5"
							         >
							         <c:forEach items="${accountTypeModelList}" var="accountTypeModel">
								         <html:option value="${accountTypeModel.accountTypeId}">
                                             ${accountTypeModel.name}</html:option>
							         </c:forEach>
						       </html:select>
					       </td>
				        </tr>
				        
				        <tr>
					       <td height="16" align="right" bgcolor="F3F3F3" class="formText">
						      Currency Code: 
					       </td>
					       <td width="58%" align="left" bgcolor="FBFBFB">

                               <html:select path="currencyCode" cssClass="textBox" id="currencyCode" tabindex="6"
							        onchange="javascript: accNoNicChanged();">
							         <c:forEach items="${currencyCodeModelList}" var="currencyCodeModel">
								         <html:option value="${currencyCodeModel.currencyCodeId}">
                                             ${currencyCodeModel.name}</html:option>
							         </c:forEach>
						       </html:select>&nbsp;
                               
                              
					           
					       </td>
				        </tr>
				        
				       
				    </c:if>
				
				
				
				
				<tr>
					<td height="16" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Account Type:
					</td>
					<td width="58%" align="left" bgcolor="FBFBFB">
			    <authz:authorize ifAnyGranted="<%=PortalConstants.LNK_PAY_MOD_CREATE%>">
						<html:select path="paymentMode" cssClass="textBox" tabindex="7" 
						id="paymentMode" onchange="disablecontrollsforpaymentMode(this)">
						<%-- <html:option value=""></html:option> --%>
							<c:forEach items="${PaymentModeModelList}"
								var="PaymentModeModelList">
								<html:option value="${PaymentModeModelList.paymentModeId}">
                       ${PaymentModeModelList.name}                       </html:option>
							</c:forEach>
						</html:select>
						</authz:authorize>
				 <authz:authorize ifNotGranted="<%=PortalConstants.LNK_PAY_MOD_CREATE%>">
					<html:select path="paymentMode" cssClass="textBox" tabindex="7" disabled="true"
						id="paymentMode" onchange="disablecontrollsforpaymentMode(this)">
						<%-- <html:option value=""></html:option> --%>
							<c:forEach items="${PaymentModeModelList}"
								var="PaymentModeModelList">
								<html:option value="${PaymentModeModelList.paymentModeId}">
                       ${PaymentModeModelList.name}                       </html:option>
							</c:forEach>
						</html:select>
				 
				 </authz:authorize>
						
					</td>
				</tr>
				
				<tr>
					       <td height="16" align="right" bgcolor="F3F3F3" class="formText">
						      Account #: 
					       </td>
					       <td align="left" bgcolor="FBFBFB">
					          <authz:authorize ifAnyGranted="<%=PortalConstants.LNK_PAY_MOD_CREATE%>">
						         <html:input path="accountNo" id="accountNo" cssClass="textBox" tabindex="8" onkeypress="return maskNumber(this,event)"
							        maxlength="50" disabled="false" onchange="javascript: accNoNicChanged();"/>
							
							  </authz:authorize>
				              <authz:authorize ifNotGranted="<%=PortalConstants.LNK_PAY_MOD_CREATE%>">
				                 <html:input path="accountNo" id="accountNo" cssClass="textBox" tabindex="8" onkeypress="return maskNumber(this,event)"
							        maxlength="50" disabled="true"  />
				                 </authz:authorize>
					       </td>
                              <input type="hidden" name="numberHidden" id="numberHidden" value=""/>
				        </tr>
				
				
				           
				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Card #:
					</td>
					<td align="left" bgcolor="FBFBFB">
						<html:input path="cardNo" id="cardNo" cssClass="textBox" onkeypress="return maskNumber(this,event)"
							maxlength="50" disabled="true" tabindex="9" onchange="javascript: accNoNicChanged();"/>
					</td>
				</tr>

				

				<tr>
					<td height="16" align="right" bgcolor="F3F3F3" class="formText">
						Card Type:
					</td>
					<td align="left" bgcolor="FBFBFB">
						<html:select path="cardType" cssClass="textBox" id="cardType" tabindex="10"
							disabled="true">
 							<html:option value=""></html:option> 
							<c:forEach items="${CardTypeModelList}" var="CardTypeModelList">
								<html:option value="${CardTypeModelList.cardTypeId}">
                       ${CardTypeModelList.name}                       </html:option>
							</c:forEach>
						</html:select>
					</td>
				</tr>
       
           
				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Expiry Date:
					</td>
					<td align="left" bgcolor="FBFBFB">
						<html:input path="expiryDate" id="expiryDate" readonly="true" tabindex="11" 
							cssClass="textBox" maxlength="20" disabled="true" />
					<img id="calButton"  name="popcal" tabindex="12"   align="top" style="cursor:pointer" src="images/cal.gif" border="0"/>
					<img id="expiryDateD" name="popcal" tabindex="13"  align="middle" title="Clear Date" onclick="javascript:$('expiryDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
						

					</td>
				</tr>


				<tr>
					<td height="16" colspan="2" align="right" bgcolor="FBFBFB"
						class="formText">
						&nbsp;
					</td>
				</tr>
				
				

				<tr>
					<td height="16" align="right" bgcolor="FBFBFB" class="formText">
					
										  <c:if test="${bank}">
					 <input type="button" id="btnVerify" name="btnVerify" class="button" value="Verify Title"  tabindex="14" />
					                <ajax:updateField
									  baseUrl="${contextPath}/p_fetchaccountinfo.html"
									  source="nic"
									  target="message"
									  action="btnVerify"
									  parser="new ResponseXmlParser()"
									  parameters="nic={nic},currencyCode={currencyCode},accountType={accountType},cardNum={cardNo},expirayDate={expiryDate},accountNum={numberHidden},mfsId={mfsId},actionType=1"
									  preFunction="preTitleFetch"
									  postFunction="postTitleFetch"
									  errorFunction="reportTitleFetchError"
									  />
						 <ajax:select 
				            source="accountType" 
				            target="paymentMode"
						    baseUrl="${contextPath}/p_fetchaccountinfo.html"
							parameters="accountType={accountType},actionType=2"  
							errorFunction="reportTitleFetchError" 
							preFunction="preFetchAccountInfo"
							postFunction="postFetchAccountInfo"
							executeOnLoad="true"
						/>			  
									  
				</c:if>
					
					</td>
					<td width="58%" align="left" bgcolor="FBFBFB">
					  

					  
					  <authz:authorize ifAnyGranted="<%=PortalConstants.LNK_PAY_MOD_CREATE%>">
							<input id="btnSave" name="btnSave" type="button" class="button" value="Link Mode" tabindex="15"
									onclick="javascript:submitForm();" />					  
					  </authz:authorize>						  
					  <authz:authorize ifNotGranted="<%=PortalConstants.LNK_PAY_MOD_CREATE%>">
					         <input id="btnSave" name="btnSave" type="button" class="button" value="Link Mode" tabindex="15"
									disabled="disabled"  />
					  </authz:authorize>										

							
					<c:choose>
						<c:when test="${empty param.appUserId}" >	
							<input id="btnCancel" name="btnCancel" type="button" class="button" value="Cancel" tabindex="16"
									onclick="javascript:window.location='p_linkpaymentmodeform.html'" />
					    </c:when>   
				    	<c:otherwise>
							<input id="btnCancel" name="btnCancel" type="button" class="button" value="Cancel" tabindex="16"
									onclick="javascript:window.location='p_linkpaymentmodeform.html'" />
						</c:otherwise>			
					</c:choose>

					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td colspan="2" align="center">
						&nbsp;
					</td>
				</tr>

				<tr>
					<td>
			</html:form>
		</table>
        
		<script type="text/javascript">
		
		<c:if test="${bank}">
		var accountType= document.getElementById("accountType");
		var u  =accountType.options;
		u[4].selected='selected';
		
		</c:if>
	
		
		
		
		
		
		
Element.hide('errorMessages');

	highlightFormElements();
	if(document.getElementById('nic').value==''){
	   document.getElementById('nic').readOnly = false;
	}
	if(!document.forms[0].name.disabled){	
		document.forms[0].name.focus();
	}
	
	
	 document.getElementById("cardtypePreSelection").value = document.getElementById("paymentMode").value;
	 
	disablecontrollsforpaymentMode(document.getElementById("linkpaymentmodeForm"));
	
    if( ${bank} ){
       document.forms[0].btnSave.disabled = true;
       //document.forms[0].btnCancel.disabled = true;
    }
	
function onFormSubmit(theForm) {
	
	  	checkNumericData();
	  	
		var currServerDate = '<%=PortalDateUtils.currentFormattedDate("MM/yy")%>';
		var _expiryDate  = trim(document.forms[0].expiryDate.value);

	 _name = theForm.name;
	 _mfsId = theForm.mfsId;
	 _accountNo = theForm.accountNo;
	 _cardNo  = theForm.cardNo;
	 _nic  = theForm.nic;
	 
	 if(_mfsId.value != '' && !isNumberWithZeroes(_mfsId) ){
	 	 	alert('Please enter valid Agent ID.');
 			_mfsId.focus();
 			return;	 	
	 }
	 
 	 if(_nic.value != '' && !isNumberWithZeroes(_nic) ){
 	 	alert('Please enter valid NIC.');
		_nic.focus();
		return;	 	
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
	 
	 if( !_accountNo.disabled &&  _accountNo.value != '' && !isNumberWithZeroes(_accountNo) ){
	 	 	alert('Please enter valid Account No.');
 			_accountNo.focus();
 			return;	 	
	 }

	 if( !_cardNo.disabled && _cardNo.value != '' && !isNumberWithZeroes(_cardNo) ){
	 	 	alert('Please enter valid Card No.');
 			_cardNo.focus();
 			return;	 	
	 }


		if( !document.forms[0].expiryDate.disabled &&  _expiryDate != '' && (isDateGreaterOrEqualForCard(currServerDate, _expiryDate))){
			alert('Please select future date.');
			return;
		}

	if (!check(theForm))// client side validation required
	{
		return false;
	}
	else
	{
	  	if(!validateFormChar(theForm)){
	    
      		return false;
      	}
	
		if( confirm('If customer information is verified then press OK to continue') )
		    return /*validateLinkPaymentModeModel(theForm)*/ true;
		else
		    return false;
	}
	
	
//	return /*validateLinkPaymentModeModel(theForm)*/ true;
	
}

      Calendar.setup(
      {
        inputField  : "expiryDate", // id of the input field
        ifFormat    : "%m/%y",      // the date format
        button      : "calButton"    // id of the button
      }
      );


	function submitForm(){
			checkNumericData();
			//alert('Going to call onSave(document.forms.linkpaymentmodeForm,null)');
			onSave(document.forms.linkpaymentmodeForm,null);
	}
function checkNumericData(){
	if (! IsNumeric(document.forms.linkpaymentmodeForm.nic.value)){
		alert ("NIC must contain numeric data");
		return false;
	}
	
}

function IsNumeric(sText)

{
   var ValidChars = "0123456789";
   var IsNumber=true;
   var Char;

 
   for (i = 0; i < sText.length && IsNumber == true; i++) 
      { 
      Char = sText.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) 
         {
         IsNumber = false;
         }
      }
   return IsNumber;
   
   }

</script>
		<v:javascript formName="linkPaymentModeModel" staticJavascript="false" />
		<script type="text/javascript"
			src="<c:url
value="/scripts/validator.jsp"/>"></script>




	</body>
</html>

