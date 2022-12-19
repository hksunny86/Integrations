/**
 * 
 */
package com.inov8.microbank.server.service.portal.dayendsettlement;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.vo.dayendsettlement.DayEndSettlementVo;
import com.inov8.microbank.webapp.action.portal.mfsaccountmodule.AccountOpeningReportController;

/**
 * @author NaseerUl
 *
 */
public class DayEndSettlementManagerImpl implements DayEndSettlementManager
{
	private static final Log     LOG = LogFactory.getLog( AccountOpeningReportController.class );

	private String dirPath;

	private final SimpleDateFormat SDF = new SimpleDateFormat("ddMMyy_HHmm");

	@Override
	public List<DayEndSettlementVo> searchDayEndSettlementFiles()
	{
		List<DayEndSettlementVo> unarchivedList = new ArrayList<>();
		List<DayEndSettlementVo> archivedList = new ArrayList<>();
        Path path = Paths.get(dirPath);

        java.util.Date createdOn = null;
        
        try(DirectoryStream<Path> filteredDirStream = Files.newDirectoryStream(path))
        {
            for(Path filterPath : filteredDirStream)
            {
            	String fileName = filterPath.getFileName().toString();
            	String fileType = null;
            	
            	if(fileName.contains("DETAIL")) {
            		fileType = "Detail OF Summary File";
            		
            	} else if(fileName.contains("HEADER")) {
            		fileType = "Header OF Summary File";
            	}
            	
            	if(StringUtil.isNullOrEmpty(fileType) || !fileName.contains(".CSV")) {
            		continue;
            	}

        		GregorianCalendar calendar = new GregorianCalendar();
        		        		
        		if(fileName.contains("_")) {

        			String fileDateTime = fileName.split("_")[2];          
            		calendar.setTimeInMillis(Long.parseLong(fileDateTime)); 
            		archivedList.add( new DayEndSettlementVo(calendar.getTime(), fileName, fileType) );
            		
        		} else {

        			unarchivedList.add( new DayEndSettlementVo(null, fileName, fileType + " *") );
        		}      		
            }
        	
            int i=0;

            Collections.sort(archivedList);
            
        	if(createdOn == null && archivedList.size() > 0) {
            	
        		createdOn = (((DayEndSettlementVo) archivedList.get(0)).getCreatedOn());
        	}  
            
            for(DayEndSettlementVo dayEndSettlementVo : unarchivedList) {

            	
            	dayEndSettlementVo.setCreatedOn(createdOn);
                archivedList.add(i, dayEndSettlementVo);
                i++;
            }

        }
        catch (IOException e)
        {
        	LOG.error(e.getMessage(), e);
		}
        return archivedList;
	}

	public void setDirPath(String dirPath)
	{
		this.dirPath = dirPath;
	}

}
