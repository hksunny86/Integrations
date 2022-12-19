package com.inov8.microbank.server.service.commandmodule;

import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import com.inov8.microbank.common.util.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.productmodule.CatalogVersionListViewModel;
import com.inov8.microbank.common.model.productmodule.CategoryModel;
import com.inov8.microbank.common.model.productmodule.ProdCatalogDetailListViewModel;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;


public class UpdateCatalogCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String catVersionNo;
	protected String deviceTypeId;
	CustomList<ProdCatalogDetailListViewModel> catalogServiceList;
	CustomList<ProdCatalogDetailListViewModel> prodCatDetCustomList;
	boolean loadCatalogFlag = true;
    boolean isConsumerAppRequest = false;

	CustomList<CategoryModel> categoryCustomList;
	private static final int CATEGORY_CASH_IN = 1;
	private static final int CATEGORY_CASH_OUT = 2;
	private static final int CATEGORY_FUNDS_TRANSFER = 3;
	private static final int CATEGORY_BILL_PAYMENT = 4;
	private static final int CATEGORY_RETAIL_PAYMENT = 17;
	private static final int CATEGORY_AGENT_TRANSFER = 19;
	private static final int CATEGORY_ACCOUNT_OPENING = 21;
	private static final int CATEGORY_HRA_ACCOUNT_OPENING = 23;
	
	protected final Log logger = LogFactory.getLog(UpdateCatalogCommand.class);

	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of UpdateCatalogCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		try
		{
			Boolean validationErr = Boolean.FALSE;
			ValidationErrors validationError;//commonCommandManager.checkActiveAppUser(appUserModel);
			if(!validationErr)
			{
				CatalogVersionListViewModel catalogVersionListViewModel = new CatalogVersionListViewModel();
				ProdCatalogDetailListViewModel prodCatalogDetailListViewModel = new ProdCatalogDetailListViewModel();
				catalogVersionListViewModel.setAppUserTypeId(appUserModel.getAppUserTypeId());
				catalogVersionListViewModel.setAppUserId(appUserModel.getAppUserId());
				//*********************************************************************************//
				//****************				For Loading Agent Web Catalog *********************//
				//*********************************************************************************//
				if(this.deviceTypeId.equals(DeviceTypeConstantsInterface.ALLPAY_WEB.toString())){
					catalogVersionListViewModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
				}else{
					catalogVersionListViewModel.setDeviceTypeId(Long.valueOf(this.deviceTypeId));
				}
				//**********************************************************************************//
				
				if(appUserModel.getDistributorContactId() == null) {
					if(appUserModel.getRetailerContactId() != null || appUserModel.getCustomerId() != null) {
						try {
							catalogVersionListViewModel = getVersion(catalogVersionListViewModel,commonCommandManager);
						}
						catch (CommandException e)
						{
							if( e.getMessage().equalsIgnoreCase(this.getMessageSource().getMessage("updateCatalogCommand.catalogDoesnotExist", null,null)) ) {
								/**
								 * Get Default Catalog ID from DEVICE_TYPE table and get its version from PRODUCT_CATALOG table.
								 * -----------------------------------------------------------------------------------------------------------------
								 */
								catalogVersionListViewModel.setProductCatalogName(CommandFieldConstants.KEY_DEFAULT_ALLPAY_CATALOG_VERSION_NAME);								
								
								catalogVersionListViewModel = getVersion(catalogVersionListViewModel,commonCommandManager);		
								/**
								 * ------------------------------------------------------------------------------------------------------------------
								 */
							}
							else
								throw e;							
						}
						if(catalogVersionListViewModel.getCatalogVersionNo() == Integer.parseInt(catVersionNo)) {
							loadCatalogFlag = false;
						}
						else {
							catVersionNo = catalogVersionListViewModel.getCatalogVersionNo().toString();
							prodCatalogDetailListViewModel.setProductCatalogId(catalogVersionListViewModel.getProductCatalogId());
							prodCatalogDetailListViewModel.setDeviceTypeId(Long.valueOf(this.deviceTypeId));
							loadCatalogServiceForUser(prodCatalogDetailListViewModel,commonCommandManager);
						}
					}
					else if(appUserModel.getAppUserTypeId().equals(UserTypeConstantsInterface.HANDLER)) {
						BaseWrapper baseWrapper = new BaseWrapperImpl();
						UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
						userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
						baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
						commonCommandManager.loadMfs(baseWrapper);
						userDeviceAccountsModel = (UserDeviceAccountsModel)baseWrapper.getBasePersistableModel();
						if(userDeviceAccountsModel.getProdCatalogId() != null) {
							catalogVersionListViewModel.setProductCatalogId(userDeviceAccountsModel.getProdCatalogId());
							catalogVersionListViewModel = getVersion(catalogVersionListViewModel,commonCommandManager);
							if(catalogVersionListViewModel.getCatalogVersionNo() == Integer.parseInt(catVersionNo)) {
								loadCatalogFlag = false;
							}
							else {
								catVersionNo = catalogVersionListViewModel.getCatalogVersionNo().toString();
								prodCatalogDetailListViewModel.setProductCatalogId(catalogVersionListViewModel.getProductCatalogId());
								prodCatalogDetailListViewModel.setDeviceTypeId(Long.valueOf(this.deviceTypeId));
								loadCatalogServiceForUser(prodCatalogDetailListViewModel,commonCommandManager);
							}
						}
						else {
							throw new CommandException(this.getMessageSource().getMessage("updateCatalogCommand.catalogDoesnotExist", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}
					}
					else
					{
						if(Integer.parseInt(catVersionNo) != 0)
						{
							catalogVersionListViewModel.setProductCatalogName(CommandFieldConstants.KEY_DEFAULT_CATALOG_VERSION_NAME);
							catalogVersionListViewModel = getVersion(catalogVersionListViewModel,commonCommandManager);
							if(catalogVersionListViewModel.getCatalogVersionNo() == Integer.parseInt(catVersionNo))
							{
								loadCatalogFlag = false;
							}
							else
							{
								catVersionNo = catalogVersionListViewModel.getCatalogVersionNo().toString();
								prodCatalogDetailListViewModel.setProductCatalogId(catalogVersionListViewModel.getProductCatalogId());
								loadCatalogServices(prodCatalogDetailListViewModel,commonCommandManager);
								loadProductCatalog(prodCatalogDetailListViewModel,commonCommandManager);
							}
						}
						else 
						{
							catalogVersionListViewModel.setProductCatalogName(CommandFieldConstants.KEY_DEFAULT_CATALOG_VERSION_NAME);
							catalogVersionListViewModel = getVersion(catalogVersionListViewModel,commonCommandManager);
							catVersionNo = catalogVersionListViewModel.getCatalogVersionNo().toString();
							prodCatalogDetailListViewModel.setProductCatalogId(catalogVersionListViewModel.getProductCatalogId());
							loadCatalogServices(prodCatalogDetailListViewModel,commonCommandManager);
							loadProductCatalog(prodCatalogDetailListViewModel,commonCommandManager);
						}
					}
				}
			}
			else
			{
				//throw new CommandException(validationError.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
		catch(FrameworkCheckedException ex)
		{
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of UpdateCatalogCommand.execute()");
		}
	}

	
	
	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of UpdateCatalogCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		catVersionNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CAT_VER);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);

        if(UserTypeConstantsInterface.CUSTOMER.longValue() == appUserModel.getAppUserTypeId()
                && deviceTypeId.equals(DeviceTypeConstantsInterface.ALL_PAY.toString())){

            isConsumerAppRequest = true;
        }

		if(deviceTypeId.equals(DeviceTypeConstantsInterface.ALLPAY_WEB.toString()))
		{
			catVersionNo="0";
		}

		if(logger.isDebugEnabled())
		{
			logger.debug("End of UpdateCatalogCommand.prepare()");
		}
	}

	@Override
	public String response() 
	{
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of UpdateCatalogCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(catVersionNo,validationErrors,"Catalog Version No");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		/*if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(catVersionNo,validationErrors,"Catalog Version No");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}*/
		if(logger.isDebugEnabled())
		{
			logger.debug("End of UpdateCatalogCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of UpdateCatalogCommand.toXML()");
		}
		StringBuilder xml = new StringBuilder();
		if(loadCatalogFlag)
		{
			List<ProdCatalogDetailListViewModel> cataServiceList = null;
			
			if(catalogServiceList != null && catalogServiceList.getResultsetList() != null)
			{
				cataServiceList = catalogServiceList.getResultsetList();
			}
			
			//<cat version="0">
			xml.append(XMLConstants.TAG_SYMBOL_OPEN) //<
				.append(XMLConstants.TAG_CAT)               //cat  
				.append(XMLConstants.TAG_SYMBOL_SPACE)		//  	
				.append(XMLConstants.ATTR_CAT_VER)          //version
				.append(XMLConstants.TAG_SYMBOL_EQUAL)      //= 
				.append(XMLConstants.TAG_SYMBOL_QUOTE)      //" 
				.append(catVersionNo)                    
				.append(XMLConstants.TAG_SYMBOL_QUOTE)      //"
				.append(XMLConstants.TAG_SYMBOL_CLOSE);     //>
			
			if(cataServiceList != null && cataServiceList.size() > 0 && this.categoryCustomList != null && this.categoryCustomList.getResultsetList() != null)
			{
				xml.append(XMLConstants.TAG_SYMBOL_OPEN).append(XMLConstants.TAG_CATEGORIES).append(XMLConstants.TAG_SYMBOL_CLOSE);
					
				for(ProdCatalogDetailListViewModel localCatalogService: cataServiceList)
				{
					
					String categoryXml = "";
					String categoryCloseXml = "";
					String productXml = "";
					String supplierXml = "";
					String supplierCloseXml = "";
					String prodsXml = "";
					String prodsCloseXml = "";
					
					
					productXml = prepareProductXml(localCatalogService);
					prodsXml = prepareProdsXml();
					prodsCloseXml = prepareProdsCloseXml();
					categoryCloseXml = prepareCategoryCloseXml();
					
					if(localCatalogService.getShowSupplierInMenu())
					{
						supplierXml = prepareSupplierXml(localCatalogService);
						supplierCloseXml = prepareSupplierCloseXml();
					}
					
					
					CategoryModel categoryModel = null;
					for(CategoryModel model: this.categoryCustomList.getResultsetList())
					{
						if(model.getCategoryId().equals(localCatalogService.getCategoryId()))
						{
							categoryModel = model;
							break;
						}
					}
					
					categoryXml = prepareCategoryXml(categoryModel);
					
					int indexOfCategoryTag = xml.indexOf(categoryXml);
					
					String parentCategoryXml = "";
					
					if(indexOfCategoryTag == -1)
					{
						while(categoryModel.getParentCategoryId() != null)
						{
							for(CategoryModel model: this.categoryCustomList.getResultsetList())
							{
								if(model.getCategoryId().equals(categoryModel.getParentCategoryId()))
								{
									categoryModel = model;
									break;
								}
							}
							
							parentCategoryXml = prepareCategoryXml(categoryModel);
							
							indexOfCategoryTag = xml.indexOf(parentCategoryXml);
							
							if(indexOfCategoryTag == -1)
							{
								categoryXml = parentCategoryXml + categoryXml;
								parentCategoryXml = "";
							}
							else
							{
								break;
							}
						}
					}
					
					if(indexOfCategoryTag == -1)
					{
						xml.append(categoryXml).append(prodsXml).append(supplierXml).append(productXml);
						if(!supplierXml.equals(""))
						{
							xml.append(supplierCloseXml);
						}
						xml.append(prodsCloseXml);
						int categoryOccurances = StringUtils.countOccurrencesOf(xml.toString(), XMLConstants.TAG_SYMBOL_OPEN + XMLConstants.TAG_CATEGORY);
						int categoryCloseOccurances = StringUtils.countOccurrencesOf(xml.toString(), categoryCloseXml);
						categoryOccurances = categoryOccurances - categoryCloseOccurances; 
						for(int i=0;i<categoryOccurances;i++)
						{
							xml.append(categoryCloseXml);
						}
					}
					else if(indexOfCategoryTag != -1 && !parentCategoryXml.equals(""))
					{
						String prefixXml = xml.substring(0, indexOfCategoryTag + parentCategoryXml.length());
						xml = xml.delete(0, indexOfCategoryTag + parentCategoryXml.length());
						prefixXml = prefixXml + categoryXml + prodsXml + supplierXml + productXml;
						if(!supplierXml.equals(""))
						{
							prefixXml = prefixXml + supplierCloseXml;
						}
						prefixXml = prefixXml + prodsCloseXml;
						int categoryOccurances = StringUtils.countOccurrencesOf(categoryXml.toString(), XMLConstants.TAG_SYMBOL_OPEN + XMLConstants.TAG_CATEGORY);
						for(int i=0;i<categoryOccurances;i++)
						{
							prefixXml = prefixXml + categoryCloseXml;
						}
						
						xml = new StringBuilder(prefixXml + xml.toString()); 
					}
					else if(indexOfCategoryTag != -1)
					{
						String prefixXml = xml.substring(0, indexOfCategoryTag + categoryXml.length());
						xml = xml.delete(0, indexOfCategoryTag + categoryXml.length());
						String middleXml = "";
						if(xml.toString().startsWith(XMLConstants.TAG_SYMBOL_OPEN + XMLConstants.TAG_CATEGORY))
						{
							int categoryTagCounter = 0;
							while(categoryTagCounter >= 0)
							{
								indexOfCategoryTag = xml.indexOf(XMLConstants.TAG_SYMBOL_OPEN + XMLConstants.TAG_CATEGORY);
								int indexOfCloseCategoryTag = xml.indexOf(categoryCloseXml);
								if(indexOfCategoryTag < indexOfCloseCategoryTag && indexOfCategoryTag != -1)
								{
									middleXml = middleXml + xml.substring(0, indexOfCategoryTag + (XMLConstants.TAG_SYMBOL_OPEN + XMLConstants.TAG_CATEGORY).length());
									xml = xml.delete(0, indexOfCategoryTag + (XMLConstants.TAG_SYMBOL_OPEN + XMLConstants.TAG_CATEGORY).length());
									categoryTagCounter++;
								}
								else if(indexOfCategoryTag > indexOfCloseCategoryTag || indexOfCategoryTag == -1)
								{
									categoryTagCounter--;
									if(categoryTagCounter < 0)
									{
										break;
									}
									middleXml = middleXml + xml.substring(0, indexOfCloseCategoryTag + categoryCloseXml.length());
									xml = xml.delete(0, indexOfCloseCategoryTag + categoryCloseXml.length());
								}
							}
							if(xml.toString().startsWith(categoryCloseXml))
							{
								middleXml = middleXml + prodsXml + supplierXml + productXml;
								if(!supplierXml.equals(""))
								{
									middleXml = middleXml + supplierCloseXml;
								}
								middleXml = middleXml + prodsCloseXml;
							}
							else if(xml.toString().startsWith(prodsXml))
							{
								middleXml = middleXml + xml.substring(0, prodsXml.length());
								xml = xml.delete(0, prodsXml.length());
								int indexOfProdsCloseXmlTag = xml.indexOf(prodsCloseXml);
								String mostInnerXml = xml.substring(0, indexOfProdsCloseXmlTag + prodsCloseXml.length());
								xml = xml.delete(0, indexOfProdsCloseXmlTag + prodsCloseXml.length());
								if(supplierXml.equals(""))
								{
									middleXml = middleXml + productXml + mostInnerXml; 
								}
								else
								{
									int indexOfSupplierXml = mostInnerXml.indexOf(supplierXml);
									if(indexOfSupplierXml != -1)
									{
										String prefixMostInnerXml = mostInnerXml.substring(0, indexOfSupplierXml + supplierXml.length());
										mostInnerXml = mostInnerXml.substring(prefixMostInnerXml.length(), mostInnerXml.length());
										middleXml = middleXml + prefixMostInnerXml + productXml + mostInnerXml; 
									}
									else if(indexOfSupplierXml == -1)
									{
										middleXml = middleXml + supplierXml + productXml + supplierCloseXml + mostInnerXml;
									}
								}
							}
						}
						else if(xml.toString().startsWith(prodsXml))
						{
							middleXml = middleXml + xml.substring(0, prodsXml.length() );
							xml = xml.delete(0, prodsXml.length());
							int indexOfProdsCloseXmlTag = xml.indexOf(prodsCloseXml);
							String mostInnerXml = xml.substring(0, indexOfProdsCloseXmlTag + prodsCloseXml.length());
							xml = xml.delete(0, indexOfProdsCloseXmlTag + prodsCloseXml.length());
							if(supplierXml.equals(""))
							{
								middleXml = middleXml + productXml + mostInnerXml; 
							}
							else
							{
								int indexOfSupplierXml = mostInnerXml.indexOf(supplierXml);
								if(indexOfSupplierXml != -1)
								{
									String prefixMostInnerXml = mostInnerXml.substring(0, indexOfSupplierXml + supplierXml.length());
									mostInnerXml = mostInnerXml.substring(prefixMostInnerXml.length(), mostInnerXml.length());
									middleXml = middleXml + prefixMostInnerXml + productXml + mostInnerXml; 
								}
								else if(indexOfSupplierXml == -1)
								{
									middleXml = middleXml + supplierXml + productXml + supplierCloseXml + mostInnerXml;
								}
							}
						}
					xml = new StringBuilder(prefixXml + middleXml + xml.toString());	
					}
				}
				xml.append(XMLConstants.TAG_SYMBOL_OPEN).append(XMLConstants.TAG_SYMBOL_SLASH).append(XMLConstants.TAG_CATEGORIES).append(XMLConstants.TAG_SYMBOL_CLOSE);
			}
			xml.append(XMLConstants.TAG_SYMBOL_OPEN).append(XMLConstants.TAG_SYMBOL_SLASH).append(XMLConstants.TAG_CAT).append(XMLConstants.TAG_SYMBOL_CLOSE);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of UpdateCatalogCommand.toXML()");
		}

        return xml.toString();
	}
	
	private CatalogVersionListViewModel getVersion(CatalogVersionListViewModel catalogVersionListViewModel,CommonCommandManager commandCommandManager) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of UpdateCatalogCommand.getVersion()");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(catalogVersionListViewModel);
		try
		{
			searchBaseWrapper = commandCommandManager.loadCatalogVersion(searchBaseWrapper);
			List<CatalogVersionListViewModel> list = searchBaseWrapper.getCustomList().getResultsetList();
			if(list != null && list.size() > 0)
			{
				catalogVersionListViewModel = list.get(0);
			}
			else
			{
				throw new CommandException(this.getMessageSource().getMessage("updateCatalogCommand.catalogDoesnotExist", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
		catch(FrameworkCheckedException ex)
		{
			
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of UpdateCatalogCommand.getVersion()");
		}
		return catalogVersionListViewModel;
	}
	
	private void loadCatalogServices(ProdCatalogDetailListViewModel prodCatDetListViewModel , CommonCommandManager commonCommandManager) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of UpdateCatalogCommand.loadCatalogServices()");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(prodCatDetListViewModel);
		try
		{
			searchBaseWrapper = commonCommandManager.loadProdCatalogForBillPayment(searchBaseWrapper);
			catalogServiceList = searchBaseWrapper.getCustomList();
		}
		catch(FrameworkCheckedException ex)
		{
			
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of UpdateCatalogCommand.loadCatalogServices()");
		}
	}
	
	private void loadProductCatalog(ProdCatalogDetailListViewModel prodCatDetListViewModel,CommonCommandManager commonCommandManager) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of UpdateCatalogCommand.loadProductCatalog()");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(prodCatDetListViewModel);
		
		if( appUserModel.getRetailerContactId() != null )
		{
			LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
			sortingOrderMap.put("sequenceNo", SortingOrder.ASC);
			searchBaseWrapper.setSortingOrderMap(sortingOrderMap) ;
		}
		
		try
		{
			searchBaseWrapper = commonCommandManager.loadProdCatalogForDiscAndVar(searchBaseWrapper);
			prodCatDetCustomList = searchBaseWrapper.getCustomList();
		}
		catch(FrameworkCheckedException ex)
		{
			
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of UpdateCatalogCommand.loadProductCatalog()");
		}
		
	}
	
	private void loadCatalogProductForRetailers(ProdCatalogDetailListViewModel prodCatDetListViewModel,CommonCommandManager commonCommandManager) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of UpdateCatalogCommand.loadProductCatalog()");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(prodCatDetListViewModel);
		
//		LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
//		sortingOrderMap.put("sequenceNo", SortingOrder.ASC);
//		searchBaseWrapper.setSortingOrderMap(sortingOrderMap) ;
		
		
		try
		{
			searchBaseWrapper = commonCommandManager.loadCatalogProductsForRetailers(searchBaseWrapper);
			prodCatDetCustomList = searchBaseWrapper.getCustomList();
		}
		catch(FrameworkCheckedException ex)
		{
			
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of UpdateCatalogCommand.loadProductCatalog()");
		}
		
	}
	
	private void loadCatalogServiceForUser(ProdCatalogDetailListViewModel prodCatDetListViewModel , CommonCommandManager commonCommandManager) throws CommandException
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(prodCatDetListViewModel);
		try
		{
			logger.info("this.deviceTypeId: " + this.deviceTypeId+" isConsuemrAppRequest:"+isConsumerAppRequest);

            if(isConsumerAppRequest){
                searchBaseWrapper = commonCommandManager.loadCatalogServiceForCustomer(searchBaseWrapper);
            }else{
                searchBaseWrapper = commonCommandManager.loadCatalogServiceForRetailers(searchBaseWrapper);
            }

            catalogServiceList = searchBaseWrapper.getCustomList();
			this.categoryCustomList = commonCommandManager.fetchAllProductCatalogCategories();

		}catch(FrameworkCheckedException ex){
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
	}


	private String prepareProductXml(ProdCatalogDetailListViewModel catalogService)
	{
		StringBuilder productXml = new StringBuilder();
		productXml.append(XMLConstants.TAG_SYMBOL_OPEN);
		productXml.append(XMLConstants.TAG_PROD);
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_ID);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		productXml.append(catalogService.getProductId());
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);

		//categoryid
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_CAT_ID);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		productXml.append(catalogService.getCategoryId());
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		//end category

		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_FLOW_ID);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		productXml.append(catalogService.getDeviceFlowId());
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_CONSUMER_LABEL);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		if(catalogService.getConsumerLabel() != null)
		{
			productXml.append(catalogService.getConsumerLabel());
		}
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_AMOUNT_REQUIRED);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		if(catalogService.getAmtRequired() != null)
		{
			productXml.append(catalogService.getAmtRequired()?1:0);
		}
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_DO_VALIDATE);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		if(catalogService.getAmtRequired() != null)
		{
			productXml.append(catalogService.getDoValidate()?1:0);
		}
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_CNSMR_INPUT_TYPE);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		if(catalogService.getConsumerInputType() != null)
		{
			productXml.append(StringEscapeUtils.escapeXml(catalogService.getConsumerInputType()));
		}
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE)
		
		.append(XMLConstants.ATTR_MULTIPLE)
		.append(XMLConstants.TAG_SYMBOL_EQUAL)
		.append(XMLConstants.TAG_SYMBOL_QUOTE);
		if(catalogService.getMultiples() != null)
		{
			productXml.append(NumberFormat.getNumberInstance(Locale.US).format(catalogService.getMultiples()));	
		}
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_MIN_AMOUNT);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		if(catalogService.getMinLimit() != null)
		{
			productXml.append(catalogService.getMinLimit());
		}
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_MIN_AMOUNT_FORMATTED);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		if(catalogService.getMinLimit() != null)
		{
			productXml.append(NumberFormat.getNumberInstance(Locale.US).format(catalogService.getMinLimit()));
		}
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_MAX_AMOUNT);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		if(catalogService.getMaxLimit() != null)
		{
			productXml.append(catalogService.getMaxLimit());	
		}
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_MAX_AMOUNT_FORMATTED);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		if(catalogService.getMaxLimit() != null)
		{
			productXml.append(NumberFormat.getNumberInstance(Locale.US).format(catalogService.getMaxLimit()));	
		}
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);

		//*************** new param consumerMaxLength
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_CONSUMER_MAX_LENGTH);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		productXml.append((catalogService.getMaxConsumerLength()==null)?"":catalogService.getMaxConsumerLength());
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		//*************** new param consumerMinLength
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_CONSUMER_MIN_LENGTH);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		productXml.append((catalogService.getMinConsumerLength()==null)?"":catalogService.getMinConsumerLength());
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		//*************** new param name
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_NAME);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		productXml.append( isConsumerAppRequest ? CommonUtils.decorateProductName(catalogService.getProductName()) : catalogService.getProductName());
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		//*************** new param inquiryRequired
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_INQUIRY_REQUIRED);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		productXml.append((catalogService.getInquiryRequired()==null || !catalogService.getInquiryRequired())?"0":"1");
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		//*************** new param partialPaymentAllowed
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_PARTIAL_PAYMENT_REQUIRED);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		productXml.append((catalogService.getPartialPaymentAllowed()==null || !catalogService.getPartialPaymentAllowed())?"0":"1");
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);

		//url
		//categoryid
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_URL);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		productXml.append(catalogService.getUrl());
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		//end category
		//end url

		//prodDenom
		//categoryid
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_PROD_DENOM);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		productXml.append(catalogService.getProdDenom());
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		//end category
		//end prodDenom

		//denomFlag
		//categoryid
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_DENOM_FLAG);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		productXml.append((catalogService.getDenomFlag()==null || !catalogService.getDenomFlag())?"0":"1");
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		//end category
		//end denomFlag

		//denomString
		//categoryid
		productXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		productXml.append(XMLConstants.ATTR_DENOM_STRING);
		productXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		productXml.append(catalogService.getDenomString());
		productXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		//end category
		//end denomString

		productXml.append(XMLConstants.TAG_SYMBOL_SLASH);
		productXml.append(XMLConstants.TAG_SYMBOL_CLOSE);
