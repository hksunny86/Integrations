	<p id='loadingArea'>
		<input type="hidden" id="fileURL" name="fileURL" path="fileURL"/>	
		<a hidden="false" href="#" id="fileLink" name="fileLink">Download</a>
		<input type="hidden" id="errorDetails"/>
	</p>

	<script language="javascript">
				
		var cellIndex = 0;
		var zipInProcess = false;
		var totalRecordsCount = 0;
		
		function createCell(_reportType) {
		
			<c:if test="${!empty requestScope.totalRecordsCount && requestScope.totalRecordsCount >0}">
				
				totalRecordsCount = ${requestScope.totalRecordsCount};
				
			</c:if>
						
			if(totalRecordsCount < 1) {
				
				return;
			}
		
			var ec_rd = document.getElementsByName('ec_rd')[0];

			var row = ec_rd.parentElement.parentElement;
			
			if(cellIndex==0) {
				cellIndex = (row.cells.length);
			}
					
			var _cell = row.insertCell(cellIndex);
			
			if(_reportType == 'SEP') {
			
				innerHTML = "<img src='images/table/separator.gif'/>" ;
				_cell.innerHTML = innerHTML;
			}
			
			if(_reportType == 'XLS') {
				
				innerHTML = "<a href='#loadingArea' title='XLS Zip Export' id='_btnZippedXLS' name='_btnZippedXLS'><img alt='XLS Zip Export' src='images/table/zip_xls.gif' style='width:50px;height:42px;' onclick='submitLink(_btnZippedXLS)'/></a>" ;
			
				_cell.innerHTML = innerHTML;
			}
			
			if(_reportType == 'XLSX') {
			
				innerHTML = "<a href='#loadingArea' title='XLSX Zip Export' id='_btnZippedXLSX' name='_btnZippedXLSX'><img alt='XLSX Zip Export' src='images/table/zip_xlsx.gif' style='width:50px;height:42px;' onclick='submitLink(_btnZippedXLSX)'/></a>" ;
			
			_cell.innerHTML = innerHTML;
			}
			
			if(_reportType == 'CSV') {
				innerHTML = "<a href='#loadingArea' title='CSV Zip Export' id='_btnZippedCSV' name='_btnZippedCSV'><img alt='CSV Zip Export' src='images/table/zip_csv.gif' style='width:50px;height:42px;' onclick='submitLink(_btnZippedCSV)'/></a>" ;
			
			_cell.innerHTML = innerHTML;
			}
			
			cellIndex++;
			return true;
		}
		
				
		function preZippedCSVDownload() {
			return createWaitCell();
		}
			
		function postZippedCSVDownload(){
				
			var fileURL = document.getElementById('fileURL');
			var fileLink = document.getElementById('fileLink');
			
			if(fileURL.value != null && fileURL.value != '' && fileURL.value.indexOf('null') == -1 &&  fileURL.value.indexOf('.zip') != -1) {
				
				fileLink.href = fileURL.value;
				clickLink(fileLink);
			
			} else {

				document.getElementById('errorDetails').value = fileURL.value;
				alert('Error while processing Zip file.');
			}

			removeWaitCell();
			
			return true;
		}
			
		function submitLink(_link) {
		
			if(zipInProcess) {
			
				alert('ZIP Report is already in process.');
				return false;
			}
		
			var linkId = _link.id.replace('_', '');
			
			if(totalRecordsCount > 65500 && linkId == 'btnZippedXLS') {
				
				alert('Total number of records cannot be process in XLS format, try, XLSX or CSV.');
				return false;
			}
		
			zipInProcess = true;
			
			var link = document.getElementById(linkId);
			
			clickLink(link);
		}
			
		function clickLink(link) {
				
			var cancelled = false;
	
			if (document.createEvent) {
					
				var event = document.createEvent("MouseEvents");
				event.initMouseEvent("click", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
				cancelled = !link.dispatchEvent(event);
					
			} else if (link.fireEvent) {
						
				cancelled = !link.fireEvent("onclick");
			}
	
			if (!cancelled) {
				window.location = link.href;
			}
		}
		
		function error(request) {
	      	
			alert(request.status+" "+request.statusText+"\nPlease contact with the administrator for more details.");
			
	      	document.getElementById('errorDetails').value = request.readyState+"/"+request.status +"/"+request.statusText + " : " +request.responseText;
	    }
		
		function createWaitCell() {
						
			var ec_rd = document.getElementsByName('ec_rd')[0];
			var row = ec_rd.parentElement.parentElement;			
			var _cell = row.insertCell(cellIndex);
			_cell.innerHTML = "<img id='loading_zip' src='images/table/loading.gif'/>" ;

			return true;
		}
		
		
		function removeWaitCell() {
			
			var ec_rd = document.getElementsByName('ec_rd')[0];
			var row = ec_rd.parentElement.parentElement;	
			
			row.deleteCell(cellIndex);
			zipInProcess = false;

			return true;
		}
		
	</script>	
	
		<ajax:updateField
			  baseUrl="${contextPath}/transactionDataDownload.html"
			  source="fileURL"
			  target="fileURL"
			  action="btnZippedXLSX"
			  parser="new ResponseXmlParser()"
			  parameters="reportId=${requestScope.reportId},reportType=XLSX"
			  preFunction="preZippedCSVDownload"
			  postFunction="postZippedCSVDownload"
			  errorFunction="error"/>

		<ajax:updateField
			  baseUrl="${contextPath}/transactionDataDownload.html"
			  source="fileURL"
			  target="fileURL"
			  action="btnZippedXLS"
			  parser="new ResponseXmlParser()"
			  parameters="reportId=${requestScope.reportId},reportType=XLS"
			  preFunction="preZippedCSVDownload"
			  postFunction="postZippedCSVDownload"
			  errorFunction="error"/>		
			  
		<ajax:updateField
			  baseUrl="${contextPath}/transactionDataDownload.html"
			  source="fileURL"
			  target="fileURL"
			  action="btnZippedCSV"
			  parser="new ResponseXmlParser()"
			  parameters="reportId=${requestScope.reportId},reportType=CSV"
			  preFunction="preZippedCSVDownload"
			  postFunction="postZippedCSVDownload"
			  errorFunction="error"/>