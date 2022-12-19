<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page	import="com.inov8.microbank.common.util.CommandFieldConstants" %>
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Pay Cash</title>
	  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,IE=11,IE=10"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js">

    </script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/jquery.navgoco.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/demo.css" type="text/css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.cookie.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.cookie.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.navgoco.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.navgoco.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/agentWebMenu.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/highlight.pack.js"></script>
    <style type="text/css">
        * {
            box-sizing: border-box;
        }

        .header {
            border: 1px solid red;
            padding: 15px;
        }

        .vertical-left-menu {
            width: 20%;
            float: left;
            padding: 15px;
            overflow-y: scroll;
            height: 500px;

        }

        .main-body-area {
            width: 80%;
            float: left;
            padding: 15px;
            height: 430px;

        }
    </style>	
<script language="javascript">

	function writeError(message) {
		var msg = document.getElementById('msg');
		msg.innerHTML = message;
		msg.className = 'error-msg';
		return false;
	}
	
	
	function validateNumber(field){
	
		if(field.value==""){
			return false; 
		}
		
		var charpos = field.value.search("[^0-9]"); 
		if(charpos >= 0) { 
			return false; 
		} 
		return true;
	}
	
	function validate() {
		
		var TXCD = document.getElementById('TXCD');		

		if(!validateNumber(TXCD) || TXCD < 5) {
			return writeError('Kindly Enter 5 Digit Transaction Code.');		
		}		
		
		return true;
	}
