package com.inov8.microbank.webapp.action.retailermodule;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AgentTaggingChildrenModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserTypeModel;
import com.inov8.microbank.common.model.HandlerModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentTransactionModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentTransactionsVOModel;
import com.inov8.microbank.common.util.CryptographyType;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.facade.portal.ola.PortalOlaFacade;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.agentgroup.AgentTaggingManager;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountClosureManager;
import com.inov8.microbank.server.service.portal.ola.PortalOlaManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.util.EncryptionUtil;
import com.sun.tools.ws.wsdl.parser.DOMForest.Handler;

public class TaggedAgentTransactionsController extends BaseSearchController{

	protected final transient Log log = LogFactory.getLog(getClass());
	private PortalOlaFacade portalOlaFacade;
	AgentTaggingManager agentTaggingManager ;
	AppUserManager appUserManager;
	AccountManager accountManager;
	private PortalOlaManager portalOlaManager;
	HandlerManager handlerManager;
	RetailerContactManager retailerContactManager;
	MfsAccountClosureManager mfsAccountClosureManager;
	private ActionLogManager actionLogManager;

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setMfsAccountClosureManager(
			MfsAccountClosureManager mfsAccountClosureManager) {
		this.mfsAccountClosureManager = mfsAccountClosureManager;
	}

	public void setRetailerContactManager(
			RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}

	public void setHandlerManager(HandlerManager handlerManager) {
		this.handlerManager = handlerManager;
	}

	public void setPortalOlaManager(PortalOlaManager portalOlaManager) {
		this.portalOlaManager = portalOlaManager;
	}

	public TaggedAgentTransactionsController() {
	    super.setFilterSearchCommandClass(TaggedAgentTransactionsVOModel.class);
	}
	
