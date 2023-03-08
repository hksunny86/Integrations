package com.inov8.microbank.server.dao.postedtransactionreportmodule.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.CreateNewDateFormat;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ProductIntgModuleInfoModel;
import com.inov8.microbank.common.model.SwitchModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.IntgTransactionTypeModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionReportModel;
import com.inov8.microbank.server.dao.postedtransactionreportmodule.PostedTransactionReportDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

public class PostedTransactionReportHibernateDAO extends BaseHibernateDAO<PostedTransactionReportModel, Long, PostedTransactionReportDAO> implements
		PostedTransactionReportDAO {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<IntgTransactionTypeModel> fetchIntgTransactionTypes( java.util.List<IntgTransactionTypeModel> intgTransactionTypeModelList,
                                                                     String propertyToSortBy, SortingOrder sortingOrder, Long... intgTransactionTypeIds ) throws DataAccessException
    {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass( IntgTransactionTypeModel.class );
        
        if( intgTransactionTypeModelList != null && intgTransactionTypeModelList.size() > 0 )
        {
        	List<Long> switchIdList = new ArrayList<Long>(0);
        	List<Long> productIntgModuleInfoIdList = new ArrayList<Long>(0);

        	for(IntgTransactionTypeModel intgTransactionTypeModel : intgTransactionTypeModelList) {
			
				SwitchModel switchModel = intgTransactionTypeModel.getSwitchRef();
				if(switchModel != null) {
					switchIdList.add(switchModel.getSwitchId());
				}
				
				ProductIntgModuleInfoModel productIntgModuleInfoModel = intgTransactionTypeModel.getProductIntgModuleInfo();
				
				if(productIntgModuleInfoModel != null) {
					productIntgModuleInfoIdList.add(productIntgModuleInfoModel.getProductIntgModuleInfoId());
				}
        	}
        	
            detachedCriteria.add(Restrictions.in("switchRef.switchId", switchIdList.toArray()));
            detachedCriteria.add(Restrictions.in("productIntgModuleInfo.productIntgModuleInfoId", productIntgModuleInfoIdList.toArray()));
        }
        if( intgTransactionTypeIds != null && intgTransactionTypeIds.length > 0 )
        {
            detachedCriteria.add(Restrictions.in("intgTransactionTypeId",intgTransactionTypeIds));
        }      

        if( SortingOrder.ASC == sortingOrder )
        {
            detachedCriteria.addOrder(Order.asc(propertyToSortBy));
        }
        else
        {
            detachedCriteria.addOrder( Order.desc(propertyToSortBy) );
        }
        List<IntgTransactionTypeModel> intgTransactionTypeModels = getHibernateTemplate().findByCriteria( detachedCriteria );

        return intgTransactionTypeModels;
    }

    @Override
    public PostedTransactionReportModel getTransactionCodeIdForReversalByStanAndUserId(MiddlewareMessageVO middlewareMessageVO, Long[] productIds, String recipientMfsId) throws FrameworkCheckedException {
        CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
        String date = null;
        try {
            if(middlewareMessageVO.getOrignalTransactionDateTime().contains("-"))
            {
                String[] dateParts = middlewareMessageVO.getOrignalTransactionDateTime().split(" ");
                String orgDate = dateParts[0];
                dateParts = orgDate.split("-");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
                String val = dateParts[2] + "/" + dateParts[1] + "/" + dateParts[0];
                Date dateObj = dateFormat.parse(val);
                dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                date = dateFormat.format(dateObj);
            }
            else
                date = createNewDateFormat.createDate(middlewareMessageVO.getOrignalTransactionDateTime());
            StringBuilder sqlBuilder = new StringBuilder(400);
            sqlBuilder.append("SELECT * FROM (");
            sqlBuilder.append(" select ptr.* FROM POSTED_TRANSACTION_REPORT ptr ");
            sqlBuilder.append("INNER JOIN TRANSACTION_DETAIL_MASTER tdm on tdm.TRANSACTION_CODE_ID = ptr.TRANSACTION_CODE_ID ");
            sqlBuilder.append("WHERE ptr.PRODUCT_ID IN (");
            sqlBuilder.append(productIds[0]).append(',').append(productIds[1]).append(',').append(productIds[2]).append(',').append(productIds[3]).append(',').append(productIds[4]);
            sqlBuilder.append(')');
            sqlBuilder.append(" AND tdm.MFS_ID = '").append(recipientMfsId).append("'");
            sqlBuilder.append(" AND ptr.STAN = '").append(middlewareMessageVO.getOrignalStan()).append("'");
            sqlBuilder.append(" AND TRUNC(ptr.created_on) = '").append(date).append("'");
            sqlBuilder.append(" ORDER BY ptr.TRANSACTION_CODE_ID DESC )");
            sqlBuilder.append("where rownum = ").append(1);
            logger.info("getTransactionCodeIdForReversalByStanAndUserId() query: " + sqlBuilder.toString());
            List<PostedTransactionReportModel> list = jdbcTemplate.query(sqlBuilder.toString(), new RowMapper<PostedTransactionReportModel>(){
                @Override
                public PostedTransactionReportModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                    PostedTransactionReportModel model = new PostedTransactionReportModel();
                    model.setSystemTraceAuditNumber(rs.getString("STAN"));
                    model.setProductId(rs.getLong("PRODUCT_ID"));
                    model.setTransactionCodeId(rs.getLong("TRANSACTION_CODE_ID"));
                    model.setPostedTransactionReportId(rs.getLong("POSTED_TRANS_RPT_ID"));
                    return model;
                }
            });
            if(list != null && !list.isEmpty())
                return list.get(0);
        } catch (Exception e) {
            logger.error("Error while parsing middlewareMessageVO.getOrignalTransactionDateTime().");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean validateDuplicateStan(Long[] productIds, String stan, String recipientMfsId) throws FrameworkCheckedException {
        StringBuilder sqlBuilder = new StringBuilder(400);
        CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
        String date = createNewDateFormat.formatDate(new Date());
        sqlBuilder.append("select count(*) FROM POSTED_TRANSACTION_REPORT ptr ");
        sqlBuilder.append("INNER JOIN TRANSACTION_DETAIL_MASTER tdm on tdm.TRANSACTION_CODE_ID = ptr.TRANSACTION_CODE_ID ");
        sqlBuilder.append("WHERE ptr.PRODUCT_ID IN (");
        sqlBuilder.append(productIds[0]).append(',').append(productIds[1]).append(',').append(productIds[2]).append(',').append(productIds[3]).append(',').append(productIds[4]);
        sqlBuilder.append(')');
        sqlBuilder.append(" AND tdm.RECIPIENT_MFS_ID = '").append(recipientMfsId).append("'");
        sqlBuilder.append("AND ptr.STAN = '").append(stan).append("'");
        sqlBuilder.append("AND TRUNC(ptr.created_on) = '").append(date).append("'");
        logger.info("ValidateDuplicateStan() query: " + sqlBuilder.toString());
        int result = jdbcTemplate.queryForInt(sqlBuilder.toString());
        if(result > 0)
            return false;

        return true;
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}