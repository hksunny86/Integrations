package com.inov8.microbank.cardconfiguration.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.cardconfiguration.dao.CardStateDAO;
import com.inov8.microbank.cardconfiguration.model.CardStateModel;

public class CardStateHibernateDAO extends BaseHibernateDAO<CardStateModel,Long,CardStateDAO> implements CardStateDAO{
}
