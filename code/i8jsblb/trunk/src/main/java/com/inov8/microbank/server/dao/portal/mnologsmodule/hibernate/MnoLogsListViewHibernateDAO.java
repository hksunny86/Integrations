package com.inov8.microbank.server.dao.portal.mnologsmodule.hibernate;

import java.util.LinkedHashMap;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.DateUtils;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.mnologsmodule.MnologsListViewModel;
import com.inov8.microbank.server.dao.portal.mnologsmodule.MnoLogsListViewDAO;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

public class MnoLogsListViewHibernateDAO extends
		BaseHibernateDAO<MnologsListViewModel, Long, MnoLogsListViewDAO>
		implements MnoLogsListViewDAO {

	@SuppressWarnings("unchecked")
	public List<UsecaseModel> findUsecases( Long[] usecaseIds )
	{
		HibernateTemplate hibernateTemplate = getHibernateTemplate();
		DetachedCriteria criteria = DetachedCriteria.forClass( UsecaseModel.class );
		criteria.add( Restrictions.in( "usecaseId", usecaseIds ) );
		criteria.addOrder( Order.asc("name") );

		return hibernateTemplate.findByCriteria( criteria );
	}

	public CustomList<MnologsListViewModel> findByExampleUnSorted(MnologsListViewModel exampleInstance, PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap, DateRangeHolderModel dateRangeHolderModel,
			ExampleConfigHolderModel exampleConfigHolderModel, String... excludeProperty) throws DataAccessException
	{
		CustomList<MnologsListViewModel> customList = null;

		// Create the Example Instance

		Example example = this.createExample(exampleInstance, dateRangeHolderModel, exampleConfigHolderModel, excludeProperty);
		// If count is required
		if (pagingHelperModel != null && pagingHelperModel.isRowCountRequired())
		{
			// Creating Criteria based on Example Object for getting count
			DetachedCriteria detachedCriteria = this.createDetachedCriteria(example);
			// If Date between Comparison is used add that so that is
			// incorporated in Count results also

			addDateRangeToCriteria(dateRangeHolderModel, detachedCriteria);

			setExtendedCriteria(exampleInstance, detachedCriteria, false);

			// Finally set the Projection to get count
			detachedCriteria.setProjection(Projections.rowCount());

			// Get the actual count
			List<Integer> list = this.getHibernateTemplate().findByCriteria(detachedCriteria);
			pagingHelperModel.setTotalRecordsCount(list.get(0));
		}

		// Creating criteria based on Example Object to get the actual Result
		// Set
		DetachedCriteria detachedCriteria = this.createDetachedCriteria(example);
		// If Date between Comparison is used add that so that is incorporated
		// in Resultset also

		addDateRangeToCriteria(dateRangeHolderModel, detachedCriteria);

		// Adding sorting to the criteria
		this.addSortingToCriteria(detachedCriteria, sortingOrderMap);

		setExtendedCriteria(exampleInstance, detachedCriteria, true);

		// If use of paging is specified get Paginated resultset
		if (pagingHelperModel != null && pagingHelperModel.getPageNo() != null && pagingHelperModel.getPageSize() != null)
		{
			customList = new CustomList(this.getHibernateTemplate().findByCriteria(detachedCriteria,
					(pagingHelperModel.getPageNo() - 1) * pagingHelperModel.getPageSize(), pagingHelperModel.getPageSize()));
		}
		// otherwise get the complete resultset
		else
		{
			customList = new CustomList(this.getHibernateTemplate().findByCriteria(detachedCriteria));

		}
		return customList;
	}

	@Override
	public MnologsListViewModel getActionLogXMLByActionLogId(Long actionLogId) throws FrameworkCheckedException {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(MnologsListViewModel.class);
		detachedCriteria.add(Restrictions.eq("actionLogId",actionLogId));
		List<MnologsListViewModel>  list = getHibernateTemplate().findByCriteria(detachedCriteria);
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	@Override
	public List<MnologsListViewModel> getActionLogData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		MnologsListViewModel mnologsListViewModel = (MnologsListViewModel) searchBaseWrapper.getBasePersistableModel();
		Criterion criterion = Restrictions.eq("actionAuthorizationId",mnologsListViewModel.getActionAuthorizationId() );
		Criterion criterion1 = Restrictions.eq("actionStatus","Approved");

		Session session = getSession();
		Criteria criteria = session.createCriteria(MnologsListViewModel.class);
		criteria.add(criterion);
		criteria.setFetchSize(20);
		List<MnologsListViewModel> list = criteria.list();
		SessionFactoryUtils.releaseSession(session, getSessionFactory());
		return list;
	}

	private Example createExample(MnologsListViewModel exampleInstance, DateRangeHolderModel dateRangeHolderModel,
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
	private void setExtendedCriteria(MnologsListViewModel exampleInstance, DetachedCriteria detachedCriteria, boolean eagerFetchFlag)
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
