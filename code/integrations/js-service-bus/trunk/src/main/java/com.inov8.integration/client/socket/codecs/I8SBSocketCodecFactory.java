package com.inov8.integration.client.socket.codecs;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 
 * <p>
 * <code>PhoenixCodecFactory</code> holds custom implemented
 * <code>ProtocolEncoder</code> and ProtocolDecoder.
 * </p>
 * <p>
 * The encoder is used to encode request into underlying protocol specifications
 * and decode is used to decode the underlying response into application's
 * specific objects.
 */
public class I8SBSocketCodecFactory implements ProtocolCodecFactory {

	private ProtocolDecoder protocolDecoder;
	private ProtocolEncoder protocolEncoder;

	public I8SBSocketCodecFactory() {
		this.protocolDecoder = new I8SBResponseDecoder();
		this.protocolEncoder = new I8SBRequestEncoder();
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
		return protocolDecoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
		return protocolEncoder;
	}

}
