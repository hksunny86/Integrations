package com.inov8.microbank.webapp.action.agenthierarchy;

import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import com.inov8.framework.common.wrapper.*;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.dao.agenthierarchymodule.SalesHierarchyDAO;
import com.inov8.microbank.server.dao.portal.taxregimemodule.TaxRegimeDAO;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountFacade;
import com.inov8.microbank.server.service.distributormodule.DistributorLevelManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import au.com.bytecode.opencsv.CSVReader;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.agenthierarchy.AreaLevelModel;
import com.inov8.microbank.common.model.agenthierarchy.BulkAgentUploadReportModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.des.EncryptionHandler;
import com.inov8.verifly.common.model.AccountInfoModel;

@ManagedBean(name="agentBulkActionBean")
@SessionScoped
public class AgentBulkActionBean {

	private static final Log logger = LogFactory.getLog(AgentActionBean.class);

	@ManagedProperty(value="#{agentHierarchyManager}")
	private AgentHierarchyManager agentHierarchyManager;

	@ManagedProperty(value="#{referenceDataManager}")
	private ReferenceDataManager referenceDataManager;

	@ManagedProperty(value="#{phoenixFinancialInstitution}")
	private AbstractFinancialInstitution phoenixFinancialInstitution;

	@ManagedProperty(value="#{jmsAgentSender}")
	private JMSAgentSender jmsAgentSender;

	@ManagedProperty(value="#{encryptionHandler}")
	private EncryptionHandler encryptionHandler;

	@ManagedProperty(value = "#{salesHierarchyDAO}")
	private SalesHierarchyDAO salesHierarchyDAO;

	@ManagedProperty(value = "#{securityFacade}")
	private AppUserManager securityFacade;

	@ManagedProperty(value = "#{mfsAccountFacade}")
	private MfsAccountFacade mfsAccountFacade;

	@ManagedProperty(value = "#{distributorLevelManager}")
	private DistributorLevelManager distributorLevelManager;

	private List<String[]> rows = null;

	private ArrayList<UploadedFile> files = new ArrayList<UploadedFile>();

	private List<RetailerContactListViewFormModel> agentModelList = new ArrayList<RetailerContactListViewFormModel>();
	private List<BulkAgentDataHolderModel> agentMerchantDetailModelList = new ArrayList<>(0);
	private List<String> errorList = new ArrayList<String>();
	private String errors = "";

