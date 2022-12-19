package com.inov8.microbank.mfsweb;

/**
 * Project Name: 			Microbank
 * @author 					Jawwad Farooq
 * Creation Date: 			April 17, 2009
 * Description:
 */

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.encrypt;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.fonepay.common.FonePayResponseCodes;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.inov8.microbank.common.model.FavoriteNumbersModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.model.productmodule.ProdCatalogDetailListViewModel;
import com.inov8.microbank.common.model.smartmoneymodule.SmAcctInfoListViewModel;
import com.inov8.microbank.common.model.transactionmodule.FetchTransactionListViewModel;
import com.inov8.microbank.common.model.transactionmodule.SalesSummaryListViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.XMLConstants;


public class MfsWebResponseDataPopulator
{
	private static final Logger logger = Logger.getLogger(MfsWebResponseDataPopulator.class);

	DocumentBuilderFactory domFactory = null ;

	public MfsWebResponseDataPopulator()
	{
		domFactory = DocumentBuilderFactory.newInstance();
	}

	public void populateAllPaySalesSummaryData(HttpServletRequest request, String xml){

		NodeList nodeList = this.executeXPathQuery(xml, "//msg/trans/*") ;
		ArrayList<SalesSummaryListViewModel> summaryList = new ArrayList<SalesSummaryListViewModel>();

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			SalesSummaryListViewModel sslvm = new SalesSummaryListViewModel ();
			NamedNodeMap map = nodeList.item(i).getAttributes() ;

			sslvm.setName(map.getNamedItem("prod").getNodeValue());
			sslvm.setSalesAmount(Double.valueOf(map.getNamedItem("amt").getNodeValue()));
			summaryList.add(sslvm);

//	        System.out.println(nodes.item(i).getFirstChild().getNodeValue());
//	        prodCatDetList.add(prodCatalogDetailListViewModel);
		}

