package com.inov8.microbank.common.util;

import org.hibernate.transaction.JNDITransactionManagerLookup;

public class AS7TransactionManagerLookup extends JNDITransactionManagerLookup{

	@Override
	public String getUserTransactionName() {
		// TODO Auto-generated method stub
		return "UserTransaction";
	}

	@Override
	protected String getName() {
		// TODO Auto-generated method stub
		return "java:jboss/TransactionManager";
	}

}
