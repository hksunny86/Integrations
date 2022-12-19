package com.inov8.microbank.server.service.portal.concernmodule;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.ConcernCategoryModel;
import com.inov8.microbank.common.model.ConcernModel;
import com.inov8.microbank.common.model.ConcernPartnerAsociationModel;
import com.inov8.microbank.common.model.ConcernPartnerModel;
import com.inov8.microbank.common.model.ConcernPartnerTypeModel;
import com.inov8.microbank.common.model.ConcernPriorityModel;
import com.inov8.microbank.common.model.ConcernStatusModel;
import com.inov8.microbank.common.model.portal.concernmodule.AppUserConcernPartnerViewModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernHistoryListViewModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernInActPartnerViewModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernListViewModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernOpHistoryListViewModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernOpRaisedToListViewModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernPartnersRuleViewModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernsParentListViewModel;
import com.inov8.microbank.common.util.ConcernPartnerTypeConstants;
import com.inov8.microbank.common.util.ConcernsConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.portal.concernmodule.AppUserConcernPartnerViewDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernCategoryDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernHistoryListViewDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernInActPartnerViewDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernListViewDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernOpHistoryListViewDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernOpRaisedToListViewDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernPartnerAssociationDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernPartnerDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernPartnerTypeDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernPartnersRuleViewDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernPriorityDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernStatusDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernsParentListViewDAO;

public class ConcernManagerImpl implements ConcernManager{
	
	private ConcernListViewDAO concernListViewDAO;
	private ConcernHistoryListViewDAO concernHistoryListViewDAO;
	private ConcernCategoryDAO concernCategoryDAO;
	private ConcernDAO concernDAO;
	private ConcernPartnerAssociationDAO concernPartnerAssociationDAO;
	private ConcernPriorityDAO concernPriorityDAO;
	private ConcernStatusDAO concernStatusDAO;
	private ConcernPartnerTypeDAO concernPartnerTypeDAO;
	private ConcernPartnerDAO concernPartnerDAO;
	private AppUserConcernPartnerViewDAO appUserConcernPartnerViewDAO;
	private ConcernsParentListViewDAO concernsParentListViewDAO;
	private ConcernInActPartnerViewDAO concernInActPartnerViewDAO;
	private ConcernPartnersRuleViewDAO concernPartnersRuleViewDAO;
	private ConcernOpHistoryListViewDAO concernOpHistoryListViewDAO;
	private ConcernOpRaisedToListViewDAO concernOpRaisedToListViewDAO;