	private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]{1,50}$";

	private boolean validateActionDisabled = Boolean.TRUE;
	private boolean addBulkAgentActionDisabled = Boolean.TRUE;
	private boolean fileUploadActionDisabled = Boolean.TRUE;

	private List<DistributorModel> distributorModelList;
	private List<SelectItem> distributors;

	private List<RegionModel> regionModelList;
	private List<SelectItem> regions;

	private BulkAgentDataHolderModel bulkAgentDataHolderModel;

	private String agentNetworkId;
	private String regionId;

	@PostConstruct
	public void ini()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentBulkActionBean.ini Starts");
		}
		try
		{
			this.distributors = new ArrayList<SelectItem>();
			this.regions = new ArrayList<SelectItem>();

			SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findAllDistributors();
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				distributorModelList = searchBaseWrapper.getCustomList().getResultsetList();

				for(DistributorModel model : distributorModelList)
				{
					if(model.getActive())	// Only Active Agent Network will be availabe to creat agents.
					{
						SelectItem item = new SelectItem();
						item.setLabel(model.getName());
						item.setValue(model.getDistributorId());
						this.distributors.add(item);
					}
				}
			}
		}
		catch(FrameworkCheckedException fce)
		{
			logger.error(fce);
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentBulkActionBean.ini End");
		}
	}

	public void onChangeAgentNetwork(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentBulkActionBean.onChangeAgentNetwork Starts");
		}
		try
		{
			this.regions.clear();
			this.regionId = "-1";
			fileUploadActionDisabled = Boolean.TRUE;
			validateActionDisabled = Boolean.TRUE;
			addBulkAgentActionDisabled = Boolean.TRUE;
			this.errorList.clear();
			agentModelList.clear();
			if(files.size() > 0)
			{
				files.remove(0);
			}
			SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findRegionsByDistributorId(Long.valueOf(this.agentNetworkId));
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				regionModelList = searchBaseWrapper.getCustomList().getResultsetList();
				for(RegionModel regionModel : regionModelList)
				{
					if(regionModel.getActive())
					{
						SelectItem item = new SelectItem();
						item.setLabel(regionModel.getRegionName());
						item.setValue(regionModel.getRegionId());
						this.regions.add(item);
					}
				}
			}
		}
		catch(FrameworkCheckedException fce)
		{
			logger.error(fce);
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentBulkActionBean.onChangeAgentNetwork End");
		}
	}

	public void clearState(ActionEvent event)
	{
		validateActionDisabled = Boolean.TRUE;
		addBulkAgentActionDisabled = Boolean.TRUE;
		this.errorList.clear();
		this.agentModelList.clear();
		rows = null;
		if(files.size() > 0)
		{
			files.remove(0);
		}
	}

	public void onChangeRegion(AjaxBehaviorEvent event)
	{
		validateActionDisabled = Boolean.TRUE;
		addBulkAgentActionDisabled = Boolean.TRUE;
		this.errorList.clear();
		agentModelList.clear();
		if(files.size() > 0)
		{
			files.remove(0);
		}
		if(this.regionId.equals("-1"))
		{
			fileUploadActionDisabled = Boolean.TRUE;
		}
		else
		{
			fileUploadActionDisabled = Boolean.FALSE;
		}
	}

	public void uploadFile(FileUploadEvent event) throws Exception
	{
		this.errorList.clear();
		agentModelList.clear();
		addBulkAgentActionDisabled = Boolean.TRUE;

		UploadedFile item = event.getUploadedFile();
		files.add(item);
		CSVReader reader = new CSVReader(new InputStreamReader(item.getInputStream()));
		this.rows = reader.readAll();
		if(this.rows != null && this.rows.size() > 0)
		{
			rows.remove(0);
			this.validateActionDisabled = Boolean.FALSE;
		}
		reader.close();
	}

	public void clearBulkAgentForm(ActionEvent event)
	{
		fileUploadActionDisabled = Boolean.TRUE;
		validateActionDisabled = Boolean.TRUE;
		addBulkAgentActionDisabled = Boolean.TRUE;
		this.agentNetworkId = "-1";
		this.regionId = "-1";
		this.regions.clear();
		this.errorList.clear();
		this.agentModelList.clear();
		this.rows = null;
		if(files.size() > 0)
		{
			files.remove(0);
		}
	}

	public void saveAgentMerchantDetail(AgentMerchantDetailModel model) throws Exception{
		Boolean isUnique = mfsAccountFacade.isAlreadyExistInitAppFormNumber(model.getInitialAppFormNo());
		if (isUnique != null && isUnique.booleanValue() == true) {
			if (null != model.getReferenceNo()) {
				Boolean isReferenceNoUnique = mfsAccountFacade.isAlreadyExistReferenceNumber(model.getReferenceNo(), model.getInitialAppFormNo());
				if (isReferenceNoUnique != null && isReferenceNoUnique.booleanValue() != true)
					throw new FrameworkCheckedException("Reference No. already exist.");
			}
			if (mfsAccountFacade.loadAgentMerchantDetailModelByUserName(model.getUserName()) != null) {
				throw new FrameworkCheckedException("A user with User Name '" + model.getUserName() + "' already exist.");
			}
			if (mfsAccountFacade.isAppUserExist(model.getUserName())) {
				throw new FrameworkCheckedException("A user with User Name '" + model.getUserName() + "' already exist.");
			}
			String userPasswordToShowIncaseError = model.getPassword();
			model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			Date dateNow = new Date();
			model.setCreatedOn(dateNow);
			model.setUpdatedOn(dateNow);
			model.setPassword(EncoderUtils.encodeToSha(bulkAgentDataHolderModel.getPassword()));
			model.setConfirmPassword(EncoderUtils.encodeToSha(bulkAgentDataHolderModel.getPassword()));
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(model);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, model.getActionId());
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, model.getUsecaseId());
			mfsAccountFacade.createAgentMerchant(baseWrapper);
		}
	}

	public void addBulkAgent(ActionEvent event)
	{
		if(agentMerchantDetailModelList != null && agentMerchantDetailModelList.size() > 0)
		{
			try {
				mfsAccountFacade.saveBulkAgentData(agentMerchantDetailModelList);
			} catch (Exception e) {
				e.printStackTrace();
			}
			JSFContext.setInfoMessage(this.agentMerchantDetailModelList.size() + " agent(s) sent on queue for creation. Please verify on search agent screen.");
			agentMerchantDetailModelList.clear();
			bulkAgentDataHolderModel = null;
		}
	}

	public void validateBulkAgents(ActionEvent event) throws Exception
	{
		boolean validated = Boolean.TRUE;
		if(CommonUtils.isEmpty(this.agentNetworkId) || this.agentNetworkId.equals("-1"))
		{
			JSFContext.addErrorMessage("Agent Network is required.");
			validated = Boolean.FALSE;
		}
		if(CommonUtils.isEmpty(this.regionId) || this.regionId.equals("-1"))
		{
			JSFContext.addErrorMessage("Region is required.");
			validated = Boolean.FALSE;
		}

		if(this.rows == null || this.rows.size() == 0)
		{
			JSFContext.addErrorMessage("CSV file is not uploaded.");
			validated = Boolean.FALSE;
		}
		if(validated)
		{
			int rowIndex = 1;
			this.errorList.clear();
			this.agentModelList.clear();

			for (String[] row :rows)
			{
				int rowLength = row.length;
				if (rowLength == 33)
				{
					RetailerContactListViewFormModel model = validateAgentMerchantData(row, rowIndex);
					if(model.isValidated())
					{
						agentMerchantDetailModelList.add(bulkAgentDataHolderModel);
						this.addBulkAgentActionDisabled = Boolean.FALSE;
					}
					else
					{
						this.errorList.addAll(model.getErrors());
					}
				}
				else
				{
					this.errorList.add("Row "+ rowIndex +" Skipped, inconsistant data found.");
					logger.debug("Row Skipped, inconsistant data found:  " + row.toString());
				}
				rowIndex++;
			}
			JSFContext.setInfoMessage(this.agentMerchantDetailModelList.size() + " agent(s) are validated successfully.");
		}
	}

	private BaseWrapper prepareModelForAgentCreation(String[] row, int index)
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();

		AgentMerchantDetailModel agentMerchantDetailModel = new AgentMerchantDetailModel();
		bulkAgentDataHolderModel.setDistributorId(Long.valueOf(agentNetworkId));
		bulkAgentDataHolderModel.setRegionId(Long.valueOf(regionId));
		return baseWrapper;
	}

	private String isAlreadyExist(String[] row) {
		Boolean validated = Boolean.TRUE;
		String errorMessage = null;
		try {
			if (!mfsAccountFacade.isAlreadyExistInitAppFormNumber(row[0])) {
				errorMessage = "Initial Application Form Number: " + row[0] + " Already Exists.";
				validated = Boolean.FALSE;
			}
			if (!mfsAccountFacade.isAlreadyExistReferenceNumber(row[1], row[0])) {
				errorMessage += "/ Reference Number: " + row[1] + " Already Exists.";
				validated = Boolean.FALSE;
			}
			if (mfsAccountFacade.loadAgentMerchantDetailModelByUserName(row[8]) != null) {
				errorMessage += "/ User Name: " + row[8] + " Already Exists.";
				validated = Boolean.FALSE;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			errorMessage += "/ Exception Occurred with message: " + ex.getMessage();
			validated = false;
		}
		return errorMessage;
	}

	private Boolean loadParentAgent(String agentTypeId) throws FrameworkCheckedException
	{
		Boolean isValid = Boolean.FALSE;
		List<DistributorLvlRetContactViewModel> list = distributorLevelManager.getParentAgentsByDistributorLevelId(0L, Long.valueOf(agentTypeId),null);
		if(list != null)
		{
			for(DistributorLvlRetContactViewModel model : list)
			{
				if(model.getDistributorLevelId().equals(Long.valueOf(agentTypeId)))
				{
					bulkAgentDataHolderModel.setParentAgentId(Long.valueOf(model.getParentRetailerContactId()));
					bulkAgentDataHolderModel.setParentAgentName(model.getParentAgentName());
					bulkAgentDataHolderModel.setUltimateParentAgentId(model.getUltimateRetailerContactId());
					bulkAgentDataHolderModel.setUltimateParentAgentName(model.getUltimateAgentName());
					bulkAgentDataHolderModel.setRetailerId(model.getRetailerId());
					isValid = Boolean.TRUE;
					break;
				}
			}
		}
		return isValid;
	}

	private RetailerContactListViewFormModel validateAgentMerchantData(String[] row, int rowIndex) throws Exception
	{
		boolean validated = Boolean.TRUE;
		bulkAgentDataHolderModel = new BulkAgentDataHolderModel();
		RetailerContactListViewFormModel model = new RetailerContactListViewFormModel();
		if(model.getErrors() == null)
			model.setErrors(new ArrayList<String>());
		List<CityModel> cityList = this.loadCityList();
		if(CommonUtils.isEmpty(row[0]))
		{
			model.getErrors().add(("Initial Application Form Number is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			bulkAgentDataHolderModel.setInitialAppFormNo(row[0]);
		}
		if(CommonUtils.isEmpty(row[1]))
		{
			model.getErrors().add(("Reference Number is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			bulkAgentDataHolderModel.setReferenceNo(row[1]);
		}
		if(CommonUtils.isEmpty(row[2]))
		{
			model.getErrors().add(("Business Name is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			bulkAgentDataHolderModel.setBusinessName(row[2]);
		}
		if(CommonUtils.isEmpty(row[3]))
		{
			model.getErrors().add(("Employee Id is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setEmployeeId(Long.parseLong(row[3]));
			appUserModel = securityFacade.getAppUserModel(appUserModel);
			SalesHierarchyModel salesHierarchyModel = salesHierarchyDAO.findSaleUserByBankUserId(appUserModel.getAppUserId());
			if(null!=salesHierarchyModel){
				if(null != appUserModel){
					bulkAgentDataHolderModel.setEmployeeAppUserModel(appUserModel);
				}else{
					validated = Boolean.FALSE;
				}
			}else{
				validated = Boolean.FALSE;
			}
		}
		if(CommonUtils.isEmpty(row[4]))
		{
			model.getErrors().add(("Product Catalog Id is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			bulkAgentDataHolderModel.setProductCatalogId(Long.valueOf(row[4]));
		}
		if(CommonUtils.isEmpty(row[5]))
		{
			model.getErrors().add(("Agent Type is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findAgentLevelsByRegionId(Long.valueOf(regionId));
			boolean agentLevelFound = Boolean.FALSE;
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				List<DistributorLevelModel> agentLevelModelList = (List<DistributorLevelModel>)searchBaseWrapper.getCustomList().getResultsetList();
				for(DistributorLevelModel agentLevelModel : agentLevelModelList)
				{
					if(agentLevelModel.getName().equalsIgnoreCase(row[5].trim()))
					{
						bulkAgentDataHolderModel.setDistributorLevelId(agentLevelModel.getDistributorLevelId());
						if(agentLevelModel.getManagingLevelId() != null)
							bulkAgentDataHolderModel.setSubAgent(Boolean.TRUE);
						else
							bulkAgentDataHolderModel.setSubAgent(Boolean.FALSE);
						agentLevelFound = Boolean.TRUE;
						break;
					}
				}
			}
			if(!agentLevelFound)
			{
				model.getErrors().add("Agent Type is not found in the system in row " + rowIndex);
				validated = Boolean.FALSE;
			}
		}
		if(!CommonUtils.isEmpty(row[32]) && validated && bulkAgentDataHolderModel.getSubAgent())
		{
			Boolean isValid = this.loadParentAgent(bulkAgentDataHolderModel.getDistributorLevelId().toString());
			if(!isValid)
			{
				model.getErrors().add("Parent Agent Id is not valid in row " + rowIndex);
				validated = Boolean.FALSE;
			}
			else {
				model.getErrors().add("Parent Agent Id is not valid in row " + rowIndex);
				validated = Boolean.FALSE;
			}
		}
		if(CommonUtils.isEmpty(row[6]))
		{
			model.getErrors().add(("Area Level is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			Long areaLevelId = null;
			List<AreaLevelModel> areaLevelModelList = null;
			SearchBaseWrapper searchBaseWrapper = this.agentHierarchyManager.findAreaNamesByRegionId(Long.valueOf(regionId));
			boolean areaFound = Boolean.FALSE;
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				if(!CommonUtils.isEmpty(row[6].trim()))
				{
					for(AreaModel areaModel:(List<AreaModel>)searchBaseWrapper.getCustomList().getResultsetList())
					{
						if(areaModel.getName().equalsIgnoreCase(row[7].trim()))
						{
							bulkAgentDataHolderModel.setAreaId(areaModel.getAreaId());
							bulkAgentDataHolderModel.setAreaLevelId(areaModel.getAreaLevelModelId());
							areaFound = Boolean.TRUE;
							break;
						}
					}
				}
				else
				{
					if(!CommonUtils.isEmpty(model.getParentRetailerContactId()))
					{
						RetailerContactModel retailerContactModel = this.agentHierarchyManager.findRetailerContactById(Long.valueOf(model.getParentRetailerContactId()));
						AreaModel parentAreaModel = this.agentHierarchyManager.findAreaById(retailerContactModel.getAreaId());
						boolean parentArealLevelFound = false;
						for(AreaLevelModel arealevelModel:areaLevelModelList) // this areaLevelModelList should be in ascending order
						{
							if(!parentArealLevelFound && arealevelModel.getAreaLevelId().equals(parentAreaModel.getAreaLevelModelId()))
							{
								parentArealLevelFound = true;
							}

							if(parentArealLevelFound)
							{
								if(arealevelModel.getAreaLevelId().equals(areaLevelId)) // just to ensure current agent's area level is udnder its parent areal level
								{
									for(AreaModel areaModel:(List<AreaModel>)searchBaseWrapper.getCustomList().getResultsetList())
									{
										if(areaModel.getAreaLevelModelId().equals(areaLevelId) && areaModel.getName().equalsIgnoreCase(row[8].trim()))
										{
											areaFound = Boolean.TRUE;
											break;
										}
									}
								}
								if(areaFound)
								{
									break;
								}
							}
						}
					}
				}
			}
			if(!areaFound)
			{
				model.getErrors().add("Area Name is not found in the system in row " + rowIndex);
				validated = Boolean.FALSE;
			}
		}
		if(CommonUtils.isEmpty(row[7]))
		{
			model.getErrors().add(("Area Name is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{

		}
		if(CommonUtils.isEmpty(row[8]))
		{
			model.getErrors().add(("User Name is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			bulkAgentDataHolderModel.setUserName(row[8]);
		}
		if(CommonUtils.isEmpty(row[9]))
		{
			model.getErrors().add(("Area Level Qualification is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			bulkAgentDataHolderModel.setAcLevelQualificationId(Long.valueOf(row[9]));
		}
		if(CommonUtils.isEmpty(row[10]))
		{
			model.getErrors().add(("Purpose of Account is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			Boolean isValid = true;
			AccountPurposeModel accountPurposeModel = new AccountPurposeModel();
			ReferenceDataWrapper accountPurposeReferenceDataWrapper = new ReferenceDataWrapperImpl(accountPurposeModel, "name", SortingOrder.ASC);
			accountPurposeReferenceDataWrapper.setBasePersistableModel(accountPurposeModel);
			try
			{
				referenceDataManager.getReferenceData(accountPurposeReferenceDataWrapper);
			} catch (Exception e)
			{
				isValid = Boolean.FALSE;
				logger.error(e.getMessage(), e);
			}
			if(isValid)
			{
				List<AccountPurposeModel> accountPurposeList = null;
				if (accountPurposeReferenceDataWrapper.getReferenceDataList() != null)
				{
					accountPurposeList = accountPurposeReferenceDataWrapper.getReferenceDataList();
					if(accountPurposeList != null && accountPurposeList.size() >0)
					{
						for(AccountPurposeModel model1 :accountPurposeList)
						{
							if(model1.getName().equalsIgnoreCase(row[10]))
							{
								bulkAgentDataHolderModel.setPurposeOfAccountId(model1.getAccountPurposeId());
								break;
							}
						}
					}
					else
					{
						validated = Boolean.FALSE;
					}
				}
				else
				{
					validated = Boolean.FALSE;
				}
			}
			else
			{
				validated = Boolean.FALSE;
			}
		}
		if(CommonUtils.isEmpty(row[11]))
		{
			model.getErrors().add(("Type of Account is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			Boolean isValid = true;
			AccountNature accountType = new AccountNature();
			ReferenceDataWrapper accountNatureDataWrapper = new ReferenceDataWrapperImpl(accountType, "accountNatureId", SortingOrder.ASC);
			accountNatureDataWrapper.setBasePersistableModel(accountType);
			try
			{
				referenceDataManager.getReferenceData(accountNatureDataWrapper);
			} catch (Exception e)
			{
				logger.error(e.getMessage(), e);
				isValid = Boolean.FALSE;
				validated = Boolean.FALSE;
			}
			if(isValid)
			{
				List<AccountNature> accountTypeList = null;
				if (accountNatureDataWrapper.getReferenceDataList() != null)
				{
					accountTypeList = accountNatureDataWrapper.getReferenceDataList();
					for(AccountNature nature : accountTypeList)
					{
						if(nature.getName().equalsIgnoreCase(row[10]))
						{
							bulkAgentDataHolderModel.setAccountTypeId(nature.getAccountNatureId());
							break;
						}
					}
				}
			}
			else
				validated = Boolean.FALSE;
		}
		if(CommonUtils.isEmpty(row[12]))
		{
			model.getErrors().add(("Account Title is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			bulkAgentDataHolderModel.setAccountTitle(row[12]);
		}
		if(CommonUtils.isEmpty(row[13]))
		{
			model.getErrors().add(("Tax Regime is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			TaxRegimeModel taxRegimeModel = new TaxRegimeModel();
			ReferenceDataWrapper taxRegimeDataWrapper = new ReferenceDataWrapperImpl(taxRegimeModel, "taxRegimeId", SortingOrder.ASC);
			taxRegimeDataWrapper.setBasePersistableModel(taxRegimeModel);
			try
			{
				referenceDataManager.getReferenceData(taxRegimeDataWrapper);
			} catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
			List<TaxRegimeModel> taxRegimeList = null;
			if (taxRegimeDataWrapper.getReferenceDataList() != null)
			{
				taxRegimeList = taxRegimeDataWrapper.getReferenceDataList();
				if(taxRegimeList != null)
				{
					for(TaxRegimeModel model1 : taxRegimeList)
					{
						if(model1.getName().equalsIgnoreCase(row[13]))
						{
							bulkAgentDataHolderModel.setTaxRegimeId(model1.getTaxRegimeId());
							bulkAgentDataHolderModel.setFed(model1.getFed());
							break;
						}
					}
				}
				else
				{
					model.getErrors().add("Tax Regime is required in row " + rowIndex);
					validated = Boolean.FALSE;
				}
			}
		}
		if(CommonUtils.isEmpty(row[14]))
		{
			model.getErrors().add(("Business Address is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			bulkAgentDataHolderModel.setBusinessAddress(row[14]);
		}
		if(CommonUtils.isEmpty(row[15]))
		{
			model.getErrors().add(("Business City Id is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			if(!setCityId(row[15],"business",cityList))
			{
				model.getErrors().add(("City Id is not valid in row " + rowIndex));
				validated = Boolean.FALSE;
			}
		}
		if(CommonUtils.isEmpty(row[16]))
		{
			model.getErrors().add(("Business Commencement Date is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
		    if(CommonUtils.isValidDate(row[16],Boolean.FALSE,Boolean.FALSE))
            {
                Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(row[16]);
                bulkAgentDataHolderModel.setBusinessCommencementDate(date1);
            }
            else
            {
                model.getErrors().add(("Business Commencement Date is not valid in row " + rowIndex));
                validated = Boolean.FALSE;
            }
		}
		if(CommonUtils.isEmpty(row[17]))
		{
			model.getErrors().add(("Business Type is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(new BusinessTypeModel(), "name", SortingOrder.ASC);
			try
			{
				referenceDataManager.getReferenceData(referenceDataWrapper);
			} catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}

			List<BusinessTypeModel> businessTypeList = referenceDataWrapper.getReferenceDataList();
			if(businessTypeList != null && businessTypeList.size() > 0)
			{
				for(BusinessTypeModel model1 : businessTypeList)
				{
					if(model1.getName().equalsIgnoreCase(row[17]))
					{
						bulkAgentDataHolderModel.setBusinessTypeId(model1.getBusinessTypeId());
						break;
					}
				}
			}
			else
			{
				model.getErrors().add(("Business Type is Invalid in row " + rowIndex));
				validated = Boolean.FALSE;
			}
		}
		if(CommonUtils.isEmpty(row[18]))
		{
			model.getErrors().add(("Business Nature is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			BusinessNatureModel businessNatureModel = new BusinessNatureModel();
			ReferenceDataWrapper businessNatureDataWrapper = new ReferenceDataWrapperImpl(businessNatureModel, "businessNatureId", SortingOrder.ASC);
			businessNatureDataWrapper.setBasePersistableModel(businessNatureModel);
			try
			{
				referenceDataManager.getReferenceData(businessNatureDataWrapper);
			} catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
			List<BusinessNatureModel> businessNatureList = null;
			if (businessNatureDataWrapper.getReferenceDataList() != null)
			{
				businessNatureList = businessNatureDataWrapper.getReferenceDataList();
			}
			if(businessNatureList != null && businessNatureList.size() > 0)
			{
				for(BusinessNatureModel model1 : businessNatureList)
				{
					if(model1.getName().equalsIgnoreCase(row[18]))
					{
						bulkAgentDataHolderModel.setBusinessNatureId(model1.getBusinessNatureId());
						break;
					}
				}
			}
			else {
				model.getErrors().add(("Business Nature is Invalid in row " + rowIndex));
				validated = Boolean.FALSE;
			}
		}
		if(CommonUtils.isEmpty(row[19]))
		{
			model.getErrors().add(("Location Type is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			LocationTypeModel locationTypeModel = new LocationTypeModel();
			ReferenceDataWrapper locationTypeDataWrapper = new ReferenceDataWrapperImpl(locationTypeModel, "locationTypeId", SortingOrder.ASC);
			locationTypeDataWrapper.setBasePersistableModel(locationTypeModel);
			try
			{
				referenceDataManager.getReferenceData(locationTypeDataWrapper);
			} catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
			List<LocationTypeModel> locationTypeList = null;
			if (locationTypeDataWrapper.getReferenceDataList() != null)
			{
				locationTypeList = locationTypeDataWrapper.getReferenceDataList();
			}
			if(locationTypeList != null && locationTypeList.size() > 0)
			{
				for(LocationTypeModel model1 : locationTypeList)
				{
					if(model1.getName().equalsIgnoreCase(row[19]))
					{
						bulkAgentDataHolderModel.setLocationTypeId(model1.getLocationTypeId());
						break;
					}
				}
			}
			else
			{
				model.getErrors().add(("Location Type is Invalid in row " + rowIndex));
				validated = Boolean.FALSE;
			}
		}
		if(CommonUtils.isEmpty(row[20]))
		{
			model.getErrors().add(("Location Size is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			ReferenceDataWrapper locationSizeDataWrapper = new ReferenceDataWrapperImpl(new LocationSizeModel(), "locationSizeId", SortingOrder.ASC);
			try
			{
				referenceDataManager.getReferenceData(locationSizeDataWrapper);
			} catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
			List<LocationSizeModel> locationSizeList = null;
			if (locationSizeDataWrapper.getReferenceDataList() != null)
			{
				locationSizeList = locationSizeDataWrapper.getReferenceDataList();
			}
			if(locationSizeList != null && locationSizeList.size() > 0)
			{
				Boolean isValid = false;
				for(LocationSizeModel model1 : locationSizeList)
				{
					if(model1.getName().equalsIgnoreCase(row[20]))
					{
						bulkAgentDataHolderModel.setLocationSizeId(model1.getLocationSizeId());
						isValid = true;
						break;
					}
				}
				if(!isValid)
				{
					model.getErrors().add(("Location Size is Invalid in row " + rowIndex));
					validated = Boolean.FALSE;
				}
			}
			else
			{
				model.getErrors().add(("Location Size is Invalid in row " + rowIndex));
				validated = Boolean.FALSE;
			}
		}
		if(CommonUtils.isEmpty(row[21]))
		{
			model.getErrors().add(("Business Established Date is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			bulkAgentDataHolderModel.setEstablishedDate(row[21]);
		}
		if(CommonUtils.isEmpty(row[22]))
		{
			model.getErrors().add(("Correspondence Address is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			bulkAgentDataHolderModel.setCorresspondenceAdrees(row[22]);
		}
		if(CommonUtils.isEmpty(row[23]))
		{
			model.getErrors().add(("Correspondence City is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			if(!setCityId(row[23],"corress",cityList))
			{
				model.getErrors().add(("Correspondence City is Invalid in row " + rowIndex));
				validated = Boolean.FALSE;
			}
		}
		if(CommonUtils.isEmpty(row[24]))
		{
			model.getErrors().add(("Applicant Name is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			bulkAgentDataHolderModel.setApplicantName(row[24]);
		}
		if(CommonUtils.isEmpty(row[25]) || !CommonUtils.isValidCnic(row[25]))
		{
			model.getErrors().add(("Applicant Id Document Value is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			AppUserModel cnicModel = securityFacade.loadAppUserByCnicAndType(row[25]);
			if(cnicModel == null)
			{
				bulkAgentDataHolderModel.setDocValue(row[25]);
			}
			else
			{
				model.getErrors().add(("Applicant Id Document Value in row " + rowIndex + " already exists."));
				validated = Boolean.FALSE;
			}
		}
		if(CommonUtils.isEmpty(row[26]))
		{
			model.getErrors().add(("Applicant Id Type is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else
		{
			int id = 0;
			for (IDDocumentTypeEnum value : IDDocumentTypeEnum.values())
			{
				id = value.getIndex();
			}
			if(id != 0)
				bulkAgentDataHolderModel.setDocTypeId(Long.valueOf(id));
			else
			{
				model.getErrors().add(("Applicant Id Type is Invalid in row " + rowIndex));
				validated = Boolean.FALSE;
			}
		}
		if(CommonUtils.isEmpty(row[27]))
		{
			model.getErrors().add(("Applicant Id Expiry Date is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else {
            if(CommonUtils.isValidDate(row[27],Boolean.TRUE,Boolean.FALSE))
            {
                Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(row[16]);
                bulkAgentDataHolderModel.setDocExpiryDate(date1);
            }
            else
            {
                model.getErrors().add(("\"Applicant Id Expiry Date is Invalid in row " + rowIndex));
                validated = Boolean.FALSE;
            }
		}
		if(CommonUtils.isEmpty(row[28]))
		{
			model.getErrors().add(("Applicant DOB is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else {
            if(CommonUtils.isValidDate(row[28],Boolean.FALSE,Boolean.TRUE))
            {
                Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(row[28]);
                bulkAgentDataHolderModel.setApplicantDOB(date1);
            }
            else
            {
                model.getErrors().add(("Applicant DOB is Invalid in row " + rowIndex));
                validated = Boolean.FALSE;
            }
		}
		if(CommonUtils.isEmpty(row[29]) || !CommonUtils.isValidMobileNo(row[29]))
		{
			model.getErrors().add(("Applicant Mobile # is required in row " + rowIndex));
			validated = Boolean.FALSE;
		}
		else {
			AppUserModel tempModel = new AppUserModel();
			tempModel.setMobileNo(row[29]);
			tempModel.setAccountClosedUnsettled(true);
			Long[] appUserTypes = {UserTypeConstantsInterface.CUSTOMER,UserTypeConstantsInterface.HANDLER,UserTypeConstantsInterface.RETAILER};
			AppUserModel mobileModel = securityFacade.loadAppUserByMobileAndType(row[29],appUserTypes);
			if(mobileModel == null)
			{
				bulkAgentDataHolderModel.setApplicantMobileNo(row[29]);
			}
			else
			{
				model.getErrors().add(("Applicant Mobile # in row " + rowIndex + " already exists."));
				validated = Boolean.FALSE;
			}
		}
		if(CommonUtils.isEmpty(row[30]) || row[30].equalsIgnoreCase("FALSE"))
		{
			bulkAgentDataHolderModel.setIsScreened(0L);
		}
		else {
			bulkAgentDataHolderModel.setIsScreened(1L);
		}
		if(CommonUtils.isEmpty(row[31]) || row[31].equalsIgnoreCase("FALSE"))
		{
			bulkAgentDataHolderModel.setIsNadraVerified(0L);
		}
		else {
			bulkAgentDataHolderModel.setIsNadraVerified(1L);
		}
		String alreadyExist = null;
		if(!CommonUtils.isEmpty(row[0]) && !CommonUtils.isEmpty(row[1]) && !CommonUtils.isEmpty(row[8]))
            alreadyExist = isAlreadyExist(row);
		if(alreadyExist != null)
        {
            model.getErrors().add(alreadyExist);
            validated = Boolean.FALSE;
        }
		if(validated)
		{
			bulkAgentDataHolderModel.setPassword("@Branchless2");
			bulkAgentDataHolderModel.setIsProcessedByScheduler(0L);
			bulkAgentDataHolderModel.setDistributorId(Long.valueOf(agentNetworkId));
			bulkAgentDataHolderModel.setRegionId(Long.valueOf(regionId));
			bulkAgentDataHolderModel.setCreatedOn(new Date());
			bulkAgentDataHolderModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		}
		model.setValidated(validated);
		return model;
	}

	private List<CityModel> loadCityList()
	{
		List<CityModel> cityList = null;
		CityModel cityModel = new CityModel();
		ReferenceDataWrapper cityDataWrapper = new ReferenceDataWrapperImpl(cityModel, "cityId", SortingOrder.ASC);
		cityDataWrapper.setBasePersistableModel(cityModel);
		try
		{
			referenceDataManager.getReferenceData(cityDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		if (cityDataWrapper.getReferenceDataList() != null)
			cityList = cityDataWrapper.getReferenceDataList();

		return cityList;
	}

	private Boolean setCityId(String val,String columnType,List<CityModel> cityList)
	{
		Boolean isValid = false;
		if(cityList != null && cityList.size() >0)
		{
			for(CityModel  model1 : cityList)
			{
				if(model1.getName().equalsIgnoreCase(val))
				{
					if(columnType.equalsIgnoreCase("business"))
						bulkAgentDataHolderModel.setBusinessCityId(model1.getCityId());
					else if(columnType.equalsIgnoreCase("corress"))
						bulkAgentDataHolderModel.setCorresspondenceCityId(model1.getCityId());
					isValid = true;
					break;
				}
			}
		}
		return isValid;
	}
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	public ArrayList<UploadedFile> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<UploadedFile> files) {
		this.files = files;
	}
	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	public void setPhoenixFinancialInstitution(
			AbstractFinancialInstitution phoenixFinancialInstitution) {
		this.phoenixFinancialInstitution = phoenixFinancialInstitution;
	}

	public void setJmsAgentSender(JMSAgentSender jmsAgentSender) {
		this.jmsAgentSender = jmsAgentSender;
	}

	public boolean isValidateActionDisabled() {
		return validateActionDisabled;
	}

	public void setValidateActionDisabled(boolean validateActionDisabled) {
		this.validateActionDisabled = validateActionDisabled;
	}

	public boolean isAddBulkAgentActionDisabled() {
		return addBulkAgentActionDisabled;
	}

	public void setAddBulkAgentActionDisabled(boolean addBulkAgentActionDisabled) {
		this.addBulkAgentActionDisabled = addBulkAgentActionDisabled;
	}

	public List<SelectItem> getDistributors() {
		return distributors;
	}

	public void setDistributors(List<SelectItem> distributors) {
		this.distributors = distributors;
	}

	public List<SelectItem> getRegions() {
		return regions;
	}

	public void setRegions(List<SelectItem> regions) {
		this.regions = regions;
	}

	public String getAgentNetworkId() {
		return agentNetworkId;
	}

	public void setAgentNetworkId(String agentNetworkId) {
		this.agentNetworkId = agentNetworkId;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public boolean isFileUploadActionDisabled() {
		return fileUploadActionDisabled;
	}

	public void setFileUploadActionDisabled(boolean fileUploadActionDisabled) {
		this.fileUploadActionDisabled = fileUploadActionDisabled;
	}

	public void setEncryptionHandler(EncryptionHandler encryptionHandler) {
		this.encryptionHandler = encryptionHandler;
	}

	public void setSalesHierarchyDAO(SalesHierarchyDAO salesHierarchyDAO) {
		this.salesHierarchyDAO = salesHierarchyDAO;
	}

	public void setSecurityFacade(AppUserManager securityFacade) {
		this.securityFacade = securityFacade;
	}

	public void setMfsAccountFacade(MfsAccountFacade mfsAccountFacade) {
		this.mfsAccountFacade = mfsAccountFacade;
	}

	public void setDistributorLevelManager(DistributorLevelManager distributorLevelManager) {
		this.distributorLevelManager = distributorLevelManager;
	}
}
