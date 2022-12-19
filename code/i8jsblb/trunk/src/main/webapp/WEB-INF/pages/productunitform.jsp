<!--Author: Asad Hayat-->

<%@include file="/common/taglibs.jsp"%>

<spring:bind path="productUnitModel.*">
  


<html>
<head>
<meta name="decorator" content="decorator2">

<v:javascript formName="productUnitModel"
staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript"
src="<c:url value="/scripts/validator.jsp"/>"></script>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="title" content="Product Unit"/>
</head>
<body>
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
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
<table width="100%"  border="0" cellpadding="0" cellspacing="1">
<form name="ProductUnitForm" method="POST" action="productunitform.html" onsubmit="return validateProductUnitModel(this)" >
<c:if test="${not empty param.productUnitId}">
  <input type="hidden"  name="isUpdate" id="isUpdate" value="true"/>
</c:if>



<spring:bind path="productUnitModel.createdBy">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="productUnitModel.updatedBy">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="productUnitModel.createdOn">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="productUnitModel.updatedOn">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>
<spring:bind path="productUnitModel.productUnitId">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="productUnitModel.sold">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>



<spring:bind path="productUnitModel.shipmentId">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="productUnitModel.productId">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>




  <tr bgcolor="FBFBFB">
            <td colspan="2" align="center">&nbsp;</td>
    </tr>
          	  <tr bgcolor="FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>User Name:&nbsp;
             </td>
            <td width="50%" align="left">
		    <spring:bind path="productUnitModel.userName">
			        <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50">
		    </spring:bind>

            </td>
          </tr>
		  <tr bgcolor="FBFBFB" >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Password/PIN:&nbsp;             </td>
            <td width="50%" align="left">
		    <spring:bind path="productUnitModel.pin">
			    <input type="password" name="${status.expression}"  value="${status.value}" class="textBox" maxlength="50">
		    </spring:bind>

 </td>
          </tr>
		  <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText" ><span style="color:#FF0000">*</span>Serial:&nbsp;</td>
            <td align="left">

	    <spring:bind path="productUnitModel.serialNo">
			    <input type="text" name="${status.expression}" class="textBox" value="${status.value}" maxlength="50"/>

	    </spring:bind>


	    </td>
		  </tr>
		  <tr bgcolor="FBFBFB">
            <td align="right" bgcolor="F3F3F3" class="formText" >Additional Field1:&nbsp;</td>
            <td align="left">
		    <spring:bind path="productUnitModel.additionalField1">
			    <input type="text" name="${status.expression}" class="textBox" value="${status.value}" maxlength="250"/>

		    </spring:bind>
                </td>
    </tr>


		  <tr bgcolor="FBFBFB">
            <td align="right" bgcolor="F3F3F3" class="formText" >Additional Field2:&nbsp;</td>
            <td align="left">
		    <spring:bind path="productUnitModel.additionalField2">
			    <input type="text" name="${status.expression}" class="textBox" value="${status.value}" maxlength="250"/>

		    </spring:bind>
                </td>
    </tr>
    </tr>


		  <tr bgcolor="FBFBFB">
            <td align="right" bgcolor="F3F3F3" class="formText" >Active:&nbsp;</td>
            <td align="left">
                <spring:bind path="productUnitModel.active">
		            <input name="${status.expression}" type="checkbox" value="true" 	
	                    <c:if test="${status.value==true}">checked="checked"</c:if>
	                    <c:if test="${empty param.mnoId}">checked="checked"</c:if>
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

				 <input name="_save" type="submit" class="button" value=" Save ">&nbsp;
				 <input name="_saveanother" type="submit" class="button" value=" Save & Add Another " >&nbsp;
        			 <input name="cancel" type="reset" class="button" value=" Cancel " onclick="/*window.location.href='shipmentmanagement.html';*/">
			  </td>
		  </tr>

	  </tr>

</form>
</table>
<script type="text/javascript">
    highlightFormElements();
    document.forms[0].userName.focus();
</script>
</body>

</html>

