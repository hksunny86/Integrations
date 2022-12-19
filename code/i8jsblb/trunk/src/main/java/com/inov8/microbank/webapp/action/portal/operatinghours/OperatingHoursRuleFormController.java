package com.inov8.microbank.webapp.action.portal.operatinghours;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;

import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.operatinghoursmodule.OperatingHoursRuleModelDAO;

import com.inov8.microbank.server.facade.portal.operatinghoursmodule.OperatingHoursRuleFacade;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperatingHoursRuleFormController extends AdvanceFormController {
    private static final Logger LOGGER = Logger.getLogger( OperatingHoursRuleFormController.class );
    private OperatingHoursRuleFacade operatingHoursRuleFacade;
    //private VelocityRuleFacade velocityRuleFacade;
    private ReferenceDataManager referenceDataManager;
    private DeviceTypeManager deviceTypeManager;
    private OperatingHoursRuleModelDAO operatingHoursRuleModelDAO;
    //private VelocityRuleModelDAO velocityRuleModelDAO;
    @Autowired
    private AgentHierarchyManager agentHierarchyManager;

    public OperatingHoursRuleFormController() {
        setCommandName("operatingHoursRuleModel");
        setCommandClass(OperatingHoursRuleModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest request) throws Exception {
        Map referenceDataMap = new HashMap();
        ReferenceDataWrapper referenceDataWrapper;

        String distributorId = ServletRequestUtils.getStringParameter(request, "distributorId");

        List<ProductModel> productModelList = null;
        ProductModel productModel = new ProductModel();
        productModel.setActive(true);

        List<SegmentModel> segmentModelLsit = null;
        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setIsActive(true);

        List<DeviceTypeModel> deviceTypeModelList = null;
        DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
        deviceTypeModel.setActive(true);

        List<DistributorModel> distributorModelList = null;
        DistributorModel	distributorModel = new DistributorModel();
        distributorModel.setActive(true);


        List<DistributorLevelModel> distributorLevelModelList = null;
        DistributorLevelModel	distributorLevelModel = new DistributorLevelModel();
        distributorLevelModel.setActive(true);

        List<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList = null;
        OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
        olaCustomerAccountTypeModel.setActive(true);


        referenceDataWrapper = new ReferenceDataWrapperImpl(productModel, "name", SortingOrder.DESC);
        try
        {
            referenceDataManager.getReferenceData(referenceDataWrapper);
        }
        catch (FrameworkCheckedException ex1)
        {
            ex1.printStackTrace();
        }
        if (null != referenceDataWrapper.getReferenceDataList() )
        {
            productModelList = referenceDataWrapper.getReferenceDataList();
        }

        referenceDataMap.put("productList", productModelList);

        ////////////////////////////////////////////////////////////
        referenceDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.DESC);
        try
        {
            referenceDataManager.getReferenceData(referenceDataWrapper);
        }
        catch (FrameworkCheckedException ex1)
        {
            ex1.printStackTrace();
        }
        if (null != referenceDataWrapper.getReferenceDataList())
        {
            segmentModelLsit = referenceDataWrapper.getReferenceDataList();
        }
        referenceDataMap.put("segmentList", segmentModelLsit);

        ////////////////////////////////////////////////////////////
        deviceTypeModelList = deviceTypeManager.searchDeviceTypes(DeviceTypeConstantsInterface.ALL_PAY, DeviceTypeConstantsInterface.BANKING_MIDDLEWARE,
                DeviceTypeConstantsInterface.ALLPAY_WEB, DeviceTypeConstantsInterface.WEB_SERVICE);
        referenceDataMap.put("deviceTypeList", deviceTypeModelList);
        ////////////////////////////////////////////////////////////
        referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.DESC);
        try
        {
            referenceDataManager.getReferenceData(referenceDataWrapper);
        }
        catch (FrameworkCheckedException ex1)
        {
            ex1.printStackTrace();
        }
        if (null != referenceDataWrapper.getReferenceDataList())
        {
            distributorModelList = referenceDataWrapper.getReferenceDataList();
        }

        referenceDataMap.put("distributorList", distributorModelList);
        ////////////////////////////////////////////////////////////

        //ACccount Type Limit
        referenceDataWrapper = new ReferenceDataWrapperImpl(olaCustomerAccountTypeModel, "name", SortingOrder.DESC);

        try {
            referenceDataManager.getReferenceData(referenceDataWrapper);
        }
        catch (FrameworkCheckedException ex1) {
            ex1.printStackTrace();
        }

        if (null != referenceDataWrapper.getReferenceDataList()) {
            olaCustomerAccountTypeModelList = referenceDataWrapper.getReferenceDataList();
        }
        referenceDataMap.put("olaCustomerAccountTypeList", olaCustomerAccountTypeModelList);


        if( !GenericValidator.isBlankOrNull(distributorId) )
        {
            SearchBaseWrapper searchBaseWrapper=new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(new DistributorModel(Long.parseLong(distributorId)));
            searchBaseWrapper	=	agentHierarchyManager.findDistributorLevelsByDistributorId(searchBaseWrapper);

            if(searchBaseWrapper.getCustomList()!=null && searchBaseWrapper.getCustomList().getResultsetList()!=null )
            {
                distributorLevelModelList	=	searchBaseWrapper.getCustomList().getResultsetList();
            }
        }

        referenceDataMap.put("distributorLevelList", distributorLevelModelList);
        return referenceDataMap;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        Long operatingHoursRuleId = ServletRequestUtils.getLongParameter(httpServletRequest, "operatingHoursRuleId");
        if (null != operatingHoursRuleId)
        {
            if (log.isDebugEnabled())
            {
                log.debug("id is not null....retrieving object from DB");
            }

            OperatingHoursRuleModel operatingHoursRuleModel = operatingHoursRuleFacade.loadOperatingHoursRuleModel(operatingHoursRuleId);
            return operatingHoursRuleModel;
        }
        else
        {
            if(log.isDebugEnabled())
            {
                log.debug("id is null....creating new instance of Model");
            }

            return new OperatingHoursRuleModel();
        }

    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
        OperatingHoursRuleModel operatingHoursRuleModel = (OperatingHoursRuleModel) object;
        return this.createOrUpdate(httpServletRequest, httpServletResponse,operatingHoursRuleModel,bindException);
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
        OperatingHoursRuleModel operatingHoursRuleModel = (OperatingHoursRuleModel) object;
        return this.createOrUpdate(httpServletRequest, httpServletResponse,operatingHoursRuleModel,bindException);
    }

    private ModelAndView createOrUpdate(HttpServletRequest request, HttpServletResponse response, OperatingHoursRuleModel model, BindException bindException) throws Exception {
        ModelAndView modelAndView = null;
        try
        {
            BaseWrapper baseWrapper = new BaseWrapperImpl();

            long theDate = new Date().getTime();

            OperatingHoursRuleModel operatingHoursRuleModelTemp = new OperatingHoursRuleModel();

            operatingHoursRuleModelTemp.setProductId( model.getProductId());
           /* operatingHoursRuleModelTemp.setDeviceTypeId( model.getDeviceTypeId());
            operatingHoursRuleModelTemp.setSegmentId(model.getSegmentId());
            operatingHoursRuleModelTemp.setDistributorId(model.getDistributorId());
            operatingHoursRuleModelTemp.setDistributorLevelId(model.getDistributorLevelId());
*/
            operatingHoursRuleModelTemp.setIsActive(model.getIsActive());
            operatingHoursRuleModelTemp.setOperatingHoursRuleId(model.getOperatingHoursRuleId());
            //Account Type Limit
           /* operatingHoursRuleModelTemp.setCustomerAccountTypeId(model.getCustomerAccountTypeId());*/

            if(!model.getIsActive()){
                operatingHoursRuleModelDAO.update(operatingHoursRuleModelTemp);
            }
            else {
                List<OperatingHoursRuleModel> recordList = operatingHoursRuleModelDAO.findByCriteria(operatingHoursRuleModelTemp);

                if (null != recordList && recordList.size() > 1) {
                    throw new FrameworkCheckedException("Same record already exists");
                }
                if (null != recordList && recordList.size() > 0 && (null == model.getOperatingHoursRuleId())) {
                    throw new FrameworkCheckedException("Same record already exists");
                } else if (null != recordList && recordList.size() == 1 && (null != model.getOperatingHoursRuleId())) {

                    if (model.getOperatingHoursRuleId().intValue() != recordList.get(0).getOperatingHoursRuleId()) {
                        throw new FrameworkCheckedException("Same record already exists");
                    }
                }

            }
            if (null != model.getOperatingHoursRuleId())
            {
                model.setUpdatedOn(new Date());
                model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
                baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_VELOCITY_RULE_USECASE_ID));
            }
            else
            {
                model.setCreatedOn(new Date(theDate));
                model.setUpdatedOn(new Date(theDate));
                model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
                baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.CREATE_VELOCITY_RULE_USECASE_ID));
            }

            model.setIsActive(model.getIsActive() == null ? false : model.getIsActive());
            baseWrapper.setBasePersistableModel(model);
            baseWrapper = this.operatingHoursRuleFacade.saveOrUpdate(baseWrapper);
            this.saveMessage(request, "Record saved successfully");
            modelAndView = new ModelAndView(this.getSuccessView());

        }

        catch (FrameworkCheckedException ex)
        {
            LOGGER.error("Exception occured while resending sms : " +ex.getMessage());
            super.saveMessage(request, ex.getMessage());
            return super.showForm(request, response, bindException);

        }
        catch (Exception ex)
        {
            LOGGER.error("Exception occured while resending sms : " +ex.getMessage());
            super.saveMessage(request, MessageUtil.getMessage("6075"));
            return super.showForm(request, response, bindException);

        }

        return modelAndView;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setOperatingHoursRuleFacade(OperatingHoursRuleFacade operatingHoursRuleFacade) {
        this.operatingHoursRuleFacade = operatingHoursRuleFacade;
    }

    public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager) {
        this.deviceTypeManager = deviceTypeManager;
    }

    public void setOperatingHoursRuleModelDAO(OperatingHoursRuleModelDAO operatingHoursRuleModelDAO) {
        this.operatingHoursRuleModelDAO = operatingHoursRuleModelDAO;
    }


    public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
        this.agentHierarchyManager = agentHierarchyManager;
    }
}
