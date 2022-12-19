package com.inov8.microbank.schedulers;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.schedulers.service.SchedulerManager;
import com.inov8.microbank.server.dao.retailermodule.BulkAgentCreationDAO;
import com.inov8.microbank.server.facade.portal.level3account.Level3AccountFacade;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountFacade;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import org.apache.log4j.Logger;

import java.util.*;

public class BulkAgentCreationScheduler {

    private static final Logger LOGGER = Logger.getLogger(BulkAgentCreationScheduler.class );

    private SchedulerManager schedulerManager;
    private BulkAgentCreationDAO bulkAgentCreationDAO;
    private MfsAccountFacade mfsAccountFacade;
    private AppUserManager securityFacade;
    private ActionLogManager actionLogManager;

    private List<BulkAgentDataHolderModel> listToUpdate = new ArrayList<>(0);

    @SuppressWarnings("unchecked")
    public void init() {
        LOGGER.info("*********** Executing Bulk Agent Creation Scheduler ***********");
        Integer ORACLE_LIMIT = 500;
        if(listToUpdate != null)
            listToUpdate.clear();
        List<BulkAgentDataHolderModel> list = null;
        try {
            list = bulkAgentCreationDAO.getDataForAgentCreation(0L);
        } catch (FrameworkCheckedException e) {
            LOGGER.error("Error in BulkAgentCreationScheduler: " + e.getMessage() + ", " + e);
        }
        if(list != null && list.size() > 0)
        {
            BaseWrapper baseWrapper = null;
            List[] chunks = this.chunks(list, ORACLE_LIMIT);
            for(List<BulkAgentDataHolderModel> models : chunks)
            {
                for(BulkAgentDataHolderModel model: models)
                {
                    baseWrapper = new BaseWrapperImpl();
                    if(!isAlreadyExist(model))
                        continue;
                    AgentMerchantDetailModel merchantDetailModel = this.prepareAgentMerchantDetailModel(model);
                    baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, 3L);
                    baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, 1208L);
                    try {
                        baseWrapper.setBasePersistableModel(merchantDetailModel);
                        baseWrapper= this.prepareModel(model,baseWrapper);
                        baseWrapper.putObject("is_scheduler",true);
                        schedulerManager.createAgentAccount(baseWrapper);
                        model.setIsProcessedByScheduler(1L);
                        model.setUpdatedOn(new Date());
                        model.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        listToUpdate.add(model);
                    } catch (FrameworkCheckedException e) {
                        LOGGER.error("Error in Bulk Agent Scheduler for INIT_APP_FOR_NUMBER: " + merchantDetailModel.getInitialAppFormNo());
                        LOGGER.error("BulkAgentCreationScheduler Error: " + e.getMessage() + ", " + e);
                    }
                }
            }
            if(listToUpdate != null && listToUpdate.size() >0)
                bulkAgentCreationDAO.saveOrUpdateCollection(listToUpdate);
        }
    }

    private Boolean isAlreadyExist(BulkAgentDataHolderModel agentDataHolderModel)
    {
         Boolean validated = Boolean.TRUE;
        String errorMessage = null;
        try
        {
            if(!mfsAccountFacade.isAlreadyExistInitAppFormNumber(agentDataHolderModel.getInitialAppFormNo()))
            {
                errorMessage = "Initial Application Form Number: " + agentDataHolderModel.getInitialAppFormNo() + " Already Exists.";
                validated = Boolean.FALSE;
            }
            if(!mfsAccountFacade.isAlreadyExistReferenceNumber(agentDataHolderModel.getReferenceNo(),agentDataHolderModel.getInitialAppFormNo()))
            {
                errorMessage += "/ Reference Number: " + agentDataHolderModel.getReferenceNo() + " Already Exists.";
                validated = Boolean.FALSE;
            }
            if(mfsAccountFacade.loadAgentMerchantDetailModelByUserName(agentDataHolderModel.getUserName()) != null)
            {
                errorMessage += "/ User Name: " + agentDataHolderModel.getUserName() + " Already Exists.";
                validated = Boolean.FALSE;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            errorMessage += "/ Exception Occurred with message: " + ex.getMessage();
            validated = false;
        }
        if(!validated)
        {
            try {
                ActionLogModel actionLogModel = new ActionLogModel();
                actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
                actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
                actionLogModel.setUsecaseId(1513L);
                actionLogModel.setInputXml(agentDataHolderModel.getUserName());
                actionLogModel.setOutputXml(errorMessage);
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                baseWrapper.setBasePersistableModel(actionLogModel);
                actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
                //
                LOGGER.error("Error Occurred: " + errorMessage);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
            }
        }
        return validated;
    }

    private BaseWrapper prepareModel(BulkAgentDataHolderModel model,BaseWrapper baseWrapper)
    {
        ApplicantDetailModel applicantDetailModel = new ApplicantDetailModel();
        Level3AccountModel level3AccountModel = new Level3AccountModel();
        level3AccountModel.setAccountPurposeId(model.getPurposeOfAccountId());
        level3AccountModel.setInitialAppFormNo(model.getInitialAppFormNo());
        if(model.getAccountTypeId() != null)
            level3AccountModel.setTypeOfAccount(model.getAccountTypeId().toString());
        level3AccountModel.setAcTitle(model.getAccountTitle());
        level3AccountModel.setCurrencyId(model.getCurrencyId());
        level3AccountModel.setTaxRegimeId(model.getTaxRegimeId());
        level3AccountModel.setFed(model.getFed());
        level3AccountModel.setBusinessName(model.getBusinessName());
        level3AccountModel.setBusinessAddress(model.getBusinessAddress());
        level3AccountModel.setBusinessAddCity(model.getBusinessCityId());
        level3AccountModel.setCommencementDate(model.getBusinessCommencementDate());
        level3AccountModel.setBusinessTypeId(model.getBusinessTypeId());
        level3AccountModel.setBusinessNatureId(model.getBusinessNatureId());
        level3AccountModel.setLocationTypeId(model.getLocationTypeId());
        level3AccountModel.setLocationSizeId(model.getLocationSizeId());
        level3AccountModel.setEstSince(Long.parseLong(model.getEstablishedDate()));
        level3AccountModel.setCorresspondanceAddress(model.getCorresspondenceAdrees());
        level3AccountModel.setCorresspondanceAddCity(model.getCorresspondenceCityId());
        level3AccountModel.setEmployeeID(model.getEmpId());
        level3AccountModel.setEmployeeName(model.getEmployeeAppUserModel().getFullName());
        applicantDetailModel.setName(model.getApplicantName());
        applicantDetailModel.setIdType(model.getDocTypeId());
        applicantDetailModel.setIdNumber(model.getDocValue());
        applicantDetailModel.setIdExpiryDate(model.getDocExpiryDate());
        applicantDetailModel.setDob(model.getApplicantDOB());
        applicantDetailModel.setMobileNo(model.getApplicantMobileNo());
        level3AccountModel.setApplicant1DetailModel(applicantDetailModel);
        level3AccountModel.setCustomerAccountTypeId(model.getAcLevelQualificationId());
        level3AccountModel.setServiceOperatorId(50028L);
        level3AccountModel.setAccountOpeningDate(new Date());

        baseWrapper.putObject("applicantDetailModel",applicantDetailModel);
        level3AccountModel.setRegistrationStateId(RegistrationStateConstantsInterface.RQST_RCVD);
        level3AccountModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);
        level3AccountModel.setCurrencyId(1L);
        baseWrapper.putObject(Level3AccountModel.LEVEL3_ACCOUNT_MODEL_KEY, level3AccountModel);
        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, 1L);
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, 1513L);

        return baseWrapper;
    }

    private List[] chunks(final List<BulkAgentDataHolderModel> pList, final int pSize)
    {
        if(pList == null || pList.size() == 0 || pSize == 0) return new List[] {};
        if(pSize < 0) return new List[] { pList };

        // Calculate the number of batches
        int numBatches = (pList.size() / pSize) + 1;

        // Create a new array of Lists to hold the return value
        List[] batches = new List[numBatches];

        for(int index = 0; index < numBatches; index++)
        {
            int count = index + 1;
            int fromIndex = Math.max(((count - 1) * pSize), 0);
            int toIndex = Math.min((count * pSize), pList.size());
            batches[index] = pList.subList(fromIndex, toIndex);
        }

        return batches;
    }

    private List<AgentMerchantDetailModel> prepareDataForAgentCreation(List<BulkAgentDataHolderModel> list)
    {
        List<AgentMerchantDetailModel> amdml = new ArrayList<>(0);
        for(BulkAgentDataHolderModel model : list)
        {
            AgentMerchantDetailModel agentMerchantDetailModel = this.prepareAgentMerchantDetailModel(model);
            amdml.add(agentMerchantDetailModel);
        }
        return amdml;
    }

    private AgentMerchantDetailModel prepareAgentMerchantDetailModel(BulkAgentDataHolderModel model)
    {
        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setEmployeeId(model.getEmpId());
        AppUserModel empUserModel = securityFacade.getAppUserModel(appUserModel);
        AgentMerchantDetailModel agentMerchantDetailModel = new AgentMerchantDetailModel();
        agentMerchantDetailModel.setDistributorId(model.getDistributorId());
        agentMerchantDetailModel.setRegionId(model.getRegionId());
        agentMerchantDetailModel.setAcLevelQualificationId(model.getAcLevelQualificationId());
        agentMerchantDetailModel.setUserName(model.getUserName());
        agentMerchantDetailModel.setBusinessName(model.getBusinessName());
        agentMerchantDetailModel.setAreaLevelId(model.getAreaLevelId());
        agentMerchantDetailModel.setAreaId(model.getAreaId());
        agentMerchantDetailModel.setDistributorLevelId(model.getDistributorLevelId());
        agentMerchantDetailModel.setProductCatalogId(model.getProductCatalogId());
        agentMerchantDetailModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
        agentMerchantDetailModel.setCreatedOn(new Date());
        agentMerchantDetailModel.setEmployeeAppUserModel(empUserModel);
        agentMerchantDetailModel.setReferenceNo(model.getReferenceNo());
        agentMerchantDetailModel.setPassword(EncoderUtils.encodeToSha(model.getPassword()));
        agentMerchantDetailModel.setInitialAppFormNo(model.getInitialAppFormNo());
        if(model.getParentAgentId() != null)
            agentMerchantDetailModel.setParentAgentId(model.getParentAgentId());
        if(model.getParentAgentName() != null)
            agentMerchantDetailModel.setParentAgentName(model.getParentAgentName());
        if(model.getUltimateParentAgentName() != null)
            agentMerchantDetailModel.setUltimateParentAgentName(model.getUltimateParentAgentName());
        if(model.getUltimateParentAgentId() != null)
            agentMerchantDetailModel.setUltimateParentAgentId(model.getUltimateParentAgentId());
        if(model.getRetailerId() != null)
            agentMerchantDetailModel.setRetailerId(model.getRetailerId());
        return agentMerchantDetailModel;
    }

    public void setBulkAgentCreationDAO(BulkAgentCreationDAO bulkAgentCreationDAO) {
        this.bulkAgentCreationDAO = bulkAgentCreationDAO;
    }

    public void setMfsAccountFacade(MfsAccountFacade mfsAccountFacade) {
        this.mfsAccountFacade = mfsAccountFacade;
    }

    public void setSecurityFacade(AppUserManager securityFacade) {
        this.securityFacade = securityFacade;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }

    public void setSchedulerManager(SchedulerManager schedulerManager) {
        this.schedulerManager = schedulerManager;
    }
}
