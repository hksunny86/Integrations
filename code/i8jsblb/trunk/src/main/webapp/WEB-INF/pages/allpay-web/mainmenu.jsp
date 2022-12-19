<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Bill Payment</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-marquee.js"></script>
		<script type="text/javascript">
			$(document).ready( function () {
			$('#linksMarquee').marquee().mouseover(function () {
			$(this).trigger('stop');
			}).mouseout(function () {
			$(this).trigger('start');
			}).mousemove(function (event) {
			if ($(this).data('drag') == true) {
			this.scrollLeft = $(this).data('scrollX') + ($(this).data('x') - event.clientX);
			}
			}).mousedown(function (event) {
			$(this).data('drag', true).data('x', event.clientX).data('scrollX', this.scrollLeft);
			}).mouseup(function () {
			$(this).data('drag', false);
			});

			});
		</script>
	</head>
	<body oncontextmenu="return false">
		<table border="0" cellpadding="0" cellspacing="0" height="100%" width="85%" align="center" class="container">
			<tr height="95px">
				<td>
					<jsp:include page="header.jsp"></jsp:include>
				</td>
			</tr>
			<tr id="contents" valign="top">
				<td>
					<form name="mainmenuform" method="post">
							<!-- div style="overflow: scroll; height: 440px; width: 1200px;" align="right"-->
							<div style="overflow-y: scroll; overflow-x:hidden; height: 405px; width: 100%;" align="right">
						<c:if test="${sessionScope.USTY != 4}">
							<table width="100%" border="0" align="center">
							  <tr align="center">			  
								<td colspan="3">
									<p id="linksMarquee">
										<c:forEach items="${Services}" var="ProdCatalogDetailListViewModelList" varStatus="serviceStatus">
											<c:forEach items="${ProdCatalogDetailListViewModelList}" var="ProdCatalogDetailListViewModel" varStatus="prodStatus">	
												<c:choose>
													<c:when test="${ProdCatalogDetailListViewModel.deviceFlowId == 1 }">													
														<a href="billPaymentDispatcherController.aw?PID=${ProdCatalogDetailListViewModel.productIdEncrypt}&SID=${ProdCatalogDetailListViewModel.serviceId}">						
																${ProdCatalogDetailListViewModel.productName}
														</a>
													</c:when>
													<c:otherwise>
														<a href="productpurchase.aw?PID=${ProdCatalogDetailListViewModel.productIdEncrypt}&SID=${ProdCatalogDetailListViewModel.serviceId}">
																${ProdCatalogDetailListViewModel.productName}
														</a>
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${serviceStatus.last && prodStatus.last}">
													</c:when>
													<c:otherwise>
														&nbsp;|&nbsp;
													</c:otherwise>
												</c:choose>						
											</c:forEach>
										</c:forEach>			
									</p>
								</td>				
							  </tr>
							</table>		  	
								<div align="center" style="height:100%;" >
									<table width="100%" height="20%" border="0" align="center">	
										<c:forEach items="${Services}" var="ProdCatalogDetailListViewModelList">
					  						<tr height="100px" align="center">
												<c:forEach items="${ProdCatalogDetailListViewModelList}" var="ProdCatalogDetailListViewModel">
												    <td width="20%" align="center">
														<c:choose>
															<c:when test="${ProdCatalogDetailListViewModel.deviceFlowId == 1 }">
																<a href="billPaymentDispatcherController.aw?PID=${ProdCatalogDetailListViewModel.productIdEncrypt}&SID=${ProdCatalogDetailListViewModel.serviceId}">
																<img src="images/aw/products/${ProdCatalogDetailListViewModel.productId}.png" title="${ProdCatalogDetailListViewModel.productName}"
																border="0" align="middle" alt="${ProdCatalogDetailListViewModel.productName}" width="120px" height="105px"><br>
																		${ProdCatalogDetailListViewModel.productName}
																</a>
															</c:when>
															<c:otherwise>
																<a href="productpurchase.aw?PID=${ProdCatalogDetailListViewModel.productIdEncrypt}&SID=${ProdCatalogDetailListViewModel.serviceId}">
																<img src="images/aw/products/${ProdCatalogDetailListViewModel.productId}.png" title="${ProdCatalogDetailListViewModel.productName}"
																		border="0" align="middle" alt="${ProdCatalogDetailListViewModel.productName}" width="120px" height="105px"><br>
																		${ProdCatalogDetailListViewModel.productName}
																</a>
															</c:otherwise>
														</c:choose>
													</td>
												</c:forEach>	
				  							</tr>				
										</c:forEach>
									</table>
								</div>
								<c:forEach items="${Products}"
									var="ProdCatalogDetailListViewModel">
									<c:choose>
										<c:when test="${ProdCatalogDetailListViewModel.deviceFlowId == 1}">
											<a href="billPaymentDispatcherController.aw?PID=${ProdCatalogDetailListViewModel.productId}&dfid=${ProdCatalogDetailListViewModel.deviceFlowId}&billServiceLabel=${ProdCatalogDetailListViewModel.billServiceLabel}&PNAME=${ProdCatalogDetailListViewModel.productName}&supplierId=${param.supplierId }&SID=${ProdCatalogDetailListViewModel.serviceId}&ACID=${param.ACID}&UID=${param.UID}&BAID=${param.BAID}">
												<img src="images/aw/${ProdCatalogDetailListViewModel.productId}.png" title="${ProdCatalogDetailListViewModel.productName}" width="120px" height="105px"
													border="0" align="middle" /><br>${ProdCatalogDetailListViewModel.productName}
											</a>
										</c:when>
										<c:otherwise>
											<a href="productpurchase.aw?PID=${ProdCatalogDetailListViewModel.productId}&dfid=${ProdCatalogDetailListViewModel.deviceFlowId}&billServiceLabel=${ProdCatalogDetailListViewModel.billServiceLabel}&PNAME=${ProdCatalogDetailListViewModel.productName}&supplierId=${param.supplierId}&ACID=${param.ACID}&UID=${param.UID}&BAID=${param.BAID}&SID=${ProdCatalogDetailListViewModel.serviceId}">
												<img src="images/aw/${ProdCatalogDetailListViewModel.productId}.png" title="${ProdCatalogDetailListViewModel.productName}" width="120px" height="105px"
													border="0" align="middle" /><br>${ProdCatalogDetailListViewModel.productName}
											</a>
										</c:otherwise>
									</c:choose>
									<br>
								</c:forEach>
						</c:if>
		</div>		
					</form>
				</td>
			</tr>
			<tr valign="bottom">
				<td>
					<jsp:include page="footer.jsp"></jsp:include>
				</td>
			</tr>
		</table>
	</body>
</html>