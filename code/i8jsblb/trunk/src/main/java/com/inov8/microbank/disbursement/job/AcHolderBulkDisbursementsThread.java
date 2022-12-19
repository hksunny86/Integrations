package com.inov8.microbank.disbursement.job;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.TaxRegimeModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.disbursement.service.BulkDisbursementsManager;
import com.inov8.microbank.disbursement.vo.DisbursementVO;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.server.service.advancesalaryloan.dao.AdvanceSalaryLoanDAO;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.tax.service.TaxManager;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
//import org.quartz.DisallowConcurrentExecution;






import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@DisallowConcurrentExecution
public class AcHolderBulkDisbursementsThread extends Thread {

    private static Logger logger = Logger.getLogger(AcHolderBulkDisbursementsThread.class);

    private Integer threadNumber = null;
    private BulkDisbursementsManager bulkDisbursementsManager = null;
    private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;
    private List<DisbursementVO> disbursementVOList = null;
    private SmsSender smsSender = null;
    private WorkFlowWrapper workFlowWrapper;
	private TaxManager taxManager;
    private CommandManager commandManager;
    private AdvanceSalaryLoanDAO advanceSalaryLoanDAO;

    public AcHolderBulkDisbursementsThread(Integer threadNumber, BulkDisbursementsManager bulkDisbursementsManager,
    		CreditAccountQueingPreProcessor creditAccountQueingPreProcessor,
                                           List<DisbursementVO> bulkDisbursementsModelList, WorkFlowWrapper workFlowWrapper,
                                           SmsSender smsSender, TaxManager taxManager, CommandManager commandManager, AdvanceSalaryLoanDAO advanceSalaryLoanDAO) {

        this.threadNumber = threadNumber;
        this.bulkDisbursementsManager = bulkDisbursementsManager;
        this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
        this.disbursementVOList = bulkDisbursementsModelList;
        this.smsSender = smsSender;
        this.workFlowWrapper = workFlowWrapper;
        this.taxManager = taxManager;
        this.commandManager = commandManager;
        this.advanceSalaryLoanDAO = advanceSalaryLoanDAO;
    }

    @Override
    public void run() {
        ArrayList<SmsMessage> smsMessageList = new ArrayList<SmsMessage>(0);

        logger.debug("\n\nThread Started # " + threadNumber + " Records to Perform : " + disbursementVOList.size());
		
        TaxRegimeModel taxRegimeModel = null;
		try {
			taxRegimeModel = taxManager.searchTaxRegimeById(TaxRegimeConstants.FEDERAL);
		} catch (FrameworkCheckedException e1) {
			logger.error("[AcHolderBulkDisbursementsThread] Exception occurred while searchTaxRegimeById: 3", e1);
		}
		
		if(taxRegimeModel == null || taxRegimeModel.getTaxRegimeId() == null){
			logger.error("[AcHolderBulkDisbursementsThread] TaxRegime for ID: 3 not loaded... So this thread is gonna be stop...");
			return;
		}
        

        for (DisbursementVO disbursementVO : disbursementVOList) {

            try {
                long start = System.currentTimeMillis();

                WorkFlowWrapper wrapper = workFlowWrapper.cloneForDisbursement();
				wrapper.setTaxRegimeModel(taxRegimeModel);
                bulkDisbursementsManager.makeAcHolderTransferFund(disbursementVO, wrapper);
                long productId = Long.parseLong(MessageUtil.getMessage("SalaryDisbursementProductId"));
                if(disbursementVO.getProductId().equals(productId)) {
                    AdvanceSalaryLoanModel advanceSalaryLoanModel = new AdvanceSalaryLoanModel();
                    advanceSalaryLoanModel.setMobileNo(disbursementVO.getMobileNo());
                    advanceSalaryLoanModel.setIsCompleted(false);
                    CustomList<AdvanceSalaryLoanModel> list = getCommonCommandManager().getAdvanceSalaryLoanDAO().findByExample(advanceSalaryLoanModel);

                    if (list != null && list.getResultsetList().size() > 0) {
                        wrapper.setLoanAmount(Double.valueOf(list.getResultsetList().get(0).getInstallmentAmount()));

                        BaseWrapper dWrapper = new BaseWrapperImpl();
                        StringBuilder sb = new StringBuilder();
                        String response = null;
                        dWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, disbursementVO.getMobileNo());
                        dWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, wrapper.getLoanAmount());
                        dWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, disbursementVO.getProductId());
                        dWrapper.putObject(CommandFieldConstants.KEY_TXAM, wrapper.getLoanAmount().toString());
                        dWrapper.putObject(CommandFieldConstants.KEY_TPAM, "0");
                        dWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, disbursementVO.getMobileNo());
//                        dWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.ADVANCE_SALARY_LOAN_ID_PROD);
                        dWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, "MOBILE");
                        dWrapper.putObject(CommandFieldConstants.KEY_STAN, "");
                        dWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, "");
                        dWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
                        dWrapper.putObject(CommandFieldConstants.KEY_APP_ID, "2");//Customer Initiated Transaction
                        sb.append("Start of executeAdvanceLoan() in Advanceloan for Product :: ").append(disbursementVO.getProductId().toString());
                        logger.info(sb.toString());
                        response = commandManager.executeCommand(dWrapper, CommandFieldConstants.KEY_CMD_ADVANCE_LOAN_PAYMENT);
                        sb.append("End of executeAdvanceLoan() in Advanceloan for Product :: ").append(disbursementVO.getProductId().toString());
                        sb.append("\n Response :: " + response);
                        logger.info(sb.toString());
                        if (response != null) {
                            if (list != null && list.getResultsetList().size() > 0) {
                                list.getResultsetList().get(0).setLastPaymentDate(new Date());
                                list.getResultsetList().get(0).setUpdatedOn(new Date());
                                list.getResultsetList().get(0).setNoOfInstallmentPaid(list.getResultsetList().get(0).getNoOfInstallmentPaid() + 1);
                                if (list.getResultsetList().get(0).getNoOfInstallment().equals(list.getResultsetList().get(0).getNoOfInstallmentPaid())) {
                                    list.getResultsetList().get(0).setIsCompleted(true);
                                }
                                advanceSalaryLoanDAO.saveOrUpdate(list.getResultsetList().get(0));
                            }
                        }
                    }
                }
                String salaryDisbursementNotification = wrapper.getTransactionModel().getConfirmationMessage();
                smsMessageList.add(new SmsMessage(disbursementVO.getMobileNo(), salaryDisbursementNotification));

                creditAccountQueingPreProcessor.startProcessing(wrapper);

                logger.info("Disbursement Id " + disbursementVO.getDisbursementId() + " Tx Code : " + wrapper.getTransactionCodeModel().getCode() +
                        " Time taken " + (System.currentTimeMillis() - start)/1000.0d  +" seconds.");

            }

            catch (Exception e) {
                logger.error("Exception occurred on A/c holder Disbursement for Mobile : " + disbursementVO.getMobileNo());
                e.printStackTrace();
            }
        }

        try {
            if(CollectionUtils.isNotEmpty(smsMessageList)) {
                logger.info("Sending SMS for successful transactions.");
                this.smsSender.send(smsMessageList);
            }
        }
        catch (Exception e) {
            logger.error("Exception occurred in send SMS.");
            e.printStackTrace();
        }
    }
	
    public void setTaxManager(TaxManager taxManager) {
		this.taxManager = taxManager;
	}

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }
}