package com.inov8.microbank.common.util;

import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;

/**
 * @author omar.butt 
 * Creation Time: Sep 26, 2016 2:41:23 PM
 */
public class ThreadLocalBillInfo
{

	private static ThreadLocal<UtilityBillVO> billInfo = new ThreadLocal<UtilityBillVO>();

	private ThreadLocalBillInfo(){ }

	public static UtilityBillVO getBillInfo(){
		return (UtilityBillVO) billInfo.get();
	}

	public static void setBillInfo(UtilityBillVO utilityBillInfo){
		billInfo.set(utilityBillInfo);
	}

	public static void remove(){
		billInfo.remove();
	}

}