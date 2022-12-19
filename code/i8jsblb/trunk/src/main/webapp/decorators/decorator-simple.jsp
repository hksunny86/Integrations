<jsp:directive.page import="com.inov8.microbank.common.util.PortalDateUtils"/>
<jsp:directive.page import="com.inov8.microbank.common.model.AppUserModel"/>
<jsp:directive.page import="com.inov8.microbank.common.util.UserUtils"/>
<!DOCTYPE html>
<html>

<%@include file="/common/taglibs.jsp"%>
<head>
<title>i8Microbank ::: <decorator:title default="" /><decorator:getProperty
	property="meta.title" /></TITLE>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="content-type" content="text/html;charset=iso-8859-1">
<meta http-equiv="Page-Enter" content="blendTrans(Duration=0.2)">
<meta http-equiv="Page-Exit" content="blendTrans(Duration=0.2)">

<link rel='stylesheet' href="${contextPath}/styles/style.css" type='text/css'>
<link rel="stylesheet" type="text/css" media="all" href="${contextPath}/styles/decorator-style/theme.css" />
<link rel="stylesheet" type="text/css" media="print" href="${contextPath}/styles/decorator-style/print.css" />
<link rel='stylesheet' href="${contextPath}/styles/style_js.css" type='text/css'>
<decorator:head />

</head>
<body
	<decorator:getProperty property="body.id" writeEntireProperty="true"/>
	<decorator:getProperty property="body.class" writeEntireProperty="true"/>
	<decorator:getProperty property="body.onload" writeEntireProperty="true"/>
	<decorator:getProperty property="body.onunload" writeEntireProperty="true"/>>


<table width="100%" cellpadding="0" cellspacing="0" border="0" style="margin:0px;">
	<tr>
	  <td width="100%">
	    <page:applyDecorator page="/decorators/simpleheader.jsp" name="simpleheader" />
	    <!--end header-->
     </td>
	</tr>
	<tr>
	  <td width="100%" align="center" style="border:none;">
	    <div id="content" class="clearfix" style="border:none;">
		  <table width="100%" border=0 cellpadding="0" cellspacing="0" style="margin:0px;">
            <tr>
			  <td valign="top" width="230" class="sidebar_menu_bg">
			    <div class="user_admin_dashboard">
                	<div class="admin_pic"></div>
                    <div class="admin_txt">
		             	<%
						AppUserModel appUser = UserUtils.getCurrentUser();
						if(null!=appUser)
						{
							if (null!=appUser.getFirstName())
							{
								out.print(appUser.getFirstName());
								if ((appUser.getFirstName().length() + appUser.getFirstName().length()) > 50)
								{
									out.print("</BR>");
								}
							}
								
							out.print("&nbsp;");
		
							if (null!=appUser.getLastName())
								out.print(appUser.getLastName());
						}
					 %>
				</div>
                    <div class="date_txt"><%=PortalDateUtils.currentFormattedDate("EEE, MMM dd, yyyy")%></div>
                </div>
                <div id="nav">
				  <div class="wrapper"><page:applyDecorator page="/decorators/menu.jsp" name="menu"/></div>
				</div><!-- end nav -->
			  </td>
              <td style="background-color:#ecf0f1; border-left: 1px dotted #d2d2d2; color:#fff;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			  <td valign="top" width="100%" bgcolor="#ecf0f1">
                <div id="main">
                  <h1><decorator:getProperty property="meta.title"/></h1>
				  <decorator:body/>
                  <div class="clearfix"></div>
				</div><!--end main-->
			  </td>
              <td style="background-color:#ecf0f1; color:#fff;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			</tr>
		  </table>
		</div>
		<!--end content--></td>
	</tr>

	<tr>
		<td width="100%">
		  <page:applyDecorator page="/decorators/footer.jsp" name="footer" />
		  <!--end footer-->
		</td>
	</tr>
</table>

<!--end page-->

</body>
</html>
