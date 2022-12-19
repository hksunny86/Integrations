package com.inov8.microbank.webapp.action.customermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.customermodule.CustomerManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import java.awt.event.ActionEvent;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "bulkCustomerSegmentActionBean")
@SessionScoped
public class BulkCustomerSegmentActionBean {

    private static final Log LOGGER = LogFactory.getLog(BulkCustomerSegmentActionBean.class);

    @ManagedProperty(value = "#{customerManager}")
    private CustomerManager customerManager;

    @ManagedProperty(value = "#{userManager}")
    private AppUserManager appUserManager;

    private boolean validateActionDisabled = Boolean.TRUE;
    private boolean addBulkCustomerActionDisabled = Boolean.TRUE;
    private boolean fileUploadActionDisabled = Boolean.TRUE;

    private List<String> errorList = new ArrayList<String>();
    private String errors = "";

    private ArrayList<UploadedFile> files = new ArrayList<UploadedFile>();
    private List<String[]> rows = null;

    private List<SegmentModel> segmentModelList;
    private List<SelectItem> segments;
    private List<CustomerModel> customerIdsToUpdate = new ArrayList<>(0);

    private String customerSegmentId;

    @PostConstruct
    public void ini()
    {
        try
        {
            LOGGER.info("Initializing BulkCustomerSegmentActionBean...");
            this.segments = new ArrayList<SelectItem>();
            setCustomerSegmentId("-1");
            SegmentModel segmentModel = new SegmentModel();
            segmentModel.setIsActive(Boolean.TRUE);
            segmentModelList = customerManager.findActiveSegments(segmentModel);
            if(segmentModelList != null)
            {
                for(SegmentModel model : segmentModelList)
                {
                    SelectItem item = new SelectItem();
                    item.setLabel(model.getName());
                    item.setValue(model.getSegmentId());
                    this.segments.add(item);
                }
            }
        }
        catch(FrameworkCheckedException ex)
        {
            LOGGER.info("Error Occurred while loading Customer Segments :: " + ex.getMessage(),ex);
        }
    }

    public void uploadFile(FileUploadEvent event) throws Exception
    {
        this.errorList.clear();
        segmentModelList.clear();
        addBulkCustomerActionDisabled = Boolean.TRUE;
        this.customerIdsToUpdate.clear();

        UploadedFile item = event.getUploadedFile();
        files.add(item);
        au.com.bytecode.opencsv.CSVReader reader = new au.com.bytecode.opencsv.CSVReader(new InputStreamReader(item.getInputStream()),'|');
        this.rows = reader.readAll();
        if(this.rows != null && this.rows.size() > 0)
        {
            rows.remove(0);
            this.setValidateActionDisabled(Boolean.FALSE);
        }
        reader.close();
    }

    private void clearForm()
    {
        setFileUploadActionDisabled(Boolean.TRUE);
        setValidateActionDisabled(Boolean.TRUE);
        setAddBulkCustomerActionDisabled(Boolean.TRUE);
        setCustomerSegmentId("-1");
        this.errorList.clear();
        setErrorList(errorList);
        this.segmentModelList.clear();
        this.rows = null;
        if(files.size() > 0)
        {
            files.remove(0);
        }
        customerIdsToUpdate.clear();
    }

    public void clearState(javax.faces.event.ActionEvent event)
    {
        clearForm();
    }

    public void onChangeCustomerSegment(AjaxBehaviorEvent event)
    {
        setFileUploadActionDisabled(Boolean.FALSE);
        setValidateActionDisabled(Boolean.FALSE);
        setAddBulkCustomerActionDisabled(Boolean.TRUE);
        this.errorList.clear();
        setErrorList(errorList);
        this.segmentModelList.clear();
        this.rows = null;
        if(files.size() > 0)
        {
            files.remove(0);
        }
        customerIdsToUpdate.clear();
    }

