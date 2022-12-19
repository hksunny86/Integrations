<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils'%>
<%@ page import='com.inov8.microbank.common.util.FinancialInstitutionConstants'%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="veriflyFinancialInstitution"><%=FinancialInstitutionConstants.VERIFLY_FINANCIAL_INSTITUTION %></c:set>

<html>
	<head>
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<style type="text/css">
		table .textBox
		{
			width: 100px;
			height: 16px;
			margin-bottom: 0px;
		}
	</style>	
	
	<meta name="decorator" content="decorator">
	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.0.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-custom.min.js"></script>

	<meta name="title" content="Agent Tagging" />
    
    <script language="javascript" type="text/javascript">
    
		var jq=$.noConflict();

	    function error(request) {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	    }

		var rowNum = 0;
		var currntParrentId = "${requestScope.agentGroupVOModel.parrentId}";
		var parentTitleFetchClicked = true;

		function changeProp(grpTitle) {
//			currntParrentId = grpTitle.value;
			parentTitleFetchClicked = false;
		}
		
	    function resetIdAndNameAttributes()
	    {
	    	jq('#rowsTable tbody tr').each(
	    		function(index)
	    		{
	    			jq(this).find(':input:not(:button)').each( function() {
	    					var name = jq(this).attr('name');
	    					var listName = name.substr(0,name.indexOf("_"));
	    					name = listName + "_" + index;
	    					var id = listName + "_" + index
	    					jq(this).attr('id', id);
	    					jq(this).attr('name', name);
    					}
	    			);
	    		}
	    	);
	    }

		function addTableRow(csvResponse, childIdType) {
				    
			if(rowNum == 999) {
				alert('Reached Maximum Limit of 1000 Records.');
				return;
			}
			
			rowNum++;
	
			var rowsTable = document.getElementById('rowsTable');
						
			var row = rowsTable.insertRow(rowNum);
			row.setAttribute('id', rowNum);
						
			var cell1  = row.insertCell(0);
			var cell2  = row.insertCell(1);
			var cell3  = row.insertCell(2);
			var cell4  = row.insertCell(3);
			var cell5  = row.insertCell(4);
			var cell6  = row.insertCell(5);
			var cell7  = row.insertCell(6);
			/*
			cell1.style.width="10%";
			cell2.style.width="10%";
			cell3.style.width="10%";
			cell4.style.width="20%";
			cell5.style.width="10%";
			cell6.style.width="10%";
			cell7.style.width="10%";
			*/
			cell1.style.textAlign="center";
			cell2.style.textAlign="center";
			cell3.style.textAlign="center";
			cell4.style.textAlign="center";
			cell5.style.textAlign="center";
			cell6.style.textAlign="center";
			cell7.style.textAlign="center";

			var childId = document.createElement("INPUT");
			childId.setAttribute("size", "7");
			childId.setAttribute('id', "childId_"+rowNum);
			childId.setAttribute('name', 'childId_'+rowNum);
			childId.setAttribute("value", '');
			childId.setAttribute("class", 'textBox');
			childId.setAttribute("maxlength", '7');
			childId.setAttribute("type", 'text');

			if(childIdType==1) {
	           	childId.readOnly = true;
			}
			 
			cell1.appendChild(childId);
			
			var appUserId = document.createElement("INPUT");
			appUserId.setAttribute("type", "hidden"); 
			appUserId.setAttribute('id', "appUserID_"+rowNum);
			appUserId.setAttribute('name', 'appUserID_'+rowNum);
			appUserId.setAttribute("value", '');
			cell1.appendChild(appUserId);
			
			var _link = document.createElement("INPUT");
			_link.setAttribute("type", "button"); 
			_link.setAttribute("value", 'Fetch Detail');
			_link.setAttribute("style", "font-weight:padding: 0px 20px;height: 25px;font-size:11px");

			if(childIdType==1) {
				_link.readOnly = true;
			}else{
	           _link.onclick = function() {
					runAjax(document.getElementById("childId_"+rowNum), 0, appUserId, cell3, cell4, cell5, cell6);
				}
			}
			
			cell2.appendChild(_link);

			var linkAdd = document.createElement("INPUT");
			linkAdd.setAttribute("type", "button"); 
			linkAdd.setAttribute("value", '+');		
			linkAdd.setAttribute("style", "font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;");
			linkAdd.setAttribute("class", 'addRow');
			
            linkAdd.onclick = function() {
                
            	var childId = document.getElementById("childId_"+rowNum).value;

            	if(childId == '') {

					alert('Kindly Enter Child Id');
					return false;
                }
            	
				addTableRow('', 0);	
			}

			var linkRemove = document.createElement("INPUT");
			linkRemove.setAttribute("type", "button"); 
			linkRemove.setAttribute("value", '-');		
			linkRemove.
			setAttribute("style", "background-color:#E40606 !important;color:#FFFCFC !important;font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;margin-left:5px;");
			
            linkRemove.onclick = function() {

				var row = this.parentNode.parentNode;
				row.parentNode.removeChild(row);
				
				rowNum--;
				
				if(rowNum >= 0) {
					document.getElementById('totalRows').value = rowNum;
				}

				if(rowNum < 1) {

					addTableRow('', 0);
				}
				resetIdAndNameAttributes();
			}
			
			cell7.appendChild(linkAdd);
			cell7.appendChild(linkRemove);

			document.getElementById('totalRows').value = rowNum;

			if(csvResponse != '') {
				
				var res = csvResponse.split(";");

				cell3.innerHTML = res[0];
				cell4.innerHTML = res[1];
				cell5.innerHTML = res[2];
				cell6.innerHTML = res[3];
				appUserId.value = res[4];
				document.getElementById("childId_"+rowNum).value = res[5];
				document.getElementById("childId_"+rowNum).defaultValue = res[5];
			}
		}	 
	 
	 
	 
	 function runAjax(childId, isParentAgent, appUserId, cell3, cell4, cell5, cell6) {

		var mfsAccountId = childId.value;
		 
		if(mfsAccountId =='') {
			
			alert('Kindly Enter New Parent/Child ID to Fetch Detail.');
			childId.select();
			childId.focus();
			return false;
		}

		var parrentId = document.getElementById('parrentId').value;
		var groupId = document.getElementById('groupId').value;
				
		if(!isParentAgent) {

			if(parrentId == mfsAccountId) {

				alert('Parent ID cannot be use in Child ID');
				childId.select();
				childId.focus();
				return false;
			}

			if(isExistAsChild(mfsAccountId) > 1) {
				
				alert('Child ID '+mfsAccountId+' already has defined');
				childId.select();
				childId.focus();
				return false;
			}
			
		} else if(isParentAgent && rowNum > 0) {

			if(isExistAsChild(parrentId) > 0) {
				
				alert('Child ID cannot be use in Parent ID');
				childId.select();
				childId.focus();
				return false;
			}
		}
	 	
		jq.ajax({

	        	type: "GET",
		        url: "p_createAgentGroupAjax.html?mfsAccountId="+mfsAccountId+"&isParentAgent="+isParentAgent+"&groupId="+groupId,
		        contentType: "application/json",
		        dataType: "text",
		        
		        success: function(xml, status, xhr){
		            	
		           	var xmlResponse = xhr.responseText;

					if(xmlResponse.includes("Error")) {

						alert(xmlResponse);
						
						if(isParentAgent == 1) {
						
							parentTitleFetchClicked = false;
						}
						
						return;
					}
					
		           	if(isParentAgent) {
						
		           		callbackMain(xmlResponse.split(";"));
						currntParrentId = mfsAccountId;
						parentTitleFetchClicked = true;

		           		if(rowNum == 0) {

			           		addTableRow('', 0);
				        }
		           		
				    } else {

			           	childId.readOnly = true;
			           	callback(xmlResponse.split(";"), appUserId, cell3, cell4, cell5, cell6);
					}
		       },

		       error: function() {
		         alert("An error occurred while processing XML file.");
		       }
		});		
			
	 }

	 function callbackMain(_xmlResponse) {
			
			document.getElementById("agentName").value = _xmlResponse[0];
			document.getElementById("mobileNumber").value = _xmlResponse[1];
			document.getElementById("cnic").value = _xmlResponse[2];
			document.getElementById("businessName").value = _xmlResponse[3];
			document.getElementById("appUserId").value = _xmlResponse[4];
	 }
	 
	 function callback(_xmlResponse, appUserId, cell3, cell4, cell5, cell6) {
			
		cell3.innerHTML = _xmlResponse[0];
		cell4.innerHTML = _xmlResponse[3];
		cell5.innerHTML = _xmlResponse[1];
		cell6.innerHTML = _xmlResponse[2];
		appUserId.value = _xmlResponse[4];
	 }


	function isExistAsChild(userId) {

		var count = 0;
		
		for(var i=1; i<= rowNum; i++) {

			var childId = document.getElementById("childId_"+i);

			if(childId != null && childId.value == userId) {
				count += 1; 
			}
		}

		return count;
	}

	 function validateForm() {
		var totalOfRows = document.getElementById('totalRows').value;
     	var originalCount = document.getElementById('originalCount').value;
     	if(totalOfRows == originalCount && !isFormDataChanged()){
       		return false;
       	}
     	
		var groupTitle = document.getElementById("groupTitle");
		var parentId = document.getElementById("parrentId");

		if(groupTitle.value =='') {
			alert('Kindly Enter Group Title.');
			groupTitle.select();
			groupTitle.focus();
			return false;
		}

		if(groupTitle.value.length < 5) {
			alert('Group Title length should atleast 5 characters.');
			groupTitle.select();
			groupTitle.focus();
			return false;
		}

		if(parentId.value =='') {
			alert('Kindly Enter Parent ID.');
			parentId.select();
			parentId.focus();
			return false;
		}

		if(parentId.value.length < 7) {
			alert('Parent ID must be in 7 digits.');
			parentId.select();
			parentId.focus();
			return false;
		}
		
		if(!parentTitleFetchClicked && parentId.value != currntParrentId) {
		
		    var r = confirm('You should fetch title bofore submit page for id '+parentId.value+
							'\nPress OK for submitting with previous id '+currntParrentId+
							'\nPress Cancel to refetch.');
			if (r == true) {
				parentTitleFetchClicked = true;
				parentId.value = currntParrentId;
			} else {
				parentTitleFetchClicked = false;
				parentId.select();
				parentId.focus();
			}
		
			return false;
		}
		
		for(var i=1; i<= rowNum; i++) {
			var childId = document.getElementById("childId_"+i);
			if(childId != undefined && childId != '' && childId != null && childId.value == parentId.value) {
				alert('Child ID cannot be use in Parent ID.');
				parentId.select();
				parentId.focus();
				return false;
			}
		}
      
		
	 }
	 
    </script>

	</head>
	<body bgcolor="#ffffff" onUnload="javascript:closeChild();">

		<div id="rsp" class="ajaxMsg"></div>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
		<html:form name='createAgentGroupForm' commandName="agentGroupVOModel" method="post" action="p_createAgentGroup.html" onsubmit="return validateForm()">
			<input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
			<input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />

			<table width="100%" border="0">
				<tr>
					<td class="formText" width="20%" align="right">Group Title :</td>
				  <td align="left" width="30%" ><html:input path="groupTitle" id="groupTitle" cssClass="textBox" maxlength="30" tabindex="1"/></td>
					<td class="formText" width="12%" align="right">&nbsp;</td>
					<td align="left" width="38%">&nbsp;</td>
				</tr>
				<tr>
					<td class="formText" align="right">Add New Parent Id :</td>
					<td align="left">
						<html:input path="parrentId" id="parrentId" cssClass="textBox" maxlength="7" tabindex="3"  onchange="changeProp(this)" 
								onkeypress="return maskNumber(this,event)"/>
						<a href="#loadingArea" onClick="runAjax(document.getElementById('parrentId'), 1)">Fetch Detail</a>
					</td>
					<td class="formText" align="right">Status :</td>
					<td align="left" >
						<html:checkbox id="status" path="status"/>
						<html:hidden path="groupId" id="groupId" cssClass="textBox" tabindex="5" maxlength="50"/>
						<html:hidden path="usecaseId" id="usecaseId" cssClass="textBox" tabindex="5" maxlength="50"/>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">Agent Name :</td>
					<td align="left">
						<html:input readonly="true" path="agentName" id="agentName" cssClass="textBox" tabindex="5" maxlength="50"/>
						<html:hidden readonly="true" path="appUserId" id="appUserId" cssClass="textBox" tabindex="5" maxlength="50"/>
						
					</td>
					<td class="formText" align="right">Business Name :</td>
					<td align="left"><html:input readonly="true" path="businessName" id="businessName" cssClass="textBox" tabindex="5" maxlength="50"/></td>
				</tr>				
				<tr>
					<td class="formText" align="right">Mobile Number :</td>
					<td align="left"><html:input readonly="true" path="mobileNumber" id="mobileNumber" cssClass="textBox" tabindex="5" maxlength="11" onkeypress="return maskNumber(this,event)"/></td>
					<td class="formText" align="right">CNIC :</td>
					<td align="left"><html:input readonly="true" path="cnic" id="cnic" cssClass="textBox" tabindex="5" maxlength="13" onkeypress="return maskNumber(this,event)"/></td>
				</tr>
				<tr>
				  <td colspan="4" align="right" class="formText">&nbsp;</td>
				</tr>
				<tr>
				  <td id='loadingArea' colspan="4" align="left" class="formText">
				  	<h2>Add / Remove Child</h2>
				  </td>
				  <!--<td class="formText" align="right">
				  	<input id="addBtn2" type="button" value="Add row" onClick="addTableRow('')"/>
				  </td>-->
				</tr>
				<tr>
				  <td colspan="4" align="left" class="formText">
				  
					<div class="eXtremeTable" style="overflow:scroll; border: thin;height:160px; align=center;">
					  <table width="100%" border="0" id="rowsTable">
	                    <tr>
	                      <td class="tableHeader" width="10%">Child ID </td>
	                      <td class="tableHeader" width="10%">Fetch Detail </td>
	                      <td class="tableHeader" width="10%">Agent Name </td>
	                      <td class="tableHeader" width="30%">Business Name </td>
	                      <td class="tableHeader" width="10%">Mobile # </td>
	                      <td class="tableHeader" width="10%">CNIC</td>
	                      <td class="tableHeader" width="10%">Action</td>
	                    </tr>
	                  </table>
	                </div>  
                
				  </td>
				<!--
				<tr>
				  <td colspan="3" align="left" class="formText">&nbsp;</td>
				  <td class="formText" align="right">
				  <input id="addBtn2" type="button" value="Add row" onClick="validateForm()"/>
				</tr>
				-->
				<tr>
				  <td colspan="1" align="left" class="formText"></td>
				  <td colspan="3"></td>
				</tr>
				<tr>
				  <td colspan="1"></td>
				  <td colspan="3">				
				  	<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
					<input type="hidden" id="totalRows" name="totalRows" value="0">
					<input type="hidden" name="originalCount" id="originalCount" value="${fedRuleManagementVO.fedRuleModelList.size()}"/>
					
				  		<c:choose>
							<c:when test="${not empty agentGroupVOModel.groupId and agentGroupVOModel.groupId > 0}">
								<authz:authorize ifAnyGranted="<%=PortalConstants.AGENT_GRP_U%>">
									<input name="button" type="submit" class="button" value="Update"/>
								</authz:authorize>
							</c:when>
							<c:otherwise>
								<authz:authorize ifAnyGranted="<%=PortalConstants.AGENT_GRP_CREATE%>">
									<input name="button" type="submit" class="button" value="Create" />
								</authz:authorize>
							</c:otherwise>
						</c:choose>
				  </td>
				</tr>


				</table>
			</html:form>
		

    
    <script language="javascript" type="text/javascript">

	    function error(request) {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	    }

		var childString = "${requestScope.agentGroupVOModel.childString}";
		var groupIdentity = "${requestScope.agentGroupVOModel.groupIdentity}";
		var onLoadRows = 0;
		if(childString != "") {
			
			childString = childString.split("|");

			for (var i = 0; i < childString.length; i++) { 

				var child = childString[i];
				
			    if(child != "") {
				    addTableRow(child, 1);
				    onLoadRows++;
				}
			}
			
		} else if(groupIdentity > 0 || groupIdentity == -1) { //(${param.groupId} > 0) {
			onLoadRows++;
		    addTableRow('', 0);
		}
		
		document.getElementById('originalCount').value = onLoadRows;
    </script>
	</body>
</html>