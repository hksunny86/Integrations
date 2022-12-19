package com.invo8.thales;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;



public class ThalesEncoder implements ProtocolEncoder {
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		String rawPdu = (String) message;
		String hexArrayLength = Integer.toHexString(rawPdu.length());
		hexArrayLength = StringUtils.leftPad(hexArrayLength, 4, '0');
		byte[] headerMessageByte = hexStringToByteArray(hexArrayLength);
		IoBuffer buffer = IoBuffer.allocate(headerMessageByte.length + rawPdu.length(), false);
		buffer.put(headerMessageByte);
		buffer.put(rawPdu.getBytes());
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
		session.close(true);
	}


}
