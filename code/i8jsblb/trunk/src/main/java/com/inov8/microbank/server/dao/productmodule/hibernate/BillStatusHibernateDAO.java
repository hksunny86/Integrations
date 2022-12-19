package com.inov8.microbank.server.dao.productmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.BillStatusModel;

import com.inov8.microbank.server.dao.productmodule.BillStatusDAO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


public class BillStatusHibernateDAO extends BaseHibernateDAO<BillStatusModel,Long, BillStatusDAO> implements BillStatusDAO {
    private JdbcTemplate jdbcTemplate;
    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //Classs to stop the duplicate challan payment , do not contains any sensitive information sql injections and paramsVlaidation applied in calling class
    //consumerNo and productID max length 30
    //Max jdbc query length with 30 characters parameters is 130

    @Override
    public boolean CheckBillStatus(String consumerNo, String productCode)throws FrameworkCheckedException{
        boolean processing = false;
        try {
            StringBuilder query = new StringBuilder(140);
            query.append("SELECT COUNT(*) FROM BILL_STATUS WHERE ");
            query.append("CONSUMER_NO  = '").append(consumerNo).append("'");
            query.append(" AND PRODUCT_CODE = '").append(productCode).append("'");
            logger.info("Query to validate validate challan Status BillStatusHibernateDAO.CheckBillStatus() :: " + query.toString());
            int size = (int) jdbcTemplate.queryForInt(query.toString());
            logger.info("Query Result :: " + size);
            processing = size>0?true:false;
        } catch (DataAccessException e) {
            logger.error("Count Query failed in Bill_Status Failed for conusmerNo: "+consumerNo+"  Product Code : "+productCode);
            e.printStackTrace();
            throw new FrameworkCheckedException("Could not Process the request at the moment for Consumer No: "+consumerNo);

        }

        return processing;
    }
    @Override
    public void AddToProcessing( String consumerNo, String productCode)throws FrameworkCheckedException{
        StringBuilder query = new StringBuilder(140);
        query.append( "INSERT INTO BILL_STATUS (CONSUMER_NO,PRODUCT_CODE) VALUES "
                + " (?, ?)");

        try {
                jdbcTemplate.update(query.toString(), new Object[]{consumerNo,productCode});
                logger.info("Query to add challan Status in BIll_STATUS BillStatusHibernateDAO.AddToProcessing() :: " + query.toString());

        } catch (DataAccessException e) {
            logger.error("Insertion in Bill_Status Failed for conusmerNo: "+consumerNo+"  Product Code : "+productCode);
            e.printStackTrace();
            throw new FrameworkCheckedException("Your request is already in process for Consumer No: "+consumerNo,e);
        }
    }
    @Override
    public void DeleteFromProcessing( String consumerNo, String productCode)throws FrameworkCheckedException {
        StringBuilder query = new StringBuilder(140);
        query.append("DELETE FROM BILL_STATUS WHERE ");
        query.append(" CONSUMER_NO  = '").append(consumerNo).append("'");
        query.append("  AND PRODUCT_CODE = '").append(productCode).append("'");
        try {
            jdbcTemplate.update(query.toString());
        } catch (DataAccessException e) {
            logger.error("Deletion from Bill_Status Failed for conusmerNo: "+consumerNo+"  ProductCode : "+productCode+ "It will be handled by db job which runs after 15 minutes");
            e.printStackTrace();
            //No need to throw exception as this data will be deleted by db job after 15 mins
            //throw new FrameworkCheckedException("Deletion from Bill_Status Failed for conusmerNo: "+consumerNo+"  ProductId : "+productId ,e);
        }
    }

}
