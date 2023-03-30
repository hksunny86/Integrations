package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

/*
 * Author : Hassan Javaid
 * Date   : 27-08-2014
 * Module : Action Authorization
 * Project: Mircobank
 * */

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.customermodule.BlinkCustomerPictureModel;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthPictureModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.server.dao.customermodule.BlinkCustomerModelDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;
import com.inov8.microbank.server.dao.portal.citymodule.CityDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.bulkdisbursements.CustomerPendingTrxManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class MfsAccountAuthorizationDetailController extends AdvanceAuthorizationFormController {


    private static final Logger LOGGER = Logger.getLogger(MfsAccountAuthorizationDetailController.class);
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private MfsAccountManager mfsAccountManager;
    private CustomerPendingTrxManager customerPendingTrxManager;
    private CommonCommandManager commonCommandManager;
    private ReferenceDataManager referenceDataManager;
    private ESBAdapter esbAdapter;
    private AppUserDAO appUserDAO;
    private BlinkCustomerModelDAO blinkCustomerModelDAO;
    private CustomerDAO customerDAO;
    private Long usecaseId;
    private CityDAO cityDAO;
    private Long appUserId;
    private String accTypeId;


    public MfsAccountAuthorizationDetailController() {
        setCommandName("actionAuthorizationModel");
        setCommandClass(ActionAuthorizationModel.class);
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    public String findPath(MfsAccountModel mfsAccountModel, ActionAuthorizationModel actionAuthorizationModel, Long ptc, String namedPic) throws FrameworkCheckedException, IOException {
        BlinkCustomerModel customerModelList = blinkCustomerModelDAO.loadBlinkCustomerModelByMobileAndAccUpdate(mfsAccountModel.getMobileNo(), 1L);
        AppUserModel appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(customerModelList.getMobileNo());
        appUserId = appUserModel.getAppUserId();
        BlinkCustomerPictureModel customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                ptc, customerModelList.getCustomerId());
        String filePath = "";
        if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
            InputStream in = new ByteArrayInputStream(customerPictureModel.getPicture());
            ImageInputStream iis = ImageIO.createImageInputStream(in);

            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
            String fileFormat = "";
            while (imageReaders.hasNext()) {
                ImageReader reader = (ImageReader) imageReaders.next();
                System.out.printf("formatName: %s%n", reader.getFormatName());
                fileFormat = reader.getFormatName();
            }
            //generating path for bytes to write on
            filePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/" + namedPic + actionAuthorizationModel.getActionAuthorizationId() + "." + "gif";
        }

        return filePath;

    }

    @Override
    protected Map loadReferenceData(HttpServletRequest request)
            throws Exception {
        Map<String, List<?>> referenceDataMap = new HashMap<String, List<?>>();
        boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
        boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);
        String accType = ServletRequestUtils.getStringParameter(request, "accTypeId");

        if (escalateRequest || resolveRequest) {
            ActionStatusModel actionStatusModel = new ActionStatusModel();
            ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl(actionStatusModel, "name", SortingOrder.ASC);
            referenceDataManager.getReferenceData(refDataWrapper);
            List<ActionStatusModel> actionStatusModelList;
            actionStatusModelList = refDataWrapper.getReferenceDataList();
            List<ActionStatusModel> tempActionStatusModelList = new ArrayList<>();

            for (ActionStatusModel actionStatusModel2 : actionStatusModelList) {
                if (((actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.intValue())
                        || (actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL.intValue())
                        || (actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.intValue())
                        || (actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.intValue())
                        || (actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.intValue())) && escalateRequest)
                    tempActionStatusModelList.add(actionStatusModel2);
                else if ((actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED.intValue()) && resolveRequest)
                    tempActionStatusModelList.add(actionStatusModel2);
            }
            referenceDataMap.put("actionStatusModel", tempActionStatusModelList);

            ////// Action Authorization history////
            Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
            if (actionAuthorizationId == null) {
                BlinkCustomerModel blinkCustomerModel = commonCommandManager.loadBlinkCustomerByBlinkCustomerId(Long.valueOf(accType));
                actionAuthorizationId = blinkCustomerModel.getActionAuthorizationId();
            }
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);

            ActionAuthorizationHistoryModel actionAuthorizationHistoryModel = new ActionAuthorizationHistoryModel();
            actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);

            List<ActionAuthorizationHistoryModel> actionAuthorizationHistoryModelList;

            refDataWrapper = new ReferenceDataWrapperImpl(actionAuthorizationHistoryModel, "escalationLevel", SortingOrder.ASC);
            referenceDataManager.getReferenceData(refDataWrapper);

            actionAuthorizationHistoryModelList = refDataWrapper.getReferenceDataList();

            referenceDataMap.put("actionAuthorizationHistoryModelList", actionAuthorizationHistoryModelList);

            if (actionAuthorizationModel.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
                    && actionAuthorizationModel.getCreatedById().longValue() == UserUtils.getCurrentUser().getAppUserId()) {
                boolean isAssignedBack = false;
                isAssignedBack = true;
                request.setAttribute("isAssignedBack", isAssignedBack);
            }
        }
        return referenceDataMap;
    }


    @Override
    protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
