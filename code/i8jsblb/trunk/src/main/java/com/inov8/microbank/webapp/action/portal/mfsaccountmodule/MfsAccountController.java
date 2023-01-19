package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthPictureModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.bulkdisbursements.CustomerPendingTrxManager;
import com.inov8.microbank.server.service.fileloader.ArbitraryResourceLoader;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.tax.service.TaxManager;
import com.inov8.microbank.webapp.action.allpayweb.AllPayRequestWrapper;
import com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.service.limit.LimitManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.LimitTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;
import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.*;

public class MfsAccountController extends AdvanceAuthorizationFormController {


    private static final Logger LOGGER = Logger.getLogger(MfsAccountController.class);
    private MfsAccountManager mfsAccountManager;
    private ReferenceDataManager referenceDataManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private CustomerPendingTrxManager customerPendingTrxManager;
    private CommonCommandManager commonCommandManager;
    private AppUserManager appUserManager;
    private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator = null;
    private ArbitraryResourceLoader arbitraryResourceLoader;
    private TaxManager taxManager;
    private LimitManager limitManager;

    public MfsAccountController() {
        setCommandName("mfsAccountModel");
        setCommandClass(MfsAccountModel.class);
    }

    @Override
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        super.initBinder(request, binder);
        CommonUtils.bindCustomDateEditor(binder);
    }

    @SuppressWarnings("null")
    @Override
    protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
        String appUserId = ServletRequestUtils.getStringParameter(req, "appUserId");
        boolean isReSubmit = ServletRequestUtils.getBooleanParameter(req, "isReSubmit", false);

        BaseWrapper baseWrapperBank = new BaseWrapperImpl();
        BankModel bankModel = new BankModel();
        bankModel.setBankId(CommissionConstantsInterface.BANK_ID);
        baseWrapperBank.setBasePersistableModel(bankModel);
        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
        boolean veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
        req.setAttribute("veriflyRequired", veriflyRequired);

        /// Added for Resubmit Authorization Request
        if (isReSubmit) {
            Long actionAuthorizationId = ServletRequestUtils.getLongParameter(req, "authId");
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);

            if (actionAuthorizationModel.getCreatedById().longValue() != UserUtils.getCurrentUser().getAppUserId().longValue()) {
                throw new FrameworkCheckedException("illegal operation performed");
            }

            XStream xstream = new XStream();
            MfsAccountModel mfsAccountModel = (MfsAccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());

            mfsAccountModel.setUsecaseId(actionAuthorizationModel.getUsecaseId());

            req.setAttribute("appUserId", mfsAccountModel.getAppUserId());

            //Populating authorization pictures
            String authfilePath = null;
            FileOutputStream fops = null;
            //Converting File bytes from db to input stream
            if (actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.CUSTOMER_PHOTO) != null && actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.CUSTOMER_PHOTO).getPicture() != null) {
                InputStream in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.CUSTOMER_PHOTO).getPicture());
                ImageInputStream iis = ImageIO.createImageInputStream(in);

                Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
                String fileFormat = "";
                while (imageReaders.hasNext()) {
                    ImageReader reader = (ImageReader) imageReaders.next();
                    System.out.printf("formatName: %s%n", reader.getFormatName());
                    fileFormat = reader.getFormatName();
                }

                authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/customerPic_" + actionAuthorizationId + "." + fileFormat;
                fops = new FileOutputStream(authfilePath);
                fops.write(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.CUSTOMER_PHOTO).getPicture());
                fops.flush();
                fops.close();
                mfsAccountModel.setCustomerPicExt(fileFormat);
            }
            if (!mfsAccountModel.getSegmentId().equals(Long.valueOf(MessageUtil.getMessage("Minor_segment_id")))) {
                InputStream in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.TERMS_AND_CONDITIONS_COPY).getPicture());
                ImageInputStream iis = ImageIO.createImageInputStream(in);
                Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
                String fileFormat = "";
                while (imageReaders.hasNext()) {
                    ImageReader reader = (ImageReader) imageReaders.next();
                    System.out.printf("formatName: %s%n", reader.getFormatName());
                    fileFormat = reader.getFormatName();
                }
                authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/tncPic_" + actionAuthorizationId + "." + fileFormat;
                fops = new FileOutputStream(authfilePath);
                fops.write(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.TERMS_AND_CONDITIONS_COPY).getPicture());
                fops.flush();
                fops.close();

                in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.SIGNATURE_SNAPSHOT).getPicture());
                iis = ImageIO.createImageInputStream(in);
                imageReaders = ImageIO.getImageReaders(iis);
                fileFormat = "";
                while (imageReaders.hasNext()) {
                    ImageReader reader = (ImageReader) imageReaders.next();
                    System.out.printf("formatName: %s%n", reader.getFormatName());
                    fileFormat = reader.getFormatName();
                }
                authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/signPic_" + actionAuthorizationId + "." + fileFormat;
                fops = new FileOutputStream(authfilePath);
                fops.write(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.SIGNATURE_SNAPSHOT).getPicture());
                fops.flush();
                fops.close();
            }
            if (!mfsAccountModel.getSegmentId().equals(Long.valueOf(MessageUtil.getMessage("Minor_segment_id")))) {
                InputStream in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT).getPicture());
                ImageInputStream iis = ImageIO.createImageInputStream(in);
                Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
                String fileFormat = "";
                while (imageReaders.hasNext()) {
                    ImageReader reader = (ImageReader) imageReaders.next();
                    System.out.printf("formatName: %s%n", reader.getFormatName());
                    fileFormat = reader.getFormatName();
                }
                authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/proofOfProfessionPic_" + actionAuthorizationId + "." + fileFormat;
                fops = new FileOutputStream(authfilePath);
                fops.write(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT).getPicture());
                fops.flush();
                fops.close();
            }
            if (mfsAccountModel.getSegmentId().equals(Long.valueOf(MessageUtil.getMessage("Minor_segment_id")))) {


                if (actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PARENT_CNIC_SNAPSHOT) != null && actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PARENT_CNIC_SNAPSHOT).getPicture() != null) {
                    InputStream in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PARENT_CNIC_SNAPSHOT).getPicture());
                    ImageInputStream iis = ImageIO.createImageInputStream(in);
                    Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
                    String fileFormat = "";
                    while (imageReaders.hasNext()) {
                        ImageReader reader = (ImageReader) imageReaders.next();
                        System.out.printf("formatName: %s%n", reader.getFormatName());
                        fileFormat = reader.getFormatName();
                    }
                    authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/parentCnicPic_" + actionAuthorizationId + "." + fileFormat;
                    fops = new FileOutputStream(authfilePath);
                    fops.write(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PARENT_CNIC_SNAPSHOT).getPicture());
                    fops.flush();
                    fops.close();
                    mfsAccountModel.setParentCnicPicExt(fileFormat);
                }
                if (actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.B_FORM_SNAPSHOT) != null && actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.B_FORM_SNAPSHOT).getPicture() != null) {
                    InputStream in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.B_FORM_SNAPSHOT).getPicture());
                    ImageInputStream iis = ImageIO.createImageInputStream(in);
                    Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
                    String fileFormat = "";
                    while (imageReaders.hasNext()) {
                        ImageReader reader = (ImageReader) imageReaders.next();
                        System.out.printf("formatName: %s%n", reader.getFormatName());
                        fileFormat = reader.getFormatName();
                    }
                    authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/bFormPic_" + actionAuthorizationId + "." + fileFormat;
                    fops = new FileOutputStream(authfilePath);
                    fops.write(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.B_FORM_SNAPSHOT).getPicture());
                    fops.flush();
                    fops.close();
                    mfsAccountModel.setbFormPicExt(fileFormat);
                }
                if (actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT) != null && actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT).getPicture() != null) {
                    InputStream in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT).getPicture());
                    ImageInputStream iis = ImageIO.createImageInputStream(in);
                    Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
                    String fileFormat = "";
                    while (imageReaders.hasNext()) {
                        ImageReader reader = (ImageReader) imageReaders.next();
                        System.out.printf("formatName: %s%n", reader.getFormatName());
                        fileFormat = reader.getFormatName();
                    }
                    authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/parentCnicBackPic_" + actionAuthorizationId + "." + fileFormat;
                    fops = new FileOutputStream(authfilePath);
                    fops.write(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT).getPicture());
                    fops.flush();
                    fops.close();
                    mfsAccountModel.setParentCnicBackPicExt(fileFormat);
                }
            }

            if (actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_FRONT_SNAPSHOT) != null && actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_FRONT_SNAPSHOT).getPicture() != null) {
                InputStream in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_FRONT_SNAPSHOT).getPicture());
                ImageInputStream iis = ImageIO.createImageInputStream(in);
                Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
                String fileFormat = "";
                while (imageReaders.hasNext()) {
                    ImageReader reader = (ImageReader) imageReaders.next();
                    System.out.printf("formatName: %s%n", reader.getFormatName());
                    fileFormat = reader.getFormatName();
                }
                authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/cnicFrontPic_" + actionAuthorizationId + "." + fileFormat;
                fops = new FileOutputStream(authfilePath);
                fops.write(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_FRONT_SNAPSHOT).getPicture());
                fops.flush();
                fops.close();
                mfsAccountModel.setCnicFrontPicExt(fileFormat);
            }
            if (actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_BACK_SNAPSHOT) != null && actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_BACK_SNAPSHOT).getPicture() != null) {

                InputStream in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_BACK_SNAPSHOT).getPicture());
                ImageInputStream iis = ImageIO.createImageInputStream(in);
                Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
                String fileFormat = "";
                while (imageReaders.hasNext()) {
                    ImageReader reader = (ImageReader) imageReaders.next();
                    System.out.printf("formatName: %s%n", reader.getFormatName());
                    fileFormat = reader.getFormatName();
                }
                authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/cnicBackPic_" + actionAuthorizationId + "." + fileFormat;
                fops = new FileOutputStream(authfilePath);
                fops.write(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_BACK_SNAPSHOT).getPicture());
                fops.flush();
                fops.close();
                mfsAccountModel.setCnicBackPicExt(fileFormat);
            }
            if (!mfsAccountModel.getSegmentId().equals(Long.valueOf(MessageUtil.getMessage("Minor_segment_id")))) {
                if (mfsAccountModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {

                    InputStream in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT).getPicture());
                    ImageInputStream iis = ImageIO.createImageInputStream(in);
                    Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
                    String fileFormat = "";
                    while (imageReaders.hasNext()) {
                        ImageReader reader = (ImageReader) imageReaders.next();
                        System.out.printf("formatName: %s%n", reader.getFormatName());
                        fileFormat = reader.getFormatName();
                    }

                    authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/level1FormPic_" + actionAuthorizationId + "." + fileFormat;
                    fops = new FileOutputStream(authfilePath);
                    fops.write(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT).getPicture());
                    fops.flush();
                    fops.close();
                }
            }

            //End Populating authorization pictures
            req.setAttribute("pageMfsId", mfsAccountModel.getMfsId());
            if (mfsAccountModel.getUsecaseId().equals(PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID)) {
                mfsAccountModel.setbFormPicCheckerComments(actionAuthorizationModel.getbFormPicCheckerComments());
                mfsAccountModel.setCustPicCheckerComments(actionAuthorizationModel.getCustPicCheckerComments());
                mfsAccountModel.setpNicPicCheckerComments(actionAuthorizationModel.getpNicPicCheckerComments());
                mfsAccountModel.setpNicBackPicCheckerComments(actionAuthorizationModel.getpNicBackPicCheckerComments());
                mfsAccountModel.setNicFrontPicCheckerComments(actionAuthorizationModel.getNicFrontPicCheckerComments());
                mfsAccountModel.setNicBackPicCheckerComments(actionAuthorizationModel.getNicBackPicCheckerComments());
            }
            return mfsAccountModel;
        }
        ///End Added for Resubmit Authorization Request


        Long id = null;
        if (null != appUserId && appUserId.trim().length() > 0) {
            id = new Long(appUserId);
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            AppUserModel appUserModel = new AppUserModel();
            appUserModel.setAppUserId(id);
            baseWrapper.setBasePersistableModel(appUserModel);
            baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);

            appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();


            UserDeviceAccountsModel userDeviceModel = mfsAccountManager.getDeviceAccountByAppUserId(appUserModel.getAppUserId(), DeviceTypeConstantsInterface.ALL_PAY);
            Long catalogId = null;
            if (userDeviceModel != null) {
                catalogId = userDeviceModel.getProdCatalogId();
            }


            MfsAccountModel mfsAccountModel = new MfsAccountModel();
            mfsAccountModel.setFiler(appUserModel.getFiler());
            mfsAccountModel.setFirstName(appUserModel.getFirstName());
            mfsAccountModel.setMiddleName(appUserModel.getMiddleName());
            mfsAccountModel.setLastName(appUserModel.getLastName());
            mfsAccountModel.setMobileNo(appUserModel.getMobileNo());
            mfsAccountModel.setAppUserId(appUserModel.getAppUserId());
            mfsAccountModel.setNic(appUserModel.getNic());
            mfsAccountModel.setNicExpiryDate(appUserModel.getNicExpiryDate());
            mfsAccountModel.setProductCatalogId(catalogId);
            mfsAccountModel.setRegistrationStateId(appUserModel.getRegistrationStateId());
            mfsAccountModel.setDob(appUserModel.getDob());
            mfsAccountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
            mfsAccountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
            mfsAccountModel.setMotherMaidenName(appUserModel.getMotherMaidenName());
            List<BlinkCustomerLimitModel> limits = limitManager.getBlinkCustomerLimitByTransactionTypeByCustomerId(appUserModel.getCustomerId());
            for (BlinkCustomerLimitModel limitModel : limits) {
                if (limitModel.getLimitType().equals(LimitTypeConstants.DAILY)) {
                    mfsAccountModel.setDailyCheck(true);
                    if (limitModel.getTransactionType().equals(TransactionTypeConstants.CREDIT)) {
                        mfsAccountModel.setCreditLimitDaily(String.valueOf(limitModel.getMaximum()));

                    } else {
                        mfsAccountModel.setDebitLimitDaily(String.valueOf(limitModel.getMaximum()));
                    }
                } else if (limitModel.getLimitType().equals(LimitTypeConstants.MONTHLY)) {
                    mfsAccountModel.setMonthlyCheck(true);
                    if (limitModel.getTransactionType().equals(TransactionTypeConstants.CREDIT)) {
                        mfsAccountModel.setCreditLimitMonthly(String.valueOf(limitModel.getMaximum()));
                    } else {
                        mfsAccountModel.setDebitLimitMonthly(String.valueOf(limitModel.getMaximum()));
                    }
                } else if (limitModel.getLimitType().equals(LimitTypeConstants.YEARLY)) {
                    mfsAccountModel.setYearlyCheck(true);
                    if (limitModel.getTransactionType().equals(TransactionTypeConstants.CREDIT)) {
                        mfsAccountModel.setCreditLimitYearly(String.valueOf(limitModel.getMaximum()));
                    } else {
                        mfsAccountModel.setDebitLimitYearly(String.valueOf(limitModel.getMaximum()));
                    }
                } else if (limitModel.getLimitType().equals(LimitTypeConstants.MAXIMUM)) {
                    mfsAccountModel.setMaximumBalanceCheck(true);
                    if (limitModel.getTransactionType().equals(TransactionTypeConstants.CREDIT)) {
                        mfsAccountModel.setMaximumCreditLimit(String.valueOf(limitModel.getMaximum()));
                    }
                }
            }


            try {
                OLAVO olaVo = this.mfsAccountManager.getAccountInfoFromOLA(appUserModel.getNic(), bankModel.getBankId());
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
            mfsAccountModel.setSearchFirstName(ServletRequestUtils.getStringParameter(req, "searchFirstName"));
            mfsAccountModel.setSearchLastName(ServletRequestUtils.getStringParameter(req, "searchLastName"));
            mfsAccountModel.setSearchMfsId(ServletRequestUtils.getStringParameter(req, "searchMfsId"));
            mfsAccountModel.setSearchNic(ServletRequestUtils.getStringParameter(req, "searchNic"));

            CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
            mfsAccountModel.setFatherCnic(customerModel.getFatherCnicNo());
            mfsAccountModel.setMotherCnic(customerModel.getMotherCnicNo());

            if (customerModel != null) {
                SmartMoneyAccountModel smartMoneyAccountModel = this.mfsAccountManager.getSmartMoneyAccountByCustomerId(customerModel.getCustomerId());
                if (smartMoneyAccountModel != null) {
                    mfsAccountModel.setIsDebitBlocked(smartMoneyAccountModel.getIsDebitBlocked());
                    mfsAccountModel.setDebitBlockAmount(smartMoneyAccountModel.getDebitBlockAmount());
                }
                if (appUserModel.getRegistrationStateId() != null) {
                    if (appUserModel.getRegistrationStateId() != RegistrationStateConstants.BULK_REQUEST_RECEIVED.longValue()) {

                        /**
                         * ******************************************************************************************************
                         * Updated by Soofia Faruq
                         * Customer's Picture is migrated from CustomerModel to CustomerPictureModel
                         */
                        CustomerPictureModel customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                                PictureTypeConstants.CUSTOMER_PHOTO, customerModel.getCustomerId().longValue());

                        if (customerModel.getSegmentId().equals(CommissionConstantsInterface.MINOR_SEGMENT)) {
                            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
                                    PictureTypeConstants.CUSTOMER_PHOTO, customerModel.getCustomerId().longValue());
                        }

                        if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.CUSTOMER_PHOTO) {
                            if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                                //Converting File bytes from db to input stream
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
                                String filePath = getServletContext().getRealPath("images") + "/upload_dir/customerPic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;

                                FileOutputStream fos = new FileOutputStream(filePath);
                                fos.write(customerPictureModel.getPicture());
                                fos.flush();
                                fos.close();
                                logger.info("Picture Extracted : " + filePath);
                                mfsAccountModel.setCustomerPicDiscrepant(customerPictureModel.getDiscrepant());
                                mfsAccountModel.setCustomerPicExt(fileFormat);
                            }
                        }

                        customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
                                PictureTypeConstants.PARENT_CNIC_SNAPSHOT, customerModel.getCustomerId().longValue());

                        if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PARENT_CNIC_SNAPSHOT) {
                            if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                                //Converting File bytes from db to input stream
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
                                String filePath = getServletContext().getRealPath("images") + "/upload_dir/parentCnicPic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;

                                FileOutputStream fos = new FileOutputStream(filePath);
                                fos.write(customerPictureModel.getPicture());
                                fos.flush();
                                fos.close();
                                logger.info("Picture Extracted : " + filePath);
                                mfsAccountModel.setParentCnicPicDiscrepant(customerPictureModel.getDiscrepant());
                                mfsAccountModel.setParentCnicPicExt(fileFormat);
                            }
                        }

                        customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
                                PictureTypeConstants.B_FORM_SNAPSHOT, customerModel.getCustomerId().longValue());

                        if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.B_FORM_SNAPSHOT) {
                            if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                                //Converting File bytes from db to input stream
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
                                String filePath = getServletContext().getRealPath("images") + "/upload_dir/bFormPic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;

                                FileOutputStream fos = new FileOutputStream(filePath);
                                fos.write(customerPictureModel.getPicture());
                                fos.flush();
                                fos.close();
                                logger.info("Picture Extracted : " + filePath);
                                mfsAccountModel.setbFormPicDiscrepant(customerPictureModel.getDiscrepant());
                                mfsAccountModel.setbFormPicExt(fileFormat);
                            }
                        }

                        customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
                                PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());

                        if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT) {
                            if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                                //Converting File bytes from db to input stream
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
                                String filePath = getServletContext().getRealPath("images") + "/upload_dir/parentCnicBackPic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;

                                FileOutputStream fos = new FileOutputStream(filePath);
                                fos.write(customerPictureModel.getPicture());
                                fos.flush();
                                fos.close();
                                logger.info("Picture Extracted : " + filePath);
                                mfsAccountModel.setParentCnicBackPicDiscrepant(customerPictureModel.getDiscrepant());
                                mfsAccountModel.setParentCnicBackPicExt(fileFormat);
                            }
                        }

                        if (!customerModel.getSegmentId().equals(CommissionConstantsInterface.MINOR_SEGMENT)) {
                            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                                    PictureTypeConstants.TERMS_AND_CONDITIONS_COPY, customerModel.getCustomerId().longValue());

                            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.TERMS_AND_CONDITIONS_COPY) {
                                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                                    //Converting File bytes from db to input stream
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
                                    String filePath = getServletContext().getRealPath("images") + "/upload_dir/tncPic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;

                                    //String filePath = getServletContext().getRealPath("images")+"/upload_dir/tncPic_"+mfsAccountModel.getAppUserId()+".png";
                                    FileOutputStream fos = new FileOutputStream(filePath);
                                    fos.write(customerPictureModel.getPicture());
                                    fos.flush();
                                    fos.close();
                                    logger.info("Picture Extracted : " + filePath);
                                    mfsAccountModel.setTncPicDiscrepant(customerPictureModel.getDiscrepant());
                                    mfsAccountModel.setTncPicExt(fileFormat);
                                }
                            }

                            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                                    PictureTypeConstants.SIGNATURE_SNAPSHOT, customerModel.getCustomerId().longValue());

                            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SIGNATURE_SNAPSHOT) {
                                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                                    //Converting File bytes from db to input stream
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
                                    String filePath = getServletContext().getRealPath("images") + "/upload_dir/signPic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;

                                    //String filePath = getServletContext().getRealPath("images")+"/upload_dir/signPic_"+mfsAccountModel.getAppUserId()+".png";
                                    FileOutputStream fos = new FileOutputStream(filePath);
                                    fos.write(customerPictureModel.getPicture());
                                    fos.flush();
                                    fos.close();
                                    logger.info("Picture Extracted : " + filePath);
                                    mfsAccountModel.setSignPicDiscrepant(customerPictureModel.getDiscrepant());
                                    mfsAccountModel.setSignPicExt(fileFormat);
                                }
                            }

                            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                                    PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, customerModel.getCustomerId().longValue());

                            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT) {
                                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                                    //Converting File bytes from db to input stream
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
                                    String filePath = getServletContext().getRealPath("images") + "/upload_dir/proofOfProfessionPic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;

                                    FileOutputStream fos = new FileOutputStream(filePath);
                                    fos.write(customerPictureModel.getPicture());
                                    fos.flush();
                                    fos.close();
                                    logger.info("Picture Extracted : " + filePath);
                                    mfsAccountModel.setProofOfProfessionPicDiscrepant(customerPictureModel.getDiscrepant());
                                    mfsAccountModel.setProofOfProfessionExt(fileFormat);
                                }
                            }
                        }

                        if (customerModel.getSegmentId().equals(CommissionConstantsInterface.MINOR_SEGMENT)) {
                            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
                                    PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModel.getCustomerId().longValue());
                        } else {
                            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                                    PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModel.getCustomerId().longValue());
                        }
                        if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_FRONT_SNAPSHOT) {
                            if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                                //Converting File bytes from db to input stream
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
                                String filePath = getServletContext().getRealPath("images") + "/upload_dir/cnicFrontPic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;

                                //String filePath = getServletContext().getRealPath("images")+"/upload_dir/cnicFrontPic_"+mfsAccountModel.getAppUserId()+".png";
                                FileOutputStream fos = new FileOutputStream(filePath);
                                fos.write(customerPictureModel.getPicture());
                                fos.flush();
                                fos.close();
                                logger.info("Picture Extracted : " + filePath);
                                mfsAccountModel.setCnicFrontPicDiscrepant(customerPictureModel.getDiscrepant());
                                mfsAccountModel.setCnicFrontPicExt(fileFormat);
                            }
                        }

                        if (customerModel.getSegmentId().equals(CommissionConstantsInterface.MINOR_SEGMENT)) {
                            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
                                    PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());
                        } else {
                            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                                    PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());
                        }
                        if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_BACK_SNAPSHOT) {
                            if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                                //Converting File bytes from db to input stream
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
                                String filePath = getServletContext().getRealPath("images") + "/upload_dir/cnicBackPic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;

                                //String filePath = getServletContext().getRealPath("images")+"/upload_dir/cnicBackPic_"+mfsAccountModel.getAppUserId()+".png";
                                FileOutputStream fos = new FileOutputStream(filePath);
                                fos.write(customerPictureModel.getPicture());
                                fos.flush();
                                fos.close();
                                logger.info("Picture Extracted : " + filePath);
                                mfsAccountModel.setCnicBackPicDiscrepant(customerPictureModel.getDiscrepant());
                                mfsAccountModel.setCnicBackPicExt(fileFormat);
                            }
                        }


                        if (!customerModel.getSegmentId().equals(CommissionConstantsInterface.MINOR_SEGMENT)) {
                            customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                                    PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT, customerModel.getCustomerId().longValue());

                            if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT) {
                                if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                                    //Converting File bytes from db to input stream
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
                                    String filePath = getServletContext().getRealPath("images") + "/upload_dir/sourceOfIncomePic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;

                                    //String filePath = getServletContext().getRealPath("images")+"/upload_dir/cnicFrontPic_"+mfsAccountModel.getAppUserId()+".png";
                                    FileOutputStream fos = new FileOutputStream(filePath);
                                    fos.write(customerPictureModel.getPicture());
                                    fos.flush();
                                    fos.close();
                                    logger.info("Picture Extracted : " + filePath);
                                    mfsAccountModel.setSourceOfIncomePicDiscrepant(customerPictureModel.getDiscrepant());
                                    mfsAccountModel.setSourceOfIncomePicExt(fileFormat);
                                }
                            }

                            if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {
                                customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                                        PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT, customerModel.getCustomerId().longValue());

                                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT) {
                                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
                                        //Converting File bytes from db to input stream
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
                                        String filePath = getServletContext().getRealPath("images") + "/upload_dir/level1FormPic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;

                                        //String filePath = getServletContext().getRealPath("images")+"/upload_dir/level1FormPic_"+mfsAccountModel.getAppUserId()+".png";
                                        FileOutputStream fos = new FileOutputStream(filePath);
                                        fos.write(customerPictureModel.getPicture());
                                        fos.flush();
                                        fos.close();
                                        logger.info("Picture Extracted : " + filePath);
                                        mfsAccountModel.setLevel1FormPicDiscrepant(customerPictureModel.getDiscrepant());
                                        mfsAccountModel.setLevel1FormPicExt(fileFormat);
                                    }
                                }
                            }
                        }
                        /**
                         * End
                         * *******************************************************************************************************
                         */
                    }
                }
                mfsAccountModel.setName(customerModel.getName());
                mfsAccountModel.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
                mfsAccountModel.setMobileNo(customerModel.getMobileNo());
                mfsAccountModel.setSegmentId(customerModel.getSegmentId());
                mfsAccountModel.setCurrency(customerModel.getCurrency());
                mfsAccountModel.setGender(customerModel.getGender());
                mfsAccountModel.setFatherHusbandName(customerModel.getFatherHusbandName());
                mfsAccountModel.setNokName(customerModel.getNokName());
                mfsAccountModel.setNokRelationship(customerModel.getNokRelationship());
                mfsAccountModel.setNokMobile(customerModel.getNokMobile());
                mfsAccountModel.setNokNic(customerModel.getNokNic());
                mfsAccountModel.setBirthPlace(customerModel.getBirthPlace());
                mfsAccountModel.setEmail(customerModel.getEmail());
                mfsAccountModel.setCnicIssuanceDate(appUserModel.getCnicIssuanceDate());
                mfsAccountModel.setRegStateComments(customerModel.getRegStateComments());
                mfsAccountModel.setCreatedOn(customerModel.getCreatedOn());
                mfsAccountModel.setComments(customerModel.getComments());
                mfsAccountModel.setInitialDeposit(customerModel.getInitialDeposit());
                mfsAccountModel.setCnicSeen(customerModel.getIsCnicSeen());
                if(customerModel.getBvs() != null) {
                    mfsAccountModel.setFatherBvs(customerModel.getBvs());
                }
                mfsAccountModel.setVerisysDone(customerModel.getVerisysDone());
                mfsAccountModel.setExpectedMonthlyTurnOver(customerModel.getMonthlyTurnOver());
                mfsAccountModel.setScreeningPerformed(customerModel.isScreeningPerformed());

                mfsAccountModel.setCustPicCheckerComments(customerModel.getCustPicCheckerComments());
                mfsAccountModel.setpNicPicCheckerComments(customerModel.getpNicPicCheckerComments());
                mfsAccountModel.setbFormPicCheckerComments(customerModel.getbFormPicCheckerComments());
                mfsAccountModel.setNicBackPicCheckerComments(customerModel.getNicBackPicCheckerComments());
                mfsAccountModel.setNicFrontPicCheckerComments(customerModel.getNicFrontPicCheckerComments());
                mfsAccountModel.setpNicBackPicCheckerComments(customerModel.getpNicBackPicCheckerComments());
                mfsAccountModel.setCustPicMakerComments(customerModel.getCustPicMakerComments());
                mfsAccountModel.setbFormPicMakerComments(customerModel.getbFormPicMakerComments());
                mfsAccountModel.setpNicPicMakerComments(customerModel.getpNicPicMakerComments());
                mfsAccountModel.setNicBackPicMakerComments(customerModel.getNicBackPicMakerComments());
                mfsAccountModel.setNicFrontPicMakerComments(customerModel.getNicFrontPicMakerComments());
                mfsAccountModel.setpNicBackPicMakerComments(customerModel.getpNicBackPicMakerComments());

                if (customerModel.getTaxRegimeId() != null) {
                    mfsAccountModel.setTaxRegimeId(customerModel.getTaxRegimeId());
                    TaxRegimeModel taxRegimeModel = new TaxRegimeModel();
                    taxRegimeModel = taxManager.searchTaxRegimeById(customerModel.getTaxRegimeId());
                    if (null != taxRegimeModel.getFed()) {
                        mfsAccountModel.setFed(taxRegimeModel.getFed());
                    }
                }


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
                String[] fundSourceId = new String[customerFundSourceList.size()];
                int i = 0;
                for (CustomerFundSourceModel customerFundSource : customerFundSourceList) {
                    fundSourceId[i] = customerFundSource.getFundSourceId().toString();
                    i++;
                }
                mfsAccountModel.setFundsSourceId(fundSourceId);

                // Populating Address Fields
                Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
                if (customerAddresses != null && customerAddresses.size() > 0) {
                    for (CustomerAddressesModel custAdd : customerAddresses) {
                        AddressModel addressModel = custAdd.getAddressIdAddressModel();
                        if (custAdd.getAddressTypeId() == 1) {
                            if (addressModel.getFullAddress() != null && !addressModel.getFullAddress().isEmpty()) {
                                mfsAccountModel.setPresentAddress(addressModel.getFullAddress());
                                mfsAccountModel.setCity(addressModel.getCityId() == null ? null : addressModel.getCityId().toString());
                            }
                        } else if (custAdd.getAddressTypeId() == 4) {
                            if (addressModel.getFullAddress() != null && !addressModel.getFullAddress().isEmpty()) {
                                mfsAccountModel.setNokMailingAdd(addressModel.getFullAddress());
                            }
                        }
                    }
                }
            }

            Long appUserTypeId = this.appUserManager.getAppUserTypeId(UserUtils.getCurrentUser().getAppUserId());
            mfsAccountModel.setAppUserTypeId(appUserTypeId);

            req.setAttribute("pageMfsId", (String) baseWrapper.getObject("userId"));
            mfsAccountModel.setMfsId((String) baseWrapper.getObject("userId"));
            // for the logging process
            mfsAccountModel.setActionId(PortalConstants.ACTION_UPDATE);
            if (mfsAccountModel.getSegmentId().equals(CommissionConstantsInterface.MINOR_SEGMENT)) {
                mfsAccountModel.setUsecaseId(new Long(PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID));
            } else {
                mfsAccountModel.setUsecaseId(new Long(PortalConstants.MFS_ACCOUNT_UPDATE_USECASE_ID));
            }
            return mfsAccountModel;
        } else {
            MfsAccountModel mfsAccountModel = new MfsAccountModel();
            // for the logging process
            mfsAccountModel.setActionId(PortalConstants.ACTION_CREATE);
            mfsAccountModel.setUsecaseId(new Long(PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID));

            Long appUserTypeId = this.appUserManager.getAppUserTypeId(UserUtils.getCurrentUser().getAppUserId());
            mfsAccountModel.setAppUserTypeId(appUserTypeId);

            return mfsAccountModel;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected Map loadReferenceData(HttpServletRequest req) throws Exception {

        Map referenceDataMap = new HashMap();

        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setIsActive(true);
        ReferenceDataWrapper segmentReferenceDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
        segmentReferenceDataWrapper.setBasePersistableModel(segmentModel);
        try {
            referenceDataManager.getReferenceData(segmentReferenceDataWrapper);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        List<SegmentModel> segmentList = null;
        if (segmentReferenceDataWrapper.getReferenceDataList() != null) {
            segmentList = segmentReferenceDataWrapper.getReferenceDataList();
        }
        referenceDataMap.put("segmentList", segmentList);

        FundSourceModel fundSourceModel = new FundSourceModel();
        ReferenceDataWrapper fundSourceDataWrapper = new ReferenceDataWrapperImpl(fundSourceModel, "fundSourceId", SortingOrder.ASC);
        fundSourceDataWrapper.setBasePersistableModel(fundSourceModel);
        try {
            referenceDataManager.getReferenceData(fundSourceDataWrapper);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        List<FundSourceModel> fundSourceList = null;
        if (fundSourceDataWrapper.getReferenceDataList() != null) {
            fundSourceList = fundSourceDataWrapper.getReferenceDataList();
        }
        referenceDataMap.put("fundSourceList", fundSourceList);

        TaxRegimeModel taxRegimeModel = new TaxRegimeModel();
        ReferenceDataWrapper taxRegimeDataWrapper = new ReferenceDataWrapperImpl(taxRegimeModel, "taxRegimeId", SortingOrder.ASC);
        taxRegimeDataWrapper.setBasePersistableModel(taxRegimeModel);
        try {
            referenceDataManager.getReferenceData(taxRegimeDataWrapper);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        List<TaxRegimeModel> taxRegimeList = null;
        if (taxRegimeDataWrapper.getReferenceDataList() != null) {
            taxRegimeList = taxRegimeDataWrapper.getReferenceDataList();
        }
        referenceDataMap.put("taxRegimeList", taxRegimeList);

        //*********************************************************************
        ProductCatalogModel productCatalog = new ProductCatalogModel();
        List<ProductCatalogModel> productCatalogList = null;
        List<ProductCatalogModel> custProductCatalogList = new ArrayList<ProductCatalogModel>();
        productCatalog.setActive(true);
        productCatalog.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        ReferenceDataWrapper customerCatalogDataWrapper = new ReferenceDataWrapperImpl(productCatalog, "name", SortingOrder.ASC);
        productCatalogList = (List<ProductCatalogModel>) referenceDataManager.getReferenceData(customerCatalogDataWrapper).getReferenceDataList();
        //get only customer catalogs.
        if (null != productCatalogList) {
            for (int i = 0; i < productCatalogList.size(); i++) {
                if (productCatalogList.get(i).getAppUserTypeId().equals(UserTypeConstantsInterface.CUSTOMER)) {
                    custProductCatalogList.add(productCatalogList.get(i));
                    //custProductCatalogList.add(i, productCatalogList.get(i));
                }
            }
        }
        referenceDataMap.put("productCatalogList", custProductCatalogList);
        //**********************************************************************


        CityModel cityModel = new CityModel();
        ReferenceDataWrapper cityDataWrapper = new ReferenceDataWrapperImpl(cityModel, "cityId", SortingOrder.ASC);
        cityDataWrapper.setBasePersistableModel(cityModel);
        try {
            referenceDataManager.getReferenceData(cityDataWrapper);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        List<CityModel> cityList = null;
        if (cityDataWrapper.getReferenceDataList() != null) {
            cityList = cityDataWrapper.getReferenceDataList();
        }
        referenceDataMap.put("cityList", cityList);

        Long[] types = {1L, 2L, 53L};
        List<OlaCustomerAccountTypeModel> customerAccountTypeList = null;
        customerAccountTypeList = this.mfsAccountManager.loadCustomerACTypes(types);
        referenceDataMap.put("customerAccountTypeList", customerAccountTypeList);

        Long[] types4Edit = {1L, 2L, 4L, 53L};
        List<OlaCustomerAccountTypeModel> customerAccountTypeList4Edit = null;
        customerAccountTypeList4Edit = this.mfsAccountManager.loadCustomerACTypes(types4Edit);
        referenceDataMap.put("customerAccountTypeListForEdit", customerAccountTypeList4Edit);

        List<LabelValueBean> genderList = new ArrayList<LabelValueBean>();
        LabelValueBean gender = new LabelValueBean("Male", "M");
        genderList.add(gender);
        gender = new LabelValueBean("Female", "F");
        genderList.add(gender);
        gender = new LabelValueBean("Khwaja Sira", "K");
        genderList.add(gender);
        referenceDataMap.put("genderList", genderList);

        List<LabelValueBean> nadraVerList = new ArrayList<LabelValueBean>();
        LabelValueBean nadraVer = new LabelValueBean("Positive", "1");
        nadraVerList.add(nadraVer);
        nadraVer = new LabelValueBean("Negative", "0");
        nadraVerList.add(nadraVer);
        referenceDataMap.put("nadraVerList", nadraVerList);

        List<LabelValueBean> screeningList = new ArrayList<LabelValueBean>();
        LabelValueBean screening = new LabelValueBean("Match", "1");
        screeningList.add(screening);
        screening = new LabelValueBean("Not Match", "0");
        screeningList.add(screening);
        referenceDataMap.put("screeningList", screeningList);

        Long[] regStateList = {RegistrationStateConstantsInterface.RQST_RCVD, RegistrationStateConstantsInterface.BULK_RQST_RCVD,
                RegistrationStateConstantsInterface.DECLINE, RegistrationStateConstants.DISCREPANT, RegistrationStateConstantsInterface.VERIFIED,
                RegistrationStateConstantsInterface.REJECTED};
        CustomList<RegistrationStateModel> regStates = commonCommandManager.getRegistrationStateByIds(regStateList);
        referenceDataMap.put("regStateList", regStates.getResultsetList());

        Long appUserTypeId = this.appUserManager.getAppUserTypeId(UserUtils.getCurrentUser().getAppUserId());
        referenceDataMap.put("appUserTypeId", appUserTypeId);


        List<LabelValueBean> filerList = new ArrayList<LabelValueBean>();
        LabelValueBean filer = new LabelValueBean("Filer", "1");
        filerList.add(filer);
        filer = new LabelValueBean("Non Filer", "0");
        filerList.add(filer);
        referenceDataMap.put("filerList", filerList);

        return referenceDataMap;
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        String mfsAccountId = "";
        Long appUserId = null;
        try {
            Date nowDate = new Date();
            MfsAccountModel mfsAccountModel = (MfsAccountModel) obj;

            /**
             * required validation
             */
            if (!validate(req, mfsAccountModel)) {
                return super.showForm(req, res, errors);
            }


            File file = arbitraryResourceLoader.loadImage("images/no_photo_icon.png");

            if (mfsAccountModel.getSignPic() == null) {
                mfsAccountModel.setSignPicByte(Files.readAllBytes(file.toPath()));
            }
            if (mfsAccountModel.getProofOfProfessionPic() == null) {
                mfsAccountModel.setProofOfProfessionPicByte(Files.readAllBytes(file.toPath()));
            }
            if (mfsAccountModel.getCnicBackPic() == null) {
                mfsAccountModel.setCnicBackPicByte(Files.readAllBytes(file.toPath()));
            }

            // check Agent balance for Initial Deposit[Omar Butt]
            if (null != mfsAccountModel.getInitialDeposit()) {
                AllPayRequestWrapper requestWrapper = initializeRequest(req);
                UserDeviceAccountsModel userDeviceAccountsModel = mfsAccountManager.getDeviceAccountByAppUserId(UserUtils.getCurrentUser().getAppUserId(), DeviceTypeConstantsInterface.ALL_PAY);
                requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALL_PAY.toString());
                requestWrapper.addParameter(CommandFieldConstants.KEY_U_ID, userDeviceAccountsModel.getUserId());
                ActionLogModel actionLogModel = new ActionLogModel();
                this.actionLogBeforeStart(actionLogModel);
                Boolean verifyPIN = verifyPIN(mfsAccountModel.getAgentPIN(), Boolean.FALSE, requestWrapper);
                if (!verifyPIN) {
                    Integer STATUS = allPayWebResponseDataPopulator.isValidBankPinTryCount(requestWrapper);
                    if (STATUS == allPayWebResponseDataPopulator.BLOCKED_BANK_PIN) {
                        if (req.getSession(false) != null) {
                            req.getSession(false).invalidate();
                        }
                        return new ModelAndView(new RedirectView("home.html"));
                    } else {
                        throw new CommandException("Please enter a valid Agent PIN", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    }
                }
                this.actionLogAfterEnd(actionLogModel);
                double initDeposit = Double.parseDouble(mfsAccountModel.getInitialDeposit());
                mfsAccountManager.verifyAgentBalanceForInitialDeposit(initDeposit);
            }

            mfsAccountModel.setRegistrationStateId(RegistrationStateConstantsInterface.RQST_RCVD);
            mfsAccountModel.setAccountPurposeId(AccountPurposeConstants.CURRENT);
            mfsAccountModel.setCurrency("586");
            mfsAccountModel.setCreatedOn(nowDate);

            baseWrapper.putObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY, mfsAccountModel);
            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, mfsAccountModel.getActionId());
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, mfsAccountModel.getUsecaseId());
            baseWrapper = this.mfsAccountManager.createMfsAccount(baseWrapper);
            /**
             * try {
             * customerPendingTrxManager.makeCustomerPendingTrx(mfsAccountModel.getNic());
             * } catch (Exception e) {
             * logger.error(e.getMessage(),e);
             *
             * }
             */

            if (null != mfsAccountModel.getInitialDeposit()) {
                this.mfsAccountManager.makeInitialDeposit(mfsAccountModel);
            }

            mfsAccountId = (String) baseWrapper.getObject(PortalConstants.KEY_MFS_ACCOUNT_ID);
            appUserId = new Long(baseWrapper.getObject(PortalConstants.KEY_APP_USER_ID).toString());
        } catch (FrameworkCheckedException exception) {
            MfsAccountModel mfsAccountModel = new MfsAccountModel();
            Long appUserTypeId = this.appUserManager.getAppUserTypeId(UserUtils.getCurrentUser().getAppUserId());
            mfsAccountModel.setAppUserTypeId(appUserTypeId);
            String msg = exception.getMessage();
            String[] args = {(String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_MFS_ID)};

            if ("MobileNumUniqueException".equals(msg)) {
                this.saveMessage(req, super.getText("newMfsAccount.mobileNumNotUnique2", args, req.getLocale()));
            } else if ("NICUniqueException".equals(msg)) {
                this.saveMessage(req, super.getText("newMfsAccount.nicNotUnique2", args, req.getLocale()));
            } else {
                this.saveMessage(req, super.getText("newMfsAccount.unknown", req.getLocale()));
            }
            return super.showForm(req, res, errors);
        } catch (Exception exception) {
            MfsAccountModel mfsAccountModel = new MfsAccountModel();
            Long appUserTypeId = this.appUserManager.getAppUserTypeId(UserUtils.getCurrentUser().getAppUserId());
            mfsAccountModel.setAppUserTypeId(appUserTypeId);
            this.saveMessage(req, MessageUtil.getMessage("6075"));
            return super.showForm(req, res, errors);
        }

        this.saveMessage(req, super.getText("newMfsAccount.recordSaveSuccessful", new Object[]{mfsAccountId}, req.getLocale()));

        String eappUserId = appUserId.toString();
        ModelAndView modelAndView;
        if (UserUtils.getCurrentUser().getAppUserTypeId() == 3) {
            modelAndView = new ModelAndView(new RedirectView("home.html"));
        } else {
            modelAndView = new ModelAndView(this.getSuccessView() + "&" + PortalConstants.KEY_APP_USER_ID + "=" + eappUserId);
        }
        return modelAndView;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        AppUserModel appUserModel = null;
        try {
            Long appUserId = new Long(ServletRequestUtils.getStringParameter(req, "appUserId"));
            Long customerAccountTypeId = ServletRequestUtils.getLongParameter(req, "customerAccountTypeId");
            appUserModel = new AppUserModel();
            appUserModel.setAppUserId(appUserId);
            baseWrapper.setBasePersistableModel(appUserModel);
            baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
            appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
            if (appUserModel.getNic() == null) {
                req.setAttribute("nicNullInDB", "true");
            }

            SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
            sma.setCustomerId(appUserModel.getCustomerId());
            sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

            SmartMoneyAccountModel smartMoneyAccountModel = this.mfsAccountManager.getSmartMoneyAccountByExample(sma);
            if (smartMoneyAccountModel != null)
                baseWrapper.putObject("smartMoneyAccountModel", smartMoneyAccountModel);

            MfsAccountModel mfsAccountModel = (MfsAccountModel) obj;

            /**
             * required validation
             */
            if (!validate(req, mfsAccountModel)) {
                return super.showForm(req, res, errors);
            }

            mfsAccountModel.setAppUserId(appUserId);
            mfsAccountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
            mfsAccountModel.setCustomerAccountTypeId(customerAccountTypeId);
            mfsAccountModel.setAccountPurposeId(AccountPurposeConstants.CURRENT);
            mfsAccountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
            baseWrapper.putObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY, mfsAccountModel);
            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, mfsAccountModel.getActionId());
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, mfsAccountModel.getUsecaseId());
            baseWrapper = this.mfsAccountManager.updateMfsAccount(baseWrapper);

        } catch (FrameworkCheckedException exception) {
            req.setAttribute("exceptionOccured", "true");
            if (appUserModel.getUsername() != null)
                req.setAttribute("pageMfsId", appUserModel.getUsername());

            MfsAccountModel mfsAccountModel = new MfsAccountModel();
            Long appUserTypeId = this.appUserManager.getAppUserTypeId(UserUtils.getCurrentUser().getAppUserId());
            mfsAccountModel.setAppUserTypeId(appUserTypeId);
            String msg = exception.getMessage();
            String[] args = {(String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_MFS_ID)};

            if ("MobileNumUniqueException".equals(msg)) {
                this.saveMessage(req, super.getText("newMfsAccount.mobileNumNotUnique2", args, req.getLocale()));
            } else if ("NICUniqueException".equals(msg)) {
                this.saveMessage(req, super.getText("newMfsAccount.nicNotUnique2", args, req.getLocale()));
            } else {
                this.saveMessage(req, super.getText("newMfsAccount.unknown", req.getLocale()));
            }
            return super.showForm(req, res, errors);
        } catch (Exception exception) {
            req.setAttribute("exceptionOccured", "true");
            if (appUserModel.getUsername() != null)
                req.setAttribute("pageMfsId", appUserModel.getUsername());

            MfsAccountModel mfsAccountModel = new MfsAccountModel();
            Long appUserTypeId = this.appUserManager.getAppUserTypeId(UserUtils.getCurrentUser().getAppUserId());
            mfsAccountModel.setAppUserTypeId(appUserTypeId);

            this.saveMessage(req, super.getText("newMfsAccount.unknown", req.getLocale()));
            return super.showForm(req, res, errors);
        }
        this.saveMessage(req, super.getText("newMfsAccount.recordUpdateSuccessful", req.getLocale()));
        ModelAndView modelAndView = new ModelAndView(new RedirectView("p_pgsearchuserinfo.html?actionId=3"));

        return modelAndView;
    }

    @Override
    protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        if (UserUtils.getCurrentUser().getAppUserTypeId() == 3) {
            return onCreate(request, response, command, errors);
        }
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        MfsAccountModel mfsAccountModel = (MfsAccountModel) command;

        /**
         * required validation
         */
        if (!validate(request, mfsAccountModel)) {
            return super.showForm(request, response, errors);
        }

        File file = arbitraryResourceLoader.loadImage("images/no_photo_icon.png");

		/*if(mfsAccountModel.getSignPic() == null || mfsAccountModel.getSignPic().getSize()==0){
			mfsAccountModel.setSignPicByte(Files.readAllBytes(file.toPath()));
		}
		if(mfsAccountModel.getCnicBackPic() == null || mfsAccountModel.getCnicBackPic().getSize()==0){
			mfsAccountModel.setCnicBackPicByte(Files.readAllBytes(file.toPath()));
		}*/

        ModelAndView modelAndView = null;
        AppUserModel appUserModel = null;
        ActionAuthPictureModel actionAuthPictureModel = null;
        Long appUserId = null;
        String appUserIdStr = ServletRequestUtils.getStringParameter(request, "appUserId");
        String mfsAccountId = "";
        Long customerAccountTypeId = null;

        boolean resubmitRequest = ServletRequestUtils.getBooleanParameter(request, "resubmitRequest", false);
        Long actionAuthorizationId = null;
        if (resubmitRequest)
            actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");

        if (mfsAccountModel.getUsecaseId().longValue() == PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID) {
            mfsAccountModel.setRegistrationStateId(RegistrationStateConstantsInterface.RQST_RCVD);
            mfsAccountModel.setAccountPurposeId(AccountPurposeConstants.CURRENT);
            mfsAccountModel.setCurrency("586");
            mfsAccountModel.setCreatedOn(new Date());
        } else {

            if (null != appUserIdStr && appUserIdStr.trim().length() > 0) {
                if (!resubmitRequest)
                    appUserId = new Long(ServletRequestUtils.getStringParameter(request, "appUserId"));
                else
                    appUserId = Long.parseLong(appUserIdStr);
            }

            customerAccountTypeId = ServletRequestUtils.getLongParameter(request, "customerAccountTypeId");
            appUserModel = new AppUserModel();
            appUserModel.setAppUserId(appUserId);
            baseWrapper.setBasePersistableModel(appUserModel);
            baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
            appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
            if (appUserModel.getNic() == null) {
                request.setAttribute("nicNullInDB", "true");
            }

            mfsAccountModel.setAppUserId(appUserId);
            mfsAccountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
            mfsAccountModel.setCustomerAccountTypeId(customerAccountTypeId);
            mfsAccountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
        }

        try {
            XStream xstream = new XStream();

            MfsAccountModel mfsAccountModelAuth = (MfsAccountModel) mfsAccountModel.clone();

            mfsAccountModelAuth.setCustomerPic(null);
            mfsAccountModelAuth.setParentCnicPic(null);
            mfsAccountModelAuth.setbFormPic(null);
            mfsAccountModelAuth.setParentCnicBackPic(null);
            mfsAccountModelAuth.setTncPic(null);
            mfsAccountModelAuth.setSignPic(null);
            mfsAccountModelAuth.setProofOfProfessionPic(null);
            mfsAccountModelAuth.setCnicFrontPic(null);
            mfsAccountModelAuth.setCnicBackPic(null);
            mfsAccountModelAuth.setLevel1FormPic(null);
            mfsAccountModelAuth.setSourceOfIncomePic(null);
            mfsAccountModelAuth.setProofOfProfessionPic(null);

            String refDataModelString = xstream.toXML(mfsAccountModelAuth);

            UsecaseModel usecaseModel = usecaseFacade.loadUsecase(mfsAccountModel.getUsecaseId());
            Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(mfsAccountModel.getUsecaseId(), new Long(0));

            //this.mfsAccountManager.validateUniqueness(mfsAccountModel);//Validate Uniqueness

            if (appUserId == null || appUserId < 1) {
                AppUserModel _appUserModel = new AppUserModel();
                _appUserModel.setMobileNo(mfsAccountModel.getMobileNo());
                _appUserModel.setNic(mfsAccountModel.getNic());

                mfsAccountManager.isUniqueCNICMobile(_appUserModel, baseWrapper);
            }

            if (nextAuthorizationLevel.intValue() < 1) {

                baseWrapper.putObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY, mfsAccountModel);
                baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, mfsAccountModel.getActionId());
                baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, mfsAccountModel.getUsecaseId());

                if (mfsAccountModel.getUsecaseId().longValue() == PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID) {
                    baseWrapper = this.mfsAccountManager.createMfsAccount(baseWrapper);
                    /**
                     * commented my atif hussain
                     * customerPendingTrxManager.makeCustomerPendingTrx(mfsAccountModel.getNic());
                     */

                    if (null != mfsAccountModel.getInitialDeposit()) {
                        this.mfsAccountManager.makeInitialDeposit(mfsAccountModel);
                    }
                    mfsAccountId = (String) baseWrapper.getObject(PortalConstants.KEY_MFS_ACCOUNT_ID);
                    appUserId = new Long(baseWrapper.getObject(PortalConstants.KEY_APP_USER_ID).toString());
                } else {
                    baseWrapper = this.mfsAccountManager.updateMfsAccount(baseWrapper);
                }


                actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel, "", refDataModelString, null, usecaseModel, actionAuthorizationId, request);

                if (mfsAccountModel.getUsecaseId().longValue() == PortalConstants.MFS_ACCOUNT_UPDATE_USECASE_ID) {

                    this.saveMessage(request, super.getText("newMfsAccount.recordUpdateSuccessful", request.getLocale())
                            + "Action is authorized successfully. Changes are saved against refernce Action ID : " + actionAuthorizationId);
                    modelAndView = new ModelAndView(new RedirectView("p_pgsearchuserinfo.html?actionId=3"));
                } else {
                    this.saveMessage(request, super.getText("newMfsAccount.recordSaveSuccessful", new Object[]{mfsAccountId}, request.getLocale())
                            + "Action is authorized successfully. Changes are saved against refernce Action ID : " + actionAuthorizationId);
                    String eappUserId = appUserId.toString();
                    modelAndView = new ModelAndView(this.getSuccessView() + "&" + PortalConstants.KEY_APP_USER_ID + "=" + eappUserId);
                }
            } else {
                AppUserModel aum = new AppUserModel();
                if (mfsAccountModel.getUsecaseId().longValue() == PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID) {
                    aum.setMobileNo(mfsAccountModel.getMobileNo());
                    aum.setNic(mfsAccountModel.getNic());
                    this.mfsAccountManager.isUniqueCNICMobile(aum, baseWrapper);
                }

                actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel, "", refDataModelString, null, usecaseModel.getUsecaseId(), mfsAccountModel.getMobileNo(), resubmitRequest, actionAuthorizationId, request);
                this.saveMessage(request, "Action is pending for approval against reference Action ID : " + actionAuthorizationId);
                if (mfsAccountModel.getSegmentId().equals(CommissionConstantsInterface.MINOR_SEGMENT)) {
                    modelAndView = new ModelAndView(new RedirectView("p_minorsearchuserinfo.html?actionId=3"));
                } else {
                    modelAndView = new ModelAndView(new RedirectView("p_pgsearchuserinfo.html?actionId=3"));
                }
            }
            ////Saving Cutomer Pictures
            CustomerModel customerModel = null;
            if (null != appUserModel) {////appUserModel is not null only in update scenario
                customerModel = appUserModel.getCustomerIdCustomerModel();
            }

            if (null == mfsAccountModel.getCustomerPic() || mfsAccountModel.getCustomerPic().getSize() < 1) {
                if (!resubmitRequest) {//Executes only for update scenario
                    actionAuthPictureModel = new ActionAuthPictureModel();
                    CustomerPictureModel customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(PictureTypeConstants.CUSTOMER_PHOTO, customerModel.getCustomerId().longValue());
                    if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
                        actionAuthPictureModel.setPicture(customerPictureModel.getPicture());
                        actionAuthPictureModel.setPictureTypeId(customerPictureModel.getPictureTypeId());
                        actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                        actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setCreatedOn(new Date());
                        actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setUpdatedOn(new Date());
                        this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                    }
                }
            } else {
                if (resubmitRequest) {
                    actionAuthPictureModel = this.actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.CUSTOMER_PHOTO);
                } else {
                    actionAuthPictureModel = new ActionAuthPictureModel();
                    actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setCreatedOn(new Date());
                }
                actionAuthPictureModel.setPicture(mfsAccountModel.getCustomerPic().getBytes());
                actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.CUSTOMER_PHOTO);
                actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                actionAuthPictureModel.setUpdatedOn(new Date());
                this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
            }


            if (null == mfsAccountModel.getTncPic() || mfsAccountModel.getTncPic().getSize() < 1) {

                if (!resubmitRequest) {
                    actionAuthPictureModel = new ActionAuthPictureModel();
                    CustomerPictureModel customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(PictureTypeConstants.TERMS_AND_CONDITIONS_COPY, customerModel.getCustomerId().longValue());
                    if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
                        actionAuthPictureModel.setPicture(customerPictureModel.getPicture());
                        actionAuthPictureModel.setPictureTypeId(customerPictureModel.getPictureTypeId());
                        actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                        actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setCreatedOn(new Date());
                        actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setUpdatedOn(new Date());
                        this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                    }
                }
            } else {
                if (resubmitRequest) {
                    actionAuthPictureModel = this.actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.TERMS_AND_CONDITIONS_COPY);
                } else {
                    actionAuthPictureModel = new ActionAuthPictureModel();
                    actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setCreatedOn(new Date());
                }
                actionAuthPictureModel.setPicture(mfsAccountModel.getTncPic().getBytes());
                actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.TERMS_AND_CONDITIONS_COPY);
                actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                actionAuthPictureModel.setUpdatedOn(new Date());
                this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
            }


            if (null == mfsAccountModel.getSignPic() || mfsAccountModel.getSignPic().getSize() < 1) {

                if (!resubmitRequest) {
                    actionAuthPictureModel = new ActionAuthPictureModel();

                    CustomerPictureModel customerPictureModel = null;

                    if (customerModel == null) {//in case of optional picture
                        customerPictureModel = new CustomerPictureModel();
                        actionAuthPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                        actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.SIGNATURE_SNAPSHOT);
                    } else {
                        customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(PictureTypeConstants.SIGNATURE_SNAPSHOT, customerModel.getCustomerId().longValue());

                        if (customerPictureModel == null)//bulk case
                        {
                            actionAuthPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                            actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.SIGNATURE_SNAPSHOT);
                        } else {
                            actionAuthPictureModel.setPicture(customerPictureModel.getPicture());
                            actionAuthPictureModel.setPictureTypeId(customerPictureModel.getPictureTypeId());
                        }
                    }

                    actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                    actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setCreatedOn(new Date());
                    actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setUpdatedOn(new Date());
                    this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                }
            } else {
                if (resubmitRequest) {
                    actionAuthPictureModel = this.actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.SIGNATURE_SNAPSHOT);
                } else {
                    actionAuthPictureModel = new ActionAuthPictureModel();
                    actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setCreatedOn(new Date());
                }
                actionAuthPictureModel.setPicture(mfsAccountModel.getSignPic().getBytes());
                actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.SIGNATURE_SNAPSHOT);
                actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                actionAuthPictureModel.setUpdatedOn(new Date());
                this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
            }
            if (null == mfsAccountModel.getProofOfProfessionPic() || mfsAccountModel.getProofOfProfessionPic().getSize() < 1) {

                if (!resubmitRequest) {
                    actionAuthPictureModel = new ActionAuthPictureModel();

                    CustomerPictureModel customerPictureModel = null;

                    if (customerModel == null) {//in case of optional picture
                        customerPictureModel = new CustomerPictureModel();
                        actionAuthPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                        actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT);
                    } else {
                        customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, customerModel.getCustomerId().longValue());

                        if (customerPictureModel == null)//bulk case
                        {
                            actionAuthPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                            actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT);
                        } else {
                            actionAuthPictureModel.setPicture(customerPictureModel.getPicture());
                            actionAuthPictureModel.setPictureTypeId(customerPictureModel.getPictureTypeId());
                        }
                    }

                    actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                    actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setCreatedOn(new Date());
                    actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setUpdatedOn(new Date());
                    this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                }
            } else {
                if (resubmitRequest) {
                    actionAuthPictureModel = this.actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT);
                } else {
                    actionAuthPictureModel = new ActionAuthPictureModel();
                    actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setCreatedOn(new Date());
                }
                actionAuthPictureModel.setPicture(mfsAccountModel.getProofOfProfessionPic().getBytes());
                actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT);
                actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                actionAuthPictureModel.setUpdatedOn(new Date());
                this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
            }

            if (null == mfsAccountModel.getCnicFrontPic() || mfsAccountModel.getCnicFrontPic().getSize() < 1) {
                if (!resubmitRequest) {
                    actionAuthPictureModel = new ActionAuthPictureModel();
                    CustomerPictureModel customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModel.getCustomerId().longValue());
                    if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
                        actionAuthPictureModel.setPicture(customerPictureModel.getPicture());
                        actionAuthPictureModel.setPictureTypeId(customerPictureModel.getPictureTypeId());
                        actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                        actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setCreatedOn(new Date());
                        actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setUpdatedOn(new Date());
                        this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                    }
                }
            } else {
                if (resubmitRequest) {
                    actionAuthPictureModel = this.actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_FRONT_SNAPSHOT);
                } else {
                    actionAuthPictureModel = new ActionAuthPictureModel();
                    actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setCreatedOn(new Date());
                }
                actionAuthPictureModel.setPicture(mfsAccountModel.getCnicFrontPic().getBytes());
                actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.ID_FRONT_SNAPSHOT);
                actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                actionAuthPictureModel.setUpdatedOn(new Date());
                this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
            }

            if (mfsAccountModel.getUsecaseId().longValue() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                if (null == mfsAccountModel.getParentCnicPic() || mfsAccountModel.getParentCnicPic().getSize() < 1) {
                    if (!resubmitRequest) {
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        CustomerPictureModel customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(PictureTypeConstants.PARENT_CNIC_SNAPSHOT,
                                customerModel.getCustomerId().longValue());
                        if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
                            actionAuthPictureModel.setPicture(customerPictureModel.getPicture());
                            actionAuthPictureModel.setPictureTypeId(customerPictureModel.getPictureTypeId());
                            actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                            actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                            actionAuthPictureModel.setCreatedOn(new Date());
                            actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                            actionAuthPictureModel.setUpdatedOn(new Date());
                            this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                        }
                    }
                } else {
                    if (resubmitRequest) {
                        actionAuthPictureModel = this.actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId,
                                PictureTypeConstants.PARENT_CNIC_SNAPSHOT);
                    } else {
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setCreatedOn(new Date());
                    }
                    actionAuthPictureModel.setPicture(mfsAccountModel.getParentCnicPic().getBytes());
                    actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.PARENT_CNIC_SNAPSHOT);
                    actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                    actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setUpdatedOn(new Date());
                    this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                }

                if (null == mfsAccountModel.getbFormPic() || mfsAccountModel.getbFormPic().getSize() < 1) {
                    if (!resubmitRequest) {
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        CustomerPictureModel customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(PictureTypeConstants.B_FORM_SNAPSHOT,
                                customerModel.getCustomerId().longValue());
                        if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
                            actionAuthPictureModel.setPicture(customerPictureModel.getPicture());
                            actionAuthPictureModel.setPictureTypeId(customerPictureModel.getPictureTypeId());
                            actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                            actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                            actionAuthPictureModel.setCreatedOn(new Date());
                            actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                            actionAuthPictureModel.setUpdatedOn(new Date());
                            this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                        }
                    }
                } else {
                    if (resubmitRequest) {
                        actionAuthPictureModel = this.actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId,
                                PictureTypeConstants.B_FORM_SNAPSHOT);
                    } else {
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setCreatedOn(new Date());
                    }
                    actionAuthPictureModel.setPicture(mfsAccountModel.getbFormPic().getBytes());
                    actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.B_FORM_SNAPSHOT);
                    actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                    actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setUpdatedOn(new Date());
                    this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                }

                if (null == mfsAccountModel.getParentCnicBackPic() || mfsAccountModel.getParentCnicBackPic().getSize() < 1) {
                    if (!resubmitRequest) {
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        CustomerPictureModel customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT,
                                customerModel.getCustomerId().longValue());
                        if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
                            actionAuthPictureModel.setPicture(customerPictureModel.getPicture());
                            actionAuthPictureModel.setPictureTypeId(customerPictureModel.getPictureTypeId());
                            actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                            actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                            actionAuthPictureModel.setCreatedOn(new Date());
                            actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                            actionAuthPictureModel.setUpdatedOn(new Date());
                            this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                        }
                    }
                } else {
                    if (resubmitRequest) {
                        actionAuthPictureModel = this.actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId,
                                PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT);
                    } else {
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setCreatedOn(new Date());
                    }
                    actionAuthPictureModel.setPicture(mfsAccountModel.getParentCnicBackPic().getBytes());
                    actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT);
                    actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                    actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setUpdatedOn(new Date());
                    this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                }
            }
            if (null == mfsAccountModel.getSourceOfIncomePic() || mfsAccountModel.getSourceOfIncomePic().getSize() < 1) {
                if (!resubmitRequest) {
                    actionAuthPictureModel = new ActionAuthPictureModel();
                    CustomerPictureModel customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT, customerModel.getCustomerId().longValue());
                    if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
                        actionAuthPictureModel.setPicture(customerPictureModel.getPicture());
                        actionAuthPictureModel.setPictureTypeId(customerPictureModel.getPictureTypeId());
                        actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                        actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setCreatedOn(new Date());
                        actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setUpdatedOn(new Date());
                        this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                    }
                }
            } else {
                if (resubmitRequest) {
                    actionAuthPictureModel = this.actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT);
                } else {
                    actionAuthPictureModel = new ActionAuthPictureModel();
                    actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setCreatedOn(new Date());
                }
                actionAuthPictureModel.setPicture(mfsAccountModel.getCnicFrontPic().getBytes());
                actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT);
                actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                actionAuthPictureModel.setUpdatedOn(new Date());
                this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
            }


            if (null == mfsAccountModel.getCnicBackPic() || mfsAccountModel.getCnicBackPic().getSize() < 1) {
                if (!resubmitRequest) {
                    actionAuthPictureModel = new ActionAuthPictureModel();

                    CustomerPictureModel customerPictureModel = null;

                    if (customerModel == null) {//in case of optional picture
                        customerPictureModel = new CustomerPictureModel();
                        actionAuthPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                    } else {
                        customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());

                        if (customerPictureModel == null)//bulk case
                        {
                            actionAuthPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                        } else {
                            actionAuthPictureModel.setPicture(customerPictureModel.getPicture());
                        }
                    }

                    actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.ID_BACK_SNAPSHOT);
                    actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                    actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setCreatedOn(new Date());
                    actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setUpdatedOn(new Date());
                    this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                }
            } else {
                if (resubmitRequest) {
                    actionAuthPictureModel = this.actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_BACK_SNAPSHOT);
                } else {
                    actionAuthPictureModel = new ActionAuthPictureModel();
                    actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setCreatedOn(new Date());
                }
                actionAuthPictureModel.setPicture(mfsAccountModel.getCnicBackPic().getBytes());
                actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.ID_BACK_SNAPSHOT);
                actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                actionAuthPictureModel.setUpdatedOn(new Date());
                this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
            }

            if (mfsAccountModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {

                if (null == mfsAccountModel.getLevel1FormPic() || mfsAccountModel.getLevel1FormPic().getSize() < 1) {
                    if (!resubmitRequest) {
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        CustomerPictureModel customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT, customerModel.getCustomerId().longValue());
                        if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
                            actionAuthPictureModel.setPicture(customerPictureModel.getPicture());
                            actionAuthPictureModel.setPictureTypeId(customerPictureModel.getPictureTypeId());
                            actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                            actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                            actionAuthPictureModel.setCreatedOn(new Date());
                            actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                            actionAuthPictureModel.setUpdatedOn(new Date());
                            this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                        }
                    }
                } else {
                    if (resubmitRequest) {
                        actionAuthPictureModel = this.actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT);
                    } else {
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setCreatedOn(new Date());
                    }
                    actionAuthPictureModel.setPicture(mfsAccountModel.getLevel1FormPic().getBytes());
                    actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT);
                    actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                    actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setUpdatedOn(new Date());
                    this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                }

            }
        } catch (FrameworkCheckedException exception) {
            String msg = exception.getMessage();
            String[] args = {(String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_MFS_ID)};

            if ("MobileNumUniqueException".equals(msg)) {
                this.saveMessage(request, super.getText("newMfsAccount.mobileNumNotUnique2", args, request.getLocale()));
            } else if ("NICUniqueException".equals(msg)) {
                this.saveMessage(request, super.getText("newMfsAccount.nicNotUnique2", args, request.getLocale()));
            } else if (msg.contains("Action authorization request already exist")) {
                this.saveMessage(request, msg);
                modelAndView = new ModelAndView(this.getSuccessView() + "&" + PortalConstants.KEY_APP_USER_ID + "=" + appUserId.toString());
                return modelAndView;
            } else {
                this.saveMessage(request, super.getText("newMfsAccount.unknown", request.getLocale()));
            }
            return super.showForm(request, response, errors);
        } catch (Exception exception) {
            this.saveMessage(request, MessageUtil.getMessage("6075"));
            return super.showForm(request, response, errors);
        }

        return modelAndView;
    }

    private AllPayRequestWrapper initializeRequest(HttpServletRequest request) {

        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

        requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, String.valueOf(DeviceTypeConstantsInterface.ALLPAY_WEB));
        requestWrapper.addParameter(CommandFieldConstants.KEY_PROD_ID, "50013");

        return requestWrapper;
    }

    /**
     * @param bankPin
     * @param fetchTitle
     * @param request
     * @throws FrameworkCheckedException
     */
    private Boolean verifyPIN(String bankPin, Boolean fetchTitle, HttpServletRequest request) throws FrameworkCheckedException {

        Boolean pinVerified = Boolean.TRUE;
        ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());
        try {
            logger.info("[MfsAccountController.verifyPin] Verifying Pin for AppUserID: " + UserUtils.getCurrentUser().getAppUserId());
            allPayWebResponseDataPopulator.verifyPIN(UserUtils.getCurrentUser(), MfsWebUtil.encryptPin(bankPin), null, null, fetchTitle);
        } catch (FrameworkCheckedException e) {
            pinVerified = Boolean.FALSE;
            logger.info("[MfsAccountController.verifyPin] Exception occured in Verifying Pin for AppUserID: " + UserUtils.getCurrentUser().getAppUserId() + ". Exception Msg:" + e.getMessage());
            throw new FrameworkCheckedException("PINExpireException");
        }
        return pinVerified;
    }

    private void actionLogBeforeStart(ActionLogModel actionLogModel) {
        actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
        actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALLPAY_WEB);
        actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));
        actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());

        actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);

        if (actionLogModel.getActionLogId() != null) {
            ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
        }
    }

    private void actionLogAfterEnd(ActionLogModel actionLogModel) {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
            actionLogModel.setEndTime(new Timestamp(new java.util.Date().getTime()));
            baseWrapper.setBasePersistableModel(actionLogModel);
            this.getActionLogManager().createOrUpdateActionLog(baseWrapper);
        } catch (Exception e) {
            logger.error("MfsAccountController.actionLogAfterEnd - Error occured: ", e);
        }
    }

    private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel) {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(actionLogModel);
        try {
            baseWrapper = this.getActionLogManager().createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
            actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
        } catch (Exception ex) {
            logger.error("[MfsAccountController]Exception occurred while insertActionLogRequiresNewTransaction() ", ex);
        }
        return actionLogModel;
    }

    private boolean validate(HttpServletRequest req, MfsAccountModel mfsAccountModel) {
        boolean flag = true;
        String accType = null;
        Long accTypeId = mfsAccountModel.getCustomerAccountTypeId();
        accType = mfsAccountModel.getAccounttypeName();

        if (mfsAccountModel.getCustomerAccountTypeId() == null) {
            this.saveMessage(req, "Account Type: is required.");
            flag = false;
        }
        if (mfsAccountModel.getMobileNo() == null) {
            this.saveMessage(req, "Mobile No: is required.");
            flag = false;
        }
        if (mfsAccountModel.getName() == null) {
            this.saveMessage(req, "Account Title: is required.");
            flag = false;
        }

        if (mfsAccountModel.getAppUserId() == null) {
            if (accTypeId.equals(CustomerAccountTypeConstants.LEVEL_0)) {
                if (mfsAccountModel.getCustomerPic() == null) {
                    this.saveMessage(req, "Customer Picture: is required.");
                    flag = false;
                }
                if (mfsAccountModel.getTncPic() == null) {
                    this.saveMessage(req, "Terms and Condition Picture: is required.");
                    flag = false;
                }

                if (mfsAccountModel.getCnicFrontPic() == null) {
                    this.saveMessage(req, "CNIC Front: is required.");
                    flag = false;
                }
            }
        }


        if (mfsAccountModel.getNic() == null) {
            this.saveMessage(req, "CNIC #: is required.");
            flag = false;
        }
        if (accTypeId.equals(CustomerAccountTypeConstants.LEVEL_0)) {
            if (mfsAccountModel.getDob() == null) {
                this.saveMessage(req, "Date of Birth: is required.");
                flag = false;
            }
        }
        if (mfsAccountModel.getNicExpiryDate() == null) {
            this.saveMessage(req, "CNIC Expiry: is required.");
            flag = false;
        }
//		if(mfsAccountModel.getCnicIssuanceDate()==null){
//			this.saveMessage(req,"Cnic Issuance Date: is required.");
//			flag=false;
//		}
        //Optional in case of L1
        if (accTypeId.equals(CustomerAccountTypeConstants.LEVEL_0)) {
            if (mfsAccountModel.isCnicSeen() == false) {
                this.saveMessage(req, "Original CNIC Seen: is required.");
                flag = false;
            }
        }

        if (mfsAccountModel.getBirthPlace() == null) {
            this.saveMessage(req, "Place of Birth: is required.");
            flag = false;
        }
        //Optional in case of L1
        if (accTypeId.equals(CustomerAccountTypeConstants.LEVEL_0)) {
            if (mfsAccountModel.getGender() == null) {
                this.saveMessage(req, "Gender: is required.");
                flag = false;
            }
//            if (mfsAccountModel.getFatherHusbandName() == null) {
//                this.saveMessage(req, "Father/Husband Name: is required.");
//                flag = false;
//            }
        }

//		if(mfsAccountModel.getMotherMaidenName()==null){
//			this.saveMessage(req, "Mother's Maiden Name: is required.");
//			flag=false;
//		}

        if (mfsAccountModel.getPresentAddress() == null) {
            this.saveMessage(req, "Mailing Address: is required.");
            flag = false;
        }

        //Optional in case of L1
        if (accTypeId.equals(CustomerAccountTypeConstants.LEVEL_0)) {
            if (mfsAccountModel.getCity() == null) {
                this.saveMessage(req, "City: is required.");
                flag = false;
            }
        }


        if (mfsAccountModel.getTaxRegimeId() == null) {
            this.saveMessage(req, "Tax Regime: is required.");
            flag = false;
        }

        if (mfsAccountModel.getFed() == null) {
            this.saveMessage(req, "FED (%age): is required.");
            flag = false;
        }


        //***************************************************************************************************************************
        //									Check if receiver cnic is blacklisted
        //***************************************************************************************************************************

        if (this.commonCommandManager.isCnicBlacklisted(mfsAccountModel.getNic())) {
            this.saveMessage(req, MessageUtil.getMessage("walkinAccountBlacklisted"));
            flag = false;
        }
        //***************************************************************************************************************************
        if (mfsAccountModel.getCustomerAccountTypeId() != null && mfsAccountModel.getCustomerAccountTypeId() == 2L) {

            if (mfsAccountModel.getLevel1FormPic() == null && mfsAccountModel.getAppUserId() == null) { // for create scenario only
                this.saveMessage(req, "Level 1 Form Picture: is required.");
                flag = false;
            }
            //Optional in case of L1
            if (accTypeId.equals(CustomerAccountTypeConstants.LEVEL_0)) {

                if (mfsAccountModel.getFundsSourceId() == null || mfsAccountModel.getFundsSourceId().length == 0) {
                    this.saveMessage(req, "Source of Funds: is required.");
                    flag = false;
                }
                if (mfsAccountModel.getNokName() == null) {
                    this.saveMessage(req, "Next of KIN: Name: is required.");
                    flag = false;
                }
                if (mfsAccountModel.getNokRelationship() == null) {
                    this.saveMessage(req, "Next of KIN: Relationship: is required.");
                    flag = false;
                }

                if (mfsAccountModel.getNokNic() == null) {
                    this.saveMessage(req, "Next of KIN: CNIC #: is required.");
                    flag = false;
                }
                if (mfsAccountModel.getNokMailingAdd() == null) {
                    this.saveMessage(req, "Next of KIN: Address: is required.");
                    flag = false;
                }
                if (mfsAccountModel.getNokMobile() == null) {
                    this.saveMessage(req, "Next of KIN: Mobile #: is required.");
                    flag = false;
                }
            }

        }

        if (mfsAccountModel.getAppUserId() != null) {

            if (mfsAccountModel.getRegistrationStateId() == null) {
                this.saveMessage(req, "Registration State: is required.");
                flag = false;
            }
        }

        if (mfsAccountModel.getRegistrationStateId().equals(RegistrationStateConstants.DISCREPANT) && (mfsAccountModel.getCustomerPicDiscrepant().equals(false) &&
                mfsAccountModel.getParentCnicPicDiscrepant().equals(false) && mfsAccountModel.getbFormPicDiscrepant().equals(false) &&
                mfsAccountModel.getCnicFrontPicDiscrepant().equals(false) && mfsAccountModel.getCnicBackPicDiscrepant().equals(false) &&
                mfsAccountModel.getParentCnicBackPicDiscrepant().equals(false))) {
            this.saveMessage(req, "Atleast one of the checkboxes should be checked.");
            flag = false;
        }

        return flag;
    }

    public ActionLogManager getActionLogManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (ActionLogManager) applicationContext.getBean("actionLogManager");
    }

    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setFinancialIntegrationManager(
            FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public void setCustomerPendingTrxManager(CustomerPendingTrxManager customerPendingTrxManager) {
        this.customerPendingTrxManager = customerPendingTrxManager;
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }

    public AppUserManager getAppUserManager() {
        return appUserManager;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
        this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
    }

    public void setArbitraryResourceLoader(ArbitraryResourceLoader arbitraryResourceLoader) {
        this.arbitraryResourceLoader = arbitraryResourceLoader;
    }

    public void setTaxManager(TaxManager taxManager) {
        this.taxManager = taxManager;
    }

    public void setLimitManager(LimitManager limitManager) {
        this.limitManager = limitManager;
    }
}
