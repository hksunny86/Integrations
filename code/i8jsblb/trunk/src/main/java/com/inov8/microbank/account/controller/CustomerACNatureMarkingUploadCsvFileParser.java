package com.inov8.microbank.account.controller;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.inov8.microbank.account.vo.BlacklistMarkingBulkUploadVo;
import com.inov8.microbank.account.vo.CustomerACNatureMarkingUploadVo;
import com.inov8.microbank.common.util.PortalDateUtils;

import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.inov8.microbank.common.util.PortalDateUtils.CSV_DATE_FORMAT;

public class CustomerACNatureMarkingUploadCsvFileParser {

    public List<CustomerACNatureMarkingUploadVo> parseCustomerAcNatureMarkingBulkUploadCsvFile
            (InputStream inputStream, CustomerACNatureMarkingUploadVo customerACNatureMarkingUploadVo) throws FileNotFoundException,
            IOException, ParseException
    {
        List<CustomerACNatureMarkingUploadVo> parsedCustomerACNatureMarkingBulkUploadVoList = null;
        Reader reader = new InputStreamReader(inputStream);
        CSVReader csvReader = new CSVReader(reader);
        /* CSVReader reader = new CSVReader(new FileReader(filePath)); // point the file from file system*/
        Date currentDate = PortalDateUtils.parseStringAsDate(PortalDateUtils.currentFormattedDate(CSV_DATE_FORMAT),CSV_DATE_FORMAT);
        HeaderColumnNameTranslateMappingStrategy<CustomerACNatureMarkingUploadVo> mappingStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
        mappingStrategy.setType(CustomerACNatureMarkingUploadVo.class);

        Map<String, String> headerColumnNameToPropertyMap = new HashMap<>(1);
        headerColumnNameToPropertyMap.put("CNIC", "cnic");
        mappingStrategy.setColumnMapping(headerColumnNameToPropertyMap);
        CsvToBean<CustomerACNatureMarkingUploadVo> csvToBean = new CsvToBean<>();
        parsedCustomerACNatureMarkingBulkUploadVoList = csvToBean.parse(mappingStrategy, reader);

        reader.close();
        return parsedCustomerACNatureMarkingBulkUploadVoList;
    }
}
