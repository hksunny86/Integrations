package com.inov8.microbank.common.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.retailermodule.RetailerContactSearchViewModel;
import com.inov8.microbank.common.vo.RetailerContactDetailVO;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;

public class LocatorCacheUtil {

	@Autowired
	private RetailerContactManager retailerContactManager;
	private static List<RetailerContactDetailVO> rcsvModels = null;

	protected final Log logger = LogFactory.getLog(getClass());


	public void getRefData () {
		try {
			rcsvModels = retailerContactManager.findRetailerContactModelList();
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
	}
	
	public static List<RetailerContactDetailVO> getRetailerModels (Long retailerId, Long categoryKey) {
		System.out.println("************************ Getting Agent Models ************************");
		if (retailerId!=null || categoryKey!=null) {/*
			List<RetailerContactSearchViewModel> rcsvModelsLocal = new ArrayList<>();
			for (RetailerContactSearchViewModel retailerContactSearchViewModel : rcsvModels) {
				if (retailerId != null && retailerContactSearchViewModel.getRetailerId().equals(retailerId)) {
					rcsvModelsLocal.add(retailerContactSearchViewModel);
				} else if (categoryKey != null && retailerContactSearchViewModel.getNatureOfBusinessId()!=null &&
						retailerContactSearchViewModel.getNatureOfBusinessId().equals(categoryKey)) {
					rcsvModelsLocal.add(retailerContactSearchViewModel);
				}
			}
			return rcsvModelsLocal;
		*/}
		return rcsvModels;
	}
	
	public static void setRcsvModels(List<RetailerContactDetailVO> rcsvModels) {
		LocatorCacheUtil.rcsvModels = rcsvModels;
	}

	public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}
}
