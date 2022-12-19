package com.inov8.microbank.common.vo.rowmappers;

import com.inov8.microbank.common.vo.product.CommissionRateDefaultVO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Inov8 on 9/21/2019.
 */
public class CommissionRateDefaultRowMapper implements org.springframework.jdbc.core.RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        CommissionRateDefaultVO vo = new CommissionRateDefaultVO();
        vo.setProductId(resultSet.getLong("PRODUCT_ID"));
        vo.setCommissionRateDefaultId(resultSet.getLong("PRODUCT_CHARGES_DEF_ID"));
        vo.setCreatedByAppUserId(resultSet.getLong("CREATED_BY"));
        vo.setUpdatedByAppUserId(resultSet.getLong("UPDATED_BY"));
        vo.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        vo.setCreatedOn(resultSet.getDate("CREATED_ON"));
        vo.setVersionNo(resultSet.getInt("VERSION_NO"));
        vo.setExclusiveFixAmount(resultSet.getDouble("EXCLUSIVE_FIX_AMOUNT"));
        vo.setExclusivePercentAmount(resultSet.getDouble("EXCLUSIVE_PERCENT_AMOUNT"));
        vo.setInclusiveFixAmount(resultSet.getDouble("INCLUSIVE_FIX_AMOUNT"));
        vo.setInclusivePercentAmount(resultSet.getDouble("INCLUSIVE_PERCENT_AMOUNT"));
        return vo;
    }
}
