<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Maqsood Shahzad-->
<%@include file="/common/taglibs.jsp"%>

<%@ page import="com.inov8.framework.common.model.popup.*" %>
<%@ page import="com.inov8.framework.common.util.*" %>
<%@ page import="com.inov8.framework.common.model.*" %>

<%@ page import="java.util.*" %>


<html>
<head>


<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/extremecomponents.js"></script>
<%
Popup popup = null;
List modelList = null;
List columnList = null;
if(null != request.getAttribute("popup"))
{
  popup = (Popup)request.getAttribute("popup");
  columnList = popup.getColumnList();
  modelList = popup.getBasePersistableModelList();
  request.setAttribute("modelList",modelList);

}
%>
<script type="text/javascript">
function getHashMapElement(key)
{
  return this[key];
}
function popupCallback(src,popupName,valueString)
{
	
  var columnHashMap = new Object;
  columnHashMap.get = getHashMapElement;

  var array1 = valueString.split(';');
  for(i=0;i<array1.length;i++)
  {
    tempStringArray = array1[i].split('=');
    columnHashMap[tempStringArray[0]] = tempStringArray[1];
  }
  if(window.opener && !opener.closed && null != opener.popupCallback)
  {
  	opener.popupCallback(src,popupName,columnHashMap);
  }

  window.close();
}
</script>
<title>
<%=popup.getTitle()%>
</title>
</head>
<body bgcolor="#ffffff">


<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

	<ec:table
		items="modelList"
                var = "model"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit"
                title="<%=popup.getTitle()%>"
 		action="${pageContext.request.contextPath}/popup.html?src=${param.src}&popupName=${param.popupName}"
                rowsDisplayed="<%=popup.getRowsDisplayed().toString()%>"
		>
            <%
            String values = null;
            if(null!=pageContext.getAttribute("model"))
            {
              values = PopupUtils.getModelPropertiesString(((BasePersistableModel)pageContext.getAttribute("model")),columnList);
              values+=";PK="+((BasePersistableModel)pageContext.getAttribute("model")).getPrimaryKey();
              values = StringUtils.removeSpecialCharacters(values);


            }
  %>
  <c:set var="rowValues" value="<%=values%>"/>
  <ec:row onmouseover="this.style.cursor='pointer'" onclick = "javascript:popupCallback('${param.src}','${param.popupName}','${rowValues}')" >
  <%
  					
                    for(int i = 0;i<columnList.size();i++)
                {
                  if(null != columnList.get(i) && ((Column)columnList.get(i)).isVisible() == true)
                  {
                  	String sortable = "true";
  					String filterable = "true";
                  	if(((Column)columnList.get(i)).isSortable() == false)
                  	{
                  		sortable = "false";
                  	}
                  	if(((Column)columnList.get(i)).isFilterable() == false)
                  	{
                  		filterable = "false";
                  	}
                  	
 					                 
                    %>
                    <ec:column property="<%=((Column)columnList.get(i)).getPropertyName()%>" title="<%=((Column)columnList.get(i)).getHeader() %>" sortable="<%=filterable%>" filterable="<%=filterable%>"/>
                    <%
                    }
                  }
                %>

                </ec:row>
	</ec:table>


</body>
</html>

