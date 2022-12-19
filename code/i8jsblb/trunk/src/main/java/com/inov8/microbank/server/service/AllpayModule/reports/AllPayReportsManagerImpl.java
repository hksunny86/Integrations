package com.inov8.microbank.server.service.AllpayModule.reports;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.des.DESEncryption;
import com.inov8.microbank.common.model.DistHeadReportListViewModel;
import com.inov8.microbank.common.model.SwitchModel;
import com.inov8.microbank.common.model.allpaymodule.AllpayRetTransViewModel;
import com.inov8.microbank.common.model.allpaymodule.DistHeadSummaryViewModel;
import com.inov8.microbank.common.model.allpaymodule.RegionalHeadSummaryViewModel;
import com.inov8.microbank.common.model.allpaymodule.RetailerBillSummaryViewModel;
import com.inov8.microbank.common.model.allpaymodule.RetailerHeadTransViewModel;
import com.inov8.microbank.common.model.allpaymodule.RetailerSummaryViewModel;
import com.inov8.microbank.common.util.SwitchConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.dao.allpaymodule.AllpayRetTransViewDAO;
import com.inov8.microbank.server.dao.allpaymodule.DistHeadReportListViewDAO;
import com.inov8.microbank.server.dao.allpaymodule.DistHeadSummaryViewDAO;
import com.inov8.microbank.server.dao.allpaymodule.RegionalHeadSummaryViewDAO;
import com.inov8.microbank.server.dao.allpaymodule.RetailerBillSummaryViewDAO;
import com.inov8.microbank.server.dao.allpaymodule.RetailerHeadTransViewDAO;
import com.inov8.microbank.server.dao.allpaymodule.RetailerSummaryViewDAO;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchModuleManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ola.OLASwitchImpl;
import com.inov8.ola.integration.vo.OLAVO;
import com.thoughtworks.xstream.XStream;

public class AllPayReportsManagerImpl implements AllPayReportsManager, ApplicationContextAware
{

	private AllpayRetTransViewDAO allpayRetTransViewDAO;
	private RetailerBillSummaryViewDAO retailerBillSummaryViewDAO;
	private DistHeadReportListViewDAO distHeadReportListViewDAO;
	private RetailerHeadTransViewDAO retailerHeadTransViewDAO;
	private ApplicationContext ctx;
	private FailureLogManager auditLogModule;
	private XStream xstream;
	private SwitchModuleManager switchManager;

	private RetailerSummaryViewDAO retailerSummaryViewDAO;
	private RegionalHeadSummaryViewDAO regionalHeadSummaryViewDAO;
	private DistHeadSummaryViewDAO distHeadSummaryViewDAO;

	public void setSwitchManager(SwitchModuleManager switchManager)
	{
		this.switchManager = switchManager;
	}

