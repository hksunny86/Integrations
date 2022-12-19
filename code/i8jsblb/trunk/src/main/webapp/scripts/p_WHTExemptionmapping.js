var jq = $.noConflict();

jq(document).ready(function($) {

    $("#loadButton").click(function (event) {
        loadWhtExemptionMapping();
    });

});
function loadWhtExemptionMapping()
{
    resetForm();
        var params = {
            userId : jq('#userId').val(),
            currentTime : jq.now() //To avoid caching by browser particularly IE
        };

        jq.getJSON( 'p_loadWhtExemptionmapping.html', params , function( whtExemptionVOList ) {

            jq(whtExemptionVOList ).each( function(idx)
            {
                var rowObj = whtExemptionVOList[idx];

                var idPrefix = "whtExemptionVOList"+idx+"_";
                var namePrefix = "whtExemptionVOList["+idx+"].";
                var templateRow = jq('#templateRow').clone().prop('id',"row_"+idx);
                templateRow.find('td:first').html(idx+1);
                templateRow.find(' td:nth-child(2)').html(this.userId);
                templateRow.find(' td:nth-child(3)').html(this.agentName);
                templateRow.find(' td:nth-child(4)').html(this.mobile);
                templateRow.find(' td:nth-child(5)').html(this.agentCnic);
                templateRow.find(' td:nth-child(6)').html(this.startDateStr);
                templateRow.find(' td:nth-child(7)').html(this.endDateStr);
                templateRow.find(' td:nth-child(9)').html(this.editButton);

                var agentId = idPrefix+templateRow.find('#appUserId').prop('id');
                var agentIdName = namePrefix+templateRow.find('#appUserId').prop('name');
                templateRow.find('#appUserId').prop('id',agentId).prop('name',agentIdName).val(this.agentId);

                var nameId = idPrefix+templateRow.find('#name').prop('id');
                var name = namePrefix+templateRow.find('#name').prop('name');
                templateRow.find('#name').prop('id',nameId).prop('name',name).val(this.agentName);

                var mobileId = idPrefix+templateRow.find('#mobile').prop('id');
                var mobile = namePrefix+templateRow.find('#mobile').prop('name');
                templateRow.find('#mobile').prop('id',mobileId).prop('name',mobile).val(this.mobileId);

                var cnicId = idPrefix+templateRow.find('#cnic').prop('id');
                var cnic = namePrefix+templateRow.find('#cnic').prop('name');
                templateRow.find('#cnic').prop('id',cnicId).prop('name',cnic).val(this.agentCnic);

                var exemptionStartId = "exemptionStateDate";//idPrefix+templateRow.find('#exemptionStateDate').prop('id');
                var exemptionStart = namePrefix+templateRow.find('#exemptionStateDate').prop('name');
                templateRow.find('#exemptionStateDate').prop('id',exemptionStartId).prop('name',exemptionStart).val(this.startDateStr);

                var exemptionEndId = idPrefix+templateRow.find('#exemptionEndDate').prop('id');
                var exemptionEnd = namePrefix+templateRow.find('#exemptionEndDate').prop('name');
                templateRow.find('#exemptionEndDate').prop('id',exemptionEndId).prop('name',exemptionEnd).val(this.endDateStr);

                var editButtonId = this.whtExemptionId;
                var editButton = namePrefix + templateRow.find('#editButton').prop('name');

                if(this.editAllowed) {
                    templateRow.find('#editButton').prop('id', editButtonId).prop('name', editButton).prop("disabled", false);
                }else{
                    templateRow.find('#editButton').prop('id', editButtonId).prop('name', editButton).prop("disabled", true);
                }

                jq('#p_loadWhtExemptionmapping tbody').append(templateRow);
            });
            resetRowClass();
        });
}

function resetRowClass()
{
    resetOddEvenRowClass();
    jq('.tableRegion tbody tr').mouseover(function()
    {
        jq(this).removeClass().addClass('highlight');
    });
    jq('.tableRegion tbody tr').mouseout(function(event)
    {
        resetOddEvenRowClass();
    });
}

function resetForm()
{
    jq('#p_loadWhtExemptionmapping tbody').html('');
}

function resetOddEvenRowClass()
{
    jq('.tableRegion tbody tr').filter(':odd').removeClass().addClass('odd');
    jq('.tableRegion tbody tr').filter(':even').removeClass().addClass('even');
}

    function editWhtRule(obj){

        document.getElementById("startDate").readOnly = false;
        document.getElementById("endDate").readOnly = false;
        document.getElementById("startDate").value = jq(obj).closest("tr").find('td:nth-child(6)').text();
        document.getElementById("endDate").value = jq(obj).closest("tr").find('td:nth-child(7)').text();
        document.getElementById("startDate").readOnly = true;
        document.getElementById("endDate").readOnly = true;
        document.getElementById("whtExemptionId").value =obj.id;
    }

    function saveAllowedOrNot(){
        var name=document.getElementById("agentName").value;
        var cnic= document.getElementById("agentCnic").value;
        var errorString ="Retailer Does Not Exist";

        if(name==errorString && cnic==errorString){
            document.getElementById("submitBtn").disabled=true;
        }else{
            document.getElementById("submitBtn").disabled=false;
        }
    }