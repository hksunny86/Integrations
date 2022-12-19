package com.inov8.microbank.account.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.microbank.account.dao.*;
import com.inov8.microbank.account.model.*;
import com.inov8.microbank.account.vo.CustomerACNatureMarkingVo;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.hibernate.criterion.MatchMode;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.account.vo.BlacklistMarkingVo;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import org.springframework.web.context.ContextLoader;

/**
 * Created by Malik on 8/17/2016.
 */
public class AccountControlManagerImpl implements AccountControlManager {

    private BlacklistMarkingViewDAO blacklistMarkingViewDAO;
	private AppUserDAO appUserDAO;
	private CustomerDAO customerDAO;
    private RetailerContactDAO retailerContactDAO;
    private BlacklistedCnicsDAO blacklistedCnicsDAO;
    private BlacklistedCNICViewDAO blacklistedCNICViewDAO;
    private BlacklistMarkUnmarkHistoryDAO  blacklistMarkUnmarkHistoryDAO;
    private SmsSender smsSender;
    private AppUserManager userManager;
    private BlackListedNicHistoryDAO blackListedNicHistoryDAO;
    private WalkInBlackListedNicDAO walkInBlackListedNicDAO;
	private ExpiredNicDAO expiredNicDAO;
	private CommonCommandManager commonCommandManager;

    @Override
    public SearchBaseWrapper searchBlacklistMarkingViewModel(SearchBaseWrapper wrapper) throws FrameworkCheckedException {

        BlacklistMarkingViewModel model = (BlacklistMarkingViewModel) wrapper.getBasePersistableModel();
        CustomList<BlacklistMarkingViewModel> list = blacklistMarkingViewDAO.findByExample(model);
        wrapper.setCustomList(list);
        return wrapper;
    }

