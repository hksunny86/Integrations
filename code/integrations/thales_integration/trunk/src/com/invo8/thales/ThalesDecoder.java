package com.invo8.thales;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class ThalesDecoder extends CumulativeProtocolDecoder {

	private static final String DECODER_STATE_KEY = ThalesDecoder.class.getName() + ".STATE";

	private static class DecoderState {
		int packetLength = -1;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer io, ProtocolDecoderOutput out) throws Exception {

		io.setAutoExpand(true);
		io.setAutoShrink(true);
		DecoderState decoderState = (DecoderState) session.getAttribute(DECODER_STATE_KEY);
		if (decoderState == null) {
			decoderState = new DecoderState();
			session.setAttribute(DECODER_STATE_KEY, decoderState);
		}

		if (!io.hasRemaining()) {
			return false;
		}

		String hexdump = io.getHexDump();
		// logger.debug("PDU Hexdump: " + hexdump);

		if (decoderState.packetLength == -1) {
			// try to read packet
			if (io.remaining() >= 2) {
				byte[] packetLengthBytes = new byte[2];
				io.get(packetLengthBytes);
				String hexString = bytesToHex(packetLengthBytes);
				decoderState.packetLength = (Integer.parseInt(hexString, 16));
			} else {
				return false;
			}
		}

		if ((io.remaining() < decoderState.packetLength)) {
			System.out.println("###################Packet not received completly##################");
			System.out.println("Actual Packet Remaining Length ->" + (decoderState.packetLength - 4));
			System.out.println("Buffer Remaining bytes->" + io.remaining());
			return false;
		}

		byte[] packetByte = null;

		packetByte = new byte[decoderState.packetLength];
		io.get(packetByte);
		String packet = new String(packetByte);
		decoderState.packetLength = -1;
		out.write(packet);

		return true;
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}
