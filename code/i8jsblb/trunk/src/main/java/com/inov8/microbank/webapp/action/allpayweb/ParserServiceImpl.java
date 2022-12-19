package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.AllPayWebUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.webapp.action.allpayweb.formbean.Category;
import com.inov8.microbank.webapp.action.allpayweb.formbean.Product;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Yasir Shabbir on 9/7/2016.
 */
@Service
public class ParserServiceImpl implements ParserService {

    private final static Logger logger = Logger.getLogger(ParserServiceImpl.class);

    private static final int CID = 0;
    private static final int NAME = 3;
    private static final int PARENT_CAT_ID = 4;
    private static final String PRODUCT_NAME = "name";
    private static final String ID = "id";
    private static final String URL = "url";
    private static final String CAT_ID = "cid";
    private static final String SEQUENCE = "seq";
    private static final int MY_ACCOUNT_CAT_ID = 33;
    private static final int PAY_BILL_CAT_ID = 4;
    private static final int MONEY_TRANSFER_CAT_ID = 3;
    private static final int ACCOUNT_OPENING_CAT_ID = 21;
    private static final Integer PIN_CHANGE_REQUIRED = 0;
    private static final int DEBIT_CARD_CAT_ID = 44;
    private static final int CASH_SERVICES_CAT_ID = 1;
    private static final int AGENT_COLLECTION_PAYMENT_CAT_ID = 126;


    private static final int CUSTOMER_HRA_CASH_WITHDRAWAL = 114;
    private static final int CUSTOMER__CASHOUT = 2;
    private static final Integer SUCCESS_CASE = 5;


