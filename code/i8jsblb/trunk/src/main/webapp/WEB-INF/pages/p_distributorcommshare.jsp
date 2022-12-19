<!--Author: Muhahmmad Atif Hussain-->
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE%></c:set>
<c:set var="updateAction"><%=PortalConstants.ACTION_UPDATE%></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE%></c:set>
<html>
<head>
<meta name="decorator" content="decorator2">

<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css" />
<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/prototype.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/scriptaculous.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/overlibmws.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/ajaxtags-1.2-beta2.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
<link type="text/css" rel="stylesheet" href="${contextPath}/styles/ajaxtags.css" />

<meta name="title" content="Agent Network Commission Share" />
<%
	String createPermission = PortalConstants.ADMIN_GP_CREATE;
	createPermission += "," + PortalConstants.PG_GP_CREATE;
	createPermission += "," + PortalConstants.MNG_COMM_SH_SHARE_CREATE;

	String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
	updatePermission += "," + PortalConstants.PG_GP_UPDATE;
	updatePermission += "," + PortalConstants.MNG_COMM_SH_SHARE_UPDATE;
%>
<script>
	var jq= $.noConflict();
	jq(document).ready(function($)
	{
		jq("input[name=action_type][value=\"default\"]").prop('checked', true);
		toggleActionType();

	});
	
	var actionType	=	"";
	function toggleActionType() {
		actionType = jq('input[name=action_type]:checked').val();

		if (actionType	== "default") {
			jq('#productId').val(-1);
			jq('#currentLevelId').val(-1);
			jq('#productLabelTdId').hide();
			jq('#productSelectTdId').hide();
		} else {
			jq('#productLabelTdId').show();
			jq('#productSelectTdId').show();
		}
		
		initTableGrid();
	}
		
	function addRow(text, levelId){
		var newRow = jq('#commissionTable .template').clone().removeAttr('class').removeAttr('style');
		
		var rowCount = jq('#commissionTable tr').length;
		rowCount=rowCount - 2;
		
		var item = {
				distributorLevel: jq('#tempDistributorLevelId').clone().attr('id',"tempDistributorLevelId"+rowCount).
				attr('name',"distCommShareDtlModelList["+rowCount+"].parentDistributorLevelIdModel.distributorLevelId").attr('value',levelId),
				commission: jq('#tempCommission').clone().attr('id',"tempCommission"+rowCount).attr('name',"distCommShareDtlModelList["+rowCount+"].commissionShare").attr('class', "textBox"),
			  };
			  template(newRow, item, text).appendTo('#commissionTable').fadeIn();
	}
		
	function template(row, item, text) {
		row.find('.distributorLevel').html(item.distributorLevel);
		row.find('.distributorLevel').append(text);
	  	row.find('.commission').html(item.commission);
	  	return row;
	}
		
	function sumCommission(){
		var total	=	0;

		jq('#currentLevelCommissionText').val(parseFloat("0.0"));
		
		jq("#commissionTable input[type=text]").each(function() {
			
			if(this.value!=null && this.value!="" && this.value!=NaN)
			{
				total	=	parseFloat(total)	+	parseFloat(this.value);	
			}
			jq('#currentLevelCommissionText').val(100-total);
        });
		
		
		if(jq("#currentLevelId")[0].selectedIndex ==0)
		{
			if(jq("#currentLevelCommissionText").val()=="0")
			{
				jq("#currentLevelCommissionText").val(parseFloat("100.0"));
			}
		}
	}
		
	function populateTableGrid()
	{
		if(jq("#distributorId").val()!=null && jq("#regionId").val()!=null && jq("#currentLevelId").val()!=null)
		{
			var distributor_Id	=	jq("#distributorId").val();
			var region_Id	=	jq("#regionId").val();
			var product_Id	=	jq("#productId").val();
			var currentLevel_Id	=	jq("#currentLevelId").val();
			var	action_Type	=	jq("input[name=action_type]:checked").val();
			
			var request = jq.ajax({
				url:"p_distributorcommshareajax.html",
				data: { distributorId: distributor_Id, regionId: region_Id, productId: product_Id, currentLevelId: currentLevel_Id, actionType: action_Type },
				async: true,
				dataType: "xml"
			});
				 
			request.done(function( response ) {
				success(response);
			});
			
		  	function success(xmlResponse) {
		  		var count	=	0;
		  		
		  		jq(xmlResponse).find('item').each(function(){//this dnt work in IE
		  			
		  			if(jq(this).find('name').text()=="parentResult")
		  			{
		  				jq("#currentLevelCommissionText").val(jq(this).find('value').text());
		  			}
		  			else if(jq(this).find('name').text()=="childResult")
		  			{
		  				jq("#tempCommission"+count).val(jq(this).find('value').text());
		  				count++;
		  			}
		  			else if(jq(this).find('name').text()=="pk")
		  			{
		  				jq("#distCommSharePk").val(jq(this).find('value').text());
		  			}		  			
		  		});
		  	}
			sumCommission();
		}
	}
		
	function validateForm()
	{
		sumCommission();
		
		var actionType = jq('input[name=action_type]:checked').val();

		if(jq("#distributorId").val()==null || jq("#distributorId").val()=="")
		{
			alert("Please select agent network.");
			return false;	
		}
		if(jq("#regionId").val()==null || jq("#regionId").val()=="")
		{
			alert("Please select region.");
			return false;	
		}
		
		if(jq("#currentLevelId").val()==null || jq("#currentLevelId").val()=="")
		{
			alert("Please select current level.");
			return false;	
		}
		if(jq("#currentLevelCommissionText").val()==null || jq("#currentLevelCommissionText").val()=="")
		{
			alert("Please select current level commission.");
			return false;	
		}
		
		var count 	=	0;
        var flag	=	true;
        var invalidValue	=	false;
		jq('#commissionTable input[type="text"]').each(function(){
			if (this.value.length == "0" )
			{
				flag=false;
				this.style.borderColor="red";
			}
			else if(isNaN(this.value)){
				invalidValue = true;
				this.style.borderColor="red";
			}
			else
			{
				count	=	parseFloat(count)	+	parseFloat(this.value);
				this.style.borderColor="#e6e6e6";
			}
		});
		
		if(!flag){
			alert("Please provide commission share");
			return flag;
		}
		if(invalidValue){
			alert("Invalid commission share");
			return false;
		}
		if (count>100)
		{
			alert("Sum of share should be 100.");
			return false;
		}
		
		return flag;
	}

	function onAgentNetworkSelect() {
		jq("#currentLevelId option").remove();
	}
	
	function initTableGrid()
	{
		jq("#currentLevelCommissionText").val(0.0);
		jq("#commissionTable").find("tr:gt(1)").remove();
		jq("#currentLevelId option[value='']").remove();
  		jq("#distCommSharePk").val("");
  		
		var count=0;
		
		jq("#currentLevelId option").each(function(){
			count=count+1;
			
			if(jq("#currentLevelId").val()==this.value)
				return false;
			addRow(this.innerHTML, this.value);
		});
		populateTableGrid();
	}
