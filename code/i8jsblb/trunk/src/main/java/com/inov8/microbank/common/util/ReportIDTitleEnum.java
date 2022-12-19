package com.inov8.microbank.common.util;

import java.util.HashMap;
import java.util.Map;

public enum ReportIDTitleEnum {
	
	
	
	TransactionDetailReport("Transaction Details",1L),
	PostedTransactionsRDV("Posted Transactions RDV",3L),
	CommissionReconciliation("Commission Reconciliation",4L),
	SenderRedeemTransactionDetail("Sender Redeem Transaction Detail",5L),	
	TotalCommissionSummaryReport("Total Commission Summary Report",6L),
	CNICbasedTransactionReport("CNIC based Transaction Report",7L),
	Agents("Agents",8L),
	SalesTeamCommissionReport("Sales Team Commission Report",9L),
	BranchlessBankingAccountsReport("Branchless Banking Accounts Report",2L),
	FEDTransactionwiseReport("FED Transaction wise Report",10L),
	WHTTransactionwiseReport("WHT Transaction wise Report",11L),
	AgentFranchiseCommissionReport("Agent/Franchise Commission Report",12L),
	SettlementAccountsLedger("Settlement Accounts Ledger",13L),
	AgentClosingBalanceReport("Agent Closing Balance Report",14L),
	CustomerClosingBalanceReport("Customer Closing Balance Report",15L),
	SearchCustomerReport("Search Customer Report",16L),
	LinkDelinkPaymentModeReport("De-Link/Re-Link Payment Mode Report",17L),
	DigiDormancyAccounts("Digi Dormancy Accounts Report", 18L);
	
	
	
	 
	  
	ReportIDTitleEnum(String title, Long id){
		this.title=title;
		this.id=id;
	}
	 	 
	private String title; 
	private Long id;
	
	public static final Map<String,Long> reportTileIdMap;

	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	static
	{	
		ReportIDTitleEnum[] reportIDTitleEnums = values();
		reportTileIdMap = new HashMap<>(reportIDTitleEnums.length);
		for (ReportIDTitleEnum reportIDTitleEnum : reportIDTitleEnums)
		{
			reportTileIdMap.put(reportIDTitleEnum.getTitle(), reportIDTitleEnum.getId()); 
		}
	}

}
