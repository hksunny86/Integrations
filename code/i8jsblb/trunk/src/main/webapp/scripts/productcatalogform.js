var jq= $.noConflict();

jq(document).ready(
    function($) {
        loadProductByUserType();
    });

function loadProductByUserType(){

    resetForm();
    var params = {
        appUserTypeId : jq('#appUserTypeId').val(),
        productCatalogId:productId,
        currentTime : jq.now() //To avoid caching by browser particularly IE
    };

    jq.getJSON( 'p_loadProductsByUserType.html', params , function( productModelVOList ) {

            jq(productModelVOList ).each( function(idx)
            {
                var rowObj = productModelVOList[idx];

                var idPrefix = "productModelVOList"+idx+"_";
                var namePrefix = "productModelVOList["+idx+"].";
                var checkBoxNamePrefix="checkBox["+this.productId+"].";
                var productId=this.productId;
                var templateRow = jq('#templateRow').clone().prop('id',"row_"+idx);

                templateRow.find('td:first').prop('checked', false);
                templateRow.find(' td:nth-child(2)').html(this.name);
                templateRow.find(' td:nth-child(3)').html(this.supplierName);
                templateRow.find('td:nth-child(4)').html(this.serviceName);

                var activeProductId = idPrefix+templateRow.find('#activeProduct').prop('id');
                var activeProductName = checkBoxNamePrefix+productId;
                if(this.checked) {
                    templateRow.find('#activeProduct').prop('id', activeProductId).prop('name', activeProductName).prop('checked', true);
                }
                else{
                    templateRow.find('#activeProduct').prop('id', activeProductId).prop('name', activeProductName).prop('checked', false);
                }

                var nameId = idPrefix+templateRow.find('#name').prop('id');
                var name = namePrefix+templateRow.find('#name').prop('name');
                templateRow.find('#name').prop('id',nameId).prop('name',name).val(this.name);

                var supplierNameId = idPrefix+templateRow.find('#supplierName').prop('id');
                var supplierName = namePrefix+templateRow.find('#supplierName').prop('name');
                templateRow.find('#supplierName').prop('id',supplierNameId).prop('name',supplierName).val(this.supplierName);

                var serviceNameId = idPrefix+templateRow.find('#serviceName').prop('id');
                var serviceName = namePrefix+templateRow.find('#serviceName').prop('name');
                templateRow.find('#supplierName').prop('id',serviceNameId).prop('name',serviceName).val(this.serviceName);

                jq('#ec_table tbody').append(templateRow);
            });
        resetRowClass();
        });
}

function resetForm()
{
    jq('#ec_table tbody').html('');
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
function resetOddEvenRowClass()
{
    jq('.tableRegion tbody tr').filter(':odd').removeClass().addClass('odd');
    jq('.tableRegion tbody tr').filter(':even').removeClass().addClass('even');
}

