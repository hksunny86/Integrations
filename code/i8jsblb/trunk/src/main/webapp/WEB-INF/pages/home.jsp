<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<%@page import="com.inov8.microbank.common.model.portal.collectionpaymentsmodule.CollectionPaymentsViewModel"%>
<%@page import="com.inov8.microbank.common.model.portal.transactiondetailmodule.ExtendedTransactionAllViewModel"%>
<%@page import="com.inov8.microbank.common.util.UserUtils"%>

<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="contextPath" scope="request">${pageContext.request.contextPath}</c:set>
<c:set var="appUserId"><%=UserUtils.getCurrentUser().getAppUserId() %></c:set>

<html>
<head>
<meta name="decorator" content="decorator">
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
	<script language="javascript">
        if (performance.navigation.type == performance.navigation.TYPE_RELOAD) {
            defineTabID();
            console.info("Page is reload")
        } else {
            console.info( "page is not reloaded");
        }
		
		function openAgentWindow() {

			var url = '${contextPath}'+'/agentweblogin.aw?UID=${mfsId}';
			
			/* var newurl = 'agentweblogin.aw?UID=${mfsId}';
			window.location=newurl; */

			//agentWindow = window.open(url,"BB_Agent_Application", "location=1, left=0, top=0, width="+screen.availWidth+", height="+screen.availHeight+", status=no, scrollbars=no, titlebar=no, toolbar=no, menubar=no, resizable=yes,  target=_self");
            //agentWindow = window.open(url, "BB_Agent_Application", '', 'postwindow');
			/* agentWindow.moveTo(screen.availLeft, screen.availTop);
			agentWindow.resizeTo(screen.availWidth, screen.availHeight);

			agentWindow.focus(); */
		}
        function openAdvanceFeatureWindow(appUserId) {
            var url = 'https://zindigi-zmiles.jsbl.com:8443/#/?appUserId=${appUserId}';
            advanceFeatureWindow = window.open(url, "Advance Features", '', 'postwindow');
        }

        setTimeout(function(){
            document.getElementById("successMessages").style.display = "none";
        },7000);

	</script>

	<meta http-equiv="cache-control" content="no-cache" />
<%--	<meta http-equiv="cache-control" content="no-store" />--%>
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="pragma" content="no-cache" />
<title>
Welcome to i8Microbank -
</title>

	<%
		/* String ctxpath1 = contextPath; */
	
		System.out.println("appUsrIDDD :"+request.getAttribute("appUserTypeId"));


		if( (request.getAttribute("appUserTypeId").toString().equals("3")) || (request.getAttribute("appUserTypeId").toString().equals("12")))
		{
			
			System.out.println("contextPath :"+request.getContextPath());
            System.out.println("MFS ID :"+request.getAttribute("mfsId"));
			response.sendRedirect(request.getContextPath()+"/agentweblogin.aw?UID="+request.getAttribute("mfsId"));
		}

	
	
		String customerPermission = PortalConstants.HOME_PAGE_QUICK_SEARCH_CUST_READ;
		customerPermission += "," + PortalConstants.CSR_GP_READ;

		String agentPermission =  PortalConstants.CSR_GP_READ;
		agentPermission +=	"," + PortalConstants.MNO_GP_READ;
		agentPermission += "," + PortalConstants.HOME_PAGE_QUICK_SEARCH_AGNT_READ;

		String handlerPermission =  PortalConstants.CSR_GP_READ;
		handlerPermission +=	"," + PortalConstants.MNO_GP_READ;
		handlerPermission += "," + PortalConstants.HOME_PAGE_QUICK_SEARCH_HANDLER_READ;

		String searchPermission = customerPermission+","+agentPermission+","+handlerPermission;

		String walkInCustSearchPermission = PortalConstants.PG_GP_READ + "," + PortalConstants.HOME_PAGE_QUICK_SEARCH_CASH_PAYMENT_READ + "," + PortalConstants.CSR_GP_READ;

		String quickSearchCompPermission = PortalConstants.CSR_GP_READ;
		quickSearchCompPermission += "," + PortalConstants.HOME_PAGE_QUICK_SEARCH_COMP_READ;

		String quickAddCompPermission = PortalConstants.CSR_GP_CREATE;
		quickAddCompPermission += "," + PortalConstants.ADD_COMP_CREATE;

		String quickSearchCollPayPermission = PortalConstants.PRS_GP_READ;
		quickSearchCollPayPermission += "," + PortalConstants.HOME_PAGE_QUICK_SEARCH_COLL_P_READ;

		String cencernPermission = PortalConstants.MNO_GP_READ + "," + PortalConstants.HOME_PAGE_QUICK_MY_CNCRN_READ;

		String agentTxHistPermission = PortalConstants.RET_GP_READ;
		agentTxHistPermission += "," + PortalConstants.HOME_PAGE_QUICK_AGNT_TX_HIST_READ;

		String viewAgentWebAppPermission = PortalConstants.RET_GP_READ;
		viewAgentWebAppPermission += "," + PortalConstants.HOME_PAGE_VIEW_AGNT_WEB_APP_READ;

		String custSignUpPermission = PortalConstants.RET_GP_CREATE;
		custSignUpPermission += "," + PortalConstants.HOME_PAGE_QUICK_BB_CUST_CREATE;

		String quickLinksPermission = PortalConstants.MNO_GP_READ + "," + PortalConstants.RET_GP_READ + "," + PortalConstants.HOME_PAGE_QUICK_MY_CNCRN_READ;
		quickLinksPermission += "," + PortalConstants.HOME_PAGE_QUICK_AGNT_TX_HIST_READ;
		quickLinksPermission += "," + PortalConstants.HOME_PAGE_VIEW_AGNT_WEB_APP_READ;
		quickLinksPermission += "," + PortalConstants.HOME_PAGE_QUICK_BB_CUST_CREATE;
		
		String txSearchPermission = PortalConstants.PG_GP_READ + "," + PortalConstants.HOME_PAGE_QUICK_SEARCH_TX_READ + "," + PortalConstants.MNO_GP_READ + "," + PortalConstants.CSR_GP_READ;

		String quickSearchPermission = searchPermission + "," + txSearchPermission + "," + walkInCustSearchPermission + "," + quickSearchCompPermission + "," + quickAddCompPermission + "," + quickSearchCollPayPermission;
	 %>

	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/home_style.css" type="text/css"/>


