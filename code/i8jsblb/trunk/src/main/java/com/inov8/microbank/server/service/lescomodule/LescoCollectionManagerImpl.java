package com.inov8.microbank.server.service.lescomodule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.LescoCollectionModel;
import com.inov8.microbank.common.model.productmodule.paymentservice.LescoLogModel;
import com.inov8.microbank.server.dao.lescomodule.LescoCollectionDAO;
import com.inov8.microbank.server.dao.lescomodule.LescoLogDAO;

public class LescoCollectionManagerImpl implements LescoCollectionManager
{
	private LescoCollectionDAO lescoCollectionDAO; 	
	private GenericDao genericDao;
	private LescoLogDAO lescoLogDAO;

	protected final Log logger = LogFactory.getLog(LescoCollectionManagerImpl.class);
	
	
	
	public BaseWrapper createLescoCollection(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start OF LescoCollectionManagerImpl.createLescoCollection()....");
		}
		LescoCollectionModel lescoCollectionModel = (LescoCollectionModel) baseWrapper.getBasePersistableModel();
		lescoCollectionModel = this.lescoCollectionDAO.saveOrUpdate(lescoCollectionModel);
		if(logger.isDebugEnabled())
		{
			logger.debug("End OF LescoCollectionManagerImpl.createLescoCollection()....");
		}
		baseWrapper.setBasePersistableModel(lescoCollectionModel);
		return baseWrapper;
	}

	public SearchBaseWrapper searchLescoCollection(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		LescoCollectionModel lescoCollectionModel = (LescoCollectionModel) searchBaseWrapper.getBasePersistableModel();
		CustomList customList = this.lescoCollectionDAO.findByExample(lescoCollectionModel,searchBaseWrapper.getPagingHelperModel(),searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		searchBaseWrapper.setCustomList(customList);
		return searchBaseWrapper;
		
	}

	

	public BaseWrapper saveOrUpdateLescoCollection(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start OF LescoCollectionManagerImpl.saveOrUpdateLescoCollection()....");
		}
		LescoCollectionModel lescoCollectionModel = (LescoCollectionModel)baseWrapper.getBasePersistableModel();
		lescoCollectionModel = this.genericDao.createEntity(lescoCollectionModel);
		baseWrapper.setBasePersistableModel(lescoCollectionModel);
		if(logger.isDebugEnabled())
		{
			logger.debug("End OF LescoCollectionManagerImpl.saveOrUpdateLescoCollection()....");
		}
		return baseWrapper;
	}

	public SearchBaseWrapper searchLescoLog(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{	
		if(logger.isDebugEnabled())
		{
			logger.debug("Start OF LescoCollectionManagerImpl.searchLescoLog()....");
		}
		CustomList<LescoLogModel> customList = this.lescoLogDAO.findByExample((LescoLogModel)searchBaseWrapper.getBasePersistableModel(), null, null, searchBaseWrapper.getDateRangeHolderModel(), null);
		searchBaseWrapper.setCustomList(customList);
		if(logger.isDebugEnabled())
		{
			logger.debug("End OF LescoCollectionManagerImpl.searchLescoLog()....");
		}
		return searchBaseWrapper;
	}
	public BaseWrapper loadLescoCollection(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		LescoCollectionModel lescoCollectionModel = (LescoCollectionModel) baseWrapper.getBasePersistableModel();
		lescoCollectionModel = this.lescoCollectionDAO.findByPrimaryKey(lescoCollectionModel.getPrimaryKey());
		baseWrapper.setBasePersistableModel(lescoCollectionModel);
		return baseWrapper;
	}

	public void setLescoCollectionDAO(LescoCollectionDAO lescoCollectionDAO)
	{
		this.lescoCollectionDAO = lescoCollectionDAO;
	}

	public void setLescoLogDAO(LescoLogDAO lescoLogDAO)
	{
		this.lescoLogDAO = lescoLogDAO;
	}

	public void setGenericDao(GenericDao genericDao)
	{
		this.genericDao = genericDao;
	}
	
}
