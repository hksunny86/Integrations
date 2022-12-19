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
		<meta name="title" content="Account Closing"/>
		
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

			function refreshParent() {
				  window.opener.location.reload();
				  window.close();
				}			
		</script>
	<style>
			.header {
				background-color: #6699CC;/*308dbb original*/
				color: white;
				font-family:verdana, arial, helvetica, sans-serif;/*verdana, arial, helvetica, sans-serif*/
				font-size: 11px;
				font-weight: bold;
				text-align: center;
				padding-right: 3px;
				padding-left: 3px;
				padding-top: 4px;
				padding-bottom: 4px;
				margin: 0px;
				border-right-style: solid;
				border-right-width: 1px;
				border-color: white;
			}		
	</style>
	</head>	
	<body bgcolor="#ffffff" onload="javascript:setFocus();backButtonOverrideBody(event);">
	<h3 class="header" id="logHeader">
		Reset Portal Password
	</h3>
	
	<c:choose>
   	<c:when test="${not empty _status}">
   		<c:choose>
				<c:when test="${_status eq 'success'}">
				
					<div class="">
							   		<table class="tableRegion">
										<tr class="odd">
											<td class="formText">	
													${message }<p>Click the button below to close the window</p>
												
											</td>
										</tr>
						 			</table>					        		
					</div>	
				</c:when>
			<c:otherwise>
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
			<input type ="button" class="button" value = "Close Window" onclick="javascript:window.close();"/>
		</div>
	</c:when>	
	<c:otherwise>
	<div class="eXtremeTable">
		   		<table class="tableRegion" width="100%">
					<tr class="titleRow">
						<td>	
							<form id="resetTransactionCodeForm" method="post" action="p-resetportalpasswordform.html" onsubmit="return validateForm(this)"  >
								<table width="100%"  border="0" cellpadding="0" cellspacing="1">
																	
									<input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
									<input type="hidden" name="usecaseId" id="usecaseId" value="${param.usecaseId}"</>
		          					
									<tr bgcolor="FBFBFB">
							            <td colspan="2" align="center">&nbsp;</td>
							        </tr>
							        
							        <tr>
							        
							        	<td  width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span><c:if test="${param.isAgent }">Agent ID</c:if><c:if test="${param.isAgent eq 'false' }">User ID</c:if>:&nbsp;</td>
							        	<td align="left" bgcolor="FBFBFB">       
							                <c:if test="${param.isAgent }">
							                <input name="agentId" type="text" size="40" readonly="readonly" class="textBox" tabindex="0" maxlength="50"  value="${param.mfsId }"  />
							                </c:if>
							                <c:if test="${param.isAgent eq 'false' }">
							                <input name="agentId" type="text" size="40" readonly="readonly" class="textBox" tabindex="0" maxlength="50"  value="${param.username}"  />
							                </c:if>	
							             </td>   		                       							    							   							       
							        </tr>    
							        <tr>
							            <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
							            	
							            <td align="left" bgcolor="FBFBFB">
							            
							            <textarea name="comments" cols="50" rows="5" tabindex="1"  style="width:163px; height: 102px" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox" >${status.value}</textarea>
							           </td>
							         </tr>					
							        <tr bgcolor="FBFBFB">
								    	<td height="19"  align="right">
									    	<input name="_save" type="submit" class="button" value="Save" tabindex="2"/>
								    	</td>
								    	<td height="19" align="left" >
											<input name="cancel" type="button" class="button" value=" Cancel " tabindex="3" onclick="javascript:window.close();"/>		
								    	</td>
								    	
							    	</tr>
							         <input type="hidden" name="appUserId" value="${param.appUserId}">
							         <input type="hidden" name="mfsId" value="${param.mfsId}">	
							          <input type="hidden" name="username" value="${param.username}">						         
							         <input type="hidden" name="notifyBySMS" value="${param.notifyBySMS}">
							         
							 	</table>
						</form>
					</td>
				</tr>
			</table>
		</div>
		</c:otherwise>
	</c:choose>		
	</body>
</html>  	