</script>			
			
	</head>
	<body oncontextmenu="return false">
	<div style="height:95px%">
	    <jsp:include page="header.jsp"></jsp:include>
	</div>
	<div class="vertical-left-menu ">
	    <jsp:include page="agentWebMenu.jsp"></jsp:include>
	</div>
	<div class="main-body-area">
	
		<table align="center" border="0" cellpadding="0" cellspacing="0" height="100%" width="85%" class="container">
			
			<tr id="contents" valign="top">
				<td>
					<form method="post" action="<c:url value="/payCashTransactionCodeVerification.aw"/>" onsubmit="return validate()">			
						
						<table align="center" width="80%" border="0">
							<tr>
								<td align="center" colspan="2" id="msg">
									&nbsp;
									<c:if test="${not empty errors}">
										<font color="#FF0000">
											<c:out value="${errors}" />
											<br/>
										</font>
									</c:if>					
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center">
									<c:if test="${empty OTP or (not empty OTP and OTP == 1)}">
										<div align="center" class="hf">Transaction Code Verification</div><br>
									</c:if>
									<c:if test="${not empty OTP and OTP == 0}">
										<div align="center" class="hf">Receiver Biometric Verification</div><br>
									</c:if>
								</td>
							</tr>
							<tr>
								<c:if test="${not empty OTP and OTP == 1}">
									<td align="right" width="50%"><span class="label">Transaction Code:</span></td>
									<td align="left"  width="50%"><input type="text" id="<%= CommandFieldConstants.KEY_ONE_TIME_PIN %>" name="<%= CommandFieldConstants.KEY_ONE_TIME_PIN %>" size="8" maxlength="5" class="text"/></td>
								</c:if>
							</tr>
							
							
							<tr>
	                    		<td align="right" width="50%">
	
		                        	<div>
		                            	<input type="button" value="Enter Thumb Image" id="buttonBVS">
		                        	</div>
	
	                    		</td>
	                    		<td>
	
	                        	<div id="divBVSImage" style="display: none;">
	                            <img id="OutputImage" id='file' name="OutputImage" alt="Finger Print" src=""
	                                 width="142"
	                                 height="142"/>
	                            <input type="hidden" name="FINGER_TEMPLATE" id="FINGER_TEMPLATE">
	                            <input type="Hidden" name="SampleImage" id="SampleImage" value="">
	                            <select cssClass="textBox"
	                                    id="FINGER_INDEX" name="FINGER_INDEX"
	                                    required>
	                                <option value="">[Select]</option>
	                                <option value="1">Right Thumb</option>
	                                <option value="2">Right Index Finger</option>
	                                <option value="3">Right Middle Finger</option>
	                                <option value="4">Right Ring Finger</option>
	                                <option value="5">Right Little Finger</option>
	                                <option value="6">Left Thumb</option>
	                                <option value="7">Left Index Finger</option>
	                                <option value="8">Left Middle Finger</option>
	                                <option value="9">Left Ring Finger</option>
	                                <option value="10">Left Little Finger</option>
	
	
	                            </select>
	                            <img id="image"/>
	
	
	                        	</div>
	                    		</td>
               	 			</tr>
							
							<tr>
								<td align="right"></td>
								<td><security:token/><input class="mainbutton" type="submit" id="confirm" name="confirm" value="Submit"></td>
							</tr>
						</table>
						
						<input type="hidden" name="<%= CommandFieldConstants.KEY_PROD_ID %>" value="${requestScope.PID}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_TX_ID %>" value="${requestScope.TRXID}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_TX_PROCESS_AMNT %>" value="${requestScope.DEDUCTION_AMOUNT}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_COMM_AMOUNT %>" value="${requestScope.DEDUCTION_AMOUNT}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_TX_AMOUNT %>" value="${requestScope.TRANSACTION_AMOUNT}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_MOB_NO %>" value="${requestScope.MS_ISDN_MOBILE}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN %>" value="${requestScope.WALKIN_SENDER_MSISDN}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN %>" value="${requestScope.WALKIN_REC_MSISDN}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_WALKIN_SENDER_CNIC %>" value="${requestScope.WALKIN_SENDER_CNIC}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC %>" value="${requestScope.WALKIN_REC_CNIC}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_PIN %>" value="${requestScope.PIN}"/>
						<input type="hidden" name="MS_ISDN_MOBILE" value="${requestScope.MS_ISDN_MOBILE}"/>						
						<input type="hidden" name="amount" value="${requestScope.CW_TOTOAL_AMOUNT_FOR_CUSTOMER}"/>
						<input type="hidden" name="IS_BVS_REQ" value="1"/>
					</form>
				</td>
				<OBJECT id="OurActiveX" name="OurActiveX" classid="clsid:121C3E0E-DC6E-45dc-952B-A6617F0FAA32" VIEWASTEXT
            codebase="${contextPath}/scripts/cabinet/OurActiveX.cab"></OBJECT>
				
			</tr>
			<tr valign="bottom">
				<td>
					<jsp:include page="footer.jsp"></jsp:include>
				</td>
			</tr>
		</table>
		
		
		<script language="javascript">
    var jq = jQuery.noConflict();


    jq("#buttonBVS").click(function () {
        OpenActiveX();
    });

    function OpenActiveX() {
        try {

            document.OurActiveX.Open(); //Running method from activeX
            //getting parameter to the ActiveX
            var template = document.OurActiveX.MyParam;

            if (template === "") {
                alert("Problem extracting  template at the moment . Please try again after some time");
            }
            else {
                document.querySelector("#FINGER_TEMPLATE").value = template;
                var image = document.OurActiveX.Image;
                document.querySelector("#SampleImage").value = image;
                document.querySelector("#OutputImage").src = "data:image/Bmp;base64," + image;
                //document.scan.submit();
                var e = document.getElementById("divBVSImage");
                if (e.style.display == 'none')
                    e.style.display = 'block';


            }


        }
        catch (Err) {
            alert(Err.description);
        }
    }

    if (jq("#divBVSImage").css('display') === 'none') {

        var bvsProcessCheck =   confirm("This transaction will perform via BVS for recipient. Do you want to continue ?");
        if (bvsProcessCheck != true) {
            history.go(-1);
        }


        if (navigator.appName == 'Microsoft Internet Explorer' || !!(navigator.userAgent.match(/Trident/) || navigator.userAgent.match(/rv 11/))) {
            // document.getElementById("citizenNumber").onfocusout = OpenActiveX();
        }
        else {
            alert("Currently \'Biometric Verification\' is supported with \'Internet Explorer\' only.\nPlease use \'Internet Explorer\' to perform biomertic verification.\n");
            history.go(-1);
        }
    }




</script>
		
		
		
		</div>
	</body>
	
</html>