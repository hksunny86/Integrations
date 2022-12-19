package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;

/**
 * Created by Attique on 9/6/2018.
 */
public interface ThirdPartyCashOutAdviceSender {
    public void send(SwitchWrapper switchWrapper) throws FrameworkCheckedException;
}
