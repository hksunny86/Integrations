package com.inov8.microbank.common.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum CommissionStackHolderEnum {

	ASKARI_STACKHOLDER(50020L),
	ZONG_STACKHOLDER(50022L),
	INOV8_STACKHOLDER(50024L),
	FED_STACKHOLDER(50026L),
	AGENT1_STACKHOLDER(50028L),
	AGENT2_STACKHOLDER(50030L),
	WHT_STACKHOLDER(50032L),
	FRANCHISE1_STACKHOLDER(50036L),
	FRANCHISE2_STACKHOLDER(50038L);
	
	private static final Map<Long, CommissionStackHolderEnum> lookup = new HashMap<Long, CommissionStackHolderEnum>();
	private Long commissionStackHolderId;
	
	static {
	
		for(CommissionStackHolderEnum commissionStackHolderEnum : EnumSet.allOf(CommissionStackHolderEnum.class)) {
	        
	        lookup.put(commissionStackHolderEnum.getCommissionStackHolderId(), commissionStackHolderEnum);
		}
	}

	private CommissionStackHolderEnum(Long productId) {
		this.commissionStackHolderId = productId;
	}

	public long getCommissionStackHolderId() {
		return commissionStackHolderId.longValue();
	}
	
	public static CommissionStackHolderEnum lookup(String commissionStackHolderId) {
		return lookup.get(commissionStackHolderId);
	}
	
	
	public static Boolean contains(Long productId){
	
		Boolean retVal=Boolean.FALSE;
		
		for (CommissionStackHolderEnum commissionStackHolderEnum : CommissionStackHolderEnum.values()) {
		
			if(commissionStackHolderEnum.commissionStackHolderId.equals(productId)){
				retVal=Boolean.TRUE;
				break;
			}
		}
		
		return retVal;
	}
	
}
