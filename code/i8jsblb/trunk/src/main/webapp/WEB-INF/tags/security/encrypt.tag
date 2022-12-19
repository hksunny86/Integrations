<%@ tag import="com.inov8.microbank.common.util.EncryptionUtil"%>

<%@ attribute name="strToEncrypt" required="true" %>

<%=EncryptionUtil.encryptWithDES(strToEncrypt)%>
