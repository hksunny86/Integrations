package com.inov8.test;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.hsm.controller.IPayShieldSwitchController;
import com.inov8.hsm.dto.PayShieldDTO;

@SuppressWarnings("all")
public class ConsoleRDVIntegration {
	private static Logger logger = LoggerFactory.getLogger(ConsoleRDVIntegration.class);
	private static IPayShieldSwitchController controller = null;
	private static final int MAX_LOGINS = 3;

	// private static String server = "127.0.0.1:8080";

	private static String readLine(String format, Object... args) throws IOException {
		if (System.console() != null) {
			return System.console().readLine(format, args);
		}
		System.out.print(String.format(format, args));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		return reader.readLine();
	}

	private static char[] readPassword(String format, Object... args) throws IOException {
		if (System.console() != null)
			return System.console().readPassword(format, args);
		return readLine(format, args).toCharArray();
	}

	public static void main(String[] args) {
		String fmt = "%1$4s %2$5s %3$10s%n";

		System.out.println("########################################");
		System.out.println("Welcome to PayShield HSM Engine Simulator.");
		System.out.println("########################################");

		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		boolean incorrectPwd = true;
		int count = 0;
		Console con = System.console();
		try {
			if (con == null) {
				System.out.println("Enter Integration Server IP:Port ");
				String server = bufferRead.readLine();
				if (StringUtils.isEmpty(server)) {
					server = "127.0.0.1:8080";
				}
				getFromProxy(server);
				phoenixInteractive();
			} else {

				while (con != null && incorrectPwd && count++ < MAX_LOGINS) {

					System.out.println("Enter Integration Server IP:Port ");
					String server = bufferRead.readLine();
					if (StringUtils.isEmpty(server)) {
						server = "127.0.0.1:8080";
					}
					getFromProxy(server);
					phoenixInteractive();

				}
			}
		} catch (IOException e) {
			logger.error("Exception",e);
		} catch (Exception e) {
			logger.error("Exception",e);
		}
	}

