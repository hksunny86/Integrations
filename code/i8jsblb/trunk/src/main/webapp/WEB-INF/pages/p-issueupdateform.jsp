<!--Title: i8Microbank-->
<!--Author: Asad Hayat-->
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

		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="cache-control" content="no-store" />
		<meta http-equiv="cache-control" content="private" />
		<meta http-equiv="cache-control" content="max-age=0, must-revalidate" />
		<meta http-equiv="expires" content="now-1" />
		<meta http-equiv="pragma" content="no-cache" />
	
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
		<meta name="title" content="Log Issue..."/>
		
	<script type="text/javascript">

		function backButtonOverrideBody(e)
		{
			if (navigator.appName != "Microsoft Internet Explorer"){
				window.forward();
			}
			else{
				history.forward();
			} 

		}


		
			function setFocus()
			{
				try
				{
					if($('comments')!=null)
					{
						$('comments').focus();
					}
				}catch(e){}
			}
			function validate()
			{
				try
				{
					if($F('comments')=="")
					{
						alert('Comments is a required field');
						$('comments').focus();
						return false;
					}
					else
					{
						if($F('comments').length>250)
						{
							alert('Maximum characters should not be more than 250');
							$('comments').focus();
							return false;
						}
						else
						{
							//if there is illegal special character
					      	if(!validateFormChar(document.forms[0]))
      							return false;
      							
							$('_save').disabled = true;							
							return true;
						}
					}
				}catch(e){}
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
		
		<%
		String issueUpdatePermissions = PortalConstants.MNG_CHARGBACK_UPDATE;
		issueUpdatePermissions += "," + PortalConstants.MNO_GP_UPDATE;
		issueUpdatePermissions += "," + PortalConstants.CSR_GP_UPDATE;
		issueUpdatePermissions += "," + PortalConstants.PG_GP_UPDATE;
		issueUpdatePermissions += "," + PortalConstants.REQ_CHARGEBACK_UPDATE;
		issueUpdatePermissions += "," + PortalConstants.RET_GP_UPDATE;
		 %>
	</head>
  <body bgcolor="#ffffff" onload="javascript:setFocus();backButtonOverrideBody(event);">
			<c:set var="C_RIFC" scope="page">
				<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_RIFC.toString()%>"/>
			</c:set>
			<c:set var="C_INVALID" scope="page">
				<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_INVALID.toString()%>"/>
			</c:set>
			<c:set var="C_OPEN" scope="page">
				<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_OPEN.toString()%>"/>
			</c:set>
			<c:set var="C_NEW" scope="page">
				<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_NEW.toString()%>"/>
			</c:set>
			<c:set var="D_RIFC" scope="page">
				<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.DISPUTE_RIFC.toString()%>"/>
			</c:set>
			<c:set var="D_INVALID" scope="page">
				<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.DISPUTE_INVALID.toString()%>"/>
			</c:set>
			<c:set var="D_OPEN" scope="page">
				<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.DISPUTE_OPEN.toString()%>"/>
			</c:set>
			<c:set var="D_NEW" scope="page">
				<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.DISPUTE_NEW.toString()%>"/>
			</c:set>

     <h3 class="header" id="logHeader">
					<c:if test="${param.issueTypeStatusId eq C_INVALID}">
						Resolve Chargeback in Favor of Agent
					</c:if>
					<c:if test="${param.issueTypeStatusId eq C_RIFC}">
						Resolve Chargeback in Favor of Customer
					</c:if>
					<c:if test="${param.issueTypeStatusId eq C_OPEN}">
						Escalate Chargeback to Inov8
					</c:if>
					
					<c:if test="${param.issueTypeStatusId eq D_INVALID}">
						Mark Dispute as Invalid
					</c:if>
					<c:if test="${param.issueTypeStatusId eq D_RIFC}">
						Resolve Dispute in Favor of Customer
					</c:if>
					<c:if test="${param.issueTypeStatusId eq D_OPEN}">
						Escalate Dispute to Inov8
					</c:if>
					<c:if test="${param.issueTypeStatusId eq C_NEW}">
						Log Chargeback
					</c:if>
					<c:if test="${param.issueTypeStatusId eq D_NEW}">
						Log Dispute
					</c:if>
     </h3>

   <c:choose>
   	<c:when test="${not empty _status}">
   		<c:choose>
				<c:when test="${_status eq 'success'}">
					<script type="text/javascript">
						<c:choose>
							<c:when test="${param.issueTypeStatusId eq C_NEW || param.issueTypeStatusId eq D_NEW}">
								if(window.opener != null){
									var btn;
									if(eval("window.opener.document.forms[1]")){
										btn =  eval("window.opener.document.forms[1].<%=request.getParameter(IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME)%>");
								    }else{
								    	btn =  eval("window.opener.document.forms[0].<%=request.getParameter(IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME)%>");
								    }
								    if(btn!=null){
								        btn.disabled = "disabled";
								    }
							    }else{
							    }
							</c:when>
							<c:otherwise>
							        if(null!=window.opener.document.getElementById("DIV_INVALID_${param.issueId}"))
									    window.opener.document.getElementById("DIV_INVALID_${param.issueId}").innerHTML="";
									if(null!=window.opener.document.getElementById("DIV_RIFC_${param.issueId}"))
									    window.opener.document.getElementById("DIV_RIFC_${param.issueId}").innerHTML="";
									if(null!=window.opener.document.getElementById("DIV_ETOI8_${param.issueId}"))
									    window.opener.document.getElementById("DIV_ETOI8_${param.issueId}").innerHTML="";
				
									<c:if test="${param.issueTypeStatusId eq C_INVALID}">
									    if(null!=window.opener.document.getElementById("DIV_INVALID_${param.issueId}"))
										    window.opener.document.getElementById("DIV_INVALID_${param.issueId}").innerHTML="YES";
									</c:if>
									<c:if test="${param.issueTypeStatusId eq C_RIFC}">
									    if(null!=window.opener.document.getElementById("DIV_RIFC_${param.issueId}"))
										    window.opener.document.getElementById("DIV_RIFC_${param.issueId}").innerHTML="YES";
									</c:if>
									<c:if test="${param.issueTypeStatusId eq C_OPEN}">
									    if(null!=window.opener.document.getElementById("DIV_ETOI8_${param.issueId}"))
										    window.opener.document.getElementById("DIV_ETOI8_${param.issueId}").innerHTML="YES";
									</c:if>
									
									<c:if test="${param.issueTypeStatusId eq D_INVALID}">
									    if(null!=window.opener.document.getElementById("DIV_INVALID_${param.issueId}"))
										    window.opener.document.getElementById("DIV_INVALID_${param.issueId}").innerHTML="YES";
									</c:if>
									<c:if test="${param.issueTypeStatusId eq D_RIFC}">
									    if(null!=window.opener.document.getElementById("DIV_RIFC_${param.issueId}"))
										    window.opener.document.getElementById("DIV_RIFC_${param.issueId}").innerHTML="YES";
									</c:if>
									<c:if test="${param.issueTypeStatusId eq D_OPEN}">
									    if(null!=window.opener.document.getElementById("DIV_ETOI8_${param.issueId}"))
										    window.opener.document.getElementById("DIV_ETOI8_${param.issueId}").innerHTML="YES";
									</c:if>
							</c:otherwise>
						</c:choose>	
						   
					</script>
					<div class="eXtremeTable">
				   		<table class="tableRegion">
							<tr class="odd">
								<td class="formText">	
							        <c:if test="${param.issueTypeStatusId eq C_NEW}">
										<p>The issue has been logged.</p><p><strong>Issue Code is <c:out value="${param.issueCode}" escapeXml="false"/></strong> . Click the button below to close the window</p>
									</c:if>
									<c:if test="${param.issueTypeStatusId eq C_INVALID}">
										<p>The issue has been resolved in favor of Agent.</p><p><strong>Issue Code is <c:out value="${param.issueCode}" escapeXml="false"/></strong> . Click the button below to close the window</p>
									</c:if>
									<c:if test="${param.issueTypeStatusId eq C_RIFC}">
										<p>The issue has been resolved in favor of Customer.</p><p><strong>Issue Code is <c:out value="${param.issueCode}" escapeXml="false"/></strong> . Click the button below to close the window</p>
									</c:if>
									<c:if test="${param.issueTypeStatusId eq C_OPEN}">
										<p>The issue has been escalated to Inov8.</p><p><strong>Issue Code is <c:out value="${param.issueCode}" escapeXml="false"/></strong> . Click the button below to close the window</p>
									</c:if>
									
							        <c:if test="${param.issueTypeStatusId eq D_NEW}">
										<p>The issue has been logged successfully.</p><p><strong>Issue Code is <c:out value="${param.issueCode}" escapeXml="false"/></strong> . Click the button below to close the window</p>
									</c:if>
									<c:if test="${param.issueTypeStatusId eq D_INVALID}">
										<p>The issue has been resolved as invalid.</p><p><strong>Issue Code is <c:out value="${param.issueCode}" escapeXml="false"/></strong> . Click the button below to close the window</p>
									</c:if>
									<c:if test="${param.issueTypeStatusId eq D_RIFC}">
										<p>The issue has been resolved in favor of Customer.</p><p><strong>Issue Code is <c:out value="${param.issueCode}" escapeXml="false"/></strong> . Click the button below to close the window</p>
									</c:if>
									<c:if test="${param.issueTypeStatusId eq D_OPEN}">
										<p>The issue has been escalated to Inov8.</p><p><strong>Issue Code is <c:out value="${param.issueCode}" escapeXml="false"/></strong> . Click the button below to close the window</p>
									</c:if>
								</td>
							</tr>
						</table>
					</div>	
				</c:when>
				<c:otherwise>
					<script type="text/javascript">
						var btn =  eval("window.opener.document.forms[1].<%=request.getParameter(IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME)%>");
					    if( btn!=null )
					        btn.disabled = "disabled";
					</script>
					<div class="eXtremeTable">
				   		<table class="tableRegion">
							<tr class="odd">
								<td class="formText"><c:out value="${message}" /></td>
							</tr>
						</table>
					</div>					
				</c:otherwise>
			</c:choose>
		   	<div class="eXtremeTable" style="text-align: center; padding-top: 10px;">
		   		<input type ="button" class="button" value = "Close Window" onclick = "window.close();"/>
		   	</div>
   	</c:when>
   	<c:otherwise>
			<div class="eXtremeTable">
		   		<table class="tableRegion" width="100%">
					<tr class="titleRow">
						<td>	
							<html:form  name = "issueForm" id = "issueForm" commandName="issueModel" action="p-issueupdateform.html" method="post" onsubmit="return validate()">
						       <table width="100%" border="0" cellpadding="0" cellspacing="1">
						       		<tr>
						       			<td>
									        <html:hidden path="custTransCode"/>
									      	<html:hidden path="transactionId"/>
									       	<html:hidden path="issueId"/>
									        <html:hidden path="issueCode"/>
									        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
							
									        <input type = "hidden" name = "btnName" value="<%=request.getParameter(IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME)%>"/>
									        <input type = "hidden" name = "<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>" value="<%=request.getParameter(IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME)%>"/>

						       			</td>
						       		</tr>
							        <tr bgcolor="FBFBFB">
							             <td colspan="2" align="center">&nbsp;</td>
						            </tr>
						           <tr>
						             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Transaction Code:&nbsp;&nbsp;</td>
						             <td width="51%" align="left" bgcolor="FBFBFB">
						               <html:hidden path="transactionCodeId" />
										<input type="text" name="transactionCode" value="<c:out value='${transactionCode}'/>" class="textBox" readonly="readonly" size="15"/>
						             </td>
						           </tr>
						           <tr>
						             <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Comments:&nbsp;&nbsp; </td>
						             <td align="left" bgcolor="FBFBFB">
									  <html:textarea path="comments" onkeypress="return maskCommon(this,event)" cssClass="textBox" rows="6" cssStyle="overflow: auto; width: 163px; height: 102px;"></html:textarea>
									 	<%--
								        <spring:bind path="issueModel.comments">
								         	 <textarea name="${status.expression}" class = "textBox" cols="25" rows="6" style="overflow: auto" type="_moz">${status.value}</textarea>
								         </spring:bind>
								          	--%>
						             </td>
						           </tr>
						           <tr bgcolor="FBFBFB">
						             <td colspan="2" align="center">&nbsp;</td>
						           </tr>
						           <tr>
							           <td align="center" colspan="2">
							           <authz:authorize ifAnyGranted="<%=issueUpdatePermissions%>">
								           <input name="_save" type="submit" class="button"  value=" Submit "/> &nbsp;
							           </authz:authorize>
							           <authz:authorize ifNotGranted="<%=issueUpdatePermissions%>">
								           <input name="_save" type="submit" class="button"  value=" Submit " disabled="disabled"/> &nbsp;
							           </authz:authorize>
							           
								           <input name="cancel" type="button" class="button" value=" Cancel " onclick="javascript:window.close();"/>
								           
							           </td>
						           </tr>
						        </table>
							</html:form>
						</td>
					</tr>
				  </table>
			    </div>		
   		</c:otherwise>
 	</c:choose>
</body>
</html>


