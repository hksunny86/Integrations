package com.inov8.microbank.server.service.operatormodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.OperatorBankInfoModel;
import com.inov8.microbank.common.model.OperatorModel;
import com.inov8.microbank.common.model.OperatorUserModel;
import com.inov8.microbank.server.dao.operatormodule.OperatorBankInfoDAO;
import com.inov8.microbank.server.dao.operatormodule.OperatorDAO;
import com.inov8.microbank.server.dao.operatormodule.OperatorUserDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public class OperatorManagerImpl implements OperatorManager
{

	private OperatorDAO operatorDAO;
	private AppUserDAO appUserDAO;
	private OperatorUserDAO operatorUserDAO;
	private OperatorBankInfoDAO operatorBankInfoDAO;

	/**
	 * updateOperator
	 *
	 * @param baseWrapper BaseWrapper
	 * @return BaseWrapper
	 * @throws FrameworkCheckedException
	 *
	 */
	public BaseWrapper updateOperator(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		OperatorModel newOperatorModel = new OperatorModel();
		OperatorModel operatorModel = (OperatorModel) baseWrapper.getBasePersistableModel();

		newOperatorModel.setOperatorId(operatorModel.getOperatorId());

		int recordCount = operatorDAO.countByExample(newOperatorModel);
		//***Check if record already exists
		if (recordCount != 0 || operatorModel.getOperatorId() != null)
		{
			operatorModel = this.operatorDAO.saveOrUpdate((OperatorModel) baseWrapper
					.getBasePersistableModel());
			baseWrapper.setBasePersistableModel(operatorModel);
			return baseWrapper;
		}
		else
		{
			//set baseWrapper to null if record exists
			baseWrapper.setBasePersistableModel(null);
			return baseWrapper;
		}
	}

	/**
	 * loadOperator
	 *
	 * @param searchBaseWrapper BaseWrapper
	 * @return BaseWrapper
	 * @throws FrameworkCheckedException
	 *
	 */
	public BaseWrapper loadOperator(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		this.operatorDAO.toString();
		baseWrapper.getBasePersistableModel().getPrimaryKey();
		OperatorModel operatorModel = this.operatorDAO.findByPrimaryKey(baseWrapper.getBasePersistableModel()
				.getPrimaryKey());
		baseWrapper.setBasePersistableModel(operatorModel);
		return baseWrapper;
	}

	public BaseWrapper getOperatorBankInfo(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		List operatorBankInfoModelList = this.operatorBankInfoDAO.findByExample(
				(OperatorBankInfoModel) baseWrapper.getBasePersistableModel()).getResultsetList();
		if (operatorBankInfoModelList.size() > 0)
			baseWrapper.setBasePersistableModel((OperatorBankInfoModel) operatorBankInfoModelList.get(0));
		else
			baseWrapper.setBasePersistableModel(null);

		return baseWrapper;
	}

	public BaseWrapper loadOperatorByAppUser(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		OperatorUserModel operatorUserModel;
		AppUserModel appUser = (AppUserModel) baseWrapper.getBasePersistableModel();
		appUser = this.appUserDAO.findByPrimaryKey(appUser.getAppUserId());

		if (appUser.getOperatorUserId() != null && appUser.getOperatorUserId() > 0)
		{
			operatorUserModel = this.operatorUserDAO.findByPrimaryKey(appUser.getOperatorUserId());
			if (operatorUserModel != null && operatorUserModel.getOperatorUserId() > 0)
			{
				baseWrapper.setBasePersistableModel(this.operatorDAO.findByPrimaryKey(operatorUserModel
						.getOperatorId()));
				return baseWrapper;
			}
		}
		baseWrapper.setBasePersistableModel(new OperatorModel());

		return baseWrapper;
	}

	public void setOperatorDAO(OperatorDAO operatorDAO)
	{
		this.operatorDAO = operatorDAO;
	}

	public void setAppUserDAO(AppUserDAO appUserDAO)
	{
		this.appUserDAO = appUserDAO;
	}

	public void setOperatorUserDAO(OperatorUserDAO operatorUserDAO)
	{
		this.operatorUserDAO = operatorUserDAO;
	}

	public void setOperatorBankInfoDAO(OperatorBankInfoDAO operatorBankInfoDAO)
	{
		this.operatorBankInfoDAO = operatorBankInfoDAO;
	}
}
