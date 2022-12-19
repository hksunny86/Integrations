package com.invo8.thales;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ThalesClientHandler extends IoHandlerAdapter {

	private boolean finished;

	public boolean isFinished() {
		return finished;
	}

	@Override
	public void sessionOpened(IoSession session) {
		System.out.println("Session Opened! ");
	}

	@Override
	public void messageReceived(IoSession session, Object message) {
		System.out.println("Response is: " + message.toString());
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		session.close();
	}

}
