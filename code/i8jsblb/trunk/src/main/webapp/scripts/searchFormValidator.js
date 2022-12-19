
function validateForm(form) {
	
	var isValid=false;
	jq("form").eq(0).find(':text').each(function() {
		if(this.value!=undefined && this.value!= "")
		{
			isValid=true;
			return false;
		}
	});
	
	jq("form").eq(0).find(':selected').each(function() {
		if(this.value!=undefined && this.value!= "")
		{
			isValid=true;
			return false;
		}
	});
	
	if(!isValid){
		alert("Kindly provide some search criteria");
		return false;
	}
	
	var _fDate =undefined;
	var _tDate = undefined;
	
	if(form.startDate!=undefined)
	{
		_fDate = form.startDate.value;
	}
	if(form.endDate!=undefined)
	{
		_tDate = form.endDate.value;
	}
	
	var startlbl = "Start Date";
	var endlbl = "End Date";

	isValid=	isValidDateRange(_fDate,_tDate,startlbl,endlbl,serverDate);
	
	if(isValid)
	{
		isValid = validateFormChar(form);
	}
	
	return isValid;
}

function isValidDateRange(_fDate,_tDate,startlbl,endlbl,serverDate)
{		
		if((_fDate==undefined || _fDate=="" || _fDate==null) && ( _tDate==undefined  || _tDate=="" || _tDate==null))
			return true;
	
		var isValid = true;
		serverDate= getJsDate( serverDate );
		
		if(_fDate!=undefined && _fDate!="")
		{
			var fDate = getJsDate( _fDate );				
			if(fDate > serverDate){
				alert(startlbl+" can't be in future.");
				isValid = false;		
			}	
		}
		
		if(_tDate!=undefined && _tDate!="")
		{
			var tDate = getJsDate( _tDate );
			
			if(tDate > serverDate){
				alert(endlbl+" can't be in future.");
				isValid = false;		
			}
		}
		
		if(_fDate!=undefined && _fDate!="" && _tDate!=undefined  && _tDate!="" )
		{
			var fDate = getJsDate( _fDate );
			var tDate = getJsDate( _tDate );
			
			if(!(fDate<=tDate)) {
				alert(startlbl+" should be less than or equal to "+endlbl);
			    isValid = false;
			}		
		}
		
	return isValid;
}

function getJsDate( date )
{
	var jsDate = new Date();
	date=date.split(" ");

	jsDate.setFullYear( date[0].split('/')[2] );
	jsDate.setMonth( (date[0].split('/')[1])-1 );
	jsDate.setDate( date[0].split('/')[0] );
	jsDate.setHours( 0 );
	jsDate.setMinutes( 0 );
	jsDate.setSeconds( 0 );
	jsDate.setMilliseconds( 0 );

	return jsDate;
}  

