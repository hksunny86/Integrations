/**
 * 
 */
package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.vo.account.BulkCustomerAccountVo;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountFacade;
import com.inov8.ola.util.CustomerAccountTypeConstants;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 15, 2014 7:00:00 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class BulkCustomerAccountsController extends AdvanceFormController
{
	private static final Logger LOGGER = Logger.getLogger( BulkCustomerAccountsController.class );

	private ReferenceDataManager referenceDataManager;
	private MfsAccountFacade mfsAccountFacade;

	public BulkCustomerAccountsController()
	{
		setCommandName("bulkCustomerAccountVo");
		setCommandClass(BulkCustomerAccountVo.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		Map<String, Object> refDataMap = new HashMap<>(3);
		CopyOnWriteArrayList<OlaCustomerAccountTypeModel> customerAccountTypeList = null;
		List<SegmentModel> segmentModelList = null;
		OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
		olaCustomerAccountTypeModel.setIsCustomerAccountType(Boolean.TRUE);
		olaCustomerAccountTypeModel.setActive(Boolean.TRUE);
		ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl(olaCustomerAccountTypeModel, "name", SortingOrder.ASC);
		try
		{
			refDataWrapper = referenceDataManager.getReferenceData(refDataWrapper);
			List<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList = refDataWrapper.getReferenceDataList();

		    if (olaCustomerAccountTypeModelList != null)
		    {
		    	customerAccountTypeList = new CopyOnWriteArrayList<OlaCustomerAccountTypeModel>(olaCustomerAccountTypeModelList);
		    	removeSpecialAccountTypes(customerAccountTypeList);
		    }

			SegmentModel segmentModel = new SegmentModel();
			segmentModel.setIsActive(Boolean.TRUE);
			refDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
			refDataWrapper = referenceDataManager.getReferenceData(refDataWrapper);
			segmentModelList = refDataWrapper.getReferenceDataList();
		}
		catch (FrameworkCheckedException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
		refDataMap.put("olaCustomerAccountTypeModelList", customerAccountTypeList);
		refDataMap.put("segmentModelList", segmentModelList);
		return refDataMap;
	}

	@Override
	protected BulkCustomerAccountVo loadFormBackingObject(HttpServletRequest request) throws Exception
	{
		return new BulkCustomerAccountVo();
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected ModelAndView onCreate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception
	{
		BulkCustomerAccountVo bulkCustomerAccountVo = (BulkCustomerAccountVo) command;
		try
		{
			long startTime = System.currentTimeMillis();
			HttpSession session = request.getSession();
			List<BulkCustomerAccountVo> bulkCustomerAccountVoList = (List<BulkCustomerAccountVo>) session.getAttribute("bulkCustomerAccountVoList");
			session.removeAttribute("bulkCustomerAccountVoList");

			if(CollectionUtils.isNotEmpty(bulkCustomerAccountVoList))
			{
				List<BulkCustomerAccountVo> validBulkCustomerAccountVoList = bulkCustomerAccountVoList.subList(bulkCustomerAccountVo.getInvalidRecordsCount(), bulkCustomerAccountVoList.size());
				if(CollectionUtils.isNotEmpty(validBulkCustomerAccountVoList))
				{
					mfsAccountFacade.enqueueBulkCustomerAccountsCreation(validBulkCustomerAccountVoList);
				}
			}
			else
			{
				this.saveMessage(request, "No Record(s) found");
				return super.showForm(request, response, errors);	
			}

			long endTime = System.currentTimeMillis();
			logger.info("***** Bulk Upload Customers - Time to create "+bulkCustomerAccountVoList.size()+" records :"+((endTime-startTime)/1000)+" secs *****");
		}
		catch(Exception e)
		{
			logger.error(e);
			this.saveMessage(request, "Records could not be saved.");
			return super.showForm(request, response, errors);	
		}
		this.saveMessage(request, "Valid record(s) saved successfully.");
		return new ModelAndView(this.getSuccessView(), "bulkCustomerAccountVo", new BulkCustomerAccountVo());
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception
	{
		List<BulkCustomerAccountVo> bulkCustomerAccountVoList = null;
		BulkCustomerAccountVo bulkCustomerAccountVo = (BulkCustomerAccountVo) command;
		MultipartFile csvFile = bulkCustomerAccountVo.getCsvFile();
		String name = csvFile.getOriginalFilename();
		String rand = UUID.randomUUID().toString();
  	    //File f = new File("D:/BulkDisbursements/" + rand + "-" + name);
  	    File f = new File("/opt/BulkDisbursements/" + rand + "-" + name);
  	    try
  	    {
  	    	csvFile.transferTo(f);
  			String filePath = f.getAbsolutePath();
  	    	try
  	    	{
  	    		bulkCustomerAccountVoList = new BulkCustomerAccountsCsvFileParser().parseBulkCustomerAccountsCsvFile(filePath, bulkCustomerAccountVo);
  	    		bulkCustomerAccountVoList = mfsAccountFacade.isCustomerOrAgent(bulkCustomerAccountVoList);
				f.delete(); // delete temporary file...
			}
  	    	catch(Exception ex)
  	    	{
				ex.printStackTrace();
			}
  	    }
  	    catch (Exception ex)
  	    {
  	    	LOGGER.error(ex.getMessage(), ex);
			this.saveMessage(request, "Some error occurred while processing the CSV file");
			return super.showForm(request, response, errors);	
		}
  	    Collections.sort(bulkCustomerAccountVoList, new BulkCustomerAccountsInvalidRecordsComparator());
  	    boolean allRecordsInvalid = false;
  	    int invalidRecordsCount = countInvalidRecords(bulkCustomerAccountVoList);
  	    if(invalidRecordsCount > 0)
  	    {
    		allRecordsInvalid = true;
  	    	//this.saveMessage(request, "File contains one or more invalid records. Kindly fix and upload again.");
		}

  	    request.getSession().setAttribute("bulkCustomerAccountVoList", bulkCustomerAccountVoList);
  	    Map<String, Object> modelMap = new HashMap<>(3);
  	    modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_RETRIEVE);
  	    modelMap.put("allRecordsInvalid", allRecordsInvalid);
        modelMap.put("invalidRecordsCount", invalidRecordsCount);
		return new ModelAndView("redirect:p_bulkcustomerspreview.html", modelMap);
	}

	private int countInvalidRecords(List<BulkCustomerAccountVo> bulkCustomerAccountVoList)
	{
		int invalidRecords = 0;
		for(BulkCustomerAccountVo bulkCustomerAccountVo : bulkCustomerAccountVoList)
		{
			if(!bulkCustomerAccountVo.getValidRecord())
			{
				invalidRecords++;
			}
			else
			{
				break;
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

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setMfsAccountFacade(MfsAccountFacade mfsAccountFacade)
	{
		this.mfsAccountFacade = mfsAccountFacade;
	}

}
