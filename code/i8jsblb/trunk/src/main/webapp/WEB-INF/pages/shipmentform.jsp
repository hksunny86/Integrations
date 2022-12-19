<!--Author: Asad Hayat-->

<%@ include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator2">

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="title" content="Shipment"/>
<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
<script type="text/javascript">
AbstractResponseParser = function() {};
CustomResponseXmlParser = Class.create();
CustomResponseXmlParser.prototype = Object.extend(new AbstractResponseParser(), {
  initialize: function() {
    this.type = "xml";
  },

  load: function(request) {
    this.content = request.responseXML;
    this.parse();
  },

  parse: function() {
    var root = this.content.documentElement;
    var responseNodes = root.getElementsByTagName("response");
    this.itemList = new Array();
    if (responseNodes.length > 0) {
      var responseNode = responseNodes[0];
      var itemNodes = responseNode.getElementsByTagName("item");
      for (var i=0; i<itemNodes.length; i++) {
        var nameNodes = itemNodes[i].getElementsByTagName("name");
        var valueNodes = itemNodes[i].getElementsByTagName("value");
        if (nameNodes.length > 0 && valueNodes.length > 0) {
          var name = nameNodes[0].firstChild.nodeValue;
          var value = valueNodes[0].firstChild.nodeValue;
          if(name == "isVariableProduct")
          {
          	if(value=="true")
          	{
				$('creditAmount').value="";
				$('outstandingCredit').value="";
				$('creditAmount').readOnly=false;
				$('outstandingCredit').readOnly=false;
			}
			else
			{
				$('creditAmount').value="0.0";
				$('outstandingCredit').value="0.0";
				$('creditAmount').readOnly=true;
				$('outstandingCredit').readOnly=true;
			}
          }
          else
 	      {
 	         this.itemList.push(new Array(name, value));
 	      }
        }
      }
    }
  }
});


	function setReadonly()
	{
		if($F('isVariableProduct')=="true")
		{
			$('creditAmount').value="";
			$('outstandingCredit').value="";
			$('creditAmount').readOnly=false;
			$('outstandingCredit').readOnly=false;
		}
		else
		{
			$('creditAmount').value="0.0";
			$('outstandingCredit').value="0.0";
			$('creditAmount').readOnly=true;
			$('outstandingCredit').readOnly=true;
		}		
	}
	
	function validateResponse()
	{
	
		
	}
</script>
<!-- if($('productId').option.value<1)
			 alert('This supplier has no product. Shipment must have a product.'); -->
</head>
<body>

<spring:bind path="shipmentModel.*">
  <c:if test="${not empty status.errorMessages}">
    <div class="errorMsg" >
      <c:forEach var="error" items="${status.errorMessages}">
        <c:out value="${error}" escapeXml="false"/>
        <br/>
      </c:forEach>
    </div>
  </c:if>
</spring:bind>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>



<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />	
<%@include file="/common/ajax.jsp"%>

	<script language="javascript" type="text/javascript">
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
      </script>



<form id="shipmentForm" method="post" action="shipmentform.html" onsubmit="return onFormSubmit(this);" >
<table width="100%"  border="0" cellpadding="0" cellspacing="1">


<c:if test="${not empty param.shipmentId}">
  <input type="hidden"  name="isUpdate" id="isUpdate" value="true"/>
  <spring:bind path="shipmentModel.supplierId" >
      <input type="hidden" name="${status.expression}" value="${status.value}"/>
  </spring:bind>
  <spring:bind path="shipmentModel.productId">
      <input type="hidden" name="${status.expression}" value="${status.value}"/>
  </spring:bind>
</c:if>


<spring:bind path="shipmentModel.shipmentId">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="shipmentModel.createdBy">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="shipmentModel.updatedBy">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="shipmentModel.createdOn">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="shipmentModel.updatedOn">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="shipmentModel.versionNo">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<input type="hidden" id="isVariableProduct"/>


<tr bgcolor="FBFBFB">
  <td colspan="2" align="center">&nbsp;</td>
