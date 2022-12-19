package com.inov8.microbank.server.dao.productmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.IBFTStatusModel;
import com.inov8.microbank.server.dao.productmodule.IBFTStatusDAO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


public class IBFTStatusHibernateDAO extends BaseHibernateDAO<IBFTStatusModel,Long, IBFTStatusDAO> implements IBFTStatusDAO {
    private JdbcTemplate jdbcTemplate;
    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //Classs to stop the duplicate ibft  , do not contains any sensitive information sql injections and paramsVlaidation applied in calling class
    //consumerNo and productID max length 30
    //Max jdbc query length with 30 characters parameters is

    @Override
    public void DeleteFromProcessing(String stan, String reqTime) throws FrameworkCheckedException {
        StringBuilder query = new StringBuilder(140);
        query.append("DELETE FROM IBFT_STATUS WHERE ");
        query.append(" STAN  = '").append(stan).append("'");
        query.append("  AND REQ_TIME = '").append(reqTime).append("'");
        try {
            jdbcTemplate.update(query.toString());
        } catch (DataAccessException e) {
            logger.error("Deletion from IBFT_Status Failed for stan: "+stan+"  at Request Time : "+reqTime+ "It will be handled by db job which runs after 5 minutes");
            e.printStackTrace();
            //No need to throw exception as this data will be deleted by db job after 15 mins
            //throw new FrameworkCheckedException("Deletion from Bill_Status Failed for conusmerNo: "+consumerNo+"  ProductId : "+productId ,e);
        }


    }

    @Override
    public void AddToProcessing(String stan, String reqTime) throws FrameworkCheckedException {
        StringBuilder query = new StringBuilder(140);
        query.append( "INSERT INTO IBFT_STATUS (STAN,REQ_TIME) VALUES "
                + " (?, ?)");

        try {
            jdbcTemplate.update(query.toString(), new Object[]{stan,reqTime});
            logger.info("Query to validate validate ibft Status IBFTStatusHibernateDAO.AddToProcessing() :: " + query.toString());

        } catch (DataAccessException e) {
            logger.error("Insertion in IBFT_Status Failed for stan: "+stan+"  at ReqTime : "+reqTime);
            e.printStackTrace();
            throw new FrameworkCheckedException("Your request is already in process for Stan: "+stan,e);
        }
    }

    @Override
    public boolean CheckIBFTStatus(String stan, String reqTime) throws FrameworkCheckedException {
        boolean processing = false;
        try {
            StringBuilder query = new StringBuilder(140);
            query.append("SELECT COUNT(*) FROM IBFT_STATUS WHERE ");
            query.append("STAN = '").append(stan).append("'");
            query.append(" AND REQ_TIME = '").append(reqTime).append("'");
            logger.info("Query to validate validate ibft Status IBFTStatusHibernateDAO.CheckIBFTStatus() :: " + query.toString());
            int size = (int) jdbcTemplate.queryForInt(query.toString());
            logger.info("Query Result :: " + size);
            processing = size>0?true:false;
        } catch (DataAccessException e) {
            logger.error("Count Query failed in IBFT_Status Failed for stan: "+stan+"  REQ_TIME : "+reqTime);
            e.printStackTrace();
            throw new FrameworkCheckedException("Could not Process the request at the moment for STAN: "+stan);

        }

        return processing;

    }
}
