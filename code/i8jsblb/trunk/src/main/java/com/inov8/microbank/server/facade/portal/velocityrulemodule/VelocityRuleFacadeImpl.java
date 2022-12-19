package com.inov8.microbank.server.facade.portal.velocityrulemodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.VelocityRuleModel;
import com.inov8.microbank.server.service.portal.velocitymodule.VelocityRuleManager;

public class VelocityRuleFacadeImpl implements VelocityRuleFacade {

	private VelocityRuleManager velocityRuleManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	@Override
	public BaseWrapper saveOrUpdate(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try 
		{		
			return this.velocityRuleManager.saveOrUpdate(baseWrapper);
		} 
		catch (Exception e)
		{
			throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION );
		}
	}

	@Override
	public VelocityRuleModel loadVelocityRuleModel(Long velocityRuleId)
			throws FrameworkCheckedException {
		try 
		{		
			return this.velocityRuleManager.loadVelocityRuleModel(velocityRuleId);
		} 
		catch (Exception e)
		{
			throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
		}
	}

	@Override
	public SearchBaseWrapper searchVelocityRule(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException {
		try 
		{		
			return this.velocityRuleManager.searchVelocityRule(searchBaseWrapper);
		} 
		catch (Exception e)
		{
			throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
		}
	}
	@Override
	public List<VelocityRuleModel> findByCriteria(VelocityRuleModel velocityRuleModel) throws FrameworkCheckedException {
		try 
		{		
			return this.velocityRuleManager.findByCriteria(velocityRuleModel);
		} 
		catch (Exception e)
		{
			throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
		}
	}

	public void setVelocityRuleManager(VelocityRuleManager velocityRuleManager) {
		this.velocityRuleManager = velocityRuleManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	
}
