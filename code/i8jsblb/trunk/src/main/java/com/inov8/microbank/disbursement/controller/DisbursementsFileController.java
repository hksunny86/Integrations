package com.inov8.microbank.disbursement.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkManualAdjustmentRefDataModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.disbursement.vo.BulkDisbursementsVOModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.disbursement.util.DisbursementStatusConstants;
import com.inov8.microbank.disbursement.model.BulkDisbursementsFileInfoModel;
import com.inov8.microbank.disbursement.service.DisbursementFileProcessor;
import com.inov8.microbank.server.dao.servicemodule.ServiceDAO;
import com.inov8.microbank.server.facade.ProductFacade;
import com.inov8.microbank.disbursement.service.BulkDisbursementsManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.xml.XmlMarshaller;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.microbank.webapp.action.portal.manualadjustmentmodule.BulkManualAdjustmentVOModel;
import com.inov8.ola.server.dao.account.AccountDAO;
import com.inov8.ola.server.dao.ledger.LedgerDAO;
import com.inov8.ola.server.dao.limit.LimitDAO;
import com.inov8.ola.server.service.limit.LimitManager;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("deprecation")
public class DisbursementsFileController extends AdvanceFormController {

	private final static Log logger = LogFactory.getLog(DisbursementsFileController.class);
	
	private BulkDisbursementsManager bulkDisbursementsManager;
	private MessageSource messageSource;
	private SmsSender smsSender;
	private MfsAccountManager mfsAccountManager;
	private AccountDAO accountDAO;
	private LedgerDAO ledgerDAO;
	private LimitManager limitManager;
	private ReferenceDataManager referenceDataManager;
	private ProductFacade productFacade;
	private ServiceDAO serviceDAO;
	private XmlMarshaller<BulkDisbursementsFileInfoModel> bulkDisbursementMarshaller;

