function doModify(url){
	//alert('in doModify()');
	//alert(url);
	if(window.opener && !window.opener.closed){
		//alert('opener exists');
		window.opener.open(url, "_blank");
	}else{
		//alert('no opener');
		window.open(url, "_blank");
	}
}

function MM_swapImgRestore() { //v2.0
  if (document.MM_swapImgData != null)
    for (var i=0; i<(document.MM_swapImgData.length-1); i+=2)
      document.MM_swapImgData[i].src = document.MM_swapImgData[i+1];
}

function MM_preloadImages() { //v2.0
  if (document.images) {
    var imgFiles = MM_preloadImages.arguments;
    if (document.preloadArray==null) document.preloadArray = new Array();
    var i = document.preloadArray.length;
    with (document) for (var j=0; j<imgFiles.length; j++) if (imgFiles[j].charAt(0)!="#"){
      preloadArray[i] = new Image;
      preloadArray[i++].src = imgFiles[j];
  } }
}

function MM_swapImage() { //v2.0
  var i,j=0,objStr,obj,swapArray=new Array,oldArray=document.MM_swapImgData;
  for (i=0; i < (MM_swapImage.arguments.length-2); i+=3) {
    objStr = MM_swapImage.arguments[(navigator.appName == 'Netscape')?i:i+1];
    if ((objStr.indexOf('document.layers[')==0 && document.layers==null) ||
        (objStr.indexOf('document.all[')   ==0 && document.all   ==null))
      objStr = 'document'+objStr.substring(objStr.lastIndexOf('.'),objStr.length);
    obj = eval(objStr);
    if (obj != null) {
      swapArray[j++] = obj;
      swapArray[j++] = (oldArray==null || oldArray[j-1]!=obj)?obj.src:oldArray[j];
      obj.src = MM_swapImage.arguments[i+2];
  } }
  document.MM_swapImgData = swapArray; //used for restore
}
function isAlphanumeric(toCheck) {
	toCheck.value = trim(toCheck.value);	
	if(toCheck.value.length>0){
		var reAlphanumeric = /^[a-zA-Z0-9,.\- ]+$/;
		return reAlphanumeric.test(toCheck.value);
	}
	else
		return true;
}

function isAlphanumericWithSpace(toCheck) {
	toCheck.value = trim(toCheck.value);
	if(toCheck.value.length>0){
		var reAlphanumeric = /^[a-z A-Z0-9,.\- ]+$/;
		return reAlphanumeric.test(toCheck.value);
	}
	else
		return true;
}

function getDifferenceInDays(fromDateStr, toDateStr) {
	var MILLISECONDS = 1000*60*60*24;
	var fromDate = new Date(fromDateStr);
	var toDate = new Date(toDateStr);
	var days = (toDate.getTime() - fromDate.getTime()) / MILLISECONDS;
	return Math.round(days);
}

function isDateSmaller(from, to) {

	var fromDate;
	var toDate;

	var result = false;
	fromDate = from.substring(6,10) + from.substring(3,5) + from.substring(0,2);
	toDate = to.substring(6,10)   + to.substring(3,5) + to.substring(0,2);
	
//   format used for mm/yyyy
//	fromDate = from.substring(3,7) + from.substring(0,2);
//	toDate = to.substring(3,7)   + to.substring(0,2);

	if( fromDate < toDate ) {
		result = true;
	}

	return result;
}


function isDateGreater(from, to) {

	var fromDate;
	var toDate;

	var result = false;
	fromDate = from.substring(6,10) + from.substring(3,5) + from.substring(0,2);
	toDate = to.substring(6,10)   + to.substring(3,5) + to.substring(0,2);
	
//   format used for mm/yyyy
//	fromDate = from.substring(3,7) + from.substring(0,2);
//	toDate = to.substring(3,7)   + to.substring(0,2);

	if( fromDate > toDate ) {
		result = true;
	}

	return result;
}

function isDateGreaterOrEqualForCard(from, to) {

	var fromDate;
	var toDate;

	var result = false;

//  format dd/mm/yyyy
//	fromDate = from.substring(6,10) + from.substring(3,5) + from.substring(0,2);
//	toDate = to.substring(6,10)   + to.substring(3,5) + to.substring(0,2);

//   format used for mm/yyyy
	fromDate = from.substring(3,7) + from.substring(0,2);
	toDate = to.substring(3,7)   + to.substring(0,2);

	if( fromDate != toDate && fromDate > toDate ) {
		result = true;
	}

	return result;
}

function isDateGreaterOrEqual(from, to) {

	var fromDate;
	var toDate;

	var result = false;

	fromDate = from.substring(6,10) + from.substring(0,2) + from.substring(3,5);
	toDate = to.substring(6,10)   + to.substring(0,2)   + to.substring(3,5);
	if( fromDate >= toDate ) {
		result = true;
	}

	return result;
}

function isNumber(toCheck){
	toCheck.value = trim(toCheck.value);
	var reFloat = /^\d+$/;
	if(reFloat.test(toCheck.value)){
		toCheck.value = trimLeadingZeros(toCheck.value);
		return true;
	}
	return false;
}

function isNumberWithZeroes(toCheck){
	toCheck.value = trim(toCheck.value);
	var reFloat = /^\d+$/;
	if(reFloat.test(toCheck.value)){
		toCheck.value = trim(toCheck.value);
		return true;
	}
	return false;
}


