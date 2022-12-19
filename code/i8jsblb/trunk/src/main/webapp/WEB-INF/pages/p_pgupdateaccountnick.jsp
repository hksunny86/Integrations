<!--Title: i8Microbank-->
<%@include file="/common/taglibs.jsp"%>

<%@ page import='com.inov8.microbank.common.util.*'%>

		<c:choose>
			<c:when test="${!empty status}">
				<c:set var="_status" value="${status}"/>
			</c:when>
			<c:otherwise>
				<c:set var="_status" value="${param.status}"/>
			</c:otherwise>
		</c:choose>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="decorator" content="decorator">

		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="cache-control" content="no-store" />
		<meta http-equiv="cache-control" content="private" />
		<meta http-equiv="cache-control" content="max-age=0, must-revalidate" />
		<meta http-equiv="expires" content="now-1" />
		<meta http-equiv="pragma" content="no-cache" />
	
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
		<meta name="title" content="Edit Account Nick"/>
		
	<script type="text/javascript">

		
		function setFocus()
			{
				try
				{
					if($('newAccountNick')!=null)
					{
						$('newAccountNick').focus();
					}
				}catch(e){}
			}
			function validate()
			{
				if (document.getElementById("newAccountNick").value=="")
				    {
						alert ("Please enter New Account Nick.");
						document.getElementById("newAccountNick").focus();
						return false;
					}
			}			
		</script>
		<style>
			.header {
				background-color: #0292e0;/*308dbb original*/
				color: white;
				font-family:arial, helvetica, sans-serif;/*verdana, arial, helvetica, sans-serif*/
				font-size: 14px;
				text-align: center;
				padding: 10px 3px;
				margin: 0px;
				border-right-style: solid;
				border-right-width: 1px;
				border-color: white;
			}		
		</style>
	</head>
  <body bgcolor="#ffffff" onload="javascript:setFocus();">

      <spring:bind path="changeAccountNickListViewModel.*">
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
	            <c:out value="${msg}" escapeXml="false"/><br/>
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>

						   
			<div class="eXtremeTable">
		   		<table class="tableRegion" width="100%">
					<tr class="titleRow">
						<td>	
							<html:form  name="changeAccountNickListViewModelForm" id="changeAccountNickListViewModelForm" commandName="changeAccountNickListViewModel" action="p_pgupdateaccountnick.html" method="post" onsubmit="return validate()">
						       <table width="100%" border="0" cellpadding="0" cellspacing="1">
						       		<tr>
						       			<td>
											<c:set var="changeAccountNickSmartMoneyAccountId">
												<security:encrypt strToEncrypt="${changeAccountNickListViewModel.smartMoneyAccountId}"/>
											</c:set>
											<c:set var="changeAccountNickAppUserId">
												<security:encrypt strToEncrypt="${changeAccountNickListViewModel.appUserId}"/>
											</c:set>

									        <input type="hidden" name="smartMoneyAccountId" value="${changeAccountNickSmartMoneyAccountId}" />
											
											<input type="hidden" name="isUpdate" id="isUpdate" value="true"/>									        
									        <html:hidden path="paymentModeId" />
									         <html:hidden path="customerId" />
									         
									         <input type="hidden" value="${changeAccountNickAppUserId}" name="appUserId" />
									         
									         <html:hidden path="bankId" />
									         
									        <input type="hidden" name = "<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>"/>
						       			</td>
						       		</tr>
							        <tr bgcolor="FBFBFB">
							             <td colspan="2" align="center">&nbsp;</td>
						            </tr>
						           <tr>
						             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Account Nick:&nbsp;</td>
						             <td width="51%" align="left" bgcolor="FBFBFB">
						               <html:input path="accountNick" id="accountNick" cssClass="textBox"  readonly="true" tabindex="-1" />
										
						             </td>
						           </tr>
						           <tr>
						             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Payment Mode:&nbsp;</td>
						             <td width="51%" align="left" bgcolor="FBFBFB">
						               <html:input path="paymentModeName" id="paymentModeName" cssClass="textBox" tabindex="-1"  readonly="true" />										
						             </td>
						           </tr>
						           <tr>
						             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">New Account Nick:&nbsp;</td>
						             <td width="51%" align="left" bgcolor="FBFBFB">
							               <input type="text" name="newAccountNick" id="newAccountNick" tabindex="1" onkeypress="return maskCommon(this,event)" maxlength="50" class="textBox">
						             </td>
						           </tr>
						           <tr bgcolor="FBFBFB">
						             <td colspan="2" align="center">&nbsp;</td>
						           </tr>
						           <tr>
							           <td align="center" width="49%">
							           </td>
							           <td width="51%" align="left">
								           <authz:authorize ifAnyGranted="<%=PortalConstants.MNG_ACC_NICK_UPDATE %>">
									           <input name="_update" type="submit" class="button" tabindex="2"  value=" Update "/>
									           <input name="cancel" type="button" class="button" tabindex="3" value=" Cancel " onclick="javascript:window.location='p_pgaccountnickmanagement.html?appUserId=${changeAccountNickAppUserId}'" />								           
								           </authz:authorize>
							           </td>
						           </tr>
						        </table>
							</html:form>
						</td>
					</tr>
				  </table>
			    </div>
			    
<script>
		$('newAccountNick').focus();
</script>			    
			    		
</body>
</html>




