package com.inov8.microbank.server.service.complaintmodule;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ComplaintStatusEnum {
	
	ASSIGNED(1L, "Assigned"), ESCALATED(2L, "Escalated"), RESOLVED(3L, "Resolved"), OVERDUE(4L, "Overdue");
	
	private Long id = null; 
	private String value = null;
	private static final Map<Long, String> lookup = new HashMap<Long, String>();
	
	private ComplaintStatusEnum(Long id, String value) {
		
		this.id = id;
		this.value = value;
	}
	
	static {
		
	    for(ComplaintStatusEnum complaintStatusEnum : EnumSet.allOf(ComplaintStatusEnum.class)) {
	    	
	         lookup.put(complaintStatusEnum.id, complaintStatusEnum.value);
	    }
	}
	
	public String getValue() {
		return value;
	}
	
	public String getValue(Long id) {
		return lookup.get(id);
	}
	
	public Long getId() {
		return id;
	}
	
	public Long getId(String value) {
		
		Long _id = null;
		
		for(Long id : lookup.keySet()) {
			
			if(value.equalsIgnoreCase(lookup.get(id))) {
				_id = id;
				break;				
			}
		}
		
		return _id;
	}	
}
