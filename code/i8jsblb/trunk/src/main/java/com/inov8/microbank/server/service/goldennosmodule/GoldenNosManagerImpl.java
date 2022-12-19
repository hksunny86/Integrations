package com.inov8.microbank.server.service.goldennosmodule;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.GoldenNosModel;
import com.inov8.microbank.common.model.goldennosmodule.GoldenNosListViewModel;
import com.inov8.microbank.server.dao.portal.mfsaccountmodule.GoldenNosDAO;
import com.inov8.microbank.server.dao.portal.mfsaccountmodule.GoldenNosListViewDAO;



public class GoldenNosManagerImpl implements GoldenNosManager
{
	GoldenNosDAO goldenNosDAO ;
	GoldenNosListViewDAO goldenNosListViewDAO;

	public BaseWrapper createOrUpdateGoldenNos(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		GoldenNosModel newGoldenNosModel = new GoldenNosModel();		
		GoldenNosModel goldenNosModel = (GoldenNosModel) baseWrapper.getBasePersistableModel();
	    
		newGoldenNosModel.setGoldenNumber(goldenNosModel.getGoldenNumber() );
	    int recordCount = goldenNosDAO.countByExample(newGoldenNosModel);
	    //***Check if name already exists
	     if (recordCount == 0 || goldenNosModel.getPrimaryKey() != null)
	     {
	    	 goldenNosModel = this.goldenNosDAO.saveOrUpdate( (
	    			 GoldenNosModel) baseWrapper.getBasePersistableModel());
	       baseWrapper.setBasePersistableModel(goldenNosModel);
	       return baseWrapper;

	     }
	     else
	     {
	       //set baseWrapper to null if record exists
	       baseWrapper.setBasePersistableModel(null);
	       return baseWrapper;
	     }
	}

	public SearchBaseWrapper searchGoldenNos(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
		CustomList<GoldenNosListViewModel>
        list = this.goldenNosListViewDAO.findByExample( (GoldenNosListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
	}

	public void setGoldenNosDAO(GoldenNosDAO goldenNosDAO) {
		this.goldenNosDAO = goldenNosDAO;
	}

	public void setGoldenNosListViewDAO(GoldenNosListViewDAO goldenNosListViewDAO) {
		this.goldenNosListViewDAO = goldenNosListViewDAO;
	}
	
	

	
}
