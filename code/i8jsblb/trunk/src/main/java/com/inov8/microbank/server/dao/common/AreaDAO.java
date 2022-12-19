package com.inov8.microbank.server.dao.common;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.RetailerContactModel;



/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public interface AreaDAO
    extends BaseDAO<AreaModel, Long>
{
	public List<AreaModel> findAreaByRegionalHierarchyId(long regionalHierarchyId) throws FrameworkCheckedException;
	public List<AreaModel> findAreaNamesByRegionId(long regionId) throws FrameworkCheckedException;
	public List<AreaModel> findAgentAreaByRegionId(long regionId) throws FrameworkCheckedException;
	public List<RetailerContactModel> findAreaReference(long areaId) throws FrameworkCheckedException;

}