</script>

<style type="text/css">
select:disabled,input:disabled {
	background-color: #F7F7F7;
}
</style>
</head>
<body bgcolor="#ffffff">
	<div id="rsp" class="ajaxMsg"></div>
	<div id="successMsg" class="infoMsg" style="display:none;"></div>
	<div id="errorMsg" class="errorMsg" style="display:none;"></div>
	<c:if test="${not empty messages}">
		<div class="infoMsg" id="successMessages">
			<c:forEach var="msg" items="${messages}">
				<c:out value="${msg}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
		<c:remove var="messages" scope="session" />
	</c:if>

	<html:form id="commissionForm" commandName="distributorCommShareVO" method="POST" action="p_distributorcommshare.html" onsubmit="return validateForm();">
		<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
		<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>"/>
		<html:hidden id="distCommSharePk" name="distCommSharePk" path="distributorCommShareId"/>
		
		<table style="width: 750px;">
			<tbody>
				<tr>
					<td colspan="4">
						<table style="width: 200px;">
							<tr>
								<td><input type="radio" name="action_type" value="default" onchange="toggleActionType();" checked="checked">Default </input></td>
								<td><input type="radio" name="action_type" value="custom" onchange="toggleActionType();">Customized </input></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td bgcolor="F3F3F3" class="formText" style="width: 25%;">Agent Network</td>
					<td style="width: 25%;">
						<html:select path="distributorId" cssClass="textBox">
							<html:option value="">--Select--</html:option>
							<c:if test="${distributorModelList != null}">
								<html:options items="${distributorModelList}" itemValue="distributorId" itemLabel="name" />
							</c:if>
						</html:select>
					</td>
					<td bgcolor="F3F3F3" class="formText" style="width: 25%;">Region</td>
					<td style="width: 25%;"><html:select path="regionId" cssClass="textBox" tabindex="4"/>
					</td>
				</tr>
				<tr>
					<td bgcolor="F3F3F3" class="formText">Current Agent Network Level</td>
					<td>
						<html:select path="currentLevelId" cssClass="textBox" onchange="initTableGrid();"/>
					</td>
					<td id="productLabelTdId" bgcolor="F3F3F3" class="formText">Product</td>
					<td id="productSelectTdId">
						<html:select path="productId" cssClass="textBox" onchange="initTableGrid();">
							<option value="">--All--</option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name" />
							</c:if>
						</html:select>
					</td>
				</tr>
			</tbody>
		</table>
		<br />
		<div class="eXtremeTable" style="display: inline-block;">
			<table id="commissionTable" class="tableRegion" style="width: 750px; float: left;">
				<thead>
					<tr>
						<td class="tableHeader">Agent Network Level</td>
						<td class="tableHeader">Commission Share (%)</td>
					</tr>
				</thead>
				<tbody>
					<tr class="template" style="display:none;">
						<td class="distributorLevel"/>
						<td class="commission"/>
					</tr>
					<c:forEach items="${distributorCommShareVO.distCommShareDtlModelList}" var="model" varStatus="modelStatus">
						<tr>
							<td class="distributorLevel">
								<spring:eval expression="distCommShareDtlModelList[${modelStatus.index}].parentDistributorLevelIdModel.name"></spring:eval>
								<html:input id="distributorLevel${modelStatus.index}" type="text" cssClass="textBox" path="distCommShareDtlModelList[${modelStatus.index}].parentDistributorLevelIdModel.distributorLevelId"/>
							</td>
							<td class="commission">
								<html:input id="commission${modelStatus.index}" type="text" cssClass="textBox" path="distCommShareDtlModelList[${modelStatus.index}].commissionShare" onkeypress="return checkNumeric(this,event)" maxlength="7"/>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<br />
		<table style="width: 750px;">
			<tbody>
				<tr>
					<td bgcolor="F3F3F3" class="formText" style="width:50%;">Current Agent Network Level commission (%)</td>
					<td><html:input id="currentLevelCommissionText" path="commissionShare" readonly="true" class="textBox" onkeyup="return maskNumber(this,event);"/></td>
				</tr>
				<tr>
					<td style="text-align: right;"><input type="submit" value="Save" /></td>
					<td style="text-align: left; vertical-align: top;"><input type="reset" value="Cancel"/></td>
				</tr>
			</tbody>
		</table>
		<ajax:select source="distributorId" target="regionId" baseUrl="${contextPath}/p_regionrefdata.html" 
		parameters="distributorId={distributorId},actionId=${retriveAction}" postFunction="onAgentNetworkSelect" />
		<ajax:select source="regionId" target="currentLevelId" baseUrl="${contextPath}/p_agentlevelrefdata.html"
		parameters="regionId={regionId},actionId=${retriveAction}" postFunction="initTableGrid"/>

	</html:form>
	<html:form commandName="distributorCommShareVO">
		<div style="visibility: hidden;">
			<html:input id="tempCommission" path="" onkeypress="return maskNumber(this,event);" onkeyup="sumCommission();"/>
			<html:hidden id="tempDistributorLevelId" path=""/>
		</div>
	</html:form>
</body>
</html>
