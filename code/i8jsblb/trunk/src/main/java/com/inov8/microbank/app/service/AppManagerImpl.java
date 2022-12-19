package com.inov8.microbank.app.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.app.dao.AppInfoDAO;
import com.inov8.microbank.app.model.AppInfoModel;
import com.inov8.microbank.app.vo.AppInfoVO;
import com.inov8.microbank.app.vo.AppVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppVersionModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.dao.mfsmodule.AppVersionDAO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ContextLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppManagerImpl implements AppManager {

    private AppVersionDAO appVersionDAO;
    private AppInfoDAO appInfoDAO;
    private MessageSource messageSource;

    @Override
    public AppVO validateAppVersion(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        long userTypeId = Long.parseLong(CommonUtils.getParameter(baseWrapper, CommandFieldConstants.KEY_USER_TYPE));

        AppVO appVO = new AppVO();
        String appId = CommonUtils.getParameter(baseWrapper, CommandFieldConstants.KEY_APP_ID);
        if(!StringUtil.isNullOrEmpty(appId)){
            appVO.setAppId(Long.valueOf(appId));
        }else{
            //****** APP_ID is null for app_Version checking. Setting App_ID = -1 to make user download latest AgentMate....");
            appVO.setAppId(AppConstants.DUMMY_APP);
        }
        appVO.setOsType(CommonUtils.getParameter(baseWrapper, CommandFieldConstants.KEY_OS));

        appVO = appVersionDAO.loadLatestAppVersion(appVO);
        if(appVO == null || StringUtil.isNullOrEmpty(appVO.getAppVersion())) {
            throw new CommandException(MessageUtil.getMessage(ErrorCodes.APP_VERSION_BLACKLIST_OBSOLETE), ErrorCodes.APP_VERSION_BLACKLIST_OBSOLETE,ErrorLevel.MEDIUM, null);
        }

        String appVersionNumber = CommonUtils.getParameter(baseWrapper, CommandFieldConstants.KEY_APP_VER);
        String appUsageLevel = null;

        if(appVersionNumber.equals(appVO.getAppVersion())) {
            appVO.setAppUsageLevel(XMLConstants.TAG_APP_LEVEL_NORMAL);

            //validate if this app is available for logging in user
            if(userTypeId != appVO.getAppUserTypeId()) {
                throw  new FrameworkCheckedException(MessageUtil.getMessage(ErrorCodes.UN_AUTHORIZED_ACCESS), null, ErrorCodes.UN_AUTHORIZED_ACCESS, null);
            }
        }

        else {

            int existingVersion = Integer.parseInt(appVersionNumber.replace(".", ""));
            int fromCompatibleVersion = Integer.parseInt(appVO.getFromCompatibleVersion().replace(".", ""));

            if(existingVersion >= fromCompatibleVersion) {
                appUsageLevel = XMLConstants.TAG_APP_LEVEL_CRITICAL;
            }

            if(existingVersion < fromCompatibleVersion) {
                appUsageLevel = XMLConstants.TAG_APP_LEVEL_BLOCK;
            }

            if(appUsageLevel.equals(XMLConstants.TAG_APP_LEVEL_BLOCK)) {
                String versionMessage = MessageUtil.getMessage(ErrorCodes.APP_VERSION_OBSOLETE.toString(), new Object[]{ StringEscapeUtils.escapeXml( appVO.getUrl() ) });
                throw new CommandException(versionMessage, ErrorCodes.APP_VERSION_BLACKLIST_OBSOLETE, ErrorLevel.MEDIUM, null);
            }
            appVO.setAppUsageLevel(appUsageLevel);
        }

        return appVO;
    }

    @Override
    public List<AppInfoVO> loadAppInfoList(Long appUserTypeId) throws FrameworkCheckedException {
        return appInfoDAO.loadAppInfoList(appUserTypeId);
    }

    @Override
    public String getDefaultAppURL(Long appUserTypeId) throws FrameworkCheckedException {
        List<AppInfoVO> appInfoVOList = loadAppInfoList(appUserTypeId);

        if(!CollectionUtils.isEmpty(appInfoVOList)) {
            return appInfoVOList.get(0).getUrl();
        }

        return "";
    }
    
    
    public void sendSMSToUsers(String userName,String pin,boolean isConsumerApp) {
		try {
			AppInfoModel appInfoModel=new AppInfoModel();
			appInfoModel.setAppId(AppConstants.CONSUMER_APP);
			List<AppInfoModel> appInfoModelList=new ArrayList<>();
			appInfoModelList=appInfoDAO.findByExample(appInfoModel).getResultsetList();
			ArrayList<String> urls=new ArrayList<>();
			if(appInfoModel!=null){
				for (AppInfoModel model:appInfoModelList
					 ) {
					urls.add(model.getUrl());
				}
			}

			BaseWrapper baseWrapper  = new BaseWrapperImpl();

			String brandName = MessageUtil.getMessage("jsbl.brandName");

			//Message to customer
			String customerSMS;
			if(isConsumerApp){
					customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_con_app", new Object[] { userName, pin},null);
			}else{
//					customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_fonepay", new Object[] { userName,urls.get(0),urls.get(1), pin}, null);
                customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_fonepay", new Object[] { userName, pin},null);

            }

			
			baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(userName, customerSMS));
			getCommonCommandManager().sendSMSToUser(baseWrapper);


		}

		catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
	}

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }
    
    public SearchBaseWrapper loadApplicationVersion(SearchBaseWrapper searchBaseWrapper) {
        AppVersionModel appVersionModel = this.appVersionDAO.findByPrimaryKey(searchBaseWrapper.
                getBasePersistableModel().
                getPrimaryKey());
        searchBaseWrapper.setBasePersistableModel(appVersionModel);
        return searchBaseWrapper;
    }

    public BaseWrapper loadApplicationVersion(BaseWrapper baseWrapper) {
        AppVersionModel appVersionModel = this.appVersionDAO.findByPrimaryKey(baseWrapper.
                getBasePersistableModel().
                getPrimaryKey());
        baseWrapper.setBasePersistableModel(appVersionModel);
        return baseWrapper;
    }

    public SearchBaseWrapper searchApplicationVersion(SearchBaseWrapper searchBaseWrapper) {
        CustomList<AppVersionModel>
                list = this.appVersionDAO.findByExample((AppVersionModel)
                        searchBaseWrapper.
                                getBasePersistableModel(),
                searchBaseWrapper.
                        getPagingHelperModel(),
                searchBaseWrapper.
                        getSortingOrderMap());
        searchBaseWrapper.setCustomList(list);
        return searchBaseWrapper;
    }

    public BaseWrapper updateApplicationVersion(BaseWrapper baseWrapper) {
        AppVersionModel appVersionModel = (AppVersionModel) baseWrapper.getBasePersistableModel();
        AppVersionModel newAppVersionModel = new AppVersionModel();
        newAppVersionModel.setAppVersionId(appVersionModel.getAppVersionId());

        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setMatchMode(MatchMode.EXACT);
        int recordCount = this.appVersionDAO.countByExample(newAppVersionModel, exampleHolder);

        if (recordCount != 0 && appVersionModel.getPrimaryKey() != null) {
            appVersionModel = this.appVersionDAO.saveOrUpdate((AppVersionModel) baseWrapper.
                    getBasePersistableModel());
            baseWrapper.setBasePersistableModel(appVersionModel);
            return baseWrapper;
        } else {
            baseWrapper.setBasePersistableModel(null);
            return baseWrapper;
        }
    }

    public BaseWrapper createApplicationVersion(BaseWrapper baseWrapper) {
        int recordCount;
        Date nowDate = new Date();
        AppVersionModel newAppVersionModel = new AppVersionModel();
        AppVersionModel appVersionModel = (AppVersionModel) baseWrapper.getBasePersistableModel();
        newAppVersionModel.setAppVersionNumber(appVersionModel.getAppVersionNumber());
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setMatchMode(MatchMode.EXACT);
        recordCount = appVersionDAO.countByExample(newAppVersionModel, exampleHolder);

        //***Check if Record already exists

        appVersionModel.setCreatedOn(nowDate);
        appVersionModel.setUpdatedOn(nowDate);

        if (recordCount == 0) {
            baseWrapper.setBasePersistableModel(this.appVersionDAO.saveOrUpdate(appVersionModel));
            return baseWrapper;
        } else {
            baseWrapper.setBasePersistableModel(null);
            return baseWrapper;
        }
    }

    public void setAppVersionDAO(AppVersionDAO appVersionDAO) {
        this.appVersionDAO = appVersionDAO;
    }

    public void setAppInfoDAO(AppInfoDAO appInfoDAO) {
        this.appInfoDAO = appInfoDAO;
    }

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
    
    
}
