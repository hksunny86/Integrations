<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Bill Payment</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
		
		<script type="text/javascript" src="../scripts/ajaxtags-1.2-beta2.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/overlibmws.js"></script>
		<link type="text/css" rel="stylesheet" href="${contextPath}/styles/ajaxtags.css" />
	
		<script language="javascript">	
		
		var rowNum = 0;
		
		function removeTableRow(row, amount) {
			
			$(row).closest('tr').remove();
			rowNum--;
			
			document.getElementById('totalRowz').innerHTML = '* '+rowNum + ' Record(s) of 100';
			document.getElementById('totalRows').value = rowNum;
				
			var totalAmount = document.getElementById("totalAmount");	
			var totalAmt = parseInt(totalAmount.innerHTML);	
			var _amount = parseInt(amount);
			totalAmount.innerHTML = totalAmt - _amount;
		}
		
		function callback(xmlResponse, row, productId, img, selectList, consumerNo) {
			
			var xmlDoc = null; 
			var parser = null;
			var BDDATE = "";
			var BAMT = "";
			var BPAID = "";
			var AFTER_DUE_DATE = "";
			var ERROR = "";

			if(xmlResponse.indexOf('Your request cannot be processed at the moment. Please try again later') >-1) {
				alert('System Error Occured\n\nYour request cannot be processed at the moment. Please try again later' +'\n\n Deleting Row.');				
				removeTableRow(row, 0);
				hideInfoLoading();
				return;
			}
			if(xmlResponse.indexOf('The Utility Bill has already been paid') >-1) {
				alert('System Error Occured\n\nThe Utility Bill has already been paid.' +'\n\n Deleting Row.');				
				removeTableRow(row, 0);
				hideInfoLoading();
				return;
			}
			
			var isIE = (navigator.appName == 'Netscape') ? false : true;
			
			if(isIE) {

          		xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
          	  	xmlDoc.async = false;
          	  	xmlDoc.loadXML(xmlResponse); 
          	  	
			} else {
				  
				parser = new DOMParser();
				xmlDoc = parser.parseFromString(xmlResponse,"text/xml")
			}

			var isPartial= isPartialProduct();
			
			  var childNodes = xmlDoc.getElementsByTagName('param');
			
			  if(childNodes.length == 0) {
				  
				  childNodes = xmlDoc.getElementsByTagName('error');
			  }
			  
			  for(var i=0; i<childNodes.length; i++) {

				var attributeValue = null; 
				var nodeValue = null; 
				
				if(isIE) {
					 attributeValue = childNodes.item(i).attributes[0].text;					
				} else {
					 attributeValue = childNodes.item(i).attributes[0].textContent;					
				}
				
				nodeValue = childNodes.item(i).childNodes[0].nodeValue;
				
				if(attributeValue == 'BDDATE') {
					BDDATE = nodeValue;
				}
				if(attributeValue == 'BAMT') {
					BAMT = nodeValue;
				}
				if(attributeValue == 'BPAID') {
					BPAID = nodeValue;
				}
				if(attributeValue == 'AFTER_DUE_DATE') {
					AFTER_DUE_DATE = nodeValue;
				}
				if(attributeValue == '9001') {
					ERROR = nodeValue;
				}	
			}
			  
			if(ERROR !='') {
				//alert(ERROR +'\n Deleting Row.');
				
				BPAID = ERROR;
				
				//removeTableRow(row, 0);

				
				hideInfoLoading();
				return;
			}
		
			row.cells[3].innerHTML = BDDATE;
			row.cells[4].innerHTML = BAMT;
			row.cells[5].innerHTML = AFTER_DUE_DATE;
			
			if(BPAID == 0 || BPAID == '0') {
				BPAID = 'Bill Unpaid';
			} else if(BPAID == 1 || BPAID == '1') {
				BPAID = 'Bill Paid';
			}
			
			row.cells[8].innerHTML = BPAID;
			  
			var amountField = document.getElementById('AMOUNT_'+rowNum);	
			amountField.value = BAMT;
			
			if(!isPartialProduct(productId)) {
				amountField.setAttribute("readOnly", "true");
			}			
			img.onclick = null;
            img.onclick = function() {
            	
            	removeTableRow(img, BAMT);
			};

			var totalAmount = document.getElementById("totalAmount");	
			var totalAmt = parseInt(totalAmount.innerHTML);	
			var _amount = parseInt(BAMT);
			totalAmount.innerHTML = totalAmt + _amount;			

			hideInfoLoading();
			
			addTableRow();		

			consumerNo.readOnly = true; 
            selectList.onclick = function() {
            	
				alert('You Cant Alter Bill Details.');
			};	
		}				
		
		
		function hideInfoLoading() {

			var billInfoLoading = document.getElementById("billInfoLoading");
			billInfoLoading.style.visibility = 'hidden';
		}
		
		
		function validateConsumerNumber(consumerNumber, rowId) {
			
			var isValid = true;

			for(var i=1; i<=100;i++) {

			var CONSUMER_NUMBER = document.getElementById("CONSUMER_NUMBER_"+i);
			
				if(i != rowId && CONSUMER_NUMBER != null && CONSUMER_NUMBER.value == consumerNumber) {
									
					alert('Consumer/Bill Number ('+consumerNumber+') already exists in List.');
					return false;
				}
			}
			return isValid;
		}
		
		function isPartialProduct(productId) {
			
			var isPartialProduct = false;
				
			var partialPaymentList = ['2510739', '2510740', '2510741', '2510744', '2510745', '2510747', '2510748', '2510749'];
			var i = partialPaymentList.indexOf(productId);

			if(i > -1) {	
				
				isPartialProduct = true;
			}
			
			return isPartialProduct;
		}

		function showInfoLoading() {
			var billInfoLoading = document.getElementById("billInfoLoading");
			billInfoLoading.style.visibility = 'visible';				
		}
		
		function callAjax(consumerNo, selectList, row, img) {
									
			if(!validateConsumerNumber(consumerNo.value, row.id)) {
				return;
			}

			showInfoLoading();			
			
			var selectedIndex = selectList.selectedIndex;
			var productId = selectList.options[selectedIndex].value;
					    	
		    $.ajax({
		            type: "GET",
		            url: "fetchBillInfo.aw?CSCD="+consumerNo.value+"&PID="+productId,
		            dataType: "xml",
		            success: function(xml, status, xhr){
		            	
		            	var xmlResponse = xhr.responseText;
		            	//alert(xmlResponse);
		            	callback(xmlResponse, row, productId, img, selectList, consumerNo);
		            },
		            error: function() {
		            alert("An error occurred while processing XML file.");
		            }
		        });			
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
		
		
		function validateForm() {
			
			for(var i=1; i<=rowNum; i++) {
				
				if(!validatePreviousRow(i)){

					return false;
				}
			}
			
			var PIN = document.getElementById("PIN");
		
			if(PIN.value == '') {
				alert('There is no PIN entered, Kindly Enter 4 Digit PIN Code.');		
				return false;
			}

			if(!validateNumber(PIN) || PIN.value.length < 4) {
				alert('Kindly Enter 4 Digit PIN Code.');			
				return false;
			}
			
			if(rowNum < 1) {
				alert('Kindly Enter atleast One Bill Payment.');			
				return false;
			}		
			
			if(navigator.appName == 'Netscape') {		
				document.getElementById("confirm").disabled = true;
			} else {
				document.getElementById("confirm").disabled = 'disabled';
			}
			
			return true;
		}
		
		
		function validatePreviousRow(rowNum) {

			var CONSUMER_NUMBER = document.getElementById("CONSUMER_NUMBER_"+rowNum).value;
			var AMOUNT = document.getElementById("AMOUNT_"+rowNum).value;
			var MOBILE_NUMBER = document.getElementById("MOBILE_NUMBER_"+rowNum).value;
			var selectedIndex = document.getElementById("PRODUCT_ID_"+rowNum).selectedIndex;
			var PRODUCT_ID = document.getElementById("PRODUCT_ID_"+rowNum).options[selectedIndex].value;
			
			if(CONSUMER_NUMBER == '') {
				alert('Kindly Enter Consumer Number.');
				return false;
			}
			if(AMOUNT == '') {
				alert('Amount is Missing.');
				return false;
			}
			if(MOBILE_NUMBER == '' || MOBILE_NUMBER.length != 11 || MOBILE_NUMBER.substring(0,2) != '03' || !validateNumber(document.getElementById("MOBILE_NUMBER_"+rowNum))) {
				alert('Kindly Enter Valid Mobile Number.');
				return false;
			}
			if(PRODUCT_ID < 0) {
				alert('Kindly Enter Select Billing Company.');
				return false;
			}
						
			return true;
		}		

		function addTableRow() {

			if(document.getElementById("billInfoLoading").style.visibility != 'hidden') {
				
				return;
				
			} else if(rowNum != 0) {
				
				if(!validatePreviousRow(rowNum)) {
					return;
				}
			}
			
			var billsDetail = document.getElementById('billsDetail');
			var consumerNo = document.getElementById("consumerNo");
			var productList = document.getElementById("productList");
			var selectedIndex = productList.selectedIndex;
			var productName = document.getElementById("productList").options[selectedIndex].text;
			var productId = document.getElementById("productList").options[selectedIndex].value;
		    
			if(rowNum == 100) {
				alert('Reached Maximum Limit of 100 Records.');
				return;
			}
			
			rowNum++;
			
			document.getElementById('totalRowz').innerHTML = rowNum + ' Record(s) of 100';
			document.getElementById('totalRows').value = rowNum;
			
			var row = billsDetail.insertRow(0);
			row.setAttribute('id', rowNum);
						
			var cell1  = row.insertCell(0);
			var cell2  = row.insertCell(1);
			var cell3  = row.insertCell(2);
			var cell4  = row.insertCell(3);
			var cell5  = row.insertCell(4);
			var cell6  = row.insertCell(5);
			var cell7  = row.insertCell(6);
			var cell8  = row.insertCell(7);
			var cell9  = row.insertCell(8);
			var cell10 = row.insertCell(9);

			cell1.style.width="11%";
			cell2.style.width="11%";
			cell3.style.width="11%";
			cell4.style.width="11%";
			cell5.style.width="10%";
			cell6.style.width="9%";
			cell7.style.width="9%";
			cell8.style.width="9%";
			cell9.style.width="9%";
			cell10.style.width="4%";

			var consumerNumber = document.createElement("INPUT");
			consumerNumber.setAttribute("type", "text"); 
			consumerNumber.setAttribute("size", "8");
			consumerNumber.setAttribute('id', "CONSUMER_NUMBER_"+rowNum);
			consumerNumber.setAttribute('name', 'CONSUMER_NUMBER_'+rowNum);
			consumerNumber.setAttribute("value", '');
			cell1.appendChild(consumerNumber);

			var mobileNumber = document.createElement("INPUT");
			mobileNumber.setAttribute("type", "text"); 
			mobileNumber.setAttribute("size", "11");
			mobileNumber.setAttribute("maxlength", "11");
			mobileNumber.value = '';
			mobileNumber.setAttribute('id', 'MOBILE_NUMBER_'+rowNum);
			mobileNumber.setAttribute('name', 'MOBILE_NUMBER_'+rowNum);
			cell2.appendChild(mobileNumber);			

			var img = document.createElement('img');
			img.src = 'images/aw/delete.jpg';
			img.setAttribute('width', '18px');
			img.setAttribute('height', '18px');
			img.setAttribute('border', '0');
			img.alt = 'Remove Bill Payment';	

            img.onclick = function() {
            	
            	removeTableRow(img, 0);
			};
			
	        cell10.appendChild(img);	
						
			var selectList = document.createElement('SELECT');
			selectList.setAttribute('id', 'PRODUCT_ID_'+rowNum);
			selectList.setAttribute('name', 'PRODUCT_ID_'+rowNum);

            for (var i = 0; i < productList.options.length; i++) {
               
                 var newOption = document.createElement("option");
                 newOption.text = productList.options[i].text;
                 newOption.value = productList.options[i].value;
                 selectList.options[selectList.options.length] = newOption;
            }			

            selectList.onclick = function() {

				if(consumerNumber.value =='') {
					alert('Kindly Enter Consumer Number First.');
					return;
				}

				if(!validateConsumerNumber(consumerNumber.value, row.id)) {
					return;
				}

				if(mobileNumber.value =='') {
					alert('Kindly Enter Mobile Number First.');
					return;
				}
			};			

            selectList.onchange = function() {
            	
				callAjax(consumerNumber, selectList, row, img);
			};	
			
			cell3.appendChild(selectList);
			
			var amountField = document.createElement("INPUT");
			amountField.setAttribute("size", "8");;
			amountField.setAttribute('id', 'AMOUNT_'+rowNum);
			amountField.setAttribute('name', 'AMOUNT_'+rowNum);
			amountField.setAttribute("type", "text"); 
			amountField.setAttribute("value", '0');
			cell8.appendChild(amountField);		
						
			if((rowNum%2) == 0) {
				cell1.style.backgroundColor = 'lightblue';
				cell2.style.backgroundColor = 'lightblue';
				cell3.style.backgroundColor = 'lightblue';
				cell4.style.backgroundColor = 'lightblue';
				cell5.style.backgroundColor = 'lightblue';
				cell6.style.backgroundColor = 'lightblue';
				cell7.style.backgroundColor = 'lightblue';
				cell8.style.backgroundColor = 'lightblue';
				cell9.style.backgroundColor = 'lightblue';
			}	
			
			hideInfoLoading();
			
		}
		
		</script>		
		
	</head>

	<body oncontextmenu="return false">
		<table cellpadding="0" cellspacing="0" height="100%" width="85%" align="center" class="container">
			<tr height="95px">
				<td>
					<jsp:include page="header.jsp"></jsp:include>
				</td>
			</tr>
			<tr id="contents" valign="top">
				<td>
					<form name="billpaymentform" method="post" action='<c:url value="/bulkBillPayment.aw"/>' onsubmit='return validateForm()'>
					
						<table align="center" width="100%" border="0">							
							<tr>
								<td width="25%" colspan="2" align="left" valign="top">
                                    <select hidden="hidden" style="visibility:hidden" 
                                    	id="productList" style="width:50px;" onchange="isPartialProduct()">
                                        <option value="-1">--Select--</option>											
                                        <c:forEach var="product" items="${requestScope.ProdCatalogDetailListViewModelList}">
                                            <option value="<c:out value="${product.productId}"/>">
                                                <c:out value="${product.productName}"/>
                                            </option> 
                                        </c:forEach>
                                    </select>                                    
                                </td>
								<td rowspan="2" align="center" width="50%">
									<div align="center" class="hf">Bulk Bill Payment</div>                                    
								</td>
								<td rowspan="2" align="center" width="25%">
                                    <img id='billInfoLoading' style="visibility: hidden;" src='images/aw/billInfoLoading.gif' 
                                     border='0' align='middle' alt='Wait Loading Bill Info'/> 
                                </td>
							</tr>
							<tr>
							  <td align="center" valign="bottom" id="totalRowz" width="20%">&nbsp;</td>
							  <td align="left" width="5%"><input id="totalRows" name="totalRows" type="hidden" size="3" value="0" /></td>
					      </tr>						
								<tr>
								  <td colspan="4"><div style="width:calc(100% - 17px);" align="center">
								  <table id="theTable" width="96%" cellpadding="0" cellspacing="0" border='0'>
							          <tbody align="center">
							            <tr>
							              <td width="11%" style="background-color: #0060af;font-size: 13px;font-weight: bold;padding:7px; color: white">Consumer #</td>
							              <td width="11%" style="background-color: #0060af;font-size: 13px;font-weight: bold;padding:7px; color: white">Mobile #</td>
							              <td width="11%" style="background-color: #0060af;font-size: 13px;font-weight: bold;padding:7px; color: white">Company</td>
							              <td width="11%" style="background-color: #0060af;font-size: 13px;font-weight: bold;padding:7px; color: white">Duedate</td>
							              <td width="10%" style="background-color: #0060af;font-size: 13px;font-weight: bold;padding:7px; color: white">Amount within Duedate</td>
							              <td width="9%" style="background-color: #0060af;font-size: 13px;font-weight: bold;padding:7px; color: white">Amount after Duedate</td>
							              <td width="9%" style="background-color: #0060af;font-size: 13px;font-weight: bold;padding:7px; color: white">Amount Payable</td>
							              <td width="9%" style="background-color: #0060af;font-size: 13px;font-weight: bold;padding:7px; color: white">Amount</td>
							              <td width="9%" style="background-color: #0060af;font-size: 13px;font-weight: bold;padding:7px; color: white">Remarks</td>
							              <td width="4%"><img src='images/aw/add.jpg' border='0' align='middle' alt='Add Bill Payment' style='width: 18px; height: 18px' onclick="addTableRow()" /></td>
							            </tr>
							          </tbody>
							        </table>		
							        </div>		

									<div style="overflow:scroll; border: medium;height:170px" align="center">
									<table id="billsDetail" width="96%" cellpadding="0" cellspacing="0" border='0'>
							          <thead align="center">

							          </thead>
							        </table>			
							        </div>	
									<div style="width:calc(100% - 17px);" align="center">
									<table width="96%" cellpadding="0" cellspacing="0" border='0'>
							          <tbody align="left">
							            <tr>
							              <td width="11%" align="right">Available Balance :&nbsp;</td>
							              <td width="11%" align="left">${requestScope.balance}</td>
							              <td width="11%" align="right">Total Amount :&nbsp;</td>
							              <td width="11%" id='totalAmount' align="left">0.0</td>
							              <td width="10%">&nbsp;</td>
							              <td width="9%">&nbsp;</td>
							              <td width="9%" align="right">
							              	<security:token/>
											<span style="color: #FF0000">*</span>
											<span class="label">PIN : </span>
										  </td>
							              <td width="9%" align="left"><input type="password" name="PIN" id="PIN" maxlength="4" size="4" class="text"/></td>
							              <td width="9%"><input id='confirm' type="submit" value="Confirm" /></td>
							              <td width="4%"><img src='images/aw/add.jpg' border='0' align='middle' alt='Add Bill Payment' style='width: 18px; height: 18px' onclick="addTableRow()" /></td>
							            </tr>
							          </tbody>
							        </table>		
							        </div>					
								</td>
							</tr>							
						</table>
					</form>
				</td>
			</tr>
			<tr valign="bottom">
				<td>
					<jsp:include page="footer.jsp"></jsp:include>
				</td>
			</tr>
		</table>
	</body>
	
	<script language="javascript">	
	 addTableRow();
	</script>
</html>