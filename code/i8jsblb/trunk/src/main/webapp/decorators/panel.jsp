
<jsp:directive.page import="com.inov8.microbank.common.util.UserUtils"/>
<jsp:directive.page import="com.inov8.microbank.common.util.PortalDateUtils"/>
<jsp:directive.page import="com.inov8.microbank.common.model.AppUserModel"/><!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<%@ include file="/common/taglibs.jsp"%>

<div align="left" id="linktext">
	<table width="100%" border="0">
		<tr>
			<td style="width: 132px;">&nbsp;</td>
			<td class="logoHeader" style="font-weight: normal; text-align: center;width: 148px;"><%=PortalDateUtils.currentFormattedDate("EEE, MMM dd, yyyy")%></td>
			<td class="logoHeader" style="font-weight: bold; text-align: center;width: 475px">
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
			</td>
			<td class="logoHeader" style="text-align: left; width: 270px; padding-left: 0px;padding-right: 0px;">
					  <a href="${contextPath}/home.html" title="Home">Home</a>
					  <a href="changepasswordform.html" title="Change Password">Change Password</a>
					  <a href="${contextPath}/logout.jsp" title="signout">Sign Out</a>
			</td>
			<td style="text-align: left; width: 60px; padding-left: 0px;">&nbsp;</td>
		</tr>
	</table>
				</div><!--end linktext-->
<%-- 
<span style="padding-left:150px; color: #497695; font-weight: normal"><%=com.inov8.microbank.common.util.Formatter.getCurrentDate()%></span>
<span style="padding-left: 75px; padding-right:130px; color: #497695;">Muhammad Asghar Arshad Ali</span>

  
  <a href="home.html" title="Home">Home</a>&nbsp;&nbsp;&nbsp;
  <!-- <a href="password.html" title="Change Password">Change Password</a>&nbsp;&nbsp;&nbsp; -->
  <a href="logout.jsp" title="signout">Sign Out</a>&nbsp;&nbsp;
 --%>	 
