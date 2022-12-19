package com.inov8.microbank.webapp.action.ajax;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.server.facade.StakeholderFacade;

public class SettlementAccountRefDataController extends AjaxController {

	private StakeholderFacade stakeholderFacade;
	
	@Override
	public String getResponseContent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Long accountType	=	ServletRequestUtils.getLongParameter(request, "accountTypeId", -1);
		
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();
		list.add(new LabelValueBean("--All--", "-2"));
		if(accountType!=-1)
		{
			List<Object[]> resultList = stakeholderFacade.loadOfSettlementAccounts(accountType);
			LabelValueBean labelValueBean;
			for (Object[] item : resultList) {

				labelValueBean = new LabelValueBean(item[2] + " - " + item[1],String.valueOf(item[0]));
				list.add(labelValueBean);
			}
		}

		AjaxXmlBuilder	ajaxXmlBuilder=new AjaxXmlBuilder();
		return ajaxXmlBuilder.addItems(list, "label", "value").toString();
	}

	public void setStakeholderFacade(StakeholderFacade stakeholderFacade) {
		this.stakeholderFacade = stakeholderFacade;
	}
}