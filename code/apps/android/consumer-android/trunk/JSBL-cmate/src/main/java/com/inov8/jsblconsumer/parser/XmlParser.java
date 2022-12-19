package com.inov8.jsblconsumer.parser;

import com.inov8.jsblconsumer.model.AdModel;
import com.inov8.jsblconsumer.model.AdvanceLoanModel;
import com.inov8.jsblconsumer.model.BankAccountModel;
import com.inov8.jsblconsumer.model.BankModel;
import com.inov8.jsblconsumer.model.CardApplicantTypeModel;
import com.inov8.jsblconsumer.model.CardCategoryModel;
import com.inov8.jsblconsumer.model.CardRankModel;
import com.inov8.jsblconsumer.model.CardStateModel;
import com.inov8.jsblconsumer.model.CardTypeModel;
import com.inov8.jsblconsumer.model.CategoryModel;
import com.inov8.jsblconsumer.model.FaqsModel;
import com.inov8.jsblconsumer.model.LocationModel;
import com.inov8.jsblconsumer.model.MbankModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.PaymentReasonModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.model.ServiceModel;
import com.inov8.jsblconsumer.model.SupplierModel;
import com.inov8.jsblconsumer.model.TpurpsModel;
import com.inov8.jsblconsumer.model.TransactionModel;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.XmlConstants;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

public class XmlParser {

    private KXmlParser parser = null;
    private String msgType = null;

    public XmlParser() {
        parser = new KXmlParser();
    }

