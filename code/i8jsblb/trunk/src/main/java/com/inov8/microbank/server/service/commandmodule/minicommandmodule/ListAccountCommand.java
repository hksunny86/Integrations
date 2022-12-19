
package com.inov8.microbank.server.service.commandmodule.minicommandmodule;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;

/**
 * 
 * @author Jawwad Farooq
 * July, 2007
 * 
 */

public class ListAccountCommand extends MiniBaseCommand
{
	protected String mobileNo;
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		mobileNo = this.removeCountryCode(mobileNo);		
		response = "" ;
	}

	
	@Override
	public void doValidate() throws CommandException
	{
		ValidationErrors validationErrors = new ValidationErrors();		
		validationErrors = ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");

		if (validationErrors.hasValidationErrors())
		{
			logger.error(validationErrors.getErrors());
			throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR,
					ErrorLevel.HIGH, new Throwable());
		}
	}

	@Override
	public void execute() throws CommandException
	{
		logger.info( "*********************************** AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey()  ) ;
		logger.info( "*********************************** UserDeviceId : " + ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getPrimaryKey()  ) ;
		
		
		try
		{
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl() ;			
			SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel() ;
			LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>() ;
			sortingOrderMap.put("miniSerialNo", SortingOrder.ASC);
		    			
			smartMoneyAccountModel.setCustomerId( ThreadLocalAppUser.getAppUserModel().getCustomerId() );
			smartMoneyAccountModel.setActive( true );
//			smartMoneyAccountModel.setMiniEnable( true );
			searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
			searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			
			searchBaseWrapper = this.getCommonCommandManager().loadSmartMoneyAccount( searchBaseWrapper )  ;
			List<SmartMoneyAccountModel> smartMoneyAccountModelList = searchBaseWrapper.getCustomList().getResultsetList() ;
			
			if( smartMoneyAccountModelList != null && smartMoneyAccountModelList.size() != 0 )
			{
				int itemsInServiceList = Integer.parseInt( this.getMessageSource().getMessage("MINI.ItemsInServiceList", null,null) ) ;
				Iterator<SmartMoneyAccountModel> smartMoneyAccountListIterator = smartMoneyAccountModelList.iterator() ;
				
				while( smartMoneyAccountListIterator.hasNext()  && itemsInServiceList > 0 )
				{
					itemsInServiceList-- ;
					smartMoneyAccountModel = smartMoneyAccountListIterator.next() ;
					
					response += /*(smartMoneyAccountModel.getminiSerialNo()==null ? "" : smartMoneyAccountModel.getminiSerialNo()) + " " +*/ smartMoneyAccountModel.getName() ;
					
					if( smartMoneyAccountModel.getDefAccount() )
						response += " " + CommandFieldConstants.DEFAULT_PAY_ACC_SYMBOL ;
//					if( smartMoneyAccountModel.getDefReceiveAccount() )
//						response += " " + CommandFieldConstants.DEFAULT_REC_ACC_SYMBOL ;
					
					response += "\n" ;					
				}				
				
			}
			else // If no active SMA linked with mini 
			{
				logger.error("Exception thrown by ListAccountCommand.execute() line:79");
				response = this.getMessageSource().getMessage("MINI.NoMiniAccLinked", null, null) ;
				throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);				
			}				
		}
		catch (FrameworkCheckedException ex)
		{
			logger.error("Exception thrown by LoginCommand.checkLogin()");
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR,
					ErrorLevel.MEDIUM, ex);
		}		
		
	}


	@Override
	public String response()
	{		
		return response;
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}

}

