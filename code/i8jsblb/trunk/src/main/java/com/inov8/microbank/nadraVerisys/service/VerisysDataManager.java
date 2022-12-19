package com.inov8.microbank.nadraVerisys.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.integration.middleware.translitration.pdu.TransliterationVo;
import com.inov8.integration.vo.NadraIntegrationVO;
import com.inov8.microbank.fonepay.vo.NADRADataVO;
import com.inov8.microbank.nadraVerisys.model.VerisysDataModel;

import java.util.List;

/**
 * Created by AtieqRe on 11/8/2016.
 */
public interface VerisysDataManager {
    VerisysDataModel saveOrUpdate(VerisysDataModel verisysDataModel);

    VerisysDataModel loadVerisysDataModel(String cnic);
    VerisysDataModel loadVerisysDataModel(Long appUserId);

    void markClosedByAppUserId(Long appUserId) throws FrameworkCheckedException;

    VerisysDataModel prepareVerisysDataModelByNadraIntegrationVO(NadraIntegrationVO nadraIntegrationVO);

    List<VerisysDataModel> getVerisysDataForTransliteration() throws FrameworkCheckedException;

    List<TransliterationVo> translateUrduToEnglish(List<VerisysDataModel> vdmList) throws Exception;

    void updateTranslatedData(List<TransliterationVo> transliterationVoList) throws Exception;
    void saveNadraData(VerisysDataModel vModel, NADRADataVO nadraVO) throws Exception;
}
