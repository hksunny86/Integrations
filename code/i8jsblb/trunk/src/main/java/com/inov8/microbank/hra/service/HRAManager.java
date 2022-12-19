package com.inov8.microbank.hra.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.hra.airtimetopup.model.ConversionRateModel;
import com.inov8.microbank.hra.airtimetopup.model.HRARemitanceInfoModel;
import com.inov8.microbank.hra.airtimetopup.vo.AirTimeTopUpVO;
import com.inov8.microbank.hra.paymtnc.model.PayMtncRequestModel;

import java.util.List;

public interface HRAManager {

    PayMtncRequestModel findPayMtncModelByPrimaryKay(Long primaryKey) throws FrameworkCheckedException;

    void saveOrUpdatePayMtncRequest(PayMtncRequestModel model) throws FrameworkCheckedException;

    SearchBaseWrapper searchPayMtncRequests(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    Double getConversionRateForTopUp() throws FrameworkCheckedException;

    //Abu Bakar
    public ConversionRateModel saveOrUpdateConversionRateModel(ConversionRateModel conversionRateModel) throws FrameworkCheckedException;
    public List<ConversionRateModel> findAllOpenConversionRates (ConversionRateModel conversionRateModel) throws FrameworkCheckedException;
    public List<ConversionRateModel> findAllConversionRates () throws FrameworkCheckedException;
 //saveorupdate
    void saveOrUpdateCollection (List<ConversionRateModel> list1) throws FrameworkCheckedException;
    void saveOrUpdateOpenConversionRates(List<ConversionRateModel> list) throws FrameworkCheckedException;

    List<HRARemitanceInfoModel> getActiveRemitances() throws FrameworkCheckedException;

    AirTimeTopUpVO getRateForSpecificRemitance(Long remitanceInfoId) throws FrameworkCheckedException;

    void saveOrUpdateTopUpCollectionRequiresNewTransaction(List<HRARemitanceInfoModel> list) throws FrameworkCheckedException;

}
