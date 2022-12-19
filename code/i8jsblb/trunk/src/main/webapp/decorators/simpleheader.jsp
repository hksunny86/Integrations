<%@ page import="com.inov8.microbank.common.util.UserUtils" %>
<div id="header_portal">
    <% if(!(UserUtils.getCurrentUser().getAppUserTypeId()== 7L)){ %>
    <div class="logo_portal"></div>
    <% }        %>
    <% if(UserUtils.getCurrentUser().getAppUserTypeId()== 7L){ %>
    <div class="sco_logo_portal"></div>
    <% }        %>
    <div class="header_top_buttons">
    	<a href="${contextPath}/home.html" title="Home" class="btn btn-large btn-home btn-home-icon">Home</a>
        <a href="changepasswordform.html" title="Change Password" class="btn btn-large btn-setting btn-setting-icon">Change Password</a>
           <a href="${contextPath}/logout.jsp" title="Logout" class="btn btn-large btn-signout btn-signout-icon">Signout</a>
        <br clear="all" />
    </div>
</div>