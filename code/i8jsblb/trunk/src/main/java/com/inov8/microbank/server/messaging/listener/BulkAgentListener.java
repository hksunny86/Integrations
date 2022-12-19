package com.inov8.microbank.server.messaging.listener;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.microbank.common.model.agenthierarchy.BulkAgentUploadReportModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.verifly.common.des.EncryptionHandler;
import com.inov8.verifly.common.model.AccountInfoModel;

public class BulkAgentListener implements MessageListener 
{
	
	protected static Log logger	= LogFactory.getLog(BulkAgentListener.class);
	
	private AgentHierarchyManager agentHierarchyManager;
	private EncryptionHandler encryptionHandler;
	
	
	@SuppressWarnings("unused")
	@Override
	public void onMessage(Message message) 
	{
		RetailerContactListViewFormModel agentFormModel = null;
		if (message instanceof ObjectMessage)
		{
			try
			{
				Object obj = ((ObjectMessage) message).getObject();
				if (null != obj && obj instanceof RetailerContactListViewFormModel)
				{
					agentFormModel = (RetailerContactListViewFormModel)obj;
					this.agentHierarchyManager.saveAgentForBulkProcess(agentFormModel);
					saveOrUpdateBulkAgentReport(agentFormModel, "Success");
				}
			}
			catch (JMSException ex)
			{
				if (agentFormModel != null)
				{
					try
					{
						saveOrUpdateBulkAgentReport(agentFormModel, "Failure");
					}
					catch(Exception e)
					{
						logger.error(e);
					}
				}
				logger.error(ex);
			}
			catch (Exception ex)
			{
				if (agentFormModel != null)
				{
					try
					{
						saveOrUpdateBulkAgentReport(agentFormModel, "Failure");
					}
					catch(Exception e)
					{
						logger.error(e);
					}
				}
				logger.error(ex);
			}
		}

	}

	/**
	 * @param agentFormModel
	 * @param status TODO
	 * @throws FrameworkCheckedException
	 */
	private void saveOrUpdateBulkAgentReport(RetailerContactListViewFormModel agentFormModel, String status) throws FrameworkCheckedException
	{
		BulkAgentUploadReportModel bulkAgentUploadReportModel = this.toBulkAgentUploadReportModel(agentFormModel);
		bulkAgentUploadReportModel.setRecordStatus(status);
		this.agentHierarchyManager.saveOrUpdateBulkAgentReport(bulkAgentUploadReportModel);
	}

	private BulkAgentUploadReportModel toBulkAgentUploadReportModel(RetailerContactListViewFormModel agentFormModel)
	{
		BulkAgentUploadReportModel model = new BulkAgentUploadReportModel();
		model.setBulkAgentReportId(agentFormModel.getBulkAgentReportId());
		model.setVersionNo(agentFormModel.getBulkAgentReportVersionNo());
		model.setAgentNetwork(agentFormModel.getDistributorName());
		model.setAgentNetworkId(Long.valueOf(agentFormModel.getDistributorId()));
		model.setRegion(agentFormModel.getRegionName());
		model.setRegionId(Long.valueOf(agentFormModel.getRegionId()));
		model.setFranchiseBranch(agentFormModel.getRetailerName());
		model.setFranchiseBranchId(Long.valueOf(agentFormModel.getRetailerId()));
		model.setPartnerGroup(agentFormModel.getPartnerGroupName());
		model.setProductCatalog(agentFormModel.getProductCatalogName());
		model.setAgentLevel(agentFormModel.getDistributorLevelName());
		model.setParentAgent(agentFormModel.getParentRetailerContactName());
		model.setAreaLevel(agentFormModel.getAreaLevelName());
		model.setAreaName(agentFormModel.getAreaName());
		model.setDescription(agentFormModel.getDescription());
		model.setComments(agentFormModel.getComments());
		model.setAgentName(agentFormModel.getName());
		model.setActive(agentFormModel.getActive());
		model.setFirstName(agentFormModel.getFirstName());
		model.setLastName(agentFormModel.getLastName());
		model.setUserName(agentFormModel.getUsername());
		model.setPassword(EncoderUtils.encodeToSha(agentFormModel.getPassword()));
		model.setZongMsisdn(Long.valueOf(agentFormModel.getMobileNo()));
		model.setAccountOpenDate(agentFormModel.getAccountOpeningDate());
		model.setAgentNtnNo(Long.valueOf(agentFormModel.getNtnNo()));
		model.setCnicNo(Long.valueOf(agentFormModel.getCnicNo()));
		model.setCnicExpiryDate(agentFormModel.getCnicExpiryDate());
		model.setContactNo(Long.valueOf(agentFormModel.getContactNo()));
		model.setLandlineNo(Long.valueOf(agentFormModel.getLandLineNo()));
		model.setMobileNo(Long.valueOf(agentFormModel.getZongMsisdn()));
		model.setEmail(agentFormModel.getEmail());
		model.setBusinessName(agentFormModel.getBusinessName());
		model.setNatureOfBusiness(agentFormModel.getNatureOfBusinessName());
		model.setShopNo(agentFormModel.getShopNo());
		model.setMarketPlaza(agentFormModel.getMarketPlaza());
		model.setDistrictTehsilTown(agentFormModel.getDistrictTehsilTownName());
		model.setCityVillage(agentFormModel.getCityVillageName());
		model.setPostOffice(agentFormModel.getPostOfficeName());
		model.setBusinessNtnNo(Long.valueOf(agentFormModel.getNtnNumber()));
		model.setNearestLandmark(agentFormModel.getNearestLandmark());
		AccountInfoModel accountInfoModel = new AccountInfoModel();
    	accountInfoModel.setAccountNo(agentFormModel.getAccountNo());
    	try{
    		encryptionHandler.encrypt(accountInfoModel);	
    	}
        catch(Exception e)
        {
        	logger.info(e);
        }
		model.setAccountNo(accountInfoModel.getAccountNo());
		model.setAccountTitle(agentFormModel.getAccountNick());
		model.setUpdatedOn(new Date());
		model.setUpdatedBy(agentFormModel.getBulkAgentUploadReportCreatedBy());
		model.setCreatedBy(agentFormModel.getBulkAgentUploadReportCreatedBy());
		model.setCreatedOn(agentFormModel.getBulkAgentUploadReportCreatedOn());
		model.setCreatedByName(agentFormModel.getCreatedByName());
		return model;
	}
	
	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}

	public void setEncryptionHandler(EncryptionHandler encryptionHandler) {
		this.encryptionHandler = encryptionHandler;
	}

	
	
}
