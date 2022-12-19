package com.inov8.microbank.common.util;

import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.disbursement.job.AcHolderBulkDisbursementsThread;
import com.inov8.microbank.disbursement.job.NonAcHolderDisbursementThread;
import com.inov8.microbank.disbursement.service.BulkDisbursementsManager;
import com.inov8.microbank.disbursement.vo.DisbursementVO;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.server.service.advancesalaryloan.dao.AdvanceSalaryLoanDAO;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.tax.service.TaxManager;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by AtieqRe on 3/16/2017.
 */
public class ThreadGenerator {
    private final static Logger LOG = Logger.getLogger(ThreadGenerator.class);

    public static final int THREAD_CARD_ANNUAL_FEE = 1;
    public static final int THREAD_NON_AC_DISBURSEMENT = 2;
    public static final int THREAD_AC_DISBURSEMENT = 3;
    private int threadType;

    private SmsSender smsSender;
    private WorkFlowWrapper workFlowWrapper;
    private ApplicationContext applicationContext;
    private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;
    private TaxManager taxManager;
    private CommandManager commandManager;
    private AdvanceSalaryLoanDAO advanceSalaryLoanDAO;
    private List<?> list;
    private Integer maxThreads;
    private Integer minThreads;
    private Integer maxTrxChunk;
    private Integer minTrxChunk;

    private BulkDisbursementsManager bulkDisbursementsManager;
//    private DebitCardManager debitCardManager;
//    private CardConfigurationManager cardConfigurationManager;
    private CommissionManager commissionManager;

    private ThreadGenerator(List<?> list) {
       this(null, -1, null, list, null);
    }

    public ThreadGenerator(ApplicationContext applicationContext, int threadType, WorkFlowWrapper workFlowWrapper, List<?> list, Map<String, Integer> threadConfigMap) {
        this.applicationContext = applicationContext;
        this.threadType = threadType;
        this.workFlowWrapper = workFlowWrapper;
        this.list = list;

        if(applicationContext != null) {
            this.smsSender = (SmsSender) applicationContext.getBean("smsSender");
            this.taxManager = (TaxManager) applicationContext.getBean("taxManager");
            this.creditAccountQueingPreProcessor = (CreditAccountQueingPreProcessor) applicationContext.getBean("creditAccountQueingPreProcessor");
            this.commandManager = (CommandManager) applicationContext.getBean("cmdManager");
            this.advanceSalaryLoanDAO = (AdvanceSalaryLoanDAO) applicationContext.getBean("advanceSalaryLoanDAO");
        }

        if(!CollectionUtils.isEmpty(threadConfigMap)) {
            maxThreads = threadConfigMap.get("maxThreadRange");
            minThreads = threadConfigMap.get("minThreadRange");
            maxTrxChunk = threadConfigMap.get("maxTrxChunk");
            minTrxChunk = threadConfigMap.get("minTrxChunk");
        }

        if(maxThreads == null || maxThreads == 0)
            maxThreads = 50;

        if(minThreads == null || minThreads == 0)
            minThreads = 10;

        if(maxTrxChunk == null || maxTrxChunk == 0)
            maxTrxChunk = 300;

        if(minTrxChunk == null || minTrxChunk == 0)
            minTrxChunk = 50;
    }