//		productXml.append(catalogService.getProductName());
//		productXml.append(XMLConstants.TAG_SYMBOL_OPEN);
//		productXml.append(XMLConstants.TAG_SYMBOL_SLASH);
//		productXml.append(XMLConstants.TAG_PROD);
//		productXml.append(XMLConstants.TAG_SYMBOL_CLOSE);
		
		return productXml.toString();
	}
	
	private String prepareSupplierXml(ProdCatalogDetailListViewModel catalogService)
	{
		StringBuilder supplierXml = new StringBuilder();
		supplierXml.append(XMLConstants.TAG_SYMBOL_OPEN);
		supplierXml.append(XMLConstants.TAG_SUPP);
		supplierXml.append(XMLConstants.TAG_SYMBOL_SPACE);
		supplierXml.append(XMLConstants.ATTR_NAME);
		supplierXml.append(XMLConstants.TAG_SYMBOL_EQUAL);
		supplierXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		supplierXml.append(catalogService.getSupplierName());
		supplierXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		supplierXml.append(XMLConstants.TAG_SYMBOL_CLOSE);
		
		return supplierXml.toString();
	}
	
	private String prepareSupplierCloseXml()
	{
		StringBuilder supplierCloseXml = new StringBuilder();
		supplierCloseXml.append(XMLConstants.TAG_SYMBOL_OPEN);
		supplierCloseXml.append(XMLConstants.TAG_SYMBOL_SLASH);
		supplierCloseXml.append(XMLConstants.TAG_SUPP);
		supplierCloseXml.append(XMLConstants.TAG_SYMBOL_CLOSE);
		
		return supplierCloseXml.toString();
	}	
	private String prepareProdsXml()
	{
		StringBuilder prodsXml = new StringBuilder();
		prodsXml.append(XMLConstants.TAG_SYMBOL_OPEN);
		prodsXml.append(XMLConstants.TAG_PRODS);
		prodsXml.append(XMLConstants.TAG_SYMBOL_CLOSE);
		
		return prodsXml.toString();
	}
	
	private String prepareProdsCloseXml()
	{
		StringBuilder prodsCloseXml = new StringBuilder();
		prodsCloseXml.append(XMLConstants.TAG_SYMBOL_OPEN);
		prodsCloseXml.append(XMLConstants.TAG_SYMBOL_SLASH);
		prodsCloseXml.append(XMLConstants.TAG_PRODS);
		prodsCloseXml.append(XMLConstants.TAG_SYMBOL_CLOSE);
		
		return prodsCloseXml.toString();
	}
	
	private String prepareCategoryCloseXml()
	{
		StringBuilder categoryCloseXml = new StringBuilder();
		categoryCloseXml.append(XMLConstants.TAG_SYMBOL_OPEN);
		categoryCloseXml.append(XMLConstants.TAG_SYMBOL_SLASH);
		categoryCloseXml.append(XMLConstants.TAG_CATEGORY);
		categoryCloseXml.append(XMLConstants.TAG_SYMBOL_CLOSE);
		
		return categoryCloseXml.toString();
	}
	private String prepareCategoryXmlTillRoot(ProdCatalogDetailListViewModel catalogService, String xml)
	{
		StringBuilder categoryXmlTillRoot = new StringBuilder(xml);
		CategoryModel categoryModel = null;
		for(CategoryModel model: this.categoryCustomList.getResultsetList())
		{
			if(model.getCategoryId().equals(catalogService.getCategoryId()))
			{
				categoryModel = model;
				break;
			}
		}
		
		while(categoryModel.getParentCategoryId() != null)
		{
			for(CategoryModel model: this.categoryCustomList.getResultsetList())
			{
				if(model.getCategoryId().equals(categoryModel.getParentCategoryId()))
				{
					categoryModel = model;
					break;
				}
			}
			
			StringBuilder categoryXml = new StringBuilder();
			
			categoryXml.append(XMLConstants.TAG_SYMBOL_OPEN)
			.append(XMLConstants.TAG_CATEGORY)
			.append(XMLConstants.TAG_SYMBOL_SPACE)
			.append(XMLConstants.ATTR_CAT_ID)
			.append(XMLConstants.TAG_SYMBOL_EQUAL)
			.append(XMLConstants.TAG_SYMBOL_QUOTE)
			.append(categoryModel.getCategoryId())
			.append(XMLConstants.TAG_SYMBOL_QUOTE)
			.append(XMLConstants.TAG_SYMBOL_SPACE)
			.append(XMLConstants.ATTR_NAME)
			.append(XMLConstants.TAG_SYMBOL_EQUAL)
			.append(XMLConstants.TAG_SYMBOL_QUOTE)
			.append(categoryModel.getName())
			.append(XMLConstants.TAG_SYMBOL_QUOTE)
			.append(XMLConstants.TAG_SYMBOL_SPACE)
			.append(XMLConstants.ATTR_CAT_ICON)
			.append(XMLConstants.TAG_SYMBOL_EQUAL)
			.append(XMLConstants.TAG_SYMBOL_QUOTE);
			if(categoryModel.getIconExt() != null)
			{
				//categoryXml.append(categoryModel.getCategoryId() + "." + categoryModel.getIconExt());				
				//int categoryId = categoryModel.getCategoryId().intValue();
				//categoryXml.append(getIconName(categoryId));
				categoryXml.append(categoryModel.getIconLocation());
			}
			categoryXml.append(XMLConstants.TAG_SYMBOL_QUOTE)
			
			.append(XMLConstants.TAG_SYMBOL_SPACE)
			.append(XMLConstants.ATTR_IS_PRODUCT)
			.append(XMLConstants.TAG_SYMBOL_EQUAL)
			.append(XMLConstants.TAG_SYMBOL_QUOTE);
			categoryXml.append(categoryModel.getIsProduct()?"1":"0");
			categoryXml.append(XMLConstants.TAG_SYMBOL_QUOTE)
			
			.append(XMLConstants.TAG_SYMBOL_CLOSE);
			
			categoryXmlTillRoot = categoryXml.append(categoryXmlTillRoot.toString());
		}
		
		return categoryXmlTillRoot.toString();
	}
	private String prepareCategoryXml(CategoryModel categoryModel)
	{
		StringBuilder categoryXml = new StringBuilder();
		
		categoryXml.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(XMLConstants.TAG_CATEGORY)
		.append(XMLConstants.TAG_SYMBOL_SPACE)
		.append(XMLConstants.ATTR_CAT_ID)
		.append(XMLConstants.TAG_SYMBOL_EQUAL)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(categoryModel.getCategoryId())
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(XMLConstants.TAG_SYMBOL_SPACE)
		.append(XMLConstants.ATTR_NAME)
		.append(XMLConstants.TAG_SYMBOL_EQUAL)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(categoryModel.getName())
		.append(XMLConstants.TAG_SYMBOL_QUOTE);
		
		
		categoryXml.append(XMLConstants.TAG_SYMBOL_SPACE)
		.append(XMLConstants.ATTR_CAT_ICON)
		.append(XMLConstants.TAG_SYMBOL_EQUAL)
		.append(XMLConstants.TAG_SYMBOL_QUOTE);
		if(categoryModel.getIconExt() != null)
		{
			//categoryXml.append(categoryModel.getCategoryId() + "." + categoryModel.getIconExt());
			//int categoryId = categoryModel.getCategoryId().intValue();
			//categoryXml.append(getIconName(categoryId));
			categoryXml.append(categoryModel.getIconLocation());
		}
		categoryXml.append(XMLConstants.TAG_SYMBOL_QUOTE);
		
		categoryXml.append(XMLConstants.TAG_SYMBOL_SPACE)
		.append(XMLConstants.ATTR_IS_PRODUCT)
		.append(XMLConstants.TAG_SYMBOL_EQUAL)
		.append(XMLConstants.TAG_SYMBOL_QUOTE);
		categoryXml.append(categoryModel.getIsProduct()?"1":"0");
		categoryXml.append(XMLConstants.TAG_SYMBOL_QUOTE);

		//parent category attr
		categoryXml.append(XMLConstants.TAG_SYMBOL_SPACE)
				.append(XMLConstants.ATTR_PARENT_CAT_ID)
				.append(XMLConstants.TAG_SYMBOL_EQUAL)
				.append(XMLConstants.TAG_SYMBOL_QUOTE);
		categoryXml.append(categoryModel.getParentCategoryId() == null ? "-1" : categoryModel.getParentCategoryId());
		categoryXml.append(XMLConstants.TAG_SYMBOL_QUOTE);

		//end parent category
		
		categoryXml.append(XMLConstants.TAG_SYMBOL_CLOSE);
		
		return categoryXml.toString();
	}
	
	private String getIconName(int categoryId) {
		String iconName = "";		
		switch(categoryId) {
		case CATEGORY_CASH_IN:
			iconName = "main_icon_cash_in.png";
			break;
		case CATEGORY_CASH_OUT:
			iconName = "main_icon_cash_out.png";
			break;
		case CATEGORY_FUNDS_TRANSFER:
			iconName = "main_icon_funds_transfer.png";
			break;
		case CATEGORY_BILL_PAYMENT:
			iconName = "main_icon_bill_payment.png";
			break;
		case CATEGORY_RETAIL_PAYMENT:
			iconName = "main_icon_retail_payment.png";
			break;
		case CATEGORY_AGENT_TRANSFER:
			iconName = "main_icon_agent_transfer.png";
			break;
		case CATEGORY_ACCOUNT_OPENING:
			iconName = "main_icon_account_open.png";						
			break;
			case CATEGORY_HRA_ACCOUNT_OPENING:
				iconName = "main_icon_account_open.png";
				break;
	}
		return iconName; 
	}
}
