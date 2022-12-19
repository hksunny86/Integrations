package com.inov8.microbank.faq.command;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.faq.model.FaqCatalogDetailViewModel;
import com.inov8.microbank.faq.model.FaqCatalogModel;
import com.inov8.microbank.faq.service.FaqManager;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FaqCommand extends BaseCommand {

    private final Log logger = LogFactory.getLog(FaqCommand.class);
    private String faqVersionNo;
    private List<FaqCatalogDetailViewModel> faqList;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        faqVersionNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FAQ_VERSION_NO);
    }

    @Override
    public void doValidate() throws CommandException {
        ValidationErrors validationErrors = new ValidationErrors();

        validate(validationErrors);

        if (validationErrors.hasValidationErrors()) {
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH, new Throwable());
        }
    }

    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        ValidatorWrapper.doRequired(faqVersionNo, validationErrors, "FAQ Version No");

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        try {

            FaqManager faqManager = this.commonCommandManager.getFaqManager();
            FaqCatalogModel faqCatalogModel = faqManager.findFaqCatalog();

            if (faqCatalogModel != null && faqCatalogModel.getVersionNo() != Integer.parseInt(faqVersionNo)) {
                faqVersionNo = faqCatalogModel.getVersionNo().toString();
                faqList = faqManager.loadFaqCatalogDetail(faqCatalogModel.getFaqCatalogId());
            }
        }

        catch (Exception ex) {
            handleException(ex);
        }
    }

    @Override
    public String response() {

        StringBuilder response = new StringBuilder();
        Map<String, Object> map = new HashMap<>(1);
        map.put(XMLConstants.ATTR_CAT_VER, faqVersionNo);

        response.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_FAQS, map, false));

        if(!CollectionUtils.isEmpty(faqList)) {
            Map<String, Object> faqs = new LinkedHashMap<>(faqList.size());
            for (FaqCatalogDetailViewModel vo : faqList) {
                faqs.put(XMLConstants.ATTR_ID, vo.getQuestionNo());
                faqs.put(XMLConstants.ATTR_QUESTION, vo.getQuestion());

                response.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_FAQ, faqs, false));
                response.append(StringEscapeUtils.escapeXml(vo.getAnswer()));
                response.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_FAQ, false));
            }
        }

        response.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_FAQS, false));

        return response.toString();
    }
}
