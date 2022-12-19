package com.inov8.integration.server.codecs;

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
public class MockCodecFactory implements ProtocolCodecFactory {

	private ProtocolDecoder decoder;
	private ProtocolEncoder encoder;

	public MockCodecFactory() {
		this.decoder = new MockRequestDecoder();
		this.encoder = new MockResponseEncoder();
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
		return decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
		return encoder;
	}

}
