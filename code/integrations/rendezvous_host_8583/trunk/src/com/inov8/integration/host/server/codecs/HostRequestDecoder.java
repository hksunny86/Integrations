package com.inov8.integration.host.server.codecs;

import com.inov8.integration.middleware.util.CommonUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.jpos.iso.ISOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.inov8.integration.middleware.util.CommonUtils;

public class HostRequestDecoder extends CumulativeProtocolDecoder {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private static final String DECODER_STATE_KEY = HostRequestDecoder.class.getName() + ".STATE";

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
            if (io.remaining() >= 4) {
                byte[] packetLengthBytes = new byte[4];
                io.get(packetLengthBytes);
				String hexString = CommonUtils.bytesToHex(packetLengthBytes);
                decoderState.packetLength = (Integer.parseInt(hexString, 16));
                
            } else {
                return false;
            }
        }

        if ((io.remaining() < decoderState.packetLength)) {
            logger.debug("###################Packet not received completly##################");
            logger.debug("Actual Packet Remaining Length ->" + (decoderState.packetLength - 4));
            logger.debug("Buffer Remaining bytes->" + io.remaining());
            return false;
        }

        byte[] packetByte = null;

        packetByte = new byte[decoderState.packetLength];
        io.get(packetByte);
        String packet = new String(packetByte);
        logger.debug("PDU: " + packet);
        decoderState.packetLength = -1;
        out.write(packetByte);

        String s1 = ISOUtil.hexdump(packetByte);
        String s2 = ISOUtil.hexString(packetByte);

        logger.debug("Decoding ISO 8583:\n" + s1);
        logger.debug("Hexdump ISO 8583:\n" + s2);

        // logger.debug("PDU Hexdump: " + Hex.encodeHexString(packetByte));
        return true;
    }

}
