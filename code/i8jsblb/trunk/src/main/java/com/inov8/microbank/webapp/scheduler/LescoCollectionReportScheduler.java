package com.inov8.microbank.webapp.scheduler;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.LescoCollectionModel;
import com.inov8.microbank.common.model.productmodule.paymentservice.LescoLogModel;
import com.inov8.microbank.common.util.LescoConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.service.lescomodule.LescoCollectionManager;

public class LescoCollectionReportScheduler extends QuartzJobBean
{
	private LescoCollectionManager lescoCollectionManager;

	protected final Log logger = LogFactory.getLog(LescoCollectionReportScheduler.class); 
	
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("START OF LescoCollectionReportScheduler.executeInternal()....");
		}
		Date nowDate = new Date();
		System.out.println("Date Is : "+nowDate.toString());
		Date paidDate = PortalDateUtils.subtractDays(nowDate, LescoConstants.DAYS_TO_SUBTRACT);
		System.out.println("Paid Date Is : "+paidDate.toString());
		
		if(lescoCollectionManager != null)
		{
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			BaseWrapper baseWrapper = new BaseWrapperImpl();
		
			DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("paidDate", paidDate,paidDate);
			
			LescoLogModel lescoLogModel = new LescoLogModel();
			searchBaseWrapper.setBasePersistableModel(lescoLogModel);
			searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
			
			LescoCollectionModel lescoCollectionModel = new LescoCollectionModel();
						
			String lescoReport = null;
			try
			{
				if(logger.isDebugEnabled())
				{
					logger.debug("GOING TO SEARCH RECORD FROM DB TABLE [LESCO_LOG]........");
				}
				
				searchBaseWrapper = this.lescoCollectionManager.searchLescoLog(searchBaseWrapper);
				CustomList<LescoLogModel> customList = 	searchBaseWrapper.getCustomList();
				if(null != customList)
				{
					List<LescoLogModel> list = customList.getResultsetList();
					
					if(null != list && list.size() > 0)
					{
						if(logger.isDebugEnabled())
						{
							logger.debug("GOING TO MAKE LESCO REPORT..............");
						}
						lescoReport = makeLescoBillReport(list,paidDate);
						
						if(logger.isDebugEnabled())
						{
							logger.debug("LESCO REPORT GENERATED SUCCESSFULLY");
						}
						Date latestCurrentDate = new Date();
						lescoCollectionModel.setCreatedBy(1L);
						System.out.println("Latest Date : "+latestCurrentDate);
						lescoCollectionModel.setCreatedOn(latestCurrentDate);
						lescoCollectionModel.setDescription(LescoConstants.LESCO_REPORT_DESCRIPTION);
						lescoCollectionModel.setFileData(lescoReport);
						lescoCollectionModel.setFileName(LescoConstants.FILE_NAME+PortalDateUtils.formatDate(paidDate, LescoConstants.FILE_NAME_DATE_FORMAT));
						lescoCollectionModel.setUpdatedBy(1L);
						System.out.println("Latest Date : "+latestCurrentDate);
						lescoCollectionModel.setUpdatedOn(latestCurrentDate);
						
						//Going to Add Record in LESCO_COLLECTION........
						
						if(logger.isDebugEnabled())
						{
							logger.debug("GOING TO ADD RECORD IN LESCO_COLLECTION TABLE......");
						}
						baseWrapper.setBasePersistableModel(lescoCollectionModel);
						baseWrapper = this.lescoCollectionManager.saveOrUpdateLescoCollection(baseWrapper);
						if(logger.isDebugEnabled())
						{
							logger.debug("Record Saved Successfully in LESCO_COLLECTION TABLE....");
						}
					}
					else
					{
						if(logger.isDebugEnabled())
						{
							logger.debug("No Record Found From DB. List is Empty...");
						}
					}
				}
				else
				{
					if(logger.isDebugEnabled())
					{
						logger.debug("No Record Found From DB. CustomList Is Empty...");
					}
				}
			}
			catch(FrameworkCheckedException fwce)
			{
				if(logger.isErrorEnabled())
				{
					logger.error(fwce);
				}
			}
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of LescoCollectionReportScheduler.executeInternal()");
		}
	}

	
	
	private String makeLescoBillReport(List<LescoLogModel> list, Date paidDate)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of LescoCollectionReportScheduler.makeLescoBillReport()");
		}
		
		LescoLogModel lescoLogModel = null;
		StringBuilder stringBuilder = new StringBuilder();
	
		stringBuilder.append(LescoConstants.BANK_CODE);
		stringBuilder.append(LescoConstants.DOUBLE_SPACE);
		stringBuilder.append(PortalDateUtils.formatDate(paidDate, LescoConstants.DUE_PAID_DATE_FORMAT));
		stringBuilder.append(LescoConstants.DOUBLE_SPACE);
		stringBuilder.append(LescoConstants.BANK_SUB_DIV);
		stringBuilder.append(LescoConstants.DOUBLE_SPACE);
		stringBuilder.append(StringUtil.leftPadWithZero(String.valueOf(list.size()), LescoConstants.TOTAL_NUMBER_OF_BILL_LENGTH));
				
		if(list != null && list.size() > 0)
		{
			for(int i = 0; i < list.size(); i++)
			{
				stringBuilder.append(LescoConstants.NEW_LINE_CHARACTER);
				lescoLogModel = list.get(i);
				
				stringBuilder.append(LescoConstants.CARD_TYPE);
				stringBuilder.append(LescoConstants.SINGLE_SPACE);
				stringBuilder.append(lescoLogModel.getConsumerNo());				
				stringBuilder.append(LescoConstants.SINGLE_SPACE);
				stringBuilder.append(StringUtil.leftPadWithZero(String.valueOf(calculateScrollNumber(i+1)), LescoConstants.SCROLL_NUMBER_LENGTH));
				stringBuilder.append(LescoConstants.SINGLE_SPACE);
				stringBuilder.append(StringUtil.leftPadWithZero(String.valueOf(i+1), LescoConstants.BILL_NUMBER_LENGTH));
				stringBuilder.append(LescoConstants.SINGLE_SPACE);
				stringBuilder.append(LescoConstants.MAX_BILL_NUMBER);
				stringBuilder.append(LescoConstants.SINGLE_SPACE);
				stringBuilder.append(PortalDateUtils.formatDate(lescoLogModel.getDueDate(), LescoConstants.DUE_PAID_DATE_FORMAT).toString());
				stringBuilder.append(LescoConstants.SINGLE_SPACE);
				stringBuilder.append(PortalDateUtils.formatDate(lescoLogModel.getPaidDate(), LescoConstants.DUE_PAID_DATE_FORMAT).toString());
				stringBuilder.append(LescoConstants.SINGLE_SPACE);
				stringBuilder.append(StringUtil.leftPadWithZero(String.valueOf(lescoLogModel.getBillAmount()), LescoConstants.AMOUNT_DUE_LENGTH));
				stringBuilder.append(LescoConstants.SINGLE_SPACE);
				stringBuilder.append(StringUtil.leftPadWithZero(String.valueOf(lescoLogModel.getPaidAmount()), LescoConstants.AMOUNT_PAID_LENGTH));
			}
		}
		
		stringBuilder.append(LescoConstants.NEW_LINE_CHARACTER);	
		
		stringBuilder.append(StringUtil.leftPadWithZero(String.valueOf(list.size()), LescoConstants.TOTAL_NUMBER_OF_BILL_LENGTH));
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of LescoCollectionReportScheduler.makeLescoBillReport()");
		}
		return stringBuilder.toString();
	}


	private int calculateScrollNumber(int billNumber)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of LescoCollectionReportScheduler.calculateScrollNumber()");
		}
		int scrollNumber = 0;
		if(billNumber <= LescoConstants.REPORT_PAGE_SIZE)
		{
			scrollNumber = 1;
		}
		else
		{
			if(billNumber % LescoConstants.REPORT_PAGE_SIZE == 0)
			{
				scrollNumber = billNumber/LescoConstants.REPORT_PAGE_SIZE;
			}
			else if(billNumber % LescoConstants.REPORT_PAGE_SIZE > 0)
			{
				scrollNumber = billNumber/LescoConstants.REPORT_PAGE_SIZE + 1;
			}
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of LescoCollectionReportScheduler.calculateScrollNumber()");
		}
		return scrollNumber;
	}
	
	public void setLescoCollectionManager(LescoCollectionManager lescoCollectionManager)
	{
		this.lescoCollectionManager = lescoCollectionManager;
	}
}
