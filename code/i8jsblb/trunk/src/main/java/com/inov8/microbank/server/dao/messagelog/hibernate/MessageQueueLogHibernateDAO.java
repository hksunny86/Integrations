/**
 * 
 */
package com.inov8.microbank.server.dao.messagelog.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.MessageQueueLogModel;
import com.inov8.microbank.server.dao.messagelog.MessageQueueLogDAO;

/**
 * @author Kashif Bashir
 *
 */
public class MessageQueueLogHibernateDAO extends BaseHibernateDAO <MessageQueueLogModel, Long, MessageQueueLogDAO> implements MessageQueueLogDAO {


}
