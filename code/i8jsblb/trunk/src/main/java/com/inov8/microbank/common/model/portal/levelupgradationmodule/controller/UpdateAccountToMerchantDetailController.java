package com.inov8.microbank.common.model.portal.levelupgradationmodule.controller;
/* 
Created by IntelliJ IDEA.
  @Copyright: 1/13/2022 On: 11:41 AM
  @author(Muhammad Aqeel)
  @project(trunk)
*/

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.customermodule.BlinkCustomerPictureModel;
import com.inov8.microbank.common.model.customermodule.MerchantAccountPictureModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthPictureModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.server.dao.customermodule.BlinkCustomerModelDAO;
import com.inov8.microbank.server.dao.customermodule.MerchantAccountModelDAO;
import com.inov8.microbank.server.dao.portal.citymodule.CityDAO;
import com.inov8.microbank.server.dao.portal.ola.OlaCustomerAccountTypeDao;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.bulkdisbursements.CustomerPendingTrxManager;
import com.inov8.microbank.server.service.fileloader.ArbitraryResourceLoader;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.tax.service.TaxManager;
import com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.thoughtworks.xstream.XStream;
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
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class UpdateAccountToMerchantDetailController extends AdvanceAuthorizationFormController {
    private ReferenceDataManager referenceDataManager;
    private OlaCustomerAccountTypeDao olaCustomerAccountTypeDao;
    private MerchantAccountModelDAO merchantAccountModelDAO;
    private MfsAccountManager mfsAccountManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private CustomerPendingTrxManager customerPendingTrxManager;
    private CommonCommandManager commonCommandManager;
    private AppUserManager appUserManager;
    private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator = null;
    private ArbitraryResourceLoader arbitraryResourceLoader;
    private TaxManager taxManager;
    private String accTypeId;
    private String accounttypeid;
    private String msg;
    private Long appUserId;
    private CityDAO cityDAO;

    public UpdateAccountToMerchantDetailController() {
        setCommandName("mfsAccountModel");
        setCommandClass(MfsAccountModel.class);
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }

    @Override
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        super.initBinder(request, binder);
        CommonUtils.bindCustomDateEditor(binder);
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
        accTypeId = ServletRequestUtils.getStringParameter(req, "accTypeId");
        boolean isReSubmit = ServletRequestUtils.getBooleanParameter(req, "isReSubmit", false);

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
            in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.CUSTOMER_PHOTO).getPicture());
            iis = ImageIO.createImageInputStream(in);
            imageReaders = ImageIO.getImageReaders(iis);
            fileFormat = "";
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

            in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_FRONT_SNAPSHOT).getPicture());
            iis = ImageIO.createImageInputStream(in);
            imageReaders = ImageIO.getImageReaders(iis);
            fileFormat = "";
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
            mfsAccountModel.setCnicFrontPicExt(fileFormat);

            in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_BACK_SNAPSHOT).getPicture());
            iis = ImageIO.createImageInputStream(in);
            imageReaders = ImageIO.getImageReaders(iis);
            fileFormat = "";
            while (imageReaders.hasNext()) {
                ImageReader reader = (ImageReader) imageReaders.next();
                System.out.printf("formatName: %s%n", reader.getFormatName());
                fileFormat = reader.getFormatName();
            }

            //End Populating authorization pictures
            req.setAttribute("pageMfsId", mfsAccountModel.getMfsId());
            return mfsAccountModel;
        }
        if (accTypeId != null) {
            accounttypeid = accTypeId;
        }
        MerchantAccountModel customerModelList = merchantAccountModelDAO.findByPrimaryKey(Long.parseLong(accTypeId));
        AppUserModel appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(customerModelList.getMobileNo());
        appUserId = appUserModel.getAppUserId();

        BaseWrapper baseWrapperBank = new BaseWrapperImpl();
        BankModel bankModel = new BankModel();
        bankModel.setBankId(CommissionConstantsInterface.BANK_ID);
        baseWrapperBank.setBasePersistableModel(bankModel);
        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
        boolean veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
        req.setAttribute("veriflyRequired", veriflyRequired);
        req.setAttribute("appUserId", appUserId);

        ///End Added for Resubmit Authorization Request


        Long id = null;
        if (null != appUserId) {
            id = new Long(appUserId);
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            appUserModel.setAppUserId(id);
            baseWrapper.setBasePersistableModel(appUserModel);
            baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);

            appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();

            MfsAccountModel mfsAccountModel = new MfsAccountModel();
            mfsAccountModel.setMobileNo(customerModelList.getMobileNo());
            mfsAccountModel.setAppUserId(appUserModel.getAppUserId());
            mfsAccountModel.setNic(customerModelList.getCnic());
            mfsAccountModel.setAddress1(customerModelList.getBusinessAddress());
            mfsAccountModel.setAccountPurposeName(customerModelList.getTypeOfBusiness());
            mfsAccountModel.setExpectedMonthlyTurnOver(customerModelList.getExpectedMonthlySalary());
            mfsAccountModel.setBusinessName(customerModelList.getBusinessName());


            mfsAccountModel.setSearchFirstName(ServletRequestUtils.getStringParameter(req, "searchFirstName"));
            mfsAccountModel.setSearchLastName(ServletRequestUtils.getStringParameter(req, "searchLastName"));
            mfsAccountModel.setSearchMfsId(ServletRequestUtils.getStringParameter(req, "searchMfsId"));
            mfsAccountModel.setSearchNic(ServletRequestUtils.getStringParameter(req, "searchNic"));

            CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();

            if (customerModel != null) {
                SmartMoneyAccountModel smartMoneyAccountModel = this.mfsAccountManager.getSmartMoneyAccountByCustomerId(customerModel.getCustomerId());
                if (smartMoneyAccountModel != null) {
                    mfsAccountModel.setIsDebitBlocked(smartMoneyAccountModel.getIsDebitBlocked());
                    mfsAccountModel.setDebitBlockAmount(smartMoneyAccountModel.getDebitBlockAmount());
                }

//                if (appUserModel.getRegistrationStateId() != RegistrationStateConstants.BULK_REQUEST_RECEIVED.longValue()) {

                MerchantAccountPictureModel customerPictureModel = this.mfsAccountManager.getMerchantCustomerPictureByTypeId(
                        PictureTypeConstants.CUSTOMER_PHOTO, customerModelList.getCustomerId());

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
                        mfsAccountModel.setCustomerPicExt(fileFormat);
                    }
                }


                customerPictureModel = this.mfsAccountManager.getMerchantCustomerPictureByTypeId(
                        PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModelList.getCustomerId());

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
                        mfsAccountModel.setCnicFrontPicExt(fileFormat);
                    }
                }
                customerPictureModel = this.mfsAccountManager.getMerchantCustomerPictureByTypeId(
                        PictureTypeConstants.ID_BACK_SNAPSHOT, customerModelList.getCustomerId());
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

                        //String filePath = getServletContext().getRealPath("images")+"/upload_dir/cnicFrontPic_"+mfsAccountModel.getAppUserId()+".png";
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                        mfsAccountModel.setCnicBackPicExt(fileFormat);
                    }
                }


                mfsAccountModel.setName(customerModelList.getConsumerName());
                mfsAccountModel.setMobileNo(customerModelList.getMobileNo());
                mfsAccountModel.setCurrency(customerModel.getCurrency());
                mfsAccountModel.setCreatedOn(customerModelList.getCreatedOn());
                mfsAccountModel.setLongitude(customerModelList.getLongitude());
                mfsAccountModel.setLatitude(customerModelList.getLatitude());
                mfsAccountModel.setComments(customerModelList.getComments());
                mfsAccountModel.setCity(customerModelList.getCity());
                // Populating Address Fields
                Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();

            }

            req.setAttribute("pageMfsId", (String) baseWrapper.getObject("userId"));
            mfsAccountModel.setActionId(PortalConstants.ACTION_UPDATE);
            mfsAccountModel.setUsecaseId(new Long(PortalConstants.MFS_MERCHANT_ACCOUNT_UPDATE_USECASE_ID));


            return mfsAccountModel;
        } else {
            MfsAccountModel mfsAccountModel = new MfsAccountModel();
            // for the logging process
            mfsAccountModel.setActionId(PortalConstants.ACTION_CREATE);
            mfsAccountModel.setUsecaseId(new Long(PortalConstants.MFS_MERCHANT_ACCOUNT_UPDATE_USECASE_ID));

            return mfsAccountModel;
        }
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object obj, BindException errors) throws Exception {
        String aq = "AQEEL";
        return null;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        AppUserModel appUserModel = null;
        try {
            Long appUserId = Long.valueOf(ServletRequestUtils.getStringParameter(req, "appUserId"));
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


            if (!validate(req, mfsAccountModel)) {
//                return super.showForm(req, res, errors);
                this.saveMessage(req, msg);
                return new ModelAndView(new RedirectView("p_updateaccounttoMerchantinfo.html"));
            }

            mfsAccountModel.setAppUserId(appUserId);
            mfsAccountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
            mfsAccountModel.setCustomerAccountTypeId(Long.valueOf(accTypeId));
            mfsAccountModel.setAccountPurposeId(AccountPurposeConstants.CURRENT);
            mfsAccountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
            baseWrapper.putObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY, mfsAccountModel);
            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, mfsAccountModel.getActionId());
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, mfsAccountModel.getUsecaseId());
            baseWrapper = this.mfsAccountManager.updateMfsMerchantAccount(baseWrapper);

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
        ModelAndView modelAndView = new ModelAndView(new RedirectView("home.html"));

        return modelAndView;
    }

    @Override
    protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        String printCheck = ServletRequestUtils.getStringParameter(request, "printCheck");

            if (UserUtils.getCurrentUser().getAppUserTypeId() == 3) {
                return onCreate(request, response, command, errors);
            }
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            MfsAccountModel mfsAccountModel = (MfsAccountModel) command;
            MerchantAccountModel blinkCustomerModel = new MerchantAccountModel();
            String accId = null;
            blinkCustomerModel.setMobileNo(mfsAccountModel.getMobileNo());
            blinkCustomerModel.setAccUpdate(1l);
            List<MerchantAccountModel> blinkData = merchantAccountModelDAO.findByExample(blinkCustomerModel).getResultsetList();
            for (MerchantAccountModel data : blinkData) {
                if (accId == null) {
                    accId = data.getMerchanAccountId().toString();
                }
            }

            /**
             * required validation
             */
            if (!validate(request, mfsAccountModel)) {
                this.saveMessage(request, msg);
                return super.showForm(request, response, errors);
            }

            File file = arbitraryResourceLoader.loadImage("images/no_photo_icon.png");

            ModelAndView modelAndView = null;
            AppUserModel appUserModel = null;
            ActionAuthPictureModel actionAuthPictureModel = null;
            String mfsAccountId = "";
            Long customerAccountTypeId = null;
            Long appUserId = null;
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(blinkData.get(0).getMobileNo());
            appUserId = appUserModel.getAppUserId();
            boolean resubmitRequest = ServletRequestUtils.getBooleanParameter(request, "resubmitRequest", false);
            Long actionAuthorizationId = null;
            if (resubmitRequest)
                actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
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
            mfsAccountModel.setCustomerAccountTypeId(58l);
            mfsAccountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());

            try {
                XStream xstream = new XStream();

                MfsAccountModel mfsAccountModelAuth = (MfsAccountModel) mfsAccountModel.clone();
                mfsAccountModelAuth.setCustomerPic(null);
                mfsAccountModelAuth.setTncPic(null);
                mfsAccountModelAuth.setSignPic(null);
                mfsAccountModelAuth.setProofOfProfessionPic(null);
                mfsAccountModelAuth.setCnicFrontPic(null);
                mfsAccountModelAuth.setCnicBackPic(null);
                mfsAccountModelAuth.setLevel1FormPic(null);
                mfsAccountModelAuth.setSourceOfIncomePic(null);

                String refDataModelString = xstream.toXML(mfsAccountModelAuth);

                UsecaseModel usecaseModel = usecaseFacade.loadUsecase(mfsAccountModel.getUsecaseId());
                Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(mfsAccountModel.getUsecaseId(), new Long(0));
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
                        if (null != mfsAccountModel.getInitialDeposit()) {
                            this.mfsAccountManager.makeInitialDeposit(mfsAccountModel);
                        }
                        mfsAccountId = (String) baseWrapper.getObject(PortalConstants.KEY_MFS_ACCOUNT_ID);
                        appUserId = new Long(baseWrapper.getObject(PortalConstants.KEY_APP_USER_ID).toString());
                    } else {
                        baseWrapper = this.mfsAccountManager.updateMfsMerchantAccount(baseWrapper);
                    }


                    actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel, "", refDataModelString, null, usecaseModel, actionAuthorizationId, request);

                    if (mfsAccountModel.getUsecaseId().longValue() == PortalConstants.MFS_ACCOUNT_UPDATE_USECASE_ID) {

                        this.saveMessage(request, super.getText("newMfsAccount.recordUpdateSuccessful", request.getLocale())
                                + "Action is authorized successfully. Changes are saved against refernce Action ID : " + actionAuthorizationId);
                        modelAndView = new ModelAndView(new RedirectView("home.html"));
                    } else {
                        this.saveMessage(request, super.getText("newMfsAccount.recordSaveSuccessful", new Object[]{mfsAccountId}, request.getLocale())
                                + "Action is authorized successfully. Changes are saved against refernce Action ID : " + actionAuthorizationId);
                        String eappUserId = appUserId.toString();
                        modelAndView = new ModelAndView(new RedirectView("home.html"));
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
                    if (accId != null) {
                        MerchantAccountModel blinkCustomerModel1 = commonCommandManager.loadMerchantCustomerByBlinkCustomerId(Long.valueOf(accId));
                        blinkCustomerModel1.setActionAuthorizationId(actionAuthorizationId);
                        merchantAccountModelDAO.saveOrUpdate(blinkCustomerModel1);
                    }
                    modelAndView = new ModelAndView(new RedirectView("home.html"));
                }

                ////Saving Cutomer Pictures
                CustomerModel customerModel = null;
                if (null != appUserModel) {////appUserModel is not null only in update scenario
                    customerModel = appUserModel.getCustomerIdCustomerModel();
                }

                if (null == mfsAccountModel.getCustomerPic() || mfsAccountModel.getCustomerPic().getSize() < 1) {
                    if (!resubmitRequest) {//Executes only for update scenario
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        MerchantAccountPictureModel customerPictureModel = this.mfsAccountManager.getMerchantCustomerPictureByTypeId(PictureTypeConstants.CUSTOMER_PHOTO, customerModel.getCustomerId().longValue());
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

                if (null == mfsAccountModel.getCnicFrontPic() || mfsAccountModel.getCnicFrontPic().getSize() < 1) {
                    if (!resubmitRequest) {
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        MerchantAccountPictureModel customerPictureModel = this.mfsAccountManager.getMerchantCustomerPictureByTypeId(PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModel.getCustomerId().longValue());
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


                if (null == mfsAccountModel.getCnicBackPic() || mfsAccountModel.getCnicBackPic().getSize() < 1) {
                    if (!resubmitRequest) {
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        MerchantAccountPictureModel customerPictureModel = this.mfsAccountManager.getMerchantCustomerPictureByTypeId(PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());
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
                        actionAuthPictureModel = this.actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_BACK_SNAPSHOT);
                    } else {
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setCreatedOn(new Date());
                    }
                    actionAuthPictureModel.setPicture(mfsAccountModel.getCnicFrontPic().getBytes());
                    actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.ID_BACK_SNAPSHOT);
                    actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                    actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    actionAuthPictureModel.setUpdatedOn(new Date());
                    this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
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
                    modelAndView = new ModelAndView(new RedirectView("p_updateaccounttoMerchantinfo.html"));
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

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map referenceDataMap = new HashMap();
        Long[] regStateList = {RegistrationStateConstantsInterface.DECLINE, RegistrationStateConstants.DISCREPANT, RegistrationStateConstantsInterface.VERIFIED,
                RegistrationStateConstantsInterface.REJECTED};
        CustomList<RegistrationStateModel> regStates = commonCommandManager.getRegistrationStateByIds(regStateList);
        referenceDataMap.put("regStateList", regStates.getResultsetList());

        referenceDataMap.put("regStateList", regStates.getResultsetList());

        referenceDataMap.put("appUserId", appUserId);

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

        return referenceDataMap;
    }

    private boolean validate(HttpServletRequest req, MfsAccountModel mfsAccountModel) {
        boolean flag = true;
        String accType = null;
        msg = "";
        Long accTypeId = mfsAccountModel.getCustomerAccountTypeId();
        accType = mfsAccountModel.getAccounttypeName();

        if (mfsAccountModel.getMobileNo() == null) {
            flag = false;
            msg = msg + "\n Mobile No: is Required";
        }

//        if (mfsAccountModel.getCity() == null) {
//            flag = false;
//            msg = msg + "\n Please Select City";
//        }
        /* *********************************************************/
//        if (mfsAccountModel.getPopRej() == true &&
//                mfsAccountModel.getCnicFrontRej() == true &&
//                mfsAccountModel.getCnicBackRej() == true &&
//                mfsAccountModel.getCustomerPicRej() == true &&
//                mfsAccountModel.getSignReg() == true &&
//                mfsAccountModel.getNameRej() == true
//        ) {
//            if (mfsAccountModel.getRegistrationStateId() != BlinkCustomerRegistrationStateConstantsInterface.REJECTED) {
//                flag = false;
//                msg = msg + "\n Please select correct registration state:REJECTED";
//            }
//        } else if (
//                mfsAccountModel.getCnicFrontRej() == true ||
//                        mfsAccountModel.getCnicBackRej() == true ||
//                        mfsAccountModel.getCustomerPicRej() == true ||
//                        mfsAccountModel.getNameRej() == true ||
//                        mfsAccountModel.getSignReg() == true) {
//            if (mfsAccountModel.getRegistrationStateId() != BlinkCustomerRegistrationStateConstantsInterface.DISCREPANT) {
//                flag = false;
//                msg = msg + "\n Please select correct registration state:DISCREPENT";
//            }
//        }

        /* *********************************************************/
        if (mfsAccountModel.getName() == null) {
            flag = false;
            msg = msg + "\n Account Title: is Required";
        }



        //Optional in case of L1


        //***************************************************************************************************************************
        //									Check if receiver cnic is blacklisted
        //***************************************************************************************************************************

        if (this.commonCommandManager.isCnicBlacklisted(mfsAccountModel.getNic())) {
            this.saveMessage(req, MessageUtil.getMessage("walkinAccountBlacklisted"));
            flag = false;
        }
        //***************************************************************************************************************************

        return flag;
    }

    public ActionLogManager getActionLogManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (ActionLogManager) applicationContext.getBean("actionLogManager");
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setOlaCustomerAccountTypeDao(OlaCustomerAccountTypeDao olaCustomerAccountTypeDao) {
        this.olaCustomerAccountTypeDao = olaCustomerAccountTypeDao;
    }


    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    public void setMerchantAccountModelDAO(MerchantAccountModelDAO merchantAccountModelDAO) {
        this.merchantAccountModelDAO = merchantAccountModelDAO;
    }

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public void setCustomerPendingTrxManager(CustomerPendingTrxManager customerPendingTrxManager) {
        this.customerPendingTrxManager = customerPendingTrxManager;
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

    public void setCityDAO(CityDAO cityDAO) {
        this.cityDAO = cityDAO;
    }


}
