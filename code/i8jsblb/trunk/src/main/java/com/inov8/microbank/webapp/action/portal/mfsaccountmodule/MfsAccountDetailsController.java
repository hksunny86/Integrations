package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.microbank.account.service.AccountControlManager;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.UserInfoListViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.customermodule.CustomerManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.server.service.ledger.LedgerManager;
import com.inov8.ola.server.service.limit.LimitManager;
import com.inov8.ola.util.Base64;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.LimitTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class MfsAccountDetailsController extends AdvanceFormController {

    private MfsAccountManager mfsAccountManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private AppUserManager appUserManager;
    //added by turab
    private AccountManager accountManager;
    private LedgerManager ledgerManager;
    private LimitManager limitManager;
    private ReferenceDataManager referenceDataManager;
    private AccountControlManager accountControlManager;
    private CustomerManager customerManager;

    public MfsAccountDetailsController() {
        setCommandName("mfsAccountModel");
        setCommandClass(MfsAccountModel.class);
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
        String appUserId = ServletRequestUtils.getStringParameter(req, "appUserId");
        Double remainingDailyCreditLimit = 0.0;
        Double remainingDailyDebitLimit = 0.0;
        Double remainingMonthlyCreditLimit = 0.0;
        Double remainingMonthlyDebitLimit = 0.0;
        Double remainingYearlyCreditLimit = 0.0;
        Double remainingYearlyDebitLimit = 0.0;

        BaseWrapper baseWrapperBank = new BaseWrapperImpl();
        BankModel bankModel = new BankModel();
        bankModel.setBankId(CommissionConstantsInterface.BANK_ID);
        baseWrapperBank.setBasePersistableModel(bankModel);

        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
        boolean veriflyRequired = true;
        try {
            veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        req.setAttribute("veriflyRequired", veriflyRequired);
        Long id = null;
        if (null != appUserId && appUserId.trim().length() > 0) {
//            id = new Long(EncryptionUtil.decryptForAppUserId(appUserId));
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            AppUserModel appUserModel = new AppUserModel();
            appUserModel.setAppUserId(Long.valueOf(appUserId));
            baseWrapper.setBasePersistableModel(appUserModel);
            baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);

            appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
            MfsAccountModel mfsAccountModel = new MfsAccountModel();

            try {
                OLAVO olaVo = this.mfsAccountManager.getAccountInfoFromOLA(appUserModel.getNic(), bankModel.getBankId());
                if (olaVo != null) {
                    mfsAccountModel.setAccountNo(olaVo.getPayingAccNo());
//			      mfsAccountModel.setAccountStatus(olaVo.getStatusName());
                    mfsAccountModel.setAccountBalance(olaVo.getBalance());
                }
            } catch (Exception ex) {
                log.warn("Exception while getting customer info from OLA: " + ex.getStackTrace());
            }

            mfsAccountModel.setCity(appUserModel.getCity());
            mfsAccountModel.setZongNo(appUserModel.getMobileNo());
            mfsAccountModel.setAppUserId(appUserModel.getAppUserId());
            mfsAccountModel.setConnectionType(appUserModel.getMobileTypeId());
            //mfsAccountModel.setRegistrationStateId(appUserModel.getRegistrationStateId());
            mfsAccountModel.setNic(appUserModel.getNic());
            mfsAccountModel.setNicExpiryDate(appUserModel.getNicExpiryDate());
            mfsAccountModel.setCountry(appUserModel.getCountry());
            mfsAccountModel.setMotherMaidenName(appUserModel.getMotherMaidenName());
            mfsAccountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
            mfsAccountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
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

            String nic = appUserModel.getNic();
            List<AppUserHistoryViewModel> appUserHistoryViewModelList = searchAppUserHistoryView(nic);
            req.setAttribute("appUserHistoryViewModelList", appUserHistoryViewModelList);

            mfsAccountModel.setDialingCode(appUserModel.getMotherMaidenName());

            mfsAccountModel.setDob(appUserModel.getDob());
            mfsAccountModel.setSearchFirstName(ServletRequestUtils.getStringParameter(req, "searchFirstName"));
            mfsAccountModel.setSearchLastName(ServletRequestUtils.getStringParameter(req, "searchLastName"));
            mfsAccountModel.setSearchMfsId(ServletRequestUtils.getStringParameter(req, "searchMfsId"));
            mfsAccountModel.setSearchNic(ServletRequestUtils.getStringParameter(req, "searchNic"));


            CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
            mfsAccountModel.setAccountPurposeId(customerModel.getAccountPurposeIdAccountPurposeModel() == null ? null : customerModel.getAccountPurposeIdAccountPurposeModel().getAccountPurposeId());
            mfsAccountModel.setAccountPurposeName(customerModel.getAccountPurposeIdAccountPurposeModel() == null ? "N/A" : customerModel.getAccountPurposeIdAccountPurposeModel().getName());
            mfsAccountModel.setAccountReasonId(customerModel.getAccountReasonIdAccountReasonModel() == null ? null : customerModel.getAccountReasonIdAccountReasonModel().getAccountReasonId());
            mfsAccountModel.setAccountReasonName(customerModel.getAccountReasonIdAccountReasonModel() == null ? "N/A" : customerModel.getAccountReasonIdAccountReasonModel().getName());

            if (customerModel != null) {

                /**
                 * ******************************************************************************************************
                 * Updated by Soofia Faruq
                 * Customer's Picture is migrated from CustomerModel to CustomerPictureModel
                 */

                req.setAttribute("webServiceEnabled", customerModel.getWebServiceEnabled());
                Boolean isUssdEnabled = Boolean.TRUE;
                if (customerModel.getCustomerUSSDEnabled() != null)
                    isUssdEnabled = customerModel.getCustomerUSSDEnabled();

                req.setAttribute("isCustomerUSSDEnabled", isUssdEnabled);

                CustomerPictureModel customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                        PictureTypeConstants.CUSTOMER_PHOTO, customerModel.getCustomerId().longValue());

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
                        String filePath = getServletContext().getRealPath("images") + "/upload_dir/" + mfsAccountModel.getAppUserId() + "." + fileFormat;

                        //String filePath = getServletContext().getRealPath("images")+"/upload_dir/"+mfsAccountModel.getAppUserId()+".png";
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                    }
                }

//                if(mfsAccountModel.getUsecaseId()!=null&&mfsAccountModel.getUsecaseId().equals(PortalConstants.MFS_ACCOUNT_UPDATE_USECASE_ID)){
//                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
//                            PictureTypeConstants.PARENT_CNIC_SNAPSHOT, customerModel.getCustomerId().longValue());
//
//                    if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PARENT_CNIC_SNAPSHOT) {
//                        if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
//                            //Converting File bytes from db to input stream
//                            InputStream in = new ByteArrayInputStream(customerPictureModel.getPicture());
//                            ImageInputStream iis = ImageIO.createImageInputStream(in);
//
//                            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
//                            String fileFormat = "";
//                            while (imageReaders.hasNext()) {
//                                ImageReader reader = (ImageReader) imageReaders.next();
//                                System.out.printf("formatName: %s%n", reader.getFormatName());
//                                fileFormat = reader.getFormatName();
//                            }
//                            //generating path for bytes to write on
//                            String filePath = getServletContext().getRealPath("images") + "/upload_dir/parentCnicPic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;
//
//                            //String filePath = getServletContext().getRealPath("images")+"/upload_dir/cnicFrontPic_"+mfsAccountModel.getAppUserId()+".png";
//                            FileOutputStream fos = new FileOutputStream(filePath);
//                            fos.write(customerPictureModel.getPicture());
//                            fos.flush();
//                            fos.close();
//                            logger.info("Picture Extracted : " + filePath);
//                            mfsAccountModel.setParentCnicPicDiscrepant(customerPictureModel.getDiscrepant());
//                            mfsAccountModel.setParentCnicPicExt(fileFormat);
//                        }
//                    }
//
//                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
//                            PictureTypeConstants.B_FORM_SNAPSHOT, customerModel.getCustomerId().longValue());
//
//                    if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.B_FORM_SNAPSHOT) {
//                        if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
//                            //Converting File bytes from db to input stream
//                            InputStream in = new ByteArrayInputStream(customerPictureModel.getPicture());
//                            ImageInputStream iis = ImageIO.createImageInputStream(in);
//
//                            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
//                            String fileFormat = "";
//                            while (imageReaders.hasNext()) {
//                                ImageReader reader = (ImageReader) imageReaders.next();
//                                System.out.printf("formatName: %s%n", reader.getFormatName());
//                                fileFormat = reader.getFormatName();
//                            }
//                            //generating path for bytes to write on
//                            String filePath = getServletContext().getRealPath("images") + "/upload_dir/bFormPic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;
//
//                            //String filePath = getServletContext().getRealPath("images")+"/upload_dir/cnicFrontPic_"+mfsAccountModel.getAppUserId()+".png";
//                            FileOutputStream fos = new FileOutputStream(filePath);
//                            fos.write(customerPictureModel.getPicture());
//                            fos.flush();
//                            fos.close();
//                            logger.info("Picture Extracted : " + filePath);
//                            mfsAccountModel.setbFormPicDiscrepant(customerPictureModel.getDiscrepant());
//                            mfsAccountModel.setbFormPicExt(fileFormat);
//                        }
//                    }
//
//                    customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeIdAndStatus(
//                            PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());
//
//                    if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT) {
//                        if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
//                            //Converting File bytes from db to input stream
//                            InputStream in = new ByteArrayInputStream(customerPictureModel.getPicture());
//                            ImageInputStream iis = ImageIO.createImageInputStream(in);
//
//                            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
//                            String fileFormat = "";
//                            while (imageReaders.hasNext()) {
//                                ImageReader reader = (ImageReader) imageReaders.next();
//                                System.out.printf("formatName: %s%n", reader.getFormatName());
//                                fileFormat = reader.getFormatName();
//                            }
//                            //generating path for bytes to write on
//                            String filePath = getServletContext().getRealPath("images") + "/upload_dir/parentCnicBackPic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;
//
//                            //String filePath = getServletContext().getRealPath("images")+"/upload_dir/cnicFrontPic_"+mfsAccountModel.getAppUserId()+".png";
//                            FileOutputStream fos = new FileOutputStream(filePath);
//                            fos.write(customerPictureModel.getPicture());
//                            fos.flush();
//                            fos.close();
//                            logger.info("Picture Extracted : " + filePath);
//                            mfsAccountModel.setParentCnicBackPicDiscrepant(customerPictureModel.getDiscrepant());
//                            mfsAccountModel.setParentCnicBackPicExt(fileFormat);
//                        }
//                    }
//                }
                /**
                 * End
                 * *******************************************************************************************************
                 */

                /*
                 * Added by Turab to show in slider all the images of the customer
                 *
                 */

                String bdawaPath = getServletContext().getRealPath("images") + File.separator + "img-not-found.gif";

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

                        //String filePath = getServletContext().getRealPath("images")+"/upload_dir/customerPic_"+mfsAccountModel.getAppUserId()+".png";
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(customerPictureModel.getPicture());
                        fos.flush();
                        fos.close();
                        logger.info("Picture Extracted : " + filePath);
                        mfsAccountModel.setCustomerPicDiscrepant(customerPictureModel.getDiscrepant());
                        mfsAccountModel.setCustomerPicExt(fileFormat);
                    }
                }

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
                } else {
                    String filePath = getServletContext().getRealPath("images") + "/upload_dir/signPic_" + mfsAccountModel.getAppUserId() + ".png";
                    FileOutputStream fos = new FileOutputStream(filePath);
                    fos.write(extractBytes(bdawaPath));
                    fos.flush();
                    fos.close();
                    logger.info("Picture Extracted : " + filePath);
                }


                customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                        PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModel.getCustomerId().longValue());

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


