package com.inov8.integration.middleware.controller;

import com.inov8.integration.middleware.translitration.pdu.TransliterationVo;

import java.io.Serializable;

/**
 * Created by inov8 on 4/11/2017.
 */
public interface TransliterationController extends Serializable {
    TransliterationVo transliteration(TransliterationVo transliterationVo) throws RuntimeException;
}
