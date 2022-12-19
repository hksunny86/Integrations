package com.inov8.microbank.common.util;

/*Created By : Hassan Javaid
 *Date : 15-10-2014 
 * */

import java.util.HashMap;
import java.util.Map;

public enum PoolAccountsEnum
{
	WHT_OLA_ACCOUNT(10000000013L,PoolAccountConstantsInterface.WHT_OLA_ACCOUNT_ID),
	FUND_TRANSFER_SUNDRY_OLA(10000000020L,PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID),
	BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_OLA(10000000021L,PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_CORE),
	AGENT_DEFAULT_COMMISSION_ACCOUNT_OLA(10000000023L,PoolAccountConstantsInterface.AGENT_DEFAULT_COMMISSION_ACCOUNT_CORE),
	SALES_TEAM_ACCOUNT_OLA(10000000019L,PoolAccountConstantsInterface.SALES_TEAM_CORE_ACCOUNT_ID),
	BANK_COMMISSION_OLA(10000000014L,PoolAccountConstantsInterface.BANK_INCOME_CORE_ACCOUNT_ID),
	FED_OLA_ACCOUNT(10000000012L,PoolAccountConstantsInterface.FED_OLA_ACCOUNT_ID),
	BULK_DISBURSEMENT_POOL_ACCOUNT_OLA(10000000017L,PoolAccountConstantsInterface.BULK_DISBURSEMENT_POOL_ACCOUNT_CORE);
	
	PoolAccountsEnum(Long olaAccountNo, Long coreAccountStakeHolderBankInfo)
    {
        this.olaAccountNo = olaAccountNo;
        this.coreAccountStakeHolderBankInfo = coreAccountStakeHolderBankInfo;
    }

    private static final Map<Long,Long> olaCoreAccountNoMap;
    
	private Long olaAccountNo;

    private Long coreAccountStakeHolderBankInfo;

    public static Long getCoreAccNoByOlaAccNo(Long olaAccountNo)
    {
		return olaCoreAccountNoMap.get(olaAccountNo);
	}

	public Long getOlaAccountNo() {
		return olaAccountNo;
	}
	public Long getCoreAccountStakeHolderBankInfo() {
		return coreAccountStakeHolderBankInfo;
	}

	static
	{
		PoolAccountsEnum[] poolAccountsEnums = values();
		olaCoreAccountNoMap = new HashMap<>(poolAccountsEnums.length);
		for (PoolAccountsEnum poolAccountsEnum : poolAccountsEnums)
		{
			olaCoreAccountNoMap.put(poolAccountsEnum.getOlaAccountNo(), poolAccountsEnum.getCoreAccountStakeHolderBankInfo()); 
		}
	}
}
