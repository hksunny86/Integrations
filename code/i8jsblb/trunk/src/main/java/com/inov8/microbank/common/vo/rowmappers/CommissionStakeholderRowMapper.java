package com.inov8.microbank.common.vo.rowmappers;

import com.inov8.microbank.common.vo.product.CommissionStakeholderVO;

import javax.swing.tree.RowMapper;
import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Inov8 on 9/21/2019.
 */
public class CommissionStakeholderRowMapper implements org.springframework.jdbc.core.RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        CommissionStakeholderVO vo = new CommissionStakeholderVO();
        vo.setStakeHolderTypeId(resultSet.getLong("STAKEHOLDER_TYPE_ID"));
        vo.setRetailerId(resultSet.getLong("RETAILER_ID"));
        vo.setOperatorId(resultSet.getLong("OPERATOR_ID"));
        vo.setDistributorId(resultSet.getLong("DISTRIBUTOR_ID"));
        vo.setBankId(resultSet.getLong("BANK_ID"));
        vo.setCreatedByAppUserId(resultSet.getLong("CREATED_BY"));
        vo.setUpdatedByAppUserId(resultSet.getLong("UPDATED_BY"));
        vo.setCommissionShAcctsTypeId(resultSet.getLong("CMSHACCTTYPE_ID"));
        vo.setCommissionStakeholderId(resultSet.getLong("COMMISSION_STAKEHOLDER_ID"));
        vo.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        vo.setCreatedOn(resultSet.getDate("CREATED_ON"));
        vo.setName(resultSet.getString("NAME"));
        vo.setContactName(resultSet.getString("CONTACT_NAME"));
        vo.setDescription(resultSet.getString("DESCRIPTION"));
        vo.setVersionNo(resultSet.getInt("VERSION_NO"));
        vo.setComments(resultSet.getString("COMMENTS"));
        vo.setDisplayOnProductScreen(resultSet.getBoolean("DISPLAY_ON_PRODUCT_SCREEN"));
        vo.setFiler(resultSet.getBoolean("DISPLAY_ON_PRODUCT_SCREEN"));
        return vo;
    }
}
