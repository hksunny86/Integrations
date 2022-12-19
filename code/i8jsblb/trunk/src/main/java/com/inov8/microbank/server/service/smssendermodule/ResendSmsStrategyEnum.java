package com.inov8.microbank.server.service.smssendermodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.model.userdeviceaccountmodule.UserDeviceAccountListViewModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstantsInterface;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDetailMasterDAO;
import com.inov8.microbank.server.dao.userdeviceaccount.UserDeviceAccountListViewDAO;
import com.inov8.microbank.server.service.portal.transactiondetaili8module.TransactionDetailI8Manager;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;

public enum ResendSmsStrategyEnum
{
    INITIATOR_SMS_STRATEGY
    {
        @Override
        void resendSms( TransactionModel transactionModel, SmsSender smsSender,TransactionDetailI8Manager transactionDetailI8Manager,UserDeviceAccountListViewDAO userDeviceAccountListViewDAO) throws FrameworkCheckedException
        {
            TransactionDetailModel transactionDetailModel = getTransactionDetailModel( transactionModel );
            long productId = transactionDetailModel.getProductId().longValue();
            SmsMessage smsMessage = null;
            
            if( ProductConstantsInterface.ACCOUNT_TO_CASH.longValue() == productId || ProductConstantsInterface.CASH_TRANSFER.longValue() == productId
            		|| ProductConstantsInterface.BULK_PAYMENT.longValue() == productId)
            {	
            	
            	TransactionDetailPortalListModel transactionDetailPortalListModel = new TransactionDetailPortalListModel();
            	transactionDetailPortalListModel.setTransactionId(transactionModel.getTransactionId());
            	
            	SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            	searchBaseWrapper.setBasePersistableModel(transactionDetailPortalListModel);
            	
            	
            	List<TransactionDetailPortalListModel> transactionDetailMasterModelList= transactionDetailI8Manager.searchTransactionDetailForI8(searchBaseWrapper).getCustomList().getResultsetList();
                if(null!=transactionDetailMasterModelList && transactionDetailMasterModelList.size()>0)
                	transactionDetailPortalListModel = transactionDetailMasterModelList.get(0);
            	
            	
            	
                if(transactionModel.getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstantsInterface.COMPLETED.longValue())
                {    	
                    UserDeviceAccountListViewModel userDeviceAccountListViewModel = new UserDeviceAccountListViewModel();
                    userDeviceAccountListViewModel.setUserId(transactionDetailPortalListModel.getAgent2Id());
                    userDeviceAccountListViewModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
       
                    List<UserDeviceAccountListViewModel> userDeviceAccountListViewModelList = userDeviceAccountListViewDAO.findByExample(userDeviceAccountListViewModel).getResultsetList();
                	
                    if(null!=userDeviceAccountListViewModelList && userDeviceAccountListViewModelList.size()>0)
                    	userDeviceAccountListViewModel = userDeviceAccountListViewModelList.get(0);
                    	
                    
                	smsMessage = new SmsMessage( userDeviceAccountListViewModel.getMobileNo(), transactionDetailModel.getCustomField4() );
	            }
                else
	            {
                	if(ProductConstantsInterface.BULK_PAYMENT.longValue() != productId){
                		UserDeviceAccountListViewModel userDeviceAccountListViewModel = new UserDeviceAccountListViewModel();
                        userDeviceAccountListViewModel.setUserId(transactionDetailPortalListModel.getAgent1Id());
                        userDeviceAccountListViewModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
           
                        List<UserDeviceAccountListViewModel> userDeviceAccountListViewModelList = userDeviceAccountListViewDAO.findByExample(userDeviceAccountListViewModel).getResultsetList();
                    	
                        if(null!=userDeviceAccountListViewModelList && userDeviceAccountListViewModelList.size()>0)
                        	userDeviceAccountListViewModel = userDeviceAccountListViewModelList.get(0);
                        
                		smsMessage = new SmsMessage( userDeviceAccountListViewModel.getMobileNo(), transactionDetailModel.getCustomField4() );

                	}
	            }
            }
            else
            {
                smsMessage = new SmsMessage( transactionModel.getSaleMobileNo(), transactionDetailModel.getCustomField4() );
            }
            smsSender.send( smsMessage );
        }
    },
    RECIPIENT_SMS_STRATEGY
    {
        @Override
        void resendSms( TransactionModel transactionModel, SmsSender smsSender,TransactionDetailI8Manager transactionDetailI8Manager,UserDeviceAccountListViewDAO userDeviceAccountListViewDAO) throws FrameworkCheckedException
        {
            TransactionDetailModel transactionDetailModel = getTransactionDetailModel( transactionModel );
            long productId = transactionDetailModel.getProductId().longValue();
            
            if( ProductConstantsInterface.ACCOUNT_TO_CASH.longValue() == productId){
                smsSender.send( new SmsMessage( transactionModel.getSaleMobileNo(), transactionDetailModel.getCustomField8() ) );
            }else{
            	smsSender.send( new SmsMessage( transactionModel.getNotificationMobileNo(), transactionModel.getConfirmationMessage() ) );
            }
        }
    },
    WALKIN_DEPOSITOR_SMS_STRATEGY
    {
        @Override
        void resendSms( TransactionModel transactionModel, SmsSender smsSender,TransactionDetailI8Manager transactionDetailI8Manager,UserDeviceAccountListViewDAO userDeviceAccountListViewDAO) throws FrameworkCheckedException
        {
            TransactionDetailModel transactionDetailModel = getTransactionDetailModel( transactionModel );
            smsSender.send( new SmsMessage( transactionDetailModel.getCustomField6(), transactionDetailModel.getCustomField8() ) );
        }
    },
    WALKIN_BENEFICIARY_SMS_STRATEGY
    {
        @Override
        void resendSms( TransactionModel transactionModel, SmsSender smsSender,TransactionDetailI8Manager transactionDetailI8Manager,UserDeviceAccountListViewDAO userDeviceAccountListViewDAO) throws FrameworkCheckedException
        {
            TransactionDetailModel transactionDetailModel = getTransactionDetailModel( transactionModel );
            smsSender.send( new SmsMessage( transactionDetailModel.getCustomField5(), transactionDetailModel.getCustomField10() ) );
        }
    };

    protected TransactionDetailModel getTransactionDetailModel( TransactionModel transactionModel )
    {
        TransactionDetailModel transactionDetailModel = null;

        List<TransactionDetailModel> transactionDetailModels = (List<TransactionDetailModel>) transactionModel.getTransactionIdTransactionDetailModelList();	        
        if( transactionDetailModels != null && !transactionDetailModels.isEmpty() )
        {
            transactionDetailModel = transactionDetailModels.get( 0 );
        }

        return transactionDetailModel;
    }

    abstract void resendSms( TransactionModel transactionModel, SmsSender smsSender,TransactionDetailI8Manager transactionDetailI8Manager,UserDeviceAccountListViewDAO userDeviceAccountListViewDAO) throws FrameworkCheckedException;
}