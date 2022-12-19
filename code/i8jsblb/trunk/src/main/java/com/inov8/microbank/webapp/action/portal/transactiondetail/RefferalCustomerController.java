package com.inov8.microbank.webapp.action.portal.transactiondetail;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.Data;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.MerchantDiscountCardModel;
import com.inov8.microbank.common.model.RefferalCustomerModel;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.dao.safrepo.MerchantDiscountCardDAO;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import org.hibernate.criterion.MatchMode;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

public class RefferalCustomerController extends BaseFormSearchController {

    private ReferenceDataManager referenceDataManager;
    private ESBAdapter esbAdapter;


    public RefferalCustomerController() {
        setCommandClass(RefferalCustomerModel.class);
        setCommandName("RefferalCustomerController");
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                                    PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {

        RefferalCustomerModel refferalCustomerModel = (RefferalCustomerModel) o;
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel();
        ArrayList<DateRangeHolderModel> dateRangeHolderModels = new ArrayList<>();
        dateRangeHolderModels.add(dateRangeHolderModel);
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);


        if (linkedHashMap.isEmpty()) {
            linkedHashMap.put("sender_mobile", SortingOrder.DESC);

        }

        searchBaseWrapper.setSortingOrderMap(linkedHashMap);

        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        esbAdapter = new ESBAdapter();
        String transmissionDateTime = new SimpleDateFormat("yyyyMMddHHss").format(new Date());
        String stan = String.valueOf((new Random().nextInt(90000000)));
        requestVO = esbAdapter.prepareRefferalCustomerRequest(I8SBConstants.RequestType_REFFERAL_CUSTOMER);
        requestVO.setMobileNumber(refferalCustomerModel.getSender_mobile());

        SwitchWrapper sWrapper = new SwitchWrapperImpl();
        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
        sWrapper = esbAdapter.makeI8SBCall(sWrapper);
        ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
        responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
        if (!responseVO.getResponseCode().equals("I8SB-200")) {
            throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
        }

        ArrayList<?> data = new ArrayList<>();

        CustomList<RefferalCustomerModel> list = new CustomList<>();
        ArrayList<RefferalCustomerModel> refferalCustomerModelsLi=new ArrayList<>();
        data = (ArrayList<?>) responseVO.getCollectionOfList().get("asa");
        if (data != null) {
            List<?> associatedAccountList = data;
            for (int i = 0; i < associatedAccountList.size(); i++) {
                refferalCustomerModel = new RefferalCustomerModel();
                refferalCustomerModel.setReceiver_mobile(((Data) associatedAccountList.get(i)).getReceiver_mobile());
                refferalCustomerModel.setSender_mobile(((Data) associatedAccountList.get(i)).getSender_mobile());
                refferalCustomerModel.setReferral_Date(((Data) associatedAccountList.get(i)).getReferral_Date());
                refferalCustomerModel.setReceiver_Referral_Name(((Data) associatedAccountList.get(i)).getReceiver_Referral_Name());
                refferalCustomerModel.setReceiver_Status(((Data) associatedAccountList.get(i)).getReceiver_Status());
                refferalCustomerModel.setReceiver_Tranx_Amount(((Data) associatedAccountList.get(i)).getReceiver_Tranx_Amount());
                refferalCustomerModel.setDeleted(((Data) associatedAccountList.get(i)).getDeleted());
                refferalCustomerModel.setCreditPayment_Amount(((Data) associatedAccountList.get(i)).getCreditPayment_Amount());
                refferalCustomerModel.setReceiver_Debit_Amount(((Data) associatedAccountList.get(i)).getReceiver_Debit_Amount());
                refferalCustomerModel.setTranx_Date(((Data) associatedAccountList.get(i)).getTranx_Date());
                refferalCustomerModel.setTranx_Type(((Data) associatedAccountList.get(i)).getTranx_Type());
                refferalCustomerModel.setTranx_From_Referre_Account(((Data) associatedAccountList.get(i)).getTranx_From_Referre_Account());
                refferalCustomerModel.setTranx_To_Account_By_Referre(((Data) associatedAccountList.get(i)).getTranx_To_Account_By_Referre());
                refferalCustomerModel.setReceiver_Signup_Name(((Data) associatedAccountList.get(i)).getReceiver_Signup_Name());
                refferalCustomerModel.setSignup_Date(((Data) associatedAccountList.get(i)).getSignup_Date());
                refferalCustomerModelsLi.add(refferalCustomerModel);

            }
            list.setResultsetList(refferalCustomerModelsLi);
        }
        pagingHelperModel.setTotalRecordsCount(refferalCustomerModelsLi.size());

        return new ModelAndView(getSuccessView(), "reqList", list.getResultsetList());

    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }


    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }
}
