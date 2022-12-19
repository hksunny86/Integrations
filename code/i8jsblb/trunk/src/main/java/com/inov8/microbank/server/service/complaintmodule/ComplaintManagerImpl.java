package com.inov8.microbank.server.service.complaintmodule;

import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL0_ASSIGNEE;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL0_ASSIGNEE_EMAIL;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL0_ASSIGNEE_NAME;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL0_ASSIGNEE_TAT;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL1_ASSIGNEE;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL1_ASSIGNEE_EMAIL;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL1_ASSIGNEE_NAME;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL1_ASSIGNEE_TAT;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL2_ASSIGNEE;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL2_ASSIGNEE_EMAIL;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL2_ASSIGNEE_NAME;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL2_ASSIGNEE_TAT;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL3_ASSIGNEE;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL3_ASSIGNEE_EMAIL;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL3_ASSIGNEE_NAME;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL3_ASSIGNEE_TAT;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL_ASSIGNEE_EMAIL_PARAM;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL_ASSIGNEE_NAME_PARAM;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL_ASSIGNEE_PARAM;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL_ASSIGNEE_TAT_PARAM;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;
import org.springframework.context.MessageSource;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.messagemodule.EmailMessage;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.StringUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.GenericDAO;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ComplaintCategoryModel;
import com.inov8.microbank.common.model.ComplaintHistoryModel;
import com.inov8.microbank.common.model.ComplaintModel;
import com.inov8.microbank.common.model.ComplaintReportModel;
import com.inov8.microbank.common.model.ComplaintSubcategoryModel;
import com.inov8.microbank.common.model.ComplaintSubcategoryViewModel;
import com.inov8.microbank.common.model.HolidayModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.complaint.ComplaintDetailVO;
import com.inov8.microbank.common.model.portal.complaint.ComplaintHistoryVO;
import com.inov8.microbank.common.model.portal.complaint.ComplaintHistoryViewModel;
import com.inov8.microbank.common.model.portal.complaint.ComplaintListViewModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintCategoryDAO;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintDAO;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintHistoryDAO;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintHistoryViewDAO;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintListViewDAO;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintParamValueDAO;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintReportDAO;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryViewDAO;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.holidaymodule.HolidayManager;
import com.inov8.microbank.server.service.jms.JmsProducer;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.webapp.action.allpayweb.AllPayWebConstant;

public class ComplaintManagerImpl implements ComplaintManager {

	private final static Log logger = LogFactory.getLog(ComplaintManagerImpl.class);
	
	private ComplaintDAO complaintDAO;
	private ComplaintHistoryDAO complaintHistoryDAO;
	private ComplaintSubcategoryDAO complaintSubcategoryDAO;
	private ComplaintSubcategoryViewDAO complaintSubcategoryViewDAO;
	private ComplaintListViewDAO complaintListViewDAO;
	private ComplaintHistoryViewDAO complaintHistoryViewDAO;
	private ComplaintCategoryDAO complaintCategoryDAO;
	private ComplaintParamValueDAO complaintParamValueDAO;
	private JmsProducer jmsProducer;
	private MessageSource messageSource;
	private HolidayManager holidayManager;
	private SmsSender smsSender = null;
	private Boolean workingSaturday = Boolean.FALSE;
	private ComplaintReportDAO complaintReportDAO;
	private ActionLogManager actionLogManager;

	private UserDeviceAccountsManager userDeviceAccountsManager = null;
	private AppUserDAO appUserDAO;
	private GenericDAO genDao;
	private String escalationLevel1Emails = null;
	private String escalationLevel2Emails = null;
	private String escalationLevel3Emails = null;

	public void saveUpdateAllHistory(List<ComplaintHistoryModel> complaintHistoryModelList) throws FrameworkCheckedException {
		
		if(!complaintHistoryModelList.isEmpty()) {
			
			complaintHistoryDAO.saveOrUpdateCollection(complaintHistoryModelList);
		}
	}	
	
	public void saveUpdate(ComplaintModel complaintModel, ComplaintReportModel reportModel) throws FrameworkCheckedException {
		
		complaintDAO.saveOrUpdate(complaintModel);
		complaintReportDAO.saveOrUpdate(reportModel);
		complaintHistoryDAO.updateComplaintHistoryStatus(reportModel.getComplaintId(), complaintModel.getStatus(), complaintModel.getRemarks());
		logger.info(complaintModel);
	}
	
	
	public List<ComplaintHistoryModel> getComplaintHistoryModelList(ComplaintHistoryModel complaintHistoryModel) throws FrameworkCheckedException {
		
		return complaintHistoryDAO.getComplaintHistoryModelList(complaintHistoryModel);
	}
	
	
	public ComplaintCategoryModel getComplaintCategoryModel(Long complaintCategoryId) throws FrameworkCheckedException {
		
		return complaintCategoryDAO.findByPrimaryKey(complaintCategoryId);
	}
	
	
	public ComplaintSubcategoryModel getComplaintSubcategoryModel(Long complaintSubcategoryId) throws FrameworkCheckedException {
		
		return complaintSubcategoryDAO.findByPrimaryKey(complaintSubcategoryId);
	}
	
	
	public List<ComplaintHistoryVO> getComplaintHistoryModelList(Long complaintId) throws FrameworkCheckedException {
		
		return complaintHistoryDAO.getComplaintHistoryVOList(complaintId);
	}
	
	
	public ComplaintModel getComplaintModelByPrimaryKey(Long complaintModelId) throws FrameworkCheckedException {
		
		return complaintDAO.findByPrimaryKey(complaintModelId);
	}
	
