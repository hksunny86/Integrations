package com.inov8.microbank.webapp.action.portal.ibft;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.IBFTRetryAdviceModel;
import com.inov8.microbank.common.model.agentsegmentrestrictionmodule.AgentSegmentRestriction;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.messagingmodule.IBFTRetryAdviceDAO;
import com.inov8.microbank.server.dao.portal.agentsegmentrestrictionmodule.AgentSegmentRestrictionDAO;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

public class DeactivateAgentSegmentAjaxController extends AjaxController {

	/**
	 * @author Omar Butt
	 */
	
	@Autowired
	private AgentSegmentRestrictionDAO agentSegmentRestrictionDAO;
	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		
		String param = (String)request.getParameter("ibftRetryAdviceId");
		
		if(!StringUtil.isNullOrEmpty(param) && StringUtil.isNumeric(param)) {

			Long ibftRetryAdviceId = Long.parseLong(param);



			try{
					this.updateClearSafIBFTStatus(ibftRetryAdviceId);
				ajaxXmlBuilder.addItem("successMsg", "Agent Segment De-Active");
			
			}catch (FrameworkCheckedException ex) {
				ex.printStackTrace();
				if(ex.getMessage().equals("Already pushed to SAF, you cannot retry this advice.") 
						|| ex.getMessage().equals("Already Successful, you cannot retry this advice.")){

					ajaxXmlBuilder.addItem("mesg",ex.getMessage());
				}else{
					ajaxXmlBuilder.addItem("mesg","Some error has occured while pushing to SAF");
				}
			}
		}
		
		return ajaxXmlBuilder.toString();
	}


	public void setAgentSegmentRestrictionDAO(AgentSegmentRestrictionDAO agentSegmentRestrictionDAO) {
		this.agentSegmentRestrictionDAO = agentSegmentRestrictionDAO;
	}

	public void updateClearSafIBFTStatus(Long ibftRetryAdviceId) throws FrameworkCheckedException {
		logger.info("Start of Update Agent Segment De-Active... agent ID:" + ibftRetryAdviceId);

		AgentSegmentRestriction agentSegmentRestriction = new AgentSegmentRestriction();
		agentSegmentRestriction.setAgentSegmentExceptionId(ibftRetryAdviceId);
		agentSegmentRestriction = agentSegmentRestrictionDAO.findByPrimaryKey(ibftRetryAdviceId);



//		agentSegmentRestriction = agentSegmentRestrictionDAO.findByExample(agentSegmentRestriction.getSegmentId(),);

		LocalDate localDate=LocalDate.now();
		if (agentSegmentRestriction!=null&& agentSegmentRestriction.getAgentID()!=null) {
			logger.info("Going to updateIBFTStatus ibftRetryAdviceId:" + ibftRetryAdviceId);
			agentSegmentRestriction.setIsActive(false);
			agentSegmentRestriction.setUpdatedOn(new Date());
			agentSegmentRestriction.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId().toString());
			agentSegmentRestrictionDAO.saveOrUpdate(agentSegmentRestriction);
		} else {
			throw new FrameworkCheckedException("Unable to update De-Active of Agent Segment");
		}

		logger.info("End of update De-Active of Agent Segment");

	}

}