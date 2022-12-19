
package com.inov8.microbank.server.service.commandmodule.minicommandmodule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
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

public class ListServiceCommand extends MiniBaseCommand
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
				
		/*
		
		try
		{
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl() ;			
			UserServiceModel userServiceModel = new UserServiceModel() ;			
			LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>() ;
			sortingOrderMap.put("name", SortingOrder.ASC);
		    			
			userServiceModel.setAppUserId( ThreadLocalAppUser.getAppUserModel().getAppUserId() );
			userServiceModel.setActive(true) ;			
			
			searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
			searchBaseWrapper.setBasePersistableModel(userServiceModel);
			
			searchBaseWrapper = this.getCommonCommandManager().getUserRegServices( searchBaseWrapper )  ;
			List<UserServiceModel> userServiceModelList = searchBaseWrapper.getCustomList().getResultsetList() ;
			
			if( userServiceModelList != null && userServiceModelList.size() != 0 )
			{
				int itemsInServiceList = Integer.parseInt( this.getMessageSource().getMessage("MINI.ItemsInServiceList", null,null) ) ;
				
				Iterator<UserServiceModel> userServiceModelListIterator = userServiceModelList.iterator() ;
				
				while( userServiceModelListIterator.hasNext() && itemsInServiceList > 0 )
				{
					itemsInServiceList-- ;
					userServiceModel = userServiceModelListIterator.next() ;
					
					response += userServiceModel.getName() + " " ;
					response += "\n" ;					
				}				
			}
			else // If no Services exists 
			{
				logger.error("Exception thrown by ListAccountCommand.execute() line:79");
				response = this.getMessageSource().getMessage("MINI.NoServiceFound", null, null) ;
				throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);				
			}				
		}
		catch (FrameworkCheckedException ex)
		{
			logger.error("Exception thrown by LoginCommand.checkLogin()");
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR,
					ErrorLevel.MEDIUM, ex);
		}			
		
			*/
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

