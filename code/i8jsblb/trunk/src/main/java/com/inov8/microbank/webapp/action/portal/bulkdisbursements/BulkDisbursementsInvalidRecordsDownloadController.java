/**
 * 
 */
package com.inov8.microbank.webapp.action.portal.bulkdisbursements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.inov8.microbank.common.model.customermodule.BulkUpdateCustomerScreeningModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import au.com.bytecode.opencsv.CSVWriter;

import com.inov8.microbank.common.model.BulkDisbursementsModel;
//import com.inov8.microbank.common.model.BulkManualAdjustmentModel;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.vo.BulkFilerNonFilerVO;
import com.inov8.microbank.common.vo.account.BulkCustomerAccountVo;

/**
 * @author NaseerUl
 *
 */
@Controller
public class BulkDisbursementsInvalidRecordsDownloadController
{

	/**
	 * @see BulkDisbursementInvalidRecordsComparator
	 * @param session
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/p_downloadinvalidrecordsfile")
	public void downloadFile(@RequestParam Long productId, HttpSession session, HttpServletResponse response) throws IOException
	{
		List<BulkDisbursementsModel> list = (List<BulkDisbursementsModel>) session.getAttribute("bulkDisbursementsModelList");
		String csvFileName = "Invalid Records.csv";
        response.setContentType("text/csv");
 
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader(headerKey, headerValue);
        CSVWriter csvWriter = new CSVWriter(response.getWriter());
        List<String[]> rowsList = null;
        if(ProductConstantsInterface.BULK_DISBURSEMENT.longValue() == productId)
		{
        	rowsList = transformBulkDisbursementInvalidRowsToCsv(list);
		}
        else
        {
        	rowsList = transformBulkPaymentInvalidRowsToCsv(list);
        }

        csvWriter.writeAll(rowsList);
        csvWriter.close();
	}

	private List<String[]> transformBulkDisbursementInvalidRowsToCsv(List<BulkDisbursementsModel> list)
	{
		List<String[]> rowsList = new ArrayList<>();
		String[] header = {"Employee No","Name","MobileNo(BBANo)","type","Cheque#","Amount","Amount to be credited on","description"};
		rowsList.add(header);
		for (BulkDisbursementsModel model : list)
        {
        	if( !model.getValidRecord() )
        	{
        		String[] row = {model.getEmployeeNo(),model.getName(),model.getMobileNo(),String.valueOf(model.getServiceId()),model.getChequeNo(),String.valueOf(model.getAmount()), model.getPaymentDateStr(),model.getDescription() };
        		rowsList.add(row);
        	}
        	else
        	{
        		break;//no more invalid records as list is sorted on basis of invalid records
        	}
		}
		return rowsList;
	}

	private List<String[]> transformBulkPaymentInvalidRowsToCsv(List<BulkDisbursementsModel> list)
	{
		List<String[]> rowsList = new ArrayList<>();
		String[] header = {"Registration No","Name","CNIC","MobileNo","type","Cheque#","Amount","Amount to be credited on","description"};
		rowsList.add(header);
		for (BulkDisbursementsModel model : list)
        {
        	if( !model.getValidRecord() )
        	{
        		String[] row = {model.getEmployeeNo(),model.getName(),model.getCnic(),model.getMobileNo(),String.valueOf(model.getServiceId()),model.getChequeNo(),String.valueOf(model.getAmount()), model.getPaymentDateStr(),model.getDescription()};
        		rowsList.add(row);
        	}
        	else
        	{
        		break;//no more invalid records as list is sorted on basis of invalid records
        	}
		}
		return rowsList;
	}
	
	/**
	 * @see BulkDisbursementInvalidRecordsComparator
	 * @param session
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/p_downloadInvalidCustomerBulkRecordsFile")
	public void downloadInvalidCustomerBulkRecordsFile(HttpSession session, HttpServletResponse response) throws IOException
	{
		List<BulkCustomerAccountVo> list = (List<BulkCustomerAccountVo>) session.getAttribute("bulkCustomerAccountVoList");
		String csvFileName = "Invalid Records.csv";
        response.setContentType("text/csv");
 
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader(headerKey, headerValue);
        CSVWriter csvWriter = new CSVWriter(response.getWriter());
        List<String[]> rowsList = transformBulkCustomerInvalidRowsToCsv(list);

        csvWriter.writeAll(rowsList);
        csvWriter.close();
	}
	
	private List<String[]> transformBulkCustomerInvalidRowsToCsv(List<BulkCustomerAccountVo> list)
	{
		List<String[]> rowsList = new ArrayList<>();
		String[] header = {"Name","MobileNo","CNIC", "CNIC Expiry","Initial Application Form No"};
		rowsList.add(header);
		for (BulkCustomerAccountVo model : list)
        {
        	if( !model.getValidRecord() )
        	{
        		String[] row = {model.getName(),model.getMobileNo(),model.getCnic(),model.getCnicExpiryDateStr(),model.getInitialAppFormNo()};
        		rowsList.add(row);
        	}
        	else
        	{
        		break;//no more invalid records as list is sorted on basis of invalid records
        	}
		}
		return rowsList;
	}
	@RequestMapping("/p_downloadinvalidbulkadjustmentrecordsfile")
	public void downloadInvalidBulkAdjustmentRecordsFile(HttpSession session, HttpServletResponse response) throws IOException
	{
		List<BulkManualAdjustmentModel> list = (List<BulkManualAdjustmentModel>) session.getAttribute("bulkManualAdjustmentsModelList");
		String csvFileName = "Bulk Adjustment Invalid Records.csv";
		response.setContentType("text/csv");

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
		response.setHeader(headerKey, headerValue);
		CSVWriter csvWriter = new CSVWriter(response.getWriter());
		List<String[]> rowsList = transformBulkAdjustmentInvalidRowsToCsv(list);

		csvWriter.writeAll(rowsList);
		csvWriter.close();
	}

	private List<String[]> transformBulkAdjustmentInvalidRowsToCsv(List<BulkManualAdjustmentModel> list)
	{
		List<String[]> rowsList = new ArrayList<>();
		String[] header = {"Sr No.","Transaction ID","Adjustment Type","From Account #", "To Account #","Amount","Description","valildationErrorMessage"};
		rowsList.add(header);
		for (BulkManualAdjustmentModel model : list)
		{
			String adjustmentType=null;
			if( !model.getIsValid() )
			{
				if(model.getAdjustmentType() == null){
					adjustmentType="";
				}else{
					adjustmentType=model.getAdjustmentType().toString();
				}
				if(model.getTrxnId() == null){
					model.setTrxnId("");
				}
				if(model.getFromAccount() == null){
					model.setFromAccount("");
				}
				if(model.getToAccount() == null){
					model.setToAccount("");
				}
				if(model.getBalance() == null){
					model.setBalance("");
				}
				if(model.getValildationErrorMessage() == null){
					model.setValildationErrorMessage("");
				}
				String[] row = {model.getSrNo(),model.getTrxnId().toString(),adjustmentType,model.getFromAccount(),model.getToAccount(),model.getBalance().toString(),model.getDescription(),model.getValildationErrorMessage()};
				rowsList.add(row);

			}
			else
			{
				break;//no more invalid records as list is sorted on basis of invalid records
			}
		}
		return rowsList;
	}
	
	@RequestMapping("/p_downloadInvalidCustomerBulkUpdateScreeningPerformedRecordsFile")
	public void downloadInvalidCustomerBulkScreeningPerformedRecordsFile(HttpSession session, HttpServletResponse response) throws IOException
	{
		List<BulkUpdateCustomerScreeningModel> list = (List<BulkUpdateCustomerScreeningModel>) session.getAttribute("bulkUpdateCustomerScreeningModelsLsit");
		String csvFileName = "Invalid Records.csv";
        response.setContentType("text/csv");
 
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader(headerKey, headerValue);
        CSVWriter csvWriter = new CSVWriter(response.getWriter());
        List<String[]> rowsList = transformBulkUpdateScreeningPerformedInvalidRowsToCsv(list);

        csvWriter.writeAll(rowsList);
        csvWriter.close();
	}
	private List<String[]> transformBulkUpdateScreeningPerformedInvalidRowsToCsv(List<BulkUpdateCustomerScreeningModel> list)
	{
		List<String[]> rowsList = new ArrayList<>();
		String[] header = {"Sr No.","CNIC","Screening Performed","Error Reason"};
		rowsList.add(header);
		for (BulkUpdateCustomerScreeningModel model : list)
		{
			String cnic=null;
			if( !model.getIsValid() )
			{
				if(model.getCnic() == null){
					cnic="";
				}else{
					cnic=model.getCnic().toString();
				}
				if(model.getIsScreeningPerformed() == null){
					model.setScreeningPerformed("No");
				}
				String[] row = {model.getSrNo(),cnic,model.getScreeningPerformed().toString(),model.getDescription()};
				rowsList.add(row);

			}
			else
			{
				break;//no more invalid records as list is sorted on basis of invalid records
			}
		}
		return rowsList;
	}
	
	@RequestMapping("/p_downloadInvalidBulkUpdateFilerNonFilerRecordsFile")
	public void downloadInvalidBulkUpdateFilerNonFilerRecordsFile(HttpSession session, HttpServletResponse response) throws IOException
	{
		List<BulkFilerNonFilerVO> list = (List<BulkFilerNonFilerVO>) session.getAttribute("bulkFilerNonFilerVOList");
		String csvFileName = "Invalid Records.csv";
        response.setContentType("text/csv");
 
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader(headerKey, headerValue);
        CSVWriter csvWriter = new CSVWriter(response.getWriter());
        List<String[]> rowsList = transformBulkUpdateFilerNonFilerInvalidRowsToCsv(list);

        csvWriter.writeAll(rowsList);
        csvWriter.close();
	}
	
	private List<String[]> transformBulkUpdateFilerNonFilerInvalidRowsToCsv(List<BulkFilerNonFilerVO> list)
	{
		List<String[]> rowsList = new ArrayList<>();
		String[] header = {"Sr No.","CNIC","Is Filer","Error Reason"};
		rowsList.add(header);
		for (BulkFilerNonFilerVO vo : list)
		{
			String cnic=null;
			if( !vo.getIsValid() )
			{
				if(vo.getCnic() == null){
					cnic="";
				}else{
					cnic=vo.getCnic().toString();
				}
				if(vo.getFiler() == null){
					vo.setFiler("");
				}
				String[] row = {vo.getSrNo(),cnic,vo.getFiler().toString(),vo.getDescription()};
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
