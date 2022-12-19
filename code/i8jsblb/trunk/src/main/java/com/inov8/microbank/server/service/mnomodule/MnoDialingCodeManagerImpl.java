package com.inov8.microbank.server.service.mnomodule;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.MnoDialingCodeModel;
import com.inov8.microbank.common.model.mnomodule.MnoDialingCodeListViewModel;
import com.inov8.microbank.server.dao.mnomodule.MnoDialingCodeDAO;
import com.inov8.microbank.server.dao.mnomodule.MnoDialingCodeListViewDAO;


/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class MnoDialingCodeManagerImpl
    implements MnoDialingCodeManager
{
  private MnoDialingCodeDAO mnoDialingCodeDAO;
   private MnoDialingCodeListViewDAO mnoDialingCodeListViewDAO;

  public void setMnoDialingCodeListViewDAO(
		  MnoDialingCodeListViewDAO mnoDialingCodeListViewDAO) {
	this.mnoDialingCodeListViewDAO = mnoDialingCodeListViewDAO;
}

public BaseWrapper createOrUpdateMnoDialingCode(BaseWrapper baseWrapper)throws FrameworkCheckedException
  {
    MnoDialingCodeModel newMnoDialingCodeModel = new
        MnoDialingCodeModel();
    MnoDialingCodeModel mnoDialingCodeModel = (
        MnoDialingCodeModel)
        baseWrapper.getBasePersistableModel();
    newMnoDialingCodeModel.setCode(mnoDialingCodeModel.getCode());
    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
	exampleHolder.setMatchMode(MatchMode.EXACT);
    int recordCount = mnoDialingCodeDAO.countByExample(
        newMnoDialingCodeModel,exampleHolder);
    //***Check if name already exists
     if (recordCount == 0 ||
         newMnoDialingCodeModel.getMnoDialingCodeId() != null)
     {
       mnoDialingCodeModel = this.mnoDialingCodeDAO.saveOrUpdate( (
           MnoDialingCodeModel) baseWrapper.getBasePersistableModel());
       baseWrapper.setBasePersistableModel(mnoDialingCodeModel);
       return baseWrapper;
     }
     else
     {
       //set baseWrapper to null if record exists
       baseWrapper.setBasePersistableModel(null);
       return baseWrapper;
     }

  }

  public SearchBaseWrapper searchMnoDialingCode(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException
  {
    
	  System.out.println("jhlkjlkj");
	  CustomList<MnoDialingCodeListViewModel>
        list = this.mnoDialingCodeListViewDAO.findByExample( (MnoDialingCodeListViewModel)
                                                  searchBaseWrapper.
                                                  getBasePersistableModel(),
                                                  searchBaseWrapper.
                                                  getPagingHelperModel(),
                                                  searchBaseWrapper.
                                                  getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }
  
  public SearchBaseWrapper findMnoDialingCode(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException
  {
	  CustomList<MnoDialingCodeModel>
        list = this.mnoDialingCodeDAO.findByExample( (MnoDialingCodeModel)searchBaseWrapper.
                                                  getBasePersistableModel(),searchBaseWrapper.getPagingHelperModel(),
                                                  searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }
  
  public SearchBaseWrapper loadMnoDialingCode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		
	  MnoDialingCodeModel mnoDialingCodeModel = this.mnoDialingCodeDAO
		.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel()
				.getPrimaryKey());
	searchBaseWrapper.setBasePersistableModel(mnoDialingCodeModel);
	return searchBaseWrapper;

	}

  
  public void setMnoDialingCodeDAO(MnoDialingCodeDAO
                                        mnoDialingCodeDAO)
  {
    this.mnoDialingCodeDAO = mnoDialingCodeDAO;
  }


}
