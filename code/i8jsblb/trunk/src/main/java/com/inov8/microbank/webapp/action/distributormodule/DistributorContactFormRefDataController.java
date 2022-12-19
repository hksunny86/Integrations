package com.inov8.microbank.webapp.action.distributormodule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.distributormodule.DistributorLevelListViewModel;
import com.inov8.microbank.server.dao.distributormodule.DistributorContactDAO;
import com.inov8.microbank.server.service.distributormodule.DistributorLevelManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class DistributorContactFormRefDataController extends AjaxController
{

	private ReferenceDataManager	referenceDataManager;
	private DistributorLevelManager distributorLevelManager;
	private DistributorContactDAO distributorContactDAO;
	
	public void setDistributorLevelManager(
			DistributorLevelManager distributorLevelManager) {
		this.distributorLevelManager = distributorLevelManager;
	}

	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String actionType = ServletRequestUtils.getRequiredStringParameter(request, "actionType");

		try
		{
			if (actionType.equalsIgnoreCase("1"))
			{
				DistributorModel distributorModel = new DistributorModel();
				List<DistributorModel> distributorModelList = null;

				distributorModel.setAreaId(ServletRequestUtils.getRequiredLongParameter(request, "areaId"));

				distributorModel.setActive(true);
				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name",
						SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);

				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					distributorModelList = referenceDataWrapper.getReferenceDataList();
					//removeNationalDistributor(distributorModelList);
				}
				AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
				if (distributorModelList.size() == 0)
					ajaxXmlBuilder.addItemAsCData(" ", "");
				return ajaxXmlBuilder.addItems(distributorModelList, "name", "distributorId").toString();

			}
			else if (actionType.equalsIgnoreCase("2"))
			{
				DistributorLevelModel distributorLevelModel = new DistributorLevelModel();
				List<DistributorLevelModel> distributorLevelModelList = null;
				// commented by Rashid Starts
				/*distributorLevelModel.setDistributorId(ServletRequestUtils.getRequiredLongParameter(request,"distributorId"));*/
				// commented by Rashid Ends
				
				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(distributorLevelModel, "name",
						SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);

				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					distributorLevelModelList = referenceDataWrapper.getReferenceDataList();
					
				}
				AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();

				if (distributorLevelModelList.size() == 0)
					ajaxXmlBuilder.addItemAsCData(" ", "");
				
				//checkHeadDistributor (distributorLevelModel.getDistributorId());
				return ajaxXmlBuilder.addItems(distributorLevelModelList, "name", "distributorLevelId").toString();
			}

			else if (actionType.equalsIgnoreCase("4"))
			{
				DistributorLevelListViewModel distributorLevelModel = new DistributorLevelListViewModel();
				List<DistributorLevelListViewModel> distributorLevelModelList = null;
				AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
				distributorLevelModel.setDistributorId(ServletRequestUtils.getRequiredLongParameter(request,
						"distributorId"));
				//System.out.println( "**************" + ServletRequestUtils.getRequiredLongParameter(request, "distributorLevelId") );
				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(distributorLevelModel, "name",
						SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);

				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					distributorLevelModelList = referenceDataWrapper.getReferenceDataList();
				}

				if (distributorLevelModelList != null && distributorLevelModelList.size() > 0)
				{
					distributorLevelModel = distributorLevelModelList.get(0);
					if (distributorLevelModel.getUltimateManagingLevelId() != null)
					{
						return ajaxXmlBuilder.addItem("ultimateManagingLevelId",
								distributorLevelModel.getUltimateManagingLevelId().toString()).addItem(
								"ultamateLevelName", distributorLevelModel.getUltimateLevelName()).toString();

					}

					else
					{

						return ajaxXmlBuilder.addItemAsCData("ultimateManagingLevelId",
								distributorLevelModel.getDistributorLevelId().toString()).addItemAsCData(
								"ultamateLevelName", distributorLevelModel.getDistributorLevelName()).toString();
					}

				}

				else
				{

					return ajaxXmlBuilder.addItemAsCData("ultimateManagingLevelId", "").addItemAsCData(
							"ultamateLevelName", "").toString();
				}

				/*AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
				 
				 if( distributorLevelModelList.size() == 0 )
				 ajaxXmlBuilder.addItemAsCData(" "," ");
				 
				 return ajaxXmlBuilder.addItemAsCData("ultimateLevelName",  distributorLevelModel.getUltimateLevelName()).toString();*/

				//return ajaxXmlBuilder.addItems(distributorLevelModelList, "ultimateLevelName", "ultimateManagingLevelId").toString();
			}

			else if (actionType.equalsIgnoreCase("6"))
			{

				DistributorLevelModel distributorLevelModel = new DistributorLevelModel();
				List<DistributorLevelModel> distributorLevelModelList = null;

				// commented by Rashid Starts
				/*distributorLevelModel.setDistributorId(ServletRequestUtils.getRequiredLongParameter(request,"distributorId"));*/
				// commented by Rashid Ends
				
				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(distributorLevelModel, "name",
						SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);

				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					distributorLevelModelList = referenceDataWrapper.getReferenceDataList();
				}
				AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();

				if (distributorLevelModelList.size() == 0)
				{
					ajaxXmlBuilder.addItemAsCData("", "");
				}
				else
					ajaxXmlBuilder.addItemAsCData("", "");
				
				return ajaxXmlBuilder.addItems(distributorLevelModelList, "name", "distributorLevelId").toString();
			}

			else if (actionType.equalsIgnoreCase("3"))
			{
				DistributorContactModel distributorContactModel = new DistributorContactModel();
				List<DistributorContactModel> distributorModelList = null;

				distributorContactModel.setDistributorId(ServletRequestUtils.getRequiredLongParameter(request,
						"distributorId"));
				System.out.println("**************"
						+ ServletRequestUtils.getRequiredLongParameter(request, "distributorId"));

				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(distributorContactModel,
						"name", SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);

				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					distributorModelList = referenceDataWrapper.getReferenceDataList();
				}
				AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
				if (distributorModelList.size() == 0)
					ajaxXmlBuilder.addItemAsCData("", "");
				return ajaxXmlBuilder.addItems(distributorModelList, "name", "distributorId").toString();
			}

			else if (actionType.equalsIgnoreCase("5"))
			{
				String isHead = ServletRequestUtils.getRequiredStringParameter(request, "isHead");
				if (isHead.equalsIgnoreCase("true"))
				{
					//DistributorLevelListViewModel distributorLevelModel = new DistributorLevelListViewModel();
					List<DistributorLevelModel> distributorLevelModelList = null;

					//distributorLevelModel.setDistributorId(ServletRequestUtils.getRequiredLongParameter(request,
					//		"distributorId"));
					//distributorLevelModel.setUltimateManagingLevelId(null);
					//distributorLevelModel.setManagingLevelId(null);
					//System.out.println( "**************" + ServletRequestUtils.getRequiredLongParameter(request, "distributorLevelId") );
					//ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(distributorLevelModel,
					//		"distributorLevelName", SortingOrder.ASC);
					//referenceDataManager.getReferenceData(referenceDataWrapper);

					/*if (referenceDataWrapper.getReferenceDataList() != null)
					{
						distributorLevelModelList = referenceDataWrapper.getReferenceDataList();
					}*/
					Long distributorId =ServletRequestUtils.getRequiredLongParameter(request,"distributorId");
					String distributorLevelHQL = " select distributorLevelId,distributorLevelName from DistributorLevelListViewModel dl "
						+ "where dl.distributorId = ? "
						+ " and dl.managingLevelId is null and dl.ultimateManagingLevelId is null " ;
					distributorLevelModelList=distributorLevelManager.searchDistributorLevelModels(distributorId,distributorLevelHQL);

					AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();

					if (distributorLevelModelList.size() == 0)
						ajaxXmlBuilder.addItemAsCData(" ", "");

					isHead = "true";
					ajaxXmlBuilder.addItemAsCData("head ", isHead);
					return ajaxXmlBuilder.addItems(distributorLevelModelList, "distributorLevelName", "distributorLevelId").toString();

				}
				else
				{

					DistributorLevelModel distributorLevelModel = new DistributorLevelModel();
					List<DistributorLevelModel> distributorLevelModelList = null;

					// commented by Rashid Starts
					/*distributorLevelModel.setDistributorId(ServletRequestUtils.getRequiredLongParameter(request,"distributorId"));*/
					// commented by Rashid Ends
					
					ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(distributorLevelModel,
							"name", SortingOrder.ASC);
					referenceDataManager.getReferenceData(referenceDataWrapper);

					if (referenceDataWrapper.getReferenceDataList() != null)
					{
						distributorLevelModelList = referenceDataWrapper.getReferenceDataList();
					}
					AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();

					if (distributorLevelModelList.size() == 0)
						ajaxXmlBuilder.addItemAsCData(" ", "");

					isHead = "false";
					ajaxXmlBuilder.addItemAsCData("head ", isHead);
					return ajaxXmlBuilder.addItems(distributorLevelModelList, "name", "distributorLevelId").toString();

				}

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
			if (actionType.equalsIgnoreCase("4"))
			{
				return ajaxXmlBuilder.addItemAsCData("ultimateManagingLevelId", "").addItemAsCData(
						"ultamateLevelName", " ").toString();

			}

			/*if (actionType.equalsIgnoreCase("6"))
			 {
			 return ajaxXmlBuilder
			 .addItemAsCData("ultimateManagingLevelId", "")
			 .addItemAsCData("ultamateLevelName", "").toString();
			 
			 }*/
			else
			{
				String result = ajaxXmlBuilder.addItemAsCData("", "").toString();
				return result;

			}
		}

		String result = new AjaxXmlBuilder().addItemAsCData("", "").toString();
		return result;
	}

	private Boolean checkHeadDistributor(Long distributorId) {
		DistributorContactModel dcm = new DistributorContactModel ();
		dcm.setHead(true);
		dcm.setDistributorId(distributorId);
		CustomList cl = this.distributorContactDAO.findByExample(dcm);
		if (cl.getResultsetList().size() !=	0){
			return true;
		}else{
			return false;
		}
		
	}
//	  private void removeNationalDistributor(List<DistributorModel> distributorModelList) {
//			for (Iterator iter = distributorModelList.iterator(); iter.hasNext();) {
//				DistributorModel distributor =(DistributorModel)  iter.next();
//				if (true == distributor.getNational()){
//					iter.remove();
//				}
//				
//			}
//			
//		}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public DistributorContactDAO getDistributorContactDAO() {
		return distributorContactDAO;
	}

	public void setDistributorContactDAO(DistributorContactDAO distributorContactDAO) {
		this.distributorContactDAO = distributorContactDAO;
	}

}
