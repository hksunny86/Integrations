	function deleteRecord(url, id, model, property, 
				httpMethod, callback, error, delLabel, deltdLabel,
				isButton, hardDel, useCaseId, action)
	{
		if( null != document.getElementById("successMsg") && document.getElementById("successMsg") != 'undefined' )
		{
			Element.hide('successMsg');
		}
		if(null != document.getElementById("successMessages") && document.getElementById("successMessages") != 'undefined')
	    {
	    	Element.hide('successMessages'); // Added By Muzzamil Idrees (21-11-2007)
	    }
		if (confirm('Are you sure you want to delete this record?'))
		{
			pars = 	'id='+ id + '&prop='+property 
					+'&model='+model+'&delLabel='+delLabel+'&deltdLabel='+deltdLabel
					+'&isBtn='+isButton+'&useCaseId='+useCaseId+'&hardDel='+hardDel+'&actionId='+action;	
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
	
	function isDelOperationSuccessful(request)
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
	      		$('del_btn'+id).value = label;
	      	}
			else
			{
				$('del_href'+id).innerHTML = label;
				$('del_href'+id).parentNode.parentNode.innerHTML = '';
			}
		    if(null != document.getElementById("successMessages") && document.getElementById("successMessages") != 'undefined')
		    {
		    	Element.hide('successMessages'); // Added By Maqsood Shahzad (23-05-2007)
		    }

		    $('successMsg').innerHTML = message;
			Element.show('successMsg');
	      }
	      else
	      	if(id==null && message!=null)
	      	{
			    $('successMsg').innerHTML = message;
				Element.show('successMsg');
	      	}
	}
	
	function defaultError(request)
	{
	  alert("An unknown error has occured. Please contact with the administrator for more details");
	}
	