    public void execute() {
        try {
            int array[] = createThreads(list.size());
            int start = 0;
            int end = array[0];
            int totalThreads = array[1];

            ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);
            List<Callable<Object>> executorsList = new ArrayList<>(totalThreads);

            for (int threadNumber = 1; threadNumber <= totalThreads; threadNumber++) {
                List<?> subList = list.subList(start, end);
                if (threadNumber != totalThreads) {
                    start = end;
                    end = end + array[0];
                } else {
                    start = end;
                    end = array[2];
                }

                LOG.info("Thread # " + threadNumber + " Size " + subList.size());
                Runnable runnable = getRunnable(threadNumber, subList);
                if(runnable != null) {
                    executorsList.add(Executors.callable(runnable));
                }

                if (threadNumber == totalThreads) {
                    subList = list.subList(start, end);
                    if (!subList.isEmpty()) {
                        LOG.info("Thread # " + threadNumber + " Size " + subList.size());
                        Runnable _runnable = getRunnable(threadNumber, subList);
                        if(_runnable != null)
                            executorsList.add(Executors.callable(_runnable));
                    }
                }


            }

            executorService.invokeAll(executorsList);
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.HOURS);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int[] createThreads(int listSize) {
        int array [] = new int[3];

        array[0] = maxTrxChunk;
        array[1] = maxThreads;
        array[2] = listSize;

        Integer totalTransactions = listSize;
        Integer trxChunkPerMaxThread = 0;
        Integer trxChunkPerMinThread = 0;
        Integer totalThreads = 0;
        Integer txPerThread = 0;
        Integer calcPendingThread = 0;
        Integer calcPendingTransactions = 0;

        if(totalTransactions < minTrxChunk) {
            maxTrxChunk = totalTransactions;
            minTrxChunk = totalTransactions;
            maxThreads = 1;
            minThreads = 1;
        }

        trxChunkPerMaxThread = totalTransactions / maxThreads;
        trxChunkPerMinThread = totalTransactions / minThreads;

        if (trxChunkPerMaxThread <= maxTrxChunk) {

            LOG.info("Case I");
            totalThreads = maxThreads;
            txPerThread = trxChunkPerMaxThread;

            if (trxChunkPerMaxThread <= minTrxChunk) {
                totalThreads = totalTransactions / trxChunkPerMinThread;
                calcPendingThread = totalTransactions % trxChunkPerMinThread;

                txPerThread = totalTransactions / minThreads;
                calcPendingTransactions = totalTransactions - txPerThread * totalThreads;
                LOG.info("KB " + calcPendingTransactions);
            }
        }

        else if (trxChunkPerMaxThread > maxTrxChunk) {
            LOG.info("Case II");
            txPerThread = maxTrxChunk;
            totalThreads = maxThreads;

            calcPendingThread = totalTransactions / maxTrxChunk - totalThreads;
            calcPendingTransactions = totalTransactions - txPerThread * totalThreads;
        }

        LOG.info("Total Transactions " + totalTransactions + " Total Threads -> " + totalThreads +
                " Tx per Thread -> " + txPerThread + " CalcPendingThreads " + calcPendingThread + " CalcPendingChunk " + calcPendingTransactions);

        array[0] = txPerThread;
        array[1] = totalThreads;

        return array;
    }

    private Runnable getRunnable(int threadNumber, List<?> subList) {
        Runnable runnable = null;

        switch (threadType) {
//            case THREAD_CARD_ANNUAL_FEE :
//                runnable = new DebitCardAnnualFeeSchedulerThread(threadNumber, (List<DebitCardModel>) subList, getCommissionManager(),
//                        getDebitCardManager(), getCardConfigurationManager(), workFlowWrapper, smsSender);
//            break;

            case THREAD_NON_AC_DISBURSEMENT :
                runnable = new NonAcHolderDisbursementThread(threadNumber, getBulkDisbursementsManager(), creditAccountQueingPreProcessor,
                        (List<DisbursementVO>)subList, workFlowWrapper, smsSender, null, null, taxManager);
            break;

            case THREAD_AC_DISBURSEMENT :
                runnable = new AcHolderBulkDisbursementsThread(threadNumber, getBulkDisbursementsManager(), creditAccountQueingPreProcessor,
                        (List<DisbursementVO>) subList, workFlowWrapper, smsSender, taxManager, commandManager, advanceSalaryLoanDAO);
            break;
        }

        return runnable;
    }

    private BulkDisbursementsManager getBulkDisbursementsManager() {
        if(bulkDisbursementsManager == null) {
            bulkDisbursementsManager = (BulkDisbursementsManager) applicationContext.getBean("bulkDisbursementsManager");
        }

        return bulkDisbursementsManager;
    }

//    private DebitCardManager getDebitCardManager() {
//        if(debitCardManager == null) {
//            debitCardManager = (DebitCardManager)applicationContext.getBean("debitCardManager");
//        }
//
//        return debitCardManager;
//    }

//    private CardConfigurationManager getCardConfigurationManager() {
//        if(cardConfigurationManager == null) {
//            cardConfigurationManager = (CardConfigurationManager)applicationContext.getBean("cardConfigurationManager");
//        }
//
//        return cardConfigurationManager;
//    }

    private CommissionManager getCommissionManager() {
        if(commissionManager == null) {
            commissionManager = (CommissionManager)applicationContext.getBean("commissionManager");
        }

        return commissionManager;
    }

    public static void main(String args[]) {
        List<String> list  = new ArrayList<>(100);
        for(int i=0; i<20000; i++) {
            list.add("Item " + i);
        }

        ThreadGenerator threadGenerator = new ThreadGenerator(list);
        threadGenerator.execute();
    }
}
