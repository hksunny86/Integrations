package com.inov8.microbank.server.dao.portal.ola.hibernate;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.DateUtils;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.ola.CustomerBbStatementViewModel;
import com.inov8.microbank.server.dao.portal.ola.CustomerBbStatementViewDao;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.LinkedHashMap;
import java.util.List;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 20, 2013 7:40:48 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class CustomerBbStatementViewHibernateDao extends BaseHibernateDAO<CustomerBbStatementViewModel, Long, CustomerBbStatementViewDao> implements CustomerBbStatementViewDao
{

    @Override
    public List<CustomerBbStatementViewModel> searchBBStatementViewByPaymentModeId(CustomerBbStatementViewModel customerBbStatementViewModel, SearchBaseWrapper searchBaseWrapper)
    {
        Long paymentModeId = null;
        if(customerBbStatementViewModel.getPaymentModeId() != null)
            paymentModeId = customerBbStatementViewModel.getPaymentModeId();

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CustomerBbStatementViewModel.class);
        detachedCriteria.add(Restrictions.eq("appUserId",customerBbStatementViewModel.getAppUserId()));
        if(customerBbStatementViewModel.getAccountId() != null)
            detachedCriteria.add(Restrictions.eq("accountId",customerBbStatementViewModel.getAccountId()));

        Criterion criterionOne = null;

        if(paymentModeId != null && paymentModeId.equals(CustomerAccountTypeConstants.LEVEL_0))
        {
            Long[] paymentModeIds = {CustomerAccountTypeConstants.LEVEL_1,CustomerAccountTypeConstants.LEVEL_0};
            criterionOne =Restrictions.in("paymentModeId",paymentModeIds);
            detachedCriteria.add(criterionOne);
        }
        else if(paymentModeId != null) {
            detachedCriteria.add(Restrictions.eq("paymentModeId",paymentModeId));
        }

        if(searchBaseWrapper.getDateRangeHolderModel() != null)
            this.addDateRangeToCriteria(searchBaseWrapper.getDateRangeHolderModel(),detachedCriteria);

        if(searchBaseWrapper.getSortingOrderMap() != null)
            this.addSortingToCriteria(detachedCriteria,searchBaseWrapper.getSortingOrderMap());

        List<CustomerBbStatementViewModel> list = getHibernateTemplate().findByCriteria(detachedCriteria);

        return list;
    }

    private void addDateRangeToCriteria(DateRangeHolderModel dateRangeHolderModel, DetachedCriteria detachedCriteria)
    {
        if (dateRangeHolderModel != null && dateRangeHolderModel.getDatePropertyName() != null
                && (!("".equals(dateRangeHolderModel.getDatePropertyName()))))
        {
            if(dateRangeHolderModel.getFromDate() != null && dateRangeHolderModel.getToDate() != null)
            {
                detachedCriteria.add(Restrictions.between(dateRangeHolderModel.getDatePropertyName(), DateUtils.getDayStartDate(dateRangeHolderModel.getFromDate()), DateUtils.getDayEndDate(dateRangeHolderModel
                        .getToDate())));
            }
            else if(dateRangeHolderModel.getFromDate() != null && dateRangeHolderModel.getToDate() == null)
            {
                detachedCriteria.add(Restrictions.ge(dateRangeHolderModel.getDatePropertyName(), DateUtils.getDayStartDate(dateRangeHolderModel.getFromDate())));
            }

            else if(dateRangeHolderModel.getFromDate() == null && dateRangeHolderModel.getToDate() != null)
            {
                detachedCriteria.add(Restrictions.le(dateRangeHolderModel.getDatePropertyName(), DateUtils.getDayEndDate(dateRangeHolderModel
                        .getToDate())));
            }
        }
    }

    private void addSortingToCriteria(DetachedCriteria detachedCriteria, LinkedHashMap<String, SortingOrder> sortingOrderMap)
    {
        if (sortingOrderMap != null && sortingOrderMap.size() > 0)
        {
            for (String key : sortingOrderMap.keySet())
            {
                SortingOrder sortingOrder = sortingOrderMap.get(key);
                if (SortingOrder.DESC == sortingOrder)
                {
                    detachedCriteria.addOrder(Order.desc(key));
                }
                else
                {
                    detachedCriteria.addOrder(Order.asc(key));
                }
            }
        }
    }

}
