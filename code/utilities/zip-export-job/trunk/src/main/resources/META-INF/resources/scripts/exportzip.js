/**
 * Created by NaseerUl on 9/6/2016.
 */

jq(document).ready(function(jq){
    var hasRows = jq("#ec_table > tbody > tr").filter(":not(.calcRow)").length > 0;
    if(hasRows)
    {
        var csvViewAllowed;
        var xlsViewAllowed;
        var xlsxViewAllowed;
        
        if( jq('img[alt=CSV]').length == 0){
        	csvViewAllowed = false;
        }
        else{
        	csvViewAllowed = true;
        }
        	  
        if( jq('img[alt=XLS]').length == 0){
        	xlsViewAllowed = false;
        }
        else{
        	xlsViewAllowed = true;
        }
        	
        
       
        if( jq('img[alt=XLSX]').length == 0){
        	xlsxViewAllowed = false;
        }
        else{
        	xlsxViewAllowed = true;
        }
        

        if(!csvViewAllowed)
        {
            var exportZipCsvHtml = '<td><img src="/i8Microbank/images/table/zip_csv.gif" style="border:0;cursor:pointer;" title="Export CSV ZIP" alt="CSV ZIP" onclick="exportZip(\'csv\');"></td>';
            jq('table.toolbar > tbody > tr > td').filter(':last').after(exportZipCsvHtml);
        }

        if(!xlsViewAllowed)
        {
            var exportZipXlsHtml = '<td><img src="/i8Microbank/images/table/zip_xls.gif" style="border:0;cursor:pointer;" title="Export CSV ZIP" alt="CSV ZIP" onclick="exportZip(\'xls\');"></td>';
            jq('table.toolbar > tbody > tr > td').filter(':last').after(exportZipXlsHtml);
        }

        if(!xlsxViewAllowed)
        {
            var exportZipXlsxHtml = '<td><img src="/i8Microbank/images/table/zip_xlsx.gif" style="border:0;cursor:pointer;" title="Export CSV ZIP" alt="CSV ZIP" onclick="exportZip(\'xlsx\');"></td>';
            jq('table.toolbar > tbody > tr > td').filter(':last').after(exportZipXlsxHtml);
        }
    }
});

function exportZip(view) {
    jq('#downloadLinkBlock').remove();
    var params = {
        view: view,
        username:username,
        email:email,
        appUserId: appUserId,
        exportFileName:jq('meta[name=title]').attr('content'),
        reportId:jq('meta[name=title]').attr('id'),
        columnsProps: jq('input[name=ec_cp]').val(),
        propsFormats: jq('input[name=ec_cf]').val(),
        columnsTitles: jq('input[name=ec_ct]').val(),
        escapeCommasColumnsIndexes:jq('input[name=ec_ecci]').val(),
        currentTime: jq.now() //To avoid caching by browser particularly IE
    };

    jq.ajax({
        url: "p_exportzip.html",
        data: params,
        cache: false,
        beforeSend: function( ) {
            jq('#downloadLinkBlock').remove();
            alert("Export request is submitted.\nZip file will be available to download on Download Zip Export Page");
        },
       /* success: function (downloadZipUrl) {
            jq('#downloadLinkBlock').remove();    
            var downloadLinkHtml = "<td id='downloadLinkBlock'><a id='fileLink' href='"+downloadZipUrl+"'>Download</a></td>";
            jq('table.toolbar > tbody > tr > td').filter(':last').after(downloadLinkHtml);
            jq('#downloadLinkHtml').click();
        },*/
        error: function() {
            alert("An unknown error has occurred. Please contact with the administrator for more details");
        },
        async: false
    });
    return false;
}