package com.inov8.microbank.server.dao.handlermodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.retailermodule.HandlerSearchViewModel;
import com.inov8.microbank.server.dao.handlermodule.HandlerSearchViewDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>
 * Title: Microbank
 * </p>
 * 
 * <p>
 * Description: Backened application for POS terminal
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: Inov8 Ltd
 * </p>
 * 
 * @author Atif Hussain
 * @version 1.0
 * 
 */

public class HandlerSearchViewHibernateDAO extends
		BaseHibernateDAO<HandlerSearchViewModel, Long, HandlerSearchViewDAO>
		implements HandlerSearchViewDAO

{
	@Override
	public List<HandlerSearchViewModel> searchHandler(HandlerSearchViewModel handlerSearchViewModel) {

		DetachedCriteria criteria = DetachedCriteria
				.forClass(HandlerSearchViewModel.class);

		boolean criteriaAdded	=	false;
		
		if(handlerSearchViewModel.getOlaCustomerAccountTypeId()!=null)
		{
			criteria.add(Restrictions.eq("olaCustomerAccountTypeId", handlerSearchViewModel.getOlaCustomerAccountTypeId()));
			criteriaAdded	=	true;
		}
		if(handlerSearchViewModel.getHandlerId()!=null)
		{
			criteria.add(Restrictions.eq("userId", handlerSearchViewModel.getHandlerId().toString()));
			criteriaAdded	=	true;
		}
		if(handlerSearchViewModel.getAgentId()!=null && !handlerSearchViewModel.getAgentId().trim().equalsIgnoreCase(""))
		{
			criteria.add(Restrictions.eq("agentId", handlerSearchViewModel.getAgentId()));
			criteriaAdded	=	true;
		}
		if(handlerSearchViewModel.getAreaId()!=null)
		{
			criteria.add(Restrictions.eq("areaId", handlerSearchViewModel.getAreaId()));
			criteriaAdded	=	true;
		}
		if(handlerSearchViewModel.getAgentName()!=null && !handlerSearchViewModel.getAgentName().trim().equalsIgnoreCase(""))
		{
			criteria.add(Restrictions.eq("agentName", handlerSearchViewModel.getAgentName()));
			criteriaAdded	=	true;
		}
		if(handlerSearchViewModel.getContactNo()!=null && !handlerSearchViewModel.getContactNo().trim().equalsIgnoreCase(""))
		{
			criteria.add(Restrictions.eq("contactNo", handlerSearchViewModel.getContactNo()));
			criteriaAdded	=	true;
		}
		if(handlerSearchViewModel.getHandlerName()!=null && !handlerSearchViewModel.getHandlerName().trim().equalsIgnoreCase(""))
		{
			criteria.add(Restrictions.ilike("handlerName", "%" + handlerSearchViewModel.getHandlerName() + "%"));
			criteriaAdded	=	true;
		}
		if(handlerSearchViewModel.getCnic()!=null && !handlerSearchViewModel.getCnic().trim().equalsIgnoreCase(""))
		{
			criteria.add(Restrictions.ilike("cnic", "%" + handlerSearchViewModel.getCnic() + "%"));
			criteriaAdded	=	true;
		}
		if(criteriaAdded)
		{
			return this.getHibernateTemplate().findByCriteria(criteria);
		}
		return new ArrayList<HandlerSearchViewModel>();
	}
}
