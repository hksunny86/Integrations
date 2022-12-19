var jq = jQuery.noConflict();
jq(document).ready(
		function() {
			var containerMargin = parseInt( jq('.container').css('margin-top'), 10 ) + parseInt( jq('.container').css('margin-bottom'), 10 );
			//alert( containerMargin );
			var topHeaderHeight = jq('#header_portal').height();
			//alert( 'topHeaderHeight: ' + topHeaderHeight );
			var windowHeight = jq(window).height();
			//alert('windowHeight: ' + windowHeight );
			var footerHeight = jq('#footer-agent-web').height();
			//alert( 'footerHeight: ' + footerHeight );
			jq('#contents').height( windowHeight - topHeaderHeight - containerMargin - footerHeight );
		}
);