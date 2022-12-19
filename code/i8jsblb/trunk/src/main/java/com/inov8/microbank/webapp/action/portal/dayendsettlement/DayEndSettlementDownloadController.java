/**
 * 
 */
package com.inov8.microbank.webapp.action.portal.dayendsettlement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inov8.microbank.common.util.MessageUtil;

/**
 * @author NaseerUl
 *
 */
@Controller
public class DayEndSettlementDownloadController
{
	private String dirPath;

	public DayEndSettlementDownloadController()
	{
		super();
		this.dirPath = MessageUtil.getMessage("fundTranFilepath");
	}

	@RequestMapping(value="/dayEndSettlementDownload")
	public void download(@RequestParam String fileName, HttpServletResponse response)
	{
		String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",fileName);
        response.setContentType("text/csv");
        response.setHeader(headerKey, headerValue);

        String filePath = dirPath + "/" + fileName;
        try
        {
			IOUtils.copy(new FileInputStream( filePath ), response.getOutputStream());
		}
        catch (FileNotFoundException e)
        {
			e.printStackTrace();
		}
        catch (IOException e)
        {
			e.printStackTrace();
		}
	}

	public void setDirPath(String dirPath)
	{
		this.dirPath = dirPath;
	}

}