function isAlpha(toCheck){
	toCheck.value = trim(toCheck.value);
	var reFloat = /^[a-zA-Z]+$/;
	return reFloat.test(toCheck.value);
}

function isAlphaWithSpace(toCheck){
	toCheck.value = trim(toCheck.value);
	var reFloat = /^[a-z A-Z]+$/;
	return reFloat.test(toCheck.value);
}


// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=2100;

function isInteger(s){
	var i;
    for (i = 0; i < s.length; i++){
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
		if (i==2) {this[i] = 29}
   }
   return this
}

function isDate(dtStr){
	var pos1=dtStr.indexOf(dtCh);
	var strMonth=dtStr.substring(0,pos1)
	var strYear=dtStr.substring(pos1+1)

	strYr=strYear
	if (strMonth.charAt(0)=="0" && strMonth.length>1){
          strMonth=strMonth.substring(1);
        }
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1){
                strYr=strYr.substring(1);
                }
	}
	month=parseInt(strMonth);

	year=parseInt(strYr);

	if (pos1==-1 ){
		alert("The date format should be : mm/yyyy")
		return false
	}

	if (strMonth.length<1 || month<1 || month>12){
		alert("Please enter a valid month")
		return false
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		return false
	}

	if (dtStr.indexOf(dtCh,0)==-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		alert("Please enter a valid date")
		return false
	}
return true
}


function isDate1(dtField){
	dtStr = dtField.value;
	var daysInMonth = DaysArray(12)
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strDay=dtStr.substring(0,pos1)
	var strMonth=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		alert("The date format should be : dd/mm/yyyy.")
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		//alert("Please enter a valid day.")
		alert("Please enter a valid date.")
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		//alert("Please enter a valid month.")
		alert("Please enter a valid date.")
		return false
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		//alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		alert("Please enter a valid 4 digit year.")
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		alert("Please enter a valid date.")
		return false
	}
return true
}

function isValidDate(dateString,lable)
{
    // First check for the pattern
    if(!/^\d{1,2}\/\d{1,2}\/\d{4}$/.test(dateString))
        return false;

    // Parse the date parts to integers
    var parts = dateString.split("/");
    var day = parseInt(parts[0], 10);
    var month = parseInt(parts[1], 10);
    var year = parseInt(parts[2], 10);

    
    // Check the ranges of month and year
    if(year < 1900 || year > 3000){
    	alert('Year must be between 1900 and 3000 for '+lable);
    	return false;
    }
    
    // Check the ranges of month and year
    if(month == 0 || month > 12){
    	alert('Month is not valid for '+lable);
    	return false;
    }
       
    var monthLength = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];

    // Adjust for leap years
    if(year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))
        monthLength[1] = 29;

    // Check the range of the day
    if(!(day > 0 && day <= monthLength[month - 1])){
    	alert('Day is not valid for '+lable);
    	return ;
    }
    
    return true;
}



function ValidateForm(){
	var dt=document.frmSample.txtDate
	if (isDate(dt.value)==false){
		dt.focus()
		return false
	}
    return true
 }



function isEmpty(field)
{
	var result = false;

	if (trim(field.value) == '') {
       	result = true;
     	}

	return result;
}

function isValidRate(field, maxLength, noOfDecimalPlaces)
{
	field.value = trim(field.value);
	var result = true; //for true
	var reFloat;
	if ( noOfDecimalPlaces == 0 )
		reFloat = /^(\d*\,*)*$/;
	else
		reFloat = /^(\d*\,*)*(\.(\d{1}|\d{2})?)?$/;

	if(! reFloat.test(field.value)){
		result = false; // not validate
	}else{
		field.value = trimLeadingZeros(field.value);
		var amount = field.value;
		var whole = "";
		var fraction = ""
		for ( var i = 0 ; i < amount.length; i++) {
		  	
			 if (amount.substr(i,1) == '.'){
				fraction = amount.substr(i+1, amount.length - i );
				break;
			}
			else{
	  			whole = whole + amount.substr(i,1);
			}
		}
		if(whole.length > maxLength) {
			result = false; //exceeds max limit
		}
		
	}

	return result;

}
function isCurrency(field, maxLength, noOfDecimalPlaces)
{
	field.value = trim(field.value);
	var result = 9; //for true
	var reFloat;
	if ( noOfDecimalPlaces == 0 )
		reFloat = /^(\d*\,*)*$/;
	else
		reFloat = /^(\d*\,*)*(\.(\d{1}|\d{2})?)?$/;

	if(! reFloat.test(field.value)){
		result = 0; // not validate
	}else{
		field.value = trimLeadingZeros(field.value);
		var amount = field.value;
		var whole = "";
		var fraction = ""
		for ( var i = 0 ; i < amount.length; i++) {
		  	if (amount.substr(i,1) == ','){
				continue;
			}
			else if (amount.substr(i,1) == '.'){
				fraction = amount.substr(i+1, amount.length - i );
				break;
			}
			else{
	  			whole = whole + amount.substr(i,1);
			}
		}
		if(whole.length > maxLength) {
			result = 1; //exceeds max limit
		}
		else{
			amount = "";
			for ( var j = whole.length; j > 0; j--){
				if ( ( whole.length - j)%3 == 0  && j != whole.length){
					amount =  "," + amount ;
				}
				amount =  whole.substr(j-1,1) + amount;
			}
			if ( fraction != "" ) {
				field.value = amount + "." + fraction;
			}
			else {
				field.value = amount;
			}
		}
	}

	return result;
}
function trimLeadingZeros(strVar)
{
	while(strVar.charAt(0)=='0')
		strVar=strVar.substring(1,strVar.length);
	if ( strVar == "")
		strVar = "0";
	else if ( new Number(strVar) < 1 )
		strVar = "0" + strVar;
	return strVar;
}

