<%@ include file="/common/taglibs.jsp" %>
<%@ page import="com.inov8.microbank.common.util.*, java.util.*, com.inov8.microbank.common.model.CommissionShSharesRuleModel " %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta name="decorator" content="decorator2">
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
        <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
        <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
        <%@include file="/common/ajax.jsp"%>
        <meta name="title" content="Manage FED Rules"/>

        <%
        String createPermission = PortalConstants.ADMIN_GP_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.MNG_FED_RULE_C;
		
		String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;	
		updatePermission +=	"," + PortalConstants.MNG_FED_RULE_U;
        
        String readPermission = PortalConstants.ADMIN_GP_READ;
        readPermission += "," + PortalConstants.MNG_FED_RULE_R;
        String deletePermission = PortalConstants.ADMIN_GP_DELETE;

        boolean updatePermissionBoolean = ! (AuthenticationUtil.checkRightsIfAny(updatePermission));
            String updateAllowed="";
            if(updatePermissionBoolean){
                updateAllowed="TRUE";
            }else{
                updateAllowed="FALSE";
            }

            boolean isPermission = (AuthenticationUtil.checkRightsIfAny(updatePermission) || AuthenticationUtil.checkRightsIfAny(createPermission));
        %>
        <script type="text/javascript">
            var jq = $.noConflict();
            jq(document).ready(
                    function($)
                    {
                    	$('#fedRulesTable tbody tr:visible').each( function(innerIndex) {
                                        var innerSelectArray = $(this).find('select:enabled');
										 var params = {
								            serviceId : innerSelectArray[0].value,
								           
								            currentTime : jq.now() //To avoid caching by browser particularly IE
								            
								        };
										
									/* jq.getJSON( 'p_serviceproductajax.html', params , function( key, value ) {
						            alert(key + "" + value);
                                   }); */
                                   
                                    $.ajax({
									  url: 'p_serviceproductajax.html', 
									  data: params,   // name of file with our data - link has been renamed
									  dataType: 'xml',    // type of file we will be reading
									  success: function(xml){parse(xml,innerIndex)},     // name of function to call when done reading file
									  error: error     // name of function to call when failed to read
									 });
							        
							    });
							    
							 function parse(xml, innerIndex){
								 var mySel = $('#product'+innerIndex); //my first drop-down
								 var selectedVal = mySel.val();
								 var selectedItem = mySel.val();
								 mySel.html('');
								 $(xml).find('item').each(function(){
								 	var name = $(this).find('name').text();
								 	var value = $(this).find('value').text();
								 /* 	if(value != "" && value == selectedItem){
								 			mySel.append("<option value=" +value + " selected=\"selected\">" + name + "</option>");
								 		}else{
								 			mySel.append("<option value=" +value + ">" + name + "</option>");
								 			mySel.val('');
								 		} */
								 	mySel.append("<option value=" +value + ">" + name + "</option>");
								 	mySel.val(selectedVal);	
								 });
								 
							}
                      
                    	
                        jq(document).on('click', '#fedRulesTable .addRow', function () {

                        	if(jq(this).closest('tr').find('td:eq(0)').find("select").val()=="" || 
                        			jq(this).closest('tr').next('tr').find('td:eq(0)').find("select").val() == ""){
                        		alert("Please Select \'Service' before adding another row\n")
                        		return false;
                        	}
                        	
                        	//jq(this).(".addRow").hide();
                        	//alert(jq(this).closest('tr').find('td:eq(0)').find("input").style());
                        	
                            var row = jq(this).closest('tr');
                            var clone = row.clone();
                            var tr = clone.closest('tr');
                            tr.find('select').val('');
                            var count=0;
                            tr.find('select').each(function() {
                                this.style.borderColor="#50B340";
                                count++;
                                if(count ==2 ){
                                    jq(this).find("option:gt(0)").remove();
                                }
                            });
                            row.find("input:button:first").attr('disabled','disabled');
                            tr.find('td:eq(0)').find("input:hidden").val('');
                            jq(this).closest('tr').after(clone);
                            
                            resetIdAndNameAttributes();
                            
                            var idOfServiceSelect=jq(this).closest('tr').next('tr').find('td:eq(0)').find("select").attr('id');
                            var idOfProductSelect=jq(this).closest('tr').next('tr').find('td:eq(1)').find("select").attr('id');
                            
                            var number = idOfServiceSelect.substring(7, idOfServiceSelect.length);
                            number++;
                            var newServiceSelectId="service"+number;
                            var newProductSelectId="product"+number;
                            
                            jq(this).closest('tr').next('tr').find('td:eq(0)').find("select").attr('id',newServiceSelectId);
                            jq(this).closest('tr').next('tr').find('td:eq(1)').find("select").attr('id',newProductSelectId);
                            var scriptPos = document.getElementById(newServiceSelectId);
                            var scriptNEW = document.createElement('script');
                            scriptNEW.type = 'text/javascript';
                            scriptNEW.text = "new AjaxJspTag.Select(\"\/i8Microbank\/p_serviceproductajax.html\",{source: \"service"+number+"\",target: \"product"+number+"\",parameters: \"serviceId={service"+number+"}\",errorFunction:error});"
                            scriptPos.parentNode.appendChild(scriptNEW);
                            
                            // var newTaxRegimeId = "taxRegimeId"+number;
                            /*jq(this).closest('tr').next('tr').find('td:eq(3)').find("select").attr('id',newTaxRegimeId);
                            var scriptPos2 = document.getElementById(newTaxRegimeId);
                            var scriptNEW2 = document.createElement('script');
                            scriptNEW2.type = 'text/javascript';
                            scriptNEW2.text = "new AjaxJspTag.Select(\"\/i8Microbank\/p_loadfed.html\",{source: \"taxRegimeId"+number+"\",target: \"rate"+number+"\",parameters: \"taxRegimeId={taxRegimeId"+number+"}\",errorFunction:error});"
                            scriptPos2.parentNode.appendChild(scriptNEW2);*/
                            
                        });

                        jq(document).on('click', '#fedRulesTable .removeRow', function () {
                            if(jq(this).closest('tr').next().html() == null){ //remove this check; if any error occurd
                            	jq(this).closest('tr').prev().find("input:button:first").prop('disabled', false);
                            }
                            jq(this).closest('tr').remove();
                            resetIdAndNameAttributes();
                        });

                        jq(document).on('hover', '#fedRulesTable .removeRow', function () {
                            jq(this).closest('tr').css( "background", "#C3C5C5" );
                        });

                        jq(document).on('mouseout', '#fedRulesTable .removeRow', function () {
                            jq(this).closest('tr').css( "background", "none" );
                        });

                        function resetIdAndNameAttributes()
                        {
                            jq('#fedRulesTable tbody tr').each(
                                    function(index)
                                    {
                                        var count=0;
                                        jq(this).find(':input:not(:button)').each( function() {
                                                    var name = $(this).attr('name');
                                                    if(count != 5 && count!=6 && count!=7){
                                                        var listName = name.substr(0,name.indexOf("["));
                                                        var propertyName = name.substr(name.indexOf("]")+2);
                                                        name = listName + "[" + index + "]." + propertyName;
                                                        var id = listName + index + "." + propertyName
                                                        jq(this).attr('id', id);
                                                        jq(this).attr('name', name);
                                                     }else{
                                                     	//fedRuleModelList[0].taxRegimeId
                                                        var xnumber=name.substring(16, (name.split(".")[0]).length);
                                                        var number = xnumber.substring(1,xnumber.length-1);
                                                        //number++;
                                                        var newServiceSelectId="service"+number;
                                                        var newProductSelectId="product"+number;
                                                        var newTaxRegimeId = "taxRegimeId"+number;
                                                        if(count==5){
                                                            jq(this).attr('id', newServiceSelectId);
                                                            name='fedRuleModelList['+index+'].serviceId';
                                                            jq(this).attr('name', name);
                                                        }else if(count==6){
                                                        	name='fedRuleModelList['+index+'].productId';
                                                            jq(this).attr('id', newProductSelectId);
                                                            jq(this).attr('name', name);
                                                        }else{
                                                        	name='fedRuleModelList['+index+'].taxRegimeId';
                                                        	jq(this).attr('id', newTaxRegimeId);
                                                        	jq(this).attr('name', name);
                                                        }
                                                    } 
                                                    count++;
                                                }
                                        );
                                    }
                            );
                        }

                        $( "#btnSave" ).click(
                                function()
                                {
                                	var rowCount = $('#fedRulesTable tbody tr').length;
                                	var originalCount = document.getElementById('originalCount').value;
                                	if(rowCount == originalCount && !isFormDataChanged())
                                  	{
                                  		return;
                                  	}
                                    var isValid = true;
                                    resetForm();
                                    $('#fedRulesTable tbody tr').each( function(outerIndex) {
                                        var inputElementArray = $(this).find(':input:not(:button):not( :hidden)');
                                        if(inputElementArray[0].value.length == 0)
                                        {
                                            isValid = false;
                                            inputElementArray[0].style.borderColor="red";
                                            alert('Service is required.');
                                        }
                                        /*else if(inputElementArray[1].value.length == 0)
                                        {
                                            isValid = false;
                                            inputElementArray[1].style.borderColor="red";
                                            alert('Product is required.');
                                        }*/
                                        /*else if(inputElementArray[2].value.length == 0)
                                            {
                                                isValid = false;
                                                inputElementArray[2].style.borderColor="red";
                                                alert('Tax Regime is required.');
                                            }*/
                         /*                else if(inputElementArray[3].value.length == 0)
                                        {
                                            isValid = false;
                                            inputElementArray[3].style.borderColor="red";
                                            alert('Type is required.');
                                        } */
                                        
                                        /*if(isValid){
    										jq('#fedRulesTable tbody tr').each( function() {
    					    				var selectArray = jq(this).find(':input');
    					    				if(selectArray[7].value > 0){
    					    					if( selectArray[9].value == null || selectArray[9].value == undefined || selectArray[9].value == ""){ //fed value/rate
    					    						selectArray[9].style.borderColor="red";
    					    						alert('FED rate is mandatory if you have selected Tax Regime!');
    					    						isValid = false;
    					    						return;
    					    						}
    					    					}
    					    				if(!isValid){
    					    					return isValid;
    					    				}
    					    				});
    									}*/
                                        
                                        if(!isValid){
                                        	return isValid;
                                        }
                                        
                                    });

                                    $('#fedRulesTable tbody tr:visible').each( function(outerIndex) {
                                        var outerSelectArray = $(this).find('select:enabled');
                                        if(!isValid){
                                            return;
                                        }

                                        $('#fedRulesTable tbody tr:visible').each( function(innerIndex) {
                                            if (outerIndex != innerIndex && innerIndex != null) {
                                                var innerSelectArray = $(this).find('select:enabled');
                                                if(outerSelectArray[0].value == innerSelectArray[0].value
                                                         && (innerSelectArray[1].selectedIndex == outerSelectArray[1].selectedIndex) )
                                                {
                                                    alert("Same Service can be used only for unique products.");
                                                    isValid=false;
                                                    return;
                                                }
                                            }
                                        });
                                        
                                        if(!isValid){
                                            return;
                                        }
                                    });

									/* if(isValid){
										jq('#fedRulesTable tbody tr').each( function() {
					    				var selectArray = jq(this).find(':input');
					    				//alert(selectArray[9].value);
					    				//alert(selectArray[7].value); //tax regime drop down
					    				if(selectArray[7].value > 0){
					    					if( selectArray[9].value == null || selectArray[9].value == undefined || selectArray[9].value == ""){ //fed value/rate
					    						selectArray[9].style.borderColor="red";
					    						alert('FED rate is mandatory if you have selected Tax Regime!');
					    						isValid = false;
					    						return;
					    						}
					    					}
					    				});
									} */
									
									if(isValid){
										jq('#fedRulesTable tbody tr').each( function() {
					    				var selectArray = jq(this).find(':input');
					    				//alert(selectArray[9].value);
					    				//alert(selectArray[7].value); //tax regime drop down
					    				
					    					if( selectArray[9].value < 0 || selectArray[9].value > 99){ //fed value/rate
					    						alert('FED rate should be between 0 to 99!');
					    						isValid = false;
					    						return;
					    						}
					    					
					    				});
									}
							
                                    if(isValid)
                                    {
                                    	if(confirm('Do you want to proceed'))
                                        	$('#fedRuleManagementForm').submit();
                                        //alert("Form Submitted.");
                                    }
                                }
                        );//end of btnSave click event
                    }

            );
            function resetForm() {
                jq('#fedRuleManagementForm').find(':input:not(:button)').each(function () {
                    this.style.borderColor = "#e6e6e6";
                });
            }
        </script>
        <script type="text/javascript">
            function markRowDeleted(index){
                var s = index;
                //alert(document.getElementById("fedRuleModelList"+index+".deleted").value);
                //document.getElementById("fedRuleModelList"+index+".deleted").value=1;
            }
            function error(request)
            {
                alert("An unknown error has occured. Please contact with the administrator for more details");
            }
        </script>
    </head>
    <body>
        <spring:bind path="fedRuleManagementVO.*">
            <c:if test="${not empty status.errorMessages}">
                <div class="errorMsg">
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
                    <c:out value="${msg}" escapeXml="false"/>
                    <br/>
                </c:forEach>
            </div>
            <c:remove var="messages" scope="session"/>
        </c:if>
        <html:form commandName="fedRuleManagementVO"
                   id="fedRuleManagementForm" method="post" action="p_fedrulemanagementform.html">
            <table border="0" width="101%">
                <tr>
                    <td colspan="2">
                        <div class="eXtremeTable">
                            <table id="fedRulesTable" class="tableRegion" width="100%" cellspacing="0" cellpadding="0" border="0">
                                <thead>
                                    <tr>
                                        <td class="tableHeader">
                                            Service
                                        </td>
                                        <td class="tableHeader">
                                            Product
                                        </td>
                                        <td class="tableHeader">
                                            Tax Regime
                                        </td>
