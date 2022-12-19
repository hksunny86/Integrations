var jq = $.noConflict();
jq(document).ready(function() {
    jq('#cnicNo').focus();
    // Set headers title and put check box with it
    var blacklistedHeader = jq('#ec_table thead').find('td.blacklistedHeader');
    blacklistedHeader.html(blacklistedHeader.html() + '<br/>  <input align="left" type="checkbox" id="blacklistedHeader" onchange="setAllBlacklisted(this);"/>');
    setHeaderBlacklisted(); //To set header select box according to received body

    jq(':checkbox').click( // set value of check boxes on click
        function(event) {
            jq(this).prop('checked', this.checked);
            jq(this).prop('value', this.checked);
        });

    jq("#ec_table .tableBody tr").each(function(index) {
        jq(this).find("input[id$='cnicNo'],input[id$='blacklisted']").each(function() {
            var name = jq(this).attr('name');
            var listName = name.substr(0, name.indexOf("["));
            var propertyName = name.substr(name.indexOf("]") + 2);
            name = listName + "[" + index + "]." + propertyName;
            var id = listName + index + "." + propertyName;
            jq(this).attr('id', id);
            jq(this).attr('name', name);
        });
    });


    jq('.blacklisted').click(function(event) {
        if (!this.checked) {

            jq('#blacklistedHeader').prop('checked', false);// de-select
        }
        setHeaderBlacklisted();

    });

    function setHeaderBlacklisted() {

        var totalRows = jq('.blacklisted').length;
        jq('.blacklisted').each(function(index) {
            if (this.checked) {
                if (index == (totalRows - 1)) {
                    jq('#blacklistedHeader').prop('checked', true);
                }
            } else {
                return false;
            }
        });
               
    }


});


function setAllBlacklisted(source) {

    checkboxes = jq('.blacklisted');
    for (var i = 0, n = checkboxes.length; i < n; i++) {
        checkboxes[i].checked = source.checked;
        checkboxes[i].value = true;
    }
    ;
   
}
function setActionId(source, cnicList) {
    var regStateId=jq('#regStateId').val();
    var cnic=jq('#cnic').val();
    //var cnicList = jq('#changedCnicList').val();
    var userId=jq('#userId').val();
    var appUserTypeId=jq('#appUserTypeId').val();
    var a = jq('#ec').attr('action');
    var url = a.substr(0, a.indexOf("="));
 
    
    if(cnicList == '[object HTMLCollection]'){
    	cnicList = '';
    }
   
    jq('#ec').attr('action', url + "=" + ACTION_UPDATE+"&regStateId="+regStateId+"&cnic="+cnic+"&cnicList="+cnicList+"&userId="+userId+"&appUserTypeId="+appUserTypeId);

    document.forms.ec.submit();
}

