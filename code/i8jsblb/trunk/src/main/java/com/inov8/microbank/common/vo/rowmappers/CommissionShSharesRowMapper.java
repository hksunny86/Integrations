package com.inov8.microbank.common.vo.rowmappers;

import com.inov8.microbank.common.vo.product.CommissionShSharesVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommissionShSharesRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        CommissionShSharesVO vo = new CommissionShSharesVO();
        vo.setProductId(resultSet.getLong("PRODUCT_ID"));
        vo.setCommissionStakeHolderId(resultSet.getLong("COMMISSION_STAKEHOLDER_ID"));
        vo.setSegmentId(resultSet.getLong("SEGMENT_ID"));
        vo.setDeviceTypeId(resultSet.getLong("DEVICE_TYPE_ID"));
        vo.setDistributorId(resultSet.getLong("DISTRIBUTOR_ID"));
        vo.setMnoId(resultSet.getLong("SERVICE_OP_ID"));
        vo.setCreatedByAppUserId(resultSet.getLong("CREATED_BY"));
        vo.setUpdatedByAppUserId(resultSet.getLong("UPDATED_BY"));
        vo.setCommissionShSharesId(resultSet.getLong("COMMISSION_SH_SHARES_ID"));
        vo.setCommissionShare(resultSet.getDouble("COMMISSION_SHARE"));
        vo.setCreatedOn(resultSet.getDate("CREATED_ON"));
        vo.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        vo.setWhtApplicable(resultSet.getLong("IS_WHT_APPLICABLE"));
        vo.setProductShSharesId(resultSet.getLong("PRODUCT_SH_SHARES_RULE_ID"));
        vo.setDeleted(resultSet.getLong("IS_DELETED"));
        return vo;
    }
}
