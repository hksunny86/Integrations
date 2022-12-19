package com.inov8.microbank.common.vo.rowmappers;

import com.inov8.microbank.common.vo.product.CommissionShSharesDefaultVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ComissionShSharesDefaultRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        CommissionShSharesDefaultVO vo = new CommissionShSharesDefaultVO();
        vo.setProductId(resultSet.getLong("PRODUCT_ID"));
        vo.setCommissionStakeHolderId(resultSet.getLong("STAKEHOLDER_ID"));
        vo.setCommissionShSharesDefaultId(resultSet.getLong("PRODUCT_SH_SHARES_DEF_ID"));
        vo.setCreatedByAppUserId(resultSet.getLong("CREATED_BY"));
        vo.setUpdatedByAppUserId(resultSet.getLong("UPDATED_BY"));
        vo.setCommissionShare(resultSet.getDouble("COMMISSION_SHARE"));
        vo.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        vo.setCreatedOn(resultSet.getDate("CREATED_ON"));
        vo.setWhtApplicable(resultSet.getLong("IS_WHT_APPLICABLE"));
        vo.setFedApplicable(resultSet.getLong("IS_FED_APPLICABLE"));
        return vo;
    }
}
