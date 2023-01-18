package com.inov8.microbank.debitcard.controller;

import com.inov8.microbank.debitcard.service.DebitCardManager;
import com.inov8.microbank.schedulers.DebitCardAnnualFeeDeductionScheduler;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DebitCardAnnualFeeSchedulerController {
    private static final Logger LOGGER = Logger.getLogger(DebitCardFeeSchedulerController.class);

    private DebitCardManager debitCardManager;
    private DebitCardAnnualFeeDeductionScheduler debitCardAnnualFeeDeductionScheduler;

    @RequestMapping(value = "/debitCardAnnualFeeSchedulerController")
    public ModelAndView runSchedulerManually(HttpServletRequest request, Model model)
    {
        LOGGER.info("*********** Starting Manually DebitCard Annual Fee Scheduler ***********");
        try
        {
            debitCardAnnualFeeDeductionScheduler.init();
        }
        catch(Exception ex)
        {
            model.addAttribute("messages", "Unable perform selected operation.");
            LOGGER.error("Unable perform selected operation." + ex.getMessage(),ex);
        }
        LOGGER.info("*********** End of Manually DebitCard Annual Fee Scheduler ***********");
        request.setAttribute("isManual", "true");
        String view = "redirect:schedulersmanagement.html";
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.addObject("isManual","true");
        return modelAndView;
    }

    public DebitCardManager getDebitCardManager() {
        return debitCardManager;
    }

    public void setDebitCardManager(DebitCardManager debitCardManager) {
        this.debitCardManager = debitCardManager;
    }

    public DebitCardAnnualFeeDeductionScheduler getDebitCardAnnualFeeDeductionScheduler() {
        return debitCardAnnualFeeDeductionScheduler;
    }

    public void setDebitCardAnnualFeeDeductionScheduler(DebitCardAnnualFeeDeductionScheduler debitCardAnnualFeeDeductionScheduler) {
        this.debitCardAnnualFeeDeductionScheduler = debitCardAnnualFeeDeductionScheduler;
    }
}
