<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<%@include file="/common/taglibs.jsp"%>
<html>
	
	<head>
<meta name="decorator" content="decorator">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="Available Payment Modes" />
		
	</head>

	<body bgcolor="#ffffff">

					
				<ul>			  					
  				<c:forEach items="${paymentModeList}" var="paymentMode">
					<li><c:out value="${paymentMode.name}"/></li>
  				</c:forEach>	
  				</ul>
 		
  		
	

	</body>


</html>    				
    				
    		    		     		  		
 		
	
			
		
	
      					             	   			
        					
        				
    				
				
								
					
    				
      					              	   			
    						
        				
    				
				
				
					
    				
      		  			              				
         	  				
         	  			
    				
				
			
					
    				
      		  			              				
         	  				        					    					
         	  			
    				
				
				
					
    				
      		  			              				
         	  					
         	  			
    				
				
				
						
           		  		
		       			
    				
				
				
				
					
    				
      					
              	   			
        						
        				
    				
				
			
				
				
					
						
						
					
					
				
   		
		

			


