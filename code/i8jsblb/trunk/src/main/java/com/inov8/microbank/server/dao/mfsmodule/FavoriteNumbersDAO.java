package com.inov8.microbank.server.dao.mfsmodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.FavoriteNumbersModel;

public interface FavoriteNumbersDAO extends BaseDAO <FavoriteNumbersModel, Long>
{
	public void deteleFavoriteNumbers(Long appUserId);
	public Long getLatestSequenceNumber(Long appUserId);
}