		request.setAttribute("salesSummaryListViewModel", summaryList);

	}

	public void populateCitiesData(HttpServletRequest request,String xml)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/cities/*") ;
		List<String> citiesList = new ArrayList<>(0);
		if(nodeList != null)
		{
			for(int i=0;i<nodeList.getLength();i++)
			{
				citiesList.add(nodeList.item(i).getAttributes().item(0).getNodeValue());
			}
		}
		request.setAttribute("citiesList",citiesList);
	}

	public void populateBankData(HttpServletRequest request, String xml) {

		NodeList nodeList = this.executeXPathQuery(xml, "//msg/banks/*") ;

		if(nodeList != null && nodeList.getLength() == 1) {

			NamedNodeMap nodeMap = nodeList.item(0).getAttributes();

			request.setAttribute("bname", nodeMap.getNamedItem(CommandFieldConstants.BANK_NAME_ATTRIBUTE).getNodeValue());
			request.setAttribute("isBank", toBoolean(nodeMap.getNamedItem("isBank").getNodeValue()));
			request.setAttribute(CommandFieldConstants.KEY_BANK_ID, Long.parseLong(nodeMap.getNamedItem("id").getNodeValue()));

//	        request.getSession(false).setAttribute("bname", nodeMap.getNamedItem(CommandFieldConstants.BANK_NAME_ATTRIBUTE).getNodeValue());
//	        request.getSession(false).setAttribute("isBank", toBoolean(nodeMap.getNamedItem("isBank").getNodeValue()));
//	        request.getSession(false).setAttribute(CommandFieldConstants.KEY_BANK_ID, Long.parseLong(nodeMap.getNamedItem("id").getNodeValue()));

			Node node = nodeList.item(0);

			if(node != null && node.hasChildNodes()) {

				NodeList childNodeList = node.getChildNodes();

				for (int j = 0; j < childNodeList.getLength(); j++) {

					NamedNodeMap maps = childNodeList.item(j).getAttributes();
					String smartMoneyAccount = maps.getNamedItem("id").getNodeValue();
					request.setAttribute(CommandFieldConstants.KEY_ACC_ID, smartMoneyAccount);
				}
			}
		}
	}

	@Deprecated
	public void populateBankInfo(HttpServletRequest request, String xml)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/banks/*") ;
		List<SmAcctInfoListViewModel> smAccountList = new ArrayList<SmAcctInfoListViewModel>();

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			SmAcctInfoListViewModel smAcctInfoListViewModel = new SmAcctInfoListViewModel ();

			NamedNodeMap map = nodeList.item(i).getAttributes() ;
			System.out.println("_----------- . > " + map.getNamedItem(CommandFieldConstants.BANK_NAME_ATTRIBUTE).getNodeValue() );
			smAcctInfoListViewModel.setBankName(map.getNamedItem(CommandFieldConstants.BANK_NAME_ATTRIBUTE).getNodeValue());
			smAcctInfoListViewModel.setIsBank(toBoolean(map.getNamedItem("isBank").getNodeValue()));
			smAcctInfoListViewModel.setBankId( Long.parseLong(map.getNamedItem("id").getNodeValue()));

			Node node = nodeList.item(i);

			if(node != null && node.hasChildNodes()) {

				NodeList childNodeList = node.getChildNodes();

				for (int j = 0; j < childNodeList.getLength(); j++) {

					NamedNodeMap maps = childNodeList.item(j).getAttributes();
					String smartMoneyAccount = maps.getNamedItem("id").getNodeValue();
					smAcctInfoListViewModel.setSmartMoneyAccountId(Long.parseLong(smartMoneyAccount));
				}
			}

			smAccountList.add(smAcctInfoListViewModel);
		}

		request.setAttribute("SmAcctInfoListViewModel", smAccountList);
	}

	public void populateAccountsByBankId(HttpServletRequest request, String xml)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/banks/*" ) ;
		List<SmAcctInfoListViewModel> smAccountList = new ArrayList<SmAcctInfoListViewModel>();
		Boolean isBank = false ;
		Boolean pinLevel = false ;

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			NamedNodeMap mapOfAttributes = nodeList.item(i).getAttributes() ;




//	        for (int j = 0; j < childNodes.getLength(); j++)
			{
				isBank = toBoolean(mapOfAttributes.getNamedItem("isBank").getNodeValue()) ;
				pinLevel = toBoolean(mapOfAttributes.getNamedItem("pinLevel").getNodeValue()) ;




				if( isBank )  // Case of bank
				{
					NodeList nodeListOfAccounts = this.executeXPathQuery(xml, "//msg/baccs/*" ) ;

					for( int countAccounts = 0; countAccounts < nodeListOfAccounts.getLength(); countAccounts++ )
					{
						SmAcctInfoListViewModel smAcctInfoListViewModel = new SmAcctInfoListViewModel ();
						smAcctInfoListViewModel.setIsBank(toBoolean(mapOfAttributes.getNamedItem("isBank").getNodeValue()));
						smAcctInfoListViewModel.setBankName(mapOfAttributes.getNamedItem(CommandFieldConstants.BANK_NAME_ATTRIBUTE).getNodeValue());
						smAcctInfoListViewModel.setBankId( Long.parseLong(mapOfAttributes.getNamedItem("id").getNodeValue()));
						smAcctInfoListViewModel.setPinLevel(toBoolean(mapOfAttributes.getNamedItem("pinLevel").getNodeValue()));

						NamedNodeMap mapOfAccountAttributes = nodeListOfAccounts.item(countAccounts).getAttributes() ;
						smAcctInfoListViewModel.setName(mapOfAccountAttributes.getNamedItem("accNo").getNodeValue());

						smAcctInfoListViewModel.setAccountId( "accNo=" + mapOfAccountAttributes.getNamedItem("accNo").getNodeValue()
								+ ";accType=" + mapOfAccountAttributes.getNamedItem("accType").getNodeValue()
								+ ";accSts=" + mapOfAccountAttributes.getNamedItem("accSts").getNodeValue()
								+ ";accCur=" + mapOfAccountAttributes.getNamedItem("accCur").getNodeValue()
						);

						smAcctInfoListViewModel.setIsCvvRequired(toBoolean(mapOfAccountAttributes.getNamedItem("cvv").getNodeValue()));
						smAcctInfoListViewModel.setIsMpinRequired(toBoolean(mapOfAccountAttributes.getNamedItem("mpin").getNodeValue()));
						smAcctInfoListViewModel.setIsTpinRequired(toBoolean(mapOfAccountAttributes.getNamedItem("tpin").getNodeValue()));
						smAcctInfoListViewModel.setChangePinRequired(toBoolean(mapOfAccountAttributes.getNamedItem("pinChReq").getNodeValue()));
						smAcctInfoListViewModel.setDefAccount(toBoolean(mapOfAccountAttributes.getNamedItem("isdef").getNodeValue()));
						request.setAttribute("bankName", smAcctInfoListViewModel.getBankName());

						smAccountList.add(smAcctInfoListViewModel);
					}
				}
				else
				{
					NodeList childNodes = nodeList.item(i).getChildNodes() ;

					for (int j = 0; j < childNodes.getLength(); j++)
					{
						NamedNodeMap childMap = childNodes.item(j).getAttributes() ;
						SmAcctInfoListViewModel smAcctInfoListViewModel = new SmAcctInfoListViewModel ();
						smAcctInfoListViewModel.setIsBank(toBoolean(mapOfAttributes.getNamedItem("isBank").getNodeValue()));
						smAcctInfoListViewModel.setBankName(mapOfAttributes.getNamedItem(CommandFieldConstants.BANK_NAME_ATTRIBUTE).getNodeValue());
						smAcctInfoListViewModel.setBankId( Long.parseLong(mapOfAttributes.getNamedItem("id").getNodeValue()));
						smAcctInfoListViewModel.setPinLevel(toBoolean(mapOfAttributes.getNamedItem("pinLevel").getNodeValue()));

						System.out.println("_----------- . > " + mapOfAttributes.getNamedItem(CommandFieldConstants.BANK_NAME_ATTRIBUTE).getNodeValue() );
						smAcctInfoListViewModel.setName(childMap.getNamedItem("nick").getNodeValue());
						smAcctInfoListViewModel.setSmartMoneyAccountId( Long.parseLong(childMap.getNamedItem("id").getNodeValue()));
						smAcctInfoListViewModel.setAccountId(childMap.getNamedItem("id").getNodeValue());
						smAcctInfoListViewModel.setChangePinRequired(toBoolean(childMap.getNamedItem("pinChReq").getNodeValue()));
						smAcctInfoListViewModel.setDefAccount(toBoolean(childMap.getNamedItem("isdef").getNodeValue()));
						smAcctInfoListViewModel.setIsCvvRequired(toBoolean(childMap.getNamedItem("cvv").getNodeValue()));
						smAcctInfoListViewModel.setIsMpinRequired(toBoolean(childMap.getNamedItem("mpin").getNodeValue()));
						smAcctInfoListViewModel.setIsTpinRequired(toBoolean(childMap.getNamedItem("tpin").getNodeValue()));
						request.setAttribute("bankName", smAcctInfoListViewModel.getBankName());
						smAccountList.add(smAcctInfoListViewModel);
					}
				}
			}
		}

		request.setAttribute("SmAcctInfoListViewModel", smAccountList);
		request.setAttribute("isBank", isBank);
		request.setAttribute("pinLevel", pinLevel);
	}


	public void populateProductsInfo(HttpServletRequest request, String xml, String supplierId)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/cat/prds/supp[@id=\"" + supplierId + "\"]/*") ;

		List<ProdCatalogDetailListViewModel> prodCatDetList = new ArrayList<ProdCatalogDetailListViewModel>();

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			ProdCatalogDetailListViewModel prodCatalogDetailListViewModel = new ProdCatalogDetailListViewModel();

			NamedNodeMap map = nodeList.item(i).getAttributes() ;
			System.out.println("_----------- . > " + nodeList.item(i).getFirstChild().getNodeValue() );
			prodCatalogDetailListViewModel.setProductName(nodeList.item(i).getFirstChild().getNodeValue());
			prodCatalogDetailListViewModel.setProductId( Long.parseLong(map.getNamedItem("id").getNodeValue()));

			if( map.getNamedItem("dfid") != null )
				prodCatalogDetailListViewModel.setDeviceFlowId(Long.parseLong(map.getNamedItem("dfid").getNodeValue()));

			if( map.getNamedItem("label") != null )
				prodCatalogDetailListViewModel.setBillServiceLabel(map.getNamedItem("label").getNodeValue());

			prodCatDetList.add(prodCatalogDetailListViewModel);
		}

		request.setAttribute("ProdCatalogDetailListViewModel", prodCatDetList);
	}

	public void populateServicesInfo(HttpServletRequest request, String xml)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/cat/srvs/*") ;
		List<ProdCatalogDetailListViewModel> prodCatDetList = new ArrayList<ProdCatalogDetailListViewModel>();

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			ProdCatalogDetailListViewModel prodCatalogDetailListViewModel = new ProdCatalogDetailListViewModel();

			NamedNodeMap map = nodeList.item(i).getAttributes() ;
			System.out.println("_----------- . > " + nodeList.item(i).getFirstChild().getNodeValue() );
			prodCatalogDetailListViewModel.setProductName(nodeList.item(i).getFirstChild().getNodeValue());
			prodCatalogDetailListViewModel.setProductId( Long.parseLong(map.getNamedItem("id").getNodeValue()));
			prodCatalogDetailListViewModel.setDeviceFlowId(Long.parseLong(map.getNamedItem("dfid").getNodeValue()));

			if( map.getNamedItem("label") != null )
				prodCatalogDetailListViewModel.setBillServiceLabel(map.getNamedItem("label").getNodeValue());

			prodCatDetList.add(prodCatalogDetailListViewModel);
		}

		request.setAttribute("ProdCatalogDetailListViewModel", prodCatDetList);
	}

	public void populateSuppliersInfo(HttpServletRequest request, String xml)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/cat/prds/*") ;
		List<ProdCatalogDetailListViewModel> prodCatDetList = new ArrayList<ProdCatalogDetailListViewModel>();

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			ProdCatalogDetailListViewModel prodCatalogDetailListViewModel = new ProdCatalogDetailListViewModel();

			NamedNodeMap map = nodeList.item(i).getAttributes() ;
			System.out.println("_----------- . > " + nodeList.item(i).getNodeValue() );
			prodCatalogDetailListViewModel.setSupplierId(Long.parseLong(map.getNamedItem("id").getNodeValue()));
			prodCatalogDetailListViewModel.setSupplierName(map.getNamedItem("name").getNodeValue());

			prodCatDetList.add(prodCatalogDetailListViewModel);
		}

		request.setAttribute("ProdCatalogDetailListViewModel", prodCatDetList);
	}


	public void populateAllPayServicesAndProducts( HttpServletRequest request, String xml )
	{
		this.populateAllPayServicesInfo(request, xml);
		this.populateAllPayProducts(request, xml);
	}

	private void populateAllPayServicesInfo(HttpServletRequest request, String xml)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/cat/srvs/*") ;

		List<List<ProdCatalogDetailListViewModel>> prodCatDetList = new ArrayList<List<ProdCatalogDetailListViewModel>>(0);

		List<ProdCatalogDetailListViewModel> prodCatDetSubList = new ArrayList<ProdCatalogDetailListViewModel>(4);

		for (int i = 0; i < nodeList.getLength(); i++) {
			NamedNodeMap map = nodeList.item(i).getAttributes();
			ProdCatalogDetailListViewModel prodCatalogDetailListViewModel = new ProdCatalogDetailListViewModel();

			String productId = map.getNamedItem("id").getNodeValue();

			prodCatalogDetailListViewModel.setProductIdEncrypt(encrypt(productId));
			prodCatalogDetailListViewModel.setProductId(Long.parseLong(productId));
			prodCatalogDetailListViewModel.setProductName(nodeList.item(i).getFirstChild().getNodeValue());
			prodCatalogDetailListViewModel.setDeviceFlowId(Long.parseLong(map.getNamedItem("dfid").getNodeValue()));
			prodCatalogDetailListViewModel.setServiceId(Long.parseLong(map.getNamedItem("SID").getNodeValue()));

			if( map.getNamedItem("label") != null )
				prodCatalogDetailListViewModel.setBillServiceLabel(map.getNamedItem("label").getNodeValue());

			prodCatDetSubList.add(prodCatalogDetailListViewModel);
			if( prodCatDetSubList.size() == 4 ) {
				prodCatDetList.add( prodCatDetSubList );
				prodCatDetSubList = new ArrayList<ProdCatalogDetailListViewModel>(4);
			}

		}

		//@author Naseer : Add remaining products(if any)
		if( !prodCatDetSubList.isEmpty() )
		{
			prodCatDetList.add( prodCatDetSubList );
		}
		logger.info("XML AllPay Services  = "+xml);
		logger.info("\nTotal AllPay Services Found = "+prodCatDetList.size() );

		request.setAttribute("Services", prodCatDetList);
	}

	private void populateAllPayProducts(HttpServletRequest request, String xml)
	{

		NodeList nodeList = this.executeXPathQuery(xml, "//msg/cat/prds/*") ;
		List<ProdCatalogDetailListViewModel> prodCatDetList = new ArrayList<ProdCatalogDetailListViewModel>();

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			NamedNodeMap map = nodeList.item(i).getAttributes() ;

			Long supplierId = Long.parseLong(map.getNamedItem("id").getNodeValue()) ;
			String supplierName = map.getNamedItem("name").getNodeValue() ;

			NodeList nodeListChild = nodeList.item(i).getChildNodes() ;

			for (int j = 0; j < nodeListChild.getLength(); j++)
			{

				ProdCatalogDetailListViewModel prodCatalogDetailListViewModel = new ProdCatalogDetailListViewModel();

				NamedNodeMap mapOfChildren = nodeListChild.item(j).getAttributes() ;

				prodCatalogDetailListViewModel.setSupplierId( supplierId );
				prodCatalogDetailListViewModel.setSupplierName( supplierName );
				prodCatalogDetailListViewModel.setProductName(nodeListChild.item(j).getFirstChild().getNodeValue());
				prodCatalogDetailListViewModel.setProductId( Long.parseLong(mapOfChildren.getNamedItem("id").getNodeValue()));
				prodCatalogDetailListViewModel.setServiceTypeId( Long.parseLong(mapOfChildren.getNamedItem("type").getNodeValue()));

				if( mapOfChildren.getNamedItem("dfid") != null && !mapOfChildren.getNamedItem("dfid").equals("") )
					prodCatalogDetailListViewModel.setDeviceFlowId(Long.parseLong(mapOfChildren.getNamedItem("dfid").getNodeValue()));

				if( mapOfChildren.getNamedItem("label") != null )
					prodCatalogDetailListViewModel.setBillServiceLabel(mapOfChildren.getNamedItem("label").getNodeValue());

				prodCatDetList.add(prodCatalogDetailListViewModel);

			}
		}
		logger.info("Total AllPay Products Found = "+prodCatDetList.size());

		request.setAttribute("Products", prodCatDetList);
	}


	public void populateFavoriteNumbersList(HttpServletRequest request, String xml)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/favs/*") ;
//		List<ProdCatalogDetailListViewModel> prodCatDetList = new ArrayList<ProdCatalogDetailListViewModel>();
		List<FavoriteNumbersModel> favNumberList = new ArrayList<FavoriteNumbersModel>();

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			FavoriteNumbersModel favoriteNumbersModel = new FavoriteNumbersModel();

			NamedNodeMap map = nodeList.item(i).getAttributes() ;
			System.out.println("_----------- . > " + nodeList.item(i).getNodeValue() );

			favoriteNumbersModel.setFavoriteNumbersId(Long.parseLong(map.getNamedItem("id").getNodeValue()));
			favoriteNumbersModel.setFavoriteNumber(map.getNamedItem("num").getNodeValue());
			favoriteNumbersModel.setName(nodeList.item(i).getFirstChild().getNodeValue());

			favNumberList.add(favoriteNumbersModel);
		}

		request.setAttribute("FavoriteNumbersModel", favNumberList);
	}


	public void populateAllPayLoginResponse(HttpServletRequest request, String xml) {

		populateTransactionSummary(request, xml);
		populateBankInfo(request, xml);
		populateBankData(request, xml);
	}



	public void populateFavoriteNumber(HttpServletRequest request, String xml, String favoriteNumberId)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/favs/fav[@id=\"" + favoriteNumberId + "\"]") ;

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			FavoriteNumbersModel favoriteNumbersModel = new FavoriteNumbersModel();
			NamedNodeMap map = nodeList.item(i).getAttributes();

			favoriteNumbersModel.setFavoriteNumbersId(Long.parseLong(map.getNamedItem("id").getNodeValue()));
			favoriteNumbersModel.setFavoriteNumber(map.getNamedItem("num").getNodeValue());
			favoriteNumbersModel.setName(nodeList.item(i).getFirstChild().getNodeValue());

			request.setAttribute("fnid", favoriteNumbersModel.getFavoriteNumbersId());
			request.setAttribute("num", favoriteNumbersModel.getFavoriteNumber());
			request.setAttribute("tag", favoriteNumbersModel.getName());

			request.setAttribute("FavoriteNumbersModel", favoriteNumbersModel);
		}
	}


	public void populateMessage(HttpServletRequest request, String xml)
	{
		if(null != xml && xml.length()>0) {
			NodeList nodeList = this.executeXPathQuery(xml, "//msg/mesgs/*");

			for(int i = 0; i < nodeList.getLength(); i++) {
				request.setAttribute("mesg", nodeList.item(0).getFirstChild().getNodeValue());
			}
		}
	}



	public void populateMiniStatementData(HttpServletRequest request, String xml)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/params/STMTS/*") ;
		String [] statements = new String[nodeList.getLength()];

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			statements[i] = nodeList.item(i).getFirstChild().getNodeValue() ;
		}

		request.setAttribute("MiniStatements", statements);
	}

	public void populateTransactionLogData(HttpServletRequest request, String xml)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/trans/*") ;
		ArrayList<FetchTransactionListViewModel> transactionsList = new ArrayList<FetchTransactionListViewModel>();

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			NamedNodeMap map = nodeList.item(i).getAttributes() ;
			FetchTransactionListViewModel transactionModel = new FetchTransactionListViewModel();

			transactionModel.setBankResponseCode(map.getNamedItem("bcode").getNodeValue());
			transactionModel.setTranCode(map.getNamedItem("code").getNodeValue());
			transactionModel.setTransactionTypeId( Long.parseLong(map.getNamedItem("type").getNodeValue()));
			transactionModel.setTransactionDateTime( map.getNamedItem("datef").getNodeValue() + " " + map.getNamedItem("timef").getNodeValue() );
			transactionModel.setName( map.getNamedItem("pmode").getNodeValue());
			transactionModel.setProductName( map.getNamedItem("prod").getNodeValue());
			transactionModel.setSupplierName( map.getNamedItem("supp").getNodeValue());
			transactionModel.setTranAmount(Double.parseDouble( map.getNamedItem("amt").getNodeValue() ));

			if( map.getNamedItem("trckid") != null )
				transactionModel.setSuppResponseCode( map.getNamedItem("trckid").getNodeValue());
			if( map.getNamedItem("helpLine") != null )
				transactionModel.setHelpLine( map.getNamedItem("helpLine").getNodeValue());
			if( map.getNamedItem("mNo") != null )
				transactionModel.setNotificationMobileNo( map.getNamedItem("mNo").getNodeValue());


			transactionsList.add(transactionModel);

//	        System.out.println(nodes.item(i).getFirstChild().getNodeValue());
//	        prodCatDetList.add(prodCatalogDetailListViewModel);
		}

		request.setAttribute("FetchTransactionListViewModel", transactionsList);
	}

	public void populateMiniStatementAgentData(HttpServletRequest request, String xml) {

		NodeList nodeList = this.executeXPathQuery(xml, "//msg/trans/*") ;

		List<TransactionDetailPortalListModel> transactionDetailPortalListModelList = new ArrayList<TransactionDetailPortalListModel>();

		for (int i = 0; i < nodeList.getLength(); i++) {

			NamedNodeMap map = nodeList.item(i).getAttributes() ;

			String transactionId = map.getNamedItem("trid").getNodeValue();
			String productName = map.getNamedItem("prod").getNodeValue();
			String productId = map.getNamedItem("prodid").getNodeValue();
			String amount = map.getNamedItem("amt").getNodeValue();
			String commission = map.getNamedItem("cmsn").getNodeValue();
			String exclusiveCharges = map.getNamedItem("excrhge").getNodeValue();
			String inclusiveCharges = map.getNamedItem("incrhge").getNodeValue();
			String dateString = map.getNamedItem("date").getNodeValue();
			String agentOneId = map.getNamedItem("AGENT_1_ID").getNodeValue();
			String agentTwoId = map.getNamedItem("AGENT_2_ID").getNodeValue();
			String agentOneCommission = map.getNamedItem("AGENT_1_COMM").getNodeValue();
			String agentTwoCommission = map.getNamedItem("AGENT_2_COMM").getNodeValue();
			String totalAmount = map.getNamedItem("TAMT").getNodeValue();

			Date date = null;

			try {

				if(!dateString.equals("")) {

					DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
					date = format.parse(dateString);
				}

			} catch (ParseException e) {
				logger.error(e);
			}

			TransactionDetailPortalListModel transactionDetailPortalListModel = new TransactionDetailPortalListModel();

			if(!transactionId.equals("")) {
				transactionDetailPortalListModel.setTransactionNo(transactionId);
			}

			if(!amount.equals("")) {
				transactionDetailPortalListModel.setTransactionAmount(Double.valueOf(amount));
			}

			transactionDetailPortalListModel.setProductName(productName);

			if(!productId.equals("")) {
				transactionDetailPortalListModel.setProductId(Long.valueOf(productId));
			}
			if(!commission.equals("")) {
				transactionDetailPortalListModel.setAgentCommission(Double.valueOf(commission));
			}
			if(!exclusiveCharges.equals("")) {
				transactionDetailPortalListModel.setExclusiveCharges(Double.valueOf(exclusiveCharges));
			}
			if(!inclusiveCharges.equals("")) {
				transactionDetailPortalListModel.setInclusiveCharges(Double.valueOf(inclusiveCharges));
			}

			if(date != null) {
				transactionDetailPortalListModel.setCreatedOn(date);
			}
			if(!agentOneId.equals("")) {
				transactionDetailPortalListModel.setAgent1Id(agentOneId);
			}
			if(!agentTwoId.equals("")) {
				transactionDetailPortalListModel.setAgent2Id(agentTwoId);
			}
			if(!agentOneCommission.equals("")) {
				transactionDetailPortalListModel.setAgentCommission(Double.valueOf(agentOneCommission));
			}
			if(!agentTwoCommission.equals("")) {
				//transactionDetailPortalListModel.setAgent2Commission(Double.valueOf(agentTwoCommission));
			}
			if(!totalAmount.equals("")) {
				transactionDetailPortalListModel.setTotalAmount(Double.valueOf(totalAmount));
			}

			transactionDetailPortalListModelList.add(transactionDetailPortalListModel);
		}

		request.setAttribute("transactionDetailPortalListModelList", transactionDetailPortalListModelList);
	}


	public void populateTransactionSummary(HttpServletRequest request, String xml)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/trans/*") ;
		FetchTransactionListViewModel transactionModel = new FetchTransactionListViewModel();

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			NamedNodeMap map = nodeList.item(i).getAttributes() ;


			transactionModel.setBankResponseCode(map.getNamedItem("bcode").getNodeValue());
			transactionModel.setTranCode(map.getNamedItem("code").getNodeValue());
			transactionModel.setTransactionTypeId( Long.parseLong(map.getNamedItem("type").getNodeValue()));
			transactionModel.setTransactionDateTime( map.getNamedItem("datef").getNodeValue() + " " + map.getNamedItem("timef").getNodeValue() );
			transactionModel.setName( map.getNamedItem("pmode").getNodeValue());
			transactionModel.setProductName( map.getNamedItem("prod").getNodeValue());
			transactionModel.setSupplierName( map.getNamedItem("supp").getNodeValue());
			transactionModel.setTranAmount(Double.parseDouble( map.getNamedItem("amt").getNodeValue() ));
			if (map.getNamedItem("TPAMF") != null)
				transactionModel.setServiceCharges(map.getNamedItem("TPAMF").getNodeValue() );

			if( map.getNamedItem("trckid") != null )
				transactionModel.setSuppResponseCode( map.getNamedItem("trckid").getNodeValue());
			if( map.getNamedItem("helpLine") != null )
				transactionModel.setHelpLine( map.getNamedItem("helpLine").getNodeValue());
			if( map.getNamedItem("mNo") != null )
				transactionModel.setNotificationMobileNo( map.getNamedItem("mNo").getNodeValue());
			if( map.getNamedItem("TAMTF") != null )
				transactionModel.setTotalTransactionAmount( map.getNamedItem("TAMTF").getNodeValue());

//	    	transactionsList.add(transactionModel);

//	        System.out.println(nodes.item(i).getFirstChild().getNodeValue());
//	        prodCatDetList.add(prodCatalogDetailListViewModel);
		}

		request.setAttribute("FetchTransactionListViewModel", transactionModel);
	}

	public void populateAllPayTransactionSummary(HttpServletRequest request, String xml)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/trans/*") ;
		FetchTransactionListViewModel transactionModel = new FetchTransactionListViewModel();

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			NamedNodeMap map = nodeList.item(i).getAttributes() ;

			if(null != map.getNamedItem("bcode"))
			{
				transactionModel.setBankResponseCode(map.getNamedItem("bcode").getNodeValue());
			}
			transactionModel.setTranCode(map.getNamedItem("code").getNodeValue());
			transactionModel.setTransactionTypeId( Long.parseLong(map.getNamedItem("type").getNodeValue()));
			transactionModel.setTransactionDateTime( map.getNamedItem("datef").getNodeValue() );
//	    	transactionModel.setTransaction(map.getNamedItem("timef").getNodeValue());
			if(null != map.getNamedItem("pmode"))
			{
				transactionModel.setName( map.getNamedItem("pmode").getNodeValue());
			}
			transactionModel.setProductName( map.getNamedItem("prod").getNodeValue());
			transactionModel.setSupplierName( map.getNamedItem("supp").getNodeValue());
			transactionModel.setTranAmount(Double.parseDouble( map.getNamedItem("amt").getNodeValue() ));

			if( map.getNamedItem("trckid") != null )
				transactionModel.setSuppResponseCode( map.getNamedItem("trckid").getNodeValue());
			if( map.getNamedItem("helpLine") != null )
				transactionModel.setHelpLine( map.getNamedItem("helpLine").getNodeValue());
			if( map.getNamedItem("mNo") != null )
				transactionModel.setNotificationMobileNo( map.getNamedItem("mNo").getNodeValue());

			if(null != map.getNamedItem("timef"))
			{
				request.setAttribute(XMLConstants.ATTR_TRN_TIMEF, map.getNamedItem("timef").getNodeValue());
			}


//	    	transactionsList.add(transactionModel);

//	        System.out.println(nodes.item(i).getFirstChild().getNodeValue());
//	        prodCatDetList.add(prodCatalogDetailListViewModel);
		}

		request.setAttribute("FetchTransactionListViewModel", transactionModel);
	}


	public void populateErrorMessages(HttpServletRequest request, String xml)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/errors/*") ;
		String errors = "";

		for (int i = 0; i < nodeList.getLength(); i++)
		{

//	    	errors += nodeList.item(i).getFirstChild().getNodeValue() + " " ;

			Node item = nodeList.item(i);

			if(item != null && item.hasChildNodes()) {

				Node firstChild = item.getFirstChild();

				if(firstChild != null) {

					errors += firstChild.getNodeValue() + " ";
					logger.info("\nError - >  " + errors );
				}
			}
		}

		if("".equals(errors) || null == errors) {
			errors = "An error has occurred, Please try later.";
			logger.error("populateErrorMessages () Error XML : \n"+xml);
		}

		request.setAttribute("errors", errors.trim());
	}


	private NodeList executeXPathQuery( String xml, String xpathExpression )
	{
		Object result = null;

		try
		{

			domFactory.setNamespaceAware(true); // never forget this!
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xml)));

			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(xpathExpression);

			result = expr.evaluate(doc, XPathConstants.NODESET);
		}
		catch (XPathExpressionException e)
		{
			e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}


		return (NodeList) result;
	}





	private static boolean toBoolean( String strValue )
	{
		if( strValue != null )
			return strValue.equals("1") ? true : false ;

		return false;
	}

	private static Date toDate( String dateStr )
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		try
		{
			Date today = df.parse(dateStr);
//            System.out.println("Today = " + df.format(today));
			return today;
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public void populateTransactionInfoForOpenAPI(WebServiceVO webServiceVO, String xml) {

		logger.info("populateTransactionInfoForOpenAPI(...) "+xml);
		NodeList nodeList = this.executeXPathQuery(xml, "/params/*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			NodeList childNodeList = nodeList.item(i).getChildNodes() ;
			for (int j = 0; j < childNodeList.getLength(); j++) {
				NodeList nList = childNodeList.item(j).getChildNodes();
				if(nList != null && nList.getLength() == 0)
				{
					NamedNodeMap namedNodeMap = childNodeList.item(0).getAttributes();
					if(namedNodeMap != null)
					{
						Double totalAmount = 0.0D;
						Double trxAmount = 0.0D;
						if(namedNodeMap.getNamedItem("TXAM") != null && namedNodeMap.getNamedItem("TXAM").getNodeValue().equals(""))
							trxAmount = Double.parseDouble(namedNodeMap.getNamedItem("TXAM").getNodeValue());
						if(namedNodeMap.getNamedItem("TAMT") != null && !namedNodeMap.getNamedItem("TAMT").getNodeValue().equals(""))
						{
							totalAmount = Double.parseDouble(namedNodeMap.getNamedItem("TAMT").getNodeValue());
							webServiceVO.setTerminalId(totalAmount.toString());
						}
						Double charges = totalAmount - trxAmount;
						if(namedNodeMap.getNamedItem("TPAM") != null && !namedNodeMap.getNamedItem("TPAM").getNodeValue().equals(""))
						{
							totalAmount = Double.parseDouble(namedNodeMap.getNamedItem("TPAM").getNodeValue());
						}
						if(namedNodeMap.getNamedItem("TRXID") != null && !namedNodeMap.getNamedItem("TRXID").getNodeValue().equals(""))
							webServiceVO.setTerminalId(namedNodeMap.getNamedItem("TRXID").getNodeValue());
						else if(namedNodeMap.getNamedItem("ID") != null && !namedNodeMap.getNamedItem("ID").getNodeValue().equals(""))
							webServiceVO.setTerminalId(namedNodeMap.getNamedItem("TRXID").getNodeValue());
						if(namedNodeMap.getNamedItem("TIMEF") !=null && !namedNodeMap.getNamedItem("TIMEF").getNodeValue().equals("")
								&& namedNodeMap.getNamedItem("DATEF") !=null && !namedNodeMap.getNamedItem("DATEF").getNodeValue().equals(""))
							webServiceVO.setDateTime(namedNodeMap.getNamedItem("DATEF").getNodeValue()+ " " + namedNodeMap.getNamedItem("TIMEF").getNodeValue());
					}
				}

				for (int k = 0; k < nList.getLength(); k++) {
					Node node = nList.item(k);
					if(node != null) {
						Node parentNode = node.getParentNode();
						NamedNodeMap namedNodeMap = null;
						if(parentNode != null)
							namedNodeMap = parentNode.getAttributes();
						if(namedNodeMap != null && namedNodeMap.item(0) != null) {
							String parentNodeValue = namedNodeMap.item(0).getNodeValue();
							String nodeValue = node.getNodeValue();
							logger.info("populateProductPurchase(...) "+parentNodeValue+" = "+nodeValue);
						}
					}
				}
			}

		}
	}


	/**
	 * @param request
	 * @param xml
	 * @author kashif.bashir
	 */
	public void populateProductPurchase(HttpServletRequest request, String xml) {

		logger.info("populateProductPurchase(...) "+xml);

		NodeList nodeList = this.executeXPathQuery(xml, "//msg/*");

		for (int i = 0; i < nodeList.getLength(); i++) {

			NodeList childNodeList = nodeList.item(i).getChildNodes() ;

			for (int j = 0; j < childNodeList.getLength(); j++) {

				NodeList nList = childNodeList.item(j).getChildNodes();
				if(nList != null && nList.getLength() == 0)
				{
					NamedNodeMap namedNodeMap = childNodeList.item(0).getAttributes();
					if(namedNodeMap != null)
					{
						Double totalAmount = 0.0D;
						Double trxAmount = 0.0D;
						if(namedNodeMap.getNamedItem("TXAM") != null && namedNodeMap.getNamedItem("TXAM").getNodeValue().equals(""))
							trxAmount = Double.parseDouble(namedNodeMap.getNamedItem("TXAM").getNodeValue());
						if(namedNodeMap.getNamedItem("TAMT") != null && !namedNodeMap.getNamedItem("TAMT").getNodeValue().equals(""))
						{
							totalAmount = Double.parseDouble(namedNodeMap.getNamedItem("TAMT").getNodeValue());
							request.setAttribute("TAMT",totalAmount.toString());
						}
						Double charges = totalAmount - trxAmount;
						request.setAttribute("CAMT",charges.toString());
						if(namedNodeMap.getNamedItem("TPAM") != null && !namedNodeMap.getNamedItem("TPAM").getNodeValue().equals(""))
						{
							totalAmount = Double.parseDouble(namedNodeMap.getNamedItem("TPAM").getNodeValue());
							request.setAttribute("TPAM",totalAmount.toString());
						}
						if(namedNodeMap.getNamedItem("TRXID") != null && !namedNodeMap.getNamedItem("TRXID").getNodeValue().equals(""))
							request.setAttribute("TRXID",namedNodeMap.getNamedItem("TRXID").getNodeValue());
						else if(namedNodeMap.getNamedItem("ID") != null && !namedNodeMap.getNamedItem("ID").getNodeValue().equals(""))
							request.setAttribute("TRXID",namedNodeMap.getNamedItem("ID").getNodeValue());
						if(namedNodeMap.getNamedItem("TIMEF") !=null && !namedNodeMap.getNamedItem("TIMEF").getNodeValue().equals("")
								&& namedNodeMap.getNamedItem("DATEF") !=null && !namedNodeMap.getNamedItem("DATEF").getNodeValue().equals(""))
							request.setAttribute("TIMEF",namedNodeMap.getNamedItem("DATEF").getNodeValue()+ " " + namedNodeMap.getNamedItem("TIMEF").getNodeValue());
					}
				}

				for (int k = 0; k < nList.getLength(); k++) {

					Node node = nList.item(k);

					if(node != null) {
						Node parentNode = node.getParentNode();
						NamedNodeMap namedNodeMap = null;
						if(parentNode != null)
							namedNodeMap = parentNode.getAttributes();
						if(namedNodeMap != null && namedNodeMap.item(0) != null) {

							String parentNodeValue = namedNodeMap.item(0).getNodeValue();
							String nodeValue = node.getNodeValue();

							logger.info("populateProductPurchase(...) "+parentNodeValue+" = "+nodeValue);

							request.setAttribute(parentNodeValue, nodeValue);
						}
					}
				}
			}

		}
	}

	public WebServiceVO populateErrorMessagesForOpenAPI(WebServiceVO webServiceVO, String xml)
	{
		NodeList nodeList = this.executeXPathQuery(xml, "//msg/errors/*") ;
		String errors = "";
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			Node item = nodeList.item(i);
			if(item != null && item.hasChildNodes()) {
				Node firstChild = item.getFirstChild();
				if(firstChild != null) {
					errors += firstChild.getNodeValue() + " ";
					logger.info("\nError Description - >  " + errors );
				}
			}
		}

		if("".equals(errors) || null == errors) {
			errors = "An error has occurred, Please try later.";
			logger.error("populateErrorMessages () Error XML : \n"+xml);
		}
		webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
		webServiceVO.setResponseCodeDescription(errors);

		return webServiceVO;
	}

}