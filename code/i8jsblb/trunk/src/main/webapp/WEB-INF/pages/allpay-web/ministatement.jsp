<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Recent Transaction Log</title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,IE=11,IE=10"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css"
          type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css"
          type="text/css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/jquery.navgoco.css"
          type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/demo.css"
          type="text/css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/jquery.cookie.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/jquery.cookie.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/jquery.navgoco.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/jquery.navgoco.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/agentWebMenu.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/highlight.pack.js"></script>
    <style type="text/css">
        * {
            box-sizing: border-box;
        }

        .header {
            border: 1px solid red;
            padding: 15px;
        }

        .vertical-left-menu {
            width: 20%;
            float: left;
            padding: 15px;
            overflow-y: scroll;
            height: 500px;

        }

        .main-body-area {
            width: 80%;
            float: left;
            padding: 15px;
            height: 430px;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            $("table.tableRegion tbody tr:even").addClass('even');
            $("table.tableRegion tbody tr:odd").addClass('odd');
            $("table.tableRegion tbody tr").hover(
                function () {
                    $(this).addClass('highlight');
                },
                function () {
                    $(this).removeClass('highlight');
                }
            );

            $("table.tableRegion thead tr td").hover(
                function () {
                    $(this).addClass('tableHeaderSort');
                },
                function () {
                    $(this).removeClass('tableHeaderSort');
                }
            );
        });
    </script>
</head>
<body oncontextmenu="return false">


<div style="height:95px%">
    <jsp:include page="header.jsp"></jsp:include>
</div>
<div class="vertical-left-menu ">
    <jsp:include page="agentWebMenu.jsp"></jsp:include>
