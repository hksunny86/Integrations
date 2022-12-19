package com.inov8.microbank.webapp.action.bankmodule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.FinancialIntegrationModel;
import com.inov8.microbank.common.model.PermissionGroupModel;
import com.inov8.microbank.common.model.VeriflyModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.bankmodule.BankManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */


public class BankFormController
    extends AdvanceFormController
{
  private BankManager bankManager;
  private ReferenceDataManager referenceDataManager;

  private Long id;

  public BankFormController()
  {
    setCommandName("bankModel");
    setCommandClass(BankModel.class);
  }

  @Override
  protected Map loadReferenceData(HttpServletRequest httpServletRequest)
  {
    Map referenceDataMap = new HashMap();

    if (log.isDebugEnabled())
    {
      log.debug("Inside reference data");
    }

    /**
     * code fragment to load reference data  for Verifly
     *
     */

    VeriflyModel veriflyModel = new VeriflyModel();
    veriflyModel.setActive(true);
    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
        veriflyModel, "name", SortingOrder.ASC);
    referenceDataWrapper.setBasePersistableModel(veriflyModel);
    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (Exception e)
    {

    }
    List<VeriflyModel> veriflyModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      veriflyModelList = referenceDataWrapper.
          getReferenceDataList();
    }
    referenceDataMap.put("veriflyModelList", veriflyModelList);
    /**
     * code fragment to load reference data for financial institutions
     */
    FinancialIntegrationModel financialIntegrationModel = new FinancialIntegrationModel();
    referenceDataWrapper = new ReferenceDataWrapperImpl(
    		financialIntegrationModel, "name", SortingOrder.ASC);
    referenceDataWrapper.setBasePersistableModel(financialIntegrationModel);
    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (Exception e)
    {

    }
    List<FinancialIntegrationModel> financialIntegrationModelList = null;
    if(referenceDataWrapper.getReferenceDataList() != null){
    	financialIntegrationModelList = referenceDataWrapper.getReferenceDataList();
    }
    referenceDataMap.put("financialIntegrationModelList", financialIntegrationModelList);
    
    
	PermissionGroupModel permissionGroupModel = new PermissionGroupModel();
	permissionGroupModel.setAppUserTypeId(UserTypeConstantsInterface.BANK);
	
	referenceDataWrapper = new ReferenceDataWrapperImpl(
			permissionGroupModel, "name", SortingOrder.ASC);
	referenceDataWrapper.setBasePersistableModel(permissionGroupModel);
	try {
	referenceDataManager.getReferenceData(referenceDataWrapper);
	} catch (FrameworkCheckedException ex1) {
		ex1.getStackTrace();
	}
	List<PermissionGroupModel> permissionGroupModelList = null;
	if (referenceDataWrapper.getReferenceDataList() != null) 
	{
		permissionGroupModelList = referenceDataWrapper.getReferenceDataList();
	}	
	referenceDataMap.put("permissionGroupModelList", permissionGroupModelList);	      
    
    
    
    return referenceDataMap;
  }

  @Override
  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
      Exception
  {
    id = ServletRequestUtils.getLongParameter(httpServletRequest, "bankId");
    if (null != id)
    {
      if (log.isDebugEnabled())
      {
        log.debug("id is not null....retrieving object from DB");
      }

      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
      BankModel bankModel = new BankModel();
      bankModel.setPrimaryKey(id);
      searchBaseWrapper.setBasePersistableModel(bankModel);
      searchBaseWrapper = this.bankManager.loadBank(searchBaseWrapper);
      
      Long permissionGroupIdInReq = (Long)searchBaseWrapper.getObject("permissionGroupId");
      httpServletRequest.setAttribute("permissionGroupIdInReq", permissionGroupIdInReq.toString());
      
      return (BankModel) searchBaseWrapper.getBasePersistableModel();
    }
    else
    {
      if (log.isDebugEnabled())
      {
        log.debug("id is null....creating new instance of Model");
      }
      return new BankModel();
    }
  }

  @Override
  protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object object, BindException errors) throws
      Exception
  {
    BaseWrapper baseWrapper = new BaseWrapperImpl();
    Long permissionGroupId = ServletRequestUtils.getLongParameter(httpServletRequest, "permissionGroupId");
    try
    {
      BankModel bankModel = (BankModel) object;
      bankModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
      bankModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
      bankModel.setActive( bankModel.getActive() == null ? false : bankModel.getActive() ) ;
      baseWrapper.setBasePersistableModel(bankModel);      

      baseWrapper.putObject("permissionGroupId", permissionGroupId);
      baseWrapper = this.bankManager.createBank(baseWrapper);

      if (null != baseWrapper.getBasePersistableModel())
      {
        this.saveMessage(httpServletRequest,
                         "Record saved successfully");
        ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
        return modelAndView;
      }
      else
      {
        this.saveMessage(httpServletRequest, "Bank with the same name already exists ");
        return super.showForm(httpServletRequest, httpServletResponse, errors);
      }
    }
    catch (FrameworkCheckedException ex)
    {
      if(ex.getMessage().equals("VeriflyAndFinancialInstitutionDoNotMatch")){
    	  super.saveMessage(httpServletRequest,
          "Verifly and financial institution do not match.");
          return super.showForm(httpServletRequest, httpServletResponse, errors);
      }
      else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
          ex.getErrorCode())
      {
        super.saveMessage(httpServletRequest,
                          "Record could not be saved.");
        return super.showForm(httpServletRequest, httpServletResponse, errors);
      }
      throw ex;
    }
    catch (Exception ex)
    {
        super.saveMessage(httpServletRequest,MessageUtil.getMessage("6075"));
        return super.showForm(httpServletRequest, httpServletResponse, errors);
    }
  }

  @Override
  protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object object, BindException errors) throws
      Exception
  {
    BaseWrapper baseWrapper = new BaseWrapperImpl();
    try
    {
      Date nowDate = new Date();
      BankModel bankModel = (BankModel) object;
      
      Long bankId = ServletRequestUtils.getLongParameter(httpServletRequest, "bankId");
      
       BankModel tempBankModel = new BankModel();
				tempBankModel.setBankId(bankId);
				baseWrapper.setBasePersistableModel(tempBankModel);
				baseWrapper = this.bankManager
						.loadBank(baseWrapper);
				tempBankModel = (BankModel) baseWrapper
						.getBasePersistableModel();
				bankModel.setCreatedByAppUserModel(tempBankModel
						.getCreatedByAppUserModel());
				bankModel.setCreatedOn(tempBankModel
						.getCreatedOn());
				
       
      bankModel.setUpdatedOn(nowDate);
      bankModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
      bankModel.setActive( bankModel.getActive() == null ? false : bankModel.getActive() ) ;
      baseWrapper.setBasePersistableModel(bankModel);
      baseWrapper = this.bankManager.updateBank(baseWrapper);

      if (null != baseWrapper.getBasePersistableModel())
      {
        this.saveMessage(httpServletRequest,
                         "Record saved successfully");
        ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
        return modelAndView;
      }
      else
      {
        this.saveMessage(httpServletRequest,
                         "Record could not be saved.");
        return super.showForm(httpServletRequest, httpServletResponse, errors);
      }
    }
    catch (FrameworkCheckedException ex)
    {
    	if(ex.getMessage().equals("VeriflyAndFinancialInstitutionDoNotMatch")){
      	  super.saveMessage(httpServletRequest,
            "Verifly and financial institution do not match.");
            return super.showForm(httpServletRequest, httpServletResponse, errors);
        }
    	else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
          ex.getErrorCode())
      {
        super.saveMessage(httpServletRequest,
                          "Bank information could not be saved");
        return super.showForm(httpServletRequest, httpServletResponse, errors);
      }
      throw ex;
    }
    catch (Exception ex)
    {
        super.saveMessage(httpServletRequest,MessageUtil.getMessage("6075"));
        return super.showForm(httpServletRequest, httpServletResponse, errors);
    }
  }

  public void setBankManager(BankManager bankManager)
  {
    this.bankManager = bankManager;
  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

}
