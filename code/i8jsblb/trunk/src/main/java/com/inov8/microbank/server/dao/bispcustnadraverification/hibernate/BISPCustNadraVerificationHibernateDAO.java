package com.inov8.microbank.server.dao.bispcustnadraverification.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BISPCustNadraVerificationModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.dao.bispcustnadraverification.BISPCustNadraVerificationDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Date;

public class BISPCustNadraVerificationHibernateDAO extends BaseHibernateDAO<BISPCustNadraVerificationModel, Long, BISPCustNadraVerificationDAO>
        implements BISPCustNadraVerificationDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Override
    public long isBVSSuccessful(Long customerId, String agentId, String cNic,Long isBVSVerified,Long appUerTypeId) throws FrameworkCheckedException {
        Long result = 0L;
        String query="SELECT COUNT(*) from BISP_CUST_NADRA_VERIFICATION where TRUNC(BUSINESS_DATE) = '" + PortalDateUtils.formatDate(new Date(),"dd-MMM-yyyy") + "'";

        if(isBVSVerified != -1)
            query += " AND IS_BVS_VERIFIED =  " + isBVSVerified ;
        if(customerId != null)
            query += " AND CUSTOMER_ID = " + customerId;
        if(agentId != null)
            query += " AND AGENT_ID = '" + agentId + "'";
        if(cNic != null && !cNic.equals(""))
            query += " AND CNIC = '" + cNic + "'";
        if(appUerTypeId != null && appUerTypeId.equals(UserTypeConstantsInterface.RETAILER)){
            query += " AND APP_USER_TYPE_ID = " + appUerTypeId;
        }
        else if(appUerTypeId != null && appUerTypeId.equals(UserTypeConstantsInterface.CUSTOMER)){
            query += " AND RESPONSE_CODE IN ('111','120','121','122','123','129','130','131','133','134','135','136')";
        }

        logger.info("Query to validate Agent BVS Request in BISPCustNadraVerificationHibernateDAO.isBVSSuccessful() :: " + query.toString() );
        try {
            result =  jdbcTemplate.queryForLong(query);
        }
        catch(Exception ex)
        {
            logger.error("Error while checking User's BVS infor in BISPCustNadraVerificationHibernateDAO.isBVSSuccessful()\n" + ex.getMessage(),ex);
            result = 0L;
        }
        return result;
    }

    @Override
    public long isNicUsedInCurrentDate(String agentId, String cNic, Long isBVSVerified, Long appUerTypeId) throws FrameworkCheckedException {
        Long result = 0L;
        String query="SELECT COUNT(*) from BISP_CUST_NADRA_VERIFICATION where TRUNC(BUSINESS_DATE) = '" + PortalDateUtils.formatDate(new Date(),"dd-MMM-yyyy") + "'";

        if(isBVSVerified != -1)
            query += " AND IS_BVS_VERIFIED =  " + isBVSVerified ;
        if(agentId != null)
            query += " AND AGENT_ID = '" + agentId + "'";
        if(cNic != null && !cNic.equals(""))
            query += " AND CNIC = '" + cNic + "'";
        if(appUerTypeId != null){
            query += " AND APP_USER_TYPE_ID = " + appUerTypeId;
        }

        logger.info("Query to validate Agent BVS Request in BISPCustNadraVerificationHibernateDAO.isNicUsedInCurrentDate() :: " + query.toString() );
        try {
            result =  jdbcTemplate.queryForLong(query);
        }
        catch(Exception ex)
        {
            result = 0L;
        }
        return result;
    }

    @Override
    public String saveOrUpdateBVSEntryRequiresNewTransaction(UserDeviceAccountsModel userDeviceAccountsModel, AppUserModel appUserModel,
                                                               String cNic, SwitchWrapper sWrapper,String transactionCode) throws FrameworkCheckedException {
        String errorMessage = "";
        I8SBSwitchControllerResponseVO responseVO = sWrapper.getI8SBSwitchControllerResponseVO();
        I8SBSwitchControllerRequestVO requestVO = sWrapper.getI8SBSwitchControllerRequestVO();
        String responseCode = responseVO.getResponseCode();
        // sWrapper.getI8SBSwitchControllerResponseVO().getI8SBSwitchControllerResponseVOList().get(1).getFingerIndex().replace(" ",","
        if(responseCode.equals("122"))
            errorMessage = MessageUtil.getMessage("i8sb.response.payment.122",null,null);
        else if(responseCode.equals("121"))
            errorMessage = MessageUtil.getMessage("i8sb.response.payment.121",null,null) + " "
                    + sWrapper.getI8SBSwitchControllerResponseVO().getI8SBSwitchControllerResponseVOList().get(1).getFingerIndex().replace(" ",",");
        else if(responseCode.equals("118"))
            errorMessage = MessageUtil.getMessage("i8sb.response.payment.118");
        else if(responseCode.equals("131"))
            errorMessage = MessageUtil.getMessage("i8sb.response.payment.131");
        else{
            responseCode = responseVO.getResponseCode();
            //responseCode = "I8SB-500";
            String errorMessageType = "payment";
            String msgKey = "i8sb.response." + errorMessageType + "." + responseCode;
            errorMessage = MessageUtil.getMessage(msgKey);
            if (StringUtil.isNullOrEmpty(errorMessage) && !responseCode.equals("I8SB-200") && !responseCode.equals("96")) {
                errorMessage = WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG;
            }
            else{
                errorMessage = responseVO.getDescription();
            }
        }
        logger.info("Response Description Received in BISPCustNadraVerificationHibernateDAO.sav() :: " + errorMessage
                + "\nwith Response Code: " + responseVO.getResponseCode() + " And Microbank Transaction Code :: " + transactionCode);
        BISPCustNadraVerificationModel custNadraVerificationModel = new BISPCustNadraVerificationModel();
        if(sWrapper.getObject("APP_USER_TYPE_ID") != null){
            custNadraVerificationModel.setAppUserTypeId((Long)sWrapper.getObject("APP_USER_TYPE_ID"));
        }
        custNadraVerificationModel.setResponseCode(responseCode);
        custNadraVerificationModel.setAgentId(userDeviceAccountsModel.getUserId());
        custNadraVerificationModel.setcNic(cNic);
        custNadraVerificationModel.setCreatedOn(new Timestamp(new Date().getTime()));
        custNadraVerificationModel.setMobileNo(appUserModel.getMobileNo());
        custNadraVerificationModel.setBusinessDate(PortalDateUtils.formatDate(new Date(),"dd-MMM-yyyy"));
        custNadraVerificationModel.setBaflSessionId(requestVO.getSessionId());
        custNadraVerificationModel.setNadraSessionId(requestVO.getSessionIdNadra());
        custNadraVerificationModel.setBaflWalletId(requestVO.getWalletAccountId());
        if(requestVO.getRequestType().equals(I8SBConstants.RequestType_BOP_CashOut))
            custNadraVerificationModel.setBaflTransactionNumber(requestVO.getRRN());
        else
            custNadraVerificationModel.setBaflTransactionNumber(requestVO.getTransactionId());
        if(responseCode.equals("I8SB-200"))
        {
            custNadraVerificationModel.setBVSRequired(Boolean.TRUE);
            errorMessage = null;
        }
        else
            custNadraVerificationModel.setBVSRequired(Boolean.FALSE);
        if(transactionCode != null)
            custNadraVerificationModel.setTransactionCode(transactionCode);
        this.saveOrUpdate(custNadraVerificationModel);
        //
        return errorMessage;
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }
}