</head>
<body bgcolor="#FFFFFF">
<c:if test="${not empty messages}">
	<div class="infoMsg" id="successMessages">
		<c:forEach var="msg" items="${messages}">
			<c:out value="${msg}" escapeXml="false" />
			<br />
		</c:forEach>
	</div>
	<c:remove var="messages" scope="session" />
</c:if>

<c:if test="${appUserTypeId == 6 || appUserTypeId == 7 || appUserTypeId == 3 || appUserTypeId == 12 || appUserTypeId == 1 || appUserTypeId == 5}">


<div class="block_container col-lg-8">

    <div class="block_row" onmouseenter="defineTabID()">
    	
        
        <html:form name="homeSearchHome" commandName="homeSearchModel"	action="home.html">
            <input type="hidden" name="actionId" value="<%=PortalConstants.ACTION_RETRIEVE%>">
            <input type="hidden" value="" name="customerOrAgent" id="customerOrAgent" />
            <authz:authorize ifAnyGranted="<%=searchPermission%>">
                <div class="col-lg-12">
                    <div class="block_box">
                        <authz:authorize ifAnyGranted="<%=customerPermission%>">
                        	 <authz:authorize ifNotGranted="<%=agentPermission%>">
                        	 	<authz:authorize ifNotGranted="<%=handlerPermission%>">
	                        		<div class="box_title blue2">Search Customer</div>
	                        	</authz:authorize>
	                        </authz:authorize>
                        </authz:authorize>    
                        <authz:authorize ifAnyGranted="<%=agentPermission%>">
                        	<authz:authorize ifNotGranted="<%=customerPermission%>">
                        		<authz:authorize ifNotGranted="<%=handlerPermission%>">
	                        		<div class="box_title blue2">Search Agent</div>
	                        	</authz:authorize>
	                        </authz:authorize>
                        </authz:authorize>
                        <authz:authorize ifAnyGranted="<%=handlerPermission%>">
                        	<authz:authorize ifNotGranted="<%=customerPermission%>">
                        		<authz:authorize ifNotGranted="<%=agentPermission%>">
	                        		<div class="box_title blue2">Search Handler</div>
	                        	</authz:authorize>
	                        </authz:authorize>
                        </authz:authorize>
                        <authz:authorize ifAnyGranted="<%=customerPermission%>">
                        	 <authz:authorize ifAnyGranted="<%=agentPermission%>">
                        	 	<authz:authorize ifAnyGranted="<%=handlerPermission%>">
	                        		<div class="box_title blue2">Search Customer / Agent / Handler</div>
	                        	</authz:authorize>
	                        </authz:authorize>
                        </authz:authorize>      
                        <div class="box_inner">
                            
                            <div class="inner-row">
                                <h3>Search Criteria:</h3>
                                <input id="nic" name="searchCriteria" type="radio" value="false" title="NIC" onClick="setMaxLength(this, 13)" 
                                    <c:if test="${homeSearchModel.searchCriteria == false}">checked="checked"</c:if> />
                                    <label for="nic">NIC</label>
                                            
                                <input id="mobileNo" name="searchCriteria" type="radio" value="true" title="Mobile No" onClick="setMaxLength(this, 11)"
                                    <c:if test="${homeSearchModel.searchCriteria == true}">checked="checked"</c:if> />
                                    <label for="mobileNo">Mobile No</label>
                            </div>
                            
                            <authz:authorize ifAnyGranted="<%=customerPermission%>">
                            <div class="inner-row">
                                <h3>Customer:</h3>
                                <html:input path="customerSearch" tabindex="8" cssClass="textBox" maxlength="13" onkeypress="return maskNumber(this,event)"/>&nbsp;
                                <input type="button" class="button"	value="Search Customer" tabindex="9" onClick="javascript:checkCustomerData()" />
                            </div>
                            </authz:authorize>
                            
                            <authz:authorize ifAnyGranted="<%=agentPermission%>">
                            <div class="inner-row">
                                <h3>Agent:</h3>
                                <html:input path="agentSearch" tabindex="8" cssClass="textBox" maxlength="13" onkeypress="return maskNumber(this,event)"/>&nbsp;
                                <input type="button" class="button" value="Search Agent" tabindex="9" onClick="javascript:checkAgentData()" />
                            </div>
                            </authz:authorize>
                            
                            <authz:authorize ifAnyGranted="<%=handlerPermission%>">
                            <div class="inner-row">
                                <h3>Handler:</h3>
                                <html:input path="handlerSearch" tabindex="8" cssClass="textBox" maxlength="13" onkeypress="return maskNumber(this,event)"/>&nbsp;
                                <input type="button" class="button" value="Search Handler" tabindex="9" onClick="javascript:checkHandlerData()" />
                            </div>
                            </authz:authorize>
                            
                        </div> <!-- ENd BOX INNER -->
                    </div>
                </div>
            </authz:authorize>
        </html:form>
        
        
        
        <authz:authorize ifAnyGranted="<%=txSearchPermission%>">
			<%request.setAttribute( "extendedTransactionAllViewModel", new ExtendedTransactionAllViewModel() );%>
            <div class="col-lg-12" onmouseenter="defineTabID()">
                <div class="block_box">
                    <div class="box_title blue2">Search Transaction</div>
                    <div class="box_inner">
                    	<html:form name='transactionDetailI8Form' commandName="extendedTransactionAllViewModel" method="post" action="p_alltransactionsmanagement.html" onsubmit="return isValidTransactionId(this.transactionCode.value);" >
                            <div class="inner-row">
                                <h3>Transaction ID:</h3>
                                <html:input path="transactionCode" id="transactionCode" cssClass="textBox" tabindex="10" maxlength="12" onkeypress="return maskInteger(this,event)"/> 
                                ${status.errorMessage}
                                <input name="_search" type="submit" class="button" value="Search Transaction" tabindex="11" />
                                <input type="hidden" id="tabvalue" value="AQ" onclick="defineTabID()"/>
                            </div>
                            <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
                    	</html:form>
                    </div>
                </div>
            </div>
            <div class="inner-row">
                <input type="button" class="button" value="Advance Configurations" onClick="return openAdvanceFeatureWindow('${appUserId}')"/>
            </div>
        </authz:authorize>



        <authz:authorize ifAnyGranted="<%=walkInCustSearchPermission%>">
        	<form name='walkInCustomerTxForm' method="get" action="p_cashwithdrawaltransdetails.html" onSubmit="return false;" >
            <div class="col-lg-12">
                <div class="block_box">
                    <div class="box_title blue2">Search Cash Payment</div>
                    <div class="box_inner">
                        <div class="inner-row">
                            <h3>Sender CNIC:</h3>
                            <input type="text" id="customerCnic" class="textBox" tabindex="12" maxlength="13" onKeyPress="return maskNumber(this,event)"/>
							<input type="button" class="button" value="Search Cash Payment" name="CashPayment" tabindex="13" onClick="JavaScript:searchCashPayments()"/>
                        </div>
                    </div>
                </div>
            </div>
            </form>
        </authz:authorize>
        
        
        
        <authz:authorize ifAnyGranted="<%=quickSearchCompPermission%>">
        	<form name='complaintForm' method="get" action="p_billpaycomplaints.html" onSubmit="return false;" >
            <div class="col-lg-12">
                <div class="block_box">
                    <div class="box_title blue2">Search Complaint</div>
                    <div class="box_inner">
                        <div class="inner-row">
                            <h3>Transaction ID / Consumer No:</h3>
                            <input type="text" id="consumerNo" class="textBox" tabindex="12" maxlength="20" onKeyPress="return maskNumber(this,event)"/>
							<input type="button" class="button" value="Search Complaint" name="searchComplaint" tabindex="13" onClick="JavaScript:searchComplaints()"/>
                        </div>
                    </div>
                </div>
            </div>
            </form>
        </authz:authorize>
        
        
        
        <authz:authorize ifAnyGranted="<%=quickAddCompPermission%>">
        	<form name='addComplaintForm' method="get" action="p_complaintform.html" onSubmit="return false;" >
            <div class="col-lg-12">
                <div class="block_box">
                    <div class="box_title blue2">Add Complaint</div>
                    <div class="box_inner">
                        <div class="inner-row">
                            <h3>CNIC:</h3>
                            <input type="text" id="walkinCnic" class="textBox" tabindex="12" maxlength="13" onKeyPress="return maskNumber(this,event)"/>
							<input type="button" class="button" value="Add Complaint" name="addComplaint" tabindex="13" onClick="JavaScript:addComplaints()"/>
                        </div>
                    </div>
                </div>
            </div>
            </form>
        </authz:authorize>
        
        
        
        <authz:authorize ifAnyGranted="<%=quickSearchCollPayPermission%>">
			<%request.setAttribute( "collectionPaymentsViewModel", new CollectionPaymentsViewModel() );%>
            <div class="col-lg-12">
                <div class="block_box">
                    <div class="box_title">Search Collection Payment</div>
                    <div class="box_inner">
                    	<html:form name="collectionPaymentsViewForm" commandName="collectionPaymentsViewModel" method="post" action="p_collectionpaymenttransactions.html">
                            <div class="inner-row">
                                <h3>Transaction ID:</h3>
                                <html:input path="transactionCode" id="transactionCode" cssClass="textBox" maxlength="20" tabindex="1" onkeypress="return maskInteger(this,event)"/>
                            </div>
                            <div class="inner-row">
                                <h3>Order ID:</h3>
                                <html:input path="consumerNo" id="consumerNo" cssClass="textBox" maxlength="20" tabindex="2" onkeypress="return maskAlphaNumeric(this,event)"/>
                            </div>
                            <div class="inner-row">
                                <input name="_search" type="submit" class="button" value="Search Collection Payment" tabindex="3"/>
                            </div>
                            <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
                    	</html:form>
                    </div>
                </div>
            </div>
        </authz:authorize>
        
        
        
    </div> <!-- ENd Block_ROW -->
    
