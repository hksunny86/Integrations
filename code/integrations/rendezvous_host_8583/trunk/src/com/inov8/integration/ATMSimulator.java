package com.inov8.integration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.jpos.iso.ISOUtil;

import com.inov8.integration.middleware.enums.MiddlewareEnum;
import com.inov8.integration.middleware.enums.TransactionCodeEnum;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.pdu.parser.ISO8583MessageParser;
import com.inov8.integration.middleware.pdu.request.BBFundTransferAdviceRequest;
import com.inov8.integration.middleware.pdu.request.BBTitleFetchRequest;
import com.inov8.integration.middleware.pdu.request.EchoRequest;
import com.inov8.integration.middleware.util.CommonUtils;
import com.inov8.integration.middleware.util.DateTools;
import com.inov8.integration.middleware.util.FormatUtils;

public class ATMSimulator {

	private static String HOSTNAME = "127.0.0.1";
	private static int PORT = 26699;
	
	static IoSession session = null;

	public static boolean waitForResponse = false;
	
	private static BufferedReader console = null;
	
	public static void main(String[] args) {
		System.out.println("ATM RDV Simulator");
		System.out.println();
		
		try {
			initConsole();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String ip = readConsole("Enter Host IP");
		if(StringUtils.isNotEmpty(ip)){
			HOSTNAME = ip;
		}
		String port = readConsole("Enter Host PORT");
		if(StringUtils.isNotEmpty(port)){
			PORT = Integer.parseInt(port);
		}
		
		ATMSimulator simulator = new ATMSimulator();
		
		try {
			simulator.startClient();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		while(true){
			System.out.println();
			System.out.println("Following operations avalible.");
			System.out.println("1. Title Fetch for Fund Transfer");
			System.out.println("2. Inter Bank Fund Trnasfer");
			System.out.println("3. Wallet Fund Trnasfer");
			System.out.println("0. Exit");
			
			String option = readConsole("Enter Valid Option");
			int optionSelected = -1;
			try{
				optionSelected = Integer.parseInt(option);
			}catch(Exception ex){
				
			}
			
			switch (optionSelected) {
			case 0:
				session.close(true);
				System.exit(0);
				break;
			case 1:
				simulator.doTitleFetch();
				break;
			case 2:
				simulator.doIBFT();
				break;
			case 3:
				simulator.doFundTransfer();
				break;
			default:
				System.out.println("Please enter valid option");
				break;
			}
		}
	}
	
	
	private void doTitleFetch(){
		BBTitleFetchRequest req = new BBTitleFetchRequest();
		req.setPan("1234567890123456");
		req.setStan("123456");
		req.setRrn("123456789012");
		
		req.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		req.setTransactionLocalTime(DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		req.setTransactionLocalDate(DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue()));
		
		req.setAccountNo1(readConsole("Enter Account No 1"));
		req.setAccountNo2(readConsole("Enter Account No 2"));
		req.setTransactionAmount(FormatUtils.parseI8Amount(readConsole("Enter Amount")));
		
		
		req.setProcessingCode(TransactionCodeEnum.JS_BB_TITLE_FETCH_CODE.getValue() + ATMSimulator.randInt(0, 2) + "000");
		req.assemble();
		sendMessage(req);
	}
	
	private void doFundTransfer(){
		
		BBFundTransferAdviceRequest req = new BBFundTransferAdviceRequest();
		
		req.setPan("1234567890123456");
		req.setStan(readConsole("Enter STAN"));
		req.setRrn("123456789012");
		
		req.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		
		
		String date = readConsole("Enter Transaction Date (MMdd)");
		if(StringUtils.isEmpty(date)){
			date = DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue());
		}
		req.setTransactionLocalDate(date);

		
		String time = readConsole("Enter Transaction Time Format (hhmmss)");
		if(StringUtils.isEmpty(time)){
			time = DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue());
		}
		req.setTransactionLocalTime(time);
		
		req.setAccountNo1(readConsole("Enter Account 1 (Core)"));
		req.setAccountNo2(readConsole("Enter Account 2 (BB)"));
		req.setTransactionAmount(FormatUtils.parseI8Amount(readConsole("Enter Amount")));
		
		req.setProcessingCode(TransactionCodeEnum.JS_BB_ACCOUNT_FT_ADVICE_CODE.getValue() + ATMSimulator.randInt(0, 2) + "000");
		req.assemble();
		sendMessage(req);
	}
	
	
	private void doIBFT(){
		BBFundTransferAdviceRequest req = new BBFundTransferAdviceRequest();
		
		req.setPan("1234567890123456");
		req.setStan(readConsole("Enter STAN"));
		req.setRrn("123456789012");
		
		req.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		
		
		String date = readConsole("Enter Transaction Date (MMdd)");
		if(StringUtils.isEmpty(date)){
			date = DateTools.currentDateToString(MiddlewareEnum.DATE_LOCAL_TRANSACTION_DATE_FORMAT.getValue());
		}
		req.setTransactionLocalDate(date);

		
		String time = readConsole("Enter Transaction Time Format (hhmmss)");
		if(StringUtils.isEmpty(time)){
			time = DateTools.currentDateToString(MiddlewareEnum.TIME_LOCAL_TRANSACTION_DATE_FORMAT.getValue());
		}
		req.setTransactionLocalTime(time);
		
		req.setAccountNo1(readConsole("Enter Account 1 (Core)"));
		req.setAccountNo2(readConsole("Enter Account 2 (BB)"));
		req.setTransactionAmount(FormatUtils.parseI8Amount(readConsole("Enter Amount")));
		
		req.setProcessingCode(TransactionCodeEnum.JS_BB_IBFT_WALLET_CREDIT_ADVICE_CODE.getValue() + ATMSimulator.randInt(0, 2) + "000");
		req.assemble();
		sendMessage(req);
	}
	
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	private static String readConsole(String message){
		try{
			System.out.print(message + ": ");
			return console.readLine();
		}catch(Exception ex){
			
		}
		return null;
	}
	