function trim(strVar)
{
	while(strVar.charAt(strVar.length-1)==' ')
		strVar=strVar.substring(0,strVar.length-1);

	while(strVar.charAt(0)==' ')
		strVar=strVar.substring(1,strVar.length);

	return strVar;
}


function trimWithOutZeros(strVar)
{
	while(strVar.charAt(strVar.length-1)==' ' && strVar.charAt(strVar.length-1)!='0'  )
		strVar=strVar.substring(0,strVar.length-1);

	while(strVar.charAt(0)==' ' && strVar.charAt(0) != '0' )
		strVar=strVar.substring(1,strVar.length);

	return strVar;
}




// monitoring package spcefic methods

function showCommentsPopup(uri) {
	var yes = 1;
	var no = 0;

	var menubar = no;
	var scrollbars = no;
	var locationbar = no;
	var directories = no;
	var resizable = no;
	var statusbar = no;
	var toolbar = no;
	var width=500;
	var height=100;
	var top=(600-height)/2;
	var left=(800-width)/2;
	windowprops = "width=" + width + ", height=" + height + ", top=" + top + ", left=" + left ;
	windowprops += (menubar ? ", menubars" : "") +
		(scrollbars ? ", scrollbars" : "") +
		(locationbar ? ", location" : "") +
		(directories ? ", directories" : "") +
		(resizable ? ", resizable" : "") +
		(statusbar ? ", statusbar" : "") +
		(toolbar ? ", toolbar" : "");

	commentPopupWindow = window.open(uri, "commentsPopup", windowprops);
}



function toFixed(number, fractDigits) {
	var sign = number < 0 ? "-" : "";
	var multiplier = Math.pow(10,fractDigits);
	var str = ""+Math.round(Math.abs(number)*multiplier);
	var len;
	while ((len = str.length) <= fractDigits) {
		str = "0" + str;
	}
	var wholelen = len-fractDigits;
	return sign + str.substring(0,wholelen) + "." + str.substring(wholelen, len);
}

function roundOff(anynum,pre)
{
   workNum = eval(anynum);
   if (workNum != null)
		retval = toFixed(workNum,pre);
   else
		retval = toFixed(0,pre);
   return retval;
}

function formatMoney(field, pre) {

	var negativeValue = "";
	var amount = field.value;
	negativeValue = amount.substr(0,1);
	if (negativeValue == "-"){
		field.value = amount.substr(1,field.value.length-1);
	}//end negative Value if condition
	field.value = parseLeadingZeros(field.value);
	field.value = roundOff(field.value,pre);
	if (negativeValue == "-"){
		amount = field.value;
		field.value = "-" + amount;
	}
}//end of formatMoney Method

var zeroCharCode = ("0").charCodeAt(0);
var nineCharCode = ("9").charCodeAt(0);
var periodCharCode = (".").charCodeAt(0);
var minusCharCode = ("-").charCodeAt(0);
var plusCharCode = ("+").charCodeAt(0);
var slashCharCode = ("/").charCodeAt(0);

var quotesCharCode = ("\"").charCodeAt(0);
var singleQuoteCharCode = ("\'").charCodeAt(0);
var atCharCode = ("@").charCodeAt(0);
var backSlashCharCode = ("\\").charCodeAt(0);
var bracketSCharCode = ("(").charCodeAt(0);
var bracketECharCode = (")").charCodeAt(0);
var underScoreCharCode = ("_").charCodeAt(0);
var spaceCharCode = (" ").charCodeAt(0);

var aCharCode = ("a").charCodeAt(0);
var zCharCode = ("z").charCodeAt(0);
var ACharCode = ("A").charCodeAt(0);
var ZCharCode = ("Z").charCodeAt(0);

function isAlphaNumericCharCode(code) {
	return ((code >= zeroCharCode && code <= nineCharCode) || (code >= aCharCode && code <= zCharCode) || (code >= ACharCode && code <= ZCharCode)
	|| 
	code == 46 ||   /*delete */
	code == 8 || 	/*back */
	code == 36 ||	/*home */
	code == 35 || 	/*end */
	code == 37 || 	/* left arrow */
	code == 39 || 	/* right arrow  */
	code == 38 || 	/* up arrow */
	code == 40 ||   /* down arrow */
	code == 9  ||	/* tab */	
	code == 13 ||   /* enter */
	code == 16 ||   /* shift */
	code == 17 ||   /* ctrl */
	code == 18    /* alt */
	);
}