</div> <!-- ENd Block_CONTAINER -->
     <c:if test="${appUserTypeId != 3 && appUserTypeId != 12}">
    <authz:authorize ifAnyGranted="<%=quickLinksPermission%>">
    <div class="block_container col-lg-4">
    
            <div class="block_row">
                <div class="col-lg-12">
                	<div class="block_box q_links">
            			
                        <div class="box_title blue2">Quick Links</div>
                        <div class="box_inner">
                            <authz:authorize ifAnyGranted="<%=cencernPermission%>">
                                <div class="inner-row">
                                    <input type="button" class="button" value="My Concerns" onClick="javascript:window.location='p_myconcerns.html?actionId=2'"/>
                                </div>
                            </authz:authorize>
                            
                            <authz:authorize ifAnyGranted="<%=agentTxHistPermission%>">
                                <div class="inner-row">
                                    <!-- for the time being commenting out this button-->
                                    <%--<input type="button" class="button" value="View My Transactions" onClick="javascript:document.location='allpayagenttransactiondetails.html?agentId=${mfsId}&actionId=2'" />--%>
                                </div>
                            </authz:authorize>
                            
                            <%-- <authz:authorize ifAnyGranted="<%=viewAgentWebAppPermission%>">
                                <div class="inner-row">
                                    <input type="button" class="button" value="Launch BB Agent Application" onClick="javascript:openAgentWindow()"/>
                                </div>
                            </authz:authorize> --%>
                            
                            <authz:authorize ifAnyGranted="<%=custSignUpPermission%>">
                                <div class="inner-row">
                                    <input type="button" class="button" value="New Customer SignUp" onClick="javascript:window.location='p_mnonewmfsaccountform.html?actionId=1'"/>
                                </div>
                            </authz:authorize>
                        </div>
                    </div>	
                </div>
            </div>
    	
    </div>
    </authz:authorize>
    </c:if>
    <!-- Agent and Handler -->
    <c:if test="${appUserTypeId == 3 || appUserTypeId == 12}">
	    
	    <div class="block_container col-lg-4">
	    
	            <div class="block_row">
	                <div class="col-lg-12">
	                	<div class="block_box q_links">
	            			
	                        <div class="box_title blue2">Quick Links</div>
	                        <div class="box_inner">
	                            <authz:authorize ifAnyGranted="<%=cencernPermission%>">
	                                <div class="inner-row">
	                                    <input type="button" class="button" value="My Concerns" onClick="javascript:window.location='p_myconcerns.html?actionId=2'"/>
	                                </div>
	                            </authz:authorize>
	                            
		                            
                                <div class="inner-row">
                                    <input type="button" class="button" value="Launch BB Agent Application" onClick="javascript:openAgentWindow()"/>
                                </div>
	                            
	                            
	                            
	                        </div>
	                    </div>	
	                </div>
	            </div>
	    	
	    </div>
    
    </c:if>
    
