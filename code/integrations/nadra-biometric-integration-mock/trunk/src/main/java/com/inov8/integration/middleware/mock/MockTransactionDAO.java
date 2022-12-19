package com.inov8.integration.middleware.mock;

import com.inov8.integration.middleware.mock.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
@Repository
@Transactional
/**
 * Java Branchless Banking Connector(JBBC) Transaction DAO
 * @author JBBC
 *
 */
public class MockTransactionDAO {
    private JdbcTemplate jdbcTemplate;
    private static Logger logger = LoggerFactory.getLogger(MockTransactionDAO.class.getSimpleName());

    public JDBCUser getCustomerData(String CNIC) {
        Object[] values = {CNIC};
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM MOCK_PERSON WHERE CNIC = ? ", values, new RowMapper<JDBCUser>() {
                        @Override
                        public JDBCUser mapRow(ResultSet rs, int row) throws SQLException {
                            JDBCUser user = new JDBCUser();

                            user.setUsername(rs.getString("FULL_NAME"));
                            user.setCNIC(rs.getString("CNIC"));
                            user.setMobileNumber(rs.getString("MOBILE_NO"));
                            user.setPresentAddress(rs.getString("PRESENT_ADDRESS"));
                            user.setBirthPlace(rs.getString("BIRTH_PLACE"));
                            user.setCardExpired(rs.getString("CARD_EXPIRED"));
                            user.setDateOfBirth(rs.getString("DATE_OF_BIRTH"));
                            user.setReligion(rs.getString("RELIGION"));
                            user.setMotherName(rs.getString("MOTHER_NAME"));
                            user.setNativeLanguage(rs.getString("NATIVE_LANGUAGE"));
                            user.setResponseCode(rs.getString("RESPONSE_CODE"));
                            user.setGender(rs.getString("GENDER"));
                            user.setIndexes(rs.getString("INDEXES"));

                            return user;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Autowired
    public MockTransactionDAO(JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
