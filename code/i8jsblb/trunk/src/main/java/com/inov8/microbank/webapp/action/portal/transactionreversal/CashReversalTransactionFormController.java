package com.inov8.microbank.webapp.action.portal.transactionreversal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.inov8.microbank.common.util.TransactionReversalConstants;
import com.inov8.microbank.common.vo.transactionreversal.TransactionReversalVo;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.server.facade.portal.transactionreversal.TransactionReversalFacade;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 3, 2012 4:54:08 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class CashReversalTransactionFormController extends SimpleFormController
{
    private TransactionReversalFacade transactionReversalFacade;
	private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;

    public CashReversalTransactionFormController()
    {
        setCommandName( TransactionReversalConstants.COMMAND_NAME );
        setCommandClass( TransactionReversalVo.class );
    }

    @Override
    protected TransactionReversalVo formBackingObject( HttpServletRequest request ) throws Exception
    {
        TransactionReversalVo txReversalVo = new TransactionReversalVo();
        txReversalVo.setTransactionCode( ServletRequestUtils.getStringParameter( request,  TransactionReversalConstants.TRANSACTION_CODE ) );
        txReversalVo.setTransactionId( ServletRequestUtils.getLongParameter( request, "transactionId" ) );
        txReversalVo.setTransactionCodeId( ServletRequestUtils.getLongParameter( request, TransactionReversalConstants.TRANSACTION_CODE_ID ) );
        txReversalVo.setBtnName( ServletRequestUtils.getStringParameter( request, "btnName" ) );
        return txReversalVo;
    }

    @Override
    protected ModelAndView onSubmit( Object command ) throws Exception
    {
        TransactionReversalVo txReversalVo = (TransactionReversalVo) command;
        boolean isReversed = false;
        try{
        	logger.info("Going to call updateTransactionReversed for TransactionCodeId:"+txReversalVo.getTransactionCodeId());
        	
        	SwitchWrapper switchWrapper = transactionReversalFacade.updateTransactionReversed( txReversalVo.getTransactionCodeId(), txReversalVo.getComments() );
        	isReversed = true;
        	String trxCode = switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getTransactionCode();
        	
        	logger.info("After updateTransactionReversed for trx ID:"+trxCode);
        	logger.info("Going to call SAF Repo - for trx ID:"+txReversalVo.getTransactionCode());
        	
        	creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(trxCode);
        	
        	logger.info("After call to SAF Repo - for trx ID:"+txReversalVo.getTransactionCode());
			
        }catch(Exception ex){
        	logger.error("Exception at updateTransactionReversed...", ex);
        }

        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put( TransactionReversalConstants.IS_REVERSED, isReversed );
        modelMap.put( TransactionReversalConstants.COMMAND_NAME, txReversalVo );

        return new ModelAndView( getSuccessView(), modelMap );
    }

    public void setTransactionReversalFacade( TransactionReversalFacade transactionReversalFacade )
    {
        this.transactionReversalFacade = transactionReversalFacade;
    }

	public void setCreditAccountQueingPreProcessor(
			CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
		this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
	}

}