	public DisbursementsFileController(){
		setCommandName("bulkDisbursementsVOModel");
	    setCommandClass(BulkDisbursementsVOModel.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<String,Object>(2);

		ServiceModel serviceModel = new ServiceModel();
		serviceModel.setServiceTypeId(ServiceTypeConstantsInterface.SERVICE_TYPE_BULK_PAYMENTS);
		
        referenceDataMap.put("serviceModelList", serviceDAO.getServiceLabels(ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER, ServiceConstantsInterface.BULK_DISB_ACC_HOLDER));

		AppUserTypeModel appUserTypeModel = new AppUserTypeModel();
		ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl(appUserTypeModel, "name", SortingOrder.ASC);
		refDataWrapper = referenceDataManager.getReferenceData(refDataWrapper,UserTypeConstantsInterface.CUSTOMER,
				UserTypeConstantsInterface.RETAILER);

		referenceDataMap.put("appUserTypeList",refDataWrapper.getReferenceDataList());

		String pathSettlementFile = localFilePath();

		File folder = new File(pathSettlementFile);
		String array[] = new String[]{};
		if(folder.exists()) {
			array = folder.list();
		}

		referenceDataMap.put("bulkDisbursementsFilePathList", Arrays.asList(array));

		return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest request)
			throws Exception {
		return new BulkDisbursementsVOModel();
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		return onUpdate(request, response, command, errors);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception
	{
		BulkDisbursementsVOModel bulkDisbursementsVOModel = (BulkDisbursementsVOModel) command;
		try {
//			BulkDisbursementsVOModel bulkDisbursementsVOModel = (BulkDisbursementsVOModel) command;

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(new ProductModel(bulkDisbursementsVOModel.getProductId()));
			productFacade.loadProduct(baseWrapper);

			ProductModel productModel = (ProductModel) baseWrapper.getBasePersistableModel();

			bulkDisbursementsVOModel.setProductName(productModel.getName());
			bulkDisbursementsVOModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			bulkDisbursementsVOModel.setBatchNumber(String.valueOf(getBatchNo()));

			File file = getFile(bulkDisbursementsVOModel);

			BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel = new BulkDisbursementsFileInfoModel();
			bulkDisbursementsFileInfoModel.setServiceId(bulkDisbursementsVOModel.getServiceId());
			bulkDisbursementsFileInfoModel.setProductId(bulkDisbursementsVOModel.getProductId());

			Long appUserTypeId = bulkDisbursementsVOModel.getAppUserTypeId();
			if (ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER.longValue() == bulkDisbursementsVOModel.getServiceId())
				appUserTypeId = UserTypeConstantsInterface.WALKIN_CUSTOMER;

			else if (appUserTypeId == null)
				appUserTypeId = UserTypeConstantsInterface.CUSTOMER;

			bulkDisbursementsFileInfoModel.setAppUserTypeId(appUserTypeId);
			bulkDisbursementsFileInfoModel.setBatchNumber(bulkDisbursementsVOModel.getBatchNumber());
			bulkDisbursementsFileInfoModel.setFileName(file.getName());
			bulkDisbursementsFileInfoModel.setFilePath(file.getAbsolutePath());
			bulkDisbursementsFileInfoModel.setStatus(DisbursementStatusConstants.STATUS_UN_PARSED);
			bulkDisbursementsFileInfoModel.setCreatedOn(new Date());
			bulkDisbursementsFileInfoModel.setUpdatedOn(new Date());
			bulkDisbursementsFileInfoModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			bulkDisbursementsFileInfoModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			bulkDisbursementsFileInfoModel.setFedPerFile(bulkDisbursementsVOModel.getFedPerFile());
			bulkDisbursementsFileInfoModel.setChargesPerFile(bulkDisbursementsVOModel.getChargesPerFile());
			bulkDisbursementsFileInfoModel.setIsCoreSumAccountNumber(bulkDisbursementsVOModel.getAccountType() == 2);
			bulkDisbursementsFileInfoModel.setSourceAccountNumber(bulkDisbursementsVOModel.getSourceACNo());
//		to do set account nick

//		DisbursementFileProcessor fileProcessor = new DisbursementFileProcessor(bulkDisbursementsFileInfoModel,
//				bulkDisbursementsVOModel, mfsAccountManager, bulkDisbursementsManager, accountDAO, ledgerDAO, limitManager);
//		new Thread(fileProcessor).start();

			baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(bulkDisbursementsFileInfoModel);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.UPDATE_BULK_DISBURSEMENT_USECASE_ID);
//		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.UPDATE_BULK_DISBURSEMENT_USECASE_ID );

			populateAuthenticationParams(baseWrapper, request, bulkDisbursementsFileInfoModel);
			bulkDisbursementsManager.saveBulkDisbursementWithAuthorization(baseWrapper);

			String actionAuthId = (String.valueOf(baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_ACTION_AUTH_ID)));
			if (!actionAuthId.equals("") && !actionAuthId.equals("null")) {
				if (baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG).equals("Action authorization request already exist with  Action ID")) {
					super.saveMessage(request, " Action authorization request already exist with  Action ID :" + actionAuthId.toString());
				} else {
					bulkDisbursementsVOModel.setIsApproved("0");
					bulkDisbursementsFileInfoModel.setIsApproved("0");
					DisbursementFileProcessor fileProcessor = new DisbursementFileProcessor(bulkDisbursementsFileInfoModel,
							bulkDisbursementsVOModel, mfsAccountManager, bulkDisbursementsManager, accountDAO, ledgerDAO, limitManager);
					new Thread(fileProcessor).start();
					super.saveMessage(request, " Action is pending for approval against reference Action ID :" + actionAuthId.toString());
				}
			} else {
				bulkDisbursementsVOModel.setIsApproved("1");
				bulkDisbursementsFileInfoModel.setIsApproved("1");
				DisbursementFileProcessor fileProcessor = new DisbursementFileProcessor(bulkDisbursementsFileInfoModel,
						bulkDisbursementsVOModel, mfsAccountManager, bulkDisbursementsManager, accountDAO, ledgerDAO, limitManager);
				new Thread(fileProcessor).start();
				super.saveMessage(request, " Batch # " + bulkDisbursementsVOModel.getBatchNumber() + " is being processed. You may search records after a while.");
			}
		}
		catch (FrameworkCheckedException ex){
			if(ex.getMessage().contains("Action authorization request already exist with  Action ID")) {
				super.saveMessage(request, "Action authorization request already exist");
			}
			else{
				super.saveMessage(request, "Your request cannot be processed in the moment. Please try again later.");
			}
			return new ModelAndView("redirect:p_bulkdisbursements.html?bulkUpload=1?batchNumber="+bulkDisbursementsVOModel.getBatchNumber());
		}
		return new ModelAndView("redirect:p_disbursementFileInfo.html?bulkUpload=1?batchNumber="+bulkDisbursementsVOModel.getBatchNumber());
	}

	@Override
	protected void populateAuthenticationParams(BaseWrapper baseWrapper, HttpServletRequest req, Object model) throws FrameworkCheckedException {
		BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel = (BulkDisbursementsFileInfoModel) model;
		ObjectMapper mapper = new ObjectMapper();
		Long actionAuthorizationId =null;
		try
		{
			actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"authId");
		} catch (ServletRequestBindingException e1) {
			e1.printStackTrace();
		}

		DateFormat df = new SimpleDateFormat(PortalDateUtils.SHORT_DATE_FORMAT_2);
		mapper.setDateFormat(df);

		String modelJsonString = null;
		String xml = null;

		XStream xstream = new XStream();
		bulkDisbursementsFileInfoModel = (BulkDisbursementsFileInfoModel) bulkDisbursementsFileInfoModel.clone();
		bulkDisbursementsFileInfoModel.setBatchNumber(bulkDisbursementsFileInfoModel.getBatchNumber());
		bulkDisbursementsFileInfoModel.setCreatedOn(new Date());
