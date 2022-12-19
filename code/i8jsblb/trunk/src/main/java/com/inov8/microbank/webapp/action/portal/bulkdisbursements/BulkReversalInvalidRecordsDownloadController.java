package com.inov8.microbank.webapp.action.portal.bulkdisbursements;

import au.com.bytecode.opencsv.CSVWriter;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkAutoReversalModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BulkReversalInvalidRecordsDownloadController {

    @RequestMapping("/p_downloadinvalidbulkreversalrecordsfile")
    public void downloadinvalidbulkreversalrecordsfile(HttpSession session, HttpServletResponse response) throws IOException
    {
        List<BulkAutoReversalModel> list = (List<BulkAutoReversalModel>) session.getAttribute("bulkAutoReversalModelList");
        String csvFileName = "Bulk Reversal Invalid Records.csv";
        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader(headerKey, headerValue);
        CSVWriter csvWriter = new CSVWriter(response.getWriter());
        List<String[]> rowsList = transformBulkReversalInvalidRowsToCsv(list);

        csvWriter.writeAll(rowsList);
        csvWriter.close();
    }

    private List<String[]> transformBulkReversalInvalidRowsToCsv(List<BulkAutoReversalModel> list)
    {
        List<String[]> rowsList = new ArrayList<>();
        String[] header = {"Sr No.","Transaction ID","valildationErrorMessage"};
        rowsList.add(header);
        for (BulkAutoReversalModel model : list)
        {
            String adjustmentType=null;
            if( !model.getIsValid() )
            {
                if(model.getValildationErrorMessage() == null){
                    model.setValildationErrorMessage("");
                }
                String[] row = {model.getSrNo(),model.getTrxnId().toString(),model.getValildationErrorMessage()};
                rowsList.add(row);

            }
            else
            {
                break;//no more invalid records as list is sorted on basis of invalid records
            }
        }
        return rowsList;
    }
}
