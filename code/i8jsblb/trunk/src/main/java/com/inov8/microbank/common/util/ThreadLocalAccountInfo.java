
/**
 * 
 */
package com.inov8.microbank.common.util;

import com.inov8.verifly.common.model.AccountInfoModel;

/**
 *  
 * Creation Time: Oct 4, 2006 3:43:23 PM
 */
@SuppressWarnings("all")
public class ThreadLocalAccountInfo
{

	private static final ThreadLocal customerAccountInfo = new ThreadLocal();
	private static final ThreadLocal loggedInCustomerAccountInfo = new ThreadLocal();
	private static final ThreadLocal loggedInAgentAccountInfo = new ThreadLocal();
	private static final ThreadLocal agentAccountInfo = new ThreadLocal();
	
	
	private ThreadLocalAccountInfo()
	{
	}

	public static AccountInfoModel getCustomerAccountInfoModel()
	{
		return (AccountInfoModel) customerAccountInfo.get();
	}

	public static void setCustomerAccountInfoModel(AccountInfoModel accountInfoModel)
	{
//		customerAccountInfo.set(accountInfoModel);
	}

	public static AccountInfoModel getLoggedInCustomerAccountInfo() {
		return (AccountInfoModel)loggedInCustomerAccountInfo.get();
	}

	public static void setLoggedInCustomerAccountInfo(AccountInfoModel accountInfoModel) {
//		loggedInCustomerAccountInfo.set(accountInfoModel);
	}

	public static AccountInfoModel getLoggedInAgentAccountInfo() {
		return (AccountInfoModel)loggedInAgentAccountInfo.get();
	}

	public static void setLoggedInAgentAccountInfo(AccountInfoModel accountInfoModel) {
//		loggedInAgentAccountInfo.set(accountInfoModel);
	}

	public static AccountInfoModel getAgentAccountInfo() {
		return (AccountInfoModel)agentAccountInfo.get();
	}

	public static void setAgentAccountInfo(AccountInfoModel accountInfoModel) {
//		agentAccountInfo.set(accountInfoModel);
	}

	public static void remove()
	{
//		System.out.println("[ ThreadLocalAccountInfo ] Removing all ThreadlocalAccountInfo objects Start...");

		if (getAgentAccountInfo() != null) {
//			System.out.println("[ ThreadLocalAccountInfo ]  Removing Account No: " + getAgentAccountInfo().getAccountNo());
		}
		
		if (getLoggedInAgentAccountInfo() != null) {
//			System.out.println("[ ThreadLocalAccountInfo ]  Removing Account No: " + getLoggedInAgentAccountInfo().getAccountNo());
		}
		
		if (getCustomerAccountInfoModel() != null) {
//			System.out.println("[ ThreadLocalAccountInfo ]  Removing Account No: " + getCustomerAccountInfoModel().getAccountNo());
		}
		
		if (getLoggedInCustomerAccountInfo() != null) {
//			System.out.println("[ ThreadLocalAccountInfo ]  Removing Account No: " + getLoggedInCustomerAccountInfo().getAccountNo());
		}
		
		agentAccountInfo.remove();
		customerAccountInfo.remove();
		loggedInAgentAccountInfo.remove();
		loggedInCustomerAccountInfo.remove();
//		System.out.println("[ ThreadLocalAccountInfo ] Removing all ThreadlocalAccountInfo objects End...");
	}

}