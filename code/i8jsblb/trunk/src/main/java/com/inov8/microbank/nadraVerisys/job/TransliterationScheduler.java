package com.inov8.microbank.nadraVerisys.job;

import com.inov8.integration.middleware.translitration.pdu.TransliterationVo;
import com.inov8.microbank.nadraVerisys.model.VerisysDataModel;
import com.inov8.microbank.nadraVerisys.service.VerisysDataManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionException;
import org.springframework.util.StopWatch;

import java.util.List;

/**
 * Created By    : Abu Turab <br>
 * Creation Date : May 25, 2017 06:11 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>purpose of document is to translate Urdu data returned from NADRA which was saved incase of BVS account opening in VERISYS_DATA, into English and save to respective customer data.
 */

public class TransliterationScheduler  {

    private static final Logger LOGGER = Logger.getLogger(TransliterationScheduler.class);
    private VerisysDataManager verisysDataManager;

    public TransliterationScheduler() {
    }


    protected void process() throws JobExecutionException {
        StopWatch stopWatch = new StopWatch("TransliterationJob");
        stopWatch.start("TransliterationJob");
        LOGGER.info("*********** Executing TransliterationScheduler ***********");

        try {
            List<VerisysDataModel> verisysDataModelList = verisysDataManager.getVerisysDataForTransliteration();
            LOGGER.info("************ Total " + verisysDataModelList.size() + " records fetched for Transliteration ************");

            if (CollectionUtils.isNotEmpty(verisysDataModelList)) {
                List<TransliterationVo> translatedVOs = verisysDataManager.translateUrduToEnglish(verisysDataModelList);
                LOGGER.info("************ Total " + translatedVOs.size() + " records translated out of " + verisysDataModelList.size() + " ************");

                verisysDataManager.updateTranslatedData(translatedVOs);
                LOGGER.info("************ Total " + translatedVOs.size() + " records updated from Urdu to English out of " + verisysDataModelList.size() + " ************");
            }
        }

        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info("*********** Finished Executing TransliterationScheduler ***********");

        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());
    }


    public void setVerisysDataManager(VerisysDataManager verisysDataManager) {
        this.verisysDataManager = verisysDataManager;
    }

}
