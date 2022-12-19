<!--Author: Asad Hayat-->

<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta name="decorator" content="decorator2">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/table.css" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="styles/jquery.multiselect.css" />
	<link rel="stylesheet" type="text/css" href="styles/jquery-ui-custom.min.css" />
	<meta name="title" content="Product Catalog"/>
	<script type="text/javascript" src="<c:url value="/scripts/toolbar.js"/>"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.0.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-custom.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/productcatalogform.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/table.js"></script>
	<style>
		#table_form{
			display:block;
			width:100%;
			height:auto;
			margin:0px;
			padding:0px;
		}
		#table_form .left{
			display:block;
			width:49%;
			float:left;
		}
		#table_form .right{
			display:block;
			width:49%;
			float:right;
		}
	</style>



	<script language="javascript" type="text/javascript">
		function checkUncheckAll(prefix,src)
		{

			var fieldValue = "SELECTED";
			var checkboxValue = true;

			if(src.checked == false)
			{
				fieldValue = "UNSELECTED";
				checkboxValue = false;
			}

			var elems = document.getElementsByTagName("input");
			for(i=0;i<elems.length;i++)
			{
				if(elems[i].name.substring(0,8) == prefix)
				{
					elems[i].value = fieldValue;
					//i+=2;
					elems[i].checked = checkboxValue;
				}
			}

		}
		function error(request)
		{
			alert("An unknown error has occured. Please contact with the administrator for more details");
		}

		function checkUncheckMaster(masterCheckBoxName,src)
		{
			if(src.name != masterCheckBoxName)
			{
				var master = eval("document.forms[0]."+masterCheckBoxName);
				if(src.checked == true)
				{
					var checkedFlag = true;
					var elems = document.getElementsByTagName("input");
					for(i=0;i<elems.length;i++)
					{
						if(elems[i].type == "hidden" && elems[i].name.substring(0,8) == "checkBox" && elems[i].value == "UNSELECTED")
						{
							checkedFlag = false;
							break;
						}
					}
					if(checkedFlag == true)
					{
						master.checked = true;
					}
				}
				else
				{
					master.checked = false;
				}
			}
		}

		function enableImage(cellObject)
		{

			var str = cellObject.innerHTML;
			var name = cellObject.id;
			var productObj = document.getElementById('productName');
			var supplierObj = document.getElementById('supplierName');
			var substr;
			if(navigator.userAgent.indexOf("Firefox") != -1)
			{
				substr = "<img";
			}
			else if(navigator.userAgent.indexOf("MSIE") != -1)
			{
				substr = "<IMG";
			}

			if(document.forms[0].sort.value=='')
			{



				document.forms[0].sort.value='asc';
				str = str+' '+'<IMG src=\'/i8Microbank/images/table/sortAsc.gif\'  style=\'border:0\'  alt=\'Arrow\' />';
				cellObject.innerHTML = str;


			}
			else if(document.forms[0].sort.value=='asc' && document.forms[0].sortName.value==name )
			{




				if(str.indexOf(substr) > -1)
				{

					str = str.substring(0,str.indexOf(substr));
				}

				document.forms[0].sort.value='desc';

				str = str+' '+'<IMG src=\'/i8Microbank/images/table/sortDesc.gif\'  style=\'border:0\'  alt=\'Arrow\' />';
				cellObject.innerHTML = str;
				if(cellObject.id=='supplierName')
				{
					var obj = document.getElementById('productName');
					var objStr = obj.innerHTML;
					if(objStr.indexOf(substr) > -1)
						obj.innerHTML = obj.innerHTML.substring(0,objStr.indexOf(substr));
				}
				else if(cellObject.id=='productName')
				{
					var obj = document.getElementById('supplierName');
					var objStr = obj.innerHTML;
					if(objStr.indexOf(substr) > -1)
						obj.innerHTML = obj.innerHTML.substring(0,objStr.indexOf(substr));
				}

			}
			else if(document.forms[0].sort.value=='desc' && document.forms[0].sortName.value==name )
			{

				if(str.indexOf(substr) > -1)
				{
					str = str.substring(0,str.indexOf(substr));
				}

				document.forms[0].sort.value='asc';

				str = str+' '+'<IMG src=\'/i8Microbank/images/table/sortAsc.gif\'  style=\'border:0\'  alt=\'Arrow\' />';
				cellObject.innerHTML = str;
				if(cellObject.id=='supplierName')
				{
					var obj = document.getElementById('productName');
					var objStr = obj.innerHTML;
					if(objStr.indexOf(substr) > -1)
						obj.innerHTML = obj.innerHTML.substring(0,objStr.indexOf(substr));
				}
				else if(cellObject.id=='productName')
				{
					var obj = document.getElementById('supplierName');
					var objStr = obj.innerHTML;
					if(objStr.indexOf(substr) > -1)
						obj.innerHTML = obj.innerHTML.substring(0,objStr.indexOf(substr));
				}

			}
			else if(document.forms[0].sort.value=='asc' && document.forms[0].sortName.value=='productName' && cellObject.id == 'supplierName')
			{


				if(str.indexOf(substr) > -1)
				{
					str = str.substring(0,str.indexOf(substr));
				}

				document.forms[0].sort.value='desc';

				str = str+' '+'<IMG src=\'/i8Microbank/images/table/sortDesc.gif\'  style=\'border:0\'  alt=\'Arrow\' />';
				cellObject.innerHTML = str;
				var obj = document.getElementById('productName');
				var objStr = obj.innerHTML;
				if(objStr.indexOf(substr) > -1)
					obj.innerHTML = obj.innerHTML.substring(0,objStr.indexOf(substr));


			}
			else if(document.forms[0].sort.value=='asc' && document.forms[0].sortName.value=='supplierName' && cellObject.id == 'productName')
			{

				if(str.indexOf(substr) > -1)
				{
					str = str.substring(0,str.indexOf(substr));
				}

				document.forms[0].sort.value='asc';

				str = str+' '+'<IMG src=\'/i8Microbank/images/table/sortAsc.gif\'  style=\'border:0\'  alt=\'Arrow\' />';
				cellObject.innerHTML = str;

				var obj = document.getElementById('supplierName');
				var objStr = obj.innerHTML;
				if(objStr.indexOf(substr) > -1)
					obj.innerHTML = obj.innerHTML.substring(0,objStr.indexOf(substr));

			}
			else if(document.forms[0].sort.value=='desc' && document.forms[0].sortName.value=='productName' && cellObject.id == 'supplierName')
			{

				if(str.indexOf(substr) > -1)
				{
					str = str.substring(0,str.indexOf(substr));
				}
				document.forms[0].sort.value='asc';
				str = str+' '+'<IMG src=\'/i8Microbank/images/table/sortAsc.gif\'  style=\'border:0\'  alt=\'Arrow\' />';
				cellObject.innerHTML = str;
				var objStr = productObj.innerHTML;
				if(objStr.indexOf(substr) > -1)
					productObj.innerHTML = productObj.innerHTML.substring(0,objStr.indexOf(substr));
			}
			else if(document.forms[0].sort.value=='desc' && document.forms[0].sortName.value=='supplierName' && cellObject.id == 'productName')
			{

				if(str.indexOf(substr) > -1)
				{
					str = str.substring(0,str.indexOf(substr));
				}
				document.forms[0].sort.value='asc';
				str = str+' '+'<IMG src=\'/i8Microbank/images/table/sortAsc.gif\'  style=\'border:0\'  alt=\'Arrow\' />';
				cellObject.innerHTML = str;
				var obj = document.getElementById('supplierName');
				var objStr = obj.innerHTML;
				if(objStr.indexOf(substr) > -1)
					obj.innerHTML = obj.innerHTML.substring(0,objStr.indexOf(substr));
			}


			document.forms[0].sortName.value=cellObject.id;



		}

	</script>
