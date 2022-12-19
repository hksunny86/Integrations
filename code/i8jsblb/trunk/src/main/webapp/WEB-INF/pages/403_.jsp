<%@ page language="java" isErrorPage="true" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>Access Denied</title>
<meta name="decorator" content="decorator">	

</head>
<body>  
		<%
			if(exception!=null)
			{
				exception.printStackTrace();
			}
		%>
	  <table width="100%" border=0 cellpadding="0" cellspacing="0">
	  <tr>
	  	<td width="100%" align="center" class="errorMessage" style="font-size:20px;" >
	  	
	  		Access Denied  <br><br><br><br>
	  	</td>
	  </tr>
	    <tr>
		  <td  width="100%" align="center" style="font-size:15px;" >	
			  You do not have rights to proceed. Please contact your system administrator
		  </td>
		</tr>
	  </table>
</body>
</html>
