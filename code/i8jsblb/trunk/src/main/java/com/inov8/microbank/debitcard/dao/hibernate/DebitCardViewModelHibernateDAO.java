package com.inov8.microbank.debitcard.dao.hibernate;

import com.inov8.framework.common.model.*;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.DateUtils;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.debitcard.dao.DebitCardViewModelDAO;
import com.inov8.microbank.debitcard.model.DebitCardViewModel;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.*;
import org.springframework.dao.DataAccessException;

import java.util.LinkedHashMap;
import java.util.List;

public class DebitCardViewModelHibernateDAO extends BaseHibernateDAO<DebitCardViewModel,Long,DebitCardViewModelDAO>
    implements DebitCardViewModelDAO {

    @Override
    public CustomList<DebitCardViewModel> findByExampleUnSorted(DebitCardViewModel exampleInstance, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap, DateRangeHolderModel dateRangeHolderModel, ExampleConfigHolderModel exampleConfigHolderModel, String... excludeProperty) throws DataAccessException {

        CustomList<DebitCardViewModel> customList = null;
        Example example = this.createExample(exampleInstance, dateRangeHolderModel, exampleConfigHolderModel, excludeProperty);
        if (pagingHelperModel != null && pagingHelperModel.isRowCountRequired())
        {
            DetachedCriteria detachedCriteria = this.createDetachedCriteria(example);
            addDateRangeToCriteria(dateRangeHolderModel, detachedCriteria);
            setExtendedCriteria(exampleInstance, detachedCriteria, false);
            detachedCriteria.setProjection(Projections.rowCount());
            List<Integer> list = this.getHibernateTemplate().findByCriteria(detachedCriteria);
            pagingHelperModel.setTotalRecordsCount(list.get(0));
        }

        DetachedCriteria detachedCriteria = this.createDetachedCriteria(example);
        addDateRangeToCriteria(dateRangeHolderModel, detachedCriteria);
        this.addSortingToCriteria(detachedCriteria, sortingOrderMap);
        setExtendedCriteria(exampleInstance, detachedCriteria, true);
        if (pagingHelperModel != null && pagingHelperModel.getPageNo() != null && pagingHelperModel.getPageSize() != null)
        {
            customList = new CustomList(this.getHibernateTemplate().findByCriteria(detachedCriteria,
                    (pagingHelperModel.getPageNo() - 1) * pagingHelperModel.getPageSize(), pagingHelperModel.getPageSize()));
        }
        else
        {
            customList = new CustomList(this.getHibernateTemplate().findByCriteria(detachedCriteria));

        }
        return customList;
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

    private void setExtendedCriteria(DebitCardViewModel exampleInstance, DetachedCriteria detachedCriteria, boolean eagerFetchFlag)
            throws HibernateException
    {
        List<AssociationModel> associationModelList = exampleInstance.getAssociationModelList();
        if (null != associationModelList && associationModelList.size() > 0)
        {
            for (AssociationModel associationModel : associationModelList)
            {
                BasePersistableModel basePersistableModel = associationModel.getValue();
                String propertyName = associationModel.getPropertyName();
                if (null != basePersistableModel)
                {
                    Long primaryKey = basePersistableModel.getPrimaryKey();

                    String primaryKeyFieldName = basePersistableModel.getPrimaryKeyFieldName();

                    if (null != primaryKey)
                    {
                        detachedCriteria.createCriteria(propertyName).add(
                                Restrictions.eq(primaryKeyFieldName, basePersistableModel.getPrimaryKey()));
                    }

                    else
                    {
                        detachedCriteria.createCriteria(propertyName).add(Example.create(basePersistableModel));
                    }
                }
                if (eagerFetchFlag)
                {
                    detachedCriteria.setFetchMode(propertyName, FetchMode.JOIN);
                }
            }
        }
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

    private Example createExample(DebitCardViewModel exampleInstance, DateRangeHolderModel dateRangeHolderModel,
                                  ExampleConfigHolderModel exampleConfigHolderModel, String... excludeProperty)
    {
        Example example = Example.create(exampleInstance);
        if (exampleConfigHolderModel == null)
        {
            exampleConfigHolderModel = this.getDefaultExampleConfigHolderModel();
        }
        if (exampleConfigHolderModel.isExcludeZeroes())
        {
            example.excludeZeroes();
        }
        if (exampleConfigHolderModel.isEnableLike())
        {
            if (exampleConfigHolderModel.getMatchMode() != null)
            {
                example.enableLike(exampleConfigHolderModel.getMatchMode());
            }
            else
            {
                example.enableLike();
            }
        }
        if (exampleConfigHolderModel.isIgnoreCase())
        {
            example.ignoreCase();
        }

        // Exclude following properties from comparison
        if (null != excludeProperty)
        {
            for (String exclude : excludeProperty)
            {
                example.excludeProperty(exclude);
            }
        }
        // Exclude the property used for Date comparison from Example object
        if (null != dateRangeHolderModel && dateRangeHolderModel.getDatePropertyName() != null && (!("".equals(dateRangeHolderModel.getDatePropertyName()))))
        {
            example.excludeProperty(dateRangeHolderModel.getDatePropertyName());
        }

        return example;
    }
}
