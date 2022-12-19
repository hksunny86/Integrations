package com.inov8.hsm.client.codecs;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HSMResponseDecoder extends CumulativeProtocolDecoder {

	private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
	private static final String DECODER_STATE_KEY = HSMResponseDecoder.class.getName() + ".STATE";

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
//		logger.debug("PDU Hexdump: " + hexdump);

		if (decoderState.packetLength == -1) {
			// try to read packet
			if (io.remaining() >= 2) {
				byte[] packetLengthBytes = new byte[2];
				io.get(packetLengthBytes);
				String hexString = Hex.encodeHexString(packetLengthBytes);
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

}