</div>
<div class="main-body-area">
    <c:if test="${pinRequired}">
        <form method="post" action="<c:url value="/ministatement.aw"/>"
              onsubmit="return validate()">
            <table align="center" width="100%" border="0">
                <tr>
                    <td align="center" colspan="2">&nbsp;</td>
                </tr>
                <tr>
                    <td align="center" colspan="2" id="msg">
                        &nbsp;
                        <c:if test="${not empty errors}">
									<span class="error-msg" style="padding-left: 20%;">
										<c:out value="${errors}"/>
									</span>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div align="center" class="hf">Enter MPIN For MiniStatement</div>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2">&nbsp;</td>
                </tr>
                <tr>
                    <td align="right"><span style="color: #FF0000">*</span><span
                            class="label">MPIN:</span>
                    </td>
                    <td width="50%"><input type="password" onkeypress="return isNumberKey(event)"
                                           name="PIN"
                                           id="PIN"
                                           maxlength="4" class="text"/></td>
                </tr>
                <tr>
                    <td align="right">
                        <input type="hidden" name="ACID" value="${requestScope.ACID}"/>
                        <input type="hidden" name="BAID" value="${requestScope.BAID}"/>
                        <input type="hidden" name="UID" value="${requestScope.UID}"/>
                        <input type="hidden" name="PIN_RETRY_COUNT" value="${PIN_RETRY_COUNT}"/>
                        <input type="hidden" name="STATUS" value="${STATUS}"/>
                    </td>
                    <td><security:token/>
                        <input class="mainbutton"
                               type="submit" id="confirm"
                               name="confirm"
                               style=" width: 120px;!important"
                               value="Next"/>
                    </td>
                </tr>
            </table>
        </form>
    </c:if>
    <c:if test="${not pinRequired}">
        <div align="center" class="eXtremeTable" style="height:100%;width:100%;">
            <table width="100%" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <c:if test="${empty transactionDetailPortalListModelList}">
                            <table align="center" width="100%" border="0" cellpadding="0"
                                   cellspacing="0">
                                <tr>
                                    <td class="statusBar">
                                        No transaction(s) found!
                                    </td>
                                </tr>
                            </table>
                        </c:if>
                    </td>
                </tr>

                <tr>
                    <td align="center" colspan="6">
                        <font color="#0066b3" size="5px">Mini Statement</font>
                        <br></br>
                    </td>
                </tr>

                <tr height="60%" valign="top" align="center">
                    <td>
                        	<table id="theTable" align="center" width="80%" class="tableRegion" cellpadding="0" cellspacing="0">
                            	<thead>
									<tr>
										<td width="6%" align="center" class="tableHeader">Sr. #</td>
										<td width="24%" align="center" class="tableHeader">Dated</td>
										<td width="16%" align="center" class="tableHeader">Transaction ID</td>
										<td width="30%" align="center" class="tableHeader">Product</td>
										<td width="12%" align="center" class="tableHeader">Debit</td>
										<td width="12%" align="center" class="tableHeader">Credit</td>
									</tr>
								</thead>
                            <tbody>
                            <c:forEach items="${transactionDetailPortalListModelList}"
                                       var="transactionModel" varStatus="i">

                                <c:set var="db" scope="page" value="0.0"/>
                                <c:set var="cr" scope="page" value="0.0"/>

                                <c:choose>

                                    <%--<c:when test="${transactionModel.productId == 50020}">--%>

                                    <c:when test="${transactionModel.productId == 50006 or transactionModel.productId == 50002 or transactionModel.productId == 50020 ||
																transactionModel.productId == 50010 or transactionModel.productId == 50011 ||
																transactionModel.productId == 50013 or transactionModel.productId == 50018 ||
																transactionModel.productId == 2510791 or transactionModel.productId == 2510801}">
                                        <%--<c:when test="${transactionModel.productId == 50006 || transactionModel.productId == 50002 || transactionModel.productId == 50020 ||
                                                                transactionModel.productId == 50010 || transactionModel.productId == 50011 ||
                                                                transactionModel.productId == 50013 || transactionModel.productId == 50018 ||
                                                                transactionModel.productId == 2510791 || transactionModel.productId == 2510801}">--%>

                                        <c:if test="${transactionModel.productId == 2510801}"> <!-- Bulk Payment -->
                                            <c:set var="cr" scope="page"
                                                   value="${(transactionModel.transactionAmount - transactionModel.inclusiveCharges) + transactionModel.agent2Commission}"/>
                                        </c:if>

                                        <c:if test="${transactionModel.productId == 50006}"> <!-- cash Withdrawal -->
                                            <c:set var="cr" scope="page"
                                                   value="${transactionModel.transactionAmount + transactionModel.agent2Commission}"/>
                                        </c:if>

                                        <c:if test="${transactionModel.productId == 50002}"> <!-- cash Deposit -->
                                            <c:set var="db" scope="page"
                                                   value="${(transactionModel.transactionAmount + transactionModel.exclusiveCharges) - transactionModel.agentCommission}"/>
                                        </c:if>

                                        <c:if test="${transactionModel.productId == 50010}"> <!-- account to cash -->
                                            <c:set var="cr" scope="page"
                                                   value="${(transactionModel.transactionAmount + transactionModel.agent2Commission)}"/>
                                        </c:if>

                                        <c:if test="${transactionModel.productId == 50018}"> <!-- Customer Retail Payment -->
                                            <c:set var="cr" scope="page"
                                                   value="${(transactionModel.transactionAmount - transactionModel.inclusiveCharges)}"/>
                                        </c:if>

                                        <c:if test="${transactionModel.productId == 50020}"> <!-- Transfer In-->
                                            <c:set var="cr" scope="page"
                                                   value="${(transactionModel.transactionAmount - transactionModel.inclusiveCharges)}"/>
                                        </c:if>

                                        <c:if test="${transactionModel.productId == 50011}">

                                            <c:if test="${transactionModel.agent1Id == sessionScope.UID}">
                                                <c:set var="db" scope="page"
                                                       value="${(transactionModel.totalAmount - transactionModel.agentCommission)}"/>
                                            </c:if>
                                            <c:if test="${transactionModel.agent2Id == sessionScope.UID}">
                                                <c:set var="cr" scope="page"
                                                       value="${(transactionModel.transactionAmount + transactionModel.agent2Commission)}"/>
                                            </c:if>

                                        </c:if>

                                        <c:if test="${transactionModel.productId == 2510791}">

                                            <c:if test="${transactionModel.agent1Id == sessionScope.UID}">
                                                <c:set var="db" scope="page"
                                                       value="${(transactionModel.transactionAmount - transactionModel.agentCommission)}"/>
                                            </c:if>

                                        </c:if>

                                        <c:if test="${transactionModel.productId == 50013}">
                                            <c:if test="${transactionModel.agent1Id == sessionScope.UID}">
                                                <c:set var="db" scope="page"
                                                       value="${(transactionModel.totalAmount)}"/>
                                            </c:if>
                                            <c:if test="${transactionModel.agent2Id == sessionScope.UID}">
                                                <c:set var="cr" scope="page"
                                                       value="${(transactionModel.totalAmount)}"/>
                                            </c:if>
                                        </c:if>

                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="db" scope="page"
                                               value="${transactionModel.transactionAmount}"/> <!-- bill payments -->

                                    </c:otherwise>

                                </c:choose>
                               <tr align="right" class="row-color">
									<td align="center">${i.count}
									</td>
	
									<td align="center"><fmt:formatDate pattern="dd-MM-yyyy hh:mm a"  value="${transactionModel.createdOn}" />
									</td>
	
									<td align="center"><c:out value="${transactionModel.transactionNo}" />
									</td>
	
									<td align="center"><c:out value="${transactionModel.productName}" />
									</td>
	
									<td align="center">
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${db}"/>
									</td>
	
									<td align="center">
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${cr}"/>
									</td>
								</tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </td>
                </tr>
            </table>
        </div>
    </c:if>

</div>
</body>
</html>
