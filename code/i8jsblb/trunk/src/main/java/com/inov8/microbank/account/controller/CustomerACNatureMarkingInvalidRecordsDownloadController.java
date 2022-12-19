package com.inov8.microbank.account.controller;

import au.com.bytecode.opencsv.CSVWriter;
import com.inov8.microbank.account.vo.BlacklistMarkingBulkUploadVo;
import com.inov8.microbank.account.vo.CustomerACNatureMarkingUploadVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerACNatureMarkingInvalidRecordsDownloadController {

    @RequestMapping("/p_downloadcustomeracnaturemarkinngbulkrecordsfile")
    public void downloadInvalidBlacklistRecordsFile(HttpSession session, HttpServletResponse response) throws IOException
    {
        List<CustomerACNatureMarkingUploadVo> list = (List<CustomerACNatureMarkingUploadVo>) session.getAttribute("customerACNatureMarkingBulkUploadVoList");
        String csvFileName = "Invalid Records.csv";
        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader(headerKey, headerValue);
        CSVWriter csvWriter = new CSVWriter(response.getWriter());
        List<String[]> rowsList = transformBlacklistInvalidRowsToCsv(list);

        csvWriter.writeAll(rowsList);
        csvWriter.close();
    }

    private List<String[]> transformBlacklistInvalidRowsToCsv(List<CustomerACNatureMarkingUploadVo> list)
    {
        List<String[]> rowsList = new ArrayList<>();
        String[] header = {"CNIC","Status"};
        rowsList.add(header);
        for (CustomerACNatureMarkingUploadVo model : list)
        {
            if( !model.getValidRecord() )
            {
                String[] row = {model.getCnic(),model.getStatus()};
                rowsList.add(row);
            }
            else
            {
                continue;//no more invalid records as list is sorted on basis of invalid records
            }
        }
        return rowsList;
    }
}
