
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

public class ListPeerCommand extends MiniBaseCommand
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
			AppUserPeerModel appUserPeerModel = new AppUserPeerModel() ;
			LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>() ;
			sortingOrderMap.put("name", SortingOrder.ASC);
		    			
			appUserPeerModel.setAppUserId( ThreadLocalAppUser.getAppUserModel().getAppUserId() );
			appUserPeerModel.setActive(true) ;			
			
			searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
			searchBaseWrapper.setBasePersistableModel(appUserPeerModel);
			
			searchBaseWrapper = this.getCommonCommandManager().getAppUserPeers( searchBaseWrapper )  ;
			List<AppUserPeerModel> appUserPeerModelList = searchBaseWrapper.getCustomList().getResultsetList() ;
			
			if( appUserPeerModelList != null && appUserPeerModelList.size() != 0 )
			{
				int itemsInServiceList = Integer.parseInt( this.getMessageSource().getMessage("MINI.ItemsInServiceList", null,null) ) ;
				Iterator<AppUserPeerModel> appUserPeerListIterator = appUserPeerModelList.iterator() ;
				
				while( appUserPeerListIterator.hasNext() && itemsInServiceList > 0 )
				{
					itemsInServiceList-- ;
					appUserPeerModel = appUserPeerListIterator.next() ;
					
					response += appUserPeerModel.getName() + " " ;
					response += "\n" ;					
				}				
			}
			else // If no Peer exists 
			{
				logger.error("Exception thrown by ListAccountCommand.execute() line:79");
				response = this.getMessageSource().getMessage("MINI.NoPeerFound", null, null) ;
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

