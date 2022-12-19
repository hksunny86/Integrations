<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<!--Title: i8Microbank-->
<!--Author: Naseer Ullah-->
<%@include file="/common/taglibs.jsp"%>

<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="cache-control" content="no-store" />
		<meta http-equiv="cache-control" content="private" />
		<meta http-equiv="cache-control" content="max-age=0, must-revalidate" />
		<meta http-equiv="expires" content="now-1" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta name="title" content="Reverse Transaction"/>

		<link rel="stylesheet" href="${contextPath}/styles/style.css" type="text/css"/>

		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>

		<script type="text/javascript">
			$(document).ready(
				function()
				{
					setFocus();
					backButtonOverrideBody();
				}
			);

			function backButtonOverrideBody()
			{
				if (navigator.appName != "Microsoft Internet Explorer")
				{
					window.forward();
				}
				else
				{
					history.forward();
				} 
			}

			function setFocus()
			{
				try
				{
					if($('#comments')!=null)
					{
						$('#comments').focus();
					}
				}catch(e){}
			}

			function validate()
			{
				try
				{
					if( $('#comments').val() == "" )
					{
						alert('Comments is a required field');
						$('#comments').focus();
						return false;
					}
					else
					{
						if( $('#comments').val().length>250 )
						{
							alert('Maximum characters should not be more than 250');
							$('#comments').focus();
							return false;
						}
						else
						{
							//if there is illegal special character
					      	if(!validateFormChar( document.forms[0] ) )
      							return false;

							document.getElementById("_save").disabled = true;
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
	</head>
    <body bgcolor="#ffffff">
    	<h3 class="header" id="logHeader">Reverse Transaction</h3>
    	<c:choose>
    		<c:when test="${empty requestScope.isReversed}">
				<div class="eXtremeTable">
		   			<table class="tableRegion" width="100%">
						<tr class="titleRow">
							<td>
								<html:form name="reverseTransactionForm" id="reverseTransactionForm" commandName="transactionReversalVo" action="p_transactionreversalform.html" method="post" onsubmit="return validate()">
						       		<table width="100%" border="0" cellpadding="0" cellspacing="1">
						       			<tr>
						       				<td>
										      	<html:hidden path="transactionId"/>
										      	<html:hidden path="transactionCodeId"/>
										      	<html:hidden path="btnName"/>
										        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
							       			</td>
							       		</tr>
								        <tr bgcolor="FBFBFB">
								             <td colspan="2" align="center">&nbsp;</td>
							            </tr>
							           <tr>
								           <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Transaction ID:&nbsp;&nbsp;</td>
								           <td width="51%" align="left" bgcolor="FBFBFB">
			                               		<html:input path="transactionCode" cssClass="textBox" readonly="true" size="15"></html:input>
								           </td>
							           </tr>
							           <tr>
								           <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Comments:&nbsp;&nbsp; </td>
								           <td align="left" bgcolor="FBFBFB">
										   		<html:textarea path="comments" onkeypress="return maskCommon(this,event)" cssClass="textBox" cols="25" rows="6" cssStyle="overflow: auto; width: 163px; height: 102px;" />
								           </td>
							           </tr>
							           <tr bgcolor="FBFBFB">
							           		<td colspan="2" align="center">&nbsp;</td>
							           </tr>
							           <tr>
								           <td align="center" colspan="2">
									           <input name="_save" id="_save" type="submit" class="button"  value=" Submit "/> &nbsp;
									           <input name="cancel" type="button" class="button" value=" Cancel " onclick="javascript:window.close();"/>
								           </td>
							           </tr>
							        </table>
								</html:form>
							</td>
						</tr>
				  </table>
			    </div>
    		</c:when>
    		<c:otherwise>
    			<div class="eXtremeTable">
			   		<table class="tableRegion">
						<tr class="odd">
							<td class="formText">
								<c:choose>
									<c:when test="${requestScope.isReversed}">
										<script type="text/javascript">
											var btn = window.opener.document.getElementById( ${transactionReversalVo.btnName} );
										    if( btn!=null )
										        btn.disabled = "disabled";
										</script>
										<p>The transaction has been reversed.</p>
									</c:when>
									<c:otherwise>
										<p>A problem occurred while reversing the transaction. Please try later.</p>
									</c:otherwise>
								</c:choose>
								<p>Click the button below to close the window.</p>
							</td>
						</tr>
					</table>
				</div>
				<div class="eXtremeTable" style="text-align: center; padding-top: 10px;">
			   		<input type ="button" class="button" value = "Close Window" onclick = "window.close();"/>
			   	</div>
    		</c:otherwise>
    	</c:choose>
	</body>
</html>