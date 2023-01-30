package com.inov8.microbank.ivr.manager;


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface IVRMpinVerificationManager {
    SearchBaseWrapper loadIVRMpinVerificationData(SearchBaseWrapper var1) throws FrameworkCheckedException;
}