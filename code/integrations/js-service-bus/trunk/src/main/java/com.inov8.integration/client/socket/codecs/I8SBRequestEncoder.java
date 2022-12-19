package com.inov8.integration.client.socket.codecs;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class I8SBRequestEncoder implements ProtocolEncoder {
    private static Logger logger = LoggerFactory.getLogger(I8SBRequestEncoder.class.getSimpleName());

    /**
     * The encode automatically called when message is sent from session.write
     * from handler. It receives the written object, extract & build bytes.
     * After getting bytes, allocate the buffer and sent them to the socket
     * output stream.
     */
    @Override
    public void encode(IoSession session, Object _message, ProtocolEncoderOutput out) throws Exception {
        logger.debug("Encoding request");
        String message = (String) _message;
        IoBuffer buffer = IoBuffer.allocate(message.length(), false);
        buffer.put(message.getBytes());
        logger.debug("Encoded request bytes length: " + message.getBytes().length);
        logger.debug("Outgoing message: " + message);
        buffer.flip();
        out.write(buffer);

    }

    /**
     * Release & dispose all the resources held by this encoder.
     */
    @Override
    public void dispose(IoSession session) throws Exception {
        logger.debug("dispose: closing session...");
        session.close(true);
    }
}
