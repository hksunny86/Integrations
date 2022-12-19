<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html; charset=utf-8" %>
<jsp:directive.page import="com.inov8.microbank.common.util.CustomRolesPermissionsAdapter"/>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="authz" %>
<%@ taglib uri="http://www.springmodules.org/tags/commons-validator" prefix="v" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="html" %>
<%@ taglib uri="http://struts-menu.sf.net/tag-el" prefix="menu" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.inov8.com.pk/tags/generic-toolbar" prefix="GenericToolbar"%>
<%@ taglib uri="http://www.inov8.com.pk/tags/ordershuttle" prefix="orderShuttletag" %>
<%@ taglib uri="http://ajaxtags.org/tags/ajax" prefix="ajax" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/ajax" %>
<%@ taglib prefix="security" tagdir="/WEB-INF/tags/security" %>
<c:set var="contextPath" scope="request">${pageContext.request.contextPath}</c:set>
<%pageContext.setAttribute("customRolesPermissionsAdapter",new CustomRolesPermissionsAdapter(request));%>
