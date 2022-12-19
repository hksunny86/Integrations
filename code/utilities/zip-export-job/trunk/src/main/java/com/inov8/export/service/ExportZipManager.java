package com.inov8.export.service;

import com.inov8.framework.common.model.ExportInfoModel;

/**
 * Created by NaseerUl on 9/6/2016.
 */
public interface ExportZipManager
{
    String searchAndExportZip(ExportInfoModel exportInfoModel) throws Exception;
}
