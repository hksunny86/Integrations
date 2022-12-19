/**
 * 
 */
package com.inov8.microbank.webapp.action.allpayweb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.webapp.action.PopupController;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;

/**
 * @author kashefbasher
 *
 */

	public class BillPaymentDispatcherController extends PopupController {

		@Override
		protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

			String PID = request.getParameter("PID");
			String SID = request.getParameter("SID");
						
 			if(PID.contains(" ")) {
 				PID = PID.replace(" ", "+");
 			}
 			
 			String productId = AllPayWebResponseDataPopulator.decrypt(PID);
 			Long serviceId = Long.valueOf(SID);
 			
			AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
 			AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
			requestWrapper.addParameter("PID", PID);
			requestWrapper.addParameter("SID", SID);
			requestWrapper.addParameter("PNAME", request.getParameter("PNAME"));
			
			String forwardPage = null;

			if(ServiceTypeConstantsInterface.SERVICE_TYPE_UTILITY_BILL_PAYMENT.longValue() == serviceId.longValue()) {

				forwardPage = "/billpaymentform.aw";

			} else if(ServiceTypeConstantsInterface.SERVICE_TYPE_DONATION_RETAILER_PAYMENT.longValue() == serviceId.longValue()) {

				forwardPage = "/donationPayment.aw";
				
			} else if(ServiceTypeConstantsInterface.SERVICE_TYPE_INTERNET_BILL_PAYMENT.longValue() == serviceId.longValue()) {

				forwardPage = "/ubpServiceStepOne.aw";
				
			} else if(ServiceTypeConstantsInterface.SERVICE_TYPE_CREDIT_CARD_RETAILER_PAYMENT.longValue() == serviceId.longValue()) {

				forwardPage = "/creditCardPaymentStepOne.aw";
			}
			
			getServletContext().getRequestDispatcher(forwardPage).forward(request, response);
			
			return null;
		}
	}