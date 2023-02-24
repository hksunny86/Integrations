package com.inov8.microbank.server.dao.securitymodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.account.vo.BlacklistMarkingBulkUploadVo;
import com.inov8.microbank.account.vo.CustomerACNatureMarkingUploadVo;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.retailermodule.HandlerSearchViewModel;
import com.inov8.microbank.common.model.usergroupmodule.CustomUserPermissionViewModel;
import com.inov8.microbank.common.model.usergroupmodule.UserPermissionViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.dao.bankmodule.BankDAO;
import com.inov8.microbank.server.dao.mnomodule.MnoDAO;
import com.inov8.microbank.server.dao.operatormodule.OperatorDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.suppliermodule.SupplierDAO;
import com.inov8.microbank.server.dao.usergroupmodule.UserPermissionViewDAO;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.SessionImpl;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@SuppressWarnings({"unchecked"})
public class AppUserHibernateDAO
        extends BaseHibernateDAO<AppUserModel, Long, AppUserDAO>
        implements AppUserDAO, UserDetailsService {

    private static final String WALK_IN_SELECTION_QUERY =
            "SELECT APP_USER_TYPE_ID, NIC, MOBILE_NO, 0, REGISTRATION_STATE_ID FROM APP_USER WHERE NIC =:cnic1 or MOBILE_NO =:mobile ";
    private static final String APP_USER_SELECTION_QUERY =
            "SELECT APP_USER_TYPE_ID, MOBILE_NO, REGISTRATION_STATE_ID, IS_CLOSED_SETTLED,IS_CLOSED_UNSETTLED FROM APP_USER WHERE MOBILE_NO =:mobile " +
                    "AND APP_USER_TYPE_ID =:appUserTypeId";
    protected static Log LOGGER = LogFactory.getLog(AppUserHibernateDAO.class);
    private SupplierDAO supplierDAO = null;
    private OperatorDAO operatorDAO = null;
    private MnoDAO mnoDAO = null;
    private BankDAO bankDAO = null;
    private UserPermissionViewDAO userPermissionViewDAO = null;
    private SmartMoneyAccountManager smartMoneyAccountManager;
    private JdbcTemplate jdbcTemplate;

    public List<AppUserModel> findAppUsersByType(Long appUserType) {
        List<AppUserModel> appUserList = null;
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AppUserModel.class);
        detachedCriteria.add(Restrictions.eq("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", appUserType));
        appUserList = getHibernateTemplate().findByCriteria(detachedCriteria);
        return appUserList;
    }

    public AppUserModel getAppUserWithRegistrationState(String mobileNo, String cnic, Long registrationState) {
        AppUserModel appUserModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(AppUserModel.class);
        criteria.add(Restrictions.eq("mobileNo", mobileNo));
        criteria.add(Restrictions.eq("nic", cnic));
        criteria.add(Restrictions.eq("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", UserTypeConstantsInterface.CUSTOMER));
        criteria.add(Restrictions.isNotNull("relationRegistrationStateModel.registrationStateId"));
        List<AppUserModel> list = getHibernateTemplate().findByCriteria(criteria);

        if (list != null && !list.isEmpty()) {

            appUserModel = list.get(0);
        }
        return appUserModel;
    }

    public AppUserModel verifyL3Agent(Long retailerContactId) {
        AppUserModel appUserModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(AppUserModel.class);
        criteria.add(Restrictions.eq("relationRegistrationStateModel.registrationStateId", RegistrationStateConstants.VERIFIED));
        criteria.add(Restrictions.eq("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", UserTypeConstantsInterface.RETAILER));
        criteria.add(Restrictions.eq("relationRetailerContactIdRetailerContactModel.retailerContactId", retailerContactId));
        List<AppUserModel> list = getHibernateTemplate().findByCriteria(criteria);

        if (list != null && !list.isEmpty()) {
            appUserModel = list.get(0);
        }
        return appUserModel;
    }

    public AppUserModel loadAppUserByMobileAndType(String mobileNo) {
        AppUserModel appUserModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(AppUserModel.class);
        criteria.add(Restrictions.eq("mobileNo", mobileNo));
        Criterion criterionOne = Restrictions.eq("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", UserTypeConstantsInterface.CUSTOMER.longValue());
        Criterion criterionTwo = Restrictions.eq("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", UserTypeConstantsInterface.RETAILER.longValue());
        LogicalExpression expressionCriterion = Restrictions.or(criterionOne, criterionTwo);
        criteria.add(expressionCriterion);
        List<AppUserModel> list = getHibernateTemplate().findByCriteria(criteria);

        if (list != null && !list.isEmpty()) {
            appUserModel = list.get(0);
        }
        return appUserModel;
    }

    @Override
    public AppUserModel loadAppUserByMobileAndTypeForCustomer(String mobileNo) {
        AppUserModel appUserModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(AppUserModel.class);
        criteria.add(Restrictions.eq("mobileNo", mobileNo));
        criteria.add(Restrictions.eq("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", UserTypeConstantsInterface.CUSTOMER.longValue()));
//		Criterion criterionOne = Restrictions.eq("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", UserTypeConstantsInterface.CUSTOMER.longValue());
//		Criterion criterionTwo = Restrictions.eq("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", UserTypeConstantsInterface.RETAILER.longValue());
//		LogicalExpression expressionCriterion = Restrictions.or(criterionOne, criterionTwo);
//		criteria.add(expressionCriterion);
        List<AppUserModel> list = getHibernateTemplate().findByCriteria(criteria);

        if (list != null && !list.isEmpty()) {
            appUserModel = list.get(0);
        }
        return appUserModel;
    }

    public AppUserModel loadAppUserByMobileAndType(String mobileNo, Long... appUserTypes) throws FrameworkCheckedException {

        DetachedCriteria criteria = DetachedCriteria.forClass(AppUserModel.class);
        criteria.add(Restrictions.eq("mobileNo", mobileNo));
        Criterion criterions = Restrictions.in("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", appUserTypes);
        criteria.add(criterions);
        List<AppUserModel> list = getHibernateTemplate().findByCriteria(criteria);

        AppUserModel appUserModel = null;

        if (list != null && !list.isEmpty()) {
            appUserModel = list.get(0);
        }

        return appUserModel;
    }

    public AppUserModel findByRetailerContractId(long retailerContactId) throws FrameworkCheckedException {
        AppUserModel appUserModel = null;
        List<AppUserModel> list = null;
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AppUserModel.class);
        detachedCriteria.add(Restrictions.eq("relationRetailerContactIdRetailerContactModel.retailerContactId", retailerContactId));
        list = getHibernateTemplate().findByCriteria(detachedCriteria);
        if (list != null && list.size() > 0) {
            appUserModel = list.get(0);
        }
        return appUserModel;
    }

    /**
     * @see org.appfuse.dao.UserDao#getUser(Long)
     */
    public AppUserModel getUser(Long userId) {
        return super.findByPrimaryKey(userId);
    }

    /**
     * @see org.appfuse.dao.UserDao#getUsers(org.appfuse.model.User)
     */
    public List getUsers(AppUserModel user) {
        return getHibernateTemplate().find(
                "from AppUserModel u order by upper(u.username)");
    }

    /**
     * @see org.appfuse.dao.UserDao#saveUser(org.appfuse.model.User)
     */
    public void saveUser(final AppUserModel user) {
        super.saveOrUpdate(user);
        getHibernateTemplate().flush();
    }

    public AppUserModel closeAgnetAccount(final AppUserModel user) throws FrameworkCheckedException {
        //Check for pending transactions
        String hql = "from UserDeviceAccountsModel udam where udam.relationAppUserIdAppUserModel.appUserId=" + user.getAppUserId();
        List list = this.getHibernateTemplate().find(hql.toString());
        UserDeviceAccountsModel udam = (UserDeviceAccountsModel) list.get(0);
        String agentId = udam.getUserId();
        hql = "from TransactionDetailPortalListModel tdp where (tdp.agent1Id='" + agentId + "' or tdp.agent2Id='" + agentId + "') and tdp.processingStatusName='" + SupplierProcessingStatusConstants.IN_PROCESS + "'";
        list = this.getHibernateTemplate().find(hql.toString());
        if (list != null && list.size() > 0) {
            throw new FrameworkCheckedException("TrnsPend");
        }
        /// check if childs exists
        hql = "from RetailerContactModel rcm where rcm.parentRetailerContactModel.retailerContactId=" + user.getRetailerContactId() + " and rcm.active=1";
        list = this.getHibernateTemplate().find(hql.toString());
        if (list != null && list.size() > 0) {
            throw new FrameworkCheckedException("ChildExists");
        }
        //Check if Core Account Linked
        hql = "from SmartMoneyAccountModel sma where sma.relationRetailerContactIdRetailerContactModel.retailerContactId=" + user.getRetailerContactId() + " and sma.relationPaymentModeIdPaymentModeModel.paymentModeId !=3 and sma.deleted=false";
        list = this.getHibernateTemplate().find(hql.toString());
        if (list != null && list.size() > 0) {
            throw new FrameworkCheckedException("AccLinked");
        }

        hql = "update RetailerContactModel set active=0  where retailerContactId=?";
        int x = this.getHibernateTemplate().bulkUpdate(hql, user.getRetailerContactId());

        super.saveOrUpdate(user);
        getHibernateTemplate().flush();
        return user;
    }

    public AppUserModel closeCustomerAccount(final AppUserModel user) throws FrameworkCheckedException {

        //Check for pending transactions
        String hql = null;
        hql = "from UserDeviceAccountsModel udam where udam.relationAppUserIdAppUserModel.appUserId=" + user.getAppUserId();
        List list = this.getHibernateTemplate().find(hql.toString());
        UserDeviceAccountsModel udam = (UserDeviceAccountsModel) list.get(0);
        String customerId = udam.getUserId();
        hql = "from TransactionDetailPortalListModel tdp where (tdp.mfsId='" + customerId + "' or tdp.recipientMfsId='" + customerId + "') and tdp.processingStatusName='" + SupplierProcessingStatusConstants.IN_PROCESS + "'";

        list = this.getHibernateTemplate().find(hql.toString());
        if (list != null && list.size() > 0) {
            throw new FrameworkCheckedException("TrnsPend");
        }
        super.saveOrUpdate(user);
        getHibernateTemplate().flush();
        return user;
    }

    public AppUserModel closeHandlerAccount(final AppUserModel handlerAppUserModel) throws FrameworkCheckedException {

        String query = "from UserDeviceAccountsModel udam where udam.relationAppUserIdAppUserModel.appUserId=" + handlerAppUserModel.getAppUserId();
        List list = this.getHibernateTemplate().find(query);
        UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) list.get(0);
        String handlerUserId = userDeviceAccountsModel.getUserId();
        query = "from HandlerTransactionDetailViewModel htd where htd.handlerId='" + handlerUserId + "'and htd.tranasactionStatus='" + SupplierProcessingStatusConstants.IN_PROCESS + "'";
        list = this.getHibernateTemplate().find(query);

        if (list != null && list.size() > 0) {
            throw new FrameworkCheckedException("TrnsPend");
        }

        super.saveOrUpdate(handlerAppUserModel);
        getHibernateTemplate().flush();
        return handlerAppUserModel;
    }

    public void checkHandlerPendingTransactions(List<HandlerSearchViewModel> handlerSearchViewModelList) throws FrameworkCheckedException {

        for (HandlerSearchViewModel handlerSearchViewModel : handlerSearchViewModelList) {


            String query = "from UserDeviceAccountsModel udam where udam.relationAppUserIdAppUserModel.appUserId=" + handlerSearchViewModel.getAppUserId();
            List list = this.getHibernateTemplate().find(query);
            UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) list.get(0);
            String handlerAppUserId = userDeviceAccountsModel.getUserId();
            query = "from TransactionDetailPortalListModel tdp where (tdp.agent1Id='" + handlerAppUserId + "' or tdp.agent2Id='" + handlerAppUserId + "') and tdp.processingStatusName='" + SupplierProcessingStatusConstants.IN_PROCESS + "'";
            list = this.getHibernateTemplate().find(query);

            if (list != null && list.size() > 0) {
                throw new FrameworkCheckedException("Transaction/s pending against Handler ID : " + handlerSearchViewModel.getAgentId());
            }

        }
        getHibernateTemplate().flush();
    }

    /**
     * @see org.appfuse.dao.UserDao#removeUser(Long)
     */
    public void removeUser(Long userId) {
        super.deleteByPrimaryKey(userId);
        getHibernateTemplate().flush();
    }

    public AppUserModel loadAppUserByUserName(String userName) {

        AppUserModel appUserModel = null;
        AppUserModel example = new AppUserModel();
        example.setUsername(userName);

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        exampleConfigHolderModel.setIgnoreCase(Boolean.FALSE);
        DateRangeHolderModel dateRangeHolderModel = null;
        CustomList<AppUserModel> appUserModelCustomList = super.findByExample(example, null, null, dateRangeHolderModel, exampleConfigHolderModel);

        if (appUserModelCustomList != null) {

            List<AppUserModel> appUserList = appUserModelCustomList.getResultsetList();

            if (!appUserList.isEmpty()) {

                appUserModel = appUserList.get(0);
            }

        }

        return appUserModel;
    }

    /**
     * @see org.acegisecurity.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        AppUserModel user = this.loadAppUserByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        }

        if (null != user) {
            //load permission list for this user
            UserPermissionViewModel userPermissionViewModel = new UserPermissionViewModel();
            userPermissionViewModel.setAppUserId(user.getAppUserId());

            ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
            exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
            exampleConfigHolderModel.setIgnoreCase(Boolean.FALSE);
            CustomList<UserPermissionViewModel> permissionList = userPermissionViewDAO.findByExample(userPermissionViewModel, null, null, exampleConfigHolderModel);
            List<UserPermissionViewModel> userPermissionList = permissionList.getResultsetList();

            //converting userPermissionList to customerUserPermissionList because customerUserPermissionViewModel implements GrantedAuthority interface.
            //userPermissionViewModel is generated class thats why we don't want to change it.
            List<CustomUserPermissionViewModel> customUserPermissionList = new ArrayList<CustomUserPermissionViewModel>();
            for (UserPermissionViewModel userPermission : userPermissionList) {
                CustomUserPermissionViewModel customPermission = new CustomUserPermissionViewModel(userPermission);
                customUserPermissionList.add(customPermission);
            }
            user.setUserPermissionList(customUserPermissionList);

            UserDetails uDetails = (UserDetails) user;

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("isAccountNonExpired: " + uDetails.isAccountNonExpired());
                LOGGER.debug("isAccountNonLocked: " + uDetails.isAccountNonLocked());
                LOGGER.debug("isCredentialsNonExpired: " + uDetails.isCredentialsNonExpired());
                LOGGER.debug("isEnabled: " + uDetails.isEnabled());
                LOGGER.debug("isVerified: " + user.getVerified());
            }
            if (!user.getVerified()) {
                throw new BadCredentialsException("User account is not verified");
            } else {
                Long userType = user.getAppUserTypeId();
                List<BasePersistableModel> list = null;
                if (UserTypeConstantsInterface.BANK.equals(userType)) {
                    BankModel bank = bankDAO.findByPrimaryKey(user.getBankUserIdBankUserModel().getBankId());
                    user.getBankUserIdBankUserModel().setBankIdBankModel(bank);
                } else if (UserTypeConstantsInterface.MNO.equals(userType)) {
                    MnoModel mno = mnoDAO.findByPrimaryKey(user.getMnoUserIdMnoUserModel().getMnoId());
                    user.getMnoUserIdMnoUserModel().setMnoIdMnoModel(mno);
                } else if (UserTypeConstantsInterface.SUPPLIER.equals(userType)) {
                    SupplierModel supplier = supplierDAO.findByPrimaryKey(user.getSupplierUserIdSupplierUserModel().getSupplierId());
                    user.getSupplierUserIdSupplierUserModel().setSupplierIdSupplierModel(supplier);
                } else if (UserTypeConstantsInterface.INOV8.equals(userType)) {
                    OperatorModel operator = operatorDAO.findByPrimaryKey(user.getOperatorUserIdOperatorUserModel().getOperatorId());
                    user.getOperatorUserIdOperatorUserModel().setOperatorIdOperatorModel(operator);
                }
                return uDetails;
            }

        }
        throw new UsernameNotFoundException("user '" + username + "' not found...");
    }

    /**
     * @param bankDAO the bankDAO to set
     */
    public void setBankDAO(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }

    /**
     * @param mnoDAO the mnoDAO to set
     */
    public void setMnoDAO(MnoDAO mnoDAO) {
        this.mnoDAO = mnoDAO;
    }

    /**
     * @param operatorDAO the operatorDAO to set
     */
    public void setOperatorDAO(OperatorDAO operatorDAO) {
        this.operatorDAO = operatorDAO;
    }

    /**
     * @param supplierDAO the supplierDAO to set
     */
    public void setSupplierDAO(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
    }

    /**
     * @param userPermissionViewDAO the userPermissionViewDAO to set
     */
    public void setUserPermissionViewDAO(UserPermissionViewDAO userPermissionViewDAO) {
        this.userPermissionViewDAO = userPermissionViewDAO;
    }

    /* (non-Javadoc)
     * @see com.inov8.microbank.server.dao.securitymodule.AppUserDAO#isAlreadyRegistered(com.inov8.microbank.common.model.AppUserModel)
     */
    public Boolean isAlreadyRegistered(AppUserModel appUserModel) {

        Boolean isAlreadyRegistered = Boolean.FALSE;

        Criterion criterionOne = Restrictions.eq("mobileNo", appUserModel.getMobileNo());
        Criterion criterionTwo = Restrictions.eq("nic", appUserModel.getNic());
        Criterion criterionThree = Restrictions.ne("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", UserTypeConstantsInterface.WALKIN_CUSTOMER.longValue());

        LogicalExpression expressionCriterion = Restrictions.or(criterionOne, criterionTwo);

        LogicalExpression logicalExpression = Restrictions.and(expressionCriterion, criterionThree);

        CustomList<AppUserModel> customList = super.findByCriteria(logicalExpression);

        if (customList != null && !customList.getResultsetList().isEmpty()) {

            appUserModel = customList.getResultsetList().get(0);

            LOGGER.info("\n\nisAlreadyRegistered (...) Aready Regisrtered " + appUserModel.getFirstName() + ", Id : " + appUserModel.getAppUserId() + " Type : " + appUserModel.getAppUserTypeId());
            isAlreadyRegistered = Boolean.TRUE;
        }

        return isAlreadyRegistered;

    }

    /**
     * @param appUserModel -> getMobileNo
     * @param appUserModel -> getAppUserTypeId
     */
    public AppUserModel loadAppUserByQuery(final String mobileNo, final long appUserTypeId) {
        AppUserModel appUserModel = null;

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT user ");
        queryBuilder.append(" FROM AppUserModel user ");
        queryBuilder.append(" WHERE ");
        queryBuilder.append(" user.mobileNo = :mobileNo ");
        queryBuilder.append(" AND ");
        queryBuilder.append(" user.relationAppUserTypeIdAppUserTypeModel.appUserTypeId = :appUserTypeId ");

        String[] paramNames = {"mobileNo", "appUserTypeId"};
        Object[] values = {mobileNo, appUserTypeId};
        try {
            List<AppUserModel> userList = getHibernateTemplate().findByNamedParam(queryBuilder.toString(), paramNames, values);
            if (CollectionUtils.isNotEmpty(userList)) {
                appUserModel = userList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appUserModel;
    }

    public AppUserModel loadAppUserByMobileByQuery(final String mobileNo) {
        AppUserModel appUserModel = null;

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT user ");
        queryBuilder.append(" FROM AppUserModel user ");
        queryBuilder.append(" WHERE ");
        queryBuilder.append(" user.mobileNo = :mobileNo ");
        String[] paramNames = {"mobileNo"};
        Object[] values = {mobileNo};
        try {
            List<AppUserModel> userList = getHibernateTemplate().findByNamedParam(queryBuilder.toString(), paramNames, values);
            if (CollectionUtils.isNotEmpty(userList)) {
                appUserModel = userList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appUserModel;
    }

    public AppUserModel loadAppUserByHandlerByQuery(final Long handlerId) {
        AppUserModel appUserModel = null;

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT user ");
        queryBuilder.append(" FROM AppUserModel user ");
        queryBuilder.append(" WHERE ");
        queryBuilder.append(" user.relationHandlerIdHandlerModel.handlerId = :handlerId ");
        queryBuilder.append(" AND ");
        queryBuilder.append(" user.relationAppUserTypeIdAppUserTypeModel.appUserTypeId = :appUserTypeId ");

        String[] paramNames = {"handlerId", "appUserTypeId"};
        Object[] values = {handlerId, UserTypeConstantsInterface.HANDLER};
        try {
            List<AppUserModel> userList = getHibernateTemplate().findByNamedParam(queryBuilder.toString(), paramNames, values);
            if (CollectionUtils.isNotEmpty(userList)) {
                appUserModel = userList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appUserModel;
    }

    public AppUserModel loadAppUserByCNIC(String cnic) {
        AppUserModel appUserModel = null;

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT user ");
        queryBuilder.append(" FROM AppUserModel user ");
        queryBuilder.append(" WHERE ");
        queryBuilder.append(" user.nic = :cnic ");
        String[] paramNames = {"cnic"};
        Object[] values = {cnic};
        try {
            List<AppUserModel> userList = getHibernateTemplate().findByNamedParam(queryBuilder.toString(), paramNames, values);
            if (CollectionUtils.isNotEmpty(userList)) {
                appUserModel = userList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appUserModel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public AppUserModel loadHeadRetailerContactAppUser(Long retailerId) {
        AppUserModel appUserModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(AppUserModel.class, "appUserModel");
        criteria.createCriteria("appUserModel.relationRetailerContactIdRetailerContactModel", "retailerContactModel").add(Restrictions.eq("head", true))
                .add(Restrictions.eq("relationRetailerIdRetailerModel.retailerId", retailerId));

        List<AppUserModel> appUserModelList = getHibernateTemplate().findByCriteria(criteria);
        if (appUserModelList != null && !appUserModelList.isEmpty()) {
            appUserModel = appUserModelList.get(0);
            try {
                Hibernate.initialize(appUserModel.getAppUserIdUserDeviceAccountsModelList());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return appUserModel;
    }

    @Override
    public AppUserModel loadAppUserModelByCustomerId(final long customerId) {
        AppUserModel appUserModel = null;

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT user ");
        queryBuilder.append(" FROM AppUserModel user ");
        queryBuilder.append(" WHERE user.relationCustomerIdCustomerModel.customerId = :customerId ");

        String[] paramNames = {"customerId"};
        Object[] values = {customerId};
        try {
            List<AppUserModel> userList = getHibernateTemplate().findByNamedParam(queryBuilder.toString(), paramNames, values);
            if (CollectionUtils.isNotEmpty(userList)) {
                appUserModel = userList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appUserModel;
    }

    @Override
    public List<AppUserModel> loadAppUserMarkAmaAccountDebitBlock() throws FrameworkCheckedException {
        AppUserModel appUserModel = null;
        List<AppUserModel> cnicList = null;
        //String hql = "select aum.nic from AppUserModel aum where aum.accountStateId is null or aum.accountStateId <> " + AccountStateConstantsInterface.ACCOUNT_STATE_DORMANT;

        String hqlCustomers = "select aum from AppUserModel aum ";
        hqlCustomers += "where aum.nic is not null ";
        hqlCustomers += "and aum.accountClosedSettled != 1 ";
        hqlCustomers += "and aum.accountClosedUnsettled != 1 ";
        hqlCustomers += "and aum.relationAppUserTypeIdAppUserTypeModel.appUserTypeId is not null ";
        hqlCustomers += " and aum.relationAppUserTypeIdAppUserTypeModel.appUserTypeId = " + UserTypeConstantsInterface.CUSTOMER;
        hqlCustomers += "and aum.relationCustomerIdCustomerModel.relationCustomerAccountTypeIdCustomerAccountTypeModel.customerAccountTypeId is not null ";
        hqlCustomers += " and aum.relationCustomerIdCustomerModel.relationCustomerAccountTypeIdCustomerAccountTypeModel.customerAccountTypeId = " + CustomerAccountTypeConstants.LEVEL_0;
        hqlCustomers += " and aum.relationCustomerIdCustomerModel.accountOpenedByAma is not null ";
        hqlCustomers += " and aum.relationCustomerIdCustomerModel.accountOpenedByAma = " + 1;
        hqlCustomers += " and aum.relationRegistrationStateModel.registrationStateId =" + RegistrationStateConstantsInterface.VERIFIED;
        hqlCustomers += " and aum.accountStateId =" + AccountStateConstantsInterface.ACCOUNT_STATE_COLD;


        try {
            cnicList = getHibernateTemplate().find(hqlCustomers);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnicList;
    }

    @Override
    public List<AppUserModel> loadPendingAccountOpeningAppUser() throws FrameworkCheckedException {
        AppUserModel appUserModel = null;
        List<AppUserModel> cnicList = null;
        //String hql = "select aum.nic from AppUserModel aum where aum.accountStateId is null or aum.accountStateId <> " + AccountStateConstantsInterface.ACCOUNT_STATE_DORMANT;

        String hqlCustomers = "select aum from AppUserModel aum ";
        hqlCustomers += "where aum.nic is not null ";
        hqlCustomers += " and aum.relationRegistrationStateModel.registrationStateId =" + RegistrationStateConstants.CLSPENDING;
        hqlCustomers += " and aum.accountStateId =" + AccountStateConstants.CLS_STATE_BLOCKED;
        try {
            cnicList = getHibernateTemplate().find(hqlCustomers);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnicList;
    }

    @Override
    public List<BulkDisbursementsModel> isAlreadyRegistered(List<BulkDisbursementsModel> list, final Long... appUserTypeIds) {

        StringBuilder sqlBuilder = new StringBuilder(200);
        sqlBuilder.append("select count(*) from APP_USER where NIC=? and APP_USER_TYPE_ID in (");
        sqlBuilder.append(appUserTypeIds[0]).append(',').append(appUserTypeIds[1]);
        sqlBuilder.append(')');
        ResultSetExtractor<Boolean> countToBooleanExtractor = new CountToBooleanExtractor();
        for (final BulkDisbursementsModel model : list) {
            Boolean registered = jdbcTemplate.query(sqlBuilder.toString(), new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement) throws SQLException {
                    preparedStatement.setString(1, model.getCnic());
                }
            }, countToBooleanExtractor);
            if (registered) {
                model.setValidRecord(false);
            }
        }
        return list;
    }

    @Override
    public boolean isAgentOrCustomer(final String mobileNo) throws FrameworkCheckedException {
        boolean registered = false;
        StringBuilder sqlBuilder = new StringBuilder(200);
        sqlBuilder.append("select count(*) from APP_USER where mobile_no = ? and APP_USER_TYPE_ID in (");
        sqlBuilder.append(UserTypeConstantsInterface.CUSTOMER).append(',').append(UserTypeConstantsInterface.RETAILER);
        sqlBuilder.append(')');
        ResultSetExtractor<Boolean> countToBooleanExtractor = new CountToBooleanExtractor();
        registered = jdbcTemplate.query(sqlBuilder.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, mobileNo);
            }
        }, countToBooleanExtractor);

        return registered;
    }

    public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
        this.smartMoneyAccountManager = smartMoneyAccountManager;
    }

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Long loadSegmentIdByMobileNo(String mobileNo) {

        Long segmentId = null;
        String hql = "select cm.relationSegmentIdSegmentModel.segmentId from CustomerModel cm, AppUserModel au"
                + " where au.relationCustomerIdCustomerModel.customerId = cm.customerId"
                + " and au.relationAppUserTypeIdAppUserTypeModel.appUserTypeId = " + UserTypeConstantsInterface.CUSTOMER
                + " and au.mobileNo = ? ";
        try {
            List<Long> list = getHibernateTemplate().find(hql, mobileNo);
            if (CollectionUtils.isNotEmpty(list)) {
                segmentId = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return segmentId;
    }

    public Long loadCustomerAccountTypeByMobileNo(String mobileNo) {

        Long accountTypeId = null;

        String hql = "select cm.relationCustomerAccountTypeIdCustomerAccountTypeModel.customerAccountTypeId from CustomerModel cm, AppUserModel au"
                + " where au.relationCustomerIdCustomerModel.customerId = cm.customerId"
                + " and au.relationAppUserTypeIdAppUserTypeModel.appUserTypeId = " + UserTypeConstantsInterface.CUSTOMER
                + " and au.mobileNo = ?";
        try {
            List<Long> list = getHibernateTemplate().find(hql, mobileNo);
            if (CollectionUtils.isNotEmpty(list)) {
                accountTypeId = list.get(0);
            }
        } catch (Exception e) {
            logger.error("Unable to load Customer.customerAccountTypeId by mobileNo:" + mobileNo, e);
        }

        return accountTypeId;
    }

    public List<AppUserModel> getAppUserModelByRegionId(Long regionId) {

        List<AppUserModel> appUserList = null;

        try {

            StringBuilder queryString = new StringBuilder();
            queryString.append(" select au from AppUserModel au, RetailerContactModel rc, RetailerModel r inner join fetch au.appUserIdUserDeviceAccountsModelList");
            queryString.append(" WHERE ");
            queryString.append(" au.relationRetailerContactIdRetailerContactModel.retailerContactId = rc.retailerContactId ");
            queryString.append(" and ");
            queryString.append(" rc.relationRetailerIdRetailerModel.retailerId = r.retailerId ");
            queryString.append(" and ");
            queryString.append(" r.regionModel.regionId = :regionId ");
            queryString.append(" order by au.firstName");

            String[] paramNames = {"regionId"};
            Object[] values = {regionId};

            appUserList = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return appUserList;
    }

    public List<String> getPreviousThreePasswordsByAppUserId(String username) {
        List<String> list = new ArrayList<String>(3);
        Session session = null;
        try {
            String hql = "select phm.password from AppUserPasswordHistoryModel phm where phm.relationAppUserIdAppUserModel.username = :username order by phm.createdOn desc";
            session = getHibernateTemplate().getSessionFactory().getCurrentSession();
            list = session.createQuery(hql).setString("username", username).setMaxResults(5).list();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (session != null) {
                SessionFactoryUtils.releaseSession(session, getSessionFactory());
            }
        }

        return list;
    }

    public void saveAppUserPasswordHistory(
            AppUserPasswordHistoryModel appUserPasswordHistory) {

        getHibernateTemplate().save(appUserPasswordHistory);
        getHibernateTemplate().flush();

    }

    @Override
    public List<AppUserModel> getAllAppUsersForPasswordChangeRequired() {
        String hql = "select distinct au from AppUserModel au left outer join fetch au.appUserIdAppUserPasswordHistoryModelList auh where au.passwordChangeRequired <> 1";
        //String hql = "from AppUserModel au where au.passwordChangeRequired <> 1";
        List<AppUserModel> users = getHibernateTemplate().find(hql);
        return users;
    }

    public List<AppUserModel> getAllAppUsersForAccountInactive() {
        String hql = "from AppUserModel au where au.credentialsExpired <> 1";
        List<AppUserModel> users = getHibernateTemplate().find(hql);
        return users;
    }

    public AppUserModel loadAppUserByCnicAndType(String cnic) {
        AppUserModel appUserModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(AppUserModel.class);
        criteria.add(Restrictions.eq("nic", cnic));
        Criterion criterionOne = Restrictions.eq("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", UserTypeConstantsInterface.CUSTOMER.longValue());
        Criterion criterionTwo = Restrictions.eq("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", UserTypeConstantsInterface.RETAILER.longValue());
        Criterion criterionThr = Restrictions.eq("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", UserTypeConstantsInterface.HANDLER.longValue());
        LogicalExpression expressionCriterion = Restrictions.or(criterionOne, criterionTwo);
        LogicalExpression expressionCriterion2 = Restrictions.or(expressionCriterion, criterionThr);

        criteria.add(expressionCriterion2);
        List<AppUserModel> list = getHibernateTemplate().findByCriteria(criteria);

        if (list != null && !list.isEmpty()) {

            appUserModel = list.get(0);
        }

        return appUserModel;
    }

    @Override
    public AppUserModel loadAppUserByEmailAddress(String emailAddress) {
        AppUserModel appUserModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(AppUserModel.class);
        criteria.add(Restrictions.eq("email", emailAddress).ignoreCase());

        List<AppUserModel> list = getHibernateTemplate().findByCriteria(criteria);

        if (list != null && !list.isEmpty()) {

            appUserModel = list.get(0);
        }

        return appUserModel;
    }

    //CNIC and Mobile
    @Override
    public AppUserModel loadAppUserByCnicAndMobileAndAppUserType(String mobileNo, String cnic, Long appUserTypeId) {
        AppUserModel appUserModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(AppUserModel.class);
        criteria.add(Restrictions.eq("mobileNo", mobileNo));
        criteria.add(Restrictions.eq("nic", cnic));
        criteria.add(Restrictions.eq("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", UserTypeConstantsInterface.CUSTOMER));
        criteria.add(Restrictions.isNotNull("relationAppUserTypeIdAppUserTypeModel.appUserTypeId"));
        List<AppUserModel> list = getHibernateTemplate().findByCriteria(criteria);


        if (list != null && !list.isEmpty()) {

            appUserModel = list.get(0);
        }
        return appUserModel;
    }

    public AppUserModel loadWalkinCustomerAppUserByCnic(String cnic) {
        AppUserModel appUserModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(AppUserModel.class);
        criteria.add(Restrictions.eq("nic", cnic));
        Criterion criterionOne = Restrictions.eq("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", UserTypeConstantsInterface.WALKIN_CUSTOMER.longValue());
//		LogicalExpression expressionCriterion = Restrictions.or(criterionOne);
        criteria.add(criterionOne);
        List<AppUserModel> list = getHibernateTemplate().findByCriteria(criteria);

        if (list != null && !list.isEmpty()) {

            appUserModel = list.get(0);
        }

        return appUserModel;
    }

    @Override
    public List<AppUserModel> findAppUsersByAppUserIds(
            List<Long> inClauseOfAppUserIds) throws FrameworkCheckedException {

        List<AppUserModel> appUsers;
        StringBuilder query = new StringBuilder("");
        query.append("from AppUserModel appUser ")
                .append("where appUser.appUserId in ( :inClauseOfAppUserIds) ");
        try {
            appUsers = this.getHibernateTemplate().findByNamedParam("from AppUserModel appUser " +
                    "where appUser.appUserId in ( :inClauseOfAppUserIds)", "inClauseOfAppUserIds", inClauseOfAppUserIds);
			/*SessionImpl session = (SessionImpl)getHibernateTemplate().getSessionFactory().getCurrentSession();
			appUsers = session.createQuery(query.toString()).setParameterList("inClauseOfAppUserIds", inClauseOfAppUserIds).list();
			SessionFactoryUtils.releaseSession(session, getSessionFactory());*/
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return appUsers;
    }

    @Override
    public List<String> findCNICsByAppUserIds(List<Long> appUserIds) throws FrameworkCheckedException {
        String query = "select au.nic from AppUserModel au where au.appUserId in ( :appUserIds )";
        List<String> cnicList = new ArrayList<String>(0);
        try {
            cnicList = this.getHibernateTemplate().findByNamedParam("select au.nic from AppUserModel au where au.appUserId in ( :appUserIds )", "appUserIds", appUserIds);
			/*SessionImpl session = (SessionImpl)getHibernateTemplate().getSessionFactory().getCurrentSession();
			cnicList = session.createQuery(query.toString()).setParameterList("appUserIds", appUserIds).list();
			SessionFactoryUtils.releaseSession(session, getSessionFactory());*/
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new FrameworkCheckedException(e.getLocalizedMessage());
        }

        return cnicList;
    }

    @Override
    public void updateAccountWithAccountTypeId(List<String> cnicList, Long accountTypeId) throws FrameworkCheckedException {
        SessionImpl session = null;
        try {
            session = (SessionImpl) getHibernateTemplate().getSessionFactory().getCurrentSession();
            Query queryToLoadAccountHolders = session.createQuery("select ah.accountHolderId from AccountHolderModel ah where ah.cnic in ( :cnicList ) ").setParameterList("cnicList", cnicList.toArray());
            List<Long> accountHolderIds = queryToLoadAccountHolders.list();
            if (accountHolderIds != null && CollectionUtils.isNotEmpty(accountHolderIds)) {
                Query queryToUpdateAccount = session.createQuery("update AccountModel acc set acc.customerAccountTypeModel.customerAccountTypeId = :accountTypeId " +
                        "where acc.relationAccountHolderIdAccountHolderModel.accountHolderId in ( :accountHolders ) ").setParameterList("accountHolders", accountHolderIds);
                queryToUpdateAccount.setParameter("accountTypeId", accountTypeId);
                queryToUpdateAccount.executeUpdate();
            } else {
                throw new FrameworkCheckedException("Customer is updated with new Account Type, But No Record Found to update in Account table");
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new FrameworkCheckedException(e.getMessage());
        } finally {

            SessionFactoryUtils.releaseSession(session, getSessionFactory());
        }
    }

    @Override
    public List<Object[]> getCNICExpiryAlertUsers() throws FrameworkCheckedException {
        Date currentDate = new Date();
        List<Object[]> resultSet = null;
        String query = "select appUserModel.appUserId, appUserModel.firstName, appUserModel.lastName, appUserModel.mobileNo, appUserModel.nicExpiryDate from AppUserModel appUserModel where appUserModel.cnicExpiryMsgSent <> 1 and appUserModel.appUserTypeId <> 9";
        resultSet = getHibernateTemplate().find(query);
        return resultSet;
    }

    @Override
    public Long getAppUserRetailerContactId(Long appUserId) throws FrameworkCheckedException {
        String query = "select appUserModel.relationRetailerContactIdRetailerContactModel.retailerContactId from AppUserModel appUserModel where appUserModel.appUserId = ?";
        List list = getHibernateTemplate().find(query, appUserId);
        Long retailerContactId = (Long) list.get(0);
        return retailerContactId;
    }

    @Override
    public Long getAppUserTypeId(Long appUserId) throws FrameworkCheckedException {
        String query = "select appUserModel.relationAppUserTypeIdAppUserTypeModel.appUserTypeId from AppUserModel appUserModel where appUserModel.appUserId = ?";
        List list = getHibernateTemplate().find(query, appUserId);
        Long appUserTypeId = (Long) list.get(0);
        return appUserTypeId;
    }

    @Override
    public boolean isEmployeeIdUnique(AppUserModel appUserModel) {
        String hql = "from AppUserModel where employeeId = ?";
        List<AppUserModel> appUserModelList = getHibernateTemplate().find(hql, new Object[]{appUserModel.getEmployeeId()});

        if (null != appUserModel.getAppUserId()) {
            if (appUserModelList.size() > 1) {
                return false;
            } else if (appUserModelList.size() == 1 && appUserModelList.get(0).getAppUserId().longValue() != appUserModel.getAppUserId().longValue()) {
                return false;
            } else
                return true;
        } else {

            if (appUserModelList.size() == 0)
                return true;
            else
                return false;
        }
    }

    @Override
    public AppUserModel checkAppUserTypeAsWalkinCustoemr(String mobileNo) {

        AppUserModel appUserModel = null;
        List<Long> collection = new ArrayList<>();
        collection.add(UserTypeConstantsInterface.BANK.longValue());
        collection.add(UserTypeConstantsInterface.CUSTOMER.longValue());
        collection.add(UserTypeConstantsInterface.DISTRIBUTOR.longValue());
        collection.add(UserTypeConstantsInterface.INOV8.longValue());
        collection.add(UserTypeConstantsInterface.MNO.longValue());
        collection.add(UserTypeConstantsInterface.PRODUCT_SUPPLIER.longValue());
        collection.add(UserTypeConstantsInterface.RETAILER.longValue());
        collection.add(UserTypeConstantsInterface.SUPPLIER.longValue());

        DetachedCriteria criteria = DetachedCriteria.forClass(AppUserModel.class);
        criteria.add(Restrictions.eq("mobileNo", mobileNo));
        criteria.add(Restrictions.in("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", collection));

        List<AppUserModel> list = getHibernateTemplate().findByCriteria(criteria);

        if (list != null && !list.isEmpty()) {
            appUserModel = list.get(0);
        }

        return appUserModel;
    }

    @Override
    public boolean isMobileNumUnique(AppUserModel appUserModel) {
        String hql = "from AppUserModel where mobileNo = ? and relationAppUserTypeIdAppUserTypeModel.appUserTypeId = ?";
        List<AppUserModel> appUserModelList = getHibernateTemplate().find(hql, new Object[]{appUserModel.getMobileNo(), appUserModel.getAppUserTypeId()});

        if (null != appUserModel.getAppUserId()) {
            if (appUserModelList.size() > 1) {
                return false;
            } else if (appUserModelList.size() == 1 && appUserModelList.get(0).getAppUserId().longValue() != appUserModel.getAppUserId().longValue()) {
                return false;
            } else
                return true;
        } else {

            if (appUserModelList.size() == 0)
                return true;
            else
                return false;
        }
    }

    @Override
    public boolean isUserIdUnique(AppUserModel appUserModel) {
        String hql = "from AppUserModel where username = ?";
        List<AppUserModel> appUserModelList = getHibernateTemplate().find(hql, new Object[]{appUserModel.getUsername()});

        if (null != appUserModel.getAppUserId()) {
            if (appUserModelList.size() > 1) {
                return false;
            } else if (appUserModelList.size() == 1 && appUserModelList.get(0).getAppUserId().longValue() != appUserModel.getAppUserId().longValue()) {
                return false;
            } else
                return true;
        } else {

            if (appUserModelList.size() == 0)
                return true;
            else
                return false;
        }
    }

    @Override
    public List<AppUserModel> findAppUserByCnicAndMobile(String mobileNo, String cnic) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AppUserModel.class);
        Criterion criterionOne = Restrictions.eq("mobileNo", mobileNo);
        Criterion criterionTwo = Restrictions.eq("nic", cnic);
        LogicalExpression expressionCriterion = Restrictions.or(criterionOne, criterionTwo);
        detachedCriteria.add(expressionCriterion);
        List<AppUserModel> list = getHibernateTemplate().findByCriteria(detachedCriteria);
        return list;
    }

    @Override
    public boolean isCnicUnique(AppUserModel appUserModel) {
        String hql = "from AppUserModel where nic = ? and relationAppUserTypeIdAppUserTypeModel.appUserTypeId = ?";
        List<AppUserModel> appUserModelList = getHibernateTemplate().find(hql, new Object[]{appUserModel.getNic(), appUserModel.getAppUserTypeId()});

        if (null != appUserModel.getAppUserId()) {
            if (appUserModelList.size() > 1) {
                return false;
            } else if (appUserModelList.size() == 1 && appUserModelList.get(0).getAppUserId().longValue() != appUserModel.getAppUserId().longValue()) {
                return false;
            } else
                return true;
        } else {

            if (appUserModelList.size() == 0)
                return true;
            else
                return false;
        }
    }

    @Override
    public List<Object[]> getCNICsToMarkAccountDormant() throws FrameworkCheckedException {

        //String hql = "select aum.nic from AppUserModel aum where aum.accountStateId is null or aum.accountStateId <> " + AccountStateConstantsInterface.ACCOUNT_STATE_DORMANT;

//		String hqlCustomers = "select aum.nic, aum.relationCustomerIdCustomerModel.relationCustomerAccountTypeIdCustomerAccountTypeModel.dormantTimePeriod,";
//		hqlCustomers += " aum.dormancyRemovedOn, aum.mobileNo, aum.dob from AppUserModel aum ";
//		hqlCustomers += "where aum.nic is not null ";
//		hqlCustomers += "and aum.accountClosedSettled != 1 ";//
//		hqlCustomers += "and aum.accountClosedUnsettled != 1 ";
//		hqlCustomers += "and aum.relationAppUserTypeIdAppUserTypeModel.appUserTypeId is not null ";
//		hqlCustomers += "and aum.relationAppUserTypeIdAppUserTypeModel.appUserTypeId = " + UserTypeConstantsInterface.CUSTOMER;
//		hqlCustomers += " and aum.relationCustomerIdCustomerModel.relationCustomerAccountTypeIdCustomerAccountTypeModel.dormantTimePeriod is not null ";
//		hqlCustomers += "and aum.relationCustomerIdCustomerModel.relationCustomerAccountTypeIdCustomerAccountTypeModel.dormantMarkingEnabled=1 ";
//		hqlCustomers += "and aum.relationRegistrationStateModel.registrationStateId in ( " + RegistrationStateConstantsInterface.BULK_RQST_RCVD +", ";
//		hqlCustomers += RegistrationStateConstantsInterface.RQST_RCVD+ " , " + RegistrationStateConstantsInterface.VERIFIED + " )";
//		hqlCustomers += " and aum.accountStateId in ( "+ AccountStateConstantsInterface.ACCOUNT_STATE_COLD + ", ";
//		hqlCustomers += AccountStateConstantsInterface.ACCOUNT_STATE_WARM + " )";

        String hqlRetailers = "select aum.nic, aum.relationRetailerContactIdRetailerContactModel.relationOlaCustomerAccountTypeModel.dormantTimePeriod, ";
        hqlRetailers += " aum.dormancyRemovedOn, aum.mobileNo, aum.dob from AppUserModel aum ";
        hqlRetailers += "where aum.nic is not null ";
        hqlRetailers += "and aum.accountClosedSettled != 1 ";//
        hqlRetailers += "and aum.accountClosedUnsettled != 1 ";
        hqlRetailers += "and aum.relationAppUserTypeIdAppUserTypeModel.appUserTypeId is not null ";
        hqlRetailers += "and aum.relationAppUserTypeIdAppUserTypeModel.appUserTypeId = " + UserTypeConstantsInterface.RETAILER;
        hqlRetailers += " and aum.relationRetailerContactIdRetailerContactModel.relationOlaCustomerAccountTypeModel.dormantTimePeriod is not null ";
        hqlRetailers += "and aum.relationRetailerContactIdRetailerContactModel.relationOlaCustomerAccountTypeModel.dormantMarkingEnabled=1 ";
        hqlRetailers += "and aum.relationRegistrationStateModel.registrationStateId in ( " + RegistrationStateConstantsInterface.BULK_RQST_RCVD + ", ";
        hqlRetailers += RegistrationStateConstantsInterface.RQST_RCVD + " , " + RegistrationStateConstantsInterface.VERIFIED + " )";
        hqlRetailers += " and aum.accountStateId in ( " + AccountStateConstantsInterface.ACCOUNT_STATE_COLD + ", ";
        hqlRetailers += AccountStateConstantsInterface.ACCOUNT_STATE_WARM + " )";

//		String hqlHandelers = "select aum.nic, aum.relationHandlerIdHandlerModel.relationAccountTypeIdOlaCustomerAccountTypeModel.dormantTimePeriod, ";
//		hqlHandelers += " aum.dormancyRemovedOn, aum.mobileNo, aum.dob from AppUserModel aum ";
//		hqlHandelers += "where aum.nic is not null " ;
//        hqlHandelers += "and aum.accountClosedSettled != 1 ";//
//        hqlHandelers += "and aum.accountClosedUnsettled != 1 ";
//		hqlHandelers += "and aum.relationAppUserTypeIdAppUserTypeModel.appUserTypeId is not null ";
//		hqlHandelers += "and aum.relationAppUserTypeIdAppUserTypeModel.appUserTypeId = " + UserTypeConstantsInterface.HANDLER;
//		hqlHandelers +=" and aum.relationHandlerIdHandlerModel.relationAccountTypeIdOlaCustomerAccountTypeModel.dormantTimePeriod is not null ";
//		hqlHandelers +="and aum.relationHandlerIdHandlerModel.relationAccountTypeIdOlaCustomerAccountTypeModel.dormantMarkingEnabled=1 ";
//		hqlHandelers += "and aum.relationRegistrationStateModel.registrationStateId in ( " + RegistrationStateConstantsInterface.BULK_RQST_RCVD +", ";
//		hqlHandelers += RegistrationStateConstantsInterface.RQST_RCVD+ " , " + RegistrationStateConstantsInterface.VERIFIED + " )";
//		hqlHandelers += " and aum.accountStateId in ( "+ AccountStateConstantsInterface.ACCOUNT_STATE_COLD + ", ";
//		hqlHandelers += AccountStateConstantsInterface.ACCOUNT_STATE_WARM + " )";

        List<Object[]> cnicList = getHibernateTemplate().find(hqlRetailers);
//		cnicList.addAll(getHibernateTemplate().find(hqlRetailers));
//		cnicList.addAll(getHibernateTemplate().find(hqlHandelers));
        //List<String> cnicList = getHibernateTemplate().find(hqlCustomer);

        return cnicList;
    }

    @Override
    public List<AppUserModel> getAppUserModelListByCNICs(List<String> cnicList) throws FrameworkCheckedException {

        List<AppUserModel> appUserModelList = this.getHibernateTemplate().findByNamedParam("from AppUserModel aum where aum.nic in (:cnicList)", "cnicList", cnicList);
        return appUserModelList;
    }

    @Override
    public List<AppUserModel> getAppUsersByTypeForHsmCall(Long appUserTypeId, String strDate1, String strDate2, int limit)
            throws FrameworkCheckedException {
        List<AppUserModel> appUserModels;
        StringBuilder hql = new StringBuilder("");
        if (appUserTypeId.longValue() == 2L) {//customer
            hql.append("SELECT au FROM AppUserModel au INNER JOIN au.relationCustomerIdCustomerModel cm WHERE au.relationAppUserTypeIdAppUserTypeModel.appUserTypeId=2 and au.passwordHint IS NULL "
                    + " AND au.createdOn BETWEEN '" + strDate1 + "' AND '" + strDate2 + "'");
        } else if (appUserTypeId.longValue() == 12L) { //handler
            hql.append("SELECT au FROM AppUserModel au INNER JOIN au.relationHandlerIdHandlerModel hm WHERE au.relationAppUserTypeIdAppUserTypeModel.appUserTypeId=12 and au.passwordHint IS NULL "
                    + " AND au.createdOn BETWEEN '" + strDate1 + "' AND '" + strDate2 + "'");
        } else { //retailer
            hql.append("SELECT au FROM AppUserModel au INNER JOIN au.relationRetailerContactIdRetailerContactModel rcm WHERE au.relationAppUserTypeIdAppUserTypeModel.appUserTypeId=3 and au.passwordHint IS NULL "
                    + " AND au.createdOn BETWEEN '" + strDate1 + "' AND '" + strDate2 + "'");
        }

        appUserModels = (List<AppUserModel>) getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString()).setMaxResults(limit).list();

        return appUserModels;
    }

    @Override
    public AppUserModel loadAppUserModelByUserId(String userId) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("select model.relationAppUserIdAppUserModel from UserDeviceAccountsModel model ");
        queryStr.append("where model.userId =:userId");

        Query query = getSession().createQuery(queryStr.toString());
        query.setParameter("userId", userId);

        try {
            List<AppUserModel> usersList = query.list();

            if (!CollectionUtils.isEmpty(usersList)) {
                return usersList.get(0);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public List<Object[]> loadUserByCNIC(String cnic, String mobile) {

        Query query = getSession().createSQLQuery(WALK_IN_SELECTION_QUERY);
        query.setParameter("cnic1", cnic);
        query.setParameter("mobile", mobile);

        return query.list();
    }

    @Override
    public List<Object[]> loadUserByMobileNo(String mobile, Long appUserTypeId) {
        Query query = getSession().createSQLQuery(APP_USER_SELECTION_QUERY);
        query.setParameter("mobile", mobile);
        query.setLong("appUserTypeId", appUserTypeId);

        return query.list();
    }

    public AppUserModel loadRetailerAppUserModelByAppUserId(Long appUserId) {
        AppUserModel appUserModel = null;

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT user ");
        queryBuilder.append(" FROM AppUserModel user ");
        queryBuilder.append(" WHERE ");
        queryBuilder.append(" user.appUserId = :appUserId ");
        queryBuilder.append(" AND");
        queryBuilder.append(" user.relationAppUserTypeIdAppUserTypeModel.appUserTypeId = " + UserTypeConstantsInterface.RETAILER);
        String[] paramNames = {"appUserId"};
        Object[] values = {appUserId};
        try {
            List<AppUserModel> userList = getHibernateTemplate().findByNamedParam(queryBuilder.toString(), paramNames, values);
            if (CollectionUtils.isNotEmpty(userList)) {
                appUserModel = userList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appUserModel;
    }

    @Override
    public AppUserModel loadAppUserByCNICAndType(String cnic, Long... appUserTypes) throws FrameworkCheckedException {

        DetachedCriteria criteria = DetachedCriteria.forClass(AppUserModel.class);
        criteria.add(Restrictions.eq("nic", cnic));
        Criterion criterions = Restrictions.in("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", appUserTypes);
        criteria.add(criterions);
        List<AppUserModel> list = getHibernateTemplate().findByCriteria(criteria);

        AppUserModel appUserModel = null;

        if (list != null && !list.isEmpty()) {
            appUserModel = list.get(0);
        }

        return appUserModel;
    }

    @Override
    public List<BlacklistMarkingBulkUploadVo> findAppUsersByNICs(List<String> nics) throws FrameworkCheckedException {

        List<BlacklistMarkingBulkUploadVo> blacklistMarkingBulkUploadVoList = new ArrayList<>(0);
        String queryString = "select au.nic as cnic,au.accountClosedUnsettled as accountClosedUnsettled, " +
                "au.accountClosedSettled as accountClosedSettled ,au.appUserId as appUserId," +
                "au.relationRegistrationStateModel.registrationStateId as registrationStateId," +
                "au.relationPrevRegistrationStateModel.registrationStateId as prevRegistrationStateId" +
                " from AppUserModel au where au.nic in (:nics)";
        Query query = getSession().createQuery(queryString);
		/*query.setParameterList("nics",nics);
		blacklistMarkingBulkUploadVoList = query.setResultTransformer( Transformers.aliasToBean(BlacklistMarkingBulkUploadVo.class)).list();*/

        List<java.lang.String> inClasue = new ArrayList<java.lang.String>(0);

        for (String nic : nics) {
            inClasue.add(nic);
        }


        if (inClasue.size() <= 1000) {
            query.setParameterList("nics", inClasue);
            blacklistMarkingBulkUploadVoList = query.setResultTransformer(Transformers.aliasToBean(BlacklistMarkingBulkUploadVo.class)).list();

        } else {//handle oracle limit of 1000 IN CLAUSE
            List<String>[] chunks = this.chunks(inClasue, 999);
            for (List<String> inClauseByThousands : chunks) {
                query.setParameterList("nics", inClauseByThousands);
                blacklistMarkingBulkUploadVoList.addAll(query.setResultTransformer(Transformers.aliasToBean(BlacklistMarkingBulkUploadVo.class)).list());

            }
        }

        return blacklistMarkingBulkUploadVoList;
    }

    @Override
    public List<CustomerACNatureMarkingUploadVo> findAppUsersByCnics(List<String> cnics) throws FrameworkCheckedException {
        List<CustomerACNatureMarkingUploadVo> customerACNatureMarkingUploadVoList = new ArrayList<>(0);
        String queryString = "select au.nic as cnic," +
                "au.accountClosedSettled as accountClosedSettled ,au.appUserId as appUserId," +
                "au.relationRegistrationStateModel.registrationStateId as registrationStateId," +
                "au.relationPrevRegistrationStateModel.registrationStateId as prevRegistrationStateId" +
                " from AppUserModel au where au.nic in (:nics)";

        Query query = getSession().createQuery(queryString);

        List<java.lang.String> inClasue = new ArrayList<java.lang.String>(0);

        for (String nic : cnics) {
            inClasue.add(nic);
        }

        if (inClasue.size() <= 1000) {
            query.setParameterList("nics", inClasue);
            customerACNatureMarkingUploadVoList = query.setResultTransformer(Transformers.aliasToBean(CustomerACNatureMarkingUploadVo.class)).list();

        } else {//handle oracle limit of 1000 IN CLAUSE
            List<String>[] chunks = this.chunks(inClasue, 999);
            for (List<String> inClauseByThousands : chunks) {
                query.setParameterList("nics", inClauseByThousands);
                customerACNatureMarkingUploadVoList.addAll(query.setResultTransformer(Transformers.aliasToBean(CustomerACNatureMarkingUploadVo.class)).list());

            }
        }
        return customerACNatureMarkingUploadVoList;
    }

    private List[] chunks(final List<String> pList, final int pSize) {
        if (pList == null || pList.size() == 0 || pSize == 0) return new List[]{};
        if (pSize < 0) return new List[]{pList};

        // Calculate the number of batches
        int numBatches = (pList.size() / pSize) + 1;

        // Create a new array of Lists to hold the return value
        List[] batches = new List[numBatches];

        for (int index = 0; index < numBatches; index++) {
            int count = index + 1;
            int fromIndex = Math.max(((count - 1) * pSize), 0);
            int toIndex = Math.min((count * pSize), pList.size());
            batches[index] = pList.subList(fromIndex, toIndex);
        }

        return batches;
    }

    @Override
    public boolean isUserFiler(Long appUserId) {

        boolean isFiler = false;
        try {
            String sql = "SELECT IS_FILER FROM APP_USER  WHERE APP_USER_ID = ?";
            int val = jdbcTemplate.queryForInt(
                    sql, new Object[]{appUserId});
            if (val == 1) {
                isFiler = true;
            }
        } catch (Exception exp) {
            logger.error("An error has occurred");
        }
        return isFiler;
    }

    @Override
    public String getCustomerId(String NIC)
            throws FrameworkCheckedException {
        String sql = "SELECT CUSTOMER_ID FROM APP_USER WHERE NIC = ? AND APP_USER_TYPE_ID = ?";
        String custId = jdbcTemplate.queryForObject(sql, new Object[]{NIC, 2L}, String.class);
        return custId;
    }

    @Override
    public AppUserModel getAppUserWithRegistrationStates(String mobileNo, String cnic, Long ...registrationStates) {
        AppUserModel appUserModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(AppUserModel.class);
        criteria.add(Restrictions.eq("mobileNo", mobileNo));
        criteria.add(Restrictions.eq("nic", cnic));
        criteria.add(Restrictions.eq("relationAppUserTypeIdAppUserTypeModel.appUserTypeId", UserTypeConstantsInterface.CUSTOMER));
        criteria.add(Restrictions.isNotNull("relationRegistrationStateModel.registrationStateId"));
        criteria.add(Restrictions.in("relationRegistrationStateModel.registrationStateId", registrationStates));
        List<AppUserModel> list = getHibernateTemplate().findByCriteria(criteria);

        if (list != null && !list.isEmpty()) {

            appUserModel = list.get(0);
        }
        return appUserModel;
    }

    @Override
    public AppUserModel loadAppUserByCnic256(String shaCnic) throws FrameworkCheckedException {
        DetachedCriteria criteria = DetachedCriteria.forClass(AppUserModel.class);
        criteria.add(Restrictions.eq("nic", shaCnic));
        List<AppUserModel> list = getHibernateTemplate().findByCriteria(criteria);

        AppUserModel appUserModel = null;

        if (list != null && !list.isEmpty()) {
            appUserModel = list.get(0);
        }

        return appUserModel;
    }

    private final class CountToBooleanExtractor implements ResultSetExtractor<Boolean> {
        @Override
        public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            int count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            return count > 0;
        }
    }

}
