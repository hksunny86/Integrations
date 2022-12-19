package com.inov8.hsm.pdu;

import java.util.Date;

/**
 * 
 * <p>
 * <code>PDUWrapper</code> is an utility class, which will hold response PDU and
 * response arrival time, and RRN key. Which is used to uniquely identify
 * transaction requests in DC and external system as well.
 * </p>
 * 
 */
public class PDUWrapper {
	private BasePDU basePDU;
	private Date poolTimeIn;
	private String RRNKey;

	public BasePDU getBasePDU() {
		return basePDU;
	}

	public void setBasePDU(BasePDU basePDU) {
		this.basePDU = basePDU;
	}

	public Date getPoolTimeIn() {
		return poolTimeIn;
	}

	public void setPoolTimeIn(Date poolTimeIn) {
		this.poolTimeIn = poolTimeIn;
	}

	public String getRRNKey() {
		return RRNKey;
	}

	public void setRRNKey(String rRNKey) {
		RRNKey = rRNKey;
	}

}
