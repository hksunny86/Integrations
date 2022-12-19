package com.inov8.integration.host.server.codecs;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.jpos.iso.ISOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.CommonUtils;

public class HostResponseEncoder implements ProtocolEncoder {
	private static Logger logger = LoggerFactory.getLogger(HostResponseEncoder.class.getSimpleName());

	/**
	 * The encode automatically called when message is sent from session.write
	 * from handler. It receives the written object, extract & build bytes.
	 * After getting bytes, allocate the buffer and sent them to the socket
	 * output stream.
	 * 
	 * @param IoSession
	 * @param Object
	 * @param ProtocolEncoderOutput
	 * 
	 * @throws Exception
	 */
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		BasePDU pdu = (BasePDU) message;

		String hexArray = Integer.toHexString(pdu.getRawPdu().length);
		hexArray = StringUtils.leftPad(hexArray, 8, '0');
		byte[] metaLengthBytes = CommonUtils.hexStringToByteArray(hexArray);
		IoBuffer buffer = IoBuffer.allocate(metaLengthBytes.length + pdu.getRawPdu().length, false);
		buffer.setAutoExpand(true);
		buffer.setAutoShrink(true);
		buffer.put(metaLengthBytes);
		buffer.put(pdu.getRawPdu());

		logger.debug("Encoded Response Bytes Length: " + pdu.getRawPdu().length);
		logger.debug("######### REQUEST HEX DUMP ############### " + buffer.getHexDump());
		logger.debug("RESPONSE STRING ASCII: " + pdu.getRawPdu());
		logger.debug("RESPONSE STRING HEX: " + Hex.encodeHexString(metaLengthBytes) + Hex.encodeHexString(pdu.getRawPdu()));
		
		String s1 = ISOUtil.hexdump(pdu.getRawPdu());
		String s2 = ISOUtil.hexString(pdu.getRawPdu());

		logger.debug("Encoding ISO 8583:\n" + s1);
		logger.debug("Hexdump ISO 8583:\n" + s2);
		
		buffer.flip();
		out.write(buffer);
	}

	/**
	 * Release & dispose all the resources held by this encoder.
	 * 
	 * @param IoSession
	 * @throws Exception
	 */
	@Override
	public void dispose(IoSession session) throws Exception {
		logger.debug("dispose: closing session...");
		session.close(true);
	}


}
