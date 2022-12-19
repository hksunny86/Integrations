package com.inov8.microbank.disbursement.controller;

import au.com.bytecode.opencsv.CSVWriter;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.disbursement.dao.BulkDisbursementsFileInfoDAO;
import com.inov8.microbank.disbursement.dao.DisbursementFileInfoViewDAO;
import com.inov8.microbank.disbursement.model.BulkDisbursementsFileInfoModel;
import com.inov8.microbank.disbursement.model.DisbursementFileInfoViewModel;
import com.inov8.microbank.disbursement.service.BulkDisbursementsManager;
import com.inov8.microbank.disbursement.util.DisbursementStatusConstants;
import com.inov8.microbank.disbursement.vo.BulkDisbursementsVOModel;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.microbank.webapp.action.portal.manualadjustmentmodule.BulkManualAdjustmentVOModel;
import com.inov8.microbank.webapp.action.portal.manualadjustmentmodule.ManualAdjustmentAuthorizationController;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkDisbursementsAuthorizationController  extends AdvanceAuthorizationFormController {

    private static final Logger LOGGER = Logger.getLogger( BulkDisbursementsAuthorizationController.class );
    private BulkDisbursementsManager bulkDisbursementsManager;
    private ReferenceDataManager referenceDataManager;
    private DisbursementFileInfoViewDAO disbursementFileInfoViewDAO;
    private BulkDisbursementsFileInfoDAO bulkDisbursementsFileInfoDAO;

    public BulkDisbursementsAuthorizationController() {
        setCommandName("actionAuthorizationModel");
        setCommandClass(ActionAuthorizationModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest request) throws Exception {
        Map<String, List<?>> referenceDataMap = new HashMap<String, List<?>>();
        boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
        boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);

        if (escalateRequest || resolveRequest){
            ActionStatusModel actionStatusModel = new ActionStatusModel();
            ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl( actionStatusModel, "name", SortingOrder.ASC );
            referenceDataManager.getReferenceData( refDataWrapper );
            List<ActionStatusModel> actionStatusModelList;
            actionStatusModelList=refDataWrapper.getReferenceDataList();
            List<ActionStatusModel> tempActionStatusModelList = new ArrayList<>();

            for (ActionStatusModel actionStatusModel2 :  actionStatusModelList) {
                if(((actionStatusModel2.getActionStatusId().intValue()== ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.intValue())
                        ||(actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL.intValue())
                        || (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.intValue())
                        || (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.intValue()))
                        && escalateRequest )
                    tempActionStatusModelList.add(actionStatusModel2);
                else if((actionStatusModel2.getActionStatusId().intValue()== ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED.intValue()) && resolveRequest)
                    tempActionStatusModelList.add(actionStatusModel2);
            }
            referenceDataMap.put( "actionStatusModel", tempActionStatusModelList);

            ////// Action Authorization history////
            Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);

            ActionAuthorizationHistoryModel actionAuthorizationHistoryModel = new ActionAuthorizationHistoryModel();
            actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);

            List<ActionAuthorizationHistoryModel> actionAuthorizationHistoryModelList;

            refDataWrapper = new ReferenceDataWrapperImpl( actionAuthorizationHistoryModel, "escalationLevel", SortingOrder.ASC );
            referenceDataManager.getReferenceData( refDataWrapper );

            actionAuthorizationHistoryModelList=refDataWrapper.getReferenceDataList();

            referenceDataMap.put( "actionAuthorizationHistoryModelList",actionAuthorizationHistoryModelList );


            List<ActionAuthorizationModel> actionAuthorizationModelList;

            refDataWrapper = new ReferenceDataWrapperImpl( actionAuthorizationModel, "escalationLevel", SortingOrder.ASC );
            referenceDataManager.getReferenceData( refDataWrapper );

            actionAuthorizationModelList=refDataWrapper.getReferenceDataList();

            referenceDataMap.put( "actionAuthorizationModelList",actionAuthorizationModelList );

            request.getSession().setAttribute("actionAuthorizationModelList", actionAuthorizationModelList);
            XStream xstream = new XStream();

            BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel = (BulkDisbursementsFileInfoModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
            String batchNumber = bulkDisbursementsFileInfoModel.getBatchNumber();

            DisbursementFileInfoViewModel disbursementFileInfoViewModel = new DisbursementFileInfoViewModel();
            disbursementFileInfoViewModel.setBatchNumber(batchNumber);
            disbursementFileInfoViewDAO.findByExample(disbursementFileInfoViewModel);

            List<DisbursementFileInfoViewModel> disbursementFileInfoViewModelList;

            refDataWrapper = new ReferenceDataWrapperImpl( disbursementFileInfoViewModel, "batchNumber", SortingOrder.ASC );
            referenceDataManager.getReferenceData( refDataWrapper );

            disbursementFileInfoViewModelList=refDataWrapper.getReferenceDataList();

            referenceDataMap.put( "disbursementFileInfoViewModel",disbursementFileInfoViewModelList );

            if(actionAuthorizationModel.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
                    && actionAuthorizationModel.getCreatedById().longValue()== UserUtils.getCurrentUser().getAppUserId()){
                boolean isAssignedBack=false;
                isAssignedBack=true;
                request.setAttribute( "isAssignedBack",isAssignedBack );
            }
        }
        return referenceDataMap;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
        boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
        boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);

        if (escalateRequest || resolveRequest) {
            Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);

            XStream xstream = new XStream();

            BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel = (BulkDisbursementsFileInfoModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
            String batchNumber = bulkDisbursementsFileInfoModel.getBatchNumber();
            BulkDisbursementsModel bulkDisbursementsModel = new BulkDisbursementsModel();
            bulkDisbursementsModel.setBatchNumber(batchNumber);
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(bulkDisbursementsModel);

            List<BulkDisbursementsModel> resultList = bulkDisbursementsManager.loadBulkDisbursementModelList(searchBaseWrapper);

            request.setAttribute("bulkDisbursementsModelList",resultList);
            return actionAuthorizationModel;
        }
        else
            return new ActionAuthorizationModel();
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onEscalate(HttpServletRequest req,HttpServletResponse resp, Object command, BindException errors) throws Exception {
        ModelAndView modelAndView = null;
        ActionAuthorizationModel model = (ActionAuthorizationModel) command;
        BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel = new BulkDisbursementsFileInfoModel();
//        String partnersAssociation[] = req.getParameterValues("checkedList");
        try {
            XStream xstream = new XStream();

            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
            boolean isValidChecker = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(), UserUtils.getCurrentUser().getAppUserId());
            long currentUserId = UserUtils.getCurrentUser().getAppUserId();

//            BulkDisbursementsVOModel bulkDisbursementsVOModel = (BulkDisbursementsVOModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
//            String batchNumber = bulkDisbursementsVOModel.getBatchNumber();

            bulkDisbursementsFileInfoModel = (BulkDisbursementsFileInfoModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
            String batchNumber = bulkDisbursementsFileInfoModel.getBatchNumber();

            UsecaseModel usecaseModel = usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
            if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue()
                    && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)) {
                if ((!isValidChecker) || (actionAuthorizationModel.getCreatedById().longValue() == currentUserId)) {
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }
                long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(PortalConstants.UPDATE_BULK_DISBURSEMENT_USECASE_ID,
                        actionAuthorizationModel.getEscalationLevel());
                if (nextAuthorizationLevel < 1) {
                    bulkDisbursementsManager.updateIsApprovedForBatch(batchNumber);

                    bulkDisbursementsFileInfoDAO.updateDisbursementFileStatusAndApprove(bulkDisbursementsFileInfoModel.getBatchNumber(),
                            DisbursementStatusConstants.STATUS_READY_TO_DISBURSE, "1");
//                    List<BulkDisbursementsModel> bulkDisbursementsModelList = loadBulkDisbursementsModelList(batchNumber);
//                    if (CollectionUtils.isNotEmpty(bulkDisbursementsModelList)) {
//                        for (BulkDisbursementsModel bulkModel : bulkDisbursementsModelList) {
//
//                        }
//                    }
                    String msg = this.getText("bulkDisbursement.add.success", req.getLocale());
                    String newMsg = msg + " against Batch Number : " + batchNumber;
                    this.saveMessage(req, newMsg);

                    if (actionAuthorizationModel.getEscalationLevel().intValue() < usecaseModel.getEscalationLevels().intValue()) {
                        approvedWithIntimationLevelsNext(actionAuthorizationModel, model, usecaseModel, req);
                    } else {
                        approvedAtMaxLevel(actionAuthorizationModel, model);
                    }
                } else {
                    escalateToNextLevel(actionAuthorizationModel, model, nextAuthorizationLevel, usecaseModel.getUsecaseId(), req);
                }
            } else if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.longValue()
                    && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)) {
                isValidChecker = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(),
                        actionAuthorizationModel.getEscalationLevel(), UserUtils.getCurrentUser().getAppUserId());

                if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))){
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }
                actionDeniedOrCancelled(actionAuthorizationModel, model,req);
            }else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.longValue()
                    && (actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)
                    || actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK))){

                if(!(actionAuthorizationModel.getCreatedById().equals(currentUserId))){
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }
                actionDeniedOrCancelled(actionAuthorizationModel,model,req);
            }else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
                    && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
                isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());

                if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))){
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }
                requestAssignedBack(actionAuthorizationModel,model,req);
            }else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_RE_SUBMIT.longValue()
                    && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK)){

                if(!(actionAuthorizationModel.getCreatedById().equals(currentUserId))){
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }
            }else{
                throw new FrameworkCheckedException("Invalid status marked");
            }
        }
        catch (FrameworkCheckedException ex)
        {
            LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
            req.setAttribute("message", ex.getMessage());
            req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(req, resp, errors);
        }
        catch(Exception ex){
            ex.printStackTrace();
            String msg = super.getText("bulkDisbursement.add.failure", req.getLocale());
            req.setAttribute("message", msg);
            req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(req, resp, errors);
        }

        req.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
        modelAndView = super.showForm(req, resp, errors);
