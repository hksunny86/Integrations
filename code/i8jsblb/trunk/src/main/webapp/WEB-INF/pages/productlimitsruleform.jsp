<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.inov8.microbank.common.util.*"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta name="decorator" content="decorator-simple">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta name="title" content="Product Limit Rules" />
	
	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>

	<script type="text/javascript">
		
		$(document).ready(function()
		{
			TABLE.formwork('#limitTable');
			
			$('#addBtn').click(function(){
				 $('input[type="submit"]').prop('disabled', false);
				var newRow = $('#limitTable .template').clone().removeAttr('class').removeAttr('style');		
				
				var rowCount = $('#limitTable tr').length;
				rowCount=rowCount - 2;
				
				var item = {
						productLimitRule: $('#productLimitRuleId').clone().attr('id',"productLimitRuleId"+rowCount).attr('name',"productLimitRuleModelList["+rowCount+"].productLimitRuleId"),
						status: $('#activeStatusId').clone().attr('id',"activeStatusId"+rowCount).attr('name',"productLimitRuleModelList["+rowCount+"].active"),
						channel: $('#channelId').clone().attr('id',"channelId"+rowCount).attr('name',"productLimitRuleModelList["+rowCount+"].deviceTypeId"),
						segment: $('#segmentId').clone().attr('id',"segmentId"+rowCount).attr('name',"productLimitRuleModelList["+rowCount+"].segmentId"),
						distributor: $('#distributorId').clone().attr('id',"distributorId"+rowCount).attr('name',"productLimitRuleModelList["+rowCount+"].distributorId"),
						accountType: $('#accountTypeId').clone().attr('id',"accountTypeId"+rowCount).attr('name',"productLimitRuleModelList["+rowCount+"].accountTypeId"),
						minLimit:$('#min_limit').clone().attr('id',"min_limit"+rowCount).attr('name',"productLimitRuleModelList["+rowCount+"].minLimit"),
						maxLimit: $('#max_limit').clone().attr('id',"max_limit"+rowCount).attr('name',"productLimitRuleModelList["+rowCount+"].maxLimit")
					  };
					  template(newRow, item).appendTo('#limitTable').fadeIn();
				
			});//end of $('#addBtn').click();
			
			
			$('input[type="text"]').on('change', function() {

			     // Change event fired..
			     $('input[type="submit"]').prop('disabled', false);
			});
			
			$('select').on('change', function() {

			     // Change event fired..
			     $('input[type="submit"]').prop('disabled', false);
			});

		});
		
		function template(row, item) {
		  row.find('.channel').html(item.channel);
		  row.find('.segment').html(item.segment);
		  row.find('.agent_network').html(item.distributor);
		  row.find('.handler_account_type').html(item.accountType);
		  row.find('.min_limit').html(item.minLimit);
		  row.find('.max_limit').html(item.maxLimit);
		  row.find('.channel').prepend(item.status);
		  row.find('.channel').prepend(item.productLimitRule);
		  return row;
		}
	
		var TABLE = {};
	
		TABLE.formwork = function(table)
		{
			var $tables = $(table);
			$tables.each(function () {
				var _table = $(this);
				<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_PRDCT_LMT_UPDATE%>">
                _table.find('thead tr').append($('<th class="delete">&nbsp;</th>'));
                _table.find('tbody tr').append($('<td class="delete"><input type="button" ' +
                    'style="background-color:#E40606 !important;color:#FFFCFC !important;"  value="X" title="Delete"/></td>'))
				</authz:authorize>
                <authz:authorize ifAnyGranted="<%=PortalConstants.MNG_PRDCT_LMTS_UPDATE%>">
                _table.find('thead tr').append($('<th class="delete">&nbsp;</th>'));
                _table.find('tbody tr').append($('<td class="delete"><input type="button" ' +
                    'style="background-color:#E40606 !important;color:#FFFCFC !important;"  value="X" title="Delete"/></td>'))
                </authz:authorize>
			});
	
			$tables.find('.delete :button').live('click', function(e) {
				 $('input[type="submit"]').prop('disabled', false);
				var tr = $(this).closest('tr');
				
				tr.each(function (i, el) {
			        var $tds = $(this).find('td');
			        $tds.get(0).children[1].value="false";
			        
			    });
				
				tr.fadeOut(400, function(){
					/* tr.remove(); */
					tr.css({ "display": "none" });
				});
			});
		}
	
		TABLE.deletable = function(button) {
		  var $button = $(button);
		  var $row = $button.parents('tr');
		 
		  $row=$row[$row.length-1];
		  $row.remove();
		}
	
		function validateForm(){
			 var flag	=	true;
			 
			$('#productLimitRuleForm tr').each(function() { 
				if(this.style.display!="none")
				{
					$('input[type="text"]', this).each(function() {
					    if($(this).type!="button" && $(this).type!="hidden")
					    {
					    	if ($(this).val().length == "0" )
							{
								flag=false;
								$(this).css({ "borderColor": "red" });
							}
							else
							{
								if (parseFloat($(this).val()) == "0" )
								{
									flag=false;
									$(this).css({ "borderColor": "red" });
								}
								else{
									$(this).css({ "borderColor": "#e6e6e6" });
								}
							}
					    }
					 });
				}
	        });
			
			
			if(!flag)
			{
				alert("Please provide values in required fields.");
				return flag;
			}
			
			var rowCount = $('#limitTable tr').length;
			rowCount=rowCount - 2;
			
			for(var x=0; x<rowCount; x++)
			{
				var minInput	=	$("#min_limit"+x);
				var maxInput	=	$("#max_limit"+x);
				
				if(minInput.closest('tr').css('display')!="none")
				{
					if(parseFloat(minInput.val()) > parseFloat(maxInput.val())){
						minInput.css({ "borderColor": "red" });
						maxInput.css({ "borderColor": "red" });
						flag=false;
					}
					else
					{
						minInput.css({ "borderColor": "#e6e6e6" });
						maxInput.css({ "borderColor": "#e6e6e6" });
					}
				}
			}
				       
			if(!flag)alert("Min. Limit should be less than Max. Limit");
			return flag;
		}
	</script>
