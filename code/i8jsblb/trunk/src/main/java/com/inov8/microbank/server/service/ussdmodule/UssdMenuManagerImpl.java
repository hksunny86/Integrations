package com.inov8.microbank.server.service.ussdmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.CustomInputTypeModel;
import com.inov8.microbank.common.model.UssdMenuMappingModel;
import com.inov8.microbank.common.model.UssdMenuModel;
import com.inov8.microbank.server.dao.ussdmodule.UssdMenuDAO;

public class UssdMenuManagerImpl implements UssdMenuManager{
	private UssdMenuDAO ussdMenuDAO;
	private GenericDao genericDAO;
	@SuppressWarnings("rawtypes")
	public BaseWrapper findMenu(BaseWrapper param)throws FrameworkCheckedException{
		BaseWrapper retVal=null;
		UssdMenuModel paramValue= (UssdMenuModel)param.getBasePersistableModel();
		StringBuilder queryBuiler=new StringBuilder();
		queryBuiler.append("select menu.relationCustomInputTypeIdCustomInputTypeModel, menu.ussdMenuId, menu.screenCode,menu.menuString,menu.commandCode,menu.inputRequired,menu.executionRequired,menu.validInputs from UssdMenuModel menu left outer join ");
		queryBuiler.append("menu.relationCustomInputTypeIdCustomInputTypeModel as customInputTypeIdCustomInputTypeModel ");
		queryBuiler.append("where menu.screenCode =? ");
		
		List result = genericDAO.findByHQL(queryBuiler.toString(),
				new Object[] { paramValue.getScreenCode()});
		if(result!=null && result.size() >0){
			Object[] objArray= (Object[])result.get(0);
			UssdMenuModel model=new UssdMenuModel();
			model.setCustomInputTypeIdCustomInputTypeModel((CustomInputTypeModel)objArray[0]);
			model.setUssdMenuId((Long)objArray[1]);
			model.setScreenCode((Long)objArray[2]);
			model.setMenuString((String)objArray[3]);
			model.setCommandCode( ((String)objArray[4]));
			model.setInputRequired( ((String)objArray[5]));
			model.setExecutionRequired( ((String)objArray[6]));
			model.setValidInputs( ((String)objArray[7]));
			param.setBasePersistableModel(model); 
			retVal=param;
		}
		return retVal;
	}
	@SuppressWarnings("rawtypes")
	public BaseWrapper findNextMenu(BaseWrapper param)throws FrameworkCheckedException{
		BaseWrapper retVal=null;
		UssdMenuMappingModel paramValue= (UssdMenuMappingModel)param.getBasePersistableModel();
//		String paymentModeHQL="select menu from UssdMenuModel menu left outer join menu.relationCustomInputTypeIdCustomInputTypeModel as customInputTypeIdCustomInputTypeModel " +
//		"where menu.screenCode = (select menuMapping.screenCodeOutput from UssdMenuMappingModel menuMapping " +
//		"where menuMapping.screenCodeInput=? and menuMapping.options =? and menuMapping.relationAppUserTypeIdAppUserTypeModel.appUserTypeId =? ) ";
		StringBuilder queryBuiler=new StringBuilder();
		queryBuiler.append("select menu.relationCustomInputTypeIdCustomInputTypeModel, menu.ussdMenuId, menu.screenCode,menu.menuString,menu.commandCode,menu.inputRequired,menu.executionRequired,menu.validInputs from UssdMenuModel menu left outer join ");
		queryBuiler.append("menu.relationCustomInputTypeIdCustomInputTypeModel as customInputTypeIdCustomInputTypeModel ");
		queryBuiler.append("where menu.screenCode = ");
		queryBuiler.append("(select menuMapping.screenCodeOutput from UssdMenuMappingModel menuMapping ");
		queryBuiler.append("where menuMapping.screenCodeInput=? and menuMapping.options =? ");
		queryBuiler.append(" and menuMapping.relationAppUserTypeIdAppUserTypeModel.appUserTypeId =? ) ");
		
		
		List result = genericDAO.findByHQL(queryBuiler.toString(),
		new Object[] { paramValue.getScreenCodeInput(),paramValue.getOptions(),paramValue.getAppUserTypeId() });
		if(result!=null && result.size() >0){
			Object[] menu=(Object[])result.get(0);
			UssdMenuModel menuModel=new UssdMenuModel();
			menuModel.setCustomInputTypeIdCustomInputTypeModel((CustomInputTypeModel)menu[0]);
			menuModel.setUssdMenuId((Long)menu[1]);
			menuModel.setScreenCode((Long)menu[2]);
			menuModel.setMenuString((String)menu[3]);
			menuModel.setCommandCode( ((String)menu[4]));
			menuModel.setInputRequired( ((String)menu[5]));
			menuModel.setExecutionRequired( ((String)menu[6]));
			menuModel.setValidInputs( ((String)menu[7]));
			param.setBasePersistableModel(menuModel); 
			retVal=param;
		}
		return retVal;
	}
	
	
	public BaseWrapper findPreviousMenu(Long screenOutputCode,Long appUserTypeID)throws FrameworkCheckedException{
		BaseWrapper retVal=null;
		UssdMenuMappingModel paramValue= null;
//		String paymentModeHQL="select menu from UssdMenuModel menu left outer join menu.relationCustomInputTypeIdCustomInputTypeModel as customInputTypeIdCustomInputTypeModel " +
//		"where menu.screenCode = (select menuMapping.screenCodeOutput from UssdMenuMappingModel menuMapping " +
//		"where menuMapping.screenCodeInput=? and menuMapping.options =? and menuMapping.relationAppUserTypeIdAppUserTypeModel.appUserTypeId =? ) ";
		StringBuilder queryBuiler=new StringBuilder();
		queryBuiler.append("select menu.relationCustomInputTypeIdCustomInputTypeModel, menu.ussdMenuId, menu.screenCode,menu.menuString,menu.commandCode,menu.inputRequired,menu.executionRequired,menu.validInputs from UssdMenuModel menu left outer join ");
		queryBuiler.append("menu.relationCustomInputTypeIdCustomInputTypeModel as customInputTypeIdCustomInputTypeModel ");
		queryBuiler.append("where menu.screenCode = ");
		queryBuiler.append("(select distinct menuMapping.screenCodeInput from UssdMenuMappingModel menuMapping ");
		queryBuiler.append("where menuMapping.screenCodeOutput=? and menuMapping.options !=? ");
		queryBuiler.append(" and menuMapping.relationAppUserTypeIdAppUserTypeModel.appUserTypeId =? ) ");
		
		
		List result = genericDAO.findByHQL(queryBuiler.toString(),
		new Object[] { screenOutputCode,"0",appUserTypeID });
		if(result!=null && result.size() >0){
			Object[] menu=(Object[])result.get(0);
			UssdMenuModel menuModel=new UssdMenuModel();
			menuModel.setCustomInputTypeIdCustomInputTypeModel((CustomInputTypeModel)menu[0]);
			menuModel.setUssdMenuId((Long)menu[1]);
			menuModel.setScreenCode((Long)menu[2]);
			menuModel.setMenuString((String)menu[3]);
			menuModel.setCommandCode( ((String)menu[4]));
			menuModel.setInputRequired( ((String)menu[5]));
			menuModel.setExecutionRequired( ((String)menu[6]));
			menuModel.setValidInputs( ((String)menu[7]));
			BaseWrapper param = new BaseWrapperImpl();
			param.setBasePersistableModel(menuModel); 
			retVal=param;
		}
		return retVal;
	}
	
