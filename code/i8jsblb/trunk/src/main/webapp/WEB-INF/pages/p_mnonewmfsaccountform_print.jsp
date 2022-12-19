<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html; charset=utf-8" %>
<jsp:directive.page import="com.inov8.microbank.common.model.AppUserModel"/>
<jsp:directive.page import="com.inov8.microbank.common.util.ThreadLocalAppUser"/>
<jsp:directive.page import="com.inov8.microbank.common.model.UserDeviceAccountsModel"/>
<jsp:directive.page import="com.inov8.microbank.common.util.PortalDateUtils"/>
<%@page import="com.inov8.microbank.common.util.*"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<c:set var="contextPath" scope="request">${pageContext.request.contextPath}</c:set>

<%
String agentId = null;
AppUserModel createdByAppUserModel = ThreadLocalAppUser.getAppUserModel();
if( createdByAppUserModel != null && createdByAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue() )
{
   	UserDeviceAccountsModel deviceAccountModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
  	if( deviceAccountModel != null && deviceAccountModel.getUserDeviceAccountsId() != null )
  	{
 	  	agentId = deviceAccountModel.getUserId();
 	  	pageContext.setAttribute( "agentId", agentId );
   	}
}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
   <script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
   <head>
   		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="pragma" content="no-cache" />
      <style type="text/css">
      	 BODY{
      	 	margin: 0px;
      	 	padding: 0px;
      	 }
         div#printDiv{
			margin: 0 auto;
		    width: 900px;
		}
      </style>
    <script type="text/javascript">
    	$(document).ready(
    		function()
    		{
		    	var parentWindow = window.opener;

		    	var customerAccountTypeCombo = parentWindow.$('customerAccountTypeId');
		    	var customerAccountType = customerAccountTypeCombo.options[customerAccountTypeCombo.selectedIndex].text;
		    	$('#customerAccountType').text( customerAccountType );

		    	var zongNo = parentWindow.$('zongNo').value;
		    	$('#zongNo').text( zongNo );

		    	var name =  parentWindow.$('name').value;
		    	$('#name').text( name );

		    	var fatherHusbandName = parentWindow.$('fatherHusbandName').value;
		    	$('#fatherHusbandName').text( fatherHusbandName );

		    	//Present Address starts
		    	var presentAddHouseNo = parentWindow.$('presentAddHouseNo').value;
		    	$('#presentAddHouseNo').text( presentAddHouseNo );

				var presentAddStreetNo = parentWindow.$('presentAddStreetNo').value;
				$('#presentAddStreetNo').text( presentAddStreetNo );

				var presentAddDistrictCombo = parentWindow.$('presentAddDistrictId');
		    	var presentAddDistrict = presentAddDistrictCombo.options[presentAddDistrictCombo.selectedIndex].text;
		    	if( presentAddDistrict == 'Others' )
		    	{
		    		presentAddDistrict = parentWindow.$('presentDistOthers').value;
		    	}
		    	$('#presentAddDistrict').text( presentAddDistrict );

		    	var presentAddCityCombo = parentWindow.$('presentAddCityId');
		    	var presentAddCity = presentAddCityCombo.options[presentAddCityCombo.selectedIndex].text;
		    	if( presentAddCity == 'Others' )
		    	{
		    		presentAddCity = parentWindow.$('presentCityOthers').value;
		    	}
		    	$('#presentAddCity').text( presentAddCity );

				var presentAddPostalOfficeCombo = parentWindow.$('presentAddPostalOfficeId');
		    	var presentAddPostalOffice = presentAddPostalOfficeCombo.options[presentAddPostalOfficeCombo.selectedIndex].text;
		    	if( presentAddPostalOffice == 'Others' )
		    	{
		    		presentAddPostalOffice = parentWindow.$('presentPostalOfficeOthers').value;
		    	}
		    	$('#presentAddPostalOffice').text( presentAddPostalOffice );
		    	//Present Address ends

				//Permanent Address starts
				var permanentAddHouseNo = parentWindow.$('permanentAddHouseNo').value;
				$('#permanentAddHouseNo').text( permanentAddHouseNo );
		    	var permanentAddStreetNo = parentWindow.$('permanentAddStreetNo').value;
		    	$('#permanentAddStreetNo').text( permanentAddStreetNo );

		    	var permanentAddDistrictCombo = parentWindow.$('permanentAddDistrictId');
		    	var permanentAddDistrict = permanentAddDistrictCombo.options[permanentAddDistrictCombo.selectedIndex].text;
		    	if( permanentAddDistrict == 'Others' )
		    	{
		    		permanentAddDistrict = parentWindow.$('permanentDistOthers').value;
		    	}
		    	$('#permanentAddDistrict').text( permanentAddDistrict );

				var permanentAddCityCombo = parentWindow.$('permanentAddCityId');
		    	var permanentAddCity = permanentAddCityCombo.options[permanentAddCityCombo.selectedIndex].text;
		    	if( permanentAddCity == 'Others' )
		    	{
		    		permanentAddCity = parentWindow.$('permanentAddCityOthers').value;
		    	}
		    	$('#permanentAddCity').text( permanentAddCity );

				var permanentAddPostalOfficeCombo = parentWindow.$('permanentAddPostalOfficeId');
		    	var permanentAddPostalOffice = permanentAddPostalOfficeCombo.options[permanentAddPostalOfficeCombo.selectedIndex].text;
		    	if( permanentAddPostalOffice == 'Others' )
		    	{
		    		permanentAddPostalOffice = parentWindow.$('permanentPostalOfficeOthers').value;
		    	}
		    	$('#permanentAddPostalOffice').text( permanentAddPostalOffice );
		    	//Permanent Address ends

				if( parentWindow.$('male').checked )
		    	{
		    		$('#gender').text( 'Male' );
		    	}
		    	else
		    	{
		    		$('#gender').text( 'Female' );
		    	}
				var dob = parentWindow.$('dob').value;
				$('#dob').text( dob );

				var languageCombo = parentWindow.$('languageId');
		    	var language = languageCombo.options[languageCombo.selectedIndex].text;
		    	$('#language').text( language );

				var nic = parentWindow.$('nic').value;
				$('#nic').text( nic );

				var nicExpiryDate = parentWindow.$('nicExpiryDate').value;
				$('#nicExpiryDate').text( nicExpiryDate );

				var segmentCombo = parentWindow.$('segmentId');
		    	var segment = segmentCombo.options[segmentCombo.selectedIndex].text;
		    	$('#segment').text( segment );

				var employeeID = parentWindow.$('employeeID').value;
				$('#employeeID').text( employeeID );
		    	var email = parentWindow.$('email').value;
		    	$('#email').text( email );
		    	var contactNo = parentWindow.$('contactNo').value;
		    	$('#contactNo').text( contactNo );
		    	var landLineNo = parentWindow.$('landLineNo').value;
		    	$('#landLineNo').text( landLineNo );
		    	var mobileNo = parentWindow.$('mobileNo').value;
		    	$('#mobileNo').text( mobileNo );

				//Other Information starts
				var customerTypeCombo = parentWindow.$('customerTypeId');
		    	var customerType = customerTypeCombo.options[customerTypeCombo.selectedIndex].text;
		    	$('#customerType').text( customerType );

				var refferedBy = parentWindow.$('refferedBy').value;
				$('#refferedBy').text( refferedBy );

				var fundsSourceCombo = parentWindow.$('fundsSourceId');
		    	var fundsSource = fundsSourceCombo.options[fundsSourceCombo.selectedIndex].text;
		    	if( fundsSource == 'Others' )
		    	{
		    		fundsSource = parentWindow.$('fundSourceOthers').value;
		    	}
		    	$('#fundsSource').text( fundsSource );

				var fundSourceNarration = parentWindow.$('fundSourceNarration').value;
				$('#fundSourceNarration').text( fundSourceNarration );
		    	//Other Information ends

		    	printForm();
    		}
    	);

    	function printForm()
    	{
    		var content = document.getElementById("printDiv"); 
           	if(content != null){
				var pri = document.getElementById("ifmcontentstoprint").contentWindow;
				pri.document.open();
				pri.document.write(content.innerHTML);
				pri.document.close();
				pri.focus();
				pri.print();
           	}
    	}
    </script>
  </head>

   <body bgcolor="#ffffff" >
   	<div id = "printDiv">
      <table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td>
           <table width="100%" border="0" cellpadding="2" cellspacing="2">
           	<tr><td height="40" colspan="5" align="center" bgcolor="#7FAC7F" style="font-size:24px;font-weight: bold;" >Branchless Banking Account Opening Draft Form ( <span id="customerAccountType"></span> )</td></tr>
	        <tr>
	         	 <td height="35" width="25%" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Application No:</span></td>
	         	 <td height="35" align="left" bgcolor="#D1FFD1">&nbsp;</td>
	        	 <td width="20%" rowspan="5" align="center" valign="middle"><img id="customerImage" src="${contextPath}/images/draft.png" width="170" height="170" style="border:1px solid #7FAC7F"/></td>
	        </tr>
	        <tr>
				<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Date:</span></td>
	         	<td height="35" bgcolor="#D1FFD1"><%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%></td>
			</tr>
	        <tr>
	         	 <td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Customer ID:</span></td>
           			<td bgcolor="#D1FFD1" height="35">
           			${mfsAccountModel.customerId}
           			</td>
	        </tr>
	        <tr>
	            <td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Mobile Number:</span></td>
           		<td height="35" bgcolor="#D1FFD1" ><span id="zongNo"></span></td>
	        </tr>
	        <c:if test="${not empty agentId}">
		        <tr>
		            <td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Agent Id:</span></td>
	           		<td height="35" bgcolor="#D1FFD1" >
           				<%=agentId%>
	           		</td>
		        </tr>
	        </c:if>
	        </table>
	        </td></tr><tr><td>
	        <table width="100%" border="0" cellpadding="2" cellspacing="2">
            <tr><td height="40" align="left" bgcolor="#7FAC7F" colspan="6"><span style="font-size:17; font-family:sans-serif; font-weight: bold;">Personal Information:</span></td></tr>
           		<tr>
	           		<td height="35" align="left" width="15%" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Name:</span></td>
		            <td height="35" align="left"  bgcolor="#D1FFD1" colspan="5">
		                 <span id="name"></span>
		            </td>
             	</tr>
             	<tr>
	           		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Name of Father/Husband:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1" colspan="5">
		                <span id="fatherHusbandName"></span>
		            </td>
             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF"  width="16%"><span style="font-weight: bold;">Present Home Address:</span></td>
             		<td height="35" bgcolor="#D1FFD1" width="15%">&nbsp;</td>
             		<td height="35" align="left" bgcolor="#AFDCAF"  width="12%"><span style="font-weight: bold;">House No:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1" width="12%">
		            	<span id="presentAddHouseNo"></span>
		            </td>
		             <td height="35" align="left" bgcolor="#AFDCAF"  width="12%"><span style="font-weight: bold;">Street/Lane No:</span></td>
		             <td height="35" align="left" bgcolor="#D1FFD1" width="14%">
		                 <span id="presentAddStreetNo"></span>
		             </td>
             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">District/Tehsil/Town:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1">
		            	<span id="presentAddDistrict"></span>
		            </td>
		            <td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">City/Village:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1">
		            	<span id="presentAddCity"></span>
		            </td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Post Office:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1">
		            	<span id="presentAddPostalOffice"></span>
		            </td>

             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Permanent Home Address:</span></td>
             		<td height="35" bgcolor="#D1FFD1">&nbsp;</td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">House No:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1">
		            	<span id="permanentAddHouseNo"></span>
		            </td>
		             <td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Street/Lane No:</span></td>
		             <td height="35" align="left" bgcolor="#D1FFD1">
		                 <span id="permanentAddStreetNo"></span>
		             </td>
             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">District/Tehsil/Town:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1">
		            	<span id="permanentAddDistrict"></span>
		            </td>
		            <td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">City/Village:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1">
		            	<span id="permanentAddCity"></span>
		            </td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Post Office:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1">
		            	<span id="permanentAddPostalOffice"></span>
		            </td>
             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Gender:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" ><span id="gender"></span></td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">CNIC No:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" ><span id="nic"></span></td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">CNIC Expiry:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" ><span id="nicExpiryDate"></span></td>
             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Segment:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" ><span id="segment"></span></td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Employee ID:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" ><span id="employeeID"></span></td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Email Address:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" ><span id="email"></span></td>

             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF" >&nbsp;</td>
             		<td height="35" align="left" bgcolor="#D1FFD1" >&nbsp;</td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Date Of Birth:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" ><span id="dob"></span></td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Language:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" ><span id="language"></span></td>
             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Contact No:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" ><span id="contactNo"></span></td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Land Line No:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" ><span id="landLineNo"></span></td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Mobile No:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" ><span id="mobileNo"></span></td>
             	</tr>
           </table>
           </td></tr><tr><td>
	       <table width="100%" border="0" cellpadding="2" cellspacing="2">
           	  <tr><td height="40" align="left" bgcolor="#7FAC7F" colspan="4"><span style="font-size:17; font-family:sans-serif; font-weight: bold;">Other Information:</span></td></tr>
	          		<tr>
	          			<td height="35" align="left" width="25%" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Type of Customer:</span></td>
	            		<td height="35" align="left" width="25%" bgcolor="#D1FFD1"><span id="customerType"></span></td>
			 			<td height="35" align="left" width="25%" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Referred By:</span></td>
	            		<td height="35" align="left" width="25%" bgcolor="#D1FFD1"><span id="refferedBy"></span></td>
	          		</tr>
	          		<tr>
	          			<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Source of Funds:</span></td>
	            		<td height="35" align="left" bgcolor="#D1FFD1" >
	            			<span id="fundsSource"></span>
				     	</td>
			 		<td height="35" align="left" bgcolor="#D1FFD1" colspan="2"/>
	          		</tr>
	          		<tr>
	          			<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Please Narrate:</span></td>
	            		<td height="35" align="left" bgcolor="#D1FFD1" colspan="3">
			 				<span id="fundSourceNarration"></span>
				 		</td>
	          		</tr>
           </table>
           </td></tr><tr><td>
	       <table border="0" cellpadding="3" cellspacing="2">
	            <tr>
	            	<td align="left" bgcolor="#D1FFD1"  colspan="4" height="40">
	            		I hereby agree with the Terms and Conditions, which I have read, understood and received a copy of, and confirm that the information
	            		provided above is correct to the best of my knowledge.
	            	</td>
	            </tr>

	            <tr>
	            	<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Customer Signature & Date:</span></td>
	            	<td height="35" align="left" bgcolor="#D1FFD1" colspan="3">&nbsp;</td>
	            </tr>

	          	<tr><td colspan="4" height="2" background="images/seprator.gif"></td></tr>
	          	<tr><td height="40" align="left" bgcolor="#7FAC7F" colspan="4"><span style="font-size:17; font-family:sans-serif; font-weight: bold;">For Office Use Only:</span></td></tr>
           		<tr>
           			<td height="35" align="left" bgcolor="#AFDCAF" width="25%"><span style="font-weight: bold;">BB Agent:</span></td>
	            	<td height="35" align="left" bgcolor="#D1FFD1" width="24%">&nbsp;</td>
	            	<td height="35" align="left" bgcolor="#AFDCAF" width="27%"><span style="font-weight: bold;">Incharge BB:</span></td>
	            	<td height="35" align="left" bgcolor="#D1FFD1" width="24%">&nbsp;</td>
	            </tr>
	            <tr>
           			<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Date:</span></td>
	            	<td height="35" align="left" bgcolor="#D1FFD1">&nbsp;</td>
	            	<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Date:</span></td>
	            	<td height="35" align="left" bgcolor="#D1FFD1">&nbsp;</td>
	            </tr>
	            <tr>
           			<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Signature & Stamp:</span></td>
	            	<td height="35" align="left" bgcolor="#D1FFD1">&nbsp;</td>
	            	<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Signature & Stamp:</span></td>
	            	<td height="35" align="left" bgcolor="#D1FFD1">&nbsp;</td>
	            </tr>
	            <tr>
	            	<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">BB Operations Officer: Bank</span></td>
	            	<td height="35" align="left" bgcolor="#D1FFD1">&nbsp;</td>
	            	<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">BB Operations Manager: Bank</span></td>
	            	<td height="35" align="left" bgcolor="#D1FFD1">&nbsp;</td>
	            </tr>
	            <tr>
	            	<td align="left" bgcolor="#AFDCAF"  height="35"><span style="font-weight: bold;">Comments:</span></td>
	            	<td align="left" bgcolor="#D1FFD1" colspan="3">&nbsp;</td>
	            </tr>
           </table>
           </td></tr></table>
           </div>
           <div id="ifmcontentstoprint" style="height: 0px; width: 0px; position: absolute; display:none;"></div>
   </body>

</html>




