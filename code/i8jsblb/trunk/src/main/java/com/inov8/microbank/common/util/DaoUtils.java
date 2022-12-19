package com.inov8.microbank.common.util;

import java.util.LinkedHashMap;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.DateUtils;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 17, 2013 3:26:19 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class DaoUtils
{

    public static void addDateRangeToCriteria(DateRangeHolderModel dateRangeHolderModel, DetachedCriteria detachedCriteria)
    {
        if(dateRangeHolderModel != null && dateRangeHolderModel.getDatePropertyName() != null
                && (!("".equals(dateRangeHolderModel.getDatePropertyName()))))
        {
            if(dateRangeHolderModel.getFromDate() != null && dateRangeHolderModel.getToDate() != null)
            {
                detachedCriteria.add(Restrictions.between(dateRangeHolderModel.getDatePropertyName(), DateUtils.getDayStartDate(dateRangeHolderModel.getFromDate()), 
                    DateUtils.getDayEndDate(dateRangeHolderModel.getToDate())));
            }
            else if(dateRangeHolderModel.getFromDate() != null && dateRangeHolderModel.getToDate() == null)
            {
                detachedCriteria.add(Restrictions.ge(dateRangeHolderModel.getDatePropertyName(), DateUtils.getDayStartDate(dateRangeHolderModel.getFromDate())));
            }
            else if(dateRangeHolderModel.getFromDate() == null && dateRangeHolderModel.getToDate() != null)
            {
                detachedCriteria.add(Restrictions.le(dateRangeHolderModel.getDatePropertyName(), DateUtils.getDayEndDate(dateRangeHolderModel.getToDate())));
            }
        }
    }

    public static void addSortingToCriteria( DetachedCriteria detachedCriteria, LinkedHashMap<String, SortingOrder> sortingOrderMap, String primaryKeyField )
    {
        if( sortingOrderMap != null && sortingOrderMap.size() > 0 )
        {
            for(String key : sortingOrderMap.keySet())
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
        else
        {
            try
            {
                if(primaryKeyField != null)
                {
                    detachedCriteria.addOrder(Order.asc(primaryKeyField));
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

}
