<%@ page import='com.inov8.microbank.common.util.*'%>
	

	<script language="javascript">
				
	jq(document).ready(function(jq){
	    
	    var totalRows = ${totalRows};
	    
	    var minRowsZipExport = "<%=MessageUtil.getMessage("export.minRows")%>";
	    
	    var xlsPermission = false;
	    var xlsxPermission = false;
	    var csvPermission = false;
	    var pdfPermission = false;
	    
	    
	    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
	    	xlsPermission = true;
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			xlsxPermission = true;
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			csvPermission = true;
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			pdfPermission = true;
		</authz:authorize>
		
		
		var hasRows = jq("#ec_table > tbody > tr").filter(":not(.calcRow)").length > 0;
	   
	    
	    if(hasRows)
	    {
	        var csvViewAllowed = jq('img[alt=CSV]').length > 0;
	        var xlsViewAllowed = jq('img[alt=XLS]').length > 0;
	        var xlsxViewAllowed = jq('img[alt=XLSX]').length > 0;

	        if(csvPermission && (totalRows>=minRowsZipExport))
	        {
	        	jq('img[alt=CSV]').remove();
	        	
	        	var exportZipCsvHtml = '<td><img src="/i8Microbank/images/table/zip_csv.gif" style="border:0;cursor:pointer;" title="Export CSV ZIP" alt="CSV ZIP" onclick="exportZip(\'csv\');"></td>';
	            jq('table.toolbar > tbody > tr > td').filter(':last').after(exportZipCsvHtml);
	        }

	        if(xlsPermission && (totalRows>=minRowsZipExport))
	        {
	        	jq('img[alt=XLS]').remove();
	        	
	        	if(totalRows<=65000){
	        		var exportZipXlsHtml = '<td><img src="/i8Microbank/images/table/zip_xls.gif" style="border:0;cursor:pointer;" title="Export CSV ZIP" alt="CSV ZIP" onclick="exportZip(\'xls\');"></td>';
		            jq('table.toolbar > tbody > tr > td').filter(':last').after(exportZipXlsHtml);
	        	}
	        	
	        }

	        if(xlsxPermission && (totalRows>=minRowsZipExport))
	        {	
	        	jq('img[alt=XLSX]').remove();
	            var exportZipXlsxHtml = '<td><img src="/i8Microbank/images/table/zip_xlsx.gif" style="border:0;cursor:pointer;" title="Export CSV ZIP" alt="CSV ZIP" onclick="exportZip(\'xlsx\');"></td>';
	            jq('table.toolbar > tbody > tr > td').filter(':last').after(exportZipXlsxHtml);
	        }
	        
	        if(pdfPermission && (totalRows>=minRowsZipExport))
	        {	
	        	jq('img[alt=PDF]').remove();
	           
	        }
	    }
	});

	function exportZip(view) {
	    jq('#downloadLinkBlock').remove();
	    var params = {
	        view: view,
	        username:"<%=UserUtils.getCurrentUser().getUsername()%>",
	        email:"<%=UserUtils.getCurrentUser().getEmail()%>",
	        appUserId: "<%=UserUtils.getCurrentUser().getAppUserId()%>",
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
	        	
	        	var hasRows = jq("#ec_table > tbody > tr").filter(":not(.calcRow)").length;
	        	if(view =='XLS' && hasRows>65000){
	                alert("XLS export can be used for 65000 records.");
	                return false;
	        	}
	        	else
	        	{
	                alert("Export request is submitted.\nZip file will be available to download on Download Zip Export Page");

	        	}
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
	</script>	
	
		