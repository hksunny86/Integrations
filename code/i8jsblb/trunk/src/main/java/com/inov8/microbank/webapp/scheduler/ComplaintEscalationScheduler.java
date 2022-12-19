package com.inov8.microbank.webapp.scheduler;

import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.CATEGORY;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.COMPALINT_EXPECTED_TAT_DATE;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.COMPLAINT_CODE;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.COMPLAINT_DESC;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL_ASSIGNEE_NAME_PARAM;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL_ASSIGNEE_PARAM;
import static com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO.LEVEL_ASSIGNEE_TAT_PARAM;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.ComplaintHistoryModel;
import com.inov8.microbank.common.model.ComplaintReportModel;
import com.inov8.microbank.common.model.portal.complaint.ComplaintHistoryVO;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;
import com.inov8.microbank.server.service.complaintmodule.ComplaintStatusEnum;

/**
 * @author Kashif Bashir
 */

public class ComplaintEscalationScheduler extends QuartzJobBean {

	private final static Log logger = LogFactory.getLog(ComplaintEscalationScheduler.class);
	private final SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("EEE.MMMMM.dd.yyyy.hh:mm:ss.aaa");
	private Boolean enableEmail = Boolean.FALSE;
	
	private ComplaintManager complaintManager;

	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		long start = System.currentTimeMillis();

		logger.info("\n\n\n@---------------------------------------------------------------------------------------------------------------");
		logger.info("@                           			Started CMS.Complaint.Esc. Scheduler");
		logger.info("@---------------------------------------------------------------------------------------------------------------");	
		logger.info("@_Complaint.Esc. Scheduler Started at "+ simpleDateFormat.format(new java.util.Date()));
							
		try {
				
			process();
				
		} catch (FrameworkCheckedException e) {
			logger.error(e.getMessage());
		}
		
