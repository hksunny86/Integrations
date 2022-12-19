<%@ tag import="com.inov8.microbank.common.util.EncryptionUtil,com.inov8.microbank.common.util.PortalConstants"%>
<%@ attribute name="id" required="true"%>
<%@ attribute name="model" required="true" %>
<%@ attribute name="property" required="true"%>
<%@ attribute name="propertyValue" required="true" type="java.lang.Boolean"%>
<%@ attribute name="callback" required="true"%>
<%@ attribute name="error" required="true"%>
<%@ attribute name="url" required="false"%>
<%@ attribute name="deleteLabel" required="false"%>
<%@ attribute name="deletedLabel" required="false"%>
<%@ attribute name="isButton" required="false" type="java.lang.Boolean"%>
<%@ attribute name="hardDel" required="false" type="java.lang.Boolean"%>
<%@ attribute name="useCaseId" type="java.lang.Long" required="true"%>

	<%
		String encryptedId = EncryptionUtil.encryptWithDES(id);
		String serverURL = "deleterecord.html";
		String delLabel = "Delete";
		String deltdLabel = "Deleted";
		Boolean hardDelete = false;
		
	if(url!=null)
			serverURL = url;

	if(deleteLabel!=null)
		delLabel = deleteLabel;

	if(deletedLabel!=null)
		deltdLabel = deletedLabel;
		
	if(hardDel!=null)
		hardDelete = hardDel;
	
	String label = null;
	
	if(!propertyValue.booleanValue())
		label = delLabel;
	else 
		label = deltdLabel;
		
	%>
	
	<% 
		if(null==isButton || !isButton.booleanValue())
		{
	%>	
	<% if(!propertyValue.booleanValue()) {%>
			<a id="del_href<%=encryptedId %>" class="deleteLink"
				href="javascript:deleteRecord('${pageContext.request.contextPath}/<%=serverURL %>', '<%=encryptedId %>', '${model}', '${property}', 'get', ${callback}, ${error}, '<%=delLabel %>', '<%=deltdLabel %>', '${isButton}', '<%=hardDelete%>', '${useCaseId}', '<%=PortalConstants.ACTION_DELETE %>')">
			<%=label %>
			</a>
	<%} else {%>
		<%=label %>
	<%} %>
	<% 
		} else {
	%>
		<% if(!propertyValue.booleanValue()) {%>
			<input id="del_btn<%=encryptedId %>" 
				type="button"
				class="button deleteBtn"
				name="del_btn<%=encryptedId %>" 
				onclick="javascript:deleteRecord('${pageContext.request.contextPath}/<%=serverURL %>', '<%=encryptedId %>', '${model}', '${property}', 'get', ${callback}, ${error}, '<%=delLabel %>', '<%=deltdLabel %>', '${isButton}', '<%=hardDelete%>', '${useCaseId}', '<%=PortalConstants.ACTION_DELETE %>')"
				value="<%=label %>" />
		<%} else {%>
			<input 
				type="button"
				class="button deleteBtn"
				name="del_btn<%=encryptedId %>" 
				disabled="disabled"
				value="<%=label %>" />
		<%} %>
	<%
		}
	 %>	
