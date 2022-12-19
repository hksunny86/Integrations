<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="com.inov8.microbank.common.util.PortalConstants" %>
<%@ include file="/common/taglibs.jsp"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
	<meta name="decorator" content="decorator">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta name="title" content="Bulk Filer / Non Filer Update" />
		<!--  <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>-->
		<script type="text/javascript" src="${contextPath}/scripts/prototype.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/toolbar.js"></script>
		<%@include file="/common/ajax.jsp"%>
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
  </head>
  
  <body bgcolor="#ffffff">
    	<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
    
    	<html:form name="bulkFilerNonFilerForm" commandName="bulkFilerNonFilerVO" enctype="multipart/form-data" method="POST"
				action="p_bulkupdatefilernonfiler.html">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
					<table width="100%" border="0" cellpadding="0" cellspacing="1">
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Upload File:
							</td>
							<td bgcolor="FBFBFB" class="text">
								<input type="file" path="csvFile" id="csvFile" name="csvFile" tabindex="1" 
										class="upload" />
								&nbsp;&nbsp;
							</td>
						</tr>
		
						<tr>
							<td width="50%">&nbsp;</td>
							<td align="left">
								<input type="button" name="_save" value="  Preview  " tabindex="2"
									onclick="javascript:onFormSubmit(document.forms.bulkFilerNonFilerForm);" class="button" />
							</td>
						</tr>
					</table>
			</html:form>
    
    		<script language="javascript" type="text/javascript">
    		
			 function onFormSubmit(theForm){ 
      			if(document.forms.bulkFilerNonFilerForm.csvFile.value == ''){
      			alert("File Location is required.");
      			return false;
      	}
      		if(!document.forms.bulkFilerNonFilerForm.csvFile.value.endsWith('.csv')){
      		alert("Only files with extension (.csv) are allowed.");
      		return false;
	    }
      		if(getFileSize() > 2){
          	alert("File to upload can't be more than 2MB in size.");
          	return false;
        }
      
	   		 onSave(document.forms.bulkFilerNonFilerForm);
      }
			 function getFileSize(){
				    var file = document.getElementById('csvFile');
				    if(file != null && file.files != null && file.files[0] != null){
						return ((file.files[0].size/1024)/1024);
					}
					return 0;
				}
			 </script>
  </body>
</html>
