/**
 * 
 */
package com.inov8.microbank.webapp.action.portal.bulkdisbursements;

import static com.inov8.microbank.common.util.PortalDateUtils.CSV_DATE_FORMAT;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkDisbursementsVOModel;
import com.inov8.microbank.common.util.AccountCreationStatusEnum;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.UserUtils;

/**
 * @author NaseerUl
 *
 */
public class BulkPaymentsCsvFileParser
{
	public List<BulkDisbursementsModel> parseBulkPaymentsCsvFile(String filePath, BulkDisbursementsVOModel bulkDisbursementsVOModel) throws FileNotFoundException, IOException, ParseException
	{
		List<BulkDisbursementsModel> parsedBulkDisbursementsModelList = null;
        CSVReader reader = new CSVReader(new FileReader(filePath)); // point the file from file system
        Date now = new Date();
        Date currentDate = PortalDateUtils.parseStringAsDate(PortalDateUtils.currentFormattedDate(CSV_DATE_FORMAT),CSV_DATE_FORMAT);
        HeaderColumnNameTranslateMappingStrategy<BulkDisbursementsModel> mappingStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
        mappingStrategy.setType(BulkDisbursementsModel.class);

        Map<String, String> headerColumnNameToPropertyMap = new HashMap<>(8);
        headerColumnNameToPropertyMap.put("Registration No", "employeeNo");
        headerColumnNameToPropertyMap.put("CNIC", "cnic");
        headerColumnNameToPropertyMap.put("MobileNo", "mobileNo");
        headerColumnNameToPropertyMap.put("Name", "name");
        headerColumnNameToPropertyMap.put("Cheque#", "chequeNo");
        headerColumnNameToPropertyMap.put("Amount", "amount");
        headerColumnNameToPropertyMap.put("Amount to be credited on", "paymentDateStr");
        headerColumnNameToPropertyMap.put("Description", "description");
        mappingStrategy.setColumnMapping(headerColumnNameToPropertyMap);
        CsvToBean<BulkDisbursementsModel> csvToBean = new CsvToBean<>();
        parsedBulkDisbursementsModelList = csvToBean.parse(mappingStrategy, reader);

        reader.close();
        for (BulkDisbursementsModel model : parsedBulkDisbursementsModelList)
        {
        	model.setValidRecord(true);
                      
    		if(GenericValidator.isBlankOrNull(model.getEmployeeNo()))
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
            if ( !CommonUtils.isValidMobileNo(model.getMobileNo()) )
            {
				model.setValidRecord(Boolean.FALSE);
			  }
//******** Following check will be executed on complete list for better performance
//                      if(!mobileNumberExists(model.getMobileNo())){
//                    	  continue;
//                      }
            model.setServiceId(bulkDisbursementsVOModel.getPaymentTypeId().longValue());
          
            if(GenericValidator.isBlankOrNull(model.getChequeNo()))
            {
        	    model.setValidRecord(false);
            }
          
          Date paymentDate = PortalDateUtils.parseStringAsDate(model.getPaymentDateStr(), CSV_DATE_FORMAT);
          if(paymentDate.before(currentDate))
          {
        	  model.setValidRecord(false);
          }
          model.setPaymentDate(paymentDate);

          if(GenericValidator.isBlankOrNull(model.getDescription())){
        	  model.setValidRecord(false);
          }

          model.setBatchNumber(""+getBatchNo());
          model.setCreationDate(now);
          model.setDeleted(Boolean.FALSE);
          model.setPosted(Boolean.FALSE);
          model.setSettled(Boolean.FALSE);

          model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
          model.setUpdatedOn(new Date());
          model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
          model.setProductId(bulkDisbursementsVOModel.getProductId());
          model.setLimitApplicable(bulkDisbursementsVOModel.getLimitApplicable());
          model.setPayCashViaCnic(bulkDisbursementsVOModel.getPayCashViaCnic());
          model.setSourceACNo(bulkDisbursementsVOModel.getSourceACNo());
          model.setAccountCreationStatus(AccountCreationStatusEnum.INITIATED.toString());
        }
        return parsedBulkDisbursementsModelList;
  }

	private Long getBatchNo()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHmmss");
        String transdatetime = format.format(new Date());
        return Long.parseLong(transdatetime);
	}
}