    public String clearAllSessionObjects(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            SessionBeanObjects.removeAllCustomerSessionObjects();
        }
        return "p_pgsearchuserinfo";
    }

    public void clearBulkCustomerSegmentForm(ActionEvent actionEvent)
    {
        clearForm();
    }

    public void updateCustomerSegment(javax.faces.event.ActionEvent event)
    {
        if(customerIdsToUpdate != null && !customerIdsToUpdate.isEmpty())
        {
            try {
                customerManager.updateBulkCustomerSegments(customerIdsToUpdate);
            } catch (FrameworkCheckedException e) {
                LOGGER.error("Error Occurred while Updating Customer Segments - Bulk ",e);
            }
        }
        JSFContext.setInfoMessage(this.customerIdsToUpdate.size() + " Customer(s) Segment Updated.Please verify on Customer Search Screen.");
        setValidateActionDisabled(Boolean.TRUE);
        addBulkCustomerActionDisabled = Boolean.TRUE;
        this.errorList.clear();
        this.customerIdsToUpdate.clear();
        this.segmentModelList.clear();
        rows = null;
        if(files.size() > 0)
        {
            files.remove(0);
        }
        setCustomerSegmentId("-1");
    }

    private BulkCustomerSegmentVO validateCustomerData(String mobileNo,String cNic,int index)
    {
        BulkCustomerSegmentVO vo = new BulkCustomerSegmentVO();
        if(vo.getErrors() == null)
            vo.setErrors(new ArrayList<String>());
        Boolean validated = Boolean.FALSE;
        AppUserModel model = new AppUserModel();
        model.setMobileNo(mobileNo);
        model.setNic(cNic);
        model.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        model.setAccountClosedUnsettled(Boolean.FALSE);

        AppUserModel found = appUserManager.getAppUserModel(model);
        if(found != null)
        {
            vo.setCustomerModel(found.getRelationCustomerIdCustomerModel());
            validated = Boolean.TRUE;
        }
        else
        {
            vo.getErrors().add("No Customer Fount against the Mobile #: " + mobileNo + " and NIC: " + cNic + " in row: " + index);
        }
        vo.setValidated(validated);
        return vo;
    }

    public void validateBulkCustomerSegments(javax.faces.event.ActionEvent event) throws Exception {
        boolean validated = Boolean.TRUE;
        if(CommonUtils.isEmpty(this.customerSegmentId) || this.customerSegmentId.equals("-1"))
        {
            JSFContext.addErrorMessage("Customer Segment is required.");
            validated = Boolean.FALSE;
        }
        if(this.rows == null || this.rows.size() == 0)
        {
            JSFContext.addErrorMessage("CSV file is not uploaded.");
            validated = Boolean.FALSE;
        }
        if(validated) {

            int rowIndex = 1;
            String mobileNo = null;
            String cNic = null;
            this.errorList.clear();
            this.segmentModelList.clear();
            this.customerIdsToUpdate.clear();
            BulkCustomerSegmentVO vo = null;
            CustomerModel customerModel = null;
            for (String[] row :rows)
            {
                int rowLength = row.length;
                if (rowLength == 2)
                {
                    mobileNo = row[0];
                    cNic = row[1];
                    validated = Boolean.TRUE;
                    if(!CommonUtils.isValidMobileNo(mobileNo))
                    {
                        validated = Boolean.FALSE;
                        this.errorList.add("Row "+ rowIndex +" Skipped, Mobile # Format is not valid.");
                    }
                    if(!CommonUtils.isValidCnic(cNic))
                    {
                        validated = Boolean.FALSE;
                        this.errorList.add("Row "+ rowIndex +" Skipped, Mobile # Format is not valid.");
                    }
                    if(validated)
                    {
                        vo = this.validateCustomerData(mobileNo,cNic,rowIndex);
                        if(vo.getValidated())
                        {
                            addBulkCustomerActionDisabled = Boolean.FALSE;
                            customerModel = vo.getCustomerModel();
                            customerModel.setSegmentId(Long.parseLong(customerSegmentId));
                            customerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                            customerModel.setUpdatedOn(new Date());
                            customerIdsToUpdate.add(customerModel);
                        }
                        else
                        {
                            this.errorList.addAll(vo.getErrors());
                        }
                    }
                }
                else
                {
                    this.errorList.add("Row "+ rowIndex +" Skipped, inconsistant data found.");
                    LOGGER.error("Row Skipped, inconsistant data found:  " + row[0] + " :: " + row [1]);
                }
                rowIndex++;
            }
            JSFContext.setInfoMessage(this.customerIdsToUpdate.size() + " Customer(s) are validated successfully.");
        }
    }

    public String getCustomerSegmentId() {
        return customerSegmentId;
    }

    public void setCustomerSegmentId(String customerSegmentId) {
        this.customerSegmentId = customerSegmentId;
    }

    public List<SelectItem> getSegments() {
        return segments;
    }

    public void setSegments(List<SelectItem> segments) {
        this.segments = segments;
    }

    public void setCustomerManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public ArrayList<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<UploadedFile> files) {
        this.files = files;
    }

    public boolean isValidateActionDisabled() {
        return validateActionDisabled;
    }

    public void setValidateActionDisabled(boolean validateActionDisabled) {
        this.validateActionDisabled = validateActionDisabled;
    }

    public boolean isAddBulkCustomerActionDisabled() {
        return addBulkCustomerActionDisabled;
    }

    public void setAddBulkCustomerActionDisabled(boolean addBulkCustomerActionDisabled) {
        this.addBulkCustomerActionDisabled = addBulkCustomerActionDisabled;
    }

    public boolean isFileUploadActionDisabled() {
        return fileUploadActionDisabled;
    }

    public void setFileUploadActionDisabled(boolean fileUploadActionDisabled) {
        this.fileUploadActionDisabled = fileUploadActionDisabled;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }
}
