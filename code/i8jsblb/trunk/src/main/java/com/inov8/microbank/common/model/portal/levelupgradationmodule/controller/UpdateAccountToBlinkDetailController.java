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
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthPictureModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.server.dao.customermodule.BlinkCustomerModelDAO;
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

public class UpdateAccountToBlinkDetailController extends AdvanceAuthorizationFormController {
    private ReferenceDataManager referenceDataManager;
    private OlaCustomerAccountTypeDao olaCustomerAccountTypeDao;
    private BlinkCustomerModelDAO blinkCustomerModelDAO;
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

    public UpdateAccountToBlinkDetailController() {
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

//            in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.TERMS_AND_CONDITIONS_COPY).getPicture());
//            iis = ImageIO.createImageInputStream(in);
//            imageReaders = ImageIO.getImageReaders(iis);
//            fileFormat = "";
//            while (imageReaders.hasNext()) {
//                ImageReader reader = (ImageReader) imageReaders.next();
//                System.out.printf("formatName: %s%n", reader.getFormatName());
//                fileFormat = reader.getFormatName();
//            }
//            authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/tncPic_" + actionAuthorizationId + "." + fileFormat;
//            fops = new FileOutputStream(authfilePath);
//            fops.write(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.TERMS_AND_CONDITIONS_COPY).getPicture());
//            fops.flush();
//            fops.close();
//
//            in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.SIGNATURE_SNAPSHOT).getPicture());
//            iis = ImageIO.createImageInputStream(in);
//            imageReaders = ImageIO.getImageReaders(iis);
//            fileFormat = "";
//            while (imageReaders.hasNext()) {
//                ImageReader reader = (ImageReader) imageReaders.next();
//                System.out.printf("formatName: %s%n", reader.getFormatName());
//                fileFormat = reader.getFormatName();
//            }
//            authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/signPic_" + actionAuthorizationId + "." + fileFormat;
//            fops = new FileOutputStream(authfilePath);
//            fops.write(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.SIGNATURE_SNAPSHOT).getPicture());
//            fops.flush();
//            fops.close();

            in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT).getPicture());
            iis = ImageIO.createImageInputStream(in);
            imageReaders = ImageIO.getImageReaders(iis);
            fileFormat = "";
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
            mfsAccountModel.setProofOfProfessionExt(fileFormat);


            in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT).getPicture());
            iis = ImageIO.createImageInputStream(in);
            imageReaders = ImageIO.getImageReaders(iis);
            fileFormat = "";
            while (imageReaders.hasNext()) {
                ImageReader reader = (ImageReader) imageReaders.next();
                System.out.printf("formatName: %s%n", reader.getFormatName());
                fileFormat = reader.getFormatName();
            }
            authfilePath = getServletContext().getRealPath("images") + "/upload_dir/authorization/sourceOfIncomePic_" + actionAuthorizationId + "." + fileFormat;
            fops = new FileOutputStream(authfilePath);
            fops.write(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT).getPicture());
            fops.flush();
            fops.close();
            mfsAccountModel.setSourceOfIncomePicExt(fileFormat);

            in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_FRONT_SNAPSHOT).getPicture());
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

            in = new ByteArrayInputStream(actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.ID_BACK_SNAPSHOT).getPicture());
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
            mfsAccountModel.setCnicBackPicExt(fileFormat);

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
            mfsAccountModel.setSignPicExt(fileFormat);


            //End Populating authorization pictures
            req.setAttribute("pageMfsId", mfsAccountModel.getMfsId());
            return mfsAccountModel;
        }
        if (accTypeId != null) {
            accounttypeid = accTypeId;
        }
        BlinkCustomerModel customerModelList = blinkCustomerModelDAO.findByPrimaryKey(Long.parseLong(accTypeId));
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
            mfsAccountModel.setDob(customerModelList.getDob());
            mfsAccountModel.setMotherMaidenName(customerModelList.getMotherMaidenName());
            mfsAccountModel.setUsCitizen(customerModelList.getUsCitizen());
            mfsAccountModel.setDualNationality(customerModelList.getDualNationality());
            mfsAccountModel.setIncome(customerModelList.getSourceOfIncome());
            mfsAccountModel.setNadraMotherMedianName(appUserModel.getMotherMaidenName());


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

                BlinkCustomerPictureModel customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
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

                customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                        PictureTypeConstants.TERMS_AND_CONDITIONS_COPY, customerModelList.getCustomerId());

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
                        mfsAccountModel.setTncPicExt(fileFormat);
                    }
                }

                customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                        PictureTypeConstants.SIGNATURE_SNAPSHOT, customerModelList.getCustomerId());

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
                        mfsAccountModel.setSignPicExt(fileFormat);
                    }
                }


                customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
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
                customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                        PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, customerModelList.getCustomerId());

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
                        mfsAccountModel.setProofOfProfessionExt(fileFormat);
                    }
                }


                customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
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
                        mfsAccountModel.setSignPicExt(fileFormat);
                    }
                }

                customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
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
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                        mfsAccountModel.setCnicBackPicExt(fileFormat);
                    }
                }
                customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                        PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT, customerModelList.getCustomerId());
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

                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                        mfsAccountModel.setSourceOfIncomePicExt(fileFormat);
                    }
                }

                if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {
                    customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                            PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT, customerModelList.getCustomerId());

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
                            mfsAccountModel.setLevel1FormPicExt(fileFormat);
                        }
                    }
                }


                mfsAccountModel.setName(customerModelList.getConsumerName());
                mfsAccountModel.setCustomerAccountTypeId(customerModelList.getCustomerAccountTypeId());
                mfsAccountModel.setMobileNo(customerModelList.getMobileNo());
                mfsAccountModel.setSegmentId(customerModel.getSegmentId());
                mfsAccountModel.setCurrency(customerModel.getCurrency());
                mfsAccountModel.setGender(customerModelList.getGender());
                mfsAccountModel.setFatherHusbandName(customerModelList.getFatherHusbandName());
                mfsAccountModel.setNokName(customerModelList.getNextOfKin());
                mfsAccountModel.setBirthPlace(customerModelList.getBirthPlace());
                mfsAccountModel.setNadraPlaceOfBirth(customerModel.getBirthPlace());
                mfsAccountModel.setRiskLevel("Medium");
                mfsAccountModel.setEmail(customerModelList.getEmailAddress());
                mfsAccountModel.setCnicIssuanceDate(customerModelList.getCnicIssuanceDate());
                mfsAccountModel.setAccountPurposeName(customerModelList.getPurposeOfAccount());
                mfsAccountModel.setMailingAddress(customerModelList.getMailingAddress());

                mfsAccountModel.setCreatedOn(customerModelList.getCreatedOn());
                mfsAccountModel.setInitialDeposit(customerModel.getInitialDeposit());
                mfsAccountModel.setClsResponseCode(customerModelList.getClsResponseCode());
                mfsAccountModel.setExpectedMonthlyTurnOver(customerModelList.getExpectedMonthlyTurnOver());
                mfsAccountModel.setLongitude(customerModelList.getLongitude());
                mfsAccountModel.setLatitude(customerModelList.getLatitude());
                mfsAccountModel.setComments(customerModelList.getComments());

                mfsAccountModel.setPresentAddress(customerModelList.getPermanentAddress());

                // Populating Address Fields
                Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
                if (customerAddresses != null && customerAddresses.size() > 0) {
                    for (CustomerAddressesModel custAdd : customerAddresses) {
                        AddressModel addressModel = custAdd.getAddressIdAddressModel();
                        if (custAdd.getAddressTypeId() == 1) {
                            if (addressModel.getFullAddress() != null && !addressModel.getFullAddress().isEmpty()) {
                                mfsAccountModel.setPresentAddress(addressModel.getFullAddress());
                                if (addressModel.getCityId() != null) {
                                    CityModel cityModel = new CityModel();
                                    cityModel = cityDAO.findAddressById(addressModel.getCityId());
                                    mfsAccountModel.setCity(cityModel.getName());
                                } else {
                                    mfsAccountModel.setCity(addressModel.getCityId() == null ? null : addressModel.getCityId().toString());
                                }
                            }
                        } else if (custAdd.getAddressTypeId() == 4) {
                            if (addressModel.getFullAddress() != null && !addressModel.getFullAddress().isEmpty()) {
                                mfsAccountModel.setNokMailingAdd(addressModel.getFullAddress());
                            }
                        }
                    }
                }

            }

            req.setAttribute("pageMfsId", (String) baseWrapper.getObject("userId"));
            mfsAccountModel.setActionId(PortalConstants.ACTION_UPDATE);
            mfsAccountModel.setUsecaseId(new Long(PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID));


            return mfsAccountModel;
        } else {
            MfsAccountModel mfsAccountModel = new MfsAccountModel();
            // for the logging process
            mfsAccountModel.setActionId(PortalConstants.ACTION_CREATE);
            mfsAccountModel.setUsecaseId(new Long(PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID));

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
                return new ModelAndView(new RedirectView("p_updateaccounttoblinkinfo.html"));
            }

            mfsAccountModel.setAppUserId(appUserId);
            mfsAccountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
            mfsAccountModel.setCustomerAccountTypeId(Long.valueOf(accTypeId));
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
        ModelAndView modelAndView = new ModelAndView(new RedirectView("home.html"));

        return modelAndView;
    }

    @Override
    protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        String printCheck = ServletRequestUtils.getStringParameter(request, "printCheck");

        if (printCheck.equals("0")) {
            if (UserUtils.getCurrentUser().getAppUserTypeId() == 3) {
                return onCreate(request, response, command, errors);
            }
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            MfsAccountModel mfsAccountModel = (MfsAccountModel) command;
            BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
            String accId = null;
            blinkCustomerModel.setMobileNo(mfsAccountModel.getMobileNo());
            blinkCustomerModel.setAccUpdate(1l);
            blinkCustomerModel.setDob(mfsAccountModel.getDob());
            List<BlinkCustomerModel> blinkData = blinkCustomerModelDAO.findByExample(blinkCustomerModel).getResultsetList();
            for (BlinkCustomerModel data : blinkData) {
                if (accId == null) {
                    accId = data.getBlinkCustomerId().toString();
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

       /* if (mfsAccountModel.getUsecaseId().longValue() == PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {
            mfsAccountModel.setRegistrationStateId(RegistrationStateConstantsInterface.RQST_RCVD);
            mfsAccountModel.setAccountPurposeId(AccountPurposeConstants.CURRENT);
            mfsAccountModel.setCurrency("586");
            mfsAccountModel.setAppUserId(appUserId);
            mfsAccountModel.setCreatedOn(new Date());
        } else {*/

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
            mfsAccountModel.setCustomerAccountTypeId(53l);
            mfsAccountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
//        }

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
                        BlinkCustomerModel blinkCustomerModel1 = commonCommandManager.loadBlinkCustomerByBlinkCustomerId(Long.valueOf(accId));
                        blinkCustomerModel1.setActionAuthorizationId(actionAuthorizationId);
                        blinkCustomerModelDAO.saveOrUpdate(blinkCustomerModel1);
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
                        BlinkCustomerPictureModel customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(PictureTypeConstants.CUSTOMER_PHOTO, customerModel.getCustomerId().longValue());
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

                if (null == mfsAccountModel.getProofOfProfessionPic() || mfsAccountModel.getProofOfProfessionPic().getSize() < 1) {

                    if (!resubmitRequest) {
                        actionAuthPictureModel = new ActionAuthPictureModel();

                        BlinkCustomerPictureModel customerPictureModel = null;

                        if (customerModel == null) {//in case of optional picture
                            customerPictureModel = new BlinkCustomerPictureModel();
                            actionAuthPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                            actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT);
                        } else {
                            customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, customerModel.getCustomerId().longValue());

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


                if (null == mfsAccountModel.getTncPic() || mfsAccountModel.getTncPic().getSize() < 1) {

                    if (!resubmitRequest) {
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        BlinkCustomerPictureModel customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(PictureTypeConstants.TERMS_AND_CONDITIONS_COPY, customerModel.getCustomerId().longValue());
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

                        BlinkCustomerPictureModel customerPictureModel = null;

                        if (customerModel == null) {//in case of optional picture
                            customerPictureModel = new BlinkCustomerPictureModel();
                            actionAuthPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                            actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.SIGNATURE_SNAPSHOT);
                        } else {
                            customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(PictureTypeConstants.SIGNATURE_SNAPSHOT, customerModel.getCustomerId().longValue());

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

                if (null == mfsAccountModel.getCnicFrontPic() || mfsAccountModel.getCnicFrontPic().getSize() < 1) {
                    if (!resubmitRequest) {
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        BlinkCustomerPictureModel customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModel.getCustomerId().longValue());
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

                        BlinkCustomerPictureModel customerPictureModel = null;

                        if (customerModel == null) {//in case of optional picture
                            customerPictureModel = new BlinkCustomerPictureModel();
                            actionAuthPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                        } else {
                            customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());

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

                if (null == mfsAccountModel.getSourceOfIncomePic() || mfsAccountModel.getSourceOfIncomePic().getSize() < 1) {
                    if (!resubmitRequest) {
                        actionAuthPictureModel = new ActionAuthPictureModel();

                        BlinkCustomerPictureModel customerPictureModel = null;

                        if (customerModel == null) {//in case of optional picture
                            customerPictureModel = new BlinkCustomerPictureModel();
                            actionAuthPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                        } else {
                            customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT, customerModel.getCustomerId().longValue());

                            if (customerPictureModel == null)//bulk case
                            {
                                actionAuthPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                            } else {
                                actionAuthPictureModel.setPicture(customerPictureModel.getPicture());
                            }
                        }

                        actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT);
                        actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
                        actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setCreatedOn(new Date());
                        actionAuthPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setUpdatedOn(new Date());
                        this.actionAuthorizationFacade.saveOrUpdate(actionAuthPictureModel);
                    }
                } else {
                    if (resubmitRequest) {
                        actionAuthPictureModel = this.actionAuthorizationFacade.getActionAuthPictureModelByTypeId(actionAuthorizationId, PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT);
                    } else {
                        actionAuthPictureModel = new ActionAuthPictureModel();
                        actionAuthPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        actionAuthPictureModel.setCreatedOn(new Date());
                    }
                    actionAuthPictureModel.setPicture(mfsAccountModel.getSourceOfIncomePic().getBytes());
                    actionAuthPictureModel.setPictureTypeId(PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT);
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
                    modelAndView = new ModelAndView(new RedirectView("p_updateaccounttoblinkinfo.html"));
                    return modelAndView;
                } else {
                    this.saveMessage(request, super.getText("newMfsAccount.unknown", request.getLocale()));
                }
                return super.showForm(request, response, errors);
            } catch (Exception exception) {
                this.saveMessage(request, MessageUtil.getMessage("6075"));
                return super.showForm(request, response, errors);
            }
        } else {
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            MfsAccountModel mfsAccountModel = (MfsAccountModel) command;
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
                Image img1 = Image.getInstance(findPath(mfsAccountModel, PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, "proofOfProfessionPic_"));

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
                pdfCell = new PdfPCell(new Phrase("Asaan Digital Account", fontBlack));
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
                BlinkCustomerModel customerModelList = blinkCustomerModelDAO.findByPrimaryKey(Long.parseLong(accTypeId));
                pdfCell = new PdfPCell(new Phrase("Source Of Income:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableFourColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(customerModelList.getSourceOfIncome(), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(3);
                TableFourColumn.addCell(pdfCell);
                ///
                pdfCell = new PdfPCell(new Phrase("Who Will Fund Your Account(CP):", fontBlack));
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

                pdfCell = new PdfPCell(new Phrase("", fontBlack));
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
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
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
                pdfCell = new PdfPCell(new Phrase(simpleDateFormat.format(mfsAccountModel.getDob()), fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);

                pdfCell = new PdfPCell(new Phrase("ID Issuance Date(CNIC/SNIC):", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(2);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(simpleDateFormat.format(mfsAccountModel.getCnicIssuanceDate()), fontBlack));
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

                pdfCell = new PdfPCell(new Phrase("Are You US Citizen/Address Holder/Business Holder(FACTA):", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(4);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getUsCitizen(), fontBlack));
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
//                pdfCell = new PdfPCell(new Phrase(mfsAccountModel.getBirthPlace(), fontBlack));
                pdfCell = new PdfPCell(new Phrase("Pakistan", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Nationality:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Pakistani", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Dual Nationality", fontBlack));
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


                pdfCell = new PdfPCell(new Phrase(customerModelList.getPermanentAddress(), fontBlack));
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
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
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
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
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
                pdfCell = new PdfPCell(new Phrase("", fontBlack));
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
                pdfCell.setColspan(3);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(1);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("Relationship with Prospective Remitter:", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.NO_BORDER);
                pdfCell.setColspan(3);
                TableEightColumn.addCell(pdfCell);
                pdfCell = new PdfPCell(new Phrase("               ", fontBlack));
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfCell.setBorder(Rectangle.BOTTOM);
                pdfCell.setColspan(1);
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
                list = commonCommandManager.getDebitCardModelDao().getDebitCardModelByMobileAndNIC(mfsAccountModel.getMobileNo(), mfsAccountModel.getNic());
                String CardType = "";
                if (list != null && !list.isEmpty()) {
                    debitCardModel = list.get(0);
                    customerModelList = blinkCustomerModelDAO.findByPrimaryKey(Long.parseLong(accTypeId));
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
                String filePath = findPath(mfsAccountModel, PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, "proofOfProfessionPic_");
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
                pdfCell = new PdfPCell(new Phrase("Yes", fontBlack));
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


        return null;
    }
    public String findPath(MfsAccountModel mfsAccountModel, Long ptc, String namedPic) throws FrameworkCheckedException, IOException {
        BlinkCustomerModel customerModelList = blinkCustomerModelDAO.findByPrimaryKey(Long.parseLong(accTypeId));
        AppUserModel appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(customerModelList.getMobileNo());
        appUserId = appUserModel.getAppUserId();
        BlinkCustomerPictureModel customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(
                ptc, customerModelList.getCustomerId());
        String filePath = "";
        if (customerPictureModel.getPicture() != null) {
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
            filePath = getServletContext().getRealPath("images") + "/upload_dir/" + namedPic + appUserId + "." + fileFormat;
        }

        return filePath;

    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map referenceDataMap = new HashMap();
        Long[] regStateList = {BlinkCustomerRegistrationStateConstantsInterface.DISCREPANT, BlinkCustomerRegistrationStateConstantsInterface.APPROVED,
                BlinkCustomerRegistrationStateConstantsInterface.REJECTED};
        CustomList<BlinkCustomerRegistrationStateModel> regStates = commonCommandManager.getBlinkRegistrationStateByIds(regStateList);

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
        if (mfsAccountModel.getRiskLevel() == null) {
            flag = false;
            msg = msg + "\n Please Select Risk Level";
        }
//        if (mfsAccountModel.getCity() == null) {
//            flag = false;
//            msg = msg + "\n Please Select City";
//        }
        /* *********************************************************/
        if (mfsAccountModel.getPopRej() == true &&
                mfsAccountModel.getCnicFrontRej() == true &&
                mfsAccountModel.getCnicBackRej() == true &&
                mfsAccountModel.getCustomerPicRej() == true &&
                mfsAccountModel.getSignReg() == true &&
                mfsAccountModel.getNameRej() == true
        ) {
            if (mfsAccountModel.getRegistrationStateId() != BlinkCustomerRegistrationStateConstantsInterface.REJECTED) {
                flag = false;
                msg = msg + "\n Please select correct registration state:REJECTED";
            }
        } else if (
                mfsAccountModel.getCnicFrontRej() == true ||
                        mfsAccountModel.getCnicBackRej() == true ||
                        mfsAccountModel.getCustomerPicRej() == true ||
                        mfsAccountModel.getNameRej() == true ||
                        mfsAccountModel.getSignReg() == true) {
            if (mfsAccountModel.getRegistrationStateId() != BlinkCustomerRegistrationStateConstantsInterface.DISCREPANT) {
                flag = false;
                msg = msg + "\n Please select correct registration state:DISCREPENT";
            }
        }

        /* *********************************************************/
        if (mfsAccountModel.getName() == null) {
            flag = false;
            msg = msg + "\n Account Title: is Required";
        }

//		if(mfsAccountModel.getCnicIssuanceDate()==null){
//			this.saveMessage(req,"Cnic Issuance Date: is required.");
//			flag=false;
//		}
        //Optional in case of L1

        if (mfsAccountModel.getBirthPlace() == null) {
            flag = false;
            msg = msg + "\n Place of Birth: is Required";
        }
        //Optional in case of L1
//		if(mfsAccountModel.getMotherMaidenName()==null){
//			this.saveMessage(req, "Mother's Maiden Name: is required.");
//			flag=false;
//		}

        if (mfsAccountModel.getPresentAddress() == null) {
            flag = false;
            msg = msg + "\n Mailing address: is Required";
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

    public void setBlinkCustomerModelDAO(BlinkCustomerModelDAO blinkCustomerModelDAO) {
        this.blinkCustomerModelDAO = blinkCustomerModelDAO;
    }

    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
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