    public Hashtable<String, Object> convertXmlToTable(String xml) {
        Hashtable<String, Object> tbl = null;
        try {
            parser.setInput(new InputStreamReader(new ByteArrayInputStream(xml
                    .getBytes())));
            int eventType = parser.getEventType();
            eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();

                if (startTag != null) {
                    startTag = startTag.toLowerCase();
                    if (startTag.equals(XmlConstants.Tags.MSG)) {
                        msgType = parser.getAttributeValue(null,
                                XmlConstants.Attributes.MSG_ID);

                        if (msgType == null || msgType.equals(""))
                            return tbl;

                        tbl = new Hashtable<String, Object>();
                        tbl.put(Constants.KEY_CMDRCVD, msgType.trim());

                        int commandReceived = Integer.parseInt(msgType);
                        switch (commandReceived) {
                            case Constants.RSP_ERROR:
                                parseErrorMsg(tbl);
                                break;

//                            case Constants.CMD_CUSTOMER_REGISTRATION:
//                                parseRegistrationMessage1(tbl);
//                                break;
                            case Constants.CMD_CUSTOMER_REGISTRATION:
                            case Constants.CMD_LOGIN:
                            case Constants.CMD_CHECK_BALANCE:
                            case Constants.CMD_DEVICE_UPDATE_OTP_VERIFICATION:
                            case Constants.CMD_LOGIN_OTP_VERIFICATION:
                            case Constants.CMD_OPEN_ACCOUNT:
                            case Constants.CMD_CUSTOMER_REGISTER_OTP:
                            case Constants.CMD_RETAIL_PAYMENT_VERIFICATION:
                            case Constants.CMD_GENERATE_MPIN:
                            case Constants.CMD_FORGOT_PASSWORD_FIRST:
                            case Constants.CMD_FORGOT_PASSWORD_SECOND:
                            case Constants.CMD_DEBIT_CARD:
                            case Constants.CMD_DEBIT_CARD_CONFIRMATION:
                            case Constants.CMD_RESET_LOGIN_PIN:
                            case Constants.CMD_FORGOT_MPIN:
                            case Constants.CMD_SET_MPIN_LATER:
                            case Constants.CMD_DEBIT_CARD_INQUIRY:
                            case Constants.CMD_HRA_TO_WALLET_INFO:

                                parseLoginMsg(tbl);
                                break;

                            case Constants.CMD_CASH_WITHDRAWAL:
                            case Constants.CMD_MINI_LOAD_INFO:
                            case Constants.CMD_BILL_INQUIRY:
                            case Constants.CMD_BOOKME_INQUIRY:
                            case Constants.CMD_MY_LIMITS:
                            case Constants.CMD_RETAIL_PAYMENT_INFO:
                            case Constants.CMD_TRANSFER_IN_INFO:
                            case Constants.CMD_FUNDS_TRANSFER_BLB2BLB_INFO:
                            case Constants.CMD_FUNDS_TRANSFER_BLB2CNIC_INFO:
                            case Constants.CMD_FUNDS_TRANSFER_2CORE_INFO:
                            case Constants.CMD_TRANSFER_OUT_INFO:
                            case Constants.CMD_VERIFY_PIN:
                            case Constants.CMD_DEBIT_CARD_ISSUANCE_INFO:
                            case Constants.CMD_DEBIT_CARD_ACTIVATION_INFO:
                            case Constants.CMD_DEBIT_CARD_ACTIVATION:
                            case Constants.CMD_DEBIT_CARD_BLOCK_INFO:
                            case Constants.CMD_DEBIT_CARD_BLOCK:
                            case Constants.CMD_ATM_PIN_GENERATE_CHANGE_INFO:
                            case Constants.CMD_COLLECTION_INFO:
                            case Constants.CMD_INFO_L1_TO_HRA:
                            case Constants.CMD_L1_TO_HRA:
                            case Constants.CMD_TRANS_PURPOSE_CODE:
                            case Constants.CMD_GET_BANKS:
                            case Constants.CMD_ADVANCE_LOAN_INFO:
                            case Constants.CMD_REGENERATE_OTP:
                            case Constants.CMD_DEBIT_CARD_ACTIVATION_CHANGE_PIN:
                            case Constants.CMD_DEBIT_CARD_BLOCK_TYPE:
                                parseInfoMsg(tbl);
                                break;
                            case Constants.CMD_DEBIT_CARD_ISSUANCE:
                                parseTransMsgDebitCardIssuance(tbl);
                                break;
                            case Constants.CMD_MINI_STATMENT:
                                parseTransMsgMiniStatement(tbl);
                                break;
                            case Constants.CMD_BOOKME_PAYMENT:
                            case Constants.CMD_RETAIL_PAYMENT:
                                parseTransMsgRetailPayment(tbl);
                                break;
                            case Constants.CMD_FUNDS_TRANSFER_BLB2BLB:
                            case Constants.CMD_FUNDS_TRANSFER_BLB2CNIC:
                                parseTransMsgTransferBlb(tbl);
                                break;
                            case Constants.CMD_COLLECTION_PAYMENT:
                                parseTransMsgCollectionPayment(tbl);
                                break;
                            case Constants.CMD_FUNDS_TRANSFER_BLB2CORE:
                                parseTransMsgBlb2Core(tbl);
                                break;
                            case Constants.CMD_SCHEDULE_PAYMENT:
                                parseTransMsgBlb2Core(tbl);
                                break;

                            case Constants.CMD_RETAIL_PAYMENT_MPASS:
                                parseTransMsgRetailPaymentMPass(tbl);
                                break;


                            case Constants.CMD_MINI_LOAD:
                                parseTransMsgMiniLoad(tbl);
                                break;
                            case Constants.CMD_BILL_PAYMENT:
                                parseTransMsgBillPayment(tbl);
                                break;
                            case Constants.CMD_TRANSFER_IN:
                            case Constants.CMD_TRANSFER_OUT:
                            case Constants.CMD_HRA_TO_WALLET:
                            case Constants.CMD_ADVANCE_LOAN:
                                parseTransMsgFundsTransfer(tbl);
                                break;
                            case Constants.CMD_FAQS:
                                parseFaqsMsg(tbl);
                                break;
                            case Constants.CMD_LOCATOR:
                                parseLocatorMsg(tbl);
                                break;
                            default:
                                parseSimpleMsg(tbl);
                                break;
                        }
                    } else
                        return tbl;
                }
            } else {
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tbl;
    }

    public void parseLoginMsg(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        int eventType = parser.next();
        ArrayList<MessageModel> msgs = null;

        ArrayList<ServiceModel> serviceList = null;
        ArrayList<BankModel> banks = null;
        BankModel bank = null;
        ArrayList<MbankModel> mbanks = null;
        MbankModel mbank = null;
        ArrayList<TpurpsModel> mtrans = null;
        TpurpsModel mtran = null;
        ArrayList<BankAccountModel> bankAccNum = null;
        BankAccountModel bankAccNumber = null;

        ArrayList<TransactionModel> transList = null;
        TransactionModel trans = null;

        // catalog
        ArrayList<CategoryModel> listCategories = null;
        CategoryModel currCat = null;
        CategoryModel prevCat = null;
        CategoryModel parentCat = null;
        ProductModel product = null;

        ArrayList<CardTypeModel> listCardTypes = null;
        ArrayList<CardRankModel> listCardRanks = null;
        ArrayList<CardCategoryModel> listCardCategories = null;
        ArrayList<CardApplicantTypeModel> listCardApplicantTypes = null;
        ArrayList<CardStateModel> listCardStates = null;

        CardTypeModel cardType;
        CardRankModel cardRank;
        CardCategoryModel cardCategory;
        CardApplicantTypeModel cardApplicant;
        CardStateModel cardState;

        ArrayList<AdModel> listAds = null;
        AdModel adModel = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(XmlConstants.Tags.MBANKS)) {
                        mbanks = new ArrayList<MbankModel>(5);
                        table.put(Constants.KEY_LIST_USR_MBANK, mbanks);
                    } else if (startTag.equals(XmlConstants.Tags.MBANK) && mbanks != null) {
                        mbank = new MbankModel(
                                "",
                                parser.getAttributeValue(null, XmlConstants.Attributes.BANK_NAME),
                                parser.getAttributeValue(null, XmlConstants.Attributes.BANK_IMD),
                                parser.getAttributeValue(null, XmlConstants.Attributes.BANK_MIN_LEN),
                                parser.getAttributeValue(null, XmlConstants.Attributes.BANK_MAX_LEN));
                        mbanks.add(mbank);
                    } else if (startTag.equals(XmlConstants.Tags.TPURPS)) {
                        mtrans = new ArrayList<TpurpsModel>(2);
                        table.put(Constants.KEY_LIST_USR_TPURPS, mtrans);
                    } else if (startTag.equals(XmlConstants.Tags.TPURP) && mbanks != null) {
                        mtran = new TpurpsModel(
                                parser.getAttributeValue(null, XmlConstants.Attributes.BANK_ID),
                                parser.getAttributeValue(null, XmlConstants.Attributes.BANK_NAME));
                        mtrans.add(mtran);
                    } else if (startTag.equals(XmlConstants.Tags.BANKS)) {
                        banks = new ArrayList<BankModel>(5);
                        table.put(Constants.KEY_LIST_USR_BANK, banks);
                    } else if (startTag.equals(XmlConstants.Tags.BANK) && banks != null) {
                        boolean isBank = false;
                        if (parser.getAttributeValue(null,
                                XmlConstants.Attributes.ISBANK) != null
                                && parser.getAttributeValue(null,
                                XmlConstants.Attributes.ISBANK).equals(
                                Constants.TRUE)) {
                            isBank = true;
                        }

                        boolean pinLevel = false;
                        if (parser.getAttributeValue(null,
                                XmlConstants.Attributes.PIN_LEVEL) != null
                                && parser.getAttributeValue(null,
                                XmlConstants.Attributes.PIN_LEVEL).equals(
                                Constants.TRUE))
                            pinLevel = true;
                        bank = new BankModel(parser.
                                getAttributeValue(null,
                                XmlConstants.Attributes.BANK_ID),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.BANK_NICK),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.BANK_PGP_KEY),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.BANK_KEY_EXPO), isBank,
                                pinLevel);
                        banks.add(bank);

                        bankAccNum = new ArrayList<BankAccountModel>(5);
                        table.put(Constants.KEY_BANK_ACC_LIST, bankAccNum);
                    } else if (startTag.equals(XmlConstants.Tags.ACC)) {
                        boolean isDef = false;
                        if (parser.getAttributeValue(null,
                                XmlConstants.Attributes.ACC_IS_DEF) != null
                                && parser.getAttributeValue(null,
                                XmlConstants.Attributes.ACC_IS_DEF).equals(
                                Constants.TRUE))
                            isDef = true;
                        boolean pReq = false;
                        if (parser.getAttributeValue(null,
                                XmlConstants.Attributes.ACC_PN_CH_REQ) != null
                                && parser.getAttributeValue(null,
                                XmlConstants.Attributes.ACC_PN_CH_REQ).equals(
                                Constants.TRUE))
                            pReq = true;
                        boolean cvvPin = false;
                        if (parser.getAttributeValue(null,
                                XmlConstants.Attributes.CVV_PIN) != null
                                && parser.getAttributeValue(null,
                                XmlConstants.Attributes.CVV_PIN).equals(
                                Constants.TRUE))
                            cvvPin = true;
                        boolean tPin = false;
                        if (parser.getAttributeValue(null, XmlConstants.Attributes.TPIN) != null
                                && parser.getAttributeValue(null,
                                XmlConstants.Attributes.TPIN).equals(
                                Constants.TRUE))
                            tPin = true;

                        boolean isBPinReq = false;
                        if (parser.getAttributeValue(null,
                                XmlConstants.Attributes.IS_BANK_PIN_REQUIRED) != null
                                && parser.getAttributeValue(null,
                                XmlConstants.Attributes.IS_BANK_PIN_REQUIRED)
                                .equals(Constants.TRUE))
                            isBPinReq = true;
                        boolean mPin = false;
                        if (parser.getAttributeValue(null, XmlConstants.Attributes.MPIN) != null
                                && parser.getAttributeValue(null,
                                XmlConstants.Attributes.MPIN).equals(
                                Constants.TRUE))
                            mPin = true;

                        boolean status = false;
                        if (parser.getAttributeValue(null,
                                XmlConstants.Attributes.BANK_ACC_STATUS) != null
                                && parser.getAttributeValue(null,
                                XmlConstants.Attributes.BANK_ACC_STATUS).equals(
                                Constants.BANK_ACC_TRUE))
                            status = true;

                        bank.addAccounts(new BankAccountModel(
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.ACC_ID),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.BANK_ACC_NUMBER), parser
                                .getAttributeValue(null,
                                        XmlConstants.Attributes.BANK_ACC_TYPE),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.BANK_ACC_CURRENCY),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.ACC_NICK), status,
                                isDef, pReq, cvvPin, tPin, mPin, isBPinReq));

                    } else if (startTag.equals(XmlConstants.Tags.DC_DETAILS)) {
                        listCardTypes = new ArrayList<CardTypeModel>(5);
                        table.put(Constants.KEY_LIST_CARD_TYPES, listCardTypes);

                        listCardRanks = new ArrayList<CardRankModel>(5);
                        table.put(Constants.KEY_LIST_CARD_RANKS, listCardRanks);

                        listCardCategories = new ArrayList<CardCategoryModel>(5);
                        table.put(Constants.KEY_LIST_CARD_CATEGORIES,
                                listCardCategories);

                        listCardApplicantTypes = new ArrayList<CardApplicantTypeModel>(
                                5);
                        table.put(Constants.KEY_LIST_CARD_APPLICANT_TYPES,
                                listCardApplicantTypes);

                        listCardStates = new ArrayList<CardStateModel>(5);
                        table.put(Constants.KEY_LIST_CARD_STATES,
                                listCardStates);
                    } else if (startTag.equals(XmlConstants.Tags.DC_TYPE)) {
                        cardType = new CardTypeModel(parser.getAttributeValue(
                                null, XmlConstants.Attributes.PARAM_ID),
                                parser.nextText());
                        listCardTypes.add(cardType);
                    } else if (startTag.equals(XmlConstants.Tags.DC_CAT)) {
                        cardCategory = new CardCategoryModel(
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.PARAM_ID),
                                parser.nextText());
                        listCardCategories.add(cardCategory);
                    } else if (startTag.equals(XmlConstants.Tags.DC_RANK)) {
                        cardRank = new CardRankModel(parser.getAttributeValue(
                                null, XmlConstants.Attributes.PARAM_ID),
                                parser.nextText());
                        listCardRanks.add(cardRank);
                    } else if (startTag.equals(XmlConstants.Tags.DC_APPTYPE)) {
                        cardApplicant = new CardApplicantTypeModel(
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.PARAM_ID),
                                parser.nextText());
                        listCardApplicantTypes.add(cardApplicant);
                    } else if (startTag.equals(XmlConstants.Tags.DC_STATE)) {
                        cardState = new CardStateModel(
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.PARAM_ID),
                                parser.nextText());
                        listCardStates.add(cardState);
                    }
                    /*
                     * Catalog parsing
                     */
                    else if (startTag.equals(XmlConstants.Tags.CAT)) {
                        table.put(Constants.KEY_CAT_VER,
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.CAT_VER));
                    } else if (startTag.equals(XmlConstants.Tags.Categories)) {
                        listCategories = new ArrayList<CategoryModel>();
                        currCat = prevCat = null;
                    } else if (startTag.equals(XmlConstants.Tags.Cagtegory)) {
                        // System.out.println("##category");
                        prevCat = currCat;
                        currCat = new CategoryModel(parser.getAttributeValue(
                                null, XmlConstants.Attributes.CID),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.PARAM_NAME),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.ICON),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.ISPRODUCT), prevCat);
                        if (prevCat != null) {
                            prevCat.addCategory(currCat);
                        }
                    } else if (startTag.equals(XmlConstants.Tags.PRD)) {
                        // System.out.println("##product");
                        product = new ProductModel(parser.getAttributeValue(
                                null, XmlConstants.Attributes.BANK_ID),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.FID), "");
                        product.setLabel(parser.getAttributeValue(null,
                                XmlConstants.Attributes.SERV_LABEL));
                        product.setMinamt(parser.getAttributeValue(null,
                                XmlConstants.Attributes.MINAMT));
                        product.setMinamtf(parser.getAttributeValue(null,
                                XmlConstants.Attributes.MINAMTF));
                        product.setMaxamt(parser.getAttributeValue(null,
                                XmlConstants.Attributes.MAXAMT));
                        product.setMaxamtf(parser.getAttributeValue(null,
                                XmlConstants.Attributes.MAXAMTF));
                        product.setAmtRequired(parser.getAttributeValue(null,
                                XmlConstants.Attributes.AMT_REQUIRED));
                        product.setDoValidate(parser.getAttributeValue(null,
                                XmlConstants.Attributes.DO_VALIDATE));
                        product.setType(parser.getAttributeValue(null,
                                XmlConstants.Attributes.TRN_TYPE));
                        product.setMultiple(parser.getAttributeValue(null,
                                XmlConstants.Attributes.TRN_MUTIPLE));
                        product.setMinConsumerLength(parser.getAttributeValue(null,
                                XmlConstants.Attributes.MIN_CONSUMER_LENGTH));
                        product.setMaxConsumerLength(parser.getAttributeValue(null,
                                XmlConstants.Attributes.MAX_CONSUMER_LENGTH));
                        product.setName(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PARAM_NAME));
                        product.setInRequired(parser.getAttributeValue(null,
                                XmlConstants.Attributes.IN_REQUIRED));
                        product.setPpRequired(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PP_REQUIRED));
                        product.setProdDenom(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PROD_DENOM));
                        product.setDenomFlag(parser.getAttributeValue(null,
                                XmlConstants.Attributes.DENOM_FLAG));
                        product.setDenomString(parser.getAttributeValue(null,
                                XmlConstants.Attributes.DENOM_STRING));
                        product.setURL(parser.getAttributeValue(null,
                                XmlConstants.Attributes.URL));

                        AppLogger.i("Name: " + product.getName());

                        currCat.addProduct(product);
                    }
                    // catalog ends

                    if (startTag.equals(XmlConstants.Tags.TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(XmlConstants.Tags.TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, XmlConstants.Attributes.CODE),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_MOB_NO),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.CUS_CODE),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_DATE),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_TYPE),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_PROD),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_AMT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_AMT_FORMAT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_AUTH_CODE),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_SUPP),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_PAY_MODE),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_BILL_SUPP_HELPLINE),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TPAM),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TPAM_FORMAT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TAMT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TAMT_FORMAT));

                        transList.add(trans);
                    } else if (startTag.equals(XmlConstants.Tags.TAG_ADS)) {
                        listAds = new ArrayList<AdModel>();
                    } else if (startTag.equals(XmlConstants.Tags.TAG_AD)) {
                        adModel = new AdModel();
                        adModel.setImageUrl((parser.getAttributeValue(null,
                                XmlConstants.Attributes.ATTR_NAME)));
                        adModel.setType(parser.getAttributeValue(null,
                                XmlConstants.Attributes.ATTR_TYPE));
                        listAds.add(adModel);
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser.getAttributeValue(null, XmlConstants.Attributes.LEVEL), parser.nextText()));
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                //	AppLogger.i("End Tag: " + endTag);
                if (endTag.equals(XmlConstants.Tags.Cagtegory)) {

                    parentCat = currCat.getParentCategory();
                    if (parentCat == null) {
                        listCategories.add(currCat);
                        currCat = null;
                    } else {
                        currCat = parentCat;
                        prevCat = currCat.getParentCategory();
                    }
                }