</head>
<body bgcolor="#ffffff">

<spring:bind path="productCatalogModel.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="errorMsg">
			<c:forEach var="error" items="${status.errorMessages}">
				<c:out value="${error}" escapeXml="false" />
				<br/>
			</c:forEach>
		</div>
	</c:if>
</spring:bind>
<c:if test="${not empty messages}">
	<div class="infoMsg" id="successMessages">
		<c:forEach var="msg" items="${messages}">
			<c:out value="${msg}" escapeXml="false"/>
			<br/>
		</c:forEach>
	</div>
	<c:remove var="messages" scope="session"/>
</c:if>

<!-- productCatalogId-->
<form id="productCatalogForm" method="post" action="productcatalogform.html" onsubmit="return onFormSubmit(this)">
	<c:if test="${not empty param.productCatalogId}">
		<input type="hidden" name="isUpdate" id="isUpdate" value="true" />


		<spring:bind path="productCatalogModel.versionNo">
			<input type="hidden" name="${status.expression}" value="${status.value}"/>
		</spring:bind>
		<spring:bind path="productCatalogModel.createdBy">
			<input type="hidden" name="${status.expression}" value="${status.value}"/>
		</spring:bind>


		<spring:bind path="productCatalogModel.createdOn">
			<input type="hidden" name="${status.expression}" value="${status.value}"/>
		</spring:bind>
		<spring:bind path="productCatalogModel.updatedOn">
			<input type="hidden" name="${status.expression}" value="${status.value}"/>
		</spring:bind>

		<spring:bind path="productCatalogModel.updatedBy">
			<input type="hidden" name="${status.expression}" value="${status.value}"/>
		</spring:bind>

		<spring:bind path="productCatalogModel.productCatalogId">
			<input type="hidden" name="${status.expression}" value="${status.value}"/>
		</spring:bind>
	</c:if>





	<br/>

	<table width="100%" border="0" cellpadding="0" cellspacing="1">

		<tr>
			<td align="right" class="formText"><span style="color:#FF0000">*</span>Name:&nbsp;</td>
			<td><html:input path="productCatalogModel.name" cssClass="textBox" maxlength="50" /></td>
		</tr>

		<tr>
			<td align="right" class="formText"><span style="color:#FF0000">*</span>User Type:&nbsp;</td>

			<td align="left"><html:select onchange="loadProductByUserType()" id="appUserTypeId" path="productCatalogModel.appUserTypeId" cssClass="textBox" tabindex="1">
				<html:option value="">---Select---</html:option>
				<c:if test="${appUserTypeModelList != null}" >
					<html:options items="${appUserTypeModelList}" itemValue="appUserTypeId" itemLabel="name"/>
				</c:if>
			</html:select>
			</td>
		</tr>


		<tr>
			<td align="right" class="formText">Description:&nbsp;</td>
			<td align="left">
				<spring:bind path="productCatalogModel.description">
					<textarea name="${status.expression}" cols="50" rows="5" class="textBox" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" >${status.value}</textarea>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td align="right" class="formText">Comments:&nbsp;</td>
			<td align="left">
				<spring:bind path="productCatalogModel.comments">
					<textarea name="${status.expression}" cols="50" rows="5" class="textBox" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" >${status.value}</textarea>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td align="right" class="formText">Active:&nbsp;</td>
			<td align="left">
				<spring:bind path="productCatalogModel.active">
					<input name="${status.expression}" type="checkbox" value="true"
						   <c:if test="${status.value==true}">checked="checked"</c:if>
						   <c:if test="${empty param.productCatalogId && empty param._save}">checked="checked"</c:if>
						   <c:if test="${status.value==false && not empty param._save }">unchecked="checked"</c:if>
					/>
				</spring:bind>
			</td>
		</tr>

		<input type = "hidden" name="sort" value=""/>
		<input type = "hidden" name="sortName" value=""/>
		<input type="hidden" name="sortedOn"/>

	</table>


	<table border="0" width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<fieldset class="cold" width="1000">
					<div class="eXtremeTable">
						<table class="tableRegion" id="ec_table" width="700" align="center">
							<thead>
							<tr>
								<td  width="100" class="tableHeader" width="10%" allign="right"> <input type="checkbox" style="margin-left:1;width:13px;height:13px;overflow:hidden;" name="all" onclick="javascript:checkUncheckAll('checkBox',this)" <c:if test="${not empty param.all}" >checked="checked"</c:if></td>
								<td  width="100" class="tableHeader" width="10%" allign="right">Product</td>
								<td  width="100"  class="tableHeader" width="10%" allign="right">Supplier</td>
								<td  width="100" class="tableHeader" width="10%" allign="right">Service</td>
							</tr>
							</thead>
							<tbody>

							</tbody>
						</table>
					</div>
				</fieldset>
			</td>
		</tr>
	</table>



	<table border="0" width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center">
				<input type="button" name="_save" onclick="javascript: onFormSubmit(document.forms[0])" value="  Save  " class="button" />
				<input type="reset" name="_cancel" onclick="javascript:window.location='productcatalogmanagement.html'" value="Cancel"  class="button" />

			</td>
		</tr>
	</table>

