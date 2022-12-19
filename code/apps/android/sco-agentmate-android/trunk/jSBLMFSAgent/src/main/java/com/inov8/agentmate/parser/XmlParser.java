package com.inov8.agentmate.parser;

import android.widget.ArrayAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.inov8.agentmate.model.BankAccountModel;
import com.inov8.agentmate.model.BankModel;
import com.inov8.agentmate.model.CategoryModel;
import com.inov8.agentmate.model.CitiesModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.PaymentReasonModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.ServiceModel;
import com.inov8.agentmate.model.SupplierModel;
import com.inov8.agentmate.model.TransactionModel;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.XmlConstants;

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
                    if (startTag.equals(Constants.TAG_MSG)) {
                        msgType = parser.getAttributeValue(null,
                                Constants.ATTR_MSG_ID);

                        if (msgType == null || msgType.equals(""))
                            return tbl;

                        tbl = new Hashtable<String, Object>();
                        tbl.put(Constants.KEY_CMDRCVD, msgType.trim());

                        int commandReceived = Integer.parseInt(msgType);
                        switch (commandReceived) {
                            case Constants.RSP_ERROR:
                                parseErrorMsg(tbl);
                                break;

                            case Constants.CMD_LOGIN:
                            case Constants.CMD_CHECK_BALANCE:
                                parseLoginMsg(tbl);
                                break;

                            case Constants.CMD_BILL_INQUIRY:
                            case Constants.CMD_RETAIL_PAYMENT_INFO:
                            case Constants.CMD_TRANSFER_IN_INFO:
                            case Constants.CMD_FUNDS_TRANSFER_BLB2BLB_INFO:
                            case Constants.CMD_FUNDS_TRANSFER_BLB2CNIC_INFO:
                            case Constants.CMD_FUNDS_TRANSFER_CNIC2BLB_INFO:
                            case Constants.CMD_FUNDS_TRANSFER_CNIC2CNIC_INFO:
                            case Constants.CMD_FUNDS_TRANSFER_2CORE_INFO:
                            case Constants.CMD_TRANSFER_OUT_INFO:
                            case Constants.CMD_CASH_IN_INFO:
                            case Constants.CMD_CASH_OUT_INFO:
                            case Constants.CMD_CASH_OUT_BY_TRX_ID_INFO:
                            case Constants.CMD_AGENT_TO_AGENT_TRANSFER_INFO:
                            case Constants.CMD_VERIFY_PIN:
                            case Constants.CMD_RECEIVE_MONEY_SENDER_REDEEM_INFO:
                            case Constants.CMD_RECEIVE_MONEY_RECEIVE_CASH_INFO:
                            case Constants.CMD_OPEN_ACCOUNT_OTP_VERIFICATION:
                            case Constants.CMD_COLLECTION_PAYMENT_INFO:
                            case Constants.CMD_L1_TO_HRA:
                            case Constants.CMD_INFO_L1_TO_HRA:
                            case Constants.CMD_IBFT_AGENT:
                            case Constants.CMD_BANKS:
                            case Constants.CMD_3RD_PARTY_CASH_OUT_INFO:
                            case Constants.CMD_DEBIT_CARD:
                            case Constants.CMD_DEBIT_CARD_CONFIRMATION:
                            case Constants.CMD_TRANS_PURPOSE_CODE:
                            case Constants.CMD_CHECK_BVS:
                            case Constants.CMD_3RD_PARTY_AGENT_BVS:
                                parseInfoMsg(tbl);
                                break;
                            case Constants.CMD_MINI_STATMENT:
                                parseTransMsgMiniStatement(tbl);
                                break;
                            case Constants.CMD_RETAIL_PAYMENT:
                                parseTransMsgRetailPayment(tbl);
                                break;
                            case Constants.CMD_FUNDS_TRANSFER_BLB2BLB:
                            case Constants.CMD_FUNDS_TRANSFER_BLB2CNIC:
                            case Constants.CMD_FUNDS_TRANSFER_CNIC2BLB:
                            case Constants.CMD_FUNDS_TRANSFER_CNIC2CNIC:
                                parseTransMsgTransferBlb(tbl);
                                break;
                            case Constants.CMD_FUNDS_TRANSFER_BLB2CORE:
                                parseTransMsgBlb2Core(tbl);
                                break;
                            case Constants.CMD_FUNDS_TRANSFER_CNIC2CORE:
                                parseTransMsgCnic2Core(tbl);
                                break;
                            case Constants.CMD_CASH_OUT:
                            case Constants.CMD_HRA_CASH_WITHDRAWAL:
                                parseTransMsgCashOut(tbl);
                                break;
                            case Constants.CMD_3RD_PARTY_CASH_OUT:
                                parseTransMsg3rdPartyCashOut(tbl);
                                break;
                            case Constants.CMD_CASH_OUT_BY_TRX_ID:
                                parseTransMsgCashOutByTrxId(tbl);
                                break;
                            case Constants.CMD_CASH_IN:
                                parseTransMsgCashIn(tbl);
                                break;
                            case Constants.CMD_IBFT_AGENT_CONFORMATION:
                                parseTransIBFT(tbl);
                                break;
                            case Constants.CMD_AGENT_TO_AGENT_TRANSFER:
                                parseTransMsgAgentToAgent(tbl);
                                break;
                            case Constants.CMD_BILL_PAYMENT:
                                parseTransMsgBillPayment(tbl);
                                break;
                            case Constants.CMD_COLLECTION_PAYMENT_TRX:
                                parseTransMsgCollectionPayment(tbl);
                                break;
                            case Constants.CMD_TRANSFER_IN:
                            case Constants.CMD_TRANSFER_OUT:
                                parseTransMsgFundsTransfer(tbl);
                                break;
                            case Constants.CMD_OPEN_ACCOUNT_VERIFY_CUSTOMER_REGISTRATION:
                                parseAccountOpenVerifyMSISDNandCNICTransMsg(tbl);
                                break;
                            case Constants.CMD_RECEIVE_MONEY_SENDER_REDEEM_PAYMENT:
                                parseReceiveMoneySenderRedeemPayment(tbl);
                                break;
                            case Constants.CMD_RECEIVE_MONEY_PENDING_TRX_PAYMENT:
                                parseReceiveMoneyPendingTrxPayment(tbl);
                                break;
                            case Constants.CMD_RECEIVE_MONEY_RECEIVE_CASH:
                                parseReceiveMoneyReceiveCash(tbl);
                                break;
                            case Constants.CMD_OPEN_ACCOUNT_NADRA_VERIFICATION:
                                parseAccountOpenVerifyMSISDNandCNICTransMsg(tbl);
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

        ArrayList<BankAccountModel> bankAccNum = null;
        BankAccountModel bankAccNumber = null;

        ArrayList<TransactionModel> transList = null;
        TransactionModel trans = null;
        // catalog
        ArrayList<CategoryModel> listCategories = null;
        CategoryModel currCat = null;
        CategoryModel prevCat = null;
        CategoryModel parentCat = null;
        SupplierModel supplier = null;
        ProductModel product = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(Constants.TAG_PARAM)) {

                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_BANKS)) {
                        banks = new ArrayList<BankModel>(5);
                        table.put(Constants.KEY_LIST_USR_BANK, banks);
                    } else if (startTag.equals(Constants.TAG_BANK)
                            && banks != null) {
                        boolean isBank = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_ISBANK) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_ISBANK).equals(
                                Constants.TRUE)) {
                            isBank = true;
                        }

                        boolean pinLevel = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_PIN_LEVEL) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_PIN_LEVEL).equals(
                                Constants.TRUE))
                            pinLevel = true;
                        bank = new BankModel(parser.getAttributeValue(null,
                                Constants.ATTR_BANK_ID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BANK_NICK),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BANK_PGP_KEY),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BANK_KEY_EXPO), isBank,
                                pinLevel);
                        banks.add(bank);

                        bankAccNum = new ArrayList<BankAccountModel>(5);
                        table.put(Constants.KEY_BANK_ACC_LIST, bankAccNum);
                    }
                    // else if(startTag.equals(Constants.TAG_BANK_ACCS))
                    // {
                    // bankAccNum = new ArrayList(5);
                    // table.put(Constants.KEY_BANK_ACC_LIST, bankAccNum);
                    // }
                    else if (startTag.equals(Constants.TAG_BANK_ACC)) {
                        boolean isDef = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_ACC_IS_DEF) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_ACC_IS_DEF).equals(
                                Constants.TRUE))
                            isDef = true;

                        boolean pReq = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_ACC_PN_CH_REQ) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_ACC_PN_CH_REQ).equals(
                                Constants.TRUE))
                            pReq = true;

                        boolean cvvPin = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_CVV_PIN) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_CVV_PIN).equals(
                                Constants.TRUE))
                            cvvPin = true;
                        boolean tPin = false;
                        if (parser.getAttributeValue(null, Constants.ATTR_TPIN) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_TPIN).equals(
                                Constants.TRUE))
                            tPin = true;

                        boolean isBPinReq = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_IS_BANK_PIN_REQUIRED) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_IS_BANK_PIN_REQUIRED)
                                .equals(Constants.TRUE))
                            isBPinReq = true;
                        boolean mPin = false;
                        if (parser.getAttributeValue(null, Constants.ATTR_MPIN) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_MPIN).equals(
                                Constants.TRUE))
                            mPin = true;

                        boolean status = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_BANK_ACC_STATUS) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_BANK_ACC_STATUS).equals(
                                Constants.BANK_ACC_TRUE))
                            status = true;
                        bankAccNumber = new BankAccountModel(
                                parser.getAttributeValue(null,
                                        Constants.ATTR_ACC_ID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BANK_ACC_NUMBER),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BANK_ACC_TYPE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BANK_ACC_CURRENCY),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_ACC_NICK), status,
                                isDef, pReq, cvvPin, tPin, mPin, isBPinReq);
                        bankAccNum.add(bankAccNumber);
                    } else if (startTag.equals(Constants.TAG_ACC)) {
                        boolean isDef = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_ACC_IS_DEF) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_ACC_IS_DEF).equals(
                                Constants.TRUE))
                            isDef = true;
                        boolean pReq = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_ACC_PN_CH_REQ) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_ACC_PN_CH_REQ).equals(
                                Constants.TRUE))
                            pReq = true;
                        boolean cvvPin = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_CVV_PIN) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_CVV_PIN).equals(
                                Constants.TRUE))
                            cvvPin = true;
                        boolean tPin = false;
                        if (parser.getAttributeValue(null, Constants.ATTR_TPIN) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_TPIN).equals(
                                Constants.TRUE))
                            tPin = true;

                        boolean isBPinReq = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_IS_BANK_PIN_REQUIRED) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_IS_BANK_PIN_REQUIRED)
                                .equals(Constants.TRUE))
                            isBPinReq = true;
                        boolean mPin = false;
                        if (parser.getAttributeValue(null, Constants.ATTR_MPIN) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_MPIN).equals(
                                Constants.TRUE))
                            mPin = true;

                        boolean status = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_BANK_ACC_STATUS) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_BANK_ACC_STATUS).equals(
                                Constants.BANK_ACC_TRUE))
                            status = true;

                        bank.addAccounts(new BankAccountModel(
                                parser.getAttributeValue(null,
                                        Constants.ATTR_ACC_ID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BANK_ACC_NUMBER), parser
                                .getAttributeValue(null,
                                        Constants.ATTR_BANK_ACC_TYPE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BANK_ACC_CURRENCY),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_ACC_NICK), status,
                                isDef, pReq, cvvPin, tPin, mPin, isBPinReq));

                    }
                    /*
                     * Catalog parsing
                     */
                    else if (startTag.equals(Constants.TAG_CAT)) {
                        table.put(Constants.KEY_CAT_VER,
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAT_VER));
                    } else if (startTag.equals(Constants.TAG_Categories)) {
                        listCategories = new ArrayList<CategoryModel>();
                        currCat = prevCat = null;
                    } else if (startTag.equals(Constants.TAG_Cagtegory)) {
                        prevCat = currCat;
                        currCat = new CategoryModel(parser.getAttributeValue(
                                null, Constants.ATTR_CID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_PARAM_NAME),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_ICON),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_ISPRODUCT), prevCat);
                        if (prevCat != null) {
                            prevCat.addCategory(currCat);
                        }
                    } else if (startTag.equals(Constants.TAG_BANK_prd)) {
                        product = new ProductModel(parser.getAttributeValue(
                                null, Constants.ATTR_BANK_ID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_FID), "");
                        product.setLabel(parser.getAttributeValue(null,
                                Constants.ATTR_SERV_LABEL));
                        product.setMinamt(parser.getAttributeValue(null,
                                Constants.ATTR_MINAMT));
                        product.setMinamtf(parser.getAttributeValue(null,
                                Constants.ATTR_MINAMTF));
                        product.setMaxamt(parser.getAttributeValue(null,
                                Constants.ATTR_MAXAMT));
                        product.setMaxamtf(parser.getAttributeValue(null,
                                Constants.ATTR_MAXAMTF));
                        product.setAmtRequired(parser.getAttributeValue(null,
                                Constants.ATTR_AMT_REQUIRED));
                        product.setDoValidate(parser.getAttributeValue(null,
                                Constants.ATTR_DO_VALIDATE));
                        product.setType(parser.getAttributeValue(null,
                                Constants.ATTR_TRN_TYPE));
                        product.setMultiple(parser.getAttributeValue(null,
                                Constants.ATTR_TRN_MUTIPLE));
                        product.setPpAllowed(parser.getAttributeValue(null,
                                Constants.ATTR_PP_ALLOWED));

                        if (parser.getAttributeValue(null,
                                Constants.ATTR_CONSUMER_MIN_LENGTH) != null) {
                            product.setConsumerMinLength(parser.getAttributeValue(null,
                                    Constants.ATTR_CONSUMER_MIN_LENGTH));
                        }
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_CONSUMER_MAX_LENGTH) != null) {
                            product.setConsumerMaxLength(parser.getAttributeValue(null,
                                    Constants.ATTR_CONSUMER_MAX_LENGTH));
                        }

                        product.setName(parser.getAttributeValue(null, Constants.ATTR_PARAM_NAME));

                        if (product.getId().equals(Constants.PRODUCT_ACCONT_OPENING)) {
                            ApplicationData.accountOpenMaxAmount = product.getMaxamt();
                            ApplicationData.accountOpenMinAmount = product.getMinamt();
                        }
                        if (supplier == null) {
                            currCat.addProduct(product);
                        } else {
                            // supplier.addProduct(product);
                        }
                    } else if (startTag.equals(Constants.TAG_BANK_supp)) {
                        // System.out.println("##supplier");
                        // supplier = new
                        // SupplierModel(parser.getAttributeValue(
                        // null, Constants.ATTR_NAME));
                        // currCat.addSupplier(supplier);
                    }

                    // catalog ends

                    if (serviceList == null)
                        serviceList = new ArrayList<ServiceModel>(25);

                    if (startTag.equals(Constants.TAG_SERV)) {
                        serviceList.add(new ServiceModel(
                                parser.getAttributeValue(null,
                                        Constants.ATTR_SERV_ID), parser
                                .getAttributeValue(null,
                                        Constants.ATTR_SERV_GF_ID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_SERV_LABEL), parser
                                .getAttributeValue(null,
                                        Constants.ATTR_SUPP_NAME),
                                parser.nextText(), 0));
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_CODE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_MOB_NO),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CUS_CODE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TYPE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_AMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_AMT_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_AUTH_CODE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_SUPP),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_PAY_MODE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_BILL_SUPP_HELPLINE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAM),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAM_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMT_FORMAT));

                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_Cagtegory)) {

                    parentCat = currCat.getParentCategory();
                    if (parentCat == null) {
                        listCategories.add(currCat);
                        currCat = null;
                    } else {
                        currCat = parentCat;
                        prevCat = currCat.getParentCategory();
                    }
                } else if (endTag.equals(Constants.TAG_Categories)) {
                    table.put(Constants.KEY_LIST_CATALOG, listCategories);
                }

                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                }
                if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                } else if (endTag.equals(Constants.TAG_MSG)) {
                }
            }

            eventType = parser.next();
        }

        serviceList = null;
        banks = null;
        msgs = null;
    }

    public ArrayList<CategoryModel> parseLocalCatalog(String xml)
            throws IOException, XmlPullParserException {

        parser.setInput(new InputStreamReader(new ByteArrayInputStream(xml
                .getBytes())));
        int eventType = parser.next();
        // catalog
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

                    if (startTag.equals(Constants.TAG_Cagtegory)) {
                        // System.out.println("##category");
                        prevCat = currCat;
                        currCat = new CategoryModel(parser.getAttributeValue(
                                null, Constants.ATTR_CID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_PARAM_NAME),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_ICON),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_ISPRODUCT), prevCat);
                        if (prevCat != null) {
                            prevCat.addCategory(currCat);
                        }
                    } else if (startTag.equals(Constants.TAG_BANK_prd)) {
                        product = new ProductModel(parser.getAttributeValue(
                                null, Constants.ATTR_BANK_ID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_FID), "");
                        product.setLabel(parser.getAttributeValue(null,
                                Constants.ATTR_SERV_LABEL));
                        product.setMinamt(parser.getAttributeValue(null,
                                Constants.ATTR_MINAMT));
                        product.setMinamtf(parser.getAttributeValue(null,
                                Constants.ATTR_MINAMTF));
                        product.setMaxamt(parser.getAttributeValue(null,
                                Constants.ATTR_MAXAMT));
                        product.setMaxamtf(parser.getAttributeValue(null,
                                Constants.ATTR_MAXAMTF));
                        product.setAmtRequired(parser.getAttributeValue(null,
                                Constants.ATTR_AMT_REQUIRED));
                        product.setDoValidate(parser.getAttributeValue(null,
                                Constants.ATTR_DO_VALIDATE));
                        product.setType(parser.getAttributeValue(null,
                                Constants.ATTR_TRN_TYPE));
                        product.setMultiple(parser.getAttributeValue(null,
                                Constants.ATTR_TRN_MUTIPLE));

                        if (parser.getAttributeValue(null,
                                Constants.ATTR_CONSUMER_MIN_LENGTH) != null) {
                            product.setConsumerMinLength(parser.getAttributeValue(null,
                                    Constants.ATTR_CONSUMER_MIN_LENGTH));
                        }
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_CONSUMER_MAX_LENGTH) != null) {
                            product.setConsumerMaxLength(parser.getAttributeValue(null,
                                    Constants.ATTR_CONSUMER_MAX_LENGTH));
                        }

                        product.setName(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME));

                        if (supplier == null) {
                            currCat.addProduct(product);
                        } else {
                            // supplier.addProduct(product);
                        }
                    } else if (startTag.equals(Constants.TAG_BANK_supp)) {
                    }
                }

            } else if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_Cagtegory)) {

                    parentCat = currCat.getParentCategory();
                    if (parentCat == null) {
                        listCategories.add(currCat);
                        currCat = null;
                    } else {
                        currCat = parentCat;
                        prevCat = currCat.getParentCategory();
                    }
                } else if (endTag.equals(Constants.TAG_Categories)) {
                    // System.out.println("##/>categories");
                    // table.put(Constants.KEY_LIST_CATALOG, listCategories);
                }

            }

            eventType = parser.next();
        }

        return listCategories;
    }

    public void parseCWTransMsg(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        AppLogger.i("##Parsing CWTransaction Message");

        int eventType = parser.next();
        ArrayList<TransactionModel> transList = null;
        ArrayList<MessageModel> msgs = null;

        TransactionModel trans = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        AppLogger.i("###Parsing TRX");
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_CODE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_MOB_NO),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TYPE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_AMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_AMT_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_AUTH_CODE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_SUPP),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_PAY_MODE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_BILL_SUPP_HELPLINE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAM),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_A1CAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_A1BAL));
                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseDRLeg2TransMsg(Hashtable<String, Object> table)
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_CODE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_AMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_AMT_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_AGENTBALANCE));
                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseAccountOpenVerifyMSISDNandCNICTransMsg(
            Hashtable<String, Object> table) throws IOException,
            XmlPullParserException {
        AppLogger.i("##Parsing Account open verify msisdn and cnic Message");
        int eventType = parser.next();
        ArrayList<MessageModel> msgs = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(Constants.TAG_PARAM)) {

                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());

                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseInfoMsg(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        int eventType = parser.next();
        ArrayList<MessageModel> msgs = null;
        ArrayList<CitiesModel> cities = null;
        ArrayList<PaymentReasonModel> paymentReasons = null;
        ArrayList<BankModel> banks = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();
                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    } else if (startTag.equals(Constants.TAG_CITIES)) {
                        cities = new ArrayList<CitiesModel>();
                    } else if (startTag.equals(Constants.TAG_MEMBERS_BANKS)) {
                        banks = new ArrayList<>();
                    } else if (startTag.equals(Constants.TAG_BANK)) {
                        banks.add(new BankModel(parser.getAttributeValue(
                                null, Constants.ATTR_PARAM_IMD),
                                parser.getAttributeValue(null, Constants.ATTR_PARAM_NAME),
                                parser.getAttributeValue(null, Constants.ATTR_MIN_LENGTH),
                                parser.getAttributeValue(null, Constants.ATTR_MAX_LENGTH)));
                    }else if (startTag.equals(Constants.TAG_CITY)) {
                        cities.add(new CitiesModel(parser.getAttributeValue(
                                null, Constants.ATTR_PARAM_NAME)));
                    } else if (startTag.equals(Constants.TAG_PAYMENT_REASONS)) {
                        paymentReasons = new ArrayList<PaymentReasonModel>();
                    } else if (startTag.equals(Constants.TAG_PAYMENT_REASON)) {
                        paymentReasons.add(new PaymentReasonModel(parser.getAttributeValue(
                                null, Constants.ATTR_PARAM_CODE),parser.getAttributeValue(
                                null, Constants.ATTR_PARAM_NAME)));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                } else if (endTag.equals(Constants.TAG_MEMBERS_BANKS)) {
                    table.put(Constants.KEY_LIST_MEMBERS_BANKS, banks);
                } else if (endTag.equals(Constants.TAG_CITIES)) {
                    table.put(Constants.KEY_LIST_CITIES, cities);
                } else if (endTag.equals(Constants.TAG_PAYMENT_REASONS)) {
                    table.put(Constants.KEY_LIST_PAYMENT_REASONS, paymentReasons);
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_DESCRIPTION),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMTF));
                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRXID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CMOB),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAM),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAMF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BALF));

                        trans.setTxam(parser.getAttributeValue(null,
                                Constants.ATTR_TXAM));
                        trans.setTxamf(parser.getAttributeValue(null,
                                Constants.ATTR_TXAMF));
                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRXID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CMOB),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAM),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAMF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BALF));
                        trans.setCoreacid(parser.getAttributeValue(null,
                                Constants.ATTR_COREACID));
                        trans.swcnic = parser.getAttributeValue(null,
                                Constants.ATTR_SWCNIC);
                        trans.setCoreactl(parser.getAttributeValue(null,
                                Constants.ATTR_COREACTL));
                        trans.setTxam(parser.getAttributeValue(null,
                                Constants.ATTR_TXAM));
                        trans.setTxamf(parser.getAttributeValue(null,
                                Constants.ATTR_TXAMF));
                        trans.setBalf(parser.getAttributeValue(null,
                                Constants.ATTR_BALF));
                        trans.setCoreactl(parser.getAttributeValue(null,
                                Constants.ATTR_COREACTL));
                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgCnic2Core(Hashtable<String, Object> table)
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRXID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CMOB),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAM),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAMF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BALF));
                        trans.setCoreacid(parser.getAttributeValue(null,
                                Constants.ATTR_COREACID));
                        trans.setSwcnic(parser.getAttributeValue(null,
                                Constants.ATTR_SWCNIC));
                        trans.setSwmob(parser.getAttributeValue(null,
                                Constants.ATTR_SWMOB));
                        trans.setTxam(parser.getAttributeValue(null,
                                Constants.ATTR_TXAM));
                        trans.setTxamf(parser.getAttributeValue(null,
                                Constants.ATTR_TXAMF));
                        trans.setBalf(parser.getAttributeValue(null,
                                Constants.ATTR_BALF));
                        trans.setCoreactl(parser.getAttributeValue(null,
                                Constants.ATTR_COREACTL));

                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgCashOut(Hashtable<String, Object> table)
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_ID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CMOB),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAM),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAMF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BALF));
                        trans.swcnic = parser.getAttributeValue(null,
                                Constants.ATTR_CNIC);
                        trans.setTxam(parser.getAttributeValue(null,
                                Constants.ATTR_TXAM));
                        trans.setTxamf(parser.getAttributeValue(null,
                                Constants.ATTR_TXAMF));

                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsg3rdPartyCashOut(Hashtable<String, Object> table)
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel();
                        trans.setId(parser.getAttributeValue(null, Constants.ATTR_TRXID));
                        trans.setSwcnic(parser.getAttributeValue(null, Constants.ATTR_CNIC));
                        trans.setCmob(parser.getAttributeValue(null, Constants.ATTR_CMOB));
                        trans.setDatef(parser.getAttributeValue(null, Constants.ATTR_DATEF));
                        trans.setTimef(parser.getAttributeValue(null, Constants.ATTR_TIMEF));
                        trans.setActitle(parser.getAttributeValue(null, Constants.ATTR_COREACTL));
                        trans.setTxam(parser.getAttributeValue(null, Constants.ATTR_TXAM));
                        trans.setTxamf(parser.getAttributeValue(null, Constants.ATTR_TXAMF));
                        trans.setTpamf(parser.getAttributeValue(null, Constants.ATTR_TPAMF));
                        trans.setTamtf(parser.getAttributeValue(null, Constants.ATTR_TAMTF));

                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransIBFT(Hashtable<String, Object> table)
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel();
                        trans.setId(parser.getAttributeValue(null, Constants.ATTR_TRXID));
                        trans.setAmob(parser.getAttributeValue(null, Constants.ATTR_AMOB));
                        trans.setBbacid(parser.getAttributeValue(null, Constants.ATTR_BBACID));
                        trans.setCoreacid(parser.getAttributeValue(null, Constants.ATTR_COREACID));
                        trans.setProd(parser.getAttributeValue(null, Constants.ATTR_PROD));
                        trans.setCamt(parser.getAttributeValue(null, Constants.ATTR_CAMT));
                        trans.setCamtf(parser.getAttributeValue(null, Constants.ATTR_CAMTF));
                        trans.setTxam(parser.getAttributeValue(null, Constants.ATTR_TXAM));
                        trans.setTxamf(parser.getAttributeValue(null, Constants.ATTR_TXAMF));
                        trans.setTpam(parser.getAttributeValue(null, Constants.ATTR_TPAM));
                        trans.setTpamf(parser.getAttributeValue(null, Constants.ATTR_TPAMF));
                        trans.setTamt(parser.getAttributeValue(null, Constants.ATTR_TAMT));
                        trans.setTamtf(parser.getAttributeValue(null, Constants.ATTR_TAMTF));
                        trans.setDate(parser.getAttributeValue(null, Constants.ATTR_TRN_DATE));
                        trans.setDatef(parser.getAttributeValue(null, Constants.ATTR_DATEF));
                        trans.setTimef(parser.getAttributeValue(null, Constants.ATTR_TIMEF));
                        trans.setBalf(parser.getAttributeValue(null, Constants.ATTR_BALF));
                        trans.setBankName(parser.getAttributeValue(null, Constants.ATTR_BENE_BANK_NAME));
                        trans.setCoreacno(parser.getAttributeValue(null, Constants.ATTR_CORE_AC_NO));
                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgCashOutByTrxId(Hashtable<String, Object> table)
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_ID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CMOB),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAM),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAMF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BALF));
                        trans.swcnic = parser.getAttributeValue(null,
                                Constants.ATTR_CNIC);
                        trans.setTxam(parser.getAttributeValue(null,
                                Constants.ATTR_TXAM));
                        trans.setTxamf(parser.getAttributeValue(null,
                                Constants.ATTR_TXAMF));
                        trans.setId(parser.getAttributeValue(null,
                                Constants.ATTR_TRXID));
                        trans.setActitle(parser.getAttributeValue(null,
                                Constants.ATTR_NAME));

                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgAgentToAgent(Hashtable<String, Object> table)
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRXID), "",
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAM),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAMF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BALF));

                        trans.setRamob(parser.getAttributeValue(null,
                                Constants.ATTR_RAMOB));
                        trans.setRacnic(parser.getAttributeValue(null,
                                Constants.ATTR_RACNIC));

                        trans.setTxam(parser.getAttributeValue(null,
                                Constants.ATTR_TXAM));
                        trans.setTxamf(parser.getAttributeValue(null,
                                Constants.ATTR_TXAMF));

                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgCashIn(Hashtable<String, Object> table)
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRXID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CMOB),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAM),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAMF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BALF));
                        trans.swcnic = parser.getAttributeValue(null,
                                Constants.ATTR_CNIC);
                        trans.setTxam(parser.getAttributeValue(null,
                                Constants.ATTR_TXAM));
                        trans.setTxamf(parser.getAttributeValue(null,
                                Constants.ATTR_TXAMF));

                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgBillPayment(Hashtable<String, Object> table)
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRXID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CMOB),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAM),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAMF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BALF));
                        trans.setProductName(parser.getAttributeValue(null,
                                Constants.ATTR_PNAME));
                        trans.setProd(parser.getAttributeValue(null,
                                Constants.ATTR_TRN_PROD));
                        trans.setConsumer(parser.getAttributeValue(null,
                                Constants.ATTR_CONSUMER));
                        trans.setBalf(parser.getAttributeValue(null,
                                Constants.ATTR_BALF));

                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseTransMsgCollectionPayment(Hashtable<String, Object> table)
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel();
                        trans.setCmob(parser.getAttributeValue(null, Constants.ATTR_CMOB));
                        trans.setConsumer(parser.getAttributeValue(null, Constants.ATTR_CONSUMER));
                        trans.setStatus(parser.getAttributeValue(null, Constants.ATTR_BPAID));
                        trans.setDueDate(parser.getAttributeValue(null, Constants.ATTR_DUEDATEF));
                        trans.setDatef(parser.getAttributeValue(null, Constants.ATTR_TRN_DATE_FORMAT));
                        trans.setTimef(parser.getAttributeValue(null, Constants.ATTR_TIMEF));
                        trans.setTxamf(parser.getAttributeValue(null, Constants.ATTR_BAMTF));
                        trans.setTpamf(parser.getAttributeValue(null, Constants.ATTR_TPAMF));
                        trans.setTamtf(parser.getAttributeValue(null, Constants.ATTR_TAMTF));
                        trans.setCamtf(parser.getAttributeValue(null, Constants.ATTR_CAMTF));
                        trans.setProductName(parser.getAttributeValue(null, Constants.ATTR_PNAME));
                        trans.setId(parser.getAttributeValue(null, Constants.ATTR_TRXID));
                        trans.setBalf(parser.getAttributeValue(null, Constants.ATTR_BALF));
                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRXID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CMOB),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAM),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAMF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BALF));
                        trans.setRcmob(parser.getAttributeValue(null,
                                Constants.ATTR_RCMOB));
                        trans.setSwcnic(parser.getAttributeValue(null,
                                Constants.ATTR_SWCNIC));
                        trans.setSwmob(parser.getAttributeValue(null,
                                Constants.ATTR_SWMOB));
                        trans.setRwmob(parser.getAttributeValue(null,
                                Constants.ATTR_RWMOB));
                        trans.setRwcnic(parser.getAttributeValue(null,
                                Constants.ATTR_RWCNIC));
                        trans.setTxam(parser.getAttributeValue(null,
                                Constants.ATTR_TXAM));
                        trans.setTxamf(parser.getAttributeValue(null,
                                Constants.ATTR_TXAMF));
                        trans.setBalf(parser.getAttributeValue(null,
                                Constants.ATTR_BALF));

                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRXID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_AMOB),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BBACID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_COREACID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAM),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TPAMF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TAMTF),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BALF));

                        trans.setTxam(parser.getAttributeValue(null,
                                Constants.ATTR_TXAM));
                        trans.setTxamf(parser.getAttributeValue(null,
                                Constants.ATTR_TXAMF));

                        trans.setCoreactl(parser.getAttributeValue(null,
                                Constants.ATTR_COREACTL));

                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseReceiveMoneySenderRedeemPayment(
            Hashtable<String, Object> table) throws IOException,
            XmlPullParserException {
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel();
                        trans.ReceiveMoneySenderRedeem(parser
                                        .getAttributeValue(null, Constants.ATTR_TRXID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_SWCNIC), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_SWMOB), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_RWCNIC), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_RWMOB), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TRN_PROD),
                                parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TAMT), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TAMTF), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_BALF));
                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseReceiveMoneyPendingTrxPayment(
            Hashtable<String, Object> table) throws IOException,
            XmlPullParserException {
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel();
                        trans.ReceiveMoneyPendingTrx(parser
                                        .getAttributeValue(null, Constants.ATTR_TRXID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_SWCNIC), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_SWMOB), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_RWCNIC), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_RWMOB), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMT), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_CAMTF), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TPAM), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TPAMF), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TAMT), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TAMTF), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TXAM), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TXAMF), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_BALF));
                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseReceiveMoneyReceiveCash(
            Hashtable<String, Object> table) throws IOException,
            XmlPullParserException {
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

                    if (startTag.equals(Constants.TAG_PARAM)) {
                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel();
                        trans.ReceiveMoneyReceiveCash(parser
                                        .getAttributeValue(null, Constants.ATTR_TRXID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_SWCNIC), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_SWMOB), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_RWCNIC), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_RWMOB), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TRN_DATE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_DATE_FORMAT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_TIME_FORMAT), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_CAMT), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_CAMTF), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TPAM), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TPAMF), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TAMT), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TAMTF), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TXAM), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_TXAMF), parser
                                        .getAttributeValue(null,
                                                Constants.ATTR_BALF));
                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                } else if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseSalesSummary(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        int eventType = parser.next();
        ArrayList<MessageModel> msgs = null;
        ArrayList<TransactionModel> transList = null;
        TransactionModel trans = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();

                    if (startTag.equals(Constants.TAG_PARAM)) {

                        table.put(parser.getAttributeValue(null,
                                Constants.ATTR_PARAM_NAME), parser.nextText());
                    } else if (startTag.equals(Constants.TAG_TRANS)) {
                        transList = new ArrayList<TransactionModel>(5);
                    } else if (startTag.equals(Constants.TAG_TRN)) {
                        trans = new TransactionModel(parser.getAttributeValue(
                                null, Constants.ATTR_TRN_PROD),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_AMT),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_TRN_AMT_FORMAT));
                        transList.add(trans);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        msgs = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        msgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();

                if (endTag.equals(Constants.TAG_TRANS)) {
                    table.put(Constants.KEY_LIST_TRANS, transList);
                }
                if (endTag.equals(Constants.TAG_MESGS)) {
                    table.put(Constants.KEY_LIST_MSGS, msgs);
                } else if (endTag.equals(Constants.TAG_MSG)) {
                }
            }

            eventType = parser.next();
        }

        msgs = null;
        transList = null;
        trans = null;
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
                    if (startTag.equals(Constants.TAG_MESGS)) {
                        errors = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        errors.add(new MessageModel(parser.getAttributeValue(
                                null, Constants.ATTR_CODE), parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_MESGS)) {
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
                    if (startTag.equals(Constants.TAG_ERRORS)) {
                        errors = new ArrayList<MessageModel>(2);
                    } else if (startTag.equals(Constants.TAG_ERROR)) {

                        MessageModel messageModel = new MessageModel();
                        messageModel.setCode(parser.getAttributeValue(null, Constants.ATTR_CODE));
                        messageModel.setLevel(parser.getAttributeValue(null, Constants.ATTR_LEVEL));

                        if (parser.getAttributeValue(null, XmlConstants.ATTR_NADRA_SESSION_ID) != null) {
                            messageModel.setNadraSessionId(parser.getAttributeValue(null,
                                    XmlConstants.ATTR_NADRA_SESSION_ID));
                        }
                        if (parser.getAttributeValue(null, Constants.ATTR_THIRD_PARTY_TRANSACTION_ID) != null) {
                            messageModel.setThirdPartyTransactionId(parser.getAttributeValue(null,
                                    Constants.ATTR_THIRD_PARTY_TRANSACTION_ID));
                        }

                        messageModel.setDescr(parser.nextText());
                        errors.add(messageModel);
//
//                        errors.add(new MessageModel(parser.getAttributeValue(
//                                null, Constants.ATTR_CODE), parser
//                                .getAttributeValue(null, Constants.ATTR_LEVEL),
//                                parser.nextText()));
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                String endTag = parser.getName();
                endTag = endTag.toLowerCase();
                if (endTag.equals(Constants.TAG_ERRORS)) {
                    table.put(Constants.KEY_LIST_ERRORS, errors);
                }
            }
            eventType = parser.next();
        }
    }

    public void parseBankAccMsg(Hashtable<String, Object> table)
            throws IOException, XmlPullParserException {
        int eventType = parser.next();
        ArrayList<BankAccountModel> bankAccNum = null;
        ArrayList<MessageModel> mesgs = null;
        BankAccountModel bankAccNumber = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String startTag = parser.getName();
                if (startTag != null) {
                    startTag = startTag.toLowerCase();
                    if (startTag.equals(Constants.TAG_BANK_ACCS)) {
                        bankAccNum = new ArrayList<BankAccountModel>(5);
                        table.put(Constants.KEY_BANK_ACC_LIST, bankAccNum);
                    } else if (startTag.equals(Constants.TAG_BANK_ACC)) {
                        boolean isDef = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_ACC_IS_DEF) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_ACC_IS_DEF).equals(
                                Constants.TRUE))
                            isDef = true;

                        boolean pReq = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_ACC_PN_CH_REQ) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_ACC_PN_CH_REQ).equals(
                                Constants.TRUE))
                            pReq = true;

                        boolean cvvPin = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_CVV_PIN) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_CVV_PIN).equals(
                                Constants.TRUE))
                            cvvPin = true;
                        boolean tPin = false;
                        if (parser.getAttributeValue(null, Constants.ATTR_TPIN) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_TPIN).equals(
                                Constants.TRUE))
                            tPin = true;

                        boolean isBPinReq = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_IS_BANK_PIN_REQUIRED) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_IS_BANK_PIN_REQUIRED)
                                .equals(Constants.TRUE))
                            isBPinReq = true;
                        boolean mPin = false;
                        if (parser.getAttributeValue(null, Constants.ATTR_MPIN) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_MPIN).equals(
                                Constants.TRUE))
                            mPin = true;

                        boolean status = false;
                        if (parser.getAttributeValue(null,
                                Constants.ATTR_BANK_ACC_STATUS) != null
                                && parser.getAttributeValue(null,
                                Constants.ATTR_BANK_ACC_STATUS).equals(
                                Constants.BANK_ACC_TRUE))
                            status = true;
                        bankAccNumber = new BankAccountModel(
                                parser.getAttributeValue(null,
                                        Constants.ATTR_ACC_ID),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BANK_ACC_NUMBER),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BANK_ACC_TYPE),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_BANK_ACC_CURRENCY),
                                parser.getAttributeValue(null,
                                        Constants.ATTR_ACC_NICK), status,
                                isDef, pReq, cvvPin, tPin, isBPinReq, mPin);
                        bankAccNum.add(bankAccNumber);
                    } else if (startTag.equals(Constants.TAG_MESGS)) {
                        mesgs = new ArrayList<MessageModel>(2);
                        table.put(Constants.KEY_LIST_MSGS, mesgs);
                    } else if (startTag.equals(Constants.TAG_MESG)) {
                        mesgs.add(new MessageModel(null, parser
                                .getAttributeValue(null, Constants.ATTR_LEVEL),
                                parser.nextText()));
                    }
                }
            }
            eventType = parser.next();
        }
    }
}