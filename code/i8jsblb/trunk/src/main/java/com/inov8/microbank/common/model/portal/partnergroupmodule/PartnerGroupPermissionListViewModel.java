package com.inov8.microbank.common.model.portal.partnergroupmodule;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

public class PartnerGroupPermissionListViewModel extends PartnerGroupListViewModel  
{
	private List<UserPermissionWrapper> userPermissionList = LazyList.decorate(new ArrayList<UserPermissionWrapper>(), new Factory() {
        public Object create() {
            return new UserPermissionWrapper();
        }
    });
	
	public PartnerGroupPermissionListViewModel()
	{
		this.setActive(true);
	}
	public List<UserPermissionWrapper> getUserPermissionList()
	{
		return this.userPermissionList;
	}
	
	public void setUserPermissionList(List<UserPermissionWrapper> userPermissionList)
	{
		this.userPermissionList = userPermissionList;
	}
}
