
	 <div id="footer_portal" style="margin-top:1px;">
		<div class="logo_inov8"><a></a></div>
<%--	    <span>Ahsan Raza<br /><br/> 2014 - Engineered and Powered by Js Bank Limited.</span>--%>
	</div>    
    

<%--    <div id="divider"><div></div></div>--%>
<%--    <span class="right"><a href=""></a>--%>
<%--    </span>--%>

<script>
	
	
	
	var divs = document.getElementsByName('popcal');
	
	for(var i = 0; i < divs.length; i++) {
        
		divs[i].align = 'top';
		divs[i].style.marginTop = '8px';
		divs[i].style.marginLeft = '3px';
		
		/*
		divs[i].onmouseover= function(){
			//alert(this.getAttribute('src'));
			var fullpath 	= this.getAttribute('src');	// get image path string (images/xyz.gif)
			var start 		= fullpath.slice(0,-4);		// get starting string "images/xyz"
			var end			= fullpath.slice(-4);		// get ending ".gif"
			this.src		= '${contextPath}/'+start+'_hover'+end;
			//alert(end);
			}
		divs[i].onmouseout= function(){
			//alert(this.getAttribute('src'));
			var fullpath 	= this.getAttribute('src');	// get image path string (images/xyz.gif)
			var start 		= fullpath.slice(0,-10);		// get starting string "images/xyz"
			var end			= fullpath.slice(-4);		// get ending ".gif"
			this.src		= '${contextPath}/'+start+end;
			alert(this.src);
			}
		*/	
    }
	
	
	
	

	//document.getElementsByName("popcal").item(n).align = "top";
	//document.getElementsByName("popcal").item(1).style.marginTop = "-10";
	//document.getElementsByName("popcal").item.style.marginTop = "-10px";
	//document.getElementById("eDate").align="middle";
</script>