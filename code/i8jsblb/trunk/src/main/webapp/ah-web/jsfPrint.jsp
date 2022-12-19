<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<html>
<head>
<title>Insert title here</title>
</head>
<body>
<f:view>
<h:form>
	<h:outputText value="Your name is: #{testClass.name}" />
	<h:outputText value="Result is : #{testClass.number.result}" />
</h:form>
</f:view>
</body>
</html>