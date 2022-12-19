package com.inov8.microbank.fonepay.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.fonepay.dao.VirtualCardDAO;
import com.inov8.microbank.fonepay.model.VirtualCardModel;
import com.inov8.microbank.fonepay.vo.NADRADataVO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by Attique on 7/14/2017.
 */
public class VirtualCardHibernateDAO  extends BaseHibernateDAO<VirtualCardModel,Long,VirtualCardDAO> implements VirtualCardDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void updateRegData(NADRADataVO vo) throws SQLException {
        getHibernateTemplate().flush();
        String query = null;
        if(vo.getPaymentModeId() == null)
        {
            query = "update APP_USER set FIRST_NAME = UNISTR(?), LAST_NAME = UNISTR(?),MOTHER_MAIDEN_NAME = UNISTR(?) where APP_USER_ID=?";
            Object[] args = {vo.getfName(),vo.getlName(), vo.getMotherMaidenName(),vo.getAppUserId()};
            jdbcTemplate.update(query,args);

            query = "update CUSTOMER set NAME = UNISTR (?) || ' ' || UNISTR (?), BIRTH_PLACE = UNISTR(?) where CUSTOMER_ID=?";
            Object[] args1 = {vo.getfName(),vo.getlName(), vo.getBirthPlace(),vo.getCustomerId()};
            jdbcTemplate.update(query,args1);

            query = "update ADDRESS set STREET_ADDRESS = UNISTR(?), FULL_ADDRESS = UNISTR(?),HOUSE_NO=UNISTR(?) where ADDRESS_ID=?";
            Object[] args2 = {vo.getAddress(),vo.getAddress(),vo.getAddress(),vo.getAddressId()};
            jdbcTemplate.update(query,args2);
        }

        query = "update ACCOUNT_HOLDER set FIRST_NAME = UNISTR(?), LAST_NAME = UNISTR(?),FATHER_NAME= UNISTR(?) where ACCOUNT_HOLDER_ID=?";
        Object[] args3 = {vo.getfName(),vo.getlName(),vo.getlName(),vo.getAccountHolderId()};
        jdbcTemplate.update(query,args3);

        query = "update ACCOUNT_INFO set FIRST_NAME = UNISTR(?), LAST_NAME = UNISTR(?) where ACCOUNT_INFO_ID=?";
        Object[] args4 = {vo.getfName(),vo.getlName(),vo.getAccountInfoId()};
        jdbcTemplate.update(query,args4);

        if(vo.getPaymentModeId() == null)
        {
            query = "update NADRA_TRANSLATED_DATA set NAME = UNISTR (?) || ' ' || UNISTR (?), CURRENT_ADDRESS = UNISTR(?) " +
                    ", PERMANENT_ADDRESS = UNISTR(?), PLACE_OF_BIRTH=UNISTR(?), MOTHER_MAIDEN_NAME=UNISTR(?) where APP_USER_ID=?";
            Object[] args5 = {vo.getfName(),vo.getlName(),vo.getAddress(),vo.getAddress(),vo.getBirthPlace(),vo.getMotherMaidenName(),vo.getAppUserId()};
            jdbcTemplate.update(query,args5);
        }

    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }
}