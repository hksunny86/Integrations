package com.inov8.integration.client.socket.codecs;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * <code>I8SBResponseDecoder</code> is mainly responsible for handling
 * response received from external system.
 * </p>
 */
public class I8SBResponseDecoder extends CumulativeProtocolDecoder {
    private static Logger logger = LoggerFactory.getLogger(I8SBResponseDecoder.class.getSimpleName());
    private static final String DECODER_STATE_KEY = I8SBResponseDecoder.class.getName() + ".STATE";

    public static class DecoderState {
        int packetLength = -1;
    }

    /**
     * doDecode is responsible for decoding response,
     * in the following fashion
     * Read Bytes
     * Read first 4 bytes & calculate response message size.
     * Verify that size is equal to remaining message or not
     * If size is not equal ot meta length then show error message
     * If size is equal or greater than meta length then read bytes of given meta length.
     */
    @Override
    protected boolean doDecode(IoSession session, IoBuffer io, ProtocolDecoderOutput out) throws Exception {
        logger.debug("Decoding response");
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

        if (decoderState.packetLength == -1) {
            // try to read packet
            if (io.remaining() >= 4) {
                byte[] packetLengthBytes = new byte[4];
                io.get(packetLengthBytes);
                String hexString = new String(packetLengthBytes);
                try {
                    decoderState.packetLength = (Integer.parseInt(hexString));
                } catch (Exception ex) {
                    logger.error("ERROR: While converting length meta data.", ex);
                    logger.debug("Firing reconnect.......");
                    session.close(true);
                    return true;
                }
            } else {
                return false;
            }
        }

        if ((io.remaining() >= decoderState.packetLength)) {
            byte[] packetByte = null;
            packetByte = new byte[decoderState.packetLength];
            io.get(packetByte);
            String packet = new String(packetByte);
            logger.debug("Incoming message: " + packet);
            decoderState.packetLength = -1;
            out.write(packet);
            return true;
        }
        logger.debug("Packet not received completely");
        logger.debug("Actual packet remaining length: " + (decoderState.packetLength - 4));
        logger.debug("Buffer remaining bytes: " + io.remaining());
        return false;
    }

}
