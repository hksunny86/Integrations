<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>  
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

   <body bgcolor="#ffffff" >
   	<div id = "printDiv">
      <table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td>
           <table width="100%" border="0" cellpadding="2" cellspacing="2">
           	<tr><td height="40" colspan="5" align="center" bgcolor="#7FAC7F" style="font-size:24px;font-weight: bold;" >Branchless Banking Account Opening Form ( ${mfsAccountModel.typeOfCustomerName} )</td></tr>
	        <tr>
	         	 <td height="35" width="25%" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Application No:</span></td>
	         	 <td height="35" align="left" bgcolor="#D1FFD1">&nbsp;</td>
	        	 <td width="20%" rowspan="5" align="center" valign="middle"><img src="${contextPath}/images/upload_dir/${mfsAccountModel.appUserId}.gif" width="170" height="170" style="border:1px solid #7FAC7F"/></td>
	        </tr>
	        <tr>
				<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Date:</span></td>
	         	<td height="35" bgcolor="#D1FFD1"><fmt:formatDate pattern="dd/MM/yyyy" value="${mfsAccountModel.applicationDate}"/></td>
			</tr>
	        <tr>
	         	 <td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Customer ID:</span></td>
           			<td bgcolor="#D1FFD1" height="35">
           			${mfsAccountModel.customerId}
          				<!-- <span style="font-weight: bold;">Zong</span> &nbsp;&nbsp;&nbsp;&nbsp;
          				<c:choose>
				            <c:when test="${mfsAccountModel.zongRelation}">
			                    Yes
		                    </c:when>
		                    <c:otherwise>
		                    	No
		                    </c:otherwise>
		                 </c:choose>	
           				
           				<br/>
           				<span style="font-weight: bold;">Askari</span> &nbsp;
           				<c:choose>
				            <c:when test="${mfsAccountModel.askariRelation}">
			                    Yes
		                    </c:when>
		                    <c:otherwise>
		                    	No
		                    </c:otherwise>
		                 </c:choose> -->
           			</td>
	        </tr>
	        <tr>
	            <td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Mobile Number:</span></td>
           		<td height="35" bgcolor="#D1FFD1" >${mfsAccountModel.zongNo}</td>        	
	        </tr> 	 
	        <c:if test="${not empty mfsAccountModel.allpayId}">
		        <tr>
		            <td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Agent Id:</span></td>
	           		<td height="35" bgcolor="#D1FFD1" >${mfsAccountModel.allpayId}</td>        	
		        </tr> 	 
	        </c:if>
	        </table>
	        </td></tr><tr><td>
	        <table width="100%" border="0" cellpadding="2" cellspacing="2">	
            <tr><td height="40" align="left" bgcolor="#7FAC7F" colspan="6"><span style="font-size:17; font-family:sans-serif; font-weight: bold;">Personal Information:</span></td></tr>
           		<tr>
	           		<td height="35" align="left" width="15%" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Name:</span></td>
		            <td height="35" align="left"  bgcolor="#D1FFD1" colspan="5">
		                 ${mfsAccountModel.name}
		            </td>
             	</tr>
             	<tr>
	           		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Name of Father/Husband:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1" colspan="5">
		                ${mfsAccountModel.fatherHusbandName}
		            </td>
             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF"  width="16%"><span style="font-weight: bold;">Present Home Address:</span></td>
             		<td height="35" bgcolor="#D1FFD1" width="15%">&nbsp;</td>
             		<td height="35" align="left" bgcolor="#AFDCAF"  width="12%"><span style="font-weight: bold;">House No:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1" width="12%">
		            	${mfsAccountModel.presentAddHouseNo}
		            </td>
		             <td height="35" align="left" bgcolor="#AFDCAF"  width="12%"><span style="font-weight: bold;">Street/Lane No:</span></td>
		             <td height="35" align="left" bgcolor="#D1FFD1" width="14%">
		                 ${mfsAccountModel.presentAddStreetNo}
		             </td>
             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">District/Tehsil/Town:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1">
		            	<c:choose>
				            <c:when test="${not empty mfsAccountModel.presentAddDistrictId}">
			                    ${mfsAccountModel.presentHomeAddDistrictName}
		                    </c:when>
		                    <c:otherwise>
		                    	${mfsAccountModel.presentDistOthers}
		                    </c:otherwise>
		                 </c:choose>			   
		            </td>	
		            <td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">City/Village:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1">
		            	<c:choose>
				            <c:when test="${not empty mfsAccountModel.presentAddCityId}">
			                    ${mfsAccountModel.presentHomeAddCityName}
		                    </c:when>
		                    <c:otherwise>
		                    	${mfsAccountModel.presentCityOthers}
		                    </c:otherwise>
		                 </c:choose>
		            </td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Post Office:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1">
		            	<c:choose>
				            <c:when test="${not empty mfsAccountModel.presentAddPostalOfficeId}">
			                    ${mfsAccountModel.presentHomeAddPostalOfficeName}
		                    </c:when>
		                    <c:otherwise>
		                    	${mfsAccountModel.presentPostalOfficeOthers}
		                    </c:otherwise>
		                 </c:choose>
		            </td>
		            
             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Permanent Home Address:</span></td>
             		<td height="35" bgcolor="#D1FFD1">&nbsp;</td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">House No:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1">
		            	${mfsAccountModel.permanentAddHouseNo}
		            </td>
		             <td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Street/Lane No:</span></td>
		             <td height="35" align="left" bgcolor="#D1FFD1">
		                 ${mfsAccountModel.permanentAddStreetNo}
		             </td>
             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">District/Tehsil/Town:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1">
		            	<c:choose>
				            <c:when test="${not empty mfsAccountModel.permanentAddDistrictId}">
			                    ${mfsAccountModel.permanentHomeAddDistrictName}
		                    </c:when>
		                    <c:otherwise>
		                    	${mfsAccountModel.permanentDistOthers}
		                    </c:otherwise>
		                 </c:choose>			   
		            </td>	
		            <td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">City/Village:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1">
		            	<c:choose>
				            <c:when test="${not empty mfsAccountModel.permanentAddCityId}">
			                    ${mfsAccountModel.permanentHomeAddCityName}
		                    </c:when>
		                    <c:otherwise>
		                    	${mfsAccountModel.permanentAddCityOthers}
		                    </c:otherwise>
		                 </c:choose>
		            </td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Post Office:</span></td>
		            <td height="35" align="left" bgcolor="#D1FFD1">
		            	<c:choose>
				            <c:when test="${not empty mfsAccountModel.permanentAddPostalOfficeId}">
			                    ${mfsAccountModel.permanentHomeAddPostalOfficeName}
		                    </c:when>
		                    <c:otherwise>
		                    	${mfsAccountModel.permanentPostalOfficeOthers}
		                    </c:otherwise>
		                 </c:choose>
		            </td>
             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Gender:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" >${mfsAccountModel.gender}</td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">CNIC No:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" >${mfsAccountModel.nic}</td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">CNIC Expiry:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" ><fmt:formatDate pattern="dd/MM/yyyy" value="${mfsAccountModel.nicExpiryDate}"/></td>
             	</tr> 
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Segment:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" >${mfsAccountModel.segmentName}</td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Employee ID:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" >${mfsAccountModel.employeeID}</td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Email Address:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" >${mfsAccountModel.email}</td>
             		
             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF" >&nbsp;</td>
             		<td height="35" align="left" bgcolor="#D1FFD1" >&nbsp;</td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Date Of Birth:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" ><fmt:formatDate pattern="dd/MM/yyyy" value="${mfsAccountModel.dob}"/></td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Language:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" >${mfsAccountModel.languageName}</td>
             	</tr>
             	<tr>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Contact No:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" >${mfsAccountModel.contactNo}</td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Land Line No:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" >${mfsAccountModel.landLineNo}</td>
             		<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Mobile No:</span></td>
             		<td height="35" align="left" bgcolor="#D1FFD1" >${mfsAccountModel.mobileNo}</td>
             	</tr>
           </table>
           </td></tr><tr><td>
	       <table width="100%" border="0" cellpadding="2" cellspacing="2">
           	  <tr><td height="40" align="left" bgcolor="#7FAC7F" colspan="4"><span style="font-size:17; font-family:sans-serif; font-weight: bold;">Other Information:</span></td></tr>
	          		<tr>
	          			<td height="35" align="left" width="25%" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Type of Customer:</span></td>
	            		<td height="35" align="left" width="25%" bgcolor="#D1FFD1">${mfsAccountModel.customerAccountName}</td>
			 			<td height="35" align="left" width="25%" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Referred By:</span></td>
	            		<td height="35" align="left" width="25%" bgcolor="#D1FFD1">${mfsAccountModel.refferedBy}</td>
	          		</tr>
	          		<tr>
	          			<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Source of Funds:</span></td>
	            		<td height="35" align="left" bgcolor="#D1FFD1" >
	            			<c:choose>
				            <c:when test="${not empty mfsAccountModel.fundsSourceId}">
			                    ${mfsAccountModel.fundSourceName}
		                    </c:when>
		                    <c:otherwise>
		                    	${mfsAccountModel.fundSourceOthers}
		                    </c:otherwise>
		                 </c:choose>
				           	
				     </td>
			 		<td height="35" align="left" bgcolor="#D1FFD1" colspan="2"/>
	          		</tr>
	          		<tr>
	          			<td height="35" align="left" bgcolor="#AFDCAF" ><span style="font-weight: bold;">Please Narrate:</span></td>
	            		<td height="35" align="left" bgcolor="#D1FFD1" colspan="3">
			 			${mfsAccountModel.fundSourceNarration}
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




