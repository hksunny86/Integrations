package com.inov8.microbank.nadraVerisys.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.util.DBManager;
import com.inov8.microbank.disbursement.util.BatchUtil;
import com.inov8.microbank.nadraVerisys.dao.VerisysDataDAO;
import com.inov8.microbank.nadraVerisys.model.VerisysDataModel;
import org.hibernate.Query;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by AtieqRe on 11/8/2016.
 */
public class VerisysDataHibernateDAO extends BaseHibernateDAO<VerisysDataModel,Long, VerisysDataDAO>
        implements VerisysDataDAO {

    private JdbcTemplate jdbcTemplate;

    private long startedAt = 0;
    private int batchNo = 1;
    private int batchCounter;
    private boolean executed;
    private Connection connection;

    private PreparedStatement appUserUpdateStatement;
    private PreparedStatement customerUpdateStatement;
    private PreparedStatement addressPresentUpdateStatement;
    private PreparedStatement addressPermanentUpdateStatement;
    private PreparedStatement accountHolderUpdateStatement;
    private PreparedStatement accountInfoUpdateStatement;
    private PreparedStatement verisysDataUpdateStatement;

    @Override
    public VerisysDataModel loadVerisysDataModel(String cnic) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("from VerisysDataModel vdm ");
        queryStr.append("where vdm.cnic =:cnic ");
        queryStr.append("and vdm.accountClosed=:isClosed");
        logger.info(queryStr);
        Query query = getSession().createQuery(queryStr.toString());

        query.setParameter("cnic", cnic);
        query.setParameter("isClosed", false);

        return (VerisysDataModel) query.uniqueResult();
    }

    @Override
    public VerisysDataModel loadVerisysDataModel(Long appUserId) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("from VerisysDataModel vdm ");
        queryStr.append("where vdm.appUserId =:appUserId");

        Query query = getSession().createQuery(queryStr.toString());
        query.setParameter("appUserId", appUserId);

        return (VerisysDataModel) query.uniqueResult();
    }


    @Override
    public List<VerisysDataModel> getUrduDataForTransliteration() throws FrameworkCheckedException
    {

        StringBuilder hql = new StringBuilder("from VerisysDataModel vdm where vdm.translated <> 1");
        return getHibernateTemplate().find(hql.toString());

    }

    @Override
    public void updateVerisysDataToTransleted(List<Long> verisysDataIds) throws FrameworkCheckedException
    {
        StringBuilder updateStatement = new StringBuilder();
        updateStatement.append("UPDATE VERISYS_DATA SET IS_TRANSLATED=1 WHERE VERISYS_DATA_ID IN (:verisysDataIds)");

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("verisysDataIds", verisysDataIds);

        NamedParameterJdbcTemplate template =
                new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());

        template.update(updateStatement.toString(), parameters);


    }

    @Override
    public void saveNadraData(VerisysDataModel model) throws FrameworkCheckedException{
        String query = "Insert into NADRA_TRANSLATED_DATA (NADRA_TRANSLATED_DATA_ID,APP_USER_ID,CNIC,NAME,CURRENT_ADDRESS,PERMANENT_ADDRESS,PLACE_OF_BIRTH,MOTHER_MAIDEN_NAME,CREATED_ON,UPDATED_ON,IS_TRANSLATED,IS_ACCOUNT_CLOSED)" +
                " values (NADRA_TRANSLATED_DATA_SEQ.nextval,"+model.getAppUserId()+",\'"+model.getCnic()+"\',UNISTR(\'"+model.getName()+"\')" +
                ",UNISTR(\'"+model.getCurrentAddress()+"\'),UNISTR(\'"+model.getPermanentAddress()+"\'),UNISTR(\'"+model.getPlaceOfBirth()+"\'),UNISTR(\'"+model.getMotherMaidenName()+"\')" +
                ",SYSDATE,SYSDATE,1,0)";
        jdbcTemplate.execute(query);
    }

    @Override
    public void addToBatch(Map<String, Object[]> map) throws Exception {
        if (batchCounter >= BatchUtil.getDbBatchSize()) {
            // execute existing batch
            executeBatch();

            // reset batch again
            initializeBatch();
        }

        prepareBatch(map);

        executed = false;
        batchCounter++;
    }

    @Override
    public Long getAddressId(Long customerId) throws Exception{
        NamedParameterJdbcTemplate template =
                new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        StringBuilder updateStatement = new StringBuilder();
        updateStatement.append("SELECT ADDRESS_ID FROM address where address_id= (select address_id from CUSTOMER_ADDRESSES where ADDRESS_TYPE_ID=1 and customer_id=:addressid)");

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("addressid", customerId);
        return template.queryForLong(updateStatement.toString(),parameters);

    }

    @Override
    public Long getAccountInfoId(Long customerId) throws Exception{
        NamedParameterJdbcTemplate template =
                new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        StringBuilder updateStatement = new StringBuilder();
        updateStatement.append("SELECT account_info_id FROM account_info where customer_id=:customerId");
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("customerId", customerId);
        return template.queryForLong(updateStatement.toString(),parameters);

    }

    @Override
    public Long getCustomer(Long appUserId) throws Exception{
        NamedParameterJdbcTemplate template =
                new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        StringBuilder updateStatement = new StringBuilder();
        updateStatement.append("SELECT CUSTOMER.CUSTOMER_ID FROM CUSTOMER,APP_USER WHERE APP_USER.CUSTOMER_ID=CUSTOMER.CUSTOMER_ID AND APP_USER.APP_USER_ID =:appUserId");

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("appUserId", appUserId);
        return template.queryForLong(updateStatement.toString(),parameters);

    }

    @Override
    public void markClosedByAppUserId(Long appUserId) throws DataAccessException
    {

        NamedParameterJdbcTemplate template =
                new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        StringBuilder updateStatement = new StringBuilder();
        updateStatement.append("UPDATE NADRA_TRANSLATED_DATA SET IS_ACCOUNT_CLOSED=1 WHERE APP_USER_ID =:appUserId");

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("appUserId", appUserId);
        template.update(updateStatement.toString(), parameters);

    }

    @Override
    public Long getAcoountHolderId(String cnic) throws Exception{
        NamedParameterJdbcTemplate template =
                new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        StringBuilder updateStatement = new StringBuilder();
        updateStatement.append("SELECT ACCOUNT_HOLDER_ID FROM ACCOUNT_HOLDER where cnic =:cnic");

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("cnic", cnic);
        return template.queryForLong(updateStatement.toString(),parameters);

    }


    @Override
    public void initializeBatch() throws Exception {
        connection = DBManager.getConnection();
        appUserUpdateStatement = connection.prepareStatement(APP_USER_UPDATE);
        customerUpdateStatement = connection.prepareStatement(CUSTOEMR_UPDATE);
        addressPresentUpdateStatement = connection.prepareStatement(PRESENT_ADDRESS_UPDATE);
        addressPermanentUpdateStatement = connection.prepareStatement(PERMANENT_ADDRESS_UPDATE);
        accountHolderUpdateStatement = connection.prepareStatement(ACCOUNT_HOLDER_UPDATE);
        accountInfoUpdateStatement = connection.prepareStatement(ACCOUNT_INFO_UPDATE);
        verisysDataUpdateStatement = connection.prepareStatement(VERISYS_UPDATE);
    }

    @Override
    public void closeBatch() throws Exception {
        if(!executed) {
            executeBatch();
        }
    }

    private void prepareBatch(Map<String, Object[]> map) throws Exception {
        Object[] appusers = map.get("AU");
        prepareAppUser(appusers);

        Object[] customers = map.get("CU");
        prepareCustomer(customers);

        Object[] customerAddresses = map.get("CA");
        prepareAddress(customerAddresses);

        Object[] accountHolders = map.get("AH");
        prepareAccountHolder(accountHolders);

        Object[] accountInfos = map.get("AI");
        prepareAccountInfo(accountInfos);

        Object[] translatedData = map.get("TD");
        prepareVerisys(translatedData);
    }

    private void prepareVerisys(Object[] obj) throws Exception {
/*        UPDATE NADRA_TRANSLATED_DATA SET IS_TRANSLATED=?,NAME_TRANSLATED=?,CURRENT_ADDRESS_TRANSLATED=?,PERMANENT_ADDRESS_TRANSLATED?," +
        "PLACE_OF_BIRTH_TRANSLATED=?,MOTHER_MAIDEN_NAME_TRANSLATED=? WHERE APP_USER_ID=?*/
        SimpleDateFormat df=new SimpleDateFormat("D-MMM-YYYY");

        verisysDataUpdateStatement.setLong(1, 1L);
        verisysDataUpdateStatement.setString(2, obj[1].toString());
        verisysDataUpdateStatement.setString(3, obj[2].toString());
        verisysDataUpdateStatement.setString(4, obj[3].toString());
        verisysDataUpdateStatement.setString(5, obj[4].toString());
        verisysDataUpdateStatement.setString(6, obj[5].toString());
        verisysDataUpdateStatement.setDate(7,new java.sql.Date(new Date().getTime()));
        verisysDataUpdateStatement.setLong(8, Long.valueOf( obj[0].toString()));
        verisysDataUpdateStatement.addBatch();
    }

    private void prepareAppUser(Object[] obj) throws Exception {
        //UPDATE APP_USER SET FIRST_NAME=?, LAST_NAME=?, MOTHER_MAIDEN_NAME=? WHERE MOBILE_NO=?
        //{mobileNo, firstName,lastName,motherMaidenName};
        appUserUpdateStatement.setString(1, obj[1].toString());
        appUserUpdateStatement.setString(2, obj[2].toString());
        appUserUpdateStatement.setString(3, obj[3].toString());
        appUserUpdateStatement.setString(4, obj[0].toString());
        appUserUpdateStatement.addBatch();
    }

    private void prepareCustomer(Object[] obj) throws Exception {
        //UPDATE CUSTOMER SET NAME=?, FATHER_HUSBAND_NAME=?, BIRTH_PLACE=?, GENDER=? WHERE MOBILE_NO=?
        //{mobileNo, firstName+lastName,fatherHusbandName,birthPlace,gender}
        customerUpdateStatement.setString(1, obj[1].toString());
        customerUpdateStatement.setString(2, obj[2].toString());
        customerUpdateStatement.setString(3, obj[0].toString());
        customerUpdateStatement.addBatch();
    }

    private void prepareAddress(Object[] obj) throws Exception {
        //{mobileNo, currentAddress,permanentAddress};
        //update presentaddress set FULL_ADDRESS=? where ADDRESS_ID = (select address_id from CUSTOMER_ADDRESSES where ADDRESS_TYPE_ID=1 and customer_id=(select customer_id from customer where mobile_no=?))
        //update permanentaddress set FULL_ADDRESS=? where ADDRESS_ID = (select address_id from CUSTOMER_ADDRESSES where ADDRESS_TYPE_ID=2 and customer_id=(select customer_id from customer where mobile_no=?))
        addressPresentUpdateStatement.setString(1, obj[1].toString());
        addressPresentUpdateStatement.setString(2, obj[1].toString());
        addressPresentUpdateStatement.setString(3, obj[1].toString());
        addressPresentUpdateStatement.setString(4, obj[0].toString());
        addressPresentUpdateStatement.addBatch();

        addressPermanentUpdateStatement.setString(1, obj[2].toString());
        addressPermanentUpdateStatement.setString(2, obj[2].toString());
        addressPermanentUpdateStatement.setString(3, obj[2].toString());
        addressPermanentUpdateStatement.setString(4, obj[0].toString());
        addressPermanentUpdateStatement.addBatch();
    }

    private void prepareAccountInfo(Object[] obj) throws Exception {
        //{mobileNo, firstName, lastName};
        //UPDATE ACCOUNT_INFO SET FIRST_NAME=?, LAST_NAME=? WHERE CUSTOMER_MOBILE_NO=?
        accountInfoUpdateStatement.setString(1, obj[1].toString());
        accountInfoUpdateStatement.setString(2, obj[2].toString());
        accountInfoUpdateStatement.setString(3, obj[0].toString());
        accountInfoUpdateStatement.addBatch();
    }

    private void prepareAccountHolder(Object[] obj) throws Exception {
        //{mobileNo, firstName, lastName, fatherHusbandName};
        //UPDATE ACCOUNT_HOLDER SET FIRST_NAME=?, LAST_NAME=?, FATHER_NAME=? WHERE MOBILE_NUMBER=?
        accountHolderUpdateStatement.setString(1, obj[1].toString());
        accountHolderUpdateStatement.setString(2, obj[2].toString());
        accountHolderUpdateStatement.setString(3, obj[2].toString());
        accountHolderUpdateStatement.setString(4, obj[0].toString());
        accountHolderUpdateStatement.addBatch();
    }


    private void executeBatch() throws Exception {
        appUserUpdateStatement.executeBatch();
        customerUpdateStatement.executeBatch();
        addressPresentUpdateStatement.executeBatch();
        addressPermanentUpdateStatement.executeBatch();
        accountHolderUpdateStatement.executeBatch();
        accountInfoUpdateStatement.executeBatch();
        verisysDataUpdateStatement.executeBatch();

        connection.commit();

        long ended = System.currentTimeMillis();
        logger.info("Batch No. " + batchNo + " Batch size " + batchCounter + " Executed at " + new Date(ended) +
                " Took " + (ended - startedAt)/1000.0d +" seconds.");

        cleanUpBatch();

        executed = true;
        batchCounter = 0;
        batchNo ++;
        startedAt = System.currentTimeMillis();
    }

    private void cleanUpBatch() throws Exception {
        cleanUpStatement(appUserUpdateStatement);
        cleanUpStatement(customerUpdateStatement);
        cleanUpStatement(addressPresentUpdateStatement);
        cleanUpStatement(addressPermanentUpdateStatement);
        cleanUpStatement(accountHolderUpdateStatement);
        cleanUpStatement(accountInfoUpdateStatement);
    }

    private void cleanUpStatement(PreparedStatement ps) throws Exception {
        ps.clearBatch();
        ps.close();
    }

    private static final String CUSTOEMR_UPDATE =
            "UPDATE CUSTOMER SET NAME=?, BIRTH_PLACE=? WHERE CUSTOMER_ID=?";

    private static final String PERMANENT_ADDRESS_UPDATE =
            "update address set FULL_ADDRESS=?,STREET_ADDRESS = ?,HOUSE_NO = ? where ADDRESS_ID =? ";
    private static final String PRESENT_ADDRESS_UPDATE =
            "update address set FULL_ADDRESS=?,STREET_ADDRESS = ?,HOUSE_NO = ? where ADDRESS_ID =?";

    private static final String ACCOUNT_HOLDER_UPDATE =
            "UPDATE ACCOUNT_HOLDER SET FIRST_NAME=?, LAST_NAME=?,FATHER_NAME= ? WHERE ACCOUNT_HOLDER_ID=?";

    private static final String APP_USER_UPDATE =
            "UPDATE APP_USER SET FIRST_NAME=?, LAST_NAME=?, MOTHER_MAIDEN_NAME=? WHERE APP_USER_ID=?";

    private static final String ACCOUNT_INFO_UPDATE = "UPDATE ACCOUNT_INFO SET FIRST_NAME=?, LAST_NAME=? WHERE ACCOUNT_INFO_ID=?";

    private static final String VERISYS_UPDATE = "UPDATE NADRA_TRANSLATED_DATA SET IS_TRANSLATED=?,NAME_TRANSLATED=?,CURRENT_ADDRESS_TRANSLATED=?,PERMANENT_ADDRESS_TRANSLATED=?," +
            "PLACE_OF_BIRTH_TRANSLATED=?,MOTHER_MAIDEN_NAME_TRANSLATED=?,UPDATED_ON=? WHERE APP_USER_ID=?";

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
