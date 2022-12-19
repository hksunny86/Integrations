package com.inov8.microbank.account.controller;
import static com.inov8.microbank.common.util.PortalDateUtils.CSV_DATE_FORMAT;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.inov8.microbank.account.vo.BlacklistMarkingBulkUploadVo;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.PortalDateUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Malik on 8/24/2016.
 */
public class BlacklistMarkingBulkUploadCsvFileParser
{
        public List<BlacklistMarkingBulkUploadVo> parseBlacklistMarkingBulkUploadCsvFile(InputStream inputStream, BlacklistMarkingBulkUploadVo blacklistMarkingBulkUploadVo) throws FileNotFoundException, IOException, ParseException
        {
            List<BlacklistMarkingBulkUploadVo> parsedBlacklistMarkingBulkUploadVoList = null;
            Reader reader = new InputStreamReader(inputStream);
            CSVReader csvReader = new CSVReader(reader);
           /* CSVReader reader = new CSVReader(new FileReader(filePath)); // point the file from file system*/
            Date currentDate = PortalDateUtils.parseStringAsDate(PortalDateUtils.currentFormattedDate(CSV_DATE_FORMAT),CSV_DATE_FORMAT);
            HeaderColumnNameTranslateMappingStrategy<BlacklistMarkingBulkUploadVo> mappingStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
            mappingStrategy.setType(BlacklistMarkingBulkUploadVo.class);

            Map<String, String> headerColumnNameToPropertyMap = new HashMap<>(1);
            headerColumnNameToPropertyMap.put("CNIC", "cnic");
            mappingStrategy.setColumnMapping(headerColumnNameToPropertyMap);
            CsvToBean<BlacklistMarkingBulkUploadVo> csvToBean = new CsvToBean<>();
            parsedBlacklistMarkingBulkUploadVoList = csvToBean.parse(mappingStrategy, reader);

            reader.close();
            return parsedBlacklistMarkingBulkUploadVoList;
        }


    }