	public static void phoenixInteractive() {

		boolean flag = true;
		while (flag) {
			System.out.println("Transaction Menu: ");
			System.out.println("\t1. Generate System PIN");
			System.out.println("\t2. Generate User PIN");
			System.out.println("\t3. Verify PIN");
			System.out.println("\t4. Change System PIN");
			System.out.println("\t5. Change User PIN");
			System.out.println("\t0. Exit");

			int option;
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String str = "";
			try {
				str = bufferRead.readLine();
			} catch (IOException e) {
				logger.error("Exception",e);
			}

			option = Integer.parseInt(str);

			try {
				switch (option) {
				case 0: {
					flag = false;
					bufferRead.close();
					System.out.println("Goodbye...");
					System.exit(1);
				}
				case 1: {
					System.out.println("You are in Generate System PIN");
					System.out.print("Enter PAN: ");
					final String PAN = bufferRead.readLine();

					Thread thread = new Thread(new Runnable() {
						
						@Override
						public void run() {
							generateSystemPIN(PAN);
						}
					});
					
					ExecutorService executorService = Executors.newFixedThreadPool(10);
					executorService.submit(thread);
					executorService.submit(thread);
					executorService.submit(thread);
					executorService.submit(thread);
					executorService.submit(thread);
					executorService.submit(thread);
					
					
					executorService.shutdown();
					
					flag = askMore(bufferRead);
				}
					break;
				case 2: {
					System.out.println("You are in Generate User PIN");
					System.out.print("Enter PAN: ");
					String PAN = bufferRead.readLine();
					
					System.out.print("Enter PIN: ");
					String PIN = bufferRead.readLine();

					generateUserPIN(PAN, PIN);
					flag = askMore(bufferRead);
				}
				case 3: {
					System.out.println("You are in Verify PIN");
					System.out.print("Enter PAN: ");
					final String PAN = bufferRead.readLine();
					
					System.out.print("Enter PIN: ");
					final String PIN = bufferRead.readLine();
					
					System.out.print("Enter PVV: ");
					final String PVV = bufferRead.readLine();
					
					
					Thread thread = new Thread(new Runnable() {
						
						@Override
						public void run() {
							verifyPIN(PAN, PIN, PVV);
						}
					});
					
					ExecutorService executorService = Executors.newFixedThreadPool(10);
					executorService.submit(thread);
					executorService.submit(thread);
					executorService.submit(thread);
					executorService.submit(thread);
					executorService.submit(thread);
					executorService.submit(thread);
					
					
					executorService.shutdown();

					
					flag = askMore(bufferRead);
				}
					break;
				case 4: {
					System.out.println("You are in Change And Genrate System PIN");
					System.out.print("Enter PAN: ");
					String PAN = bufferRead.readLine();
					
					System.out.print("Enter OLD PIN: ");
					String oldPIN = bufferRead.readLine();
					
					System.out.print("Enter OLD PVV: ");
					String oldPVV = bufferRead.readLine();

					changeSystemPIN(PAN, oldPIN, oldPVV);
					flag = askMore(bufferRead);
				}
					break;
				case 5: {
					System.out.println("You are in Change User PIN");
					System.out.print("Enter PAN: ");
					String PAN = bufferRead.readLine();
					
					System.out.print("Enter Old PIN: ");
					String PIN = bufferRead.readLine();
					
					System.out.print("Enter Old PVV: ");
					String PVV = bufferRead.readLine();
					
					System.out.print("Enter New PIN: ");
					String newPIN = bufferRead.readLine();

					changeUserPIN(PAN, PIN, PVV, newPIN);
					flag = askMore(bufferRead);
				}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				logger.error("Exception",e);
			}

		}

	}




	private static void changeUserPIN(String PAN, String oldPIN, String oldPVV, String newPIN) {
		PayShieldDTO payShieldDTO = new PayShieldDTO();
		payShieldDTO.setPAN(PAN);
		payShieldDTO.setOldPIN(oldPIN);
		payShieldDTO.setOldPVV(oldPVV);
		payShieldDTO.setPIN(newPIN);
	
		try {
			payShieldDTO = (PayShieldDTO) controller.changeUserPIN(payShieldDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (payShieldDTO.getResponseCode().equals("00")) {

			logger.info("******* DEBUG LOGS FOR Change User PIN *********");
			logger.info("RRN: " + payShieldDTO.getUPID());
			logger.info("<< Response >> " + payShieldDTO.print(true));

		} else {
			logger.info("ResponseCode: " + payShieldDTO.getResponseCode());
			logger.info("<< Response >> " + payShieldDTO.print(true));
		}
		
	}

	private static void changeSystemPIN(String PAN, String oldPIN, String oldPVV) {
		PayShieldDTO payShieldDTO = new PayShieldDTO();
		payShieldDTO.setPAN(PAN);
		payShieldDTO.setOldPIN(oldPIN);
		payShieldDTO.setOldPVV(oldPVV);
	
		try {
			payShieldDTO = (PayShieldDTO) controller.changeSystemPIN(payShieldDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (payShieldDTO.getResponseCode().equals("00")) {

			logger.info("******* DEBUG LOGS FOR Change System PIN *********");
			logger.info("RRN: " + payShieldDTO.getUPID());
			logger.info("<< Response >> " + payShieldDTO.print(true));

		} else {
			logger.info("ResponseCode: " + payShieldDTO.getResponseCode());
			logger.info("<< Response >> " + payShieldDTO.print(true));
		}
		
	}

	private static void verifyPIN(String PAN, String PIN, String PVV) {
		PayShieldDTO payShieldDTO = new PayShieldDTO();
		payShieldDTO.setPAN(PAN);
		payShieldDTO.setPIN(PIN);
		payShieldDTO.setPVV(PVV);
		
		try {
			payShieldDTO = (PayShieldDTO) controller.verifyPIN(payShieldDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (payShieldDTO.getResponseCode().equals("00")) {

			logger.info("******* DEBUG LOGS FOR Verify PIN *********");
			logger.info("RRN: " + payShieldDTO.getUPID());
			logger.info("<< Response >> " + payShieldDTO.print(true));

		} else {
			logger.info("ResponseCode: " + payShieldDTO.getResponseCode());
			logger.info("<< Response >> " + payShieldDTO.print(true));
		}
		
	}

	private static void generateUserPIN(String PAN, String PIN) {
		PayShieldDTO payShieldDTO = new PayShieldDTO();
		payShieldDTO.setPAN(PAN);
		payShieldDTO.setPIN(PIN);
		
		try {
			payShieldDTO = (PayShieldDTO) controller.generateUserPIN(payShieldDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (payShieldDTO.getResponseCode().equals("00")) {

			logger.info("******* DEBUG LOGS FOR Generate User PIN *********");
			logger.info("RRN: " + payShieldDTO.getUPID());
			logger.info("<< Response >> " + payShieldDTO.print(true));

		} else {
			logger.info("ResponseCode: " + payShieldDTO.getResponseCode());
			logger.info("<< Response >> " + payShieldDTO.print(true));
		}
	}

	public static void generateSystemPIN(String PAN) {
		PayShieldDTO payShieldDTO = new PayShieldDTO();
		payShieldDTO.setPAN(PAN);
		
		try {
			payShieldDTO = (PayShieldDTO) controller.generateSystemPIN(payShieldDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (payShieldDTO.getResponseCode().equals("00")) {

			logger.info("******* DEBUG LOGS FOR Generate System PIN *********");
			logger.info("RRN: " + payShieldDTO.getUPID());
			logger.info("<< Response >> " + payShieldDTO.print(true));
		} else {
			logger.info("ResponseCode: " + payShieldDTO.getResponseCode());
			logger.info("<< Response >> " + payShieldDTO.print(true));
		}

	}


	public static boolean askMore(BufferedReader bufferRead) throws Exception {
		System.out.println("If you want to continue, enter 1");
		System.out.println("OR");
		System.out.println("enter 0 to exit the System");
		String str = bufferRead.readLine();
		int option = Integer.parseInt(str);
		if (option == 0) {
			return false;
		} else {
			return true;
		}
	}

	public static void getFromContext() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		controller = (IPayShieldSwitchController) context.getBean("middlewareswitch");
	}

	public static void getFromProxy(String server) throws Exception {
		HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
		httpInvokerProxyFactoryBean.setServiceInterface(IPayShieldSwitchController.class);
		httpInvokerProxyFactoryBean.setServiceUrl("http://" + server + "/paysheild-engine/ws/payshield-switch");
		httpInvokerProxyFactoryBean.afterPropertiesSet();
		controller = (IPayShieldSwitchController) httpInvokerProxyFactoryBean.getObject();
	}
}