</tr>
          
          <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="#F3F3F3" class="formText"><span style="color:#FF0000">*</span>Supplier:&nbsp;</td>
            <td align="left">
              <spring:bind path="shipmentModel.supplierId" >

							<select name="${status.expression}" class="textBox" id="${status.expression}" <c:if test="${not empty param.shipmentId}">disabled='disabled'</c:if> >
								<c:forEach items="${supplierModelList}" var="supplierModelList">
									<option value="${supplierModelList.supplierId}"
										<c:if test="${status.value == supplierModelList.supplierId}">selected="selected"</c:if>>
										${supplierModelList.name}
									</option>
								</c:forEach>
							</select>
        ${status.errorMessage}

              </spring:bind>
              
                          </td>
          </tr>
                    <tr bgcolor="FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Product:&nbsp;               </td>
            <td width="50%" align="left">
              <spring:bind path="shipmentModel.productId">

							<select name="${status.expression}" class="textBox" id="${status.expression}" <c:if test="${not empty param.shipmentId}">disabled='disabled'</c:if>>
							
							 
							        	<c:if test="${not empty productModelList}">
								<c:forEach items="${productModelList}" var="productModelList">
									<option value="${productModelList.productId}"
										<c:if test="${status.value == productModelList.productId}">selected="selected"</c:if>>
										${productModelList.name}
									</option>
								</c:forEach>
								</c:if>	
							</select>
        ${status.errorMessage}
              </spring:bind>

            </td>
          </tr>
          <tr bgcolor="FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Shipment Type:&nbsp;</td>
            <td width="50%" align="left">
              <spring:bind path="shipmentModel.shipmentTypeId">

							<select name="${status.expression}" class="textBox" id="${status.expression}">
								<c:forEach items="${shipmentTypeModelList}" var="shipmentTypeModelList">
									<option value="${shipmentTypeModelList.shipmentTypeId}"
										<c:if test="${status.value == shipmentTypeModelList.shipmentTypeId}">selected="selected"</c:if>>
										${shipmentTypeModelList.name}
									</option>
								</c:forEach>
							</select>
        ${status.errorMessage}
              </spring:bind>

            </td>
          </tr>
          
          <tr >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Shipment Reference ID:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">
                <c:if test="${not empty shipmentModel.shipmentId}">

                <input type="text" name="shipmentReferenceId" readonly="readonly" class="textBox" value="${shipmentModel.shipmentReferenceId}"/>
                <input name="contactLookupButton" type="button" value="-o" class="button" onClick="javascript:callLookup('shipmentReferenceId','shipmentReferencePopup',600,200)"/>
                </c:if>
                <c:if test="${empty shipmentModel.shipmentId}">

                <input type="text" name="shipmentReferenceId" readonly="readonly" class="textBox" value="${shipmentModel.shipmentReferenceId}"/>
                <input name="contactLookupButton" type="button" value="-o" class="button" onClick="javascript:callLookup('shipmentReferenceId','shipmentReferencePopup',600,200)"/>
                </c:if>
                <img title="Clear Shipment Reference Id" onclick="javascript:document.forms.shipmentForm.shipmentReferenceId.value='';"  style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" /> 
              </td>
          </tr>
          
          <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Payment Mode:&nbsp;</td>
            <td align="left">
              <spring:bind path="shipmentModel.paymentModeId">

							<select name="${status.expression}" class="textBox" id="${status.expression}">
								<c:forEach items="${paymentTypeModelList}" var="paymentTypeModelList">
									<option value="${paymentTypeModelList.paymentModeId}"
										<c:if test="${status.value == paymentTypeModelList.paymentModeId}">selected="selected"</c:if>>
										${paymentTypeModelList.name}
									</option>
								</c:forEach>
							</select>
        ${status.errorMessage}
              </spring:bind>

            </td>
          </tr>
          <tr bgcolor="FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Credit Amount:&nbsp;</td>
            <td width="50%" align="left">
						<spring:bind path="shipmentModel.creditAmount">
							<c:if test="${empty shipmentModel.shipmentId}">
								<c:choose>
									<c:when test="${isVariableProduct==true}">
										<input type="text" 
											id="${status.expression}"
											name="${status.expression}"
											value="${status.value}"
											onkeypress="return maskNumber(this,event)" class="textBox"
											maxlength="14"/>
									</c:when>
									<c:otherwise>
										<input type="text" 
											id="${status.expression}"
											name="${status.expression}"
											value="${status.value}"
											onkeypress="return maskNumber(this,event)" class="textBox"
											maxlength="14" readonly="readonly" />
										</c:otherwise>
								</c:choose>
							</c:if>
							<c:if test="${not empty shipmentModel.shipmentId}">
								<c:choose>
									<c:when test="${isVariableProduct==true}">
										<input type="text" 
											id="${status.expression}"
											name="${status.expression}"
											value="${creditAmount}"
											onkeypress="return maskNumber(this,event)" class="textBox"
											maxlength="14"/>
									</c:when>
									<c:otherwise>
										<input type="text" 
											id="${status.expression}"
											name="${status.expression}"
											value="${creditAmount}"
											onkeypress="return maskNumber(this,event)" class="textBox"
											maxlength="14" readonly="readonly" />
										</c:otherwise>
								</c:choose>
							</c:if>
						</spring:bind>
					</td>
          </tr>

           <tr bgcolor="FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Outstanding Credit:&nbsp;</td>
            <td width="50%" align="left">
						<spring:bind path="shipmentModel.outstandingCredit">
							<c:if test="${empty shipmentModel.shipmentId}">
								<c:choose>
									<c:when test="${isVariableProduct==true}">
										<input type="text" name="${status.expression}" 
											id="${status.expression}"
											value="${status.value}"
											onkeypress="return maskNumber(this,event)" class="textBox"
											maxlength="14"/>
									</c:when>
									<c:otherwise>
										<input type="text" name="${status.expression}"
											id="${status.expression}"
											value="${status.value}"
											onkeypress="return maskNumber(this,event)" class="textBox"
											maxlength="14" readonly="readonly" />
									</c:otherwise>
								</c:choose>
							</c:if>
							<c:if test="${not empty shipmentModel.shipmentId}">
								<c:choose>
									<c:when test="${isVariableProduct==true}">
										<input type="text" name="${status.expression}" 
											id="${status.expression}"
											value="${outstandingCredit}"
											onkeypress="return maskNumber(this,event)" class="textBox"
											maxlength="14"/>
									</c:when>
									<c:otherwise>
										<input type="text" name="${status.expression}"
											id="${status.expression}"
											value="${outstandingCredit}"
											onkeypress="return maskNumber(this,event)" class="textBox"
											maxlength="12" readonly="readonly" />
									</c:otherwise>
								</c:choose>
							</c:if>
						</spring:bind>
					</td>
          </tr>
          <tr bgcolor="FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Purchase Price:&nbsp;</td>
            <td width="50%" align="left">
              <spring:bind path="shipmentModel.price">
                <c:if test="${empty shipmentModel.shipmentId}">
                  <input type="text" name="${status.expression}"  value="${status.value}" onkeypress="return maskNumber(this,event)" class="textBox" maxlength="14"/>
                </c:if>
                <c:if test="${not empty shipmentModel.shipmentId}">
                  <input type="text" name="${status.expression}"  value="${price}" onkeypress="return maskNumber(this,event)" class="textBox" maxlength="14"/>
                </c:if>
              </spring:bind>
            </td>
          </tr>
          
          <tr bgcolor="FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Quantity:&nbsp;</td>
            <td width="50%" align="left">
              <spring:bind path="shipmentModel.quantity">
                <input type="text" name="quantity"  value="${status.value}" onkeypress="return maskNumber(this,event)" class="textBox" maxlength="10" readonly="readonly"/>
              </spring:bind>
            </td>
          </tr>
          
            <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Date Of Purchase:&nbsp;</td>
    <td>
				    <spring:bind path="shipmentModel.purchaseDate">
							<input type="text" name="${status.expression}" class="textBox" id="${status.expression}"
								maxlength="50" value="${status.value}" readonly="readonly" />
						</spring:bind>
						<img id="pDate" tabindex="4"  name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="pDate" tabindex="5" name="popcal" title="Clear Date" onclick="javascript:$('purchaseDate').value=''"  align="middle" style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" /> 
    </td>
  </tr>

            <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Shipment Date:&nbsp;</td>
    <td>
				    <spring:bind path="shipmentModel.shipmentDate">
							<input type="text" name="${status.expression}" class="textBox" id="${status.expression}"
								maxlength="50" value="${status.value}" readonly="readonly" />
						</spring:bind>
						<img id="sDate" tabindex="4"  name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="sDate" tabindex="5" name="popcal" title="Clear Date" onclick="javascript:$('shipmentDate').value=''"  align="middle" style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" /> 
    </td>
  </tr>


            <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Expiry Date:&nbsp;</td>
    <td>
				    <spring:bind path="shipmentModel.expiryDate">
							<input type="text" name="${status.expression}" class="textBox" id="${status.expression}"
								maxlength="50" value="${status.value}" readonly="readonly" />
						</spring:bind>
						<img id="eDate" tabindex="4"  name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="eDate" tabindex="5" name="popcal" title="Clear Date" onclick="javascript:$('expiryDate').value=''"  align="middle" style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" /> 
    </td>
  </tr>


          <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText" >Description:&nbsp;</td>
            <td align="left">
              <spring:bind path="shipmentModel.description">
                <textarea name="${status.expression}" class="textBox" cols="50" rows="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)">${status.value}</textarea>
              </spring:bind>
            </td>
          </tr>
          <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText" >Comments:&nbsp;</td>
            <td align="left">
              <spring:bind path="shipmentModel.comments">
                <textarea name="${status.expression}" class="textBox" cols="50" rows="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)">${status.value}</textarea>
              </spring:bind>
            </td>
          </tr>

<tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="shipmentModel.active">
	    <input name="${status.expression}" type="checkbox" value="true" 	
	    <c:if test="${status.value==true}">checked="checked"</c:if>
	    <c:if test="${empty param.shipmentId && empty param._save}">checked="checked"</c:if>
	    <c:if test="${status.value==false && not empty param._save }">unchecked="checked"</c:if>
	    />			    
     </spring:bind>
    </td>
  </tr>

          <tr bgcolor="FBFBFB">
            <td colspan="2" align="center">&nbsp;</td>

            <tr bgcolor="FBFBFB">
              <td colspan="2" align="center">&nbsp;</td>
            </tr>
            <tr bgcolor="FBFBFB">
              <td colspan="2" align="center">
				<c:if test="${shipmentModel.shipmentId == null}">
                  <input name="Import Entry" type="submit" class="button" value=" Upload Entry " disabled="disabled"/>&nbsp;
                  <input name="Manual Entry" type="submit" class="button" value=" Manual Entry " disabled="disabled"/>&nbsp;
                </c:if>

                <c:if test="${shipmentModel.shipmentId != null}">
                  <input name="Upload Entry" type="button" class="button" value=" Upload Entry " 
                   <c:if test="${shipmentModel.active==false}">disabled="disabled" </c:if>
                      <c:if test="${isVariableProduct==true}">disabled="disabled" </c:if>
                   <c:if test="${isCurDateGTExpiryDate==true}">disabled="disabled"</c:if>
                   onclick="window.location.href='uploadentryform.html?shipmentId=${shipmentModel.shipmentId}&productId=${shipmentModel.productId}&creditAmount=${shipmentModel.creditAmount}';"/>&nbsp;
                  <input name="Manual Entry" type="button" class="button" value=" Manual Entry " 
                  <c:if test="${shipmentModel.active==false}">disabled="disabled" </c:if> 
                 <c:if test="${isVariableProduct==true}">disabled="disabled" </c:if>
                  <c:if test="${isCurDateGTExpiryDate==true}">disabled="disabled"</c:if>
                  onclick="window.location.href='productunitform.html?shipmentId=${shipmentModel.shipmentId}&productId=${shipmentModel.productId}';"/>&nbsp;
                  <input name="Import Entries" type="hidden" class="button" value=" Import Entries "/>
                </c:if>

                <c:if test="${shipmentModel.shipmentId == null}">
                  <input name="_save" type="submit" class="button" value="  Save  "/>&nbsp;
                </c:if>

                <c:if test="${shipmentModel.shipmentId != null}">
                  <input name="_save" type="submit" class="button" value="  Save  "/>&nbsp;
                </c:if>

                <input name="cancel" type="reset" class="button" <c:if test="${empty param.shipmentId}">onclick="javascript:revertDropdowns(document.forms.shipmentForm);"</c:if>  value=" Cancel "/>
              </td>
            </tr>
