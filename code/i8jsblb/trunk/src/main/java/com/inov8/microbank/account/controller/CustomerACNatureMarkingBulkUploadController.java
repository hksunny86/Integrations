package com.inov8.microbank.account.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.account.model.BlacklistMarkingViewModel;
import com.inov8.microbank.account.model.BlacklistedCnicsModel;
import com.inov8.microbank.account.service.AccountControlManager;
import com.inov8.microbank.account.vo.BlacklistMarkingBulkUploadVo;
import com.inov8.microbank.account.vo.BlacklistMarkingVo;
import com.inov8.microbank.account.vo.CustomerACNatureMarkingUploadVo;
import com.inov8.microbank.account.vo.CustomerACNatureMarkingVo;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.account.SmartMoneyAccountVO;
import com.inov8.microbank.fonepay.common.FonePayResponseCodes;
import com.inov8.microbank.fonepay.common.FonePayUtils;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManagerImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerACNatureMarkingBulkUploadController extends AdvanceFormController {

    private AppUserDAO userDao;
    private CommonCommandManagerImpl commonCommandManager;
    private AccountControlManager accountControlManager;


    public CustomerACNatureMarkingBulkUploadController()
    {
        setCommandName("customerACNatureMarkingBulkUploadVo");
        setCommandClass(CustomerACNatureMarkingUploadVo.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String, Object> refDataMap = new HashMap<>(1);
        List<LabelValueBean> acNatureMarkingList=new ArrayList<>();
        LabelValueBean acNature = new LabelValueBean("Saving", "2");
        acNatureMarkingList.add(acNature);
        acNature = new LabelValueBean("Current", "1");
        acNatureMarkingList.add(acNature);
        refDataMap.put("acNatureMarkingList",acNatureMarkingList);
        return refDataMap;
    }

    @Override
    protected CustomerACNatureMarkingUploadVo loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        return new CustomerACNatureMarkingUploadVo();
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException errors) throws Exception {
        CustomerACNatureMarkingUploadVo customerACNatureMarkingUploadVo = (CustomerACNatureMarkingUploadVo) o;
        try{
            long startTime = System.currentTimeMillis();
            HttpSession session = httpServletRequest.getSession();
            List<CustomerACNatureMarkingUploadVo> customerACNatureMarkingBulkUploadVoList = (List<CustomerACNatureMarkingUploadVo>)
                    session.getAttribute("customerACNatureMarkingBulkUploadVoList");
            List<CustomerACNatureMarkingVo> customerACNatureMarkingVoList=new ArrayList<>();
            session.removeAttribute("customerACNatureMarkingBulkUploadVoList");

            if(CollectionUtils.isNotEmpty(customerACNatureMarkingBulkUploadVoList)){
                List<CustomerACNatureMarkingUploadVo> validCustomerACNatureMarkingBulkUploadVoList = customerACNatureMarkingBulkUploadVoList.
                        subList(customerACNatureMarkingUploadVo.getInvalidRecordsCount(), customerACNatureMarkingBulkUploadVoList.size());
                if(CollectionUtils.isNotEmpty(validCustomerACNatureMarkingBulkUploadVoList)){
                    BaseWrapper baseWrapper=new BaseWrapperImpl();
                    CustomerModel customerModel=new CustomerModel();
                    CustomerACNatureMarkingVo customerACNatureMarkingVo = null;
                    for(CustomerACNatureMarkingUploadVo customerACNatureMarkingUploadVo1:validCustomerACNatureMarkingBulkUploadVoList){
                        customerACNatureMarkingVo=new CustomerACNatureMarkingVo();
                        customerACNatureMarkingVo.setCustomerACNature(customerACNatureMarkingUploadVo1.getCustomerACNature());
                        customerACNatureMarkingVo.setAppUserId(customerACNatureMarkingUploadVo1.getAppUserId());
                        customerACNatureMarkingVo.setCnicNo(customerACNatureMarkingUploadVo1.getCnic());
                        customerACNatureMarkingVoList.add(customerACNatureMarkingVo);
                    }
                    customerModel.setCustomerACNatureMarkingVoList(customerACNatureMarkingVoList);
                    baseWrapper.setBasePersistableModel(customerModel);
                    if(customerModel.getCustomerACNatureMarkingVoList().get(0).getCustomerACNature() == 2L){
                        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.MARK_SAVING_AC_NATURE_BULK_USECASE_ID );
                    }else{
                        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.MARK_CURRENT_AC_NATURE_BULK_USECASE_ID );
                    };

                    populateAuthenticationParams(baseWrapper, httpServletRequest, customerModel);
                     accountControlManager.saveCustomerACNatureMarkingWithAuthorization(baseWrapper);

                    String message = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG);
                    if(StringUtil.isNullOrEmpty(message)){
                        this.saveMessage(httpServletRequest, "Records updated successfully.");
                    }else{
                        this.saveMessage(httpServletRequest, message);
                    }
                }

            }
            else
            {
                this.saveMessage(httpServletRequest, "No Record(s) found");
                return super.showForm(httpServletRequest, httpServletResponse, errors);
            }

            long endTime = System.currentTimeMillis();
            logger.info("***** Customer AC Nature Marking Bulk Upload - Time to create records :"+((endTime-startTime)/1000)+" secs *****");
        }
        catch(Exception e){
            logger.error(e);
            this.saveErrorMessage(httpServletRequest, "Records could not be saved.");
            return super.showForm(httpServletRequest, httpServletResponse, errors);
        }

        return new ModelAndView(this.getSuccessView(), "customerACMarkingUploadVo", new CustomerACNatureMarkingUploadVo());

    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException errors) throws Exception {
        List<CustomerACNatureMarkingUploadVo> customerACNatureMarkingBulkUploadVoList = null;
        List<CustomerACNatureMarkingUploadVo> savedCustomerACNatureMarkingBulkUploadVoList=null;
        HashMap<String,CustomerACNatureMarkingUploadVo> acNatureBulkUploadVohashMap=new HashMap<>();
        CustomerACNatureMarkingUploadVo customerACNatureMarkingUploadVo = (CustomerACNatureMarkingUploadVo) o;
        List<String> nicsList=new ArrayList<>();
        MultipartFile csvFile = customerACNatureMarkingUploadVo.getCsvFile();
        ByteArrayInputStream stream = new   ByteArrayInputStream(csvFile.getBytes());
        String myString = IOUtils.toString(stream, "UTF-8");
        String lines[] = myString.split("\\r?\\n");
        boolean headerFlag = false, duplicateFlag = false;
        if(!(lines[0].toLowerCase().toString()).equals("cnic")){
            headerFlag = true;
        }

        try{
            customerACNatureMarkingBulkUploadVoList = new CustomerACNatureMarkingUploadCsvFileParser().parseCustomerAcNatureMarkingBulkUploadCsvFile(csvFile.getInputStream(), customerACNatureMarkingUploadVo);
            if(headerFlag != true){
                //remove those null CNICS records from the list
                for(int i = 0; i< customerACNatureMarkingBulkUploadVoList.size(); i++){
                    if((customerACNatureMarkingBulkUploadVoList.get(i).getCnic() == null) || customerACNatureMarkingBulkUploadVoList.get(i).getCnic().equals("") ){
                        customerACNatureMarkingBulkUploadVoList.remove(i);
                        i=i-1;
                        if(customerACNatureMarkingBulkUploadVoList.size()-1 == i){
                            break;
                        }

                    }
                }

                //Find Duplicate CNICs
                String firstCnic = null, secondCnic = null;

                for(int i = 0; i< customerACNatureMarkingBulkUploadVoList.size(); i++){

                    firstCnic =  customerACNatureMarkingBulkUploadVoList.get(i).getCnic();

                    for(int j = i+1; j < customerACNatureMarkingBulkUploadVoList.size(); j++){

                        secondCnic = customerACNatureMarkingBulkUploadVoList.get(j).getCnic();

                        if(firstCnic.equals(secondCnic)){

                            customerACNatureMarkingBulkUploadVoList.get(i).setIsDuplicate(true);
                            customerACNatureMarkingBulkUploadVoList.get(j).setIsDuplicate(true);
                            duplicateFlag = true;
                        }
                    }
                }
            }

            for(CustomerACNatureMarkingUploadVo model:customerACNatureMarkingBulkUploadVoList) {
                model.setCustomerACNature(customerACNatureMarkingUploadVo.getCustomerACNature());
                nicsList.add(model.getCnic());
            }
            savedCustomerACNatureMarkingBulkUploadVoList=( List<CustomerACNatureMarkingUploadVo> )userDao.findAppUsersByCnics(nicsList);

            for(CustomerACNatureMarkingUploadVo bulkUploadVo:savedCustomerACNatureMarkingBulkUploadVoList){
                acNatureBulkUploadVohashMap.put(bulkUploadVo.getCnic(),bulkUploadVo);
            }

            for (CustomerACNatureMarkingUploadVo model : customerACNatureMarkingBulkUploadVoList)
            {
                try {
                    model.setValidRecord(true);
                    model.setStatus("Valid");

                    if (headerFlag == true) {
                        model.setStatus("Header of csv file is missing/incorrect.");
                    }

                    long accountNature = 0L;
                    AppUserModel appUserModel = getCommonCommandManager().getAppUserModelByCNIC(model.getCnic());

                    if(appUserModel == null){
                        model.setValidRecord(false);
                        model.setStatus("Account does not exist");
                    }
                    else {
                        CustomerModel customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
                        accountNature = customerModel.getAcNature();
                        SmartMoneyAccountModel sma = getCommonCommandManager().loadSmartMoneyAccountModel(appUserModel);

                        UserDeviceAccountsModel uda = getCommonCommandManager().loadUserDeviceAccountByUserId(appUserModel.getUsername());

                        if (uda.getAccountExpired()) {
                            model.setValidRecord(false);
                            model.setStatus("Account is expired");
                        } else if (uda.getAccountLocked()) {
                            model.setValidRecord(false);
                            model.setStatus("Account is Blocked");
                        } else if (uda.getCredentialsExpired()) {
                            model.setValidRecord(false);
                            model.setStatus("Account is expired");
                        } else if (!uda.getAccountEnabled()) {
                            model.setValidRecord(false);
                            model.setStatus("Account is de-activated");
                        }

                        if(sma != null && sma.getStatusId() != null && sma.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE)){
                            model.setValidRecord(false);
                            model.setStatus("Account is not Active");
                        }

                        if(sma != null && sma.getStatusId() != null && sma.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED)){
                            model.setValidRecord(false);
                            model.setStatus("Account is Blocked");
                        }

                        if (getCommonCommandManager().isCnicBlacklisted(appUserModel.getNic())) {
                            model.setValidRecord(false);
                            model.setStatus("CNIC is Blacklisted");
                        }
                        if (appUserModel.getAccountClosedUnsettled() || appUserModel.getAccountClosedSettled()) {
                            model.setValidRecord(false);
                            model.setStatus("Customer Account has been closed.");
                        }

                        model.setAppUserId(acNatureBulkUploadVohashMap.get(model.getCnic()).getAppUserId());
                        model.setPrevRegistrationStateId(acNatureBulkUploadVohashMap.get(model.getCnic()).getPrevRegistrationStateId());
                        model.setRegistrationStateId(acNatureBulkUploadVohashMap.get(model.getCnic()).getRegistrationStateId());
                    }

                    List<ActionAuthorizationModel> existingReqList = null;
                    ActionAuthorizationModel actionAuthorizationModel = new ActionAuthorizationModel();
                    actionAuthorizationModel.setReferenceId(appUserModel.getAppUserId().toString());
                    existingReqList = getCommonCommandManager().getActionAuthorizationFacade().checkExistingRequest(actionAuthorizationModel).getResultsetList();
                    if (existingReqList != null && !existingReqList.isEmpty()){
                        model.setValidRecord(false);
                        model.setStatus("Action Authorization is already in Pending.");
                    }

//                    model.setAppUserId(acNatureBulkUploadVohashMap.get(model.getCnic()).getAppUserId());
//                    model.setPrevRegistrationStateId(acNatureBulkUploadVohashMap.get(model.getCnic()).getPrevRegistrationStateId());
//                    model.setRegistrationStateId(acNatureBulkUploadVohashMap.get(model.getCnic()).getRegistrationStateId());

                    if (!CommonUtils.isValidCnic(model.getCnic())) {
                        model.setValidRecord(false);
                        model.setStatus("CNIC number is not valid");

                         /*   } else if(nicBulkUploadVohashMap.get(model.getCnic()).getAccountClosedSettled()) {
                                model.setValidRecord(false);
                                model.setStatus("Account is Closed Settled");
                            } else if(nicBulkUploadVohashMap.get(model.getCnic()).getAccountClosedUnsettled()) {
                                model.setValidRecord(false);
                                model.setStatus("Account is Closed Un-Settled");*/

                    } else if (accountNature == 2L && (model.getCustomerACNature() != null && model.getCustomerACNature() == 2L)) {
                        model.setValidRecord(false);
                        model.setStatus("Customer Account is already in 'Saving' state");
                    } else if (accountNature == 1L && (model.getCustomerACNature() != null && model.getCustomerACNature() == 1L)) {
                        model.setValidRecord(false);
                        model.setStatus("Customer Account is already in 'Current' state");

                    } else if (model.getIsDuplicate() != null && duplicateFlag == true && model.getIsDuplicate() == true) {
                        model.setValidRecord(false);
                        model.setStatus("Duplicate CNICs.");
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        boolean allRecordsInvalid = false;
        int invalidRecordsCount = countInvalidRecords(customerACNatureMarkingBulkUploadVoList);
        if(invalidRecordsCount > 0)
        {
            allRecordsInvalid = true;
            //this.saveMessage(request, "File contains one or more invalid records. Kindly fix and upload again.");
        }

        httpServletRequest.getSession().setAttribute("customerACNatureMarkingBulkUploadVoList", customerACNatureMarkingBulkUploadVoList);
        Map<String, Object> modelMap = new HashMap<>(3);
        modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_RETRIEVE);
        modelMap.put("allRecordsInvalid", allRecordsInvalid);
        modelMap.put("invalidRecordsCount", invalidRecordsCount);
        return new ModelAndView("redirect:p_customeracnaturemarkingbulkuploadpreview.html", modelMap);
    }

    private int countInvalidRecords(List<CustomerACNatureMarkingUploadVo> customerACNatureMarkingBulkUploadVoList)
    {
        int invalidRecords = 0;
        for(CustomerACNatureMarkingUploadVo customerACNatureMarkingUploadVo : customerACNatureMarkingBulkUploadVoList)
        {
            if(!customerACNatureMarkingUploadVo.getValidRecord())
            {
                invalidRecords++;
            }
            else
            {
                continue;
            }

        }
        return invalidRecords;
    }

    @Override
    protected void populateAuthenticationParams(BaseWrapper baseWrapper, HttpServletRequest req, Object model) throws FrameworkCheckedException {
        CustomerModel customerModel = (CustomerModel) model;
        ObjectMapper mapper = new ObjectMapper();
        Long actionAuthorizationId =null;
        try
        {
            actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"authId");
        } catch (ServletRequestBindingException e1) {
            e1.printStackTrace();
        }

        DateFormat df = new SimpleDateFormat(PortalDateUtils.SHORT_DATE_FORMAT_2);
        mapper.setDateFormat(df);

        String modelJsonString = null;

        try {

            modelJsonString = mapper.writeValueAsString(customerModel);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(MessageUtil.getMessage(ActionAuthorizationConstantsInterface.REF_DATA_EXCEPTION_MSG));
        }

        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME, MessageUtil.getMessage("CustomerACNatureMarkingUploadVo.methodName"));
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS, CustomerModel.class.getSimpleName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME, CustomerModel.class.getName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME, MessageUtil.getMessage("CustomerACNatureMarkingUploadVo.Manager"));
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID, customerModel.getCustomerACNatureMarkingVoList().get(0).getAppUserId().toString());

        baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
        //baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.MARK_BLACKLISTED_BULK_USECASE_ID );
        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID,PortalConstants.ACTION_UPDATE);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME, this.getFormView());
    }

    public void setUserDao(AppUserDAO userDao) {
        this.userDao = userDao;
    }
    public void setCommonCommandManager(CommonCommandManagerImpl commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    public void setAccountControlManager(AccountControlManager accountControlManager) {
        this.accountControlManager = accountControlManager;
    }
}
