package com.inov8.microbank.hra.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.hra.airtimetopup.dao.ConversionRateDAO;
import com.inov8.microbank.hra.airtimetopup.dao.RemitanceInfoDAO;
import com.inov8.microbank.hra.airtimetopup.model.ConversionRateModel;
import com.inov8.microbank.hra.airtimetopup.model.HRARemitanceInfoModel;
import com.inov8.microbank.hra.airtimetopup.vo.AirTimeTopUpVO;
import com.inov8.microbank.hra.paymtnc.dao.PayMtncRequestDAO;
import com.inov8.microbank.hra.paymtnc.model.PayMtncRequestModel;
import org.hibernate.criterion.MatchMode;

import java.util.List;

public class HRAManagerImpl implements HRAManager {

    private PayMtncRequestDAO payMtncRequestDAO;
    private ConversionRateDAO conversionRateDAO;
    private RemitanceInfoDAO remitanceInfoDAO;

    public void setPayMtncRequestDAO(PayMtncRequestDAO payMtncRequestDAO) {
        this.payMtncRequestDAO = payMtncRequestDAO;
    }

    @Override
    public PayMtncRequestModel findPayMtncModelByPrimaryKay(Long primaryKey) throws FrameworkCheckedException {
        return payMtncRequestDAO.findByPrimaryKey(primaryKey);
    }

    @Override
    public void saveOrUpdatePayMtncRequest(PayMtncRequestModel model) throws FrameworkCheckedException {
        payMtncRequestDAO.saveOrUpdate(model);
    }

    @Override
    public SearchBaseWrapper searchPayMtncRequests(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        PayMtncRequestModel model = (PayMtncRequestModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<PayMtncRequestModel> customList = payMtncRequestDAO.findByExample(model,searchBaseWrapper.getPagingHelperModel(),
                searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
        searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

    @Override
    public Double getConversionRateForTopUp() throws FrameworkCheckedException {
        return null;
    }

    @Override
    public ConversionRateModel saveOrUpdateConversionRateModel(ConversionRateModel conversionRateModel) throws FrameworkCheckedException {
        conversionRateModel = conversionRateDAO.saveOrUpdate(conversionRateModel);
        return conversionRateModel;
    }

    @Override
    public List<ConversionRateModel> findAllOpenConversionRates(ConversionRateModel conversionRateModel) throws FrameworkCheckedException {
        return conversionRateDAO.findAllRates(conversionRateModel);
    }

    @Override
    public List<ConversionRateModel> findAllConversionRates() throws FrameworkCheckedException {
        List<ConversionRateModel> list = conversionRateDAO.findAllConversionRates(null);
        return list;
    }

    @Override
    public void saveOrUpdateCollection(List<ConversionRateModel> list1) throws FrameworkCheckedException {
        conversionRateDAO.saveOrUpdateCollection(list1);
    }

    @Override
    public void saveOrUpdateOpenConversionRates(List<ConversionRateModel> list) throws FrameworkCheckedException {
        conversionRateDAO.saveOrUpdateCollection(list);
    }

    @Override
    public List<HRARemitanceInfoModel> getActiveRemitances() throws FrameworkCheckedException {
        return remitanceInfoDAO.getActiveRemitances();
    }

    @Override
    public AirTimeTopUpVO getRateForSpecificRemitance(Long remitanceInfoId) throws FrameworkCheckedException {
        return conversionRateDAO.getRateForSpecificRemitance(remitanceInfoId);
    }

    @Override
    public void saveOrUpdateTopUpCollectionRequiresNewTransaction(List<HRARemitanceInfoModel> list) throws FrameworkCheckedException {
        remitanceInfoDAO.saveOrUpdateCollection(list);
    }

    public void setConversionRateDAO(ConversionRateDAO conversionRateDAO) {
        this.conversionRateDAO = conversionRateDAO;
    }

    public void setRemitanceInfoDAO(RemitanceInfoDAO remitanceInfoDAO) {
        this.remitanceInfoDAO = remitanceInfoDAO;
    }
}