function isAlphaNumericCharCodeForUserName(code) {
	return ((code >= zeroCharCode && code <= nineCharCode) || (code >= aCharCode && code <= zCharCode) || (code >= ACharCode && code <= ZCharCode)
	|| 
	code == 46 ||   /*delete */
	code == 8 || 	/*back */
	code == 36 ||	/*home */
	code == 35 || 	/*end */
	code == 37 || 	/* left arrow */
	code == 39 || 	/* right arrow  */
	code == 38 || 	/* up arrow */
	code == 40 ||   /* down arrow */
	code == 9  ||	/* tab */	
	code == 13 ||   /* enter */
	code == 16 ||   /* shift */
	code == 17 ||   /* ctrl */
	code == 18 ||   /* alt */
	code == underScoreCharCode
	);
}
function isAlphaNumericCharCodeForAH(code) {
	return ((code >= zeroCharCode && code <= nineCharCode) || (code >= aCharCode && code <= zCharCode) || (code >= ACharCode && code <= ZCharCode)
	|| code == underScoreCharCode || code == minusCharCode || code == spaceCharCode ||
	code == 46 ||   /*delete */
	code == 8 || 	/*back */
	code == 36 ||	/*home */
	code == 35 || 	/*end */
	code == 37 || 	/* left arrow */
	code == 39 || 	/* right arrow  */
	code == 38 || 	/* up arrow */
	code == 40 ||   /* down arrow */
	code == 9  ||	/* tab */	
	code == 13 ||   /* enter */
	code == 16 ||   /* shift */
	code == 17 ||   /* ctrl */
	code == 18    /* alt */
	
	
	);
}
function isAlphaNumericCharCodeWithSpaces(code) {
	return ((code >= zeroCharCode && code <= nineCharCode) || (code >= aCharCode && code <= zCharCode) || (code >= ACharCode && code <= ZCharCode)
	|| code == spaceCharCode ||
	code == 46 ||   /*delete */
	code == 8 || 	/*back */
	code == 36 ||	/*home */
	code == 35 || 	/*end */
	code == 37 || 	/* left arrow */
	code == 39 || 	/* right arrow  */
	code == 38 || 	/* up arrow */
	code == 40 ||   /* down arrow */
	code == 9  ||	/* tab */	
	code == 13 ||   /* enter */
	code == 16 ||   /* shift */
	code == 17 ||   /* ctrl */
	code == 18 	    /* alt */
	);
}

function isNumericCharCode(code) {
	return ((code >= zeroCharCode && code <= nineCharCode)
	|| 
	code == 46 ||   /*delete */
	code == 8 || 	/*back */
	code == 36 ||	/*home */
	code == 35 || 	/*end */
	code == 37 || 	/* left arrow */
	code == 39 || 	/* right arrow  */
	code == 38 || 	/* up arrow */
	code == 40 ||   /* down arrow */
	code == 9  ||	/* tab */	
	code == 13 ||   /* enter */
	code == 16 ||   /* shift */
	code == 17 ||   /* ctrl */
	code == 18 	    /* alt */
	);
}

function isNumericCharCodeNew(code) {
	return (code >= zeroCharCode && code <= nineCharCode);
}



function isAlphaCharCode(code) {
	return ( (code >= aCharCode && code <= zCharCode) || (code >= ACharCode && code <= ZCharCode)
	||
	code == 46 ||   /*delete */
	code == 8 || 	/*back */
	code == 36 ||	/*home */
	code == 35 || 	/*end */
	code == 37 || 	/* left arrow */
	code == 39 || 	/* right arrow  */
	code == 38 || 	/* up arrow */
	code == 40 ||   /* down arrow */
	code == 9  ||   /* tab */	
	code == 13 ||   /* enter */
	code == 16 ||   /* shift */
	code == 17 ||   /* ctrl */
	code == 18 	    /* alt */
	 );
}

function isAlphaCharCodeWithSpaces(code) {
	return ( (code >= aCharCode && code <= zCharCode) || (code >= ACharCode && code <= ZCharCode)
	|| code == spaceCharCode ||
	code == 46 ||   /*delete */
	code == 8 || 	/*back */
	code == 36 ||	/*home */
	code == 35 || 	/*end */
	code == 37 || 	/* left arrow */
	code == 39 || 	/* right arrow  */
	code == 38 || 	/* up arrow */
	code == 40 ||   /* down arrow */
	code == 9  ||   /* tab */	
	code == 13 ||   /* enter */
	code == 16 ||   /* shift */
	code == 17 ||   /* ctrl */
	code == 18 	    /* alt */
	 );
}