		@Override
		protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
				Object model, HttpServletRequest request,
				LinkedHashMap<String, SortingOrder> sortingOrderMap)
				throws Exception {
			Map<String,Object> detailMap = new HashMap<>();
			String taggedAgentId= ServletRequestUtils.getStringParameter(request, "tID");
			String CDATE= ServletRequestUtils.getStringParameter(request, "cDate");
			String EDATE= ServletRequestUtils.getStringParameter(request, "eDate");
			request.setAttribute("cDate", CDATE);
			request.setAttribute("eDate", EDATE);
			List<TaggedAgentTransactionModel> resultList = new ArrayList<>();
			List<AgentTaggingChildrenModel> taggedAgentDetailList = new ArrayList<>();
			AgentTaggingChildrenModel agentTaggingChildrenModel = new AgentTaggingChildrenModel();
			AppUserModel appUserModel = new AppUserModel();
			AccountHolderModel  accountHolderModel = new AccountHolderModel();
			AccountModel accountModel = new AccountModel();
			RetailerContactModel  retailerContactModel = new RetailerContactModel();
		    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		    SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
		    BaseWrapper baseWrapper = new BaseWrapperImpl();
		    BaseWrapper bWrapper = new BaseWrapperImpl();
		    AgentTaggingChildrenModel aTaggingChildrenModel = new AgentTaggingChildrenModel();
		    TaggedAgentTransactionModel  tDetailMasterModel = new TaggedAgentTransactionModel();
		    TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel();
		  //  AgentTaggingChildrenModel ataggingChildrenModel = new AgentTaggingChildrenModel();
		    
			if(taggedAgentId !=null){
				agentTaggingChildrenModel.setUserId(taggedAgentId);
				searchBaseWrapper.setBasePersistableModel(agentTaggingChildrenModel);
				taggedAgentDetailList = (List<AgentTaggingChildrenModel>) agentTaggingManager.getTaggedAgentDetail(searchBaseWrapper).getCustomList().getResultsetList();
				if(taggedAgentDetailList != null && taggedAgentDetailList.size() >0){
					aTaggingChildrenModel = taggedAgentDetailList.get(0);
				}
				Long agentGroupTaggingId = aTaggingChildrenModel.getAgentTaggingId();
				request.setAttribute("agentTaggingId", agentGroupTaggingId);
				
				if( aTaggingChildrenModel.getAppUserModelId()!=null){
				appUserModel.setAppUserId(aTaggingChildrenModel.getAppUserModelId());
				baseWrapper.setBasePersistableModel(appUserModel);
				baseWrapper =  appUserManager.loadAppUser(baseWrapper);
				}
				
				Long hId;
				Long primaryRetailerId;
				HandlerModel handler= new HandlerModel();
				BaseWrapper br = new BaseWrapperImpl();
				if(baseWrapper.getBasePersistableModel() !=null){
				appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
					if(appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.HANDLER.longValue()){
					hId= appUserModel.getHandlerId();
					handler.setHandlerId(hId);
					br.setBasePersistableModel(handler);
					br = handlerManager.loadHandler(br);
						if(br.getBasePersistableModel()!=null){
					handler=(HandlerModel) br.getBasePersistableModel();
					primaryRetailerId = handler.getRetailerContactId();
					RetailerContactModel rcModel = new RetailerContactModel();
					rcModel.setRetailerContactId(primaryRetailerId);
					BaseWrapper bWrapper_ = new BaseWrapperImpl();
					bWrapper.setBasePersistableModel(rcModel);
					bWrapper = this.retailerContactManager.loadRetailerContact(bWrapper);
					if(bWrapper.getBasePersistableModel()!=null){
					retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
					   }
					}
						 if(retailerContactModel != null ){
								String taggedAgentBusinessName = retailerContactModel.getBusinessName();
								request.setAttribute("taggedAgentBusinessName", taggedAgentBusinessName);
				
								}
						  if(appUserModel!=null){
								String taggedAgentMobileNo = appUserModel.getMobileNo();
								request.setAttribute("taggedAgentMobileNo", taggedAgentMobileNo);
			
							} 
						  
					tDetailMasterModel.setHandlerId(Long.parseLong(taggedAgentId));
					request.setAttribute("balance", "");
				  }
				}
				if(appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.RETAILER.longValue()){
				retailerContactModel = appUserModel.getRetailerContactIdRetailerContactModel();

			    BaseWrapper basWrapper = new BaseWrapperImpl();
			    basWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
		      	basWrapper.setBasePersistableModel(appUserModel);

				ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(basWrapper);
				ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
				Double bal;
				try
				{
				 bal= mfsAccountClosureManager.checkAgentBalance(appUserModel);
				}
				catch(Exception e){
					bal = 0.0D;
				}
				
				actionLogModel.setCustomField1(appUserModel.getRetailerContactId().toString());
				actionLogModel.setUsecaseId((Long)basWrapper.getObject(PortalConstants.KEY_USECASE_ID));
		    	this.actionLogManager.completeActionLog(actionLogModel);

							request.setAttribute("balance", bal);	
				
						         if(retailerContactModel != null ){
									String taggedAgentBusinessName = retailerContactModel.getBusinessName();
									request.setAttribute("taggedAgentBusinessName", taggedAgentBusinessName);
					
									}
						             if(appUserModel!=null){
										String taggedAgentMobileNo = appUserModel.getMobileNo();
										request.setAttribute("taggedAgentMobileNo", taggedAgentMobileNo);
					
									} 
		
				
				tDetailMasterModel.setAgentId(taggedAgentId);
}
				
				
				request.setAttribute("tAgentId", taggedAgentId);
				String appID= ServletRequestUtils.getStringParameter(request, "pkid");
				request.setAttribute("appUserId", appID);
			
				SearchBaseWrapper sBWrapper = new SearchBaseWrapperImpl(); 
				
				 String eDate=null;
				  if(EDATE!=null || EDATE!="")
				  {
				  eDate = EDATE.substring(0, EDATE.length()-1).trim();
				  }
				  Date SDate=null ;
				  Date EDate=null ;
				  if(CDATE != ""){
					  SDate = PortalDateUtils.parseStringAsDate(CDATE, PortalDateUtils.SHORT_DATE_TIME_FORMAT);
					  SDate.setHours(0);
					 
				  }
				  if(eDate.length() > 0){
					  EDate = PortalDateUtils.parseStringAsDate(EDATE, PortalDateUtils.SHORT_DATE_TIME_FORMAT);
					  EDate.setHours(23);
					  EDate.setMinutes(59);
					  EDate.setSeconds(59);
				  }
				  
			  DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("abc",SDate,EDate);

			  sBWrapper.setDateRangeHolderModel( dateRangeHolderModel );
			  sBWrapper.setBasePersistableModel(tDetailMasterModel);
			  sBWrapper.setPagingHelperModel(pagingHelperModel);
			  sBWrapper = portalOlaManager.searchTaggedAgentTransactionDetail(sBWrapper);
			  CustomList cList = sBWrapper.getCustomList();
			  if(cList != null && cList.getResultsetList() != null && cList.getResultsetList().size() > 0){
				  resultList = cList.getResultsetList();
				  pagingHelperModel.setTotalRecordsCount(resultList.size());
		        	detailMap.put("taggedAgentTransactionsList",resultList);
			  }else{
		        	pagingHelperModel.setTotalRecordsCount(0);
		        	detailMap.put("taggedAgentTransactionsList",resultList);
		        }
			}
			return new ModelAndView("taggedAgentTransactions",detailMap);
		}
			
		
		
		 private void decryptTaggedAgentsListViewModels(AccountModel accountModel)
		    {
		          	try
		        	{
	         		EncryptionUtil.docryptFields( CryptographyType.DECRYPT, accountModel, "balance");
					
		        	}
	            
	         
	         catch( Exception e )
	         {
	             log.error( e.getMessage(), e );
	         }
		        	
		        
		    }
		 
		 public void setAccountManager(AccountManager accountManager) {
				this.accountManager = accountManager;
			}
			public void setAppUserManager(AppUserManager appUserManager) {
				this.appUserManager = appUserManager;
			}
			public void setAgentTaggingManager(AgentTaggingManager agentTaggingManager) {
				this.agentTaggingManager = agentTaggingManager;
			}
			public void setPortalOlaFacade(PortalOlaFacade portalOlaFacade) {
				this.portalOlaFacade = portalOlaFacade;
			}

}