//                customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
//                        PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT, customerModel.getCustomerId().longValue());
//
//                if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT) {
//                    if (customerPictureModel.getPicture() != null && customerPictureModel.getPicture().length > 1) {
//                        //Converting File bytes from db to input stream
//                        InputStream in = new ByteArrayInputStream(customerPictureModel.getPicture());
//                        ImageInputStream iis = ImageIO.createImageInputStream(in);
//
//                        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
//                        String fileFormat = "";
//                        while (imageReaders.hasNext()) {
//                            ImageReader reader = (ImageReader) imageReaders.next();
//                            System.out.printf("formatName: %s%n", reader.getFormatName());
//                            fileFormat = reader.getFormatName();
//                        }
//                        //generating path for bytes to write on
//                        String filePath = getServletContext().getRealPath("images") + "/upload_dir/sourceOfIncomePic_" + mfsAccountModel.getAppUserId() + "." + fileFormat;
//
//                        //String filePath = getServletContext().getRealPath("images")+"/upload_dir/cnicFrontPic_"+mfsAccountModel.getAppUserId()+".png";
//                        FileOutputStream fos = new FileOutputStream(filePath);
//                        fos.write(customerPictureModel.getPicture());
//                        fos.flush();
//                        fos.close();
//                        logger.info("Picture Extracted : " + filePath);
//                        mfsAccountModel.setSourceOfIncomePicDiscrepant(customerPictureModel.getDiscrepant());
//                        mfsAccountModel.setSourceOfIncomePicExt(fileFormat);
//                    }
//                }


                customerPictureModel = this.mfsAccountManager.getCustomerPictureByTypeId(
                        PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());

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
                } else {
                    String filePath = getServletContext().getRealPath("images") + "/upload_dir/cnicBackPic_" + mfsAccountModel.getAppUserId() + ".png";
                    FileOutputStream fos = new FileOutputStream(filePath);
                    fos.write(extractBytes(bdawaPath));
                    fos.flush();
                    fos.close();
                    logger.info("Picture Extracted : " + filePath);
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

                mfsAccountModel.setName(customerModel.getName());
                mfsAccountModel.setNokContactNo(customerModel.getNokContactNo());
                mfsAccountModel.setNokMobile(customerModel.getNokMobile());
                mfsAccountModel.setNokName(customerModel.getNokName());
                mfsAccountModel.setNokRelationship(customerModel.getNokRelationship());
                mfsAccountModel.setNokNic(customerModel.getNokNic());
                mfsAccountModel.setComments(customerModel.getComments());
                mfsAccountModel.setTypeOfCustomerName(customerModel.getCustomerTypeIdCustomerTypeModel() == null ? null : customerModel.getCustomerTypeIdCustomerTypeModel().getName());
                mfsAccountModel.setTransactionModeId(customerModel.getTransactionModeIdTransactionModeModel() == null ? null : customerModel.getTransactionModeIdTransactionModeModel().getTransactionModeId());
                mfsAccountModel.setInitialDeposit(customerModel.getInitialDeposit());
                mfsAccountModel.setAccounttypeName(customerModel.getCustomerAccountTypeIdCustomerAccountTypeModel().getName());
                mfsAccountModel.setSegmentNameStr(customerModel.getSegmentIdSegmentModel().getName());
                mfsAccountModel.setRegStateName((appUserModel.getRegistrationStateModel() == null ? "" : appUserModel.getRegistrationStateModel().getName()));

                mfsAccountModel.setBirthPlace(customerModel.getBirthPlace());
                mfsAccountModel.setScreeningPerformed(customerModel.isScreeningPerformed());
                mfsAccountModel.setVerisysDone(customerModel.getVerisysDone());

                mfsAccountModel.setHraNokMobile(customerModel.getNokContactNo());
                mfsAccountModel.setHraOccupation(customerModel.getHraOccupation());
                mfsAccountModel.setHraTransactionPurpose(customerModel.getHraTrxnPurpose());

                //CustomerRemitterModel  customerRemitterModel = customerModel.getCustomerRemitterIdCustomerRemitterModel();

                if (customerModel.getTaxRegimeIdTaxRegimeModel() != null) {
                    mfsAccountModel.setTaxRegimeName(customerModel.getTaxRegimeIdTaxRegimeModel().getName());
                }
                mfsAccountModel.setFed(customerModel.getFed());
                SmartMoneyAccountModel _smartMoneyAccountModel = new SmartMoneyAccountModel();
                _smartMoneyAccountModel.setCustomerId(customerModel.getCustomerId());
                _smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                SmartMoneyAccountModel smartMoneyAccount = this.mfsAccountManager.getSmartMoneyAccountByExample(_smartMoneyAccountModel);
                //SmartMoneyAccountModel smartMoneyAccount = this.mfsAccountManager.getSmartMoneyAccountByCustomerId(customerModel.getCustomerId());

                if (smartMoneyAccount != null) {
                    String accountStatus = this.getAccountState(appUserModel.getAccountStateId());
                    mfsAccountModel.setIsDebitBlocked(smartMoneyAccount.getIsDebitBlocked());
                    mfsAccountModel.setDebitBlockAmount(smartMoneyAccount.getDebitBlockAmount());
                    mfsAccountModel.setAccountState(accountStatus);
                }

                Long olaCustomerAccountTypeId = customerModel.getCustomerAccountTypeId();

                NumberFormat formatter = new DecimalFormat("#0.00");
                Date currentDate = new Date();
                Calendar cal = GregorianCalendar.getInstance();
                Date startDate;
                Double dailyDebitConsumed = 0.0d, dailyCreditConsumed = 0.0d, monthlyDebitConsumed = 0.0d,
                        monthlyCreditConsumed = 0.0d, yearlyDebitConsumed = 0.0d, yearlyCreditConsumed = 0.0d;
                List<Double> remainingLimits;

                AccountModel accountModel = accountManager.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(mfsAccountModel.getNic(), olaCustomerAccountTypeId
                        , OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
                if (accountModel == null)
                    accountModel = accountManager.getLastClosedAccount(mfsAccountModel.getNic(), olaCustomerAccountTypeId);

                SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
                sma.setCustomerId(customerModel.getCustomerId());
                sma.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
                sma.setActive(Boolean.TRUE);
                SmartMoneyAccountModel hraSmartMoneyAccountModel = this.mfsAccountManager.getSmartMoneyAccountByExample(sma);
                Long hraAccountId = null;
                if (hraSmartMoneyAccountModel == null) {
                    sma.setActive(Boolean.FALSE);
                    List<SmartMoneyAccountModel> list = mfsAccountManager.getLastClosedSMAAccount(sma);
                    if (list != null && !list.isEmpty())
                        hraSmartMoneyAccountModel = list.get(0);
                }
                if (hraSmartMoneyAccountModel != null)
                    hraAccountId = hraSmartMoneyAccountModel.getSmartMoneyAccountId();
                if (hraAccountId != null) {
                    AccountModel hraAccountModel = accountManager.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(mfsAccountModel.getNic(), CustomerAccountTypeConstants.HRA
                            , OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
                    if (hraAccountModel == null)
                        hraAccountModel = accountManager.getLastClosedAccount(mfsAccountModel.getNic(), CustomerAccountTypeConstants.HRA);
                    Long accountId = hraAccountModel.getAccountId();
                    List<CustomerRemitterModel> customerRemitterModelList = getRemittanceModelList(customerModel.getCustomerId());
                    req.setAttribute("customerRemitterModelList", customerRemitterModelList);
                    //daily debit consumed
                    dailyDebitConsumed = ledgerManager.getDailyConsumedBalance(accountId, TransactionTypeConstants.DEBIT, currentDate);
                    //daily credit consumed
                    dailyCreditConsumed = ledgerManager.getDailyConsumedBalance(accountId, TransactionTypeConstants.CREDIT, currentDate);
                    cal.setTime(new Date());
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    startDate = cal.getTime();

                    monthlyDebitConsumed = ledgerManager.getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.DEBIT, startDate, currentDate);
                    //monthly credit consumed
                    monthlyCreditConsumed = ledgerManager.getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.CREDIT, startDate, currentDate);
                    //yearly debit consumed
                    cal.setTime(new Date());
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    cal.set(Calendar.MONTH, 0);
                    startDate = cal.getTime();

                    yearlyDebitConsumed = ledgerManager.getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.DEBIT, startDate, currentDate);
                    //yearly credit consumed
                    yearlyCreditConsumed = ledgerManager.getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.CREDIT, startDate, currentDate);
                    //

                    remainingLimits = calculateLimits(dailyDebitConsumed, dailyCreditConsumed, monthlyDebitConsumed, monthlyCreditConsumed,
                            yearlyDebitConsumed, yearlyCreditConsumed, 4L);

                    //formatter.format(remainingLimits.get(0))
                    String accountState = null;
                    if (hraSmartMoneyAccountModel.getAccountStateId() != null)
                        accountState = getAccountState(hraSmartMoneyAccountModel.getAccountStateId());

                    mfsAccountModel.setHraNokMobile(customerModel.getNokContactNo());
                    mfsAccountModel.setHraAccountState(accountState);
                    mfsAccountModel.setHraAccounttypeName("HRA");
                    mfsAccountModel.setRemainingHRADailyCreditLimit(formatter.format(remainingLimits.get(0)));
                    mfsAccountModel.setRemainingHRADailyDebitLimit(formatter.format(remainingLimits.get(1)));
                    mfsAccountModel.setRemainingHRAMonthlyCreditLimit(formatter.format(remainingLimits.get(2)));
                    mfsAccountModel.setRemainingHRAMonthlyDebitLimit(formatter.format(remainingLimits.get(3)));
                    mfsAccountModel.setRemainingHRAYearlyCreditLimit(formatter.format(remainingLimits.get(4)));
                    mfsAccountModel.setRemainingHRAYearlyDebitLimit(formatter.format(remainingLimits.get(5)));
                    //
                    req.setAttribute("isHraAccount", true);

                    if (hraSmartMoneyAccountModel != null && hraSmartMoneyAccountModel.getRegistrationStateId() != null) {
                        mfsAccountModel.setHraRegistrationStateId(hraSmartMoneyAccountModel.getRegistrationStateId());
                        if (hraSmartMoneyAccountModel.getAccountStateId() != null) {
                            String accountStatus = this.getAccountState(hraSmartMoneyAccountModel.getAccountStateId());
                            mfsAccountModel.setHraAccountState(accountStatus);
                        }
                    }
                } else {
                    req.setAttribute("isHraAccount", false);
                }

                if (accountModel != null) {
                    currentDate = new Date();
                    cal = GregorianCalendar.getInstance();
                    //daily debit consumed
                    dailyDebitConsumed = ledgerManager.getDailyConsumedBalance(accountModel.getAccountId(), TransactionTypeConstants.DEBIT, currentDate);
                    if (dailyDebitConsumed == null)
                        dailyDebitConsumed = 0.0d;
                    //daily credit consumed
                    dailyCreditConsumed = ledgerManager.getDailyConsumedBalance(accountModel.getAccountId(), TransactionTypeConstants.CREDIT, currentDate);
                    if (dailyCreditConsumed == null)
                        dailyCreditConsumed = 0.0d;
		    	  /*
		    	  //weekly debit consumed
		    	  cal.add(Calendar.DATE, -7);
		    	  Double weeklyDebitConsumed = ledgerManager.getConsumedBalanceByDateRange(accountModel.getAccountId(), TransactionTypeConstants.DEBIT, cal.getTime(), currentDate);
		    	  //weekly credit consumed
		    	  Double weeklyCreditConsumed = ledgerManager.getConsumedBalanceByDateRange(accountModel.getAccountId(), TransactionTypeConstants.CREDIT, cal.getTime(), currentDate);
		    	  */
                    //monthly debit consumed
                    cal.setTime(new Date());
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    startDate = cal.getTime();

                    monthlyDebitConsumed = ledgerManager.getConsumedBalanceByDateRange(accountModel.getAccountId(), TransactionTypeConstants.DEBIT, startDate, currentDate);
                    if (monthlyDebitConsumed == null)
                        monthlyDebitConsumed = 0.0d;
                    //monthly credit consumed
                    monthlyCreditConsumed = ledgerManager.getConsumedBalanceByDateRange(accountModel.getAccountId(), TransactionTypeConstants.CREDIT, startDate, currentDate);
                    if (monthlyCreditConsumed == null)
                        monthlyCreditConsumed = 0.0d;
                    //yearly debit consumed
                    cal.setTime(new Date());
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    cal.set(Calendar.MONTH, 0);
                    startDate = cal.getTime();

                    yearlyDebitConsumed = ledgerManager.getConsumedBalanceByDateRange(accountModel.getAccountId(), TransactionTypeConstants.DEBIT, startDate, currentDate);
                    if (yearlyDebitConsumed == null)
                        yearlyDebitConsumed = 0.0d;
                    //yearly credit consumed
                    yearlyCreditConsumed = ledgerManager.getConsumedBalanceByDateRange(accountModel.getAccountId(), TransactionTypeConstants.CREDIT, startDate, currentDate);
                    if (yearlyCreditConsumed == null)
                        yearlyCreditConsumed = 0.0d;
                    if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.BLINK)) {
                        remainingLimits = calculateBlinkCustomerLimits(dailyDebitConsumed, dailyCreditConsumed, monthlyDebitConsumed, monthlyCreditConsumed,
                                yearlyDebitConsumed, yearlyCreditConsumed, 4L, appUserModel.getCustomerId());
                        req.setAttribute("isBlinkAccount", true);
                    } else {
                        remainingLimits = calculateLimits(dailyDebitConsumed, dailyCreditConsumed, monthlyDebitConsumed, monthlyCreditConsumed,
                                yearlyDebitConsumed, yearlyCreditConsumed, customerModel.getCustomerAccountTypeId());
                        //
                    }

                    mfsAccountModel.setRemainingDailyCreditLimit(formatter.format(remainingLimits.get(0)));
                    mfsAccountModel.setRemainingDailyDebitLimit(formatter.format(remainingLimits.get(1)));
                    mfsAccountModel.setRemainingMonthlyCreditLimit(formatter.format(remainingLimits.get(2)));
                    mfsAccountModel.setRemainingMonthlyDebitLimit(formatter.format(remainingLimits.get(3)));
                    mfsAccountModel.setRemainingYearlyCreditLimit(formatter.format(remainingLimits.get(4)));
                    mfsAccountModel.setRemainingYearlyDebitLimit(formatter.format(remainingLimits.get(5)));
                } else {
                    super.saveMessage(req, "Could not retreive remaining limits for this customer");
                }
                /*******************************************************end by turab***************************************************/
                String cnic = appUserModel.getNic();
                _smartMoneyAccountModel = new SmartMoneyAccountModel();
                _smartMoneyAccountModel.setCustomerId(customerModel.getCustomerId());
                _smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                smartMoneyAccount = this.mfsAccountManager.getSmartMoneyAccountByExample(_smartMoneyAccountModel);
                if (smartMoneyAccount != null && smartMoneyAccount.getSmartMoneyAccountId() != null) {
                    req.setAttribute("smAccountId", smartMoneyAccount.getSmartMoneyAccountId());
                }
                if (appUserModel.getRegistrationStateId() != null)
                    mfsAccountModel.setRegistrationStateId(appUserModel.getRegistrationStateId());
                UserDeviceAccountsModel deviceAccountModel = this.mfsAccountManager.getDeviceAccountByAppUserId(Long.valueOf(appUserId), DeviceTypeConstantsInterface.MOBILE);
                if (deviceAccountModel != null && deviceAccountModel.getUserDeviceAccountsId() != null) {

                    // Set Device Account ID
                    mfsAccountModel.setCustomerId(deviceAccountModel.getUserId());
                    req.setAttribute("deviceAccId", deviceAccountModel.getUserDeviceAccountsId());
                    req.setAttribute("deviceAccEnabled", deviceAccountModel.getAccountEnabled());
                    req.setAttribute("deviceAccLocked", deviceAccountModel.getAccountLocked());
                    req.setAttribute("credentialsExpired", deviceAccountModel.getCredentialsExpired());
                    //req.setAttribute("blacklisted", appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.BLACKLISTED)?true:false);
                    req.setAttribute("blacklisted", accountControlManager.isCnicBlacklisted(cnic));


                    String statusDetails = appUserManager.getStatusDetails(appUserModel.getAppUserId(),
                            deviceAccountModel.getUpdatedOn(),
                            deviceAccountModel.getAccountLocked(),
                            deviceAccountModel.getAccountEnabled()
                    );
                    req.setAttribute("statusDetails", statusDetails);

                }
                mfsAccountModel.setApplicationNo(customerModel.getApplicationN0());
                mfsAccountModel.setApplicationDate(customerModel.getCreatedOn());
                mfsAccountModel.setName(customerModel.getName());
                mfsAccountModel.setFatherHusbandName(customerModel.getFatherHusbandName());

                if (customerModel.getGender() != null) {
                    if (customerModel.getGender().equals("M")) {
                        mfsAccountModel.setGender("Male");
                    } else if (customerModel.getGender().equals("F")) {
                        mfsAccountModel.setGender("Female");
                    } else if (customerModel.getGender().equals("K")) {
                        mfsAccountModel.setGender("Khwaja Sira");
                    }
                }
                mfsAccountModel.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
                if (customerModel.getCustomerTypeIdCustomerTypeModel() != null) {
                    mfsAccountModel.setCustomerAccountName(customerModel.getCustomerTypeIdCustomerTypeModel().getName());
                }
                mfsAccountModel.setEmail(customerModel.getEmail());
                mfsAccountModel.setCreatedOn(customerModel.getCreatedOn());

                UserInfoListViewModel userInfoListViewModel = new UserInfoListViewModel();
                userInfoListViewModel.setAppUserId(appUserModel.getAppUserId());
                BaseWrapper baseWrapperUserInfo = new BaseWrapperImpl();
                baseWrapperUserInfo.setBasePersistableModel(userInfoListViewModel);
                baseWrapperUserInfo = mfsAccountManager.searchUserInfoByPrimaryKey(baseWrapperUserInfo);
                userInfoListViewModel = (UserInfoListViewModel) baseWrapperUserInfo.getBasePersistableModel();
                if (userInfoListViewModel != null) {
                    mfsAccountModel.setCreatedBy(userInfoListViewModel.getAccountOpenedBy());
                }

                mfsAccountModel.setLocation(mfsAccountManager.getAreaByAppUserId(customerModel.getCreatedBy()));

                mfsAccountModel.setContactNo(customerModel.getContactNo());
                mfsAccountModel.setLandLineNo(customerModel.getLandLineNo());
                mfsAccountModel.setMobileNo(customerModel.getMobileNo());
                mfsAccountModel.setRefferedBy(customerModel.getReferringName1());
                if (customerModel.getFundSourceIdFundSourceModel() != null) {
                    mfsAccountModel.setFundSourceNarration(customerModel.getFundSourceIdFundSourceModel().getName());
                }

                mfsAccountModel.setSegmentId(customerModel.getSegmentId());
                if (customerModel.getSegmentIdSegmentModel() != null) {
                    mfsAccountModel.setSegmentName(customerModel.getSegmentIdSegmentModel().getName());
                }
                mfsAccountModel.setCustomerTypeId(customerModel.getCustomerTypeId());
                if (customerModel.getCustomerAccountTypeIdCustomerAccountTypeModel() != null) {
                    mfsAccountModel.setTypeOfCustomerName(customerModel.getCustomerAccountTypeIdCustomerAccountTypeModel().getName());
                }
                mfsAccountModel.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
