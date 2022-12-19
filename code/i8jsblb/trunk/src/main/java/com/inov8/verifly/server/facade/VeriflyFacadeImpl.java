package com.inov8.verifly.server.facade;

import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.TypeMismatchDataAccessException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.util.Error;
import com.inov8.verifly.common.util.Errors;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.common.wrapper.logmodule.LogWrapper;
import com.inov8.verifly.common.wrapper.logmodule.LogWrapperImpl;
import com.inov8.verifly.server.service.logmodule.LogManager;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;

/**
 * INOV8 INC
 * <p>
 * interact with dao classes for database operations
 *
 *
 * @author Irfan Mirza
 * @author Basit Mehr
 * @version 1.0
 */
public class VeriflyFacadeImpl implements VeriflyFacade {

    private VeriflyManager veriflyManager;
    private LogManager logManager;
    private MessageSource messageSource;
    private final transient Log logger = LogFactory.getLog(VeriflyFacadeImpl.class);

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public VeriflyBaseWrapper verifyOneTimePin(VeriflyBaseWrapper wrapper) {
        VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();
        try {
            localBaseWrapper = veriflyManager.verifyOneTimePin(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }


    public VeriflyBaseWrapper generateOneTimePin(VeriflyBaseWrapper wrapper) {
        VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();
        try {
            localBaseWrapper = veriflyManager.generateOneTimePin(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }


    public VeriflyBaseWrapper changeAccountNick(VeriflyBaseWrapper wrapper) {
        VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();
        try {
            localBaseWrapper = veriflyManager.changeAccountNick(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }

    
    /**
     * activatePIN
     *
     * @param wrapper VeriflyBaseWrapper
     * @return VeriflyBaseWrapper
     * @throws FrameworkCheckedException
     * @todo Implement this
     *   com.inov8.verifly.server.service.veriflymodule.VeriflyManager method
     */
    public VeriflyBaseWrapper activatePIN(VeriflyBaseWrapper wrapper) {
        VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();
        try {
            localBaseWrapper = veriflyManager.activatePIN(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }

    /**
     * changePIN
     *
     * @param wrapper VeriflyBaseWrapper
     * @return VeriflyBaseWrapper
     * @throws FrameworkCheckedException
     * @todo Implement this
     *   com.inov8.verifly.server.service.veriflymodule.VeriflyManager method
     */
    public VeriflyBaseWrapper changePIN(VeriflyBaseWrapper wrapper) {
        VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();

        System.out.println("...........start...............VeriflyFacase inchangepin..........................");

        try {
            localBaseWrapper = veriflyManager.changePIN(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;

    }

    /**
     * deactivatePIN
     *
     * @param wrapper VeriflyBaseWrapper
     * @return VeriflyBaseWrapper
     * @throws FrameworkCheckedException
     * @todo Implement this
     *   com.inov8.verifly.server.service.veriflymodule.VeriflyManager method
     */
    public VeriflyBaseWrapper deactivatePIN(VeriflyBaseWrapper wrapper) {
        VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();
        try {
            localBaseWrapper = veriflyManager.deactivatePIN(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }

    /**
     * generatePIN
     *
     * @param wrapper VeriflyBaseWrapper
     * @return VeriflyBaseWrapper
     * @throws FrameworkCheckedException
     * @todo Implement this
     *   com.inov8.verifly.server.service.veriflymodule.VeriflyManager method
     */
    public VeriflyBaseWrapper generatePIN(VeriflyBaseWrapper wrapper) {
        VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();
        try {
            localBaseWrapper = veriflyManager.generatePIN(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }

    /**
     * getLog
     *
     * @param wrapper VeriflyBaseWrapper
     * @return VeriflyBaseWrapper
     * @throws FrameworkCheckedException
     * @todo Implement this
     *   com.inov8.verifly.server.service.veriflymodule.VeriflyManager method
     */
    public VeriflyBaseWrapper getLog(VeriflyBaseWrapper wrapper) {
        VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();
        try {
            localBaseWrapper = veriflyManager.getLog(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }

    public LogWrapper getLog(LogWrapper logWrapper) {
        LogWrapper localBaseWrapper = new LogWrapperImpl();
        try {
            localBaseWrapper = logManager.viewTransactionLog(logWrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }

    /**
     * resetPIN
     *
     * @param wrapper VeriflyBaseWrapper
     * @return VeriflyBaseWrapper
     * @throws FrameworkCheckedException
     * @todo Implement this
     *   com.inov8.verifly.server.service.veriflymodule.VeriflyManager method
     */
    public VeriflyBaseWrapper resetPIN(VeriflyBaseWrapper wrapper) {
        VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();
        try {
            localBaseWrapper = veriflyManager.resetPIN(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }

    /**
     * verifyPIN
     *
     * @param wrapper VeriflyBaseWrapper
     * @return VeriflyBaseWrapper
     * @throws FrameworkCheckedException
     * @todo Implement this
     *   com.inov8.verifly.server.service.veriflymodule.VeriflyManager method
     */
    public VeriflyBaseWrapper verifyPIN(VeriflyBaseWrapper wrapper) {
        VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();
        try {
            localBaseWrapper = veriflyManager.verifyPIN(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }
    
    

    public VeriflyBaseWrapper modifyAccountInfo(VeriflyBaseWrapper wrapper) {
        VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();
        try {
            localBaseWrapper = veriflyManager.modifyAccountInfo(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }
    
    public VeriflyBaseWrapper modifyAccountInfoForBBAgents(VeriflyBaseWrapper wrapper) {
        VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();
        try {
            localBaseWrapper = veriflyManager.modifyAccountInfoForBBAgents(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }

    public void setVeriflyManager(VeriflyManager veriflyManager) {
        this.veriflyManager = veriflyManager;
    }

    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    public VeriflyBaseWrapper deleteAccount(VeriflyBaseWrapper wrapper) {
        VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();
        try {
            localBaseWrapper = veriflyManager.deleteAccount(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }

    public LogWrapper insertLogRequiresNewTransaction(LogWrapper wrapper) {
        LogWrapper localBaseWrapper = new LogWrapperImpl();
        try {
            localBaseWrapper = logManager.insertLogRequiresNewTransaction(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }

    public LogWrapper updateLogRequiresNewTransaction(LogWrapper logWrapper) throws
            FrameworkCheckedException {
        LogWrapper localBaseWrapper = new LogWrapperImpl();
        try {
            localBaseWrapper = logManager.insertLogRequiresNewTransaction(logWrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }

    public VeriflyBaseWrapper markAsDeleted(VeriflyBaseWrapper wrapper){
        VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();
        try {
            localBaseWrapper = veriflyManager.markAsDeleted(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }    
    
    public LogWrapper viewTransactionLog(LogWrapper logWrapper) {
        LogWrapper localBaseWrapper = new LogWrapperImpl();
        try {
            localBaseWrapper = logManager.viewTransactionLog(logWrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
    }

    private VeriflyBaseWrapper fillErrorObject(Exception ex,
                                               VeriflyBaseWrapper
                                               veriflyBaseWrapper) {
        veriflyBaseWrapper.setExceptionStatus(true);
        veriflyBaseWrapper.putObject("ExceptionsList", this.getErrorsObject(ex));
        return veriflyBaseWrapper;
    }

    private LogWrapper fillErrorObject(Exception ex,
                                       LogWrapper logWrapper) {
        logWrapper.setExceptionStatus(true);
        logWrapper.putObject("ExceptionsList", this.getErrorsObject(ex));
        return logWrapper;
    }

    private Errors getErrorsObject(Exception exception) {
        Error error = new Error();
        Errors errors = new Errors();
        this.logger.error(exception.toString());
        if (exception instanceof ClassCastException) {
            error.setErrorCode(5000);
            error.setErrorMessage(this.messageSource.getMessage("5000", null, null));
            errors.addErrorObject(error);
        } else if (exception instanceof NullPointerException) {
            error.setErrorCode(5000);
            error.setErrorMessage(this.messageSource.getMessage("5000", null, null));
            errors.addErrorObject(error);
        } else if (exception instanceof ArrayIndexOutOfBoundsException) {
            error.setErrorCode(5000);
            error.setErrorMessage(this.messageSource.getMessage("5000", null, null));
            errors.addErrorObject(error);
        }

        else if (exception instanceof ParseException) {
            error.setErrorCode(5000);
            error.setErrorMessage(this.messageSource.getMessage("5000", null, null));
            errors.addErrorObject(error);
        } else if (exception instanceof NumberFormatException) {
            error.setErrorCode(5000);
            error.setErrorMessage(this.messageSource.getMessage("5000", null, null));
            errors.addErrorObject(error);
        } else if (exception instanceof NumberFormatException) {
            error.setErrorCode(5000);
            error.setErrorMessage(this.messageSource.getMessage("5000", null, null));
            errors.addErrorObject(error);
        } else if (exception instanceof DataAccessException) {
            if (exception instanceof DataRetrievalFailureException) {
                error.setErrorCode(5001);
                error.setErrorMessage(this.messageSource.getMessage("5001", null, null));
            } else if (exception instanceof EmptyResultDataAccessException) {
                error.setErrorCode(5002);
                error.setErrorMessage(this.messageSource.getMessage("5002", null, null));
            } else if (exception instanceof
                       IncorrectResultSizeDataAccessException) {
                error.setErrorCode(5003);
                error.setErrorMessage(this.messageSource.getMessage("5003", null, null));
            } else if (exception instanceof OptimisticLockingFailureException) {
                error.setErrorCode(5004);
                error.setErrorMessage(this.messageSource.getMessage("5004", null, null));
            } else if (exception instanceof TypeMismatchDataAccessException) {
                error.setErrorCode(5005);
                error.setErrorMessage(this.messageSource.getMessage("5005", null, null));
            }
            errors.addErrorObject(error);
        } else {
            error.setErrorCode(5006);
            error.setErrorMessage(this.messageSource.getMessage("5006", null, null));
            errors.addErrorObject(error);
        }

        return errors;
    }

	public VeriflyBaseWrapper verifyCredentials(VeriflyBaseWrapper wrapper)
			throws Exception {
		VeriflyBaseWrapper localBaseWrapper = new VeriflyBaseWrapperImpl();
        try {
            localBaseWrapper = veriflyManager.verifyCredentials(wrapper);
        } catch (Exception ex) {
            localBaseWrapper = this.fillErrorObject(ex, localBaseWrapper);
        }
        return localBaseWrapper;
		
	}

	@Override
	public String getAccountNumber(Long customerId) throws Exception {
	
		return veriflyManager.getAccountNumber(customerId);
	}

	@Override
	public AccountInfoModel getAccountInfoModel(Long customerId, Long paymentModeId) throws Exception {

		return veriflyManager.getAccountInfoModel(customerId, paymentModeId);
	}

	@Override
	public AccountInfoModel getAccountInfoModel(Long customerId, String accountNick) throws Exception {
		// TODO Auto-generated method stub
		return veriflyManager.getAccountInfoModel(customerId, accountNick);
	}

}
