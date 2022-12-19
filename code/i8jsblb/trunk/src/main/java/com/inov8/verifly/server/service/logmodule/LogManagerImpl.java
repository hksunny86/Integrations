package com.inov8.verifly.server.service.logmodule;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.util.CustomList;
import com.inov8.verifly.common.constants.ActionConstants;
import com.inov8.verifly.common.constants.FailureReasonConstants;
import com.inov8.verifly.common.constants.LogConstants;
import com.inov8.verifly.common.constants.StatusConstants;
import com.inov8.verifly.common.model.LogDetailModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.model.logmodule.LogListViewModel;
import com.inov8.verifly.common.wrapper.logmodule.LogWrapper;
import com.inov8.verifly.common.wrapper.logmodule.LogWrapperImpl;
import com.inov8.verifly.server.dao.logmodule.LogDAO;
import com.inov8.verifly.server.dao.logmodule.LogListViewDAO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;

public class LogManagerImpl implements LogManager {
    LogDAO logDao;
    LogListViewDAO logListViewDAO;
    private final Log logger = LogFactory.getLog(this.getClass());

    public LogManagerImpl() {}

    public LogWrapper insertLogRequiresNewTransaction(LogWrapper logWrapper) throws Exception {
        LogModel logModel = logWrapper.getLogModel();
        logModel = logDao.saveOrUpdate(logModel);
        //logWrapper.setLogModel(logModel);
        return logWrapper;
    }

    public LogWrapper updateLogRequiresNewTransaction(LogWrapper logWrapper) throws Exception {
        LogModel logModel = logWrapper.getLogModel();
        logModel = logDao.saveOrUpdate(logModel);
        logDao.saveOrUpdate(logModel);
        logWrapper.setLogModel(logModel);
        return logWrapper;
    }

    /**
     * This method interact with db to find log information and return it in logWrapper object
     *
     * @param wrapper LogWrapper
     * @return LogWrapper
     * @throws Exception
     */
    public LogWrapper viewTransactionLog(LogWrapper logWrapper) throws
            Exception {
        //log activities
        logger.info("viewTransactionLog start... ");

        LogModel logModel = new LogModel();
        LogDetailModel logDetailModel;

        // log for deactivate pin method.
        logModel.setStartTime(new Date());
        logModel.setActionId(ActionConstants.VIEW_TRANSACTION_LOG);

        // loging input params.
        XStream xstream = new XStream(new PureJavaReflectionProvider());
        String logWrapperString = xstream.toXML(logWrapper);

        logModel.setInputParam(logWrapperString);
        logDetailModel = new LogDetailModel();
        logDetailModel.setActionId(ActionConstants.VIEW_TRANSACTION_LOG);

        LogListViewModel logListViewModel = logWrapper.getLogListViewModel();

        // getting log info from db depending to logListViewModel values
        CustomList<LogListViewModel>
                customlist = this.logListViewDAO.findByExample(logListViewModel);
        List<LogListViewModel> resultSetlist = customlist.getResultsetList();

        if (resultSetlist != null && !resultSetlist.isEmpty()) {
            logWrapper.setLogListViewModelList(resultSetlist);

            //handle og info
            logDetailModel.setStatusId(StatusConstants.SUCCESS);
            logModel.setStatusId(StatusConstants.SUCCESS);

            //log activities
            logger.info("successfully found records from database");
        } else {
            //log activities
            logger.info("No record found returning NULL");

            logDetailModel.setStatusId(StatusConstants.FAILURE);
            logDetailModel.setFailureReasonId(FailureReasonConstants.
                                              RECORD_NOT_FOUND);
            logModel.setStatusId(StatusConstants.FAILURE);
            logModel.setFailureReasonId(FailureReasonConstants.RECORD_NOT_FOUND);

            logWrapper = null;
        }

        logModel.addLogIdLogDetailModel(logDetailModel);

        //saving log createdby and createdbyuserid information id send from the client
        LogModel receivedLogModel = logWrapper.getLogModel();
        String createdBy = receivedLogModel != null?receivedLogModel.getCreatedBy():"";
        Long   createdByUserId = receivedLogModel != null && receivedLogModel.getCreatdByUserId() != null ?receivedLogModel.getCreatdByUserId():0;
        //checking max range of createdByUserId
        long createdByUserIdTmp = 0;
        try{
            createdByUserIdTmp = createdByUserId.longValue();
        }catch(Exception e){
            e = null;
        }
        if ( createdByUserIdTmp != 0 && createdByUserIdTmp < LogConstants.CREATED_BY_USER_ID_MAX_RANGE ) {
            logModel.setCreatdByUserId(createdByUserIdTmp);
        }
        if(createdBy != null){
            logModel.setCreatedBy(createdBy);
        }


        logModel.setEndTime(new Date());
        String outputParams = xstream.toXML(logWrapper);
        logModel.setOutputParam(outputParams);
        LogWrapper logWrappertoSave = new LogWrapperImpl();
        logWrappertoSave.setLogModel(logModel);
        this.insertLogRequiresNewTransaction(logWrappertoSave);

        //log activities
        logger.info("viewTransactionLog End... ");

        return logWrapper;
    }

    public void setLogDao(LogDAO logDao) {
        this.logDao = logDao;
    }

    public void setLogListViewDAO(LogListViewDAO logListViewDAO) {
        this.logListViewDAO = logListViewDAO;
    }
}
