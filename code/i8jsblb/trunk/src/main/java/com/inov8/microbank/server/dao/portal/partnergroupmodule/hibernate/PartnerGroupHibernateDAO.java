package com.inov8.microbank.server.dao.portal.partnergroupmodule.hibernate;

import java.util.List;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.server.dao.portal.partnergroupmodule.PartnerGroupDAO;


public class PartnerGroupHibernateDAO extends BaseHibernateDAO<PartnerGroupModel, Long, PartnerGroupDAO>
implements PartnerGroupDAO  {

	public List<PartnerGroupModel> findPartnerGroupsByPartnerId(Long partnerId)
	{
		String hql = "From PartnerGroupModel pgm where pgm.relationPartnerIdPartnerModel.partnerId = ?";
		List<PartnerGroupModel> partnerGroupModelList = getHibernateTemplate().find(hql, partnerId);
		return partnerGroupModelList;
	}

	public List getPartnerGroups( Long partnerId ,Boolean  admin)
	  {
	    StringBuilder hql = new StringBuilder();
	    
	    hql.append("SELECT pg.partnerGroupId , pg.name FROM PartnerGroupModel pg ");
	    hql.append("WHERE pg.relationPartnerIdPartnerModel.partnerId = ? ");
	    hql.append("and pg.active = true " );
	    if(!admin)
	    {
	    	hql.append(" and pg.partnerGroupId not in");
	    	hql.append(" ( select pgd.relationPartnerGroupIdPartnerGroupModel.partnerGroupId from PartnerGroupDetailModel pgd");
	    	hql.append(" where pgd.relationPartnerGroupIdPartnerGroupModel.partnerGroupId = pg.partnerGroupId");
	    	hql.append(" and pgd.relationUserPermissionIdUserPermissionModel.userPermissionId = 21 ");//ADM_GP
	    	hql.append(" and pgd.readAllowed = true )");  
	    }
	    hql.append(" Order by pg.name ");

	    return this.getHibernateTemplate().find(hql.toString(), new Object[]{partnerId}) ;
	  }

	@Override
	public boolean isNameUnique(PartnerGroupModel partnerGroupModel) {
		String hql = "from PartnerGroupModel where name = ?";
		List<PartnerGroupModel> partnerGroupModelList = getHibernateTemplate().find(hql, new Object[]{partnerGroupModel.getName()});
		
		if(null != partnerGroupModel.getPartnerGroupId()){
			if(partnerGroupModelList.size()>1 ){
				return false;
			}else if(partnerGroupModelList.size() == 1 && partnerGroupModelList.get(0).getPartnerGroupId().longValue() != partnerGroupModel.getPartnerGroupId().longValue()){
				return false;
			}else{
				return true;
			}
		}else{
			if(partnerGroupModelList.size()==0){
				return true;
			}else{
				return false;
			}
		}
	}

}
