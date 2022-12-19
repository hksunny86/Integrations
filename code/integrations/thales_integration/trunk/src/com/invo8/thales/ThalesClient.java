package com.invo8.thales;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class ThalesClient {

	public static void main(String[] args) throws IOException, InterruptedException {
		IoConnector connector = new NioSocketConnector();
		connector.getSessionConfig().setReadBufferSize(2048);

		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ThalesCodecFactory()));

		connector.setHandler(new ThalesClientHandler());
		ConnectFuture future = connector.connect(new InetSocketAddress("10.0.1.170", 1545));
		future.awaitUninterruptibly();


		
		if (!future.isConnected()) {
			return;
		}
		IoSession session = future.getSession();
		session.getConfig().setUseReadOperation(true);
		goIntrative(session);
		session.getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}
	
	
	public static void goIntrative(IoSession session){
		
		boolean flag = true;
		while (flag) {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String command = "";
			try {
				command = bufferRead.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Append Header
			command = "0001"+ command;
			
			session.write(command);
		}
	}

}
