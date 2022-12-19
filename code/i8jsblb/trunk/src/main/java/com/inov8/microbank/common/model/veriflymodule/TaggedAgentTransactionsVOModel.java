package com.inov8.microbank.common.model.veriflymodule;

import java.util.ArrayList;
import java.util.List;

import com.inov8.microbank.common.model.portal.agentgroup.AgentGroupVOModel;

public class TaggedAgentTransactionsVOModel {
	
	private String productOrService;
	private String transactionCount;  
	private String transactionAmount;
	private String productId;
	

	
	
	//private List<TaggedAgentTransactionsVOModel> children = new ArrayList<TaggedAgentTransactionsVOModel>(0);
	
	
	public String getproductOrService() {
		return productOrService;
	}
	public void setproductOrService(String productOrService) {
		this.productOrService = productOrService;
	}
	public String gettransactionCount() {
		return transactionCount;
	}
	public void setTransactionCount(String transactionCount) {
		this.transactionCount = transactionCount;
	}
	public String getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
	
	
	
	
	

}
