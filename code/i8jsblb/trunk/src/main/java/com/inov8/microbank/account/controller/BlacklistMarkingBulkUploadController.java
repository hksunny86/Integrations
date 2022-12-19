package com.inov8.microbank.account.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.account.dao.BlacklistedCnicsDAO;
import com.inov8.microbank.account.model.BlacklistMarkingViewModel;
import com.inov8.microbank.account.model.BlacklistedCnicsModel;
import com.inov8.microbank.account.service.AccountControlManager;
import com.inov8.microbank.account.vo.BlacklistMarkingBulkUploadVo;
import com.inov8.microbank.account.vo.BlacklistMarkingVo;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.ola.util.CustomerAccountTypeConstants;

/**
 * Created by Malik on 8/23/2016.
 */
public class BlacklistMarkingBulkUploadController extends AdvanceFormController
{
    private ReferenceDataManager referenceDataManager;
    private AccountControlManager accountControlManager;
    private AppUserDAO userDao;
    private BlacklistedCnicsDAO blacklistedCnicsDAO;

    public BlacklistMarkingBulkUploadController()
    {
        setCommandName("blacklistMarkingBulkUploadVo");
        setCommandClass(BlacklistMarkingBulkUploadVo.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
    {
        Map<String, Object> refDataMap = new HashMap<>(1);
        List<LabelValueBean> blacklistMarkingList=new ArrayList<>();
        LabelValueBean blacklisted = new LabelValueBean("Mark", "true");
        blacklistMarkingList.add(blacklisted);
        blacklisted = new LabelValueBean("Unmark", "false");
        blacklistMarkingList.add(blacklisted);
        refDataMap.put("blacklistMarkingList",blacklistMarkingList);
        return refDataMap;
    }

    @Override
    protected BlacklistMarkingBulkUploadVo loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception
    {
        return new BlacklistMarkingBulkUploadVo();
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    protected ModelAndView onCreate(HttpServletRequest request,
                                    HttpServletResponse response, Object command, BindException errors) throws Exception
    {
        BlacklistMarkingBulkUploadVo blacklistMarkingBulkUploadVo = (BlacklistMarkingBulkUploadVo) command;
        try
        {
            long startTime = System.currentTimeMillis();
            HttpSession session = request.getSession();
            List<BlacklistMarkingBulkUploadVo> blacklistMarkingBulkUploadVoList = (List<BlacklistMarkingBulkUploadVo>) session.getAttribute("blacklistMarkingBulkUploadVoList");
            List<BlacklistMarkingVo> blacklistMarkingVoList=new ArrayList<>();
            session.removeAttribute("blacklistMarkingBulkUploadVoList");

            if(CollectionUtils.isNotEmpty(blacklistMarkingBulkUploadVoList))
            {
                List<BlacklistMarkingBulkUploadVo> validBlacklistMarkingBulkUploadVoList = blacklistMarkingBulkUploadVoList.subList(blacklistMarkingBulkUploadVo.getInvalidRecordsCount(), blacklistMarkingBulkUploadVoList.size());
                if(CollectionUtils.isNotEmpty(validBlacklistMarkingBulkUploadVoList))
                {
                    BaseWrapper baseWrapper=new BaseWrapperImpl();
                    BlacklistMarkingViewModel blacklistMarkingViewModel=new BlacklistMarkingViewModel();
                    BlacklistMarkingVo blacklistMarkingVo;
                    for(BlacklistMarkingBulkUploadVo blacklistMarkingBulkUploadVo1:validBlacklistMarkingBulkUploadVoList){
                        blacklistMarkingVo=new BlacklistMarkingVo();
                        blacklistMarkingVo.setBlacklisted(blacklistMarkingBulkUploadVo1.getIsBlacklisted());
                        blacklistMarkingVo.setAppUserId(blacklistMarkingBulkUploadVo1.getAppUserId());
                        blacklistMarkingVo.setCnicNo(blacklistMarkingBulkUploadVo1.getCnic());
                        blacklistMarkingVoList.add(blacklistMarkingVo);
                    }
                    blacklistMarkingViewModel.setBlacklistMarkingVoList(blacklistMarkingVoList);
                    baseWrapper.setBasePersistableModel(blacklistMarkingViewModel);
                    if(blacklistMarkingViewModel.getBlacklistMarkingVoList().get(0).getBlacklisted() == true){
                    	 baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.MARK_BLACKLISTED_BULK_USECASE_ID );
                    }else{
                    	baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.UNMARK_BLACKLISTED_BULK_USECASE_ID );
                    }
                    
                   
                    
                    
                    populateAuthenticationParams(baseWrapper, request, blacklistMarkingViewModel);
                    accountControlManager.saveBlacklistMarkingViewModelWithAuthorization(baseWrapper);
                    
                    String message = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG);
                    if(StringUtil.isNullOrEmpty(message)){
                    	this.saveMessage(request, "Records updated successfully.");
                    }else{                    	
                    	this.saveMessage(request, message);
                    }

                }
            }
            else
            {
                this.saveMessage(request, "No Record(s) found");
                return super.showForm(request, response, errors);
            }

            long endTime = System.currentTimeMillis();
            logger.info("***** Blacklist Marking Bulk Upload - Time to create records :"+((endTime-startTime)/1000)+" secs *****");
            
           
        }
        catch(Exception e)
        {
            logger.error(e);
            this.saveErrorMessage(request, "Records could not be saved.");
            return super.showForm(request, response, errors);
        }
    
        return new ModelAndView(this.getSuccessView(), "blacklistMarkingBulkUploadVo", new BlacklistMarkingBulkUploadVo());
    }

    
    
    
    @Override
    protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception
    {
        List<BlacklistMarkingBulkUploadVo> blacklistMarkingBulkUploadVoList = null;
        List<BlacklistMarkingBulkUploadVo> savedBlacklistMarkingBulkUploadVoList=null;
        HashMap<String,BlacklistMarkingBulkUploadVo> nicBulkUploadVohashMap=new HashMap<>();
        BlacklistMarkingBulkUploadVo blacklistMarkingBulkUploadVo = (BlacklistMarkingBulkUploadVo) command;
        List<String> nicsList=new ArrayList<>();
        MultipartFile csvFile = blacklistMarkingBulkUploadVo.getCsvFile();
        ByteArrayInputStream stream = new   ByteArrayInputStream(csvFile.getBytes());
        String myString = IOUtils.toString(stream, "UTF-8");
        String lines[] = myString.split("\\r?\\n");
        boolean headerFlag = false, duplicateFlag = false;
        if(!(lines[0].toLowerCase().toString()).equals("cnic")){
        	headerFlag = true;
        }
        
            try
            {
                blacklistMarkingBulkUploadVoList = new BlacklistMarkingBulkUploadCsvFileParser().parseBlacklistMarkingBulkUploadCsvFile(csvFile.getInputStream(), blacklistMarkingBulkUploadVo);
             
                if(headerFlag != true){
                	  //remove those null CNICS records from the list
                    for(int i = 0; i< blacklistMarkingBulkUploadVoList.size(); i++){
                 	   if((blacklistMarkingBulkUploadVoList.get(i).getCnic() == null) || blacklistMarkingBulkUploadVoList.get(i).getCnic().equals("") ){
                 		   blacklistMarkingBulkUploadVoList.remove(i);
                 		   i=i-1;
                 		   if(blacklistMarkingBulkUploadVoList.size()-1 == i){
                 			   break;
                 		   }
                 		   
                 	   }
                    }
                    
                    //Find Duplicate CNICs
                    String firstCnic = null, secondCnic = null;
                    
                    for(int i = 0; i< blacklistMarkingBulkUploadVoList.size(); i++){
                    	
                    	firstCnic =  blacklistMarkingBulkUploadVoList.get(i).getCnic();
                    	
                    	for(int j = i+1; j < blacklistMarkingBulkUploadVoList.size(); j++){
                    		
                    		secondCnic = blacklistMarkingBulkUploadVoList.get(j).getCnic();
                    		
                    		if(firstCnic.equals(secondCnic)){
                    			
                    			blacklistMarkingBulkUploadVoList.get(i).setIsDuplicate(true);
                    			blacklistMarkingBulkUploadVoList.get(j).setIsDuplicate(true);
                    			duplicateFlag = true;
                    		}
                    	}
                    	
                    	
                    	/*if(blacklistMarkingBulkUploadVoList.get(i).getCnic().equals(blacklistMarkingBulkUploadVoList.get(i+1).getCnic()) ){
                    		duplicateFlag = true;
                    		blacklistMarkingBulkUploadVoList.get(i).setIsDuplicate(true);
                    		blacklistMarkingBulkUploadVoList.get(i+1).setIsDuplicate(true);
                    		break;
                    	}*/
                    }
                }
                            
                for(BlacklistMarkingBulkUploadVo model:blacklistMarkingBulkUploadVoList) {
                    model.setIsBlacklisted(blacklistMarkingBulkUploadVo.getIsBlacklisted());
                    nicsList.add(model.getCnic());
                }
                savedBlacklistMarkingBulkUploadVoList=( List<BlacklistMarkingBulkUploadVo> )userDao.findAppUsersByNICs(nicsList);


                for(BlacklistMarkingBulkUploadVo bulkUploadVo:savedBlacklistMarkingBulkUploadVoList){
                    nicBulkUploadVohashMap.put(bulkUploadVo.getCnic(),bulkUploadVo);
                }

                //********************************************************************************
                for (BlacklistMarkingBulkUploadVo model : blacklistMarkingBulkUploadVoList)
                {
                    try
                    {
                        model.setValidRecord(true);
                        model.setStatus("Valid");
                        //MARK ANYONE BLACKLISTED EVEN IF ITS CUSTOMER DONT EXIST
                        if(nicBulkUploadVohashMap.get(model.getCnic())==null) {
                            BlacklistedCnicsModel blacklistedCnicsModel = blacklistedCnicsDAO.findBlacklistedCnicsModelByCnicNo(model.getCnic());

                            if(blacklistedCnicsModel != null && model.getIsBlacklisted() && blacklistedCnicsModel.getBlacklisted() == model.getIsBlacklisted()) {
                                model.setValidRecord(false);
                                model.setStatus("CNIC is already Marked Blacklisted");
                            } else if(blacklistedCnicsModel != null && !model.getIsBlacklisted() && blacklistedCnicsModel.getBlacklisted() == model.getIsBlacklisted()) {
                                model.setValidRecord(false);
                                model.setStatus("CNIC is already Unmarked Blacklisted");
                            } else if(!CommonUtils.isValidCnic(model.getCnic())) {
                                model.setValidRecord(false);
                                model.setStatus("CNIC number is not valid");
                            }
                            else if(model.getIsDuplicate() != null && duplicateFlag == true && model.getIsDuplicate() == true ){
                           	 	model.setValidRecord(false);
                           	 	model.setStatus("Duplicate CNICs.");
                           }
                            
                            if(headerFlag == true){
                            	model.setStatus("Header of csv file is missing/incorrect.");
                            }
                            
                            
                        }
                        else {
                        	
                        	boolean flag = accountControlManager.isCnicBlacklisted(model.getCnic());
                        	
                            model.setAppUserId(nicBulkUploadVohashMap.get(model.getCnic()).getAppUserId());
                            model.setPrevRegistrationStateId(nicBulkUploadVohashMap.get(model.getCnic()).getPrevRegistrationStateId());
                            model.setRegistrationStateId(nicBulkUploadVohashMap.get(model.getCnic()).getRegistrationStateId());

                            if(!CommonUtils.isValidCnic(model.getCnic())) {
                                model.setValidRecord(false);
                                model.setStatus("CNIC number is not valid");

                         /*   } else if(nicBulkUploadVohashMap.get(model.getCnic()).getAccountClosedSettled()) {
                                model.setValidRecord(false);
                                model.setStatus("Account is Closed Settled");
                            } else if(nicBulkUploadVohashMap.get(model.getCnic()).getAccountClosedUnsettled()) {
                                model.setValidRecord(false);
                                model.setStatus("Account is Closed Un-Settled");*/
                                
                            }else if(flag == true && model.getIsBlacklisted() == true){
                            	 model.setValidRecord(false);
                                 model.setStatus("CNIC is already Marked Blacklisted");
                            }else if(flag == false && model.getIsBlacklisted() == false){
                            	model.setValidRecord(false);
                                model.setStatus("CNIC is already Unmarked Blacklisted");
                                
                            }else if(model.getIsDuplicate() != null && duplicateFlag == true && model.getIsDuplicate() == true ){
                           	 	model.setValidRecord(false);
                           	 	model.setStatus("Duplicate CNICs.");
                           }
                         
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                //*****************************************************************************************
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        boolean allRecordsInvalid = false;
        int invalidRecordsCount = countInvalidRecords(blacklistMarkingBulkUploadVoList);
        if(invalidRecordsCount > 0)
        {
            allRecordsInvalid = true;
            //this.saveMessage(request, "File contains one or more invalid records. Kindly fix and upload again.");
        }

        request.getSession().setAttribute("blacklistMarkingBulkUploadVoList", blacklistMarkingBulkUploadVoList);
        Map<String, Object> modelMap = new HashMap<>(3);
        modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_RETRIEVE);
        modelMap.put("allRecordsInvalid", allRecordsInvalid);
        modelMap.put("invalidRecordsCount", invalidRecordsCount);
        return new ModelAndView("redirect:p_blacklistmarkingbulkuploadpreview.html", modelMap);
    }

    //*******************************************************************************************************************************************
    @Override
    protected void populateAuthenticationParams(BaseWrapper baseWrapper, HttpServletRequest req, Object model) throws FrameworkCheckedException {

    	BlacklistMarkingViewModel blacklistMarkingViewModelVo = (BlacklistMarkingViewModel) model;
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

            modelJsonString = mapper.writeValueAsString(blacklistMarkingViewModelVo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(MessageUtil.getMessage(ActionAuthorizationConstantsInterface.REF_DATA_EXCEPTION_MSG));
        }


        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME, MessageUtil.getMessage("BlacklistMarkingViewModel.methodName"));
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS, BlacklistMarkingViewModel.class.getSimpleName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME, BlacklistMarkingViewModel.class.getName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME, MessageUtil.getMessage("BlacklistMarkingViewModel.Manager"));
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID, blacklistMarkingViewModelVo.getAppUserId());

        baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
       //baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.MARK_BLACKLISTED_BULK_USECASE_ID );
        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID,PortalConstants.ACTION_UPDATE);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME, this.getFormView());

    }
    
    //***********************************************************************************************************************************
    
    private int countInvalidRecords(List<BlacklistMarkingBulkUploadVo> blacklistMarkingBulkUploadVoList)
    {
        int invalidRecords = 0;
        for(BlacklistMarkingBulkUploadVo blacklistMarkingBulkUploadVo : blacklistMarkingBulkUploadVoList)
        {
            if(!blacklistMarkingBulkUploadVo.getValidRecord())
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

    private void removeSpecialAccountTypes(CopyOnWriteArrayList<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList)
    {
        //Iterator<OlaCustomerAccountTypeModel> it = olaCustomerAccountTypeModelList.iterator();
        //So far only one special account type exists which is SETTLEMENT (id = 3L)
        for (OlaCustomerAccountTypeModel model : olaCustomerAccountTypeModelList) {
            if(model.getCustomerAccountTypeId().longValue() == CustomerAccountTypeConstants.SETTLEMENT
                    || model.getCustomerAccountTypeId().longValue() == UserTypeConstantsInterface.WALKIN_CUSTOMER){
                olaCustomerAccountTypeModelList.remove(model);
            }
        }
    }

    
    
    public void setUserDao(AppUserDAO userDao)
    {
        this.userDao = userDao;
    }

    public void setAccountControlManager(AccountControlManager accountControlManager)
    {
        this.accountControlManager = accountControlManager;
    }
    public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
    {
        this.referenceDataManager = referenceDataManager;
    }

    public void setBlacklistedCnicsDAO(BlacklistedCnicsDAO blacklistedCnicsDAO)
    {
        this.blacklistedCnicsDAO = blacklistedCnicsDAO;
    }

}