	private static void initConsole()throws Exception{
		// Initialize Console
		console = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void sendMessage(BasePDU pdu) {
		session.write(pdu);
	}

	public void startClient() throws Throwable {
		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(0);

		ProtocolCodecFilter codec = new ProtocolCodecFilter(new ProtocolCodecFactory() {

			RequestEncoder encoder = new RequestEncoder();
			ResponseDecoder decoder = new ResponseDecoder();

			@Override
			public ProtocolEncoder getEncoder(IoSession session) throws Exception {
				return encoder;
			}

			@Override
			public ProtocolDecoder getDecoder(IoSession session) throws Exception {
				return decoder;
			}
		});

		connector.getFilterChain().addLast("codec", codec);

		// connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.setHandler(new SimulatorClientHandler());
		

		for (;;) {
			try {
				ConnectFuture future = connector.connect(new InetSocketAddress(HOSTNAME, PORT));
				future.awaitUninterruptibly();
				session = future.getSession();
				break;
			} catch (RuntimeIoException e) {
				System.err.println("Failed to connect.");
				e.printStackTrace();
				Thread.sleep(5000);
			}
		}

		// wait until the summation is done
//		session.getCloseFuture().awaitUninterruptibly();
//		connector.dispose();
	}
}

class SimulatorClientHandler extends IoHandlerAdapter {

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		super.messageReceived(session, message);
		
		ATMSimulator.waitForResponse = false;
		byte[] packet = (byte[])message;
		System.out.println(new String(packet));
		ISO8583MessageParser.parse(packet);
	}
	
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		/*EchoRequest echoRequest = new EchoRequest();
		echoRequest.setStan("012345");
		echoRequest.setNetworkManagementCode("270");
		echoRequest.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		echoRequest.build();
		session.write(echoRequest);*/
	}
	
	@Override
	public void sessionOpened(IoSession session) {
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
		((SocketSessionConfig) session.getConfig()).setTcpNoDelay(true);
		((SocketSessionConfig) session.getConfig()).setKeepAlive(true);
		((SocketSessionConfig) session.getConfig()).setReuseAddress(true);
		session.getConfig().setUseReadOperation(true);
		
		/*EchoRequest echoRequest = new EchoRequest();
		echoRequest.setStan("012345");
		echoRequest.setNetworkManagementCode("001");
		echoRequest.setTransactionDate(DateTools.currentDateToString(MiddlewareEnum.TRANSACTION_DATE_FORMAT.getValue()));
		echoRequest.build();
		session.write(echoRequest);*/
	}

}

class RequestEncoder implements ProtocolEncoder {

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		BasePDU pdu = (BasePDU) message;

		String hexArray = Integer.toHexString(pdu.getRawPdu().length);
		hexArray = StringUtils.leftPad(hexArray, 8, '0');
		byte[] metaLengthBytes = CommonUtils.hexStringToByteArray(hexArray);
		IoBuffer buffer = IoBuffer.allocate(metaLengthBytes.length + pdu.getRawPdu().length, false);
		buffer.setAutoExpand(true);
		buffer.setAutoShrink(true);
		buffer.put(metaLengthBytes);
		buffer.put(pdu.getRawPdu());

		String s2 = ISOUtil.hexString(pdu.getRawPdu());
		System.out.println("Hexdump ISO 8583:\n" + s2);
		buffer.flip();
		out.write(buffer);
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		session.close(true);
	}

}

class ResponseDecoder extends CumulativeProtocolDecoder {

	private static final String DECODER_STATE_KEY = ResponseDecoder.class.getName() + ".STATE";

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
			return false;
		}

		byte[] packetByte = null;

		packetByte = new byte[decoderState.packetLength];
		io.get(packetByte);
		decoderState.packetLength = -1;
		out.write(packetByte);

		return true;
	}
	
	

}