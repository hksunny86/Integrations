/**
 * 
 */
package com.inov8.microbank.server.messaging.listener;

import java.io.Serializable;
import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.util.AccountCreationStatusEnum;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.disbursement.vo.BulkDisbursementsXmlVo;
import com.inov8.microbank.server.facade.BulkDisbursementsFacade;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountFacade;
import com.inov8.microbank.server.service.xml.XmlMarshaller;

/**
 * @author NaseerUl
 *
 */
public class WalkInAccountMessageListener implements MessageListener
{
	private static final Logger LOGGER = Logger.getLogger( WalkInAccountMessageListener.class );
	private MfsAccountFacade mfsAccountFacade;
	private BulkDisbursementsFacade bulkDisbursementsFacade;
	private XmlMarshaller<BulkDisbursementsXmlVo> xmlMarshaller;

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
					BulkDisbursementsXmlVo vo = xmlMarshaller.unmarshal(xml);
					BulkDisbursementsModel model = vo.toBulkDisbursementsModel();

					AppUserModel walkInAppUserModel = null;
					BaseWrapper baseWrapper = null;

					try {
						Boolean walkinCustomerExists = mfsAccountFacade.updateWalkinCustomerIfExists(vo.getCnic(), vo.getMobileNo(), null);
						if(walkinCustomerExists)
						{
							walkInAppUserModel = new AppUserModel();
							walkInAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);
							walkInAppUserModel.setNic(vo.getCnic());

							SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
							searchBaseWrapper.setBasePersistableModel(walkInAppUserModel);

							searchBaseWrapper = mfsAccountFacade.loadAppUserByMobileNumberAndType(searchBaseWrapper);
							walkInAppUserModel = (AppUserModel) searchBaseWrapper.getBasePersistableModel();
							model.setAccountCreationStatus(AccountCreationStatusEnum.SUCCESSFUL.toString());
						}
						else
						{
							AppUserModel createdByAppUserModel = new AppUserModel(vo.getCreatedBy());
							baseWrapper = mfsAccountFacade.createWalkinCustomerAccount(vo.getCnic(), vo.getMobileNo(), createdByAppUserModel);
							walkInAppUserModel = (AppUserModel) baseWrapper.getObject("appUserModel");
							if(walkInAppUserModel != null)
							{
								model.setAccountCreationStatus(AccountCreationStatusEnum.SUCCESSFUL.toString());
							}
							else
							{
								Serializable serializable = baseWrapper.getObject("ErrMessage");
								if(null != serializable)
								{
									String failureReason = (String) serializable;
									model.setFailureReason(failureReason);
									model.setAccountCreationStatus(AccountCreationStatusEnum.FAILED.toString());
								}
							}
						}
					}
					catch (Exception e)
					{
						model.setAccountCreationStatus(AccountCreationStatusEnum.FAILED.toString());
						model.setFailureReason(e.getMessage());
						LOGGER.error(e.getMessage(),e);
					}
					model.setAppUserIdAppUserModel(walkInAppUserModel);
					model.setUpdatedOn(new Date());
					bulkDisbursementsFacade.update(model);
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

	public void setMfsAccountFacade(MfsAccountFacade mfsAccountFacade) {
		this.mfsAccountFacade = mfsAccountFacade;
	}

	public void setBulkDisbursementsFacade(BulkDisbursementsFacade bulkDisbursementsFacade) {
		this.bulkDisbursementsFacade = bulkDisbursementsFacade;
	}

	public void setXmlMarshaller(XmlMarshaller<BulkDisbursementsXmlVo> xmlMarshaller) {
		this.xmlMarshaller = xmlMarshaller;
	}
}
