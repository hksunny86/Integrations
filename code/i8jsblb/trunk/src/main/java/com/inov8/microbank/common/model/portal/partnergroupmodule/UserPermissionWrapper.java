package com.inov8.microbank.common.model.portal.partnergroupmodule;

/**
 * 
 * @author haroon.anwar
 *
 */
public class UserPermissionWrapper 
{
	private long permissionId;
	private Integer userPermissionSectionId;
	private String userPermissionSectionName;
	private Integer sequenceNo;
	private String permissionName;
	private String permissionShortName;
	private boolean readAvailable;
	private boolean updateAvailable;
	private boolean deleteAvailable;
	private boolean createAvailable;
	private boolean readAllowed;
	private boolean updateAllowed;
	private boolean deleteAllowed;
	private boolean createAllowed;
	
	public UserPermissionWrapper()
	{
		
	}
	
	public UserPermissionWrapper(long permissionId, String permissionName)
	{
		this.permissionId = permissionId;
		this.permissionName = permissionName; 
	}
	public long getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}

	public Integer getUserPermissionSectionId()
    {
        return userPermissionSectionId;
    }

    public void setUserPermissionSectionId( Integer userPermissionSectionId )
    {
        this.userPermissionSectionId = userPermissionSectionId;
    }

    public String getUserPermissionSectionName()
    {
        return userPermissionSectionName;
    }

    public void setUserPermissionSectionName( String userPermissionSectionName )
    {
        this.userPermissionSectionName = userPermissionSectionName;
    }

    public Integer getSequenceNo()
    {
        return sequenceNo;
    }

    public void setSequenceNo( Integer sequenceNo )
    {
        this.sequenceNo = sequenceNo;
    }

    public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public boolean isReadAvailable() {
		return readAvailable;
	}

	public void setReadAvailable(boolean readAvailable) {
		this.readAvailable = readAvailable;
	}

	public boolean isUpdateAvailable() {
		return updateAvailable;
	}

	public void setUpdateAvailable(boolean updateAvailable) {
		this.updateAvailable = updateAvailable;
	}

	public boolean isDeleteAvailable() {
		return deleteAvailable;
	}

	public void setDeleteAvailable(boolean deleteAvailable) {
		this.deleteAvailable = deleteAvailable;
	}

	public boolean isCreateAvailable() {
		return createAvailable;
	}

	public void setCreateAvailable(boolean createAvailable) {
		this.createAvailable = createAvailable;
	}

	public boolean isReadAllowed() {
		return readAllowed;
	}

	public void setReadAllowed(boolean readAllowed) {
		this.readAllowed = readAllowed;
	}

	public boolean isUpdateAllowed() {
		return updateAllowed;
	}

	public void setUpdateAllowed(boolean updateAllowed) {
		this.updateAllowed = updateAllowed;
	}

	public boolean isDeleteAllowed() {
		return deleteAllowed;
	}

	public void setDeleteAllowed(boolean deleteAllowed) {
		this.deleteAllowed = deleteAllowed;
	}

	public boolean isCreateAllowed() {
		return createAllowed;
	}

	public void setCreateAllowed(boolean createAllowed) {
		this.createAllowed = createAllowed;
	}

	public String getPermissionShortName() {
		return permissionShortName;
	}

	public void setPermissionShortName(String permissionShortName) {
		this.permissionShortName = permissionShortName;
	}		
}
