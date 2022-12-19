	function activateDeactivate(url, id, model, property, 
				httpMethod, callback, error, activeMsg, 
				deactiveMsg, activeLabel, deactiveLabel,
				isButton, action)
	{
		if( null != document.getElementById("successMsg") && document.getElementById("successMsg") != 'undefined' )
		{
			Element.hide('successMsg');
		}
		if(null != document.getElementById("successMessages") && document.getElementById("successMessages") != 'undefined')
	    {
	    	Element.hide('successMessages'); // Added By Muzzamil Idrees (21-11-2007)
	    }
		if (confirm('Are you sure you want to update status?'))
		{
			pars = 	'id='+ id + '&prop='+property 
					+'&model='+model+'&actMsg='+activeMsg+'&deactMsg='+deactiveMsg
					+'&actLabel='+activeLabel+'&deactLabel='+deactiveLabel+'&isBtn='+isButton+'&actionId='+action;
			var myAjax = new Ajax.Request(
				url, 
				{
					method: httpMethod, 
					parameters: pars,
					onFailure: error,
					onSuccess: callback
				});
		}
	}
	
	function isOperationSuccessful(request)
	{
		var xmlDoc = request.responseXML;
		var options = xmlDoc.getElementsByTagName("item");
		var label = "";
		var isBtn = false;
		var id = null;
		var message = null;
	      for (var i=0; i<options.length; i++) 
	      {
	        var nameNodes = options[i].getElementsByTagName("name");
	        var valueNodes = options[i].getElementsByTagName("value");
	        if (nameNodes.length > 0 && valueNodes.length > 0) 
	        {
	          var name = nameNodes[0].firstChild.nodeValue;
	          var value = valueNodes[0].firstChild.nodeValue;
	          if(name == "id")
	          {
	          	id = value;
	          }
	          else if(name=="label")
	          	{
	          		label = value;
	          	}
	          else if(name=="mesg")
	          {
	          	message = value;
	          }	
	          else if(name=="isBtn")
	          {
	          	if(value=="true")
	          		isBtn = true;
	          }	
	        }
	      }
	      if(id!==null && message!==null)
	      {
	      	if(isBtn)
	      	{
	      		$('_btn'+id).value = label;
	      	}
			else
			{
				$('_href'+id).innerHTML = label;
			}
		    if(null != document.getElementById("successMessages") && document.getElementById("successMessages") != 'undefined')
		    {
		    	Element.hide('successMessages'); // Added By Maqsood Shahzad (23-05-2007)
		    }

		    $('successMsg').innerHTML = message;
			Element.show('successMsg');
	      }
	      else
	      {
	    	  if(id==null && message!=null)
	    	  {
	    		  $('successMsg').innerHTML = message;
	    		  Element.show('successMsg');
	    	  }
	      }
	      scrollToTop(1000);
	}
	
	function defaultError(request)
	{
	  alert("An unknown error has occured. Please contact with the administrator for more details");
	}
	
	function commissionError(request)
	{
		alert("Only 1 Account of a Stakeholder with same type can be Activated at a time. Please Deactivate all others to Activate this one.");
	}