<!--                                         <td class="tableHeader">
                                            Type
                                        </td> -->
                                        <td class="tableHeader">
                                            FED Rate (%)
                                        </td>
                                        <td class="tableHeader">
                                            Apply
                                        </td>
                                        <td class="tableHeader">
                                            Action
                                        </td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${fedRuleManagementVO.fedRuleModelList}" var="authenticationMethodRuleModel" varStatus="iterationStatus">
                                        <tr>
                                            <td align="center">
                                                <%-- <html:hidden path="fedRuleModelList[${iterationStatus.index}].fedRuleId"/> --%>
                                                <html:hidden path="fedRuleModelList[${iterationStatus.index}].versionNo"/>
                                                <html:hidden path="fedRuleModelList[${iterationStatus.index}].createdOn"/>
                                                <html:hidden path="fedRuleModelList[${iterationStatus.index}].updatedOn"/>
                                                <html:hidden path="fedRuleModelList[${iterationStatus.index}].createdBy"/>
                                                <html:hidden path="fedRuleModelList[${iterationStatus.index}].updatedBy"/>

                                                <html:select id="service${iterationStatus.index}" path="fedRuleModelList[${iterationStatus.index}].serviceId" cssClass="textBox" disabled="<%=updateAllowed%>">
                                                    <html:option value="">---Select---</html:option>
                                                    <html:options items="${serviceModelList}" itemLabel="name" itemValue="serviceId"/>
                                                </html:select>
                                                <ajax:select
                                                        source="service${iterationStatus.index}"
                                                        target="product${iterationStatus.index}"
                                                        baseUrl="${contextPath}/p_serviceproductajax.html"
                                                        parameters="serviceId={service${iterationStatus.index}}" errorFunction="error"/>
                                            </td>
                                            <td align="center">
                                                <html:select id="product${iterationStatus.index}" path="fedRuleModelList[${iterationStatus.index}].productId" cssClass="textBox" disabled="<%=updateAllowed%>">
                                                    <html:option value="">---Select---</html:option>
                                                    <%-- <html:options items="${fedRuleManagementVO.productModelList}" itemLabel="name" itemValue="productId"/> --%>
                                                    <html:options items="${productModelList}" itemLabel="name" itemValue="productId"/>
                                                </html:select></td>
                                            <td align="center">
                                                <html:select path="fedRuleModelList[${iterationStatus.index}].taxRegimeId" cssClass="textBox" disabled="<%=updateAllowed%>" id="taxRegimeId${iterationStatus.index}">
                                                    <html:option value="">---Select---</html:option>
                                                    <html:options items="${taxRegimeModelList}" itemLabel="name" itemValue="taxRegimeId"/>
                                                </html:select>
                                                <%-- <ajax:updateField parser="new ResponseXmlParser()"
                                                		eventType="change"
                                                		action="taxRegimeId${iterationStatus.index}"
                                                        source="taxRegimeId${iterationStatus.index}"
                                                        target="rate${iterationStatus.index}"
                                                        baseUrl="${contextPath}/p_loadfed.html"
                                                        parameters="taxRegimeId={taxRegimeId${iterationStatus.index}}" errorFunction="error"/> --%>
                                            </td>
