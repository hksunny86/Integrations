package com.inov8;

import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.EncryptionUtil;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("all")
public class RDVMBRegressionTest {
    private static Logger logger = LoggerFactory.getLogger(RDVMBRegressionTest.class);
    private static I8SBSwitchController controller = null;
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
        long startTime = new Date().getTime();

        System.out.println("########################################");
        System.out.println("Middleware Integration Simulator.");
        System.out.println("########################################");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        List<Callable<Void>> threads = new ArrayList<>();
        final StringBuffer stringBuffer = new StringBuffer();
        boolean incorrectPwd = true;
        int count = 0;
        Console con = System.console();
        try {
            System.out.println("Enter Sending request ");
//            String server = bufferRead.readLine();
//            if (StringUtils.isEmpty(server)) {
//                server = "127.0.0.1:9090";
//            }
            String server = "127.0.0.1:9090";
            getFromProxy(server);
        } catch (IOException e) {
            logger.error("Exception", e);
        } catch (Exception e) {
            logger.error("Exception", e);
        }


        for (int i = 0; i < 1; i++) {
             int finalI = i;
            threads.add(
                    new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            try {
                                /*I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                                requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
                                requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
                                requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
                                requestVO.setRequestType(I8SBConstants.RequestType_LDAPRequest);
                                final long start = System.currentTimeMillis();
                                requestVO.setUserId("1234567890123992");
                                requestVO.setCNIC("1234567890123992");
                                requestVO.setRelationshipNumber("555555555");
                                requestVO.setPasswordBitmap(String.valueOf(Thread.currentThread().getId()));
                                requestVO.setPassword(String.valueOf(Thread.currentThread().getId() * 56));
                                requestVO.setAccountId1(RDVWSEncryption.encrypt("123456789"));
                                requestVO.setAccountId2(RDVWSEncryption.encrypt("1234567891"));
                                requestVO.setAmount("500");
                                requestVO.setFPIN(RDVWSEncryption.encrypt("1234"));
                                requestVO.setTransactionFees("100");
                                requestVO.setActivityFlag("S");
                                requestVO.setUserName("admin");
                                requestVO.setPassword("inov8@com");
                                requestVO.setDomainName("testdomain.com");
                                requestVO = (I8SBSwitchControllerRequestVO) controller.invoke(requestVO);*/

                                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                                final long start = System.currentTimeMillis();

                                requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
                                requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
                                requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_RDV_MB);
//                                requestVO.setRequestType(I8SBConstants.RequestType_CNICValidation);

                                requestVO.setRequestType(I8SBConstants.RequestType_SendSMS);

                                requestVO.setUserId("1234567890123992");
                                requestVO.setRelationshipNumber("555555555");
                                requestVO.setPasswordBitmap("23564");
                                requestVO.setPassword("222555888");
                                requestVO.setPAN("SazZ78aFcoInrppOjKSFi8wszzJ32doOv+MDyr0pTvU=");
                                requestVO.setPinData("6tBH5Et3C3b9p7Xzr1YVIQ==");
                                requestVO.setCNIC("3840112345678");
                                requestVO.setMobilePhone("03414937631");
                                requestVO.setSmsText("SazZ78aFcoInrppOjKSFi8wszzJ32doOv+MDyr0pTvU=");
                                requestVO.setSmsTransactionNature("N");
                                requestVO.setSmsTransactionType("FPIN");
                                requestVO.setDateOfBirth(DateTime.now().toString());
                                try {
                                    requestVO = (I8SBSwitchControllerRequestVO) controller.invoke(requestVO);
                                }catch (Exception e)
                                {
                                    System.out.print(e.getMessage());
                                }


                                stringBuffer.append("User Id" + requestVO.getUserId() + " response Code :" + requestVO.getI8SBSwitchControllerResponseVO().getResponseCode() + " done in " + (System.currentTimeMillis() - start) + " (ms) \n");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }

            );
        }
        ExecutorService es = Executors.newFixedThreadPool(90);

        try {
            es.invokeAll(threads);
            es.shutdown();
            long endTime = new Date().getTime();
            long difference = (endTime - startTime) / 1000;
            System.out.println(stringBuffer.toString());
            System.out.println("Processed in " + difference + "seconds");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public static void getFromProxy(String server) throws Exception {
        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        httpInvokerProxyFactoryBean.setServiceInterface(I8SBSwitchController.class);
        httpInvokerProxyFactoryBean.setServiceUrl("http://" + server + "/I8SB/remote/api");
        httpInvokerProxyFactoryBean.afterPropertiesSet();
        controller = (I8SBSwitchController) httpInvokerProxyFactoryBean.getObject();
    }
}