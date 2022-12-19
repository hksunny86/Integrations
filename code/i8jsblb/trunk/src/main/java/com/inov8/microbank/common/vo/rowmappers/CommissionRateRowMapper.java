package com.inov8.microbank.common.vo.rowmappers;

import com.inov8.microbank.common.vo.product.CommissionRateVO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Inov8 on 9/21/2019.
 */
public class CommissionRateRowMapper implements org.springframework.jdbc.core.RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        CommissionRateVO vo = new CommissionRateVO();
        vo.setTransactionTypeId(resultSet.getLong("TRANSACTION_TYPE_ID"));
        vo.setStakeholderBankInfoId(resultSet.getLong("STAKEHOLDER_BANK_INFO_ID"));
        vo.setSegmentId(resultSet.getLong("SEGMENT_ID"));
        vo.setProductId(resultSet.getLong("PRODUCT_ID"));
        vo.setPaymentModeId(resultSet.getLong("PAYMENT_MODE_ID"));
        vo.setCommissionTypeId(resultSet.getLong("COMMISSION_TYPE_ID"));
        vo.setCommissionStakeholderId(resultSet.getLong("COMMISSION_STAKEHOLDER_ID"));
        vo.setCommissionReasonId(resultSet.getLong("COMMISSION_REASON_ID"));
        vo.setCreatedByAppUserId(resultSet.getLong("CREATED_BY"));
        vo.setUpdatedByAppUserId(resultSet.getLong("UPDATED_BY"));
        vo.setDeviceTypeId(resultSet.getLong("DEVICE_TYPE_ID"));
        vo.setDistributorId(resultSet.getLong("DISTRIBUTOR_ID"));
        vo.setMnoId(resultSet.getLong("SERVICE_OP_ID"));
        vo.setCommissionRateId(resultSet.getLong("COMMISSION_RATE_ID"));
        vo.setRate(resultSet.getDouble("RATE"));
        vo.setFromDate(resultSet.getDate("FROM_DATE"));
        vo.setToDate(resultSet.getDate("TO_DATE"));
        vo.setActive(resultSet.getLong("IS_ACTIVE"));
        vo.setDescription(resultSet.getString("DESCRIPTION"));
        vo.setComments(resultSet.getString("COMMENTS"));
        vo.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        vo.setCreatedOn(resultSet.getDate("CREATED_ON"));
        vo.setVersionNo(resultSet.getInt("VERSION_NO"));
        vo.setRangeStarts(resultSet.getDouble("RANGE_STARTS"));
        vo.setRangeEnds(resultSet.getDouble("RANGE_ENDS"));
        vo.setExclusiveFixAmount(resultSet.getDouble("EXCLUSIVE_FIX_AMOUNT"));
        vo.setExclusivePercentAmount(resultSet.getDouble("EXCLUSIVE_PERCENT_AMOUNT"));
        vo.setInclusiveFixAmount(resultSet.getDouble("INCLUSIVE_FIX_AMOUNT"));
        vo.setInclusivePercentAmount(resultSet.getDouble("INCLUSIVE_PERCENT_AMOUNT"));
        vo.setDeleted(resultSet.getLong("IS_DELETED"));
        return vo;
    }
}