</table>
		<ajax:select 
			source="supplierId" 
			target="productId"
			baseUrl="${contextPath}/shipmentFormRefDataController.html"
			parameters="supplierId={supplierId},rType=2" 
			postFunction="validateResponse"
			parser="new CustomResponseXmlParser()"
			errorFunction="error" />


		<ajax:updateField 
			source="productId" 
			action="productId" 
			eventType="change"
			target="isVariableProduct" 
			baseUrl="${contextPath}/shipmentFormRefDataController.html" 
			parameters="productId={productId},supplierId={supplierId},rType=3"
			errorFunction="error"
			postFunction="setReadonly"
			parser="new ResponseXmlParser()"			
			/>
</form>
			
<script type="text/javascript">

function popupCallback(src,popupName,columnHashMap)
{
  if(src=="shipmentReferenceId")
  {
    document.forms.shipmentForm.shipmentReferenceId.value = columnHashMap.get('PK');    
  }
}

function isDateGreaterOrEqualForCard(from, to,expiray) {


	

	var fromDate;
	var toDate;
	var tillExpiray; 

	var result = false;

//  format dd/mm/yyyy

if (to!='')
{
	fromDate = from.substring(6,10) + from.substring(3,5) + from.substring(0,2);
	toDate = to.substring(6,10)   + to.substring(3,5) + to.substring(0,2);
	tillExpiray= expiray.substring(6,10)   + expiray.substring(3,5) + expiray.substring(0,2);
	if( fromDate != tillExpiray && toDate != tillExpiray && fromDate > tillExpiray && toDate > tillExpiray) {
		result = true;
	}
}
else
{

	fromDate = from.substring(6,10) + from.substring(3,5) + from.substring(0,2);
	
	tillExpiray= expiray.substring(6,10)   + expiray.substring(3,5) + expiray.substring(0,2);
	if( fromDate != tillExpiray  && fromDate > tillExpiray) {
		result = true;
	}
}
	
	return result;
}

