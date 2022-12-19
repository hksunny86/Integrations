<%@ page language="java" isErrorPage="true" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>

<title>An error has occurred</title>
<meta name="decorator" content="decorator">	
</head>

<body>  
	  <table width="100%" border=0 cellpadding="0" cellspacing="0">
	  <tr>
	  	<td width="100%" align="center" class="errorMessage" style="font-size:20px;" >
	  	
	  		  <br><br><br><br>
	  	</td>
	  </tr>
	  
		<tr>	
			 <td  width="100%" align="center" style="font-size:20px;color: #FF8000">		
				Dear Customer, Your request cannot be processed at the moment. Please try again later.<br>
				
				<% 
						if(exception!=null)
						{
							exception.printStackTrace();
							/* This part of code forwards the user to the login page in case the user account is disabled, locked, expired, etc,.*/	
							if(exception instanceof org.springframework.security.authentication.BadCredentialsException)
						 	{
						 		request.getRequestDispatcher("login.jsp?error=true").forward(request, response);
						 		
							} 
							else if(exception instanceof IllegalArgumentException)
						 	{
						 		request.getRequestDispatcher("home.html").forward(request, response);
							}
						}
				    %>		
			</td>
		</tr>
	  </table>
</body>
</html>

