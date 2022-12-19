package com.inov8.export.controller;

import com.inov8.export.service.ExportZipManager;
import com.inov8.framework.common.model.ExportInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NaseerUl on 9/6/2016.
 */
@Controller
public class ExportZipController
{
    private static final Log LOGGER = LogFactory.getLog(ExportZipController.class);

    private ExportZipManager exportZipManager;

    private MessageSource messageSource;

    public ExportZipController()
    {

    }

    @RequestMapping(value = "/p_exportzip", method = RequestMethod.GET)
    public @ResponseBody void exportZip(HttpServletRequest request, HttpSession session, ExportInfoModel exportInfoModel)
    {
        String downloadZipUrl = null;

        //Set File Name
        String fileNameLengthStr = messageSource.getMessage("export.filename.length",null,null);
        int fileNameLength = Integer.valueOf(fileNameLengthStr);
        String normalizedExportFileName = exportInfoModel.getExportFileName().replaceAll("/","_").replaceAll(" ","_");
        if(normalizedExportFileName.length() > fileNameLength)
        {
            normalizedExportFileName = normalizedExportFileName.substring(0,fileNameLength);
        }
        exportInfoModel.setExportFileName(normalizedExportFileName);

        //Set properties format
        String propsFormats = exportInfoModel.getPropsFormats();
        if(!GenericValidator.isBlankOrNull(propsFormats))
        {
            String[] columnsAndFormatsPairs = propsFormats.split(",");
            Map<String,String> propsFormatsMap = new HashMap<>(columnsAndFormatsPairs.length);
            for(String columnAndFormat:columnsAndFormatsPairs)
            {
                String[] columnAndFormatPair = columnAndFormat.split(":");
                propsFormatsMap.put(columnAndFormatPair[0],columnAndFormatPair[1]);
            }
            exportInfoModel.setPropsFormatsMap(propsFormatsMap);
        }

        //Set export dir
        String exportDir = messageSource.getMessage("export.directory",null,null);
        String exportDirPath = request.getServletContext().getRealPath(exportDir);
        exportInfoModel.setExportDirPath(exportDirPath);

        String referrer = request.getParameter("referrer");
        if(null==referrer)
        {
            referrer = request.getHeader("Referer");
        }
        if(referrer != null)
        {
            String refererServletPath = referrer.substring(referrer.lastIndexOf('/'));
            if(refererServletPath.contains("?"))
            {
                refererServletPath = refererServletPath.substring(0,refererServletPath.indexOf('?'));//remove query string
            }
           // DetachedCriteria detachedCriteria = (DetachedCriteria) session.getAttribute(refererServletPath);
            
            ExportInfoModel sessionExportModel = (ExportInfoModel) session.getAttribute(refererServletPath);
            
            exportInfoModel.setDetachedCriteria(sessionExportModel.getDetachedCriteria());
            exportInfoModel.setPackageCall(sessionExportModel.getPackageCall());
            exportInfoModel.setFromDate(sessionExportModel.getFromDate());
            exportInfoModel.setToDate(sessionExportModel.getToDate());
            exportInfoModel.setDateType(sessionExportModel.getDateType());

            String exportFileName = null;
			try {
				exportFileName = exportZipManager.searchAndExportZip(exportInfoModel);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				e.printStackTrace();
			}
            downloadZipUrl = request.getContextPath()+"/"+exportDir+"/"+exportFileName;
        }
        //return downloadZipUrl;
    }

    @Autowired
    public void setExportZipManager(ExportZipManager exportZipManager)
    {
        this.exportZipManager = exportZipManager;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }
}
