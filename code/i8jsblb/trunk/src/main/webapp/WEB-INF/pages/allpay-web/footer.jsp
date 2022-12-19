<%--
<%@ page language="java" import="com.inov8.microbank.webapp.action.allpayweb.Encryption" %>


<script>
$(document).ready(function(){
	$("#footer-agent-web td").hover(
		function()
		{
			$(this).addClass('footer-td-bg');
		},
		function()
		{
			$(this).removeClass('footer-td-bg');
		}
	)
})
</script>

<table height="78px" width="100%" cellpadding="0" cellspacing="0">
	<tr style="margin:0px;" height="78px">
		<td align="center" valign="bottom" height="78px">
			<!-- USER TYPES: 3 - Retailer 4 - Agent -->
			<table id="footer-agent-web" border="0" align="center" height="78px" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<!-- 
					<td width="15%">
						<a href='#' title='i8 Cr. Transfer'>
							<img src="images/aw/icon20.png" border='0' align='middle' alt='i8 Credit Transfer' />
							i8 Cr Transfer
						</a>		
					</td>
						-->
					
					<c:if test="${sessionScope.USTY != 4}">
					<!-- 
						<td>	
							<a href="salessummary.aw" title="Sales Summary">
								<img src="images/aw/icon21.png" border="0" align="middle" />Sales Summary
							</a>			
						</td>
						-->
					
					</c:if> 
					<!-- 
					<td>
						<a href="transactionlog.aw?APID=${sessionScope.APID}&ACID=${requestScope.ACID}&BAID=${requestScope.BAID}&UID=${requestScope.UID}" title="Transaction Log">
							<img src="images/aw/icon22.png" border="0" align="middle" />Transaction	Log
						</a>		
					</td>
						-->
					<td>
						<a href="changePin.aw" title="Change PIN" class="change-pin">
							Change PIN
						</a>		
					</td>
					<td>
						<a href="checkallpaybalance.aw" title="Check Balance" class="check-balance">
							Check Balance
						</a>		
					</td>
					<td>
						<a href="productpurchase.aw?PID=<%= Encryption.encrypt("50002")%>&dfid=<%= Encryption.encrypt("17")%>&PNAME=<%= Encryption.encrypt("Receive Cash")%>" title="Receive Cash" class="receive-cash">
							Receive Cash
						</a>		
					</td>
					<td>
						<a href="payCashStepOne.aw" title="Pay Cash" class="pay-cash">
							Pay Cash
						</a>
					</td>
					<td>
						<a href="payCashCNICStepOne.aw" title="Pay Cash to CNIC (IDP)" class="pay-cash-to-cnic">
							Pay Cash (CNIC)
						</a>
					</td>
					<td>
						<a href="mainmenu.aw" title="Bill Payment" class="bill-payment">
							Bill Payment
						</a>
					</td>
					<td>
						<a href="agentTransfer.aw" title="Agent Transfer" class="agent-transfer">
							Agent Transfer
						</a>
					</td>
					<td>
						<a href="case2CashStepOne.aw" title="Cash Transfer" class="cash-transfer">
							Cash Transfer
						</a>
					</td>
					<td>
						
						<a href="transactionlog.aw?APID=${sessionScope.APID}&ACID=${requestScope.ACID}&BAID=${requestScope.BAID}&UID=${requestScope.UID}" title="Transaction Log">
						
						<a href="ministatement.aw" title="Mini Statement" class="mini-statement">
							Mini Statement
						</a>		
					</td>
					<td>
						<a href="myCommission.aw" title="My Commission" class="my-commission">
							My Commission
						</a>
					</td>
					<td>
						<a href="transferIn.aw" title="Transfer-In" class="my-commission">
							Transfer-In
						</a>
					</td>
					<td>
						<a href="transferOut.aw" title="Transfer-Out" class="my-commission">
							Transfer-Out
						</a>
					</td>
					<td>
						<a href="bulkBillPayment.aw" title="Bulk Bill Payment" class="my-commission">
							Bulk Bill Payment
						</a>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
--%>