	public int findPreviousMenuCount(Long screenOutputCode,Long appUserTypeID)throws FrameworkCheckedException{
		int retVal=0;
		UssdMenuMappingModel paramValue= null;
		StringBuilder queryBuiler=new StringBuilder();
		
		queryBuiler.append("select distinct menuMapping.screenCodeInput from UssdMenuMappingModel menuMapping ");
		queryBuiler.append("where menuMapping.screenCodeOutput=? and menuMapping.options !='0' ");
		queryBuiler.append(" and menuMapping.relationAppUserTypeIdAppUserTypeModel.appUserTypeId =?  ");
		
		
		List result = genericDAO.findByHQL(queryBuiler.toString(),
		new Object[] { screenOutputCode, appUserTypeID });
		if(result!=null && result.size() >0){
			retVal=result.size();
		}
		return retVal;
	}
	
	
	public UssdMenuDAO getUssdMenuDAO() {
		return ussdMenuDAO;
	}
	public void setUssdMenuDAO(UssdMenuDAO ussdMenuDAO) {
		this.ussdMenuDAO = ussdMenuDAO;
	}
	public GenericDao getGenericDAO() {
		return genericDAO;
	}
	public void setGenericDAO(GenericDao genericDAO) {
		this.genericDAO = genericDAO;
	}

}
