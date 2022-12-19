package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

import static com.inov8.microbank.common.util.PortalDateUtils.CSV_DATE_FORMAT;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.vo.account.BulkCustomerAccountVo;
import com.inov8.ola.util.CustomerAccountTypeConstants;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 15, 2014 7:00:00 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class BulkCustomerAccountsCsvFileParser
{
	public List<BulkCustomerAccountVo> parseBulkCustomerAccountsCsvFile(String filePath, BulkCustomerAccountVo bulkCustomerAccountVo) throws FileNotFoundException, IOException, ParseException
	{
		List<BulkCustomerAccountVo> parsedBulkCustomerAccountsVoList = null;
        CSVReader reader = new CSVReader(new FileReader(filePath)); // point the file from file system
        Date currentDate = PortalDateUtils.parseStringAsDate(PortalDateUtils.currentFormattedDate(CSV_DATE_FORMAT),CSV_DATE_FORMAT);
        HeaderColumnNameTranslateMappingStrategy<BulkCustomerAccountVo> mappingStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
        mappingStrategy.setType(BulkCustomerAccountVo.class);

        Map<String, String> headerColumnNameToPropertyMap = new HashMap<>(4);
        headerColumnNameToPropertyMap.put("Name", "name");
        headerColumnNameToPropertyMap.put("MobileNo", "mobileNo");
        headerColumnNameToPropertyMap.put("CNIC", "cnic");
        headerColumnNameToPropertyMap.put("CNIC Expiry", "cnicExpiryDateStr");
        headerColumnNameToPropertyMap.put("Initial Application Form No", "initialAppFormNo");
        mappingStrategy.setColumnMapping(headerColumnNameToPropertyMap);
        CsvToBean<BulkCustomerAccountVo> csvToBean = new CsvToBean<>();
        parsedBulkCustomerAccountsVoList = csvToBean.parse(mappingStrategy, reader);

        reader.close();
        for (BulkCustomerAccountVo model : parsedBulkCustomerAccountsVoList)
        {
        	try
        	{
				model.setValidRecord(true);
				          
				if(GenericValidator.isBlankOrNull(model.getName()))
				{
					model.setValidRecord(false);
				}

				if(!CommonUtils.isValidCnic(model.getCnic()))
				{
				    model.setValidRecord(false);
				}

				if(GenericValidator.isBlankOrNull(model.getMobileNo()) )
				{
				    model.setValidRecord(false);
				}
				else if(model.getMobileNo().length() == 10)
				{
				    String mob = "0" + model.getMobileNo();
				    model.setMobileNo(mob);
				}
				if( !CommonUtils.isValidMobileNo(model.getMobileNo()) )
				{
					model.setValidRecord(Boolean.FALSE);
				}
        
		        Date cnicExpiryDate = PortalDateUtils.parseStringAsDate(model.getCnicExpiryDateStr(), CSV_DATE_FORMAT);
		        if(cnicExpiryDate.before(currentDate))
		        {
		        	model.setValidRecord(false);
		        }
		        boolean isLevel2 = true;
		        if(bulkCustomerAccountVo.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0) 
		        		|| bulkCustomerAccountVo.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)){
		        	
		        	isLevel2 = false;
		        }
		        
		        model.setIsLevel2(isLevel2);
		        
				if(isLevel2 && GenericValidator.isBlankOrNull(model.getInitialAppFormNo())){
					model.setValidRecord(false);
				}
		        
		        model.setCnicExpiryDate(cnicExpiryDate);
		        model.setCustomerAccountTypeId(bulkCustomerAccountVo.getCustomerAccountTypeId());
		        model.setSegmentId(bulkCustomerAccountVo.getSegmentId());
		        model.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			}
        	catch (Exception e)
        	{
				e.printStackTrace();
			}

        }
        return parsedBulkCustomerAccountsVoList;
	}

}
