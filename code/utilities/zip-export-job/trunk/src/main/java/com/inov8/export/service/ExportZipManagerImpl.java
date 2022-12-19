package com.inov8.export.service;

import com.inov8.export.dao.OracleJdbcDao;
import com.inov8.framework.common.model.ExportInfoModel;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.nio.file.Paths;

/**
 * Created by NaseerUl on 9/6/2016.
 */
public class ExportZipManagerImpl implements ExportZipManager
{
    private static final Logger LOGGER = Logger.getLogger(ExportZipManagerImpl.class );

    private static long day = 0;

    private OracleJdbcDao oracleJdbcDao;

    public ExportZipManagerImpl()
    {
        super();
    }

    @Override
    public String searchAndExportZip(ExportInfoModel exportInfoModel) throws Exception
    {
    	SqlRowSet rowSet = oracleJdbcDao.queryForRowSet(exportInfoModel);
    	return null;
    	
    	/*LOGGER.info("Querying for all Records.");
    	Long startTime= System.currentTimeMillis();
    	LOGGER.info("Start Time for Query "+ new Date());
    	SqlRowSet rowSet = oracleJdbcDao.queryForRowSet(exportInfoModel);
    	
    	LOGGER.info("End Time for Query"+ new Date());
    	Long endTime= System.currentTimeMillis();			 
		LOGGER.info("Total Query time consumed in seconds : "+ (endTime-startTime)/1000);
       
		SqlRowSet totalsRowSet = null;
        if(exportInfoModel.getPropsFormatsMap() != null && exportInfoModel.getPropsFormatsMap().values().contains(TableConstants.CURRENCY))
        {
            LOGGER.info("Querying for totals row.");
            startTime= System.currentTimeMillis();
          
            
            totalsRowSet = oracleJdbcDao.queryForTotalsRowSet(exportInfoModel);
            
         
            
        

        }
        startTime= System.currentTimeMillis();
        LOGGER.info("Start Time for Export "+ new Date());
        
        String filePath = ExportViewEnum.valueOf(exportInfoModel.getView().toUpperCase()).export(exportInfoModel,rowSet,totalsRowSet);
        
        LOGGER.info("End Time for Export "+ new Date());
        endTime= System.currentTimeMillis();			 
		LOGGER.info("Total File Writing time consumed in seconds : "+ (endTime-startTime)/1000);
        
		startTime= System.currentTimeMillis();
		String zipFileName = makeZip(filePath);
		
		endTime= System.currentTimeMillis();			 
		LOGGER.info("Total Zip making time consumed in seconds : "+ (endTime-startTime)/1000);
	        
        return zipFileName;*/
            
    }

    private String makeZip(String filePath)
    {
        String zipFileName = null;
        try{
            LOGGER.info(" Compressing File " + filePath);
            String gzipFilePath = filePath+".gz";
            String zipFilePath;
            if(filePath.endsWith("xlsx"))
            {
                zipFilePath = filePath.substring(0,filePath.length()-5)+".zip";
            }
            else
            {
                zipFilePath = filePath.substring(0,filePath.length()-4)+".zip";
            }
            String zipCommand = "gzip " + filePath;
            String mvCommand = "mv " + gzipFilePath + " " + zipFilePath;
            LOGGER.info(" Executing command> " + zipCommand);
            Process zipProcess = Runtime.getRuntime().exec(zipCommand);
            zipProcess.waitFor();
            LOGGER.info(" Executing command> " + mvCommand);
            Process renameProcess = Runtime.getRuntime().exec(mvCommand);//rename .gz to .zip
            renameProcess.waitFor();
            LOGGER.info("Zip Done.");
            zipFileName = Paths.get(zipFilePath).getFileName().toString();
            /*long dayLocal = Long.parseLong(filePath.substring(filePath.length() - 22, filePath.length() - 14));
            LOGGER.info(filePath.substring(filePath.length()-22,filePath.length()-14));
            if(day<dayLocal){
                if(day==0){
                    day = dayLocal - 1;
                }
                String deleteCommand = "rm -rf "+filePath+"*"+day+"*.csv.gz";
                LOGGER.info("SU Started");
                p = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(p.getOutputStream());
                LOGGER.info("Deleting Files "+deleteCommand);
                os.writeBytes(deleteCommand + "\n");
                os.writeBytes("exit\n");
                os.flush();
                p.waitFor();
                LOGGER.info("Deletion Done.");
                day = dayLocal;
            }*/
        }catch(Exception e){
        	LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return zipFileName;
    }

    public void setOracleJdbcDao(OracleJdbcDao oracleJdbcDao)
    {
        this.oracleJdbcDao = oracleJdbcDao;
    }
}