function dataTypecheck(theForm)
{
var errorMessage = '' ;

var creditAmount=theForm.creditAmount;
if(!isValidRate(creditAmount,14,2))
{
	errorMessage = "Credit Amount is incorrect" ;
}

var outStandingAmount=theForm.outstandingCredit;
if(!isValidRate(outStandingAmount,14,2))
{
	errorMessage = errorMessage + "\nOutstanding Amount is incorrect" ;
}


var price=theForm.price;
if(!isValidRate(price,14,2))
{
errorMessage = errorMessage + "\nPurchase Price is incorrect" ;
}

if( errorMessage != '' )
{
	alert( errorMessage ) ;
	return false ;
}

return true;
}

function revertDropdowns(theForm)
{
	
	theForm.reset();
	var obj = new AjaxJspTag.Select(
	"/i8Microbank/shipmentFormRefDataController.html", {
	parameters: "supplierId={supplierId},rType=2",
	target: "productId",
	source: "supplierId",
	errorFunction: error
	});
	obj.execute();
	
	

}

Calendar.setup(
      {
        inputField  : "purchaseDate", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "pDate"    // id of the button
      }
      );
Calendar.setup(      
      {  
        inputField  : "expiryDate", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "eDate"    // id of the button
      }
      );    
Calendar.setup(      
      {  
        inputField  : "shipmentDate", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "sDate"    // id of the button
      }

      );
      
function onFormSubmit(theForm) {
    /*if(!validateFormChar(theForm)){
      	return false;
    }*/
    
    

	var shipmentDate = theForm.shipmentDate.value.substring(6,10) + theForm.shipmentDate.value.substring(3,5) + theForm.shipmentDate.value.substring(0,2);
	var purchaseDate = theForm.purchaseDate.value.substring(6,10)   + theForm.purchaseDate.value.substring(3,5) + theForm.purchaseDate.value.substring(0,2);
	var expiryDate=theForm.expiryDate.value.substring(6,10)   + theForm.expiryDate.value.substring(3,5) + theForm.expiryDate.value.substring(0,2);
   
   if(   trim(theForm.shipmentDate.value) != '' && shipmentDate < purchaseDate )
	{
			alert('Shipment Date can never be lesser than Date of Purchase');
			document.getElementById('sDate').focus();
			return false;
	}		


   if(   trim(theForm.expiryDate.value) != '' && isDateGreaterOrEqualForCard(theForm.purchaseDate.value,theForm.shipmentDate.value,theForm.expiryDate.value)){
			alert('Expiry Date should be after the Date of Purchase ');
			document.getElementById('sDate').focus();
			return false;
		}
		
	if(   trim(theForm.shipmentDate.value) != '' && trim(theForm.expiryDate.value) != ''&&expiryDate < shipmentDate )
	{
			alert('Shipment Date should be Before the Date of Expiry');
			document.getElementById('sDate').focus();
			return false;
	}		
   var _creditAmount = trim(theForm.creditAmount.value);
   var _price = trim(theForm.price.value);
   
   if(  _creditAmount!='' && _price!='' &&  parseFloat(_creditAmount) > parseFloat(_price) ){
       alert('Credit Amount should be less than the Purchase Price');
       theForm.creditAmount.focus();
       return false;
       
   }
		if (validateShipmentModel(theForm))
		{
		 if(!dataTypecheck(theForm))
			 {
   
				 document.getElementById('price').focus();
				 return false;
 			}
		 return true;

		}
		
		return false;
		}

</script>
	

<v:javascript formName="shipmentModel" staticJavascript="false"/>
<script type="text/javascript" src="<c:url
value="/scripts/validator.jsp"/>"></script>

</body>
</html>

