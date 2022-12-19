package com.inov8.hsm.pdu.parser;

import java.io.Serializable;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.util.ConfigReader;
import com.inov8.hsm.util.FieldUtil;
import com.inov8.hsm.util.VersionInfo;
@VersionInfo(lastModified = "24/12/2014", releaseVersion = "1.0", version = "1.1", createdBy = "Zeeshan Ahmed, Faisal Basra",tags="HSM Header Parser")
public class HeaderParser implements Serializable{
	
	private static final long serialVersionUID = -6340778434129005226L;
	
	private static int HEADER_SIZE = Integer.parseInt(ConfigReader.getInstance().getProperty("header.size", "24", false));

	public static BaseHeader parseHeader(StringBuilder hexBuilder) {
		BaseHeader header = new BaseHeader();
		
		header.setUPID(FieldUtil.extractString(hexBuilder, HEADER_SIZE));
		header.setCommand(FieldUtil.extractString(hexBuilder, 2));
		
		return header;
	}
}
