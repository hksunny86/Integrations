package com.inov8.microbank.server.service.commandmodule;

/**
 * Project Name: 			Microbank	
 * @author 					Jawwad Farooq
 * Creation Date: 			April 17, 2009  			
 * Description:				
 */


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.productmodule.CatalogVersionListViewModel;
import com.inov8.microbank.common.model.productmodule.ProdCatalogDetailListViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.util.XMLConstants;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;


public class GetLatestCatalogCommand extends BaseCommand 
{	
	protected String catVersionNo;
	protected String deviceTypeId;
	CustomList<ProdCatalogDetailListViewModel> catalogServiceList;
	CustomList<ProdCatalogDetailListViewModel> prodCatDetCustomList;
	boolean loadCatalogFlag = true;
	
	protected final Log logger = LogFactory.getLog(GetLatestCatalogCommand.class);
	
		
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of GetLatestCatalogCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		try
		{
				CatalogVersionListViewModel catalogVersionListViewModel = new CatalogVersionListViewModel();
				ProdCatalogDetailListViewModel prodCatalogDetailListViewModel = new ProdCatalogDetailListViewModel();
				AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
				catalogVersionListViewModel.setAppUserId(appUserModel.getAppUserId());
				
				if(appUserModel.getRetailerContactId() != null)
				{
					try
					{
						catalogVersionListViewModel = getVersion(catalogVersionListViewModel,commonCommandManager);
					}
					catch (CommandException e)
					{
						if( e.getMessage().equalsIgnoreCase(this.getMessageSource().getMessage("updateCatalogCommand.catalogDoesnotExist", null,null)) )
						{
							if( deviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()) )
								catalogVersionListViewModel.setProductCatalogName(CommandFieldConstants.KEY_DEFAULT_CATALOG_VERSION_NAME);
							else if( deviceTypeId.equals(DeviceTypeConstantsInterface.ALLPAY_WEB.toString()) )
								catalogVersionListViewModel.setProductCatalogName(CommandFieldConstants.KEY_DEFAULT_ALLPAY_CATALOG_VERSION_NAME);
							
							
							catalogVersionListViewModel = getVersion(catalogVersionListViewModel,commonCommandManager);								
						}
						else
							throw e;							
					}
//					if(catalogVersionListViewModel.getCatalogVersionNo() == Integer.parseInt(catVersionNo))
//					{
//						loadCatalogFlag = false;
//					}
//					else
					{
						catVersionNo = catalogVersionListViewModel.getCatalogVersionNo().toString();
						prodCatalogDetailListViewModel.setProductCatalogId(catalogVersionListViewModel.getProductCatalogId());
						//loadCatalogProductForRetailers(prodCatalogDetailListViewModel,commonCommandManager);
						loadCatalogServiceForRetailers(prodCatalogDetailListViewModel,commonCommandManager);
					}
				}
				else
				{
					if( deviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()) )
						catalogVersionListViewModel.setProductCatalogName(CommandFieldConstants.KEY_DEFAULT_CATALOG_VERSION_NAME);
					else if( deviceTypeId.equals(DeviceTypeConstantsInterface.ALLPAY_WEB.toString()) )
						catalogVersionListViewModel.setProductCatalogName(CommandFieldConstants.KEY_DEFAULT_ALLPAY_CATALOG_VERSION_NAME);
					
