<%@ tag import="com.inov8.microbank.common.util.EncryptionUtil,com.inov8.microbank.common.util.PortalConstants"%>
<%@ attribute name="id" required="true"%>
<%@ attribute name="model" required="true" %>
<%@ attribute name="property" required="true"%>
<%@ attribute name="propertyValue" required="true" type="java.lang.Boolean"%>
<%@ attribute name="callback" required="true"%>
<%@ attribute name="error" required="true"%>
<%@ attribute name="url" required="false"%>
<%@ attribute name="activeMessage" required="false"%>
<%@ attribute name="deactiveMessage" required="false"%>
<%@ attribute name="activeLabel" required="false"%>
<%@ attribute name="deactiveLabel" required="false"%>
<%@ attribute name="isButton" required="false" type="java.lang.Boolean"%>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean"%>

	<%
		String encryptedId = EncryptionUtil.encryptWithDES(id);
		String serverURL = "activatedeactivate.html";
		String actLabel = "Activate";
		String deactLabel = "Deactivate";
		
	if(url!=null)
			serverURL = url;

	if(activeLabel!=null)
		actLabel = activeLabel;

	if(deactiveLabel!=null)
		deactLabel = deactiveLabel;
	
	String label = null;
	
	if(!propertyValue.booleanValue())
		label = actLabel;
	else 
		label = deactLabel;
	
	%>
	
	<% 
		if(null==isButton || !isButton.booleanValue())
		{
	%>	
	<% if(null==disabled || !disabled.booleanValue()) {%>
			<a id="_href<%=encryptedId %>"
				href="javascript:activateDeactivate('${pageContext.request.contextPath}/<%=serverURL %>', '<%=encryptedId %>', '${model}', '${property}', 'get', ${callback}, ${error}, '${activeMessage}', '${deactiveMessage}', '<%=actLabel %>', '<%=deactLabel %>', '${isButton}', '<%=PortalConstants.ACTION_UPDATE %>')">
				<%=label %>
			</a>
	<%} else {%>
		<%=label %>
	<%} %>
	<% 
		} else {
	%>
		<input id="_btn<%=encryptedId %>" 
			type="button"
			class="button"
			name="_btn<%=encryptedId %>" 
			onclick="javascript:activateDeactivate('${pageContext.request.contextPath}/<%=serverURL %>', '<%=encryptedId %>', '${model}', '${property}', 'get', ${callback}, ${error}, '${activeMessage}', '${deactiveMessage}', '<%=actLabel %>', '<%=deactLabel %>', '${isButton}', '<%=PortalConstants.ACTION_UPDATE %>')"
			value="<%=label %>" />
	<%
		}
	 %>	