</c:if>



        <script language="javascript" type="text/javascript">
            function defineTabID() {
                var iPageTabID = sessionStorage.getItem("tabID");
                if (iPageTabID == null) {
                    var iLocalTabID = localStorage.getItem("tabID");
                    var iPageTabID = (iLocalTabID == null) ? 1 : Number(iLocalTabID) + 1;
                    localStorage.setItem("tabID", iPageTabID);
                    sessionStorage.setItem("tabID", iPageTabID);
                    document.getElementById("tabvalue").setAttribute('value', iPageTabID);
                    //alert("Tab id is ::: " + iPageTabID);
                }
                else {
                    document.getElementById("tabvalue").setAttribute('value', iPageTabID);
                    //alert("Tab id is ::: " + iPageTabID+" AQ");
                }
            }

            function isValidTransactionId( transactionId )
        	{
        		var isValid = true;
        		if( transactionId == null || transactionId == '' )
        		{
        			alert( 'Transaction ID is required.' );
        			isValid = false;
        		}
        		return isValid;
        	}

        	function searchCashPayments()
        	{
        		var customerCnic = walkInCustomerTxForm.customerCnic.value;
        		if( isValidCustomerCnic("Sender CNIC", customerCnic ) )
        		{
        			javascript:document.location='p_cashwithdrawaltransdetails.html?actionId=2&home=true&customerCnic=' + customerCnic;
        		}
        	}

        	function searchComplaints()
        	{
        		var consumerNo = complaintForm.consumerNo.value;
        		if( isValidConsumerNo( consumerNo ) )
        		{
        			javascript:document.location='p_billpaycomplaints.html?actionId=2&consumerno=' + consumerNo;
        		}
        	}

        	function addComplaints()
        	{	
        		var cnic = addComplaintForm.walkinCnic.value;
        		if( isValidCustomerCnic( 'CNIC', cnic ) )
        		{
        			javascript:document.location='p_complaintform.html?actionId=1&cnic=' + cnic;
        		}
        	}
        	
        	function isValidCustomerCnic( fieldName, cnic )
        	{
        		
        		var isValid = true;
        		if( cnic == null || cnic == '' )
        		{
        			alert( fieldName + ' is required.' );
        			isValid = false;
        		}
        		else if( cnic.length != 13 )
       			{
        			alert( fieldName + ' length should be 13 digits.' );
        			isValid = false;
       			}
        		return isValid;
        	}

        	function isValidConsumerNo( consumerNo )
        	{
        		var isValid = true;
        		if( consumerNo == null || consumerNo == '' )
        		{
        			alert( 'Consumer No is required.' );
        			isValid = false;
        		}
        		return isValid;
        	}

        	var lastSelectedCriteriaBtn = document.getElementById('nic');

        	function checkCustomerData(){
        		var isValid = true;
        		var customerSearch = document.getElementById('customerSearch');

        		if(customerSearch != null && customerSearch.value != ''){
        			customerSearch = trim(customerSearch.value);
        			if(customerSearch == ''){
        				alert('Please enter Customer Mobile No or NIC');
        				isValid = false;
        			}
        			else
        			{
        				var nicChecked = document.getElementById('nic').checked;
        				if( nicChecked )
        				{
        					if( customerSearch.length < 13 )
        					{
        						alert( 'NIC length should be 13 digits.' );
        						isValid = false;
        					}
        				}
        				else
        				{
        					if( customerSearch.length < 11 )
        					{
        						alert( 'Mobile No length should be 11 digits' );
        						isValid = false;
        					}
        				}
        			}
        		}
        		else
        		{
        			alert('Please enter Customer Mobile No or NIC');
        			isValid = false;
        		}

				if( isValid )
       			{
       				document.getElementById('customerOrAgent').value = 'Customer';
       				document.forms[0].submit();
       			}
        	}
        	function checkAgentData(){
        		var isValid = true;
        		var agentSearch = document.getElementById('agentSearch');

        		if(agentSearch != null && agentSearch.value != ''){
        			agentSearch = trim(agentSearch.value);
        			if(agentSearch == ''){
        				alert('Please enter Agent Mobile No or NIC');
        				isValid = false;
        			}
        			else
        			{
        				var nicChecked = document.getElementById('nic').checked;
        				if( nicChecked )
        				{
        					if( agentSearch.length < 13 )
        					{
        						alert( 'NIC length should be 13 digits.' );
        						isValid = false;
        					}
        				}
        				else
        				{
        					if( agentSearch.length < 11 )
        					{
        						alert( 'Mobile No length should be 11 digits' );
        						isValid = false;
        					}
        				}
        			}
        		}
        		else
        		{
        			alert('Please enter Agent Mobile No or NIC');
        			isValid = false;
        		}

				if( isValid )
       			{
       				document.getElementById('customerOrAgent').value = 'Agent';
       				document.forms[0].submit();
       			}
        	}
        	
        	function checkHandlerData(){
        		var isValid = true;
        		var handlerSearch = document.getElementById('handlerSearch');

        		if(handlerSearch != null && handlerSearch.value != ''){
        			handlerSearch = trim(handlerSearch.value);
        			if(handlerSearch == ''){
        				alert('Please enter Handler Mobile No or NIC');
        				isValid = false;
        			}
        			else
        			{
        				var nicChecked = document.getElementById('nic').checked;
        				if( nicChecked )
        				{
        					if( handlerSearch.length < 13 )
        					{
        						alert( 'NIC length should be 13 digits.' );
        						isValid = false;
        					}
        				}
        				else
        				{
        					if( handlerSearch.length < 11 )
        					{
        						alert( 'Mobile No length should be 11 digits' );
        						isValid = false;
        					}
        				}
        			}
        		}
        		else
        		{
        			alert('Please enter Handler Mobile No or NIC');
        			isValid = false;
        		}

				if( isValid )
       			{
       				document.getElementById('customerOrAgent').value = 'Handler';
       				document.forms[0].submit();
       			}
        	}

			function setMaxLength( criteriaRadioBtn, length )
			{
				if( lastSelectedCriteriaBtn != criteriaRadioBtn )
				{
					var customerSearchTxtField = document.getElementById( 'customerSearch' );
					if( customerSearchTxtField != null )
					{
						customerSearchTxtField.value = "";
						customerSearchTxtField.maxLength = length;
					}

					var agentSearchTxtField = document.getElementById( 'agentSearch' );
					if( agentSearchTxtField != null )
					{
						agentSearchTxtField.value = "";
						agentSearchTxtField.maxLength = length;
					}
				}				

				lastSelectedCriteriaBtn = criteriaRadioBtn;
			}


/* window.location='${contextPath}'+'/agentweblogin.aw?UID=${mfsId}'; */

        </script>
</body>

</html>
