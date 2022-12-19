package com.inov8.integration.middleware.pdu;

import java.io.Serializable;
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
public class PDUWrapper implements Serializable{
	private static final long serialVersionUID = -5580322991079986218L;
	private AMBITPDU basePDU;
	private Date poolTimeIn;
	private String RRNKey;

	public AMBITPDU getBasePDU() {
		return basePDU;
	}

	public void setBasePDU(AMBITPDU basePDU) {
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