	public SearchBaseWrapper searchRetailerTransactions(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{

		CustomList<AllpayRetTransViewModel> list = this.allpayRetTransViewDAO.findByExample((AllpayRetTransViewModel) searchBaseWrapper.getBasePersistableModel(),
				searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;

	}

	public AllpayRetTransViewDAO getAllpayRetTransViewDAO()
	{
		return allpayRetTransViewDAO;
	}

	public void setAllpayRetTransViewDAO(AllpayRetTransViewDAO allpayRetTransViewDAO)
	{
		this.allpayRetTransViewDAO = allpayRetTransViewDAO;
	}

	public SearchBaseWrapper searchRetailerBillSummaries(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		CustomList<RetailerBillSummaryViewModel> list = this.retailerBillSummaryViewDAO.findByExample((RetailerBillSummaryViewModel) searchBaseWrapper
				.getBasePersistableModel(), searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);

		list.setResultsetList(this.decryptAccountsForBillSummary(list.getResultsetList()));

		try
		{
			getOLAAccountsForRetailerBillSummary(searchBaseWrapper, list.getResultsetList());
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
		}

		return searchBaseWrapper;
	}

	public SearchBaseWrapper searchRetailerHeadTransactions(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{

		CustomList<RetailerHeadTransViewModel> list = this.retailerHeadTransViewDAO.findByExample((RetailerHeadTransViewModel) searchBaseWrapper
				.getBasePersistableModel(), searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		
		list.setResultsetList(this.decryptAccountsForRetTrans(list.getResultsetList()));

		try
		{
			getOLAAccountsForRetailerTrans(searchBaseWrapper, list.getResultsetList());
		}
		catch (RuntimeException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return searchBaseWrapper;

	}

	public SearchBaseWrapper searchDistributorHeadTransactions(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{

		CustomList<DistHeadReportListViewModel> list = this.distHeadReportListViewDAO
				.findByExample((DistHeadReportListViewModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper
						.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel());

		//		List<DistHeadReportListViewModel> list = this.distHeadReportListViewDAO.getReport(searchBaseWrapper);
		searchBaseWrapper.setCustomList(list);

		list.setResultsetList(this.decryptAccounts(list.getResultsetList()));

		try
		{
			getOLAAccounts(searchBaseWrapper, list.getResultsetList());
		}
		catch (RuntimeException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("");

		return searchBaseWrapper;
	}

	public void getOLAAccounts(SearchBaseWrapper searchBaseWrapper, List<DistHeadReportListViewModel> list) throws FrameworkCheckedException
	{
		OLASwitchImpl ola = new OLASwitchImpl(this.ctx);
		ola.setAuditLogModuleFacade(this.auditLogModule);
		ola.setXstream(this.xstream);

		try
		{
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dateStr = null;

			if (((DistHeadReportListViewModel) searchBaseWrapper.getBasePersistableModel()).getCreatedOn() != null)
				dateStr = format.parse(((DistHeadReportListViewModel) searchBaseWrapper.getBasePersistableModel()).getCreatedOn());
			else
				dateStr = new Date();

			DecimalFormat decimalFormater = new DecimalFormat("###,###.00");

			OLAVO olaVO = new OLAVO();
			olaVO.setStatsDate(dateStr);

			ThreadLocalActionLog.setActionLogId(128447l);

			SwitchWrapper sw = new SwitchWrapperImpl();
			sw.setSwitchSwitchModel(new SwitchModel());
			sw.getSwitchSwitchModel().setUrl( this.getOLAURL() );
//			sw.getSwitchSwitchModel().setUrl("http://127.0.0.1:8080/olaintegration/ws/olaSwitch");
			sw.setOlavo(olaVO);

			sw = ola.getAllAccountsWithStats(sw);

			HashMap<String, OLAVO> olaAccounts = sw.getOlaAccountsWithStatsHashMap();

			for (DistHeadReportListViewModel distHead : list)
			{
				OLAVO olaVOTemp = olaAccounts.get(distHead.getDistAccountNo());
				if (olaVOTemp != null)
				{
					System.out.println(olaVOTemp.getStartDayBalance());

					distHead.setStartDayBalance(decimalFormater.format(Double.parseDouble(olaVOTemp.getStartDayBalance())));					
					distHead.setBalance(decimalFormater.format(olaVOTemp.getBalance()));
				}

				OLAVO olaVORet = olaAccounts.get(distHead.getRetailerAccountNo());
				if (olaVORet != null)
				{
					distHead.setEndDayBalance(olaVORet.getEndDayBalance());
					distHead.setStartDayBalanceRet(olaVORet.getStartDayBalance());
					
				}

			}

		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}

	public void getOLAAccountsForRetailerBillSummary(SearchBaseWrapper searchBaseWrapper, List<RetailerBillSummaryViewModel> list) throws FrameworkCheckedException
	{
		OLASwitchImpl ola = new OLASwitchImpl(this.ctx);
		ola.setAuditLogModuleFacade(this.auditLogModule);
		ola.setXstream(this.xstream);

//		try
		{
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dateStr = null;

			if (((RetailerBillSummaryViewModel) searchBaseWrapper.getBasePersistableModel()).getTxDate() != null)
				dateStr = (((RetailerBillSummaryViewModel) searchBaseWrapper.getBasePersistableModel()).getTxDate());
			else
				dateStr = new Date();

			DecimalFormat decimalFormater = new DecimalFormat("###,###.00");

			OLAVO olaVO = new OLAVO();
			olaVO.setStatsDate(dateStr);

			ThreadLocalActionLog.setActionLogId(128447l);

			SwitchWrapper sw = new SwitchWrapperImpl();
			sw.setSwitchSwitchModel(new SwitchModel());
			sw.getSwitchSwitchModel().setUrl( this.getOLAURL() );
			sw.setOlavo(olaVO);

			sw = ola.getAllAccountsWithStats(sw);

			HashMap<String, OLAVO> olaAccounts = sw.getOlaAccountsWithStatsHashMap();

			for (RetailerBillSummaryViewModel distHead : list)
			{
				OLAVO olaVOTemp = olaAccounts.get(distHead.getRetAccountNo());
				if (olaVOTemp != null)
				{
					//					 System.out.println( olaVOTemp.getStartDayBalance() );

					distHead.setStartDayBalance(decimalFormater.format(Double.parseDouble(olaVOTemp.getStartDayBalance())));
					if( olaVOTemp.getEndDayBalance() != null )
						distHead.setEndDayBalance(decimalFormater.format(Double.parseDouble(olaVOTemp.getEndDayBalance())));
					distHead.setBalance(decimalFormater.format(olaVOTemp.getBalance()));
				}

				//				 OLAVO olaVORet = olaAccounts.get( distHead.getRetailerAccountNo() ) ;				 
				//				 if( olaVORet != null )
				//				 {
				//					 distHead.setEndDayBalance(olaVORet.getEndDayBalance()) ;					 
				//				 }

			}

		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
	}
	
	public void getOLAAccountsForRetailerTrans(SearchBaseWrapper searchBaseWrapper, List<RetailerHeadTransViewModel> list) throws FrameworkCheckedException
	{
		OLASwitchImpl ola = new OLASwitchImpl(this.ctx);
		ola.setAuditLogModuleFacade(this.auditLogModule);
		ola.setXstream(this.xstream);

//		try
		{
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dateStr = null;

			if (((RetailerHeadTransViewModel) searchBaseWrapper.getBasePersistableModel()).getTxDate() != null)
				dateStr = (((RetailerHeadTransViewModel) searchBaseWrapper.getBasePersistableModel()).getTxDate());
			else
				dateStr = new Date();

			DecimalFormat decimalFormater = new DecimalFormat("###,###.00");

			OLAVO olaVO = new OLAVO();
			olaVO.setStatsDate(dateStr);

			ThreadLocalActionLog.setActionLogId(128447l);

			SwitchWrapper sw = new SwitchWrapperImpl();
			sw.setSwitchSwitchModel(new SwitchModel());
			sw.getSwitchSwitchModel().setUrl( this.getOLAURL() );
			sw.setOlavo(olaVO);

			sw = ola.getAllAccountsWithStats(sw);

			HashMap<String, OLAVO> olaAccounts = sw.getOlaAccountsWithStatsHashMap();

			for (RetailerHeadTransViewModel distHead : list)
			{
				OLAVO olaVOTemp = olaAccounts.get(distHead.getHeadAccountNo());
				if (olaVOTemp != null)
				{
					//					 System.out.println( olaVOTemp.getStartDayBalance() );

					distHead.setStartDayBalance(decimalFormater.format(Double.parseDouble(olaVOTemp.getStartDayBalance())));
					distHead.setBalance(decimalFormater.format(olaVOTemp.getBalance()));
				}

				 OLAVO olaVORet = olaAccounts.get( distHead.getRetAccountNo() ) ;				 
				 if( olaVORet != null )
				 {
					 distHead.setRetStartDayBalance(decimalFormater.format(Double.parseDouble(olaVORet.getStartDayBalance())));
					 distHead.setRetBalance(decimalFormater.format(olaVORet.getBalance()));
					 if( olaVORet.getEndDayBalance() != null )
						 distHead.setRetEndDayBalance(decimalFormater.format(Double.parseDouble(olaVORet.getEndDayBalance()))) ;
				 }

			}

		}
//		catch (ParseException e)
//		{
//			e.printStackTrace();
//		}
	}
	
	private String getOLAURL()
	{
		try
		{
			SwitchModel switchModel = new SwitchModel();
			
			switchModel.setPrimaryKey(SwitchConstants.OLA_SWITCH) ;
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(switchModel);
			
			switchModel = (SwitchModel)this.switchManager.loadSwitch( baseWrapper ).getBasePersistableModel() ;
			
			if( switchModel != null )
				return switchModel.getUrl() ;
			else
				return "";
		}
		catch (FrameworkCheckedException e)
		{
			e.printStackTrace();
		}
		return "";
	}


	public List<DistHeadReportListViewModel> decryptAccounts(List<DistHeadReportListViewModel> dataList)
	{
		DESEncryption des = DESEncryption.getInstance();

		for (DistHeadReportListViewModel distHead : dataList)
		{
			try
			{
				distHead.setDistAccountNo(des.decrypt(distHead.getDistAccountNo()));
				distHead.setRetailerAccountNo(des.decrypt(distHead.getRetailerAccountNo()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return dataList;
	}

	public List<RetailerBillSummaryViewModel> decryptAccountsForBillSummary(List<RetailerBillSummaryViewModel> dataList)
	{
		DESEncryption des = DESEncryption.getInstance();

		for (RetailerBillSummaryViewModel distHead : dataList)
		{
			try
			{
				distHead.setRetAccountNo(des.decrypt(distHead.getRetAccountNo()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return dataList;
	}
	
	public List<RetailerHeadTransViewModel> decryptAccountsForRetTrans(List<RetailerHeadTransViewModel> dataList)
	{
		DESEncryption des = DESEncryption.getInstance();

		for (RetailerHeadTransViewModel distHead : dataList)
		{
			try
			{
				distHead.setRetAccountNo(des.decrypt(distHead.getRetAccountNo()));
				distHead.setHeadAccountNo(des.decrypt(distHead.getHeadAccountNo()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return dataList;
	}

	public RetailerBillSummaryViewDAO getRetailerBillSummaryViewDAO()
	{
		return retailerBillSummaryViewDAO;
	}

	public void setRetailerBillSummaryViewDAO(RetailerBillSummaryViewDAO retailerBillSummaryViewDAO)
	{
		this.retailerBillSummaryViewDAO = retailerBillSummaryViewDAO;
	}

	public DistHeadReportListViewDAO getDistHeadReportListViewDAO()
	{
		return distHeadReportListViewDAO;
	}

	public void setDistHeadReportListViewDAO(DistHeadReportListViewDAO distHeadReportListViewDAO)
	{
		this.distHeadReportListViewDAO = distHeadReportListViewDAO;
	}

	public OLAVO getAllPayBalanceStats(List<DistHeadReportListViewModel> contactsList)
	{

		return null;
	}

	public RetailerHeadTransViewDAO getRetailerHeadTransViewDAO()
	{
		return retailerHeadTransViewDAO;
	}

	public void setRetailerHeadTransViewDAO(RetailerHeadTransViewDAO retailerHeadTransViewDAO)
	{
		this.retailerHeadTransViewDAO = retailerHeadTransViewDAO;
	}
	
	// For Report of ...... Master Id
	public SearchBaseWrapper searchDistHeadSummary(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
//		CustomList<DistHeadSummaryViewModel> list = this.distHeadSummaryViewDAO.findByExample((DistHeadSummaryViewModel) searchBaseWrapper.getBasePersistableModel(),
//				searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());

		CustomList<DistHeadSummaryViewModel> list = null ;
//		
//		
//
//		
		searchBaseWrapper = this.distHeadSummaryViewDAO.getDistHeadSummary(searchBaseWrapper);
		
		// List<DistHeadReportListViewModel> list =
		// this.distHeadReportListViewDAO.getReport(searchBaseWrapper);
//		searchBaseWrapper.setCustomList(list);
		
		if( searchBaseWrapper.getCustomList() != null )
		{
			searchBaseWrapper.getCustomList().setResultsetList(this.decryptAccountsForDistHeadSummary(searchBaseWrapper.getCustomList().getResultsetList()));

			try
			{
				getOLAAccountsForDistHeadSummary(searchBaseWrapper, searchBaseWrapper.getCustomList().getResultsetList());
			}
			catch (RuntimeException e)
			{
				e.printStackTrace();
			}
		}
		
		return searchBaseWrapper;
	}
	public List<DistHeadSummaryViewModel> decryptAccountsForDistHeadSummary(List<DistHeadSummaryViewModel> dataList)
	{
		DESEncryption des = DESEncryption.getInstance();

		for (DistHeadSummaryViewModel distHead : dataList)
		{
			try
			{
				if( distHead.getAccountNo() != null )
					distHead.setAccountNo(des.decrypt(distHead.getAccountNo()));
//				distHead.setHeadAccountNo(des.decrypt(distHead.getHeadAccountNo()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return dataList;
	}
	public void getOLAAccountsForDistHeadSummary(SearchBaseWrapper searchBaseWrapper, List<DistHeadSummaryViewModel> list) throws FrameworkCheckedException
	{
		OLASwitchImpl ola = new OLASwitchImpl(this.ctx);
		ola.setAuditLogModuleFacade(this.auditLogModule);
		ola.setXstream(this.xstream);

		try
		{
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dateStr = null;
			OLAVO olaVO = new OLAVO();
			

			if (((DistHeadSummaryViewModel) searchBaseWrapper.getBasePersistableModel()).getStartDate() != null)
				dateStr = format.parse(((DistHeadSummaryViewModel) searchBaseWrapper.getBasePersistableModel()).getStartDate());
			else
				dateStr = new Date();
			
			olaVO.setStatsStartDate(dateStr);
				
				
			if (((DistHeadSummaryViewModel) searchBaseWrapper.getBasePersistableModel()).getEndDate() != null)
					dateStr = format.parse(((DistHeadSummaryViewModel) searchBaseWrapper.getBasePersistableModel()).getEndDate());
			else
				olaVO.setStatsEndDate(new Date());

			olaVO.setStatsEndDate(dateStr);
			
			DecimalFormat decimalFormater = new DecimalFormat("###,##0.00");


			ThreadLocalActionLog.setActionLogId(128447l);

			SwitchWrapper sw = new SwitchWrapperImpl();
			sw.setSwitchSwitchModel(new SwitchModel());
			sw.getSwitchSwitchModel().setUrl( this.getOLAURL() );
			sw.setOlavo(olaVO);

			sw = ola.getAllAccountsStatsWithRange(sw);

			HashMap<String, OLAVO> olaAccounts = sw.getOlaAccountsWithStatsHashMap();

			for (DistHeadSummaryViewModel distHead : list)
			{
				OLAVO olaVOTemp = olaAccounts.get(distHead.getAccountNo());
				if (olaVOTemp != null)
				{
					//					 System.out.println( olaVOTemp.getStartDayBalance() );

					distHead.setOpeningBalance(decimalFormater.format(Double.parseDouble(olaVOTemp.getStartDayBalance())));
					if( olaVOTemp.getEndDayBalance() != null )
					{
//						if( olaVOTemp.getEndDayBalance() == 0 )
//							distHead.setClosingBalance(decimalFormater.format(Double.parseDouble(olaVOTemp.getEndDayBalance())));
//						else
							distHead.setClosingBalance(decimalFormater.format(Double.parseDouble(olaVOTemp.getEndDayBalance())));
						
					}
//					distHead.setBalance(decimalFormater.format(olaVOTemp.getBalance()));
				}

//				 OLAVO olaVORet = olaAccounts.get( distHead.getRetAccountNo() ) ;				 
//				 if( olaVORet != null )
//				 {
//					 distHead.setRetStartDayBalance(decimalFormater.format(Double.parseDouble(olaVORet.getStartDayBalance())));
//					 distHead.setRetBalance(decimalFormater.format(olaVORet.getBalance()));
//					 if( olaVORet.getEndDayBalance() != null )
//						 distHead.setRetEndDayBalance(decimalFormater.format(Double.parseDouble(olaVORet.getEndDayBalance()))) ;
//				 }

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	

	public SearchBaseWrapper searchRegionalHeadSummary(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
//		CustomList<RegionalHeadSummaryViewModel> list = this.regionalHeadSummaryViewDAO.findByExample((RegionalHeadSummaryViewModel) searchBaseWrapper
//				.getBasePersistableModel(), searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
//
//		searchBaseWrapper.setCustomList(list);
//		
		
		CustomList<RegionalHeadSummaryViewModel> list = null ;
		searchBaseWrapper = this.regionalHeadSummaryViewDAO.getRegionalHeadSummary(searchBaseWrapper);
//		
		// List<DistHeadReportListViewModel> list =
		// this.distHeadReportListViewDAO.getReport(searchBaseWrapper);
//		searchBaseWrapper.setCustomList(list);
		
		
		
		searchBaseWrapper.getCustomList().setResultsetList(this.decryptAccountsForRegionalHeadSummary(searchBaseWrapper.getCustomList().getResultsetList()));

		try
		{
			getOLAAccountsForRegionalHeadSummary(searchBaseWrapper, searchBaseWrapper.getCustomList().getResultsetList());
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
		}

		return searchBaseWrapper;
	}
	public List<RegionalHeadSummaryViewModel> decryptAccountsForRegionalHeadSummary(List<RegionalHeadSummaryViewModel> dataList)
	{
		DESEncryption des = DESEncryption.getInstance();

		for (RegionalHeadSummaryViewModel distHead : dataList)
		{
			try
			{
				if( distHead.getAccountNo() != null )
					distHead.setAccountNo(des.decrypt(distHead.getAccountNo()));
//				distHead.setHeadAccountNo(des.decrypt(distHead.getHeadAccountNo()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return dataList;
	}

	public void getOLAAccountsForRegionalHeadSummary(SearchBaseWrapper searchBaseWrapper, List<RegionalHeadSummaryViewModel> list) throws FrameworkCheckedException
	{
		OLASwitchImpl ola = new OLASwitchImpl(this.ctx);
		ola.setAuditLogModuleFacade(this.auditLogModule);
		ola.setXstream(this.xstream);

		try
		{
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dateStr = null;
			OLAVO olaVO = new OLAVO();
			
			if (((RegionalHeadSummaryViewModel) searchBaseWrapper.getBasePersistableModel()).getStartDate() != null)
				dateStr = format.parse(((RegionalHeadSummaryViewModel) searchBaseWrapper.getBasePersistableModel()).getStartDate());
			else
				dateStr = new Date();
			
			olaVO.setStatsStartDate(dateStr);
				
				
			if (((RegionalHeadSummaryViewModel) searchBaseWrapper.getBasePersistableModel()).getEndDate() != null)
					dateStr = format.parse(((RegionalHeadSummaryViewModel) searchBaseWrapper.getBasePersistableModel()).getEndDate());
			else
				olaVO.setStatsEndDate(new Date());

			olaVO.setStatsEndDate(dateStr);

			DecimalFormat decimalFormater = new DecimalFormat("###,###.00");

			
//			olaVO.setStatsDate(dateStr);

			ThreadLocalActionLog.setActionLogId(128447l);

			SwitchWrapper sw = new SwitchWrapperImpl();
			sw.setSwitchSwitchModel(new SwitchModel());
			sw.getSwitchSwitchModel().setUrl( this.getOLAURL() );
			sw.setOlavo(olaVO);

			sw = ola.getAllAccountsStatsWithRange(sw);

			HashMap<String, OLAVO> olaAccounts = sw.getOlaAccountsWithStatsHashMap();

			for (RegionalHeadSummaryViewModel distHead : list)
			{
				OLAVO olaVOTemp = olaAccounts.get(distHead.getAccountNo());
				if (olaVOTemp != null)
				{
					//					 System.out.println( olaVOTemp.getStartDayBalance() );

					distHead.setOpeningBalance(decimalFormater.format(Double.parseDouble(olaVOTemp.getStartDayBalance())));
					if( olaVOTemp.getEndDayBalance() != null )
						distHead.setClosingBalance(decimalFormater.format(Double.parseDouble(olaVOTemp.getEndDayBalance())));
//					distHead.setBalance(decimalFormater.format(olaVOTemp.getBalance()));
				}

//				 OLAVO olaVORet = olaAccounts.get( distHead.getRetAccountNo() ) ;				 
//				 if( olaVORet != null )
//				 {
//					 distHead.setRetStartDayBalance(decimalFormater.format(Double.parseDouble(olaVORet.getStartDayBalance())));
//					 distHead.setRetBalance(decimalFormater.format(olaVORet.getBalance()));
//					 if( olaVORet.getEndDayBalance() != null )
//						 distHead.setRetEndDayBalance(decimalFormater.format(Double.parseDouble(olaVORet.getEndDayBalance()))) ;
//				 }

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public SearchBaseWrapper searchRetSummary(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
//		CustomList<RetailerSummaryViewModel> list = this.retailerSummaryViewDAO.findByExample((RetailerSummaryViewModel) searchBaseWrapper.getBasePersistableModel(),
//				searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());

		// List<DistHeadReportListViewModel> list =
		// this.distHeadReportListViewDAO.getReport(searchBaseWrapper);
//		searchBaseWrapper.setCustomList(list);
		
		CustomList<RetailerSummaryViewModel> list = null ;
		searchBaseWrapper = this.retailerSummaryViewDAO.getRetailerSummary(searchBaseWrapper);
		
		searchBaseWrapper.getCustomList().setResultsetList(this.decryptAccountsForRetSummary(searchBaseWrapper.getCustomList().getResultsetList()));

		try
		{
			getOLAAccountsForRetSummary(searchBaseWrapper, searchBaseWrapper.getCustomList().getResultsetList());
		}
		catch (RuntimeException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return searchBaseWrapper;

	}
	public List<RetailerSummaryViewModel> decryptAccountsForRetSummary(List<RetailerSummaryViewModel> dataList)
	{
		DESEncryption des = DESEncryption.getInstance();

		for (RetailerSummaryViewModel distHead : dataList)
		{
			try
			{
				if( distHead.getAccountNo() != null )
					distHead.setAccountNo(des.decrypt(distHead.getAccountNo()));
//				distHead.setHeadAccountNo(des.decrypt(distHead.getHeadAccountNo()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return dataList;
	}

	public void getOLAAccountsForRetSummary(SearchBaseWrapper searchBaseWrapper, List<RetailerSummaryViewModel> list) throws FrameworkCheckedException
	{
		OLASwitchImpl ola = new OLASwitchImpl(this.ctx);
		ola.setAuditLogModuleFacade(this.auditLogModule);
		ola.setXstream(this.xstream);

		try
		{
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dateStr = null;
			OLAVO olaVO = new OLAVO();
			
			if (((RetailerSummaryViewModel) searchBaseWrapper.getBasePersistableModel()).getStartDate() != null)
				dateStr = format.parse(((RetailerSummaryViewModel) searchBaseWrapper.getBasePersistableModel()).getStartDate());
			else
				dateStr = new Date();
			
			olaVO.setStatsStartDate(dateStr);
				
				
			if (((RetailerSummaryViewModel) searchBaseWrapper.getBasePersistableModel()).getEndDate() != null)
					dateStr = format.parse(((RetailerSummaryViewModel) searchBaseWrapper.getBasePersistableModel()).getEndDate());
			else
				olaVO.setStatsEndDate(new Date());

			olaVO.setStatsEndDate(dateStr);


			DecimalFormat decimalFormater = new DecimalFormat("###,###.00");

			
//			olaVO.setStatsDate(dateStr);

			ThreadLocalActionLog.setActionLogId(128447l);

			SwitchWrapper sw = new SwitchWrapperImpl();
			sw.setSwitchSwitchModel(new SwitchModel());
			sw.getSwitchSwitchModel().setUrl( this.getOLAURL() );
			sw.setOlavo(olaVO);

			sw = ola.getAllAccountsStatsWithRange(sw);

			HashMap<String, OLAVO> olaAccounts = sw.getOlaAccountsWithStatsHashMap();

			for (RetailerSummaryViewModel distHead : list)
			{
				OLAVO olaVOTemp = olaAccounts.get(distHead.getAccountNo());
				if (olaVOTemp != null)
				{
					//					 System.out.println( olaVOTemp.getStartDayBalance() );

					distHead.setOpeningBalance(decimalFormater.format(Double.parseDouble(olaVOTemp.getStartDayBalance())));
					if( olaVOTemp.getEndDayBalance() != null )
						distHead.setClosingBalance(decimalFormater.format(Double.parseDouble(olaVOTemp.getEndDayBalance())));
//					distHead.setBalance(decimalFormater.format(olaVOTemp.getBalance()));
				}

//				 OLAVO olaVORet = olaAccounts.get( distHead.getRetAccountNo() ) ;				 
//				 if( olaVORet != null )
//				 {
//					 distHead.setRetStartDayBalance(decimalFormater.format(Double.parseDouble(olaVORet.getStartDayBalance())));
//					 distHead.setRetBalance(decimalFormater.format(olaVORet.getBalance()));
//					 if( olaVORet.getEndDayBalance() != null )
//						 distHead.setRetEndDayBalance(decimalFormater.format(Double.parseDouble(olaVORet.getEndDayBalance()))) ;
//				 }

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	

	public void setApplicationContext(ApplicationContext ctx) throws BeansException
	{
		this.ctx = ctx;
		auditLogModule = (FailureLogManager) ctx.getBean("auditLogModuleFacade");
		xstream = (XStream) ctx.getBean("xstream");
	}

	public RegionalHeadSummaryViewDAO getRegionalHeadSummaryViewDAO()
	{
		return regionalHeadSummaryViewDAO;
	}

	public void setRegionalHeadSummaryViewDAO(RegionalHeadSummaryViewDAO regionalHeadSummaryViewDAO)
	{
		this.regionalHeadSummaryViewDAO = regionalHeadSummaryViewDAO;
	}

	public DistHeadSummaryViewDAO getDistHeadSummaryViewDAO()
	{
		return distHeadSummaryViewDAO;
	}

	public void setDistHeadSummaryViewDAO(DistHeadSummaryViewDAO distHeadSummaryViewDAO)
	{
		this.distHeadSummaryViewDAO = distHeadSummaryViewDAO;
	}

	public RetailerSummaryViewDAO getRetailerSummaryViewDAO()
	{
		return retailerSummaryViewDAO;
	}

	public void setRetailerSummaryViewDAO(RetailerSummaryViewDAO retailerSummaryViewDAO)
	{
		this.retailerSummaryViewDAO = retailerSummaryViewDAO;
	}

}