function isCommonCharCode(code) {
	return ( (code >= aCharCode && code <= zCharCode) || (code >= ACharCode && code <= ZCharCode) ||
	(code >= zeroCharCode && code <= nineCharCode) || code == periodCharCode || code == minusCharCode ||
	code == plusCharCode || code == slashCharCode || code == quotesCharCode || code == singleQuoteCharCode ||
	code == atCharCode || code == backSlashCharCode || code == bracketSCharCode || code == bracketECharCode ||
	code == underScoreCharCode || code == spaceCharCode 
	|| 
	code == 46 ||   /*delete */
	code == 8 || 	/*back */
	code == 36 ||	/*home */
	code == 35 || 	/*end */
	code == 37 || 	/* left arrow */
	code == 39 || 	/* right arrow  */
	code == 38 || 	/* up arrow */
	code == 40 ||   /* down arrow */
	code == 9  ||   /* tab */	
	code == 13 ||   /* enter */
	code == 16 ||   /* shift */
	code == 17 ||   /* ctrl */
	code == 18 ||   /* alt */
	code == 44      /* comma */
	);
}
function isCommonCharCodeNotificationMessage(code) {
	return ( (code >= aCharCode && code <= zCharCode) || (code >= ACharCode && code <= ZCharCode) ||
	(code >= zeroCharCode && code <= nineCharCode) || code == periodCharCode || code == minusCharCode ||
	code == plusCharCode || code == slashCharCode || code == quotesCharCode || code == singleQuoteCharCode ||
	code == atCharCode || code == backSlashCharCode || code == bracketSCharCode || code == bracketECharCode ||
	code == underScoreCharCode || code == spaceCharCode 
	|| 
	code == 46 ||   /*delete */
	code == 8 || 	/*back */
	code == 36 ||	/*home */
	code == 35 || 	/*end */
	code == 37 || 	/* left arrow */
	code == 39 || 	/* right arrow  */
	code == 38 || 	/* up arrow */
	code == 40 ||   /* down arrow */
	code == 9  ||   /* tab */	
	code == 13 ||   /* enter */
	code == 16 ||   /* shift */
	code == 17 || 	  /* ctrl */
	code == 63 || 
	code == 58 ||
	code == 18 	    /* alt */
	);
}

function checkNumeric(field,e) {
		var code = e.keyCode;
		if (code == periodCharCode)
			return (field.value.indexOf(".") == "-1");

		if ((code == minusCharCode) || (code == plusCharCode)) {
			if ((field.value.indexOf("-") != "-1") ||
				(field.value.indexOf("+") != "-1")) {
				return false;
			}
		}

		return isNumericCharCodeNew(code)
				|| code == minusCharCode
				|| code == plusCharCode;
}//end of checkNumeric Method



function checkCurrency(field, numericLength) {

		var code = event.keyCode;
		if (code == periodCharCode)
			return (field.value.indexOf(".") == "-1");

		if (code == minusCharCode ) {
			if (field.value.indexOf("-") != "-1") {
				return false;
			} else {
				if (field.value.length > 0){
					return false;
				} else {
					return true;
					}
			}
		}

		if (field.value.indexOf("-") != "-1") {
			numericLength = numericLength + 1;
		}

		if( isNumericCharCode(code) ) {
			if(field.value.length >=  numericLength){
				return (field.value.indexOf(".") != "-1");
			}
			return true;
		} else {
			return false;
		}

}//end of CheckCurrency Method

function checkWholeNumber(field, numericLength) {
		var code = event.keyCode;
		if (code == minusCharCode ) {
			if (field.value.indexOf("-") != "-1") {
				return false;
			} else {
				if (field.value.length > 0){
					return false;
				} else {
					return true;
					}
			}
		}

		if (field.value.indexOf("-") != "-1") {
				numericLength = numericLength + 1;
		}

		if( isNumericCharCode(code) ) {
			if(field.value.length >=  numericLength){
				return false;
			}
			return true;
		} else {
			return false;
		}
}//end of checkWholeNumber method

function stripCurrency(curr){
	var reFloat = /,/;
	var find = curr.search(reFloat);
	while(find != -1){
		curr = curr.replace(reFloat,"");
		find = curr.search(reFloat);

	}
	return new Number(curr);
}

function parseCurrency(field){
	field.value = stripCurrency(field.value);
	field.select();
}

function insertCommas(field){

	commasInsertion(field, true);
}

function insertCommasWhole(field){
	commasInsertion(field, false);
}

function parseLeadingZeros(strVar)
{
	while(strVar.charAt(0)=='0')
		strVar=strVar.substring(1,strVar.length);
	if ( strVar == "")
		strVar = "0";
	else if ( new Number(strVar) < 1 )
		strVar = "0" + strVar;
	return strVar;
}


function commasInsertion(field, deciFlag){

	var whole = "";
	var fraction = "";
	var negativeValue = "";
	var amount = field.value;

	negativeValue = amount.substr(0,1);


	if (negativeValue == "-" && amount.length > 1){
		field.value = amount.substr(1,amount.length-1);
	}//end negative Value if condition



	field.value = parseLeadingZeros(field.value);
	amount = field.value;

	for ( var i = 0 ; i < amount.length; i++) {
	  	if (amount.substr(i,1) == ','){
		continue;
		}
		else if (amount.substr(i,1) == '.'){
			fraction = amount.substr(i+1, amount.length - i );
			break;
		}
		else{
  			whole = whole + amount.substr(i,1);
		}
	}
	amount = "";
	for ( var j = whole.length; j > 0; j--){
		if ( ( whole.length - j)%3 == 0  && j != whole.length){
			amount =  "," + amount ;
		}
		amount =  whole.substr(j-1,1) + amount;
	}
	if(amount == "") {
		amount = "0";
	}
	if ( fraction != "" ) {
		field.value = amount + "." + fraction;
	}
	else {
		if (deciFlag == true){
			field.value = amount + ".00";
		} else {
			field.value = amount;
		}
	}

	amount = field.value;

	if (negativeValue == "-" && amount.length > 1 && amount != "0"  && amount != "0.00"){
		field.value = "-" + amount;
	}


}//end of commasInsertion Method


