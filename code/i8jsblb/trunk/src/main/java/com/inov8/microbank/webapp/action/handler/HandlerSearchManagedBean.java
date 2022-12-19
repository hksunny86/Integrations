package com.inov8.microbank.webapp.action.handler;

/**
 * @author AtifHu
 */
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.retailermodule.HandlerSearchViewModel;
import com.inov8.microbank.common.util.GenericComparator;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.verifly.common.des.EncryptionHandler;
import org.apache.commons.validator.GenericValidator;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "handlerSearchManagedBean")
@ViewScoped
public class HandlerSearchManagedBean implements Serializable {

	@ManagedProperty(value = "#{accountManager}")
	private AccountManager accountManager;

	@ManagedProperty(value = "#{distributorManager}")
	private DistributorManager distributorManager;

	@ManagedProperty(value = "#{agentHierarchyManager}")
	private AgentHierarchyManager agentHierarchyManager;

	@ManagedProperty(value = "#{appUserDAO}")
	private AppUserDAO appUserDAO;

	@ManagedProperty(value = "#{productCatalogManager}")
	private ProductCatalogManager productCatalogManager;

	@ManagedProperty(value = "#{encryptionHandler}")
	private EncryptionHandler encryptionHandler;

	@ManagedProperty(value = "#{mfsAccountManager}")
	private MfsAccountManager mfsAccountManager;

	@ManagedProperty(value = "#{handlerManager}")
	private HandlerManager handlerManager;

	private List<SelectItem> accountTypeList;
	private List<HandlerSearchViewModel> handlerSearchViewModelList;

	private String userId;
	private String handlerName;
	private String agentId;
	private String agentName;
	private String mobileNumber;
	private Long accountTypeId;

	private int resultSize;

	private int UNSORTED = 0;
	private int ASCENDING_ORDER = 1;
	private int DESCENDING_ORDER = 2;
	private int sortingOrder = 0;

	private int handlerIDSortingOrder = UNSORTED;
	private int handlerNameSortingOrder = UNSORTED;
	private int agentIDSortingOrder = UNSORTED;
	private int agentNameSortingOrder = UNSORTED;
	private int userNameSortingOrder = UNSORTED;
	private int mobileNoSortingOrder = UNSORTED;
	private int areaNameSortingOrder = UNSORTED;
	private int statusSortingOrder = UNSORTED;
	private int accountTypeSortingOrder=UNSORTED;

	private boolean editSecurityCheck;
	private boolean printFormAllowed;

