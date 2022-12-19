
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>



<html>
	<head>


		<v:javascript formName="retailerContactModel" staticJavascript="false" />
		<script type="text/javascript"
			src="<c:url value="/scripts/validator.jsp"/>"></script>

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<meta name="title" content="New Agent Account" />

		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript"
			 src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/toolbar.js"></script>


		      
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
      
	</head>
	<body>
		<div id = "printDiv">


		<table width="100%" border="0" cellpadding="0" cellspacing="1">
		<tr><td height="50" colspan="6" align="center" bgcolor="#8FBC8F" style="font-size:24px;font-weight: bold;" >Agent Account Opening Form</td></tr>
			<tr bgcolor="#C1FFC1">
				<td colspan="6" align="center">
					&nbsp;
				</td>
			</tr>
			
			<tr bgcolor="#C1FFC1">
				<td height="40" width="17%" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">User Name:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" width="17%" align="left">
					${retailerContactListViewFormModel.username}
				</td>
			</tr>
			
			<tr>
				<td height="40" width="17%" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">Area:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" width="17%" align="left">
				${retailerContactListViewFormModel.printAreaName}
				</td>
				
				<td height="40" width="10%" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">Branch/Franchise:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" width="17%" align="left">
				${retailerContactListViewFormModel.printRetailerName}
				</td>
			
				<td height="40" width="17%" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">Partner Group:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" width="25%" align="left">
				${retailerContactListViewFormModel.printPartnerGroupName}
				</td>
			</tr>
			
			<tr>
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">First Name:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.firstName}
				</td>
				
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">Last Name:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.lastName}
				</td>
			
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">Is Sub Agent:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
					<c:choose>
				            <c:when test="${retailerContactListViewFormModel.head}">
			                    Yes
		                    </c:when>
		                    <c:otherwise>
		                    	No
		                    </c:otherwise>
		            </c:choose>
				</td>
			</tr>
			
			<tr bgcolor="#C1FFC1">
				<td colspan="6" align="center">
					&nbsp;
				</td>
			</tr>
			
			<tr>
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">Application No:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.applicationNo}
				</td>
				
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">MSISDN:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.zongMsisdn}
				</td>
				
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;"> Account Opening Date:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				<fmt:formatDate pattern="dd/MM/yyyy" value="${retailerContactListViewFormModel.accountOpeningDate}"/>
				</td>
			</tr>
			
			<tr>
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;"> NTN No:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.ntnNo}
				</td>
				
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;"> CNIC No:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.cnicNo}
				</td>
				
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;"> CNIC Expiry Date:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				<fmt:formatDate pattern="dd/MM/yyyy" value="${retailerContactListViewFormModel.cnicExpiryDate}"/>
				</td>
			</tr>
			
			<tr>
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;"> Contact No:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.contactNo}
				</td>
				
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;"> Land Line No:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.landLineNo}
				</td>
				
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;"> Mobile No:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.mobileNumber}
				</td>
			</tr>
			
			<tr bgcolor="##C1FFC1">
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">Business Name:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.businessName}
				</td>
			</tr>
			
			<tr><td height="70"colspan="6" align="left" bgcolor="#8FBC8F"><h2>Business Address:</h2></td></tr>
			
			<tr>
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">Shop No:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.shopNo}
				</td>
				
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">Market / Plaza:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.marketPlaza}
				</td>
			</tr>
			
			<tr>
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">District / Tehsil / Town:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.printDistrictTehsilTownName}
				</td>
				
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">City / Village:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.printCityVillageName}
				</td>
			</tr>
			
			<tr>
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">Post Office:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.printPostOfficeName}
				</td>
				
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">NTN No:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.ntnNumber}
				</td>
			</tr>
			
			<tr>
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">Nearest Landmark:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.nearestLandmark}
				</td>
			</tr>
			
			<tr bgcolor="#C1FFC1">
				<td colspan="6" align="center">
					&nbsp;
				</td>
			</tr>
			
			<tr>
				<td height="40" align="left" bgcolor="#8FBC8F">
					<span style="font-weight: bold;">Nature of Business:</span>
				</td>
				<td bgcolor="#C1FFC1" height="40" align="left">
				${retailerContactListViewFormModel.printNatureOfBusinessName}
				</td>
			</tr>

		</table>
</div>
           <div id="ifmcontentstoprint" style="height: 0px; width: 0px; position: absolute; display:none;"></div>
           <script type="text/javascript">
           	var content = document.getElementById("printDiv");
           	if(content != null){
				var pri = document.getElementById("ifmcontentstoprint").contentWindow;
				pri.document.open();
				pri.document.write(content.innerHTML);
				pri.document.close();
				pri.focus();
				pri.print();
           	}
           </script>

	</body>
</html>
