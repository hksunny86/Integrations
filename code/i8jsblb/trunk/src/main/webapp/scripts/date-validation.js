	function validateDateRange(_fDate,_tDate,startlbl,endlbl,serverDate)
	{
		var isValid = true;
  		
		if((_fDate == '') && (_tDate == '')){
  			return isValid;
  		}
  		else if( !(_fDate == '') && _tDate ==''){						
				alert("Provide "+endlbl);
				isValid = false;										
		}else if( !(_tDate == '') && _fDate==''){					
				alert("Provide "+startlbl);
				isValid = false;
  		}else {
  				isValid = isValidDateRange(_fDate,_tDate,startlbl,endlbl,serverDate);
  		}

		return isValid;
	}
	
	function validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,serverDate)
	{
		var isValid = true;
  		
		if( _fDate == '' && _tDate == '' ){
			alert("Please provide "+startlbl+" and "+endlbl);
		    isValid = false;					
		}else if( _fDate == '' ){
			alert("Please provide "+startlbl);
		    isValid = false;					
		}else if( _tDate == '' ){
			alert("Please provide "+endlbl);
		    isValid = false;					
		}else {
  			isValid = isValidDateRange(_fDate,_tDate,startlbl,endlbl,serverDate);
  		}		
		return isValid;
	}
	
	function isValidDateRange(_fDate,_tDate,startlbl,endlbl,serverDate)
	{		
			var isValid = true;
			var fDate = getJsDate( _fDate );				
			var tDate = getJsDate( _tDate );
			serverDate= getJsDate( serverDate );
			if( fDate > serverDate && tDate > serverDate) {
				alert(startlbl+" and "+endlbl+" can't be in future.");
				isValid = false;
			}
			else if(fDate > serverDate){
					alert(startlbl+" can't be in future.");
					isValid = false;		
			}
			else if(tDate > serverDate){
					alert(endlbl+" can't be in future.");
					isValid = false;		
			}
			else if(!(fDate<=tDate)) {
					 alert(startlbl+" should be less than or equal to "+endlbl);
			    isValid = false;
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