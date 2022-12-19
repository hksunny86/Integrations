
package com.inov8.microbank.server.service.commandmodule.minicommandmodule;


import java.util.HashMap;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.billreminder.UserServiceCustFldViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.MiniXMLUtil;
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

public class BillRetrievalCommand extends MiniBaseCommand
{
	protected String mobileNo;
	protected HashMap parameters = null ;
	protected String firstParam ;
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		parameters = (HashMap)((HashMap)baseWrapper.getObject(CommandFieldConstants.KEY_PARAM_HASHMAP)).clone() ;
		
		firstParam = (String)parameters.get( CommandFieldConstants.KEY_PARAM + CommandFieldConstants.PARAM_COUNTER_FIRST_VAL ) ; 
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		mobileNo = this.removeCountryCode(mobileNo);		
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
		
		if( firstParam == null || firstParam.equalsIgnoreCase("") )
		{
			logger.error("Exception thrown by BillRetrievalCommand.doValidate() Line:74  Service Nick is Empty");
			response = this.getMessageSource().getMessage("MINI.NoSuchServiceNick", new Object[]{firstParam}, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
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
			BaseWrapper baseWrapper = new BaseWrapperImpl() ;
			
			/**
			 * Load the user_service info against this nick
			 */
			
			UserServiceCustFldViewModel userServiceCustFldViewModel = new UserServiceCustFldViewModel();
			userServiceCustFldViewModel.setAppUserId( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
			userServiceCustFldViewModel.setUserServiceName( firstParam ) ;
			userServiceCustFldViewModel.setActive(true);
			searchBaseWrapper.setBasePersistableModel(userServiceCustFldViewModel) ;
//			searchBaseWrapper = this.getCommonCommandManager().getUserService(searchBaseWrapper);
			List<UserServiceCustFldViewModel> userServiceCustFldViewModelList = searchBaseWrapper.getCustomList().getResultsetList() ; 
			
			
			if( userServiceCustFldViewModelList != null && userServiceCustFldViewModelList.size() > 0 )
			{
				userServiceCustFldViewModel = (UserServiceCustFldViewModel)userServiceCustFldViewModelList.get(0) ;
				firstParam = userServiceCustFldViewModel.getUserServiceName() ;
				
				// Load Product model to check whether the product is active or not
				ProductModel productModel = new ProductModel() ;
				productModel.setProductId( userServiceCustFldViewModel.getProductId() ) ;
				baseWrapper.setBasePersistableModel( productModel ) ;
				productModel = (ProductModel)this.getCommonCommandManager().loadProduct( baseWrapper ).getBasePersistableModel() ;
				
				if( !productModel.getActive() )
				{
					logger.error("Exception thrown by BillRetrievalCommand.execute(). Product not Active");
					response = this.getMessageSource().getMessage("MINI.ServiceInActive", new Object[]{ productModel.getName() } , null) ;
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);				
				}
				
				SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel() ;				
				
				smartMoneyAccountModel.setCustomerId( ThreadLocalAppUser.getAppUserModel().getCustomerId() );
//				smartMoneyAccountModel.setDefPayAccount( true ) ;
				//smartMoneyAccountModel.setMiniEnable(true) ;
				//smartMoneyAccountModel.setActive(true) ;
				searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				
				searchBaseWrapper = this.getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper) ;
				
				
				if( searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0 ) 
				{
					smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0) ;
					
					if( !smartMoneyAccountModel.getActive() )
					{
						logger.error("Exception thrown by BillRetrievalCommand.execute(). Smart Money Account not Active");
						response = this.getMessageSource().getMessage("MINI.InActiveDefAcc", new Object[]{ smartMoneyAccountModel.getName() } , null) ;
						throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);						
					}
//					if( !smartMoneyAccountModel.getMiniEnable() )
//					{
//						logger.error("Exception thrown by LoginCommand.execute(). Smart Money Account not Mini account");
//						response = this.getMessageSource().getMessage("MINI.DefAccNotLinkedWithMini", new Object[]{ smartMoneyAccountModel.getName() } , null) ;
//						throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);						
//					}
					
					baseWrapper.putObject( CommandFieldConstants.KEY_PROD_ID, userServiceCustFldViewModel.getProductId() ) ;
					baseWrapper.putObject( CommandFieldConstants.KEY_ACC_ID, smartMoneyAccountModel.getSmartMoneyAccountId() ) ;
					baseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
					
					saveMiniCommandLog( Long.parseLong(CommandFieldConstants.CMD_GET_SUPPLIER_INFO));
					
					// populate all the custom parameters
					baseWrapper.putObject( userServiceCustFldViewModel.getKeyName1(), userServiceCustFldViewModel.getCustomFieldValue1() ) ;
					baseWrapper.putObject( userServiceCustFldViewModel.getKeyName2(), userServiceCustFldViewModel.getCustomFieldValue2() ) ;
					baseWrapper.putObject( userServiceCustFldViewModel.getKeyName3(), userServiceCustFldViewModel.getCustomFieldValue3() ) ;
					baseWrapper.putObject( userServiceCustFldViewModel.getKeyName4(), userServiceCustFldViewModel.getCustomFieldValue4() ) ;
					baseWrapper.putObject( userServiceCustFldViewModel.getKeyName5(), userServiceCustFldViewModel.getCustomFieldValue5() ) ;
					baseWrapper.putObject( userServiceCustFldViewModel.getKeyName6(), userServiceCustFldViewModel.getCustomFieldValue6() ) ;
					baseWrapper.putObject( userServiceCustFldViewModel.getKeyName7(), userServiceCustFldViewModel.getCustomFieldValue7() ) ;
					baseWrapper.putObject( userServiceCustFldViewModel.getKeyName8(), userServiceCustFldViewModel.getCustomFieldValue8() ) ;
					baseWrapper.putObject( userServiceCustFldViewModel.getKeyName9(), userServiceCustFldViewModel.getCustomFieldValue9() ) ;
					baseWrapper.putObject( userServiceCustFldViewModel.getKeyName10(), userServiceCustFldViewModel.getCustomFieldValue10() ) ;
					
					response = this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_GET_SUPPLIER_INFO);
					
					
				}
				else //If there is no smart money account linked with Mini 
				{
					logger.error("Exception thrown by BillRetrievalCommand.execute(). No Smart Money Account");
					response = this.getMessageSource().getMessage("MINI.NoMiniAccLinked", null, null) ;
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
				}
			}
			else // In-Valid Service Nick
			{
				logger.error("Exception thrown by BillRetrievalCommand.execute(). In-valid Service nick");
				response = this.getMessageSource().getMessage("MINI.ServiceNickNotFound", new Object[]{firstParam}, null) ;
				throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
			}
		}
		catch (FrameworkCheckedException ex)
		{
			logger.error("Exception thrown by BillRetrievalCommand.execute(). " + ex.getMessage());
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR,
					ErrorLevel.MEDIUM, ex);
		}				
	}


	@Override
	public String response()
	{		
		try
		{
			String values[] = new String[8];
			
			values[0] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.CONSUMER_REF_NO_NODEREF ) ;
			values[1] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.BILL_DATE_NODEREF ) ;
			values[2] = firstParam ;
			values[3] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.LATE_BILL_DATE_NODEREF ) ;
			values[4] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.BILL_AMOUNT_NODEREF ) ;			
			values[5] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.BILL_PAID_STATUS_NODEREF ) ;
			
			if( values[5].equalsIgnoreCase("0") )
				values[5] = "Unpaid" ;
			else
				values[5] = "Paid" ;
			
			
			response = this.getMessageSource().getMessage("MINI.BillRetrievalSMS", values, null) ;
		}
		catch (XPathExpressionException e1)
		{
			e1.printStackTrace();
		}
		
		return response;
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}

}