    @Override
    public List<Category> parseCateogry(String xmlResponse) {

        List<Category> categoryList = new ArrayList<Category>();
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = new ByteArrayInputStream(xmlResponse.getBytes());

            org.w3c.dom.Document document = builder.parse(inputStream);

            NodeList list = document.getElementsByTagName("category");


            for (int i = 0; i < list.getLength(); i++) {


                Node node = list.item(i);
                final int categoryId = Integer.parseInt(node.getAttributes().item(CID).getNodeValue());
                Category category = addCategory(node, categoryList);
                if(node.getLastChild().getNodeName().equals("prds")){
                    addLastChildProduct(node, category);
                }

                else if  (node.getFirstChild().getNodeName().equals("prds")) {
                    addFirstChildProduct(node, category);
                }


                switch (categoryId) {
                    case ACCOUNT_OPENING_CAT_ID:
                        addAccountOpeningProduct(category);
                        break;
                    case MONEY_TRANSFER_CAT_ID:
                        addReceiveMoneyProduct(category);
                        break;
                    case PAY_BILL_CAT_ID:
                        //addBulkBillPaymentProduct(category); //turab 11-27-2017 blocked for time being to not show unimplemted products on agentweb
                        break;
                    case CASH_SERVICES_CAT_ID:
                        addCashInProducts(category);
                        break;

                    case CUSTOMER_HRA_CASH_WITHDRAWAL:
                        addCustomerHRACashWithDrawal(category);
                        break;

                    case AGENT_COLLECTION_PAYMENT_CAT_ID:
                        addAgentCollectionPaymentProduct(category);
                        break;
                    case CUSTOMER__CASHOUT:
                        addCashOutProducts(category);
                        break;
                }


                /*if (node.getChildNodes().getLength() > 1) {
                    if (node.getChildNodes().item(1).getNodeName().equals("prds")) {
                        addProductCriteria(category, node.getChildNodes().item(1).getChildNodes());
                    }
                }*/


            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        addMyAccountProducts(categoryList);
        return makeChildParentRelationShip(categoryList);
    }

    private Category addCustomerHRACashWithDrawal(Category category)
    {
        Product hra = new Product();
        hra.setName("HRA Cash Withdrawal");
        hra.setUrl("customerHraCashWithdrawalInfo.aw");
        category.addProduct(hra);
        return category;
    }

    private Category addCashInProducts(Category category)
    {
        Product cashInProduct = new Product();
        cashInProduct.setName("Cash In");
        cashInProduct.setUrl("productpurchase.aw");
        category.addProduct(cashInProduct);
        return category;
    }

    private Category addAccountOpeningProduct(Category category)
    {
        Product accountOpeningProduct = new Product();
        accountOpeningProduct.setName("Account Opening");
        accountOpeningProduct.setUrl("accountOpeningWithBVS.aw");
        category.addProduct(accountOpeningProduct);

        return category;
    }


    private List<Category> addAccountProducts(List<Category> categoryList) {
        Product accountOpeningProduct = new Product();
        accountOpeningProduct.setName("BVS A/C Opening");
        accountOpeningProduct.setUrl("accountOpeningBVS.aw");


        Product accountOpeningProductWithoutBVS = new Product();
        accountOpeningProductWithoutBVS.setName("Conventional A/C Opening");
        accountOpeningProductWithoutBVS.setUrl("accountOpening.aw");

        Product conventionalAccountOpeningProductWithCardIssuance = new Product();
        conventionalAccountOpeningProductWithCardIssuance.setName("Conventional A/C Opening with Card Issuance");
        conventionalAccountOpeningProductWithCardIssuance.setUrl("conventionalAccountOpeningCardIssuance.aw");

        Product bvsAccountOpeningProductWithCardIssuance = new Product();
        bvsAccountOpeningProductWithCardIssuance.setName("BVS A/C Opening with Card Issuance");
        bvsAccountOpeningProductWithCardIssuance.setUrl("bvsAccountOpeningCardIssuance.aw");

        Category accountOpeningCategory = new Category();
        accountOpeningCategory.setCategoryId(-1);
        accountOpeningCategory.setName("Account Opening");
        accountOpeningCategory.setParentCategoryId(-1);
        accountOpeningCategory.setSequenceNumber(1);
        accountOpeningCategory.addProduct(accountOpeningProduct);
        accountOpeningCategory.addProduct(accountOpeningProductWithoutBVS);
        accountOpeningCategory.addProduct(conventionalAccountOpeningProductWithCardIssuance);
        accountOpeningCategory.addProduct(bvsAccountOpeningProductWithCardIssuance);


        categoryList.add(accountOpeningCategory);

        return categoryList;
    }

    private Category addCategory(Node node, List<Category> categoryList) {
        Category category = new Category();
        final int categoryId = Integer.parseInt(node.getAttributes().item(CID).getNodeValue());
        category.setCategoryId(categoryId);
        category.setName(node.getAttributes().item(NAME).getNodeValue());
        if(node.getAttributes().item(PARENT_CAT_ID)!= null)
            category.setParentCategoryId(Integer.parseInt(node.getAttributes().item(PARENT_CAT_ID).getNodeValue()));
        if(null != node.getAttributes().getNamedItem(SEQUENCE))
            category.setSequenceNumber(Integer.parseInt(node.getAttributes().getNamedItem(SEQUENCE).getNodeValue()));
        categoryList.add(category);
        return category;
    }

    private void addFirstChildProduct(Node node, Category category) {
        final NodeList childNodes = node.getFirstChild().getChildNodes();
        //turab 11-27-2017 blocked for time being to not show unimplemted products on agentweb
        //addProductCriteria(category, childNodes);
    }

    private void addLastChildProduct(Node node, Category category) {
        final NodeList childNodes = node.getLastChild().getChildNodes();
        //turab 11-27-2017 blocked for time being to not show unimplemted products on agentweb
        //addProductCriteria(category, childNodes);
    }

    private void addProductCriteria(Category category, NodeList childNodes) {
        for (int a = 0; a < childNodes.getLength(); a++) {
            Node productNodes = childNodes.item(a);
            Product product = new Product();
            product.setCategoryId(Integer.parseInt(productNodes.getAttributes().getNamedItem(CAT_ID).getNodeValue()));
            product.setName(productNodes.getAttributes().getNamedItem(PRODUCT_NAME).getNodeValue());
            product.setUrl(productNodes.getAttributes().getNamedItem(URL).getNodeValue());

            if (category.getCategoryId() == 55) {
                product.setUrl("collectPayment.aw?PID=" + productNodes.getAttributes().getNamedItem(ID).getNodeValue());
            } else if (category.getParentCategoryId() == 4) {
                product.setUrl("productpurchase.aw?PID=" + productNodes.getAttributes().getNamedItem(ID).getNodeValue()+"&PNAME="+productNodes.getAttributes().getNamedItem(PRODUCT_NAME).getNodeValue());
            }
            else if(category.getCategoryId() == 3){
                System.out.println();
            }

            category.addProduct(product);
        }
    }


    private List<Category> makeChildParentRelationShip(List<Category> categoryList) {
        Category tempCategory = new Category();
        Set<Category> newCategoryList = new HashSet<>();
        for (Category category : categoryList) {
            if (category.getParentCategoryId() != -1) {
                tempCategory.setCategoryId(category.getParentCategoryId());
                final int i = categoryList.indexOf(tempCategory);
                Category parentCategory = categoryList.get(i);
                parentCategory.addChildCategory(category);
            } else {
                newCategoryList.add(category);
            }
        }
        final ArrayList<Category> categories = new ArrayList<>(newCategoryList);
        Collections.sort(categories);

        return categories;
    }


    private Category addBulkBillPaymentProduct(Category category) {
        Product bulkBillProduct = new Product();
        bulkBillProduct.setName("Bulk Bill Payment");
        bulkBillProduct.setUrl("bulkBillPayment.aw");
        bulkBillProduct.setCategoryId(PAY_BILL_CAT_ID);
        category.addProduct(bulkBillProduct);

        return category;
    }

    private Category addReceiveMoneyProduct(Category category) {
        Product receiveMoneyProduct = new Product();
        receiveMoneyProduct.setName("Receive Money");
        receiveMoneyProduct.setUrl("payCashStepOne.aw");

        Product senderReedemProduct = new Product();
        senderReedemProduct.setName("Sender Redeem");
        senderReedemProduct.setUrl("senderReedem.aw");


        Product moneyTransferProduct = new Product();
        moneyTransferProduct.setName("Money Transfer Sending");
        moneyTransferProduct.setUrl("case2CashStepOne.aw");


        category.addProduct(moneyTransferProduct);
        category.addProduct(receiveMoneyProduct);
        category.addProduct(senderReedemProduct);

        return category;
    }

    private Category addAgentCollectionPaymentProduct(Category category) {
        Product agentVrgProduct = new Product();
        agentVrgProduct.setName("ITP Challan Collection");
        agentVrgProduct.setUrl("challanpaymentform.aw?PID=50056");

        category.addProduct(agentVrgProduct);

        Product kpChallan = new Product();
        kpChallan.setName("KP Challan Collection");
        kpChallan.setUrl("challanpaymentform.aw?PID=10245131");

        category.addProduct(kpChallan);

        Product licenseFeeCollection = new Product();
        licenseFeeCollection.setName("Driving License Fee Collection");
        licenseFeeCollection.setUrl("challanpaymentform.aw?PID=10245132");

        category.addProduct(licenseFeeCollection);

        Product etCollection = new Product();
        etCollection.setName("E & T Collection");
        etCollection.setUrl("challanpaymentform.aw?PID=10245133");

        category.addProduct(etCollection);

        return category;
    }


    private Category addCashOutProducts(Category category) {
        Product cashOutViaTCode = new Product();
        cashOutViaTCode.setName("By Transaction ID");
        cashOutViaTCode.setUrl("customerCashOutInfo.aw");


        Product cashOutViaIVR = new Product();
        cashOutViaIVR.setName("By IVR");
        cashOutViaIVR.setUrl("customer2CashOutInfo.aw");

        category.addProduct(cashOutViaTCode);
        category.addProduct(cashOutViaIVR);

        return category;
    }



    private List<Category> addMyAccountProducts(List<Category> categoryList) {


        Category category = new Category();
        category.setCategoryId(MY_ACCOUNT_CAT_ID);

        if (categoryList.contains(category)) {
            category = categoryList.get(categoryList.indexOf(category));
        } else {

            category.setName("My Account");
            category.setParentCategoryId(-1);
            category.setSequenceNumber(99);
            categoryList.add(category);
        }

        Product changePinProduct = new Product();
        changePinProduct.setName("Change MPIN");
        changePinProduct.setUrl("changePin.aw");

        Product checkBalanceProduct = new Product();
        checkBalanceProduct.setName("Check Balance");
        checkBalanceProduct.setUrl("checkallpaybalance.aw");


        Product miniStatementProduct = new Product();
        miniStatementProduct.setName("Mini Statement");
        miniStatementProduct.setUrl("ministatement.aw");

        Product myCommisionProduct = new Product();
        myCommisionProduct.setName("My Commission");
        myCommisionProduct.setUrl("myCommission.aw");


        Product myLimitsProduct = new Product();
        myLimitsProduct.setName("My Limits");
        myLimitsProduct.setUrl("checkRemainingLimits.aw");


        /*
        This is for handler , only Agent can see this menu's
        1) My Commission
        2) Check balance
        3) Mini Statement
         */
        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        if (appUserModel != null && appUserModel.getHandlerId() == null) {
            category.addProduct(checkBalanceProduct);
            category.addProduct(miniStatementProduct);
            //category.addProduct(myCommisionProduct);

        }
        category.addProduct(changePinProduct);
        //category.addProduct(myLimitsProduct);


        return categoryList;

    }


    /**
     * @param responseXml
     * @param requestWrapper
     * @return
     */
    @Override
    public boolean isPinChangeRequired(String responseXml, AllPayRequestWrapper requestWrapper) throws XPathExpressionException {

        if (AllPayWebUtil.isPinChangeRequired(responseXml)) {

            requestWrapper.setAttribute("oldPinLabel", "Please enter Password to be changed for the first time:");

            requestWrapper.getSession().setAttribute(AllPayWebConstant.PIN_CHANGE_REQUIRED.getValue(), Boolean.TRUE);
            requestWrapper.getSession().setAttribute("message", "Please change your PIN for the first time.");
            requestWrapper.getSession().setAttribute("APID", requestWrapper.getParameter("UID"));
            requestWrapper.setAttribute("PIN_CHANGE_REQUIRED", true);

            return true;
        }

        return false;
    }

}
