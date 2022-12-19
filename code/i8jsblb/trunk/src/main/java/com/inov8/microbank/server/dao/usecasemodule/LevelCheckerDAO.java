package com.inov8.microbank.server.dao.usecasemodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;

public interface LevelCheckerDAO extends BaseDAO<LevelCheckerModel, Long> {

	List<LevelCheckerModel> getAsociatedCheckers(List<Long> usecaseLevels) throws FrameworkCheckedException;

}
