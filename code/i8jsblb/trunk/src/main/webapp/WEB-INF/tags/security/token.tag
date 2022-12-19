<%@tag description="Synchronizer Token" import="com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator" %>

<input 
	type="hidden" 
		name="<%= AllPayWebResponseDataPopulator.TOKEN_KEY %>" 
			value="<%=session.getAttribute(AllPayWebResponseDataPopulator.TOKEN_KEY)%>" />