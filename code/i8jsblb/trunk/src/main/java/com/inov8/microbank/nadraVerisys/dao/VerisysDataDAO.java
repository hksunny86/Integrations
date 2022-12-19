package com.inov8.microbank.nadraVerisys.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.nadraVerisys.model.VerisysDataModel;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * Created by Attique on 10/31/2017.
 */
public interface VerisysDataDAO extends BaseDAO<VerisysDataModel,Long> {

    VerisysDataModel loadVerisysDataModel(String cnic);
    VerisysDataModel loadVerisysDataModel(Long appUserId);

    List<VerisysDataModel> getUrduDataForTransliteration() throws FrameworkCheckedException;

    void updateVerisysDataToTransleted(List<Long> verisysDataIds) throws FrameworkCheckedException;

    void saveNadraData(VerisysDataModel model) throws FrameworkCheckedException;

    void addToBatch(Map<String, Object[]> map) throws Exception;
     Long getAddressId(Long customerId) throws Exception;
    Long getAccountInfoId(Long customerId) throws Exception;
    Long getCustomer(Long appUserId) throws Exception;

    void markClosedByAppUserId(Long appUserId) throws DataAccessException;

    Long getAcoountHolderId(String cnic) throws Exception;
    void initializeBatch() throws Exception;

    void closeBatch() throws Exception;
}