	@PostConstruct
	public void init() {
		accountTypeList = new ArrayList<>();

		try {
			List<OlaCustomerAccountTypeModel> olaCustomerModelList = accountManager.getAllActiveCustomerAccountTypesGrouped();

			for (OlaCustomerAccountTypeModel model : olaCustomerModelList) {
				accountTypeList.add(new SelectItem(model.getCustomerAccountTypeId(), model.getName()));
			}
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
	}

	public void search() {
		handlerSearchViewModelList = new ArrayList<HandlerSearchViewModel>();

		if (this.handlerName != null && this.handlerName.trim().equalsIgnoreCase("")) {
			this.handlerName = null;
		}
		if (this.agentId != null && this.agentId.trim().equalsIgnoreCase("")) {
			this.agentId = null;
		}
		if (this.agentName != null && this.agentName.trim().equalsIgnoreCase("")) {
			this.agentName = null;
		}
		if (this.mobileNumber != null && this.mobileNumber.trim().equalsIgnoreCase("")) {
			this.mobileNumber = null;
		}
		if (this.accountTypeId != null && this.accountTypeId == 0) {
			this.accountTypeId = null;
		}

		HandlerSearchViewModel handlerSearchViewModel = new HandlerSearchViewModel();
		handlerSearchViewModel.setAgentId(agentId);
		handlerSearchViewModel.setAgentName(agentName);

		if (!GenericValidator.isBlankOrNull(userId) && GenericValidator.isLong(userId))
			handlerSearchViewModel.setHandlerId(Long.parseLong(userId));

		handlerSearchViewModel.setHandlerName(handlerName);
		handlerSearchViewModel.setContactNo(mobileNumber);
		handlerSearchViewModel.setOlaCustomerAccountTypeId(accountTypeId);

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(handlerSearchViewModel);

		try {
			searchBaseWrapper = handlerManager.searchHandlerView(searchBaseWrapper);

			if (searchBaseWrapper.getCustomList() != null
					&& searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
				handlerSearchViewModelList = searchBaseWrapper.getCustomList().getResultsetList();
			}
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}

		resultSize = handlerSearchViewModelList.size();
	}

	public String editHandler(HandlerSearchViewModel handlerSearchViewModel) {
		return null;
	}

	public void clearSearchHandlerScreen() {
		this.agentId = null;
		this.agentName = null;
		this.mobileNumber = null;
		this.accountTypeId = null;
		handlerSearchViewModelList = new ArrayList<>();
	}

	private HSSFCellStyle getCellStyle(HSSFWorkbook wb) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.BLACK.index);
		cellStyle.setWrapText(false);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public void exportToExcel() {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("Exported Handler WorkBook");
			HSSFCellStyle cellStyle = getCellStyle(wb);

			HSSFRow row = sheet.createRow(0);

			HSSFCell cell = row.createCell(Short.valueOf("0"));
			cell.setCellValue("Agent ID");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(Short.valueOf("1"));
			cell.setCellValue("Agent Name");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(Short.valueOf("2"));
			cell.setCellValue("Handler ID");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(Short.valueOf("3"));
			cell.setCellValue("Handler Name");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(Short.valueOf("4"));
			cell.setCellValue("User Name");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(Short.valueOf("5"));
			cell.setCellValue("Mobile No");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(Short.valueOf("6"));
			cell.setCellValue("Area Name");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(Short.valueOf("7"));
			cell.setCellValue("Open/Close");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(Short.valueOf("8"));
			cell.setCellValue("Account Type");
			cell.setCellStyle(cellStyle);

			int rowCounter = 1;
			for (HandlerSearchViewModel model : handlerSearchViewModelList) {
				row = sheet.createRow(rowCounter);
				rowCounter++;

				cell = row.createCell(Short.valueOf("0"));
				cell.setCellValue(model.getAgentId());

				cell = row.createCell(Short.valueOf("1"));
				cell.setCellValue(model.getAgentName());

				cell = row.createCell(Short.valueOf("2"));
				cell.setCellValue(model.getUserId());

				cell = row.createCell(Short.valueOf("3"));
				cell.setCellValue(model.getHandlerName());

				cell = row.createCell(Short.valueOf("4"));
				cell.setCellValue(model.getUserName());

				cell = row.createCell(Short.valueOf("5"));
				cell.setCellValue(model.getContactNo());

				cell = row.createCell(Short.valueOf("6"));
				cell.setCellValue(model.getAreaName());

				cell = row.createCell(Short.valueOf("7"));
				cell.setCellValue(model.getAccountClosed());

				cell = row.createCell(Short.valueOf("8"));
				cell.setCellValue(model.getOlaCustomerAccountTypeName());
			}

			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse res = (HttpServletResponse) context.getExternalContext().getResponse();
			res.setContentType("application/vnd.ms-excel");
			res.setHeader("Content-disposition", "attachment;filename=handler-data.xls");

			ServletOutputStream out = res.getOutputStream();
			wb.write(out);
			out.flush();
			out.close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sortByAgentId(ActionEvent e) {
		if (this.agentIDSortingOrder == this.ASCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator(Boolean.FALSE));
			this.agentIDSortingOrder = this.DESCENDING_ORDER;
		} else if (this.agentIDSortingOrder == this.DESCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("index", Boolean.TRUE));
			this.agentIDSortingOrder = this.UNSORTED;
		} else {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("agentId", Boolean.TRUE));
			this.agentIDSortingOrder = this.ASCENDING_ORDER;
		}

		handlerIDSortingOrder = UNSORTED;
		handlerNameSortingOrder = UNSORTED;
		agentNameSortingOrder = UNSORTED;
		userNameSortingOrder = UNSORTED;
		mobileNoSortingOrder = UNSORTED;
		areaNameSortingOrder = UNSORTED;
		statusSortingOrder = UNSORTED;
		accountTypeSortingOrder=UNSORTED;
		
		sortingOrder	=	agentIDSortingOrder;
	}
	
	public void sortByAgentName(ActionEvent e) {
		if (this.agentNameSortingOrder == this.ASCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator(Boolean.FALSE));
			this.agentNameSortingOrder = this.DESCENDING_ORDER;
		} else if (this.agentNameSortingOrder == this.DESCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("index", Boolean.TRUE));
			this.agentNameSortingOrder = this.UNSORTED;
		} else {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("agentName", Boolean.TRUE));
			this.agentNameSortingOrder = this.ASCENDING_ORDER;
		}

		handlerIDSortingOrder = UNSORTED;
		handlerNameSortingOrder = UNSORTED;
		agentIDSortingOrder = UNSORTED;
		userNameSortingOrder = UNSORTED;
		mobileNoSortingOrder = UNSORTED;
		areaNameSortingOrder = UNSORTED;
		statusSortingOrder = UNSORTED;
		accountTypeSortingOrder=UNSORTED;
		
		sortingOrder	=	agentNameSortingOrder;
	}
	
	public void sortByHandlerId(ActionEvent e) {
		if (this.handlerIDSortingOrder == this.ASCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator(Boolean.FALSE));
			this.handlerIDSortingOrder = this.DESCENDING_ORDER;
		} else if (this.handlerIDSortingOrder == this.DESCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("index", Boolean.TRUE));
			this.handlerIDSortingOrder = this.UNSORTED;
		} else {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("handlerID", Boolean.TRUE));
			this.handlerIDSortingOrder = this.ASCENDING_ORDER;
		}

		handlerNameSortingOrder = UNSORTED;
		agentIDSortingOrder = UNSORTED;
		agentNameSortingOrder = UNSORTED;
		userNameSortingOrder = UNSORTED;
		mobileNoSortingOrder = UNSORTED;
		areaNameSortingOrder = UNSORTED;
		statusSortingOrder = UNSORTED;
		accountTypeSortingOrder=UNSORTED;
		
		sortingOrder	=	handlerIDSortingOrder;
	}
	
	public void sortByHandlerName(ActionEvent e) {
		if (this.handlerNameSortingOrder == this.ASCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator(Boolean.FALSE));
			this.handlerNameSortingOrder = this.DESCENDING_ORDER;
		} else if (this.handlerNameSortingOrder == this.DESCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("index", Boolean.TRUE));
			this.handlerNameSortingOrder = this.UNSORTED;
		} else {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("handlerName", Boolean.TRUE));
			this.handlerNameSortingOrder = this.ASCENDING_ORDER;
		}

		handlerIDSortingOrder = UNSORTED;
		agentIDSortingOrder = UNSORTED;
		agentNameSortingOrder = UNSORTED;
		userNameSortingOrder = UNSORTED;
		mobileNoSortingOrder = UNSORTED;
		areaNameSortingOrder = UNSORTED;
		statusSortingOrder = UNSORTED;
		accountTypeSortingOrder=UNSORTED;
		
		sortingOrder	=	handlerNameSortingOrder;
	}

	//
	
	public void sortByUserName(ActionEvent e) {
		if (this.userNameSortingOrder == this.ASCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator(Boolean.FALSE));
			this.userNameSortingOrder = this.DESCENDING_ORDER;
		} else if (this.userNameSortingOrder == this.DESCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("index", Boolean.TRUE));
			this.userNameSortingOrder = this.UNSORTED;
		} else {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("userName", Boolean.TRUE));
			this.userNameSortingOrder = this.ASCENDING_ORDER;
		}

		handlerIDSortingOrder = UNSORTED;
		handlerNameSortingOrder = UNSORTED;
		agentIDSortingOrder = UNSORTED;
		agentNameSortingOrder = UNSORTED;
		mobileNoSortingOrder = UNSORTED;
		areaNameSortingOrder = UNSORTED;
		statusSortingOrder = UNSORTED;
		accountTypeSortingOrder=UNSORTED;
		
		sortingOrder	=	userNameSortingOrder;
	}

	public void sortByContactNo(ActionEvent e) {
		if (this.mobileNoSortingOrder == this.ASCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator(Boolean.FALSE));
			this.mobileNoSortingOrder = this.DESCENDING_ORDER;
		} else if (this.mobileNoSortingOrder == this.DESCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("index", Boolean.TRUE));
			this.mobileNoSortingOrder = this.UNSORTED;
		} else {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("contactNo", Boolean.TRUE));
			this.mobileNoSortingOrder = this.ASCENDING_ORDER;
		}

		handlerIDSortingOrder = UNSORTED;
		handlerNameSortingOrder = UNSORTED;
		agentIDSortingOrder = UNSORTED;
		agentNameSortingOrder = UNSORTED;
		userNameSortingOrder = UNSORTED;
		areaNameSortingOrder = UNSORTED;
		statusSortingOrder = UNSORTED;
		accountTypeSortingOrder=UNSORTED;
		
		sortingOrder	=	mobileNoSortingOrder;
	}

	public void sortByAreaName(ActionEvent e) {
		if (this.areaNameSortingOrder == this.ASCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator(Boolean.FALSE));
			this.areaNameSortingOrder = this.DESCENDING_ORDER;
		} else if (this.areaNameSortingOrder == this.DESCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("index", Boolean.TRUE));
			this.areaNameSortingOrder = this.UNSORTED;
		} else {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("areaName", Boolean.TRUE));
			this.areaNameSortingOrder = this.ASCENDING_ORDER;
		}

		handlerIDSortingOrder = UNSORTED;
		handlerNameSortingOrder = UNSORTED;
		agentIDSortingOrder = UNSORTED;
		agentNameSortingOrder = UNSORTED;
		userNameSortingOrder = UNSORTED;
		statusSortingOrder = UNSORTED;
		mobileNoSortingOrder=UNSORTED;
		accountTypeSortingOrder=UNSORTED;
		
		sortingOrder	=	areaNameSortingOrder;
	}
	
	
	public void sortByStatus(ActionEvent e) {
		if (this.statusSortingOrder == this.ASCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator(Boolean.FALSE));
			this.statusSortingOrder = this.DESCENDING_ORDER;
		} else if (this.statusSortingOrder == this.DESCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("index", Boolean.TRUE));
			this.statusSortingOrder = this.UNSORTED;
		} else {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("accountClosed", Boolean.TRUE));
			this.statusSortingOrder = this.ASCENDING_ORDER;
		}

		handlerIDSortingOrder = UNSORTED;
		handlerNameSortingOrder = UNSORTED;
		agentIDSortingOrder = UNSORTED;
		agentNameSortingOrder = UNSORTED;
		userNameSortingOrder = UNSORTED;
		mobileNoSortingOrder=UNSORTED;
		areaNameSortingOrder=UNSORTED;
		accountTypeSortingOrder=UNSORTED;
		
		sortingOrder	=	statusSortingOrder;
	}
	
	public void sortByAccountType(ActionEvent e) {
		if (this.accountTypeSortingOrder == this.ASCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator(Boolean.FALSE));
			this.accountTypeSortingOrder = this.DESCENDING_ORDER;
		} else if (this.accountTypeSortingOrder == this.DESCENDING_ORDER) {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("index", Boolean.TRUE));
			this.accountTypeSortingOrder = this.UNSORTED;
		} else {
			Collections.sort(this.handlerSearchViewModelList, new GenericComparator("olaCustomerAccountTypeName", Boolean.TRUE));
			this.accountTypeSortingOrder = this.ASCENDING_ORDER;
		}

		handlerIDSortingOrder = UNSORTED;
		handlerNameSortingOrder = UNSORTED;
		agentIDSortingOrder = UNSORTED;
		agentNameSortingOrder = UNSORTED;
		userNameSortingOrder = UNSORTED;
		mobileNoSortingOrder=UNSORTED;
		areaNameSortingOrder=UNSORTED;
		statusSortingOrder	=UNSORTED;
		
		sortingOrder	=	accountTypeSortingOrder;
	}
	
	public List<SelectItem> getAccountTypeList() {
		return accountTypeList;
	}

	public void setAccountTypeList(List<SelectItem> accountTypeList) {
		this.accountTypeList = accountTypeList;
	}

	public AccountManager getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public DistributorManager getDistributorManager() {
		return distributorManager;
	}

	public void setDistributorManager(DistributorManager distributorManager) {
		this.distributorManager = distributorManager;
	}

	public AgentHierarchyManager getAgentHierarchyManager() {
		return agentHierarchyManager;
	}

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}

	public AppUserDAO getAppUserDAO() {
		return appUserDAO;
	}

	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}

	public ProductCatalogManager getProductCatalogManager() {
		return productCatalogManager;
	}

	public void setProductCatalogManager(ProductCatalogManager productCatalogManager) {
		this.productCatalogManager = productCatalogManager;
	}

	public EncryptionHandler getEncryptionHandler() {
		return encryptionHandler;
	}

	public void setEncryptionHandler(EncryptionHandler encryptionHandler) {
		this.encryptionHandler = encryptionHandler;
	}

	public MfsAccountManager getMfsAccountManager() {
		return mfsAccountManager;
	}

	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}

	public HandlerManager getHandlerManager() {
		return handlerManager;
	}

	public void setHandlerManager(HandlerManager handlerManager) {
		this.handlerManager = handlerManager;
	}

	public List<HandlerSearchViewModel> getHandlerSearchViewModelList() {
		return handlerSearchViewModelList;
	}

	public void setHandlerSearchViewModelList(List<HandlerSearchViewModel> handlerSearchViewModelList) {
		this.handlerSearchViewModelList = handlerSearchViewModelList;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Long getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(Long accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	public int getResultSize() {
		return resultSize;
	}

	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	public boolean isEditSecurityCheck() {
		return editSecurityCheck;
	}

	public void setEditSecurityCheck(boolean editSecurityCheck) {
		this.editSecurityCheck = editSecurityCheck;
	}

	public int getAgentIDSortingOrder() {
		return agentIDSortingOrder;
	}

	public void setAgentIDSortingOrder(int agentIDSortingOrder) {
		this.agentIDSortingOrder = agentIDSortingOrder;
	}

	public int getAgentNameSortingOrder() {
		return agentNameSortingOrder;
	}

	public void setAgentNameSortingOrder(int agentNameSortingOrder) {
		this.agentNameSortingOrder = agentNameSortingOrder;
	}

	public int getUserNameSortingOrder() {
		return userNameSortingOrder;
	}

	public void setUserNameSortingOrder(int userNameSortingOrder) {
		this.userNameSortingOrder = userNameSortingOrder;
	}

	public int getMobileNoSortingOrder() {
		return mobileNoSortingOrder;
	}

	public void setMobileNoSortingOrder(int mobileNoSortingOrder) {
		this.mobileNoSortingOrder = mobileNoSortingOrder;
	}

	public int getAreaNameSortingOrder() {
		return areaNameSortingOrder;
	}

	public void setAreaNameSortingOrder(int areaNameSortingOrder) {
		this.areaNameSortingOrder = areaNameSortingOrder;
	}

	public int getStatusSortingOrder() {
		return statusSortingOrder;
	}

	public void setStatusSortingOrder(int statusSortingOrder) {
		this.statusSortingOrder = statusSortingOrder;
	}

	public boolean isPrintFormAllowed() {
		return printFormAllowed;
	}

	public void setPrintFormAllowed(boolean printFormAllowed) {
		this.printFormAllowed = printFormAllowed;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public int getHandlerIDSortingOrder() {
		return handlerIDSortingOrder;
	}

	public void setHandlerIDSortingOrder(int handlerIDSortingOrder) {
		this.handlerIDSortingOrder = handlerIDSortingOrder;
	}

	public int getHandlerNameSortingOrder() {
		return handlerNameSortingOrder;
	}

	public void setHandlerNameSortingOrder(int handlerNameSortingOrder) {
		this.handlerNameSortingOrder = handlerNameSortingOrder;
	}

	public int getSortingOrder() {
		return sortingOrder;
	}

	public void setSortingOrder(int sortingOrder) {
		this.sortingOrder = sortingOrder;
	}

	public int getAccountTypeSortingOrder() {
		return accountTypeSortingOrder;
	}

	public void setAccountTypeSortingOrder(int accountTypeSortingOrder) {
		this.accountTypeSortingOrder = accountTypeSortingOrder;
	}

}