    @Override
    public SearchBaseWrapper searchBlacklistedCNICViewModel(SearchBaseWrapper wrapper) throws FrameworkCheckedException {

        BlacklistedCnicsViewModel model = (BlacklistedCnicsViewModel) wrapper.getBasePersistableModel();
        CustomList<BlacklistedCnicsViewModel> list = blacklistedCNICViewDAO.findByExample(model,
                wrapper.getPagingHelperModel(),
                wrapper.getSortingOrderMap(),
                wrapper.getDateRangeHolderModelList());

        wrapper.setCustomList(list);

        return wrapper;
    }
    @Override
    public void saveBlacklistMarkingViewModel(BaseWrapper wrapper) throws FrameworkCheckedException {
    	ArrayList<SmsMessage> smsMessageList = new ArrayList<SmsMessage>();
        List<AppUserModel> appUserModelModifiedList = new ArrayList<AppUserModel>();
        List<BlacklistedCnicsModel> blacklistedCnicsModelModifiedList = new ArrayList<>();
        List<BlacklistMarkUnmarkHistoryModel> historyModelModifiedList = new ArrayList<>();
        BlacklistMarkingViewModel blacklistMarkingViewModel = (BlacklistMarkingViewModel) wrapper.getBasePersistableModel();
        List<BlacklistMarkingVo> list = blacklistMarkingViewModel.getBlacklistMarkingVoList();

        Map<String, Boolean> blacklistedCnicsMap = new HashMap<>();

        for (BlacklistMarkingVo blacklistMarkingVo : list) {
            if (blacklistMarkingVo.getCnicNo() != null) {
                blacklistedCnicsMap.put(blacklistMarkingVo.getCnicNo(), blacklistMarkingVo.getBlacklisted());
            }
        }
        Collection<String> nicsKeySet = blacklistedCnicsMap.keySet();
        List<String> nicsList = new ArrayList<>();
        nicsList.addAll(nicsKeySet);

        //For adding in Blacklisted Cnics List If its account is not present
        //Update cnics which are already present in CNIC blacklisted table
        Collection<String> cnicNos = blacklistedCnicsMap.keySet();

        if (cnicNos.size() != 0) {
            List<BlacklistedCnicsModel> blacklistedCnicsModelList = blacklistedCnicsDAO.loadBlacklistedCnicsModelByCnicNos(cnicNos);
            for (BlacklistedCnicsModel blacklistedCnicsModel : blacklistedCnicsModelList) {
                blacklistedCnicsModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                blacklistedCnicsModel.setUpdatedOn(new Date());
                blacklistedCnicsModel.setBlacklisted(blacklistedCnicsMap.get(blacklistedCnicsModel.getCnicNo()));
                blacklistedCnicsModelModifiedList.add(blacklistedCnicsModel);
                cnicNos.remove(blacklistedCnicsModel.getCnicNo());

                BlacklistMarkUnmarkHistoryModel historyModel = new BlacklistMarkUnmarkHistoryModel();
                historyModel.setCnicNo(blacklistedCnicsModel.getCnicNo());
                if(blacklistedCnicsModel.getBlacklisted() == true){
                	historyModel.setAction("Marked");
                }else{
                	historyModel.setAction("Unmarked");
                }
                // We don't need comments in this case
                //historyModel.setComments(blacklistedCnicsModel.getComments());
                historyModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                historyModel.setUpdatedOn(new Date());

                historyModelModifiedList.add(historyModel);

                //**********************************************************************//
                //	SMS Work on Blacklist Marking Without Authorization					//
                //**********************************************************************//
                boolean isAgent = false, isHandler = false, isCustomer = false;
                long userTypeId = 0;
                String mobNo = null;
                AppUserModel appUserModel = userManager.loadAppUserByCNIC(blacklistedCnicsModel.getCnicNo());
                if(appUserModel != null){
                	userTypeId = appUserModel.getAppUserTypeId();
                	mobNo = appUserModel.getMobileNo();
                }


                //long userTypeId = UserUtils.getCurrentUser().getAppUserTypeId().longValue();
                if(UserTypeConstantsInterface.RETAILER.longValue() == userTypeId){
      				isAgent = true;
      			}
      			else if(UserTypeConstantsInterface.HANDLER.longValue() == userTypeId){
      				isHandler = true;
      			}
      			else if(UserTypeConstantsInterface.CUSTOMER.longValue() == userTypeId){
      				isCustomer = true;
      			}


          		String messageString = "";
    			boolean isBlacklisted = blacklistedCnicsModel.getBlacklisted();
    			if(isBlacklisted == true ){
    				if(isAgent) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_agent_sms3");
    				}
    				else if(isHandler) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_handler_sms1");
    				}
    				else if(isCustomer) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_customer_sms5");
    				}
    			}else{

    				if(isAgent) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_agent_sms4");
    				}
    				else if(isHandler) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_handler_sms2");
    				}
    				else if(isCustomer) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_customer_sms6");
    				}
    			}

    			if(mobNo !=null && !messageString.equals("")){
    				/*SmsMessage smsMessage = new SmsMessage(mobNo, messageString);
    	    		smsSender.send(smsMessage);*/
    				smsMessageList.add(new SmsMessage(appUserModel.getMobileNo(), messageString));

    			}

            //***********************************************************************************************
            }

            //Create new records for cnic numbers which dont exists
            for (String cnicNo : cnicNos) {
                BlacklistedCnicsModel blacklistedCnicsModel = new BlacklistedCnicsModel();
                blacklistedCnicsModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                blacklistedCnicsModel.setCreatedOn(new Date());
                blacklistedCnicsModel.setVersionNo(0);
                blacklistedCnicsModel.setCnicNo(cnicNo);
                blacklistedCnicsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                blacklistedCnicsModel.setUpdatedOn(new Date());
                blacklistedCnicsModel.setBlacklisted(blacklistedCnicsMap.get(cnicNo));
                blacklistedCnicsModelModifiedList.add(blacklistedCnicsModel);

                BlacklistMarkUnmarkHistoryModel historyModel = new BlacklistMarkUnmarkHistoryModel();
                historyModel.setCnicNo(cnicNo);
                if(blacklistedCnicsMap.get(cnicNo) == true){
                	historyModel.setAction("Marked");
                }else{
                	historyModel.setAction("Unmarked");
                }
                historyModel.setComments(blacklistedCnicsModel.getComments());
                historyModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                historyModel.setUpdatedOn(new Date());

                historyModelModifiedList.add(historyModel);

              //**********************************************************************//
                //	SMS Work on Blacklist Marking Without Authorization					//
                //**********************************************************************//
                boolean isAgent = false, isHandler = false, isCustomer = false;
                long userTypeId = 0;
                String mobNo = null;
                AppUserModel appUserModel = userManager.loadAppUserByCNIC(blacklistedCnicsModel.getCnicNo());
                if(appUserModel != null){
                	userTypeId = appUserModel.getAppUserTypeId();
                	mobNo = appUserModel.getMobileNo();
                }


                //long userTypeId = UserUtils.getCurrentUser().getAppUserTypeId().longValue();
                if(UserTypeConstantsInterface.RETAILER.longValue() == userTypeId){
      				isAgent = true;
      			}
      			else if(UserTypeConstantsInterface.HANDLER.longValue() == userTypeId){
      				isHandler = true;
      			}
      			else if(UserTypeConstantsInterface.CUSTOMER.longValue() == userTypeId){
      				isCustomer = true;
      			}


          		String messageString = "";
    			boolean isBlacklisted = blacklistedCnicsModel.getBlacklisted();
    			if(isBlacklisted == true ){
    				if(isAgent) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_agent_sms3");
    				}
    				else if(isHandler) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_handler_sms1");
    				}
    				else if(isCustomer) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_customer_sms5");
    				}
    			}else{

    				if(isAgent) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_agent_sms4");
    				}
    				else if(isHandler) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_handler_sms2");
    				}
    				else if(isCustomer) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_customer_sms6");
    				}
    			}

    			if(mobNo !=null && !messageString.equals("")){
    				/*SmsMessage smsMessage = new SmsMessage(mobNo, messageString);
    	    		smsSender.send(smsMessage);*/
    				smsMessageList.add(new SmsMessage(appUserModel.getMobileNo(), messageString));

    			}

            //***********************************************************************************************







            }
        }

        /*if (appUserModelModifiedList.size() != 0) {
            appUserDAO.saveOrUpdateCollection(appUserModelModifiedList);
        }*/

	    if (blacklistedCnicsModelModifiedList.size() != 0) {
	        blacklistedCnicsDAO.saveOrUpdateCollection(blacklistedCnicsModelModifiedList);
	    }
	    if(historyModelModifiedList.size() != 0){
	    	blacklistMarkUnmarkHistoryDAO.saveOrUpdateCollection(historyModelModifiedList);
	    }
	    if(smsMessageList.size() != 0){
			smsSender.send(smsMessageList);

		}

    }


    @Override
    public void saveBlacklistMarkingViewModelWithAuthorization(BaseWrapper wrapper) throws FrameworkCheckedException {
    	ArrayList<SmsMessage> smsMessageList = new ArrayList<SmsMessage>();
    	List<BlacklistMarkUnmarkHistoryModel> historyModelModifiedList = new ArrayList<>();
        List<AppUserModel> appUserModelModifiedList = new ArrayList<AppUserModel>();
        List<BlacklistedCnicsModel> blacklistedCnicsModelModifiedList = new ArrayList<>();
        BlacklistMarkingViewModel blacklistMarkingViewModel = (BlacklistMarkingViewModel) wrapper.getBasePersistableModel();
        List<BlacklistMarkingVo> list = blacklistMarkingViewModel.getBlacklistMarkingVoList();

        Map<String, Boolean> blacklistedCnicsMap = new HashMap<>();

        for (BlacklistMarkingVo blacklistMarkingVo : list) {
            if (blacklistMarkingVo.getCnicNo() != null) {
                blacklistedCnicsMap.put(blacklistMarkingVo.getCnicNo(), blacklistMarkingVo.getBlacklisted());
            }
        }
        Collection<String> nicsKeySet = blacklistedCnicsMap.keySet();
        List<String> nicsList = new ArrayList<>();
        nicsList.addAll(nicsKeySet);

       //For adding in Blacklisted Cnics List If its account is not present
        //Update cnics which are already present in CNIC blacklisted table
        Collection<String> cnicNos = blacklistedCnicsMap.keySet();

        if (cnicNos.size() != 0) {
            List<BlacklistedCnicsModel> blacklistedCnicsModelList = blacklistedCnicsDAO.loadBlacklistedCnicsModelByCnicNos(cnicNos);
            for (BlacklistedCnicsModel blacklistedCnicsModel : blacklistedCnicsModelList) {
                blacklistedCnicsModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

                blacklistedCnicsModel.setUpdatedOn(new Date());
                blacklistedCnicsModel.setBlacklisted(blacklistedCnicsMap.get(blacklistedCnicsModel.getCnicNo()));
                blacklistedCnicsModelModifiedList.add(blacklistedCnicsModel);
                cnicNos.remove(blacklistedCnicsModel.getCnicNo());
              //**********************History Work*************************************
	        	BlacklistMarkUnmarkHistoryModel historyModel = new BlacklistMarkUnmarkHistoryModel();
                historyModel.setCnicNo(blacklistedCnicsModel.getCnicNo());
                if(blacklistedCnicsModel.getBlacklisted() == true){
                	historyModel.setAction("Marked");
                }else{
                	historyModel.setAction("Unmarked");
                }
                //historyModel.setComments(blacklistedCnicsModel.getComments());
                historyModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                historyModel.setUpdatedOn(new Date());

                historyModelModifiedList.add(historyModel);
	        ///****************************************************************


                //**********************************************************************//
                //		SMS Work on Blacklist Marking With Authorization				//
                //**********************************************************************//
                boolean isAgent = false, isHandler = false, isCustomer = false;

                long userTypeId = 0;
                String mobNo = null;
                AppUserModel appUserModel = userManager.loadAppUserByCNIC(blacklistedCnicsModel.getCnicNo());
                if(appUserModel != null){
                	userTypeId = appUserModel.getAppUserTypeId();
                	mobNo = appUserModel.getMobileNo();
                }

                //long userTypeId = UserUtils.getCurrentUser().getAppUserTypeId().longValue();
                if(UserTypeConstantsInterface.RETAILER.longValue() == userTypeId){
      				isAgent = true;
      			}
      			else if(UserTypeConstantsInterface.HANDLER.longValue() == userTypeId){
      				isHandler = true;
      			}
      			else if(UserTypeConstantsInterface.CUSTOMER.longValue() == userTypeId){
      				isCustomer = true;
      			}

          		String messageString = "";
    			boolean isBlacklisted = blacklistedCnicsModel.getBlacklisted();
    			boolean isBlacklisted2 = blacklistedCnicsModel.getBlacklisted();
    			if(isBlacklisted == true || isBlacklisted2 == true ){
    				if(isAgent) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_agent_sms3");
    				}
    				else if(isHandler) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_handler_sms1");
    				}
    				else if(isCustomer) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_customer_sms5");
    				}
    			}else{

    				if(isAgent) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_agent_sms4");
    				}
    				else if(isHandler) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_handler_sms2");
    				}
    				else if(isCustomer) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_customer_sms6");
    				}
    			}
    			if(mobNo !=null && !messageString.equals("")){
    				/*SmsMessage smsMessage = new SmsMessage(mobNo, messageString);
    	    		smsSender.send(smsMessage);*/
    				smsMessageList.add(new SmsMessage(appUserModel.getMobileNo(), messageString));
    			}

            //***********************************************************************************************
            }

            //Create new records for cnic numbers which dont exists
            for (String cnicNo : cnicNos) {
                BlacklistedCnicsModel blacklistedCnicsModel = new BlacklistedCnicsModel();
                blacklistedCnicsModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                blacklistedCnicsModel.setCreatedOn(new Date());
                blacklistedCnicsModel.setVersionNo(0);
                blacklistedCnicsModel.setCnicNo(cnicNo);
                blacklistedCnicsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                blacklistedCnicsModel.setUpdatedOn(new Date());
                blacklistedCnicsModel.setBlacklisted(blacklistedCnicsMap.get(cnicNo));
                blacklistedCnicsModelModifiedList.add(blacklistedCnicsModel);
                /////////////////////////////////////////////////////////////////////////////////
                //**********************History Work*************************************
	        	BlacklistMarkUnmarkHistoryModel historyModel = new BlacklistMarkUnmarkHistoryModel();
                historyModel.setCnicNo(blacklistedCnicsModel.getCnicNo());
                if(blacklistedCnicsModel.getBlacklisted() == true){
                	historyModel.setAction("Marked");
                }else{
                	historyModel.setAction("Unmarked");
                }
                //historyModel.setComments(blacklistedCnicsModel.getComments());
                historyModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                historyModel.setUpdatedOn(new Date());

                historyModelModifiedList.add(historyModel);
                ///*********************************************************************//
                //**********************************************************************//
                //		SMS Work on Blacklist Marking With Authorization				//
                //**********************************************************************//
                boolean isAgent = false, isHandler = false, isCustomer = false;

                long userTypeId = 0;
                String mobNo = null;
                AppUserModel appUserModel = userManager.loadAppUserByCNIC(blacklistedCnicsModel.getCnicNo());
                if(appUserModel != null){
                	userTypeId = appUserModel.getAppUserTypeId();
                	mobNo = appUserModel.getMobileNo();
                }

                //long userTypeId = UserUtils.getCurrentUser().getAppUserTypeId().longValue();
                if(UserTypeConstantsInterface.RETAILER.longValue() == userTypeId){
      				isAgent = true;
      			}
      			else if(UserTypeConstantsInterface.HANDLER.longValue() == userTypeId){
      				isHandler = true;
      			}
      			else if(UserTypeConstantsInterface.CUSTOMER.longValue() == userTypeId){
      				isCustomer = true;
      			}

          		String messageString = "";
    			boolean isBlacklisted = blacklistedCnicsModel.getBlacklisted();
    			boolean isBlacklisted2 = blacklistedCnicsModel.getBlacklisted();
    			if(isBlacklisted == true || isBlacklisted2 == true ){
    				if(isAgent) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_agent_sms3");
    				}
    				else if(isHandler) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_handler_sms1");
    				}
    				else if(isCustomer) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_customer_sms5");
    				}
    			}else{

    				if(isAgent) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_agent_sms4");
    				}
    				else if(isHandler) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_handler_sms2");
    				}
    				else if(isCustomer) {
    					messageString = MessageUtil.getMessage("smsCommand.blacklist_customer_sms6");
    				}
    			}

    			if(mobNo !=null && !messageString.equals("")){
    				/*SmsMessage smsMessage = new SmsMessage(mobNo, messageString);
    	    		smsSender.send(smsMessage);*/
    				smsMessageList.add(new SmsMessage(appUserModel.getMobileNo(), messageString));
    			}
                ///////////////////////////////////////////////////////////////////////////////////
            }
        }

        //Save blacklisting
	    if (blacklistedCnicsModelModifiedList.size() != 0) {
	        blacklistedCnicsDAO.saveOrUpdateCollection(blacklistedCnicsModelModifiedList);
	    }
	    //Save Blacklist History
	    if(historyModelModifiedList.size() != 0){
	    	blacklistMarkUnmarkHistoryDAO.saveOrUpdateCollection(historyModelModifiedList);
	    }
	    if(smsMessageList.size() != 0){
			smsSender.send(smsMessageList);
		}
    }

	@Override
	public void saveCustomerACNatureMarkingWithAuthorization(BaseWrapper wrapper) throws FrameworkCheckedException {
		ArrayList<SmsMessage> smsMessageList = new ArrayList<SmsMessage>();
		List<BlacklistMarkUnmarkHistoryModel> historyModelModifiedList = new ArrayList<>();
		List<AppUserModel> appUserModelModifiedList = new ArrayList<AppUserModel>();
		List<CustomerModel> customerModelList = new ArrayList<>();
		CustomerModel customerModel = (CustomerModel) wrapper.getBasePersistableModel();
		List<CustomerACNatureMarkingVo> list = customerModel.getCustomerACNatureMarkingVoList();

		Map<String, Long> markedACNaturesMap = new HashMap<>();
		Map<String, Long> appUserIdsMap = new HashMap<>();

		for (CustomerACNatureMarkingVo customerACNatureMarkingVo : list) {
			if (customerACNatureMarkingVo.getCnicNo() != null) {
				markedACNaturesMap.put(customerACNatureMarkingVo.getCnicNo(), customerACNatureMarkingVo.getCustomerACNature());
			}
		}
		for (CustomerACNatureMarkingVo customerACNatureMarkingVo : list) {
			if (customerACNatureMarkingVo.getCnicNo() != null) {
				appUserIdsMap.put(customerACNatureMarkingVo.getCnicNo(), customerACNatureMarkingVo.getAppUserId());
			}
		}
		Collection<String> nicsKeySet = markedACNaturesMap.keySet();
		Collection<String> appUserIdsKeySet = appUserIdsMap.keySet();
		List<String> markedAcList = new ArrayList<>();
		List<String> appUserIdsList = new ArrayList<>();

		markedAcList.addAll(nicsKeySet);
		appUserIdsList.addAll(appUserIdsKeySet);

		Collection<String> cnicNos = markedACNaturesMap.keySet();
		Collection<String> appUserIds = appUserIdsMap.keySet();



		if (cnicNos.size() != 0) {
			for (String appUserId : appUserIds) {

				AppUserModel appUserModel = getCommonCommandManager().getAppUserModelByCNIC(appUserId);
				CustomerModel customerModel1 = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());

				if (customerModel.getCustomerACNatureMarkingVoList().get(0).getCustomerACNature() == 2L) {
					customerModel1.setAcNature(2L);
				}
				else{
					customerModel1.setAcNature(1L);
				}
				customerModel1.setUpdatedOn(new Date());

				customerModelList.add(customerModel1);
//				customerDAO.saveOrUpdate(customerModel1);


				String mobNo = null;
				if (appUserModel != null) {
					mobNo = appUserModel.getMobileNo();
				}
				String messageString = "";
//			boolean isBlacklisted = blacklistedCnicsModel.getBlacklisted();
//			boolean isBlacklisted2 = blacklistedCnicsModel.getBlacklisted();
				if (customerModel.getCustomerACNatureMarkingVoList().get(0).getCustomerACNature() == 2L) {
					messageString = MessageUtil.getMessage("smsCommand.ac_nature_customer_sms5");

				} else {
					messageString = MessageUtil.getMessage("smsCommand.ac_nature_customer_sms6");
				}

				if (mobNo != null && !messageString.equals("")) {
    				/*SmsMessage smsMessage = new SmsMessage(mobNo, messageString);
    	    		smsSender.send(smsMessage);*/
					smsMessageList.add(new SmsMessage(appUserModel.getMobileNo(), messageString));
				}
			}
		}
		if (customerModelList.size() != 0) {
			customerDAO.saveOrUpdateCollection(customerModelList);
		}
		if(smsMessageList.size() != 0){
			smsSender.send(smsMessageList);
		}
	}

	@Override
    public BaseWrapper markUnmarkBlacklistedWithAuthorization(BaseWrapper wrapper) throws FrameworkCheckedException {
    	ArrayList<SmsMessage> smsMessageList = new ArrayList<SmsMessage>();
    	List<BlacklistMarkUnmarkHistoryModel> historyModelModifiedList = new ArrayList<>();
    	BlacklistMarkingVo blacklistMarkingVo = (BlacklistMarkingVo) wrapper.getBasePersistableModel();
        AppUserModel appUserModel = appUserDAO.findByPrimaryKey(blacklistMarkingVo.getAppUserId());
        BlacklistedCnicsModel blackListModel2 = blacklistedCnicsDAO.findBlacklistedCnicsModelByCnicNo(appUserModel.getNic());
        long theDate = new Date().getTime();
		AppUserModel appUserModel1 = new AppUserModel();

		Long appUserType = appUserModel.getAppUserTypeId();
        Long appUserId = appUserModel.getAppUserId();
        
        BlacklistedCnicsModel blackListModel = new BlacklistedCnicsModel(); 
        //true case
        if (blacklistMarkingVo.getBlacklisted()) {
        	        	
        	blackListModel.setCreatedOn(new Date(theDate));
        	blackListModel.setUpdatedOn(new Date(theDate));
        	blackListModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        	blackListModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        	blackListModel.setBlacklisted(blacklistMarkingVo.getBlacklisted());
        	blackListModel.setCnicNo(appUserModel.getNic());
        	blackListModel.setComments(blacklistMarkingVo.getComments());
	        	if(blackListModel2 != null){
	        		blackListModel.setVersionNo(blackListModel2.getVersionNo());	
	        	}else{
	        		blackListModel.setVersionNo(0);	
	        	}
	       //**********************History Work************************************* 	
	        	BlacklistMarkUnmarkHistoryModel historyModel = new BlacklistMarkUnmarkHistoryModel();
                historyModel.setCnicNo(blackListModel.getCnicNo());
                if(blackListModel.getBlacklisted() == true){
                	historyModel.setAction("Marked");
                }else{
                	historyModel.setAction("Unmarked");
                }
                historyModel.setComments(blackListModel.getComments());
                historyModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                historyModel.setUpdatedOn(new Date());
                
                historyModelModifiedList.add(historyModel);


				appUserModel.setRegistrationStateId(RegistrationStateConstants.BLACK_LISTED);

				appUserModel = appUserDAO.saveOrUpdate(appUserModel);
	        ///****************************************************************
	 
        }
        //false case
        else if (!blacklistMarkingVo.getBlacklisted()) {
           
			blackListModel.setBlacklisted(blacklistMarkingVo.getBlacklisted());
			blackListModel.setCnicNo(appUserModel.getNic());
			blackListModel.setComments(blacklistMarkingVo.getComments());
			
		//**********************History Work************************************* 	
        	BlacklistMarkUnmarkHistoryModel historyModel = new BlacklistMarkUnmarkHistoryModel();
            historyModel.setCnicNo(blackListModel.getCnicNo());
            if(blackListModel.getBlacklisted() == true){
            	historyModel.setAction("Marked");
            }else{
            	historyModel.setAction("Unmarked");
            }
            historyModel.setComments(blackListModel.getComments());
            historyModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            historyModel.setUpdatedOn(new Date());
            
            historyModelModifiedList.add(historyModel);

			appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);

			appUserModel = appUserDAO.saveOrUpdate(appUserModel);
        //****************************************************************
        }
        
        if(blackListModel2 != null){
        	blackListModel2.setBlacklisted(blacklistMarkingVo.getBlacklisted());
        	blackListModel2.setComments(blacklistMarkingVo.getComments());
        	blacklistedCnicsDAO.saveOrUpdate(blackListModel2);
        	if(historyModelModifiedList.size() != 0){
		    	blacklistMarkUnmarkHistoryDAO.saveOrUpdateCollection(historyModelModifiedList);
		    }
    	}else{
    		 blacklistedCnicsDAO.saveOrUpdate(blackListModel);
    		 if(historyModelModifiedList.size() != 0){
    		    	blacklistMarkUnmarkHistoryDAO.saveOrUpdateCollection(historyModelModifiedList);
    		    }
    	}
        
        //***********************************************************************************************
     
      		boolean isAgent = false, isHandler = false, isCustomer = false;
      		if(null != appUserModel){
      			long userTypeId = appUserModel.getAppUserTypeId().longValue();
      			if(UserTypeConstantsInterface.RETAILER.longValue() == userTypeId){
      				isAgent = true;
      			}
      			else if(UserTypeConstantsInterface.HANDLER.longValue() == userTypeId){
      				isHandler = true;
      			}
      			else if(UserTypeConstantsInterface.CUSTOMER.longValue() == userTypeId){
      				isCustomer = true;
      			}
      		}

      		String messageString = "";
			boolean isBlacklisted = blackListModel.getBlacklisted();
			boolean isBlacklisted2 = blackListModel.getBlacklisted();
			if(isBlacklisted == true || isBlacklisted2 == true ){
				if(isAgent) {
					messageString = MessageUtil.getMessage("smsCommand.blacklist_agent_sms3");
				}
				else if(isHandler) {
					messageString = MessageUtil.getMessage("smsCommand.blacklist_handler_sms1");
				}
				else if(isCustomer) {
					messageString = MessageUtil.getMessage("smsCommand.blacklist_customer_sms5");
				}
			}else{
				
				if(isAgent) {
					messageString = MessageUtil.getMessage("smsCommand.blacklist_agent_sms4");
				}
				else if(isHandler) {
					messageString = MessageUtil.getMessage("smsCommand.blacklist_handler_sms2");
				}
				else if(isCustomer) {
					messageString = MessageUtil.getMessage("smsCommand.blacklist_customer_sms6");
				}
			}
			
			 smsMessageList.add(new SmsMessage(appUserModel.getMobileNo(), messageString));
			
			
		//SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), messageString);
		smsSender.send(smsMessageList);

        //***********************************************************************************************
        return wrapper;
    }

    @Override
    public Boolean isCnicBlacklisted(String cnic) {
        return blacklistedCnicsDAO.isCnicBlacklisted(cnic);
    }
	@Override
	public BaseWrapper markUnmarkBlacklistedCNIC(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
    	BlacklistedCnicsModel blackList = (BlacklistedCnicsModel)baseWrapper.getBasePersistableModel();
    	 /* if (blackList.getVersionNo() == null)
          {
    		  blackList.setBlacklisted(blackList.getBlacklisted());
          }*/
          try
          {
        	  blacklistedCnicsDAO.saveOrUpdate(blackList);
          }
          catch (DataIntegrityViolationException e)
          {
          	throw new FrameworkCheckedException("Exception occured while creating/updating the BlacklistedCnicsModel", e);
           
          }
          catch(Exception e)
          {
          	throw new FrameworkCheckedException(e.getMessage());
          }
          return baseWrapper;
	}
    
    
    @Override
    public Boolean isUserNameAlreadyExist(String username) {
        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setUsername(username);
        final CustomList<AppUserModel> appUserModelCustomList = appUserDAO.findByExample(appUserModel);
        if (appUserModelCustomList.getResultsetList() != null && appUserModelCustomList.getResultsetList().size() > 0) {
            return true;
        }
        return false;
    }
    
    
    
    
    @Override
	public Boolean isAgentWebEnabled(Long retailerContactId) {

    	RetailerContactModel model = retailerContactDAO.findByPrimaryKey(retailerContactId);
		Boolean isEnabled = false;
		 
		 if(model.getIsAgentWebEnabled() == true){
			 isEnabled = true;
		 }else{
			 isEnabled = false;
		 }
		 return isEnabled;
	}

	@Override
	public Boolean isAgentUSSDEnabled(Long retailerContactId) {

		RetailerContactModel model = retailerContactDAO.findByPrimaryKey(retailerContactId);
		Boolean isEnabled = false;

		if(model.getAgentUssdEnabled() == true){
			isEnabled = true;
		}else{
			isEnabled = false;
		}
		return isEnabled;
	}

	// Debit Card Fee Enabled
	@Override
	public Boolean isDebitCardFeeEnabled(Long retailerContactId) {
		RetailerContactModel model = retailerContactDAO.findByPrimaryKey(retailerContactId);
		Boolean isEnabled = false;

		if(model.getIsDebitCardFeeEnabled() != null && model.getIsDebitCardFeeEnabled() == true){
			isEnabled = true;
		}else{
			isEnabled = false;
		}
		return isEnabled;
	}


	@Override
    public List<String> loadBlacklistedCNICList() {
        return blacklistedCnicsDAO.loadBlacklistedCNICList();
    }
    
    public void setAppUserDAO(AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }

    public void setBlacklistMarkingViewDAO(BlacklistMarkingViewDAO blacklistMarkingViewDAO) {
        this.blacklistMarkingViewDAO = blacklistMarkingViewDAO;
    }


    public void setBlacklistedCnicsDAO(BlacklistedCnicsDAO blacklistedCnicsDAO) {
        this.blacklistedCnicsDAO = blacklistedCnicsDAO;
    }

    public void setBlacklistedCNICViewDAO(BlacklistedCNICViewDAO blacklistedCNICViewDAO) {
        this.blacklistedCNICViewDAO = blacklistedCNICViewDAO;
    }

	
	
	public void setBlacklistMarkUnmarkHistoryDAO(
			BlacklistMarkUnmarkHistoryDAO blacklistMarkUnmarkHistoryDAO) {
		this.blacklistMarkUnmarkHistoryDAO = blacklistMarkUnmarkHistoryDAO;
	}

	@Override
	public SearchBaseWrapper loadBlacklistMarkUnmarkHistory(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<BlacklistMarkUnmarkHistoryModel>
				list = this.blacklistMarkUnmarkHistoryDAO.findByExample( (BlacklistMarkUnmarkHistoryModel)
						searchBaseWrapper.
								getBasePersistableModel(),
				searchBaseWrapper.
						getPagingHelperModel(),
				searchBaseWrapper.
						getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		if(list != null){
			searchBaseWrapper.setCustomList(list);
		}
		return searchBaseWrapper;
	}

	@Override
	public SearchBaseWrapper loadBlackListedNICHistory(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
    	BlackListedNicHistoryViewModel model = (BlackListedNicHistoryViewModel) searchBaseWrapper.getBasePersistableModel();
		ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
		exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
		exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
    	CustomList<BlackListedNicHistoryViewModel> customList = this.blackListedNicHistoryDAO.findByExample(model, null,null,searchBaseWrapper.getDateRangeHolderModel(),exampleConfigHolderModel);
		if(customList != null)
			searchBaseWrapper.setCustomList(customList);
		return searchBaseWrapper;
	}

	@Override
	public SearchBaseWrapper loadWalkInBlackListedNics(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		WalkInBlackListedNicViewModel model = (WalkInBlackListedNicViewModel) searchBaseWrapper.getBasePersistableModel();
    	ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
		exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
		exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
    	CustomList<WalkInBlackListedNicViewModel> customList = this.walkInBlackListedNicDAO.findByExample(model,
                null,null,searchBaseWrapper.getDateRangeHolderModel(),exampleConfigHolderModel);
		if(customList != null)
			searchBaseWrapper.setCustomList(customList);
		return searchBaseWrapper;
	}

	@Override
	public SearchBaseWrapper loadExpiredNics(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
    	ExpiredNicViewModel model = (ExpiredNicViewModel) searchBaseWrapper.getBasePersistableModel();
    	ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
    	exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
    	exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
		CustomList<ExpiredNicViewModel> customList = this.expiredNicDAO.findByExample(model,
              null,null,searchBaseWrapper.getDateRangeHolderModel(),exampleConfigHolderModel);
		if(customList != null)
			searchBaseWrapper.setCustomList(customList);
		return searchBaseWrapper;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public void setUserManager(AppUserManager userManager) {
		this.userManager = userManager;
	}

	public void setRetailerContactDAO(RetailerContactDAO retailerContactDAO) {
		this.retailerContactDAO = retailerContactDAO;
	}


	public void setBlackListedNicHistoryDAO(BlackListedNicHistoryDAO blackListedNicHistoryDAO) {
		this.blackListedNicHistoryDAO = blackListedNicHistoryDAO;
	}

	public void setWalkInBlackListedNicDAO(WalkInBlackListedNicDAO walkInBlackListedNicDAO) {
		this.walkInBlackListedNicDAO = walkInBlackListedNicDAO;
	}

	public void setExpiredNicDAO(ExpiredNicDAO expiredNicDAO) {
		this.expiredNicDAO = expiredNicDAO;
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}
	public CommonCommandManager getCommonCommandManager() {
		ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
		return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
	}
}