function isNumeric(stra) {
	for (var i=0; i< stra.length; i++) {
		var chv = stra.charAt(i);
		if (chv < '0' || chv > '9') return false;
	}
	return true;
}



function isValidDateStr(dateStr)
{

	if (dateStr.length == 0) return true;

	if (dateStr.length < 10) return false;
	// Checks for the following valid date formats:
	// MM/DD/YY   MM/DD/YYYY   MM-DD-YY   MM-DD-YYYY
	// Also separates date into month, day, and year variables

	// to require a 2 OR 4 digit year use the following:
	//var datePat = /^(\d{1,2})(\/|-)(\d{1,2})\2(\d{2}|\d{4})$/;

	// To require a 4 digit year entry, use this line instead:
	var datePat = /^(\d{2})(\/|-)(\d{2})\2(\d{4})$/;

	var matchArray = dateStr.match(datePat); // is the format ok?
	if (matchArray == null)
	{
		var datePat2 = /^(\d{8})$/;
		matchArray = dateStr.match(datePat2);
		if (matchArray == null)
		{
			return "Date is not in a valid format."
		}
		else
		{
			year = matchArray[1].substr(0,4);
			if (matchArray[1].substr(0,4) >= 1969)
			{
				year = matchArray[1].substr(0,4);
				month = matchArray[1].substr(4,2);
				day = matchArray[1].substr(6,2);
			}
			else if (matchArray[1].substr(4,4) >= 1969)
			{
				year = matchArray[1].substr(4,4);
				month = matchArray[1].substr(0,2);
				day = matchArray[1].substr(2,2);
			}
			else
			{
				return "Year must be after 1969";
			}
		}
	}
	else
	{
		month = matchArray[1]; // parse date into variables
		day = matchArray[3];
		year = matchArray[4];
	}
	if (month < 1 || month > 12)
	{ // check month range
		return "Month must be between 1 and 12.";
	}
	if (day < 1 || day > 31)
	{
		return "Day must be between 1 and 31.";
	}
	if ((month==4 || month==6 || month==9 || month==11) && day==31)
	{
		return "Month "+month+" doesn't have 31 days!";
	}
	if (month == 2)
	{ // check for february 29th
		var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
		if (day>29 || (day==29 && !isleap))
		{
			return "February " + year + " doesn't have " + day + " days!";
		}
	}
	return null;
}

//format mm/yyyyy*
function maskdate(obj,e) {
//	var code = event.keyCode;
	var code = e.charCode ? e.charCode : e.keyCode;
	
	if (!isNumericCharCode(code) && code != slashCharCode) {

		return false;

	} else if (obj.value.length > 0) {
		var val = obj.value;
			if (!isNumeric(val.substring(val.length, val.length-1)))
			{
				obj.value = val.substring(0, val.length-1);
			}

       		if (obj.value.length == 2 ) {

           		var lasttwochar = obj.value.substring(obj.value.length,obj.value.length-2);

           		if  (!isNumeric(lasttwochar)) {
              			obj.value = obj.value.substring(0,obj.value.length-2);
           		} else {
              			obj.value = obj.value +"/";
           		}

       		}
   	}
   	return true;

}


//format dd/mm/yyyy* mm/dd/yyyyy*
function maskdate2(obj) {
	var code = event.keyCode;
	if (!isNumericCharCode(code) && code != slashCharCode) {

		return false;

	} else if (obj.value.length > 0) {
		var val = obj.value;
			if (!isNumeric(val.substring(val.length, val.length-1)))
			{
				obj.value = val.substring(0, val.length-1);
			}

       		if (obj.value.length == 2 ||  obj.value.length == 5) {
          
           		var lasttwochar = obj.value.substring(obj.value.length,obj.value.length-2);
                            
           		if  (!isNumeric(lasttwochar)) {
              			obj.value = obj.value.substring(0,obj.value.length-2);
           		} else {
              			obj.value = obj.value +"/"; 
           		}
           
       		}    
   	} 
   	return true;

}




	function textAreaLengthCounter(field, maxlimit) {
		if (field.value.length > maxlimit){ // if too long...trim it!
			alert('You cannot enter more than '+maxlimit+' characters');	
			field.value = field.value.substring(0, maxlimit);
			field.focus();			
 	    }
	}

function disablePasteOption(e) {
	var code = (document.all) ? event.keyCode:e.which;
	if (parseInt(code) == 17 && parseInt(code) === 86)
	{
		return false;
   	}
   	return true;	
}	

	
function maskNumber(obj, e) {
	var code = e.charCode ? e.charCode : e.keyCode;
	
	if (!isNumericCharCode(code)||  code == 38 || code == 35 || code == 36 || code == 37  || code == 39 || code == 40 ) {
		return false;
   	}
   	return true;
}
function maskInteger(obj, e) {
	var code = e.charCode ? e.charCode : e.keyCode;
	if (!isNumericCharCode(code)||  code == 38 || code == 35 || code == 36 || code == 37  || code == 39 || code == 40|| code == 46) {
		return false;
   	}
   	return true;
}

function maskAlpha(obj, e) {
	var code = e.charCode ? e.charCode : e.keyCode;
	
	if (!isAlphaCharCode(code) ||  code == 38 || code == 35 || code == 36 || code == 37 || code == 39 || code == 40 || code == 46) {
		return false;
   	}
   	return true;
}