//			mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		modelJsonString= xstream.toXML(bulkDisbursementsFileInfoModel);

//			modelJsonString = mapper.writeValueAsString(bulkDisbursementsFileInfoModel.getBatchNumber());


//			xml = bulkDisbursementMarshaller.marshal(bulkDisbursementsFileInfoModel);

		//		baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME, MessageUtil.getMessage("BulkDisbursementsVOModel.methodName"));
//		baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS, BulkDisbursementsFileInfoModel.class.getSimpleName());
//		baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME, BulkDisbursementsFileInfoModel.class.getName());
		baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
//		baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME, MessageUtil.getMessage("BulkDisbursementsVOModel.Manager"));
//		baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID, ThreadLocalAppUser.getAppUserModel().getAppUserId().toString());

//		baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
		//baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.MARK_BLACKLISTED_BULK_USECASE_ID );
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID,PortalConstants.ACTION_UPDATE);
//		baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME, this.getFormView());
	}


	private File getFile(BulkDisbursementsVOModel bulkDisbursementsVOModel) throws Exception {

		File file = null;

		if(bulkDisbursementsVOModel.getFileSourceFTP()) {

			String ftpFolderPath = MessageUtil.getMessage("path.bulk.disbursements.ftp");

			String filePath = bulkDisbursementsVOModel.getBulkDisbursementsFilePath();
			file = new File(ftpFolderPath + filePath);

		} else {

			MultipartFile csvFile = bulkDisbursementsVOModel.getCsvFile();

			if(!csvFile.getName().equals("csvFile") ) {
				throw new Exception("File must be in csv format.");
			}

			String upLoadedFolderPath = localFilePath();

			//below line is comment because this is for development enviroment
//	       File f = new File("F:/opt/BulkDisbursements");

//			file = new File(upLoadedFolderPath + bulkDisbursementsVOModel.getBatchNumber() + String.valueOf(bulkDisbursementsVOModel.getProductId()) +".csv");
			file = new File( bulkDisbursementsVOModel.getBatchNumber() + String.valueOf(bulkDisbursementsVOModel.getProductId()) +".csv");

			csvFile.transferTo(file);
		}

		return file;
	}

	private String localFilePath() {
		return MessageUtil.getMessage("path.bulk.disbursements.folder");
	}

	private Long getBatchNo() {
		
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHmmss");
        String transdatetime = format.format(new Date());
        return Long.parseLong(transdatetime);
	}

	private BulkDisbursementsVOModel populateBulkDisbursementsVOModel(BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel) {
		BulkDisbursementsVOModel bulkDisbursementsVOModel = new BulkDisbursementsVOModel();
		bulkDisbursementsVOModel.setBatchNumber(bulkDisbursementsFileInfoModel.getBatchNumber());
		bulkDisbursementsVOModel.setCreatedOn(bulkDisbursementsFileInfoModel.getCreatedOn());
		return bulkDisbursementsVOModel;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public SmsSender getSmsSender() {
		return smsSender;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public BulkDisbursementsManager getBulkDisbursementsManager() {
		return bulkDisbursementsManager;
	}

	public void setBulkDisbursementsManager(BulkDisbursementsManager bulkDisbursementsManager) {
		this.bulkDisbursementsManager = bulkDisbursementsManager;
	}
	
	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}

	public void setProductFacade(ProductFacade productFacade) {
		this.productFacade = productFacade;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
	public void setServiceDAO(ServiceDAO serviceDAO) {
		this.serviceDAO = serviceDAO;
	}

	public void setAccountDAO(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}

	public void setLedgerDAO(LedgerDAO ledgerDAO) {
		this.ledgerDAO = ledgerDAO;
	}
	public void setLimitManager(LimitManager limitManager) {
		this.limitManager = limitManager;
	}

	public void setBulkDisbursementMarshaller(XmlMarshaller<BulkDisbursementsFileInfoModel> bulkDisbursementMarshaller) {
		this.bulkDisbursementMarshaller = bulkDisbursementMarshaller;
	}
}
