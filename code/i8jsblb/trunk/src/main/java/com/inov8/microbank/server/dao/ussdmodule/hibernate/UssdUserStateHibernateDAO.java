package com.inov8.microbank.server.dao.ussdmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.UserStateModel;
import com.inov8.microbank.server.dao.ussdmodule.UssdUserStateDAO;

public class UssdUserStateHibernateDAO extends BaseHibernateDAO<UserStateModel,Long,UssdUserStateDAO> implements 
UssdUserStateDAO {

	public boolean deleteUserState(String msisdn) {
		boolean retVal=false;
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("delete UserStateModel where msisdn ='");
		stringBuilder.append(msisdn+"'");
//		stringBuilder.append("' or senderId=");
//		stringBuilder.append(senderID);
	    int rows=this.getHibernateTemplate().bulkUpdate(stringBuilder.toString());
	    if(rows >0){
	    	retVal=true;
	    }
	    return retVal;
		
	}

}
