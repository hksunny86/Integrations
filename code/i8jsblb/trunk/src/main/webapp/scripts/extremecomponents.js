function getParameterMap(form) {
    var p = document.forms[form].elements;
    var map = new Object();
    for(var x=0; x < p.length; x++) {
        var key = p[x].name;
        var val = p[x].value;
        
        //Check if this field name is unique.
        //If the field name is repeated more than once
        //add it to the current array.
        var curVal = map[key]; 
        if (curVal) { // more than one field so append value to array
        	curVal[curVal.length] = val;
        } else { // add field and value
        	map[key]= [val];
        }
    }
    return map;
}

function setFormAction(form, action, method) {
	if (action) {
		document.forms[form].setAttribute('action', action);
	}
	
	if (method) {
		document.forms[form].setAttribute('method', method);
	}
	
	document.forms[form].ec_eti.value='';
}

function blockSpecialCharacters()
{
var isLegal;
var elems = document.getElementsByTagName("input");

for(i=0;i<elems.length;i++)
{
	if(elems[i].type == 'text' && elems[i].name != 'undefined' && (elems[i].name.substring(0,5) == 'ec_f_') && elems[i].value != '')
	{
	
		isLegal = isLegalCharacter(elems[i].value);
		if(!isLegal)
		{
			alert('Special characters(\',`,~,#,^,&,*,<,>,|,[,]) are not allowed');		
			return isLegal;
		}
		
	}
}
return isLegal;
}

function isLegalCharacter(str)			
{
	return isInValidCharSet(str,"'`~#^&*<>|[]?");
}
function isInValidCharSet(str,charset)
{
	var result = true;
	if(str == '')
	return true;
	
		// Note: doesn't use regular expressions to avoid early Mac browser bugs	
		for (var i=0;i<str.length;i++)
		{
			
			if (charset.indexOf(str.substr(i,1))>=0)
			{
				result = false;
				break;
			}
		}
		
	return result;
}