</form>


<div style="visibility: hidden">
	<table id="grid">
		<tr id="templateRow">
			<td align="center"><input id="activeProduct" name="activeProduct" type="checkbox" value="true"></td>
			<td align="left"><input type="hidden" id="name" name="name" /></td>
			<td align="left"><input type="hidden" id="supplierName" name="supplierName" /></td>
			<td align="left"><input type="hidden" id="serviceName" name="serviceName" /></td>
		</tr>
	</table>
</div>



<script type="text/javascript">

	var productId =<%=request.getParameter("productCatalogId")%>;

	highlightFormElements();
	/*if( document.forms[0].isUpdate == null ) {
	 document.forms[0].name.focus();
	 }
	 else {
	 document.forms[0].description.focus();
	 }
	 */

	function setProductSelectionState(chkbx) {
		//make sure that always know the state of the checkboxon

		if (chkbx.checked) {
			eval('document.forms.productCatalogForm.checkBox' + chkbx.name).value='SELECTED';
		}

		else {
			eval('document.forms.productCatalogForm.checkBox' + chkbx.name).value='UNSELECTED';
		}
	}


	function doRequired( field, label )
	{
		if( field.value.trim() == '' || field.value.length == 0 )
		{
			alert( label + ' is required field.' );
			return false;
		}
		return true;
	}

	function onFormSubmit(theForm) {

		if(doRequired( document.getElementById("name"), 'Name' )
				&& doRequired( document.getElementById("appUserTypeId"), 'User Type')){

		}else {
			return false;
		}
		theForm.submit();
	}

	//added by aali
	function getRowIndex(row){
		row.rowIndex;
	}

	var clickedRowId = -1 ;
	var clickedRowBackground="#BBDDFF";
	function onRowClicked( row )
	{
		//Clears the previously clicked row
		if( clickedRowId != -1 ) {
			document.getElementById("ec_table").rows[clickedRowId].style.backgroundColor = clickedRowBackground;
			//"#9DB8E3" ;
		}
		clickedRowId = row.rowIndex ;
		clickedRowBackground=row.style.backgroundColor;
		row.style.backgroundColor = "#BBDDFF" ;
	}

	//swapNode is only supported by IE, so replaced with swap(a, b)
	// Funtion which moves the row upward
	function moveUpward() {
		if( clickedRowId > 2 && clickedRowId != -1)
			swap(document.getElementById("ec_table").rows[clickedRowId], document.getElementById("ec_table").rows[--clickedRowId])
	}

	// Funtion which moves the row downward
	function moveDown() {
		if(clickedRowId != -1 && clickedRowId < (document.getElementById("ec_table").rows.length -1) )
			swap(document.getElementById("ec_table").rows[clickedRowId], document.getElementById("ec_table").rows[++clickedRowId])
	}

	function swap(a, b) {
		var pa1= a.parentNode, pa2= b.parentNode, sib= b.nextSibling;
		if(sib === a)
			sib= sib.nextSibling;

		pa1.replaceChild(b, a);

		if(sib)
			pa2.insertBefore(a, sib);
		else
			pa2.appendChild(a);
	}
</script>

<v:javascript formName="productCatalogModel" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

</body>
</html>