//        return new ModelAndView("disbursementFileInfoList"+bulkDisbursementsFileInfoModel);
        return modelAndView;
    }

    @Override
    protected ModelAndView onResolve(HttpServletRequest req, HttpServletResponse resp, Object command, BindException errors) throws Exception {
        ModelAndView modelAndView = null;
        ActionAuthorizationModel model = (ActionAuthorizationModel) command;
//        String partnersAssociation[] = req.getParameterValues("checkedList");
        try {
            XStream xstream = new XStream();

            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
            UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
            BaseWrapper baseWrapper = new BaseWrapperImpl();

//            BulkDisbursementsVOModel bulkDisbursementsVOModel = (BulkDisbursementsVOModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
//            String batchNumber = bulkDisbursementsVOModel.getBatchNumber();

            BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel = (BulkDisbursementsFileInfoModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
            String batchNumber = bulkDisbursementsFileInfoModel.getBatchNumber();

            bulkDisbursementsManager.updateIsApprovedForBatch(batchNumber);
//            List<BulkManualAdjustmentModel> bulkManualAdjustmentModelList = loadBulkManualAdjustmentModelList(batchId);

            String msg = this.getText("bulkDisbursement.add.success", req.getLocale());
            String newMsg=msg+" against Batch Number : "+batchNumber;
            this.saveMessage(req, newMsg);

            resolveWithIntimation(actionAuthorizationModel,model, usecaseModel, req);

        }
        catch (Exception e){
            LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), e);
            req.setAttribute("message", e.getMessage());
            req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(req, resp, errors);
        }
        req.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
        modelAndView = super.showForm(req, resp, errors);
        return modelAndView;
    }
    private List<BulkDisbursementsModel> loadBulkDisbursementsModelList(String batchNumber) throws Exception{
        BulkDisbursementsModel bulkDisbursementsModel = new BulkDisbursementsModel();
        bulkDisbursementsModel.setBatchNumber(batchNumber);
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(bulkDisbursementsModel);

        return bulkDisbursementsManager.loadBulkDisbursementModelList(searchBaseWrapper);
    }

    @RequestMapping(value= "/downloadValidRecords")
    public String loadDisbursementFileInfoModel(HttpSession session, HttpServletResponse response) throws FrameworkCheckedException {
        List<BulkDisbursementsFileInfoModel> list = (List<BulkDisbursementsFileInfoModel>) session.getAttribute("disbursementFileInfoViewModel");
        String csvFileName = "Invalid Records.csv";
        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader(headerKey, headerValue);
        CSVWriter csvWriter = null;
        try {
            csvWriter = new CSVWriter(response.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String[]> rowsList = transformValidRowsToCsv(list);

        csvWriter.writeAll(rowsList);
        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String[]> transformValidRowsToCsv(List<BulkDisbursementsFileInfoModel> list)
    {
        List<String[]> rowsList = new ArrayList<>();
        String[] header = {"CNIC","Status"};
        rowsList.add(header);
        for (BulkDisbursementsFileInfoModel model : list)
        {
            if( model.getValidRecords() > 1)
            {
                String[] row = {String.valueOf(model.getValidRecords()), String.valueOf(model.getStatus())};
                rowsList.add(row);
            }
            else
            {
                continue;//no more invalid records as list is sorted on basis of invalid records
            }
        }
        return rowsList;
    }

    public void setBulkDisbursementsManager(BulkDisbursementsManager bulkDisbursementsManager) {
        this.bulkDisbursementsManager = bulkDisbursementsManager;
    }

    @Override
    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setDisbursementFileInfoViewDAO(DisbursementFileInfoViewDAO disbursementFileInfoViewDAO) {
        this.disbursementFileInfoViewDAO = disbursementFileInfoViewDAO;
    }

    public void setBulkDisbursementsFileInfoDAO(BulkDisbursementsFileInfoDAO bulkDisbursementsFileInfoDAO) {
        this.bulkDisbursementsFileInfoDAO = bulkDisbursementsFileInfoDAO;
    }
}
