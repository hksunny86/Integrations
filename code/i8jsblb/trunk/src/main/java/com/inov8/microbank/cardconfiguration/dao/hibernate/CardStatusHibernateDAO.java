package com.inov8.microbank.cardconfiguration.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.cardconfiguration.dao.CardStatusDAO;
import com.inov8.microbank.cardconfiguration.model.CardStatusModel;

public class CardStatusHibernateDAO extends BaseHibernateDAO<CardStatusModel,Long,CardStatusDAO> implements CardStatusDAO{
}
