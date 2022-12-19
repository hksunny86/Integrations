package com.inov8.microbank.account.controller;

import au.com.bytecode.opencsv.CSVWriter;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.account.vo.CustomerACNatureMarkingUploadVo;
import com.inov8.microbank.app.model.AppInfoModel;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.disbursement.model.BulkDisbursementsFileInfoModel;
import com.inov8.microbank.disbursement.model.DisbursementFileInfoViewModel;
import com.inov8.microbank.disbursement.service.DisbursementFileFacade;
import com.inov8.microbank.disbursement.vo.BulkDisbursementsVOModel;
import com.thoughtworks.xstream.XStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BulkDisbursementValidRecordsDownloadController {

    @Autowired
    private DisbursementFileFacade disbursementFacade;

    @RequestMapping("/p_downloadBulkDisbursementValidRecordsfile")
    public void downloadValidRecordsFile(HttpSession session, HttpServletResponse response) throws IOException
    {
        List<ActionAuthorizationModel> list = (List<ActionAuthorizationModel>) session.getAttribute("actionAuthorizationModelList");
        list.get(0).getReferenceData();
        String csvFileName = "Valid Records.csv";
        response.setContentType("text/csv");

        List<BulkDisbursementsModel> bulkDisbursementsModelList=new ArrayList<>();

        XStream xstream = new XStream();
        BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel = (BulkDisbursementsFileInfoModel) xstream.fromXML
                (list.get(0).getReferenceData());

        String batchNumber = bulkDisbursementsFileInfoModel.getBatchNumber();

        BulkDisbursementsModel bulkDisbursementsModel = new BulkDisbursementsModel();
        bulkDisbursementsModel.setBatchNumber(batchNumber);
        bulkDisbursementsModelList = disbursementFacade.findBulkDibursementsByBatchNumber(bulkDisbursementsModel).getResultsetList();

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader(headerKey, headerValue);
        CSVWriter csvWriter = new CSVWriter(response.getWriter());

        List<String[]> rowsList = transformValidRowsToCsv(bulkDisbursementsModelList);

        csvWriter.writeAll(rowsList);
        csvWriter.close();
    }

    private List<String[]> transformValidRowsToCsv(List<BulkDisbursementsModel> list)
    {
        List<String[]> rowsList = new ArrayList<>();
        String[] header = {"Employee No","Name","MobileNo","Cheque#","Amount","Amount To be Credited On","Description"};
        rowsList.add(header);
        for (BulkDisbursementsModel model : list)
        {
            if( model.getValidRecord())
            {
                String[] row = {model.getEmployeeNo(),model.getName(),model.getMobileNo(),model.getChequeNo(),
                        String.valueOf(model.getAmount()), String.valueOf(model.getPaymentDate()),model.getDescription()};
                rowsList.add(row);
            }
            else
            {
                continue;//no more invalid records as list is sorted on basis of invalid records
            }
        }
        return rowsList;
    }

    @RequestMapping(value= "/downloadValidRecords")
    public String processBatch(@RequestParam HttpSession request, HttpServletResponse response, String id) throws FrameworkCheckedException {
        DisbursementFileInfoViewModel infoViewModel = disbursementFacade.findDisbursementInfoViewModel(Long.parseLong(id));
        request.setAttribute("disbursementFileInfoViewModel", infoViewModel);

        try {
            downloadValidRecordsFile(request,response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "p_downloadBulkDisbursementValidRecordsfile";
    }

    public void setDisbursementFacade(DisbursementFileFacade disbursementFacade) {
        this.disbursementFacade = disbursementFacade;
    }
}