function maskAlphaWithSp(obj, e) {
	var code = e.charCode ? e.charCode : e.keyCode;
	if (!isAlphaCharCodeWithSpaces(code) ||  code == 38 || code == 35 || code == 36 || code == 37 || code == 39 || code == 40 || code == 46) {
		return false;
   	}
   	return true;
	
}

function maskAlphaNumeric(obj, e) {
	var code = e.charCode ? e.charCode : e.keyCode;
	if (!isAlphaNumericCharCode(code) ||  code == 38 || code == 35 || code == 36 || code == 37 || code == 39 || code == 40 || code == 46) {
		return false;
   	}
   	return true;
	
}

function maskAlphaNumericWithSp(obj, e) {
	var code = e.charCode ? e.charCode : e.keyCode;
	if (!isAlphaNumericCharCodeWithSpaces(code) ||  code == 38 || code == 35 || code == 36 || code == 37 || code == 39 || code == 40 || code == 46) {
		return false;
   	}
   	return true;

}
function maskAlphaNumericWithSpdashforAH(obj, e) {
	var code = e.charCode ? e.charCode : e.keyCode;
	if (!isAlphaNumericCharCodeForAH(code) ||  code == 38 || code == 35 || code == 36 || code == 37 || code == 39 || code == 40 ) {
		return false;
   	}
   	return true;

}
function maskAlphaNumericForUserName(obj, e) {
	/*var minusCharCode = ("-").charCodeAt(0);
	var underScoreCharCode = ("_").charCodeAt(0);*/
	
	var code = e.charCode ? e.charCode : e.keyCode;
	if (!isAlphaNumericCharCodeForUserName(code) ||  code == 38 || code == 35 || code == 36 || code == 37 || code == 39 || code == 40) {
		return false;
   	}
   	return true;

}

function maskAlphaNumericWithPeriodForUserName(obj, e) {	
	var code = e.charCode ? e.charCode : e.keyCode;
	if ((!isAlphaNumericCharCodeForUserName(code) ||  code == 38 || code == 35 || code == 36 || code == 37 || code == 39 || code == 40)  && ((code!= minusCharCode) && (code!=underScoreCharCode) && (code != periodCharCode))) {
		return false;
   	}
   	return true;

}
function maskCommon(obj, e) {
	var code = e.charCode ? e.charCode : e.keyCode;
	
	if (!isCommonCharCode(code) || code == 38 || code == 34 ) {
		return false;
  	}
   	return true;
}

function maskCommonNotificaitonMessage(obj, e) {
	var code = e.charCode ? e.charCode : e.keyCode;

	
	if (!isCommonCharCodeNotificationMessage(code) || code == 38 || code == 34 ) {
		return false;
  	}
   	return true;
}

 function validateFormChar(_form){
 	//var _Elements = Form.getInputs($(_form),'text');
 	var _Elements = _form.elements;
 	 	
 	for(i=0;i<_form.elements.length;i++){ 	 	
 	if(_Elements[i].type == "text"){ 	// if coming element is a text element 	
 		if(validateCommonChar(_Elements[i])){
 			alert('Special Characters \" < > & ^ are not allowed here.');
 			_Elements[i].focus();
 			return false;
 		}
 		}
 	}

	_Elements = document.getElementsByTagName('textarea');
 	for(i=0;i<_Elements.length;i++){
 		if(validateCommonChar(_Elements[i])){
 			alert('Special Characters \" < > & ^ are not allowed here.');
 			_Elements[i].focus();
 			return false;
 		}
 	}
 	
 	return true;
 }

function validateCommonChar(_Field){
var checkOK = "^<>&\"";
  
  var checkStr = _Field.value;	 
	  if(checkStr.length > 0) {
	    for (index = 0;  index < checkStr.length;  index++){
		  var found = false;
		  ch = checkStr.charAt(index);
		  for (j = 0;  j < checkOK.length;  j++){
			if (ch == checkOK.charAt(j)){
			    return found = true;

			}
		}//end of loop with j
	   }
	}	
			
	  return found;
}

function validateAlphaWithSp(_Field,lable){
	  var checkStr = _Field.value;	 
		if(checkStr.length > 0) {
			for (var index = 0;  index < checkStr.length;  index++){
				var code = checkStr.charAt(index).charCodeAt(0);
			  if (!isAlphaCharCodeWithSpaces(code) ||  code == 38 || code == 35 || code == 36 || code == 37 || code == 39 || code == 40 || code == 46) 
			  {
				  alert("Kindly provide valid input for "+lable);	
				  _Field.focus();
				  return false;
			  }
			 
			}
		    return true;
		}				
		else
		{
			return true;
		}
	}


function validateTextAreaCommonChar(_Field){
var checkOK = "^<>&\"";
  
  var checkStr = _Field.value;	 
	  if(checkStr.length > 0) {
	    for (index = 0;  index < checkStr.length;  index++){
		  var found = false;
		  ch = checkStr.charAt(index);
		  for (j = 0;  j < checkOK.length;  j++){
			if (ch == checkOK.charAt(j)){
			    return found = true;

			}
		}//end of loop with j
	   }
	}	
			
	  return found;
}