//Todo
                if (endTag.equals(XmlConstants.Tags.Categories)) {
                    table.put(Constants.KEY_LIST_CATALOG, listCategories);
                }

                if (endTag.equals(XmlConstants.Tags.TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                }

                if (endTag.equals(XmlConstants.Tags.TAG_ADS)) {
                    table.put(XmlConstants.KEY_LIST_ADS, listAds);
                }

                if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                } else if (endTag.equals(XmlConstants.Tags.MSG)) {
                }
            }
            eventType = parser.next();
        }

        serviceList = null;
        banks = null;
        msgs = null;
    }

    public ArrayList<CategoryModel> parseLocalCatelog(String xml)
            throws IOException, XmlPullParserException {

        parser.setInput(new InputStreamReader(new ByteArrayInputStream(xml.getBytes())));
        int eventType = parser.next();

        ArrayList<CategoryModel> listCategories = null;
        CategoryModel currCat = null;
        CategoryModel prevCat = null;
        CategoryModel parentCat = null;
        SupplierModel supplier = null;
        ProductModel product = null;

        listCategories = new ArrayList<CategoryModel>();
        // table.put(Constants.KEY_LIST_CATEGORIES,
        // listCategories);
        currCat = prevCat = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();
                    if (startTag.equals(XmlConstants.Tags.Cagtegory)) {
                        prevCat = currCat;
                        currCat = new CategoryModel(
                                parser.getAttributeValue(null, XmlConstants.Attributes.CID),
                                parser.getAttributeValue(null, XmlConstants.Attributes.PARAM_NAME),
                                parser.getAttributeValue(null, XmlConstants.Attributes.ICON),
                                parser.getAttributeValue(null, XmlConstants.Attributes.ISPRODUCT),
                                prevCat);
                        if (prevCat != null) {
                            prevCat.addCategory(currCat);
                        }
                    } else if (startTag.equals(XmlConstants.Tags.PRD)) {
                        product = new ProductModel(
                                parser.getAttributeValue(null, XmlConstants.Attributes.BANK_ID),
                                parser.getAttributeValue(null, XmlConstants.Attributes.FID),
                                "");
                        product.setLabel(parser.getAttributeValue(null, XmlConstants.Attributes.SERV_LABEL));
                        product.setMinamt(parser.getAttributeValue(null, XmlConstants.Attributes.MINAMT));
                        product.setMinamtf(parser.getAttributeValue(null, XmlConstants.Attributes.MINAMTF));
                        product.setMaxamt(parser.getAttributeValue(null, XmlConstants.Attributes.MAXAMT));
                        product.setMaxamtf(parser.getAttributeValue(null, XmlConstants.Attributes.MAXAMTF));
                        product.setAmtRequired(parser.getAttributeValue(null, XmlConstants.Attributes.AMT_REQUIRED));
                        product.setDoValidate(parser.getAttributeValue(null, XmlConstants.Attributes.DO_VALIDATE));
                        product.setType(parser.getAttributeValue(null, XmlConstants.Attributes.TRN_TYPE));
                        product.setMultiple(parser.getAttributeValue(null, XmlConstants.Attributes.TRN_MUTIPLE));
                        product.setMinConsumerLength(parser.getAttributeValue(null, XmlConstants.Attributes.MIN_CONSUMER_LENGTH));
                        product.setMaxConsumerLength(parser.getAttributeValue(null, XmlConstants.Attributes.MAX_CONSUMER_LENGTH));
                        product.setName(parser.getAttributeValue(null, XmlConstants.Attributes.PARAM_NAME));
                        product.setInRequired(parser.getAttributeValue(null, XmlConstants.Attributes.IN_REQUIRED));
                        product.setPpRequired(parser.getAttributeValue(null, XmlConstants.Attributes.PP_REQUIRED));
                        product.setProdDenom(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PROD_DENOM));
                        product.setDenomFlag(parser.getAttributeValue(null,
                                XmlConstants.Attributes.DENOM_FLAG));
                        product.setDenomString(parser.getAttributeValue(null,
                                XmlConstants.Attributes.DENOM_STRING));
                        product.setURL(parser.getAttributeValue(null, XmlConstants.Attributes.URL));
                        currCat.addProduct(product);
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.Cagtegory)) {
                    parentCat = currCat.getParentCategory();
                    if (parentCat == null) {
                        listCategories.add(currCat);
                        currCat = null;
                    } else {
                        currCat = parentCat;
                        prevCat = currCat.getParentCategory();
                    }
                } else if (endTag.equals(XmlConstants.Tags.Categories)) {
                    // System.out.println("##/>categories");
                    // table.put(Constants.KEY_LIST_CATALOG, listCategories);
                }

            }
            eventType = parser.next();
        }
        return listCategories;
    }

    public void parseInfoMsg(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Param Message");
        int eventType = parser.next();
        ArrayList<MessageModel> msgs = null;
        ArrayList<PaymentReasonModel> paymentReasons = null;
        ArrayList<MbankModel> banks = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();
                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null, XmlConstants.Attributes.PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser.getAttributeValue(null, XmlConstants.Attributes.LEVEL), parser.nextText()));
                    } else if (startTag.equals(Constants.TAG_PAYMENT_REASONS)) {
                        paymentReasons = new ArrayList<PaymentReasonModel>();
                    } else if (startTag.equals(Constants.TAG_PAYMENT_REASON)) {
                        paymentReasons.add(new PaymentReasonModel(parser.getAttributeValue(
                                null, Constants.ATTR_PARAM_CODE), parser.getAttributeValue(
                                null, Constants.ATTR_PARAM_NAME)));
                    } else if (startTag.equals(Constants.TAG_MEMBERS_BANKS)) {
                        banks = new ArrayList<>();
                    } else if (startTag.equals(Constants.TAG_BANK)) {
                        banks.add(new MbankModel(
                                "",
                                parser.getAttributeValue(null, Constants.ATTR_PARAM_NAME),
                                parser.getAttributeValue(null, Constants.ATTR_PARAM_IMD),
                                parser.getAttributeValue(null, Constants.ATTR_MIN_LENGTH),
                                parser.getAttributeValue(null, Constants.ATTR_MAX_LENGTH)));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                } else if (endTag.equals(Constants.TAG_PAYMENT_REASONS)) {
                    table.put(Constants.KEY_LIST_PAYMENT_REASONS, paymentReasons);
                } else if (endTag.equals(Constants.TAG_MEMBERS_BANKS)) {
                    table.put(Constants.KEY_LIST_MEMBERS_BANKS, banks);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgMiniStatement(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Transaction Message");
        int eventType = parser.next();
        ArrayList<TransactionModel> transList = null;
        ArrayList<MessageModel> msgs = null;

        TransactionModel trans = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(XmlConstants.Tags.TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(XmlConstants.Tags.TRN)) {
                        trans = new TransactionModel(
                                parser.getAttributeValue(null, XmlConstants.Attributes.DATE),
                                parser.getAttributeValue(null, XmlConstants.Attributes.DESCRIPTION),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_DATE_FORMAT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TAMT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TAMTF));
                        transList.add(trans);
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, XmlConstants.Attributes.LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgRetailPayment(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Transaction Message");
        int eventType = parser.next();
        ArrayList<TransactionModel> transList = null;
        ArrayList<MessageModel> msgs = null;

        TransactionModel trans = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(XmlConstants.Tags.TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(XmlConstants.Tags.TRN)) {
                        trans = new TransactionModel(
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_DATE),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_DATE_FORMAT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_TIME_FORMAT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRXID),
                                parser.getAttributeValue(null, XmlConstants.Attributes.AMOB),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_PROD),
                                parser.getAttributeValue(null, XmlConstants.Attributes.CAMT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.CAMTF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TPAM),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TPAMF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TAMT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TAMTF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.BALF));
                        trans.setRaname(parser.getAttributeValue(null, XmlConstants.Attributes.RANAME));
                        trans.setTxam(parser.getAttributeValue(null, XmlConstants.Attributes.TXAM));
                        trans.setTxamf(parser.getAttributeValue(null, XmlConstants.Attributes.TXAMF));
                        transList.add(trans);
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, XmlConstants.Attributes.LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }


    public void parseTransMsgRetailPaymentMPass(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Transaction Message");
        int eventType = parser.next();
        ArrayList<TransactionModel> transList = null;
        ArrayList<MessageModel> msgs = null;

        TransactionModel trans = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(XmlConstants.Tags.TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(XmlConstants.Tags.TRN)) {
                        trans = new TransactionModel();
                        trans.setId(parser.getAttributeValue(null, XmlConstants.Attributes.TRXID));
                        trans.setDate(parser.getAttributeValue(null, XmlConstants.Attributes.TRN_DATE));
                        trans.setDatef(parser.getAttributeValue(null, XmlConstants.Attributes.TRN_DATE_FORMAT));

                        trans.setTimef(parser.getAttributeValue(null, XmlConstants.Attributes.TRN_TIME_FORMAT));
                        trans.setProd(parser.getAttributeValue(null, XmlConstants.Attributes.TRN_PROD));
                        trans.setProductName(parser.getAttributeValue(null, XmlConstants.Attributes.MNAME));

                        trans.setTxam(parser.getAttributeValue(null, XmlConstants.Attributes.TXAM));
                        trans.setTxamf(parser.getAttributeValue(null, XmlConstants.Attributes.TXAMF));

                        transList.add(trans);
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, XmlConstants.Attributes.LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }
    public void parseSchedulePayment(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Transaction Message");
        int eventType = parser.next();
        ArrayList<TransactionModel> transList = null;
        ArrayList<MessageModel> msgs = null;

        TransactionModel trans = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(XmlConstants.Tags.TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(XmlConstants.Tags.TRN)) {
                        trans = new TransactionModel();
                        trans.setCoreacid(parser.getAttributeValue(null,
                                XmlConstants.Attributes.COREACID));
                        trans.swcnic = parser.getAttributeValue(null,
                                XmlConstants.Attributes.SWCNIC);
                        trans.setCoreactl(parser.getAttributeValue(null,
                                XmlConstants.Attributes.COREACTL));
                        trans.setTxam(parser.getAttributeValue(null,
                                XmlConstants.Attributes.TXAM));
                        trans.setTxamf(parser.getAttributeValue(null,
                                XmlConstants.Attributes.TXAMF));
                        trans.setBalf(parser.getAttributeValue(null,
                                XmlConstants.Attributes.BALF));
                        trans.setCoreactl(parser.getAttributeValue(null,
                                XmlConstants.Attributes.COREACTL));
                        trans.setRcmob(parser.getAttributeValue(null,
                                XmlConstants.Attributes.RCMOB));
                        transList.add(trans);
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, XmlConstants.Attributes.LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgBlb2Core(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Transaction Message");
        int eventType = parser.next();
        ArrayList<TransactionModel> transList = null;
        ArrayList<MessageModel> msgs = null;

        TransactionModel trans = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(XmlConstants.Tags.TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(XmlConstants.Tags.TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, XmlConstants.Attributes.TRN_DATE),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRXID),
                                null,
                                "",
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.CAMT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.CAMTF),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TPAM),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TPAMF),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TAMT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TAMTF),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.BALF));
                        trans.setCoreacid(parser.getAttributeValue(null,
                                XmlConstants.Attributes.COREACID));
                        trans.swcnic = parser.getAttributeValue(null,
                                XmlConstants.Attributes.SWCNIC);
                        trans.setCoreactl(parser.getAttributeValue(null,
                                XmlConstants.Attributes.COREACTL));
                        trans.setTxam(parser.getAttributeValue(null,
                                XmlConstants.Attributes.TXAM));
                        trans.setTxamf(parser.getAttributeValue(null,
                                XmlConstants.Attributes.TXAMF));
                        trans.setBalf(parser.getAttributeValue(null,
                                XmlConstants.Attributes.BALF));
                        trans.setCoreactl(parser.getAttributeValue(null,
                                XmlConstants.Attributes.COREACTL));
                        trans.setRcmob(parser.getAttributeValue(null,
                                XmlConstants.Attributes.RCMOB));
                        transList.add(trans);
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, XmlConstants.Attributes.LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgMiniLoad(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Bill Payment Transaction Message");
        int eventType = parser.next();
        ArrayList<TransactionModel> transList = null;
        ArrayList<MessageModel> msgs = null;

        TransactionModel trans = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(XmlConstants.Tags.TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(XmlConstants.Tags.TRN)) {
                        trans = new TransactionModel();
                        trans.setMiniLoadTransaction(
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRXID),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_PROD),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TMOB),
                                parser.getAttributeValue(null, XmlConstants.Attributes.AMOB),
                                parser.getAttributeValue(null, XmlConstants.Attributes.DATE),
                                parser.getAttributeValue(null, XmlConstants.Attributes.DATEF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_TIME_FORMAT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TXAM),
                                parser.getAttributeValue(null, XmlConstants.Attributes.CAMT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.CAMTF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TPAM),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TPAM_FORMAT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TAMT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TAMT_FORMAT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.BALF)
                        );
                        trans.setProductName(parser.getAttributeValue(null, XmlConstants.Attributes.PNAME));
                        trans.setProd(parser.getAttributeValue(null, XmlConstants.Attributes.TRN_PROD));
                        trans.setConsumer(parser.getAttributeValue(null, XmlConstants.Attributes.CONSUMER));
                        trans.setBalf(parser.getAttributeValue(null, XmlConstants.Attributes.BALF));

                        transList.add(trans);
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, XmlConstants.Attributes.LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgBillPayment(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Bill Payment Transaction Message");
        int eventType = parser.next();
        ArrayList<TransactionModel> transList = null;
        ArrayList<MessageModel> msgs = null;

        TransactionModel trans = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(XmlConstants.Tags.TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(XmlConstants.Tags.TRN)) {
                        trans = new TransactionModel();
                        trans.setBillPaymentTransaction(
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_DATE),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_DATE_FORMAT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_TIME_FORMAT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRXID),
                                parser.getAttributeValue(null, XmlConstants.Attributes.CMOB),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_PROD),
                                parser.getAttributeValue(null, XmlConstants.Attributes.CAMT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.CAMTF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TPAM),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TPAMF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TAMT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TAMTF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.BALF));
                        trans.setProductName(parser.getAttributeValue(null, XmlConstants.Attributes.PNAME));
                        trans.setProd(parser.getAttributeValue(null, XmlConstants.Attributes.TRN_PROD));
                        trans.setConsumer(parser.getAttributeValue(null, XmlConstants.Attributes.CONSUMER));
                        trans.setBalf(parser.getAttributeValue(null, XmlConstants.Attributes.BALF));

                        transList.add(trans);
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, XmlConstants.Attributes.LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgCollectionPayment(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Bill Payment Transaction Message");
        int eventType = parser.next();
        ArrayList<TransactionModel> transList = null;
        ArrayList<MessageModel> msgs = null;

        TransactionModel trans = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(XmlConstants.Tags.TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(XmlConstants.Tags.TRN)) {
                        trans = new TransactionModel();
                        trans.setCollectionPaymentTransaction(
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRXID),
                                parser.getAttributeValue(null, XmlConstants.Attributes.PID),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_PROD),
                                parser.getAttributeValue(null, XmlConstants.Attributes.CNSMRNO),
                                parser.getAttributeValue(null, XmlConstants.Attributes.BAMT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.BAMTF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.CAMT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.CAMTF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TAMT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TAMTF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.CMOB),
                                "",
                                parser.getAttributeValue(null, XmlConstants.Attributes.DATEF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.BALF));


                        transList.add(trans);
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, XmlConstants.Attributes.LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }


    public void parseTransMsgTransferBlb(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Transaction Message");
        int eventType = parser.next();
        ArrayList<TransactionModel> transList = null;
        ArrayList<MessageModel> msgs = null;

        TransactionModel trans = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(XmlConstants.Tags.TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(XmlConstants.Tags.TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, XmlConstants.Attributes.TRN_DATE),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRXID),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.CMOB),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_PROD),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.CAMT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.CAMTF),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TPAM),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TPAMF),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TAMT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TAMTF),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.BALF));
                        trans.setRcmob(parser.getAttributeValue(null,
                                XmlConstants.Attributes.RCMOB));
                        trans.setSwcnic(parser.getAttributeValue(null,
                                XmlConstants.Attributes.SWCNIC));
                        trans.setSwmob(parser.getAttributeValue(null,
                                XmlConstants.Attributes.SWMOB));
                        trans.setRwmob(parser.getAttributeValue(null,
                                XmlConstants.Attributes.RWMOB));
                        trans.setRwcnic(parser.getAttributeValue(null,
                                XmlConstants.Attributes.RWCNIC));
                        trans.setTxam(parser.getAttributeValue(null,
                                XmlConstants.Attributes.TXAM));
                        trans.setTxamf(parser.getAttributeValue(null,
                                XmlConstants.Attributes.TXAMF));
                        trans.setBalf(parser.getAttributeValue(null,
                                XmlConstants.Attributes.BALF));

                        transList.add(trans);
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, XmlConstants.Attributes.LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgFundsTransfer(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Transaction Message");
        int eventType = parser.next();
        ArrayList<TransactionModel> transList = null;
        ArrayList<MessageModel> msgs = null;

        TransactionModel trans = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(XmlConstants.Tags.TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(XmlConstants.Tags.TRN)) {
                        trans = new TransactionModel(
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_DATE),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_DATE_FORMAT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_TIME_FORMAT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRXID),
                                parser.getAttributeValue(null, XmlConstants.Attributes.AMOB),
                                parser.getAttributeValue(null, XmlConstants.Attributes.BBACID),
                                parser.getAttributeValue(null, XmlConstants.Attributes.COREACID),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TRN_PROD),
                                parser.getAttributeValue(null, XmlConstants.Attributes.CAMT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.CAMTF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TPAM),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TPAMF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TAMT),
                                parser.getAttributeValue(null, XmlConstants.Attributes.TAMTF),
                                parser.getAttributeValue(null, XmlConstants.Attributes.BALF));

                        trans.setTxam(parser.getAttributeValue(null,
                                XmlConstants.Attributes.TXAM));
                        trans.setTxamf(parser.getAttributeValue(null,
                                XmlConstants.Attributes.TXAMF));

                        trans.setCoreactl(parser.getAttributeValue(null,
                                XmlConstants.Attributes.COREACTL));

                        transList.add(trans);
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, XmlConstants.Attributes.LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseRegistrationMessage1(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        int eventType = parser.next();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();
                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null, XmlConstants.Attributes.PARAM_NAME), parser.nextText());
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
            }
            eventType = parser.next();
        }
    }

    public void parseSimpleMsg(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Simple Message!!");
        int eventType = parser.next();
        ArrayList<MessageModel> errors = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();
                    if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        errors = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        errors.add(new MessageModel(
                                parser.getAttributeValue(null, XmlConstants.Attributes.CODE),
                                parser.getAttributeValue(null, XmlConstants.Attributes.LEVEL),
                                parser.nextText()));
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, errors);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseErrorMsg(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        int eventType = parser.next();
        ArrayList<MessageModel> errors = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();
                    if (startTag.equals(XmlConstants.Tags.ERRORS)) {
                        errors = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.ERROR)) {
                        errors.add(new MessageModel(parser.getAttributeValue(
                                null, XmlConstants.Attributes.CODE), parser
                                .getAttributeValue(null, XmlConstants.Attributes.LEVEL),
                                parser.nextText()));
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.ERRORS)) {
                    table.put(Constants.KEY_LIST_ERRORS, errors);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgDebitCardIssuance(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Debit Card Issuance Transaction Message");
        int eventType = parser.next();
        ArrayList<TransactionModel> transList = null;
        ArrayList<MessageModel> msgs = null;

        TransactionModel trans = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(XmlConstants.Tags.TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(XmlConstants.Tags.TRN)) {
                        trans = new TransactionModel();
                        trans.setDebitCardIssunaceTransaction(parser
                                        .getAttributeValue(null, XmlConstants.Attributes.TRXID),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_PROD), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.ACTITLE), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.BNAME), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.TXAM), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.TXAMF), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.CAMT), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.CAMTF), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.TPAM), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.TPAMF), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.TAMT), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.TAMTF),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.TRN_DATE_FORMAT), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.BALF), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.ICA), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.CNAME), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.CTNAME), parser
                                        .getAttributeValue(null,
                                                XmlConstants.Attributes.MOBN));
                        transList.add(trans);
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, XmlConstants.Attributes.LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    private void parseFaqsMsg(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Faqs Message");

        ArrayList<MessageModel> msgs = null;
        ArrayList<FaqsModel> listFaqs = null;
        FaqsModel faq = null;

        int eventType = parser.next();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.FAQS)) {
                        listFaqs = new ArrayList<FaqsModel>();
                        ApplicationData.faqVersion = parser.getAttributeValue(
                                null, XmlConstants.Attributes.FAQS_VERSION);
                    } else if (startTag.equals(XmlConstants.Tags.FAQ)) {
                        faq = new FaqsModel(parser.getAttributeValue(null,
                                XmlConstants.Attributes.FAQS_ID),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.FAQS_QUESTION),
                                parser.nextText());

                        listFaqs.add(faq);
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null,
                                        XmlConstants.Attributes.LEVEL), parser
                                .nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();

                if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
                if (endTag.equals(XmlConstants.Tags.FAQS)) {
                    table.put(Constants.KEY_LIST_FAQS, listFaqs);
                }
            }
            eventType = parser.next();
        }

    }

    public ArrayList<FaqsModel> parseLocalFaqs(String xml) throws IOException,
            XmlPullParserException {

        parser.setInput(new InputStreamReader(new ByteArrayInputStream(xml
                .getBytes())));
        int eventType = parser.next();
        ArrayList<FaqsModel> listFaqs = null;
        FaqsModel faq = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.FAQS)) {
                        listFaqs = new ArrayList<FaqsModel>();
                    } else if (startTag.equals(XmlConstants.Tags.FAQ)) {

                        faq = new FaqsModel(parser.getAttributeValue(null,
                                XmlConstants.Attributes.FAQS_ID),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.FAQS_QUESTION),
                                parser.nextText());

                        listFaqs.add(faq);
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(XmlConstants.Tags.FAQS)) {
                }
            }

            eventType = parser.next();
        }

        return listFaqs;
    }

    private void parseLocatorMsg(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing Locator Message");

        ArrayList<MessageModel> msgs = null;
        ArrayList<LocationModel> listLocations = null;
        LocationModel location = null;

        int eventType = parser.next();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(XmlConstants.Tags.PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                XmlConstants.Attributes.PARAM_NAME), parser.nextText());

                    } else if (startTag.equals(XmlConstants.Tags.LOCATIONS)) {
                        listLocations = new ArrayList<LocationModel>();
                    } else if (startTag.equals(XmlConstants.Tags.LOCATION)) {
                        location = new LocationModel(parser.getAttributeValue(
                                null, XmlConstants.Attributes.LOCATION_NAME),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.DISTANCE),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.CONTACT),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.ADD),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.LATITUDE),
                                parser.getAttributeValue(null,
                                        XmlConstants.Attributes.LONGITUDE));

                        listLocations.add(location);
                    } else if (startTag.equals(XmlConstants.Tags.MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(XmlConstants.Tags.MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null,
                                        XmlConstants.Attributes.LEVEL), parser
                                .nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();

                if (endTag.equals(XmlConstants.Tags.MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
                if (endTag.equals(XmlConstants.Tags.LOCATIONS)) {
                    table.put(Constants.KEY_LIST_LOCATIONS, listLocations);
                }
            }
            eventType = parser.next();
        }
    }
}