//	    	  if(customerModel.getFundSourceId() != null){
//	    		  mfsAccountModel.setFundsSourceId(customerModel.getFundSourceId());
//	    		  mfsAccountModel.setFundSourceName(customerModel.getFundSourceIdFundSourceModel().getName());
//	    	  }

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


                // Populating Address Fields
                Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
                if (customerAddresses != null && customerAddresses.size() > 0) {
                    for (CustomerAddressesModel custAdd : customerAddresses) {

                        AddressModel addressModel = custAdd.getAddressIdAddressModel();

                        if (custAdd.getAddressTypeId() == 4) {
                            if (addressModel.getFullAddress() != null && !addressModel.getFullAddress().isEmpty()) {
                                mfsAccountModel.setNokMailingAdd(addressModel.getFullAddress());
                            }
                        }

                        if (null != custAdd.getApplicantTypeId() && !custAdd.getApplicantTypeId().equals(ApplicantTypeConstants.APPLICANT_TYPE_1))
                            continue;

                        if (custAdd.getAddressTypeId() == 1) {
                            if (addressModel.getCityId() != null) {
                                mfsAccountModel.setCity(addressModel.getCityIdCityModel().getName());
                            } else if (addressModel.getOtherCity() != null && !addressModel.getOtherCity().isEmpty()) {
                                mfsAccountModel.setPresentHomeAddCityName(addressModel.getOtherCity());
                            }
                            if (addressModel.getPostalOfficeId() != null) {
                                mfsAccountModel.setPresentAddPostalOfficeId(addressModel.getPostalOfficeId());
                                //mfsAccountModel.setPresentHomeAddPostalOfficeName(addressModel.getPostalOfficeIdPostalOfficeModel().getName());
                            }
                            if (addressModel.getFullAddress() != null && !addressModel.getFullAddress().isEmpty()) {
                                mfsAccountModel.setPresentAddress(addressModel.getFullAddress());
                            }

                        } else if (custAdd.getAddressTypeId() == 2) {
                            if (addressModel.getCityId() != null) {
                                mfsAccountModel.setPermanentAddCityId(addressModel.getCityId());
                                mfsAccountModel.setPermanentHomeAddCityName(addressModel.getCityIdCityModel().getName());
                            } else if (addressModel.getOtherCity() != null && !addressModel.getOtherCity().isEmpty()) {
                                mfsAccountModel.setPermanentAddCityOthers(addressModel.getOtherCity());
                            }
                            if (addressModel.getDistrictId() != null) {
                                mfsAccountModel.setPermanentAddDistrictId(addressModel.getDistrictId());
                                mfsAccountModel.setPermanentHomeAddDistrictName(addressModel.getDistrictIdDistrictModel().getName());
                            } else if (addressModel.getOtherDistrict() != null && !addressModel.getOtherDistrict().isEmpty()) {
                                mfsAccountModel.setPermanentDistOthers(addressModel.getOtherDistrict());
                            }
                            if (addressModel.getPostalOfficeId() != null) {
                                mfsAccountModel.setPermanentAddPostalOfficeId(addressModel.getPostalOfficeId());
                                mfsAccountModel.setPermanentHomeAddPostalOfficeName(addressModel.getPostalOfficeIdPostalOfficeModel().getName());
                            } else if (addressModel.getOtherPostalOffice() != null && !addressModel.getOtherPostalOffice().isEmpty()) {
                                mfsAccountModel.setPermanentPostalOfficeOthers(addressModel.getOtherPostalOffice());
                            }
                            if (addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()) {
                                mfsAccountModel.setPermanentAddHouseNo(addressModel.getHouseNo());
                            }
                            if (addressModel.getStreetNo() != null && !addressModel.getStreetNo().isEmpty()) {
                                mfsAccountModel.setpermanentAddStreetNo(addressModel.getStreetNo());
                            }
                        } else if (custAdd.getAddressTypeId() == 3) {
                            if (addressModel.getPostalOfficeId() != null) {
                                mfsAccountModel.setBuisnessPostalOfficeId(addressModel.getPostalOfficeId());
                            }
                            if (addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()) {
                                mfsAccountModel.setBuisnessAddress(addressModel.getHouseNo());
                            }
                        }
                    }
                }


            }

            req.setAttribute("pageMfsId", (String) baseWrapper.getObject("userId"));
            // for the logging process
            mfsAccountModel.setActionId(PortalConstants.ACTION_UPDATE);
            mfsAccountModel.setUsecaseId(new Long(PortalConstants.MFS_ACCOUNT_UPDATE_USECASE_ID));

            return mfsAccountModel;
        } else {
            MfsAccountModel mfsAccountModel = new MfsAccountModel();
            // for the logging process
            mfsAccountModel.setActionId(PortalConstants.ACTION_CREATE);
            mfsAccountModel.setUsecaseId(new Long(PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID));
            return mfsAccountModel;
        }

    }

    private List<CustomerRemitterModel> getRemittanceModelList(Long cusId) throws FrameworkCheckedException {
        List<CustomerRemitterModel> listCustomerRemitterModels = customerManager.getRemittanceModelList(cusId);
        return listCustomerRemitterModels;
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest req) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
        ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
        return modelAndView;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
        ModelAndView modelAndView = new ModelAndView(getSuccessView());

        return modelAndView;
    }

    private List<AppUserHistoryViewModel> searchAppUserHistoryView(String nic) throws FrameworkCheckedException {
        nic = nic.substring(nic.indexOf("-") + 1);
        List<AppUserHistoryViewModel> appUserHistoryViewModelList = null;
        AppUserHistoryViewModel appUserHistoryViewModel = new AppUserHistoryViewModel();
        appUserHistoryViewModel.setNic(nic);

        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        wrapper.setBasePersistableModel(appUserHistoryViewModel);

        LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<>();
        sortingOrderMap.put("createdOn", SortingOrder.ASC);
        wrapper.setSortingOrderMap(sortingOrderMap);

        wrapper = appUserManager.searchAppUserHistoryView(wrapper);
        CustomList<AppUserHistoryViewModel> customList = wrapper.getCustomList();
        if (customList != null) {
            appUserHistoryViewModelList = customList.getResultsetList();
        }
        return appUserHistoryViewModelList;
    }

    public byte[] extractBytes(String ImageName) throws IOException {
        String base64String = com.inov8.ola.util.Base64.encodeFromFile(ImageName);
        return Base64.decode(base64String);
    }

    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    public void setFinancialIntegrationManager(
            FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void setLedgerManager(LedgerManager ledgerManager) {
        this.ledgerManager = ledgerManager;
    }

    public void setLimitManager(LimitManager limitManager) {
        this.limitManager = limitManager;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        if (referenceDataManager != null) {
            this.referenceDataManager = referenceDataManager;
        }
    }

    public void setAccountControlManager(AccountControlManager accountControlManager) {
        this.accountControlManager = accountControlManager;
    }

    private List<Double> calculateLimits(Double dailyDebitConsumed, Double dailyCreditConsumed, Double monthlyDebitConsumed, Double monthlyCreditConsumed,
                                         Double yearlyDebitConsumed, Double yearlyCreditConsumed, Long customerAccountTypeId) throws FrameworkCheckedException {
        List<LimitModel> limits = limitManager.getLimitsByCustomerAccountType(customerAccountTypeId);
        List<Double> remainingLimits = new ArrayList<>();

        Double remainingDailyCreditLimit = 0.0;
        Double remainingDailyDebitLimit = 0.0;
        Double remainingMonthlyCreditLimit = 0.0;
        Double remainingMonthlyDebitLimit = 0.0;
        Double remainingYearlyCreditLimit = 0.0;
        Double remainingYearlyDebitLimit = 0.0;

        for (LimitModel limitModel : limits) {
            if (limitModel.getLimitTypeId().equals(LimitTypeConstants.DAILY)) {
                if (limitModel.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT)) {
                    remainingDailyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - dailyCreditConsumed;
                    remainingDailyCreditLimit = remainingDailyCreditLimit < 0 ? 0 : remainingDailyCreditLimit;
                } else {
                    remainingDailyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - dailyDebitConsumed;
                    remainingDailyDebitLimit = (remainingDailyDebitLimit < 0 ? 0 : remainingDailyDebitLimit);
                }
            } else if (limitModel.getLimitTypeId().equals(LimitTypeConstants.MONTHLY)) {
                if (limitModel.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT)) {
                    remainingMonthlyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - monthlyCreditConsumed;
                    remainingMonthlyCreditLimit = remainingMonthlyCreditLimit < 0 ? 0 : remainingMonthlyCreditLimit;
                } else {
                    remainingMonthlyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - monthlyDebitConsumed;
                    remainingMonthlyDebitLimit = remainingMonthlyDebitLimit < 0 ? 0 : remainingMonthlyDebitLimit;
                }
            } else if (limitModel.getLimitTypeId().equals(LimitTypeConstants.YEARLY)) {
                if (limitModel.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT)) {
                    remainingYearlyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - yearlyCreditConsumed;
                    remainingYearlyCreditLimit = remainingYearlyCreditLimit < 0 ? 0 : remainingYearlyCreditLimit;
                } else {
                    remainingYearlyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - yearlyDebitConsumed;
                    remainingYearlyDebitLimit = remainingYearlyDebitLimit < 0 ? 0 : remainingYearlyDebitLimit;
                }
            }
        }
        remainingLimits.add(remainingDailyCreditLimit);
        remainingLimits.add(remainingDailyDebitLimit);
        remainingLimits.add(remainingMonthlyCreditLimit);
        remainingLimits.add(remainingMonthlyDebitLimit);
        remainingLimits.add(remainingYearlyCreditLimit);
        remainingLimits.add(remainingYearlyDebitLimit);
        return remainingLimits;
    }


    private List<Double> calculateBlinkCustomerLimits(Double dailyDebitConsumed, Double dailyCreditConsumed, Double monthlyDebitConsumed, Double monthlyCreditConsumed,
                                                      Double yearlyDebitConsumed, Double yearlyCreditConsumed, Long customerAccountTypeId, Long customerId) throws FrameworkCheckedException {
        List<BlinkCustomerLimitModel> limits = limitManager.getBlinkCustomerLimitByTransactionTypeByCustomerId(customerId);
        List<Double> remainingLimits = new ArrayList<>();

        Double remainingDailyCreditLimit = 0.0;
        Double remainingDailyDebitLimit = 0.0;
        Double remainingMonthlyCreditLimit = 0.0;
        Double remainingMonthlyDebitLimit = 0.0;
        Double remainingYearlyCreditLimit = 0.0;
        Double remainingYearlyDebitLimit = 0.0;

        for (BlinkCustomerLimitModel limitModel : limits) {
            if (limitModel.getLimitType().equals(LimitTypeConstants.DAILY)) {
                if (limitModel.getTransactionType().equals(TransactionTypeConstants.CREDIT)) {
                    remainingDailyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - dailyCreditConsumed;
                    remainingDailyCreditLimit = remainingDailyCreditLimit < 0 ? 0 : remainingDailyCreditLimit;
                } else {
                    remainingDailyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - dailyDebitConsumed;
                    remainingDailyDebitLimit = (remainingDailyDebitLimit < 0 ? 0 : remainingDailyDebitLimit);
                }
            } else if (limitModel.getLimitType().equals(LimitTypeConstants.MONTHLY)) {
                if (limitModel.getTransactionType().equals(TransactionTypeConstants.CREDIT)) {
                    remainingMonthlyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - monthlyCreditConsumed;
                    remainingMonthlyCreditLimit = remainingMonthlyCreditLimit < 0 ? 0 : remainingMonthlyCreditLimit;
                } else {
                    remainingMonthlyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - monthlyDebitConsumed;
                    remainingMonthlyDebitLimit = remainingMonthlyDebitLimit < 0 ? 0 : remainingMonthlyDebitLimit;
                }
            } else if (limitModel.getLimitType().equals(LimitTypeConstants.YEARLY)) {
                if (limitModel.getTransactionType().equals(TransactionTypeConstants.CREDIT)) {
                    remainingYearlyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - yearlyCreditConsumed;
                    remainingYearlyCreditLimit = remainingYearlyCreditLimit < 0 ? 0 : remainingYearlyCreditLimit;
                } else {
                    remainingYearlyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - yearlyDebitConsumed;
                    remainingYearlyDebitLimit = remainingYearlyDebitLimit < 0 ? 0 : remainingYearlyDebitLimit;
                }
            }
        }
        remainingLimits.add(remainingDailyCreditLimit);
        remainingLimits.add(remainingDailyDebitLimit);
        remainingLimits.add(remainingMonthlyCreditLimit);
        remainingLimits.add(remainingMonthlyDebitLimit);
        remainingLimits.add(remainingYearlyCreditLimit);
        remainingLimits.add(remainingYearlyDebitLimit);
        return remainingLimits;
    }

    public void setCustomerManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    private Long getOlaCustomerAccountTypeId(CustomerModel customerModel) {
        Long olaCustomerAccountTypeId = null;
        Long customerAccountTypeId = customerModel.getCustomerAccountTypeId();

        if (customerAccountTypeId.equals(CustomerAccountTypeConstants.LEVEL_0))
            olaCustomerAccountTypeId = CustomerAccountTypeConstants.LEVEL_0;
        else if (customerAccountTypeId.equals(CustomerAccountTypeConstants.LEVEL_1))
            olaCustomerAccountTypeId = CustomerAccountTypeConstants.LEVEL_1;
        else if (customerAccountTypeId.equals(CustomerAccountTypeConstants.LEVEL_2))
            olaCustomerAccountTypeId = CustomerAccountTypeConstants.LEVEL_1;

        return olaCustomerAccountTypeId;
    }

    private String getAccountState(Long accountStateId) {
        String status = null;
        if (accountStateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_CLOSED)) {
            status = "Closed"; //closed is handling now from smartmoneyaccount
        } else if (accountStateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_COLD)) {
            status = "Cold";
        } else if (accountStateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_DECEASED)) {
            status = "Deceased";
        } else if (accountStateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_DORMANT)) {//dormatn handling is now from smartmoneyaccount
            status = "Dormant";
        } else if (accountStateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_HOT)) {
            status = "Hot";
        } else if (accountStateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_WARM)) {
            status = "Warm";
        }
        return status;
    }
}
