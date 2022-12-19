package com.inov8.integration.middleware.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Zeeshan Ahmad on 10/6/2016.
 */
@Controller
public class NadraVerificationWebController {


    @RequestMapping("/test")
    public ModelAndView helloWorld() {
        ModelAndView model = new ModelAndView("verify");
        populateDefaultModel(model);
        return model;
    }


    private void populateDefaultModel(ModelAndView model) {

        Map<String, String> indexList = new LinkedHashMap<String, String>();
        indexList.put("1", "Right Thumb");
        indexList.put("2", "Right Index Finger");
        indexList.put("3", "Right Middle Finger");
        indexList.put("4", "Right Ring Finger");
        indexList.put("5", "Right Little Finger");
        indexList.put("6", "Left Thumb");
        indexList.put("7", "Left Index Finger");
        indexList.put("8", "Left Middle Finger");
        indexList.put("9", "Left Ring Finger");
        indexList.put("10", "Left Little Finger");
        model.addObject("indexList", indexList);

        Map<String, String> templateList = new LinkedHashMap<String, String>();
        templateList.put("ISO_19794_2", "ISO_19794_2");
        templateList.put("RAW_IMAGE", "RAW_IMAGE");
        model.addObject("templateList", templateList);

        Map<String, String> areaList = new LinkedHashMap<String, String>();
        areaList.put("khyber-pakhtunkhwa", "Khyber-Pakhtunkhwa");
        areaList.put("fata", "Fata");
        areaList.put("punjab", "Punjab");
        areaList.put("sindh", "Sindh");
        areaList.put("baluchistan", "Baluchistan");
        areaList.put("islamabad", "Islamabad");
        areaList.put("gilgit-baltistan", "Gilgit-Baltistan");
        areaList.put("azad Kashmir", "Azad Kashmir");
        model.addObject("areaList", areaList);

    }

}
