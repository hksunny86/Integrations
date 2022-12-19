<!--Author: Mohammad Shehzad Ashraf -->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
   <head>
<meta name="decorator" content="decorator">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
      <meta name="title" content="Concern Category"/>
      
         <script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
  
  
  
   <%@include file="/common/ajax.jsp"%>
  

 	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
  	<script type="text/javascript">
  		var jq=$.noConflict();
		function initDelProgress()
		{   
			if (confirm('Are you sure you want to proceed?')==true)
		    {

			  $('errorMsg').innerHTML = "";
			  $('successMsg').innerHTML = "";
			  Element.hide('successMsg'); 
			  Element.hide('errorMsg'); 
				
 	    	  return true;
		    }
		    else
			{
			  $('errorMsg').innerHTML = "";
			  $('successMsg').innerHTML = "";
			  Element.hide('successMsg'); 
			  Element.hide('errorMsg'); 
			  
				return false;
			}
			
			
		}


		var isErrorOccured = false;
		function resetDelProgress()
		{
			divId = $F('divsToDisable');   
		    if(!isErrorOccured)
		    {   
			    // clear error box
			    $('errorMsg').innerHTML = "";
			  	Element.hide('errorMsg'); 
			    if($('successMessages')!=null){
			        $('successMessages').innerHTML = "";
			        Element.hide('successMessages');
			    }
			    
			    $('successMsg').innerHTML = $F('message');
			    
			    // display success message
			    Element.show('successMsg');

			    deleteConcernCategoryRow(divId);//delete Row containing this category
			}
			
			isErrorOccured = false;
			
			if($('del_'+divId) != null){
			  	$('del_'+divId).innerHTML = "&nbsp;";
			 }
			if($('edit_'+divId) != null){
			  	$('edit_'+divId).innerHTML = "&nbsp;";
			 }
			scrollToTop(1000);

		}

		function reportDelError(request, obj) 
		{ 
		  $('successMsg').innerHTML = "";
		  Element.hide('successMsg'); 
		  $('errorMsg').innerHTML = "Record could not be deleted.";
		  Element.show('errorMsg');
		  isErrorOccured = true;
		  scrollToTop(1000);
		}

		function deleteConcernCategoryRow(concernCategoryId)
  		{
  			var categoryDiv = $('del_'+concernCategoryId);
  			var categoryDivParents = Element.ancestors( categoryDiv );
  			Element.hide( categoryDivParents[1] );
  		}
	</script>
	<%
		String createPermission = PortalConstants.CONCERN_CAT_DEF_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.CSR_GP_CREATE;

		String updatePermission = PortalConstants.CONCERN_CAT_DEF_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.CSR_GP_UPDATE;

		String deletePermission = PortalConstants.CONCERN_CAT_DEF_DELETE;
		deletePermission +=	"," + PortalConstants.PG_GP_DELETE;
		deletePermission +=	"," + PortalConstants.CSR_GP_DELETE;
	 %>
  
  
  
   </head>
   <body bgcolor="#ffffff">

  <c:set var="retrieveAction" value="<%=PortalConstants.ACTION_RETRIEVE%>" />

	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
	<div id="errorMsg" class ="errorMsg" style="display:none;"></div>

			<c:if test="${not empty status.errorMessages}">
				<div class="errorMsg">
					<c:forEach var="error" items="${status.errorMessages}">
						<c:out value="${error}" escapeXml="false" />
						<br />
					</c:forEach>
				</div>
			</c:if>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
		<table width="100%">
          <html:form name='concernCategoryForm' id='concernCategoryForm'
				commandName="concernCategoryModel" method="post"
				action="p_opconcerncatdefmanagement.html?actionId=${retrieveAction}"
				onsubmit="/*return validateForm()*/">
				
			<input type="hidden" id="message" value=""/>
			<input type="hidden" id="divsToDisable" value=""/>
		
			<tr>
              <td colspan="2" width="67%" align="right">&nbsp;
 	               <authz:authorize ifAnyGranted="<%=createPermission%>">
							<a href="p_opconcerncategoryform.html?actionId=1" class="linktext" onclick="" align="right"> Add Concern Category </a><br/>
			       </authz:authorize>
              </td>
          </tr>				
          <tr>
              <td width="33%" class="formText" align="right">Category:</td>
              <td width="67%" class="formText" align="left">
                 <html:input path="name"
							onkeypress="return maskCommon(this,event)" id="name"
							cssClass="textBox" tabindex="1" maxlength="50" />
              </td>
          </tr>
          <tr>
              <td width="33%" class="formText" align="right"></td>
              <td width="67%" class="formText" align="left">
                <input name="_search" type="submit" class="button" tabindex="2"
									value="Search" />
				<input name="reset" type="reset" class="button" tabindex="3"
							value="Cancel"
							onclick="javascript: window.location='p_opconcerncatdefmanagement.html?actionId=${retrieveAction}'" />
              </td>
          </tr>
          </html:form>
      </table>
      <ec:table
      items="concernCategoryList"
      var = "concernCategoryModel"
      retrieveRowsCallback="limit"
      filterRowsCallback="limit"
      sortRowsCallback="limit"
      action="${pageContext.request.contextPath}/p_opconcerncatdefmanagement.html?actionId=${retrieveAction}"
      title=""
      filterable="false"  >
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        	<ec:exportXls fileName="Concern Category.xls" tooltip="Export Excel" />
        </authz:authorize>
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Concern Category.xlsx" tooltip="Export Excel" />
		</authz:authorize>
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        	<ec:exportPdf fileName="Concern Category.pdf" tooltip="Export Pdf" headerTitle="Concern Category"
          		view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" />
        </authz:authorize>
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Concern Category.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
         <ec:row>
           <ec:column property="name" title="Category" filterable="false" width="300"/>
           
		<authz:authorize ifAnyGranted="<%=updatePermission%>">
           	<ec:column viewsAllowed="html"  alias=" " title="" filterable="false" width="100" sortable="false" style="text-align:center;">
				<div id="edit_${concernCategoryModel.concernCategoryId}">	
					<input type="button" value="Edit" class="button" onclick="javascript: editConcernCategory(${concernCategoryModel.concernCategoryId}); " style="width: 60"/>
		        </div>
			</ec:column>
		</authz:authorize>
		<authz:authorize ifNotGranted="<%=updatePermission%>">
           	<ec:column viewsAllowed="html"  alias=" " title="" filterable="false" width="100" sortable="false" style="text-align:center;">
				<div id="edit_${concernCategoryModel.concernCategoryId}">	
					<input type="button" value="Edit" class="button" style="width: 60" disabled="disabled"/>
		        </div>
			</ec:column>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=deletePermission%>">
          <ec:column viewsAllowed="html"  alias=" " title="" filterable="false" width="100" sortable="false" style="text-align:center;">
			   <div id="del_${concernCategoryModel.concernCategoryId}">	

					  <input type="hidden" id="actionId" value="<%=PortalConstants.ACTION_UPDATE%>"/>	
		           		   <input type="button" value=" Delete " class="button" id="btnDel_${concernCategoryModel.concernCategoryId}" />

						
						<ajax:updateField
						  baseUrl="${contextPath}/p_opconcerncategorychange.html"
						  source="btnDel_${concernCategoryModel.concernCategoryId}"
						  target="message,divsToDisable"
						  action="btnDel_${concernCategoryModel.concernCategoryId}"
						  parameters="actionId={actionId},btnDel=${concernCategoryModel.concernCategoryId}"
						  parser="new ResponseXmlParser()"
						  preFunction="initDelProgress"
						  postFunction="resetDelProgress" 
						  errorFunction="reportDelError"
						  />		 				  

           	   </div>	
           </ec:column>
		</authz:authorize> 
		<authz:authorize ifNotGranted="<%=deletePermission%>">
          <ec:column viewsAllowed="html"  alias=" " title="" filterable="false" width="100" sortable="false" style="text-align:center;">
			   <div id="del_${concernCategoryModel.concernCategoryId}">			  	
		           <input type="button" value=" Delete " class="button" id="btnDel_${concernCategoryModel.concernCategoryId}" disabled="disabled" />
           	   </div>	
           </ec:column>
		</authz:authorize> 
         </ec:row>
      </ec:table>

      <script language="javascript" type="text/javascript">
            function editConcernCategory(ccid){
               window.location = '${pageContext.request.contextPath}/p_opconcerncategoryform.html?actionId=2&concernCategoryId='+ccid;            
            }
            function deleteConcernCategory(ccid){
               if (confirm('Are you sure you want to proceed?')==true)
                   window.location = '${pageContext.request.contextPath}/p_opconcerncatdefmanagement.html?&_setActivate=false&actionId=2&concernCategoryId='+ccid;            
            }
	  </script>

   </body>
</html>