	public SearchBaseWrapper searchConcernsList(SearchBaseWrapper searchBaseWrapper)
	throws FrameworkCheckedException{
	    CustomList<ConcernListViewModel>
	      list = this.concernListViewDAO.findByExample( (
	    		  ConcernListViewModel)
	          searchBaseWrapper.
	          getBasePersistableModel(),
	          searchBaseWrapper.
	          getPagingHelperModel(),
	          searchBaseWrapper.
	          getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
	    searchBaseWrapper.setCustomList(list);
	    return searchBaseWrapper;

	}

	public SearchBaseWrapper searchConcernsParentList(SearchBaseWrapper searchBaseWrapper)
	throws FrameworkCheckedException{
	    CustomList<ConcernsParentListViewModel>
	      list = this.concernsParentListViewDAO.findByExample( (
	    		  ConcernsParentListViewModel)
	          searchBaseWrapper.
	          getBasePersistableModel(),
	          searchBaseWrapper.
	          getPagingHelperModel(),
	          searchBaseWrapper.
	          getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
	    searchBaseWrapper.setCustomList(list);
	    return searchBaseWrapper;

	}	
	
	
	public SearchBaseWrapper searchConcernsHistory(SearchBaseWrapper searchBaseWrapper)
	throws FrameworkCheckedException{
	    CustomList<ConcernHistoryListViewModel>
	      list = this.concernHistoryListViewDAO.findByExample( (
	    		  ConcernHistoryListViewModel)
	          searchBaseWrapper.
	          getBasePersistableModel(),
	          searchBaseWrapper.
	          getPagingHelperModel(),
	          searchBaseWrapper.
	          getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
	    searchBaseWrapper.setCustomList(list);
	    return searchBaseWrapper;

	}
	
	public SearchBaseWrapper searchOpConcernsHistory(SearchBaseWrapper searchBaseWrapper)
	throws FrameworkCheckedException{
	    CustomList<ConcernOpHistoryListViewModel>
	      list = this.concernOpHistoryListViewDAO.findByExample( (
	    		  ConcernOpHistoryListViewModel)
	          searchBaseWrapper.
	          getBasePersistableModel(),
	          searchBaseWrapper.
	          getPagingHelperModel(),
	          searchBaseWrapper.
	          getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
	    searchBaseWrapper.setCustomList(list);
	    return searchBaseWrapper;

	}		
	
	public BaseWrapper searchConcernCategoryByName(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ConcernCategoryModel concernCategoryModel = (ConcernCategoryModel)baseWrapper.getBasePersistableModel();
		ExampleConfigHolderModel configModel = new ExampleConfigHolderModel();
		configModel.setMatchMode(MatchMode.EXACT);
		CustomList<ConcernCategoryModel> list = this.concernCategoryDAO.findByExample(concernCategoryModel,null,null,configModel);
		baseWrapper.putObject("list", list);
		return baseWrapper;
	}

	public SearchBaseWrapper searchConcernCategory(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<ConcernCategoryModel>
	      list = this.concernCategoryDAO.findByExample( (ConcernCategoryModel)
	          searchBaseWrapper.
	          getBasePersistableModel(),
	          searchBaseWrapper.
	          getPagingHelperModel(),
	          searchBaseWrapper.
	          getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
	      searchBaseWrapper.setCustomList(list);
	    return searchBaseWrapper;
	}

	public BaseWrapper createConcernCategory(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ConcernCategoryModel concernCategoryModel = (ConcernCategoryModel)baseWrapper.getBasePersistableModel();
		concernCategoryModel = this.concernCategoryDAO.saveOrUpdate(concernCategoryModel);
		baseWrapper.setBasePersistableModel(concernCategoryModel);
		return baseWrapper;
	}

	public BaseWrapper searchConcernCategoryByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ConcernCategoryModel concernCategoryModel = (ConcernCategoryModel)baseWrapper.getBasePersistableModel();
		concernCategoryModel = this.concernCategoryDAO.findByPrimaryKey(concernCategoryModel.getConcernCategoryId());
		baseWrapper.setBasePersistableModel(concernCategoryModel);
		return baseWrapper;
	}

	public BaseWrapper updateConcernCategory(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ConcernCategoryModel concernCategoryModel = (ConcernCategoryModel)baseWrapper.getBasePersistableModel();
		concernCategoryModel = this.concernCategoryDAO.saveOrUpdate(concernCategoryModel);
		baseWrapper.setBasePersistableModel(concernCategoryModel);
		return baseWrapper;
	}

	public BaseWrapper createConcern(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		ConcernModel concernModel = (ConcernModel)baseWrapper.getBasePersistableModel();
		String cCode = getUniqueConcernCode();
		concernModel.setConcernCode(cCode);
		this.concernDAO.saveOrUpdate(concernModel);
		ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();
		concernPartnerModel = this.concernPartnerDAO.findByPrimaryKey(concernModel.getRecipientPartnerId());
		baseWrapper.setBasePersistableModel(concernModel);
		baseWrapper.putObject("recepientPartnerName", concernPartnerModel.getName());
		return baseWrapper;
	}
	
	public BaseWrapper findIndirectActiveConcern(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		String  concernCode = (String)baseWrapper.getObject(ConcernsConstants.KEY_CONCERN_CODE);
		if(concernCode == null){
			return null;
		}
		
		ConcernInActPartnerViewModel concernInActPartnerViewModel = new ConcernInActPartnerViewModel();
		concernInActPartnerViewModel.setConcernCode(concernCode);
		
		CustomList <ConcernInActPartnerViewModel> clist = concernInActPartnerViewDAO.findByExample(concernInActPartnerViewModel);
		baseWrapper.putObject("clist", clist);
		return baseWrapper;
	}
	

	public BaseWrapper updateConcernModel(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ConcernModel concernModel = (ConcernModel)baseWrapper.getBasePersistableModel();
		concernModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		concernModel.setUpdatedOn(new Date());
		concernModel = this.concernDAO.saveOrUpdate(concernModel);
		
		//also need to close all child concren if there are
		if(concernModel.getConcernStatusId().longValue() == ConcernsConstants.STATUS_CLOSED.longValue()){
			 List <ConcernModel>secondartConcernsList = concernDAO.findSecondaryConcernRecords(concernModel.getConcernCode());
			 Iterator<ConcernModel> itr = secondartConcernsList.iterator();
			 ConcernModel childConcernModel = null;
			 while(itr.hasNext()){
				 childConcernModel = itr.next();
				 childConcernModel.setConcernStatusId(ConcernsConstants.STATUS_CLOSED);
				 childConcernModel.setComments(ConcernsConstants.CLOSED_COMMENTS);
				 childConcernModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				 childConcernModel.setUpdatedOn(new Date());
				 concernDAO.saveOrUpdate(childConcernModel);
			 }
		}
		
		return baseWrapper;
	}
	
	public BaseWrapper updateConcernForReply(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ConcernModel concernModel = (ConcernModel)baseWrapper.getBasePersistableModel();

		String isCreator = (String)baseWrapper.getObject(ConcernsConstants.KEY_IS_CREATOR);
		
		ConcernModel changedConcernModel = null;
		
		if("true".equals(isCreator)){
			//is creator == true mean that we need to updated primary record
			changedConcernModel = concernDAO.findPrimaryConcernRecord(concernModel.getConcernCode());
			changedConcernModel.setConcernStatusId(concernModel.getConcernStatusId());
		}else {			
			changedConcernModel = concernDAO.findActiveIndirectPartner(concernModel.getConcernCode());
			changedConcernModel.setConcernStatusId(concernModel.getConcernStatusId());
		}

		changedConcernModel.setComments(concernModel.getComments());
		changedConcernModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		changedConcernModel.setUpdatedOn(new Date());
		
		concernDAO.saveOrUpdate(changedConcernModel);
		ConcernPartnerModel concernPartnerModel = this.concernPartnerDAO.findByPrimaryKey(changedConcernModel.getInitiatorPartnerId());
		baseWrapper.putObject("initiatorPartnerName", concernPartnerModel.getName());
		ConcernPartnerModel
		concernPartnerModelnew = this.concernPartnerDAO.findByPrimaryKey(changedConcernModel.getRecipientPartnerId());
		baseWrapper.putObject("recepientPartnerName", concernPartnerModelnew.getName());
		return baseWrapper;
	}
	
	
	public BaseWrapper updateConcernForResolver(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ConcernModel concernModel = (ConcernModel)baseWrapper.getBasePersistableModel();
		String newComments = concernModel.getComments();
		Long recipientPartnerId = concernModel.getRecipientPartnerId();
		String title = concernModel.getTitle();
		String concernCode = concernModel.getConcernCode();
		Long currentAppUserId  = UserUtils.getCurrentUser().getAppUserId();
		
		String isRaisedAgain = "false";
		
		ConcernModel resultedConcernModel = null;
		
		AppUserConcernPartnerViewModel appUserConcernPartnerViewModel = null;
		appUserConcernPartnerViewModel = this.appUserConcernPartnerViewDAO.findByPrimaryKey(currentAppUserId);
 
		
		//if there are any other indirect active record exist... void this
		ConcernModel indirectActiveConcernModel = concernDAO.findActiveIndirectPartner(concernCode);
		if(indirectActiveConcernModel != null){
			indirectActiveConcernModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			indirectActiveConcernModel.setUpdatedOn(new Date());
			
			//if status is replied then u cannot void this
			if(indirectActiveConcernModel.getConcernStatusId().longValue() != ConcernsConstants.STATUS_REPLIED ){
				indirectActiveConcernModel.setConcernStatusId(ConcernsConstants.STATUS_VOID);
			}
			
			indirectActiveConcernModel.setActive(false);
			this.concernDAO.saveOrUpdate(indirectActiveConcernModel);
		}
		
		//if initiator and recipient already exist against this concern code then just update status and comments
		ConcernModel voidedConcernModel = new ConcernModel();
		voidedConcernModel.setInitiatorPartnerId(concernModel.getInitiatorPartnerId());
		voidedConcernModel.setRecipientPartnerId(recipientPartnerId);
		voidedConcernModel.setConcernCode(concernCode);
		CustomList <ConcernModel>voidedClist = concernDAO.findByExample(voidedConcernModel);
		List <ConcernModel>voidedList = voidedClist.getResultsetList();

		if(!voidedList.isEmpty()){
			//initiator and partner exist against concern code .. thus just need to update ... I8 Raised again AFTER VOIDED
			voidedConcernModel = voidedList.get(0);
			voidedConcernModel.setConcernStatusId(ConcernsConstants.STATUS_INPROCESS);
			voidedConcernModel.setComments(newComments);
			voidedConcernModel.setUpdatedBy(currentAppUserId);
			voidedConcernModel.setUpdatedOn(new Date());
			voidedConcernModel.setActive(true);
			
			resultedConcernModel = voidedConcernModel;
			//raised again flag
			isRaisedAgain = "true";
		}else{

			//create new cocnern MOdel  
			ConcernModel parentConcernModel = new ConcernModel();
			parentConcernModel = concernDAO.findPrimaryConcernRecord(concernCode);
			
			//create new concern for resolving partner.. 
			ConcernModel newConcernModel = new ConcernModel();
			newConcernModel.setComments(newComments);
			newConcernModel.setConcernCategoryId(parentConcernModel.getConcernCategoryId());
			newConcernModel.setConcernCode(concernCode);
			newConcernModel.setConcernPriorityId(parentConcernModel.getConcernPriorityId());
			newConcernModel.setConcernStatusId(ConcernsConstants.STATUS_OPEN);
			newConcernModel.setCreatedBy(currentAppUserId);
			newConcernModel.setUpdatedBy(currentAppUserId);
			newConcernModel.setCreatedOn(new Date());
			newConcernModel.setUpdatedOn(new Date());
			newConcernModel.setDescription(concernModel.getDescription());
			newConcernModel.setRecipientPartnerId(recipientPartnerId);
			newConcernModel.setInitiatorPartnerId(appUserConcernPartnerViewModel.getConcernPartnerId());
			newConcernModel.setParentConcernId(parentConcernModel.getConcernId());
			newConcernModel.setTitle(title);
			newConcernModel.setActive(true);
			
			resultedConcernModel = newConcernModel; 
			
			this.concernDAO.saveOrUpdate(newConcernModel);
		}	
		
		ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();
		concernPartnerModel = this.concernPartnerDAO.findByPrimaryKey(recipientPartnerId);
		baseWrapper.putObject("recepientPartnerName", concernPartnerModel.getName());
		baseWrapper.putObject("isRaisedAgain", isRaisedAgain);
		baseWrapper.setBasePersistableModel(resultedConcernModel);
		
		return baseWrapper;
	}	
	

	public BaseWrapper loadConcern(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		ConcernListViewModel concernListViewModel = concernListViewDAO.findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
		baseWrapper.setBasePersistableModel(concernListViewModel);
		return baseWrapper;
	}
	
	public BaseWrapper loadConcernModel(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ConcernModel concernModel = this.concernDAO.findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
		baseWrapper.setBasePersistableModel(concernModel);
		return baseWrapper;
	}

	public SearchBaseWrapper searchConcernPriority(){
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		CustomList<ConcernPriorityModel> list = this.concernPriorityDAO.findAll();
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper searchConcernStatus() throws FrameworkCheckedException {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		CustomList<ConcernStatusModel> list = this.concernStatusDAO.findAll();
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public SearchBaseWrapper searchConcernType() throws FrameworkCheckedException {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		CustomList<ConcernPartnerTypeModel> list = this.concernPartnerTypeDAO.findAll();
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public SearchBaseWrapper searchConcernPartner() throws FrameworkCheckedException {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		CustomList<ConcernPartnerModel> list = this.concernPartnerDAO.findAll();
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public SearchBaseWrapper searchConcernCategory() throws FrameworkCheckedException {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		CustomList<ConcernCategoryModel> list = this.concernCategoryDAO.findAll();
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public BaseWrapper searchAppUserConcernPartnerViewByPrimaryKey(BaseWrapper baseWrapper) {
		AppUserConcernPartnerViewModel appUserConcernPartnerViewModel = (AppUserConcernPartnerViewModel)baseWrapper.getBasePersistableModel();
		appUserConcernPartnerViewModel = this.appUserConcernPartnerViewDAO.findByPrimaryKey(appUserConcernPartnerViewModel.getAppUserId());
		
		baseWrapper.setBasePersistableModel(appUserConcernPartnerViewModel);
		return baseWrapper;
	}

	public BaseWrapper loadConcernPartnerByPrimaryKey(BaseWrapper baseWrapper) {
		ConcernPartnerModel concernPartnerModel = (ConcernPartnerModel)baseWrapper.getBasePersistableModel();
		concernPartnerModel = this.concernPartnerDAO.findByPrimaryKey(concernPartnerModel.getConcernPartnerId());
		baseWrapper.setBasePersistableModel(concernPartnerModel);
		return baseWrapper;
	}	
	
	
	public SearchBaseWrapper searchConcernPartners(SearchBaseWrapper searchBaseWrapper) {
		ConcernPartnerModel concernPartnerModel = (ConcernPartnerModel)searchBaseWrapper.getBasePersistableModel();
		CustomList clist  = this.concernPartnerDAO.findByExample(concernPartnerModel);
		searchBaseWrapper.setCustomList(clist);
		return searchBaseWrapper;
	}
	
	private String generateConcernCode()
	{
	      String orgChars = "CC" ;
	      String alphaChars = RandomUtils.generateRandom(8, true, true).toUpperCase() ;

	       return orgChars + alphaChars ;
	}

	public SearchBaseWrapper searchOpPartners(SearchBaseWrapper searchBaseWrapper) {
		ConcernOpRaisedToListViewModel concernOpRaisedToListViewModel = (ConcernOpRaisedToListViewModel)searchBaseWrapper.getBasePersistableModel();
		CustomList  clist = concernOpRaisedToListViewDAO.findByExample(concernOpRaisedToListViewModel);
		searchBaseWrapper.setCustomList(clist);
		return searchBaseWrapper;
	}	
	
	public BaseWrapper getPartnerPartners(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		Long partnerId = new Long(baseWrapper.getObject(ConcernsConstants.KEY_PARTNER_ID).toString());
		//find associated partner list.. this will only return partner Ids in Long
		List associatedPartnerIdsList = concernPartnerDAO.findAssociatedPartnerPartners(partnerId);
		
		ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();
		List <ConcernPartnerModel>concernPartnerModelListFromIds = new ArrayList<ConcernPartnerModel>();
		CustomList <ConcernPartnerModel>operatorList = new CustomList();
		
		if(!associatedPartnerIdsList.isEmpty()){
			//now populate concernPartnerModel from previous list
			Iterator <Long>itr = associatedPartnerIdsList.iterator();
			while(itr.hasNext()){
				concernPartnerModel = concernPartnerDAO.findByPrimaryKey(itr.next());
				concernPartnerModelListFromIds.add(concernPartnerModel);
			}
		}else{
			//load only operator partner .. currently there is only inov8 operator exist that must exist in any case
			concernPartnerModel.setConcernPartnerTypeId(ConcernPartnerTypeConstants.BANK);
			operatorList = concernPartnerDAO.findByExample(concernPartnerModel);
		}

		if(!concernPartnerModelListFromIds.isEmpty()){
			CustomList resltlist = new CustomList();
			resltlist.setResultsetList(concernPartnerModelListFromIds);
			baseWrapper.putObject("clist", resltlist);
		}else{
			baseWrapper.putObject("clist", operatorList);
		}
		
		return baseWrapper;
	}
	
	public BaseWrapper createPartnerAssociationReferenceData(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ConcernPartnersRuleViewModel concernPartnersRuleViewModel = (ConcernPartnersRuleViewModel)baseWrapper.getBasePersistableModel();
		CustomList clist = concernPartnersRuleViewDAO.findByExample(concernPartnersRuleViewModel);
		List <ConcernPartnersRuleViewModel> list = clist.getResultsetList();
		
		if(list.isEmpty()){
			//insert all partner against selected partner
//			List <ConcernPartnerModel> partnerList	= concernPartnerDAO.findAllOtherPartners(concernPartnersRuleViewModel.getPartnerId());
			List <ConcernPartnerModel> partnerList	= concernPartnerDAO.findAllPartnersIncludingMyself();
			Iterator <ConcernPartnerModel>itr = partnerList.iterator();
			ConcernPartnerModel tmpConcernPartnerModel = null;
			ConcernPartnerAsociationModel tmpConcernPartnerAsociationModel = null;
			
			while(itr.hasNext()){
				tmpConcernPartnerModel = itr.next();
				tmpConcernPartnerAsociationModel = new ConcernPartnerAsociationModel();
				tmpConcernPartnerAsociationModel.setAssociatedPartnerId(tmpConcernPartnerModel.getConcernPartnerId());				
				tmpConcernPartnerAsociationModel.setPartnerId(concernPartnersRuleViewModel.getPartnerId());
				createConcernPartnerAsociationRecord(tmpConcernPartnerAsociationModel);
			}
		}else{
			//check if there are all partners exist against selected partner
//			List <ConcernPartnerModel> partnerList	= concernPartnerDAO.findAllOtherPartners(concernPartnersRuleViewModel.getPartnerId());
			
			List <ConcernPartnerModel> partnerList	= concernPartnerDAO.findAllPartnersIncludingMyself();
			Iterator <ConcernPartnerModel>itr = partnerList.iterator();
			ConcernPartnerModel tmpConcernPartnerModel = null;
			ConcernPartnerAsociationModel tmpConcernPartnerAsociationModel = null;		
			while(itr.hasNext()){
				tmpConcernPartnerModel = itr.next();
				tmpConcernPartnerAsociationModel = new ConcernPartnerAsociationModel();
				tmpConcernPartnerAsociationModel.setPartnerId(concernPartnersRuleViewModel.getPartnerId());
				tmpConcernPartnerAsociationModel.setAssociatedPartnerId(tmpConcernPartnerModel.getConcernPartnerId());
				CustomList<ConcernPartnerAsociationModel>assoPartnerCList = concernPartnerAssociationDAO.findByExample(tmpConcernPartnerAsociationModel);
				List<ConcernPartnerAsociationModel>assoPartnerList = assoPartnerCList.getResultsetList();
				if(assoPartnerList.isEmpty()){
					createConcernPartnerAsociationRecord(tmpConcernPartnerAsociationModel);
				}
			}
		}
		return baseWrapper;
	}	
	
	private ConcernPartnerAsociationModel createConcernPartnerAsociationRecord(ConcernPartnerAsociationModel tmpConcernPartnerAsociationModel){
		
		Long currentUserAppUserId = UserUtils.getCurrentUser().getAppUserId();
		//if partner is I8 then need to active this partner					
		AppUserConcernPartnerViewModel appUserConcernPartnerViewModel = this.appUserConcernPartnerViewDAO.findByPrimaryKey(currentUserAppUserId);
		if(appUserConcernPartnerViewModel.getConcernPartnerId().longValue() == tmpConcernPartnerAsociationModel.getAssociatedPartnerId().longValue()){
			//if i8 then activate
			tmpConcernPartnerAsociationModel.setActive(true);
		}else{
			// if not i8 inactive this
			tmpConcernPartnerAsociationModel.setActive(false);
		}
		
		tmpConcernPartnerAsociationModel.setCreatedBy(currentUserAppUserId);
		tmpConcernPartnerAsociationModel.setCreatedOn(new Date());
		tmpConcernPartnerAsociationModel.setUpdatedBy(currentUserAppUserId);
		tmpConcernPartnerAsociationModel.setUpdatedOn(new Date());
		concernPartnerAssociationDAO.saveOrUpdate(tmpConcernPartnerAsociationModel);
		
		return tmpConcernPartnerAsociationModel;
	}
	
	public SearchBaseWrapper searchAllOtherPartners(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		Long partnerId = new Long(searchBaseWrapper.getObject("partnerId").toString());
		List <ConcernPartnerModel> partnerList	= concernPartnerDAO.findAllOtherPartners(partnerId);
		CustomList clist = new CustomList();
		clist.setResultsetList(partnerList);
		searchBaseWrapper.setCustomList(clist);		
		return searchBaseWrapper;
	}
	
	
	public SearchBaseWrapper loadConcernPartnerRules(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException{
		ConcernPartnersRuleViewModel concernPartnersRuleViewModel = (ConcernPartnersRuleViewModel)searchBaseWrapper.getBasePersistableModel();
		CustomList clist = concernPartnersRuleViewDAO.findByExample(concernPartnersRuleViewModel);	
		searchBaseWrapper.setCustomList(clist);
		return searchBaseWrapper;
	}	
	
	private String getUniqueConcernCode()
	{
	       ConcernModel concernModel = new ConcernModel();
    	   ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
    	   exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);

	       boolean isNotUnique = true;
	       while (isNotUnique)
	       {
	    	   concernModel.setConcernCode(generateConcernCode());
		       try
		       {
			       Integer count = this.concernDAO.countByExample(concernModel, exampleConfigHolderModel);
			       if(count==0)
			    	   isNotUnique = false;
		       }
		       catch(Exception ex)
		       {
		    	   ex.printStackTrace();
		       }
	       }
	       return concernModel.getConcernCode();
	}

	
	public BaseWrapper updatePartnerAssociationsRole(BaseWrapper baseWrapper) {
		String []partnersAssociation = (String[])baseWrapper.getObject("partnersAssociation");
		Long partnerId = new Long((String)baseWrapper.getObject("partnerId"));
		ConcernPartnerAsociationModel concernPartnerAssociationModel = new ConcernPartnerAsociationModel();
		concernPartnerAssociationModel.setPartnerId(partnerId);
		//first clear all check boxes against this partner
		CustomList clist = concernPartnerAssociationDAO.findByExample(concernPartnerAssociationModel);
		List <ConcernPartnerAsociationModel>list = clist.getResultsetList();
		Iterator <ConcernPartnerAsociationModel>itr = list.iterator();
		ConcernPartnerAsociationModel tmpConcernPartnerAsociationModel = null;
		while(itr.hasNext()){
			tmpConcernPartnerAsociationModel = itr.next();
			
			//if partner is I8 then don't inactive this partner 

			Long currentUserAppUserId = UserUtils.getCurrentUser().getAppUserId();	
			AppUserConcernPartnerViewModel appUserConcernPartnerViewModel = this.appUserConcernPartnerViewDAO.findByPrimaryKey(currentUserAppUserId);
			if(appUserConcernPartnerViewModel.getConcernPartnerId().longValue() != tmpConcernPartnerAsociationModel.getAssociatedPartnerId().longValue()){
				//Inactive all other than I8
				tmpConcernPartnerAsociationModel.setActive(false);
			}		
			tmpConcernPartnerAsociationModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			tmpConcernPartnerAsociationModel.setUpdatedOn(new Date());			
			concernPartnerAssociationDAO.saveOrUpdate(tmpConcernPartnerAsociationModel);		
		}

		if(partnersAssociation != null){
			for(int i=0;partnersAssociation.length>i;i++ ){
				Long assocationKey = new Long(partnersAssociation[i]);
				ConcernPartnerAsociationModel concernPartnerAsociationModel = concernPartnerAssociationDAO.findByPrimaryKey(assocationKey);
				concernPartnerAsociationModel.setActive(true);
				concernPartnerAsociationModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				concernPartnerAsociationModel.setUpdatedOn(new Date());
				concernPartnerAssociationDAO.saveOrUpdate(concernPartnerAsociationModel);
			}
		}
		
		return baseWrapper;
	}		
	
	
	public void setConcernListViewDAO(ConcernListViewDAO concernListViewDAO) {
		this.concernListViewDAO = concernListViewDAO;
	}

	public void setConcernHistoryListViewDAO(
			ConcernHistoryListViewDAO concernHistoryListViewDAO) {
		this.concernHistoryListViewDAO = concernHistoryListViewDAO;
	}

	public void setConcernCategoryDAO(ConcernCategoryDAO concernCategoryDAO) {
		this.concernCategoryDAO = concernCategoryDAO;
	}

	public void setConcernDAO(ConcernDAO concernDAO) {
		this.concernDAO = concernDAO;
	}

	public void setConcernPartnerAssociationDAO(
			ConcernPartnerAssociationDAO concernPartnerAssociationDAO) {
		this.concernPartnerAssociationDAO = concernPartnerAssociationDAO;
	}

	public void setConcernPriorityDAO(ConcernPriorityDAO concernPriorityDAO) {
		this.concernPriorityDAO = concernPriorityDAO;
	}

	public void setConcernStatusDAO(ConcernStatusDAO concernStatusDAO) {
		this.concernStatusDAO = concernStatusDAO;
	}

	public void setConcernPartnerTypeDAO(ConcernPartnerTypeDAO concernPartnerTypeDAO) {
		this.concernPartnerTypeDAO = concernPartnerTypeDAO;
	}

	public void setConcernPartnerDAO(ConcernPartnerDAO concernPartnerDAO) {
		this.concernPartnerDAO = concernPartnerDAO;
	}

	public void setAppUserConcernPartnerViewDAO(
			AppUserConcernPartnerViewDAO appUserConcernPartnerViewDAO) {
		this.appUserConcernPartnerViewDAO = appUserConcernPartnerViewDAO;
	}

	public void setConcernsParentListViewDAO(
			ConcernsParentListViewDAO concernsParentListViewDAO) {
		this.concernsParentListViewDAO = concernsParentListViewDAO;
	}

	public void setConcernInActPartnerViewDAO(
			ConcernInActPartnerViewDAO concernInActPartnerViewDAO) {
		this.concernInActPartnerViewDAO = concernInActPartnerViewDAO;
	}

	public void setConcernPartnersRuleViewDAO(
			ConcernPartnersRuleViewDAO concernPartnersRuleViewDAO) {
		this.concernPartnersRuleViewDAO = concernPartnersRuleViewDAO;
	}

	public void setConcernOpHistoryListViewDAO(
			ConcernOpHistoryListViewDAO concernOpHistoryListViewDAO) {
		this.concernOpHistoryListViewDAO = concernOpHistoryListViewDAO;
	}

	public void setConcernOpRaisedToListViewDAO(
			ConcernOpRaisedToListViewDAO concernOpRaisedToListViewDAO) {
		this.concernOpRaisedToListViewDAO = concernOpRaisedToListViewDAO;
	}

}