//        accTypeId = ServletRequestUtils.getStringParameter(request, "accTypeId");
        boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
        boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);
        String accType = ServletRequestUtils.getStringParameter(request, "accTypeId");

        if (escalateRequest || resolveRequest) {
            Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
            if (actionAuthorizationId == null) {
                BlinkCustomerModel blinkCustomerModel = commonCommandManager.loadBlinkCustomerByBlinkCustomerId(Long.valueOf(accType));
                actionAuthorizationId = blinkCustomerModel.getActionAuthorizationId();
            }
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);

            XStream xstream = new XStream();
            MfsAccountModel mfsAccountModel = (MfsAccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
            request.setAttribute("mfsAccountModel", mfsAccountModel);
            usecaseId = mfsAccountModel.getUsecaseId();

            /***
             * Populating Customer Source of Funds
             */
            if (null == mfsAccountModel.getFundSourceName()) {
                if (mfsAccountModel.getUsecaseId() == PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {
                    mfsAccountModel.setFundsSourceId(null);
                } else {
                    String[] fundSourceName = new String[mfsAccountModel.getFundsSourceId().length];
                    fundSourceName = mfsAccountManager.getFundSourceName(mfsAccountModel.getFundsSourceId());
                    mfsAccountModel.setFundSourceName(fundSourceName);
                }
            } else {
                mfsAccountModel.setFundSourceName(mfsAccountModel.getFundSourceName());
            }
            if (mfsAccountModel.getBirthPlace() != null && mfsAccountModel.getBirthPlaceName() == null)
                mfsAccountModel.setBirthPlaceName(mfsAccountModel.getBirthPlace());

            if (mfsAccountModel.getCity() != null && NumberUtils.isNumber(mfsAccountModel.getCity())) {


                CityModel cityModel = new CityModel();
                cityModel.setCityId(Long.parseLong(mfsAccountModel.getCity()));
                ReferenceDataWrapper cityWrapper = new ReferenceDataWrapperImpl();
                cityWrapper.setBasePersistableModel(cityModel);
                cityWrapper = referenceDataManager.getReferenceData(cityWrapper, Long.parseLong(mfsAccountModel.getCity()));
                List<CityModel> cityList = cityWrapper.getReferenceDataList();
                if (cityList != null && !cityList.isEmpty())
                    mfsAccountModel.setCity(cityList.get(0).getName());

            }
            request.setAttribute("mfsAccountModel", mfsAccountModel);

            Long[] regStateList = {RegistrationStateConstantsInterface.DECLINE, RegistrationStateConstantsInterface.DISCREPANT,
                    RegistrationStateConstantsInterface.VERIFIED, RegistrationStateConstants.BLOCKED, RegistrationStateConstants.CLSPENDING};
            List<RegistrationStateModel> regStates = commonCommandManager.getRegistrationStateByIds(regStateList).getResultsetList();

            for (RegistrationStateModel registrationStateModel : regStates) {
                if (null != mfsAccountModel.getRegistrationStateId() && (mfsAccountModel.getRegistrationStateId().longValue() == registrationStateModel.getRegistrationStateId().longValue()))
                    mfsAccountModel.setRegStateName(registrationStateModel.getName());
            }


            SegmentModel segmentModel = new SegmentModel();
            segmentModel.setIsActive(true);
            ReferenceDataWrapper segmentReferenceDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
            segmentReferenceDataWrapper.setBasePersistableModel(segmentModel);
            referenceDataManager.getReferenceData(segmentReferenceDataWrapper);

            List<SegmentModel> segmentList = null;
            if (segmentReferenceDataWrapper.getReferenceDataList() != null) {
                segmentList = segmentReferenceDataWrapper.getReferenceDataList();

                for (SegmentModel segmentModel2 : segmentList) {

                    if (null != mfsAccountModel.getSegmentId() && (mfsAccountModel.getSegmentId().longValue() == segmentModel2.getSegmentId().longValue()))
                        mfsAccountModel.setSegmentNameStr(segmentModel2.getName());
                }
            }

            OlaCustomerAccountTypeModel customerAccountTypeModel = new OlaCustomerAccountTypeModel();
            customerAccountTypeModel.setActive(true);
            customerAccountTypeModel.setIsCustomerAccountType(true); //added by Turab
            ReferenceDataWrapper customerAccountTypeDataWrapper = new ReferenceDataWrapperImpl(customerAccountTypeModel, "name", SortingOrder.ASC);
            customerAccountTypeDataWrapper.setBasePersistableModel(customerAccountTypeModel);
            referenceDataManager.getReferenceData(customerAccountTypeDataWrapper);


            CopyOnWriteArrayList<OlaCustomerAccountTypeModel> customerAccountTypeList = null;
            if (customerAccountTypeDataWrapper.getReferenceDataList() != null) {
                customerAccountTypeList = new CopyOnWriteArrayList<OlaCustomerAccountTypeModel>(customerAccountTypeDataWrapper.getReferenceDataList());
                if (!CollectionUtils.isEmpty(customerAccountTypeList)) {
                    //remove special account types from screen. like settlemnt account type is used for commission settlemnt in OLA and walkin customer. it needs to be removed
                    //because it is for system use only.
                    removeSpecialAccountTypes(customerAccountTypeList);

                    for (OlaCustomerAccountTypeModel olaCustomerAccountTypeModel : customerAccountTypeList) {

                        if (null != mfsAccountModel.getCustomerAccountTypeId() && (mfsAccountModel.getCustomerAccountTypeId().longValue() == olaCustomerAccountTypeModel.getCustomerAccountTypeId().longValue()))
                            mfsAccountModel.setCustomerAccountName(olaCustomerAccountTypeModel.getName());
                    }
                }

            }

            request.setAttribute("mfsAccountModel", mfsAccountModel);

            //Populating authorization pictures
            String authfilePath = null;
            FileOutputStream fops = null;

            authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/customerPic_" + actionAuthorizationId + ".gif";
            fops = new FileOutputStream(authfilePath);
            ActionAuthPictureModel authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.CUSTOMER_PHOTO);
            if (authPictureModel != null && authPictureModel.getPicture() != null) {
                fops.write(authPictureModel.getPicture());
            }
            fops.flush();
            fops.close();

            authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/parentCnicPic_" + actionAuthorizationId + ".gif";
            fops = new FileOutputStream(authfilePath);
            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PARENT_CNIC_SNAPSHOT);
            if (authPictureModel != null && authPictureModel.getPicture() != null) {
                fops.write(authPictureModel.getPicture());
            }
            fops.flush();
            fops.close();

            authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/bFormPic_" + actionAuthorizationId + ".gif";
            fops = new FileOutputStream(authfilePath);
            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.B_FORM_SNAPSHOT);
            if (authPictureModel != null && authPictureModel.getPicture() != null) {
                fops.write(authPictureModel.getPicture());
            }
            fops.flush();
            fops.close();

            authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/parentCnicBackPic_" + actionAuthorizationId + ".gif";
            fops = new FileOutputStream(authfilePath);
            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT);
            if (authPictureModel != null && authPictureModel.getPicture() != null) {
                fops.write(authPictureModel.getPicture());
            }
            fops.flush();
            fops.close();

            authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/tncPic_" + actionAuthorizationId + ".gif";
            fops = new FileOutputStream(authfilePath);
            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.TERMS_AND_CONDITIONS_COPY);
            if (authPictureModel != null && authPictureModel.getPicture() != null) {
                fops.write(authPictureModel.getPicture());
            }
            fops.flush();
            fops.close();

            authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/signPic_" + actionAuthorizationId + ".gif";
            fops = new FileOutputStream(authfilePath);
            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.SIGNATURE_SNAPSHOT);

            if (authPictureModel != null && authPictureModel.getPicture() != null) {
                fops.write(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.SIGNATURE_SNAPSHOT).getPicture());
            }

            fops.flush();
            fops.close();

            authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/cnicFrontPic_" + actionAuthorizationId + ".gif";
            fops = new FileOutputStream(authfilePath);
            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_FRONT_SNAPSHOT);
            if (authPictureModel != null && authPictureModel.getPicture() != null) {
                fops.write(authPictureModel.getPicture());
            }
            fops.flush();
            fops.close();

            authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/cnicBackPic_" + actionAuthorizationId + ".gif";
            fops = new FileOutputStream(authfilePath);
            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_BACK_SNAPSHOT);
            if (authPictureModel != null && authPictureModel.getPicture() != null) {
                fops.write(authPictureModel.getPicture());
            }
            fops.flush();
            fops.close();

            authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/sourceOfIncomePic_" + actionAuthorizationId + ".gif";
            fops = new FileOutputStream(authfilePath);
            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT);
            if (authPictureModel != null && authPictureModel.getPicture() != null) {
                fops.write(authPictureModel.getPicture());
            }
            fops.flush();
            fops.close();

            authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/proofOfProfessionPic_" + actionAuthorizationId + ".gif";
            fops = new FileOutputStream(authfilePath);
            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT);
            if (authPictureModel != null && authPictureModel.getPicture() != null) {
                fops.write(authPictureModel.getPicture());
            }
            fops.flush();
            fops.close();


            //End Populating authorization pictures

            if (actionAuthorizationModel.getUsecaseId().longValue() == PortalConstants.MFS_ACCOUNT_UPDATE_USECASE_ID ||
                    actionAuthorizationModel.getUsecaseId().longValue() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                MfsAccountModel currentMfsAccountModel = populateCurrentInfoModel(mfsAccountModel.getAppUserId());
                request.setAttribute("currentMfsAccountModel", currentMfsAccountModel);

                BaseWrapper baseWrapper = new BaseWrapperImpl();
                AppUserModel appUserModel = new AppUserModel();
                appUserModel.setAppUserId(mfsAccountModel.getAppUserId());
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
                appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
                CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
                mfsAccountModel.setInitialDeposit(customerModel.getInitialDeposit());
                CustomerPictureModel customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                        PictureTypeConstants.CUSTOMER_PHOTO, customerModel.getCustomerId().longValue());

                if(mfsAccountModel.getUsecaseId() != null && mfsAccountModel.getUsecaseId() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
                            PictureTypeConstants.CUSTOMER_PHOTO, customerModel.getCustomerId().longValue());
                }

                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.CUSTOMER_PHOTO) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        String filePath = getServletContext().getRealPath("images") + "/upload_dir/customerPic_" + mfsAccountModel.getAppUserId() + ".gif";
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                        if (actionAuthorizationModel.getUsecaseId().longValue() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                            mfsAccountModel.setCustomerPicDiscrepant(mfsAccountModel.getCustomerPicDiscrepant());
                        } else {
                            mfsAccountModel.setCustomerPicDiscrepant(customerPictureModel.getDiscrepant());
                        }
                    }
                }

                if (actionAuthorizationModel.getUsecaseId().longValue() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
                            PictureTypeConstants.PARENT_CNIC_SNAPSHOT, customerModel.getCustomerId().longValue());

                    if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PARENT_CNIC_SNAPSHOT) {
                        if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                            String filePath = getServletContext().getRealPath("images") + "/upload_dir/parentCnicPic_" + mfsAccountModel.getAppUserId() + ".gif";
                            FileOutputStream fos = new FileOutputStream(filePath);
                            fos.write(customerPictureModel.getPicture());
                            fos.flush();
                            fos.close();
                            logger.info("Picture Extracted : " + filePath);
                            mfsAccountModel.setParentCnicPicDiscrepant(mfsAccountModel.getParentCnicPicDiscrepant());
                        }
                    }

                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
                            PictureTypeConstants.B_FORM_SNAPSHOT, customerModel.getCustomerId().longValue());

                    if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.B_FORM_SNAPSHOT) {
                        if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                            String filePath = getServletContext().getRealPath("images") + "/upload_dir/bFormPic_" + mfsAccountModel.getAppUserId() + ".gif";
                            FileOutputStream fos = new FileOutputStream(filePath);
                            fos.write(customerPictureModel.getPicture());
                            fos.flush();
                            fos.close();
                            logger.info("Picture Extracted : " + filePath);
                            mfsAccountModel.setbFormPicDiscrepant(mfsAccountModel.getbFormPicDiscrepant());
                        }
                    }

                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
                            PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());

                    if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT) {
                        if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                            String filePath = getServletContext().getRealPath("images") + "/upload_dir/parentCnicBackPic_" + mfsAccountModel.getAppUserId() + ".gif";
                            FileOutputStream fos = new FileOutputStream(filePath);
                            fos.write(customerPictureModel.getPicture());
                            fos.flush();
                            fos.close();
                            logger.info("Picture Extracted : " + filePath);
                            mfsAccountModel.setParentCnicBackPicDiscrepant(mfsAccountModel.getParentCnicBackPicDiscrepant());
                        }
                    }
                }

                if (actionAuthorizationModel.getUsecaseId().longValue() != PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                            PictureTypeConstants.TERMS_AND_CONDITIONS_COPY, customerModel.getCustomerId().longValue());

                    if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.TERMS_AND_CONDITIONS_COPY) {
                        if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                            String filePath = getServletContext().getRealPath("images") + "/upload_dir/tncPic_" + mfsAccountModel.getAppUserId() + ".gif";
                            FileOutputStream fos = new FileOutputStream(filePath);
                            fos.write(customerPictureModel.getPicture());
                            fos.flush();
                            fos.close();
                            logger.info("Picture Extracted : " + filePath);
                            mfsAccountModel.setTncPicDiscrepant(customerPictureModel.getDiscrepant());
                        }
                    }

                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                            PictureTypeConstants.SIGNATURE_SNAPSHOT, customerModel.getCustomerId().longValue());

                    if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SIGNATURE_SNAPSHOT) {
                        if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                            String filePath = getServletContext().getRealPath("images") + "/upload_dir/signPic_" + mfsAccountModel.getAppUserId() + ".gif";
                            FileOutputStream fos = new FileOutputStream(filePath);
                            fos.write(customerPictureModel.getPicture());
                            fos.flush();
                            fos.close();
                            logger.info("Picture Extracted : " + filePath);
                            mfsAccountModel.setSignPicDiscrepant(customerPictureModel.getDiscrepant());
                        }
                    }
                }

                if(actionAuthorizationModel.getUsecaseId().longValue() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID){
                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
                            PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModel.getCustomerId().longValue());
                }
                else {
                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                            PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModel.getCustomerId().longValue());
                }
                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_FRONT_SNAPSHOT) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        String filePath = getServletContext().getRealPath("images") + "/upload_dir/cnicFrontPic_" + mfsAccountModel.getAppUserId() + ".gif";
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                        if (actionAuthorizationModel.getUsecaseId().longValue() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                            mfsAccountModel.setCnicFrontPicDiscrepant(mfsAccountModel.getCnicFrontPicDiscrepant());
                        } else {
                            mfsAccountModel.setCnicFrontPicDiscrepant(customerPictureModel.getDiscrepant());
                        }
                    }
                }

                if(actionAuthorizationModel.getUsecaseId().longValue() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID){
                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
                            PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());
                }
                else {
                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                            PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());
                }
                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_BACK_SNAPSHOT) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        String filePath = getServletContext().getRealPath("images") + "/upload_dir/cnicBackPic_" + mfsAccountModel.getAppUserId() + ".gif";
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                        if (actionAuthorizationModel.getUsecaseId().longValue() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                            mfsAccountModel.setCnicBackPicDiscrepant(mfsAccountModel.getCnicBackPicDiscrepant());
                        } else {
                            mfsAccountModel.setCnicBackPicDiscrepant(customerPictureModel.getDiscrepant());
                        }
                    }
                }


                if (actionAuthorizationModel.getUsecaseId().longValue() != PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                            PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT, customerModel.getCustomerId().longValue());

                    if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT) {
                        if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                            String filePath = getServletContext().getRealPath("images") + "/upload_dir/sourceOfIncomePic_" + mfsAccountModel.getAppUserId() + ".gif";
                            FileOutputStream fos = new FileOutputStream(filePath);
                            fos.write(customerPictureModel.getPicture());
                            fos.flush();
                            fos.close();
                            logger.info("Picture Extracted : " + filePath);
                            mfsAccountModel.setSourceOfIncomePicDiscrepant(customerPictureModel.getDiscrepant());
                        }
                    }

                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                            PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, customerModel.getCustomerId().longValue());

                    if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT) {
                        if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                            String filePath = getServletContext().getRealPath("images") + "/upload_dir/proofOfProfessionPic_" + mfsAccountModel.getAppUserId() + ".gif";
                            FileOutputStream fos = new FileOutputStream(filePath);
                            fos.write(customerPictureModel.getPicture());
                            fos.flush();
                            fos.close();
                            logger.info("Picture Extracted : " + filePath);
                            mfsAccountModel.setProofOfProfessionPicDiscrepant(customerPictureModel.getDiscrepant());
                        }
                    }


                    if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {
                        customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                                PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT, customerModel.getCustomerId().longValue());

                        if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT) {
                            if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                                String filePath = getServletContext().getRealPath("images") + "/upload_dir/level1FormPic_" + mfsAccountModel.getAppUserId() + ".gif";
                                FileOutputStream fos = new FileOutputStream(filePath);
                                fos.write(customerPictureModel.getPicture());
                                fos.flush();
                                fos.close();
                                logger.info("Picture Extracted : " + filePath);
                                mfsAccountModel.setLevel1FormPicDiscrepant(customerPictureModel.getDiscrepant());
                            }
                        }
                    }
                }

            }

            if (actionAuthorizationModel.getUsecaseId().longValue() == PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {
                MfsAccountModel currentMfsAccountModel = populateCurrentInfoModelData(mfsAccountModel.getAppUserId(), mfsAccountModel);

                request.setAttribute("currentMfsAccountModel", currentMfsAccountModel);
                request.setAttribute("mfsAccountModel", mfsAccountModel);
                System.out.println("Customer Account type id::" + mfsAccountModel.getCustomerAccountTypeId());
                if (mfsAccountModel.getCustomerAccountTypeId() == 53) {
                    mfsAccountModel.setCustomerAccountName("Blink");
                }
                if (mfsAccountModel.getRegistrationStateId() != null) {
                    if (mfsAccountModel.getRegistrationStateId() == 1) {
                        mfsAccountModel.setRegStateName("BULK_REQUEST_RECEIVED");
                    } else if (mfsAccountModel.getRegistrationStateId() == 2) {
                        mfsAccountModel.setRegStateName("REQUEST_RECEIVED");
                    } else if (mfsAccountModel.getRegistrationStateId() == 3) {
                        mfsAccountModel.setRegStateName("APPROVED");
                    } else if (mfsAccountModel.getRegistrationStateId() == 4) {
                        mfsAccountModel.setRegStateName("DISCREPANT");
                    } else if (mfsAccountModel.getRegistrationStateId() == 5) {
                        mfsAccountModel.setRegStateName("DECLINE");
                    } else if (mfsAccountModel.getRegistrationStateId() == 6) {
                        mfsAccountModel.setRegStateName("REJECTED");
                    } else if (mfsAccountModel.getRegistrationStateId() == 7) {
                        mfsAccountModel.setRegStateName("BLACK_LISTED");
                    } else {
                        mfsAccountModel.setRegStateName("BLINK_PENDING");
                    }
                }
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                AppUserModel appUserModel = new AppUserModel();
                appUserModel.setAppUserId(mfsAccountModel.getAppUserId());
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
                appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
                CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
                mfsAccountModel.setInitialDeposit(customerModel.getInitialDeposit());
                BlinkCustomerPictureModel customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                        PictureTypeConstants.CUSTOMER_PHOTO, customerModel.getCustomerId().longValue());

                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.CUSTOMER_PHOTO) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        String filePath = getServletContext().getRealPath("images") + "/upload_dir/customerPic_" + mfsAccountModel.getAppUserId() + ".gif";
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                    }
                }

                customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                        PictureTypeConstants.TERMS_AND_CONDITIONS_COPY, customerModel.getCustomerId().longValue());

                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.TERMS_AND_CONDITIONS_COPY) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        String filePath = getServletContext().getRealPath("images") + "/upload_dir/tncPic_" + mfsAccountModel.getAppUserId() + ".gif";
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                    }
                }

                customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                        PictureTypeConstants.SIGNATURE_SNAPSHOT, customerModel.getCustomerId().longValue());

                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SIGNATURE_SNAPSHOT) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        String filePath = getServletContext().getRealPath("images") + "/upload_dir/signPic_" + mfsAccountModel.getAppUserId() + ".gif";
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                    }
                }


                customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                        PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModel.getCustomerId().longValue());

                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_FRONT_SNAPSHOT) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        String filePath = getServletContext().getRealPath("images") + "/upload_dir/cnicFrontPic_" + mfsAccountModel.getAppUserId() + ".gif";
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                    }
                }


                customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                        PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());

                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_BACK_SNAPSHOT) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        String filePath = getServletContext().getRealPath("images") + "/upload_dir/cnicBackPic_" + mfsAccountModel.getAppUserId() + ".gif";
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                    }
                }


                customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                        PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT, customerModel.getCustomerId().longValue());

                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        String filePath = getServletContext().getRealPath("images") + "/upload_dir/sourceOfIncomePic_" + mfsAccountModel.getAppUserId() + ".gif";
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                    }
                }

                customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                        PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, customerModel.getCustomerId().longValue());

                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        String filePath = getServletContext().getRealPath("images") + "/upload_dir/proofOfProfessionPic_" + mfsAccountModel.getAppUserId() + ".gif";
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                    }
                }


                if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {
                    customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                            PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT, customerModel.getCustomerId().longValue());

                    if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT) {
                        if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                            String filePath = getServletContext().getRealPath("images") + "/upload_dir/level1FormPic_" + mfsAccountModel.getAppUserId() + ".gif";
                            FileOutputStream fos = new FileOutputStream(filePath);
                            fos.write(customerPictureModel.getPicture());
                            fos.flush();
                            fos.close();
                            logger.info("Picture Extracted : " + filePath);
                        }
                    }
                }

            }

            return actionAuthorizationModel;
        } else
            return new ActionAuthorizationModel();
    }


    @Override
    protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object object,
                                    BindException errors) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object,
                                    BindException errors) throws Exception {

        return null;
    }

    @Override
    protected ModelAndView onEscalate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        String printCheck = ServletRequestUtils.getStringParameter(request, "printCheck");

        if (printCheck.equals("0")) {
            ModelAndView modelAndView = null;
            I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
            I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
            I8SBSwitchControllerRequestVO requestVOCLS = new I8SBSwitchControllerRequestVO();
            I8SBSwitchControllerResponseVO responseVOCLS = new I8SBSwitchControllerResponseVO();
            ActionAuthorizationModel model = (ActionAuthorizationModel) command;
            try {
                ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
                boolean isValidChecker = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(), UserUtils.getCurrentUser().getAppUserId());
                long currentUserId = UserUtils.getCurrentUser().getAppUserId();


                UsecaseModel usecaseModel = usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());


                if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)) {
                    if ((!isValidChecker) || (actionAuthorizationModel.getCreatedById().longValue() == currentUserId)) {
                        throw new FrameworkCheckedException("You are not authorized to update action status.");
                    }

                    long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel());
                    if (nextAuthorizationLevel < 1) {

                        XStream xstream = new XStream();
                        MfsAccountModel mfsAccountModel = (MfsAccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
                        if (mfsAccountModel.getUsecaseId() == PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {
                            if (mfsAccountModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.APPROVED)) {


                                esbAdapter = new ESBAdapter();
                                String transmissionDateTime = new SimpleDateFormat("yyyyMMddHHss").format(new Date());
                                String stan = String.valueOf((new Random().nextInt(90000000)));
                                requestVOCLS = esbAdapter.prepareCLSRequest(I8SBConstants.RequestType_CLSJS_ImportScreening);
                                if (mfsAccountModel.getLastName() != null) {
                                    requestVOCLS.setName(mfsAccountModel.getName() + " " + mfsAccountModel.getLastName());
                                } else {
                                    requestVOCLS.setName(mfsAccountModel.getName());
                                }
                                requestVOCLS.setCNIC(mfsAccountModel.getNic());
                                SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy");
                                String dateOfBirth = formatter3.format(mfsAccountModel.getDob());
                                requestVOCLS.setDateOfBirth(dateOfBirth);
                                requestVOCLS.setNationality("Pakistan");
                                requestVOCLS.setRequestId(transmissionDateTime + stan);
                                requestVOCLS.setMobileNumber(mfsAccountModel.getMobileNo());
                                requestVOCLS.setFatherName(UserUtils.getCurrentUser().getCustomerIdCustomerModel().getFatherHusbandName());

                                if (mfsAccountModel.getCity() == null) {
                                    requestVOCLS.setCity("");
                                } else {
                                    requestVOCLS.setCity(mfsAccountModel.getCity());
                                }

                                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                                sWrapper.setI8SBSwitchControllerRequestVO(requestVOCLS);
                                sWrapper.setI8SBSwitchControllerResponseVO(responseVOCLS);
                                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                                responseVOCLS = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

                                if (!responseVOCLS.getResponseCode().equals("I8SB-200")) {
                                    throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                                } else {
                                    if (!(responseVOCLS.getCaseStatus().equalsIgnoreCase("No Matches") || responseVOCLS.getCaseStatus().
                                            equalsIgnoreCase("Passed By Rule") || responseVOCLS.getCaseStatus().
                                            equalsIgnoreCase("False Positive Match") ||
                                            responseVOCLS.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules") ||
                                            responseVOCLS.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-False Positive|Private-False Positive") ||
                                            responseVOCLS.getCaseStatus().equalsIgnoreCase("Private-False Positive") ||
                                            responseVOCLS.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|Private-Passed by Rules") ||
                                            responseVOCLS.getCaseStatus().equalsIgnoreCase("Private-Passed by Rules") ||
                                            responseVOCLS.getCaseStatus().equalsIgnoreCase("No Match")
                                            || responseVOCLS.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-Passed by Rules|Private-False Positive") ||
                                            responseVOCLS.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-Passed by Rules|Private-Passed by Rules"))) {
                                        baseWrapper.putObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY, mfsAccountModel);
                                        mfsAccountManager.saveClsPendignAccount(baseWrapper, responseVOCLS);

                                    } else if (responseVOCLS.getCaseStatus().equals("True Match-Compliance")) {
                                        baseWrapper.putObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY, mfsAccountModel);
                                        mfsAccountManager.accountBlock(baseWrapper);

                                    } else {
                                        mfsAccountModel.setClsResponseCode(responseVOCLS.getCaseStatus());
                                        SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
                                        requestVO = ESBAdapter.prepareRequestVoForUpdateAccountToBlink(I8SBConstants.RequestType_L2_ACCOUNT_UPGRADE_VALIDATION);
                                        responseVO = new I8SBSwitchControllerResponseVO();
                                        requestVO.setCNIC(mfsAccountModel.getNic());
                                        requestVO.setAddress("Approved");
                                        requestVO.setPurposeOfAccount("Approved");
                                        requestVO.setExpectedMonthlyTurnOver("Approved");
                                        requestVO.setExpectedMonthlyTurnOver("Approved");
                                        requestVO.setMailingAddress("Approved");
                                        requestVO.setEmail("Approved");
                                        requestVO.setSourceOfIncome("Approved");
                                        requestVO.setMobileNumber("Approved");
                                        if (mfsAccountModel.getFhRej() == true) {
                                            requestVO.setFatherName("Rejected");
                                        } else {
                                            requestVO.setFatherName("Approved");
                                        }
                                        if (mfsAccountModel.getSourcePicRej() == true) {
                                            requestVO.setSourceOfIncomePic("Rejected");
                                        } else {
                                            requestVO.setSourceOfIncomePic("Approved");
                                        }
                                        if (mfsAccountModel.getPopRej() == true) {
                                            requestVO.setProofOfProfession("Rejected");
                                        } else {
                                            requestVO.setProofOfProfession("Approved");
                                        }
                                        if (mfsAccountModel.getCnicFrontRej() == true) {
                                            requestVO.setCnicFrontPic("Rejected");
                                        } else {
                                            requestVO.setCnicFrontPic("Approved");
                                        }
                                        if (mfsAccountModel.getCnicBackRej() == true) {
                                            requestVO.setCnicBackPic("Rejected");
                                        } else {
                                            requestVO.setCnicBackPic("Approved");
                                        }
                                        if (mfsAccountModel.getCustomerPicRej() == true) {
                                            requestVO.setCustomerPic("Rejected");
                                        } else {
                                            requestVO.setCustomerPic("Approved");
                                        }
                                        if (mfsAccountModel.getSignReg() == true) {
                                            requestVO.setSignaturePic("Rejected");
                                        } else {
                                            requestVO.setSignaturePic("Approved");
                                        }
                                        if (mfsAccountModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.APPROVED)) {
                                            requestVO.setStatus("Approved");
                                        } else if (mfsAccountModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.DISCREPANT)) {
                                            requestVO.setStatus("Discripent");
                                        } else if (mfsAccountModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.REJECTED)) {
                                            requestVO.setStatus("Rejected");
                                        }
                                        i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                                        try {
                                            i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
                                            requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();
                                            responseVO = requestVO.getI8SBSwitchControllerResponseVO();
                                            if (responseVO.getResponseCode().equals("I8SB-200")) {
                                                logger.info("Successfully your Discrepant call to I8SB at " + new Date());
                                            }
                                        } catch (Exception ex) {
                                            throw new FrameworkCheckedException("Error during call to I8SB ::" + responseVO.getDescription());
                                        }
                                    }
                                }
                            } else {
                                SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
                                requestVO = ESBAdapter.prepareRequestVoForUpdateAccountToBlink(I8SBConstants.RequestType_L2_ACCOUNT_UPGRADE_VALIDATION);
                                responseVO = new I8SBSwitchControllerResponseVO();
                                requestVO.setCNIC(mfsAccountModel.getNic());
                                requestVO.setAddress("Approved");
                                requestVO.setPurposeOfAccount("Approved");
                                requestVO.setExpectedMonthlyTurnOver("Approved");
                                requestVO.setMailingAddress("Approved");
                                requestVO.setEmail("Approved");
                                requestVO.setSourceOfIncome("Approved");
                                requestVO.setSourceOfIncomePic("Approved");
                                requestVO.setMobileNumber("Approved");

                                if (mfsAccountModel.getFhRej() == true) {
                                    requestVO.setFatherName("Rejected");
                                } else {
                                    requestVO.setFatherName("Approved");
                                }
                                if (mfsAccountModel.getPopRej() == true) {
                                    requestVO.setProofOfProfession("Rejected");
                                } else {
                                    requestVO.setProofOfProfession("Approved");
                                }
                                if (mfsAccountModel.getCnicFrontRej() == true) {
                                    requestVO.setCnicFrontPic("Rejected");
                                } else {
                                    requestVO.setCnicFrontPic("Approved");
                                }
                                if (mfsAccountModel.getSourcePicRej() == true) {
                                    requestVO.setSourceOfIncomePic("Rejected");
                                } else {
                                    requestVO.setSourceOfIncomePic("Approved");
                                }
                                if (mfsAccountModel.getCnicBackRej() == true) {
                                    requestVO.setCnicBackPic("Rejected");
                                } else {
                                    requestVO.setCnicBackPic("Approved");
                                }
                                if (mfsAccountModel.getSignReg() == true) {
                                    requestVO.setSignaturePic("Rejected");
                                } else {
                                    requestVO.setSignaturePic("Approved");
                                }

                                if (mfsAccountModel.getCustomerPicRej() == true) {
                                    requestVO.setCustomerPic("Rejected");
                                } else {
                                    requestVO.setCustomerPic("Approved");
                                }
                                if (mfsAccountModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.APPROVED)) {
                                    requestVO.setStatus("Approved");
                                } else if (mfsAccountModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.DISCREPANT)) {
                                    requestVO.setStatus("Discripent");
                                } else if (mfsAccountModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.REJECTED)) {
                                    requestVO.setStatus("Rejected");
                                }
                                i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                                try {
                                    i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
                                    requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();
                                    responseVO = requestVO.getI8SBSwitchControllerResponseVO();
                                    if (responseVO.getResponseCode().equals("I8SB-200")) {
                                        logger.info("Successfully your Discrepant call to I8SB at " + new Date());
                                    }
                                } catch (Exception ex) {
                                    throw new FrameworkCheckedException("Error during call to I8SB ::" + responseVO.getDescription());
                                }

                            }
                        }

                        if (mfsAccountModel.getUsecaseId() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                            SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
                            requestVO = ESBAdapter.prepareRequestVoForMinorCustomer(I8SBConstants.RequestType_MinorAccountSync);
                            responseVO = new I8SBSwitchControllerResponseVO();
                            requestVO.setMobileNumber(mfsAccountModel.getMobileNo());
                            if (mfsAccountModel.getCustomerPicDiscrepant()) {
                                requestVO.setCustomerPic("0");
                            } else {
                                requestVO.setCustomerPic("1");
                            }

                            if (mfsAccountModel.getParentCnicPicDiscrepant()) {
                                requestVO.setFatherCnicPic("0");
                                requestVO.setMotherCnicPic("0");
                            } else {
                                requestVO.setFatherCnicPic("1");
                                requestVO.setMotherCnicPic("1");
                            }

                            if (mfsAccountModel.getCnicFrontPicDiscrepant()) {
                                requestVO.setMinorCnicPic("0");
                            } else {
                                requestVO.setMinorCnicPic("1");
                            }
                            if (mfsAccountModel.getParentCnicBackPicDiscrepant()) {
                                requestVO.setCnicBackPic("0");
                            } else {
                                requestVO.setCnicBackPic("1");
                            }
                            if (mfsAccountModel.getCnicBackPicDiscrepant()) {
                                requestVO.setMinorCnicBackPic("0");
                            } else {
                                requestVO.setMinorCnicBackPic("1");
                            }
                            if (mfsAccountModel.getbFormPicDiscrepant()) {
                                requestVO.setReserved1("0");
                            } else {
                                requestVO.setReserved1("1");
                            }

                            if (mfsAccountModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.APPROVED)) {
                                requestVO.setStatus("Approved");
                            } else if (mfsAccountModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.DISCREPANT)) {
                                requestVO.setStatus("Discripent");
                            } else if (mfsAccountModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.REJECTED)) {
                                requestVO.setStatus("Rejected");
                            }
                            i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                            try {
                                i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
                                requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();
                                responseVO = requestVO.getI8SBSwitchControllerResponseVO();
                                if (responseVO.getResponseCode().equals("I8SB-200")) {
                                    logger.info("Successfully your Discrepant call to I8SB at " + new Date());
                                }
                            } catch (Exception ex) {
                                throw new FrameworkCheckedException("Error during call to I8SB ::" + responseVO.getDescription());
                            }
                        }

                        if (mfsAccountModel.getCustomerPicDiscrepant() == null) {
                            mfsAccountModel.setCustomerPicDiscrepant(false);
                        }

                        if (mfsAccountModel.getParentCnicPicDiscrepant() == null) {
                            mfsAccountModel.setParentCnicPicDiscrepant(false);
                        }

                        if (mfsAccountModel.getbFormPicDiscrepant() == null) {
                            mfsAccountModel.setbFormPicDiscrepant(false);
                        }

                        if (mfsAccountModel.getSourceOfIncomePicDiscrepant() == null) {
                            mfsAccountModel.setSourceOfIncomePicDiscrepant(false);
                        }
                        if (mfsAccountModel.getTncPicDiscrepant() == null) {
                            mfsAccountModel.setTncPicDiscrepant(false);
                        }
                        if (mfsAccountModel.getSignPicDiscrepant() == null) {
                            mfsAccountModel.setSignPicDiscrepant(false);
                        }
                        if (mfsAccountModel.getCnicFrontPicDiscrepant() == null) {
                            mfsAccountModel.setCnicFrontPicDiscrepant(false);
                        }
                        if (mfsAccountModel.getCnicBackPicDiscrepant() == null) {
                            mfsAccountModel.setCnicBackPicDiscrepant(false);
                        }
                        if (mfsAccountModel.getProofOfProfessionPicDiscrepant() == null) {
                            mfsAccountModel.setProofOfProfessionPicDiscrepant(false);
                        }
                        if (mfsAccountModel.getLevel1FormPicDiscrepant() == null) {
                            mfsAccountModel.setLevel1FormPicDiscrepant(false);
                        }
                        mfsAccountModel.setCustPicCheckerComments(model.getCustPicCheckerComments());
                        mfsAccountModel.setpNicPicCheckerComments(model.getpNicPicCheckerComments());
                        mfsAccountModel.setbFormPicCheckerComments(model.getbFormPicCheckerComments());
                        mfsAccountModel.setNicBackPicCheckerComments(model.getNicBackPicCheckerComments());
                        mfsAccountModel.setNicFrontPicCheckerComments(model.getNicFrontPicCheckerComments());
                        mfsAccountModel.setpNicBackPicCheckerComments(model.getpNicBackPicCheckerComments());

                        if (mfsAccountModel.getUsecaseId() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                            mfsAccountModel.setComments(model.getCheckerComments());
                        }
                        ////////////////Setting Updated Images//////////////////
                        ActionAuthPictureModel authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.CUSTOMER_PHOTO);
                        if (authPictureModel != null && authPictureModel.getPicture() != null)
                            mfsAccountModel.setCustomerPicByte(authPictureModel.getPicture());
                        authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.TERMS_AND_CONDITIONS_COPY);
                        if (authPictureModel != null && authPictureModel.getPicture() != null)
                            mfsAccountModel.setTncPicByte(authPictureModel.getPicture());

                        authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.SIGNATURE_SNAPSHOT);

                        if (authPictureModel != null && authPictureModel.getPicture() != null) {
                            mfsAccountModel.setSignPicByte(authPictureModel.getPicture());
                        }

                        authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.ID_FRONT_SNAPSHOT);
                        if (authPictureModel != null && authPictureModel.getPicture() != null) {
                            mfsAccountModel.setCnicFrontPicByte(authPictureModel.getPicture());
                        }
                        if (mfsAccountModel.getCustomerAccountTypeId() == 53) {
                            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT);
                            if (authPictureModel != null && authPictureModel.getPicture() != null) {
                                mfsAccountModel.setProofOfProfessionPicByte(authPictureModel.getPicture());
                            }

                            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT);
                            if (authPictureModel != null && authPictureModel.getPicture() != null) {
                                mfsAccountModel.setSourceOfIncomeByte(authPictureModel.getPicture());
                            }
                        }

                        authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.ID_BACK_SNAPSHOT);
                        if (authPictureModel != null && authPictureModel.getPicture() != null) {
                            mfsAccountModel.setCnicBackPicByte(authPictureModel.getPicture());
                        }

                        authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT);
                        ////////////////////End Setting Updated Images///////////

                        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, mfsAccountModel.getActionId());
                        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, mfsAccountModel.getUsecaseId());
                        baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID, actionAuthorizationModel.getCreatedById());
                        baseWrapper.putObject(PortalConstants.KEY_CREATED_ON, actionAuthorizationModel.getCreatedOn());
                        baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME, actionAuthorizationModel.getCreatedByUsername());


                        baseWrapper.putObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY, mfsAccountModel);

                        //this.mfsAccountManager.validateUniqueness(mfsAccountModel);//Validate Uniqueness

                        if (mfsAccountModel.getUsecaseId().longValue() == PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID) {
                            baseWrapper = this.mfsAccountManager.createMfsAccount(baseWrapper);
                            /**
                             COMMENTED BY ATIF

                             customerPendingTrxManager.makeCustomerPendingTrx(mfsAccountModel.getNic());
                             *
                             */

                            if (null != mfsAccountModel.getInitialDeposit()) {
                                this.mfsAccountManager.makeInitialDeposit(mfsAccountModel);
                            }
                            //mfsAccountId = (String)baseWrapper.getObject(PortalConstants.KEY_MFS_ACCOUNT_ID);
                            //appUserId = new Long(baseWrapper.getObject(PortalConstants.KEY_APP_USER_ID).toString());
                        } else {
                            //setting current Data for history
                            MfsAccountModel oldMfsAccountModel = populateCurrentInfoModelData(mfsAccountModel.getAppUserId(), mfsAccountModel);
                            if (mfsAccountModel.getUsecaseId().longValue() == PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {
                                if ((mfsAccountModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.APPROVED))) {
                                    if ((responseVOCLS.getCaseStatus().equalsIgnoreCase("No Matches") || responseVOCLS.getCaseStatus().
                                            equalsIgnoreCase("Passed By Rule") || responseVOCLS.getCaseStatus().
                                            equalsIgnoreCase("False Positive Match") ||
                                            responseVOCLS.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules") ||
                                            responseVOCLS.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-False Positive|Private-False Positive") ||
                                            responseVOCLS.getCaseStatus().equalsIgnoreCase("Private-False Positive") ||
                                            responseVOCLS.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|Private-Passed by Rules") ||
                                            responseVOCLS.getCaseStatus().equalsIgnoreCase("Private-Passed by Rules") ||
                                            responseVOCLS.getCaseStatus().equalsIgnoreCase("No Match")
                                            || responseVOCLS.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|Private-False Positive")
                                            || responseVOCLS.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-Passed by Rules|Private-False Positive") ||
                                            responseVOCLS.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-Passed by Rules|Private-Passed by Rules"))) {
                                        baseWrapper = this.mfsAccountManager.updateMfsAccount(baseWrapper);

                                    } else if (responseVOCLS.getCaseStatus().equals("True Match-Compliance")) {
                                        BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
                                        blinkCustomerModel = commonCommandManager.loadBlinkCustomerByMobileAndAccUpdate(mfsAccountModel.getMobileNo(), 1L);
                                        blinkCustomerModel.setRegistrationStatus(mfsAccountModel.getRegistrationStateId().toString());
                                        blinkCustomerModel.setActionStatusId((model.getActionStatusId()));
                                        blinkCustomerModel.setComments(mfsAccountModel.getComments());
                                        blinkCustomerModel.setChkComments(model.getCheckerComments());
                                        blinkCustomerModel.setClsResponseCode("True Match-Compliance");
                                        blinkCustomerModel.setAccUpdate(0l);
                                        blinkCustomerModel.setUpdatedOn(new Date());
                                        blinkCustomerModelDAO.saveOrUpdate(blinkCustomerModel);
                                    } else {
                                        BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
                                        blinkCustomerModel = commonCommandManager.loadBlinkCustomerByMobileAndAccUpdate(mfsAccountModel.getMobileNo(), 1L);
                                        blinkCustomerModel.setRegistrationStatus(mfsAccountModel.getRegistrationStateId().toString());
                                        blinkCustomerModel.setActionStatusId((model.getActionStatusId()));
                                        blinkCustomerModel.setComments(mfsAccountModel.getComments());
                                        blinkCustomerModel.setChkComments(model.getCheckerComments());
                                        blinkCustomerModelDAO.saveOrUpdate(blinkCustomerModel);
                                    }
                                } else {

                                    BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
                                    blinkCustomerModel = commonCommandManager.loadBlinkCustomerByMobileAndAccUpdate(mfsAccountModel.getMobileNo(), 1L);
                                    blinkCustomerModel.setRegistrationStatus(mfsAccountModel.getRegistrationStateId().toString());
                                    blinkCustomerModel.setActionStatusId((model.getActionStatusId()));
                                    blinkCustomerModel.setComments(mfsAccountModel.getComments());
                                    blinkCustomerModel.setChkComments(model.getCheckerComments());
                                    blinkCustomerModel.setAccUpdate(0l);
                                    blinkCustomerModel.setUpdatedOn(new Date());
                                    blinkCustomerModelDAO.saveOrUpdate(blinkCustomerModel);


                                }
                            } else {
                                baseWrapper = this.mfsAccountManager.updateMfsAccount(baseWrapper);

                            }

                            ///Saving old images
                            ActionAuthPictureModel actionAuthPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.CUSTOMER_PHOTO);
                            if (actionAuthPictureModel != null && oldMfsAccountModel.getCustomerPicByte() != null) {
                                actionAuthPictureModel.setPicture(oldMfsAccountModel.getCustomerPicByte());
                                actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                            }
                            oldMfsAccountModel.setCustomerPicByte(null);

                            actionAuthPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.PARENT_CNIC_SNAPSHOT);
                            if (actionAuthPictureModel != null && oldMfsAccountModel.getParentCnicPicByte() != null) {
                                actionAuthPictureModel.setPicture(oldMfsAccountModel.getParentCnicPicByte());
                                actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                            }

                            oldMfsAccountModel.setParentCnicPicByte(null);

                            actionAuthPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.B_FORM_SNAPSHOT);
                            if (actionAuthPictureModel != null && oldMfsAccountModel.getbFormPicByte() != null) {
                                actionAuthPictureModel.setPicture(oldMfsAccountModel.getbFormPicByte());
                                actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                            }
                            oldMfsAccountModel.setbFormPicByte(null);

                            actionAuthPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT);
                            if (actionAuthPictureModel != null && oldMfsAccountModel.getParentCnicBackPicByte() != null) {
                                actionAuthPictureModel.setPicture(oldMfsAccountModel.getParentCnicBackPicByte());
                                actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                            }

                            oldMfsAccountModel.setParentCnicBackPicByte(null);

                            actionAuthPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.TERMS_AND_CONDITIONS_COPY);
                            if (actionAuthPictureModel != null && oldMfsAccountModel.getTncPicByte() != null) {
                                actionAuthPictureModel.setPicture(oldMfsAccountModel.getTncPicByte());
                                actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                            }
                            oldMfsAccountModel.setTncPicByte(null);

                            actionAuthPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.SIGNATURE_SNAPSHOT);
                            if (actionAuthPictureModel != null && oldMfsAccountModel.getSignPicByte() != null) {
                                actionAuthPictureModel.setPicture(oldMfsAccountModel.getSignPicByte());
                                actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                            }

                            oldMfsAccountModel.setSignPicByte(null);

                            actionAuthPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.ID_FRONT_SNAPSHOT);
                            if (actionAuthPictureModel != null && oldMfsAccountModel.getCnicFrontPicByte() != null) {
                                actionAuthPictureModel.setPicture(oldMfsAccountModel.getCnicFrontPicByte());
                                actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                            }

                            oldMfsAccountModel.setCnicFrontPicByte(null);

                            actionAuthPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.ID_BACK_SNAPSHOT);
                            if (actionAuthPictureModel != null && oldMfsAccountModel.getCnicBackPicByte() != null) {
                                actionAuthPictureModel.setPicture(oldMfsAccountModel.getCnicBackPicByte());
                                actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                            }
                            oldMfsAccountModel.setCnicBackPicByte(null);


                            actionAuthorizationModel.setReferenceData(xstream.toXML(oldMfsAccountModel));
                            //End setting current Data for history
                        }

                        if (actionAuthorizationModel.getEscalationLevel().intValue() < usecaseModel.getEscalationLevels().intValue()) {
                            approvedWithIntimationLevelsNext(actionAuthorizationModel, model, usecaseModel, request);
                        } else {
                            approvedAtMaxLevel(actionAuthorizationModel, model);
                        }

                    } else {
                        escalateToNextLevel(actionAuthorizationModel, model, nextAuthorizationLevel, usecaseModel.getUsecaseId(), request);
                    }
                } else if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)) {

                    if ((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))) {
                        throw new FrameworkCheckedException("You are not authorized to update action status.");
                    }
                    XStream xstream = new XStream();
                    MfsAccountModel mfsAccountModel = (MfsAccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());

                    if (mfsAccountModel.getUsecaseId() == PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {

                        BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
                        blinkCustomerModel = commonCommandManager.loadBlinkCustomerByMobileAndAccUpdate(mfsAccountModel.getMobileNo(), 1L);
//                    blinkCustomerModel.setRegistrationStatus(mfsAccountModel.getRegistrationStateId().toString());
                        blinkCustomerModel.setActionStatusId(model.getActionStatusId());
                        blinkCustomerModel.setComments(mfsAccountModel.getComments());
                        blinkCustomerModel.setChkComments(model.getCheckerComments());
                        blinkCustomerModelDAO.saveOrUpdate(blinkCustomerModel);

                    }
                    if (mfsAccountModel.getUsecaseId() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {

                        CustomerModel customerModel = new CustomerModel();
                        customerModel = commonCommandManager.getCustomerModelByMobileNumber(mfsAccountModel.getMobileNo());
                        customerModel.setCustPicCheckerComments(model.getCustPicCheckerComments());
                        customerModel.setpNicPicCheckerComments(model.getpNicPicCheckerComments());
                        customerModel.setbFormPicCheckerComments(model.getbFormPicCheckerComments());
                        customerModel.setNicBackPicCheckerComments(model.getNicBackPicCheckerComments());
                        customerModel.setNicFrontPicCheckerComments(model.getNicFrontPicCheckerComments());
                        customerModel.setpNicBackPicCheckerComments(model.getpNicBackPicCheckerComments());
                        customerModel.setCustPicMakerComments(mfsAccountModel.getCustPicMakerComments());
                        customerModel.setbFormPicMakerComments(mfsAccountModel.getbFormPicMakerComments());
                        customerModel.setpNicPicMakerComments(mfsAccountModel.getpNicPicMakerComments());
                        customerModel.setNicBackPicMakerComments(mfsAccountModel.getNicBackPicMakerComments());
                        customerModel.setNicFrontPicMakerComments(mfsAccountModel.getNicFrontPicMakerComments());
                        customerModel.setpNicBackPicMakerComments(mfsAccountModel.getpNicBackPicMakerComments());
                        customerDAO.saveOrUpdate(customerModel);
                    }
                    actionDeniedOrCancelled(actionAuthorizationModel, model, request);


                } else if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.longValue()
                        && (actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)
                        || actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK))) {

                    if (!(actionAuthorizationModel.getCreatedById().equals(currentUserId))) {
                        throw new FrameworkCheckedException("You are not authorized to update action status.");
                    }
                    XStream xstream = new XStream();
                    MfsAccountModel mfsAccountModel = (MfsAccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());

                    if (mfsAccountModel.getUsecaseId() == PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {

                        BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();

                        blinkCustomerModel = commonCommandManager.loadBlinkCustomerByMobileAndAccUpdate(mfsAccountModel.getMobileNo(), 1L);
//                    blinkCustomerModel.setRegistrationStatus(mfsAccountModel.getRegistrationStateId().toString());
                        blinkCustomerModel.setActionStatusId(model.getActionStatusId());
                        blinkCustomerModel.setComments(mfsAccountModel.getComments());
                        blinkCustomerModel.setChkComments(model.getCheckerComments());
                        blinkCustomerModelDAO.saveOrUpdate(blinkCustomerModel);

                    }
                    actionDeniedOrCancelled(actionAuthorizationModel, model, request);


                } else if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
                        && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)) {
                    isValidChecker = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(), UserUtils.getCurrentUser().getAppUserId());

                    if ((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))) {
                        throw new FrameworkCheckedException("You are not authorized to update action status.");
                    }
                    XStream xstream = new XStream();
                    MfsAccountModel mfsAccountModel = (MfsAccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
                    if (mfsAccountModel.getUsecaseId() == PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {
                        BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
                        blinkCustomerModel = commonCommandManager.loadBlinkCustomerByMobileAndAccUpdate(mfsAccountModel.getMobileNo(), 1L);
//                    blinkCustomerModel.setRegistrationStatus(mfsAccountModel.getRegistrationStateId().toString());
                        blinkCustomerModel.setActionStatusId(model.getActionStatusId());
                        blinkCustomerModel.setComments(mfsAccountModel.getComments());
                        blinkCustomerModel.setChkComments(model.getCheckerComments());
                        blinkCustomerModelDAO.saveOrUpdate(blinkCustomerModel);

                    }
                    if (mfsAccountModel.getUsecaseId() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {

                        CustomerModel customerModel = new CustomerModel();
                        customerModel = commonCommandManager.getCustomerModelByMobileNumber(mfsAccountModel.getMobileNo());
                        customerModel.setCustPicCheckerComments(model.getCustPicCheckerComments());
                        customerModel.setpNicPicCheckerComments(model.getpNicPicCheckerComments());
                        customerModel.setbFormPicCheckerComments(model.getbFormPicCheckerComments());
                        customerModel.setNicBackPicCheckerComments(model.getNicBackPicCheckerComments());
                        customerModel.setNicFrontPicCheckerComments(model.getNicFrontPicCheckerComments());
                        customerModel.setpNicBackPicCheckerComments(model.getpNicBackPicCheckerComments());
                        customerModel.setCustPicMakerComments(mfsAccountModel.getCustPicMakerComments());
                        customerModel.setbFormPicMakerComments(mfsAccountModel.getbFormPicMakerComments());
                        customerModel.setpNicPicMakerComments(mfsAccountModel.getpNicPicMakerComments());
                        customerModel.setNicBackPicMakerComments(mfsAccountModel.getNicBackPicMakerComments());
                        customerModel.setNicFrontPicMakerComments(mfsAccountModel.getNicFrontPicMakerComments());
                        customerModel.setpNicBackPicMakerComments(mfsAccountModel.getpNicBackPicMakerComments());
                        customerDAO.saveOrUpdate(customerModel);
                    }
                    requestAssignedBack(actionAuthorizationModel, model, request);

                } else {

                    throw new FrameworkCheckedException("Invalid status marked");
                }

            } catch (FrameworkCheckedException ex) {
                if ("MobileNumUniqueException".equals(ex.getMessage())) {
                    request.setAttribute("message", super.getText("newMfsAccount.mobileNumNotUnique", request.getLocale()));
                } else if ("NICUniqueException".equals(ex.getMessage())) {
                    request.setAttribute("message", super.getText("newMfsAccount.nicNotUnique", request.getLocale()));
                } else if ("AccountOpeningCommissionException".equals(ex.getMessage())) {
                    request.setAttribute("message", super.getText("newMfsAccount.commission.failed", request.getLocale()));
                } else {
                    if (null != ex.getMessage())
                        request.setAttribute("message", ex.getMessage().replaceAll("<br>", ""));
                    else
                        request.setAttribute("message", MessageUtil.getMessage("6075"));
                }

                LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : " + model.getActionAuthorizationId(), ex);
                request.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
                return super.showForm(request, response, errors);
            } catch (Exception ex) {

                request.setAttribute("message", MessageUtil.getMessage("6075"));
                LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : " + model.getActionAuthorizationId(), ex);
                request.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
                return super.showForm(request, response, errors);
            }
            request.setAttribute("status", IssueTypeStatusConstantsInterface.SUCCESS);
            modelAndView = super.showForm(request, response, errors);
            return modelAndView;
        } else {
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            ActionAuthorizationModel model = (ActionAuthorizationModel) command;
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
            XStream xstream = new XStream();
            MfsAccountModel mfsAccountModel = (MfsAccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
            BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
            String pattern = "dd-MM-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Document document = new Document();

            BaseFont bf = null;
            bf = BaseFont.createFont(
                    BaseFont.TIMES_ROMAN,
                    BaseFont.CP1252,
                    BaseFont.EMBEDDED);
            Font fontRed = new Font(bf, 8, Font.BOLD, BaseColor.RED);
            Font fontBlue = new Font(bf, 8, Font.BOLD, BaseColor.BLUE);
            Font fontBlack = new Font(bf, 7, Font.BOLD, BaseColor.BLACK);
            Font fontWhite = new Font(bf, 8, Font.BOLD, BaseColor.WHITE);

            Font fontBlackInfoSmall = new Font(bf, 4, Font.BOLD, BaseColor.BLACK);

            OutputStream file = null;
            try {
                response.setContentType("application/pdf");

                // Create a new Document object

                PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "inline; filename=NewPdfFormat.pdf");

                //open
                document.open();
                PdfPTable TableEightColumn = new PdfPTable(8);
                PdfPTable TableFourColumn = new PdfPTable(4);
                PdfPTable TableTwocolumn = new PdfPTable(2);
                Paragraph p = new Paragraph();
                /////////////////////////////////1////////////////////////////////////
                PdfPCell pdfCell = new PdfPCell(new Phrase("*", fontRed));
                Image img1 = Image.getInstance(findPath(mfsAccountModel, actionAuthorizationModel, PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, "proofOfProfessionPic_"));

                //////////////////////New Format PDF///////////////
                PdfPTable NewFirstTable = new PdfPTable(3);
                pdfCell = new PdfPCell(new Phrase("JS Mobile Account Opening Form", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfCell.setBorder(Rectangle.TOP);
                NewFirstTable.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfCell.setBorder(Rectangle.TOP);
                NewFirstTable.addCell(pdfCell);
                String ImageLogoPath = getServletContext().getRealPath("images") + "/upload_dir/jslogo.png";
                img1 = Image.getInstance(ImageLogoPath);
//                img1 = Image.getInstance(findPath(mfsAccountModel,PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT,"proofOfProfessionPic_"));
                img1.scaleToFit(200, 100);
                pdfCell = new PdfPCell(img1, true);
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.TOP);
                NewFirstTable.addCell(pdfCell);
                document.add(NewFirstTable);


                PdfPTable TableoneColumn = new PdfPTable(1);
                pdfCell = new PdfPCell(new Phrase("Account Details", fontWhite));
                pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfCell.setBorder(Rectangle.TOP);
                pdfCell.setBackgroundColor(BaseColor.BLUE);
                TableoneColumn.addCell(pdfCell);
                document.add(TableoneColumn);

                TableFourColumn = new PdfPTable(4);
                pdfCell = new PdfPCell(new Phrase("Type Of Account:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableFourColumn.addCell(pdfCell);
//                Chunk sigUnderline = new Chunk("                                                                                     ");
//                sigUnderline.setUnderline(0.1f, -2f);
                if (mfsAccountModel.getCustomerAccountTypeId().equals(53L)) {
                    pdfCell = new PdfPCell(new Phrase("ULTRA Account", fontBlack));
                } else if (mfsAccountModel.getCustomerAccountTypeId().equals(1L)) {
                    pdfCell = new PdfPCell(new Phrase("LEVEL-0 Account", fontBlack));

                } else {
                    pdfCell = new PdfPCell(new Phrase("LEVEL-1 Account", fontBlack));

                }
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(3);
                TableFourColumn.addCell(pdfCell);

                pdfCell = new PdfPCell(new Phrase("Prefered Branch:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableFourColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(3);
                TableFourColumn.addCell(pdfCell);
                //
//                TableFourColumn = new PdfPTable(4);
                pdfCell = new PdfPCell(new Phrase("Expected Monthly Transaction:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableFourColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableFourColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Expected Monthly Turnover:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableFourColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getExpectedMonthlyTurnOver(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableFourColumn.addCell(pdfCell);
                ////

                pdfCell = new PdfPCell(new Phrase("Source Of Income:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableFourColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getIncome(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(3);
                TableFourColumn.addCell(pdfCell);
                ///
                pdfCell = new PdfPCell(new Phrase("Who Will Fund Your Account(CP)?:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableFourColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(3);
                TableFourColumn.addCell(pdfCell);
                ///
                pdfCell = new PdfPCell(new Phrase("Purpose Of Account:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableFourColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getAccountPurposeName(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(3);
                TableFourColumn.addCell(pdfCell);
                document.add(TableFourColumn);
                TableoneColumn = new PdfPTable(1);
                pdfCell = new PdfPCell(new Phrase("Personal Information & KYC(Individual Application Section)", fontWhite));
                pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfCell.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setBackgroundColor(BaseColor.BLUE);
                TableoneColumn.addCell(pdfCell);
                document.add(TableoneColumn);
                TableEightColumn = new PdfPTable(8);
                pdfCell = new PdfPCell(new Phrase("Prefix/Title:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);

                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getName(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Full Name As Per CNIC/SNIC:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getName(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(4);
                TableEightColumn.addCell(pdfCell);
                ////second line Personal Info//
                pdfCell = new PdfPCell(new Phrase("ID Card Number (CNIC/SNIC):", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getNic(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("ID Type:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getCity(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(3);
                TableEightColumn.addCell(pdfCell);
                ///Third Row Personal Info////
                pdfCell = new PdfPCell(new Phrase("ID Expiry Date:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                if (mfsAccountModel.getNicExpiryDate() != null) {
                    pdfCell = new PdfPCell(new Phrase(simpleDateFormat.format(mfsAccountModel.getNicExpiryDate()), fontBlack));
                } else {
                    pdfCell = new PdfPCell(new Phrase("", fontBlack));
                }
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);

                pdfCell = new PdfPCell(new Phrase("Date Of Birth:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                if (mfsAccountModel.getDob() != null) {
                    pdfCell = new PdfPCell(new Phrase(simpleDateFormat.format(mfsAccountModel.getDob()), fontBlack));
                } else {
                    pdfCell = new PdfPCell(new Phrase("", fontBlack));
                }
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);

                pdfCell = new PdfPCell(new Phrase("ID Issuance Date(CNIC/SNIC):", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                if (mfsAccountModel.getCnicIssuanceDate() != null) {
                    pdfCell = new PdfPCell(new Phrase(simpleDateFormat.format(mfsAccountModel.getCnicIssuanceDate()), fontBlack));
                } else {
                    pdfCell = new PdfPCell(new Phrase("", fontBlack));
                }
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);

                ////Row four Personal Information ////////
                pdfCell = new PdfPCell(new Phrase("Gender:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getGender(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Marital Status:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Mobile Number:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getMobileNo(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);
                //////////Fifth Row Personal Information/////////
                pdfCell = new PdfPCell(new Phrase("Father/Spouse Name As Per CNIC/SNIC:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(3);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getFatherHusbandName(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);

                pdfCell = new PdfPCell(new Phrase("Mother's Maiden Name:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getMotherMaidenName(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);

                //6th Row Personal Information//

                pdfCell = new PdfPCell(new Phrase("Are You US Citizen/Address Holder/Business Holder?(FACTA):", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(4);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("    ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(1);
                TableEightColumn.addCell(pdfCell);

                pdfCell = new PdfPCell(new Phrase("City/Place Of Birth:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getBirthPlace(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);
                //7th Row Personal Information///
                pdfCell = new PdfPCell(new Phrase("Country Of Birth:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Pakistan", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Nationality:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Pakistan", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Dual Nationality?", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getDualNationality(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);
                ///8th row personal information///
                pdfCell = new PdfPCell(new Phrase("Country List-If Dual:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Province:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getProvince(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("City", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getCity(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(1);
                TableEightColumn.addCell(pdfCell);
                ///9th Row Personal Information///
                pdfCell = new PdfPCell(new Phrase("Permanent Address:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getPresentAddress(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(6);
                TableEightColumn.addCell(pdfCell);
                //10th row Personal Informaiton///
                pdfCell = new PdfPCell(new Phrase("Employer /Business Name:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getSourceOfIncome(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(6);
                TableEightColumn.addCell(pdfCell);
                //11th row personal information ///
                pdfCell = new PdfPCell(new Phrase("Flat/House/Building No:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(6);
                TableEightColumn.addCell(pdfCell);
                //12th row personal information//
                pdfCell = new PdfPCell(new Phrase("Street/Area/Address:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(6);
                TableEightColumn.addCell(pdfCell);
                //13th row personal information//
                pdfCell = new PdfPCell(new Phrase("Email Address:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getEmail(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(3);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Creditor Industry:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                //14th row personal information//
                pdfCell = new PdfPCell(new Phrase("Geographical Spread:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(6);
                TableEightColumn.addCell(pdfCell);

                //15th row personal informaiton//
                pdfCell = new PdfPCell(new Phrase("Province Spread:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(3);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Country Spread:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(3);
                TableEightColumn.addCell(pdfCell);
                //16th row personal information //
                pdfCell = new PdfPCell(new Phrase("Is Creditor JS Account Holder", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);

                pdfCell = new PdfPCell(new Phrase("Creditor JS's Account No:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);

                pdfCell = new PdfPCell(new Phrase("Creditor (CP) Name:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);


                //17th row personal information//
                pdfCell = new PdfPCell(new Phrase("Name With Prospective Remitter:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Relationship with Prospective Remitter:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                document.add(TableEightColumn);
                document.newPage();
                ////////////////////Next Of Kin Information////////
                TableoneColumn = new PdfPTable(1);
                pdfCell = new PdfPCell(new Phrase("Next Of Kin", fontWhite));
                pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfCell.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setBackgroundColor(BaseColor.BLUE);
                TableoneColumn.addCell(pdfCell);
                document.add(TableoneColumn);
                //1st row next of kin information//
                TableEightColumn = new PdfPTable(8);
                pdfCell = new PdfPCell(new Phrase("Name:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getNokName(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Relation With Next Of Kin:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getNokRelationship(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(3);
                TableEightColumn.addCell(pdfCell);
                document.add(TableEightColumn);
                ////Request For Debit Card And Cheque Book///
                TableoneColumn = new PdfPTable(1);
                pdfCell = new PdfPCell(new Phrase("Request For Debit Card & Cheque Book", fontWhite));
                pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfCell.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setBackgroundColor(BaseColor.BLUE);
                TableoneColumn.addCell(pdfCell);
                document.add(TableoneColumn);
                List<DebitCardModel> list = null;
                DebitCardModel debitCardModel = new DebitCardModel();
//                list = commonCommandManager.getDebitCardModelDao().getDebitCardModelByMobileAndNIC(mfsAccountModel.getMobileNo(), mfsAccountModel.getNic());
                String CardType = "";
                if (list != null && !list.isEmpty()) {
                    debitCardModel = list.get(0);
                    BlinkCustomerModel customerModelList = blinkCustomerModelDAO.findByPrimaryKey(Long.parseLong(accTypeId));
                    AppUserModel appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(customerModelList.getMobileNo());
                    SmartMoneyAccountModel sam = commonCommandManager.getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                    if (sam.getCardTypeId() != null) {
                        if (sam.getCardTypeId().equals(1))
                            CardType = "PayPak";
                        if (sam.getCardTypeId().equals(2))
                            CardType = "Zindagi PayPak Card";
                        if (sam.getCardTypeId().equals(3))
                            CardType = "MasterCard BLB Debit Card";

                    }
                }

                TableEightColumn = new PdfPTable(8);
                ////1st row Request For Debit Card And Cheque Book///
                pdfCell = new PdfPCell(new Phrase("Debit Card Type:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(CardType, fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(6);
                TableEightColumn.addCell(pdfCell);
                ////2nd row Request For Debit Card And Cheque Book///
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(4);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Cheque Book Required", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                ///3rd row Request For Debit Card And Cheque Book///
                pdfCell = new PdfPCell(new Phrase("Name On Debit Card", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(debitCardModel.getDebitCardEmbosingName(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(6);
                TableEightColumn.addCell(pdfCell);
                //4th row Request For Debit Card And Cheque Book //Info
                pdfCell = new PdfPCell(new Phrase("Upto 19 Characters Including Spaces,NickName Are Not Allowed", fontBlackInfoSmall));
                pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(8);
                TableEightColumn.addCell(pdfCell);

                document.add(TableEightColumn);
                ////E-Banking///
                TableoneColumn = new PdfPTable(1);
                pdfCell = new PdfPCell(new Phrase("E-Banking", fontWhite));
                pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfCell.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setBackgroundColor(BaseColor.BLUE);
                TableoneColumn.addCell(pdfCell);
                document.add(TableoneColumn);
                //1st row Ebanking //
                TableEightColumn = new PdfPTable(8);
                pdfCell = new PdfPCell(new Phrase("SMS Alerts", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(3);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("E-Statement", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(3);
                TableEightColumn.addCell(pdfCell);
                document.add(TableEightColumn);
                ////Specimen Signature///
                TableoneColumn = new PdfPTable(1);
                pdfCell = new PdfPCell(new Phrase("Specimen Signature", fontWhite));
                pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfCell.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setBackgroundColor(BaseColor.BLUE);
                TableoneColumn.addCell(pdfCell);
                document.add(TableoneColumn);
                //1st row Specimen Signature //
                TableEightColumn = new PdfPTable(8);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(1);
                TableEightColumn.addCell(pdfCell);
                String filePath = findPath(mfsAccountModel, actionAuthorizationModel, PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, "proofOfProfessionPic_");
                if (filePath != "") {
                    img1 = Image.getInstance(filePath);
                    pdfCell = new PdfPCell(img1, true);
                    pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    pdfCell.setBorder(Rectangle.NO_BORDER);
                    pdfCell.setColspan(2);
                    TableEightColumn.addCell(pdfCell);
                } else {
                    pdfCell = new PdfPCell(new Phrase("Picture Not Loaded", fontBlack));
                    pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    pdfCell.setBorder(Rectangle.NO_BORDER);
                    pdfCell.setColspan(2);
                    TableEightColumn.addCell(pdfCell);
                }
//                img1 =Image.getInstance(filePath);
//                pdfCell = new PdfPCell(img1,true);
//                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                pdfCell.setBorder(Rectangle.NO_BORDER);
//                pdfCell.setColspan(2);
//                TableEightColumn.addCell(pdfCell);
                TableTwocolumn = new PdfPTable(2);
                pdfCell = new PdfPCell(new Phrase("Goe Tagging", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableTwocolumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Longitude:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableTwocolumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getLongitude(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableTwocolumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Latitude:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableTwocolumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getLatitude(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableTwocolumn.addCell(pdfCell);
                pdfCell = new PdfPCell(TableTwocolumn);
                pdfCell.setColspan(4);
                pdfCell.setBorder(Rectangle.NO_BORDER);

                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                TableEightColumn.addCell(pdfCell);
                //2nd row Specimen Signature //
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(1);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Signature", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(4);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                TableEightColumn.addCell(pdfCell);
                document.add(TableEightColumn);
                ////Client/Account Database///
                TableoneColumn = new PdfPTable(1);
                pdfCell = new PdfPCell(new Phrase("Client/Account Database", fontWhite));
                pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfCell.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setBackgroundColor(BaseColor.BLUE);
//                pdfCell.setFixedHeight(10F);
                TableoneColumn.addCell(pdfCell);
                document.add(TableoneColumn);
                TableEightColumn = new PdfPTable(8);
                //1st row client/account Database//
                pdfCell = new PdfPCell(new Phrase("Occupation/Profession", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Industry", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                //2nd row client/account Database//
                pdfCell = new PdfPCell(new Phrase("Political Exposed Person Category", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(6);
                TableEightColumn.addCell(pdfCell);
                //3rd row client /account Database//
                pdfCell = new PdfPCell(new Phrase("PEP Status", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Are You Txt Residend Of Another Country (CRS)", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(3);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);
                //4th row client /account database//
                pdfCell = new PdfPCell(new Phrase("Acceptance Of Terms & Conditions", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Yes", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Digital Consent For Account Opening and Use Of Documents/Information", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(4);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);
                document.add(TableEightColumn);

                //close
                document.close();
                System.out.println("Done");


            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            } finally {

                // closing FileOutputStream
                try {
                    if (file != null) {
                        file.close();
                    }
                } catch (IOException io) {/*Failed to close*/

                }

            }

            return null;
        }
    }


    @Override
    protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        return null;
    }

    @Override
    protected ModelAndView onResolve(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ModelAndView modelAndView = null;
        ActionAuthorizationModel model = (ActionAuthorizationModel) command;
        try {
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
            UsecaseModel usecaseModel = usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
            XStream xstream = new XStream();
            MfsAccountModel mfsAccountModel = (MfsAccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());

            ////////////////Setting Updated Images//////////////////
            ActionAuthPictureModel authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.CUSTOMER_PHOTO);
            if (authPictureModel != null && authPictureModel.getPicture() != null)
                mfsAccountModel.setCustomerPicByte(authPictureModel.getPicture());

            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.TERMS_AND_CONDITIONS_COPY);
            if (authPictureModel != null && authPictureModel.getPicture() != null)
                mfsAccountModel.setTncPicByte(authPictureModel.getPicture());

            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.SIGNATURE_SNAPSHOT);
            if (authPictureModel != null && authPictureModel.getPicture() != null)
                mfsAccountModel.setSignPicByte(authPictureModel.getPicture());

            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.ID_FRONT_SNAPSHOT);
            if (authPictureModel != null && authPictureModel.getPicture() != null)
                mfsAccountModel.setCnicFrontPicByte(authPictureModel.getPicture());

            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.ID_BACK_SNAPSHOT);
            if (authPictureModel != null && authPictureModel.getPicture() != null)
                mfsAccountModel.setCnicBackPicByte(authPictureModel.getPicture());

            authPictureModel = actionAuthorizationFacade.getActionAuthPictureModelByTypeId(model.getActionAuthorizationId(), PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT);
            if (mfsAccountModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)
                    && authPictureModel != null && authPictureModel.getPicture() != null) {
                mfsAccountModel.setLevel1FormPicByte(authPictureModel.getPicture());
            }
            ////////////////////End Setting Updated Images///////////

            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, mfsAccountModel.getActionId());
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, mfsAccountModel.getUsecaseId());
            baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID, actionAuthorizationModel.getCreatedById());
            baseWrapper.putObject(PortalConstants.KEY_CREATED_ON, actionAuthorizationModel.getCreatedOn());
            baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME, actionAuthorizationModel.getCreatedByUsername());

            baseWrapper.putObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY, mfsAccountModel);

            //this.mfsAccountManager.validateUniqueness(mfsAccountModel);//Validate Uniqueness

            if (mfsAccountModel.getUsecaseId().longValue() == PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID) {
                baseWrapper = this.mfsAccountManager.createMfsAccount(baseWrapper);
                /**
                 * Commented by atif hussain
                 * customerPendingTrxManager.makeCustomerPendingTrx(mfsAccountModel.getNic());
                 */

                if (null != mfsAccountModel.getInitialDeposit()) {
                    this.mfsAccountManager.makeInitialDeposit(mfsAccountModel);
                }
                //mfsAccountId = (String)baseWrapper.getObject(PortalConstants.KEY_MFS_ACCOUNT_ID);
                //appUserId = new Long(baseWrapper.getObject(PortalConstants.KEY_APP_USER_ID).toString());
            } else {
                //setting current Data for history
                MfsAccountModel oldMfsAccountModel = this.populateCurrentInfoModel(mfsAccountModel.getAppUserId());

                baseWrapper = this.mfsAccountManager.updateMfsAccount(baseWrapper);


                actionAuthorizationModel.setReferenceData(xstream.toXML(oldMfsAccountModel));

                ///Saving old images
                ActionAuthPictureModel actionAuthPictureModel = new ActionAuthPictureModel();
                actionAuthPictureModel.setActionAuthorizationId(model.getActionAuthorizationId());
                actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.CUSTOMER_PHOTO);
                actionAuthPictureModel.setPicture(oldMfsAccountModel.getCustomerPicByte());
                actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);

                actionAuthPictureModel = new ActionAuthPictureModel();
                actionAuthPictureModel.setActionAuthorizationId(model.getActionAuthorizationId());
                actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.TERMS_AND_CONDITIONS_COPY);
                actionAuthPictureModel.setPicture(oldMfsAccountModel.getTncPicByte());
                actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);

                actionAuthPictureModel = new ActionAuthPictureModel();
                actionAuthPictureModel.setActionAuthorizationId(model.getActionAuthorizationId());
                actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.TERMS_AND_CONDITIONS_COPY);
                actionAuthPictureModel.setPicture(oldMfsAccountModel.getTncPicByte());
                actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);

                actionAuthPictureModel = new ActionAuthPictureModel();
                actionAuthPictureModel.setActionAuthorizationId(model.getActionAuthorizationId());
                actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.SIGNATURE_SNAPSHOT);
                actionAuthPictureModel.setPicture(oldMfsAccountModel.getSignPicByte());
                actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);

                actionAuthPictureModel = new ActionAuthPictureModel();
                actionAuthPictureModel.setActionAuthorizationId(model.getActionAuthorizationId());
                actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.ID_FRONT_SNAPSHOT);
                actionAuthPictureModel.setPicture(oldMfsAccountModel.getCnicFrontPicByte());
                actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);

                actionAuthPictureModel = new ActionAuthPictureModel();
                actionAuthPictureModel.setActionAuthorizationId(model.getActionAuthorizationId());
                actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.ID_BACK_SNAPSHOT);
                actionAuthPictureModel.setPicture(oldMfsAccountModel.getCnicBackPicByte());
                actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);

                if (mfsAccountModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {
                    actionAuthPictureModel = new ActionAuthPictureModel();
                    actionAuthPictureModel.setActionAuthorizationId(model.getActionAuthorizationId());
                    actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT);
                    actionAuthPictureModel.setPicture(oldMfsAccountModel.getLevel1FormPicByte());
                    actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                }
                //End setting current Data for history
            }

            resolveWithIntimation(actionAuthorizationModel, model, usecaseModel, request);

        } catch (FrameworkCheckedException ex) {
            if ("MobileNumUniqueException".equals(ex.getMessage())) {
                request.setAttribute("message", super.getText("newMfsAccount.mobileNumNotUnique", request.getLocale()));
            } else if ("NICUniqueException".equals(ex.getMessage())) {
                request.setAttribute("message", super.getText("newMfsAccount.nicNotUnique", request.getLocale()));
            } else if ("AccountOpeningCommissionException".equals(ex.getMessage())) {
                request.setAttribute("message", super.getText("newMfsAccount.commission.failed", request.getLocale()));
            } else {
                if (null != ex.getMessage())
                    request.setAttribute("message", ex.getMessage().replaceAll("<br>", ""));
                else
                    request.setAttribute("message", MessageUtil.getMessage("6075"));
            }

            LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : " + model.getActionAuthorizationId(), ex);
            request.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(request, response, errors);
        } catch (Exception ex) {

            request.setAttribute("message", MessageUtil.getMessage("6075"));
            LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : " + model.getActionAuthorizationId(), ex);
            request.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(request, response, errors);
        }
        request.setAttribute("status", IssueTypeStatusConstantsInterface.SUCCESS);
        modelAndView = super.showForm(request, response, errors);
        return modelAndView;
    }

    private void removeSpecialAccountTypes(CopyOnWriteArrayList<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList) {

        for (OlaCustomerAccountTypeModel model : olaCustomerAccountTypeModelList) {
            if (model.getCustomerAccountTypeId().longValue() == CustomerAccountTypeConstants.SETTLEMENT
                    || model.getCustomerAccountTypeId().longValue() == UserTypeConstantsInterface.WALKIN_CUSTOMER) {
                olaCustomerAccountTypeModelList.remove(model);
            }
        }
    }

    private MfsAccountModel populateCurrentInfoModel(Long appUserId) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setAppUserId(appUserId);
        baseWrapper.setBasePersistableModel(appUserModel);
        baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);

        appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
        MfsAccountModel mfsAccountModel = new MfsAccountModel();
        mfsAccountModel.setFirstName(appUserModel.getFirstName());
        mfsAccountModel.setLastName(appUserModel.getLastName());
        mfsAccountModel.setMobileNo(appUserModel.getMobileNo());
        mfsAccountModel.setAppUserId(appUserModel.getAppUserId());
        mfsAccountModel.setNic(appUserModel.getNic());
        mfsAccountModel.setNicExpiryDate(appUserModel.getNicExpiryDate());
        mfsAccountModel.setRegistrationStateId(appUserModel.getRegistrationStateId());
        mfsAccountModel.setDob(appUserModel.getDob());
        mfsAccountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
        mfsAccountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
        mfsAccountModel.setMfsId(appUserModel.getUsername());
        mfsAccountModel.setMotherMaidenName(appUserModel.getMotherMaidenName());


        try {
            OLAVO olaVo = this.mfsAccountManager.getAccountInfoFromOLA(appUserModel.getNic(), CommissionConstantsInterface.BANK_ID);
            if (olaVo != null) {
                mfsAccountModel.setAccountNo(olaVo.getPayingAccNo());
            }
        } catch (Exception ex) {
            log.warn("Exception while getting customer info from OLA: " + ex.getStackTrace());
        }

        if (null != appUserModel.getClosedByAppUserModel()) {
            mfsAccountModel.setClosedBy(appUserModel.getClosedByAppUserModel().getUsername());
        }
        mfsAccountModel.setClosedOn(appUserModel.getClosedOn());
        if (null != appUserModel.getSettledByAppUserModel()) {
            mfsAccountModel.setSettledBy(appUserModel.getSettledByAppUserModel().getUsername());
        }
        mfsAccountModel.setSettledOn(appUserModel.getSettledOn());
        mfsAccountModel.setClosingComments(appUserModel.getClosingComments());
        mfsAccountModel.setSettlementComments(appUserModel.getSettlementComments());
        mfsAccountModel.setDialingCode(appUserModel.getMotherMaidenName());

        CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
        if (customerModel != null) {
            mfsAccountModel.setName(customerModel.getName());
            mfsAccountModel.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
            mfsAccountModel.setMobileNo(customerModel.getMobileNo());
            mfsAccountModel.setSegmentId(customerModel.getSegmentId());
            mfsAccountModel.setFax(customerModel.getFax());
            mfsAccountModel.setFed(customerModel.getFed());
            mfsAccountModel.setTaxRegimeName(customerModel.getTaxRegimeIdTaxRegimeModel() == null ? null : customerModel.getTaxRegimeIdTaxRegimeModel().getName());
            mfsAccountModel.setCurrency(customerModel.getCurrency());
            mfsAccountModel.setGender(customerModel.getGender());
            mfsAccountModel.setCustomerTypeId(customerModel.getCustomerTypeId());
            mfsAccountModel.setFatherHusbandName(customerModel.getFatherHusbandName());
            mfsAccountModel.setFatherCnic(customerModel.getFatherCnicNo());
            mfsAccountModel.setNokName(customerModel.getNokName());
            mfsAccountModel.setNokNic(customerModel.getNokNic());
            mfsAccountModel.setNokRelationship(customerModel.getNokRelationship());
            mfsAccountModel.setNokMobile(customerModel.getNokMobile());

            if (null != customerModel.getBirthPlace()) {
                mfsAccountModel.setBirthPlaceName(customerModel.getBirthPlace());
            }

            mfsAccountModel.setEmail(customerModel.getEmail());

            mfsAccountModel.setLandLineNo(customerModel.getLandLineNo());
            mfsAccountModel.setContactNo(customerModel.getContactNo());
            mfsAccountModel.setTransactionModeId(customerModel.getTransactionModeId());
            mfsAccountModel.setAccountPurposeId(customerModel.getAccountPurposeId());
            mfsAccountModel.setRegStateComments(customerModel.getRegStateComments());
            mfsAccountModel.setCreatedOn(customerModel.getCreatedOn());
            mfsAccountModel.setComments(customerModel.getComments());
            mfsAccountModel.setCnicSeen(customerModel.getIsCnicSeen());
            if(customerModel.getBvs() != null) {
                mfsAccountModel.setFatherBvs(customerModel.getBvs());
            }
            mfsAccountModel.setVerisysDone(customerModel.getVerisysDone());
            mfsAccountModel.setScreeningPerformed(customerModel.isScreeningPerformed());

            /***
             * Populating Customer Source of Funds
             */

            CustomerFundSourceModel customerFundSourceModel = new CustomerFundSourceModel();
            customerFundSourceModel.setCustomerId(customerModel.getCustomerId());
            ReferenceDataWrapper customerFundSourceReferenceDataWrapper = new ReferenceDataWrapperImpl(customerFundSourceModel, "customerFundSourceId", SortingOrder.ASC);
            customerFundSourceReferenceDataWrapper.setBasePersistableModel(customerFundSourceModel);
            try {
                referenceDataManager.getReferenceData(customerFundSourceReferenceDataWrapper);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            List<CustomerFundSourceModel> customerFundSourceList = null;
            if (customerFundSourceReferenceDataWrapper.getReferenceDataList() != null) {
                customerFundSourceList = customerFundSourceReferenceDataWrapper.getReferenceDataList();
            }
            String[] fundSourceName = new String[customerFundSourceList.size()];
            int i = 0;
            for (CustomerFundSourceModel customerFundSource : customerFundSourceList) {
                fundSourceName[i] = customerFundSource.getFundSourceIdFundSourceModel().getName();
                i++;
            }
            mfsAccountModel.setFundSourceName(fundSourceName);

            CustomerPictureModel customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                    PictureTypeConstants.CUSTOMER_PHOTO, customerModel.getCustomerId().longValue());

            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.CUSTOMER_PHOTO) {
                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                    mfsAccountModel.setCustomerPicByte(customerPictureModel.getPicture());
                }
            }

            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                    PictureTypeConstants.PARENT_CNIC_SNAPSHOT, customerModel.getCustomerId().longValue());

            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PARENT_CNIC_SNAPSHOT) {
                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                    mfsAccountModel.setParentCnicPicByte(customerPictureModel.getPicture());
                }
            }

            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                    PictureTypeConstants.B_FORM_SNAPSHOT, customerModel.getCustomerId().longValue());

            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.B_FORM_SNAPSHOT) {
                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                    mfsAccountModel.setbFormPicByte(customerPictureModel.getPicture());
                }
            }

            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                    PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());

            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT) {
                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                    mfsAccountModel.setParentCnicBackPicByte(customerPictureModel.getPicture());
                }
            }

            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                    PictureTypeConstants.TERMS_AND_CONDITIONS_COPY, customerModel.getCustomerId().longValue());

            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.TERMS_AND_CONDITIONS_COPY) {
                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                    mfsAccountModel.setTncPicByte(customerPictureModel.getPicture());
                }
            }
            if (mfsAccountModel.getCustomerAccountTypeId() == 53) {
                customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                        PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, customerModel.getCustomerId().longValue());

                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        mfsAccountModel.setProofOfProfessionPicByte(customerPictureModel.getPicture());
                    }
                }
                customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                        PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT, customerModel.getCustomerId().longValue());

                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        mfsAccountModel.setSourceOfIncomeByte(customerPictureModel.getPicture());
                    }
                }
            }

            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                    PictureTypeConstants.SIGNATURE_SNAPSHOT, customerModel.getCustomerId().longValue());

            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SIGNATURE_SNAPSHOT) {
                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                    mfsAccountModel.setSignPicByte(customerPictureModel.getPicture());
                }
            }

            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                    PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModel.getCustomerId().longValue());

            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_FRONT_SNAPSHOT) {
                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                    mfsAccountModel.setCnicFrontPicByte(customerPictureModel.getPicture());
                }
            }

            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                    PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());

            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_BACK_SNAPSHOT) {
                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                    mfsAccountModel.setCnicBackPicByte(customerPictureModel.getPicture());
                }
            }

            if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {
                customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                        PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT, customerModel.getCustomerId().longValue());

                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        mfsAccountModel.setLevel1FormPicByte(customerPictureModel.getPicture());
                    }
                }
            }
            // Populating Address Fields
            Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
            if (customerAddresses != null && customerAddresses.size() > 0) {
                for (CustomerAddressesModel custAdd : customerAddresses) {
                    AddressModel addressModel = custAdd.getAddressIdAddressModel();
                    if (custAdd.getAddressTypeId() == 1) {
                        if (addressModel.getFullAddress() != null && !addressModel.getFullAddress().isEmpty()) {
                            mfsAccountModel.setPresentAddress(addressModel.getFullAddress());
                        }
                        if (addressModel.getCityId() != null) {
                            mfsAccountModel.setCity(addressModel.getCityIdCityModel().getName());
                        }
                    } else if (custAdd.getAddressTypeId() == 4) {
                        if (addressModel.getFullAddress() != null && !addressModel.getFullAddress().isEmpty()) {
                            mfsAccountModel.setNokMailingAdd(addressModel.getFullAddress());
                        }
                    }
                }
            }
        }

        /////Load Reference Data

        Map referenceDataMap = new HashMap();
        try {
            SegmentModel segmentModel = new SegmentModel();
            segmentModel.setIsActive(true);
            ReferenceDataWrapper segmentReferenceDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
            segmentReferenceDataWrapper.setBasePersistableModel(segmentModel);
            referenceDataManager.getReferenceData(segmentReferenceDataWrapper);


            List<SegmentModel> segmentList = null;
            if (segmentReferenceDataWrapper.getReferenceDataList() != null) {
                segmentList = segmentReferenceDataWrapper.getReferenceDataList();

                for (SegmentModel segmentModel2 : segmentList) {

                    if (null != mfsAccountModel.getSegmentId() && (mfsAccountModel.getSegmentId().longValue() == segmentModel2.getSegmentId().longValue()))
                        mfsAccountModel.setSegmentNameStr(segmentModel2.getName());
                }
            }

            CustomerTypeModel customerTypeModel = new CustomerTypeModel();
            ReferenceDataWrapper customerTypeDataWrapper = new ReferenceDataWrapperImpl(customerTypeModel, "name", SortingOrder.ASC);
            customerTypeDataWrapper.setBasePersistableModel(customerTypeModel);
            referenceDataManager.getReferenceData(customerTypeDataWrapper);

            List<CustomerTypeModel> customerTypeList = null;
            if (customerTypeDataWrapper.getReferenceDataList() != null) {
                customerTypeList = customerTypeDataWrapper.getReferenceDataList();

                for (CustomerTypeModel customerTypeModel2 : customerTypeList) {
                    if (null != mfsAccountModel.getCustomerTypeId() && (mfsAccountModel.getCustomerTypeId().longValue() == customerTypeModel2.getCustomerTypeId().longValue()))
                        mfsAccountModel.setCustomerTypeName(customerTypeModel2.getName());
                }
            }

            OlaCustomerAccountTypeModel customerAccountTypeModel = new OlaCustomerAccountTypeModel();
            customerAccountTypeModel.setActive(true);
            customerAccountTypeModel.setIsCustomerAccountType(true); //added by Turab
            ReferenceDataWrapper customerAccountTypeDataWrapper = new ReferenceDataWrapperImpl(customerAccountTypeModel, "name", SortingOrder.ASC);
            customerAccountTypeDataWrapper.setBasePersistableModel(customerAccountTypeModel);
            referenceDataManager.getReferenceData(customerAccountTypeDataWrapper);


            CopyOnWriteArrayList<OlaCustomerAccountTypeModel> customerAccountTypeList = null;
            if (customerAccountTypeDataWrapper.getReferenceDataList() != null) {
                customerAccountTypeList = new CopyOnWriteArrayList<OlaCustomerAccountTypeModel>(customerAccountTypeDataWrapper.getReferenceDataList());
                if (!CollectionUtils.isEmpty(customerAccountTypeList)) {
                    //remove special account types from screen. like settlemnt account type is used for commission settlemnt in OLA and walkin customer. it needs to be removed
                    //because it is for system use only.
                    removeSpecialAccountTypes(customerAccountTypeList);

                    for (OlaCustomerAccountTypeModel olaCustomerAccountTypeModel : customerAccountTypeList) {

                        if (null != mfsAccountModel.getCustomerAccountTypeId() && (mfsAccountModel.getCustomerAccountTypeId().longValue() == olaCustomerAccountTypeModel.getCustomerAccountTypeId().longValue()))
                            mfsAccountModel.setAccounttypeName(olaCustomerAccountTypeModel.getName());
                    }
                }

            }

            if (mfsAccountModel.getGender() == "M")
                mfsAccountModel.setGender("Male");
            else if (mfsAccountModel.getGender() == "F")
                mfsAccountModel.setGender("Female");
            else if (mfsAccountModel.getGender() == "K")
                mfsAccountModel.setGender("Khwaja Sira");

            Long[] regStateList = {RegistrationStateConstantsInterface.DECLINE, RegistrationStateConstantsInterface.DISCREPANT,
                    RegistrationStateConstantsInterface.VERIFIED, RegistrationStateConstants.BLOCKED, RegistrationStateConstants.CLSPENDING};
            List<RegistrationStateModel> regStates = commonCommandManager.getRegistrationStateByIds(regStateList).getResultsetList();

            for (RegistrationStateModel registrationStateModel : regStates) {
                if (null != mfsAccountModel.getRegistrationStateId() && (mfsAccountModel.getRegistrationStateId().longValue() == registrationStateModel.getRegistrationStateId().longValue()))
                    mfsAccountModel.setRegStateName(registrationStateModel.getName());
            }


            TransactionModeModel transactionModeModel = new TransactionModeModel();
            ReferenceDataWrapper transactionModeReferenceDataWrapper = new ReferenceDataWrapperImpl(transactionModeModel, "transactionModeId", SortingOrder.ASC);
            transactionModeReferenceDataWrapper.setBasePersistableModel(transactionModeModel);
            try {
                referenceDataManager.getReferenceData(transactionModeReferenceDataWrapper);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            List<TransactionModeModel> transactionModeList = null;
            if (transactionModeReferenceDataWrapper.getReferenceDataList() != null) {
                transactionModeList = transactionModeReferenceDataWrapper.getReferenceDataList();
                for (TransactionModeModel transactionModeModel2 : transactionModeList) {
                    if (null != mfsAccountModel.getTransactionModeId() && (mfsAccountModel.getTransactionModeId().longValue() == transactionModeModel2.getTransactionModeId().longValue()))
                        mfsAccountModel.setUsualModeOfTrans(transactionModeModel2.getName());
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception();
        }

        return mfsAccountModel;
    }

    private MfsAccountModel populateCurrentInfoModelData(Long appUserId, MfsAccountModel mfsModel) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setAppUserId(appUserId);
        baseWrapper.setBasePersistableModel(appUserModel);
        baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);

        appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
        MfsAccountModel mfsAccountModel = new MfsAccountModel();
        mfsAccountModel.setFirstName(appUserModel.getFirstName());
        mfsAccountModel.setLastName(appUserModel.getLastName());
        mfsAccountModel.setMobileNo(appUserModel.getMobileNo());
        mfsAccountModel.setAppUserId(appUserModel.getAppUserId());
        mfsAccountModel.setNic(appUserModel.getNic());
        mfsAccountModel.setNicExpiryDate(appUserModel.getNicExpiryDate());
        mfsAccountModel.setRegistrationStateId(appUserModel.getRegistrationStateId());
        mfsAccountModel.setDob(appUserModel.getDob());
        mfsAccountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
        mfsAccountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
        mfsAccountModel.setMfsId(appUserModel.getUsername());
        mfsAccountModel.setMotherMaidenName(appUserModel.getMotherMaidenName());
        if (mfsModel.getCustomerAccountTypeId().equals(53l) && mfsModel.getCustomerPicRej() != null && mfsModel.getRiskLevel() != null) {
            mfsAccountModel.setEmailRej(mfsModel.getEmailRej());
            mfsAccountModel.setEmailApp(mfsModel.getEmailApp());
            mfsAccountModel.setMobApp(mfsModel.getMobApp());
            mfsAccountModel.setMobRej(mfsModel.getMobRej());
            mfsAccountModel.setCustomerPicApp(mfsModel.getCustomerPicApp());
            mfsAccountModel.setCustomerPicRej(mfsModel.getCustomerPicRej());
            mfsAccountModel.setCnicRej(mfsModel.getCnicRej());
            mfsAccountModel.setCnicApp(mfsModel.getCnicApp());
            mfsAccountModel.setCnicFrontApp(mfsModel.getCnicFrontApp());
            mfsAccountModel.setCnicFrontRej(mfsModel.getCnicRej());
            mfsAccountModel.setCnicBackApp(mfsModel.getCnicBackApp());
            mfsAccountModel.setCnicBackRej(mfsModel.getCnicBackRej());
            mfsAccountModel.setPopApp(mfsModel.getPopApp());
            mfsAccountModel.setPopRej(mfsModel.getPopRej());
            mfsAccountModel.setSourcePicApp(mfsModel.getSourcePicApp());
            mfsAccountModel.setSourcePicRej(mfsModel.getSourcePicRej());
            mfsAccountModel.setpAddressApp(mfsModel.getpAddressApp());
            mfsAccountModel.setpAddressRej(mfsModel.getpAddressRej());
            mfsAccountModel.setPoaApp(mfsModel.getPoaApp());
            mfsAccountModel.setPoaRej(mfsModel.getPoaRej());
            mfsAccountModel.setFhApp(mfsModel.getFhApp());
            mfsAccountModel.setFhRej(mfsModel.getFhRej());
            mfsAccountModel.setMailingApp(mfsModel.getMailingApp());
            mfsAccountModel.setMailingRej(mfsModel.getMailingRej());
            mfsAccountModel.setTurnOverApp(mfsModel.getTurnOverApp());
            mfsAccountModel.setTurnOverRej(mfsModel.getTurnOverRej());
            mfsAccountModel.setClsResponseCode(mfsModel.getClsResponseCode());
            mfsAccountModel.setRiskLevel(mfsModel.getRiskLevel());
            mfsAccountModel.setAccountPurposeName(mfsModel.getAccountPurposeName());
            mfsAccountModel.setLatitude(mfsModel.getLatitude());
            mfsAccountModel.setLongitude(mfsModel.getLongitude());
            mfsAccountModel.setUsCitizen(mfsModel.getUsCitizen());
            mfsAccountModel.setDualNationality(mfsModel.getDualNationality());
            mfsAccountModel.setExpectedMonthlyTurnOver(mfsModel.getExpectedMonthlyTurnOver());
            mfsAccountModel.setMailingAddress(mfsModel.getMailingAddress());
            mfsAccountModel.setCity(mfsModel.getCity());
            mfsAccountModel.setComments(mfsModel.getComments());
            mfsAccountModel.setCustomerPicDiscrepant(mfsAccountModel.getCustomerPicDiscrepant());
            mfsAccountModel.setCnicFrontPicDiscrepant(mfsAccountModel.getCnicFrontPicDiscrepant());
            mfsAccountModel.setbFormPicDiscrepant(mfsAccountModel.getbFormPicDiscrepant());
            mfsAccountModel.setParentCnicPicDiscrepant(mfsAccountModel.getParentCnicPicDiscrepant());
        }
        if(mfsModel.getUsecaseId().equals(PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID)){
            mfsAccountModel.setCustPicCheckerComments(mfsModel.getCustPicCheckerComments());
            mfsAccountModel.setpNicPicCheckerComments(mfsModel.getpNicPicCheckerComments());
            mfsAccountModel.setbFormPicCheckerComments(mfsModel.getbFormPicCheckerComments());
            mfsAccountModel.setNicBackPicCheckerComments(mfsModel.getNicBackPicCheckerComments());
            mfsAccountModel.setNicFrontPicCheckerComments(mfsModel.getNicFrontPicCheckerComments());
            mfsAccountModel.setpNicBackPicCheckerComments(mfsModel.getpNicBackPicCheckerComments());
            mfsAccountModel.setCustPicMakerComments(mfsModel.getCustPicMakerComments());
            mfsAccountModel.setbFormPicMakerComments(mfsModel.getbFormPicMakerComments());
            mfsAccountModel.setpNicPicMakerComments(mfsModel.getpNicPicMakerComments());
            mfsAccountModel.setNicBackPicMakerComments(mfsModel.getNicBackPicMakerComments());
            mfsAccountModel.setNicFrontPicMakerComments(mfsModel.getNicFrontPicMakerComments());
            mfsAccountModel.setpNicBackPicMakerComments(mfsModel.getpNicBackPicMakerComments());
            mfsAccountModel.setCustomerPicDiscrepant(mfsModel.getCustomerPicDiscrepant());
            mfsAccountModel.setCnicFrontPicDiscrepant(mfsModel.getCnicFrontPicDiscrepant());
            mfsAccountModel.setbFormPicDiscrepant(mfsModel.getbFormPicDiscrepant());
            mfsAccountModel.setParentCnicPicDiscrepant(mfsModel.getParentCnicPicDiscrepant());
            mfsAccountModel.setParentCnicBackPicDiscrepant(mfsModel.getParentCnicBackPicDiscrepant());
            mfsAccountModel.setCnicBackPicDiscrepant(mfsModel.getCnicBackPicDiscrepant());
        }


        try {
            OLAVO olaVo = this.mfsAccountManager.getAccountInfoFromOLA(appUserModel.getNic(), CommissionConstantsInterface.BANK_ID);
            if (olaVo != null) {
                mfsAccountModel.setAccountNo(olaVo.getPayingAccNo());
            }
        } catch (Exception ex) {
            log.warn("Exception while getting customer info from OLA: " + ex.getStackTrace());
        }

        if (null != appUserModel.getClosedByAppUserModel()) {
            mfsAccountModel.setClosedBy(appUserModel.getClosedByAppUserModel().getUsername());
        }
        mfsAccountModel.setClosedOn(appUserModel.getClosedOn());
        if (null != appUserModel.getSettledByAppUserModel()) {
            mfsAccountModel.setSettledBy(appUserModel.getSettledByAppUserModel().getUsername());
        }
        mfsAccountModel.setSettledOn(appUserModel.getSettledOn());
        mfsAccountModel.setClosingComments(appUserModel.getClosingComments());
        mfsAccountModel.setSettlementComments(appUserModel.getSettlementComments());
        mfsAccountModel.setDialingCode(appUserModel.getMotherMaidenName());

        CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
        if (customerModel != null) {
            mfsAccountModel.setName(customerModel.getName());
            mfsAccountModel.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
            mfsAccountModel.setMobileNo(customerModel.getMobileNo());
            mfsAccountModel.setSegmentId(customerModel.getSegmentId());
            mfsAccountModel.setFax(customerModel.getFax());
            mfsAccountModel.setFed(customerModel.getFed());
            mfsAccountModel.setTaxRegimeName(customerModel.getTaxRegimeIdTaxRegimeModel() == null ? null : customerModel.getTaxRegimeIdTaxRegimeModel().getName());
            mfsAccountModel.setCurrency(customerModel.getCurrency());
            mfsAccountModel.setGender(customerModel.getGender());
            mfsAccountModel.setCustomerTypeId(customerModel.getCustomerTypeId());
            mfsAccountModel.setFatherHusbandName(customerModel.getFatherHusbandName());
            mfsAccountModel.setNokName(customerModel.getNokName());
            mfsAccountModel.setNokNic(customerModel.getNokNic());
            mfsAccountModel.setNokRelationship(customerModel.getNokRelationship());
            mfsAccountModel.setNokMobile(customerModel.getNokMobile());

            if (null != customerModel.getBirthPlace()) {
                mfsAccountModel.setBirthPlaceName(customerModel.getBirthPlace());
            }

            mfsAccountModel.setEmail(customerModel.getEmail());

            mfsAccountModel.setLandLineNo(customerModel.getLandLineNo());
            mfsAccountModel.setContactNo(customerModel.getContactNo());
            mfsAccountModel.setTransactionModeId(customerModel.getTransactionModeId());
            mfsAccountModel.setAccountPurposeId(customerModel.getAccountPurposeId());
            mfsAccountModel.setRegStateComments(customerModel.getRegStateComments());
            mfsAccountModel.setCreatedOn(customerModel.getCreatedOn());
            mfsAccountModel.setComments(customerModel.getComments());
            mfsAccountModel.setCnicSeen(customerModel.getIsCnicSeen());
            if(customerModel.getBvs() != null) {
                mfsAccountModel.setFatherBvs(customerModel.getBvs());
            }
            mfsAccountModel.setVerisysDone(customerModel.getVerisysDone());
            mfsAccountModel.setScreeningPerformed(customerModel.isScreeningPerformed());

            /***
             * Populating Customer Source of Funds
             */

            CustomerFundSourceModel customerFundSourceModel = new CustomerFundSourceModel();
            customerFundSourceModel.setCustomerId(customerModel.getCustomerId());
            ReferenceDataWrapper customerFundSourceReferenceDataWrapper = new ReferenceDataWrapperImpl(customerFundSourceModel, "customerFundSourceId", SortingOrder.ASC);
            customerFundSourceReferenceDataWrapper.setBasePersistableModel(customerFundSourceModel);
            try {
                referenceDataManager.getReferenceData(customerFundSourceReferenceDataWrapper);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            List<CustomerFundSourceModel> customerFundSourceList = null;
            if (customerFundSourceReferenceDataWrapper.getReferenceDataList() != null) {
                customerFundSourceList = customerFundSourceReferenceDataWrapper.getReferenceDataList();
            }
            String[] fundSourceName = new String[customerFundSourceList.size()];
            int i = 0;
            for (CustomerFundSourceModel customerFundSource : customerFundSourceList) {
                fundSourceName[i] = customerFundSource.getFundSourceIdFundSourceModel().getName();
                i++;
            }
            mfsAccountModel.setFundSourceName(fundSourceName);

            CustomerPictureModel customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                    PictureTypeConstants.CUSTOMER_PHOTO, customerModel.getCustomerId().longValue());

            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.CUSTOMER_PHOTO) {
                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                    mfsAccountModel.setCustomerPicByte(customerPictureModel.getPicture());
                }
            }

            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                    PictureTypeConstants.TERMS_AND_CONDITIONS_COPY, customerModel.getCustomerId().longValue());

            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.TERMS_AND_CONDITIONS_COPY) {
                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                    mfsAccountModel.setTncPicByte(customerPictureModel.getPicture());
                }
            }
            if (mfsAccountModel.getCustomerAccountTypeId() == 53) {
                customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                        PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, customerModel.getCustomerId().longValue());

                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        mfsAccountModel.setProofOfProfessionPicByte(customerPictureModel.getPicture());
                    }
                }
                customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                        PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT, customerModel.getCustomerId().longValue());

                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        mfsAccountModel.setSourceOfIncomeByte(customerPictureModel.getPicture());
                    }
                }
            }

            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                    PictureTypeConstants.SIGNATURE_SNAPSHOT, customerModel.getCustomerId().longValue());

            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SIGNATURE_SNAPSHOT) {
                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                    mfsAccountModel.setSignPicByte(customerPictureModel.getPicture());
                }
            }

            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                    PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModel.getCustomerId().longValue());

            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_FRONT_SNAPSHOT) {
                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                    mfsAccountModel.setCnicFrontPicByte(customerPictureModel.getPicture());
                }
            }

            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                    PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());

            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_BACK_SNAPSHOT) {
                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                    mfsAccountModel.setCnicBackPicByte(customerPictureModel.getPicture());
                }
            }

            if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {
                customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                        PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT, customerModel.getCustomerId().longValue());

                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT) {
                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                        mfsAccountModel.setLevel1FormPicByte(customerPictureModel.getPicture());
                    }
                }
            }
            // Populating Address Fields
            Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
            if (customerAddresses != null && customerAddresses.size() > 0) {
                for (CustomerAddressesModel custAdd : customerAddresses) {
                    AddressModel addressModel = custAdd.getAddressIdAddressModel();
                    if (custAdd.getAddressTypeId() == 1) {
                        if (addressModel.getFullAddress() != null && !addressModel.getFullAddress().isEmpty()) {
                            mfsAccountModel.setPresentAddress(addressModel.getFullAddress());
                        }
                        if (addressModel.getCityId() != null) {
                            mfsAccountModel.setCity(addressModel.getCityIdCityModel().getName());
                        }
                    } else if (custAdd.getAddressTypeId() == 4) {
                        if (addressModel.getFullAddress() != null && !addressModel.getFullAddress().isEmpty()) {
                            mfsAccountModel.setNokMailingAdd(addressModel.getFullAddress());
                        }
                    }
                }
            }
        }

        /////Load Reference Data

        Map referenceDataMap = new HashMap();
        try {
            SegmentModel segmentModel = new SegmentModel();
            segmentModel.setIsActive(true);
            ReferenceDataWrapper segmentReferenceDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
            segmentReferenceDataWrapper.setBasePersistableModel(segmentModel);
            referenceDataManager.getReferenceData(segmentReferenceDataWrapper);


            List<SegmentModel> segmentList = null;
            if (segmentReferenceDataWrapper.getReferenceDataList() != null) {
                segmentList = segmentReferenceDataWrapper.getReferenceDataList();

                for (SegmentModel segmentModel2 : segmentList) {

                    if (null != mfsAccountModel.getSegmentId() && (mfsAccountModel.getSegmentId().longValue() == segmentModel2.getSegmentId().longValue()))
                        mfsAccountModel.setSegmentNameStr(segmentModel2.getName());
                }
            }

            CustomerTypeModel customerTypeModel = new CustomerTypeModel();
            ReferenceDataWrapper customerTypeDataWrapper = new ReferenceDataWrapperImpl(customerTypeModel, "name", SortingOrder.ASC);
            customerTypeDataWrapper.setBasePersistableModel(customerTypeModel);
            referenceDataManager.getReferenceData(customerTypeDataWrapper);

            List<CustomerTypeModel> customerTypeList = null;
            if (customerTypeDataWrapper.getReferenceDataList() != null) {
                customerTypeList = customerTypeDataWrapper.getReferenceDataList();

                for (CustomerTypeModel customerTypeModel2 : customerTypeList) {
                    if (null != mfsAccountModel.getCustomerTypeId() && (mfsAccountModel.getCustomerTypeId().longValue() == customerTypeModel2.getCustomerTypeId().longValue()))
                        mfsAccountModel.setCustomerTypeName(customerTypeModel2.getName());
                }
            }

            OlaCustomerAccountTypeModel customerAccountTypeModel = new OlaCustomerAccountTypeModel();
            customerAccountTypeModel.setActive(true);
            customerAccountTypeModel.setIsCustomerAccountType(true); //added by Turab
            ReferenceDataWrapper customerAccountTypeDataWrapper = new ReferenceDataWrapperImpl(customerAccountTypeModel, "name", SortingOrder.ASC);
            customerAccountTypeDataWrapper.setBasePersistableModel(customerAccountTypeModel);
            referenceDataManager.getReferenceData(customerAccountTypeDataWrapper);


            CopyOnWriteArrayList<OlaCustomerAccountTypeModel> customerAccountTypeList = null;
            if (customerAccountTypeDataWrapper.getReferenceDataList() != null) {
                customerAccountTypeList = new CopyOnWriteArrayList<OlaCustomerAccountTypeModel>(customerAccountTypeDataWrapper.getReferenceDataList());
                if (!CollectionUtils.isEmpty(customerAccountTypeList)) {
                    //remove special account types from screen. like settlemnt account type is used for commission settlemnt in OLA and walkin customer. it needs to be removed
                    //because it is for system use only.
                    removeSpecialAccountTypes(customerAccountTypeList);

                    for (OlaCustomerAccountTypeModel olaCustomerAccountTypeModel : customerAccountTypeList) {

                        if (null != mfsAccountModel.getCustomerAccountTypeId() && (mfsAccountModel.getCustomerAccountTypeId().longValue() == olaCustomerAccountTypeModel.getCustomerAccountTypeId().longValue()))
                            mfsAccountModel.setAccounttypeName(olaCustomerAccountTypeModel.getName());
                    }
                }

            }

            if (mfsAccountModel.getGender() == "M")
                mfsAccountModel.setGender("Male");
            else if (mfsAccountModel.getGender() == "F")
                mfsAccountModel.setGender("Female");
            else if (mfsAccountModel.getGender() == "K")
                mfsAccountModel.setGender("Khwaja Sira");

            Long[] regStateList = {RegistrationStateConstantsInterface.DECLINE, RegistrationStateConstantsInterface.DISCREPANT, RegistrationStateConstantsInterface.VERIFIED};
            List<RegistrationStateModel> regStates = commonCommandManager.getRegistrationStateByIds(regStateList).getResultsetList();

            for (RegistrationStateModel registrationStateModel : regStates) {
                if (null != mfsAccountModel.getRegistrationStateId() && (mfsAccountModel.getRegistrationStateId().longValue() == registrationStateModel.getRegistrationStateId().longValue()))
                    mfsAccountModel.setRegStateName(registrationStateModel.getName());
            }


            TransactionModeModel transactionModeModel = new TransactionModeModel();
            ReferenceDataWrapper transactionModeReferenceDataWrapper = new ReferenceDataWrapperImpl(transactionModeModel, "transactionModeId", SortingOrder.ASC);
            transactionModeReferenceDataWrapper.setBasePersistableModel(transactionModeModel);
            try {
                referenceDataManager.getReferenceData(transactionModeReferenceDataWrapper);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            List<TransactionModeModel> transactionModeList = null;
            if (transactionModeReferenceDataWrapper.getReferenceDataList() != null) {
                transactionModeList = transactionModeReferenceDataWrapper.getReferenceDataList();
                for (TransactionModeModel transactionModeModel2 : transactionModeList) {
                    if (null != mfsAccountModel.getTransactionModeId() && (mfsAccountModel.getTransactionModeId().longValue() == transactionModeModel2.getTransactionModeId().longValue()))
                        mfsAccountModel.setUsualModeOfTrans(transactionModeModel2.getName());
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception();
        }

        return mfsAccountModel;
    }

    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setCustomerPendingTrxManager(
            CustomerPendingTrxManager customerPendingTrxManager) {
        this.customerPendingTrxManager = customerPendingTrxManager;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    public void setBlinkCustomerModelDAO(BlinkCustomerModelDAO blinkCustomerModelDAO) {
        this.blinkCustomerModelDAO = blinkCustomerModelDAO;
    }

    public void setAppUserDAO(AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }

    public void setCityDAO(CityDAO cityDAO) {
        this.cityDAO = cityDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }
}