package com.invo8.thales;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


public class ThalesCodecFactory implements ProtocolCodecFactory {

	private ProtocolDecoder decoder;
	private ProtocolEncoder encoder;
	
	public ThalesCodecFactory() {
		this.decoder = new ThalesDecoder();
		this.encoder = new ThalesEncoder();
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
