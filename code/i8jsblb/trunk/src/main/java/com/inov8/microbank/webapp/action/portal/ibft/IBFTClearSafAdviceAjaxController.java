package com.inov8.microbank.webapp.action.portal.ibft;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.microbank.common.model.IBFTRetryAdviceModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.messagingmodule.IBFTRetryAdviceDAO;
import com.inov8.microbank.server.facade.portal.transactionreversal.TransactionReversalFacade;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

public class IBFTClearSafAdviceAjaxController extends AjaxController {

	/**
	 * @author Omar Butt
	 */
	
	@Autowired
	private IBFTRetryAdviceDAO ibftRetryAdviceDAO;
	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		
		String param = (String)request.getParameter("ibftRetryAdviceId");
		
		if(!StringUtil.isNullOrEmpty(param) && StringUtil.isNumeric(param)) {

			Long ibftRetryAdviceId = Long.parseLong(param);
			LocalDate localDate=LocalDate.fromDateFields(new Date());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date	dt = formatter.parse(String.valueOf(localDate));


			try{
					this.updateClearSafIBFTStatus(ibftRetryAdviceId,dt, PortalConstants.IBFT_STATUS_CLEAR_SAF);
				ajaxXmlBuilder.addItem("mesg", "IBFT Credit Advice has been pushed to SAF Clear");
			
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


	public void setIbftRetryAdviceDAO(IBFTRetryAdviceDAO ibftRetryAdviceDAO) {
		this.ibftRetryAdviceDAO = ibftRetryAdviceDAO;
	}

	public void updateClearSafIBFTStatus(Long ibftRetryAdviceId, Date requestTime, String status) throws FrameworkCheckedException {
		logger.info("Start of updateIBFTStatus... ibftRetryAdviceId:" + ibftRetryAdviceId + " , requestTime:" + requestTime + " , new Status:" + status);

		IBFTRetryAdviceModel iBFTRetryAdviceModel = new IBFTRetryAdviceModel();
		iBFTRetryAdviceModel.setIbftRetryAdviceId(ibftRetryAdviceId);

		LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
		sortingOrderMap.put("ibftRetryAdviceId", SortingOrder.DESC);

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("requestTime", requestTime, requestTime);

		iBFTRetryAdviceModel = ibftRetryAdviceDAO.findByPrimaryKey(ibftRetryAdviceId);

		LocalDate localDate=LocalDate.now();
		if (iBFTRetryAdviceModel != null && iBFTRetryAdviceModel.getIbftRetryAdviceId() != null) {
			logger.info("Going to updateIBFTStatus ibftRetryAdviceId:" + ibftRetryAdviceId + " , requestTime:" + requestTime + " , IbftRetryAdviceId:" + iBFTRetryAdviceModel.getIbftRetryAdviceId());
			iBFTRetryAdviceModel.setStatus(status+"-"+localDate);
			iBFTRetryAdviceModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			iBFTRetryAdviceModel.setUpdatedOn(new Date());
			ibftRetryAdviceDAO.saveOrUpdate(iBFTRetryAdviceModel);
		} else {
			throw new FrameworkCheckedException("Unable to update status of iBFTRetryAdviceModel to (" + status + ") ibftRetryAdviceId:" + ibftRetryAdviceId + " , requestTime:" + requestTime);
		}

		logger.info("End of updateIBFTStatus... ibftRetryAdviceId:" + ibftRetryAdviceId + " , requestTime:" + requestTime + " , Status:" + status);

	}

}