<%--                                             <td align="center">
                                                <html:select path="fedRuleModelList[${iterationStatus.index}].inclusive" cssClass="textBox" disabled="<%=updateAllowed%>">
                                                    <html:options items="${typeList}" itemLabel="label" itemValue="value"/>
                                                </html:select>
                                            </td> --%>
                                            <td align="center">
                                                <html:input path="fedRuleModelList[${iterationStatus.index}].rate" cssClass="textBox" disabled="<%=updateAllowed%>" maxlength="5" id="rate${iterationStatus.index}" onkeypress="return checkNumeric(this, event)"/>
                                            </td>
                                            <td align="center">
                                                <html:checkbox path="fedRuleModelList[${iterationStatus.index}].active" disabled="<%=updateAllowed%>"/>
                                            </td>
                                            <td>
                                                <%-- <authz:authorize ifAnyGranted="<%=PortalConstants.AUTH_MTHD_RULE_CREATE%>"> --%>
                                                <c:if test="${iterationStatus.index+1 < fedRuleManagementVO.fedRuleModelList.size()}">
                                                    <input type="button" id="fedRuleModelList${iterationStatus.index}.add" class="addRow" value="+" style="font-weight:bold;padding: 0px 10px;height: 29px;font-size:20px;" title="Add row" disabled="disabled"/>
                                                </c:if>
                                                <c:if test="${iterationStatus.index+1 >= fedRuleManagementVO.fedRuleModelList.size()}">
                                                    <input type="button" id="fedRuleModelList${iterationStatus.index}.add" class="addRow" value="+" style="font-weight:bold;padding: 0px 10px;height: 29px;font-size:20px;" title="Add row" />
                                                </c:if>
                                                <%-- </authz:authorize> --%>
                                                <%-- <authz:authorize ifAnyGranted="<%=PortalConstants.AUTH_MTHD_RULE_DELETE%>"> --%>
                                                    <input type="button" id="fedRuleModelList${iterationStatus.index}.remove" class="removeRow" value="-" title="Remove row" style="background-color:#E40606 !important;color:#FFFCFC !important;font-weight:bold;padding: 0px 10px;height: 29px;font-size:20px;" onClick="markRowDeleted(${iterationStatus.index});"/>
                                                <%-- </authz:authorize> --%>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>">
                        <input type="hidden" name="originalCount" id="originalCount" value="${fedRuleManagementVO.fedRuleModelList.size()}"/>
                       <authz:authorize ifAnyGranted="<%=updatePermission%>" >
                         <input type="button" value="Save" class="button" id="btnSave" />
                        </authz:authorize>
                       <authz:authorize ifNotGranted="<%=updatePermission%>">
                         <input type="button" value="Save" class="button" id="btnSave" disabled="disabled"/>
                        </authz:authorize>
                        <input name="reset" type="reset" onclick="javascript: window.location='p_fedrulemanagementform.html'" class="button" value="Cancel"/>
                    </td>
                </tr>
            </table>
            
            <input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
   			<input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />
   			
        </html:form>
        <script type="text/javascript">

            function popupwindow(url, title, w, h) {
                var left = (screen.width/2)-(w/2);
                var top = (screen.height/2)-(h/2);
                return window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
            }

        </script>
    </body>

</html>