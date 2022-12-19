package com.inov8.integration.server.codecs;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inov8.integration.middleware.pdu.BasePDU;

/**
 * <p>
 * This class is responsible to encode data into Phoenix Protocol format & to
 * send them. The underlying protocol message format should be conformed at the
 * time of building PDU, when preparing request. Usually it will happen in
 * Service layer.
 * </p>
 * 
 * @see ProtocolEncoder.
 */
public class MockResponseEncoder implements ProtocolEncoder {
	private static Logger logger = LoggerFactory.getLogger(MockResponseEncoder.class.getSimpleName());

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
		logger.debug("Encoding PDU to Phoenix format.");
		BasePDU basePdu = (BasePDU) message;
		byte[] pdu = basePdu.getRawPdu();

		String hexArray = Integer.toHexString(pdu.length);
		hexArray = StringUtils.leftPad(hexArray, 8, '0');
		byte[] metaLengthBytes = hexStringToByteArray(hexArray);
		IoBuffer buffer = IoBuffer.allocate(metaLengthBytes.length + pdu.length, false);
		buffer.setAutoExpand(true);
		buffer.setAutoShrink(true);
		buffer.put(metaLengthBytes);
		buffer.put(pdu);

		logger.debug("Encoded Request Bytes Length: " + pdu.length);
		logger.debug("######### REQUEST HEX DUMP ############### " + buffer.getHexDump());
		logger.debug("REQUEST STRING ASCII: " + pdu);
		logger.debug("REQUEST STRING HEX: " + Hex.encodeHexString(metaLengthBytes) + Hex.encodeHexString(pdu));
		buffer.flip();
		out.write(buffer);
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
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