	public ComplaintReportModel loadComplaintReportByComplaintId(Long complaintId) throws FrameworkCheckedException {
		ComplaintReportModel complaintReportModel = new ComplaintReportModel();
		complaintReportModel.setComplaintId(complaintId);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		CustomList<ComplaintReportModel> list = complaintReportDAO.findByExample(complaintReportModel,null,null,exampleHolder);
		if(list != null && list.getResultsetList().size() > 0){
			complaintReportModel = list.getResultsetList().get(0);
		}
		return complaintReportModel;
	}
	
	public Map<String, Object> getTurnedAroundTime(Integer displayOrder, Long complaintId) throws FrameworkCheckedException {
		
		Map<String, Object> map = complaintSubcategoryDAO.getComplaintSubcategoryByComplaintId(complaintId);
		
		String assigneeAppUserName = null;
		String assigneeAppUserEmail = null;
		BigDecimal assigneeAppUserId = null;
		BigDecimal turnedAroundTime = new BigDecimal(0);		
		
		switch (displayOrder) {

			case 0: {
				
				turnedAroundTime = (BigDecimal) map.get(LEVEL0_ASSIGNEE_TAT);
				assigneeAppUserId = (BigDecimal) map.get(LEVEL0_ASSIGNEE);
				assigneeAppUserName = (String) map.get(LEVEL0_ASSIGNEE_NAME);
				assigneeAppUserEmail = (String) map.get(LEVEL0_ASSIGNEE_EMAIL);				
				break;
			} 
			
			case 1: {
				
				turnedAroundTime = (BigDecimal) map.get(LEVEL1_ASSIGNEE_TAT);
				assigneeAppUserId = (BigDecimal) map.get(LEVEL1_ASSIGNEE);
				assigneeAppUserName = (String) map.get(LEVEL1_ASSIGNEE_NAME);
				assigneeAppUserEmail = (String) map.get(LEVEL1_ASSIGNEE_EMAIL);				
				break;
			} 
			
			case 2: {
				
				turnedAroundTime = (BigDecimal) map.get(LEVEL2_ASSIGNEE_TAT);
				assigneeAppUserId = (BigDecimal) map.get(LEVEL2_ASSIGNEE);
				assigneeAppUserName = (String) map.get(LEVEL2_ASSIGNEE_NAME);
				assigneeAppUserEmail = (String) map.get(LEVEL2_ASSIGNEE_EMAIL);				
				break;
			} 
			
			case 3: {

				turnedAroundTime = (BigDecimal) map.get(LEVEL3_ASSIGNEE_TAT);
				assigneeAppUserId = (BigDecimal) map.get(LEVEL3_ASSIGNEE);
				assigneeAppUserName = (String) map.get(LEVEL3_ASSIGNEE_NAME);
				assigneeAppUserEmail = (String) map.get(LEVEL3_ASSIGNEE_EMAIL);
				break;
			} 
		}

		map.put(LEVEL_ASSIGNEE_TAT_PARAM, turnedAroundTime);	
		map.put(LEVEL_ASSIGNEE_PARAM, assigneeAppUserId.longValue());
		map.put(LEVEL_ASSIGNEE_NAME_PARAM, assigneeAppUserName);	
		map.put(LEVEL_ASSIGNEE_EMAIL_PARAM, assigneeAppUserEmail);	
		
		return map;
	}

