<%@ tag import="com.inov8.microbank.common.util.UserUtils, com.inov8.microbank.common.util.EncryptionUtil"%>

<%@ attribute name="action" required="true" type="java.lang.Long"%>
<%@ attribute name="ifAccessAllowed" required="true" fragment="true"%>
<%@ attribute name="ifAccessNotAllowed" required="true" fragment="true"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<c:set var="isAuthorized" ><%=UserUtils.isAccessAllowed(action, UserUtils.getCurrentUser())%></c:set>
<c:choose>
	<c:when test="${isAuthorized eq true}">
		<jsp:invoke fragment="ifAccessAllowed"/>
	</c:when>
	<c:otherwise>
		<jsp:invoke fragment="ifAccessNotAllowed"/>
	</c:otherwise>
</c:choose>