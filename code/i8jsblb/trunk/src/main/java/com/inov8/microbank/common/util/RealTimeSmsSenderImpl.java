package com.inov8.microbank.common.util;

import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.SmsLogModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.NovaAlertMessage;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.verifly.server.dao.logmodule.SmsLogDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.web.context.ContextLoader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RealTimeSmsSenderImpl implements RealTimeSmsSender {
    private final Log logger = LogFactory.getLog(this.getClass());
    private String url;
    private String defaultUrl;
    private Map<String, String> urls;

    //private String ;
    private SwitchController switchController;
    private ESBAdapter esbAdapter;
    private CommonCommandManager commonCommandmanager;

    private SmsLogDao logDAO;
    private OracleSequenceMaxValueIncrementer oracleSequenceMaxValueIncrementer;

    public RealTimeSmsSenderImpl() {

    }

    public static void main(String[] args) {
        String cellNumber = "00923214084099";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");


        String issuanceDate = "2016-10-23";
        try {
            Date formatIssuanceDate = dateFormat.parse(issuanceDate);
            System.out.println("Issuance Date" + formatIssuanceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        StringBuffer sbf = new StringBuffer();
        sbf.append(cellNumber);
        if (cellNumber.startsWith("+92") && cellNumber.length() == 13 && StringUtil.isInteger(cellNumber.substring(3))) {

        } else if (cellNumber.startsWith("0092") && cellNumber.length() == 14 && StringUtil.isInteger(cellNumber)) {
        } else if (cellNumber.startsWith("3") && cellNumber.length() == 10 && StringUtil.isInteger(cellNumber)) {
            cellNumber = "0092" + cellNumber;
        } else if (cellNumber.startsWith("92") && cellNumber.length() == 12 && StringUtil.isInteger(cellNumber)) {
            cellNumber = "00" + cellNumber;
        } else if (cellNumber.startsWith("03") && cellNumber.length() == 11 && StringUtil.isInteger(cellNumber)) {
            cellNumber = cellNumber.replaceFirst("0", "0092");
        }

        System.out.println("The formatted cell number is " + cellNumber);


        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:WEB-INF/server-messaging.xml");

        RealTimeSmsSender smsSender = (RealTimeSmsSenderImpl) factory.getBean("realTimeSmsSender");
        smsSender.send(new SmsMessage("03214084099", "Testing", "03214084099"));

    }

    /* (non-Javadoc)
     * @see com.inov8.microbank.common.util.RealTimeSmsSender#send(com.inov8.microbank.common.model.messagemodule.SmsMessage)
     */
    public void send(SmsMessage smsMessage) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start send() of RealTimeSmsSenderImpl");
        }
        String to = null;
        String msg = null;
        String from = null;

        //setting the sender number to default value
        url = defaultUrl;

        // retreiving the sender if set by client
        if (smsMessage.getSender() != null) {
            String tempUrl = urls.get(smsMessage.getSender());
            if (tempUrl != null)
                url = tempUrl;
        }
        if (smsMessage.getFrom() != null) {
            from = smsMessage.getFrom();
        }

        // ....................

        to = smsMessage.getMobileNo();
        to = this.formatNumberWith92(to);
        if (!StringUtil.isInteger(to)) {
            logger.warn("'Mobile No' is not valid!!! ignoring this SMS");
            return;
        }


        msg = smsMessage.getMessageText();
        UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
        if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
            String message = MessageParsingUtils.parseMessageForIpos(smsMessage.getMessageText());
            smsMessage.setMessageText(message);
        }

        try {

            Long messageId = this.oracleSequenceMaxValueIncrementer.nextLongValue();
            logger.info("Sequence for SMS log..." + messageId);

            SmsLogModel model = new SmsLogModel();
            model.setMessageId(messageId.toString());
            model.setSentDateTime(new Date());
            model.setRecipientMobileNumber(to);
            model.setSmsLogId(messageId);
            logDAO.saveOrUpdate(model);

            this.getSwitchController();
            PhoenixIntegrationMessageVO messageVO = new PhoenixIntegrationMessageVO();
            messageVO.setMessageType(msg);
            messageVO.setMobileNumber(to);
            messageVO.setTransactionId(messageId.toString());
            logger.info("Calling M3 SMS Integration Service.......");
            if (MessageUtil.getMessage("SENDSMS.CHANNEL").equals("0")) {
                if (MessageUtil.getMessage("").equals(""))
                    messageVO = (PhoenixIntegrationMessageVO) switchController.transaction(messageVO);
                logger.info("Response recieved from SMS Integration Service : " + messageVO.getResponseCode());
                if (StringUtil.isNullOrEmpty(messageVO.getResponseCode()) || !messageVO.getResponseCode().equals("0")) {
                    logger.error("Sms sending failed (invalid response code)");
                    throw new RuntimeException("SMS sending failed");
                }
            } else if (MessageUtil.getMessage("SENDSMS.CHANNEL").equals("2")) {
                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                requestVO = ESBAdapter.prepareM3TechSmsRequest(I8SBConstants.RequestType_SendSMS);
                requestVO.setRecieverMobileNo(this.formatNumberWith0092(to));
                requestVO.setSmsText(msg);
                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

                if (!responseVO.getResponseCode().equals("I8SB-200")) {
                    throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                }
            } else if (MessageUtil.getMessage("SENDSMS.CHANNEL").equals("3")) {
                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                requestVO = ESBAdapter.prepareEoceanRequest(I8SBConstants.RequestType_EOCEAN);
                requestVO.setRecieverMobileNo(this.formatNumberWith092(to));
                requestVO.setSmsText(msg);
                String mobileNumber = this.formatNumberWith092(to);
                String shortCode = mobileNumber.substring(1, 4);
                String mobilink = MessageUtil.getMessage("Mobilink.Short.codes");
                List<String> mobilinkShortCode = Arrays.asList(mobilink.split("\\s*,\\s*"));
                String ufone = MessageUtil.getMessage("ufone.short.codes");
                List<String> ufoneShortCode = Arrays.asList(ufone.split("\\s*,\\s*"));
                String telenor = MessageUtil.getMessage("telenor.short.codes");
                List<String> telenorShortCodes = Arrays.asList(telenor.split("\\s*,\\s*"));
                String zong = MessageUtil.getMessage("zong.short.codes");
                List<String> zongShortCode = Arrays.asList(zong.split("\\s*,\\s*"));
                String scom = MessageUtil.getMessage("scom.short.codes");
                List<String> scomShortCode = Arrays.asList(scom.split("\\s*,\\s*"));
                if (mobilinkShortCode.contains(shortCode)) {
                    requestVO.setMobileNetwork(MessageUtil.getMessage("Mobilink.operator.name"));
                } else if (ufoneShortCode.contains(shortCode)) {
                    requestVO.setMobileNetwork(MessageUtil.getMessage("ufone.operator.name"));
                } else if (telenorShortCodes.contains(shortCode)) {
                    requestVO.setMobileNetwork(MessageUtil.getMessage("telenor.operator.name"));
                } else if (zongShortCode.contains(shortCode)) {
                    requestVO.setMobileNetwork(MessageUtil.getMessage("zong.operator.name"));

                } else if (scomShortCode.contains(shortCode)) {
                    requestVO.setMobileNetwork(MessageUtil.getMessage("scom.operator.name"));

                }

//                AppUserModel appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(this.formatNumberWith092(to), UserTypeConstantsInterface.CUSTOMER);
//                if (appUserModel != null) {
//                    if (appUserModel.getCustomerMobileNetwork() != null) {
//                        if (appUserModel.getCustomerMobileNetwork().equalsIgnoreCase("jazz") || appUserModel.getCustomerMobileNetwork().equalsIgnoreCase("warid")) {
//                            requestVO.setMobileNetwork("Mobilink");
//                        }else {
//                            requestVO.setMobileNetwork(appUserModel.getCustomerMobileNetwork());
//                        }
//                    } else {
//                        requestVO.setMobileNetwork("Zong");
//                    }
//                } else {
//                    requestVO.setMobileNetwork("Zong");
//                }
                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

                if (!responseVO.getResponseCode().equals("I8SB-200")) {
                    throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                }
            } else if (MessageUtil.getMessage("SENDSMS.CHANNEL").equals("2")) {
                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                requestVO = ESBAdapter.prepareM3TechSmsRequest(I8SBConstants.RequestType_SendSMS);
                requestVO.setRecieverMobileNo(this.formatNumberWith0092(to));
                requestVO.setSmsText(msg);
                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

                if (!responseVO.getResponseCode().equals("I8SB-200")) {
                    throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                }
            }
//            } else {
//                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
//                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
//                requestVO = ESBAdapter.prepareEoceanNewRequest(I8SBConstants.RequestType_SendSMS);
//                requestVO.setRecieverMobileNo(this.formatNumberWith092(to));
//                requestVO.setSmsText(msg);
//                String mobileNumber = this.formatNumberWith092(to);
//                String shortCode = mobileNumber.substring(1, 4);
//                String mobilink = MessageUtil.getMessage("Mobilink.Short.codes");
//                List<String> mobilinkShortCode = Arrays.asList(mobilink.split("\\s*,\\s*"));
//                String ufone = MessageUtil.getMessage("ufone.short.codes");
//                List<String> ufoneShortCode = Arrays.asList(ufone.split("\\s*,\\s*"));
//                String telenor = MessageUtil.getMessage("telenor.short.codes");
//                List<String> telenorShortCodes = Arrays.asList(telenor.split("\\s*,\\s*"));
//                String zong = MessageUtil.getMessage("zong.short.codes");
//                List<String> zongShortCode = Arrays.asList(zong.split("\\s*,\\s*"));
//                String scom = MessageUtil.getMessage("scom.short.codes");
//                List<String> scomShortCode = Arrays.asList(scom.split("\\s*,\\s*"));
//                if (mobilinkShortCode.contains(shortCode)) {
//                    requestVO.setMobileNetwork(MessageUtil.getMessage("Mobilink.operator.name"));
//                } else if (ufoneShortCode.contains(shortCode)) {
//                    requestVO.setMobileNetwork(MessageUtil.getMessage("ufone.operator.name"));
//                } else if (telenorShortCodes.contains(shortCode)) {
//                    requestVO.setMobileNetwork(MessageUtil.getMessage("telenor.operator.name"));
//                } else if (zongShortCode.contains(shortCode)) {
//                    requestVO.setMobileNetwork(MessageUtil.getMessage("zong.operator.name"));
//
//                } else if (scomShortCode.contains(shortCode)) {
//                    requestVO.setMobileNetwork(MessageUtil.getMessage("scom.operator.name"));
//
//                }
//
////                AppUserModel appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(this.formatNumberWith092(to), UserTypeConstantsInterface.CUSTOMER);
////                if (appUserModel != null) {
////                    if (appUserModel.getCustomerMobileNetwork() != null) {
////                        if (appUserModel.getCustomerMobileNetwork().equalsIgnoreCase("jazz") || appUserModel.getCustomerMobileNetwork().equalsIgnoreCase("warid")) {
////                            requestVO.setMobileNetwork("Mobilink");
////                        }else {
////                            requestVO.setMobileNetwork(appUserModel.getCustomerMobileNetwork());
////                        }
////                    } else {
////                        requestVO.setMobileNetwork("Zong");
////                    }
////                } else {
////                    requestVO.setMobileNetwork("Zong");
////                }
//                SwitchWrapper sWrapper = new SwitchWrapperImpl();
//                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
//                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
//                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
//                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
//                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
//
//                if (!responseVO.getResponseCode().equals("I8SB-200")) {
//                    throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
//                }
//            }

        } catch (Exception ex) {
            logger.error("Sms sending failed due to Exception...", ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End send() of RealTimeSmsSenderImpl");
        }
    }


    @Override
    public void novaAlertSMS(NovaAlertMessage smsMessage) {
        logger.info("SmsSenderImpl.pushNotification() request sent to I8SB");
        SwitchWrapper sWrapper = new SwitchWrapperImpl();
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        try {
            requestVO = esbAdapter.prepareRefferalCustomerRequest(I8SBConstants.RequestType_Notification);
            requestVO.setMobileNumber(smsMessage.getMobileNo());
            requestVO.setSmsText(smsMessage.getText());
            sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
            sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
            sWrapper = esbAdapter.makeI8SBCall(sWrapper);
            ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
            responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

            if (!responseVO.getResponseCode().equals("I8SB-200")) {
                throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
            }
        } catch (Exception ex) {
            logger.error("Notifcation sending failed due to Exception...", ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End send() of RealTimeSmsSenderImpl");
        }
    }

    private void getSwitchController() {
        switchController = HttpInvokerUtil.getHttpInvokerFactoryBean(SwitchController.class, defaultUrl);
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    public void setDefaultUrl(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getUrls() {
        return urls;
    }

    public void setUrls(Map<String, String> urls) {
        this.urls = urls;
    }

    private String formatNumberWith92(String cellNumber) {
        if (null != cellNumber && !"".equalsIgnoreCase(cellNumber)) {
//			mobNum = mobNum.replaceFirst("0", "0092");
            if (cellNumber.startsWith("+92") && cellNumber.length() == 13 && StringUtil.isInteger(cellNumber.substring(3))) {

            } else if (cellNumber.startsWith("0092") && cellNumber.length() == 14 && StringUtil.isInteger(cellNumber)) {
            } else if (cellNumber.startsWith("3") && cellNumber.length() == 10 && StringUtil.isInteger(cellNumber)) {
                cellNumber = "0092" + cellNumber;
            } else if (cellNumber.startsWith("92") && cellNumber.length() == 12 && StringUtil.isInteger(cellNumber)) {
                cellNumber = "00" + cellNumber;
            } else if (cellNumber.startsWith("03") && cellNumber.length() == 11 && StringUtil.isInteger(cellNumber)) {
                cellNumber = cellNumber.replaceFirst("0", "0092");
            }

        }

        return cellNumber;
    }

    private String formatNumberWith092(String cellNumber) {
        if (null != cellNumber && !"".equalsIgnoreCase(cellNumber)) {
//			mobNum = mobNum.replaceFirst("0", "0092");
            if (cellNumber.startsWith("+92") && cellNumber.length() == 13 && StringUtil.isInteger(cellNumber.substring(3))) {

            } else if (cellNumber.startsWith("0092") && cellNumber.length() == 14 && StringUtil.isInteger(cellNumber)) {
                cellNumber = cellNumber.replaceFirst("0092", "0");
            } else if (cellNumber.startsWith("3") && cellNumber.length() == 10 && StringUtil.isInteger(cellNumber)) {
                cellNumber = "0092" + cellNumber;
            } else if (cellNumber.startsWith("92") && cellNumber.length() == 12 && StringUtil.isInteger(cellNumber)) {
                cellNumber = "00" + cellNumber;
            } else if (cellNumber.startsWith("03") && cellNumber.length() == 11 && StringUtil.isInteger(cellNumber)) {
                cellNumber = cellNumber.replaceFirst("0", "0092");
            }

        }

        return cellNumber;
    }

    private String formatNumberWith0092(String cellNumber) {
        if (null != cellNumber && !"".equalsIgnoreCase(cellNumber)) {
//			mobNum = mobNum.replaceFirst("0", "0092");
            if (cellNumber.startsWith("+92") && cellNumber.length() == 13 && StringUtil.isInteger(cellNumber.substring(3))) {

            } else if (cellNumber.startsWith("0092") && cellNumber.length() == 14 && StringUtil.isInteger(cellNumber)) {
                cellNumber = cellNumber.replaceFirst("0092", "92");
            } else if (cellNumber.startsWith("3") && cellNumber.length() == 10 && StringUtil.isInteger(cellNumber)) {
                cellNumber = "0092" + cellNumber;
            } else if (cellNumber.startsWith("92") && cellNumber.length() == 12 && StringUtil.isInteger(cellNumber)) {
                cellNumber = "00" + cellNumber;
            } else if (cellNumber.startsWith("03") && cellNumber.length() == 11 && StringUtil.isInteger(cellNumber)) {
                cellNumber = cellNumber.replaceFirst("0", "0092");
            }

        }

        return cellNumber;
    }

    public void setOracleSequenceMaxValueIncrementer(OracleSequenceMaxValueIncrementer oracleSequenceMaxValueIncrementer) {
        this.oracleSequenceMaxValueIncrementer = oracleSequenceMaxValueIncrementer;
    }

    public SmsLogDao getLogDAO() {
        return logDAO;
    }

    public void setLogDAO(SmsLogDao logDAO) {
        this.logDAO = logDAO;
    }


    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }
}