function chkdate1(objName) {


//var strDatestyle = "US"; //United States date style
var strDatestyle = "EU";  //European date style
var strDate;
var strDateArray;
var strDay;
var strMonth;
var strYear;
var intday;
var intMonth;
var intYear;
var booFound = false;
var datefield = objName;
var strSeparatorArray = new Array("-"," ","/",".");
var intElementNr;
var err = 0;
var strMonthArray = new Array(12);
strMonthArray[0] = "Jan";
strMonthArray[1] = "Feb";
strMonthArray[2] = "Mar";
strMonthArray[3] = "Apr";
strMonthArray[4] = "May";
strMonthArray[5] = "Jun";
strMonthArray[6] = "Jul";
strMonthArray[7] = "Aug";
strMonthArray[8] = "Sep";
strMonthArray[9] = "Oct";
strMonthArray[10] = "Nov";
strMonthArray[11] = "Dec";
strDate = datefield.value;
if (strDate.length < 1) {
return true;
}
for (intElementNr = 0; intElementNr < strSeparatorArray.length; intElementNr++) {
if (strDate.indexOf(strSeparatorArray[intElementNr]) != -1) {
strDateArray = strDate.split(strSeparatorArray[intElementNr]);
if (strDateArray.length != 3) {
err = 1;
return false;
}
else {
strDay = strDateArray[0];
strMonth = strDateArray[1];
strYear = strDateArray[2];
}
booFound = true;
   }
}
if (booFound == false) {
if (strDate.length>5) {
strDay = strDate.substr(0, 2);
strMonth = strDate.substr(2, 2);
strYear = strDate.substr(4);
   }
}
if (strYear.length == 2) {
strYear = '20' + strYear;
}
// US style
if (strDatestyle == "US") {
strTemp = strDay;
strDay = strMonth;
strMonth = strTemp;
}
intday = parseInt(strDay, 10);
if (isNaN(intday)) {
err = 2;
return false;
}
intMonth = parseInt(strMonth, 10);
if (isNaN(intMonth)) {
for (i = 0;i<12;i++) {
if (strMonth.toUpperCase() == strMonthArray[i].toUpperCase()) {
intMonth = i+1;
strMonth = strMonthArray[i];
i = 12;
   }
}
if (isNaN(intMonth)) {
err = 3;
return false;
   }
}
intYear = parseInt(strYear, 10);


if (isNaN(intYear)) {
err = 4;
return false;
}
if (intMonth>12 || intMonth<1) {
err = 5;
return false;
}
if ((intMonth == 1 || intMonth == 3 || intMonth == 5 || intMonth == 7 || intMonth == 8 || intMonth == 10 || intMonth == 12) && (intday > 31 || intday < 1)) {
err = 6;
return false;
}
if ((intMonth == 4 || intMonth == 6 || intMonth == 9 || intMonth == 11) && (intday > 30 || intday < 1)) {
err = 7;
return false;
}
if (intMonth == 2) {
if (intday < 1) {
err = 8;
return false;
}
if (LeapYear(intYear) == true) {
if (intday > 29) {
err = 9;
return false;
}
}
else {
if (intday > 28) {
err = 10;
return false;
}
}
}
//if (strDatestyle == "US") {
//datefield.value = strMonthArray[intMonth-1] + " " + intday+" " + strYear;
//}
//else {
//datefield.value = intday + " " + strMonthArray[intMonth-1] + " " + strYear;
//}

return true;
}

function LeapYear(intYear) {
if (intYear % 100 == 0) {
if (intYear % 400 == 0) { return true; }
}
else {
if ((intYear % 4) == 0) { return true; }
}
return false;
}

function isEmail(toCheck) {
	toCheck.value = trim(toCheck.value);
	var reEmail = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/igm;
	return reEmail.test(toCheck.value);
}

function IsNumericData(sText, label) 
{
   var ValidChars = "0123456789";
   var IsNumber=true;
   var Char;

   if(sText.length > 0) {
	   for (var i = 0; i < sText.length && IsNumber == true; i++) 
	      { 
	      Char = sText.charAt(i); 
	      if (ValidChars.indexOf(Char) == -1) {
	      	 alert(label+' is not a valid number');
	         IsNumber = false;
	      }
	   }
   }
   else
   {
   	IsNumber = true;
   }
   return IsNumber;
}


	function isNumber(value) {
		    
		var charpos = value.search("[^0-9]"); 
		
  		if(charpos >= 0) { 
  			
  			return false; 
  		} 
  		
  		return true;
  	}

	function maskAlphaNumericWithSpDtHyphen(obj, e) {
		var code = e.charCode ? e.charCode : e.keyCode;
		if (!isAlphaNumericCharCodeWithSpaces(code) ||  code == 38 || code == 35 || code == 36 || code == 37 || code == 39 || code == 40 ) {
			return false;
	   	}
	   	return true;

	}

	function isValidPicOrPdf(fileName, label)
    {
  	  if(!fileName.value.endsWith('.jpg') && !fileName.value.endsWith('.JPG')
  			  && !fileName.value.endsWith('.png') && !fileName.value.endsWith('.PNG')
  			  && !fileName.value.endsWith('.pdf') && !fileName.value.endsWith('.PDF'))
 		  {
  		  alert(label + ' must be a jpg, png or pdf file.');
  		  return false;
 		  }
  	  return true;
    }