package com.inov8.microbank.common.vo.rowmappers;

import com.inov8.microbank.common.vo.product.CommissionRateDefaultVO;
import com.inov8.microbank.common.vo.product.CommissionRateVO;
import com.inov8.microbank.common.vo.product.DistributorCommShareViewVO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Inov8 on 9/21/2019.
 */
public class DistributorCommShareViewRowMapper implements org.springframework.jdbc.core.RowMapper  {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        DistributorCommShareViewVO vo=new DistributorCommShareViewVO();
        vo.setPk(resultSet.getLong("PK"));
        vo.setAppUserId(resultSet.getLong("APP_USER_ID"));
        vo.setProductId(resultSet.getLong("PRODUCT_ID"));
        vo.setDistributorLevelId(resultSet.getLong("DISTRIBUTOR_LEVEL_ID"));
        vo.setDistributorId(resultSet.getLong("DISTRIBUTOR_ID"));
        vo.setParentAppUserId(resultSet.getLong("PARENT_APP_USER_ID"));
        vo.setParentRetailerContactId(resultSet.getLong("PARENT_RETAILER_CONTACT_ID"));
        vo.setParentCommissionShare(resultSet.getDouble("PARENT_COMMISSION_SHARE"));
        vo.setCommissionShare(resultSet.getDouble("COMMISSION_SHARE"));
        vo.setHead(resultSet.getLong("IS_HEAD"));

        return vo;
    }
}
