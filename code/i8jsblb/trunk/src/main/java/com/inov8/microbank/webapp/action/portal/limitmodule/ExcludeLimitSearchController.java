package com.inov8.microbank.webapp.action.portal.limitmodule;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.ExtendedLimitRuleModel;
import com.inov8.microbank.common.model.LimitRuleModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionAllViewModel;
import com.inov8.ola.server.facade.LimitFacade;

public class ExcludeLimitSearchController extends BaseFormSearchController
{
	private static final Logger LOGGER = Logger.getLogger( ExcludeLimitFormController.class );
	private LimitFacade limitFacade;
	private ReferenceDataManager referenceDataManager;

	public ExcludeLimitSearchController()
	{
		 super.setCommandName("extendedLimitRuleModel");
		 super.setCommandClass(ExtendedLimitRuleModel.class);
	}

	@Override
	protected Map<String, List<?>> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		 Map referenceDataMap = new HashMap();
		 ReferenceDataWrapper referenceDataWrapper;
		 
		 List<ProductModel> productModelList = null;
		 ProductModel productModel = new ProductModel();
		 productModel.setActive(true);
		 
		 List<SegmentModel> segmentModelLsit = null;
		 SegmentModel segmentModel = new SegmentModel();
		 segmentModel.setIsActive(true);
		 
		 List<OlaCustomerAccountTypeModel> accountTypeModelList = null;
		 OlaCustomerAccountTypeModel alaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
		 alaCustomerAccountTypeModel.setActive(true);
		
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
		 referenceDataWrapper = new ReferenceDataWrapperImpl(alaCustomerAccountTypeModel, "name", SortingOrder.DESC);
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
			 accountTypeModelList = referenceDataWrapper.getReferenceDataList();
		  }
		 
		 referenceDataMap.put("accountTypeList", accountTypeModelList);
		 
		 return referenceDataMap;
	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			Object model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		ExtendedLimitRuleModel extendedLimitRuleModel = (ExtendedLimitRuleModel) model;

		searchBaseWrapper.setBasePersistableModel( (LimitRuleModel) extendedLimitRuleModel );

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", extendedLimitRuleModel.getStartDate(),
				extendedLimitRuleModel.getEndDate());

		searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

		//sorting order
		if( sortingOrderMap.isEmpty() )
		{
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

		CustomList<LimitRuleModel> list = this.limitFacade.searchLimitRule(searchBaseWrapper).getCustomList();

		return new ModelAndView( getSuccessView(), "limitRuleModelList", list.getResultsetList());
	}


	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setLimitFacade(LimitFacade limitFacade) {
		this.limitFacade = limitFacade;
	}


}

