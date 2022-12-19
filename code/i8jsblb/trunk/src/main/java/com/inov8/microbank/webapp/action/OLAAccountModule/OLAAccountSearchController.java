package com.inov8.microbank.webapp.action.OLAAccountModule;

import static com.inov8.microbank.common.util.WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.service.olamodule.OLAManager;
import com.inov8.ola.integration.vo.OLAVO;


public class OLAAccountSearchController extends BaseSearchController {

	private final String REQUEST_PARAM = "olaAccountList";
	
	private OLAManager olaManager;

	protected OLAAccountSearchController() {
		super.setFilterSearchCommandClass(OLAVO.class);
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, 
			HttpServletRequest request, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {		
	
		String view = getSearchView();
		
		Integer listSize = 0;
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl ();
		
		OLAVO olavo= (OLAVO)object;
		
		List<OLAVO> olaList = null;
		
		olavo.setSortingOrderMap(sortingOrderMap);
		
		searchBaseWrapper.putObject(OLAVO.class.getName(), olavo);
		
		try {
			
			olaList = olaManager.makeAccount(searchBaseWrapper, request);
					
			if(olaList != null) {
				checkOLAVOList(olaList);
				listSize = olaList.size();
				sortByBalance(sortingOrderMap, olaList);
			}
			
		} catch(Exception e) {
			
			if(PHOENIX_SERVICE_DOWN_MSG.equalsIgnoreCase(e.getMessage())) {
				
				saveMessage(request, PHOENIX_SERVICE_DOWN_MSG);				
			}
		
			view = getSearchView(); 					
		}

		pagingHelperModel.setTotalRecordsCount(listSize);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		
		return new ModelAndView(view, REQUEST_PARAM, olaList);	
	}
	
	
	/**
	 * @param olaList
	 */
	private void checkOLAVOList(List<OLAVO> _olaList) {
		
		List<OLAVO> olaList = new CopyOnWriteArrayList<OLAVO>(_olaList);
		
		for(OLAVO olaVo : olaList) {
			
			String firstName = olaVo.getFirstName();
			String lastName = olaVo.getLastName();
			String cnic = olaVo.getCnic();
			String mobileNumber = olaVo.getMobileNumber();
			
			// set last name as blank if first name & last name are same
			if(firstName != null && lastName != null) {
				
				if(firstName.trim().equalsIgnoreCase(lastName.trim())) {
					olaVo.setLastName(null);
				}	
			} 
			
			// to display blank values against account having cnic = -1
//			if((cnic != null && cnic.equalsIgnoreCase("-1")) || 
//					(mobileNumber != null && mobileNumber.equalsIgnoreCase("-1"))) {
			//account to cash sundry / cash to cash sundry / commission recon settlement accounts have these values dummy. empty these values
			if("10000000002".equals(olaVo.getPayingAccNo())
					|| "10000000003".equals(olaVo.getPayingAccNo())
					|| "10000000001".equals(olaVo.getPayingAccNo())) {
				
				olaVo.setLastName(null);
				olaVo.setCnic(null);
				olaVo.setMobileNumber(null);
				olaVo.setDob(null);
			}
			
			// remove special account
			if("0000000001".equals(olaVo.getPayingAccNo())) {
				olaList.remove(olaVo);
			}
			
			if(olaVo.getCustomerAccountTypeId() != null && 
					olaVo.getCustomerAccountTypeId().longValue() == UserTypeConstantsInterface.WALKIN_CUSTOMER.longValue()) {
				olaList.remove(olaVo);
			}
		}	
		
		_olaList.clear();
		_olaList.addAll(olaList);
	}

	
	/**
	 * @param sortingOrderMap
	 * @param olaList
	 */
	private void sortByBalance(LinkedHashMap<String, SortingOrder> sortingOrderMap , List<OLAVO> olaList) {		

		if(sortingOrderMap.keySet().size() >0) {
			
			Object propertyName = sortingOrderMap.keySet().toArray()[0];
			
			if(OLAVOBalanceComparator.BALANCE.equalsIgnoreCase(String.valueOf(propertyName))) {
			
				Collections.sort(olaList, new OLAVOBalanceComparator());
				if(sortingOrderMap.get(propertyName).equals(SortingOrder.DESC)) {
					Collections.reverse(olaList);
				}
			}
		}		
	}
	
	
	public void setOlaManager(OLAManager olaManager) {
		this.olaManager = olaManager;
	}

}
	class OLAVOBalanceComparator implements Comparator<OLAVO> {

		protected final static String BALANCE = "balance"; 
		
		public int compare(OLAVO OLAVO_A, OLAVO OLAV_B) {
	
			Double OLAVO_A_Balance = OLAVO_A.getBalance();
			Double OLAV_B_Balance = OLAV_B.getBalance();
	
			if(OLAVO_A_Balance == null){
				OLAVO_A_Balance = 0d;
	        }
	        
	        if(OLAV_B_Balance == null){
	        	OLAV_B_Balance = 0d;
	        }
	        
			return OLAVO_A_Balance.compareTo(OLAV_B_Balance);
		}
	}