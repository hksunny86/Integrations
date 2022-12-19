package com.inov8.microbank.server.dao.distributormodule.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.server.dao.distributormodule.DistributorContactDAO;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public class DistributorContactHibernateDAO
    extends BaseHibernateDAO<DistributorContactModel, Long, DistributorContactDAO>
    implements DistributorContactDAO
{

  public boolean isManagingContact( Long fromDistributor, Long toDistributor )
  {
    Session session = this.getSession();
    Query sqlQuery = session.createSQLQuery( "SELECT child.DISTRIBUTOR_CONTACT_ID as CHLD_DIST_CONT_ID"
        + " FROM Distributor_Contact child WHERE child.MANAGING_CONTACT_ID IN "
        + " ("
        + " select Distributor_Contact.DISTRIBUTOR_CONTACT_ID "
        + " from   Distributor_Contact "
        + " start with Distributor_Contact.DISTRIBUTOR_CONTACT_ID = ? "
        + " connect by prior Distributor_Contact.DISTRIBUTOR_CONTACT_ID = Distributor_Contact.MANAGING_CONTACT_ID "
        + " )"
        + "AND child.DISTRIBUTOR_CONTACT_ID = ? "
        + " ").addScalar("CHLD_DIST_CONT_ID", Hibernate.LONG ) ;

    List result = sqlQuery.setLong(0,fromDistributor).setLong(1,toDistributor).list()  ;

    SessionFactoryUtils.releaseSession(session, getSessionFactory());

    if( result.size() > 0 )
      return true ;


    return false;
  }

}
