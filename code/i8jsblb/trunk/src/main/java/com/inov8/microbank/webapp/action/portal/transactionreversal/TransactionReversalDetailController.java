package com.inov8.microbank.webapp.action.portal.transactionreversal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.inov8.microbank.common.util.TransactionReversalConstants;
import com.inov8.microbank.common.vo.transactionreversal.TransactionReversalVo;
import com.inov8.microbank.server.facade.portal.transactionreversal.TransactionReversalFacade;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 8, 2012 4:55:35 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class TransactionReversalDetailController extends AbstractController
{

    private TransactionReversalFacade transactionReversalFacade;

    @Override
    protected ModelAndView handleRequestInternal( HttpServletRequest req, HttpServletResponse resp ) throws Exception
    {
        String transactionCode = ServletRequestUtils.getStringParameter( req, TransactionReversalConstants.TRANSACTION_CODE );
        Long transactionCodeId = ServletRequestUtils.getLongParameter( req, TransactionReversalConstants.TRANSACTION_CODE_ID );

        TransactionReversalVo txReversalVo = transactionReversalFacade.findTransactionReversalDetail( transactionCodeId );

        if( txReversalVo != null )
        {
            txReversalVo.setTransactionCodeId( transactionCodeId );
        }

        req.setAttribute( TransactionReversalConstants.TRANSACTION_CODE, transactionCode );
        return new ModelAndView( "p_transactionreversaldetail", TransactionReversalConstants.COMMAND_NAME, txReversalVo );
    }

    public void setTransactionReversalFacade( TransactionReversalFacade transactionReversalFacade )
    {
        this.transactionReversalFacade = transactionReversalFacade;
    }

}
