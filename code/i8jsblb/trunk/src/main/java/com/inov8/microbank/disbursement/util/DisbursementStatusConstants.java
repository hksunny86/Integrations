package com.inov8.microbank.disbursement.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by AtieqRe on 2/22/2017.
 */
public class DisbursementStatusConstants {

    public static final Integer STATUS_UN_PARSED = 0;
    public static final Integer STATUS_PARSING = 1;
    public static final Integer STATUS_PARSING_FAILED = 2;
    public static final Integer STATUS_SAVING_FAILED = 3;
    public static final Integer STATUS_PARSED = 4;
    public static final Integer STATUS_WALK_IN_CREATION_IN_PROGRESS = 5;
    public static final Integer STATUS_WALK_IN_CREATION_PAUSED = 6;
    public static final Integer STATUS_READY_TO_DISBURSE = 7;
    public static final Integer STATUS_DISBURSEMENT_IN_PROGRESS = 8;
    public static final Integer STATUS_DISBURSED = 9;
    public static final Integer STATUS_CANCELED = 10;
    public static final Integer STATUS_ON_HOLD = 11;
    public static final Map<Integer, String> map = new LinkedHashMap<>(10);

    static{
        map.put(STATUS_UN_PARSED, "Not parsed yet");
        map.put(STATUS_PARSING, "Parsing in process");
        map.put(STATUS_PARSING_FAILED, "Parsing failed");
        map.put(STATUS_SAVING_FAILED, "Saving failed");
        map.put(STATUS_PARSED, "Parsing completed");
        map.put(STATUS_WALK_IN_CREATION_IN_PROGRESS, "Walk-in Creation in progress");
        map.put(STATUS_WALK_IN_CREATION_PAUSED, "Walk-in Creation paused");
        map.put(STATUS_READY_TO_DISBURSE, "Ready to disburse");
        map.put(STATUS_DISBURSEMENT_IN_PROGRESS, "Disbursement in progress");
        map.put(STATUS_DISBURSED, "Disbursed");
        map.put(STATUS_CANCELED, "Cancelled");
        map.put(STATUS_ON_HOLD, "On hold");
    }
}