		logger.info("@_C.Esc. Scheduler Took : "+ ((System.currentTimeMillis() - start)/1000) + " Second(s).");		
		logger.info("\n                           			Ended CMS Complaint.Esc. Scheduler");
		logger.info("@---------------------------------------------------------------------------------------------------------------\n\n\n");	
		
	}
	
	
	private void process() throws FrameworkCheckedException {

		List<ComplaintReportModel> complaintReportModelList = new ArrayList<ComplaintReportModel>(0);
		List<ComplaintHistoryModel> complaintHistoryModelList = new ArrayList<ComplaintHistoryModel>(0);
		List<ComplaintHistoryModel> complaintHistoryModelEmailList = new ArrayList<ComplaintHistoryModel>(0);
		Map<Long, String> updateStatusMap = new HashMap<Long, String>(0);
		Map<Long, String> updateEscStatusMap = new HashMap<Long, String>(0);
		
		ComplaintHistoryModel complaintHistoryModel = new ComplaintHistoryModel();
		complaintHistoryModel.setStatus(ComplaintStatusEnum.ASSIGNED.getValue());
		complaintHistoryModel.setTatEndTime(new Date());
		
		List<ComplaintHistoryModel> complaintHistoryList = complaintManager.getComplaintHistoryModelList(complaintHistoryModel);
		
		for(ComplaintHistoryModel sourceObject : complaintHistoryList) {
			
			Integer displayOrder = sourceObject.getDisplayOrder().intValue();
			
			if(displayOrder <= 2) {
				
				displayOrder += 1; 
			}
			
			Map<String, Object> parameterMap = complaintManager.getTurnedAroundTime(displayOrder, sourceObject.getComplaintId());

			Date expectedTatEndDate = (Date) parameterMap.get(COMPALINT_EXPECTED_TAT_DATE);
			
			Boolean overDueCase = Boolean.FALSE;
			
			if(new Date().after(expectedTatEndDate)) {
				overDueCase = Boolean.TRUE;
				updateStatusMap.put(sourceObject.getComplaintId(), ComplaintStatusEnum.OVERDUE.getValue());
				updateEscStatusMap.put(sourceObject.getComplaintId(), ComplaintStatusEnum.OVERDUE.getValue());
//		OLD		updateEscStatusMap.put(sourceObject.getComplaintId(), getEscStatus(sourceObject));
			}			
			
			ComplaintHistoryModel _complaintHistoryModel = null;
			
			if(sourceObject.getDisplayOrder().intValue() <= 2 || overDueCase) {
				
				_complaintHistoryModel = createComplaintHistoryModel(sourceObject, parameterMap, overDueCase);

				if(_complaintHistoryModel != null) {
					
					_complaintHistoryModel.setUpdatedBy(1L);
					complaintHistoryModelList.add(_complaintHistoryModel);		
					
					ComplaintReportModel complaintReportModel = createComplaintReportModel(parameterMap, _complaintHistoryModel);
                    
					complaintReportModelList.add(complaintReportModel);
					
					if(overDueCase) {
						
						complaintReportModel.setEscalationStatus(ComplaintStatusEnum.OVERDUE.getValue());
						
					} else {
						// already marked as OVERDUE @107 so update when not overDueCase
						updateEscStatusMap.put(_complaintHistoryModel.getComplaintId(), getEscStatus(_complaintHistoryModel));
					}
					
					complaintHistoryModelEmailList.add(_complaintHistoryModel);
				}						
				sourceObject.setUpdatedBy(null);
				complaintHistoryModelList.add(sourceObject);
			}
		}
		
		complaintManager.saveUpdateAllHistory(complaintHistoryModelList);
		
		complaintManager.updateComplaintStatus(updateStatusMap, updateEscStatusMap, complaintReportModelList);
		
		if(enableEmail) {
			
			complaintManager.sendEscalationEmail(complaintHistoryModelEmailList);
		}
	}
	
	
	/**
	 * @param sourceObject
	 * @param parameterMap
	 * @return
	 * @throws FrameworkCheckedException
	 */
	
	private ComplaintHistoryModel createComplaintHistoryModel(ComplaintHistoryModel sourceObject, Map<String, Object> parameterMap, Boolean overDueCase) throws FrameworkCheckedException {

		ComplaintHistoryVO complaintHistoryVO = new ComplaintHistoryVO();
		complaintHistoryVO.setComplaintCategory((String)parameterMap.get(CATEGORY));
		complaintHistoryVO.setTitle((String)parameterMap.get(COMPLAINT_DESC));
		complaintHistoryVO.setComplaintCode((String)parameterMap.get(COMPLAINT_CODE));

		ComplaintHistoryModel copyObject = null;
		
		try {

			if(!overDueCase) {
				
				Integer assigneeTurnedAroundTime = ((BigDecimal)parameterMap.get(LEVEL_ASSIGNEE_TAT_PARAM)).intValue();			
				Long assigneeAppUserId = (Long) parameterMap.get(LEVEL_ASSIGNEE_PARAM);
				
				Integer displayOrder = sourceObject.getDisplayOrder().intValue();
				displayOrder = (displayOrder <= 2) ? (displayOrder += 1) : displayOrder;
			
				Calendar tatEndTimeCalendar = GregorianCalendar.getInstance();
				tatEndTimeCalendar.setTime(sourceObject.getTatEndTime());
//				Date turnedAroundDateTime = complaintManager.calcTurnedAroundTime(assigneeTurnedAroundTime, tatEndTimeCalendar);
				Date turnedAroundDateTime = complaintManager.calcExpectedTat(assigneeTurnedAroundTime, tatEndTimeCalendar);
	
				logger.info("@_Current TAT : ---> "+simpleDateFormat.format(new Date(sourceObject.getTatEndTime().getTime())));
				logger.info("@_Next TAT    : ---> "+simpleDateFormat.format(turnedAroundDateTime));
				logger.info("@_TAT Assigned   : ---> "+ assigneeTurnedAroundTime);
				
				copyObject = new ComplaintHistoryModel();
				
				BeanUtils.copyProperties(copyObject, sourceObject);
	
				copyObject.setComplaintHistoryId(null);
				copyObject.setAssignedOn(new Date());
				copyObject.setCreatedOn(new Date());
				copyObject.setUpdatedOn(new Date());
				copyObject.setTatEndTime(turnedAroundDateTime);
				copyObject.setStatus(ComplaintStatusEnum.ASSIGNED.getValue());
				copyObject.setDisplayOrder(displayOrder);
					
				copyObject.setComplaintHistoryVO(complaintHistoryVO);					
				copyObject.setAppUserId(assigneeAppUserId);		

				sourceObject.setStatus(ComplaintStatusEnum.ESCALATED.getValue());
			}
		
			if(overDueCase) {

				sourceObject.setComplaintHistoryVO(complaintHistoryVO);	
				sourceObject.setStatus(ComplaintStatusEnum.OVERDUE.getValue());
			}
				
			
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		} catch (FrameworkCheckedException e) {
			logger.error(e);
		}
		
		return (overDueCase ? sourceObject : copyObject);
	}
	
		
	/**
	 * @param parameterMap
	 * @param complaintHistoryModel
	 * @return
	 */
	private ComplaintReportModel createComplaintReportModel(Map<String, Object> parameterMap, ComplaintHistoryModel complaintHistoryModel) {
		
		Long assigneeAppUserId = ((Long) parameterMap.get(LEVEL_ASSIGNEE_PARAM)).longValue();
		String assigneeAppUserName = (String) parameterMap.get(LEVEL_ASSIGNEE_NAME_PARAM);
		
		ComplaintReportModel complaintReportModel = new ComplaintReportModel();
		complaintReportModel.setEscalationStatus(getEscStatus(complaintHistoryModel));
		complaintReportModel.setStatus(complaintHistoryModel.getStatus());
		complaintReportModel.setDisplayOrder(complaintHistoryModel.getDisplayOrder());

		complaintReportModel.setLevelAssigneeId(assigneeAppUserId);
		complaintReportModel.setLevelAssigneeName(assigneeAppUserName);
		complaintReportModel.setCurrentAssigneeId(assigneeAppUserId);
		complaintReportModel.setCurrentAssigneeName(assigneeAppUserName);
		complaintReportModel.setLevelTATEndTime(complaintHistoryModel.getTatEndTime());
		complaintReportModel.setComplaintId(complaintHistoryModel.getComplaintId());
		
		return complaintReportModel;
	}
	
	
	/**
	 * @param complaintHistoryModel
	 * @return
	 */
	private String getEscStatus(ComplaintHistoryModel complaintHistoryModel) {
		
		String escStatus = null;
		
		switch (complaintHistoryModel.getDisplayOrder().intValue()) {
		
			case 1 :{
				escStatus = ComplaintsModuleConstants.ESC_STATUS_LEVEL_1;
				break;
			}		
			case 2 :{
				escStatus = ComplaintsModuleConstants.ESC_STATUS_LEVEL_2;
				break;
			}		
			case 3 :{
				escStatus = ComplaintsModuleConstants.ESC_STATUS_LEVEL_3;
				break;
			}
		}
		
		return escStatus;
	}
	
//	Dependancies
	
	public void setEnableEmail(Boolean enableEmail) {
		this.enableEmail = enableEmail;
	}

	public void setComplaintManager(ComplaintManager complaintManager) {
		this.complaintManager = complaintManager;
	}
}