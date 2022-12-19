package com.inov8.microbank.hra.airtimetopup.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.hra.airtimetopup.model.ConversionRateModel;
import com.inov8.microbank.hra.airtimetopup.vo.AirTimeTopUpVO;

import java.util.List;

public interface ConversionRateDAO extends BaseDAO<ConversionRateModel, Long> {
    List<ConversionRateModel> getAll();

    List<ConversionRateModel> findAllRates(ConversionRateModel conversionRateModel);

    List<ConversionRateModel> findAllConversionRates(ConversionRateModel conversionRateModel) throws FrameworkCheckedException;

    void save(ConversionRateModel conversionRateModel);

    void update(ConversionRateModel conversionRateModel, String[] params);

    AirTimeTopUpVO getRateForSpecificRemitance(Long remitanceInfoId) throws FrameworkCheckedException;
}
