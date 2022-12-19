package com.inov8.hsm.client.codecs;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inov8.hsm.pdu.BasePDU;

public class HSMRequestEncoder implements ProtocolEncoder {
	private static Logger logger = LoggerFactory.getLogger(HSMRequestEncoder.class.getSimpleName());


	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		BasePDU basePDU = (BasePDU) message;
		String rawPdu = new String(basePDU.getRawPdu());
		String hexArrayLength = Integer.toHexString(rawPdu.length());
		hexArrayLength = StringUtils.leftPad(hexArrayLength, 4, '0');
		byte[] headerMessageByte = Hex.decodeHex(hexArrayLength.toCharArray());
		IoBuffer buffer = IoBuffer.allocate(headerMessageByte.length + rawPdu.length(), false);
		buffer.put(headerMessageByte);
		buffer.put(rawPdu.getBytes());
		
		logger.info("Sent Message: " + rawPdu);
		
		buffer.flip();
		out.write(buffer);
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		logger.debug("dispose: closing session...");
		session.close(true);
	}

}