					catalogVersionListViewModel = getVersion(catalogVersionListViewModel,commonCommandManager);
					catVersionNo = catalogVersionListViewModel.getCatalogVersionNo().toString();
					prodCatalogDetailListViewModel.setProductCatalogId(catalogVersionListViewModel.getProductCatalogId());
					loadCatalogServices(prodCatalogDetailListViewModel,commonCommandManager);
					loadProductCatalog(prodCatalogDetailListViewModel,commonCommandManager);
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
		
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
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
		
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		if(!validationErrors.hasValidationErrors())
		{
			
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
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
		StringBuilder strBuilder = new StringBuilder();
		boolean supplierTagFlag = false;
		if(loadCatalogFlag)
		{
			String supplierName;
			List<ProdCatalogDetailListViewModel> cataServiceList = null;
			List<ProdCatalogDetailListViewModel> prodCatDetList = null;
			
			if(catalogServiceList != null && catalogServiceList.getResultsetList() != null)
			{
				cataServiceList = catalogServiceList.getResultsetList();	
			}
			if(prodCatDetCustomList != null && prodCatDetCustomList.getResultsetList() != null)
			{
				prodCatDetList = prodCatDetCustomList.getResultsetList();			
			}
			strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
				.append(XMLConstants.TAG_CAT)
				.append(XMLConstants.TAG_SYMBOL_SPACE)			
				.append(XMLConstants.ATTR_CAT_VER)
				.append(XMLConstants.TAG_SYMBOL_EQUAL)
				.append(XMLConstants.TAG_SYMBOL_QUOTE)
				.append(catVersionNo) 
				.append(XMLConstants.TAG_SYMBOL_QUOTE)
				.append(XMLConstants.TAG_SYMBOL_CLOSE);
			
			if(prodCatDetList != null && prodCatDetList.size() > 0)
			{
			
				ProdCatalogDetailListViewModel tempProdCatDetModel = prodCatDetList.get(0);
			
				
				strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
					.append(XMLConstants.TAG_PRODS)
					.append(XMLConstants.TAG_SYMBOL_CLOSE);
				
				supplierName = tempProdCatDetModel.getSupplierName();
				
				
				strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
					.append(XMLConstants.TAG_SUPP)
					.append(XMLConstants.TAG_SYMBOL_SPACE)
					.append(XMLConstants.ATTR_SUPP_NAME)
					.append(XMLConstants.TAG_SYMBOL_EQUAL)
					.append(XMLConstants.TAG_SYMBOL_QUOTE)
					.append(supplierName)
					.append(XMLConstants.TAG_SYMBOL_QUOTE)
					
					// Adding Supplier ID
					.append(XMLConstants.TAG_SYMBOL_SPACE)
					.append(XMLConstants.ATTR_MSG_ID)
					.append(XMLConstants.TAG_SYMBOL_EQUAL)
					.append(XMLConstants.TAG_SYMBOL_QUOTE)
					.append(tempProdCatDetModel.getSupplierId())
					.append(XMLConstants.TAG_SYMBOL_QUOTE)
					
					.append(XMLConstants.TAG_SYMBOL_CLOSE);
					
				for(int i = 0; i < prodCatDetList.size(); i++)
				{
					if(supplierTagFlag)	
					{
						strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
							.append(XMLConstants.TAG_SUPP)
							.append(XMLConstants.TAG_SYMBOL_SPACE)
							.append(XMLConstants.ATTR_SUPP_NAME)
							.append(XMLConstants.TAG_SYMBOL_EQUAL)
							.append(XMLConstants.TAG_SYMBOL_QUOTE)
							.append(prodCatDetList.get(i).getSupplierName())
							.append(XMLConstants.TAG_SYMBOL_QUOTE)
							
							.append(XMLConstants.TAG_SYMBOL_SPACE)
							.append(XMLConstants.ATTR_MSG_ID)
							.append(XMLConstants.TAG_SYMBOL_EQUAL)
							.append(XMLConstants.TAG_SYMBOL_QUOTE)
							.append(prodCatDetList.get(i).getSupplierId())
							.append(XMLConstants.TAG_SYMBOL_QUOTE)
							
							.append(XMLConstants.TAG_SYMBOL_CLOSE);
						supplierName = prodCatDetList.get(i).getSupplierName();
						supplierTagFlag = false;
					}	
				
					strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
						.append(XMLConstants.TAG_PROD)
						.append(XMLConstants.TAG_SYMBOL_SPACE)
						.append(XMLConstants.ATTR_PROD_ID)
						.append(XMLConstants.TAG_SYMBOL_EQUAL)
						.append(XMLConstants.TAG_SYMBOL_QUOTE)
						.append(prodCatDetList.get(i).getProductId()) 
						.append(XMLConstants.TAG_SYMBOL_QUOTE)
						.append(XMLConstants.TAG_SYMBOL_SPACE)
						.append(XMLConstants.ATTR_PROD_TYPE)
						.append(XMLConstants.TAG_SYMBOL_EQUAL)
						.append(XMLConstants.TAG_SYMBOL_QUOTE)
						.append(prodCatDetList.get(i).getServiceTypeId()) 
						.append(XMLConstants.TAG_SYMBOL_QUOTE)
						
						.append(XMLConstants.TAG_SYMBOL_SPACE)
						.append(XMLConstants.ATTR_PROD_AMOUNT)
						.append(XMLConstants.TAG_SYMBOL_EQUAL)
						.append(XMLConstants.TAG_SYMBOL_QUOTE)
						.append(replaceNullWithZero(prodCatDetList.get(i).getUnitPrice())) 
						.append(XMLConstants.TAG_SYMBOL_QUOTE)
						
						
						.append(XMLConstants.TAG_SYMBOL_SPACE)
						.append(XMLConstants.ATTR_GENI_DEV_FLOW_ID)
						.append(XMLConstants.TAG_SYMBOL_EQUAL)
						.append(XMLConstants.TAG_SYMBOL_QUOTE)
						.append(replaceNullWithEmpty(prodCatDetList.get(i).getDeviceFlowId() == null ? "" : 
							prodCatDetList.get(i).getDeviceFlowId().toString()))
						.append(XMLConstants.TAG_SYMBOL_QUOTE)
						
						.append(XMLConstants.TAG_SYMBOL_SPACE)
						.append(XMLConstants.ATTR_FORMATED_AMOUNT)
						.append(XMLConstants.TAG_SYMBOL_EQUAL)
						.append(XMLConstants.TAG_SYMBOL_QUOTE)
						.append(Formatter.formatNumbers((prodCatDetList.get(i).getUnitPrice()))) 
						.append(XMLConstants.TAG_SYMBOL_QUOTE)
						
						.append(XMLConstants.TAG_SYMBOL_CLOSE)
						.append(prodCatDetList.get(i).getProductName())
						.append(XMLConstants.TAG_SYMBOL_OPEN)
						.append(XMLConstants.TAG_SYMBOL_SLASH)
						.append(XMLConstants.TAG_PROD)
						.append(XMLConstants.TAG_SYMBOL_CLOSE);
					
					if(prodCatDetList.size() > i+1)
					{
						if(!supplierName.equalsIgnoreCase(prodCatDetList.get(i+1).getSupplierName()))
						{
							strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
								.append(XMLConstants.TAG_SYMBOL_SLASH)
								.append(XMLConstants.TAG_SUPP)
								.append(XMLConstants.TAG_SYMBOL_CLOSE);
							supplierTagFlag = true;
						}
					}
				}
				strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
					.append(XMLConstants.TAG_SYMBOL_SLASH)
					.append(XMLConstants.TAG_SUPP)
					.append(XMLConstants.TAG_SYMBOL_CLOSE);
				
				strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
					.append(XMLConstants.TAG_SYMBOL_SLASH)
					.append(XMLConstants.TAG_PRODS)
					.append(XMLConstants.TAG_SYMBOL_CLOSE);
			}
			if(cataServiceList != null && cataServiceList.size() > 0)
			{
				strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
					.append(XMLConstants.TAG_SERVS)
					.append(XMLConstants.TAG_SYMBOL_CLOSE);
					
				for(ProdCatalogDetailListViewModel localCatalogService:cataServiceList)
				{
					strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
						.append(XMLConstants.TAG_SERV)
						.append(XMLConstants.TAG_SYMBOL_SPACE)
						.append(XMLConstants.ATTR_SERV_ID)
						.append(XMLConstants.TAG_SYMBOL_EQUAL)
						.append(XMLConstants.TAG_SYMBOL_QUOTE)
						.append(localCatalogService.getProductId())
						.append(XMLConstants.TAG_SYMBOL_QUOTE)
						.append(XMLConstants.TAG_SYMBOL_SPACE)
						.append(XMLConstants.ATTR_GENI_DEV_FLOW_ID)
						.append(XMLConstants.TAG_SYMBOL_EQUAL)
						.append(XMLConstants.TAG_SYMBOL_QUOTE)
						.append(this.replaceNullWithOne(localCatalogService.getDeviceFlowId()))
						.append(XMLConstants.TAG_SYMBOL_QUOTE);
						
					

					strBuilder.append(XMLConstants.TAG_SYMBOL_SPACE)
					.append("SID")
					.append(XMLConstants.TAG_SYMBOL_EQUAL)
					.append(XMLConstants.TAG_SYMBOL_QUOTE)
					.append(replaceNullWithEmpty(localCatalogService.getServiceId() == null ? "" : 
						localCatalogService.getServiceId().toString()))
					.append(XMLConstants.TAG_SYMBOL_QUOTE);				
					
					
					
					if(localCatalogService.getBillServiceLabel() != null && !localCatalogService.getBillServiceLabel().trim().equals(""))
					{
						strBuilder.append(XMLConstants.TAG_SYMBOL_SPACE)
							.append(XMLConstants.ATTR_GENI_BILL_SERVICE_LABEL)
							.append(XMLConstants.TAG_SYMBOL_EQUAL)
							.append(XMLConstants.TAG_SYMBOL_QUOTE)
							.append(localCatalogService.getBillServiceLabel())
							.append(XMLConstants.TAG_SYMBOL_QUOTE);
					}
					strBuilder.append(XMLConstants.TAG_SYMBOL_CLOSE)
						.append(localCatalogService.getProductName())
						.append(XMLConstants.TAG_SYMBOL_OPEN)
						.append(XMLConstants.TAG_SYMBOL_SLASH)
						.append(XMLConstants.TAG_SERV)
						.append(XMLConstants.TAG_SYMBOL_CLOSE);
				}
			
				strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
					.append(XMLConstants.TAG_SYMBOL_SLASH)
					.append(XMLConstants.TAG_SERVS)
					.append(XMLConstants.TAG_SYMBOL_CLOSE);
			}
			strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
				.append(XMLConstants.TAG_SYMBOL_SLASH)
				.append(XMLConstants.TAG_CAT)
				.append(XMLConstants.TAG_SYMBOL_CLOSE);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of UpdateCatalogCommand.toXML()");
		}
		return strBuilder.toString();
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
	
	
	private void loadCatalogServiceForRetailers(ProdCatalogDetailListViewModel prodCatDetListViewModel , CommonCommandManager commonCommandManager) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of UpdateCatalogCommand.loadCatalogServiceForRetailers()");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(prodCatDetListViewModel);
		try
		{
			searchBaseWrapper = commonCommandManager.loadCatalogServiceForRetailers(searchBaseWrapper);
			catalogServiceList = searchBaseWrapper.getCustomList();
		}
		catch(FrameworkCheckedException ex)
		{
			
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of UpdateCatalogCommand.loadCatalogServiceForRetailers()");
		}
	}
	

}
