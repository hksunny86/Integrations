package com.inov8.integration.middleware.mock;

import com.inov8.integration.host.HostTransactionProcessor;
import com.inov8.integration.middleware.util.FormatUtils;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.integration.webservice.vo.Transaction;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MicroBankMockResponse {

    private static Logger logger = LoggerFactory.getLogger(MicroBankMockResponse.class.getSimpleName());

    public void cashWithDrawalCommand(MiddlewareMessageVO vo) {
        vo.setResponseCode("00");
    }

    public void cashWithDrawalReversalCommand(MiddlewareMessageVO vo) {
    	vo.setResponseCode("00");

    }

    public void miniStatementCommand(WebServiceVO vo) {
//        Integer[] amountArr = {1000,2000,1500,500,300};
//        String[] createdOn = {"10-JUL-14"};
//        String[] productName = {"Send Money"};
//        List<Transaction> list = vo.getTransactions();
//        Transaction t=new Transaction();
//        for(int i=0;i<amountArr.length;i++) {
//            t = new Transaction();
//            t.setAmount(amountArr[i].toString());
//            t.setDescription(productName[i]);
//            t.setDate(createdOn[i]);
//            list.add(t);
//        }
        vo.setResponseCode("00");
//        vo.setTransactions(list);
    }

    public void balanceInquiryCommand(WebServiceVO vo) {
        vo.setResponseCode("00");
        vo.setTransactionProcessingAmount("100000");


    }

    public void posTransactionCoammand(MiddlewareMessageVO vo) {
        vo.setResponseCode("00");
        vo.setTransactionAmount("20000");

    }

    public void posTransactionReversalCommand(MiddlewareMessageVO vo) {
        vo.setResponseCode("00");
        vo.setTransactionAmount("60000");
    }
    public void posReverseTransactionCommand(MiddlewareMessageVO vo) {
        vo.setResponseCode("00");
        vo.setTransactionAmount("70000");
    }
}
