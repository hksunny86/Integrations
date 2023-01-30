package com.inov8.microbank.ivr.manager;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.IVRMpinVerificationViewModel;
import com.inov8.microbank.ivr.dao.IVRMpinVerificationDAO;

// @Created On 1/27/2023 : Friday
// @Created By muhammad.aqeel
public class IVRMpinVerificationManagerImpl implements IVRMpinVerificationManager {
    private IVRMpinVerificationDAO ivrMpinVerificationDAO;

    public IVRMpinVerificationManagerImpl() {
    }

    public SearchBaseWrapper loadIVRMpinVerificationData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        IVRMpinVerificationViewModel model = (IVRMpinVerificationViewModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<IVRMpinVerificationViewModel> customList = this.ivrMpinVerificationDAO.findByExample(model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(), new String[0]);
        if (customList != null) {
            searchBaseWrapper.setCustomList(customList);
        }

        return searchBaseWrapper;
    }

    public void setIvrMpinVerificationDAO(IVRMpinVerificationDAO ivrMpinVerificationDAO) {
        this.ivrMpinVerificationDAO = ivrMpinVerificationDAO;
    }
}