</head>
<body>
	<spring:bind path="productLimitRuleVo.*">
		<c:if test="${not empty status.errorMessages}">
			<div class="errorMsg">
				<c:forEach var="error" items="${status.errorMessages}">
					<c:out value="${error}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
		</c:if>
	</spring:bind>
	<c:if test="${not empty messages}">
		<div class="infoMsg" id="successMessages">
			<c:forEach var="msg" items="${messages}">
				<c:out value="${msg}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
		<c:remove var="messages" scope="session" />
	</c:if>
	<html:form id="productLimitRuleForm" commandName="productLimitRuleVo" method="post" action="productlimitsruleform.html" cssStyle="padding:25px;" onsubmit="return validateForm();">
		<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
		<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>">
		<input type="hidden" name="productName" value="${param.productName}" />
		
		<html:hidden path="productId"/>	
					
		<h2>${param.productName}</h2>
		
		<hr/>
		<style>
			.prod_lr_table {
				font-size: 13px;
				text-align: center;
				background-color: #0292e0;
				padding: 7px;
				color: white;
			}
			
		</style>
		<table id="limitTable" width="100%" border="0" cellpadding="0" cellspacing="1" style="width: 80%;">
			<thead>
				<tr>
					<th class="prod_lr_table">Channel</th>
					<th class="prod_lr_table">Segment</th>
					<th class="prod_lr_table">Agent Network</th>
					<th class="prod_lr_table">Handler Account Type</th>
					<th class="prod_lr_table">Min. Limit</th>
					<th class="prod_lr_table">Max. Limit</th>
				</tr>
			</thead>
			<tbody>
				<tr class="template" style="display:none;">
					<td class="channel"></td>
					<td class="segment"></td>
					<td class="agent_network"></td>
					<td class="handler_account_type"></td>
					<td class="min_limit"></td>
					<td class="max_limit"></td>
				</tr>
				<c:forEach items="${productLimitRuleVo.productLimitRuleModelList}" var="model" varStatus="modelStatus">
					<tr>
						<td class="channel">
							<html:select id="channelId${modelStatus.index}" cssClass="textBox" path="productLimitRuleModelList[${modelStatus.index}].deviceTypeId">
								<option value="-1">--All--</option>
								<html:options items="${deviceTypeModelList}" itemLabel="name" itemValue="deviceTypeId" />
							</html:select>
							<html:hidden id="activeStatusId${modelStatus.index}" path="productLimitRuleModelList[${modelStatus.index}].active" value="true"/>
							<html:hidden id="productLimitRuleId${modelStatus.index}" path="productLimitRuleModelList[${modelStatus.index}].productLimitRuleId"/>
							<html:hidden path="productLimitRuleModelList[${modelStatus.index}].versionNo"/>
						</td>
						<td class="segment"><html:select id="segmentId${modelStatus.index}" cssClass="textBox" path="productLimitRuleModelList[${modelStatus.index}].segmentId">
								<option value="-1">--All--</option>
								<html:options items="${segmentModelList}" itemLabel="name" itemValue="segmentId" />
							</html:select></td>
						<td class="agent_network">
							<html:select id="distributorId${modelStatus.index}" cssClass="textBox" path="productLimitRuleModelList[${modelStatus.index}].distributorId">
								<option value="-1">--All--</option>
								<c:if test="${not empty distributorModelList}">
									<html:options items="${distributorModelList}" itemLabel="name" itemValue="distributorId" />
								</c:if>
							</html:select></td>
						<td class="handler_account_type">
							<html:select id="accountTypeId${modelStatus.index}" cssClass="textBox" path="productLimitRuleModelList[${modelStatus.index}].accountTypeId">
								<option value="">--Select--</option>
								<html:options items="${accountTypeModelList}" itemLabel="name" itemValue="customerAccountTypeId" />
							</html:select>
						</td>
						<td class="min_limit"><html:input id="min_limit${modelStatus.index}"  type="text" cssClass="textBox" path="productLimitRuleModelList[${modelStatus.index}].minLimit" onkeypress="return checkNumeric(this,event);" maxlength="7"/></td>
						<td class="max_limit"><html:input id="max_limit${modelStatus.index}" type="text" cssClass="textBox" path="productLimitRuleModelList[${modelStatus.index}].maxLimit" onkeypress="return checkNumeric(this,event)" maxlength="7"/></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<table style="width:100%;">
			<tr>
				<% if(UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L)) %>
					<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_PRDCT_LMT_UPDATE%>">
					<td colspan="5" style="padding-bottom: 30px"><input id="addBtn" type="button" value="Add row"></td>
					</authz:authorize>
				<% if(UserUtils.getCurrentUser().getMnoId() == null || (UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50027L))) %>
				<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_PRDCT_LMTS_UPDATE%>">
					<td colspan="5" style="padding-bottom: 30px"><input id="addBtn" type="button" value="Add row"></td>
				</authz:authorize>
			</tr>
			<tr>
				<td colspan="5" align="center">
					<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_PRDCT_LMT_UPDATE%>">
						<input type="submit" value="Save" class="button" disabled="disabled"/>
					</authz:authorize>
					<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_PRDCT_LMTS_UPDATE%>">
						<input type="submit" value="Save" class="button" disabled="disabled"/>
					</authz:authorize>
					<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_LMTS_UPDATE %>">
						<input name="reset" type="reset" onclick="javascript: window.location='productupdateform.html?productId=${param.productId}'" class="button" value="Cancel" />
					</authz:authorize>
					<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_LMT_READ %>">
						<input name="reset" type="reset" onclick="javascript: window.location='productsearch.html'" class="button" value="Cancel" />
					</authz:authorize>
				</td>
			</tr>
		</table>
	</html:form>

	<html:form commandName="productLimitRuleVo" >
		<div style="visibility: hidden;">
			<html:input id="activeStatusId" cssClass="delete" name="activeStatusId" path="active" type="hidden" value="true"/>
			<html:input id="productLimitRuleId" name="productLimitRuleId" path="productLimitRuleId" type="hidden"/>
			<html:select id="channelId" name="channelId" cssClass="textBox" path="deviceTypeId">
				<option value="-1">--All--</option>
				<html:options items="${deviceTypeModelList}" itemLabel="name" itemValue="deviceTypeId" />
			</html:select>
			<html:select id="segmentId" name="segmentId" cssClass="textBox" path="segmentId">
				<option value="-1">--All--</option>
				<html:options items="${segmentModelList}" itemLabel="name" itemValue="segmentId" />
			</html:select>
			<html:select id="distributorId" name="distributorId" cssClass="textBox" path="distributorId">
				<option value="-1">--All--</option>
				<html:options items="${distributorModelList}" itemLabel="name" itemValue="distributorId" />
			</html:select>
			<html:select id="accountTypeId" cssClass="textBox" path="accountTypeId">
				<option value="">--Select--</option>
				<html:options items="${accountTypeModelList}" itemLabel="name" itemValue="customerAccountTypeId" />
			</html:select>
			<input id="min_limit" name="min_limit" type="text" class="textBox" onkeypress="return maskNumber(this,event);" maxlength="7"/>
			<input id="max_limit" name="max_limit" type="text" class="textBox" onkeypress="return maskNumber(this,event);" maxlength="7"/>
		</div>
	</html:form>
</body>
</html>
