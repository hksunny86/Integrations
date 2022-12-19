/**
 * 
 */
package com.inov8.microbank.server.messaging.listener;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.Level2AccountModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.util.ApplicantTypeConstants;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.RegistrationStateConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.vo.account.BulkCustomerAccountVo;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountFacade;
import com.inov8.microbank.server.service.xml.XmlMarshaller;
import com.inov8.ola.util.CustomerAccountTypeConstants;

/**
 * @author NaseerUl
 *
 */
public class CustomerAccountMessageListener implements MessageListener
{
	private static final Logger LOGGER = Logger.getLogger( CustomerAccountMessageListener.class );

	private MfsAccountFacade mfsAccountFacade;
	private XmlMarshaller<BulkCustomerAccountVo> xmlMarshaller;

	@Override
	public void onMessage(Message message)
	{
		try
		{
			if(message instanceof TextMessage)
			{
				TextMessage textMessage = (TextMessage) message;
				String xml = textMessage.getText();
				if(!GenericValidator.isBlankOrNull(xml))
				{
					BulkCustomerAccountVo vo = xmlMarshaller.unmarshal(xml);
					BaseWrapper baseWrapper = new BaseWrapperImpl();

					AppUserModel createdByAppUserModel = new AppUserModel(vo.getCreatedBy());
					ThreadLocalAppUser.setAppUserModel(createdByAppUserModel);

					if(CustomerAccountTypeConstants.LEVEL_0 == vo.getCustomerAccountTypeId()
							|| CustomerAccountTypeConstants.LEVEL_1 == vo.getCustomerAccountTypeId())
					{
						MfsAccountModel mfsAccountModel = vo.toMfsAccountModel();
						mfsAccountModel.setRegistrationStateId(RegistrationStateConstants.BULK_REQUEST_RECEIVED);
						mfsAccountModel.setCountry("1");//Pakistan
						mfsAccountModel.setCreatedOn(new Date());
						
						
						baseWrapper.putObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY, mfsAccountModel);
						baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, mfsAccountModel.getActionId());
						baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, mfsAccountModel.getUsecaseId());
						mfsAccountFacade.createMfsAccount(baseWrapper);
					}
					else
					{
						Level2AccountModel level2AccountModel = vo.toLevel2AccountModel();
						ApplicantDetailModel applicant1DetailModel = new ApplicantDetailModel();
						applicant1DetailModel.setIdNumber(vo.getCnic());
						applicant1DetailModel.setIdType(1L); //CNIC type
						applicant1DetailModel.setIdExpiryDate(vo.getCnicExpiryDate());
						applicant1DetailModel.setName(vo.getName());
						applicant1DetailModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
						level2AccountModel.setApplicant1DetailModel(applicant1DetailModel);
						level2AccountModel.setCustomerAccountName(vo.getName());
						level2AccountModel.setRegistrationStateId(RegistrationStateConstants.BULK_REQUEST_RECEIVED);
						level2AccountModel.setCreatedOn(new Date());
						level2AccountModel.setInitialAppFormNo(vo.getInitialAppFormNo());
						level2AccountModel.setIsBulkRequest(true);
						baseWrapper.putObject(Level2AccountModel.LEVEL2_ACCOUNT_MODEL_KEY, level2AccountModel);
						baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, level2AccountModel.getActionId());
						baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, level2AccountModel.getUsecaseId());
						mfsAccountFacade.createLevel2Account(baseWrapper);
					}
					
				}
			}
		}
		catch (FrameworkCheckedException e)
		{
			LOGGER.error(e.getMessage(),e);
		}
		catch (JMSException e)
		{
			LOGGER.error(e.getMessage(),e);
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage(),e);
		}
	}

	public void setMfsAccountFacade(MfsAccountFacade mfsAccountFacade)
	{
		this.mfsAccountFacade = mfsAccountFacade;
	}

	public void setXmlMarshaller(XmlMarshaller<BulkCustomerAccountVo> xmlMarshaller)
	{
		this.xmlMarshaller = xmlMarshaller;
	}
}
