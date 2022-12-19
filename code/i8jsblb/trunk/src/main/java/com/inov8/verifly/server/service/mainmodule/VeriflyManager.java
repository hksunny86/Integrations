package com.inov8.verifly.server.service.mainmodule;

//import com.inov8.framework.common.exception.*;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.logmodule.LogWrapper;
/**
 *
 * @author irfan mirza
 * @version 1.0
 * @date  06-Sep-2006
 *
 */

public interface VeriflyManager {
    public VeriflyBaseWrapper generatePIN(VeriflyBaseWrapper wrapper)throws Exception;
    public VeriflyBaseWrapper verifyPIN(VeriflyBaseWrapper wrapper)throws Exception;
    public VeriflyBaseWrapper changePIN(VeriflyBaseWrapper wrapper)throws Exception;
    public VeriflyBaseWrapper resetPIN(VeriflyBaseWrapper wrapper)throws Exception;
    public VeriflyBaseWrapper activatePIN(VeriflyBaseWrapper wrapper)throws Exception;
    public VeriflyBaseWrapper deactivatePIN(VeriflyBaseWrapper wrapper)throws Exception;
    public VeriflyBaseWrapper getLog(VeriflyBaseWrapper wrapper)throws Exception;
    public VeriflyBaseWrapper modifyAccountInfo(VeriflyBaseWrapper wrapper)throws Exception;
    public VeriflyBaseWrapper modifyAccountInfoForBBAgents(VeriflyBaseWrapper wrapper)throws Exception;
    public VeriflyBaseWrapper deleteAccount(VeriflyBaseWrapper wrapper)throws Exception;
    public VeriflyBaseWrapper changeAccountNick(VeriflyBaseWrapper wrapper)throws Exception;
    public VeriflyBaseWrapper generateOneTimePin(VeriflyBaseWrapper wrapper)throws Exception;
    public VeriflyBaseWrapper verifyOneTimePin(VeriflyBaseWrapper wrapper)throws Exception;
    public VeriflyBaseWrapper markAsDeleted(VeriflyBaseWrapper wrapper)throws Exception;
    public LogWrapper getLog(LogWrapper logWrapper)throws Exception;
	public VeriflyBaseWrapper verifyCredentials(VeriflyBaseWrapper wrapper) throws Exception;
	public String getAccountNumber(Long customerId) throws Exception;
	AccountInfoModel getAccountInfoModel(Long customerId, Long paymentModeId) throws Exception;
	AccountInfoModel getAccountInfoModel(Long customerId, String accountNick) throws Exception;
}
