package com.inov8.integration.host.server.codecs;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class HostCodecFactory implements ProtocolCodecFactory {

	private ProtocolDecoder decoder;
	private ProtocolEncoder encoder;

	public HostCodecFactory() {
		this.decoder = new HostRequestDecoder();
		this.encoder = new HostResponseEncoder();
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
