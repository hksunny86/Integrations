package com.inov8.integration.middleware.constants;

/**
 * Created by Zeeshan Ahmad on 6/2/2016.
 */
public interface EnumHelper {

    EnumHelper[] result();

    public String getName();

    public String getPath();

    public boolean isList();

    public Class getListEnum();

    public Class getListClass();
}