	public BaseWrapper createComplaint(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		Date nowDate = new Date();
		ComplaintModel complaintModel = (ComplaintModel)baseWrapper.getBasePersistableModel();
		String cCode = getUniqueComplaintCode();
		complaintModel.setComplaintCode(cCode);

		ComplaintCategoryModel categoryModel = this.complaintCategoryDAO.findByPrimaryKey(complaintModel.getComplaintCategoryId());
		ComplaintSubcategoryModel subcategoryModel = this.complaintSubcategoryDAO.findByPrimaryKey(complaintModel.getComplaintSubcategoryId());
		
		if(categoryModel.getComplaintCategoryId().longValue() != subcategoryModel.getComplaintCategoryId().longValue()){
			throw new FrameworkCheckedException("Invalid data mapping. Complait Catagory ID : "+categoryModel.getComplaintCategoryId()+" cannnot be mapped with Compalint Subcatagory ID : "+subcategoryModel.getComplaintSubcategoryId());
		}
		
		Expected_Turned_Around_Time : {
			
			Date expectedTat = calcExpectedTat(subcategoryModel);			
			logger.info("@Expected TAT "+ expectedTat);
			complaintModel.setExpectedTat(expectedTat);
		}
		
		this.complaintDAO.saveOrUpdate(complaintModel);
		
		ComplaintHistoryModel historyModel = new ComplaintHistoryModel();
		historyModel.setComplaintId(complaintModel.getComplaintId());
		historyModel.setAppUserId(subcategoryModel.getLevel0Assignee());
		historyModel.setAssignedOn(nowDate);
		
		Tat_End_Time : {
			
			int level0Tat = subcategoryModel.getLevel0AssigneeTat().intValue();
			Calendar gregorianCalendar = new GregorianCalendar().getInstance();
			
//			Date tatEndTime = calcTurnedAroundTime(level0Tat, gregorianCalendar);
			Date tatEndTime = calcExpectedTat(level0Tat, gregorianCalendar);	
			logger.info("@Tat_End_Time "+ tatEndTime);
			historyModel.setTatEndTime(tatEndTime);
		}
		
		historyModel.setCreatedBy(complaintModel.getCreatedBy());
		historyModel.setCreatedOn(nowDate);
		historyModel.setStatus(ComplaintsModuleConstants.STATUS_ASSIGNED);
		historyModel.setDisplayOrder(0);
		this.complaintHistoryDAO.saveOrUpdate(historyModel);

		ComplaintReportModel complaintReportModel = new ComplaintReportModel();
		try {
			BeanUtils.copyProperties(complaintReportModel, complaintModel);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		complaintReportModel.setComplaintReportId(null);
		complaintReportModel.setVersionNo(null);
		
		complaintReportModel.setCreatedByName(UserUtils.getCurrentUser().getFirstName()+" "+UserUtils.getCurrentUser().getLastName());

		AppUserModel currentAssigneeAppUserModel = appUserDAO.getUser(historyModel.getAppUserId());
		complaintReportModel.setCurrentAssigneeId(historyModel.getAppUserId());
		complaintReportModel.setCurrentAssigneeName(currentAssigneeAppUserModel.getFirstName()+" "+currentAssigneeAppUserModel.getLastName());
		complaintReportModel.setLevel0AssigneeId(historyModel.getAppUserId());
		complaintReportModel.setLevel0AssigneeName(complaintReportModel.getCurrentAssigneeName());
		complaintReportModel.setLevel0TATEndTime(historyModel.getTatEndTime());
		complaintReportModel.setComplaintCategory(categoryModel.getName());
		complaintReportModel.setComplaintSubcategory(subcategoryModel.getName());
		
		this.complaintReportDAO.saveOrUpdate(complaintReportModel);
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String mobileno = complaintModel.getMobileNo();
		Object[] args = { complaintModel.getComplaintCode(),df.format(complaintModel.getExpectedTat())};

		String messageString = MessageUtil.getMessage("complaint.add.sms", args);
		SmsMessage smsMessage = new SmsMessage(mobileno, messageString);
		smsSender.send(smsMessage);

		baseWrapper.setBasePersistableModel(complaintModel);
		return baseWrapper;
	}
	
	
	public void createComplaint(Long complaintCategoryId, Long appUserIdCustomer) throws FrameworkCheckedException {
		createComplaint(complaintCategoryId, appUserIdCustomer, null, new Date());
	}

	public void createComplaint(Long complaintCategoryId, Long appUserIdCustomer, String comments, Date updationTime) throws FrameworkCheckedException {
		
		AppUserModel appUserModel = UserUtils.getCurrentUser();
		
		ComplaintReportModel complaintReportModel = new ComplaintReportModel();
				
		ComplaintModel complaintModel = new ComplaintModel();
		
		complaintModel.setComplaintCode(getUniqueComplaintCode());
		complaintModel.setCreatedOn(updationTime);
		complaintModel.setComplaintCategoryId(complaintCategoryId);
		complaintModel.setStatus(ComplaintsModuleConstants.STATUS_RESOLVED);
		complaintModel.setEscalationStatus(ComplaintsModuleConstants.STATUS_RESOLVED);
		complaintModel.setCreatedBy(appUserModel.getAppUserId());
		complaintModel.setClosedBy(appUserModel.getAppUserId());
		complaintModel.setClosedOn(updationTime);
		
		if(comments != null){
			if(comments.length() > 250){
				comments = comments.substring(0,250);
			}
			complaintModel.setRemarks(comments);
		}
		
		try {
			
			if(appUserIdCustomer != null) {
			
				AppUserModel appUserCustomer = appUserDAO.getUser(appUserIdCustomer);
				
				if(appUserCustomer != null) {
					
					String initiatorId = getComplaintInitiator(appUserIdCustomer);
					complaintModel.setInitiatorType(getAppUserTypeString(appUserCustomer.getAppUserTypeId()));
					complaintModel.setInitiatorId(initiatorId);
					complaintModel.setInitAppUserId(appUserIdCustomer);
					complaintModel.setInitiatorCNIC(appUserCustomer.getNic());
					complaintModel.setMobileNo(appUserCustomer.getMobileNo());
					complaintModel.setInitiatorName(appUserCustomer.getFirstName() +" "+appUserCustomer.getLastName());					
				}
				
				BeanUtils.copyProperties(complaintReportModel, complaintModel);
			}
			
			this.complaintDAO.saveOrUpdate(complaintModel);
			
			ComplaintCategoryModel complaintCategoryModel = getComplaintCategoryModel(complaintCategoryId);
			complaintReportModel.setCreatedByName(appUserModel.getFirstName()+" "+appUserModel.getLastName());
			complaintReportModel.setClosedByName(complaintReportModel.getCreatedByName());
			
			complaintReportModel.setComplaintId(complaintModel.getComplaintId());
			complaintReportModel.setComplaintReportId(null);
			complaintReportModel.setComplaintCategory(complaintCategoryModel.getName());
			
			this.complaintReportDAO.saveOrUpdate(complaintReportModel);
			
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	private String getComplaintInitiator(Long appUserIdCustomer) throws Exception {
		
		String initiatorId = "";
		
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(appUserIdCustomer);
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		
		baseWrapper = userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
		
		if(baseWrapper != null && baseWrapper.getBasePersistableModel() != null) {
			
			userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
			
			if(userDeviceAccountsModel != null) {
				
				initiatorId = userDeviceAccountsModel.getUserId();
			}
		}
		
		return initiatorId;		
	}
	
	public void sendEscalationEmail(List<ComplaintHistoryModel> complaintHistoryModelEmailList) throws FrameworkCheckedException {		
		
		if(!complaintHistoryModelEmailList.isEmpty()) {
			
			for(ComplaintHistoryModel complaintHistoryModel : complaintHistoryModelEmailList) {
				
				List<Object> emailContentsList = getEscalationEmailContents(complaintHistoryModel);
							
				EmailMessage emailMessage = new EmailMessage();
					
				emailMessage.setRecepients((String[])emailContentsList.get(0));
				emailMessage.setSubject((String)emailContentsList.get(1));
				emailMessage.setText((String)emailContentsList.get(2));
					
				jmsProducer.produce(emailMessage, DestinationConstants.EMAIL_DESTINATION);	
			}
		}
	}
	
	public List<Object> getEscalationEmailContents(ComplaintHistoryModel complaintHistoryModel) throws FrameworkCheckedException {
		
		String[] emails = null;
		List<Object> emailContentsList = new ArrayList<>(0);		
		Integer level = complaintHistoryModel.getDisplayOrder();
		Object[] parameters = new Object[7];

		parameters[0] = complaintHistoryModel.getComplaintHistoryVO().getComplaintCode();
		parameters[1] = complaintHistoryModel.getAssignedOn();
		parameters[2] = "Escalated Level "+level;
		parameters[3] = complaintHistoryModel.getTatEndTime();
		parameters[4] = complaintHistoryModel.getComplaintHistoryVO().getComplaintCategory();
		parameters[5] = complaintHistoryModel.getComplaintHistoryVO().getTitle();
		
		if(level.intValue() == 1) {
			emails = this.escalationLevel1Emails.split(",");
		} else if(level.intValue() == 2) {
			emails = this.escalationLevel2Emails.split(",");
		} else if(level.intValue() == 3) {
			emails = this.escalationLevel3Emails.split(",");
		}
		
		String subject = (messageSource.getMessage("ComplaintEscalationScheduler.EMAIL_SUBJECT", new Object[]{parameters[0]}, null));

		String bodyTextContent = messageSource.getMessage("ComplaintEscalationScheduler.EMAIL_MESSAGE", parameters, null);
		String bodyFooterContent = messageSource.getMessage("ComplaintEscalationScheduler.EMAIL_MESSAGE_CLOSING", null, null);
		
		emailContentsList.add(emails);
		emailContentsList.add(subject);
		emailContentsList.add(bodyTextContent+bodyFooterContent);
		
		return emailContentsList;
	}	


	private Date calcExpectedTat(ComplaintSubcategoryModel subcategoryModel) throws FrameworkCheckedException {
		
		Integer totalTat = subcategoryModel.getTotalTat().intValue();
		Boolean is24HourTAT = (totalTat >= 24 ? Boolean.TRUE : Boolean.FALSE);		

		final Integer ONE_HOUR = 1;
		Calendar calendar = new GregorianCalendar();
				
		for(int i = 1; i<=totalTat; i++) {

			calcTurnedAroundTime(ONE_HOUR, calendar);
		}

		return calendar.getTime();
	}


	public Date calcExpectedTat(Integer totalTat, Calendar calendar) throws FrameworkCheckedException {
		
		final Integer ONE_HOUR = 1;
				
		for(int i = 1; i<=totalTat; i++) {

			calcTurnedAroundTime(ONE_HOUR, calendar);
		}

		return calendar.getTime();
	}
	

	
	public Date calcTurnedAroundTime(Integer assigneeTurnedAroundTimeHour, Calendar calendar) throws FrameworkCheckedException {

		int hour = 0;
				
		int DAY_OF_WEEK = calendar.get(Calendar.DAY_OF_WEEK);

		HolidayModel holidayModel = holidayManager.getHolidayModelByDate(calendar.getTime());
		
		Boolean isHoliday = (holidayModel != null ? Boolean.TRUE : Boolean.FALSE);		
		Boolean is24HourTAT = (assigneeTurnedAroundTimeHour >= 24 ? Boolean.TRUE : Boolean.FALSE);	
		
		logger.info(calendar.getTime());
		
		if(isHoliday) {
					
			if(!isHolidayInWeekEnd(holidayModel)) {
			
				if(is24HourTAT) {
					
					calendar.set(Calendar.HOUR_OF_DAY, 23);
					calendar.set(Calendar.MINUTE, 59);
					calendar.set(Calendar.SECOND, 59);
					
				} else {
	
					calendar.set(Calendar.HOUR_OF_DAY, 00);
					calendar.set(Calendar.MINUTE, 00);
					calendar.set(Calendar.SECOND, 00);
				}
			}
			
		} else if(is24HourTAT) {
			
			switch(DAY_OF_WEEK) {

				case (Calendar.FRIDAY): {
//					hour = (24 * 2);
					break;
				}
	
				case (Calendar.SATURDAY): {
					
					hour = -(24 * 1);

					calendar.set(Calendar.HOUR_OF_DAY, 23);
					calendar.set(Calendar.MINUTE, 59);
					calendar.set(Calendar.SECOND, 59);
					break;
				}
		
				case (Calendar.SUNDAY): {

					hour = -(24 * 2);
					
					calendar.set(Calendar.HOUR_OF_DAY, 23);
					calendar.set(Calendar.MINUTE, 59);
					calendar.set(Calendar.SECOND, 59);
					break;
				}
			}
			
		} else {
			
			int _hour = 0;
			
			switch(DAY_OF_WEEK) {

				case (Calendar.SATURDAY): {
					_hour = (24*2);			
					calendar.set(Calendar.HOUR_OF_DAY, 00);
					calendar.set(Calendar.MINUTE, 00);
					calendar.set(Calendar.SECOND, 00);		
					break;
				}
		
				case (Calendar.SUNDAY): {
					_hour = (24);
					calendar.set(Calendar.HOUR_OF_DAY, 00);
					calendar.set(Calendar.MINUTE, 00);
					calendar.set(Calendar.SECOND, 00);
					break;
				}
			}
			
			calendar.add(calendar.HOUR, _hour); // Start of Monday
		}

		logger.info(calendar.getTime());
		
		return getTurnedAroundTime(assigneeTurnedAroundTimeHour, calendar, is24HourTAT, isHoliday);
	}
	
	/**
	 * @param assigneeTurnedAroundTime i.e LEVEL0_ASSIGNEE_TAT, LEVEL1_ASSIGNEE_TAT, LEVEL2_ASSIGNEE_TAT, LEVEL3_ASSIGNEE_TAT of COMPLAINT_SUBCATEGORY_ID
	 * @return calendar
	 */
	private Date getTurnedAroundTime(Integer _assigneeTurnedAroundTimeHour, Calendar calendar, Boolean is24HourTAT, Boolean isHoliday) throws FrameworkCheckedException {
		
		Integer assigneeTurnedAroundTimeHour = _assigneeTurnedAroundTimeHour;
		
		if(is24HourTAT) {			
			assigneeTurnedAroundTimeHour = checkWeekEndInRange(_assigneeTurnedAroundTimeHour, calendar);
		}
		
		logger.info(calendar.getTime());
		
		GregorianCalendar todayCalendar = (GregorianCalendar) calendar.clone();
		
		calendar.add(calendar.HOUR, assigneeTurnedAroundTimeHour);
		
		logger.info(calendar.getTime());
		
		if(isHoliday) {
			
			this.setHolidayModelTAT(todayCalendar, calendar, is24HourTAT);
			
		} else {
			this.setHolidayModelTAT(GregorianCalendar.getInstance(), calendar, is24HourTAT);

		}
		
				
		GregorianCalendar holidayTATCalendar = (GregorianCalendar) calendar.clone();
		
		this.setCalendarTAT(calendar);
				
		this.setHolidayModelTAT(holidayTATCalendar, calendar, is24HourTAT);

		return calendar.getTime();
	}
	
	
	/**
	 * @param calendar
	 */
	private void setCalendarTAT(Calendar calendar) {

		Integer totalTurnedAroundTime = 0;
		Integer DAY_OF_WEEK = calendar.get(Calendar.DAY_OF_WEEK);
		
		if((DAY_OF_WEEK.intValue() == Calendar.SATURDAY) && !workingSaturday) {
			
			totalTurnedAroundTime += (24 * 2);
			
		} else if(DAY_OF_WEEK.intValue() == Calendar.SUNDAY) {

			totalTurnedAroundTime += (24);
		}	

		calendar.add(calendar.HOUR, (totalTurnedAroundTime));		
		logger.info(calendar.getTime());
	}	
	
	
	private Integer checkWeekEndInRange(Integer _assigneeTurnedAroundTimeHour, Calendar calendar) {
		
		Integer assigneeTurnedAroundTimeHour = _assigneeTurnedAroundTimeHour;
		
		GregorianCalendar cloneCalendar = (GregorianCalendar) calendar.clone();
		cloneCalendar.add(calendar.HOUR, assigneeTurnedAroundTimeHour);

		int START_DATE = calendar.get(Calendar.DATE);
		int END_DATE = cloneCalendar.get(Calendar.DATE);
		GregorianCalendar dateCalendar = new GregorianCalendar();
		
		for(int i = START_DATE; i<= END_DATE; i++) {

			dateCalendar.set(Calendar.DATE, i);
			
			int DAY_OF_WEEK = dateCalendar.get(Calendar.DAY_OF_WEEK);
			
			if(DAY_OF_WEEK == Calendar.SATURDAY && !workingSaturday) {	
			
				assigneeTurnedAroundTimeHour += 24;
			}
			
			if(DAY_OF_WEEK == Calendar.SUNDAY) {
				
				assigneeTurnedAroundTimeHour += 24;
			}
			
			logger.info(dateCalendar.getTime());
		}

		logger.info(assigneeTurnedAroundTimeHour);
		return assigneeTurnedAroundTimeHour;
	}
	
	
	private void setHolidayModelTAT(Calendar holidayDate, Calendar calendar, Boolean is24HourTAT) {
						
		if(is24HourTAT) {
			
			List<HolidayModel> listHoliday = holidayManager.getHolidayModelByRange(holidayDate, calendar);
						
			GregorianCalendar updatedCalendar = null;
						
			for(HolidayModel holidayModel : listHoliday) {
				
				if(!isHolidayInWeekEnd(holidayModel)) {

					updatedCalendar = (GregorianCalendar) calendar.clone();

					updatedCalendar.add(calendar.HOUR, (24));
					setHolidayModelTAT(null, updatedCalendar, Boolean.FALSE);	
				}
				
				logger.info(calendar.getTime());				
			}			
			
			if(updatedCalendar != null) {
				calendar.set(Calendar.DATE, updatedCalendar.get(Calendar.DATE));
			}
			
		} else {
			
			HolidayModel holidayModel = holidayManager.getHolidayModelByDate(calendar.getTime());	
			
			if(holidayModel != null && !isHolidayInWeekEnd(holidayModel)) {

				calendar.add(calendar.HOUR, (24));
				logger.info(calendar.getTime());
				setHolidayModelTAT(null, calendar, Boolean.FALSE);	
			}
		}
		
		logger.info(calendar.getTime());
	}
	
	
	
	private Boolean isHolidayInWeekEnd(HolidayModel holidayModel) {
		
		Boolean isValidHoliday = Boolean.FALSE;
		
		Date holidayDate = holidayModel.getHolidayDate();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setGregorianChange(holidayDate);
		int DAY_OF_WEEK = calendar.get(GregorianCalendar.DAY_OF_WEEK);

		if(DAY_OF_WEEK == GregorianCalendar.SATURDAY && !workingSaturday) {
			isValidHoliday = Boolean.TRUE;
		}
		
		if(DAY_OF_WEEK == GregorianCalendar.SUNDAY) {
			isValidHoliday = Boolean.TRUE;
		}
		
		return isValidHoliday;
	}
	
	
	public void updateComplaintStatus(Map<Long, String> updateStatusMap, Map<Long, String> updateEscStatusMap, List<ComplaintReportModel> complaintReportModelList) {
		
		complaintDAO.updateComplaintStatus(updateStatusMap, updateEscStatusMap, complaintReportModelList);
	}
	
	public static Boolean isValidString(String string) {
		
		if(AllPayWebConstant.NULL_OBJECT.getValue() != string && 
				!AllPayWebConstant.BLANK_SPACE.getValue().equals(string)) {
			
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
	
	
	public void setComplaintDAO(ComplaintDAO complaintDAO) {
		this.complaintDAO = complaintDAO;
	}
	public void setComplaintHistoryDAO(ComplaintHistoryDAO complaintHistoryDAO) {
		this.complaintHistoryDAO = complaintHistoryDAO;
	}	
	public void setComplaintSubcategoryDAO(ComplaintSubcategoryDAO complaintSubcategoryDAO) {
		this.complaintSubcategoryDAO = complaintSubcategoryDAO;
	}

	public void setGenDao(GenericDAO genDao) {
		this.genDao = genDao;
	}
	public void setComplaintListViewDAO(ComplaintListViewDAO complaintListViewDAO) {
		this.complaintListViewDAO = complaintListViewDAO;
	}
	public void setComplaintHistoryViewDAO(ComplaintHistoryViewDAO complaintHistoryViewDAO) {
		this.complaintHistoryViewDAO = complaintHistoryViewDAO;
	}
	public void setJmsProducer(JmsProducer jmsProducer) {
		this.jmsProducer = jmsProducer;
	}
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}	
	
	private String generateCode(){
		DateFormat df = new SimpleDateFormat("yyMMdd");		
	    String alphaChars = RandomUtils.generateRandom(4, false, true) ;
	    return df.format(System.currentTimeMillis()) + alphaChars ;
	}

	private String getUniqueComplaintCode(){
		ComplaintModel complaintModel = new ComplaintModel();
 	   	ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
 	   	exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
 	   	
 	   	boolean isNotUnique = true;
 	   	while (isNotUnique){
 	   		complaintModel.setComplaintCode(generateCode());
		    try{
		    	Integer count = this.complaintDAO.countByExample(complaintModel, exampleConfigHolderModel);
			    if(count==0){
			    	isNotUnique = false;
			    }
		    }catch(Exception ex){
		    	ex.printStackTrace();
		    }
 	   	}
	    return complaintModel.getComplaintCode();
	}
	
	public Map getComplaintParamValueMap(Long complaintId) {
		
		return complaintParamValueDAO.getComplaintParamValueMap(complaintId);
	}
	
	@Override
	public CustomList<ComplaintListViewModel> searchComplaintList(SearchBaseWrapper wrapper ) throws FrameworkCheckedException {
		ComplaintListViewModel model = (ComplaintListViewModel)wrapper.getBasePersistableModel();
		CustomList<ComplaintListViewModel> list = complaintListViewDAO.findByExample( model, wrapper.getPagingHelperModel(),
				wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
		return list;
	}
	
	@Override
	public CustomList<ComplaintReportModel> searchComplaintReportList(SearchBaseWrapper wrapper ) throws FrameworkCheckedException {
		ComplaintReportModel model = (ComplaintReportModel)wrapper.getBasePersistableModel();
		CustomList<ComplaintReportModel> list = complaintReportDAO.findByExample( model, wrapper.getPagingHelperModel(),
				wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
		return list;
	}

	@Override
	public CustomList<ComplaintHistoryViewModel> searchComplaintHistory(SearchBaseWrapper wrapper ) throws FrameworkCheckedException {
		ComplaintHistoryViewModel model = (ComplaintHistoryViewModel)wrapper.getBasePersistableModel();
		CustomList<ComplaintHistoryViewModel> list = complaintHistoryViewDAO.findByExample( model, wrapper.getPagingHelperModel(),
				wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
		return list;
	}

	public List<LabelValueBean> loadAssigneeList() throws FrameworkCheckedException{
		return this.complaintSubcategoryDAO.loadAssigneeList();
	}
	
	public List<LabelValueBean> loadL0AssigneeList() throws FrameworkCheckedException{
		return this.complaintSubcategoryDAO.loadl0AssigneeList();
	}
	
	public List<LabelValueBean> loadL1AssigneeList() throws FrameworkCheckedException{
		return this.complaintSubcategoryDAO.loadl1AssigneeList();
	}
	
	public List<LabelValueBean> loadL2AssigneeList() throws FrameworkCheckedException{
		return this.complaintSubcategoryDAO.loadl2AssigneeList();
	}
	
	public List<LabelValueBean> loadL3AssigneeList() throws FrameworkCheckedException{
		return this.complaintSubcategoryDAO.loadl3AssigneeList();
	}
	
	public void setHolidayManager(HolidayManager holidayManager) {
		this.holidayManager = holidayManager;
	}
	public void setWorkingSaturday(Boolean workingSaturday) {
		this.workingSaturday = workingSaturday;
	}
	public void setComplaintCategoryDAO(ComplaintCategoryDAO complaintCategoryDAO) {
		this.complaintCategoryDAO = complaintCategoryDAO;
	}
	public void setComplaintParamValueDAO(ComplaintParamValueDAO complaintParamValueDAO) {
		this.complaintParamValueDAO = complaintParamValueDAO;
	}
	public void setSmsSender(SmsSender smsSender){
		this.smsSender = smsSender;
	}
	public void setEscalationLevel1Emails(String escalationLevel1Emails) {
		this.escalationLevel1Emails = escalationLevel1Emails;
	}
	public void setEscalationLevel2Emails(String escalationLevel2Emails) {
		this.escalationLevel2Emails = escalationLevel2Emails;
	}
	public void setEscalationLevel3Emails(String escalationLevel3Emails) {
		this.escalationLevel3Emails = escalationLevel3Emails;
	}
	public void setComplaintReportDAO(ComplaintReportDAO complaintReportDAO) {
		this.complaintReportDAO = complaintReportDAO;
	}
	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}
	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}
	
	@Override
	public String getAssigneeName(Long appUserId) throws FrameworkCheckedException{
		return complaintSubcategoryDAO.getAssigneeName(appUserId);
	}	
	
	@Override
	public void updateComplaint(ComplaintDetailVO complaintDetailVO) throws FrameworkCheckedException {
		ComplaintModel _complaintModel = complaintDetailVO.getComplaintModel();

		ComplaintModel complaintModel = complaintDAO.findByPrimaryKey(_complaintModel.getComplaintId());
		ComplaintReportModel reportModel = this.loadComplaintReportByComplaintId(_complaintModel.getComplaintId());
		
		if(_complaintModel.getStatus().equals("Escalate")){
			// manual escalation scenario
			if(complaintDetailVO.getEscalateTo() != null){
				ComplaintSubcategoryModel subcategoryModel = this.getComplaintSubcategoryModel(complaintModel.getComplaintSubcategoryId());
				
				complaintModel.setEscalationStatus("Level "+complaintDetailVO.getEscalateTo());
				complaintModel.setUpdatedOn(new Date());
				complaintModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				complaintDAO.saveOrUpdate(complaintModel);
				
				//Escalate Current Assignee
				ComplaintHistoryModel currentHistoryModel = this.loadCurrentComplaintHistory(complaintModel.getComplaintId());
				currentHistoryModel.setStatus(ComplaintStatusEnum.ESCALATED.getValue());
				currentHistoryModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				currentHistoryModel.setUpdatedOn(new Date());
				currentHistoryModel.setRemarks(_complaintModel.getRemarks());
				complaintHistoryDAO.saveOrUpdate(currentHistoryModel);
				
				reportModel.setEscalationStatus("Level "+complaintDetailVO.getEscalateTo());
				reportModel.setUpdatedOn(new Date());
				reportModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

				Map<String, Object> parameterMap = getTurnedAroundTime(complaintDetailVO.getEscalateTo().intValue(), _complaintModel.getComplaintId());

				if(complaintDetailVO.getEscalateTo().longValue() == 1){
					String assigneeName = this.getAssigneeName(subcategoryModel.getLevel1Assignee());
					Long assigneeId = subcategoryModel.getLevel1Assignee();
					Date tatEndTime = this.calcTurnedAroundTime(subcategoryModel.getLevel1AssigneeTat().intValue(), new GregorianCalendar().getInstance());
					reportModel.setLevel1AssigneeId(assigneeId);
					reportModel.setLevel1AssigneeName(assigneeName);
					reportModel.setLevel1TATEndTime(tatEndTime);
					updateHistoryAndReport(reportModel, assigneeId, assigneeName, tatEndTime,1, parameterMap);
				}else if(complaintDetailVO.getEscalateTo().longValue() == 2){
					String assigneeName = this.getAssigneeName(subcategoryModel.getLevel2Assignee());
					Long assigneeId = subcategoryModel.getLevel2Assignee();
					Date tatEndTime = this.calcTurnedAroundTime(subcategoryModel.getLevel2AssigneeTat().intValue(), new GregorianCalendar().getInstance());
					reportModel.setLevel2AssigneeId(assigneeId);
					reportModel.setLevel2AssigneeName(assigneeName);
					reportModel.setLevel2TATEndTime(tatEndTime);
					updateHistoryAndReport(reportModel, assigneeId, assigneeName, tatEndTime,2, parameterMap);
				}else if(complaintDetailVO.getEscalateTo().longValue() == 3){
					String assigneeName = this.getAssigneeName(subcategoryModel.getLevel3Assignee());
					Long assigneeId = subcategoryModel.getLevel3Assignee();
					Date tatEndTime = this.calcTurnedAroundTime(subcategoryModel.getLevel3AssigneeTat().intValue(), new GregorianCalendar().getInstance());
					reportModel.setLevel3AssigneeId(assigneeId);
					reportModel.setLevel3AssigneeName(assigneeName);
					reportModel.setLevel3TATEndTime(tatEndTime);
					updateHistoryAndReport(reportModel, assigneeId, assigneeName, tatEndTime,3, parameterMap);
				}
			}
		}else{
			// resolve/invalid scenario
			complaintModel.setStatus(_complaintModel.getStatus());
			complaintModel.setRemarks(_complaintModel.getRemarks());
			complaintModel.setUpdatedOn(new Date());
			complaintModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			complaintModel.setClosedBy(UserUtils.getCurrentUser().getAppUserId());
			complaintModel.setClosedOn(new Date());
			
			reportModel.setStatus(_complaintModel.getStatus());
			reportModel.setRemarks(_complaintModel.getRemarks());
			reportModel.setUpdatedOn(new Date());
			reportModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			reportModel.setClosedBy(UserUtils.getCurrentUser().getAppUserId());
			reportModel.setClosedByName(UserUtils.getCurrentUser().getFirstName() + " " + UserUtils.getCurrentUser().getLastName());
			reportModel.setClosedOn(new Date());
			this.saveUpdate(complaintModel,reportModel);
			
			if(ComplaintsModuleConstants.STATUS_RESOLVED.equals(_complaintModel.getStatus()) || 
					ComplaintsModuleConstants.STATUS_DECLINED.equals(_complaintModel.getStatus()) ){
				
				String mobileno = reportModel.getMobileNo();
				Object[] args = { complaintModel.getComplaintCode()};
				String msgKey = "";
				if(ComplaintsModuleConstants.STATUS_RESOLVED.equals(_complaintModel.getStatus())){
					msgKey = "complaint.resolve.sms";
				}else if(ComplaintsModuleConstants.STATUS_DECLINED.equals(_complaintModel.getStatus())){
					msgKey = "complaint.declined.sms";
				}
				String messageString = MessageUtil.getMessage(msgKey, args);
				SmsMessage smsMessage = new SmsMessage(mobileno, messageString);
				smsSender.send(smsMessage);
			}
			
		}
		
		
	}
	
	private void updateHistoryAndReport(ComplaintReportModel reportModel,Long assigneeId,String assigneeName, Date tatEndTime, Integer displayOrder, Map<String, Object> parameterMap) throws FrameworkCheckedException {
		
		reportModel.setCurrentAssigneeId(assigneeId);
		reportModel.setCurrentAssigneeName(assigneeName);
		
		ComplaintHistoryModel historyModel = new ComplaintHistoryModel();
		historyModel.setComplaintId(reportModel.getComplaintId());
		historyModel.setAppUserId(assigneeId);
		historyModel.setAssignedOn(new Date());
		historyModel.setTatEndTime(tatEndTime);
		historyModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		historyModel.setCreatedOn(new Date());
		historyModel.setStatus(ComplaintsModuleConstants.STATUS_ASSIGNED);
		historyModel.setDisplayOrder(displayOrder);
		
		complaintReportDAO.saveOrUpdate(reportModel);
		complaintHistoryDAO.saveOrUpdate(historyModel);
	}

	private ComplaintHistoryModel loadCurrentComplaintHistory(Long complaintId) throws FrameworkCheckedException {
		ComplaintHistoryModel complaintHistoryModel = new ComplaintHistoryModel();
		complaintHistoryModel.setComplaintId(complaintId);
		complaintHistoryModel.setStatus(ComplaintsModuleConstants.STATUS_ASSIGNED);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		CustomList<ComplaintHistoryModel> list = complaintHistoryDAO.findByExample(complaintHistoryModel,null,null,exampleHolder);
		if(list != null && list.getResultsetList().size() > 0){
			complaintHistoryModel = list.getResultsetList().get(0);
		}
		return complaintHistoryModel;
	}

	private String getAppUserTypeString(Long appUserTypeId){
		String userType = "";
		if(appUserTypeId.longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()){
			userType = "Customer";
		}else if(appUserTypeId.longValue() == UserTypeConstantsInterface.RETAILER.longValue()){
			userType = "Agent";
		}else if(appUserTypeId.longValue() == UserTypeConstantsInterface.WALKIN_CUSTOMER.longValue()){
			userType = "Walk-in Customer";
		}
		return userType;
	}

	@Override
	public List<ComplaintReportModel> searchComplaintByConsumerNo(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		return complaintReportDAO.searchComplaintByConsumerNo(wrapper);
	}

	@Override
	public List<ComplaintReportModel> searchUserComplaintHistory(Long appUserId) throws FrameworkCheckedException {
		return complaintReportDAO.searchUserComplaintHistory(appUserId);
	}

	@Override
	public BaseWrapper saveComplaintSubcategory(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		ComplaintSubcategoryModel complaintSubcategoryModel = (ComplaintSubcategoryModel)baseWrapper.getBasePersistableModel();	
		Short totalTat = (short) (complaintSubcategoryModel.getLevel0AssigneeTat() + complaintSubcategoryModel.getLevel1AssigneeTat() + complaintSubcategoryModel.getLevel2AssigneeTat() + complaintSubcategoryModel.getLevel3AssigneeTat());
		complaintSubcategoryModel.setTotalTat(totalTat);
		complaintSubcategoryModel = this.complaintSubcategoryDAO.saveOrUpdate(complaintSubcategoryModel) ;		
		baseWrapper.setBasePersistableModel(complaintSubcategoryModel);
		actionLogModel.setCustomField1(complaintSubcategoryModel.getComplaintSubcategoryId().toString());
		actionLogModel.setCustomField11(complaintSubcategoryModel.getName());
	    this.actionLogManager.completeActionLog(actionLogModel);
		return baseWrapper;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LabelValueBean> loadBankUserList() throws FrameworkCheckedException {
		return complaintSubcategoryDAO.loadBankUsers();
	}

	@Override
	public List<ComplaintSubcategoryViewModel> searchComplaintSubcategoryList(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		
		ComplaintSubcategoryViewModel model = (ComplaintSubcategoryViewModel)wrapper.getBasePersistableModel();
		
		return complaintSubcategoryViewDAO.searchComplaintNature(model);
	}

	@Override
	public BaseWrapper loadComplaintSubcategory(BaseWrapper wrapper) throws FrameworkCheckedException {
		ComplaintSubcategoryModel model = complaintSubcategoryDAO.findByPrimaryKey(wrapper.getBasePersistableModel().getPrimaryKey());
		wrapper.setBasePersistableModel(model);
		return wrapper;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setComplaintSubcategoryViewDAO(
			ComplaintSubcategoryViewDAO complaintSubcategoryViewDAO) {
		this.complaintSubcategoryViewDAO = complaintSubcategoryViewDAO;
	}
}